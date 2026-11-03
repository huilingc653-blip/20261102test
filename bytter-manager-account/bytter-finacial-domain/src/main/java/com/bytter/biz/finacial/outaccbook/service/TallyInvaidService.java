package com.bytter.biz.finacial.outaccbook.service;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.outaccbook.OutAccbookMessageEnum;
import com.bytter.biz.finacial.outaccbook.dao.TallyDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtlHis;
import com.bytter.biz.finacial.outaccbook.entity.FcPreOutAccbook;
import com.bytter.biz.finacial.outaccbook.vo.AccBookHisVO;
import com.bytter.biz.finacial.outaccbook.vo.AccBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @Title: Service
* @Description: 表外记账作废
* @author onlineGenerator
* @date 2017-06-30 11:04:08
* @version 1.0
*
*/
@Service("tallyInvaidService")
@Transactional
public class TallyInvaidService extends BaseService<FcOutAccbook,String>{
	
	@Autowired
	private TallyDao tallyDao;
	@Autowired
	private TallyService tallyService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private ICorpService  corpService;
	@Override
	public BaseDao<FcOutAccbook, String> getEntityDao() {
		return tallyDao;
	}
	
	
	/**
	 * 查询作废-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyCancelData(DataTablesInput dataTablesInput,String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		return tallyDao.queryTableTallyCancelData(dataTablesInput,user,corpAuth);
	}
	
	/**
	 * 查询作废修改-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyCancelUpdateData(DataTablesInput dataTablesInput,String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		return tallyDao.queryTableTallyCancelUpdateData(dataTablesInput,user,corpAuth);
	}
	
	/**
	 * 查询作废复核-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyCancelCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		return tallyDao.queryTableTallyCancelCheckData(dataTablesInput,user);
	}
	
	/**
	 * 功能介绍 : 记账作废暂存<br>	
	 */
	public void tallyCancelTemp(String id,String reason, ICurrentUser user){
		AccBookVO vo = beformCancel(null,id,reason,user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_INVALID_EDIT, vo.getBook().getId(), user.getId());
	}
	
	/**
	 * 功能介绍 : 记账作废提交<br>	
	 */
	public void tallyCancelSubmit(String id, String reason, ICurrentUser user) {
		AccBookVO vo = beformCancel(OperateType.SUBMIT, id, reason, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_INVALID_EDIT, vo.getBook().getId(), user.getId());
		notificationService.sendIgnoreMySelf(vo.getBook().getId(), vo.getBook().getNetId(),
				NotificationConstant.FINACIAL.OUTBOOK_INVALID_CHECK, vo.getBook(), user.getId());
	}
	
	/**
	 * 功能介绍 : 删除作废<br>	
	 */
	public void tallyCancelDelete(String id, ICurrentUser user){
		AccBookVO vo = beformCancel(OperateType.DELETE, id, null, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_INVALID_EDIT, vo.getBook().getId(), user.getId());
	}
	
	/**
	 * 功能介绍 : 作废打回<br>	
	 */
	public void tallyCancelBack(String id,String reason, ICurrentUser user){
		AccBookVO vo = beformCancel(OperateType.BACK, id, reason, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_INVALID_CHECK, vo.getBook().getId(), user.getId());
		notificationService.sendToUser(vo.getBook().getId(), vo.getBook().getInvalidOpCode(), NotificationConstant.FINACIAL.OUTBOOK_INVALID_EDIT, vo.getBook());
	}
	
	/**
	 * 功能介绍 : 作废复核<br>	
	 */
	public void tallyCancelCheck(String id, ICurrentUser user){
		AccBookVO vo = beformCancel(OperateType.CHECK, id, null, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_INVALID_CHECK, vo.getBook().getId(), user.getId());
	}
	
	/**
	 * 作废前数据处理
	 */
	public AccBookVO beformCancel(OperateType opType,String id,String reason, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_INVALID);
		voucher.setOpType(opType);
		voucher.setUserCode(user.getId());//设置用户
		AccBookVO vo = cancel(voucher,id,reason);//作废处理
		//账户余额处理
		tallyService.updateAccBalance(voucher);
		return vo;
	}
	
	/**
	 * 功能介绍 : 记账作废处理<br>	
	 */
	public AccBookVO cancel(VoucherWrapper voucher,String id,String reason){
		AccBookVO vo = tallyService.getAccBookVoByid(id);
		Integer vouStatus = vo.getBook().getStatus();
		OperateType operateType = getOpType(vouStatus,voucher.getOpType());
		voucher.setOpType(operateType);
		tallyService.setVoucherWrapper(voucher,vo);//将表外记账信息保存到凭证封装对象
		if(operateType == OperateType.MODIFY ){//修改操作   || (operateType == OperateType.SUBMIT && (vouStatus.intValue() ==  OuterStatus.STATUE_INVALID_TEMP.get() || vouStatus.intValue() == OuterStatus.STATUE_INVALID_BACK.get()))
			tallyService.setVoucherWrapperOri(voucher,vo);
		}
		//检查凭证状态
		checkCancelStatus(operateType,vo.getBook());
		// 作废复核之前将写入FcPreOutAccBook表
		if (OperateType.CHECK.equals(operateType)) {
			AccBookHisVO hisVO = new AccBookHisVO();
			setAccBookToHis(hisVO,vo);//复制信息到历史记录表中
			vo.getBook().setInvalidCheckDate(DateUtils.getDate());
			vo.getBook().setInvalidChecker(voucher.getUserCode());
			tallyService.saveAccBookHis(hisVO);
			vo.getBook().setStatus(OuterStatus.STATUE_INVALID_CHECK.get());
		}
		// 更新作废原因与作废操作人与时间
		if (operateType == OperateType.SUBMIT || OperateType.ADD == operateType
				|| operateType == OperateType.MODIFY || operateType == OperateType.DELETE) {
			if (operateType == OperateType.DELETE) {
				vo.getBook().setInvalidOpCode(null);
				vo.getBook().setInvalidOpTime(null);
				vo.getBook().setInvalidBackReason(null);
				vo.getBook().setInvalidReason(null);
				vo.getBook().setStatus(OuterStatus.STATUE_TALLY_CHECK.get());
			} else if(OperateType.ADD == operateType || operateType == OperateType.MODIFY || operateType == OperateType.SUBMIT){
				vo.getBook().setInvalidOpCode(voucher.getUserCode());//作废人员
				vo.getBook().setInvalidOpTime(DateUtils.getDate());//作废时间
				vo.getBook().setInvalidReason(reason);
				if(operateType == OperateType.SUBMIT){
					vo.getBook().setStatus(OuterStatus.STATUE_INVALID_WAIT_CHECK.get());
				}else{
					vo.getBook().setStatus(OuterStatus.STATUE_INVALID_TEMP.get());
				}
			}
		}else if(OperateType.BACK == operateType){
			vo.getBook().setInvalidBackReason(reason);
			vo.getBook().setStatus(OuterStatus.STATUE_INVALID_BACK.get());
		}
		this.saveOrUpdate(vo.getBook());//保存表外记账
		return vo;
	}
	
	
	
	/**
	 * 功能介绍 : 暂存获取操作类型，其他操作返回原有操作<br>	
	 */
	@Transactional(readOnly = true)
	protected OperateType getOpType(Integer vouStatus,OperateType opType){
		OperateType type = null;
		if (opType != null) {
			return opType;
		}
		
		if(vouStatus.intValue() == OuterStatus.STATUE_TALLY_CHECK.get()){
			type = OperateType.ADD;
		}else if (vouStatus.intValue() == OuterStatus.STATUE_INVALID_TEMP.get() || vouStatus.intValue() == OuterStatus.STATUE_INVALID_BACK.get()){
			type = OperateType.MODIFY;
		}
		
		return type;
	}
	
	/**
	 * 功能介绍 : 将信息复制到历史对象中<br>	
	 */
	@Transactional(readOnly = true)
	public void setAccBookToHis(AccBookHisVO hisVO,AccBookVO vo){
		FcPreOutAccbook bookHis = new FcPreOutAccbook();
		BeanUtils.copyNotNullProperties(vo.getBook(), bookHis);//复制属性到历史对象
		bookHis.setBookId(vo.getBook().getId());
		hisVO.setBookHis(bookHis);
		hisVO.setDtlsHis(BeanUtils.copyListProperties(vo.getDtls(), FcOutAccbookDtlHis.class));
	}
	
	
	
	/**
	 * 
	 * 功能介绍 : 记账作废状态验证<br>
	 */
	protected void checkCancelStatus(OperateType opType,FcOutAccbook book){
 		Integer status = book.getStatus();
 		if(opType == OperateType.ADD || opType == OperateType.MODIFY || opType == OperateType.SUBMIT){
 			if(status.intValue() != OuterStatus.STATUE_TALLY_CHECK.get().intValue() && status.intValue() != OuterStatus.STATUE_INVALID_TEMP.get().intValue() && status.intValue() != OuterStatus.STATUE_INVALID_BACK.get().intValue()){
 				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
 			}
 		}else if(opType == OperateType.DELETE){
 			if(status.intValue() != OuterStatus.STATUE_INVALID_BACK.get().intValue() && status.intValue() != OuterStatus.STATUE_INVALID_TEMP.get().intValue()){
 				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
 			}
 		}else if(opType == OperateType.BACK || opType == OperateType.CHECK){
 			if(status.intValue() != OuterStatus.STATUE_INVALID_WAIT_CHECK.get().intValue()){
 				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
 			}
 		}
 	}
}
