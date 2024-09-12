package com.robin.iot.common.core.util.tree;

import java.util.*;
import java.util.function.*;
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

    /**
     * 将树结构数据打平
     *
     * @param tree 树结构数据
     * @param getChildren 设置子节点方法
     * @param setChildren 设置夏季方法
     * @return 打平后的数据
     * @param <E> 泛型节点
     */
    public static <E> List<E> flat(List<E> tree, Function<E, List<E>> getChildren, Consumer<E> setChildren) {
        List<E> data = new ArrayList<>();
        postOrder(tree, node -> {
            setChildren.accept(node);
            data.add(node);
        }, getChildren);
        return data;
    }

    /**
     * 后序遍历
     * @param tree 树结构数据
     * @param consumer 遍历后对单个节点的处理逻辑
     * @param setChildren 给节点设置子节点的逻辑
     * @param <E> 泛型节点
     */
    public static <E> void postOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> setChildren) {
        for (E node : tree) {
            List<E> children = setChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                postOrder(children, consumer, setChildren);
            }
            consumer.accept(node);
        }
    }

    /**
     * 层序遍历
     * @param tree 树结构数据
     * @param consumer 遍历后对单个节点的处理逻辑
     * @param setChildren 给节点设置子节点的逻辑
     * @param <E> 泛型节点
     */
    public static <E> void levelOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> setChildren) {
        Queue<E> queue = new LinkedList<>(tree);
        while (!queue.isEmpty()) {
            E node = queue.poll();
            consumer.accept(node);
            List<E> children = setChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }
        }
    }

    /**
     * 前序遍历
     * @param tree 树结构数据
     * @param consumer 遍历后对单个节点的处理逻辑
     * @param setChildren 给节点设置子节点的逻辑
     * @param <E> 泛型节点
     */
    public static <E> void preOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> setChildren) {
        tree.forEach(node -> {
            consumer.accept(node);
            List<E> children = setChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                preOrder(children, consumer, setChildren);
            }
        });
    }

    /**
     * 树中的所有子节点按 comparator 排序
     * @param tree 树结构数据
     * @param comparator 排序器
     * @param getChildren 获取子节点方法
     * @return 排序后的树结构数据
     * @param <E> 泛型节点
     */
    public static <E> List<E> sort(List<E> tree, Comparator<E> comparator, Function<E, List<E>> getChildren) {
        tree.forEach(node -> {
            List<E> children = getChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                children.sort(comparator);
            }
        });
        tree.sort(comparator);
        return tree;
    }

}
