package com.bytter.biz.finacial.interest.utils;
import com.bytter.biz.finacial.interest.service.proccessor.IInterestProvider;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import org.springframework.util.Assert;
import java.util.*;
public class InterestHelper {
	
	private final static Map<String, String> ACCTYPE_INTEREST_PROVIDER_MAPPING = new HashMap<String, String>();
    static {
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT, "Current");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEMAND, "BankCurrent");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_LOAN, "Loan");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BORROW, "BankLoan");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BUSINESS_BEGULAR, "Fixed");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BANK_BEGULAR, "BankFixed");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_INNER_ACCOUNT, "InnerAcc");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_BANK_DEPOSIT, "Notify");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_LOANBYMANDATE, "Entrust");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_ENTRUSTED_DEPOSIT, "");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_OFFBALANCE, "OutFinacial");
        ACCTYPE_INTEREST_PROVIDER_MAPPING.put(InnerAccountConstant.FCACC_AGREEMENT, "Agreed");
    }
    
	public static String[] getAccTypes(String accType, AccTypeAllFilterType filterType) {
        if (StringUtils.isBlank(accType)) {
            if (filterType == AccTypeAllFilterType.BATCH) {
                //批量计息的开户种类
                return getAccTypeConstants(
                		InnerAccountConstant.FCACC_BUSINESS_ACCOUNT,
                		InnerAccountConstant.FCACC_BANK_DEMAND,
                		InnerAccountConstant.FCACC_LOAN,
                		InnerAccountConstant.FCACC_BANK_BORROW,
                		InnerAccountConstant.FCACC_BUSINESS_BEGULAR,
                		//InnerAccountConstant.FCACC_BANK_BEGULAR,
                		//InnerAccountConstant.FCACC_INNER_ACCOUNT,
                		InnerAccountConstant.FCACC_BANK_DEPOSIT,
                		InnerAccountConstant.FCACC_LOANBYMANDATE
                		//,InnerAccountConstant.FCACC_OFFBALANCE
                        ,InnerAccountConstant.FCACC_AGREEMENT
                		);
            } else if (filterType == AccTypeAllFilterType.CALCULATE) {
                //利息测算的开户种类全部包含银行活期、银行定期、银行借款
                return getAccTypeConstants(
                		InnerAccountConstant.FCACC_BANK_DEMAND,
                		InnerAccountConstant.FCACC_BANK_BORROW,
                		InnerAccountConstant.FCACC_BANK_BEGULAR);
            }
        } else {
            return accType.split(",");
        }
        return getAccTypeConstants(accType);
    }
	
	
	/**
	 * 
	 * <b>功能介绍:</b>获取开户种类常量值<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public static String[] getAccTypeConstants(String... filters) {
		List<String> types = new ArrayList<String>();
		for (String str : filters) {
			types.add(str);
		}
		String[] accTypes = new String[types.size()];
		for (int i=0;i<types.size();i++) {
			accTypes[i] = types.get(i);
		}
        return accTypes;
    }
	
	
	/**
	 * <b>功能介绍:</b>是否季末<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public static boolean isSeasonEnd(Date endInstDate) {
        Calendar endInstCalendar = Calendar.getInstance();
        endInstCalendar.setTime(endInstDate);
        // 固定3/6/9/12为季末月份
        switch (endInstCalendar.get(Calendar.MONTH) + 1) {
        case 3:
        case 6:
        case 9:
        case 12:
            return true;
        default:
            return false;
        }
    }
	
	/**
	 * <b>功能介绍:</b>是否月末最后一天<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public static boolean isLastDayOfMonth(Date date){
        Date tempDate = date;
        Calendar cldr = Calendar.getInstance();
        cldr.setTime(tempDate);
        int firstMonth = cldr.get(Calendar.MONTH);
        
        cldr.add(Calendar.DAY_OF_MONTH, 1);
        int secMonth = cldr.get(Calendar.MONTH);
        if (firstMonth == secMonth) {
            return false;
        }
        return true;
    }
	
	/**
	 * <b>功能介绍 : </b>是否系统计息日<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public static boolean isSysInstDay(Date endInstDate,String batchInstDay) {
        Calendar endInstCalendar = Calendar.getInstance();
        endInstCalendar.setTime(endInstDate);
        return endInstCalendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(batchInstDay);
    }
	
	
	/**
	 * <b>功能介绍：</b>根据账户类型获取利息业务处理类型<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
    public static IInterestProvider getInterestProvider(String accTypeCode) {
        Assert.notNull(ACCTYPE_INTEREST_PROVIDER_MAPPING.get(accTypeCode), "未配置开户种类" + accTypeCode + "对应的利息适配器。");
        String interestProviderBeanName = "interest_" + ACCTYPE_INTEREST_PROVIDER_MAPPING.get(accTypeCode)
                + "InterestProvider";
        return (IInterestProvider) ApplicationContextUtil.getContext().getBean(interestProviderBeanName);
    }
	
    /**
     * <b>功能介绍：</b>是否月初第一天<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public static boolean isMonthFirstWorkDay(Date endInstDate) {
        Calendar lastWorkCalendar = Calendar.getInstance();
        lastWorkCalendar.setTime(endInstDate);
        int day = lastWorkCalendar.get(Calendar.DAY_OF_MONTH);
        if (day == 1) {
            return true;
        }
        return false;
    }
    
    /**
     * <br>
     * <b>功能：</b>是否一借多贷预提凭证<br>
     * <b>作者：</b>李又虎<br>
     * <b>日期：</b> 2012-7-9 <br>
     * @param accTypeCode
     * @return
     */
    public static boolean isMoreCreditAccBook(String accTypeCode) {
        //01 04 05 08  16
        if (accTypeCode.equals(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT) || accTypeCode.equals(InnerAccountConstant.FCACC_BANK_BORROW)
                || accTypeCode.equals(InnerAccountConstant.FCACC_BUSINESS_BEGULAR) || accTypeCode.equals(InnerAccountConstant.FCACC_BANK_DEPOSIT)
                || accTypeCode.equals(InnerAccountConstant.FCACC_BANK_DEMAND) || accTypeCode.equals(InnerAccountConstant.FCACC_LOAN)
                || accTypeCode.equals(InnerAccountConstant.FCACC_AGREEMENT)
                ) {
            return true;
        }
        return false;
    }
    
    /**
     * <b>功能介绍：</b>产生全局uuid<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public static String createGlobalId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**计算天数差*/
    public static long getDiffDays(Date startDate, Date endDate) {
        startDate = InterestHelper.cleanAndGet(startDate);
        endDate = InterestHelper.cleanAndGet(endDate);
        long milliSeconds = endDate.getTime() - startDate.getTime();
        return milliSeconds / 24 * 60 * 60 * 1000;
    }
    
    /**
     * 清除日期对象的时分秒值
     * @param date
     * @return
     */
    public static Date cleanAndGet(Date date) {
        Calendar cldr = Calendar.getInstance();
        cldr.setTime(date);
        cldr.set(Calendar.HOUR_OF_DAY, 0);
        cldr.set(Calendar.MINUTE, 0);
        cldr.set(Calendar.SECOND, 0);
        cldr.set(Calendar.MILLISECOND, 0);
        return cldr.getTime();
    }
}
