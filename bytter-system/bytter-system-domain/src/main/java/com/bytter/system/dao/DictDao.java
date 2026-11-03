package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.Dict;
import com.bytter.system.entity.DictGroup;
import com.bytter.system.vo.DictVO;
import com.google.common.collect.Maps;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 字典管理Dao
 *
 * @author zhoul
 * @date 2017/5/15
 */
@Repository
public class DictDao extends BaseDao<Dict, String> implements Serializable {
    /**
     * 获取字典菜单列表数据
     *
     * @param tableGridInput
     * @return
     */
    public TableGridResponse listDict(TableGridInput tableGridInput) {
        Map<String, Object> map = Maps.newHashMap();
        StringBuilder hql = new StringBuilder();
        hql.append("select d.id as id,d.name as name,d.code as code,d.dictGroup.groupName as groupName, d.dictGroup.groupCode as groupCode,d.sort as sort");
        hql.append(" from Dict d inner join d.dictGroup where 1=1 ");
        IParameter parameter = tableGridInput.getParameter();
        //搜索组ID
        String dictGroupId = parameter.getPara("dictGroupId");
        if (StringUtils.isNotEmpty(dictGroupId)) {
            hql.append(" and d.dictGroup.id=:dictGroupId ");
            map.put("dictGroupId", dictGroupId);
        }
        //模糊匹配字典组code
        String dictGroupCode = parameter.getPara("dictGroupCode");
        if (StringUtils.isNotEmpty(dictGroupCode)) {
            hql.append(" and d.dictGroup.groupCode like :dictGroupCode ");
            map.put("dictGroupCode", "%" + dictGroupCode + "%");
        }
        //模糊匹配字典名称
        String name = parameter.getPara("name");
        if (StringUtils.isNotEmpty(name)) {
            hql.append(" and d.name like :name ");
            map.put("name", "%" + name + "%");
        }
        hql.append(" order by ");
        String sort = tableGridInput.getSort();
        String para = tableGridInput.getOrder();
        if (StringUtils.isNotEmpty(para)) {
            if (para.startsWith("group")) {
                hql.append(" d.dictGroup.");
            } else {
                hql.append("d.");
            }
            hql.append(para).append(" ").append(sort);
            hql.append(",");
        }
        hql.append("d.dictGroup.id,d.sort");
        Long count = countHqlResult(hql.toString(), map);
        List<DictVO> list = new ArrayList<>();
        if (count > 0) {
            Query query = createQuery(hql.toString(), map);
            query.setFirstResult(tableGridInput.getStart());
            query.setMaxResults(tableGridInput.getRows());
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List multiLangList = query.list();
            list = copyListAndInject(multiLangList, DictVO.class);
        }
        return new TableGridResponse<>(tableGridInput, count, list);
    }
    /**
     * 批量删除
     *
     * @param dictIds
     */
    public void batchDelete(List<String> dictIds) {
        for (int i = 0; i < dictIds.size(); i++) {
            delete(dictIds.get(i));
        }
    }
    //    @Cacheable(cacheNames = "dictListCache")
    public List<DictVO> buildDictJS(String locale) {
//        StringBuilder hql = new StringBuilder();
//        hql.append("select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode");
//        hql.append(" from Dict d inner join d.dictGroup order by d.dictGroup.groupCode,d.sort");
//        return listByHQLAliasToBean(hql.toString(), null, DictVO.class, true);
        String sql = "SELECT d.id as id,bt_nvl_char(ib.LANG_CONTEXT,d.type_name) as name,d.TYPE_CODE as code, dg.TYPE_GROUP_CODE as groupCode FROM " +
                " SYS_DICT d LEFT JOIN SYS_DICT_GROUP dg ON dg.id = d.TYPE_GROUP_ID LEFT JOIN SYS_INTERNATION_BASE ib ON d.id = ib.KEY_ID and ib.LANG_CODE=:locale " +
                "WHERE 1=1 order by d.type_group_id,d.sort ";
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("locale", locale);
        SQLQuery sqlQuery = createSQLQuery(sql, map);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//        sqlQuery.setCacheable(true);
        List list = sqlQuery.list();
        return copyListAndInject(list, DictVO.class);
    }
    /**
     * 根据字典组和字典的值获取字典的名称
     *
     * @param groupCode
     * @param code
     * @return
     */
    public DictVO getDictByDictGroupAndCode(String groupCode, Object code) {
        Map<String, String> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        map.put("code", code.toString());
        StringBuilder hql = new StringBuilder();
        hql.append("select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode");
        hql.append(" from Dict d where d.dictGroup.groupCode =:groupCode and d.code=:code ");
        return getByHQLAliasToBean(hql.toString(), map, DictVO.class, true);
    }
    /**
     * 根据字典组和字典的名称获取字典的值
     *
     * @param groupCode
     * @param name
     * @return
     */
    public DictVO getDictByDictGroupAndName(String groupCode, Object name) {
        Map<String, String> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        map.put("name", name.toString());
        StringBuilder hql = new StringBuilder();
        hql.append("select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode");
        hql.append(" from Dict d where d.dictGroup.groupCode =:groupCode and d.name=:name ");
        return getByHQLAliasToBean(hql.toString(), map, DictVO.class, true);
    }
    public DictVO getDictByDictName(String groupCode, String code) {
        Map<String, String> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        map.put("code", code);
        StringBuilder hql = new StringBuilder();
        hql.append("select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode");
        hql.append(" from Dict d where d.dictGroup.groupCode =:groupCode and d.code=:code ");
        return getByHQLAliasToBean(hql.toString(), map, DictVO.class, true);
    }
    public List<DictVO> getDictByDictGroup(String groupCode) {
        Map<String, String> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        StringBuilder hql = new StringBuilder();
        hql.append("select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode,d.sort as sort ");
        hql.append(" from Dict d where d.dictGroup.groupCode =:groupCode order by d.sort");
        return listByHQLAliasToBean(hql.toString(), map, DictVO.class, true);
    }
    /**
     * 根据字典类型Code获取对应的字典数据。
     *
     * @param groupCode 字典类型Code。
     * @return 字典数据集合。
     */
    public List<DictVO> getDictListByGroupCode(String... groupCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("groupCode", groupCode);
        String hql = "select d.id as id,d.name as name,d.code as code, d.dictGroup.groupCode as groupCode,d.sort as sort " +
                " from Dict d where d.dictGroup.groupCode in(:groupCode) order by d.sort";
        return listByHQLAliasToBean(hql, params, DictVO.class, true);
    }
    public List<String> listDictNameByDictGroup(String groupCode) {
        Map<String, String> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        StringBuilder hql = new StringBuilder();
        hql.append(" select name from Dict d where d.dictGroup.groupCode =:groupCode order by d.sort");
        return listByHQL(hql.toString(), map);
    }
    public List<DictGroup> findList(String sqlWhere) {
        String sql = " select t.id, t.type_group_code as groupCode, t.type_group_name as groupName from sys_dict_group t where 1 = 1";
        return this.listBySQLAliasToBean(sql + sqlWhere, null, DictGroup.class);
    }

    public List<DictVO> getNameByTypeCodesAndTypeGroupCode(List<String> typeCodes, String groupCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.ID as id,a.TYPE_CODE as code,a.TYPE_NAME as name,b.TYPE_GROUP_CODE as groupCode from sys_dict a join sys_dict_group b on a.TYPE_GROUP_ID = b.ID ");
        sql.append(" where a.TYPE_CODE in(:typeCodes) and b.TYPE_GROUP_CODE = :groupCode ");
        Map<String, Object> map = Maps.newHashMap();
        map.put("groupCode", groupCode);
        map.put("typeCodes", typeCodes);
        return listBySQLAliasToBean(sql.toString(),map,DictVO.class);
    }


    /**
     * 根据字典类型Code获取对应的字典数据。
     *
     * @param groupCodes 字典类型Code。
     * @return 字典数据集合。
     */
    public List<DictVO> getDictListByGroupCodes(List<String> groupCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.ID as id,a.TYPE_CODE as code,a.TYPE_NAME as name,b.TYPE_GROUP_CODE as groupCode " +
                "from sys_dict a join sys_dict_group b on a.TYPE_GROUP_ID = b.ID ");
        sql.append(" where b.TYPE_GROUP_CODE in(:groupCode)  ");
        Map<String, Object> map = Maps.newHashMap();
        map.put("groupCode", groupCodes);
        sql.append(" order by a.sort");
        return listBySQLAliasToBean(sql.toString(),map,DictVO.class);
    }
}
