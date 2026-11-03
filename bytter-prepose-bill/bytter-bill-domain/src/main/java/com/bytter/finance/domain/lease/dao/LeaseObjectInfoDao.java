package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeaseObjectInfo;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseItemTypeVO;
import com.bytter.finance.domain.lease.vo.LeaseObjectInfoVO;
import com.bytter.finance.constant.InlConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class LeaseObjectInfoDao extends BaseDao<LeaseObjectInfo, String> {
    /*
     * 标的物登记列表
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
        sql.append(" and (M.status=0 or M.status=11 or M.status=-1)");// 只取登记暂存，审批打回或者登记待审批状态的数据
        params.put("id", leaseId);
        return getDynamicBySQL(input, sql.toString(), LeaseObjectInfoVO.class, "", false, params);
    }
    /*
     * 标的物登记查看列表
     */
    public List<LeaseObjectInfoVO> queryLeaseObjectInfoList(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT M.ID as id ,M.LEASE_ITEM_CODE as leaseItemCode,M.LEASE_ITEM_NAME as leaseItemName, ");
        //TODO 补充币种名称
        sql.append(
                "M.BRAND as  brand, M.SPE_MODEL as speModel,M.CAL_CORP as calCorp,M.NUMB as numb,M.CUR_ID as curId,M.CUR_NAME as curName,");
        sql.append(
                "M.NOW_MONEY as nowMoney,M.SAVE_ADDR as saveAddr,M.MAKE_BY as makeBy,M.BUY_DATE buyDate,M.RMK as rmk,M.STATUS as status,");
        sql.append("T.NAME as leaseTypeName,M.OLD_MONEY oldMoney");
        sql.append(
                " FROM CMS_LEASE_SUB_MATER M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID left join CMS_TENANCY_ITEMS_TYPES T on M.LEASE_TYPE_ID=T.id ");
        sql.append(" WHERE 1=1 and M.LEASE_ID=:id and M.status<>-2");
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseObjectInfoVO.class);
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
    /*
     * 检查是否有重复的租赁物编号
     */
    public LeaseObjectInfoVO checkRepeat(String itemCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT M.ID as id  ");
        sql.append(" FROM CMS_LEASE_SUB_MATER M");
        sql.append(" WHERE 1=1 and M.STATUS='0' and M.LEASE_ITEM_CODE=:itemCode ");// 查询暂存状态下的合同编号是否重复
        params.put("itemCode", itemCode);
        return getBySQLAliasToBean(sql.toString(), params, LeaseObjectInfoVO.class);
    }
    /*
     * 校验是否存在对应的融资租赁合同编号
     */
    public CmsFinancingLeaseInfoVO checkItem(String leaseNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT M.ID as id  ");
        sql.append(" FROM CMS_FINANCING_LEASE_INFO M");
        sql.append(" WHERE 1=1 and M.STATUS not in (-2,-5,-6) and M.LEASE_NO=:leaseNo ");// 查询融资租赁合同编号是否存在
        params.put("leaseNo", leaseNo);
        return getBySQLAliasToBean(sql.toString(), params, CmsFinancingLeaseInfoVO.class);
    }
    /*
     * 检查是否有重复的租赁物编号
     */
    public boolean checkLeaseItemCode(String itemCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT COUNT(1) as count  ");
        sql.append(" FROM CMS_LEASE_SUB_MATER m  left join CMS_FINANCING_LEASE_INFO i on m.lease_id=i.id");
        sql.append(
                " WHERE 1=1 and m.STATUS not in(-2,0,-1) and m.LEASE_ITEM_CODE=:itemCode and i.STATUS not in(-2,-5,-6) ");// 查询除暂存,打回，和删除外所有状态下的合同编号是否重复
        params.put("itemCode", itemCode);
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
    /*
     * 获取租赁物类型名称
     */
    @SuppressWarnings("rawtypes")
    public List<LeaseItemTypeVO> getModuleName() {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT  ID as id,CODE as code,NAME as name,RMK as rmk from CMS_TENANCY_ITEMS_TYPES c ");
        sql.append(" WHERE 1=1 and c.STATUS <> -2 or c.STATUS is null");
        return listBySQLAliasToBean(sql.toString(), params, LeaseItemTypeVO.class);
    }
    /**
     * 打回和暂存列表
     *
     * @param input
     * @return
     */
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
        sql.append(" and M.STATUS = " + InlConstant.COMMIT_APPROVE);
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "M.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), LeaseObjectInfoVO.class, "", false, null);
    }
}
