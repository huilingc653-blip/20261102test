package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolSignVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
　 * @Title: 票据池签收
　 * @author jy.cao
  * @Description:AbPoolSignDao.java
　 * @date 2020年2月26日
　 * @version 1.0
 */
@Repository
public class AbPoolSignDao extends BaseDao<AbPoolDtl,String>{
	/**
	 * 获取查询的sql语句
	 *
	 * @return
	 */
	private StringBuilder getQuerySql(boolean isApprove) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" p.id as recId,apd.id as pdId,V.ID AS vouId,vv.id AS vvId,P.BILL_TYPE AS billType,");
		sql.append(" p.PAYER AS payer,p.ACCEPTOR AS acceptor,p.ORIG_NO AS origNo,a.id as accId,");
		sql.append(" p.BILL_MONEY AS billMoney,p.START_DATE AS startDate,p.END_DATE AS endDate,p.PAYEE AS payee,");
		//sql.append(" apd.opt_status AS optStatus,sc.code as corpCode,sc.name as corpName,apd.INOUT_TYPE as inoutType,");
		sql.append(" apd.opt_status AS optStatus,apc.corp_code as corpCode,apc.corp_name as corpName,apd.INOUT_TYPE as inoutType,");
		//sql.append(" ap.bp_Name as abPoolName,apd.create_Time as createTime,P.REC_CORP_ID AS corpId,bi.bif_code as bifCode,");
		sql.append(" ap.bp_Name as abPoolName,apd.create_Time as createTime,P.REC_CORP_ID AS corpId,");
		sql.append(" P.rec_acct_id as bankAcc,");
		if (isApprove) {
			sql.append("(case  when apd.status is null then 0 else apd.status end) AS status,");
			sql.append("apd.id as id,apd.id as businessKey ");
		}else{
			sql.append("(case  when apd.status is null then 0 when apd.opt_status='-2' then 0 else apd.status end) AS status,");
			sql.append("P.ID AS id ");
		}
		sql.append(" from AB_BILL_RECEIVEBIZ P  ");
		sql.append(" left join ab_accrecords a on a.recid=p.id and a.opt_status not in ('"+AccebillConstant.AB_BILL_OPT_STATUS_2+"') ");
		sql.append(" left join ab_pool_dtl apd on apd.id=a.pd_id ");
		sql.append(" left join ab_pool_corp apc on p.REC_CORP_ID = apc.corp_id and APC.valid_sign=1 ");
		//sql.append(" left join sys_corp sc on apc.corp_id = sc.id   ");
		sql.append(" left join AB_VOUCHERINFO_VICE vv on apd.vv_id=vv.id ");
		sql.append(" left join AB_VOUCHERINFO v on vv.vid =v.id ");
		sql.append(" LEFT JOIN AB_POOL ap on apd.bp_id = ap.id  ");
		//sql.append(" LEFT JOIN BT_BANK_ACC ba on ba.bank_acc = P.rec_acct_id  ");
		//sql.append(" LEFT JOIN BIS_BIF_INIT bi on bi.id = ba.bif_code ");
		return sql;
	}
	
	/**
	 * 查询待签收数据
	 * @param input
	 * @param corpAuthList
	 * @return
	 */
	public DataTablesOutput listAbPoolSignTab(DataTablesInput input, List<String> corpAuthList) {
		Map<String,Object> params= new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySql(false));
		sql.append(" where 1=1 ");
		sql.append(" and p.bankendrsmtmk=:bankendrsmtmk");
		//票据状态为18待签收
		StringBuilder sqlWhere = new StringBuilder(" (P.STATUS IN(:STATUS) and (apd.valid_sign<>:validSign or apd.valid_sign is null)) ");
		sql.append(" and ( " +sqlWhere);
		sql.append(" or ");
		//签收完成
		sql.append(" (P.STATUS IN(:STATUS2) and apd.status not in (:pstatus) and apd.valid_sign<>:validSign)) ");
		//票据签收业务为：003提示收票010背书申请012回购式贴现赎回019质押解除
		sql.append(" AND P.MSGTYPE IN (:MSGTYPE) ");
		params.put("STATUS", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING});
		params.put("STATUS2", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK});
		params.put("validSign", AbPoolConstant.AB_POLL_VALID_SIGN_2);
		params.put("pstatus", new Integer[]{AbPoolConstant.AB_POOL_STATUS_2});
		params.put("MSGTYPE", new String[]{
				AccebillConstant.AB_MSGTYPE_RECBILL,
				AccebillConstant.AB_MSGTYPE_RECITE,
				AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
				AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE});
		params.put("bankendrsmtmk", AccebillConstant.AB_BANEM_Y);
		//处理审批状态多选查询
		StringBuilder sqlStatus = new StringBuilder(" ");
		if (StringUtils.isNotEmpty(input.getFormdata().get("ins_statusName"))){
			List<String> statusList = Arrays.asList(input.getFormdata().get("ins_statusName").toString().split(","));
			sqlStatus.append(" and ( ");
			for(int i=0; i<statusList.size();i++){
				if(i!=0){sqlStatus.append(" or ");}
				//查询待处理数据
				if(statusList.get(i).equals(String.valueOf(AbPoolConstant.AB_POOL_STATUS0))){
					sqlStatus.append("("+sqlWhere+" and (apd.status is null or apd.status=:pdStatus)) ");
					params.put("pdStatus", AbPoolConstant.AB_POOL_STATUS_1);
				}else{
					sqlStatus.append(" apd.status="+statusList.get(i));
				}
			}
			sqlStatus.append(" ) ");
			sql.append(sqlStatus);
			input.getFormdata().remove("ins_statusName");
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "apc.CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		input.addOrder("origNo", true);
		return this.getDynamicBySQL(input, sql.toString(), AbPoolSignVO.class, "", false,params);
	}
	/**
	 * 审批页面查询当前操作员可审批的电票签收数据
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessList) {
		StringBuilder sql = new StringBuilder(getQuerySql(true));
		sql.append(" WHERE 1=1 ");
		Map<String, Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "apd.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbPoolSignVO.class, "", false, params);
	}
	/**
	 * 根据票据池交易明细iId查询签收入池信息
	 *
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	public List<AbPoolSignVO> getAbPoolSignVOByPdId(String pdId) {
		StringBuilder sql = new StringBuilder(getQuerySql(true));
		sql.append(" WHERE apd.id = :pdId  ");
		Map<String, Object> params = new HashMap<>();
		params.put("pdId", pdId);
		List<AbPoolSignVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbPoolSignVO.class);
		return listInfo;
	}
	
	/* 单位有票据签收信息，不允许删除**/
	public int getValidSiginNumByCorpId(String corpId){
		StringBuilder sql  = new StringBuilder()
				.append(" select count(*)")
				.append(" from AB_BILL_RECEIVEBIZ P  ")
				.append(" left join ab_pool_corp apc on p.REC_CORP_ID = apc.corp_id ")
				.append(" left join sys_corp sc on apc.corp_id = sc.id")
				.append(" where 1=1")
				.append(" and p.msgtype not IN (:MSGTYPE)")
				.append(" and sc.id=:corpId");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("corpId",corpId);
		params.put("MSGTYPE", new String[]{
				AccebillConstant.AB_MSGTYPE_RECITE,
				AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
				AccebillConstant.AB_MSGTYPE_DISCOUNT});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}
	/**
	 * 根据单位id获取此单位下有没有处理中的数据
	 * 电票待签收、审批中、指令中的数据（在途）
	 * @param corpId
	 * @return 有效额度分配
	 */
	public int getPoolSignByCorpId(String corpId){
		StringBuilder sql  = new StringBuilder()
				.append(" select count(*)")
				.append(" from AB_BILL_RECEIVEBIZ p ")
				.append(" left join ab_accrecords a on a.recid=p.id and a.opt_status not in ('"+AccebillConstant.AB_BILL_OPT_STATUS_2+"') ")
				.append(" left join ab_pool_dtl apd on apd.id=a.pd_id ")
				.append(" left join ab_pool_corp apc on p.REC_CORP_ID = apc.corp_id ")
				.append(" where 1=1 and apc.corp_id=:corpId ")
				.append(" and p.msgtype IN (:MSGTYPE)  and apc.VALID_SIGN=:validSign ")
				.append(" and (((apd.status>:status1 and apd.status<:status2) or apd.opt_status in (:optStatus)) or p.STATUS=:status) ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("corpId",corpId);
		params.put("validSign",AbPoolConstant.AB_POLL_VALID_SIGN_1);
		params.put("status",AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS0);
		params.put("status2",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("optStatus",new String[]{
				AbPoolConstant.AB_POOL_OPT_STATUS16,
				AbPoolConstant.AB_POOL_OPT_STATUS18,
				AbPoolConstant.AB_POOL_OPT_STATUS_1});
		params.put("MSGTYPE", new String[]{
				AccebillConstant.AB_MSGTYPE_RECBILL,
				AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
				AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
				AccebillConstant.AB_MSGTYPE_RECITE
		});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}
}
