package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolSignInfoVO;
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
 * @Description: 票据池票据池签约信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Repository
public class AbPoolSignInfoDao extends BaseDao<AbPoolSignInfoVO, String> {
	public String getBaseSql(){
		StringBuilder sql = new StringBuilder("select ");
		//sql.append("s.id,sc.id as corpId,sc.code as corpCode,sc.name as corpName,s.con_no as conNo,s.con_date as conDate,s.sign_date as signDate,s.sign_org_name as signOrgName, ");
		sql.append("s.id,pc.corp_id as corpId,pc.corp_code as corpCode,pc.corp_name as corpName,s.con_no as conNo,s.con_date as conDate,s.sign_date as signDate,s.sign_org_name as signOrgName, ");
		sql.append("s.valid_sign as validSign,s.bank_acc as bailAcc,s.bail_money as bailMoney,p.bp_code as poolNo,p.bp_name as poolName,s.create_time as createTime,s.update_time as updateTime, ");
		sql.append("s.orig_code as origCode,s.orig_name as origName ");
		sql.append("From AB_POOL_SIGN_INFO s ");
		sql.append("left join ab_pool p on s.bp_id=p.id ");
		sql.append("left join ab_pool_corp pc on pc.corp_id=s.corp_id and pc.valid_sign=:validSign ");
		//sql.append("left join sys_corp sc on sc.id=pc.corp_id ");
        sql.append(" where 1=1 ");
		return sql.toString();
	}
	
	/**
	 * "查询票据池票据池签约信息列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbPoolSignInfoVO> datatableQuery(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<String, Object>();
		String etime = (String)dataTablesInput.getFormdata().get("led_signDate");
		if(StringUtils.isNotBlank(etime)){
			etime = etime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_signDate", etime);
		}
		StringBuilder sql = new StringBuilder(getBaseSql());
		params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "pc.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbPoolSignInfoVO.class, null, false, params);
	}
	
	/**
	 * 根据ID获取票据池签约信息
	 * @param id
	 * @return
	 */
	public List<AbPoolSignInfoVO> getAbPoolSignInfoVOById(String id) {
		if(StringUtils.isEmpty(id)){
			return null;
		}
		Map<String,Object> params=new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(getBaseSql());
        params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        sql.append(" and s.id=:id");
        params.put("id", id);
		return this.listBySQLAliasToBean(sql.toString(), params, AbPoolSignInfoVO.class);
	}
	
}
