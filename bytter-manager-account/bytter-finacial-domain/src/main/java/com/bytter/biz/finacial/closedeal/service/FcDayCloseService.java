package com.bytter.biz.finacial.closedeal.service;
import com.bytter.biz.finacial.accbook.service.FcChargeAccrueService;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.closedeal.bean.FcAccInitCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcFreezeCheckBean;
import com.bytter.biz.finacial.closedeal.bean.FcOutAccbookCheckBean;
import com.bytter.biz.finacial.closedeal.dao.FcDayCloseDao;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.entity.FcDayStart;
import com.bytter.biz.finacial.closedeal.vo.CldBaseCheckInfoBean;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.biz.finacial.paramconfig.entity.FcSetMonth;
import com.bytter.biz.finacial.paramconfig.service.FcSetMonthService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.CmsLoanSendOutCheckDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendCheckDTO;
import com.bytter.finance.dto.CmsProvideLoanSendCheckDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
* @Title: Service
* @Description: FC_DAY_CLOSE
* @author onlineGenerator
* @date 2017-07-06 14:51:02
* @version 1.0
*
*/
@Service("fcDayCloseService")
@Transactional
public class FcDayCloseService extends BaseService<FcDayClose,String> {
	@Autowired
	private FcDayCloseDao fcDayCloseDao;
	@Autowired
	private FcCarryService carryService;
	@Autowired
	private FcDayOverService overService;
	@Autowired
	private FcControlCheckService checkService;
	@Autowired
	private FcNoControlCheckService noCheckService;
	@Autowired
	private FcSetMonthService setMonthService;
	@Autowired
	private FcChargeAccrueService accrueService;
	@DubboReference(check = false)
	private IConfigService configService;
	@Autowired
	private FcDayStartService fcDayStartService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcDayClose entity){
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
 	public void save(FcDayClose entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcDayClose entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcDayClose entity){
 		super.saveOrUpdate(entity);
 	}
 	
 	public BaseDao<FcDayClose, String> getEntityDao() {
		return fcDayCloseDao;
	}
 	
 	/**
	 * 功能介绍 : 获取网点的日结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
 	@Transactional(readOnly = true)
	public FcDayClose getDayCloseDate(String netId) {
		return fcDayCloseDao.getDayCloseDate(netId);
	}
	/**
	* <br>
	* <b>功能：</b> 获取资金池主体日结日期 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2019/4/30 <br>
	*/
	@Transactional(readOnly = true)
	public FcDayClose getPoolDayCloseDate(String corpId) {
		return fcDayCloseDao.getDayCloseDateByType(corpId, CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get());
	}
	
	/**
	 * 功能介绍 : 获取网点的月结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
 	@Transactional(readOnly = true)
	public FcDayClose getMonthCloseDate(String netId) {
		return fcDayCloseDao.getMonthCloseDate(netId);
	}
	
	/**
	 * 功能介绍 : 获取网点的日结日期。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcMonthCloseDTO getMonthCloseFinalDate(String netId) {
		FcDayClose dayClose = getMonthCloseDate(netId);
		if (null == dayClose) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NO_INIT_NET);
		}
		FcMonthCloseDTO closeDTO = new FcMonthCloseDTO();
		closeDTO.setNetCode(netId);
		Calendar cal = Calendar.getInstance();
        cal.setTime(dayClose.getCloseDate());
        //待月结月份
        cal.add(Calendar.MONTH, 1);//默认下一个月为月结日期
		
        closeDTO.setCloseYear(cal.get(Calendar.YEAR));
        closeDTO.setCloseMonth(cal.get(Calendar.MONTH) + 1);
		return closeDTO;
	}
	
	/**
	 * 功能介绍 : 获取网点的日结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcDayCloseDTO getDayCloseFinalDate(String netId) {
		FcDayClose dayClose = getDayCloseDate(netId);
		if (null == dayClose) {//网点未初始化
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NO_INIT_NET);
		}
		FcDayCloseDTO closeDTO = new FcDayCloseDTO();
		BeanUtils.copyProperties(dayClose, closeDTO);
		return closeDTO;
	}
	/**
	* <b>功能：</b> 获取资金池主体日结日期 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2019/5/7 <br>
	*/
	public FcDayCloseDTO getPoolDayCloseFinalDate(String netId) {
		FcDayClose dayClose = getPoolDayCloseDate(netId);
		if (null == dayClose) {//资金池主体未初始化
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NO_INIT_POOLNET);
		}
		FcDayCloseDTO closeDTO = new FcDayCloseDTO();
		BeanUtils.copyProperties(dayClose, closeDTO);
		return closeDTO;
	}
 	
	/**
	 * 功能介绍 : 初始化网点日结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
 	public void initNetCloseDate(String netId, Date closeDate, String userId) {
 		if (StringUtils.isEmpty(netId)) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_INIT_NET_NULL);
		}
 		//获取网点的日结日期，不存在则初始化
 		FcDayClose dayClose = getDayCloseDate(netId);
 		if (null == dayClose) {
			dayClose = new FcDayClose();
			dayClose.setNetCode(netId);//网点
			if (null == closeDate) {//如果初始化日期为空，则默认为当前日期的前一日
				closeDate = DateUtils.addDay(DateUtils.getDate(), -1);
				closeDate = DateUtils.parseDate(closeDate);//日期时分秒初始化为0
			}
			
			dayClose.setOverType(CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get());//结转类型
			dayClose.setCloseDate(closeDate);//日结日期
			dayClose.setCreateBy(userId);
 			dayClose.setCreateTime(DateUtils.getDate());
 			fcDayCloseDao.save(dayClose);
		}
 		
 		//获取网点的月结日期，不存在则初始化
 		FcDayClose monthClose = getMonthCloseDate(netId);
 		if (null == monthClose) {
 			monthClose = new FcDayClose();
 			monthClose.setNetCode(netId);//网点
			if (null == closeDate) {//如果初始化日期为空，则默认为当前日期的前一日
				closeDate = DateUtils.addDay(DateUtils.getDate(), -1);
				closeDate = DateUtils.parseDate(closeDate);//日期时分秒初始化为0
			}
			Calendar calendar = Calendar.getInstance(Locale.CHINA);
			calendar.setTime(closeDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.add(Calendar.MONTH, -1);//上个月作为已完成月结日期
			monthClose.setCloseDate(calendar.getTime());//月结日期
			
			monthClose.setOverType(CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());//结转类型
			monthClose.setCreateBy(userId);
			monthClose.setCreateTime(DateUtils.getDate());
 			fcDayCloseDao.save(monthClose);
		}
 	}
 	/**
 	* <b>功能：</b> 初始化资金池主体日结日期 <br>
 	* <b>作者：</b>liyh<br>
 	* <b>日期：</b> 2019/5/7 <br>
 	*/
	public void initPoolNetCloseDate(String netId, Date closeDate, String userId) {
		if (StringUtils.isEmpty(netId)) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_INIT_POOLNET_NULL);
		}
		//获取网点的日结日期，不存在则初始化
		FcDayClose dayClose = getPoolDayCloseDate(netId);
		if (null == dayClose) {
			dayClose = new FcDayClose();
			dayClose.setNetCode(netId);//网点
			if (null == closeDate) {//如果初始化日期为空，则默认为当前日期的前一日
				closeDate = DateUtils.addDay(DateUtils.getDate(), -1);
				closeDate = DateUtils.parseDate(closeDate);//日期时分秒初始化为0
			}
			dayClose.setOverType(CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get());//资金池结转类型
			dayClose.setCloseDate(closeDate);//日结日期
			dayClose.setCreateBy(userId);
			dayClose.setCreateTime(DateUtils.getDate());
			fcDayCloseDao.save(dayClose);
		}
	}
 	
 	/**
 	 * 功能介绍 : 日结前检查。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
 	@Transactional(readOnly = true)
 	public Map<String, Object> dayCloseCheck(FcDayCloseDTO dayClose){
 		Map<String, Object> returnMap = new HashMap<String, Object>();
 		List<CldBaseCheckInfoBean> listCheck = checkService.checkDayControl(dayClose);
        returnMap.put("checkList", listCheck);
        List<CldBaseCheckInfoBean> listNoCheck = noCheckService.checkDayNoControl(dayClose);
        returnMap.put("unCheckList", listNoCheck);
        
        if (listCheck == null || listCheck.isEmpty()) {
        	returnMap.put("success", "0");//可以执行日结程序
        } else {
        	returnMap.put("success", "1");
		}
 		
 		return returnMap;
 	}
 	/**
 	* <br>
 	* <b>功能：</b> 跨日日结检查是否有未结的账 <br>
 	* <b>作者：</b>liyh<br>
 	* <b>日期：</b> 2019/4/30 <br>
 	*/
 	public void checkCanOverDays(FcDayCloseVO closeVO){
        Date startDate = closeVO.getLastCloseDate();
        Date endDate = closeVO.getCloseDate();
        if (DateUtils.dayDiffs(endDate, startDate) > 1) {
			if (overService.existAccbookOverDays(closeVO)) {
				throw new ParameterException(CloseMessageEnum.FIN_CLOSE_EXIST_ACCBOOK, DateUtils.formatDate(endDate));
			}
        }
    }
 	/**
 	 * 功能介绍 : 日结过程调用。<br>
 	 * <b>日期：</b>2017年7月11日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	public void doDayClose(FcDayCloseVO closeVO, ICurrentUser user) {
		//跨日日结检查不能有未结账务
        checkCanOverDays(closeVO);
		
		boolean beforeCheck = beforeDayCloseCheck(closeVO, CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get());
		if (!beforeCheck) {//结转日期和本次日结开始日期不一致，需重新进入日结加载网点日结日期
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_DATE_ERROR);
		}
		//日结检查控制项和不控制项
		
		overService.exceDayClose(closeVO, user);
	}
	
	
	/**
	 * 功能介绍 : 日结前检查。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public boolean beforeDayCloseCheck(FcDayCloseVO closeVO, String overType) {
		// 取结转记录中最近一条结转数据，比较结转结束日期和日结日期
		FcCarry carry = carryService.queryNoReverseFcCarries(closeVO.getNetCode(), overType);
		if (carry != null) {
			Date voucherEndDate = carry.getVoucherEndDate();// 上次结转结束日期
			if (voucherEndDate != null) {
				// 上次结转日期和本次日结开始日期相同，则可以进行日结操作
				if (DateUtils.formatDate(voucherEndDate).equals(DateUtils.formatDate(closeVO.getLastCloseDate()))) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 功能介绍 : 资金池主体单位日结前检查。<br>
	 * <b>日期：</b>2019年4月30日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> dayPoolCloseCheck(FcDayCloseDTO dayClose){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<CldBaseCheckInfoBean> listCheck = checkService.checkPoolDayControl(dayClose);
		returnMap.put("checkList", listCheck);
		if (listCheck == null || listCheck.isEmpty()) {
			returnMap.put("success", "0");//可以执行日结程序
		} else {
			returnMap.put("success", "1");
		}
		return returnMap;
	}
	/**
	* <br>
	* <b>功能：</b> 资金池主体单位日结 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2019/4/30 <br>
	*/
	public void doPoolDayClose(FcDayCloseVO closeVO, ICurrentUser user) {
		//跨日日结循环调用
		checkCanOverDays(closeVO);
		boolean beforeCheck = beforeDayCloseCheck(closeVO, CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get());
		if (!beforeCheck) {//结转日期和本次日结开始日期不一致，需重新进入日结加载网点日结日期
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_POOL_DATE_ERROR);
		}
		//日结处理
		overService.excePoolDayClose(closeVO, user);
	}
	
	/**
 	 * 功能介绍 : 账务明细不通过查看。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	@Transactional(readOnly = true)
	public List<FcAccbookCheckBean> getNoCheckBookDtl(FcDayCloseDTO dayClose) {
		return checkService.getNoCheckBookDtl(dayClose);
	}
	
	/**
 	 * 功能介绍 : 表外账务明细不通过查看。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	@Transactional(readOnly = true)
	public List<FcOutAccbookCheckBean> getNoCheckOutBookDtl(FcDayCloseDTO dayClose) {
		return checkService.getNoCheckOutBookDtl(dayClose);
	}
	
	/**
 	 * 功能介绍 : 账户复核不通过查询。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	@Transactional(readOnly = true)
	public List<FcAccDTO> findNoCheckVFcAcc(FcDayCloseDTO dayClose){
		return checkService.findNoCheckVFcAcc(dayClose);
	}
	
	/**
 	 * 功能介绍 : 账户初始化不通过查看。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	@Transactional(readOnly = true)
	public List<FcAccInitCheckBean> findNoCheckAccInit(FcDayCloseDTO dayClose){
		return checkService.findNoCheckAccInit(dayClose);
	}
	
	/**
 	 * 功能介绍 : 冻结解冻不通过查看。<br>
 	 * <b>日期：</b>2017年7月13日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	@Transactional(readOnly = true)
	public List<FcFreezeCheckBean> findNoCheckFreeze(FcDayCloseDTO dayClose) {
		return checkService.findNoCheckFreeze(dayClose);
	}
	
	/**
	 * 功能介绍 ：查询内部贷款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsLoanSendOutCheckDTO> findLoanSendOutInfo(FcDayCloseDTO dayClose) {
		return noCheckService.getLoanSendOutInfoByEndDate(dayClose);
	}
	
	/**
	 * 功能介绍 ：查询内部贷款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsProvideLoanSendCheckDTO> findBankLoadSendInfo(FcDayCloseDTO dayClose) {
		return noCheckService.getBankLoadSendInfoByEndDate(dayClose);
	}
	
	/**
	 * 功能介绍 ：查询内部贷款到期数据。<br>
	 * <b>日期：</b>2017年11月15日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public List<CmsPrecativeLoanSendCheckDTO> findPrecativeLoanInfo(FcDayCloseDTO dayClose) {
		return noCheckService.getPrecativeLoanInfoByEndDate(dayClose);
	}
	
	/**
 	 * 功能介绍 : 日结过程调用。<br>
 	 * <b>日期：</b>2017年7月11日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	public void execReverseDay(FcDayCloseDTO closeVO, ICurrentUser user,boolean isPool) {
		beforeReDayCloseCheck(closeVO);
		overService.exceReDayClose(closeVO, user,isPool);
	}
	/**
	 * 功能介绍 : 反日结前检查。<br>
	 * <b>日期：</b>2017年7月14日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void beforeReDayCloseCheck(FcDayCloseDTO closeVO) {
		// 取结转记录中最近一条结转数据，比较结转结束日期和日结日期
		FcCarry carry = carryService.queryNoReverseFcCarries(closeVO.getNetCode(),
				CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get());
		if (carry == null) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NO_CARRY);
		}
		boolean checkFlag = false;
		if (carry != null) {
			Date voucherEndDate = carry.getVoucherEndDate();// 上次结转结束日期
			if (voucherEndDate != null) {
				// 上次结转日期和本次日结开始日期相同，则可以进行日结操作
				if (DateUtils.formatDate(voucherEndDate).equals(DateUtils.formatDate(closeVO.getLastCloseDate()))) {
					FcDayStart startDay = fcDayStartService.getDayStartDate(closeVO.getNetCode());
					if(startDay != null) {
						if (closeVO.getLastCloseDate().compareTo(startDay.getStartDate()) == 0) {//是否已日初
							throw new ParameterException(CloseMessageEnum.FIN_RES_CLOSE_NET_DATE_ERROR);
						}
					}
					checkFlag = true;
				}
			}
		}
		if (!checkFlag) {//结转日期和本次日结开始日期不一致，需重新进入日结加载网点日结日期
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_DATE_ERROR);
		}
		closeVO.setCloseDate(carry.getVoucherEndDate());
		closeVO.setLastCloseDate(DateUtils.addDay(carry.getVoucherStartDate(), -1));
		Date startDate = carry.getVoucherStartDate();
		FcCarry mcarry = carryService.queryNoReverseFcCarries(closeVO.getNetCode(),
				CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());
		if (mcarry != null) {
			//上次日结的开始日期比已月结的结束日期小，说明日结后已做了月结了
			Date endDate2 = mcarry.getVoucherEndDate();//月结结束日期
			if (endDate2 != null && startDate.compareTo(endDate2) <= 0) {
				throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_MONTH_CARRY);
			}
		}
	}
	/**
	 * 功能介绍 : 日结过程调用。<br>
	 * <b>日期：</b>2017年7月11日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public void execPoolReverseDay(FcDayCloseDTO closeVO, ICurrentUser user,boolean isPool) {
		beforeRePoolDayCloseCheck(closeVO);
		overService.exceRePoolDayClose(closeVO, user,isPool);
	}
	
	/**
	 * 功能介绍 : 反日结前检查。<br>
	 * <b>日期：</b>2017年7月14日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void beforeRePoolDayCloseCheck(FcDayCloseDTO closeVO) {
		// 取结转记录中最近一条结转数据，比较结转结束日期和日结日期
		FcCarry carry = carryService.queryNoReverseFcCarries(closeVO.getNetCode(),
				CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get());
		if (carry == null) {
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_POOLNET_NO_CARRY);
        }
		boolean checkFlag = false;
		if (carry != null) {
			Date voucherEndDate = carry.getVoucherEndDate();// 上次结转结束日期
			if (voucherEndDate != null) {
				// 上次结转日期和本次日结开始日期相同，则可以进行日结操作
				if (DateUtils.formatDate(voucherEndDate).equals(DateUtils.formatDate(closeVO.getLastCloseDate()))) {
					checkFlag = true;
				}
			}
		}
		if (!checkFlag) {//结转日期和本次日结开始日期不一致，需重新进入日结加载网点日结日期
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_POOLNET_DATE_ERROR);
		}
		closeVO.setCloseDate(carry.getVoucherEndDate());
		closeVO.setLastCloseDate(DateUtils.addDay(carry.getVoucherStartDate(), -1));
	}
 	/**
 	 * 功能介绍 : 月结前检查。<br>
 	 * <b>日期：</b>2017年7月17日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
 	public Map<String, Object> monthCloseCheck(FcMonthCloseDTO dayClose){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<CldBaseCheckInfoBean> listCheck = this.checkMonthControl(dayClose);
        List<CldBaseCheckInfoBean> listNCCheck = this.checkMonthNoControl(dayClose);
        returnMap.put("checkList", listCheck);
        returnMap.put("unCheckList", listNCCheck);
        
        if (listCheck == null || listCheck.isEmpty()) {
        	returnMap.put("success", "0");//可以执行日结程序
        } else {
        	returnMap.put("success", "1");
		}
 		return returnMap;
 	}
 	
 	/**
 	 * 功能介绍 : 月结检查控制。<br>
 	 * <b>日期：</b>2017年7月17日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
 	private List<CldBaseCheckInfoBean> checkMonthControl(FcMonthCloseDTO closeDTO) {
        int serNo = 1;
        List<CldBaseCheckInfoBean> rt = new ArrayList<CldBaseCheckInfoBean>();
        FcSetMonth months = setMonthService.getNetSetMonth(closeDTO.getNetCode(), closeDTO.getYearMonth2());
        Date closeDate = getDayCloseFinalDate(closeDTO.getNetCode()).getCloseDate();
        boolean flag = false;
        
        String overParam = configService.getConfig(ConfigConstant.FCL, "month_over_type").getValue();
        if (StringUtils.isEmpty(overParam)) {
        	overParam = "1";
        }
        monthCloseBefore(closeDTO);
        if ("1".equals(overParam)) {//自然月
            if (closeDTO.getEndDate().compareTo(closeDate) > 0) {
            	flag = true;
            }
        } else {
            if (months.getDateEnd() != null && months.getDateEnd().compareTo(closeDate) > 0) {
                flag = true;
            }
        }
        if (flag) {
            CldBaseCheckInfoBean acfd = new CldBaseCheckInfoBean();
            acfd.setCheckNumber(serNo++);
            acfd.setCheckType(CloseConstant.ClsMonthCheck.NO_DAY_CLOSE.getValue());
            acfd.setCheckMessage(CloseConstant.ClsMonthCheck.NO_DAY_CLOSE.getName());
            rt.add(acfd);
        }
        
        return rt;
    }
 	
 	/**
 	 * 功能介绍 : 月结检查不控制内容。<br>
 	 * <b>日期：</b>2017年7月17日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
 	private List<CldBaseCheckInfoBean> checkMonthNoControl(FcMonthCloseDTO closeDTO) {
        List<CldBaseCheckInfoBean> rt = new ArrayList<CldBaseCheckInfoBean>();
        int serNo = 1;
        //是否存在未入账的手续费
        if (this.accrueService.queryNoTallyChargeAccrue(closeDTO)) {
            CldBaseCheckInfoBean dep = new CldBaseCheckInfoBean();
            dep.setCheckNumber(serNo++);
            dep.setCheckType(CloseConstant.ClsMonthCheck.HANDING_CHARGE.getValue());
            dep.setCheckMessage(CloseConstant.ClsMonthCheck.HANDING_CHARGE.getName());
            rt.add(dep);
        }
        //损益类科目余额是否为0
        if (overService.isIncomBlanceZero(closeDTO)) {
            CldBaseCheckInfoBean acfd = new CldBaseCheckInfoBean();
            acfd.setCheckNumber(serNo++);
            acfd.setCheckType(CloseConstant.ClsMonthCheck.PCA_BALANCE_CHECK.getValue());
            acfd.setCheckMessage(CloseConstant.ClsMonthCheck.PCA_BALANCE_CHECK.getName());
            rt.add(acfd);
        }
        return rt;
    }
 	
 	/**
 	 * 功能介绍 : 月结前区间检查。<br>
 	 * <b>日期：</b>2017年7月18日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
 	private void monthCloseBefore(FcMonthCloseDTO closeDTO) {
 		//获取已完成月结日期
 		FcDayClose dayClose = getMonthCloseDate(closeDTO.getNetCode());
 		Date overMonth = dayClose.getCloseDate();
 		//页面的月结月份日期
 		if (closeDTO.getYearMonth2().compareTo(DateUtils.formatDate(overMonth, "yyyyMM")) <= 0) {
        	throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_ALREADY_CARRY);
        }else if(closeDTO.getYearMonth2().compareTo(DateUtils.formatDate(overMonth, "yyyyMM")) >= 2){
        	throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_SUPERALREADY_CARRY);
        }
        String overParam = configService.getConfig(ConfigConstant.FCL, "month_over_type").getValue();
        if (StringUtils.isEmpty(overParam)) {
        	overParam = "1";
        }
        
        Date month_End = null;
        Date startDate = null;
        if ("1".equals(overParam)) {//自然月
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(overMonth);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            startDate = calendar.getTime();//月份第一天
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            month_End = calendar.getTime();//月末最后一天
        } else {//区间
        	FcSetMonth months = setMonthService.getNetSetMonth(closeDTO.getNetCode(), closeDTO.getYearMonth2());
            if (months == null) {
            	throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NO_CARRY_SET);
            }
            startDate = months.getDateStart();//月结开始日期
            month_End = months.getDateEnd();//月结结束日期
        }
        closeDTO.setCloseDate(startDate);//月结开始日期
        closeDTO.setEndDate(month_End);//月结结束日期
    }
 	
 	/**
 	 * 功能介绍 : 日结过程调用。<br>
 	 * <b>日期：</b>2017年7月11日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	public void doMonthClose(FcMonthCloseDTO closeDTO, ICurrentUser user) {
		//月结前检查控制
		monthCloseBefore(closeDTO);
		
		overService.exceMonthClose(closeDTO, user);
	}
	
	/**
 	 * 功能介绍 : 日结过程调用。<br>
 	 * <b>日期：</b>2017年7月11日<br>
 	 * @version 1.0
 	 * @author liyh 
 	 */
	public void execReverseMonth(FcMonthCloseDTO closeDTO, ICurrentUser user) {
		//反月结检查，是否存在反月结信息
        FcCarry fcCarry = carryService.queryNoReverseFcCarries(closeDTO.getNetCode(),
				CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());//月结记录
        if (fcCarry == null) {
        	throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_NO_MCARRY);
        }
		
		overService.exceReMonthClose(closeDTO, user);
	}
	
	
}
