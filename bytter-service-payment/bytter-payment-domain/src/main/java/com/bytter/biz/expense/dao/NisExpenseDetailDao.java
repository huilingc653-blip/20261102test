package com.bytter.biz.expense.dao;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 费用报销表
 * @date 2017-05-31 16:04:01
 */
@Repository
public class NisExpenseDetailDao extends BaseDao<NisExpenseDetail, String> {
	/**
	 * 费用报销表通用查@param sqlWhere
	 *
	 * @return
	 */
	private String getCommonSql() {
		StringBuilder sql = new StringBuilder();
		sql.append("select n.id as id,n.expense_Id as expenseId,n.corp_Id as corpId,n.expense_Date as expenseDate,n.work_Id as workId,n.user_Name as userName,n.dept_Name as deptName,");
		sql.append(" n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.op_Type as opType,o.op_Type_Name as opTypeName,t.id as busTypeId,o.id as opTypeId,n.bif_Code as bifCode,");//i.name as bifName,");
		sql.append(" n.dif_Bank as difBank,n.area_Sign as areaSign,n.expense_Money as expenseMoney,n.bank_Code as bankCode,n.bank_Name as bankName,n.card_Type as cardType,");
		sql.append(" n.prov as prov,n.city as city,n.bank_Acc as bankAcc,n.acc_Name as accName,n.purpose as purpose,n.rmk as rmk,n.bill_Id as billId,b.bill_No as billNo,");
		sql.append(" n.bill_From as billFrom,n.status as status,n.erp_Id as erpId,n.voucher_Stat as voucherStat,n.first_return_Msg as firstReturnMsg,n.next_return_Msg as nextReturnMsg,n.return_Time as returnTime,");
		sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,b.voucher_Stat as excVoucherStat");
		//sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,e.voucher_No as voucherNo,e.voucher_Stat as excVoucherStat");
		sql.append(" from Nis_Expense_Detail n left join Nis_Billhead b on n.bill_Id = b.id");
		sql.append(" left join Nis_Business_Type t on n.bus_Type = t.bus_Type");
		sql.append(" left join Nis_Operation_Type o on n.op_Type = o.op_Type and o.bus_Type = t.bus_Type");
		//sql.append(" left join Bis_exc e on b.id = e.cbs_bill_id and e.voucher_From = '" + BdlinkConstant.FROM_NIS_PAY + "'");
		sql.append(" where 1=1");
		return sql.toString();
	}
	/**
	 * 查询报销单据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisExpenseDetailVO.class, "n", false);
	}
	/**
	 * 费用报销查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisExpenseDetailVO.class, "n", false);
	}
	/**
	 * 根据ID获取报销信息
	 *
	 * @param id
	 * @return
	 */
	public NisExpenseDetailVO getByIdUnion(String id) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and n.id=:id ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, NisExpenseDetailVO.class);
	}
	/**
	 * 根据付款Id获取费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillId(String billId) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and n.bill_Id=:billId ");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billId", billId);
		sql.append(" order by n.expense_Id desc");
		return listBySQLAliasToBean(sql.toString(), params, NisExpenseDetailVO.class);
	}
	/**
	 * 需审核列表数据
	 *
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	public DataTablesOutput<NisExpenseDetailVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		if(!EmptyUtils.isEmpty(businessSql)){
			sql.append(" and ").append(businessSql);
		}
		String flowStatus = dataTablesInput.getParameter().get("flowStatus");
		//已审批不过滤状态
		if(!IWorkFlowService.PASS.equals(flowStatus)) {
			//过滤已审数据
			sql.append(" and n.status<> ").append(NetbankConstant.STATUE_CHECK);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), NisExpenseDetailVO.class, "n", false);
	}
	/**
	 * 根据付款Id获取交易失败的费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillIdFailure(String billId) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and n.bill_Id=:billId and n.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "')");
		sql.append(" order by n.expense_Id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billId", billId);
		return listBySQLAliasToBean(sql.toString(), params, NisExpenseDetailVO.class);
	}
	/**
	 * 根据付款Id获取交易失败的费用报销明细列表
	 *
	 * @param billId
	 * @return
	 */
	public List<NisExpenseDetailVO> listByBillIdWaitResult(String billId) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and n.bill_Id=:billId and n.voucher_Stat = '"+ BdlinkConstant.WAIT_RESULT + "'");
		sql.append(" order by n.expense_Id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("billId", billId);
		return listBySQLAliasToBean(sql.toString(), params, NisExpenseDetailVO.class);
	}
	/**
	 * 根据单位统计费用报销单
	 *
	 * @param corpId
	 * @return
	 */
	public int getExpenseCountByCorpId(String corpId) {
		String sql = "select count(*) from nis_expense_detail where corp_id=:corpId";
		HashMap<String, Object> params = new HashMap<>();
		params.put("corpId", corpId);
		//0-暂存、11-待审批、-1-审批打回、95-审批通过/待汇总
		sql += " and status in ('0','11','-1','95')";
		return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
	}
	/**
	 * 获取费用报销单总金额
	 *
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumExpenseMoneyByBillId(String billId) {
		StringBuilder sql = new StringBuilder("select sum(expense_Money) as expenseMoney from nis_expense_detail n");
		sql.append(" where n.bill_id=:billId ");
		HashMap params = new HashMap();
		params.put("billId", billId);
		Object sumExpenseMoneyObj = this.createSQLQuery(sql.toString(), params).uniqueResult();
		//默认-1，表示明细还未生成（主要考虑未设置审批流程且自动发送指令的极端情况，事务未提交获取不到明细无需校验）
		BigDecimal sumExpenseMoney = new BigDecimal(-1);
		if (!EmptyUtils.isEmpty(sumExpenseMoneyObj)) {
			sumExpenseMoney = (BigDecimal) sumExpenseMoneyObj;
		}
		return sumExpenseMoney;
	}
	/**
	 * 获取费用报销单成功总金额
	 *
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumSuccessExpenseMoneyByBillId(String billId) {
		StringBuilder sql = new StringBuilder("select sum(expense_Money) as expenseMoney from nis_expense_detail n");
		sql.append(" where n.bill_id=:billId and n.voucher_Stat ='"+BdlinkConstant.SUCCESS+"'");
		HashMap params = new HashMap();
		params.put("billId", billId);
		Object sumExpenseMoneyObj = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal sumExpenseMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(sumExpenseMoneyObj)) {
			sumExpenseMoney = (BigDecimal) sumExpenseMoneyObj;
		}
		return sumExpenseMoney;
	}
	/**
	 * 获取费用报销单失败总金额
	 *
	 * @param billId
	 * @return
	 */
	public BigDecimal getSumFailExpenseMoneyByBillId(String billId) {
		StringBuilder sql = new StringBuilder("select sum(expense_Money) as expenseMoney from nis_expense_detail n");
		sql.append(" where n.bill_id=:billId and n.voucher_Stat in ('"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		HashMap params = new HashMap();
		params.put("billId", billId);
		Object sumExpenseMoneyObj = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal sumExpenseMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(sumExpenseMoneyObj)) {
			sumExpenseMoney = (BigDecimal) sumExpenseMoneyObj;
		}
		return sumExpenseMoney;
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId){
		StringBuilder hql=new StringBuilder("update NisExpenseDetail set voucherStat=:voucherStat");
		hql.append(" where billId=:billId and voucherStat in ('"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("billId", billId);
		params.put("voucherStat", BdlinkConstant.PRE_SEND);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @return
	 */
	public int updateVoucherStatByBillIdQueryResult(String billId){
		StringBuilder hql=new StringBuilder("update NisExpenseDetail set voucherStat=:voucherStat");
		hql.append(" where billId=:billId and voucherStat in ('"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("billId", billId);
		params.put("voucherStat", BdlinkConstant.WAIT_RESULT);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId, String voucherStat){
		StringBuilder hql=new StringBuilder("update NisExpenseDetail set voucherStat=:voucherStat");
		hql.append(" where billId=:billId and voucherStat = '"+BdlinkConstant.WAIT_RESULT+"'");
		Map<String,Object> params = new HashMap<>();
		params.put("billId", billId);
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param billId 付款单号
	 * @param voucherStat 修改的指令
	 * @return
	 */
	public int updateVoucherStatByBillId(String billId, String voucherStat, List<String> expenseIds, boolean isIn){
		StringBuilder hql=new StringBuilder("update NisExpenseDetail set voucherStat=:voucherStat");
		hql.append(" where billId=:billId and voucherStat = '"+BdlinkConstant.WAIT_RESULT+"'");
		if(isIn){
			String idAuthSql = SqlUtil.getInSql(expenseIds, "id", true);
			hql.append(" and ").append(idAuthSql);
		}else{
			String idAuthSql = SqlUtil.getNotinSql(expenseIds, "id", true);
			hql.append(" and ").append(idAuthSql);
		}
		Map<String,Object> params = new HashMap<>();
		params.put("billId", billId);
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 带原状态更新费用报销单
	 * @param vo 费用报销对象
	 * @param oldStatus 原状态控制
	 * @return
	 */
	public int updateNisExpenseDetailStatus(NisExpenseDetailVO vo, Integer[] oldStatus) {
		StringBuilder hql=new StringBuilder();
		hql.append("update NisExpenseDetail set corpId=:corpId,expenseDate=:expenseDate,workId=:workId,userName=:userName,");
		hql.append(" deptName=:deptName,opType=:opType,busType=:busType,bifCode=:bifCode,");
		hql.append(" difBank=:difBank,areaSign=:areaSign,expenseMoney=:expenseMoney,bankCode=:bankCode,");
		hql.append(" bankName=:bankName,cardType=:cardType,bankAcc=:bankAcc,accName=:accName,prov=:prov,");
		hql.append(" city=:city,purpose=:purpose,rmk=:rmk,billId=:billId,voucherStat=:voucherStat,billFrom=:billFrom,");
		hql.append(" erpId=:erpId,status=:status,attachmentId=:attachmentId");
		Map<String,Object> params = new HashMap<>();
		params.put("corpId", vo.getCorpId());
		params.put("expenseDate", vo.getExpenseDate());
		params.put("workId", vo.getWorkId());
		params.put("userName", vo.getUserName());
		params.put("deptName", vo.getDeptName());
		params.put("opType", vo.getOpType());
		params.put("busType", vo.getBusType());
		params.put("bifCode", vo.getBifCode());
		params.put("difBank", vo.getDifBank());
		params.put("areaSign", vo.getAreaSign());
		params.put("expenseMoney", vo.getExpenseMoney());
		params.put("bankCode", vo.getBankCode());
		params.put("bankName", vo.getBankName());
		params.put("cardType", vo.getCardType());
		params.put("bankAcc", vo.getBankAcc());
		params.put("accName", vo.getAccName());
		params.put("prov", vo.getProv());
		params.put("city", vo.getCity());
		params.put("purpose", vo.getPurpose());
		params.put("rmk", vo.getRmk());
		params.put("billId", vo.getBillId());
		params.put("voucherStat", vo.getVoucherStat());
		params.put("billFrom", vo.getBillFrom());
		params.put("erpId", vo.getErpId());
		params.put("status", vo.getStatus());
		params.put("attachmentId", vo.getAttachmentId());
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
		hql.append("where id=:id and status in (:oldStatus) ");
		params.put("id", vo.getId());
		params.put("oldStatus", oldStatus);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 获取ERP待处理单据
	 * @return
	 */
	public List<NisExpenseDetailVO> listErpTodoBill() {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and n.bill_From=:billFrom and n.status=:status");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("billFrom", ExpenseConstant.BILLFROM_2);
		params.put("status", ExpenseConstant.STATUE_TEMP);
		return listBySQLAliasToBean(sql.toString(), params, NisExpenseDetailVO.class);
	}
}
