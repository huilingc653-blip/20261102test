package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisChartVO;
import com.bytter.biz.report.vo.AverageDailyBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 日均存款分析Dao
 *
 * @author qinl
 * @description 日均存款分析Dao
 * @date 2020.02.24
 */
@Repository
public class AverageDailyBalanceAnalysisDao extends BaseDao<AverageDailyBalanceAnalysisVO, String> {
    /**
     * 查询银行余额-单位统计维度
     * @param input 输入条件
     * @return 银行余额列表
     */
    public List<AverageDailyBalanceAnalysisVO> selectCorpListForBank(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//前置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//后置时间
        String accType = (String) input.getFormdata().get("accType");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" c.id as corpId,c.name AS corpName,a.bank_acc,b.acc_name,b.acc_type,a.cur_id,a.bal_date as balDate,a.bal AS balance ");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" LEFT JOIN bt_bank_acc b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN sys_corp c ON a.corp_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.status = '95'");
        sql.append(" AND b.valid_sign = '1'");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        if(StringUtils.isNotBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND").append(SqlUtil.getInSql(ids, "c.id", true));
        sql.append(" ORDER BY b.acc_name,a.bal_date DESC");
        Map<String,String> map = new HashMap<>();
        map.put("curId",curId);
        map.put("before",beforeDate);
        map.put("after",afterDate);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisVO.class);
    }
    /**
     * 查询银行余额-银行类型统计维度
     * @param input 输入条件
     * @return 银行余额列表
     */
    public List<AverageDailyBalanceAnalysisVO> selectBankTypeListForBank(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//前置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//后置时间
        String accType = (String) input.getFormdata().get("accType");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" c.id as corpId,c.type_name AS corpName,a.bank_acc,b.acc_name,b.acc_type,a.cur_id,a.bal_date as balDate,a.bal AS balance ");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" LEFT JOIN bt_bank_acc b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN bt_bank_type c ON b.bank_type_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.status = '95'");
        sql.append(" AND b.valid_sign = '1'");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        if(StringUtils.isNotBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND").append(SqlUtil.getInSql(ids, "a.corp_id", true));
        sql.append(" ORDER BY b.acc_name,a.bal_date DESC");
        Map<String,String> map = new HashMap<>();
        map.put("curId",curId);
        map.put("after",afterDate);
        map.put("before",beforeDate);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisVO.class);
    }
    public List<AverageDailyBalanceAnalysisChartVO> selectAverageBankTypeListForBank(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String corpName = (String) input.getFormdata().get("corpName");//单位名称
        String accType = (String) input.getFormdata().get("accType");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" c.id as corpId,c.type_name AS corpName,a.bank_acc,b.acc_name,b.acc_type as bankAccType,a.cur_id,a.bal_date as balDate,a.bal AS averageBalance ");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" LEFT JOIN bt_bank_acc b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN bt_bank_type c ON b.bank_type_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.status = '95'");
        sql.append(" AND b.valid_sign = '1'");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND c.type_name = :corpName");
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        if(StringUtils.isNotBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND").append(SqlUtil.getInSql(ids, "a.corp_id", true));
        sql.append(" ORDER BY b.acc_name,a.bal_date DESC");
        Map<String,String> map = new HashMap<>();
        map.put("curId",curId);
        map.put("after",afterDate);
        map.put("before",beforeDate);
        map.put("corpName",corpName);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisChartVO.class);
    }
    /**
     * 查询内部账号余额-单位统计维度
     * @param input 输入条件
     * @return 内部账号余额列表
     */
    public List<AverageDailyBalanceAnalysisVO> selectCorpListForInner(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String accType = (String) input.getFormdata().get("accTypeInner");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" b.corp_id as corpId,c.name AS corpName,a.account,a.acc_name,b.acc_type,a.cur_id,a.balance_date as balDate,a.balance AS balance");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        if(StringUtils.isNotBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("after",afterDate);
        map.put("before",beforeDate);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisVO.class);
    }
    /**
     * 查询内部账号余额-银行类型维度
     * @param input 输入条件
     * @return 内部账号余额列表
     */
    public List<AverageDailyBalanceAnalysisVO> selectBankTypeListForInner(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String accType = (String) input.getFormdata().get("accTypeInner");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" b.corp_id as corpId,d.name as corpName,a.account,a.acc_name,b.acc_type,a.cur_id,a.balance_date as balDate,a.balance AS balance");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" LEFT JOIN sys_corp d ON c.net_id = d.id  ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        if(StringUtils.isBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("after",afterDate);
        map.put("before",beforeDate);
        map.put("curId",curId);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisVO.class);
    }
    public List<AverageDailyBalanceAnalysisChartVO> selectAverageBankTypeListForInner(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String corpName = (String) input.getFormdata().get("corpName");//单位名称
        String accType = (String) input.getFormdata().get("accTypeInner");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" b.corp_id as corpId,d.name as corpName,a.account,a.acc_name,b.acc_type as innerAccType,a.cur_id,a.balance_date as balDate,a.balance AS averageBalance");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" LEFT JOIN sys_corp d ON c.net_id = d.id  ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND d.name = :corpName");
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        if(StringUtils.isBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "b.corp_id", true));
        Map<String,String> map = new HashMap<>();
        map.put("after",afterDate);
        map.put("before",beforeDate);
        map.put("curId",curId);
        map.put("corpName",corpName);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisChartVO.class);
    }
    /**
     * 查询银行每天的余额
     * @return 银行余额列表
     */
    public List<AverageDailyBalanceAnalysisChartVO> selectDailyListForBank(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String corpName = (String) input.getFormdata().get("corpName");//单位名称
        String accType = (String) input.getFormdata().get("accType");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" c.id as corpId,c.name AS corpName,a.bank_acc,b.acc_name,b.acc_type as bankAccType,a.cur_id,a.bal_date,a.bal AS averageBalance ");
        sql.append(" FROM bis_acc_bal a");
        sql.append(" LEFT JOIN bt_bank_acc b ON a.bank_acc = b.bank_acc ");
        sql.append(" LEFT JOIN sys_corp c ON a.corp_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND b.status = '95'");
        sql.append(" AND b.valid_sign = '1'");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.bal_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND c.name = :corpName");
        sql.append(" AND b.acc_type not in");
        sql.append("('").append(BtBankAccConstant.ACCTYPE_03).append("',");
        sql.append("'").append(BtBankAccConstant.ACCTYPE_09).append("')");
        if(StringUtils.isNotBlank(accType)){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND").append(SqlUtil.getInSql(ids, "c.id", true));
        sql.append(" ORDER BY b.acc_name,a.bal_date DESC");
        Map<String,String> map = new HashMap<>();
        map.put("corpName",corpName);
        map.put("curId",curId);
        map.put("before",beforeDate);
        map.put("after",afterDate);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisChartVO.class);
    }
    /**
     * 查询内部账号每天的余额
     * @return 内部账号余额列表
     */
    public List<AverageDailyBalanceAnalysisChartVO> selectDailyListForInner(DataTablesInput input){
        String corpIds = (String) input.getFormdata().get("corpIds");//单位
        String[] ids = StringUtils.split(corpIds, ",");
        String curId = (String) input.getFormdata().get("curId");//币别
        String[] curIds = curId.split(",");
        String afterDate = (String) input.getFormdata().get("afterDate");//后置时间
        String beforeDate = (String) input.getFormdata().get("beforeDate");//前置时间
        String corpName = (String) input.getFormdata().get("corpName");//单位名称
        String accType = (String) input.getFormdata().get("accType");//账户类别
        String[] accTypes = null;
        String accTypeSql = "";
        if(StringUtils.isNotBlank(accType)){
            accTypes = accType.split(CommonConstant.COMMA_SPLIT);
            for(int i=0;i<accTypes.length;i++){
                if(i==0){
                    accTypeSql = "'"+accTypes[i]+"'";
                }else{
                    accTypeSql += ",'"+accTypes[i]+"'";
                }
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" b.corp_id as corpId,c.name AS corpName,a.account,a.acc_name,b.acc_type as innerAccType,a.cur_id,a.balance_date as balDate,a.balance AS averageBalance");
        sql.append(" FROM fc_acc_balance a");
        sql.append(" LEFT JOIN fc_acc b ON a.account = b.blnctr_acc ");
        sql.append(" LEFT JOIN sys_corp c ON b.corp_id = c.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND").append(SqlUtil.getInSql(curIds, "a.cur_id", true));
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) >= :before");
        sql.append(" AND bt_to_char ( a.balance_date, 'YYYY-MM-DD' ) <= :after");
        sql.append(" AND c.name = :corpName");
        sql.append(" AND b.acc_type in");
        sql.append("('").append(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEMAND).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BUSINESS_BEGULAR).append("',");
        sql.append("'").append(InnerAccountConstant.FCACC_BANK_DEPOSIT).append("')");
        if(accType!=null){
            sql.append(" AND b.acc_type in("+accTypeSql+")");
        }
        sql.append(" AND b.acc_state in");
        sql.append("('").append(InnerAccountConstant.ACC_STATE_NORMAL).append("',");
        sql.append("'").append(InnerAccountConstant.ACC_STATE_EDIT).append("')");
        sql.append(" AND").append(SqlUtil.getInSql(ids, "c.id", true));
        Map<String,String> map = new HashMap<>();
        map.put("corpName",corpName);
        map.put("curId",curId);
        map.put("before",beforeDate);
        map.put("after",afterDate);
        return listBySQLAliasToBean(sql.toString(),map,AverageDailyBalanceAnalysisChartVO.class);
    }
}
