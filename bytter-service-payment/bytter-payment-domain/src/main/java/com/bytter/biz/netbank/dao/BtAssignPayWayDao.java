package com.bytter.biz.netbank.dao;
import java.util.HashMap;
import java.util.List;

import com.bytter.biz.netbank.vo.BtAssignPayWayUserVO;
import org.springframework.stereotype.Repository;
import com.bytter.biz.netbank.entity.BtAssignPayWay;
import com.bytter.biz.netbank.vo.BtAssignPayWayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 付款方式分配表
 * @date 2017-05-17 10:08:00
 */
@Repository
public class BtAssignPayWayDao extends BaseDao<BtAssignPayWay, String> {
	/**
	 * 根据单位ID获取分配的付款方式信息
	 *
	 * @param corpId
	 * @return
	 */
	public List<BtAssignPayWayVO> getByCorpId(String corpId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select p.corpId as corpId,p.voucherCode as voucherCode,f.voucherName as voucherName, ");
		hql.append(" p.defVoucherCode as defVoucherCode,f.sysVoucherCode as sysVoucherCode,d.voucherName as defVoucherName ");
		hql.append(" from BtAssignPayWay p left join FcVoucherType f on f.id = p.voucherCode left join FcVoucherType d on d.id=p.defVoucherCode ");
		hql.append(" where f.validSign = :validSign and p.corpId = :corpId ");
		HashMap<String, String> params = new HashMap<>();
		params.put("validSign", CommonConstant.VALID_SIGN_YES);
		params.put("corpId", corpId);
		hql.append(" order by f.voucherType ");
		return listByHQLAliasToBean(hql.toString(), params, BtAssignPayWayVO.class);
	}
	/**
	 * 查询付款方式分配信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtAssignPayWayVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct c.corp_id as corpId from bt_assign_pay_way c");
		return getDynamicBySQL(dataTablesInput, sql.toString(), BtAssignPayWayVO.class, "c", false);
	}
	/**
	 * 获取已分配付款方式的单位
	 * @return
	 */
	public List<String> getCorpIds() {
		return createSQLQuery("select distinct corp_id from bt_assign_pay_way").list();
	}
	/**
	 * 根据单位统计分配的单位付款方式
	 * @param corpId
	 * @return
	 */
	public int getAssignPayWayCountByCorpId(String corpId){
		String sql="select count(*) from bt_assign_pay_way where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
}
