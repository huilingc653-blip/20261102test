package com.bytter.biz.finacial.outaccbook.service;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.outaccbook.OutAccbookMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.outaccbook.dao.TallyDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtl;
import com.bytter.biz.finacial.outaccbook.vo.AccBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 表外记账冲销
* @author 吕伟林
* @date 2017-06-30 11:04:08
* @version 1.0
*
*/
@Service("tallyChargeService")
@Transactional
public class TallyChargeService extends BaseService<FcOutAccbook,String>{
	@Autowired
	private TallyDao tallyDao;
	@Autowired
	private TallyService tallyService;
	@DubboReference(check = false)
	private IFcAccService fcAccServiceImpl;
	@DubboReference(check = false)
	private ICorpService corpServiceImpl;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private ICorpService  corpService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@Override
	public BaseDao<FcOutAccbook, String> getEntityDao() {
		return tallyDao;
	}
	/**
	 * 查询作废-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyChargeData(DataTablesInput dataTablesInput,String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		return tallyDao.queryTableTallyChargeData(dataTablesInput,user,corpAuth);
	}
	/**
	 * 查询作废修改-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyChargeUpdateData(DataTablesInput dataTablesInput,String resource, ICurrentUser user) {
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		return tallyDao.queryTableTallyChargeUpdateData(dataTablesInput,user,corpAuth);
	}
	/**
	 * 查询作废复核-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyChargeCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		return tallyDao.queryTableTallyChargeCheckData(dataTablesInput, user);
	}
	/**
	 * 功能介绍 : 记账冲账暂存<br>
	 */
	public void tallyChargeTemp(String id, FcOutAccbook book, ICurrentUser user){
		book.setStatus(OuterStatus.STATUE_RUSH_TEMP.get());
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_RUSH);
		voucher.setOpType(OperateType.ADD);
		voucher.setUserCode(user.getId());//设置用户
		AccBookVO vo = submitOrTempAccBook(voucher, id, book, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_RUSH_EDIT, vo.getBook().getId(),user.getId());
		//账户余额处理
		tallyService.updateAccBalance(voucher);
	}
	/**
	 * 功能介绍 : 记账冲账提交<br>
	 */
	public void tallyChargeSubmit(String id,FcOutAccbook book, ICurrentUser user) {
		book.setStatus(OuterStatus.STATUE_RUSH_WAIT_CHECK.get());
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_RUSH);
		voucher.setOpType(OperateType.SUBMIT);
		voucher.setUserCode(user.getId());//设置用户
		AccBookVO vo = submitOrTempAccBook(voucher, id, book, user);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_RUSH_EDIT, vo.getBook().getId(), user.getId());
		notificationService.sendIgnoreMySelf(vo.getBook().getId(), vo.getBook().getNetId(), NotificationConstant.FINACIAL.OUTBOOK_RUSH_CHECK, vo.getBook(), user.getId());
		//账户余额处理
		tallyService.updateAccBalance(voucher);
	}
	/**
	 * 功能介绍 : 删除冲账<br>
	 */
	public void tallyChargeDelete(FcOutAccbook book, ICurrentUser user){
		beformCharge(OperateType.DELETE, book, user);
		AccBookVO vo = tallyService.getAccBookVoByid(book.getId());
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_RUSH_EDIT, book.getId(), user.getId());
	}
	/**
	 * 功能介绍 : 冲账打回<br>
	 */
	public void tallyChargeBack(FcOutAccbook book, ICurrentUser user){
		book.setStatus(OuterStatus.STATUE_RUSH_BACK.get());
		beformCharge(OperateType.BACK, book, user);
		AccBookVO vo = tallyService.getAccBookVoByid(book.getId());
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_RUSH_CHECK, vo.getBook().getId(), user.getId());
		notificationService.sendToUser(vo.getBook().getId(), vo.getBook().getOperatorCode(), NotificationConstant.FINACIAL.OUTBOOK_RUSH_EDIT, vo.getBook());
	}
	/**
	 * 功能介绍 : 冲账复核<br>
	 */
	public void tallyChargeCheck(FcOutAccbook book, ICurrentUser user){
		book.setStatus(OuterStatus.STATUE_RUSH_CHECK.get());
		beformCharge(OperateType.CHECK, book, user);
		AccBookVO vo = tallyService.getAccBookVoByid(book.getId());
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_RUSH_CHECK, vo.getBook().getId(), user.getId());
	}
	/**
	 * 冲账前数据处理
	 */
	public void beformCharge(OperateType opType,FcOutAccbook book, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_RUSH);
		voucher.setOpType(opType);
		voucher.setUserCode(user.getId());//设置用户
		charge(voucher, book, user);//冲销处理
		//账户余额处理
		tallyService.updateAccBalance(voucher);
	}
	/**
	 * 功能介绍 : 冲账处理审批处理<br>
	 */
	protected void charge(VoucherWrapper voucher,FcOutAccbook book, ICurrentUser user){
		OperateType opType = voucher.getOpType();
		AccBookVO rushVO = tallyService.getAccBookVoByid(book.getId());
		tallyService.setVoucherWrapper(voucher, rushVO);
		checkChargeStatus(opType,rushVO.getBook());
		if(OperateType.DELETE == opType){
			AccBookVO oriVO = tallyService.getAccBookVoByid(rushVO.getBook().getRelAccbookId());
			oriVO.getBook().setRushSign(FinacialConstant.BOOLEAN_FALSE);
			this.saveOrUpdate(oriVO.getBook());
			tallyDao.delete(rushVO.getBook());
			tallyDao.delDtlByOutId(book.getId());
		}else if(OperateType.BACK == opType){
			rushVO.getBook().setStatus(book.getStatus());
			rushVO.getBook().setBackReason(book.getBackReason());
			tallyDao.saveOrUpdate(rushVO.getBook());
		}else if(OperateType.CHECK == opType){
			rushVO.getBook().setStatus(book.getStatus());
			rushVO.getBook().setCheckerCode(user.getId());//设置复合人
			rushVO.getBook().setCheckTime(new Date());
			tallyDao.saveOrUpdate(rushVO.getBook());
		}
	}
	/**
	 * 暂存或提交冲销
	 */
	public AccBookVO submitOrTempAccBook(VoucherWrapper voucher,String id, FcOutAccbook book, ICurrentUser user){
		validate(book);
		AccBookVO oriVO = tallyService.getAccBookVoByid(id);
		AccBookVO rushVO = new AccBookVO();
		if(EmptyUtils.isEmpty(book.getId())){//新增
			if(FinacialConstant.BOOLEAN_TRUE.equals(oriVO.getBook().getRushSign())){
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,oriVO.getBook().getOrderNo());
			}
			FcOutAccbook rushBook = new FcOutAccbook();
			BeanUtils.copyProperties(oriVO.getBook(), rushBook);
			rushBook.setId(null);
			rushBook.setVoucherNo(null);
			rushBook.setOperatorCode(user.getId());
			rushBook.setCheckerCode(null);
			rushBook.setCheckTime(null);
			String netCode = "";
			String netId = "";//网点ID
			if(!EmptyUtils.isEmptyList(oriVO.getDtls())){
				FcAccDTO acc = fcAccServiceImpl.getAccDTOByAccount(oriVO.getDtls().get(0).getAccount());
				CorpDTO corp = corpServiceImpl.getParentById(acc.getCorpId());
				if(EmptyUtils.isEmpty(corp)){
					corp = corpServiceImpl.get(acc.getCorpId());
				}
				netCode = corp.getCode();
				netId = corp.getId();
			}else{
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_NONENTITY,"");
			}
			rushBook.setOrderNo(tallyService.getAccbookOrderNo(netCode));//TODO 流水号
			rushBook.setRedblueSign(FinacialConstant.REDBLUE_SIGN_RED);
			rushBook.setRelAccbookId(oriVO.getBook().getId());
			//用户输入信息
			rushBook.setRushReason(book.getRushReason());
			rushBook.setTallyDate(book.getTallyDate());
			rushBook.setStatus(book.getStatus());
			rushBook.setEnrolTime(new Date());
			rushVO.setBook(rushBook);
			//判断记账日期是否比已日结日期早
			Date dayOverDate = dayCloseService.getDayCloseFinalDate(netId);//根据网点获取已日结日期
			//判断记账日期是否比已日结日期早
			if (rushBook.getTallyDate().compareTo(dayOverDate) <= 0) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED,
						DateUtils.formatDate(rushBook.getTallyDate()));
			}
			this.saveEntity(rushVO.getBook());//保存获取ID
			oriVO.getBook().setRushSign(FinacialConstant.BOOLEAN_TRUE);
			this.saveOrUpdate(oriVO.getBook());
			List<FcOutAccbookDtl> dtls = new ArrayList<FcOutAccbookDtl>();
			for(FcOutAccbookDtl dtl : oriVO.getDtls()){
				FcOutAccbookDtl newDtl = new FcOutAccbookDtl();
				BeanUtils.copyProperties(dtl, newDtl);
				newDtl.setId(null);
				newDtl.setOutAccbookId(rushVO.getBook().getId());
				newDtl.setDebitMoney(BigDecimal.ZERO.subtract(dtl.getDebitMoney()));
				newDtl.setCreditMoney(BigDecimal.ZERO.subtract(dtl.getCreditMoney()));
                dtls.add(newDtl);
			}
			rushVO.setDtls(dtls);
			List<String> accounts = dtls.stream().map(FcOutAccbookDtl::getAccount).collect(Collectors.toList());
			List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
			Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
			dtls.forEach( a -> {
				FcAccDTO fcAccDTO = map.get(a.getAccount());
				a.setAccName(fcAccDTO.getAccName());
				a.setSubjectId(fcAccDTO.getSubjectId());
				a.setSubjectName(fcAccDTO.getSubjectName());
				a.setCorpId(fcAccDTO.getCorpId());
				a.setBalanceWay(fcAccDTO.getBalanceWay());
				a.setCurId(fcAccDTO.getCurId());
				a.setCurName(fcAccDTO.getCurName());
				a.setMeasureUnit(fcAccDTO.getMeasureUnit());
			});
			tallyDao.batchSave(dtls);
		}else{//修改
			rushVO = tallyService.getAccBookVoByid(book.getId());
			tallyService.setVoucherWrapperOri(voucher, rushVO);//原始凭证
			if(rushVO.getBook().getStatus().intValue() != OuterStatus.STATUE_RUSH_BACK.get().intValue() && rushVO.getBook().getStatus().intValue() != OuterStatus.STATUE_RUSH_TEMP.get().intValue()){
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,oriVO.getBook().getOrderNo());
			}
			FcOutAccbook rushBook = rushVO.getBook();
			//判断记账日期是否比已日结日期早
			Date dayOverDate = dayCloseService.getDayCloseFinalDate(rushBook.getNetId());//根据网点获取已日结日期
			//判断记账日期是否比已日结日期早
			if (rushBook.getTallyDate().compareTo(dayOverDate) <= 0) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED,
						DateUtils.formatDate(rushBook.getTallyDate()));
			}
			//用户输入信息
			rushBook.setRushReason(book.getRushReason());
			rushBook.setTallyDate(book.getTallyDate());
			rushBook.setUpdateBy(user.getId());
			rushBook.setUpdateTime(new Date());
			rushBook.setStatus(book.getStatus());
			//判断记账日期是否比已日结日期早 TODO
			this.saveOrUpdate(rushBook);
		}
		tallyService.setVoucherWrapper(voucher, rushVO);
		return rushVO;
	}
	/**
	 * 功能介绍 : 验证<br>
	 */
	protected void validate(FcOutAccbook book){
		Integer status = book.getStatus();
		if(book.getTallyDate() == null){
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_TALLYDATE_EMPTY);
		}
	}
	/**
	 * 功能介绍 : 记账作废状态验证<br>
	 */
	protected void checkChargeStatus(OperateType opType,FcOutAccbook book){
		Integer status = book.getStatus();
		if(opType == OperateType.DELETE){
			if(status.intValue() != OuterStatus.STATUE_RUSH_BACK.get().intValue() && status.intValue() != OuterStatus.STATUE_RUSH_TEMP.get().intValue()){
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
			}
		}else if(opType == OperateType.BACK || opType == OperateType.CHECK){
			if(status.intValue() != OuterStatus.STATUE_RUSH_WAIT_CHECK.get().intValue()){
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
			}
		}
	}
	/**
	 * 功能介绍 : 暂存获取操作类型，其他操作返回原有操作<br>
	 */
	protected OperateType getOpType(Integer vouStatus,OperateType opType){
		OperateType type = null;
		if (opType != null) {
			return opType;
		}
		if(vouStatus.intValue() == OuterStatus.STATUE_TALLY_CHECK.get()){
			type = OperateType.ADD;
		}else if (vouStatus.intValue() == OuterStatus.STATUE_RUSH_TEMP.get() || vouStatus.intValue() == OuterStatus.STATUE_RUSH_BACK.get()){
			type = OperateType.MODIFY;
		}
		return type;
	}
    
}
