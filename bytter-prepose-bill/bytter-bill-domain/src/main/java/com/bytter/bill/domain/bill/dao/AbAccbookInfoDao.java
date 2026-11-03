package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbBillAccbookInfoDTO;
import com.bytter.bill.domain.bill.entity.AbAccbookInfo;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: 票据记账信息Dao
 * @Description: AB_ACCBOOK_INFO
 * @author zhengg
 * @date 2018-09-06 19:20:01
 * @version 1.0
 *
 */
@Repository
public class AbAccbookInfoDao extends BaseDao<AbAccbookInfo,String>{
	/**
	 * 根据流水ID获取此流水对应记账信息
	 * @param aid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AbAccbookInfo getAbAccbookInfoByAid(String aid){
		if(StringUtils.isEmpty(aid)){
			return null;
		}
		String hql="from AbAccbookInfo where aid=:aid";
		Map<String,Object> params=new HashMap<>();
		params.put("aid", aid);
		List<AbAccbookInfo> list=this.listByHQL(hql, params);
		if(!ListUtils.isNullOrEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	/**
     * 查询手工记账数据信息
     *
     * @param input
     * @param busGloabIds
     * @return
     */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getAbAccbookInfoList(DataTablesInput input, List<String> corpAuthList, ICurrentUser user, List<String> busGloabIds) {
		StringBuilder sql=new StringBuilder("SELECT ");
		sql.append(" B.AID,B.NET_ID AS NETID,B.CORP_ID AS CORPID,B.BUS_ORI_TYPE AS BUSORITYPE,B.SYS_RACE_CODE AS SYSRACECODE,V.BILL_MONEY AS BILLMONEY, ");
		sql.append(" V.ORIG_NO AS ORIGNO,V.PAYER,V.PAYEE,V.ACCEPTOR,V.BILL_TYPE AS BILLTYPE,V.BILL_FLAG AS BILLFLAG,V.START_DATE AS STARTDATE,V.END_DATE AS ENDDATE ,");
		sql.append(" V.PAYEEACC AS PAYEEACC,V.PAYEE_BANK_NAME AS PAYEEBANKNAME,V.PAYEEBANKNO as PAYEEBANKNO,A.ABS as ABSS,BT_NVL_CHAR(B.IS_BACK,'1') ISBACK,");
//		sql.append(" FA.VOUCHER_STATE as VOUCHERSTATE,FA.ORDER_NO as ORDERNO,FA.VOUCHER_NO as VOUCHERNO,FA.BUS_GLOBAL_ID as BUSGLOBALID,A.REC_NM as recNm,A.REQ_NM as reqNm, ");
		sql.append(" A.BUS_GLOBAL_ID as BUSGLOBALID,A.REC_NM as recNm,A.REQ_NM as reqNm, ");
		sql.append("D.INTEREST_RATES AS INTERESTRATES,D.DAYNUM AS DAYNUM ,D.INTEREST AS INTEREST,D.RPDOPENDT AS RPDOPENDT,D.RPDDUEDT AS RPDDUEDT ,V.BILL_STATUS AS BILLSTATUS,");
		sql.append("A.REQ_ACCT_ID AS REQACCTID,T.ALLOCATED_CODE AS ALLOCATEDCODE,T.RECORD_CORP_NAME AS RECORDCORPNAME,T.RECEVIE_CORP_NAME AS RECEVIECORPNAME,");
		sql.append("T.RECEVIE_BANKACC AS RECEVIEBANKACC,T.ALLOCATION_MONEY AS ALLOCATIONMONEY,T.ALLOCATION_REASON AS ALLOCATION_REASON ");
		sql.append(" FROM AB_ACCBOOK_INFO B  ");
		sql.append(" INNER JOIN AB_ACCRECORDS A ON B.AID=A.ID ");
		sql.append(" INNER JOIN AB_VOUCHERINFO V ON V.ID=A.VID ");
		sql.append(" LEFT JOIN AB_DISCOUNT D ON D.RECORD_ID=A.ID ");
		//sql.append("LEFT JOIN AB_BILL_TRANSFER T ON A.TRID = T.ID");
		sql.append(" LEFT JOIN  AB_BILL_TRANSFER_DTL L ON L.BILL_AID = A.LASTID ");
		sql.append(" LEFT JOIN AB_BILL_TRANSFER T ON T.ID = L.TRANSFER_ID ");
//		sql.append(" LEFT JOIN FC_ACCBOOK FA ON FA.BUS_GLOBAL_ID=A.BUS_GLOBAL_ID "); FC_ACCBOOK 流水账主档表 凭证模块表
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<> ();
		if (!input.getFormdata().containsKey("ins_voucherState")) {
			sql.append(" and A.BUS_GLOBAL_ID IS NULL ");
		} else {
			if (null != input.getFormdata().get("ins_voucherState") && input.getFormdata().get("ins_voucherState").toString().contains(FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.getValue())) {
				sql.append(" and B.OPERATOR_CODE != '" + user.getId() + "' ");
			}
			sql.append(" and ").append(SqlUtil.getInSql(busGloabIds, "A.BUS_GLOBAL_ID", true)).append(" ");
		}
		sql.append(" and (A.status=:status or B.IS_BACK='0' ) ");
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS94);
		if(corpAuthList!=null){
			String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "B.corp_id", params);
			sql.append(" and ").append(corpAuthSql).append(" ");
		}
//		sql.append(" order by V.ORIG_NO desc ");
		input.addOrder("origNo", true);
		return getDynamicBySQL(input, sql.toString(), AbBillAccbookInfoDTO.class, "", false,params);
	}
	public AbBillAccbookInfoDTO getAbBillInfoById(String id){
		StringBuilder sql=new StringBuilder("SELECT ");
		sql.append(" B.AID,B.NET_ID AS NETID,B.CORP_ID AS CORPID,B.BUS_ORI_TYPE AS BUSORITYPE,B.SYS_RACE_CODE AS SYSRACECODE,A.STATUS AS STATUS,V.BILL_MONEY AS BILLMONEY, ");
		sql.append(" V.ORIG_NO AS ORIGNO,V.PAYER,V.PAYEE,V.ACCEPTOR,V.BILL_TYPE AS BILLTYPE,V.BILL_FLAG AS BILLFLAG,V.START_DATE AS STARTDATE,V.END_DATE AS ENDDATE ,");
		sql.append(" V.PAYEEACC AS PAYEEACC,V.PAYEE_BANK_NAME AS PAYEEBANKNAME,V.PAYEEBANKNO as PAYEEBANKNO,A.ABS as ABSS, A.BUS_GLOBAL_ID AS BUSGLOBALID,A.LASTID AS LASTID,");
		sql.append(" A.REC_ACCTSVCR AS AOACCNINFAC ");
		sql.append(" FROM AB_ACCBOOK_INFO B  ");
		sql.append(" INNER JOIN AB_ACCRECORDS A ON B.AID=A.ID ");
		sql.append(" INNER JOIN AB_VOUCHERINFO V ON V.ID=A.VID ");
		sql.append(" left JOIN AB_DISCOUNT D ON D.RECORD_ID = A.ID ");
		sql.append(" WHERE B.AID = :id");
		Map<String,Object> params = new HashMap<> ();
		params.put("id",id);
		return getBySQLAliasToBean(sql.toString(), params, AbBillAccbookInfoDTO.class);
	}
	/**
	 * 根据单位id查询票据待记账数据
	 * @param corpId 单位id
	 * @return
	 */
	public boolean checkToBeBookByCorpId(String corpId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("select l.id  ");
		sql.append(" FROM ab_voucherinfo_vice c ");
		sql.append(" left join ab_accrecords l on c.id=l.vvid ");
		sql.append(" where  l.status=:status and c.corp_id=:corpId ");
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("corpId",corpId);
		Query query = createSQLQuery(sql.toString(), params);
		List<String> list = query.list();
		if(list!=null&&!list.isEmpty()){
			return true;
		}
		return false;
	}
	/**待记账查询*/
	public List<AbBillAccbookInfoDTO> queryTallyAbAccbook(Integer busOriType) {
		StringBuilder sql=new StringBuilder("SELECT ");
		sql.append(" B.AID,V.BILL_MONEY AS BILLMONEY ,B.CORP_ID AS CORPID ");
		sql.append(" FROM AB_ACCBOOK_INFO B  ");
		sql.append(" INNER JOIN AB_ACCRECORDS A ON B.AID=A.ID ");
		sql.append(" INNER JOIN AB_VOUCHERINFO V ON V.ID=A.VID ");
		sql.append(" LEFT JOIN AB_DISCOUNT D ON D.RECORD_ID=A.ID ");
		sql.append(" LEFT JOIN  AB_BILL_TRANSFER_DTL L ON L.BILL_AID = A.LASTID ");
		sql.append(" LEFT JOIN AB_BILL_TRANSFER T ON T.ID = L.TRANSFER_ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" and A.BUS_GLOBAL_ID IS NULL ");
		Map<String,Object> params = new HashMap<> ();
		sql.append(" and (A.status=:status or B.IS_BACK='0' ) ");
		sql.append(" and B.bus_Ori_Type = :busOriType ");
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("busOriType",busOriType);
		return listBySQLAliasToBean(sql.toString(),params, AbBillAccbookInfoDTO.class);
	}
}
