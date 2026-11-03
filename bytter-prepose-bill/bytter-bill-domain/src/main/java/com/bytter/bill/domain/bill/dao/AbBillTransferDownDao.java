package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownCorpDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownManageVo;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownNetCollectVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 票据划拨公共业务数据库操作类
 * @author onlineGenerator
 * @date 2017-10-31
 * @version 1.0
 *
 */
@Repository
public class AbBillTransferDownDao extends BaseDao<AbBillTransferDownManageVo, String> {
	
	/**
	 * 加载票据下拨业务信息数据
	 * @param dataTablesInput 查询入参
	 * @param corpAuthList 单位权限
	 * @return
	 */
	public DataTablesOutput<AbBillTransferDownManageVo> listAbBillQueryDtl(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map <String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append (" select  ");
		sql.append (" t.busType as busType,t.allocatedCode as allocatedCode,t.applyCode as applyCode,");
		sql.append (" t.recordCorpId as recordCorpId,t.recordCorpCode as recordCorpCode ,t.recordCorpName as recordCorpName,t.recevieCorpCode as recevieCorpCode,t.recevieCorpName as recevieCorpName,");
		sql.append (" t.aid as aid,t.recordBankacc as recordBankacc,t.recevieBankAcc as recevieBankAcc,");
		sql.append (" t.applyMoney as applyMoney,t.allocationMoney as allocationMoney,t.applyReason as applyReason,");
		sql.append (" t.allocationReason as allocationReason,t.recordDate as recordDate,t.billCurrency as billCurrency,");
		sql.append (" t.billCurrencyName as billCurrencyName,t.businessDate as businessDate,t.status as status ");
        sql.append ("  from (select c.brief_id as busType, ");//业务类型
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t.allocated_code else null end) as allocatedCode, ");//下拨单号
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t2.allocated_code else t.allocated_code end) as applyCode, ");//申请单号
		sql.append ("  t.record_corp_id as recordCorpId, ");//录入单位id
		sql.append ("  t.record_corp_name as recordCorpName, ");//录入单位名称
		sql.append ("  t.RECORD_CORP_CODE as recordCorpCode, ");//录入单位Code
		sql.append ("  t.recevie_corp_id as recevieCorpId, ");//收票单位id
		sql.append ("  t.recevie_corp_name as recevieCorpName, ");//收票单位名称
		sql.append ("  t.RECEVIE_CORP_CODE as recevieCorpCode, ");//收票单位名称
		sql.append ("  c.id as aid, ");//操作流水id
		//sql.append ("  t.bill_no as billNo, ");//选票票据号
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t.record_bankacc else null end) as recordBankacc, ");//下拨中心账号
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t.recevie_bankAcc else null end) as recevieBankAcc, ");//收票单位账号
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t2.allocation_money else t.allocation_money end) as applyMoney, ");//申请金额
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t.allocation_money else t2.allocation_money end) as allocationMoney, ");//下拨金额
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t2.allocation_reason else t.allocation_reason end) as applyReason, ");//申请原因
		sql.append ("  (case c.brief_id when '"+ AccebillConstant.BRIEF_STATUS_6204+"' then t.allocation_reason else t2.allocation_reason end) as allocationReason, ");//下拨原因
		sql.append ("  t.record_date as recordDate, ");//录入日期
		sql.append ("  t.bill_currency as billCurrency, ");//币别id
		sql.append ("  t.BILL_CUR_NAME as billCurrencyName, ");
		sql.append ("  c.business_date as businessDate, ");//业务办理日期
		sql.append ("  c.status as status ");
		sql.append ("  from ab_accrecords c ");
		sql.append ("  left join AB_BILL_TRANSFER t on t.id = c.trid ");
		sql.append ("  left join ab_accrecords c2 on c2.id = c.lastid ");
		sql.append ("  left join ab_voucherinfo_vice vv1 on vv1.id=c.vvid");
		sql.append ("  left join AB_BILL_TRANSFER t2 on c2.trid = t2.id ");
//		sql.append ("  left join BT_CURRENCY bc on bc.id = t.bill_currency ");
		//sql.append ("  left join ab_brief_status bs on bs.status = c.brief_id ");
		sql.append ("  where c.trid is not null ");
		//用户满足对应查询权限的单位数据
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "t.record_corp_id", params);
		sql.append("and ").append(corpAuthSql).append(" ");
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (t.recevie_bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "t.recevie_bankAcc", "bankAccs", params)).append(")");
		}
		sql.append ("  )t ");
//		sql.append(" left join sys_corp sc1 on t.recordCorpId=sc1.id  ");
//		sql.append(" left join sys_corp sc2 on t.recevieCorpId=sc2.id ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillTransferDownManageVo.class, "", true,params);
	}
	/**
	 * 加载网点票据划拨汇总表信息数据
	 * @param dataTablesInput 查询入参
	 * @param corpAuthList 单位权限
	 * @return
	 */
	public DataTablesOutput<AbBillTransferDownNetCollectVO> listNetCollectInfoBak(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map <String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from(");
		sql.append("select  ");
		sql.append("BT_NVL_CHAR(c.corp_id,b.recevie_corp_id) as corpId, ");
		// sql.append("min(sc.code) as corpCode, ");
		// sql.append("min(sc.name) as corpName, ");
		// sql.append("min(cn.code) as netCode,  ");
		// sql.append("min(cn.name) as netName, ");
		sql.append("sum(case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6201+"' then BT_NVL_NUM(d.bill_money,0) else 0 end) as shMoney, ");
		sql.append("sum(case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6202+"' then BT_NVL_NUM(d.bill_money,0) else 0 end) as gjMoney, ");
		sql.append("sum(BT_NVL_NUM(b.allocation_money,0)) as xbMoney,  ");
		sql.append("(sum(BT_NVL_NUM(d.bill_money,0))-sum(BT_NVL_NUM(b.allocation_money,0))) as netBalMoney  ");
		sql.append("from ab_accrecords a  ");
		sql.append("left join ab_bill_transfer b on a.trid=b.id ");
		sql.append("left join ab_voucherinfo_vice c on c.id=a.vvid ");
		sql.append("left join ab_voucherinfo d on d.id=a.vid ");
		// sql.append("left join sys_corp sc on (sc.id=c.corp_id OR (c.corp_id IS NULL AND sc.id=b.recevie_corp_id)) ");
		// sql.append("left join sys_corp cn on cn.id=sc.net_id ");
		sql.append("where (brief_id in (:brief_id1) or (brief_id=:brief_id2 and b.id is not null)) ");
		sql.append("and a.status=:status and a.opt_status=:opt_status  ");
		params.put("brief_id1",new String[]{AccebillConstant.BRIEF_STATUS_6201,AccebillConstant.BRIEF_STATUS_6202});
		params.put("brief_id2",AccebillConstant.BRIEF_STATUS_6204);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("opt_status",AccebillConstant.AB_BILL_OPT_STATUS19);
		Map formData=dataTablesInput.getFormdata();
		if(formData.containsKey("beginDate") && StringUtils.isNotEmpty((String) formData.get("beginDate"))){
			sql.append("and a.business_date>=:beginDate ");
			params.put("beginDate", DateUtils.parse((String) formData.get("beginDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE));
			formData.remove("beginDate");
		}
		if(formData.containsKey("endDate") && StringUtils.isNotEmpty((String) formData.get("endDate"))){
			sql.append("and a.business_date<:endDate ");
			params.put("endDate", DateUtils.addDay(DateUtils.parse((String) formData.get("endDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE),1));
			formData.remove("endDate");
		}
		if(formData.containsKey("corpId")&& StringUtils.isNotEmpty((String)formData.get("corpId"))){
			String[] corpIds=String.valueOf(formData.get("corpId")).split(",");
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpIds);
			formData.remove("corpId");
		}else{
			//用户满足对应查询权限的单位数据
			// String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "c.corp_id", params);
			// sql.append("and ").append(corpAuthSql);
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpAuthList);
		}
		sql.append("group by corpId  ");
		sql.append(") info ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillTransferDownNetCollectVO.class, "", true,params);
	}
	/**
	 * 加载网点票据划拨汇总表信息数据
	 * @param dataTablesInput 查询入参
	 * @param corpAuthList 单位权限
	 * @return
	 */
	public DataTablesOutput<AbBillTransferDownNetCollectVO> listNetCollectInfo(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map <String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from(");
		sql.append("select  ");
		sql.append("tmp.corpId, ");
		sql.append("sum(case tmp.brief_id when '"+AccebillConstant.BRIEF_STATUS_6201+"' then BT_NVL_NUM(tmp.bill_money,0) else 0 end) as shMoney, ");
		sql.append("sum(case tmp.brief_id when '"+AccebillConstant.BRIEF_STATUS_6202+"' then BT_NVL_NUM(tmp.bill_money,0) else 0 end) as gjMoney, ");
		sql.append("sum(BT_NVL_NUM(tmp.allocation_money,0)) as xbMoney,  ");
		sql.append("(sum(BT_NVL_NUM(tmp.bill_money,0))-sum(BT_NVL_NUM(tmp.allocation_money,0))) as netBalMoney  ");
		sql.append("from (  ");
		sql.append("select BT_NVL(c.corp_id, b.recevie_corp_id) as corpId,a.brief_id, d.bill_money, b.allocation_money  ");
		sql.append("from ab_accrecords a  ");
		sql.append("left join ab_bill_transfer b on a.trid=b.id ");
		sql.append("left join ab_voucherinfo_vice c on c.id=a.vvid ");
		sql.append("left join ab_voucherinfo d on d.id=a.vid ");
		sql.append("where (brief_id in (:brief_id1) or (brief_id=:brief_id2 and b.id is not null)) ");
		sql.append("and a.status=:status and a.opt_status=:opt_status  ");
		params.put("brief_id1",new String[]{AccebillConstant.BRIEF_STATUS_6201,AccebillConstant.BRIEF_STATUS_6202});
		params.put("brief_id2",AccebillConstant.BRIEF_STATUS_6204);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("opt_status",AccebillConstant.AB_BILL_OPT_STATUS19);
		Map formData=dataTablesInput.getFormdata();
		if(formData.containsKey("beginDate") && StringUtils.isNotEmpty((String) formData.get("beginDate"))){
			sql.append("and a.business_date>=:beginDate ");
			params.put("beginDate", DateUtils.parse((String) formData.get("beginDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE));
			formData.remove("beginDate");
		}
		if(formData.containsKey("endDate") && StringUtils.isNotEmpty((String) formData.get("endDate"))){
			sql.append("and a.business_date<:endDate ");
			params.put("endDate", DateUtils.addDay(DateUtils.parse((String) formData.get("endDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE),1));
			formData.remove("endDate");
		}
		if(formData.containsKey("corpId")&& StringUtils.isNotEmpty((String)formData.get("corpId"))){
			String[] corpIds=String.valueOf(formData.get("corpId")).split(",");
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpIds);
			formData.remove("corpId");
		}else{
			//用户满足对应查询权限的单位数据
			// String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "c.corp_id", params);
			// sql.append("and ").append(corpAuthSql);
			if (ObjectUtil.isNotEmpty(corpAuthList)){
				sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
				params.put("corpId",corpAuthList);
			}
		}
		sql.append(") tmp  ");
		sql.append("group by corpId  ");
		sql.append(") info ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillTransferDownNetCollectVO.class, "", true,params);
	}
	/**
	 * 加载网点票据划拨汇总表信息数据
	 * @param vo 查询入参
	 * @param corpAuthList 单位权限
	 * @return
	 */
	public List<AbBillTransferDownNetCollectVO> listNetCollectInfo(AbBillTransferDownNetCollectVO vo,List<String> corpAuthList) {
		Map <String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from(");
		sql.append("select  ");
		sql.append("BT_NVL_CHAR(c.corp_id,b.recevie_corp_id) as corpId, ");
		// sql.append("min(sc.code) as corpCode, ");
		// sql.append("min(sc.name) as corpName, ");
		// sql.append("min(cn.code) as netCode,  ");
		// sql.append("min(cn.name) as netName, ");
		sql.append("sum(case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6201+"' then BT_NVL_NUM(d.bill_money,0) else 0 end) as shMoney, ");
		sql.append("sum(case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6202+"' then BT_NVL_NUM(d.bill_money,0) else 0 end) as gjMoney, ");
		sql.append("sum(BT_NVL_NUM(b.allocation_money,0)) as xbMoney,  ");
		sql.append("(sum(BT_NVL_NUM(d.bill_money,0))-sum(BT_NVL_NUM(b.allocation_money,0))) as netBalMoney  ");
		sql.append("from ab_accrecords a  ");
		sql.append("left join ab_bill_transfer b on a.trid=b.id ");
		sql.append("left join ab_voucherinfo_vice c on c.id=a.vvid ");
		sql.append("left join ab_voucherinfo d on d.id=a.vid ");
		// sql.append("left join sys_corp sc on (sc.id=c.corp_id OR (c.corp_id IS NULL AND sc.id=b.recevie_corp_id)) ");
		// sql.append("left join sys_corp cn on cn.id=sc.net_id ");
		sql.append("where (brief_id in (:brief_id1) or (brief_id=:brief_id2 and b.id is not null)) ");
		sql.append("and a.status=:status and a.opt_status=:opt_status  ");
		params.put("brief_id1",new String[]{AccebillConstant.BRIEF_STATUS_6201,AccebillConstant.BRIEF_STATUS_6202});
		params.put("brief_id2",AccebillConstant.BRIEF_STATUS_6204);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("opt_status",AccebillConstant.AB_BILL_OPT_STATUS19);
		if(!EmptyUtils.isEmpty(vo.getBeginDate())){
			sql.append("and a.business_date>=:beginDate ");
			params.put("beginDate", vo.getBeginDate());
		}
		if(!EmptyUtils.isEmpty(vo.getEndDate())){
			sql.append("and a.business_date<=:endDate ");
			params.put("endDate", vo.getEndDate());
		}
		if(!EmptyUtils.isBlank(vo.getCorpId())){
			String[] corpIds=String.valueOf(vo.getCorpId()).split(",");
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpIds);
		}else{
			//用户满足对应查询权限的单位数据
			// String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "sc.id", params);
			// sql.append("and ").append(corpAuthSql);
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpAuthList);
		}
		sql.append("group by corpId order by netBalMoney asc ");
		sql.append(") info ");
		return listBySQLAliasToBean(sql.toString(),params, AbBillTransferDownNetCollectVO.class);
	}
	/**
	 * 加载票据调拨明细表信息数据
	 * @param dataTablesInput 查询入参
	 * @param corpAuthList 单位权限
	 * @return
	 */
	public DataTablesOutput<AbBillTransferDownCorpDtlVO> listCorpDtlInfo(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map <String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from(");
		sql.append("select  ");
		sql.append("BT_NVL(c.corp_id,b.recevie_corp_id) as corpId, ");
		// sql.append("sc.code as corpCode, ");
		// sql.append("sc.name as corpName, ");
		// sql.append("cn.code as netCode,  ");
		// sql.append("cn.name as netName, ");
		sql.append("a.business_date as businessDate, ");
		sql.append("a.create_date as createDate, ");
		sql.append("a.brief_id as briefId, ");
		sql.append("case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6201+"' then BT_NVL_NUM(d.bill_money,0) else 0 end as shMoney, ");
		sql.append("case a.brief_id when '"+AccebillConstant.BRIEF_STATUS_6202+"' then BT_NVL_NUM(d.bill_money,0) else 0 end as gjMoney, ");
		sql.append("BT_NVL_NUM(b.allocation_money,0) as xbMoney, ");
		sql.append("b.allocated_code as allocatedCode  ");
		sql.append("from ab_accrecords a  ");
		sql.append("left join ab_bill_transfer b on a.trid=b.id ");
		sql.append("left join ab_voucherinfo_vice c on c.id=a.vvid ");
		sql.append("left join ab_voucherinfo d on d.id=a.vid ");
		// sql.append("left join sys_corp sc on sc.id=c.corp_id or (c.corp_id is null and sc.id=b.recevie_corp_id) ");
		// sql.append("left join sys_corp cn on cn.id=sc.net_id ");
		sql.append("where (brief_id in (:brief_id1) or (brief_id=:brief_id2 and b.id is not null)) ");
		sql.append("and a.status=:status and a.opt_status=:opt_status  ");
		params.put("brief_id1",new String[]{AccebillConstant.BRIEF_STATUS_6201,AccebillConstant.BRIEF_STATUS_6202});
		params.put("brief_id2",AccebillConstant.BRIEF_STATUS_6204);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("opt_status",AccebillConstant.AB_BILL_OPT_STATUS19);
		Map formData=dataTablesInput.getFormdata();
		if(formData.containsKey("beginDate") && StringUtils.isNotEmpty((String) formData.get("beginDate"))){
			sql.append("and a.business_date>=:beginDate ");
			params.put("beginDate", DateUtils.parse((String) formData.get("beginDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE));
			formData.remove("beginDate");
		}
		if(formData.containsKey("endDate") && StringUtils.isNotEmpty((String) formData.get("endDate"))){
			sql.append("and a.business_date<:endDate ");
			params.put("endDate", DateUtils.addDay(DateUtils.parse((String) formData.get("endDate"),DateUtils.PATTERN_CLASSICAL_SIMPLE),1));
			formData.remove("endDate");
		}
		if(formData.containsKey("corpId") && StringUtils.isNotEmpty(formData.get("corpId"))){
			String[] corpIds=formData.get("corpId").toString().split(",");
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpIds);
			formData.remove("corpId");
		}else{
			//用户满足对应查询权限的单位数据
			// String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "sc.id", params);
			// sql.append("and ").append(corpAuthSql);
			sql.append("and (c.corp_id in (:corpId) or (c.corp_id IS NULL AND b.recevie_corp_id in (:corpId)))");
			params.put("corpId",corpAuthList);
		}
		sql.append(") info ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillTransferDownCorpDtlVO.class, "", true,params);
	}
}
