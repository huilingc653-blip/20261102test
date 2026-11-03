package com.bytter.cloud.common.core.util;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 连续的树形结构 <br>
 * 即： <br>
 * root<br>
 * |-a1<br>
 * |--a2 (该节点被禁用)<br>
 * |---a3（该节点会紧接a1）<br>
 * |-b<br>
 *
 * @author covenanter
 * @since 2019-11-24
 */
public interface TreeNode<T> extends DiscontinuityTreeNode<T> {
    /**
     * 全路径
     *
     * @return 全路径
     */
    @JsonIgnore
    String getPath();
    /**
     * 全路径的分隔符
     *
     * @return 分隔符
     */
    @JsonIgnore
    String getSplit();
}
