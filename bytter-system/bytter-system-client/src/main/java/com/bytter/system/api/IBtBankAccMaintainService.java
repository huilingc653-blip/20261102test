package com.bytter.system.api;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
public interface IBtBankAccMaintainService {
	
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user);
	/**
	 * 资金池查询
	 * @param dataTablesInput
	 * @param user
	 * @return
	 */
	public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, ICurrentUser user);
	public DataTablesOutput dataTableForCounterAcc(DataTablesInput dataTablesInput, ICurrentUser user, String corpId,String id);
	/**
	 * 资金池内部账号设置
	 * @param dataTablesInput
	 * @param user
	 * @param corpId
	 * @return
	 */
	public DataTablesOutput dataTableForCounterPoolAcc(DataTablesInput dataTablesInput, ICurrentUser user, String corpId);
	/**
	 * 关联内部账号
	 *
	 * @param id
	 * @param blnctrAcc
	 * @param flag
	 * @param curId
	 */
	public void toAdd(String id, String blnctrAcc, String flag, String curId);
	/**
	 * 取消关联内部账号
	 *
	 * @param id
	 * @param flag
	 * @param curId
	 */
	public void toDel(String id, String flag, String curId);
}
