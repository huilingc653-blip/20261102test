package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpBussiness;
import com.bytter.biz.ttpay.vo.TpBussinessTextVO;
import com.bytter.biz.ttpay.vo.TpBussinessVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class TpBussinessDao extends BaseDao<TpBussiness, String> {
    public DataTablesOutput getQueryDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ID id,t.CODE code,s.SHOPTYPE shoptypeName,t.SHOP_NAME shopName,t.STATUS status,t.IS_FLAG isFlag");
        sql.append(" from TP_BUSINESS t left join TP_SHOPTYPE s on t.SHOPTYPE=s.id where t.STATUS<>-2");
        Map<String, Object> params = new HashMap<String, Object>();
        return getDynamicBySQL(input, sql.toString(), TpBussinessVO.class, "t", false, params);
    }
    public TpBussinessVO getTpBussiness(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ID id,t.CODE code,t.SHOPTYPE shopType,s.SHOPTYPE as shoptypeName,t.SHOP_NAME shopName,t.STATUS status,t.IS_FLAG isFlag,");
        sql.append(" t.PROVINCE province,t.ADDRESS address,t.CONTACT_MAN contactMan,t.TEL tel");
        sql.append(" from TP_BUSINESS t  left join TP_SHOPTYPE s on t.SHOPTYPE=s.id where t.STATUS<>-2 and t.id=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, TpBussinessVO.class);
    }
    public List<TpBussinessTextVO> getTpBussinessTexts(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.ID id,t.PAY_TYPE payType,t.LOGIN_NO loginNo, t.PHONE phone,t.EMAIL email,t.RMK rmk,t.PAYEE_MODE payeeMode");
        sql.append(" from TP_BUSINESS_TEXT t   where t.STATUS<>-2 and t.TP_BUSINESS_ID=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, TpBussinessTextVO.class);
    }
    public boolean checkUnion(TpBussinessVO tpBussinessVO) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.*");
        sql.append(" from  TP_BUSINESS t where STATUS<>-2 ");
        Map<String, String> params = new HashMap<String, String>();
        sql.append(" and t.CODE=:code");
        params.put("code", tpBussinessVO.getCode());
        if (!EmptyUtils.isEmpty(tpBussinessVO.getId())) {//编辑
            sql.append(" and t.CODE<>:updateCode");
            params.put("updateCode", this.get(tpBussinessVO.getId()).getCode());//校验排除自身的code
        }
        List<TpBussinessVO> tpBussinessVOS=listBySQLAliasToBean(sql.toString(), params, TpBussinessVO.class);
        if (tpBussinessVOS.size() < 1) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * 第三方支付：查询收款方列表
     * @param corpId 单位id
     * @param payType 支付方式（微信、支付宝）
     * @return 收款商户号列表
     */
    public List<TpBussinessTextVO> getBussinessinfo(String corpId,String payType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select b.shop_name shopName,st.shoptype shopType,t.login_no loginNo,t.phone,t.email,t.pay_Type as payType,t.payee_mode as payeeMode ");
        sql.append("from tp_business_text t ");
        sql.append("left join tp_business b on t.tp_business_id = b.id ");
        sql.append("left join tp_business_corp c on b.id = c.business_id ");
        sql.append("left join tp_shoptype st on b.shoptype = st.id ");
        sql.append("where b.status=95 and t.status=95 ");
        if(StringUtils.isNotEmpty(corpId)){
            sql.append("and c.corp_id =:corpId ");
            params.put("corpId", corpId);
        }
        if(StringUtils.isNotEmpty(payType)){
            sql.append("and t.pay_Type = :payType ");
            params.put("payType", payType);
        }
        return listBySQLAliasToBean(sql.toString(), params, TpBussinessTextVO.class);
    }
}
