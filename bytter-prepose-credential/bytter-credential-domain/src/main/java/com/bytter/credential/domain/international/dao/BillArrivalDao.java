package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.vo.ArrivalAndEnrolVO;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-17 19:01
 * @Version 1.0
 */
@Repository
public class BillArrivalDao extends BaseDao<LcBillArrival, String> {
    /**
     * 列表显示
     * @param dataTablesInput
     * @param corpIds
     * @return DataTablesOutput
     */
    public DataTablesOutput listStorageCredit(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        /*开证表*/
//        sql.append("select * from (");
        sql.append("select  lc.id id, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.lc_no lcNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.issuing_date issuingDate, ");
        sql.append("        lc.expiry_date expiryDate, ");
        sql.append("        lc.credit_money creditMoney, ");
        sql.append("        lc.issuing_cur_id issuingCurId, ");
        sql.append("        lc.status status,");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.bank_name issuingBankName,");
        sql.append("        bt_nvl_num((select sum(bill_money) from lc_bill_arrival where lc_id = lc.id and status > 0),0)  hadbillMoney,");
        sql.append("        bt_nvl_num(lc.credit_money - (select sum(bill_money) from lc_bill_arrival where lc_id = lc.id and status > 0),0) hadNotbillMoney,");
        sql.append("        bt_nvl_num((select sum(pay_money) from lc_bill_arrival where lc_id = lc.id and status > 0),0) sumPaymentMoney");
        sql.append(" from LC_ENROL lc ");
        sql.append(" where 1 = 1 and lc.status = 105 and lc.lc_type = '0' and ");
        sql.append(getSQLIn(corpIds, 900, "lc.corp_id"));
        // 已到单金额/未到单金额/付汇金额为计算列，VO中无对应表字段，普通排序拼接不生效，需通过OrderString按完整表达式排序
        if (dataTablesInput.getOrder() != null && !dataTablesInput.getOrder().isEmpty()
                && dataTablesInput.getOrder().get(0) != null) {
            Integer orderColumnIndex = dataTablesInput.getOrder().get(0).getColumn();
            List<Column> columns = dataTablesInput.getColumns();
            if (orderColumnIndex != null && columns != null && orderColumnIndex >= 0
                    && orderColumnIndex < columns.size()) {
                String orderColumnName = columns.get(orderColumnIndex).getName();
                String orderDir = dataTablesInput.getOrder().get(0).getDir();
                if ("hadbillMoney".equals(orderColumnName)) {
                    dataTablesInput.setOrderString(new OrderString(
                            "(BT_NVL_NUM((SELECT SUM(BILL_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))", orderDir));
                } else if ("hadNotbillMoney".equals(orderColumnName)) {
                    dataTablesInput.setOrderString(new OrderString(
                            "(BT_NVL_NUM(LC.CREDIT_MONEY - (SELECT SUM(BILL_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))", orderDir));
                } else if ("sumPaymentMoney".equals(orderColumnName)) {
                    dataTablesInput.setOrderString(new OrderString(
                            "(BT_NVL_NUM((SELECT SUM(PAY_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))", orderDir));
                }
            }
        }
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), ArrivalAndEnrolVO.class, "lc", false, null, null);
    }
    /**
     * 到单登记修改查询
     * @param dataTablesInput
     * @param corpIds
     * @return DataTablesOutput
     */
    public DataTablesOutput listArrivalUpdate(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select  lb.id id, ");
        sql.append("        lb.arrival_code arrivalCode, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lb.arrival_no arrivalNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lb.payment_date paymentDate, ");
        sql.append("        lb.bill_money billMoney, ");
        sql.append("        lb.pay_money payMoney, ");
        sql.append("        lc.cur_name curName, ");
        sql.append("        lc.bank_name issuingBankName,");
        sql.append("        lc.beneficiary beneficiary, ");
        sql.append("        lb.close_lc_no closeLcNo, ");
        sql.append("        lb.status status");
        sql.append(" from lc_bill_arrival lb ");
        sql.append("left join LC_ENROL lc ");
        sql.append(" on lb.lc_id = lc.id ");
        sql.append(" where 1 = 1 and lb.status = 0 and lb.arrival_type = 0 and ");
        sql.append(getSQLIn(corpIds, 900, "lb.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcBillArrivalVO.class, "lb", false, null, null);
    }
    /**
     * 计算已到单次数和已到单金额
     * @return list
     */
    public List getArrivalCountAndSumbillMoney(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) BILLARRIVALCOUNT, ");
        sql.append("bt_nvl_num(sum(lb.bill_money), 0) SUMBILLMONEY ");
        sql.append("  from LC_BILL_ARRIVAL lb ");
        sql.append("  left join lc_Enrol lc ");
        sql.append("    on lb.lc_id = lc.id ");
        sql.append(" where lb.lc_id = :lcId ");
        sql.append(" and lb.status >= 1  ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setProperties(params);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }
    /**
     * 到单报表查询
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listReportBillArrival(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select");
        sql.append("       lb.id id, ");
        sql.append("       lb.arrival_code arrivalCode, ");
        sql.append("       lc.lc_inter lcInter, ");
        sql.append("       lc.credit_no creditNo, ");
        sql.append("       sc.name applicant, ");
        sql.append("       lb.beneficiary beneficiary, ");
        sql.append("       lc.contract_no contractNo, ");
        sql.append("       lb.cur_id curId, ");
        sql.append("       lb.cur_name curName, ");
        sql.append("       lc.credit_money creditMoney, ");
        sql.append("       lb.create_date createDate, ");
        sql.append("       lb.delay_dates delayDates, ");
        sql.append("       lb.bill_money billMoney, ");
        sql.append("       lb.pay_money payMoney, ");
        sql.append("       lb.payment_date paymentDate, ");
        sql.append("       lb.pay_date payDate, ");
        sql.append("       bt_nvl_num(ld.deposit_money, 0) depositMoney, ");
        sql.append("       ld.deposit_date depositDate, ");
        sql.append("       ld.expiry_date expiryDate, ");
        sql.append("       bt_nvl_num(ld.reapy_money, 0) repayMoney, ");
        sql.append("       lb.hand_doc_bank handDocBank, ");
        sql.append("       lb.status status, ");
        sql.append("       case ");
        sql.append("       when lb.status = 2 then lp.status ");
        sql.append("       when lb.status = 3 then ld.status ");
        sql.append("       else -100 ");
        sql.append("       end as status2, ");
        sql.append("       lb.rmk rmk");
        sql.append("  from lc_bill_arrival lb ");
        sql.append("  left join lc_enrol lc ");
        sql.append("    on lb.lc_id = lc.id ");
        sql.append("  left join lc_deposit ld ");
        sql.append("    on lb.id = ld.arrival_id ");
        sql.append("  left join lc_payment lp        ");
        sql.append("    on lb.id = lp.arrival_id   ");
        sql.append(" left join sys_corp sc on lc.applicant = sc.id ");
        sql.append(" where 1 = 1");
        sql.append(" and lb.arrival_type = 0 and ");
        sql.append(getSQLIn(corpIds, 900, "lb.corp_id"));

        if (dataTablesInput.getOrder() != null && !dataTablesInput.getOrder().isEmpty()
                && dataTablesInput.getOrder().get(0) != null) {
            Integer orderColumnIndex = dataTablesInput.getOrder().get(0).getColumn();
            List<Column> columns = dataTablesInput.getColumns();
            if (orderColumnIndex != null && columns != null && orderColumnIndex >= 0
                    && orderColumnIndex < columns.size()) {
                String orderColumnName = columns.get(orderColumnIndex).getName();
                String orderDir = dataTablesInput.getOrder().get(0).getDir();
                if ("depositMoney".equals(orderColumnName)) {
                    dataTablesInput.setOrderString(new OrderString(
                            "(bt_nvl_num(ld.deposit_money, 0))", orderDir));
                }else if("repayMoney".equals(orderColumnName)){
                    dataTablesInput.setOrderString(new OrderString(
                            "(bt_nvl_num(ld.reapy_money, 0))", orderDir));
                }
            }
        }

        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcBillArrivalVO.class, "lb", false, null, null);
    }
}
