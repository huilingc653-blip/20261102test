package com.bytter.biz.netbank.dao;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bytter.biz.netbank.entity.BtAssignPayWayUser;
import com.bytter.biz.netbank.vo.BtAssignPayWayUserVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 付款方式用户分配表
 * @date 2017-05-17 10:08:00
 */
@Repository
public class BtAssignPayWayUserDao extends BaseDao<BtAssignPayWayUser, String> {
	/**
	 * 根据用户id获取分配的付款方式信息
	 *
	 * @param userId
	 * @return
	 */
	public List<BtAssignPayWayUserVO> getByUserId(String userId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select p.userId as userId,p.voucherCode as voucherCode,f.voucherName as voucherName, ");
		hql.append(" f.sysVoucherCode as sysVoucherCode ");
		hql.append(" from BtAssignPayWayUser p,FcVoucherType f where f.id = p.voucherCode ");
		hql.append(" and f.validSign = :validSign and p.userId = :userId ");
		HashMap<String, String> params = new HashMap<>();
		params.put("validSign", CommonConstant.VALID_SIGN_YES);
		params.put("userId", userId);
		hql.append(" order by f.voucherType ");
		return listByHQLAliasToBean(hql.toString(), params, BtAssignPayWayUserVO.class);
	}
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataByUserId(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from BT_ASSIGN_PAY_WAY_USER");
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtAssignPayWayUserVO.class, "c", false);
	}
	
	/**
	 * 查询付款方式分配信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
//	@SuppressWarnings("rawtypes")
//	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select c.id as userId  from sys_user c ");
//		sql.append(" where c.id in (select user_id from BT_ASSIGN_PAY_WAY_USER) ");
//		return getDynamicBySQL(dataTablesInput, sql.toString(), BtAssignPayWayUserVO.class, "c", false);
//	}
	
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtAssignPayWayUserVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select c.id as userId,c.login_name as loginName,c.username as userName  from sys_user c ");
		sql.append(" where c.id in (select user_id from BT_ASSIGN_PAY_WAY_USER) ");
		//支付方式
		String voucherTypeId = (String) dataTablesInput.getFormdata().get("ins_voucherTypeId");
		if (StringUtils.isNotBlank(voucherTypeId)) {
			sql.append(" and c.id in (select user_id from BT_ASSIGN_PAY_WAY_USER where " + SqlUtil.getInSql(voucherTypeId.split(CommonConstant.COMMA_SPLIT), "voucher_Code", true) + ")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtAssignPayWayUserVO.class, "c", false);
	}
}
