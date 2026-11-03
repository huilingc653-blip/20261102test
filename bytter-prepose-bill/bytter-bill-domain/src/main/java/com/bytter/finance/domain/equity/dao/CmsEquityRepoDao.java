package com.bytter.finance.domain.equity.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_EQUITY_REPO
 * @date 2022-06-28
 */
@Repository
public class CmsEquityRepoDao extends BaseDao<CmsEquityRepo, String> {
    /**
     * 功能介绍 : 查找回购
     */
    public List<CmsEquityRepoVO> getByCode(String equityCode, String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_REPO r");
        sql.append(" where r.equity_code=:equityCode");
        if (flag.equals("pass")) {
            sql.append(" and r.status in (95)");
        } else if (flag.equals("temp")) {
            sql.append(" and r.status in (-1,0)");
        } else if (flag.equals("comm")) {
            sql.append(" and r.status in (11)");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityRepoVO.class);
    }
    /**
     * 功能介绍 : 删除暂存的数据
     */
    public void deleteRepo(String equityCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete  from  CMS_EQUITY_REPO where equity_code=:equityCode and status in (0,-1)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 股权融资缴付/过户列表数据查询
     */
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r.* ");
        sql.append(" from CMS_EQUITY_REPO r ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "r.id")).append(" ");
        }
        OrderString orderString = new OrderString("r.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityRepoVO.class, null, false);
    }
    /**
     * 功能介绍 : 根据计划明细id查找回购
     */
    public List<CmsEquityRepoVO> getByPlanDetail(String detailId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_REPO r");
        sql.append(" where r.REPO_PLAN_DETAIL_ID=:detailId");
        sql.append(" and r.status in (95)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("detailId", detailId);
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityRepoVO.class);
    }
}
