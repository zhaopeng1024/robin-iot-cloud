package com.robin.iot.common.mybatis.util;

import java.util.Arrays;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author zhao peng
 * @date 2024/10/7 22:46
 **/
public final class StringUtils {

    public static final String COMMA = ",";

    /**
     * 逗号分隔的字符串转 List<String>
     * @param str 逗号分隔的字符
     * @return List<String> 实例
     */
    public static List<String> commaStringToList(String str) {
        if (str == null) {
            return null;
        }
        return Arrays.asList(str.trim().split(COMMA));
    }

    /**
     * List<String> 转逗号分隔的字符串
     * @param strList List<String>
     * @return 逗号分隔的字符串
     */
    public static String listToCommaString(List<String> strList) {
        if (strList == null) {
            return null;
        }
        if (strList.isEmpty()) {
            return "";
        }
        return String.join(COMMA, strList);
    }

}
