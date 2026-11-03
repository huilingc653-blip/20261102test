package com.bytter.biz.finacial.outaccbook.service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bytter.core.common.context.UserContextHelper;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.service.base.OverdraftValidator;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant.OuterStatus;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.outaccbook.OutAccbookMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.outaccbook.dao.TallyDao;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbook;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtl;
import com.bytter.biz.finacial.outaccbook.entity.FcOutAccbookDtlHis;
import com.bytter.biz.finacial.outaccbook.entity.FcPreOutAccbook;
import com.bytter.biz.finacial.outaccbook.vo.AccBookHisVO;
import com.bytter.biz.finacial.outaccbook.vo.AccBookVO;
import com.bytter.biz.finacial.outaccbook.vo.TallyOutAccbookVO;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
/**
* @Title: Service
* @Description: 表外流水账主档表
* @author onlineGenerator
* @date 2017-06-30 11:04:08
* @version 1.0
*
*/
@Service("tallyService")
@Transactional
public class TallyService extends BaseService<FcOutAccbook,String> {
	@Autowired
	private TallyDao tallyDao;
	@Autowired
	private FcPreOutAccbookService fcPreOutAccbookService;
	@Autowired
	private FcOutAccbookDtlHisService fcOutAccbookDtlHisService;
	@DubboReference(check = false)
	private ICorpService corpServiceImpl;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	@DubboReference(check = false)
	private IAccountGrantService accGrantService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private ICorpService  corpService;
	/**
	 * 查询表外记账数据-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryOutAccbookData(DataTablesInput dataTablesInput,String resource, ICurrentUser user){
		List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
		return tallyDao.queryOutAccbookData(dataTablesInput,user,corpAuth);
	}
	/**
	 * 查询记账复核-分页
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput queryTableTallyCheckData(
			DataTablesInput dataTablesInput, ICurrentUser user) {
		return tallyDao.queryTableTallyCheckData(dataTablesInput,user);
	}
	/**
	 * 功能介绍 : 暂存表外记账<br>
	 */
	public void tempAccbook(AccBookVO vo, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setUserCode(user.getId());//设置用户
		if(EmptyUtils.isEmpty(vo.getBook().getId())){
			voucher.setOpType(OperateType.ADD);//记账提交
			this.saveAccBook(vo, OuterStatus.STATUE_TALLY_TEMP.get(),voucher,user);
		}else{
			voucher.setOpType(OperateType.MODIFY);//记账提交
			this.updateAccBook(vo, OuterStatus.STATUE_TALLY_TEMP.get(),voucher);
		}
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_CHECK, vo.getBook().getId(), user.getId());
		//账户余额处理
		updateAccBalance(voucher);
	}
	/**
	 * 功能介绍 : 提交表外记账<br>
	 */
	public void submitAccbook(AccBookVO vo, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setOpType(OperateType.SUBMIT);//记账提交
		voucher.setUserCode(user.getId());//设置用户
		if(EmptyUtils.isEmpty(vo.getBook().getId())){
			this.saveAccBook(vo, OuterStatus.STATUE_TALLY_WAIT_CHECK.get(),voucher,user);
		}else{
			this.updateAccBook(vo, OuterStatus.STATUE_TALLY_WAIT_CHECK.get(),voucher);
		}
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_EDIT, vo.getBook().getId(), user.getId());
		notificationService.sendIgnoreMySelf(vo.getBook().getId(), vo.getBook().getNetId(),
				NotificationConstant.FINACIAL.OUTBOOK_CHECK, this.get(vo.getBook().getId(),true), user.getId());
		//账户余额处理
		updateAccBalance(voucher);
		//如果自动复核，则复核凭证通过TODO
        /*if (false) {
			//复核
			this.tallyCheck(vo.getBook().getId());
		}*/
	}
	/**
	 * 功能介绍 : 批量复核<br>
	 */
	public void batchTallyCheck(List<String> ids, ICurrentUser user) {
		for(String id : ids){
			tallyCheck(id, user);
		}
	}
	/**
	 * 功能介绍 : 批量打回<br>
	 */
	public void batchTallyBack(List<String> ids, ICurrentUser user){
		for(String id : ids){
			tallyBack(id,"",user);
		}
	}
	/**
	 * 功能介绍 : 打回<br>
	 */
	public void tallyBack(String id,String backReason, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setOpType(OperateType.BACK);//复核
		voucher.setUserCode(user.getId());//设置用户
		AccBookVO vo = new AccBookVO();
		vo.setBook(this.get(id));
		vo.setDtls(tallyDao.queryDtlByOutId(id));
		setVoucherWrapper(voucher,vo);
		//状态验证
		checkStatus(vo.getBook());
		//保存表外数据
		backSaveOutAccbook(vo,backReason);
		//账户余额处理
		updateAccBalance(voucher);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_CHECK, vo.getBook().getId(), user.getId());
		notificationService.sendToUser(vo.getBook().getId(), vo.getBook().getOperatorCode(), NotificationConstant.FINACIAL.OUTBOOK_EDIT, vo.getBook());
	}
	/**
	 * 功能介绍 : 保存 打回的表外数据<br>
	 */
	public void backSaveOutAccbook(AccBookVO vo,String backReason){
		vo.getBook().setStatus(OuterStatus.STATUE_TALLY_BACK.get());
		vo.getBook().setBackReason(backReason);
		this.saveOrUpdate(vo.getBook());
	}
	/**
	 * 功能介绍 : 复核<br>
	 */
	public void tallyCheck(String id, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setOpType(OperateType.CHECK);//复核
		voucher.setUserCode(user.getId());//设置用户
		AccBookVO vo = new AccBookVO();
		vo.setBook(this.get(id));
		vo.setDtls(tallyDao.queryDtlByOutId(id));
		setVoucherWrapper(voucher,vo);
		//状态验证
		checkStatus(vo.getBook());
		//保存表外数据
		checkSaveOutAccbook(vo, user);
		//账户余额处理
		updateAccBalance(voucher);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_CHECK, vo.getBook().getId(), user.getId());
	}
	/**
	 * 功能介绍 : 保存 复核的表外数据<br>
	 */
	public void checkSaveOutAccbook(AccBookVO vo, ICurrentUser user){
		String netCode = "";
		if(!EmptyUtils.isEmptyList(vo.getDtls())){
			FcAccDTO acc = iFcAccService.getAccDTOByAccount(vo.getDtls().get(0).getAccount());
			CorpDTO corp = corpServiceImpl.getParentById(acc.getCorpId());
			if(EmptyUtils.isEmpty(corp)){
				corp = corpServiceImpl.get(acc.getCorpId());
			}
			netCode = corp.getCode();
		}else{
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_NONENTITY,"");
		}
		vo.getBook().setVoucherNo(getAccbookVouvherNo(netCode));
		vo.getBook().setStatus(OuterStatus.STATUE_TALLY_CHECK.get());
		vo.getBook().setCheckTime(new Date());
		vo.getBook().setCheckerCode(user.getId());
		this.saveOrUpdate(vo.getBook());
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_CHECK, vo.getBook().getId(), user.getId());
	}
	/**
	 *
	 * 功能介绍 : 抹账<br>
	 */
	public void delOutAccbook(String id, ICurrentUser user){
		VoucherWrapper voucher = new VoucherWrapper();
		voucher.setProcessType(ProcessType.PROCESS_COMMON);
		voucher.setOpType(OperateType.DELETE);//记账提交
		voucher.setUserCode(user.getId());//设置用户
		//删除数据
		delAccBookData(id, voucher, user);
		//账户余额处理
		updateAccBalance(voucher);
	}
	/**
	 *
	 * 功能介绍 :新增保存表外记账信息 <br>
	 */
	public void saveAccBook(AccBookVO vo,Integer status,VoucherWrapper voucher, ICurrentUser user) {
		//验证
		String netId = outAccbookCheck(vo);
		//设置默认值
		CorpDTO corp = corpServiceImpl.get(netId);
		vo.getBook().setOrderNo(SequenceSingleton.getSequence(SequenceEnum.OUT_ORDER_NO, corp.getCode()));
		vo.getBook().setNetId(netId);
		vo.getBook().setRedblueSign(FinacialConstant.REDBLUE_SIGN_BLUE);
		vo.getBook().setBusOriType(OuterFinacialConstant.OuterBusOriType.TALLY_HAND.get());//业务来源 手工记账
		vo.getBook().setStatus(status);//状态
		vo.getBook().setEnrolTime(new Date());
		vo.getBook().setOperatorCode(user.getId());
		vo.getBook().setOverSign(OuterFinacialConstant.OVER_SIGN_N);
		//保存
		tallyDao.save(vo.getBook());
		//设置行号与外键
		int i = 1;
		for (FcOutAccbookDtl dtl : vo.getDtls()) {
			dtl.setLineNo(i);
			if(!bigDecimalIsZero(dtl.getDebitMoney())){
				dtl.setCreditMoney(BigDecimal.ZERO);
			}
			if(!bigDecimalIsZero(dtl.getCreditMoney())){
				dtl.setDebitMoney(BigDecimal.ZERO);
			}
			dtl.setOutAccbookId(vo.getBook().getId());
			i++;
		}
		//保存
		tallyDao.batchSave(vo.getDtls());
		//保存凭证到中间对象
		setVoucherWrapper(voucher,vo);
	}
	/**
	 * 功能介绍 :修改保存表外记账信息 <br>
	 */
	public void updateAccBook(AccBookVO vo,Integer status,VoucherWrapper voucher){
		//历史数据对象
		AccBookHisVO hisVO = new AccBookHisVO();
		FcPreOutAccbook bookHis = new FcPreOutAccbook();
		List<FcOutAccbookDtlHis> dtlsHis = BeanUtils.copyListProperties(tallyDao.queryDtlByOutId(vo.getBook().getId()), FcOutAccbookDtlHis.class);//查询原始凭证明细
		//验证
		String netId = outAccbookCheck(vo);
		vo.getBook().setNetId(netId);
		FcOutAccbook t = this.get(vo.getBook().getId(),true);//原始凭证
		tallyStatus(t);
		BeanUtils.copyNotNullProperties(t, bookHis);//复制属性到历史对象
		BeanUtils.copyNotNullProperties(vo.getBook(), t);//将改变值设置到原始凭证中
		t.setStatus(status);//状态
		vo.getBook().setStatus(status);
		tallyDao.saveOrUpdate(t);
		//保存DTL
		int i = 1;
		List<FcOutAccbookDtl> dtls = new ArrayList<FcOutAccbookDtl>();
		for(FcOutAccbookDtl dtl : vo.getDtls()){
			dtl.setLineNo(i);
			if(!bigDecimalIsZero(dtl.getDebitMoney())){
				dtl.setCreditMoney(BigDecimal.ZERO);
			}
			if(!bigDecimalIsZero(dtl.getCreditMoney())){
				dtl.setDebitMoney(BigDecimal.ZERO);
			}
			FcOutAccbookDtl td = tallyDao.queryDtlById(dtl.getId());
			BeanUtils.copyNotNullProperties(dtl, td);
			dtls.add(td);
			i++;
		}
		tallyDao.batchSaveOrUpdate(dtls);
		//保存历史数据
		bookHis.setBookId(t.getId());
		hisVO.setBookHis(bookHis);
		hisVO.setDtlsHis(dtlsHis);
		saveAccBookHis(hisVO);
		//保存凭证到中间对象
		setVoucherWrapper(voucher,vo);
		//保存原始凭证到中间对象
		setVoucherWrapper(voucher,hisVO);
	}
	public void delAccBookData(String id,VoucherWrapper voucher, ICurrentUser user){
		FcOutAccbook book = this.get(id);
		//判断当前表外记账状态
		tallyStatus(book);
		//构建凭证中间对象
		AccBookVO vo = new AccBookVO();
		vo.setBook(book);
		vo.setDtls(tallyDao.queryDtlByOutId(id));
		setVoucherWrapper(voucher,vo);
		//删除明细
		tallyDao.delDtlByOutId(id);
		notificationService.complete(NotificationConstant.FINACIAL.OUTBOOK_EDIT, vo.getBook().getId(), user.getId());
		//删除主档信息表
		deleteEntity(book);
		//保存历史记录
		AccBookHisVO hisVO = new AccBookHisVO();
		FcPreOutAccbook bookHis = new FcPreOutAccbook();
		BeanUtils.copyNotNullProperties(book, bookHis);//复制属性到历史对象
		bookHis.setBookId(book.getId());
		hisVO.setBookHis(bookHis);
		hisVO.setDtlsHis(BeanUtils.copyListProperties(vo.getDtls(), FcOutAccbookDtlHis.class));
		saveAccBookHis(hisVO);
	}
	/**
	 *
	 * 功能介绍 : 账户余额处理<br>
	 * <b>日期：</b>2017-7-9<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	protected void updateAccBalance(VoucherWrapper voucher){
		//数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
		OverdraftValidator overValidator = new OverdraftValidator();
		List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = overValidator.listFcOnlineLedger(voucher);
		Map<String, FcOnlineLedgerDTO> ledgerMap = overValidator.processVoucherBalance(voucher);
		//更新账户联机余额
		if (OperateType.CHECK.equals(voucher.getOpType())) {
			fcOnlineLedgerService.updateCheckBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()));
		} else {
			fcOnlineLedgerService.updateOnlineBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()), origOnlineLedgerDTOList);
		}
	}
	/**
	 * 功能介绍 : 将vo的值设置到VouchWrapper的原始凭证中<br>
	 * @param voucher
	 * @param vo
	 * <b>日期：</b>2017-7-10<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public void setVoucherWrapper(VoucherWrapper voucher,AccBookHisVO vo){
		FcAccbookVoucherVO fcAcc= new FcAccbookVoucherVO();
		fcAcc.setOpType(voucher.getOpType());
		FcAccbook book = new FcAccbook();
		BeanUtils.copyNotNullProperties(vo.getBookHis(), book);
		book.setVoucherState(OuterFinacialConstant.outInversionInnerStatus(vo.getBookHis().getStatus()));
		fcAcc.setAccbook(book);
		List<FcAccbookDtl> list = new ArrayList<FcAccbookDtl>();
		if(!EmptyUtils.isEmptyList(vo.getDtlsHis())){
			for (FcAccbookDtl dtl : BeanUtils.copyListProperties(vo.getDtlsHis(), FcAccbookDtl.class)) {
				if(!bigDecimalIsZero(dtl.getDebitMoney())){
					dtl.setCreditMoney(BigDecimal.ZERO);
				}
				if(!bigDecimalIsZero(dtl.getCreditMoney())){
					dtl.setDebitMoney(BigDecimal.ZERO);
				}
				list.add(dtl);
			}
		}
		fcAcc.setBookDtls(list);
		voucher.getOriTarget().add(fcAcc);
	}
	/**
	 * 功能介绍 : 将vo的值设置到VouchWrapper的原始凭证中<br>
	 * @param voucher
	 * @param vo
	 * <b>日期：</b>2017-7-10<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public void setVoucherWrapperOri(VoucherWrapper voucher,AccBookVO vo){
		FcAccbookVoucherVO fcAcc= new FcAccbookVoucherVO();
		fcAcc.setOpType(voucher.getOpType());
		FcAccbook book = new FcAccbook();
		BeanUtils.copyNotNullProperties(vo.getBook(), book);
		book.setVoucherState(OuterFinacialConstant.outInversionInnerStatus(vo.getBook().getStatus()));
		fcAcc.setAccbook(book);
		List<FcAccbookDtl> list = new ArrayList<FcAccbookDtl>();
		if(!EmptyUtils.isEmptyList(vo.getDtls())){
			for (FcAccbookDtl dtl : BeanUtils.copyListProperties(vo.getDtls(), FcAccbookDtl.class)) {
				if(!bigDecimalIsZero(dtl.getDebitMoney())){
					dtl.setCreditMoney(BigDecimal.ZERO);
				}
				if(!bigDecimalIsZero(dtl.getCreditMoney())){
					dtl.setDebitMoney(BigDecimal.ZERO);
				}
				list.add(dtl);
			}
		}
		fcAcc.setBookDtls(list);
		voucher.getOriTarget().add(fcAcc);
	}
	/**
	 * 功能介绍 : 将vo的值设置到VouchWrapper的凭证中<br>
	 * @param voucher
	 * @param vo
	 * <b>日期：</b>2017-7-10<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public void setVoucherWrapper(VoucherWrapper voucher,AccBookVO vo){
		FcAccbookVoucherVO fcAcc= new FcAccbookVoucherVO();
		fcAcc.setOpType(voucher.getOpType());
		FcAccbook book = new FcAccbook();
		BeanUtils.copyNotNullProperties(vo.getBook(), book);
		book.setVoucherState(OuterFinacialConstant.outInversionInnerStatus(vo.getBook().getStatus()));
		fcAcc.setAccbook(book);
		List<FcAccbookDtl> list = new ArrayList<FcAccbookDtl>();
		if(!EmptyUtils.isEmptyList(vo.getDtls())){
			for (FcAccbookDtl dtl : BeanUtils.copyListProperties(vo.getDtls(), FcAccbookDtl.class)) {
				if(!bigDecimalIsZero(dtl.getDebitMoney())){
					dtl.setCreditMoney(BigDecimal.ZERO);
				}
				if(!bigDecimalIsZero(dtl.getCreditMoney())){
					dtl.setDebitMoney(BigDecimal.ZERO);
				}
				list.add(dtl);
			}
		}
		fcAcc.setBookDtls(list);
		voucher.getTarget().add(fcAcc);
	}
	/**
	 *
	 * 功能介绍 : 备份表外信息到历史记录表中<br>
	 * @param hisVO
	 * <b>日期：</b>2017-7-7<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public void saveAccBookHis(AccBookHisVO hisVO){
		hisVO.getBookHis().setId(null);
		fcPreOutAccbookService.save(hisVO.getBookHis());
		List<FcOutAccbookDtlHis> dtlsHis = new ArrayList<FcOutAccbookDtlHis>();
		for(FcOutAccbookDtlHis his : hisVO.getDtlsHis()){
			his.setId(null);
			his.setOutAccbookId(hisVO.getBookHis().getId());
			dtlsHis.add(his);
		}
		fcOutAccbookDtlHisService.batchSave(dtlsHis);
	}
	/**
	 *
	 * 功能介绍 : 根据表外ID查询表外明细<br>
	 * @param id
	 * @return
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@Transactional(readOnly = true)
	public List<TallyOutAccbookVO> queryOutDtlById(String id) {
		List<TallyOutAccbookVO> tallyOutAccbookVOS = tallyDao.queryOutDtlById(id);
		List<String> accounts = tallyOutAccbookVOS.stream().map(TallyOutAccbookVO::getAcc).collect(Collectors.toList());
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccSubjectCurDateByAccounts(accounts);
		Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
		tallyOutAccbookVOS.forEach(a -> {
			FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(a.getAcc());
			a.setAccName(fcAccSubjectDTO.getAccName());
			a.setCurId(fcAccSubjectDTO.getCurId());
			a.setUnit(fcAccSubjectDTO.getMeasureUnit());
			a.setSubjectName(fcAccSubjectDTO.getSubjectName());
		});
		return tallyOutAccbookVOS;
	}
	/**
	 * 功能介绍 : 根据ID获取AccBookVO对象<br>
	 */
	@Transactional(readOnly = true)
	public AccBookVO getAccBookVoByid(String id){
		AccBookVO vo = new AccBookVO();
		vo.setBook(tallyDao.get(id,true));
		vo.setDtls(tallyDao.queryDtlByOutId(id));
		return vo;
	}
	/**
	 * 功能介绍 ： 根据账号获取凭证号。<br>
	 * <b>日期：</b>2017-7-6<br>
	 * @version 1.0
	 * @author lvwl
	 */
	protected String getAccbookVouvherNo(String netCode){
		return SequenceSingleton.getSequence(SequenceEnum.OUT_VOUCHER_NO, netCode);
	}
	/**
	 * 功能介绍 ： 根据账号获取凭证号。<br>
	 * <b>日期：</b>2017-7-6<br>
	 * @version 1.0
	 * @author lvwl
	 */
	protected String getAccbookOrderNo(String netCode){
		return SequenceSingleton.getSequence(SequenceEnum.OUT_ORDER_NO, netCode);
	}
	/**
	 * 删除对象
	 * @param entity
	 */
	public void deleteEntity(FcOutAccbook entity){
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
	public void save(FcOutAccbook entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcOutAccbook entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	public void saveOrUpdate(FcOutAccbook entity){
		super.saveOrUpdate(entity);
	}
	/**验证**/
	/**
	 * 表外记账验证
	 */
	public String outAccbookCheck(AccBookVO vo){
		List<FcOutAccbookDtl> dtlList = null;
		String netId = "";
		if(!EmptyUtils.isEmpty(vo.getBook())){
			if(EmptyUtils.isEmpty(vo.getBook().getTallyDate())){
				throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_TALLYDATE_EMPTY);
			}
		}else{
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_TALLYMESSAGE_EMPTY);
		}
		if(!EmptyUtils.isEmptyList(vo.getDtls()) && vo.getDtls().size()>0){
			dtlList = new ArrayList<FcOutAccbookDtl>();
			int direction = 0;
			for (FcOutAccbookDtl dtl : vo.getDtls()) {
				//当linNo为null时说明前天数组出现跳下标（删除导致）
				if(EmptyUtils.isEmpty(dtl.getLineNo())){
					continue;
				}
				if(EmptyUtils.isEmpty(dtl.getSummaryContent())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_SUMMARYCONTENT_EMPTY);
				}
				if(EmptyUtils.isEmpty(dtl.getAccount())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_EMPTY);
				}
				if(bigDecimalIsZero(dtl.getDebitMoney()) && bigDecimalIsZero(dtl.getCreditMoney())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DEBIT_CERDIT_ZERO);
				}
				if(!bigDecimalIsZero(dtl.getDebitMoney()) && !bigDecimalIsZero(dtl.getCreditMoney())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DEBIT_CERDIT_NOT_ZERO);
				}
				//借方金额不为0
				if(!bigDecimalIsZero(dtl.getDebitMoney())){
					if(direction == -1){
						throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DIRECTION_DIFFER);
					}
					if(direction == 0){
						direction = 1;
					}
				}
				//贷方金额不为0
				if(!bigDecimalIsZero(dtl.getCreditMoney())){
					if(direction == 1){
						throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DIRECTION_DIFFER);
					}
					if(direction == 0){
						direction = -1;
					}
				}
				FcAccDTO fcAccDTO= iFcAccService.getAccDTOByAccount(dtl.getAccount());
				if(EmptyUtils.isEmpty(fcAccDTO)){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_NONENTITY,dtl.getAccount());
				}
				if(InnerAccountConstant.ACC_STATE_DESTORY.equals(fcAccDTO.getAccState())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_CANCEL,dtl.getAccount());
				}
				if(EmptyUtils.isEmpty(fcAccDTO.getNetId())){
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_ACCOUNT_NET_NONENTITY,dtl.getAccount());
				}
				//用户是否有账号的操作权限？？？？TODO
				//记账日期必须大于账户启用日期
				if (vo.getBook().getTallyDate().before(fcAccDTO.getAccStartDate())) {
					throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_TALLYDATE_LE_STARTDATE);
				}
				if(EmptyUtils.isEmpty(netId)){
					netId = fcAccDTO.getNetId();
				}else{
					if(!netId.equals(fcAccDTO.getNetId())){
						throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_NOT_NOE_NET);
					}
				}
				dtlList.add(dtl);
			}
		}
		Date dayOverDate = dayCloseService.getDayCloseFinalDate(netId);//根据网点获取已日结日期
		//判断记账日期是否比已日结日期早
		if (vo.getBook().getTallyDate().compareTo(dayOverDate) <= 0) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_RUSHD_AFTER_CLOSED,
					DateUtils.formatDate(vo.getBook().getTallyDate()));
        }
		if(dtlList.size() == 0){
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DTL);
		}
		vo.setDtls(dtlList);
		return netId;
	}
	/**
	 * 功能介绍 : 判断表外记账状态是否能修改<br>
	 * @param book
	 * <b>日期：</b>2017-7-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	protected  void tallyStatus(FcOutAccbook book){
		if(EmptyUtils.isEmpty(book.getId())){
			return;
		}
		if(!(book.getStatus().intValue() == OuterStatus.STATUE_TALLY_TEMP.get().intValue()
				|| book.getStatus() == OuterStatus.STATUE_TALLY_BACK.get().intValue()
				|| book.getStatus() == OuterStatus.STATUE_TALLY_WAIT_CHECK.get().intValue() )){
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE);
		}
	}
	/**
	 * 功能介绍 : 判断复核状态<br>
	 */
	protected  void checkStatus(FcOutAccbook book){
		if(EmptyUtils.isEmpty(book.getId())){
			return;
		}
		if(book.getStatus() != OuterStatus.STATUE_TALLY_WAIT_CHECK.get().intValue()){
			throw new ParameterException(OutAccbookMessageEnum.FIN_OUTBOOK_TALLY_DATE_UPDATE_BY_ORDERNO,book.getOrderNo());
		}
	}
	public boolean bigDecimalIsZero(BigDecimal data){
		if(data == null || data.compareTo(BigDecimal.ZERO) == 0){
			return true;
		}
		return false;
	}
	public BaseDao<FcOutAccbook, String> getEntityDao() {
		return tallyDao;
	}
	/**
	 * 功能介绍 : 获取有权限的表外账号。<br>
	 * <b>日期：</b>2017年11月4日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<FcAccDTO> getOuterTallyAccount(ICurrentUser user) {
		List<String> accs = accGrantService.listByMultiVal(user.getId(), FinacialResourceConstant.FINACIAL_OUTACCBOOK_TALL_QUERY, null,
				AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
		/*Map<String, String> accMap = Maps.uniqueIndex(accs, new Function<String, String>() {
			@Override
			public String apply(String input) {
				return input;
			}
		});*/
		List<FcAccDTO> accDTOs = iFcAccService.getOutAccTypeAccs();
		List<FcAccDTO> list = new ArrayList<FcAccDTO>();
		for (FcAccDTO fcAcc : accDTOs) {
//			if (accMap.containsKey(fcAcc.getId())) {
			if (accs.contains(fcAcc.getId())) {
				list.add(fcAcc);
			}
		}
		return list;
	}
}
