package com.bytter.biz.finacial.interest.service.proccessor;

import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.interest.bean.*;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.finacial.interest.vo.InterestResult;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.FcAccDTO;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>类功能：</b>利息处理基类<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月11日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Transactional
public class BaseInterestProcessor implements Serializable {
	private InterestInfoContainer interestInfoContainer;
	
//	@DubboReference(check = false)
	private IFcAccService fcAccServiceImpl;
	
	public BaseInterestProcessor(InterestInfoContainer interestInfoContainer,IFcAccService iFcAccService) {
        this.interestInfoContainer = interestInfoContainer;
        this.fcAccServiceImpl = iFcAccService;
        //TODO 另外一种方法 在调用方 注册ReferenceBean，再放开以下代码
//        if(null == iFcAccService){
//        	iFcAccService = (IFcAccService) ApplicationContextUtil.getContext().getBean("fcAccServiceI");
//        }
    }
	
	/**
	 * <b>功能介绍：</b>结息<br>
	 * <b>日期：</b>2017年11月1日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
    public InterestResult interest() {
        rushPredrawn();//冲预提
        createPredrawnVoucherInfo();//生成预提凭证
        createInterestVoucherInfo();//生成利息凭证
        processInterestDtls();//结转利息、预提数据
        return processResult();//处理结果
    }
	
	/**
	 * <b>功能介绍:</b>计息<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
    public InterestResult interestaccrual(ICurrentUser userBean ) {
        queryAccuBalanceInfos();//查询计息账户的积数余额
        queryInstRateAdjustInfos();//查询计息账户的利率调整记录
        groupingByInterestType();//按结息或预提分组数据
        calcInterest();//计算利息
        saveTempInterest(userBean);//暂存利息 并处理消息发送
        return processResult();//处理结果
    }
    
    /**
     * <b>功能介绍：</b>计算还款金额利息<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @return
     */
    public InterestResult calRePayMoneyInterest() {
        groupAccuBalanceByInterestType();
        calcInterest();
        return null;
    }
    
    
    /**
     * <b>功能介绍：</b>生成利息凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected void createInterestVoucherInfo() {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            AccInterestInfo accInterestInfo = entry.getValue();
            accInterestInfo.setPoolSign(interestInfoContainer.isPoolSign());
            accInterestInfo.setAccBean(interestInfoContainer.getAccBean());//计息Bean
            InterestHelper.getInterestProvider(entry.getKey().getaccType()).processInterestVoucher(accInterestInfo);
        }
    }
    
    /**
     * <b>功能介绍：</b>结转利息、预提数据<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void processInterestDtls() {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            InterestHelper.getInterestProvider(entry.getKey().getaccType()).processInterestDtls(entry.getValue());
        }
    }
    
    /**
     * <b>功能介绍：</b>冲预提<br>
     * <b>日期：</b>2017年11月1日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected void rushPredrawn() {
        if(InterestConstant.GENERATEMODE_INTEREST_PART.equals(interestInfoContainer.getGenerateMode())){
            return;
        }
        //判断是否能冲预提
        String rushWay = interestInfoContainer.getSysPredrawRushWay();
        if (InterestConstant.PREDRAW_RUSH_WAY_INTEREST_DAY.equals(rushWay)) { //计息日预提冲销
            if (!interestInfoContainer.isSysInstDay()||(!interestInfoContainer.isSeasonEnd())) { //非计息日不处理
                return;
            }
        } else if (InterestConstant.PREDRAW_RUSH_WAY_MONTH_END.equals(rushWay)) { //月末预提月初冲销
            if (!InterestHelper.isMonthFirstWorkDay(interestInfoContainer.getEndInstDate())) { //非月初第一个工作日不处理
                return;
            }
        }
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            AccTypeKeyBean accBean = entry.getKey();
            String accTypeCode = accBean.getaccType();
            AccInterestInfo accInterestInfo = entry.getValue();
            IInterestProvider interestProvider = InterestHelper.getInterestProvider(accTypeCode);
            accInterestInfo.setFcPredrawDtls(interestProvider.queryUnRushPredrawDtls(
                    interestInfoContainer.getNetCode(), accTypeCode, "", interestInfoContainer.getEndInstDate()));
            accInterestInfo.setNeedSave(true);
            accInterestInfo.setAutoRush(true);
            accInterestInfo.setPoolSign(interestInfoContainer.isPoolSign());//资金池标识
            
            setAccBookOper(accInterestInfo, accTypeCode);
            InterestHelper.getInterestProvider(accTypeCode).rushPredrawn(accInterestInfo);
        }
    }
    
    /**
     * <b>功能介绍：</b>外部冲预提接口(只处理冲预提，不产生凭证)<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void interestRush(){
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            AccTypeKeyBean accBean = entry.getKey();
            String accTypeCode = accBean.getaccType();
            AccInterestInfo accInterestInfo = entry.getValue();
            accInterestInfo.setNeedSave(false);
            accInterestInfo.setAutoRush(true);
            InterestHelper.getInterestProvider(accTypeCode).rushPredrawn(accInterestInfo);
        }
    }
    
    /**
     * <b>功能介绍：</b>设置账务记账人<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void setAccBookOper(AccInterestInfo accInterestInfo, String accTypeCode) {
        List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        FcInterestDtlTemp interestDtlTemp = null;
        if (interestDtls.isEmpty() || interestDtls.size() == 0) {
            List<FcPredrawDtlTempVO> predrawDtlTemps = accInterestInfo.getPredrawDtls();
            if (predrawDtlTemps.isEmpty() || predrawDtlTemps.size() == 0) {
                return;
            }
            List<FcInterestDtlTemp> interestDtlTemps = predrawDtlTemps.get(0).getInterestDtls();
            if (EmptyUtils.isEmptyList(interestDtlTemps)) {
                return;
            }
            interestDtlTemp = interestDtlTemps.get(0);
            InterestHelper.getInterestProvider(accTypeCode).setAccbookTallyCode(interestDtlTemp.getOperatorCode());
        } else {
            interestDtlTemp = interestDtls.get(0);
            InterestHelper.getInterestProvider(accTypeCode).setAccbookTallyCode(interestDtlTemp.getOperatorCode());
        }
    }
    
    /**
     * <b>功能介绍:</b>查询计息账户的积数余额<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void queryAccuBalanceInfos() {
    	List<FcAccuBalancef> accuBalances = new ArrayList<FcAccuBalancef>();
        List<FcAccuBalancef> queryResult = null;
        if(InterestConstant.GENERATEMODE_INTEREST_PART.equals(interestInfoContainer.getGenerateMode())){//个别计息
            //查计息账户的积数
            for (String accTypeCode : interestInfoContainer.getAccTypeCodes()) {
                if (InterestConstant.PREDRAW_RUSH_WAY_MONTH_END.equals(interestInfoContainer.getSysPredrawRushWay())) {
                    //查询指定内部账户积数
                    queryResult = InterestHelper.getInterestProvider(accTypeCode).queryAccAccuBalance(interestInfoContainer.getAccount(), DateUtils.addDay(interestInfoContainer.getEndInstDate(), 1));
                } else {
                    //查询指定内部账户积数
                    queryResult = InterestHelper.getInterestProvider(accTypeCode).queryAccAccuBalance(interestInfoContainer.getAccount(), interestInfoContainer.getEndInstDate());
                }
                if (InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accTypeCode) || InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accTypeCode) || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accTypeCode)) {
                    for (FcAccuBalancef accuBalance : queryResult) {
                        accuBalance.setCumu(interestInfoContainer.getTellerMoney());//设置为取款金额
                    }
                }
                if (queryResult != null && queryResult.size() > 0) {
                    accuBalances.addAll(queryResult);
                }
            }
        } else {//批量计息
            for (String accTypeCode : interestInfoContainer.getAccTypeCodes()) {
                if (InterestConstant.PREDRAW_RUSH_WAY_MONTH_END.equals(interestInfoContainer.getSysPredrawRushWay())) {
                    queryResult = InterestHelper.getInterestProvider(accTypeCode).queryAccuBalanceInfos(interestInfoContainer.getNetCode(), accTypeCode, DateUtils.addDay(interestInfoContainer.getEndInstDate(), 1));
                } else {
                    queryResult = InterestHelper.getInterestProvider(accTypeCode).queryAccuBalanceInfos(interestInfoContainer.getNetCode(), accTypeCode, interestInfoContainer.getEndInstDate());
                }
                if (queryResult != null && queryResult.size() > 0) {
                    accuBalances.addAll(queryResult);
                }
            }
        }
        List<FcAccuBalancef> accuBalancesNew = mergeAccuBalances(accuBalances);
        interestInfoContainer.setAccuBalances(accuBalancesNew);
        setInteretAcc(accuBalancesNew);//将所有积数的账号放到账号集合里
    }
    
    /**
     * <b>功能介绍：</b>账号相同日期的积数合并<br>
     * <b>日期：</b>2017年11月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param accuBalances
     * @return
     */
    private List<FcAccuBalancef> mergeAccuBalances(List<FcAccuBalancef> accuBalances){
        List<FcAccuBalancef> accuBalancesNew = new ArrayList<FcAccuBalancef>();
        Map<String, FcAccuBalancef> accb = new HashMap<String, FcAccuBalancef>();
        Map<Date, List<FcAccuBalancef>> dateMap = accuBalances.stream().filter(item -> StringUtils.isNotEmpty(item.getBalanceDate())).collect(Collectors.groupingBy(FcAccuBalancef::getBalanceDate));
        for (FcAccuBalancef accub : accuBalances) {
            String agreeDepositSign = accub.getAgreedDepositSign();//协定标识
            if (EmptyUtils.isEmpty(agreeDepositSign)) {
                agreeDepositSign = CommonConstant.VALID_SIGN_NO;
            }
            Integer agreeType = accub.getAgreedType();
            String agreeTypeStr = CloseConstant.DEFAULT_AGREE_TYPE;
            if (!EmptyUtils.isEmpty(agreeType)) {
                agreeTypeStr = agreeType.toString();
            }

            //如果一天内存在多条积数，且有积数已经结息或者预提并且积数、调整积数为0则不参与利息计算。
            String key = accub.getAccount() + "_" + DateUtils.date2Str(accub.getBalanceDate(), DateUtils.date_sdf()) + "_" + agreeDepositSign + "_" + agreeTypeStr;
            FcAccuBalancef oldAcc = accb.get(key);
            if (null != accub.getBalanceDate() && dateMap.get(accub.getBalanceDate()).size() >= 2) {
                if (CommonConstant.NO.equals(accub.getPreSign()) && CommonConstant.NO.equals(accub.getInterestSign())) {
                    if (accub.getAdjustCumu().compareTo(BigDecimal.ZERO) != 0 || accub.getCumu().compareTo(BigDecimal.ZERO) != 0) {
                        accuBalancesNew.add(accub);
                    }
                } else {
                    if (EmptyUtils.isEmpty(oldAcc)) {
                        accb.put(key, accub);
                    } else {
                        oldAcc.setCumu(oldAcc.getCumu().add(accub.getCumu()));
                        oldAcc.setAdjustCumu(oldAcc.getAdjustCumu().add(accub.getAdjustCumu()));
                    }
                }
            } else {
                if (EmptyUtils.isEmpty(oldAcc)) {
                    accb.put(key, accub);
                } else {
                    oldAcc.setCumu(oldAcc.getCumu().add(accub.getCumu()));
                    //TODO 这里有问题，当 数据来源是 FixedInterestProvider.queryAccAccuBalance时， adjustCumn 为空
                    if (oldAcc.getAdjustCumu() != null && accub.getAdjustCumu() != null){

                        oldAcc.setAdjustCumu(oldAcc.getAdjustCumu().add(accub.getAdjustCumu()));
                    }
                }
            }
        }
        for (Map.Entry<String, FcAccuBalancef> entry : accb.entrySet()) {
            accuBalancesNew.add(entry.getValue());
        }
        Collections.sort(accuBalancesNew, new Comparator<FcAccuBalancef>() {//按账号和日期排序
            public int compare(FcAccuBalancef o1, FcAccuBalancef o2) {
                int i = o1.getAccount().compareTo(o2.getAccount());
                if (i == 0) {
                    return o1.getBalanceDate().compareTo(o2.getBalanceDate());
                }
                return i;
            }
        });
        return accuBalancesNew;
    }
    
    /**
     * <b>功能介绍：</b>设置要计息的账号集<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void setInteretAcc(List<FcAccuBalancef> accuBalances){
    	Map<String,FcAccDTO> fcAccList = new HashMap<String, FcAccDTO>();
    	Set<String> accs = new HashSet<String>();
    	for (FcAccuBalancef accub : accuBalances) {
    		accs.add(accub.getAccount());
		}
    	for (String acc : accs) {
    		fcAccList.put(acc, fcAccServiceImpl.getAccDTOByAccount(acc));
		}
    	interestInfoContainer.setInterestAcc(fcAccList);
    }
    
    /**
     * <b>功能介绍：</b>查询计息账户的利率调整记录<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void queryInstRateAdjustInfos() {
    	Map<String, InstrateAdjustBean> instrateAdjustInfos = new HashMap<String, InstrateAdjustBean>();
        Map<String, InstrateAdjustBean> queryResult = null;
        for (String accTypeCode : interestInfoContainer.getAccTypeCodes()) {
            List<AccInstrateBean> instrateBeans = InterestHelper.getInterestProvider(accTypeCode)
                    .queryInstRateAdjustInfos(interestInfoContainer.getNetCode(), accTypeCode,
                            null, interestInfoContainer.getCorpIds(), interestInfoContainer.getAccount());
            //FIXME 计息账户的余额区间调整记录设置暂时不做
            queryResult = setInstrateAdjust(instrateBeans);
            if (queryResult != null && queryResult.size() > 0) {
                instrateAdjustInfos.putAll(queryResult);
            }
        }
        interestInfoContainer.setInstrateAdjustInfos(instrateAdjustInfos);
    }
    
    /**
     * <b>功能介绍：</b>设置利率调整Bean中余额区间信息<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @SuppressWarnings("unused")
	private void setInstrateBeanInfo(List<AccInstrateBean> instrateBeans, List<AccInstrateZoneBean> instrateZoneBeans){
    	Map<Long, List<AccInstrateZoneBean>> zoneMap = new HashMap<Long, List<AccInstrateZoneBean>>();
        for (AccInstrateZoneBean zoneBean : instrateZoneBeans) {
            Long adjustId = zoneBean.getAdjustId();
            List<AccInstrateZoneBean> tempList = zoneMap.get(adjustId);
            if (tempList == null) {
                tempList = new ArrayList<AccInstrateZoneBean>();
                zoneMap.put(adjustId, tempList);
            }
            tempList.add(zoneBean);
        }
        
        for (AccInstrateBean instrateBean : instrateBeans) {
            String changeType = instrateBean.getChangeType();//调整方式
            if ("3".equals(changeType)) {
            	String adjustId = instrateBean.getAdjustId();
                List<AccInstrateZoneBean> zoneList = zoneMap.get(adjustId);
                if (zoneList != null && !zoneList.isEmpty()) {
                    instrateBean.setZoneBeans(zoneList);
                }
            }
        }
    }
    
    /**
     * <b>功能介绍：</b>添加利率调整方式分组<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private Map<String, InstrateAdjustBean> setInstrateAdjust(List<AccInstrateBean> instrateBeans){
        Map<String, InstrateAdjustBean> result = new HashMap<String, InstrateAdjustBean>();
        for (AccInstrateBean bean : instrateBeans) {
            String account = bean.getAccount();
            String instrtType = bean.getInstrtType();
            InstrateAdjustBean accAdjstInfo = result.get(account);
            if (accAdjstInfo == null) {
                accAdjstInfo = new InstrateAdjustBean();
                result.put(account, accAdjstInfo);
            }
            //添加利率调整方式
            accAdjstInfo.getAccInstratesByAdjustType(instrtType, true).add(bean);
        }
        return result;
    }
    
    /**
     * <b>功能介绍：</b>按结息或预提分组数据<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void groupingByInterestType() {
        Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalanceMap = null;
        Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestMap = new HashMap<AccTypeKeyBean, AccInterestInfo>();
        Map<String, InterestProcessType> accProcessTypeMap = new HashMap<String, InterestProcessType>();
        for (FcAccuBalancef accuBalance : interestInfoContainer.getAccuBalances()) {
        	FcAccDTO fcAcc = interestInfoContainer.getInterestAcc().get(accuBalance.getAccount());
            InterestProcessType interestProcessType = InterestProcessType.NONE;
            if(EmptyUtils.isNotEmpty(interestInfoContainer.getAccount())){
                interestProcessType = InterestProcessType.INTEREST;//个别账户计息采用计息算法
            } else {
                if (OperateType.CALCULATE == interestInfoContainer.getOpType()) { //利息测算，采用预提算法
                    interestProcessType = InterestProcessType.PREDRAW;
                } else if (OperateType.SUM == interestInfoContainer.getOpType()) {
                    interestProcessType = InterestProcessType.INTEREST;//汇总采用计息算法
                } else {
                    interestProcessType = accProcessTypeMap.get(fcAcc.getBlnctrAcc());
                    if (interestProcessType == null) {
                        interestProcessType = getInterestProcessType(fcAcc);
                        accProcessTypeMap.put(fcAcc.getBlnctrAcc(), interestProcessType);
                    }
                }
            }
            if (interestProcessType != InterestProcessType.NONE) {
                AccTypeKeyBean accTypeKeyBean = new AccTypeKeyBean(fcAcc.getNetId(), fcAcc.getCurId(),
                        fcAcc.getAccType());
                AccInterestInfo accInterestInfo = accTypeInterestMap.get(accTypeKeyBean);
                if (accInterestInfo == null) {
                    accInterestInfo = new AccInterestInfo();
                    if (interestInfoContainer.getInterestTime() == null) {
                        interestInfoContainer.setInterestTime(DateUtils.getDate());
                    }
                    accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
                    accInterestInfo.setContainer(interestInfoContainer);
                    interestAccuBalanceMap = new HashMap<InterestProcessType, Map<String, List<FcAccuBalancef>>>();
                    accInterestInfo.setInterestAccuBalances(interestAccuBalanceMap);
                    accTypeInterestMap.put(accTypeKeyBean, accInterestInfo);
                }
                interestAccuBalanceMap = accInterestInfo.getInterestAccuBalances();
                Map<String, List<FcAccuBalancef>> accuBalanceMap = interestAccuBalanceMap.get(interestProcessType);
                if (accuBalanceMap == null) {
                    accuBalanceMap = new HashMap<String, List<FcAccuBalancef>>();
                    interestAccuBalanceMap.put(interestProcessType, accuBalanceMap);
                }
                List<FcAccuBalancef> accuBalanceList = accuBalanceMap.get(fcAcc.getBlnctrAcc());
                if (accuBalanceList == null) {
                    accuBalanceList = new ArrayList<FcAccuBalancef>();
                    accuBalanceMap.put(fcAcc.getBlnctrAcc(), accuBalanceList);
                }
                accuBalanceList.add(accuBalance);
            }
        }
        interestInfoContainer.setAccTypeInterestInfos(accTypeInterestMap);
    }
	
	
    /**
     * <b>功能介绍：</b>判断预提还是结息<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private InterestProcessType getInterestProcessType(FcAccDTO fcAcc) {
    	InterestProcessType interestProcessType = InterestProcessType.NONE;
        String accPredrawSign = fcAcc.getPredrawSign(); //账户预提类型
        if (InterestConstant.PREDRAW_SIGN_SEASON_ANTICIPATE.equals(accPredrawSign)) { //季末结息预提
        	if (interestInfoContainer.isSeasonEnd()) { //季末
                if (InterestConstant.BATCH_INST_WAY_INTEREST_DAY.equals(interestInfoContainer.getSysBatchInterestWay())) { //结息方式为计息日结息
                    if (interestInfoContainer.isSysInstDay()) { //计息日则结息
                        interestProcessType = InterestProcessType.INTEREST;
                    } else { //非计息日则不处理
                        interestProcessType = InterestProcessType.NONE;
                    }
                } else { //结息方式为不控制则结息
                    interestProcessType = InterestProcessType.INTEREST;
                }
            } else { // 非季末则预提
                interestProcessType = InterestProcessType.PREDRAW;
            }
        }else if (InterestConstant.PREDRAW_SIGN_SEASON_NO_ANTICIPATE.equals(accPredrawSign)) { //季末结息不预提
            if (interestInfoContainer.isSeasonEnd()) { //季末
                if (InterestConstant.BATCH_INST_WAY_INTEREST_DAY.equals(interestInfoContainer.getSysBatchInterestWay())) { //结息方式为计息日结息
                    if (interestInfoContainer.isSysInstDay()) { //计息日则结息
                        interestProcessType = InterestProcessType.INTEREST;
                    } else { //非计息日则不处理
                        interestProcessType = InterestProcessType.NONE;
                    }
                } else { //结息方式为不控制则结息
                    interestProcessType = InterestProcessType.INTEREST;
                }
            } else { //非季末则不处理
                interestProcessType = InterestProcessType.NONE;
            }
        } else if (InterestConstant.PREDRAW_SIGN_MONTH_INTEREST.equals(accPredrawSign)) { //每月结息
            if (InterestConstant.BATCH_INST_WAY_INTEREST_DAY.equals(interestInfoContainer.getSysBatchInterestWay())) { //结息方式为计息日结息
                if (interestInfoContainer.isSysInstDay()) { //计息日则结息
                    interestProcessType = InterestProcessType.INTEREST;
                } else { //非计息日则不处理
                    interestProcessType = InterestProcessType.NONE;
                }
            } else { //结息方式为不控制
                interestProcessType = InterestProcessType.INTEREST;
            }
        } else if (InterestConstant.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.equals(accPredrawSign)) { //季末不结息预提
            interestProcessType = InterestProcessType.PREDRAW;
        } else if (InterestConstant.PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE.equals(accPredrawSign)) { //季末不结息不预提
        	// 季末不结息预提即每次做预提处理
            interestProcessType = InterestProcessType.NONE;
        }
        if (interestProcessType == InterestProcessType.PREDRAW) { //预提参数判断
            if (InterestConstant.PREDRAW_RUSH_WAY_INTEREST_DAY.equals(interestInfoContainer.getSysPredrawRushWay())) { //计息日预提冲销
                if (!interestInfoContainer.isSysInstDay()) { //非计息日不处理
                    interestProcessType = InterestProcessType.NONE;
                }
            } else if (InterestConstant.PREDRAW_RUSH_WAY_MONTH_END.equals(interestInfoContainer.getSysPredrawRushWay())) { //月末预提月初冲销
                if (!interestInfoContainer.isLastWorkDay()) { //非月末最后一个工作日不处理
                    interestProcessType = InterestProcessType.NONE;
                }
            }
        }
        return interestProcessType;
    }
    
    
    /**
     * <b>功能介绍：</b>还款金额积数分组<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void groupAccuBalanceByInterestType() {
    	FcAccDTO fcAcc = null;
    	AccTypeKeyBean accTypeKeyBean = null;
        AccInterestInfo accInterestInfo = null;
        List<FcAccuBalancef> accuBalanceList = null;
        InterestProcessType interestProcessType = null;
        Map<String, List<FcAccuBalancef>> accuBalanceMap = null;
        Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalanceMap = null;
        Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestMap = new HashMap<AccTypeKeyBean, AccInterestInfo>();
        setInteretAcc(interestInfoContainer.getAccuBalances());//设置账号集
        Map<String, FcAccDTO> interestAcc = interestInfoContainer.getInterestAcc();//取出积数中所有账号集
        for (FcAccuBalancef accuBalance : interestInfoContainer.getAccuBalances()) {
            fcAcc = interestAcc.get(accuBalance.getAccount());
            interestProcessType = InterestProcessType.INTEREST;
            accTypeKeyBean = new AccTypeKeyBean(fcAcc.getNetId(), fcAcc.getCurId(), fcAcc.getAccType());
            accInterestInfo = accTypeInterestMap.get(accTypeKeyBean);
            if (accInterestInfo == null) {
                accInterestInfo = new AccInterestInfo();
                accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
                accInterestInfo.setContainer(interestInfoContainer);
                interestAccuBalanceMap = new HashMap<InterestProcessType, Map<String, List<FcAccuBalancef>>>();
                accInterestInfo.setInterestAccuBalances(interestAccuBalanceMap);
                accTypeInterestMap.put(accTypeKeyBean, accInterestInfo);
            }
            interestAccuBalanceMap = accInterestInfo.getInterestAccuBalances();
            accuBalanceMap = interestAccuBalanceMap.get(interestProcessType);
            if (accuBalanceMap == null) {
                accuBalanceMap = new HashMap<String, List<FcAccuBalancef>>();
                interestAccuBalanceMap.put(interestProcessType, accuBalanceMap);
            }
            accuBalanceList = accuBalanceMap.get(fcAcc.getBlnctrAcc());
            if (accuBalanceList == null) {
                accuBalanceList = new ArrayList<FcAccuBalancef>();
                accuBalanceMap.put(fcAcc.getBlnctrAcc(), accuBalanceList);
            }
            accuBalanceList.add(accuBalance);
        }
        interestInfoContainer.setAccTypeInterestInfos(accTypeInterestMap);
    }
    
    /**
     * <b>功能介绍：</b>计算利息<br>
     * <b>日期：</b>2017年11月1日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected void calcInterest() {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            InterestHelper.getInterestProvider(entry.getKey().getaccType()).calcInterest(entry.getValue());
        }
    }
    
    /**
     * <b>功能介绍：</b>暂存利息<br>
     * <b>日期：</b>2017年11月1日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected void saveTempInterest(ICurrentUser userBean ) {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            InterestHelper.getInterestProvider(entry.getKey().getaccType()).saveTempInterest(entry.getValue(),entry.getKey().getnetId(), userBean, interestInfoContainer.getGenerateMode());
        }
    }
    
    /**
     * <b>功能介绍：</b>生成预提凭证<br>
     * <b>日期：</b>2017年11月1日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected void createPredrawnVoucherInfo() {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
            InterestHelper.getInterestProvider(entry.getKey().getaccType())
                    .processPredrawnVoucher(entry.getValue());
        }
    }
    
    /**
     * <b>功能介绍：</b>处理结果<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected InterestResult processResult() {
        InterestResult result=null;//默认是批量计息
        //个别计息需返回利息清单数据
        if (InterestConstant.GENERATEMODE_INTEREST_PART.equals(interestInfoContainer.getGenerateMode())){
            result = new InterestResult();
            for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos().entrySet()) {
                AccInterestInfo interestInfo = entry.getValue();
                if (EmptyUtils.isEmpty(interestInfo)) {
                    interestInfo = new AccInterestInfo();
                }
                result.setAccInterestInfo(interestInfo);
            }
        }
        return result;
    }
    /**
     * <b>功能介绍:</b>个别账户计息<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public InterestResult istAccrual( ICurrentUser userBean) {
        queryAccAccuBalance();
        queryInstRateAdjustInfos();
        groupingByInterestType();
        calcInterest();
        saveTempInterest(userBean);//22
        return processResult();
    }
    /**
     * <b>功能介绍:</b>查询计息账户的积数余额<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void queryAccAccuBalance() {
        String account = interestInfoContainer.getAccount();
        Map<String,FcAccDTO> fcAccList = new HashMap<String, FcAccDTO>();
        FcAccDTO accDTO = fcAccServiceImpl.getAccDTOByAccount(account);
        //查询积数
        List<FcAccuBalancef> accuBalances = InterestHelper.getInterestProvider(accDTO.getAccType()).queryAccAccuBalance(
                interestInfoContainer.getAccount(), DateUtils.addDay(interestInfoContainer.getEndInstDate(), 1));
        fcAccList.put(account, fcAccServiceImpl.getAccDTOByAccount(account));
        interestInfoContainer.setInterestAcc(fcAccList);//设置计息账户
        List<FcAccuBalancef> accuBalancesNew = mergeAccuBalances(accuBalances);
        interestInfoContainer.setAccuBalances(accuBalancesNew);
    }
    /**
    * <br>
    * <b>功能：</b> 处理利息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/20 0020 <br>
    */
    public InterestResult singleInterest() {
        processInterestDtls();
        return processResult();
    }
    /**
    * <br>
    * <b>功能：</b> 个别利息测算 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    public InterestResult singleInterestaccrual(ICurrentUser userBean) {
        //splitAccyBalances();
        setInteretAcc(interestInfoContainer.getAccuBalances());//将所有积数的账号放到账号集合里
        queryInstRateAdjustInfos();
        groupingByInterestType();
        calcInterest();
        saveTempInterest(userBean);//33
        //积数更新为已计息，并保存积数与还款之间的对应关系
        updateAccuBalanceSign();
        return processResult();
    }
    /**
    * <br>
    * <b>功能：</b> 拆分正常积数为还款积数和剩余积数 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    public void splitAccyBalances() {
        queryAccuBalanceInfos();
        //根据取款金额拆分积数
        List<FcAccuBalancef> accuBalances = interestInfoContainer.getAccuBalances();
        BigDecimal money = interestInfoContainer.getTellerMoney();//取款金额
        if (money != null && money.compareTo(BigDecimal.ZERO) > 0) {
            //2、对积数进行拆分
            List<FcAccuBalancef> repayAccuBalances = new ArrayList<FcAccuBalancef>();//归还积数
            List<FcAccuBalancef> unPayAccuBalances = new ArrayList<FcAccuBalancef>();//剩余未归还积数
            FcAccuBalancef rePayAccuBalance = new FcAccuBalancef();
            FcAccuBalancef unPayAccuBalance = new FcAccuBalancef();
            for (FcAccuBalancef istFcAccuBalance : accuBalances) {
                rePayAccuBalance = new FcAccuBalancef();
                unPayAccuBalance = new FcAccuBalancef();
                if (istFcAccuBalance.getCumu().compareTo(money) >= 0) {
                    //积数等于还款金额,该积数为本次还款的积数，不需要拆分
                    BeanUtils.copyProperties(istFcAccuBalance, rePayAccuBalance);
                    rePayAccuBalance.setCumu(money);
                    rePayAccuBalance.setId(null);
                    BeanUtils.copyProperties(istFcAccuBalance, unPayAccuBalance);
                    unPayAccuBalance.setCumu(istFcAccuBalance.getCumu().subtract(money));
                    //计算拆分调整积数=原调整积数*(取款金额/原积数)
                    if (istFcAccuBalance.getAdjustCumu() != null) {
                        BigDecimal aAdjustCumu = new BigDecimal(0);
                        if (istFcAccuBalance.getCumu().compareTo(new BigDecimal(0)) != 0) {
                            aAdjustCumu = (istFcAccuBalance.getAdjustCumu().multiply(money)).divide(istFcAccuBalance.getCumu(), 2);
                        } else {
                            aAdjustCumu = istFcAccuBalance.getAdjustCumu();
                        }
                        rePayAccuBalance.setAdjustCumu(aAdjustCumu);
                        unPayAccuBalance.setAdjustCumu(istFcAccuBalance.getAdjustCumu().subtract(aAdjustCumu));
                    }
                    repayAccuBalances.add(rePayAccuBalance);
                    unPayAccuBalances.add(unPayAccuBalance);
                } else {
                    //积数小于还款金额，积数全部计算
                    BeanUtils.copyProperties(istFcAccuBalance, rePayAccuBalance);
                    rePayAccuBalance.setId(null);
                    rePayAccuBalance.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
                    rePayAccuBalance.setInterestTime(DateUtils.getDate());
                    repayAccuBalances.add(rePayAccuBalance);
                    BeanUtils.copyProperties(istFcAccuBalance, unPayAccuBalance);
                    unPayAccuBalance.setCumu(BigDecimal.ZERO);
                    unPayAccuBalance.setAdjustCumu(null);
                    unPayAccuBalances.add(unPayAccuBalance);
                }
            }
            interestInfoContainer.setAccuBalances(repayAccuBalances);//本次需要计息的积数
            if (OperateType.SUBMIT.equals(interestInfoContainer.getOpType())) {
                //新增积数
            }
        }
    }
    /**
     * <br>
     * <b>功能：</b> 新增拆分积数 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    public void saveNewAccuBalance(List<FcAccuBalancef> accuBalances) {
    }
    /**
    * <br>
    * <b>功能：</b> 更新积数为已计息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    public void updateAccuBalanceSign() {
        if (!OperateType.SUBMIT.equals(interestInfoContainer.getOpType())) {
            return;
        }
        List<FcAccuBalancef> accuBalances = interestInfoContainer.getAccuBalances();
        List<String> ids = new ArrayList<>();
        for (FcAccuBalancef accuBalancef : accuBalances) {
            ids.add(accuBalancef.getId());
        }
        for (String accTypeCode : interestInfoContainer.getAccTypeCodes()) {
            //查询指定内部账户积数
            InterestHelper.getInterestProvider(accTypeCode).updateAccuBalancesInterestSign(interestInfoContainer.getBillCode(),
                    ids, interestInfoContainer.getInterestTime());
        }
    }
}
