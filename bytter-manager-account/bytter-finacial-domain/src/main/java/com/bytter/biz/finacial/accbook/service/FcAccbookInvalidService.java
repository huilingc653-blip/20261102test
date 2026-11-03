package com.bytter.biz.finacial.accbook.service;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.finacial.accbook.dao.FcAccbookDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.service.base.ChargeProcessor;
import com.bytter.biz.finacial.accbook.service.base.UnionAccbookProcessor;
import com.bytter.biz.finacial.accbook.util.FcAccbookUtils;
import com.bytter.biz.finacial.accbook.vo.FcAccbookListVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookNotifyVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.GenerateMode;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.service.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * 作废Service
 * @author liyh
 *
 */
@Service("fcAccbookInvalidService")
@Transactional
public class FcAccbookInvalidService extends FcAccbookService {
	@Autowired
	private FcAccbookDao fcAccbookDao;
	@Autowired
	private FcAccbookDtlService bookDtlservice;
	@Autowired
	private FcAccbookExtService bookExtservice;
	@Autowired
	private FcAccbookMsgService bookMsgservice;
	
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	
	@DubboReference(check = false)
	private IFcBusRaceService busRaceService;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;

	@Autowired
	private UnionAccbookProcessor unionAccbookProcessor;
	/**
	 * 功能介绍 ：获取作废凭证列表数据。<br>
	 * <b>日期：</b>2017年5月31日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput getCanInvalidFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.EQ, "operatorCode", userId);//是记账人
		input.addFilter(MatchTypeEnum.EQ, "rushSign", FinacialConstant.RUSH_SIGN_N);//未冲
		//input.addFilter(MatchTypeEnum.NE, "busOriType", FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());//非利息凭证
		//input.addFilter(MatchTypeEnum.NE, "generateMode", FinaBookConstant.GenerateMode.DEPOSIT.get());//非存款管理凭证
		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.EQ, "voucherState",
						FinaBookConstant.VoucherStatus.STATUS_CHECK.get() );//正常凭证
		//input.addFilter(MatchTypeEnum.NE, "raceCode", FinaBookConstant.SysRaceCode.NB_CHARGE_ACCRUE.get());//手续费计提
		//非存款管理凭证、手续费计提、银企收款记账、利息
//		input.addFilter(MatchTypeEnum.IN, "generateMode", FinaBookConstant.GenerateMode.TAILLY.get()
//                + "," + FinaBookConstant.GenerateMode.UNION_ACCBOOK.get() + "," + FinaBookConstant.GenerateMode.EXCHANGE.get()
//				+ "," + FinaBookConstant.GenerateMode.PROFIT_BOOK.get() + "," + FinaBookConstant.GenerateMode.TAX.get()
//				+ "," + FinaBookConstant.GenerateMode.INNER_PAY_BOOK.get() + "," + FinaBookConstant.GenerateMode.BANK_COLLECT_BOOK.get()
//                + "," + FinaBookConstant.GenerateMode.ENTRUST_COLLECT_BOOK.get());
		
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> data = dataTablesOutput.getData();
		List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			data.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		return dataTablesOutput;
	}
	
	/**
	 * 功能介绍 ：获取作废修改凭证列表数据。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput getEditInvalidFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.EQ, "operatorCode", userId);// 是记账人
		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.IN, "voucherState",
				FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get() + ","
						+ FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get() + ","
						+ FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get());
		input.addFilter(MatchTypeEnum.NE, "raceCode", FinaBookConstant.SysRaceCode.NB_CHARGE_ACCRUE.get());//手续费计提
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> data = dataTablesOutput.getData();
		List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			data.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		return dataTablesOutput;
	}
	
	/**
	 * 功能介绍 ：获取作废复核凭证列表数据。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput getCheckInvalidFcAccbookVoList(DataTablesInput input, String resource, String userId) {
		input.addFilter(MatchTypeEnum.NE, "operatorCode", userId);//FIXME 不能是记账人
		// 状态记账打回、暂存、待复核
		input.addFilter(MatchTypeEnum.EQ, "voucherState", FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get());
		input.addFilter(MatchTypeEnum.NE, "raceCode", FinaBookConstant.SysRaceCode.NB_CHARGE_ACCRUE.get());//手续费计提
		
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "main_info.net_id", true);
		DataTablesOutput dataTablesOutput = fcAccbookDao.queryCanEditAccbooks(input, false, corpAuthSql);
		List<FcAccbookListVO> data = dataTablesOutput.getData();
		List<String> ids = data.stream().map(FcAccbookListVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> dtos = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		Map<String, String> map = dtos.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
		if (CollectionUtils.isNotEmpty(dtos)){
			data.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
				a.setVoucherId(a.getVoucherType());
			});
		}
		return dataTablesOutput;
	}
	
	/**
	 * 功能介绍 : 获取操作类型,该方法只能算出经办流程的操作，
	 * 		        对于退回与复核及删除需要传进去(作废冲账时不处理新录入凭证的作废)。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private OperateType getInvalidVoucherOpType(String voucherStatus, OperateType opType,
			String status) {
		OperateType operateType = null;
		if (opType != null) {
			return opType;
		}
		// 经办流程操作类型
		if (FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get().equals(status)) {
		    //作废暂存或者打回的数据再暂存还是按照修改处理 
			if (voucherStatus.equals(FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get()) ||
					voucherStatus.equals(FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get()) ) {
				operateType = OperateType.MODIFY;
			} else {
				operateType = OperateType.ADD;
			}
		} else if (voucherStatus.equals(FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get())) {
			operateType = OperateType.SUBMIT;
		}
		return operateType;
	}
	/**
	 * 作废前校验凭证状态
	 * @param pageType
	 * @param busGlobalId
	 */
	@Override
	public void validVoucher(String pageType, String busGlobalId) {
		FcAccbook accbook = getAccbookByBusGlobalId(busGlobalId);
		String voucherStatus = accbook.getVoucherState();
		//检查凭证状态，非暂存、打回、待复核状态不允许修改保存或提交
		if (FinacialConstant.PAGE_INIT.equals(pageType)) {//作废申请
			FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_CHECK.get()});
		} else if (FinacialConstant.PAGE_EDIT.equals(pageType)) {//作废修改
			FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get(),
					FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get(), FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get()});
		} else if (FinacialConstant.PAGE_CHECK.equals(pageType)) {//作废复核
			FcAccbookUtils.validVoucherStatus(voucherStatus, new String[]{FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get()});
		}
	}
	/**
	 * 功能介绍 : 初始化化凭证作废的信息。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void preFixInvalidVoucher(VoucherWrapper voucher, OperateType opType, String status) {
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String invalidReason = "";//作废原因
		if(null != voucherVO.getBookExt()) {
			invalidReason = voucherVO.getBookExt().getInvalidReason();//作废原因
		}
		
		List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(voucher.getBusGlobalId());
		voucher.setTarget(voucherVOs);
		FcAccbook accbook = voucher.getBookVoucher().getAccbook();
		OperateType finalOpType = getInvalidVoucherOpType(accbook.getVoucherState(), opType, status);
		voucher.setOpType(finalOpType);//设置凭证操作类型
		for (FcAccbookVoucherVO accbookVoucher : voucherVOs) {
			FcAccbook fcAccbook = accbookVoucher.getAccbook();
			if (FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get().equals(status)
					|| FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get().equals(status)
					||FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get().equals(status)) {
				validInvalidBefore(fcAccbook.getId());//复核过程都要校验,作废打回和作废删除不校验
			}
			
			//fcAccbook.setVoucherState(status);//设置凭证状态
			accbookVoucher.setOpType(finalOpType);
			FcAccbookExt bookExt = accbookVoucher.getBookExt();
			//设置作废原因，如果不存在凭证扩展信息，则添加凭证扩展记录
			if (null == bookExt) {
				bookExt = new FcAccbookExt();
				bookExt.setBookId(fcAccbook.getId());//设置bookId
				accbookVoucher.setBookExt(bookExt);
			}
			if (!StringUtils.isEmpty(invalidReason)) {
				bookExt.setInvalidReason(invalidReason);//设置作废原因
			}
			if(null == voucherVO.getBookExt()) {
				voucherVO.setBookExt(new FcAccbookExt());
				voucherVO.getBookExt().setBookId(voucherVO.getAccbook().getId());
			}
			String backReason = voucherVO.getBookExt().getBackReason();//打回原因
			if (!StringUtils.isEmpty(backReason)) {
				bookExt.setBackReason(backReason);//设置打回原因
			}
		}
	}
	
	/**
	 * 功能介绍 : 设置冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 * @param status 
	 * @param opType 
	 */
	public void processInvalidVoucher(VoucherWrapper voucher, OperateType opType, String status) {
		//1、作废前加载凭证数据
		preFixInvalidVoucher(voucher, opType, status);
		//		获取初始联机余额信息
		List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
		//2、提交处理数据和余额
		//FIXME 是否需要同步处理账户余额，保持和其他地方一致
		Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
		
		//3、数据保存
		commonProcessInvalidVoucher(voucher, ledgerMap, opType, status, origOnlineLedgerDTOList);
		
		//TODO 4、是否需要还原相关单据数据，待后续添加
		if (OperateType.CHECK.equals(opType)) {
			/**
			 * 手续费中有个同行和异地的判断来源于指令处理，如果不产生指令，则手续费中的同行异地不处理
			 * 如果存在手续费，先处理手续费
			 */
			new ChargeProcessor().processResult(voucher, ledgerMap);
		}
//		单位申请付款-一般付款
		if(FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.getValue().equals(voucher.getBookVoucher().getAccbook().getBusOriType())){
			if (OperateType.SUBMIT == opType) {//作废提交
				nisBillheadService.updateNisBillheadForInvalidFin(voucher.getBookVoucher().getAccbook().getOrigVoucherNo(), NetbankConstant.STATUE_TALLY_INVALID_WAIT_CHECK, "作废待复核", FinacialManualConstant.TALLY_SIGN_TALLY_CHECK, voucher.getUser());
			} else if (OperateType.CHECK == opType) {//作废复核
				nisBillheadService.updateNisBillheadForInvalidFin(voucher.getBookVoucher().getAccbook().getOrigVoucherNo(), NetbankConstant.STATUE_TALLY_INVALID_CHECKPASS, "作废复核通过", FinacialManualConstant.TALLY_SIGN_TALLY_OVER, voucher.getUser());
			} else if (OperateType.BACK == opType) {//作废打回
				nisBillheadService.updateNisBillheadForInvalidFin(voucher.getBookVoucher().getAccbook().getOrigVoucherNo(), NetbankConstant.STATUE_TALLY_INVALID_BACK, "作废打回", FinacialManualConstant.TALLY_SIGN_TALLY_BACK, voucher.getUser());
			} else {//作废暂存
				nisBillheadService.updateNisBillheadForInvalidFin(voucher.getBookVoucher().getAccbook().getOrigVoucherNo(), NetbankConstant.STATUE_TALLY_INVALID_TEMP, "作废暂存", FinacialManualConstant.TALLY_SIGN_TALLY_TEMP, voucher.getUser());
			}
		}
	}
	/**
	 * 功能介绍 : 凭证作废处理。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 * @param status 
	 */
	protected synchronized void commonProcessInvalidVoucher(VoucherWrapper voucher,
			Map<String, FcOnlineLedgerDTO> ledgerMap, OperateType operateType, String status, List<FcOnlineLedgerDTO> origOnlineLedgerDTOList) {
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			FcAccbookExt bookExt = voucherVO.getBookExt();
			if (null == bookExt) {
				bookExt = new FcAccbookExt();
				bookExt.setBookId(accbook.getId());//设置bookId
			}
			OperateType finalOpType = voucherVO.getOpType();
			// 更新作废原因与作废操作人与时间
			if (finalOpType == OperateType.SUBMIT || OperateType.ADD == finalOpType
					|| finalOpType == OperateType.MODIFY || finalOpType == OperateType.DELETE) {
				if (operateType == OperateType.DELETE) {
					bookExt.setInvalidOpCode(null);//清空作废人和作废时间
					bookExt.setInvalidOpTime(null);
					bookExt.setInvalidReason(null);//清空作废原因
				} else {
					bookExt.setInvalidOpCode(voucher.getUserCode());//作废人员
					bookExt.setInvalidOpTime(DateUtils.getDate());//作废时间
				}
			}
			// 作废复核之前将写入FcPreAccBook表
			if (OperateType.CHECK.equals(operateType)) {
				backupFcAccbook(voucherVO, FinaBookConstant.VoucherOpType.OpType_B.get());
			}
			accbook.setVoucherState(status);//设置凭证状态
			merge(accbook);
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			int lineNo = 1;//判断凭证分录是否和原凭证分录数量一致
			for (FcAccbookDtl accbookDtl : bookDtls) {
				accbookDtl.setBookId(accbook.getId());
				accbookDtl.setLineNo(lineNo++);
				//处理明细余额，明细上的余额不显示，复核时再写入
				//FIXME 账户余额更新待处理
				bookDtlservice.merge(accbookDtl);
			}
			if (null != bookExt) {
				bookExtservice.merge(bookExt);
			}
			
			String msg = null;
			if (OperateType.BACK.equals(operateType)) {
				msg = bookExt.getBackReason();//作废打回原因
			}
			//记录作废日志
			bookMsgservice.saveBookMsg(accbook.getId(), getMsgType(), voucherVO.getOpType(), msg, voucher.getUserCode());
		}
		//更新账户联机余额
		if (OperateType.CHECK.equals(operateType)) {
			fcOnlineLedgerService.updateCheckBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()));
		} else {
			fcOnlineLedgerService.updateOnlineBalanceList(new ArrayList<FcOnlineLedgerDTO>(ledgerMap.values()), origOnlineLedgerDTOList);
		}
	}
	
	/**
	 * 功能介绍 : 返回日志操作类型。<br>
	 * <b>日期：</b>2017年10月31日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Override
	public String getMsgType() {
		return FinacialConstant.FINACIAL_MSGTYPE_B;
	}
	/**
	 * 功能介绍 : 作废暂存。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processInvalidVoucherSave(VoucherWrapper voucher) {
		String status = FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get();
		processInvalidVoucher(voucher, null, status);
		completeEditNotificationInfo(voucher);//消息完成
		completeCheckNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 作废提交。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processInvalidVoucherSubmit(VoucherWrapper voucher) {
		String status = FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get();
		processInvalidVoucher(voucher, OperateType.SUBMIT, status);
		completeEditNotificationInfo(voucher);//消息完成
		completeCheckNotificationInfo(voucher);
		//如果自动复核，则复核凭证通过
		if (voucher.isAutoCheck()) {
			processInvalidVoucherCheck(voucher);
		} else {
			sendCheckNotificationInfo(voucher);
		}
	}
	
	/**
	 * 功能介绍 :消息完成。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void completeEditNotificationInfo(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			notificationService.complete(NotificationConstant.FINACIAL.BOOK_INVALID_EDIT, accbook.getId(), voucher.getUserCode());
		}
	}
	
	/**
	 * 功能介绍 :消息完成。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void completeCheckNotificationInfo(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			if (!EmptyUtils.isEmpty(accbook.getId())) {
				notificationService.complete(NotificationConstant.FINACIAL.BOOK_INVALID_CHECK, accbook.getId(), voucher.getUserCode());
			}
		}
	}
	
	/**
	 * 功能介绍 : 发送消息提醒。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void sendCheckNotificationInfo(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			if (FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get().equals(accbook.getVoucherState())) {
				// 发送消息作废复核提醒
				FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
				notifyVO.setOrderNo(accbook.getOrderNo());
				List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
				BigDecimal money = BigDecimal.ZERO;
				if (!EmptyUtils.isEmptyList(bookDtls)) {
					for (FcAccbookDtl accbookDtl : bookDtls) {
						if (null != accbookDtl.getDebitMoney()) {
							money = money.add(accbookDtl.getDebitMoney());
						}
					}
				}
				notifyVO.setMoney(money);
				notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(),
						NotificationConstant.FINACIAL.BOOK_INVALID_CHECK, notifyVO, voucher.getUserCode());
			}
		}
	}
	
	/**
	 * 功能介绍 :消息完成。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void sendBackNotificationInfo(VoucherWrapper voucher) {
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			FcAccbookExt accbookExt = voucherVO.getBookExt();
			// 打回改账处，通知提醒记账人
			FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
			notifyVO.setOrderNo(accbook.getOrderNo());
			List<FcAccbookDtl> bookDtls = voucherVO.getBookDtls();
			BigDecimal money = BigDecimal.ZERO;
			if (!EmptyUtils.isEmptyList(bookDtls)) {
				for (FcAccbookDtl accbookDtl : bookDtls) {
					if (null != accbookDtl.getDebitMoney()) {
						money = money.add(accbookDtl.getDebitMoney());
					}
				}
			}
			notifyVO.setMoney(money);
			notificationService.sendToUser(accbook.getId(), accbookExt.getInvalidOpCode(),
					NotificationConstant.FINACIAL.BOOK_INVALID_EDIT, notifyVO);
		}
	}
	
	/**
	 * 功能介绍 : 作废删除抹账。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processInvalidVoucherDelete(VoucherWrapper voucher) {
		String status = FinaBookConstant.VoucherStatus.STATUS_CHECK.get();
		processInvalidVoucher(voucher, OperateType.DELETE, status);
		completeEditNotificationInfo(voucher);//消息完成
		completeCheckNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 作废打回。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processInvalidVoucherBack(VoucherWrapper voucher) {
		String status = FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get();
		processInvalidVoucher(voucher, OperateType.BACK, status);
		completeCheckNotificationInfo(voucher);//消息完成
		//打回作废人消息提醒
		sendBackNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 作废复核。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processInvalidVoucherCheck(VoucherWrapper voucher) {
		String status = FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get();
		processInvalidVoucher(voucher, OperateType.CHECK, status);
//		new UnionAccbookProcessor().processUnionAccbook(voucher);
		unionAccbookProcessor.processUnionAccbook(voucher);
		//作废复核处理相关业务数据
		processVoucherCheckData(voucher);
		completeCheckNotificationInfo(voucher);//消息完成
	}
	/**
	 * 功能介绍 : 作废复核通过还原原单据信息。<br>
	 * <b>日期：</b>2018年12月4日<br>
	 * @version 1.0
	 * @author liyh
	 */
	private void processVoucherCheckData(VoucherWrapper voucher) {
		updateBusinessData(voucher);
	}
	
	/**
	 * 功能介绍 : 根据原凭证号查询已计提过手续费的手续费凭证数据。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findTargetChargeAccbooksBy(Set<String> bookId) {
		return fcAccbookDao.findTargetChargeAccbooksBy(bookId);
	}
	/**
	 * 功能介绍 : 根据凭证流水号查询冲账作废生成的手续费凭证<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findRushChargeAccbooksBy(String orderNo) {
		return fcAccbookDao.findRushChargeAccbooksBy(orderNo);
	}
	
	/**
	 * 功能介绍 : 作废前校验。<br>
	 * <b>日期：</b>2017年11月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void validInvalidBefore(String bookId) {
		String invalidSign = invalidBefore(bookId);
		if (FinacialConstant.DEAL_FLAG_1.equals(invalidSign)) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_INVALID_NOTALLOW);
		} else if (FinacialConstant.DEAL_FLAG_2.equals(invalidSign)){
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_INVALID_BISEXC_NORMAL);
		}
	}
	
	/**
	 * 功能介绍 : 账务作废前校验。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author liyh 
	 * @return 0 : 可以作废   1：凭证本身不能作废   2：指令未失败不能作废
	 */
	public String invalidBefore(String id) {
		FcAccbookVoucherVO voucher = getFcAccbookVoucherVO(id);
		FcAccbook accbook = voucher.getAccbook();
		String generateMode = accbook.getGenerateMode();//产生方式
		if (GenerateMode.ACCEPTANCE_BOOK.get().equals(generateMode)
				|| GenerateMode.CREDIT_BOOK.get().equals(generateMode)
				|| GenerateMode.INTEREST.get().equals(generateMode)
				|| GenerateMode.DEPOSIT.get().equals(generateMode)
				) {//银企接口收款、承兑汇票、信贷、利息、手续费凭证
			if(GenerateMode.CREDIT_BOOK.get().equals(generateMode) &&
					BusOriType.NB_BANK_REPAYMENT_ONLINE.get().equals(accbook.getBusOriType())) {//如果是线上还款
				return "0";
			}
			return "1";
			//throw new ParameterException(AccbookMessageEnum.FIN_BOOK_INVALID_NOTALLOW);
		}
		if (GenerateMode.INNER_PAY_BOOK.get().equals(generateMode)) {//内网付款
			String sysRaceCode = "";
			//判断业务来源busOriType
			Integer busOriType = accbook.getBusOriType();//业务来源
			String busRaceId = accbook.getBusRaceId();//业务种类
			FcBusRaceDTO busRaceDTO = busRaceService.findFcBusRaceById(busRaceId);
			if (!EmptyUtils.isEmpty(busRaceDTO)) {
				sysRaceCode = busRaceDTO.getSysRaceCode();//系统业务种类
				if (SysRaceCode.NB_UNIT_COMMON_PAYMENT_SAME.get().equals(sysRaceCode)) {//内转
					return "0";
				}
			}
			List<FcAccbookDtl> accbookDtls = voucher.getBookDtls();
			if (!EmptyUtils.isEmptyList(accbookDtls)) {
				FcAccbookDtl accbookDtl = accbookDtls.get(0);
				String billNo = accbookDtl.getOrigVoucherNo();//业务单号即付款单号
				BisExcDTO excDTO = null;
				if (SysRaceCode.NB_CNT_POS_CENTPAY.get().equals(sysRaceCode)) {
					excDTO = bisExcService.getBisExcByVoucherNo(billNo);//中心付款为自营付款和策略产生的指令，直接根据指令编号查询指令状态
				} else {
					String voucherFrom = trans2VoucherFrom(sysRaceCode);
					if (!EmptyUtils.isEmpty(voucherFrom)) {
						excDTO = bisExcService.getBisExcByCbsBillNo(billNo, voucherFrom);//单位付款根据单号和业务来源查询
					}
				}
				if (!EmptyUtils.isEmpty(excDTO)) {
					String voucherStat = excDTO.getVoucherStat();//指令状态
					if (!BdlinkConstant.BANK_FAILED.equals(voucherStat)
							&& !BdlinkConstant.INVALIDATE.equals(voucherStat)
							&& !BdlinkConstant.INTERFACE_FAILED.equals(voucherStat) 
							&& !BdlinkConstant.WAIT_FILL.equals(voucherStat)//待发送
							&& !BdlinkConstant.SUSPECTED.equals(voucherStat)
							) {//指令失败、无效、银行处理失败、可疑指令
						return "2";
					}
				}
			}
		}
		return "0";
	}
	
}
