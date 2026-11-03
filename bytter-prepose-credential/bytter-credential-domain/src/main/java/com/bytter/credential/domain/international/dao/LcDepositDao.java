package com.bytter.credential.domain.international.dao;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.domain.international.entity.LcDeposit;
import com.bytter.credential.domain.international.entity.LcDepositDelay;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.credential.domain.international.vo.LcDepositReportVO;
import com.bytter.credential.domain.international.vo.LcDepositVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-21 10:11
 * @Version 1.0
 */
@Repository
public class LcDepositDao extends BaseDao<LcDeposit, String> {
    /**
     *  进口押汇申请列表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listArrival(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("       lb.id id, ");
        sql.append("       lb.arrival_code arrivalCode, ");
        sql.append("       lc.lc_inter lcInter, ");
        sql.append("       lc.credit_no creditNo, ");
        sql.append("       lc.contract_no contractNo, ");
        sql.append("       lb.arrival_no arrivalNo, ");
        sql.append("       lb.pay_money payMoney, ");
        sql.append("       lb.cur_name curName, ");
        sql.append("       lb.cur_id curId,");
        sql.append("       lb.create_date createDate, ");
        sql.append("       lb.pay_date payDate, ");
        sql.append("       lb.payment_date paymentDate, ");
        sql.append("       lb.status status, ");
        sql.append("       lc.beneficiary beneficiary, ");
        sql.append("       lb.hand_doc_bank handDocBank ");
        sql.append("  from LC_BILL_ARRIVAL lb ");
        sql.append("  left join lc_enrol lc ");
        sql.append("  on lb.lc_id = lc.id ");
        sql.append("  where lb.arrival_type = 0 and lb.status = 1 and ");
        sql.append(getSQLIn(corpIds, 900, "lb.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcBillArrivalVO.class, "lb", false, null, null);
    }
    /**
     *  押汇列表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listDeposit(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ld.id id, ");
        sql.append("        ld.deposit_no depositNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        lb.bill_money billMoney, ");
        sql.append("        lb.bill_money - ((select nvl(sum(payment_money), 0) ");
        sql.append("                            from lc_payment ");
        sql.append("                           where ARRIVAL_ID = ld.arrival_id ");
        sql.append("                             and status >= 0) + ");
        sql.append("        (select nvl(sum(deposit_money), 0) ");
        sql.append("                            from lc_deposit ");
        sql.append("                           where ARRIVAL_ID = lb.id ");
        sql.append("                             and status >= 0)) needPayMoney, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.cur_id curId,");
        sql.append("        ld.create_date createDate, ");
        sql.append("        lb.payment_date paymentDate, ");
        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ld.status status ");
        sql.append("   from LC_DEPOSIT ld ");
        sql.append("   left join lc_bill_arrival lb ");
        sql.append("     on lb.id = ld.arrival_id ");
        sql.append("   left join lc_enrol lc ");
        sql.append("     on ld.lc_id = lc.id ");
        sql.append("  where 1 = 1 ");
        sql.append("  and (ld.status = 0 or ld.status = 95 or ld.status = -1) and ");
        sql.append(getSQLIn(corpIds, 900, "ld.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositVO.class, "ld", false, null, null);
    }
    /**
     *  押汇审批列表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listDepositApprove(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ld.id id, ");
        sql.append("        ld.deposit_no depositNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        lb.bill_money billMoney, ");
        sql.append("        lb.bill_money - ((select nvl(sum(payment_money), 0) ");
        sql.append("                            from lc_payment ");
        sql.append("                           where ARRIVAL_ID = ld.arrival_id ");
        sql.append("                             and status >= 0) + ");
        sql.append("        (select nvl(sum(deposit_money), 0) ");
        sql.append("                            from lc_deposit ");
        sql.append("                           where ARRIVAL_ID = lb.id ");
        sql.append("                             and status >= 0)) needPayMoney, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.cur_id curId,");
        sql.append("        ld.create_date createDate, ");
        sql.append("        lb.payment_date paymentDate, ");
        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ld.status status ");
        sql.append("   from LC_DEPOSIT ld ");
        sql.append("   left join lc_bill_arrival lb ");
        sql.append("     on lb.id = ld.arrival_id ");
        sql.append("   left join lc_enrol lc ");
        sql.append("     on ld.lc_id = lc.id ");
        sql.append("  where 1 = 1 ");
        sql.append("  and (ld.status >= 11 and ld.status < 95)  ");
        sql.append(" and "+getSQLIn(corpIds, 900, "ld.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositVO.class, "ld", false, null, null);
    }
    /**
     * 还押汇列表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listlcDepositRepay(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ld.id id, ");
        sql.append("        ld.deposit_no depositNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");//登记日期:押汇日期
        sql.append("        lb.payment_date paymentDate, ");//到单日期:到单日期
        sql.append("        lb.pay_date payDate, ");//押汇日期:到单到期日期-付汇日期
        sql.append("        ld.EXPIRY_DATE expiryDate, ");//到期日期：押汇到期日
        sql.append("        ld.cur_id curId,");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.create_date createDate, ");
        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ldd.expiry_date depositDelayExpiryDate, ");
        sql.append("        ld.status status ");
        sql.append("   from LC_DEPOSIT ld ");
        sql.append("   left join lc_bill_arrival lb ");
        sql.append("     on lb.id = ld.arrival_id ");
        sql.append("   left join lc_enrol lc ");
        sql.append("     on ld.lc_id = lc.id ");
        sql.append("   left join (select l.* from lc_deposit_delay l");
        sql.append("   where l.create_date = ( SELECT max( create_date ) FROM lc_deposit_delay l1 WHERE l.deposit_id = l1.deposit_id and l1.status > -2)) ldd ");
        sql.append("     on ld.id = ldd.deposit_id " );
        sql.append("  where 1 = 1 and");
        //(ldd.status >= 99 or ldd.status = -2)
        sql.append(" (ld.status = 96 or ld.status = 97 or (ld.status = 99 and ldd.status >= 99)) and ");
        sql.append(getSQLIn(corpIds, 900, "ld.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositVO.class, "ld", false, null, null);
    }
    /**
     * 根据押汇id获取数据
     * @param params map参数
     * @return List<LcDepositDelay>
     */
    public List<LcDepositDelay> getMaxDateDepositDelayByDepositId(Map<String,Object> params) {
        StringBuilder hql = new StringBuilder();
        hql.append("from LcDepositDelay where depositId = :depositId order by expiryDate desc");
        return listByHQLAliasToBean(hql.toString(), params, LcDepositDelay.class);
    }
    /**
     * 通过信用证ID获取该信用证下所有还押汇金额
     * @param params map参数
     * @return map
     */
    public Map getAllDepositRepayMoneyByLcId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select BT_nvl(sum(repay_money),0) as money");
        sql.append("  from LC_DEPOSIT_REPAY");
        sql.append(" where DEPOSIT_ID in (select id from LC_DEPOSIT where lc_id = :lcId) ");
        return getBySQL(sql.toString(), params);
    }
    /**
     * 进口押汇数据查询
     *
     * @return
     */
    public DataTablesOutput listReportDepositDataTable(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ld.id                                                              id, ");
        sql.append("       ld.deposit_no                                                      depositNo, ");
        sql.append("       lc.lc_inter                                                        lcInter, ");
        sql.append("       lc.credit_no                                                       creditNo, ");
        sql.append("       lb.ARRIVAL_NO                                                      arrivalNo, ");
        sql.append("       lc.contract_no                                                     contractNo, ");
        sql.append("       ld.deposit_money                                                   depositMoney, ");
        sql.append("       ld.cur_id                                                          curId, ");
        sql.append("       ld.cur_name                                                        curName, ");
        sql.append("       ld.deposit_date                                                    depositDate, ");//押汇日期
        sql.append("       lb.payment_date                                                    paymentDate, ");//到单日期
        sql.append("       lb.PAY_DATE                                                        payDate, ");//到单到期日（付汇日期）
        sql.append("       ld.expiry_date                                                     expiryDate, ");//押汇日期
        sql.append("       lb.hand_doc_bank                                                   handDocBank, ");
        sql.append("       lb.beneficiary                                                     beneficiary, ");
        sql.append("       BT_NVL_NUM(repay.repay_money, 0)                                   repayDepositMoney, ");
        sql.append("       BT_NVL_NUM(ld.DEPOSIT_MONEY - repay.repay_money, ld.DEPOSIT_MONEY) leftRepayDepositMoney, ");
        sql.append("       ld.status                                                          status ");
        sql.append("from LC_DEPOSIT ld ");
        sql.append("         left join lc_bill_arrival lb ");
        sql.append("                   on lb.id = ld.arrival_id ");
        sql.append("         left join lc_enrol lc ");
        sql.append("                   on ld.lc_id = lc.id ");
        sql.append("         left join (select DEPOSIT_ID, sum(REPAY_MONEY) repay_money from LC_DEPOSIT_REPAY group by DEPOSIT_ID) repay ");
        sql.append("                   on repay.DEPOSIT_ID = ld.ID ");
        sql.append("where 1 = 1 ");
        OrderString orderString = new OrderString("ld.deposit_no", "desc");
        input.setOrderString(orderString);
        Map<String, Object> params = input.getFormdata();
        if (null != params.get("depositStatus")) {
            if (params.get("depositStatus") instanceof ArrayList) {
                sql.append("  and ld.status in (96,97,99,999)");
            } else {
                if (InternationalConstant.DEPOSIT_STATUS_2.equals(params.get("depositStatus").toString())) {
                    sql.append(" and ld.status = " + InternationalConstant.BUSINESSFINISH);
                } else {
                    sql.append(" and ld.status in (96,97,99) ");
                }
            }
        } else {
            sql.append("  and ld.status in (96,97,99,999)");
        }
        return getDynamicBySQL(input, sql.toString(), LcDepositReportVO.class, "ld", false, null);
    }
}
