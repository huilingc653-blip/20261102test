package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityType;
import com.bytter.finance.domain.security.vo.CmsSecurityTypeVO;
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
public class CmsSecurityTypeDao extends BaseDao<CmsSecurityType, String> {
    /**
     * 获取资产证券化类型表数据
     *
     * @param input
     *            前端参数
     * @return 资产证券化类型表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, type_code as typeCode, type_name as typeName, rmk ");
        sql.append(" from CMS_SECURITY_TYPE t where t.status = :status ");
        // 状态为0
        params.put("status", SecurityConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsSecurityTypeVO.class, "", false, null, params);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param id
     *            资产证券化类型id
     * @param typeCode
     *            资产证券化类型编码
     * @param typeName
     *            资产证券化类型名称
     * @return 是否已存在 true已存在 false不存在
     */
    public boolean isExistType(String id, String typeCode, String typeName) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder("select id from CMS_SECURITY_TYPE t where t.status = :status ");
        sql.append(" and (t.type_code = :typeCode or t.type_name = :typeName) ");
        if (!StringUtils.isEmpty(id)) {
            sql.append(" and id <> :id ");
            params.put("id", id);
        }
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        params.put("typeCode", typeCode);
        params.put("typeName", typeName);
        List<CmsSecurityType> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityType.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 获取有效的资产证券化类型集合
     *
     * @return 有效的资产证券化类型集合
     */
    public List<CmsSecurityTypeVO> listSecurityType() {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, type_code as typeCode, type_name as typeName, rmk ");
        sql.append(" from CMS_SECURITY_TYPE t where t.status = :status ");
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsSecurityTypeVO.class);
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsSecurityTypeVO getTypeById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_TYPE t");
        sql.append(" where t.id=:id");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsSecurityTypeVO.class);
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
        sql.append(" left join CMS_SECURITY_TYPE t on t.id = r.SECURITY_TYPE_ID where t.id = :id ");
        params.put("id", id);
        List<CmsSecurityTypeVO> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityTypeVO.class);
        return !CollectionUtils.isEmpty(list);
    }
}
