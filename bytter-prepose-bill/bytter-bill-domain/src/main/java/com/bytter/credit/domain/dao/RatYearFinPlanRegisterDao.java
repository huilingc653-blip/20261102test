package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.credit.domain.entity.RatYearFinPlanRegister;
import com.bytter.credit.domain.vo.RatYearFinPlanRegisterVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 年度融资规划登记
 *
 * @author fangcf
 * @date 2024-06-28
 */
@Repository
public class RatYearFinPlanRegisterDao extends BaseDao<RatYearFinPlanRegister, String> {

    /**
     * 查询年度融资规划登记数据
     *
     * @param input        入参
     * @param authCorpList 有资源权限的单位集合
     * @return output 出参
     */
    public DataTablesOutput listOutputData(DataTablesInput input, List<String> authCorpList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id as id, a.CORP_ID as corpId, a.CORP_NAME as corpName, a.YEARS as years, a.YEAR_PURCHASE_AMOUNT as yearPurchaseAmount, a.MERGE_INVESTMENT_AMOUNT as mergeInvestmentAmount, ");
        sql.append(" a.SOLVE_MEASURE as solveMeasure, a.EXIST_DIFFICULTY as existDifficulty, a.STATUS as status, a.CREATE_DATE as createDate ");
        sql.append(" from RAT_YEARFIN_PLAN_REGISTER a where 1 = 1 and a.STATUS <> -2 ");
        sql.append(" and ( ").append(getSQLIn(authCorpList, 900, "a.CORP_ID")).append(") ");
        OrderString order = new OrderString("a.CREATE_DATE", "desc");
        input.setOrderString(order);
        return getDynamicBySQL(input, sql.toString(), RatYearFinPlanRegisterVO.class, null, false);
    }
}
