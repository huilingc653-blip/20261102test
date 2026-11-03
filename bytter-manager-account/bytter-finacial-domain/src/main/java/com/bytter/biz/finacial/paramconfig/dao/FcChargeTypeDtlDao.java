package com.bytter.biz.finacial.paramconfig.dao;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.paramconfig.entity.ChargeTypeBankTypeRel;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: 费用类型明细
* @author onlineGenerator
* @date 2017-06-15 11:24:27
* @version 1.0
*
*/
@Repository
public class FcChargeTypeDtlDao extends BaseDao<FcChargeTypeDtl,String>{
	
	/**
	 * 功能介绍 : 获取费用类型关联费用计算明细集合。<br>
	 * <b>日期：</b>2017年7月4日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcChargeTypeDtl> getChargeTypeDtlByTypeId(String typeId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select t from FcChargeTypeDtl t  ")
			.append(" where t.chargeTypeId = :typeId ");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		return listByHQL(hql.toString(), params);
	}
	
}
