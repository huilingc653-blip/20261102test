package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpType;
import com.bytter.biz.dataexchange.basic.vo.ErpTypeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 接口类型
* @author onlineGenerator
* @date 2017-12-13 14:53:07
* @version 1.0
*
*/
@Repository
public class ErpTypeDao extends BaseDao<ErpType,String> {
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id,t.type_code AS typeCode,t.type_name AS typeName,t.rmk FROM ERP_TYPE t  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ErpTypeVO.class, "t", false);
    }
    public List<ErpTypeVO> listErpType() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id,t.type_code AS typeCode,t.type_name AS typeName,t.rmk FROM ERP_TYPE t  ");
        Map<String, String> param = new HashMap<String, String>();
        return listBySQLAliasToBean(sql.toString(),param, ErpTypeVO.class);
    }
    public ErpTypeVO getErpTypeVO(ErpTypeVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id,t.type_code AS typeCode,t.type_name AS typeName,t.rmk FROM ERP_TYPE t where 1=1 ");
        Map<String, String> param = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(vo.getId())){
            sql.append(" and t.id=:id ");
            param.put("id",vo.getId());
        }
        if(StringUtils.isNotEmpty(vo.getTypeCode())){
            sql.append(" and t.type_code=:type_code ");
            param.put("type_code",vo.getTypeCode());
        }
        if(StringUtils.isNotEmpty(vo.getTypeName())){
            sql.append(" and t.type_name=:type_name ");
            param.put("type_name",vo.getTypeName());
        }
        if(StringUtils.isNotEmpty(vo.getRmk())){
            sql.append(" and t.rmk=:rmk ");
            param.put("rmk",vo.getRmk());
        }
        return getBySQLAliasToBean(sql.toString(),param, ErpTypeVO.class);
    }
    public ErpType getErpType(ErpTypeVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT t.id,t.type_code AS typeCode,t.type_name AS typeName,t.rmk FROM ERP_TYPE t where 1=1 ");
        Map<String, String> param = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(vo.getId())){
            sql.append(" and t.id=:id ");
            param.put("id",vo.getId());
        }
        if(StringUtils.isNotEmpty(vo.getTypeCode())){
            sql.append(" and t.type_code=:type_code ");
            param.put("type_code",vo.getTypeCode());
        }
        if(StringUtils.isNotEmpty(vo.getTypeName())){
            sql.append(" and t.type_name=:type_name ");
            param.put("type_name",vo.getTypeName());
        }
        if(StringUtils.isNotEmpty(vo.getRmk())){
            sql.append(" and t.rmk=:rmk ");
            param.put("rmk",vo.getRmk());
        }
        return getBySQLAliasToBean(sql.toString(),param, ErpType.class);
    }
}
