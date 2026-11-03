package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.NisFlowRule;
import com.bytter.biz.netbank.entity.NisFlowRulePurpose;
import com.bytter.biz.netbank.vo.NisFlowRulePurposeVO;
import com.bytter.biz.netbank.vo.NisFlowRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 资金调拨流水规则维护
 * @date 2024-05-08
 */
@Repository
public class NisFlowRuleDao extends BaseDao<NisFlowRule, String> {
	/**
	 * 查询资金调拨流水规则维护列表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisFlowRuleVO> findListBysql(DataTablesInput input) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select n.id as id,n.transfer_type as transferType,n.transfer_mode as transferMode,n.status AS status, ");
		sb.append(" n.create_date as createDate,n.create_by as createBy,n.update_date as updateDate,n.update_by as updateBy ");
		sb.append(" from nis_flow_rule n where 1 = 1 ");
		return getDynamicBySQL(input, sb.toString(), NisFlowRuleVO.class, "n", false);
	}

	/**
	 * 根据规则id删除规则用途
	 * @param ruleId
	 */
	public void deleteNisFlowRulePurpose(String ruleId){
		Map<String, Object> params = new HashMap<>();
		params.put("ruleId", ruleId);
		String sql = "  delete from nis_flow_rule_purpose  where rule_id = :ruleId ";
		createSQLQuery(sql, params).executeUpdate();
	}

	/**
	 * 根据规则id获取规则用途
	 * @param ruleId
	 */
	public List<NisFlowRulePurposeVO> getNisFlowRulePurpose(String ruleId) {
		Map<String, Object> params = new HashMap<>();
		params.put("ruleId", ruleId);
		StringBuilder sb = new StringBuilder();
		sb.append(" select n.id as id,n.rule_id as ruleId,n.purpose as purpose,n.bank_type_id as bankTypeId");
		sb.append(" from nis_flow_rule_purpose n where 1 = 1 and n.rule_id =:ruleId");
		return this.listBySQLAliasToBean(sb.toString(), params, NisFlowRulePurposeVO.class);
	}
}
