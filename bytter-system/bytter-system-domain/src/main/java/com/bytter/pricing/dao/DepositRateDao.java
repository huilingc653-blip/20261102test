package com.bytter.pricing.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.pricing.constant.PricingConstant;
import com.bytter.system.pricing.dto.DepositRateDTO;
import com.bytter.pricing.entity.DepositRate;
import com.bytter.pricing.vo.DepositRateVO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: DepositRateDao
 * @ProjectName T2_IDEA
 * @Description: 存款利率维护数据访问层
 * @date 2019/5/23 9:44
 */
@Repository
public class DepositRateDao extends BaseDao<DepositRate,String> {
    /**
     * 查询语句
     * @param sql
     */
    private void querySql(StringBuilder sql){
        sql.append(" select m.id, m.credit_period_id  as depositRateType,t.code  as rateTypeCode,t.period_name as rateTypeName,")
                .append(" m.time_of_take_effect as timeOfTakeEffect,t.loan_class  as depositClass,t.period  as period,")
                .append(" t.period_cycle as periodCycle,m.rate as rate,m.time_of_last_modifier as timeOfLastModifier,")
                .append(" m.status as status,m.money_type  as moneyType, m.is_base_loan_rate as isbaseloanrate,")
                .append(" m.remark  as remark,m.check_op as checkOp,m.returnde_scribe,")
                .append(" m.create_by  as createBy, t.interest_rate_type as interestRateType,")
                .append(" case when  m.reference_rate_type is null then '0' else m.reference_rate_type end as referenceRateType,")
                .append(" m.rate_up_limit  as rateUpLimit,")
                .append(" m.rate_down_limit  as  rateDownLimit, t.deposit_acc_sign  as depositAccSign, ")
                .append(" bc.cur_name as curName ")
                .append(" from tb_loan_rate_maintence m inner join tb_credit_period t on t.id = m.credit_period_id")
                .append(" left join bt_currency bc on bc.id = m.money_type ")
                .append( "  where t.interest_rate_type='1' ");
    }
    /**
     * 获取利率列表数据
     * @param input
     * @param businessSql
     * @return
     */
    public DataTablesOutput getDataTables(DataTablesInput input,String businessSql){
        StringBuilder sql=new StringBuilder();
        this.querySql(sql);
        sql.append(businessSql);
        return getDynamicBySQL(input, sql.toString(), DepositRateVO.class, null, false);
    }
    /**
     * 根据id获取存款利率数据
     */
    public DepositRateVO getDepositRateById(String id){
        StringBuilder sql=new StringBuilder();
        Map<String,String> map=new HashMap<String,String>();
        this.querySql(sql);
        sql.append( " and m.id=:id ");
        map.put("id",id);
        return this.getBySQLAliasToBean(sql.toString(), map, DepositRateVO.class);
    }
    /**
     * 根据条件获取存款利率数据
     */
    public List<DepositRateVO> getDepositRateByParams(DepositRateVO depositRateVO){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> map=new HashMap<String,Object>();
        this.querySql(sql);
        if(StringUtils.isNotEmpty(depositRateVO.getDepositRateType())){
            sql.append(" and m.credit_period_id =:depositRateType ");
            map.put("depositRateType",depositRateVO.getDepositRateType());
        }
        if(StringUtils.isNotEmpty(depositRateVO.getMoneyType())){
            sql.append(" and m.money_Type=:moneyType ");
            map.put("moneyType",depositRateVO.getMoneyType());
        }
        if(StringUtils.isNotEmpty(depositRateVO.getTimeOfTakeEffect())){
            sql.append(" and m.time_of_take_effect =:timeOfTakeEffect ");
            map.put("timeOfTakeEffect",depositRateVO.getTimeOfTakeEffect());
        }
//        if(StringUtils.isNotEmpty(depositRateVO.getStatus())){
//            sql.append(" and m.status <>:status ");
//            map.put("status",PricingConstant.SAVE_TEMP);
//        }
        if(StringUtils.isNotEmpty(depositRateVO.getId())){
            sql.append(" and m.id not in(:id ) ");
            map.put("id",depositRateVO.getId());
        }
        return this.listBySQLAliasToBean(sql.toString(),map,DepositRateVO.class);
    }
    /**
     * 获取同类型编号、同币别的科目利率维护，按生效日期升序排序
     */
    public List<DepositRateVO> getDepositRates(DepositRateVO depositRateVO){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> map=new HashMap<String,Object>();
        this.querySql(sql);
        if(StringUtils.isNotEmpty(depositRateVO.getDepositRateType())){
            sql.append(" and m.credit_period_id =:depositRateType ");
            map.put("depositRateType",depositRateVO.getDepositRateType());
        }
        if(StringUtils.isNotEmpty(depositRateVO.getMoneyType())){
            sql.append(" and m.money_Type=:moneyType ");
            map.put("moneyType",depositRateVO.getMoneyType());
        }
        if(StringUtils.isNotEmpty(depositRateVO.getId())){
            sql.append(" and m.id not in(:id ) ");
            map.put("id",depositRateVO.getId());
        }
        sql.append(" and m.status in(1,2) order by m.time_of_take_effect asc ");//获取待复核和复核通过的数据
        return this.listBySQLAliasToBean(sql.toString(),map,DepositRateVO.class);
    }
    /**
     * 根据存款账户类别及开户日期、币别获取存款利率详情
     */
    public List<DepositRateDTO> getDepositRateByDepositClass(String depositClass, Date time, String curId){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> map=new HashMap<String,Object>();
        this.querySql(sql);
        sql.append(" and t.loan_class=:depositClass and m.status=:status ")
           .append(" and m.time_of_take_effect <=:time")//找开户日期之前的数据
           .append(" and m.money_type=:curId ")
           .append(" order by m.time_of_take_effect desc");
        map.put("depositClass",depositClass);
        map.put("status", PricingConstant.STATUS_PASS);
        map.put("time",time);
        map.put("curId",curId);
        return this.listBySQLAliasToBean(sql.toString(),map,DepositRateDTO.class);
    }
    /**
     * 根据条件查询复核通过的存款利率
     */
    public List<DepositRateDTO> getDepositRateByParams(DepositRateDTO depositRateDTO){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> map=new HashMap<String,Object>();
        this.querySql(sql);
        sql.append(" and m.status=2 and m.TIME_OF_TAKE_EFFECT <=:date ");
        if(null!=depositRateDTO.getTimeOfTakeEffect()){//生效时间
            map.put("date",depositRateDTO.getTimeOfTakeEffect());
        }else{
            map.put("date", DateUtils.getDate());
        }
        if(EmptyUtils.isNotEmpty(depositRateDTO.getReferenceRateType())){//利率种类
            sql.append(" and bt_nvl(m.REFERENCE_RATE_TYPE,0)=:referenceRateType ");
            map.put("referenceRateType",depositRateDTO.getReferenceRateType());
        }else{
            sql.append(" and bt_nvl(m.REFERENCE_RATE_TYPE,0)<>1 ");//存量数据默认为基准利率
        }
        if(EmptyUtils.isNotEmpty(depositRateDTO.getDepositClass())){//存款种类
            sql.append(" and t.loan_class=:depositClass ");
            String depositClass="";
            if(depositRateDTO.getDepositClass().equals("01")||depositRateDTO.getDepositClass().equals("02")){//数据库存储与定义类别不同，需进行类别对应转换
                depositClass="04";//活期
            }else if(depositRateDTO.getDepositClass().equals("08")){
                depositClass="06";//通知
            }else if(depositRateDTO.getDepositClass().equals("05")||depositRateDTO.getDepositClass().equals("06")){
                depositClass="05";//定期
            }
            map.put("depositClass",depositClass);
        }
        if(EmptyUtils.isNotEmpty(depositRateDTO.getMoneyType())){//币别
            sql.append(" and m.money_type=:moneyType ");
            map.put("moneyType",depositRateDTO.getMoneyType());
        }
        if(EmptyUtils.isNotEmpty(depositRateDTO.getDepositRateType())){//存款类别
            sql.append(" and m.credit_period_id=:depositRateType ");
            map.put("depositRateType",depositRateDTO.getDepositRateType());
        }
        if(null!=depositRateDTO.getPeriod()){
            sql.append(" and t.period=:period ");
            map.put("period",depositRateDTO.getPeriod());
        }
        if(null!=depositRateDTO.getPeriodCycle()){
            sql.append(" and t.period_cycle=:periodCycle  ");
            map.put("periodCycle",depositRateDTO.getPeriodCycle());
        }
        sql.append(" order by m.TIME_OF_TAKE_EFFECT desc");
        return this.listBySQLAliasToBean(sql.toString(),map,DepositRateDTO.class);
    }
}
