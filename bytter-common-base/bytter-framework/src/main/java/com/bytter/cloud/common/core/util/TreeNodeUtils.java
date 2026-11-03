package com.bytter.cloud.common.core.util;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import java.util.*;
/**
 * List数据，转换树形结构和顺序结构工具类
 *
 * @author covenanter
 * @since 2019-11-25
 */
public class TreeNodeUtils {
    private TreeNodeUtils() {
    }
    /**
     * 将树形结构集合数据转成顺序结构数据
     *
     * @param treeList 树形结构集合
     * @param <T>      1、实现{@link DiscontinuityTreeNode}接口的对象进行转换，将会丢失连续的节点。<br>
     *                 2、实现{@link TreeNode}接口的对象进行转换，如果出现不连续节点，将会自动保留之后的节点。
     * @return 顺序结构集合
     */
    public static <T extends DiscontinuityTreeNode<T>> List<T> transformToList(List<T> treeList) {
        if (CollectionUtils.isEmpty(treeList)) {
            return treeList;
        }
        List<T> tempTreeNodes = new ArrayList<>();
        for (T treeNode : treeList) {
            tempTreeNodes.add(treeNode);
            List<T> children = treeNode.getChildren();
            if (!CollectionUtils.isEmpty(children)) {
                tempTreeNodes.addAll(transformToList(children));
                treeNode.setChildren(null);
            }
        }
        return tempTreeNodes;
    }
    /**
     * 将无结构集合转换为树形结构集合
     *
     * @param list 无结构集合
     * @param <T>  1、实现{@link DiscontinuityTreeNode}接口的对象进行转换，将会丢失连续的节点。<br>
     *             2、实现{@link TreeNode}接口的对象进行转换，如果出现不连续节点，将会自动保留之后的节点。
     * @return 树形数据结构集合
     */
    public static <T extends DiscontinuityTreeNode<T>> List<T> transformToTree(List<T> list) {
        return transformToTree(list, null);
    }
    /**
     * 将无结构集合转换为树形结构集合
     *
     * @param handlerList 无结构集合
     * @param comparator  排序器
     * @param <T>         1、实现{@link DiscontinuityTreeNode}接口的对象进行转换，将会丢失连续的节点。<br>
     *                    2、实现{@link TreeNode}接口的对象进行转换，如果出现不连续节点，将会自动保留之后的节点。
     * @return 树形数据结构集合
     */
    public static <T extends DiscontinuityTreeNode<T>> List<T> transformToTree(List<T> handlerList, Comparator<? super T> comparator) {
        if (CollectionUtils.isEmpty(handlerList)) {
            return handlerList;
        }
        //使用map，把对象ID作为map的key，对象作为map的值。
        //此时map的key相当于集合对象的指针，通过指针直接修改对象中的孩子节点
        Map<String, T> map = new HashMap<>(handlerList.size());
        for (T discontinuityTreeNode : handlerList) {
            map.put(discontinuityTreeNode.getId(), discontinuityTreeNode);
        }
        List<T> tempTreeNodes = new ArrayList<>();
        //遍历需要构建树的结合
        for (T discontinuityTreeNode : handlerList) {
            //在map中查找当前对象的父对象
            T mapTreeNode = map.get(discontinuityTreeNode.getParentId());
            //如果父对象不为空，且父对象就不是自己本身
            if (mapTreeNode != null && !discontinuityTreeNode.getId().equals(discontinuityTreeNode.getParentId())) {
                //将当前的树形节点加入到父对象的孩子中
                setChildrenValue(mapTreeNode, discontinuityTreeNode);
            } else {
                //如果父对象为空，或者父对象就是自己本身，执行下面代码
                //建立是否连续树形节点标识
                boolean continuityFlag = false;
                //是否是连续树形节点
                if (discontinuityTreeNode instanceof TreeNode) {
                    //将非连续树形节点专为连续树形节点
                    TreeNode<?> treeNode = (TreeNode) discontinuityTreeNode;
                    String simpleClassName = discontinuityTreeNode.getClass().getSimpleName();
                    //获取分隔符
                    String split = treeNode.getSplit();
                    Assert.hasText(split, simpleClassName + "#getSplit() method can not return null");
                    //获取该节点的全路径
                    String path = treeNode.getPath();
                    Assert.hasText(path, simpleClassName + "#getPath() method can not return null");
                    String[] paths = path.split(split);
                    //根据全路径节点，找到当前节点所处的下标
                    int currentIndex = -1;
                    for (int i = 0; i < paths.length; i++) {
                        if (discontinuityTreeNode.getId().equals(paths[i])) {
                            currentIndex = i;
                            break;
                        }
                    }
                    //只有当当前节点处于第三个位置，即下标为2之后，出现不连续节点
                    if (currentIndex >= 2) {
                        T tempNode = map.get(paths[currentIndex - 2]);
                        setChildrenValue(tempNode, discontinuityTreeNode);
                        //更改标识，设置为连续节点
                        continuityFlag = true;
                    }
                }
                //如果不是连续节点，则将对象加入数组中
                if (!continuityFlag) {
                    tempTreeNodes.add(discontinuityTreeNode);
                }
            }
        }
        //是否存在排序器
        if (comparator != null) {
            //将顶级集合先排序
            tempTreeNodes.sort(comparator);
            //遍历孩子节点，进行递归排序
            loopSort(tempTreeNodes, comparator);
        }
        return tempTreeNodes;
    }
    /**
     * 设置孩子节点
     *
     * @param treeNode  被设置的对象
     * @param childNode 孩子节点
     * @param <T>       1、实现{@link DiscontinuityTreeNode}接口的对象进行转换，将会丢失连续的节点。<br>
     *                  2、实现{@link TreeNode}接口的对象进行转换，如果出现不连续节点，将会自动保留之后的节点。
     */
    private static <T extends DiscontinuityTreeNode<T>> void setChildrenValue(T treeNode, T childNode) {
        if (treeNode.getChildren() == null) {
            treeNode.setChildren(new ArrayList<>());
        }
        treeNode.getChildren().add(childNode);
    }
    /**
     * 递归循环排序
     *
     * @param childNodes 子节点集合
     * @param comparator 排序器
     * @param <T>        树节点类型
     */
    private static <T extends DiscontinuityTreeNode<T>> void loopSort(List<T> childNodes, Comparator<? super T> comparator) {
        for (T treeNode : childNodes) {
            List<T> children = treeNode.getChildren();
            if (children == null || treeNode.getChildren().size() <= 1) {
                continue;
            }
            treeNode.getChildren().sort(comparator);
            loopSort(treeNode.getChildren(), comparator);
        }
    }
}
