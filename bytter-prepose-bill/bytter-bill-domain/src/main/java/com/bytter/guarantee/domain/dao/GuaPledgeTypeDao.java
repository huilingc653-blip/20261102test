package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.GuaPledgeType;
import com.bytter.guarantee.domain.vo.GuaPledgeTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GuaPledgeTypeDao extends BaseDao<GuaPledgeType, String> {
    /**
     * 查询抵质押物类型
     * 
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput queryGuaPledgeType(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B WHERE B.VALID_SIGN='1' ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaPledgeTypeVO.class, "", false);
    }
    public GuaPledgeType getGuaPledgeTypeById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B ");
        sql.append("WHERE B.ID=:id");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, GuaPledgeType.class);
    }
    public List<GuaPledgeTypeVO> getListByValidSign() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B WHERE B.VALID_SIGN='1' ");
        return listBySQLAliasToBean(sql.toString(), null, GuaPledgeTypeVO.class);
    }
    public List<GuaPledgeTypeVO> getListByValidSign1(String typeAttr) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B WHERE B.VALID_SIGN='1'  AND B.TYPE_ATTR=:typeAttr ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("typeAttr", typeAttr);
        return listBySQLAliasToBean(sql.toString(), params, GuaPledgeTypeVO.class);
    }
    /**
     * 校验抵质押物类型维护表中的相应数据能否被修改和删除
     * 
     * @param id
     * @return
     */
    public boolean dateCheck(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) AS count FROM GUA_PLEDGE_INFO B WHERE B.PLEDGE_TYPE=:id ");
        sql.append("AND B.END_DATE >=:sysdate  AND B.STATUS<>-2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sysdate", new java.sql.Date(System.currentTimeMillis()));
        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            BigDecimal count = (BigDecimal) map.get("COUNT");
            if (count != null) {
                int i = count.compareTo(BigDecimal.ZERO);
                if (i == 1) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 根据抵质押物类型编号查询列表
     */
    public List<GuaPledgeType> getListByTypeCode(String typeCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B WHERE B.VALID_SIGN='1' and B.TYPE_CODE=:typeCode");
        params.put("typeCode", typeCode);
        return listBySQLAliasToBean(sql.toString(), params, GuaPledgeType.class);
    }
    /**
     * 根据类型名称和类型查询列表
     */
    public GuaPledgeType getListByTypeCode(String typeName, String typeAttr) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("SELECT B.ID AS id,B.TYPE_CODE AS typeCode,B.TYPE_NAME AS typeName,");
        sql.append("B.TYPE_ATTR AS typeAttr,B.VALID_SIGN AS validSign,B.CREATE_DATE AS createDate,");
        sql.append("B.CREATE_BY AS createBy,B.UPDATE_DATE AS updateDate,B.UPDATE_BY AS	updateBy ");
        sql.append(" FROM GUA_PLEDGE_TYPE B WHERE B.VALID_SIGN='1' ");
        sql.append(" and B.TYPE_ATTR=:typeAttr and B.TYPE_NAME=:typeName");
        params.put("typeName", typeName);
        params.put("typeAttr", typeAttr);
        return getBySQLAliasToBean(sql.toString(), params, GuaPledgeType.class);
    }
}
