package com.bytter.report.domain.synthesize.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.synthesize.constants.MoneyMapConstant;
import com.bytter.report.domain.synthesize.vo.CountryVO;
import com.bytter.report.domain.synthesize.vo.MoneyMapVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Map;
/**
 * 资金地图Dao
 *
 * @author zuoy
 * @since 2020-01-16
 */
@Component
public class MoneyMapDao extends BaseDao {
    /**
     * 获取银行账号余额
     * 世界地图
     *
     * @return
     */
    public List<CountryVO> listBankAccBalanceByContinent(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select continent, cur_id, sum(totalBal) as balance from ( ");
        sql.append("    select case acc.is_domestic_bank when '1' then '5'");
        sql.append("     when '0' then (select sc.continent from bt_swift_info s left join sys_country sc on s.country_code = sc.code where s.swift_code = acc.bank_code) end as continent, ");
        sql.append("     b.bank_acc, b.cur_id, acc.bank_code, sum(b.avail_bal) as totalBal ");
        sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
        sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
        sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
        sql.append("     where t.bal_date = b.bal_date ");
        // 不为贷款户
        sql.append(" and acc.acc_type <> '03' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" group by b.bank_acc, b.cur_id, acc.bank_code, acc.is_domestic_bank ");
        sql.append(" ) m ");
        sql.append("group by continent, cur_id ");
        Map<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取内部账号余额
     * 世界地图
     *
     * @return
     */
    public List<CountryVO> listInnerAccCountryBalance(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.cur_code as curId, sum(fc.check_balance-fc.freeze_money) as balance from fc_online_ledger fc, fc_acc fa ");
        sql.append("    where fc.account = fa.blnctr_acc and fa.acc_state <> 'D' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "fa.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append("    group by fc.cur_code ");
        Map<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取银行账号余额
     * 大洲地图
     *
     * @param continent           国家所属大区
     * @param countryNameLanguage 国家语言类型
     * @return
     */
    public List<CountryVO> listBankAccBalanceByContinentCountry(String continent, String countryNameLanguage, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select countryName, cur_id, sum(totalBal) as balance from (  ");
        if (MoneyMapConstant.COUNTRY_LANGUAGE_ZH.equalsIgnoreCase(countryNameLanguage)) {
            sql.append("    select case acc.is_domestic_bank when '1' then '中国' when '0' then sc.name_zh end as countryName, ");
        } else {
            sql.append("    select case acc.is_domestic_bank when '1' then 'Chain' when '0' then sc.name_en end as countryName, ");
        }
        sql.append("     b.bank_acc, b.cur_id, acc.bank_code, sum(b.avail_bal) as totalBal  ");
        sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
        sql.append("     left join bt_swift_info s on s.swift_code = acc.bank_code left join sys_country sc on s.country_code = sc.code ");
        sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
        sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
        sql.append("     where t.bal_date = b.bal_date ");
        sql.append(" and sc.continent = :continent ");
        // 不为贷款户
        sql.append(" and acc.acc_type <> '03' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" group by b.bank_acc, b.cur_id, acc.bank_code, acc.is_domestic_bank, sc.name_en, sc.name_zh ");
        sql.append(" ) m ");
        sql.append("group by countryName, cur_id ");
        Map<String, Object> params = Maps.newHashMap();
        params.put("continent", continent);
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取银行账号余额
     * 中国地图
     *
     * @return
     */
    public List<CountryVO> listBankAccBalanceByChina(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select prov, cur_id, sum(totalBal) as balance,continent from ( ");
        sql.append("    select case acc.is_domestic_bank when '1' then '5' when '0' ");
        sql.append("    then (select sc.continent from bt_swift_info s left join sys_country sc  ");
        sql.append("    on s.country_code = sc.code where s.swift_code = acc.bank_code) end as continent,  ");
        sql.append("    acc.prov as prov, b.bank_acc, b.cur_id,  sum(b.avail_bal) as totalBal ");
        sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
        sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
        sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
        sql.append("     where t.bal_date = b.bal_date and acc.is_domestic_bank = 1 ");
        // 不为贷款户
        sql.append(" and acc.acc_type <> '03' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" group by b.bank_acc, b.cur_id, acc.prov  ");
        sql.append(" ) m ");
        sql.append("group by prov, cur_id ");
        Map<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取内部账号余额
     * 中国地图
     *
     * @return
     */
    public List<CountryVO> listInnerAccCountryBalanceByChina(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.cur_code as curId, sum(fc.check_balance-fc.freeze_money) as balance from fc_online_ledger fc, fc_acc fa ");
        sql.append("    where fc.account = fa.blnctr_acc and fa.acc_state <> 'D' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "fa.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append("    group by fc.cur_code ");
        Map<String, Object> params = Maps.newHashMap();
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取银行账号余额
     * 中国地图
     *
     * @return
     */
    public List<CountryVO> listBankAccBalanceByProvince(String provId, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        sql.append("select city, cur_id, sum(totalBal) as balance from ( ");
        sql.append("    select acc.city as city, b.bank_acc, b.cur_id,  sum(b.avail_bal) as totalBal ");
        sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
        sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
        sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
        sql.append("     where t.bal_date = b.bal_date and acc.is_domestic_bank = 1 ");
        sql.append(" and acc.prov = :provId ");
        params.put("provId", provId);
        // 不为贷款户
        sql.append(" and acc.acc_type <> '03' ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" ");
        sql.append(" group by b.bank_acc, b.cur_id, acc.city  ");
        sql.append(" ) m ");
        sql.append("group by city, cur_id ");
        return listBySQLAliasToBean(sql.toString(), params, CountryVO.class);
    }
    /**
     * 获取银行账号余额
     * 按单位统计
     *
     * @return
     */
    public DataTablesOutput listBankAccBalanceByCorp(DataTablesInput dataTablesInput, List<String> corpIds) {
        String corpId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("corpId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("corpId"));
        String balanceType = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("balanceType")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("balanceType"));
        String curId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("curId"));
        String bankAcc = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankAcc")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankAcc"));
        String balanceDate = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("balanceDate")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("balanceDate"));
        String bankName = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankName")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankName"));
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        sql.append(" select * from ( ");
        // 银行余额
        if (!StringUtils.hasText(balanceType) || (StringUtils.hasText(balanceType) && MoneyMapConstant.BALANCE_TYPE_BANK_ACC.equalsIgnoreCase(balanceType))) {
            sql.append("select acc.corp_id, '2' as balanceType, t.countryName, acc.prov, acc.bank_acc, acc.acc_type, acc.attribute_id,acc.nature_id, t.cur_id, t.balance from ( ");
            sql.append("select countryName, bank_acc, cur_id, sum(totalBal) as balance from ( ");
            sql.append("    select case acc.is_domestic_bank when '1' then (select sc.name_zh from sys_country sc where sc.name_zh='中国') ");
            sql.append("     when '0' then (select sc.name_zh from bt_swift_info s left join sys_country sc on s.country_code = sc.code where s.swift_code = acc.bank_code) end as countryName, ");
            sql.append("     b.bank_acc, b.cur_id, acc.bank_code, sum(b.avail_bal) as totalBal ");
            sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
            sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
            sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
            sql.append("     where t.bal_date = b.bal_date and acc.is_domestic_bank = 1 ");
            if (StringUtils.hasText(balanceDate)) {
                sql.append(" and t.bal_date <= :balanceDate ");
                params.put("balanceDate", DateUtils.parseDate(balanceDate));
            }
            // 不为贷款户
            sql.append(" and acc.acc_type <> '03' ");
            String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
            sql.append("  group by b.bank_acc, b.cur_id, acc.bank_code, acc.is_domestic_bank) b2 ");
            sql.append(" group by countryName,bank_acc, cur_id  ) t ");
            sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
            if (!StringUtils.hasText(balanceType)) {
                sql.append(" union all ");
            }
        }
        // 内部存款
        //TODO SYS服务提供
        if (!StringUtils.hasText(balanceType) || (StringUtils.hasText(balanceType) && MoneyMapConstant.BALANCE_TYPE_INNER_ACC.equalsIgnoreCase(balanceType))) {
            sql.append(" select acc.corp_id, '1' as balanceType, '中国' as countryName,  (select r.id from sys_region r where r.name='广东省' ) as prov, acc.blnctr_acc as bank_acc, acc.acc_type, acc.attribute_Id, acc.nature_Code_Id as nature_id, t.cur_id, t.balance from ( ");
            sql.append(" select fc.cur_code as cur_Id,fc.account, sum(fc.check_balance-fc.freeze_money) as balance from fc_online_ledger fc, fc_acc fa  ");
            sql.append("  where fc.account = fa.blnctr_acc and fa.acc_state <> 'D' ");
            String corpAuthSql = SqlUtil.getInSql(corpIds, "fa.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
            sql.append(" group by fc.cur_code,fc.account) t ");
            sql.append(" left join fc_acc acc on t.account = acc.blnctr_acc ");
        }
        sql.append(" ) t where 1 = 1  ");
        // 过滤余额为0
        sql.append(" and t.balance <> 0 ");
        if (StringUtils.hasText(corpId)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "t.corp_id", true));
        }
        if (StringUtils.hasText(curId)) {
            sql.append(" and t.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (StringUtils.hasText(bankAcc)) {
            sql.append(" and t.bank_acc like :bankAcc");
            params.put("bankAcc", "%" + bankAcc + "%");
        }
        sql.append(" order by t.corp_id, t.bank_acc, t.countryName, t.cur_id ");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, MoneyMapVO.class);
    }
    public DataTablesOutput listBankAccBalanceByBank(DataTablesInput dataTablesInput, List<String> corpIds) {
        String corpId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("corpId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("corpId"));
        String balanceType = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("balanceType")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("balanceType"));
        String curId = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("curId")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("curId"));
        String bankAcc = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankAcc")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankAcc"));
        String balanceDate = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("balanceDate")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("balanceDate"));
        String bankName = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("bankName")) ? "" : String.valueOf(dataTablesInput.getFormdata().get("bankName"));
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from ( ");
        // 银行余额
        if (!StringUtils.hasText(balanceType) || (StringUtils.hasText(balanceType) && MoneyMapConstant.BALANCE_TYPE_BANK_ACC.equalsIgnoreCase(balanceType))) {
            sql.append("select acc.corp_id, bt.type_name as bankTypeName, acc.bank_name, '2' as balanceType, t.countryName, acc.prov, acc.bank_acc, acc.acc_type, acc.attribute_id,acc.nature_id, acc.is_online, t.cur_id,  t.balance from ( ");
            sql.append("select countryName, bank_acc, cur_id, sum(totalBal) as balance from ( ");
            sql.append("    select case acc.is_domestic_bank when '1' then (select sc.name_zh from sys_country sc where sc.name_zh='中国') ");
            sql.append("     when '0' then (select sc.name_zh from bt_swift_info s left join sys_country sc on s.country_code = sc.code where s.swift_code = acc.bank_code) end as countryName, ");
            sql.append("     b.bank_acc, b.cur_id, acc.bank_code, sum(b.avail_bal) as totalBal ");
            sql.append("     from bis_acc_bal b left join bt_bank_acc acc on b.bank_acc = acc.bank_acc ");
            sql.append("     left join (select bal.bank_acc, bal.cur_id,  max(bal.bal_date) as bal_date from bis_acc_bal bal group by bal.bank_acc, bal.cur_id) t ");
            sql.append("     on b.bank_acc = t.bank_acc and b.cur_id = t.cur_id ");
            sql.append("     where t.bal_date = b.bal_date and acc.is_domestic_bank = 1 ");
            if (StringUtils.hasText(balanceDate)) {
                sql.append(" and t.bal_date <= :balanceDate ");
                params.put("balanceDate", DateUtils.parseDate(balanceDate));
            }
            // 不为贷款户
            sql.append(" and acc.acc_type <> '03' ");
            String corpAuthSql = SqlUtil.getInSql(corpIds, "acc.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
            sql.append("  group by b.bank_acc, b.cur_id, acc.bank_code, acc.is_domestic_bank) b2 ");
            sql.append(" group by countryName,bank_acc, cur_id  ) t ");
            sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
            sql.append(" left join bt_bank_type bt on acc.bank_type_id = bt.id ");
            if (!StringUtils.hasText(balanceType)) {
                sql.append(" union all ");
            }
        }
        // 内部存款
        if (!StringUtils.hasText(balanceType) || (StringUtils.hasText(balanceType) && MoneyMapConstant.BALANCE_TYPE_INNER_ACC.equalsIgnoreCase(balanceType))) {
            sql.append(" select acc.corp_id, c.name as bankTypeName, c1.name as bank_name, '1' as balanceType, '中国' as countryName,  (select r.id from sys_region r where r.name='广东省' ) as prov,");
            sql.append("  acc.blnctr_acc as bank_acc, acc.acc_type, acc.attribute_Id, acc.nature_Code_Id as nature_id, '' as is_online, t.cur_id, t.balance from ( ");
            sql.append(" select fc.cur_code as cur_Id,fc.account, sum(fc.check_balance-fc.freeze_money) as balance from fc_online_ledger fc, fc_acc fa  ");
            sql.append("  where fc.account = fa.blnctr_acc and fa.acc_state <> 'D' ");
            String corpAuthSql = SqlUtil.getInSql(corpIds, "fa.corp_id", true);
            sql.append(" and ").append(corpAuthSql);
            sql.append(" group by fc.cur_code,fc.account) t ");
            sql.append(" left join fc_acc acc on t.account = acc.blnctr_acc ");
            sql.append(" left join sys_corp c on c.id=acc.net_id ");
            sql.append(" left join sys_corp c1 on c1.id=acc.corp_id ");
        }
        sql.append(" ) t where 1 = 1 ");
        // 过滤余额为0
        sql.append(" and t.balance <> 0 ");
        if (StringUtils.hasText(corpId)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "t.corp_id", true));
        }
        if (StringUtils.hasText(curId)) {
            sql.append(" and t.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (StringUtils.hasText(bankAcc)) {
            sql.append(" and t.bank_acc like :bankAcc");
            params.put("bankAcc", "%" + bankAcc + "%");
        }
        if (StringUtils.hasText(bankName)) {
            sql.append(" and t.bankTypeName like :bankName");
            params.put("bankName", "%" + bankName + "%");
        }
        sql.append(" order by t.bankTypeName, t.bank_acc, t.countryName, t.cur_id ");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString(), params, MoneyMapVO.class);
    }
}
