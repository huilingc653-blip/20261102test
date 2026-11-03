package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolLimitApply;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class AbPoolLimitApplyDao extends BaseDao<AbPoolLimitApply,String>{
	protected static StringBuilder staticSQL(){
		return new StringBuilder().append("select ap.id as id,")
			     .append(" ap.limt_type as limtType,ap.apply_money as applyMoney,ap.END_DATE as endDate,ap.rmk as rmk,")
			     .append(" ap.CREATE_TIME as  createTime,")
			     .append(" apc.CORP_NAME as apcName,apc.BUSTYPE_LEVEL as corpLevel,")
			     // .append(" (select cs.name From AB_POOL_CORP cp left join sys_corp cs on cs.id=cp.CORP_ID where cp.id=apc.PARENT_ID) as parentCorpName,")
			     .append(" cp.CORP_NAME as parentCorpName,")
			     .append(" apb.BUSTYPE_CODE as  busTypeCode,apb.BUSTYPE_NAME as busTypeName,")
			     // .append(" (select ab.bustype_name from AB_POOL_BUSTYPE ab where ab.id=apb.parent_id) as parentBusName,")
			     .append(" ab.bustype_name as parentBusName,")
			     .append(" apl.BP_NAME as bpName,ap.OPT_STATUS as opStatus,ap.STATUS as status,ap.id as businessKey  ")
			     .append(" ,apc.CORP_CODE as corpCode,apc.CORP_NAME as corpName,apbe.re_msg as returnMsg ")
			     .append(" From  ")
			     .append(" AB_POOL_LIMIT_APPLY ap")
			     .append(" Left Join AB_POOL_BUSTYPE apb on apb.id=ap.bustype_id")
			     .append(" left join AB_POOL_CORP apc on  apc.CORP_ID=ap.CORP_ID  and apc.VALID_SIGN=1")
			     .append(" left join AB_POOL_CORP cp on cp.id=apc.PARENT_ID ")
			     .append(" left join AB_POOL_BUSTYPE ab on ab.id=apb.parent_id ")
			     .append(" left join AB_POOL apl on apl.id=ap.BP_ID")
				 .append(" LEFT JOIN ab_pool_bank_exc apbe ON ap.ID=apbe.id ")
				 .append(" WHERE ap.status<>-2 ");
	}
	 @SuppressWarnings("rawtypes")
	public DataTablesOutput<AbPoolLimitApplyVO> listAbPoolLimitApply(DataTablesInput input,
			List<String> corpAuthList, String userId) {
		StringBuilder sql = new StringBuilder().append(staticSQL());
		Map<String, Object> params = new HashMap<String, Object>();
		//拼接申请日期
		Map<String, Object> formdata = input.getFormdata();
		String transDate = (String) formdata.get("led_transDate");
		// 手动拼接时间条件，时间戳类型
		if (StringUtils.isNotEmpty(transDate)) {
			transDate += " 23:59:59";
			formdata.put("led_transDate", transDate);
			input.setFormdata(formdata);
		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolLimitApplyVO.class, null, false, params);
	}
	 
	 
	/**
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbPoolLimitApplyVO> dataTableApprove(DataTablesInput input, List<String> businessList) {
		StringBuilder sql = new StringBuilder(staticSQL());
		Map<String, Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "ap.id",
				params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbPoolLimitApplyVO.class, "", false, params);
	}
	 
	 
	 
	/**
	 * 批量修改操作
	 * 
	 * @param rid
	 *            流水id
	 * @return
	 */
	public int updateabPoolLimitApplyById(String rid, Integer status,String optStatus,
			ICurrentUser userVo) {
		if (StringUtils.isEmpty(rid)) {
			throw new BusinessException(
					ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
		}
		Map<String, Object> params = new HashMap<>();
		String[] ids = { rid };
		params.put("ids", ids);
		if (status != null) {
			params.put("status", status);
		}
		if (optStatus != null) {
			params.put("optStatus", optStatus);
		}
		if (userVo != null) {
			params.put("updateId", userVo.getId());
		}
		params.put("updateTime", new Date());
		return this.updateRecordByParams(params);
	}
	 
	
	
	  /**
     * 根据ids批量修改流水表数据
     * @param params
     * @return
     */
    public int updateRecordByParams(Map<String,Object> params){
        StringBuilder hql=new StringBuilder("update AbPoolLimitApply "
                + " set "
                + "updateTime = :updateTime ");
        if(params.containsKey("updateId")){
            hql.append(",updateId = :updateId ");
        }
        if(params.containsKey("status")){
            hql.append(",status = :status ");
        }
        if(params.containsKey("optStatus")){
            hql.append(",optStatus = :optStatus ");
        }
        if(!params.containsKey("ids")){
        	return 0;
        }
    	hql.append(" where 1=1 ");
    	if(params.containsKey("ids")){
    		hql.append(" and ").append(SqlUtil.getInSqlByParams("ids","id",params));
    	}
        if(params.containsKey("status")){
            hql.append(" and status<>:status ");
        }
        return this.batchExecute(hql.toString(), params);
    }
    
    
    public  List<AbPoolLimitApplyVO> getAbPoolLimitApplyVoById(String id) {
    	StringBuilder sql = new StringBuilder(staticSQL());
        sql.append(" and ap.id = :id  ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",id);
		return this.listBySQLAliasToBean(sql.toString(), params, AbPoolLimitApplyVO.class);
    }
    
    
    /**
     * 根据单位id获取此单位中是否存在额度
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidLimitNumByCorpId(String corpId){
        String sql="select count(*) from AB_POOL_LIMIT_APPLY where 1=1  and corp_id=:corpId";
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
	/**
	 * 根据单位id获取此单位下有没有处理中的数据
	 * @param corpId
	 * @return 有效额度申请
	 */
	public int getPoolLimitApplyByCorpId(String corpId){
		StringBuilder sql= new StringBuilder("select count(*) from AB_POOL_LIMIT_APPLY where corp_id=:corpId ");
		sql.append("and (STATUS in (:status) or opt_Status in (:optStatus))");
		Map<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		params.put("status", new Integer[]{AbPoolConstant.AB_POOL_STATUS0, AbPoolConstant.AB_POOL_STATUS11});
		params.put("optStatus", new String[]{AbPoolConstant.AB_POOL_OPT_STATUS16, AbPoolConstant.AB_POOL_OPT_STATUS18});
		return ((Number)this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue();
	}
}
