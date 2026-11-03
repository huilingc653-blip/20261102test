package com.bytter.biz.finacial.interest.dao;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.interest.entity.FcInterestSingleNodBook;
import com.bytter.biz.finacial.interest.vo.FcInterestSingleNodBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class FcInterestSingleNodBookingDao extends BaseDao<FcInterestSingleNodBook, String> {
    //改为由 基础模块提供
//    public List<FcAccDTO> getNoticeAccount(String corpId, String curId) {
    public List<FcAccDTO> getNoticeAccount(List<String> accounts) {
        StringBuffer sql = new StringBuffer();
        // 账户类别：08-通知存款
        /*sql.append("select t.id as id,t.update_Date as updateDate,t.blnctr_Acc as blnctrAcc,");
        sql.append("t.acc_Name as accName,t.net_Id as netId,");
        sql.append("t.corp_Id as corpId,t.cur_Id as curId,");
        sql.append("t.subject_Id as subjectId,f.sys_subject_code as sysSubjectCode,f.subject_name as subjectName,");
        sql.append("t.acc_Type as accType,t.attribute_Id as attributeId,t.nature_Code_Id as natureCodeId,");
        sql.append("t.bank_Code as bankCode,t.Delay_Rate as DelayRate,");
        sql.append("t.balance_Way as balanceWay,t.interest_Sign as interestSign,");
        sql.append("t.interest_Start_Date as interestStartDate,t.interest_Rate as interestRate,");
        sql.append("t.overdraft_Sign as overdraftSign,");
        sql.append("t.overdraft_Rate as overdraftRate,t.overdraft_Money as overdraftMoney,");
        sql.append("t.contract_No as contractNo,t.deadline_Date as deadlineDate,");
        sql.append("t.delay_Deadline_Date as delayDeadlineDate,t.overdue_Rate as overdueRate,");
        sql.append("t.acc_For_Deduct as accForDeduct,t.deduct_Mode as deductMode,");
        sql.append("t.notify_Type as notifyType,t.acc_State as accState,");
        sql.append("t.agreed_Deposit_Sign as agreedDepositSign,t.predraw_Sign as predrawSign,");
        sql.append("t.if_Compare as ifCompare,t.acc_Id as accId,");
        sql.append("t.acc_Start_Date as accStartDate,t.operator_Code as operatorCode,");
        sql.append("t.acc_Open_Time as accOpenTime,t.handle_Charge as handleCharge,");
        sql.append("t.shortcut as shortcut,t.union_Net as unionNet,");
        sql.append("t.union_Acc as unionAcc,t.precation_Acc as precationAcc,");
        sql.append("t.precation_Corp as precationCorp,t.poundage_Rate as poundageRate,");
        sql.append("t.charge_Acc as chargeAcc,t.transact_Way as transactWay,fc.check_balance as fcBalance ");
        sql.append(" from Fc_Acc t left join fc_Subject f on t.subject_id=f.id ");
        sql.append(" left join sys_corp net on t.net_id=net.id ");
        sql.append(" left join sys_corp corp on t.corp_id=corp.id ");
        sql.append(" left join fc_online_ledger fc on fc.account = t.blnctr_acc "); // 返回
        sql.append(" left join bt_currency cur on t.cur_id=cur.id where 1=1 ");
        sql.append(" and not exists (select 1 from CHO_INNER_FINANCE b where t.blnctr_acc = b.payee_acc ) "); //blnctr_acc 和 表CHO_INNER_FINANCE 中有相同 payee_acc，则 过滤
        sql.append(" and t.acc_type = '08' ");
        Map<String, String> params = Maps.newHashMap();
        if (StringUtils.hasText(corpId)) {
            sql.append(" and t.corp_id = :corpId");
            params.put("corpId", corpId);
        }
        if (StringUtils.hasText(curId)) {
            sql.append(" and t.cur_id = :curId");
            params.put("curId", curId);
        }
        sql.append(" order by t.blnctr_acc ");*/
        sql.append(" select fc.account as blnctrAcc, fc.check_balance as fcBalance from fc_online_ledger fc where 1=1 "); // 返回
        sql.append(" and ").append(SqlUtil.getInSql(accounts, "fc.account", true));
        Map<String, String> params = Maps.newHashMap();
        return this.listBySQLAliasToBean(sql.toString(), params, FcAccDTO.class);
    }

    public DataTablesOutput dataTableBooking(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("select * from fc_interest_single_nod_book t where 1 = 1 ");
        String corpAuthSql = SqlUtil.getInSql(corpIds, "t.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        return this.dealDynamicBySQL(dataTablesInput, sql.toString(), FcInterestSingleNodBookVO.class, "t", false);
    }
    public FcInterestSingleNodBookVO getByUnion(String id) {
        StringBuffer sql = new StringBuffer();
        /*sql.append("select t.id, t.bill_no, t.corp_id, t.notice_account_id, t.notice_account, n.acc_name as notice_account_name,t.cur_id, t.book_money, t.book_date,");
        sql.append(" t.balance, t.interest_rate, t.interest_start_date, t.status, t.notice_type, t.capital_acc_id, t.capital_acc, c.acc_name as capital_acc_name, ");
        sql.append(" t.interest_acc_id, t.interest_acc, i.acc_name as interest_acc_name, t.expect_interest, t.purpose, t.remark, ");
        sql.append(" t.create_by, t.create_date, t.update_by, t.update_date from fc_interest_single_nod_book t ");
        sql.append(" left join fc_acc n on t.notice_account_id = n.id "); // n.acc_name as notice_account_name
        sql.append(" left join fc_acc c on t.capital_acc_id = c.id "); // c.acc_name as capital_acc_name
        sql.append(" left join fc_acc i on t.interest_acc_id = i.id "); // i.acc_name as interest_acc_name
        sql.append(" where t.id = :id ");*/
        sql.append("select t.id, t.bill_no, t.corp_id, t.notice_account_id, t.notice_account,t.cur_id, t.book_money, t.book_date,");
        sql.append(" t.balance, t.interest_rate, t.interest_start_date, t.status, t.notice_type, t.capital_acc_id, t.capital_acc, ");
        sql.append(" t.interest_acc_id, t.interest_acc, t.expect_interest, t.purpose, t.remark, ");
        sql.append(" t.create_by, t.create_date, t.update_by, t.update_date from fc_interest_single_nod_book t ");
        sql.append(" where t.id = :id ");
        Map<String, String> params = Maps.newHashMap();
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, FcInterestSingleNodBookVO.class);
    }
    /**
     * 根据通知存款账号获取账号相同的预约
     * @param noticeAccount
     * @return
     */
    public List<FcInterestSingleNodBookVO> listByAccount(String noticeAccount,String id){
        String hql = " from FcInterestSingleNodBook f where f.noticeAccount = :noticeAccount and f.status = 0 ";
        Map<String,Object> param = new HashMap<>();
        if(EmptyUtils.isNotEmpty(id)){
            hql += " and f.id <> :id ";
            param.put("id",id);
        }
        param.put("noticeAccount",noticeAccount);
        return listByHQLAliasToBean(hql,param,FcInterestSingleNodBookVO.class);
    }
    /**
    * <br>
    * <b>功能：</b> 更新账户预约记录为已使用 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/28 0028 <br>
    */
    public void updateAccountBookNod(String id, String account, Integer status) {
        Map<String,Object> params = new HashMap<>();
        String sql = " update fc_interest_single_nod_book f set f.status = :status where 1 = 1 ";
        if (EmptyUtils.isNotEmpty(account)) {
            sql += " and f.notice_account = :account ";
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(id)) {
            sql += " and f.id = :id ";
            params.put("id", id);
        }
        sql += " and f.status = 0 ";
        params.put("status", status);
        createSQLQuery(sql, params).executeUpdate();
    }
}
