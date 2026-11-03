package com.bytter.finance.domain.inl.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.GuaContractCmsInlVO;
import com.bytter.finance.dto.GuaContractCmsInlDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class InlInfoDao extends BaseDao<CmsInlInfo, String> {
    /**
     * 打回和暂存列表
     *
     * @param input
     * @param corpIds
     * @return
     */
    public DataTablesOutput dataTable(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.*,rt.repay_name as repayTypeName");
        sql.append("  from CMS_INL_INFO info ");
        sql.append(" left join cms_repay_type rt on info.repay_type=rt.id ");
        sql.append(" where 1 = 1");
        sql.append(" and info.status in (-1,0,95) ");
        sql.append(" and (" + getSQLIn(corpIds, 900, "info.borrow_in_corp") + ")");
        OrderString orderString = new OrderString("info.bill_No", "desc");
        input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), CmsInlInfoVO.class, "info", false);
    }
    /**
     * 打回和暂存列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput listInlDataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.*,rt.repay_name as repayTypeName ");
        sql.append("  from CMS_INL_INFO info ");
        sql.append("  left join cms_repay_type rt on info.repay_type=rt.id ");
        sql.append(" where 1 = 1");
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and info.status = " + InlConstant.COMMIT_APPROVE);
        }
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and (" + getSQLIn(ids, 900, "info.id") + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsInlInfoVO.class, "", false);
    }
    public DataTablesOutput<GuaContractCmsInlVO> getGuaForInl(DataTablesInput input, List<String> corpIds, List<String> businessIdList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.*");
        sql.append(" from CMS_INL_INFO info ");
        sql.append(" where info.is_hang_guarantee = '1'");
        sql.append(" and info.status not in  (-2,-3) ");
        sql.append(" and (" + getSQLIn(businessIdList, 900, "info.id") + ")");
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and (" + getSQLIn(corpIds, 900, "info.borrow_in_corp") + ")");
        } else {
            sql.append(" and 1 <> 1 ");
        }
        return getDynamicBySQL(input, sql.toString(), GuaContractCmsInlVO.class, "info", false, null);
    }
    public boolean existsBillNo(String billNo) {
        if (StringUtils.isEmpty(billNo)) {
            return false;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billNo", billNo);
        Object count = createSQLQuery("select count(1) from CMS_INL_INFO where bill_no = :billNo", params).uniqueResult();
        return count != null && ((Number) count).intValue() > 0;
    }

    public List<GuaContractCmsInlDTO> getGuaInlLoan() {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.bill_No as billNo, info.gua_contract as guaContract, info.cur_id as curId, info.cur_name as curName, info.money, ");
        sql.append(" info.start_date as startDate, info.end_date as endDate, info.id as id ");
        sql.append(" from CMS_INL_INFO info ");
        sql.append(" where info.is_hang_guarantee = '1'");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsInlDTO.class);
    }
}
