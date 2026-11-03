package com.bytter.finance.domain.equity.dao;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhneghj
 * @version 1.0
 * @Title: 股权融资回购计划明细Dao
 * @Description: 股权融资回购计划明细Dao
 * @date 2022-06-29
 */
@Repository
public class CmsEquityRepoPlanDetailDao extends BaseDao<CmsEquityRepoPlanDetail, String> {
    /**
     * 根据融资编号查询计划明细
     *
     * @param repoPlanId
     *            计划主表id
     * @param equityCode
     *            融资编号
     * @return 计划明细
     */
    public List<CmsEquityRepoPlanDetailVO> listRepoPlanDetailByEquityCodeOrPlanId(String repoPlanId,
            String equityCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select ppd.id as id, ppd.REPO_PLAN_ID as repoPlanId,ppd.REPO_DATE as repoDate,ppd.equity_Code as equityCode,");
        sql.append(" ppd.REPO_MONEY as repoMoney,ppd.RE_REPO_MONEY as reRepoMoney,ppd.ACCOUNTED as accounted,");
        sql.append(" ppd.IS_PAY as isPay,ppd.VALID_SIGN as validSign,");
        sql.append(
                " ppd.ADD_OR_UPDATE as addOrUpdate,ppd.RMK as rmk, ppd.CREATE_DATE as createDate,ppd.UPDATE_DATE as updateDate ");
        sql.append(" from CMS_EQUITY_REPO_PLAN_DETAIL ppd");
        sql.append(" left join CMS_EQUITY_REPO_PLAN pp on pp.id=ppd.repo_plan_id");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(repoPlanId)) {
            sql.append(" and ppd.REPO_PLAN_ID = :repoPlanId ");
            params.put("repoPlanId", repoPlanId);
        }
        if (StringUtils.isNotEmpty(equityCode)) {
            sql.append(" and pp.EQUITY_CODE = :equityCode ");
            params.put("equityCode", equityCode);
        }
        // sql.append(" order by ppd.REPO_DATE ");
        sql.append(" order by ppd.RE_REPO_MONEY desc ");
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityRepoPlanDetailVO.class);
    }
    /**
     * 根据计划主表id删除计划明细表数据
     *
     * @param repoPlanId
     *            计划主表id
     * @param detailIds
     *            计划明细表id
     * @return 结果数
     */
    public int deleteRepoPlanDetailByPlanId(String repoPlanId, List<String> detailIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" DELETE CmsEquityRepoPlanDetail where repoPlanId=:repoPlanId ");
        if (!CollectionUtils.isEmpty(detailIds)) {
            sql.append("  and id not in (:detailIds) ");
            params.put("detailIds", detailIds);
        }
        params.put("repoPlanId", repoPlanId);
        return this.batchExecute(sql.toString(), params);
    }
}
