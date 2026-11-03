package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefinePool;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_VOUCHER_ENTRY_DEFINE_POOL 
* @author onlineGenerator
* @date 2017-05-22 16:44:06
* @version 1.0
*
*/
@Repository
public class FcVoucherEntryDefinePoolDao extends BaseDao<FcVoucherEntryDefinePool,String>{
	
	/**
	 * 功能介绍 ： 根据记账规则获取规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcVoucherEntryDefine> getEntryDefinesByRuleId(String ruleId) {
		String hql = " from FcVoucherEntryDefine where businessRuleDefineId = :ruleId order by groupNum ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ruleId", ruleId);
		return this.createQuery(hql, params).list();
	}
	
	/**
	 * 功能介绍 : 获取手续费记账规则明细。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcVoucherEntryDefine> getChargeDefinesByRuleId(String ruleId) {
		String hql = " from FcVoucherEntryDefine where businessRuleDefineId = :ruleId and belongType = :belongType order by balanceWay desc ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ruleId", ruleId);
		params.put("belongType", FinacialConstant.BELONG_TYPE_CHARGE);
		return this.createQuery(hql, params).list();
	}
	
	/**
	 * 功能介绍 ： 根据记账规则ID删除规则记账分录。<br>
	 * <b>日期：</b>2017年5月24日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteEntryDefinesByRuleId(String ruleId) {
		String hql = " delete from FcVoucherEntryDefinePool where businessRuleDefineId = :ruleId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ruleId", ruleId);
		this.batchExecute(hql, params);
	}
	
	/**
	 * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
	 */
	public List<FcVoucherEntryDefineVo> getVoucherEntryDefinesPoolByRuleId(String ruleId) {
		StringBuilder sql=new StringBuilder();
		/*sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
		sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fs.id as subjectId,fc.subject_code as subjectCode, ");
		sql.append("fs.subject_name as subjectName,fc.summary as summary, ");
		sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
		sql.append(" from fc_voucher_entry_define_pool fc ");
		sql.append(" left join fc_subject_pool fs on fc.subject_code=fs.id");
		sql.append(" where fc.business_rule_define_id = :ruleId ");
		sql.append(" order by fc.group_num,fc.balance_way desc ");*/
		sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
		sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectId,fc.subject_code as subjectCode, ");
		sql.append("fc.summary as summary, ");
		sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
		sql.append(" from fc_voucher_entry_define_pool fc ");
		sql.append(" where fc.business_rule_define_id = :ruleId ");
		sql.append(" order by fc.group_num,fc.balance_way desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ruleId", ruleId);
		return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefineVo.class);
	}
	
	/**
	 * 功能介绍 ： 根据记账规id则获取规则记账分录。<br>
	 * <b>日期：</b>2017年9月21日<br>
	 * @version 1.0
	 * @author yangl 
	 */
	public List<FcVoucherEntryDefineVo> getInitVoucherEntryDefinesByRuleId(String ruleId) {
		StringBuilder sql=new StringBuilder();
		/*sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
		sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
		sql.append("fs.subject_name as subjectName,fc.summary as summary, ");
		sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
		sql.append(" from FC_VOUCHER_ENTRY_INIT fc ");
		sql.append(" left join fc_subject fs on fc.subject_code=fs.id");
		sql.append(" where fc.business_rule_define_id = :ruleId ");
		sql.append(" order by fc.group_num,fc.balance_way desc ");*/
		sql.append("select fc.id as id,fc.business_rule_define_id as businessRuleDefineId, ");
		sql.append("fc.belong_type as belongType,fc.balance_way as balanceWay,fc.subject_code as subjectCode, ");
		sql.append("fc.summary as summary, ");
		sql.append("fc.entry_type as entryType,fc.group_num as groupNum ");
		sql.append(" from FC_VOUCHER_ENTRY_INIT fc ");
		sql.append(" where fc.business_rule_define_id = :ruleId ");
		sql.append(" order by fc.group_num,fc.balance_way desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ruleId", ruleId);
		return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefineVo.class);
	}
	
	/**
	 * 功能介绍 : 删除已存在记账规则明细。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public void deleteInitEntryRule() {
		StringBuilder sql = new StringBuilder();
		sql.append(" delete from fc_voucher_entry_define_pool ");
		sql.append(" where exists (select 1 from fc_voucher_entry_init ");
		sql.append(" where fc_voucher_entry_init.business_rule_define_id = fc_voucher_entry_define_pool.business_rule_define_id ) ");
		createSQLQuery(sql.toString()).executeUpdate();
	}
	
	/**
	 * 功能介绍 : 记账规则明细导入。<br>
	 */
	public void importInitEntryRule() {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into fc_voucher_entry_define_pool (id, business_rule_define_id, ");
		sql.append(" belong_type, balance_way, subject_code, account, cur_id, summary, entry_type, ");
		sql.append(" group_num, voucher_type,create_time, create_by, update_time, update_by) ");
		sql.append(" select fb.id,fb.business_rule_define_id, ");
		sql.append(" fb.belong_type,fb.balance_way,fb.subject_code,fb.account,fb.cur_code,fb.summary,fb.entry_type, ");
		sql.append(" fb.group_num,fb.voucher_type,fb.create_time,fb.create_by,fb.update_time,fb.update_by ");
		sql.append(" from fc_voucher_entry_init fb ");
		sql.append(" where not exists (select 1 from fc_voucher_entry_define_pool fd where fd.id = fb.id) ");
		sql.append(" and fb.business_rule_define_id in ('af28332d296744f883c4bb30113086ef','46effb959bad41389469a1691b3b0639','b03570b46f0c49cab882fb107fc6c881')");
		createSQLQuery(sql.toString()).executeUpdate();
	}
}
