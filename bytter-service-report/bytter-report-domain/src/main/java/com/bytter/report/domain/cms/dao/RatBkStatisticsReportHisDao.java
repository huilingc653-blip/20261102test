package com.bytter.report.domain.cms.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.report.domain.cms.vo.RatBkStatisticSearchVO;
import com.bytter.report.domain.cms.vo.RatBkStatisticsReportHisVo;
import com.bytter.report.domain.cms.vo.RatUrationVO;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RatBkStatisticsReportHisDao extends BaseDao<RatBkStatisticSearchVO,String> {


    public List<RatBkStatisticsReportHisVo> getRatBkAmountStatisticsListHis(RatBkStatisticSearchVO ratVO, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select rb.id, rb.corp_id as corpId, rb.corp_name as corpName, rb.bank_type as bankType, rb.bank_name as bankName, rb.rat_mode as ratMode,rb.dis_way as disWay,rb.begin_date as beginDate," +
                "rb.end_date as endDate, rb.cur_code as curCode, rb.cur_name as curName, rb.money, rb.use_money as useMoney,rb.approve_money as approveMoney,rb.over_plus_money as overPlusMoney," +
                "rb.left_money as leftMoney,rb.status_str as statusStr,rb.report_date as reportDate,rb.rat_code as ratCode ");
        sql.append(" from RAT_BK_STAT_REPORT_HISTORY rb");
        sql.append(" where 1=1");
        if (EmptyUtils.isNotEmpty(ratVO.getCorpId()) && EmptyUtils.isNotEmpty(ratVO.getCorpId().trim())){
            List<String> corpIds = Arrays.asList(ratVO.getCorpId().split(","));
            sql.append(" and "+getSQLIn(corpIds,900,"rb.corp_id"));
        }else{
            sql.append(" and "+getSQLIn(corpAuth,900,"rb.corp_id"));
        }
        if (ratVO.getBankTypeId() != null && !EmptyUtils.isEmpty(ratVO.getBankTypeId().trim())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(ratVO.getBankTypeId().split(",")), 900, "rb.bank_type") + ") ");
        }
        if (EmptyUtils.isNotEmpty(ratVO.getRatMode())&& EmptyUtils.isNotEmpty(ratVO.getRatMode().trim())){
            sql.append(" and rb.rat_mode ="+ratVO.getRatMode());
        }
        if (EmptyUtils.isNotEmpty(ratVO.getCurCode())&& EmptyUtils.isNotEmpty(ratVO.getCurCode().trim())){
            sql.append(" and rb.cur_code ="+"'"+ratVO.getCurCode()+"'");
        }
//        if (EmptyUtils.isNotEmpty(ratVO.getStatus())&& EmptyUtils.isNotEmpty(ratVO.getStatus().trim())){
//            sql.append(" and rb.status='"+ratVO.getStatus()+"'");
//        }
        if (EmptyUtils.isNotEmpty(ratVO.getReportDate())&& EmptyUtils.isNotEmpty(ratVO.getReportDate().trim())){
            sql.append(" and rb.report_date= to_date('"+ratVO.getReportDate()+ "','yyyy-MM-dd') ");
        }
        if (!EmptyUtils.isEmpty(ratVO.getBeginDate()) && !EmptyUtils.isEmpty(ratVO.getEndDate())) {
            sql.append("  and rb.begin_date between to_date('" + DateUtils.formatDate(ratVO.getBeginDate()) + "','yyyy-MM-dd') ");
            sql.append("  and to_date('" + DateUtils.formatDate(ratVO.getEndDate()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(ratVO.getBeginDate())) {
                sql.append("  and rb.begin_date >= to_date('" + DateUtils.formatDate(ratVO.getBeginDate()) + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(ratVO.getEndDate())) {
                sql.append("  and rb.begin_date <= to_date('" + DateUtils.formatDate(ratVO.getEndDate()) + "','yyyy-MM-dd') ");
            }
        }
        if (!EmptyUtils.isEmpty(ratVO.getBeginDate2()) && !EmptyUtils.isEmpty(ratVO.getEndDate2())) {
            sql.append("  and rb.end_date between to_date('" + DateUtils.formatDate(ratVO.getBeginDate2()) + "','yyyy-MM-dd') ");
            sql.append("  and to_date('" + DateUtils.formatDate(ratVO.getEndDate2()) + "','yyyy-MM-dd') ");
        } else {
            if (!EmptyUtils.isEmpty(ratVO.getBeginDate2())) {
                sql.append("  and rb.end_date >= to_date('" + DateUtils.formatDate(ratVO.getBeginDate2()) + "','yyyy-MM-dd') ");
            }
            if (!EmptyUtils.isEmpty(ratVO.getEndDate2())) {
                sql.append("  and rb.end_date <= to_date('" + DateUtils.formatDate(ratVO.getEndDate2()) + "','yyyy-MM-dd') ");
            }
        }
        sql.append(" order by rb.corp_id ");
        return listBySQLAliasToBean(sql.toString(),new HashMap<>(),RatBkStatisticsReportHisVo.class);
    }

    public void deleteHisRatBkStatisics(Date nowDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("delete from RAT_BK_STAT_REPORT_HISTORY where to_char(report_date,'yyyy-MM-dd')=:date");
        params.put("date", DateUtils.format(nowDate,"yyyy-MM-dd"));
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
