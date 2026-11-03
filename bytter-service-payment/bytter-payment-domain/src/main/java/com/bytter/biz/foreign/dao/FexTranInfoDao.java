package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;


/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 交易编码表
 * @date 2019-07-15 15:20:33
 */
@Repository
public class FexTranInfoDao extends BaseDao<FexTranInfo,String> {

    /**
     * 获取所有的交易编码（根据编码排序）
      * @return
     */
    public List<FexTranInfo> listOrderByTranCode(){
        String hql = " from FexTranInfo order by tranCode";
        return this.listByHQL(hql);
    }


    /**
     * 根据交易编码获取交易编码信息
     * @param tranCode
     * @return
     */
    public FexTranInfo getByTranCode(String tranCode){
        String hql = " from FexTranInfo where tranCode = :tranCode";
        HashMap<String, String> params = new HashMap<>();
        params.put("tranCode", tranCode);
        return  this.getUnique(hql, params);
    }

    /**
     * 根据项目名称获取交易编码信息
     * @param tranName
     * @return
     */
    public FexTranInfo getByTranName(String tranName){
        String hql = " from FexTranInfo where tranName = :tranName";
        HashMap<String, String> params = new HashMap<>();
        params.put("tranName", tranName);
        return  this.getUnique(hql, params);
    }
}
