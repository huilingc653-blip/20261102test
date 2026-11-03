package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpDataType;
import com.bytter.biz.dataexchange.basic.entity.ErpType;
import com.bytter.biz.dataexchange.basic.vo.ErpDataTypeVO;
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
* @Description: 数据类型
* @author onlineGenerator
* @date 2017-12-13 15:29:54
* @version 1.0
*
*/
@Repository
public class ErpDataTypeDao extends BaseDao<ErpDataType,String> {
    public List<ErpDataType> listErpType() {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.code as code,t.name as name,");
        sql.append("t.table_name as tableName,t.key_field as keyField,");
        sql.append("t.value_field as valueField,t.query_condition as queryCondition,");
        sql.append("t.valid_sign as validSign,t.rmk as rmk ");
        sql.append(" from ERP_DATA_TYPE t ");
        Map<String, String> param = new HashMap<String, String>();
        return listBySQLAliasToBean(sql.toString(),param, ErpDataType.class);
    }
    public ErpDataType getErpDataType(ErpDataType vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.code as code,t.name as name,");
        sql.append("t.table_name as tableName,t.key_field as keyField,");
        sql.append("t.value_field as valueField,t.query_condition as queryCondition,");
        sql.append("t.valid_sign as validSign,t.rmk as rmk ");
        sql.append(" from ERP_DATA_TYPE t where 1=1 ");
        Map<String, String> param = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(vo.getId())){
            sql.append(" and t.id=:id ");
            param.put("id",vo.getId());
        }
        if(StringUtils.isNotEmpty(vo.getCode())){
            sql.append(" and t.code=:code ");
            param.put("code",vo.getCode());
        }
        if(StringUtils.isNotEmpty(vo.getName())){
            sql.append(" and t.name=:name ");
            param.put("name",vo.getName());
        }
        if(StringUtils.isNotEmpty(vo.getKeyField())){
            sql.append(" and t.key_field=:key_field ");
            param.put("key_field",vo.getKeyField());
        }
        if(StringUtils.isNotEmpty(vo.getRmk())){
            sql.append(" and t.rmk=:rmk ");
            param.put("rmk",vo.getRmk());
        }
        return getBySQLAliasToBean(sql.toString(),param, ErpDataType.class);
    }
    public ErpDataTypeVO getErpDataTypeVO(ErpDataTypeVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.code as code,t.name as name,");
        sql.append("t.table_name as tableName,t.key_field as keyField,");
        sql.append("t.value_field as valueField,t.query_condition as queryCondition,");
        sql.append("t.valid_sign as validSign,t.rmk as rmk ");
        sql.append(" from ERP_DATA_TYPE t where 1=1 ");
        Map<String, String> param = new HashMap<String, String>();
        if(StringUtils.isNotEmpty(vo.getId())){
            sql.append(" and t.id=:id ");
            param.put("id",vo.getId());
        }
        if(StringUtils.isNotEmpty(vo.getCode())){
            sql.append(" and t.code=:code ");
            param.put("code",vo.getCode());
        }
        if(StringUtils.isNotEmpty(vo.getName())){
            sql.append(" and t.name=:name ");
            param.put("name",vo.getName());
        }
        if(StringUtils.isNotEmpty(vo.getKeyField())){
            sql.append(" and t.key_field=:key_field ");
            param.put("key_field",vo.getKeyField());
        }
        if(StringUtils.isNotEmpty(vo.getRmk())){
            sql.append(" and t.rmk=:rmk ");
            param.put("rmk",vo.getRmk());
        }
        return getBySQLAliasToBean(sql.toString(),param, ErpDataTypeVO.class);
    }
    /**
     * 查询数据集
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.id as id,t.code as code,t.name as name,");
        sql.append("t.table_name as tableName,t.key_field as keyField,");
        sql.append("t.value_field as valueField,t.query_condition as queryCondition,");
        sql.append("t.valid_sign as validSign,t.rmk as rmk ");
        sql.append(" from ERP_DATA_TYPE t");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ErpDataTypeVO.class, "t", false);
    }
    public List<ErpDataType> listByCode(String code) {
        List<ErpDataType> list = listBy("code",code);
        return list;
    }
}
