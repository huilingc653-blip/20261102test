package com.bytter.biz.reconcile.dao;

import com.bytter.biz.bdlink.vo.BisBankMonthStatementDtlVO;
import com.bytter.biz.reconcile.entity.BisBankMonthStatementDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
/**
 * 银行对账单Dao
 *
 * @author huangx 2022-12-01
 */
@Repository
public class BisBankMonthStatementDtlDao extends BaseDao<BisBankMonthStatementDtl, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.customer_Name as customerName, t.customer_Account as customerAccount, t.bill_Bengin_Date as billBenginDate, t.bill_End_Date as billEndDate, t.closing_Balance as closingBalance,");
        sql.append(" t.total_Number as totalNumber, t.total_Credit_Amt as totalCreditAmt, t.cur_id as curId, t.bif_Code_Id as bifCodeId, t.save_Type as saveType, t.file_Path as filePath, t.file_Type as fileType,t.create_Time as createTime, ");
        sql.append(" t.reserve1, t.reserve2, t.reserve3, t.reserve4, t.reserve5 ");
        sql.append(" from Bis_Bank_Month_Statement_Dtl t ");
        sql.append(" WHERE 1=1 ");
        sql.append(commonSql);
        String billDate = (String) input.getFormdata().get("billDate");
        if(StringUtils.isNotEmpty(billDate)){
            input.getFormdata().put("ged_billEndDate", billDate);
            input.getFormdata().put("led_billBenginDate", billDate);
        }
        return getDynamicBySQL(input, sql.toString(), BisBankMonthStatementDtlVO.class, "t", false);
    }
    /**
     * 根据ID获取银行对账单
     *
     * @param id
     * @return
     */
    public BisBankMonthStatementDtlVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.customer_Name as customerName, t.customer_Account as customerAccount, t.bill_Bengin_Date as billBenginDate, t.bill_End_Date as billEndDate, t.closing_Balance as closingBalance,");
        sql.append(" t.total_Number as totalNumber, t.total_Credit_Amt as totalCreditAmt, t.cur_id as curId, t.bif_Code_Id as bifCodeId, t.save_Type as saveType, t.file_Path as filePath, t.file_Type as fileType,t.create_Time as createTime, ");
        sql.append(" t.reserve1, t.reserve2, t.reserve3, t.reserve4, t.reserve5,acc.corp_id as corpId,acc.acc_name as customerAccountName,b.name as bifName,pdf.statement_Img as statementImg ");
        sql.append(" from Bis_Bank_Month_Statement_Dtl t left join bt_bank_acc acc on t.customer_Account=acc.bank_acc");
        sql.append(" inner join Bis_Bank_Month_Statement_Pdf pdf on pdf.id=t.id");
        sql.append(" left join bis_bif_init b on t.bif_Code_Id=b.id");
        sql.append(" WHERE t.id=:id ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisBankMonthStatementDtlVO.class);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param ids
     * @return
     */
    public List<BisBankMonthStatementDtlVO> listByIds(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id, t.customer_Name as customerName, t.customer_Account as customerAccount, t.bill_Bengin_Date as billBenginDate, t.bill_End_Date as billEndDate, t.closing_Balance as closingBalance,");
        sql.append(" t.total_Number as totalNumber, t.total_Credit_Amt as totalCreditAmt, t.cur_id as curId, t.bif_Code_Id as bifCodeId, t.save_Type as saveType, t.file_Path as filePath, t.file_Type as fileType,t.create_Time as createTime, ");
        sql.append(" t.reserve1, t.reserve2, t.reserve3, t.reserve4, t.reserve5,acc.corp_id as corpId,acc.acc_name as customerAccountName,b.name as bifName,pdf.statement_Img as statementImg ");
        sql.append(" from Bis_Bank_Month_Statement_Dtl t left join bt_bank_acc acc on t.customer_Account=acc.bank_acc");
        sql.append(" inner join Bis_Bank_Month_Statement_Pdf pdf on pdf.id=t.id");
        sql.append(" left join bis_bif_init b on t.bif_Code_Id=b.id");
        sql.append(" where  ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "t.id", true));
        return listBySQLAliasToBean(sql.toString(), null, BisBankMonthStatementDtlVO.class);
    }
}
