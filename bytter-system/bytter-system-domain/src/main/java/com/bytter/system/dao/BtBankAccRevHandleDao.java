package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.entity.BtBankAccApp;
import org.springframework.stereotype.Repository;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@Repository
public class BtBankAccRevHandleDao extends BaseDao<BtBankAccApp, String> {
    /**
     *@Description 查询银行账号变更记录
     *@Param dataTablesInput
     *@Param joinSql
     *@return DataTablesOutput
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.acc_no as accNo,acc.bank_acc as bankAcc,acc.acc_name as accName,acc.corp_id as corpId,c.name AS corpName, acc.status as status ,");
        sql.append(" acc.acc_type as accType,att.id as attributeId,nat.nature_name as natureName, att.attribute_name as attributeName,");
        sql.append(" nat.id as natureId,acc.bank_name as bankName,acc.bank_type_id as bankTypeId, ");
        sql.append(" acc.prov as prov,acc.city as city,acc.electric_bill as electricBill,acc.op_ression as opRession, ");
        sql.append(" acc.attachment_id as attachmentId, 'app' as isForm, acc.is_domestic_bank as isDomesticBank, ");
        sql.append(" b.acc_use_code as accUseCode ");
        sql.append(" from bt_bank_acc_app acc ");
        sql.append(" left join sys_corp c on c.id = acc.corp_id ");
        sql.append(" left join BIS_ACC_ATTRIBUTE att on acc.attribute_id = att.id ");
        sql.append(" left join bt_acc_nature nat on acc.nature_id = nat.id ");
        sql.append(" left join bt_bank_acc b on acc.bank_acc = b.bank_acc ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(" and ").append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BtBankAccAppDTO.class, "", false);
    }
}
