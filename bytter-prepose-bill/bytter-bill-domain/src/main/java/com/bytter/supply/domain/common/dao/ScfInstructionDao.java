package com.bytter.supply.domain.common.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.supply.constant.ScfEntBankOpenConstant;
import com.bytter.supply.domain.common.entity.ScfInstruction;
import com.bytter.supply.domain.common.vo.ScfInstructionVO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @Title: ScfInstructionDao
 * @Description: 指令管理DAO
 */
@Repository
public class ScfInstructionDao extends BaseDao<ScfInstruction, String> {

    /**
     * 根据凭证编码和指令类型查询指令数量
     */
    public int countByVoucherCodeAndInstrType(String voucherCode, String instrType, String sign) {
        String sql = " select count(1) from scf_instruction t where t.relate_no = ? and t.instr_type = ? and t.sign = ? ";
        int count = ((Number) createSQLQuery(sql, voucherCode, instrType, sign).uniqueResult()).intValue();
        return count;
    }

    /**
     * 列表查询
     *
     * @param dataTablesInput 查询条件
     * @return 分页结果
     */
    public DataTablesOutput<ScfInstructionVO> dataTable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.instr_code, t.instr_type, t.instr_type_name, t.platform_id, t.platform_name, ");
        sql.append("        t.product_id, t.product_name, t.direct_if_id, t.voucher_code, t.relate_no, t.corp_id, t.corp_name, ");
        sql.append("        t.cur_id, t.cur_name, t.instr_amt, t.instr_date, t.expire_date, ");
        sql.append("        t.trans_status, t.trans_reason, t.sign, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date ");
        sql.append(" from scf_instruction t where 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfInstructionVO.class, "", false);
    }

    /**
     * 根据sign标识查询指令列表（联查接收单位、资金方）
     */
    public DataTablesOutput<ScfInstructionVO> dataTableBySign(DataTablesInput dataTablesInput, String sign) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.instr_code, t.instr_type, t.instr_type_name, t.platform_id, t.platform_name, ");
        sql.append("        t.product_id, t.product_name, t.direct_if_id, t.voucher_code, t.relate_no, t.corp_id, t.corp_name, ");
        sql.append("        t.cur_id, t.cur_name, t.instr_amt, t.instr_date, t.expire_date, ");
        sql.append("        t.trans_status, t.trans_reason, t.sign, ");
        sql.append("        t.create_by, t.create_date, t.update_by, t.update_date, ");
        sql.append("        rti.receive_company as receiveCompany, ");
        sql.append("        li.funder_name as funderName ");
        sql.append(" from scf_instruction t ");
        sql.append(" left join scf_receivable_transfer_info rti on t.relate_no = rti.transfer_no and t.instr_type in ('");
        sql.append(ScfEntBankOpenConstant.RECEIVABLE_VOUCHER_TRANSFER).append("', '");
        sql.append(ScfEntBankOpenConstant.RECEIVABLE_TRANSFER_RESULT).append("') ");
        sql.append(" left join scf_loan_info li on t.relate_no = li.loan_no and t.instr_type in ('");
        sql.append(ScfEntBankOpenConstant.RECEIVABLE_FINANCE_APPLY).append("', '");
        sql.append(ScfEntBankOpenConstant.RECEIVABLE_FINANCE_RESULT_QUERY).append("') ");
        sql.append(" where t.sign = '" + sign + "' ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfInstructionVO.class, "", false);
    }

}
