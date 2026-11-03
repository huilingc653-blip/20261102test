package com.bytter.increment.domain.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.increment.domain.vo.ChoFinanceReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.BiRemainTermDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: dao
 * @Description: 即将到期存款分析
 * @author wangzx
 * @date 2020.1.15
 * @version 1.0
 *
 */
@Repository
public class ChoFinanceReportDao extends BaseDao<ChoFinanceReportVO,String> {
	 /**
     * 查询银行定期存款信息
     * @param dataTablesInput
     * @param joinSql
     * @param biRemainTermDTOs
     * @return
     */
    public List<ChoFinanceReportVO> getChoFinanceReportVOList(DataTablesInput dataTablesInput, String joinSql, List<BiRemainTermDTO> biRemainTermDTOs){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        String corpIds = (String) dataTablesInput.getFormdata().get("ins_corpId");
        String opType = (String) dataTablesInput.getFormdata().get("eqs_opType");
        String curId = (String) dataTablesInput.getFormdata().get("eqs_curId");

        String corpIdsTemp = (String) dataTablesInput.getFormdata().get("INs_corpId");
        String opTypeTemp = (String) dataTablesInput.getFormdata().get("EQs_opType");
        String curIdTemp = (String) dataTablesInput.getFormdata().get("EQs_curId");
        //是否折合币种
        String isToCurr  = (String) dataTablesInput.getFormdata().get("isToCurr");

        //截止日期
        sql.append("select id, corpId, curId, type, opType, buyMoney, drawMoney, interestStartDate, deadlineDate, calEndDate, referIncomeRate, ");
        sql.append(" interestRate, blInterest, blInterestRate, incomeClause, maxWishPayday, certificateWay, accInterestRate, dayLimit, termId from (");

        for(int i = 0; i < biRemainTermDTOs.size(); i++){
            if(i != 0){
                sql.append( " union all ");
            }
            BiRemainTermDTO biRemainTermDTO = biRemainTermDTOs.get(i);
            //银行定期存款
            sql.append("select f.id as id, f.bill_no as billNo, f.corp_Id as corpId, f.cur_Id as curId, 'finance' as type, '1' as opType, f.pay_Money as buyMoney, ");
            sql.append(" bt_nvl_num((case b.is_online when '0' then (select sum(t.pay_money) from cho_bank_finance t where t.pay_bill_id = f.id and t.status = 95)");
            sql.append(" else (select sum(t.pay_money) from cho_bank_finance t left join bis_finance_exc e on t.id = e.cbs_bill_id");
            sql.append(" where t.pay_bill_id = f.id and e.voucher_stat = '0') end),0) as drawMoney, f.interest_Start_Date as interestStartDate, ");
            //sql.append(" f.deadline_Date as deadlineDate, bt_to_date('" + DateUtils.formatDate(biRemainTermDTOs.get(i).getMaxTermDate()) + "','YYYY-MM-DD') as calEndDate, null as referIncomeRate,");
            sql.append(" f.deadline_Date as deadlineDate,");
            //修改控制，如果期限截止日期小于到期日期，截止日期就为期限截止日期，反之到期日期
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getMaxTermDate())){
                sql.append(" case when bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD') < f.deadline_Date then bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD')");
                sql.append(" else f.deadline_Date end as calEndDate,");
            }else {
                sql.append(" f.deadline_Date as calEndDate,");
            }

            sql.append("  null as referIncomeRate,f.interest_Rate as interestRate, f.bl_Interest as blInterest, f.bl_interest_rate as blInterestRate, null as incomeClause, ");
            sql.append(" (case b.is_online when '0' then (select max(t.wish_payday) from cho_bank_finance t where t.pay_bill_id = f.id and t.status = 95)");
            sql.append(" else (select max(t.wish_payday) from cho_bank_finance t left join bis_finance_exc e on t.id = e.cbs_bill_id");
            sql.append(" where t.pay_bill_id = f.id and e.voucher_stat = '0') end) as maxWishPayday, null as certificateWay, null as accInterestRate, null as dayLimit,");
            sql.append(" '" + biRemainTermDTO.getId() + "' as termId");
            sql.append(" from cho_bank_finance f left join bt_bank_acc b on f.payer_Acc_Id = b.id left join bis_finance_exc e on f.id = e.cbs_bill_id");
            sql.append(" where f.bus_type = 10 and (f.is_bl = '1' or e.voucher_stat = '0')");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())){
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }

            sql.append( " union all ");

            //银行理财
            sql.append("select f.id as id, f.bill_no as billNo, f.corp_id as corpId, f.cur_id as curId,  'req' as type, '1' as opType, f.buy_money as buyMoney, ");
            sql.append(" bt_nvl_num((select sum(prd.redeem_money) from cho_product_redeem prd where prd.req_id = f.id ");//时间控制
            sql.append(" and prd.status = 95),0) as drawMoney, f.interest_start_date as interestStartDate, f.deadline_date as deadlineDate,");
            //sql.append(" bt_to_date('" + DateUtils.formatDate(biRemainTermDTOs.get(i).getMaxTermDate()) + "','YYYY-MM-DD') as calEndDate, f.refer_income_rate as referIncomeRate, null as interestRate,");
            //修改控制，如果期限截止日期小于到期日期，截止日期就为期限截止日期，反之到期日期
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getMaxTermDate())){
                sql.append(" case when bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD') < f.deadline_Date then bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD')");
                sql.append(" else f.deadline_Date end as calEndDate,");
            } else {
                sql.append(" f.deadline_Date as calEndDate,");
            }

            sql.append(" f.refer_income_rate as referIncomeRate, null as interestRate,");
            sql.append(" null as blInterest, null as blInterestRate, f.income_clause as incomeClause, null as maxWishPayday, null as certificateWay, null as accInterestRate, null as dayLimit,");
            sql.append(" '" + biRemainTermDTO.getId() + "' as termId");
            sql.append(" from cho_product_req f left join cho_product_type pt on pt.product_code = f.product_code ");
            sql.append(" where f.status = '95' and f.product_type = '2'");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())) {
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
            sql.append(" union all ");

            //内部定期存款
            sql.append("select f.id as id, f.bill_no as billNo, f.corp_Id as corpId, f.cur_Id as curId, 'inner' as type, '2' as opType, f.pay_Money as buyMoney,");
            sql.append(" bt_nvl_num((select bt_nvl_num(sum(t.pay_money), 0) from cho_inner_finance t where t.related_Bill_Id = f.id ");
            sql.append(" and t.status = 100 and t.bus_type in (11)),0) as drawMoney, f.interest_Start_Date as interestStartDate, f.deadline_Date as deadlineDate,");
            //sql.append(" bt_to_date('" + DateUtils.formatDate(biRemainTermDTOs.get(i).getMaxTermDate()) + "','YYYY-MM-DD') as calEndDate, ");
            //修改控制，如果期限截止日期小于到期日期，截止日期就为期限截止日期，反之到期日期
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getMaxTermDate())){
                sql.append(" case when bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD') < f.deadline_Date then bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD')");
                sql.append(" else f.deadline_Date end as calEndDate,");
            } else {
                sql.append(" f.deadline_Date as calEndDate,");
            }

            sql.append(" null as referIncomeRate, f.interest_Rate as interestRate, null as blInterest, null as blInterestRate, null as incomeClause, null as maxWishPayday, f.certificate_Way as certificateWay, a.interest_rate as accInterestRate, null as dayLimit,");
            sql.append(" '" + biRemainTermDTO.getId() + "' as termId");
            sql.append(" from cho_inner_finance f left join fc_acc a on f.payer_Acc_Id = a.id where f.bus_type = 10 and f.status = 100");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getMaxTermDate())){
                sql.append(" and f.interest_Start_Date <=  bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "', 'YYYY-MM-DD')");
            }
            sql.append(" and ((select bt_nvl_num(sum(t.pay_money), 0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status = 100) < f.pay_money)");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())) {
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
            sql.append(" union all ");

            //保证金定期
            sql.append("select id, billNo, corpId, curId, type, opType, buyMoney, drawMoney, interestStartDate, deadline_Date as deadlineDate, calEndDate,");
            sql.append("referIncomeRate, interestRate, blInterest, blInterestRate, incomeClause, maxWishPayday, certificateWay, accInterestRate, dayLimit, termId ");
            sql.append(" from ( select b.id as id, b.bill_no as billNo, b.corp_id as corpId, b.cur_id as curId, 'bond' as type, '1' as opType, b.bond_money as buyMoney,");
            sql.append(" bt_nvl_num((select sum(f.back_money) from fc_bond_back f where f.bond_id = b.id and f.status = 95), 0) as drawMoney,");
            sql.append(" b.start_date as interestStartDate, b.end_date as deadline_Date, ");
            //修改控制，如果期限截止日期小于到期日期，截止日期就为期限截止日期，反之到期日期
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getMaxTermDate())){
                sql.append(" case when bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD') < b.end_date then bt_to_date('" + DateUtils.formatDate(biRemainTermDTO.getMaxTermDate()) + "','YYYY-MM-DD')");
                sql.append(" else b.end_date end as calEndDate,");
            } else {
                sql.append(" b.end_date as calEndDate,");
            }

            sql.append(" null as referIncomeRate, b.interest_rate as interestRate, null as blInterest, null as blInterestRate, null as incomeClause,");
            sql.append(" (select max(f.back_date) from fc_bond_back f where f.bond_id = b.id and f.status = 95) as maxWishPayday, null as certificateWay, null as accInterestRate, b.day_limit as dayLimit,");
            sql.append(" '" + biRemainTermDTO.getId() + "' as termId");
            sql.append(" from fc_bond b where b.status = 95 and b.is_regular = '1') f where 1=1 ");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())) {
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
        }
        sql.append(") f where 1 = 1 ");
        sql.append(joinSql);

        //加入单位过滤条件
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + SqlUtil.getInSql(corpIds.trim().trim().split(","), "f.corpId", true));
        }
        //加入内部/银行存款条件
        if (EmptyUtils.isNotEmpty(opType)) {
            sql.append(" and f.opType =:opType ");
            params.put("opType", opType);
        }
        //加入币别条件
        if(StringUtils.isEmpty(isToCurr) || "0".equals(isToCurr)){
            if (EmptyUtils.isNotEmpty(curId)) {
                sql.append(" and f.curId =:curId ");
                params.put("curId", curId);
            }
        }
        //图表条件过滤
        if (EmptyUtils.isNotEmpty(corpIdsTemp)) {
            sql.append(" and " + SqlUtil.getInSql(corpIdsTemp.trim().trim().split(","), "f.corpId", true));
        }
        if (EmptyUtils.isNotEmpty(opTypeTemp)) {
            sql.append(" and f.opType =:opType ");
            params.put("opType", opTypeTemp);
        }
        //加入币别条件
        if(StringUtils.isEmpty(isToCurr) || "0".equals(isToCurr)){
            if (EmptyUtils.isNotEmpty(curIdTemp)) {
                sql.append(" and f.curId =:curId ");
                params.put("curId", curIdTemp);
            }
        }
        System.out.println("sql============"+sql.toString());
        System.out.println("params============"+params.toString());
        return this.listBySQLAliasToBean(sql.toString(),params,ChoFinanceReportVO.class);
    }
}
