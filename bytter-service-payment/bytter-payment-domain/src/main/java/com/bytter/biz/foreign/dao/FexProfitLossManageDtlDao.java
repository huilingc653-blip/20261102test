package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexProfitLossManageDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2025/9/10<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class FexProfitLossManageDtlDao extends BaseDao<FexProfitLossManageDtl,String> {

    /**
     * 根据交易id获取损益明细集合
     * @param reqId
     * @return
     */
    public List<FexProfitLossManageDtl> listFexProfitLossManageDtl(String reqId){
        Map<String,String> params = new HashMap<>();
        params.put("reqId",reqId);
        StringBuilder hql = new StringBuilder();
        hql.append("from FexProfitLossManageDtl where reqId =:reqId");
        params.put("reqId", reqId);
        return createQuery(hql.toString(),params).list();
    }

}
