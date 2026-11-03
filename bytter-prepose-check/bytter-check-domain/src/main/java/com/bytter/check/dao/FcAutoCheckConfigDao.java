package com.bytter.check.dao;
import com.bytter.check.entity.FcAutoCheckConfig;
import com.bytter.check.vo.FcAutoCheckConfigVO;
import com.bytter.check.vo.FcBankCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class FcAutoCheckConfigDao extends BaseDao<FcAutoCheckConfig, String> {
    /**
     * 功能介绍 :自动对账配置信息查询
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fac.id,fac.AUTO_CHECK_CODE as autoCheckCode,fac.AUTO_CHECK_NAME as autoCheckName,fac.CHECK_TYPE as checkType,");
        sql.append(" fac.LAUNCH_TIME as launchTime,fac.CHECK_CYCLE as checkCycle,fac.VALID_SIGN as validSign");
        sql.append(" from FC_AUTO_CHECK_CONFIG fac ");
        sql.append(" where 1=1 ");
        sql.append(" and fac.STATUS<>-2");
        OrderString orderString = new OrderString("fac.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAutoCheckConfigVO.class, null, false);
    }
    public List<FcAutoCheckConfigVO> getAutoCheckByCOde(String code, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select fac.id,fac.AUTO_CHECK_CODE as autoCheckCode,fac.AUTO_CHECK_NAME as autoCheckName,fac.CHECK_TYPE as checkType,");
        sql.append(" fac.LAUNCH_TIME as launchTime,fac.CHECK_CYCLE as checkCycle,fac.VALID_SIGN as validSign");
        sql.append(" from FC_AUTO_CHECK_CONFIG fac ");
        sql.append(" where 1=1 ");
        sql.append(" and fac.STATUS<>-2");
        sql.append(" and fac.AUTO_CHECK_CODE=:code");
        if (!EmptyUtils.isEmpty(id)) {
            sql.append(" and fac.id<>:id");
            params.put("id", id);
        }
        params.put("code", code);
        return listBySQLAliasToBean(sql.toString(), params, FcAutoCheckConfigVO.class);
    }
    /**
     * 功能介绍 :查询中心对账数据
     */
    //TODO fc_check_init、fc_bank_balance_adjust 都是属于属于资金核对模块的
    public List<FcBankCheckVO> getAutoFcBankCheckVO(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ")
                .append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, ")
                .append(" acc.bank_name as bankName, acc.acc_name as accName, ")
                .append(" bc.cur_name as curName,acc.corp_id as corpId,sc.name as corpName, ")
                .append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate ")
                .append(" from fc_check_init fc left join ")
                .append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
                .append("  where (fb.status <> -2 or fb.status is null) ")
                .append(" and fb.check_type = '0' ")    //FC_BANK_BALANCE_ADJUST.check_type（0中心对账 1ERP对账）
                .append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
                .append(" inner join bt_bank_acc acc on fc.bank_acc = acc.bank_acc ") //基础
                .append(" inner join bt_currency bc on fc.cur_id = bc.id ") // 拆，基础
                .append(" inner join sys_corp sc on acc.corp_id = sc.id ") //把 bt_bank_acc\bt_currency\sys_corp
                .append("  where fc.check_type = '1'  and acc.valid_Sign = '1' and acc.status = 95 ")
                .append(" and ").append(getSQLIn(corpIds, 900, "acc.corp_id"));
        return listBySQLAliasToBean(sql.toString(), null, FcBankCheckVO.class);
    }
    /**
     * 功能介绍 :查询ERP对账数据
     */
    //TODO fc_check_init、fc_bank_balance_adjust 都是属于属于资金核对模块的
    public List<FcBankCheckVO> getErpFcBankCheckVO(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ")
                .append(" fc.bank_acc as bankAcc,fc.account as account,fc.cur_id as curId, ")
                .append(" acc.bank_name as bankName, acc.acc_name as accName, ")
                .append(" bc.cur_name as curName,acc.corp_id as corpId,sc.name as corpName, ")
                .append(" fc.check_date as checkDate,fd.begin_date as beginDate, fd.end_date as endDate ")
                .append(" from fc_check_init fc left join ")
                .append(" (select fb.bank_acc,fb.account,fb.cur_code,max(end_date) as end_date,max(begin_date) as begin_date from FC_BANK_BALANCE_ADJUST fb ")
                .append("  where (fb.status <> -2 or fb.status is null) ")
                .append(" and fb.check_type = '1' ")    //FC_BANK_BALANCE_ADJUST.check_type（0中心对账 1ERP对账）
                .append(" group by fb.bank_acc,fb.account,fb.cur_code) fd on fc.bank_acc = fd.bank_acc and fc.account = fd.account and fc.cur_id = fd.cur_code ")
                .append(" inner join bt_bank_acc acc on fc.bank_acc = acc.bank_acc ")
                .append(" inner join bt_currency bc on fc.cur_id = bc.id ")
                .append(" inner join sys_corp sc on acc.corp_id = sc.id ") // 拆
                .append("  where fc.check_type = '2' ")
                .append(" and ").append(getSQLIn(corpIds, 900, "acc.corp_id"));
        return listBySQLAliasToBean(sql.toString(), null, FcBankCheckVO.class);
    }
}
