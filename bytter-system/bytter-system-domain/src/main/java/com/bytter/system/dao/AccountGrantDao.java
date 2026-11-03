package com.bytter.system.dao;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.entity.AccountGrant;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/7/5
 */
@Repository
public class AccountGrantDao extends BaseDao<AccountGrant, String> implements InitializingBean {
    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private IConfigService configServiceI;
    public List<AccountGrant> listAssigned(String accountId, String accountType, String userId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        hql.append("from AccountGrant where 1=1 ");
        if (!EmptyUtils.isBlank(accountType) && AccountGrantConstant.INNER_BANK_TYPE.equals(accountType)) {
            hql.append(" and accountType=:accountType");
            map.put("accountType", Integer.parseInt(accountType));
        }
        if (!EmptyUtils.isBlank(accountId)) {
            hql.append(" and accountId=:accountId ");
            map.put("accountId", accountId);
        }
        if (!EmptyUtils.isBlank(userId)) {
            hql.append(" and userId=:userId ");
            map.put("userId", userId);
        }
        return createQuery(hql.toString(), map).list();
    }
    //查询变更前的账号权限
    public AccountGrant getAccountGrant(String accountId, Integer accountType, String userId, String corpId) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        hql.append("select s.account_id as accountId,s.account_type accountType,s.user_id as userId,s.corp_id as corpId,"
                + "s.can_query_detail as canQueryDetail,s.can_query_balance as canQueryBalance,s.can_transfer_accounts as canTransferAccounts  from SYS_ACCOUNT_GRANT s where 1=1 ");
        if (StringUtils.isNotEmpty(accountType) && accountType.equals(1)) {
            hql.append(" and s.account_type=:accountType");
            map.put("accountType", accountType);
        }
        if (!EmptyUtils.isBlank(accountId)) {
            hql.append(" and s.account_id=:accountId ");
            map.put("accountId", accountId);
        }
        if (!EmptyUtils.isBlank(userId)) {
            hql.append(" and s.user_id=:userId ");
            map.put("userId", userId);
        }
        if (!EmptyUtils.isBlank(corpId)) {
            hql.append(" and s.corp_id=:corpId ");
            map.put("corpId", corpId);
        }
        return getBySQLAliasToBean(hql.toString(), map, AccountGrant.class);
    }
    public void delete(AccountGrant accountGrant) {
        //update by zhoul 2018-12-05
        //新增corpId作为删除条件
        String hql = "delete from AccountGrant where accountId=?0 and accountType=?1 and userId=?2 and corpId=?3";
        createQuery(hql, accountGrant.getAccountId(), accountGrant.getAccountType(), accountGrant.getUserId(), accountGrant.getCorpId()).executeUpdate();
    }
    public List<String> listBy(String corpId, String accountType, String permission) {
        return getListByHql(null, corpId, null, null, accountType, permission);
    }
    public List<String> listBy(String userId, String corpId, String accountType, String permission) {
        return getListByHql(userId, corpId, null, null, accountType, permission);
    }
    public List<String> listBy(List<String> corpIds, String accountType, String permission) {
        return getListByHql(null, null, corpIds, null, accountType, permission);
    }
    public List<String> listBy(String userId, List<String> corpIds, String accountType, String permission) {
        return getListByHql(userId, null, corpIds, null, accountType, permission);
    }
    public List<String> listBy(List<String> corpIds, List<String> curIds, String accountType, String permission) {
        return getListByHql(null, null, corpIds, curIds, accountType, permission);
    }
    public List<String> listBy(String userId, List<String> corpIds, List<String> curIds, String accountType, String permission) {
        return getListByHql(userId, null, corpIds, curIds, accountType, permission);
    }
    public List<String> listByV2(String userId, List<String> corpIds, List<String> curIds, String accountType, String permission) {
        return getListByHqlV2(userId, null, corpIds, curIds, accountType, permission);
    }
    public List<String> listBy(String userId, String corpId, List<String> corpIds, List<String> curIds, String accountType, String permission) {
        return getListByHql(userId, corpId, corpIds, curIds, accountType, permission);
    }
    /**
     * 根据条件查询内部账号账户信息[ 返回的是 内部账号列表，不是内部账号id列表]
     *
     * @param corpIds
     * @param curIds
     * @param permission
     * @return
     */
    public List<String> getListAccountByHql(List<String> corpIds, List<String> curIds, String permission) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        sql.append(" select DISTINCT acc.blnctr_acc from sys_account_grant t ");
        sql.append(" inner join fc_acc acc on  t.account_id = acc.id where 1 = 1");
        if (!EmptyUtils.isEmptyList(curIds)) {
            sql.append(" and ").append(getSQLIn(curIds, 900, "acc.cur_id"));
        }
        if (!EmptyUtils.isEmptyList(corpIds)) {
            sql.append(" and ").append(getSQLIn(corpIds, 900, "acc.corp_id"));
        }
        sql.append(getCommonPermission(permission));
        return this.createSQLQuery(sql.toString(), map).list();
    }
    /**
     * 根据条件查询权限
     *
     * @param userId      用户ID
     * @param corpId      单位ID
     * @param corpIds     根据权限查询出的单位ID
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy
     */
    private List<String> getListByHql(String userId, String corpId, List<String> corpIds, List<String> curIds, String accountType, String permission) {
        if (EmptyUtils.isBlank(corpId) && EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        //权限控制类型  1-单位级 2-账号级
        String controlType = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.CONTROLTYPE).getValue();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        sql.append(" select DISTINCT t.account_id from sys_account_grant t ");
        if (!EmptyUtils.isBlank(accountType)) {
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || accountType.equalsIgnoreCase(AccountGrantConstant.POOLINNER_BANK_TYPE)) { // 内部账户
                sql.append(" inner join fc_acc acc on  t.account_id = acc.id where 1 = 1");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" and ").append(getSQLIn(curIds, 900, "acc.cur_id"));
                }
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    sql.append(" and acc.pool_sign = '0' ");
                } else {
                    sql.append(" and acc.pool_sign = '1' ");
                }
            } else { // 在线、离线银行账户
                sql.append(" inner join bt_bank_acc acc on t.account_id = acc.id ");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" inner join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
                }
                sql.append(" where 1 = 1 ");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" and ").append(getSQLIn(curIds, 900, "cur.cur_id"));
                }
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    sql.append(" and is_online = 1 ");
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    sql.append(" and is_online = 0 ");
                }
                if (accountType.split(CommonConstant.COMMA_SPLIT).length > 1 && Arrays.asList(accountType.split(CommonConstant.COMMA_SPLIT)).contains(AccountGrantConstant.ELECTRIC_BILL_BANK_TYPE)) {
                    sql.append(" and acc.electric_bill='1' ");
                }
            }
        } else {
            sql.append(" where 1 = 1 ");
        }
        sql.append(getCommonAccountType(accountType));
        if (!EmptyUtils.isBlank(corpId)) {
            String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
            if (corpArr.length == 1) {
                sql.append(" and acc.corp_id = :corpId ");
                map.put("corpId", corpId);
            } else {
                sql.append(" and ").append(SqlUtil.getInSql(corpArr, "acc.corp_id", true));
            }
        }
        if (!EmptyUtils.isEmptyList(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_id", true));
        }
        // zuoy 2017-10-10
        if (!EmptyUtils.isBlank(userId)) {
            sql.append(" and t.user_id = :userId ");
            map.put("userId", userId);
        }
        if (!CommonConstant.YES.equals(controlType)) {
            sql.append(getCommonPermission(permission));
        }
        return this.createSQLQuery(sql.toString(), map).list();
    }
    /**
     * 根据条件查询权限
     *
     * @param userId      用户ID
     * @param corpId      单位ID
     * @param corpIds     根据权限查询出的单位ID
     * @param curIds      币别ID
     * @param accountType 1内部账号，2银行账号，3离线账号
     * @param permission  d 详情，b 余额查询，t 转账
     * @return
     * @author zuoy
     */
    private List<String> getListByHqlV2(String userId, String corpId, List<String> corpIds, List<String> curIds, String accountType, String permission) {
        if (EmptyUtils.isBlank(corpId) && EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        if (!EmptyUtils.isBlank(accountType)) {
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || accountType.equalsIgnoreCase(AccountGrantConstant.POOLINNER_BANK_TYPE)) { // 内部账户
                sql.append(" select DISTINCT acc.blnctr_acc from sys_account_grant t ");
                sql.append(" inner join fc_acc acc on  t.account_id = acc.id where 1 = 1");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" and ").append(getSQLIn(curIds, 900, "acc.cur_id"));
                }
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)) {
                    sql.append(" and acc.pool_sign = '0' ");
                } else {
                    sql.append(" and acc.pool_sign = '1' ");
                }
            } else { // 在线、离线银行账户
                sql.append(" select DISTINCT acc.bank_acc from sys_account_grant t ");
                sql.append(" inner join bt_bank_acc acc on t.account_id = acc.id ");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" inner join bt_bank_acc_cur cur on acc.id = cur.bank_acc_id ");
                }
                sql.append(" where 1 = 1 ");
                if (!EmptyUtils.isEmptyList(curIds)) {
                    sql.append(" and ").append(getSQLIn(curIds, 900, "cur.cur_id"));
                }
                if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.ONLINE_BANK_TYPE)) {
                    // 仅查询在线账户
                    sql.append(" and is_online = 1 ");
                } else if (accountType.split(CommonConstant.COMMA_SPLIT).length == 1 && accountType.equalsIgnoreCase(AccountGrantConstant.OFFLINE_BANK_TYPE)) {
                    // 仅查询离线账户
                    sql.append(" and is_online = 0 ");
                }
                if (accountType.split(CommonConstant.COMMA_SPLIT).length > 1 && Arrays.asList(accountType.split(CommonConstant.COMMA_SPLIT)).contains(AccountGrantConstant.ELECTRIC_BILL_BANK_TYPE)) {
                    sql.append(" and acc.electric_bill='1' ");
                }
            }
        } else {
            sql.append(" where 1 = 1 ");
        }
        sql.append(getCommonAccountType(accountType));
        if (!EmptyUtils.isBlank(corpId)) {
            String[] corpArr = corpId.split(CommonConstant.COMMA_SPLIT);
            if (corpArr.length == 1) {
                sql.append(" and acc.corp_id = :corpId ");
                map.put("corpId", corpId);
            } else {
                sql.append(" and ").append(SqlUtil.getInSql(corpArr, "acc.corp_id", true));
            }
        }
        if (!EmptyUtils.isEmptyList(corpIds)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_id", true));
        }
        // zuoy 2017-10-10
        if (!EmptyUtils.isBlank(userId)) {
            sql.append(" and t.user_id = :userId ");
            map.put("userId", userId);
        }
        sql.append(getCommonPermission(permission));
        return this.createSQLQuery(sql.toString(), map).list();
    }
    /**
     * 组装1内部账号，2银行账号，3离线账号
     *
     * @param accountType
     * @return
     */
    private static String getCommonAccountType(String accountType) {
        if (!EmptyUtils.isBlank(accountType)) {
            String[] accTypes = accountType.split(CommonConstant.COMMA_SPLIT);
            if (accTypes.length == 1) {
                StringBuilder sql = new StringBuilder();
                String type = accTypes[0];
                if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(type)) {
                    sql.append(" and account_type = " + AccountGrantConstant.INNER_BANK_TYPE);
                } else if (AccountGrantConstant.ONLINE_BANK_TYPE.equalsIgnoreCase(type)) {
                    sql.append(" and account_type = " + AccountGrantConstant.ONLINE_BANK_TYPE);
                } else if (AccountGrantConstant.OFFLINE_BANK_TYPE.equalsIgnoreCase(type)) {
                    sql.append(" and account_type = " + AccountGrantConstant.OFFLINE_BANK_TYPE);
                } else if (AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(type)) {
                    sql.append(" and account_type = " + AccountGrantConstant.POOLINNER_BANK_TYPE);
                }
                return sql.toString();
            } else {
                StringBuilder sql = new StringBuilder(" and ( ");
                for (String type : accTypes) {
                    String temp = "";
                    if (sql.indexOf("account_type") > -1) {
                        temp = " OR ";
                    }
                    if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(type)) {
                        sql.append(temp + " account_type = " + AccountGrantConstant.INNER_BANK_TYPE);
                    } else if (AccountGrantConstant.ONLINE_BANK_TYPE.equalsIgnoreCase(type)) {
                        sql.append(temp + " account_type = " + AccountGrantConstant.ONLINE_BANK_TYPE);
                    } else if (AccountGrantConstant.OFFLINE_BANK_TYPE.equalsIgnoreCase(type)) {
                        sql.append(temp + " account_type = " + AccountGrantConstant.OFFLINE_BANK_TYPE);
                    } else if (AccountGrantConstant.POOLINNER_BANK_TYPE.equalsIgnoreCase(type)) {
                        sql.append(temp + " account_type = " + AccountGrantConstant.POOLINNER_BANK_TYPE);
                    }
                }
                sql.append(")");
                return sql.toString();
            }
        }
        return "";
    }
    /**
     * 组装d 详情，b 余额查询，t 转账
     *
     * @param permission
     * @return
     */
    private String getCommonPermission(String permission) {
        StringBuilder sql = new StringBuilder();
        if (!EmptyUtils.isBlank(permission)) {
            for (String p : permission.split(CommonConstant.COMMA_SPLIT)) {
                if (p.equalsIgnoreCase(AccountGrantConstant.QUERY_DETAIL)) {
                    sql.append(" and can_query_detail = 1 ");
                }
                if (p.equalsIgnoreCase(AccountGrantConstant.QUERY_BALANCE)) {
                    sql.append(" and can_query_balance = 1 ");
                }
                if (p.equalsIgnoreCase(AccountGrantConstant.TRANSFER_ACCOUNT)) {
                    sql.append(" and can_transfer_accounts = 1 ");
                }
            }
        }
        return sql.toString();
    }
    public List<AccountGrant> listByAccountIdsAndUserIds(List<String> accountIds, List<String> userIds) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("accountIds", accountIds);
        map.put("userIds", userIds);
        String sql = "select ag1.* FROM sys_account_grant ag1 left join sys_account_grant ag2 on ag1.id=ag2.id \n" +
                "where ag1.ACCOUNT_ID in (:accountIds) and ag2.USER_ID in (:userIds)";
        return listBySQLAliasToBean(sql, map, AccountGrant.class);
    }
    public void deleteByAccountIdAndUserId(AccountGrant accountGrant) {
        deleteByAccountIdAndUserId(accountGrant, null, null);
    }
    public void deleteByAccountIdAndUserId(AccountGrant accountGrant, List<String> accountIdList, List<String> userId) {
        StringBuilder sql = new StringBuilder("delete from SYS_ACCOUNT_GRANT where 1=1 ");
        Map<String, Object> map = Maps.newHashMap();
        if (accountIdList != null && accountIdList.size() > 0) {
            if (accountIdList.size() > 1000) {
                sql.append(" and ").append(SqlUtil.getInSqlByParams(accountIdList, "ACCOUNT_ID", "accountIds", map));
            } else {
                map.put("accountIds", accountIdList);
                sql.append(" and ACCOUNT_ID in (:accountIds) ");
            }
        } else if (StringUtils.isNotEmpty(accountGrant.getAccountId())) {
            map.put("accountId", accountGrant.getAccountId());
            sql.append(" and ACCOUNT_ID =:accountId ");
        } else {
            return;
        }
        if (userId != null && userId.size() > 0) {
            if (userId.size() > 1000) {
                sql.append(" and ").append(SqlUtil.getInSqlByParams(userId, "USER_ID", "userIds", map));
            } else {
                map.put("userIds", userId);
                sql.append(" and USER_ID in (:userIds) ");
            }
        } else if (StringUtils.isNotEmpty(accountGrant.getUserId())) {
            map.put("userId", accountGrant.getUserId());
            sql.append(" and USER_ID=:userId ");
        } else {
            return;
        }
        createSQLQuery(sql.toString(), map).executeUpdate();
    }
    /**
     * 是否具有权限
     *
     * @param userId
     * @param account
     * @param accountType
     * @param permission
     * @return
     */
    public Boolean checkPermission(String userId, String account, String accountType, String permission, String resource, String joinSql) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(resource)) {
            // 内部账号，资金池账号
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || accountType.equalsIgnoreCase(AccountGrantConstant.POOLINNER_BANK_TYPE)) {
                sql.append(" select acc.id from fc_acc acc where acc.blnctr_acc = :account ");
            } else {
                // 在线、离线银行账户
                sql.append(" select acc.id from bt_bank_acc acc where acc.bank_acc = :account ");
            }
            sql.append(joinSql);
            map.put("account", account);
        } else {
            sql.append(" select t.account_id from sys_account_grant t where exists( ");
            // 内部账号，资金池账号
            if (AccountGrantConstant.INNER_BANK_TYPE.equalsIgnoreCase(accountType)
                    || accountType.equalsIgnoreCase(AccountGrantConstant.POOLINNER_BANK_TYPE)) {
                sql.append(" select fc.id from fc_acc fc where fc.id = t.account_id and fc.blnctr_acc = :account ");
            } else {
                // 在线、离线银行账户
                sql.append(" select acc.id from bt_bank_acc acc where acc.id = t.account_id and acc.bank_acc = :account ");
            }
            sql.append(" ) ");
            map.put("account", account);
            if (!EmptyUtils.isBlank(userId)) {
                sql.append(" and t.user_id = :userId ");
                map.put("userId", userId);
            }
            sql.append(getCommonPermission(permission));
        }
        List list = this.createSQLQuery(sql.toString(), map).list();
        return EmptyUtils.isEmptyList(list);
    }
    /**
     * 删除用所有的账号授权信息
     *
     * @param userId
     */
    public void deleteByUserId(String userId) {
        createSQLQuery("delete from sys_account_grant where user_id = ?", userId).executeUpdate();
    }
    /**
     * 查询用户srcUserId和用户tarUserId 相同/不同的授权账号
     *
     * @param srcUserId
     * @param tarUserId
     * @param flag
     * @return
     */
    public List<AccountGrant> getAccoutnGrantByUser(String srcUserId, String tarUserId, String flag) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("srcUserId", srcUserId);
        params.put("tarUserId", tarUserId);
        StringBuilder sql = new StringBuilder();
        sql.append("select srcGrant.* ");
        sql.append("  from SYS_ACCOUNT_GRANT srcGrant ");
        sql.append(" where srcGrant.User_Id = :srcUserId ");
        if ("1".equals(flag)) {
//            查找不同的账号权限
            sql.append("   and srcGrant.Account_Id not in ");
        } else {
//            查找相同的账号权限
            sql.append("   and srcGrant.Account_Id in ");
        }
        sql.append("       (select tarGrant.Account_Id ");
        sql.append("          from SYS_ACCOUNT_GRANT tarGrant ");
        sql.append("         where tarGrant.User_Id = :tarUserId) ");
        return listBySQLAliasToBean(sql.toString(), params, AccountGrant.class);
    }
    /**
     * 更改账号的类型
     *
     * @param accountId   账号ID
     * @param accountType 更改类型 1内部账号，2银行账号，3离线账号
     */
    public void updateAccountTypeByAccount(String accountId, String accountType) {
        createSQLQuery("update sys_account_grant set account_type = ? where account_id = ?", accountType, accountId).executeUpdate();
    }
    public void saveAccountGrant(AccountGrant accountGrant) {
        Map<String, Object> map = BeanUtils.beanToMap(accountGrant);
        String sql = "insert into sys_account_grant (ID, ACCOUNT_ID, ACCOUNT_TYPE, USER_ID, CORP_ID, CAN_QUERY_DETAIL, CAN_QUERY_BALANCE, CAN_TRANSFER_ACCOUNTS) " +
                "values(:id,:accountId,:accountType, :userId,:corpId,:canQueryDetail,:canQueryBalance, :canTransferAccounts)";
        namedParameterJdbcTemplate.update(sql, map);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
