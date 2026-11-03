package com.bytter.biz.bdlink.dao;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
@Repository
public class BisBankReceiptDtlDisposeDao extends BaseDao<BisBankReceiptDtlDispose, String> {
    public DataTablesOutput<BisBankReceiptDtlDisposeVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.id as id,d.bank_type_id as bankTypeId,d.is_enable as isEnable,d.cur_id as curId,d.trans_time as transTime,d.receipt_no as receiptNo, d.check_code as checkCode ,");
        sql.append(" d.cd_sign as cdSign,d.trans_amt as transAmt,d.pay_acc_no as payAccNo, d.pay_acc_name as payAccName,d.pay_branch_name as payBranchName,");
        sql.append(" d.payee_acc_no as payeeAccNO,d.payee_acc_name as payeeAccName,d.payee_bracn_name as payeeBracnName,d.usefor as usefor,d.remark1 as remark1, ");
        sql.append(" d.remark2 as remark2,d.create_date as createDate, d.create_by as createBy, d.update_date as updateDate,d.update_by as updateBy ");
        sql.append(" from BIS_BANK_RECEIPT_DTL_DISPOSE d where 1=1  ");
        DataTablesOutput output = getDynamicBySQL(dataTablesInput, sql.toString(), BisBankReceiptDtlDisposeVO.class, "d", false);
        return output;
    }
    public BisBankReceiptDtlDisposeVO getByIdUnion(String id) {
        String sql = "select * from BIS_BANK_RECEIPT_DTL_DISPOSE d where d.id=:id";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, BisBankReceiptDtlDisposeVO.class);
    }
    public BisBankReceiptDtlDisposeVO getByBankTypeId(String bankid) {
        String sql = "select * from BIS_BANK_RECEIPT_DTL_DISPOSE d where d.bank_type_id=:id";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", bankid);
        return getBySQLAliasToBean(sql, params, BisBankReceiptDtlDisposeVO.class);
    }
}
