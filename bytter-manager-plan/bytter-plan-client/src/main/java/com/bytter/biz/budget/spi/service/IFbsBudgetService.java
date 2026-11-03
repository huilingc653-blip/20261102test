package com.bytter.biz.budget.spi.service;
import java.util.Date;
import java.util.List;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;

/**
 * 资金计划服务
 * @author huangx
 * @since 2018-03-01
 */
public interface IFbsBudgetService {
	/**
	 * 获取可执行金额
	 * @param corpId	单位ID
	 * @param itemCode	科目编码
	 * @param processDate	执行日期
	 * @param billNo	业务单号
	 * @param billId	业务单ID
	 * @return 计划执行DTO
	 */
	FbsBudgetProcessCommonDTO getFbsBudgetProcessByItemCode(String corpId, String itemCode, Date processDate, String billNo, String billId);
	
	List<FbsBudgetProcessCommonDTO> getFbsBudgetProcessByItemCodeForList(List<FbsBudgetProcessDTO> list);
	/**
	 * app审批挂接计划执行数据
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @param processDate	执行日期
	 * @return	计划执行集合
	 */
	List<FbsBudgetProcessCommonDTO> listFbsBudgetProcessByBusiness(String billNo, String billId, String businessName, String processSysname, String corpId, Date processDate);
	/**
	 * 获取资金计划参数 明细项控制模式参数
	 * 从参数读到需要控制的列
	 * @return 资金计划参数 明细项控制模式参数
	 */
	String getColumnsByDtlColSel();
	/**
	 * 根据周期类型获取上报明细表名
	 * @param timeType 周期类型
	 * @return 上报明细表表名
	 */
	String getTableNameByTimeType(String timeType);
	/**
	 * 根据周期类型获取实体名称
	 * @param timeType 周期类型
	 * @return 上报明细表实体名称
	 */
	String getEntityNameByTimeType(String timeType);
	/**
	 * 判断是否启用周期类型
	 *
	 * @param timeType 周期类型
	 * @return true/false
	 */
	boolean isExistTimeType(String timeType);

    List<FbsBudgetProcessCommonDTO> getItemDetail(FbsBudgetProjectDetailDTO fbsBudgetProjectDetailVO);
}
