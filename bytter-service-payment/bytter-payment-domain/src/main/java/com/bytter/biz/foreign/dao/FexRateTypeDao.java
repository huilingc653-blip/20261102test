package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.vo.FexRateTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinl
 * @version 1.0
 * @Title: dao
 * @Description: 外汇牌价类型表
 * @date 2020-07-09
 */
@Repository
public class FexRateTypeDao extends BaseDao<FexRateType, String> {
    private StringBuilder commonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id,t.TYPE_NAME,t.RMK,t.RATE_INTERFACE_TYPE,t.BIF_CODE,t.IS_NEED_QUERY_CUR,t.VALID_SIGN, ");
        sql.append(" t.CREATE_DATE,t.CREATE_BY,t.UPDATE_DATE,t.UPDATE_BY,b.name as bifCodeName ");
        sql.append(" FROM FEX_RATE_TYPE t");
        sql.append(" left join BIS_BIF_INIT b on t.BIF_CODE = b.id");
        sql.append(" WHERE 1=1 ");
        return sql;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(commonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexRateTypeVO.class, "t", false);
    }

    public FexRateTypeVO getByVO(FexRateTypeVO vo){
        StringBuilder sql = new StringBuilder();
        sql.append(commonSql());
        if(StringUtils.isNotBlank(vo.getRateInterfaceType())){
            sql.append(" AND t.RATE_INTERFACE_TYPE = '").append(vo.getRateInterfaceType()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getBifCode())){
            sql.append(" AND t.BIF_CODE = '").append(vo.getBifCode()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getId())){
            sql.append(" AND t.id = '").append(vo.getId()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getTypeName())){
            sql.append(" AND t.TYPE_NAME = '").append(vo.getTypeName()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getValidSign())){
            sql.append(" AND t.VALID_SIGN = '").append(vo.getValidSign()).append("'");
        }
        return getBySQLAliasToBean(sql.toString(), null,FexRateTypeVO.class);
    }

    public List<FexRateTypeVO> getFexRateTypeList(FexRateTypeVO vo){
        StringBuilder sql = new StringBuilder();
        sql.append(commonSql());
        if(StringUtils.isNotBlank(vo.getRateInterfaceType())){
            sql.append(" AND t.RATE_INTERFACE_TYPE = '").append(vo.getRateInterfaceType()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getBifCode())){
            sql.append(" AND t.BIF_CODE = '").append(vo.getBifCode()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getId())){
            sql.append(" AND t.id = '").append(vo.getId()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getTypeName())){
            sql.append(" AND t.TYPE_NAME = '").append(vo.getTypeName()).append("'");
        }
        if(StringUtils.isNotBlank(vo.getValidSign())){
            sql.append(" AND t.VALID_SIGN = '").append(vo.getValidSign()).append("'");
        }
        return listBySQLAliasToBean(sql.toString(), null,FexRateTypeVO.class);
    }

    /**
     * 获取跟其他业务的关联数量
     */
    public long checkAssociated(String id,Map<String,Map<String,String>> tableMap){
        long check = 0;
        for(String table:tableMap.keySet()){
            check = check +getCount(table,tableMap.get(table));
        }
        return check;
    }

    /**
     * 获取关联数量
     * @param table 表名
     * @param map 列名/值
     */
    private long getCount(String table, Map<String,String> map){
        StringBuilder sql = new StringBuilder();
        sql.append("select count(id) from ");
        sql.append(table);
        sql.append(" WHERE ");
        for(String key:map.keySet()){
            sql.append(key).append("=").append("'").append( map.get(key)).append("'").append(" OR ");
        }
        String substring = sql.toString().substring(0, sql.toString().length() - 3);
        SQLQuery query = getSession().createSQLQuery(substring);
        List list = query.list();
        BigDecimal b = (BigDecimal)list.get(0);
        return b.intValue();
    }

    /**
     * 根据外汇牌价接口类别获取外汇牌价类型
     * @param rateInterfaceType 外汇牌价接口类别 1-银企直联 2-网络机器人
     * @return
     */
    public List<FexRateTypeVO> listByRateInterfaceType(String rateInterfaceType){
        StringBuilder sql = new StringBuilder(this.commonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(rateInterfaceType)) {
                sql.append(" and t.rate_interface_type=:rateInterfaceType");
                params.put("rateInterfaceType", rateInterfaceType);
        }
        sql.append(" and t.valid_Sign=:validSign");
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexRateTypeVO.class);
    }

    /**
     * 获取所有有效的外汇牌价类型
     * @return
     */
    public List<FexRateTypeVO> listFexRateTypeAll(){
        StringBuilder sql = new StringBuilder(this.commonSql());
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" and t.valid_Sign=:validSign");
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexRateTypeVO.class);
    }

    public FexRateTypeVO getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(commonSql());
        if(StringUtils.isNotBlank(id)){
            sql.append(" AND t.id = '").append(id).append("'");
        }
        return getBySQLAliasToBean(sql.toString(), null,FexRateTypeVO.class);
    }
}
