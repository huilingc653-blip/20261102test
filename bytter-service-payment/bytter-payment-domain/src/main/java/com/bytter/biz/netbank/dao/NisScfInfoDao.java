package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisScfInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.core.base.BaseDao;

/**
 * @Title: Dao
 * @Description: 付款单-供应链金融债权凭证关联表
 * @author zhukr
 * @since 2026/8/18
 */
@Repository
public class NisScfInfoDao extends BaseDao<NisScfInfo, String> {

    /**
     * 根据付款单ID获取关联的债权凭证
     *
     * @param payId 付款单ID
     * @return 关联信息
     */
    public List<NisScfInfo> listByPayId(String payId) {
        String hql = " from NisScfInfo s where s.payId = :payId ";
        Map<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        return listByHQL(hql, params);
    }

    /**
     * 根据付款单ID和凭证类型获取关联的债权凭证
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 关联信息
     */
    public List<NisScfInfo> listByPayIdAndType(String payId, String voucherType) {
        String hql = " from NisScfInfo s where s.payId = :payId and s.voucherType = :voucherType ";
        Map<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        params.put("voucherType", voucherType);
        return listByHQL(hql, params);
    }

    /**
     * 根据付款单号和凭证类型获取关联的债权凭证
     *
     * @param payNo       付款单号
     * @param voucherType 凭证类型（1=应付，2=应收）
     * @return 关联信息
     */
    public List<NisScfInfo> listByPayNoAndType(String payNo, String voucherType) {
        String hql = " from NisScfInfo s where s.payNo = :payNo and s.voucherType = :voucherType ";
        Map<String, Object> params = new HashMap<>();
        params.put("payNo", payNo);
        params.put("voucherType", voucherType);
        return listByHQL(hql, params);
    }

    /**
     * 根据付款单ID和凭证类型删除关联信息
     *
     * @param payId       付款单ID
     * @param voucherType 凭证类型（1=应付，2=应收）
     */
    public void delByPayIdAndType(String payId, String voucherType) {
        String hql = "delete from NisScfInfo s where s.payId = :payId and s.voucherType = :voucherType";
        Map<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        params.put("voucherType", voucherType);
        batchExecute(hql, params);
    }

    /**
     * 根据债权凭证ID获取付款单与凭证的关联信息
     *
     * @param voucherId 债权凭证ID
     * @return 关联信息
     */
    public List<NisScfInfo> listByVoucherId(String voucherId) {
        String hql = " from NisScfInfo s where s.voucherId = :voucherId ";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherId", voucherId);
        return listByHQL(hql, params);
    }

    /**
     * 根据付款单ID集合删除关联信息
     *
     * @param payIds 付款单ID集合
     */
    public void delByPayIds(List<String> payIds) {
        if (ObjectUtil.isEmpty(payIds)) {
            return;
        }
        String hql = "delete from NisScfInfo s where s.payId in (:payIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("payIds", payIds);
        batchExecute(hql, params);
    }
}
