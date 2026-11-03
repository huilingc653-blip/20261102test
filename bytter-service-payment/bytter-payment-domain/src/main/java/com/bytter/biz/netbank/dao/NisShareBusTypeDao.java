package com.bytter.biz.netbank.dao;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.entity.NisShareBusType;
import com.bytter.biz.netbank.vo.NisShareBusTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 共享业务类型维护DAO
 *
 * @author fangcf
 * @date 2025-05-15
 */
@Repository
public class NisShareBusTypeDao extends BaseDao<NisShareBusType, String> {

    /**
     * 共享业务类型列表
     *
     * @param input 入参
     * @return 共享业务类型数据
     */
    public DataTablesOutput<NisShareBusTypeVO> getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select nsbt.ID as id, nsbt.SHARE_BUS_TYPE as shareBusType, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName, nsbt.STATUS as status, ");
        sql.append(" nsbt.BUS_TYPE_ID as busTypeId, nbt.BUS_TYPE as busType, nbt.BUS_TYPE_NAME as busTypeName ");
        sql.append(" from NIS_SHARE_BUS_TYPE nsbt ");
        sql.append(" left join NIS_BUS_TYPE nbt on nbt.ID = nsbt.BUS_TYPE_ID ");
        sql.append(" where 1 = 1 ");
        return getDynamicBySQL(input, sql.toString(), NisShareBusTypeVO.class, "nsbt", false);
    }

    /**
     * 根据共享业务类型编码查询共享业务类型，id不为空则为修改操作
     *
     * @param shareBusType 共享业务类型编码
     * @param id           主键id
     */
    public List<NisShareBusType> listByShareBusType(String shareBusType, String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from NIS_SHARE_BUS_TYPE where SHARE_BUS_TYPE = :shareBusType ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("shareBusType", shareBusType);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and ID != :id ");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, NisShareBusType.class);
    }

    /**
     * 获取所有正常的共享业务类型
     *
     * @param busTypeId    业务类型id
     * @param shareBusType 共享业务类型编码
     * @return 共享业务类型集合
     */
    public List<NisShareBusType> listValidShareBusTypeByBusType(String busTypeId, String shareBusType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from NIS_SHARE_BUS_TYPE where STATUS = :status ");
        Map<String, Object> params = Maps.newHashMap();
        if (StringUtils.isNotEmpty(busTypeId) && !"all".equals(busTypeId)) {
            sql.append(" and ").append(SqlUtil.getInSql(busTypeId.split(CommonConstant.COMMA_SPLIT), "BUS_TYPE_ID", true));
        }
        if (StringUtils.isNotEmpty(shareBusType)) {
            sql.append(" and SHARE_BUS_TYPE = :shareBusType ");
            params.put("shareBusType", shareBusType);
        }
        sql.append(" order by SHARE_BUS_TYPE ");
        params.put("status", CommonConstant.VALID_SIGN_YES);
        return listBySQLAliasToBean(sql.toString(), params, NisShareBusType.class);
    }

    /**
     * 据共享业务类型id获取付款通知单信息
     *
     * @param shareBusTypeId 共享业务类型id
     * @return 业务类别与共享业务类型集合
     */
    public List<NisBillhead> listByShareBusTypeId(String shareBusTypeId) {
        String hql = " from NisBillhead b where b.shareBusTypeId = :shareBusTypeId";
        Map<String, Object> params = Maps.newHashMap();
        params.put("shareBusTypeId", shareBusTypeId);
        return this.listByHQLAliasToBean(hql, params, NisBillhead.class);
    }
}
