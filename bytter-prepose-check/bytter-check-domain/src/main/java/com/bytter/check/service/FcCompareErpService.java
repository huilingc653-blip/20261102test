package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.CheckConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.check.dao.FcCompareErpDao;
import com.bytter.check.entity.*;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.FcAccListVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 14:43:17
* @version 1.0
*
*/
@Service("fcCompareErpService")
@Transactional
public class FcCompareErpService extends BaseService<FcCompareAcc,String> {
	@Autowired
	private FcCompareErpDao fcCompareErpDao;
	@Autowired
	private FcBankBillService fcBankBillService;
	@Autowired
	private FcCheckInitService fcCheckInitService;
	@Autowired
	private FcBankCheckLogService fcBankCheckLogService;
	@Autowired
	private FcBankCheckDtlService fcBankCheckDtlService;
	@Autowired
	private FcBankBalanceAdjustService fcBankBalanceAdjustService;
	@Autowired
	private FcBankBalanceAdjustDetailService adjustDetailService;
	@Autowired
	private FcCompareAccProcService fcCompareAccProcService;
	@Autowired
	private FcBankBillProcService fcBankBillProcService;
	@Autowired
	private FcCompareCheckTypeService fcCompareCheckTypeService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IFcDayCloseService iFcDayCloseService;
	@DubboReference(check = false)
	private IBtBankAccService bankService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCompareAcc entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcCompareAcc entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCompareAcc entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCompareAcc entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcCompareAcc, String> getEntityDao() {
		return fcCompareErpDao;
	}
	
	/**
	 * <b>功能介绍：</b>初始化单位<br>
	--------------------------------------------------------
	 */
	public String getInitNetId(String resouce,String userId){
		List<CorpDTO> authCorps = iCorpService.listUserCorp(resouce,userId);
		List<CorpDTO> authCorpss = new ArrayList<CorpDTO>();
		for (CorpDTO corp : authCorps) {
			//if(CorpConstant.CORP_TYPE_CODE_CORP.equals(corp.getType())){
				authCorpss.add(corp);
			//}
		}
		Collections.sort(authCorpss, new Comparator<CorpDTO>(){//网点排序
			public int compare(CorpDTO o1, CorpDTO o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		});
		if(EmptyUtils.isEmptyList(authCorpss)){//没有可操作的网点
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CORPNOTAUTH);
		}
		return authCorpss.get(0).getId();
	}
	
	
	/**
	 * <b>功能介绍：</b>获取网点日结日期<br>
	 * <b>日期：</b>2017年9月26日<br>
	 * <b>作者：</b>caozx<br>-------------------------------
	 * @version 1.0
	 * @param netId
	 * @return
	 */
	public Date getDayOverByNetId(String netId){
		return iFcDayCloseService.getDayCloseFinalDate(netId);
	}
	
	/**
	 * <b>功能介绍:查询中心对账数据(未达、已对账、已勾兑、已对账等)</b><br>
	---------------------------------------------------------------------------------
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryErpCheckDatas(FcCompareErpSearchVO fcCompareErpSearchVO, Date transDate, String[] compareSigns){
		return fcCompareErpDao.queryErpCheckDatas(fcCompareErpSearchVO,transDate,compareSigns);
	}
	
	/**
	 * <b>功能介绍:获取对账的银行账号</b><br>
	 * <b>日期：</b>2017年6月9日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0--------------------------------
	 */
	@Transactional(readOnly = true)
	public List<FcCompareAccVO> getBankAccByOpType(String bankAcc, String netCode, String natureCode, String userId){
    	return fcCompareErpDao.getBankAccByOpType(bankAcc,netCode,natureCode,userId);
    }
	
	/**
	 * 功能介绍:对账操作处理<br>
	 *     --------------------------------------------------------------------
	 */
	public void doCheck(FcBankCheckSearchVo search, Date checkDate, ICurrentUser user){
//		super.logger.info("对账开始日期："+checkDate);
//		super.logger.info("对账结束日期："+search.getTransTime());
//		super.logger.info("对账帐号："+search.getAccount());
//		super.logger.info("对账类型："+search.getCheckType());
		
		String userId = user.getId();
        Date transDate = search.getTransTime();
        String account = search.getAccount();
        String bankAcc = search.getBankAcc();
        String curId = search.getCurId();
        String importType = search.getCheckType();//核对标准
        
        //银企对账类型
        String paramType = search.getParamType();
		 checkDate = DateUtils.addDay(checkDate, 1);//前台输入的 是 2013.5.31 本次对账应该是从 2013.6.1开始
		 
		 //重新生成银行对账数据
		 //generateBankErpDatas(transDate, checkDate, bankAcc, userCode, account);
		 //重新生成ERP银行对账数据
		 //generateErpBankDatas(transDate, checkDate, bankAcc, userCode);
		 //更新上期未达为未对账
		 fcBankBillService.updateErpBillInitStatus(transDate, checkDate, bankAcc, curId);
		 this.updateErpBillInitStatus(transDate, checkDate, bankAcc, curId);
		 //银行对账数据
		 FcBankBillVo bankSearchBean = new FcBankBillVo();
		 bankSearchBean.setTallyDate(transDate);
		 bankSearchBean.setBankAcc(bankAcc);
		 bankSearchBean.setAccount(search.getAccount());
		 bankSearchBean.setCurId(curId);
		 //查询银行对帐数据ERP
		 List<FcBankBillVo> bankBillList = fcBankBillService.queryFcBankBillsErp(bankSearchBean, user.getId());
		
		  //ERP对账数据
		 bankSearchBean.setAccount(bankAcc);
		 FcCompareAccVO erpSearchBean = new FcCompareAccVO();
		 erpSearchBean.setCompAcc(bankAcc);
		 erpSearchBean.setTallyDate(transDate);
		 erpSearchBean.setCurId(curId);
		 List<FcCompareAccVO> bankErpList = fcCompareErpDao.getCheckFcCompareErpDatas( erpSearchBean, user.getId());
		 //对账日志
		 FcBankCheckLog checkLog = buildCheckLog(bankAcc, account, CheckConstant.OPTYPE_TYPE_CHECK,paramType,importType,user);
		 checkLog.setBeginDate(checkDate);
		 checkLog.setEndDate(transDate);
		 checkLog.setCurCode(curId);
		 fcBankCheckLogService.save(checkLog);
		 
		 //核对成功的数据
		 List<FcCheckOverBean> checkOverList = new ArrayList<FcCheckOverBean>();
		 if (bankBillList == null || bankBillList.size() < 1) {
			 return;
		 }
		 
		 if (FinacialConstant.CheckType.TYPE_MONEY_SAP_VOUCHERNO.equals(importType)){
			 doCheckRushVoucher(bankErpList);//核对是否互为冲销票据
		 }
		 for (FcBankBillVo bankBillVo : bankBillList) {
			 String compareSign = bankBillVo.getCompareSign();
			 if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)
					 || FinacialConstant.COMPARE_SIGN_OVER.equals(compareSign)) {
				 //手工勾对和已结转
				 continue;
			 } else if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {
				 compareWithErpBankAcc(checkOverList, bankBillVo, bankErpList, importType);
			 }
		 }
		 //保存数据
		 updateCheckErpOverCompareSign(checkOverList, checkLog.getId());
	}
	/**
	 * <br>
	 * <b>功能：</b>更新上期未达的数据为未对账<br>
	-----------------------------------------------------------------------------------------
	 */
	public void updateErpBillInitStatus(Date transDate, Date checkDate, String account, String curId) {
		fcCompareErpDao.updateErpBillInitStatus(transDate, checkDate, account, curId);
	}
	/**
	 * <br>
	 * <b>功能：</b>核对互为冲销票据<br>
	--------------------------------------------------------------------------
	 */
	private void doCheckRushVoucher(List<FcCompareAccVO> bankErpList){
		String erpVoucherNo = "";//ERP票据号
		String erpImportType = "";//ERP导入类型
		String erpBankId = "";//ERP银行ID
		String content = "";//ERP摘要
		List<Map<String, Object>> rushList = new ArrayList<Map<String,Object>>();
		/**
		 * 互为冲销凭证
		 */
		for (FcCompareAccVO ctrMap : bankErpList) {
			erpImportType =  ctrMap.getImportType();
			erpBankId =  ctrMap.getBankId();
			content =  ctrMap.getSummaryContent();
			erpVoucherNo =  ctrMap.getVoucherNo();
			boolean flag = false;
			//erp接口数据是否互为冲销凭证
			for (FcCompareAccVO tempMap : bankErpList) {
				flag = false;
				String tempErpBankId =  tempMap.getBankId();
				if (tempErpBankId.equals(erpBankId)) {
					continue;
				}
				String billNo = tempMap.getVoucherNo();//凭证号
				if (StringUtils.isNotEmpty(billNo) && StringUtils.isNotBlank(content)
						&& content.indexOf(billNo) >= 0) {
					flag  = true;//记对账标志为已对账
					Map<String, Object> rushMap = new HashMap<String, Object>();
					String erpBankIdTemp =  tempMap.getBankId();
					String erpImportTypeTemp =  ctrMap.getImportType();
					rushMap.put("bankId", erpBankIdTemp);
					rushMap.put("importType", erpImportTypeTemp);
					rushMap.put("billNo", erpVoucherNo);//对方的凭证号
					rushList.add(rushMap);
					Map<String, Object> rushMap2 = new HashMap<String, Object>();
					rushMap2.put("bankId", erpBankId);
					rushMap2.put("importType", erpImportType);
					rushMap2.put("billNo", billNo);//对方的凭证号
					rushList.add(rushMap2);
					//bankErpList.remove(tempMap);//互为冲销凭证
					break;
				}
			}
			if (flag) {
				//bankErpList.remove(ctrMap);//互为冲销凭证
				//break;
			}
		}
		//反写票据信息
		updateComparesignRushVoucher(rushList, FinacialConstant.COMPARE_SIGN_YES);
	}
	/**
	 * <br>
	 * <b>功能：</b>更新互为冲销的凭证<br>
	------------------------------------------------------------------------------------------
	 */
	public void updateComparesignRushVoucher(List<Map<String, Object>> rushList, String compareSign) {
		for (Map<String, Object> rushMap : rushList) {
			fcCompareErpDao.updateComparesignRushVoucher(rushMap, compareSign);
		}
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>根据核对标准与ERP数据进行比对<br>
	 ----------------------------------------------------------------------------------------
	 */
	public void compareWithErpBankAcc( List<FcCheckOverBean> checkOverList,
									   FcBankBillVo bankBill, List<FcCompareAccVO> bankErpList, String checkType) {
		//对账标识编号
		Date date=DateUtils.getDate();//获取当前时间
		long number=getRadomNumber(6);
		String checkCode=DateUtils.getDateStr(date,"yyyy-MM-dd")+number;
		String bankId = (String) bankBill.getId();//序号
		String importType = (String) bankBill.getImportType();//输入类型
		FcBankBill bankBillPK = new FcBankBill();
		bankBillPK.setBankId(bankId);
		bankBillPK.setImportType(importType);
		String bankAcc = (String) bankBill.getBankAcc();//银行账号
		Date bankTallyDate = (Date) bankBill.getTallyDate();//银行发生时间
		String bankErpVoucherNo = StringUtils.formatNull((String) bankBill.getVoucherNo());//ERP票据号
		String bankVoucherNo = StringUtils.formatNull((String) bankBill.getErpVoucherNo());//银行票据号
		BigDecimal bankDebitMoney = (BigDecimal) bankBill.getDebitMoney();//银行借方发生额
		BigDecimal bankCreditMoney = (BigDecimal) bankBill.getCreditMoney();//银行贷方发生额
		String bankContent = (String) bankBill.getSummaryContent();//银行摘要
		String bankPayerAccNo = (String) bankBill.getBankAcc();//银行付款方账号
		String bankPayeeAccNo = (String) bankBill.getPayeeAcc();//银行收款方账号
		String curId = bankBill.getCurId();
		
		boolean flag = false;//本次核对标志
		FcCompareAcc compareAcc = new FcCompareAcc();
		BigDecimal erpDebitMoney = BigDecimal.ZERO;//中心借方发生额
		BigDecimal erpCreditMoney = BigDecimal.ZERO;//中心贷方发生额
		Date erpTallyDate = null;//ERP发生时间
		String erpVoucherNo = "";//ERP票据号
		String erpImportType = "";//ERP导入类型
		String erpBankId = "";//ERP银行ID
		String erpContent = "";//ERP摘要
		String erpPayerAccNo = "";//ERP付款方账号
		String erpPayeeAccNo = "";//ERP收款方账号
		String ctrId = "";
		for (FcCompareAccVO ctrMap : bankErpList) {
			erpDebitMoney = (BigDecimal) ctrMap.getDebitMoney();
			erpCreditMoney = (BigDecimal) ctrMap.getCreditMoney();
			erpTallyDate = (Date) ctrMap.getTallyDate();
			erpVoucherNo = StringUtils.formatNull((String) ctrMap.getVoucherNo());
			erpImportType = (String) ctrMap.getImportType();
			erpBankId = (String) ctrMap.getBankId();
			erpContent = StringUtils.formatNull((String) ctrMap.getSummaryContent());
			erpPayerAccNo = (String) ctrMap.getBankAcc();
			erpPayeeAccNo = (String) ctrMap.getPayeeAcc();
			ctrId = ctrMap.getId();
			if (FinacialConstant.CheckType.TYPE_MONEY.get().equals(checkType)) {//发生额核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_MONEY_BILLNO.get().equals(checkType)) {//发生额和票据号核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& bankVoucherNo!=null && erpVoucherNo.equals(bankVoucherNo)) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_TIME_MONEY.get().equals(checkType)) {//时间与发生额核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& erpTallyDate.compareTo(bankTallyDate) == 0) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_TIME_MONEY_BILLNO.get().equals(checkType)) {//时间发生额票据号核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& erpTallyDate.compareTo(bankTallyDate) == 0 && bankVoucherNo!=null && erpVoucherNo.equals(bankVoucherNo)&&bankVoucherNo!=null) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_VOUCHERNO.get().equals(checkType)){//按凭证号核对
				if (StringUtils.isNotBlank(bankErpVoucherNo) && StringUtils.isNotBlank(erpVoucherNo)
						&& erpVoucherNo.equals(bankErpVoucherNo)){
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_MONEY_SAP_VOUCHERNO.get().equals(checkType)){//按发生额和SAP凭证号核对
				//付款 单位金额=银行金额并且单位凭证号=银行摘要中包含单位的凭证号
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& StringUtils.isNotBlank(erpVoucherNo) && StringUtils.isNotBlank(bankContent)
						&& bankContent.indexOf(erpVoucherNo) != -1) {
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				} else if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0 &&
						StringUtils.isNotBlank(bankErpVoucherNo) && StringUtils.isNotBlank(erpVoucherNo)
						&& erpVoucherNo.equals(bankErpVoucherNo)) {
					//收款 单位金额=银行金额并且单位凭证号=银行记录的凭证号
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_TIME_ABS_MONEY.get().equals(checkType)) {//按时间与摘要与发生额核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& erpContent.equals(bankContent) && erpTallyDate.compareTo(bankTallyDate) == 0) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			} else if (FinacialConstant.CheckType.TYPE_TIME_ACC_ABS_MONEY.get().equals(checkType)) {//按时间+付款方账号+收款方账号+摘要+发生额核对
				if (erpCreditMoney.compareTo(bankDebitMoney) == 0 && erpDebitMoney.compareTo(bankCreditMoney) == 0
						&& erpContent.equals(bankContent) && erpPayerAccNo.equals(bankPayerAccNo)
						&& erpPayeeAccNo.equals(bankPayeeAccNo) && erpTallyDate.compareTo(bankTallyDate) == 0) {//核对成功
					flag = true;//记对账标志为已对账
					bankErpList.remove(ctrMap);
					break;
				}
			}
		}
		if (flag) {
			compareAcc = new FcCompareAcc();
			compareAcc.setBankId(erpBankId);
			compareAcc.setImportType(erpImportType);
			checkOverList.add(new FcCheckOverBean(bankBill.getId(),ctrId,bankAcc,curId,bankId,importType));
		}else{
			//unOveredDatas.add(bankBillPK);
		}
	}
	/**
	 * 得到随机数，指定随机数长度
	 * @param length 随机数长度
	 * @return--------------------------------------------------------------------
	 */
	public static synchronized long getRadomNumber(int length){
		long first = 1;
		long second = 1;
		for(int i=1;i<length;i++){
			second *= 10;
		}
		first = second * 10;
		first = first - second - 1;
		long number = Math.round(Math.random()*first+second);
		return number;
	}
	
	
	/**
	 * <b>功能介绍：</b>更新对账标志为已对账<br>
	-----------------------------------------------------------------------------------
	 */
	public void updateCheckErpOverCompareSign(List<FcCheckOverBean> checkOverList, String logId) {
		if (checkOverList.size() == 0) {
            return;
        }
		List<String> bankBillPKList = new ArrayList<String>();
		List<String> compareAccPKList = new ArrayList<String>();
		List<FcBankCheckDtl> checkList = new ArrayList<FcBankCheckDtl>();
		
		for (FcCheckOverBean overBean : checkOverList) {
			bankBillPKList.add(overBean.getBankBillPK());
			compareAccPKList.add(overBean.getCompareAccPK());//erpPK
			//checkList.add(new FcBankCheckDtl(overBean.getBankAcc(),logId,overBean.getBankBillPK(),CheckConstant.LOGTYPE_TYPE_ERP));
			checkList.add(new FcBankCheckDtl(overBean.getCurId(),overBean.getBankAcc(), logId, overBean.getBankBillPK(), CheckConstant.LOGTYPE_TYPE_ERP, overBean.getCompareAccPK()));
			
		}
		//更新对账标志并插入对账明细
		fcBankBillService.updateComparesignOverCheck(bankBillPKList,FinacialConstant.COMPARE_SIGN_YES);
		fcCompareErpDao.updateComparesignOverCheck(compareAccPKList,FinacialConstant.COMPARE_SIGN_YES);
		fcBankCheckDtlService.batchSave(checkList);
	}
	
	/**
	 * <b>功能介绍：</b>构建对账日志<br>
	 * <b>日期：</b>2017年9月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param account
	 * @param opType
	 * @param paramType 对账类型
	 * @param checkType 核对标准
	 * @return--------------------------------------------------------
	 */
	public FcBankCheckLog buildCheckLog(String bankAcc, String account, String opType,String paramType,String checkType,ICurrentUser user){
		FcBankCheckLog log = new FcBankCheckLog();
		log.setAccount(account);
		log.setBankAcc(bankAcc);
		log.setCheckType(paramType);//对账类型
		log.setCheckFlag(checkType);//核对标准
		log.setOpCode(user.getLoginName());
		log.setOpDate(DateUtils.getDate());
		log.setOpName(user.getUsername());
		log.setOpType(opType);
		log.setCreateBy(user.getLoginName());
		log.setCreateTime(DateUtils.getDate());
		return log;
	}
	
	/**
	 * <b>功能介绍:获取调节平衡余额</b><br>
	---------------------------------------------------------------------------------------------------
	 * @version 1.0
	 */
	public Map<String, Object> loadAdjustErpBalance(BigDecimal blnBalance, BigDecimal bankBalance, String bankAcc,
            String account, Date transDate, String curId) {
		Map<String, Object> model = new HashMap<String, Object>();
		/*VBankAccInfoAll accInfoAll = bankAccService.getInfoAllByBankAcc(bankAcc);
		model.put("accName", accInfoAll.getAccName());
		model.put("bankName", accInfoAll.getBankName());*/
		//ERP未达账数据
		Map<String, Object> erpStayTotal = queryStayOverErpSumCc(transDate, account,bankAcc, null, curId);
		BigDecimal ctrPayMoney = (BigDecimal) erpStayTotal.get("creditMoney");//ERP已付、银行未付 贷
		BigDecimal ctrRecMoney = (BigDecimal) erpStayTotal.get("debitMoney");//ERP已收、银行未收  借
		model.put("ctrPayMoney", Double.valueOf(ctrPayMoney.doubleValue()));
		
        //银行未达账数据
        Map<String, Object> bankStayTotal = queryStayOverErpBankSum(transDate, bankAcc, null, curId);
        BigDecimal bankPayMoney = (BigDecimal) bankStayTotal.get("debitMoney");//银行已付、中心未付 借
        BigDecimal bankRecMoney = (BigDecimal) bankStayTotal.get("creditMoney");//银行已收、中心未收 贷
        model.put("bankPayMoney", Double.valueOf(bankPayMoney.doubleValue()));
        model.put("bankRecMoney", Double.valueOf(bankRecMoney.doubleValue()));
        
        BigDecimal blnAdjustBalance = blnBalance.add(bankRecMoney).subtract(bankPayMoney);//单位调节平衡数
        BigDecimal bankAdjustBalance = bankBalance.add(ctrRecMoney).subtract(ctrPayMoney);//银行调节平衡数
        model.put("blnAdjustBalance", Double.valueOf(blnAdjustBalance.doubleValue()));
        model.put("bankAdjustBalance", Double.valueOf(bankAdjustBalance.doubleValue()));
        model.put("blnBalance", Double.valueOf(blnBalance.doubleValue()));
        model.put("bankBalance", Double.valueOf(bankBalance.doubleValue()));
        model.put("bankAcc", bankAcc);
        model.put("nowDate", DateUtils.date2Str(DateUtils.date_sdf()));
		return model;
	}
	/**
	 * <br>
	 * <b>功能：</b>获取银行未达账金额合计(长江财险)<br>
------------------------------------------------------------------------------------------------
	 */
	public Map<String, Object> queryStayOverErpSumCc(Date transDate, String account,String bankAcc, String overType, String curId) {
		return fcCompareErpDao.queryStayOverErpBankSumCc(transDate,account, bankAcc, overType, curId);
	}
	/**
	 * <br>
	 * <b>功能：</b>获取ERP银行未达账金额合计<br>
	---------------------------------------------------------------------------------------------
	 */
	public Map<String, Object> queryStayOverErpBankSum(Date transDate, String bankAcc, String overType, String curId) {
		return fcCompareErpDao.queryStayOverErpBankSum(transDate, bankAcc, overType, curId);
	}
	/**
	 * 
	 * <b>功能介绍:手工勾兑</b><br>
	-----------------------------------------------------------------------------------------
	 */
	public void doHandOver(String account,String bankAcc,String ctrPk,String bankPk,Date checkDate_s,Date transDate_s,
			String opType,String paramType,ICurrentUser user){
		String[] ctrPkIds = ctrPk.split(",");
		String[] bankPkIds = bankPk.split(",");
		List<String> compareAccPKList = new ArrayList<String>();
		List<String> bankBillPKList = new ArrayList<String>();
		for (String ctrPkId : ctrPkIds) {
			compareAccPKList.add(ctrPkId);
		}
		for (String bankPkId : bankPkIds) {
			bankBillPKList.add(bankPkId);
		}
		FcBankCheckLog checkLog = null;
		//勾对日志
		if (StringUtils.isNotEmpty(opType) && "2".equals(opType)) {//反勾兑
			checkLog = buildCheckLog(bankAcc, account, FinacialConstant.COMPARE_SIGN_NO, FinacialConstant.CHECK_TYPE_ERP,paramType,user);
		} else if (StringUtils.isNotEmpty(opType) && "3".equals(opType)) {//确认未达
			checkLog = buildCheckLog(bankAcc, account, FinacialConstant.COMPARE_SIGN_STAY, FinacialConstant.CHECK_TYPE_ERP,paramType,user);
		} else if (StringUtils.isNotEmpty(opType) && "4".equals(opType)) {//未达还原
			checkLog = buildCheckLog(bankAcc, account, FinacialConstant.COMPARE_SIGN_NO, FinacialConstant.CHECK_TYPE_ERP,paramType,user);
		} else {
			checkLog = buildCheckLog(bankAcc, account, FinacialConstant.COMPARE_SIGN_HAND, FinacialConstant.CHECK_TYPE_ERP,paramType,user);
		}
		checkLog.setBeginDate(DateUtils.addDay(checkDate_s, 1));
		checkLog.setEndDate(transDate_s);
		
		
		String compareSign = FinacialConstant.COMPARE_SIGN_HAND;
	    if (StringUtils.isNotEmpty(opType) && "2".equals(opType)) {//反勾对
	        compareSign = FinacialConstant.COMPARE_SIGN_NO;
        } else if (StringUtils.isNotEmpty(opType) && "3".equals(opType)) {//确认未达
        	 compareSign = FinacialConstant.COMPARE_SIGN_STAY;
        } else if (StringUtils.isNotEmpty(opType) && "4".equals(opType)) {//未达还原
        	 compareSign = FinacialConstant.COMPARE_SIGN_NO;
        }
	    //更新对账标志为手工勾对
	    if(!EmptyUtils.isEmptyList(compareAccPKList)){
	    	fcCompareErpDao.updateComparesignOverCheck(compareAccPKList, compareSign);
	    }
	    if(!EmptyUtils.isEmptyList(bankBillPKList)){
	    	fcBankBillService.updateComparesignOverCheck(bankBillPKList, compareSign);
	    }
		
	}
	/**
	 * 
	 * <b>功能介绍:结转操作</b><br>
	---------------------------------------------------------------------------------------
	 */
	public String doAdjustOverOther(FcBankCheckSearchVo fca,Date checkDate,ICurrentUser user,String checkSign) throws Exception{
		String curId = fca.getCurId();
		String bankAcc = fca.getBankAcc();
        String account = fca.getAccount();
        Date transDate = fca.getTransTime();
        String paramType = fca.getParamType();
        BigDecimal blnBalance = fca.getBlnBalance();
        BigDecimal bankBalance = fca.getBankBalance();
        Map<String, Object> result = new HashMap<String, Object>();
        //余额调节表
        FcBankBalanceAdjust balAdjust = new FcBankBalanceAdjust();
        balAdjust.setAccount(account);
        balAdjust.setBankAcc(bankAcc);
        balAdjust.setCurCode(fca.getCurId());
        balAdjust.setAdjustDate(new Date());
        balAdjust.setBeginDate(DateUtils.addDay(checkDate, 1));//余额调节表开始日期为上次结转的下一天
        balAdjust.setEndDate(transDate);
        balAdjust.setCheckType(paramType);//对账类型
        balAdjust.setBlnInitBalance(blnBalance);
        balAdjust.setBankInitBalance(bankBalance);
        balAdjust.setAdjCode(user.getUsername());
        
        
        List<FcBankBalanceAdjustDetail> details = new ArrayList<FcBankBalanceAdjustDetail>();//余额调节表明细
		//erp
		balAdjust.setBankInitBalance(bankBalance);
		balAdjust.setBlnInitBalance(blnBalance);
		//判断余额是否平衡
		result = loadAdjustErpBalance(blnBalance, bankBalance, bankAcc, account, transDate, curId);
		Double blnAdjustBalanceStr = (Double) result.get("blnAdjustBalance");
		Double bankAdjustBalanceStr = (Double) result.get("bankAdjustBalance");
		BigDecimal blnAdjustBalance = BigDecimal.valueOf(blnAdjustBalanceStr);
		BigDecimal bankAdjustBalance = BigDecimal.valueOf(bankAdjustBalanceStr);
		if (blnAdjustBalance.compareTo(bankAdjustBalance) != 0) {
			//logger.debug("银行和ERP调节平衡数不平衡，请核对数据再结转！");
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_ERPCHECK_OVER_NOTBALANCE);
		}
		balAdjust.setBankAdjBal(bankAdjustBalance);
		balAdjust.setBlnAdjBalance(blnAdjustBalance);
		balAdjust.setCreateBy(user.getLoginName());
		//保存余额调节表
		fcBankBalanceAdjustService.save(balAdjust);
		/**
		 * 保存ERP和银行对账单数据到历史数据
		 */
		//saveAdjustErpHisDatas(transDate, bankAcc, balAdjust.getAdId());
		//billService.saveAdjustCntrHisDatas(transDate, bankAcc, balAdjust.getAdId());
		this.saveAdjustCntrProcDatas(transDate, bankAcc, balAdjust.getId(), curId);
		this.saveAdjustBankProcDatas(transDate, bankAcc, balAdjust.getId(), curId);
		// ERP上期留存数据
		List<FcCompareAccVO> ctrStayList = queryUnOverErpStayDatas(bankAcc, null, transDate, curId);
		for(FcCompareAccVO ctrStay : ctrStayList){
			FcBankBalanceAdjustDetail dtl = new FcBankBalanceAdjustDetail();
			dtl.setTallyDate(ctrStay.getTallyDate());
			dtl.setBankAcc(bankAcc);
			dtl.setBillNo(ctrStay.getBillNo()== null ? "" : ctrStay.getBillNo().toString());
			dtl.setCreditMoney(ctrStay.getCreditMoney());
			dtl.setDebitMoney(ctrStay.getDebitMoney());
			dtl.setRedblueSign(ctrStay.getRedblueSign());
			dtl.setSummaryContent(ctrStay.getSummaryContent() == null ? "" : ctrStay.getSummaryContent().toString());
			dtl.setVoucherNo(ctrStay.getVoucherNo() == null ? "" : ctrStay.getVoucherNo().toString());
			dtl.setAjustId(balAdjust.getId());
			dtl.setCurCode(ctrStay.getCurId());
			dtl.setParamType(CheckConstant.ERP_DATA);
			details.add(dtl);
		}
		updateOverStayAccInfos(bankAcc, transDate, curId);//结转ERP未达数据为未对账
		updateOverBankInfos(bankAcc, transDate, curId);//结转ERP对账单 已对账和手工勾对的为已结转
		//修改账户初始化信息-结转日期
		fcCheckInitService.updateCheckInitCarryDate(bankAcc, account,transDate,FinacialConstant.CHECK_TYPE_ERP);
	
		List<FcBankBillVo> bankStayList = fcBankBillService.queryUnOverCtrStayDatas(bankAcc, null, transDate, curId);
        for(FcBankBillVo bankStay : bankStayList){
            FcBankBalanceAdjustDetail dtl = new FcBankBalanceAdjustDetail();
            dtl.setTallyDate(bankStay.getTallyDate());
            dtl.setBankAcc(bankAcc);
            dtl.setBillNo(bankStay.getBillNo() == null ? "" : bankStay.getBillNo());
            dtl.setCreditMoney(bankStay.getCreditMoney());
            dtl.setDebitMoney(bankStay.getDebitMoney());
            dtl.setRedblueSign(bankStay.getRedblueSign());
            dtl.setSummaryContent(bankStay.getSummaryContent() == null ? "" : bankStay.getSummaryContent());
            dtl.setVoucherNo(bankStay.getVoucherNo() == null ? "" : bankStay.getVoucherNo());
            dtl.setParamType(CheckConstant.BANK_DATA);
            dtl.setAjustId(balAdjust.getId());
            dtl.setCurCode(curId);
            details.add(dtl);//添加明细
        }
        
        adjustDetailService.batchSave(details);//保存明细
        
        //结转银行未达数据
        fcBankBillService.updateOverStayBankInfos(bankAcc, transDate, curId);
        //结转银行对账单
        fcBankBillService.updateOverBankInfos(bankAcc, transDate, curId);
        //保存结转日志
        FcBankCheckLog checkLog = buildCheckLog(bankAcc, account, CheckConstant.OPTYPE_TYPE_OVER,fca.getParamType(),fca.getCheckType(),user);
        checkLog.setBillId(balAdjust.getId());
        checkLog.setCurCode(fca.getCurId());
        checkLog.setBeginDate(balAdjust.getBeginDate());
        checkLog.setEndDate(balAdjust.getEndDate());
        fcBankCheckLogService.save(checkLog);
        return balAdjust.getId();//设置结转ID
	}
	/**
	 * <br>
	 * <b>功能：</b>保存银行对账单数据到历史数据<br>
	 *-----------------------------------------------------------------------------
	 */
	public void saveAdjustErpHisDatas(Date transDate, String bankAcc, Long adjustId) {
		fcCompareErpDao.saveAdjustErpHisDatas(transDate, bankAcc, adjustId);
	}
	/**
	 * <br>
	 * <b>功能：</b>结转中心未达数据<br>
---------------------------------------------------------------------------------------
	 */
	public void updateOverStayAccInfos(String bankAcc, Date transDate, String curId){
		fcCompareErpDao.updateOverStayAccInfos(bankAcc, transDate, curId);
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>结转ERP对账数据<br>
	 *     ------------------------------------------------------------------------------
	 */
	public void updateOverBankInfos(String bankAcc, Date transDate, String curId){
		fcCompareErpDao.updateOverBankInfos(bankAcc, transDate, curId);
	}
	/**
	 * <b>功能介绍：</b>保存erp对账单数据到结转表<br>
-------------------------------------------------------------------------------
	 */
	public void saveAdjustCntrProcDatas(Date transDate, String account, String adjustId, String curId){
		List<FcCompareAccVO> compareAccIds = fcCompareErpDao.getctrDataOverData(transDate, account,FinacialConstant.CHECK_TYPE_ERP, curId);
		List<FcCompareAccProc> procs = new ArrayList<FcCompareAccProc>();
		for (FcCompareAccVO compare : compareAccIds) {
			FcCompareAccProc proc= new FcCompareAccProc();
			proc.setAjustId(adjustId);
			proc.setFcCompareAccId(compare.getId());//erp对账单id
			proc.setCompareSignHis(compare.getCompareSign());//erp对账标志
			procs.add(proc);
		}
		fcCompareAccProcService.batchSave(procs);
	}
	
	/**
	 * <b>功能介绍：</b>保存银行对账单数据到结转表<br>
	 -----------------------------------------------------------------------------
	 */
	public void saveAdjustBankProcDatas(Date transDate, String bankacc, String adjustId, String curId){
		List<FcBankBillVo> bankBillIds = fcBankBillService.getBankDataOverData(transDate, bankacc, curId);
		List<FcBankBillProc> procs = new ArrayList<FcBankBillProc>();
		for (FcBankBillVo bankBill : bankBillIds) {
			FcBankBillProc proc = new FcBankBillProc();
			proc.setAjustId(adjustId);
			proc.setFcBankBillId(bankBill.getId());//银行对账单ID
			proc.setCompareSignHis(bankBill.getCompareSign());//银行对账单历史对账标志
			procs.add(proc);
		}
		fcBankBillProcService.batchSave(procs);
	}
	/**
	 * <br>
	 * <b>功能：</b>查询上期留存未对账的ERP数据<br>
	-------------------------------------------------------------------------------
	 */
	public List<FcCompareAccVO> queryUnOverErpStayDatas(String bankAcc, String overType, Date transDate, String curId) {
		return fcCompareErpDao.queryUnOverErpStayDatas(bankAcc, overType, transDate, curId);
	}
	
	
	/**
	 * <b>功能介绍：</b>获取账户账面金额<br>
	 ----------------------------------------------------------------
	 */
	//@Transactional(readOnly = true)
	public FcBankErpBalInfoVO getAccBalInfo(String account, String bankAcc, String curId, String transDate, String paramType, String accDisplayType) throws Exception{
		FcBankErpBalInfoVO bankInfo = new FcBankErpBalInfoVO();
		bankInfo.setAccount(account);
		bankInfo.setBankAcc(bankAcc);
		bankInfo.setTransTimeStr(transDate);
		bankInfo.setParamType(paramType);
		bankInfo.setAccDisplayType(accDisplayType);
		 //根据银行账号查询账号信息
		BtBankAccDTO bankAccDTO = bankService.getBankAccInfo(bankAcc);
		 //对账结束日期
		Date tDate = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		Date lastOverDate = null;
		BigDecimal bankInitBal = BigDecimal.ZERO;//银行账面初始余额
		BigDecimal erpInitBal = BigDecimal.ZERO;//ERP账面初始余额		
		 //获取账户上次结转日期
		FcBankBalanceAdjust adjust = fcCompareErpDao.getLastAdjustOver(account, bankAcc, curId);
		 //通过账号获取账户余额初始化信息
        FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(bankAcc, account, FinacialConstant.CHECK_TYPE_ERP, curId);
		if(EmptyUtils.isEmpty(adjust)){
			if(EmptyUtils.isEmpty(fcCheckInit)){//还未初始化账户余额，请先初始化账户余额
				//logger.debug("银行账号：[" + bankAcc + "],erp科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!");
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_ERPCHECK_BALNOTINIT, bankAcc, account);
			}
			lastOverDate = fcCheckInit.getCheckDate();
			bankInitBal = fcCheckInit.getBankBalance();
			erpInitBal = queryErpBankAccBalance(bankAcc, tDate, curId);//查询ERP银行账户当前余额
		}else{
			//上次结转的余额
			lastOverDate = adjust.getEndDate();//上次结转日期
			if (fcCheckInit != null) {
				Date checkDate = fcCheckInit.getCheckDate();
				if (checkDate != null && checkDate.after(lastOverDate)) {
					lastOverDate = checkDate;
					bankInitBal = fcCheckInit.getBankBalance();
					erpInitBal = queryErpBankAccBalance(bankAcc, tDate, curId);
				}else {
					bankInitBal = adjust.getBankInitBalance();
					erpInitBal = adjust.getBlnInitBalance();
				}
			} else {
				//"银行账号：[" + bankAcc + "],企业科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_ERPCHECK_BALNOTINIT,bankAcc,account);
			}
		}
		bankInfo.setLastOverDate(lastOverDate);
		String lastOverDateStr = DateUtils.date2Str(lastOverDate, DateUtils.date_sdf());
        if (tDate.before(DateUtils.addDay(lastOverDate, 1))) {//结束日期应大于上次结转日期
        	//logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE,lastOverDateStr);
        }
        BigDecimal bankBalance = BigDecimal.ZERO;//银行账面余额
        BigDecimal erpBankBalance = BigDecimal.ZERO;//ERP银行账面余额
        if ("1".equals(bankInfo.getCheckSign())) {//对账标识（批量对账）
         /*erpBankBalance = queryErpBankAccBalance(bankAcc, transDate);//ERP银行账面余额
         bankInfo.setBlnInitBalance(queryErpBankAccBalance(bankAcc, tDate));//ERP银行账面初始对账余额
         BigDecimal erpDtlBal = getTransDateErpBalance(account, lastOverDate, tDate);//ERP明细发生额
         erpBankBalance = bankInfo.getBlnInitBalance().add(erpDtlBal);//结束日期的余额
         bankInfo.setBankInitBalance(queryErpBankAccBalance(accDisplayType, bankAcc, tDate, account));//银行账面初始对账余额
         double hisBal = getImportService().getAccHisBalService().getNearestDayBankBal(bankAcc, transDate);//银行账号结束日期余额
         bankBalance = BigDecimal.valueOf(hisBal);*/
        } else {
			BigDecimal erpDtlBal = getTransErpBalance(bankAcc, lastOverDate, tDate, curId);//对账区间ERP对账单发生额
            erpBankBalance = erpInitBal.add(erpDtlBal).setScale(2, BigDecimal.ROUND_HALF_UP);//ERP账面余额
            bankBalance = bisAccBalService.getCurBalOrLastRealBal(bankAcc, curId, tDate);//取对账期末余额
            if (EmptyUtils.isEmpty(bankBalance)) {
                BigDecimal duringMoney = bisAccDtlService.getDuringMoney(curId, bankAcc, lastOverDate, tDate);//对账期间银行发生额
                bankBalance = bankInitBal.add(duringMoney).setScale(2, BigDecimal.ROUND_HALF_UP);//银行账面余额
            }
/*            BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getBisAccDtlByAccCurTime(bankAcc, curId, tDate);//取最近的明细余额作为期末余额
            if (!EmptyUtils.isEmpty(bisAccDtlDTO)) {
                bankBalance = bisAccDtlDTO.getBal();//明细余额
            } else {
                bankBalance = bisAccBalService.getCurBalOrLastBal(bankAcc, curId, tDate);
            }*/
        }
        bankInfo.setAccBal(erpBankBalance);
        bankInfo.setBankBal(bankBalance);
        return bankInfo;
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>查询ERP银行账户当前余额<br>
	 *     ------------------------------------------------------------------------
	 */
	public BigDecimal queryErpBankAccBalance(String accDisplayType, String bankAcc, Date transTime, String account, String curId) {
		BigDecimal balance = BigDecimal.ZERO;
		if(StringUtils.isBlank(account)){
			account = bankAcc;
		}
		FcCheckInit init = fcCheckInitService.getCheckInitByAccount(bankAcc, account,FinacialConstant.CHECK_TYPE_ERP, curId);
		if (init == null) {
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_ERPCHECK_BALNOTINIT,bankAcc,account);
			
		}
		Date checkDate = init.getCheckDate();
		if (checkDate.after(transTime)) {//结转日期check_date大于对账的结束日期
			balance = BigDecimal.ZERO;
		} else {
			balance = init.getBankBalance();
		}
		return balance;
	}
	/**
	 *
	 * <br>
	 * <b>功能：</b>查询ERP银行账户当前余额<br>
	 * @param bankAcc----------------------------------------
	 * @param transTime
	 * @return
	 */
	public BigDecimal queryErpBankAccBalance(String bankAcc, Date transTime, String curId) {
		BigDecimal balance = BigDecimal.ZERO;
		FcCheckInit fcCheckErpInit = fcCompareErpDao.getCheckInitByAccount(bankAcc, curId);
		if (null == fcCheckErpInit) {
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_BANKACC_IS_NULL,bankAcc);
		}
		Date checkDate = fcCheckErpInit.getCheckDate();
		if (checkDate.after(transTime)) {//结转日期checkDate大于对账的结束日期
			balance = BigDecimal.ZERO;
		} else {
			balance = fcCheckErpInit.getBlnBalance();
		}
		return balance;
	}
	/**
	 * <br>
	 * <b>功能：</b>获取erp对账单时间段发生额<br>
	 * @return--------------------------------------------------------------
	 */
	public BigDecimal getTransErpBalance(String bankAcc, Date checkDate, Date tallyDate,String curId) {
		return fcCompareErpDao.getTransErpBalance(bankAcc, checkDate, tallyDate, curId);
	}
	/**
	 * <br>------------------------------------------------------------------------------
	 * <b>功能：</b>获取银行对账单时间段发生额<br>
	 */
	public BigDecimal getTransBillBalance(String bankAcc, Date checkDate, Date tallyDate) {
		return fcCompareErpDao.getTransBillBalance(bankAcc, checkDate, tallyDate);
	}
	/**
	 * 根据开始时间，结束时间查询对账信息
	 * @param tallyStartDate
	 * @param tallyEndDate
	 * @return
	 */
	public List<FcCompareAccVO> listForEBill(Date tallyStartDate, Date tallyEndDate) {
		//查询账户
		List<String> accS = new ArrayList<>();
		String[] accTypes=new String[]{"01", "05", "08"};
		List<FcAccListVO> accList = fcAccService.getAccountByParams(accTypes,"","","");
		for (FcAccListVO acc : accList) {
			accS.add(acc.getBlnctrAcc());
		}
		return fcCompareErpDao.listForEBill(tallyStartDate,tallyEndDate,accS);
	}
    /**
     * <br>
     * <b>功能：</b> 查询ERP对账结果 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/10/15 0015 <br>
     */
    public DataTablesOutput queryCheckList(DataTablesInput input, ICurrentUser user, Date calcDate, String compareSign) {
		//查询账户
		List<String> acc = new ArrayList<>();
		List<String> accS = new ArrayList<>();
		List<String> corpAuth = new ArrayList<>();
		Map<String, BtBankAccDTO> accMap = new HashMap<>();
		String bankAcc =  (String) input.getFormdata().get("bankAcc");
		String corpS = (String)input.getFormdata().get("eqs_corpId");
		String corpIds = (String) input.getFormdata().get("ins_corpId");
		if (!EmptyUtils.isEmpty(bankAcc)) {	//将bankacc 拆分为list，拼接查询条件，避免in条件超长
			acc = StringUtils.splitToList(",",bankAcc);
//			accMap = bankService.getBankAccByInfoAccs(accS);
			corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY, user.getId());
			List<BtBankAccDTO> accList = bankService.getListByCorpIds(corpAuth);
			for(String accs:acc){
				List<String> accsList=accList.stream().map(BtBankAccDTO::getBankAcc).filter(dto->dto.equals(accs)).collect(Collectors.toList());
				if(accsList.size()>0){
					accS.add(accsList.get(0));
				}
			}
			accMap = bankService.getBankAccByInfoAccs(accS);
		}else{
			if (StringUtils.isNotEmpty(corpS)) {
				corpAuth.add(corpS);
			}else if(EmptyUtils.isNotEmpty(corpIds)){
				String[] corpIdstr = corpIds.split(",");
				for(int i=0;i<corpIdstr.length;i++){
					corpAuth.add(corpIdstr[i]);
				}
			}else{
				corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY, user.getId());
			}
			List<BtBankAccDTO> accList = bankService.getListByCorpIds(corpAuth);
			for (BtBankAccDTO dto : accList) {
				accS.add(dto.getBankAcc());
				accMap.put(dto.getBankAcc(),dto);
			}
		}
        DataTablesOutput output = fcCompareErpDao.queryCheckList(input, accS, calcDate,compareSign);
		List<FcBankCheckVO> voList=output.getData();
		if(voList.size()>0){
			//单位和币别
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			Map<String, CorpDTO> corpMap=iCorpService.listCorpDtoByCorpIds(corpAuth);
			for(FcBankCheckVO vo:voList){
				BtBankAccDTO dto =accMap.get(vo.getBankAcc());
				vo.setBankName(dto.getBankName());
				vo.setAccName(dto.getAccName());
				vo.setCorpId(dto.getCorpId());
				vo.setCurName(curMap.get(vo.getCurId()).getCurName() );
				vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
			}
		}
		return output;
    }
	/**
	 * 查询批量结转列表
	 * @param input
	 * @param user
	 * @param compareSign
	 * @return
	 */
	public DataTablesOutput queryAdjustList(DataTablesInput input, ICurrentUser user, String compareSign) {
		//查询账户
		List<String> acc = new ArrayList<>();
		List<String> accS = new ArrayList<>();
		List<String> corpAuth = new ArrayList<>();
		Map<String, BtBankAccDTO> accMap = new HashMap<>();
		String bankAcc =  (String) input.getFormdata().get("bankAcc");
		corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY, user.getId());
		if (!EmptyUtils.isEmpty(bankAcc)) {	//将bankacc 拆分为list，拼接查询条件，避免in条件超长
			acc = StringUtils.splitToList(",",bankAcc);
//			accMap = bankService.getBankAccByInfoAccs(accS);
			List<BtBankAccDTO> accList = bankService.getListByCorpIds(corpAuth);
			for(String accs:acc){
				List<String> accsList=accList.stream().map(BtBankAccDTO::getBankAcc).filter(dto->dto.equals(accs)).collect(Collectors.toList());
				if(accsList.size()>0){
					accS.add(accsList.get(0));
				}
			}
			accMap = bankService.getBankAccByInfoAccs(accS);
		}else{
			List<BtBankAccDTO> accList = bankService.getListByCorpIds(corpAuth);
			for (BtBankAccDTO dto : accList) {
				accS.add(dto.getBankAcc());
				accMap.put(dto.getBankAcc(),dto);
			}
		}
		String transTime =(String)input.getFormdata().get("transTime");
		String compareCalDateString = new StringBuilder("bt_to_date('").append(transTime).append("','yyyy-MM-dd')").toString();
		String account =  (String) input.getFormdata().get("account");
		//获取结转基本信息（账户初始化余额、对账日期、未对账笔数等）
		DataTablesOutput output = fcCompareErpDao.getAdjustInfo(input,compareCalDateString,accS,transTime,compareSign);
		try {
			List<FcBankCheckVO> bankCheckList = output.getData();
			if(bankCheckList.size()>0) {
				//单位和币别
				Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
				Map<String, CorpDTO> corpMap = iCorpService.listCorpDtoByCorpIds(corpAuth);
				//根据结转基本信息，补充账户企业账面余额、银行账面余额
				for (FcBankCheckVO checkVO : bankCheckList) {
					BtBankAccDTO dto =accMap.get(checkVO.getBankAcc());
					checkVO.setBankName(dto.getBankName());
					checkVO.setAccName(dto.getAccName());
					checkVO.setCorpId(dto.getCorpId());
					checkVO.setCurName(curMap.get(checkVO.getCurId()).getCurName() );
					checkVO.setCorpName(corpMap.get(checkVO.getCorpId()).getName());
					FcBankErpBalInfoVO fcBankErpBalInfoVO = this.getAccBalInfoForList(checkVO.getAccount(), checkVO.getBankAcc(), checkVO.getCurId(), transTime, "1", "0");
					if (!EmptyUtils.isEmpty(fcBankErpBalInfoVO)) {
						checkVO.setBlnAdjBal(fcBankErpBalInfoVO.getAccBal());
						checkVO.setBankAdjBal(fcBankErpBalInfoVO.getBankBal());
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return output;
	}
	/**
	 * <b>功能介绍：</b>获取账户账面金额<br>
	 ----------------------------------------------------------------
	 */
	//@Transactional(readOnly = true)
	public FcBankErpBalInfoVO getAccBalInfoForList(String account, String bankAcc, String curId,String transDate,String paramType,String accDisplayType) throws Exception{
		FcBankErpBalInfoVO bankInfo = new FcBankErpBalInfoVO();
		bankInfo.setAccount(account);
		bankInfo.setBankAcc(bankAcc);
		bankInfo.setTransTimeStr(transDate);
		bankInfo.setParamType(paramType);
		bankInfo.setAccDisplayType(accDisplayType);
		//根据银行账号查询账号信息
		BtBankAccDTO bankAccDTO = bankService.getBankAccInfo(bankAcc);
		//对账结束日期
		Date tDate = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		Date lastOverDate = null;
		BigDecimal bankInitBal = BigDecimal.ZERO;//银行账面初始余额
		BigDecimal erpInitBal = BigDecimal.ZERO;//ERP账面初始余额
		//获取账户上次结转日期
		FcBankBalanceAdjust adjust = fcCompareErpDao.getLastAdjustOver(account, bankAcc, curId);
		//通过账号获取账户余额初始化信息
		FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(bankAcc, account, FinacialConstant.CHECK_TYPE_ERP, curId);
		if(EmptyUtils.isEmpty(adjust)){
			lastOverDate = fcCheckInit.getCheckDate();
			bankInitBal = fcCheckInit.getBankBalance();
			erpInitBal = queryErpBankAccBalance(bankAcc, tDate, curId);//查询ERP银行账户当前余额
		}else{
			//上次结转的余额
			lastOverDate = adjust.getEndDate();//上次结转日期
			if (fcCheckInit != null) {
				Date checkDate = fcCheckInit.getCheckDate();
				if (checkDate != null && checkDate.after(lastOverDate)) {
					lastOverDate = checkDate;
					bankInitBal = fcCheckInit.getBankBalance();
					erpInitBal = queryErpBankAccBalance(bankAcc, tDate, curId);
				}else {
					bankInitBal = adjust.getBankInitBalance();
					erpInitBal = adjust.getBlnInitBalance();
				}
			}
		}
		bankInfo.setLastOverDate(lastOverDate);
		String lastOverDateStr = DateUtils.date2Str(lastOverDate, DateUtils.date_sdf());
		BigDecimal bankBalance = BigDecimal.ZERO;//银行账面余额
		BigDecimal erpBankBalance = BigDecimal.ZERO;//ERP银行账面余额
		if ("1".equals(bankInfo.getCheckSign())) {//对账标识（批量对账）
		} else {
			BigDecimal erpDtlBal = getTransErpBalance(bankAcc, lastOverDate, tDate, curId);//对账区间ERP对账单发生额
			erpBankBalance = erpInitBal.add(erpDtlBal).setScale(2, BigDecimal.ROUND_HALF_UP);//ERP账面余额
            bankBalance = bisAccBalService.getCurBalOrLastRealBal(bankAcc, curId, tDate);//取对账期末余额
            if (EmptyUtils.isEmpty(bankBalance)) {
                BigDecimal duringMoney = bisAccDtlService.getDuringMoney(curId, bankAcc, lastOverDate, tDate);//对账期间银行发生额
                bankBalance = bankInitBal.add(duringMoney).setScale(2, BigDecimal.ROUND_HALF_UP);//银行账面余额
            }
		}
		bankInfo.setAccBal(erpBankBalance);
		bankInfo.setBankBal(bankBalance);
		return bankInfo;
	}
	/**
	 * <br>
	 * <b>功能：</b> 查下已初始化对账余额的ERP单位科目对账情况 <br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2022/9/22 <br>
	 */
	public DataTablesOutput queryCorpCheckList(DataTablesInput input, ICurrentUser user, Date calcDate, String compareSign) {
		List<String> corpAuth = new ArrayList<>();
		if (StringUtils.isNotEmpty(input.getFormdata().get("eqs_corpId"))) {
			corpAuth.add(input.getFormdata().get("eqs_corpId").toString());
			input.getFormdata().remove("eqs_corpId");
		}else{
			corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_BALANCE_ADJUST_QUERY, user.getId());
		}
		//查询账户
		List<String> accS = new ArrayList<>();
		Map<String, BtBankAccDTO> accMap = new HashMap<>();
		List<BtBankAccDTO> accList = bankService.getListByCorpIds(corpAuth);
		for (BtBankAccDTO acc : accList) {
			accS.add(acc.getBankAcc());
			accMap.put(acc.getBankAcc(),acc);
		}
		DataTablesOutput output = fcCompareErpDao.queryCorpCheckList(input, accS, calcDate,compareSign);
		List<FcBankCheckVO> voList=output.getData();
		if(voList.size()>0){
			//单位和币别
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			Map<String, CorpDTO> corpMap=iCorpService.listCorpDtoByCorpIds(corpAuth);
			for(FcBankCheckVO vo:voList){
				vo.setCurName(curMap.get(vo.getCurId()).getCurName() );
				vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
				BtBankAccDTO acc =accMap.get(vo.getBankAcc());
				vo.setBankName(acc.getBankName());
				vo.setAccName(acc.getAccName());
			}
		}
		return output;
	}
	/**
	 *  获取该用户的单位下面的所有银行账号的查询明细和查询余额权限是否一致
	 *  @param currentUser 操作用户
	 */
	public boolean getPermissionByCorp(String corpId, ICurrentUser currentUser){
		//账号查询对象
		AccountSearchDTO accountSearchDTO = new AccountSearchDTO();
		//币别
		//accountSearchDTO.setCurId(bean.getCurId());
		//取该用户下有操作权限单位下的所选的银行类型的在线账号
		//单位id
		accountSearchDTO.setCorpId(corpId);
		//查询类型 银行账号
		accountSearchDTO.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
		//查询明细的权限
		accountSearchDTO.setPermission(AccountGrantConstant.QUERY_DETAIL);
		//查询结果是否包含已注销账户
		accountSearchDTO.setContainDelBankAcc(false);
		//操作用户
		accountSearchDTO.setUserId(currentUser.getId());
		//符合条件的账号
		List<AccountDTO> accountDTOS = accountGrantService.listBy(accountSearchDTO);
		//查询余额的权限
		accountSearchDTO.setPermission(AccountGrantConstant.QUERY_BALANCE);
		//符合条件的账号
		List<AccountDTO> accountDTOS2 = accountGrantService.listBy(accountSearchDTO);
		if(accountDTOS.size()==accountDTOS2.size() && accountDTOS.size()>0) {
			return true;
		}else{
			return false;
		}
	}
}
