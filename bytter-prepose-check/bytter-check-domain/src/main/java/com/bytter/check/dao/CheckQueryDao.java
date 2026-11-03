package com.bytter.check.dao;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.FcCompareAccTempVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: 中心对账--erp对账单
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-06-02 17:21:47
* @version 1.0
*
*/
@Repository
public class CheckQueryDao extends BaseDao<FcCompareAcc,String>{
	
	/**查询erp对账table表**/
	public DataTablesOutput listByTable(DataTablesInput input,List<String> accList,String type){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*,a.cur_id as curCode from fc_compare_acc a ");
		if("1".equals(type)){
			sql.append(" inner join fc_acc f");
			sql.append(" on a.comp_acc = f.blnctr_acc ");
			sql.append(" where a.check_type = 1");
		}else{
			sql.append(" inner join bt_bank_acc f");
			sql.append(" on a.comp_acc = f.bank_acc ");
			sql.append(" where a.check_type = 2");
		}
		sql.append(" and ").append(getSQLIn(accList, 900, " a.comp_acc"));
		return getDynamicBySQL(input,sql.toString(), FcCompareAccTempVo.class,"",false);
	}
	
	
	/**查询条件--导出**/
	public List<FcCompareAcc> findListBySql(String eqs_compAcc,Date ged_tallyDate,Date led_tallyDate,String eqs_compareSign){
		String ged_tallyDate1 = null;
		String led_tallyDate1 = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select tally_date as tallyDate,comp_acc as compAcc,debit_money as debitMoney,credit_money as creditMoney,");
		sql.append("BT_NVL_NUM(balance,0) as balance,voucher_no as coucherNo,redblue_sign as redblueSign,compare_sign as compareSign,");
		sql.append("summary_content as summaryContent,data_source as dataSource");
		sql.append(" from Fc_Compare_Acc where 1=1");
		if(eqs_compAcc!= null && StringUtils.isNoneBlank(eqs_compAcc.trim())){
			sql.append(" and comp_acc = :eqs_compAcc ");
		}
		if(ged_tallyDate != null || led_tallyDate != null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			 	ged_tallyDate1 = sdf.format(ged_tallyDate);
			SimpleDateFormat sdfl=new SimpleDateFormat("yyyy-MM-dd");
			 	led_tallyDate1 = sdfl.format(led_tallyDate);
			sql.append(" and tally_date>=BT_TO_DATE(:ged_tallyDate1,'yyyy-mm-dd') and tally_date<=BT_TO_DATE(:led_tallyDate1 ,'yyyy-mm-dd')");
		}
				 
		if(EmptyUtils.isNotEmpty(eqs_compareSign) &&  StringUtils.isNoneBlank(eqs_compareSign.trim())){
			sql.append(" and compare_sign = :eqs_compareSign");
		}
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("eqs_compAcc", eqs_compAcc);
		params.put("ged_tallyDate1", ged_tallyDate1);
		params.put("led_tallyDate1", led_tallyDate1);
		params.put("eqs_compareSign", eqs_compareSign);
		return listBySQLAliasToBean(sql.toString(), params, FcCompareAcc.class);
	}
	
	
	/**查询条件--导出**/
	public List<FcCompareAcc> listQueryAll(){
		String sql = "select tally_date,debit_money,comp_acc,credit_money,BT_NVL_NUM(balance,0) as balance,voucher_no,redblue_sign,compare_sign,summary_content,data_source from fc_compare_acc";
		return listBySQLAliasToBean(sql.toString(), null, FcCompareAcc.class);
	}
	
}
