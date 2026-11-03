package com.bytter.finance.domain.lease.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.lease.entity.LeaseBuyBackInfo;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseContinueInfoVO;
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
public class LeaseBuyBackDao extends BaseDao<LeaseBuyBackInfo, String> {
    /**
     * 功能介绍 : 租赁回购列表数据查询
     */
    public DataTablesOutput getDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.id,linfo.LEASE_NO leaseNo,linfo.lease_type leaseType,linfo.lease_code leaseCode, ");
        sql.append("linfo.pay_code payCode ,linfo.corp_id corpId,linfo.lessor lessor,linfo.supplier supplier,");
        sql.append("linfo.status status,linfo.continue_count continueCount,r.status repoStatus,linfo.corp_name as corpName,linfo.ori_cur_name oriCurName, ");
        sql.append(" linfo.ori_cur_name curName,linfo.ORI_MONEY oriMoney,linfo.RMB_MONEY rmbMoney,linfo.BEGIN_DATE beginDate,linfo.END_DATE endDate ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo left join cms_lease_repo r on linfo.id = r.lease_id ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status in (95,100)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseContinueInfoVO.class, null, false);
    }
    /**
     * 打回和暂存列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c.id,linfo.lease_type,linfo.corp_id,linfo.lessor,linfo.id as leaseId, ");
        //TODO 补充单位名称
        sql.append("linfo.corp_name as corpName, ");
        sql.append("linfo.supplier,linfo.lease_code,linfo.pay_code,c.repo_date,c.status as repoStatus, ");
        sql.append(" c.IS_ADVANCE_BACK,c.rmk as repoRmk,c.repo_money from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append("  left join cms_lease_repo c on linfo.id = c.lease_id ");
        sql.append(" where 1 = 1");
        sql.append(" and c.status = " + InlConstant.COMMIT_APPROVE);
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "c.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsAfterLeaseInfoVO.class, "linfo", false, null);
    }
    public CmsAfterLeaseInfoVO queryLeaseObjectInfo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select c.id,linfo.lease_type,linfo.corp_id,linfo.lessor,linfo.id as leaseId, ");
        //TODO 补充单位名称
        sql.append("linfo.corp_name as corpName, ");
        sql.append("linfo.supplier,linfo.lease_code,linfo.pay_code,c.repo_date as repoDate,c.status as repoStatus,");
        sql.append("c.IS_ADVANCE_BACK,c.repo_money,c.rmk as repo_rmk from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append("left join cms_lease_repo c on linfo.id = c.lease_id");
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
    public CmsAfterLeaseInfoVO getLeaseById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select linfo.*,r.REPO_DATE repoDate,r.rmk as refundRmk,r.IS_ADVANCE_BACK as isAdvanceBack ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" left join cms_lease_repo r on linfo.id = r.lease_id ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.id =:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
    public List<CmsAfterLeaseInfoVO> getRepo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select r.id Id,r.REPO_DATE repoDate,r.IS_ADVANCE_BACK isAdvanceBack,r.REPO_MONEY repoMoney,r.RMK repoRmk");
        sql.append(" FROM CMS_FINANCING_LEASE_INFO i LEFT JOIN CMS_LEASE_REPO r ON r.LEASE_ID=i.ID  ");
        sql.append(" WHERE 1=1 and r.LEASE_ID=:leaseId and r.STATUS!=-2");
        sql.append(" order by r.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
}
