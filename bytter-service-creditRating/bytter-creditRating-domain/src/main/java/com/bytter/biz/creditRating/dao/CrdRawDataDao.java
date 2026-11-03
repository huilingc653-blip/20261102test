package com.bytter.biz.creditRating.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.creditRating.entity.CrdRawData;
import com.bytter.biz.creditRating.vo.CrInstitutionalRawDataVo;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.SqlUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CrdRawDataDao extends BaseDao<CrdRawData, String> {

    public DataTablesOutput queryAll(List<String> ids){
        StringBuilder querySql = new StringBuilder();
        querySql.append(" select id ,DATA_NAME as dataName, DATA_CODE as dataCode, ESTABLISHMENT_DATE as establishmentDate");
        querySql.append(" , REGISTERED_CAPITAL as registeredCapital, LEGAL_REPRESENTATIVE as legalRepresentative, BUSINESS_SCOPE as businessScope");
        querySql.append(" , DATA_ID as dataId, DATA_SOURCE_CATEGORY_NAME as dataSourceCategoryName, DATA_SOURCE_CATEGORY_CODE as dataSourceCategoryCode");
        querySql.append(" ,DATA_SOURCE_ID as dataSourceId, ");
        querySql.append("  from RAW_DATA where status = 95 ");
        if (ids != null && ids.size() > 0 ){
            querySql.append(" and ").append(SqlUtil.getInSql(ids, "id", true));
        }
        return getDynamicBySQL(null, querySql.toString(), CrdRawDataVo.class, "", false);
    }

    //listBySQLAliasToBean

    public List<CrdRawDataVo> queryByIds(List<String> ids){
        StringBuilder querySql = new StringBuilder();
        querySql.append(" select id ,DATA_NAME as dataName, DATA_CODE as dataCode, ESTABLISHMENT_DATE as establishmentDate");
        querySql.append(" , REGISTERED_CAPITAL as registeredCapital, LEGAL_REPRESENTATIVE as legalRepresentative, BUSINESS_SCOPE as businessScope");
        querySql.append(" , DATA_ID as dataId, DATA_SOURCE_CATEGORY_NAME as dataSourceCategoryName, DATA_SOURCE_CATEGORY_CODE as dataSourceCategoryCode");
        querySql.append(" ,DATA_SOURCE_ID as dataSourceId, ");
        querySql.append("  from RAW_DATA where status = 95 ");
        if (ids != null && ids.size() > 0 ){
            querySql.append(" and ").append(SqlUtil.getInSql(ids, "id", true));
        }
        HashMap<String, Object> paramMap = Maps.newHashMap();
        return listByHQLAliasToBean(querySql.toString(),paramMap, CrdRawDataVo.class);
    }

    public DataTablesOutput institutionDatatable(DataTablesInput input){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySql = new StringBuilder();
        Map<String, Object> formdata = input.getFormdata();
        input.setFormdata(null);
        querySql.append(" select rd.id, ird.Institution_Code as institutionCode, ird.institution_type as institutionType ");
        querySql.append(" , ird.BANK_TYPE_CODE as bankType, ird.Institution_Name as institutionName, ird.status, ird.creator, ird.CREATE_DATE as createDate ");
        querySql.append(" from institutional_raw_Data ird left join RAW_DATA rd on ird.Raw_Data_id = rd.id ");
        querySql.append(" where 1 = 1 ");
        if (formdata.containsKey("likes_insCode") && ObjectUtil.isNotEmpty( formdata.get("likes_insCode")) ){
            querySql.append(" and ird.institution_code like '%"+ formdata.get("likes_insCode")+"%'");
        }
        if (formdata.containsKey("ins_instTypes") && ObjectUtil.isNotEmpty( formdata.get("ins_instTypes"))){
            querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_instTypes")).split(",")
                    , "ird.institution_type", true));
        }
        if (formdata.containsKey("likes_insName") && ObjectUtil.isNotEmpty( formdata.get("likes_insName"))){
            querySql.append(" and ird.institution_name like '%"+ formdata.get("likes_insName")+"%'");
        }
        if (formdata.containsKey("ins_bankTypes") && ObjectUtil.isNotEmpty( formdata.get("ins_bankTypes"))){
            querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_bankTypes")).split(",")
                    , "ird.bank_type_code", true));
        }
        if (formdata.containsKey("ins_insStatus") ){
            if (ObjectUtil.isNotEmpty( formdata.get("ins_insStatus"))){
                querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_insStatus")).split(",")
                        , "ird.status", true));
            }
            if (ObjectUtil.isEmpty( formdata.get("ins_insStatus"))){
                querySql.append(" and  ird.status != '-2' ");
            }
        }
        if (formdata.containsKey("likes_createby") && ObjectUtil.isNotEmpty( formdata.get("likes_createby"))){
            querySql.append(" and ird.creator like '%"+ formdata.get("likes_createby")+"%'");
        }
        input.setOrderString(new OrderString("ird.create_date","desc"));
        return getDynamicBySQL(input, querySql.toString(), CrInstitutionalRawDataVo.class,"", false, params);
    }

    public DataTablesOutput institutionDatatableToApprove(DataTablesInput input, List<String> ids){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySql = new StringBuilder();
        Map<String, Object> formdata = input.getFormdata();
        input.setFormdata(null);
        querySql.append(" select rd.id, ird.Institution_Code as institutionCode, ird.institution_type as institutionType ");
        querySql.append(" , ird.BANK_TYPE_CODE as bankType, ird.Institution_Name as institutionName, ird.status, ird.creator, ird.CREATE_DATE as createDate ");
        querySql.append(" from institutional_raw_Data ird left join RAW_DATA rd on ird.Raw_Data_id = rd.id ");
        querySql.append(" where  ird.status = '001' ");
        if (null != null && ids.size() > 0){
            querySql.append(" and  " + SqlUtil.getInSql(ids, "rd.id", true));
        }
        if (formdata.containsKey("likes_insCode") && ObjectUtil.isNotEmpty( formdata.get("likes_insCode")) ){
            querySql.append(" and ird.institution_code like '%"+ formdata.get("likes_insCode")+"%'");
        }
        if (formdata.containsKey("ins_instTypes") && ObjectUtil.isNotEmpty( formdata.get("ins_instTypes"))){
            querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_instTypes")).split(",")
                    , "ird.institution_type", true));
        }
        if (formdata.containsKey("likes_insName") && ObjectUtil.isNotEmpty( formdata.get("likes_insName"))){
            querySql.append(" and ird.institution_name like '%"+ formdata.get("likes_insName")+"%'");
        }
        if (formdata.containsKey("ins_bankTypes") && ObjectUtil.isNotEmpty( formdata.get("ins_bankTypes"))){
            querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_bankTypes")).split(",")
                    , "ird.bank_type_code", true));
        }
//        if (formdata.containsKey("ins_insStatus") && ObjectUtil.isNotEmpty( formdata.get("ins_insStatus"))){
//            querySql.append(" and  " + SqlUtil.getInSql(String.valueOf(formdata.get("ins_insStatus")).split(",")
//                    , "ird.status", true));
//        }
        if (formdata.containsKey("likes_createby") && ObjectUtil.isNotEmpty( formdata.get("likes_createby"))){
            querySql.append(" and ird.creator like '%"+ formdata.get("likes_createby")+"%'");
        }

        input.setOrderString(new OrderString("ird.create_date","desc"));
        return getDynamicBySQL(input, querySql.toString(), CrInstitutionalRawDataVo.class,"", false, params);
    }
}
