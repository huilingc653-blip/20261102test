package com.bytter.cashVoucher.service;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.cashVoucher.dao.BvPayeeChequeCheckDao;
import com.bytter.cashVoucher.dao.BvPayeeChequeDao;
import com.bytter.cashVoucher.entity.BvPayeeCheque;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: BvPayeeChequeService
 * @Description: 支票收款复核业务处理类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 * 
 */
@Service("bvPayeeChequeCheckService")
@Transactional
public class BvPayeeChequeCheckService extends BaseService<BvPayeeCheque, String> {
	@Autowired
	private BvPayeeChequeCheckDao bvPayeeChequeCheckDao;
	@Autowired
	private BvPayeeChequeDao bvPayeeChequeDao;
	@Autowired
	private BvLogService bvLogService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	
	/**
	 * 
	* @Title: operateUpdate  
	* @Description: TODO(操作复核通过和打回)  
	* @param @param id 
	* @param @param reason
	* @param @param operate
	* @param @param userVo
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public boolean operateUpdate(String id,String reason,String operate,ICurrentUser userVo,Integer status){
		BvPayeeCheque bvPayeeCheque = get(id,true);
		bvPayeeCheque.setUpdateName(userVo.getUsername());
		bvPayeeCheque.setUpdateBy(userVo.getLoginName());
		bvPayeeCheque.setUpdateDate(new Date());
		bvPayeeCheque.setStatus(status);
		try {
			String logType="",msg="";
			if(PayeeChequeConstant.BV_CHEQUE_TYPE_BACK.equals(operate)){ //打回
				logType = BlankvouConstant.BVLOG_LOGTYPE_CHECKBACK;
				msg= reason;
			}else if(PayeeChequeConstant.BV_CHEQUE_TYPE_CHECK.equals(operate)){ //通过
				logType = BlankvouConstant.BVLOG_LOGTYPE_CHECKOK;
				msg= PayeeChequeConstant.LANG_LOG_CHECK_SUCC;
			}else{
				return false;
			}
			bvPayeeChequeCheckDao.saveOrUpdate(bvPayeeCheque);
			// 清除消息提醒
			notificationService.complete(NotificationConstant.CASHVOUCHER.CASHVOUCHER_BVPAYEECHEQUE_CHECK, bvPayeeCheque.getId(), userVo.getId());
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
	* @Description: TODO(批量操作复核通过和打回)  
	* @param @param ids 多条id
	* @param @param rmk    备注
	* @param @param operate 操作类型
	* @param @param userVo  当前用户信息
	* @param @return    参数  
	* @return boolean    返回类型  
	* @throws
	 */
	public 	synchronized boolean batchOperateUpdate(String ids,String rmk,String operate,ICurrentUser userVo,Integer status){
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("ids", ids.split(","));
		params.put("status",status );
		int num = bvPayeeChequeDao.batchDeleteBvPayeeCheque(params,userVo);
		if(num>0){
			String logType="",msg="";
			if(PayeeChequeConstant.BV_CHEQUE_TYPE_BATHBACK.equals(operate)){//批量打回
				logType = BlankvouConstant.BVLOG_LOGTYPE_CHECKBACK;
				msg=rmk;
			}else if(PayeeChequeConstant.BV_CHEQUE_TYPE_BATHCHECK.equals(operate)){//批量复核通过
				logType = BlankvouConstant.BVLOG_LOGTYPE_CHECKOK;
				msg=PayeeChequeConstant.LANG_LOG_CHECK_SUCC;
			}
			String[] msgs = new String[ids.split(",").length];
			for(int i=0;i<ids.split(",").length;i++){
				msgs[i] = msg;
				// 清除消息提醒
				notificationService.complete(NotificationConstant.CASHVOUCHER.CASHVOUCHER_BVPAYEECHEQUE_CHECK, ids.split(",")[i], userVo.getId());
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
