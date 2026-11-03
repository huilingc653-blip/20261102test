package com.bytter.system.dao;
import com.bytter.system.entity.BisAccAttribute;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 账户性质维护
 * @date 2017.9.14
 */
@Repository
public class BisAccAttributeDao extends BaseDao<BisAccAttribute, String> {
	/**
	 * 根据编码获取账户属性
	 *
	 * @param attributeCode
	 * @return
	 */
	public BisAccAttribute findByCode(String attributeCode) {
		return getUniqueBy("attributeCode", attributeCode);
	}
	/**
	 * 获取有效的账户属性
	 *
	 * @return
	 */
	public List<BisAccAttribute> getValidAttribute() {
		return listBy("validSign", CommonConstant.VALID_SIGN_YES);
	}
	/**
	 * 获取可对外付款的账户属性
	 * @return
	 */
	public List<BisAccAttribute> getAttriButeForEign(){
		String hql = " from BisAccAttribute where (foreignDes = 2 or foreignDes = 3 ) and validSign = "+ CommonConstant.VALID_SIGN_YES;
		return listByHQLAliasToBean(hql,BisAccAttribute.class);
	}
	/**
	 * 判断账户属性是否在使用中
	 *
	 * @return
	 */
	public boolean queryUse(String attributeId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select COUNT(*) as count");
		sql.append(" from bt_bank_acc_app b ");
		sql.append(" where b.ATTRIBUTE_ID=:attributeId");
		Map<String,String> params = new HashMap<String,String>();
		params.put("attributeId", attributeId);
		Map map=getBySQL(sql.toString(), params);
		if(!map.isEmpty()){
			BigDecimal count = (BigDecimal)map.get("COUNT");
			if(count!=null){
				int i=count.compareTo(BigDecimal.ZERO);
				if(i!=0){
					return true;
				}
			}
		}
		StringBuilder sql2 = new StringBuilder();
		sql2.append("select COUNT(*) as count");
		sql2.append(" from Fc_Acc b ");
		sql2.append(" where b.ATTRIBUTE_ID=:attributeId");
		Map map2=getBySQL(sql2.toString(), params);
		if(!map2.isEmpty()){
			BigDecimal count = (BigDecimal)map.get("COUNT");
			if(count!=null){
				int i=count.compareTo(BigDecimal.ZERO);
				if(i!=0){
					return true;
				}
			}
		}
		StringBuilder sql3 = new StringBuilder();
		sql3.append("select COUNT(*) as count");
		sql3.append(" from bt_bank_acc  b ");
		sql3.append(" where b.ATTRIBUTE_ID=:attributeId");
		params.put("attributeId", attributeId);
		Map map3=getBySQL(sql3.toString(), params);
		if(!map3.isEmpty()){
			BigDecimal count = (BigDecimal)map3.get("COUNT");
			if(count!=null){
				int i=count.compareTo(BigDecimal.ZERO);
				if(i!=0){
					return true;
				}
			}
		}
		return false;   //未使用
	}
}
