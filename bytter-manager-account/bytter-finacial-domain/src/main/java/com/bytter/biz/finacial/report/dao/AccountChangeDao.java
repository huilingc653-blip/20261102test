package com.bytter.biz.finacial.report.dao;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.report.vo.AccountChangeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
	
@Repository
public class AccountChangeDao extends BaseDao<AccountChangeVo,String>{
	/**
	 * TODO 报表相关查询 暂不处理
	 * @param vo
	 * @param corpAuth
	 * @return
	 */
	public  List<AccountChangeVo>  listAll(AccountChangeVo vo,List<String> corpAuth){
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.account,c.acc_name,b.balance_way,a.yester_balance,b.debit_money,b.credit_money,a.balance, ");
		sql.append(" b.cur_id,b.subject_id,a.balance_date,s.subject_name, y.cur_name ");
		sql.append(" from fc_acc_balance a,fc_acc c,fc_subject s,bt_currency y,"); // 报表相关查询 暂不处理
		sql.append(" (select fac.account as account,min(fac.balance_date) min_date,");
		sql.append(" sum(fac.debit_money) debit_money,sum(fac.credit_money) credit_money, max(fac.balance_date) max_date,");
		sql.append(" fcc.balance_way,fcc.cur_id,fcc.subject_id ");
		sql.append(" from fc_acc_balance fac, fc_acc fcc where fac.account = fcc.blnctr_acc and fac.balance_date >= fcc.acc_start_date "); // 报表相关查询 暂不处理
		if(!checkIsNull(vo.getNetId())){
			sql.append(" and fcc.net_id = :netId ");
		}
		if (!checkIsNull(vo.getCorpId())) {
			sql.append(" and fcc.corp_Id = :corpId");
		}
		if(!checkIsNull(vo.getLe_account())){
			sql.append(" and fac.account like :le_account ");
		}
		if(!checkIsNull(vo.getGe_account())){
			sql.append(" and fac.account <= :ge_account ");
		}
		if(!checkIsNull(vo.getSubjectId())){
			sql.append(" and fcc.subject_id = :subjectId");
		}
		String leDate = null;
		if(vo.getLe_balanceDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			leDate = sdf.format(vo.getLe_balanceDate());
			sql.append(" and fac.balance_date >= BT_TO_DATE(:le_balanceDate, 'yyyy-MM-dd')");	
		}
		String geDate = null;
		if(vo.getGe_balanceDate() != null){
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			 geDate = sdf2.format(vo.getGe_balanceDate());
			sql.append(" and fac.balance_date <= BT_TO_DATE( :ge_balanceDate,'yyyy-MM-dd')");
		}
		sql.append(" and fcc.subject_id not in (select subject_code from fc_subject where fc_subject.subject_sort = 'G') ");
		sql.append(" group by fac.account, fcc.balance_way,fcc.cur_id,fcc.subject_id) b ");
		sql.append(" where a.account = b.account and b.account = c.blnctr_acc  and (a.balance_date = b.min_date or a.balance_date = b.max_date)");
		sql.append(" and s.id = c.subject_id and c.cur_id = y.id ");
		if(!checkIsNull(vo.getNetId())){
			sql.append(" and c.net_id = :netId ");
		}
		if (!checkIsNull(vo.getCorpId())) {
			sql.append(" and c.corp_Id = :corpId");
		}
		if(!checkIsNull(vo.getLe_account())){
			sql.append(" and a.account like :le_account");
		}
		if(!checkIsNull(vo.getGe_account())){
			sql.append(" and a.account <= :ge_account ");
		}
		if(!checkIsNull(vo.getSubjectId())){
			sql.append(" and b.subject_id = :subjectId");
		}
		String leDate1 = null;
		if(vo.getLe_balanceDate() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			leDate1 = sdf.format(vo.getLe_balanceDate());
			sql.append(" and a.balance_date >= BT_TO_DATE(:le_balanceDate, 'yyyy-MM-dd')");	
		}
		String geDate1 = null;
		if(vo.getGe_balanceDate() != null){
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			geDate1 = sdf2.format(vo.getGe_balanceDate());
			sql.append(" and a.balance_date <= BT_TO_DATE( :ge_balanceDate,'yyyy-MM-dd')");
		}
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "c.corp_Id"));
        sql.append(" order by y.cur_code,s.subject_code,b.subject_Id,a.account,a.balance_date");
	
			
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("netId",vo.getNetId() );
		param.put("corpId",vo.getCorpId() );
		param.put("le_account","%"+vo.getLe_account()+"%" );
		param.put("ge_account", vo.getGe_account());
		param.put("subjectId",vo.getSubjectId() );
		param.put("le_balanceDate", leDate);
		param.put("ge_balanceDate",geDate );
		
		return listBySQLAliasToBean(sql.toString(), param, AccountChangeVo.class);
		
	}
	
	private static boolean checkIsNull(String str) {
		if (EmptyUtils.isEmpty(str) || "".equals(str.trim()) || "undefined".equals(str)) {
			return true;
		}
		return false;
	}
}
