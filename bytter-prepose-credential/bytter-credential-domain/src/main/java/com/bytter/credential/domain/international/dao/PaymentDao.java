package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.credential.domain.international.vo.LcPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-19 9:39
 * @Version 1.0
 */
@Repository
public class PaymentDao extends BaseDao<LcPayment, String> {
    /**
     *  付汇申请列表
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
        sql.append("       lb.cur_id curId, ");
        sql.append("       lb.cur_name curName, ");
        sql.append("       lb.create_date createDate, ");
        sql.append("       lb.payment_date paymentDate, ");
        sql.append("       lb.pay_date payDate, ");
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
     * 付汇修改列表
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listPaymentUpdate(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("       lp.id id, ");
        sql.append("       lp.payment_no paymentNo, ");
        sql.append("       lc.lc_inter lcInter, ");
        sql.append("       lc.credit_no creditNo, ");
        sql.append("       lc.contract_no contractNo, ");
        sql.append("       lba.arrival_no arrivalNo, ");
        sql.append("       lp.payment_money paymentMoney, ");
        sql.append("       lp.cur_id curId, ");
        sql.append("       lp.cur_name curName, ");
        sql.append("       lp.create_date createDate, ");
        sql.append("       lp.payment_date paymentDate, ");
        sql.append("       lba.payment_date billArrivalDate, ");
        sql.append("       lp.status status, ");
        sql.append("       lc.beneficiary beneficiary, ");
        sql.append("       lba.hand_doc_bank handDocBank ");
        sql.append("  from LC_PAYMENT lp ");
        sql.append("  left join lc_enrol lc ");
        sql.append("  on lp.lc_id = lc.id ");
        sql.append("  left join LC_BILL_ARRIVAL lba ");
        sql.append("  on lp.ARRIVAL_ID = lba.id ");
        sql.append("  where 1=1 and (lp.status = -1 or lp.status = 0) and ");
        sql.append(getSQLIn(corpIds, 900, "lp.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcPaymentVO.class, "lp", false, null, null);
    }
    /**
     * 付汇审批中数据查询
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listPaymentApprove(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("       lp.id id, ");
        sql.append("       lp.payment_no paymentNo, ");
        sql.append("       lc.lc_inter lcInter, ");
        sql.append("       lc.credit_no creditNo, ");
        sql.append("       lc.contract_no contractNo, ");
        sql.append("       lba.arrival_no arrivalNo, ");
        sql.append("       lp.payment_money paymentMoney, ");
        sql.append("       lp.cur_id curId, ");
        sql.append("       lp.cur_name curName, ");
        sql.append("       lp.create_date createDate, ");
        sql.append("       lp.payment_date paymentDate, ");
        sql.append("       lba.payment_date billArrivalDate, ");
        sql.append("       lp.status status, ");
        sql.append("       lc.beneficiary beneficiary, ");
        sql.append("       lba.hand_doc_bank handDocBank ");
        sql.append("  from LC_PAYMENT lp ");
        sql.append("  left join lc_enrol lc ");
        sql.append("  on lp.lc_id = lc.id ");
        sql.append("  left join LC_BILL_ARRIVAL lba ");
        sql.append("  on lp.ARRIVAL_ID = lba.id ");
        sql.append("  where 1=1 and (lp.status >=11 and lp.status < 95)");
        sql.append(" and ").append(getSQLIn(corpIds, 900, "lp.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcPaymentVO.class, "lp", false, null, null);
    }
    /**
     * 已押汇付汇金额
     * @param params map参数
     * @return DataTablesOutput
     */
    public List getSumDepositedAndPaymented(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if ("1".equals(params.get("flag").toString())) {
            sql.append("select nvl(sum(t.deposit_money),0) totalMoney");
            sql.append("  from lc_deposit t");
        } else if ("2".equals(params.get("flag").toString())) {
            sql.append("select nvl(sum(t.payment_money),0) totalMoney");
            sql.append("  from lc_payment t");
        }
            sql.append(" where t.status >= 95");
            sql.append("   and t.arrival_id = :arrivalId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setProperties(params);
        return query.list();
    }
    /**
     * 押汇付汇在途金额
     * @param params map参数
     * @return list
     */
    public List getSumDepositingAndPaymenting(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        if ("1".equals(params.get("flag").toString())) {
            sql.append("select nvl(sum(t.deposit_money),0) totalMoney");
            sql.append("  from lc_deposit t");
        } else if ("2".equals(params.get("flag").toString())) {
            sql.append("select nvl(sum(t.payment_money),0) totalMoney");
            sql.append("  from lc_payment t");
        }
            sql.append(" where t.status < 95 and t.status >= 0");
            sql.append("   and t.arrival_id = :arrivalId");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setProperties(params);
        return query.list();
    }
}
