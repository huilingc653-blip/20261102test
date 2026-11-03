package com.bytter.biz.bdlink.api.service;
/**
 * 调拨策略接口类
 * @author huangx
 * @since 2017-09-02 09:49:44
 */
public interface IBisAccGatherPolicyService {
    /**
     * 根据币别和账号删除调拨策略关系
     * @param bankAccId 账号ID
     * @param prentAccId 父账号ID
     * @param curId 币别ID
     */
    void deleteBisGatherPolicyByCurId(String bankAccId, String prentAccId, String curId);
}
