package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.BisPayPolicyPayerAcc;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 自动支付策略支付账号关联
 *
 * @author fangcf
 * @since 2025-05-19
 */
@Repository
public class BisPayPolicyPayerAccDao extends BaseDao<BisPayPolicyPayerAcc, String> {

    /**
     * 据策略ID获取业务支付账号关联信息
     *
     * @param policyId 策略id
     * @return 支付账号关联信息集合
     */
    public List<BisPayPolicyPayerAcc> listByPolicyId(String policyId) {
        String hql = " from BisPayPolicyPayerAcc b where b.policyId = :policyId";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("policyId", policyId);
        return (List<BisPayPolicyPayerAcc>) this.getSession().createQuery(hql).setProperties(params).list();
    }
}
