package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposure;
import com.bytter.biz.foreign.vo.FexExposureSearchVO;
import com.bytter.biz.foreign.vo.FexExposureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexExposureDao extends BaseDao<FexExposure, String> {

    /**
     * 根据敞口上报主信息获取上报明细
     * @param mainId
     * @param curId
     * @return
     */
    public List<FexExposureVO> listVOByMainId(String mainId, String curId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.main_Id as mainId,t.cur_Id as curId,t.sort as sort,");
        sql.append(" t.begin_Date as beginDate,t.end_Date as endDate,t.fill_Unit as fillUnit");
        sql.append(" from Fex_Exposure t where t.main_Id=:mainId");
        if(StringUtils.isNotBlank(curId)){
            sql.append(" and t.cur_Id=:curId");
        }
        sql.append(" order by t.cur_Id,sort");
        HashMap<String, String> params = new HashMap<>();
        params.put("mainId", mainId);
        params.put("curId", curId);
        return this.listBySQLAliasToBean(sql.toString(), params, FexExposureVO.class);
    }

    /**
     * 根据敞口上报主信息获取上报明细
     * @param searchVO
     * @return
     */
    public List<FexExposureVO> listVOBySearch(FexExposureSearchVO searchVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.main_Id as mainId,t.cur_Id as curId,t.sort as sort,");
        sql.append(" t.begin_Date as beginDate,t.end_Date as endDate,t.fill_Unit as fillUnit");
        sql.append(" from Fex_Exposure t where t.main_Id=:mainId");
        if(StringUtils.isNotBlank(searchVO.getCurId())){
            sql.append(" and t.cur_Id=:curId");
        }
        if(searchVO.getBeginDate() != null){
            sql.append(" and t.begin_Date>=:beginDate");
        }
        if(searchVO.getEndDate() != null){
            sql.append(" and t.end_Date<=:endDate");
        }
        if(StringUtils.isNotBlank(searchVO.getSumUnit())){
            sql.append(" and t.fill_Unit=:sumUnit");
        }
        sql.append(" order by t.cur_Id,sort");
        HashMap<String, Object> params = new HashMap<>();
        params.put("mainId", searchVO.getId());
        params.put("curId", searchVO.getCurId());
        params.put("beginDate", searchVO.getBeginDate());
        params.put("endDate", searchVO.getEndDate());
        params.put("sumUnit", searchVO.getSumUnit());
        return this.listBySQLAliasToBean(sql.toString(), params, FexExposureVO.class);
    }

    /**
     * 删除上报明细
     * @param id
     */
    public void deleteByMainId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from Fex_Exposure where main_id=:mainId");
        Map<String,String> params = new HashMap<String,String>();
        params.put("mainId",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 根据子单获取上报的周期集合
     * @param childMainIds
     * @return
     */
    public List<FexExposureVO> getPeriodByChildCorp(List<String> childMainIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct f.begin_date as beginDate,f.end_date as endDate,f.fill_unit as fillUnit from Fex_Exposure f ");
        sql.append(" left join Fex_Exposure_Main m on m.id = f.main_id");
        sql.append(" where ").append(SqlUtil.getInSql(childMainIds, "m.id", true));
        sql.append(" group by f.begin_date,f.end_date,f.fill_unit");
        return listBySQLAliasToBean(sql.toString(), null, FexExposureVO.class);
    }
}
