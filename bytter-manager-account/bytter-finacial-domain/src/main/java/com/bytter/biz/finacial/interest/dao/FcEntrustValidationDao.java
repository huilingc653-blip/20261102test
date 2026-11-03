package com.bytter.biz.finacial.interest.dao;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.core.base.BaseDao;
@Repository
public class FcEntrustValidationDao extends BaseDao<FcInterestDtl, String> implements Serializable {
	/**
	 * <b>功能介绍：</b>批量删除<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param list
	 */
	public void deleteListBatch(List list){
		Session session = getSession();
        for (int i = 0, size = list.size(); i < size; i++) {
            Object obj = list.get(i);
            session.delete(obj);
            if (i % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
	}
	
	/**
	 * <b>功能介绍：</b>批量更新数据(一个标识指向其他的引用)<br>
	 * <b>日期：</b>2017年8月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param list
	 */
	public void updateListBatch(List list) {
        Session session = getSession();
        for (int i = 0, size = list.size(); i < size; i++) {
            Object obj = list.get(i);
            session.merge(obj);
            if (i % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
    }
	
	
	
	
}
