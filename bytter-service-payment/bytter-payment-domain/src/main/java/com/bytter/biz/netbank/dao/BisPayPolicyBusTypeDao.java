package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.BisPayPolicyBusType;
import com.bytter.biz.netbank.vo.BisPayPolicyBusTypeVO;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * 自动支付策略业务类别关联表
 *
 * @author huangx, fangcf
 * @since 2025-05-19
 */
@Repository
public class BisPayPolicyBusTypeDao extends BaseDao<BisPayPolicyBusType, String> {

    /**
     * 据策略ID获取业务类别关联信息
     *
     * @param policyId 策略id
     * @return 业务类别与共享业务类型集合
     */
    public List<BisPayPolicyBusType> listByPolicyId(String policyId) {
        String hql = " from BisPayPolicyBusType b where b.policyId = :policyId";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("policyId", policyId);
        return (List<BisPayPolicyBusType>) this.getSession().createQuery(hql).setProperties(params).list();
    }

    /**
     * 据策略ID获取业务类别关联信息
     *
     * @param policyId 策略id
     * @return 业务类别集合
     */
    public List<BisPayPolicyBusTypeVO> listBusTypeByPolicyId(String policyId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.policy_Id as policyId, b.bus_Type_Id as busTypeId, n.bus_Type_Name as busTypeName, b.SHARE_BUS_TYPE_ID as shareBusTypeId, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName ");
        sql.append(" from bis_pay_policy_bustype b ");
        sql.append(" left join nis_bus_type n on n.id = b.bus_Type_Id ");
        sql.append(" left join nis_share_bus_type nsbt on nsbt.id = b.SHARE_BUS_TYPE_ID ");
        sql.append(" where b.policy_Id = :policyId ");
        HashMap<String, String> params = Maps.newHashMap();
        params.put("policyId", policyId);
        return listBySQLAliasToBean(sql.toString(), params, BisPayPolicyBusTypeVO.class);
    }

    /**
     * 据共享业务类型id获取业务类别关联信息
     *
     * @param shareBusTypeId 共享业务类型id
     * @return 业务类别与共享业务类型集合
     */
    public List<BisPayPolicyBusType> listByShareBusTypeId(String shareBusTypeId) {
        String hql = " from BisPayPolicyBusType b where b.shareBusTypeId = :shareBusTypeId";
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("shareBusTypeId", shareBusTypeId);
        return (List<BisPayPolicyBusType>) this.getSession().createQuery(hql).setProperties(params).list();
    }
}
