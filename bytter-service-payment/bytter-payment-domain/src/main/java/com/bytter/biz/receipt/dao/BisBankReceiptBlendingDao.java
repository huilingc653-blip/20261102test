package com.bytter.biz.receipt.dao;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlAutoVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.biz.receipt.entity.BisReceiptDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BisBankReceiptBlendingDao extends BaseDao<BisBankReceiptDtl, String> {

    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        String eqs_id = (String) input.getFormdata().get("eqs_id");
        input.getFormdata().remove("eqs_id");
        String eqs_blendSign = (String) input.getFormdata().get("eqs_blendSign");
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(" SELECT");
        selectSql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        selectSql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date,b.BLEND_SIGN, ");
        selectSql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, b.cd_sign,a.corp_id ");
        selectSql.append(" FROM bis_bank_receipt_dtl b LEFT JOIN bt_bank_acc a");

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id, t.pay_acc_no as payAccNo, t.pay_acc_name as payAccName, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no as payeeAccNo, t.payee_acc_name as payeeAccName ,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt as transAmt, t.cur_id as curId, t.usefor, t.abs, t.remark, t.bus_type as busType, t.bus_no as busNo, t.charage_date as charageDate,t.BLEND_SIGN as blendSign, ");
        sql.append(" t.receipt_no as receiptNo, t.receipt_check_code as receiptCheckCode, t.trans_serial_no as transSerialNo, t.net_code as netCode, t.teller_no as tellerNo, t.cd_sign as cdSign,t.corp_id as corpId ");
        sql.append(" FROM (");
        sql.append(selectSql);
        sql.append(" ON b.pay_acc_no = a.bank_acc");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '1'");
        if (StringUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        sql.append(" UNION");
        sql.append(selectSql);
        sql.append(" ON b.payee_acc_no = a.bank_acc");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '0'");
        if (StringUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        sql.append(" ) t ");
        sql.append(" WHERE 1=1");
        boolean isHasId = false;
        if(EmptyUtils.isNotEmpty(eqs_id)){
            if("#".equals(eqs_id)){
                sql.append(" and t.id = '"+eqs_id+"' ");
            }else {
                isHasId = true;
                sql.append(" and  " + SqlUtil.getInSql(eqs_id.split(","), "t.id", true));
            }
        }
        if(eqs_blendSign == null && !isHasId){
            sql.append(" and t.blend_sign = '0' ");
        }
        return getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlVO.class, "t", false);
    }



    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnionForAotoBlendingDtl(DataTablesInput input) {
        String eqs_blendSign = (String) input.getFormdata().get("eqs_blendSign");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id, t.pay_acc_no as payAccNo, t.pay_acc_name as payAccName, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no as payeeAccNo, t.payee_acc_name as payeeAccName ,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt as transAmt, t.cur_id as curId, t.usefor, t.abs, t.remark, t.bus_type as busType, t.bus_no as busNo, t.charage_date as charageDate,t.BLEND_SIGN as blendSign, ");
        sql.append(" t.receipt_no as receiptNo, t.receipt_check_code as receiptCheckCode, t.trans_serial_no as transSerialNo, t.net_code as netCode, t.teller_no as tellerNo, t.cd_sign as cdSign ");
        sql.append(" FROM bis_bank_receipt_dtl t where 1=1");
        if(eqs_blendSign == null){
            sql.append(" and t.blend_sign = '0' ");
        }
        return getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlVO.class, "t", false);
    }


    /**
     * 根据id获取银行电子回单
     *
     * @param ids
     * @return
     */
    public List<BisBankReceiptDtlVO> listReceiptByids(String ids) {
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(" SELECT");
        selectSql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        selectSql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date,b.BLEND_SIGN, ");
        selectSql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, b.cd_sign,a.corp_id ");
        selectSql.append(" FROM bis_bank_receipt_dtl b LEFT JOIN bt_bank_acc a");

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id, t.pay_acc_no as payAccNo, t.pay_acc_name as payAccName, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no as payeeAccNo, t.payee_acc_name as payeeAccName ,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt as transAmt, t.cur_id as curId, t.usefor, t.abs, t.remark, t.bus_type as busType, t.bus_no as busNo, t.charage_date as charageDate,t.BLEND_SIGN as blendSign, ");
        sql.append(" t.receipt_no as receiptNo, t.receipt_check_code as receiptCheckCode, t.trans_serial_no as transSerialNo, t.net_code as netCode, t.teller_no as tellerNo, t.cd_sign as cdSign,t.corp_id as corpId ");
        sql.append(" FROM (");
        sql.append(selectSql);
        sql.append(" ON b.pay_acc_no = a.bank_acc");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '1'");
        sql.append(" UNION");
        sql.append(selectSql);
        sql.append(" ON b.payee_acc_no = a.bank_acc");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '0'");
        sql.append(" ) t");
        sql.append(" WHERE 1=1");
        if (EmptyUtils.isNotEmpty(ids)) {
            sql.append(" and " + SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "t.id", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, BisBankReceiptDtlVO.class);
    }

    /**
     * 更新流水电子回单勾对状态
     * @param id
     * @param receiptBlendSign
     * @return
     */
    public int toUpdateBlendSignForDtl(String id, String receiptBlendSign) {
        String sql = " update bis_acc_dtl set RECEIPT_BLEND_SIGN = :receiptBlendSign where id = :id";
        if (CommonConstant.YES.equals(receiptBlendSign)) {
            sql += " and (RECEIPT_BLEND_SIGN<>:receiptBlendSign or RECEIPT_BLEND_SIGN='' or RECEIPT_BLEND_SIGN is null)";
        } else {
            sql += " and RECEIPT_BLEND_SIGN<>:receiptBlendSign";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("receiptBlendSign", receiptBlendSign);
        return createSQLQuery(sql, map).executeUpdate();
    }

    /**
     * 更新电子回单勾兑状态表
     *
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id, String blendSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update BIS_BANK_RECEIPT_DTL set BLEND_SIGN = :blendSign where id = :id ");
        params.put("id", id);
        params.put("blendSign", blendSign);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新电子回单是否自动勾对的值
     * @param id
     * @param autoSign
     * @return
     */
    public int toUpdateAutoSign(String id, String autoSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update BIS_BANK_RECEIPT_DTL set AUTO_SIGN = :autoSign where id = :id ");
        params.put("id", id);
        params.put("autoSign", autoSign);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 更新电子回单勾兑状态表
     *
     * @param id
     * @param blendSign
     * @return
     */
    public int toUpdateBlendSign(String id, String blendSign,String oldBlendSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update BIS_BANK_RECEIPT_DTL set BLEND_SIGN = :blendSign where id = :id ");
        params.put("id", id);
        params.put("blendSign", blendSign);
        if(EmptyUtils.isNotEmpty(oldBlendSign)){
            if(BtBankAccConstant.BLENDSIGN_0.equals(oldBlendSign)) {
                sql.append(" and (BLEND_SIGN = :oldBlendSign or BLEND_SIGN='' or BLEND_SIGN is null) ");
            }else{
                sql.append(" and (BLEND_SIGN = :oldBlendSign");
            }
            params.put("oldBlendSign", oldBlendSign);
        }
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 插入银行电子回单和银行流水的关系
     *
     * @param dtlId
     * @param receiptId
     */
    public int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName) {
        String sql = " insert into BIS_RECEIPT_DTL(DTL_ID,RECEIPT_ID,RULES_NAME,PAYMENTBLENDING_DATE) values (?,?,?,?)";
        Date PAYMENTBLENDING_DATE = DateUtils.getDate();
        SQLQuery sqlQuery = createSQLQuery(sql, dtlId, receiptId, rulesName,PAYMENTBLENDING_DATE);
        return sqlQuery.executeUpdate();
    }

    /**
     * 删除中间关联表
     *
     * @param receiptId
     */
    public void deleteBisReceiptDtl(String receiptId) {
        String sql = " delete from BIS_RECEIPT_DTL where RECEIPT_ID =:receiptId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("receiptId", receiptId);
        createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 删除银行电子回单信息
     * @param receiptId
     */
    public void deleteBisBankReceiptDtl(String receiptId) {
        StringBuilder sql=new StringBuilder();
        sql.append("  DELETE FROM BIS_BANK_RECEIPT_DTL ");
        sql.append("  WHERE id = :receiptId ");
        sql.append("  AND EXISTS ( ");
        sql.append("          SELECT 1 ");
        sql.append("  FROM bis_bank_receipt_import ");
        sql.append(" WHERE RECEIPT_ID = :receiptId ");
        sql.append(" ) ");

        HashMap<String, Object> params = new HashMap<>();
        params.put("receiptId", receiptId);
        createSQLQuery(sql.toString()).setProperties(params).executeUpdate();
    }

    /**
     * 删除中间关联表
     * @param dtlId
     */
    public void deleteDtlId(String dtlId) {
        String sql = " delete from BIS_RECEIPT_DTL where DTL_ID =:dtlId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据银行电子回单id获取中间关联表的流水id
     *
     * @param receiptId
     * @return
     */
    public List<String> getDtlId(String receiptId) {
        String sql = " select  DTL_ID from BIS_RECEIPT_DTL where RECEIPT_ID =:receiptId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("receiptId", receiptId);
        return createSQLQuery(sql, params).list();
    }

    /**
     * 根据银行电子回单id获取中间关联表的流水id
     *
     * @param receiptId
     * @return
     */
    public List<BisReceiptDtl> getBisReceiptDtl(String receiptId) {
        String sql = " select  * from BIS_RECEIPT_DTL where RECEIPT_ID =:receiptId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("receiptId", receiptId);
        return listBySQLAliasToBean(sql,params,BisReceiptDtl.class);
    }

    /**
     * 根据银行电子回单id获取中间关联表的流水id
     *
     * @param dtlId
     * @return
     */
    public List<String> getReceiptId(String dtlId) {
        String sql = " select  RECEIPT_ID from BIS_RECEIPT_DTL where DTL_ID =:dtlId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return createSQLQuery(sql, params).list();
    }

    /**
     * 根据银行电子回单id获取中间关联表的流水id
     *
     * @param dtlId
     * @return
     */
    public List<String> getDtlId(List<String> dtlId) {
        String sql = " select  DTL_ID from BIS_RECEIPT_DTL where DTL_ID in (:dtlId)  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return createSQLQuery(sql, params).list();
    }

    public DataTablesOutput getDtlRules(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from(select bbd.id as receiptId,bbes.rules as rules,bbes.rules_name as rulesName,a.corp_id as corpId,bbd.trans_amt as amt,bbd.pay_acc_no as payAccNo,");
        sql.append(" bbd.pay_branch_name as payBranchName,bbd.payee_bracn_name as payeeBracnName,bbd.BIS_EBILL_NO as bisEbillNo,");
        sql.append(" bbd.receipt_no as receiptNo,bbd.trans_serial_no as transSerialNo,");
        sql.append(" bbes.is_exist_id as isExistId,bbes.dtl_only_id as dtlOnlyId,bbes.receipt_only_id as receiptOnlyId,");
        sql.append(" bbd.pay_acc_name as payAccName,bbd.payee_acc_no as payeeAccNo,bbd.payee_acc_name as payeeAccName,");
        sql.append(" bbd.cd_sign as cdSign,bbd.charage_date as charageDate,bbd.cur_id as curId,bbd.usefor as usefor,bbes.trans_Days as transDays ");
        sql.append(" from BIS_BANK_RECEIPT_DTL bbd");
        sql.append("  INNER JOIN  BIS_BIF_INIT bbi on  bbd.bif_code_id = bbi.id");
        sql.append("  INNER JOIN  BIS_BANK_EXECUTION_STRATEGY bbes on bbi.bank_type_id = bbes.bank_type_id  and bbes.type = '1' and bbes.is_enable = '1'");
        sql.append(" LEFT JOIN bt_bank_acc a ON bbd.payee_acc_no = a.bank_acc");
        sql.append(" where bbd.blend_sign='0'  and bbd.cd_sign='0' and (bbd.auto_sign = '0' or bbd.auto_sign='' or bbd.auto_sign is null) ");
        sql.append(" UNION all");
        sql.append(" select bbd.id as receiptId,bbes.rules as rules,bbes.rules_name as rulesName,a.corp_id as corpId,bbd.trans_amt as amt,bbd.pay_acc_no as payAccNo,");
        sql.append(" bbd.pay_branch_name as payBranchName,bbd.payee_bracn_name as payeeBracnName,bbd.BIS_EBILL_NO as bisEbillNo,");
        sql.append(" bbd.receipt_no as receiptNo,bbd.trans_serial_no as transSerialNo,");
        sql.append(" bbes.is_exist_id as isExistId,bbes.dtl_only_id as dtlOnlyId,bbes.receipt_only_id as receiptOnlyId,");
        sql.append(" bbd.pay_acc_name as payAccName,bbd.payee_acc_no as payeeAccNo,bbd.payee_acc_name as payeeAccName,");
        sql.append(" bbd.cd_sign as cdSign,bbd.charage_date as charageDate,bbd.cur_id as curId,bbd.usefor as usefor,bbes.trans_Days as transDays ");
        sql.append(" from BIS_BANK_RECEIPT_DTL bbd");
        sql.append("  INNER JOIN  BIS_BIF_INIT bbi on  bbd.bif_code_id = bbi.id");
        sql.append("  INNER JOIN  BIS_BANK_EXECUTION_STRATEGY bbes on bbi.bank_type_id = bbes.bank_type_id  and bbes.type = '1' and bbes.is_enable = '1'");
        sql.append(" LEFT JOIN bt_bank_acc a ON bbd.pay_acc_no = a.bank_acc");
        sql.append(" where bbd.blend_sign='0'  and bbd.cd_sign='1' and (bbd.auto_sign = '0' or bbd.auto_sign='' or bbd.auto_sign is null) ) t where 1=1 ");
        //sql.append( " and t.bisEbillNo = '330617453GVBN79OCC8'");
        return getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlAutoVO.class, "t", false);
    }

    public List<BisAccDtlDTO> getJust(String dtlONlyId, String check) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append("select b.id as id,b.amt from bis_acc_dtl b where b."+dtlONlyId+"=:bisEbillNo and b.id not in (select DTL_ID from BIS_RECEIPT_DTL)");
        params.put("bisEbillNo", check);
        return listBySQLAliasToBean(sql.toString(), params, BisAccDtlDTO.class);
    }

    public List<BisBankReceiptDtlAutoVO> getForReceipt(String bankAccTypeStr, Map<String, String> map, String cdSingnStr) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, String> params = new HashMap<String, String>();
        sql.append("select b.*,b.id as receiptId from BIS_BANK_RECEIPT_DTL b where 1=1 ");
        if (StringUtils.isNotEmpty(map)) {
            if (bankAccTypeStr.contains("amt")) {
                    sql.append(" and b.trans_amt=:amt");
                    params.put("amt", map.get("amt"));
            }
            if (bankAccTypeStr.contains("oppAccNo")) {
                if (cdSingnStr.equals("0")) {
                    if ("null".equals(map.get("payAccNo"))) {
                        sql.append(" and (b.pay_acc_no is null or b.pay_acc_no='')");
                    } else {
                        sql.append(" and b.pay_acc_no=:oppAccNo");
                        params.put("oppAccNo", map.get("payAccNo"));
                    }
                } else if (cdSingnStr.equals("1")) {
                    if ("null".equals(map.get("payeeAccNo"))) {
                        sql.append(" and (b.payee_acc_no is null or b.payee_acc_no='')");
                    } else {
                        sql.append(" and b.payee_acc_no=:oppAccNo");
                        params.put("oppAccNo", map.get("payeeAccNo"));
                    }
                }
            }
            if (bankAccTypeStr.contains("oppAccName")) {
                if (cdSingnStr.equals("0")) {
                        sql.append(" and b.pay_acc_name=:oppAccName");
                        params.put("oppAccName", map.get("payAccName"));

                } else if (cdSingnStr.equals("1")) {
                        sql.append(" and b.payee_acc_name=:oppAccName");
                        params.put("oppAccName", map.get("payeeAccName"));

                }
            }
            if (bankAccTypeStr.contains("bankAcc")||bankAccTypeStr.contains("corpId")) {
                if (cdSingnStr.equals("0")) {
                    if ("null".equals(map.get("payeeAccNo"))) {
                        sql.append(" and (b.payee_acc_no is null or b.payee_acc_no='')");
                    } else {
                        sql.append(" and b.payee_acc_no=:bankAcc");
                        params.put("bankAcc", map.get("payeeAccNo"));
                    }

                } else if (cdSingnStr.equals("1")) {
                    if ("null".equals(map.get("payAccNo"))) {
                        sql.append(" and (b.pay_acc_no is null or b.pay_acc_no='')");
                    } else {
                        sql.append(" and b.pay_acc_no=:bankAcc");
                        params.put("bankAcc", map.get("payAccNo"));
                    }
                }
            }
            if (bankAccTypeStr.contains("accName")) {
                if (cdSingnStr.equals("0")) {
                        sql.append(" and b.payee_acc_name=:accName");
                        params.put("accName", map.get("payeeAccName"));

                } else if (cdSingnStr.equals("1")) {
                        sql.append(" and b.pay_acc_name=:accName");
                        params.put("accName", map.get("payAccName"));
                }
            }
            if (bankAccTypeStr.contains("transTime")) {
                    sql.append(" and BT_TO_CHAR(b.CHARAGE_DATE,'yyyy-mm-dd')=:transTime");
                    params.put("transTime", map.get("charageDate"));
            }

                sql.append(" and b.cd_sign=:cdSign");
                params.put("cdSign", map.get("cdSign"));
            if (bankAccTypeStr.contains("uses")) {
                    sql.append(" and b.usefor=:uses");
                    params.put("uses", map.get("usefor"));
            }
            if (bankAccTypeStr.contains("curId")) {
                    sql.append(" and b.cur_id=:curId");
                    params.put("curId", map.get("curId"));
            }
        } else {
            if (bankAccTypeStr.equals("BIS_EBILL_NO")) {
                sql.append(" and b.BIS_EBILL_NO=:bisEbillNo");
                params.put("bisEbillNo", cdSingnStr);
            }
            if (bankAccTypeStr.equals("TRANS_SERIAL_NO")) {
                sql.append(" and b.TRANS_SERIAL_NO=:bisEbillNo");
                params.put("bisEbillNo", cdSingnStr);
            }
        }
        sql.append(" and b.id not in (select RECEIPT_ID from BIS_RECEIPT_DTL)");
        sql.append(" and (b.BLEND_SIGN = '0' or b.BLEND_SIGN ='' or b.BLEND_SIGN is null)");
        sql.append(" and (b.AUTO_SIGN = '0' or b.AUTO_SIGN = '' or b.AUTO_SIGN is null) ");
        return listBySQLAliasToBean(sql.toString(), params, BisBankReceiptDtlAutoVO.class);
    }
}
