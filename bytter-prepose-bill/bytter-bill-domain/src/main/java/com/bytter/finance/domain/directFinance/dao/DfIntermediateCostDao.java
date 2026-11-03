package com.bytter.finance.domain.directFinance.dao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-12 14:43
 * @Version 1.0
 */
@Repository
public class DfIntermediateCostDao extends BaseDao<DfIntermediateCost, String> {
    /**
     * 通过主表删除中间费用数据
     * 
     * @param params
     */
    public void deleteIntermediateCost(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("delete from df_intermediate_cost where DIRECT_FINANCE_ID = :id ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 获取中间费用
     * 
     * @param params
     * @return
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" COST_TYPE_ID costTypeId, ");
        sql.append(" cost_name costName, ");
        sql.append(" RATE rate, ");
        sql.append(" ADVANCE_FEES advanceFees ");
        sql.append(" from  DF_INTERMEDIATE_COST ic");
        sql.append(" left join DF_COST_TYPE ct on ic.COST_TYPE_ID = ct.id");
        sql.append(" where DIRECT_FINANCE_ID = :directFinanceId ");
        return listBySQLAliasToBean(sql.toString(), params, DfIntermediateCostVO.class);
    }
    /**
     * 数据保存
     * 
     * @param intermediateCost
     */
    public void saveIntermediateCost(DfIntermediateCost intermediateCost) {
        super.save(intermediateCost);
    }
}
