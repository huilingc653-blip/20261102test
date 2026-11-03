package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityAssetType;
import com.bytter.finance.domain.security.vo.CmsSecurityAssetTypeVO;
import com.bytter.finance.constant.SecurityConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class CmsSecurityAssetTypeDao extends BaseDao<CmsSecurityAssetType, String> {
    /**
     * 获取基础资产类型表数据
     *
     * @param input
     *            前端参数
     * @return 基础资产类型表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder(
                "select id, asset_type_code as assetTypeCode, asset_type_name as assetTypeName, rmk ");
        sql.append(" from CMS_SECURITY_ASSET_TYPE t where t.status = :status ");
        // 状态为0
        params.put("status", SecurityConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsSecurityAssetTypeVO.class, "", false, null, params);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param id
     *            基础资产类型id
     * @param assetTypeCode
     *            基础资产类型编码
     * @param assetTypeName
     *            基础资产类型名称
     * @return 是否已存在 true已存在 false不存在
     */
    public boolean isExistType(String id, String assetTypeCode, String assetTypeName) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder("select id from CMS_SECURITY_ASSET_TYPE t where t.status = :status ");
        sql.append(" and (t.asset_type_code = :assetTypeCode or t.asset_type_name = :assetTypeName) ");
        if (!StringUtils.isEmpty(id)) {
            sql.append(" and id <> :id ");
            params.put("id", id);
        }
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        params.put("assetTypeCode", assetTypeCode);
        params.put("assetTypeName", assetTypeName);
        List<CmsSecurityAssetType> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityAssetType.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 获取有效的基础资产类型集合
     *
     * @return 有效的基础资产类型集合
     */
    public List<CmsSecurityAssetTypeVO> listSecurityAssetType() {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder(
                "select id, asset_type_code as assetTypeCode, asset_type_name as assetTypeName, rmk ");
        sql.append(" from CMS_SECURITY_ASSET_TYPE t where t.status = :status ");
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsSecurityAssetTypeVO.class);
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsSecurityAssetTypeVO getAssetTypeByCode(String assetTypeCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_ASSET_TYPE t");
        sql.append(" where t.asset_type_code=:assetTypeCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assetTypeCode", assetTypeCode);
        return getBySQLAliasToBean(sql.toString(), params, CmsSecurityAssetTypeVO.class);
    }
    /**
     * 判断该类型是否被引用
     *
     * @param id
     *            id
     * @return 是否被引用 true被引用
     */
    public boolean isQuoteType(String id) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder("select t.id from CMS_SECURITY_INFO r ");
        sql.append(" left join CMS_SECURITY_ASSET_TYPE t on t.ASSET_TYPE_CODE = r.ASSET_TYPE_CODE where t.id = :id ");
        params.put("id", id);
        List<CmsSecurityAssetTypeVO> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityAssetTypeVO.class);
        return !CollectionUtils.isEmpty(list);
    }
}
