package com.bytter.biz.pool.dao;
import com.bytter.biz.pool.entity.FcCorpPool;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcCorpPoolVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 资金池单位单位类
 * @date 2019-04-30 16:44:38
 */
@Repository
public class FcCorpPoolDao extends BaseDao<FcCorpPool, String> {
    /**
     * 资金池单位通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id, f.corp_id as corpId, f.pool_code as poolCode, f.pool_name as poolName, f.status as status,");
        sql.append("f.pool_id as poolId, f.pool_sign as poolSign, f.rmk as rmk, f.corp_code as corpCode, f.corp_name as corpName");
        sql.append(" from ( ");
        sql.append("select f.id as id,f.corp_id as corp_id,f.pool_code as pool_code,");
        sql.append("case when f.pool_sign = '1' then f.pool_name ");
        sql.append(" when f.pool_sign = '0' then (select pool_name from fc_corp_pool c where c.id = f.pool_id) end pool_name,");
        sql.append("f.status as status,f.pool_id as pool_id,f.pool_sign as pool_sign,f.rmk as rmk,");
        sql.append("s.code as corp_code,s.name as corp_name,s.sort,s.code from fc_corp_pool f left join sys_corp s on f.corp_id = s.id");
        sql.append(") f");
        sql.append(" where 1=1 ");
        return sql.toString();
    }
    /**
     * 根据id查找资金池
     * @param id
     * @return
     */
    public FcCorpPool getByIdCache(String id) {
        String hql = "from FcCorpPool where id = ?0";
        Query query = createQuery(hql, id);
        return (FcCorpPool) query.uniqueResult();
    }
    /**
     * 根据id查询资金池信息
     * @param id
     * @return
     */
    public FcCorpPoolVO getFcCorpPoolById(String id){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String,Object> params = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(id)){
            sql.append(" and f.id = :id");
            params.put("id",id);
        }
        return getBySQLAliasToBean(sql.toString(),params,FcCorpPoolVO.class);
    }
    /**
     * 根据资金池代码查询资金池信息
     * @param poolCode
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolCode(String poolCode){
        String hql = " from FcCorpPool f where f.poolSign = '1' and f.poolCode = :poolCode ";
        Map<String,String> params = new HashMap<>();
        params.put("poolCode",poolCode);
        return this.getSession().createQuery(hql).setProperties(params).list();
    }
    /**
     * 根据资金池名称查询资金池信息
     * @param poolName
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByPoolName(String poolName){
        String hql = " from FcCorpPool f where f.poolName = :poolName ";
        Map<String,String> params = new HashMap<>();
        params.put("poolName",poolName);
        return this.getSession().createQuery(hql).setProperties(params).list();
    }
    /**
     * 根据单位id查询资金池主体单位
     * @param corpId
     * @return
     */
    public List<FcCorpPool> getFcCorpPoolByCorpId(String corpId,String poolSign){
        String hql = " from FcCorpPool f where f.poolSign = :poolSign and f.status = '1' and f.corpId = :corpId ";
        Map<String,String> params = new HashMap<>();
        params.put("corpId",corpId);
        params.put("poolSign",poolSign);
        return this.getSession().createQuery(hql).setProperties(params).list();
    }
    /**
     * 查询资金池树列表数据
     * @param poolCode
     * @param poolName
     * @param status
     * @return
     */
    public List<FcCorpPool> listAllPool(String poolCode,String poolName, String status){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<>();
        if(StringUtils.isNotBlank(poolCode)){
            sql.append(" and f.pool_code like :poolCode");
            params.put("poolCode","%"+poolCode+"%");
        }
        if(StringUtils.isNotBlank(poolName)){
            sql.append(" and f.pool_name like :poolName");
            params.put("poolName","%"+poolName+"%");
        }
        if(StringUtils.isNotBlank(status)){
            sql.append(" and f.status = :status");
            params.put("status",status);
        }
        sql.append(" order by f.pool_code desc");
        return listBySQLAliasToBean(sql.toString(), params, FcCorpPool.class);
    }
    /**
     * 根据id查询资金池下面的子单位个数
     * @param poolId
     * @return
     */
    public List<FcCorpPool> listByPoolId(String poolId){
        String hql = "from FcCorpPool f where f.status = '1' and f.poolId=?0";
        return createQuery(hql.toString(), poolId).list();
    }
    /**
     * 根据主体单位查询资金池单位下面的子单位
     * @param poolId
     * @return
     */
    public List<FcCorpPool> corpIdByPoolId(String poolId){
        StringBuilder sql = new StringBuilder();
        sql.append("select p.corp_id from fc_corp_pool p where p.pool_id in (select f.id from fc_corp_pool f");
        sql.append(" where f.corp_id in (select c.corp_id from fc_corp_pool c ");
        sql.append("where c.id = :poolId) and f.pool_sign = '1')");
        HashMap<String, String> params = new HashMap<>();
        params.put("poolId",poolId);
        return listBySQLAliasToBean(sql.toString(),params,FcCorpPool.class);
    }
    /**
     * 获取资源权限的资金池
     * @param joinSql
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPools(String joinSql){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  f.pool_sign = '1' and f.status = '1' ");
        sql.append(joinSql);
        sql.append(" order by f.pool_code desc");
        return listBySQLAliasToBean(sql.toString(), null, FcCorpPoolVO.class);
    }
    /**
     * 获取资源权限的资金池
     * @param joinSql
     * @param poolId
     * @return
     */
    public List<FcCorpPoolVO> getUserCorpPoolChilds(String joinSql, String poolId){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  f.pool_sign = '0' and f.status = '1'");
        sql.append(joinSql);
        HashMap<String, String> params = new HashMap<>();
        if(StringUtils.isNotBlank(poolId)){
            sql.append(" and f.pool_id = :poolId");
            params.put("poolId",poolId);
        }
        sql.append(" order by f.sort,f.code");
        return listBySQLAliasToBean(sql.toString(), params, FcCorpPoolVO.class);
    }
    /**
     * 根据资金池ID和子单位Id获取子单位信息
     * @param poolId
     * @param corpId
     * @return
     */
    public FcCorpPoolVO getChildCorpByPoolId(String poolId, String corpId){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String,Object> params = new HashMap<String,Object>();
        if(StringUtils.isNotBlank(poolId)){
            sql.append(" and f.pool_id = :poolId");
            params.put("poolId",poolId);
        }
        if(StringUtils.isNotBlank(corpId)){
            sql.append(" and f.corp_id = :corpId");
            params.put("corpId",corpId);
        }
        return getBySQLAliasToBean(sql.toString(),params,FcCorpPoolVO.class);
    }
    /**
     * 根据资金池id和单位id查找资金池信息
     * @param poolSign
     * @param corpId
     * @return
     */
    public List<FcCorpPool> getPoolInfo(String poolSign,String corpId){
        StringBuilder hql = new StringBuilder();
        hql.append("from FcCorpPool where poolSign =:poolSign and corpId =:corpId");
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("poolSign",poolSign);
        params.put("corpId",corpId);
        return listByHQL(hql.toString(),params);
    }
    /**
     * 查询所有资金池信息
     * @return
     */
    public List<FcCorpPoolVO> getAllCorpPool(){
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.status = '1' ");
        return  listBySQLAliasToBean(sql.toString(), null, FcCorpPoolVO.class);
    }
    /**
     * 根据单位统计资金池
     * @param corpId
     * @return
     */
    public int getPoolCountByCorpId(String corpId){
        String sql="select count(*) from fc_corp_pool where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    public List<FcCorpPoolDTO> getFcCorpPoolByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from fc_corp_pool a where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "a.id"));
        return listBySQLAliasToBean(sql.toString(), null, FcCorpPoolDTO.class);
    }
    
    public List<FcCorpPoolDTO> getFcCorpPoolByPoolIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from fc_corp_pool a where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "a.id"));
        return listBySQLAliasToBean(sql.toString(), null, FcCorpPoolDTO.class);
    }
    
    
    public List<FcCorpPoolDTO> getPoolNameByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id, a.pool_name as poolName from fc_corp_pool a where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "a.id"));
        return listBySQLAliasToBean(sql.toString(), null, FcCorpPoolDTO.class);
    }
}
