package com.bytter.biz.receipt.dao;

import com.bytter.biz.bdlink.vo.BisBankDtlBillVO;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.receipt.entity.BisBankReceiptDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

/**
 * 银行电子回单Dao
 *
 * @author zuoy 2018-04-09
 */
@Repository
public class BisBankReceiptDtlDao extends BaseDao<BisBankReceiptDtl, String> {

    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBankReceiptDtlVO> getDataTablesUnion(DataTablesInput input, StringBuilder commonSql) {
        Map<String,Object> params = new HashMap<>();
        //交易时间 兼容Oracle格式化
        String gedCharageDate = (String) input.getFormdata().get("ged_charageDate");
        String ledCharageDate = (String) input.getFormdata().get("led_charageDate");
        String usefor = (String) input.getFormdata().get("likes_usefor");

        StringBuffer dateJoinSql = new StringBuffer();
        String dialect = ((SessionImpl) getSession()).getFactory().getDialect().getClass().getName();
        if (StringUtils.isNotBlank(gedCharageDate)) {
            gedCharageDate = gedCharageDate + " 00:00:00";
            if(dialect.toLowerCase().contains("oracle")){//如果是oracle数据库
                dateJoinSql.append(" and b.charage_date >= to_date('").append(gedCharageDate).append("','yyyy-MM-dd hh24:mi:ss')");
            }else {
                dateJoinSql.append(" and b.charage_date >= '").append(gedCharageDate).append("'");
            }
            input.getFormdata().remove("ged_charageDate");
        }
        if(StringUtils.isNotBlank(ledCharageDate)){
            ledCharageDate = ledCharageDate + " 23:59:59";
            if(dialect.toLowerCase().contains("oracle")){//如果是oracle数据库
                dateJoinSql.append(" and b.charage_date <= to_date('").append(ledCharageDate).append("','yyyy-MM-dd hh24:mi:ss')");
            }else {
                dateJoinSql.append(" and b.charage_date <= '").append(ledCharageDate).append("'");
            }
            input.getFormdata().remove("led_charageDate");
        }

        input.getFormdata().remove("likes_usefor");
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append(" t.id, t.pay_acc_no, t.pay_acc_name, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no, t.payee_acc_name,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt, t.cur_id, t.usefor, t.abs, t.remark, t.bus_type, t.bus_no, t.charage_date, ");
        sql.append(" t.receipt_no, t.receipt_check_code, t.trans_serial_no, t.net_code, t.teller_no, bt_nvl_num(t.prints, 0) prints,");
        sql.append(" t.cd_sign,t.is_from,t.status,");
        sql.append(" t.bank_acc, t.acc_name, t.bank_name,t.opp_acc_no,t.opp_acc_name,t.opp_acc_bank,t.corpId,t.corpName ");
        sql.append(" FROM ( ");

        // 支出部分
        sql.append(" SELECT");
        sql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        sql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date, ");
        sql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, bt_nvl_num(b.prints, 0) prints, ");
        sql.append(" b.cd_sign,b.is_from as is_from,b.status,");
        sql.append(" b.pay_acc_no as bank_acc, b.pay_acc_name as acc_name, b.pay_branch_name as bank_name, ");
        sql.append(" b.payee_acc_no as opp_acc_no, b.payee_acc_name as opp_acc_name, b.payee_bracn_name as opp_acc_bank,");
        sql.append(" a.corp_id corpId,sc.name corpName ");
        sql.append(" FROM bis_bank_receipt_dtl b ");
        sql.append(" LEFT JOIN bt_bank_acc a ON b.pay_acc_no = a.bank_acc ");
        sql.append(" LEFT JOIN sys_corp sc ON a.corp_id = sc.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '1'");
        if (StringUtils.isNotEmpty(usefor)) {
            sql.append(" and b.usefor like '%").append(usefor).append("%'");
        }
        // 单位权限
        if (StringUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        if (StringUtils.isNotEmpty(dateJoinSql)) {
            sql.append(dateJoinSql);
        }
        sql.append(" UNION");
        // 收入部分
        sql.append(" SELECT");
        sql.append(" b.id, b.pay_acc_no, b.pay_acc_name, b.pay_branch_name, b.bif_code_id, b.payee_acc_no, b.payee_acc_name,");
        sql.append(" b.payee_bracn_name, b.trans_amt, b.cur_id, b.usefor, b.abs, b.remark, b.bus_type, b.bus_no, b.charage_date, ");
        sql.append(" b.receipt_no, b.receipt_check_code, b.trans_serial_no, b.net_code, b.teller_no, bt_nvl_num(b.prints, 0) prints, ");
        sql.append(" b.cd_sign,b.is_from as is_from,b.status,");
        sql.append(" b.payee_acc_no as bank_acc, b.payee_acc_name as acc_name, b.payee_bracn_name as bank_name, ");
        sql.append(" b.pay_acc_no as opp_acc_no, b.pay_acc_name as opp_acc_name, b.pay_branch_name as opp_acc_bank, ");
        sql.append(" a.corp_id corpId,sc.name corpName ");
        sql.append(" FROM bis_bank_receipt_dtl b ");
        sql.append(" LEFT JOIN bt_bank_acc a ON b.payee_acc_no = a.bank_acc");
        sql.append(" LEFT JOIN sys_corp sc ON a.corp_id = sc.id ");
        sql.append(" WHERE 1=1");
        sql.append(" AND b.cd_sign = '0'");
        if (StringUtils.isNotEmpty(usefor)) {
            sql.append(" and b.usefor like '%").append(usefor).append("%'");
        }
        // 单位权限
        if (StringUtils.isNotEmpty(commonSql)) {
            sql.append(commonSql);
        }
        if (StringUtils.isNotEmpty(dateJoinSql)) {
            sql.append(dateJoinSql);
        }
        sql.append(" ) t ");
        sql.append(" WHERE 1=1 ");
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


        return getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlVO.class, "t", false, params);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        String hql = "update BisBankReceiptDtl set prints = bt_nvl_num(prints, 0) + 1  where id = :id ";
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
    public BisBankReceiptDtlVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.pay_acc_no as payAccNo, b.pay_acc_name as payAccName, b.pay_branch_name as payBankName, b.bif_code_id as bifCode, b.payee_acc_no as payeeAccNo, b.payee_acc_name as payeeAccName,");
        sql.append(" b.payee_bracn_name as payeeBankName, b.trans_amt as transAmt, b.cur_id as curId, b.usefor as usefor, b.abs as abs, b.remark as remark, b.bus_type as busType, b.bus_no as busNo, b.charage_date as charageDate, ");
        sql.append(" b.receipt_no as receiptNo, b.receipt_check_code as receiptCheckCode, b.trans_serial_no as transSerialNo, b.net_code as netCode, b.teller_no as tellerNo, b.filetype as filetype, bt_nvl_num(b.prints, 0)  as prints,");
        sql.append(" p.ebillimg as ebillimg,p.pdf_to_img as pdfToImg,b.cd_sign as cdSign,b.is_from as isFrom,b.status as status,b.SAVE_TYPE as saveType,b.FILE_PATH as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL b ");
        sql.append(" left join BIS_BANK_RECEIPT_PDF p on b.id=p.receipt_Id");
        sql.append(" where 1 = 1 AND b.cd_sign = '1'");
        sql.append(" and  b.id=:id");
        sql.append(" UNION all");
        sql.append(" select b.id as id, b.pay_acc_no as payAccNo, b.pay_acc_name as payAccName, b.pay_branch_name as payBankName, b.bif_code_id as bifCode, b.payee_acc_no as payeeAccNo, b.payee_acc_name as payeeAccName,");
        sql.append(" b.payee_bracn_name as payeeBankName, b.trans_amt as transAmt, b.cur_id as curId, b.usefor as usefor, b.abs as abs, b.remark as remark, b.bus_type as busType, b.bus_no as busNo, b.charage_date as charageDate, ");
        sql.append(" b.receipt_no as receiptNo, b.receipt_check_code as receiptCheckCode, b.trans_serial_no as transSerialNo, b.net_code as netCode, b.teller_no as tellerNo, b.filetype as filetype, bt_nvl_num(b.prints, 0)  as prints,");
        sql.append(" p.ebillimg as ebillimg,p.pdf_to_img as pdfToImg,b.cd_sign as cdSign,b.is_from as isFrom,b.status as status,b.SAVE_TYPE as saveType,b.FILE_PATH as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL b ");
        sql.append(" left join BIS_BANK_RECEIPT_PDF p on b.id=p.receipt_Id");
        sql.append(" where 1 = 1 AND b.cd_sign = '0'");
        sql.append(" and  b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisBankReceiptDtlVO.class);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param id
     * @return
     */
    public BisBankReceiptDtlVO getByIdUnionV2(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.filetype AS filetype,p.EBILLIMG as ebillimg,p.PDF_TO_IMG as pdfToImg,b.SAVE_TYPE as saveType,b.FILE_PATH as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL b left join BIS_BANK_RECEIPT_PDF p ON b.id = p.receipt_Id ");
        sql.append(" where 1 = 1 AND b.cd_sign in ('0','1') ");
        sql.append(" and  b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        List<Map> mapList = listBySQL(sql.toString(), params);
        if(EmptyUtils.isEmpty(mapList)){
            return null;
        }
        BisBankReceiptDtlVO bisBankReceiptDtlVO = BeanUtils.copyMapProperties(mapList.get(0), BisBankReceiptDtlVO.class);
        return bisBankReceiptDtlVO;
    }
    /**
     * 自动迁移数据调用方法
     * @param date
     * @return
     */
    public DataTablesOutput getByTime(DataTablesInput input, Date date){
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id as id, t.pay_acc_no as payAccNo, t.pay_acc_name as payAccName, t.pay_branch_name as payBankName, t.bif_code_id as bifCode, t.payee_acc_no as payeeAccNo, t.payee_acc_name as payeeAccName,");
        sql.append(" t.payee_bracn_name as payeeBankName, t.trans_amt as transAmt, t.cur_id as curId, t.usefor as usefor, t.abs as abs, t.remark as remark, t.bus_type as busType, t.bus_no as busNo, t.charage_date as charageDate, ");
        sql.append(" t.receipt_no as receiptNo, t.receipt_check_code as receiptCheckCode, t.trans_serial_no as transSerialNo, t.net_code as netCode, t.teller_no as tellerNo, t.filetype as filetype, bt_nvl_num(t.prints, 0)  as prints,");
        sql.append(" p.ebillimg as ebillimg,p.pdf_to_img as pdfToImg,t.CD_SIGN as cdSign,t.file_path as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL t ");
        sql.append(" left join BIS_BANK_RECEIPT_PDF p on t.id=p.receipt_Id where 1 = 1");
//		sql.append(" and  b.CHARAGE_DATE < :date");
//		HashMap<String, Object> params = new HashMap<>();
//		params.put("date", date);
        input.getFormdata().put("led_charageDate", DateUtils.format(date));
        return  getDynamicBySQL(input, sql.toString(), BisBankReceiptDtlVO.class, "t", false);
    }
    /**
     * 自动迁移数据调用方法 获取需求迁移的总数
     * @param date
     * @return
     */
    public BigDecimal getByTimeCount(Date date){
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) as count ");
        sql.append(" from BIS_BANK_RECEIPT_DTL b ");
        sql.append(" where  b.CHARAGE_DATE < :date");
        HashMap<String, Object> params = new HashMap<>();
        params.put("date", date);
        BigDecimal result = new BigDecimal(0);
        Map map = getBySQL(sql.toString(),params);
        result = (BigDecimal) map.get("COUNT");
        return result;
    }
    /**
     * 根据电子回单id得到关联的流水id
     * @param id
     * @return
     */
    public List<BisBankDtlBillVO> getDtls(String id){
        String sql="select brd.RECEIPT_ID,brd.DTL_ID,brd.RULES_NAME from BIS_RECEIPT_DTL brd where brd.RECEIPT_ID=:id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql, params, BisBankDtlBillVO.class);
    }
    /**
     * 根据付款ID获取电子回单信息
     *
     * @param ids
     * @return
     */
    public List<BisBankReceiptDtlVO> listByIds(String ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id, b.pay_acc_no as payAccNo, b.pay_acc_name as payAccName, b.pay_branch_name as payBankName, b.bif_code_id as bifCode, b.payee_acc_no as payeeAccNo, b.payee_acc_name as payeeAccName,");
        sql.append(" b.payee_bracn_name as payeeBankName, b.trans_amt as transAmt, b.cur_id as curId, b.usefor as usefor, b.abs as abs, b.remark as remark, b.bus_type as busType, b.bus_no as busNo, b.charage_date as charageDate, ");
        sql.append(" b.receipt_no as receiptNo, b.receipt_check_code as receiptCheckCode, b.trans_serial_no as transSerialNo, b.net_code as netCode, b.teller_no as tellerNo, b.filetype as filetype, bt_nvl_num(b.prints, 0)  as prints,");
        sql.append(" p.ebillimg as ebillimg,p.pdf_to_img as pdfToImg,b.SAVE_TYPE as saveType,b.FILE_PATH as filePath ");
        sql.append(" from BIS_BANK_RECEIPT_DTL b ");
        sql.append(" left join BIS_BANK_RECEIPT_PDF p on b.id=p.receipt_Id where 1 = 1");
        sql.append(" and  ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "b.id", true));
        return listBySQLAliasToBean(sql.toString(), null, BisBankReceiptDtlVO.class);
    }
    /**
     * 根据付款表id得到电子回单表ID集合
     * @param payId
     * @return 回单表ID集合
     */
    public List<String> getIdsByPayId(String payId){
        String sql="select brd.RECEIPT_ID from nis_bill_bank_serial_no nis inner join bis_receipt_dtl brd on nis.DTL_ID=brd.DTL_ID where nis.BILL_ID=:payId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("payId", payId);
        List list = this.listBySQL(sql,params);
        List<String> listId=new ArrayList<>();
        for(Object info:list){
            listId.add(((Map)info).get("RECEIPT_ID").toString());
        }
        return listId;
    }
}
