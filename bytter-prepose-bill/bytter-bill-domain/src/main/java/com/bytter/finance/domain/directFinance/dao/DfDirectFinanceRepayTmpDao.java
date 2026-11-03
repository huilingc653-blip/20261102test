package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepayTmp;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayTmpVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-12 15:04
 * @Version 1.0
 */
@Repository
public class DfDirectFinanceRepayTmpDao extends BaseDao<DfDirectFinanceRepayTmp, String> {
    /**
     * 通过主表删除还款计划数据
     *
     * @param params
     */
    public void deleteDirectFinanceTmp(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from DF_DIRECT_FINANCE_REPAY_TMP where DIRECT_FINANCE_ID = :id ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 获取还本付息计划
     *
     * @param params
     * @return
     */
    public List<DfDirectFinanceRepayTmpVO> listDirectFinanceRepayTmpByDfId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" REPAY_DATE repayDate, ");
        sql.append(" BT_NVL_NUM(REPAY_MONEY,0) repayMoney, ");
        sql.append(" BT_NVL_NUM(INTEREST,0) interest, ");
        sql.append(" BT_NVL_NUM(SERVICE_CHARGE,0) serviceCharge ");
        sql.append(" from  DF_DIRECT_FINANCE_REPAY_TMP");
        sql.append(" where DIRECT_FINANCE_ID = :directFinanceId order by REPAY_DATE ");
        return listBySQLAliasToBean(sql.toString(), params, DfDirectFinanceRepayTmpVO.class);
    }
    /**
     * 查询到期天数以内的数据
     *
     * @param params
     * @return
     */
    public List<DfDirectFinanceRepayTmp> listDirectFinanceRepayTmpNotice(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder();
        hql.append("  from  DfDirectFinanceRepayTmp");
        hql.append(" where round(repayDate - sysdate) <= to_number(:expiryDays) ");
        hql.append("   and round(repayDate - sysdate) >= 0 ");
        return listByHQL(hql.toString(), params);
    }
}
