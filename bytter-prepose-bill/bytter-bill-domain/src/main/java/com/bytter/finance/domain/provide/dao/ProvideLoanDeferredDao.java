package com.bytter.finance.domain.provide.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.entity.ProvideLoanDeferred;
import com.bytter.finance.domain.provide.vo.ProvideLoanDeferredVO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhengxn
 * @version 1.0
 * @Title: 借款展期申请Dao
 * @Description: CMS_PROVIDE_LOAN_SEND
 * @date 2017-07-24 14:13:13
 */
@Repository
public class ProvideLoanDeferredDao extends BaseDao<ProvideLoanDeferred, String> {

    /***
     * 借款展期申请列表
     *
     * @param input 入参
     * @param LoanDfNum 是否
     * @param corpAuth 单位权限
     * @return 出参
     */
    public DataTablesOutput queryAll(DataTablesInput input, boolean LoanDfNum, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.corp_id as corpId, s.corp_name as corpName, i.bill_code as billCode, s.bill_code as provideCode, s.contract_code as sendContractCode,");
        sql.append("s.create_date as createDate,i.bank_name as bankName,i.cur_id as curId,i.cur_name as curName,type.name as plClassName,");
        sql.append("i.pl_money as plMoney,s.money as money,i.pl_date as plDate,s.repay_date as repayDate,i.repay_type as repayType, ctype.REPAY_NAME as repayTypeName1,");
        sql.append("i.pl_type as plType,i.pl_term as plTerm,i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus,");
        sql.append(" deferreds.mature_date as deferredStartDate,");// 上次展期开始日期
        sql.append(" deferreds.deferred_repay_date as deferredRepayDate ");// 上次展期到期日期
        sql.append(" from cms_provide_loan_send s ");
        sql.append(" left join cms_provide_loan_info i ");
        sql.append(" on s.loan_code = i.bill_code ");
        sql.append(" left join CMS_REPAY_TYPE ctype on i.REPAY_TYPE=ctype.id");
        sql.append(" left join cms_loan_type type on type.code = i.pl_class and type.status <> -2 and type.type='" + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(" left join (select def.li_code,max(def.mature_date) as mature_date ,max(def.deferred_repay_date) as deferred_repay_date from ");
        sql.append(" cms_provide_loan_deferred def where def.status <> -2 group by def.li_code) deferreds on deferreds.li_code = s.bill_code");
        if (LoanDfNum) {
            sql.append(" where s.status in(102,104) and s.biz_status <> " + CmsConstant.BUSINESSSTATE_48 + " and i.status > -1 and i.status < 110");
            sql.append(" and s.money > (select bt_nvl(sum(repay_money), 0) ");
            sql.append(" from cms_provide_loan_repay plr ");
            sql.append(" where plr.send_code = s.bill_code ");
            sql.append(" and plr.status >= -1) ");
        } else {
            sql.append(" where s.status=" + CmsConstant.COMPELETOTAL + " and s.biz_status <> " + CmsConstant.BUSINESSSTATE_48 + " and i.status > -1  and i.status < 110");// 记账完成
        }
        sql.append(" and ((deferreds.deferred_repay_date is null and s.repay_date >= BT_TO_DATE('" + DateUtils.formatDate() + "','yyyy-MM-dd')) or deferreds.deferred_repay_date >= BT_TO_DATE('" + DateUtils.formatDate() + "','yyyy-MM-dd'))");// 预期不能展期
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "s.corp_id"));
        return getDynamicBySQL(input, sql.toString(), ProvideLoanDeferredVO.class, "", false);
    }

    /**
     * 功能介绍 :通过id查询展期申请页面显示内容<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public ProvideLoanDeferredVO application(String sendBillCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select i.corp_id as corpId,i.bank_name as bankName,i.pl_class as plClass,i.bill_code as billCode,s.contract_code as sendContractCode,");
        sql.append("s.loan_acc as loanAcc,i.pl_account as plAccount,i.cur_id as curId,i.pl_money as plMoney,s.bill_code as provideCode,s.loan_code as loanCode,");
        sql.append("s.money as money,s.send_date as sendDate,s.repay_date as repayDate,s.create_date as createDate, drd.maxDate as deferredRepayDate,");
        sql.append("i.corp_name as corpName,i.cur_name as curName,");
        sql.append("(select BT_NVL_NUM(sum(r.repay_money),0) from cms_provide_loan_repay r,cms_provide_loan_send s where r.send_code = s.bill_code and r.status=:status) as repayMoney ");
        sql.append(" from cms_provide_loan_send s ");
        sql.append(" left join cms_provide_loan_info i ");
        sql.append(" on s.loan_code = i.bill_code ");
        sql.append(" left join (select db.li_code, max(db.deferred_repay_date) as maxDate");
        sql.append(" from cms_provide_loan_deferred db  where db.status = 110 group by db.li_code) drd");
        sql.append(" on drd.li_code = s.bill_code");
        sql.append(" where s.bill_code=:billCode");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", sendBillCode);
        param.put("status", CmsConstant.COMPELETOTAL);// 102
        return getBySQLAliasToBean(sql.toString(), param, ProvideLoanDeferredVO.class);
    }

    /**
     * 查询在途金额
     **/
    public ProvideLoanDeferredVO queryPassageMoney(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select BT_NVL_NUM(sum(r.repay_money),0) as passageMoney from cms_provide_loan_repay r,cms_provide_loan_send s ");
        sql.append("where r.send_code = s.bill_code and r.status>=11 and r.status<=21 and s.bill_code=:billCode");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", billCode);
        return getBySQLAliasToBean(sql.toString(), param, ProvideLoanDeferredVO.class);
    }

    /**
     * 功能介绍：借款展期补录/修改/打回列表<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>更新者：</b>zhengxn<br>
     * <b>日期：</b> 2017-5-1 <br>
     * <b>更新内容：</b> 修改XXXX <br>
     */
    public DataTablesOutput queryTable(DataTablesInput input, String pageType, String createName,
                                       List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select s.corp_id as corpId,s.corp_name as corpName,i.bill_code as billCode,s.bill_code as provideCode,d.id as deferredId,d.bill_code as deferredCode,");
        sql.append("d.create_date as createDate,s.repay_date as repayDate,d.deferred_repay_date as deferredRepayDate,");
        sql.append("d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,d.status as provideStatus,d.biz_status as bizStatus");
        sql.append(" from cms_provide_loan_deferred d ");
        sql.append(" left join cms_provide_loan_send s on d.li_code = s.bill_code");
        sql.append(" left join cms_provide_loan_info i on i.bill_code = s.loan_code");
        sql.append(" where 1=1 ");
        if ("listAddItional".equals(pageType)) {
            sql.append("and d.status =" + CmsConstant.APPROVED);// 95审核通过
        }
        if ("listUpdate".equals(pageType)) {
            sql.append("and d.status <=" + CmsConstant.TEMP + " and d.status >=" + CmsConstant.BACK);// 暂存，打回
            sql.append(" and d.create_by = '" + createName + "'");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "s.corp_id"));
        return getDynamicBySQL(input, sql.toString(), ProvideLoanDeferredVO.class, "", false);
    }

    /**
     * 功能介绍：借款展期处理列表<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>更新者：</b>zhengxn<br>
     * <b>日期：</b> 2017-5-1 <br>
     * <b>更新内容：</b> 修改XXXX <br>
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryHandle(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();

        sql.append("select s.bill_code as provideCode,d.id as deferredId,i.bank_name as bankName,i.net_id as netId,");
        sql.append("s.loan_acc as loanAcc,i.corp_id as corpId,i.corp_name as corpName,i.cur_id as curId,i.cur_name as curName,i.bank_id as bankId,i.pl_money as plMoney,s.money as money,");
        sql.append("i.pl_term as plTerm,i.pl_date as plDate,s.repay_date as repayDate,d.deferred_repay_date as deferredRepayDate,d.bill_code as deferredCode,");
        sql.append("d.deferred_limt as deferredLimt,d.create_name as createName,d.create_date as agencyDate,i.tern_unit as ternUnit,d.contract_code as contractCode");
        sql.append(" from cms_provide_loan_deferred d ");
        sql.append("left join cms_provide_loan_send s ");
        sql.append("on d.li_code = s.bill_code ");
        sql.append("left join cms_provide_loan_info i ");
        sql.append(" on s.loan_code = i.bill_code ");
        sql.append("where d.status =" + CmsConstant.COMPELTEXTENDATE);// 104展期申请成功 或者 租金处理完成
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "s.corp_id"));
        return getDynamicBySQL(input, sql.toString(), ProvideLoanDeferredVO.class, "", false);
    }

    /**
     * 功能介绍：借款展期申请保存时更改发放表状态<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public int updateSend(Integer status, String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ProvideLoanSend set status =:status where billCode =:billCode");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("billCode", billCode);
        return batchExecute(sql.toString(), map);
    }

    /**
     * 功能介绍：借款展期修改删除<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn <b>日期：</b> 2017-5-1 <br>
     */
    public int deleteDef(Integer status, String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ProvideLoanDeferred set status =:status where id =:id");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("id", id);
        return batchExecute(sql.toString(), map);
    }

    /**
     * 功能介绍：验证展期合同号是否重复<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public boolean verifyContractCode(String contractCode, String billCode) {
        Map<String, Object> param = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select 1 from cms_provide_loan_deferred where contract_code =:contractCode");
        if (!EmptyUtils.isEmpty(billCode)) {
            sql.append(" and bill_code !=:billCode ");
            param.put("billCode", billCode);
        }
        param.put("contractCode", contractCode);
        List list = listBySQL(sql.toString(), param);
        return list == null || list.size() <= 0;
    }

    /**
     * 审核列表查询
     **/
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select i.*,sc.name as corpName from cms_provide_loan_deferred i ");
        sb.append(" left join sys_corp sc on sc.id = i.CORP_ID ");
        sb.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sb.append(" and ").append(getSQLIn(ids, 900, "i.id"));
        }
        return getDynamicBySQL(input, sb.toString(), ProvideLoanDeferredVO.class, "", false);
    }

    /**
     * <b>功能介绍：</b>获取展期次数<br>
     * <b>日期：</b>2017年9月18日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    public Integer getProvideLoanSendCountByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select count(1) sendCount from cms_provide_loan_deferred d left join cms_provide_loan_send s on d.li_code = s.bill_code where s.bill_code = :billCode and d.status <> -2 ");
        params.put("billCode", billCode);
        java.math.BigDecimal sendCount = (java.math.BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return sendCount.intValue();
    }

    /**
     * <b>功能介绍：</b>获取展期次数明细列表<br>
     * <b>日期：</b>2017年9月27日<br>
     * <b>作者：</b>zhengxn<br>
     *
     * @version 1.0
     */
    public List<ProvideLoanDeferredVO> getProvideLoanSendListByBillCode(String liCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.corp_id as corpId, d.bill_code as billCode,d.li_code as liCode,d.contract_code as contractCode,d.mature_date as matureDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,");
        sql.append(" i.corp_name as corpName, ");
        sql.append(" d.deferred_repay_date as deferredRepayDate,d.deferred_rate as deferredRate,d.biz_status as bizStatus");
        sql.append("  from cms_provide_loan_deferred d ");
        sql.append("  left join cms_provide_loan_send s");
        sql.append("  on d.li_code = s.bill_code ");
        //TODO 补充单位信息
        sql.append("  left join cms_provide_loan_info i on i.bill_code = s.loan_code ");
        // sql.append(" where d.li_Code =:liCode and d.biz_status = 65 order By d.create_date desc");
        sql.append("  where d.li_Code =:liCode and d.status >= -1 order By d.create_date desc");// 改为取展期所有数据，删除状态数据除外
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("liCode", liCode);
        return listBySQLAliasToBean(sql.toString(), param, ProvideLoanDeferredVO.class);
    }

    /**
     * <b>功能介绍：</b>根据send_Code获取所有已展期处理完成的数据<br>
     * <b>日期：</b>2023年5月6日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public List<ProvideLoanDeferredVO> getFinishDeferredListByBillCode(String sendCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.corp_id as corpId, d.bill_code as billCode,d.li_code as liCode,d.contract_code as contractCode,d.mature_date as matureDate,d.deferred_limt as deferredLimt,d.tern_unit as ternUnit,");
        sql.append(" d.deferred_repay_date as deferredRepayDate,d.deferred_rate as deferredRate,d.biz_status as bizStatus");
        sql.append("  from cms_provide_loan_deferred d ");
        sql.append("  left join cms_provide_loan_send s");
        sql.append("  on d.li_code = s.bill_code ");
        sql.append("  where d.li_Code =:sendCode and d.status = " + CmsConstant.FINISH);
        sql.append(" order by d.create_date");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("sendCode", sendCode);

        return listBySQLAliasToBean(sql.toString(), param, ProvideLoanDeferredVO.class);
    }

    /**
     * 查询最大的展期日期 根据send_Code
     *
     * @return
     */
    public Date getMaxDeferredDate(String sendCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select max(deferred_repay_date) as deferredRepayDate from cms_provide_loan_deferred where li_code = :sendCode and status <> -2 ");
        params.put("sendCode", sendCode);
        Date deferredRepayDate = (Date) createSQLQuery(sql.toString(), params).uniqueResult();
        return deferredRepayDate;
    }

    /**
     * 根据发放表billcode获取展期表最后一次展期日期及展期利率
     */
    public ProvideLoanDeferred getDeferredBySendBillCode(String sendBillCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_provide_loan_deferred i,");
        sql.append(" (select loan.li_code as liCode, max(loan.deferred_repay_date) as deferredRepayDate ");
        sql.append(" from cms_provide_loan_deferred loan  where li_code = :sendBillCode  and status <> -2 group by loan.li_code) a ");
        sql.append(" where i.deferred_repay_date = a.deferredRepayDate  and a.liCode = i.li_code ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("sendBillCode", sendBillCode);
        return getBySQLAliasToBean(sql.toString(), params, ProvideLoanDeferred.class);
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
        sql.append("select count(*) from cms_provide_loan_deferred where 1=1 and corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(bizStatus)) {
            sql.append(" and BIZ_STATUS in(:bizStatus) ");
            params.put("bizStatus", bizStatus.split(","));
        }
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in(:status) ");
            params.put("status", status.split(","));
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
}
