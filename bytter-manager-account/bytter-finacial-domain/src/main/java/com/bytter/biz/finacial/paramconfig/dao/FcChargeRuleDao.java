package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeChargeRuleRel;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeRule;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeRuleVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
@Repository
public class FcChargeRuleDao extends BaseDao<FcChargeRule,String>{
	
	/**
	 * 功能介绍: 费用计提维护表list(Vo对象)<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listFcChargeRule(DataTablesInput input){
		/*StringBuilder sql = new StringBuilder("select fcr.id as id,bpt.voucher_name as voucherType,nbt.id as sysRaceId,nbt.sys_race_name as raceCode," +
				"fs.subject_name as debitSubjCode,fss.subject_name as creditSubjCode,fcr.summary summary,sc.name netId," +
				"fcr.create_time,fcr.create_by,fcr.update_time,fcr.update_by,fcr.cur_code as curCode,bc.cur_name as curName, " +
				"fs.id as fsSubjId,fss.id as fssSubjId,bc.id as curId,bpt.id as voucherId ");
		sql.append("from FC_CHARGE_RULE fcr ");
						sql.append("left join fc_sys_bus_race nbt on fcr.race_code = nbt.sys_race_code ");
						sql.append("left join fc_voucher_type bpt on fcr.voucher_type = bpt.id ");
						sql.append("left join fc_subject fs on fcr.debit_subj_code = fs.id ");
						sql.append("left join fc_subject fss on fcr.credit_subj_code = fss.id ");
						sql.append("left join bt_summary bs on fcr.summary = bs.id ");
						sql.append("left join bt_currency bc on fcr.cur_code = bc.id ");
						sql.append("left join sys_corp sc on fcr.net_id = sc.id  where 1=1 ");*/
		// fs.subject_name as debitSubjCode；fs.id as fsSubjId；--》fsSubjId
		// fss.subject_name as creditSubjCode；fss.id as fssSubjId；--》fssSubjId
		// bc.cur_name as curName；bc.id as curId --》curCode 实际是 id
		// sc.name netId --》netId 实际是 id
		//好混乱：fcr.voucher_type as voucherId，bpt.voucher_name as voucherType；历史遗留，暂不处理
		StringBuilder sql = new StringBuilder("select fcr.id as id,nbt.id as sysRaceId,nbt.sys_race_name as raceCode," +
				"fcr.summary summary,fcr.net_id netId," +
				"fcr.create_time,fcr.create_by,fcr.update_time,fcr.update_by,fcr.cur_code as curCode, " +
				"fcr.debit_subj_code as fsSubjId,fcr.credit_subj_code as fssSubjId,fcr.cur_code as curId,fcr.voucher_type as voucherId ");
		sql.append("from FC_CHARGE_RULE fcr ");
		sql.append("left join fc_sys_bus_race nbt on fcr.race_code = nbt.sys_race_code ");
//		sql.append("left join fc_voucher_type bpt on fcr.voucher_type = bpt.id ");
		sql.append("left join bt_summary bs on fcr.summary = bs.id ");
		sql.append("where 1=1 ");
		//TODO fc_voucher_type 表在结算
		return getDynamicBySQL(input, sql.toString(), FcChargeRuleVo.class, "",false);				
	}
	/**
	 * 功能介绍:费用计提凭证分录list<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public List<FcVoucherEntryDefineVo> listFcVoucherEntryDefine(String id){
		StringBuilder sql = new StringBuilder();
		/*sql.append("select d.id,d.business_rule_define_id as businessRuleDefineId,d.belong_type as belongType, ");
		sql.append("d.balance_way as balanceWay,d.subject_code as subjectCode,sub.subject_name as subjectName, ");
		sql.append("d.voucher_type as voucherType,v.voucher_name as voucherName,d.account,d.cur_code as curCode, ");
		sql.append("d.summary,d.summary as summaryContent,d.entry_type as entryType,d.group_num as groupNum, ");
		sql.append(" sub.id as subjectId,v.id as voucherId ");
		sql.append("from fc_voucher_entry_define d ");
		//sql.append("left join bt_summary s on d.summary = s.id ");
		sql.append("left join fc_subject sub on d.subject_code = sub.id ");
		sql.append("left join fc_voucher_type v on d.voucher_type = v.id ");
		sql.append("where d.belong_type=:belongType and d.business_rule_define_id=:id ");
		sql.append("order by d.balance_way desc ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("belongType", FinacialConstant.BELONG_TYPE_CHARGE);
		params.put("id", id);*/
		//v.voucher_name as voucherName
		sql.append("select d.id,d.business_rule_define_id as businessRuleDefineId,d.belong_type as belongType, ");
		sql.append("d.balance_way as balanceWay,d.subject_code as subjectCode, ");
		sql.append("d.voucher_type as voucherType,d.account,d.cur_code as curCode, ");
		sql.append("d.summary,d.summary as summaryContent,d.entry_type as entryType,d.group_num as groupNum, ");
		sql.append(" d.subject_code as subjectId,d.voucher_type as voucherId ");
		sql.append("from fc_voucher_entry_define d ");
//		sql.append("left join fc_voucher_type v on d.voucher_type = v.id ");
		sql.append("where d.belong_type=:belongType and d.business_rule_define_id=:id ");
		sql.append("order by d.balance_way desc ");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("belongType", FinacialConstant.BELONG_TYPE_CHARGE);
		params.put("id", id);
		return listBySQLAliasToBean(sql.toString(), params, FcVoucherEntryDefineVo.class);
 	}
	/**
	 * 功能介绍:费用类型与费用计提list<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<ChargeTypeChargeRuleRel> listChargeTypeChargeRules(String id){
		Query query = null;
	    StringBuilder sb = new StringBuilder("from ChargeTypeChargeRuleRel fct where fct.chargeRuleId=?0");
	    query = createQuery(sb.toString(),id);
	    return query.list();
	}
	
	/**
	 * 功能介绍:保存费用类型与费用计提关联<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void saveChargeTypeChargeRuleRel(ChargeTypeChargeRuleRel chargeTypeChargeRuleRel){
		getSession().save(chargeTypeChargeRuleRel);
	}
	/**
	 * 功能介绍:保存费用计提凭证分录<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void saveFcVoucherEntryDefine(FcVoucherEntryDefine fcVoucherEntryDefine){
		getSession().save(fcVoucherEntryDefine);
	}
	
	/**
	 * 功能介绍:删除费用类型与费用计提关联<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteChargeTypeChargeRuleRel(String chargeRuleId){
		createSQLQuery("DELETE FROM CHARGE_TYPE_CHARGE_RULE_REL WHERE CHARGE_RULE_ID=?",chargeRuleId).executeUpdate();
	}
	
	/**
	 * 功能介绍:删除<br>
	 * <b>日期：</b>2017年5月26日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void deleteFcVoucherEntryDefine(String chargeRuleId){
		createSQLQuery("DELETE FROM FC_VOUCHER_ENTRY_DEFINE WHERE BUSINESS_RULE_DEFINE_ID=?",chargeRuleId).executeUpdate();
	}
	
	/**
	 * 
	 * 功能介绍 : 根据业务规则获取手续费规则。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeRule> queryByBusRaceAndVoucherType(Set<String> raceCodes, Set<String> voucherTypeCodes,
			String netCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeRule t  ")
			.append(" where t.voucherType in (:voucherTypeCodes) ")
			.append(" and t.netId = :netCode ")
			.append(" and t.raceCode in ( select b.sysRaceCode from FcBusRace b where b.id in (:raceCodes) ) ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("raceCodes", raceCodes);
		params.put("voucherTypeCodes", voucherTypeCodes);
		params.put("netCode", netCode);
		return listByHQL(hql.toString(), params);
	}
	
	/**
	 * 功能介绍: 根据id查询费用计提详情(Vo对象)<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcChargeRuleVo getFcChargeRuleById(String id){
		StringBuilder sql = new StringBuilder(" ");
		/*sql.append(" select fcr.id as id,bpt.voucher_name as voucherType,nbt.id as sysRaceId,nbt.sys_race_name as raceCode,fs.subject_name as debitSubjCode, ");
		sql.append(" fss.subject_name as creditSubjCode,fcr.summary summary,sc.name netId,fcr.create_time,fcr.create_by,fcr.update_time, ");
		sql.append(" fcr.update_by,fcr.cur_code as curCode,bc.cur_name as curName,fs.id as fsSubjId,fss.id as fssSubjId,bc.id as curId,bpt.id as voucherId ");
		sql.append(" from FC_CHARGE_RULE fcr ");
		sql.append(" left join fc_sys_bus_race nbt on fcr.race_code = nbt.sys_race_code ");
		sql.append(" left join fc_voucher_type bpt on fcr.voucher_type = bpt.id ");
		sql.append(" left join fc_subject fs on fcr.debit_subj_code = fs.id ");
		sql.append(" left join fc_subject fss on fcr.credit_subj_code = fss.id ");
		sql.append(" left join bt_summary bs on fcr.summary = bs.id ");
		sql.append(" left join sys_corp sc on fcr.net_id = sc.id ");
		sql.append(" left join bt_currency bc on fcr.cur_code = bc.id where fcr.id = :id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);*/
		//fs.subject_name as debitSubjCode；fs.id as fsSubjId
		//fss.subject_name as creditSubjCode；fss.id as fssSubjId
		//sc.name netId
		//bc.cur_name as curName

		//bpt.voucher_name as voucherType
		sql.append(" select fcr.id as id,nbt.id as sysRaceId,nbt.sys_race_name as raceCode, ");
		sql.append(" fcr.summary summary,fcr.net_id netId,fcr.create_time,fcr.create_by,fcr.update_time, ");
		sql.append(" fcr.update_by,fcr.cur_code as curCode,fcr.debit_subj_code as fsSubjId,fcr.credit_subj_code as fssSubjId,fcr.cur_code as curId,fcr.voucher_type as voucherId ");
		sql.append(" from FC_CHARGE_RULE fcr ");
		sql.append(" left join fc_sys_bus_race nbt on fcr.race_code = nbt.sys_race_code ");
//		sql.append(" left join fc_voucher_type bpt on fcr.voucher_type = bpt.id ");
		sql.append(" left join bt_summary bs on fcr.summary = bs.id ");
		sql.append(" where fcr.id = :id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return (FcChargeRuleVo)getBySQLAliasToBean(sql.toString(), params, FcChargeRuleVo.class);
	}
}
