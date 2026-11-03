package com.bytter.biz.payment.api.constant;

/**
 * 资金需求常量类
 */
public class NisDemandConstant {
    /*** 资金需求状态：0-暂存  */
    public final static Integer STATUE_TEMP = 0;
    /*** 资金需求状态：1-打回  */
    public final static Integer STATUE_BACK = -1;
    /*** 资金需求状态：-2-做废  */
    public final static Integer STATUE_DEL = -2;
    /*** 资金需求状态：1-已确认  */
    public final static Integer STATUE_CHECK = 1;
    /*** 资金需求状态：2-下拨中  */
    public final static Integer STATUE_WAIT_ALLOCATION = 2;
    /*** 资金需求状态：3-已下拨  */
    public final static Integer STATUE_ALLOCATION = 3;
    /*** 资金需求状态：4-下拨失败  */
    public final static Integer STATUE_ALLOCATION_FAIL = 4;
    /*** 资金需求状态：5-下拨状态未确认  */
    public final static Integer STATUE_ALLOCATION_NOT_CHECK = 5;


    /*** 资金需求类型  01-付款*/
    public final static String DEMANDTYPE_01 = "01";
    /*** 资金需求类型  02-承兑到期*/
    public final static String DEMANDTYPE_02 = "02";
    /*** 资金需求类型  03-融资到期*/
    public final static String DEMANDTYPE_03 = "03";
    /*** 资金需求类型  04-工资配置表*/
    public final static String DEMANDTYPE_04 = "04";
    /*** 资金需求类型  05-工资单据表*/
    public final static String DEMANDTYPE_05 = "05";

    /*** 头寸调拨指令状态：*/
    public final static String WAIT_RESULT = "1";//等待结果
    public final static String SUCCESS = "2";//交易成功
    public final static String PART_SUCCESS = "3";//部分成功：存在失败调拨指令
    public final static String FAILED = "4";//交易失败



}
