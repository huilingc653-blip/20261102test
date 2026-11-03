package com.bytter.biz.finacial.api.service;
import com.bytter.biz.finacial.api.vo.FcOutAccBalanceVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
* @Title: Service
* @Description: FC_OUT_ACC_BALANCE
* @author onlineGenerator
* @date 2017-06-07 10:24:29
* @version 1.0
*
*/
public interface IFcOutAccBalanceService  {
	/**
	 * 初始化余额
	 * @param balanceDate 余额日期
	 * @param blnctrAcc 账号
	 * @param accName 户名
	 */
	public void initAccountBalance(java.util.Date balanceDate, String blnctrAcc, String accName,String curId,String loginName);
	
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput);
	
	public FcOutAccBalanceVO getFcOutAccBalanceById(String id);
	
	public void save(FcOutAccBalanceVO fcOutAccBalance);
	
	public void saveOrUpdate(FcOutAccBalanceVO fcOutAccBalance);
	
	public void deleteEntity(FcOutAccBalanceVO fcOutAccBalance);
}
