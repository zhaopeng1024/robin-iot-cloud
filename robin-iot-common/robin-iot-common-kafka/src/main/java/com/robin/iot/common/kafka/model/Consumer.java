package com.robin.iot.common.kafka.model;

import lombok.Data;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * kafka 消费者
 *
 * @author zhaopeng
 * @date 2024/7/16 12:02
 */
@Data
public class Consumer {

    /**
     * 消费者监听容器工厂。
     */
    private String containerFactory;

    /**
     * kafka 服务器。
     */
    private List<String> bootstrapServers = new ArrayList<String>() {{
        add("127.0.0.1:9092");
    }};

    /**
     * Key 的反序列化器，二进制的消息 Key 转换成具体的类型。
     */
    private Class<?> keyDeserializer = StringSerializer.class;

    /**
     * Value 的反序列化器，二进制的消息内容转换成具体的类型。
     */
    private Class<?> valueDeserializer = StringSerializer.class;

    /**
     * 标识消费者的消费组。
     */
    private String groupId = "default-group";

    /**
     * 心跳与消费者协调的间隔时间。
     */
    private Integer heartbeatInterval = 3000;

    /**
     * 每次 fetch 请求时，server 应该返回的最小字节数。
     * <p>
     * 如果没有足够的数据返回，请求会等待，直到足够的数据才会返回。
     * <p>
     * 默认为 1。
     */
    private Integer fetchMinSize = 1;

    /**
     * fetch 请求发给 broker 后，在 broker 中可能会被阻塞（ 当 topic 中 records 的总 size 小于 fetch.min.size 时），
     * 此时这个 fetch 请求耗时就会比较长。
     * <p>
     * 这个配置就是来配置 consumer 最多等待 response 多久。
     */
    private Integer fetchMaxWait = 100;

    /**
     * 会话的超时限制。
     */
    private Integer sessionTimeoutMs = 6000;

    /**
     * 需要在 session.timeout.ms 这个时间限制内处理完的条数。
     * <p>
     * 默认为 500。
     */
    private Integer maxPollRecords = 500;

    /**
     * 是否自动同步 offset，默认 true。
     */
    private Boolean enableAutoCommit = Boolean.TRUE;

    /**
     * 没有初始化的 offset 消费 earliest latest none。
     * <p>
     * 默认为 latest。
     */
    private String autoOffsetReset = "latest";
}
