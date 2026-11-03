package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.entity.ConPdMapdtl;
import com.bytter.guarantee.domain.vo.ConPdMapDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: ConPdMapDtlDao
 * @Description:
 * @author wangzy
 * @date 2019/11/9 16:19
 *
 */
@Repository
public class ConPdMapDtlDao extends BaseDao<ConPdMapdtl, String> {
    /**
     * 根据映射id查询所有映射明细信息
     * 
     * @param pdCode
     *            产品Code
     * @param typeId
     *            类型ID
     * @return
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<ConPdMapDtlVO> listConPdMapDtlVO(String pdCode, String typeId) {
        StringBuilder sql = new StringBuilder(" select ");
        sql.append(
                " p.pdDtlCode as pdDtlCode,f.field_Name as fieldName,f.id as conTypeDtlId,f.REQUIRED_SIGN as requiredSign from CON_TYPE_FIELD f ");
        sql.append(
                " left join (select pDtl.PD_DTLCODE as pdDtlCode,pDtl.TYPE_DTLID as typeDtlId from CON_PD_MAPDTL pDtl inner join ");
        sql.append(
                " CON_PD_MAP m on m.id=pDtl.map_Id and m.TYPE_ID=:typeId and m.PD_CODE=:pdCode) p on p.typeDtlId=f.id where f.type_Id=:typeId");
        sql.append(" and f.VALID_SIGN = :validSign order by f.sort_num ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pdCode", pdCode);
        params.put("typeId", typeId);
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        List<ConPdMapDtlVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConPdMapDtlVO.class);
        if (listInfo != null) {
            return listInfo;
        }
        return null;
    }
    /**
     * 查询合同类型对应的所有关键要素
     * 
     * @param conTypeId
     * @return
     *
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<ConPdMapDtlVO> listTypeField(String conTypeId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as conTypeDtlId,f.FIELD_NAME as fieldName from CON_TYPE_FIELD f ");
        sql.append(" where f.valid_Sign=:validSign ");
        if (StringUtils.isNotEmpty(conTypeId)) {
            sql.append(" and f.TYPE_ID=:conTypeId ");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("conTypeId", conTypeId);
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        List<ConPdMapDtlVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params, ConPdMapDtlVO.class);
        if (listInfo != null) {
            return listInfo;
        }
        return null;
    }
}
