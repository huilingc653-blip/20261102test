package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.entity.EDraftOrganBill;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * <b>描述：</b>机构查询Dao<br>
 * <b>作者：</b>李杰<br>
 * <b>日期：</b> 2013-12-24 <br>
 *
 */
@Repository("eDraftOrganBillDao")
public class EDraftOrganBillDao extends BaseDao<EDraftOrganBill,String> {
	
	/**
	 * 
	 * <b>方法描述：</b>根据机构类型或机构名称查询机构信息<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public List<EDraftOrganBill> queryEDraftOrganByParam(String organType,String organName,Long organId) {
		StringBuffer hql = new StringBuffer("FROM EDraftOrganBill WHERE 1=1");
		Map<String, Object> param = new HashMap<String, Object>();
		if(organType != null) {
			hql.append(" AND organType = :organType");
			param.put("organType", organType);
		}
		
		if(organName != null) {
			hql.append(" AND organName LIKE :organName");
			param.put("organName",  organName);
		}
		
		
		if (organId != null) {
			hql.append(" AND organId = :organId");
			param.put("organId", organId);
		}
		
		return this.listByHQL(hql.toString(), param);
	}
	
	/**
	 * 
	 * <b>方法描述：</b>删除指定机构类型的数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public void deleteByType(String organType) {
		String hql = " DELETE EDraftOrganBill WHERE organType = '"+organType+"'";
		this.batchExecute(hql);
	}
	
	/**
	 * 
	 * <b>方法描述：</b>删除所有数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2017-09-02 <br>
	 */
	public void deleteAll() {
		String hql = " DELETE EDraftOrganBill WHERE 1=1";
		this.batchExecute(hql);
	}
	
}
