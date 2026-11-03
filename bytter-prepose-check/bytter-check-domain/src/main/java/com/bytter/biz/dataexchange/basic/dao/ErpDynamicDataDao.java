package com.bytter.biz.dataexchange.basic.dao;
import com.bytter.biz.dataexchange.basic.entity.ErpDynamicData;
import com.bytter.biz.dataexchange.basic.vo.ErpDynamicDataVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 自定义数据类型
 * @author zhengg
 * @date 2018-04-19 11:24:27
* @version 1.0
*
*/
@Repository
public class ErpDynamicDataDao extends BaseDao<ErpDynamicData,String> {
	
	public List<ErpDynamicData> listByCode(String code) {
		List<ErpDynamicData> list = listBy("code",code);
		return list;
	}
	
	/**
	 * 获取所有自定义数据类型
	 * @return 返回数据集合
	 */
	public List<ErpDynamicData> getAllDynamicDataType() {
		String hql="from ErpDynamicData order by dataTypeName asc ";
		List<ErpDynamicData> list = listByHQL(hql);
		return list;
	}
	/**
	 * 进行批量删除操作
     * @param ids 删除数据ID
	 * @return 返回删除笔数
	 */
    public int batchDelete(String[] ids) {
    	String hql="delete from ErpDynamicData where ";
    	Map<String,Object> params=new HashMap<>();
    	hql+= SqlUtil.getInSqlByParams(ids, "id", params);
        return this.batchExecute(hql, params);
    }
    /**
     * 查询数据集
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select edd.id,edd.type_code typeCode,edd.data_type_id dataTypeId,edd.inner_code innerCode,edd.inner_name innerName,edd.inner_reserve innerReserve, ");
        sql.append(" edd.external_code externalCode,edd.external_name externalName,edd.external_reserve externalReserve ");
        sql.append(" from erp_dynamic_data edd where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ErpDynamicDataVO.class, "edd", false);
    }
    
    /**
     * 查询数据集
     * @param dataTablesInput
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<ErpDynamicDataVO> getErpDynamicDataVOList(String typeCode,String dataTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select edd.type_code typeCode,et.type_name typeName,edt.DATA_TYPE_CODE dataTypeCode,DATA_TYPE_NAME dataTypeName,");
        sql.append(" edd.inner_code innerCode,edd.inner_name innerName,edd.inner_reserve innerReserve, ");
        sql.append(" edd.external_code externalCode,edd.external_name externalName,edd.external_reserve externalReserve ");
        sql.append(" from erp_dynamic_data edd ");
        sql.append(" left join erp_type et on edd.type_code=et.type_code ");
        sql.append(" left join erp_dynamic_data_type edt on edt.id=edd.data_type_id ");
        sql.append(" where 1=1 and edd.type_code=:typeCode ");
        sql.append(" and edd.data_type_id=:dataTypeId ");
        sql.append(" order by edd.inner_code,edd.create_date ");
        Map<String,Object> params=new HashMap<>();
        params.put("typeCode", typeCode);
        params.put("dataTypeId", dataTypeId);
        return this.listBySQLAliasToBean(sql.toString(), params, ErpDynamicDataVO.class);
    }
	/**
	 * 获取自定义数据映射
	 *
	 * @param typeCode     对应接口类型
	 * @param dateTypeCode 对应数据类型
	 * @return
	 * @author zuoy 2018-05-17
	 */
	public List<ErpDynamicDataVO> listErpDynamicDataByTypeCode(String typeCode, String dateTypeCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("select edd.id,edd.type_code typeCode,edd.data_type_id dataTypeId,edd.inner_code innerCode,edd.inner_name innerName,edd.inner_reserve innerReserve, ");
		sql.append(" edd.external_code externalCode,edd.external_name externalName,edd.external_reserve externalReserve, edt.data_type_code dataTypeCode ");
		sql.append(" from erp_dynamic_data edd left join erp_dynamic_data_type edt on edt.id=edd.data_type_id ");
		sql.append("  where 1=1 and edd.type_code=:typeCode and edt.data_type_code = :dateTypeCode ");
		Map<String,Object> params=new HashMap<>();
		params.put("typeCode", typeCode);
		params.put("dateTypeCode", dateTypeCode);
		return this.listBySQLAliasToBean(sql.toString(), params, ErpDynamicDataVO.class);
	}
}
