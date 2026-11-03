package com.bytter.pricing.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.pricing.entity.DepositRateType;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: DepositIRateTypeDao
 * @ProjectName T2_IDEA
 * @Description: 利率类型数据访问层
 * @date 2019/5/22 14:23
 */
@Repository
public class DepositRateTypeDao extends BaseDao<DepositRateType,String> {
    /**
     * 根据类型编号获取利率类型
     * @param code
     * @return
     */
    public DepositRateType findPeriodByCode(String code){
        return getUniqueBy("code", code);
    }
    /**
     * 获取所有存款利率类型
     */
    public List<DepositRateType> getAllRateType(){
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.code as code,t.period_name as name,t.loan_class as depositClass,t.deposit_Acc_Sign as depositAccSign   ")
                .append(" from  TB_CREDIT_PERIOD t where t.INTEREST_RATE_TYPE=1 ");
        return this.listBySQLAliasToBean(sql.toString(),null, DepositRateType.class);
    }
    /**
     * 是否被存款利率引用
     */
    public boolean isReferedRate(DepositRateType rateType){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<String, Object>();
        sql.append("select * from TB_Loan_Rate_Maintence r where r.credit_period_id = :id ");//and r.status = :status
        params.put("id", rateType.getId());
//        params.put("status", PricingConstant.STATUS_PASS);
        List result = listBySQL(sql.toString(), params);
        if(EmptyUtils.isEmptyList(result)){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 根据存款类别获取存款利率类型
     */
    public List<DepositRateType> getRateTypeByDepositClass(String depositClass){
        StringBuilder sql=new StringBuilder();
        Map<String,String> params=new HashMap<String,String>();
        sql.append("select t.id as id,t.code as code,t.period_name as name,t.loan_class as depositClass  ")
                .append(" from  TB_CREDIT_PERIOD t where t.INTEREST_RATE_TYPE=1 and t.loan_class=:depositClass ");
        params.put("depositClass",depositClass);
        return this.listBySQLAliasToBean(sql.toString(),params, DepositRateType.class);
    }
    /**
     * 根据存款类别和存款账户标识获取存款利率类型
     * @param depositClass 存款类别
     * @param depositAccSign 存款标识
     * @return 存款利率类型集合
     */
    public List<DepositRateType> getRateTypeByParams(String depositClass,String depositAccSign){
        StringBuilder sql=new StringBuilder();
        Map<String,String> params=new HashMap<String,String>();
        sql.append("select t.id as id,t.code as code,t.period_name as name,t.loan_class as depositClass,  ")
                .append(" t.deposit_Acc_Sign as depositAccSign,t.period as period,t.period_Cycle as periodCycle ")
                .append(" from  TB_CREDIT_PERIOD t   ")
                .append(" INNER JOIN tb_loan_rate_maintence m ON t.id = m.credit_period_id ")
                .append(" where t.INTEREST_RATE_TYPE=1 AND m.status='2' ");
        if(EmptyUtils.isNotEmpty(depositClass)){
            sql.append(" and t.loan_class=:depositClass");
            params.put("depositClass",depositClass);
        }
        if(EmptyUtils.isNotEmpty(depositAccSign)){
            sql.append(" and t.deposit_Acc_Sign=:depositAccSign ");
            params.put("depositAccSign",depositAccSign);
        }
        return this.listBySQLAliasToBean(sql.toString(),params, DepositRateType.class);
    }



    /**
     * 根据存款类别和存款账户标识获取存款利率类型
     * @param depositClass 存款类别
     * @param depositAccSign 存款标识
     * @return 存款利率类型集合
     */
    public List<DepositRateType> getRateTypeByParamsNotJoinCur(String depositClass,String depositAccSign){
        StringBuilder sql=new StringBuilder();
        Map<String,String> params=new HashMap<String,String>();
        sql.append("select t.id as id,t.code as code,t.period_name as name,t.loan_class as depositClass,  ")
                .append(" t.deposit_Acc_Sign as depositAccSign,t.period as period,t.period_Cycle as periodCycle ")
                .append(" from  TB_CREDIT_PERIOD t   ")
                .append(" where t.INTEREST_RATE_TYPE=1 ");
        if(EmptyUtils.isNotEmpty(depositClass)){
            sql.append(" and t.loan_class=:depositClass");
            params.put("depositClass",depositClass);
        }
        if(EmptyUtils.isNotEmpty(depositAccSign)){
            sql.append(" and t.deposit_Acc_Sign=:depositAccSign ");
            params.put("depositAccSign",depositAccSign);
        }
        return this.listBySQLAliasToBean(sql.toString(),params, DepositRateType.class);
    }
}
