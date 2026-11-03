package com.bytter.finance.domain.lease.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.lease.entity.LeaseExtend;
import com.bytter.finance.domain.lease.vo.CmsLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseExtendVO;
import com.bytter.finance.constant.InlConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class LeaseExtendDao extends BaseDao<LeaseExtend, String> {
    /**
     * 功能介绍 : 租赁申请列表数据查询
     */
    public DataTablesOutput getDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.ID id,linfo.LEASE_NO leaseNo,linfo.LEASE_TYPE leaseType,linfo.LEASE_CODE leaseCode, ");
        sql.append("linfo.ORI_CURID as oriCurid,linfo.ORI_MONEY as oriMoney,linfo.BEGIN_DATE as beginDate,linfo.END_DATE as endDate,");
        sql.append("linfo.RMB_MONEY as rmbMoney,linfo.PAY_CODE payCode ,linfo.CORP_ID corpId,linfo.LESSOR lessor,linfo.SUPPLIER supplier,");
        sql.append("linfo.STATUS status,linfo.EXPENDED_COUNT expendedCount,linfo.corp_name as corpName,linfo.ori_cur_name as oriCurName ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.STATUS in (95,100)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseInfoVO.class, null, false);
    }
    /*
     * 获取对应的展期
     */
    public List<LeaseExtendVO> getExtend(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ID as leaseId,M.STATUS status,M.RMK rmk,M.CREATE_DATE createDate,M.LAST_EXPENDED_DATE lastExpendedDate,");
        sql.append(
                " M.EXPENDED_DATE expendedDate,M.EXPENDED_CODE expendedCode,M.EXPENDED_LIMIT expendedLimit,M.TERN_UNIT ternUnit,");
        sql.append("M.POINTS_PROPORTION_SELECT pointsProportionSelect ,M.POINTS_PROPORTION_TEXT pointsProportionText,");
        sql.append("M.RATE rate");
        sql.append(" FROM CMS_LEASE_EXTENDED M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID  ");
        sql.append(" WHERE 1=1 and M.LEASE_ID=:leaseId and M.STATUS!=-2");
        sql.append(" order by M.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseExtendVO.class);
    }
    /*
     * 查看展期日志
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataListMsgData(DataTablesInput dataTablesInput, String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ID as leaseId,M.LAST_EXPENDED_DATE lastExpendedDate,M.EXPENDED_DATE expendedDate, ");
        sql.append(
                "M.EXPENDED_CODE expendedCode,M.EXPENDED_LIMIT expendedLimit,M.TERN_UNIT ternUnit,M.POINTS_PROPORTION_SELECT pointsProportionSelect,");
        sql.append(
                "M.POINTS_PROPORTION_TEXT pointsProportionText,M.RATE rate,M.STATUS status,M.RMK rmk,I.END_DATE endDate");
        sql.append(" FROM CMS_LEASE_EXTENDED M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID  ");
        sql.append(" WHERE 1=1 and M.LEASE_ID=:leaseId and M.STATUS!=-2");
        params.put("leaseId", leaseId);
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseExtendVO.class, "", false, params);
    }
    /*
     * 审批数据
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ID as leaseId,M.LAST_EXPENDED_DATE lastExpendedDate,M.EXPENDED_DATE expendedDate, ");
        sql.append(
                "M.EXPENDED_CODE expendedCode,M.EXPENDED_LIMIT expendedLimit,M.TERN_UNIT ternUnit,M.POINTS_PROPORTION_SELECT pointsProportionSelect,");
        sql.append("M.POINTS_PROPORTION_TEXT pointsProportionText,M.RATE rate,M.STATUS status,M.RMK rmk, ");
        sql.append(
                "I.END_DATE endDate,I.LEASE_NO leaseNo,I.lease_type leaseType,I.lease_code leaseCode,I.pay_code payCode, ");
        //TODO 补充单位名称
        sql.append("I.corp_name as corpName, ");
        sql.append("I.corp_id corpId,I.lessor lessor,I.supplier supplier,I.EXPENDED_COUNT expendedCount ");
        sql.append(" FROM CMS_LEASE_EXTENDED M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID  ");
        sql.append(" WHERE 1=1  ");
        sql.append(" and M.status = " + InlConstant.COMMIT_APPROVE);
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "M.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), LeaseExtendVO.class, "", false, null);
    }
    /*
     * 审批查看
     */
    @SuppressWarnings("rawtypes")
    public LeaseExtendVO queryLeaseObjectInfo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(
                "SELECT M.ID as id ,M.LEASE_ID as leaseId,M.LAST_EXPENDED_DATE lastExpendedDate,M.EXPENDED_DATE expendedDate, ");
        sql.append(
                "M.EXPENDED_CODE expendedCode,M.EXPENDED_LIMIT expendedLimit,M.TERN_UNIT ternUnit,M.POINTS_PROPORTION_SELECT pointsProportionSelect,");
        sql.append(
                "M.POINTS_PROPORTION_TEXT pointsProportionText,M.RATE rate,M.STATUS status,M.RMK rmk,I.END_DATE endDate");
        sql.append(" FROM CMS_LEASE_EXTENDED M LEFT JOIN CMS_FINANCING_LEASE_INFO I ON M.LEASE_ID=I.ID  ");
        sql.append(" WHERE 1=1 and M.id=:id ");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, LeaseExtendVO.class);
    }
}
