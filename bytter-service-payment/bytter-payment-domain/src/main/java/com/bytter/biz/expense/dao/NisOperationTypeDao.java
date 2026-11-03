package com.bytter.biz.expense.dao;
import com.bytter.biz.expense.entity.NisOperationType;
import com.bytter.biz.expense.vo.NisOperationTypeVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 操作类型表
 * @date 2017-06-01 14:40:32
 */
@Repository
public class NisOperationTypeDao extends BaseDao<NisOperationType, String> {
	/**
	 * 根据业务类型获取操作类型列表
	 *
	 * @param busType
	 * @return
	 */
	public List<NisOperationTypeVO> listByBusType(String busType) {
		StringBuilder hql = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		hql.append(" from NisOperationType o where o.busType = :busType");
		params.put("busType", busType);
		return listByHQLAliasToBean(hql.toString(), params, NisOperationTypeVO.class);
	}
	/**
	 * 根据业务类型和操作类型名称查询数据
	 * zuoy 2017-08-15
	 *
	 * @param busType    业务类型
	 * @param opTypeName 操作类型名称
	 * @return NisOperationType
	 */
	public NisOperationType getByBusTypeAndOpName(String busType, String opTypeName) {
		StringBuilder hql = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		hql.append(" from NisOperationType o where o.busType = :busType and o.opTypeName = :opTypeName");
		params.put("busType", busType);
		params.put("opTypeName", opTypeName);
		return (NisOperationType) this.createQuery(hql.toString()).setProperties(params).uniqueResult();
	}
}
