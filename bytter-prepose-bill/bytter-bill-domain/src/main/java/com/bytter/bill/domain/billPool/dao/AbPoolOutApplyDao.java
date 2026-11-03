package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolOutApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
　 * @Title: 出池申请DAO
　 * @author jy.cao
  * @Description:AbPoolOutApplyDao.java
　 * @date 2020年2月28日
　 * @version 1.0
 */
@Repository
public class AbPoolOutApplyDao extends BaseDao<AbPoolOutApplyVO,String>{
	protected static StringBuilder masterSql(boolean isApprove){
		StringBuilder sql = new StringBuilder().append("select ")
				.append(" V.ID AS vouId,P.ID AS viceId, apd.id as apdId,")
				.append(" V.BILL_TYPE AS billType,  apd.bp_Id as bpId,apd.BRIEF_ID as briefId,")
				.append(" P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,")
		        .append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,")
				.append(" (case when P.BACK_BRIEF_ID = '8019' and apd.brief_id != '8019' then -1 when apd.brief_id != '8019' then 0 else apd.status end) as status,")
				.append(" (case when apd.brief_id = '8019' then apd.opt_status else null end) as optStatus,pbe.re_msg as returnMsg,P.back_brief_id as backBriefId,")
				//.append(" sc.code as corpCode, sc.name as corpName,apd.valid_sign as validSign,")
				.append(" apc.corp_code as corpCode, apc.corp_name as corpName,apd.valid_sign as validSign,")
				.append(" apd.INOUT_TYPE as inoutType, ap.bp_Name as abPoolName,bt_nvl_char(P.POOL_STATUS, '0') as poolSatatus,apd.create_Time as createTime");
		        if(isApprove) {
		    	    sql.append(",apd.id as businessKey");
			    }   
		        sql.append(" from AB_POOL_DTL apd")
		        .append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON apd.vv_id=p.id")
		        .append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID")
		        .append(" LEFT JOIN AB_POOL ap on apd.bp_id = ap.id")
		        .append(" left join ab_pool_corp apc on p.corp_id = apc.corp_id  and apc.VALID_SIGN=1")
		        //.append(" left join sys_corp sc on apc.corp_id = sc.id ")
				.append(" LEFT JOIN ab_pool_bank_exc pbe on pbe.id = apd.id ")
		        .append(" where 1=1 ");
        return sql;
	}
	public DataTablesOutput listAbPoolOutApply(DataTablesInput input, List<String> corpAuthList) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.* from (");
		sql.append(AbPoolOutApplyDao.masterSql(false));
		sql.append(" and ( ");
		sql.append(" (bt_nvl_char(P.POOL_STATUS,:nvlStatus)=:poolStatus and apd.BRIEF_ID in (:briefId) and apd.valid_sign=:validSign and apd.status=:status and apd.opt_status=:optStatus )");
		sql.append("  or (apd.BRIEF_ID in (:oBriefId) and apd.status!=:statusDelete ) ");
		sql.append(" )");
		//过滤掉锁定的数据
		sql.append(" and p.VALID_SIGN=:validSign ");
		params.put("briefId", new String[]{
				AccebillConstant.AB_POOL_BRIEF_8018,
				AccebillConstant.AB_POOL_BRIEF_8218,
				AccebillConstant.AB_POOL_BRIEF_8118
				});
		params.put("nvlStatus", AbPoolConstant.AB_BILL_POOL_STATUS_0);
    	params.put("poolStatus", AbPoolConstant.AB_BILL_POOL_STATUS_1);
    	params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
    	params.put("validSignOver", AbPoolConstant.AB_POLL_VALID_SIGN_2);
    	params.put("status", AbPoolConstant.AB_POOL_STATUS95);
    	params.put("optStatus", AbPoolConstant.AB_POOL_OPT_STATUS19);
    	params.put("statusDelete", AbPoolConstant.AB_POOL_STATUS_2);
    	params.put("oBriefId", new String[]{
				AccebillConstant.AB_POOL_BRIEF_8019
				});
//		//待处理
//		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName"))
//				&& input.getFormdata().get("eqs_statusName").equals(AbPoolConstant.AB_POOL_IS_INTO_0)){
//			 sql.append("  and apd.BRIEF_ID in (:briefId) and apd.valid_sign=:validSign");
//			 input.getFormdata().remove("eqs_statusName");
//		}
//		//审批通过
//		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName"))
//				&& input.getFormdata().get("eqs_statusName").equals(String.valueOf(AbPoolConstant.AB_POOL_STATUS95))){
//			 sql.append("  and apd.BRIEF_ID in (:oBriefId)  and apd.status=:status");
//			 input.getFormdata().remove("eqs_statusName");
//		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", params);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" ) p ");
		sql.append(" where 1=1 ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolOutApplyVO.class, "", false, params);
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select p.* from (");
		sql.append(AbPoolOutApplyDao.masterSql(true));
		Map<String, Object> params = new HashMap<String, Object>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "apd.id", params);
		sql.append(" and ").append(businessSql);
		sql.append(" ) p ");
		sql.append(" where 1=1 ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolOutApplyVO.class, "", false, params);
	}
	
	
	public AbPoolOutApplyVO queryAbPoolOutApplyVOByBpid(String bpid){
    	StringBuilder sql = new StringBuilder(AbPoolOutApplyDao.masterSql(false));
        sql.append(" and apd.id =:bpid");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bpid",bpid);
        List<AbPoolOutApplyVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolOutApplyVO.class);
        return ListUtils.isNotNullOrEmpty(listInfo)?listInfo.get(0):null;
    }
}
