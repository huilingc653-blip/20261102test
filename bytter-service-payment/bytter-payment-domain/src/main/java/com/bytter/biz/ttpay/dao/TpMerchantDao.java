package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpMerchant;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author 郑刚
 * @version 1.0
 * @Title: DAO
 * @Description: 商户信息Dao
 * @date 2019-09-26 15:43:05
 */
@Repository
public class TpMerchantDao extends BaseDao<TpMerchant, String> {
//
//	/**
//	 *
//	 * @Title: 商户信息信息查询列表
//	 * @param  input
//	 * @return DataTablesOutput    返回类型
//	 * @throws
//	 */
//	@SuppressWarnings("rawtypes")
//	public DataTablesOutput getDataTablesUnion(DataTablesInput input){
//		StringBuilder sql=new StringBuilder("select ");
//		sql.append(" m.ID,m.parent_Id as parentId,c.code as corpCode,c.name as corpName,m.PAY_TYPE as PAYTYPE,m.is_Online as isOnline,m.rmk ");
//		sql.append(" from TP_MERCHANT m ");
//		sql.append(" left join sys_corp c on c.id=m.corp_id ");
//		sql.append(" where 1=1 ");
//		Map<String,Object> params=new HashMap<String,Object>();
//		return getDynamicBySQL(input, sql.toString(),TpMerchantVO.class,"", false,params);
//	}
    /**
     * @param params       查询参数
     * @param corpAuthList 有权限单位
     * @return list    返回类型
     * @throws
     * @Title: 商户信息信息查询列表
     */
    public List<TpMerchantVO> getTpMerchantVOList(Map<String, Object> params, List<String> corpAuthList) {
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" m.ID,m.parent_Id as parentId,m.corp_id as corpId,m.merchant_No as merchantNo,m.PAY_TYPE as PAYTYPE,m.is_Online as isOnline,m.rmk ");
        sql.append(" from TP_MERCHANT m ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1  and m.valid_sign=:validSign ");
        if (corpAuthList == null || corpAuthList.size() < 1) {
            return null;
        }
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "m.corp_id", params));
        if (params.containsKey("corpIds")) {
            sql.append(" and m.corp_id in (:corpIds) ");
        }
        if (params.containsKey("payTypes")) {
            sql.append(" and m.pay_type in (:payTypes) ");
        }
        if (params.containsKey("merchantNo")) {
            sql.append(" and m.merchant_No like :merchantNo ");
        }
        sql.append(" order by bt_nvl_num(m.sort,0),m.merchant_No ");
        return listBySQLAliasToBean(sql.toString(), params, TpMerchantVO.class);
    }
    /**
     * @param merchantNo
     * @return payTyte    返回类型
     * @throws
     * @Title: 商户信息信息
     */
    public TpMerchantVO getTpMerchantVOByMerchantNoAndPayType(String merchantNo, String payTyte) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" m.ID,m.parent_Id as parentId,m.corp_id as corpId,m.merchant_No as merchantNo,m.PAY_TYPE as PAYTYPE,m.is_Online as isOnline,m.rmk ");
        sql.append(" from TP_MERCHANT m ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1 and m.valid_sign=:validSign ");
        if (StringUtils.isEmpty(merchantNo) || StringUtils.isEmpty(payTyte)) {
            return null;
        }
        sql.append(" and m.merchant_No =:merchantNo ");
        sql.append(" and m.pay_type =:payTyte ");
        Map<String, Object> params = new HashMap<>();
        params.put("merchantNo", merchantNo);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        params.put("payTyte", payTyte);
        List<TpMerchantVO> list = listBySQLAliasToBean(sql.toString(), params, TpMerchantVO.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 根据客商信息id获取对应子账户信息
     *
     * @param mchId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TpMerchant> getTpMerchantByMchId(String mchId) {
        String hql = "from TpMerchant where validSign=:validSign and parentId=:mchId order by sort ";
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        return this.listByHQL(hql, params);
    }
    /**
     * 根据商户编号获取商户信息
     *
     * @param merchantNo 商户号
     * @return
     */
    @SuppressWarnings("unchecked")
    public TpMerchant getTpMerchant(String merchantNo) {
        String hql = "from TpMerchant where validSign=:validSign and merchantNo=:merchantNo";
        Map<String, Object> params = new HashMap<>();
        params.put("merchantNo", merchantNo);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        List<TpMerchant> merchant = this.listByHQL(hql.toString(), 0, 1, params);
        if (EmptyUtils.isEmptyList(merchant)) {
            return null;
        }
        return merchant.get(0);
    }
    /**
     * 根据客商信息id删除对应子账户信息
     *
     * @param mchId
     * @return
     */
    public int deleteTpMerchantByMchId(String mchId) {
        String sql = "update TP_MERCHANT set valid_sign=:validSign where parent_Id=:mchId";
        Map<String, Object> params = new HashMap<>();
        params.put("mchId", mchId);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN0);
        return this.batchSQLExecute(sql, params);
    }
    /**
     * 判断相同缴款方式下，是否merchantNo商户号是否有相同的维护
     *
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @param mchId      商户ID
     * @return true不存在重复的信息 false存在重复的信息
     */
    public boolean merchantNoCheck(String merchantNo, String payType, String mchId) {
        String hql = "from TpMerchant where merchantNo=:merchantNo and payType=:payType and validSign=:validSign";
        Map<String, Object> params = new HashMap<>();
        params.put("merchantNo", merchantNo);
        params.put("payType", payType);
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if (StringUtils.isNotEmpty(mchId)) {
            hql += " and id!=:mchId ";
            params.put("mchId", mchId);
        }
        @SuppressWarnings("unchecked")
        List<TpMerchant> list = this.listByHQL(hql, params);
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }
    public List<TpMerchantVO> getByList(String corpId, String payType) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" m.ID,m.parent_Id as parentId,m.corp_id as corpId,m.merchant_No as merchantNo,m.PAY_TYPE as PAYTYPE,m.is_Online as isOnline,m.rmk ");
        sql.append(" from TP_MERCHANT m ");
        //sql.append(" left join sys_corp c on c.id=m.corp_id ");
        sql.append(" where 1=1  and m.valid_sign=:validSign ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", TtPayConstant.TTPAY_VALIDSIGN1);
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and m.corp_id =:corpId ");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(payType)) {
            sql.append(" and m.pay_type = :payType ");
            params.put("payType", payType);
        }
        return listBySQLAliasToBean(sql.toString(), params, TpMerchantVO.class);
    }
}
