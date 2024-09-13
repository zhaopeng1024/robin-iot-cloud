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
@SuppressWarnings("unused")
public final class TreeUtils {

    /**
     * 构建树形结构
     * <p>
     * 底层使用递归，大数据量下效率较低，但比较简单
     *
     * @param data 要构建成树形结构的列表数据
     * @param rootCheck 根节点判断函数，比如： node -> node.getId() == 0L
     * @param parentCheck 上级节点判断函数，比如：(parent, node) -> parent.getId().equals(node.getParentId())
     * @param setChildren 节点设置子节点方法，比如：(parent, children) -> parent.setChildren(children)
     * @return 树形结构数据列表
     * @param <E> 泛型实体
     */
    public static <E> List<E> buildTree(List<E> data, Predicate<E> rootCheck, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setChildren) {
        return data.stream().filter(rootCheck).peek(item -> setChildren.accept(item, buildChildren(item, data, parentCheck, setChildren))).collect(Collectors.toList());
    }

    /**
     * 构建树形结构
     * <p>
     * 使用 Map，大数据量场景下，效率比较高
     *
     * @param data 要构建成树形结构的列表数据
     * @param getParentId 节点获取上级 ID 的函数
     * @param getId 节点获取 ID 的函数
     * @param rootCheck 根节点判断函数，比如：node -> node.getParentId() == null，或者 node -> node.getId() == 0L 等
     * @param setChildren 节点设置子节点的函数，比如：(parent, children) -> parent.setChildren(children)
     * @return 树形结构数据列表
     * @param <I> 节点 ID 字段泛型
     * @param <E> 节点泛型
     */
    public static <I, E> List<E> buildTree(List<E> data, Function<E, I> getParentId, Function<E, I> getId, Predicate<E> rootCheck, BiConsumer<E, List<E>> setChildren) {
        // 构建父级数据 Map，使用 Optional 考虑 parentId 为 null 的情况
        LinkedHashMap<Optional<I>, List<E>> parentNodeMap = data.stream().collect(Collectors.groupingBy(
                node -> Optional.ofNullable(getParentId.apply(node)),
                LinkedHashMap::new,
                Collectors.toList()
        ));
        List<E> tree = new ArrayList<>();
        data.forEach(node -> {
            setChildren.accept(node, Optional.ofNullable(parentNodeMap.get(Optional.ofNullable(getId.apply(node)))).orElse(List.of()));
            if (rootCheck.test(node)) {
                tree.add(node);
            }
        });
        return tree;
    }

    private static <E> List<E> buildChildren(E parent, List<E> data, BiFunction<E, E, Boolean> parentCheck, BiConsumer<E, List<E>> setChildren) {
        return data.stream().filter(item -> parentCheck.apply(parent, item)).peek(item -> setChildren.accept(item, buildChildren(item, data, parentCheck, setChildren))).collect(Collectors.toList());
    }

    /**
     * 将树形结构数据打平
     *
     * @param tree 要打平的树形结构数据
     * @param getChildren 节点中获取子节点的函数，比如：node -> node.getChildren()
     * @param setChildren 打平后节点设置字节点的函数，比如：node -> node.setChildren(null)
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
     * 对树形结构数据进行后序遍历
     *
     * @param tree 要遍历的树形结构数据
     * @param consumer 遍历过程中对单个节点的处理函数，比如：node -> System.out.println(node)
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
     * @param <E> 泛型节点
     */
    public static <E> void postOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getChildren) {
        tree.forEach(node -> {
            List<E> children = getChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                postOrder(children, consumer, getChildren);
            }
            consumer.accept(node);
        });
    }

    /**
     * 对树形结构数据进行层序遍历
     *
     * @param tree 要遍历的树形结构数据
     * @param consumer 遍历过程中对单个节点的处理函数，比如：node -> System.out.println(node)
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
     * @param <E> 泛型节点
     */
    public static <E> void levelOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getChildren) {
        Queue<E> queue = new LinkedList<>(tree);
        while (!queue.isEmpty()) {
            E node = queue.poll();
            consumer.accept(node);
            List<E> children = getChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                queue.addAll(children);
            }
        }
    }

    /**
     * 对树形结构数据进行前序遍历
     * @param tree 要遍历的树形结构数据
     * @param consumer 遍历过程中对单个节点的处理函数，比如：node -> System.out.println(node)
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
     * @param <E> 泛型节点
     */
    public static <E> void preOrder(List<E> tree, Consumer<E> consumer, Function<E, List<E>> getChildren) {
        tree.forEach(node -> {
            consumer.accept(node);
            List<E> children = getChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                preOrder(children, consumer, getChildren);
            }
        });
    }

    /**
     * 树形结构数据中的所有子节点按 comparator 排序
     *
     * @param tree 树形结构数据
     * @param comparator 排序器，比如：(n1, n2) -> n1.getSort() - n2.getSort()、Comparator.comparing(node -> node.getSort()) 等
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
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

    /**
     * 树形结构数据中过滤出满足 predicate 的节点
     * @param tree 要过滤的树形结构数据
     * @param predicate 节点过滤条件，比如：node -> node.getSort() < 50
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
     * @return 过滤后的树形结构数据
     * @param <E> 泛型节点
     */
    public static <E> List<E> filter(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren) {
        Iterator<E> iterator = tree.iterator();
        while (iterator.hasNext()) {
            E node = iterator.next();
            if (predicate.test(node)) {
                List<E> children = getChildren.apply(node);
                if (children != null && !children.isEmpty()) {
                    filter(children, predicate, getChildren);
                }
            } else {
                iterator.remove();
            }
        }
        return tree;
    }

    /**
     * 树形结构数据中按条件搜索，并删除不满足条件的节点，返回剩余的树形结构数据
     * @param tree 要搜索的树形结构数据
     * @param predicate 节点搜索条件，比如：node -> node.getId = 105L
     * @param getChildren 节点获取子节点的函数，比如：node -> node.getChildren()
     * @param <E> 泛型节点
     */
    public static <E> void search(List<E> tree, Predicate<E> predicate, Function<E, List<E>> getChildren) {
        Iterator<E> iterator = tree.iterator();
        while (iterator.hasNext()) {
            E node = iterator.next();
            List<E> children = getChildren.apply(node);
            if (children != null && !children.isEmpty()) {
                search(children, predicate, getChildren);
            }
            if (!predicate.test(node) && (children == null || children.isEmpty())) {
                iterator.remove();
            }
        }
    }

}
