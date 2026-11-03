package com.bytter.report.domain.cms.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.report.domain.cms.vo.ProvideCapitalizeVO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 借款发放表
 * @date 2017-07-20 10:45:09
 */
@Repository
public class CmsProvideQueryDao extends BaseDao {
    /**
     * <br>
     * <b>功能：</b> 查询银行借款融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/6 0006 <br>
     */
    public List<ProvideCapitalizeVO> queryLoanCapitalize(List<String> corpAuth, String curId, Date calcDate, List<String> corpIds, Date startRepayDate, Date endRepayDate, Date startBorrowDate, Date endReBorrowDate, String bankTypeId, String plClass, List<String> plType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.id as infoId,send.bill_code as sendBillCode, ");
        sql.append(" bt.bank_type as bankType, bt.type_name as bankTypeName,ibi.bank_name as bankName, ");
        sql.append(" corp.name as corpName,cur.cur_name as curName,info.cur_id as curId, ");
        sql.append(" info.corp_id as corpId,corp.parent_id as superCorpId,corp.net_id as netId, ");
        sql.append(" corp.cur_id as natureCur,send.send_date as sendDate, ");
        sql.append(" send.repay_date as repayDate,info.IS_HANG_GUARANTEE as hangGuarantee, ");
        sql.append(" type.name as plClassName,send.pl_rate as plRate, ");
        sql.append(" send.money as sendMoney, (BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0) ) as leftMoney, ");
        sql.append(" info.bailmoney_rate as bailRate, info.bailmoney as bailMoney, info.rmk as rmk, ");
        sql.append(" rb.money as rationMoney ,rb.begindate as rationBegindate,rb.enddate as rationEnddate ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info ");
        sql.append(" on send.loan_code = info.bill_code and send.status >= 95 and send.status <= 110 ");
        sql.append(" left join (select cr.send_code, ");
        sql.append(" sum(cr.repay_money) as sum_repay_money ");
        sql.append("   from cms_provide_loan_repay cr ");
        sql.append("   where cr.status >= 102 ");
        if(!EmptyUtils.isEmpty(calcDate)) {
            sql.append(" and cr.repay_date <= bt_to_date('").append(DateUtils.formatDate(calcDate)).append("','yyyy-MM-dd') ");
        }
        sql.append("   group by cr.send_code) repay on repay.send_code = send.bill_code ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class ");
        sql.append(" inner join bt_currency cur on cur.id = info.cur_id ");
        sql.append(" inner join sys_corp corp on corp.id = info.corp_id ");
        sql.append(" inner join BT_INPUT_BANK_INFO ibi on ibi.sys_bank_code = info.bank_id ");
        sql.append(" left JOIN BT_BANK_TYPE bt ON ibi.BANK_PREFIX = bt.BANK_PREFIX ");
        sql.append(" and (bt.bank_name_Prefix is null or ibi.bank_name like bt_concat3('%', bt.bank_name_Prefix, '%')) ");
        sql.append(" left join rat_uration ru on info.id = ru.bill_id ");
        sql.append(" left join rat_bkration rb on ru.rat_code = rb.id ");
        sql.append(" where send.status >= 102 and type.type = '2'  AND type.status != - 2 ");
//        sql.append(" and BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0) > 0 ");
        sql.append(" and BT_NVL_NUM(send.money, 0) - (CASE WHEN repay.sum_repay_money IS NULL THEN 0  ELSE repay.sum_repay_money END ) > 0 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if(EmptyUtils.isNotEmpty(corpIds)){
            sql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
        }
        Map<String, Object> params = new HashMap<>();
        if(!EmptyUtils.isEmpty(calcDate)) {
            sql.append(" and send.send_date <= bt_to_date('").append(DateUtils.formatDate(calcDate)).append("','yyyy-MM-dd') ");
        }
        if(EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and cur.id = :curId ");
            params.put("curId", curId);
        }
        sql.append(" and  send.repay_date >= :sysdate ");
        params.put("sysdate", new Date());
        if(!EmptyUtils.isEmpty(startRepayDate)) {
            sql.append(" and send.repay_date >= bt_to_date('").append(DateUtils.formatDate(startRepayDate)).append("','yyyy-MM-dd') ");
        }
        if(!EmptyUtils.isEmpty(endRepayDate)) {
            sql.append(" and send.repay_date <= bt_to_date('").append(DateUtils.formatDate(endRepayDate)).append("','yyyy-MM-dd') ");
        }
        if(!EmptyUtils.isEmpty(startBorrowDate)) {
            sql.append(" and send.send_date >= bt_to_date('").append(DateUtils.formatDate(startBorrowDate)).append("','yyyy-MM-dd') ");
        }
        if(!EmptyUtils.isEmpty(endRepayDate)) {
            sql.append(" and send.send_date <= bt_to_date('").append(DateUtils.formatDate(endReBorrowDate)).append("','yyyy-MM-dd') ");
        }
        if(EmptyUtils.isNotEmpty(bankTypeId)) {
            sql.append(" and bt.id = :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(EmptyUtils.isNotEmpty(plClass)) {
            sql.append(" and info.pl_class = :plClass ");
            params.put("plClass", plClass);
        }
        if(EmptyUtils.isNotEmpty(plType)) {
            sql.append("            and ( ");
            sql.append("                  (info.IS_HANG_GUARANTEE = '1' ");
            sql.append("                      and info.ID in (select distinct guaCon.BUSINESS_ID ");
            sql.append("                                    from GUA_CONTRACT_OCCUP guaCon ");
            sql.append("                                             left join GUA_CONTRACT_WAY guaWay on guaCon.CONTRACT_ID = guaWay.GUA_ID ");
            sql.append("                                    where 1 = 1 ");
            sql.append("                                          and (" + getSQLIn(plType, 900, "guaWay.way_codes") + ")");
            sql.append("                      ) ");
            sql.append("                      ) ");
            sql.append("                  or ");
            sql.append("                  (info.IS_HANG_CREDIT = '1' ");
            sql.append("                      and info.ID in (select distinct ru.BILL_ID ");
            sql.append("                                    from RAT_URATION ru ");
            sql.append("                                             left join RAT_BKRATION rb on ru.RAT_CODE = rb.ID ");
            sql.append("                                             left join GUA_CONTRACT_OCCUP guaCon on rb.ID = guaCon.BUSINESS_ID ");
            sql.append("                                             left join GUA_CONTRACT_WAY guaWay on guaCon.CONTRACT_ID = guaWay.GUA_ID ");
            sql.append("                                    where 1 = 1 ");
            sql.append("                                      and ru.SYSCODE = 'lcs' ");
            sql.append("                                      and (" + getSQLIn(plType, 900, "guaWay.way_codes") + ")");
            sql.append("                                      and rb.IS_HANG_GUARANTEE = '1') ");
            sql.append("                      ) ");
            sql.append("              ) ");
        }
        sql.append(" order by info.corp_id,bt.bank_type,send.send_date,send.repay_date ");
        return listBySQLAliasToBean(sql.toString(), params, ProvideCapitalizeVO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询发放数据所有的还款计划明细 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/10 0010 <br>
     */
    public List<ProvideCapitalizeVO> queryAllLoanCapitalize(List<String> loanIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select info.id as infoId,send.bill_code as sendBillCode,crs.id as repayMentId, ");
        sql.append(" send.send_date as sendDate, crs.predice_repay_date as repayDate, ");
        sql.append(" crs.repayment_money as sendMoney, ");
        sql.append(" info.bailmoney_rate as bailRate, info.bailmoney as bailMoney ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info ");
        sql.append(" on send.loan_code = info.bill_code and send.status >= 95 and send.status <= 110 ");
        sql.append(" inner join cms_repayment_schedule cr on cr.bill_no = info.id and cr.cms_type = 'B1' ");
        sql.append(" inner join cms_repayment_scheduledetailed crs on crs.relation_id = cr.id and (crs.valid_sign <> '-2' or crs.valid_sign is null) ");
        sql.append(" where 1 = 1 ");
        sql.append(" and ").append(getSQLIn(loanIds, 900, "info.id"));
        sql.append(" order by info.id,crs.predice_repay_date ");
        return listBySQLAliasToBean(sql.toString(), params, ProvideCapitalizeVO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询指定日期前累计还款金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/6 0006 <br>
     */
    public List<CmsLoanRepayDTO> getRepayedSumByDate(List<String> sendCodes, Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select repay.send_code as sendCode, sum(repay.repay_money) as repayMoney ");
        sql.append(" from cms_provide_loan_repay repay ");
        sql.append(" where repay.status >= :status and repay.status <> -2 and repay.repay_money > 0 ");
        sql.append(" and repay.repay_date <= :endDate ");
        sql.append(" and ").append(getSQLIn(sendCodes, 900, "repay.send_code"));
        sql.append(" group by repay.send_code ");
        params.put("status", CmsConstant.APPROVED);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanRepayDTO.class);
    }
}
