package com.bytter.check.dao;
import com.bytter.check.constant.CheckConstant;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.constant.FinacialResourceConstant;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.vo.FcBankBillSearchVo;
import com.bytter.check.vo.FcBankBillVo;
import com.bytter.check.vo.FcCompareErpSearchVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Dao
* @Description: FC_BANK_BILLgetInterfaceFcBankBill
* @author lixi
* @date 2017-06-02 10:04:16
* @version 1.0
*
*/
@Repository
public class FcBankBillDao extends BaseDao<FcBankBill,String>{
	
	//TODO SYS服务提供
	private void getQueryCorpIdsSql(StringBuilder sqlSb, String userId,String field ){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM SYS_CORP_USER_RESOURCE cur ");
		sql.append(" where 1 = 1 ");
		if (StringUtils.isNotBlank(userId)) {
			sql.append(" and cur.USER_ID ='"+ userId+"'");
		}
		sql.append(" AND cur.PERMISSION ='"+ FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_QUERY+"'");
		sql.append(" and ").append(field).append(" = cur.CORP_ID");
		sql = sqlSb.append(" and exists(" ).append(sql).append(")");
	}
	/**
	 * 查询银行接口导入数据
	 */
	@SuppressWarnings("rawtypes")
	//TODO SYS服务提供 ; bis_acc_dtl --> 哪个模块的表？
	public DataTablesOutput getInterfaceFcBankBill(DataTablesInput dataTablesInput, String userId) {
		StringBuilder sql = new StringBuilder();//e.account as account,
		sql.append(" select a.id as id , a.bank_acc as bankacc,a.trans_time as transtime, (case a.cd_sign  when '1' then  a.amt else  0 end) as debitmoney , a.cur_Id as curId , "
				+ " a.OPP_ACC_NO as oppAccNo ,a.OPP_ACC_NAME as oppAccName, "
				+ "  (case a.cd_sign   when '0' then  a.amt else 0 end) as creditmoney,a.voucher_no as billno,a.voucher_no as voucherNo, a.remark as summarycontent  ");
		sql.append(" from bis_acc_dtl a inner join bt_bank_acc f on a.bank_acc = f.bank_acc " +
				" where 1=1 and ( a.import_sign <> 1 or BT_NVL_NUM(a.import_sign,10) <> 10 ) ");
		sql.append(" and exists( select 1 from fc_check_init e where e.bank_acc = a.bank_acc ) ");
		//sql.append(" and ").append(getSQLIn(corpAuth, 900, "f.corp_id"));
		getQueryCorpIdsSql(sql,userId,"f.corp_id");
		sql.append(" and exists (");
		sql.append(" select 1 ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id ");
		sql.append(" where acc.valid_sign = '1' ");
		sql.append("   and acc.status = 95 ");
		sql.append("   and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' ");
		sql.append("   and ACCOUNT_GRANT.ACCOUNT_TYPE BETWEEN '2' and '3' ");
		getQueryCorpIdsSql(sql,userId,"ACCOUNT_GRANT.CORP_ID");
		//sql.append("   and (" + getSQLIn(corpAuth, 900, "ACCOUNT_GRANT.CORP_ID") + ") ");
		sql.append("   and ACCOUNT_GRANT.USER_ID ='" + userId + "'");
		sql.append(" and a.bank_acc = acc.bank_acc )");
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if (StringUtils.isNotEmpty(gedTransTime)) {
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if (StringUtils.isNotEmpty(ledTransTime)) {
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}
		DataTablesOutput d = getDynamicBySQL(dataTablesInput, sql.toString(), FcBankBillVo.class, null, false);
		return d;
	}
	/**
	 * 查询
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getFcBankBill(DataTablesInput dataTablesInput,List<String> bankAccList) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.id as id ,a.voucher_no as voucherNo,a.account as account ,a.bank_acc as bankAcc , a.tally_date as tallyDate,a.debit_money as debitMoney, a.credit_money as creditMoney, ");
		sql.append(" a.balance as balance, a.redblue_sign as redblueSign,a.bill_no as billNo,a.compare_sign as compareSign,a.bank_id as bankId,a.adjust_date as adjustDate,  ");
		sql.append(" a.import_date as importDate, a.import_type as importType,a.line_no as lineNo "); //, case when c.account is null then '0' else '1' end type
		sql.append(" from fc_bank_bill a  ");
		sql.append(" left join fc_check_init i on i.bank_acc = a.bank_acc and i.account = a.account  ");
		sql.append("  and i.check_date < a.tally_date ");
		sql.append(" where 1 = 1  ");  //and b.acc_type  in ('02')
		if (ObjectUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_bankTypeId")) ){
			String bankTypeId = dataTablesInput.getFormdata().get("eqs_bankTypeId").toString();
			sql.append(" and a.bank_type_id = ").append("'").append(bankTypeId).append("'");
		}
		sql.append(" and ").append(getSQLIn(bankAccList, 900, "a.bank_acc"));
		return getDynamicBySQL(dataTablesInput,sql.toString(),FcBankBillVo.class,null, false);
	}
	/**
	 * 查询银行接口导入数据
	 */
	public String findBankAccSql( List<String> corpAuth, String userId) {
		StringBuilder sql = new StringBuilder();//e.account as account,
		sql.append(" SELECT  acc.id,acc.bank_acc AS bankAcc,acc.bank_name AS bankName,acc.acc_name AS accName ");
		sql.append(" FROM bt_bank_acc acc ");
		sql.append(" LEFT JOIN SYS_ACCOUNT_GRANT ACCOUNT_GRANT  ON ACCOUNT_GRANT.ACCOUNT_ID = acc.id   ");
		sql.append(" LEFT JOIN SYS_CORP_USER_RESOURCE cur ON ACCOUNT_GRANT.CORP_ID = cur.CORP_ID AND acc.corp_id = cur.CORP_ID ");
		sql.append(" WHERE acc.valid_sign = '1'  AND acc.status = 95 ");
		sql.append(" AND ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' ");
		sql.append(" AND ACCOUNT_GRANT.ACCOUNT_TYPE IN ('2', '3') ");
		sql.append(" AND cur.PERMISSION ='"+ FinacialResourceConstant.FINACIAL_CHECK_BANK_INTERFACE_QUERY+"'");
		sql.append(" and cur.USER_ID = '" + userId + "' ");
		sql.append(" and ACCOUNT_GRANT.USER_ID = '" + userId + "' ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_id"));
		return sql.toString();
	}
	/**
	 * 查询银行接口导入数据
	 */
	public String findBisAccDtlSql( List<String> corpAuth, List<String> accList) {
		StringBuilder sql = new StringBuilder();//e.account as account,
		sql.append(" select a.id as id , a.bank_acc as bankacc,a.trans_time as transtime, (case a.cd_sign  when '1' then  a.amt else  0 end) as debitmoney , a.cur_Id as curId , "
				+ " a.OPP_ACC_NO as oppAccNo ,a.OPP_ACC_NAME as oppAccName, "
				+ "  (case a.cd_sign   when '0' then  a.amt else 0 end) as creditmoney,a.voucher_no as billno,a.voucher_no as voucherNo, a.remark as summarycontent  ");
		sql.append(" from bis_acc_dtl a where 1=1 and ( a.import_sign <> 1 or a.import_sign is null ) ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id"));
		sql.append(" and ").append(getSQLIn(accList, 900, "a.bank_acc"));
		return sql.toString();
	}
	/**
	 * 查询银行接口导入数据
	 */
	@SuppressWarnings("rawtypes")
	//TODO SYS服务提供
	public DataTablesOutput getInterfaceFcBankBill(DataTablesInput dataTablesInput, List<String> corpAuth, String userId) {
		StringBuilder sql = new StringBuilder();//e.account as account,
		sql.append(" select a.id as id , a.bank_acc as bankacc,a.trans_time as transtime, (case a.cd_sign  when '1' then  a.amt else  0 end) as debitmoney , a.cur_Id as curId , "
				+ " a.OPP_ACC_NO as oppAccNo ,a.OPP_ACC_NAME as oppAccName, "
				+ "  (case a.cd_sign   when '0' then  a.amt else 0 end) as creditmoney,a.voucher_no as billno,a.voucher_no as voucherNo, a.remark as summarycontent  ");
		sql.append(" from bis_acc_dtl a inner join bt_bank_acc f on a.bank_acc = f.bank_acc " +
				" where 1=1 and ( a.import_sign <> 1 or a.import_sign is null ) ");
		sql.append(" and exists( select 1 from fc_check_init e where e.bank_acc = a.bank_acc ) ");
		sql.append(" and ").append(getSQLIn(corpAuth, 900, "f.corp_id"));
		sql.append(" and a.bank_acc in (");
		sql.append(" select acc.bank_acc ");
		sql.append(" from bt_bank_acc acc ");
		sql.append(" left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id ");
		sql.append(" where acc.valid_sign = '1' ");
		sql.append("   and acc.status = 95 ");
		sql.append("   and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' ");
		sql.append("   and ACCOUNT_GRANT.ACCOUNT_TYPE in ('2', '3') ");
		sql.append("   and (" + getSQLIn(corpAuth, 900, "ACCOUNT_GRANT.CORP_ID") + ") ");
		sql.append("   and ACCOUNT_GRANT.USER_ID = '" + userId + "' ");
		sql.append(" ) ");
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if (StringUtils.isNotEmpty(gedTransTime)) {
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if (StringUtils.isNotEmpty(ledTransTime)) {
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FcBankBillVo.class, null, false);
	}
	
   /**
     * 查询未对账银行对账单
     */
	public List<FcBankBill> insertUnCheckBankBills(FcBankBillVo fc) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select dtl.bank_acc                                        as bankAcc, ");
		sql.append("        BT_TRUNC_DATE(dtl.trans_time)                       as tallyDate, ");
		sql.append("        (case dtl.cd_sign when '1' then dtl.amt else 0 end) as debitMoney, ");
		sql.append("        (case dtl.cd_sign when '0' then dtl.amt else 0 end) as creditMoney, ");
		sql.append("        dtl.OPP_ACC_NO                                      as oppAccNo, ");
		sql.append("        dtl.OPP_ACC_NAME                                    as oppAccName, ");
		sql.append("        dtl.voucher_no                                      as voucherNo, ");
		sql.append("        dtl.remark                                          as summaryContent, ");
		sql.append("        dtl.cur_Id                                          as curId, ");
		sql.append("        dtl.id                                              as bankId ");
		sql.append(" from bis_acc_dtl dtl ");
		sql.append("          inner join bt_bank_acc bank on dtl.bank_acc = bank.bank_acc ");
		sql.append(" where (dtl.import_sign <> '1' or dtl.import_sign is null) ");
		if (StringUtils.isNotEmpty(fc.getId())) {
			sql.append(" and (" + getSQLIn(Arrays.asList(fc.getId().split(",")), 900, "dtl.id") + ")");
		}else {
			if (StringUtils.isNotEmpty(fc.getBankAcc())) {
				params.put("bankacc", fc.getBankAcc());
				sql.append("   and bank.bank_acc = :bankacc ");
			}
			if (StringUtils.isNotEmpty(fc.getCorpId())) {
				params.put("corpId", fc.getCorpId());
				sql.append("   and bank.CORP_ID = :corpId ");
			}
			sql.append("   and dtl.trans_time >= :begindate ");
			sql.append("   and dtl.trans_time < :enddate ");
			params.put("begindate", fc.getGedTransTime());
			Calendar c = Calendar.getInstance();
			c.setTime(fc.getLedTransTime());
			c.add(Calendar.DAY_OF_MONTH, 1);
			params.put("enddate", c.getTime());
		}
		sql.append("   and exists(select 1 from fc_check_init e where e.bank_acc = dtl.bank_acc) ");
		return listBySQLAliasToBean(sql.toString(), params, FcBankBill.class);
	}
	

	/**
	 * 修改银行明细导出标志
	 */
	//TODO SYS服务提供
	public int updateBankBillImportSign(FcBankBillVo fcBankBill, String userId, List<String> corpAuth) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" update bis_acc_dtl dtl set import_sign = '1' , import_date = BT_SYSDATE()  ");
		sql.append("          where 1 = 1  ");
		sql.append("          and (import_sign <> '1' or import_sign is null) ");
		if (StringUtils.isNotEmpty(fcBankBill.getId())) {
			sql.append(" and (" + getSQLIn(Arrays.asList(fcBankBill.getId().split(",")), 900, "dtl.id") + ")");
		}else {
			sql.append("          and dtl.trans_time >= :begindate  ");
			sql.append("          and dtl.trans_time < :enddate  ");
			if (StringUtils.isNotEmpty(fcBankBill.getBankAcc())) {
				sql.append("          and dtl.bank_acc = :bankAcc  ");
				params.put("bankAcc", fcBankBill.getBankAcc());
			} else {
				sql.append("          and dtl.bank_acc in (  ");
				sql.append(" select acc.bank_acc ");
				sql.append(" from bt_bank_acc acc ");
				sql.append("          left join erp_basic_data erp on acc.bank_acc = erp.bytter_code ");
				sql.append("          left join SYS_ACCOUNT_GRANT ACCOUNT_GRANT on ACCOUNT_GRANT.ACCOUNT_ID = acc.id ");
				sql.append(" where erp.data_type_code = 'BT_BANK_ACC' ");
				sql.append("   and erp.type_code = 'rsm' ");
				sql.append("   and acc.valid_sign = '1' ");
				sql.append("   and acc.status = 95 ");
				sql.append("   and ACCOUNT_GRANT.CAN_QUERY_DETAIL = '1' ");
				sql.append("   and ACCOUNT_GRANT.ACCOUNT_TYPE in ('2', '3') ");
				sql.append("   and (" + getSQLIn(corpAuth, 900, "ACCOUNT_GRANT.CORP_ID") + ") ");
				sql.append("   and ACCOUNT_GRANT.USER_ID = '" + userId + "' ");
				sql.append(" ) ");
			}
			params.put("begindate", fcBankBill.getGedTransTime());
			Calendar c = Calendar.getInstance();
			c.setTime(fcBankBill.getLedTransTime());
			c.add(Calendar.DAY_OF_MONTH, 1);
			params.put("enddate", c.getTime());
		}
		return createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	  /**
     * 
     * 是否存在已结转的数据
     */
    @SuppressWarnings("rawtypes")
    public boolean isHaveOverBankBill(String bankIds) {
        String sql = "select * from fc_bank_bill a where "
                + " a.import_type = :importType and a.bank_Id in (:id) and a.compare_Sign = :compareSign";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("importType", FinacialConstant.IMPORT_TYPE_INTERFACE);
        params.put("id", bankIds.split(","));
        params.put("compareSign", FinacialConstant.COMPARE_SIGN_OVER);
        List list = createSQLQuery(sql, params).list();
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
	/**
	 *
	 * 是否存在已提取的数据
	 */
	@SuppressWarnings("rawtypes")
	public boolean isHaveOverBankBill1(String bankIds) {
		String sql = "select * from fc_bank_bill a where "
				+ " a.import_type = :importType and a.bank_Id in (:id) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("importType", FinacialConstant.IMPORT_TYPE_INTERFACE);
		params.put("id", bankIds.split(","));
		List list = createSQLQuery(sql, params).list();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}
    /**
     * 删除银行对账数据
     */
    public void deleteFcBankBills(FcBankBill temp) {
    	 String bankId = temp.getBankId();
         String sql = "delete from fc_bank_bill  where import_type = :importType and bank_Id in (:bankId)";
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("bankId", bankId);
         params.put("importType", FinacialConstant.IMPORT_TYPE_INTERFACE);
         createSQLQuery(sql, params).executeUpdate();
	}
	 
    /**
     * 查询维护了余额初始化过的账号
     */
  //TODO SYS服务提供
	public List<FcBankBillVo> getIsCheckBankAcc(List<String> accList){
		Map<String,Object> val = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct b.bank_acc as bankacc ");
		sb.append(" from fc_check_init b   where 1=1 ");
		sb.append(" and ").append(getSQLIn(accList, 900, "b.bank_acc"));
		return listBySQLAliasToBean(sb.toString(), val, FcBankBillVo.class);
	}
    
	/**
	 * 功能介绍:查询银行对账数据(未达、已对账、已勾兑、已对账等)<br>中心的
	 * <b>日期：</b>2017年6月6日<br>
	 * @version 1.0
	 * @author caozx
	 */
	 @SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryBankCheckDatas(String bankAcc, String account, Date transDate, String[] compareSigns, BigDecimal beginMoney, BigDecimal endMoney) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		 List<Map<String, Object>> list = new ArrayList<>();
		sql.append("select distinct a.tally_date as tallyDate,a.account as account,a.bank_acc as bankAcc,a.import_type as importType,a.bank_id as bankId,  ");
		sql.append("a.summary_Content as summaryContent,a.line_no as lineNo,a.redblue_sign as redblueSign,");
		sql.append("case when BT_NVL_NUM(a.DEBIT_MONEY,0) <> 0 then '1' else '0' end as cdSign,");
		sql.append("BT_NVL_NUM(a.debit_money,0) + BT_NVL_NUM(a.credit_money,0) as money,a.voucher_No as voucherNo,a.compare_sign as compareSign,");
		sql.append(" a.id,a.BILL_NO as billNo ");
		sql.append(" from  fc_bank_bill a  ");
		sql.append(" inner join fc_check_init i on i.bank_acc = a.bank_acc ");
		sql.append(" and i.check_date < a.tally_date ");
		int i=0;
		 StringBuilder sqladd =  new StringBuilder();
		for (String compareSign : compareSigns) {
			if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达账记录
				if(i!=0){sqladd.append(" or ");}
				sqladd.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_STAY+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//已经勾兑记录
				if(i!=0){sqladd.append(" or ");}
				sqladd.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_HAND+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已经对账记录
				if(i!=0){sqladd.append(" or ");}
				sqladd.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_YES+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)){//未对账
				if(i!=0){sqladd.append(" or ");}
				sqladd.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_NO+"'  or a.compare_sign = '' or a.compare_sign is null ");
				i++;
			}
		}
		if(EmptyUtils.isNotEmpty(sqladd.toString())){
			sql.append(" and ( ");
			sql.append(sqladd);
			sql.append(" ) ");
		} else {
			return list;
		}
		sql.append(" and a.bank_acc in (:bankAcc) and a.account in (:account) and a.tally_date <= :transDate ");
        if (!EmptyUtils.isEmpty(beginMoney) && !EmptyUtils.isEmpty(endMoney)) {
            if (beginMoney.compareTo(BigDecimal.ZERO) > 0 || endMoney.compareTo(BigDecimal.ZERO) > 0) {
             sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY >=:beginMoney) ");
             sql.append(" and (a.DEBIT_MONEY + a.CREDIT_MONEY <=:endMoney) ");
             params.put("beginMoney", beginMoney);
             params.put("endMoney", endMoney);
            }
        }
		sql.append(" order by a.bank_acc,tally_date,money ");
		params.put("bankAcc", bankAcc.split(","));
		params.put("account", account.split(","));
		params.put("transDate", transDate);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("tallyDate", StandardBasicTypes.DATE);
		sqlQuery.addScalar("account", StandardBasicTypes.STRING);
		sqlQuery.addScalar("bankAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("importType", StandardBasicTypes.STRING);
		sqlQuery.addScalar("bankId", StandardBasicTypes.STRING);
		sqlQuery.addScalar("summaryContent", StandardBasicTypes.STRING);
		sqlQuery.addScalar("lineNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("redblueSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("cdSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("money", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("voucherNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compareSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("id", StandardBasicTypes.STRING);
		sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 list = sqlQuery.list();
		return list;
	 }
	/**
	 * 功能介绍:查询银行对账数据(未达、已对账、已勾兑、已对账等)<br>ERPDE
	 * <b>日期：</b>2017年6月6日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryErpBankCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO, Date transDate, String[] compareSigns) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct a.tally_date as tallyDate,a.account as account,a.bank_acc as bankAcc,a.import_type as importType,a.bank_id as bankId,  ");
		sql.append("a.summary_Content as summaryContent,a.line_no as lineNo,a.redblue_sign as redblueSign,a.opp_acc_name as oppAccName,");
		sql.append("case when BT_NVL_NUM(a.DEBIT_MONEY,0) <> 0 then '1' else '0' end as cdSign,");
		sql.append("BT_NVL_NUM(a.debit_money,0) + BT_NVL_NUM(a.credit_money,0) as money,a.voucher_No as voucherNo,a.compare_sign as compareSign,");
		sql.append(" A.CUR_ID as curId,a.id,a.BILL_NO as billNo ");
		sql.append(" from  fc_bank_bill a  ");
		sql.append(" where 1 = 1 and a.compare_sign <> '").append(FinacialConstant.COMPARE_SIGN_OVER).append("' ");
        sql.append(" and exists (select 1  from fc_check_init i  where i.bank_acc = a.bank_acc and i.cur_id = a.cur_id ");
        sql.append(" and ( i.check_date < a.tally_date or a.import_type = '4' )  ");
        sql.append(" ) ");
        StringBuilder compareSignStr = new StringBuilder();
		int i=0;
		for (String compareSign : compareSigns) {
			if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达账记录
				if(i!=0){compareSignStr.append(" or ");}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_STAY+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//已经勾兑记录
				if(i!=0){compareSignStr.append(" or ");}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_HAND+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已经对账记录
				if(i!=0){compareSignStr.append(" or ");}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_YES+"' ");
				i++;
				continue;
			}
			if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)){//未对账
				if(i!=0){compareSignStr.append(" or ");}
				compareSignStr.append(" a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_STAY_UP+"'  or a.compare_sign = '"+FinacialConstant.COMPARE_SIGN_NO+"'  or a.compare_sign = '' or a.compare_sign is null ");
				i++;
			}
		}
        if (StringUtils.isNotEmpty(compareSignStr)) {
            sql.append("  and ( " + compareSignStr + " )");
        }
		sql.append(" and a.bank_acc = :bankAcc and a.tally_date <= :transDate ");
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
				sql.append(" order by money ");
			}
			if ("oppAccName".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by a.opp_acc_name ");
			}
			if ("voucherNo".equals(fcCompareErpSearchVO.getOrderKey())) {
				sql.append(" order by a.voucher_No ");
			}
			sql.append(fcCompareErpSearchVO.getOrderType());
		} else {
			sql.append(" order by tally_date,(BT_NVL_NUM(a.debit_money,0) + BT_NVL_NUM(a.credit_money,0)) ");
		}
		params.put("bankAcc", fcCompareErpSearchVO.getBankAcc());
		params.put("account", fcCompareErpSearchVO.getAccount());
		params.put("curId", fcCompareErpSearchVO.getCurId());
		params.put("transDate", transDate);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(),params);
		sqlQuery.addScalar("tallyDate", StandardBasicTypes.DATE);
		sqlQuery.addScalar("account", StandardBasicTypes.STRING);
		sqlQuery.addScalar("oppAccName", StandardBasicTypes.STRING);
		sqlQuery.addScalar("bankAcc", StandardBasicTypes.STRING);
		sqlQuery.addScalar("importType", StandardBasicTypes.STRING);
		sqlQuery.addScalar("bankId", StandardBasicTypes.STRING);
		sqlQuery.addScalar("summaryContent", StandardBasicTypes.STRING);
		sqlQuery.addScalar("lineNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("redblueSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("cdSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("money", StandardBasicTypes.BIG_DECIMAL);
		sqlQuery.addScalar("voucherNo", StandardBasicTypes.STRING);
		sqlQuery.addScalar("compareSign", StandardBasicTypes.STRING);
		sqlQuery.addScalar("curId", StandardBasicTypes.STRING);
		sqlQuery.addScalar("id", StandardBasicTypes.STRING);
		sqlQuery.addScalar("billNo", StandardBasicTypes.STRING);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
	 
	 /**
	  * 功能介绍:查询银行对帐数据<br>中心的
	  * <b>日期：</b>2017年6月8日<br>
	  * @version 1.0
	  * @author caozx
	  */
	 public List<FcBankBillVo> queryCheckFcBankBills(FcBankBillSearchVo bankSearchBean,
													 String orderBy, String userCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct a.id,a.voucher_no,a.account,a.bank_acc,a.tally_date,BT_NVL_NUM(a.debit_money,0) as debit_money,BT_NVL_NUM(a.credit_money,0) as credit_money,a.balance, ");
		sql.append("a.redblue_sign,a.bill_no,a.compare_sign,a.bank_id,a.adjust_date,a.import_date,a.import_type,a.line_no,a.cur_id ");
		sql.append(" from fc_bank_bill a ");
		sql.append(" inner join fc_check_init i on i.bank_acc = a.bank_acc");
		sql.append(" and i.check_date < a.tally_date ");
		sql.append("where 1=1 "); 
		//sql.append("and fa.acc_type in ('02') ");
		sql.append("and a.tally_date <= :importDateEnd "); 
		sql.append("and a.account = :account ");
		sql.append("and a.bank_acc = :bankAcc "); 
		sql.append("and a.cur_id = :curId ");
		sql.append("and (a.compare_sign = '0' or a.compare_sign = '1' or a.compare_sign = '' or a.compare_sign is null) ");
		sql.append("order by a.account,a.tally_date,a.voucher_no,a.bank_id,a.import_date "); 
		params.put("importDateEnd", bankSearchBean.getImportDateEnd());
		params.put("account", bankSearchBean.getAccount());
		params.put("bankAcc", bankSearchBean.getBankAcc());
		params.put("curId", bankSearchBean.getCurId());
		return listBySQLAliasToBean(sql.toString(), params, FcBankBillVo.class);
	 }
	 
	 /**
	  * <b>功能介绍:更新状态为已对账</b><br>
	  * <b>日期：</b>2017年6月12日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  */
	 public void updateComparesignOverCheck(List<String> bankBillPKList,String compareSign){
		 StringBuilder sql  = new StringBuilder();
			sql.append("update fc_bank_bill a set a.compare_sign = :compareSign where 1=1 and ");
			sql.append(getSQLIn(bankBillPKList, 900, "a.id"));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("compareSign", compareSign);
			createSQLQuery(sql.toString(), params).executeUpdate();
	 }
	 
	 /**
	  * 功能介绍:获取银行未达账金额合计<br>
	  * <b>日期：</b>2017年6月8日<br>
	  * @version 1.0
	  * @author caozx
	  */
	 @SuppressWarnings("unchecked")
	 public Map<String, Object> queryStayOverBankSum(Date transDate, String bankAcc, String overType, String curId) {
		 StringBuilder sql = new StringBuilder();
		 sql.append("SELECT BT_NVL_NUM(count(a.debit_money),0) moneyCount, BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
		 sql.append(" FROM FC_BANK_BILL a where ");
		 sql.append(" exists (select 1 from fc_check_init i where i.bank_acc = a.bank_acc and i.cur_id = a.cur_id ");
		 sql.append(" and i.check_date < a.tally_date) and (a.compare_sign = :compareSign ) ");
		 if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {//银行已付、中心未付
			 sql.append(" and a.debit_money <> 0 and a.credit_money = 0");
	        } else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {//银行已收、中心未收
	        	sql.append(" and a.debit_money = 0 and a.credit_money <> 0");
	        }
		 sql.append(" and a.bank_acc= :bankAcc and a.cur_id = :curId and a.tally_date <= :transDate ");
		 Map<String, Object> params = new HashMap<String, Object>();
	        params.put("bankAcc", bankAcc);
	        params.put("curId", curId);
	        params.put("transDate", transDate);
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
	  * <b>功能介绍：</b>统计未对账总数<br>
	  * <b>日期：</b>2017年10月10日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param transDate
	  * @param bankAcc
	  * @param overType
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 public Map<String, Object> queryUnOverCtrSum(Date transDate, String bankAcc, String overType) {
		StringBuilder sql = new StringBuilder();
		sql.append("select BT_NVL_NUM(count(a.debit_money),0) moneyCount,");
		sql.append("BT_NVL_NUM(sum(a.debit_money),0) debitMoney,BT_NVL_NUM(sum(a.credit_money),0) creditMoney ");
		sql.append("from fc_bank_bill a  inner join fc_check_init i on i.bank_acc = a.bank_acc and i.check_date < a.tally_date ");
		sql.append("where (a.compare_sign = :compareSign or a.compare_sign = '' or a.compare_sign is null ) ");
		if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {// 银行已付、中心未付
			sql.append(" and a.debit_money <> 0 and a.credit_money = 0 ");
		} else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {// 银行已收、中心未收
			sql.append(" and a.debit_money = 0 and a.credit_money <> 0 ");
		}
		sql.append(" and a.bank_acc= :bankAcc and a.tally_date <= :transDate ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transDate", transDate);
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_NO);
		SQLQuery sqlQuery = createSQLQuery(sql.toString(), params);
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
	  * 
	  * <b>功能介绍:查询上期留存的银行数据</b><br>
	  * <b>日期：</b>2017年6月16日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  */
	@SuppressWarnings("unchecked")
	public List<FcBankBillVo> queryUnOverCtrStayDatas(String bankAcc, String overType, Date transDate, String curId) {
		 StringBuilder sql = new StringBuilder();
		 sql.append("SELECT a.VOUCHER_NO,a.ACCOUNT,a.BANK_ACC,a.TALLY_DATE,a.DEBIT_MONEY, a.CREDIT_MONEY, ");
		 sql.append("a.BALANCE, a.REDBLUE_SIGN,a.BILL_NO,a.COMPARE_SIGN,a.BANK_ID,a.ADJUST_DATE, ");
		 sql.append("a.IMPORT_DATE, a.IMPORT_TYPE,a.LINE_NO ,a.SUMMARY_CONTENT SUMMARY_CONTENT,a.cur_id ");
		 sql.append("FROM FC_BANK_BILL a WHERE (a.compare_sign = :compareSign0 ) ");
		 
	        if (CheckConstant.BANK_PAY_CTR_NO_PAY.equals(overType)) {//银行已付、中心未付
	        	 sql.append("and a.debit_money <> 0 and a.credit_money = 0 ");
	        } else if (CheckConstant.BANK_REC_CTR_NO_REC.equals(overType)) {//银行已收、中心未收
	        	sql.append("and a.debit_money = 0 and a.credit_money <> 0 ");
	        }
	        
	        sql.append("and exists (select 1 from fc_check_init b ");
			sql.append("where a.bank_acc = b.bank_acc and (a.tally_date > b.check_date  or a.import_type = '4') ) ");
			sql.append("and a.bank_acc in (:bankAccs) and a.cur_id = :curId and a.tally_date <= :transDate order by a.TALLY_DATE ");
	        
	        Map<String, Object> params = new HashMap<String, Object>();
	        String[] bankAccs = bankAcc.split(",");
	        params.put("bankAccs", bankAccs);
	        params.put("transDate", transDate);
	        params.put("curId", curId);
	        params.put("compareSign0", FinacialConstant.COMPARE_SIGN_STAY);
	        params.put("compareSign1", FinacialConstant.COMPARE_SIGN_STAY_UP);
            return listBySQLAliasToBean(sql.toString(), params, FcBankBillVo.class);
	 }
	
	/**
	 * <b>功能介绍：</b>结转银行对账数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param transDate
	 */
	public void updateOverBankInfos(String bankAcc, Date transDate, String curId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("update fc_bank_bill b set b.compare_sign = :compareSignOver ");
		sql.append("where b.bank_acc = :bankAcc and (b.compare_sign = :compareSignYes or b.compare_sign = :compareSignHand) ");
		sql.append("and b.cur_id = :curId ");
		sql.append("and b.tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
        params.put("transDate", DateUtils.formatDate(transDate));
        params.put("compareSignOver", FinacialConstant.COMPARE_SIGN_OVER);
        params.put("compareSignYes", FinacialConstant.COMPARE_SIGN_YES);
        params.put("compareSignHand", FinacialConstant.COMPARE_SIGN_HAND);
        createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>结转银行未达数据<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param transDate
	 */
	public void updateOverStayBankInfos(String bankAcc, Date transDate, String curId){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("update fc_bank_bill b set b.compare_sign = :compareSignNo where b.bank_acc = :bankAcc and b.compare_sign = :compareSignStay ");
		sql.append("and b.cur_id = :curId ");
		sql.append("and b.tally_date <= bt_to_date(:transDate,'yyyy-MM-dd') ");
		params.put("bankAcc", bankAcc);
		params.put("curId", curId);
        params.put("transDate", DateUtils.formatDate(transDate));
        params.put("compareSignNo", FinacialConstant.COMPARE_SIGN_NO);
        params.put("compareSignStay", FinacialConstant.COMPARE_SIGN_STAY);
        createSQLQuery(sql.toString(), params).executeUpdate();
	}
    /**
    * <br>
    * <b>功能：</b> 根据结转ID获取银行对账单数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2022/1/6 0006 <br>
    */
    public List<Map<String, String>> getOverBankBillId(String[] adId) {
        String sql = " select fc_bank_bill_id as ID from fc_bank_bill_proc p where p.ajust_id in (:ajustId) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ajustId", adId);
        return listBySQL(sql, params);
    }
	
	/**
	 * <b>功能介绍：</b>反结转更新-银行对账单为未对账<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankIds
	 */
	public int updateRedoAdjustOver(List<String> bankIds){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update fc_bank_bill b set b.compare_sign = :compareSign where 1=1 ");
        sql.append(" and ").append(getSQLIn(bankIds, 900, "b.id"));
		params.put("compareSign", FinacialConstant.COMPARE_SIGN_NO);
    	//params.put("ajustId", bankIds);
    	return createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
	/**
	 * <b>功能介绍：</b>获取银行可结转对账单<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param bankacc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FcBankBillVo> getBankDataOverData(Date transDate, String bankacc, String curId) {
		String sql = "select b.id,b.compare_sign from fc_bank_bill b where b.bank_acc = :bankAcc and b.compare_sign in (:compareSign) and b.tally_date <= :transDate ";
		sql += " and b.cur_id = :curId ";
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankacc);
        params.put("transDate", transDate);
		params.put("curId", curId);
        params.put("compareSign",
                new String[] { FinacialConstant.COMPARE_SIGN_STAY, FinacialConstant.COMPARE_SIGN_YES,
                		FinacialConstant.COMPARE_SIGN_HAND });
        return listBySQLAliasToBean(sql, params, FcBankBillVo.class);
	}
	
	/**
	 * <b>功能介绍：</b>查询银行结转明细清单<br>
	 * <b>日期：</b>2017年10月20日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param 
	 * @param ajustId
	 * @param stayRadios
	 * @param handRadios
	 * @param checkRadios
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<FcBankBillVo> queryBankOverDatas(String ajustId,String stayRadios,String handRadios,String checkRadios){
		StringBuilder sql = new StringBuilder();
		List<Integer> compareSigns = new ArrayList<Integer>();
		if("1".equals(stayRadios)){//未达账
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_STAY));
		}
		if("1".equals(handRadios)){//已勾兑
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_HAND));
		}
		if("1".equals(checkRadios)){//已对账
			compareSigns.add(Integer.valueOf(FinacialConstant.COMPARE_SIGN_YES));
		}
		sql.append("select b.tally_date,b.summary_content,BT_NVL_NUM(b.debit_money,0) as debit_money,BT_NVL_NUM(b.credit_money,0) as credit_money," );
        sql.append("b.voucher_no,b.bill_no,p.compare_sign_his as compare_sign from fc_bank_bill b, fc_bank_bill_proc p ");
		sql.append("where b.id = p.fc_bank_bill_id and p.ajust_id= '"+ajustId+"' ");
		sql.append("and " + getSQLIn(compareSigns, 900, "p.compare_sign_his"));
		sql.append(" order by b.tally_date ");
		return listBySQLAliasToBean(sql.toString(), null, FcBankBillVo.class);	
	}
	
	 /**
     * 计算最大行号
     */
	public Integer getMaxLineNo(String bankAcc, Date tallyDate) {
        String sql = "select case when max(line_no) is null then 0 else max(line_no) end  as lineNo "
                + " from fc_bank_bill where bank_acc= :bankAcc and tally_date = :tallyDate";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankAcc);
        params.put("tallyDate", tallyDate);
        SQLQuery sqlQuery = createSQLQuery(sql, params);
        sqlQuery.addScalar("lineNo", StandardBasicTypes.INTEGER);
        List list = sqlQuery.list();
        if (list != null && list.size() > 0) {
        	Integer lineNo = (Integer) list.get(0);
            return lineNo+1;
        }
        return 1;
    }
	/**
	 * <br>
	 * <b>功能：</b>更新上期未达的数据为未对账<br>
	 * 
	 */
	public void updateBankBillInitStatus(Date transDate, Date checkDate, String bankAcc, String curId) {
		String deleteSql = "Update fc_bank_bill a set compare_sign = '0' where tally_date <= to_date(:transDate,'yyyy-MM-dd' )"
				+ " AND compare_sign in (:compareSign) And bank_acc = :bankAcc and cur_id = :curId "
				+ " and exists (select 1 from fc_check_init b"
				+ " where a.bank_acc = b.bank_acc and a.cur_id = b.cur_id and a.tally_date > b.check_date and check_type = '2' ) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankAcc", bankAcc);
		params.put("transDate", DateUtils.formatDate(transDate));
		params.put("curId", curId);
		params.put("checkDate", DateUtils.formatDate(checkDate));
        /*params.put("compareSign", new String[]{ CompareSign.COMPARE_SIGN_STAY.get(),
        		CompareSign.COMPARE_STAY_HAND.get(), CompareSign.COMPARE_SIGN_STAY_UP.get()});*/
		params.put("compareSign", new String[]{FinacialConstant.COMPARE_SIGN_STAY_UP});//上期未达
		Query query = getSession().createSQLQuery(deleteSql);
		if (params != null)
			query.setProperties(params);
		 query.executeUpdate();
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>查询银行对帐数据ERP<br>
	 *     -----------------------------------------------------------
	 */
	@SuppressWarnings("unchecked")
	public List<FcBankBillVo> queryFcBankBillsErp(FcBankBillVo vo,String userCode) {
		Map<String,Object> param = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct a.voucher_no as voucherNo, a.account, a.bank_acc as bankAcc,a.tally_date as tallyDate,");
		sql.append(" a.debit_money as debitMoney, a.credit_money as creditMoney,a.balance,  a.redblue_sign as redblueSign,");
		sql.append(" a.bill_no as billNo, a.compare_sign as compareSign, a.bank_id as bankId,a.adjust_date as adjustDate,");
		sql.append(" a.summary_content as summaryContent,a.import_date as importDate,a.import_type as importType, a.line_no as lineNo,");
		sql.append(" case  when c.account is null then  '0' else '1' end TYPE, a.erp_voucher_no as erpVoucherNo, a.payee_acc as payeeAcc ,");
		sql.append(" a.id, a.cur_id as curId ");
		sql.append(" from fc_bank_bill a ");
		sql.append(" left join (select * from fc_bank_balance_adjust where status <> -2) c ");
		sql.append(" on c.bank_acc = a.bank_acc ");
		sql.append(" inner join fc_check_init i  on i.bank_acc = a.bank_acc ");
		sql.append(" and i.check_date < a.tally_date  where 1 = 1 ");
		//sql.append(" and a.redblue_sign = 'B' ");
		sql.append(" and (a.compare_sign = '0' or a.compare_sign = '' or  a.compare_sign is null) ");
		sql.append(" and i.check_date < a.tally_date ");
		if(vo.getTallyDate() != null){
			sql.append(" and a.tally_Date <= to_date(:tallyDate,'yyyy-MM-dd') ");
			param.put("tallyDate",DateUtils.formatDate(vo.getTallyDate()));
		}
		if(EmptyUtils.isNotEmpty(vo.getBankAcc())){
			sql.append(" and a.bank_acc = :bankAcc");
			//sql.append(" and a.account = :account ");
			param.put("bankAcc",vo.getBankAcc());
			//param.put("account",vo.getAccount());
		}
		if(EmptyUtils.isNotEmpty(vo.getCurId())){
			sql.append(" and a.cur_id = :curId ");
			param.put("curId", vo.getCurId());
		}
		sql.append(" order by a.account,a.tally_date,a.voucher_no ");
		return listBySQLAliasToBean(sql.toString(),param,FcBankBillVo.class);
	}
    /**
     * <br>
     * <b>功能：</b> 删除银行对账单 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/6/3 0003 <br>
     */
    public int deleteBankBill(FcBankBill entity) {
        String sql = " delete from fc_bank_bill where id = :id and bank_Acc = :bankAcc and compare_sign = :compareSign "
                + " and compare_Sign in (:oldSign) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("bankAcc", entity.getBankAcc());
        params.put("compareSign", entity.getCompareSign());
        params.put("oldSign", new String[]{FinacialConstant.COMPARE_SIGN_YES, FinacialConstant.COMPARE_SIGN_NO});
        return createSQLQuery(sql, params).executeUpdate();
    }

}
