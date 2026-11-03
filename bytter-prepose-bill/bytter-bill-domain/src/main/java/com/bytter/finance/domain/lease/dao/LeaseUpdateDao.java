package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeaseUpdate;
import com.bytter.finance.domain.lease.vo.LeaseListUpdateVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Dao
 * @Description: CMS_LEASE_UPDATE
 * @date 2022-04-20
 */
@Repository
public class LeaseUpdateDao extends BaseDao<LeaseUpdate, String> {
    /*
     * 获取对应的变更
     */
    public List<LeaseUpdateVO> getExtend(String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select u.* ");
        sql.append(" from CMS_LEASE_UPDATE u ");
        sql.append(" where 1=1 ");
        sql.append(" and u.lease_id=:leaseId");
        sql.append(" and u.status not in (-2)");
        sql.append(" order by u.create_date desc ");
        params.put("leaseId", leaseId);
        return listBySQLAliasToBean(sql.toString(), params, LeaseUpdateVO.class);
    }
    /**
     * 功能介绍 : 变更主页租赁申请列表数据查询
     */
    public DataTablesOutput getUpdateDatatables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select linfo.id,linfo.lease_type as leaseType,linfo.lease_no as leaseNo,linfo.corp_id as corpId,linfo.corp_name as corpName,");
        sql.append(" linfo.ORI_CURID as oriCurid,linfo.ORI_MONEY as oriMoney,linfo.BEGIN_DATE as beginDate,linfo.END_DATE as endDate,linfo.ori_cur_name as oriCurName,");
        sql.append(" linfo.RMB_MONEY as rmbMoney,linfo.lessor,linfo.supplier,linfo.lease_code as leaseCode,linfo.pay_code as payCode,linfo.status as modifystatus ");
        sql.append(" from CMS_FINANCING_LEASE_INFO linfo");
        sql.append(" where 1=1 ");
        sql.append(" and linfo.status in (95,100)");
        // sql.append(" and linfo.control_status='N'");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "linfo.corp_id"));
        OrderString orderString = new OrderString("linfo.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseListUpdateVO.class, null, false);
    }
    /**
     * 功能介绍 : 变更审批列表表查询<br>
     */
    public DataTablesOutput getDataUpdateApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select u.* ");
        sql.append(" from CMS_LEASE_UPDATE u ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "u.id"));
        }
        return getDynamicParamsBySQL(dataTablesInput, sql.toString(), LeaseUpdateVO.class, "u", false,
                DynamicallyObject.getInstance(params), null);
    }
    /**
     * 查询已支付的金额
     */
    public BigDecimal getRePayMoney(String leaseId) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select sum(del.lease_monry) as AMT  from CMS_FINANCING_LEASE_INFO info");
        sql.append("  left join CMS_LEASE_CAPITAL_PLAN p  on info.id = p.lease_id");
        sql.append("  left join CMS_LEASE_CAPITAL_PLANDEL del on del.lease_plan_id = p.id");
        sql.append("  where del.is_repay = '1' and info.id=:leaseId group by info.id");
        params.put("leaseId", leaseId);
        Map<String, BigDecimal> amp = this.getBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(amp)) {
            return BigDecimal.ZERO;
        }
        return EmptyUtils.isEmpty(amp.get("AMT")) ? BigDecimal.ZERO : new BigDecimal(String.valueOf(amp.get("AMT")));
    }
}
