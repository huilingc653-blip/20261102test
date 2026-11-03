package com.bytter.guarantee.domain.dao;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.ConBusHook;
import com.bytter.core.base.BaseDao;
import com.bytter.guarantee.constant.ConConstant;
/**
 * @version V1.0
 * @Title: 合同业务挂接Dao
 * @Description: 合同业务挂接数据操作类
 * @author: zhengg
 * @date: 2019-11-9
 */
@Repository
public class ConBusHookDao extends BaseDao<ConBusHook, String> {
    /**
     * 通过参数获取业务信息
     * 
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ConBusHook> getConBusHookByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuffer hql = new StringBuffer("from ConBusHook cbh  ");
        hql.append(" where 1=1 ");
        if (params.containsKey("validSign")) {
            hql.append(" and cbh.validSign=:validSign");
        }
        if (params.containsKey("conId")) {
            hql.append(" and cbh.conId=:conId ");
        }
        if (params.containsKey("busId")) {
            hql.append(" and cbh.busId=:busId ");
        }
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据条件设置合同挂接失效
     * 
     * @param params
     * @return
     */
    public int updateConBusHookIsOver(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return 0;
        }
        params.put("validSign", ConConstant.CON_BUSHOOK_VALID_SIGN_0);
        StringBuffer hql = new StringBuffer("update ConBusHook set validSign=:validSign");
        if (params.containsKey("conId")) {
            hql.append(" ,updateId=:updateId ");
        }
        if (params.containsKey("conId")) {
            hql.append(" ,updateTime=:updateTime ");
        }
        hql.append(" where 1=1 ");
        if (params.containsKey("conId")) {
            hql.append(" and conId=:conId ");
        }
        if (params.containsKey("busId")) {
            hql.append(" and busId=:busId ");
        }
        return this.batchExecute(hql.toString(), params);
    }
}
