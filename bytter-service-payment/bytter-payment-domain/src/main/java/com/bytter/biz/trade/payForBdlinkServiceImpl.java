package com.bytter.biz.trade;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.allocation.dao.NisAllocationReqDao;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.foreign.dao.FexCashFlowDao;
import com.bytter.biz.foreign.dao.FexForeignReqDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexForeignReq;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.payment.api.service.IpayForBdlinkService;
import com.bytter.biz.wage.dao.NisWageDao;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BdlinkConstant;

@DubboService
@Service("payForBdlinkService")
@Transactional
public class payForBdlinkServiceImpl implements IpayForBdlinkService{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	NisBillheadDao nisBillheadDao;
	@Autowired
	NisAllocationReqDao nisAllocationReqDao;
	@Autowired
	FexForeignReqDao fexForeignReqDao;
	@Autowired
	NisWageDao nisWageDao;
	@Autowired
	FexCashFlowDao fexCashFlowDao;
	

	@Override
	public AjaxJson bdlinkToPayTrade(String cbsBillId, String busiType, String toUpdateStatus,String voucvherFrom) {
				String tablename = "";
				try {
					if(BdlinkConstant.FUND_ALLOCATION.equals(busiType) || BdlinkConstant.CASH_SWEEP.equals(busiType)) {//NisAllocationReqDao
						NisAllocationReq nisAllocationReq = nisAllocationReqDao.get(cbsBillId, true);
						nisAllocationReq.setVoucherStat(toUpdateStatus);
						tablename = "NisAllocationReq";
						nisAllocationReq.setUpdateDate(new Date());
						nisAllocationReqDao.update(nisAllocationReq);
						logger.error("into  nisAllocation =================================table "+tablename+" success");
					}else if(BdlinkConstant.SINGLE_COMMON.equals(busiType) || BdlinkConstant.EXPENSE_REIMBURSEMENT.equals(busiType)) {//nisBillheadDao
						NisBillhead nis = nisBillheadDao.get(cbsBillId, true);
						nis.setVoucherStat(toUpdateStatus);
						tablename = "NisBillhead";
						nis.setUpdateDate(new Date());
						nisBillheadDao.update(nis);
						logger.error("into  nisBillhead =================================table "+tablename+" success");
					}
					else if(BdlinkConstant.PAYROLL.equals(busiType)) {
						NisWage nisWage = nisWageDao.get(cbsBillId, true);
						nisWage.setVoucherStat(toUpdateStatus);
						tablename = "NisWage";
						nisWage.setUpdateDate(new Date());
						nisWageDao.update(nisWage);
						logger.error("into  nisWage =================================table "+tablename+" success");
					}else if(BdlinkConstant.FOREIGN_EXCHANGE.equals(busiType)) {
						tablename = "FexCashFlow";
						if(!StringUtils.isBlank(voucvherFrom)) {
							if("1".equals(voucvherFrom)) {
								FexForeignReq fexForeignReq = fexForeignReqDao.get(cbsBillId, true);
								fexForeignReq.setVoucherStat(toUpdateStatus);
								fexForeignReq.setUpdateDate(new Date());
								fexForeignReqDao.update(fexForeignReq);
								logger.error("into  fexForeign =================================table "+tablename+" success");
							}else if("2".equals(voucvherFrom)) {
								FexCashFlow fexCashFlow = fexCashFlowDao.get(cbsBillId, true);
								fexCashFlow.setVoucherStat(toUpdateStatus);
								fexCashFlow.setUpdateDate(new Date());
								fexCashFlowDao.update(fexCashFlow);
								logger.error("into  fexCashFlow =================================table "+tablename+" success");
							}
						}else {
							logger.error("voucvherFrom is null ===================== !!");
							return new AjaxJson(false,"update "+tablename+"status failed");
						}
						
					}
					logger.error("update =================================table "+tablename+" success");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("update ======================"+tablename+"status failed due to {}",e.getMessage());
					return new AjaxJson(false,"update "+tablename+"status failed");
				}
				return new AjaxJson(true,"success");
	}
	
}
