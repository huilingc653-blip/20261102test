package com.bytter.biz.finacial.api.constant;
import java.io.Serializable;
/**
 * 对账处理常量
 *
 * @author caozx
 * @since 2011-8-21
 */
public class CheckConstant implements Serializable {
    /**
     * 核对标准-按发生额核对
     */
    public final static String TYPE_MONEY = "1";
    /**
     * 核对标准-按发生额和票据号核对
     */
    public final static String TYPE_MONEY_BILLNO = "2";
    /**
     * 核对标准-按时间与发生额核对
     */
    public final static String TYPE_TIME_MONEY = "3";
    /**
     * 核对标准-按时间发生额票据号核对
     */
    public final static String TYPE_TIME_MONEY_BILLNO = "4";
    /**
     * 核对标准-按凭证号核对
     */
    public final static String TYPE_VOUCHERNO = "5";
    /**
     * 未达账类别-中心已付、银行未付
     */
    public final static String CTR_PAY_BANK_NO_PAY = "1";
    /**
     * 未达账类别-中心已收、银行未收
     */
    public final static String CTR_REC_BANK_NO_REC = "2";
    /**
     * 未达账类别-银行已付、中心未付
     */
    public final static String BANK_PAY_CTR_NO_PAY = "3";
    /**
     * 未达账类别-银行已收、中心未收
     */
    public final static String BANK_REC_CTR_NO_REC = "4";
    /**
     * 余额调节表明细数据类型-银行数据
     */
    public final static String BANK_DATA = "1";
    /**
     * 余额调节表明细数据类型-中心数据
     */
    public final static String CTR_DATA = "2";
    /**
     * 余额调节表明细数据类型-ERP数据
     */
    public final static String ERP_DATA = "3";
    /**
     * 操作类型  - 对账
     */
    public final static String OPTYPE_TYPE_CHECK = "1";
    /**
     * 操作类型  - 手工勾对
     */
    public final static String OPTYPE_TYPE_HAND_OVER = "2";
    /**
     * 操作类型  - 结转
     */
    public final static String OPTYPE_TYPE_OVER = "3";
    /**
     * 操作类型  - 本期未达
     */
    public final static String OPTYPE_TYPE_STAY = "4";
    /**
     * 操作类型  - 勾兑状态调整
     */
    public final static String OPTYPE_EDIT_HAND_OVER = "5";
    /**
     * 操作类型  - 反结转
     */
    public final static String OPTYPE_REDO_TYPE_OVER = "6";
    /**
     * 操作类型  - 反勾对
     */
    public final static String OPTYPE_TYPE_HAND_UN_OVER = "7";
    /**
     * 操作类型  - 未达还原
     */
    public final static String OPTYPE_TYPE_STAY_RECOVER = "8";
    /**
     * ERP对账单
     */
    public final static String LOGTYPE_TYPE_ERP = "0";
    /**
     * 银行对账单
     */
    public final static String LOGTYPE_TYPE_BANK = "1";
    /**
     * 中心对账单
     */
    public final static String LOGTYPE_TYPE_ACC = "2";
    /**
     * 核对标准-发生额
     */
    public final static String CHECK_TYPE_MONEY = "01";
    /**
     * 核对标准-时间
     */
    public final static String CHECK_TYPE_TIME = "02";
    /**
     * 核对标准-票据号
     */
    public final static String CHECK_TYPE_BILLNO = "03";
    /**
     * 核对标准-凭证号
     */
    public final static String CHECK_TYPE_VOUCHERNO = "04";
}
