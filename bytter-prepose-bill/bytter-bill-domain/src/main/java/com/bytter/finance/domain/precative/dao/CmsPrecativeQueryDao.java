package com.bytter.finance.domain.precative.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.finance.domain.loan.vo.CmsLoanQueryVO;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.domain.precative.vo.PrecativeQueryVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
/**
 * @Title: Dao
 * @Description: 委托贷款查询表
 * @author
 * @date 2017-07-20 14:00:39
 * @version 1.0
 *
 */
@Repository
public class CmsPrecativeQueryDao extends BaseDao<PrecativeQueryVo, String> {
    /***
     * 查询委托贷款数据
     */
    public DataTablesOutput dataTableQuery(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select info.bill_code as infoBillCode , info.precative_id as precativeId , info.poundage_rate as poundageRate , info.loan_opp as loanOpp, ");
        sb.append(
                " info.cur_Id as curId , info.term as term , info.tern_Unit as ternUnit , send.bill_code as sendBillCode , info.pl_type as plType , ");
        sb.append(
                " info.money as infoMoney , send.money as sendMoney , send.father_code as fatherCode, info.status as infoStatus , send.status as sendStatus , info.rmk as infoRmk , ");
        sb.append(
                " info.start_date as startDate , send.send_date as sendDate , info.end_date as endDate , send.deadline_date as deadlineDate , info.contract_code as infoContractCode , ");
        //TODO 补充名称相关信息
        sb.append(" info.cur_name as curName,info.precative_name as precativeName,info.loanopp_name as loanOppName, ");
        sb.append(
                " send.contract_code as sendContractCode , info.rate as rate , send.repay_type as repayType , info.con_id as conId,");
        sb.append(
                " repay.repayMoney as repayMoney , away_repay.passageMoney as passageMoney , info.biz_status as infoBizStatus,send.biz_status as sendBizStatus , BT_NVL_NUM(deffers.deferCount,0) as counts ,");
        // sb.append(" case when send.biz_status is null then info.biz_status else send.biz_status end as status ");
        sb.append(" BT_NVL_CHAR(send.biz_status,info.biz_status) as status");
        sb.append(" from cms_precative_loan_info info ");
        sb.append(" left join cms_precative_loan_send send  on info.bill_code = send.father_code ");
        // sb.append(" left join cms_precative_loan_repay repays on send.bill_code = repays.send_code ");
        sb.append(
                " left join (select BT_NVL_NUM(sum(plr.repay_money), 0) repayMoney, plr.send_code  from cms_precative_loan_repay plr left join cms_precative_loan_send pls on plr.send_code = pls.bill_code where 1 = 1 ");
        sb.append(
                " and plr.status >= 102 and BT_NVL(plr.repay_type,'sss') <> '4' group by plr.send_code) repay on repay.send_code = send.bill_code ");
        sb.append(
                " left join (select BT_NVL_NUM(sum(plr.repay_money), 0) passageMoney, plr.send_code from cms_precative_loan_repay plr left join  cms_precative_loan_send pls on plr.send_code = pls.bill_code where 1 = 1 ");
        sb.append(
                " and ((plr.status != -2 ) and  plr.status < 102)  group by plr.send_code) away_repay on away_repay.send_code = send.bill_code ");
        sb.append(
                " left join (select count(defer.li_code) deferCount, defer.li_code  from cms_precative_loan_deferred defer  left join cms_precative_loan_send pls ");
        sb.append(
                " on defer.li_code = pls.bill_code  where 1 = 1  and (defer.status != -2)  group by defer.li_code) deffers on deffers.li_code = send.bill_code ");
        sb.append(" where info.status != -2 ");
        sb.append(" and (").append(getSQLIn(corpAuth, 900, "info.precative_id"));
        sb.append(" or ").append(getSQLIn(corpAuth, 900, "info.loan_opp"));
        sb.append(" ) ");
        return getDynamicBySQL(input, sb.toString(), PrecativeQueryVo.class, "", false);
    }
}
