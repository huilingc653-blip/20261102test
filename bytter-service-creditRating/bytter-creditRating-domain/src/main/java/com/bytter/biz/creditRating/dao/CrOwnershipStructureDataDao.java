package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrOwnershipStructureData;
import com.bytter.biz.creditRating.vo.CrOwnershipStructureDataVo;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CrOwnershipStructureDataDao extends BaseDao<CrOwnershipStructureData, String> {

    public List<CrOwnershipStructureDataVo> queryByInstitutionalRawDataId(String institutionalRawDataId){
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id,investor,financing_way as financingWay,investment_method as investmentMethod,");
        querySQL.append(" contribution_amount as contributionAmount,shareholding_ratio as shareholdingRatio,");
        querySQL.append(" asset_name as assetName, institutional_raw_data_id as institutionalRawDataId, creator,");
        querySQL.append("  create_Date as createDate ");
        querySQL.append(" from  ownership_structure_data ");
        querySQL.append(" where institutional_raw_data_id =:institutionalRawDataId");
        Map params = Maps.newHashMap();
        params.put("institutionalRawDataId", institutionalRawDataId);
        return listBySQLAliasToBean(querySQL.toString(), params, CrOwnershipStructureDataVo.class);
    }

    public List<CrOwnershipStructureDataVo> findByIds(List<String> ids){
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id,investor,financing_way as financingWay,investment_method as investmentMethod,");
        querySQL.append(" contribution_amount as contributionAmount,shareholding_ratio as shareholdingRatio,");
        querySQL.append(" asset_name as assetName, institutional_raw_data_id as institutionalRawDataId, creator,");
        querySQL.append("  create_Date as createDate, rmk  ");
        querySQL.append(" from  ownership_structure_data ");
        querySQL.append(" where 1 = 1 ").append(" and ").append(getSQLIn(ids, 900, "id")).append(" order by CREATE_DATE asc");
        Map params = Maps.newHashMap();
        return listBySQLAliasToBean(querySQL.toString(), params, CrOwnershipStructureDataVo.class);
    }

    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId){
        StringBuilder querySQL = new StringBuilder();
        Map params = Maps.newHashMap();
        querySQL.append(" update ownership_structure_data set institutional_raw_data_id =:rawDataId ");
        querySQL.append(" where ").append(getSQLIn(ids, 900, "id"));
        params.put("rawDataId", institutionalRawDataId);
        createSQLQuery(querySQL.toString(), params).executeUpdate();
    }

}
