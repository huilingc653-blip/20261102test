package com.bytter.check.dao;
import com.bytter.biz.finacial.api.constant.CheckConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcCheckInit;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcBankCheckVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.check.vo.FcCompareErpSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 14:43:17
* @version 1.0
*
*/
@Repository
public class FcCompareErpDao extends BaseDao<FcCompareAcc,String>{
    /**
     * 功能介绍:查询中心对账数据(未达、已对账、已勾兑、已对账等)<br>
     * -------------------------------------------------------------------------------------------------
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryErpCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO, Date transDate, String[] compareSigns) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.VOUCHER_NO as voucherNo,a.COMP_ACC as compAcc,a.TALLY_DATE as tallyDate, a.CHECK_Type as checkType,a.id,");
        sql.append(" (a.DEBIT_MONEY + a.CREDIT_MONEY ) as MONEY,a.BILL_NO as billNo,a.REDBLUE_SIGN as redblueSign,a.IMPORT_TYPE as importType,");
        sql.append("  a.COMPARE_SIGN as compareSign,a.line_no as lineNo, a.BALANCE, a.SUMMARY_CONTENT as summaryContent,a.bank_id as bankId,");
        sql.append(" case when a.DEBIT_MONEY <> 0 then  '1' else '0' end as cdSign,a.ORDER_NO as orderNo,a.cur_id as curCode ");//a.bank_id as ORDER_LINE_NO
        sql.append(" FROM FC_COMPARE_Acc a where 1 = 1");
//		sql.append(" and a.redblue_sign = 'B' ");
        sql.append(" and exists (select 1  from fc_check_init i  where i.bank_acc = a.COMP_ACC and i.cur_id = a.cur_id ");
        sql.append(" and ( i.check_date < a.tally_date  or a.IMPORT_TYPE = '4' )  ");
        sql.append(" ) ");
		String sign = "or";
		StringBuilder compareSignStr = new StringBuilder();
		int i = 0;
		if( Arrays.asList(compareSigns).contains("0")){
			compareSignStr.append("  (a.compare_sign = :compareSign or a.compare_sign = '' or a.compare_sign is null or a.compare_sign = '")
                    .append(FinacialConstant.COMPARE_SIGN_STAY_UP).append("') ");
			i = 1;
		}
		for (String compareSign : compareSigns) {
            if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达账记录
            	if(i>0) {
					compareSignStr.append(sign);
				}
				compareSignStr.append(" a.compare_sign = '").append(FinacialConstant.COMPARE_SIGN_STAY).append("' ");
				i = 1;
            }
            if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//已经勾兑记录
				if(i>0) {
					compareSignStr.append(sign);
				}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_HAND+"' ");
				i = 1;
            }
            if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已经对账记录
				if(i>0) {
					compareSignStr.append(sign);
				}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_YES+"' ");
				i = 1;
            }
            if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)){//未对账
				if(i>0) {
					compareSignStr.append(sign);
				}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_NO+"'  or a.compare_sign = '' or a.compare_sign is null ");
				i = 1;
            }
        }
		if (StringUtils.isNotEmpty(compareSignStr)) {
			sql.append("  and ( " + compareSignStr + " )");
		}
		sql.append(" and a.comp_acc = :account and a.tally_date <=:transDate ");
		sql.append(" and a.cur_id = :curId  ");
		if (null != fcCompareErpSearchVO.getStartMoney()) {
			sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY ) >= " + fcCompareErpSearchVO.getStartMoney().toPlainString());
		}
		if (null != fcCompareErpSearchVO.getEndMoney()) {
			sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY ) <= " + fcCompareErpSearchVO.getEndMoney().toPlainString());
		}
		if (StringUtils.isNotEmpty(fcCompareErpSearchVO.getOrderKey())) {
			if ("tallyDate".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by a.tally_date ");
			}
			if ("cdSign".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by cdSign ");
			}
			if ("money".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by MONEY ");
			}
			if ("summaryContent".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by a.SUMMARY_CONTENT ");
			}
			if ("voucherNo".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by a.VOUCHER_NO ");
			}
			sql.append(fcCompareErpSearchVO.getOrderType());
		} else {
			sql.append(" order by tally_date,(a.DEBIT_MONEY + a.CREDIT_MONEY) ");
		}
		params.put("account", fcCompareErpSearchVO.getBankAcc());
		params.put("curId", fcCompareErpSearchVO.getCurId());
		params.put("compareSign",FinacialConstant.COMPARE_SIGN_NO);
		params.put("transDate", transDate);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("tallyDate", StandardBasicTypes.DATE);
		sqlQuery.addScalar("orderNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("redblueSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("lineNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("summaryContent", StandardBasicTypes.STRING);
		sqlQuery.addScalar("cdSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("money", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("voucherNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compareSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("curCode", StandardBasicTypes.STRING);
		sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("id", StandardBasicTypes.STRING);
		sqlQuery.addScalar("checkType", StandardBasicTypes.STRING);
		sqlQuery.addScalar("importType", StandardBasicTypes.STRING);
		sqlQuery.addScalar("BALANCE", StandardBasicTypes.STRING);
		sqlQuery.addScalar("bankId", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	
	/**
	 * <b>功能介绍：</b>获取银行账号
	 * ---------------------------------------------------------------------<br>
	 */
  //TODO 整体改造
	@SuppressWarnings("unchecked")
	public List<FcCompareAccVO> getBankAccByOpType(String bankAcc,String netCode,String natureCode,String userId){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select acc.id,acc.bank_name as bankName, acc.bank_acc as bankAcc,c.cur_id as curId,");
		sql.append(" erp.erp_code as erpCode, acc.corp_id as corpCode, acc.nature_id as natureId,cur.cur_name as curName ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" inner join erp_basic_data erp on acc.bank_acc = erp.bytter_code ");
		sql.append(" left join bt_bank_acc_cur c on acc.id = c.bank_acc_id ");
		sql.append(" left join bt_currency cur on c.cur_id = cur.id");
		sql.append(" and erp.DATA_TYPE_CODE = 'BT_BANK_ACC' and erp.type_code = 'rsm' ");
		sql.append(" where acc.valid_sign = '1'  and acc.IS_ONLINE = '1' ");
		if(StringUtils.isNotEmpty(bankAcc)){
			sql.append("and acc.bank_Acc = :bankAcc ");
			params.put("bankAcc", bankAcc);
		}
		if(StringUtils.isNotEmpty(netCode)){
			sql.append("and acc.corp_id = :netCode ");
			params.put("netCode", netCode);
		}
		if(StringUtils.isNotEmpty(natureCode)){
			sql.append("and acc.nature_Id = :natureCode  ");
			params.put("natureCode", natureCode);
		}
		sql.append(" and exists (select 1 from sys_account_grant gt  where gt.ACCOUNT_ID = acc.id ");
		//sql.append(" and gt.user_id = :userId ");
		sql.append(" and (gt.can_query_balance = '1' or gt.can_query_detail = '1' or gt.can_transfer_accounts = '1')) ");
		sql.append("  union all ");
		sql.append(" select app.id,bb.bank_name as bankName,app.bank_acc as bankAcc,c.cur_id as curId, erp.erp_code as erpCode,");
		sql.append(" app.corp_id as corpCode, app.nature_id as natureId,cur.cur_name as curName ");
		sql.append(" from bt_bank_acc app ");
        sql.append(" left join bt_bank_acc_cur c on app.id = c.bank_acc_id ");
        sql.append(" left join bt_currency cur on c.cur_id = cur.id");
		sql.append(" inner join bt_bank bb on bb.SYS_BANK_CODE = app.bank_code ");
		sql.append(" inner join erp_basic_data erp on app.bank_acc = erp.bytter_code ");
		sql.append(" and erp.DATA_TYPE_CODE = 'BT_BANK_ACC' and erp.type_code = 'rsm' ");
		sql.append(" where app.status <> -2  and app.IS_ONLINE = '0' ");
		sql.append(" and exists (select 1  from sys_account_grant gra where gra.account_id = app.id ");
		//sql.append(" and gra.user_id = :userId ");
		sql.append(" and (gra.can_query_detail = '1' or gra.can_query_balance = '1' or  gra.can_transfer_accounts = '1')) ");
		
		if(StringUtils.isNotEmpty(bankAcc)){
			sql.append("and app.bank_Acc = :bankAcc ");
			params.put("bankAcc", bankAcc);
		}
		if(StringUtils.isNotEmpty(netCode)){
			sql.append("and app.corp_id = :netCode ");
			params.put("netCode", netCode);
		}
		if(StringUtils.isNotEmpty(natureCode)){
			sql.append("and app.nature_Id = :natureCode  ");
			params.put("natureCode", natureCode);
		}
		return listBySQLAliasToBean(sql.toString(),params, FcCompareAccVO.class);
		
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>通过账号获取账户余额初始化信息<br>
	 * @param bankAcc--------------------------------------------------------
	 * @return
	 */
	public FcCheckInit getCheckInitByAccount(String bankAcc, String curId){
		String sql = " select * from Fc_Check_Init where bank_Acc = :bankAcc and check_type = 2 and cur_id = :curId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		return  getBySQLAliasToBean(sql.toString(),params,FcCheckInit.class);
	}
	
	/**
	 * <br>
	 * <b>功能：</b>获取erp对账单时间段发生额<br>
	 * @return----------------------------------------------------------
	 */
	public BigDecimal getTransErpBalance(String bankAcc, Date checkDate, Date tallyDate,String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select bt_nvl_num( sum(debit_money),0) - bt_nvl_num(sum(credit_money),0) balance ");
		sql.append(" from fc_compare_acc where comp_acc = :bankAcc and  ( compare_sign <> '6' or compare_sign is null) ");
		sql.append(" and tally_Date > bt_to_date( :checkDate ,'yyyy-MM-dd') and tally_Date <= bt_to_date(:tallyDate ,'yyyy-MM-dd') ");
		sql.append(" and cur_id = :curId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("checkDate", DateUtils.formatDate(checkDate,"yyyy-MM-dd"));
		params.put("tallyDate", DateUtils.formatDate(tallyDate,"yyyy-MM-dd"));
		params.put("importType", FinacialConstant.COMPARE_SIGN_STAY_UP);//未达不参与余额计算
		params.put("curId", curId);
		List<FcCompareAccVO> list = listBySQLAliasToBean(sql.toString(), params,FcCompareAccVO.class);
		if (null == list || list.isEmpty()) {
			return BigDecimal.ZERO;
		} 
		return list.get(0).getBalance();
	}
	/**
	 * <br>
	 * <b>功能：</b>获取银行对账单时间段发生额<br>
	 * @return------------------------------------------------------------
	 */
	public BigDecimal getTransBillBalance(String bankAcc, Date checkDate, Date tallyDate) {
		StringBuilder sql = new StringBuilder();
		//sql.append(" select (bb.balance + rb.rbalance) balance from (");
		sql.append(" select bt_nvl_num(sum(credit_money), 0) - bt_nvl_num(sum(debit_money), 0) balance from fc_bank_bill ");
		sql.append(" where bank_acc = :bankAcc and import_type <> :importType ");
		sql.append(" and tally_Date > to_date(:checkDate,'yyyy-MM-dd') ");
		sql.append(" and tally_Date <= to_date(:tallyDate,'yyyy-MM-dd')  ");
		//sql.append(" and (compare_sign = '0' or compare_sign = '1' or compare_sign = '' or compare_sign is null) ");
		/*sql.append(" ( select bt_nvl(sum(debit_money), 0) - bt_nvl(sum(credit_money), 0) rbalance from fc_bank_bill ");
		sql.append("  where bank_acc = :bankAcc and import_type <> :importType and tally_Date > to_date(:checkDate,'yyyy-MM-dd') ");
		sql.append(" and tally_Date <= to_date(:tallyDate,'yyyy-MM-dd') and redblue_sign = 'R' ");
		sql.append("  and (compare_sign = '0' or compare_sign = '1' or compare_sign = '' or  compare_sign is null) )rb ");*/
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("checkDate", DateUtils.formatDate(checkDate));
		params.put("tallyDate", DateUtils.formatDate(tallyDate));
		params.put("importType",FinacialConstant.COMPARE_SIGN_STAY_UP);//未达不参与余额计算
		List<FcBankBillVo> list = listBySQLAliasToBean(sql.toString(), params, FcBankBillVo.class);
		if (null == list || list.isEmpty()) {
			return BigDecimal.ZERO;
		}
		return list.get(0).getBalance();
	}
	/**
	 * <br>
	 * <b>功能：</b>更新上期未达的数据为未对账<br>
	 ------------------------------------------------------------------------
	 */
	public void updateErpBillInitStatus(Date transDate, Date checkDate, String account, String curId) {
		String deleteSql = "Update fc_compare_acc a set compare_sign = '0' where tally_date <= to_date(:transDate,'yyyy-MM-dd') "
				+ " AND compare_sign in (:compareSign) And comp_acc = :account and cur_id = :curId "
				+ " and exists (select 1 from fc_check_init b "
				+ " where a.comp_acc = b.bank_acc and a.cur_id = b.cur_id and a.tally_date > b.check_date and b.check_type = '2' ) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("transDate",DateUtils.formatDate(transDate) );
		params.put("checkDate", DateUtils.formatDate(checkDate));
		params.put("curId", curId);
        /*params.put("compareSign", new String[]{ CompareSign.COMPARE_SIGN_STAY.get(),
        		CompareSign.COMPARE_STAY_HAND.get(), CompareSign.COMPARE_SIGN_STAY_UP.get()});*/
		params.put("compareSign", new String[]{ FinacialConstant.COMPARE_SIGN_STAY_UP});
		Query query = getSession().createSQLQuery(deleteSql);
		if (params != null)
			query.setProperties(params);
		query.executeUpdate();
	}
	/**
	 * <br>
	 * <b>功能：</b>查询ERP对账数据<br>
	 * @return------------------------------------------------------------------------------
	 */
	@SuppressWarnings("unchecked")
	public List<FcCompareAccVO> getCheckFcCompareErpDatas(FcCompareAccVO vo, String userCode){
		Map<String,Object> param = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct erp.bank_id bankId,erp.voucher_no voucherNo,erp.comp_acc compAcc,erp.tally_date tallyDate,");
		sql.append(" bt_nvl_num(erp.debit_money,0) as debitMoney,bt_nvl_num(erp.credit_money,0) as creditMoney, bt_nvl_num(erp.balance,0) as balance,erp.bill_no billNo,");
		sql.append(" erp.redblue_sign redblueSign, erp.compare_sign compareSign, erp.summary_content summaryContent,");
		sql.append(" erp.line_no lineNo,erp.import_type importType, erp.payee_acc as payeeAcc, ");
		sql.append(" CASE WHEN adj.account IS NULL THEN  '0' ELSE  '1' END isAdj ,erp.id,erp.cur_id as curId ");
		sql.append(" FROM fc_compare_acc erp ");
		sql.append(" LEFT JOIN (select * from fc_bank_balance_adjust where status <> -2) adj");
		sql.append(" ON adj.bank_acc = erp.comp_acc");
		sql.append(" INNER JOIN fc_check_init fct ON erp.comp_acc = fct.bank_acc ");
		sql.append(" WHERE 1 = 1 ");
		sql.append(" AND fct.check_date < erp.tally_date ");
		//sql.append(" AND erp.redblue_sign = 'B'");
		sql.append(" AND (erp.compare_sign = '0' OR erp.compare_sign = '' OR erp.compare_sign IS NULL) ");
		if(EmptyUtils.isNotEmpty(vo.getCompAcc())){
			sql.append(" and erp.comp_acc = :compAcc");
			param.put("compAcc",vo.getCompAcc());
		}
		if(vo.getTallyDate() != null){
			sql.append(" and erp.tally_date <= :tallyDate ");
			param.put("tallyDate",vo.getTallyDate());
		}
		if(EmptyUtils.isNotEmpty(vo.getCurId())){
			sql.append(" and erp.cur_id = :curId ");
			param.put("curId", vo.getCurId());
		}
		sql.append(" ORDER BY erp.comp_acc, erp.tally_date, erp.voucher_no");
		return listBySQLAliasToBean(sql.toString(),param,FcCompareAccVO.class);
	}
	/**
	 * <br>
	 * <b>功能：</b>更新互为冲销的凭证<br>
	 *     ------------------------------------------------------------------
	 */
	public void updateComparesignRushVoucher(Map<String, Object> rushMap, String compareSign) {
		StringBuffer updateSql = new StringBuffer("update fc_compare_acc erp set erp.compare_sign= :compareSign, erp.bill_no = :billNo ");
		updateSql.append(" where erp.bank_id = :bankId and erp.import_type = :importType ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankId" , (String) rushMap.get("bankId"));
		params.put("importType", (String) rushMap.get("importType"));
		params.put("billNo", (String) rushMap.get("billNo"));
		params.put("compareSign", compareSign);
		Query query = getSession().createSQLQuery(updateSql.toString());
		if (params != null)
			query.setProperties(params);
		query.executeUpdate();
	}
	
	 
	 /**
	  * 功能介绍:获取银行未达账金额合计(长江财险)<br>
	-----------------------------------------------------------------------------
	  */
	 @SuppressWarnings("unchecked")
	 public Map<String, Object> queryStayOverErpBankSumCc(Date transDate, String account, String bankAcc, String overType, String curId) {
		StringBuilder sqlSelect = new StringBuilder();
		sqlSelect.append("SELECT BT_NVL_NUM(count(a.debit_money),0) as moneyCount, BT_NVL_NUM(sum(a.debit_money),0) as debitMoney, ");
		sqlSelect.append(" BT_NVL_NUM(sum(a.credit_money),0) as creditMoney");
		sqlSelect.append(" FROM FC_COMPARE_ACC a where ");
		sqlSelect.append(" exists (select 1 from fc_check_init i where i.bank_acc = a.comp_acc and (i.check_date < a.tally_date or a.import_type = '4') and check_Type = '2' ) ");
		sqlSelect.append("  and (a.compare_sign = :compareSign ) ");
		if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {//银行已付、中心未付
			sqlSelect.append(" and a.debit_money <> 0 and a.credit_money = 0");
		} else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {//银行已收、中心未收
			sqlSelect.append(" and a.debit_money = 0 and a.credit_money <> 0");
		}
		sqlSelect.append(" and a.comp_acc= :compAcc and a.tally_date <= :transDate ");
		sqlSelect.append(" and a.cur_id = :curId ");
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("bankAcc", bankAcc);
		params.put("compAcc", bankAcc);
		params.put("curId", curId);
		params.put("transDate", transDate);
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_STAY);
		
		SQLQuery sqlQuery = createSQLQuery(sqlSelect.toString(),params);
		sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
		sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		List<Map<String, Object>> list = sqlQuery.list();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			resultMap = list.get(0);
		}
		
		return resultMap;
	  }
	/**
	 * <br>
	 * <b>功能：</b>获取ERP银行未达账金额合计<br>
-------------------------------------------------------------------------------------------------------
	 */
	public Map<String, Object> queryStayOverErpBankSum(Date transDate, String bankAcc, String overType, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT BT_NVL_NUM(count(a.debit_money),0) as moneyCount, BT_NVL_NUM(sum(a.debit_money),0) as debitMoney,");
		sql.append("BT_NVL_NUM(sum(a.credit_money),0) as creditMoney  ");
		sql.append(" FROM FC_BANK_BILL a where ");
		sql.append(" exists (select 1 from fc_check_init i where i.bank_acc = a.bank_acc and (i.check_date < a.tally_date or a.import_type = '4') and check_Type = '2')");
		sql.append(" and (a.compare_sign = :compareSign ) ");
		if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {//银行已付、中心未付
			sql.append(" and a.debit_money <> 0 and a.credit_money = 0");
		} else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {//银行已收、中心未收
			sql.append(" and a.debit_money = 0 and a.credit_money <> 0");
		}
		sql.append(" and a.bank_acc= :bankAcc and a.tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ");
		sql.append(" and a.cur_id = :curId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transDate", DateUtils.formatDate(transDate,"yyyy-MM-dd"));
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_STAY);
		
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("moneyCount", StandardBasicTypes.LONG);
		sqlQuery.addScalar("debitMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("creditMoney", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			resultMap = list.get(0);
		}
		return resultMap;
	}
	
	 /**
	  * <b>功能介绍:更新状态为已对账</b><br>
	-------------------------------------------------------------------------------
	  */
	 public void updateComparesignOverCheck(List<String> compareAccPKList,String compareSign){
		 Map<String, Object> params = new HashMap<String, Object>();
		 StringBuilder sql = new StringBuilder();
		 sql.append("update fc_compare_acc a set a.compare_sign= :compareSign ");
		 sql.append("where 1=1 and ");
		 sql.append(getSQLIn(compareAccPKList, 900, "a.id"));
		 params.put("compareSign", compareSign);
		 createSQLQuery(sql.toString(), params).executeUpdate();
	 }
	 
	 /**
	  * <b>功能介绍：</b>获取账户上次结转日期<br>
	 --------------------------------------------------------------------------------------------
	  */
	 public FcBankBalanceAdjust getLastAdjustOver(String account, String bankAcc, String curId) {
		 StringBuilder sql = new StringBuilder();
		 sql.append(" select * from Fc_Bank_Balance_Adjust a where a.end_Date = (select max(b.end_Date) from Fc_Bank_Balance_Adjust b where ");
		 sql.append("b.account = :account and b.bank_Acc = :bankAcc and b.cur_code = :curId and (b.status is null or b.status <> :status)) ");
		 sql.append("and a.account = :account and a.bank_Acc = :bankAcc and a.cur_code = :curId and (a.status is null or a.status <> :status) ");
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("account", account);
         params.put("bankAcc", bankAcc);
         params.put("curId", curId);
         params.put("status", CommonConstant.STATUE_DELETE);
	     return getBySQLAliasToBean(sql.toString(),params,FcBankBalanceAdjust.class);
	 }
	
	/**
	 * <br>
	 * <b>功能：</b>保存银行对账单数据到历史数据<br>
	--------------------------------------------------------------------------------------------
	 */
	public void saveAdjustErpHisDatas(Date transDate, String bankAcc, Long adjustId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" insert into FC_COMPARE_ERP_HIS ");
		sql.append(" (ADJUST_ID, voucher_no,tally_date,bank_acc,debit_money,credit_money,balance,");
		sql.append(" redblue_sign,compare_sign, bank_id,bill_no,adjust_date,import_date,import_type,");
		sql.append("  line_no, summary_content,data_source) ");
		sql.append("select"+adjustId);
		sql.append(",a.voucher_no,a.tally_date,a.comp_acc,a.debit_money, a.credit_money,a.balance,");
		sql.append(" a.redblue_sign,a.compare_sign,a.bank_id,a.bill_no,a.adjust_date,a.import_date,");
		sql.append("  a.import_type, a.line_no,a.summary_content, a.data_source ");
		sql.append(" from fc_compare_acc a ");
		sql.append("  where a.bank_acc = :bankAcc ");
		sql.append(" and a.compare_sign in (:compareSign) ");
		sql.append(" and tally_date <= to_date(:transDate,'yyyy-MM-dd' )");
		sql.append(" and a.check_type = '2' ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transDate", DateUtils.formatDate(transDate,"yyyy-MM-dd"));
		params.put("compareSign", new String[] { FinacialConstant.COMPARE_SIGN_STAY, FinacialConstant.COMPARE_SIGN_YES,
						FinacialConstant.COMPARE_SIGN_HAND });
		Query query = getSession().createSQLQuery(sql.toString());
		if (params != null) {
			query.setProperties(params);
		}
		query.executeUpdate();
		
	}
	
	/**
	 * <b>功能介绍：</b>获取中心可结转对账单<br>
-----------------------------------------------------------------------------------------------
	 */
	@SuppressWarnings("unchecked")
	public List<FcCompareAccVO> getctrDataOverData(Date transDate, String account,String checkType, String curId){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.id,a.compare_sign from fc_compare_acc a where a.comp_acc = :account ");
		sql.append(" and a.compare_sign in (:compareSign) and a.tally_date <= :transDate and check_type = :checkType");
		sql.append(" and a.cur_Id = :curId ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("checkType",checkType);
        params.put("account", account);
        params.put("transDate", transDate);
        params.put("curId", curId);
        params.put("compareSign",
                new String[] { FinacialConstant.COMPARE_SIGN_STAY, FinacialConstant.COMPARE_SIGN_YES,
                		FinacialConstant.COMPARE_SIGN_HAND });
        return listBySQLAliasToBean(sql.toString(), params, FcCompareAccVO.class);
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>查询上期留存未对账的ERP数据<br>
------------------------------------------------------------------------------------------
	 */
	@SuppressWarnings("unchecked")
	public List<FcCompareAccVO> queryUnOverErpStayDatas(String bankAcc, String overType, Date transDate, String curId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT erp.comp_acc as compAcc, erp.tally_date as tallyDate,erp.voucher_no as voucherNo, ");
		sql.append(" erp.bill_no as billNo, erp.summary_content as summaryContent,erp.debit_money as debitMoney,");
		sql.append(" erp.credit_money  as  creditMoney,erp.balance as balance, erp.compare_sign as compareSign,erp.cur_id as curId,");
		sql.append(" erp.redblue_sign as redblueSign,erp.bank_id as bankId, erp.import_type as importType,erp.line_no as lineNo ");
		sql.append(" FROM fc_compare_acc erp  WHERE  1=1 ");
		sql.append(" and (erp.compare_sign = :compareSign0)");
		//sql.append(" AND redblue_sign = 'B'");
		if (CheckConstant.CTR_PAY_BANK_NO_PAY.equals(overType)) {//中心已付、银行未付
			sql.append("  AND erp.debit_money <> 0 AND erp.credit_money = 0 " );
		} else if (CheckConstant.CTR_REC_BANK_NO_REC.equals(overType)) {//中心已收、银行未收
			sql.append("  AND erp.debit_money = 0 AND erp.credit_money <> 0 " );
		}
		sql.append(" and exists (select 1  from fc_check_init b  where erp.comp_acc = b.bank_acc  and (erp.tally_date > b.check_date or erp.import_type = '4')) ");
		sql.append(" AND erp.comp_acc in (:bankAccs)  and erp.tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ");
		sql.append(" AND erp.cur_id = :curId ");
		sql.append(" ORDER BY erp.tally_date");
		Map<String, Object> params = new HashMap<String, Object>();
		String[] bankAccs = bankAcc.split(",");
		params.put("bankAccs", bankAccs);
		params.put("curId", curId);
		params.put("transDate",DateUtils.formatDate(transDate));
		params.put("compareSign0", FinacialConstant.COMPARE_SIGN_STAY);
		params.put("compareSign1", FinacialConstant.COMPARE_SIGN_STAY_UP);
		return listBySQLAliasToBean(sql.toString(),params,FcCompareAccVO.class);
	
	}
	/**
	 * <br>
	 * <b>功能：</b>结转中心未达数据<br>
	------------------------------------------------------------------------
	 */
	public void updateOverStayAccInfos(String bankAcc, Date transDate, String curId) {
		String overSql = "update fc_compare_acc set compare_sign = :compareSignNo" +
				" where comp_acc = :bankAcc and compare_sign = :compareSignStay" +
				" and cur_id = :curId " +
				" and tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transDate", DateUtils.formatDate(transDate));
		params.put("compareSignNo", FinacialConstant.COMPARE_SIGN_NO);
		params.put("compareSignStay", FinacialConstant.COMPARE_SIGN_STAY);
		createSQLQuery(overSql.toString(), params).executeUpdate();
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>结转ERP对账数据<br>
---------------------------------------------------------------------------------------------
	 */
	public void updateOverBankInfos(String bankAcc, Date transDate, String curId){
		String overSql = "update fc_compare_acc set compare_sign = :compareSignOver" +
				" where comp_acc = :bankAcc and (compare_sign = :compareSignYes or compare_sign = :compareSignHand)" +
				" and cur_id = :curId " +
				" and tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
		params.put("transDate", DateUtils.formatDate(transDate));
		params.put("compareSignOver", FinacialConstant.COMPARE_SIGN_OVER);
		params.put("compareSignYes", FinacialConstant.COMPARE_SIGN_YES);
		params.put("compareSignHand", FinacialConstant.COMPARE_SIGN_HAND);
		createSQLQuery(overSql.toString(), params).executeUpdate();
	}
	/**
	 * 根据开始时间，结束时间查询对账信息
	 * @param tallyStartDate
	 * @param tallyEndDate
	 * @return
	 */
	public List<FcCompareAccVO> listForEBill(Date tallyStartDate, Date tallyEndDate,List<String> accS) {
		StringBuilder sql = new StringBuilder();
		sql.append("    SELECT fca.voucher_no as voucherNo," +
				"           fca.interest_start_date as interestStartDate," +
				"           fca.tally_date as tallyDate," +
				"           fca.create_by as createBy," +
				"           fca.summary_content as summaryContent," +
				"           fca.comp_acc as compAcc," +
				"           fca.debit_money as debitMoney," +
				"           fca.credit_money as creditMoney," +
				"           fca.balance as balance," +
				"           fca.redblue_sign as redblueSign," +
				"           fca.line_no as lineNo " +
				"      FROM fc_compare_acc fca " +
				"     WHERE fca.tally_date >= :tallyStartDate " +
				"        AND fca.tally_date < :tallyEndDate  ") ;
		sql.append(" and ").append(getSQLIn(accS, 900, "fca.comp_acc"));
		sql.append("     ORDER BY fca.comp_acc ");
		Map<String, Object> params = new HashMap<>();
		params.put("tallyStartDate",tallyStartDate);
		params.put("tallyEndDate",tallyEndDate);
		return listBySQLAliasToBean(sql.toString(),params,FcCompareAccVO.class);
	}
    /**
     * <br>
     * <b>功能：</b> 查询ERP对账结果 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/15 0015 <br>
     */
    public DataTablesOutput queryCheckList(DataTablesInput input, List<String> accS, Date calcDate, String compareSign) {
        StringBuilder sql = new StringBuilder();
        String compareCalDateString = new StringBuilder("bt_to_date('").append(DateUtils.formatDate(calcDate)).append("','yyyy-MM-dd')").toString();
        sql.append(" select ")
                .append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, ")
                .append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate, ")
                .append(" BT_NVL_NUM(eb.erpCnt,0) as erpCnt, BT_NVL_NUM(bb.bankCnt,0) as bankCnt ")
                .append(" from fc_check_init fc left join ")
                .append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
                .append("  where (fb.status <> -2 or fb.status is null) ")
                .append(" and fb.check_type = '1' ")
                .append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
                .append(" left join (select comp.comp_acc,comp.cur_id,count(1) as erpCnt from fc_compare_acc comp where comp.compare_sign <> '3' and comp.check_type = '2' ");
        sql.append(" and exists (select 1  from fc_check_init fc2  where fc2.bank_acc = comp.comp_acc and comp.cur_id = fc2.cur_id ");
        sql.append(" and ( fc2.check_date < comp.tally_date  or comp.IMPORT_TYPE = '4' ) ) ");
        if (!EmptyUtils.isEmpty(calcDate)) {
            sql.append(" and comp.tally_date <= ").append(compareCalDateString);
        }
        sql.append(" and comp.compare_sign <> '3' group by comp.comp_acc,comp.cur_id) eb on eb.comp_acc = fc.bank_acc and eb.cur_id = fc.cur_id ")
                .append(" left join (select bank.bank_acc,bank.cur_id,count(1) as bankCnt from fc_bank_bill bank where bank.compare_sign <> '3' ");
        sql.append(" and exists (select 1  from fc_check_init fc1  where fc1.bank_acc = bank.bank_acc and bank.cur_id = fc1.cur_id " );
        sql.append(" and ( fc1.check_date < bank.tally_date  or bank.IMPORT_TYPE = '4' ) )");
        if (!EmptyUtils.isEmpty(calcDate)) {
            sql.append(" and bank.tally_date <= ").append(compareCalDateString);
        }
        sql.append(" group by bank.bank_acc,bank.cur_id) bb on bb.bank_acc = fc.bank_acc and bb.cur_id = fc.cur_id ")
                .append(" where fc.check_type = '2' ");
        if ("0".equals(compareSign)) {
            sql.append(" and fc.check_date < ").append(compareCalDateString);
            sql.append(" and (fd.end_date is null or fd.end_date < ").append(compareCalDateString).append(")");
        } else if ("1".equals(compareSign)) {
            sql.append(" and (fc.check_date >= ").append(compareCalDateString);
            sql.append("or fd.end_date >= ").append(compareCalDateString).append(")");
        }
        if (accS != null) {
            sql.append(" and ").append(getSQLIn(accS, 900, "fc.bank_acc"));
        }
        sql.append("  ");
        return getDynamicBySQL(input, sql.toString(), FcBankCheckVO.class,"",false);
    }
	/**
	 * 查询批量结转列表
	 * @param input
	 * @param compareCalDateString
	 * @param accS
	 * @param calcDate
	 * @param compareSign
	 * @return
	 */
	public DataTablesOutput getAdjustInfo(DataTablesInput input,String compareCalDateString,List<String> accS, String calcDate, String compareSign){
		StringBuilder sql = new StringBuilder();
		sql.append(" select ")
				.append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, fc.bank_balance AS bankBalance,fc.bln_balance AS blnBalance, ")
				.append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate, ")
				.append(" BT_NVL_NUM(eb.erpCnt,0) as erpCnt, BT_NVL_NUM(bb.bankCnt,0) as bankCnt ")
				.append(" from fc_check_init fc left join ")
				.append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
				.append("  where (fb.status <> -2 or fb.status is null) ")
				.append(" and fb.check_type = '1' ")	//FC_BANK_BALANCE_ADJUST.check_type（0中心对账 1ERP对账）
				.append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
				//comp.compare_sign <> '3' 银行对账数据表，对账数据不等于已结转
				.append(" left join (select comp.comp_acc,comp.cur_id,count(1) as erpCnt from fc_compare_acc comp where comp.compare_sign <> '3' and comp.check_type = '2' ");
		sql.append(" and exists (select 1  from fc_check_init fc2  where fc2.account = comp.comp_acc and comp.cur_id = fc2.cur_id ");
		sql.append(" and ( fc2.check_date < comp.tally_date  or comp.IMPORT_TYPE = '4' ) ) ");
		//comp.IMPORT_TYPE = 4  未达账初始化
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and comp.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" and comp.compare_sign <> '3' group by comp.comp_acc,comp.cur_id) eb on eb.comp_acc = fc.account and eb.cur_id = fc.cur_id ")
				.append(" left join (select bank.bank_acc,bank.cur_id,count(1) as bankCnt from fc_bank_bill bank where bank.compare_sign <> '3' ");
		sql.append(" and exists (select 1  from fc_check_init fc1  where fc1.bank_acc = bank.bank_acc and bank.cur_id = fc1.cur_id " );
		sql.append(" and ( fc1.check_date < bank.tally_date  or bank.IMPORT_TYPE = '4' ) )");
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and bank.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" group by bank.bank_acc,bank.cur_id) bb on bb.bank_acc = fc.bank_acc and bb.cur_id = fc.cur_id ")
				.append(" where fc.check_type = '2' ");
		if ("0".equals(compareSign)) {
			sql.append(" and fc.check_date < ").append(compareCalDateString);
			sql.append(" and (fd.end_date is null or fd.end_date < ").append(compareCalDateString).append(")");
		} else if ("1".equals(compareSign)) {
			sql.append(" and (fc.check_date >= ").append(compareCalDateString);
			sql.append("or fd.end_date >= ").append(compareCalDateString).append(")");
		}
		if (accS != null) {
			sql.append(" and ").append(getSQLIn(accS, 900, "fc.bank_acc"));
		}
		sql.append("  ");
		return getDynamicBySQL(input, sql.toString(), FcBankCheckVO.class,"",false);
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询ERP单位对账结果 <br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2022/9/22 <br>
	 */
	public DataTablesOutput queryCorpCheckList(DataTablesInput input, List<String> accS, Date calcDate, String compareSign) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.bankAcc,max(checkDate) as checkDate,max(beginDate) as beginDate,max(endDate) as endDate,sum(erpCnt) as erpCnt,sum(bankCnt) as bankCnt  from (");
		String compareCalDateString = new StringBuilder("bt_to_date('").append(DateUtils.formatDate(calcDate)).append("','yyyy-MM-dd')").toString();
		sql.append(" select ")
				.append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, ")
				.append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate, ")
				.append(" BT_NVL_NUM(eb.erpCnt,0) as erpCnt, BT_NVL_NUM(bb.bankCnt,0) as bankCnt ")
				.append(" from fc_check_init fc left join ")
				.append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
				.append("  where (fb.status <> -2 or fb.status is null) ")
				.append(" and fb.check_type = '1' ")
				.append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
				.append(" left join (select comp.comp_acc,comp.cur_id,count(1) as erpCnt from fc_compare_acc comp where comp.compare_sign <> '3' and comp.check_type = '2' ");
		sql.append(" and exists (select 1  from fc_check_init fc2  where fc2.bank_acc = comp.comp_acc and comp.cur_id = fc2.cur_id ");
		sql.append(" and ( fc2.check_date < comp.tally_date  or comp.IMPORT_TYPE = '4' ) ) ");
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and comp.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" and comp.compare_sign <> '3' group by comp.comp_acc,comp.cur_id) eb on eb.comp_acc = fc.bank_acc and eb.cur_id = fc.cur_id ")
				.append(" left join (select bank.bank_acc,bank.cur_id,count(1) as bankCnt from fc_bank_bill bank where bank.compare_sign <> '3' ");
		sql.append(" and exists (select 1  from fc_check_init fc1  where fc1.bank_acc = bank.bank_acc and bank.cur_id = fc1.cur_id " );
		sql.append(" and ( fc1.check_date < bank.tally_date  or bank.IMPORT_TYPE = '4' ) )");
		if (!EmptyUtils.isEmpty(calcDate)) {
			sql.append(" and bank.tally_date <= ").append(compareCalDateString);
		}
		sql.append(" group by bank.bank_acc,bank.cur_id) bb on bb.bank_acc = fc.bank_acc and bb.cur_id = fc.cur_id ")
				.append(" where fc.check_type = '2' ");
		if ("0".equals(compareSign)) {
			sql.append(" and fc.check_date < ").append(compareCalDateString);
			sql.append(" and (fd.end_date is null or fd.end_date < ").append(compareCalDateString).append(")");
		} else if ("1".equals(compareSign)) {
			sql.append(" and (fc.check_date >= ").append(compareCalDateString);
			sql.append("or fd.end_date >= ").append(compareCalDateString).append(")");
		}
		if (accS != null) {
			sql.append(" and ").append(getSQLIn(accS, 900, "fc.bank_acc"));
		}
		sql.append(" ) b group by bankAcc ");
		return getDynamicBySQL(input, sql.toString(), FcBankCheckVO.class,"",false);
	}
}
