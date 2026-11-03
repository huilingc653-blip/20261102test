package com.bytter.supply.domain.platform.dao;

import com.bytter.supply.domain.platform.entity.ScfProduct;
import com.bytter.supply.domain.platform.vo.ScfProductVo;

import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ScfProductManagerDao
 * @Description 供应链融资产品DAO
 */
@Repository
public class ScfProductDao extends BaseDao<ScfProduct, String> {

    /**
     * 根据平台id查询融资产品列表
     */
    public List<ScfProductVo> getListByPlatformId(String platformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.product_code as productCode, p.product_name as productName, p.product_type as productType, ");
        sql.append(" p.valid_sign as validSign, p.platform_id as platformId ");
        sql.append(" FROM scf_product p WHERE p.platform_id = :platformId and p.valid_sign = '1' ");
        sql.append(" ORDER BY p.product_code ASC ");
        Map<String, Object> params = new HashMap<>();
        params.put("platformId", platformId);
        return listBySQLAliasToBean(sql.toString(), params, ScfProductVo.class);
    }

    /**
     * 根据平台id删除融资产品
     */
    public void deleteByPlatformId(String platformId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" DELETE FROM scf_product WHERE platform_id = :platformId ");
        Map<String, Object> params = new HashMap<>();
        params.put("platformId", platformId);
        batchSQLExecute(sql.toString(), params);
    }

    public List<ScfProductVo> getAllProductJson() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.id as id, p.product_code as productCode, p.product_name as productName, p.product_type as productType, ");
        sql.append(" p.valid_sign as validSign, p.platform_id as platformId ");
        sql.append(" FROM scf_product p WHERE p.valid_sign = '1' ");
        sql.append(" ORDER BY p.product_code ASC ");
        return listBySQLAliasToBean(sql.toString(), null, ScfProductVo.class);
    }
}
