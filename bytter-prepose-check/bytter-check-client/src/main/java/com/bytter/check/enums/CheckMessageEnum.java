package com.bytter.check.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 对账信息枚举类
 * @author liyh
 * @since 2017-7-6
 */
public enum CheckMessageEnum implements IMessageEnum {
	/**
	 * 02 对账管理
	 * xx1 系统维护
	 * xx2 中心数据管理
	 * xx3 银行数据管理
	 * xx4 ERP数据管理
	 * xx5 对账
	 * xx6 对账查询
	 */
	/**数据中存在已结转的数据，不能重新生成*/
	FIN_CHECK_CENTRALIYDATAMANAGER_COMPARE_OVER("02201"),
	/**凭证号不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_VOUCHERNO_EMPTY("02202"),
	/**流水号不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_ORDERNO_EMPTY("02203"),
	/**中心账号不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_COMPACC_EMPTY("02204"),
	/**借贷方金额不能同时存在*/
	FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_NOTZERO("02205"),
	/**借贷方金额不能同时为0*/
	FIN_CHECK_CENTRALIYDATAMANAGER_DE_CR_ZERO("02206"),
	/**记账日期不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_TALLYDATE_EMPTY("02207"),
	/**企业账来源不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_DATASOURCE_EMPTY("02208"),
	/**余额不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_BALANCE_EMPTY("02209"),
	/**银行账号不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_EMPTY("02210"),
	/**银行账号未维护ERP科目对应关系*/
	FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_NOTERPR("02211"),
	/**银行账号不存在*/
	FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_ISNULL("02212"),
	/**内部账号/erp企业科目不能为空*/
	FIN_CHECK_CENTRALIYDATAMANAGER_ACCOUNT_EMPTY("02213"),
	/**内部账号不存在或未授权*/
	FIN_CHECK_ACCOUNT_IS_NULL("02214"),
	/**已对账的数据不能删除*/
	FIN_CHECK_CENTRALIYDATAMANAGER_COMPARE_YES_DEL("02247"),
	/**未初始化余额*/
	FIN_PARAMCONFIG_FCBANKINIT_IS_NULL("02201"),
	/**记账日期大于初始化日期*/
	FIN_PARAMCONFIG_TALLYDATE_PASS_CHECKDATE("02202"),
	/**银行账号为空*/
	FIN_CHECK_FCBANKBILLTEMP_NETBANK_NULL("02101"),
	/**发生日期为空*/
	FIN_CHECK_FCBANKBILLTEMP_TALLYDATE_NULL("02102"),
	/**借方金额为空*/
	FIN_CHECK_FCBANKBILLTEMP_DEBITMONEY_NULL("02103"),
	/**贷方金额为空*/
	FIN_CHECK_FCBANKBILLTEMP_CREDITMONEY_NULL("02104"),
	/**凭证号为空*/
	FIN_CHECK_FCBANKBILLTEMP_VOUCHERNO_NULL("02105"),
	/**企业账来源为空*/
	FIN_CHECK_FCBANKBILLTEMP_DATASOURCE_NULL("02106"),
	/**Excel为空*/
	FIN_CHECK_FCBANKBILLTEMP_EXCEL_NULL("02107"),
	/**记账日期大于对账初始日期*/
	FIN_CHECK_FCBANKBILL_TALLYDATE_PASS_CHECKDATE("02301"),
	/**存在已结转的数据不能添加*/
	FIN_CHECK_FCBANKBILL_CONTAIN_COMPARE_SIGN_OVER("02302"),
	/***不存在账号查询明细权限*/
	FIN_CHECK_FCBANKBILL_NOT_BANKACC_QUERY_AUTHORITY("02304"),
	/**借贷方金额不能同时为0*/
	FIN_CHECK_FCBANKBILL_DE_CR_ZERO("02305"),
	/**借贷方金额不能同时不为0*/
	FIN_CHECK_FCBANKBILL_DE_CR_NOT_ZERO("02309"),
	/**币别不能为空*/
	FIN_CHECK_FCBANKBILL_CURID_NULL("02306"),
	/**发生日期为空*/
	FIN_CHECK_FCBANKBILL_TALLYDATE_NULL("02307"),
	/*红蓝标志不能为空*/
	FIN_CHECK_FCBANKBILL_REDBULESIGN_NULL("02308"),
	/**已经产生银行余额调节表，不能删除！如需删除，请先删除余额调节表*/
	FIN_CHECK_FCBANKBILL_IS_ALREADY_ADJUST("02308"),
	/**结束日期应大于上次结转日期{0},请重新选择日期!*/
	FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE("025001"),
	/**中心账号：{0}余额不存在，请确定是否已日结！*/
	FIN_CHECK_CENTERCHECK_ACCBALISNOTEXISTS("025002"),
	/**未能找到银行账户：{0}明细或余额，请确定是否已初始化银行账号余额或明细！*/
	FIN_CHECK_CENTERCHECK_NOTBANKACCDTL("025003"),
	/**银行和中心调节平衡数不平衡，请核对数据再结转*/
	FIN_CHECK_CENTERCHECK_OVER_NOTBALANCE("025004"),
	/**银行账号：{0},中心账号：{1}还未初始化账户余额，请先初始化账户余额!*/
	FIN_CHECK_CENTERCHECK_BALNOTINIT("025005"),
	/**没有可操作的网点，请分配用户资源权限*/
	FIN_CHECK_NETNOTAUTH("025006"),
	/**银行账号：{0},ERP企业科目：{1}还未初始化账户余额，请先初始化账户余额!*/
	FIN_CHECK_ERPCHECK_BALNOTINIT("025007"),
	/**银行和ERP调节平衡数不平衡，请核对数据再结转！*/
	FIN_CHECK_ERPCHECK_OVER_NOTBALANCE("025008"),
	/**没有可操作的单位，请分配用户资源权限*/
	FIN_CHECK_CORPNOTAUTH("025009"),
	/**发生日期不能为空*/
	FIN_CHECK_FCBANKBILLTEMP_TALLYDATE_NOT_NULL("02110"),
	/**银行账号:{0}还未初始化账户余额，请先初始化账户余额!*/
	FIN_CHECK_BANKACC_IS_NULL("02111"),
    /**选择的结转记录不是最后的结转记录，应从最后的结转记录开始反结转!*/
    FIN_CHECK_BANKACC_ADJUST_BEHIND("02112"),
    /**选择的结转记录已处理，请刷新后重试!*/
    FIN_CHECK_BANKACC_ADJUST_CHANGED("02113"),
	/**提取的数据已存在!*/
	FIN_CHECK_BANKACC_DATA_EXIST("02114"),
	/**自动对账策略代码不能重复!*/
	FIN_CHECK_CONFIG_CODE_ERROR("02115"),
	;
	CheckMessageEnum(String code) {
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
