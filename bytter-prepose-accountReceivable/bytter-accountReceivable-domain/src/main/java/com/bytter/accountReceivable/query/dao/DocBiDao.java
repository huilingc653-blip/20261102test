package com.bytter.accountReceivable.query.dao;

import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.query.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 应收账款首页查询dao
 */
@Repository
public class DocBiDao extends BaseDao<DocManage, String> {

    //查外汇牌价中间价
    public List<FexRateVo> queryMiddleRate(String tenantId){
        StringBuilder sql = new StringBuilder();
        sql.append(" with cur_type as(");
        sql.append(" select	r.cur,r.refer_Cur,max(e.create_date) create_date, r.tenant_id");
        sql.append(" from fex_rate_info r");
        sql.append(" left join fex_rate_exc e on r.rate_Exc_Id = e.id");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" where t.rate_Interface_Type = '1' and r.create_by = 'timer'");
        sql.append(" group by r.cur,r.refer_Cur, r.tenant_id)");
        sql.append(" select	distinct r.refer_Cur curId,r.middle_Rate / 100 as middleRate");
        sql.append(" from fex_rate_info r");
        sql.append(" left join fex_rate_exc e on r.rate_Exc_Id = e.id");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" left join bt_currency c on	r.cur = c.id");
        sql.append(" join cur_type ct on r.cur = ct.cur	and r.refer_Cur = ct.refer_Cur and e.create_date = ct.create_date");
        sql.append(" where 1 = 1 and c.ENGLISH_CODE = 'CNY'	and t.rate_Interface_Type = '1'");
        return listBySQLAliasToBean(sql.toString(), null, FexRateVo.class);
    };

    //查应收余额，合同金额，逾期金额,分币种
    public List<ArAnalysisVo> queryBalanceReceivable(String corpInSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" select");
        sql.append(" dm.cur_id as curId,");
        sql.append(" sum(case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal");
        sql.append(" else dm.RECEIVABLE_MONEY");
        sql.append(" end) as writeOffBal,");
        sql.append(" sum(BT_NVL_NUM(dm.contract_money,0)) as contractMoney,");
        sql.append(" sum(case when BT_DATEDIFF(dm.END_DATE) < 0 and dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal");
        sql.append(" when BT_DATEDIFF(dm.END_DATE) < 0 and dm.WRITE_OFF_bal is null then dm.RECEIVABLE_MONEY");
        sql.append(" else 0 end) as overdueAmount");
        sql.append(" from doc_manage dm where 1=1 and dm.status = 95");
        if(StringUtils.isNotEmpty(corpInSql)){
            sql.append(" and").append(corpInSql);
        }
        sql.append(" group by dm.cur_id");
        return listBySQLAliasToBean(sql.toString(), null, ArAnalysisVo.class);
    }

    /**
     * 平均应收账款余额
     * 全年每月月末应收款余额之和 / 12,若不足一年，按实际月份计算，
     */
    public List<ArTurnoverVo> queryAverageArBalance(ReceivableIndexQueryVo queryVo, String corpInSql, String tenantId){
        StringBuilder sql = new StringBuilder();
        sql.append(" select dm.cur_id as curId,");
        sql.append(" sum(case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end) writeOffBal,");
        sql.append(" sum(dm.RECEIVABLE_MONEY) receivableMoney");
        sql.append(" from doc_manage dm where 1=1 and dm.status = 95");
        if(StringUtils.isNotEmpty(corpInSql)){
            sql.append(" and ").append(corpInSql);
        }
        sql.append(" and dm.RECEIPT_DATE >= :startDate and dm.RECEIPT_DATE < :endDate");
        sql.append(" group by dm.cur_id");
        Map<String, Object> param = getParam(queryVo);
        param.put("tenantId", tenantId);
        return listBySQLAliasToBean(sql.toString(), param, ArTurnoverVo.class);
    }

    /**
     * 应收趋势分析
     *　按月统计应收余额及核销金额，分币种
     */
    public List<ArTrendVo> queryArTrendAnalysis(String corpInSql, String monthInSql){
        StringBuilder sql = new StringBuilder();
        sql.append(" with detail as(");
        sql.append(" select  dm.cur_id curId, dm.tenant_id,");
        sql.append(" BT_TO_CHAR(dm.RECEIPT_DATE, 'yyyy-MM') month,");
        sql.append(" sum(case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end) as writeOffBal,");
        sql.append(" sum(BT_NVL_NUM(dm.CONTRACT_Money,0)) contractMoney,");
        sql.append(" sum(dm.RECEIVABLE_MONEY) receivableMoney,");
        sql.append(" (sum(dm.RECEIVABLE_MONEY) - sum(case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end)) as writeOffAmount");
        sql.append(" from  doc_manage dm ");
        sql.append(" where 1 = 1 and dm.STATUS = 95");
        if (StringUtils.isNotEmpty(corpInSql)) {
            sql.append(" and").append(corpInSql);
        }
        sql.append(" group by BT_TO_CHAR(dm.RECEIPT_DATE, 'yyyy-MM'),dm.cur_id, dm.tenant_id)");
        sql.append(" select d1.curId,");
        sql.append(" d1.month,");
        sql.append(" sum(d2.writeOffBal) writeOffBal,");
        sql.append(" sum(d2.contractMoney) contractMoney,");
        sql.append(" d1.receivableMoney,");
        sql.append(" d1.writeOffAmount");
        sql.append(" from detail d1");
        sql.append(" join detail d2 on d2.month <= d1.month and d2.curId = d1.curId where 1 = 1");
        if (StringUtils.isNotEmpty(monthInSql)) {
            sql.append(" and").append(monthInSql);
        }
        sql.append(" group by d1.month,d1.curId");
        return listBySQLAliasToBean(sql.toString(), null, ArTrendVo.class);
    }

    /**
     * 应收款占比分析
     * 按客商名称统计合同金额，应收款余额，应收账款比例，客商信用等级，逾期金额
     */
    public List<ArRateVo> queryArRatio(ReceivableIndexQueryVo queryVo, String corpInSql, String tenantId){
        String category = queryVo.getCategory();
        if(StringUtils.isNotEmpty(category)){
            String sql;
            sql = "byWriteOffBal".equals(category) ? getArRatioOfWriteOffBal(corpInSql) : getOverDueAmount(corpInSql);
            Map<String, Object> param = new HashMap<>();
            param.put("tenantId", tenantId);
            return listBySQLAliasToBean(sql, param, ArRateVo.class);
        }
        return null;
    }

    private String getArRatioOfWriteOffBal(String corpInSql){
        StringBuilder sql = new StringBuilder();
        sql.append(getMiddleRate());
        sql.append(", detail as( ");
        sql.append(" select dm.cur_id curId,");
        sql.append(" dm.EXTERNAL_NAME externalName,");
        sql.append(" sec.CREDIT_RATING creditRating,");
        sql.append(" sum(BT_NVL_NUM(dm.WRITE_OFF_bal,BT_NVL_NUM(dm.RECEIVABLE_MONEY,0))) as writeOffBal,");
        sql.append(" sum(BT_NVL_NUM(dm.CONTRACT_Money, 0) ) contractMoney");
        sql.append(" from doc_manage dm");
        sql.append(" left join sys_external_corp sec on dm.EXTERNAL_CODE = sec.code");
        sql.append(" where 1 = 1 and dm.status = 95");
        if (StringUtils.isNotEmpty(corpInSql)) {
            sql.append(" and" + corpInSql);
        }
        sql.append(" group by dm.EXTERNAL_NAME, dm.cur_id)");
        sql.append(",detail_sum as( select tt.externalName,tt.creditRating,sum(tt.writeOffBal) writeOffBal,sum(tt.contractMoney) contractMoney from(");
        sql.append(" select t.curid,t.externalName,t.creditRating,");
        sql.append(" case when s.middleRate is not null then t.writeOffBal * s.middleRate else t.writeOffBal end writeOffBal,");
        sql.append(" case when s.middleRate is not null then t.contractMoney * s.middleRate else t.contractMoney end contractMoney");
        sql.append(" from detail t left join cur_rate s on t.curId = s.curId )");
        sql.append(" tt group by tt.externalName,tt.creditRating )");
        //比例改为跟合同金额的比值
        //sql.append(", total as(select sum(writeOffBal) writeOffBalTotal from detail_sum)");
        sql.append(" select t.externalName, t.creditRating, t.writeOffBal, t.contractMoney");
        //sql.append(" concat(round((t.writeOffBal/ case when t.contractMoney = 0 then 1 else t.contractMoney end) * 100,2), '%') rate");
        sql.append(" from detail_sum t");
        sql.append(" order by t.writeOffBal desc limit 10");
        return sql.toString();
    }

    private String getOverDueAmount(String corpInSql){
        StringBuilder sql = new StringBuilder();
        sql.append(getMiddleRate());
        sql.append(", detail as( ");
        sql.append("select dm.cur_id curId,");
        sql.append(" dm.EXTERNAL_NAME externalName,");
        sql.append(" sec.CREDIT_RATING creditRating,");
        sql.append(" sum(case when BT_DATEDIFF(dm.END_DATE) < 0 then (BT_NVL_NUM(dm.WRITE_OFF_bal,BT_NVL_NUM(dm.RECEIVABLE_MONEY,0))) else 0 end) overDueAmount,");
        sql.append(" sum(BT_NVL_NUM(dm.CONTRACT_Money, 0) ) contractMoney");
        sql.append(" from doc_manage dm");
        sql.append(" left join sys_external_corp sec on dm.EXTERNAL_CODE = sec.code ");
        sql.append(" where 1 = 1 and dm.status = 95");
        if (StringUtils.isNotEmpty(corpInSql)) {
            sql.append(" and ").append(corpInSql);
        }
        sql.append(" group by dm.EXTERNAL_NAME, dm.cur_id)");

        sql.append(",detail_sum as( select tt.externalName,tt.creditRating,sum(tt.overDueAmount) overDueAmount,sum(tt.contractMoney) contractMoney from(");
        sql.append(" select t.curid,t.externalName,t.creditRating,");
        sql.append(" case when s.middleRate is not null then t.overDueAmount * s.middleRate else t.overDueAmount end overDueAmount,");
        sql.append(" case when s.middleRate is not null then t.contractMoney * s.middleRate else t.contractMoney end contractMoney");
        sql.append(" from detail t left join cur_rate s on t.curId = s.curId )");
        sql.append(" tt group by tt.externalName,tt.creditRating )");
        sql.append(" select t.externalName, t.creditRating, t.overDueAmount, t.contractMoney");
        sql.append(" from detail_sum t");
        sql.append(" order by t.overDueAmount desc limit 10");
        return sql.toString();
    }

    //获取外汇牌价中间价
    private String getMiddleRate(){
        StringBuilder sql = new StringBuilder();
        sql.append(" with cur_type as(");
        sql.append(" select	r.cur,r.refer_Cur,max(e.create_date) create_date, r.tenant_id");
        sql.append(" from fex_rate_info r");
        sql.append(" left join fex_rate_exc e on r.rate_Exc_Id = e.id");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" where t.rate_Interface_Type = '1' and r.create_by = 'timer'");
        sql.append(" group by r.cur,r.refer_Cur, r.tenant_id)");
        sql.append(" , cur_rate as(");
        sql.append(" select	distinct r.refer_Cur curId,r.middle_Rate/100 as middleRate");
        sql.append(" from fex_rate_info r");
        sql.append(" left join fex_rate_exc e on r.rate_Exc_Id = e.id");
        sql.append(" left join fex_rate_type t on t.id = r.rate_Type_Id");
        sql.append(" left join bt_currency c on	r.cur = c.id");
        sql.append(" join cur_type ct on r.cur = ct.cur	and r.refer_Cur = ct.refer_Cur	and e.create_date = ct.create_date");
        sql.append(" where 1 = 1 and c.ENGLISH_CODE = 'CNY' and t.rate_Interface_Type = '1')");
        return sql.toString();
    }

    private Map<String, Object> getParam(ReceivableIndexQueryVo queryVo){

        Map<String, Object> param = new HashMap<>();
        Optional<String> startDateOptional = Optional.ofNullable(queryVo.getStartDate());
        Optional<String> endDateOptional = Optional.ofNullable(queryVo.getEndDate());

        String startDate = startDateOptional.map(item -> YearMonth.parse(item)
            .atDay(1)
            .toString())
            .orElse(LocalDate.now().withDayOfYear(1).toString());

        String endDate = endDateOptional.map(item -> YearMonth.parse(item)
            .plusMonths(1)
            .atDay(1).toString())
            .orElse(LocalDate.now().withDayOfMonth(1).toString());

        long monthBetween = ChronoUnit.MONTHS.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("monthBetween", monthBetween == 0 ? 1 : monthBetween);

        return param;
    }

    public List<ArDocTypeDistVo> queryArDocTypeDist(ReceivableIndexQueryVo queryVo, String corpInSql) {
        StringBuilder sql = new StringBuilder();

        sql.append(" with detail as(");
        sql.append(" select dm.BATCH_BUS_TYPE, dm.tenant_id,");
        sql.append(" dm.CUR_ID,");
        sql.append(" sum(BT_NVL_NUM(dm.WRITE_OFF_bal,BT_NVL_NUM(dm.RECEIVABLE_MONEY,0))) as writeOffBal");
        sql.append(" from doc_manage dm where 1 = 1 and dm.status = 95");
        if(StringUtils.isNotEmpty(corpInSql)){
            sql.append(" and ").append(corpInSql);
        }
        sql.append(" group by dm.CUR_ID,dm.BATCH_BUS_TYPE, dm.tenant_id)");
        sql.append(" select BT_NVL_CHAR(nbt.BUS_TYPE_NAME,'未区分类型') as name,");
        sql.append(" dm.CUR_ID curId,dm.writeOffBal");
        sql.append(" from detail dm");
        sql.append(" left join NIS_BUS_TYPE nbt");
        sql.append(" on dm.BATCH_BUS_TYPE = nbt.id");
        Map<String, Object> param = getParam(queryVo);
        return listBySQLAliasToBean(sql.toString(), param, ArDocTypeDistVo.class);
    }

    public List<ArAgingDistVo> queryArAgingDist(ReceivableIndexQueryVo queryVo, String corpInSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" with detail as(");
        sql.append(" select dm.CUR_ID curId, dm.tenant_id,BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE) agingDay,");
        sql.append(" sum(BT_NVL_NUM(dm.WRITE_OFF_BAL, dm.RECEIVABLE_MONEY)) writeOffBal");
        sql.append(" from doc_manage dm");
        sql.append(" where 1 = 1 and dm.status = 95");
        sql.append(" and BT_DATEDIFF2(BT_SYSDATE(),dm.AGING_DATE) >= 0");
        sql.append(" and (dm.WRITE_OFF_BAL <> 0 or dm.WRITE_OFF_BAL is null)");
        if(StringUtils.isNotEmpty(corpInSql)){
            sql.append(" and ").append(corpInSql);
        }
        sql.append(" group by dm.CUR_ID ,BT_DATEDIFF2(BT_SYSDATE(), dm.AGING_DATE), dm.tenant_id)");
        sql.append(" select s.curId,s.agingDay,sum(s.writeOffBal) writeOffBal");
        sql.append(" from(");
        sql.append(" select t.curId,case when t.agingDay>=0 and t.agingDay<= 30 then 'thirtyDay'");
        sql.append(" when t.agingDay>30 and t.agingDay<= 60 then 'sixtyDay'");
        sql.append(" when t.agingDay>60 and t.agingDay<= 90 then 'ninetyDay'");
        sql.append(" when t.agingDay>90 then 'overNinetyDay'");
        sql.append(" else 'notDueDay' end agingDay,t.writeOffBal");
        sql.append(" from detail t) s group by s.agingDay,s.curId");
        return  listBySQLAliasToBean(sql.toString(), null, ArAgingDistVo.class);
    }

}
