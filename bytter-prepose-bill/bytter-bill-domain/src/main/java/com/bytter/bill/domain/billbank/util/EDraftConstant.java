package com.bytter.bill.domain.billbank.util;
import com.bytter.bill.api.bill.constant.AccebillConstant;
/**
 * 电票系统使用到的所有公共常量定义，依次为： 1、业务状态 2、操作状态 3、业务类型 4、科目 5、票据状态(人行定义) 6、报文编码(人行定义)
 * 7、线上清算标记(人行定义) 8、可再转让标记(人行定义) 9、贴现类型(人行定义) 10、签收标志(人行定义) 11、票据类型(人行定义)
 * 12、返回处理码(人行定义) 13、渠道编号 14、票据类型(本系统内) 15、账号来源 16、授权账号模式 17、其他常量
 * 
 * @author yangmy
 * 
 */
public class EDraftConstant {
	public enum METHOD {
		SUBMIT, AGREE, REFUSE, CANCEL, TOSIGN
	}
    /**
	 * @Fields BOX_STATUS_10 : 业务处理中
	 */
	public static final int BOX_STATUS_10 = 10;
	/**
	 * @Fields BOX_STATUS_11 : 业务处理完成
	 */
	public static final int BOX_STATUS_11 = 11;
	/**
	 * 暂存 10 业务申请暂存
	 */
	public static final int OPERATE_STATUS_TEMPSAVE_10 = 10;
	/**
	 * 待单位审批 11 单位提交审批
	 */
	public static final int OPERATE_STATUS_PRECORPAPPROVE_11 = 11;
	/**
	 * 单位审批中 12 单位审批业务
	 */
	public static final int OPERATE_STATUS_CORPAPPROVING_12 = 12;
	/**
	 * 待财务公司确认 13 单位审批完成
	 */
	public static final int OPERATE_STATUS_PREFCCONFRIM_13 = 13;
	/**
	 * 待财务公司审批 14
	 */
	public static final int OPERATE_STATUS_PREFCAPPROVE_14 = 14;
	/**
	 * 财务公司审批中 15
	 */
	public static final int OPERATE_STATUS_FCAPPROVING_15 = 15;
	/**
	 * 财务公司审批完成，待发送
	 */
	public static final int OPERATE_STATUS_PRESEND = 16;
	/**
	 * 已经发送待回应 17
	 */
	public static final int OPERATE_STATUS_SENDED_17 = 17;
	/**
	 * 已回应待签收 18
	 */
	public static final int OPERATE_STATUS_SENDCONFIRM_18 = 18;
	/**
	 * 已经签收成功，业务完成 19
	 */
	public static final int OPERATE_STATUS_BIZFINISH_19 = 19;
	/**
	 * 待提示承兑 30
	 */
	public static final int OPERATE_STATUS_ACCEPTING_30 = 30;
	/**
	 * 待提示收票 31
	 */
	public static final int OPERATE_STATUS_ISSUANCE_31 = 31;
	
	/**
	 * 发送中 0
	 * 
	 * Description：</b>增加票据的状态
	 * Author：</b>李斌<br>
	 * Date：</b> 2014-10-15
	 */
	public static final int OPERATE_STATUS_SENDING = 0;
	/**
	 * 失败 －1 通信失败
	 */
	public static final int OPERATE_STATUS_FAILED = -1;
	/**
	 * 打回 －2 审批打回或确认打回
	 */
	public static final int OPERATE_STATUS_ROLLBACK = -2;
	/**
	 * 已撤回 －3 对业务的撤回状态
	 */
	public static final int OPERATE_STATUS_WITHDRAW = -3;
	/**
	 * 拒收 －4 对方回复拒收或者是通用确认失败
	 */
	public static final int OPERATE_STATUS_REFUSE = -4;
	/**
	 * 失败（交行特有状态） -5 通信失败
	 */
	public static final int OPERATE_STATUS_COMMFAILED = -5;
	/**
	 * 已接收到的报文信息，待签收处理
	 */
	public static final int OPERATE_STATUS_PREREPLY = 10;
	/**
	 * 已接收到的报文信息，已回复
	 */
	public static final int OPERATE_STATUS_REPLYED = 11;
	// 业务类型常量类(以1008开头的是我方发起的申请类业务，以10089开头的是对方发起，由我方签收的业务)
	/**
	 * 开票业务(1008001)
	 */
	public final static String BUSINESS_001 = "1008001";
	/**
	 * 提示承兑业务(1008002)
	 */
	public final static String BUSINESS_002 = "1008002";
	/**
	 * 提示收票业务(1008003)
	 */
	public final static String BUSINESS_003 = "1008003";
	/**
	 * 提示付款业务(1008020)
	 */
	public final static String BUSINESS_020 = "1008020";
	/**
	 * 追索通知(1008022)
	 */
	public final static String BUSINESS_022 = "1008022";
	/**
	 * 撤票(1008004)
	 */
	public final static String BUSINESS_004 = "1008004";
	/**
	 * 背书(1008010)
	 */
	public final static String BUSINESS_010 = "1008010";
	/**
	 * 贴现(1008011)
	 */
	public final static String BUSINESS_011 = "1008011";
	/**
	 * 回购式贴现赎回(1008012)
	 */
	public final static String BUSINESS_012 = "1008012";
	/**
	 * 转贴现(1008013)
	 */
	public final static String BUSINESS_013 = "1008013";
	/**
	 * 回购式转贴现赎回(1008014)
	 */
	public final static String BUSINESS_014 = "1008014";
	/**
	 * 再贴现(1008015)
	 */
	public final static String BUSINESS_015 = "1008015";
	/**
	 * 回购式再贴现赎回(1008016)
	 */
	public final static String BUSINESS_016 = "1008016";
	/**
	 * 央行卖出商业汇票(1008025)
	 */
	public final static String BUSINESS_025 = "1008025";
	/**
	 * 保证申请(1008017)
	 */
	public final static String BUSINESS_017 = "1008017";
	/**
	 * 质押(1008018)
	 */
	public final static String BUSINESS_018 = "1008018";
	/**
	 * 质押解除(1008019)
	 */
	public final static String BUSINESS_019 = "1008019";
	/**
	 * 逾期提示付款(1008021)
	 */
	public final static String BUSINESS_021 = "1008021";
	/**
	 * 票据结清解除保证(10080201)
	 */
	public final static String BUSINESS_0201 = "10080201";
	/**
	 * 票据逾期结清解除保证(10080211)
	 */
	public final static String BUSINESS_0211 = "10080211";
	// 追索 022
	/**
	 * 追索同意清偿(1008023)
	 */
	public final static String BUSINESS_023 = "1008023";
	/**
	 * 通用回复(1008031)
	 */
	public final static String BUSINESS_031 = "1008031";
	/**
	 * 通用撤回(1008032)
	 */
	public final static String BUSINESS_032 = "1008032";
	/**
	 * 清分失败(1008035)
	 */
	public final static String BUSINESS_035 = "1008035";
	/**
	 * 信用查询(1008037)
	 */
	public final static String BUSINESS_037 = "1008037";
	/**
	 * 自由格式报文(1008039)
	 */
	public final static String BUSINESS_039 = "1008039";
	/*
	 * 8 汇总详细申请(1008072)
	 */
	public final static String BUSINESS_072 = "1008072";
	/**
	 * 详细申请报文
	 */
	public final static String BUSINESS_074 = "1008074";
	/**
	 * 每月票据信息汇总核对申请报文
	 */
	public final static String BUSINESS_078 = "1008078";
	/**
	 * 纸票承兑登记
	 */
	public final static String BUSINESS_101 = "1008101";
	/**
	 * 纸票未用退回登记
	 */
	public final static String BUSINESS_125 = "1008125";
	/**
	 * 纸票贴现登记
	 */
	public final static String BUSINESS_102 = "1008102";
	/**
	 * 纸票转贴现登记
	 */
	public final static String BUSINESS_103 = "1008103";
	/**
	 * 纸票再贴现登记
	 */
	public final static String BUSINESS_104 = "1008104";
	/**
	 * 纸票质押登记
	 */
	public final static String BUSINESS_105 = "1008105";
	/**
	 * 纸票质押解除登记
	 */
	public final static String BUSINESS_106 = "1008106";
	/**
	 * 纸票委托收款登记
	 */
	public final static String BUSINESS_107 = "1008107";
	/**
	 * 纸票结清登记
	 */
	public final static String BUSINESS_108 = "1008108";
	/**
	 * 纸票拒付登记
	 */
	public final static String BUSINESS_109 = "1008109";
	/**
	 * 纸票挂失止付及公示催告登记
	 */
	public final static String BUSINESS_110 = "1008110";
	/**
	 * 纸票止付解除登记
	 */
	public final static String BUSINESS_111 = "1008111";
	/**
	 * 纸票登记信息查询
	 */
	public final static String BUSINESS_121 = "1008121";
	/**
	 * 转贴现报价发布
	 */
	public final static String BUSINESS_123 = "1008123";
	/**
	 * 申请付款
	 */
	public final static String BUSINESS_8001 = "10088001";
	/**
	 * 定期代付
	 */
	public final static String BUSINESS_8002 = "10088002";
	/**
	 * 定期代付销账
	 */
	public final static String BUSINESS_8003 = "10088003";
	/**
	 * 开票退票
	 */
	public final static String BUSINESS_8004 = "10088004";
	/**
	 * 收票退票
	 */
	public final static String BUSINESS_8005 = "10088005";
	/**
	 * 收到票据挂失
	 */
	public final static String BUSINESS_8006 = "10088006";
	/**
	 * 收到票据解挂
	 */
	public final static String BUSINESS_8007 = "10088007";
	/**
	 * 托收到账
	 */
	public final static String BUSINESS_8008 = "10088008";
	/**
	 * 贴现到账
	 */
	public final static String BUSINESS_8009 = "10088009";
	/**
	 * 转贴现到账
	 */
	public final static String BUSINESS_8010 = "10088010";
	/**
	 * 再贴现到账
	 */
	public final static String BUSINESS_8011 = "10088011";
	/**
	 * 电票撤票
	 */
	public final static String BUSINESS_8012 = "10088012";
	/**
	 * 拒付
	 */
	public final static String BUSINESS_8013 = "10088013";
	/**
	 * 入库
	 */
	public final static String BUSINESS_8014 = "10088014";
	/**
	 * 移库
	 */
	public final static String BUSINESS_8015 = "10088015";
	/**
	 * 托收
	 */
	public final static String BUSINESS_8016 = "10088016";
	/**
	 * 转贴现赎回
	 */
	public final static String BUSINESS_8017 = "10088017";
	/**
	 * 开出票据挂失
	 */
	public final static String BUSINESS_8018 = "10088018";
	/**
	 * 开出票据解挂
	 */
	public final static String BUSINESS_8019 = "10088019";
	/**
	 * 收票修改
	 */
	public final static String BUSINESS_8025 = "10088025";
	/**
	 * 开票修改
	 */
	public final static String BUSINESS_8026 = "10088026";
	/**
	 * 转贴现转入
	 */
	public final static String BUSINESS_8030 = "10088030";
	/**
	 * 转贴现签收
	 */
	public final static String BUSINESS_9013 = "10089013";
	/**
	 * 回购式转贴现赎回签收
	 */
	public final static String BUSINESS_9014 = "10089014";
	/**
	 * 再贴现签收
	 */
	public final static String BUSINESS_9015 = "10089015";
	/**
	 * 回购式再贴现赎回签收
	 */
	public final static String BUSINESS_9016 = "10089016";
	/**
	 * 提示承兑签收
	 */
	public final static String BUSINESS_9002 = "10089002";
	/**
	 * 提示收票签收
	 */
	public final static String BUSINESS_9003 = "10089003";
	/**
	 * 背书签收签收
	 */
	public final static String BUSINESS_9010 = "10089010";
	/**
	 * 贴现签收
	 */
	public final static String BUSINESS_9011 = "10089011";
	/**
	 * 回购式贴现赎回签收
	 */
	public final static String BUSINESS_9012 = "10089012";
	/**
	 * 保证申请签收
	 */
	public final static String BUSINESS_9017 = "10089017";
	/**
	 * 质押签收
	 */
	public final static String BUSINESS_9018 = "10089018";
	/**
	 * 质押解除签收
	 */
	public final static String BUSINESS_9019 = "10089019";
	/**
	 * 提示付款签收
	 */
	public final static String BUSINESS_9020 = "10089020";
	/**
	 * 逾期提示付款签收
	 */
	public final static String BUSINESS_9021 = "10089021";
	/**
	 * 追索签收
	 */
	public final static String BUSINESS_9022 = "10089022";
	/**
	 * 追索同意清偿签收
	 */
	public final static String BUSINESS_9023 = "10089023";
	/**
	 * 央行卖出商业汇票签收
	 */
	public final static String BUSINESS_9025 = "10089025";
	/**
	 * 不可转让撤销申请
	 */
	public final static String BUSINESS_024 = "1008024";
	/**
	 * 不可转让撤销签收
	 */
	public final static String BUSINESS_9024 = "10089024";
	/**
	 * @Fields DRAFT_ENSURE_REQUEST : 出票保证申请
	 */
	public final static String BUSINESS_0171 = "10080171";
	/**
	 * @Fields ACCEPT_ENSURE_REQUEST : 承兑保证申请
	 */
	public final static String BUSINESS_0172 = "10080172";
	/**
	 * @Fields ENDORSED_ENSURE_REQUEST : 背书保证申请
	 */
	public final static String BUSINESS_0173 = "10080173";
	// 科目
	/**
	 * 单位开票
	 */
	public final static long SUBJECT_80080001 = 80080001L;
	/**
	 * 单位库存
	 */
	public final static long SUBJECT_80080002 = 80080002L;
	/**
	 * 银行代保管
	 */
	public final static long SUBJECT_80080003 = 80080003L;
	public final static long SUBJECT_80080006 = 80080006L;
	/**
	 * 财务公司代保管
	 */
	public final static long SUBJECT_80080064 = 80080064L;
	/**
	 * 电票质押
	 */
	public final static long SUBJECT_80080016 = 80080016L;
	/**
	 * 发出托收
	 */
	public final static long SUBJECT_80080066 = 80080066L;
	/**
	 * 纸票质押
	 */
	public final static long SUBJECT_80080068 = 80080068L;
	/**
	 * 发出贴现
	 */
	public final static long SUBJECT_80080070 = 80080070L;
	
	/**
	 * 转贴现
	 */
	public final static long SUBJECT_80080071 = 80080071L;
	
	/**
	 * 再贴现
	 */
	public final static long SUBJECT_80080072 = 80080071L;
	/**
	 * 收到票据挂失
	 */
	public final static long SUBJECT_80080073 = 80080073L;
	/**
	 * 开出票据挂失
	 */
	public final static long SUBJECT_80080076 = 80080076L;
	/**
	 * 单位开票登记
	 */
	public final static long SUBJECT_80080005 = 80080005L;
	/**
	 * 出票保证
	 */
	public final static long SUBJECT_80080050 = 80080050L;
	/**
	 * 承兑保证
	 */
	public final static long SUBJECT_80080051 = 80080051L;
	/**
	 * 背书保证
	 */
	public final static long SUBJECT_80080052 = 80080052L;
	// 人行定义的票据状态
	/**
	 * @Fields BILL_STATUS_010004 : 出票已登记
	 */
	public static final String BILL_STATUS_010004 = "010004";
	/**
	 * @Fields BILL_STATUS_020001 : 提示承兑待签收
	 */
	public static final String BILL_STATUS_020001 = "020001";
	/**
	 * @Fields BILL_STATUS_020006 : 提示承兑已签收
	 */
	public static final String BILL_STATUS_020006 = "020006";
	/**
	 * @Fields BILL_STATUS_030001 : 提示收票待签收
	 */
	public static final String BILL_STATUS_030001 = "030001";
	/**
	 * @Fields BILL_STATUS_030006 : 提示收票已签收
	 */
	public static final String BILL_STATUS_030006 = "030006";
	/**
	 * @Fields BILL_STATUS_030007 : 提示收票拒绝签收
	 */
	public static final String BILL_STATUS_030007 = "030007";
	/**
	 * @Fields BILL_STATUS_000002 : 结束已作废
	 */
	public static final String BILL_STATUS_000002 = "000002";
	/**
	 * @Fields BILL_STATUS_100001 : 背书待签收
	 */
	public static final String BILL_STATUS_100001 = "100001";
	/**
	 * @Fields BILL_STATUS_100006 : 背书已签收
	 */
	public static final String BILL_STATUS_100006 = "100006";
	/**
	 * @Fields BILL_STATUS_110101 : 买断式贴现待签收
	 */
	public static final String BILL_STATUS_110101 = "110101";
	/**
	 * @Fields BILL_STATUS_110103 : 买断式贴现已签收待清算
	 */
	public static final String BILL_STATUS_110103 = "110103";
	/**
	 * @Fields BILL_STATUS_110105 : 买断式贴现已签收已排队
	 */
	public static final String BILL_STATUS_110105 = "110105";
	/**
	 * @Fields BILL_STATUS_110106 : 买断式贴现已签收
	 */
	public static final String BILL_STATUS_110106 = "110106";
	/**
	 * @Fields BILL_STATUS_110201 : 回购式贴现待签收
	 */
	public static final String BILL_STATUS_110201 = "110201";
	/**
	 * @Fields BILL_STATUS_110203 : 回购式贴现已签收待清算
	 */
	public static final String BILL_STATUS_110203 = "110203";
	/**
	 * @Fields BILL_STATUS_110205 : 回购式贴现已签收已排队
	 */
	public static final String BILL_STATUS_110205 = "110205";
	/**
	 * @Fields BILL_STATUS_110206 : 回购式贴现已签收
	 */
	public static final String BILL_STATUS_110206 = "110206";
	/**
	 * @Fields BILL_STATUS_110216 : 回购式贴现已至赎回开放日
	 */
	public static final String BILL_STATUS_110216 = "110216";
	/**
	 * @Fields BILL_STATUS_110218 : 回购式贴现已逾赎回截止日
	 */
	public static final String BILL_STATUS_110218 = "110218";
	/**
	 * @Fields BILL_STATUS_120001 : 回购式贴现赎回待签收
	 */
	public static final String BILL_STATUS_120001 = "120001";
	/**
	 * @Fields BILL_STATUS_120003 : 回购式贴现赎回已签收待清算
	 */
	public static final String BILL_STATUS_120003 = "120003";
	/**
	 * @Fields BILL_STATUS_120005 : 回购式贴现赎回已签收已排队
	 */
	public static final String BILL_STATUS_120005 = "120005";
	/**
	 * @Fields BILL_STATUS_120006 : 回购式贴现赎回已签收
	 */
	public static final String BILL_STATUS_120006 = "120006";
	/**
	 * @Fields BILL_STATUS_130101 : 买断式转贴现待签收
	 */
	public static final String BILL_STATUS_130101 = "130101";
	/**
	 * @Fields BILL_STATUS_130103 : 买断式转贴现已签收待清算
	 */
	public static final String BILL_STATUS_130103 = "130103";
	/**
	 * @Fields BILL_STATUS_130105 : 买断式转贴现已签收已排队
	 */
	public static final String BILL_STATUS_130105 = "130105";
	/**
	 * @Fields BILL_STATUS_130106 : 买断式转贴现已签收
	 */
	public static final String BILL_STATUS_130106 = "130106";
	/**
	 * @Fields BILL_STATUS_130201 : 回购式转贴现待签收
	 */
	public static final String BILL_STATUS_130201 = "130201";
	/**
	 * @Fields BILL_STATUS_130203 : 回购式转贴现已签收待清算
	 */
	public static final String BILL_STATUS_130203 = "130203";
	/**
	 * @Fields BILL_STATUS_130205 : 回购式转贴现已签收已排队
	 */
	public static final String BILL_STATUS_130205 = "130205";
	/**
	 * @Fields BILL_STATUS_130206 : 回购式转贴现已签收
	 */
	public static final String BILL_STATUS_130206 = "130206";
	/**
	 * @Fields BILL_STATUS_130216 : 回购式转贴现已至赎回开放日
	 */
	public static final String BILL_STATUS_130216 = "130216";
	/**
	 * @Fields BILL_STATUS_130218 : 回购式转贴现已逾赎回截止日
	 */
	public static final String BILL_STATUS_130218 = "130218";
	/**
	 * @Fields BILL_STATUS_140001 : 回购式转贴现赎回待签收
	 */
	public static final String BILL_STATUS_140001 = "140001";
	/**
	 * @Fields BILL_STATUS_140003 : 回购式转贴现赎回已签收待清算
	 */
	public static final String BILL_STATUS_140003 = "140003";
	/**
	 * @Fields BILL_STATUS_140005 : 回购式转贴现赎回已签收已排队
	 */
	public static final String BILL_STATUS_140005 = "140005";
	/**
	 * @Fields BILL_STATUS_140006 : 回购式转贴现赎回已签收
	 */
	public static final String BILL_STATUS_140006 = "140006";
	/**
	 * @Fields BILL_STATUS_150101 : 买断式再贴现待签收
	 */
	public static final String BILL_STATUS_150101 = "150101";
	/**
	 * @Fields BILL_STATUS_150103 : 买断式再贴现已签收待清算
	 */
	public static final String BILL_STATUS_150103 = "150103";
	/**
	 * @Fields BILL_STATUS_150105 : 买断式再贴现已签收已排队
	 */
	public static final String BILL_STATUS_150105 = "150105";
	/**
	 * @Fields BILL_STATUS_150106 : 买断式再贴现已签收
	 */
	public static final String BILL_STATUS_150106 = "150106";
	/**
	 * @Fields BILL_STATUS_150201 : 回购式再贴现待签收
	 */
	public static final String BILL_STATUS_150201 = "150201";
	/**
	 * @Fields BILL_STATUS_150203 : 回购式再贴现已签收待清算
	 */
	public static final String BILL_STATUS_150203 = "150203";
	/**
	 * @Fields BILL_STATUS_150205 : 回购式再贴现已签收已排队
	 */
	public static final String BILL_STATUS_150205 = "150205";
	/**
	 * @Fields BILL_STATUS_150206 : 回购式再贴现已签收
	 */
	public static final String BILL_STATUS_150206 = "150206";
	/**
	 * @Fields BILL_STATUS_150216 : 回购式再贴现已至赎回开放日
	 */
	public static final String BILL_STATUS_150216 = "150216";
	/**
	 * @Fields BILL_STATUS_150218 : 回购式再贴现已逾赎回截止日
	 */
	public static final String BILL_STATUS_150218 = "150218";
	/**
	 * @Fields BILL_STATUS_160001 : 回购式再贴现赎回待签收
	 */
	public static final String BILL_STATUS_160001 = "160001";
	/**
	 * @Fields BILL_STATUS_160003 : 回购式再贴现赎回已签收待清算
	 */
	public static final String BILL_STATUS_160003 = "160003";
	/**
	 * @Fields BILL_STATUS_160005 : 回购式再贴现赎回已签收已排队
	 */
	public static final String BILL_STATUS_160005 = "160005";
	/**
	 * @Fields BILL_STATUS_160006 : 回购式再贴现赎回已签收
	 */
	public static final String BILL_STATUS_160006 = "160006";
	/**
	 * @Fields BILL_STATUS_180001 : 质押待签收
	 */
	public static final String BILL_STATUS_180001 = "180001";
	/**
	 * @Fields BILL_STATUS_180006 : 质押已签收
	 */
	public static final String BILL_STATUS_180006 = "180006";
	/**
	 * @Fields BILL_STATUS_180020 : 质押已至票据到期日
	 */
	public static final String BILL_STATUS_180020 = "180020";
	/**
	 * @Fields BILL_STATUS_190001 : 质押解除待签收
	 */
	public static final String BILL_STATUS_190001 = "190001";
	/**
	 * @Fields BILL_STATUS_190006 : 质押解除已签收
	 */
	public static final String BILL_STATUS_190006 = "190006";
	/**
	 * @Fields BILL_STATUS_170001 : 保证待签收
	 */
	public static final String BILL_STATUS_170001 = "170001";
	/**
	 * @Fields BILL_STATUS_170002 : 保证已签收
	 */
	public static final String BILL_STATUS_170002 = "170002";
	/**
	 * @Fields BILL_STATUS_200001 : 提示付款待签收
	 */
	public static final String BILL_STATUS_200001 = "200001";
	/**
	 * @Fields BILL_STATUS_200003 : 提示付款已签收待清算
	 */
	public static final String BILL_STATUS_200003 = "200003";
	/**
	 * @Fields BILL_STATUS_200005 : 提示付款已签收已排队
	 */
	public static final String BILL_STATUS_200005 = "200005";
	/**
	 * @Fields BILL_STATUS_000000 : 结束已结清
	 */
	public static final String BILL_STATUS_000000 = "000000";
	/**
	 * @Fields BILL_STATUS_200312 : 提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）
	 */
	public static final String BILL_STATUS_200312 = "200312";
	/**
	 * @Fields BILL_STATUS_200412 : 提示付款已拒付（可拒付追索，可以追所有人）
	 */
	public static final String BILL_STATUS_200412 = "200412";
	/**
	 * @Fields BILL_STATUS_200512 : 提示付款已拒付（不可进行拒付追索）
	 */
	public static final String BILL_STATUS_200512 = "200512";
	/**
	 * @Fields BILL_STATUS_210001 : 逾期提示付款待签收
	 */
	public static final String BILL_STATUS_210001 = "210001";
	/**
	 * @Fields BILL_STATUS_210003 : 逾期提示付款已签收待清算
	 */
	public static final String BILL_STATUS_210003 = "210003";
	/**
	 * @Fields BILL_STATUS_210005 : 逾期提示付款已签收已排队
	 */
	public static final String BILL_STATUS_210005 = "210005";
	/**
	 * @Fields BILL_STATUS_210312 : 逾期提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）
	 */
	public static final String BILL_STATUS_210312 = "210312";
	/**
	 * @Fields BILL_STATUS_210412 : 逾期提示付款已拒付（可拒付追索，可以追所有人）
	 */
	public static final String BILL_STATUS_210412 = "210412";
	/**
	 * @Fields BILL_STATUS_220607 : 拒付追索待清偿
	 */
	public static final String BILL_STATUS_220607 = "220607";
	/**
	 * @Fields BILL_STATUS_230601 : 拒付追索同意清偿待签收
	 */
	public static final String BILL_STATUS_230601 = "230601";
	/**
	 * @Fields BILL_STATUS_230606 : 拒付追索同意清偿已签收
	 */
	public static final String BILL_STATUS_230606 = "230606";
	/**
	 * @Fields BILL_STATUS_220707 : 非拒付追索待清偿
	 */
	public static final String BILL_STATUS_220707 = "220707";
	/**
	 * @Fields BILL_STATUS_220710 : 非拒付追索已撤销
	 */
	public static final String BILL_STATUS_220710 = "220710";
	/**
	 * @Fields BILL_STATUS_230701 : 非拒付追索同意清偿待签收
	 */
	public static final String BILL_STATUS_230701 = "230701";
	/**
	 * @Fields BILL_STATUS_230706 : 非拒付追索同意清偿已签收
	 */
	public static final String BILL_STATUS_230706 = "230706";
	/**
	 * @Fields BILL_STATUS_250001 : 央行卖票待签收
	 */
	public static final String BILL_STATUS_250001 = "250001";
	/**
	 * @Fields BILL_STATUS_250003 : 央行卖票已签收待清算
	 */
	public static final String BILL_STATUS_250003 = "250003";
	/**
	 * @Fields BILL_STATUS_250005 : 央行卖票已签收已排队
	 */
	public static final String BILL_STATUS_250005 = "250005";
	/**
	 * @Fields BILL_STATUS_250006 : 央行卖票已签收
	 */
	public static final String BILL_STATUS_250006 = "250006";
	/**
	 * @Fields BILL_STATUS_000026 : 已逾票据权利失效日
	 */
	public static final String BILL_STATUS_000026 = "000026";
	/**
	 * 034 通用转发
	 */
	public static final String COMMONTRANSMISSION = "034";
	/**
	 * 001 出票申请
	 */
	public static final String MSG_TYPE_REGISTER_REQUEST_001 = "001";
	/**
	 * 002 提示承兑
	 */
	public static final String MSG_TYPE_ACCEPTANCE_REQUEST_002 = "002";
	/**
	 * 003 提示收票
	 */
	public static final String MSG_TYPE_ISSUANCE_REQUEST_003 = "003";
	/**
	 * 004 撤票申请
	 */
	public static final String MSG_TYPE_DESTRUCTION_REQUEST_004 = "004";
	/**
	 * 010 背书转让
	 */
	public static final String MSG_TYPE_ENDORSEMENT_REQUEST_010 = "010";
	/**
	 * 011 贴现申请
	 */
	public static final String MSG_TYPE_DISCOUNT_REQUEST_011 = "011";
	/**
	 * 012 回购式贴现赎回申请
	 */
	public static final String MSG_TYPE_REPURCHASED_DISCOUNT_REQUEST_012 = "012";
	
	/**
	 * 013转贴现申请
	 */
	public static final String MSG_TYPE_REDISCOUNT_REQUEST_013 = "013";
	
	/**
	 * 014 回购式转贴现赎回申请
	 */
	public static final String MSG_TYPE_REPURCHASED_REDISCOUNT_REQUEST_014 = "014";
	
	/**
	 * 015再贴现申请
	 */
	public static final String MSG_TYPE_AGAINDISCOUNT_REQUEST_015 = "015";
	
	/**
	 * 016 回购式再贴现赎回申请
	 */
	public static final String MSG_TYPE_REPURCHASED_AGAINDISCOUNT_REQUEST_016 = "016";
	/**
	 * 017 保证申请
	 */
	public static final String MSG_TYPE_GUARANTEE_REQUEST_017 = "017";
	/**
	 * 018 质押申请
	 */
	public static final String MSG_TYPE_COLLATERALIZATION_REQUEST_018 = "018";
	public static final String MSG_TYPE_COLLATERALIZATION_REQUEST_018_RC01 = "018_RC01";//交行成员单位质押
	public static final String MSG_TYPE_COLLATERALIZATION_REQUEST_018_RC04 = "018_RC04";//交行代理财务公司质押
	/**
	 * 019 质押解除申请
	 */
	public static final String MSG_TYPE_REPURCHASED_COLLATERALIZATION_REQUEST_019 = "019";
	public static final String MSG_TYPE_REPURCHASED_COLLATERALIZATION_REQUEST_019_RC01 = "019_RC01";//交行成员单位质押解除
	public static final String MSG_TYPE_REPURCHASED_COLLATERALIZATION_REQUEST_019_RC04 = "019_RC04";//交行代理财务公司质押解除
	/**
	 * 020 提示付款申请
	 */
	public static final String MSG_TYPE_PRESENTATION_REQUEST_020 = "020";
	/**
	 * 021 逾期提示付款申请报文
	 */
	public static final String MSG_TYPE_OVER_DUE_PRESENTATION_REQUEST_021 = "021";
	/**
	 * 022 追索通知
	 */
	public static final String MSG_TYPE_RECOURSE_NOTIFICATION_022 = "022";
	/**
	 * 023 追索同意清偿申请
	 */
	public static final String MSG_TYPE_RECOURSE_AGREEMENT_REQUEST_023 = "023";
	
	/**
	 * 024 追索撤回
	 */
	public static final String MSG_TYPE_RECOURSE_CANCELLATION_REQUEST_024 = "024";
	/**
	 * 031 通用回复(签收)
	 */
	public static final String MSG_TYPE_COMMON_SIGN_UP_031 = "031";
	/**
	 * 032 通用撤回
	 */
	public static final String MSG_TYPE_CANCELLATION_REQUEST_032 = "032";
	/**
	 * 线上清算
	 */
	public final static String STTLM_MK_SM00 = "SM00";
	/**
	 * 线下清算
	 */
	/**
	 * 可再转让
	 */
	public final static String BAN_ENDRSMT_MK_EM00 = "EM00";
	/**
	 * 不可再转让
	 */
	public final static String BAN_ENDRSMT_MK_EM01 = "EM01";
	/**
	 * 买断式
	 */
	public final static String RPDMK_RM00 = "RM00";
	/**
	 * 回购式
	 */
	public final static String RPDMK_RM01 = "RM01";
	/**
	 * SU00 人行通用签收标志：同意签收
	 */
	public final static String SIGN_UP_FLAG_AGREE = "SU00";
	/**
	 * SU01 人行通用签收标志：拒绝签收
	 */
	public final static String SIGN_UP_FLAG_REFUSE = "SU01";
	/**
	 * @Fields BILL_TYPE_AC01 : 人行票据类型：银承
	 */
	public final static String BILL_TYPE_AC01 = "AC01";
	/**
	 * @Fields BILL_TYPE_AC02 : 人行票据类型：商承
	 */
	public final static String BILL_TYPE_AC02 = "AC02";
	/**
	 * @Fields PE1I0000 : 人行处理码：处理成功
	 */
	public static final String PE1I0000 = "PE1I0000";
	/**
	 * 兴业银行渠道编号
	 */
	public final static String CIB_CHANNEL_CODE = "309";
	/**
	 * 交通银行渠道编号
	 */
	public final static String COMM_CHANNEL_CODE = "301";
	/**
	 * 光大银行渠道编号
	 */
	public final static String CEB_CHANNEL_CODE = "303";
	
	/**
	 * 中国银行渠道编号
	 */
	public final static String BOC_CHANNEL_CODE = "104";
	/**
	 * @Fields BILL_TYPE_10 : 系统内票据类型：银承
	 */
	public final static int BILL_TYPE_10 = 10;
	/**
	 * @Fields BILL_TYPE_11 : 系统内票据类型：商承
	 */
	public final static int BILL_TYPE_11 = 11;
	/**
	 * @Fields ACCT_TYPE_CUSTOMER : 账号类型：往来客户
	 */
	public final static String ACCT_TYPE_CUSTOMER = "0";
	/**
	 * @Fields ACCT_TYPE_COMPANY : 账号类型：成员单位
	 */
	public final static String ACCT_TYPE_COMPANY = "1";
	/**
	 * @Fields ACCT_TYPE_AUTHORIZE : 账号类型：被授权账号
	 */
	public final static String ACCT_TYPE_AUTHORIZE = "2";
	/**
	 * @Fields IS_AUTHORISE_ACCT : 是否授权账号模式(是否启用授权账号模式 1:是 0:不是)
	 */
	public static final long IS_AUTHORISE_ACCT_MODE = 2128;
	/**
	 * @Fields ENABLED_AUTHORIZE_MODE : 是否启用授权账号模式 1:是 0:不是
	 */
	public final static String ENABLED_AUTHORIZE_MODE = "1";
	/**
	 * @Fields DISABLED_AUTHORIZE_MODE : 是否启用授权账号模式 1:是 0:不是
	 */
	public final static String DISABLED_AUTHORIZE_MODE = "0";
	/**
	 * @Fields ENABLE_CIB_OPERATE_QUERY : 是否启用网银历史票据查询 1 启用
	 */
	public final static String ENABLE_CIB_OPERATE_QUERY = "1";
	/**
	 * 来源于指令表
	 */
	public final static String SOURCE_TYPE_ACCRECORDS_0 = "0";
	/**
	 * 来源于网银历史表
	 */
	public final static String SOURCE_TYPE_NETBANK_HISTORY_1 = "1";
	public final static String CIB_QUERY_STR = "enable_cib_operate_query";
	/**
	 * 到期无条件支付委托
	 */
	public final static String ENTRSPAY = "CC00";
	/**
	 * @Fields CIB_MANUAL_QUERY_STR 启用手动查询(实时查询)
	 */
	public final static String CIB_MANUAL_QUERY_STR = "enable_cib_manual_query";
	/**
	 * @Fields CIB_MANUAL_QUERY_VALUE 启用手动查询标识 1启用 0禁用
	 */
	public final static String CIB_MANUAL_QUERY_VALUE = "1";
	public final static String DATA_AUTH_CODE = "data_auth_code";
	/**
	 * @Fields CURRENCY_CCY : 币种：人民币
	 */
	public final static String CURRENCY_CNY = "CNY";
	/** 接收报文 */
	public static final int IN = 0;
	/** 发出报文 */
	public static final int OUT = 1;
	/**
	 * 基础数据模块名
	 */
	public static final String MODULE_NAME_ADM = "adm";
	/**
	 * 电票模块名
	 */
	public static final String MODULE_NAME_E_DRAFT = "eDraft";
	/**
	 * 分页时每页显示行数(Bt_Param)
	 */
	public static final String ROWS_PER_PAGE = "ranges";
	/**
	 * 清算方式参数(Bt_Param)
	 */
	public static final String LIQUIDATION_MODE = "dectMemoFlag";
	/**
	 * 追索申请参数
	 */
	public static final String PARAM_CODE_COLLATERALIZATION = "collateralization";
	/**
	 * 单位签收参数
	 */
	public static final String PARAM_CODE_SIGN_UP = "singUp";
	/**
	 * 业务审批参数
	 */
	public static final String PARAM_CODE_APPROVE = "approve";
	/**
	 * 企业端支持银行范围
	 */
	public static final String PARAM_CODE_ENABLED_BANK = "enabledBank";
	/**
	 * 业务申请查询模式
	 */
	public static final String PARAM_CODE_QUERY_MODE = "queryMode";
	/**
	 * 贴现申请参数
	 */
	public static final String PARAM_CODE_DISCOUNT = "discount";
	/**
	 * 是否挂接预算管理
	 */
	public static final String PARAM_CODE_ENABLED_BUDGET = "enabledBudget";
	/**
	 * 批量发送指令 0-是,1-否
	 */
	public static final String PARAM_CODE_MULTI_INSTRUCTION = "multiInstruction";
	/**
	 * 授权账号模式,1-是,0-否
	 */
	public static final String PARAM_EMPOWER_ACC = "empoweracc";
//	public static final String PARAM_OPERATOR_NO = "operator_no";
//
//	public static final String PARAM_OPERATOR_PWD = "operator_pwd";
//
//	public static final String PARAM_CUST_NO = "cust_no";
//
//	public static final String PARAM_PORT = "port";
	/**
	 * 硕正表格配置文件编码
	 */
	public static final String SUPCAN_CONFIG_ENCODING = "UTF-8";
	/**
	 * 硕正表格配置文件目录
	 */
	public static final String SUPCAN_CONFIG_DIR = "xml/scgrid/eDraft/";
	/**
	 * 硕正表格配置文件后缀
	 */
	public static final String SUPCAN_CONFIG_SUFFIX = ".xml";
	/**
	 * 通过数据库生成自增长的MSGID
	 */
	public final static Integer BUSINESS_TYPE_MSGID = 0;
	/**
	 * 通过数据库生成查询程序使用的自增长ID
	 */
	public final static Integer BUSINESS_TYPE_QUERY_ID = 1;
	/**
	 * 提示付款/逾期 付款类型：01 正常提示付款 ； 02逾期提示付款
	 */
	public final static String PRESENTATION_TYPE_01 = "01";
	public final static String PRESENTATION_OVER_TYPE_02 = "02";
	/** PS00开户机构代理回复签章 */
	public final static String PROXY_00 = "PS00";
	/** PS01票据当事人自己签章 */
	public final static String PROXY_01 = "PS01";
	/**
	 * 网银历史表的状态 19 表示可发起业务
	 */
	public final static int HISTROY_STATUS_19 = 19;
	/**
	 * 网银历史表的状态 10 表示业务申请中
	 */
	public final static int HISTROY_STATUS_10 = 10;
	/**
	 * 网银历史表的状态 0 表示业务处理完成
	 */
	public final static int HISTROY_STATUS_0 = 0;
	/**
	 * 拒付追索
	 */
	public final static String RECOURSE_TYPE_RT00 = "RT00";
	/**
	 * 非拒付追索
	 */
	public final static String RECOURSE_TYPE_RT01 = "RT01";
	public final static String USER_KEY = "com.bettersoft.admin.userbean";
	/**
	 * 卖方付息
	 */
	public final static String PAY_TYPE_SELL = "1";
	/**
	 * 买方付息
	 */
	public final static String PAY_TYPE_BUY = "2";
	/**
	 * 协议付息
	 */
	public final static String PAY_TYPE_DISCUSS = "3";
	/**
	 * 写入在途
	 */
	public final static int BUDGET_STATUS_NEW = 0;
	/**
	 * 在途转执行
	 */
	public final static int BUDGET_STATUS_FINESHED = 100;
	/**
	 * 撤销在途
	 */
	public final static int BUDGET_STATUS_DELETE = -2;
	/**
	 * 开票预算科目参数
	 */
	public final static String PARAM_CODE_SUBJECT_REGISTER = "registerSubject";
	/**
	 * 背书预算科目参数
	 */
	public final static String PARAM_CODE_SUBJECT_ENDORSE = "endorseSubject";
	/**
	 * 电票账户标识
	 */
	public final static String ELECTRIC_BILL_ACC_FLAG = "1";
	/**
	 * 自动提示承兑标志（代表是）wuh
	 */
	public final static String AUTOACCEPTANCE_FLAG_0 = "0";
	/**
	 * 自动提示承兑标志（代表否）
	 */
	public final static String AUTOACCEPTANCE_FLAG_1 = "1";
	/**
	 * 自动提示收票标志（代表是）
	 */
	public final static String AUTOISSUSE_FLAG_0 = "0";
	/**
	 * 自动提示收票标志（代表否）
	 */
	public final static String AUTOISSUSE_FLAG_1 = "1";
	/**
	 * 开票票据能否转让(代表是)
	 */
	public final static String BANK_END_RSMTMK_CODE_00 = "EM00";
	/**
	 * 开票票据能否转让(代表否)
	 */
	public final static String BANK_END_RSMTMK_CODE_01 = "EM01";
	/**
	 * 电票自动提示承兑参数（默认1） 0--是；1--否
	 * **/
	public final static String AUTO_ACCEPTANCE_CODE = "autoAcceptor";
	/**
	 * 电票自动提示收票参数（默认1） 0--是；1--否
	 * **/
	public final static String AUTO_ISSUSE_CODE = "autoIssuse";
	/**
	 * 电票自动提示付款参数（默认1） 0--是；1--否
	 * **/
	public final static String AUTO_POINT_OUT_PAY_CODE = "auto_point_out_pay";
	/**
	 * 电票自动提示付款提前天数参数（默认10）
	 * 
	 * **/
	public final static String EDRAFT_PAY_BEFORE_DATE_CODE = "eDraft_pay_before_Date";
	/**
	 * 商票提示付款截止天数（默认10）
	 * 
	 * **/
	public final static String BUSNIESS_POINT_PAY_ENDDATE_CODE = "busniess_point_pay_enddate";
	/**
	 * 银票提示付款截止天数（默认10）
	 * 
	 * **/
	public final static String NOTE_POINT_PAY_ENDDATE_CODE = "note_point_pay_endDate";
	/**
	 * 商业电票自动提示值
	 * 
	 * **/
	public final static String EDRAFT_PARAM_VALUE1 = "0";
	/**
	 * 商业电票自动发送指令参数名
	 * 
	 * **/
	public final static String AUTO_INSTRUCT_SEND_CODE = "auto_instruct_send";
	/**
	 * 商业电票指令发送频率参数名
	 * 
	 * **/
	public final static String AUTO_INSTRUCT_SEND_RATE_CODE = "auto_instruct_send_rate";
	/**
	 * 商业电票审批完成，待发送 (自动发送指令用)
	 */
	public static final int OPERATE_STATUS_PRESEND_CODE = 16;
	
	/**
	 * 电票系统中国内金融机构的业务账号
	 */
	public final static String BANK_ACCOUNT = "0";
	/**
	 * 根据业务ID 转换业务类型
	 * 
	 * @return 2014-6-4
	 */
	public static String convertBusinessTypeBybriefId(String bridfId) {
		String bussinessType = "";
		if (bridfId.equals(EDraftConstant.BUSINESS_9002)) {
			bussinessType = "提示承兑签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9003)) {
			bussinessType = "提示收票签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9010)) {
			bussinessType = "背书签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9011)) {
			bussinessType = "贴现签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9012)) {
			bussinessType = "回购式贴现赎回签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9013)) {
			bussinessType = "转贴现签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9014)) {
			bussinessType = "回购式转贴现签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9018)) {
			bussinessType = "质押签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9019)) {
			bussinessType = "质押解除签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9020)) {
			bussinessType = "提示付款签收";
		}
		if (bridfId.equals(EDraftConstant.BUSINESS_9012)) {
			bussinessType = "回购式贴现赎回签收";
		}
		return bussinessType;
	}
	/**
	 * 指令表消息类型--支付信用
	 */
	public final static String MSG_TYPE_PAYMENTCREDIT= "01";
	
	/**
	 * 指令表消息类型--背面信息
	 */
	public final static String MSG_TYPE_BACKINFO= "02";
	/**
	 * 指令表消息类型--正信息
	 */
	public final static String MSG_TYPE_BILLINFO= "03";
	/**
	 * 指令表消息状态--16待发送
	 */
	public final static Integer MSG_STATUS_16= 16;
	/**
	 * 指令表消息状态--19成功
	 */
	public final static Integer MSG_STATUS_19= 19;
	/**
	 * 指令表消息状态-- -1失败
	 */
	public final static Integer MSG_STATUS_1= -1;
	
	public static String getT2BriefId(String briefId){
		if(StringUtil.isNotEmpty(briefId)&&briefId.indexOf("1008")!=-1){
			briefId=briefId.replace("1008", "");
		}
		return briefId;
	}
	
	public static String getV106BriefId(String briefId){
		if(StringUtil.isNotEmpty(briefId)&&briefId.indexOf("1008")<0){
			briefId="1008"+briefId;
		}
		return briefId;
	}
	
	public static String getSourceTypeByBriefId(String briefId){
		String briefIdT2=getT2BriefId(briefId);
		if("001".equals(briefIdT2)
				||"002".equals(briefIdT2)
				||"003".equals(briefIdT2)){
			return AccebillConstant.AB_VOUCHERINFO_OP_TYPE0;
		}else{
			return AccebillConstant.AB_VOUCHERINFO_OP_TYPE1;
		}
	}
}
