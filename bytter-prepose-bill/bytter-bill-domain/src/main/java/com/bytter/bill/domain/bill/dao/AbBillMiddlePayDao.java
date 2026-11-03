package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.dto.AbBillPayMiddleDTO;
import com.bytter.bill.domain.bill.entity.AbBillPayMiddle;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * 付款办理挂接已付款票据中间Dao层
 *
 * @author ouyj
 */
@Repository
public class AbBillMiddlePayDao extends BaseDao<AbBillPayMiddle, String> {
    /**
     * 根据票据从表id获取票据付款中间表集合
     *
     * @param vvid  票据从表id
     * @param payId 付款单id
     * @return 据付款中间表集合
     */
    public List<AbBillPayMiddleDTO> getAbBillPayMiddleList(String vvid, String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select balance, payMoney, billId, payId, id, createDate, endDate, newStmIssuofpprInd, blgrdbagcrclPrmtInd from ( ");
        sql.append(" select CASE WHEN M.BALANCE IS NULL THEN V.BILL_MONEY ELSE M.BALANCE END AS balance, ");
        sql.append(" m.pay_money as payMoney, VV.ID AS billId, m.pay_id as payId, m.id as id, m.create_date as createDate, v.END_DATE as endDate, ");
        sql.append(" v.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd, v.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd ");
        sql.append(" FROM AB_VOUCHERINFO_VICE VV LEFT JOIN AB_VOUCHERINFO V ON VV.VID = V.ID  and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
        sql.append(" LEFT JOIN AB_BILL_PAY_MIDDLE M ON VV.ID = M.BILL_ID ");
        sql.append(" WHERE (m.create_date in (SELECT MAX(create_date) FROM AB_BILL_PAY_MIDDLE WHERE status IN (0, 1) GROUP BY bill_id) ");
        sql.append(" OR m.create_date IS NULL) ");
        sql.append(" and (m.status in (0, 1) or m.status is null) ");
        HashMap<String, Object> params = new HashMap<>(2);
        if (StringUtils.isNotEmpty(vvid)) {
            sql.append(" AND vv.id IN (:vvid) ");
            params.put("vvid", vvid.split(","));
        }
        if (StringUtils.isNotEmpty(payId)) {
            sql.append(" AND m.pay_id IN (:payId) ");
            params.put("payId", payId);
        }
        sql.append(" ) m ORDER BY balance DESC, endDate ");
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillPayMiddleDTO.class);
    }
    /**
     * 根据付款单id获取票据付款中间表集合
     *
     * @param payId 付款单id
     * @return 据付款中间表集合
     */
    public List<AbBillPayMiddleDTO> getAbBillPayMiddleListByPayId(String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.balance as balance, m.create_date as createDate, m.pay_money as payMoney, ");
        sql.append(" m.bill_id AS billId, m.pay_id as payId, m.id as id  ");
        sql.append(" FROM AB_BILL_PAY_MIDDLE m  ");
        sql.append(" WHERE pay_id = :payId ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("payId", payId);
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillPayMiddleDTO.class);
    }
    /**
     * 根据票据付款中间表id修改余额
     *
     * @param id      票据付款中间表id
     * @param balance 新增余额值
     */
    public void updateAbBillPayMiddleBalance(String id, double balance) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update AB_BILL_PAY_MIDDLE set balance = balance + :balance where id = :id ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("balance", balance);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据付款表id修改票据付款中间表状态
     *
     * @param payId  付款表id
     * @param status 新状态
     */
    public void updateAbBillPayMiddleStatus(String payId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update AB_BILL_PAY_MIDDLE set status = :status where pay_id = :payId ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("payId", payId);
        params.put("status", status);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据付款id删除对应的票据付款中间表数据
     *
     * @param payId 付款id
     */
    public void deleteAbBillPayMiddleByPayId(String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from AB_BILL_PAY_MIDDLE where pay_id = :payId ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("payId", payId);
        this.batchSQLExecute(sql.toString(), params);
    }
    /**
     * 根据付款单id和票据从表id查询对应的票据中间表数据
     *
     * @param billId 从表id
     * @param payId  付款单id
     * @return 票据中间表数据
     */
    public List<AbBillPayMiddleDTO> queryBillPayMid(String billId, String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.balance as balance, m.create_date as createDate, m.pay_money as payMoney, ");
        sql.append(" m.bill_id AS billId, m.pay_id as payId, m.id as id  ");
        sql.append(" FROM AB_BILL_PAY_MIDDLE m  ");
        sql.append(" WHERE pay_id = :payId ");
        sql.append(" and bill_Id = :billId ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("payId", payId);
        params.put("billId", billId);
        return this.listBySQLAliasToBean(sql.toString(), params, AbBillPayMiddleDTO.class);
    }
    /**
     * 根据付款id和票据从表id删除对应的票据付款中间表数据
     *
     * @param billId 从表id
     * @param payId 付款id
     */
    public void deleteAbBillPayMiddleByPayId(String billId, String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from AB_BILL_PAY_MIDDLE where pay_id = :payId and bill_Id = :billId ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("payId", payId);
        params.put("billId", billId);
        this.batchSQLExecute(sql.toString(), params);
    }
}
