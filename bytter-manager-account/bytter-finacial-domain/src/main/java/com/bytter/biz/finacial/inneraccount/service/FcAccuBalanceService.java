package com.bytter.biz.finacial.inneraccount.service;
import com.bytter.biz.finacial.inneraccount.dao.FcAccuBalanceDao;
import com.bytter.biz.finacial.inneraccount.entity.FcAccuBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
/**
* @Title: Service
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:03:48
* @version 1.0
*
*/
@Service("fcAccuBalanceService")
@Transactional
public class FcAccuBalanceService extends BaseService<FcAccuBalance,String> {
	@Autowired
	private FcAccuBalanceDao fcAccuBalanceDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcAccuBalance entity){
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
 	public void save(FcAccuBalance entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcAccuBalance entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcAccuBalance entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcAccuBalance, String> getEntityDao() {
		return fcAccuBalanceDao;
	}
	/**
	 * 根据id 获取 当前对象实体
	 * @param id
	 * @return
	 */
	public FcAccuBalance getFcAccuBalanceById(String id){
		return fcAccuBalanceDao.getFcAccuBalanceById(id);
	}
	/**
	 * 查找某一天的账号对象
	 * @param date
	 * @param account
	 * @return
	 */
	public FcAccuBalance getEntityByAccountAndDate(Date date, String account){
		return fcAccuBalanceDao.getEntityByAccountAndDate(date,account);
	}
	/**
	 * 初始化余额积数
	 * @param balanceDate
	 * @param blnctrAcc
	 * @param curId
	 * @param cumu
	 */
	public void initAccuBalance(Date balanceDate,String blnctrAcc,String curId,BigDecimal cumu,String loginName) {
		FcAccuBalance fcAccuBalance = new FcAccuBalance();
		fcAccuBalance.setBalanceDate(balanceDate);
		fcAccuBalance.setAccount(blnctrAcc);
		fcAccuBalance.setCumu(cumu);
		fcAccuBalance.setAdjustCumu(BigDecimal.ZERO);
		fcAccuBalance.setAgreedDepositSign("0");
		fcAccuBalance.setInterestSign("0");
		fcAccuBalance.setCurCode(curId);
		fcAccuBalance.setCreateTime(DateUtils.getDate());
		fcAccuBalance.setCreateBy(loginName);
		super.saveOrUpdate(fcAccuBalance);
	}
	/**
	 * 更新账户的积数
	 * @param account
	 * @param balanceDate
	 * @param cumu
	 */
	public void updateAccountAccuBalanceByBalanceDate(String account, Date balanceDate, BigDecimal cumu) {
		fcAccuBalanceDao.updateAccountAccuBalanceByBalanceDate(account, balanceDate, cumu);
	}
}
