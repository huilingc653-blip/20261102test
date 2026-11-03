package com.bytter.biz.receipt.dao;
import com.bytter.biz.receipt.entity.EReceiptType;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 电子回单类型
* @author onlineGenerator
* @date 2017-06-05 10:52:28
* @version 1.0
*
*/
@Repository
public class EReceiptTypeDao extends BaseDao<EReceiptType,String>{
	/**
	 * 查询电子回单类型列表
	 * @param validSign
	 * @param billType
	 * @return
	 */
	public List<EReceiptType> listReceiptType(String validSign,String billType){
		StringBuilder hql=new StringBuilder(" from EReceiptType where 1=1 ");
		Map<String,Object> map=new HashMap<String,Object>();
		if(StringUtils.isNotEmpty(validSign)){
			hql.append(" and validSign =:validSign");
			map.put("validSign",validSign);
		}
		if(StringUtils.isNotEmpty(billType)){
			hql.append(" and billType =:billType");
			map.put("billType",billType);
		}
		return listByHQL(hql.toString(),map);
	}
	/**
	 * 查询电子回单类型
	 * @param billType
	 * @return
	 */
	public EReceiptType getReceiptType(String billType){
		return getUniqueBy("billType",billType);
	}
}
