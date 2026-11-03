package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.ConPdMap;
import com.bytter.guarantee.domain.vo.ConPdMapVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
 * @Title: ConPdMapDao
 * @Description: 合同类型要素映射Dao
 * @author wangzy
 * @date 2019/11/8 13:35
 *
 */
@Repository
public class ConPdMapDao extends BaseDao<ConPdMap, String> {
    /**
     * 合同类型关键要素映射列表查询
     * 
     * @param input
     * @return
     *
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        StringBuilder sql = new StringBuilder("select ");
        sql.append(" p.id as id,t.con_Name as typeName,p.TYPE_ID as typeId,p.PD_CODE as pdCode ");
        sql.append(" from CON_PD_MAP p left join CON_TYPE t on p.TYPE_ID=t.id ");
        sql.append(" where t.VALID_SIGN=:validSign ");
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        return getDynamicBySQL(input, sql.toString(), ConPdMapVO.class, "", false, params);
    }
    /**
     * 根据产品类型代码和合同类型Id查询
     * 
     * @param typeId
     *            合同类型ID
     * @param pdCode
     *            产品Code
     * @return
     */
    public ConPdMapVO getConPdMapVO(String typeId, String pdCode) {
        StringBuilder sql = new StringBuilder("select p.CREATE_ID as createId,p.CREATE_TIME as createTime,");
        sql.append(" p.PD_CODE as pdCode,p.TYPE_ID as typeId,p.id as id from CON_PD_MAP p ");
        sql.append(" where TYPE_ID = :typeId and PD_CODE = :pdCode");
        Map<String, Object> params = new HashMap<>();
        params.put("typeId", typeId);
        params.put("pdCode", pdCode);
        return getBySQLAliasToBean(sql.toString(), params, ConPdMapVO.class);
    }
    /**
     * 根据映射表Id删除所有映射明细
     * 
     * @param mapId
     *            映射id
     * @return
     */
    public void deletePdDtlByMapId(String mapId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from CON_PD_MAPDTL where MAP_ID = :mapId ";
        params.put("mapId", mapId);
        createSQLQuery(sql, params).executeUpdate();
    }
}
