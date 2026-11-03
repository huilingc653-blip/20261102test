package com.bytter.guarantee.domain.dao;
import com.bytter.guarantee.domain.vo.ConTypeFieldVO;
import com.bytter.guarantee.constant.ConConstant;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.bytter.guarantee.domain.entity.ConTypeField;
import com.bytter.core.base.BaseDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @Title: 详细信息维护Dao
 * @Package com.bytter.biz.contract.dao
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-6 下午4:04:39
 * @version V1.0
 */
@Repository
public class ConTypeFieldDao extends BaseDao<ConTypeField, String> {
    public void delFileIdTypeId(String typeId) {
        createSQLQuery("delete from CON_TYPE_FIELD where type_id=?", typeId).executeUpdate();
    }
    @SuppressWarnings("unchecked")
    public List<String> queryFileIdTypeIdList(String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id from CON_TYPE_FIELD t  where TYPE_ID=:typeId");
        SQLQuery query = createSQLQuery(sql.toString());
        query.setString("typeId", typeId);
        List<String> list = query.list();
        return list;
    }
    /**
     * 功能介绍 : 根据合同类型id获取有效的合同类型字段明细表<br>
     * <b>日期：</b>2019年11月06日<br>
     * 
     * @version 1.0
     * @author zhenghj
     */
    public List<ConTypeFieldVO> getConTypeFieldById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select id,TYPE_ID as typeId,FIELD_NAME as fieldName,REQUIRED_SIGN as requiredSign,FIELD_TYPE as fieldType,SORT_NUM as sortNum,RMK from CON_TYPE_FIELD  where VALID_SIGN=:validSign and TYPE_ID=:typeId order by SORT_NUM ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("typeId", id);
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        return listBySQLAliasToBean(sql.toString(), params, ConTypeFieldVO.class);
    }
    /**
     * 
     * @Title: getConTypeFieldByTypeId @Description: TODO(typid查询详细) @param @param id @param @return 参数 @return
     *         List<ConTypeFieldVO> 返回类型 @throws
     */
    public List<ConTypeFieldVO> getConTypeFieldByTypeId(String typeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select id,TYPE_ID as typeId,FIELD_NAME as fieldName,REQUIRED_SIGN as requiredSign,FIELD_TYPE as fieldType,SORT_NUM as sortNum,RMK,VALID_SIGN as validSign from CON_TYPE_FIELD  where TYPE_ID=:typeId order by SORT_NUM ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("typeId", typeId);
        return listBySQLAliasToBean(sql.toString(), params, ConTypeFieldVO.class);
    }
}
