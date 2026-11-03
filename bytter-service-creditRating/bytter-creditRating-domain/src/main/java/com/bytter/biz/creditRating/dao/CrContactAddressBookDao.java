package com.bytter.biz.creditRating.dao;

import com.bytter.biz.creditRating.entity.CrContactAddressBook;
import com.bytter.biz.creditRating.vo.CrContactAddressBookVo;
import com.bytter.core.base.BaseDao;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CrContactAddressBookDao extends BaseDao<CrContactAddressBook, String> {

    public List<CrContactAddressBookVo> queryByInstitutionalRawDataId(String insId){
        Map params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id,name,sex,job,telephone_num as telephoneNum,email, wechat_num as wechatNum, qq_num as qqNum");
        querySQL.append(" ,institutional_raw_data_id as institutionalRawDataId,rmk, creator,create_date  createDate ");
        querySQL.append(" from contact_address_book where institutional_raw_data_id =:insId");
        params.put("insId", insId);


        return listBySQLAliasToBean(querySQL.toString(),params, CrContactAddressBookVo.class);
    }

    public List<CrContactAddressBookVo> findByIds(List<String> ids){
        Map params = Maps.newHashMap();
        StringBuilder querySQL = new StringBuilder();
        querySQL.append(" select id,name,sex,job,telephone_num as telephoneNum,email, wechat_num as wechatNum, qq_num as qqNum");
        querySQL.append(" ,institutional_raw_data_id as institutionalRawDataId, rmk, creator,create_date  createDate ");
        querySQL.append("   from contact_address_book ");
        querySQL.append(" where 1 = 1 ").append(" and ").append(getSQLIn(ids, 900, "id")).append(" order by create_date asc");
        return listBySQLAliasToBean(querySQL.toString(), params, CrContactAddressBookVo.class);
    }

    public void updateInstitutionalRawDataIdByIds(List<String> ids, String institutionalRawDataId){
        StringBuilder querySQL = new StringBuilder();
        Map params = Maps.newHashMap();
        querySQL.append(" update contact_address_book set institutional_raw_data_id =:rawDataId ");
        querySQL.append(" where ").append(getSQLIn(ids, 900, "id"));
        params.put("rawDataId", institutionalRawDataId);
        createSQLQuery(querySQL.toString(), params).executeUpdate();
    }
}
