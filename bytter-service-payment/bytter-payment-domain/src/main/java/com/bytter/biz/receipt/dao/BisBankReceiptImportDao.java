package com.bytter.biz.receipt.dao;

import com.bytter.biz.payment.api.dto.BisBankReceiptImportDTO;
import com.bytter.biz.receipt.entity.BisBankReceiptImport;
import com.bytter.biz.receipt.vo.BisBankReceiptImportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caop
 * @date 2024/8/2 17:12
 */
@Repository
public class BisBankReceiptImportDao extends BaseDao<BisBankReceiptImport, String> {

    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select t.id,t.bif_code_id as bifCodeId,t.bank_acc as bankAcc,t.cur_id as curId,t.import_date as importDate,");
        sql.append(" t.attachment_id as attachmentId,t.receipt_id as receiptId,t.blend_sign as blendSign,t.status,");
        sql.append(" BT_CONCAT3( sa.name,'.',sa.type ) as fileName,bba.corp_id as corpId,sc.name as corpName,t.acc_dtl_id as accDtlId,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,dtl.amt as amt,REPORT_TEXT as reportText ");
        sql.append(" from BIS_BANK_RECEIPT_IMPORT t left join sys_attachment sa on  t.attachment_id = sa.id ");
        sql.append(" left join bt_bank_acc bba on bba.bank_acc = t.bank_acc ");
        sql.append(" left join sys_corp sc on sc.id = bba.corp_id");
        sql.append(" left join bis_acc_dtl dtl on dtl.id=t.acc_dtl_id");
        sql.append(" where 1=1 ");
        return sql.toString();
    }

    public List<BisBankReceiptImport> queryListInId(List<String> ids){
        String sql="select * from BIS_BANK_RECEIPT_IMPORT where status<>:status and id in(:ids)";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        map.put("status", CommonConstant.STATUE_DELETE);
         return this.listBySQLAliasToBean(sql,map,BisBankReceiptImport.class);
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(" and ").append(joinSql);
        }
        sql.append(" and t.status<>"+ CommonConstant.STATUE_DELETE);
        String yes = (String) dataTablesInput.getFormdata().get("eqs_validSign");
        if(CommonConstant.VALID_SIGN_YES.equals(yes)){
            sql.append(" and t.acc_dtl_id is not null ");
        }else if(CommonConstant.VALID_SIGN_NO.equals(yes)){
            sql.append(" and t.acc_dtl_id is null ");
        }
        //导入时间
        String gedImportDate = (String) dataTablesInput.getFormdata().get("ged_importDate");
        String ledImportDate = (String) dataTablesInput.getFormdata().get("led_importDate");
        if(StringUtils.isNotEmpty(gedImportDate)){
            gedImportDate = gedImportDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_importDate", gedImportDate);
        }
        if(StringUtils.isNotEmpty(ledImportDate)){
            ledImportDate = ledImportDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_importDate", ledImportDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisBankReceiptImportVO.class, "t", false);
    }


    public BisBankReceiptImportVO getById(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and t.id=:id");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisBankReceiptImportVO.class);
    }


    public int updateReceiptIdImport(String id,String bankReceiptDtlId,String dtlId) {
        String sql = " update BIS_BANK_RECEIPT_IMPORT set RECEIPT_ID = :bankReceiptDtlId,acc_dtl_id=:dtlId,blend_sign='1' " +
                " where id = :id and RECEIPT_ID is null";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("dtlId", dtlId);
        map.put("bankReceiptDtlId",bankReceiptDtlId);
        return createSQLQuery(sql, map).executeUpdate();
    }

    public int updateReceiptIdImportBatch(List<String> ids,String bankReceiptDtlId,String dtlId) {
        String sql = " update BIS_BANK_RECEIPT_IMPORT set RECEIPT_ID = :bankReceiptDtlId,acc_dtl_id=:dtlId,blend_sign='1' " +
                " where id in( :ids ) and RECEIPT_ID is null";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        map.put("dtlId", dtlId);
        map.put("bankReceiptDtlId",bankReceiptDtlId);
        return createSQLQuery(sql, map).executeUpdate();
    }

//    public int updateBlendSignForImport(String id,String receiptBlendSign) {
//        String sql = " update BIS_BANK_RECEIPT_IMPORT set BLEND_SIGN = :receiptBlendSign " +
//                " where id = :id and BLEND_SIGN<>:receiptBlendSign";
//        Map<String, Object> map = new HashMap<>();
//        map.put("id", id);
//        map.put("receiptBlendSign", receiptBlendSign);
//        return createSQLQuery(sql, map).executeUpdate();
//    }

    public int cancelBlendForImport(String receiptId) {
        String sql = " update BIS_BANK_RECEIPT_IMPORT set BLEND_SIGN = '0',receipt_id = null,acc_dtl_id=null " +
                " where receipt_id = :receiptId and BLEND_SIGN = '1' ";
        Map<String, Object> map = new HashMap<>();
        map.put("receiptId", receiptId);
        return createSQLQuery(sql, map).executeUpdate();
    }

    public int saveBisReceiptDtl(String dtlId, String receiptId, String rulesName) {
        String sql = " insert into BIS_RECEIPT_DTL(DTL_ID,RECEIPT_ID,RULES_NAME,PAYMENTBLENDING_DATE) values (?,?,?,?)";
        Date PAYMENTBLENDING_DATE = DateUtils.getDate();
        SQLQuery sqlQuery = createSQLQuery(sql, dtlId, receiptId, rulesName,PAYMENTBLENDING_DATE);
        return sqlQuery.executeUpdate();
    }

    public int updateBlendSignForDtl(String id, String receiptBlendSign) {
        String sql = " update bis_acc_dtl set RECEIPT_BLEND_SIGN = :receiptBlendSign where id = :id and (RECEIPT_BLEND_SIGN<>:receiptBlendSign or RECEIPT_BLEND_SIGN is null) ";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("receiptBlendSign", receiptBlendSign);
        return createSQLQuery(sql, map).executeUpdate();
    }

    public int updateBlendSign(String id, String blendSign) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" update BIS_BANK_RECEIPT_DTL set BLEND_SIGN = :blendSign where id = :id ");
        params.put("id", id);
        params.put("blendSign", blendSign);
        return createSQLQuery(sql.toString(), params).executeUpdate();
    }

    public List<String> getReceiptId(String dtlId) {
        String sql = " select  RECEIPT_ID from BIS_RECEIPT_DTL where DTL_ID =:dtlId  ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return createSQLQuery(sql, params).list();
    }

    public BisBankReceiptImportDTO getByReceiptIdAndDtlId(String receiptId, String dtlId) {
        String hql = " from BisBankReceiptImport t where t.receiptId=:receiptId and t.accDtlId=:dtlId";
        HashMap<String, String> params = new HashMap<>();
        params.put("receiptId", receiptId);
        params.put("dtlId", dtlId);
        List<BisBankReceiptImportDTO> list = this.listByHQLAliasToBean(hql, params, BisBankReceiptImportDTO.class);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 查询所有未勾兑的离线回单
     * @param blendSign
     * @return
     */
    public List<BisBankReceiptImportVO> queryBisBankReceiptImportBySign(String blendSign) {
        StringBuilder sql=new StringBuilder(getCommonSql());
        sql.append(" and t.blend_sign =:blendSign and t.status=95 ");


//        String hql = " from BisBankReceiptImport t where t.blendSign=:blendSign and status=95";
        if ("0".equals(blendSign)){
//            hql+=" or t.blendSign is null";
            sql.append(" or t.blend_sign is null ");
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("blendSign", blendSign);
        List<BisBankReceiptImportVO> list = this.listBySQLAliasToBean(sql.toString(), params, BisBankReceiptImportVO.class);
        return list;
    }
}
