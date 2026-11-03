package com.bytter.finance.domain.equity.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.equity.entity.CmsEquityPayThe;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheVO;
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
 * @Description: CMS_EQUITY_PAY_THE
 * @date 2022-06-20
 */
@Repository
public class CmsEquityPayTheDao extends BaseDao<CmsEquityPayThe, String> {
    /**
     * 功能介绍 : 查找缴付
     */
    public List<CmsEquityPayTheVO> getByCode(String equityCode, String flag) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_PAY_THE p");
        sql.append(" where p.equity_code=:equityCode");
        if (flag.equals("pass")) {
            sql.append(" and p.status in (95)");
        } else if (flag.equals("temp")) {
            sql.append(" and p.status in (-1,0)");
        } else if (flag.equals("comm")) {
            sql.append(" and p.status in (11)");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityPayTheVO.class);
    }
    /**
     * 功能介绍 : 股权融资缴付/过户列表数据查询
     */
    public DataTablesOutput getDatatableApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.*,d.asset_name as assetName ");
        sql.append(" from CMS_EQUITY_PAY_THE p ");
        sql.append(" left join CMS_EQUITY_INFO_DETAIL d on p.equity_code=d.equity_code");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "p.id")).append(" ");
        }
        OrderString orderString = new OrderString("p.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsEquityPayTheVO.class, null, false);
    }
    /**
     * 功能介绍 : 根据计划明细id查找缴付
     */
    public List<CmsEquityPayTheVO> getByPlanDetail(String detailId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_PAY_THE p");
        sql.append(" where p.PLAN_DETAIL_ID=:detailId");
        sql.append(" and p.status in (95)");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("detailId", detailId);
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityPayTheVO.class);
    }
}
