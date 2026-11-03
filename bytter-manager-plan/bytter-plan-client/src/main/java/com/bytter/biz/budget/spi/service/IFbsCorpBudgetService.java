package com.bytter.biz.budget.spi.service;
import java.util.List;
import java.util.Map;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
/**
 * 单位资金计划设置表
 * @author zuoy
 * @since 2018-06-13
 */
public interface IFbsCorpBudgetService {
	/**
	 * 单位是否上报
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean reportBudgetByCorpId(String corpId);
	/**
	 * 单位是否控制
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean controlBudgetByCorpId(String corpId);
	/**
	 * 单位是否汇总
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean collectFlagByCorpId(String corpId);
	/**
	 * 获取可上报的单位
	 * @return 计划上报单位集合
	 */
	List<FbsCorpBudgetDTO> listFbsCorpBudgetDTOByReportBudget();
	/**
	 * 获取受控制的单位
	 * @return 计划控制单位集合
	 */
	List<FbsCorpBudgetDTO> listFbsCorpBudgetDTOByControlBudget();
	/**
	 * 获取可上报的下属单位
	 * @param parentCorpId 上级单位ID
	 * @return 可上报的下属单位集合
	 */
	List<FbsCorpBudgetDTO> listReportBudgetByParentCorpId(String parentCorpId);
	/**
	 * 获取可汇总的下属单位 必须可上报才能可汇总
	 * @param parentCorpId 上级单位ID
	 * @return 可汇总的下属单位集合
	 */
	List<FbsCorpBudgetDTO> listReportAndCollectByParentCorpId(String parentCorpId);
	/**
	 * 判断单位是否要汇总下级单位
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean getChildCollectFlag(String corpId);
	/**
	 * 获取单位的汇总标志
	 * @param parentCorpIds
	 * @return
	 */
	Map<String, String> listReportAndCollectByParentCorpId(List<String> parentCorpIds);
	/**
	 * 判断单位本身是否要汇总
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean getCollectFlag(String corpId);
	/**
	 * 根据单位ID获取单位资金计划设置表
	 * @param corpId 单位ID
	 * @return 单位资金计划设置信息
	 */
	FbsCorpBudgetDTO getByCorpId(String corpId);
	/**
	 * 根据单位集合获取单位资金计划设置表
	 * @param corpIds 单位ID集合
	 * @return 单位资金计划设置集合
	 */
	List<FbsCorpBudgetDTO> listByCorpIds(List<String> corpIds);
	/**
	 * 获取具有上报权限的单位集合
	 * @param corpIds 单位ID集合
	 * @return 单位集合
	 */
	List<String> listCorpIdsByReportBudget(List<String> corpIds);


    boolean getChildCollectFlagMap(List<String> ids);

    List<FbsCorpBudgetDTO> listReportBudgetByParentCorpId(List<String> parentCorpIds);

    /**
     * 判断单位本身是否要汇总
     * @param corpIds 单位ID
     * @return true/false
     */
    Map<String, Boolean> queryCollectFlagMap(List<String> corpIds);
}
