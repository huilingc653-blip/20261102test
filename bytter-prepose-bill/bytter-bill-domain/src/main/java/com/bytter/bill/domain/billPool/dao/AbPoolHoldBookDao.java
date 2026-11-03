package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolHoldBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 票据池持有账簿信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Repository
public class AbPoolHoldBookDao extends BaseDao<AbPoolHoldBookVO, String> {
	public String getBaseSql(){
		StringBuilder sql = new StringBuilder("select ");
		//sql.append("h.id,sc.id as corpId,sc.code as corpCode,sc.name as corpName,v.bill_type as billType,v.bill_flag as billFlag,h.inout_type as inoutType,h.orig_no as origNo,h.create_time as createTime,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate, ");
		sql.append("h.id,pc.corp_id as corpId,pc.corp_code as corpCode,pc.corp_name as corpName,v.bill_type as billType,v.bill_flag as billFlag,h.inout_type as inoutType,h.orig_no as origNo,h.create_time as createTime,v.bill_money as billMoney,v.start_date as startDate,v.end_date as endDate, ");
		sql.append("v.payer,v.payee,v.acceptor,p.bp_code as poolNo,p.bp_name as poolName,h.UPDATE_TIME as updateTime  ");
		sql.append("From ab_pool_hold_book h  ");
		sql.append("left join ab_voucherinfo v on v.orig_no=h.orig_no ");
		sql.append("left join ab_pool p on h.bp_id=p.id ");
		sql.append("left join ab_pool_corp pc on pc.corp_id=h.corp_id and pc.valid_Sign=:validSign ");
		//sql.append("left join sys_corp sc on sc.id=pc.corp_id  ");
		sql.append("where 1=1 ");
		return sql.toString();
	}
	
	/**
	 * "查询票据池持有账簿信息列表数据
	 * @param dataTablesInput
	 * @param corpAuthList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput, List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<String, Object>();
		String etime = (String)dataTablesInput.getFormdata().get("led_startDate");
		if(StringUtils.isNotBlank(etime)){
			etime = etime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_startDate", etime);
		}
		StringBuilder sql = new StringBuilder(getBaseSql());
		params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolHoldBookVO.class, null, false, params);
	}
	
	/**
	 * 根据ID获取持有账簿信息
	 * @param entity
	 * @return
	 */
	public List<AbPoolHoldBookVO> getAbPoolHoldBookVOById(String id) {
		if(StringUtils.isEmpty(id)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(getBaseSql());
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        sql.append(" and h.id=:id");
        params.put("id", id);
		return this.listBySQLAliasToBean(sql.toString(), params, AbPoolHoldBookVO.class);
	}
	
}
