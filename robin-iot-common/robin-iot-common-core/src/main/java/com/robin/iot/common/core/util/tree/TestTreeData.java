package com.robin.iot.common.core.util.tree;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

/**
 * 测试树形结构数据
 *
 * @author zhao peng
 * @date 2024/9/12 0:12
 **/
public class TestTreeData {

    public static void main(String[] args) {

        Area hd = new Area(1L, 0L, "hd", "HD", 0, "0");
        Area jyy = new Area(10L, 1L, "jyy", "江雅园办公大楼", 0, "0,1");
        Area zy = new Area(20L, 1L, "hd", "中盈办公大楼", 10, "0,1");
        Area f1 = new Area(101L, 10L, "F1", "一楼", 10, "0,1,10");
        Area f2 = new Area(102L, 10L, "F2", "二楼", 20, "0,1,10");
        Area f3 = new Area(103L, 10L, "F3", "三楼", 30, "0,1,10");
        Area f4 = new Area(104L, 10L, "F4", "四楼", 40, "0,1,10");
        Area f5 = new Area(105L, 10L, "F5", "五楼", 50, "0,1,10");
        Area f6 = new Area(106L, 10L, "F6", "六楼", 60, "0,1,10");
        Area f7 = new Area(107L, 10L, "F7", "六楼", 70, "0,1,10");
        Area b1 = new Area(108L, 10L, "B1", "负一楼", 80, "0,1,10");
        Area b2 = new Area(109L, 10L, "B2", "负二楼", 90, "0,1,10");
        Area fire = new Area(110L, 10L, "Fire", "消控室", 100, "0,1,10");

        List<Area> areas = List.of(hd, jyy, zy, f1, f2, f3, f4, f5, f6, f7, b1, b2, fire);

        List<Area> treeAreas = TreeUtils.buildTree(areas, node -> node.getParentId().equals(0L), (parent, child) -> parent.getId().equals(child.getParentId()), Area::setSubAreas);
        System.out.println(JSONObject.toJSONString(treeAreas));
        TreeUtils.postOrder(treeAreas, node -> System.out.println(JSONObject.toJSONString(node)), Area::getSubAreas);
        System.out.println(JSONObject.toJSONString(TreeUtils.flat(treeAreas, Area::getSubAreas, node -> node.setSubAreas(null))));


    }

}
