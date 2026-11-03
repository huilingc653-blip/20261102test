package com.bytter.biz.finacial.accbook.dao;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlned;
import com.bytter.biz.finacial.accbook.vo.CentralityDataVO;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcCompareAccDTO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: FC_ACCBOOK_BLNED
 * @author onlineGenerator
 * @date 2017-06-19 11:28:14
 * @version 1.0
 *
 */
@Repository
public class FcAccbookBlnedDao extends FcAccbookBaseDao<FcAccbookBlned, String> {
	@Override
	protected String getAccbookTable() {
		return "fc_accbook_blned";
	}
	@Override
	protected String getAccbookDtlTable() {
		return "fc_accbook_blned_dtl";
	}
	@Override
	protected String getAccbookEntity() {
		return "com.bytter.biz.finacial.accbook.entity.FcAccbookBlned";
	}
	@Override
	protected String getAccbookDtlEntity() {
		return "com.bytter.biz.finacial.accbook.entity.FcAccbookBlnedDtl";
	}
	
	/**
	 * 功能介绍 : 根据原凭证号查询已计提过手续费的手续费凭证数据。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List findTargetChargeAccbooksBy(String bookId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select accbook.id from fc_accbook_blned accbook,fc_charge_accrue charge ")
				.append("where accbook.id = charge.order_no and charge.target_order_no = :orderNo ")
				.append("and accbook.bus_ori_type = :oriType and charge.tally_sign = :tallySign ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", bookId);
		params.put("oriType", FinaBookConstant.BusOriType.HANDLE_CHARGE.get());
		params.put("tallySign", FinacialConstant.TALLY_SIN_Y);
		return listBySQL(sql.toString(), params);
	}
	/**
	 * 更新为已修改过现金流项目
	 * @param prams
	 */
	public void updateChangeCashflux(Map<String,Object> prams){
		StringBuilder sql = new StringBuilder();
		sql.append("update FC_ACCBOOK_BLNED set IS_CHANGE_CASHFLUX = '1' where id = :id");
		createSQLQuery(sql.toString(), prams).executeUpdate();
	}
	/**
	 * 对账查询日结后账务数据 用于中心对账
	 * @param dataTablesInput
	 * @param accList
	 * @return
	 */
	public DataTablesOutput listGenAcc(DataTablesInput dataTablesInput, List<String> accList) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select accbook.voucher_no as voucherNo, dtl_info.account as compAcc, accbook.tally_date as tallyDate, dtl_info.debit_money as debitMoney, dtl_info.credit_money as creditMoney, dtl_info.balance as balance, ");
		sql.append(" accbook.redblue_sign as redblueSign, dtl_info.summary_content as summaryContent,dtl_info.orig_voucher_no as billNo, accbook.order_no as orderNo,dtl_info.line_no as lineNo  from ");
		//TODO                                                                                              流水号的获取暂时用原始凭证
		//已结流水账主档表  已结流水账明细表  内部账户信息表
		sql.append(" fc_accbook_blned accbook , fc_accbook_blned_dtl dtl_info ");
		sql.append(" where  accbook.id = dtl_info.book_id ");
		sql.append(" and accbook.voucher_state <> :voucherState ");
		//TODO 权限过滤
		sql.append(" and ").append(getSQLIn(accList, 900, "dtl_info.account"));
		//拼接页面查询条件
		sql.append(getWhereCondition(dataTablesInput, new CentralityDataVO(), null, false));
		sql.append(" order by dtl_info.account,accbook.tally_date,accbook.voucher_no ");
		params.put("voucherState", "B");
		params.putAll(dataTablesInput.getSearchValues());
		return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CentralityDataVO.class);
	}
	/**
	 * 内部账号凭证保存为对账单数据
	 * @param params
	 * @param accList
	 * @param orderNos
	 * @return
	 */
	public List<FcCompareAccDTO> queryCompareAcc(Map<String,Object> params, List<String> accList, List<String> orderNos){
		StringBuilder sql = new StringBuilder();
		sql.append(" select accbook.order_no as orderNo, accbook.voucher_no as voucherNo, accbook.tally_date as tallyDate, accbook.interest_start_date as interestStartDate, dtl_info.summary_content as summaryContent,");
		sql.append(" dtl_info.account as compAcc, dtl_info.debit_money as debitMoney, dtl_info.credit_money as creditMoney, dtl_info.balance as balance, accbook.redblue_sign as redblueSign, dtl_info.line_no as lineNo,");
		sql.append(" accbook.cur_code as curId  ");
		sql.append(" from fc_accbook_blned accbook, fc_accbook_blned_dtl dtl_info");
		sql.append(" where accbook.id = dtl_info.book_id ");
		sql.append(" and accbook.voucher_state <> :voucherState ");//状态过滤
		if (StringUtils.isNotEmpty(params.get("queryBeginDate")) && StringUtils.isNotEmpty(params.get("queryEndDate"))){
			sql.append(" and accbook.tally_Date >= :queryBeginDate and accbook.tally_Date <= :queryEndDate ");
		}
		if(EmptyUtils.isNotEmpty(accList)){
			sql.append(" and ").append(getSQLIn(accList, 900, "dtl_info.account"));
		}
		if(EmptyUtils.isNotEmpty(orderNos)){
			sql.append(" and ").append(SqlUtil.getNotinSql(orderNos,"accbook.order_no",true));
		}
		params.put("voucherState", "B");
		List<FcCompareAccDTO> list = listBySQLAliasToBean(sql.toString(), params, FcCompareAccDTO.class);
		return list;
	}
	public FcAccbookDTO queryCompareAcc(FcAccbookDTO dto){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.voucher_no,a.GENERATE_MODE  ");
		sql.append(" from fc_accbook a ");
		sql.append(" where  1=1");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(dto.getVoucherNo())){
			sql.append(" and a.VOUCHER_NO =:VOUCHER_NO ");
			params.put("VOUCHER_NO",dto.getVoucherNo());
		}
		sql.append(" union  ");
		sql.append(" select b.voucher_no,b.GENERATE_MODE  ");
		sql.append(" from fc_accbook_blned b");
		sql.append(" where  1=1");
		if (StringUtils.isNotEmpty(dto.getVoucherNo())){
			sql.append(" and b.VOUCHER_NO =:VOUCHER_NO ");
			params.put("VOUCHER_NO",dto.getVoucherNo());
		}
		return getBySQLAliasToBean(sql.toString(), params, FcAccbookDTO.class);
	}
	public List<FcCompareAccDTO> getAccbookDtlsByOrigVoucherNo(String origVoucherNo) {
//		String hql = "select t,item.itemName as cashFluxName from FcAccbookDtl t left join FcCashItem item on t.cashFlux = item.id where t.bookId = :bookId order by t.lineNo";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ORIG_VOUCHER_NO", origVoucherNo);
//		return listByHQL(hql, params);
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.VOUCHER_NO from FC_ACCBOOK_BLNED t where t.ORIG_VOUCHER_NO = :ORIG_VOUCHER_NO");
		List<FcCompareAccDTO> list = listBySQLAliasToBean(sql.toString(), params, FcCompareAccDTO.class);
		return list;
	}
}
