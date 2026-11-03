package com.bytter.biz.finacial.inneraccount.service;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.inneraccount.dao.FcAccBalanceDao;
import com.bytter.biz.finacial.inneraccount.entity.FcAccBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Title: Service
* @Description: FC_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:04:02
* @version 1.0
*
*/
@Service("fcAccBalanceService")
@Transactional
public class FcAccBalanceService extends BaseService<FcAccBalance,String> {
	@Autowired
	private FcAccBalanceDao fcAccBalanceDao;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	/**
	 * 查找
	 * @return
	 */
	public FcAccBalance getId(String id){
		return fcAccBalanceDao.get(id);
	}
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcAccBalance entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcAccBalance entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setNetId(fcAccDTO.getNetId());
		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccBalance entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcAccBalance entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setNetId(fcAccDTO.getNetId());
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcAccBalance, String> getEntityDao() {
		return fcAccBalanceDao;
	}
	/**
	 * 查找某一天的账号对象
	 * @param date
	 * @param account
	 * @return
	 */
	public FcAccBalance getEntityByAccountAndDate(Date date, String account){
 		return fcAccBalanceDao.getEntityByAccountAndDate(date,account);
	}
	/**
	 * 初始化余额
	 * @param balanceDate 余额日期
	 * @param blnctrAcc 账号
	 * @param accName 户名
	 */
	public void initAccountBalance(Date balanceDate, String blnctrAcc, String accName,String curId,String loginName){
		FcAccBalance balance = getEntityByAccountAndDate(balanceDate,blnctrAcc);
		if(balance != null) {
			throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_BALANCE_NOT_EXISTS,
					blnctrAcc,DateUtils.getDateStr(balanceDate,"yyyy-MM-dd"));
		}
		FcAccBalance fcAccBalance = new FcAccBalance();
		fcAccBalance.setBalanceDate(balanceDate);
		fcAccBalance.setAccount(blnctrAcc);
		fcAccBalance.setAccName(accName);
		fcAccBalance.setBalance(BigDecimal.ZERO);
		fcAccBalance.setCnt(0);
		fcAccBalance.setCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setRushCnt(0);
		fcAccBalance.setRushCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setRushDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setTallyDaySign("0");
		fcAccBalance.setTrCnt(0);
		fcAccBalance.setCurId(curId);
		fcAccBalance.setTrCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setTrDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setYesterBalance(BigDecimal.ZERO);
		fcAccBalance.setDebitCnt(0);
		fcAccBalance.setCreditCnt(0);
		fcAccBalance.setCreateTime(DateUtils.getDate());
		fcAccBalance.setCreateBy(loginName);
		super.saveOrUpdate(fcAccBalance);
	}
	public void updateAccBalanceByInitAcc(Date date, String account, BigDecimal balance) {
		FcAccBalance fcAccBalance = fcAccBalanceDao.getEntityByAccountAndDate(date,account);
		if(fcAccBalance != null) {
			fcAccBalanceDao.updateAccBalanceByInitAcc(date, account, fcAccBalance.getBalance().add(balance));
		}
	}
	/**
	 * 判断月账单是否可以核对
	 *
	 * @param date 月账单
	 * @return true 存在，false 不存在
	 */
	public boolean existBalance(String date) {
		return fcAccBalanceDao.existBalance(date);
	}
	/**
	 * 根据内部账号分组查询笔数
	 * @param account
	 * @return
	 */
	public FcAccBalanceVO getGroupByAccount(String account) {
		return fcAccBalanceDao.getGroupByAccount(account);
	}
	/**
	 * 按日期分组余额
	 * @param dates
	 * @return
	 */
	public List<FcAccBalanceVO> getGroupByDates(Date[] dates) {
		return fcAccBalanceDao.getGroupByDates(dates);
	}

	public Map<String, FcAccBalanceVO> getGroupByAccount(List<String> accountList) {
		if (CollectionUtil.isEmpty(accountList)) {
			return Maps.newHashMap();
		}
		return fcAccBalanceDao.getGroupByAccount(accountList);
	}

	public FcAccBalance getEntityByParams(Date balanceDate, String account, String curId) {
		return fcAccBalanceDao.getEntityByParams(balanceDate, account, curId);
	}
}
