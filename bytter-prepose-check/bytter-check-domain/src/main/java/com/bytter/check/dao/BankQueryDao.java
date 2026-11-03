package com.bytter.check.dao;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_BANK_BILL
* @author onlineGenerator
* @date 2017-06-07 11:30:25
* @version 1.0
*
*/
@Repository
public class BankQueryDao extends BaseDao<FcBankBill,String>{
	/**银行帐号**/
	public  List<Map<String, String>> listBack(){
		String hql = "select ID,BANK_ACC from Fc_Bank_Bill";
		List<Map<String,String>> list = listBySQL(hql,null);
		
		return list ;
	}
	
	/**查询table表**/
	public DataTablesOutput listTable(DataTablesInput input,List<String> accS){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.* from fc_bank_bill a where 1=1");
		sql.append(" and ").append(getSQLIn(accS, 900, "a.bank_acc"));
		return getDynamicBySQL(input,sql.toString(), FcBankBillVo.class,"",false);
	}
	
	
	
	/**导出--查询条件**/
	public List<FcBankBill> listQuery(String eqs_compareSign,String eqs_bankAcc,Date ged_tallyDate,Date led_tallyDate){
		String led_tallyDate1 = null;
		String ged_tallyDate1 = null;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select a.tally_date,a.bank_acc,a.debit_money,a.credit_money,BT_NVL_NUM(balance,0) as balance,a.bill_no,a.redblue_sign,a.compare_sign ");
		sql.append(" from fc_bank_bill a ");
		sql.append(" where 1=1 ");
		
		if(EmptyUtils.isNotEmpty(eqs_compareSign ) && !" ".equals(eqs_compareSign)){
			sql.append("and a.compare_sign= :eqs_compareSign ");
		}
		if(EmptyUtils.isNotEmpty(eqs_bankAcc)){
			sql.append("and a.bank_acc= :eqs_bankAcc ");
		}
		if(ged_tallyDate != null && led_tallyDate != null){
			SimpleDateFormat sdf = new 	SimpleDateFormat("yyyy-MM-dd");
			ged_tallyDate1 = sdf.format(ged_tallyDate);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			led_tallyDate1 = sdf1.format(led_tallyDate);
			
			sql.append(" and a.tally_date>=BT_TO_DATE(:ged_tallyDate1,'yyyy-mm-dd') and a.tally_date<=BT_TO_DATE(:led_tallyDate1,'yyyy-mm-dd')");
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("eqs_compareSign", eqs_compareSign);
		map.put("eqs_bankAcc", eqs_bankAcc);
		map.put("ged_tallyDate1", ged_tallyDate1);
		map.put("led_tallyDate1", led_tallyDate1);
		
		return listBySQLAliasToBean(sql.toString(),map,FcBankBill.class);
	}
	
	
}
