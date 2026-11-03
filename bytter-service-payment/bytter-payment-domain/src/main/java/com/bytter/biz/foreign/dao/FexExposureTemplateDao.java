package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposureTemplate;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @Title: Dao
 * @Description: 敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexExposureTemplateDao extends BaseDao<FexExposureTemplate, String> {

    /**
     * 敞口模板表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.template_Code as templateCode,f.template_Name as templateName,");
        sql.append(" f.is_Year_Fill as isYearFill,f.is_Total_Fill as isTotalFill,f.total_Fill_Period as totalFillPeriod,f.total_Fill_Unit as totalFillUnit,");
        sql.append(" f.scroll_Fill_Period as scrollFillPeriod,f.scroll_Fill_Unit as scrollFillUnit,f.scroll_Resolve_Period as scrollResolvePeriod,f.scroll_Resolve_Unit as scrollResolveUnit,");
        sql.append(" f.valid_Sign as validSign,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate");
        sql.append(" from Fex_Exposure_Template f where 1=1");
        return sql.toString();
    }

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexExposureTemplateVO.class, "f", false);
    }

    /**
     * 根据ID获取敞口信息
     *
     * @param id
     * @return
     */
    public FexExposureTemplateVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexExposureTemplateVO.class);
    }
}
