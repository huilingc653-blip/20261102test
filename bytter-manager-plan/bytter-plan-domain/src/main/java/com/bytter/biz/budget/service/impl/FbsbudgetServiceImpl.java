package com.bytter.biz.budget.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.service.FbsBudgetProcessService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.service.FbsBudgetService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.vo.FbsBudgetProcessCommonVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author zhangxb
 * @date 2017/5/22 0022.
 */
@Service("fbsbudgetServiceImpl")
@Transactional
@DubboService
public class FbsbudgetServiceImpl implements IFbsBudgetService {
	@Autowired
	private FbsBudgetService fbsbudgetService;

	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	/**
	 * 获取可执行金额
	 *
	 * @param corpId
	 * @param itemCode
	 * @param processDate
	 * @param billNo
	 * @param billId
	 * @return
	 */
	@Override
	public FbsBudgetProcessCommonDTO getFbsBudgetProcessByItemCode(String corpId, String itemCode, Date processDate, String billNo, String billId) {
		FbsBudgetProcessCommonDTO dto = new FbsBudgetProcessCommonDTO();
		FbsBudgetProcessCommonVO vo = fbsbudgetService.getFbsBudgetProcessByItemCode(corpId, itemCode, processDate, billNo, billId, null);
		BeanUtils.copyProperties(vo, dto);
		return dto;
	}
	/**
	 * app审批挂接计划执行数据
	 *
	 * @param billNo
	 * @param billId         业务单ID
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param corpId
	 * @param processDate
	 * @return
	 */
	@Override
	public List<FbsBudgetProcessCommonDTO> listFbsBudgetProcessByBusiness(String billNo, String billId, String businessName, String processSysname, String corpId, Date processDate) {
		List<FbsBudgetProcessCommonVO> dto = fbsbudgetService.listFbsBudgetProcessByBusiness(billNo, billId, businessName, processSysname, corpId, processDate);
		if (EmptyUtils.isEmptyList(dto)) {
			return null;
		}
		return BeanUtils.copyListProperties(dto, FbsBudgetProcessCommonDTO.class);
	}
	@Override
	public String getColumnsByDtlColSel() {
		return fbsbudgetService.getColumnsByDtlColSel();
	}
	/**
	 * 根据周期类型获取上报明细表名
	 * @param timeType
	 * @return
	 */
	@Override
	public String getTableNameByTimeType(String timeType){
		return FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
	}
	/**
	 * 根据周期类型获取实体名称
	 * @param timeType
	 * @return
	 */
	@Override
	public String  getEntityNameByTimeType(String timeType){
		return FbsBudgetDtlAbs.getEntityNameByTimeType(timeType);
	}
	/**
	 * 判断是否启用周期类型
	 *
	 * @param timeType
	 * @return
	 */
	@Override
	public boolean isExistTimeType(String timeType) {
		return fbsbudgetService.isExistTimeType(timeType);
	}

	@Override
	public List<FbsBudgetProcessCommonDTO> getItemDetail(FbsBudgetProjectDetailDTO projectDetailVO) {
		String businessName = projectDetailVO.getBusinessName();
		String processSysname = projectDetailVO.getProcessSysname();
		String corpId = projectDetailVO.getCorpId();
		String itemCode = projectDetailVO.getItemCode();
		Date processDate = projectDetailVO.getProcessDate();
		String billNo = projectDetailVO.getBillNo();
		String id = projectDetailVO.getId();
		String curId = projectDetailVO.getCurId();
		List<FbsBudgetProcess> processList = this.fbsBudgetProcessService.getBudgetProcesInfo(businessName, processSysname, billNo, null, id);
		List<FbsBudgetProcessCommonDTO> list = BeanUtils.copyListProperties(processList, FbsBudgetProcessCommonDTO.class);
		for(FbsBudgetProcessCommonDTO dto : list){
			FbsBudgetProcessCommonVO fbsBudgetProcessCommonVO = this.fbsbudgetService.getFbsBudgetProcessByItemCode(corpId, dto.getItemCode(), processDate, billNo, null, curId);
			dto.setItemMoney(fbsBudgetProcessCommonVO.getItemMoney());
			dto.setSuperItemMoney(fbsBudgetProcessCommonVO.getSuperItemMoney());
			dto.setTopItemMoney(fbsBudgetProcessCommonVO.getTopItemMoney());
			dto.setExchangeRate(fbsBudgetProcessCommonVO.getExchangeRate());
			dto.setItemId(fbsBudgetProcessCommonVO.getItemId());
			dto.setProcessRule(fbsBudgetProcessCommonVO.getProcessRule());
		}
		return list;
	}

	@Override
	public List<FbsBudgetProcessCommonDTO> getFbsBudgetProcessByItemCodeForList(List<FbsBudgetProcessDTO> list) {
		List<FbsBudgetProcessCommonVO> vos = fbsbudgetService.getFbsBudgetProcessByItemCodeForList(list);
		List<FbsBudgetProcessCommonDTO> dtos = BeanUtils.copyListProperties(vos, FbsBudgetProcessCommonDTO.class);
		return dtos;
	}
}
