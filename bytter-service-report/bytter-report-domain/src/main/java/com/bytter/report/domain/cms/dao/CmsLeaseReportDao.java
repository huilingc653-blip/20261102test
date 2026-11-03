package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.entity.CmsFinancingLeaseInfo;
import com.bytter.report.domain.cms.vo.CmsLeaseReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanggl
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_FINANCING_LEASE_INFO
 * @date 2022-04-24
 */
@Repository
public class CmsLeaseReportDao extends BaseDao<CmsFinancingLeaseInfo, String> {

    /**
     * 功能介绍 : 变更主页租赁申请列表数据查询
     */
    public DataTablesOutput getDatatables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from(");
        sql.append("select i.id leaseId,i.LEASE_TYPE ,i.CORP_ID,i.LESSOR,i.SUPPLIER,i.LEASE_CODE,i.BEGIN_DATE,i.END_DATE,i.ORI_CURID,i.ORI_MONEY,i.RMB_MONEY,i.status, ");
        sql.append(" sum(case when p.IS_REPAY = 1 then p.THIS_MONEY else 0 end) as allThisMoney,");
        sql.append(" sum(case when p.IS_REPAY = 1 then p.LEASE_MONRY else 0 end) as allMoney,");
        sql.append(" sum(case when p.IS_REPAY = 1 then p.LEASE_INTEREST else 0 end) as allInter,");
        sql.append(" sum(case when p.IS_REPAY = 1 then p.THIS_COST else 0 end) as allCost,");
        sql.append(" sum(case when p.IS_REPAY = 0 then p.THIS_MONEY else 0 end) as notThisMoney,");
        sql.append(" sum(case when p.IS_REPAY = 0 then p.LEASE_MONRY else 0 end) as notallMoney,");
        sql.append(" sum(case when p.IS_REPAY = 0 then p.LEASE_INTEREST else 0 end) as notallInter,");
        sql.append(" sum(case when p.IS_REPAY = 0 then p.THIS_COST else 0 end) as notallCost ");
        sql.append(" from CMS_FINANCING_LEASE_INFO i  ");
        sql.append(" left join CMS_LEASE_CAPITAL_PLAN plan on plan.LEASE_ID = i.id");
        sql.append(" left join CMS_LEASE_CAPITAL_PLANDEL p on plan.ID = p.LEASE_PLAN_ID");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        sql.append(" group by p.lease_plan_id,i.id,i.LEASE_TYPE,");
        sql.append("i.CORP_ID,i.LESSOR,i.SUPPLIER,i.LEASE_CODE,i.BEGIN_DATE,i.END_DATE,i.ORI_CURID,i.ORI_MONEY,i.RMB_MONEY,i.status ");
        sql.append(")i where 1=1");
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseReportVO.class, null, false);
    }

    public DataTablesOutput getDetailDatatables(DataTablesInput dataTablesInput, String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select p.PAY_DATE payDate,p.PAY_PLAN_DATE planDate,p.THIS_MONEY thisMoney ,p.LEASE_MONRY leaseMoney,p.LEASE_INTEREST leaseInterest, ");
        sql.append(" p.THIS_COST thisCost,p.SURPLUS_MONEY surplusMoney,c.LEASE_CODE leaseCode,c.ID,c.rate,p.BAIL_MONEY bailMoney,");
        sql.append(" c.BEGIN_DATE beginDate,c.LESSOR,c.END_DATE endDate,p.IS_REPAY isRepay, c.POINTS_PROPORTION_TEXT pointProText");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p left join CMS_LEASE_CAPITAL_PLAN z on z.id = p.LEASE_PLAN_ID left join CMS_FINANCING_LEASE_INFO c on z.LEASE_ID = c.ID ");
        sql.append(" where 1=1 and z.LEASE_ID = :leaseId and p.valid_sign <> '" +CmsConstant.DELETED+ "' ");
        params.put("leaseId", leaseId);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseReportVO.class, null, false, params);
    }

    public List<CmsLeaseReportVO> getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.LEASE_CODE,f.CORP_ID,f.LESSOR,f.BEGIN_DATE,f.END_DATE,f.lease_type leaseType from CMS_FINANCING_LEASE_INFO f");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(leaseId)) {
            sql.append(" and f.id= :leaseId ");
            params.put("leaseId", leaseId);
        }
        List<CmsLeaseReportVO> list = listBySQLAliasToBean(sql.toString(), params, CmsLeaseReportVO.class);
        return list;
    }
}
