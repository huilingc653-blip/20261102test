package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRate;
import com.bytter.finance.domain.provide.vo.CmsInterestWayVO;
import com.bytter.finance.domain.provide.vo.CmsProvideLoanRateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: CMS_PROVIDE_LOAN_RATE
 * @author onlineGenerator
 * @date 2017-08-29 10:36:00
 * @version 1.0
 *
 */
@Repository
public class CmsProvideLoanRateDao extends BaseDao<CmsProvideLoanRate, String> {
    public List<CmsProvideLoanRateVO> listCmsProvideLoanRateVO(String id) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "pl_rate as plRate,deferred_rate as deferredRate,overdue_rate as overdueRate,enter_date as enterDate from cms_provide_loan_rate ");
        sql.append("where bill_Code=:id order by create_date desc");
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideLoanRateVO.class);
    }
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput, String billCode) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(
                "pl_rate as plRate,deferred_rate as deferredRate,overdue_rate as overdueRate,enter_date as enterDate from cms_provide_loan_rate ");
        sql.append("where bill_Code=:billCode order by create_date asc");
        params.put("billCode", billCode);
        return getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, CmsProvideLoanRateVO.class);
    }
}
