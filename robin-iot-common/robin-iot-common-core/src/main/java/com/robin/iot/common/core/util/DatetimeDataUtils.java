package com.robin.iot.common.core.util;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 日期时间数据填充补齐工具类
 *
 * @author zhao peng
 * @date 2024/9/24 21:41
 **/
public final class DatetimeDataUtils {

    public static final String[] HOURS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    public static final String[] DAYS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    public static final String[] MONTHS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    public static List<Data> fill(List<Data> data, FillType fillType, Object defaultValue) {
        Map<String, Data> dataMap = data.stream().collect(Collectors.toMap(Data::getDateTime, Function.identity(), (v1, v2) -> v2));
        String[] fillArray = null;
        switch (fillType) {
            case HOUR:
                fillArray = HOURS;
                break;
            case DAY:
                fillArray = DAYS;
                break;
            case MONTH:
                fillArray = MONTHS;
                break;
            default:
                Assert.isTrue(true, "填充类型有误");
                break;
        }
        List<Data> fullData = new ArrayList<>(fillArray.length);
        for (int i = 0, j = 0; i < fillArray.length; i++) {
            if (!dataMap.containsKey(fillArray[i])) {
                fullData.add(new Data(fillArray[i], defaultValue));
            } else {
                fullData.add(data.get(j++));
            }
        }
        return fullData;
    }

    public static class Data {
        private String dateTime;
        private Object value;

        public Data(String dateTime, Object value) {
            this.dateTime = dateTime;
            this.value = value;
        }

        public String getDateTime() {
            return dateTime;
        }
        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
        public Object getValue() {
            return value;
        }
        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "dateTime='" + dateTime + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    public enum FillType {
        HOUR,
        DAY,
        MONTH
    }

    public static void main(String[] args) {
        List<Data> data = new ArrayList<>();
        data.add(new Data("1", 100));
        data.add(new Data("12", 200));
        data.add(new Data("13", 300));
        data.add(new Data("14", 200));
        List<Data> fullData = DatetimeDataUtils.fill(data, FillType.MONTH, 0);
        fullData.forEach(System.out::println);
    }
}
