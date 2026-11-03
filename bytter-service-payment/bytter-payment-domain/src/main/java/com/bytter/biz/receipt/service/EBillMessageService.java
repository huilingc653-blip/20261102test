package com.bytter.biz.receipt.service;
import java.util.Date;
import com.bytter.core.base.entity.ICurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.receipt.dao.EBillMessageDao;
import com.bytter.biz.receipt.entity.EBillMessage;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
/**
* @Title: Service
* @Description: E_BILL_MESSAGE
* @author onlineGenerator
* @date 2017-08-22 17:34:11
* @version 1.0
*
*/
@Service("eBillMessageService")
@Transactional
public class EBillMessageService extends BaseService<EBillMessage,String> {
	@Autowired
	private EBillMessageDao eBillMessageDao;
	/**
	* 保存
	* @param entity
	*/
 	public void save(EBillMessage entity){
 		super.saveEntity(entity);
 	}
	@Override
	public BaseDao<EBillMessage, String> getEntityDao() {
		return eBillMessageDao;
	}
	
	/**
	 * 创建电子回单日志信息
	 * @param billNo 回单编号
	 * @param opType 回单类型
	 * @param msg 消息
	 */
	public void saveEBillMessage(String billNo,Integer opType,String msg, ICurrentUser user){
		EBillMessage entity=new EBillMessage();
		entity.setBillNo(billNo);
		entity.setOpType(opType);
		entity.setMessage(msg);
		entity.setEnrolDate(new Date());
		entity.setOp(user.getLoginName());
		entity.setOpName(user.getUsername());
		eBillMessageDao.save(entity);
	}
}
