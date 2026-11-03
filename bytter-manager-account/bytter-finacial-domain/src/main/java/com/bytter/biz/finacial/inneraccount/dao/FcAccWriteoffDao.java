package com.bytter.biz.finacial.inneraccount.dao;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.inneraccount.entity.FcAccWriteoff;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FC_ACC_WRITEOFF
* @author onlineGenerator
* @date 2017-06-12 10:08:33
* @version 1.0
*
*/
@Repository
public class FcAccWriteoffDao extends BaseDao<FcAccWriteoff,String>{
    /**
     * 删除记录
     * @param blnctrAcc
     * @return
     */
    public int delWriteOffByBlnctrAcc(String blnctrAcc) {
        String hql = " delete from FcAccWriteoff where writeoffAcc = :blnctrAcc";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("blnctrAcc", blnctrAcc);
        return updateDelByHql(hql, params);
    }
    @SuppressWarnings("rawtypes")
	public int  updateDelByHql(String hql, Map params) {
        Query query = getSession().createQuery(hql);
        if (params != null) {
            query.setProperties(params);
        }
        return query.executeUpdate();
    }
}
