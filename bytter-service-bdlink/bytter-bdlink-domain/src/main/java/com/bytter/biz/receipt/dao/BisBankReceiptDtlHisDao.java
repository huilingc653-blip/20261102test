package com.bytter.biz.receipt.dao;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.receipt.entity.BisBankReceiptDtlHis;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 银行电子回单Dao
 *
 * @author zuoy 2018-04-09
 */
@Repository
public class BisBankReceiptDtlHisDao extends BaseDao<BisBankReceiptDtlHis, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankReceiptDtlHisVO> getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        Map<String,Object> params = new HashMap<>();
        //交易时间
        String gedCharageDate = (String) input.getFormdata().get("ged_charageDate");
        String ledCharageDate = (String) input.getFormdata().get("led_charageDate");
        if(StringUtils.isNotBlank(gedCharageDate)){
            commonSql.append(" and t.charage_date >=:gedCharageDate");
            params.put("gedCharageDate", gedCharageDate);
        }
        if(StringUtils.isNotBlank(ledCharageDate)){
            commonSql.append(" and t.charage_date <=:ledCharageDate");
            params.put("ledCharageDate", ledCharageDate);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id, t.pay_acc_no, t.pay_acc_name, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no, t.payee_acc_name,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt, t.cur_id, t.usefor, t.abs, t.remark, t.bus_type, t.bus_no, t.charage_date, ");
        sql.append(" t.receipt_no, t.receipt_check_code, t.trans_serial_no, t.net_code, t.teller_no, bt_nvl_num(t.prints, 0) prints, t.cd_sign, ");
        sql.append(" t.bank_acc, t.acc_name, t.bank_name,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank ");
        sql.append(" FROM (");
        sql.append(" SELECT");
        sql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        sql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date, ");
        sql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, bt_nvl_num(b.prints, 0) prints, b.cd_sign, ");
        sql.append(" b.pay_acc_no as bank_acc, b.pay_acc_name as acc_name, b.pay_branch_name as bank_name, ");
        sql.append(" b.payee_acc_no as opp_acc_no, b.payee_acc_name as opp_acc_name, b.payee_bracn_name as opp_acc_bank");
        sql.append(" FROM BIS_BANK_RECEIPT_DTL_HIS b WHERE b.cd_sign = '1'");
        if(input.getFormdata().containsKey("bankAccGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("bankAccGrantList") == null ? null : (List<String>) input.getFormdata().get("bankAccGrantList");
            sql.append(" and (b.pay_acc_no is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.pay_acc_no", "bankAccs", params))
                    .append(" or b.payee_acc_no is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.payee_acc_no", "bankAccs2", params)).append(")");
        }
        sql.append(" UNION");
        sql.append(" SELECT");
        sql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        sql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date, ");
        sql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, bt_nvl_num(b.prints, 0) prints, b.cd_sign, ");
        sql.append(" b.payee_acc_no as bank_acc, b.payee_acc_name as acc_name, b.payee_bracn_name as bank_name, ");
        sql.append(" b.pay_acc_no as opp_acc_no, b.pay_acc_name as opp_acc_name, b.pay_branch_name as opp_acc_bank ");
        sql.append(" FROM BIS_BANK_RECEIPT_DTL_HIS b WHERE b.cd_sign = '0'");
        if(input.getFormdata().containsKey("bankAccGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = input.getFormdata().get("bankAccGrantList") == null ? null : (List<String>) input.getFormdata().get("bankAccGrantList");
            sql.append(" and (b.pay_acc_no is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.pay_acc_no", "bankAccs", params))
                    .append(" or b.payee_acc_no is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.payee_acc_no", "bankAccs2", params))
                    .append(")");
        }
        sql.append(" ) t");
        sql.append(" left join bt_bank_acc acc ON t.bank_acc = acc.bank_acc");
        sql.append(" WHERE 1=1");
        if (StringUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        //是否关联付款单
        String isRelBill = (String) input.getFormdata().get("eqs_isRelBill");
        //是否关联流水
        String isRelDtl = (String) input.getFormdata().get("eqs_isRelDtl");
        if (CommonConstant.YES.equals(isRelBill)) {
            sql.append(" and exists (select receipt_id from BIS_RECEIPT_DTL rd where rd.receipt_id = t.id and ");
            sql.append(" exists (select dtl_id from NIS_BILL_BANK_SERIAL_NO rb where rd.dtl_id = rb.dtl_id))");
        } else if (CommonConstant.NO.equals(isRelBill)){
            sql.append(" and t.cd_sign = '1'");
            sql.append(" and not exists (select receipt_id from BIS_RECEIPT_DTL rd where rd.receipt_id = t.id and ");
            sql.append(" exists (select dtl_id from NIS_BILL_BANK_SERIAL_NO rb where rd.dtl_id = rb.dtl_id))");
        }
        if (CommonConstant.YES.equals(isRelDtl) && !CommonConstant.YES.equals(isRelBill)) {
            sql.append(" and exists (select receipt_id from BIS_RECEIPT_DTL rd where rd.receipt_id = t.id)");
        } else if (CommonConstant.NO.equals(isRelDtl)) {
            sql.append(" and not exists (select receipt_id from BIS_RECEIPT_DTL rd where rd.receipt_id = t.id)");
        }
        return getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlHisVO.class, "t", false, params);
    }
	/**
	 * 更新打印次数
	 *
	 * @param id
	 */
	public void updatePrint(String id) {
		String hql = "update BisBankReceiptDtlHis set prints = bt_nvl_num(prints, 0) + 1  where id = :id ";
		Map<String, String> values = new HashMap<>();
		values.put("id", id);
		this.batchExecute(hql.toString(), values);
	}
	/**
	 * 根据付款ID获取电子回单信息
	 *
	 * @param id
	 * @return
	 */
	public BisBankReceiptDtlHisVO getByIdUnion(String id) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.id as id, b.pay_acc_no as payAccNo, b.pay_acc_name as payAccName, b.pay_branch_name as payBankName, b.bif_code_id as bifCode, b.payee_acc_no as payeeAccNo, b.payee_acc_name as payeeAccName,");
		sql.append(" b.payee_bracn_name as payeeBankName, b.trans_amt as transAmt, b.cur_id as curId, b.usefor as usefor, b.abs as abs, b.remark as remark, b.bus_type as busType, b.bus_no as busNo, b.charage_date as charageDate, ");
		sql.append(" b.receipt_no as receiptNo, b.receipt_check_code as receiptCheckCode, b.trans_serial_no as transSerialNo, b.net_code as netCode, b.teller_no as tellerNo, b.filetype as filetype, bt_nvl_num(b.prints, 0)  as prints,b.FILE_PATH as filePath ");
		sql.append(" from BIS_BANK_RECEIPT_DTL_HIS b ");
		sql.append(" where 1 = 1");
		sql.append(" and  b.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BisBankReceiptDtlHisVO.class);
	}
    public List<BisBankReceiptDtlHisVO> listByIds(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.pay_acc_no as payAccNo, b.pay_acc_name as payAccName, b.pay_branch_name as payBankName, b.bif_code_id as bifCode, b.payee_acc_no as payeeAccNo, b.payee_acc_name as payeeAccName,");
        sql.append(" b.payee_bracn_name as payeeBankName, b.trans_amt as transAmt, b.cur_id as curId, b.usefor as usefor, b.abs as abs, b.remark as remark, b.bus_type as busType, b.bus_no as busNo, b.charage_date as charageDate, ");
        sql.append(" b.receipt_no as receiptNo, b.receipt_check_code as receiptCheckCode, b.trans_serial_no as transSerialNo, b.net_code as netCode, b.teller_no as tellerNo, b.filetype as filetype, bt_nvl_num(b.prints, 0)  as prints,b.FILE_PATH as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL_HIS b ");
        sql.append(" where 1 = 1");
        sql.append(" and  ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "b.id", true));
        return listBySQLAliasToBean(sql.toString(), null, BisBankReceiptDtlHisVO.class);
    }
}
