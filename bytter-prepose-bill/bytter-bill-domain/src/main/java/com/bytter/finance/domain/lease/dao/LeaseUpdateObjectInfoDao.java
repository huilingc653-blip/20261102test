package com.bytter.finance.domain.lease.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.lease.entity.LeaseItemType;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateObjectInfoVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class LeaseUpdateObjectInfoDao extends BaseDao<LeaseItemType, String> {
    /*
     * 标的物修改列表
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryLeaseObjectInfoList(DataTablesInput input, String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT M.ID as id ,M.LEASE_ITEM_CODE as leaseItemCode,M.LEASE_ITEM_NAME as leaseItemName,M.LEASE_TYPE_ID leaseTypeId, ");
        sql.append("M.BRAND as  brand, M.SPE_MODEL as speModel,M.CAL_CORP as calCorp,M.NUMB as numb,M.CUR_ID as curId,M.CUR_NAME as curName, ");
        sql.append("M.NOW_MONEY as nowMoney,M.SAVE_ADDR as saveAddr,M.MAKE_BY as makeBy,M.BUY_DATE buyDate,M.RMK as rmk,M.STATUS as status,");
        sql.append("T.NAME as name,M.OLD_MONEY oldMoney");
        sql.append(" FROM CMS_LEASE_SUB_MATER M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID left join CMS_TENANCY_ITEMS_TYPES T on M.LEASE_TYPE_ID=T.id ");
        sql.append(" WHERE 1=1 and M.LEASE_ID=:id ");
        sql.append(" and (M.status=95 or M.status=94 or M.status=1 or M.status=12 or M.status=-3)");
        params.put("id", leaseId);
        return getDynamicBySQL(input, sql.toString(), LeaseObjectInfoVO.class, "", false, params);
    }
    /*
     * 检查是否有重复的租赁物编号 状态： -2 删除 95 标的物登记审批通过 0 标的物登记暂存 11 标的物登记待审批 -1 登记审批打回 94 标的物修改审批通过 1 标的物修改暂存 12 标的物修改审批中 -3 修改审批打回
     */
    public boolean checkLeaseItemCode(String itemCode, String id) {// 暂存打回删除的状态租赁物编号可以允许重复
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT COUNT(1) as count  ");
        sql.append(" FROM CMS_LEASE_SUB_MATER m left join CMS_FINANCING_LEASE_INFO i on m.lease_id=i.id");
        sql.append(
                " WHERE 1=1 and m.status not in(-2,0,-1,1,-3) and m.LEASE_ITEM_CODE=:itemCode and i.STATUS not in(-2,-5,-6) ");
        params.put("itemCode", itemCode);
        if (!EmptyUtils.isEmpty(id)) { // 排除自身
            sql.append("and m.ID<>:id");
            params.put("id", id);
        }
        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            Object count = map.get("COUNT");
            if (EmptyUtils.isEmpty(count)) {
                count = map.get("count");
            }
            if (count instanceof Number) {// 不同数据库驱动返回类型不一致：Oracle 为 BigDecimal，MySQL 为 BigInteger/Long
                return ((Number) count).longValue() == 0;
            }
        }
        return false;
    }
    /**
     * 功能介绍 : 租赁申请列表数据查询
     */
    public DataTablesOutput getDataleaseDatatables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.STATUS in (95,100)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.CORP_ID"));
        List<String> updateIds = this.getUpdateIds();// 获取可以修改的id
        sql.append(" and ").append(getSQLIn(updateIds, 900, "linfo.ID"));
        OrderString orderString = new OrderString("linfo.CREATE_DATE", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, null, false);
    }
    // 获取可以修改的id
    public List<String> getUpdateIds() {
        List<String> ids = new ArrayList<String>();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct m.lease_id as Id from CMS_LEASE_SUB_MATER m where 1=1 ");
        sql.append("and m.status in(95,94,1,12,-3)");
        List<LeaseUpdateObjectInfoVO> vos = listBySQLAliasToBean(sql.toString(), params, LeaseUpdateObjectInfoVO.class);
        for (LeaseUpdateObjectInfoVO vo : vos) {
            ids.add(vo.getId());
        }
        return ids;
    }
    /*
     * 检查是否有重复的租赁物编号
     */
    public LeaseObjectInfoVO checkRepeat(String itemCode) { // 导入时判断修改暂存状态是否有重复的租赁物编号
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT M.ID as id  ");
        sql.append(" FROM CMS_LEASE_SUB_MATER M");
        sql.append(" WHERE 1=1 and M.status='1' and M.LEASE_ITEM_CODE=:itemCode ");// 查询暂存状态下的合同编号是否重复
        params.put("itemCode", itemCode);
        return getBySQLAliasToBean(sql.toString(), params, LeaseObjectInfoVO.class);
    }
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ITEM_CODE as leaseItemCode,M.LEASE_ITEM_NAME as leaseItemName,I.LEASE_NO as leaseNo, ");
        //TODO 补充币种名称
        sql.append(
                "M.BRAND as  brand, M.SPE_MODEL as speModel,M.CAL_CORP as calCorp,M.NUMB as numb,M.CUR_ID as curId,M.CUR_NAME as curName,");
        sql.append(
                "M.NOW_MONEY as nowMoney,M.SAVE_ADDR as saveAddr,M.MAKE_BY as makeBy,M.BUY_DATE buyDate,M.RMK as rmk,M.STATUS as status,");
        sql.append("T.NAME as name,M.OLD_MONEY oldMoney");
        sql.append(
                " FROM CMS_LEASE_SUB_MATER M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID left join CMS_TENANCY_ITEMS_TYPES T on M.LEASE_TYPE_ID=T.id ");
        sql.append(" where 1 = 1");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and M.status = " + CmsConstant.LEASE_COMMITAPPROVE);
        }
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "M.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), LeaseObjectInfoVO.class, "", false);
    }
    /*
     * 查看标的物
     */
    @SuppressWarnings("rawtypes")
    public LeaseObjectInfoVO queryLeaseObjectInfo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ITEM_CODE as leaseItemCode,M.LEASE_ITEM_NAME as leaseItemName,M.LEASE_TYPE_ID leaseTypeId, ");
        //TODO 补充币种名称
        sql.append(
                "M.BRAND as  brand, M.SPE_MODEL as speModel,M.CAL_CORP as calCorp,M.NUMB as numb,M.CUR_ID as curId,M.CUR_NAME as curName,");
        sql.append(
                "M.NOW_MONEY as nowMoney,M.SAVE_ADDR as saveAddr,M.MAKE_BY as makeBy,M.BUY_DATE buyDate,M.RMK as rmk,M.STATUS as status,");
        sql.append("T.NAME as name,M.OLD_MONEY oldMoney");
        sql.append(
                " FROM CMS_LEASE_SUB_MATER M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID left join CMS_TENANCY_ITEMS_TYPES T on M.LEASE_TYPE_ID=T.id ");
        sql.append(" WHERE 1=1 and M.id=:id ");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, LeaseObjectInfoVO.class);
    }
}
