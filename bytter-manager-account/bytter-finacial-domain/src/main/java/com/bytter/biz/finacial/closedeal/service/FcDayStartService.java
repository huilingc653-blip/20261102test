package com.bytter.biz.finacial.closedeal.service;
import com.bytter.biz.finacial.closedeal.dao.FcDayStartDao;
import com.bytter.biz.finacial.closedeal.entity.FcCarry;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.entity.FcDayStart;
import com.bytter.biz.finacial.closedeal.vo.FcDayStartVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
/**
* @Title: Service
* @Description: FC_DAY_START
* @author onlineGenerator
* @date 2017-07-06 14:51:02
* @version 1.0
*
*/
@Service("fcDayStartService")
@Transactional
public class FcDayStartService extends BaseService<FcDayStart,String> {
	@Autowired
	private FcDayStartDao fcDayStartDao;
	@Autowired
	private FcCarryService carryService;
	@Autowired
	private FcDayOverService overService;
	@Autowired
	private FcDayCloseService fcDayCloseService;
	
	
	
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcDayStart entity){
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
 	public void save(FcDayStart entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcDayStart entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcDayStart entity){
 		super.saveOrUpdate(entity);
 	}
 	
 	public BaseDao<FcDayStart, String> getEntityDao() {
		return fcDayStartDao;
	}
 	
 	/**
	 * 功能介绍 :获取网点的日初日期。<br>----------------
	 */
 	@Transactional(readOnly = true)
	public FcDayStart getDayStartDate(String netId) {
		return fcDayStartDao.getDayStartDate(netId);
	}
	
	/**
	 * 功能介绍 : 获取网点的日初日期。<br>------------------
	 */
	public FcDayStartVO getDayStartFinalDate(String netId) {
		FcDayStart dayStart = getDayStartDate(netId);
		FcDayStartVO startVO = new FcDayStartVO();
		if (null == dayStart) {//网点未初始化
			//throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NO_INIT_NET);
			FcDayClose close = fcDayCloseService.getDayCloseDate(netId);
			if(close.getLastCloseDate() == null){
				throw new ParameterException(CloseMessageEnum.FIN_START_NET_NOT_CLOSE_NOT_DATE);
			}
			Date LastStartDate = DateUtils.addDay(close.getLastCloseDate(),-1);
			startVO.setLastStartDate(LastStartDate);
			startVO.setStartDate(close.getLastCloseDate());
		}else {
			BeanUtils.copyProperties(dayStart, startVO);
		}
		return startVO;
	}
	
 	/**
 	 * 功能介绍 : 日初处理。<br>
 	 */
	public void doDayStart(FcDayStartVO startVO, ICurrentUser user) {
        //查询有没有反日初
		boolean beforeCheck = beforeDayStartCheck(startVO, CloseConstant.CarryType.CARRY_TYPE_DAY_START.get());
		if (!beforeCheck) {//结转日期和本次日结开始日期不一致，需重新进入日结加载网点日结日期
			throw new ParameterException(CloseMessageEnum.FIN_CLOSE_NET_DATE_ERROR);
		}
		//日结处理
		overService.disposeAgreeAccu(startVO, user);
	}
	
	
	/**
	 * 功能介绍 : 日初前检查。------------
	 */
	public boolean beforeDayStartCheck(FcDayStartVO closeVO, String overType) {
		// 取结转记录中最近一条结转数据，比较结转结束日期和日结日期
		FcCarry carry = carryService.queryNoReverseFcCarries(closeVO.getNetCode(), overType);
		if (carry != null) {
			Date voucherEndDate = carry.getVoucherEndDate();// 上次结转结束日期
			if (voucherEndDate != null) {
				// 上次结转日期和本次日结开始日期相同，则可以进行日结操作
				if (DateUtils.formatDate(voucherEndDate).equals(DateUtils.formatDate(closeVO.getLastStartDate()))) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * 功能介绍 : 反日初过程调用。<br> ----------------
	 */
	public void execReverseDay(FcDayStartVO startVO, ICurrentUser user) {
		beforeReDayStartCheck(startVO);
		overService.ReverseDayStart(startVO, user);
	}
	/**
	 * 功能介绍 : 反日初前检查。<br>----------------
	 */
	public void beforeReDayStartCheck(FcDayStartVO startVO) {
		// 取结转记录中最近一条结转数据，比较结转结束日期和日结日期
		FcCarry carry = carryService.queryNoReverseFcCarries(startVO.getNetCode(),
				CloseConstant.CarryType.CARRY_TYPE_DAY_START.get());
		if (carry == null) {
			throw new ParameterException(CloseMessageEnum.FIN_START_NET_NO_CARRY);
		}
		boolean checkFlag = false;
		if (carry != null) {
			Date voucherEndDate = carry.getVoucherEndDate();// 上次结转结束日期
			if (voucherEndDate != null) {
				// 上次结转日期和本次日初开始日期相同，则可以进行日初操作
				if (voucherEndDate.compareTo(startVO.getLastStartDate()) == 0) {
					checkFlag = true;
				}
			}
		}
		if (!checkFlag) {//结转日期和本次日初开始日期不一致，需重新进入日初加载网点日初日期
			throw new ParameterException(CloseMessageEnum.FIN_START_NET_DATE_ERROR);
		}
		startVO.setStartDate(carry.getVoucherEndDate());
		startVO.setLastStartDate(DateUtils.addDay(carry.getVoucherStartDate(), -1));
	}
	
	
}
