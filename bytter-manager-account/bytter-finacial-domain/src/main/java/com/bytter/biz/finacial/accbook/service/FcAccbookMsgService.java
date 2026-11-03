package com.bytter.biz.finacial.accbook.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.dao.FcAccbookMsgDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookMsg;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
/**
* @Title: Service
* @Description: FC_ACCBOOK_MSG
* @author onlineGenerator
* @date 2017-06-16 16:15:08
* @version 1.0
*
*/
@Service("fcAccbookMsgService")
@Transactional
public class FcAccbookMsgService extends BaseService<FcAccbookMsg,String> {
	@Autowired
	private FcAccbookMsgDao fcAccbookMsgDao;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcAccbookMsg entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcAccbookMsg entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccbookMsg entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcAccbookMsg entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcAccbookMsg, String> getEntityDao() {
		return fcAccbookMsgDao;
	}
	
	/**
	 * 功能介绍 : 保存账务操作日志记录。<br>
	 * <b>日期：</b>2017年10月31日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void saveBookMsg(String bookId, String msgType, OperateType opType, String msg, String userId) {
		FcAccbookMsg entity = new FcAccbookMsg();

		entity.setBookId(bookId);//账务表ID
		entity.setMsgType(msgType);//账务操作类型
		//记录凭证操作日志表
		String msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_0;
		if (OperateType.ADD.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_0;
		} else if (OperateType.MODIFY.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_1;
		} else if (OperateType.SUBMIT.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_2;
		} else if (OperateType.DELETE.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_3;
		} else if (OperateType.BACK.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_4;
		} else if (OperateType.CHECK.equals(opType)) {
			msgStatus = FinacialConstant.FINACIAL_MSGSTATUS_5;
		}
		entity.setMsgStatus(msgStatus);//账务状态
		entity.setMsg(msg);//操作记录
		entity.setCreateBy(userId);
		entity.setCreateTime(DateUtils.getDate());
		save(entity);
	}
}
