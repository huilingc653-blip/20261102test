package com.bytter.biz.finacial.tally.dao;
import com.bytter.biz.finacial.api.dto.FcAccbookImportDTO;
import com.bytter.biz.finacial.tally.entity.FcAccbookImport;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 凭证导入Dao
 */
@Repository
public class VoucherImportDao extends BaseDao<FcAccbookImport, String> {
    public DataTablesOutput<FcAccbookImportDTO> voucherImpDatatable(@RequestBody DataTablesInput dataTablesInput, String userId) {
        String sql = " select f.id as id,  f.tally_date as tallyDate,f.interest_start_date as interestStartDate," +
                " f.summary_content as summaryContent,f.line_no as lineNo, f.account as account," +
                " f.debit_money as debitMoney, f.credit_money as creditMoney,f.voucher_type as voucherType, " +
//                " v.voucher_name as voucherName,f.rmk as rmk,f.affix_amount as affixAmount,fb.race_name as raceName " +
                " f.rmk as rmk,f.affix_amount as affixAmount,fb.race_name as raceName " +
                " from FC_ACCBOOK_IMPORT f " +
//                " left join Fc_Voucher_Type v on f.voucher_type = v.id" +
                " left join fc_bus_race fb on f.bus_race_id = fb.id" +
                " where 1=1 " +
                " and operator_code = " + "'" + userId + "' order by f.create_date";
        return getDynamicBySQL(dataTablesInput, sql, FcAccbookImportDTO.class, "", false);
    }
    /**
     * 查询内部账号是否存在
     **/
    public FcAccDTO isEmptyAccount(String account) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("account", account);
        return getBySQLAliasToBean("select * from fc_acc where blnctr_acc = :account", param, FcAccDTO.class);
    }
    public List<FcAccbookImportDTO> getImprotVoucher(List<String> ids) {
        String sql = "select f.* from  fc_accbook_import f where 1=1 " +
                " and " + getSQLIn(ids, 900, "f.id") +
                " order by f.create_date , (f.debit_money + f.credit_money), f.line_no asc ";
        return listBySQLAliasToBean(sql, null, FcAccbookImportDTO.class);
    }
    public void deleteImportVoucher(List<String> ids) {
        String sql = "delete from fc_accbook_import " +
                " where " + getSQLIn(ids, 900, "id");
        createSQLQuery(sql).executeUpdate();
    }
}
