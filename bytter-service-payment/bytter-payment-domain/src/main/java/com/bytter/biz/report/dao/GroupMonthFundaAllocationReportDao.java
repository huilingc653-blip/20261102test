package com.bytter.biz.report.dao;

import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 集团月度资金划拨分析表
 * @date 2024-05-16
 */
@Repository
public class GroupMonthFundaAllocationReportDao extends BaseDao<BisExcDTO, String> {
	/**
	 * 资金调拨列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		params.put("status", 1);
		sql.append(" select sc.id as id,sc.code as code,sc.name as name,sc.parent_id as parentId,sc.parent_Ids as parentIds,");
		sql.append(" sc.status as status,sc.net_id as netId,sc.cur_id as curId,sc.operationArea_id as operationAreaId ");
		sql.append(" from sys_corp sc  where 1 = 1 ");
		sql.append(" and sc.status =:status  ");
		if(StringUtils.isNotBlank(joinSql)){
			sql.append(" and").append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), CorpDTO.class, "", false,params);
	}
}
