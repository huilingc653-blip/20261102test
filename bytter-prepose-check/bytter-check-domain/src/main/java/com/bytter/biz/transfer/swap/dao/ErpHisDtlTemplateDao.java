package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlTemplate;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/29.
 */
@Repository
public class ErpHisDtlTemplateDao extends BaseDao<ErpHisDtlTemplate,String> {
    public ErpHisDtlTemplate findExitBySid(String sid) {
        String hql = "from ErpHisDtlTemplate where serialId=?0";
        return getUnique(hql, sid);
    }
    public int batchStatusByIds(Map<String, Object> params) {
        StringBuilder hql=new StringBuilder("update ErpHisDtlTemplate "
                + " set status = :status,updateDy=:updateDy,updateDate=:updateDate ");
        if (params.containsKey("reverse2")){
            hql.append(" ,reverse2=:reverse2 ");
        }
        hql.append(" where serialId in (:ids) ");
        return this.batchExecute(hql.toString(), params);
    }
    public List<ErpHisDtlTemplate> listErpHisDtlTemplateByIds(Map<String, Object> params) {
        StringBuilder hql=new StringBuilder("select e from ErpHisDtlTemplate e ");
        hql.append(" where e.serialId in (:ids) ");
        return this.listByHQL(hql.toString(), params);
    }
}
