package com.bytter.biz.wage.dao;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: NIS_WAGE
 * @author onlineGenerator
 * @date 2018-01-08 17:19:13
 * @version 1.0
 *
 */
@Repository
public class NisWageDao extends BaseDao<NisWage,String>{
	/**
	 * 查询代发工资单
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisWageVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,");
		sql.append(" n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,");
		sql.append(" n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,");
		sql.append(" acc.BANK_TYPE_ID as bankTypeId,t.TYPE_NAME as bankTypeName, ");
		sql.append(" b.id as busTypeId,n.bus_type as busType,b.bus_type_name as busTypeName,n.erp_id as erpId,n.LANDING_LIMIT_RESULT as landingLimitResult ");
//		String opType = (String) dataTablesInput.getFormdata().get("opType");
//		if ("query".equals(opType) || "listDeal".equals(opType)) {
//			sql.append(",(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat='"+BdlinkConstant.SUCCESS+"') as successNum,");
//			sql.append("(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat in('"+BdlinkConstant.BANK_FAILED+"','"+BdlinkConstant.INTERFACE_FAILED+"')) as failNum,");
//			sql.append("(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat='"+BdlinkConstant.WAIT_RESULT+"') as waitNum ");
//		}
		sql.append(" from nis_wage n left join bt_bank_acc acc on n.payer_Acc = acc.bank_Acc ");
		sql.append(" left join bt_bank_type t on acc.BANK_TYPE_ID = t.id ");
		sql.append(" left join nis_bus_type b on n.bus_type = b.id where 1 = 1 ");
		
		String voucherStat = ObjectUtils.isNotEmpty(dataTablesInput.getFormdata().get("voucherStat")) ? dataTablesInput.getFormdata().get("voucherStat").toString() : "";
		if(!EmptyUtils.isBlank(voucherStat)){
			sql.append(" and exists(select 1 from bis_exc b where b.cbs_bill_no = n.wage_no ");
			String[] voucherStats = voucherStat.split(",");
			voucherStat = SqlUtil.getInSql(voucherStats, "b.voucher_stat", true);
			sql.append(" and ").append(voucherStat).append(") ");
		}
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisWageVO.class, "n", false);
	}
	/**
	 * 根据代发工资ID获取代发工资信息
	 *
	 * @param id
	 * @return
	 */
	public NisWageVO getByIdUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,");
		sql.append(" n.bif_code as bifCode, n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,");
		sql.append(" n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,");
		sql.append(" b.id as busTypeId,n.bus_type as busType,b.bus_type_name as busTypeName,n.erp_id as erpId,n.LANDING_LIMIT_RESULT as landingLimitResult");
		//sql.append("c.bank_Name as payerBank,c.bank_Code as payerBankCode,c.prov as payerProv,c.city as payerCity,c.bank_type_id as bankTypeId");
		//sql.append(" from nis_wage n left join bt_bank_acc c on n.payer_Acc = c.bank_Acc left join nis_bus_type b on n.bus_type = b.id ");
		sql.append(" from nis_wage n left join nis_bus_type b on n.bus_type = b.id ");
		sql.append(" where n.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, NisWageVO.class);
	}
	/**
	 * 根据代发工资单号获取代发工资单（落地限额回调按业务单号定位单据用）
	 *
	 * @param wageNo 代发工资单号
	 * @return
	 */
	public NisWage getByWageNo(String wageNo) {
		String hql = " from NisWage n where n.wageNo=:wageNo ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("wageNo", wageNo);
		List<NisWage> list = this.listByHQL(hql, params);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return list.get(0);
	}
	/**
	 * 根据代发工资ID获取代发工资信息
	 *
	 * @param idList
	 * @return
	 */
	public List<NisWageVO> listByIds(List<String> idList) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,corp.name as corpName,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,c.bank_Name as payerBank,");
		sql.append(" n.bif_code as bifCode, c.bank_Code as payerBankCode,c.prov as payerProv,c.city as payerCity,n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,");
		sql.append(" n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,");
		sql.append(" b.id as busTypeId,n.bus_type as busType,b.bus_type_name as busTypeName,c.bank_type_id as bankTypeId");
		sql.append(" from nis_wage n left join bt_bank_acc c on n.payer_Acc = c.bank_Acc left join nis_bus_type b on n.bus_type = b.id ");
		sql.append(" left join sys_corp corp on n.corp_id = corp.id ");
		sql.append(" where 1=1");
		String idAuthSql = SqlUtil.getInSql(idList, "n.id", true);
		sql.append(" and ").append(idAuthSql);
		HashMap<String, String> params = new HashMap<String, String>();
		return listBySQLAliasToBean(sql.toString(), params, NisWageVO.class);
	}
	/**
	 * 判断是否是重复的工资单
	 * @param vo
	 * @return
	 */
	public boolean isCheckRepeat(NisWageVO vo){
		boolean result = false;
		String hql = " from NisWage n where n.payerAcc = :payerAcc and n.corpId = :corpId and n.wages = :wages and n.wageDay = :wageDay and n.status<>:status";
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(vo.getId())){
			hql += " and id<>:id";
			params.put("id",vo.getId());
		}
		params.put("payerAcc",vo.getPayerAcc());
		params.put("corpId",vo.getCorpId());
		params.put("wages",vo.getWages());
		params.put("wageDay",vo.getWageDay());
		params.put("status",NetbankConstant.STATUE_DEL);
		List list = this.listByHQL(hql,params);
		if(!EmptyUtils.isEmptyList(list)){
			result = true;
		}
		return result;
	}
	/**
	 * 需审核列表数据
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput<NisWageVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,");
		sql.append(" n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.LANDING_LIMIT_RESULT as landingLimitResult");
		sql.append(" from nis_wage n where 1=1");
		if(!EmptyUtils.isEmpty(businessSql)){
			sql.append(" and ").append(businessSql);
		}
		String flowStatus = dataTablesInput.getParameter().get("flowStatus");
		//已审批不过滤状态
		if(!IWorkFlowService.PASS.equals(flowStatus)){
			sql.append(" and n.status > 10 and n.status < 95 ");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisWageVO.class, "n", false);
	}
	/**
	 * 查询代发工资结果异常处理
	 *
	 * @param dataTablesInput
	 * @return
	 * @author zuoy 2018-06-20
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesDeal(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,");
		sql.append(" n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate");
//		sql.append(",(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat='"+BdlinkConstant.SUCCESS+"') as successNum,");
//		sql.append("(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat in('"+BdlinkConstant.BANK_FAILED+"','"+BdlinkConstant.INTERFACE_FAILED+"')) as failNum,");
//		sql.append("(select count(*) from nis_wage_detail d where d.wage_id=n.id and d.voucher_stat='"+BdlinkConstant.WAIT_RESULT+"') as waitNum ");
		sql.append(" from nis_wage n where 1 = 1 ");
		sql.append(" and n.status not in (" + NetbankConstant.STATUE_BACK + ")");
		sql.append(" and exists(select 1 from bis_exc b where b.cbs_bill_no = n.wage_no ");
		sql.append(" and b.voucher_stat in ('" + BdlinkConstant.EXCEPTION + "','" + BdlinkConstant.DATA_TAMPER + "','" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "') ");
		if(!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("voucherStat"))
				&& !EmptyUtils.isBlank(dataTablesInput.getFormdata().get("voucherStat").toString()) ){
			sql.append(" and b.voucher_stat = '").append(dataTablesInput.getFormdata().get("voucherStat")).append("'");
		}
		sql.append(") ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisWageVO.class, "n", false);
	}
	/**
	 * 判断是否是重复的工资册标题
	 * @param corpId
	 * @param wageTitle
	 * @return
	 */
	public boolean isCheckWageTitle(String corpId, String wageTitle, String id){
		boolean result = false;
		String hql = " from NisWage n where n.wageTitle = :wageTitle and n.status<>:status and n.corpId=:corpId";
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(id)){
			hql += " and id<>:id";
			params.put("id",id);
		}
		params.put("wageTitle",wageTitle);
		params.put("status",NetbankConstant.STATUE_DEL);
		params.put("corpId",corpId);
		List list = this.listByHQL(hql,params);
		if(!EmptyUtils.isEmptyList(list)){
			result = true;
		}
		return result;
	}
	/**
	 * 根据单位统计代发工资单
	 * @param corpId
	 * @return
	 */
	public int getWageCountByCorpId(String corpId){
		String sql="select count(*) from nis_wage where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//0-暂存、11-待审批、-1-审批打回
		sql+=" and status in ('0','11','-1')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 带原状态更新代发工资单
	 * @param vo 代发工资对象
	 * @param oldStatus 原状态控制
	 * @return
	 */
	public int updateNisWageStatus(NisWageVO vo, Integer[] oldStatus) {
		StringBuilder hql=new StringBuilder();
		hql.append("update NisWage set payerAcc=:payerAcc,payerAccName=:payerAccName,");
		hql.append(" curId=:curId,bifCode=:bifCode,wageDay=:wageDay,wishPayday=:wishPayday,");
		hql.append(" addWord=:addWord,status=:status,attachmentId=:attachmentId,voucherStat=:voucherStat,");
		hql.append(" busType=:busType");
		Map<String,Object> params = new HashMap<>();
		params.put("payerAcc", vo.getPayerAcc());
		params.put("payerAccName", vo.getPayerAccName());
		params.put("curId", vo.getCurId());
		params.put("bifCode", vo.getBifCode());
		params.put("wageDay", vo.getWageDay());
		params.put("wishPayday", vo.getWishPayday());
		params.put("voucherStat", vo.getVoucherStat());
		params.put("addWord", vo.getAddWord());
		params.put("status", vo.getStatus());
		params.put("attachmentId", vo.getAttachmentId());
		params.put("busType", vo.getBusType());
		if (StringUtils.isNotEmpty(vo.getCreateBy())) {
			hql.append(",createBy=:createBy ");
			params.put("createBy", vo.getCreateBy());
		}
		if (vo.getCreateDate() != null) {
			hql.append(",createDate=:createDate ");
			params.put("createDate", vo.getCreateDate());
		}
		if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
			hql.append(",updateBy=:updateBy ");
			params.put("updateBy", vo.getUpdateBy());
		}
		if (vo.getUpdateDate() != null) {
			hql.append(",updateDate=:updateDate ");
			params.put("updateDate", vo.getUpdateDate());
		}
		hql.append(" where id=:id and status in (:oldStatus) ");
		params.put("id", vo.getId());
		params.put("oldStatus", oldStatus);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 获取ERP待处理单据
	 * @return
	 */
	public List<NisWageVO> listErpTodoBill() {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.wage_No as wageNo,n.corp_Id as corpId,n.wage_Title as wageTitle,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,c.bank_Name as payerBank,");
		sql.append(" n.bif_code as bifCode, c.bank_Code as payerBankCode,c.prov as payerProv,c.city as payerCity,n.cur_Id as curId,n.wages as wages,n.wage_Day as wageDay,n.wish_Payday as wishPayday,n.add_Word as addWord,");
		sql.append(" n.status as status,n.voucher_Stat as voucherStat,n.attachment_Id as attachmentId,n.create_By as createBy,");
		sql.append(" n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.erp_Id as erpId,");
		sql.append(" b.id as busTypeId,n.bus_type as busType,b.bus_type_name as busTypeName,c.bank_type_id as bankTypeId");
		sql.append(" from nis_wage n left join bt_bank_acc c on n.payer_Acc = c.bank_Acc left join nis_bus_type b on n.bus_type = b.id ");
		sql.append(" where (n.erp_Id is not null or n.erp_id<>'') and n.status=:status");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("status", NetbankConstant.STATUE_TEMP);
		return listBySQLAliasToBean(sql.toString(), params, NisWageVO.class);
	}

	public int updateVoucherState(String cbsBillId, String voucherStat) {
		String sb = " update nis_allocation_req set voucher_stat = :voucherStat " +
				" where  id = :id";
		Map<String,Object> param = new HashMap<>();
		param.put("id", cbsBillId);
		param.put("voucherStat", voucherStat);
		return createSQLQuery(sb, param).executeUpdate();
	}
	
	public NisWage getNisWageVOByVoucherNo(String voucherNo) {
		StringBuilder sql=new StringBuilder("select * from nis_wage n ");
		sql.append(" where n.voucher_No=:voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("voucherNo", voucherNo);
		return getBySQLAliasToBean(sql.toString(), params, NisWage.class);
	}
	
	public List<NisWage> getWageTileList() {
		StringBuilder sql=new StringBuilder("select * from nis_wage n ");
		return listBySQLAliasToBean(sql.toString(), null, NisWage.class);
	}

	/**
	 * 更新代发工资单银行落地限额处理结果。
	 */
	public void updateLandingLimitResult(String id, String landingLimitResult) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("landingLimitResult", landingLimitResult);
		batchSQLExecute("update NIS_WAGE set LANDING_LIMIT_RESULT=:landingLimitResult where ID=:id", params);
	}

	/**
	 * 翻转代发工资单银行落地限额处理结果（互斥：仅当落地标识仍为 0-待执行才更新，0/A/R 终态不可再翻）。
	 *
	 * @return 更新行数；0 = 落地标识已非 0（已被并发回调翻转收尾/未处于落地待执行），调用方应中止收尾
	 */
	public int updateLandingLimitResultIfPending(String id, String landingLimitResult) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("landingLimitResult", landingLimitResult);
		return batchSQLExecute("update NIS_WAGE set LANDING_LIMIT_RESULT=:landingLimitResult "
				+ "where ID=:id and LANDING_LIMIT_RESULT='0'", params);
	}
}
