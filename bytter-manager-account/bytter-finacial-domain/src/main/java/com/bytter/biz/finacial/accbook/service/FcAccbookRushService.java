package com.bytter.biz.finacial.accbook.service;
import com.bytter.biz.finacial.accbook.dao.FcAccbookBlnedDao;
import com.bytter.biz.finacial.accbook.entity.*;
import com.bytter.biz.finacial.accbook.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.GenerateMode;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.VoucherStatus;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcBusRaceDTO;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.util.FinacialUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
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
/**
 * 冲账Service
 * @author liyh
 *
 */
@Service("fcAccbookRushService")
@Transactional
public class FcAccbookRushService extends FcAccbookService {
	@Autowired
	private FcAccbookBlnedDao fcAccbookBlnedDao;
	
	@Autowired
	private FcAccbookBlnedService bookBlnedService;
	@Autowired
	private FcAccbookBlnedDtlService bookBlnedDtlservice;
	
	@Autowired
	private FcAccbookExtService fcAccbookExtService;
	@Autowired
	private FcAccbookBlnedExtService bookBlnedExtService;
	
	@DubboReference(check = false)
	private IFcBusRaceService busRaceService;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	
	/**
	 * 功能介绍 :消息完成。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void completeEditNotificationInfo(VoucherWrapper voucher) {
		if(!voucher.isAutoCheck()){
			for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
				FcAccbook accbook = voucherVO.getAccbook();
				if (!EmptyUtils.isEmpty(accbook.getOrigVoucherNo())) {//冲账凭证
					notificationService.complete(NotificationConstant.FINACIAL.BOOK_RUSH_EDIT, accbook.getId(), voucher.getUserCode());
				}
			}
		}
	}
	
	/**
	 * 功能介绍 :消息完成。<br>
	 * <b>日期：</b>2017年11月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void completeCheckNotificationInfo(VoucherWrapper voucher) {
		if(!voucher.isAutoCheck()){
			for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
				FcAccbook accbook = voucherVO.getAccbook();
				//打回改账处，通知提醒记账人
				if (!EmptyUtils.isEmpty(accbook.getOrigVoucherNo())) {//冲账凭证
					if (!EmptyUtils.isEmpty(accbook.getId())) {
						notificationService.complete(NotificationConstant.FINACIAL.BOOK_RUSH_CHECK, accbook.getId(), voucher.getUserCode());
					}
				}
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
		if(!voucher.isAutoCheck()){
			for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
				FcAccbook accbook = voucherVO.getAccbook();
				// 发送消息复核提醒
				if (!EmptyUtils.isEmpty(accbook.getOrigVoucherNo())) {//冲账凭证
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
							NotificationConstant.FINACIAL.BOOK_RUSH_CHECK, notifyVO, voucher.getUserCode());
				}
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
		if(!voucher.isAutoCheck()){
			for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
				FcAccbook accbook = voucherVO.getAccbook();
				// 打回改账处，通知提醒冲账人
				if (!EmptyUtils.isEmpty(accbook.getOrigVoucherNo())) {//冲账凭证
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
					notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(),
							NotificationConstant.FINACIAL.BOOK_RUSH_EDIT, notifyVO);
				}
			}
		}
	}
	
	/**
	 * 功能介绍 : 冲账前获取冲账凭证ID。<br>
	 * <b>日期：</b>2017年11月8日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void setRushOrigVoucherNo(VoucherWrapper voucher) {
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String blnedDookId = voucherVO.getAccbook().getOrigVoucherNo();
		if (EmptyUtils.isBlank(blnedDookId)) {
			String busGlobalId = voucher.getBusGlobalId();
			if (EmptyUtils.isBlank(busGlobalId)) {
				throw new ParameterException(AccbookMessageEnum.FIN_BOOK_RUSH_BUSGLOBALID_NULL);//冲账需传递参数
			}
			List<FcAccbookBlned> books = bookBlnedService.getBooksByBusGlobalId(busGlobalId);
			if (!EmptyUtils.isEmptyList(books)) {
				blnedDookId = books.get(0).getId();
				voucherVO.getAccbook().setOrigVoucherNo(blnedDookId);//冲账凭证ID
			}
		}
	}
	
	/**
	 * 功能介绍 : 凭证冲账提交。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void submitRushVoucher(VoucherWrapper voucher)  {
		setRushOrigVoucherNo(voucher);
	    voucher.setOpType(OperateType.SUBMIT);
	    processRushVoucherSubmit(voucher);
	    completeEditNotificationInfo(voucher);
	    completeCheckNotificationInfo(voucher);
	    
	    /*
	     * 自动复核
	     */
	    if(voucher.isAutoCheck()){
	    	getEntityDao().flush();
	    	checkRushVoucher(voucher);
	    } else {
			sendCheckNotificationInfo(voucher);
		}
	}
	
	/**
	 * 功能介绍 : 冲账凭证提交处理。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void processRushVoucherSubmit(VoucherWrapper voucher) {
		//1、冲账提交前数据准备，校验
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String blnedDookId = voucherVO.getAccbook().getOrigVoucherNo();
		//TODO 
		validRushBefore(blnedDookId);
		
		boolean isNew = StringUtils.isBlank(voucherVO.getAccbook().getId());
		
		FcAccbookBlned bookBlned = null;
		if (isNew) {
			bookBlned = fcAccbookBlnedDao.get(blnedDookId, true);
			preprocessRushVoucherSave(voucher, bookBlned);
		} else {
			preprocessRushVoucherUpdate(voucher);
		}
		for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {
			if (bookVoucher.getOpType() != OperateType.DELETE) {
				bookVoucher.setOpType(OperateType.SUBMIT);
			}
			//凭证状态设置为待复核
			bookVoucher.getAccbook().setVoucherState(FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get());
		}
		for (FcAccbookVoucherVO bookVoucher : voucher.getOriTarget()) {
			if (bookVoucher.getOpType() != OperateType.DELETE) {
				bookVoucher.setOpType(OperateType.SUBMIT);
			}
		}
//		获取初始联机余额信息
		List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
		//2、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
		Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
		//3、数据保存
		processVoucherRushSave(voucher, ledgerMap, bookBlned, origOnlineLedgerDTOList);
	}
		
	/**
	 * 功能介绍 ； 冲账修改保存。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void updateRushVoucher(VoucherWrapper voucher) {
		voucher.setOpType(OperateType.MODIFY);
		processRushVoucherUpdate(voucher);
		completeEditNotificationInfo(voucher);
		completeCheckNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 冲账修改处理。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void processRushVoucherUpdate(VoucherWrapper voucher) {
		//1、冲账提交前数据准备，校验
		preprocessRushVoucherUpdate(voucher);
		for (FcAccbookVoucherVO bookVoucher : voucher.getTarget()) {//凭证状态设置为暂存
			bookVoucher.getAccbook().setVoucherState(FinaBookConstant.VoucherStatus.STATUS_TEMP.get());
		}
		//		获取初始联机余额信息
		List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
		//2、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
		Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
		//3、数据保存
		processVoucherRushSave(voucher, ledgerMap, null, origOnlineLedgerDTOList);
	}
	
	/**
	 * 功能介绍 ： 冲账凭证修改前数据准备。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void preprocessRushVoucherUpdate(VoucherWrapper voucher) {
		List<FcAccbookVoucherVO> target = voucher.getTarget();
		FcAccbookVoucherVO newRushVoucher = target.remove(0);//新产生的凭证
		
		FcAccbookVoucherVO oriRushVoucher = getFcAccbookVoucherVO(newRushVoucher.getAccbook().getId());
		oriRushVoucher.setOpType(OperateType.MODIFY);
		oriRushVoucher.setRush(true);
		FcAccbook oriRushAccbook = oriRushVoucher.getAccbook();
		validRushBefore(oriRushAccbook.getOrigVoucherNo());//冲账检查
		
		oriRushAccbook.setTallyDate(newRushVoucher.getAccbook().getTallyDate());//修改记账日期
		oriRushAccbook.setInterestStartDate(newRushVoucher.getAccbook().getInterestStartDate());//修改起息日期
		FcAccbookExt bookExt = oriRushVoucher.getBookExt();//凭证附加信息
		FcAccbookExt newbookExt = newRushVoucher.getBookExt();//凭证附加信息
		if (null != bookExt) {
			bookExt.setDesReason(newRushVoucher.getBookExt().getDesReason());// 更新冲账原因
			BeanUtils.copyProperties(bookExt, newbookExt);
			oriRushVoucher.setBookExt(newbookExt);
		} else {
			bookExt = new FcAccbookExt();
			BeanUtils.copyProperties(newbookExt, bookExt);
			oriRushVoucher.setBookExt(bookExt);
		}
		voucher.addOriTarget(oriRushVoucher);//修改前的冲账凭证
		
		if (!target.isEmpty()) {// 有录入新凭证的情况
			FcAccbookVoucherVO newAccbook = target.get(0);
			if (newAccbook.getOpType() == OperateType.DELETE) {// 删除新录入凭证
				FcAccbookVoucherVO oriNewAccbook = getFcAccbookVoucherVO(newAccbook.getAccbook().getId());
				oriNewAccbook.setOpType(newAccbook.getOpType());//删除凭证
				target.remove(0);
				target.add(oriNewAccbook);
			} else {
				//对新录入的凭证进行相关的设置
				newAccbook.getAccbook().setBusOriType(BusOriType.RUSH.get());//冲账
				initAccbook(newAccbook.getAccbook(), VoucherStatus.STATUS_TEMP.get());//暂存状态
				newAccbook.getAccbook().setOperatorCode(voucher.getUserCode());//记账人
				if (newAccbook.getOpType() == null) {
		            if (StringUtils.isNotBlank(newAccbook.getAccbook().getId())) {
		            	newAccbook.setOpType(OperateType.MODIFY);
		            } else {
		            	newAccbook.setOpType(OperateType.ADD);
		            }
		        }
				
				if (StringUtils.isNotBlank(newAccbook.getAccbook().getId())) {//设置原先新录入的凭证
					FcAccbookVoucherVO oriNewAccbook = getFcAccbookVoucherVO(newAccbook.getAccbook().getId());
					oriNewAccbook.setOpType(OperateType.MODIFY);
					voucher.addOriTarget(oriNewAccbook);
				}
			}
		}
		target.add(0, oriRushVoucher);
		processValidate(voucher);
	}
	
	/**
	 * 功能介绍 : 凭证冲账保存。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void saveRushVoucher(VoucherWrapper voucher) {
	    voucher.setOpType(OperateType.ADD);
	    processRushVoucherSave(voucher);
	    //completeEditNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 凭证冲账处理。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void processRushVoucherSave(VoucherWrapper voucher) {
		//1、冲账前数据准备，校验
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String blnedDookId = voucherVO.getAccbook().getOrigVoucherNo();
		validRushBefore(blnedDookId);
		
		FcAccbookBlned bookBlned = fcAccbookBlnedDao.get(blnedDookId, true);
		preprocessRushVoucherSave(voucher, bookBlned);
		//		获取初始联机余额信息
		List<FcOnlineLedgerDTO> origOnlineLedgerDTOList = listFcOnlineLedger(voucher);
		//2、数据准备 FIXME 是否需要同步处理账户余额，保持和其他地方一致
		Map<String, FcOnlineLedgerDTO> ledgerMap = processAccountBalance(voucher);
		processVoucherRushSave(voucher, ledgerMap, bookBlned, origOnlineLedgerDTOList);
	}
	
	/**
	 * 功能介绍 : 处理账务数据和余额更新。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private synchronized void processVoucherRushSave(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap,
			FcAccbookBlned bookBlned, List<FcOnlineLedgerDTO> origLedgerDTOs) {
		//数据保存
		processVoucherSave(voucher, ledgerMap, origLedgerDTOs);
		//更新冲账标识
		if (null != bookBlned) {
			updateOriVoucherRushSign(bookBlned, FinacialConstant.RUSH_SIGN_YES);// 设置为已冲账
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
		return FinacialConstant.FINACIAL_MSGTYPE_R;
	}
	
	/**
	 * 功能介绍 : 更新冲账标识位。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void updateOriVoucherRushSign(FcAccbookBlned bookBlned, String sign) {
		bookBlned.setRushSign(sign);
		bookBlnedService.update(bookBlned);
	}
	
	/**
	 * 功能介绍 : 初始化新增冲账凭证保存。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void preprocessRushVoucherSave(VoucherWrapper voucher, FcAccbookBlned bookBlned) {
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String blnedDookId = voucherVO.getAccbook().getOrigVoucherNo();
		
		List<FcAccbookBlnedDtl> bookBlnedDtls = bookBlnedDtlservice.getAccbookDtlsByBookId(blnedDookId);
		FcAccbookBlnedExt bookBlnedExt = bookBlnedExtService.getBookBlnedExtByBookId(blnedDookId);
		//FcAccbookExtVO extVo = bookBlnedExtService.getBookExtVOByBookId(blnedDookId);
		//重设账务附加信息
		FcAccbookExt bookExt = new FcAccbookExt();
		if (null != bookBlnedExt) {
			BeanUtils.copyProperties(bookBlnedExt, bookExt, new String[]{"id"});
		}
		bookExt.setDesReason(voucherVO.getBookExt().getDesReason());//冲账原因	
		voucherVO.setBookExt(bookExt);
		
		//重设账务主档信息
		FcAccbook accbook = voucherVO.getAccbook();
		FcAccbook rushAccbook = new FcAccbook();
		BeanUtils.copyProperties(bookBlned, rushAccbook);
		rushAccbook.setOrigVoucherNo(blnedDookId);
		rushAccbook.setId(accbook.getId());
		rushAccbook.setTallyDate(accbook.getTallyDate());
		if (null != accbook.getInterestStartDate()) {
			rushAccbook.setInterestStartDate(accbook.getInterestStartDate());
		} else {
			rushAccbook.setInterestStartDate(bookBlned.getInterestStartDate());
		}
		rushAccbook.setBusOriType(BusOriType.RUSH.get());
		initAccbook(rushAccbook, VoucherStatus.STATUS_TEMP.get());//暂存状态
		rushAccbook.setOperatorCode(voucher.getUserCode());//操作用户
		rushAccbook.setRedblueSign(FinacialConstant.RB_SIGN_R);
		rushAccbook.setVoucherNo(null);
		voucherVO.setOpType(OperateType.ADD);//记账暂存
		voucherVO.setAccbook(rushAccbook);
		
		//重设账务明细信息 金额为负数
		List<FcAccbookDtl> bookDtls = new ArrayList<FcAccbookDtl>();
		for (FcAccbookBlnedDtl bookDtl : bookBlnedDtls) {
			FcAccbookDtl copyDtl = new FcAccbookDtl();
			BeanUtils.copyProperties(bookDtl, copyDtl);
			copyDtl.setId(null);
			copyDtl.setCreditMoney(BigDecimal.ZERO.subtract(bookDtl.getCreditMoney()));// 设置金额相反
			copyDtl.setDebitMoney(BigDecimal.ZERO.subtract(bookDtl.getDebitMoney()));
			bookDtls.add(copyDtl);
		}
		voucherVO.setBookDtls(bookDtls);
		
		if (voucher.getTarget().size() > 1) {// 初始化新录入凭证的基本信息
			FcAccbookVoucherVO newVoucher = voucher.getTarget().get(1);
			FcAccbook newAccbook = newVoucher.getAccbook();
			//判断是否删除凭证
			if (null == newVoucher.getOpType()) {
				if (StringUtils.isEmpty(newAccbook.getId())) {
					newVoucher.setOpType(OperateType.ADD);//记账暂存
				} else {
					newVoucher.setOpType(OperateType.MODIFY);//记账修改
				}
			}
			newAccbook.setBusOriType(BusOriType.RUSH.get());
			initAccbook(newAccbook, VoucherStatus.STATUS_TEMP.get());//暂存状态
			newAccbook.setOperatorCode(voucher.getUserCode());//记账人
		}
		
		//为所有的凭证添加新的busGlobalId
		String busGlobalId = voucher.getBusGlobalId();
		if (StringUtils.isBlank(busGlobalId)) {
		    busGlobalId = FinacialUtils.createGlobalId();//生成全局ID
		    voucher.setBusGlobalId(busGlobalId);
		}
		for (FcAccbookVoucherVO bookVoucer : voucher.getTarget()) {
			bookVoucer.getAccbook().setBusGlobalId(busGlobalId);
		}
		processValidate(voucher);
	}
	
	/**
	 * 功能介绍 : 数据加载和校验。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void processValidate(VoucherWrapper voucher){
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
	        //校验凭证数据有效性
	        validateVoucher(voucherVO);
		}
	}
	
	/**
	 * 功能介绍 : 复核前查询冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void preCheckVoucher(VoucherWrapper voucher) {
		//复核前查询凭证数据
		voucher.setOpType(OperateType.CHECK);
		List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(voucher.getBusGlobalId());
		voucher.setTarget(voucherVOs);
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			FcAccbook accbook = voucherVO.getAccbook();
			String blnedBookId = accbook.getOrigVoucherNo();
			if (!EmptyUtils.isEmpty(blnedBookId)) {
				validRushBefore(blnedBookId);//复核前冲账检查
			}
			
			voucherVO.setOpType(OperateType.CHECK);//记账复核通过
			initCheckAccbook(voucherVO.getAccbook(), VoucherStatus.STATUS_CHECK.get());//复核状态
			voucherVO.getAccbook().setCheckerCode(voucher.getUserCode());//复核人
		}
	}
	
	/**
	 * 功能介绍 : 复核冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void checkRushVoucher(VoucherWrapper voucher) {
		//1、复核前加载凭证数据
		preCheckVoucher(voucher);
		
		//2、提交处理数据和余额
		processVoucherCheck(voucher);
		//TODO 3、是否需要还原相关单据数据，待后续添加
		completeCheckNotificationInfo(voucher);
	}
	
	/**
	 * 功能介绍 : 打回前查询冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void preBackVoucher(VoucherWrapper voucher) {
		//打回前查询凭证数据
		voucher.setOpType(OperateType.BACK);
		List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(voucher.getBusGlobalId());
		
		FcAccbookVoucherVO fcAccbook = voucher.getBookVoucher();
		if (null != fcAccbook) {//清空冲账凭证原凭证
			voucher.setTarget(new ArrayList<FcAccbookVoucherVO>());
			
		}
		voucher.addTargets(voucherVOs);
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			voucherVO.setOpType(OperateType.BACK);//记账打回
			voucherVO.getAccbook().setVoucherState(VoucherStatus.STATUS_BACK.get());//打回状态
			
			FcAccbookExt bookExt = voucherVO.getBookExt();
			if (null == bookExt) {
				bookExt = new FcAccbookExt();
				bookExt.setBookId(voucherVO.getAccbook().getId());
				voucherVO.setBookExt(bookExt);
			}
			if (null != fcAccbook.getBookExt()) {
				voucherVO.getBookExt().setBackReason(fcAccbook.getBookExt().getBackReason());//打回原因
			}
		}
	}
	
	/**
	 * 功能介绍 : 打回冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void backRushVoucher(VoucherWrapper voucher) {
		//1、打回前加载凭证数据
		preBackVoucher(voucher);
		
		//2、打回处理数据和余额
		processVoucherBack(voucher);
	
		completeCheckNotificationInfo(voucher);
		
		sendBackNotificationInfo(voucher);
	}
	
	private void preDeleteVoucher(VoucherWrapper voucher) {
		//删除
		voucher.setOpType(OperateType.DELETE);
		List<FcAccbookVoucherVO> voucherVOs = getBookVoucherByBusGlobalId(voucher.getBusGlobalId());
		voucher.addTargets(voucherVOs);
		for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
			voucherVO.setOpType(OperateType.DELETE);//抹账
		}
	}
	
	/**
	 * 功能介绍 : 删除冲账凭证数据。<br>
	 * <b>日期：</b>2017年7月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteRushVoucher(VoucherWrapper voucher) {
		//1、抹账前加载凭证数据
		preDeleteVoucher(voucher);
		FcAccbookVoucherVO voucherVO = voucher.getBookVoucher();
		String blnedDookId = voucherVO.getAccbook().getOrigVoucherNo();
		FcAccbookBlned bookBlned = fcAccbookBlnedDao.get(blnedDookId, true);
		completeEditNotificationInfo(voucher);
		completeCheckNotificationInfo(voucher);
		
		//2、抹账处理数据和余额
		processVoucherDelete(voucher);
		//3、还原冲账标识
		if (null != bookBlned) {
			updateOriVoucherRushSign(bookBlned, FinacialConstant.RUSH_SIGN_NO);// 还原为未冲账
		}
	}
	
	/**
	 * 功能介绍 : 冲账前校验。<br>
	 * <b>日期：</b>2017年11月3日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	private void validRushBefore(String bookId) {
		String invalidSign = rushBefore(bookId);
		if (FinacialConstant.DEAL_FLAG_1.equals(invalidSign)) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_RUSH_NOTALLOW);
		} else if (FinacialConstant.DEAL_FLAG_2.equals(invalidSign)){
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_RUSH_BISEXC_NORMAL);
		}
	}
	
	/**
	 * 功能介绍 : 账务冲账前校验。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author liyh 
	 * @return 0 : 可以冲账  1：凭证本身不能冲账   2：指令未失败不能冲账
	 */
	public String rushBefore(String id) {
		FcAccbookPageVO voucher = bookBlnedService.getFcAccbookBlnedPageVOById(id,false);
		if (null == voucher || voucher.getAccbook() == null) {
			return "0";
		}
		FcAccbookVO accbook = voucher.getAccbook();
		String generateMode = accbook.getGenerateMode();//产生方式
		if (GenerateMode.BANK_COLLECT_BOOK.get().equals(generateMode)
				|| GenerateMode.ACCEPTANCE_BOOK.get().equals(generateMode)
				|| GenerateMode.CREDIT_BOOK.get().equals(generateMode)
				|| GenerateMode.INTEREST.get().equals(generateMode)
//				|| GenerateMode.HANDLE_CHARGE.get().equals(generateMode)
				|| GenerateMode.DEPOSIT.get().equals(generateMode)
				) {//银企接口收款、承兑汇票、信贷、利息、手续费凭证
			return "1";
			//throw new ParameterException(AccbookMessageEnum.FIN_BOOK_INVALID_NOTALLOW);
		}
		if (GenerateMode.INNER_PAY_BOOK.get().equals(generateMode)) {//内网付款
			String sysRaceCode = "";
			//判断业务来源busOriType
			String busRaceId = accbook.getBusRaceId();//业务种类
			FcBusRaceDTO busRaceDTO = busRaceService.findFcBusRaceById(busRaceId);
			if (!EmptyUtils.isEmpty(busRaceDTO)) {
				sysRaceCode = busRaceDTO.getSysRaceCode();//系统业务种类
				if (SysRaceCode.NB_UNIT_COMMON_PAYMENT_SAME.get().equals(sysRaceCode)) {//内转
					return "0";
				}
			}
			List<FcAccbookDtlVO> accbookDtls = voucher.getBookDtls();
			if (!EmptyUtils.isEmptyList(accbookDtls)) {
				FcAccbookDtlVO accbookDtl = accbookDtls.get(0);
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
