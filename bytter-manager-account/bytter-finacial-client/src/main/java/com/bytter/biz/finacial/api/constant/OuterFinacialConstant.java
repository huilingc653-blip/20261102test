package com.bytter.biz.finacial.api.constant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.VoucherStatus;
import com.bytter.core.constant.ConstantBean;
/**
 * 表外常量类
 * @author liyh
 * @since 2017-7-6
 */
public class OuterFinacialConstant  implements  java.io.Serializable {
	private static final long serialVersionUID = 1L;
    /**
     * 表外状态
     */
    public static class OuterStatus {
        /**记账暂存*/
        public final static ConstantBean<Integer> STATUE_TALLY_TEMP = new ConstantBean<Integer>("记账暂存", 10);
        /**记账打回*/
        public final static ConstantBean<Integer> STATUE_TALLY_BACK = new ConstantBean<Integer>("记账复核打回", 11);
        /**记账待复核*/
        public final static ConstantBean<Integer> STATUE_TALLY_WAIT_CHECK = new ConstantBean<Integer>("记账待复核", 12);
        /**记账复核*/
        public final static ConstantBean<Integer> STATUE_TALLY_CHECK = new ConstantBean<Integer>("记账复核", 13);
        /**作废暂存*/
        public final static ConstantBean<Integer> STATUE_INVALID_TEMP = new ConstantBean<Integer>("作废暂存", 20);
        /**作废打回*/
        public final static ConstantBean<Integer> STATUE_INVALID_BACK = new ConstantBean<Integer>("作废复核打回", 21);
        /**作废待复核*/
        public final static ConstantBean<Integer> STATUE_INVALID_WAIT_CHECK = new ConstantBean<Integer>("作废待复核", 22);
        /**冲账复核*/
        public final static ConstantBean<Integer> STATUE_INVALID_CHECK = new ConstantBean<Integer>("作废复核", 23);
        /**冲账暂存*/
        public final static ConstantBean<Integer> STATUE_RUSH_TEMP = new ConstantBean<Integer>("冲账暂存", 30);
        /**作废打回*/
        public final static ConstantBean<Integer> STATUE_RUSH_BACK = new ConstantBean<Integer>("冲账复核打回", 31);
        /**作废待复核*/
        public final static ConstantBean<Integer> STATUE_RUSH_WAIT_CHECK = new ConstantBean<Integer>("冲账待复核", 32);
        /**作废复核*/
        public final static ConstantBean<Integer> STATUE_RUSH_CHECK = new ConstantBean<Integer>("冲账复核", 33);
    }
    /**
     * 业务来源类型 
     */
    public static class OuterBusOriType {
        public final static ConstantBean<Integer> TALLY_HAND = new ConstantBean<Integer>("手工记账", 10);
        public final static ConstantBean<Integer> VOUCHER_MNG = new ConstantBean<Integer>("空白凭证", 20);
    }
    /**
     * 红蓝标志
     */
    public static class RedBlueSign {
        public final static ConstantBean<String> RED = new ConstantBean<String>("红", "R");
        public final static ConstantBean<String> BLUE = new ConstantBean<String>("蓝", "B");
    }
    
    public static final String JXLS_EXP_PATH = "/outerfinacial/jxlsTemplates";
    
    /**
     * 计量单位
     */
    public static class OuterMeasureUnit {
    	public final static ConstantBean<String> UNIT_MONEY = new ConstantBean<String>("金额", "M");
    	public final static ConstantBean<String> UNIT_NUMBER = new ConstantBean<String>("个数","N");
    	public final static ConstantBean<String> UNIT_TICKET = new ConstantBean<String>("张数", "T");
    }
    
    /**
     * 余额方向 
     */
    public static class OuterBalanceWay {
        public final static ConstantBean<Integer> BAL_DEBIT = new ConstantBean<Integer>("借", 1);
        public final static ConstantBean<Integer> BAL_CREDIT = new ConstantBean<Integer>("贷", -1);
        public final static ConstantBean<Integer> BAL_EQUAL = new ConstantBean<Integer>("平", 0);
    }
    /**
     *根据表外状态获取凭证状态
     * @param outStatus 表外状态
     * @return 凭证状态
     */
    public static String outInversionInnerStatus(Integer outStatus){
    	if(OuterStatus.STATUE_TALLY_BACK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_BACK.get();
    	}else if(OuterStatus.STATUE_TALLY_TEMP.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_TEMP.get();
    	}else if(OuterStatus.STATUE_TALLY_WAIT_CHECK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_WAIT_CHECK.get();
    	}else if(OuterStatus.STATUE_TALLY_CHECK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_CHECK.get();
    	}else if(OuterStatus.STATUE_INVALID_BACK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_INVALID_BACK.get();
    	}else if(OuterStatus.STATUE_INVALID_TEMP.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_INVALID_TEMP.get();
    	}else if(OuterStatus.STATUE_INVALID_WAIT_CHECK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_INVALID_WAIT_CHECK.get();
    	}else if(OuterStatus.STATUE_INVALID_CHECK.get().intValue() == outStatus.intValue()){
    		return VoucherStatus.STATUS_INVALID_CHECK.get();
    	}
    	return null;
    }
    
    /**
     * 结转标识-未结转
     */
    public static final String OVER_SIGN_N = "0";
    
    /**
     * 结转标识-已结转
     */
    public static final String OVER_SIGN_Y = "1";
}
