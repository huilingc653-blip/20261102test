package com.bytter.biz.netbank.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 业务类别表
 * @date 2017-05-17 19:23:52
 */
@Repository
public class NisBusTypeDao extends BaseDao<NisBusType, String> {
    /**
     * 查询业务种类维护列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput findListBysql(DataTablesInput input) {
        StringBuilder sb = new StringBuilder();
        sb.append("select n.id as id,n.bus_type as busType,n.bus_type_name as busTypeNameShow,n.cd_sign as cdSign,");
        sb.append(" n.item_code as itemCode,n.valid_sign as validSign,n.SYS_BUS_TYPE as sysBusType from Nis_Bus_Type n");
//		sb.append(" where 1 = 1 ");
        sb.append(" left join fbs_item f on n.item_code = f.item_code where 1 = 1 ");
        return getDynamicBySQL(input, sb.toString(), NisBusTypeVO.class, "n", false);
    }

    /**
     * 根据ID查询业务类型信息
     *
     * @param id
     * @return
     */
    public NisBusTypeVO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bus_type as busType,n.bus_type_name as busTypeName,n.cd_sign as cdSign,");
        sql.append(" n.item_code as itemCode,n.valid_sign as validSign,SYS_BUS_TYPE as sysBusType,SETTLE_TYPE as settleType from Nis_Bus_Type n");
//		sql.append(" left join fbs_item f on n.item_code = f.item_code ");
        sql.append(" where n.id = :id ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisBusTypeVO.class);
    }

    /**
     * 获取业务类型信息
     *
     * @param validSign 有效标志
     * @param cdSign    收付款标志
     * @return 业务类型信息集合
     */
    public List<NisBusTypeVO> listNisBusType(String validSign, String cdSign) {
        StringBuilder sb = new StringBuilder();
        sb.append("select n.id as id,n.bus_type as busType,n.bus_type_name as busTypeName,n.cd_sign as cdSign, ");
        sb.append(" n.item_code as itemCode,n.valid_sign as validSign,SYS_BUS_TYPE as sysBusType,SETTLE_TYPE as settleType from Nis_Bus_Type n ");
        sb.append(" where n.valid_sign = :validSign ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(cdSign)) {
            sb.append(" and n.cd_sign = :cdSign ");
            params.put("cdSign", cdSign);
        }
        sb.append(" order by n.bus_type ");
        params.put("validSign", validSign);
        return listBySQLAliasToBean(sb.toString(), params, NisBusTypeVO.class);
    }

    /**
     * 根据编码获取业务类型
     *
     * @param busType
     * @return
     */
    public NisBusType findBusType(String busType) {
        String sql = " from NisBusType n where n.busType = :busType ";
        return (NisBusType) createQuery(sql).setString("busType", busType).uniqueResult();
    }

    /**
     * 根据编码或Id获取业务类型
     *
     * @param busType 业务类型编号或ID。
     * @return 业务类型。
     */
    public NisBusType findBusTypeByIdOrType(String busType) {
        String sql = " from NisBusType n where n.id = :busType or n.busType = :busType ";
        return (NisBusType) createQuery(sql).setString("busType", busType).uniqueResult();
    }

    /**
     * 根据业务编码获取有效的业务类型数据。
     *
     * @param busType 业务编码。
     * @return 业务类型。
     */
    public NisBusTypeVO getByBusType(String busType) {
        String sb = "select n.id as id,n.bus_type as busType,n.bus_type_name as busTypeName,n.cd_sign as cdSign, " +
                " n.item_code as itemCode,n.valid_sign as validSign,sys_bus_type as sysBusType,settle_type as settleType from nis_bus_type n " +
                " where n.bus_type = :busType and n.valid_sign = :validSign ";
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        params.put("busType", busType);
        List<NisBusTypeVO> listData = listBySQLAliasToBean(sb, params, NisBusTypeVO.class);
        return ObjectUtil.isNotEmpty(listData) ? listData.get(0) : null;
    }

    /**
     * 获取有效的业务种类
     *
     * @return
     */
    public List<NisBusType> getValidBusType() {
        return listBy("validSign", CommonConstant.VALID_SIGN_YES);
    }

    /**
     * 根据系统业务类别得到业务类别
     *
     * @return
     */
    public List<NisBusTypeVO> getBySysBusType(String sysBusType) {
        StringBuilder sb = new StringBuilder();
        sb.append("select n.id as id,n.bus_type as busType,n.bus_type_name as busTypeName,n.cd_sign as cdSign, ");
        sb.append(" n.item_code as itemCode,f.item_name as itemName,n.valid_sign as validSign,n.SYS_BUS_TYPE as sysBusType,SETTLE_TYPE as settleType from Nis_Bus_Type n ");
        sb.append(" left join fbs_item f on n.item_code = f.item_code where n.valid_sign = :validSign and n.SYS_BUS_TYPE = :sysBusType  order by n.bus_type");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        params.put("sysBusType", sysBusType);
        return listBySQLAliasToBean(sb.toString(), params, NisBusTypeVO.class);
    }
}
