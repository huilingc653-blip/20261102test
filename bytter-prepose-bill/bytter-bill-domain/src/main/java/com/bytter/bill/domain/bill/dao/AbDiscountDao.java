package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbDiscount;
import com.bytter.core.base.BaseDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据贴现
* @author onlineGenerator
* @date 2017-06-21 15:43:17
* @version 1.0
*
*/
@Repository
public class AbDiscountDao extends BaseDao<AbDiscount,String>{
	
	/**
	 * 根据票据流水ID获取票据贴现信息
	 * @param recordId
	 * @return
	 */
	public List<AbDiscount> listAbDiscountinfoByRecordId(String recordId){
		String hql="from AbDiscount where recordId=:recordId";
		Map<String,Object> params=new HashMap<>();
		params.put("recordId", recordId);
		List<AbDiscount> list=this.listByHQL(hql, params);
		return list;
	}
	
	/**
	 * 根据recordId获取唯一票据贴现信息
	 * @param recordId
	 * @return
	 */
	public AbDiscount getAbDiscountByRecordId(String recordId){
		String hql="from AbDiscount where recordId=:recordId ";
		Map<String,Object> params=new HashMap<>();
		params.put("recordId",recordId);
		return (AbDiscount) this.createQuery(hql,params).uniqueResult();
	}
	/**
	 * 根据传递流水ID获取贴现明细id
	 * @param recordId  流水ID
	 * @return 贴现明细id
	 */
	public String getDiscountIdByRecordId(String recordId){
		StringBuilder sql=new StringBuilder("select a.id from Ab_Discount a ");
		sql.append(" where ");
		sql.append(" a.RECORD_ID=:recordId ");
		Map<String,Object> params=new HashMap<>();
		params.put("recordId", recordId);
		Query query=this.createSQLQuery(sql.toString(), params).setMaxResults(1);
		Object obj=query.uniqueResult();
		if(obj!=null){
			return (String)obj;
		}
		return "";
	}
	
	/**
	 * 根据recordId获取唯一票据贴现信息
	 * @param params
	 * @return
	 */
	public int deleteByRecordId(Map<String, Object> params){
		String hql="DELETE AbDiscount where recordId=:recordId";
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 *打回时候把当前贴现明细表的流水id修改为之前的流水id
	 * @param id 明细id
	 * @param recordId 流水id
	 * @return 返回修改成功数量
	 */
	public int updateDiscountRecordIdById(String id,String recordId){
		StringBuilder sql = new StringBuilder();
		sql.append(" update AbDiscount");
		sql.append(" set recordId=:recordId");
		sql.append(" where id=:id");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", id);
		params.put("recordId", recordId);
		return this.batchExecute(sql.toString(), params);
	}
	
}
