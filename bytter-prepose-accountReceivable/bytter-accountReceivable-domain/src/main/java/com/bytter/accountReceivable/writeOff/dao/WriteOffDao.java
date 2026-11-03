package com.bytter.accountReceivable.writeOff.dao;

import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.docManage.vo.DocManageWriteOffVo;
import com.bytter.accountReceivable.query.vo.ForceWriteOffVo;
import com.bytter.accountReceivable.writeOff.vo.AbBillPayAndReceVO;
import com.bytter.accountReceivable.writeOff.vo.BisAccDtlVO;
import com.bytter.accountReceivable.writeOff.vo.DocWriteOffRelationVo;
import com.bytter.accountReceivable.writeOff.vo.LcBillArrivalVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class WriteOffDao extends BaseDao<DocManage, String> implements Serializable {

    /**
     * 获取查询应收单公共sql
     *
     * @return
     */
    private String getDocDataCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("     dm.id as id, ");
        sql.append("     dm.code as code, ");
        sql.append("     dm.CORP_ID as corpId, ");
        sql.append("     sc.name as corpName, ");
        sql.append("     dm.BATCH_BUS_TYPE as batchBusType, ");
        sql.append("     nbt.BUS_TYPE_NAME as batchBusTypeName, ");
        sql.append("     dm.SOURCE as source,sd.TYPE_NAME as sourceName, ");
        sql.append("     dm.WRITE_OFF_STATUS as writeOffStatus, ");
        sql.append("     dm.STATUS as status, ");
        sql.append("     case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end as writeOffBal, ");
        sql.append("     dm.WRITE_OFF_TYPE as writeOffType, ");
        sql.append("     dm.VOUCHER_TYPE as voucherType, ");
        sql.append("     dm.RECEIPT_DATE as receiptDate, ");
        sql.append("     dm.END_DATE as endDate, ");
        sql.append("     dm.AGING_DATE as agingDate, ");
        sql.append("     dm.RECEIVABLE_MONEY as receivableMoney, ");
        sql.append("     dm.CUR_ID as curId, ");
        sql.append("     bc.ENGLISH_CODE as enCode, ");
        sql.append("     dm.EXTERNAL_NAME as externalName, ");
        sql.append("     dm.EXTERNAL_TYPE as externalType, ");
        sql.append("     dm.CONTRACT_CODE as contractCode, ");
        sql.append("     dm.PAYEE_ACC as payeeAcc, ");
        sql.append("     dm.PAYEE_ACC_NAME as payeeAccName, ");
        sql.append("     dm.PAYEE_BANK_CODE as payeeBankCode, ");
        sql.append("     dm.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append("     dm.PAYER_ACC_NO as payerAccNo, ");
        sql.append("     dm.PAYER_ACC_NAME as payerAccName, ");
        sql.append("     dm.PAYER_BANK_NO as payerBankNo, ");
        sql.append("     dm.PAYER_BANK_NAME as payerBankName ");
        sql.append(" from doc_manage dm ");
        sql.append(" left join nis_bus_type nbt on ");
        sql.append(" dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" left join bt_currency bc on ");
        sql.append(" dm.CUR_ID = bc.ID ");
        sql.append(" left join sys_corp sc on sc.id = dm.corp_id ");
        sql.append("      left join (select d.TYPE_CODE,d.TYPE_NAME from SYS_DICT d inner join SYS_DICT_GROUP g on d.TYPE_GROUP_ID = g.id and g.TYPE_GROUP_CODE='writeOff.docSource' ) sd  on sd.TYPE_CODE = dm.SOURCE ");
        sql.append(" where 1 = 1 ");
        return sql.toString();
    }

    private String getWriteOffDocDataCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select DISTINCT ");
        sql.append("     dm.id as id, ");
        sql.append("     dm.code as code, ");
        sql.append("     dm.CORP_ID as corpId, ");
        sql.append("     dm.BATCH_BUS_TYPE as batchBusType, ");
        sql.append("     nbt.BUS_TYPE_NAME as batchBusTypeName, ");
        sql.append("     dm.SOURCE as source, ");
        sql.append("     dm.WRITE_OFF_STATUS as writeOffStatus, ");
        sql.append("     dwor.STATUS as status, ");
        sql.append("     case when dm.WRITE_OFF_bal is not null then dm.WRITE_OFF_bal else dm.RECEIVABLE_MONEY end as writeOffBal, ");
        sql.append("     dm.WRITE_OFF_TYPE as writeOffType, ");
        sql.append("     dm.VOUCHER_TYPE as voucherType, ");
        sql.append("     dm.RECEIPT_DATE as receiptDate, ");
        sql.append("     dm.END_DATE as endDate, ");
        sql.append("     dm.AGING_DATE as agingDate, ");
        sql.append("     dm.RECEIVABLE_MONEY as receivableMoney, ");
        sql.append("     dm.CUR_ID as curId, ");
        sql.append("     dm.EXTERNAL_NAME as externalName, ");
        sql.append("     dm.EXTERNAL_TYPE as externalType, ");
        sql.append("     dm.CONTRACT_CODE as contractCode, ");
        sql.append("     dm.PAYEE_ACC as payeeAcc, ");
        sql.append("     dm.PAYEE_ACC_NAME as payeeAccName, ");
        sql.append("     dm.PAYEE_BANK_CODE as payeeBankCode, ");
        sql.append("     dm.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append("     dm.PAYER_ACC_NO as payerAccNo, ");
        sql.append("     dm.PAYER_ACC_NAME as payerAccName, ");
        sql.append("     dm.PAYER_BANK_NO as payerBankNo, ");
        sql.append("     dm.PAYER_BANK_NAME as payerBankName ");
        sql.append(" from doc_manage dm ");
//        sql.append(" left join DOC_WIRTE_OFF_RELATION dwor on dm.id = dwor.doc_id  and dm.WRITE_OFF_TYPE  =dwor.WRITE_OFF_TYPE  ");
        sql.append(" left join DOC_WIRTE_OFF_RELATION dwor on dm.id = dwor.doc_id ");
        sql.append(" left join nis_bus_type nbt on ");
        sql.append(" dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" where 1 = 1 ");
        return sql.toString();
    }

    private String getWriteOffDocDataCommonSql2() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append(" dm.id as id, ");
        sql.append(" dm.code as code, ");
        sql.append(" dm.CORP_ID as corpId, ");
        sql.append(" dm.BATCH_BUS_TYPE as batchBusType, ");
        sql.append(" dm.BUS_TYPE_NAME as batchBusTypeName, ");
        sql.append(" dm.SOURCE as source, ");
        sql.append(" dm.WRITE_OFF_STATUS as writeOffStatus, ");
        sql.append(" dm.STATUS as status, ");
        sql.append(" dm.write_off_bal as writeOffBal, ");
        sql.append(" dm.WRITE_OFF_TYPE as writeOffType, ");
        sql.append(" dm.VOUCHER_TYPE as voucherType, ");
        sql.append(" dm.RECEIPT_DATE as receiptDate, ");
        sql.append(" dm.END_DATE as endDate, ");
        sql.append(" dm.AGING_DATE as agingDate, ");
        sql.append(" dm.RECEIVABLE_MONEY as receivableMoney, ");
        sql.append(" dm.CUR_ID as curId, ");
        sql.append(" dm.EXTERNAL_NAME as externalName, ");
        sql.append(" dm.EXTERNAL_TYPE as externalType, ");
        sql.append(" dm.CONTRACT_CODE as contractCode, ");
        sql.append(" dm.PAYEE_ACC as payeeAcc, ");
        sql.append(" dm.PAYEE_ACC_NAME as payeeAccName, ");
        sql.append(" dm.PAYEE_BANK_CODE as payeeBankCode, ");
        sql.append(" dm.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append(" dm.PAYER_ACC_NO as payerAccNo, ");
        sql.append(" dm.PAYER_ACC_NAME as payerAccName, ");
        sql.append(" dm.PAYER_BANK_NO as payerBankNo, ");
        sql.append(" dm.PAYER_BANK_NAME as payerBankName, ");
        sql.append(" dm.name as corpName,");
        sql.append(" dm.cur_name as curName");
        sql.append(" from ( ");
//        sql.append(" select temp1.* from ( ");
        sql.append(" select DISTINCT ");
        sql.append("     dm1.id, ");
        sql.append("     dm1.code, ");
        sql.append("     dm1.CORP_ID, ");
        sql.append("     dm1.BATCH_BUS_TYPE, ");
        sql.append("     nbt.BUS_TYPE_NAME, ");
        sql.append("     dm1.SOURCE, ");
        sql.append("     dm1.WRITE_OFF_STATUS, ");
        sql.append("     dwor.STATUS, ");
        sql.append("     case when dm1.WRITE_OFF_bal is not null then dm1.WRITE_OFF_bal else dm1.RECEIVABLE_MONEY end as write_off_bal, ");
        sql.append("     dm1.WRITE_OFF_TYPE, ");
        sql.append("     dm1.VOUCHER_TYPE, ");
        sql.append("     dm1.RECEIPT_DATE, ");
        sql.append("     dm1.END_DATE, ");
        sql.append("     dm1.AGING_DATE, ");
        sql.append("     dm1.RECEIVABLE_MONEY, ");
        sql.append("     dm1.CUR_ID, ");
        sql.append("     dm1.EXTERNAL_NAME, ");
        sql.append("     dm1.EXTERNAL_TYPE, ");
        sql.append("     dm1.CONTRACT_CODE, ");
        sql.append("     dm1.PAYEE_ACC, ");
        sql.append("     dm1.PAYEE_ACC_NAME, ");
        sql.append("     dm1.PAYEE_BANK_CODE, ");
        sql.append("     dm1.PAYEE_BANK_NAME, ");
        sql.append("     dm1.PAYER_ACC_NO, ");
        sql.append("     dm1.PAYER_ACC_NAME, ");
        sql.append("     dm1.PAYER_BANK_NO, ");
        sql.append("     dm1.PAYER_BANK_NAME, ");
        sql.append(" sc.name,");
        sql.append(" bc.cur_name ");
        sql.append(" from doc_manage dm1 ");
//        sql.append(" left join DOC_WIRTE_OFF_RELATION dwor on dm.id = dwor.doc_id  and dm.WRITE_OFF_TYPE  =dwor.WRITE_OFF_TYPE  ");
        sql.append(" left join DOC_WIRTE_OFF_RELATION dwor on dm1.id = dwor.doc_id ");
        sql.append(" left join nis_bus_type nbt on ");
        sql.append(" dm1.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" left join sys_corp sc on sc.id = dm1.corp_id ");
        sql.append(" left join bt_currency bc on bc.id = dm1.cur_id ");
        sql.append(" where 1 = 1 ");
        return sql.toString();
    }


    /**
     * 查询核销处理 应收单列表数据
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDocDataTablesOfWriteOff(DataTablesInput dataTablesInput, String joinSql) {
//        String queryType = (String) dataTablesInput.getFormdata().get("queryType");
        StringBuilder sql = new StringBuilder();
        sql.append(this.getWriteOffDocDataCommonSql2());
        sql.append(" and dm1.STATUS in (95) ");

        String ins_writeOffStatus = (String) dataTablesInput.getFormdata().get("ins_writeOffStatus");
        if (StringUtils.isNotBlank(ins_writeOffStatus)) {
            String[] writeOffStatus = ins_writeOffStatus.split(",");
            sql.append(" and " + SqlUtil.getInSql(writeOffStatus, "dm1.WRITE_OFF_STATUS", true));
        }
        String status = (String) dataTablesInput.getFormdata().get("eqi_status");
//        String eqsWriteOffStatus = (String) dataTablesInput.getFormdata().get("eqs_writeOffStatus");
//        if(EmptyUtils.isEmpty(eqsWriteOffStatus)){
//            dataTablesInput.getFormdata().put("ins_writeOffStatus", "0,1");
//        }
        if (StringUtils.isNotEmpty(status)) {
            if ("95".equals(status)) {
                sql.append(" and dwor.STATUS in(95,-3) ");
            } else {
                sql.append(" and dwor.STATUS = " + status);
            }
            dataTablesInput.getFormdata().remove("eqi_status");
        }
//        if(StringUtils.isEmpty(queryType) || !"approve".equals(queryType)){
//            sql.append(" and dm.id not in ( select doc_id from DOC_WIRTE_OFF_RELATION where status <> 95 )");
//        }
        String eqd_receiptDate = (String) dataTablesInput.getFormdata().get("eqd_receiptDate");
        if (StringUtils.isNotEmpty(eqd_receiptDate)) {
            dataTablesInput.getFormdata().put("ged_receiptDate", eqd_receiptDate + " 00:00:00");
            dataTablesInput.getFormdata().put("led_receiptDate", eqd_receiptDate + " 23:59:59");
            dataTablesInput.getFormdata().remove("eqd_receiptDate");
        }
        String eqd_endDate = (String) dataTablesInput.getFormdata().get("eqd_endDate");
        if (StringUtils.isNotEmpty(eqd_endDate)) {
            dataTablesInput.getFormdata().put("ged_endDate", eqd_endDate + " 00:00:00");
            dataTablesInput.getFormdata().put("led_endDate", eqd_endDate + " 23:59:59");
            dataTablesInput.getFormdata().remove("eqd_endDate");
        }
        String eqd_agingDate = (String) dataTablesInput.getFormdata().get("eqd_agingDate");
        if (StringUtils.isNotEmpty(eqd_agingDate)) {
            dataTablesInput.getFormdata().put("ged_agingDate", eqd_agingDate + " 00:00:00");
            dataTablesInput.getFormdata().put("led_agingDate", eqd_agingDate + " 23:59:59");
            dataTablesInput.getFormdata().remove("eqd_agingDate");
        }
        //多租户过滤
//        sql.append(addTenantConditionForSQL("dm1"));
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String executeSql = sql.toString();
//        String customJoinSql = getWhereAllCondition(dataTablesInput, new DocManageWriteOffVo(), "dm", false);
//        customJoinSql += " order by dwor.CREATE_DATE,dm.code desc) temp group by temp.code ";
//        sql.append(customJoinSql);
//        String executeSql = " select * from ( " + ;
//        dataTablesInput.setFormdata(new HashMap<>());
        executeSql += " GROUP BY dm1.code, dm1.id, dm1.CORP_ID, dm1.BATCH_BUS_TYPE, nbt.BUS_TYPE_NAME, dm1.SOURCE, " +
                "   dm1.WRITE_OFF_STATUS, dwor.STATUS, dm1.write_off_bal, dm1.WRITE_OFF_TYPE, dm1.VOUCHER_TYPE, " +
                "   dm1.RECEIPT_DATE, dm1.END_DATE, dm1.AGING_DATE, dm1.RECEIVABLE_MONEY, dm1.CUR_ID, " +
                "   dm1.EXTERNAL_NAME, dm1.EXTERNAL_TYPE, dm1.CONTRACT_CODE, dm1.PAYEE_ACC, dm1.PAYEE_ACC_NAME, " +
                "   dm1.PAYEE_BANK_CODE, dm1.PAYEE_BANK_NAME, dm1.PAYER_ACC_NO, dm1.PAYER_ACC_NAME, dm1.PAYER_BANK_NO, " +
                "   dm1.PAYER_BANK_NAME, sc.name, bc.cur_name ) dm where 1=1 ";
        return getDynamicBySQL(dataTablesInput, executeSql, DocManageWriteOffVo.class, "dm", false);
    }

    /**
     * 查询核销处理 应收单列表数据
     *
     * @return
     */
    public List<DocManageWriteOffVo> getDocsOfWriteOffByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(this.getDocDataCommonSql());
        sql.append(" and dm.id in(:ids) ");
        Map<String, Object> param = new HashMap<>();
        param.put("ids", ids);
        return listBySQLAliasToBean(sql.toString(), param, DocManageWriteOffVo.class);
    }

    public List<DocManageWriteOffVo> getDocRelation(List<String> ids, String batchNo) {
        String sql = "select m.ID, CORP_ID, CODE, BATCH_BUS_TYPE as batchBusType,nbt.BUS_TYPE_NAME as batchBusTypeName, SOURCE, VOUCHER_TYPE, RECEIPT_DATE, END_DATE, AGING_DATE, PAYEE_ACC, PAYEE_ACC_NAME, PAYEE_BANK_CODE, PAYEE_BANK_NAME, RECEIVABLE_MONEY, CUR_ID, EXRATE_TYPE, CONVERT_PRE_MONEY, PAYER_ACC_NO, PAYER_ACC_NAME, PAYER_BANK_NAME, PAYER_BANK_NO, EXTERNAL_NAME, EXTERNAL_TYPE, CONTRACT_CODE, CONTRACT_NAME, CONTRACT_Money, CONTRACT_DATE, WRITE_OFF_STATUS, " +
                "w.WRITE_OFF_TYPE as writeOffType,exchangeRate,BEFORE_WRITE_OFF_BAL as writeOffBal,w.EXECUTE_MONEY as executeMoney ,(w.BEFORE_WRITE_OFF_BAL-w.EXECUTE_MONEY) as newWriteOffBal  \n" +
                "from doc_manage m inner join (select doc_id,batch_no,sum(EXECUTE_MONEY) as EXECUTE_MONEY,max(exchange_rate) as exchangeRate,max(BEFORE_WRITE_OFF_BAL) as BEFORE_WRITE_OFF_BAL,max(WRITE_OFF_TYPE) as WRITE_OFF_TYPE from DOC_WIRTE_OFF_RELATION where BATCH_NO =:batchNo group by doc_id,batch_no) w on  w.doc_id = m.ID \n" +
                " left join nis_bus_type nbt on  m.BATCH_BUS_TYPE = nbt.ID " +
                "where m.id in (:ids) and BATCH_NO =:batchNo";
        Map<String, Object> param = new HashMap<>();
        param.put("ids", ids);
        param.put("batchNo", batchNo);
        return listBySQLAliasToBean(sql.toString(), param, DocManageWriteOffVo.class);
    }

    /**
     * 根据id获取应收单数据
     *
     * @param ids
     * @return
     */
    public List<DocManageVo> getDocManageVoByIds(List<String> ids, List<String> writeOffStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(this.getDocDataCommonSql());
        sql.append(" and dm.STATUS = 95 ");
        if (EmptyUtils.isNotEmpty(writeOffStatus)) {
            sql.append(" and ").append(getSQLIn(writeOffStatus, 900, "dm.write_off_status"));
        }
        sql.append(" and ").append(getSQLIn(ids, 900, "dm.id"));
        return listBySQLAliasToBean(sql.toString(), params, DocManageVo.class);
    }

    /**
     * 应收单据核销关系表 删除数据
     *
     * @param ids
     */
    public void delDocWriteOffRelationByDocIds(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from DOC_WIRTE_OFF_RELATION where 1=1 ");
        sql.append(" and ").append(getSQLIn(ids, 900, "doc_id"));
        createSQLQuery(String.valueOf(sql)).executeUpdate();
    }

    /**
     * 获取流水查询公共sql
     *
     * @return
     */
    private String getDtlDataCommonSql(String... batchNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("     t.id as id, ");
        sql.append("     ba.corp_id as corpId, ");
        sql.append("     t.BANK_SERIAL_NO as bankSerialNo, ");
        sql.append("     t.TRANS_TIME as transTime, ");
        sql.append("     t.CUR_ID as curId, ");
        sql.append("     bc.english_code as enCode, ");
        sql.append("     t.AMT as amt, ");
        sql.append("     t.ACC_NAME as accName, ");
        sql.append("     t.BANK_NAME as bankName, ");
        sql.append("     t.BANK_ACC as bankAcc, ");
        sql.append("     t.OPP_ACC_NAME as oppAccName, ");
        sql.append("     t.OPP_ACC_NO as oppAccNo, ");
        sql.append("     t.opp_acc_bank as oppAccBank, ");
        sql.append("     t.bif_code as bifCode, ");
        sql.append("     t.RETURN_TIME as returnTime, ");
        sql.append("     t.USES as uses, ");
        sql.append("     t.POSTSCRIPT as postscript, ");
        sql.append("     BT_NVL((select relation_id from DOC_WIRTE_OFF_RELATION where relation_id=t.id and status = 11 group by relation_id ),1) as flag, ");
        sql.append("     t.WRITE_OFF_STATUS as writeOffStatus, ");
        sql.append("     case when t.WRITE_OFF_bal is not null then t.WRITE_OFF_bal else t.AMT end as writeOffBal, ");
        sql.append("     t.WRITE_OFF_TYPE as writeOffType, ");
        sql.append("  bc.cur_name as curName ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(", w.EXECUTE_MONEY as executeMoney ");
        }
        sql.append(" from bis_acc_dtl t ");
        sql.append(" left join bt_bank_acc ba on t.bank_acc = ba.bank_acc ");
        sql.append(" left join bt_currency bc on t.CUR_ID = bc.id ");
        sql.append(" left join bt_bank_type bbt on ba.bank_type_id = bbt.id ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(" left  join (select RELATION_ID,batch_no,sum(EXECUTE_MONEY) as EXECUTE_MONEY from DOC_WIRTE_OFF_RELATION where BATCH_NO ='" + batchNo[0] + "' group by RELATION_ID,batch_no) w on  w.RELATION_ID = t.ID  ");
        }
        sql.append(" where 1 = 1 ");
        return sql.toString();
    }

    /**
     * 查询核销处理 流水列表数据
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDtlDataTablesOfWriteOff(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
//        String eqsWriteOffStatus = (String) dataTablesInput.getFormdata().get("eqs_writeOffStatus");
//        if(EmptyUtils.isEmpty(eqsWriteOffStatus)){
//            dataTablesInput.getFormdata().put("ins_writeOffStatus", "0,1");
//        }
        dataTablesInput.getFormdata().remove("eqs_id");
        String queryType = (String) dataTablesInput.getFormdata().get("queryType");
        dataTablesInput.getFormdata().remove("queryType");

        sql.append(this.getDtlDataCommonSql());
        sql.append(" and t.cd_sign = '0' ");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '') ");
//        if(StringUtils.isEmpty(queryType) || !"approve".equals(queryType)){
//            sql.append(" and t.id not in ( select relation_id from DOC_WIRTE_OFF_RELATION where status not in (95,11))");
//        }
        //多租户过滤
        //sql.append(addTenantConditionForSQL("dm"));
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        if (EmptyUtils.isNotEmpty(eqs_id)) {
            if ("#".equals(eqs_id)) {
//                sql.append(" and t.id = '"+eqs_id+"' ");
            } else {
                sql.append(" and  " + SqlUtil.getInSql(eqs_id.split(","), "t.id", true));
            }
        } else {
            //查询未核销和部分核销流水记录
            //sql.append(" and t.write_off_status <> '2' ");
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
        //考虑流水账号为空的情况
        String bankAcc = (String) dataTablesInput.getFormdata().get("eqs_bankAcc");
        if ("null".equals(bankAcc)) {
            sql.append(" and (t.bank_acc is null or t.bank_acc='')");
            dataTablesInput.getFormdata().put("eqs_bankAcc", "");
        }
        String oppAccNo = (String) dataTablesInput.getFormdata().get("eqs_oppAccNo");
        if ("null".equals(oppAccNo)) {
            sql.append(" and (t.opp_acc_no is null or t.opp_acc_no='')");
            dataTablesInput.getFormdata().put("eqs_oppAccNo", "");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlVO.class, "t", false);
    }

    /**
     * 根据id获取ls数据
     *
     * @param ids
     * @return
     */
    public List<BisAccDtlVO> getDtlsOfWriteOffByIds(List<String> ids, String... batchNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(this.getDtlDataCommonSql(batchNo));
        sql.append(" and ").append(getSQLIn(ids, 900, "t.id"));
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlVO.class);
    }

    /**
     * 根据id获取ls数据
     *
     * @param ids
     * @return
     */
    public List<BisAccDtlVO> getDtlDataByIds(List<String> ids, List<String> writeOffStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(this.getDtlDataCommonSql());
        sql.append(" and t.cd_sign = '0' ");
        sql.append(" and (t.status = 95 or t.status is null or t.status = '') ");
        sql.append(" and ").append(getSQLIn(ids, 900, "t.id"));
        if (EmptyUtils.isNotEmpty(writeOffStatus)) {
            sql.append(" and ").append(getSQLIn(writeOffStatus, 900, "t.write_off_status"));
        }
        return listBySQLAliasToBean(sql.toString(), null, BisAccDtlVO.class);
    }

    /**
     * 加载应收票据台账数据
     *
     * @param dataTablesInput 查询入参
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput billReceDataTable(DataTablesInput dataTablesInput, List<String> corpAuthList, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
//        String eqsWriteOffStatus = (String) dataTablesInput.getFormdata().get("eqs_writeOffStatus");
//        if(EmptyUtils.isEmpty(eqsWriteOffStatus)){
//            dataTablesInput.getFormdata().put("ins_writeOffStatus", "0,1");
//        }
        if (dataTablesInput.isMoneySum()) {
            sql.append("select BT_NVL_NUM(sum(bill.billMoney),0) as billMoney from (");
        } else {
            sql.append(" select");
            sql.append(" bill.*,");
            sql.append(" BT_NVL((select relation_id from DOC_WIRTE_OFF_RELATION where relation_id = bill.id and status = 11  group by relation_id ), 1) as flag");
            sql.append(" from (");
        }
        Date businessDate = null;
        String ged_receDate = (String) dataTablesInput.getFormdata().get("ged_receDate");
        if (StringUtils.isNotEmpty(ged_receDate) && ged_receDate.length() == 10) {
            businessDate = DateUtils.parse(ged_receDate, DateUtils.PATTERN_CLASSICAL_SIMPLE);
            params.put("vvBusinessDate", businessDate);
        }
        sql.append(this.getReceSql(businessDate));
        //租户号
        params.put("tenantId", user.getTenantId());
        //用户满足对应查询权限的单位数据
        String corpAuthSql = SqlUtil.getInSql(corpAuthList, "v.apply_corp_id", true);
        sql.append("and ").append(corpAuthSql).append(" ");

        String transferWay = (String) dataTablesInput.getFormdata().get("ins_transferWay");
        //添加持有方式条件
        if (StringUtils.isNotEmpty(transferWay)) {
            List<String> way = Arrays.asList(transferWay.split(","));
            sql.append(" and (bs.BUS_TYPE in (:transferWay) ");
            params.put("transferWay", way);
            if (way.contains("0")) {
                sql.append(" or bs.BUS_TYPE is null ");
            }
            // transferWay前带-为业务中数据
            List<String> wayList = new ArrayList<String>(way);
            if (!EmptyUtils.isEmpty(wayList)) {
                for (int i = 0; i < wayList.size(); i++) {
                    if (wayList.get(i).contains("-")) {
                        wayList.set(i, wayList.get(i).replace("-", ""));
                    }
                }
                params.put("transferWay", wayList);
            }
            //dataTablesInput.getFormdata().remove("ins_transferWay");
            sql.append(") ");
        }
        // 是否对内条件查询
        if (StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_insideSing"))) {
            String insideSing = dataTablesInput.getFormdata().get("eqs_insideSing").toString();
            if (AccebillConstant.AB_INSIDE_SING_Y.equals(insideSing)) {
                sql.append(" and ((v.inside_sing=:insideSing and a.brief_id in (:briefId1) and ba.id is null) or ba.inside_sing=:insideSing or bd.inside_sing=:insideSing) ");
                params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003,
                        AccebillConstant.BRIEF_STATUS_9010,
                        AccebillConstant.BRIEF_STATUS_9012,
                        AccebillConstant.BRIEF_STATUS_9019,
                        AccebillConstant.BRIEF_STATUS_6019,
                        AccebillConstant.BRIEF_STATUS_6001,
                        AccebillConstant.BRIEF_STATUS_6212});
            } else if (AccebillConstant.AB_INSIDE_SING_N.equals(insideSing)) {
                sql.append(" and ((v.inside_sing=:insideSing and ba.id is null)  or ba.inside_sing=:insideSing or (ba.id is not null and ba.inside_sing is null) or bd.inside_sing=:insideSing) ");
            }
            params.put("insideSing", insideSing);
        }
        if (dataTablesInput.getFormdata().containsKey("accountGrantList")) {
            //添加账户授权控制
            List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
            sql.append(" and (v.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "v.bank_acc", "bankAccs", params)).append(")");
        }
        String queryType = (String) dataTablesInput.getFormdata().get("queryType");
        dataTablesInput.getFormdata().remove("queryType");
//        if(StringUtils.isEmpty(queryType) || !"approve".equals(queryType)){
//            sql.append(" and v.id not in ( select relation_id from DOC_WIRTE_OFF_RELATION where status <> 95 )");
//        }
        String eqs_id = (String) dataTablesInput.getFormdata().get("eqs_id");
        if (StringUtils.isNotEmpty(eqs_id) && !"#".equals(eqs_id)) {
            sql.append(" and ").append(SqlUtil.getInSql(eqs_id.split(","), "v.id", true));
        }
        sql.append(" order by v.create_date) bill where 1=1 ");
        //增加可核销余额不能为空
        sql.append(" and bill.writeOffBal is not null ");
        //添加票据业务状态条件
        addBillBusinessStatusConditions(dataTablesInput);
        return getDynamicBySQLForBill(dataTablesInput, sql.toString(), AbBillPayAndReceVO.class, "bill", false, params);
    }

    /**
     * @param ids
     * @param writeOffStatus
     * @return
     */
    public List<AbBillPayAndReceVO> getBillDataByIds(List<String> ids, List<String> writeOffStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(getReceSql(null));
        sql.append(" and ").append(getSQLIn(ids, 900, "v.id"));
        if (EmptyUtils.isNotEmpty(writeOffStatus)) {
            sql.append(" and ").append(getSQLIn(writeOffStatus, 900, "v.write_off_status"));
        }
        Map<String, Object> params = new HashMap<>();
        //租户号
        params.put("tenantId", this.getCurrentUserTenantId());
        return listBySQLAliasToBean(sql.toString(), params, AbBillPayAndReceVO.class);
    }

    /**
     * @param ids
     * @return
     */
    public List<AbBillPayAndReceVO> getBillRecesByIds(List<String> ids, String... batchNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(getReceSql(null, batchNo));
        sql.append(" and ").append(getSQLIn(ids, 900, "v.id"));
        Map<String, Object> params = new HashMap<>();
        //租户号
        params.put("tenantId", this.getCurrentUserTenantId());
        return listBySQLAliasToBean(sql.toString(), params, AbBillPayAndReceVO.class);
    }

    /**
     * 应收票据公共sql
     *
     * @return
     */
    public StringBuilder getReceSql(Date businessDate, String... batchNo) {
        StringBuilder sql = new StringBuilder("select v.id, ba.PD_ID pDID, i.BILL_FLAG as billFlag,i.BILL_TYPE as billType,i.ORIG_NO as origNo,");
        sql.append(" i.PAYER as payer,i.payer_bank_name as payerBankName,i.ACCEPTOR as acceptor,i.START_DATE as startDate,i.END_DATE as endDate,v.bank_Name as bankName,");
        sql.append(" BT_NVL_CHAR(v.cn_accrecords,BT_NVL_CHAR(a.cn_accrecords,a.rec_nm)) as cnAccrecords,v.corp_Id as corpId,");
        sql.append("  case when bn.business_date is not null then bn.business_date when bd.business_date is null then ba.business_date else bd.business_date end as transferDate,");
        sql.append("  (case when bd.rec_nm = '' or bd.rec_nm is null then ba.rec_nm else bd.rec_nm end) as rearAccrecords,BT_CONCAT4('(',sc.code,')',sc.name)");
        sql.append("  as corpCodeAndName,i.BILL_MONEY as billMoney,a.business_date as receDate,");
        sql.append("  BT_NVL_CHAR(bn.brief_id,BT_NVL_CHAR(bd.BRIEF_ID,BT_NVL_CHAR(ba.BRIEF_ID,a.BRIEF_ID))) as briefId,BT_NVL_CHAR(bn.send_sign,BT_NVL_CHAR(bd.send_sign,BT_NVL_CHAR(ba.send_sign, a.send_sign))) as sendSign,");
        sql.append(" BT_NVL_NUM(ad.INTEREST_RATES,ad2.INTEREST_RATES) as interestRates,BT_NVL_NUM(ad.INTEREST,ad2.INTEREST) as interest,");
        sql.append(" case when ad3.REAL_MONEY is not null THEN ad3.REAL_MONEY when ad2.REAL_MONEY is not null then ad2.REAL_MONEY ELSE ad.REAL_MONEY end as realMoney,");
        sql.append(" CASE WHEN bn.status = -2 or bn.opt_status IN('-3','-4') THEN '0' WHEN  bn.brief_id in('6011') or bn.status !=95 or bn.opt_status=' ' or bn.opt_status!=19 THEN bt_nvl_char(CONCAT('-',bs.BUS_TYPE) ,'0') ELSE  bt_nvl_char(bs.BUS_TYPE,'0')  END as transferWay,banktype.id as banktypeid,  v.bank_acc as bankAcc, "); //i.ENDORSEMENT_COUNT as endorsementCount,
        sql.append(" v.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,v.BUS_CORP_ID as busCorpId,v.BUS_PERSONNEL as busPersonnel ");
        sql.append(" , i.new_stm_issuofppr_ind as newStmIssuofpprInd, i.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd,  ");
        sql.append(" i.sbbll_rng_strt_sn as sbbllRngStrtSn, i.sbbll_rng_end_sn as sbbllRngEndSn, i.bl_std_amt as blStdAmt, i.bl_rmtr_unn_id_no as blRmtrUnnIdNo, ");
        sql.append(" CASE WHEN bn.status is not null THEN bn.status ELSE a.status end AS status,CASE WHEN bn.opt_status is not null THEN bn.opt_status ELSE a.opt_status end AS optStatus,");
        sql.append(" CASE WHEN bn.RETURN_MSG  is not null THEN  bn.RETURN_MSG ELSE a.RETURN_MSG end AS returnMsg,case WHEN bn.brief_id is not null and bn.brief_id IN ('6012','6020') THEN '1' WHEN bn.brief_id is not null and bn.sttlmmk ='SM00' and bn.send_sign='1' THEN '1' ELSE '0' END AS isSettle, ");
        sql.append(" CASE WHEN bn.sttlmmk  is not null THEN bn.sttlmmk ElSE null end as sttlmmk ,CASE WHEN bn.id  is not null THEN '1' ElSE '0' end as transSign ");
        sql.append(" ,v.enter_stock as enterStock, case when v.write_off_bal is not null then v.write_off_bal ELSE i.BILL_MONEY end as writeOffBal, v.write_off_type as writeOffType, v.write_off_status as writeOffStatus ");
        sql.append(" ,(select id from bt_currency bc where bc.ENGLISH_CODE = 'RMB') curId ");
//        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id and (i.OLD_DRAFT_ID is null or i.OLD_DRAFT_ID not like '%-del')  inner join ab_accrecords a on v.id=a.vvid ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(", w.EXECUTE_MONEY as executeMoney ");
        }
        sql.append(" from ab_voucherinfo_vice v inner join ab_voucherinfo i on v.vid=i.id inner join ab_accrecords a on v.id=a.vvid ");
        sql.append(" left join ab_accrecords ba on a.id=ba.lastid   and ba.brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020') and ba.status<>-2 AND ba.opt_status NOT IN ('-3','-4') ");
        sql.append(" left join ab_accrecords bd on ba.id=bd.lastid and bd.status=95 and bd.opt_status='19' and bd.brief_id = '011' and bd.status<>-2  AND bd.opt_status NOT IN ('-3','-4') and ba.id is not null ");
        sql.append(" left join ab_accrecords bc on bd.id=bc.lastid and bd.status=95 and bd.opt_status='19' and bc.brief_id = '6012' and bc.status<>-2 and bd.id is not null ");
        sql.append(" left join (SELECT vvid,max(create_date) AS maxDate FROM ab_accrecords WHERE brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020') ");
        if (businessDate != null) {
            sql.append(" and business_date>=:vvBusinessDate ");
        }
        sql.append(" GROUP BY vvid ) m ON m.vvid = v.id ");
//        sql.append(" left join (SELECT vvid,max(create_date) AS maxDate FROM ab_accrecords WHERE brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020')  GROUP BY vvid ) m ON m.vvid = v.id ");
//        sql.append(" left JOIN ab_accrecords bn ON bn.vvid = m.vvid AND bn.create_date = m.maxDate ");
        sql.append(" left JOIN ab_accrecords bn ON bn.vvid = m.vvid and bn.brief_id in ('6004','010','011','6011','6012','8018','018','020','021','6020') AND bn.create_date = m.maxDate ");
        sql.append(" left join ab_brief_status bs on ((bd.brief_id is not null and bs.status =bd.brief_id) or (bd.brief_id is null and bs.status=ba.brief_id)) ");
        sql.append(" left join ab_discount ad on bd.id=ad.record_id ");
        sql.append(" left join ab_discount ad2 on ba.id=ad2.record_id ");
        sql.append(" left join ab_discount ad3 on bc.id=ad3.record_id  ");
        sql.append(" left join SYS_CORP sc on v.CORP_ID=sc.ID ");
        sql.append(" left join sys_corp s2 on v.apply_corp_id = s2.id ");
        sql.append(" left join bt_bank_acc acc on acc.bank_acc =v.bank_acc");
        sql.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(" left  join (select RELATION_ID,batch_no,sum(EXECUTE_MONEY) as EXECUTE_MONEY from DOC_WIRTE_OFF_RELATION where BATCH_NO ='" + batchNo[0] + "' group by RELATION_ID,batch_no) w on  w.RELATION_ID = v.ID  ");
        }
        //sql.append(" left join (SELECT COUNT(*) AS ENDORSEMENTCOUNT, ORIG_NO AS ORIGNO FROM AB_BACKBILLINFO BACK WHERE BACK.BUSTYPE = '010'");
        //sql.append(" GROUP BY BACK.ORIG_NO) back1 ON back1.origNo = i.orig_no ");
        sql.append(" where 1=1 and a.opt_status='19' and a.status=95 and v.TYPE='1' and (");
        sql.append(" (a.brief_id in ('6001','6019','6212') and ((ba.brief_id !='6004') or ba.brief_id is null))");
        sql.append(" or (a.brief_id in ('9003','9010','9012','9019'))) ");
        sql.append(" and v.tenant_id = :tenantId ");
        return sql;
    }

    /**
     * @Description 根据票据业务状态添加条件
     * @author zhous
     * @date 2024/1/9 17:51
     */
    private void addBillBusinessStatusConditions(DataTablesInput dataTablesInput) {
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String businessStatus = String.valueOf(formdata.get("businessStatus"));
        if (StringUtils.isNotEmpty(businessStatus)) {
            List<String> businessStatusList = Arrays.asList(businessStatus.split(","));
            for (String status : businessStatusList) {
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_1)) {
                    //待签收 设置业务状态和指令状态
                    setStatus(formdata, "ins_statusName", String.valueOf(BtBankAccConstant.STATUE_CHECK));
                    setStatus(formdata, "ins_optStatusName", AccebillConstant.AB_BILL_OPT_STATUS18);
                }
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_2)) {
                    //已签收 设置业务状态和指令状态
                    setStatus(formdata, "ins_statusName", String.valueOf(BtBankAccConstant.STATUE_CHECK));
                    setStatus(formdata, "ins_optStatusName", AccebillConstant.AB_BILL_OPT_STATUS19);
                    setStatus(formdata, "ins_isSettle", "0");
                }
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_3)) {
                    //拒签 设置业务状态和指令状态
                    setStatus(formdata, "ins_optStatusName", AccebillConstant.AB_BILL_OPT_STATUS_4);
                }
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_4)) {
                    //撤回 设置业务状态和指令状态
                    setStatus(formdata, "ins_optStatusName", AccebillConstant.AB_BILL_OPT_STATUS_3);
                }
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_5)) {
                    //已结清 设置业务状态和指令状态
                    setStatus(formdata, "ins_statusName", String.valueOf(BtBankAccConstant.STATUE_CHECK));
                    setStatus(formdata, "ins_optStatusName", AccebillConstant.AB_BILL_OPT_STATUS19);
                    setStatus(formdata, "ins_isSettle", "1");
                }
                if (status.contains(AccebillConstant.AB_BILL_BUSINESS_STATUS_6)) {
                    //已收票 设置业务状态和指令状态
                    setStatus(formdata, "eqs_transSign", "0");
                }
            }
        }
    }

    /**
     * @Description
     * @author zhous
     * @date 2024/1/9 17:51
     */
    private void setStatus(Map<String, Object> formdata, String condition, String status) {
        String s = !"null".equals(String.valueOf(formdata.get(condition))) ? String.valueOf(formdata.get(condition)) : "";
        if (StringUtils.isEmpty(s)) {
            s = String.valueOf(status);
        } else {
            s += ",";
            s += String.valueOf(status);
        }
        formdata.put(condition, s);
    }

    /**
     * 获取待核销信用证数据公共sql
     *
     * @return
     */
    public String getLcDataCommonSql(String... batchNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select lb.*, lc.CREDIT_NO as creditNo, c.CUR_NAME as curName,c.ENGLISH_CODE  as enCode, s.name as corpName, lc.LC_INTER as lcInter, a.status2, a.type, lc.OPENING_BANK_NAME as openingBankName, ");
        sql.append("  BT_NVL((select relation_id from DOC_WIRTE_OFF_RELATION where relation_id=lb.id and status = 11 group by relation_id ),1) as flag,");
        sql.append(" a.mortgageMoney, a.incomingMoney "); //, a.negotiationMoney
        sql.append(" , case when lb.write_off_bal is not null then lb.write_off_bal else a.incomingMoney end as writeOffBal, lb.write_off_type as writeOffType, lb.write_off_status as writeOffStatus ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(", w.EXECUTE_MONEY as executeMoney ");
        }
        sql.append(" from lc_bill_arrival lb ");
        sql.append(" left join lc_enrol lc on lb.LC_ID = lc.ID ");
        sql.append(" left join bt_currency c on lb.CUR_ID = c.ID ");
        sql.append(" left join sys_corp s on lb.CORP_ID  = s.ID ");
        if (batchNo != null && batchNo.length > 0 && StringUtils.isNotEmpty(batchNo[0])) {
            sql.append(" left  join (select RELATION_ID,batch_no,sum(EXECUTE_MONEY) as EXECUTE_MONEY from DOC_WIRTE_OFF_RELATION where BATCH_NO ='" + batchNo[0] + "' group by RELATION_ID,batch_no) w on  w.RELATION_ID = lb.ID  ");
        }
        sql.append(" left join (select lb.id, null as status2, null as type, null as mortgageMoney, null as incomingMoney, null as negotiationMoney from lc_bill_arrival lb where lb.STATUS in (99,8,0) ");
        sql.append(" union ");
        sql.append(" select lb.id, lm.STATUS as status2, 1 as type, lm.MORTGAGE_MONEY  as mortgageMoney, null as incomingMoney, null as negotiationMoney from lc_mortgage lm left join lc_bill_arrival lb on lb.id = lm.ARRIVAL_ID ");
        sql.append("         union ");
        sql.append(" select lb.id, li.STATUS as status2, 2 as type, null  as mortgageMoney, li.INCOMING_MONEY  as incomingMoney, null as negotiationMoney from lc_incoming li left join lc_bill_arrival lb on lb.id = li.ARRIVAL_ID ");
        //当前版本没有这个表，暂时先注释
        //sql.append("         union ");
        //sql.append(" select lb.id, ln.STATUS as status2, 3 as type, null  as mortgageMoney, null  as incomingMoney, ln.negotiation_money as negotiationMoney from lc_negotiation ln left join lc_bill_arrival lb on lb.id = ln.ARRIVAL_ID and ln.status not in (-2,-3) and ln.type=0 ");
        sql.append("         ) a on a.id = lb.ID ");
        sql.append(" where lb.ARRIVAL_TYPE = 1 and lb.STATUS != -2 and a.incomingMoney is not null ");

        return sql.toString();
    }

    /**
     * 查询核销处理 信用证交单列表数据
     *
     * @param input
     * @param corpIds 单位集合
     * @return DataTablesOutput
     */
    public DataTablesOutput getLcDataTablesOfWriteOff(DataTablesInput input, List<String> corpIds) {
        String queryType = (String) input.getFormdata().get("queryType");
//        String eqsWriteOffStatus = (String) input.getFormdata().get("eqs_writeOffStatus");
//        if(EmptyUtils.isEmpty(eqsWriteOffStatus)){
//            input.getFormdata().put("ins_writeOffStatus", "0,1");
//        }
        input.getFormdata().remove("queryType");
        StringBuilder sql = new StringBuilder();
        sql.append(getLcDataCommonSql());
        sql.append(" and  " + SqlUtil.getInSql(corpIds, "lb.corp_id", true));
//        if(StringUtils.isEmpty(queryType) || !"approve".equals(queryType)){
//            sql.append(" and lb.id not in ( select relation_id from DOC_WIRTE_OFF_RELATION where status <> 95 )");
//        }
        String eqs_id = (String) input.getFormdata().get("eqs_id");
        if (StringUtils.isNotEmpty(eqs_id) && !"#".equals(eqs_id)) {
            sql.append(" and ").append(SqlUtil.getInSql(eqs_id.split(","), "lb.id", true));
        }
        return getDynamicParamsBySQL(input, sql.toString(), LcBillArrivalVO.class, "", false, null, null);
    }

    /**
     * @param ids
     * @return
     */
    public List<LcBillArrivalVO> getLcDataByIds(List<String> ids, List<String> writeOffStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(getLcDataCommonSql());
        sql.append(" and ").append(getSQLIn(ids, 900, "lb.id"));
        if (EmptyUtils.isNotEmpty(writeOffStatus)) {
            sql.append(" and ").append(getSQLIn(writeOffStatus, 900, "lb.write_off_status"));
        }
        return listBySQLAliasToBean(sql.toString(), null, LcBillArrivalVO.class);
    }

    /**
     * @param ids
     * @return
     */
    public List<LcBillArrivalVO> getLcsOfWriteOffByIds(List<String> ids, String... batchNO) {
        StringBuilder sql = new StringBuilder();
        sql.append(getLcDataCommonSql(batchNO));
        sql.append(" and ").append(getSQLIn(ids, 900, "lb.id"));
        return listBySQLAliasToBean(sql.toString(), null, LcBillArrivalVO.class);
    }


    /**
     * 更新应收单据核销信息
     *
     * @param id
     * @param writeOffBal
     * @param writeOffStatus
     * @param writeOffType
     * @return
     */
    public int toUpdateDocManageWriteOff(String id, BigDecimal writeOffBal, String writeOffStatus, String writeOffType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update doc_manage set ");
        appendNullableAssignment(sql, params, "write_off_bal", "writeOffBal", writeOffBal);
        sql.append(", write_off_status=:writeOffStatus, write_off_type=:writeOffType where id = :id ");
        params.put("id", id);
        params.put("writeOffStatus", writeOffStatus);
        params.put("writeOffType", writeOffType);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新流水核销信息
     *
     * @param id
     * @param writeOffBal
     * @param writeOffStatus
     * @param writeOffType
     * @param fbsStatus      0未确认，1已确认，2已删除
     * @return
     */
    public int toUpdateDtlWriteOff(String id, BigDecimal writeOffBal, String writeOffStatus, String writeOffType, String fbsStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update bis_acc_dtl set ");
        appendNullableAssignment(sql, params, "write_off_bal", "writeOffBal", writeOffBal);
        sql.append(", write_off_status=:writeOffStatus, write_off_type=:writeOffType, ");
        appendNullableAssignment(sql, params, "fbs_status", "fbsStatus", fbsStatus);
        sql.append(" where id = :id ");
        params.put("id", id);
        params.put("writeOffStatus", writeOffStatus);
        params.put("writeOffType", writeOffType);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新票据核销信息
     *
     * @param id
     * @param writeOffBal
     * @param writeOffStatus
     * @param writeOffType
     * @return
     */
    public int toUpdateBillWriteOff(String id, BigDecimal writeOffBal, String writeOffStatus, String writeOffType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update ab_voucherinfo_vice set ");
        appendNullableAssignment(sql, params, "write_off_bal", "writeOffBal", writeOffBal);
        sql.append(", write_off_status=:writeOffStatus, write_off_type=:writeOffType where id = :id ");
        params.put("id", id);
        params.put("writeOffStatus", writeOffStatus);
        params.put("writeOffType", writeOffType);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新信用证核销信息
     *
     * @param id
     * @param writeOffBal
     * @param writeOffStatus
     * @param writeOffType
     * @return
     */
    public int toUpdateLcWriteOff(String id, BigDecimal writeOffBal, String writeOffStatus, String writeOffType) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update lc_bill_arrival set ");
        appendNullableAssignment(sql, params, "write_off_bal", "writeOffBal", writeOffBal);
        sql.append(", write_off_status=:writeOffStatus, write_off_type=:writeOffType where id = :id ");
        params.put("id", id);
        params.put("writeOffStatus", writeOffStatus);
        params.put("writeOffType", writeOffType);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    private void appendNullableAssignment(StringBuilder sql, Map<String, Object> params,
                                          String columnName, String parameterName, Object value) {
        sql.append(columnName).append("=");
        if (value == null) {
            sql.append("null");
        } else {
            sql.append(":").append(parameterName);
            params.put(parameterName, value);
        }
    }

    /**
     * 作废收款单
     *
     * @param billId
     * @param user
     */
    public Integer abolishNisReceivable(String billId, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update NIS_RECEIVABLE set status =:status, update_by=:updateBy, update_date=:updateDate where bill_id = :billId ");
        params.put("billId", billId);
        params.put("status", NetbankConstant.STATUE_INVALID);
        params.put("updateBy", user.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }


    /**
     * 根据应收单id获取核销历史记录
     *
     * @param docId
     * @return
     */
    public List<DocWriteOffRelationVo> getWriteOffHisDetail(String docId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select rel.batch_no as batchNo, rel.relation_id as relationId, case when rel.PAYMENT_MEANS = '1' then bad.TRANS_TIME when rel.PAYMENT_MEANS = '2' then a.BUSINESS_DATE when rel.PAYMENT_MEANS = '3' then lb.PAYMENT_DATE else null end transDate, ");
        sql.append(" rel.CREATE_DATE as createDate, rel.RELATION_ORIGIN_MONEY as relationOriginMoney, rel.EXCHANGE_RATE as exchangeRate, rel.RELATION_CONVERT_MONEY as relationConvertMoney, ");
        sql.append(" rel.PAYMENT_MEANS as paymentMeans, rel.FORCE_REASON as forceReason, rel.WRITE_OFF_NUM as writeOffNum,rel.WRITE_OFF_STATUS as afterDocWriteOffStatus, ");
        sql.append(" case when rel.WRITE_OFF_NUM in ('11','21') then rel.doc_id else rel.relation_id end as nisReceivableBillId,rel.WRITE_OFF_TYPE  as writeOffType ");
        sql.append(" from doc_wirte_off_relation rel ");
        sql.append(" left join doc_manage dm on rel.DOC_ID = dm.ID ");
        sql.append(" left join bis_acc_dtl bad on rel.RELATION_ID = bad.id and rel.PAYMENT_MEANS = '1' ");
        sql.append(" left join ab_accrecords a on rel.RELATION_ID = a.VVID and rel.PAYMENT_MEANS = '2' ");
        sql.append(" left join lc_bill_arrival lb on rel.RELATION_ID = lb.id and rel.PAYMENT_MEANS = '3' ");
        sql.append(" where rel.STATUS in (-3,95)  and rel.doc_id =:docId ");
        sql.append(" order by rel.create_date ");

        params.put("docId", docId);
        return this.listBySQLAliasToBean(sql.toString(), params, DocWriteOffRelationVo.class);
    }

    /**
     * 根据应收单id获取核销历史记录
     *
     * @param docId
     * @return
     */
    public List<DocWriteOffRelationVo> getWriteOffHisDetail2(String docId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select  dm.id as docId,rel.batch_no as batchNo, rel.relation_id as relationId, ");
        sql.append(" rel.CREATE_DATE as createDate, rel.RELATION_ORIGIN_MONEY as relationOriginMoney, rel.EXCHANGE_RATE as exchangeRate, rel.RELATION_CONVERT_MONEY as relationConvertMoney, ");
        sql.append(" rel.PAYMENT_MEANS as paymentMeans, rel.FORCE_REASON as forceReason, rel.WRITE_OFF_NUM as writeOffNum,rel.WRITE_OFF_STATUS as afterDocWriteOffStatus, ");
        sql.append(" case when rel.WRITE_OFF_NUM in ('11','21') then rel.doc_id else rel.relation_id end as nisReceivableBillId,rel.WRITE_OFF_TYPE  as writeOffType ");
        sql.append(" from doc_wirte_off_relation rel ");
        sql.append(" left join doc_manage dm on rel.DOC_ID = dm.ID ");
        sql.append(" left join bis_acc_dtl bad on rel.RELATION_ID = bad.id and rel.PAYMENT_MEANS = '1' ");
        sql.append(" left join lc_bill_arrival lb on rel.RELATION_ID = lb.id and rel.PAYMENT_MEANS = '3' ");
        sql.append(" where rel.STATUS in (-3,95)  and rel.doc_id =:docId ");
        sql.append(" order by rel.create_date ");

        params.put("docId", docId);
        return this.listBySQLAliasToBean(sql.toString(), params, DocWriteOffRelationVo.class);
    }

    public List<DocWriteOffRelationVo> getWriteOffHisDetail3(String docId) {
        String dialect = ((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();

        if (dialect.toLowerCase().contains("oracle")) {//如果是oracle数据库
            sql.append("SELECT ");
            sql.append("    LISTAGG(dm.id, ',') WITHIN GROUP (ORDER BY dm.id)                      AS docId, ");
            sql.append("    rel.batch_no                                                          AS batchNo, ");
            sql.append("    LISTAGG(rel.relation_id, ',') WITHIN GROUP (ORDER BY rel.relation_id) AS relationId, ");
            sql.append("    MAX(rel.CREATE_DATE)                                                  AS createDate, ");
            sql.append("    SUM(rel.RELATION_ORIGIN_MONEY)                                        AS relationOriginMoney, ");
            sql.append("    MAX(rel.EXCHANGE_RATE)                                                AS exchangeRate, ");
            sql.append("    SUM(rel.RELATION_CONVERT_MONEY)                                       AS relationConvertMoney, ");
            sql.append("    MAX(rel.PAYMENT_MEANS)                                                AS paymentMeans, ");
            sql.append("    MAX(rel.FORCE_REASON)                                                 AS forceReason, ");
            sql.append("    MAX(rel.WRITE_OFF_NUM)                                                AS writeOffNum, ");
            sql.append("    MAX(rel.WRITE_OFF_STATUS)                                             AS afterDocWriteOffStatus, ");
            sql.append("    MAX(CASE WHEN rel.WRITE_OFF_NUM IN ('11','21') THEN rel.doc_id ");
            sql.append("             ELSE rel.relation_id END)                                    AS nisReceivableBillId, ");
            sql.append("    MAX(rel.WRITE_OFF_TYPE)                                               AS writeOffType ");
            sql.append("FROM doc_wirte_off_relation rel ");
            sql.append("LEFT JOIN doc_manage      dm  ON rel.DOC_ID      = dm.ID ");
            sql.append("LEFT JOIN bis_acc_dtl     bad ON rel.RELATION_ID = bad.id AND rel.PAYMENT_MEANS = '1' ");
            sql.append("LEFT JOIN lc_bill_arrival lb  ON rel.RELATION_ID = lb.id  AND rel.PAYMENT_MEANS = '3' ");
            sql.append("WHERE rel.STATUS IN (-3,95) ");
            sql.append("  AND rel.BATCH_NO IN ( ");
            sql.append("        SELECT BATCH_NO FROM doc_wirte_off_relation dwor ");
            sql.append("        WHERE DOC_ID = :docId AND STATUS IN (-3,95) ");
            sql.append("        GROUP BY BATCH_NO ");
            sql.append("  ) ");
            sql.append("GROUP BY rel.BATCH_NO ");
            sql.append("ORDER BY MAX(rel.CREATE_DATE) ");
        } else {
            sql.append(" select  group_concat(dm.id) as docId,rel.batch_no as batchNo, group_concat(rel.relation_id) as relationId, ");
            sql.append(" rel.CREATE_DATE as createDate, sum(rel.RELATION_ORIGIN_MONEY) as relationOriginMoney, rel.EXCHANGE_RATE as exchangeRate, sum(rel.RELATION_CONVERT_MONEY) as relationConvertMoney, ");
            sql.append(" rel.PAYMENT_MEANS as paymentMeans, rel.FORCE_REASON as forceReason, rel.WRITE_OFF_NUM as writeOffNum,rel.WRITE_OFF_STATUS as afterDocWriteOffStatus, ");
            sql.append(" case when rel.WRITE_OFF_NUM in ('11','21') then rel.doc_id else rel.relation_id end as nisReceivableBillId,rel.WRITE_OFF_TYPE  as writeOffType ");
            sql.append(" from doc_wirte_off_relation rel ");
            sql.append(" left join doc_manage dm on rel.DOC_ID = dm.ID ");
            sql.append(" left join bis_acc_dtl bad on rel.RELATION_ID = bad.id and rel.PAYMENT_MEANS = '1' ");
            sql.append(" left join lc_bill_arrival lb on rel.RELATION_ID = lb.id and rel.PAYMENT_MEANS = '3' ");
            sql.append(" where rel.STATUS in (-3,95)  and rel.BATCH_NO in (select BATCH_NO from  doc_wirte_off_relation dwor where DOC_ID =:docId and STATUS in (-3,95) group by BATCH_NO)     group by rel.BATCH_NO  ");
            sql.append(" order by rel.create_date ");
        }

        params.put("docId", docId);
        return this.listBySQLAliasToBean(sql.toString(), params, DocWriteOffRelationVo.class);
    }


    /**
     * 根据billid获取收款单id
     *
     * @param billId
     * @return
     */
    public NisReceivableDTO getNisReceivableByBillId(String billId) {
        String sql = " select * from nis_receivable where bill_id =:billId and status >=95 ";
        Map<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        List<NisReceivableDTO> docWriteOffRelations = listBySQLAliasToBean(sql, params, NisReceivableDTO.class);
        if (EmptyUtils.isNotEmpty(docWriteOffRelations)) {
            return docWriteOffRelations.get(0);
        }
        return new NisReceivableDTO();
    }

    public NisReceivableDTO getNisReceivableByBillIdAndBatchNo(String billId, String batchNo) {
        Map<String, String> params = new HashMap<>();
        params.put("billId", billId);
        String sql = "select * from nis_receivable where status >=95 and bill_id =:billId";
        NisReceivableDTO docWriteOffRelations = null;
        if (StringUtils.isNotEmpty(batchNo)) {
            String batchNoSql = " and bus_bill_no =:batchNo";
            params.put("batchNo", batchNo);
            docWriteOffRelations = getBySQLAliasToBean(sql + batchNoSql, params, NisReceivableDTO.class);
        }
        if (!Optional.ofNullable(docWriteOffRelations).isPresent()) {
            //历史数据没有保存批次号
            params.remove("batchNo");
            docWriteOffRelations = getBySQLAliasToBean(sql, params, NisReceivableDTO.class);
        }
        return docWriteOffRelations;
    }

    public DataTablesOutput queryForceWriteOff(String docId, String batchNo) {
        Map<String, String> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select dm.CODE ,dm.corp_id as corpId,dm.source, dm.end_Date as endDate,dm.aging_Date as agingDate,dm.RECEIPT_DATE as receiptDate ,dm.external_Name as externalName, dwor.FORCE_REASON_TYPE as forceReasonType, " +
                "dwor.WRITE_OFF_STATUS as writeOffStatus,dwor.BEFORE_WRITE_OFF_BAL  as beforeWriteOffBal,nbt.BUS_TYPE_NAME as batchBusTypeName");
        sql.append(" ,dwor.WRITE_OFF_TYPE as writeOffType  , (dwor.BEFORE_WRITE_OFF_BAL - dwor.RELATION_CONVERT_MONEY) as relationConvertMoney, dwor.FORCE_PAYMENT_MEANS as forcePaymentMeans,");
        sql.append(" dwor.FORCE_REASON as forceReason, dwor.RELATION_CONVERT_MONEY as afterDocWriteOffBal,dwor.BEFORE_WRITE_OFF_STATUS  as beforeWriteOffStatus ");
        sql.append(" from doc_manage dm left join doc_wirte_off_relation dwor  on dm.id = dwor.DOC_ID");
        sql.append(" left join nis_bus_type nbt on  dm.BATCH_BUS_TYPE = nbt.ID ");
        sql.append(" where 1 = 1");
        sql.append(" and dm.id =:docId");
        sql.append(" and dwor.BATCH_NO =:batchNo");
        params.put("docId", docId);
        params.put("batchNo", batchNo);
        return getDynamicBySQL(new DataTablesInput(), sql.toString(), ForceWriteOffVo.class, "", false, params);
    }


}
