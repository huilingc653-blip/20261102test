package com.bytter.erp.constant;
/**
 * 全局公用常量
 * Created by zhoul on 2017/5/14.
 */
public class CommonConstant {
    /**
     * 英文状态下逗号作为分隔符
     */
    public static final String COMMA_SPLIT = ",";
    /**
     * 英文状态下的点作为分隔符
     */
    public static final String DOT_SPLIT = "\\.";
    /**
     * 英文状态下分号作为分隔符
     */
    public static final String SEMI_SPLIT = ";";
    /**
     * 英文状态下冒号作为分隔符
     */
    public static final String COLON_SPLIT = ":";
    /**
     * 在request上的session名
     */
    public static final String REQUEST_BODY_JSON_ATTR = "REQUEST_BODY_JSON_ATTR";
    public static final String CHECKBOX_ON = "on";
    public static final String CHECKBOX_OFF = "off";
    public static final Integer STATUE_DELETE = -2;//"删除"
    public static final Integer STATUE_BACK = -1;//("打回", -1);
    public static final Integer STATUE_TEMP = 0;//("暂存", 0);
    public static final Integer STATUE_WAIT_CHECK = 11;//("待审批", 11);
    public static final Integer STATUE_CHECK = 95;//("审批通过", 95);
    public static final Integer STATUE_DRAW_CHECK = 100;//("记账复核通过", 100);
    public static final Integer STATUE_ACCOUNTING_SUBMIT = 97;//("记账提交", 97);
    /**
     * 操作类型
     */
    public static final String SUBMIT_SEARCH = "S"; //查询
    public static final String SUBMIT_ADD = "A"; //新增
    public static final String SUBMIT_TEMP = "T"; //暂存
    public static final String SUBMIT_MODIFY = "M";//修改
    public static final String SUBMIT_CHECK = "C"; //复核
    public static final String SUBMIT_VIEW = "V"; //查看
    public static final String SUBMIT_DELETE = "D"; //删除
    public static final String SUBMIT_CANCEL = "S";//取消
    public static final String SUBMIT_IMPORT = "I"; //导入
    public static final String SEND_SIGN_YES = "1";//已提醒/发送
    public static final String SEND_SIGN_NO = "0";//未提醒/发送
    /**
     * 是否有效
     */
    public static final String VALID_SIGN_YES = "1";
    /**
     * 是否有效
     */
    public static final String VALID_SIGN_NO = "0";
    /**
     * 管理单元标识-已删除
     */
    public static final String VALID_SIGN_DELETE = "-2";
    /**
     * 是否：0-否
     */
    public final static String NO = "0";
    /**
     * 是否：1-是
     */
    public final static String YES = "1";
    /**
     * 100
     */
    public final static int HUNDRED = 100;
    /**
     * 50
     */
    public final static int FIFTY = 50;
    /**
     * 10
     */
    public final static int TEN = 10;
}
