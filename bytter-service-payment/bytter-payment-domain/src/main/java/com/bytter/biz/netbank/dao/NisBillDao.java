package com.bytter.biz.netbank.dao;
import java.util.HashMap;
import java.util.List;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.vo.NisBillDataVo;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.core.base.BaseDao;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 付款票据关联表
 * @date 2017-05-23 15:20:33
 */
@Repository
public class NisBillDao extends BaseDao<NisBill, String> {

	/**
	 * 根据付款ID获取付款票据关联信息
	 * @param payId
	 * @return
	 */
	public List<NisBill> listByPayId(String payId){
		this.flush();
		this.clear();
		String hql = " from NisBill n where n.payId = :payId";
		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payId", payId);
        return (List<NisBill>)this.getSession().createQuery(hql).setProperties(params).list();
	}

	/**
	 * 根据付款单ID集合批量查询交易时间。
	 *
	 * @param payIdList 付款单ID集合。
	 * @return 数据集合。
	 */
	public List<NisBillDataVo> listDataByPayId(List<String> payIdList){
		if (ObjectUtil.isEmpty(payIdList)){
			return ListUtil.empty();
		}
		String sql = "select nb.pay_id as payId,nb.bill_id as billId,aa.business_date as businessDate " +
				" from nis_bill nb left join AB_ACCRECORDS aa on nb.bill_id = aa.id " +
				" where "+ SqlUtil.getInSql(payIdList,"nb.pay_id",true) + " and aa.BUSINESS_DATE is not null";
		return this.listBySQLAliasToBean(sql,null,NisBillDataVo.class);
	}

	/**
	 * 根据付款单id和票据流水id删除
	 *
	 * @param billIds 票据流水id
	 * @param payIds  付款单id
	 */
	public void delByPayAndBillId(String[] billIds, String[] payIds) {
		String sql = "delete from nis_bill where bill_id in (:billIds) and pay_id in (:payIds)";
		HashMap<String, Object> params = new HashMap<>(2);
		params.put("billIds", billIds);
		params.put("payIds", payIds);
		this.batchSQLExecute(sql, params);
	}
	/**
	 * 根据票据ID获取付款票据关联信息
	 * @param billId 票据id
	 * @param payId 付款表ID
	 * @return
	 */
	public NisBill getByBillId(String billId,String payId){
		String hql = " from NisBill n where n.billId = :billId and n.payId=:payId";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("billId", billId);
		params.put("payId", payId);
		return (NisBill)this.getSession().createQuery(hql).setProperties(params).uniqueResult();
	}

	/**
	 * 根据票据ID获取付款票据关联信息
	 * @param billId 票据id
	 * @return
	 */
	public NisBill getByBillId(String billId){
		String hql = " from NisBill n where n.billId = :billId ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("billId", billId);
		return (NisBill)this.getSession().createQuery(hql).setProperties(params).uniqueResult();
	}

	/**
	 * 根据旧billId修改新的billId
	 *
	 * @param billIdOld 旧票据流水id
	 * @param billIdNew 新票据流水id
	 * @return
	 */
	public void updateNisBillByBillId(String billIdOld, String billIdNew){
		if(StrUtil.isEmpty(billIdOld)||StrUtil.isEmpty(billIdNew)){
			return;
		}
		String sql = "update nis_bill set bill_id=:billIdNew where bill_id=:billIdOld";
		HashMap<String, Object> params = new HashMap<>(2);
		params.put("billIdOld", billIdOld);
		params.put("billIdNew", billIdNew);
		this.batchSQLExecute(sql, params);
	}

	/**
	 * 根据付款Ids删除关联信息
	 * @param payIds
	 */
	public void delByPayIds(List<String> payIds) {
		String sql = "delete from nis_bill where pay_id in (:payIds)";
		HashMap<String, Object> params = new HashMap<>(1);
		params.put("payIds", payIds);
		this.batchSQLExecute(sql, params);
	}

    public List<NisBillhead> listByRefundOrgId(String refundOrgId) {
		String hql = "from NisBillhead where refundOrgId = :refundOrgId  and status <> :status ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("refundOrgId", refundOrgId);
		params.put("status", NetbankConstant.STATUE_DEL);
		return listByHQL(hql, params);
    }
}
