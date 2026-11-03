package com.bytter.biz.inneraccount.dao;

import com.bytter.biz.inneraccount.entity.FcInitAcc;
import com.bytter.biz.inneraccount.vo.FcInitAccVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账号初始化
 * @date 2017-06-12 18:12:02
 */
@Repository
public class FcInitAccDao extends BaseDao<FcInitAcc, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput tableInit(DataTablesInput dataTablesInput, String joinSql, String loginName, Map<String, Object> params) {
        StringBuilder sb=new StringBuilder();
        StringBuilder sb1=new StringBuilder();
        StringBuilder sb2=new StringBuilder();
        sb1.append("select distinct '0' as id, a.blnctr_acc as account,a.acc_name as accName,a.balance_way as balanceWay,a.cur_id as curId,");
        sb1.append(" 0 as cumulation,0 as balance,-10 as status,a.acc_Start_Date as accStartDate,0 as yearBalance,0 as yearTotal,a.net_id as netId,a.corp_id as corpId");
        sb1.append(" from fc_acc a inner join FC_ONLINE_LEDGER b on a.blnctr_acc=b.account");
        sb1.append(" left join FC_INIT_ACC g on a.BLNCTR_ACC = g.account and g.status!=-2");
        sb1.append(" left join (select account,count(1) cnt from fc_acc_balance group by account) fb on a.blnctr_acc = fb.account");
        sb1.append(" where a.ACC_STATE = 'N' and fb.cnt <=1 and a.acc_type!=:accType and b.tally_cnt=0 ");
        sb1.append(" and b.check_cnt=0 and b.online_balance=0 and b.check_balance=0 and g.id is null ");
        sb1.append(joinSql);
        sb2.append(" select distinct c.id as id, a.blnctr_acc as account,a.acc_name as accName,a.balance_way as balanceWay,a.cur_id as curId,");
        sb2.append(" c.cumulation as cumulation,c.balance as balance,c.status as status,a.acc_Start_Date as accStartDate,");
        sb2.append(" c.year_balance as yearBalance,c.year_total as yearTotal,a.net_id as netId,a.corp_id as corpId");
        sb2.append(" from FC_INIT_ACC c  join  fc_acc a");
        sb2.append(" on c.account=a.blnctr_acc  where 1=1 and a.acc_type!=:accType ");
        sb2.append(" and a.pool_sign<>'1'");//资金池标识：0-非资金池 1-资金池
        sb2.append(joinSql);

        String status="";
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("status"))) {
            status=dataTablesInput.getFormdata().get("status").toString();
        }
        sb.append(getCommonSql());
        params.put("accType", InnerAccountConstant.FCACC_OFFBALANCE);

        if("-10".equals(status)) {
            sb.append(sb1.toString());
        }else if("0".equals(status)){
            sb.append(sb2.toString()).append(" and c.status=:status and c.create_by =:loginName");
            params.put("status",CommonConstant.STATUE_TEMP);
            params.put("loginName",loginName);
        }else if("-1".equals(status)){
            sb.append(sb2.toString()).append(" and c.status=:status and c.create_by =:loginName");
            params.put("status",CommonConstant.STATUE_BACK);
            params.put("loginName",loginName);
        }else{
            sb.append(sb1).append(" union ").append(sb2).append( " and c.status in (:status) and c.create_by =:loginName");
            params.put("status",new Integer[]{CommonConstant.STATUE_TEMP,CommonConstant.STATUE_BACK});
            params.put("loginName",loginName);
        }
        sb.append(" ) s LEFT JOIN sys_corp corp ON s.netId=corp.id LEFT JOIN bt_currency cur ON s.curId=cur.id where 1=1 ");

        return getDynamicBySQL(dataTablesInput, sb.toString(), FcInitAccVO.class, "", false,params);
    }

    private String getCommonSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select s.id as id, s.account as account,s.accName as accName,s.balanceWay as balanceWay,s.curId as curId,");
        sb.append(" s.cumulation as cumulation,s.balance as balance,s.status as status,s.accStartDate as accStartDate,");
        sb.append(" s.yearBalance as yearBalance,s.yearTotal as yearTotal,s.netId as netId,s.corpId as corpId,corp.name as netName, cur.cur_name AS curName from (");
        return sb.toString();
    }

    /**
     * 查询可复核的内部初始化账号列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select * from ( ");
        sb.append(" select distinct c.id as id, c.account, ");
        sb.append(" c.cumulation as cumulation,c.balance as balance,c.status as status,");
        sb.append(" c.year_balance as yearBalance,c.year_total as yearTotal,t.acc_Name as accName,t.CUR_ID as curId,t.CORP_ID as corpId,t.acc_Start_Date as accStartDate");
        sb.append(" from FC_INIT_ACC c  ");
        sb.append(" left join fc_acc t on c.account = t.BLNCTR_ACC  ");
        sb.append(" where 1=1 and c.status=11 ");
        sb.append(joinSql);
        sb.append(" ) s");
        return getDynamicBySQL(dataTablesInput, sb.toString(), FcInitAccVO.class, "", false);
    }

    public List<FcInitAcc> getList(FcInitAcc fcInitAcc, String joinSql) {
        StringBuilder sb = new StringBuilder();
        sb.append("from FcInitAcc ");
        sb.append("where 1=1");
        sb.append(joinSql);
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(fcInitAcc.getAccount())) {
            sb.append(" and account=:account");
            params.put("account", fcInitAcc.getAccount());
        }
        if (StringUtils.isNotEmpty(fcInitAcc.getAccount())) {
            sb.append(" and status=:status");
            params.put("status", fcInitAcc.getStatus());
        }
        if (StringUtils.isNotEmpty(fcInitAcc.getCreateBy())) {
            sb.append(" and createBy !=:createBy");
            params.put("createBy", fcInitAcc.getCreateBy());
        }
        return listByHQL(sb.toString(), params);
    }

    /**
     * 复核通过 or 打回
     *
     * @param ids
     */
    public void updateStatus(String ids, Integer status) {
        String hql = " update FcInitAcc set status=:status where id in (:ids) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("ids", ids.split(","));
        this.batchExecute(hql, params);
    }

    /**
     * 复核通过
     *
     * @param ids
     */
    public void updateStatusCheck(String ids, Integer status, String checkerCode) {
        String hql = " update FcInitAcc set status=:status,checkerCode=:checkerCode,checkTime=:checkTime where id in (:ids) ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", status);
        params.put("checkerCode", checkerCode);
        params.put("checkTime", DateUtils.getDate());
        params.put("ids", ids.split(","));
        this.batchExecute(hql, params);
    }

    public FcInitAcc getById(String id) {
        StringBuilder sb = new StringBuilder();
        sb.append("from FcInitAcc ");
        sb.append("where id =:id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getUnique(sb.toString(), params);
    }

    public List<FcInitAcc> getListByIds(String ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("from FcInitAcc ");
        sb.append("where id in (:ids) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", ids.split(","));
        return listByHQL(sb.toString(), params);
    }

    /**
     * 根据参数查询可以初始化的内部账号
     *
     * @return
     */
    public List<String> findCanInitAccounts() {
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct a.blnctr_acc as account ");
        sb.append(" from fc_acc a left join FC_ONLINE_LEDGER b on a.blnctr_acc=b.account");
        sb.append(" left join (select account, count(account) as acc_bal_count from fc_acc_balance group by account) c on c.account = a.BLNCTR_ACC");
        sb.append(" where a.ACC_STATE = 'N' and a.acc_type!='14' and b.tally_cnt=0 and b.check_cnt=0 and b.online_balance=0 and b.check_balance=0");
        sb.append(" and acc_bal_count<=1");
        return searchBySql(sb.toString(), null);
    }

    /**
     * 每日余额表的借贷金额
     *
     * @param dates
     * @return
     */
    public Map<java.util.Date, BigDecimal[]> findAccBalanceInfoByDate(Date[] dates) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bal.balance_date,sum(case when acc.balance_way = 1 then bal.balance else 0 end) as credit_balance,");
        sql.append("sum(case when acc.balance_way = -1 then bal.balance else 0 end) as debit_balance ");
        sql.append("from fc_acc_balance bal,fc_acc acc where bal.account=acc.blnctr_acc ");
        sql.append("and balance_date in(:dates) group by balance_date");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dates", dates);
        Map<java.util.Date, BigDecimal[]> accBalanceInfo = new HashMap<java.util.Date, BigDecimal[]>();
        for (Object[] result : (List<Object[]>) searchBySql(sql.toString(), params)) {
            accBalanceInfo.put((java.util.Date) result[0], new BigDecimal[]{(BigDecimal) result[1],
                    (BigDecimal) result[2]});
        }
        return accBalanceInfo;
    }

    /**
     * 初始化未复核
     *
     * @param account
     * @return
     */
    public boolean accountInitCheck(String account) {
        String sql = " SELECT COUNT(1) as CO FROM FC_INIT_ACC WHERE ACCOUNT = :account AND CHECKER_CODE IS NULL AND STATUS = :status";
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("status", CommonConstant.STATUE_WAIT_CHECK);
        Map rs = getBySQL(sql, params);
        Integer count = new Integer(rs.get("CO").toString());
        if (count > 0) {
            return true;
        }
        return false;
    }

    public List searchBySql(String sql, Map params) {
        SQLQuery query = createSQLQuery(sql);
        if (params != null) {
            query.setProperties(params);
        }
        return query.list();
    }

    /**
     * 根据账号查对象
     *
     * @param account
     * @return
     */
    public FcInitAcc listByAccount(String account) {
        List<FcInitAcc> list = listBy("account", account);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据帐号查询初始化数据
     * @return
     */
    public List<FcInitAcc> finInitAccByAcc(List<String> accounts) {
        Map<String, Object> param = new HashMap<>();
        param.put("accounts", accounts);
        String hql = " from FcInitAcc where account in (:accounts) and checkerCode is not null";
        return listByHQL(hql, param);
    }

    public  List<FcInitAcc> finInitAccByAccAndStatus(List<String> accounts, Integer status) {
        Map<String, Object> param = new HashMap<>();
        param.put("accounts", accounts);
        param.put("status", status);
        String hql = " from FcInitAcc where account in (:accounts) and status = :status";
        return listByHQL(hql, param);
    }
}
