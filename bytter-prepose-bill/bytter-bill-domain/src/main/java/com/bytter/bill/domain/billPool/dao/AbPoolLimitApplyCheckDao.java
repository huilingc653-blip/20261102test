package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitApplyCheck;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbPoolLimitApplycheckService
 * @Description: 入池申请额度核准
 * @author jinyong.cao
 * @date 2020/2/21 00:07:38
 *
 */
@Repository
public class AbPoolLimitApplyCheckDao extends BaseDao<AbPoolLimitApplyCheck,String>{
	protected static StringBuilder staticSQL(boolean isApprove) {
		StringBuilder sql = new StringBuilder().append("select ap.id as id,")
				.append(" ap.limt_type as limtType,ap.apply_money as applyMoney,ap.END_DATE as endDate,ap.rmk as rmk,")
				.append(" ap.CREATE_TIME as  createTime,")
				.append(" apc.corp_name as apcName,apc.BUSTYPE_LEVEL as corpLevel,check_Type as checkType,")
				// .append(" (select cs.name From AB_POOL_CORP cp left join sys_corp cs on cs.id=cp.CORP_ID where cp.id=apc.PARENT_ID) as parentCorpName,")
				.append(" (select cp.corp_name From AB_POOL_CORP cp where cp.id=apc.PARENT_ID) as parentCorpName,")
				.append(" apb.BUSTYPE_CODE as  busTypeCode,apb.BUSTYPE_NAME as busTypeName,")
				.append(" (select ab.bustype_name from AB_POOL_BUSTYPE ab where ab.id=apb.parent_id) as parentBusName,")
				.append(" apl.BP_NAME as bpName,ap.OPT_STATUS as opStatus,ap.STATUS as status,apbe.re_msg as returnMsg  ")
				.append(" ,apc.corp_code as corpCode,apc.corp_name as corpName ");
		if (isApprove) {
			sql.append(",ap.id as businessKey");
		}
		sql.append(" From  ")
				.append(" AB_POOL_LIMIT_APPLYCHECK ap")
				.append(" Left Join AB_POOL_BUSTYPE apb on apb.id=ap.bustype_id")
				.append(" left join AB_POOL_CORP apc on  apc.CORP_ID=ap.CORP_ID  and apc.VALID_SIGN=1")
				// .append(" left join SYS_CORP s on s.id=apc.CORP_ID")
				.append(" left join AB_POOL apl on apl.id=ap.BP_ID")
				.append(" LEFT JOIN ab_pool_bank_exc apbe ON ap.ID=apbe.id ")
				.append(" WHERE (ap.status<>-2 or ap.status is null) ");
		return sql;
	}

	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbPoolLimitApplyCheckVO> listAbPoolIntoApplyCheck(DataTablesInput input, List<String> corpAuthList, String userId) {
		StringBuilder sql = new StringBuilder().append(staticSQL(false));
		Map<String, Object> params = new HashMap<String, Object>();
		// 拼接申请日期
		Map<String, Object> formdata = input.getFormdata();
		String transDate = (String) formdata.get("led_transDate");
		// 手动拼接时间条件，时间戳类型
		if (StringUtils.isNotEmpty(transDate)) {
			transDate += " 23:59:59";
			formdata.put("led_transDate", transDate);
			input.setFormdata(formdata);
		}
        String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "ap.CORP_ID", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolLimitApplyCheckVO.class, null, false, params);
	}
	    
	/**
	 * 根据ids批量修改流水表数据
	 * 
	 * @param params
	 * @return
	 */
	public int updateRecordByParams(Map<String, Object> params) {
		StringBuilder hql = new StringBuilder("update AbPoolLimitApplyCheck " + " set " + "updateTime = :updateTime ");
		if (params.containsKey("updateId")) {
			hql.append(",updateId = :updateId ");
		}
		if (params.containsKey("status")) {
			hql.append(",status = :status ");
		}
		if (params.containsKey("optStatus")) {
			hql.append(",optStatus = :optStatus ");
		}
		if (params.containsKey("checkType")) {
			if(params.get("checkType").toString().equals("2")){
				hql.append(",checkType =null ");
			}else{
				hql.append(",checkType = :checkType ");
			}
		}
		if (params.containsKey("checkMsg")) {
			hql.append(",checkMsg = :checkMsg ");
		}
		if (!params.containsKey("ids")) {
			return 0;
		}
		hql.append(" where 1=1 ");
		if (params.containsKey("ids")) {
			hql.append(" and ").append(SqlUtil.getInSqlByParams("ids","id",params));
		}
		return this.batchExecute(hql.toString(), params);
	}
	
	/**
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
        StringBuilder sql = new StringBuilder(staticSQL(true));
        Map<String,Object> params = new HashMap<>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "ap.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolLimitApplyCheckVO.class, "", false,params);
    }
    
    public AbPoolLimitApplyCheckVO queryAbPoolLimitApplyCheckVoById(String id){
    	StringBuilder sql = new StringBuilder(staticSQL(false));
        sql.append(" and ap.id = :id");
        Map<String, Object> params = new HashMap<>();
        params.put("id",id);
        List<AbPoolLimitApplyCheckVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolLimitApplyCheckVO.class);
        return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
    }
	/**
	 * 根据单位id获取此单位下有没有处理中的数据
	 * @param corpId
	 * @return 有效额度申请复核
	 */
	public int getPoolLimitApplyCheckByCorpId(String corpId){
		StringBuilder sql = new StringBuilder(" select count(*) from AB_POOL_LIMIT_APPLYCHECK ");
		sql.append(" where corp_id=:corpId and (STATUS in (:status) or OPT_STATUS in (:optStatus))");
		Map<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		params.put("status", new Integer[]{AbPoolConstant.AB_POOL_STATUS0, AbPoolConstant.AB_POOL_STATUS11});
		params.put("optStatus", new String[]{AbPoolConstant.AB_POOL_OPT_STATUS16, AbPoolConstant.AB_POOL_OPT_STATUS18});
		return ((Number)this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue();
	}
	    
}
