package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondType;
import com.bytter.finance.domain.bond.vo.CmsBondTypeVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 债卷类型Dao
 *
 * @author ouyj
 */
@Repository
public class CmsBondTypeDao extends BaseDao<CmsBondType, String> {
    /**
     * 获取债券类型列表数据
     *
     * @param input
     *            前端参数
     * @return 债券类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, bond_code as bondCode, bond_name as bondName, rmk ");
        sql.append(" from CMS_BOND_TYPE t where t.is_delete = :isDelete or t.is_delete is null");
        // 查询未删除的债券类型
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondTypeVo.class, "", false, null, params);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param id
     *            债券类型id
     * @param bondCode
     *            债券名
     * @param bondName
     *            债券编码
     * @return 是否已存在 true已存在 false不存在
     */
    public boolean isExistType(String id, String bondCode, String bondName) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder(
                "select id from CMS_BOND_TYPE t where (t.is_delete = :isDelete or t.is_delete is null) ");
        sql.append(" and (t.bond_code = :bondCode or t.bond_name = :bondName) ");
        if (!StringUtils.isEmpty(id)) {
            sql.append(" and id <> :id ");
            params.put("id", id);
        }
        // 查询未删除的债券类型
        params.put("isDelete", CmsConstant.IS_FALSE);
        params.put("bondCode", bondCode);
        params.put("bondName", bondName);
        List<CmsBondType> list = listBySQLAliasToBean(sql.toString(), params, CmsBondType.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 判断该债券类型是否被引用
     *
     * @param id
     *            债券类型id
     * @return 是否被引用 true被引用
     */
    public boolean isQuoteType(String id) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder("select t.id from CMS_BOND_REGISTER_INFO r ");
        sql.append(" left join CMS_BOND_TYPE t on t.id = r.type_id where t.id = :id ");
        params.put("id", id);
        List<CmsBondType> list = listBySQLAliasToBean(sql.toString(), params, CmsBondType.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 获取有效的债券类型集合
     *
     * @return 有效的债券类型集合
     */
    public List<CmsBondTypeVo> listValidBondType() {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, bond_code as bondCode, bond_name as bondName ");
        sql.append(" from CMS_BOND_TYPE t where t.is_delete = :isDelete or t.is_delete is null");
        // 查询未删除的债券类型
        params.put("isDelete", CmsConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsBondTypeVo.class);
    }
}
