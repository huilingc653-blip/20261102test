package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpMerchantVar;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: DAO
 * @Description: 商户信息参数Dao
 * @author 郑刚
 * @date 2019-09-26 15:43:05
 * @version 1.0
 */
@Repository
public class TpMerchantVarDao extends BaseDao<TpMerchantVar,String>{
	/**
	 * 根据客商信息id获取对应参数信息
	 * @param mchId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TpMerchantVar> getTpMerchantVarByMchId(String mchId){
		String hql="from TpMerchantVar where mchId=:mchId order by sort ";
		Map<String,Object> params=new HashMap<>();
		params.put("mchId", mchId);
		return this.listByHQL(hql, params);
	}
	/**
	 * 根据客商信息id删除对应参数信息
	 * @param mchId
	 * @return
	 */
	public int deleteTpMerchantVarByMchId(String mchId){
		String sql="delete from tp_merchant_var where mch_Id=:mchId";
		Map<String,Object> params=new HashMap<>();
		params.put("mchId", mchId);
		return this.batchSQLExecute(sql, params);
	}
}
