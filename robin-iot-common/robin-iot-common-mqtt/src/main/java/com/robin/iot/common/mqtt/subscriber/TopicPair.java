package com.robin.iot.common.mqtt.subscriber;

import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主题对，用于处理 MQTT 的订阅主题
 *
 * @author zhao peng
 * @date 2024/7/18 22:54
 **/
public class TopicPair {

    private final static Pattern TO_PATTERN = Pattern.compile("\\{(\\w+)}");
    private final static Pattern TO_TOPIC = Pattern.compile("[^/]*\\{\\w+}[^/]*");
    private final static String STRING_PARAM = "([^/]+)";
    private final static String NUMBER_PARAM = "(\\\\d+(:?\\\\.\\\\d+)?)";

    private String topic;
    private Pattern pattern;
    private TopicParam[] params;
    @Getter
    private int qos;
    private String group;

    public static TopicPair of(String topic, int qos) {
        return of(topic, qos, null, new HashMap<>());
    }

    /**
     * 该函数用于创建一个 TopicPair 对象，该对象包含有关 MQTT 主题的信息，如主题名称、QoS 级别、组信息和参数类型映射。
     * <p>
     * 函数首先验证主题名称不为空且 QoS 级别在 0 到 2 之间。
     * <p>
     * 然后根据主题是否包含占位符来处理。如果主题包含占位符，则将其转换为模式，并解析出参数列表。
     * <p>
     * 否则，直接使用主题名称。最后，验证主题名称的格式，并设置QoS级别和组信息，返回创建的TopicPair对象。
     * @param topic 主题名称
     * @param qos QoS 级别
     * @param group 组
     * @param paramTypeMap 参数类型映射
     * @return TopicPair 对象
     */
    public static TopicPair of(String topic, int qos, String group, HashMap<String, Class<?>> paramTypeMap) {
        Assert.isTrue(topic != null && !topic.isEmpty(), "topic cannot be blank");
        Assert.isTrue(qos >= 0, "qos min value is 0");
        Assert.isTrue(qos <= 2, "qos max value is 2");
        TopicPair topicPair = new TopicPair();
        if (topic.contains("{")) {
            LinkedList<TopicParam> params = new LinkedList<>();
            topicPair.pattern = toPattern(topic, params, paramTypeMap);
            topicPair.params = params.toArray(new TopicParam[0]);
            topicPair.topic = TO_TOPIC.matcher(topic).replaceAll("+");
        } else {
            topicPair.topic = topic;
        }
        MqttTopic.validate(topicPair.topic, true);
        topicPair.qos = qos;
        topicPair.group = group;
        return topicPair;
    }

    /**
     * 该函数将给定的主题字符串转换为一个模式，用于后续的匹配操作。
     * <p>
     * 函数首先通过替换主题字符串中的特定符号，得到一个模式字符串。
     * <p>
     * 然后，利用正则表达式匹配器对模式字符串进行匹配，找到所有的参数名。
     * 对于每个参数名，根据其在参数类型映射表中的类型，选择对应的参数模式。
     * <p>
     * 最后，将匹配结果拼接到一个字符串构建器中，并在开头和结尾添加正则表达式的开始和结束符号。
     * 最终，将构建好的字符串编译为一个正则表达式模式，并返回该模式。
     * @param topic 主题字符串
     * @param params 参数列表
     * @param paramTypeMap 参数类型映射表
     * @return 正则表达式模式
     */
    private static Pattern toPattern(String topic, LinkedList<TopicParam> params, HashMap<String, Class<?>> paramTypeMap) {
        String pattern = replaceSymbols(topic);
        Matcher matcher = TO_PATTERN.matcher(pattern);
        StringBuilder builder = new StringBuilder("^");
        int group = 1;
        while (matcher.find()) {
            String paramName = matcher.group(1);
            params.add(new TopicParam(paramName, group));
            if (paramTypeMap.containsKey(paramName)) {
                Class<?> paramType = paramTypeMap.get(paramName);
                if (Number.class.isAssignableFrom(paramType)) {
                    matcher.appendReplacement(builder, NUMBER_PARAM);
                    ++group;
                } else {
                    matcher.appendReplacement(builder, STRING_PARAM);
                }
            } else {
                matcher.appendReplacement(builder, STRING_PARAM);
            }
            ++group;
        }
        matcher.appendTail(builder);
        builder.append("$");
        return Pattern.compile(builder.toString());
    }

    /**
     * 获取主题字符串，如果启用了共享订阅，则返回共享订阅的主题字符串，否则返回原始主题字符串。
     * @param enableShare 是否启用共享订阅
     * @return 主题字符串
     */
    public String getTopic(boolean enableShare) {
        if (enableShare) {
            if (this.group != null && !this.group.isBlank()) {
                return "$share/" + this.group + "/" + this.topic;
            }
        }
        return this.topic;
    }

    /**
     * 判断给定的主题字符串是否与当前主题匹配。
     * @param topic 主题字符串
     * @return 是否匹配
     */
    public boolean isMatched(String topic) {
        if (this.pattern != null) {
            return pattern.matcher(topic).matches();
        } else {
            return MqttTopic.isMatched(this.topic, topic);
        }
    }

    /**
     * 获取匹配给定主题字符串的参数值映射。
     * @param topic 主题字符串
     * @return 参数值映射
     */
    public HashMap<String, String> getPathValueMap(String topic) {
        HashMap<String, String> map = new HashMap<>();
        if (pattern != null) {
            Matcher matcher = pattern.matcher(topic);
            if (matcher.find()) {
                for (TopicParam param : params) {
                    String group = matcher.group(param.getAt());
                    map.put(param.getName(), group);
                }
            }
        }
        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicPair topicPair = (TopicPair) o;
        return Objects.equals(topic, topicPair.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic);
    }

    /**
     * 该函数用于获取 TopicPair 对象的排序顺序。如果 TopicPair 对象没有模式，则返回 1；
     * 否则返回 -params.length 。
     * @return 排序顺序
     */
    public int order() {
        return this.pattern == null ? 1 : -params.length;
    }

    /**
     * 该函数用于将给定字符串中的特定字符替换为对应的转义字符或正则表达式。
     * 在字符串topic中，特定字符包括$ ^ . ? * | ( ) [ ] \ + #。具体替换规则如下：
     * <li>
     * '$','^','.','?','*','|','(',')','[',']','\' 这些字符在正则表达式中具有特殊含义，为了保持其字面意义，
     * 需要在前面加上 '\' 进行转义。
     * <p>
     * '+' 字符被替换为 [^/]+，表示匹配一个或多个不是 '/' 的字符。
     * <p>
     * '#' 字符被替换为 .* ，表示匹配任意多个任意字符。
     * <p>
     * 最终，函数返回替换后的字符串。
     * @param topic 主题
     * @return 替换后的字符串
     */
    private static String replaceSymbols(String topic) {
        StringBuilder sb = new StringBuilder();
        char[] chars = topic.toCharArray();
        for (char ch : chars) {
            switch (ch) {
                case '$':
                case '^':
                case '.':
                case '?':
                case '*':
                case '|':
                case '(':
                case ')':
                case '[':
                case ']':
                case '\\':
                    sb.append('\\').append(ch);
                    break;
                case '+':
                    sb.append("[^/]+");
                    break;
                case '#':
                    sb.append(".*");
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }
}
