package com.bytter.biz.finacial.accbook.service;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.biz.finacial.accbook.dao.FcChargeAccrueDao;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlned;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcChargeAccrue;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.FcChargeAccruePageVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeRule;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.service.FcBusRaceService;
import com.bytter.biz.finacial.paramconfig.service.FcChargeRuleService;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherEntryDefineService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
* @Title: Service
* @Description: FC_CHARGE_ACCRUE
* @author onlineGenerator
* @date 2017-07-04 15:14:11
* @version 1.0
*
*/
@Service("fcChargeAccrueService")
@Transactional
public class FcChargeAccrueService extends BaseService<FcChargeAccrue,String> {
	@Autowired
	private FcChargeAccrueDao fcChargeAccrueDao;
	@Autowired
	private FcChargeRuleService ruleService;
	@Autowired
	private FcBusRaceService busRaceService;
	@Autowired
	@Qualifier("fcAccbookService")
	private FcAccbookService bookService;
	@Autowired
	private FcAccbookBlnedService bookBlnedService;
	@Autowired
	private FcVoucherEntryDefineService entryDefineService;
	
	@DubboReference(check = false)
	private IFcAccService accService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;

	@DubboReference(check = false)
	private IFcVoucherTypeService iFcVoucherTypeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcChargeAccrue entity){
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
 	public void save(FcChargeAccrue entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setCurName(fcAccDTO.getCurName());
		BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(fcAccDTO.getCurId());
		entity.setCurCode(btCurrencyDTO.getCurCode());
		entity.setAccName(fcAccDTO.getAccName());
		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcChargeAccrue entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcChargeAccrue entity){
		FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
		entity.setCurName(fcAccDTO.getCurName());
		BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(fcAccDTO.getCurId());
		entity.setCurCode(btCurrencyDTO.getCurCode());
		entity.setAccName(fcAccDTO.getAccName());
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcChargeAccrue, String> getEntityDao() {
		return fcChargeAccrueDao;
	}
	
	/**
	 * 功能介绍 : 查询手续费清单数据。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@Transactional(readOnly = true)
	public DataTablesOutput<FcChargeAccruePageVO> queryChargeAccrue(DataTablesInput input,String resource, String userId){
		//手续费计提页面，凭证种类条件查询
		Object object = input.getFormdata().get("likes_voucherName");
		List<String> voucherIds = new ArrayList<>();
		if (ObjectUtils.isNotNull(object)){
			String voucherName = object.toString();
			//模糊查询
			List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeLikeName(voucherName);
			voucherIds = fcVoucherTypeDTOS.stream().map(FcVoucherTypeDTO::getId).collect(Collectors.toList());
			if (CollectionUtils.isEmpty(voucherIds)){
				return new DataTablesOutput<>();
			}
		}
		List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
		List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccNameAndMeasureUnitByCorpId(corpAuth);
		List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());

		DataTablesOutput<FcChargeAccruePageVO> output = fcChargeAccrueDao.queryChargeAccrue(input,voucherIds,accounts);
		List<FcChargeAccruePageVO> data = output.getData();
		List<String> ids = data.stream().map(FcChargeAccruePageVO::getVoucherType).collect(Collectors.toList());
		List<FcVoucherTypeDTO> fcVoucherTypeDTOS = iFcVoucherTypeService.getFcVoucherTypeByIds(ids);
		if (CollectionUtils.isNotEmpty(fcVoucherTypeDTOS)){
			Map<String, String> map = fcVoucherTypeDTOS.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, FcVoucherTypeDTO::getVoucherName));
			data.forEach(a -> {
				a.setVoucherName(map.get(a.getVoucherType()));
			});
		}
		return output;
	}
	
	/**
     * 功能介绍 : 手续费计提操作。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
	public void doChargeAccrue(String id, String userId) {
		List<FcChargeAccrue> chargeAccrues = fcChargeAccrueDao.queryChargeAccrueById(id);
		List<FcChargeAccrue> list = new ArrayList<FcChargeAccrue>();
		for (FcChargeAccrue chargeAccrue : chargeAccrues) {
			if (FinacialConstant.TALLY_SIN_N.equals(chargeAccrue.getTallySign())) {
				list.add(chargeAccrue);
			}
		}
		if (null == list || list.isEmpty()) {
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_TALLYNO);
		}
		createChargeVoucher(null, userId, chargeAccrues);
	}
	
	/**
     * 功能介绍 : 手续费计提凭证处理。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
	public void createChargeVoucher(String busGlobalId, String userCode, List<FcChargeAccrue> chargeAccrues) {
		if (null == chargeAccrues || chargeAccrues.isEmpty()) {
			return;
		}
		VoucherWrapper voucher = createVoucherWrapper(busGlobalId, userCode, chargeAccrues);
		voucher.setAutoCheck(true);//设置自动复核
		bookService.submitFcAccbook(voucher);
        
        //更新手续费记录复核状态
        for (FcChargeAccrue chargeAccrue : chargeAccrues) {
            Date time = DateUtils.getDate();
            chargeAccrue.setAccrueTime(time);
            chargeAccrue.setCheckerCode(userCode);
            chargeAccrue.setOperatorCode(userCode);
            chargeAccrue.setCheckTime(time);
            chargeAccrue.setTallySign(FinacialConstant.TALLY_SIN_Y);//已计提
            chargeAccrue.setOrderNo(voucher.getBookVoucher().getAccbook().getId());//设置流水ID
            saveOrUpdate(chargeAccrue);
            //原凭证标识为已计息
            String bookId = chargeAccrue.getTargetOrderNo();//原凭证的流水ID
            FcAccbook accbook = bookService.get(bookId, true);
            if (null != accbook) {
            	accbook.setChargedSign(FinacialConstant.TALLY_SIN_Y);//已计提
            	bookService.update(accbook);//更新凭证计提标识 
			} else {
				FcAccbookBlned accbookBlned = bookBlnedService.get(bookId, true);
				accbookBlned.setChargedSign(FinacialConstant.TALLY_SIN_Y);//已计提
				bookBlnedService.update(accbookBlned);//更新凭证计提标识 
			}
        }
    }
	
	/**
     * 功能介绍 : 生成手续费凭证。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
	private VoucherWrapper createVoucherWrapper(String busGlobalId, String userCode, List<FcChargeAccrue> chargeAccrues) {
        Map<FcChargeRule, List<FcChargeAccrue>> chargeAccruesMap = new HashMap<FcChargeRule, List<FcChargeAccrue>>();
        for (FcChargeAccrue chargeAccrue : chargeAccrues) {//统计一个业务规则对应的计提记录
        	String chargeRuleId = chargeAccrue.getChargeRuleId();
        	FcChargeRule chargeRule = ruleService.get(chargeRuleId, true);
            List<FcChargeAccrue> ruleChargeAccrues = chargeAccruesMap.get(chargeRule);
            if (ruleChargeAccrues == null) {
                ruleChargeAccrues = new ArrayList<FcChargeAccrue>();
                chargeAccruesMap.put(chargeRule, ruleChargeAccrues);
            }
            ruleChargeAccrues.add(chargeAccrue);
        }
        VoucherWrapper voucherWrapper = new VoucherWrapper();
        voucherWrapper.setCharge(true);
        voucherWrapper.setBusGlobalId(busGlobalId);
        voucherWrapper.setOriType(BusOriType.HANDLE_CHARGE.get());
        voucherWrapper.setUserCode(userCode);
        voucherWrapper.setProcessType(ProcessType.PROCESS_COMMON);
        for (Entry<FcChargeRule, List<FcChargeAccrue>> entry : chargeAccruesMap.entrySet()) {
            voucherWrapper.addTarget(createAccbook(null, userCode, entry.getKey(), entry.getValue()));
        }
        return voucherWrapper;
    }
	
	/**
     * 功能介绍 : 创建手续费凭证数据。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
	private FcAccbookVoucherVO createAccbook(String busGlobalId, String userCode, FcChargeRule chargeRule,
            List<FcChargeAccrue> chargeAccrues) {
		FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
        BigDecimal creditTotal = BigDecimal.ZERO;
        Map<String, BigDecimal> accountMoneys = new HashMap<String, BigDecimal>();
        Map<String, FcAccDTO> accDTOMap = new HashMap<String, FcAccDTO>();
        String netCode = null;
        for (FcChargeAccrue chargeAccrue : chargeAccrues) {
            String account = chargeAccrue.getAccount();
            BigDecimal accountMoney = accountMoneys.get(account);
            if (accountMoney == null) {
                accountMoney = BigDecimal.ZERO;
            }
            accountMoneys.put(account, accountMoney.add(chargeAccrue.getChargeMoney()));
            creditTotal = creditTotal.add(chargeAccrue.getChargeMoney());
            
            FcAccDTO accDTO = accDTOMap.get(account);
            if (null == accDTO) {
            	accDTO = getFcAccDTOByAccount(account);
            	accDTOMap.put(account, accDTO);
			}
            netCode = accDTO.getNetId();
        }
        FcChargeAccrue chargeAccrue = chargeAccrues.get(0);
        String account = chargeAccrue.getAccount();
    	FcAccDTO fcAccDTO = accService.getAccDTOByAccount(account);
    	String netId = fcAccDTO.getNetId();
    	Date closeDate = dayCloseService.getDayCloseFinalDate(netId);//已日结日期
    	Date targetTallyDate = chargeAccrue.getTargetTallyDate();//原凭证的记账日期
    	Date tallyDate = DateUtils.addDay(closeDate, 1);//新记账日期
    	if (tallyDate.compareTo(targetTallyDate) < 0) {//日结日期>原凭证记账日期
    		tallyDate = targetTallyDate;//以实际发生业务的记账日期为准
    	}
    	FcAccbook accbook = createAccbookMainInfo(busGlobalId, userCode, netCode);
    	accbook.setTallyDate(tallyDate);//以实际记账日期为准
    	accbook.setInterestStartDate(tallyDate);
        
        //查询设置手续费规则对应的会计分录定义，注意！这里不能通过chargeRule的vouchers关联获取。
        chargeRule.setFcVoucherEntryDefines(entryDefineService.getChargeDefinesByRuleId(chargeRule.getId()));
        if(EmptyUtils.isEmptyList(chargeRule.getFcVoucherEntryDefines())){
        	throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_ENTRYDEFINE_NULL);
        }
        //处理借方分录
        FcVoucherEntryDefine debitEntryDefine = chargeRule.getFcVoucherEntryDefines().get(0);
        List<FcAccbookDtl> accbookDtls = new ArrayList<FcAccbookDtl>();
        for (Entry<String, BigDecimal> debitAccountMoney : accountMoneys.entrySet()) {
            FcAccbookDtl accbookDebitDtl = createAccbookDebitDtl(debitEntryDefine, debitAccountMoney.getKey(),
                    debitAccountMoney.getValue(), accDTOMap.get(debitAccountMoney.getKey()));
            accbookDtls.add(accbookDebitDtl);
        }
        //处理贷方分录
        FcVoucherEntryDefine creditEntryDefine = chargeRule.getFcVoucherEntryDefines().get(1);
        FcAccbookDtl accbookCreditDtl = createAccbookCreditDtl(creditEntryDefine, creditTotal, 
        		chargeAccrues.get(0).getAccount(), netCode);
        accbookDtls.add(accbookCreditDtl);
        accbook.setVoucherType(accbookCreditDtl.getVoucherType());//设置凭证类型
        
        voucherVO.setAccbook(accbook);
        voucherVO.setBookDtls(accbookDtls);
        return voucherVO;
    }
	/**
     * 功能介绍 : 创建手续费主表数据。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private FcAccbook createAccbookMainInfo(String busGlobalId, String userCode, String netCode) {
        FcAccbook accbook = new FcAccbook();
        accbook.setBusGlobalId(busGlobalId);
        accbook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//普通
        accbook.setOperatorCode(userCode);
        accbook.setBusOriType(BusOriType.HANDLE_CHARGE.get());
        accbook.setVoucherState(FinaBookConstant.VoucherStatus.STATUS_TEMP.get());
        Date date = new Date();
        accbook.setTallyDate(date);
        accbook.setInterestStartDate(date);
        //获取系统业务品种对应的品种代码
        String busRaceId = busRaceService.getAndSaveNetBusRace(netCode, FinaBookConstant.SysRaceCode.NB_CHARGE_ACCRUE, userCode);
        accbook.setBusRaceId(busRaceId);
        return accbook;
    }
    /**
     * 功能介绍 : 创建手续费借方分录数据。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private FcAccbookDtl createAccbookDebitDtl(FcVoucherEntryDefine debitEntryDefine, String account,
            BigDecimal debitMoney, FcAccDTO fcAccDTO) {
        FcAccbookDtl accbookDtl = new FcAccbookDtl();
        accbookDtl.setVoucherType(debitEntryDefine.getVoucherType());
        accbookDtl.setSummaryContent(debitEntryDefine.getSummary());
        accbookDtl.setDebitMoney(debitMoney);
        accbookDtl.setCreditMoney(BigDecimal.ZERO);
        accbookDtl.setAccount(account);
        accbookDtl.setAccName(fcAccDTO.getAccName());
        return accbookDtl;
    }
    /**
     * 功能介绍 : 创建手续费贷方分录数据。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private FcAccbookDtl createAccbookCreditDtl(FcVoucherEntryDefine creditEntryDefine, BigDecimal creditMoney,
    		String chargeAccount, String netId) {
        FcAccbookDtl accbookDtl = new FcAccbookDtl();
        accbookDtl.setVoucherType(creditEntryDefine.getVoucherType());
        accbookDtl.setSummaryContent(creditEntryDefine.getSummary());
        accbookDtl.setCreditMoney(creditMoney);
        accbookDtl.setDebitMoney(BigDecimal.ZERO);
        FcAccDTO fcAcc = new FcAccDTO();
        
        if (!StringUtils.isEmpty(creditEntryDefine.getAccount())) {//规则中已定义手续费账号
        	fcAcc = this.valChargeAccountCur(creditEntryDefine.getAccount(), chargeAccount, netId, creditEntryDefine.getSubjectCode());
        } else {
        	List<FcAccDTO> fcAccs = accService.getFcAccBySubjectCode(chargeAccount, netId, creditEntryDefine.getSubjectCode());
    		if(fcAccs.isEmpty()){
    			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_ACCOUNT_NO);
    		}
    		if(fcAccs.size() > 1){
    			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_ACCOUNT_MANY);
    		}
    		fcAcc = fcAccs.get(0);
        }
        accbookDtl.setAccount(fcAcc.getBlnctrAcc());
        accbookDtl.setAccName(fcAcc.getAccName());
        return accbookDtl;
    }
    /**
     * 功能介绍 : 获取手续费账号。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private FcAccDTO valChargeAccountCur(String defineAccount, String chargeAccount, String netId, String subjectCode){
    	FcAccDTO fcAcc = new FcAccDTO();
    	boolean curFlag = accService.getFcAccCurCode(defineAccount, chargeAccount);
    	if(!curFlag){
    		List<FcAccDTO> fcAccs = accService.getFcAccBySubjectCode(chargeAccount, netId, subjectCode);
    		if(fcAccs.isEmpty()){
    			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_ACCOUNT_NO);
    		}
    		if(fcAccs.size() > 1){
    			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_CHARGE_ACCOUNT_MANY);
    		}
    		fcAcc = fcAccs.get(0);
    	} else {
    		fcAcc = getFcAccDTOByAccount(defineAccount);
    	}
    	return fcAcc;
    }
	
    /**
     * 功能介绍 : 查询账户相关信息。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    public FcAccDTO getFcAccDTOByAccount(String account){
    	return accService.getAccDTOByAccount(account);
    }
    
    /**
	 * 功能介绍 : 根据凭证流水号查询未计提记录。<br>
	 * <b>日期：</b>2017年7月5日<br>
	 * @version 1.0
	 * @author liyh 
	 */
    public List<FcChargeAccrue> queryChargeAccrueByOrderNo(Set<String> orderNos) {
		return fcChargeAccrueDao.queryChargeAccrueByOrderNo(orderNos);
	}
	
    /**
	 * 功能介绍 : 未入账手续费。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public boolean queryNoTallyChargeAccrue(FcMonthCloseDTO closeDTO) {
		List<FcAccVO> fcAccVOList = iFcAccService.listBy("netId", closeDTO.getNetCode());
		if (CollectionUtils.isEmpty(fcAccVOList)) {
			return false;
		}
		List<String> accounts = fcAccVOList.stream().map(FcAccVO::getBlnctrAcc).collect(Collectors.toList());
		List<FcChargeAccrue> list = fcChargeAccrueDao.queryNoTallyChargeAccrue(closeDTO,accounts);
		if (null == list || list.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/**
	 * <b>功能：</b>根据账号获取未结转手续费<br>
	 * <b>作者：</b>yangl<br>
	 * <b>日期：</b>2017-09-28<br>
	 * 
	 * @param blnctrAcc
	 * @return
	 */
	public Map<String, BigDecimal> getNotOverChargeByAccount(String blnctrAcc) {
		return fcChargeAccrueDao.getNotOverChargeByAccount(blnctrAcc);
	}
	
	/**
     * 功能介绍 : 标识计提记录为删除。<br>
     * <b>日期：</b>2017年11月8日<br>
     * @version 1.0
     * @author liyh 
     */
    public void invalidChargeAccrue(String chargeId) {
    	fcChargeAccrueDao.invalidChargeAccrue(chargeId);
    }
	
}
