package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.biz.finacial.api.dto.FcAccBalanceDTO;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.check.dao.FcCompareCenterAutoDao;
import com.bytter.check.dao.FcCompareCenterDao;
import com.bytter.check.entity.FcBankBalanceAdjust;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.FcBankCheckVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Service("fcCompareCenterAutoService")
@Transactional
public class FcCompareCenterAutoService extends BaseService<FcCompareAcc,String> {
	@Autowired
	private FcCompareCenterDao fcCompareAccDao;
	@Autowired
	private FcCompareCenterAutoDao fcCompareCenterAutoDao;
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
	private IBtCurrencyService iBtCurrencyService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IFcAccBalanceService fcAccBalanceService;
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
	 * <br>
	 * <b>功能：</b> 查询批量对账列表 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2020/10/15 0015 <br>
	 */
	public DataTablesOutput queryCheckList(DataTablesInput input, ICurrentUser user, Date calcDate, String compareSign) {
		List<String> corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK, user.getId());
		Map<String, Object> params = new HashMap<>();
		String accSql= fcCompareCenterAutoDao.queryCheckListTheAccSql( input,  corpAuth, params);
		List<BtBankAccDTO> bankAccList=btBankAccService.getBankAccByCorpIdList(corpAuth,accSql, params);
		Map<String,BtBankAccDTO> accMap=new HashMap<>();
		List<String> accList=new ArrayList<>();
		for(BtBankAccDTO acc:bankAccList){
			accList.add(acc.getBankAcc());
			accMap.put(acc.getBankAcc(),acc);
		}
		DataTablesOutput output = fcCompareCenterAutoDao.queryCheckList(input, accList, calcDate,compareSign);
		List<FcBankCheckVO> voList=output.getData();
		if(voList.size()>0){
			Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
			for (FcBankCheckVO vo : voList) {
				if(StringUtils.isNotEmpty(vo.getCurId())) {
					vo.setCurName(curMap.get(vo.getCurId()).getCurName());
				}
				BtBankAccDTO bankAccDTO = accMap.get(vo.getBankAcc());
				if(!EmptyUtils.isEmpty(bankAccDTO)) {
					vo.setBankName(bankAccDTO.getBankName());
					vo.setAccName(bankAccDTO.getAccName());
					vo.setCorpId(bankAccDTO.getCorpId());
					vo.setCorpName(bankAccDTO.getCorpName());
				}
			}
		}
		return output;
	}
	/**
	 * <br>
	 * <b>功能：</b> 查询批量结转列表 <br>
	 * <b>作者：</b>liyh<br>
	 * <b>日期：</b> 2020/10/15 0015 <br>
	 */
	public DataTablesOutput queryAdjustList(DataTablesInput input, ICurrentUser user, Date calcDate, String compareSign) {
		String transTime =(String)input.getFormdata().get("transTime");
		String compareCalDateString = new StringBuilder("bt_to_date('").append(transTime).append("','yyyy-MM-dd')").toString();
		String account =  (String) input.getFormdata().get("account");
		String bankAcc =  (String) input.getFormdata().get("bankAcc");
		//查询账户
		List<String> accS = new ArrayList<>();
		List<String> corpAuth = new ArrayList<>();
		corpAuth = iCorpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CHECK, user.getId());
		Map<String, BtBankAccDTO> accMap = new HashMap<>();
		if (!EmptyUtils.isEmpty(bankAcc)) {	//将bankacc 拆分为list，拼接查询条件，避免in条件超长
			accS = StringUtils.splitToList(",",bankAcc);
			accMap = btBankAccService.getBankAccByInfoAccs(accS);
		}else{
			List<BtBankAccDTO> accList = btBankAccService.getListByCorpIds(corpAuth);
			for (BtBankAccDTO acc : accList) {
				accS.add(acc.getBankAcc());
				accMap.put(acc.getBankAcc(),acc);
			}
		}
		//获取结转基本信息（账户初始化余额、对账日期、未对账笔数等）
		DataTablesOutput output = fcCompareCenterAutoDao.getAdjustInfo(input,compareCalDateString,accS,calcDate,compareSign);
		try {
			List<FcBankCheckVO> bankCheckList = output.getData();
			if(bankCheckList.size()>0) {
				//单位和币别
				Map<String, BtCurrencyDTO> curMap = iBtCurrencyService.getCurrency();
				Map<String, CorpDTO> corpMap = iCorpService.listCorpDtoByCorpIds(corpAuth);
				Date compareCalDate = DateUtils.parse(transTime,"yyyy-MM-dd");
				FcAccBalanceDTO fcAccBalanceDTO = fcAccBalanceService.getBalanceByAccountAndDate(account, compareCalDate);
				List<BisAccBalDTO> bankBallist = accBalService.getBankBal(bankAcc, compareCalDateString);
				//中心未达账数据
				List<Map<String, Object>> ctrStayTotal = fcCompareCenterAutoDao.queryStayOverCtrSum(DateUtils.parseDate(transTime), account, bankAcc, null);
				//银行未达账数据
				List<Map<String, Object>> bankStayTotal = fcCompareCenterAutoDao.queryStayOverBankSum(DateUtils.parseDate(transTime), bankAcc, null);
				//根据结转基本信息，补充账户企业账面余额、银行账面余额
				for (FcBankCheckVO checkVO : bankCheckList) {
					BtBankAccDTO acc =accMap.get(checkVO.getBankAcc());
					checkVO.setBankName(acc.getBankName());
					checkVO.setAccName(acc.getAccName());
					checkVO.setCorpId(acc.getCorpId());
					checkVO.setCurName(curMap.get(checkVO.getCurId()).getCurName() );
					checkVO.setCorpName(corpMap.get(checkVO.getCorpId()).getName());
					boolean isFlagBal = false;    //是否存在银行账户余额，不存在则从初始化余额+-发生额获取
//					for (FcAccountBalanceVo accBalVo : accBallist) {
//						if (checkVO.getAccount().equals(accBalVo.getAccount())
//								&& checkVO.getCurId().equals(accBalVo.getCurId())) {
//							checkVO.setBlnBalance(accBalVo.getBalance());
//						}
//					}
					if(!EmptyUtils.isEmpty(fcAccBalanceDTO)){
						checkVO.setBlnBalance(fcAccBalanceDTO.getBalance());
					}
					for (BisAccBalDTO bankBalVo : bankBallist) {
						if (checkVO.getBankAcc().equals(bankBalVo.getBankAcc())
								&& checkVO.getCurId().equals(bankBalVo.getCurId())) {
							checkVO.setBankBalance(bankBalVo.getBal());
							isFlagBal = true;
						}
					}
					if (!isFlagBal) {
						//银行账户余额表没有余额，则根据初始化余额，结转余额+发生额生成账面余额
						this.getBankInitBal(checkVO, transTime);
					}
					this.loadAdjustBalance(checkVO, ctrStayTotal, bankStayTotal);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return output;
	}
	/**
	 * 获取银行账户余额（初始化余额+发生额）
	 * @param checkVO
	 * @param transDate
	 */
	private void getBankInitBal(FcBankCheckVO checkVO,String transDate){
		Date transDate_ = DateUtils.str2Date(transDate, DateUtils.date_sdf());
		Date lastOverDate = checkVO.getCheckDate();
		BigDecimal bankInitBal = BigDecimal.ZERO;//银行账面初始余额
		FcBankBalanceAdjust adjust = fcCompareAccDao.getLastAdjustOver(checkVO.getAccount(), checkVO.getBankAcc());
		if(EmptyUtils.isEmpty(checkVO.getEndDate())){
			if(EmptyUtils.isEmpty(checkVO.getCheckDate())){//还未初始化账户余额，请先初始化账户余额
				//logger.debug("银行账号：[" + bankAcc + "],erp科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!");
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_BALNOTINIT,checkVO.getBankAcc(),checkVO.getAccount());
			}
			bankInitBal = checkVO.getBankBalance();
		}else{
			lastOverDate = adjust.getEndDate();
			if (!EmptyUtils.isEmpty(checkVO.getCheckDate())) {
				Date checkDate = checkVO.getCheckDate();
				if (checkDate != null && checkDate.after(lastOverDate)) {
					lastOverDate = checkDate;
					bankInitBal = checkVO.getBankBalance();
				}else {
					bankInitBal = adjust.getBankInitBalance();
				}
			} else {
				//"银行账号：[" + bankAcc + "],企业科目：[" + account + "]还未初始化账户余额，请先初始化账户余额!
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_BALNOTINIT,checkVO.getBankAcc(),checkVO.getAccount());
			}
		}
		String lastOverDateStr = DateUtils.date2Str(lastOverDate, DateUtils.date_sdf());
		if (transDate_.before(DateUtils.addDay(lastOverDate, 1))) {//结束日期应大于上次结转日期
			//logger.debug("结束日期应大于上次结转日期[" + lastOverDateStr + "],请重新选择日期!");
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTERCHECK_ENDDATELESSLASTOVERDATE,lastOverDateStr);
		}
		BigDecimal duringMoney = bisAccDtlService.getDuringMoney(checkVO.getCurId(), checkVO.getBankAcc(), lastOverDate, transDate_);//对账期间银行发生额
		BigDecimal bankBalance = bankInitBal.add(duringMoney).setScale(2, BigDecimal.ROUND_HALF_UP);//银行账面余额
		//初始余额+发生额 = 最终银行账户账面余额
		checkVO.setBankBalance(bankBalance);
	}
	/**
	 * <b>功能介绍:获取调节平衡余额</b><br>
	 * <b>日期：</b>2017年6月8日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	public void loadAdjustBalance(FcBankCheckVO checkVO, List<Map<String, Object>> ctrStayTotal,List<Map<String, Object>> bankStayTotal) {
		checkVO.setBankAdjBal(checkVO.getBankBalance());
		checkVO.setBlnAdjBal(checkVO.getBlnBalance());
		for(Map<String, Object> map: ctrStayTotal){
			String compAcc = (String)map.get("compAcc");
			String compCurId = (String)map.get("curId");
			if(checkVO.getAccount().equals(compAcc) && checkVO.getCurId().equals(compCurId)){
				BigDecimal ctrPayMoney = (BigDecimal) map.get("creditMoney");//中心已付、银行未付
				BigDecimal ctrRecMoney = (BigDecimal) map.get("debitMoney");//中心已收、银行未收
				BigDecimal bankAdjustBalance = checkVO.getBankBalance().add(ctrRecMoney).subtract(ctrPayMoney);//银行调节平衡数
				checkVO.setBankAdjBal(bankAdjustBalance);
			}
		}
		for(Map<String, Object> map: bankStayTotal){
			String bankAcc = (String)map.get("bankAcc");
			String bankCurId = (String)map.get("curId");
			if(checkVO.getBankAcc().equals(bankAcc) && checkVO.getCurId().equals(bankCurId)){
				BigDecimal bankPayMoney = (BigDecimal) map.get("debitMoney");//银行已付、中心未付
				BigDecimal bankRecMoney = (BigDecimal) map.get("creditMoney");//银行已收、中心未收
				BigDecimal blnAdjustBalance = checkVO.getBlnBalance().add(bankRecMoney).subtract(bankPayMoney);//单位调节平衡数
				checkVO.setBlnAdjBal(blnAdjustBalance);
			}
		}
	}
	/**
	 * 账号列表详细页面
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public DataTablesOutput listInterAccInfoDetail(DataTablesInput dataTablesInput) {
		DataTablesOutput output = fcAccService.getFcAcc(dataTablesInput);
		return output;
	}
}
