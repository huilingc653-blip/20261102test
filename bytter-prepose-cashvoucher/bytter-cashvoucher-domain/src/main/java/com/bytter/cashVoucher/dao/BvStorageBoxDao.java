package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvStorageBox;
import com.bytter.cashVoucher.vo.BvStorageBoxVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;
/**
 * @Title: Dao
 * @Description: 库箱维护
 * @author onlineGenerator
 * @date 2017-05-25 18:17:27
 * @version 1.0
 *
 */
@Repository
public class BvStorageBoxDao extends BaseDao<BvStorageBox, String> {
	/**
	 * 按条件查询库箱数据
	 * 
	 * @param bvbMap
	 * @return
	 */
	public List<BvStorageBox> listStorageBoxList(Map<String, String> bvbMap) {
		List<Criterion> cList = new ArrayList<Criterion>();
		for (String key : bvbMap.keySet()) {
			Criterion criterion = Restrictions.eq(key, bvbMap.get(key));
			cList.add(criterion);
		}
		Criterion criterion = Restrictions.eq("validSign", "2");
		Criterion criterion2 = Restrictions.not(criterion);
		cList.add(criterion2);
		Criterion[] arry = new Criterion[cList.size()];
		return list(cList.toArray(arry));
	}
	/**
	 * 查询有效的库箱数据(标志为【启用】的)
	 * 
	 * @return
	 */
	public List<BvStorageBox> listStorageBoxByValid(Map<String, String> params) {
		String hql="from BvStorageBox a where a.validSign = :validSign and a.keeperUid = :keeperUid ";
		return this.listByHQL(hql, params);
	}
	/**
	 * 查询库箱维护数据
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataTablesOutput<BvStorageBoxVo> getBvStorageBoxs(DataTablesInput input, List<String> corpAuthList) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.Id as Id, a.code as code,a.name as name,a.corp_id as corpId, ");
		sql.append(" a.keeper_Uid as keeperUid,a.keeper_name as keeperName,a.corp_name as corpName,a.address as address,a.valid_sign as validSign ");
		sql.append(" from bv_storage_box a where a.valid_sign<>:VALID_SIGN ");
		params.put("VALID_SIGN", 2);
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "a.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), BvStorageBoxVo.class, "a", false, params);
	}
	/**
	 * 查询库箱维护详情页（查看）
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataTablesOutput<BvStorageBoxVo> getBvStorageBoxLook(String id) {
		DataTablesInput input = new DataTablesInput();
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.Id as Id, a.code as code,a.name as name,a.rmk as rmk, ");
		sql.append(" a.keeper_Uid as keeperUid,a.keeper_name as keeperName,a.corp_name as corpName,a.address as address,a.valid_sign as validSign ");
		sql.append(" from bv_storage_box a where a.valid_sign<>:VALID_SIGN ");
		if (!EmptyUtils.isEmpty(id)) {
			sql.append(" and a.id = :id");
			params.put("id", id);
		}
		params.put("VALID_SIGN", 2);
		return getDynamicBySQL(input, sql.toString(), BvStorageBoxVo.class, "a", false, params);
	}
	/**
	 * 验证同一单位下是否code也相同，若相同则返回false
	 * 
	 * @param code
	 * @param corpId
	 * @param id
	 * @return
	 */
	public boolean storageBoxVerification(String code, String corpId, String id) {
		StringBuilder sql = new StringBuilder(
				"select id from bv_storage_box where valid_sign<>:VALID_SIGN and code=:code and corp_id=:corpId and id <>:id");
		Map<String, Object> params = new HashMap<>();
		params.put("VALID_SIGN", 2);
		params.put("code", code);
		params.put("corpId", corpId);
		params.put("id", id);
		List<BvStorageBoxVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BvStorageBoxVo.class);
		if (listInfo != null && !listInfo.isEmpty()) {
			return false;
		}
		return true;
	}
	/**
	 * 根据单位id来查找库箱
	 * 
	 * @return
	 */
	public List<BvStorageBoxVo> listStorageBoxByCord(String corpId, String userId) {
		StringBuilder sql = new StringBuilder(
				"select id,corp_id,code,name from bv_storage_box where valid_sign=:VALID_SIGN and  corp_id=:corpId");
		Map<String, Object> params = new HashMap<>();
		params.put("VALID_SIGN", 1);
		params.put("corpId", corpId);
		params.put("keeperUid", userId);
		List<BvStorageBoxVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, BvStorageBoxVo.class);
		return listInfo;
	}
	/**
	 * 根据多个单位id来查找库箱
	 * 
	 * @return
	 */
	public List<BvStorageBox> listStorageBoxByCords(String corpIds, String userId) {
		if (StringUtils.isEmpty(corpIds)) {
			return null;
		}
		StringBuilder hql = new StringBuilder();
		hql.append(" from BvStorageBox where validSign=:validSign ");
		List<String> corpIdArr = Arrays.asList(corpIds.split(","));
		hql.append(" and (" + getSQLIn(corpIdArr, 900, "corpId") + ") ");
		hql.append(" and keeperUid = :keeperUid ");
		Map<String, Object> params = new HashMap<>();
		params.put("validSign", "1");
		params.put("keeperUid", userId);
		return this.listByHQL(hql.toString(), params);
	}
}
