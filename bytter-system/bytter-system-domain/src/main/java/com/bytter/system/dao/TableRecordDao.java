package com.bytter.system.dao;
import com.bytter.system.entity.TableRecord;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.ListUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Created by zhoul on 2017/4/13.
 */
@Repository
public class TableRecordDao extends BaseDao<TableRecord, String> {
    public TableRecord getByTableKeyAndUserId(String tableKey, String userId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tableKey", tableKey));
        criteria.add(Restrictions.eq("userId", userId));
        List<TableRecord> list = criteria.list();
        if (ListUtils.isNotNullOrEmpty(list)) {
            return list.get(0);
        }
        return null;
//        return (TableRecord) criteria.uniqueResult();
    }



    public TableRecord getDefTableKey(String tableKey) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tableKey",tableKey));
        List<TableRecord> list=criteria.list();
        if(ListUtils.isNotNullOrEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
