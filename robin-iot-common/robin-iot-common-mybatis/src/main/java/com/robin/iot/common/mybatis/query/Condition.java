package com.robin.iot.common.mybatis.query;

import java.lang.annotation.*;

/**
 * 查询条件注解
 * <p>
 * 封装了 EQ、NE、LIKE、GT、LT、GE、LE、IN、BETWEEN 等条件注解
 *
 * @author zhao peng
 * @date 2024/10/7 21:04
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Condition {

    /**
     * 等于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface EQ {
        Logic logic() default Logic.AND;
    }

    /**
     * 不等于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface NE {
        Logic logic() default Logic.AND;
    }

    /**
     * 模糊查询
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface LIKE {
        Logic logic() default Logic.AND;
    }

    /**
     * 大于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface GT {
        Logic logic() default Logic.AND;
    }

    /**
     * 小于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface LT {
        Logic logic() default Logic.AND;
    }

    /**
     * 大于等于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface GE {
        Logic logic() default Logic.AND;
    }

    /**
     * 小于等于
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface LE {
        Logic logic() default Logic.AND;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface IN {
        Logic logic() default Logic.AND;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface BETWEEN {
        Logic logic() default Logic.AND;
    }

}
