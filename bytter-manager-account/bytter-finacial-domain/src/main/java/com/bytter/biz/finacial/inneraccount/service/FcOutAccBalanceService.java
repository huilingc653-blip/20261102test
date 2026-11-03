package com.bytter.biz.finacial.inneraccount.service;
import com.bytter.biz.finacial.inneraccount.dao.FcOutAccBalanceDao;
import com.bytter.biz.finacial.inneraccount.entity.FcOutAccBalance;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
/**
* @Title: Service
* @Description: FC_OUT_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:24:29
* @version 1.0
*
*/
@Service("fcOutAccBalanceService")
@Transactional
public class FcOutAccBalanceService extends BaseService<FcOutAccBalance,String> {
	@Autowired
	private FcOutAccBalanceDao fcOutAccBalanceDao;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(FcOutAccBalance entity){
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
 	public void save(FcOutAccBalance entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcOutAccBalance entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(FcOutAccBalance entity){
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<FcOutAccBalance, String> getEntityDao() {
		return fcOutAccBalanceDao;
	}
	/**
	 * 初始化余额
	 * @param balanceDate 余额日期
	 * @param blnctrAcc 账号
	 * @param accName 户名
	 */
	public void initAccountBalance(java.util.Date balanceDate, String blnctrAcc, String accName,String curId,String loginName){
		FcOutAccBalance fcAccBalance = new FcOutAccBalance();
		fcAccBalance.setAccount(blnctrAcc);
		fcAccBalance.setCreditCount(0);
		fcAccBalance.setCreditMoney(BigDecimal.ZERO);
		fcAccBalance.setCreditNum(0);
		fcAccBalance.setCreditPageNum(0);
		fcAccBalance.setDebitCount(0);
		fcAccBalance.setDebitMoney(BigDecimal.ZERO);
		fcAccBalance.setDebitNum(0);
		fcAccBalance.setDebitPageNum(0);
		fcAccBalance.setOptDate(balanceDate);
		fcAccBalance.setTBalance(BigDecimal.ZERO);
		fcAccBalance.setTCount(0);
		fcAccBalance.setTPageNum(0);
		fcAccBalance.setYBalance(BigDecimal.ZERO);
		fcAccBalance.setYCount(0);
		fcAccBalance.setCurCode(curId);
		fcAccBalance.setYPageNum(0);
		fcAccBalance.setCreateTime(DateUtils.getDate());
		fcAccBalance.setCreateBy(loginName);
		super.saveOrUpdate(fcAccBalance);
	}
}
