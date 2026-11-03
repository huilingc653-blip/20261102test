package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductReqQueryVo;
import com.bytter.increment.domain.vo.ChoProductReqUncollectedVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Dao
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoProductReqQueryDao extends BaseDao<ChoProductReq,String> {
    public DataTablesOutput<ChoProductReqQueryVo> getDataTablesUnion(DataTablesInput input, String joinSql){
        try {
            StringBuilder sql = new StringBuilder();
            Class<ChoProductReqQueryVo> voClazz = ChoProductReqQueryVo.class;
            // 是否查询保本类型
            boolean isQuery = input.getFormdata().get("initType") != null;
            sql.append(" select distinct pr.id as id, prd.req_id as reqId,pr.corp_id as corpId,pr.bank_type_id as bankTypeId, pr.bank_name as bankName,pr.product_code as productCode, ");
            sql.append(" pt.product_name as productName,pr.product_type as productType,pr.income_clause as incomeClause, pr.bank_acc as bankAcc, ");
            sql.append(" pr.buy_money as buyMoney,pr.cur_id as curId,pr.cur_name as curName, pr.interest_start_date as interestStartDate,prd.redeem_date as redeemDate, ");
            sql.append(" pr.deadline_date as deadlineDate,pr.refer_income_rate as referIncomeRate,pr.refer_income_money as referIncomeMoney,prd.income_rate as incomeRate, ");
//            sql.append(" prd.income_rate as incomeRate, prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, prd.charge_money as chargeMoney, ");
            sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, pr.buy_date as buyDate, pr.biz_status as bizStatus,");
            sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
            sql.append(" pr.is_move_up as isMoveUp,pr.status as status,pr.redeem_status as redeemStatus,pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource, ");
            sql.append(" pr.portion as portion,pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
            sql.append(" case when pr.is_maintain_val is null then '1' else pr.is_maintain_val end isMaintainVal, pr.day_limit as dayLimit ");
            if (!isQuery) {
                sql.append(" ,pn.actual_income as actualIncome, pn.bal as bal, pn.net_val_date as netValDate,  pn.buy_money as holdMoney ");
            }
            sql.append(" from cho_product_req pr ");
            sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
            sql.append(" left join cho_product_redeem_detail prd on pr.id = prd.req_id ");
            if (!isQuery) {
                sql.append(" LEFT JOIN cho_product_netval pn ON pn.req_id = pr.id");
                sql.append(" LEFT JOIN (SELECT MAX(net_val_date) AS MAXDATE, REQ_ID AS REQID FROM cho_product_netval GROUP BY REQ_ID) pn2 ");
                sql.append(" ON pn2.REQID = pr.id ");
            }
            sql.append(" where 1 = 1 ");
            sql.append(" and (pr.status <> -2 or pr.status is null)");

            //访问地址来源是否为通知,待赎回金额≠0
            String from = (String) input.getFormdata().get("from");
            if(!EmptyUtils.isEmpty(from) && from.equals("notification")){
                sql.append(" and pr.buy_money > (select  BT_NVL_NUM(sum(d.REDEEM_MONEY),0) from cho_product_redeem prd left join cho_product_redeem_detail d on prd.id = d.cpr_id where d.req_id = pr.ID and prd.status = 95) ");
            }

            if (StringUtils.isNotEmpty(joinSql)) {
                sql.append(joinSql);
            }
            if (isQuery) {
                sql.append(" and pt.FINANCIAL_TYPE in (0, 1) ");
            } else {
                sql.append(" and (pn2.maxDate = pn.net_val_date or pn.net_val_date is null ) ");
            }
            sql.append(getWhereAllCondition(input,voClazz.newInstance(),"",false));

            Map<String, Object> values = input.getSearchValues();
            String countSQL = countSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL,values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
//                    inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<ChoProductReqQueryVo>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
    public DataTablesOutput<ChoProductReqQueryVo> getDataTablesUnionForSelect(DataTablesInput input){
        try {
            StringBuilder sql = new StringBuilder();
            Class<ChoProductReqQueryVo> voClazz = ChoProductReqQueryVo.class;
            sql.append(" select pr.id as id, prd.req_id as reqId,pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_type_id as bankTypeId, pr.bank_type_name as bankTypeName, pr.bank_name as bankName,pr.product_code as productCode, ");
            sql.append(" pt.product_name as productName,pr.product_type as productType,pr.income_clause as incomeClause, pr.bank_acc as bankAcc, ");
            sql.append(" pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, prd.redeem_date as redeemDate,prd.redeem_money as redeemedMoney,pr.interest_start_date as interestStartDate, ");
            sql.append(" pr.deadline_date as deadlineDate,pr.refer_income_rate as referIncomeRate,pr.refer_income_money as referIncomeMoney, ");
            sql.append(" prd.income_rate as incomeRate, prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, prd.charge_money as chargeMoney, ");
            sql.append(" pr.is_move_up as isMoveUp,pr.status as status,pr.redeem_status as redeemStatus,pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,prd.COLLECTION_CONFIRM ");
            sql.append(" from cho_product_req pr ");
            sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
            sql.append(" LEFT JOIN ( ");
            sql.append("     select r.redeem_date,r.redeem_money,r.req_id,r.income_rate,r.SUB_FEE_MONEY,r.REDEEM_CHARGE_MONEY,r.charge_money,r.COLLECTION_CONFIRM from cho_product_redeem r, ");
            sql.append(" 		(select max(redeem_date) as redeem_date,req_id from cho_product_redeem where status = '95' group by req_id ) maxRedeem ");
            sql.append("     where r.req_id = maxRedeem.req_id and r.redeem_date = maxRedeem.redeem_date ");
            sql.append("   ) prd ON prd.req_id = pr.id and prd.redeem_date = redeem_date ");
            sql.append(" where 1 = 1 and (prd.COLLECTION_CONFIRM = '0' or prd.COLLECTION_CONFIRM is null) ");
            sql.append(getWhereAllCondition(input,voClazz.newInstance(),"",false));
            Map<String, Object> values = input.getSearchValues();
            String countSQL = countSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL,values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<ChoProductReqQueryVo>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
    protected String countSQL(String orgHql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from( ")
                .append(orgHql)
                .append(" ) total");
        return sql.toString();
    }
    /**
     * 应收未收利息明细表列表数据
     * @param input
     * @return
     */
    public DataTablesOutput uncollectedDataTable(DataTablesInput input, String joinSql) {
        try {
            StringBuilder sql = new StringBuilder();
            Class<ChoProductReqUncollectedVo> voClazz = ChoProductReqUncollectedVo.class;
            String isExpireStr = (String) input.getFormdata().get("isExpire");
            String calEndDateStr = (String) input.getFormdata().get("calEndDate");
            // 是否查询保本类型
            boolean isQuery = input.getFormdata().get("initType") != null;
            uncollectedCommonSql(sql);
            sql.append(" from cho_product_req pr ");
            sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");

            sql.append(" LEFT JOIN ( SELECT r.redeem_date, r.redeem_money, r.req_id,r.income_rate, r.SUB_FEE_MONEY, r.REDEEM_CHARGE_MONEY,r.charge_money  ");
            sql.append(" 	FROM cho_product_redeem_detail r,cho_product_redeem cpr, ( SELECT max( d.redeem_date ) AS redeem_date, d.req_id FROM cho_product_redeem cpr left join cho_product_redeem_detail d on cpr.id = d.cpr_id ");
            sql.append(" WHERE STATUS = '95' and d.REDEEM_DATE <= :calEndDate  ");
            sql.append(" 	GROUP BY d.req_id ) maxRedeem  WHERE r.req_id = maxRedeem.req_id and cpr.id = r.cpr_id AND r.redeem_date = maxRedeem.redeem_date  ");
            sql.append(" and cpr.create_date = (select max(create_date) from cho_product_redeem r2 left join cho_product_redeem_detail d2 on r2.id = d2.cpr_id where r.req_id=d2.req_id and r.redeem_date=d2.redeem_date)  ) prd ON prd.req_id = pr.id");
            sql.append(" where 1 = 1 and pr.status in ('95','110') ");
            if ("1".equalsIgnoreCase(isExpireStr)){
                sql.append(" and pr.deadline_date <= :calEndDate ");
            }else if ("2".equalsIgnoreCase(isExpireStr)){
                sql.append(" and pr.deadline_date > :calEndDate ");
            }
            if (StringUtils.isNotEmpty(joinSql)) {
                sql.append(joinSql);
            }
            if (isQuery) {
                sql.append(" and pt.FINANCIAL_TYPE in (0, 1) ");
            }
            sql.append(getWhereAllCondition(input,voClazz.newInstance(),"",false));
            // sql.append(" order by pr.corp_id, pr.product_type,pr.product_code,pr.interest_start_date ");
            Map<String, Object> values = input.getSearchValues();
            values.put("calEndDate", DateUtils.str2Date(calEndDateStr, DateUtils.date_sdf()));
            String countSQL = countSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL,values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<ChoProductReqUncollectedVo>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
    /**
     * 应收未收利息明细公共查询列
     * @param sql sql
     */
    private void uncollectedCommonSql(StringBuilder sql) {
        sql.append(" select pr.id as id, prd.req_id as reqId, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_type_id as bankTypeId, pr.bank_type_name as bankTypeName, pr.bank_name as bankName,");
        sql.append(" pr.product_type as productType, pr.product_code as productCode, pt.product_name as productName, pr.bank_acc as bankAcc, ");
        sql.append(" pr.buy_money as buyMoney, pr.cur_id as curId, pr.cur_name as curName, pr.day_limit as dayLimit, pr.interest_start_date as interestStartDate, ");
        sql.append(" pr.deadline_date as deadlineDate, pr.refer_income_rate as referIncomeRate, pr.refer_income_money as referIncomeMoney, ");
        sql.append(" prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, prd.charge_money as chargeMoney, ");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, pr.buy_date as buyDate,");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append(" case when pr.is_maintain_val is null then '1' else pr.is_maintain_val end isMaintainVal,");
        sql.append(" pr.income_clause as incomeClause, prd.redeem_date as redeemDate ");
    }
    /**
     * 理财到期提醒,根据设置的提醒天数,查找未赎回完成且处于天数内的登记记录,返回的buyMoney为剩余金额
     * @param startDate
     * @param finishDate
     * @return
     */
    public List<ChoProductReqVo> listChoProductByDate(Date startDate, Date finishDate){
        StringBuilder sql = new StringBuilder();
        sql.append(" select bt_nvl_num((pr.buy_money - prd.redeemMoney), pr.buy_money) as buyMoney,pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName");
        sql.append(" from cho_product_req pr left join (select bt_nvl(sum(c.redeem_money), 0) as redeemMoney, c.req_id ");
        sql.append(" from cho_product_redeem c left join cho_product_req p on p.id = c.req_id ");
        sql.append(" where 1 = 1 and c.status = '95' and p.deadline_date >= :startDate ");
        sql.append(" and p.deadline_date <= :finishDate group by c.req_id) prd ");
        sql.append(" on prd.req_id = pr.id where 1 = 1 ");
        sql.append(" and pr.deadline_date >= :startDate and pr.deadline_date <= :finishDate ");
        sql.append(" and pr.status = '95' and (pr.NOTICE_SIGN = '0' or pr.NOTICE_SIGN is null) ");
        Map<String,Object> params = new HashMap<>();
        params.put("startDate",startDate);
        params.put("finishDate",finishDate);
        return listBySQLAliasToBean(sql.toString(),params, ChoProductReqVo.class);
    }
    /**
     * 理财到期提醒,根据设置的提醒天数,查找未赎回完成且处于天数内的登记记录,返回的buyMoney为购买金额
     * @param startDate
     * @param finishDate
     * @return
     */
    public List<ChoProductReqVo> listChoProductBuyMoneyByDate(Date startDate, Date finishDate){
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName, pr.product_code as productCode, pr.buy_money as buyMoney, ");
        sql.append(" pr.bank_acc as bankAcc, pr.bank_type_name as bankTypeName, pr.deadline_date as deadlineDate, ");
        sql.append(" pr.REFER_INCOME_RATE as referIncomeRate, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate ");
        sql.append(" from cho_product_req pr where 1 = 1 ");
        sql.append(" and pr.deadline_date >= :startDate ");
        sql.append(" and pr.deadline_date <= :finishDate ");
        sql.append(" and pr.status = '95' ");
        sql.append(" and (pr.NOTICE_SIGN = '0' or pr.NOTICE_SIGN is null or pr.NOTICE_SIGN = '') ");
        Map<String,Object> params = new HashMap<>();
        params.put("startDate",startDate);
        params.put("finishDate",finishDate);
        return listBySQLAliasToBean(sql.toString(),params, ChoProductReqVo.class);
    }
    /**
     * 根据参数获取手机报表理财数据
     * @param vo
     * @param startDate
     * @param endDate
     * @return
     */
    public List<ChoProductReqUncollectedVo> listReportInfo(ChoProductReqUncollectedVo vo, String startDate, String endDate, String joinSql){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append("select pr.id as id,pr.corp_id as corpId, pr.corp_name as corpName, pr.product_code as productCode,pt.product_name as productName,");
        sql.append("pr.product_type as productType,pr.bank_acc as bankAcc, pr.buy_money as buyMoney,");
        sql.append("pr.deadline_date as deadlineDate,pr.refer_income_money as referIncomeMoney,pr.status as status");
        sql.append(" from cho_product_req pr left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join ( select r.redeem_date, r.redeem_money, r.req_id,r.income_rate, r.sub_fee_money, r.redeem_charge_money,r.charge_money  ");
        sql.append(" 	from cho_product_redeem r, ( select max( redeem_date ) as redeem_date, req_id from cho_product_redeem where status = '95' and bt_to_char (redeem_date, 'YYYY-MM-DD' ) <= :calEndDate ");
        sql.append(" 	group by req_id ) maxredeem  where r.req_id = maxredeem.req_id and r.redeem_date = maxredeem.redeem_date ) prd on prd.req_id = pr.id  ");
        sql.append(" where 1 = 1 and pr.status in ('95', '110') ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if(StringUtils.isNotBlank(startDate)){
            sql.append(" and (( bt_to_char ( pr.deadline_date , 'YYYY-MM-DD' ) >= '").append(startDate).append("')");
        }
        if(StringUtils.isNotBlank(endDate)){
            if(StringUtils.isNotBlank(startDate)){
                sql.append(" and (bt_to_char ( pr.deadline_date  , 'YYYY-MM-DD' ) <= '").append(endDate).append("')");
            }else{
                sql.append(" and ((bt_to_char ( pr.deadline_date  , 'YYYY-MM-DD' ) <= '").append(endDate).append("')");
            }
        }
        sql.append(" or pr.deadline_date is null) ");
        if(StringUtils.isNotBlank(vo.getCorpId())){
            sql.append(" and ").append(SqlUtil.getInSql(vo.getCorpId().split(CommonConstant.COMMA_SPLIT), "pr.corp_id", true));
        }
        params.put("calEndDate", DateUtils.format(vo.getCalEndDate(),"yyyy-MM-dd"));
        if(StringUtils.isNotBlank(vo.getCurId())){
            sql.append(" and pr.cur_id =:curId ");
            params.put("curId",vo.getCurId());
        }
        if ("1".equalsIgnoreCase(vo.getIsExpireStr())){
            sql.append(" and bt_to_char ( pr.deadline_date  , 'YYYY-MM-DD' ) <= '").append(DateUtils.formatDate(vo.getCalEndDate())).append("'");
        }else if ("2".equalsIgnoreCase(vo.getIsExpireStr())){
            sql.append(" and bt_to_char ( pr.deadline_date  , 'YYYY-MM-DD' ) > '").append(DateUtils.formatDate(vo.getCalEndDate())).append("'");
        }
        sql.append(" order by pr.corp_id, pr.product_type,pr.product_code,pr.interest_start_date ");
        return listBySQLAliasToBean(sql.toString(),params, ChoProductReqUncollectedVo.class);
    }
    /**
     * 应收未收利息明细（非保本数据查询）
     * @param input 参数
     * @param joinSql 单位权限控制
     * @return 列表数据
     */
    public DataTablesOutput uncollectedNonDataTable(DataTablesInput input, String joinSql) {
        try {
            StringBuilder sql = new StringBuilder();
            Class<ChoProductReqUncollectedVo> voClazz = ChoProductReqUncollectedVo.class;
            uncollectedCommonSql(sql);
            sql.append(" ,pn.actual_income as actualIncome, pn.net_val_date as netValDate, pn.buy_money as holdMoney ,pr.portion as portion,  ");
            sql.append(" pn.daily_net_val as dailyNetVal ");
            sql.append(" from cho_product_req pr ");
            sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
            sql.append(" LEFT JOIN ( SELECT r.redeem_date, r.redeem_money, r.req_id,r.income_rate, r.SUB_FEE_MONEY, r.REDEEM_CHARGE_MONEY,r.charge_money  ");
            sql.append(" 	FROM cho_product_redeem r, ( SELECT max( redeem_date ) AS redeem_date, req_id FROM cho_product_redeem WHERE STATUS = '95' ");
            sql.append(" 	GROUP BY req_id ) maxRedeem  WHERE r.req_id = maxRedeem.req_id AND r.redeem_date = maxRedeem.redeem_date  ");
            sql.append(" and r.create_date = (select max(create_date) from cho_product_redeem r2 where r.req_id=r2.req_id and r.redeem_date=r2.redeem_date)  ) prd ON prd.req_id = pr.id");
            sql.append(" LEFT JOIN cho_product_netval pn ON pn.req_id = pr.id");
            sql.append(" LEFT JOIN (SELECT MAX(net_val_date) AS MAXDATE, REQ_ID AS REQID FROM cho_product_netval GROUP BY REQ_ID) pn2 ");
            sql.append(" ON pn2.REQID = pr.id ");
            sql.append(" where 1 = 1 and pr.status in ('95','110') ");
            sql.append(" and pn2.maxDate = pn.net_val_date ");
            if (StringUtils.isNotEmpty(joinSql)) {
                sql.append(joinSql);
            }
            sql.append(getWhereAllCondition(input,voClazz.newInstance(),"",false));
            // sql.append(" order by pr.corp_id, pr.product_type,pr.product_code,pr.interest_start_date ");
            Map<String, Object> values = input.getSearchValues();
            String countSQL = countSQL(sql.toString());
            BigDecimal count = (BigDecimal) createSQLQuery(countSQL,values).uniqueResult();
            List list;
            if (count.compareTo(BigDecimal.ZERO) > 0) {
                list = listBySQL(sql.toString(), input.getStart(), input.getLength(), values);
                list = BeanUtils.copyMapListProperties(list, voClazz);
                if (null != voClazz) {
                    //inject(list);
                }
            } else {
                list = new ArrayList(0);
            }
            return new DataTablesOutput<ChoProductReqUncollectedVo>(input.getDraw(), list, count.intValue(), count.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParameterException(e);
        }
    }
}
