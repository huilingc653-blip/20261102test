package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.BisPayPolicyPayee;
import com.bytter.biz.netbank.vo.BisPayPolicyPayeeVO;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 自动支付策略收款人关联表
 *
 * @author huangx，fangcf
 * @since 2025-05-19
 */
@Repository
public class BisPayPolicyPayeeDao extends BaseDao<BisPayPolicyPayee, String> {
    /**
     * 据策略ID获取业务收款方关联信息
     *
     * @param policyId 策略id
     * @return 收款方关联信息集合
     */
    public List<BisPayPolicyPayee> listByPolicyId(String policyId) {
        String hql = " from BisPayPolicyPayee b where b.policyId = :policyId";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("policyId", policyId);
        return (List<BisPayPolicyPayee>) this.getSession().createQuery(hql).setProperties(params).list();
    }


    /**
     * 据策略ID获取业务类别关联信息
     *
     * @param policyId 策略id
     * @return 收款方关联信息
     */
    public List<BisPayPolicyPayeeVO> listByPolicyIdJoin(String policyId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.policy_Id as policyId, b.payee_Id as payeeId, b.payee_acc as payeeAcc, b.payee_name as payeeName, bt_nvl(b.IS_PAYEE, '1') as isPayee ");
        sql.append(" from bis_pay_policy_payee b ");
        sql.append(" where b.policy_Id = :policyId");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("policyId", policyId);
        return listBySQLAliasToBean(sql.toString(), params, BisPayPolicyPayeeVO.class);
    }

}
