package com.bytter.biz.budget.spi.service;
import java.util.List;
import java.util.Set;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.vo.FbsItemVO;
/**
 * 资金计划科目接口类
 * @author huangx
 * @since 2018-03-01
 */
public interface IFbsItemService {
	/**
	 * 获取有效计划列表
	 * @return  计划科目集合
	 */
	List<FbsItemDTO> listFbsItemInfoValid();
	/**
	 * 根据有效标志计划科目列表
	 * @param validSign 有效标志
	 * @return 计划科目集合
	 */
	List<FbsItemDTO> listFbsItemInfo(String validSign);
	/**
	 * 根据资金流向获取计划列表查询账号列表
	 * @param validSign 有效标志，可空
	 * @param sign 资金流向
	 * @return  计划科目集合
	 */
	List<FbsItemDTO> listFbsItemInfoBySign(String validSign,Integer sign);
	/**
	 * 根据科目代码查找科目信息
	 * @param itemCode 计划科目
	 * @return 计划科目信息
	 */
	FbsItemDTO getFbsItemByCode(String itemCode);
	/**
	 * 根据资金流向获取可执行的计划科目集合
	 * @param sign 资金流向 -1 流出 1 流入
	 * @return
	 */
	List<FbsItemDTO> listFbsItemForProcess(Integer sign);
	/**
	 * 获取计划执行科目
	 * @return
	 */
	String getFbsItemForProcess(Integer sign);
	
	List<com.bytter.biz.budget.spi.vo.FbsItemVO> getFbsItemVOS(Set<String> itemCodes);
}
