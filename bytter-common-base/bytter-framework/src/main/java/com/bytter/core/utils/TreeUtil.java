package com.bytter.core.utils;
import com.bytter.core.base.entity.TreeEntity;
import com.bytter.core.base.entity.TreeEntityTemp;
import com.bytter.core.base.persistence.tree.interfaces.*;
import java.util.*;
/**
 * 用于构建树形结构工具类 树形工具有两种数据结构 1、顺序结构，没有层级关系 2、有层级关系
 *
 * @author zhoul
 * @since 2017/9/6
 * @deprecated Use {@link com.bytter.framework.util.TreeNodeUtils}
 */
@Deprecated
public class TreeUtil {
    /**
     * 构建List数据结构，按照树形结构排序的树
     *
     * @param treeDataList
     *            需要转为树的list集合
     * @param <M>
     *            list中的泛型
     * @return 转换为树形 List数据结构的集合
     */
    public static <M extends TreeList> List<M> getTreeNodeList(List<M> treeDataList) {
        return coverTreeList(treeDataList, null, null, null);
    }
    /**
     * 构建List数据结构，按照树形结构排序的树
     *
     * @param treeDataList
     *            需要转为树的list集合
     * @param c
     *            排序比较器
     * @param <M>
     *            list中的泛型
     * @return 转换为树形 List数据结构的集合
     */
    public static <M extends TreeList> List<M> getTreeNodeList(List<M> treeDataList, Comparator<? super M> c) {
        return coverTreeList(treeDataList, null, c, null);
    }
    private static <M extends TreeList> List<M> coverTreeList(List<M> nodeList, M node, Comparator<? super M> c,
            Set<M> flagList) {
        if (nodeList == null) {
            throw new RuntimeException("list can not be null");
        }
        if (flagList == null) {
            flagList = new HashSet<>();
        }
        List<M> tempList = new ArrayList<>();
        Iterator<M> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            M tempNode = iterator.next();
            String tempParentId = tempNode.getParentId();
            if ((node == null
                    && (StringUtils.isBlank(tempParentId) || !flagList.contains(tempNode)))
                    || (node != null && (tempParentId != null && tempParentId.equals(node.getId())))) {
                List<M> children = coverTreeList(new ArrayList<>(nodeList), tempNode, c, flagList);
                if (!children.isEmpty()) {
                    if (c != null) {
                        Collections.sort(children, c);
                    }
                    tempList.addAll(children);
                }
                tempList.add(tempNode);
                flagList.add(tempNode);
                iterator.remove();
            }
        }
        return tempList;
    }
    /**
     * 构建真正有包含层级的属性结构
     *
     * @param treeDataList
     *            需要转为树的list集合
     * @param <M>
     *            list中的泛型
     * @return 转换为树形有包含关系层级结构的list
     */
    public static <M extends TreeStructure> List<M> getTreeNodeStructure(List<M> treeDataList) {
        // sortTree(treeDataList);
        return coverTreeStructure(treeDataList, null, null, null);
    }
    /**
     * 构建真正有包含层级的属性结构
     *
     * @param treeDataList
     *            需要转为树的list集合
     * @param c
     *            比较器
     * @param <M>
     *            list中的泛型
     * @return 转换为树形有包含关系层级结构的list
     */
    public static <M extends TreeStructure> List<M> getTreeNodeStructure(List<M> treeDataList,
            Comparator<? super M> c) {
        // sortTree(treeDataList);
        return coverTreeStructure(treeDataList, null, c, null);
    }
    private static <M extends TreeStructure> List<M> coverTreeStructure(List<M> nodeList, M node,
            Comparator<? super M> c, Set<M> flagList) {
        if (nodeList == null) {
            throw new RuntimeException("list can not be null");
        }
        if (flagList == null) {
            flagList = new HashSet<>();
        }
        List<M> tempList = new ArrayList<>();
        Iterator<M> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            M tempNode = iterator.next();
            String tempParentId = tempNode.getParentId();
            if ((node == null
                    && (StringUtils.isBlank(tempParentId) || !flagList.contains(tempNode)))
                    || (node != null && (tempParentId != null && tempParentId.equals(node.getId())))) {
                tempList.add(tempNode);
                flagList.add(tempNode);
                iterator.remove();
                List<M> children = coverTreeStructure(new ArrayList<>(nodeList), tempNode, c, flagList);
                if (!children.isEmpty()) {
                    if (c != null) {
                        Collections.sort(children, c);
                    }
                    tempNode.setChildren(children);
                }
            }
        }
        return tempList;
    }
    /**
     * 创建父子结构的树
     *
     * @param nodeList
     *            集合
     * @return 转换为树形有包含关系层级结构的list
     */
    @SuppressWarnings({ "rawtypes" })
    public static <M extends TreeStructure> List<M> createTreeStructure(List<M> nodeList) {
        return createTreeStructure(nodeList, null);
    }
    /**
     * 创建树顺序的数据且排序
     *
     * @param nodeList
     *            集合
     * @return 转换为树形有包含关系层级结构的list
     */
    @SuppressWarnings({ "rawtypes" })
    public static <M extends TreeSortStructure> List<M> createTreeTier(List<M> nodeList) {
        List<M> treeList = createTreeStructure(nodeList, null);
        treeSort(treeList);
        List<M> treeTierList = new ArrayList<>();
        treeStructureToTier(treeTierList, treeList, 0);
        return treeTierList;
    }
    /**
     * 根据树结构转换树顺序
     *
     * @param treeTierList
     *            集合
     * @param nodeList
     *            集合
     * @param level
     *            层级
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <M extends TreeStructureAttribute> void treeStructureToTier(List<M> treeTierList, List<M> nodeList,
            Integer level) {
        if (nodeList != null && !nodeList.isEmpty()) {
            if (treeTierList == null) {
                treeTierList = new ArrayList<>();
            }
            if (level == null) {
                level = 0;
            }
            for (TreeStructureAttribute node : nodeList) {
                node.setLevel(level);
                TreeStructureAttribute nodeNew = BeanUtils.copyNewInstanceProperties(node, node.getClass());
                nodeNew.setChildren(null);
                treeTierList.add((M) nodeNew);
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    nodeNew.setIsLeaf(false);
                    treeStructureToTier(treeTierList, node.getChildren(), level + 1);
                } else {
                    nodeNew.setIsLeaf(true);
                }
            }
        }
    }
    /**
     * 根据树结构转换树顺序
     *
     * @param treeTierList
     *            集合
     * @param nodeList
     *            集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <M extends TreeStructure> void treeStructureToTier(List<M> treeTierList, List<M> nodeList) {
        if (nodeList != null && !nodeList.isEmpty()) {
            if (treeTierList == null) {
                treeTierList = new ArrayList<>();
            }
            for (TreeStructure node : nodeList) {
                TreeStructure nodeNew = BeanUtils.copyNewInstanceProperties(node, node.getClass());
                nodeNew.setChildren(null);
                treeTierList.add((M) nodeNew);
                if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                    treeStructureToTier(treeTierList, node.getChildren());
                }
            }
        }
    }
    /**
     * 根据父节点获取父子结构的树包含本身
     *
     * @param nodeList
     *            集合
     * @return 转换为树形有包含关系层级结构的list
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <M extends TreeStructure> List<M> createTreeStructure(List<M> nodeList, String fatherNodeId) {
        if (nodeList == null) {
            throw new RuntimeException("list can not be null");
        }
        List<M> treeInfos = null;
        if (null != nodeList && !nodeList.isEmpty()) {
            // 创建根节点
            TreeEntity root = new TreeEntityTemp();
            // root.setName("根目录");
            TreeStructure fatherNode = null;
            // 组装Map数据
            Map<String, TreeStructure> dataMap = new LinkedHashMap();
            for (TreeStructure node : nodeList) {
                dataMap.put(node.getId(), node);
            }
            // 组装树形结构
            Set<Map.Entry<String, TreeStructure>> entrySet = dataMap.entrySet();
            for (Map.Entry<String, TreeStructure> entry : entrySet) {
                TreeStructure children = entry.getValue();
                if (StringUtils.isEmpty(children.getParentId())) {
                    setChildren(root, children);
                } else {
                    TreeStructure info = dataMap.get(children.getParentId());
                    if (info == null) {
                        setChildren(root, children);
                    } else {
                        setChildren(info, children);
                    }
                }
                if (String.valueOf(children.getId()).equals(fatherNodeId)) {
                    fatherNode = children;
                }
            }
            // 对树形结构进行二叉树排序
            // root.sortChildren();
            if (fatherNode != null) {
                treeInfos = new ArrayList<>();
                treeInfos.add((M) fatherNode);
            } else {
                treeInfos = root.getChildren();
            }
        }
        return treeInfos;
    }
    /**
     * 父子结构的树排序
     *
     * @param nodeList
     *            集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <M extends TreeSortStructure> void treeSort(List<M> nodeList) {
        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }
        Collections.sort(nodeList, new Comparator<TreeSortStructure>() {
            @Override
            public int compare(TreeSortStructure o1, TreeSortStructure o2) {
                int result = 0;
                Integer ordby1 = o1.getSort();
                Integer ordby2 = o2.getSort();
                String parentId1 = o1.getParentId();
                String parentId2 = o2.getParentId();
                if ((StringUtils.isEmpty(parentId1) && StringUtils.isNotEmpty(parentId2))
                        || (StringUtils.isNotEmpty(parentId1) && StringUtils.isEmpty(parentId2))) {
                    result = (StringUtils.isEmpty(parentId1) ? -1 : (parentId1.equals(parentId2) ? 0 : 1));
                } else if (null != ordby1 && null != ordby2) {
                    result = (ordby1 < ordby2 ? -1 : (ordby1.equals(ordby2) ? 0 : 1));
                }
                return result;
            }
        });
        // 对每个节点的下一层节点进行排序
        for (TreeSortStructure info : nodeList) {
            treeSort(info.getChildren());
        }
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void setChildren(TreeStructure entity, TreeStructure children) {
        if (entity.getChildren() == null) {
            entity.setChildren(new ArrayList<>());
        }
        entity.getChildren().add(children);
    }
    public static <M extends TreeStructureAttribute> List<M> getTreeNodeStructureAttribute(List<M> treeDataList) {
        return coverTreeStructureAttribute(treeDataList, null, null, null, false);
    }
    public static <M extends TreeStructureAttribute> List<M> getTreeNodeStructureAttributeList(List<M> treeDataList) {
        return coverTreeStructureAttribute(treeDataList, null, null, null, true);
    }
    public static <M extends TreeStructureAttribute> List<M> getTreeNodeStructureAttribute(List<M> treeDataList,
            Comparator<? super M> c) {
        return coverTreeStructureAttribute(treeDataList, null, c, null, false);
    }
    public static <M extends TreeStructureAttribute> List<M> getTreeNodeStructureAttributeList(List<M> treeDataList,
            Comparator<? super M> c) {
        return coverTreeStructureAttribute(treeDataList, null, c, null, true);
    }
    private static <M extends TreeStructureAttribute> List<M> coverTreeStructureAttribute(List<M> nodeList, M node,
            Comparator<? super M> c, Set<M> flagList, boolean treeTable) {
        if (nodeList == null) {
            throw new RuntimeException("list can not be null");
        }
        if (flagList == null) {
            flagList = new HashSet<>();
        }
        List<M> tempList = new ArrayList<>();
        Iterator<M> iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            M tempNode = iterator.next();
            String tempParentId = tempNode.getParentId();
            if ((node == null
                    && (StringUtils.isBlank(tempParentId) || !flagList.contains(tempNode)))
                    || (node != null && (tempParentId != null && tempParentId.equals(node.getId())))) {
                // 获取层级结构
                if (node == null || node.getLevel() == null) {
                    tempNode.setLevel(0);
                } else {
                    tempNode.setLevel(node.getLevel() + 1);
                }
                // tempList.add(tempNode);
                flagList.add(tempNode);
                iterator.remove();
                List<M> children = coverTreeStructureAttribute(new ArrayList<>(nodeList), tempNode, c, flagList,
                        treeTable);
                tempNode.setIsLeaf(children.isEmpty());
                if (treeTable) {
                    tempList.add(tempNode);
                }
                if (!children.isEmpty()) {
                    if (c != null) {
                        Collections.sort(children, c);
                    }
                    if (treeTable) {
                        tempList.addAll(children);
                    } else {
                        tempNode.setChildren(children);
                    }
                }
                if (!treeTable) {
                    tempList.add(tempNode);
                }
            }
        }
        return tempList;
    }
    /**
     * 将层级结构的数据转换为顺序集合
     *
     * @param m
     *            实现TreeStructure 的POJO
     * @param <M>
     *            POJO 作为泛型
     * @return POJO集合
     */
    public static <M extends TreeStructure> List<M> treeStructureToList(M m) {
        if (m == null) {
            throw new RuntimeException("data can not be null");
        }
        List<M> tempList = new ArrayList<>();
        treeStructureToList(tempList, m);
        return tempList;
    }
    public static <M extends TreeStructureAttribute> List<M> treeStructureToList(M m) {
        if (m == null) {
            throw new RuntimeException("data can not be null");
        }
        List<M> tempList = new ArrayList<>();
        treeStructureToList(tempList, m);
        return tempList;
    }
    public static <M extends TreeStructureAttribute> List<M> treeStructureToList(List<M> mList) {
        if (mList == null) {
            throw new RuntimeException("data can not be null");
        }
        List<M> tempList = new ArrayList<>();
        for (M node : mList) {
            treeStructureToList(tempList, node);
        }
        return tempList;
    }
    private static <M extends TreeStructure> void treeStructureToList(List<M> mList, M m) {
        mList.add(m);
        List<M> children = m.getChildren();
        if (children != null && !children.isEmpty()) {
            m.setChildren(null);
            // mList.addAll(children);
            for (M childrenObj : children) {
                treeStructureToList(mList, childrenObj);
            }
        }
    }
    /**
     * 搜索树形层级结构中指定id的所有子节点（包含自身）
     *
     * @param m
     *            实现TreeStructure 的POJO
     * @param id
     *            需要搜索的id
     * @param <M>
     *            POJO 作为泛型
     * @return 树形结构结果集
     */
    public static <M extends TreeStructure> List<M> searchTreeStructureToList(M m, final String id) {
        if (m == null) {
            throw new RuntimeException("data can not be null");
        }
        List<M> tempList = new ArrayList<>();
        TreeMatcher<M> treeMatcher = new TreeMatcher<M>() {
            @Override
            public boolean hit(M o1) {
                return id.equals(o1.getId());
            }
        };
        searchTreeStructureToList(tempList, m, treeMatcher, null);
        return tempList;
    }
    /**
     * 搜索树形层级结构中指定id的所有子节点（包含自身）
     *
     * @param mList
     *            实现TreeStructure 的POJO List结合
     * @param id
     *            需要搜索的id
     * @param <M>
     *            POJO 作为泛型
     * @return 树形结构结果集
     */
    public static <M extends TreeStructure> List<M> searchTreeStructureToList(List<M> mList, final String id) {
        if (mList == null) {
            throw new RuntimeException("data can not be null");
        }
        TreeMatcher<M> treeMatcher = new TreeMatcher<M>() {
            @Override
            public boolean hit(M o1) {
                return id.equals(o1.getId());
            }
        };
        return batchSearchTreeStructureToList(mList, treeMatcher);
    }
    public static <M extends TreeStructure> List<M> searchTreeStructureToList(M m, TreeMatcher<M> matcher) {
        if (m == null) {
            throw new RuntimeException("data can not be null");
        }
        List<M> tempList = new ArrayList<>();
        searchTreeStructureToList(tempList, m, matcher, null);
        return tempList;
    }
    public static <M extends TreeStructure> List<M> searchTreeStructureToList(List<M> mList, TreeMatcher<M> matcher) {
        if (mList == null) {
            throw new RuntimeException("data can not be null");
        }
        return batchSearchTreeStructureToList(mList, matcher);
    }
    private static <M extends TreeStructure> List<M> batchSearchTreeStructureToList(List<M> mList,
            TreeMatcher<M> matcher) {
        List<M> allList = new ArrayList<>();
        for (M m : mList) {
            List<M> tempList = new ArrayList<>();
            searchTreeStructureToList(tempList, m, matcher, null);
            if (!tempList.isEmpty()) {
                allList.addAll(tempList);
            }
        }
        return allList;
    }
    /**
     * 搜索树形层级结构中指定id的所有子节点（包含自身）
     *
     * @param mList
     *            实现TreeStructure 的POJO 集合
     * @param m
     *            实现TreeStructure 的POJO
     * @param matcher
     *            匹配器
     * @param hit
     *            命中标识
     * @param <M>
     *            POJO 作为泛型
     */
    private static <M extends TreeStructure> void searchTreeStructureToList(List<M> mList, M m, TreeMatcher<M> matcher,
            Boolean hit) {
        if (hit == null) {
            hit = matcher.hit(m);
            if (hit) {
                mList.add(m);
            }
        }
        List<M> children = m.getChildren();
        if (!EmptyUtils.isEmpty(children)) {
            if (hit) {
                m.setChildren(null);
                mList.addAll(children);
            }
            for (M childrenObj : children) {
                // 递归调用，hit为true时标识命中，将该标识一直传递下去，否为设置为null
                searchTreeStructureToList(mList, childrenObj, matcher, hit ? true : null);
            }
        }
    }
    // 2017年11月14日 17:31:08
    public static <M extends TreeStructureAttribute<M>> List<M> coverTreeList(List<M> mList) {
        return coverTree(mList, null, false);
    }
    public static <M extends TreeStructureAttribute<M>> List<M> coverTreeTable(List<M> mList) {
        return coverTree(mList, null, true);
    }
    public static <M extends TreeStructureAttribute<M>> List<M> coverTree(List<M> mList, M m, Boolean treeTable) {
        List<M> tempList = new ArrayList<>();
        for (M tempM : mList) {
            String tempParentId = tempM.getParentId();
            if ((m == null && (StringUtils.isBlank(tempParentId) || "0".equals(tempParentId.trim())))
                    || (m != null && (StringUtils.isNotBlank(tempParentId) && tempParentId.equals(m.getId())))) {
                // 获取层级结构
                if (m == null || m.getLevel() == null) {
                    tempM.setLevel(0);
                } else {
                    tempM.setLevel(m.getLevel() + 1);
                }
                List<M> children = coverTree(mList, tempM, treeTable);
                tempM.setIsLeaf(children.isEmpty());
                if (treeTable) {
                    tempList.add(tempM);
                }
                if (!children.isEmpty()) {
                    if (treeTable) {
                        tempList.addAll(children);
                    } else {
                        tempM.setChildren(children);
                    }
                }
                if (!treeTable) {
                    tempList.add(tempM);
                }
            }
        }
        return tempList;
    }

    /**
     * 构建树形列表（平铺结构，但按树形顺序排序）
     */
    public static <M extends TreeList> List<M> buildTreeList(List<M> list, Comparator<? super M> comparator) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 按ID分组
        Map<String, M> nodeMap = new HashMap<>();
        for (M node : list) {
            nodeMap.put(node.getId(), node);
        }

        // 找到所有根节点
        List<M> roots = new ArrayList<>();
        for (M node : list) {
            String parentId = node.getParentId();
            if (StringUtils.isBlank(parentId) || !nodeMap.containsKey(parentId)) {
                roots.add(node);
            }
        }

        // 对根节点排序
        if (comparator != null) {
            roots.sort(comparator);
        }

        // 递归构建平铺列表
        List<M> result = new ArrayList<>();
        for (M root : roots) {
            addNodeAndChildren(result, root, nodeMap, comparator, new HashSet<>());
        }

        return result;
    }

    /**
     * 递归添加节点及其子节点到结果列表
     */
    private static <M extends TreeList> void addNodeAndChildren(List<M> result, M node,
                                                                Map<String, M> nodeMap, Comparator<? super M> comparator, Set<String> visited) {
        // 防止循环引用
        if (visited.contains(node.getId())) {
            return;
        }
        visited.add(node.getId());

        // 先递归处理子节点
        List<M> children = new ArrayList<>();
        for (M potentialChild : nodeMap.values()) {
            if (node.getId().equals(potentialChild.getParentId())) {
                children.add(potentialChild);
            }
        }

        // 对子节点排序
        if (comparator != null && !children.isEmpty()) {
            children.sort(comparator);
        }

        // 递归处理每个子节点
        for (M child : children) {
            addNodeAndChildren(result, child, nodeMap, comparator, visited);
        }

        // 最后添加当前节点
        result.add(node);
    }
}
