package com.bytter.biz.report.dao;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.report.vo.InternalAndBankAccBalVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.proxy.DynamicallyObject;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.constant.CloseConstant;
/**
 * @author zhoup
 * @Description 内部银行账户余额分析情况表
 * @date 2018.6.8
 */
@Repository
public class InternalAndBankAccBalAnalysisDao extends BaseDao<InternalAndBankAccBalVO, String> {
    public DataTablesOutput getInternalAndBankAccBal(DataTablesInput input, List<String> corpAuth) {
        if (!StringUtils.isNotEmpty(corpAuth)) {
            return null;
        }
        DataTablesInput inputs = new DataTablesInput();
        inputs.setLength(input.getLength());
        inputs.setStart(input.getStart());
        inputs.setDraw(input.getDraw());
        Map<String, Object> param = input.getSearchValues();
        StringBuilder sql = new StringBuilder();
        try {
            sql.append(" select c.netName as netName , c.netId as netId,c.corpId as corpId,c.corpName as corpName,c.curId as curId,c.curName as curName,"
                    + "  c.bankAcc as bankAcc, c.bankName as bankName,c.bankType,"
                    + "  c.isOnLine as isOnLine from  ");
            sql.append(" ( " + getBankAccBal(input, corpAuth) + " ) c ");
            sql.append(" where 1=1");
            if (StringUtils.isNotEmpty(corpAuth)) {
                sql.append(" and ").append(getSQLIn(corpAuth, 900, "c.corpId"));
            }
            sql.append(getWhereAllCondition(input, InternalAndBankAccBalVO.class.newInstance(), "", false));
            sql.append(" order by c.corpId,c.bankType,c.bankAcc,c.isOnLine desc");
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.remove("eqbalDate");
        return getDynamicBySQL(inputs, sql.toString(), InternalAndBankAccBalVO.class, null, false, DynamicallyObject.getInstance(param));
    }
    public List<InternalAndBankAccBalVO> queryBankAccBal(List<String> accList, List<String> curList, DataTablesInput input) {
        Map<String, Object> param = input.getSearchValues();
        String eqbalDate = DateUtils.formatDate((Date) param.get("eqbalDate"));
        StringBuilder sql = new StringBuilder();
        sql.append(" select bal.bank_Acc as bankAcc, bal.cur_id as curId,");
        sql.append(" bt_nvl_num(bal.bal,0) as bal,bt_nvl_num(bal.frz_bal,0) as frzBal,bt_nvl_num(bal.AVAIL_BAL,0) as useBal, bac.counter_acc as accountAcc ");
        sql.append(" FROM bis_acc_bal bal ");
        sql.append(" left join bt_bank_acc acc on bal.bank_Acc = acc.bank_acc ");
        sql.append(" left join bt_bank_acc_cur bac on acc.id = bac.bank_acc_id and bal.cur_Id = bac.cur_id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and ").append(getSQLIn(accList, 900, "bal.bank_acc"));
        sql.append(" and ").append(getSQLIn(curList, 900, "bal.cur_id"));
        sql.append(" and bal.bal_date  =  bt_to_date('" + eqbalDate + "','yyyy-MM-dd') ");
        return listBySQLAliasToBean(sql.toString(), null, InternalAndBankAccBalVO.class);
    }
    /**
     * 功能介绍 : 获取网点的日结日期。<br>
     * <b>日期：</b>2018年9月5日<br>
     *
     * @version 1.0
     * @author zhoup
     */
    public Map getDayCloseDate(String netId) {
        String sql = "select CLOSE_DATE from FC_DAY_CLOSE where net_Code = :netId and over_Type = :overType ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", netId);
        params.put("overType", CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get());
        return getBySQL(sql, params);
    }
    /**
     * 查银行账号余额
     *
     * @param input
     * @param corpAuth
     * @return
     */
    public String getBankAccBal(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> param = input.getSearchValues();
        String eqbalDate = DateUtils.formatDate((Date) param.get("eqbalDate"));
        sql.append(" select acc.corp_Id as corpId,sy.name as corpName,cur.cur_name as curName, bac.cur_id as curId, sy.net_id as netId,sc.name as netName,");
        sql.append(" acc.bank_Acc as bankAcc, acc.acc_name as bankName,acc.bank_type_id as bankType,");
//        sql.append(" b.bal as bal,bt_nvl_num(b.frz_bal,0) as frzBal,b.AVAIL_BAL as useBal, bac.counter_acc as accountAcc,");
        sql.append(" acc.is_online as isOnLine ");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_bank_acc_cur bac on acc.id = bac.bank_acc_id ");
        sql.append(" left join sys_corp sy on acc.corp_Id = sy.id"); //查单位名称
        sql.append(" left join bt_currency  cur on bac.cur_Id = cur.id");
        sql.append(" left join sys_corp sc on sy.net_id = sc.id where 1=1"); //查网点
//        sql.append(" and b.bal_date=(select max(ba.bal_date) from bis_acc_bal ba where ba.bank_acc=b.bank_acc and ba.cur_id=b.cur_id and ba.bal_date <=bt_to_date('" + eqbalDate + "','yyyy-MM-dd'))");
        return sql.toString();
    }
    /**
     * 查询内部账号的余额情况，当查询的余额日期小于等于对应单位日结日期时，余额在fc_acc_balance表里面查,且取离查询的余额日期最近的余额
     * 当余额日期大于对应单位日结日期时，在fc_online_ledger表里面查
     *
     * @param input
     * @param corpAuth
     * @return
     */
    public String getFcAccBal(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        List<String> list = new ArrayList<>();
        int i = 0;
        Map<String, Object> param = input.getSearchValues();
        String str = (String) param.get("innetId");
        if (StringUtils.isNotEmpty(str)) {
            String[] strs = str.split(",");
            list = Arrays.asList(strs);
        } else {
            list = corpAuth;
        }
        String eqbalDate = DateUtils.formatDate((Date) param.get("eqbalDate"));
        String eqbalDateEnd = DateUtils.format(DateUtils.getIntegralEndTime(DateUtils.parse(eqbalDate, "yyyy-MM-dd")));
        sql.append(" select fcBal.corpId as corpId,sy.name as corpName,cur.cur_name as curName,fcBal.curId  as curId,fcBal.netId as netId,sc.name as netName,");
        sql.append(" fcBal.accountAcc as accountAcc,fcBal.accountName as accountName, ");
        sql.append(" fcBal.balance as balance,fcBal.freezeMoney as freezeMoney,fcBal.useMoney as useMoney from(");
        for (String netId : list) {
            i++;
            Date closeDate = null;
            Map<String, Object> mapCloseDate = this.getDayCloseDate(netId);
            if (StringUtils.isNotEmpty(mapCloseDate)) {
                closeDate = (Date) mapCloseDate.get("CLOSE_DATE");
            }
            sql.append(" (select fc.corp_id as corpId,fc.cur_id  as curId,fc.blnctr_acc as accountAcc,fc.acc_name as accountName,fc.net_id as netId, ");
            if (StringUtils.isNotEmpty(closeDate)) {
                if (closeDate.compareTo(DateUtils.parse(eqbalDate, "yyyy-MM-dd")) < 0) {
                    sql.append(" fol.CHECK_BALANCE as balance,bt_nvl_num(fol.FREEZE_MONEY,0) as freezeMoney,(fol.CHECK_BALANCE - bt_nvl_num(fol.FREEZE_MONEY,0)) as useMoney");
                } else {
                    sql.append(" fab.balance as balance ,bt_nvl_num(fab.freeze_money,0) as freezeMoney,( fab.balance - bt_nvl_num(fab.freeze_money,0)) as useMoney ");
                }
            } else {
                sql.append(" fol.CHECK_BALANCE as balance,bt_nvl_num(fol.FREEZE_MONEY,0) as freezeMoney,(fol.CHECK_BALANCE - bt_nvl_num(fol.FREEZE_MONEY,0)) as useMoney");
            }
            if (StringUtils.isNotEmpty(closeDate)) {
                if (closeDate.compareTo(DateUtils.parse(eqbalDate, "yyyy-MM-dd")) < 0) {
                    sql.append(" from fc_online_ledger fol left join fc_acc fc on fc.BLNCTR_ACC=fol.account and fc.cur_Id=fol.cur_code ");
                } else {
                    sql.append(" from fc_acc_balance fab left join fc_acc fc on fc.blnctr_acc=fab.account and fc.cur_id=fab.cur_id ");
                    sql.append(" where fab.balance_date=(select max(fa.BALANCE_DATE) from fc_acc_balance fa where fa.ACCOUNT=fc.BLNCTR_ACC and fc.cur_id=fa.cur_id and fa.BALANCE_DATE <=bt_to_date('" + eqbalDate + "','yyyy-MM-dd'))");
                }
            } else {
                sql.append(" from fc_online_ledger fol left join fc_acc fc on fc.BLNCTR_ACC=fol.account and fc.cur_Id=fol.cur_code ");
            }
            sql.append(" and fc.net_id='" + netId + "')");
            if (i < list.size()) {
                sql.append(" union ");
            }
        }
        sql.append(" )fcBal");
        sql.append(" left join sys_corp sy on sy.id=fcBal.corpId");
        sql.append(" left join bt_currency cur on fcBal.curId=cur.id");
        sql.append(" left join sys_corp sc on sc.id=fcBal.netId");
        return sql.toString();
    }
}
