package com.bytter.biz.finacial.closedeal.dao;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.closedeal.vo.FcAccountBalanceVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 账户批量余额表
 * @date 2017-06-23 14:22:37
 */
@Repository
public class FcAccountBalanceDao extends BaseDao<FcAccountBalanceVo, String> {
    /**
     * 账户余额查询
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<FcAccountBalanceVo> getFcAccBalance(DataTablesInput dataTablesInput, StringBuilder authSql) {
        StringBuilder sql = getSql().append(authSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccountBalanceVo.class, "", false);
    }
    /**
     * 账户余额查询（导出时用）
     */
    public List<FcAccountBalanceVo> getFcAccBalance(Date ged_balanceDate, Date led_balanceDate, List<String> accList) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = getSql();
        if (!EmptyUtils.isEmpty(led_balanceDate) && !EmptyUtils.isEmpty(ged_balanceDate)) {
            sql.append(" and b.balance_date >= :begindate and b.balance_date < :enddate ");
            param.put("begindate", ged_balanceDate);
            param.put("enddate", led_balanceDate);
        }
        if (!CollectionUtils.isEmpty(accList)) {
            sql.append(SqlUtil.getInSql(accList, "t.account", true));
        }
        return listBySQLAliasToBean(sql.toString(), param, FcAccountBalanceVo.class);
    }
    /**
     * 获取sql语句
     */
    public StringBuilder getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.balance_date as balanceDate, b.account as account,b.yester_balance as yesterBalance, ");
        sql.append(" b.debit_money as debitMoney ,b.credit_money as creditMoney ,b.balance as balance ");
        sql.append(" from fc_acc_balance b where ");
        return sql;
    }
    /**
     * 获取用户有权限的账号
     * TODO 改为由基础模块提供
     * 没有被调用 暂注释
     */
	/*public List<FcAccDTO> getAccount(List<String> corps) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.blnctr_acc as blnctrAcc, a.acc_name as accName  from Fc_Acc a ");
		sql.append(" where a.acc_State in ('N', 'P', 'S')  and a.corp_id in(:corpId) order by a.blnctr_acc ");
		Map<String , Object> param = new HashMap<String, Object>();
		param.put("corpId", corps);
		return listBySQLAliasToBean(sql.toString(), param, FcAccDTO.class);
	}*/
}
