package com.bytter.report.domain.cms.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.cms.vo.LoanRepayReportVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsLoanRepayReportDao extends BaseDao {
    /**
     * 按单位统计在指定贷款日范围的已放款的贷款
     * @param startDate
     * @param endDate
     * @param corpIds
     * @return
     */
    public HashMap<String, BigDecimal> findLoanByLoanDate(Date startDate, Date endDate, List<String> corpIds){
        StringBuilder sql=new StringBuilder("select s.CORP_ID as CORP_ID,sum(s.loan_money) as TOTALLOANMONEY ");
        sql.append("   from cms_loan_info l left join cms_loan_send s on l.bill_code = s.loan_code ");
        sql.append("   where s.biz_status not in (32,37) and l.status != -2 ");
        Map<String,Object> params= Maps.newHashMap();
        // 注意是贷款日，不是放款日
        if(!EmptyUtils.isEmpty(startDate)) {
            sql.append("   and l.loan_date >=:startDate ");
            params.put("startDate",startDate);
        }
        if(!EmptyUtils.isEmpty(endDate)) {
            sql.append("   and l.loan_date <=:endDate ");
            params.put("endDate",endDate);
        }
        if(EmptyUtils.isNotEmpty(corpIds)) {
            String corpIdCon = " and " + SqlUtil.getInSql(corpIds, "l.corp_id", true);
            sql.append(corpIdCon);
        }
        sql.append(" group by s.corp_id");
        List<Map> recodes = listBySQL(sql.toString(),params);
        HashMap<String,BigDecimal> result =Maps.newHashMap();
        if(EmptyUtils.isEmpty(recodes)){
            return result;
        }
        for(Map<String,Object> recode:recodes){
            result.put((String) recode.get("CORP_ID"),(BigDecimal) recode.get("TOTALLOANMONEY"));
        }
        return result;
    }
    /**
     * 按单位统计在指定还款日范围的还款
     * @param startDate
     * @param endDate
     * @param corpIds
     * @return
     */
    public HashMap<String, BigDecimal> findRepayByReplayDate(Date startDate, Date endDate, List<String> corpIds){
        //都已经有还款记录了就可能通过打回删除了
        StringBuilder sql=new StringBuilder("select CORP_ID,sum(repay_money) as TOTALREPAYMONEY ");
        sql.append("   from CMS_LOAN_REPAY ");
        sql.append("   where biz_status not in(41,47) ");
        Map<String,Object> params= Maps.newHashMap();
        if(!EmptyUtils.isEmpty(startDate)) {
            sql.append("   and repay_date >=:startDate ");
            params.put("startDate",startDate);
        }
        if(!EmptyUtils.isEmpty(endDate)) {
            sql.append("   and repay_date <=:endDate ");
            params.put("endDate",endDate);
        }
        if(EmptyUtils.isNotEmpty(corpIds)) {
            String corpIdCon = " and " + SqlUtil.getInSql(corpIds, "corp_id", true);
            sql.append(corpIdCon);
        }
        sql.append(" group by corp_id");
        List<Map> recodes = listBySQL(sql.toString(),params);
        HashMap<String,BigDecimal> result =Maps.newHashMap();
        if(EmptyUtils.isEmpty(recodes)){
            return result;
        }
        for(Map<String,Object> recode:recodes){
            result.put((String) recode.get("CORP_ID"),(BigDecimal) recode.get("TOTALREPAYMONEY"));
        }
        return result;
    }
    /**
     * 查询贷款信息中的申请单位和网点
     * @param corpIds
     * @param input
     * @return
     */
    public DataTablesOutput<LoanRepayReportVO> findLoanCorp(List<String> corpIds, DataTablesInput input) {
        StringBuilder sql=new StringBuilder("select  distinct l.net_code as netCode,l.corp_id as corpId,c1.name as netName,c2.name as corpName " );
        sql.append(" from cms_loan_info l left join sys_corp c1 on l.net_code = c1.id left join sys_corp c2 on l.corp_id = c2.id where l.status != -2 and ");
        sql.append(SqlUtil.getInSql(corpIds, "l.corp_id", true));
        Map<String,Object> params = Maps.newHashMap();
        DataTablesOutput<LoanRepayReportVO> result=this.getDynamicBySQL(input, sql.toString(), LoanRepayReportVO.class, "", false, params);
        return result;
    }
}
