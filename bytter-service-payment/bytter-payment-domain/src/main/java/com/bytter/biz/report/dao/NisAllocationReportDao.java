package com.bytter.biz.report.dao;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BIS_EXC
 * @date 2017-10-09 16:31:56
 */
@Repository
public class NisAllocationReportDao extends BaseDao<BisExcDTO, String> {
	/**
	 * 资金调拨列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from (");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, b1.voucher_from as voucherFrom, b1.req_date as reqDate, b1.cur_id as curId, ");
		sql.append(" b1.pay_money as payMoney, b1.purpose as purpose, b1.payer_acc as payerAcc, b1.payer_acc_name as payerAccName,b1.payer_bank as payerBank, ");
		sql.append(" b1.payee_acc as payeeAcc, b1.payee_acc_name as payeeAccName, b1.payee_bank as payeeBank, acc2.corp_id as oppCorpId from bis_exc b1 ");
		sql.append(" left join sys_corp c on b1.corp_id = c.id left join bt_bank_acc acc2 on b1.payee_acc = acc2.bank_acc ");
		sql.append(" where 1=1 and b1.VOUCHER_STAT='0'");
		String sql1 = joinSql.replace("t.corpId","c.id");
		sql1 = sql1.replace("t.voucherFrom","b1.voucher_from");
		sql.append(sql1);
		if(dataTablesInput != null && dataTablesInput.getFormdata() != null){
			Map<String,Object> map = dataTablesInput.getFormdata();
			if(map.get("eqs_voucherFrom") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("eqs_voucherFrom").toString())){
				sql.append(" and b1.voucher_from = '"+map.get("eqs_voucherFrom")+"'");
			}
			/*if(map.get("ged_reqDate") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ged_reqDate").toString())){
				sql.append(" and to_date('"+map.get("ged_reqDate")+"','yyyy-MM-dd') <= b1.req_date");
			}*/
			if(map.get("ins_corpId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ins_corpId").toString())){
				String[] arr = map.get("ins_corpId").toString().split(",");
				String s = org.apache.commons.lang.StringUtils.join(arr,"','");
				s = "('" + s + "')";
				sql.append(" and c.id in "+s);
			}
			if(map.get("eqs_curId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("eqs_curId").toString())){
				sql.append(" and b1.cur_id = '"+map.get("eqs_curId")+"'");
			}
			if(map.get("geg_payMoney") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("geg_payMoney").toString())){
				sql.append(" and "+map.get("geg_payMoney")+" <= b1.pay_money");
			}
			if(map.get("leg_payMoney") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("leg_payMoney").toString())){
				sql.append(" and b1.pay_money <= " + map.get("leg_payMoney"));
			}
			if(map.get("ins_oppCorpId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ins_oppCorpId").toString())){
				String[] arr = map.get("ins_oppCorpId").toString().split(",");
				String s = org.apache.commons.lang.StringUtils.join(arr,"','");
				s = "('" + s + "')";
				sql.append(" and acc2.corp_id in "+s);
			}
		}
		sql.append(" union all ");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, b2.voucher_from as voucherFrom, case when b2.return_time is null then ar.create_date else b2.return_time end regDate, ");
		sql.append(" b2.cur_id as curId, b2.gather_amt as payMoney, b2.purpose, acc1.bank_acc as payerAcc, acc1.acc_name as payerAccName, acc1.bank_name as payerBank, ");
		sql.append(" acc2.bank_acc as payeeAcc, acc2.acc_name as payeeAccName, acc2.bank_name as payeeBank, acc1.corp_id as oppCorpId from bis_gather_cmd b2  ");
		sql.append(" left join bt_bank_acc acc1 on b2.child_acc_id = acc1.id left join bt_bank_acc acc2 on b2.parent_acc_id = acc2.id ");
		sql.append(" left join sys_corp c on acc1.corp_id = c.id left join nis_allocation_req ar on ar.id = b2.cbs_bill_id ");
		sql.append(" where 1=1 and b2.CMD_STAT = '0'");
		String sql2 = joinSql.replace("t.corpId","c.id");
		sql2 = sql2.replace("t.voucherFrom","b2.voucher_from");
		sql.append(sql2);
		if(dataTablesInput != null && dataTablesInput.getFormdata() != null){
			Map<String,Object> map = dataTablesInput.getFormdata();
			if(map.get("eqs_voucherFrom") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("eqs_voucherFrom").toString())){
				sql.append(" and b2.voucher_from = '"+map.get("eqs_voucherFrom")+"'");
				map.remove("eqs_voucherFrom");
			}
			/*if(map.get("ged_reqDate") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ged_reqDate").toString())){
				sql.append(" and to_date('"+map.get("ged_reqDate")+"','yyyy-MM-dd') <= b1.req_date");
			}*/
			if(map.get("ins_corpId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ins_corpId").toString())){
				String[] arr = map.get("ins_corpId").toString().split(",");
				String s = org.apache.commons.lang.StringUtils.join(arr,"','");
				s = "('" + s + "')";
				sql.append(" and c.id in "+s);
				map.remove("ins_corpId");
			}
			if(map.get("eqs_curId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("eqs_curId").toString())){
				sql.append(" and b2.cur_id = '"+map.get("eqs_curId")+"'");
				map.remove("eqs_curId");
			}
			if(map.get("geg_payMoney") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("geg_payMoney").toString())){
				sql.append(" and "+map.get("geg_payMoney")+" <= b2.gather_amt");
				map.remove("geg_payMoney");
			}
			if(map.get("leg_payMoney") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("leg_payMoney").toString())){
				sql.append(" and b2.gather_amt <= " + map.get("leg_payMoney"));
				map.remove("leg_payMoney");
			}
			if(map.get("ins_oppCorpId") != null && org.apache.commons.lang.StringUtils.isNotBlank(map.get("ins_oppCorpId").toString())){
				String[] arr = map.get("ins_oppCorpId").toString().split(",");
				String s = org.apache.commons.lang.StringUtils.join(arr,"','");
				s = "('" + s + "')";
				sql.append(" and acc1.corp_id in "+s);
				map.remove("ins_oppCorpId");
			}
		}
		sql.append(" ) t  where 1 = 1 ");
		//sql.append(joinSql);
		dataTablesInput.setOrderString(new OrderString("t.corpCode", "asc"));
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisAllocationReportVO.class, "", false);
	}
	/**
	 * 根据条件查询明细
	 *
	 * @param netId        网点ID
	 * @param corpId       单位ID
	 * @param curId        币别ID
	 * @param beginReqDate 开始日期
	 * @param endReqDate   结束日期
	 * @param joinSql      业务类型范围
	 * @return
	 */
	public List<NisAllocationReportVO> listBy(String netId, String corpId, String curId, Date beginReqDate, Date endReqDate, String joinSql) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append("select t.corpid, t.curid, (sum((case when t.voucherfrom = 2 then t.paymoney else 0 end)) + sum((case when t.voucherfrom = 7 then t.paymoney else 0 end))) downPayMoney,");
		sql.append(" (sum((case when t.voucherfrom = 5 then t.paymoney else 0 end)) + sum((case when t.voucherfrom = 8 then t.paymoney else 0 end))) upPayMoney, ");
		sql.append(" sum((case when t.voucherfrom = 3 then t.paymoney else 0 end)) askPayMoney, sum((case when t.voucherfrom = 4 then t.paymoney else 0 end)) posPayMoney,");
		sql.append(" sum((case when t.voucherfrom = 6 then t.paymoney else 0 end)) turnPayMoney from (");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, c.net_id as netId, b1.voucher_from as voucherFrom, b1.req_date as reqDate, b1.cur_id as curId, ");
		sql.append(" b1.pay_money as payMoney, b1.purpose as purpose, b1.payer_acc as payerAcc, b1.payer_acc_name as payerAccName,b1.payer_bank as payerBank, ");
		sql.append(" b1.payee_acc as payeeAcc, b1.payee_acc_name as payeeAccName, b1.payee_bank as payeeBank, acc2.corp_id as oppCorpId from bis_exc b1 ");
		sql.append(" left join sys_corp c on b1.corp_id = c.id left join bt_bank_acc acc2 on b1.payee_acc = acc2.bank_acc ");
		sql.append(" where b1.voucher_stat = '0' ");
		sql.append(" union all ");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, c.net_id as netId, b2.voucher_from as voucherFrom, b2.return_time as reqDate, b2.cur_id as curId, ");
		sql.append(" b2.gather_amt as payMoney, b2.purpose, acc1.bank_acc as payerAcc, acc1.acc_name as payerAccName, acc1.bank_name as payerBank, acc1.corp_id as oppCorpId, ");
		sql.append(" acc2.bank_acc as payeeAcc, acc2.acc_name as payeeAccName, acc2.bank_name as payeeBank from bis_gather_cmd b2  ");
		sql.append(" left join bt_bank_acc acc1 on b2.child_acc_id = acc1.id left join bt_bank_acc acc2 on b2.parent_acc_id = acc2.id ");
		sql.append(" left join sys_corp c on acc1.corp_id = c.id ");
		sql.append(" where b2.cmd_stat = '0' ");
		sql.append(" ) t  where 1 = 1 ");
		sql.append(joinSql);
		if (!EmptyUtils.isBlank(netId)) {
			String[] netIds = netId.split(CommonConstant.COMMA_SPLIT);
			String corpSql = SqlUtil.getInSql(netIds, "t.corpId", true);
			String netSql = SqlUtil.getInSql(netIds, "t.netId", true);
			sql.append(" and ( " + corpSql + "or" + netSql + ")");
		}
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and " + SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "t.corpId", true));
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and " + SqlUtil.getInSql(curId.split(CommonConstant.COMMA_SPLIT), "t.curId", true));
		}
		if (!EmptyUtils.isEmpty(beginReqDate)) {
			sql.append(" and t.reqDate >= :beginReqDate ");
			params.put("beginReqDate", beginReqDate);
		}
		if (!EmptyUtils.isEmpty(endReqDate)) {
			sql.append(" and t.reqDate <= :endReqDate ");
			params.put("endReqDate", endReqDate);
		}
		sql.append(" group by t.corpId,t.curId ");
		return this.listBySQLAliasToBean(sql.toString(), params, NisAllocationReportVO.class);
	}
	/**
	 * 指令列表查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableSum(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.corpid, t.corpcode as corpCode ,t.corpname as corpName, t.curid, (sum((case when t.voucherfrom = 2 then t.paymoney else 0 end)) + sum((case when t.voucherfrom = 7 then t.paymoney else 0 end))) downPayMoney,");
		sql.append(" (sum((case when t.voucherfrom = 5 then t.paymoney else 0 end)) + sum((case when t.voucherfrom = 8 then t.paymoney else 0 end))) upPayMoney, ");
		sql.append(" sum((case when t.voucherfrom = 3 then t.paymoney else 0 end)) askPayMoney, sum((case when t.voucherfrom = 4 then t.paymoney else 0 end)) posPayMoney,");
		sql.append(" sum((case when t.voucherfrom = 6 then t.paymoney else 0 end)) turnPayMoney from (");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, c.net_id as netId, b1.voucher_from as voucherFrom, b1.req_date as reqDate, b1.cur_id as curId, ");
		sql.append(" b1.pay_money as payMoney, b1.purpose as purpose, b1.payer_acc as payerAcc, b1.payer_acc_name as payerAccName,b1.payer_bank as payerBank, ");
		sql.append(" b1.payee_acc as payeeAcc, b1.payee_acc_name as payeeAccName, b1.payee_bank as payeeBank, acc2.corp_id as oppCorpId from bis_exc b1 ");
		sql.append(" left join sys_corp c on b1.corp_id = c.id left join bt_bank_acc acc2 on b1.payee_acc = acc2.bank_acc ");
		sql.append(" where b1.voucher_stat = '0' ");
		sql.append(" union all ");
		sql.append(" select c.id as corpId, c.code as corpCode, c.name as corpName, c.net_id as netId, b2.voucher_from as voucherFrom, case when b2.return_time is null then ar.create_date else b2.return_time end regDate, b2.cur_id as curId, ");
		sql.append(" b2.gather_amt as payMoney, b2.purpose, acc1.bank_acc as payerAcc, acc1.acc_name as payerAccName, acc1.bank_name as payerBank, acc1.corp_id as oppCorpId, ");
		sql.append(" acc2.bank_acc as payeeAcc, acc2.acc_name as payeeAccName, acc2.bank_name as payeeBank from bis_gather_cmd b2  ");
		sql.append(" left join bt_bank_acc acc1 on b2.child_acc_id = acc1.id left join bt_bank_acc acc2 on b2.parent_acc_id = acc2.id ");
		sql.append(" left join sys_corp c on acc1.corp_id = c.id ");
		sql.append(" LEFT JOIN nis_allocation_req ar ON ar.id = b2.cbs_bill_id  ");
		sql.append(" where b2.cmd_stat = '0' ");
		sql.append(" ) t  where 1 = 1 ");
		sql.append(joinSql);
		String allCondition = getWhereCondition(dataTablesInput, new NisAllocationReportVO(), "", false);
		Map<String, Object> params = dataTablesInput.getSearchValues(null);
		sql.append(allCondition);
		sql.append(" group by t.corpId, t.curId, t.corpcode, t.corpname ");
		sql.append(" order by t.corpcode, t.curid ");
		return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, NisAllocationReportVO.class);
	}
	/**
	 * 手机报表数据列表
	 * @param vo
	 * @param joinSql
	 * @return
	 */
	public List<NisAllocationReportVO> getReportMoblieList(NisAllocationReportVO vo, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select t.* from (");
		sql.append(" select b1.id as id, c.id as corpId, c.code as corpCode, c.name as corpName, b1.voucher_from as voucherFrom, b1.req_date as reqDate, b1.cur_id as curId,cur.CUR_NAME as curName,cur.cur_sign as curSign, ");
		sql.append(" b1.pay_money as payMoney, b1.purpose as purpose, b1.payer_acc as payerAcc, b1.payer_acc_name as payerAccName,b1.payer_bank as payerBank, ");
		sql.append(" b1.payee_acc as payeeAcc, b1.payee_acc_name as payeeAccName, b1.payee_bank as payeeBank, acc2.corp_id as oppCorpId from bis_exc b1 ");
		sql.append(" left join sys_corp c on b1.corp_id = c.id left join bt_bank_acc acc2 on b1.payee_acc = acc2.bank_acc ");
		sql.append(" left join bt_currency cur on b1.cur_id = cur.id ");
		sql.append(" where 1=1 and b1.VOUCHER_STAT='0'");
		sql.append(" union all ");
		sql.append(" select b2.id as id, c.id as corpId, c.code as corpCode, c.name as corpName, b2.voucher_from as voucherFrom, case when b2.return_time is null then ar.create_date else b2.return_time end regDate, ");
		sql.append(" b2.cur_id as curId,cur.CUR_NAME as curName, cur.cur_sign as curSign, b2.gather_amt as payMoney, b2.purpose, acc1.bank_acc as payerAcc, acc1.acc_name as payerAccName, acc1.bank_name as payerBank, ");
		sql.append(" acc2.bank_acc as payeeAcc, acc2.acc_name as payeeAccName, acc2.bank_name as payeeBank, acc1.corp_id as oppCorpId from bis_gather_cmd b2  ");
		sql.append(" left join bt_bank_acc acc1 on b2.child_acc_id = acc1.id left join bt_bank_acc acc2 on b2.parent_acc_id = acc2.id ");
		sql.append(" left join sys_corp c on acc1.corp_id = c.id left join nis_allocation_req ar on ar.id = b2.cbs_bill_id ");
		sql.append(" left join bt_currency cur on b2.cur_id = cur.id ");
		sql.append(" where 1=1 and b2.CMD_STAT = '0'");
		sql.append(" ) t  where 1 = 1 ");
		sql.append(joinSql);
		Map<String, Object> params = new HashMap<>();
		if(StringUtils.isNotBlank(vo.getCorpId())){
			sql.append(" and ").append(SqlUtil.getInSql(vo.getCorpId().split(CommonConstant.COMMA_SPLIT), "t.corpId", true));
		}
		if(!EmptyUtils.isEmpty(vo.getCurId())){
			sql.append(" and t.curId = :curId");
			params.put("curId",vo.getCurId());
		}
		if(!EmptyUtils.isEmpty(vo.getStartDateStr())){
			sql.append(" and bt_to_char (t.reqDate, 'YYYY-MM-DD' ) >= :startDate");
			params.put("startDate", vo.getStartDateStr());
		}
		if(!EmptyUtils.isEmpty(vo.getEndDateStr())){
			sql.append(" and bt_to_char (t.reqDate, 'YYYY-MM-DD' ) <= :endDate");
			params.put("endDate", vo.getEndDateStr());
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherFrom())){
			sql.append(" and t.voucherFrom = :voucherFrom");
			params.put("voucherFrom",vo.getVoucherFrom());
		}
		if(!EmptyUtils.isEmpty(vo.getId())){
			sql.append(" and t.id = :id");
			params.put("id",vo.getId());
		}
		sql.append(" order by t.corpCode asc" );
		return listBySQLAliasToBean(sql.toString(), params, NisAllocationReportVO.class);
	}
}
