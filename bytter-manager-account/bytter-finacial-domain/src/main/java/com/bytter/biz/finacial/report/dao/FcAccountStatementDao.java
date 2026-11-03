package com.bytter.biz.finacial.report.dao;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.report.vo.FcAccountDtlVO;
import com.bytter.biz.finacial.report.vo.FcAccountStatementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: FcAccountStatementDao
 * @ProjectName T3.3
 * @Description: TODO
 * @date 2020/6/30 14:56
 */
@Repository
public class FcAccountStatementDao extends BaseDao {
    public List<FcAccountDtlVO> getDtlData(FcAccountStatementVO statementVO){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        sql.append(" select * from ( ");
        sql.append(" select a.voucher_no          as origVoucherNo, ")
                .append(" a.tally_date          as tallyDate, ")
                .append(" a.interest_start_date as interestStartDate, ")
                .append(" a.voucher_no          as voucherNo, ")
                .append(" d.SUMMARY_CONTENT          as summaryContent, ")
                .append(" a.voucher_type        as voucherType, ")
                .append(" a.orig_voucher_no     as billNo, ")
                .append(" a.cur_code as curCode, ")
                .append(" c.blnctr_acc as account, ")
                .append(" d.debit_money        as debitMoney, ")
                .append(" d.credit_money        as creditMoney, ")
                .append(" d.balance ")
                .append(" from fc_accbook_blned a ")
                .append(" inner join fc_accbook_blned_dtl d ")
                .append(" on a.id = d.book_id ")
                .append(" inner join fc_acc c ") // 报表查询 暂不处理
                .append("  on d.account = c.blnctr_acc ")
                .append(" where c.if_compare = '1' ")//内部账号“是否对账”为“是”
                .append(" and a.voucher_state = 'N' ");//取记账复核通过的流水
        if(!EmptyUtils.isBlank(statementVO.getNetId())){
            sql.append(" and c.net_id =:netId ");
            params.put("netId",statementVO.getNetId());
        }
        sql.append(" union ")
        .append(" select e.voucher_no          as origVoucherNo, ")
                .append("  e.tally_date          as tallyDate, ")
                .append("  e.interest_start_date as interestStartDate, ")
                .append(" e.voucher_no          as voucherNo, ")
                .append(" f.SUMMARY_CONTENT                 as summaryContent, ")
                .append("  e.voucher_type        as voucherType, ")
                .append(" e.orig_voucher_no     as billNo, ")
                .append(" e.cur_code as curCode, ")
                .append(" c.blnctr_acc as account,")
                .append(" f.debit_money         as debitMoney, ")
                .append(" f.credit_money        as creditMoney, ")
                .append(" f.balance ")
                .append(" from fc_accbook e ")
                .append(" inner join fc_accbook_dtl f ")
                .append(" on e.id = f.book_id ")
                .append(" inner join fc_acc c ") // 报表查询 暂不处理
                .append(" on f.account = c.blnctr_acc ")
                .append(" where c.if_compare = '1' ")
                .append(" and e.voucher_state = 'N' ");
        if(!EmptyUtils.isBlank(statementVO.getNetId())){
            sql.append(" and c.net_id =:netId ");
            params.put("netId",statementVO.getNetId());
        }
                sql.append(" )s ")
                .append(" where s.account =:account ");
        params.put("account",statementVO.getAccount());
        if(null!=statementVO.getStartDate()){
            sql.append(" and s.tallyDate>=:startDate ");
            params.put("startDate",statementVO.getStartDate());
        }
        if(null!=statementVO.getEndDate()){
            sql.append(" and s.tallyDate<=:endDate ");
            params.put("endDate",statementVO.getEndDate());
        }
        if(!EmptyUtils.isBlank(statementVO.getVoucherType())){
            sql.append(" and s.voucherType=:voucherType ");
            params.put("voucherType",statementVO.getVoucherType());
        }
        if(!EmptyUtils.isBlank(statementVO.getSummaryContent())){
            sql.append(" and s.summaryContent like :summaryContent ");
            params.put("summaryContent","%" + statementVO.getSummaryContent() + "%");
        }
        if(!EmptyUtils.isBlank(statementVO.getCurId())){
            sql.append(" and s.curCode=:curId ");
            params.put("curId",statementVO.getCurId());
        }
        return listBySQLAliasToBean(sql.toString(),params,FcAccountDtlVO.class,null);
    }
    /**
     * 查询账户期初余额
     */
    public FcAccountStatementVO getAccountMsg(FcAccountStatementVO statementVO){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        sql.append(" select c.account, c.acc_name as accName, c.yester_balance yesterBalance, c.balance_date as balanceDate, ")
                .append(" c.cur_id as curId,c.net_id as netId ")
                .append(" from fc_acc_balance c ")
                .append(" where 1=1 ");
        if(null!=statementVO.getStartDate()){
            sql.append(" and c.balance_date >=:startDate ");
            params.put("startDate",statementVO.getStartDate());
        }
        if (!EmptyUtils.isBlank(statementVO.getAccount())) {
            sql.append(" and c.account=:account ");
            params.put("account", statementVO.getAccount());
        }
        if(!EmptyUtils.isBlank(statementVO.getCurId())){
            sql.append(" and c.cur_id=:curId ");
            params.put("curId",statementVO.getCurId());
        }
        sql.append("  order by c.balance_date ");
        List<FcAccountStatementVO> list = this.listBySQLAliasToBean(sql.toString(),params,FcAccountStatementVO.class);
        FcAccountStatementVO fcAccountStatementVO = null;
        if(EmptyUtils.isNotEmpty(list)){
            fcAccountStatementVO = list.get(0);
        }
        return fcAccountStatementVO;
    }
    public List<FcAccountStatementVO> getAccountMsgAll(FcAccountStatementVO statementVO,List<String> accounts){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        sql.append("  select m.*,t.net_id as netId from ( ")
                .append(" select ")
                .append(" c.account,c.acc_name as accName,c.yester_balance yesterBalance, ")
                .append(" c.balance_date as balanceDate,c.cur_id as curId ")
                .append(" from fc_acc_balance c where c.balance_date = (select min(balance_date) from fc_acc_balance b where c.account = b.account ")
                .append(" ");
        if(null!=statementVO.getStartDate()){
            sql.append(" and b.balance_date >=:startDate ");
            params.put("startDate",statementVO.getStartDate());
        }
        if(null!=statementVO.getEndDate()){
            sql.append(" and b.balance_date <=:endDate ");
            params.put("endDate",statementVO.getEndDate());
        }
        sql.append(" ) ");
        if (EmptyUtils.isNotEmpty(accounts)) {
            sql.append(" and ").append(getSQLIn(accounts, 900, "c.account"));
        }
        if (!EmptyUtils.isBlank(statementVO.getCurId())) {
            sql.append(" and c.cur_id=:curId ");
            params.put("curId", statementVO.getCurId());
        }
        if(null!=statementVO.getStartDate()){
            sql.append(" and c.balance_date >=:startDate ");
        }
        if(null!=statementVO.getEndDate()){
            sql.append(" and c.balance_date <=:endDate ");
        }
        sql.append("  ) m ")
                .append(" left join fc_acc t") // 报表查询 暂不处理
                .append(" on m.account = t.blnctr_acc order by t.blnctr_Acc asc, t.ACC_OPEN_TIME desc, t.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(),params,FcAccountStatementVO.class,null);
    }
    public List<FcAccDTO> getAccAll(FcAccountStatementVO statementVO, List<String> corpAuth){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<>();
        sql.append("  select c.id         as id, ")
                .append("  c.blnctr_Acc as blnctrAcc, ")
                .append("  c.net_Id     as netId, ")
                .append("  c.corp_Id    as corpId, ")
                .append("  c.cur_Id     as curId ")
                .append("  from fc_acc c ")  // 报表查询 暂不处理
                .append("  where c.blnctr_acc in ")
                .append(" (select distinct bal.account ")
                .append(" from fc_acc_balance bal ")
                .append(" where bal.balance_date >= :startDate  ")
                .append(" and bal.account in ")
                .append(" (select distinct d.account ")
                .append(" from fc_accbook_blned a ")
                .append(" left join fc_accbook_blned_dtl d ")
                .append(" on a.id = d.book_id ")
                .append(" where a.voucher_state = 'N' ")
                .append(" and a.tally_date >= :startDate  ")
                .append(" and a.tally_date <= :endDate ")
                .append(" union ")
                .append(" select distinct f.account ")
                .append(" from fc_accbook e ")
                .append(" left join fc_accbook_dtl f ")
                .append(" on e.id = f.book_id ")
                .append(" where e.voucher_state = 'N' ")
                .append(" and e.tally_date >= :startDate  ")
                .append(" and e.tally_date <= :endDate )) ");//查询日期有期初余额的数据
        params.put("startDate",statementVO.getStartDate());
        params.put("endDate",statementVO.getEndDate());
        sql.append(" and c.if_compare=1 ");
        if(EmptyUtils.isNotEmpty(corpAuth)){
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "c.corp_Id"));
        }
        if(!EmptyUtils.isBlank(statementVO.getCorpId())){
            sql.append(" and c.corp_Id=:corpId ");
            params.put("corpId",statementVO.getCorpId());
        }
        if(!EmptyUtils.isBlank(statementVO.getCurId())){
            sql.append(" and c.cur_id=:curId ");
            params.put("curId",statementVO.getCurId());
        }
        if(!EmptyUtils.isBlank(statementVO.getAccount())){
            sql.append(" and c.blnctr_Acc=:account ");
            params.put("account",statementVO.getAccount());
        }
                sql.append("  order by c.blnctr_Acc asc, c.ACC_OPEN_TIME desc, c.CREATE_DATE ");
        return listBySQLAliasToBean(sql.toString(),params,FcAccDTO.class,null);
    }
}
