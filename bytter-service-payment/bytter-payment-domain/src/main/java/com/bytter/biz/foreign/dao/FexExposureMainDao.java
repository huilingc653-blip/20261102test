package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexExposureMain;
import com.bytter.biz.foreign.vo.FexExposureMainVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 单位敞口上报主表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexExposureMainDao extends BaseDao<FexExposureMain, String> {

    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.corp_Id as corpId,sc.name corpName,t.cur_Id as curId,");
        sql.append(" t.template_Id as templateId,tm.template_Name as templateName,t.enrol_Date as enrolDate,");
        sql.append(" t.status as status from Fex_Exposure_Main t");
        sql.append(" left join Fex_Exposure_Template tm on tm.id=t.template_Id ");
        sql.append(" left join SYS_CORP sc on sc.id=t.corp_Id ");
        sql.append("  where 1=1");
        sql.append(joinSql);
        //币别
        String curId = (String) dataTablesInput.getFormdata().get("likes_curId");
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and t.cur_Id like '%" + curId + "%'");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexExposureMainVO.class, "t", false);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public FexExposureMainVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.corp_Id as corpId,tt.name as corpName,t.cur_Id as curId,");
        sql.append(" t.template_Id as templateId,tm.template_Name as templateName,tm.template_Code as templateCode,t.enrol_Date as enrolDate,");
        sql.append(" t.is_Year_Fill as isYearFill,t.is_Total_Fill as isTotalFill,t.total_Fill_Period as totalFillPeriod,");
        sql.append(" t.total_Fill_Unit as totalFillUnit,t.scroll_Fill_Period as scrollFillPeriod,t.scroll_Fill_Unit as scrollFillUnit,");
        sql.append(" t.scroll_Resolve_Period as scrollResolvePeriod,t.scroll_Resolve_Unit as scrollResolveUnit,");
        sql.append(" t.status as status from Fex_Exposure_Main t");
        sql.append(" left join Fex_Exposure_Template tm on tm.id=t.template_Id  ");
        sql.append(" left join sys_corp tt on tt.id=t.corp_Id  ");
        sql.append(" where t.id=:id");
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexExposureMainVO.class);
    }

    /**
     * 删除上报明细
     * @param id
     */
    public void deleteById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from Fex_Exposure_Main where id=:id)");
        Map<String,String> params = new HashMap<String,String>();
        params.put("id",id);
        this.createSQLQuery(sql.toString(),params).executeUpdate();
    }

    /**
     * 需审核列表数据
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput<FexExposureMainVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append(" t.template_Id as templateId,tm.template_Name as templateName,t.enrol_Date as enrolDate,");
        sql.append(" t.status as status from Fex_Exposure_Main t");
        sql.append(" left join Fex_Exposure_Template tm on tm.id=t.template_Id where 1=1");
        sql.append(" and ").append(businessSql);
        //币别
        String curId = (String) dataTablesInput.getFormdata().get("likes_curId");
        if (StringUtils.isNotBlank(curId)) {
            sql.append(" and t.cur_Id like '%" + curId + "%'");
        }
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)) {
            //过滤已审数据
            sql.append(" and t.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexExposureMainVO.class, "t", false);
    }

    /**
     * 根据单位ID和单位模板获取对应的敞口上报信息
     *
     * @param corpId
     * @param templateId
     * @param beginDate
     * @param endDate
     * @return
     */
    public FexExposureMain getCorpExposure(String corpId, String templateId, Date beginDate, Date endDate) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from FexExposureMain t where t.corpId = :corpId and t.templateId = :templateId ");
        if (beginDate != null) {
            hql.append(" and t.enrolDate >= :beginDate and t.enrolDate <= :endDate ");
        }
        hql.append(" order by t.enrolDate desc");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("templateId", templateId);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        List<FexExposureMain> list=this.listByHQL(hql.toString(), params);
        FexExposureMain fexExposureMain = null;
        if(list!=null && list.size()>0){
            fexExposureMain = list.get(0);
        }
        return fexExposureMain;
    }

    /**
     * 根据单位Id和模板id获取敞口上报信息
     *
     * @param corpId
     * @param templateId
     * @return
     */
    public List<FexExposureMain> listByCorpIdAndTemplateId(String corpId, String templateId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from FexExposureMain t where t.corpId = :corpId and t.templateId = :templateId ");
        hql.append(" order by t.enrolDate desc");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("templateId", templateId);
        return listByHQLAliasToBean(hql.toString(), params, FexExposureMain.class);
    }

    /**
     * 根据子单获取上报的币种集合
     * @param childMainIds
     * @return
     */
    public List<String> getCurIdsByChildCorp(List<String> childMainIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct t.cur_Id from Fex_Exposure t ");
        sql.append(" where ").append(SqlUtil.getInSql(childMainIds, "t.main_id", true));
        sql.append(" group by t.cur_Id");
        return this.createSQLQuery(sql.toString()).list();
    }

    /**
     * 根据子单获取上报的主信息集合
     * @param corpIds
     * @param templateId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<String> getMainIdsByChildCorp(List<String> corpIds, String templateId, Date beginDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id from Fex_Exposure_Main t where t.template_Id = :templateId ");
        sql.append(" and t.enrol_Date >= :beginDate and t.enrol_Date <= :endDate ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds, "t.corp_Id", true));
        sql.append(" and t.status=:status");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("templateId", templateId);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        params.put("status", CommonConstant.STATUE_CHECK);
        return this.createSQLQuery(sql.toString(), params).list();
    }
}
