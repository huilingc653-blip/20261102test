package com.bytter.check.dao;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCheckInitVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 银行中心余额初始表
* @date 2017-05-31 11:15:24
* @version 1.0
*
*/
@Repository
public class FcCheckInitDao extends BaseDao<FcCheckInit,String>{
	
	/**
	 * 获取已对账过的银行账号。。不能再初始化
	 * @return
	 */
	public List<FcBankBillVo> listFcBankBillVo(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select g.bank_acc as bankAcc from fc_bank_bill g where  g.compare_sign ='1' ");
		return listBySQLAliasToBean(sql.toString(),null,FcBankBillVo.class);
	}
	/**
	 * 功能介绍:查询是否初始化余额<br>
	 */
	public FcCheckInit getListByBankAcc(String bankAcc,String curid,String checkType) {
		Map<String,Object> val = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from FcCheckInit a where 1 = 1");
		if(!EmptyUtils.isEmpty(bankAcc)){
			sb.append(" and bankAcc = :bankAcc");
			val.put("bankAcc", bankAcc);
		}
		if(!EmptyUtils.isEmpty(curid)){
			sb.append(" and curId = :curid");
			val.put("curid", curid);
		}
		if(!EmptyUtils.isEmpty(checkType)){
			sb.append(" and checkType = :checkType");
			val.put("checkType", checkType);
		}
		List<FcCheckInit> list = createQuery(sb.toString(), val).list();
		if(!EmptyUtils.isEmptyList(list)){
        	return list.get(0);
        }
        return null;
	}
	/**
	 * 功能介绍:查询是否初始化余额<br>
	 */
	public List<FcCheckInit> getListByBankAcc(List<String> bankAcc,String checkType) {
		Map<String,Object> val = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" from FcCheckInit a where 1 = 1");
		if(!EmptyUtils.isEmpty(bankAcc)){
			sb.append(" and bankAcc in :bankAcc");
			val.put("bankAcc", bankAcc);
		}
		if(!EmptyUtils.isEmpty(checkType)){
			sb.append(" and checkType = :checkType");
			val.put("checkType", checkType);
		}
		List<FcCheckInit> list = createQuery(sb.toString(), val).list();
		return list;
	}
	
	/**
	 * <b>功能介绍：</b>通过账号获取账户余额初始化信息<br>
	 * <b>日期：</b>2017年9月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param account
	 * @return
	 */
	public FcCheckInit getCheckInitByAccount(String bankAcc, String account,String checkType, String curId) {
        String hql = "from FcCheckInit fpkid where fpkid.account= :account and fpkid.bankAcc= :bankAcc and checkType = :checkType and fpkid.curId = :curId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankAcc);
        params.put("account", account);
		params.put("checkType",checkType);
        params.put("curId", curId);
		return getUnique(hql, params);
    }
	
	/**
	 * <b>功能介绍：</b>保存账户结转日期<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param account
	 * @param transDate
	 */
	public void updateCheckInitCarryDate(String bankAcc, String account, Date transDate,String checkType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_check_init i set i.carry_date = to_date(:transDate,'yyyy-MM-dd') where i.account = :account and i.bank_acc = :bankAcc and check_type = :checkType ");
		params.put("account", account);
        params.put("bankAcc", bankAcc);
        params.put("transDate", DateUtils.formatDate(transDate));
		params.put("checkType", checkType);
        createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>反结转更新<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param balAdjust
	 */
	public void updateRedoAdjustOver(FcBankBalanceAdjust balAdjust) {
		 String sql = "update fc_check_init f set f.carry_date = :carryDate where f.bank_acc = :bankAcc and f.account = :account and f.cur_id = :curCode ";
		 Map<String, Object> params = new HashMap<String, Object>();
		 params.put("carryDate", DateUtils.addDay(balAdjust.getBeginDate(), -1));
	     params.put("bankAcc", balAdjust.getBankAcc());
	     params.put("account", balAdjust.getAccount());
	     params.put("curCode", balAdjust.getCurCode());
	     createSQLQuery(sql.toString(), params).executeUpdate();
	}
	/**
	 * 获取所有ERP映射表映射银行账号
	 * @return
	 */
	public List<FcCheckInitVo> listByErpBasicData(){
		StringBuilder sql = new StringBuilder();
		sql.append(" select erp.erp_code as erpCode,erp.erp_name as erpName,erp.erp_reverse as erpReverse,erp.bytter_code as bankAcc ");
		sql.append(" from erp_basic_data erp ");
		sql.append(" where  erp.data_type_code = 'BT_BANK_ACC' and erp.type_code = 'rsm' ");
		return this.listBySQLAliasToBean(sql.toString(),null,FcCheckInitVo.class);
	}
//	//TODO 整体改造
//	public DataTablesOutput queryCheckInitList(DataTablesInput dataTablesInput,List<String> corpAuth,String bankAcc, String curId,String checkType){
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select acc.bank_name as bankName, acc.bank_acc as bankAcc, acc.acc_name as accName,");
//		sql.append("  erp.erp_code as erpCode,erp.erp_name as erpName,erp.erp_reverse as erpReverse, cur.cur_id as curId,");
//		sql.append("  it.checkDate,it.bankBalance,it.rmk,acc.corp_id as corpId, ");
//		sql.append(" it.blnBalance,it.id, bc.cur_name as curName ");
//		sql.append(" from bt_bank_acc acc");
//		sql.append(" inner join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
//		sql.append(" inner join bt_currency bc on bc.id = cur.cur_id ");
//		sql.append(" inner join erp_basic_data erp on acc.bank_acc = erp.bytter_code ");
//		sql.append(" and erp.data_type_code = 'BT_BANK_ACC' and erp.type_code = 'rsm' and (cur.cur_id = erp.erp_reverse or erp.erp_reverse is null) ");
//		sql.append(" left join ( select fc.check_date as checkDate,fc.bank_balance as bankBalance,fc.rmk as rmk,");
//		sql.append(" fc.bln_balance as blnBalance,fc.id,fc.bank_acc as bankAcc,fc.cur_id as curId  from fc_check_init fc ");
//		sql.append(" where fc.check_type = "+checkType+" ) it ");
//		sql.append(" on acc.bank_acc = it.bankAcc and cur.cur_id = it.curId ");
//		sql.append(" where acc.valid_sign = '1' ");
//		if(corpAuth != null) {
//			sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_id"));
//		}
//		if(EmptyUtils.isNotEmpty(bankAcc) ||
//				(dataTablesInput.getFormdata().get("likes_bankAccount") != null && dataTablesInput.getFormdata().get("likes_bankAccount") != "")) {
//			if(EmptyUtils.isNotEmpty(bankAcc)) {
//				sql.append("  and acc.bank_acc like'%" + bankAcc + "%' ");
//			}else{
//				sql.append("  and acc.bank_acc like'%" + dataTablesInput.getFormdata().get("likes_bankAccount") + "%' ");
//			}
//			dataTablesInput.getFormdata().remove("likes_bankAccount");
//		}
//		if(dataTablesInput.getFormdata().get("likes_bankAccountName") != null && dataTablesInput.getFormdata().get("likes_bankAccountName") != "") {
//			sql.append("  and acc.acc_name like'%"+dataTablesInput.getFormdata().get("likes_bankAccountName")+"%' ");
//			dataTablesInput.getFormdata().remove("likes_bankAccountName");
//		}
//		if (EmptyUtils.isNotEmpty(curId)) {
//		    sql.append(" and cur.cur_id = '").append(curId).append("' ");
//        }
//		sql.append(" and exists ");
//		sql.append(" (select 1  from sys_account_grant gt ");
//		sql.append("  where gt.account_id = acc.id ");
//		sql.append(" and (can_query_balance = '1' or can_query_detail = '1' or can_transfer_accounts = '1')) ");
//
//		return getDynamicBySQL(dataTablesInput,sql.toString(),FcCheckInitVo.class,"app",false);
//	}
    /**
    * <br>
    * <b>功能：</b> 查询对账账号 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2022/1/7 0007 <br>
    */
	//TODO SYS服务提供
    public List<BtBankAccDTO> getInitBankAccsData(String corpId, String userId, String checkType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params=new HashMap<String, Object>();
        sql.append(" select distinct acc.bank_name bankName, acc.bank_acc bankAcc,accCur.cur_id as curId,acc.acc_name as accName,  ");
        sql.append(" cur.cur_name as curName  ");
        sql.append(" from bt_bank_acc acc inner join fc_check_init init on acc.bank_acc = init.bank_acc ");
        sql.append(" inner join bt_bank_acc_cur accCur on acc.id = accCur.bank_acc_id and accCur.Cur_Id = init.cur_id ");
        sql.append(" inner join bt_currency cur on accCur.cur_id=cur.id ");
        sql.append(" inner join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id");
        sql.append(" where acc.valid_sign =:validSign and acc.status=:status ");
        sql.append(" and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' and ACCOUNT_GRANT.ACCOUNT_TYPE in('2','3') ");
        sql.append(" and ACCOUNT_GRANT.USER_ID = :userId");
        if (!EmptyUtils.isEmpty(checkType)) {
            sql.append(" and init.check_type = :checkType ");
            params.put("checkType", checkType);
        }
        params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
        params.put("status", BtBankAccConstant.STATUE_CHECK);
        params.put("userId", userId);
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and acc.corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        sql.append(" order by acc.bank_acc,accCur.cur_id ");
        return listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
    }

	public Map<String,Object> getInitBankAccsDataSql(String corpId, String userId,List<String> bankaccList) {
		Map<String,Object> result = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params=new HashMap<>();
		sql.append(" select distinct acc.bank_name bankName, acc.bank_acc bankAcc,accCur.cur_id as curId,acc.acc_name as accName,  ");
		sql.append(" cur.cur_name as curName  ");
		sql.append(" from bt_bank_acc acc  ");
		sql.append(" inner join bt_bank_acc_cur accCur on acc.id = accCur.bank_acc_id ");
		sql.append(" inner join bt_currency cur on accCur.cur_id=cur.id ");
		sql.append(" inner join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id");
		sql.append(" where acc.valid_sign =:validSign and acc.status=:status ");
		sql.append(" and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' and ACCOUNT_GRANT.ACCOUNT_TYPE in('2','3') ");
		sql.append(" and ACCOUNT_GRANT.USER_ID = :userId");
		if (!EmptyUtils.isEmpty(bankaccList)) {
			sql.append(" and "+ SqlUtil.getInSql(bankaccList,"acc.id",true));
		}
		params.put("validSign", BtBankAccConstant.VALIDSIGN_1);
		params.put("status", BtBankAccConstant.STATUE_CHECK);
		params.put("userId", userId);
		if (StringUtils.isNotEmpty(corpId)) {
			sql.append(" and acc.corp_id = :corpId ");
			params.put("corpId", corpId);
		}
		sql.append(" order by acc.bank_acc,accCur.cur_id ");
		result.put("sql",sql.toString());
		result.put("map",params);
		return  result;
	}
	 
}
