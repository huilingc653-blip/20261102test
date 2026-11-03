package com.bytter.check.service;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.finacial.api.constant.CheckConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.FcAccBalanceDTO;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.check.api.IFcCompareCenterService;
import com.bytter.check.dao.FcCompareCenterDao;
import com.bytter.check.dto.FcCompareAccBeanDTO;
import com.bytter.check.entity.*;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Service
* @Description: FC_COMPARE_ACC
* @author onlineGenerator
* @date 2017-05-31 14:43:17
* @version 1.0
*
*/
@Service("fcCompareCenterService")
@DubboService
@Transactional
public class FcCompareCenterService extends BaseService<FcCompareAcc,String> implements IFcCompareCenterService {
	@Autowired
	private FcCompareCenterDao fcCompareAccDao;
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
	private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
	private IBisAccBalService accBalService;
	@DubboReference(check = false)
	private IFcAccService fcAccService;
	@DubboReference(check = false)
	private IFcAccBalanceService fcAccBalanceService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
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
		return fcCompareAccDao;
	}
	
	/**
	 * <b>功能介绍：</b>初始化网点<br>
	 * <b>日期：</b>2017年9月19日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public String getInitNetId(String resouce,String userId){
		List<CorpDTO> authCorps = iCorpService.listUserCorp(resouce,userId);
		List<CorpDTO> authNets = new ArrayList<CorpDTO>();
		for (CorpDTO corp : authCorps) {
			if(CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
				authNets.add(corp);
			}
		}
		Collections.sort(authNets, new Comparator<CorpDTO>(){//网点排序
			public int compare(CorpDTO o1, CorpDTO o2) {
				return o1.getSort().compareTo(o2.getSort());
			}
		});
		if(EmptyUtils.isEmptyList(authNets)){//没有可操作的网点
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_NETNOTAUTH);
		}
		return authNets.get(0).getId(); 
	}
	
	/**
	 * <b>功能介绍：</b>没有权限的网点<br>
	 * <b>日期：</b>2017年11月13日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	public String getNotAuthNets(String resouce,String userId){
		List<CorpDTO> authCorps = iCorpService.listUserCorp(resouce,userId);
		List<String> authNets = new ArrayList<String>();
		List<CorpDTO> allNet = iCorpService.listNet();
		List<String> allNets = new ArrayList<String>();
		String notAuthNet = "";
		for (CorpDTO corp : authCorps) {
			if(CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())){
				authNets.add(corp.getId());
			}
		}
		for (CorpDTO net : allNet) {
			allNets.add(net.getId());
		}
		
		allNets.removeAll(authNets);
		for (String corpId : allNets) {
			notAuthNet += corpId +",";
		}
		return notAuthNet;
	}
	
	/**
	 * <b>功能介绍：</b>获取网点日结日期<br>
	 * <b>日期：</b>2017年9月26日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param netId
	 * @return
	 */
	public Date getDayOverByNetId(String netId){
		return iFcDayCloseService.getDayCloseFinalDate(netId);
	}
	
	/**
	 * <b>功能介绍:查询中心对账数据(未达、已对账、已勾兑、已对账等)</b><br>
	 * <b>日期：</b>2017年6月9日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> queryCntrCheckDatas(String bankAcc,String account,Date transDate,String[] compareSigns,BigDecimal beginMoney,BigDecimal endMoney){
        if (0 == compareSigns.length) {
            return new ArrayList<>();
        }
		return fcCompareAccDao.queryCntrCheckDatas(bankAcc,account,transDate,compareSigns,beginMoney,endMoney);
	}
	
	/**
	 * <b>功能介绍:获取对账的银行账号</b><br>
	 * <b>日期：</b>2017年6月9日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	@Transactional(readOnly = true)
	public List<Map<String,Object>> getBankAccByOpType(String bankAcc,String netCode,String natureCode){
    	return btBankAccService.getBankAccByOpType(bankAcc,netCode,natureCode);
    }
	
	/**
	 * 功能介绍:查询中心对帐数据<br>
	 * <b>日期：</b>2017年6月8日<br>
	 * @version 1.0
	 * @author caozx
	 */
	@Transactional(readOnly = true)
	 public List<FcCompareAccVO> queryFcCompareAccs(FcBankBillSearchVo searchBean,
													String orderBy, String userCode) {
		 return fcCompareAccDao.queryFcCompareAccs(searchBean, orderBy, userCode);
	 }
	 
	 /**
	  * 功能介绍:获取中心未达账金额合计<br>
	  * <b>日期：</b>2017年6月8日<br>
	  * @version 1.0
	  * @author caozx
	  */
	@Transactional(readOnly = true)
	 public Map<String, Object> queryStayOverCtrSum(Date transDate, String account, String bankAcc, String overType) {
		 return fcCompareAccDao.queryStayOverCtrSum(transDate, account, bankAcc, overType);
	 }
	 
	 /**
	  * <b>功能介绍：</b>统计未对账总数<br>
	  * <b>日期：</b>2017年10月10日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param transDate
	  * @param account
	  * @param overType
	  * @return
	  */
	@Transactional(readOnly = true)
	 public Map<String, Object> queryUnOverCtrSum(Date transDate, String account, String overType) {
		 return fcCompareAccDao.queryUnOverCtrSum(transDate, account, overType);
	 }
    /**
     * <br>
     * <b>功能：</b> 根据结转ID获取企业对账单数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/1/6 0006 <br>
     */
    public List<Map<String, String>> getOverAccBillId(String[] adId){
        return fcCompareAccDao.getOverAccBillId(adId);
    }
	 
	 /**
	  * <b>功能介绍：</b>反结转更新<br>
	  * <b>日期：</b>2017年10月18日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param accIds
	  */
	 public int updateRedoAdjustOver(List<String> accIds){
		 return fcCompareAccDao.updateRedoAdjustOver(accIds);
	 }
	 
	 /**
	  * <b>功能介绍：</b>查询中心结转明细清单<br>
	  * <b>日期：</b>2017年10月20日<br>
	  * <b>作者：</b>caozx<br>
	  * @version 1.0
	  * @param ajustId
	  * @param stayRadios
	  * @param handRadios
	  * @param checkRadios
	  * @return
	  */
	 @Transactional(readOnly = true)
	 public List<FcCompareAccVO> queryCntrOverDatas(String ajustId,String stayRadios,String handRadios,String checkRadios){
		return fcCompareAccDao.queryCntrOverDatas(ajustId, stayRadios, handRadios, checkRadios); 
	 }
	 
	
	/**
	 * 功能介绍:对账操作处理<br>
	 * <b>日期：</b>2017年6月7日<br>
	 * @version 1.0
	 * @author caozx
	 */
	public void doCheck(FcBankCheckSearchVo search, Date checkDate, ICurrentUser user){
 		super.logger.info("对账结束日期："+search.getTransTime());
		super.logger.info("对账帐号："+search.getAccount());
		super.logger.info("对账类型："+search.getCheckType());
		
		String userId = user.getId();
        Date transDate = search.getTransTime();
        String account = search.getAccount();
        String bankAcc = search.getBankAcc();
        String curId = search.getCurId();
        String checkType = search.getCheckType();//核对标准
        
        //银企对账类型
        String paramType = search.getParamType();
        if(FinacialConstant.BOOLEAN_FALSE.equals(paramType)){ // 银行-中心对账
        	//重新生成银行对账数据
        	//generateBankDatas(transDate, checkDate, bankAcc, userId,curId);
        	//重新生成中心数据
        	//generateCtrDatas(transDate, checkDate,checkType, account,curId);
        	
        	FcBankBillSearchVo bankSearchBean = new FcBankBillSearchVo();
        	bankSearchBean.setAccount(account);
        	bankSearchBean.setImportDateEnd(transDate);
        	bankSearchBean.setBankAccTemp(bankAcc);
        	//中心对账数据
        	List<FcCompareAccVO> compareAccList = queryFcCompareAccs(bankSearchBean, "",userId);
        	bankSearchBean.setBankAcc(bankAcc);
        	bankSearchBean.setCurId(curId);
        	//银行对账数据
        	List<FcBankBillVo> bankBillList = fcBankBillService.queryCheckFcBankBills(bankSearchBean, "", userId);
        	//核对成功的数据
        	List<FcCheckOverBean> checkOverList = new ArrayList<FcCheckOverBean>();
        	//对账日志
        	FcBankCheckLog checkLog = buildCheckLog(bankAcc, account, CheckConstant.OPTYPE_TYPE_CHECK,paramType,checkType,user);
        	checkLog.setCurCode(curId);
        	checkLog.setBeginDate(checkDate);
        	checkLog.setEndDate(transDate);
        	fcBankCheckLogService.save(checkLog);
        	
        	if (bankBillList == null || bankBillList.size() < 1) {
        		return;
        	}
        	
        	for (FcBankBillVo bankBill : bankBillList) {
        		String compareSign = bankBill.getCompareSign();
        		if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)
        				|| FinacialConstant.COMPARE_SIGN_OVER.equals(compareSign)) {
        			//手工勾对和已结转
        			continue;
        		} else if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)
        				|| FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {
        			compareWithBlnctrAcc(checkOverList, bankBill, compareAccList, checkType);
        		}
        	}
        	//保存数据
        	updateCheckOverCompareSign(checkOverList, checkLog.getId());
        }else{	//银行-ERP对账
        	
        }
	}
	
	/**
	 * <b>功能介绍：</b>根据核对标准与中心数据进行比对<br>
	 * <b>日期：</b>2017年9月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkOverList
	 * @param bankBill
	 * @param compareAccList
	 * @param checkType
	 */
	public void compareWithBlnctrAcc(List<FcCheckOverBean> checkOverList,
			FcBankBillVo bankBill, List<FcCompareAccVO> compareAccList, String checkType) {
		String bankAcc = bankBill.getBankAcc();//银行账号
        Date bankTallyDate = bankBill.getTallyDate();//银行发生时间
        String bankBillNo = bankBill.getBillNo() == null?"":bankBill.getBillNo();//银行票据号
        BigDecimal bankDebitMoney = bankBill.getDebitMoney();//银行借方发生额
        BigDecimal bankCreditMoney = bankBill.getCreditMoney();//银行贷方发生额
        String bankVoucher = bankBill.getVoucherNo();//凭证号
        
		boolean flag = false;//本次核对标志
        BigDecimal ctrDebitMoney = BigDecimal.ZERO;//中心借方发生额
        BigDecimal ctrCreditMoney = BigDecimal.ZERO;//中心贷方发生额
        Date ctrTallyDate = null;//中心发生时间
        String ctrBillNo = "";//中心票据号
        String voucherOrderNo = "";//中心流水号
        String ctrId = "";//中心对账实体主键
        String curId = "";//币别
        //核对明细
        String[] checkTypes = fcCompareCheckTypeService.getcheckTypesBycheckTypeId(checkType).get("checkTypes").split(",");
        
        for (FcCompareAccVO compareAcc : compareAccList) {
        	 ctrDebitMoney = compareAcc.getDebitMoney();//中心借方发生额
             ctrCreditMoney = compareAcc.getCreditMoney();//中心贷方发生额
             ctrTallyDate = compareAcc.getTallyDate();//中心发生时间
             ctrBillNo = compareAcc.getBillNo()==null?"":compareAcc.getBillNo();//中心票据号
             voucherOrderNo = compareAcc.getVoucherNo();//凭证号
             ctrId = compareAcc.getId();
             curId = compareAcc.getCurId();
             List<Boolean> checkState = new ArrayList<Boolean>();
             for (String check : checkTypes) {//循环核对标准明细
            	 if(CheckConstant.CHECK_TYPE_MONEY.equals(check)){//按发生额核对
            		 if(ctrCreditMoney.compareTo(bankDebitMoney)==0 && ctrDebitMoney.compareTo(bankCreditMoney)==0){
            			 checkState.add(true);
            		 }else{
            			 checkState.add(false);
            		 }
            	 }else if(CheckConstant.CHECK_TYPE_TIME.equals(check)){//按时间核对
            		 if(ctrTallyDate.compareTo(bankTallyDate)==0){
            			 checkState.add(true);
            		 }else{
            			 checkState.add(false);
            		 }
            	 }else if(CheckConstant.CHECK_TYPE_BILLNO.equals(check)){//按票据号核对
            		 if(EmptyUtils.isNotEmpty(ctrBillNo) && ctrBillNo.equals(bankBillNo)){
            			 checkState.add(true);
            		 }else{
            			 checkState.add(false);
            		 }
            	 }else if(CheckConstant.CHECK_TYPE_VOUCHERNO.equals(check)){//按凭证号核对
            		 if(EmptyUtils.isNotEmpty(voucherOrderNo) && voucherOrderNo.equals(bankVoucher)){
            			 checkState.add(true);
            		 }else{
            			 checkState.add(false);
            		 }
            	 }
			}
             
            if(!checkState.contains(false)){
            	flag = true;//记对账标志为已对账
                compareAccList.remove(compareAcc);
                break;
            }
		}
        if (flag) {
       	    checkOverList.add(new FcCheckOverBean(bankBill.getId(),ctrId,bankAcc,curId,null,null));
        }
	}
	
	/**
	 * <b>功能介绍：</b>更新对账标志为已对账<br>
	 * <b>日期：</b>2017年9月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param checkOverList
	 * @param logId
	 */
	public void updateCheckOverCompareSign(List<FcCheckOverBean> checkOverList, String logId) {
		if (checkOverList.size() == 0) {
            return;
        }
		List<String> bankBillPKList = new ArrayList<String>();
		List<String> compareAccPKList = new ArrayList<String>();
		List<FcBankCheckDtl> checkList = new ArrayList<FcBankCheckDtl>();
		
		for (FcCheckOverBean overBean : checkOverList) {
			bankBillPKList.add(overBean.getBankBillPK());
			compareAccPKList.add(overBean.getCompareAccPK());
			checkList.add(new FcBankCheckDtl(overBean.getCurId(), overBean.getBankAcc(), logId, overBean.getBankBillPK(), CheckConstant.LOGTYPE_TYPE_BANK, overBean.getCompareAccPK()));
			//checkList.add(new FcBankCheckDtl(overBean.getCurId(),overBean.getBankAcc(), logId, overBean.getBankBillPK(), CheckConstant.LOGTYPE_TYPE_ACC, overBean.getCompareAccPK()));
		}
		//更新对账标志并插入对账明细
		fcBankBillService.updateComparesignOverCheck(bankBillPKList,FinacialConstant.COMPARE_SIGN_YES);
		fcCompareAccDao.updateComparesignOverCheck(compareAccPKList,FinacialConstant.COMPARE_SIGN_YES);
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
	 * @return
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
	 * <b>日期：</b>2017年6月8日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public Map<String, Object> loadAdjustBalance(BigDecimal blnBalance, BigDecimal bankBalance, String bankAcc,
            String account, Date transDate) {
		Map<String, Object> model = new HashMap<String, Object>();
		FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(account);
		String curId = fcAccDTO.getCurId();
        //中心未达账数据
        Map<String, Object> ctrStayTotal = queryStayOverCtrSum(transDate, account, bankAcc,null);
        
        BigDecimal ctrPayMoney = (BigDecimal) ctrStayTotal.get("creditMoney");//中心已付、银行未付
        BigDecimal ctrRecMoney = (BigDecimal) ctrStayTotal.get("debitMoney");//中心已收、银行未收
        model.put("ctrPayMoney", Double.valueOf(ctrPayMoney.doubleValue()));
        //银行未达账数据
        Map<String, Object> bankStayTotal = fcBankBillService.queryStayOverBankSum(transDate, bankAcc, null, curId);
        BigDecimal bankPayMoney = (BigDecimal) bankStayTotal.get("debitMoney");//银行已付、中心未付
        BigDecimal bankRecMoney = (BigDecimal) bankStayTotal.get("creditMoney");//银行已收、中心未收
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
	 * 
	 * <b>功能介绍:手工勾兑</b><br>
	 * <b>日期：</b>2017年6月16日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
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
		
		String compareSign = FinacialConstant.COMPARE_SIGN_HAND;
	    if (StringUtils.isNotEmpty(opType) && "2".equals(opType)) {//反勾对
	        compareSign = FinacialConstant.COMPARE_SIGN_NO;
        } else if (StringUtils.isNotEmpty(opType) && "3".equals(opType)) {//确认未达
        	 compareSign = FinacialConstant.COMPARE_SIGN_STAY;
        } else if (StringUtils.isNotEmpty(opType) && "4".equals(opType)) {//未达还原
        	 compareSign = FinacialConstant.COMPARE_SIGN_NO;
        } else if (StringUtils.isNotEmpty(opType) && "5".equals(opType)) {//反对账
            compareSign = FinacialConstant.COMPARE_SIGN_NO;
        }
	    //更新对账标志为手工勾对
	    if(!EmptyUtils.isEmptyList(compareAccPKList)){
	    	fcCompareAccDao.updateComparesignOverCheck(compareAccPKList, compareSign);
	    }
	    if(!EmptyUtils.isEmptyList(bankBillPKList)){
	    	fcBankBillService.updateComparesignOverCheck(bankBillPKList, compareSign);
	    }
		
	}
	/**
	 * 
	 * <b>功能介绍:结转操作</b><br>
	 * <b>日期：</b>2017年6月16日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public String doAdjustOverOther(FcBankCheckSearchVo fca,Date checkDate,ICurrentUser user,String checkSign){
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
        
        if(FinacialConstant.BOOLEAN_TRUE.equals(paramType)){//erp对账
        	
        }else if(FinacialConstant.BOOLEAN_FALSE.equals(paramType)){//中心对账
        	balAdjust.setBankInitBalance(bankBalance);
            balAdjust.setBlnInitBalance(blnBalance);
            //判断余额是否平衡
            result = loadAdjustBalance(blnBalance, bankBalance, bankAcc, account, transDate);
            Double blnAdjustBalanceD = (Double) result.get("blnAdjustBalance");
            Double bankAdjustBalanceD = (Double) result.get("bankAdjustBalance");
            BigDecimal blnAdjustBalance = BigDecimal.valueOf(blnAdjustBalanceD);
            BigDecimal bankAdjustBalance = BigDecimal.valueOf(bankAdjustBalanceD);
            if (blnAdjustBalance.compareTo(bankAdjustBalance) != 0) {//银行和中心调节平衡数不平衡，请核对数据再结转！
            	logger.debug("银行和中心调节平衡数不平衡，请核对数据再结转！");
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_OVER_NOTBALANCE);
            }
            balAdjust.setBankAdjBal(bankAdjustBalance);
            balAdjust.setBlnAdjBalance(blnAdjustBalance);
            balAdjust.setCreateBy(user.getLoginName());
            fcBankBalanceAdjustService.save(balAdjust);//保存余额调节表
            //保存中心和银行对账单数据到结转表
            this.saveAdjustCntrProcDatas(transDate, account, balAdjust.getId(), curId);
            this.saveAdjustBankProcDatas(transDate, bankAcc, balAdjust.getId(), curId);
            
            List<FcCompareAccVO> ctrStayList = fcCompareAccDao.queryUnOverCtrStayDatas(account, null, transDate);
            for (FcCompareAccVO ctrStay : ctrStayList) {
            	FcBankBalanceAdjustDetail dtl = new FcBankBalanceAdjustDetail();
                dtl.setTallyDate(ctrStay.getTallyDate());
                dtl.setBillNo(ctrStay.getBillNo() == null ? "" : ctrStay.getBillNo());
                dtl.setCreditMoney(ctrStay.getCreditMoney());
                dtl.setDebitMoney(ctrStay.getDebitMoney());
                dtl.setRedblueSign(ctrStay.getRedblueSign());
                dtl.setSummaryContent(ctrStay.getSummaryContent() == null ? "" : ctrStay.getSummaryContent());
                dtl.setVoucherNo(ctrStay.getVoucherNo() == null ? "" : ctrStay.getVoucherNo());
                dtl.setAjustId(balAdjust.getId());
                dtl.setCurCode(ctrStay.getCurId());
                dtl.setParamType(CheckConstant.CTR_DATA);
                details.add(dtl);
			}
            //结转中心未达数据
            fcCompareAccDao.updateOverStayAccInfos(account, transDate);
            //结转中心对账单
            fcCompareAccDao.updateOverAccInfos(account, transDate);
            //修改账户初始化信息
            fcCheckInitService.updateCheckInitCarryDate(bankAcc, account, transDate,FinacialConstant.CHECK_TYPE_CEN);
        }
        
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
            dtl.setCurCode(bankStay.getCurId());
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
	 * <b>功能介绍：</b>保存中心对账单数据到结转表<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param account
	 * @param adjustId
	 */
	public void saveAdjustCntrProcDatas(Date transDate, String account, String adjustId, String curId){
		List<FcCompareAccVO> compareAccIds = fcCompareAccDao.getctrDataOverData(transDate, account, curId);
		List<FcCompareAccProc> procs = new ArrayList<FcCompareAccProc>();
		for (FcCompareAccVO compare : compareAccIds) {
			FcCompareAccProc proc= new FcCompareAccProc();
			proc.setAjustId(adjustId);
			proc.setFcCompareAccId(compare.getId());//中心对账单id
			proc.setCompareSignHis(compare.getCompareSign());//中心对账标志
			procs.add(proc);
		}
		fcCompareAccProcService.batchSave(procs);
	}
	
	/**
	 * <b>功能介绍：</b>保存银行对账单数据到结转表<br>
	 * <b>日期：</b>2017年10月17日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param adjustId
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
	 * <b>功能介绍：</b>查询总信息<br>
	 * <b>日期：</b>2017年10月10日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param blnBalance
	 * @param bankBalance
	 * @param bankAcc
	 * @param account
	 * @param transDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> queryTotalInfo(BigDecimal blnBalance, BigDecimal bankBalance, String bankAcc,
            String account, Date transDate) {
		Map<String, Object> model = new HashMap<String, Object>();
		//中心已付、银行未付
		Map<String, Object> ctrPayTotal = queryUnOverCtrSum(transDate, account,
				CheckConstant.CTR_PAY_BANK_NO_PAY);
		model.put("ctrPayTotal", ctrPayTotal.get("moneyCount"));
        BigDecimal ctrPayMoney = (BigDecimal) ctrPayTotal.get("creditMoney");
        model.put("ctrPayMoney", Double.valueOf(ctrPayMoney.doubleValue()));
        //中心已收、银行未收
        Map<String, Object> ctrRecTotal = queryUnOverCtrSum(transDate, account,
        		CheckConstant.CTR_REC_BANK_NO_REC);
        model.put("ctrRecTotal", ctrRecTotal.get("moneyCount"));
        BigDecimal ctrRecMoney = (BigDecimal) ctrRecTotal.get("debitMoney");
        model.put("ctrRecMoney", Double.valueOf(ctrRecMoney.doubleValue()));
       //银行已付、中心未付
        Map<String, Object> bankPayTotal = fcBankBillService.queryUnOverCtrSum(transDate, bankAcc,
        		CheckConstant.BANK_PAY_CTR_NO_PAY);
        model.put("bankPayTotal", bankPayTotal.get("moneyCount"));
        BigDecimal bankPayMoney = (BigDecimal) bankPayTotal.get("debitMoney");
        model.put("bankPayMoney", Double.valueOf(bankPayMoney.doubleValue()));
       //银行已收、中心未收
        Map<String, Object> bankRecTotal = fcBankBillService.queryUnOverCtrSum(transDate, bankAcc,
        		CheckConstant.BANK_REC_CTR_NO_REC);
        model.put("bankRecTotal", bankRecTotal.get("moneyCount"));
        BigDecimal bankRecMoney = (BigDecimal) bankRecTotal.get("creditMoney");
        model.put("bankRecMoney", Double.valueOf(bankRecMoney.doubleValue()));
       //单位调节平衡数
        BigDecimal blnAdjustBalance = blnBalance.add(bankRecMoney).subtract(bankPayMoney);
        BigDecimal bankAdjustBalance = bankBalance.add(ctrRecMoney).subtract(ctrPayMoney);
        model.put("blnAdjustBalance", Double.valueOf(blnAdjustBalance.doubleValue()));
        model.put("bankAdjustBalance", Double.valueOf(bankAdjustBalance.doubleValue()));
        model.put("blnBalance", Double.valueOf(blnBalance.doubleValue()));
        model.put("bankBalance", Double.valueOf(bankBalance.doubleValue()));
        model.put("bankAcc", bankAcc);
        model.put("nowDate", DateUtils.date2Str(DateUtils.date_sdf()));
		return model;
	}
	
	/**
	 * <b>功能介绍：</b>获取账户账面金额<br>
	 * <b>日期：</b>2017年9月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param account
	 * @param bankAcc
	 * @param transDate
	 * @param paramType
	 * @param accDisplayType
	 * @return
	 */
	@Transactional(readOnly = true)
	public FcBankErpBalInfoVO getAccBalInfo(String account, String bankAcc, String transDate, String paramType, String accDisplayType, String curId) throws Exception{
		FcBankErpBalInfoVO bankInfo = new FcBankErpBalInfoVO();
		bankInfo.setAccount(account);
		bankInfo.setBankAcc(bankAcc);
		bankInfo.setTransTimeStr(transDate);
		bankInfo.setParamType(paramType);
		bankInfo.setAccDisplayType(accDisplayType);
		Date transDate_ = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		FcBankBalanceAdjust adjust = fcCompareAccDao.getLastAdjustOver(account, bankAcc);
		FcCheckInit fcCheckInit = fcCheckInitService.getCheckInitByAccount(bankAcc, account,FinacialConstant.CHECK_TYPE_CEN, curId);
        Date lastOverDate = null;
        BigDecimal bankInitBal = BigDecimal.ZERO;//银行账面初始余额
		if(EmptyUtils.isEmpty(adjust)){
            if(EmptyUtils.isEmpty(fcCheckInit)){//还未初始化账户余额，请先初始化账户余额
                //logger.debug("银行账号：[" + bankAcc + "],erp科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!");
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_BALNOTINIT,bankAcc,account);
            }
            lastOverDate = fcCheckInit.getCheckDate();
            bankInitBal = fcCheckInit.getBankBalance();
        }else{
            //上次结转的余额
            lastOverDate = adjust.getEndDate();//上次结转日期
            if (fcCheckInit != null) {
                Date checkDate = fcCheckInit.getCheckDate();
                if (checkDate != null && checkDate.after(lastOverDate)) {
                    lastOverDate = checkDate;
                    bankInitBal = fcCheckInit.getBankBalance();
                }else {
                    bankInitBal = adjust.getBankInitBalance();
                }
            } else {
                //"银行账号：[" + bankAcc + "],企业科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!
                throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_BALNOTINIT,bankAcc,account);
            }
        }
        bankInfo.setLastOverDate(lastOverDate);
        String lastOverDateStr = DateUtils.date2Str(lastOverDate, DateUtils.date_sdf());
        if (transDate_.before(DateUtils.addDay(lastOverDate, 1))) {//结束日期应大于上次结转日期
            //logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE,lastOverDateStr);
        }
		//银行中心对账
        BigDecimal bankBalance = accBalService.getCurBalOrLastRealBal(bankAcc, curId, transDate_);
        if (EmptyUtils.isEmpty(bankBalance)) {
            BigDecimal duringMoney = bisAccDtlService.getDuringMoney(fcCheckInit.getCurId(), bankAcc, lastOverDate, transDate_);//对账期间银行发生额
            bankBalance = bankInitBal.add(duringMoney).setScale(2, BigDecimal.ROUND_HALF_UP);//银行账面余额
        }
        BigDecimal accountBalance = queryBlnctrAccBalance(account, transDate_);//中心账面余额
		bankInfo.setAccBal(accountBalance);
		bankInfo.setBankBal(bankBalance);
		return bankInfo;
	}
	
	
	/**
	 * <b>功能介绍：</b>银行账面余额<br>
	 * <b>日期：</b>2017年9月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param bankAcc
	 * @param transTime
	 * @param account
	 * @return
	 */
	@Transactional(readOnly = true)
	public BigDecimal queryBankAccBalance(String bankAcc, Date transTime, String account) {
		BigDecimal balance = BigDecimal.ZERO;
		List<BigDecimal> listDtl = fcCompareAccDao.getBankAccBalance(bankAcc, transTime);
		List<BigDecimal> listBal = fcCompareAccDao.getBankAccBalanceByBal(bankAcc, transTime);
		if(!EmptyUtils.isEmptyList(listDtl)){
			balance = listDtl.get(0);
		}else if(!EmptyUtils.isEmptyList(listBal)){
			balance = listBal.get(0);
		}else{
			logger.debug("未能找到银行账户：[" + bankAcc + "]明细或余额，请确定是否已初始化银行账号余额或明细！");
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_NOTBANKACCDTL,bankAcc);
		}
		return balance;
	}
	
	/**
	 * <b>功能介绍：</b>查询中心账户余额<br>
	 * <b>日期：</b>2017年9月27日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param blnctrAcc
	 * @param balanceDate
	 * @return
	 */
	@Transactional(readOnly = true)
	public BigDecimal queryBlnctrAccBalance(String blnctrAcc, Date balanceDate) {
		BigDecimal balance = BigDecimal.ZERO;
		FcAccBalanceDTO fcAccBalanceDTO = fcAccBalanceService.getBalanceByAccountAndDate(blnctrAcc,balanceDate);
		if (!EmptyUtils.isEmpty(fcAccBalanceDTO)) {
			balance = fcAccBalanceDTO.getBalance();
        }else{
        	logger.debug("中心账号：" + blnctrAcc + "余额不存在，请确定是否已日结！");
        	throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ACCBALISNOTEXISTS,blnctrAcc);
        }
        return balance;
	}
	
	
	/**
	 * <b>功能介绍：</b>重新产生银行数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param bankAcc
	 * @param userCode
	 */
	public void generateBankDatas(Date transDate, Date checkDate, String bankAcc, String userCode,String curId) {
		//1、删除接口导入且未对账的数据
		fcCompareAccDao.deleteInterfaceImportDatas(transDate, checkDate, bankAcc,curId);
        //2、  更新手工录入或文本导入且已对账的数据为未对账
		fcCompareAccDao.updateBankBillStatus(transDate, checkDate, bankAcc,curId);
        //3、插入银行对账表中不存在的对账数据
		List<FcBankBillVo> bankBillVOs = fcCompareAccDao.findNoExistNeedCheckBills(transDate, checkDate, bankAcc, userCode,curId);
		List<FcBankBill> bill = BeanUtils.copyListProperties(bankBillVOs, FcBankBill.class);
		fcBankBillService.batchSave(bill);
	}
	
	/**
	 * <b>功能介绍：</b>重新产生中心数据<br>
	 * <b>日期：</b>2017年9月28日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param transDate
	 * @param checkDate
	 * @param account
	 */
	public void generateCtrDatas(Date transDate, Date checkDate,String checkType, String account,String curId) {
		//1、删除对账时产生的且状态为已对账或未对账的数据
		fcCompareAccDao.deleteCheckCompareAccs(transDate, checkDate, account,curId);
        //2、将手工录入的，已对账的数据改为未对账
		fcCompareAccDao.updateCompareAccStatus(transDate, checkDate, account,curId);
        //2、插入对账表中不存在的需对账的数据
		List<FcCompareAccVO> comAccVOs = fcCompareAccDao.findNoExistNeedCheckAccs(transDate, checkDate,checkType, account,curId);
		List<FcCompareAcc> comAccs = BeanUtils.copyListProperties(comAccVOs, FcCompareAcc.class);
		this.batchSave(comAccs);
	}
	/**
	 * 企业账导出
	 * @param request
	 * @param response
	 * @param list
	 * @param title
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> list,String title) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
		response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, title) + ".xls");
		try (OutputStream os = response.getOutputStream()) {
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 无边框靠左对齐样式
			HSSFCellStyle leftNoBorderCellStyle = workbook.createCellStyle();
			leftNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			leftNoBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
			// 无边框居中对齐样式
			HSSFCellStyle centerNoBorderCellStyle = workbook.createCellStyle();
			centerNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			centerNoBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
			HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            rightCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFPalette customPalette = workbook.getCustomPalette();
            customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 255, (byte) 255, (byte) 128);
            customPalette.setColorAtIndex(HSSFColor.RED.index, (byte) 255, (byte) 128, (byte) 128);
            customPalette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 176, (byte) 220, (byte) 129);
            customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 186, (byte) 230);
            HSSFCellStyle yellowCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            yellowCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            yellowCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle redCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            redCellStyle.setFillForegroundColor(HSSFColor.RED.index);
            redCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle blueCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            blueCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            blueCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle greenCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            greenCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
            greenCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle yellowRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            yellowRigthCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            yellowRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            yellowRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle redRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            redRigthCellStyle.setFillForegroundColor(HSSFColor.RED.index);
            redRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            redRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle blueRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            blueRigthCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            blueRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            blueRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle greenRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            greenRigthCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
            greenRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            greenRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle tempCellStyle = centerCellStyle;
            HSSFCellStyle tempRightCellStyle = rightCellStyle;
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);
			// 标题
			setExportTitle(sheet, title, centerNoBorderCellStyle);
			//表头
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell row1cell0 = row1.createCell(0);
			row1cell0.setCellStyle(centerCellStyle);
			row1cell0.setCellValue("日期");
            HSSFCell row1cell01 = row1.createCell(1);
            row1cell01.setCellStyle(centerCellStyle);
            row1cell01.setCellValue("中心账号");
			HSSFCell row1cell1 = row1.createCell(2);
			row1cell1.setCellStyle(centerCellStyle);
			row1cell1.setCellValue("票据号");
			HSSFCell row1cell2 = row1.createCell(3);
			row1cell2.setCellStyle(centerCellStyle);
			row1cell2.setCellValue("借/贷");
			HSSFCell row1cell3 = row1.createCell(4);
			row1cell3.setCellStyle(centerCellStyle);
			row1cell3.setCellValue("金额");
			HSSFCell row1cell4 = row1.createCell(5);
			row1cell4.setCellStyle(centerCellStyle);
			row1cell4.setCellValue("凭证号");
			int i = 1;
			for(Map<String,Object> map : list){
				i++;
				HSSFRow row = sheet.createRow(i);
				String compareSign = null;//对账标识
                if(EmptyUtils.isEmpty(map.get("compareSign"))) {
                    compareSign = FinacialConstant.COMPARE_SIGN_NO;
                } else {
                    compareSign = (String) map.get("compareSign");//对账标识
                }
                if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {//未对账
                    tempCellStyle = yellowCellStyle;
                    tempRightCellStyle = yellowRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已对账
                    tempCellStyle = blueCellStyle;
                    tempRightCellStyle = blueRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//手工勾对
                    tempCellStyle = greenCellStyle;
                    tempRightCellStyle = greenRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达
                    tempCellStyle = redCellStyle;
                    tempRightCellStyle = redRigthCellStyle;
                }
				HSSFCell rowCell0 = row.createCell(0);
				rowCell0.setCellStyle(tempCellStyle);
				rowCell0.setCellValue(map.get("tallyDate").toString());
                HSSFCell rowCell01 = row.createCell(1);
                rowCell01.setCellStyle(tempCellStyle);
                rowCell01.setCellValue(map.get("compAcc").toString());
				HSSFCell rowCell1 = row.createCell(2);
				rowCell1.setCellStyle(tempCellStyle);
				rowCell1.setCellValue(map.get("billNo") == null ? "" : map.get("billNo").toString());
				HSSFCell rowCell2 = row.createCell(3);
				rowCell2.setCellStyle(tempCellStyle);
				String cdSignStr = "";
				String cdSign = map.get("cdSign").toString();
				if("1".equals(cdSign)){
					cdSignStr = "借";
				}else{
					cdSignStr = "贷";
				}
				rowCell2.setCellValue(cdSignStr);
				HSSFCell rowCell3 = row.createCell(4);
				rowCell3.setCellStyle(tempRightCellStyle);
				rowCell3.setCellValue(NumberUtil.formatNumeric(map.get("money").toString()));
				HSSFCell rowCell4 = row.createCell(5);
				rowCell4.setCellStyle(tempCellStyle);
				rowCell4.setCellValue(map.get("voucherNo") == null ? "" : map.get("voucherNo").toString());
			}
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 银行账导出
	 * @param request
	 * @param response
	 * @param list
	 * @param title
	 */
	public void exportBank(HttpServletRequest request, HttpServletResponse response, List<Map<String,Object>> list,String title) {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download;");
		response.setHeader("content-disposition", "attachment;filename=" + ExportUtils.getFileName(request, title) + ".xls");
		//银行流水id集合
		List<String> ids = new ArrayList<>();
		Map<String, Map<String, Object>> bankMap = new HashMap<>();
		for(Map<String,Object> map : list){
			if(!EmptyUtils.isEmpty(map.get("bankId"))) {
		        String bankId = (String) map.get("bankId");
				ids.add(bankId);
                bankMap.put(bankId, map);
			}
		}
		List<BisAccDtlDTO> bisAccDtlDTOList = bisAccDtlService.getByIds(ids);
		Map<String, BisAccDtlDTO> bisAccDtlDTOMap = new HashMap<>();
		//数据合并
        for(BisAccDtlDTO accDtlDTO : bisAccDtlDTOList){
            bisAccDtlDTOMap.put(accDtlDTO.getId(), accDtlDTO);
        }
		try (OutputStream os = response.getOutputStream()) {
			// 声明一个工作薄
			HSSFWorkbook workbook = new HSSFWorkbook();
			// 无边框靠左对齐样式
			HSSFCellStyle leftNoBorderCellStyle = workbook.createCellStyle();
			leftNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			leftNoBorderCellStyle.setAlignment(HorizontalAlignment.LEFT);
			// 无边框居中对齐样式
			HSSFCellStyle centerNoBorderCellStyle = workbook.createCellStyle();
			centerNoBorderCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			centerNoBorderCellStyle.setAlignment(HorizontalAlignment.CENTER);
            HSSFPalette customPalette = workbook.getCustomPalette();
            customPalette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 255, (byte) 255, (byte) 128);
            customPalette.setColorAtIndex(HSSFColor.RED.index, (byte) 255, (byte) 128, (byte) 128);
            customPalette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 176, (byte) 220, (byte) 129);
            customPalette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 79, (byte) 186, (byte) 230);
            HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
			HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            HSSFCellStyle yellowCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            yellowCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            yellowCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle redCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            redCellStyle.setFillForegroundColor(HSSFColor.RED.index);
            redCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle blueCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            blueCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            blueCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle greenCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
            greenCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
            greenCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFCellStyle yellowRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            yellowRigthCellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
            yellowRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            yellowRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle redRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            redRigthCellStyle.setFillForegroundColor(HSSFColor.RED.index);
            redRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            redRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle blueRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            blueRigthCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            blueRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            blueRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle greenRigthCellStyle = ExportUtils.getRightCellStyle(workbook, false);
            greenRigthCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
            greenRigthCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            greenRigthCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
            HSSFCellStyle tempCellStyle = centerCellStyle;
            HSSFCellStyle tempRightCellStyle = rightCellStyle;
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet(title);
			// 标题
			// 设置表格默认列宽度为20个字节
			sheet.setDefaultColumnWidth(15);
			// (实际列宽+列宽基数：0.72) * 256
//            int width = new BigDecimal((80 / 3.5 + 0.72) * 256).intValue();
			sheet.setColumnWidth(0, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(1, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(2, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(3, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(4, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(5, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(6, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(7, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(8, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(9, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
			sheet.setColumnWidth(10, BigDecimal.valueOf((30 + 0.72) * 256).intValue());
			sheet.setColumnWidth(11, BigDecimal.valueOf((30 + 0.72) * 256).intValue());
			sheet.setColumnWidth(12, BigDecimal.valueOf((30 + 0.72) * 256).intValue());
			sheet.setColumnWidth(13, BigDecimal.valueOf((30 + 0.72) * 256).intValue());
			// 主标题
			HSSFRow titleRow = sheet.createRow(0);
			HSSFCell titleCell = titleRow.createCell(0);
			titleCell.setCellStyle(centerCellStyle);
			titleCell.setCellValue(title);
			for (int i = 1; i <= 13; i++) {
				titleRow.createCell(i);
			}
			//CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
			//合并列
			CellRangeAddress region = new CellRangeAddress(0, 0, 0, 13);
			ExportUtils.setRegionStyle(sheet, region, centerCellStyle);
			sheet.addMergedRegion(region);
			//表头
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell row1cell0 = row1.createCell(0);
			row1cell0.setCellStyle(centerCellStyle);
			row1cell0.setCellValue("交易时间");
			HSSFCell row1cell1 = row1.createCell(1);
			row1cell1.setCellStyle(centerCellStyle);
			row1cell1.setCellValue("本方账号");
			HSSFCell row1cell2 = row1.createCell(2);
			row1cell2.setCellStyle(centerCellStyle);
			row1cell2.setCellValue("本方账户名");
			HSSFCell row1cell3 = row1.createCell(3);
			row1cell3.setCellStyle(centerCellStyle);
			row1cell3.setCellValue("借贷标志");
			HSSFCell row1cell4 = row1.createCell(4);
			row1cell4.setCellStyle(centerCellStyle);
			row1cell4.setCellValue("币别");
			HSSFCell row1cell5 = row1.createCell(5);
			row1cell5.setCellStyle(centerCellStyle);
			row1cell5.setCellValue("交易金额");
			HSSFCell row1cell6 = row1.createCell(6);
			row1cell6.setCellStyle(centerCellStyle);
			row1cell6.setCellValue("余额");
			HSSFCell row1cell7 = row1.createCell(7);
			row1cell7.setCellStyle(centerCellStyle);
			row1cell7.setCellValue("对方账号");
			HSSFCell row1cell8 = row1.createCell(8);
			row1cell8.setCellStyle(centerCellStyle);
			row1cell8.setCellValue("对方账户名");
			HSSFCell row1cell9 = row1.createCell(9);
			row1cell9.setCellStyle(centerCellStyle);
			row1cell9.setCellValue("对方账户开户行");
			HSSFCell row1cell10 = row1.createCell(10);
			row1cell10.setCellStyle(centerCellStyle);
			row1cell10.setCellValue("摘要");
			HSSFCell row1cell11 = row1.createCell(11);
			row1cell11.setCellStyle(centerCellStyle);
			row1cell11.setCellValue("附言");
			HSSFCell row1cell12 = row1.createCell(12);
			row1cell12.setCellStyle(centerCellStyle);
			row1cell12.setCellValue("项目信息");
			HSSFCell row1cell13 = row1.createCell(13);
			row1cell13.setCellStyle(centerCellStyle);
			row1cell13.setCellValue("用途");
			int i = 1;
            for(Map<String,Object> map : list) {
                String bankId = (String) map.get("bankId");
                BisAccDtlDTO bisAccDtlDTO = bisAccDtlDTOMap.get(bankId);
                if (EmptyUtils.isEmpty(bisAccDtlDTO)) {
                    bisAccDtlDTO = new BisAccDtlDTO();
                    if (!EmptyUtils.isEmpty(map.get("tallyDate"))) {
                        bisAccDtlDTO.setTransTime((Date) map.get("tallyDate"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("bankAcc"))) {
                        bisAccDtlDTO.setBankAcc((String) map.get("bankAcc"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("accName"))) {
                        bisAccDtlDTO.setAccName((String) map.get("accName"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("cdSign"))) {
                        bisAccDtlDTO.setCdSign((String) map.get("cdSign"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("curId"))) {
                        bisAccDtlDTO.setCurId((String) map.get("curId"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("money"))) {
                        bisAccDtlDTO.setAmt((BigDecimal) map.get("money"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("balance"))) {
                        bisAccDtlDTO.setBal((BigDecimal) map.get("balance"));
                    }
                    if (!EmptyUtils.isEmpty(map.get("voucherNo"))) {
                        bisAccDtlDTO.setVoucherNo((String) map.get("voucherNo"));
                    }
                }
                String compareSign = null;//对账标识
                if(EmptyUtils.isEmpty(map.get("compareSign"))) {
                    compareSign = FinacialConstant.COMPARE_SIGN_NO;
                } else {
                    compareSign = (String) map.get("compareSign");//对账标识
                }
                if (FinacialConstant.COMPARE_SIGN_NO.equals(compareSign)) {//未对账
                    tempCellStyle = yellowCellStyle;
                    tempRightCellStyle = yellowRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_YES.equals(compareSign)) {//已对账
                    tempCellStyle = blueCellStyle;
                    tempRightCellStyle = blueRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_HAND.equals(compareSign)) {//手工勾对
                    tempCellStyle = greenCellStyle;
                    tempRightCellStyle = greenRigthCellStyle;
                } else if (FinacialConstant.COMPARE_SIGN_STAY.equals(compareSign)) {//未达
                    tempCellStyle = redCellStyle;
                    tempRightCellStyle = redRigthCellStyle;
                }
				i++;
				HSSFRow row = sheet.createRow(i);
				HSSFCell rowCell0 = row.createCell(0);
				rowCell0.setCellStyle(tempCellStyle);
				rowCell0.setCellValue(DateUtils.format(bisAccDtlDTO.getTransTime()));
				HSSFCell rowCell1 = row.createCell(1);
				rowCell1.setCellStyle(tempCellStyle);
				rowCell1.setCellValue(bisAccDtlDTO.getBankAcc());
				HSSFCell rowCell2 = row.createCell(2);
				rowCell2.setCellStyle(tempCellStyle);
				rowCell2.setCellValue(bisAccDtlDTO.getAccName());
				HSSFCell rowCell3 = row.createCell(3);
				rowCell3.setCellStyle(tempCellStyle);
				rowCell3.setCellValue(bisAccDtlDTO.getCdSignName());
				HSSFCell rowCell4 = row.createCell(4);
				rowCell4.setCellStyle(tempCellStyle);
				rowCell4.setCellValue(bisAccDtlDTO.getCurName());
				HSSFCell rowCell5 = row.createCell(5);
				rowCell5.setCellStyle(tempRightCellStyle);
				rowCell5.setCellValue(NumberUtil.formatNumeric(bisAccDtlDTO.getAmt()));
				HSSFCell rowCell6 = row.createCell(6);
				rowCell6.setCellStyle(tempRightCellStyle);
				rowCell6.setCellValue(NumberUtil.formatNumeric(bisAccDtlDTO.getBal()));
				HSSFCell rowCell7 = row.createCell(7);
				rowCell7.setCellStyle(tempCellStyle);
				rowCell7.setCellValue(bisAccDtlDTO.getOppAccNo());
				HSSFCell rowCell8 = row.createCell(8);
				rowCell8.setCellStyle(tempCellStyle);
				rowCell8.setCellValue(bisAccDtlDTO.getOppAccName());
				HSSFCell rowCell9 = row.createCell(9);
				rowCell9.setCellStyle(tempCellStyle);
				rowCell9.setCellValue(bisAccDtlDTO.getOppAccBank());
				HSSFCell rowCell10 = row.createCell(10);
				rowCell10.setCellStyle(tempCellStyle);
				rowCell10.setCellValue(bisAccDtlDTO.getRemark());
				HSSFCell rowCell11 = row.createCell(11);
				rowCell11.setCellStyle(tempCellStyle);
				rowCell11.setCellValue(bisAccDtlDTO.getPostscript());
				HSSFCell rowCell12 = row.createCell(12);
				rowCell12.setCellStyle(tempCellStyle);
				rowCell12.setCellValue(bisAccDtlDTO.getProjectRemark());
				HSSFCell rowCell13 = row.createCell(13);
				rowCell13.setCellStyle(tempCellStyle);
				rowCell13.setCellValue(bisAccDtlDTO.getUses());
			}
			workbook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 设置标题
	 *
	 * @param sheet
	 * @param title
	 * @param centerCellStyle
	 */
	public void setExportTitle(HSSFSheet sheet, String title, HSSFCellStyle centerCellStyle) {
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(15);
		// (实际列宽+列宽基数：0.72) * 256
//            int width = new BigDecimal((80 / 3.5 + 0.72) * 256).intValue();
		sheet.setColumnWidth(0, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
		sheet.setColumnWidth(1, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
		sheet.setColumnWidth(2, BigDecimal.valueOf((17 + 0.72) * 256).intValue());
		sheet.setColumnWidth(3, BigDecimal.valueOf((13 + 0.72) * 256).intValue());
		sheet.setColumnWidth(4, BigDecimal.valueOf((25 + 0.72) * 256).intValue());
		sheet.setColumnWidth(5, BigDecimal.valueOf((30 + 0.72) * 256).intValue());
		// 主标题
		HSSFRow titleRow = sheet.createRow(0);
		HSSFCell titleCell = titleRow.createCell(0);
		titleCell.setCellStyle(centerCellStyle);
		titleCell.setCellValue(title);
		for (int i = 1; i <= 5; i++) {
			titleRow.createCell(i);
		}
		//CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
		//合并列
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
		ExportUtils.setRegionStyle(sheet, region, centerCellStyle);
		sheet.addMergedRegion(region);
	}
	@Override
	public List<BigDecimal> queryAccBalanceByDate(String account, Date balanceDate) {
		return fcCompareAccDao.queryAccBalanceByDate(account, balanceDate);
	}

	@Override
	public List<FcCompareAccBeanDTO> getCompAccByOrderNo(List<FcCompareAccBeanDTO> dtos) {
		List<FcCompareAcc> compAccByOrderNo = fcCompareAccDao.getCompAccByOrderNo(dtos);
		if (compAccByOrderNo.size() < 1) {
			return Collections.EMPTY_LIST;
		}
		return BeanUtils.copyListProperties(compAccByOrderNo, FcCompareAccBeanDTO.class);
	}
}
