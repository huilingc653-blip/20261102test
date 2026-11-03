package com.bytter.guarantee.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.ConLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: Dao
 * @Description: 合同管理日志
 * @version 1.0
 *
 */
@Repository
public class ConLogDao extends BaseDao<ConLog, String> {
    /**
     * 查询满足quoteId的日志记录数据
     * 
     * @param quoteIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ConLog> listConLog(String[] quotezIds, String[] quoteIds, String[] idsAll) {
        String hql = "from ConLog where 1=1 ";
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(quotezIds)) {
            params.put("quotezIds", quotezIds);
            hql += "and quotezId in (:quotezIds) ";
        }
        if (StringUtils.isNotEmpty(quoteIds)) {
            params.put("quoteIds", quoteIds);
            hql += "and quoteId in (:quoteIds) ";
        }
        if (StringUtils.isNotEmpty(idsAll)) {
            params.put("idsAll", idsAll);
            hql += "and (quoteId in (:idsAll) or quotezId in (:idsAll) ) ";
        }
        hql += " order by createDate asc ";
        return this.listByHQL(hql, params);
    }
}
