package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexLinkmanInfo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款联系人表
 * @date 2019-07-15 15:20:33
 */
@Repository
public class FexLinkmanInfoDao extends BaseDao<FexLinkmanInfo,String> {

    /**
     * 根据单位Id获取汇款联系人信息
     * @param corpId
     * @return
     */
    public List<FexLinkmanInfo> listByCorpId(String corpId){
        return this.listBy("corpId", corpId);
    }

    /**
     * 根据单位Id获取汇款联系人信息
     * @param corpId
     * @param payerName
     * @return
     */
    public List<FexLinkmanInfo> listByCorpId(String corpId, String payerName) {
        String hql = " from FexLinkmanInfo f where f.corpId=:corpId and f.payerName=:payerName";
        HashMap<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("payerName", payerName);
        return  this.listByHQL(hql, params);
    }

}
