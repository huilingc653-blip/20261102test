package com.bytter.credential.domain.international.dao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-11-30 18:02
 * @Version 1.0
 */
@Repository
public class CreatedCreditDao extends BaseDao <LcEnrol, String> {
    public List<LcEnrol> getLcEnrolById(String id) {
        Map<String, String> params = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from  LcEnrol where  id = :id");
        params.put("id", id);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 列表显示
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listCredit(DataTablesInput dataTablesInput, List<String> corpIds){
        StringBuilder sql = new StringBuilder();
        sql.append("select  lc.id id, ");
        sql.append("        lc.lc_no lcNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.issuing_date issuingDate, ");
        sql.append("        lc.expiry_date expiryDate, ");
        sql.append("        lc.credit_money creditMoney, ");
        sql.append("        lc.issuing_cur_id issuingCurId, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lc.status status,");
        sql.append("        lc.purpose purpose");
        sql.append(" from LC_ENROL lc ");
        sql.append(" where 1 = 1 and (lc.status = 1 or lc.status = -1) and lc.lc_type = '0' and ");
        sql.append(getSQLIn(corpIds, 900, "lc.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 审批列表展示
     * @param dataTablesInput
     * @param ids 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listCreditApprove(DataTablesInput dataTablesInput, List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append("select  lc.id id, ");
        sql.append("        lc.lc_no lcNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.issuing_date issuingDate, ");
        sql.append("        lc.expiry_date expiryDate, ");
        sql.append("        lc.credit_money creditMoney, ");
        sql.append("        lc.issuing_cur_id issuingCurId, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lc.status status,  ");
        sql.append("        lc.purpose purpose");
        sql.append(" from LC_ENROL lc ");
        sql.append(" where 1 = 1 and lc.status >=11 and lc.status < 99 and lc.lc_type = '0' and ");
        sql.append(getSQLIn(ids, 900, "lc.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 查询该信用证号是否重复
     * @param params map参数
     * @return DataTablesOutput
     */
    public long getcreditNoCount(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
        hql.append("select ");
        hql.append("count(creditNo) ");
        hql.append(" from ");
        hql.append(" LcEnrol ");
        hql.append(" where ");
        hql.append(" 1 = 1 and lcType = '0' and creditNo = :creditNo");
        return countHqlResult(hql.toString(), params);
    }
    /**
     * 信用证查询报表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listReportCredits(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  ");
        sql.append("       lc.id, ");
        sql.append("       lc.bank_name issuingBank, ");
        //TODO 补充名称
        sql.append("       lc.bank_name issuingBankName, ");
        sql.append("       lc.re_bank reBank, ");
        sql.append("       lc.lc_inter lcInter, ");
        sql.append("       lc.corp_name applicant, ");
        sql.append("       lc.applicant_corp_name applicantName, ");
        sql.append("       lc.beneficiary beneficiary, ");
        sql.append("       lc.credit_no creditNo, ");
        sql.append("       lc.re_credit_no reCreditNo, ");
        sql.append("       lc.contract_no contractNo, ");
        sql.append("       lc.issuing_date issuingDate, ");
        sql.append("       lc.expiry_date expiryDate, ");
        sql.append("       lc.cur_name curName, ");
        sql.append("       lc.issuing_cur_id issuingCurId, ");
        sql.append("       bt_nvl_num(lc.credit_money,0) creditMoney, ");
        sql.append("       bt_nvl_num(lc.open_money,0) openMoney, ");
        sql.append("       lc.delay_dates delayDates, ");
        sql.append("       bt_nvl_num(lc.charge_money,0) chargeMoney, ");
        sql.append("       bt_nvl_num(lc.acceptance_fee,0) acceptanceFee, ");
        sql.append("       lc.discount_interest discountInterest, ");
        sql.append("       bt_nvl_num(t1.bill_money,0) billMoney, ");
        sql.append("       bt_nvl_num(t1.handling_charge,0) handlingCharge, ");
        sql.append("       t1.payment_date arrivalBillDate, ");
        sql.append("       bt_nvl_num(t2.payment_money,0) paymentMoney, ");
        sql.append("       t2.payment_date paymentDate, ");
        sql.append("       bt_nvl_num(t3.deposit_money,0) depositMoney, ");
        sql.append("       bt_nvl_num(t3.reapy_money,0) reapyMoney, ");
        sql.append("       lc.rmk rmk ");
        sql.append("  from lc_enrol lc ");
        sql.append("  left join (select lb.lc_id lc_id, ");
        sql.append("                    sum(lb.bill_money) bill_money, ");
        sql.append("                    sum(lb.handling_charge) handling_charge, ");
        sql.append("                    max(payment_date) payment_date ");
        sql.append("               from lc_bill_arrival lb ");
        sql.append("              group by lb.lc_id) t1 ");
        sql.append("    on lc.id = t1.lc_id ");
        sql.append("  left join (select lp.lc_id, ");
        sql.append("                    sum(lp.payment_money) payment_money, ");
        sql.append("                    max(lp.payment_date) payment_date ");
        sql.append("               from lc_payment lp ");
        sql.append("              group by lp.lc_id) t2 ");
        sql.append("    on lc.id = t2.lc_id ");
        sql.append("  left join (select ld.lc_id, ");
        sql.append("                    sum(ld.deposit_money) deposit_money, ");
        sql.append("                    sum(ld.reapy_money) reapy_money ");
        sql.append("               from lc_deposit ld ");
        sql.append("              group by ld.lc_id) t3 ");
        sql.append("    on lc.id = t3.lc_id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and lc.status != -2 ");
        sql.append(" and lc.lc_type = '0' ");
        sql.append(" and " +getSQLIn(corpIds, 900, "lc.corp_id"));

        if (dataTablesInput.getOrder() != null && !dataTablesInput.getOrder().isEmpty()
                && dataTablesInput.getOrder().get(0) != null) {
            Integer orderColumnIndex = dataTablesInput.getOrder().get(0).getColumn();
            List<Column> columns = dataTablesInput.getColumns();
            if (orderColumnIndex != null && columns != null && orderColumnIndex >= 0
                    && orderColumnIndex < columns.size()) {
                String orderColumnName = columns.get(orderColumnIndex).getName();
                String orderDir = dataTablesInput.getOrder().get(0).getDir();
                if ("paymentMoney".equals(orderColumnName)) {
                    dataTablesInput.setOrderString(new OrderString(
                            "(bt_nvl_num(t2.payment_money,0))", orderDir));
                }else if("billMoney".equals(orderColumnName)){
                    dataTablesInput.setOrderString(new OrderString(
                            "(bt_nvl_num(t1.bill_money,0))", orderDir));
                }
            }
        }
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 获取信用证即将到期数据
     * @param params map参数
     * @return List<LcEnrol>
     */
    public List<LcEnrol> getLcEnrolbyExpiryDays(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
//        hql.append("select lc.* ");
        hql.append("  from LcEnrol lc ");
        hql.append(" where 1 = 1 ");
        hql.append("   and lc.expiryDate <= :exprireDate ");
        hql.append("   and lc.expiryDate > :today ");
        hql.append("   and lc.lcType = '0' ");
        hql.append("   and lc.status >= 99");
        return listByHQL(hql.toString(), params);
    }
    /**
    *查询没有挂接担保的数据
    *@Param dataTablesInput
    *@Param corpIds
    *@return
    */
    public DataTablesOutput listLcNotGua(DataTablesInput dataTablesInput){
        StringBuilder sql = new StringBuilder();
        sql.append("select lc.*,lc.bank_name issuingBankName ");
        sql.append("  from (select lce.*, (lce.credit_money - bt_nvl_num(finish_data.finish_money,0)) left_money ");
        sql.append("          from lc_enrol lce ");
        sql.append("          left join (select lc_id, ");
        sql.append("                           sum(bt_nvl_num(finish_money, 0)) finish_money ");
        sql.append("                      from (select lc_id, sum(payment_money) finish_money ");
        sql.append("                              from LC_PAYMENT ");
        sql.append("                             where status = 999 ");
        sql.append("                             group by lc_id ");
        sql.append("                            union all ");
        sql.append("                            select ld.lc_id lc_id, ");
        sql.append("                                   sum(ldr.finish_money) fish_money ");
        sql.append("                              from LC_DEPOSIT ld ");
        sql.append("                              left join (select DEPOSIT_ID, ");
        sql.append("                                               sum(repay_money) finish_money ");
        sql.append("                                          from LC_DEPOSIT_REPAY ");
        sql.append("                                         group by DEPOSIT_ID) ldr ");
        sql.append("                                on ld.id = ldr.DEPOSIT_ID ");
        sql.append("                             group by ld.lc_id) finish ");
        sql.append("                     group by lc_id) finish_data ");
        sql.append("            on lce.id = finish_data.lc_id) lc ");
        sql.append(" where lc.left_money > 0 ");
        sql.append(" and  (lc.status = 95 or lc.status = 105)");
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcEnrolVO.class, "lc", false, null, null);
    }
    /**
     *单位校验使用
     *@Param corpId
     *@return
     */
    public List<LcEnrol> verifyCorpDeleteByCorpId(String corpId){
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        StringBuilder hql = new StringBuilder();
        hql.append(" from LcEnrol where 1 = 1 and status != " + InternationalConstant.DELETED + " and corpId = :corpId");
        return listByHQL(hql.toString(), params);
    }
    /**
     *单位校验使用
     *@Param corpId
     *@return
     */
    public List<LcEnrol> verifyCorpDisableByCorpId(String corpId){
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        StringBuilder hql = new StringBuilder();
        hql.append(" from LcEnrol where 1 = 1 and lcType = '0' and status = 105 and corpId = :corpId");
        return listByHQL(hql.toString(), params);
    }
    /**
     *单位校验使用
     *@Param corpId
     *@return
     */
    public List<LcEnrol> verifyCorpDisableByCorpIdForReceive(String corpId){
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        StringBuilder hql = new StringBuilder();
        hql.append(" from LcEnrol where 1 = 1 and lcType = '1' and status != -2 and corpId = :corpId");
        return listByHQL(hql.toString(), params);
    }
    /**
     * 单位校验使用
     *
     * @return
     * @Param corpId
     */
    public Integer verifyTempData(String table, String where) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from ");
        sql.append(table);
        sql.append(" where 1 = 1 ");
        sql.append("and "+where);
        return Integer.valueOf(createSQLQuery(sql.toString()).uniqueResult().toString());
    }
}
