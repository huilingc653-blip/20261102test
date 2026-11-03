package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.vo.BtChargeBackTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 退单类型表
 * @date 2017-05-17 14:26:13
 */
@Repository
public class BtChargeBackTypeDao extends BaseDao<BtChargeBackType, String> {
	public String getCommonSql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id, b.bus_type as busType, b.bus_type_name as busTypeName, b.bus_type_name as busTypeNameShow, ");
		sql.append("b.is_default as isDefault,b.valid_sign as validSign,b.rmk as rmk,b.create_date as createDate,");
		sql.append("b.create_by as createBy,b.update_date as updateDate,b.update_by as updateBy");
		sql.append(" from BT_CHARGE_BACK_TYPE b where 1=1 ");
		return sql.toString();
	}
	/**
	 * 查询退单类型列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtChargeBackTypeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder(getCommonSql());
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtChargeBackTypeVO.class, "b", false);
	}
	/**
	 * 根据退单ID获取退单信息
	 *
	 * @param id
	 * @return
	 */
	public BtChargeBackType getById(String id) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and  b.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BtChargeBackType.class);
	}
	/**
	 * 根据退单ID获取退单信息
	 *
	 * @param id
	 * @return
	 */
	public BtChargeBackTypeVO getByIdUnion(String id) {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and  b.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BtChargeBackTypeVO.class);
	}
	/**
	 * 根据编码获取退单类型
	 *
	 * @param busType
	 * @return
	 */
	public BtChargeBackType findByCode(String busType) {
		return getUniqueBy("busType", busType);
	}
	/**
	 * 获取有效的退单类型
	 *
	 * @return
	 */
	public List<BtChargeBackTypeVO> getValidChargeBackType() {
		StringBuilder sql = new StringBuilder(this.getCommonSql());
		sql.append(" and  b.valid_sign=:validSign");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("validSign", CommonConstant.VALID_SIGN_YES);
		return listBySQLAliasToBean(sql.toString(),params,BtChargeBackTypeVO.class);
	}
	/**
	 * 获取默认退单类型
	 * @return
	 */
	public BtChargeBackType findByDef() {
		return getUniqueBy("isDefault", "1");
	}
}
