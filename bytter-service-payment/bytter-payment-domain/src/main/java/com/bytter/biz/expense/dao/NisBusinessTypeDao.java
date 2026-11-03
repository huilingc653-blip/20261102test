package com.bytter.biz.expense.dao;
import com.bytter.biz.expense.vo.NisBusinessTypeVO;
import org.springframework.stereotype.Repository;
import com.bytter.biz.expense.entity.NisBusinessType;
import com.bytter.core.base.BaseDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 业务类型表
* @author onlineGenerator
* @date 2017-06-01 14:42:30
* @version 1.0
*
*/
@Repository
public class NisBusinessTypeDao extends BaseDao<NisBusinessType,String>{
	/**
	 * 根据业务类型名称查询
	 * zuoy 2017-08-15
	 * @return NisBusinessType
	 */
	public List<NisBusinessTypeVO> listAllBusType() {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NisBusinessType b");
		return listByHQLAliasToBean(hql.toString(), NisBusinessTypeVO.class);
	}
	/**
	 * 根据业务类型名称查询
	 * zuoy 2017-08-15
	 * @param busTypeName	业务类型名称
	 * @return NisBusinessType
	 */
	public NisBusinessType getBusTypeName(String busTypeName) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from NisBusinessType b where b.busTypeName = :busTypeName ");
		Map<String, String> params = new HashMap<String, String>();
		params.put("busTypeName", busTypeName);
		return (NisBusinessType) this.createQuery(hql.toString()).setProperties(params).uniqueResult();
	}
}
