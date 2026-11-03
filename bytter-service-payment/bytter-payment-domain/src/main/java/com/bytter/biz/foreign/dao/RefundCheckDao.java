package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.vo.RefundCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 外汇退款勾对
 * @author huangx
 * @date 2024-03-8 09:20:33
 * @version 1.0
 *
 */
@Repository
public class RefundCheckDao extends BaseDao<RefundCheckVO, String> {
    /**
     * 获取银行明细列表
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        String eqs_blendSign = (String) dataTablesInput.getFormdata().get("eqs_blendSign");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        sql.append(" select distinct a.id as id,a.acc_no as accNo,acc.corp_id as corpId,a.bank_acc as bankAcc,a.acc_name as accName,a.bank_name as bankName,a.bif_code as bifCode, ");
        sql.append(" a.opp_acc_no as oppAccNo,a.opp_acc_name as oppAccName,a.opp_acc_bank as oppAccBank,a.cd_sign as cdSign,bt_nvl_char(a.rb_sign, '2') as rbSign, ");
        sql.append(" a.amt as amt,a.bal as bal,a.cur_id as curId,cur.cur_name as curName,a.uses as uses,a.postscript as postscript,a.remark as remark,a.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, a.project_remark as projectRemark,case when a.fex_blend_sign is null then '0' else a.fex_blend_sign end as blendSign,a.bank_serial_no as bankSerialNo,");
        sql.append(" a.refund_Blend_Type as refundBlendType,a.paymentblending_date as paymentBlendingDate, ");
        //增加现金流相关字段
        sql.append(" f.id as cashFlowId,f.bill_No as billNo,f.tran_Bill_Id as tranBillId,r.bill_No as tranBillNo,f.corp_Bank_Acc as corpBankAcc, f.corp_Acc_Name as corpAccName,");
        sql.append(" f.cotp_Bank_Acc as cotpBankAcc, f.cotp_Acc_Name as cotpAccName,f.pay_Money as payMoney,f.settle_Date as settleDate ");
        sql.append(" from bis_acc_dtl a left join bt_bank_acc acc on a.bank_acc = acc.bank_acc  ");
        sql.append(" left join bis_acc_attribute att on acc.attribute_id = att.id ");
        sql.append(" left join bis_foreign_exc b on a.bank_serial_no = b.bank_serial_no ");
        sql.append(" left join bt_currency cur on a.cur_id = cur.id ");
        sql.append(" left join  fex_cash_flow f on f.bank_serial_no=a.bank_serial_no");
        sql.append(" left join fex_tran_req r on f.tran_bill_id=r.id");
        sql.append(" where ((a.cd_sign = '0' and a.amt >0 ) or (a.cd_sign = '1' and a.amt <0 ))  "  );
        //过滤有效状态的流水
        sql.append(" and a.status = " + CommonConstant.STATUE_CHECK);
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if(!EmptyUtils.isEmpty(eqs_blendSign)) {
            if (BtBankAccConstant.BLENDSIGN_0.equalsIgnoreCase(eqs_blendSign)) {
                sql.append(" and (a.fex_blend_sign = '" + eqs_blendSign + "' or a.fex_blend_sign is null) ");
            } else {
                sql.append(" and a.fex_blend_sign = '" + eqs_blendSign + "' ");
            }
        }
        dataTablesInput.getFormdata().remove("eqs_blendSign");
        //根据查询条件及账户数据进行一次过滤
        List<String> corpList = null;
        String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
        if (StringUtils.isNotBlank(corpId)) {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            corpList = Arrays.asList(corpIds);
        }
        if (EmptyUtils.isNotEmpty(corpList)) {
            String corpAuthSql = SqlUtil.getInSql(corpList, "acc.corp_id",true);
            sql.append(" and ").append(corpAuthSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), RefundCheckVO.class, "a", false);
    }

    /**
     * 根据id 得到所有明细
     * @param ids
     * @return
     */
    public List<RefundCheckVO> listDtlByIds(String[] ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.fex_blend_sign as blendSign,t.bank_serial_no as bankSerialNo,");
        sql.append(" t.refund_Blend_Type as refundBlendType,t.paymentblending_date as paymentBlendingDate");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where ").append(SqlUtil.getInSql(ids, "t.id", true));
        return listBySQLAliasToBean(sql.toString(), null, RefundCheckVO.class);
    }

    /**
     * 根据流水号ID获取流水
     * @param id
     * @return
     */
    public RefundCheckVO getDtlById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id,t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,t.bif_code as bifCode, ");
        sql.append(" t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,bt_nvl_char(t.rb_sign, '2') as rbSign, ");
        sql.append(" t.amt as amt,t.bal as bal,t.cur_id as curId,cur.cur_name as curName,t.uses as uses,t.postscript as postscript,t.remark as remark,t.trans_time as transTime, ");
        sql.append(" acc.attribute_Id as attributeId, att.attribute_name as attributeName, t.project_remark as projectRemark,t.fex_blend_sign as blendSign,t.bank_serial_no as bankSerialNo,");
        sql.append(" t.refund_Blend_Type as refundBlendType,t.paymentblending_date as paymentBlendingDate");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc  ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_currency cur on t.cur_id = cur.id ");
        sql.append(" where t.id=:id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, RefundCheckVO.class);
    }

    /**
     * 更新流水的勾对状态
     * @param id
     * @param blendSign
     * @return
     */
    public int updateBlendSign(String id, String blendSign, String bankSerialNo, String refundBlendType, ICurrentUser user){
        StringBuilder sql = new StringBuilder();
        sql.append("update bis_acc_dtl set fex_blend_sign = :blendSign");
        Map<String, Object> params = new HashMap<>();
        //如果修改为已勾对则存入当前时间为勾对时间。如果修改为未勾兑就存入null
        if(BtBankAccConstant.BLENDSIGN_1.equals(blendSign)){
            sql.append(", paymentblending_date = :paymentBlendingDate ");
            params.put("paymentBlendingDate", DateUtils.getDate());
        }
        // 流水号
        if (StringUtils.isNotBlank(bankSerialNo)) {
            sql.append(", bank_serial_no = :bankSerialNo ");
            params.put("bankSerialNo", bankSerialNo);
        }
        // 处理方式
        sql.append(", refund_Blend_Type = :refundBlendType ");
        params.put("refundBlendType", refundBlendType);
        if (user != null) {
            sql.append(", update_By = :updateBy, update_Date=:updateDate ");
            params.put("updateBy", user.getLoginName());
            params.put("updateDate", DateUtils.getDate());
        }
        sql.append(" where id = :id");
        params.put("id",id);
        params.put("blendSign",blendSign);
        return this.batchSQLExecute(sql.toString(), params);
    }
}
