package com.bytter.cloud.common.core.util;
import java.util.List;
/**
 * 不连续、不连贯的树形结构 <br>
 * 即： <br>
 * root<br>
 * |-a1<br>
 * |--a2 (该节点被禁用)<br>
 * |---a3（该节点会紧接a1）<br>
 * |-b<br>
 *
 * @author zhoul  2019-11-24
 * @since 1.0.0
 */
public interface DiscontinuityTreeNode<T> {
    /**
     * 返回树形的ID
     *
     * @return 主键ID
     */
    String getId();
    /**
     * 返回树形的父ID
     *
     * @return 父ID
     */
    String getParentId();
    /**
     * 设置子节点
     *
     * @param children 孩子对象
     */
    void setChildren(List<T> children);
    /**
     * 返回子节点
     *
     * @return 子节点
     */
    List<T> getChildren();
}
