package com.bytter.biz.finacial.api.enums.closedeal;
import com.bytter.core.enums.IMessageEnum;
/**
 * 日结信息枚举类
 * @author liyh
 * @since 2017-7-6
 */
public enum CloseMessageEnum implements IMessageEnum {
	
	/**
	 * 06 期终处理
	 * 
	 */
	/**初始化日结网点不能为空*/
	FIN_CLOSE_INIT_NET_NULL("06001"),
	/**网点未初始化，请联系管理员*/
	FIN_CLOSE_NO_INIT_NET("06002"),
	/**网点已日结或反日结，请刷新页面*/
	FIN_CLOSE_NET_DATE_ERROR("06003"),
	/**流水号为{0}的流水账不平衡!*/
	FIN_CLOSE_ORDER_NO_BALANCE("06004"),
	/**各科目的复核联机余额借贷不平衡!*/
	FIN_CLOSE_SUBJECT_BALANCE("06005"),
	/**各科目每日批量余额借贷不平衡!*/
	FIN_CLOSE_FC_ACC_BALANCE("06006"),
	/**联机复核笔数与批量笔数是否相等!*/
	FIN_CLOSE_ACCLEDGER_CNT("06007"),
	/**联机复核余额与批量余额是否相等!*/
	FIN_CLOSE_ACCLEDGER_BALANCE("06008"),
	/**账户每日余额不存在!*/
	FIN_CLOSE_ACC_BALANCE_NOEXIST("06009"),
	/**网点未日结过，不能进行反日结!*/
	FIN_CLOSE_NET_NO_CARRY("06010"),
	/**已做过月结，请先反月结再处理反日结信息！*/
	FIN_CLOSE_NET_MONTH_CARRY("06011"),
	/**网点未月结过，不能进行反月结!*/
	FIN_CLOSE_NET_NO_MCARRY("06012"),
	/**月份已经月结过,不能再进行月结处理!*/
	FIN_CLOSE_NET_ALREADY_CARRY("06013"),
	/**请联系管理员,没有设置月区间定义表!*/
	FIN_CLOSE_NET_NO_CARRY_SET("06014"),
	/**{0}日期前有未结的账，不能跨日日结!!*/
	FIN_CLOSE_EXIST_ACCBOOK("06015"),
	/**日结区间存在表外凭证未复核的情况,请处理*/
	FIN_CLOSE_EXIST_CHECK_OUTACCBOOK("06016"),
	/**结转日期不能小于上次结转日期{0}*/
	FIN_CLOSE_PROFITYEAR_STARTDATE("06017"),
	/**上一月份还未月结，不能进行月结处理!*/
	FIN_CLOSE_NET_SUPERALREADY_CARRY("06018"),
	/**网点未获取到，不能进行{0}结处理!*/
	FIN_CLOSE_NET_NOTGET("06019"),
	/**未开设本年利润科目内部账号*/
	FINACIAL_NO_PROFIT_YEAR_ACC_ALERT_INFO1("060120"),
	/**该网点未设置本年利润业务种类*/
	FINACIAL_NO_BUSRACE_ALERT_INFO2("060121"),
	/**存在待复核或打回凭证，请先处理！*/
	FINACIAL_HAS_DATA_ALERT_INFO3("060122"),
	/**请先维护损益记账规则*/
	FINACIAL_HAS_DATA_ALERT_INFO4("060123"),
	/**暂无符合条件的数据*/
	FINACIAL_HAS_DATA_ALERT_INFO5("060124"),
	/**资金池主体已日结或反日结，请刷新页面*/
	FIN_CLOSE_POOL_DATE_ERROR("060125"),
	/**初始化日结资金池主体不能为空*/
	FIN_CLOSE_INIT_POOLNET_NULL("060126"),
	/**资金池主体未初始化，请联系管理员*/
	FIN_CLOSE_NO_INIT_POOLNET("060127"),
	/**资金池主体未获取到，不能进行{0}结处理!*/
	FIN_CLOSE_POOLNET_NOTGET("060128"),
	/**资金池主体未日结过，不能进行反日结!*/
	FIN_CLOSE_POOLNET_NO_CARRY("060129"),
	/**资金池主体已日结或反日结，请刷新页面*/
	FIN_CLOSE_POOLNET_DATE_ERROR("060130"),
	/**网点未日初过，不能进行反日初!*/
	FIN_START_NET_NO_CARRY("060131"),
	/**网点已日初或反日初，请刷新页面*/
	FIN_START_NET_DATE_ERROR("060132"),
	/**网点已日初，不能进行反日结*/
	FIN_RES_CLOSE_NET_DATE_ERROR("060133"),
	/**网点当前日结日期{0}未日结，不能进行多日日初*/
	FIN_START_NET_NOT_CLOSE("060134"),
	/**账务明细余额更新失败*/
	FIN_ACCBOOKBALANCE_UPODATE_ERROR("060135"),
	/**网点当前未日结，不能进行多日日初*/
	FIN_START_NET_NOT_CLOSE_NOT_DATE("060136"),
	/**期终处理内部结转损益转本年利润(0)已记账*/
	FIN_PROCES_INT_CARRY_PROANDLOSS_BOOKKEPT("060137"),
	;
	CloseMessageEnum(String code) {
		this.code = code;
	}
	private String code;
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public Enum getEnum() {
		return this;
	}
	@Override
	public String getCode() {
		return code;
	}
}
