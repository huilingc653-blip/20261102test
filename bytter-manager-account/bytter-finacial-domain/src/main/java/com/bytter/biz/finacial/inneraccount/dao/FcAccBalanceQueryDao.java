package com.bytter.biz.finacial.inneraccount.dao;
import com.bytter.biz.finacial.inneraccount.vo.FcAccBalanceQueryVO;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 内部账户余额信息表
 * @date 2017-06-28 19:36:12
 */
@Repository
public class FcAccBalanceQueryDao extends BaseDao<FcAccBalanceQueryVO, String> {
    /**
     * 上层没有被调用，注释
     * @param dataTablesInput
     * @param joinSql
     * @param params
     * @return
     */
    /*@SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.account as account, fa.acc_name as accName, fa.corp_id as corpId, fa.subject_id as subjectId,fs.subject_name as subjectName,");
        sql.append(" fc.cur_code as curId, fc.online_balance as onlineBalance, fc.check_balance as checkBalance, (fc.check_balance-fc.freeze_money) as availableBalance,");
        sql.append(" fa.overdraft_money as overdraftMoney,fa.acc_type as accType, fa.acc_open_time as accOpenTime, fa.deadline_date as deadlineDate,fa.relation_acc as relationAcc,");
        sql.append(" fa.interest_rate as interestRate");
        sql.append(" from fc_online_ledger fc, fc_acc fa, fc_subject fs");
        sql.append(" where fc.account = fa.blnctr_acc and fa.subject_id = fs.id and fa.pool_sign<>'1'");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccBalanceQueryVO.class, null, false, params);
    }*/
    /**
     * 获取账户余额集合
     * @param joinSql 单位过滤
     * @param params 参数
     * @param poolFlag 是否资金池
     * @return
     * 没有被调用，注释
     */
    /*public List<FcAccBalanceQueryVO> selectList(StringBuilder joinSql, Map<String, Object> params,Boolean poolFlag){
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.account as account, fa.acc_name as accName, fa.corp_id as corpId, fa.subject_id as subjectId,fs.subject_name as subjectName,");
        sql.append(" fc.cur_code as curId, fc.online_balance as onlineBalance, fc.check_balance as checkBalance, (fc.check_balance-fc.freeze_money) as availableBalance,");
        sql.append(" fa.overdraft_money as overdraftMoney,fa.acc_type as accType, fa.acc_open_time as accOpenTime, fa.deadline_date as deadlineDate,fa.relation_acc as relationAcc,");
        sql.append(" fa.interest_rate as interestRate");
        sql.append(" from fc_online_ledger fc, fc_acc fa, fc_subject fs");
        sql.append(" where fc.account = fa.blnctr_acc and fa.subject_id = fs.id and fa.acc_state <> 'D'");
        //添加资金池过滤条件
        if(poolFlag != null){
            if(poolFlag){
                sql.append("and fa.pool_sign='1' ");
            }else{
                sql.append("and fa.pool_sign='0' ");
            }
        }
        sql.append(joinSql);
        return this.listBySQLAliasToBean(sql.toString(),params,FcAccBalanceQueryVO.class);
    }*/
    /**
     * 上层没被调用，注释
     * @param dataTablesInput
     * @param joinSql
     * @param params
     * @return
     */
    /*@SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForPool(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.account as account, fa.acc_name as accName, fa.corp_id as corpId, fa.subject_id as subjectId,fs.subject_name as subjectName,");
        sql.append(" fc.cur_code as curId, fc.online_balance as onlineBalance, fc.check_balance as checkBalance, (fc.check_balance-fc.freeze_money) as availableBalance,");
        sql.append(" fa.overdraft_money as overdraftMoney, fa.acc_open_time as accOpenTime, fa.deadline_date as deadlineDate,");
        sql.append(" fa.interest_rate as interestRate");
        sql.append(" from fc_online_ledger fc, fc_acc fa, fc_subject_pool fs");
        sql.append(" where fc.account = fa.blnctr_acc and fa.subject_id = fs.id and fa.pool_sign='1'");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccBalanceQueryVO.class, null, false, params);
    }*/
    /**
     * 获取单个账号的余额信息
     * @param account
     * @return
     * 上层没有被调用，注释
     */
    /*public FcAccBalanceQueryVO getFcAccBalByAccount(String account) {
        StringBuilder sql = new StringBuilder();
        sql.append("select fc.account as account, fa.acc_name as accName, fa.corp_id as corpId, fa.subject_id as subjectId,fs.subject_name as subjectName,");
        sql.append(" fc.cur_code as curId, fc.online_balance as onlineBalance, fc.check_balance as checkBalance, (fc.check_balance-fc.freeze_money) as availableBalance,");
        sql.append(" fa.overdraft_money as overdraftMoney,fa.acc_type as accType, fa.acc_open_time as accOpenTime, fa.deadline_date as deadlineDate,fa.relation_acc as relationAcc,");
        sql.append(" fa.interest_rate as interestRate");
        sql.append(" from fc_online_ledger fc, fc_acc fa, fc_subject fs");
        sql.append(" where fc.account = fa.blnctr_acc and fa.subject_id = fs.id");
        sql.append(" and fc.account=:account");
        HashMap<String, String> params = new HashMap<>();
        params.put("account", account);
        List<FcAccBalanceQueryVO> list = this.listBySQLAliasToBean(sql.toString(), params, FcAccBalanceQueryVO.class);
        if(!EmptyUtils.isEmptyList(list)){
            return list.get(0);
        }
        return null;
    }*/
}
