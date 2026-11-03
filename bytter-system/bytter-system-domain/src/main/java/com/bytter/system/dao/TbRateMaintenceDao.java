package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.InstRateBeanDto;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import com.bytter.system.constants.BaseDataConstant;
import com.bytter.system.entity.TbLoanRateMaintence;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 贷款利率维护
 * @date 2017-07-26 18:38:18
 */
@Repository
public class TbRateMaintenceDao extends BaseDao<TbLoanRateMaintence, String> {
    /**
     * 功能介绍 : 查询sql<br>
     *
     * @param sql <b>日期：</b>2017-7-29<br>
     *            <br>
     * @version 1.0
     * @author lvwl
     */
    private void querySql(StringBuilder sql) {
        sql.append(
                " select mai.id,mai.base_rate_id as baseRateId,mai.credit_period_id as creditPeriodId,base.code baseRateCode,base.base_type_name as baseRateName,per.period_name as creditPeriodName,mai.time_of_take_effect as timeOfTakeEffect, ");
        sql.append(
                " per.loan_class as loanClass,per.period as period,mai.rate_from_way as rateFromWay,per.period_cycle as periodCycle,mai.rate as rate, base.code as code, ");
        sql.append(
                " mai.rate_up_limit as rateUpLimit, mai.rate_down_limit as rateDownLimit,mai.time_of_last_modifier as timeOfLastModifier, ");
        sql.append(
                " mai.status as status,mai.is_valid as isValid,mai.money_type as moneyType,cur.cur_name as moneyTypeName,mai.borrow_type as borrowType,mai.is_base_loan_rate as isbaseloanrate ");
        sql.append(" from tb_loan_rate_maintence mai ");
        sql.append(" inner join tb_base_rate base on base.id = mai.base_rate_id ");
        sql.append(" inner join tb_credit_period per on per.id = mai.credit_period_id ");
    }

    /**
     * 功能介绍 : 查询 - 分页<br>
     */
    //TODO 用到
    public DataTablesOutput<LoanRateMaintenceVO> listLoanRateMaintence(DataTablesInput dataTablesInput, String loginName) {
        String lo = loginName;
        StringBuilder sql = new StringBuilder();
        this.querySql(sql);
        sql.append(" inner join bt_currency cur on cur.id = mai.money_type ");
        sql.append(" where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRateMaintenceVO.class, "", false);
    }

    /**
     * 功能介绍 : 根据基准利率查询是否关联贷款利率维护<br>
     *
     * @param baseRateId 基准利率ID
     * @return 贷款利率维护关联基准利率：true,否则：false
     */
    //TODO 用到
    public boolean checkBaseRateById(String baseRateId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "FROM TbLoanRateMaintence TB WHERE TB.baseRateId = :baseRateId";
        params.put("baseRateId", baseRateId);
        List list = this.createQuery(hql, params).list();
        return !EmptyUtils.isEmptyList(list);
    }

    /**
     * 功能介绍 : 根绝贷款期限Id与生效日期查询是否存在重复<br>
     *
     * @return 重复：true ,否则false
     */
    public boolean checkRepeatByPeriodIdAndTime(String periodId, Date time, String moneyType, String baseRateId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "FROM TbLoanRateMaintence TB WHERE TB.creditPeriodId = :creditPeriodId and TB.timeOfTakeEffect = :timeOfTakeEffect and TB.moneyType=:moneyType and TB.baseRateId = :baseRateId";
        params.put("creditPeriodId", periodId);
        params.put("timeOfTakeEffect", time);
        params.put("moneyType", moneyType);
        params.put("baseRateId", baseRateId);
        List list = this.createQuery(hql, params).list();
        return !EmptyUtils.isEmptyList(list);
    }

    /**
     * 功能介绍 : <br>
     * 根据Id查询贷款利率维护VO并注入字典
     */
    public LoanRateMaintenceVO getLoanRateById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        this.querySql(sql);
        sql.append(" inner join bt_currency cur on cur.id = mai.money_type ");
        sql.append(" where 1=1 ");
        sql.append(" and mai.id = :id");
        params.put("id", id);
        LoanRateMaintenceVO vo = this.getBySQLAliasToBean(sql.toString(), params, LoanRateMaintenceVO.class);
        //inject(vo);
        // injection.searchInject(vo,LoanRateMaintenceVO.class);//注入字典
        return vo;
    }

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param: time
     * 生效日期
     * @param: cur
     * 币别
     * @param: base
     * 基础利率
     */
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCurBase(Date time, String cur, String base) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        querySql(sql);
        sql.append(" inner join bt_currency cur on cur.id = mai.money_type ");
        sql.append(
                " inner join (select a.base_rate_id,a.credit_period_id,a.money_type,max(time_of_take_effect) as maxTime ");
        sql.append(" from tb_loan_rate_maintence a ");
        sql.append(
                " where a.money_type=:moneyType and a.base_rate_id=:base and a.time_of_take_effect<=BT_TO_DATE(:time, 'YYYY-MM-DD')");
        sql.append(
                " and a.is_valid=:isValid and a.status=:status group by base_rate_id,credit_period_id,money_type) maxTime");
        sql.append(
                " on maxTime.base_rate_id = mai.base_rate_id and maxTime.credit_period_id = mai.credit_period_id and maxTime.money_type = mai.money_type and maxTime.maxTime = mai.time_of_take_effect ");
        sql.append(" where 1=1 ");
        sql.append(" and mai.money_type=:moneyType ");
        sql.append(" and mai.base_rate_id=:base ");
        sql.append(" and mai.is_valid=:isValid and mai.status=:status order by per.code");// 默认条件
        params.put("moneyType", cur);
        params.put("base", base);
        params.put("time", DateUtils.formatDate(time));
        params.put("isValid", BaseDataConstant.IS_VALID_TRUE);
        params.put("status", BaseDataConstant.STATUS_PASS);
        return listBySQLAliasToBean(sql.toString(), params, LoanRateMaintenceVO.class);
    }

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param: time
     * 生效日期
     * @param: cur
     * 币别
     * @param: base
     * 生效日期
     */
    public List<LoanRateMaintenceVO> getAllRateMaintenceByTimeCurBase(Date time, String cur, String base) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " SELECT mai.base_rate_id AS baseRateId,mai.credit_period_id AS creditPeriodId,mai.money_type AS moneyType,mai.time_of_take_effect AS timeOfTakeEffect, ");
        sql.append(" mai.rate AS rate,mai.rate_up_limit AS rateUpLimit,mai.rate_down_limit AS rateDownLimit ");
        sql.append(" FROM tb_loan_rate_maintence mai ");
        sql.append(
                " inner join (select a.base_rate_id,a.credit_period_id,a.money_type,max(time_of_take_effect) as maxTime ");
        sql.append(" from tb_loan_rate_maintence a ");
        sql.append(
                " where a.money_type=:moneyType and a.base_rate_id=:base and a.time_of_take_effect<=BT_TO_DATE(:time, 'YYYY-MM-DD')");
        sql.append(
                " and a.is_valid=:isValid and a.status=:status group by base_rate_id,credit_period_id,money_type) maxTime");
        sql.append(
                " on maxTime.base_rate_id = mai.base_rate_id and maxTime.credit_period_id = mai.credit_period_id and maxTime.money_type = mai.money_type and maxTime.maxTime <= mai.time_of_take_effect ");
        sql.append(" where 1=1 ");
        sql.append(" and mai.money_type=:moneyType ");
        sql.append(" and mai.base_rate_id=:base ");
        sql.append(" and mai.is_valid=:isValid and mai.status=:status");// 默认条件
        params.put("moneyType", cur);
        params.put("base", base);
        params.put("time", DateUtils.formatDate(time));
        params.put("isValid", BaseDataConstant.IS_VALID_TRUE);
        params.put("status", BaseDataConstant.STATUS_PASS);
        return listBySQLAliasToBean(sql.toString(), params, LoanRateMaintenceVO.class);
    }

    /**
     * 功能介绍 : 根据条件查找合适的维护利率<br>
     *
     * @param curId             币别
     * @param referenceRateType 基准类型
     * @param repayCycle        周期类型
     * @param plDate            日期（借款/贷款开始日期）
     */
    public List<LoanRateMaintenceVO> getRateMaintence(String plDate, String referenceRateType, String curId, String repayCycle) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.rate as rate , a.time_Of_Take_Effect as timeOfTakeEffect,a.rate_up_limit as rateUpLimit,a.rate_down_limit as rateDownLimit,a.is_base_loan_rate as isBaseLoanRate ");
        sb.append(
                "  from TB_Loan_Rate_Maintence a  left join TB_BASE_RATE b on a.base_rate_id = b.id  left join TB_CREDIT_PERIOD c  on a.credit_period_id = c.id ");
        sb.append(
                "  where a.time_Of_Take_Effect =(select max(time_Of_Take_Effect) from TB_Loan_Rate_Maintence where time_Of_Take_Effect <=  BT_TO_DATE(:timeOfTakeEffect, 'YYYY-MM-DD')   and status = '2' and is_Valid = '1'  and base_rate_id =:baseRateId and credit_period_id =:creditPeriodId ) ");
        sb.append("  and b.id =:baseRateId    and c.id =:creditPeriodId  and a.money_type = :moneyType ");
        params.put("baseRateId", referenceRateType);
        params.put("creditPeriodId", repayCycle);
        params.put("timeOfTakeEffect", plDate);
        params.put("moneyType", curId);
        SQLQuery sq = createSQLQuery(sb.toString(), params);
        sq.addScalar("rate", StandardBasicTypes.BIG_DECIMAL);
        sq.addScalar("timeOfTakeEffect", StandardBasicTypes.DATE);
        sq.addScalar("rateUpLimit", StandardBasicTypes.BIG_DECIMAL);
        sq.addScalar("rateDownLimit", StandardBasicTypes.BIG_DECIMAL);
        sq.addScalar("isBaseLoanRate", StandardBasicTypes.STRING);
        sq.setResultTransformer(Transformers.aliasToBean(LoanRateMaintenceVO.class));
        return sq.list();
    }

    /**
     * 查询最大的生效日期
     */
    public Date checkTimeOfTakeEffect(TbRateMaintenceDTO dto) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select max(time_Of_Take_Effect) as TIMEOFTAKEEFFECT  from TB_Loan_Rate_Maintence b where b.status = '2' and b.is_Valid = '1'  ");
        sb.append(
                "  and b.base_rate_id =:baseRateId and b.credit_period_id =:creditPeriodId  and b.money_type = :moneyType ");
        params.put("baseRateId", dto.getBaseRateId());
        params.put("creditPeriodId", dto.getCreditPeriodId());
        params.put("moneyType", dto.getMoneyType());
        SQLQuery sq = createSQLQuery(sb.toString(), params);
        sq.addScalar("TIMEOFTAKEEFFECT", StandardBasicTypes.DATE);
        Map result = (Map) sq.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
        return result == null ? null : (Date) (result.get("TIMEOFTAKEEFFECT"));
    }

    public Date getNextMinDate(TbRateMaintenceDTO dto) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select min(time_Of_Take_Effect) as TIMEOFTAKEEFFECT  from TB_Loan_Rate_Maintence b where b.time_Of_Take_Effect > BT_TO_DATE(:timeOfTakeEffect, 'YYYY-MM-DD')   and b.status = '2' and b.is_Valid = '1'  ");
        sb.append(
                "  and b.base_rate_id =:baseRateId and b.credit_period_id =:creditPeriodId  and b.money_type = :moneyType ");
        params.put("baseRateId", dto.getBaseRateId());
        params.put("creditPeriodId", dto.getCreditPeriodId());
        params.put("timeOfTakeEffect", DateUtils.formatDate(dto.getTimeOfTakeEffect()));
        params.put("moneyType", dto.getMoneyType());
        SQLQuery sq = createSQLQuery(sb.toString(), params);
        sq.addScalar("TIMEOFTAKEEFFECT", StandardBasicTypes.DATE);
        Map result = (Map) sq.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
        return result == null ? null : (Date) (result.get("TIMEOFTAKEEFFECT"));
    }

    /**
     * @param params
     * @return
     */
    public List<TbLoanRateMaintence> listLoanRateMaintence(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("       t.*");
        sql.append("   from TB_LOAN_RATE_MAINTENCE t");
        sql.append(" where 1 = 1");
        sql.append(" and BASE_RATE_ID = :baseRateId");
        sql.append(" and CREDIT_PERIOD_ID = :creditPeriodId");
        sql.append(" and MONEY_TYPE = :curId");
        if (!EmptyUtils.isEmpty(params.get("date"))) {
            sql.append(" and TIME_OF_TAKE_EFFECT=:date");
        }
        return listBySQLAliasToBean(sql.toString(), params, TbLoanRateMaintence.class);
    }

    /**
     * @param params
     * @return
     */
    public List<TbLoanRateMaintence> listRateMaintenceSort(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        sql.append("       t.*");
        sql.append("   from TB_LOAN_RATE_MAINTENCE t");
        sql.append(" where 1 = 1");
        sql.append(" and BASE_RATE_ID = :baseRateId");
        sql.append(" and CREDIT_PERIOD_ID = :creditPeriodId");
        sql.append(" and MONEY_TYPE = :curId");
        sql.append(" order by TIME_OF_TAKE_EFFECT desc");
        return listBySQLAliasToBean(sql.toString(), params, TbLoanRateMaintence.class);
    }

    public List<InstRateBeanDto> getLoanRateMaintence(Set<String> repayCycles) {
        String sql = " SELECT a.rate as newRate,a.time_of_take_effect as effectDate,credit_period_id as period_id,money_type as curCode,base_rate_id as baseRateId FROM TB_LOAN_RATE_MAINTENCE a "
                + " WHERE a.status = 2 and a.is_valid = '1' ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creditPeriodId", repayCycles);
        if (null != repayCycles && !repayCycles.isEmpty()) {
            String hqlSelect = "";
            for (String repayCycle : repayCycles) {
                if (EmptyUtils.isEmpty(repayCycle)) {
                    continue;
                }
                String[] cycleArr = repayCycle.split(",");
                if (null != cycleArr && cycleArr.length >= 3) {
                    hqlSelect += " (a.credit_period_id = '" + cycleArr[0] + "' and a.money_type = '" + cycleArr[1] + "'"
                            + " and a.base_rate_id = '" + cycleArr[2] + "' ) OR";
                }
            }
            if (EmptyUtils.isNotEmpty(hqlSelect)) {
                hqlSelect = hqlSelect.substring(0, hqlSelect.length() - 2);
                sql += " and ( " + hqlSelect + " ) ";
            }
        }
        sql += " ORDER BY a.credit_period_id,a.time_of_take_effect";
        return listBySQLAliasToBean(sql, params, InstRateBeanDto.class);
    }

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCur(Date time, String cur) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct a.base_rate_id as baseRateId, base.base_type_name as baseRateName, base.IS_BANK_BASE_RATE as isBankBaseRate ");
        sql.append(" from tb_loan_rate_maintence a ");
        sql.append(" inner join tb_base_rate base on base.id = a.base_rate_id ");
        // 复核通过并已启用
        sql.append(" where a.is_valid = :isValid and a.status = :status and a.money_type = :moneyType ");
        if (!EmptyUtils.isEmpty(time)) {
            sql.append(" and a.time_of_take_effect <= BT_TO_DATE(:time, 'YYYY-MM-DD')");
            params.put("time", DateUtils.formatDate(time));
        }
        params.put("moneyType", cur);
        params.put("isValid", BaseDataConstant.IS_VALID_TRUE);
        params.put("status", BaseDataConstant.STATUS_PASS);
        return listBySQLAliasToBean(sql.toString(), params, LoanRateMaintenceVO.class);
    }
}
