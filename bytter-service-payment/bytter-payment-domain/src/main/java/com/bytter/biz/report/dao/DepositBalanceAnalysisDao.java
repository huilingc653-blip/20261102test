package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.DepositBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 分析系统当前存款余额与上月的差异、与年初的差异。
 *
 * @author qinl
 * @Description 存款余额对比分析Dao
 * @date 2020.01.19
 */
@Repository
public class DepositBalanceAnalysisDao extends BaseDao<DepositBalanceAnalysisVO, String> {
    /**
     * 根据时间获取内部账号余额信息-单位统计维度
     * @param date 时间
     * @param curId 币别
     * @return 内部账号余额列表
     */
    public List<DepositBalanceAnalysisVO> selectCorpListForInner(String date, String curId,String[] ids){
        String[] curIds = curId.split(",");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id as innerId,a.account,a.balance_date,a.cur_id,a.acc_name,a.balance as balance,b.corp_id as corpId,c.name as corpName");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" ,(SELECT account, max( balance_date ) AS balance_date FROM fc_acc_balance ");
        sql.append(" where bt_to_char ( balance_date , 'YYYY-MM-DD' ) <= :balanceDate ");
        sql.append(" GROUP BY account ) t");
        sql.append(" WHERE 1=1");
        sql.append(" AND t.account = a.account");
        sql.append(" AND t.balance_date = a.balance_date");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("balanceDate",date);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,DepositBalanceAnalysisVO.class);
    }
    /**
     * 根据时间获取银行账号余额信息-单位统计维度
     * @param date 时间
     * @param curId 币别
     * @return 银行余额列表
     */
    public List<DepositBalanceAnalysisVO> selectCorpListForBank(String date, String curId,String[] ids){
        String[] curIds = curId.split(",");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id as bankId,a.bank_acc,a.corp_id as corpId,b.acc_name,c.name as corpName,a.cur_id,a.bal_date,a.bal as balance");
        sql.append(" FROM BIS_ACC_BAL a");
        sql.append(" LEFT JOIN BT_BANK_ACC b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN sys_corp c ON a.corp_id = c.id ");
        sql.append(" ,(SELECT bank_acc, max( bal_date ) AS bal_date FROM BIS_ACC_BAL ");
        sql.append(" where bt_to_char ( bal_date , 'YYYY-MM-DD' ) <= :balDate ");
        sql.append(" GROUP BY bank_acc ) t");
        sql.append(" WHERE 1=1");
        sql.append(" AND t.bank_acc = a.bank_acc");
        sql.append(" AND t.bal_date = a.bal_date");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        sql.append(" AND b.status = '95' ");
        sql.append(" AND b.valid_sign = '1' ");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "a.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("balDate",date);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,DepositBalanceAnalysisVO.class);
    }
    /**
     * 根据时间获取内部账号余额信息-银行类别统计维度
     * @param date 时间
     * @param curId 币别
     * @return 内部账号余额列表
     */
    public List<DepositBalanceAnalysisVO> selectTypeListForInner(String date, String curId,String[] ids){
        String[] curIds = curId.split(",");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id as innerId,a.account,a.balance_date,a.cur_id,a.acc_name,a.balance as balance,b.corp_id as corpId,c.name as corpName,c.net_id as netId,d.name as netName");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" LEFT JOIN sys_corp d ON c.net_id = d.id ");
        sql.append(" ,(SELECT account, max( balance_date ) AS balance_date FROM fc_acc_balance ");
        sql.append(" where bt_to_char ( balance_date , 'YYYY-MM-DD' ) <= :balanceDate ");
        sql.append(" GROUP BY account ) t");
        sql.append(" WHERE 1=1");
        sql.append(" AND t.account = a.account");
        sql.append(" AND t.balance_date = a.balance_date");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("balanceDate",date);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,DepositBalanceAnalysisVO.class);
    }
    /**
     * 根据时间获取银行账号余额信息-银行类别统计维度
     * @param date 时间
     * @param curId 币别
     * @return 银行余额列表
     */
    public List<DepositBalanceAnalysisVO> selectTypeListForBank(String date, String curId,String[] ids){
        String[] curIds = curId.split(",");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" a.id as bankId,a.bank_acc,a.corp_id as corpId,b.acc_name,c.type_name as corpName,a.cur_id,a.bal_date,a.bal as balance");
        sql.append(" FROM BIS_ACC_BAL a");
        sql.append(" LEFT JOIN BT_BANK_ACC b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN BT_BANK_TYPE c ON b.bank_type_id = c.id ");
        sql.append(" ,(SELECT bank_acc, max( bal_date ) AS bal_date FROM BIS_ACC_BAL ");
        sql.append(" where bt_to_char ( bal_date , 'YYYY-MM-DD' ) <= :balDate ");
        sql.append(" GROUP BY bank_acc ) t");
        sql.append(" WHERE 1=1");
        sql.append(" AND t.bank_acc = a.bank_acc");
        sql.append(" AND t.bal_date = a.bal_date");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        sql.append(" AND b.status = '95' ");
        sql.append(" AND b.valid_sign = '1' ");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "a.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("balDate",date);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,DepositBalanceAnalysisVO.class);
    }
    /**
     * 根据ID查询详细余额-银行账号
     * @param ids 余额id
     * @return 银行余额列表
     */
    public List<DepositBalanceAnalysisVO> selectDetailsForBank(String[] ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT bank_acc as corpName,bal as balance,cur_id FROM bis_acc_bal");
        sql.append(" WHERE").append(SqlUtil.getInSql(ids, "id", true));
        return listBySQLAliasToBean(sql.toString(),null,DepositBalanceAnalysisVO.class);
    }
    /**
     * 根据ID查询详细余额-内部账号
     * @param ids 余额id
     * @return 内部余额列表
     */
    public List<DepositBalanceAnalysisVO> selectDetailsForInner(String[] ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT account as corpName,balance,cur_id FROM fc_acc_balance");
        sql.append(" WHERE").append(SqlUtil.getInSql(ids, "id", true));
        return listBySQLAliasToBean(sql.toString(),null,DepositBalanceAnalysisVO.class);
    }
}
