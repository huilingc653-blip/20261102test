package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrIndicatorCategory;
import com.bytter.biz.creditRating.vo.CrIndicatorCategoryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标类别(已弃用)
 */
@Repository
public class CrIndicatorCategoryDao extends BaseDao<CrIndicatorCategory, String> {

    public DataTablesOutput getDataTable(DataTablesInput input){
        StringBuilder sql = new StringBuilder();
        sql.append(" select ic.ID as id,ic.CATEGORY_ENCODING as categoryEnCoding,ic.CATEGORY_NAME as categoryName,ic.CATEGORY_DESCRIPTION as categoryDescription,ic.DELETE_FLAG as deleteFlag,ic.STATUS as status,");
        sql.append(" ic.CREATOR as creator,ic.CREATE_DATE as createDate from CR_INDICATOR_CATEGORY ic where 1=1 and ic.status <> -1 ");
        return getDynamicBySQL(input,sql.toString(), CrIndicatorCategoryVo.class,"",false);
    }

    public CrIndicatorCategoryVo getById(String id){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select ic.ID as id,ic.CATEGORY_ENCODING as categoryEnCoding,ic.CATEGORY_NAME as categoryName,ic.CATEGORY_DESCRIPTION as categoryDescription,ic.DELETE_FLAG as deleteFlag,ic.STATUS as status,");
        sql.append(" ic.CREATOR as creator,ic.CREATE_DATE as createDate from CR_INDICATOR_CATEGORY ic where 1=1 and ic.status <> -1 ");
        map.put("id",id);
        List<CrIndicatorCategoryVo> result = listBySQLAliasToBean(sql.toString(), map, CrIndicatorCategoryVo.class);
        if(result.size()>0){
            return result.get(0);
        }else{
            return null;
        }
    }

    public List<CrIndicatorCategoryVo> getEffectData(){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select ic.ID as id,ic.CATEGORY_ENCODING as categoryEnCoding,ic.CATEGORY_NAME as categoryName,ic.CATEGORY_DESCRIPTION as categoryDescription,ic.DELETE_FLAG as deleteFlag,ic.STATUS as status,");
        sql.append(" ic.CREATOR as creator,ic.CREATE_DATE as createDate from CR_INDICATOR_CATEGORY ic where 1=1 and ic.status <> -1 ");
        return listBySQLAliasToBean(sql.toString(), map, CrIndicatorCategoryVo.class);
    }

}
