package com.bytter.check.dao;
import com.bytter.check.constant.CheckConstant;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.vo.AdjustDetailVO;
import com.bytter.check.vo.FcBankBalanceAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 对账余额调节表
* @author onlineGenerator
* @date 2017-06-02 15:29:08
* @version 1.0
*
*/
@Repository
public class FcBankBalanceAdjustDao extends BaseDao<FcBankBalanceAdjust,String>{
	/**
	 * <b>功能介绍：</b>余额调节表列表数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listFcBankBalanceAdjust(DataTablesInput input,List<String> accS) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct a.id,a.check_type,a.bank_acc,a.account,a.cur_code,a.bank_init_balance,a.bln_init_balance,");
		sql.append("a.bank_adj_bal,a.bln_adj_balance,a.adjust_date,a.begin_date,a.end_date,a.adj_code,a.status,");
		sql.append("a.readj_date,a.readj_user,a.update_by,a.update_time,a.create_by,a.create_time,a.cur_code as curCode ");
		sql.append("from FC_BANK_BALANCE_ADJUST a ");
		sql.append("where (a.status is null or a.status <> -2) ");
		sql.append(" and ").append(getSQLIn(accS, 900, "a.bank_acc"));
		return getDynamicBySQL(input, sql.toString(), FcBankBalanceAdjustVO.class, "", false);
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adIds
	 * @return
	 */
	public List<FcBankBalanceAdjust> getFcBankBalanceAdjust(String[] adIds){
		StringBuilder hql = new StringBuilder();
		hql.append("from FcBankBalanceAdjust a where a.id in (:ids) and (a.status is null or a.status <> -2) order by a.endDate desc ");
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("ids", adIds);
    	return listByHQL(hql.toString(), params);
	}
	/**
	* <br>
	* <b>功能：</b> 查询是否存在选择结转记录之后的结转记录 <br>
	* <b>作者：</b>liyh<br>
	* <b>日期：</b> 2020/10/20 0020 <br>
	*/
    public List<FcBankBalanceAdjustVO> queryBehindAdjusts(String[] adIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct a.id,a.check_type,a.bank_acc,a.account,a.cur_code,a.bank_init_balance,a.bln_init_balance,");
        sql.append("a.bank_adj_bal,a.bln_adj_balance,a.adjust_date,a.begin_date,a.end_date,a.adj_code,a.status ");
        sql.append(" from fc_bank_balance_adjust a where ")
            .append(" check_type = '1' and a.id not in (:ids) and  exists (select 1 from ( ")
            .append(" select b.bank_acc,b.account,b.cur_code,max(end_date) as end_date from fc_bank_balance_adjust b ")
            .append(" where (b.status <> -2 or b.status is null) and b.id in (:ids) group by b.bank_acc,b.account,b.cur_code ) ")
            .append(" c where a.bank_acc = c.bank_acc and a.account = c.account and a.cur_code = c.cur_code and a.end_date > c.end_date ")
            .append(" ) and (a.status <> -2 or a.status is null)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", adIds);
        return listBySQLAliasToBean(sql.toString(), params, FcBankBalanceAdjustVO.class);
    }
	
	/**
	 * <b>功能介绍：</b>删除余额调节表<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param adIds
	 */
	public int doDeleteBalanceAdjust(String[] adIds,String readjUser) {
		List<String> idsIn = new ArrayList<String>();
		for (String id : adIds) {
			idsIn.add(id);
		}
		String sql = "update fc_bank_balance_adjust a set a.status = :status, a.readj_date = :readjDdate,a.readj_user = :readjUser ";
		sql += " where (a.status is null or a.status <> :status) and " + getSQLIn(idsIn, 900, "a.id");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", CommonConstant.STATUE_DELETE);
		params.put("readjDdate", DateUtils.getDate());
		params.put("readjUser", readjUser);//反结转用户
		return createSQLQuery(sql,params).executeUpdate();
	}
    /**
     * <b>功能介绍：</b>删除余额调节表<br>
     * <b>日期：</b>2017年10月18日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param adIds
     */
    public void doDeleteBalanceAdjustDtls(String[] adIds,String readjUser) {
        List<String> idsIn = new ArrayList<String>();
        for (String id : adIds) {
            idsIn.add(id);
        }
        String sqlDtl = "update fc_bank_balance_adjust_detail d set d.status = :status where 1 = 1 and " + getSQLIn(idsIn, 900, "d.ajust_id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", CommonConstant.STATUE_DELETE);
        params.put("readjDdate", DateUtils.getDate());
        params.put("readjUser", readjUser);//反结转用户
        createSQLQuery(sqlDtl,params).executeUpdate();
    }
	
	/**
	 * <b>功能介绍：</b>查询余额调节表<br>
	 * <b>日期：</b>2017年10月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @return
	 */
	public FcBankBalanceAdjustVO getFcBankBalanceAdjustById(String id){
		StringBuilder sql  = new StringBuilder();
		sql.append("select a.id,a.check_type,a.account,a.bank_acc,a.cur_code,a.bank_init_balance,a.bln_init_balance,a.bank_adj_bal,a.bln_adj_balance,a.adjust_date,");
		sql.append("a.begin_date,a.end_date,a.adj_code,a.status,a.readj_date,a.readj_user");
		sql.append(" from fc_bank_balance_adjust a ");
		sql.append("where a.id = :id ");
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("id", id);
    	return getBySQLAliasToBean(sql.toString(), params, FcBankBalanceAdjustVO.class);
	}
	
	/**
	 * <b>功能介绍：</b>查询余额调节表明细<br>
	 * <b>日期：</b>2017年10月19日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param dataType
	 * @param queryType
	 * @param paramType
	 * @param adjId
	 * @return
	 */
	public List<AdjustDetailVO> queryAdjustDetaiData(String dataType, String queryType, String paramType, String adjId){
		StringBuilder sql = new StringBuilder();
		String sqlAnd = "";
		sql.append("select d.tally_date as tallyDate,d.summary_content as summaryContent,d.bill_no as billNo,d.voucher_no voucherNo,");
		if(CheckConstant.CTR_PAY_BANK_NO_PAY.equals(queryType)||CheckConstant.BANK_REC_CTR_NO_REC.equals(queryType)){
			if(CheckConstant.CTR_DATA.equals(paramType)){//银行-中心对账
				sql.append("d.debit_money as money,");
				sqlAnd = "d.debit_money";
			}else{
				sql.append("d.credit_money as money,");
				sqlAnd = "d.credit_money";
			}
		}
		if(CheckConstant.CTR_REC_BANK_NO_REC.equals(queryType)||CheckConstant.BANK_PAY_CTR_NO_PAY.equals(queryType)){
			if(CheckConstant.CTR_DATA.equals(paramType)){//银行-中心对账
				sql.append("d.credit_money as money,");
				sqlAnd = "d.credit_money";
			}else{
				sql.append("d.debit_money as money,");
				sqlAnd = "d.debit_money";
			}
		}
		sql.append("' ' as checkRecord from fc_bank_balance_adjust_detail d ");
		sql.append("where d.param_type = :dataType and d.ajust_id = :ajustId ");
		sql.append(" and " + sqlAnd + " <> 0 ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ajustId", adjId);
		params.put("dataType", dataType);
		return listBySQLAliasToBean(sql.toString(), params, AdjustDetailVO.class);
	}
	
	/**
	 * 判断账号是否进行了余额调节表
	 */
	public boolean checkIsAdjust(String account , String bankAcc){
		StringBuilder sql  = new StringBuilder();
		sql.append(" select a.id,a.check_type,a.account,a.bank_acc,a.cur_code , a.status ");
		sql.append(" from fc_bank_balance_adjust a where a.bank_acc =:bankAcc and a.account =:account and a.status != -2");
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("account", account);
    	params.put("bankAcc", bankAcc);
    	List<FcBankBalanceAdjust> list = listBySQLAliasToBean(sql.toString(), params, FcBankBalanceAdjust.class);
    	if(CollectionUtils.isEmpty(list)){
    		return false;
    	}else{
    		return true;
    	}
	}
}
