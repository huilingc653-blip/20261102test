package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.entity.CmsLoanInfo;
import com.bytter.report.domain.cms.vo.BusinessVolumeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BusinessVolumeDao extends BaseDao<CmsLoanInfo,String> {
    
    public String getLoanSql(String monthDate,String startDate){
        //内部贷款
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'L' as busType,'01' as busSign, a.corp_id as corpId,a.cur_code as curId,count(*) as num ,sum(BT_NVL_NUM(a.loan_money, 0)) as money ");
        sql.append(" from cms_loan_info a ");
        sql.append(" left join cms_loan_send s  on a.bill_code = s.loan_code ");
        sql.append(" left join cms_loan_type t  on t.code = a.loan_genre  ");
        sql.append(" where 1 = 1 and s.status =102  and a.status != -2 and t.status != -2 and t.type = 1");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(s.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(s.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by a.corp_id,a.cur_code ");
        return sql.toString();
    }
    
    public String getProvideSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //银行借款
        sql.append("  select  'L' as busType,'02' as busSign, info.corp_id as corpId,info.cur_id as curId,count(*) as num ,sum(BT_NVL_NUM(send.money,0)) as money  ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join cms_provide_loan_send send on send.loan_code = info.bill_code ");
        sql.append(" left join bt_bank bank  on bank.sys_bank_code = info.bank_id ");
        sql.append(" left join cms_loan_type type  on type.code = info.pl_class");
        sql.append(" where 1 = 1 ");
        sql.append(" and send.status = 102  and info.status != -2  and type.status != -2 and type = '2' ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(send.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(send.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by info.corp_id,info.cur_id ");
        return sql.toString();
    }

    public String getPrecativeSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //委托贷款
        sql.append("  select 'L' as busType,'03' as busSign, info.loan_opp as corpId,info.cur_Id as curId, count(*) as num ,sum(BT_NVL_NUM(send.money ,0)) as money ");
        sql.append("  from cms_precative_loan_info info ");
        sql.append(" left join cms_precative_loan_send send  on info.bill_code = send.father_code ");
        sql.append(" where send.status = 102 and info.status != -2 ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(send.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(send.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by info.loan_opp,info.cur_id ");
        return sql.toString();
    }
    
    public String getDirectSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //直融
        sql.append("  select  'L' as busType,'04' as busSign,i.corp_id as corpId,i.cur_id as curId,count(*) as num ,sum(BT_NVL_NUM(i.Surplus_Money,0))  as money ");
        sql.append(" from df_direct_finance_info i ");
        sql.append(" left join df_business_type bt ");
        sql.append(" on i.df_business_type_id = bt.id ");
        sql.append(" where i.status >= 95 ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(i.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(i.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by i.corp_id,i.cur_id ");
        return sql.toString();
    }

    public String getPaymentSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //付汇
        sql.append(" select 'I' as busType,'05' as busSign,lp.corp_id as corpId,lp.cur_id as curId,count(*) as num ,sum(BT_NVL_NUM(lp.payment_money,0)) as money ");
        sql.append(" from LC_PAYMENT lp ");
        sql.append(" left join lc_enrol lc  on lp.lc_id = lc.id ");
        sql.append(" left join LC_BILL_ARRIVAL lba on lp.ARRIVAL_ID = lba.id ");
        sql.append(" where 1=1 and lp.status >= 95 ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(lp.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(lp.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by lp.corp_id,lp.cur_id ");
        return sql.toString();
    }

    public String getDepositSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //还押汇
        sql.append(" select 'I' as busType,'06' as busSign,r.corp_id as corpId,r.cur_id as curId,count(*) as num ,sum(BT_NVL_NUM(r.repay_money,0)) as money ");
        sql.append(" from LC_DEPOSIT_REPAY r ");
        sql.append(" left join LC_DEPOSIT ld ");
        sql.append(" on r.Deposit_Id = ld.id ");
        sql.append(" where 1 = 1 and r.status = 0 ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(r.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and r.create_date < to_date('"+ monthDate + "','yyyy-MM-dd')" );
        }
        sql.append(" group by r.corp_id,r.cur_id ");
        return sql.toString();
    }

    public String getPayableSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //应付出票
        sql.append(" select 'A' as busType,'07' as busSign,vv.corp_id as corpId,'4028802e5c346968015c346c18850008' as curId,count(*) num, sum(BT_NVL_NUM(v.bill_money,0)) as money ");
        sql.append(" From ab_accrecords a ");
        sql.append(" inner join  ab_voucherinfo v on a.vid=v.id ");
        sql.append(" inner join ab_voucherinfo_vice vv on vv.id=a.vvid ");
        sql.append(" where a.brief_id='001' and a.status=95 and a.opt_status='19' ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(a.business_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and a.business_date < to_date('"+ monthDate + "','yyyy-MM-dd')" );
        }
        sql.append(" group by vv.corp_id ");
        return sql.toString();
    }

    public String getReceivableSql(String monthDate,String startDate){
        StringBuilder sql = new StringBuilder();
        //应收票据
        sql.append(" select 'A' as busType,'08' as busSign,vv.corp_id as corpId,'4028802e5c346968015c346c18850008' as curId,count(*) num, sum(BT_NVL_NUM(v.bill_money,0)) as money ");
        sql.append(" From ab_accrecords a ");
        sql.append(" inner join ab_voucherinfo v on a.vid=v.id ");
        sql.append(" inner join ab_voucherinfo_vice vv on vv.id=a.vvid ");
        sql.append(" where a.brief_id in ('6001','9003','9010','9012','9018','9019','6019','6212') ");
        sql.append(" and a.status=95 and a.opt_status='19' ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(a.business_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and a.business_date < to_date('"+ monthDate + "','yyyy-MM-dd')" );
        }
        sql.append(" group by vv.corp_id ");
        return sql.toString();
    }

    /**
     * 银行存款类投资公共查询SQL
     * @param monthDate
     * @return
     */

    public String getChoBankFinanceCommonSql(String monthDate, String busType,String startDate){
        //busType-10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期
        StringBuilder sql = new StringBuilder();
        sql.append("select 'C' as busType,'"+busType+"' as busSign,f.corp_id as corpId,f.cur_id as curId, count(*) as num,sum(bt_nvl_num(f.pay_money,0)) as money");
        sql.append(" from cho_bank_finance f left join bt_bank_acc b on f.payer_acc = b.bank_acc left join bis_finance_exc e on f.id = e.cbs_bill_id");
        sql.append(" where 1 = 1 and f.bus_type = "+busType);
        if(StringUtils.isNotBlank(busType) && ("11".equals(busType) || "13".equals(busType) )){
            //定期转活期和通知转活期，离线或者不属于系统账号的审批通过就统计
            sql.append(" and f.status = 95 and ((b.is_online is null or b.is_online = '0') or (e.voucher_stat = '0' and b.is_online = '1'))");
        }else{
            sql.append(" and f.status = 95 and ((f.is_bl = 1 and b.is_online = '0') or (e.voucher_stat = '0' and b.is_online = '1'))");
        }
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(f.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(f.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by f.corp_id,f.cur_id");
        return sql.toString();
    }

    /**
     * 银行存款类投资 10-活期转定期
     * @param monthDate
     * @return
     */
    public String getChoBankFinancBusSigneHDSql(String monthDate,String startDate) {
        StringBuilder sql = new StringBuilder(getChoBankFinanceCommonSql(monthDate,"10",startDate));
        return sql.toString();
    }

    /**
     * 银行存款类投资 11-定期转活期
     * @param monthDate
     * @return
     */
    public String getChoBankFinancBusSigneDHSql(String monthDate,String startDate) {
        StringBuilder sql = new StringBuilder(getChoBankFinanceCommonSql(monthDate,"11",startDate));
        return sql.toString();
    }

    /**
     * 银行存款类投资 12-活期转通知
     * @param monthDate
     * @return
     */
    public String getChoBankFinancBusSigneHTSql(String monthDate,String startDate) {
        StringBuilder sql = new StringBuilder(getChoBankFinanceCommonSql(monthDate,"12",startDate));
        return sql.toString();
    }

    /**
     * 银行存款类投资 13-通知转活期
     * @param monthDate
     * @return
     */
    public String getChoBankFinancBusSigneTHSql(String monthDate,String startDate) {
        StringBuilder sql = new StringBuilder(getChoBankFinanceCommonSql(monthDate,"13",startDate));
        return sql.toString();
    }

    /**
     * 银行理财类投资 - 理财登记
     * @param monthDate
     * @return
     */
    public String getChoProductReqSql(String monthDate,String startDate) {
        //choProductReq-理财登记-09
        StringBuilder sql = new StringBuilder();
        sql.append("select 'C' as busType,'09' as busSign,pr.corp_Id as corpId,pr.cur_id as curId,count(*) as num,sum(bt_nvl_num(pr.buy_money,0)) as money");
        sql.append(" from cho_product_req pr where 1 = 1 and pr.status >= 95");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(pr.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(pr.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by pr.corp_id, pr.cur_id");
        return sql.toString();
    }

    /**
     * 银行理财类投资 - 理财赎回
     * @param monthDate
     * @return
     */
    public String getChoProductRedeemSql(String monthDate,String startDate) {
        //choProductRedeem-理财赎回-14
        StringBuilder sql = new StringBuilder();
        sql.append("select 'C' as busType,'14' as busSign,prd.corp_Id as corpId,pr.cur_id as curId,count(*) as num,sum(bt_nvl_num(prd.redeem_money,0)) as money");
        sql.append(" from cho_product_redeem prd left join cho_product_req pr on pr.id = prd.req_id where 1 = 1");
        sql.append(" and prd.status = 95 and pr.status = 110");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(prd.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(prd.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by prd.corp_id, pr.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类-付款办理-内转
     *
     * @param monthDate
     * @return
     */
    public String getNisBillheadNZSql(String monthDate,String startDate) {
        //05内转-18
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'18' as busSign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money");
        sql.append(" from Nis_Billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bt_bank_acc acc on n.acc_id = acc.id");
        sql.append(" where n.status <> -2 and f.sys_voucher_code not in ('49', '50') and f.voucher_type = '05' and n.status = 100");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }


    /**
     * 结算业务的分类 - 付款办理-转付(15)
     * @param monthDate
     * @return
     */
    public String getNisBillheadZFSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'15' as busSign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money");
        sql.append(" from Nis_Billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bis_exc b on n.id = b.cbs_bill_id and b.voucher_from = 1");
        sql.append(" left join bt_bank_acc acc on n.acc_id = acc.id where 1 = 1 ");
        sql.append(" and n.status <> -2 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '02'");
        /*sql.append(" and ((n.is_payed = '1' and n.status = 95) or (acc.is_online = '0' and n.status = 95) or (n.is_payed <> '1' and b.voucher_stat = '0'))");*/
        sql.append(" and ((f.is_account ='1' and n.status = 100 and n.is_payed = '1')"); //记账已付款
        sql.append(" or (f.is_account ='1' and n.is_payed <> '1'  and b.voucher_stat = '0')");//记账未付款必须指令成功
        sql.append(" or (f.is_account ='0' and n.status = 95 and n.is_payed = '1')");//不记账已付款
        sql.append(" or (f.is_account ='0' and n.is_payed <> '1' and acc.is_online = '1' and b.voucher_stat = '0')");//不记账未付款在线账号必须指令成功
        sql.append(" or (f.is_account ='0' and acc.is_online = '0' and n.status = 95))");//不记账离线账号
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 付款办理-现付(16)
     * @param monthDate
     * @return
     */
    public String getNisBillheadXFSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'16' as busSign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money");
        sql.append(" from Nis_Billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bt_bank_acc acc on n.acc_id = acc.id");
        sql.append(" where 1 = 1 and n.status <> -2 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '04'");
        sql.append(" and ((f.is_account = '0' and n.status = 95) or (f.is_account = '1' and n.status = 100))");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();

    }

    /**
     * 结算业务的分类 - 付款办理-代付(17)
     * @param monthDate
     * @return
     */
    public String getNisBillheadDFSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'17' as busSign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money");
        sql.append(" from Nis_Billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bis_exc b on n.id = b.cbs_bill_id and b.voucher_from = 1");
        sql.append(" left join bt_bank_acc acc on n.acc_id = acc.id where 1 = 1 ");
        sql.append(" and n.status <> -2 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '33'");
        /*sql.append(" and ((n.is_payed = '1' and n.status = 95) or (acc.is_online = '0' and n.status = 95) or (n.is_payed <> '1' and b.voucher_stat = '0'))");*/
        sql.append(" and ((f.is_account ='1' and n.status = 100 and n.is_payed = '1')"); //记账已付款
        sql.append(" or (f.is_account ='1' and n.is_payed <> '1' and b.voucher_stat = '0')");//记账未付款必须指令成功
        sql.append(" or (f.is_account ='0' and n.status = 95 and n.is_payed = '1')");//不记账已付款
        sql.append(" or (f.is_account ='0' and n.is_payed <> '1' and acc.is_online = '1' and b.voucher_stat = '0')");//不记账未付款在线账号必须指令成功
        sql.append(" or (f.is_account ='0' and acc.is_online = '0' and n.status = 95))");//不记账离线账号
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 付款办理-外付网银(19)
     * @param monthDate
     * @return
     */
    public String getNisBillheadWYSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'19' as busSign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money");
        sql.append(" from Nis_Billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bis_exc b on n.id = b.cbs_bill_id and b.voucher_from = 1");
        sql.append(" left join bt_bank_acc acc on n.acc_id = acc.id where 1 = 1 ");
        sql.append(" and n.status <> -2 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '34'");
        sql.append(" and ((n.is_payed = '1' and n.status = 95) or (acc.is_online = '0' and n.status = 95)");
        sql.append(" or (n.is_payed <> '1' and acc.is_online = '1' and f.is_account = '0' and n.status = 95 and b.voucher_stat = '0'))");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 付款办理-电汇(20)
     * @param monthDate
     * @return
     */
    public String getNisBillheadDHSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'20' as busSign, n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money ");
        sql.append(" from nis_billhead n left join fc_voucher_type f on n.voucher_type_id = f.id left join bt_bank_acc acc on n.acc_id = acc.id ");
        sql.append(" where 1 = 1 and n.status <> -2 and n.bill_from <> 2 and f.voucher_type = '06' and n.status = 95 ");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 付款办理-现金支票(21)
     * @param monthDate
     * @return
     */
    public String getNisBillheadXJSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'21' as busSign, n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money ");
        sql.append(" from nis_billhead n left join fc_voucher_type f on n.voucher_type_id = f.id where 1 = 1");
        sql.append(" and n.status = 95 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '62'");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 付款办理-转账支票(63)
     * @param monthDate
     * @return
     */
    public String getNisBillheadZZSql(String monthDate,String startDate) {
        //NisBillhead付款办理-02转付-15 04现付-16 33代付-17 05内转-18 34外付网银-19 06电汇-20 62现金支票-21 63转账支票-22
        StringBuilder sql = new StringBuilder();
        sql.append(" select 'N' as busType,'22' as busSign, n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.pay_money,0)) as money ");
        sql.append(" from nis_billhead n left join fc_voucher_type f on n.voucher_type_id = f.id where 1 = 1");
        sql.append(" and n.status = 95 and f.sys_voucher_code not in ('49', '50') and n.bill_from <> 2 and f.voucher_type = '63'");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }
    /**
     * 结算业务的分类 - 费用报销
     * @param monthDate
     * @return
     */
    public String getNisExpenseDetailSql(String monthDate,String startDate) {
        //expense -费用报销-23
        StringBuilder sql = new StringBuilder();
        sql.append("select 'N' as busType,'23' as busSign,");
        sql.append(" n.corp_id as corpId,b.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.expense_money,0)) as money");
        sql.append(" from nis_expense_detail n left join nis_billhead b on n.bill_id = b.id left join bis_exc e on b.id = e.cbs_bill_id and e.voucher_from = 1");
        sql.append(" left join fc_voucher_type f on b.voucher_type_id = f.id left join bt_bank_acc acc on b.payer_acc = acc.bank_acc where 1 = 1");
        sql.append(" and n.status = 100 and ((b.is_payed = '1' and b.status = 95) ");//已付款的
        sql.append(" or (acc.is_online = '0' and b.status = 95)");//离线账号
        sql.append(" or (b.is_payed <> '1' and e.voucher_stat = '0'))");//未付款交易成功
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, b.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 代发工资
     * @param monthDate
     * @return
     */
    public String getWageSql(String monthDate,String startDate) {
        //wage-代发工资-24
        StringBuilder sql = new StringBuilder();
        sql.append("select 'N' as busType,'24' as bussign,n.corp_Id as corpId,n.cur_id as curId,count(*) as num,sum(bt_nvl_num(n.wages,0)) as money");
        sql.append(" from nis_wage n left join bt_bank_acc c on n.payer_acc = c.bank_acc left join bis_bif_rule r on n.bif_code = r.id");
        sql.append(" where 1 = 1 and n.status <> -2");
        sql.append(" and (n.status = 95 and (c.is_online = '0' or r.IS_BANK_ONLINE = '0')");//银行代发离线
        sql.append(" or (c.is_online = '1' and r.IS_BANK_ONLINE = '1' and n.voucher_stat = 0))");//银行代发在线
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(n.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by n.corp_id, n.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类公共查询SQL - 资金调拨(收款)
     * @param monthDate
     * @param type
     * @return
     */
    public String getNisAllocationReqRecCommonSql(String monthDate,String type,String startDate) {
        //10-上划-25 14-上缴-28
        StringBuilder sql = new StringBuilder();
        sql.append("select 'N' as busType,");
        String joinSql = "";
        if("10".equals(type)){
            joinSql = "'25' as bussign,";
        }else {
            joinSql = "'28' as bussign,";
        }
        sql.append(joinSql);
        sql.append(" ar.corp_id as corpId,ar.cur_id as curId,count(*) as num,sum(bt_nvl_num(ar.money,0)) as money");
        sql.append(" from nis_allocation_req ar left join bis_gather_cmd g on ar.id = g.cbs_bill_id and g.voucher_From in ('5', '6')");
        sql.append(" left join bt_bank_acc acc on ar.payer_acc_id = acc.id");
        sql.append(" where 1 = 1 and ar.status <> -2 and ar.type <> 13 and g.cmd_stat = '0' ");
        sql.append(" and ar.type ='"+type+"'");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(ar.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(ar.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by ar.corp_id, ar.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类公共查询SQL - 资金调拨(付款)
     * @param monthDate
     * @param type
     * @return
     */
    public String getNisAllocationRecPayCommonSql(String monthDate,String type,String startDate) {
        //11-下拨-26 12-请款-27
        StringBuilder sql = new StringBuilder();
        sql.append("select 'N' as busType,");
        String joinSql = "";
        if("11".equals(type)){
            joinSql = "'26' as bussign,";
        }else{
            joinSql = "'27' as bussign,";
        }
        sql.append(joinSql);
        sql.append(" ar.corp_id as corpId,ar.cur_id as curId,count(*) as num,sum(bt_nvl_num(ar.money,0)) as money");
        sql.append(" from nis_allocation_req ar left join Bis_exc b on ar.id = b.cbs_bill_id and b.voucher_From in ('2', '3')");
        sql.append(" left join bt_bank_acc acc on ar.payer_acc_id = acc.id");
        sql.append(" where 1 = 1 and ar.status <> -2 and ar.type <> 13 and b.voucher_stat = '0' ");
        sql.append(" and ar.type ='"+type+"'");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(ar.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(ar.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by ar.corp_id, ar.cur_id");
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 资金调拨-上划
     * @param monthDate
     * @return
     */
    public String getNisAllocationReqSHSql(String monthDate,String startDate) {
        //10-上划-25 11-下拨-26 12-请款-27 14-上缴-28
        StringBuilder sql = new StringBuilder(getNisAllocationReqRecCommonSql(monthDate,"10",startDate));
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 资金调拨-下拨
     * @param monthDate
     * @return
     */
    public String getNisAllocationReqXBSql(String monthDate,String startDate) {
        //10-上划-25 11-下拨-26 12-请款-27 14-上缴-28
        StringBuilder sql = new StringBuilder(getNisAllocationRecPayCommonSql(monthDate,"11",startDate));
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 资金调拨-请款
     * @param monthDate
     * @return
     */
    public String getNisAllocationReqQKSql(String monthDate,String startDate) {
        //10-上划-25 11-下拨-26 12-请款-27 14-上缴-28
        StringBuilder sql = new StringBuilder(getNisAllocationRecPayCommonSql(monthDate,"12",startDate));
        return sql.toString();
    }

    /**
     * 结算业务的分类 - 资金调拨-上缴
     * @param monthDate
     * @return
     */
    public String getNisAllocationReqSJSql(String monthDate,String startDate) {
        //10-上划-25 11-下拨-26 12-请款-27 14-上缴-28
        StringBuilder sql = new StringBuilder(getNisAllocationReqRecCommonSql(monthDate,"14",startDate));
        return sql.toString();
    }

    /**
     * 外汇业务 - 外币汇款
     * @param monthDate
     * @return
     */
    public String getFexForeignReqSql(String monthDate,String startDate) {
        //29-外币汇款
        StringBuilder sql = new StringBuilder();
        sql.append("select 'F' as busType,'29' as busSign,f.corp_Id as corpId,f.cur_id as curId, count(*) as num,sum(bt_nvl_num(f.pay_money, 0)) as money");
        sql.append(" from Fex_Foreign_Req f left join bis_Foreign_exc c on f.id = c.cbs_Bill_Id left join bt_bank_acc acc on f.acc_id = acc.id");
        sql.append(" where 1 = 1 and f.status <> -2 and ((acc.is_online = '0' and f.status = '95') or (acc.is_online = '1' and c.voucher_stat = '0'))");
        if(StringUtils.isNotEmpty(monthDate.trim())){
            sql.append(" and bt_to_char(f.create_date,'yyyy-MM-dd') >= '"+ startDate+"'" );
            sql.append(" and bt_to_char(f.create_date,'yyyy-MM-dd') < '"+ monthDate+"'" );
        }
        sql.append(" group by f.corp_id, f.cur_id ");
        return sql.toString();
    }



    public DataTablesOutput getDataTable(DataTablesInput input,List<String> corpIds,String joinSql){
        Map<String, Object> inputs = input.getFormdata();
        String monthDate = (String)inputs.get("monthDate");
        Date mdate = DateUtils.parse(monthDate,"yyyy-MM-dd");
        Date sDate = DateUtils.getFirstMonthDate(mdate);
        String startDate = DateUtils.formatDate(sDate);//开始日期
        mdate = DateUtils.addDay(mdate,1);
        monthDate = DateUtils.formatDate(mdate);//结束日期

        String busType = (String)inputs.get("busType");
        List<String> typeList = new ArrayList<>();
        if(StringUtils.isNotBlank(busType)) {
            String[] typeArr = busType.split(",");
            typeList = Arrays.asList(typeArr);
        }
        String busSign = (String)inputs.get("busSign");
        StringBuilder sql = new StringBuilder();
        int i = 0;
        sql.append(" select z.busType,z.busSign,z.corpId,z.curId,z.money,z.num ");
        sql.append(" from (");
        sql.append(getSql(typeList,busSign,monthDate,startDate));
        sql.append(" ) z where 1=1 ");

        if(corpIds != null && corpIds.size() > 0){//单位
            sql.append(" and ").append(getSQLIn(corpIds, 900, "z.corpId"));
        }

        if(EmptyUtils.isNotEmpty((String)inputs.get("curId"))){
            sql.append("  and z.curId = " + "'" + (String)inputs.get("curId") + "'");
        }else{
            sql.append(joinSql);
        }
        sql.append(" order by z.corpId ");
        return getDynamicBySQL(input, sql.toString(), BusinessVolumeVO.class, null, false);
    }
    
    public List<BusinessVolumeVO> getList(String curId,List<String> corpIds,Date date,String busType,String busSign,Date sDate){
        date = DateUtils.addDay(date,1);
        String date1 = DateUtils.formatDate(date);
        String startDate = DateUtils.formatDate(sDate);//开始日期
        List<String> typeList = new ArrayList<>();
        if(StringUtils.isNotBlank(busType)) {
            String[] typeArr = busType.split(",");
            typeList = Arrays.asList(typeArr);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select z.busType,z.busSign,z.corpId,z.curId,z.money,z.num  ");
        sql.append(" from (");
        sql.append(getSql(typeList,busSign,date1,startDate));

        sql.append(" ) z where 1=1 ");
        
        if(corpIds != null && corpIds.size() > 0){//单位
            sql.append(" and ").append(getSQLIn(corpIds, 900, "z.corpId"));
        }

        if(EmptyUtils.isNotEmpty(curId)){
            sql.append("  and z.curId = " + "'" + curId + "'");
        }

        sql.append(" order by z.corpId ");
        return listBySQLAliasToBean(sql.toString(), null, BusinessVolumeVO.class);
    }

   public String getSql(List<String> typeList,String busSign,String monthDate,String startDate){
       StringBuilder sql = new StringBuilder();
       int i = 0;
       if(typeList.contains("L") || EmptyUtils.isEmptyList(typeList)){
           if("01".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               sql.append(getLoanSql(monthDate,startDate));//内部
               i=1;
           }
    
           if( "02".equals(busSign) ||  StringUtils.isEmpty(busSign.trim())) {
               if(i>0){
                   sql.append(" union all ");
               }
               sql.append(getProvideSql(monthDate,startDate));//银行
               i=1;
           }
    
           if( "03".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if(i>0){
                   sql.append(" union all ");
               }
               sql.append(getPrecativeSql(monthDate,startDate));//委托
               i=1;
           }
    
           if("04".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if(i>0){
                   sql.append(" union all ");
               }
               sql.append(getDirectSql(monthDate,startDate));//直融
               i=1;
           }
       }

       if(typeList.contains("I") || EmptyUtils.isEmptyList(typeList)) {
           if ("05".equals(busSign) ||  StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getPaymentSql(monthDate,startDate));//付汇
               i = 1;
           }

           if ("06".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getDepositSql(monthDate,startDate));//还押汇
               i = 1;
           }
       }

       if(typeList.contains("A") || EmptyUtils.isEmptyList(typeList)) {
           if ("07".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getPayableSql(monthDate,startDate));//应付出票
               i = 1;
           }

           if ("08".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getReceivableSql(monthDate,startDate));//应收票据
               i = 1;
           }
       }

       if(typeList.contains("C") || EmptyUtils.isEmptyList(typeList)) {
           if ("10".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoBankFinancBusSigneHDSql(monthDate,startDate));//活转定
               i = 1;
           }

           if ("11".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoBankFinancBusSigneDHSql(monthDate,startDate));//定期转活期
               i = 1;
           }

           if ("12".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoBankFinancBusSigneHTSql(monthDate,startDate));//活期转通知
               i = 1;
           }

           if ("13".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoBankFinancBusSigneTHSql(monthDate,startDate));//通知转活期
               i = 1;
           }

           if ("09".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoProductReqSql(monthDate,startDate));//理财登记
               i = 1;
           }

           if ("14".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getChoProductRedeemSql(monthDate,startDate));//理财赎回
               i = 1;
           }
       }

       if(typeList.contains("N") || EmptyUtils.isEmptyList(typeList)) {
           if ("18".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadNZSql(monthDate,startDate));//付款办理-内转
               i = 1;
           }

           if ( "15".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadZFSql(monthDate,startDate));//付款办理-转付
               i = 1;
           }

           if ( "16".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadXFSql(monthDate,startDate));//付款办理-现付
               i = 1;
           }

           if ( "17".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadDFSql(monthDate,startDate));//付款办理-代付
               i = 1;
           }

           if ( "19".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadWYSql(monthDate,startDate));//付款办理-外付网银
               i = 1;
           }

           if ( "20".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadDHSql(monthDate,startDate));//付款办理-电汇
               i = 1;
           }

           if ( "21".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadXJSql(monthDate,startDate));//付款办理-现金支票
               i = 1;
           }

           if ( "22".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisBillheadZZSql(monthDate,startDate));//付款办理-转账支票
               i = 1;
           }

           if ( "23".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisExpenseDetailSql(monthDate,startDate));//费用报销
               i = 1;
           }

           if ( "24".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getWageSql(monthDate,startDate));//代发工资
               i = 1;
           }

           if ( "25".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisAllocationReqSHSql(monthDate,startDate));//资金调拨-上划
               i = 1;
           }

           if ( "26".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisAllocationReqXBSql(monthDate,startDate));//资金调拨-下拨
               i = 1;
           }

           if ( "27".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisAllocationReqQKSql(monthDate,startDate));//资金调拨-请款
               i = 1;
           }

           if ( "28".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getNisAllocationReqSJSql(monthDate,startDate));//资金调拨-上缴
               i = 1;
           }
       }

       if(typeList.contains("F") || EmptyUtils.isEmptyList(typeList)) {
           if ( "29".equals(busSign) || StringUtils.isEmpty(busSign.trim())) {
               if (i > 0) {
                   sql.append(" union all ");
               }
               sql.append(getFexForeignReqSql(monthDate,startDate));//外汇业务-外币汇款
           }
       }
       return sql.toString();
   }
   
}
