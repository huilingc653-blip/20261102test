package com.bytter.check.dao;

import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.bdlink.vo.BisPaymentBlendingVO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.biz.payment.api.vo.NisBillBankSerialNoVO;
import com.bytter.biz.payment.api.vo.VNisPayBillheadVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BisAccDtlDTO;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author qijh
 */
@Repository
public class PaymentBlendingDao extends BaseDao<BisAccDtlVO, String> {
//TODO 放到资金核对
    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql,String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
                " where dtl.cd_sign = '1' and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        //sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no and b.bank_serial_no <> '' ");
        //sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" where t.cd_sign='1' and (b.voucher_from = '" + BdlinkConstant.FROM_NIS_PAY + "' or b.voucher_from is null) ");
        } else {
            if("0".equals(allocationAllowBlending)) {
                sql.append(" where t.cd_sign='1' and (b.voucher_from = '" + BdlinkConstant.FROM_NIS_PAY + "' or b.voucher_from is null) and t.id not in (" + sqlnoIn + ") ");
            }else{
                sql.append(" where t.cd_sign='1' ");
            }
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqs_blendSign)) {
            sql.append(" and (t.BLEND_SIGN = '" + eqs_blendSign + "' or t.BLEND_SIGN is null) ");
        } else {
            if (StringUtils.isNotEmpty(eqs_blendSign)) {
                sql.append(" and t.BLEND_SIGN = '" + eqs_blendSign + "' ");
            }
        }
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" and    " + SqlUtil.getInSql(eqs_id.split("/"), "t.id", true));
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    /**
     * 保存付款单业务类型
     *
     * @param id
     * @param busType
     */
    public int toSaveBillBusType(String id, String busType) {
        String sql = " update nis_billhead set bus_type = :busType where id = :id ";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("busType", busType);
        return createSQLQuery(sql, map).executeUpdate();
    }
    /**
     * 保存代发工资业务类型
     *
     * @param id
     * @param busType
     */
    public int toSaveWageBusType(String id, String busType) {
        String sql = " update nis_wage set bus_type = :busType where id = :id ";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("busType", busType);
        return createSQLQuery(sql, map).executeUpdate();
    }
    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
//    public int toUpdateBlendSign(String id, String blendSign) {
//        String sql = " update bis_acc_dtl set blend_sign = :blendSign where id = :id";
//        //避免重复勾兑
//        if(BtBankAccConstant.BLENDSIGN_1.equals(blendSign)){
//            sql += " and (blend_sign<>:blendSign or blend_sign is null or blend_sign = '' ) ";
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("blendSign", blendSign);
//        return createSQLQuery(sql, map).executeUpdate();
//    }
    /*
  根据勾兑规则查流水
   */
//    public DataTablesOutput getBisaccByRules(DataTablesInput input) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select a.id as id,a.BANK_SERIAL_NO as bankSerialNo from bis_acc_dtl a");
//        sql.append(" where 1=1");
//        return getDynamicBySQL(input,sql.toString(),BisAccDtlDTO.class,"a", false);
//
//    }
    /*
  根据流水号查流水
   */
    public List<BisAccDtlDTO> getBisacc(String dtlOnlyId,String dtlOnlyValue) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        sql.append("select d.id as Id,d.BIS_DTL_NO as bisDtlNo from bis_acc_dtl d ");
        sql.append(" where 1 = 1  and d.id not in (select dtl_id from NIS_BILL_BANK_SERIAL_NO)  and d."+dtlOnlyId+" = :bisDtlNo");
        params.put("bisDtlNo", dtlOnlyValue);
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);

    }
    /*
    根据流水查付款单
     */
    public List<VNisPayBillDTO> getBillhead(String billOnlyId, String dtlOnlyValue, String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        String table = getVNisPayBillSql();
        if("1".equals(allocationAllowBlending)){
            table = getVNisPayBillAllocationSql();
        }
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append("select n.id as Id,n.billNo as billNo,n.isFrom as isFrom,exc.BIS_DTL_NO as bisDtlNo from ("+table+") n  ");
        sql.append(" left join bis_exc exc on n.id = exc.cbs_bill_id");
        sql.append(" where 1=1  and n.id  not in(select bill_id from NIS_BILL_BANK_SERIAL_NO) ");
        //and exc.BIS_DTL_NO=:bisDtlNo
        if(billOnlyId.equals("BIS_DTL_NO")){
            sql.append(" and exc.BIS_DTL_NO=:bisDtlNo ");
        }else if(billOnlyId.equals("BANK_SERIAL_NO")){
            sql.append(" and exc.BANK_SERIAL_NO=:bisDtlNo ");
        }else if(billOnlyId.equals("BILL_NO")){
            sql.append(" and n.billNo=:bisDtlNo ");
        }
        params.put("bisDtlNo", dtlOnlyValue);
        return listBySQLAliasToBean(sql.toString(), params, VNisPayBillDTO.class);
    }
    public DataTablesOutput getDtlRules(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (select b.id as Id,e.rules as rules,e.rules_name as rulesName,b.BIS_DTL_NO as bisDtlNo,b.BANK_SERIAL_NO as bankSerialNo, ");
        sql.append("b.OPP_ACC_NO as oppAccNo,b.amt as amt,b.BANK_ACC as bankAcc,b.TRANS_TIME as transTime,b.ACC_NO as accNo,b.again_sign as againSign, ");
        sql.append("b.CUR_ID as curId,a.corp_id as corpId,b.cd_sign as cdSign,b.USES as uses,e.IS_EXIST_ID as isExistId,e.trans_days as transDays, ");
        sql.append("b.ACC_NAME as accName,b.OPP_ACC_NAME as oppAccName,e.BILL_ONLY_ID as billOnlyId,e.DTL_ONLY_ID as dtlOnlyId ");
        sql.append(" from  bis_bank_execution_strategy e  left join BIS_BIF_INIT i on i.bank_type_id = e.bank_type_id");
        sql.append(" left join bis_acc_dtl b on i.id = b.BIF_CODE   left join bt_bank_acc a on b.bank_acc = a.bank_acc  ");
        sql.append("where  e.type = '0' and e.is_enable = '1' and b.BLEND_SIGN='0' and b.cd_sign='1' and (b.auto_sign = '0' or b.auto_sign='' or b.auto_sign is null) ) t  order by t.transTime desc ");
        return getDynamicBySQL(input, sql.toString(), BisPaymentBlendingVO.class, "t", false);
    }
    /**
     * 更新流水银行流水号
     *
     * @param id
     * @param bankSerialNo
     * @return
     */
//    public int toUpdateBankSerialNo(String id, String bankSerialNo) {
//        String sql = " update bis_acc_dtl set bank_serial_no = :bankSerialNo where id = :id";
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("bankSerialNo", bankSerialNo);
//        return createSQLQuery(sql, map).executeUpdate();
//    }
    /**
     * 根据id 得到所有明细
     *
     * @param ids
     */
    public List<BisAccDtlVO> listDtlByids(String[] ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.BLEND_SIGN as blendSign,t.bank_serial_no as bankSerialNo");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where 1 = 1 and t.id in ( :ids ) ");
        Map<String, Object> params= new HashMap<>();
        params.put("ids", ids);
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlVO.class);
    }
    /**
     * 获取未勾兑的明细数量
     *
     * @return
     */
    public List<Map<String, Object>> getDtlCountNoblendSign(List<String> userIds,String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
                " where 1=1 and dtl.cd_sign = '1' and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select  g.user_id as USERID ,count(t.id) as COUNT from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc ");
        sql.append(" left join sys_account_grant g on acc.id = g.account_id and g.can_query_detail = 1 ");
        sql.append(" where 1 = 1 and (t.blend_sign = '0' or t.blend_sign is null) and t.cd_sign = '1' ");
        if("0".equals(allocationAllowBlending)){
            sql.append(" and t.id not in (" + sqlnoIn + ") ");
        }
        sql.append(" and g.user_id is not null ");
        if (!EmptyUtils.isEmptyList(userIds)) {
            sql.append(" and ").append(getSQLIn(userIds, 900, "g.user_id"));
        }
        sql.append(" group by g.user_id ");
        List<Map<String, Object>> list = createSQLQuery(sql.toString()).setCacheable(false).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }
    /**
     * 删除中间关联表
     *
     * @param dtlId
     */
    public void deleteBillBankSerialNoByDtlId(String dtlId) {
        String sql = " delete from NIS_BILL_BANK_SERIAL_NO where   DTL_ID=:dtlId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }
    /**
     * 删除中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoByBillId(String billId) {
        String sql = " delete from NIS_BILL_BANK_SERIAL_NO where   bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }
    /**
     * 根据业务单据号得到流水id
     *
     * @param billId
     * @return
     */
    public List<String> getDtlId(String billId) {
        String sql = " select  DTL_ID from NIS_BILL_BANK_SERIAL_NO where  bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }
    /**
     * 根据流水id获取业务单号
     *
     * @param dtlId
     * @return
     */
    public List<String> getBillId(String dtlId) {
        String sql = " select  bill_id from NIS_BILL_BANK_SERIAL_NO where  DTL_ID=:dtlId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }
    /**
     * 插入中间关联表
     *
     * @param billId
     * @param isFrom
     * @param bankSerialNo
     */
    public void insetBillBankSerialNo(String billId, String isFrom, String bankSerialNo, String dtlId) {
        String sql = " insert into NIS_BILL_BANK_SERIAL_NO(bill_id,BANK_SERIAL_NO,IS_FROM,DTL_ID,PAYMENTBLENDING_DATE) values(:billId,:bankSerialNo,:isFrom,:dtlId,:paymentBlendingDate)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("bankSerialNo", bankSerialNo);
        params.put("isFrom", isFrom);
        params.put("dtlId", dtlId);
        Date paymentBlendingDate = DateUtils.getDate();
        params.put("paymentBlendingDate",paymentBlendingDate);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 插入退款勾兑中间关联表
     *
     * @param billId
     * @param isFrom
     * @param bankSerialNo
     */
    public void insetBillBankSerialNoRefund(String billId, String isFrom, String bankSerialNo, String dtlId) {
        String sql = " insert into NIS_BILL_BANK_SERIAL_NO_REFUND(bill_id,BANK_SERIAL_NO,IS_FROM,DTL_ID,PAYMENTBLENDING_DATE) values(:billId,:bankSerialNo,:isFrom,:dtlId,:paymentBlendingDate)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("bankSerialNo", bankSerialNo);
        params.put("isFrom", isFrom);
        params.put("dtlId", dtlId);
        Date paymentBlendingDate = DateUtils.getDate();
        params.put("paymentBlendingDate",paymentBlendingDate);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    private String getVNisPayBillSql(){
        String sql = "select n.id as id,\n" +
                "       n.bill_No            as billNo,\n" +
                "       n.corp_Id            as corpId,\n" +
                "       f.voucher_name       as voucherName,\n" +
                "       case when b.payer_acc is not null then b.payer_acc\n" +
                "       else n.payer_Acc  end    AS payerAcc,\n" +
                "       n.payer_Acc_Name     as payerAccName,\n" +
                "       n.cur_Id             as curId,\n" +
                "       n.wish_payday        as wishPayday,\n" +
                "       n.pay_Money          as payMoney,\n" +
                "       n.payee_Acc          as payeeAcc,\n" +
                "       n.payee_Acc_Name     as payeeAccName,\n" +
                "       n.purpose            as purpose,\n" +
                "       n.bus_Type           as busType,\n" +
                "       n.status             as status,\n" +
                "       n.blend_sign         as blendSign,\n" +
                "       'nis_billhead'               as isFrom\n" +
                "  from Nis_Billhead n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join Nis_Bus_Type t on n.bus_Type = t.id\n" +
                "  left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1'\n" +
                "  left join bt_bank_acc c on n.down_Acc_Id = c.id\n" +
                " where 1 = 1\n" +
                "   and n.status = (case\n" +
                "         when f.is_account = 1 then\n" +
                "          100\n" +
                "         else\n" +
                "          95\n" +
                "       end)\n" +
                "   and ((n.is_payed = 1) or\n" +
                "       (n.is_payed = 0 and\n" +
                "       (b.voucher_stat = '0' or b.voucher_stat is null)))\n" +
                "  union all\n" +
                "  select n.id as id,\n" +
                "         n.wage_no as billNo,\n" +
                "         n.corp_id as corpId,\n" +
                "         '代发工资' as voucherName,\n" +
                "         n.payer_acc as payerAcc,\n" +
                "         n.payer_acc_name as payerAccName,\n" +
                "         n.cur_id as curId,\n" +
                "         n.wish_payday   as wishPayday,\n" +
                "         n.wages as payMoney,\n" +
                "         '' as payeeAcc,\n" +
                "         '' as payeeAccName,\n" +
                "         '代发工资' as purpose,\n" +
                "         n.bus_type as busType,\n" +
                "         n.status as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_wage'             as isFrom\n" +
                "  from nis_wage n\n" +
                "  where 1=1 and n.status = 95\n" +
                "  union all\n" +
                "  select n.id                 as id,\n" +
                "         n.bill_no            as billNo,\n" +
                "         n.corp_id            as corpId,\n" +
                "         f.voucher_name       as voucherName,\n" +
                "         n.corp_bank_acc      as payerAcc,\n" +
                "         n.corp_acc_name      as payerAccName,\n" +
                "         n.cur_id             as curId,\n" +
                "         null                 as wishPayday,\n" +
                "         n.pay_money          as payMoney,\n" +
                "         n.COTP_BANK_ACC      as payeeAcc,\n" +
                "         n.COTP_ACC_NAME      as payeeAccName,\n" +
                "         req.purpose          as purpose,\n" +
                "         pro.pro_name         as busType,\n" +
                "         n.status             as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'fex_cash_flow'      as isFrom\n" +
                "  from fex_cash_flow n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join fex_tran_req req on req.id = n.TRAN_BILL_ID\n" +
                "  left join fex_product pro on req.pro_id = pro.id\n" +
                "  where 1=1 and n.status =95 and n.PAY_STATUS = '1' and n.CD_SIGN = '1' ";
        return sql;
    }

    private String getVNisPayBillAllocationSql(){
        String sql = "select n.id as id,\n" +
                "       n.bill_No            as billNo,\n" +
                "       n.corp_Id            as corpId,\n" +
                "       f.voucher_name         as voucherName,\n" +
                "       case when b.payer_acc is not null then b.payer_acc\n" +
                "       else n.payer_Acc  end    AS payerAcc,\n" +
                "       n.payer_Acc_Name     as payerAccName,\n" +
                "       n.cur_Id             as curId,\n" +
                "       n.wish_payday        as wishPayday,\n" +
                "       n.pay_Money          as payMoney,\n" +
                "       n.payee_Acc          as payeeAcc,\n" +
                "       n.payee_Acc_Name     as payeeAccName,\n" +
                "       n.purpose            as purpose,\n" +
                "       n.bus_Type           as busType,\n" +
                "       n.status             as status,\n" +
                "       n.blend_sign         as blendSign,\n" +
                "       'nis_billhead'               as isFrom\n" +
                "  from Nis_Billhead n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join Nis_Bus_Type t on n.bus_Type = t.id\n" +
                "  left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1'\n" +
                "  left join bt_bank_acc c on n.down_Acc_Id = c.id\n" +
                " where 1 = 1\n" +
                "   and n.status = (case\n" +
                "         when f.is_account = 1 then\n" +
                "          100\n" +
                "         else\n" +
                "          95\n" +
                "       end)\n" +
                "   and ((n.is_payed = 1) or\n" +
                "       (n.is_payed = 0 and\n" +
                "       (b.voucher_stat = '0' or b.voucher_stat is null)))\n" +
                "  union all\n" +
                "  select n.id as id,\n" +
                "         n.wage_no as billNo,\n" +
                "         n.corp_id as corpId,\n" +
                "         '代发工资' as voucherName,\n" +
                "         n.payer_acc as payerAcc,\n" +
                "         n.payer_acc_name as payerAccName,\n" +
                "         n.cur_id as curId,\n" +
                "         n.wish_payday   as wishPayday,\n" +
                "         n.wages as payMoney,\n" +
                "         '' as payeeAcc,\n" +
                "         '' as payeeAccName,\n" +
                "         '代发工资' as purpose,\n" +
                "         n.bus_type as busType,\n" +
                "         n.status as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_wage'             as isFrom\n" +
                "  from nis_wage n\n" +
                "  where 1=1 and n.status = 95\n" +
                "  union all\n" +
                "  select n.id                 as id,\n" +
                "         n.bill_no            as billNo,\n" +
                "         n.corp_id            as corpId,\n" +
                "         f.voucher_name       as voucherName,\n" +
                "         n.corp_bank_acc      as payerAcc,\n" +
                "         n.corp_acc_name      as payerAccName,\n" +
                "         n.cur_id             as curId,\n" +
                "         null                 as wishPayday,\n" +
                "         n.pay_money          as payMoney,\n" +
                "         n.COTP_BANK_ACC      as payeeAcc,\n" +
                "         n.COTP_ACC_NAME      as payeeAccName,\n" +
                "         req.purpose          as purpose,\n" +
                "         pro.pro_name         as busType,\n" +
                "         n.status             as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'fex_cash_flow'      as isFrom\n" +
                "  from fex_cash_flow n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join fex_tran_req req on req.id = n.TRAN_BILL_ID\n" +
                "  left join fex_product pro on req.pro_id = pro.id\n" +
                "  where 1=1 and n.status =95 and n.PAY_STATUS = '1' and n.CD_SIGN = '1'\n" +
                "  union all\n" +
                "    select n.id                 as id,\n" +
                "         n.bill_no              as billNo,\n" +
                "         n.corp_id              as corpId,\n" +
                "         case when n.type = '10' then '上划'\n" +
                "              when n.type = '11' then '下拨'\n" +
                "              when n.type = '12' then '请款'\n" +
                "              when n.type = '13' then '头寸调拨'\n" +
                "              when n.type = '14' then '上缴'\n" +
                "           end                  as voucherName,\n" +
                "         n.PAYER_ACC        as payerAcc,\n" +
                "         n.PAYER_ACC_NAME        as payerAccName,\n" +
                "         n.cur_id               as curId,\n" +
                "         n.Wish_Payday          as wishPayday,\n" +
                "         n.MONEY                as payMoney,\n" +
                "         n.payee_acc            as payeeAcc,\n" +
                "         n.PAYEE_ACC_NAME       as payeeAccName,\n" +
                "         n.purpose              as purpose,\n" +
                "         null                   as busType,\n" +
                "         n.status               as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_allocation_req'      as isFrom\n" +
                "  from nis_allocation_req n\n" +
                "  where 1=1 and n.status >= 95 ";
        return sql;
    }
    /**
     * 根据银行流水获取付款单
     *
     * @param input
     * @return
     */
    public DataTablesOutput getDataTablesUnionForDtl(DataTablesInput input,String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        DataTablesOutput output = null;
        String table = getVNisPayBillSql();
        if("1".equals(allocationAllowBlending)){
            table = getVNisPayBillAllocationSql();
        }
        sql.append(" select distinct n.* from ("+table+") n ");
        sql.append(" left join bis_exc exc on n.id = exc.cbs_bill_id  ");
        sql.append(" where 1=1 ");
        sql.append(" and n.id not in (select bill_id from NIS_BILL_BANK_SERIAL_NO) ");
        String gedTransTime = (String) input.getFormdata().get("ged_wishPayday");
        String ledTransTime = (String) input.getFormdata().get("led_wishPayday");
        input.getFormdata().remove("ged_wishPayday");
        input.getFormdata().remove("led_wishPayday");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            sql.append(" and  case when exc.trans_time  is not null then exc.trans_time  else ");
            sql.append(" n.wishPayday end  >= '"+ gedTransTime+ "'");
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            sql.append(" and  case when exc.trans_time  is not null then exc.trans_time  else ");
            sql.append(" n.wishPayday end  <= '"+ ledTransTime+ "'");
        }
        output = getDynamicBySQL(input, sql.toString(), VNisPayBillheadVO.class, "n", false);
        return output;
    }

    /**
     * 根据规则获取银行明细列表
     *
     * @param input
     * @return
     */
    public Boolean getBisAccDtlDataTable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COUNT(*) as count from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc a on t.bank_acc = a.bank_acc where 1= 1 and t.BLEND_SIGN='0' and t.cd_sign='1'  ");
        sql.append(" and t.id not in (select dtl_id from NIS_BILL_BANK_SERIAL_NO) ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (input.getFormdata().containsKey("oppAccName")) {
            sql.append(" and t.OPP_ACC_NAME=:oppAccName");
            params.put("oppAccName", input.getFormdata().get("oppAccName"));
        }
        if (input.getFormdata().containsKey("accName")) {
            sql.append(" and t.ACC_NAME=:accName");
            params.put("accName", input.getFormdata().get("accName"));
        }
        if (input.getFormdata().containsKey("uses")) {
            sql.append(" and t.USES=:uses");
            params.put("uses", input.getFormdata().get("uses"));

        }
        if (input.getFormdata().containsKey("corpId")) {
            sql.append(" and a.CORP_ID=:corpId");
            params.put("corpId", input.getFormdata().get("corpId"));
        }
        if (input.getFormdata().containsKey("cdSign")) {
            sql.append(" and t.CD_SIGN=:cdSign");
            params.put("cdSign", input.getFormdata().get("cdSign"));

        }
        if (input.getFormdata().containsKey("curId")) {
            sql.append(" and t.CUR_ID=:curId");
            params.put("curId", input.getFormdata().get("curId"));
        }
        if (input.getFormdata().containsKey("transTime")) {
            sql.append(" and BT_TO_CHAR(t.TRANS_TIME,'yyyy-mm-dd')=:transTime ");
            params.put("transTime", input.getFormdata().get("transTime"));
        }
        if (input.getFormdata().containsKey("oppAccNo")) {
            sql.append(" and t.OPP_ACC_NO=:oppAccNo");
            params.put("oppAccNo", input.getFormdata().get("oppAccNo"));
        }
        if (input.getFormdata().containsKey("bankAcc")) {
            sql.append(" and t.BANK_ACC=:bankAcc");
            params.put("bankAcc", input.getFormdata().get("bankAcc"));
        }
        if (input.getFormdata().containsKey("amt")) {
            sql.append(" and t.amt=:amt ");
            params.put("amt", input.getFormdata().get("amt"));
        }

        Map map = getBySQL(sql.toString(), params);
        if (!map.isEmpty()) {
            BigDecimal count = (BigDecimal) map.get("COUNT");
            if (count != null) {
                int i = count.compareTo(BigDecimal.ONE);
                if (i == 0) {
                    return true;
                }
            }else{
                BigInteger count1 = (BigInteger) map.get("count");
                if (count1 != null) {
                    int i = count1.compareTo(BigInteger.ONE);
                    if (i == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getPaymentDtlDataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo");
        sql.append(" from bis_acc_dtl t");
        sql.append(" left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" left join NIS_BILL_BANK_SERIAL_NO s on s.bank_serial_no = t.id ");
        sql.append(" where 1= 1 ");
        String billId = (String) dataTablesInput.getFormdata().get("billId");
        String from = (String) dataTablesInput.getFormdata().get("from");
        sql.append(" and s.BILL_ID =  '" + billId + "' and s.IS_FROM = '" + from + "' ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput exportAll(DataTablesInput dataTablesInput, String joinSql,String allocationAllowBlending) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
                " where dtl.cd_sign = '1' and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo,t.paymentblending_date as paymentBlendingDate");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
//        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no and b.bank_serial_no <> '' ");
        sql.append(" where t.cd_sign='1'  ");
        if("0".equals(allocationAllowBlending)){
            sql.append(" and t.id not in (" + sqlnoIn + ") ");
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqs_blendSign)) {
            sql.append(" and (t.BLEND_SIGN = '" + eqs_blendSign + "' or t.BLEND_SIGN is null) ");
        } else {
            if (StringUtils.isNotEmpty(eqs_blendSign)) {
                sql.append(" and t.BLEND_SIGN = '" + eqs_blendSign + "' ");
            }
        }
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" and    " + SqlUtil.getInSql(eqs_id.split("/"), "t.id", true));
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }
    /**
     * 更新流水是否自动勾对的值
     * @param id
     * @param autoSign
     * @return
     */
    public int toUpdateAutoSign(String id, String autoSign,String againSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update bis_acc_dtl set AUTO_SIGN = :autoSign  ");
        params.put("id", id);
        params.put("autoSign", autoSign);
        if(EmptyUtils.isNotEmpty(againSign)){
            sql.append(",AGAIN_SIGN = :againSign ");
            params.put("againSign", againSign);
        }
        sql.append(" where id = :id ");
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新流水勾兑状态
     *
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id, String blendSign) {
        String sql = " update bis_acc_dtl set blend_sign = :blendSign,paymentblending_date = :paymentblendingDate where id = :id ";
        Date paymentblendingDate = DateUtils.getDate();
        //避免重复勾兑
        if(BtBankAccConstant.BLENDSIGN_1.equals(blendSign)){
            sql += " and (blend_sign<>:blendSign or blend_sign is null or blend_sign = '' ) ";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("blendSign", blendSign);
        map.put("paymentblendingDate", paymentblendingDate);
        return createSQLQuery(sql, map).executeUpdate();
    }


    /**
     * 更新单据的勾对状态
     * @param billId 单据id
     * @param isFrom 单据来源哪个表
     * @param blendSign 勾对标志
     */
    public void updateBillBlendSign(String billId, String isFrom,String blendSign){
        String table = isFrom;
        String sql = " update "+table+" set blend_sign = :blendSign where id = :billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("blendSign", blendSign);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 获取银行明细列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql,String allocationAllowBlending,List<String> accList) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        String purpose = (String) dataTablesInput.getFormdata().get("likes_purpose");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        String sqlnoIn = "select dtl.id from bis_acc_dtl dtl left join bt_bank_acc acc on dtl.bank_acc = acc.bank_acc\n " +
                " left join bt_bank_acc acc1 on dtl.opp_acc_no = acc1.bank_acc,bt_bank_acc_cur cur\n " +
                " where dtl.cd_sign = '1' \n" +
                " and " + SqlUtil.getInSql(accList, "cur.bank_acc_id", true) +
                " and " + SqlUtil.getInSql(accList, "cur.prent_acc_id", true) +
                "and ((acc.id = cur.bank_acc_id and acc1.id = cur.prent_acc_id) or (acc.id=cur.prent_acc_id and acc1.id=cur.bank_acc_id)) ";
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime,t.paymentblending_date as paymentBlendingDate, ");
        sql.append(" acc.attribute_Id as attributeId, t.project_remark as projectRemark,case when t.BLEND_SIGN is null then '0' else t.BLEND_SIGN end as blendSign,t.bank_serial_no as bankSerialNo ");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join bis_exc b on t.bank_serial_no = b.bank_serial_no and b.bank_serial_no <> '' ");
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" where t.cd_sign='1'  ");
        } else {
            if("0".equals(allocationAllowBlending)) {
                sql.append(" where t.cd_sign='1' and t.id not in (" + sqlnoIn + ") ");
            }else{
                sql.append(" where t.cd_sign='1' ");
            }
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        //用途
        if (EmptyUtils.isNotEmpty(purpose)) {
            sql.append(" and t.uses like '%"+purpose+"%'");
        }
        if(EmptyUtils.isNotEmpty(eqs_blendSign)){
            sql.append(" and t.BLEND_SIGN = '" + eqs_blendSign + "' ");
        }
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            sql.append(" and    " + SqlUtil.getInSql(eqs_id.split(","), "t.id", true));
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }

    /**
     * 根据流水id获取业务单号
     *
     * @param dtlId
     * @return
     */
    public List<NisBillBankSerialNoVO> getBillByDtlId(String dtlId) {
        String sql = " select  bill_id as billId,is_from as isFrom,paymentblending_date as paymentblendingDate from NIS_BILL_BANK_SERIAL_NO where  DTL_ID=:dtlId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return listBySQLAliasToBean(sql,params, NisBillBankSerialNoVO.class);
    }

}
