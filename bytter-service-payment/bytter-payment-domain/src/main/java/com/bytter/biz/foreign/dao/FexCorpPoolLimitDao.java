package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpPoolLimit;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 外汇资金池限额设置表
 * @date 2020-02-12 14:31:56
 */
@Repository
public class FexCorpPoolLimitDao extends BaseDao<FexCorpPoolLimit, String> {

    /**
     * 根据外汇资金池id(或者资金池账号表id)查找外汇资金池限额设置
     *
     * @param relatedId
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<FexCorpPoolLimit> listLimitByIdType(String relatedId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select f.id as id,f.related_id as relatedId,f.limit_type as limitType,f.limit_money as limitMoney,");
        sql.append(" f.pool_sign as poolSign FROM fex_corp_pool_limit f where 1=1 ");
        Map<String,Object> params = new HashMap<>();
        if(StringUtils.isNotEmpty(relatedId)){
            sql.append("and f.related_id =:relatedId");
            params.put("relatedId",relatedId);
        }
        sql.append(" order by f.limit_type ");
        return listBySQLAliasToBean(sql.toString(),params,FexCorpPoolLimit.class);
    }
}
