package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.ErpBankReceiptImgView;
import com.bytter.biz.erp.entity.ErpBankReceiptView;
import com.bytter.biz.erp.vo.request.BankReceiptQueryReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author fangcf
 * @date 2026-05-12
 */
@Repository
public class ErpBankReceiptViewDao extends BaseDao {

    /**
     * 根据条件查询电子回单
     *
     * @param reqVo 过滤条件vo
     * @return List<ErpBankReceiptView> 电子回单
     */
    public List<ErpBankReceiptView> listBankReceipt(BankReceiptQueryReqVo reqVo) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuilder sql = new StringBuilder();
        sql.append(" select v.SERIAL_ID as serialId, v.TENANT_ID as tenantId, ");
        sql.append(" v.PAYER_ACC_NO as payerAccNo, v.PAYER_ACC_NAME as payerAccName, v.PAYER_BANK_NAME as payerBankName, ");
        sql.append(" v.PAYEE_ACC_NO as payeeAccNo, v.PAYEE_ACC_NAME as payeeAccName, v.PAYEE_BANK_NAME as payeeBankName, ");
        sql.append(" v.AMT as amt, v.CUR_CODE as curCode, v.RECEIPT_NO as receiptNo, v.TRANS_SERIAL_NO as transSerialNo, ");
        sql.append(" v.CD_SIGN as cdSign, v.CHARGE_DATE as chargeDate ");
        sql.append(" from ERP_BANK_RECEIPT_VIEW v ");
        sql.append(" where v.TENANT_ID = :tenantId and v.PAYER_ACC_NO = :bankAcc ");
        if (StringUtils.isNotEmpty(reqVo.getCdSign())) {
            sql.append(" and v.CD_SIGN = :cdSign ");
            params.put("cdSign", reqVo.getCdSign());
        }
        sql.append(" and v.CHARGE_DATE >= bt_to_date(:beginDate, 'yyyy-MM-dd') and v.CHARGE_DATE < bt_to_date(:endDate, 'yyyy-MM-dd') ");
        sql.append(" order by v.CHARGE_DATE ");

        params.put("tenantId", reqVo.getTenantId());
        params.put("bankAcc", reqVo.getBankAcc());
        params.put("beginDate", reqVo.getBeginDate());
        // 结束日期要+1天
        String endDate = DateUtils.formatDate(DateUtils.addDay(DateUtils.parseDate(reqVo.getEndDate()), 1));
        params.put("endDate", endDate);

        return this.listBySQLAliasToBean(sql.toString(), params, ErpBankReceiptView.class);
    }

    /**
     * 根据条件查询电子回单信息
     *
     * @param tenantId 租户id
     * @param serialId 主键id
     * @return List<ErpBankReceiptImgView> 电子回单
     */
    public List<ErpBankReceiptImgView> listBankReceiptImgView(String tenantId, String serialId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select v.SERIAL_ID as serialId, v.TENANT_ID as tenantId, ");
        sql.append(" v.SAVE_TYPE as saveType, v.FILE_TYPE as fileType, v.FILE_PATH as filePath, ");
        sql.append(" v.EBILLIMG as ebillimg, v.PDF_TO_IMG as pdfToImg ");
        sql.append(" from ERP_BANK_RECEIPT_IMG_VIEW v ");
        sql.append(" where v.TENANT_ID = :tenantId and v.SERIAL_ID = :serialId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("tenantId", tenantId);
        params.put("serialId", serialId);

        return this.listBySQLAliasToBean(sql.toString(), params, ErpBankReceiptImgView.class);
    }
}
