package com.bytter.supply.domain.receivable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.enums.ApprovalStatusEnum;
import com.bytter.supply.domain.receivable.entity.ScfReceivableTransferInfo;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoDao
 * @Description: 债权凭证转让信息Dao
 * @since 2026/7/8
 */
@Repository
public class ScfReceivableTransferInfoDao extends BaseDao<ScfReceivableTransferInfo, String> {

    /** 查询转让审批中的记录，并按工作流业务ID过滤。 */
    public DataTablesOutput<ScfReceivableTransferInfoVO> getPendingApproval(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, (select v.id from scf_receivable_voucher_info v where v.voucher_code = t.voucher_code) as business_key, ");
        sql.append("        t.transfer_no,t.transfer_batch_no, t.tenant_id, t.voucher_code, t.transfer_status, ");
        sql.append("        t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.issue_transfer_company, t.issue_transfer_company_id, t.cur_id, t.cur_name, ");
        sql.append("        t.receive_company, t.receive_company_id, t.receiver_credit_code, ");
        sql.append("        t.transfer_date, t.transfer_amt, t.purpose, t.remark, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_transfer_info t where t.transfer_status = 'approval-ing' ");
        sql.append(businessSql == null || businessSql.isEmpty() ? " and 1 = 0 " : " and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableTransferInfoVO.class, "t", false);
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfReceivableTransferInfoVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.transfer_no,t.transfer_batch_no, t.tenant_id, t.voucher_code, t.transfer_status, ");
        sql.append("        t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.issue_transfer_company, t.issue_transfer_company_id, t.cur_id, t.cur_name, ");
        sql.append("        t.receive_company, t.receive_company_id, t.receiver_credit_code, ");
        sql.append("        t.transfer_date, t.transfer_amt, t.purpose, t.remark, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        // 转让记录正式列表不展示暂存草稿，草稿仅供转让编辑/回显使用。
        sql.append(" from scf_receivable_transfer_info t where t.transfer_status <> '")
                .append(ApprovalStatusEnum.DRAFT.getCode()).append("' ");

        Object transferStatus = dataTablesInput.getFormdata().get("transfer_status");
        if (transferStatus != null && !String.valueOf(transferStatus).trim().isEmpty()) {
            sql.append(" and t.transfer_status = '").append(transferStatus).append("' ");
        }

        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfReceivableTransferInfoVO.class, "", false);
    }

    /**
     * 转让列表查询：可按转让状态、转让标识分别查询一对多/多对一，可按凭证编号或凭证ID集合过滤
     *
     * @param transferFlag 转让标识：1 一对多，2 多对一；为空不限制转让标识
     * @param voucherCode  债权凭证编号；为空不按凭证编号过滤
     * @param voucherIds   债权凭证ID集合（逗号分隔，付款方式52回显联动前端传的应收凭证ID集合）；为空不按凭证ID过滤
     * @param transferStatus 转让状态；为空查询所有状态
     * @return 转让信息列表
     */
    public List<ScfReceivableTransferInfoVO> listTemp(String transferFlag, String voucherCode, String voucherIds, String transferStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.transfer_no, t.transfer_batch_no, t.tenant_id, t.voucher_code, t.transfer_status, ");
        sql.append("        t.transfer_flag, ");
        sql.append("        t.platform_id, t.platform_name, t.product_id, t.product_name, ");
        sql.append("        t.issue_transfer_company, t.issue_transfer_company_id, t.cur_id, t.cur_name, ");
        sql.append("        t.receive_company, t.receive_company_id, t.receiver_credit_code, ");
        sql.append("        t.transfer_date, t.transfer_amt, t.purpose, t.remark, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_receivable_transfer_info t where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNoneBlank(transferStatus)) {
            sql.append(" and t.transfer_status = :transferStatus ");
            params.put("transferStatus", transferStatus);
        }
        if (StringUtils.isNoneBlank(transferFlag)) {
            sql.append(" and t.transfer_flag = :transferFlag ");
            params.put("transferFlag", transferFlag);
        }
        if (StringUtils.isNoneBlank(voucherCode)) {
            sql.append(" and t.voucher_code = :voucherCode ");
            params.put("voucherCode", voucherCode);
        } else if (StringUtils.isNoneBlank(voucherIds)) {
            // 按凭证ID集合联动：由凭证ID反查凭证编号，再匹配转让暂存（付款方式52回显场景）
            sql.append(" and t.voucher_code in (select c.voucher_code from scf_receivable_voucher_info c where c.id in (:voucherIds)) ");
            params.put("voucherIds", StringUtils.splitToList(",", voucherIds));
        }
        List list = this.listMapBySQL(sql.toString(), params);
        if (ListUtils.isNotNullOrEmpty(list)) {
            return BeanUtils.copyMapListProperties(list, ScfReceivableTransferInfoVO.class);
        }
        return new ArrayList<>();
    }
}
