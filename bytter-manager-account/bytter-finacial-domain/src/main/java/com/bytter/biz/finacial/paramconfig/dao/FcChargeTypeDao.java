package com.bytter.biz.finacial.paramconfig.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeType;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeTypeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
@Repository
public class FcChargeTypeDao extends BaseDao<FcChargeType,String>{
	
	/**
	 * <b>功能介绍:查询列表数据</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listFcChargeType(DataTablesInput input){
		StringBuilder sql = new StringBuilder();
		sql.append("select fc.id as id,fc.charge_type_code as chargeTypeCode,fc.charge_type_name as chargeTypeName, ");
		sql.append("fc.cur_code as curCode,fc.emergency_sign as emergencySign,fc.create_time as createTime, ");
		sql.append("fc.area_sign as areaSign,fc.dif_bank as difBank,fc.charge as charge  from FC_CHARGE_TYPE fc where 1=1 ");
		return getDynamicBySQL(input, sql.toString(), FcChargeTypeVo.class,"fc",false);
	}
	/**
	 * 功能介绍:根据费用id查找费用明细<br>
	 * <b>日期：</b>2017年5月25日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<FcChargeTypeDtl> findByChargeTypeDtls(String id){
		 Query query = null;
	     StringBuilder sb = new StringBuilder("from FcChargeTypeDtl fd where fd.chargeTypeId=?0 order by MIN_RANGE_MONEY ASC");
	     query = createQuery(sb.toString(),id);
	     return query.list();
	}
	/**
	 * 功能介绍:根据编码获取费用类型<br>
	 * <b>日期：</b>2017年5月27日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public FcChargeType findByCode(String chargeCode){
		return getUniqueBy("chargeTypeCode", chargeCode);
	}
	
	/**
	 * <b>功能介绍：</b>修改数据判断是否已存在<br>
	 * <b>日期：</b>2017年9月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @param chargeCode
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isExistsByCode(String id,String chargeCode){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("from FcChargeType ft where ft.id != :ID and ft.chargeTypeCode = :chargeCode ");
		params.put("ID", id);
		params.put("chargeCode", chargeCode);
		List list = listByHQL(hql.toString(), params);
		if(EmptyUtils.isEmptyList(list)){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 功能介绍:根据id查找银行类别<br>
	 * <b>日期：</b>2017年5月25日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public List<String> findByBankTypeByChargeId(String id){
		return createSQLQuery("select bank_type from CHARGE_TYPE_BANK_TYPE_REL where CHARGE_TYPE_ID=?", id).list();
	}
	
	/**
	 * <b>功能介绍：</b>根据id查找银行类别名称<br>
	 * <b>日期：</b>2017年9月18日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	// TODO bt_bank_type 属于基础域
	@SuppressWarnings("unchecked")
	/*public List<String> findByBankTypeNameByBankTypeID(List<String> bankTypes){
		return createSQLQuery("select type_name from bt_bank_type where 1=1 and "+getSQLIn(bankTypes, 900, "id")).list();
	}*/
	
	/**
	 * 功能介绍:<br>
	 * 根据费用id删除相关银行类型
	 * <b>日期：</b>2017年5月22日<br>
	 * @version 1.0
	 * @author caozx 
	 */
	public void deleteChargeBankByChargeId(String id){
		createSQLQuery("delete from CHARGE_TYPE_BANK_TYPE_REL where charge_type_id=?",id).executeUpdate();
	}
	
	/**
	 * 功能介绍:删除相关明细<br>
	 * <b>日期：</b>2017年5月23日<br>
	 * @version 1.0
	 * @author caozx 
	 */
	public void deleteChargeDtlsByChargeId(String id){
		createSQLQuery("delete from FC_CHARGE_TYPE_DTL where charge_type_id=?",id).executeUpdate();
	}
	
	/**
	 * 功能介绍:费用是否被引用<br>
	 * <b>日期：</b>2017年5月27日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@SuppressWarnings("unchecked")
	public boolean isUsed(String id){
		List<String> list = createQuery("from ChargeTypeChargeRuleRel where chargeTypeId=?0",id).list();
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * <b>功能介绍:根据id获取费用详情</b><br>
	 * <b>日期：</b>2017年08月24日<br>
	 * <b>作者：</b>yangl<br>
	 * @version 1.0
	 */
	@SuppressWarnings("unchecked")
	public FcChargeTypeVo findById(String id){
		StringBuilder sql = new StringBuilder();
		sql.append("select fc.id as id,fc.charge_type_code as chargeTypeCode,fc.charge_type_name as chargeTypeName, ");
		sql.append("fc.cur_code as curCode,fc.emergency_sign as emergencySign,fc.create_time as createTime, ");
		sql.append("fc.create_by as createBy,fc.update_time as updateTime,fc.update_by as updateBy, ");
		sql.append("fc.area_sign as areaSign,fc.dif_bank as difBank,fc.charge as charge  from FC_CHARGE_TYPE fc where 1=1 ");
		Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(id)) {
        	sql.append(" and fc.id =:id");
            params.put("id", id);
        }
		return (FcChargeTypeVo)getBySQLAliasToBean(sql.toString(), params,FcChargeTypeVo.class);
	}
	
}
