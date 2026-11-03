package com.bytter.report.domain.netbank.dao;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.constant.AllocationConstant;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.report.domain.netbank.vo.SettlementStatisticsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Arrays;
import java.util.Date;
/**
 * 结算业务量统计表Dao
 *
 * @author huangx 2022-06-21
 */
@Repository
public class SettlementStatisticsDao extends BaseDao<SettlementStatisticsVO, String> {
    /**
     * 查询日期条件拼接
     * @param dataTablesInput
     * @param filedName 字段名称
     * @return
     */
    public String getSqlWhereBySearchDate(DataTablesInput dataTablesInput, String filedName){
        StringBuilder sql = new StringBuilder();
        //查询时间
        String gedSearchDate = (String) dataTablesInput.getFormdata().get("ged_searchDate");
        String ledSearchDate = (String) dataTablesInput.getFormdata().get("led_searchDate");

        if(StringUtils.isNotBlank(gedSearchDate)){
            Date searchDateBegin  = DateUtils.parse(gedSearchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            sql.append(" and " + filedName + " >= bt_to_date('").append(DateUtils.format(searchDateBegin,DateUtils.PATTERN_CLASSICAL_SIMPLE)).append("','yyyy-MM-dd') ");
        }
        if(StringUtils.isNotBlank(ledSearchDate)){
            Date searchDateEnd  = DateUtils.parse(ledSearchDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            searchDateEnd=DateUtils.addDay(searchDateEnd,1);//结束日期加1天
            sql.append(" and " + filedName + " < bt_to_date('").append(DateUtils.format(searchDateEnd,DateUtils.PATTERN_CLASSICAL_SIMPLE)).append("','yyyy-MM-dd') ");
        }
        return sql.toString();
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        //网点
        String netId = (String) dataTablesInput.getFormdata().get("netId");
        if(StringUtils.isNotBlank(netId)){
            joinSql+=" and c.net_id = '" + netId + "'";
        }
        //单位
        String corpId = (String) dataTablesInput.getFormdata().get("corpId");
        if(StringUtils.isNotBlank(corpId)){
            joinSql+=" and n.corp_Id = '" + corpId + "'";
        }
        sql.append("select t.voucherTypeId,t.curId,sum(payNumbers) as payNumbers,sum(payMoney) as payMoney,sum(tallyNumbers) as tallyNumbers,sum(tallyMoney) as tallyMoney from (");
        //1.付款：在线（交易成功）
        sql.append("select n.voucher_Type_Id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" left join bis_exc e on n.id = e.cbs_bill_id and e.voucher_from = '"+ BdlinkConstant.FROM_NIS_PAY +"'");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where e.voucher_stat = '"+BdlinkConstant.SUCCESS+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.voucher_type_id, n.cur_id");
        sql.append(" union all ");
        //2.付款：离线（台账）
        sql.append("select n.voucher_Type_Id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" left join bis_exc e on n.id = e.cbs_bill_id and e.voucher_from = '"+ BdlinkConstant.FROM_NIS_PAY +"'");
        sql.append(" left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where (b.is_online = '"+ CommonConstant.NO +"' or n.is_payed = '"+CommonConstant.YES+"')");
        sql.append(" and (e.voucher_no is null or e.voucher_no = '')");
        sql.append(" and (n.status = '"+ NetbankConstant.STATUE_CHECK +"' or n.status = '"+ NetbankConstant.STATUE_TALLY_CHECKPASS+"')");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wish_payday"));
        sql.append(" group by n.voucher_type_id, n.cur_id");
        sql.append(" union all ");
        //3.付款：记账不产生指令
        sql.append("select n.voucher_Type_Id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,count(*) as tallyNumbers,sum(n.pay_money) as tallyMoney");
        sql.append(" from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" left join bis_exc e on n.id = e.cbs_bill_id and e.voucher_from = '"+BdlinkConstant.FROM_NIS_PAY+"'");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where n.is_payed = '"+ CommonConstant.NO +"' and (e.voucher_no is null or e.voucher_no = '')");
        sql.append(" and n.tally_sign = '"+ CommonConstant.YES +"' and t.is_account = '"+ CommonConstant.YES +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wish_payday"));
        sql.append(" group by n.voucher_type_id, n.cur_id");
        sql.append(" union all ");
        //4.票据付款
        sql.append("select n.voucher_Type_Id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where n.is_payed = '"+CommonConstant.NO+"' and t.sys_voucher_code in ('"+NetbankConstant.PAY_TYPE_49+"', '"+NetbankConstant.PAY_TYPE_50+"')");
        sql.append(" and n.status = '"+NetbankConstant.STATUE_BILL_COMPLETE+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wish_payday"));
        sql.append(" group by n.voucher_type_id, n.cur_id");
        sql.append(" union all ");
        //5.付款：记账且产生指令（交易成功）
        sql.append("select n.voucher_Type_Id as voucherTypeId,n.cur_id as curId,0 as payNumbers,0 as payMoney,count(*) as tallyNumbers,sum(n.pay_money) as tallyMoney");
        sql.append(" from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" left join bis_exc e on n.id = e.cbs_bill_id and e.voucher_from = '"+BdlinkConstant.FROM_NIS_PAY+"'");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where e.voucher_stat = '"+BdlinkConstant.SUCCESS+"'");
        sql.append(" and n.tally_sign = '"+ CommonConstant.YES +"' and t.is_account = '"+ CommonConstant.YES +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.voucher_type_id, n.cur_id");
        sql.append(" union all ");
        //6.代发工资：离线（台账）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_48+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.wages) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_wage n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join bis_bif_rule r on r.bif_code = n.bif_code");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where (b.is_online = '"+ CommonConstant.NO +"' or r.is_bank_online = '"+ CommonConstant.NO +"')");
        sql.append(" and b.status = '"+NetbankConstant.STATUE_CHECK+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wage_day"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //7.代发工资：在线（交易成功）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_48+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.wages) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_wage n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join bis_bif_rule r on r.bif_code = n.bif_code");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.YES +"' and r.is_bank_online = '"+ CommonConstant.YES +"'");
        sql.append(" and b.status = '"+NetbankConstant.STATUE_CHECK+"'");
        sql.append(" and not exists (select count(*) from bis_exc e where e.cbs_bill_id = n.voucher_stat and e.voucher_from = '"+BdlinkConstant.FROM_NIS_WAGE+"' and e.voucher_stat <> '"+BdlinkConstant.SUCCESS+"')");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wage_day"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //8.头寸调拨：离线（台账）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_37+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_allocation_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.NO +"'");
        sql.append(" and (n.status = '"+ NetbankConstant.STATUE_CHECK +"' or n.status = '"+NetbankConstant.STATUE_TALLY_CHECKPASS+"')");
        sql.append(" and n.type = '"+ AllocationConstant.ALLOCATION_TYPE_POS +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wish_payday"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //8.请款&下拨：离线（台账）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_10+"' and t.is_system='1') as voucherTypeId,");
        sql.append("n.cur_id as curId,count(*) as payNumbers,sum(n.money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_allocation_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.NO +"'");
        sql.append(" and (n.status = '"+ NetbankConstant.STATUE_CHECK +"' or n.status = '"+NetbankConstant.STATUE_TALLY_CHECKPASS+"')");
        sql.append(" and n.type in ('"+ AllocationConstant.ALLOCATION_TYPE_DOWN +"','"+AllocationConstant.ALLOCATION_TYPE_ASK+"')");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.wish_payday"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //9.头寸调拨&请款&下拨：在线（交易成功）
        sql.append("select e.voucher_type_id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from nis_allocation_req n");
        sql.append(" left join bis_exc e on n.id=e.cbs_bill_id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where e.voucher_stat = '"+ BdlinkConstant.SUCCESS +"'");
        sql.append(" and (n.status = '"+ NetbankConstant.STATUE_CHECK +"' or n.status = '"+NetbankConstant.STATUE_TALLY_CHECKPASS+"')");
        sql.append(" and n.type in ('"+ AllocationConstant.ALLOCATION_TYPE_POS +"','"+ AllocationConstant.ALLOCATION_TYPE_DOWN +"','"+AllocationConstant.ALLOCATION_TYPE_ASK+"')");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by e.voucher_type_id,n.cur_id");
        sql.append(" union all ");
        //10.头寸调拨：记账
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_37+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,0 as payNumbers,0 as payMoney,count(*) as tallyNumbers,sum(n.money) as tallyMoney");
        sql.append(" from nis_allocation_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join bis_exc e on n.id=e.cbs_bill_id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where (b.is_online = '"+CommonConstant.NO+"' or (b.is_online = '"+CommonConstant.NO+"' and e.voucher_stat = '"+BdlinkConstant.SUCCESS+"'))");
        sql.append(" and n.type = '"+ AllocationConstant.ALLOCATION_TYPE_POS +"' and n.status = '"+NetbankConstant.STATUE_TALLY_CHECKPASS+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //11.请款&下拨：记账
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_10+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,0 as payNumbers,0 as payMoney,count(*) as tallyNumbers,sum(n.money) as tallyMoney");
        sql.append(" from nis_allocation_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join bis_exc e on n.id=e.cbs_bill_id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where (b.is_online = '"+CommonConstant.NO+"' or (b.is_online = '"+CommonConstant.NO+"' and e.voucher_stat = '"+BdlinkConstant.SUCCESS+"'))");
        sql.append(" and n.type in ('"+ AllocationConstant.ALLOCATION_TYPE_DOWN +"','"+AllocationConstant.ALLOCATION_TYPE_ASK+"')");
        sql.append(" and n.status = '"+NetbankConstant.STATUE_TALLY_CHECKPASS+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //12.外币汇款：离线（台账）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_34+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from fex_foreign_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.NO +"' and n.status = '"+ NetbankConstant.STATUE_CHECK +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.pay_date"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //13.外币汇款：在线（交易成功）
        sql.append("select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_34+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from fex_foreign_req n left join bt_bank_acc b on n.payer_acc = b.bank_acc");
        sql.append(" left join bis_foreign_exc e on n.id=e.cbs_bill_id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.YES +"' and n.status = '"+ NetbankConstant.STATUE_CHECK +"'");
        sql.append(" and e.voucher_stat='"+BdlinkConstant.SUCCESS+"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.cur_id");
        sql.append(" union all ");
        //14.外汇交易：离线（台账）
        sql.append("select n.voucher_type_id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from fex_cash_flow n left join sys_corp c on c.id = n.corp_id");
        sql.append(" where n.pay_status = '"+ CommonConstant.YES +"'");
        sql.append(" and n.cd_sign='"+ CommonConstant.YES +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.settle_date"));
        sql.append(" group by n.voucher_type_id,n.cur_id");
        sql.append(" union all ");
        //15.外汇交易：在线（交易成功）
        sql.append("select n.voucher_type_id as voucherTypeId,n.cur_id as curId,count(*) as payNumbers,sum(n.pay_money) as payMoney,0 as tallyNumbers,0 as tallyMoney");
        sql.append(" from fex_cash_flow n left join bt_bank_acc b on n.corp_bank_acc = b.bank_acc");
        sql.append(" left join bis_foreign_exc e on n.id=e.cbs_bill_id");
        sql.append(" left join sys_corp c on c.id = n.corp_id");
        sql.append(" where b.is_online = '"+ CommonConstant.YES +"' and e.voucher_stat='"+BdlinkConstant.SUCCESS+"'");
        sql.append(" and n.cd_sign='"+ CommonConstant.YES +"'");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "e.trans_time"));
        sql.append(" group by n.voucher_type_id,n.cur_id");
        //16.转收
        sql.append(" union all ");
        sql.append(" select (select id from fc_voucher_type t where t.sys_voucher_code = '"+NetbankConstant.PAY_TYPE_01+"' and t.is_system='1') as voucherTypeId,");
        sql.append(" n.cur_id as curId,count(*) as payNumbers,sum(n.money) as payMoney,count(*) as tallyNumbers,sum(n.money) as tallyMoney ");
        sql.append("  from bt_net_chalk n left join bt_bank_acc b on n.bank_acc_id = b.id ");
        sql.append(" left join sys_corp c on c.id = n.corp_id ");
        sql.append(" where   n.cd_sign = 0 and b.parent_sign = '1' and c.type = 0 ");
        sql.append(" and n.estate = '1' and n.bank_acc is not null and (n.purpose <> '转付下拨' or n.purpose is null) ");
        sql.append(" and n.corp_id not in (select bc.corp_id from bt_bank_acc bc where bc.bank_acc = n.opp_Acc_No and n.bank_acc <> n.opp_Acc_No) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(this.getSqlWhereBySearchDate(dataTablesInput, "n.trans_time"));
        sql.append(" group by n.cur_id");
        sql.append(" ) t where 1=1");
        //币种
        String curId = (String) dataTablesInput.getFormdata().get("curId");
        if(StringUtils.isNotBlank(curId)){
            sql.append(" and t.curId = '" + curId + "'");
        }
        //付款方式
        String voucherTypeIdStr = (String) dataTablesInput.getFormdata().get("voucherTypeId");
        if(StringUtils.isNotBlank(voucherTypeIdStr)){
            String[] voucherTypeIds = voucherTypeIdStr.split(CommonConstant.COMMA_SPLIT);
            sql.append(" and ").append(SqlUtil.getInSql(Arrays.asList(voucherTypeIds), "t.voucherTypeId", true));
        }
        sql.append(" group by voucherTypeId, curId");
        return getDynamicBySQL(dataTablesInput, sql.toString(), SettlementStatisticsVO.class, "t", false);
    }
}
