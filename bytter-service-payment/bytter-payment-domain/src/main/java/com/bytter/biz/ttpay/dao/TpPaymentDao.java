package com.bytter.biz.ttpay.dao;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author chenf
 * @version 1.0
 * @Title: DAO
 * @Description: 第三方支付Dao
 * @date 2022-05-09
 */
@Repository
public class TpPaymentDao extends BaseDao<TpPayment, String> {
    /**
     * 付款通知表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select n.id as id, n.bill_no as billNo,n.corp_id as corpId,n.bus_title as busTitle,");
        sql.append(" n.payer_acc as payerAcc,n.payer_acc_name as payerAccName,n.pay_type as payType,n.pay_money as payMoney,");
        sql.append(" n.payee_acc as payeeAcc,n.payee_acc_name as payeeAccName,n.wish_payday as wishPayDay,n.payee_mode as payeeMode,n.purpose as purpose,n.add_word as addWord,");
        sql.append(" n.is_forindividual as isForindividual,n.bus_Type as busType,t.bus_type_name as busTypeName,n.status,n.rmk,n.cur_id as curId,n.attachment_id as attachmentId,n.create_date as createDate");
        sql.append(" from tp_payment n");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        //sql.append(" left join sys_corp sc on n.corp_id = sc.id");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql, String isMergeAndSplit) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpPaymentVO.class, "n", false);
    }
    /**
     * 重复付款校验
     *
     * @param corpId    单位
     * @param payMoney  金额
     * @param payeeAcc  收款账号
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param id        单据号（编辑时校验需要）
     * @param tempFlag  true - 查询暂存打回状态， false-查询已提交之后的状态数据
     * @return
     */
    public List<TpPayment> listByRePay(String corpId, BigDecimal payMoney, String payeeAcc, String beginDate, String endDate, String curId, String id, boolean tempFlag) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from TpPayment n where n.corpId = :corpId and n.curId = :curId and n.payMoney = :payMoney and n.payeeAcc = :payeeAcc ");
        hql.append(" and n.createDate >= :beginDate and n.createDate <= :endDate ");
        if (tempFlag) {
            hql.append("and n.status in (").append(NetbankConstant.STATUE_TEMP).append(",").append(NetbankConstant.STATUE_BACK).append(")");
        } else {
            hql.append("and n.status not in (").append(NetbankConstant.STATUE_TEMP).append(",").append(NetbankConstant.STATUE_BACK).append(",").append(NetbankConstant.STATUE_DEL).append(")");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("curId", curId);
        params.put("payMoney", payMoney);
        params.put("payeeAcc", payeeAcc);
        params.put("beginDate", DateUtils.parse(beginDate));
        params.put("endDate", DateUtils.parse(endDate));
        if (!StringUtils.isBlank(id)) {
            hql.append(" and n.id <> :id ");
            params.put("id", id);
        }
        return listByHQLAliasToBean(hql.toString(), params, TpPayment.class);
    }
    /**
     * 根据单位统计付款信息
     *
     * @param corpId
     * @return
     */
    public int getBillheadCountByCorpId(String corpId) {
        String sql = "select count(*) from nis_billhead where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql += " and status in ('0','11','-1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 获取已用余额
     *
     * @param corpId     单位id
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @return 限额方式，日：1，月：2
     */
    public BigDecimal getSumPayMoney(String corpId, String merchantNo, String payType, String beginDate, String endDate) {
        BigDecimal payMoney = BigDecimal.ZERO;
        StringBuilder sql = new StringBuilder("");
        sql.append(" select sum(pay_money) as payMoney from tp_payment ");
        // 暂存和删除、打回数据不计入
        sql.append(" where status not in ( " + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_DEL + ")");
        sql.append(" and create_date >= to_date('" + beginDate + "','yyyy-MM-dd') ");
        sql.append(" and create_date < to_date('" + endDate + "','yyyy-MM-dd') ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        if (StringUtils.isNotEmpty(corpId)) {
            sql.append(" and corp_id = :corpId ");
            params.put("corpId", corpId);
        }
        if (StringUtils.isNotEmpty(merchantNo)) {
            sql.append(" and payer_acc = :merchantNo ");
            params.put("merchantNo", merchantNo);
        }
        if (StringUtils.isNotEmpty(payType)) {
            sql.append(" and pay_type = :payType ");
            params.put("payType", payType);
        }
        List<TpPaymentVO> list = this.listBySQLAliasToBean(sql.toString(), params, TpPaymentVO.class);
        if (null != list && list.size() > 0 && null != list.get(0).getPayMoney()) {
            payMoney = list.get(0).getPayMoney();
        }
        return payMoney;
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public TpPaymentVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, TpPaymentVO.class);
    }
    /**
     * 带原状态更新付款单
     *
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateTpPaymentStatus(TpPaymentVO vo, Integer[] oldStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update TpPayment set status=:status");
        Map<String, Object> params = new HashMap<>();
        params.put("status", vo.getStatus());
        if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
            hql.append(",updateBy=:updateBy ");
            params.put("updateBy", vo.getUpdateBy());
        }
        if (vo.getUpdateDate() != null) {
            hql.append(",updateDate=:updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        if (StringUtils.isNotEmpty(vo.getVoucherStat())) {
            hql.append(",voucherStat=:voucherStat ");
            params.put("voucherStat", vo.getVoucherStat());
        }
        if (StringUtils.isNotEmpty(vo.getTrandNo())) {
            hql.append(",trandNo=:trandNo ");
            params.put("trandNo", vo.getTrandNo());
        }
        hql.append(" where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
        //银行接口指令状态 为2 处理失败或者为空时 发送或重新发送将指令状态改为3待发送
        if (StringUtils.isNotEmpty(vo.getVoucherStat())) {
            hql.append("and (voucherStat= '2' or voucherStat is null)");
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  n.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        sql.append(" and ").append(businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpPaymentVO.class, "n", false);
    }
    /**
     * 获取审批单位集合
     *
     * @param businessSql
     * @return
     */
    public List<String> getCorpIdsByDataTableApprove(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct n.corp_Id from tp_payment n where 1=1 ");
        sql.append(" and ").append(businessSql);
        return this.getSession().createSQLQuery(sql.toString()).list();
    }
    /**
     * 根据付款ID获取付款信息(不关联其他表)
     *
     * @param id
     * @return
     */
    public TpPaymentVO getVoById(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, TpPaymentVO.class);
    }
    /**
     * 根据商户、支付方式查询业务在途金额
     *
     * @param corpId
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @return
     */
    public BigDecimal getUseBal(String corpId, String merchantNo, String payType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(p.pay_money) usePayMoney");
        sql.append(" from tp_payment p");
        sql.append(" where p.status>=" + NetbankConstant.STATUE_BACK);
        sql.append(" and p.status<" + NetbankConstant.STATUE_CHECK );
        sql.append(" and p.payer_acc =:merchantNo and p.pay_type=:payType");
        HashMap params = new HashMap();
        params.put("merchantNo", merchantNo);
        params.put("payType", payType);
        Object payMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal userMoney = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(payMoney)) {
            userMoney = (BigDecimal) payMoney;
        }
        return userMoney;
    }
}
