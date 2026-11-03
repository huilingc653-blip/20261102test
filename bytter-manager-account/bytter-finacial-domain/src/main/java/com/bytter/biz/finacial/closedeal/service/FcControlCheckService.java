package com.bytter.biz.finacial.closedeal.service;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.payment.api.dto.FcFreezeDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcFreezeService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.closedeal.bean.FcAccInitCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcFreezeCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcOutAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.dao.FcControlCheckDao;
import com.bytter.biz.finacial.closedeal.vo.CldBaseCheckInfoBean;
import com.bytter.biz.payment.api.dto.FcAccInitCheckBeanDTO;
import com.bytter.biz.payment.api.service.IFcInitAccService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 日结控制检查service
 * @author liyh
 *
 */
@Service("fcControlCheckService")
@Transactional
public class FcControlCheckService extends FcCloseBaseService {
	@Autowired
	private FcControlCheckDao checkDao;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IFcInitAccService iFcInitAccService;
	@DubboReference(check = false)
	private ISystemForeignService iSystemForeignService;
	@DubboReference(check = false)
	private IUserService iUserService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;

	@DubboReference(check = false)
	private IFcFreezeService iFcFreezeService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	/**
	 * 功能介绍 : 日结控制检查。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<CldBaseCheckInfoBean> checkDayControl(FcDayCloseDTO dayClose) {
		List<CldBaseCheckInfoBean> rt = new ArrayList<CldBaseCheckInfoBean>();
        int serNo = 0;
        
        // 1  账户管理 存在未复核的记录  
        setNoCheckVFcAcc(dayClose, rt, serNo++);
        //1、1. 账户管理 添加初始化未复核
        setNoCheckAccountInit(dayClose, rt, serNo++);
        // 2 存款业务 存在冻结或解冻扣划未复核的记录
        setNoCheckFreeze(dayClose, rt, serNo++);
        // 2.0.1 存款业务 存在扣划未复核的记录
        //setNoCheckFreezeDeduct(dayClose, rt, serNo++);
        // 2.1 存款业务 通知信息取消登记未复核
        //setNoCheckNoticeRegister(dayClose, rt, serNo++);
        // 3  存款业务 存在存款取款未复核的记录    
        //setNoCheckFcDepositManager(dayClose, rt, serNo++);
        // 3  存款业务 存在定期质押或者定期解除质押的未复核记录
        //setNoCheckFcFixedDeposit(dayClose, rt, serNo++);
        // 4  存款业务 存在协定登记或变更未复核的记录
        //setNoCheckAgreedDepositList(dayClose, rt, serNo++);
        // 4.1 存款业务 到期处理未复核的记录
        //setNoCheckDeferredDeal(dayClose, rt, serNo++);
        // 5  网银控制台 存在单位申请付款未复核的记录
        //setNoCheckUnitData(dayClose, rt, serNo++);
        //测试数据 ：6  网银控制台 存在中心申请付款未复核的记录   
        //setNoCheckCenterPaymen(dayClose, rt, serNo++);
        // 7  网银控制台 存在委托收款未复核的记录
        //setNoCheckConsignCollection(dayClose, rt, serNo++);
        //8  网银控制台 存在银企接口收款未复核的记录 
        //setNoCheckBankCollectionQuery(dayClose, rt, serNo++);
        //9  网银控制台 存在结售汇未复核的记录 
        //setNoCheckConditionList(dayClose, rt, serNo++);
        //10 网银控制台 存在存取款未复核的记录  
        //setNoCheckDepositOrDrawQuery(dayClose, rt, serNo++);
        //11 网银控制台 存在银行借款未复核的记录  
        //setNoCheckBankLoan(dayClose, rt, serNo++);
        // 12 网银控制台 存在同业拆入拆出未复核的记录 
        //setNoCheckInterBank(dayClose, rt, serNo++);
        // 13 网银控制台 存在企业内部借款未复核的记录
        //setNoCheckEnterpriseLoan(dayClose, rt, serNo++);
        //14 网银控制台 存在委托贷款未复核的记录 
        //setNoCheckForConsignLoan(dayClose, rt, serNo++);
        //15 柜台账务 存在柜台账务未复核的记录
        setNoCheckAccBookDtl(dayClose, rt, serNo++);
        //17 表外账务 存在表外账务未复核的记录
        setNoCheckOutAccBookDtl(dayClose, rt, serNo++);
		return rt;
	}
	@Transactional(readOnly = true)
	public void setNoCheckVFcAcc(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		// todo:数据转换
		com.bytter.system.api.dto.FcDayCloseDTO fcDayCloseDTO = BeanUtils.copyNewInstanceProperties(dayClose, com.bytter.system.api.dto.FcDayCloseDTO.class);
		List<FcAccDTO> list = fcAccService.findNoCheckVFcAcc(fcDayCloseDTO);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.ACCOUNT.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.ACCOUNT.getValue());
			check1.setCheckMessage(
					CldBaseCheckInfoBean.getCheckControlMessage(CloseConstant.DayCheckControlItem.ACCOUNT.getName()));
			rt.add(check1);
		}
    }
	@Transactional(readOnly = true)
	public List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose){
		// todo:数据转换
		com.bytter.system.api.dto.FcDayCloseDTO fcDayCloseDTO = BeanUtils.copyNewInstanceProperties(dayClose, com.bytter.system.api.dto.FcDayCloseDTO.class);
		return fcAccService.findNoCheckVFcAcc(fcDayCloseDTO);
	}
	@Transactional(readOnly = true)
	public void setNoCheckAccountInit(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		// 改为远程调用
//		List<FcAccInitCheckBean> list = checkDao.findNoCheckAccInit(dayClose);
		com.bytter.biz.payment.api.dto.FcDayCloseDTO fcDayCloseDTO = new com.bytter.biz.payment.api.dto.FcDayCloseDTO();
		BeanUtils.copyProperties(dayClose,fcDayCloseDTO);
		List<FcAccInitCheckBeanDTO> noCheckAccInit = iFcInitAccService.findNoCheckAccInit(fcDayCloseDTO);
		if (!EmptyUtils.isEmptyList(noCheckAccInit)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.ACCOUNT_INITCHECK.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.ACCOUNT.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.ACCOUNT_INITCHECK.getName()));
			rt.add(check1);
		}
	}
	@Transactional(readOnly = true)
	public List<FcAccInitCheckBean> findNoCheckAccInit(FcDayCloseDTO dayClose){
		// 改为远程调用
//		return checkDao.findNoCheckAccInit(dayClose);
		com.bytter.biz.payment.api.dto.FcDayCloseDTO fcDayCloseDTO = new com.bytter.biz.payment.api.dto.FcDayCloseDTO();
		BeanUtils.copyProperties(dayClose,fcDayCloseDTO);
		List<FcAccInitCheckBeanDTO> noCheckAccInit = iFcInitAccService.findNoCheckAccInit(fcDayCloseDTO);
		return BeanUtils.copyListProperties(noCheckAccInit,FcAccInitCheckBean.class);
	}
	@Transactional(readOnly = true)
	public void setNoCheckFreeze(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<FcFreezeDTO> list = iFcFreezeService.findNoCheckFreeze(dayClose.getCloseDate(), getAccList(dayClose.getNetCode()));
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.FREEZE.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.ACCOUNT.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.ACCOUNT.getName()));
			rt.add(check1);
		}
	}

	private List<String> getAccList(String netId){
		List<FcAccDTO> fcAccDTOS = fcAccService.getFcAccByNetId(netId);
		return fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<FcFreezeCheckBean> findNoCheckFreeze(FcDayCloseDTO dayClose) {
		List<FcFreezeDTO> fcFreezeDTOS = iFcFreezeService.findNoCheckFreeze(dayClose.getCloseDate(), getAccList(dayClose.getNetCode()));
		List<String> loginNames = fcFreezeDTOS.stream().map(FcFreezeDTO::getFreezeOperator).collect(Collectors.toList());
		Map<String,String> userNameMap = iUserService.getUserNameByLoginNames(loginNames);
		List<String> blnctrAccs = fcFreezeDTOS.stream().map(FcFreezeDTO::getBlnctrAcc).collect(Collectors.toList());
		String netId = dayClose.getNetCode();
		Map<String,String> accNameMap = iFcAccService.getAccNameByNetIdAndBlnctrAccs(netId, blnctrAccs);
		fcFreezeDTOS.forEach(a -> {
			a.setFreezeOperator(userNameMap.get(a.getFreezeOperator())); //这里历史遗留，一个字段2用
			a.setAccName(accNameMap.get(a.getBlnctrAcc()));
		});
		return BeanUtils.copyListProperties(fcFreezeDTOS,FcFreezeCheckBean.class);
	}
	@Transactional(readOnly = true)
	public void setNoCheckAccBookDtl(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<FcAccbookCheckBean> list = checkDao.queryNoCheckAccBookDtl(dayClose);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.COUNTER_ACCOUNT.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.COUNTER.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.COUNTER_ACCOUNT.getName()));
			rt.add(check1);
		}
	}
	@Transactional(readOnly = true)
	public List<FcAccbookCheckBean> getNoCheckBookDtl(FcDayCloseDTO dayClose) {
		List<FcAccbookCheckBean> list = checkDao.queryNoCheckAccBookDtl(dayClose);
		List<String> ids = list.stream().map(FcAccbookCheckBean::getOperatorCode).collect(Collectors.toList());
		Map<String, String> blnctrAccAndAccNameMap = iFcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
		//获取付款方式
		Map<String, FcVoucherTypeDTO> fcVoucherTypeMap = fcVoucherTypeService.getFcVoucherTypeMap();
		//填充 userName
		Map<String,String> userNameMap = iUserService.getUserNameByIds(ids);
		list.forEach(a ->{
			a.setOperatorCode(userNameMap.get(a.getOperatorCode()));
			a.setAccName(blnctrAccAndAccNameMap.get(a.getAccount()));
			if(fcVoucherTypeMap != null){
				if(fcVoucherTypeMap.containsKey(a.getVoucherType())){
					FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeMap.get(a.getVoucherType());
					if(fcVoucherTypeDTO != null){
						a.setVoucherName(fcVoucherTypeDTO.getVoucherName());
					}
				}
			}
		});
		return list;
	}
	@Transactional(readOnly = true)
	public void setNoCheckOutAccBookDtl(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<FcOutAccbookCheckBean> list = checkDao.queryNoCheckOutAccBookDtl(dayClose);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.COUNTER_OUTER_ACCOUNT.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.OUTER_COUNTER.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.COUNTER_OUTER_ACCOUNT.getName()));
			rt.add(check1);
		}
	}
	@Transactional(readOnly = true)
	public List<FcOutAccbookCheckBean> getNoCheckOutBookDtl(FcDayCloseDTO dayClose) {
		List<FcOutAccbookCheckBean> list = checkDao.queryNoCheckOutAccBookDtl(dayClose);
		String netId = dayClose.getNetCode();
		List<String> accounts = list.stream().map(FcOutAccbookCheckBean::getAccount).collect(Collectors.toList());
		Map<String, String> accNameMap = iFcAccService.getAccNameByNetIdAndBlnctrAccs(netId, accounts);
		List<String> ids = list.stream().map(FcOutAccbookCheckBean::getOperator).collect(Collectors.toList());
		Map<String, String> userNameMap = iUserService.getUserNameByIds(ids);
		list.forEach(a ->{
			a.setOperator(userNameMap.get(a.getOperator()));
			a.setAccName(accNameMap.get(a.getAccount()));
		});
		return list;
	}
	/**
	 * 功能介绍 : 日结控制检查。<br>
	 * <b>日期：</b>2019年4月30日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public List<CldBaseCheckInfoBean> checkPoolDayControl(FcDayCloseDTO dayClose) {
		List<CldBaseCheckInfoBean> rt = new ArrayList<CldBaseCheckInfoBean>();
		int serNo = 0;
		//TODO
		// 1  账户管理 资金池主体存在未复核的记录
		setNoCheckVFcAcc(dayClose, rt, serNo++);
		// 2 网络记账 存在未复核的记录
		setNoCheckAccBookDtl(dayClose, rt, serNo++);
		return rt;
	}
}
