package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolFeginApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.guarantee.enums.ConMessageEnum;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 入池DAO
 */
@Repository
public class AbPoolIntoApplyDao extends BaseDao<AbPoolDtl,String> {
    protected static StringBuilder getSqlMain(boolean isApprove){
    	StringBuilder sql = new StringBuilder().append("select ")
		    			.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,apd.id as apdId,V.BILL_TYPE AS billType,apd.bp_Id as bpId,apd.BRIEF_ID as briefId,")
				        .append(" P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,")
				        .append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,")
				        .append(" (case when P.BACK_BRIEF_ID IS NOT NULL and apd.status <> 95  then -1 when  ap.bp_Name is null then 0 ")
				        .append(" when s.brief_id=8019 and s.valid_sign=1 and s.opt_status =19 and s.status=95 then 0 else apd.status  end )AS status,")
				        .append(" (case when s.brief_id=8019 and s.valid_sign=1 and s.opt_status =19 and s.status=95   then null when  ap.bp_Name is null then null else s.opt_status  end ) AS optStatus,")
				        .append(" sc.code as corpCode,sc.name as corpName,apd.INOUT_TYPE as inoutType,ap.bp_Name as abPoolName,")
				        .append(" bt_nvl_char(P.POOL_STATUS,0) as poolSatatus,apd.create_Time as createTime,bi.bif_code as bifCode,p.BACK_BRIEF_ID as backBriefId");
		        if(isApprove) {
		        	    sql.append(",apd.id as businessKey");
				}
         return sql;
    }
    
    //待入池的数据
    protected static StringBuilder beginHead(){
    	StringBuilder sql = new StringBuilder();
		sql.append("select ")
    	    .append("  S.ID AS ID,'1' as bifType,V.ID AS vouId,P.ID AS viceId,V.BILL_TYPE AS billType,null as bpId,")
    	    .append("  null as briefId,apc.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,")
    	    .append("  V.ACCEPTOR AS acceptor, V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,")
    	    .append("  V.END_DATE AS endDate, V.PAYEE AS payee, (case when P.BACK_BRIEF_ID = :briefI8018 then -1 else 0 end) as status , null as optStatus,")
			//.append("  sc.code as corpCode,sc.name as corpName,null as inoutType,null as abPoolId, null as abPoolName,bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus,")
    	    .append("  apc.corp_code as corpCode,apc.corp_name as corpName,null as inoutType,null as abPoolId, null as abPoolName,bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus,")
    	    //.append("  null as createTime,bi.bif_code as bifCode,p.BACK_BRIEF_ID as backBriefId")
    	    .append("  null as createTime,p.BACK_BRIEF_ID as backBriefId")
    	    //.append("  ,null returnMsg " )
    	    .append("  ,null returnMsg,P.bank_acc as bankAcc " )
			.append(" from AB_ACCRECORDS S")
			.append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON P.ID=S.VVID")
			.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID")
			.append(" LEFT join ab_pool_corp apc on p.corp_id = apc.corp_id  and apc.VALID_SIGN=1")
			//.append(" LEFT join sys_corp sc on apc.corp_id = sc.id ")
			//.append(" LEFT JOIN BT_BANK_ACC ba on ba.bank_acc = P.bank_acc  ")
			//.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code ")
			.append(" left join ab_brief_status bs on bs.status=s.brief_id ")
			.append(" where 1=1 ");
	   return sql;
    }
    
    //已入池的数据
    protected static StringBuilder hisHead(boolean isApprove){
    	StringBuilder sql =  new StringBuilder().append(" select")
	    		 .append("  apd.id as ID,'0' as bifType,V.ID AS vouId,P.ID AS viceId,V.BILL_TYPE AS billType,")
	    		 .append("  apd.bp_Id as bpId, apd.BRIEF_ID as briefId,P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,")
	    		 .append("  V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney, V.START_DATE AS startDate,")
	    		 .append("  V.END_DATE AS endDate, V.PAYEE AS payee,apd.status as status, apd.opt_status as optStatus,")
	    		 //.append("  sc.code as corpCode, sc.name as corpName,apd.INOUT_TYPE as inoutType,")
	    		 .append("  apc.corp_code as corpCode, apc.corp_name as corpName,apd.INOUT_TYPE as inoutType,")
	    		 //.append("  ap.id as abPoolId, ap.bp_Name as abPoolName,bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus,apd.create_Time as createTime,bi.bif_code as bifCode,")
	    		 .append("  ap.id as abPoolId, ap.bp_Name as abPoolName,bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus,apd.create_Time as createTime,")
	    		 .append("  p.BACK_BRIEF_ID as backBriefId,")
	    		 //.append("  pbe.re_msg returnMsg ")
	    		 .append("  pbe.re_msg returnMsg,P.bank_acc as bankAcc ");
    	     if(isApprove) {
		         sql.append(",apd.ID as businessKey");
			 }
	         sql.append(" from AB_POOL_DTL apd")
		        .append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON P.ID=apd.vv_id")
		        .append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID")
		        .append(" LEFT JOIN AB_POOL ap on apd.bp_id = ap.id")
		        .append(" LEFT join ab_pool_corp apc on p.corp_id = apc.corp_id  and apc.VALID_SIGN=1")
		        //.append(" LEFT join sys_corp sc on apc.corp_id = sc.id ")
		        //.append(" LEFT JOIN BT_BANK_ACC ba on ba.bank_acc = P.bank_acc  ")
		 		//.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code ")
		 		.append(" LEFT JOIN ab_pool_bank_exc pbe on pbe.id = apd.id ")
		        .append(" where 1=1 ");
	      return sql;
    }
    
    
	public DataTablesOutput listAbPoolIntoApply(DataTablesInput input, List<String> corpAuthList) {
        Map<String,Object> params= new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder();
        sql.append(" select p.* from (");
        //可操作数据
        sql.append(AbPoolIntoApplyDao.beginHead());
//    	sql .append(" AND S.BRIEF_ID in (:briefId)")
    	sql .append(" and bs.bus_type=:busType and (p.bill_source is null or p.bill_source!=:billSourcePledge) ")
	    	.append(" and s.opt_status=:optStatus AND s.VALID_SIGN =:validSign")
	    	.append(" and s.status=:aStatus")
	    	.append(" AND (P.POOL_STATUS is null or P.POOL_STATUS = :poolStatus) ")
	    	.append(" AND (S.BANKENDRSMTMK =:bankendrsmtmk or S.BANKENDRSMTMK is null)")
	    	//.append(" AND ((v.bill_flag =:billFlagY and ba.circulation_mode =:circulationModeY) or v.bill_flag =:billFlagN )  ")
	    	.append(" AND (v.bill_flag =:billFlagY  or v.bill_flag =:billFlagN )  ")
			//过滤掉锁定的数据
			.append(" and p.VALID_SIGN=:validSign1");
    	sql.append("  union all ");
    	//历史数据
        sql.append(AbPoolIntoApplyDao.hisHead(false));
//        sql.append("  and apd.lastid is null");
        sql.append("  and apd.status != :statusApd ");
        sql.append("  AND apd.BRIEF_ID = :briefI8018 ");
//        sql.append("  AND apd.BRIEF_ID = :briefI8018 AND apd.VALID_SIGN in (:validSign2)");
		sql.append(") p ");
		sql.append(" where 1 = 1");
    	params.put("busType", AccebillConstant.BRIEF_STATUS_6001);
    	params.put("billSourcePledge", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
    	params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
    	params.put("validSign1", AbPoolConstant.AB_POLL_VALID_SIGN_1);
    	params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
    	params.put("aStatus", AbPoolConstant.AB_POOL_STATUS95);
    	params.put("poolStatus", AbPoolConstant.AB_BILL_POOL_STATUS_0);
    	params.put("bankendrsmtmk", AccebillConstant.AB_BANEM_Y);
    	params.put("billFlagY", AccebillConstant.AB_BILL_FLAG_ELECT);
    	params.put("billFlagN", AccebillConstant.AB_BILL_FLAG_PAPER);
    	params.put("circulationModeY", AccebillConstant.AB_INSIDE_SING_Y);
    	params.put("circulationModeN", new String[]{
    			AbPoolConstant.AB_POLL_VALID_SIGN_0,
				AbPoolConstant.AB_POLL_VALID_SIGN_1
				});
    	params.put("briefI8018", AbPoolConstant.AB_POOL_BRIEF_8018);
    	params.put("statusApd", AbPoolConstant.AB_POOL_OPT_STATUS_2);
//    	params.put("validSign2", new String[]{
//    			AbPoolConstant.AB_POLL_VALID_SIGN_0,
//				AbPoolConstant.AB_POLL_VALID_SIGN_1
//				});
//		//待处理
//		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName"))
//				&& input.getFormdata().get("eqs_statusName").equals(AbPoolConstant.AB_POOL_IS_INTO_0)){
//			 sql.append("  and p.status = :status0");
//			 params.put("status0", AbPoolConstant.AB_POOL_STATUS0);
//			 input.getFormdata().remove("eqs_statusName");
//		}
//		//审批打回
//		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName"))
//				&& input.getFormdata().get("eqs_statusName").equals(AbPoolConstant.AB_POOL_OPT_STATUS_1)){
//			 sql.append(" and ( p.backBriefId is not null )");
//			 input.getFormdata().remove("eqs_statusName");
//		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "p.corpId", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolFeginApplyVO.class, "", false,params);
    }
	 /**
     * 修改流水信息
     * @param refMap
     * @param userVo
     * @return
     */
    public int updateRecordById(Map<String,Object> refMap, ICurrentUser userVo){
        if(null==refMap){
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params=new HashMap<>();
        String[] ids = {refMap.get("rid").toString()};
        params.put("ids", ids);
        
        if(refMap.containsKey("status")){
        	params.put("status", refMap.get("status"));
        }
        
        if(refMap.containsKey("optStatus")){
        	params.put("optStatus", refMap.get("optStatus"));
        }
        if(refMap.containsKey("updateId")){
        	params.put("updateId", refMap.get("updateId"));
        }
        if(refMap.containsKey("validSign")){
        	params.put("validSign", refMap.get("validSign"));
        }
        params.put("updateTime", new Date());
        return this.updateRecordByParams(params);
    }
	/**
	 *
	 * @param params
	 * @return
	 */
	public int updateRecordByParams(Map<String,Object> params){
        StringBuilder hql=new StringBuilder("update AbPoolDtl "
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
        if(params.containsKey("validSign")){
            hql.append(",validSign = :validSign");
        }
    	hql.append(" where 1=1 ");
    	if(params.containsKey("ids")){
    		hql.append(" and ").append(SqlUtil.getInSqlByParams("ids","id",params));
    	}
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 审批页面查询当前用户可审批的数据
     * @param input
     * @param businessList
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
        StringBuilder sql = new StringBuilder();
        sql.append(AbPoolIntoApplyDao.hisHead(true));
        Map<String,Object> params = new HashMap<String,Object>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "apd.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolIntoApplyVO.class, "", false,params);
    }
    
    public AbPoolIntoApplyVO queryAbPoolIntoApplyVOByBpid(String bpid){
    	StringBuilder sql = new StringBuilder();
	    sql.append(AbPoolIntoApplyDao.hisHead(false));
        sql.append(" and apd.id = :bpid");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bpid",bpid);
        List<AbPoolIntoApplyVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolIntoApplyVO.class);
        return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
    }
    
    /**
     * 根据单位id获取此单位中是否存在入票据池业务流程流水中
     * @param corpId
     * @param briefId
     * @return 存在笔数
     */
    public int getValidPoolDtlNumByCorpId(String corpId,String briefId){
    	StringBuilder sql  = new StringBuilder()
    		.append(" select count(*) from ab_pool_dtl apd")
    		.append(" left join ab_voucherinfo_vice vv on vv.id=apd.vv_id")
    		.append(" left join ab_pool_corp apc on vv.corp_id= apc.corp_id")
    		.append(" left join sys_corp sc on apc.corp_id = sc.id")
    		.append(" where 1=1 and vv.corp_id=:corpId and apd.brief_id=:briefId");
    	Map<String,Object> params=new HashMap<String,Object>();
    	params.put("corpId",corpId);
    	params.put("briefId",briefId);
    	return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
    }
    
}
