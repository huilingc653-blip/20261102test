package com.bytter.credential.domain.international.dao;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.domain.international.entity.LcDepositDelay;
import com.bytter.credential.domain.international.vo.LcDepositDelayVO;
import com.bytter.credential.domain.international.vo.LcDepositVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-25 11:36
 * @Version 1.0
 */
@Repository
public class LcDepositDelayDao extends BaseDao<LcDepositDelay, String> {
    /**
     * 展期申请列表查询
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listlcDepositDelayApply(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder(1024);
        sql.append(" SELECT ld.id id, ");
        sql.append("        ld.deposit_no depositNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");
        sql.append("        ld.EXPIRY_DATE expiryDate, ");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.cur_id curId, ");
        sql.append("        ld.create_date createDate, ");
        sql.append("        lb.payment_date paymentDate, ");
        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ld.status status ");
        sql.append(" FROM LC_DEPOSIT ld ");
        sql.append(" LEFT JOIN lc_enrol lc ON ld.lc_id = lc.id ");
        sql.append(" LEFT JOIN lc_bill_arrival lb ON ld.arrival_id = lb.id ");
        sql.append(" WHERE ( (ld.status = 96 ) OR (ld.status = 99 AND lb.STATUS = 3) ) ");
        sql.append(" AND NOT EXISTS ( ");
        sql.append("    SELECT 1 FROM lc_deposit_delay ldd ");
        sql.append("    WHERE ldd.deposit_id = ld.id AND ldd.status IN (-1, 0, 11) ");
        sql.append(" )");
        String corpIdSql = getSQLIn(corpIds, 900, "ld.corp_id");
        if (StringUtils.isNotBlank(corpIdSql)) {
            sql.append(" AND ").append(corpIdSql);
        }
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositVO.class, "ld", false, null, null);
    }
    /**
     * 展期修改查询
     * @param dataTablesInput
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listlcDepositDelay(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ldd.id id, ");
        sql.append("        ldd.delay_no delayNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");
        sql.append("        ldd.delay_date delayDate, ");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.cur_id curId,");
        sql.append("        ldd.create_date createDate, ");
        sql.append("        lb.payment_date paymentDate, ");
//        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ldd.status status ");
        sql.append("   from LC_DEPOSIT_DELAY ldd ");
        sql.append("   left join LC_DEPOSIT ld ");
        sql.append("     on ldd.deposit_id = ld.id ");
        sql.append("   left join lc_bill_arrival lb ");
        sql.append("     on lb.id = ld.arrival_id ");
        sql.append("   left join lc_enrol lc ");
        sql.append("     on ld.lc_id = lc.id ");
        sql.append("  where 1 = 1 ");
        sql.append("  and (ldd.status = 0 or ldd.status = -1)");
        sql.append(" and "+getSQLIn(corpIds, 900, "ldd.corp_id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositDelayVO.class, "ldd", false, null, null);
    }
    /**
     * 展期审批数据查询
     * @param dataTablesInput
     * @param ids 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput listlcDepositDelayApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ldd.id id, ");
        sql.append("        ldd.delay_no delayNo, ");
        sql.append("        lc.lc_inter lcInter, ");
        sql.append("        lc.contract_no contractNo, ");
        sql.append("        lc.credit_no creditNo, ");
        sql.append("        lb.ARRIVAL_NO arrivalNo, ");
        sql.append("        ld.deposit_money depositMoney, ");
        sql.append("        ld.deposit_date depositDate, ");
        sql.append("        ld.EXPIRY_DATE expiryDate, ");
        sql.append("        ld.cur_name curName, ");
        sql.append("        ld.cur_id curId,");
        sql.append("        ld.create_date createDate, ");
        sql.append("        lb.payment_date paymentDate, ");
        sql.append("        lb.hand_doc_bank handDocBank, ");
        sql.append("        lb.beneficiary beneficiary, ");
        sql.append("        ldd.status status ");
        sql.append("   from LC_DEPOSIT_DELAY ldd ");
        sql.append("   left join LC_DEPOSIT ld ");
        sql.append("     on ldd.deposit_id = ld.id ");
        sql.append("   left join lc_bill_arrival lb ");
        sql.append("     on lb.id = ld.arrival_id ");
        sql.append("   left join lc_enrol lc ");
        sql.append("     on ld.lc_id = lc.id ");
        sql.append("  where 1 = 1 ");
        sql.append("  and ldd.status >= 11 and ldd.status < 99 ");
        sql.append(" and " + getSQLIn(ids, 900, "ldd.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LcDepositDelayVO.class, "ldd", false, null, null);
    }
    /**
     * 获取该押汇id的最大展期日期
     * @param params map参数
     * @return map
     */
    public Map getMaxDate(Map<String,Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(expiry_date) maxDate from LC_DEPOSIT_DELAY ");
        sql.append(" where ");
        sql.append(" 1 = 1 ");
        sql.append(" and status >= 11 ");
        sql.append(" and deposit_id = :depositId ");
        return getBySQL(sql.toString(), params);
    }
}
