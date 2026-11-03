package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplateStore;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateStatusEnum;
import com.bytter.biz.transfer.swap.vo.ErpTemplateBillVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateStoreVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2018/5/29.
 */
@Repository
public class ErpTemplateStoreDao extends BaseDao<ErpTemplateStore,String> {
    public String selectBisAccDtlSql(DataTablesInput dataTablesInput,Map<String, Object> params, List<String> accStrList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id ,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time ");
        sql.append(" from bis_acc_dtl t where 1 = 1 ");

        String corpAuthSql = SqlUtil.getInSqlByParams(accStrList, "t.bank_acc", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        String dealType = (String) dataTablesInput.getFormdata().get("dealType");
        //付款
        if(ErpInterConfigEnum.PAYER.getCode().equals(dealType)){
            params.put("eqcdSign",1);
        }
        //收款
        if(ErpInterConfigEnum.PAYEE.getCode().equals(dealType)){
            params.put("eqcdSign",0);
        }
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
            params.put("getransTime",gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
            params.put("letransTime",ledTransTime);
        }
//        dataTablesInput.getFormdata().set("dealType");
        Map<String, Object> result = getWhereConditionAndValues(dataTablesInput,new ErpTemplateStoreVO(),"t",false);
        params.putAll((Map<String, Object>)result.get("value"));
        sql.append(result.get("where"));
        return sql.toString();
    }
    public ErpTemplateStoreVO getErpTemplateStoreVO(DataTablesInput dataTablesInput, String serialId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_name as templateName,ts.type_name as typeName,");
        sql.append(" h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,");
        sql.append(" case when h.status = 0 then '0'  when h.status = 1 then '1'  when h.status = 2 then '2'  when h.status = 10 then '10' ");
        sql.append("  when h.status = -2 then '-2'  when h.status = -1 then '-1'   when h.status is null then 'null' end as statusName,");
        sql.append("  h.update_dy AS opName,h.update_date opDate ");
        sql.append(" from erp_his_dtl_template h ");
        sql.append(" LEFT JOIN erp_template_store ts ON ts.id = h.template_id");
        sql.append(" where 1 = 1 ");
        Map<String, Object> allMap = new HashMap<String, Object>();
        String status = (String) dataTablesInput.getFormdata().get("status");
        if(StringUtils.isNotBlank(status)){
            if(String.valueOf(HisDtlTemplateStatusEnum.ISALL.getStatus()).equals(status)){//全部
                status = "0";
                sql.append(" and (h.status=:status or h.status is null or h.status='-1' ) ");
            }else if(String.valueOf(HisDtlTemplateStatusEnum.ISNULL.getStatus()).equals(status)){
                sql.append(" and h.status is null ");
            }else{
                sql.append(" and h.status=:status ");
            }
            allMap.put("status",status);
        }
        if(StringUtils.isNotBlank(serialId)) {
            sql.append(" and h.serial_id=:serial_id ");
            allMap.put("serial_id", serialId);
        }

        Map<String, Object> result = getWhereConditionAndValues(dataTablesInput,new ErpTemplateStoreVO(),"t",false);
        allMap.putAll((Map<String, Object>)result.get("value"));
        sql.append(result.get("where"));
        return getBySQLAliasToBean(sql.toString(),allMap,ErpTemplateStoreVO.class);
    }
    public String selectBisAccDtlByIdSql( String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id ,t.acc_no,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time ");
        sql.append(" from bis_acc_dtl t where t.id=:id ");
        return sql.toString();
    }
    public ErpTemplateStoreVO getErpTemplateStoreVOById(String serialId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_name as templateName,ts.type_name as typeName,");
        sql.append(" h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,");
        sql.append(" case when h.status = 0 then '0'  when h.status = 1 then '1'  when h.status = 2 then '2'  when h.status = 10 then '10' ");
        sql.append("  when h.status = -2 then '-2'  when h.status = -1 then '-1'   when h.status is null then 'null' end as statusName,");
        sql.append("  h.update_dy AS opName,h.update_date opDate ");
        sql.append(" from erp_his_dtl_template h ");
        sql.append(" LEFT JOIN erp_template_store ts ON ts.id = h.template_id");
        sql.append(" where 1 = 1 ");
        Map<String, Object> allMap = new HashMap<String, Object>();
        if(StringUtils.isNotBlank(serialId)) {
            sql.append(" and h.serial_id=:serial_id ");
            allMap.put("serial_id", serialId);
        }
        return getBySQLAliasToBean(sql.toString(),allMap,ErpTemplateStoreVO.class);
    }
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_name as templateName,ts.type_name as typeName,temp.* from ( ");
        sql.append(" select h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,t.id as dtlId,t.acc_no,acc.corp_id as corpId,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" case when h.status = 0 then '0'  when h.status = 1 then '1'  when h.status = 2 then '2'  when h.status = 10 then '10' ");
        sql.append("  when h.status = -2 then '-2'  when h.status = -1 then '-1'   when h.status is null then 'null' end as statusName,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,su.username opName,h.update_date opDate ");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc");
        sql.append(" left join erp_his_dtl_template h on h.serial_id = t.id");
        sql.append(" left join sys_user su on h.update_dy = su.id");
        sql.append(" where 1 = 1 ");
        Map<String, Object> allMap = new HashMap<String, Object>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "acc.corp_id", allMap);
        sql.append(" and ").append(corpAuthSql).append(" ");
        String status = (String) dataTablesInput.getFormdata().get("status");
        if(StringUtils.isNotBlank(status)){
            if(String.valueOf(HisDtlTemplateStatusEnum.ISALL.getStatus()).equals(status)){//全部
                status = "0";
                sql.append(" and (h.status=:status or h.status is null or h.status='-1' ) ");
            }else if(String.valueOf(HisDtlTemplateStatusEnum.ISNULL.getStatus()).equals(status)){
                sql.append(" and h.status is null ");
            }else{
                sql.append(" and h.status=:status ");
            }
            allMap.put("status",status);
        }
        String dealType = (String) dataTablesInput.getFormdata().get("dealType");
        //付款
        if(ErpInterConfigEnum.PAYER.getCode().equals(dealType)){
            dataTablesInput.getFormdata().put("eqs_cdSign", 1);
        }
        //收款
        if(ErpInterConfigEnum.PAYEE.getCode().equals(dealType)){
            dataTablesInput.getFormdata().put("eqs_cdSign", 0);
        }
        //交易时间
        String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
        String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
        if (StringUtils.isNotEmpty(gedTransTime)) {
            gedTransTime = gedTransTime + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
        }
        if (StringUtils.isNotEmpty(ledTransTime)) {
            ledTransTime = ledTransTime + " 23:59:59";
            dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
        }
        Map<String, Object> result = getWhereConditionAndValues(dataTablesInput,new ErpTemplateStoreVO(),"t",false);
        allMap.putAll((Map<String, Object>)result.get("value"));
        sql.append(result.get("where"));
        sql.append(" ) temp left join erp_template_store ts on ts.id=temp.template_id");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString().replaceAll("order by","ORDER BY"), allMap, ErpTemplateStoreVO.class);
    }
    public ErpTemplateStoreVO selectBankccById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_id as templateId,ts.template_name as templateName,ts.type_name as typeName,ts.id as storeId,temp.* from ( ");
        sql.append(" select h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,t.id as dtlId,t.acc_no,acc.corp_id as corpId,t.bank_acc,t.acc_name,t.bank_name,t.bif_code,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.cd_sign,t.rb_sign,");
        sql.append(" t.amt,t.bal,t.cur_id,t.uses,t.postscript,t.remark,t.trans_time,su.username opName,h.update_date opDate ");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc");
        sql.append(" left join erp_his_dtl_template h on h.serial_id = t.id");
        sql.append(" left join sys_user su on h.update_dy = su.id");
        sql.append(" where t.id=:id ");
        sql.append(" ) temp LEFT JOIN erp_template_store ts ON ts.id = temp.template_id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,ErpTemplateStoreVO.class);
    }
    public DataTablesOutput getDataTablebill(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_name as templateName,ts.type_name as typeName,temp.* from ( ");
        sql.append(" SELECT h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,b.trans_time as transTime,b.maturity_date as maturityDate,b.id,b.corp_id AS corpId,b.corp_name AS corpName,b.abs,b.rmk,b.money,b.acceptor,b.acceptorbankno as acceptorBankNo,b.acceptoracc as acceptorAcc,b.acceptor_bank_name as acceptorBankName, ");
        sql.append(" b.bill_type as billType,b.bill_flag as billFlag,b.operation_Id as tradeType,b.code,b.payer,b.payer_bank_name as payerName,b.payer_bank_acc as payerAcc,b.receiver,b.receiver_bank_name as receiverName,b.receiver_bank_acc as receiverAcc,su.username opName,h.update_date opDate,b.BILL_OP_VESTING_DATE as businessDate ");
        sql.append(" from v_erp_bill b LEFT JOIN erp_his_dtl_template h ON h.serial_id = b.id ");
        sql.append(" left join sys_user su on h.update_dy = su.id ");
        sql.append(" where 1 = 1 ");
        Map<String, Object> allMap = new HashMap<String, Object>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", allMap);
        sql.append(" and ").append(corpAuthSql).append(" ");
        String status = (String) dataTablesInput.getFormdata().get("status");
        if(StringUtils.isNotBlank(status)){
            if(String.valueOf(HisDtlTemplateStatusEnum.ISALL.getStatus()).equals(status)){//预处理
                status = "0";
                sql.append(" and (h.status=:status or h.status is null or h.status='-1' ) ");
            }else if(String.valueOf(HisDtlTemplateStatusEnum.ISNULL.getStatus()).equals(status)){
                sql.append(" and h.status is null ");
            }else{
                sql.append(" and h.status=:status ");
            }
            allMap.put("status",status);
        }
        Map<String, Object> result = getWhereConditionAndValues(dataTablesInput,new ErpTemplateBillVO(),"b",false);
        allMap.putAll((Map<String, Object>)result.get("value"));
        sql.append(result.get("where"));
        sql.append(" ) temp left join erp_template_store ts on ts.id=temp.template_id");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString().replaceAll("order by","ORDER BY"), allMap, ErpTemplateBillVO.class);
    }
    public ErpTemplateBillVO selectBillById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ts.template_id as templateId,ts.template_name as templateName,ts.type_name as typeName,ts.id as storeId,temp.* from ( ");
        sql.append(" SELECT h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,b.trans_time as transTime,b.maturity_date as maturityDate,b.id,b.corp_id AS corpId,b.corp_name AS corpName,b.abs,b.rmk,b.money,b.acceptor,b.acceptorbankno as acceptorBankNo,b.acceptoracc as acceptorAcc,b.acceptor_bank_name as acceptorBankName, ");
        sql.append(" b.bill_type as billType,b.bill_flag as billFlag,b.operation_Id as tradeType,b.code,b.payer,b.payer_bank_name as payerName,b.payer_bank_acc as payerAcc,b.receiver,b.receiver_bank_name as receiverName,b.receiver_bank_acc as receiverAcc,su.username opName,h.update_date opDate ");
        sql.append(" from v_erp_bill b LEFT JOIN erp_his_dtl_template h ON h.serial_id = b.id ");
        sql.append(" left join sys_user su on h.update_dy = su.id ");
        sql.append(" where b.id=:id ");
        sql.append(" ) temp LEFT JOIN erp_template_store ts ON ts.id = temp.template_id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,ErpTemplateBillVO.class);
    }
    /**
     * 获取补录中间映射信息
     * @param serialId
     * @return
     */
    public ErpHisDtlTemplate getErpHisDtlTemplate(String serialId){
        StringBuilder sql = new StringBuilder("select id,serial_Id as serialId,TEMPLATE_ID as TEMPLATEID,status,type from ERP_HIS_DTL_TEMPLATE where serial_Id =:serialId");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serialId",serialId);
        return getBySQLAliasToBean(sql.toString(),params,ErpHisDtlTemplate.class);
    }
}
