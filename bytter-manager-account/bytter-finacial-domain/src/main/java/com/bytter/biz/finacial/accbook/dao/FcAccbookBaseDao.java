package com.bytter.biz.finacial.accbook.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public abstract class FcAccbookBaseDao<T, PK extends Serializable> extends BaseDao<T, PK> {
	protected abstract String getAccbookTable();
    protected abstract String getAccbookDtlTable();
    protected abstract String getAccbookEntity();
    protected abstract String getAccbookDtlEntity();
    /**
	 * 功能介绍 : 查询可修改的凭证。<br>
	 * <b>日期：</b>2017年6月15日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public DataTablesOutput queryCanEditAccbooks(DataTablesInput input, boolean normalFlag, String... corpAuthSql) {
		/*String fields = " select main_info.voucher_state as voucherState,voucherType.id as voucherId,voucherType.voucher_name as voucherName,"
				+ " main_info.tally_date as tallyDate,main_info.interest_start_date as interestStartDate,main_info.voucher_no as voucherNo,"
				+ " main_info.order_no as orderNo,main_info.orig_voucher_no as origVoucherNo,"
				+ " main_info.bus_global_id as busGlobalId,main_info.id as id,main_info.checker_code as checkerCode,"
				+ " su.username as operatorCode,main_info.money as money,main_info.rmk as rmk,main_info.bus_race_id as busRaceId,"
				+ " main_info.bus_Ori_Type as busOriType,main_info.net_id as netId,sc.name as netName ";*/
		//增加冗余字段 su.username as operatorCode --> user_name;  sc.name as netName --> corp_name //TODO 主表不能增加字段
//		String fields = " select main_info.voucher_state as voucherState,voucherType.id as voucherId,voucherType.voucher_name as voucherName,"
		String fields = " select main_info.voucher_state as voucherState,main_info.voucher_type as voucherType,"
				+ " main_info.tally_date as tallyDate,main_info.interest_start_date as interestStartDate,main_info.voucher_no as voucherNo,"
				+ " main_info.order_no as orderNo,main_info.orig_voucher_no as origVoucherNo,"
				+ " main_info.bus_global_id as busGlobalId,main_info.id as id,main_info.checker_code as checkerCode,"
				+ " main_info.money as money,main_info.rmk as rmk,main_info.operator_code AS operatorCode,main_info.bus_race_id as busRaceId,fc.race_name AS raceName,"
				+ " main_info.bus_Ori_Type as busOriType,main_info.net_id as netId ";

		// 增加金额，备注查询条件
		String conditionsSQL =  "";
		Map<String, Object> conditionsParams = new HashMap<>();
		String geg_debitMoney = (String) input.getFormdata().get("geg_debitMoney");
		if (ObjectUtil.isNotEmpty(geg_debitMoney)){
			conditionsSQL += " and main_info.money >=:geg_debitMoney ";
			conditionsParams.put("geg_debitMoney", new BigDecimal(geg_debitMoney));
			input.getFormdata().remove("geg_debitMoney");
		}
		String leg_debitMoney = (String) input.getFormdata().get("leg_debitMoney");
		if (ObjectUtil.isNotEmpty(leg_debitMoney)){
			conditionsSQL += " and main_info.money <=:leg_debitMoney ";
			conditionsParams.put("leg_debitMoney", new BigDecimal(leg_debitMoney));
			input.getFormdata().remove("leg_debitMoney");
		}
		String likes_rmk = (String) input.getFormdata().get("likes_rmk");
		if (ObjectUtil.isNotEmpty(likes_rmk)){
			conditionsSQL += " and main_info.rmk like :likes_rmk ";
			conditionsParams.put("likes_rmk", "%" + likes_rmk + "%");
			input.getFormdata().remove("likes_rmk");
		}

		Class voClazz = FcAccbookListVO.class;
		String query = getCommonFromQuerySql(input, normalFlag);
		Map<String, Object> params = input.getSearchValues();
		String condition = "";
		if (!EmptyUtils.isEmpty(corpAuthSql)) {
			if (corpAuthSql.length >= 1) {
				query += " and "+ corpAuthSql[0];
			}
		}
		if (ObjectUtil.isNotEmpty(conditionsSQL)){
			query += conditionsSQL;
			params.putAll(conditionsParams);
		}

		Object bean = null;
        try {
            String countSQL = prepareCountSQL(fields + query);
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL, params).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
            	String order = getWhereOrderBy(input, FcAccBookOrderVO.class.newInstance(), "");
                list = listBySQL(fields + query + order, input.getStart(), input.getLength(), params);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if(null != voClazz){
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
			return new DataTablesOutput(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
	}
	protected String getCommonFromQuerySql(DataTablesInput input, boolean normalFlag) {
		String accbookTable = getAccbookTable(); // fc_accbook / fc_accbook_blned
		String accbookDtlTable = getAccbookDtlTable(); // fc_accbook_dtl / fc_accbook_blned_dtl 新增了 net_id 字段
		StringBuilder sql = new StringBuilder();
		try {
	        sql.append("from (");
	        sql.append("select accbook.voucher_state ,accbook.voucher_Type,accbook.tally_date,accbook.interest_start_date,");
	        sql.append("accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id,accbook.checker_code,accbook.orig_voucher_no,");
	        sql.append("accbook.operator_code,accbook.bus_race_id,accbook.rmk,sum(case when accbook.redblue_sign='");
	        sql.append(FinacialConstant.RUSH_SIGN_YES);
	        sql.append("' then 0-dtl.credit_money else dtl.credit_money end) as money, ");
	        sql.append(" accbook.bus_ori_type, accbook.net_id ");
	        sql.append("from " + accbookTable + " accbook, "+accbookDtlTable+" dtl,(");
	        sql.append("select distinct a.book_id,a.net_id from (");
	        sql.append("select dtl.book_id,dtl.net_id, sum(dtl.debit_money) as money from ");
	        sql.append(accbookDtlTable);
	        sql.append(" dtl where dtl.debit_money + dtl.credit_money<>0 ");
	        sql.append(getWhereCondition(input, FcAccbookDebitSearchVO.class.newInstance(), "", false));
	        sql.append(" group by dtl.book_id, dtl.net_id ");
	        sql.append(") a,(");
	        sql.append("select dtl.book_id from ");
	        sql.append(accbookDtlTable);
	        sql.append(" dtl where dtl.debit_money + dtl.credit_money<>0 ");
	        sql.append(getWhereCondition(input, FcAccbookCreditSearchVO.class.newInstance(), "", false));
	        sql.append(") b where a.book_id=b.book_id ");
	        sql.append(getWhereCondition(input, FcAccbookMoneySearchVO.class.newInstance(), "", false));
	        sql.append(") dtl_info where dtl_info.book_id=accbook.id ");
	        sql.append(" and dtl.book_id = dtl_info.book_id ");
			sql.append(getWhereCondition(input, FcAccbookDtlSearchVO.class.newInstance(), "", false));
			if (!normalFlag) {//非正常凭证业务过滤利息、收款、中心付款凭证
				//sql.append(" and accbook.bus_ori_type not in (")
						//.append(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get()).append(",")
						//.append(FinaBookConstant.BusOriType.NB_CNT_POS_CENTPAY.get()).append(" ) ");
						//.append(FinaBookConstant.BusOriType.NB_BANKDIRLINK_DEPOSIT.get()).append(" ) ");
			}
	        sql.append(" group by accbook.voucher_state,accbook.voucher_Type,accbook.tally_date,accbook.operator_code,accbook.checker_code,");
	        sql.append("accbook.bus_race_id,accbook.bus_ori_type,accbook.orig_voucher_no,accbook.rmk,accbook.net_id,");
	        sql.append("accbook.interest_start_date,accbook.voucher_no,accbook.order_no,accbook.bus_global_id,accbook.id) main_info ");
	        sql.append(" left join fc_bus_race fc on  fc.id = main_info.bus_race_id ");
//	        sql.append(getWhereCondition(input, FcBusRaceVo.class.newInstance(), "", false));
	        /*sql.append("left join sys_user su on su.id = main_info.operator_code ");
	        sql.append("left join sys_corp sc on sc.id = main_info.net_id ");*/
//	        sql.append("left join fc_voucher_type voucherType on voucherType.id = main_info.voucher_type where 1 = 1");
	        sql.append("where 1 = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
        return sql.toString();
    }
	/**
	 * 功能介绍 : 获取账务主表明细。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAccbookVoById(String id,boolean isPool) {
		StringBuilder sql = new StringBuilder();
		//fc_carry 冗余字段 username
		sql.append("select a.id as id, a.tenant_id as tenantId, a.net_id as net_id,a.order_no as order_no,a.voucher_no as voucher_no,")
				.append(" a.enrol_time as enrol_time,a.tally_date as tally_date,a.interest_start_date as interest_start_date,")
				.append(" a.voucher_type as voucher_type,a.emergency_sign as emergency_sign,")
				.append(" a.operator_code as operator_code,a.checker_code as checker_code,a.affix_amount as affix_amount,")
				.append(" a.redblue_sign as redblue_sign,a.voucher_state as voucher_state,a.rush_sign as rush_sign,")
				.append(" a.generate_mode as generate_mode,a.bus_ori_type as bus_ori_type,a.rmk as rmk,")
				.append(" a.cur_code as cur_code,a.bus_race_id as bus_race_id,c.race_name as race_name,a.attachment_Id,")
				.append(" a.is_change_cashflux isChangeCashflux,")
				.append(" c.id as raceId,a.bus_global_id as busGlobalId,(select c.user_name as username\n" +
						"                  from fc_carry c\n" +
						"                 where c.carry_type = 'D'\n" +
						"                   and c.restore_carry_optor is null\n" +
						"                   and c.voucher_start_date <= a.TALLY_DATE\n" +
						"                   and c.voucher_end_date >= a.TALLY_DATE\n" +
						"                   and c.net_id = a.net_id) as manageName ")
				.append(" from ")
				.append(getAccbookTable())
				.append(" a ");
//				.append(" left join bt_currency b on a.cur_code = b.id");
				if(!isPool) {
					sql.append(" left join fc_bus_race c on a.bus_race_id = c.id");
				}else{
					sql.append(" left join fc_bus_race_pool c on a.bus_race_id = c.id");
				}
//				sql.append(" left join fc_voucher_type d on a.voucher_type = d.id");
		sql.append(" where a.id = :id ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql.toString(), params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 获取账务主表明细。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAccbookVoByVoucherNo(String voucherNo,boolean isPool) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id as id, a.tenant_id as tenantId, a.net_id as net_id,a.order_no as order_no,a.voucher_no as voucher_no,")
				.append(" a.enrol_time as enrol_time,a.tally_date as tally_date,a.interest_start_date as interest_start_date,")
//				.append(" a.voucher_type as voucher_type,d.voucher_name as voucher_name,a.emergency_sign as emergency_sign,")
				.append(" a.voucher_type as voucher_type,a.emergency_sign as emergency_sign,")
				.append(" a.operator_code as operator_code,a.checker_code as checker_code,a.affix_amount as affix_amount,")
				.append(" a.redblue_sign as redblue_sign,a.voucher_state as voucher_state,a.rush_sign as rush_sign,")
				.append(" a.generate_mode as generate_mode,a.bus_ori_type as bus_ori_type,a.rmk as rmk,")
				.append(" a.cur_code as cur_code,b.cur_name as cur_name,a.bus_race_id as bus_race_id,c.race_name as race_name,a.attachment_Id ")
				.append(" from ")
				.append(getAccbookTable())
				.append(" a ")
				.append(" left join bt_currency b on a.cur_code = b.id");
		if(!isPool) {
			sql.append(" left join fc_bus_race c on a.bus_race_id = c.id");
		}else{
			sql.append(" left join fc_bus_race_pool c on a.bus_race_id = c.id");
		}
//		sql.append(" left join fc_voucher_type d on a.voucher_type = d.id");
		sql.append(" where a.voucher_no = :voucherNo ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherNo", voucherNo);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql.toString(), params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 根据账务流水ID获取流水详细明细。<br>
	 * <b>日期：</b>2017年6月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public List<FcAccbookDtlVO> getPageDtlsByBookId(String bookId,boolean isPool) {
		/*sql.append("select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content,")
				.append(" a.account as account,b.acc_name as acc_name,c.subject_name as subject_name,c.id as subjectId,a.debit_money as debit_money,")
				.append(" a.cash_flux cashFlux, ")
				.append(" item.ITEM_NAME cashFluxName,")
				.append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,a.orig_voucher_id as orig_voucher_id")
				.append(" from ")
				.append(getAccbookDtlTable())
				.append(" a left join fc_acc b on a.account = b.blnctr_acc ");
		sql.append(" left join FC_CASH_ITEM item on a.cash_flux = item.id");
		if(true == isPool){
			sql.append(" left join fc_subject_pool c on b.subject_id = c.id ");
		}else {
			sql.append(" left join fc_subject c on b.subject_id = c.id ");
		}*/
		String sql = "select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content," +
				" a.account as account,a.acc_name as acc_name,a.subject_name as subject_name,a.subject_id as subjectId,a.debit_money as debit_money," +
				" a.cash_flux cashFlux, " +
				" item.ITEM_NAME cashFluxName," +
				" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,a.orig_voucher_id as orig_voucher_id" +
				" from " +
				getAccbookDtlTable() + " a " +
				" left join FC_CASH_ITEM item on a.cash_flux = item.id" +
				// TODO 流水明细表 增加 acc_name、subject_id、subject_name；填充数据时，根据账号查询 fc_acc 表，拿到 subject_id
				" where a.book_id = :bookId " +
				" order by a.line_no ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookId);
		return listBySQLAliasToBean(sql, params, FcAccbookDtlVO.class);
	}
    /**
     * 功能介绍 : 根据账务流水ID获取流水详细明细。<br>
     * <b>日期：</b>2017年6月20日<br>
     * @version 1.0
     * @author liyh
     */
    public List<FcAccbookDtlVO> getPageDtlsByBookIds(List<String> bookIds, boolean isPool) {
        /*sql.append("select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content,")
                .append(" a.account as account,b.acc_name as acc_name,c.subject_name as subject_name,c.id as subjectId,a.debit_money as debit_money,")
                .append(" a.cash_flux cashFlux, ")
                .append(" item.ITEM_NAME cashFluxName,")
                .append(" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,a.orig_voucher_id as orig_voucher_id")
                .append(" from ")
                .append(getAccbookDtlTable())
                .append(" a left join fc_acc b on a.account = b.blnctr_acc ");
        sql.append(" left join FC_CASH_ITEM item on a.cash_flux = item.id");
        if(true == isPool){
            sql.append(" left join fc_subject_pool c on b.subject_id = c.id ");
        }else {
            sql.append(" left join fc_subject c on b.subject_id = c.id ");
        }
        sql.append(" where a.book_id in (:bookId) ");
        sql.append(" order by a.book_id,a.line_no ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bookId", bookIds);*/
		//TODO 填充数据；流水明细表增加字段 acc_name、subject_id、subject_name
		String sql = "select a.id as id,a.book_id as book_id,a.line_no as line_no,a.summary_content as summary_content," +
				" a.account as account,a.acc_name as acc_name,a.subject_name as subjectName,a.subject_id as subjectId,a.debit_money as debit_money," +
				" a.cash_flux cashFlux, " +
				" item.ITEM_NAME cashFluxName," +
				" a.credit_money as credit_money,a.balance as balance,a.orig_voucher_no as orig_voucher_no,a.orig_voucher_id as orig_voucher_id" +
				" from " +
				getAccbookDtlTable() +
				" a " +
				" left join FC_CASH_ITEM item on a.cash_flux = item.id" +
				" where a.book_id in (:bookId) " +
				" order by a.book_id,a.line_no ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bookId", bookIds);
        return listBySQLAliasToBean(sql, params, FcAccbookDtlVO.class);
    }
	/**
	 * 功能介绍 : 获取同一全局ID的所有账务信息。<br>
	 * <b>日期：</b>2017年6月23日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@SuppressWarnings("unchecked")
	public List<T> getBooksByBusGlobalId(String busGlobalId) {
		String hql = " select t from " + getAccbookEntity() + " t where t.busGlobalId = :busGlobalId order by t.enrolTime,t.origVoucherNo ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("busGlobalId", busGlobalId);
		return createQuery(hql, params).list();
	}
	/**
	 * <b>功能介绍：</b>获取同一流水号的所有账务信息<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> getBooksByOrderNo(String orderNo){
		String hql = " select t from " + getAccbookEntity() + " t where t.orderNo = :orderNo order by t.enrolTime ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		return createQuery(hql, params).list();
	}
	/**
	 * <b>功能介绍：</b>获取同一流水号的所有账务信息<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FcAccbookVO getAccbookByOrderNo(String orderNo){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," );
		sql.append(" a.enrol_time as enrolTime,a.tally_date as tallyDate,a.interest_start_date as interestStartDate," );
		sql.append(" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,a.emergency_sign as emergencySign," );
		sql.append(" a.operator_code as operatorCode,a.checker_code as checkerCode,a.check_time as checkTime,a.affix_amount as affixAmount," );
		sql.append(" a.redblue_sign as redblueSign,a.voucher_state as voucherState,a.charged_sign as chargedSign,a.rush_sign as rushSign," );
		sql.append(" a.generate_mode as generateMode,a.bus_ori_type as busOriType,a.rmk as rmk,a.postscript as postscript," );
		sql.append(" a.cur_code as curCode,a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName,a.attachment_Id " );
		sql.append(" from " );
		sql.append(getAccbookTable());
		sql.append(" a " );
		sql.append(" left join fc_bus_race c on a.bus_race_id = c.id");
		sql.append(" where a.order_No = :orderNo ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(orderNo)){
			params.put("orderNo", orderNo.trim());
		}

		List<FcAccbookVO> list = listBySQLAliasToBean(sql.toString(), params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据凭证号获取凭证信息
	 * @param voucherNo
	 * @return
	 */
	public FcAccbookVO getAccbookByVoucherNo(String voucherNo){
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.enrol_time as enrolTime,a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,a.emergency_sign as emergencySign," +
				" a.operator_code as operatorCode,a.checker_code as checkerCode,a.check_time as checkTime,a.affix_amount as affixAmount," +
				" a.redblue_sign as redblueSign,a.voucher_state as voucherState,a.charged_sign as chargedSign,a.rush_sign as rushSign," +
				" a.generate_mode as generateMode,a.bus_ori_type as busOriType,a.rmk as rmk,a.postscript as postscript," +
				" a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName" +
				" from fc_accbook_blned  a " +
//				" left join bt_currency b on a.cur_code = b.id" +
				" left join fc_bus_race c on a.bus_race_id = c.id" +
//				" left join fc_voucher_type d on a.voucher_type = d.id" +
				" where a.voucher_no = :voucherNo ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherNo", voucherNo.trim());
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * <b>功能介绍：</b>获取同一流水号的所有账务信息-资金池<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FcAccbookVO getPoolAccbookByOrderNo(String orderNo){
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.enrol_time as enrolTime,a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,a.emergency_sign as emergencySign," +
				" a.operator_code as operatorCode,a.checker_code as checkerCode,a.check_time as checkTime,a.affix_amount as affixAmount," +
				" a.redblue_sign as redblueSign,a.voucher_state as voucherState,a.charged_sign as chargedSign,a.rush_sign as rushSign," +
				" a.generate_mode as generateMode,a.bus_ori_type as busOriType,a.rmk as rmk,a.postscript as postscript," +
				" a.cur_code as curCode,a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a " +
				" left join fc_bus_race_pool c on a.bus_race_id = c.id" +
				" where a.order_No = :orderNo ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo.trim());
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * <b>功能介绍：</b>获取同一全局ID的所有账务信息<br>
	 * <b>日期：</b>2017年9月7日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param busGlobalId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FcAccbook getAccbookByBusGlobalId(String busGlobalId){
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.enrol_time as enrolTime,a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
//				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,d.voucher_name as voucherName,a.emergency_sign as emergencySign," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,a.emergency_sign as emergencySign," +
				" a.operator_code as operatorCode,a.checker_code as checkerCode,a.check_time as checkTime,a.affix_amount as affixAmount," +
				" a.redblue_sign as redblueSign,a.voucher_state as voucherState,a.charged_sign as chargedSign,a.rush_sign as rushSign," +
				" a.generate_mode as generateMode,a.bus_ori_type as busOriType,a.rmk as rmk,a.postscript as postscript,a.attachment_Id," +
//				" b.cur_name as curCode,a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName," +
				" a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName" +
				" from " +
				getAccbookTable() +
				" a " +
//				" left join bt_currency b on a.cur_code = b.id" +
				" left join fc_bus_race c on a.bus_race_id = c.id" +
//				" left join fc_voucher_type d on a.voucher_type = d.id" +
				" where a.bus_global_id = :busGlobalId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("busGlobalId", busGlobalId);
		List<FcAccbook> list = listBySQLAliasToBean(sql, params, FcAccbook.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * <b>功能介绍：</b>按业务来源和状态查询账务<br>
	 * <b>日期：</b>2018年3月13日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param params
	 * @return
	 */
	// TODO 没有被使用 暂注释掉
	/*public List<FcAccbookVO> findTallyAccbookByBusOriTypeAndState(Map<String, Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct a.id as id,a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo,")
				.append(" a.tally_date as tallyDate,a.interest_start_date as interestStartDate,")
				.append(" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,")
				.append(" a.voucher_state as voucherState,a.rush_sign as rushSign,")
				.append(" a.bus_ori_type as busOriType,a.bus_global_id as busGlobalId,a.attachment_Id ")
				.append(" from ")
				.append(getAccbookTable())
				.append(" a , ").append(getAccbookDtlTable()).append(" b,fc_acc c where a.id = b.book_id and b.account = c.blnctr_acc ");
		for (Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			if("netCode".equals(key)){
				sql.append(" and c.net_id = :netCode ");
			}
			if("curCode".equals(key)){
				sql.append(" and c.cur_id = :curCode ");
			}
			if("busOriType".equals(key)){
				sql.append(" and a.bus_ori_type = :busOriType ");
			}
			if("voucherState".equals(key)){
				sql.append(" and a.voucher_state in :voucherState ");
			}
		}
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookVO.class);
	}*/
	/**
	 * <br>
	 * <b>功能：</b>未复核的账务<br>
	 * <b>作者：</b>yangl<br>
	 * <b>日期：</b> 2017-09-28 <br>
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean existUnCheckAccbook(String account, String busGlobalId) {
		Map<String, Object> params = new HashMap<String, Object>();
	    StringBuffer hql = new StringBuffer();
	    hql.append("select 1 from "+getAccbookDtlTable()+" dtl left join fc_accbook accbook on accbook.id=dtl.book_id");
//        hql.append(" left join fc_acc fc on fc.blnctr_acc=dtl.account where 1=1 ");
        hql.append(" where 1=1 ");
        hql.append(" and accbook.voucher_state in ('C','F','T') and dtl.account = :account ");
        if (!EmptyUtils.isEmpty(busGlobalId)) {
        	hql.append(" and accbook.bus_Global_Id <> :busGlobalId ");
        	params.put("busGlobalId", busGlobalId);
		}
        params.put("account", account);
        List list = createSQLQuery(hql.toString(), params).list();
		return EmptyUtils.isEmptyList(list);
	}
	/**
	 * 功能介绍 : 获取账务主表明细。<br>
	 * <b>日期：</b>2017年9月30日<br>
	 * @version 1.0
	 * @author yangl
	 */
	public FcAccbook getAccbookById(String id) {
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.enrol_time as enrolTime,a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
//				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,d.voucher_name as voucherName,a.emergency_sign as emergencySign," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo,a.emergency_sign as emergencySign," +
				" a.operator_code as operatorCode,a.checker_code as checkerCode,a.check_time as checkTime,a.affix_amount as affixAmount," +
				" a.redblue_sign as redblueSign,a.voucher_state as voucherState,a.charged_sign as chargedSign,a.rush_sign as rushSign," +
				" a.generate_mode as generateMode,a.bus_ori_type as busOriType,a.rmk as rmk,a.postscript as postscript," +
//				" b.id as curCode,a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName,a.attachment_Id " +
				" a.cur_code as curCode,a.bus_race_id as busRaceId, a.bus_global_id as busGlobalId, c.race_name as busRaceName,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a " +
//				" left join bt_currency b on a.cur_code = b.id" +
				" left join fc_bus_race c on a.bus_race_id = c.id" +
//				" left join fc_voucher_type d on a.voucher_type = d.id" +
				" where a.id = :id ";
		// bt_currency 在基础模块，fc_voucher_type 在结算模块
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<FcAccbook> list = listBySQLAliasToBean(sql, params, FcAccbook.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 获取账务主表明细。<br>
	 * <b>日期：</b>2017年11月09日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAccbookVOByBillId(String billId) {
		String sql = "select a.id as id,a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo," +
				" a.voucher_state as voucherState,a.rush_sign as rushSign," +
				" a.bus_ori_type as busOriType,a.bus_global_id as busGlobalId,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a left join " + getAccbookDtlTable() + " b on a.id = b.book_id " +
				" where b.orig_voucher_id = :billId and a.voucher_state <> '" + FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get() + "' " +
				" and a.bus_ori_type <> '" + FinaBookConstant.BusOriType.RUSH.get() + "'";
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("busOriType", busOriType);
		params.put("billId", billId);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 获取账务主表明细(包含作废凭证)。<br>
	 * <b>日期：</b>2017年11月09日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAllAccbookVOByBillId(String billId) {
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo," +
				" a.voucher_state as voucherState,a.rush_sign as rushSign," +
				" a.bus_ori_type as busOriType,a.bus_global_id as busGlobalId,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a left join " + getAccbookDtlTable() + " b on a.id = b.book_id " +
				" where b.orig_voucher_id = :billId" +
				" and a.bus_ori_type <> '" + FinaBookConstant.BusOriType.RUSH.get() + "'" +
				" order by a.enrol_time desc";
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("busOriType", busOriType);
		params.put("billId", billId);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 通过凭证号获取账务信息。<br>
	 * <b>日期：</b>2018年1月30日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAccbookVOByVoucherNo(String voucherNo) {
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo," +
				" a.voucher_state as voucherState,a.rush_sign as rushSign," +
				" a.bus_ori_type as busOriType,a.bus_global_id as busGlobalId,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a left join " + getAccbookDtlTable() + " b on a.id = b.book_id " +
				" where a.voucher_no = :voucherNo  ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("voucherNo", voucherNo);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	/**
	 * 功能介绍 : 获取账务主表明细。<br>
	 * <b>日期：</b>2017年11月09日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcAccbookVO getAccbookVOByOrigVoucherNo(String oriBookId) {
		String sql = "select a.id as id, a.tenant_id as tenantId, a.net_id as netId,a.order_no as orderNo,a.voucher_no as voucherNo," +
				" a.tally_date as tallyDate,a.interest_start_date as interestStartDate," +
				" a.voucher_type as voucherType,a.orig_voucher_no as origVoucherNo," +
				" a.voucher_state as voucherState,a.rush_sign as rushSign," +
				" a.bus_ori_type as busOriType,a.bus_global_id as busGlobalId,a.affix_amount as affixAmount,a.rmk,a.attachment_Id " +
				" from " +
				getAccbookTable() +
				" a " +
				" where a.orig_voucher_no = :oriBookId  ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oriBookId", oriBookId);
		List<FcAccbookVO> list = listBySQLAliasToBean(sql, params, FcAccbookVO.class);
		if (null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
