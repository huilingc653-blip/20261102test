package com.bytter.biz.transfer.swap.dao;
import com.bytter.biz.transfer.swap.entity.ErpHisDtlComField;
import com.bytter.biz.transfer.swap.enums.ErpInterConfigEnum;
import com.bytter.biz.transfer.swap.enums.HisDtlTemplateStatusEnum;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlComFieldVO;
import com.bytter.biz.transfer.swap.vo.ErpHisDtlVO;
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
 * 收付补录字段持久
 */
@Repository
public class ErpHisDtlComFieldDao extends BaseDao<ErpHisDtlComField,String> {
    public ErpHisDtlComField getByTypeAndCode(ErpHisDtlComField config) {
        String hql = "from ErpHisDtlComField where type=?0 and fieldCode=?1";
        return getUnique(hql, config.getType(), config.getFieldCode());
    }
    public List<ErpHisDtlComField> getByTemplateId(String templateId) {
        String sql = "select template_id as templateId,field_code as fieldCode, text_value as textValue from ERP_HIS_DTL_COMFIELD where template_Id =:templateId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("templateId",templateId);
        return this.listBySQLAliasToBean(sql.toString(),params,ErpHisDtlComField.class);
    }
    public int deleteEntity(String templateId){
        String hql ="delete from ErpHisDtlComField where templateId=?0 ";
        return this.batchExecute(hql,templateId);
    }
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList,String code) {
        StringBuilder sql = new StringBuilder();
//        sql.append("select sendReason,sendBackNo,status,dtlId,accNo,corpId,bankAcc,accName,bankName,bifCode,oppAccNo,");
//        sql.append("oppAccName,oppAccBank,cdSign,amt,bal,curId,uses,postscript,remark,transTime,opName,opDate,ts.* from ( ");
        sql.append(" select h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id,h.status,t.id as dtlId,");
        sql.append(" t.acc_no as accNo,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,t.bank_name as bankName,");
        sql.append(" t.bif_code as bifCode,t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,");
        sql.append(" t.amt,t.bal,t.cur_id as curId,t.uses,t.postscript,t.remark,t.trans_time as transTime,su.username opName,h.update_date opDate ");
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
//        sql.append(" ) temp left join ");
//        sql.append(appendSql(code));
//        sql.append(" ts on ts.template_id=temp.template_id");
        return this.getDataTablesOutputBySQLAliasToBean(dataTablesInput, sql.toString().replaceAll("order by","ORDER BY"), allMap, ErpTemplateStoreVO.class);
    }
    private String appendSql(String code){
        StringBuilder sql = new StringBuilder();
        sql.append("(select * from (select template_id,field_code, text_value from erp_his_dtl_comfield)");
        sql.append("pivot(max(text_value) for field_code in (");
        sql.append(code);
        sql.append(")))");
        return sql.toString();
    }
    public List<ErpHisDtlComFieldVO> getListByTemplateId(String templateId,String type) {
        StringBuilder sql = new StringBuilder();
        sql.append("select e.type as type,e.field_code  as fieldCode,e.show_name as showName,");
        sql.append("e.is_not_null as isNotNull,e.ordernum as orderNum,e.is_input as isInput,e.data_source as dataSource,");
        sql.append("h.template_id as templateId,h.text_value as textValue,h.date_value as dateValule,");
        sql.append("h.int_value as intValue,h.reverse1 as reverse1,h.reverse2 as reverse2 ");
        sql.append("from erp_inter_config_fill e ");
        sql.append("left join erp_his_dtl_comfield h on e.type=h.type and e.field_code=h.field_code and e.valid_sign='1' ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type",type);
        if(StringUtils.isNotEmpty(templateId)) {
            sql.append(" and h.template_id =:templateId");
            params.put("templateId",templateId);
        }else{
            sql.append(" and h.template_id is null ");
        }
        sql.append(" where e.type =:type ");
        return this.listBySQLAliasToBean(sql.toString(),params,ErpHisDtlComFieldVO.class);
    }
    public ErpHisDtlVO selectInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select h.send_reson as sendReason,h.send_back_no as sendBackNo,h.template_id as templateId,h.status as status,");
        sql.append("t.id as dtlId,t.acc_no as acc_no,acc.corp_id as corpId,t.bank_acc as bankAcc,t.acc_name as accName,");
        sql.append("t.bank_name as bankName,t.bif_code as bifCode,t.opp_acc_no as oppAccNo,t.opp_acc_name as oppAccName,");
        sql.append("t.opp_acc_bank as oppAccBank,t.cd_sign as cdSign,t.amt as amt,t.bal as bal,t.cur_id as curId,t.uses as uses,");
        sql.append("t.remark as remark,t.trans_time as transTime,su.username as opName,h.update_date as opDate ");
        sql.append(" from bis_acc_dtl t left join bt_bank_acc acc on t.bank_acc = acc.bank_acc");
        sql.append(" left join erp_his_dtl_template h on h.serial_id = t.id");
        sql.append(" left join sys_user su on h.update_dy = su.id");
        sql.append(" where t.id=:id ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,ErpHisDtlVO.class);
    }
}
