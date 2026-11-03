package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolIntoRepeal;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolIntoRepealVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbPoolIntoRepealDao
 * @Description: 入池撤销Dao
 * @author zhenghj
 * @date 2021/11/08
 */
@Repository
public class AbPoolIntoRepealDao extends BaseDao<AbPoolIntoRepeal,String> {
    //待入池撤销的数据
    protected static StringBuilder beginHead(){
    	StringBuilder sql = new StringBuilder();
		sql.append("select ")
			.append("R.ID AS ID,PD.ID AS apdId,s.id as aid,null as accId,")
			.append("V.ID AS vouId, P.ID AS viceId, V.BILL_TYPE AS billType,")
			.append("r.bp_id as bpId, pd.brief_id as briefId, apc.CORP_ID AS corpId, V.BILL_FLAG AS billFlag,")
			.append("V.PAYER AS payer, V.ACCEPTOR AS acceptor, V.ORIG_NO AS origNo, V.BILL_MONEY AS billMoney,")
			.append("V.START_DATE AS startDate, V.END_DATE AS endDate,PD.INOUT_TYPE as inoutType,pd.CREATE_TIME as pdCreateTime,PD.APP_NO AS appNo,")
			.append("V.PAYEE AS payee, 0 as status,")
			//.append("null as optStatus, sc.code as corpCode, sc.name as corpName, ap.bp_Name as abPoolName,ap.id as abPoolId,")
			.append("null as optStatus, apc.corp_code as corpCode, apc.corp_name as corpName, ap.bp_Name as abPoolName,ap.id as abPoolId,")
			//.append("bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus, null as createTime, bi.bif_code as bifCode,")
			.append("bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus, null as createTime,")
			.append("p.BACK_BRIEF_ID as backBriefId, pbe.re_msg as returnMsg,")
			//.append("ba.circulation_Mode as circulationMode,p.bank_Acc as bankAcc,p.bank_Code as bankCode ")
			.append("P.circulation_Mode as circulationMode,p.bank_Acc as bankAcc,p.bank_Code as bankCode ")
			.append(" from ab_pool_into_repeal R")
			.append(" LEFT JOIN AB_VOUCHERINFO V ON V.Orig_No = r.orig_no")
			.append(" LEFT JOIN ab_pool_dtl PD ON R.ORIG_NO=PD.ORIG_NO")
			.append(" LEFT JOIN AB_ACCRECORDS S ON S.VID=V.ID AND S.PD_ID=PD.ID")
			.append(" LEFT JOIN AB_ACCRECORDS S2  ON s2.lastid=s.id and S2.BRIEF_ID='8032' ")
			.append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON P.ID = S.VVID")
			.append(" LEFT JOIN AB_POOL ap on r.bp_id = ap.id")
			.append(" LEFT JOIN ab_pool_corp apc on p.corp_id = apc.corp_id and apc.VALID_SIGN = 1")
			//.append(" LEFT JOIN sys_corp sc on apc.corp_id = sc.id")
			//.append(" LEFT JOIN BT_BANK_ACC ba on ba.bank_acc = P.bank_acc")
			//.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code")
			.append(" LEFT JOIN ab_brief_status bs on bs.status = s.brief_id")
			.append(" LEFT JOIN ab_pool_bank_exc pbe on pbe.id = r.id")
			.append(" where 1=1 ");
	   return sql;
    }
    //入池撤销的数据
    protected static StringBuilder hisHead(boolean isApprove){
    	StringBuilder sql =  new StringBuilder().append(" select ")
				.append("R.ID AS ID,PD.ID AS apdId,null as aid,s2.id as accId,")
				.append("V.ID AS vouId, P.ID AS viceId, V.BILL_TYPE AS billType,")
				.append("r.bp_id as bpId, pd.brief_id as briefId, apc.CORP_ID AS corpId, V.BILL_FLAG AS billFlag,")
				.append("V.PAYER AS payer, V.ACCEPTOR AS acceptor, V.ORIG_NO AS origNo, V.BILL_MONEY AS billMoney,")
				.append("V.START_DATE AS startDate, V.END_DATE AS endDate,PD.INOUT_TYPE as inoutType,pd.CREATE_TIME as pdCreateTime,PD.APP_NO AS appNo,")
				.append("V.PAYEE AS payee, (case when P.BACK_BRIEF_ID = '8032' then -1 when pd.status = -2 then  0 else bt_nvl_num( pd.status, 0) end) as status,")
				//.append("pd.opt_status as optStatus, sc.code as corpCode, sc.name as corpName, ap.bp_Name as abPoolName,ap.id as abPoolId,")
				.append("pd.opt_status as optStatus, apc.corp_code as corpCode, apc.corp_name as corpName, ap.bp_Name as abPoolName,ap.id as abPoolId,")
				//.append("bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus, s2.create_date as createTime, bi.bif_code as bifCode,")
				.append("bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus, s2.create_date as createTime, ")
				.append("p.BACK_BRIEF_ID as backBriefId, pbe.re_msg as returnMsg,")
				//.append("ba.circulation_Mode as circulationMode,p.bank_Acc as bankAcc,p.bank_Code as bankCode ");
				.append("p.circulation_Mode as circulationMode,p.bank_Acc as bankAcc,p.bank_Code as bankCode ");
		if(isApprove) {
			sql.append(",pd.ID as businessKey ");
		}
		sql.append(" from ab_pool_dtl PD ")
				.append(" left join ab_pool_into_repeal R  ON R.ID = PD.REPEAL_ID ")
				.append(" LEFT JOIN AB_VOUCHERINFO V  ON V.Orig_No = r.orig_no")
				.append(" LEFT JOIN AB_ACCRECORDS S2 ON S2.VID = V.ID AND S2.PD_ID = PD.ID")
				.append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON P.ID = S2.VVID")
				.append(" LEFT JOIN AB_POOL ap on r.bp_id = ap.id")
				.append(" LEFT JOIN ab_pool_corp apc on p.corp_id = apc.corp_id and apc.VALID_SIGN = 1")
				//.append(" LEFT JOIN sys_corp sc on apc.corp_id = sc.id")
				//.append(" LEFT JOIN BT_BANK_ACC ba  on ba.bank_acc = P.bank_acc")
				//.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code")
				.append(" LEFT JOIN ab_brief_status bs on bs.status = s2.brief_id")
				.append(" LEFT JOIN ab_pool_bank_exc pbe on pbe.id = r.id")
				.append(" where 1=1 ");
	      return sql;
    }
    
	public DataTablesOutput listAbPoolIntoRepeal(DataTablesInput input, List<String> corpAuthList) {
        Map<String,Object> params= new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder();
        sql.append(" select p.* from (");
        //可操作数据
        sql.append(AbPoolIntoRepealDao.beginHead());
        sql.append(" AND pd.brief_id in (:briefId) AND s.brief_id in (:briefId8018) and PD.opt_status = :optStatus18 AND PD.VALID_SIGN = :validSign1 AND R.VALID_SIGN = :validSign1 and PD.status = :status95 and S2.ID IS NULL ");
		sql.append("  union all ");
		//历史数据
		sql.append(AbPoolIntoRepealDao.hisHead(false));
		sql.append("  and ((pd.status != :statusApd and r.valid_sign=:validSign2) or (pd.status in (:statusApd2) and r.valid_sign=:validSign1)) ");
		sql.append("  AND pd.BRIEF_ID = :briefI8032 ");
		sql.append(") p ");
		sql.append(" where 1 = 1");
		params.put("briefId", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018, AbPoolConstant.AB_POOL_BRIEF_018});
		params.put("briefId8018", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018});
		params.put("validSign1", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		params.put("validSign2", AbPoolConstant.AB_POLL_VALID_SIGN_2);
		params.put("optStatus18", AbPoolConstant.AB_POOL_OPT_STATUS18);
		params.put("status95", AbPoolConstant.AB_POOL_STATUS95);
		params.put("statusApd",  AbPoolConstant.AB_POOL_OPT_STATUS_2);
		params.put("statusApd2",  new Integer[]{AbPoolConstant.AB_POOL_STATUS_2, AbPoolConstant.AB_POOL_STATUS11, AbPoolConstant.AB_POOL_STATUS_1, AbPoolConstant.AB_POOL_STATUS95});
		params.put("briefI8032", AbPoolConstant.AB_POOL_BRIEF_8032);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "p.corpId", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolIntoRepealVO.class, "", false,params);
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
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
        StringBuilder sql = new StringBuilder();
        sql.append(AbPoolIntoRepealDao.hisHead(true));
        Map<String,Object> params = new HashMap<String,Object>();
        String businessSql = SqlUtil.getInSqlByParams(businessList, "pd.id", params);
        sql.append(" and ").append(businessSql).append(" ");
        return getDynamicBySQL(input, sql.toString(), AbPoolIntoApplyVO.class, "", false,params);
    }
    
    public List<AbPoolIntoRepealVO>  queryAbPoolIntoRepealVoById(List rid){
		Map<String,Object> params= new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder();
		sql.append(" select p.* from (");
		//可操作数据
		sql.append(AbPoolIntoRepealDao.beginHead());
		sql.append(" AND pd.brief_id in (:briefId) AND s.brief_id in (:briefId8018) and PD.opt_status = :optStatus18 AND PD.VALID_SIGN = :validSign1 AND R.VALID_SIGN = :validSign1  and PD.status = :status95 and S2.ID IS NULL ");
		sql.append("  union all ");
		//历史数据
		sql.append(AbPoolIntoRepealDao.hisHead(false));
		sql.append("  and ((pd.status != :statusApd and r.valid_sign=:validSign2) or (pd.status in (:statusApd2) and r.valid_sign=:validSign1)) ");
		sql.append("  AND pd.BRIEF_ID = :briefI8032 ");
		sql.append(") p ");
		sql.append(" where 1 = 1");
		sql.append(" and p.id in (:rid)");
		params.put("briefId", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018, AbPoolConstant.AB_POOL_BRIEF_018});
		params.put("briefId8018", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018});
		params.put("validSign1", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		params.put("validSign2", AbPoolConstant.AB_POLL_VALID_SIGN_2);
		params.put("optStatus18", AbPoolConstant.AB_POOL_OPT_STATUS18);
		params.put("status95", AbPoolConstant.AB_POOL_STATUS95);
		params.put("statusApd", AbPoolConstant.AB_POOL_OPT_STATUS_2);
		params.put("statusApd2",  new Integer[]{AbPoolConstant.AB_POOL_STATUS_2, AbPoolConstant.AB_POOL_STATUS11, AbPoolConstant.AB_POOL_STATUS_1, AbPoolConstant.AB_POOL_STATUS95});
		params.put("briefI8032", AbPoolConstant.AB_POOL_BRIEF_8032);
		params.put("rid", rid);
        List<AbPoolIntoRepealVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolIntoRepealVO.class);
        return listInfo;
    }
	public List<AbPoolIntoRepealVO>  queryAbPoolIntoRepealVoByApdId(List<String> apdId){
		Map<String,Object> params= new HashMap<String,Object>();
		StringBuilder sql=new StringBuilder();
		sql.append(" select p.* from (");
		//可操作数据
		sql.append(AbPoolIntoRepealDao.beginHead());
		sql.append(" AND pd.brief_id in (:briefId) AND s.brief_id in (:briefId8018) and PD.opt_status = :optStatus18 AND PD.VALID_SIGN = :validSign1 AND R.VALID_SIGN = :validSign1 and PD.status = :status95 and S2.ID IS NULL ");
		sql.append("  union all ");
		//历史数据
		sql.append(AbPoolIntoRepealDao.hisHead(false));
		sql.append("  and ((pd.status != :statusApd and r.valid_sign=:validSign2) or (pd.status in (:statusApd2) and r.valid_sign=:validSign1)) ");
		sql.append("  AND pd.BRIEF_ID = :briefI8032 ");
		sql.append(") p ");
		sql.append(" where 1 = 1");
		sql.append(" and p.apdId in (:apdId)");
		params.put("briefId", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018, AbPoolConstant.AB_POOL_BRIEF_018});
		params.put("briefId8018", new String[]{AbPoolConstant.AB_POOL_BRIEF_8018});
		params.put("validSign1", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		params.put("validSign2", AbPoolConstant.AB_POLL_VALID_SIGN_2);
		params.put("optStatus18", AbPoolConstant.AB_POOL_OPT_STATUS18);
		params.put("status95", AbPoolConstant.AB_POOL_STATUS95);
		params.put("statusApd", AbPoolConstant.AB_POOL_OPT_STATUS_2);
		params.put("statusApd2",  new Integer[]{AbPoolConstant.AB_POOL_STATUS_2, AbPoolConstant.AB_POOL_STATUS11, AbPoolConstant.AB_POOL_STATUS_1, AbPoolConstant.AB_POOL_STATUS95});
		params.put("briefI8032", AbPoolConstant.AB_POOL_BRIEF_8032);
		params.put("apdId", apdId);
		List<AbPoolIntoRepealVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolIntoRepealVO.class);
		return listInfo;
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
