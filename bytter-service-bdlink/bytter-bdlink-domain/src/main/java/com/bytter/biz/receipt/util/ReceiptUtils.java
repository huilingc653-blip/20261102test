package com.bytter.biz.receipt.util;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.MD5Util;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.ReceiptConstant;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhangxb
 * @date 2017/7/4.
 */
public class ReceiptUtils {
    /**
     * 根据不同的类型反馈不同的页面
     * @param billType
     * @return
     */
    public static String getListBill(String billType){
        if (ReceiptConstant.RECEIPT_BILL_TYPE_14.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_15.equals(billType)){
            return "EDrawMoneyBill";
        }
        if (ReceiptConstant.RECEIPT_BILL_TYPE_04.equals(billType) || ReceiptConstant.RECEIPT_BILL_TYPE_05.equals(billType)){
            return "EDepositIstBill";
        }
        if(ReceiptConstant.RECEIPT_BILL_TYPE_ALL.equals(billType)){
            return "EAllBill";
        }
        return  replaceUnderlineAndfirstToUpper(ReceiptConstant.BILL_MAP.get(billType), "_", "");
    }
    public static String getSearchSqlCommon(String printSign){
        StringBuilder sql = new StringBuilder();
        if (ReceiptConstant.PRINT_STATUS_YES.equals(printSign)) {
            sql.append(" and B.PRINTS>1 ");
        } else if (ReceiptConstant.PRINT_STATUS_NO.equals(printSign)) {
            sql.append(" and B.PRINTS=1");
        }
        return sql.toString();
    }
    public static String[] getBillTableName(){
        String[] billTableName = new String[]{
                ReceiptConstant.E_ACCRUAL_BILL,
                ReceiptConstant.E_CONSIGN_BILL,
                ReceiptConstant.E_LOAN_BILL,
                ReceiptConstant.E_NOTICE_BILL,
                ReceiptConstant.E_POUNDAGE_BILL,
                ReceiptConstant.E_TRANS_BILL,
                ReceiptConstant.E_COMPARE_BILL,
                ReceiptConstant.E_ACCEPT_BILL,
                ReceiptConstant.E_EXCHANGE_BILL,
                ReceiptConstant.E_PRECATIVE_LOAN_BILL
        };
        return billTableName;
    }
    /**
     *  替换字符串并让它的下一个字母为大写
     * @param srcStr
     * @param org
     * @param ob
     * @return
     */
    public static String replaceUnderlineAndfirstToUpper(String srcStr,String org,String ob)
    {
        String [] src=srcStr.toLowerCase().split(org);
        StringBuilder sb=new StringBuilder();
        for(String s : src){
            sb.append(StringUtils.firstCharToUpperCase(s));
            sb.append(ob);
        }
        if(StringUtils.isNotEmpty(ob)){
            return sb.toString().substring(0,sb.toString().length()-1);
        }else{
            return sb.toString();
        }
    }
    
//    --随机数
//    v_random       := substr(v_bill_no, (length(v_bill_no) - 6), 6);
//    v_encry_source := v_debit_acc ||
//                      TRIM(to_char(v_loan_money, '999999999999.99')) ||
//                      to_char(SYSDATE, 'yyyymmdd') || v_random;
//    v_validcode    := v_encry_source;
    
    /**
     * 根据单号获取随机数
     * @param billNo 单号
     * @return
     */
    public static String getBillRandom(String billNo){
    	if(StringUtils.isEmpty(billNo)||billNo.length()<6){
    		return null;
    	}
    	String random=billNo.substring(billNo.length()-6);
    	return random;
    }
    /**
     * 获取电子回单验证码
     * @param debitAcc 借方账户
     * @param creditAcc 贷方账户
     * @param money 金额
     * @param voucherNo 凭证号
     * @param random 随机数
     * @return
     */
    public static String getBillValidcode(String debitAcc,String creditAcc,BigDecimal money,String voucherNo,String random){
    	if(StringUtils.isEmpty(debitAcc)|| StringUtils.isEmpty(random)||money==null){
    		return null;
    	}
    	StringBuilder validcode=new StringBuilder(debitAcc);
    	if(StringUtils.isNotEmpty(creditAcc)){
    		validcode.append(creditAcc);
    	}
    	validcode.append(NumberUtil.formatDouble(money.doubleValue()))
    	.append(DateUtils.formatDate("yyyyMMdd"));
        if(StringUtils.isNotEmpty(voucherNo)){
            validcode.append(voucherNo);
        }
        validcode.append(random);
    	return validcode.toString();
    }
    
    /**
     * 获取验证码以后的加密码
     * @param validcode 验证码
     * @return
     */
    public static String getBillEncrySource(String validcode){
    	if(StringUtils.isEmpty(validcode)){
    		return null;
    	}
    	return MD5Util.MD5Encode(validcode);
    }
    /**
     * 根据时间判断是否是季末
     * @param date
     * @return
     */
    public static boolean getSeason(Date date){
    	if(date==null){
    		return false;
    	}
    	int month=Integer.parseInt(DateUtils.formatDate(date, "MM"));
    	int balance=month%3;
    	return balance==0?true:false;
    }
}
