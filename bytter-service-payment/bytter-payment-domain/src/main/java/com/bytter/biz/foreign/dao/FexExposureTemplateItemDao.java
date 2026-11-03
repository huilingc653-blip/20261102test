package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposureTemplateItem;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 敞口模板科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexExposureTemplateItemDao extends BaseDao<FexExposureTemplateItem, String> {

    /**
     * 根据模板ID删除科目信息
     * @param templateId
     */
    public void deleteByTemplateId(String templateId) {
        String sql = "delete from fex_exposure_template_item where template_id=:templateId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("templateId", templateId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据模板ID获取模板对应的科目列表
     * @param templateId
     * @return
     */
    public List<FexExposureTemplateItemVO> listVOByTemplateId(String templateId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.template_Id as templateId,f.item_Id as itemId,f.item_Code as itemCode,");
        sql.append(" i.item_Name as itemName, i.super_Item_Code as superItemCode,i.sign as sign,i.is_Natural_Hedge as isNaturalHedge");
        sql.append(" from fex_exposure_template_item f left join fex_exposure_item i on i.id=f.item_Id");
        sql.append(" where f.template_Id=:templateId order by f.item_Code");
        HashMap<String, String> params = new HashMap<>();
        params.put("templateId", templateId);
        return listBySQLAliasToBean(sql.toString(), params, FexExposureTemplateItemVO.class);
    }

    /**
     * 根据科目获取模板信息
     * @param itemCodes
     * @return
     */
    public List<FexExposureTemplateItemVO> listVOByItemCodes(List<String> itemCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.template_Id as templateId,f.item_Id as itemId,f.item_Code as itemCode,");
        sql.append(" i.item_Name as itemName, i.super_Item_Code as superItemCode,i.sign as sign,i.is_Natural_Hedge as isNaturalHedge");
        sql.append(" from fex_exposure_template_item f left join fex_exposure_item i on i.id=f.item_Id");
        sql.append(" where ").append(getSQLIn(itemCodes, 900, "f.item_code"));
        return listBySQLAliasToBean(sql.toString(), null, FexExposureTemplateItemVO.class);
    }
}
