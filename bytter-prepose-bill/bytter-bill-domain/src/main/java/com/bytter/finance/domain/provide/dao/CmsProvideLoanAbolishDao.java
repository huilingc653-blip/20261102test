package com.bytter.finance.domain.provide.dao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanAbolish;
import com.bytter.finance.domain.provide.vo.LoanAbolishVO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: CMS_PROVIDE_LOAN_ABOLISH
 * @author onlineGenerator
 * @date 2017-08-15 15:39:47
 * @version 1.0
 *
 */
@Repository
public class CmsProvideLoanAbolishDao extends BaseDao<CmsProvideLoanAbolish, String> {
    /**
     * 功能介绍：借款作废列表<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput<LoanAbolishVO> queryTable(DataTablesInput input, String userLoanName, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id as id,i.bill_code as billCode,i.contract_no as contractNo, i.pl_account as plAccount,");
        sql.append("i.create_date as infoCreateDate,i.cur_id as curId, i.pl_money as plMoney,BT_NVL_NUM(send.sendMoney,0) as money,");
        sql.append("i.pl_date as plDate,i.repay_date as repayDate, i.repay_type as repayType,ctype.REPAY_NAME as  repayTypeName1,i.pl_class as plClass,i.pl_type as plType,");
        sql.append("i.pl_term as plTerm,i.tern_unit as ternUnit,type.name as plClassName,i.status as status,BT_NVL_CHAR(a.biz_status,i.biz_status) as bizStatus,");
        //TODO 补充名称信息
        sql.append("i.bank_name as bankName,i.corp_name as corpName,i.cur_name as curName ");
        sql.append(" from cms_provide_loan_info i");
        sql.append(" left join CMS_REPAY_TYPE ctype on i.REPAY_TYPE=ctype.id");
        sql.append(" left join cms_provide_loan_abolish a on a.pli_code = i.bill_code");
        sql.append(" left join cms_loan_type type on type.code = i.pl_class and type.status!=-2 and type.type='" + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(" left join (select send.loan_code,BT_NVL_NUM(sum(send.money),0) sendMoney from cms_provide_loan_send send where send.status >=102 group by send.loan_code) send");
        sql.append(" on i.bill_code = send.loan_code");
        sql.append(" where i.status >= -1 and i.status <= 96 and i.status not in (11,21)");// 未发放的才允许作废 不在待审批状态
        sql.append(" and (a.status is null or a.status = -1) ");
        sql.append(" and not exists(select 1 from cms_provide_loan_send se where se.loan_code = i.bill_code and se.status != -2)");
        sql.append(" and i.create_by = '" + userLoanName + "'");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        return getDynamicBySQL(input, sql.toString(), LoanAbolishVO.class, null, false);
    }
    /**
     * 功能介绍：借款作废审批列表<br>
     */
    public DataTablesOutput getListDataAbolishApproveTable(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id as id,i.bill_code as billCode,i.contract_no as contractNo, i.pl_account as plAccount,");
        sql.append(
                "i.create_date as infoCreateDate,i.cur_id as curId, i.pl_money,BT_NVL_NUM(s.sendMoney,0) as money,");
        //TODO 补充名称信息
        sql.append(
                "i.bank_name as bankName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                " i.pl_date as plDate,i.repay_date as repayDate, i.repay_type as repayType, ctype.REPAY_NAME as repayTypeName1, i.pl_class as plClass,");
        sql.append(
                "i.pl_type as plType,i.pl_term as plTerm,i.tern_unit as ternUnit,type.name as plClassName,a.biz_status as bizStatus");
        sql.append(" from cms_provide_loan_abolish a ");
        sql.append(" left join cms_provide_loan_info i on i.bill_code = a.pli_code");
        sql.append(" left join CMS_REPAY_TYPE ctype on i.REPAY_TYPE = ctype.id");
//        sql.append(" left join bt_bank b  on b.sys_bank_code = i.bank_id");
//        sql.append(" left join sys_corp s  on s.id = i.corp_id ");
        sql.append(" left join cms_loan_type type on type.code = i.pl_class and type.status!=-2 and type.type='"
                + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(
                " left join (select s.loan_code,BT_NVL_NUM(sum(s.money),0)sendMoney from cms_provide_loan_send s where s.status >=102 group by s.loan_code) s");
        sql.append(" on i.bill_code = s.loan_code");
        sql.append(" where 1 = 1");
        sql.append(" and a.status != 110 and a.status != 3");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "a.id"));
        }
        return getDynamicBySQL(input, sql.toString(), LoanAbolishVO.class, null, false);
    }
    /**
     * 功能介绍：修改info表状态<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public int updateInfoStatus(Integer status, String infoBillCode) {
        StringBuilder hql = new StringBuilder();
        hql.append("update CmsProvideLoanInfo set status =:status where billCode =:infoBillCode");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("infoBillCode", infoBillCode);
        return batchExecute(hql.toString(), map);
    }
    /**
     * 功能介绍：借款展期申请保存时更改发放表状态<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public int updateSendStatus(Integer status, String sendLoanCode) {
        StringBuilder hql = new StringBuilder();
        hql.append("update ProvideLoanSend set status =:status where loanCode =:sendLoanCode");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("sendLoanCode", sendLoanCode);
        return batchExecute(hql.toString(), map);
    }
    /**
     * 功能介绍：借款作废办理列表<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput queryStartTable(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.id as id,i.bill_code as billCode,i.corp_id as corpId,i.corp_name as corpName,i.contract_no as contractNo, i.con_id as conId,BT_NVL_NUM(i.pl_money,0) as plMoney,");
        //TODO 名称补充
        sql.append("pls.money as sendMoney, i.bank_name as bankName,i.cur_id as curId,i.cur_name as curName, pls.cur_code as sendCurCode,pls.cur_name as sendCurName, i.pl_date as plDate,");
        sql.append("  i.repay_date as repayDate,i.pl_term as plTerm,i.tern_unit as ternUnit ,a.abolished_date as abolishedDate, a.status as status,a.biz_status as bizStatus   ");
        sql.append(" from cms_provide_loan_abolish a");
        sql.append(" left join cms_provide_loan_info i  on a.pli_code = i.bill_code ");
        sql.append(" left join (select loan_code,cur_code,cur_name,sum(money) as money from cms_provide_loan_send group by loan_code,cur_code,cur_name) pls on i.bill_code = pls.loan_code ");
        // sql.append(" where a.status = 95");
        sql.append(" where a.status != -1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        return getDynamicBySQL(input, sql.toString(), LoanAbolishVO.class, null, false);
    }
    /**
     * 功能介绍：根据infobillCode查询abolish对象<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public CmsProvideLoanAbolish getLoanAbolishByBillCode(String pli_code) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from CmsProvideLoanAbolish where pli_code=:pli_code";
        params.put("pli_code", pli_code);
        return (CmsProvideLoanAbolish) createQuery(hql, params).uniqueResult();
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from cms_provide_loan_abolish where corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(bizStatus)) {
            sql.append(" and BIZ_STATUS=:bizStatus");
            params.put("bizStatus", bizStatus);
        }
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in(:status) ");
            params.put("status", status.split(","));
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
