package com.bytter.credential.domain.international.dao;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.vo.LcPaymentCostVO;
import com.bytter.credential.domain.international.vo.LcPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-20 11:12
 * @Version 1.0
 */
@Repository
public class SupplementPaymentDao extends BaseDao<LcPayment, String> {
    /**
     * 付汇审批完成数据查询
     * @param dataTablesInput
     * @param corpIds
     * @return DataTablesOutput
     */
    public DataTablesOutput listPaymentApproved(DataTablesInput dataTablesInput, List<String> corpIds) {
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
        sql.append("  where 1=1 and lp.status >= 95 and lp.status < 999 and ");
        sql.append(getSQLIn(corpIds, 900, "lp.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcPaymentVO.class, "lp", false, null, null);
    }
    /**
     * 将对应数据全部删除
     * @param paymentId
     */
    public void deletePaymentCosts(String paymentId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete ");
        sql.append(" from ");
        sql.append(" LC_PAYMENT_COST ");
        sql.append(" where ");
        sql.append(" payment_id = :paymentId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setString("paymentId", paymentId);
        query.executeUpdate();
    }
    /**
     * 获取费用纪录表
     * @param params
     * @return List<Object>
     */
    public List<Object> listPaymentCost(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" id id,");
        sql.append(" fee_type feeType,");
        sql.append(" fee_money feeMoney");
        sql.append(" from LC_PAYMENT_COST");
        sql.append(" where 1=1");
        sql.append(" and payment_id = :paymentId ");
        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setProperties(params);
        query.addScalar("id");
        query.addScalar("feeType");
        query.addScalar("feeMoney");
        query.setResultTransformer(Transformers.aliasToBean(LcPaymentCostVO.class));
        return query.list();
    }
    /**
     * 通过信用证ID获取该信用证下所有付汇汇金额
     * @param params
     * @return Map
     */
    public Map getAllPaymentMoneyByLcId(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        sql.append("select Bt_nvl(sum(payment_money),0) as money from LC_PAYMENT  where lc_id = :lcId and status = :status");
        return getBySQL(sql.toString(), params);
    }
}
