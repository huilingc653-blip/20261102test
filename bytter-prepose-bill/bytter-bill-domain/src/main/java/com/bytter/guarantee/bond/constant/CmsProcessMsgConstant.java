package com.bytter.guarantee.bond.constant;
import java.io.Serializable;

/**
 * 信贷操作信息类型
 */
public class CmsProcessMsgConstant implements Serializable {
    /** 提交 */
    public final static int MSGA_SUBMIT = 20;
    /** 录入暂存 */
    public final static int MSGA_INPUT = 21;
    /** 修改暂存 */
    public final static int MSGA_MODIFY = 22;
    /** 删除 */
    public final static int MSGA_DELETE = 23;
    /** 审批通过 */
    public final static int MSGA_CHECKOK = 24;
    /** 审批不通过,打回 */
    public final static int MSGA_ROLLBACK = 25;
    /** 数据补录 */
    public final static int MSGA_REPAIR = 26;
    /** 记账打回 */
    public final static int MSGA_TALLYBACK = 27;
    /** 改账打回 */
    public final static int MSGA_EDITTALLYBACK = 28;
    /** 记账复核打回 */
    public final static int MSGA_TALLYCHECKBACK = 29;
    /** 数据补录打回 */
    public final static int MSGA_REPAIRBACK = 31;
    /** 修改提交 */
    public final static int MSGA_REPAIR_MODIFY = 32;
    /** 已领取 */
    public final static int MSGA_LETTER_OF_CONFIRMATION = 33;
    /** 数据处理打回 */
    public final static int MSGA_HANDLEBACK = 34;
    /** 禁用 */
    public final static int MSGA_FORBIDDEN = 35;
    /** 利息确认通过 */
    public final static int MSGA_INTERETAFFIRM = 36;
    /** 记账通过 */
    public final static int MSGA_TALLYTHROUGH = 42;
    /** 改账通过 */
    public final static int MSGA_TALLYEDITTHROUGH = 43;
    /** 复核通过 */
    public final static int MSGA_TALLYPASS = 44;
    /** 记账提交 */
    public final static int MSGA_TALLYSUBMIT = 45;
    /** 改账提交 */
    public final static int MSGA_TALLYEDITSUBMIT = 46;
    /** 记账暂存 */
    public final static int MSGA_TALLYMODIFY = 47;
    /** 补录审批打回 */
    public final static String MSGA_REPAIR_CHECKBACK = "48";
    /** 录入提交 */
    public final static String MSGA_INPUT_SUBMIT = "49";
    /** 补录提交 */
    public final static String MSGA_REPAIR_SUBMIT = "50";
    /** 补录暂存 */
    public final static String MSGA_REPAIR_TEMP = "51";
    /** 补录打回 */
    public final static String MSGA_REPAIR_BACK = "52";
    /** 补录审批通过 */
    public final static String MSGA_REPAIR_CHECKOK = "53";
    /** 发放提交 */
    public final static String MSGA_SEND_SUBMIT = "54";
    /** 发放暂存 */
    public final static String MSGA_SEND_TEMP = "55";
    /** 跳过发放 */
    public final static String MSGA_SEND_JUMP = "56";
    /** 发放打回 */
    public final static String MSGA_SEND_BACK = "57";
    /** 发放审批通过 */
    public final static String MSGA_SEND_CHECKOK = "58";
    /** 发放审批打回 */
    public final static String MSGA_SEND_CHECKBACK = "59";
    /** 还款补录打回 */
    public final static String MSGA_REPAY_REPAIR_BACK = "60";
    /** 还款补录提交 */
    public final static String MSGA_REPAY_REPAIR_SUBMIT = "61";
    /** 还款补录暂存 */
    public final static String MSGA_REPAY_REPAIR_TEMP = "62";
    /** 还款删除 */
    public final static String MSGA_REPAY_DELETE = "63";
    /** 还款审批通过 */
    public final static String MSGA_REPAY_CHECKOK = "64";
    /** 还款审批打回 */
    public final static String MSGA_REPAY_CHECKBACK = "65";
    /** 还款补录审批通过 */
    public final static String MSGA_REPAY_REPAIR_CHECKOK = "66";
    /** 还款补录审批打回 */
    public final static String MSGA_REPAY_REPAIR_CHECKBACK = "67";
    /** 还款修改提交 */
    public final static String MSGA_REPAY_UPDATE_SUBMIT = "68";
    /** 还款修改暂存 */
    public final static String MSGA_REPAY_UPDATE_TEMP = "69";
    /** 还款提交 */
    public final static String MSGA_REPAY_SUBMIT = "70";
    /** 还款暂存 */
    public final static String MSGA_REPAY_TEMP = "71";
    /** 审批打回 */
    public final static String MSGA_CHECKBACK = "72";
    /** 注销 */
    public final static String MSGA_LOGOUT = "73";
    /** 变更 */
    public final static String MSGA_CHANGE = "74";
    /** 信息补录 */
    public final static String MSGA_REPAIROP = "75";
    /** 新增审批通过 */
    public final static String MSGA_INPUT_CHECKOK = "76";
    /** 新增审批打回 */
    public final static String MSGA_INPUT_CHECKBACK = "77";
    /** 释放授信额度 */
    public final static String MSGA_RELEASE_RAT = "78";
    /** 释放保证金 */
    public final static String MSGA_RELEASE_BOND = "79";
    /** 发放修改暂存 */
    public final static String MSGA_SEND_UPDATE_TEMP = "80";
    /** 还款提交审批 */
    public final static String MSGA_REPAY_SUBMIT_CHECK = "81";
    /** 还款修改提交审批 */
    public final static String MSGA_REPAY_UPDATE_CHECK = "82";
    /** 还款申请暂存 */
    public final static String MSGA_REPAY_APPLY_TEMP = "83";
    /** 结算中心利息确认通过 */
    public final static String MSGA_ISTSURE_PASS = "84";
    /** 利息确认打回 */
    public final static String MSGA_ISTSURE_BACK = "85";
    /** 还款申请删除 */
    public final static String MSGA_REPAY_APPLY_DEL = "86";
    /** 展期申请成功 */
    public final static String MSGA_DEFERRED_APPLY_SUCCESS = "87";
    /** 展期补录审批打回 */
    public final static String MSGA_DEF_REPAIR_CHECKBACK = "88";
    /** 展期补录审批通过 */
    public final static String MSGA_DEF_REPAIR_CHECKOK = "89";
    /** 展期申请提交 */
    public final static String MSGA_DEF_APPLY_SUBMIT = "90";
    /** 展期申请暂存 */
    public final static String MSGA_DEF_APPLY_TEMP = "91";
    /** 展期申请补录 */
    public final static String MSGA_DEF_APPLY_REPAIR = "92";
    /** 展期申请修改提交 */
    public final static String MSGA_DEF_UPDATE_SUBMIT = "93";
    /** 展期申请修改暂存 */
    public final static String MSGA_DEF_UPDATE_TEMP = "94";
    /** 展期申请打回 */
    public final static String MSGA_DEF_APPLY_BACK = "95";
    /** 展期审批通过 */
    public final static String MSGA_DEF_CHECKOK = "96";
    /** 展期审批打回 */
    public final static String MSGA_DEF_CHECKBACK = "97";
    /** 展期处理打回 */
    public final static String MSGA_DEF_HANDLE_BACK = "98";
    /** 发放记账提交 */
    public final static String MSGA_SEND_TALLY_SUBMIT = "99";
    /** 发放记账暂存 */
    public final static String MSGA_SEND_TALLY_TEMP = "100";
    /** 发放记账打回 */
    public final static String MSGA_SEND_TALLY_BACK = "101";
    /** 发放记账复核通过 */
    public final static String MSGA_SEND_TALLY_CHECKOK = "102";
    /** 发放记账复核打回 */
    public final static String MSGA_SEND_TALLY_CHECKBACK = "103";
    /** 还款记账提交 */
    public final static String MSGA_REPAY_TALLY_SUBMIT = "104";
    /** 还款记账暂存 */
    public final static String MSGA_REPAY_TALLY_TEMP = "105";
    /** 还款记账打回 */
    public final static String MSGA_REPAY_TALLY_BACK = "106";
    /** 还款记账复核通过 */
    public final static String MSGA_REPAY_TALLY_CHECKOK = "107";
    /** 还款记账复核打回 */
    public final static String MSGA_REPAY_TALLY_CHECKBACK = "108";
    /** 发放改账打回 */
    public final static String MSGA_SEND_EDITTALLY_BACK = "109";
    /** 还款改账打回 */
    public final static String MSGA_REPAY_EDITTALLY_BACK = "110";
    /** 委托贷款发放记账暂存 */
    public final static String MSGA_PRESEND_TALLY_TEMP = "111";
    /** 委托贷款发放记账提交 */
    public final static String MSGA_PRESEND_TALLY_SUBMIT = "112";
    /** 委托贷款发放改账提交 */
    public final static String MSGA_PRESEND_EDITTALLY_SUBMIT = "113";
    /** 委托贷款发放改账打回 */
    public final static String MSGA_PRESEND_EDITTALLYBACK = "114";
    /** 委托贷款发放复核打回 */
    public final static String MSGA_PRESEND_CHECKBACK = "115";
    /** 委托贷款发放复核通过 */
    public final static String MSGA_PRESEND_CHECKOK = "116";
    /** 委托贷款归还记账暂存 */
    public final static String MSGA_PREREPAY_TALLY_TEMP = "117";
    /** 委托贷款归还记账提交 */
    public final static String MSGA_PREREPAY_TALLY_SUBMIT = "118";
    /** 委托贷款归还改账提交 */
    public final static String MSGA_PREREPAY_EDITTALLY_SUBMIT = "119";
    /** 委托贷款归还改账打回 */
    public final static String MSGA_PREREPAY_EDITTALLY_BACK = "120";
    /** 委托贷款归还复核打回 */
    public final static String MSGA_PREREPAY_CHECKBACK = "121";
    /** 委托贷款归还复核通过 */
    public final static String MSGA_PREREPAY_CHECKOK = "122";
    /** 借款展期删除 */
    public final static String MSGA_PROVIDE_DEF_DELETE = "123";
    /** 借款展期提交 */
    public final static String MSGA_PROVIDE_DEF_SUBMIT = "124";
    /** 借款展期审批通过 */
    public final static String MSGA_PROVIDE_DEF_CHECKOK = "125";
    /** 借款展期审批打回 */
    public final static String MSGA_PROVIDE_DEF_CHECKBACK = "126";
    /** 展期补录提交 */
    public final static String MSGA_DEF_REPAIR_SUBMIT = "127";
    /** 展期补录打回 */
    public final static String MSGA_DEF_REPAIR_BACK = "128";
    /** 展期处理 */
    public final static String MSGA_DEF_HANDLE = "129";
    /** 展期修改提交 */
    public final static String MSGA_DEF_MODIFY_SUBMIT = "130";
    /** 展期修改暂存 */
    public final static String MSGA_DEF_MODIFY_TEMP = "131";
    /** 展期处理成功 */
    public final static String MSGA_DEF_HANDLE_SUCCESS = "132";
    // /**展期申请审批通过*/
    // public final static String MSGA_DEF_APPLY_CHECKOK= "133";
    // /**展期申请审批打回*/
    // public final static String MSGA_DEF_APPLY_CHECKBACK= "134";
    /** 展期申请删除 */
    public final static String MSGA_DEF_APPLY_DELETE = "135";
    /** 作废提交 */
    public final static String MSGA_ABOLISH_SUBMIT = "136";
    /** 作废暂存 */
    public final static String MSGA_ABOLISH_TEMP = "137";
    /** 作废审批通过 */
    public final static String MSGA_ABOLISH_CHECKOK = "138";
    /** 作废审批打回 */
    public final static String MSGA_ABOLISH_CHECKBACK = "139";
    /** 作废办理 */
    public final static String MSGA_ABOLISH_HANDLE = "140";
    /** 作废撤销 */
    public final static String MSGA_ABOLISH_REPEAL = "141";
    /** 合同生成 */
    public final static String MSGA_CREATE_CONTRACT = "142";
    /** 删除打回 */
    public final static String MSGA_DELETE_BACK = "147";
    /** 债券注册审批通过 */
    public final static int MSGA_BOND_CHECKOK = 148;
    /** 债券注册审批打回 */
    public final static int MSGA_BOND_CHECKBACK = 149;
    /** 可转债新增提交 */
    public final static int MSGA_BOND_TRANSFER_ADD_SUBMIT = 150;
    /** 可转债修改提交 */
    public final static int MSGA_BOND_TRANSFER_UPDATE_SUBMIT = 151;
    /** 可转债审批通过 */
    public final static int MSGA_BOND_TRANSFER_CHECKOK = 152;
    /** 可转债审批打回 */
    public final static int MSGA_BOND_TRANSFER_CHECKBACK = 153;
    /** 可转债删除 */
    public final static int MSGA_BOND_TRANSFER_DEL = 154;
    /** 债券发行审批通过 */
    public final static int MSGA_BOND_ISSUE_CHECKOK = 155;
    /** 债券发行审批打回 */
    public final static int MSGA_BOND_ISSUE_CHECKBACK = 156;
    /** 资产证券化登记审批通过 */
    public final static int MSGA_SECURITY_CHECKOK = 157;
    /*** 资产证券化登记审批打回 */
    public final static int MSGA_SECURITY_CHECKBACK = 158;
    /** 资产证券化发行审批通过 */
    public final static int MSGA_SECURITY_ISSUE_CHECKOK = 159;
    /*** 资产证券化发行审批打回 */
    public final static int MSGA_SECURITY_ISSUE_CHECKBACK = 160;
}
