package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCorpExposureTemplate;
import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @Title: Dao
 * @Description: 单位敞口模板科目表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexCorpExposureTemplateDao extends BaseDao<FexCorpExposureTemplate, String> {

    /**
     * 敞口模板表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.corp_Id as corpId,  sc.name corpName,f.cur_Id as curId,f.template_id as TemplateId,");
        sql.append(" t.template_Code as templateCode,t.template_Name as templateName,");
        sql.append(" f.valid_Sign as validSign,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate");
        sql.append(" from Fex_Corp_Exposure_Template f");
        sql.append(" left join Fex_Exposure_Template t on f.template_id=t.id");
        sql.append(" left join SYS_CORP sc on f.corp_Id=sc.id");
        sql.append(" where 1=1");
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
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCorpExposureTemplateVO.class, "f", false);
    }

    /**
     * 根据ID获取敞口信息
     *
     * @param id
     * @return
     */
    public FexCorpExposureTemplateVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexCorpExposureTemplateVO.class);
    }

    /**
     * 根据单位获取模板信息
     * @param corpId
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.valid_Sign=:validSign and t.valid_Sign=:validSign and f.corp_Id=:corpId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("corpId", corpId);
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexCorpExposureTemplateVO.class);
    }

    /**
     * 根据模板Id获取单位模板信息
     * @param templateId
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByTemplateId(String templateId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.template_Id=:templateId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("templateId", templateId);
        return listBySQLAliasToBean(sql.toString(), params, FexCorpExposureTemplateVO.class);
    }

    /**
     * 根据模板Id获取单位模板信息
     * @param templateId
     * @return
     */
    public FexCorpExposureTemplateVO getByCorpIdAndTemplateId(String corpId, String templateId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and f.corp_Id=:corpId and f.template_Id=:templateId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("corpId", corpId);
        params.put("templateId", templateId);
        return getBySQLAliasToBean(sql.toString(), params, FexCorpExposureTemplateVO.class);
    }

    /**
     * 根据单位获取模板信息
     * @param corpId
     * @return
     */
    public List<FexCorpExposureTemplateVO> listByCorpIdForCollectCorp(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct m.corp_id as corpId,m.cur_Id as curId,");
        sql.append(" m.template_id as TemplateId,t.template_Code as templateCode,t.template_Name as templateName");
        sql.append(" from Fex_Exposure_Main m");
        sql.append(" left join Fex_Exposure_Template t on m.template_id=t.id");
        sql.append(" where m.corp_Id=:corpId and t.valid_Sign=:validSign");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("corpId", corpId);
        params.put("validSign", CommonConstant.YES);
        return listBySQLAliasToBean(sql.toString(), params, FexCorpExposureTemplateVO.class);
    }
}
