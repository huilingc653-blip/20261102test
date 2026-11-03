package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeBankTypeRel;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: 费用类型与银行类型关联表
* @author onlineGenerator
* @date 2017-05-23 14:29:59
* @version 1.0
*
*/
@Repository
public class ChargeTypeBankTypeRelDao extends BaseDao<ChargeTypeBankTypeRel,String>{
	
	public void save(final ChargeTypeBankTypeRel entity) {
		getSession().save(entity);
	}
	
	/**
	 * 功能介绍 : 获取费用类型关联银行类型集合。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<ChargeTypeBankTypeRel> getChargeTypeRelsByTypeId(String typeId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from ChargeTypeBankTypeRel t  ")
			.append(" where t.chargeTypeId = :typeId ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		return listByHQL(hql.toString(), params);
	}
}
