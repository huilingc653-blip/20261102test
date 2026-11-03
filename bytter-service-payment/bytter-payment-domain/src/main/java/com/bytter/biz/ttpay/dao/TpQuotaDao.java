package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpQuota;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author chenf
 * @version 1.0
 * @Title: DAO
 * @Description: 限额信息Dao
 * @date 2022-05-06
 */
@Repository
public class TpQuotaDao extends BaseDao<TpQuota, String> {
    /**
     * 查询限额信息
     *
     * @param dataTablesInput 查询条件
     * @return
     */
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select t.ID as id, t.CORP_ID as corpID, t.MERCHANT_ID as merchantId, t.MERCHANT_NO as merchantNo, t.PAY_WAY as payWay,");
        sql.append(" t.DAY_SINGLE as daySingle, t.DAY_TOTAL as dayTotal, t.MONTH_TOTAL as monthTotal, ");
        sql.append(" t.VALID_SIGN as validSign, t.EFFECT_DATE as effectDate, t.rmk as rmk, ");
        sql.append(" t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate ");
        sql.append(" from TP_QUOTA t ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "t.CORP_ID", params));
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpQuotaVO.class, "", false, params);
    }
    public TpQuotaVO getTpQuotaInfoById(String id) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select t.ID as id, t.CORP_ID as corpID, t.MERCHANT_ID as merchantId, t.MERCHANT_NO as merchantNo, t.PAY_WAY as payWay, ");
        sql.append(" t.DAY_SINGLE as daySingle, t.DAY_TOTAL as dayTotal, t.MONTH_TOTAL as monthTotal, ");
        sql.append(" t.VALID_SIGN as validSign, t.EFFECT_DATE as effectDate, t.rmk as rmk, ");
        sql.append(" t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate ");
        sql.append(" from TP_QUOTA t ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and t.id=:id ");
            params.put("id", id);
        }
        List<TpQuotaVO> list = listBySQLAliasToBean(sql.toString(), params, TpQuotaVO.class);
        if (null == list || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 通过单位、商户号、支付方式 获取限额信息
     *
     * @param corpId     单位id
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @return
     */
    public TpQuotaVO getTpQuotaInfo(String corpId, String merchantNo, String payType) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select t.ID as id, t.CORP_ID as corpID, t.MERCHANT_ID as merchantId, t.MERCHANT_NO as merchantNo, t.PAY_WAY as payWay, ");
        sql.append(" t.DAY_SINGLE as daySingle, t.DAY_TOTAL as dayTotal, t.MONTH_TOTAL as monthTotal, ");
        sql.append(" t.VALID_SIGN as validSign, t.EFFECT_DATE as effectDate, t.rmk as rmk, ");
        sql.append(" t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate ");
        sql.append(" from TP_QUOTA t ");
        sql.append(" where t.VALID_SIGN =1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and t.corp_id=:corpId ");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(merchantNo)) {
            sql.append(" and t.MERCHANT_NO=:merchantNo ");
            params.put("merchantNo", merchantNo);
        }
        if (StringUtils.isNotEmpty(payType)) {
            sql.append(" and t.PAY_WAY=:payWay ");
            params.put("payWay", payType);
        }
        List<TpQuotaVO> list = listBySQLAliasToBean(sql.toString(), params, TpQuotaVO.class);
        if (null == list || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
    public List<TpQuotaVO> getQuotaInfo(TpQuotaVO infoVO) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select t.ID as id, t.CORP_ID as corpID, t.MERCHANT_ID as merchantId, t.MERCHANT_NO as merchantNo, t.PAY_WAY as payWay, ");
        sql.append(" t.DAY_SINGLE as daySingle, t.DAY_TOTAL as dayTotal, t.MONTH_TOTAL as monthTotal, ");
        sql.append(" t.VALID_SIGN as validSign, t.EFFECT_DATE as effectDate, t.rmk as rmk, ");
        sql.append(" t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate ");
        sql.append(" from TP_QUOTA t ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(infoVO.getCorpId())) {
            sql.append(" and t.corp_id=:corpId ");
            params.put("corpId", infoVO.getCorpId());
        }
        if (StringUtils.isNotEmpty(infoVO.getMerchantId())) {
            sql.append(" and t.MERCHANT_ID=:merchantId ");
            params.put("merchantId", infoVO.getMerchantId());
        }
        if (StringUtils.isNotEmpty(infoVO.getPayWay())) {
            sql.append(" and t.PAY_WAY=:payWay ");
            params.put("payWay", infoVO.getPayWay());
        }
        List<TpQuotaVO> list = listBySQLAliasToBean(sql.toString(), params, TpQuotaVO.class);
        return list;
    }
}
