package com.bytter.biz.finacial.closedeal.service;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.closedeal.vo.CldBaseCheckInfoBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsLoanSendOutCheckDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendCheckDTO;
import com.bytter.finance.dto.CmsProvideLoanSendCheckDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * 日结控制不检查service
 * @author liyh
 *
 */
@Service("fcNoControlCheckService")
@Transactional
public class FcNoControlCheckService extends FcCloseBaseService {
	
	@DubboReference(check = false)
	private ICmsLoanService cmsLoanService;
	/**
	 * 功能介绍 : 日结不检查项。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CldBaseCheckInfoBean> checkDayNoControl(FcDayCloseDTO dayClose) {
		List<CldBaseCheckInfoBean> rt = new ArrayList<CldBaseCheckInfoBean>();
		int serNo = 1;
        /*
        //存款业务:存在有几笔定期到期的业务！
        setRegularDue(rt,param,serNo++);
        //存款业务:存在有几笔协定到期的业务！
        setAgreedDeposit(rt,param,serNo);
        */
        //企业贷款到期提醒查询
		setEnterpriseLoans(dayClose, rt, serNo++);
		//银行贷款提醒查询
		setBankLoans(dayClose, rt, serNo++);
		//委托贷款提醒查询
		setEntrustLoans(dayClose, rt, serNo++);
        /*
        //同业拆借提醒查询
        setInterbankLending(rt,param,serNo);
        //承兑托收到期
        setAbOperation(rt,param,serNo);
        //承兑定期到期
        //setAbReceive(rt,param,serNo);
        //是否有未冲的预提
        setRushPredraw(rt, param, serNo);*/
        
		
        return rt;
	}
	
	/**
	 * 功能介绍 ：查询内部贷款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsLoanSendOutCheckDTO> getLoanSendOutInfoByEndDate(FcDayCloseDTO dayClose) {
		// todo:数据转换
		com.bytter.finance.dto.FcDayCloseDTO fcDayCloseDTO = BeanUtils.copyNewInstanceProperties(dayClose, com.bytter.finance.dto.FcDayCloseDTO.class);
		return cmsLoanService.getLoanSendOutInfoByEndDate(fcDayCloseDTO);
	}
	
	/**
	 * 功能介绍 ：查询银行借款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsProvideLoanSendCheckDTO> getBankLoadSendInfoByEndDate(FcDayCloseDTO dayClose) {
		// todo:数据转换
		com.bytter.finance.dto.FcDayCloseDTO fcDayCloseDTO = BeanUtils.copyNewInstanceProperties(dayClose, com.bytter.finance.dto.FcDayCloseDTO.class);
		return cmsLoanService.getBankLoadSendInfoByEndDate(fcDayCloseDTO);
	}
	
	/**
	 * 功能介绍 ：查询委托贷款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsPrecativeLoanSendCheckDTO> getPrecativeLoanInfoByEndDate(FcDayCloseDTO dayClose) {
		// todo:数据转换
		com.bytter.finance.dto.FcDayCloseDTO fcDayCloseDTO = BeanUtils.copyNewInstanceProperties(dayClose, com.bytter.finance.dto.FcDayCloseDTO.class);
		return cmsLoanService.getPrecativeLoanInfoByEndDate(fcDayCloseDTO);
	}
	
	/**
	 * 功能介绍 ： 内部贷款到期提醒。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public void setEnterpriseLoans(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<CmsLoanSendOutCheckDTO> list = getLoanSendOutInfoByEndDate(dayClose);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.INTRA_FAMILY_LOAN.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.CREDIT.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.INTRA_FAMILY_LOAN.getName()));
			rt.add(check1);
		}
	}
	
	/**
	 * 功能介绍 ： 银行借款到期提醒。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public void setBankLoans(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<CmsProvideLoanSendCheckDTO> list = getBankLoadSendInfoByEndDate(dayClose);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.BANK_LEND.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.CREDIT.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.BANK_LEND.getName()));
			rt.add(check1);
		}
	}
	
	/**
	 * 功能介绍 ： 委托贷款到期提醒。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public void setEntrustLoans(FcDayCloseDTO dayClose, List<CldBaseCheckInfoBean> rt, int serNo) {
		List<CmsPrecativeLoanSendCheckDTO> list = getPrecativeLoanInfoByEndDate(dayClose);
		if (!EmptyUtils.isEmptyList(list)) {
			CldBaseCheckInfoBean check1 = new CldBaseCheckInfoBean();
			check1.setCheckNumber(serNo);
			check1.setCheckType(CloseConstant.DayCheckControlItem.ENTRUST_LOAN.getValue());
			check1.setModelName(CloseConstant.DayCheckModel.CREDIT.getValue());
			check1.setCheckMessage(CldBaseCheckInfoBean
					.getCheckControlMessage(CloseConstant.DayCheckControlItem.ENTRUST_LOAN.getName()));
			rt.add(check1);
		}
	}
	
}
