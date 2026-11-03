package com.bytter.bill.domain.billPool.utils;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
* @Title:  过滤utils结构
* @Package com.bytter.biz.billPool.utils   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2020-1-07 上午11:19:48   
* @version V1.0
 */
public class BusTypeUtil {
	public static List<AbPoolBusTypeVO> splicingStructure(List<AbPoolBusTypeVO> treeNodes) {
		List<AbPoolBusTypeVO> trees = new ArrayList<AbPoolBusTypeVO>();
		//过滤父节点
		for (AbPoolBusTypeVO treeNode : treeNodes) {
			if (StringUtils.isBlank(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		//递归解析拼接节点信息
		List<AbPoolBusTypeVO> treeNodeList =new ArrayList<AbPoolBusTypeVO>();
		Map<String, Object> map = new HashMap<String, Object>();
		for(AbPoolBusTypeVO treeNode :trees){
			 if(!map.containsValue(treeNode.getId())){
				 map.put(null, treeNode.getId());
				 treeNodeList.add(treeNode);
			 }
			 if(ListUtils.isNotNullOrEmpty(treeNode.getChildren())){
				 splicingChildrenCD(map,treeNode,treeNodeList);
			 }
		}
		return treeNodeList;
	}
	
	public static void splicingChildrenCD(Map<String, Object> map,AbPoolBusTypeVO children,List<AbPoolBusTypeVO> childrenList){
		for(AbPoolBusTypeVO treeNode :children.getChildren()){
			 if(!map.containsValue(treeNode.getId())){
				 map.put(null, treeNode.getId());
				 childrenList.add(treeNode);
			 }
			 if(ListUtils.isNotNullOrEmpty(treeNode.getChildren())){
				 splicingChildrenCD(map,treeNode,childrenList);
			 }
		}
	}
    /** 
     * 递归查找地址子节点 
     * @param treeNodes 
     * @return 
     */
	public static AbPoolBusTypeVO findChildren(AbPoolBusTypeVO treeNode,List<AbPoolBusTypeVO> treeNodes) {
		for (AbPoolBusTypeVO it : treeNodes) {
			if(treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<AbPoolBusTypeVO>());
				}
				treeNode.getChildren().add(findChildren(it,treeNodes));
			}
		}
		return treeNode;
	}
}
