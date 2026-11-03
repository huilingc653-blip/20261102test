package com.bytter.biz.allocation.dao;
import com.bytter.biz.allocation.entity.ExcRulesManage;
import com.bytter.biz.allocation.vo.ExcRulesManageAdCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
@Repository
public class ExcRulesManageDao extends BaseDao<ExcRulesManage, String> {
	
	public List<ExcRulesManage> getExcRulesManageByPayerAccId(String payerAccId){
		String hql=" from ExcRulesManage e where e.payerAccId=:payerAccId order by e.corpId";
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("payerAccId",payerAccId);
		return listByHQL(hql, params);
	}

	public DataTablesOutput<ExcRulesManageAdCorpVO> getDataTablesUnion(DataTablesInput dataTableInput,String joinSql){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.filter_code as filterCode,b.filter_name as filterName,b.corp_id as corpId,");
		sql.append(" b.payee_acc_id as payeeAccId,b.payee_acc_no as payeeAccNo,b.payer_acc_id as payerAccId,b.payer_acc_no as payerAccNo,");
		sql.append(" b.launch_time as launchTime,b.valid_sign as valid_sign,b.rmk as rmk,");
		sql.append(" b.create_by as createBy, b.create_date as createDate,b.update_by as updateBy,b.update_Date as updateDate");
		sql.append(" from BIS_POLICY_FILTER b where 1=1");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTableInput, sql.toString(), ExcRulesManageAdCorpVO.class, "b", false);
	}
	
	public ExcRulesManageAdCorpVO getByUnion(String id){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.filter_code as filterCode,b.filter_name as filterName,b.corp_id as corpId,");
		sql.append(" b.payee_acc_id as payeeAccId,b.payee_acc_no as payeeAccNo,b.payer_acc_id as payerAccId,b.payer_acc_no as payerAccNo,");
		sql.append(" b.launch_time as launchTime,b.valid_sign as valid_sign,b.rmk as rmk,");
		sql.append(" b.create_by as createBy, b.create_date as createDate,b.update_by as updateBy,b.update_Date as updateDate");
		sql.append(" from BIS_POLICY_FILTER b");
		sql.append(" where b.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ExcRulesManageAdCorpVO.class);
	}
	
	public List<ExcRulesManage> getByPayeeNoAndPayerNo(String payeeNo,String payerNo){
		String hql=" from ExcRulesManage e where e.payeeAccNo=:payeeNo and e.payerAccNo=:payerNo";
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("payeeNo",payeeNo);
		params.put("payerNo",payerNo);
		return listByHQL(hql, params);
	}
}
