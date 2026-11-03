package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrVisitRecordRegistration;
import com.bytter.biz.creditRating.vo.CrVisitRecordRegistrationVo;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CrVisitRecordRegistrationDao extends BaseDao<CrVisitRecordRegistration, String> {

    public List<CrVisitRecordRegistrationVo> queryByInstitutionalRawDataId(String insId){
        StringBuilder querySQL = new StringBuilder();
        Map params = Maps.newHashMap();
        params.put("insId", insId);
        querySQL.append(" select id , visiting_matters as visitingMatters, visiting_personnel as visitingPersonnel,");
        querySQL.append(" visiting_obj as visitingObj, visiting_obj_job as visitingObjJob, visit_date as visitDate, ");
        querySQL.append( "institutional_raw_data_id as institutionalRawDataId ,rmk, creator, create_date as createDate ");
        querySQL.append(" from visit_record_registration ");
        querySQL.append(" where institutional_raw_data_id =:insId ");
        return listBySQLAliasToBean( querySQL.toString(), params, CrVisitRecordRegistrationVo.class);
    }


    public List<CrVisitRecordRegistrationVo> findByIds(List<String> visitIds){
        StringBuilder querySQL = new StringBuilder();
        Map params = Maps.newHashMap();
//        params.put("id", visitIds);
        querySQL.append(" select id , visiting_matters as visitingMatters, visiting_personnel as visitingPersonnel,");
        querySQL.append(" visiting_obj as visitingObj, visiting_obj_job as visitingObjJob, visit_date as visitDate, ");
        querySQL.append( "institutional_raw_data_id as institutionalRawDataId ,rmk, creator, create_date as createDate ");
        querySQL.append(" from visit_record_registration ");
        querySQL.append(" where 1 =1  and  ").append(getSQLIn(visitIds, 900, "id")).append(" order by create_date asc");
        return listBySQLAliasToBean( querySQL.toString(), params, CrVisitRecordRegistrationVo.class);
    }

    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId){
        StringBuilder querySQL = new StringBuilder();
        Map params = Maps.newHashMap();
        querySQL.append(" update visit_record_registration set institutional_raw_data_id =:rawDataId ");
        querySQL.append(" where ").append(getSQLIn(ids, 900, "id"));
        params.put("rawDataId", institutionalRawDataId);
        createSQLQuery(querySQL.toString(), params).executeUpdate();
    }

}
