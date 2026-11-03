package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.core.base.BaseDao;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * @version V1.0
 * @Title: 合同业务流水表业务Dao
 * @Description: 合同业务流水表业务数据操作类
 * @author: zhenghj
 * @date: 2019-11-4
 */
@Repository
public class ConInfoRecordDao extends BaseDao<ConInfoRecord, String> {
    /**
     * 根据ids批量修改流水表数据
     * 
     * @param params
     * @return
     */
    public int updateRecordByParams(Map<String, Object> params) {
        StringBuilder hql = new StringBuilder("update ConInfoRecord " + " set " + "updateTime = :updateTime ");
        if (params.containsKey("updateId")) {
            hql.append(",updateId = :updateId ");
        }
        if (params.containsKey("validSign")) {
            hql.append(",validSign = :validSign ");
        }
        if (params.containsKey("backMsg")) {
            hql.append(",backMsg = :backMsg ");
        }
        if (params.containsKey("lastId")) {
            hql.append(",lastId = :lastId ");
        }
        if (params.containsKey("busType")) {
            hql.append(",busType = :busType ");
        }
        if (params.containsKey("status")) {
            hql.append(",status = :status ");
        }
        if (params.containsKey("opMsg")) {
            hql.append(",opMsg = :opMsg ");
        }
        if (params.containsKey("opDate")) {
            hql.append(",opDate = :opDate ");
        }
        if (!params.containsKey("ids") && !params.containsKey("conIdW")) {
            return 0;
        }
        hql.append(" where 1=1 ");
        if (params.containsKey("ids")) {
            hql.append(" and id in (:ids) ");
        }
        if (params.containsKey("status")) {
            hql.append(" and status<>:status ");
        }
        if (params.containsKey("statusOld")) {
            hql.append(" and status=:statusOld ");
        }
        if (params.containsKey("conIdW")) {
            hql.append(" and mid =:conIdW ");
        }
        if (params.containsKey("statusW")) {
            hql.append(" and status=:statusW ");
        }
        if (params.containsKey("validSignW")) {
            hql.append(" and validSign=:validSignW ");
        }
        if (params.containsKey("busTypesW")) {
            hql.append(" and busType in (:busTypesW) ");
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 通过参数获取流水表业务信息
     * 
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ConInfoRecord> getConInfoRecordByParams(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuffer hql = new StringBuffer("from ConInfoRecord cir  ");
        hql.append(" where cir.status !=:deleteStatus ");
        params.put("deleteStatus", ConConstant.CON_DRAFT_STATUS_99);
        if (params.containsKey("mid")) {
            hql.append(" and cir.mid=:mid ");
        }
        if (params.containsKey("busTypes")) {
            hql.append(" and cir.busType in (:busTypes) ");
        }
        if (params.containsKey("status")) {
            hql.append(" and cir.status=:status ");
        }
        if (params.containsKey("validSign")) {
            hql.append(" and cir.validSign=:validSign) ");
        }
        hql.append(" order by cir.busType,cir.createTime ");
        return this.listByHQL(hql.toString(), params);
    }
}
