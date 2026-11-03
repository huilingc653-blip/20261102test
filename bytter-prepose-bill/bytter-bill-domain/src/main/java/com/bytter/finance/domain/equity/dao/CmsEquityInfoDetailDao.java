package com.bytter.finance.domain.equity.dao;
import com.bytter.finance.domain.equity.entity.CmsEquityInfoDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_EQUITY_INFO_DETAIL
 * @date 2022-06-08
 */
@Repository
public class CmsEquityInfoDetailDao extends BaseDao<CmsEquityInfoDetail, String> {
    public void updateEquityId(List<String> ids, String equityId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update  CMS_EQUITY_INFO_DETAIL set equity_id=:equityId");
        sql.append(" where 1=1");
        sql.append(" and ").append(getSQLIn(ids, 900, "id"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityId", equityId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /*
     * 根据登记id得到明细
     */
    public List<CmsEquityInfoDetailVO> getDetails(String equityId, List<String> bizStatuss) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_INFO_DETAIL det");
        sql.append(" where det.equity_id=:equityId");
        if (!EmptyUtils.isEmpty(bizStatuss)) {
            sql.append(" and ").append(getSQLIn(bizStatuss, 50, "det.biz_status"));
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityId", equityId);
        sql.append("  order by det.create_date ");
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityInfoDetailVO.class);
    }
    public CmsEquityInfoDetailVO getDetailByCode(String equityCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_INFO_DETAIL det");
        sql.append(" where det.equity_code=:equityCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        return getBySQLAliasToBean(sql.toString(), params, CmsEquityInfoDetailVO.class);
    }
    public void deleteOtherDetail() {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from  CMS_EQUITY_INFO_DETAIL where equity_id is null");
        createSQLQuery(sql.toString()).executeUpdate();
    }
    public void updateBizStatus(String equityId, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update  CMS_EQUITY_INFO_DETAIL set biz_status=:bizStatus");
        sql.append(" where 1=1");
        sql.append(" and equity_id=:equityId");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityId", equityId);
        params.put("bizStatus", bizStatus);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public void updateBizStatusByEquityCode(String equityCode, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update  CMS_EQUITY_INFO_DETAIL set biz_status=:bizStatus");
        sql.append(" where 1=1");
        sql.append(" and equity_Code=:equityCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("equityCode", equityCode);
        params.put("bizStatus", bizStatus);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
