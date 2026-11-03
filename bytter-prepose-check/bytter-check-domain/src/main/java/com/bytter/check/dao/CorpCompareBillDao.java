package com.bytter.check.dao;
import com.bytter.check.entity.CorpCompareBill;
import com.bytter.check.vo.CorpCompareBillInputVO;
import com.bytter.core.base.BaseDao;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 单位对账单dao
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */
@Repository
public class CorpCompareBillDao extends BaseDao<CorpCompareBill, String> {
    @Autowired
    private IWorkFlowService workFlowService;
    public DataTablesOutput getCorpCompareBillInputVO(DataTablesInput dataTablesInput, String joinSql,List<String> businessIds) {
        Map<String, Object> formData = dataTablesInput.getFormdata();
        String type = (String) formData.get("type");
        String year = (String) formData.get("year");
        String month = (String) formData.get("month");
        Map<String, Object> map = new HashMap<>();
        String select = "select distinct fccb.id AS billId,fa.CORP_ID AS corpId,fa.BLNCTR_ACC AS account,fa.acc_name AS accountName," +
                "fa.CUR_ID AS currencyId,fa.ACC_TYPE AS accountType,fab.BALANCE AS balance,fccb.ERP_BALANCE as erpBalance,BT_NVL(fccb.STATUS,-2) AS status," +
                "BT_NVL(fccb.IS_CHECK,1) AS same,fccb.OP_CODE as opCode,fccb.CREATE_DATE as opDate,fccb.BILL_MONTH as billMonth ";
        StringBuilder from = new StringBuilder(" from FC_ACC fa " +
                "left join FC_ACC_BALANCE fab on fa.BLNCTR_ACC=fab.ACCOUNT ");
        if ("approve".equals(type)) {
            select = "select distinct fccb.id AS billId,fa.CORP_ID AS corpId,fa.BLNCTR_ACC AS account,fa.acc_name AS accountName," +
                    "fa.CUR_ID AS currencyId,fa.ACC_TYPE AS accountType,fccb.acc_BALANCE AS balance,fccb.ERP_BALANCE as erpBalance,BT_NVL(fccb.STATUS,-2) AS status," +
                    "BT_NVL(fccb.IS_CHECK,1) AS same,fccb.OP_CODE as opCode,fccb.CREATE_DATE as opDate,fccb.BILL_MONTH as billMonth ";
            from = new StringBuilder(" from FC_ACC fa ");
        }
        //不为审批列表
        if (!"approve".equals(type)) {
            from.append(" AND fab.balance_date = BT_TO_DATE( :balanceDate, 'yyyy-MM-dd') ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(year + "-" + month + "-01"));
            } catch (ParseException ignored) {
            }
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            map.put("balanceDate", sdf.format(calendar.getTime()));
        }
        from.append("left join FC_CORP_COMPARE_BILL fccb on fa.BLNCTR_ACC=fccb.ACCOUNT ");
        //不为审批列表
        if (!"approve".equals(type)) {
            from.append(" and (fccb.BILL_MONTH is null or fccb.BILL_MONTH=:billMonth) ");
            map.put("billMonth", year + "-" + month);
        }
        if ("approve".equals(type)) {
            from.append("WHERE 1 = 1 ");
        } else {
            from.append("WHERE fab.balance_date is not null ");
        }
        String status = (String) formData.get("status");
        String accountType = (String) formData.get("accountType");
        if (!"approve".equals(type)) {
            from.append(joinSql);
        }
        //状态 有值且不为未对账
        if (StringUtils.hasText(status) && !"approve".equals(type)) {
            if ("-2".equals(status)) {
                from.append(" AND (fccb.status='-2' or fccb.status is null) ");
            } else {
                from.append(" AND fccb.status=:status ");
                map.put("status", status);
            }
        }
        //内部账号
        String account = (String) formData.get("account");
        if (StringUtils.hasText(account)) {
            from.append(" AND fa.BLNCTR_ACC like :account ");
            map.put("account", "%" + account + "%");
        }
        //账号类型
        if (StringUtils.hasText(accountType)) {
            from.append(" AND fa.ACC_TYPE=:accountType ");
            map.put("accountType", accountType);
        } else {
            from.append(" AND fa.ACC_TYPE in ('01','02','05') ");
        }
        //是否相符
        String same = (String) formData.get("same");
        if (StringUtils.hasText(same)) {
            from.append(" AND fccb.IS_CHECK=:same ");
            map.put("same", same);
        }
        //单位
        String corpId = (String) formData.get("corpId");
        if (StringUtils.hasText(corpId)) {
            String[] corps = corpId.split(",");
            from.append(" AND " + SqlUtil.getInSql(corps, "fa.CORP_ID", true));
        }
        WorkFlowBusiness workFlowBusiness = null;
        String queryCount = "";
        if ("approve".equals(type)) {
            if (!CollectionUtils.isEmpty(businessIds)) {
                from.append("AND fccb.ID in :businessId");
                map.put("businessId", businessIds);
            } else {
                from.append("AND fccb.STATUS = '11'");
            }
            queryCount = "fccb.id";
        } else {
            queryCount = "fab.id";
        }
        //查询是否存在数据
        String countSql = "select count(*) from ( select distinct " + queryCount + " " + from.toString() + " ) so ";
        BigDecimal total = (BigDecimal) createSQLQuery(countSql, map).uniqueResult();
        if (total.intValue() > 0) {
            int first = 0;
            Integer pageNumber = dataTablesInput.getPageNumber();
            if (pageNumber != 0) {
                first = (pageNumber - 1) * dataTablesInput.getPageSize();
            }
            if(!StringUtils.isEmpty(map.get("balanceDate"))) {
            	from.append(" AND fa.create_date <= BT_TO_DATE('"+map.get("balanceDate").toString()+"','yyyy-MM-dd')  ");
            }
            List<CorpCompareBillInputVO> list = listBySQLAliasToBean(select + from.toString() + " order by fa.BLNCTR_ACC,fccb.CREATE_DATE ", first, dataTablesInput.getPageSize(), map, CorpCompareBillInputVO.class);
            return new DataTablesOutput<>(dataTablesInput.getDraw(), list, total.intValue(), total.intValue());
        }
        return new DataTablesOutput<>(dataTablesInput.getDraw(), null, total.intValue(), total.intValue());
    }
    private List<String> getBillMonth(List<String> ids) {
        String sql = "select BILL_MONTH FROM FC_CORP_COMPARE_BILL where id in :ids group by BILL_MONTH";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        return createSQLQuery(sql, map).list();
    }
    public String getBalanceByAccountAndDate(String account, String date) {
        String sql = "select balance from fc_acc_balance where ACCOUNT = :account ";
        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        if (StringUtils.hasText(date)) {
            sql += " AND balance_date = BT_TO_DATE(:date,'yyyy-MM-dd')";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(sdf.parse(date + "-01"));
            } catch (ParseException ignored) {
            }
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            map.put("date", sdf.format(calendar.getTime()));
        }
        return ((Number) createSQLQuery(sql, map).uniqueResult()).toString();
    }
    /**
     * 更新审批时间
     *
     * @param billId 账单ID
     */
    public void updateApproveDate(String billId) {
        String sql = "update FC_CORP_COMPARE_BILL set CHECK_DATE = :checkDate where ID = :billId ";
        Map<String, Object> params = new HashMap<>();
        params.put("checkDate", new Date());
        params.put("billId", billId);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 更新打印次数
     *
     * @param billId 账单ID
     */
    public void updateBillPrintCount(String billId) {
        String hql = "update FC_CORP_COMPARE_BILL set prints = nvl(prints,0) + 1 where id = :billId ";
        Map<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        createSQLQuery(hql, params).executeUpdate();
    }
    /**
     * 判断月账单是否可以核对
     *
     * @param date 月账单
     * @return true 存在，false 不存在
     */
    public boolean existBalance(String date) {
        String sql = "SELECT id FROM fc_acc_balance WHERE balance_date = BT_TO_DATE(:date,'yyyy-MM-dd')";
        Map<String, Object> params = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date + "-01"));
        } catch (ParseException ignored) {
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        params.put("date", sdf.format(calendar.getTime()));
        List<String> strings = listBySQLAliasToBean(sql, 0, 1, params, String.class);
        return !CollectionUtils.isEmpty(strings);
    }
}
