package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexPayerInfo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;


/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款人信息表
 * @date 2019-07-15 15:20:33
 */
@Repository
public class FexPayerInfoDao extends BaseDao<FexPayerInfo,String> {

    /**
     * 根据汇款账号获取汇款人信息
     * @param payerAcc
     * @return
     */
    public FexPayerInfo getByPayerAcc(String payerAcc){
        return this.getUniqueBy("payerAcc", payerAcc);
    }
}
