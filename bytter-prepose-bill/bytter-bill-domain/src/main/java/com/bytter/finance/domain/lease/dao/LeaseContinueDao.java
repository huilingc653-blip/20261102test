package com.bytter.finance.domain.lease.dao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.lease.entity.LeaseContinueInfo;
import com.bytter.finance.domain.lease.vo.CmsAfterLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseContinueInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseContinueVO;
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
public class LeaseContinueDao extends BaseDao<LeaseContinueInfo, String> {
    /**
     * 功能介绍 : 租赁续租列表数据查询
     */
    public DataTablesOutput getDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.id,linfo.LEASE_NO leaseNo,linfo.lease_type leaseType,linfo.lease_code leaseCode, ");
        //TODO 补充币种ID
        sql.append("linfo.pay_code payCode ,linfo.corp_id corpId,linfo.ori_curid as oriCurid,linfo.lessor lessor,linfo.supplier supplier,");
        sql.append("linfo.status status,linfo.continue_count continueCount,linfo.ori_cur_name as oriCurName,linfo.corp_name as corpName, ");
        sql.append("linfo.ORI_MONEY oriMoney,linfo.RMB_MONEY rmbMoney,linfo.BEGIN_DATE beginDate,linfo.END_DATE endDate ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where linfo.status in (95,100)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsLeaseContinueInfoVO.class, null, false);
    }
    public List<LeaseContinueVO> getContinueStatus(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select c.continue_date,c.continue_code,c.status,c.rmk,c.create_date from ");
        sql.append(" cms_lease_continue c where 1=1 and c.lease_id = :leaseId order by c.create_date desc ");
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
        sql.append("select c.id,linfo.lease_type,linfo.corp_id,linfo.lessor,linfo.id as leaseId, ");
        //TODO 补充单位名称
        sql.append("linfo.corp_name as corpName, ");
        sql.append("linfo.supplier,linfo.lease_code,linfo.pay_code,c.continue_date,c.status as continueStatus, ");
        sql.append(" c.continue_code,c.rmk as continue_rmk from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append("  left join cms_lease_continue c on linfo.id = c.lease_id ");
        sql.append(" where 1 = 1");
        sql.append(" and c.status = " + InlConstant.COMMIT_APPROVE);
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "c.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsAfterLeaseInfoVO.class, "linfo", false, null);
    }
    public CmsAfterLeaseInfoVO queryLeaseContinueInfo(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select c.id,linfo.lease_type,linfo.corp_id,linfo.lessor,linfo.id as leaseId, ");
        //TODO 补充单位名称
        sql.append("linfo.corp_name as corpName, ");
        sql.append("linfo.supplier,linfo.lease_code,linfo.pay_code,c.continue_date,c.status as continue_status,");
        sql.append("c.continue_code,c.rmk as continue_rmk from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append("left join cms_lease_continue c on linfo.id = c.lease_id");
        sql.append(" WHERE 1=1 and c.id=:id ");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
    public DataTablesOutput getDataListMsgData(DataTablesInput dataTablesInput, String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(
                "select c.lease_id,c.continue_code,c.continue_date,c.rmk,c.create_by,c.create_date,c.update_by,c.update_date from cms_lease_continue c");
        sql.append(" WHERE 1=1 and c.LEASE_ID=:leaseId ");
        sql.append(" order by c.CREATE_DATE DESC");
        params.put("leaseId", leaseId);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsAfterLeaseInfoVO.class, "", false, params);
    }
    /**
     * 续租页面主信息
     *
     * @param id
     * @return
     */
    public CmsAfterLeaseInfoVO getLeaseById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select linfo.*");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.id =:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
    public List<CmsAfterLeaseInfoVO> getContinue(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT r.id,r.CONTINUE_DATE,r.CONTINUE_CODE,r.RMK rmk");
        sql.append(" FROM CMS_FINANCING_LEASE_INFO i LEFT JOIN CMS_LEASE_CONTINUE r ON r.LEASE_ID=i.ID  ");
        sql.append(" WHERE 1=1 and r.LEASE_ID=:leaseId and r.STATUS!=-2");
        sql.append(" order by r.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
    public List<CmsAfterLeaseInfoVO> getContinueDate(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("SELECT r.id,r.CONTINUE_DATE as continueDate,r.CONTINUE_CODE as continueCode,r.RMK rmk");
        sql.append(" FROM CMS_LEASE_CONTINUE r  ");
        sql.append(" WHERE 1=1 and r.LEASE_ID=:leaseId and r.STATUS!=-2");
        sql.append(" order by r.CREATE_DATE DESC");
        params.put("leaseId", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsAfterLeaseInfoVO.class);
    }
}
