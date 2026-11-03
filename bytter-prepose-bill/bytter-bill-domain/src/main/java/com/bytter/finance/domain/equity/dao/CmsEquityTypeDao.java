package com.bytter.finance.domain.equity.dao;
import com.bytter.finance.domain.equity.entity.CmsEquityType;
import com.bytter.finance.domain.equity.vo.CmsEquityTypeVo;
import com.bytter.finance.constant.EquityConstant;
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
 * 融资租赁类型dao
 *
 * @author wangf
 */
@Repository
public class CmsEquityTypeDao extends BaseDao<CmsEquityType, String> {
    /**
     * 获取融资租赁类型列表数据
     *
     * @param input
     *            前端参数
     * @return 融资租赁类型列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, type_code as typeCode, type_name as typeName, rmk ");
        sql.append(" from CMS_EQUITY_TYPES t where t.status = :status ");
        // 状态为1
        params.put("status", EquityConstant.IS_TRUE);
        return getDynamicBySQL(input, sql.toString(), CmsEquityTypeVo.class, "", false, null, params);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param id
     *            融资租赁类型id
     * @param typeCode
     *            融资租赁类型编码
     * @param typeName
     *            融资租赁类型名称
     * @return 是否已存在 true已存在 false不存在
     */
    public boolean isExistType(String id, String typeCode, String typeName) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder("select id from CMS_EQUITY_TYPES t where t.status = :status ");
        sql.append(" and (t.type_code = :typeCode or t.type_name = :typeName) ");
        if (!StringUtils.isEmpty(id)) {
            sql.append(" and id <> :id ");
            params.put("id", id);
        }
        // 状态为1
        params.put("status", EquityConstant.IS_TRUE);
        params.put("typeCode", typeCode);
        params.put("typeName", typeName);
        List<CmsEquityType> list = listBySQLAliasToBean(sql.toString(), params, CmsEquityType.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 获取有效的融资租赁类型集合
     *
     * @return 有效的融资租赁类型集合
     */
    public List<CmsEquityTypeVo> listEquityType() {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, type_code as typeCode, type_name as typeName, rmk ");
        sql.append(" from CMS_EQUITY_TYPES t where t.status = :status ");
        // 状态为1
        params.put("status", EquityConstant.IS_TRUE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsEquityTypeVo.class);
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsEquityTypeVo getTypeByCode(String typeCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_EQUITY_TYPES t");
        sql.append(" where t.type_code=:typeCode");
        sql.append(" and t.status = :status");
        sql.append(" order by t.create_date desc ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("typeCode", typeCode);
        params.put("status", EquityConstant.IS_TRUE);
        List<CmsEquityTypeVo> list = listBySQLAliasToBean(sql.toString(), params, CmsEquityTypeVo.class);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }
}
