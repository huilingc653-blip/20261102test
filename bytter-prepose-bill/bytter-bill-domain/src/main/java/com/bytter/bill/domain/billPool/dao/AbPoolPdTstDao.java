package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolPdTstVO;
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
 * 
　 * @Title: 质押托管转换DAO
　 * @author jy.cao
  * @Description:AbPoolPdTstDao.java
　 * @date 2020年3月5日
　 * @version 1.0
 */
@Repository
public class AbPoolPdTstDao extends BaseDao<AbPoolPdTstVO,String>{
	protected static StringBuilder mainEntrance(boolean isApprove){
		StringBuilder sql = new StringBuilder().append("select ")
		.append(" V.ID AS vouId,P.ID AS viceId, apd.id as apdId,")
		.append(" V.BILL_TYPE AS billType,  apd.bp_Id as bpId,apd.BRIEF_ID as briefId,")
		.append(" P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,")
        .append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,")
		.append(" (case when p.BACK_BRIEF_ID =8118 then -1 when apd.brief_id='8018' and apd.valid_sign=1 then 0 else apd.status end) as status,")
		.append(" (case when apd.brief_id='8018' and apd.valid_sign=1 then  null else  apd.opt_status  end) as optStatus,P.back_brief_id as backBriefId,")
		//.append(" sc.code as corpCode, sc.name as corpName,apd.valid_sign as validSign,")
		.append(" apc.corp_code as corpCode, apc.corp_name as corpName,apd.valid_sign as validSign,")
		.append(" apd.INOUT_TYPE as inoutType, ap.bp_Name as abPoolName,bt_nvl_char(P.POOL_STATUS, 0) as poolSatatus,apd.create_Time as createTime");
        if(isApprove) {
    	    sql.append(",apd.id as businessKey");
	    }   
        sql.append(" from AB_POOL_DTL apd")
        .append(" LEFT JOIN AB_VOUCHERINFO_VICE P ON apd.vv_id=p.id")
        .append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID")
        .append(" LEFT JOIN AB_POOL ap on apd.bp_id = ap.id")
        .append(" left join ab_pool_corp apc on p.corp_id = apc.corp_id  and apc.VALID_SIGN=1")
        //.append(" left join sys_corp sc on apc.corp_id = sc.id ")
        .append(" where 1=1 ");
        return sql;
	}
	
	public DataTablesOutput listAbPoolTstApply(DataTablesInput input, List<String> corpAuthList) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder(AbPoolPdTstDao.mainEntrance(false));
		sql.append(" and  bt_nvl_char(P.POOL_STATUS, 0) = 1");
		//过滤掉锁定的数据
		sql.append(" and p.VALID_SIGN=1 ");
		sql.append(" and ((apd.BRIEF_ID=8018 and apd.valid_sign=1)  or (apd.BRIEF_ID in (8218,8118) and apd.valid_sign in(0,1)) )");
		//待处理
		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName")) 
				&& input.getFormdata().get("eqs_statusName").equals(AbPoolConstant.AB_POOL_IS_INTO_0)){
			 sql.append("  and apd.BRIEF_ID=8018 and apd.valid_sign=1");
			 input.getFormdata().remove("eqs_statusName");
		}
		//审批通过
		if(StringUtils.isNotEmpty(input.getFormdata().get("eqs_statusName")) 
				&& input.getFormdata().get("eqs_statusName").equals(String.valueOf(AbPoolConstant.AB_POOL_STATUS95))){
			 sql.append("  and apd.BRIEF_ID in (8019,8218,8118)  and apd.status=95");
			 input.getFormdata().remove("eqs_statusName");
		}
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(), AbPoolPdTstVO.class, "", false, params);
	}
	
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
		StringBuilder sql = new StringBuilder(AbPoolPdTstDao.mainEntrance(true));
		Map<String, Object> params = new HashMap<String, Object>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "apd.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		params.put("cValidSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
		return this.getDynamicBySQL(input, sql.toString(), AbPoolPdTstVO.class, "", false, params);
	}
	
	
	public List<AbPoolPdTstVO> queryTstInfoList(String vvid,String bpId){
		StringBuilder sql = new StringBuilder(AbPoolPdTstDao.mainEntrance(true));
		sql.append(" AND apd.BRIEF_ID in(:briefId)");
        sql.append(" and apd.vv_id = :vvid");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vvid",vvid);
        params.put("briefId", new String[]{
				AccebillConstant.AB_POOL_BRIEF_8218,
				AbPoolConstant.AB_POOL_BRIEF_8118
		});
        params.put("cValidSign", AbPoolConstant.AB_POLL_VALID_SIGN_1);
        List<AbPoolPdTstVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolPdTstVO.class);
		return listInfo;
	}
	
}
