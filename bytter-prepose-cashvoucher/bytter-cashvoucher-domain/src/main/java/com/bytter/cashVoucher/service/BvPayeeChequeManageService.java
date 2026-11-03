package com.bytter.cashVoucher.service;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.cashVoucher.dao.BvPayeeChequeCheckDao;
import com.bytter.cashVoucher.dao.BvPayeeChequeDao;
import com.bytter.cashVoucher.entity.BvPayeeCheque;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: bvPayeeChequeManageService
 * @Description: 支票收款登记业务处理类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
@Service("bvPayeeChequeManageService")
@Transactional
public class BvPayeeChequeManageService extends BaseService<BvPayeeCheque, String> {
	@Autowired
	private BvPayeeChequeCheckDao bvPayeeChequeCheckDao;
	@Autowired
	private BvPayeeChequeDao bvPayeeChequeDao;
	@Autowired
	private BvLogService bvLogService;
	/**
	 *
	 * @Title: operateUpdate
	 * @Description: TODO(操作更新)
	 * @param @param id
	 * @param @param reason
	 * @param @param operate
	 * @param @param userVo
	 * @param @return    参数
	 * @return boolean    返回类型
	 * @throws
	 */
	public boolean operateUpdate(String id,String operate,ICurrentUser userVo,Integer status){
		BvPayeeCheque bvPayeeCheque = get(id,true);
		bvPayeeCheque.setUpdateName(userVo.getUsername());
		bvPayeeCheque.setUpdateBy(userVo.getLoginName());
		bvPayeeCheque.setUpdateDate(new Date());
		bvPayeeCheque.setStatus(status);
		try {
			String logType="",msg="";
			switch (operate) {
				// 退票
				case PayeeChequeConstant.BV_CHEQUE_TYPE_RETREAT:
					logType = BlankvouConstant.BVLOG_LOGTYPE_BACKBILL;
					msg= BlankvouConstant.AB_BILL_RESOURCE_BVPAYEECHEQUEODTL_RETURNTICKET;
					break;
				// 作废
				case PayeeChequeConstant.BV_CHEQUE_TYPE_TOVOID:
					logType = BlankvouConstant.BVLOG_LOGTYPE_CANCELLATION;
					msg= BlankvouConstant.AB_BILL_RESOURCE_BVPAYEECHEQUEDTL_CANCEL;
					break;
				// 托收申请
				case PayeeChequeConstant.BV_CHEQUE_TYPE_COLLECTION:
					logType = BlankvouConstant.BVLOG_LOGTYPE_COLLECTION;
					msg= BlankvouConstant.BLANKVOU_BYPAYEECHEQUE_MSG_COLLECTION_SUCC;
					break;
				// 到账
				case PayeeChequeConstant.BV_CHEQUE_TYPE_ARRIVALACCOUNT:
					logType = BlankvouConstant.BVLOG_LOGTYPE_ARRIVALACCOUNT;
					msg= BlankvouConstant.BLANKVOU_BYPAYEECHEQUE_MSG_ARRIVALACCOUNT_SUCC;
					break;
				default:
					break;
			}
			bvPayeeChequeCheckDao.saveOrUpdate(bvPayeeCheque);
			bvLogService.saveBvLog(bvPayeeCheque.getId(),logType,msg, userVo);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 *
	 * @Title: batchOperateUpdate
	 * @Description: TODO(批量操作退票和作废)
	 * @param @param ids 多条id
	 * @param @param rmk    备注
	 * @param @param operate 操作类型
	 * @param @param userVo  当前用户信息
	 * @param @return    参数
	 * @return boolean    返回类型
	 * @throws
	 */
	public 	synchronized boolean batchOperateUpdate(String ids,String operate,ICurrentUser userVo,Integer status){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("ids", ids.split(","));
		params.put("status",status );
		int num = bvPayeeChequeDao.batchDeleteBvPayeeCheque(params,userVo);
		if(num>0){
			String logType="",msg="";
			switch (operate) {
				// 退票
				case PayeeChequeConstant.BV_CHEQUE_TYPE_BATHRETREAT:
					logType = BlankvouConstant.BVLOG_LOGTYPE_BACKBILL;
					msg= BlankvouConstant.AB_BILL_RESOURCE_BVPAYEECHEQUEODTL_RETURNTICKET;
					break;
				// 作废
				case PayeeChequeConstant.BV_CHEQUE_TYPE_BATHTOVOID:
					logType = BlankvouConstant.BVLOG_LOGTYPE_CANCELLATION;
					msg= BlankvouConstant.AB_BILL_RESOURCE_BVPAYEECHEQUEDTL_CANCEL;
					break;
				// 到账
				case PayeeChequeConstant.BV_CHEQUE_TYPE_BATHARRIVALACCOUNT:
					logType = BlankvouConstant.BVLOG_LOGTYPE_ARRIVALACCOUNT;
					msg= BlankvouConstant.BLANKVOU_BYPAYEECHEQUE_MSG_ARRIVALACCOUNT_SUCC;
					break;
				default:
					break;
			}
			String[] msgs = new String[ids.split(",").length];
			for(int i=0;i<ids.split(",").length;i++){
				msgs[i] = msg;
			}
			bvLogService.batchSaveBvLog(ids.split(","), logType, msgs,userVo);
			return true;
		}
		return false;
	}
	@Override
	public BaseDao<BvPayeeCheque, String> getEntityDao() {
		return bvPayeeChequeDao;
	}
}
