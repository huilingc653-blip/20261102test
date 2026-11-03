package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityPlanDetail;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityPlanDetailDao extends BaseDao<CmsSecurityPlanDetail, String> {
    /*
     * 根据编号获取计划明细
     */
    public List<CmsSecurityPlanDetailVO> getPlanDetailByCode(String code) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_PLAN_DETAIL where SECURITY_ISSUE_CODE=:code");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        sql.append("  order by  IS_REPAY desc,REPAY_DATE,REMAIN_MONEY desc ");
        return listBySQLAliasToBean(sql.toString(), params, CmsSecurityPlanDetailVO.class);
    }
    /**
     * 根据计划主表id删除计划明细表数据
     *
     * @param planId
     *            计划主表id
     * @param detailIds
     *            计划明细表id
     * @return 结果数
     */
    public void deleteOtherDetail(String planId, List<String> detailIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" delete from CMS_SECURITY_PLAN_DETAIL where SECURITY_PLAN_ID=:planId");
        if (!CollectionUtils.isEmpty(detailIds)) {
            sql.append("  and id not in (:detailIds) ");
            params.put("detailIds", detailIds);
        }
        params.put("planId", planId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
