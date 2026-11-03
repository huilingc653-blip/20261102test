package com.bytter.biz.capital.dao;
import com.bytter.biz.capital.entity.NisCapitalRecord;
import com.bytter.biz.capital.vo.NisCapitalRecordVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@Repository
public class NisCapitalRecordDao extends BaseDao<NisCapitalRecord,String> {
    
    public DataTablesOutput<NisCapitalRecordVo> getDataTableUnion(DataTablesInput dataTablesInput, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id, c.corp_id as corpId, c.payee_acc as payeeAcc,c.payee_acc_name as payeeAccName, c.bus_type as busType, ");
        sql.append(" c.pay_money as payMoney, c.wish_payday as wishPayDay, c.project_name as projectName, ");
        sql.append(" c.purpose as purpose, c.voucher_type as voucherType, c.net_id as netId, c.contract_code as contractCode, ");
        sql.append(" c.contract_name as contractName, c.contract_type as contractType, c.create_date as createDate,c.status as status ");
        sql.append(" from nis_capital_record c ");
        sql.append(" where 1 = 1  ");
        sql.append(" and "+ SqlUtil.getInSql(corpIds,"c.corp_id",true));
        return getDynamicBySQL(dataTablesInput,sql.toString(), NisCapitalRecordVo.class,"c",false,null);
    }
    public NisCapitalRecordVo getNisCapitalRecordVoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select c.id as id, c.corp_id as corpId, c.payee_acc as payeeAcc, c.bus_type as busType, ");
        sql.append(" c.pay_money as payMoney, c.wish_payday as wishPayDay, c.project_name as projectName, ");
        sql.append(" c.purpose as purpose, c.voucher_type as voucherType, c.net_id as netId, c.contract_code as contractCode, ");
        sql.append(" c.contract_name as contractName, c.contract_type as contractType, c.create_date as createDate,c.status as status, ");
        sql.append(" c.payee_acc_name as payeeAccName, c.payee_bank_code as payeeBankCode, c.payee_bank as payeeBank,c.attachment_id as attachmentId ");
        sql.append(" from nis_capital_record c where 1 = 1 ");
        sql.append(" and c.id = :id ");
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,NisCapitalRecordVo.class);
    }
}
