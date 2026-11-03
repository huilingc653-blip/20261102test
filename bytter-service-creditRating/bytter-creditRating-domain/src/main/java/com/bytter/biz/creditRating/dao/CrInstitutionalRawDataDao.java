package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.vo.CrInstitutionalRawDataVo;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CrInstitutionalRawDataDao extends BaseDao<CrInstitutionalRawData, String> {

    public InstitutionDetailsVo queryByRawDataId(String rawDataId){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id as insId, institution_type as institutionType,institution_name as institutionName, institution_code as institutionCode");
        querySQL.append(" , bank_type_code as bankType, financial_license_inst_code as financialLicenseInstCode ");
        querySQL.append(" , unified_social_credit_code as unifiedSocialCreditCode, regional_code as regionalCode, place");
        querySQL.append(" , prov, city, BANK_TYPE_ID as bankTypeId,  bank_code as bankCode, institution_name_code as institutionNameCode ");
        querySQL.append(" , registered_capital as registeredCapital,start_business_period as  startBusinessPeriod,end_business_period as endBusinessPeriod");
        querySQL.append(" , tel, email, employee_count employeeCount,business_status as businessStatus, status,rmk  ");
        querySQL.append(" from institutional_raw_data  ");
        querySQL.append(" where raw_data_id =:rawDataId  and status <> '-2' ");
        params.put("rawDataId", rawDataId);
        List<InstitutionDetailsVo> institutionDetailsVoDbs = listBySQLAliasToBean(querySQL.toString(), params, InstitutionDetailsVo.class);
        if(null != institutionDetailsVoDbs && institutionDetailsVoDbs.size()>0 ){
            return institutionDetailsVoDbs.get(0);
        }
        return null;

    }


    public List<CrInstitutionalRawDataVo> queryByTypes(List<String> typeList){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id as id, institution_type as institutionType,institution_name as institutionName, institution_code as institutionCode");
        querySQL.append(" , bank_type_code as bankType, financial_license_inst_code as financialLicenseInstCode" );
        querySQL.append(" , unified_social_credit_code as unifiedSocialCreditCode, regional_code as regionalCode, place");
        querySQL.append(" , prov, city ");
        querySQL.append(" , registered_capital as registeredCapital,start_business_period as  startBusinessPeriod,end_business_period as endBusinessPeriod");
        querySQL.append(" , tel, email, employee_count employeeCount,business_status as businessStatus, status, rmk  ");
        querySQL.append(" from institutional_raw_data  ");
        if (null != typeList &&  0 < typeList.size()){
            querySQL.append(" where status = '095' and ").append(getSQLIn(typeList, 900, "institution_type"));
        }
        List<CrInstitutionalRawDataVo> dbs = listBySQLAliasToBean(querySQL.toString(), params, CrInstitutionalRawDataVo.class);
        return dbs;

    }

    public List<InstitutionDetailsVo> findInstitutionByIds(List<String> ids){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select ird.id as insId, ird.Institution_Code as institutionCode, ird.institution_type as institutionType ");
        querySQL.append(" , ird.BANK_TYPE_CODE as bankType, ird.Institution_Name as institutionName, ird.status, ird.creator ");
        querySQL.append(" , ird.BANK_CODE as bankCode, ird.BANK_TYPE_ID as bankTypeId ");
        querySQL.append(" , rd.data_source_category_name as dataSourceCategoryName, rd.data_source_category_code as dataSourceCategoryCode ");
        querySQL.append(" , rd.data_source_id as dataSourceId, ird.CREATE_DATE as createDate ");
        querySQL.append(" from institutional_raw_Data ird left join RAW_DATA rd on ird.Raw_Data_id = rd.id ");
        querySQL.append(" where  ird.status <> '-2' ");
        if (null != ids &&  0 < ids.size()){
            querySQL.append(" and ").append(getSQLIn(ids, 900, "ird.id"));
        }

        return listBySQLAliasToBean(querySQL.toString(), params, InstitutionDetailsVo.class);

    }


    public CrInstitutionalRawDataVo getByRawDataId(String rawDataId){
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id , institution_type as institutionType,institution_name as institutionName, institution_code as institutionCode");
        querySQL.append(" , bank_type_code as bankType, financial_license_inst_code as financialLicenseInstCode" );
        querySQL.append(" , unified_social_credit_code as unifiedSocialCreditCode, regional_code as regionalCode, place");
        querySQL.append(" , prov, city, institution_name_code as institutionNameCode ");
        querySQL.append(" , registered_capital as registeredCapital,start_business_period as  startBusinessPeriod");
        querySQL.append(" , end_business_period as endBusinessPeriod, raw_data_id as rawDataId");
        querySQL.append(" , tel, email, employee_count employeeCount,business_status as businessStatus, status,rmk ");
        querySQL.append(" , CREATE_DATE as createDate, CREATOR as creator, BANK_CODE as bankCode, BANK_TYPE_ID as bankTypeId ");
        querySQL.append(" from institutional_raw_data  ");
        querySQL.append(" where raw_data_id =:rawDataId  and status <> '-2' ");
        params.put("rawDataId", rawDataId);
        return getBySQLAliasToBean(querySQL.toString(), params, CrInstitutionalRawDataVo.class);
    }

    public int updateStatusById(String id, String status){
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update institutional_raw_data set status =:status where id=:id";
        params.put("id", id);
        params.put("status", status);
        return this.batchSQLExecute(sql, params);
    }
}
