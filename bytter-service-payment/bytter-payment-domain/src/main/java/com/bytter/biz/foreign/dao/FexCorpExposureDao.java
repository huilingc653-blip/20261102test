package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpExposure;
import com.bytter.biz.foreign.vo.FexCorpExposureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 单位敞口设置表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexCorpExposureDao extends BaseDao<FexCorpExposure, String> {

    /**
     * 获取单位ID集合
     * @param orgType
     * @return
     */
    public List<String> listCorpIdsBySeach(String orgType, String search) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String corpTableName = "sys_corp";
        if (CommonConstant.YES.equals(orgType)) {
            //资金计划的组织架构 暂未实现
        }
        sql.append(" select c.id corpId  from " + corpTableName + " c left join fex_corp_exposure f on c.id = f.corp_id ");
        sql.append(" where c.status = 1 ");
        if (!EmptyUtils.isBlank(search)) {
            sql.append(" and (c.code like :search or c.name like :search) ");
            params.put("search", "%" + search + "%");
        }
        sql.append(" order by c.sort, c.code ");
        return this.createSQLQuery(sql.toString()).setProperties(params).list();
    }

    /**
     * 单位敞口设置集合
     * @param orgType
     * @return
     */
    public List<FexCorpExposureVO> listFbsCorpExposure(String orgType, String search) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        String corpTableName = "sys_corp";
        if (CommonConstant.YES.equals(orgType)) {
            //资金计划的组织架构 暂未实现
        }
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId, ");
        sql.append(" f.is_Collect as isCollect,f.is_Natural_Hedge as isNaturalHedge, ");
        sql.append(" (select count(*) from " + corpTableName + " c1 where c1.parent_id = c.id and c1.status = 1 ) childCount ");
        sql.append(" from  " + corpTableName + "  c left join fex_corp_exposure f on c.id = f.corp_id ");
        sql.append(" where c.status = 1 ");
        if (!EmptyUtils.isBlank(search)) {
            sql.append(" and (c.code like :search or c.name like :search) ");
            params.put("search", "%" + search + "%");
        }
        sql.append(" order by c.sort, c.code ");
        return this.listBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }

    /**
     * 删除单位敞口信息
     */
    public void deleteCorpExposure() {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from fex_corp_exposure");
        this.createSQLQuery(sql.toString()).executeUpdate();
    }

    /**
     * 根据单位ID获取上级单位敞口信息
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getParentCorpExposureByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId, ");
        sql.append(" f.is_Collect as isCollect,f.is_Natural_Hedge as isNaturalHedge ");
        sql.append(" from sys_corp c left join fex_corp_exposure f on c.id = f.corp_id ");
        sql.append(" where c.status = 1 and c.id=(select parent_Id from sys_corp where id=:corpId)");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.getBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }

    /**
     * 根据上级单位ID获取下级单位的集合
     * @param parentId
     * @return
     */
    public List<FexCorpExposureVO> listCorpExposureByParentId(String parentId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId, ");
        sql.append(" f.is_Collect as isCollect,f.is_Natural_Hedge as isNaturalHedge ");
        sql.append(" from sys_corp c left join fex_corp_exposure f on c.id = f.corp_id ");
        sql.append(" where c.status = 1 and c.parent_Id=:parentId");
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        return this.listBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }

    /**
     * 根据单位ID获取上级单位敞口信息
     * @param corpId
     * @return
     */
    public FexCorpExposureVO getCorpExposureByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as corpId,c.name as corpName,c.code as corpCode, c.parent_id as corpParentId, c.type as corpType, c.net_id as corpNetId, ");
        sql.append(" f.is_Collect as isCollect,f.is_Natural_Hedge as isNaturalHedge ");
        sql.append(" from sys_corp c left join fex_corp_exposure f on c.id = f.corp_id ");
        sql.append(" where c.status = 1 and c.id=:corpId");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return this.getBySQLAliasToBean(sql.toString(), params, FexCorpExposureVO.class);
    }
}
