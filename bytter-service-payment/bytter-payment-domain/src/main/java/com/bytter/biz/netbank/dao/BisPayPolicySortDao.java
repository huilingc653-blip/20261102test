package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.BisPayPolicySort;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @Title: Dao
 * @Description: 自动支付优先策略表
 * @author huangx
 * @date 2020-04-17 14:38:52
 * @version 1.0
 *
 */
@Repository
public class BisPayPolicySortDao extends BaseDao<BisPayPolicySort, String> {
    /**
     * 据策略ID获取业务优先策略关联信息
     * @param policyId
     * @return
     */
    public List<BisPayPolicySort> listByPolicyId(String policyId){
        String hql = " from BisPayPolicySort b where b.policyId = :policyId order by b.sort";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("policyId", policyId);
        return (List<BisPayPolicySort>)this.getSession().createQuery(hql).setProperties(params).list();
    }
}
