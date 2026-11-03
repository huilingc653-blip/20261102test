package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.ChoProduct;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoProductDao extends BaseDao<ChoProduct,String> {
    public DataTablesOutput<ChoProductVo> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id,p.bank_type_id as bankTypeId, p.bank_type_name as bankTypeName, p.product_type as productType,p.product_code as productCode, ");
        sql.append(" p.product_name as productName, p.cur_id as curId, p.cur_name as curName, p.rmk as rmk, p.FINANCIAL_TYPE as financialType, p.CERTIFICATE_WAY as certificateWay ");
        sql.append(" from cho_product_type p where p.valid_sign = 1 ");
        return getDynamicBySQL(input, sql.toString(), ChoProductVo.class, "p", false, null);
    }
    /**
     * 根据产品代码查找产品对象
     * @param code
     * @return
     */
    public ChoProduct findProductByCode(String code){
        String hql = "from ChoProduct where product_code=:productCode";
        Map<String,Object> map = new HashMap<>();
        map.put("productCode",code);
        return (ChoProduct) this.getSession().createQuery(hql).setProperties(map).uniqueResult();
    }
    /**
     * 根据产品类型和银行类型获取产品列表
     * @param productType
     * @param bankType
     * @return
     */
    public List<ChoProduct> getProductsByType(String productType, String bankType) {
        String hql = "from ChoProduct where product_type=:productType and bank_type_id = :bankType and VALID_SIGN = '1' ";
        Map<String,Object> map = new HashMap<>();
        map.put("productType",productType);
        map.put("bankType",bankType);
        return this.getSession().createQuery(hql).setProperties(map).list();
    }
    /**
     * 查询所有状态为有效的产品
     * @return
     */
    public List<ChoProductVo> listValidSignAll(String financialType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id,p.bank_type_id as bankTypeId, p.bank_type_name as bankTypeName, p.product_type as productType,p.product_code as productCode, ");
        sql.append(" p.product_name as productName, p.cur_id as curId, p.cur_name as curName, p.rmk as rmk, p.FINANCIAL_TYPE as financialType, p.CERTIFICATE_WAY as certificateWay ");
        sql.append(" from CHO_PRODUCT_TYPE p where VALID_SIGN = '1'  ");
        if (StringUtils.isNotBlank(financialType)) {
            sql.append(" and FINANCIAL_TYPE = :financialType ");
        }
        sql.append(" order by product_code, product_name ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("financialType", financialType);
        return this.listBySQLAliasToBean(sql.toString(), params, ChoProductVo.class);
    }


    /**
     * 查询所有状态为有效的产品
     * @return
     */
    public List<ChoProductVo> listValidSignAll(String financialType, String productType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id,p.bank_type_id as bankTypeId, p.bank_type_name as bankTypeName, p.product_type as productType,p.product_code as productCode, ");
        sql.append(" p.product_name as productName, p.cur_id as curId, p.cur_name as curName, p.rmk as rmk, p.FINANCIAL_TYPE as financialType, p.CERTIFICATE_WAY as certificateWay ");
        sql.append(" from CHO_PRODUCT_TYPE p where VALID_SIGN = '1'  ");
        if (StringUtils.isNotBlank(financialType)) {
            sql.append(" and FINANCIAL_TYPE = :financialType ");
        }
        if (StringUtils.isNotBlank(productType)) {
            sql.append(" and product_type = :productType ");
        }
        sql.append(" order by product_code, product_name ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("financialType", financialType);
        return this.listBySQLAliasToBean(sql.toString(), params, ChoProductVo.class);
    }



    /**
     * 根据产品代码获取有效产品
     *
     * @param productCode  产品代码
     * @return 产品类型
     */
    public ChoProductVo getByCode(String productCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id, VALID_SIGN as validSign, is_approve as isApprove from CHO_PRODUCT_TYPE p where product_code = :productCode ");
        sql.append(" and (VALID_SIGN = :validSign1 or (VALID_SIGN = :validSign0 and is_approve = :validSign0)) ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("productCode", productCode);
        params.put("validSign1", CommonConstant.VALID_SIGN_YES);
        params.put("validSign0", CommonConstant.VALID_SIGN_NO);
        List<ChoProductVo> choProductList = this.listBySQLAliasToBean(sql.toString(), params, ChoProductVo.class);
        return CollectionUtils.isEmpty(choProductList) ? null : choProductList.get(0);
    }
    /**
     * 校验是否存在产品代码、产品类型、产品名称、银行类别、币种一致的产品类型
     *
     * @param choProductReq 理财登记vo
     * @return 产品类型
     */
    public ChoProductVo checkIsRepeat(ChoProductReq choProductReq) {
        StringBuilder sql = new StringBuilder();
        sql.append("select id from CHO_PRODUCT_TYPE p where product_code = :productCode and product_type = :productType");
        sql.append(" and product_name = :productName and FINANCIAL_TYPE = :financialType and cur_id = :curId and bank_type_id = :bankTypeId ");
        sql.append(" and (VALID_SIGN = :validSign1 or (VALID_SIGN = :validSign0 and is_approve = :validSign0)) ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("productCode", choProductReq.getProductCode());
        params.put("productName", choProductReq.getProductName());
        params.put("productType", choProductReq.getProductType());
        params.put("financialType", choProductReq.getFinancialType());
        params.put("curId", choProductReq.getCurId());
        params.put("bankTypeId", choProductReq.getBankTypeId());
        params.put("validSign1", CommonConstant.VALID_SIGN_YES);
        params.put("validSign0", CommonConstant.VALID_SIGN_NO);
        List<ChoProductVo> choProductList = this.listBySQLAliasToBean(sql.toString(), params, ChoProductVo.class);
        return CollectionUtils.isEmpty(choProductList) ? null : choProductList.get(0);
    }
    /**
     * 根据产品代码删除产品类型
     *
     * @param productCode 产品代码
     */
    public void delByCode(String productCode) {
        StringBuilder sql = new StringBuilder("delete from CHO_PRODUCT_TYPE where product_code = :productCode");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("productCode", productCode);
        this.batchSQLExecute(sql.toString(), params);
    }
}
