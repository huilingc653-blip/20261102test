package com.bytter.core.base.persistence.tree.interfaces;
/**
 * 带有层级结构且排序树形接口
 *
 * @author zhengg
 * @date 2017/11/20
 *
 * @deprecated Use {@link com.bytter.framework.util.tree.TreeNode}
 */
@Deprecated
public interface TreeSortStructure<M> extends TreeStructureAttribute<M> {
    Integer getSort();
}
