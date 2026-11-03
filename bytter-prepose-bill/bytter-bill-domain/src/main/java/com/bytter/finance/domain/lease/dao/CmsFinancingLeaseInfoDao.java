package com.bytter.finance.domain.lease.dao;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.lease.vo.GuaContractCmsLeaseVO;
import com.bytter.finance.dto.GuaContractCmsLeaseDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_LEASE_UPDATE
 * @date 2022-04-02
 */
@Repository
public class CmsFinancingLeaseInfoDao extends BaseDao<CmsFinancingLeaseInfo, String> {
    /**
     * 功能介绍 : 租赁申请列表数据查询
     */
    public DataTablesOutput getDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status in (-1,0,11)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, null, false);
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
        OrderString orderString = new OrderString("linfo.CREATE_DATE", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, null, false);
    }
    /**
     * 融资申请查询列表 - 数据
     */
    public DataTablesOutput getQueryDatatable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status not in (-2,-5,-6)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, null, false);
    }
    /**
     * 功能介绍 : 租赁补录列表数据查询
     */
    public DataTablesOutput getRepairDataLeaseTable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status in (95,20,21,-3)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, null, false);
    }
    public List<CmsFinancingLeaseInfoVO> getLeaseByLeaseCode(String leaseCode, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap();
        sql.append(" select linfo.*");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status not in (-2)");
        sql.append(" and linfo.lease_code =:leaseCode");
        params.put("leaseCode", leaseCode);
        if (StringUtils.isNotEmpty(id)) {
            sql.append(" and linfo.id <> :id");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, CmsFinancingLeaseInfoVO.class);
    }
    public CmsFinancingLeaseInfoVO getLeaseById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select linfo.*");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.id =:id");
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, CmsFinancingLeaseInfoVO.class);
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "linfo.id"));
        }
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, "linfo", false,
                DynamicallyObject.getInstance(params), null);
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     */
    public DataTablesOutput getDataRepairApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.* ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "linfo.id"));
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), CmsFinancingLeaseInfoVO.class, "linfo", false,
                DynamicallyObject.getInstance(params), null);
    }
    public List<GuaContractCmsLeaseDTO> getGuaForLeaseInfo() {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.LEASE_NO as leaseNo, info.LEASE_CODE as leaseCode, ");
        sql.append(" info.ORI_CURID as oriCurid, info.ORI_CUR_NAME as oriCurName, ");
        sql.append(" info.ORI_MONEY as oriMoney, info.BEGIN_DATE as beginDate, info.END_DATE as endDate, ");
        sql.append(" info.id as id ");
        sql.append(" from CMS_FINANCING_LEASE_INFO info ");
        sql.append("where info.is_hang_guarantee = '1' ");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsLeaseDTO.class);
    }

    public DataTablesOutput<GuaContractCmsLeaseVO> getGuaForLease(DataTablesInput input, List<String> corpIds, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.LEASE_NO as leaseNo, info.LEASE_CODE as leaseCode, ");
        sql.append(" info.ORI_CURID as oriCurid, info.ORI_CUR_NAME as oriCurName, ");
        sql.append(" info.ORI_MONEY as oriMoney, info.BEGIN_DATE as beginDate, info.END_DATE as endDate, ");
        sql.append(" info.id as id ");
        sql.append(" from CMS_FINANCING_LEASE_INFO info ");
        sql.append("where info.is_hang_guarantee = '1' ");
        if (EmptyUtils.isNotEmpty(ids)) {
            sql.append(" and " + getSQLIn(ids, 900, "info.id"));
        }
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "info.corp_id"));
        }
        DataTablesInput input2 = new DataTablesInput();
        BeanUtils.copyProperties(input,input2,"formdata");
        return getDynamicBySQL(input2, sql.toString(), GuaContractCmsLeaseVO.class, "n", false);
    }
}
