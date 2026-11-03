package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbIntellectElement;
import com.bytter.bill.domain.bill.vo.AbIntellectElementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * author: liusy
 * date: 2022/10/20 10:30
 * description: 选票要素Dao
 */
@Repository
public class AbIntellectElementDao extends BaseDao<AbIntellectElement, String> {
    /**
     * 获取选票规则组公共列sql
     *
     * @return 选票规则组公共列sql
     */
    public StringBuilder selectCommonColumnSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.ELEMENT_CODE as elementCode, t.ELEMENT_NAME as elementName, t.VALID_SIGN as validSign, ");
        sql.append(" t.SOURCE_TYPE as sourceType, t.SOURCE_NAME as sourceName, t.RMK as rmk, t.DICT_GROUP as dictGroup, ");
        sql.append(" t.DATA_TYPE as dataType, t.TABLE_NAME as tableName, t.FIELD_NAME as fieldName, t.COMPUTE_MODE as computeMode,");
        sql.append(" t.SOURCE_ELEMENT_NAME1 as sourceElementName1, t.SOURCE_ELEMENT_NAME2 as sourceElementName2 ");
        return sql;
    }
    /**
     * 要素列表查询
     * @param input
     * @return
     */
    public DataTablesOutput datatable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from AB_INTELLECT_ELEMENT t ");
        return getDynamicBySQL(input, sql.toString(), AbIntellectElementVO.class, "t", true, null);
    }
    /**
     * 查询所有有效选票要素
     * @return
     */
    public List<AbIntellectElementVO> listElement() {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from AB_INTELLECT_ELEMENT t ");
        sql.append(" where t.VALID_SIGN = :validSign");
        Map params = new HashMap();
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, AbIntellectElementVO.class);
    }
    /**
     * 通过id查询
     * @param id
     * @return
     */
    public AbIntellectElementVO getElementById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.* from AB_INTELLECT_ELEMENT t ");
        //,dictGroup.TYPE_GROUP_NAME dictGroupName
//        sql.append(" left join SYS_DICT_GROUP dictGroup on t.DICT_GROUP = dictGroup.TYPE_GROUP_CODE");
        sql.append(" where ");
        sql.append(" t.id = :id");
        Map params = new HashMap();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectElementVO.class);
    }
    /**
     * 根据编码查询选票要素信息
     *
     * @param id            主键
     * @param code 规则编码
     * @return 选票要素信息
     */
    public AbIntellectElementVO queryByElementCode(String id, String code) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_ELEMENT t ");
        sql.append(" where ELEMENT_CODE = :code");
        Map<String, Object> params = new HashMap<>(2);
        params.put("code", code);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectElementVO.class);
    }
    /**
     * 根据名称查询选票要素信息
     *
     * @param id   主键
     * @param name 规则名称
     * @return 选票规则组数据
     */
    public AbIntellectElementVO queryByElementName(String id, String name) {
        StringBuilder sql = selectCommonColumnSql();
        sql.append(" from AB_INTELLECT_ELEMENT t ");
        sql.append(" where ELEMENT_NAME = :name");
        Map<String, Object> params = new HashMap<>(2);
        params.put("name", name);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and id != :id ");
            params.put("id", id);
        }
        return getBySQLAliasToBean(sql.toString(), params, AbIntellectElementVO.class);
    }
    /**
     * 判断该选票要素是否被引用
     *
     * @param id 选票要素id
     * @return 是否被引用 true被引用
     */
    public boolean isQuote(String id) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder("select t.id from AB_INTELLECT_ELEMENT t ");
        sql.append(" left join AB_INTELLECT_RULE_DTL d on d.element_id = t.id where d.id = :id and d.rule_code is not null ");
        params.put("id", id);
        List<AbIntellectElementVO> list = listBySQLAliasToBean(sql.toString(), params, AbIntellectElementVO.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 查询引用数量
     * @param id
     * @return
     */
    public long getCountBysourceElementName(String id) {
        Criteria c = createCriteria();
        c.add(Restrictions.or(Restrictions.eq("sourceElementName1", id), Restrictions.eq("sourceElementName2", id)));
        return countCriteriaResult(c);
    }
}
