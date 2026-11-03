package com.bytter.check.dao;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.CentralityDataVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 中心数据管理DAO
* @author onlineGenerator
* @date 2017-05-31 15:28:08
* @version 1.0
*
*/
@Repository
public class CentralityDataManageDao extends BaseDao<FcCompareAcc,String>{
	/**中心对账单录入*/
	public DataTablesOutput dataTable(DataTablesInput dataTablesInput,List<String> accList){
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id as id,a.voucher_No as voucherNo,a.comp_Acc as compAcc,");
		sql.append(" a.tally_Date as tallyDate,a.debit_money as debitMoney,a.credit_money as creditMoney,");
		sql.append(" a.bill_no as billNo,redblue_sign as redblueSign,a.compare_Sign as compareSign "); 
		sql.append(" from fc_compare_acc a ");
		sql.append(" where 1=1 ");
		sql.append(" and ").append(getSQLIn(accList, 900, "a.comp_acc"));
		return getDynamicBySQL(dataTablesInput,sql.toString(), FcCompareAccVO.class,"a",false);
	}
	/**
	 * 
	 * 功能介绍 : 工具方法，将List<Map> 转化成where 条件<br>	
	 * @param param
	 * @return
	 * <b>日期：</b>2017-6-6<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	public String mapToWhere(List<Map<String,String>> param,String prefix){
		StringBuilder where = new StringBuilder();
			for (Map<String, String> map : param) {
				if(EmptyUtils.isBlank(where.toString())){
					 where.append(" ");
				 }else{
					 where.append(" or ");
				 }
				
				 StringBuilder sb = new StringBuilder();
				 for (Map.Entry<String, String> entry : map.entrySet()) {
					 if(!EmptyUtils.isEmpty(sb.toString())){
						 sb.append(" and ");
					 }
					 if(!EmptyUtils.isBlank(prefix)){
						 sb.append(prefix+".");
					 }
					 sb.append(entry.getKey()+" = '"+entry.getValue()+"'");
				 }
				 where.append(" ( ");
				 where.append(sb.toString());
				 where.append(" ) ");
			}
		return "("+where.toString()+")";
	}
	
	/**
	 *
	 * 功能介绍 : <br>
	 * @param params
	 * <b>日期：</b>2017-6-7<br> <br>
	 * @version 1.0
	 * @author liusy
	 */
	public List<FcCompareAcc> queryCompareAcc(Map<String,String> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" select accbook.order_no as orderNo, accbook.voucher_no as voucherNo, accbook.tally_date as tallyDate, accbook.interest_start_date as interestStartDate, dtl_info.summary_content as summaryContent,");
		sql.append(" dtl_info.account as compAcc, dtl_info.debit_money as debitMoney, dtl_info.credit_money as creditMoney, dtl_info.balance as balance, accbook.redblue_sign as redblueSign, dtl_info.line_no as lineNo,");
		sql.append(" accbook.cur_code as curId  ");
		sql.append(" from fc_accbook_blned accbook, fc_accbook_blned_dtl dtl_info, fc_acc acc");
		sql.append(" where accbook.id = dtl_info.book_id and acc.blnctr_acc = dtl_info.account ");
		sql.append(" and not exists(select 1 from FC_COMPARE_ACC comp where comp.order_no = accbook.order_no and comp.comp_acc = dtl_info.account and comp.line_no = dtl_info.line_no)");
		sql.append(" and acc.IF_COMPARE = '1' and accbook.voucher_state <> :voucherState ");//状态过滤
		if (StringUtils.isNotEmpty(params.get("corpId"))){
			sql.append(" and acc.net_id = :corpId ");
		}
		if (StringUtils.isNotEmpty(params.get("queryBeginDate")) && StringUtils.isNotEmpty(params.get("queryEndDate"))){
			sql.append(" and accbook.tally_Date >= BT_TO_DATE(:queryBeginDate,'yyyy-mm-dd') and accbook.tally_Date <= BT_TO_DATE(:queryEndDate,'yyyy-mm-dd') ");
		}
		if (StringUtils.isNotEmpty(params.get("account"))){
			sql.append(" and acc.blnctr_acc = :account ");
		}
		params.put("voucherState", "B");
		return listBySQLAliasToBean(sql.toString(), params, FcCompareAcc.class);
	}
	
	/**
	 * 
	 * 功能介绍。获取余额初始化后的账号<br>
	 * <b>日期：</b>2017年10月13日<br>
	 * @version 1.0
	 * @author zhengxn 
	 */
	public List<CentralityDataVO> getValidAccount(List<String> accs){
		StringBuilder sql = new StringBuilder();
		sql.append("select init.account,init.cur_id");
		sql.append(" from fc_check_init init");
		sql.append(" where 1=1 ");
		sql.append(" and ").append(getSQLIn(accs, 900, "init.account"));
		return listBySQLAliasToBean(sql.toString(),null,CentralityDataVO.class);
	}
    /**
     * <br>
     * <b>功能：</b> 删除中心对账单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/3 0003 <br>
     */
    public int deleteCompareAcc(FcCompareAcc entity) {
        String sql = " delete from fc_compare_acc where id = :id and comp_acc = :compAcc and compare_sign = :compareSign "
                + " and compare_Sign in (:oldSign) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("compAcc", entity.getCompAcc());
        params.put("compareSign", entity.getCompareSign());
        params.put("oldSign", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_NO});
        return createSQLQuery(sql, params).executeUpdate();
    }
	/**
	 *
	 * @param joinSql
	 * @return
	 */
	public List<String> getFcCompareAcc(String joinSql) {
		StringBuilder sql = new StringBuilder("select f.order_no as orderNo");
		sql.append(" from fc_compare_acc f ");
		sql.append(" where 1=1 ");
		sql.append(joinSql);
		return createSQLQuery(sql.toString()).list();
	}
}
