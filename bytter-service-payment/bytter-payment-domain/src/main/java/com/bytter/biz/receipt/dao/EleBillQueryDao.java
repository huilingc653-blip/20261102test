package com.bytter.biz.receipt.dao;
import com.bytter.biz.receipt.entity.EPrecativeLoanBill;
import com.bytter.biz.receipt.vo.EleAllBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class EleBillQueryDao extends BaseDao<EPrecativeLoanBill,String> {
    /**
     * 查询所有电子回单
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listForUnion(DataTablesInput input, String commonSql, List<String> accList){
        StringBuilder sql = new StringBuilder();
        sql.append("select type.bill_name AS billTypeName,A.* from (");
        //委托贷款电子回单表查询 委托贷款还款通知单
        sql.append(" SELECT A.ID as id,A.BILL_NO as billNo,A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType,");
        sql.append(" A.voucher_No as voucherNo,'' as oldVoucher,A.TALLY_DATE as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" FROM E_PRECATIVE_LOAN_BILL A,E_bill_PRINT B");
        sql.append(" WHERE A.BILL_NO = B.BILL_NO");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.precative_loan_acc", true));
        //查询利息清单电子回单
        sql.append(" union SELECT A.ID as id,A.BILL_NO as billNo,A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType,");
        sql.append(" A.voucher_No as voucherNo,'' as oldVoucher,A.TALLY_DATE as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" from e_accrual_bill A,E_BILL_PRINT B WHERE A.BILL_NO=B.BILL_NO and a.corp_id =b.corp_id and A.accrual <> 0");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.ACC_NO", true));
        //查询贷款电子回单表
        sql.append(" union SELECT A.ID as id,A.BILL_NO as bill_no,A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType,");
        sql.append(" A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher, ");
        sql.append(" A.TALLY_DATE as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" FROM E_LOAN_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id =b.corp_id");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.DEPOSIT_ACC", true));
        //查询通知单电子回单
        sql.append(" union SELECT A.ID as id,A.BILL_NO as billNo,A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType, ");
        sql.append(" A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher, ");
        sql.append(" A.tally_date as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" FROM E_NOTICE_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id=b.corp_id ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and (").append(SqlUtil.getInSql(accList, "A.PAYER_ACC", true));
        sql.append(" or ").append(SqlUtil.getInSql(accList, "A.PAYEE_ACC", true)).append(")");
        //手续费通知单
        sql.append(" union SELECT A.ID as id,A.BILL_NO as billNo,A.corp_id as corpId, A.status as status, A.BILL_TYPE as billType, ");
        sql.append(" A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher, ");
        sql.append(" A.tally_date as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" FROM E_POUNDAGE_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO  and a.corp_id=b.corp_id ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.PAYER_ACC", true));
        //通知存单
        sql.append(" union SELECT A.ID as id,A.BILL_NO as billNo, A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType, ");
        sql.append(" A.VOUCHER_NO as voucherNo,A.ORIG_VOUCHER_NO as oldVoucher, ");
        sql.append(" A.tally_date as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append(" from E_TRANS_BILL a left join E_BILL_PRINT b on A.BILL_NO = b.BILL_NO and a.corp_id = b.corp_id ");
        sql.append(" where 1=1 ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.current_acc", true));
        //查询承兑汇票电子回单
        sql.append(" union SELECT A.ID as id,A.BILL_NO as billNo, A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType, ");
        sql.append(" A.VOUCHER_NO as voucherNo,A.OLD_VOUCHER as oldVoucher, ");
        sql.append(" A.tally_date as tallyDate,B.PRINTS as prints,A.OLD_VOUCHER_ID as oldVoucherId ");
        sql.append(" FROM E_ACCEPT_BILL A, E_BILL_PRINT B WHERE A.BILL_NO = B.BILL_NO and a.corp_id =b.corp_id ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.PAYER_ACC", true));
        //查询对账单电子回单表
        sql.append(" union SELECT '' as id,A.BILL_NO as billNo, A.corp_id as corpId, A.status as status,A.BILL_TYPE as billType, ");
        sql.append(" '' as voucherNo,'' as oldVoucher, ");
        sql.append(" null as tallyDate,B.PRINTS as prints,'' as oldVoucherId ");
        sql.append("from (select DISTINCT \n" +
                "                a.acc_no,\n" +
//                "                a.acc_name,\n" +
                "                a.corp_id,\n" +
                "                a.bill_type,\n" +
                "                a.bill_no,\n" +
//                "                a.cur_id,\n" +
//                "                a.status,\n" +
                "                a.status\n" +
//                "                a.prints ,\n" +
//                "                a.ENCRY_DATE " +
                "  from e_compare_bill a ) a, e_bill_print b where a.bill_no = b.bill_no and a.corp_id =b.corp_id ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        sql.append(" and ").append(SqlUtil.getInSql(accList, "A.ACC_NO", true));
        sql.append(") A ");
//        sql.append(" LEFT JOIN fc_accbook c ON A.voucherNo = c.VOUCHER_NO");
//        sql.append(" LEFT JOIN fc_accbook_blned cb ON A.voucherNo = cb.VOUCHER_NO");
        sql.append(" inner join E_RECEIPT_TYPE type on A.billType = type.bill_type");
        sql.append(" where type.valid_sign = '1'");
        return getDynamicBySQL(input, sql.toString(), EleAllBillVO.class, "A", false);
    }
}
