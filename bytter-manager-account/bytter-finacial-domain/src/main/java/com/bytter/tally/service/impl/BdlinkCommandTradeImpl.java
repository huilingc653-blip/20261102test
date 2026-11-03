package com.bytter.tally.service.impl;

import java.util.HashMap;
import java.util.Map;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.dto.BisGatherCmdDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IBisForeignExcService;
import com.bytter.biz.payment.api.service.IBisGatherCmdService;
import com.bytter.increment.api.dto.BisFinanceExcDTO;
import com.bytter.increment.api.service.IBisFinanceExcService;
import com.bytter.tally.api.service.IBdlinkCommandTrade;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.core.constant.BdlinkConstant;

@Service("bdlinkCommandTrade")
@DubboService
@Transactional
public class BdlinkCommandTradeImpl implements IBdlinkCommandTrade {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@DubboReference(check = false)
	private IBisExcService iBisExcService;
	
	@DubboReference(check = false)
	private IBisGatherCmdService iBisGatherCmdService;

	@DubboReference(check = false)
	private IBisForeignExcService iBisForeignExcService;

	@DubboReference(check = false)
	IBisFinanceExcService iBisFinanceExcService;
	
	
	@Override
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

	/**
	 * result 0 更新交易状态失败   10 更新交易状态成功
	 */
	@Override
	public int updateTradeStatusForCommand(String voucherNo, String busiType, String toUpdateStatus) {
		int result = 0;
				/*try {
				if(BdlinkConstant.SINGLE_COMMON.equals(busiType) || BdlinkConstant.FUND_ALLOCATION.equals(busiType)
				   || BdlinkConstant.EXPENSE_REIMBURSEMENT.equals(busiType) || BdlinkConstant.PAYROLL.equals(busiType)) {
					BisExcVO vo = iBisExcService.getByVoucherNo(voucherNo);
					BisExc bisExc = new BisExc();
					BeanUtils.copyNotNullProperties(vo, bisExc);
					bisExc.setVoucherStat(toUpdateStatus);
					iBisExcService.update(bisExc);
					result = 1;
				}else if(BdlinkConstant.CASH_SWEEP.equals(busiType)) {
					BisGatherCmdVO gvo = iBisGatherCmdService.getByVoucherNo(voucherNo);
					BisGatherCmd gExc = new BisGatherCmd();
					BeanUtils.copyNotNullProperties(gvo, gExc);
					gExc.setCmdStat(toUpdateStatus);
					iBisGatherCmdService.update(gExc);
					result = 1;
				}else if(BdlinkConstant.FOREIGN_EXCHANGE.equals(busiType)) {
					BisForeignExcVO fvo = iBisForeignExcService.getByVoucherNo(voucherNo);
					BisForeignExc fExc = new BisForeignExc();
					BeanUtils.copyNotNullProperties(fvo, fExc);
					fExc.setVoucherStat(toUpdateStatus);
					iBisForeignExcService.update(fExc);
					result = 1;
				}else if(BdlinkConstant.FINANCIAL_INSTRUCTIONS.equals(busiType)) {
					BisFinanceExcDTO dto = iBisFinanceExcService.getByVoucherNo(voucherNo);
					dto.setVoucherStat(toUpdateStatus);
					iBisFinanceExcService.updateBisFinanceExc(dto);
					result = 1;
				}else {
					//donothing
				}
				}catch(Exception e) {
					logger.error("指令:"+voucherNo+" 业务类型:"+busiType +"更新交易状态失败 {}", e.getMessage());
					return result;
				}*/
		return result;
	}

}
