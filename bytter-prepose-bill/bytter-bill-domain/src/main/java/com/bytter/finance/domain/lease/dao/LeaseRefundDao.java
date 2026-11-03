package com.bytter.finance.domain.lease.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.lease.entity.LeaseRefundInfo;
import com.bytter.finance.domain.lease.vo.*;
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
public class LeaseRefundDao extends BaseDao<LeaseRefundInfo, String> {
    /**
     * 功能介绍 : 租赁退租列表数据查询
     */
    public DataTablesOutput getDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.id,linfo.LEASE_NO leaseNo,linfo.lease_type leaseType,linfo.lease_code leaseCode, ");
        sql.append("linfo.pay_code payCode ,linfo.corp_id corpId,linfo.lessor lessor,linfo.supplier supplier,");
        sql.append("linfo.status status,linfo.continue_count continueCount,linfo.ori_cur_name as oriCurName,linfo.corp_name as corpName, ");
        sql.append(" linfo.ORI_MONEY oriMoney,linfo.RMB_MONEY rmbMoney,linfo.BEGIN_DATE beginDate,linfo.END_DATE endDate ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status in (95,100)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseContinueInfoVO.class, null, false);
    }
    /**
     * 查询退租状态
     *
     * @param id
     * @return
     */
    public List<LeaseContinueVO> getRefundStatus(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.status,c.rmk,c.create_date from ");
        sql.append(
                " cms_lease_refund c where 1=1 and c.lease_id = :leaseId and c.status<>'-2' order by c.create_date desc ");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseContinueVO.class);
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
                "select r.id,linfo.lease_type,linfo.lessor, linfo.corp_id,linfo.supplier,linfo.lease_code ,linfo.id as leaseId");
        sql.append(",linfo.pay_code,r.status as refund_status,r.refund_date,r.is_advance_back,r.rmk as refundRmk,linfo.corp_id corpId,linfo.corp_name corpName");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo left join CMS_LEASE_REFUND r on linfo.id = r.lease_id");
        sql.append(" where 1 = 1");
        sql.append(" and r.status = " + InlConstant.COMMIT_APPROVE);
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "r.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsAfterLeaseInfoVO.class, "linfo", false, null);
    }
    /**
     * 审批数据
     *
     * @param id
     * @return
     */
    public CmsAfterLeaseInfoVO queryLeaseRefundInfo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select c.id,linfo.lease_type,linfo.corp_id,linfo.lessor,linfo.id as leaseId, ");
        //TODO 补充单位名称
        sql.append("linfo.corp_name as corpName,");
        sql.append("linfo.supplier,linfo.lease_code,linfo.pay_code,c.refund_date,c.status as refund_status,");
        sql.append("c.is_advance_back,c.rmk as refund_rmk from CMS_FINANCING_LEASE_INFO linfo  ");
        sql.append("left join cms_lease_refund c on linfo.id = c.lease_id");
        sql.append(" WHERE 1=1 and c.id=:id ");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
    /**
     * 退租页面主信息
     *
     * @param id
     * @return
     */
    public LeaseRefundVO getLeaseById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select linfo.*,r.refund_date refundDate,r.rmk as refundRmk,r.IS_ADVANCE_BACK as isAdvanceBack ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo");
        sql.append(" left join cms_lease_refund r on linfo.id = r.lease_id ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.id =:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, LeaseRefundVO.class);
    }
    public List<LeaseRefundUpdateVO> getRefund(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT r.id,r.REFUND_DATE,r.IS_ADVANCE_BACK,r.RMK");
        sql.append(" FROM CMS_FINANCING_LEASE_INFO i LEFT JOIN CMS_LEASE_REFUND r ON r.LEASE_ID=i.ID  ");
        sql.append(" WHERE 1=1 and r.LEASE_ID=:leaseId and r.STATUS!=-2");
        sql.append(" order by r.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseRefundUpdateVO.class);
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
        sql.append(" WHERE 1=1 and M.LEASE_ID=:leaseId and M.STATUS!=-2 and M.STATUS = 95");
        sql.append(" order by M.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, LeaseExtendVO.class);
    }
}
