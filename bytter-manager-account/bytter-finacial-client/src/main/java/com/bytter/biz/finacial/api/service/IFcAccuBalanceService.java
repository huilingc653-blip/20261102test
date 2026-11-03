package com.bytter.biz.finacial.api.service;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.biz.finacial.api.vo.FcAccuBalanceVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
* @Title: Service
* @Description: FC_ACCU_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:03:48
* @version 1.0
*
*/
public interface IFcAccuBalanceService {
	/**
	 * 查找某一天的账号对象
	 * @param date
	 * @param account
	 * @return
	 */
	public FcAccuBalanceVO getEntityByAccountAndDate(Date date, String account);
	/**
	 * 初始化余额积数
	 * @param balanceDate
	 * @param blnctrAcc
	 * @param curId
	 * @param cumu
	 */
	public void initAccuBalance(Date balanceDate,String blnctrAcc,String curId,BigDecimal cumu,String loginName);
	/**
	 * 更新账户的积数
	 * @param account
	 * @param balanceDate
	 * @param cumu
	 */
	public void updateAccountAccuBalanceByBalanceDate(String account, Date balanceDate, BigDecimal cumu);
	
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput);
	
	public FcAccuBalanceVO getFcAccuBalanceById(String id);
	
	public void save(FcAccuBalanceVO fcAccuBalance);
	
	public void saveOrUpdate(FcAccuBalanceVO fcAccuBalance);
	
	public void deleteEntity(FcAccuBalanceVO fcAccuBalance);
}
