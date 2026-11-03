package com.bytter.biz.trade.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IBisForeignExcService;
import com.bytter.biz.payment.api.service.IBisGatherCmdService;
import com.bytter.biz.payment.api.service.IpayForBdlinkService;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
import com.bytter.increment.api.service.IBisFinanceExcService;
import com.bytter.increment.api.service.ICBFService;
import com.bytter.biz.trade.service.BdlinkToPayService;

@Service("bdlinkToPayService")
@Transactional
public class BdlinkToPayServiceImpl implements BdlinkToPayService{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@DubboReference(check = false)
	private ICBFService cBFService;
	
	@DubboReference
	private IpayForBdlinkService payForBdlinkService;
	
	
	@DubboReference(check = false)
	private IBisExcService iBisExcService;
	
	@DubboReference(check = false)
	private IBisGatherCmdService iBisGatherCmdService;

	@DubboReference(check = false)
	private IBisForeignExcService iBisForeignExcService;

	@DubboReference(check = false)
	IBisFinanceExcService iBisFinanceExcService;
	
	
	public Map<String,String> ibdlinkTrade(String voucherNo, String busiType) {
		Map<String,String> map = new HashMap<String,String>();
		String cbsBillId = "";
		if(StringUtils.isBlank(voucherNo) || StringUtils.isBlank(busiType)) {
			cbsBillId = "";
		}
		if(BdlinkConstant.SINGLE_COMMON.equals(busiType) || BdlinkConstant.FUND_ALLOCATION.equals(busiType)
		   || BdlinkConstant.EXPENSE_REIMBURSEMENT.equals(busiType) || BdlinkConstant.PAYROLL.equals(busiType)) {
			BisExcDTO bisExcDTO = iBisExcService.getByVoucherNo(voucherNo);
			cbsBillId = !ObjectUtils.isEmpty(bisExcDTO) && !StringUtils.isBlank(bisExcDTO.getCbsBillId()) ? bisExcDTO.getCbsBillId() : "";
		}else if(BdlinkConstant.CASH_SWEEP.equals(busiType)) {
			BisGatherCmdDTO bisGatherCmdDTO = iBisGatherCmdService.getByVoucherNo(voucherNo);
			cbsBillId = !ObjectUtils.isEmpty(bisGatherCmdDTO) && !StringUtils.isBlank(bisGatherCmdDTO.getCbsBillId()) ? bisGatherCmdDTO.getCbsBillId() : "";
		}else if(BdlinkConstant.FOREIGN_EXCHANGE.equals(busiType)) {
			BisForeignExcDTO bisForeignExcDTO = iBisForeignExcService.getByVoucherNo(voucherNo);
			cbsBillId = !ObjectUtils.isEmpty(bisForeignExcDTO) && !StringUtils.isBlank(bisForeignExcDTO.getCbsBillId()) ? bisForeignExcDTO.getCbsBillId() : "";
			map.put("voucherFrom", bisForeignExcDTO.getVoucherFrom());
		}else if(BdlinkConstant.FINANCIAL_INSTRUCTIONS.equals(busiType)) {
			BisFinanceExcDTO dto = iBisFinanceExcService.getByVoucherNo(voucherNo);
			cbsBillId = !ObjectUtils.isEmpty(dto) && !StringUtils.isBlank(dto.getCbsBillId()) ? dto.getCbsBillId() : "";
		}else {
			//donothing
		}
		map.put("cbsBillId", cbsBillId);
		return map;
	}

	
	/***
	 * String voucher_no 指令编号
	 * String busiType 业务类型
	 * String toUpdateStatus 待更新指令状态
	 */
	@Override
	public AjaxJson bdlinkToPayTrade(String voucherNo, String busiType, String toUpdateStatus) {
		logger.error("银企直连回调-指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " start");
		Map<String,String> map = ibdlinkTrade(voucherNo, busiType);
		if(map==null || map.size()==0) {
			logger.error("没有找到cbsBillId对应的entity");
			return new AjaxJson(false,"no voucher of entity!");
		}
		AjaxJson ajaxJson = new AjaxJson(true,"success");
		try {
			String voucherFrom = (String)map.get("voucherFrom");
			String cbsBillId = (String)map.get("cbsBillId");
			if(BdlinkConstant.FUND_ALLOCATION.equals(busiType) 
						|| BdlinkConstant.CASH_SWEEP.equals(busiType) 
						|| BdlinkConstant.SINGLE_COMMON.equals(busiType) 
						|| BdlinkConstant.EXPENSE_REIMBURSEMENT.equals(busiType)
						|| BdlinkConstant.PAYROLL.equals(busiType)
						|| BdlinkConstant.FOREIGN_EXCHANGE.equals(busiType)) {
				logger.error("资金下拨,资金归集,单笔普通付款,费用报销,代发工资,外汇指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " start");
				ajaxJson = payForBdlinkService.bdlinkToPayTrade(cbsBillId, busiType, toUpdateStatus, voucherFrom);
				logger.error("资金下拨,资金归集,单笔普通付款,费用报销,代发工资,外汇指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " start 更新结果:"+ajaxJson.toString());
				
			}else if(BdlinkConstant.FINANCIAL_INSTRUCTIONS.equals(busiType)) {
				logger.error("理财指令指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " start");
				ChoBankFinanceDTO dto = cBFService.getChoBankFinanceById(cbsBillId);
				if(ObjectUtils.isEmpty(dto)) {
					return new AjaxJson(false,"no voucher of entity!");
				}
				int result = cBFService.updateBankFinanceByVoucherNo(dto);
				if(result==1) {
					ajaxJson = new AjaxJson(true,"success");
				}else {
					logger.error("ChoBankFinance  指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " failed");
					ajaxJson = new AjaxJson(false,"update ChoBankFinance failed!");
				}
				logger.error("理财指令指令======:  result="+result+" 更新结果:ajaxJson="+ajaxJson.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("update status failed due to {}", e.getMessage());
			return new AjaxJson(false,"update status failed due to"+e.getMessage());
		}
		logger.error("银企直连回调-指令: "+voucherNo+" 业务类型: "+busiType+" 更新状态 : " +toUpdateStatus+  " end");
		return ajaxJson;
	}

}
