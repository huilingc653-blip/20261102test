package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 租赁计划主表
 * @date 2022-04-02 14:22:49
 */
@Repository
public class LeaseCapitalPlanDao extends BaseDao<LeaseCapitalPlan, String> {
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String isSaveSign,
            List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.rate as rate, p.repay_cycle as repayCycle, p.pay_way as payWay, p.pay_money as payMoney, ");
        sql.append(" p.status as status, p.is_savesign as isSaveSign,");
        sql.append(" p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        //TODO 补充单位名称
        sql.append(" l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId, l.corp_name as corpName,l.lease_no as leaseNo, ");
        sql.append(" l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid,l.ori_cur_name as oriCurName, l.ori_money as oriMoney, ");
        sql.append(" l.rmb_money as rmbMoney,l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus,  l.biz_status as bizStatus ");
        sql.append(" from CMS_FINANCING_LEASE_INFO l ");
        sql.append(" left join CMS_LEASE_CAPITAL_PLAN p on p.lease_id = l.id ");
        sql.append(" where 1 = 1 and l.status not in('0','11')");
        Map<String, Object> params = new HashMap<>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "l.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        if (CmsConstant.IS_TRUE.equals(isSaveSign)) {
            sql.append(" and ( p.is_savesign= '" + isSaveSign + "' or p.is_savesign is null ) ");
        }
        if (CmsConstant.IS_FALSE.equals(isSaveSign)) {
            sql.append(" and p.is_savesign= '" + isSaveSign + "' ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseCapitalPlanVO.class, "", false, params);
    }
    /**
     * 功能介绍 : 租金计划数据查询根据Id集合
     *
     * @param dataTablesInput
     * @param ids
     * @return
     */
    public DataTablesOutput getInfoByIds(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.rate as rate, p.repay_cycle as repayCycle, p.pay_way as payWay, p.pay_money as payMoney, ");
        sql.append(" p.status as status, p.is_savesign as isSaveSign,");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(
                " l.rmb_money as rmbMoney, l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus, l.biz_status as bizStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN p ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on p.lease_id = l.id ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "l.id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseCapitalPlanVO.class, null, false);
    }
    /**
     * 筛选添加租金计划的数据
     *
     * @return 筛选添加租金计划的数据
     */
    public List<LeaseCapitalPlanVO> getAllDataByPlan(List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.rate as rate, p.repay_cycle as repayCycle, p.pay_way as payWay, p.pay_money as payMoney, ");
        sql.append(" p.status as status, p.is_savesign as isSaveSign,");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(
                " l.rmb_money as rmbMoney, l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN p ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on p.lease_id = l.id ");
        sql.append(" where 1 = 1 and l.status in (:status) ");
        Map<String, Object> params = new HashMap<>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "l.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        params.put("status", new Integer[] { CmsConstant.APPROVED, CmsConstant.LEASE_REPAIR_TEMP,
                CmsConstant.LEASE_REPAIR_COMMITAPPROVE, CmsConstant.LEASE_REPAIR_APPROVED });
        return listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanVO.class);
    }
    public List<LeaseCapitalPlanVO> getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.lease_id as leaseId, p.lease_code as leaseCode,p.begin_date as beginDate, p.end_date as endDate, ");
        sql.append(
                " p.rate as rate, p.repay_cycle as repayCycle, p.repay_type as repayType,p.pay_way as payWay, p.corp_id as corpId, ");
        //TODO 补充单位名称
        sql.append(" l.corp_name as corpName, ");
        sql.append(" p.pay_money as payMoney, p.status as status, p.is_savesign as isSaveSign, ");
        sql.append(" p.repay_cycel as repayCycel, p.repay_begin_date as repayBeginDate, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN p ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on p.lease_id = l.id ");
        sql.append(" where 1 = 1 and p.ID=:id ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanVO.class);
    }
    public List<LeaseCapitalPlanVO> getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.repay_type as repayType,p.repay_begin_date as repayBeginDate, ");
        sql.append(" p.repay_cycel as repayCycel, p.begin_date as beginDate, p.end_date as endDate, ");
        sql.append(" p.rate as rate, p.repay_cycle as repayCycle, p.pay_way as payWay, ");
        sql.append(
                " case when p.pay_money is null then l.ori_money else p.pay_money end as payMoney, p.status as status,");
        sql.append(
                " p.create_by as createBy, p.create_date as createDate, p.update_by as updateBy, p.update_date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        sql.append(" l.rmb_money as rmbMoney, l.status as leaseStatus ");
        sql.append(" from CMS_FINANCING_LEASE_INFO l ");
        sql.append(" left join CMS_LEASE_CAPITAL_PLAN p on p.lease_id = l.id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(leaseId)) {
            sql.append(" and l.id = :leaseId ");
            params.put("leaseId", leaseId);
        }
        List<LeaseCapitalPlanVO> list = listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanVO.class);
        return list;
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select plan.id as id,plan.begin_date as beginDate, plan.end_date as endDate, ");
        sql.append(
                " plan.rate as rate, plan.repay_cycle as repayCycle, plan.pay_way as payWay, plan.pay_money as payMoney, ");
        sql.append(" plan.status as status, plan.is_savesign as isSaveSign,");
        sql.append(
                " plan.create_By as createBy, plan.create_Date as createDate, plan.update_By as updateBy, plan.update_Date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(" l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode, ");
        sql.append(" l.ori_curid as oriCurid, l.ori_money as oriMoney, l.rmb_money as rmbMoney, ");
        sql.append(" l.status as leaseStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN plan ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on plan.lease_id = l.id ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "plan.id")).append(" ");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseCapitalPlanVO.class, null, false);
    }
    /**
     * 功能介绍 : 补录审批查询列表<br>
     */
    public DataTablesOutput getDataUpdateApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select plan.id as id,plan.begin_date as beginDate, plan.end_date as endDate, ");
        sql.append(
                " plan.rate as rate, plan.repay_cycle as repayCycle, plan.pay_way as payWay, plan.pay_money as payMoney, ");
        sql.append(" plan.status as status, plan.is_savesign as isSaveSign,");
        sql.append(
                " plan.create_By as createBy, plan.create_Date as createDate, plan.update_By as updateBy, plan.update_Date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(" l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode, ");
        sql.append(" l.ori_curid as oriCurid, l.ori_money as oriMoney, l.rmb_money as rmbMoney, ");
        sql.append(" l.status as leaseStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN plan ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on plan.lease_id = l.id ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "plan.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseCapitalPlanVO.class, null, false);
    }
}
