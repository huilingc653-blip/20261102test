package com.bytter.accountReceivable.docManage.dao;

import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.docManage.vo.DocManageApproveVo;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @author yangxd
 * @date 2025-03-26
 * @version 1.0
 *
 */
@Repository
public class DocManageDao extends BaseDao<DocManage, String>{


    public DataTablesOutput listDocManage(DataTablesInput input,String CorpInSql) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder("select");
        sql.append(" dm.id, dm.code, dm.corp_id as corpId, dm.batch_Bus_Type as batchBusType, nbt.BUS_TYPE_NAME as batchBusTypeName,");
        sql.append(" dm.source, dm.voucher_type  as voucherType,reason_Status as reasonStatus,");
        sql.append(" dm.status, dm.receipt_date as receiptDate, dm.end_date  as endDate, dm.aging_date  as agingDate,");
        sql.append(" dm.receivable_money  as receivableMoney, dm.cur_id  as curId, dm.external_name  as externalName,");
        sql.append(" dm.external_type  as externalType, dm.contract_code  as contractCode,");
        sql.append(" sc.name as corpName,bc.CUR_NAME as curName");
        sql.append(" from  doc_manage dm  left join NIS_BUS_TYPE  nbt on dm.BATCH_BUS_TYPE =  nbt.id");
        sql.append(" left join sys_corp sc on sc.id = dm.CORP_ID ");
        sql.append(" left join bt_currency bc  on bc.id = dm.CUR_ID ");
        sql.append("  where 1=1");
        sql.append(" and dm.write_off_status = '0'");
        sql.append(" and").append(CorpInSql);
        Object eqiStatus = input.getFormdata().get("ins_status");
        if(eqiStatus == null || "".equals(eqiStatus)){
            sql.append(" and dm.status not in ('-2','-3') ");
        }
        return getDynamicBySQL(input,sql.toString(), DocManageVo.class,"dm",false,params);
    }


    public DataTablesOutput listDocManageApprove(DataTablesInput input,String CorpInSql,String businessSql) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder("select");
        sql.append(" dm.id, dm.code, dm.corp_id as corpId, dm.batch_Bus_Type as batchBusType, nbt.BUS_TYPE_NAME as batchBusTypeName, dm.source, dm.voucher_type  as voucherType,");
        sql.append(" dm.status, dm.receipt_date as receiptDate, dm.end_date  as endDate, dm.aging_date  as agingDate,");
        sql.append(" dm.receivable_money  as receivableMoney, dm.cur_id  as curId, dm.external_name  as externalName,");
        sql.append(" dm.external_type  as externalType, dm.contract_code  as contractCode,");
        sql.append(" sc.NAME as corpName,bc.CUR_NAME as curName ");
        sql.append(" from  doc_manage dm ");
        sql.append(" left join nis_bus_type nbt on ");
        sql.append(" dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" left join sys_corp sc on dm.CORP_ID = sc.id ");
        sql.append(" left join bt_currency bc on dm.CUR_ID = bc.id ");
        sql.append(" where 1=1 ");
        sql.append(" and dm.write_off_status = '0'");
        sql.append(" and").append(CorpInSql);
        sql.append(" and ").append(businessSql);
        return getDynamicBySQL(input,sql.toString(), DocManageApproveVo.class,"dm",false,params);
    }



    public DocManageVo queryDocManageById(String id) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder("select * from  doc_manage dm  where 1=1");
        sql.append(" and id =:id");
        sql.append(" ");
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params,DocManageVo.class);
    }
    public List<DocManageVo> queryDocManageByIds(String idsInSql,String condition) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder("select * from  doc_manage dm  where 1=1");
        sql.append(" and ").append(idsInSql);
        if (StringUtils.isNotEmpty(condition))
            sql.append(" and ").append(condition);
    return listBySQLAliasToBean(sql.toString(),params,DocManageVo.class);
    }

    /**
     * 获取应收查询公共sql
     * @return
     */
    public String getQueryDocManageCommonSql(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select dm.id, dm.code, dm.corp_id as corpId, dm.source, dm.voucher_type  as voucherType,");
        sql.append(" dm.status, dm.receipt_date as receiptDate, dm.end_date  as endDate, dm.aging_date  as agingDate,");
        sql.append(" dm.receivable_money  as receivableMoney, dm.cur_id  as curId, dm.external_name  as externalName,");
        sql.append(" dm.external_type  as externalType, dm.contract_code  as contractCode, dm.contract_name  as contractName, dm.contract_money  as contractMoney, dm.contract_date  as contractDate, ");
        sql.append(" dm.payee_acc as payeeAcc, dm.payee_acc_name  as payeeAccName, dm.payee_bank_code  as payeeBankCode, dm.payee_bank_name as payeeBankName, ");
        sql.append(" dm.payer_acc_no as payerAccNo, dm.payer_acc_name  as payerAccName, dm.payer_bank_name  as payerBankName, dm.payer_bank_no as payerBankNo ");
        sql.append(" ,case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end as writeOffBal ");
        sql.append(" ,dm.write_off_type as writeOffType, dm.write_off_status as writeOffStatus ");
        sql.append(" ,dm.BATCH_BUS_TYPE as batchBusType, nbt.BUS_TYPE_NAME as batchBusTypeName,dm.REASON_TITLE as reasonTitle , dm.REASON_CONTENT as reasonContent ");
        sql.append(" ,dm.attachment_id as attachmentId, dm.CONTRACT_ATTACHMENT_ID as contractAttachmentId, ");
        sql.append(" sc.name as corpName,bc.CUR_NAME  as curName ");
        sql.append(" from  doc_manage dm ");
        sql.append(" left join nis_bus_type nbt on ");
        sql.append(" dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" left join sys_corp sc on dm.CORP_ID = sc.ID ");
        sql.append(" left join bt_currency bc on dm.CUR_ID = bc.ID");
        sql.append(" where 1=1 ");

        return sql.toString();
    }
    /**
     * 应收查询 列表查询
     * @param input
     * @param corpInSql
     * @return
     */
    public DataTablesOutput listQueryDocManage(DataTablesInput input,String corpInSql) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(this.getQueryDocManageCommonSql());
        sql.append(" and dm.status = 95 ");
        if(StringUtils.isNotEmpty(corpInSql)){
            sql.append(" and").append(corpInSql);
        }
        return getDynamicBySQL(input,sql.toString(), DocManageVo.class,"dm",false,params);
    }

    /**
     * 根据id获取应收数据
     * @param id
     * @return
     */
    public DocManageVo getDocManageDetailById(String id){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(this.getQueryDocManageCommonSql());
        sql.append(" and dm.id=:id ");
        params.put("id", id);

        return getBySQLAliasToBean(sql.toString(), params, DocManageVo.class);
    }

    /**
     * 根据名称获取业务类型id
     * @param corpId
     * @param cdSign
     * @param busTypeName
     * @return
     */
    public String getByBusTypeByName(String corpId, String cdSign, String busTypeName, String tenantId) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append("select distinct nc.bus_id as busId ");
        sql.append(" from NIS_CORP_BUS nc,NIS_BUS_TYPE nt where nc.bus_id = nt.id and nt.valid_sign = :validSign ");
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        if (!EmptyUtils.isBlank(corpId)) {
            sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "nc.corp_id", true));
        }
        if (!EmptyUtils.isBlank(cdSign)) {
            sql.append(" and nt.cd_sign = :cdSign");
            params.put("cdSign", cdSign);
        }
        if (!EmptyUtils.isBlank(busTypeName)) {
            sql.append(" and nt.bus_type_name = :busTypeName");
            params.put("busTypeName", busTypeName);
        }
        sql.append(" and nt.TENANT_ID =:tenantId ");
        params.put("tenantId", tenantId);
        Object result = createSQLQuery(sql.toString(), params).uniqueResult();
        String busType ="";
        if (!EmptyUtils.isEmpty(result)) {
            busType =  (String)result;
        }
        return busType;
    }

    public String queryDictTypeNameByGrepAndTypeCode(String grepKey,String typeCode){
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append(" select TYPE_NAME as typeName from sys_dict sd  where 1=1");
        sql.append(" and sd.TYPE_GROUP_ID = ( select id from sys_dict_group sdg");
        sql.append(" where TYPE_GROUP_CODE = :grepKey)");
        sql.append("and sd.TYPE_CODE = :typeCode");
        params.put("grepKey", grepKey);
        params.put("typeCode", typeCode);
        Object result = createSQLQuery(sql.toString(), params).uniqueResult();
        String typeName ="";
        if (!EmptyUtils.isEmpty(result)) {
            typeName =  (String)result;
        }
        return typeName;
    }

}
