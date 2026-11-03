package com.bytter.system.dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.system.vo.BtBankAccMobileBalVO;
import com.bytter.system.vo.BtBankAccMobileDtlVO;
import com.bytter.system.vo.BtBankAccMobileVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
/**
 * @Title: Controller
 * @Description: BT_BANK_ACC
 * @author onlineGenerator
 * @date 2017-06-14 14:26:46
 * @version 1.0
 *
 */
@Repository
public class BankAccMobieDao extends BaseDao<T, String> implements  java.io.Serializable{
	/**
	 * 查询账号余额
	 * @param corpId
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param bankAccId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BtBankAccMobileBalVO> getBal(String corpId, String userId, String beginDate, String endDate, String bankAccId,String curId){
		List<BtBankAccMobileBalVO> result = new ArrayList<BtBankAccMobileBalVO>();
		String sql = " select distinct acc.cur_name as curName,\n" +
				"                bal.bal_date as balDate,\n" +
				"                acc.bank_acc as bankAcc,\n" +
				"                acc.bank_name as bankName,\n" +
				"                bal.bal as bal\n" +
				"  from sys_corp c,\n" +
				"       (select acc.id,acc.valid_sign,acc.bank_acc,acc.bank_name,cur.cur_name,acc.corp_id,cur.id as curId\n" +
				"           from bt_bank_acc acc\n" +
				"           left join bt_bank_acc_cur acccur\n" +
				"             on acc.id = acccur.bank_acc_id\n" +
				"           left join bt_currency cur\n" +
				"             on acccur.cur_id = cur.id) acc,\n" +
				"       bis_acc_bal bal\n" +
				" where c.id = acc.corp_id\n" +
				"   and acc.bank_acc = bal.bank_acc\n" +
				"   and c.status = '1'\n" +
				"   and acc.valid_sign = '1'\n" +
				"   and acc.id in\n" +
				"       ( select g.account_id from sys_account_grant g\n" +
				"         where g.user_id = :userId\n" +
				"           and g.can_query_balance = '1') ";
		if(StringUtils.isNotEmpty(corpId)){
			sql += " and c.id = :corpId";
		}
		if(StringUtils.isNotEmpty(beginDate)){
			sql += " and to_char(bal.bal_date,'yyyy-MM-dd') >= :beginDate ";
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql += " and to_char(bal.bal_date,'yyyy-MM-dd') <= :endDate ";
		}
		if(StringUtils.isNotEmpty(bankAccId)){
			sql += " and acc.id = :bankAccId ";
		}
		if(StringUtils.isNotEmpty(curId)){
			sql += " and acc.curId = :curId ";
		}
		sql +="    order by acc.cur_name, acc.bank_acc, bal.bal_date ";
		Map<String,String> param = new HashMap<String, String>();
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		param.put("corpId", corpId);
		param.put("userId", userId);
		param.put("bankAccId", bankAccId);
		param.put("curId", curId);
		result = listBySQLAliasToBean(sql.toString(), param, BtBankAccMobileBalVO.class);
		return result;
	}
	/**
	 *
	 * @param corpId
	 * @param userId
	 * @param beginDate
	 * @param endDate
	 * @param bankAcc
	 * @param beginMoney
	 * @param endMoney
	 * @param oppBankAcc
	 * @return
	 */
	public List<BtBankAccMobileDtlVO> getDtl(String corpId, String userId, String beginDate, String endDate, String bankAcc, String beginMoney,
											 String endMoney, String oppBankAcc,String curId){
		List<BtBankAccMobileDtlVO> result = new ArrayList<BtBankAccMobileDtlVO>();
		String sql = "  select distinct acc.cur_name as curName,\n" +
				"                 dtl.trans_time as transTime,\n" +
				"                 acc.bank_acc as bankAcc,\n" +
				"                 dtl.bal as bal,\n" +
				"                 dtl.opp_acc_no as oppAccNo,\n" +
				"                 dtl.opp_acc_name as oppAccName,\n" +
				"                 dtl.remark as remark,\n" +
				"                 dtl.amt as amt,\n" +
				"                 dtl.cd_sign as cdSign\n" +
				"   from sys_corp c,\n" +
				"        (select acc.id,\n" +
				"                acc.valid_sign,\n" +
				"                acc.bank_acc,\n" +
				"                acc.bank_name,\n" +
				"                cur.cur_name,\n" +
				"                acc.corp_id,cur.id as curId\n" +
				"           from bt_bank_acc acc\n" +
				"           left join bt_bank_acc_cur acccur\n" +
				"             on acc.id = acccur.bank_acc_id\n" +
				"           left join bt_currency cur\n" +
				"             on acccur.cur_id = cur.id) acc,\n" +
				"        bis_acc_dtl dtl\n" +
				"  where c.id = acc.corp_id\n" +
				"    and acc.BANK_ACC = dtl.bank_acc\n" +
				"    and c.status = '1'\n" +
				"    and acc.valid_sign = '1'\n" +
				"    and acc.id in (select g.account_id\n" +
				"                     from sys_account_grant g\n" +
				"                      where g.user_id = :userId\n" +
				"                      and g.can_query_detail = '1') ";
		if(StringUtils.isNotEmpty(corpId)){
			sql += " and c.id = :corpId";
		}
		if(StringUtils.isNotEmpty(beginDate)){
			sql += " and to_char(dtl.trans_time,'yyyy-MM-dd') >= :beginDate ";
		}
		if(StringUtils.isNotEmpty(endDate)){
			sql += " and to_char(dtl.trans_time,'yyyy-MM-dd') <= :endDate ";
		}
		if(StringUtils.isNotEmpty(bankAcc)){
			sql += " and acc.bank_acc = :bankAcc ";
		}
		if(StringUtils.isNotEmpty(beginMoney)){
			sql += " and dtl.amt >= "+NumberUtils.createDouble(beginMoney);
		}
		if(StringUtils.isNotEmpty(endMoney)){
			sql += " and dtl.amt <= "+ NumberUtils.createDouble(endMoney);
		}
		if(StringUtils.isNotEmpty(oppBankAcc)){
			sql += " and dtl.opp_acc_no like '%"+oppBankAcc+"%'";
		}
		if(StringUtils.isNotEmpty(curId)){
			sql += " and acc.curId = :curId ";
		}
		sql +=" order by acc.cur_name,acc.bank_acc,dtl.trans_time ";
		Map<String,String> param = new HashMap<String, String>();
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		param.put("corpId", corpId);
		param.put("userId", userId);
		param.put("bankAcc", bankAcc);
		param.put("curId", curId);
		result = listBySQLAliasToBean(sql.toString(), param, BtBankAccMobileDtlVO.class);
		return result;
	}
	/**
	 * 获取有权限的银行账号
	 * @param userId
	 * @param corpId
	 * @return
	 */
	public List<BtBankAccMobileVO> getGrantBankAcc(String userId, String corpId, String grantType){
		List<BtBankAccMobileVO> result = new ArrayList<>();
		String sql = " select acc.bank_acc as bankAcc,acc.bank_name as bankName ,acc.cur_name as curName"+
				"  from (select acc.id,acc.valid_sign,acc.bank_acc,acc.bank_name,cur.cur_name,acc.corp_id from  bt_bank_acc acc left join bt_bank_acc_cur acccur on acc.id = acccur.bank_acc_id\n" +
				" left join bt_currency cur on acccur.cur_id = cur.id) acc ,sys_account_grant g  "+
				"  where acc.id = g.account_id and acc.valid_sign = '1'"+
				"  and g.user_id = :userId";
		if("bal".equals(grantType)){
			sql +=	"  and g.can_query_balance =  '1' ";
		}
		if("dtl".equals(grantType)){
			sql +=	" and g.can_query_detail = '1' ";
		}
		if(StringUtils.isNotEmpty(corpId)){
			sql +=	" and acc.corp_id = :corpId ";
		}
		Map<String,String> param = new HashMap<String, String>();
		param.put("corpId", corpId);
		param.put("userId", userId);
		result = listBySQLAliasToBean(sql.toString(), param, BtBankAccMobileVO.class);
		return result;
	}
}
