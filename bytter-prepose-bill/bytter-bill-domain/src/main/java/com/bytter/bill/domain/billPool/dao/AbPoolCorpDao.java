package com.bytter.bill.domain.billPool.dao;
import com.bytter.bill.domain.billPool.entity.AbPoolCorp;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: AbPoolCorpDao
 * @Description: 票据池单位Dao
 * @author wangzy
 * @date 2019/12/30 15:19
 *
 */
@Repository
public class AbPoolCorpDao extends BaseDao<AbPoolCorp, String> {
    /**
     * 票据池单位查询
     * @param params 查询条件
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public List<AbPoolCorpVO> listAbPoolCorpVO(Map<String,Object> params) {
        StringBuilder sql = new StringBuilder("select ");
        //sql.append(" p.id as id,s.code as corpCode,s.name as corpName,p.BUSTYPE_LEVEL as bustypeLevel,sc.name as higherCorp,");
        sql.append(" p.id as id,p.corp_code as corpCode,p.corp_name as corpName,p.BUSTYPE_LEVEL as bustypeLevel,ab.CORP_NAME as higherCorp,");
        sql.append(" p.PARENT_ID as parentId,p.VALID_SIGN as validSign,p.corp_Id as corpId,ab.corp_id as abCorpId ");
        //sql.append(" from AB_POOL_CORP p left join sys_corp s on s.id=p.CORP_ID left join AB_POOL_CORP ab on p.PARENT_ID=ab.id ");
        sql.append(" from AB_POOL_CORP p left join AB_POOL_CORP ab on p.PARENT_ID=ab.id ");
        //sql.append(" and ab.VALID_SIGN in (:validSign) left join sys_corp sc on sc.id=ab.CORP_ID  ");
        sql.append(" and ab.VALID_SIGN in (:validSign) ");
        sql.append(" where p.VALID_SIGN in (:validSign) ");
        if(params.containsKey("corpName")){
            sql.append(" and p.corp_name like :corpName ");
        }
        if(params.containsKey("corpCode")){
            sql.append(" and p.corp_code like :corpCode ");
        }
        sql.append(" order by p.BUSTYPE_LEVEL,p.create_time ");
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        return listBySQLAliasToBean(sql.toString(), params, AbPoolCorpVO.class);
    }
    /**
     * 获取所有父类Id
     * @return 单位id
     * @author
     */
    public List<String> getAllPoolParentIds() {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.PARENT_ID as parentId from AB_POOL_CORP p ");
        sql.append(" where p.VALID_SIGN in (:validSign) ");
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 获取所有票据池单位Id
     * @return 单位corpIds
     * @author
     */
    public List<String> listAllPoolCorpIds(String corpAuthSql) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.corp_Id as corpId from AB_POOL_CORP p ");
        sql.append(" where p.VALID_SIGN in (:validSign) ");
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        if(StringUtils.isNotEmpty(corpAuthSql)){
            sql.append(" and ").append(corpAuthSql);
        }
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 获取启用票据池单位Id
     * @return 单位corpIds
     * @author
     */
    public List<String> getPoolCorpIdsByCorpId(String corpAuthSql) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.corp_Id as corpId from AB_POOL_CORP p ");
        sql.append(" where p.VALID_SIGN in (:validSign) ");
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_1});
        if(StringUtils.isNotEmpty(corpAuthSql)){
            sql.append(" and ").append(corpAuthSql);
        }
        Query query = createSQLQuery(sql.toString(), params);
        List<String> list = query.list();
        return list;
    }
    /**
     * 票据池单位信息查询
     * @param id
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public AbPoolCorpVO getAbPoolCorpInfo(String id) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" p.corp_code as corpCode,p.corp_name as corpName,p.CORP_ID as corpId ");
        sql.append(" from AB_POOL_CORP p  ");
        sql.append(" where p.VALID_SIGN in (:validSign) and p.id=:id");
        params.put("id", id);
        params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
        return getBySQLAliasToBean(sql.toString(), params, AbPoolCorpVO.class);
    }
}
