package com.bytter.guarantee.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.core.base.BaseDao;
/**
 * @version V1.0
 * @Title: 合同字段明细业务Dao
 * @Description: 合同字段明细业务数据操作类
 * @author: zhenghj
 * @date: 2019-11-4
 */
@Repository
public class ConInfoBusdtlDao extends BaseDao<ConInfoBusDtl, String> {
    /**
     * 通过参数获取字段明细业务信息
     * 
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ConInfoBusDtl> getConInfoBusDtlByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuffer hql = new StringBuffer("from ConInfoBusDtl cib  ");
        hql.append(" where 1=1 ");
        if (params.containsKey("mid")) {
            hql.append(" and cib.mid=:mid ");
        }
        hql.append(" order by cib.sortNum ");
        return this.listByHQL(hql.toString(), params);
    }
    /**
     * 根据主表id删除字段明细信息
     * 
     * @param mid
     *            主表id
     */
    public void deleteConInfoBusDtlByMid(String mid) {
        StringBuilder hql = new StringBuilder("delete from ConInfoBusDtl where mid=:mid ");
        Map<String, Object> params = new HashMap<>();
        params.put("mid", mid);
        this.batchExecute(hql.toString(), params);
    }
}
