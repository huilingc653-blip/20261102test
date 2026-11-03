package com.bytter.system.inneraccount.util;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.constants.InnerAccountConstant;
import java.util.Date;
/**
 * Created by zhangxb on 2017/6/7.
 */
public class InnerAccountUtils {
    /**
     * 得到每日余额表联合主键日期，根据账号启用日期减一天得到
     * @param startDate
     * @return
     */
    public static Date getBalanceDate(Date startDate){
        Date   balanceDate = DateUtils.addDay(startDate,-1);
        return balanceDate;
    }
    public static String getPaySignByAccAttribute(String accAttribute) {
        String paySign = null;
        if("01".equals(accAttribute)){
            paySign = "B";
        }else if("02".equals(accAttribute)){
            paySign = "R";
        }else if("03".equals(accAttribute)){
            paySign = "P";
        }else{
            paySign = "B";
        }
        return  paySign;
    }
    /**
     * 传入一个integer数组，取出最大值
     * @param array
     * @return
     */
    public static Integer getMax(Integer[] array) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
				max = array[i];
			}
        }
        return max;
    }
    /**
     * 过滤账户种类是否需要验证
     * acc_type 01 05 08 活期 定期  通知 可以不需要账户余额为零才可以注销
     * @param accType
     */
    public static boolean validAccType(String accType){
        String[] requireAccType= new String[]{
                InnerAccountConstant.FCACC_BANK_DEMAND,
                InnerAccountConstant.FCACC_LOAN,
                InnerAccountConstant.FCACC_BANK_BORROW,
                InnerAccountConstant.FCACC_BANK_BEGULAR,
                InnerAccountConstant.FCACC_INNER_ACCOUNT,
                InnerAccountConstant.FCACC_LOANBYMANDATE,
                InnerAccountConstant.FCACC_ENTRUSTED_DEPOSIT,
                InnerAccountConstant.FCACC_OFFBALANCE
        };
        boolean flag=false;
        for(String str: requireAccType){
            if(str.equals(accType)){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
