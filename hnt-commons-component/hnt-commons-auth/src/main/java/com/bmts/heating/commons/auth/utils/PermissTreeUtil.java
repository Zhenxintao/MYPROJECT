package com.bmts.heating.commons.auth.utils;



import com.bmts.heating.commons.basement.model.db.entity.SysPermission;

import java.util.ArrayList;
import java.util.List;

public class PermissTreeUtil {
    /**
     * 获取目录根节点
     *
     * @return
     */
    public static List<SysPermission> getTree(List<SysPermission> nodeList) {
        List<SysPermission> list = new ArrayList<>();
        for (SysPermission t : nodeList) {
            if (t.getPid().equals(0)) {
                list.add(t);
                t.setChildren(getChilNode(nodeList,t));
            }
        }
        return list;
    }

    /**
     * 根据pid查询子节点
     *
     * @param nodeList
     * @return
     */
    public static List<SysPermission> getChilNode(List<SysPermission> nodeList, SysPermission t) {
        //  为每一个树节点都创建一个Children
        t.setChildren(new ArrayList<>());
        //  这里获取是为了放入数据
        List<SysPermission> list = t.getChildren();
        for (SysPermission tree : nodeList) {
            //  判断当前的父id是否等于父节点id
            if (t.getId().equals(tree.getPid())) {
                //  是的话传入父类的子集合中
                list.add(tree);
                //  表示节点为该父id
                getChilNode(nodeList, tree);
            }
        }
        //  这个返回其实只有返回总树结构有用
        return list;
    }

}
