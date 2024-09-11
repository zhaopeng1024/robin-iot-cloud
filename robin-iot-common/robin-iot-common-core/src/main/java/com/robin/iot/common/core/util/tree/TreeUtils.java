package com.robin.iot.common.core.util.tree;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 数结构工具类
 *
 * @author zhao peng
 * @date 2024/9/11 22:45
 **/
public final class TreeUtils {

    /**
     * 构建树形结构
     * @param data 列表数据
     * @param rootCheck 根节点判断条件
     * @param parentCheck 父节点判断条件
     * @param setChildren 设置子节点方法
     * @return 树形结构
     * @param <E> 泛型实体
     */
    public static <E> List<E> buildTree(List<E> data, Predicate<E> rootCheck, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setChildren) {
        return data.stream().filter(rootCheck).peek(item -> setChildren.accept(item, buildChildren(item, data, parentCheck, setChildren))).collect(Collectors.toList());
    }

    private static <E> List<E> buildChildren(E parent, List<E> data, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setChildren) {
        return data.stream().filter(item -> parentCheck.apply(parent, item)).peek(item -> setChildren.accept(item, buildChildren(item, data, parentCheck, setChildren))).collect(Collectors.toList());
    }

}
