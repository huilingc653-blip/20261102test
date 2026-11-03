package com.bytter.finance.domain.security.dao;
import com.bytter.finance.domain.security.entity.CmsSecurityLevel;
import com.bytter.finance.domain.security.vo.CmsSecurityLevelVO;
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
public class CmsSecurityLevelDao extends BaseDao<CmsSecurityLevel, String> {
    /**
     * 获取资产证券化层级表数据
     *
     * @param input
     *            前端参数
     * @return 资产证券化层级表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, level_code as levelCode, level_name as levelName, rmk ");
        sql.append(" from CMS_SECURITY_LEVEL l where l.status = :status ");
        // 状态为0
        params.put("status", SecurityConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsSecurityLevelVO.class, "", false, null, params);
    }
    /**
     * 判断编码或名称是否已存在
     *
     * @param id
     *            资产证券化层级id
     * @param levelCode
     *            资产证券化层级编码
     * @param levelName
     *            资产证券化层级名称
     * @return 是否已存在 true已存在 false不存在
     */
    public boolean isExistLevel(String id, String levelCode, String levelName) {
        Map<String, Object> params = new HashMap<>(4);
        StringBuilder sql = new StringBuilder("select id from CMS_SECURITY_LEVEL l where l.status = :status ");
        sql.append(" and (l.level_code = :levelCode or l.level_name = :levelName) ");
        if (!StringUtils.isEmpty(id)) {
            sql.append(" and id <> :id ");
            params.put("id", id);
        }
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        params.put("levelCode", levelCode);
        params.put("levelName", levelName);
        List<CmsSecurityLevel> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityLevel.class);
        return !CollectionUtils.isEmpty(list);
    }
    /**
     * 获取有效的资产证券化层级集合
     *
     * @return 有效的资产证券化层级集合
     */
    public List<CmsSecurityLevelVO> listSecurityLevel() {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = new StringBuilder("select id, level_code as levelCode, level_name as levelName, rmk ");
        sql.append(" from CMS_SECURITY_LEVEL l where l.status = :status ");
        // 状态为1
        params.put("status", SecurityConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsSecurityLevelVO.class);
    }
    /**
     * 根据code获取到对象
     *
     * @return 对应code得类型对象
     */
    public CmsSecurityLevelVO getLevelByCode(String levelCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_LEVEL l");
        sql.append(" where l.level_code=:levelCode");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("levelCode", levelCode);
        return getBySQLAliasToBean(sql.toString(), params, CmsSecurityLevelVO.class);
    }
    /**
     * 根据名称获取到对象
     *
     * @return 对应名称得类型对象
     */
    public CmsSecurityLevelVO getLevelByName(String levelName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from CMS_SECURITY_LEVEL l");
        sql.append(" where l.level_name=:levelName");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("levelName", levelName);
        return getBySQLAliasToBean(sql.toString(), params, CmsSecurityLevelVO.class);
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
        StringBuilder sql = new StringBuilder("select t.id from CMS_SECURITY_LEVEL_INFO r ");
        sql.append(" left join CMS_SECURITY_LEVEL t on t.id = r.LEVEL_ID where t.id = :id ");
        params.put("id", id);
        List<CmsSecurityLevelVO> list = listBySQLAliasToBean(sql.toString(), params, CmsSecurityLevelVO.class);
        return !CollectionUtils.isEmpty(list);
    }
}
