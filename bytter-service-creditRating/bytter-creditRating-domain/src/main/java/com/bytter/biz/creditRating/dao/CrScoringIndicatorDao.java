package com.bytter.biz.creditRating.dao;

import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.creditRating.entity.CrScoringIndicator;
import com.bytter.biz.creditRating.vo.CrScoringIndicatorVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CrScoringIndicatorDao extends BaseDao<CrScoringIndicator, String> {

    public DataTablesOutput getDataTable(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2");
        return getDynamicBySQL(input,sql.toString(), CrScoringIndicatorVo.class,"",false);
    }

    public CrScoringIndicatorVo getById(String id){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2 and si.id = :id ");
        map.put("id",id);
        List<CrScoringIndicatorVo> result = listBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
        if(result.size()>0){
            return result.get(0);
        }else{
            return null;
        }
    }
    public List<CrScoringIndicatorVo> getList(CrScoringIndicatorVo vo){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2  ");
        if (StringUtils.isNotBlank(vo.getScoringIndicatorCode())){
            sql.append(" and si.SCORING_INDICATOR_CODE like '%"+vo.getScoringIndicatorCode()+"%' ");

        }
        if (StringUtils.isNotBlank(vo.getScoringIndicatorName())){
            sql.append(" and si.SCORING_INDICATOR_NAME like '%"+vo.getScoringIndicatorName()+"%' ");
        }
        if (!ObjectUtils.isEmpty(vo.getStatus())){
            sql.append(" and si.STATUS = "+vo.getStatus()+" ");
        }
        return listBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
    }

    public List<CrScoringIndicatorVo> getEffectData(CrScoringIndicatorVo vo){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS = 1  ");
        if (StringUtils.isNotBlank(vo.getScoringIndicatorCode())){
            sql.append(" and si.SCORING_INDICATOR_CODE like '%"+vo.getScoringIndicatorCode().replaceAll("\\s","")+"%' ");

        }
        if (StringUtils.isNotBlank(vo.getScoringIndicatorName())){
            sql.append(" and si.SCORING_INDICATOR_NAME like '%"+vo.getScoringIndicatorName().replaceAll("\\s","")+"%' ");

        }
        return listBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
    }

    public  List<CrScoringIndicatorVo> getByParentId(String parentId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2 and si.PARENT_ID = :parentId");
        map.put("parentId",parentId);
        List<CrScoringIndicatorVo> result = listBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
        return result;
    }

    public CrScoringIndicatorVo getByScoringIndCode(String scoringIndicatorCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2 and si.SCORING_INDICATOR_CODE = :scoringIndicatorCode");
        map.put("scoringIndicatorCode", scoringIndicatorCode);
        return getBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
    }

    public List<CrScoringIndicatorVo> getByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select si.ID as id,si.PARENT_ID as parentId,si.SCORING_INDICATOR_CODE as scoringIndicatorCode,si.SCORING_INDICATOR_NAME as scoringIndicatorName,si.SCORING_INDICATOR_DESCRIPTION as scoringIndicatorDescription,");
        sql.append(" si.STATUS as status,si.LEVEL_ as level_,si.CATEGORY_ENCODING as categoryEnCoding,si.CATEGORY_NAME as categoryName ,si.VERSION_NUMBER as versionNumber,si.DELETE_FLAG as deleteFlag,");
        sql.append(" si.CREATOR as creator,si.CREATE_DATE as createDate,si.DATA_SOURCE_ID as dataSourceId,si.DATA_SOURCE_CODE as dataSourceCode,si.DATA_SOURCE_NAME as dataSourceName,si.DATA_SOURCE_ORIGIN as dataSourceOrigin,si.SCORING_RUBRIC as scoringRubric,si.IS_LAST as isLast ");
        sql.append(" from CR_SCORING_INDICATOR si where si.STATUS <> -2 ");
        if (!CollectionUtils.isEmpty(ids)){
            sql.append(" and "+getSQLIn(ids,900,"si.ID"));
        }
        return listBySQLAliasToBean(sql.toString(), map, CrScoringIndicatorVo.class);
    }

    public Object getMaxScoringCode() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT MAX(SCORING_INDICATOR_CODE) AS max_string_id FROM cr_scoring_indicator");
        return this.createSQLQuery(sql.toString(), new HashMap<>()).uniqueResult();
    }
}
