package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 单位业务类别对照表
 * @date 2017-05-18 16:36:11
 */
@Repository
public class NisCorpBusDao extends BaseDao<NisCorpBus, String> {
    /**
     * 根据单位ID获取分配的业务种类
     *
     * @param corpId
     * @return
     */
    public List<NisCorpBusVO> getByBusType(String corpId) {
        StringBuilder hql = new StringBuilder();
        hql.append("select nc.id as id,nc.corpId as corpId,nc.busId as busId,nt.id as busId,nt.busType as busType,nt.busTypeName as busTypeName,");
        hql.append("nt.cdSign,nt.itemCode as itemCode from NisCorpBus nc,NisBusType nt where nc.busId=nt.id ");
        hql.append("and nt.validSign = :validSign and nc.corpId = :corpId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("corpId", corpId);
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return listByHQLAliasToBean(hql.toString(), params, NisCorpBusVO.class);
    }

    /**
     * 根据单位ID,收付款标志获取分配的业务种类
     *
     * @param corpId
     * @return
     */
    public List<NisCorpBusVO> getByBusType(String corpId, String cdSign) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        hql.append("select distinct nc.busId as busId, nt.busType as busType,nt.busTypeName as busTypeName,nt.itemCode as itemCode,nt.sysBusType as sysBusType ");
        hql.append(" from NisCorpBus nc inner join NisBusType nt on nc.busId = nt.id where 1 = 1 and nt.validSign = :validSign ");
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        if (!EmptyUtils.isBlank(corpId)) {
            hql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "nc.corpId", true));
        }
        if (!EmptyUtils.isBlank(cdSign)) {
            hql.append(" and nt.cdSign = :cdSign");
            params.put("cdSign", cdSign);
        }
        return listByHQLAliasToBean(hql.toString(), params, NisCorpBusVO.class);
    }

    /**
     * 根据单位ID,收付款标志获取分配的业务种类
     *
     * @param corpId
     * @return
     */
    public List<String> getBusIdsByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct nc.bus_Id as busId from Nis_Corp_Bus nc ");
        if (!EmptyUtils.isBlank(corpId)) {
            sql.append(" where ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "nc.corp_Id", true));
        }
        return this.createSQLQuery(sql.toString()).list();
    }

    /**
     * 根据收付款标志获取分配的业务种类
     *
     * @param cdSign
     * @return
     */
    public List<NisCorpBusVO> getByBusTypeByCdSign(String cdSign) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        hql.append("select nt.id as busId,nt.busType as busType,nt.busTypeName as busTypeName, ");
        hql.append(" nt.cdSign from NisBusType nt where nt.validSign = :validSign ");
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        hql.append(" and nt.cdSign = :cdSign");
        params.put("cdSign", cdSign);
        return listByHQLAliasToBean(hql.toString(), params, NisCorpBusVO.class);
    }

    /**
     * 根据业务种类id查出单位是否已分配
     *
     * @param busId
     * @return
     */
    public List<NisCorpBus> getByBusCorp(String busId) {
        return listBy("busId", busId);
    }

    /**
     * 查询单位业务种类设置信息
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<NisCorpBusVO> getDataTablesUnion(DataTablesInput dataTablesInput, List<String> authCorpIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct n.corp_id as corpId ");
        sql.append(" from nis_corp_bus n left join nis_bus_type t on n.bus_id = t.id ");
        sql.append(" where t.valid_Sign = '1'");
        sql.append(" and ").append(SqlUtil.getInSql(authCorpIdList, "n.corp_id", true));
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisCorpBusVO.class, "c", false);
    }

    /**
     * 获取指定业务类型被占用的情况
     *
     * @param busTypeId
     * @return
     */
    public Integer getBusTypeUsedCount(String busTypeId) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" select count(*) as NUM from NIS_BILLHEAD b left join NIS_CORP_BUS c on c.bus_id = b.bus_type ");
        sql.append(" where status < 95 and status <> -2 ");
        sql.append(" and bus_id = :busTypeId ");
        params.put("busTypeId", busTypeId);
        Map rs = getBySQL(sql.toString(), params);
        Integer count = new Integer(rs.get("NUM").toString() == null ? "0" : rs.get("NUM").toString());
        return count;
    }

    /**
     * 根据单位ID,收付款标志,系统标志获取业务类别
     *
     * @param corpId
     * @return
     */
    public List<NisCorpBusVO> getBySysBusType(String corpId, String cdSign, String sysBusType) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        hql.append("select distinct nc.busId as busId, nt.busType as busType,nt.busTypeName as busTypeName,nt.itemCode as itemCode");
        hql.append(" from NisCorpBus nc,NisBusType nt where nc.busId = nt.id and nt.validSign = :validSign ");
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        if (!EmptyUtils.isBlank(corpId)) {
            hql.append(" and nc.corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (!EmptyUtils.isBlank(cdSign)) {
            hql.append(" and nt.cdSign = :cdSign");
            params.put("cdSign", cdSign);
        }
        if (!EmptyUtils.isBlank(sysBusType)) {
            hql.append(" and nt.sysBusType = :sysBusType");
            params.put("sysBusType", sysBusType);
        }
        return listByHQLAliasToBean(hql.toString(), params, NisCorpBusVO.class);
    }

    /**
     * 根据单位统计分配的业务类别
     *
     * @param corpId
     * @return
     */
    public int getBusTypeCountByCorpId(String corpId) {
        String sql = "select count(*) from nis_corp_bus where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 根据业务种类ID删除已分配的数据
     *
     * @param busId
     */
    public void deleteByBusId(String busId) {
        String sql = " delete from nis_corp_bus where bus_id=:busId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("busId", busId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 查询单位业务种类设置信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisCorpBusVO> getDataTablesViewUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct t.bus_type as busType,t.bus_type_name as busTypeName,t.cd_sign as cdSign");
        sql.append(" from nis_corp_bus n left join nis_bus_type t on n.bus_id = t.id ");
        sql.append(" where t.valid_Sign='1'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisCorpBusVO.class, "t", false);
    }

    /**
     * 获取已设置业务种类的单位
     *
     * @return
     */
    public List<String> getCorpIdList() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct corp_id from nis_corp_bus ");
        return this.createSQLQuery(sql.toString()).list();
    }

    /**
     * 根据单位ID获取分配的业务种类
     *
     * @param corpId
     * @return
     */
    public List<String> getByBusTypeList(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct nt.bus_Type as busType from Nis_Corp_Bus nc left join Nis_Bus_Type nt on nc.bus_Id = nt.id");
        sql.append(" where nt.valid_Sign = :validSign and nc.corp_Id = :corpId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("corpId", corpId);
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return this.createSQLQuery(sql.toString(), params).list();
    }


    public List<NisCorpBusVO> getByBusTypeForAll() {
        StringBuilder hql = new StringBuilder();
        hql.append("select nc.id as id,nc.corpId as corpId,nc.busId as busId,nt.id as busId,nt.busType as busType,nt.busTypeName as busTypeName,");
        hql.append("nt.cdSign,nt.itemCode as itemCode from NisCorpBus nc,NisBusType nt ");
        return listByHQLAliasToBean(hql.toString(), null, NisCorpBusVO.class);
    }
}
