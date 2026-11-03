package com.bytter.report.domain.financial.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.financial.vo.GeneralLedgerDetailVO;
import com.bytter.report.domain.financial.vo.GeneralLedgerSearchVO;
import com.bytter.report.domain.financial.vo.GeneralLedgerVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 总账查询Dao
 *
 * @author zuoy 2020-11-19
 */
@Component
public class GeneralLedgerDao extends BaseDao<GeneralLedgerVO, String> {
    /**
     * 总账查询列表页面
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.net_id, t.cur_id from fc_subject_day t ");
        sql.append(" where 1 = 1");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "t.net_id", true);
        sql.append(" and ").append(corpAuthSql);
        Map<String, Object> params = Maps.newHashMap();
        // 单位
        String netIds = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("corpId"))
                ? "" : String.valueOf(dataTablesInput.getFormdata().get("corpId"));
        if (StringUtils.hasText(netIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(netIds.split(","), "t.net_id", true));
        }
        // 币别
        String curId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId"))
                ? "" : String.valueOf(dataTablesInput.getFormdata().get("curId"));
        if (StringUtils.hasText(curId)) {
            sql.append(" and t.cur_id = :curId ");
            params.put("curId", curId);
        }
        // 科目
        String subjectId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("subjectId"))
                ? "" : String.valueOf(dataTablesInput.getFormdata().get("subjectId"));
        if (StringUtils.hasText(subjectId)) {
            sql.append(" and ( t.subject_Id = :subjectId ");
            sql.append(" or t.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId )");
            sql.append(" )");
            params.put("subjectId", subjectId);
        }
        // 日期
        String searchBeginDate = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("beginDate"))
                ? "" : String.valueOf(dataTablesInput.getFormdata().get("beginDate"));
        String searchEndDate = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("endDate"))
                ? "" : String.valueOf(dataTablesInput.getFormdata().get("endDate"));
        sql.append(" and t.opt_date >= :beginDate and t.opt_date <= :endDate ");
        // 开始时间当月第一天
        Date beginDate = DateUtils.getFirstMonthDate(DateUtils.parseDate(searchBeginDate + "-01"));
        // 结束时间当月最后一天
        Date endDate = DateUtils.getLastMonthDate(DateUtils.parseDate(searchEndDate + "-01"));
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        sql.append("group by t.net_id, t.cur_id");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, GeneralLedgerVO.class);
    }
    public GeneralLedgerDetailVO getLastYearLedgerDetail(GeneralLedgerSearchVO searchVO) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.net_id, to_char(t.opt_date, 'yyyy-MM') ledgerDate, ");
        sql.append(" bt_nvl_num(sum(t.today_balance), 0) balance  from fc_subject_day t where 1=1 ");
        Map<String, Object> params = Maps.newHashMap();
        // 单位
        sql.append(" and t.net_id = :netId ");
        params.put("netId", searchVO.getNetId());
        // 科目
        sql.append(" and ( t.subject_Id = :subjectId ");
        sql.append(" or t.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId )) ");
        params.put("subjectId", searchVO.getSubjectId());
        // 币别
        sql.append(" and t.cur_id = :curId ");
        params.put("curId", searchVO.getCurId());
        // 开始时间当月第一天
        Date beginDate = DateUtils.parseDate(searchVO.getBeginDate() + "-01");
        // 本年第一天减一为上年12月
        Date lastYearDate = DateUtils.addMonth(DateUtils.getFirstYearDate(beginDate), -1);
        sql.append(" and t.opt_date >= :beginDate and t.opt_date <= :endDate ");
        // 12月第一天
        params.put("beginDate", DateUtils.getFirstMonthDate(lastYearDate));
        // 12月最后一天
        params.put("endDate", DateUtils.getLastMonthDate(lastYearDate));
        // 取每月最近一天的数据
        sql.append(" and  t.opt_date = (select max(t1.opt_date) from fc_subject_day t1 where t1.net_id = t.net_id ");
        sql.append(" and ( t1.subject_Id = :subjectId ");
        sql.append("    or t1.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId)) ");
        sql.append(" and to_char(t.opt_date, 'yyyy-MM') = to_char(t1.opt_date, 'yyyy-MM')) ");
        // 分组
        sql.append(" group by t.net_id, to_char(t.opt_date, 'yyyy-MM') ");
        return this.getBySQLAliasToBean(sql.toString(), params, GeneralLedgerDetailVO.class);
    }
    public List<GeneralLedgerDetailVO> listLastMonthLedgerDetail(GeneralLedgerSearchVO searchVO, Date startMonthDate, Date endMonthDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.net_id, to_char(t.opt_date, 'yyyy-MM') ledgerDate, ");
        sql.append(" bt_nvl_num(sum(t.tr_debit_money+ t.cash_debit_money), 0) debitMoney, ");
        sql.append(" bt_nvl_num(sum(t.tr_credit_money + t.cash_credit_money), 0) creditMoney ");
        sql.append(" from fc_subject_day t where 1=1 ");
        Map<String, Object> params = Maps.newHashMap();
        // 单位
        sql.append(" and t.net_id = :netId ");
        params.put("netId", searchVO.getNetId());
        // 科目
        sql.append(" and ( t.subject_Id = :subjectId ");
        sql.append(" or t.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId )) ");
        params.put("subjectId", searchVO.getSubjectId());
        // 币别
        sql.append(" and t.cur_id = :curId ");
        params.put("curId", searchVO.getCurId());
        sql.append(" and t.opt_date >= :beginDate and t.opt_date <= :endDate ");
        params.put("beginDate", DateUtils.getFirstMonthDate(startMonthDate));
        params.put("endDate", DateUtils.getLastMonthDate(endMonthDate));
        // 分组
        sql.append(" group by t.net_id, to_char(t.opt_date, 'yyyy-MM') ");
        return this.listBySQLAliasToBean(sql.toString(), params, GeneralLedgerDetailVO.class);
    }
    /**
     * 查询本月合计明细数据
     *
     * @param searchVO
     * @return
     */
    public List<GeneralLedgerDetailVO> getGeneralLedgerDetailList(GeneralLedgerSearchVO searchVO) {
        StringBuffer sql = new StringBuffer();
        sql.append("select t.net_id, to_char(t.opt_date, 'yyyy-MM') ledgerDate, ");
        sql.append(" bt_nvl_num(sum(t.tr_debit_money+ t.cash_debit_money), 0) debitMoney, ");
        sql.append(" bt_nvl_num(sum(t.tr_credit_money + t.cash_credit_money), 0) creditMoney, ");
        sql.append(" bt_nvl_num(sum(t.today_balance), 0) balance  from fc_subject_day t ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = Maps.newHashMap();
        // 单位
        sql.append(" and t.net_id = :netId ");
        params.put("netId", searchVO.getNetId());
        // 科目
        sql.append(" and ( t.subject_Id = :subjectId ");
        sql.append(" or t.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId )) ");
        params.put("subjectId", searchVO.getSubjectId());
        // 币别
        sql.append(" and t.cur_id = :curId ");
        params.put("curId", searchVO.getCurId());
        // 开始时间当月第一天
        Date beginDate = DateUtils.getFirstMonthDate(DateUtils.parseDate(searchVO.getBeginDate() + "-01"));
        // 结束时间当月最后一天
        Date endDate = DateUtils.getLastMonthDate(DateUtils.parseDate(searchVO.getEndDate() + "-01"));
        sql.append(" and t.opt_date >= :beginDate and t.opt_date <= :endDate ");
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        // 分组
        sql.append(" group by t.net_id, to_char(t.opt_date, 'yyyy-MM') ");
        // 排序
        sql.append("  order by to_char(t.opt_date, 'yyyy-MM') ");
        return this.listBySQLAliasToBean(sql.toString(), params, GeneralLedgerDetailVO.class);
    }
    /**
     * 获取内部账户期初余额
     * @param searchVO
     * @return
     */
    public BigDecimal getInitBalance(GeneralLedgerSearchVO searchVO) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(a.balance) as initBalance from fc_init_acc a left join fc_acc c on a.account = c.blnctr_acc");
        sql.append(" where a.status = :status");
        Map<String, Object> params = Maps.newHashMap();
        // 状态
        params.put("status", CommonConstant.STATUE_CHECK);
        // 单位
        sql.append(" and c.net_id = :netId ");
        params.put("netId", searchVO.getNetId());
        // 科目
        sql.append(" and ( c.subject_Id = :subjectId ");
        sql.append(" or c.subject_id in (select s.id from fc_subject s where s.father_id = :subjectId )) ");
        params.put("subjectId", searchVO.getSubjectId());
        // 币别
        sql.append(" and c.cur_id = :curId ");
        params.put("curId", searchVO.getCurId());
        // 开始时间当月第一天
        Date beginDate = DateUtils.getFirstMonthDate(DateUtils.parseDate(searchVO.getBeginDate() + "-01"));
        // 结束时间当月最后一天
        Date endDate = DateUtils.getLastMonthDate(DateUtils.parseDate(searchVO.getEndDate() + "-01"));
        sql.append(" and a.init_time >= :beginDate and a.init_time <= :endDate ");
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal initBalance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(initBalance)) {
            initBalance = (BigDecimal) result;
        }
        return initBalance;
    }
}
