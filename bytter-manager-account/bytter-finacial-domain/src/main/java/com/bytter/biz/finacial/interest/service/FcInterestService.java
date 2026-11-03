package com.bytter.biz.finacial.interest.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcAccBalanceDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.bean.*;
import com.bytter.biz.finacial.interest.dao.FcInterestDao;
import com.bytter.biz.finacial.interest.entity.*;
import com.bytter.biz.finacial.interest.service.proccessor.BaseInterestProcessor;
import com.bytter.biz.finacial.interest.service.proccessor.BaseRushPredrawProccessor;
import com.bytter.biz.finacial.interest.service.proccessor.IInterestProvider;
import com.bytter.biz.finacial.interest.utils.AccTypeAllFilterType;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.*;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("fcInterestService")
@Transactional
public class FcInterestService extends BaseService<FcInterestDtlTemp, String> implements ICorpDisableVerifyService {
    @Autowired
    private FcInterestDao fcInterestDao;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @Autowired
    private FcOnlineLedgerService fcOnlineLedgerService;
    @Autowired(required = false)
    protected IFcVoucherTypeService iFcVoucherTypeService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccBalanceService accBalanceService;
    @Autowired(required = false)
    private FcEntrustLoanRepayService loanRepayService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService iUserService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<FcInterestDtlTemp, String> getEntityDao() {
        return fcInterestDao;
    }
    /**
     * <b>功能介绍：</b>获取所有网点<br>
     * <b>日期：</b>2017年7月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<CorpDTO> getNet() {
        return corpService.listNet();
    }
    /**
     * <b>功能介绍：</b>根据权限资源初始化网点<br>
     * <b>日期：</b>2017年11月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param resouce
     * @return
     * @version 1.0
     */
    public String getInitNetId(String resouce, String userId) {
        List<CorpDTO> authCorps = corpService.listUserCorp(resouce, userId);
        List<CorpDTO> authNets = new ArrayList<CorpDTO>();
        for (CorpDTO corp : authCorps) {
            if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
                authNets.add(corp);
            }
        }
        Collections.sort(authNets, new Comparator<CorpDTO>() {//网点排序
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getSort().compareTo(o2.getSort());
            }
        });
        if (EmptyUtils.isEmptyList(authNets)) {//没有可操作权限的网点
            logger.debug("没有可操作的网点，请分配用户资源权限");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NETNOTAUTH);
        }
        return authNets.get(0).getId();
    }
    /**
     * <b>功能介绍：</b>没有权限的网点(过滤使用)<br>
     * <b>日期：</b>2017年11月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param resouce 资源
     * @return
     * @version 1.0
     */
    public String getNotAuthNets(String resouce, String userId) {
        List<CorpDTO> authCorps = corpService.listUserCorp(resouce, userId);
        List<String> authNets = new ArrayList<String>();
        List<CorpDTO> allNet = corpService.listNet();
        List<String> allNets = new ArrayList<String>();
        String notAuthNet = "";
        for (CorpDTO corp : authCorps) {
            if (CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
                authNets.add(corp.getId());
            }
        }
        for (CorpDTO net : allNet) {
            allNets.add(net.getId());
        }
        allNets.removeAll(authNets);
        for (String corpId : allNets) {
            notAuthNet += corpId + ",";
        }
        return notAuthNet;
    }
    /**
     * <b>功能介绍:加载利息清单</b><br>
     * <b>日期：</b>2017年7月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<Map<String, Object>> queryInterestAccType(int curr, String netCode, String accType,
                                                          String opType, String sessionId, boolean poolSign) {
        return fcInterestDao.queryAccTypeBean(netCode, accType, opType, sessionId, null, null, isInterestZeroSign());
    }
    /**
     * <b>功能介绍:加载利息清单</b><br>
     * <b>日期：</b>2017年7月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public Map<String, Object> queryInterestDtlByAccType(String netId, String accType, String opType, int curr,
                                                         boolean poolSign, String interestSign, String curId, ICurrentUser user) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("ACCTYPE", accType);
        pageInfo.put("INTERESTSIGN", interestSign);
        pageInfo.put("CURID", curId);
        result = fcInterestDao.queryCanEditInterestDtl(curr, netId, accType, opType, user.getId(), pageInfo, isInterestZeroSign());
        if (result.isEmpty()) {
            return result;
        }
        BigDecimal interestTot = BigDecimal.ZERO;
        for (FcInterestDtlTempVo interetDtl : (List<FcInterestDtlTempVo>) result.get("interestDtls")) {
            interestTot = interestTot.add(interetDtl.getInterest());
        }
        Map<String, Object> pageData = (Map<String, Object>) result.get("pageData");
        if (poolSign) {//资金池分录
//            String poolInfo = multiLangServiceI.getLang("finacial.interest.poolNet.Acc")
            String poolInfo = InterestConstant.POOL_INTEREST_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE"));
            poolInfo = multiLangServiceI.getLang(poolInfo);
            result.put("voucherInfo", poolInfo.split(";"));
        } else {
            if (InterestConstant.INTEREST_SIGN_IST.equals(pageData.get("INTERESTSIGN"))) {
                result.put("voucherInfo", InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE")));
            } else {
                result.put("voucherInfo", InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE")));
            }
        }
        result.put("interestTot", interestTot);
        return result;
    }
    /**
     * <b>功能介绍:加载利息清单</b><br>
     * <b>日期：</b>2017年7月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryCanEditInterestDtl(int curr, String netCode, String accType,
                                                       String opType, String sessionId, boolean poolSign, String account, String generateMode) {
        if (EmptyUtils.isEmpty(generateMode)) {
            generateMode = InterestConstant.GENERATEMODE_INTEREST_BATCH;//默认-批量计息
        }
        boolean zeroSign = isInterestZeroSign();//是否显示0利息
        List<Map<String, Object>> datas = fcInterestDao.queryAccTypeBean(netCode, accType, opType, sessionId, account, generateMode, zeroSign);
        Map<String, Object> result = new HashMap<String, Object>();
        if (datas.isEmpty()) {
            return result;
        }
        Map<String, Object> pageInfo = datas.get(curr - 1);
        pageInfo.put("account", account);
        pageInfo.put("generateMode", generateMode);
        result = fcInterestDao.queryCanEditInterestDtl(curr, netCode, accType, opType, sessionId, pageInfo, zeroSign);
        if (result.isEmpty()) {
            return result;
        }
        BigDecimal interestTot = BigDecimal.ZERO;
        for (FcInterestDtlTempVo interetDtl : (List<FcInterestDtlTempVo>) result.get("interestDtls")) {
            interestTot = interestTot.add(interetDtl.getInterest());
        }
        Map<String, Object> pageData = (Map<String, Object>) result.get("pageData");
        if (poolSign) {//资金池分录
//            String poolInfo = multiLangServiceI.getLang("finacial.interest.poolNet.Acc")
            String poolInfo = InterestConstant.POOL_INTEREST_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE"));
            poolInfo = multiLangServiceI.getLang(poolInfo);
            result.put("voucherInfo", poolInfo.split(";"));
        } else {
            if (InterestConstant.INTEREST_SIGN_IST.equals(pageData.get("INTERESTSIGN"))) {
                result.put("voucherInfo", InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE")));
            } else {
                result.put("voucherInfo", InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(pageData.get("ACCTYPE")));
            }
        }
        result.put("interestTot", interestTot);
        result.put("total", datas.size());
        return result;
    }
    /**
     * <b>功能介绍：</b>加载冲预提利息清单<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param curr
     * @param searchBean
     * @param account
     * @param opType
     * @return
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> queryRushFcInterestDtls(int curr, InterestSearchBean searchBean,
                                                       String account, String opType) {
        List<Map<String, String>> datas = fcInterestDao.queryRushAccTypeBean(searchBean, account, opType);
        Map<String, Object> result = new HashMap<String, Object>();
        if (datas.isEmpty()) {
            return result;
        }
        Map<String, String> pageInfo = datas.get(curr - 1);
        result = fcInterestDao.queryRushFcInterestDtls(searchBean, account, opType, pageInfo);
        if (result.isEmpty()) {
            return result;
        }
        BigDecimal interestTot = BigDecimal.ZERO;
        for (FcInterestDtlVo interetDtls : (List<FcInterestDtlVo>) result.get("interestDtls")) {
            interestTot = interestTot.add(interetDtls.getInterest());
        }
        result.put("pageData", pageInfo);
        result.put("voucherInfo", InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(pageInfo.get("ACCTYPE")));
        result.put("interestTot", interestTot);
        result.put("curr", curr);
        result.put("total", datas.size());
        return result;
    }
    /**
     * <b>功能介绍：</b>加载凭证明细(利息暂存)<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, Object> createCanEditInterestVoucher(int curr, String netCode, String accType,
                                                            String opType, ICurrentUser user, boolean poolSign,
                                                            String accountCode, String generateMode, Integer affixAmount) {
        if (EmptyUtils.isEmpty(generateMode)) {
            generateMode = InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        List<Object[]> headlist = new ArrayList<Object[]>();
        boolean isMoreVoucherDtl = isMoreVoucherDtl();//计息凭证类型是否多条分录
        boolean isAccInCenter = true;//利息收入\利息支出账号是否挂中心
        if (isMoreVoucherDtl) {
            headlist = fcInterestDao.queryVoucherHeadList(netCode, accType, opType, user.getId(), true, false, accountCode,generateMode);
        } else {//一借一贷
            headlist = fcInterestDao.queryVoucherHeadList(netCode, accType, opType, user.getId(), false, true, accountCode,generateMode);
        }
        if (headlist.isEmpty()) {
            return result;
        }
        Object[] head = headlist.get(curr - 1);
        String accTypeCode = (String) head[1];
        String curCode = (String) head[0];
        String interestSign = (String) head[2];
        String account = "";
        String corpCode = "";
        if (isMoreVoucherDtl && !isAccInCenter) {
            corpCode = (String) head[4];
        } else {
            account = (String) head[3];
        }
        List<FcInterestDtlTemp> interestDtlList = fcInterestDao.createCanEditInterestVoucher(netCode,
                accTypeCode, curCode, interestSign, opType, user.getId(), account, corpCode,generateMode);
        result = createInterestVoucher(netCode, user, head, interestDtlList, isMoreVoucherDtl, poolSign);
        if (result.isEmpty()) {
            return result;
        } else {
            List<FcFinacialAccbookVO> accbooks = (List<FcFinacialAccbookVO>) result.get("voucherInfo");
            if(EmptyUtils.isNotEmpty(accbooks)) {
                for(FcFinacialAccbookVO accbookVO : accbooks) {
                    if (!EmptyUtils.isEmpty(affixAmount)) {
                        accbookVO.setAffixAmount(affixAmount);
                    } else {
                        accbookVO.setAffixAmount(0);
                    }
                }
            }
        }
        result.put("interestTot", headlist.size());//设置总页数
        result.put("headlist", headlist);
        return result;
    }
    /**
     * <b>功能介绍：</b>加载凭证明细(冲预提)<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param curr
     * @param searchBean
     * @param account
     * @param opType
     * @param user
     * @return
     * @version 1.0
     */
    public Map<String, Object> createCanInterestRushVoucher(int curr, InterestSearchBean searchBean,
                                                            String account, String opType, ICurrentUser user) {
        ShiroUser userBean = new ShiroUser(user.getId(), user.getLoginName(), user.getUsername());
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isMoreVoucherDtl = isMoreVoucherDtl();//计息凭证类型是否多条分录
        List<Map<String, String>> pageHead = null;
        if (isMoreVoucherDtl) {
            pageHead = fcInterestDao.queryRushInterestVoucherHead(searchBean, account, opType, isMoreVoucherDtl);
        } else {//一借一贷
            pageHead = fcInterestDao.queryRushInterestVoucherHead(searchBean, account, opType, isMoreVoucherDtl);
        }
        if (pageHead.isEmpty()) {
            return result;
        }
        Map<String, String> dataMap = pageHead.get(curr - 1);
        InterestInfoContainer container = new InterestInfoContainer();
        container.setTallyDate(DateUtils.getDate());
        container.setOpType(OperateType.RUSH);
        container.setUserBean(userBean);
        container.setEndInstDate(searchBean.getBlnInstDate());
        container.setTallyDate(searchBean.getTallyDate());
        String accType = dataMap.get("ACCTYPE");
        if(StringUtils.isBlank(accType)){
            accType = dataMap.get("acctype");
        }
        container.setAccTypeCodes(InterestHelper.getAccTypes(accType, AccTypeAllFilterType.BATCH));
        if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
            container.setAccInCenter(true);//利息收入\利息支出账号是否挂中心
        } else {
            container.setAccInCenter(false);
        }
        container.setMoreVoucherDtl(isMoreVoucherDtl);//计息凭证类型是否多条分录
        IInterestProvider interestProvider = InterestHelper.getInterestProvider(accType);
        AccInterestInfo accInterestInfo = new AccInterestInfo();
        container.setRushMoney(fcInterestDao.getAccRushInterest(searchBean.getNetId(), accType,
                dataMap.get("curId"), searchBean.getBlnInstDate(), dataMap.get("blnctracc"), opType));
        accInterestInfo.setContainer(container);
        accInterestInfo.setFcPredrawDtls(fcInterestDao.queryWaitRushPredrawDtls(searchBean.getNetId(), accType, dataMap.get("curId"),
                searchBean.getBlnInstDate(), dataMap.get("blnctracc"), opType));
        AccTypeKeyBean accTypeKeyBean = new AccTypeKeyBean(searchBean.getNetId(), dataMap.get("curId"), accType);
        accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
        accInterestInfo.setNeedSave(false);//不需要保存凭证信息
        interestProvider.rushPredrawn(accInterestInfo);
        List<FcFinacialAccbookVO> accbook = accInterestInfo.getRushAccbook();
        BigDecimal accbookTot = BigDecimal.ZERO;
        BigDecimal accbookCreditTot = BigDecimal.ZERO;
        if (CollectionUtil.isEmpty(accbook)){
            result.put("voucherInfo", accbook);
            result.put("interestTot", pageHead.size());//设置总页数
            result.put("pageHead", pageHead);
            return result;
        }
        for (FcFinacialAccbookVO fcFinacialAccbook : accbook) {
            accbookTot = BigDecimal.ZERO;
            accbookCreditTot = BigDecimal.ZERO;
            if(fcFinacialAccbook.getAffixAmount() == null){
                fcFinacialAccbook.setAffixAmount(0);
            }
            fcFinacialAccbook.setTallyDate(searchBean.getTallyDate());
            fcFinacialAccbook.setInterestStartDate(searchBean.getTallyDate());
            List<FcFinacialAccbookDtlVO> accbookDtls = fcFinacialAccbook.getFcAccbookDtls();
            for (FcFinacialAccbookDtlVO dtl : accbookDtls) {
                accbookCreditTot = accbookCreditTot.add(dtl.getCreditMoney());
                accbookTot = accbookTot.add(dtl.getDebitMoney());
            }
            fcFinacialAccbook.setCreditSum(accbookCreditTot);
            fcFinacialAccbook.setDebitSum(accbookTot);
        }
        result.put("voucherInfo", accbook);
        result.put("interestTot", pageHead.size());//设置总页数
        result.put("pageHead", pageHead);
        return result;
    }
    /**
     * <b>功能介绍:根据网点获取日结日期</b><br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Date getDayOverDate(String netId) {
        return iFcDayCloseService.getDayCloseFinalDate(netId);
    }
    /**
     * <br>
     * <b>功能：</b> 计息凭证类型(多借一贷/一借多贷,一借一贷)是否多条分录 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/11/23 0023 <br>
     */
    public boolean isMoreVoucherDtl() {
        String paramVal = configService.getConfig("ist", "moreVoucherDtl").getValue();
        return "1".equals(paramVal) ? true : false;
    }
    /**
     * <b>功能介绍:</b>计息/提交<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public InterestResult interestaccrualByBatch(String netCode, String accTypeCode, Date tallyDate, Date endInstDate,
                                                 ICurrentUser userBean, OperateType opType, boolean IsHoldOn,
                                                 boolean poolSign, String account, String generateMode, BigDecimal tellerMoney) {
        //产生标志，默认是批量计息
        if (EmptyUtils.isEmpty(generateMode)) {
            generateMode = InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        //1、删除此次会话中暂存的预提明细和利息明细.
        fcInterestDao.deletePredraw(netCode, null, InterestConstant.INTEREST_STATUE_TEMP, account, generateMode);
        fcInterestDao.deleteInterest(netCode, null, InterestConstant.INTEREST_STATUE_TEMP, account, generateMode);
        doDeleteByBatch(netCode, accTypeCode, userBean.getId(), account, generateMode);
        // 2、初始化计息容器
        InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
        interestInfoContainer.setGenerateMode(generateMode);
        interestInfoContainer.setAccount(account);
        interestInfoContainer.setNetCode(netCode);
        interestInfoContainer.setTallyDate(tallyDate);
        interestInfoContainer.setEndInstDate(endInstDate);
        interestInfoContainer.setUserBean(new ShiroUser(userBean.getId(), userBean.getLoginName(), userBean.getUsername()));
        interestInfoContainer.setSessionId(userBean.getId());
        interestInfoContainer.setOpType(opType);
        interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes(accTypeCode, AccTypeAllFilterType.BATCH));
        interestInfoContainer.setSeasonEnd(InterestHelper.isSeasonEnd(endInstDate)); //是否季末
        interestInfoContainer.setLastWorkDay(InterestHelper.isLastDayOfMonth(endInstDate)); //是否月末最后一个工作日
        ShiroUser user = new ShiroUser();
        user.setId(userBean.getId());
        user.setUsername(userBean.getUsername());
        interestInfoContainer.setUserBean(user);
        // 读取系统参数设置
        if (poolSign) {
            interestInfoContainer.setAccInCenter(true);
        } else {
            if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
                interestInfoContainer.setAccInCenter(true);//按结算中心计息
            } else {
                interestInfoContainer.setAccInCenter(false);//按单位或银行计息
            }
        }
        interestInfoContainer.setMoreVoucherDtl(isMoreVoucherDtl());//分录条数
        interestInfoContainer.setSysInstDay(InterestHelper.isSysInstDay(endInstDate, getSysBatchInstDay())); //是否计息日
        interestInfoContainer.setSysPredrawRushWay(getSysPredrawRushWay()); //预提冲预提方式
        interestInfoContainer.setSysBatchInterestWay(getSysBatchInstWay()); //批量结息方式
        interestInfoContainer.setSysFixInterestWay(getSysFixInstWay()); //定期分段计息方式
        interestInfoContainer.setSysRateType(new BigDecimal(getDivisorBySysRateType())); //系统利率类型
        interestInfoContainer.setFixedActrueDays(fixedActrueDays());//定期月份计算日利率天数
        interestInfoContainer.setPoolSign(poolSign);
        interestInfoContainer.setTellerMoney(tellerMoney);
        if (InterestConstant.GENERATEMODE_INTEREST_PART.equals(generateMode)) {
            interestInfoContainer.setNotifyDraw(true);//需要预约取款
        }
        // 3、计息  且里面逻辑有消息发送
        InterestResult interestResult = new BaseInterestProcessor(interestInfoContainer, iFcAccService).interestaccrual(userBean);
        // 4、当提交时更新此次会话中结息账户的积数计息标志和计息时间
        if (opType == OperateType.SUBMIT) {
            if (null != interestInfoContainer.getInterestTime()) {
                Map<String, Object> result = validateInterestVoucher(netCode, accTypeCode, "validList", userBean, poolSign, account,generateMode);
                String msgCode = (String) result.get("msgCode");
                if ("-1".equals(msgCode) || "-2".equals(msgCode) || "-3".equals(msgCode)) {
                    if ("-1".equals(msgCode)) {
                        if( accTypeCode.indexOf(InnerAccountConstant.FCACC_LOANBYMANDATE) != -1){
                            boolean isAccInCenter = true;//XXX 利息收入\利息支出账号是否挂中心参数默认为网点
                            List<Object[]> lists = this.fcInterestDao.queryVoucherHeadList0(netCode, "validList", userBean.getId(), isAccInCenter, false);
                            if (lists.isEmpty()) {
                                logger.debug("本计息周期内没有可以计息的数据，不能计息!");
                                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);
                            }
                        }else{
                            logger.debug("本计息周期内没有可以计息的数据，不能计息!");
                            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);
                        }

                    } else {//不满足记账凭证条件
                        logger.debug("不满足记账凭证条件");
                        throw new ParameterException(InterestMessageEnum.FIN_INTEREST_VOUCHERNOTPASS);
                    }
                }
                fcInterestDao.updateAccuForBatch(userBean.getId(), endInstDate,
                        interestInfoContainer.getInterestTime(), InterestConstant.INTEREST_SIGN_YES, netCode);
                fcInterestDao.updateAccuForBatch(userBean.getId(), endInstDate,
                        interestInfoContainer.getInterestTime(), InterestConstant.INTEREST_SIGN_NO, netCode);
                //不足款清单保存
                List<FcLessPayBill> lessPayBills = new ArrayList<FcLessPayBill>();
                for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos()
                        .entrySet()) {
                    if (entry.getKey().getaccType().equals(InnerAccountConstant.FCACC_LOAN)) {
                        lessPayBills.addAll(entry.getValue().getLessPayBills());
                    }
                }
                if (lessPayBills.size() > 0) {
                    List<String> accounts = lessPayBills.stream().map(FcLessPayBill::getAccount).collect(Collectors.toList());
                    List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
                    Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
                    lessPayBills.forEach( a -> {
                        FcAccDTO fcAccDTO = map.get(a.getAccount());
                        a.setAccName(fcAccDTO.getAccName());
                        a.setAccForDeduct(fcAccDTO.getAccForDeduct());
                    });
                    fcInterestDao.addBatchByHql(lessPayBills);
                }
                if (isBatchInstAutoCheck() && InterestConstant.GENERATEMODE_INTEREST_BATCH.equals(generateMode)) {
                    processInterestPass(interestInfoContainer, accTypeCode);
                }
            } else {
                logger.debug("本计息周期内没有可以计息的数据，不能计息!");
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);//不满足记账凭证条件
            }
            //过滤委托贷款不够扣息的利息明细
            validataInterestByLoanbymandate(interestInfoContainer, IsHoldOn);
        }
        return interestResult;
    }
    /**
     * <b>功能介绍：</b>过滤委托贷款不够扣息的利息明细<br>
     * <b>日期：</b>2017年12月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param container
     * @param IsHoldOn  是否继续计息
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public void validataInterestByLoanbymandate(InterestInfoContainer container, boolean IsHoldOn) {
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : container.getAccTypeInterestInfos()
                .entrySet()) {
            if (entry.getKey().getaccType().equals(InnerAccountConstant.FCACC_LOANBYMANDATE)) {//开户种类为委托贷款
                List<FcInterestDtlTemp> dtls = entry.getValue().getInterestDtls();
                List<Object[]> head = vourchHeadSortUniq(dtls, entry.getKey().getaccType());
                List<String> notInterestDtlId = new ArrayList<String>();
                String notAccs = "";
                Map<String, BigDecimal> accDebitMoney = new HashMap<String, BigDecimal>();
                Map<String, BigDecimal> notInterestDtls = new HashMap<String, BigDecimal>();
                for (Object[] head_ : head) {
                    //根据暂存利息明细构建凭证
                    Map<String, Object> result = createInterestVoucher(container.getNetCode(), container.getUserBean(), head_, entry.getValue().getInterestDtls(), false, false);
                    List<FcFinacialAccbookVO> accbookVOs = ((List<FcFinacialAccbookVO>) result.get("voucherInfo"));
                    //凭证按利息从小到大排序，从金额较小开始提交
                    Collections.sort(accbookVOs, new Comparator<FcFinacialAccbookVO>() {
                        @Override
                        public int compare(FcFinacialAccbookVO o1, FcFinacialAccbookVO o2) {
                            return o1.getDebitSum().compareTo(o2.getDebitSum());
                        }
                    });
                    for (FcFinacialAccbookVO accbookVO : accbookVOs) {
                        //借方明细
                        FcFinacialAccbookDtlVO debitDtl = accbookVO.getFcAccbookDtls().get(0);
                        BigDecimal checkbalnce = fcOnlineLedgerService.getByBlnctrAcc(debitDtl.getAccount()).getCheckBalance();
                        if (EmptyUtils.isEmpty(accDebitMoney.get(debitDtl.getAccount()))) {//借方账号本次没有被使用
                            if (debitDtl.getDebitMoney().compareTo(checkbalnce) > 0) {//利息大于账号可用余额
                                for (FcInterestDtlTemp dtltemp : accbookVO.getInterestDtls()) {
                                    notInterestDtlId.add(dtltemp.getId());
                                    //分段利息计算合计
                                    BigDecimal otherInterest = notInterestDtls.get(dtltemp.getAccount() + "_" + debitDtl.getAccount()) == null ? new BigDecimal(0) : notInterestDtls.get(dtltemp.getAccount() + "_" + debitDtl.getAccount());
                                    //要过滤的利息明细
                                    notInterestDtls.put(dtltemp.getAccount() + "_" + debitDtl.getAccount(), dtltemp.getInterest().add(otherInterest));
                                }
                            }
                        } else {
                            if (debitDtl.getDebitMoney().compareTo(accDebitMoney.get(debitDtl.getAccount())) > 0) {
                                for (FcInterestDtlTemp dtltemp : accbookVO.getInterestDtls()) {
                                    notInterestDtlId.add(dtltemp.getId());
                                    BigDecimal otherInterest = notInterestDtls.get(dtltemp.getAccount() + "_" + debitDtl.getAccount()) == null ? new BigDecimal(0) : notInterestDtls.get(dtltemp.getAccount() + "_" + debitDtl.getAccount());
                                    notInterestDtls.put(dtltemp.getAccount() + "_" + debitDtl.getAccount(), dtltemp.getInterest().add(otherInterest));
                                }
                            }
                        }
                        accDebitMoney.put(debitDtl.getAccount(), (fcOnlineLedgerService.getByBlnctrAcc(debitDtl.getAccount()).getCheckBalance()).subtract(debitDtl.getDebitMoney()));
                    }
                }
                if (notInterestDtlId.size() > 0) {
                    if (IsHoldOn) {
                        fcInterestDao.updateBalanceLackAccu(notInterestDtlId, container.getEndInstDate(),/* container.getUserBean().getId()*/"");
                        fcInterestDao.filterBalanceLack(notInterestDtlId, /* container.getUserBean().getId()*/"");
                    } else {
                        for (Entry<String, BigDecimal> notInterestDtl : notInterestDtls.entrySet()) {
                            //计息账号：9001011125000000 扣息账号：9001012120000002 利息：31.11  余额：20.00；
                            notAccs += multiLangServiceI.getLang("finacial.interest.Loanbymandate.notBalance.msg",
                                    notInterestDtl.getKey().split("_")[0], notInterestDtl.getKey().split("_")[1],
                                    NumberUtil.formatNumeric(notInterestDtl.getValue()))
                                    + " <span style='color:red;'>"
                                    + NumberUtil.formatNumeric(fcOnlineLedgerService
                                    .getByBlnctrAcc(notInterestDtl.getKey().split("_")[1]).getCheckBalance())
                                    + "</span>；<br>";
                        }
                        throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOTENOUGH_BALANCE, "<br>" + notAccs);
                    }
                }
            }
        }
    }
    /**
     * <b>功能介绍：</b>排序去重<br>
     * <b>日期：</b>2017年12月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param dtls
     * @param accType
     * @return
     * @version 1.0
     */
    public List<Object[]> vourchHeadSortUniq(List<FcInterestDtlTemp> dtls, String accType) {
        Map<String, Object[]> vourchs = new HashMap<String, Object[]>();
        for (FcInterestDtlTemp dtl : dtls) {//去重
            if (dtl.getInterest().doubleValue() > 0) {
                Object[] vourchHead = new Object[4];
                vourchHead[0] = dtl.getCurId();
                vourchHead[1] = accType;
                vourchHead[2] = dtl.getInterestSign();
                vourchHead[3] = dtl.getAccount();
                vourchs.put(dtl.getCurId(), vourchHead);
            }
        }
        List<Object[]> list = new ArrayList<Object[]>();
        for (Entry<String, Object[]> vourch : vourchs.entrySet()) {
            list.add(vourch.getValue());
        }
        Collections.sort(list, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                if (o1[0].toString().compareTo(o2[0].toString()) != 0) {//按币别排序
                    return o1[0].toString().compareTo(o2[0].toString());
                } else {
                    return o1[2].toString().compareTo(o2[2].toString());//按利息类型排序
                }
            }
        });
        return list;
    }
    /**
     * <b>功能介绍：</b>批量计息复核<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     * @param accBean
     * @version 1.0
     */
    public void processInterestCheck(AccInterestBean accBean) {
        String netCode = accBean.getNetCode();
        String accTypeCode = accBean.getAccTypeCode();
        Date tallyDate = accBean.getTallyDate();
        Date endInstDate = accBean.getEndInstDate();
        ICurrentUser user = accBean.getUser();
        OperateType opType = accBean.getOpType();
        String account = accBean.getAccount();
        boolean poolSign = accBean.isPoolSign();
        String generateMode = accBean.getGenerateMode();
        String userId = user.getId();
        if (EmptyUtils.isEmpty(generateMode)) {
            generateMode=InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        List<FcInterestDtlTemp> interestDtlTemps = fcInterestDao.queryFcInterestDtlTemps(netCode, accTypeCode, endInstDate, userId, account,generateMode);
        List<FcPredrawDtlTemp> predrawDtlTemps = fcInterestDao.queryFcPredrawDtlTemps(netCode, accTypeCode, endInstDate, userId, account);
        for (FcPredrawDtlTemp preDtl : predrawDtlTemps) {//设置预提对应的利息明细
            preDtl.setInterestDtls(fcInterestDao.getFcInterestDtlTempByPreId(preDtl.getId()));
            preDtl.setInterestTime(fcInterestDao.getFcPredrawDtlTempById(preDtl.getId()).getInterestTime());
        }
        if ((interestDtlTemps == null || interestDtlTemps.size() == 0)
                && (predrawDtlTemps == null || predrawDtlTemps.size() == 0)) {
            String check = multiLangServiceI.getLang("common.button.check");//复核
            String back = multiLangServiceI.getLang("common.button.back");//打回

            Map<String, String> queryFaAccParams = Maps.newHashMap();
            queryFaAccParams.put("netId", netCode);
            queryFaAccParams.put("accTypeCode", accTypeCode);
            List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccsAndAcctype(new ArrayList<>(), queryFaAccParams);
            List<String> accounts = Lists.newArrayList();
            accounts.add(account);
            if (ListUtils.isNotNullOrEmpty(fcAccDTOS)){
                fcAccDTOS.stream().forEach(a ->{
                    accounts.add(a.getBlnctrAcc());
                });
            }
            long allNo = fcInterestDao.queryCountFcInterestDtlTempsAll(netCode, accTypeCode, userId, accounts, generateMode);

            if (allNo > 0 || fcInterestDao.queryCountFcPredrawDtlTempsAll(netCode, accTypeCode, userId, account) > 0) {
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOTCHECKORBACKDATA_INTERESTDATE, opType == OperateType.CHECK ? check : back);
            } else {
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOTCHECKORBACKDATA, opType == OperateType.CHECK ? check : back);
            }
        }
        if (opType == OperateType.CHECK) {//复核通过
            InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
            interestInfoContainer.setNetCode(netCode);
            interestInfoContainer.setAccount(account);
            interestInfoContainer.setGenerateMode(generateMode);
            interestInfoContainer.setTallyDate(tallyDate);
            interestInfoContainer.setEndInstDate(endInstDate);
            interestInfoContainer.setUserBean(new ShiroUser(userId, user.getLoginName(), user.getUsername()));
            interestInfoContainer.setOpType(opType);
            interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes(accTypeCode, AccTypeAllFilterType.BATCH));
            interestInfoContainer.setSeasonEnd(InterestHelper.isSeasonEnd(endInstDate)); //是否季末
            interestInfoContainer.setSysInstDay(InterestHelper.isSysInstDay(endInstDate, getSysBatchInstDay())); //是否计息日
            interestInfoContainer.setFixedActrueDays(fixedActrueDays());//定期月份计算日利率天数
            interestInfoContainer.setPoolSign(poolSign);
            interestInfoContainer.setAccBean(accBean);//账户计息Bean对象
            groupInterestByInterestType(interestInfoContainer, interestDtlTemps, predrawDtlTemps);
            processInterestPass(interestInfoContainer, accTypeCode);
        } else if (opType == OperateType.BACK) {//复核打回
            if (EmptyUtils.isEmpty(accTypeCode) || accTypeCode.indexOf(InnerAccountConstant.FCACC_LOAN) != -1) {
                                        fcInterestDao.checkLessPayBill(netCode, accTypeCode, endInstDate, InterestConstant.INTEREST_BACK);
            }
            fcInterestDao.backInterestDtlTemp(netCode, accTypeCode, endInstDate, userId, account,generateMode);
        }
        //完成消息
        for(FcInterestDtlTemp fcInterestDtlTemp : interestDtlTemps){
            notificationService.complete(NotificationConstant.FINACIAL.INTEREST_CHECK,fcInterestDtlTemp.getId(),userId);
        }
    }
    /**
     * <b>功能介绍：</b>按照计息类型分组利息<br>
     * <b>日期：</b>2017年10月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param interestInfoContainer
     * @param interestDtlTemps
     * @param predrawDtlTemps
     * @version 1.0
     */
    private void groupInterestByInterestType(InterestInfoContainer interestInfoContainer,
                                             List<FcInterestDtlTemp> interestDtlTemps, List<FcPredrawDtlTemp> predrawDtlTemps) {
        FcAccDTO fcAcc = null;
        AccTypeKeyBean accTypeKeyBean = null;
        AccInterestInfo accInterestInfo = null;
        InterestProcessType interestProcessType = null;
        Map<String, List<FcAccuBalancef>> accuBalanceMap = null;
        Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalanceMap = null;
        Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestMap = new HashMap<AccTypeKeyBean, AccInterestInfo>();
        for (FcInterestDtlTemp interestDtlTemp : interestDtlTemps) {
            fcAcc = iFcAccService.getAccDTOByAccount(interestDtlTemp.getAccount());
            interestProcessType = InterestProcessType.INTEREST;
            accTypeKeyBean = new AccTypeKeyBean(fcAcc.getNetId(), fcAcc.getCurId(), fcAcc.getAccType());
            accInterestInfo = accTypeInterestMap.get(accTypeKeyBean);
            if (accInterestInfo == null) {
                accInterestInfo = new AccInterestInfo();
                if (interestInfoContainer.getInterestTime() == null) {
                    if (accInterestInfo.getInterestTime() == null) {
                        accInterestInfo.setInterestTime(interestDtlTemp.getInterestTime());//设置为计息的时间
                    }
                    interestInfoContainer.setInterestTime(accInterestInfo.getInterestTime());
                }
                interestInfoContainer.setInterestDateTime(new java.sql.Timestamp(interestDtlTemp.getInterestTime().getTime()));
                accInterestInfo.setInterestDtls(new ArrayList<FcInterestDtlTemp>());
                accInterestInfo.setPredrawDtls(new ArrayList<FcPredrawDtlTempVO>());
                accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
                accInterestInfo.setContainer(interestInfoContainer);
                interestAccuBalanceMap = new HashMap<InterestProcessType, Map<String, List<FcAccuBalancef>>>();
                accInterestInfo.setInterestAccuBalances(interestAccuBalanceMap);
                accTypeInterestMap.put(accTypeKeyBean, accInterestInfo);
            }
            accInterestInfo.getInterestDtls().add(interestDtlTemp);
            interestAccuBalanceMap = accInterestInfo.getInterestAccuBalances();
            accuBalanceMap = interestAccuBalanceMap.get(interestProcessType);
            if (accuBalanceMap == null) {
                accuBalanceMap = new HashMap<String, List<FcAccuBalancef>>();
                interestAccuBalanceMap.put(interestProcessType, accuBalanceMap);
            }
        }
        List<String> preIds = new ArrayList<String>();
        for (FcPredrawDtlTemp predrawDtlTemp : predrawDtlTemps) {
            fcAcc = iFcAccService.getAccDTOByAccount(predrawDtlTemp.getAccount());
            interestProcessType = InterestProcessType.PREDRAW;
            accTypeKeyBean = new AccTypeKeyBean(fcAcc.getNetId(), fcAcc.getCurId(), fcAcc.getAccType());
            accInterestInfo = accTypeInterestMap.get(accTypeKeyBean);
            if (accInterestInfo == null) {
                accInterestInfo = new AccInterestInfo();
                if (interestInfoContainer.getInterestTime() == null) {
                    if (accInterestInfo.getInterestTime() == null) {
                        accInterestInfo.setInterestTime(predrawDtlTemp.getInterestTime());//设置为预提的时间
                    }
                    interestInfoContainer.setInterestTime(accInterestInfo.getInterestTime());
                }
                interestInfoContainer.setInterestDateTime(new java.sql.Timestamp(predrawDtlTemp.getInterestTime().getTime()));
                accInterestInfo.setInterestDtls(new ArrayList<FcInterestDtlTemp>());
                accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
                accInterestInfo.setContainer(interestInfoContainer);
                interestAccuBalanceMap = new HashMap<InterestProcessType, Map<String, List<FcAccuBalancef>>>();
                accInterestInfo.setInterestAccuBalances(interestAccuBalanceMap);
                accTypeInterestMap.put(accTypeKeyBean, accInterestInfo);
            }
            if (!preIds.contains(predrawDtlTemp.getId())) {
                FcPredrawDtlTempVO preDtlVO = new FcPredrawDtlTempVO();
                BeanUtils.copyProperties(predrawDtlTemp, preDtlVO);
                accInterestInfo.getPredrawDtls().add(preDtlVO);
                preIds.add(preDtlVO.getId());
            }
            interestAccuBalanceMap = accInterestInfo.getInterestAccuBalances();
            accuBalanceMap = interestAccuBalanceMap.get(interestProcessType);
            if (accuBalanceMap == null) {
                accuBalanceMap = new HashMap<String, List<FcAccuBalancef>>();
                interestAccuBalanceMap.put(interestProcessType, accuBalanceMap);
            }
        }
        interestInfoContainer.setAccTypeInterestInfos(accTypeInterestMap);
    }
    /**
     * <b>功能介绍：</b>提交/暂存冲预提<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void submitRushPredraw(InterestCommonBean bean, BigDecimal rushMoney, String strOpType) throws Exception {
        InterestSearchBean searchBean = new InterestSearchBean();
        searchBean.setAccType(bean.getAccTypeCode());
        searchBean.setBlnInstDate(bean.getEndInstDate());
        searchBean.setNetId(bean.getNetCode());
        //deletePredrawRushDtls(searchBean, null);//删除暂存和打回的冲预提明细
        if (!"submit".equals(strOpType)) {
            deletePredrawRushDtls(searchBean, null);//删除暂存和打回的冲预提明细
        }
        OperateType opType = null;
        if ("temp".equals(strOpType)) {
            opType = OperateType.ADD;
        } else if ("submit".equals(strOpType)) {
            opType = OperateType.SUBMIT;
        } else if ("check".equals(strOpType)) {
            opType = OperateType.CHECK;
        } else if ("back".equals(strOpType)) {
            opType = OperateType.BACK;
        }
        InterestInfoContainer container = new InterestInfoContainer();
        container.setInterestTime(DateUtils.getDate());
        container.setRushMoney(rushMoney);
        container.setTallyDate(bean.getTallyDate());
        container.setOpType(opType);
        ShiroUser shiroUser = new ShiroUser();
        shiroUser.setId(bean.getUserId());
        container.setUserBean(shiroUser);
        container.setEndInstDate(bean.getEndInstDate());
        container.setNetCode(bean.getNetCode());
        container.setAccount(bean.getAccount());
        container.setAccTypeCodes(InterestHelper.getAccTypes(bean.getAccTypeCode(), AccTypeAllFilterType.BATCH));
        if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
            container.setAccInCenter(true);
        } else {
            container.setAccInCenter(false);
        }
        container.setMoreVoucherDtl(isMoreVoucherDtl());//计息凭证类型是否多条分录
        List<FcPredrawDtl> predrawDtls = fcInterestDao.queryUnRushPredrawDtls(bean, "", strOpType);
        if (predrawDtls == null || predrawDtls.size() == 0) {
            return;
        }
        setInteretPreAcc(predrawDtls, container);
        groupPreDrawByInterestType(container, predrawDtls);
        new BaseRushPredrawProccessor(container).rushPredraw();
    }
    /**
     * <b>功能介绍：</b>冲账户预提(只冲销预提，不产生凭证)<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param user
     * @param depositManagerId
     * @param tellerMoney
     * @param interestTime
     * @version 1.0
     */
    public void interestRushPredraw(String account, ICurrentUser user, String depositManagerId, BigDecimal tellerMoney, Date interestTime) {
        FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(account);
        String netCode = fcAcc.getNetId();
        List<FcPredrawDtlVo> predrawDtls = fcInterestDao.queryAccFcPredrawDtlVo(account);
        if (null == tellerMoney) {
            tellerMoney = BigDecimal.ZERO;
        }
        List<FcPredrawRushDtl> predrawRushDtls = new ArrayList<FcPredrawRushDtl>();
        for (FcPredrawDtlVo istFcPredrawDtl : predrawDtls) {
            BigDecimal interest = istFcPredrawDtl.getInterest();//预提金额
            BigDecimal strikeInterest = istFcPredrawDtl.getStrikeInterest();//已冲金额
            if (null == strikeInterest) {
                strikeInterest = BigDecimal.ZERO;
            }
            if (interest.compareTo(strikeInterest) <= 0) {//过滤已全部冲的预提
                continue;
            }
            BigDecimal leftInterest = interest.subtract(strikeInterest);//剩余未冲=预提减去已冲
            FcPredrawRushDtl predrawRushDtl = new FcPredrawRushDtl();
            predrawRushDtl.setPredrawDtlId(istFcPredrawDtl.getId());
            predrawRushDtl.setOperatorCode(user.getId());
            predrawRushDtl.setStrikeDate(interestTime);
            String orderNo = DateUtils.formatDate(istFcPredrawDtl.getInterestTime(), "yyyyMMddHHmmss");//设置冲预提orderNo
            predrawRushDtl.setOrderNo(orderNo + corpService.get(netCode).getCode());
            if (StringUtils.isNotEmpty(depositManagerId)) {
                predrawRushDtl.setDepositManagerId(depositManagerId);
            }
            predrawRushDtl.setStatus(InterestConstant.INTEREST_STATUE_CHECK);
            BigDecimal rushMoney = leftInterest;//全部冲
            //按比例冲预提
            if (tellerMoney.compareTo(BigDecimal.ZERO) > 0) {
                //获取冲销比例
                FcAccBalanceDTO accBalanceDTO = accBalanceService.getBalanceByAccountAndDate(account, istFcPredrawDtl.getBlnInstDate());
                if(!EmptyUtils.isEmpty(accBalanceDTO)) {
                    BigDecimal balance = accBalanceDTO.getBalance();
                    if (null != balance && balance.compareTo(BigDecimal.ZERO) != 0) {
                        rushMoney = interest.multiply(tellerMoney).divide(balance, 2, BigDecimal.ROUND_HALF_UP);
                    }
                }
            }
            predrawRushDtl.setStrikeInterest(rushMoney);//本次冲销金额
            predrawRushDtls.add(predrawRushDtl);

        }
        fcInterestDao.addBatchByHql(predrawRushDtls);
    }
    /**
     * <b>功能介绍：</b>设置冲预提明细账号集<br>
     * <b>日期：</b>2017年7月20日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void setInteretPreAcc(List<FcPredrawDtl> predrawDtls, InterestInfoContainer interestInfoContainer) {
        Map<String, FcAccDTO> fcAccList = new HashMap<String, FcAccDTO>();
        Set<String> accs = new HashSet<String>();
        for (FcPredrawDtl fcPredrawDtl : predrawDtls) {
            accs.add(fcPredrawDtl.getAccount());
        }
        for (String acc : accs) {
            fcAccList.put(acc, iFcAccService.getAccDTOByAccount(acc));
        }
        interestInfoContainer.setInterestAcc(fcAccList);
    }
    /**
     * <b>功能介绍：</b>按账户开户种类分组预提信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void groupPreDrawByInterestType(InterestInfoContainer container, List<FcPredrawDtl> predrawDtls) {
        FcAccDTO fcAcc = null;
        AccTypeKeyBean accTypeKeyBean = null;
        AccInterestInfo accInterestInfo = null;
        InterestProcessType interestProcessType = null;
        Map<String, List<FcAccuBalancef>> accuBalanceMap = null;
        Map<InterestProcessType, Map<String, List<FcAccuBalancef>>> interestAccuBalanceMap = null;
        Map<AccTypeKeyBean, AccInterestInfo> accTypeInterestMap = new HashMap<AccTypeKeyBean, AccInterestInfo>();
        for (FcPredrawDtl predrawDtl : predrawDtls) {
            fcAcc = container.getInterestAcc().get(predrawDtl.getAccount());
            interestProcessType = InterestProcessType.INTEREST;
            accTypeKeyBean = new AccTypeKeyBean(fcAcc.getNetId(), fcAcc.getCurId(), fcAcc.getAccType());
            accInterestInfo = accTypeInterestMap.get(accTypeKeyBean);
            if (accInterestInfo == null) {
                accInterestInfo = new AccInterestInfo();
                accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
                accInterestInfo.setContainer(container);
                if (OperateType.CHECK == container.getOpType()) {
                    accInterestInfo.setNeedSave(true);
                } else {
                    accInterestInfo.setNeedSave(false);
                }
                interestAccuBalanceMap = new HashMap<InterestProcessType, Map<String, List<FcAccuBalancef>>>();
                accInterestInfo.setInterestAccuBalances(interestAccuBalanceMap);
                accTypeInterestMap.put(accTypeKeyBean, accInterestInfo);
            }
            if (!accInterestInfo.getFcPredrawDtls().contains(predrawDtl)) {
                accInterestInfo.getFcPredrawDtls().add(predrawDtl);
            }
            interestAccuBalanceMap = accInterestInfo.getInterestAccuBalances();
            accuBalanceMap = interestAccuBalanceMap.get(interestProcessType);
            if (accuBalanceMap == null) {
                accuBalanceMap = new HashMap<String, List<FcAccuBalancef>>();
                interestAccuBalanceMap.put(interestProcessType, accuBalanceMap);
            }
        }
        container.setAccTypeInterestInfos(accTypeInterestMap);
    }
    /**
     * <b>功能介绍：</b>查询未冲销的预提信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcPredrawDtl> queryUnRushPredrawDtls(InterestCommonBean bean, String curCode, String opType) {
        return fcInterestDao.queryUnRushPredrawDtls(bean, curCode, opType);
    }
    /**
     * <b>功能介绍：</b>获取账户冲预提金额<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public BigDecimal getRushPreDrawMoney(String account, BigDecimal tellerMoney, String depositManagerId) {
        BigDecimal rushMoney = fcInterestDao.getRushPreDrawMoney(account);
        BigDecimal unCheckedMoney = BigDecimal.ZERO;
        if (EmptyUtils.isNotEmpty(depositManagerId)) {
            unCheckedMoney = fcInterestDao.getRushUnCheckDrawMoney(depositManagerId);
        }
        rushMoney = rushMoney.add(unCheckedMoney);
        if (tellerMoney != null && tellerMoney.compareTo(BigDecimal.ZERO) > 0) {
            //冲部分预提
            FcOnlineLedgerDTO ledger = fcOnlineLedgerService.getByBlnctrAcc(account);
            if (null != ledger) {
                if (ledger.getCheckBalance().doubleValue() != 0) {
                    rushMoney = rushMoney.multiply(tellerMoney).divide(ledger.getCheckBalance(), 2, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        return rushMoney;
    }
    /**
     * <br>
     * <b>功能：</b>处理批量计息复核通过<br>
     * <b>作者：</b>李又虎<br>
     * <b>日期：</b> 2012-12-5 <br>
     *
     * @param interestInfoContainer
     * @param accTypeCode
     */
    private void processInterestPass(InterestInfoContainer interestInfoContainer, String accTypeCode) {
        interestInfoContainer.setSysPredrawRushWay(getSysPredrawRushWay()); //预提冲预提方式
        if (interestInfoContainer.isPoolSign()) {
            interestInfoContainer.setAccInCenter(true);//按结算中心计息
        } else {
            if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
                interestInfoContainer.setAccInCenter(true);//按结算中心计息
            } else {
                interestInfoContainer.setAccInCenter(false);//按成员单位
            }
        }
        interestInfoContainer.setMoreVoucherDtl(isMoreVoucherDtl());//计息凭证类型是否多条分录
        interestInfoContainer.setInstBisExc(false);//是否发送指令
        interestInfoContainer.setOpType(OperateType.CHECK);
        interestInfoContainer.setBatchRushPre(false);//不默认冲预提，根据账户去判断
        new BaseInterestProcessor(interestInfoContainer, iFcAccService).interest();
        //2、不足款清单复核通过
        if (StringUtils.isBlank(accTypeCode) || accTypeCode.indexOf(InnerAccountConstant.FCACC_LOAN) != -1) {
            Date endInstDate = interestInfoContainer.getEndInstDate();
            String netCode = interestInfoContainer.getNetCode();
            fcInterestDao.checkLessPayBill(netCode, InnerAccountConstant.FCACC_LOAN, endInstDate, InterestConstant.INTEREST_STATUE_CHECK);
        }
    }
    /**
     * <b>功能介绍:</b>删除利息<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void doDeleteByBatch(String netCode, String accType, String sessionId, String account, String generateMode) {
        fcInterestDao.updateAccuBalancesInterestSign(netCode, accType, sessionId, account, generateMode);
        List<String> accounts;
        if (StringUtils.isBlank(accType) || accType.contains(InnerAccountConstant.FCACC_LOAN)) {
            accounts = iFcAccService.getBlnctrAccByNetCodeAndAccType(netCode, InnerAccountConstant.FCACC_LOAN);
            if (ListUtils.isNotNullOrEmpty(accounts) ){
                fcInterestDao.deleteLessPay(accounts, sessionId);//删除不足款清单
            }
        }
        fcInterestDao.deleteInterestDtlTemp(netCode, sessionId, account, generateMode);//删除利息
    }
    /**
     * <b>功能介绍：</b>删除冲预提信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void deletePredrawRushDtls(InterestSearchBean searchBean, String account) {
        fcInterestDao.deletePredrawRushDtls(searchBean, account);
    }
    /**
     * <b>功能介绍：</b>校验凭证要素是否符合<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, Object> validateInterestVoucher(String netCode, String accType,
                                                       String opType, ICurrentUser user, boolean poolSign, String accountCode,String generateMode) {
        fcInterestDao.flushSession();
        Map<String, Object> result = new HashMap<String, Object>();
        List<Object[]> headlist = new ArrayList<Object[]>();
        boolean isMoreVoucherDtl = false;//XXX  计息凭证类型默认一借一贷
        boolean isAccInCenter = true;//XXX 利息收入\利息支出账号是否挂中心默认为是
        if (isMoreVoucherDtl) {//不是一借一贷，按照开户种类、币别、结息类型+委贷账户分页
            headlist = fcInterestDao.queryVoucherHeadList(netCode, accType, opType, user.getId(), isAccInCenter, false, accountCode,generateMode);
        } else {
            headlist = fcInterestDao.queryVoucherHeadList(netCode, accType, opType, user.getId(), false, true, accountCode,generateMode);
        }
        String msgCode = "1";
        String msg = multiLangServiceI.getLang("fin.interest.interestnodata");//本计息周期内没有可以计息的数据，不能计息!
        result.put("msgCode", msgCode);
        result.put("msg", msg);
        if (headlist.isEmpty()) {
            msgCode = "-1";
            result.put("msgCode", msgCode);
            result.put("msg", msg);
            return result;
        }
        List<String[]> message = new ArrayList<String[]>();
        boolean flag = false;
        boolean toFlag = false;
        for (Object[] head : headlist) {
            try {
                String accTypeCode = (String) head[1];
                String curCode = (String) head[0];
                String interestSign = (String) head[2];
                String account = "";
                String corpCode = "";
                if (isMoreVoucherDtl && !isAccInCenter) {
                    corpCode = (String) head[4];
                } else {
                    account = (String) head[3];
                }
                List<FcInterestDtlTemp> interestDtlList = fcInterestDao.createCanEditInterestVoucher(netCode,
                        accTypeCode, curCode, interestSign, opType, user.getId(), account, corpCode,generateMode);
                result = createInterestVoucher(netCode, user, head, interestDtlList, isMoreVoucherDtl, poolSign);
                if (result.isEmpty()) {
                } else {
                    toFlag = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = true;
                String error = e.getMessage();
                message.add(new String[]{"-2", error});
            }
        }
        if (toFlag && !flag) {
            return result;
        }
        if (message != null && message.size() > 0) {
            if (flag) {
                msg = "";
                //有未开账户
                for (String[] err : message) {
                    if ("-2".equals(err[0])) {
                        msg += err[1] + "/";
                    }
                }
                if (msg.endsWith("/")) {
                    msg = msg.substring(0, msg.length() - 1);
                }
                msgCode = "-2";
            } else {
                msg = "本计息周期内利息为0，不能计息!";
                msgCode = "-3";
            }
        }
        result.put("msgCode", msgCode);
        result.put("msg", msg);
        return result;
    }
    /**
     * <b>功能介绍：</b>构建利息凭证<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, Object> createInterestVoucher(String netCode, ICurrentUser user,
                                                     Object[] head, List<FcInterestDtlTemp> interestDtlList, boolean isMoreVoucherDtl, boolean poolSign) {
        Map<String, Object> result = new HashMap<String, Object>();
        FcInterestDtlTemp interestDtlTemp = interestDtlList.get(interestDtlList.size() - 1);
        InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
        interestInfoContainer.setNetCode(netCode);
        interestInfoContainer.setTallyDate(interestDtlTemp.getTallyDate());
        interestInfoContainer.setSessionId(user.getId());
        interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes((String) head[1], AccTypeAllFilterType.BATCH));
        interestInfoContainer.setSysInstDay(InterestHelper.isSysInstDay(interestDtlTemp.getBlnInstDate(), getSysBatchInstDay())); //是否计息日
        interestInfoContainer.setSysPredrawRushWay(getSysPredrawRushWay()); //预提冲预提方式
        interestInfoContainer.setSysBatchInterestWay(getSysBatchInstWay()); //批量结息方式
        interestInfoContainer.setSysFixInterestWay(getSysFixInstWay()); //定期分段计息方式
        interestInfoContainer.setSysRateType(new BigDecimal(getDivisorBySysRateType())); //系统利率类型
        if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
            interestInfoContainer.setAccInCenter(true);//利息收入\利息支出账号是否挂中心
        } else {
            interestInfoContainer.setAccInCenter(false);
        }
        interestInfoContainer.setMoreVoucherDtl(isMoreVoucherDtl);//计息凭证类型是否多条分录
        interestInfoContainer.setFixedActrueDays(fixedActrueDays());//定期月份计算日利率天数
        ShiroUser userBean = new ShiroUser(user.getId(), user.getLoginName(), user.getUsername());
        interestInfoContainer.setUserBean(userBean);
        AccInterestInfo accInterestInfo = new AccInterestInfo();
        accInterestInfo.setInterestDtls(interestDtlList);
        accInterestInfo.setContainer(interestInfoContainer);
        AccTypeKeyBean accTypeKeyBean = new AccTypeKeyBean(netCode, (String) head[0], (String) head[1]);
        accInterestInfo.setAccTypeKeyBean(accTypeKeyBean);
        accInterestInfo.setNeedSave(false);//不需要保存凭证信息
        accInterestInfo.setPoolSign(poolSign);//资金池标识
        IInterestProvider interestProvider = InterestHelper.getInterestProvider((String) head[1]);
        List<FcFinacialAccbookVO> accbook = null;
        if (InterestConstant.INTEREST_SIGN_IST.equals((String) head[2])) {
            interestProvider.createInterestVoucher(accInterestInfo);
            accbook = accInterestInfo.getInterestAccbook();
        } else {
            interestProvider.createPredrawVoucher(accInterestInfo);
            accbook = accInterestInfo.getPredrawAccbook();
        }
        if (null == accbook || EmptyUtils.isEmptyList(accbook)) {
            return result;
        }
        BigDecimal accbookTot = BigDecimal.ZERO;
        BigDecimal accbookCreditTot = BigDecimal.ZERO;
        for (FcFinacialAccbookVO fcFinacialAccbook : accbook) {
            accbookTot = BigDecimal.ZERO;
            accbookCreditTot = BigDecimal.ZERO;
            fcFinacialAccbook.setTallyDate(interestDtlTemp.getTallyDate());
            fcFinacialAccbook.setInterestStartDate(interestDtlTemp.getTallyDate());
            List<FcFinacialAccbookDtlVO> accbookDtls = fcFinacialAccbook.getFcAccbookDtls();
            for (FcFinacialAccbookDtlVO dtl : accbookDtls) {
                accbookCreditTot = accbookCreditTot.add(dtl.getCreditMoney());
                accbookTot = accbookTot.add(dtl.getDebitMoney());
            }
            //设置汇总信息
            fcFinacialAccbook.setCreditSum(accbookCreditTot);
            fcFinacialAccbook.setDebitSum(accbookTot);
        }
        result.put("voucherInfo", accbook);
        result.put("voucherTypeName", iFcVoucherTypeService.getFcVoucherTypeById(accbook.get(0).getVoucherType()).getVoucherName());
        result.put("interestDtlList", accInterestInfo);
        return result;
    }
    /**
     * <b>功能介绍：</b>加载积数清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, Object> queryCanEditAccuBalance(DataTablesInput pgd, String netCode,
                                                       String accType, String opType, String sessionId, String accountCode,String generateMode) {
        if(EmptyUtils.isEmpty(generateMode)){
            generateMode=InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        int accuBalanceCounts = 0;
        int accuCounts = 0;
        int noAccuCounts = 0;
        if (StringUtils.isBlank(accType) || accType.indexOf(InnerAccountConstant.FCACC_BUSINESS_BEGULAR) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_BANK_BEGULAR) != -1 || accType.indexOf(InnerAccountConstant.FCACC_BANK_DEPOSIT) != -1) {
            noAccuCounts = fcInterestDao.getNoAccuBalanceCounts(netCode, accType, opType, sessionId, accountCode, generateMode).intValue();
            accuBalanceCounts += noAccuCounts;
            //accuCounts = noAccuCounts;
        }
        if (StringUtils.isBlank(accType) || accType.indexOf(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_BANK_DEMAND) != -1 || accType.indexOf(InnerAccountConstant.FCACC_LOAN) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_BANK_BORROW) != -1 || accType.indexOf(InnerAccountConstant.FCACC_INNER_ACCOUNT) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_LOANBYMANDATE) != -1 || accType.indexOf(InnerAccountConstant.FCACC_OFFBALANCE) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_AGREEMENT) != -1) {
            accuCounts = fcInterestDao.getAccuBalanceCounts(netCode, accType, opType, sessionId, accountCode, generateMode).intValue();//积数余额表账号总数
            accuBalanceCounts += accuCounts;
        }
        if (accuBalanceCounts == 0) {
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("footer", new ArrayList<Map<String, Object>>());
            result.put("rowsCount", 0);
            return result;
        }
        result.put("rowsCount", accuBalanceCounts);//设置记录数
        int currentPage = pgd.getStart() / pgd.getLength() + 1;
        int pageSize = pgd.getLength();
        List<Map<String, Object>> accuBalances = new ArrayList<Map<String, Object>>();
        if ((currentPage - 1) * pageSize <= accuCounts
                || (currentPage * pageSize >= accuCounts && accuCounts / pageSize > currentPage)) {
            accuBalances = fcInterestDao.queryCanEditAccuBalance(pgd, netCode, accType, opType, sessionId, accountCode, generateMode);
        }
        //企业活期账户无需查询
        if (StringUtils.isBlank(accType) || accType.indexOf(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT) < 0) {
            int leftSize = pageSize - accuBalances.size();
            if (leftSize > 0) {
                int noAccucounts = 0;
                int count = currentPage * pageSize - accuCounts;
                List<Map<String, Object>> interestDtlTemps = fcInterestDao.queryCanEditNoAccuBalance(netCode, accType,
                        opType, sessionId, generateMode);
                int countDays = 0;
                int lastCountDays = 0;
                int length = count % pageSize;
                int startday = 0;
                boolean flag = false;
                int countSize = 0;
                for (Map<String, Object> map : interestDtlTemps) {
                    BigDecimal cumulation = (BigDecimal) map.get("CUMULATION");
                    //int days = ((Long) map.get("DAYS")).intValue();
                    //BigDecimal aCumu = cumulation.divide(new BigDecimal(days), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal aCumu = cumulation.divide((BigDecimal) map.get("DAYS"), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal cd = (BigDecimal) map.get("DAYS");
                    int countDay = cd.intValue();
                    countDays += countDay;
                    if (flag || countDays >= count || countDays >= count - pageSize) {//满足取的数据，构造数据
                        if (!flag && count >= pageSize) {
                            length = pageSize;
                            startday = (currentPage - 1) * pageSize - accuCounts;
                            if (pageSize * currentPage - accuCounts > lastCountDays) {
                                startday = pageSize * (currentPage - 1) - accuCounts - lastCountDays;
                            }
                        }
                        Date startDate = (Date) map.get("STARTDATE");
                        Date endDate = (Date) map.get("ENDDATE");
                        String account = (String) map.get("ACCOUNT");
                        String accName = (String) map.get("ACCNAME");
                        String accTypeName = (String) map.get("ACCTYPE");
                        for (int i = 0; i < length - countSize; i++) {
                            Map<String, Object> accuMap = new HashMap<String, Object>();
                            Date balanceDate = DateUtils.addDay(startDate, startday);
                            if (balanceDate.compareTo(endDate) > 0) {
                                startday = 0;
                                flag = true;
                                countSize = i;
                                break;
                            }
                            accuMap.put("BALANCEDATE", DateUtils.format(balanceDate));
                            accuMap.put("ACCOUNT", account);
                            accuMap.put("ACCNAME", accName);
                            accuMap.put("CUMU", aCumu);
                            accuMap.put("ADJUSTCUMU", BigDecimal.ZERO);
                            accuMap.put("ACCTYPE", accTypeName);
                            if (noAccucounts < leftSize) {
                                accuBalances.add(accuMap);
                                noAccucounts++;
                            } else {
                                break;
                            }
                            startday++;
                        }
                    }
                    if (accuBalances.size() == pageSize) {
                        break;
                    }
                    lastCountDays += countDay;
                }
            }
        }
        BigDecimal totACumu = BigDecimal.ZERO;
        BigDecimal totAAdjustCumu = BigDecimal.ZERO;
        /**
         * 添加小计
         */
        List<Map<String, Object>> accus = new ArrayList<Map<String, Object>>();
        String lastAccount = "";
        BigDecimal totACumu0 = BigDecimal.ZERO;
        BigDecimal totAAdjustCumu0 = BigDecimal.ZERO;
        for (Map<String, Object> accuBalanceInfo : accuBalances) {
            String account = (String) accuBalanceInfo.get("ACCOUNT");
            if (!StringUtils.isBlank(lastAccount) && !account.equals(lastAccount)) {
                /**
                 * 添加小计
                 */
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("BALANCEDATE", "");
                temp.put("ACCNAME", "");
                temp.put("CUMU", totACumu0);
                temp.put("ADJUSTCUMU", totAAdjustCumu0);
                temp.put("ACCOUNT", "total");
                temp.put("ACCTYPE", "");
                accus.add(temp);
                totACumu0 = BigDecimal.ZERO;
                totAAdjustCumu0 = BigDecimal.ZERO;
            }
            //Date balanceDate = (Date) accuBalanceInfo.get("BALANCEDATE");
            accuBalanceInfo.put("BALANCEDATE", accuBalanceInfo.get("BALANCEDATE"));
            String acumu = accuBalanceInfo.get("CUMU").toString();
            totACumu = totACumu.add(BigDecimal.valueOf(Double.parseDouble(acumu)));
            totACumu0 = totACumu0.add(BigDecimal.valueOf(Double.parseDouble(acumu)));
            Double aAdjuectCumu = Double.parseDouble(accuBalanceInfo.get("ADJUSTCUMU").toString());
            if (aAdjuectCumu != null) {
                totAAdjustCumu = totAAdjustCumu.add(BigDecimal.valueOf(aAdjuectCumu));
                totAAdjustCumu0 = totAAdjustCumu0.add(BigDecimal.valueOf(aAdjuectCumu));
            }
            accus.add(accuBalanceInfo);
            lastAccount = account;
        }
        /**
         * 添加小计
         */
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("BALANCEDATE", "");
        temp.put("ACCNAME", "");
        temp.put("CUMU", totACumu0);
        temp.put("ADJUSTCUMU", totAAdjustCumu0);
        temp.put("ACCOUNT", "total");
        temp.put("ACCTYPE", "");
        accus.add(temp);
        List<Map<String, Object>> footer = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerInfo = new HashMap<String, Object>();
        footerInfo.put("BALANCEDATE", "");
        footerInfo.put("ACCNAME", "");
        footerInfo.put("CUMU", totACumu);
        footerInfo.put("ADJUSTCUMU", totAAdjustCumu);
        footerInfo.put("ACCOUNT", "AllTotal");
        footerInfo.put("ACCTYPE", "");
        footer.add(footerInfo);
        result.put("rows", accus);
        result.put("footer", footer);
        return result;
    }
    /**
     * <b>功能介绍：</b>加载积数清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, Object> queryAccountAccuBalance(DataTablesInput pgd, String netCode,
                                                       String accType, String opType, String sessionId, String accountCode,String generateMode) {
        if(EmptyUtils.isEmpty(generateMode)){
            generateMode=InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        int accuBalanceCounts = 0;
        int accuCounts = 0;
        int noAccuCounts = 0;
        if (StringUtils.isBlank(accType) || accType.indexOf(InnerAccountConstant.FCACC_BUSINESS_BEGULAR) != -1
                || accType.indexOf(InnerAccountConstant.FCACC_BANK_BEGULAR) != -1 || accType.indexOf(InnerAccountConstant.FCACC_BANK_DEPOSIT) != -1) {
            noAccuCounts = fcInterestDao.getNoAccuBalanceCounts(netCode, accType, opType, sessionId, accountCode, generateMode).intValue();
            accuBalanceCounts += noAccuCounts;
        }
        if (accuBalanceCounts == 0) {
            result.put("rows", new ArrayList<Map<String, Object>>());
            result.put("footer", new ArrayList<Map<String, Object>>());
            result.put("rowsCount", 0);
            return result;
        }
        result.put("rowsCount", accuBalanceCounts);//设置记录数
        int currentPage = pgd.getStart() / pgd.getLength() + 1;
        int pageSize = pgd.getLength();
        List<Map<String, Object>> accuBalances = new ArrayList<Map<String, Object>>();
        if ((currentPage - 1) * pageSize <= accuCounts
                || (currentPage * pageSize >= accuCounts && accuCounts / pageSize > currentPage)) {
            accuBalances = fcInterestDao.queryCanEditAccuBalance(pgd, netCode, accType, opType, sessionId, accountCode, generateMode);
        }
        //企业活期账户无需查询
        if (StringUtils.isBlank(accType) || accType.indexOf(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT) < 0) {
            if (accuBalances.size() < pageSize) {
                int count = currentPage * pageSize - accuCounts;
                List<Map<String, Object>> interestDtlTemps = fcInterestDao.queryAccountNoAccuBalance(netCode, accountCode, accType,
                        opType, sessionId, generateMode);
                int countDays = 0;
                int lastCountDays = 0;
                int length = count % pageSize;
                int startday = 0;
                boolean flag = false;
                int countSize = 0;
                for (Map<String, Object> map : interestDtlTemps) {
                    BigDecimal cumulation = (BigDecimal) map.get("CUMULATION");
                    BigDecimal aCumu = cumulation.divide((BigDecimal) map.get("DAYS"), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal cd = (BigDecimal)(map.get("COUNTDAYS") != null ? map.get("COUNTDAYS") : map.get("countDays"));
                    int countDay = 0;
                    if(cd != null){
                        countDay = cd.intValue();
                    }

                    countDays += countDay;
                    if (flag || countDays >= count || countDays >= count - pageSize) {//满足取的数据，构造数据
                        if (!flag && count >= pageSize) {
                            length = pageSize;
                            startday = (currentPage - 1) * pageSize - accuCounts;
                            if (pageSize * currentPage - accuCounts > lastCountDays) {
                                startday = pageSize * (currentPage - 1) - accuCounts - lastCountDays;
                            }
                        }
                        Date startDate = (Date) (map.get("STARTDATE") != null ? map.get("STARTDATE") : map.get("startDate"));
                        Date endDate = (Date) (map.get("ENDDATE") != null ? map.get("ENDDATE") : map.get("endDate"));
                        String account = String.valueOf(map.get("ACCOUNT") != null ? map.get("ACCOUNT") : map.get("account"));
                        String accName = String.valueOf(map.get("ACCNAME") != null ? map.get("ACCNAME") : map.get("accName"));
                        String accTypeName = String.valueOf(map.get("ACCTYPE") != null ? map.get("ACCTYPE") : map.get("accType") );
                        for (int i = 0; i < length - countSize; i++) {
                            Map<String, Object> accuMap = new HashMap<String, Object>();
                            Date balanceDate = DateUtils.addDay(startDate, startday);
                            if (balanceDate.compareTo(endDate) > 0) {
                                startday = 0;
                                flag = true;
                                countSize = i;
                                break;
                            }
                            accuMap.put("BALANCEDATE", DateUtils.format(balanceDate));
                            accuMap.put("ACCOUNT", account);
                            accuMap.put("ACCNAME", accName);
                            accuMap.put("CUMU", aCumu);
                            accuMap.put("ADJUSTCUMU", BigDecimal.ZERO);
                            accuMap.put("ACCTYPE", accTypeName);
                            accuBalances.add(accuMap);
                            startday++;
                        }
                    }
                    if (accuBalances.size() == pageSize) {
                        break;
                    }
                    lastCountDays += countDay;
                }
            }
        }
        BigDecimal totACumu = BigDecimal.ZERO;
        BigDecimal totAAdjustCumu = BigDecimal.ZERO;
        /**
         * 添加小计
         */
        List<Map<String, Object>> accus = new ArrayList<Map<String, Object>>();
        String lastAccount = "";
        BigDecimal totACumu0 = BigDecimal.ZERO;
        BigDecimal totAAdjustCumu0 = BigDecimal.ZERO;
        for (Map<String, Object> accuBalanceInfo : accuBalances) {
            String account = (String) accuBalanceInfo.get("ACCOUNT");
            if (!StringUtils.isBlank(lastAccount) && !account.equals(lastAccount)) {
                /**
                 * 添加小计
                 */
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("BALANCEDATE", "");
                temp.put("ACCNAME", "");
                temp.put("CUMU", totACumu0);
                temp.put("ADJUSTCUMU", totAAdjustCumu0);
                temp.put("ACCOUNT", "total");
                temp.put("ACCTYPE", "");
                accus.add(temp);
                totACumu0 = BigDecimal.ZERO;
                totAAdjustCumu0 = BigDecimal.ZERO;
            }
            //Date balanceDate = (Date) accuBalanceInfo.get("BALANCEDATE");
            accuBalanceInfo.put("BALANCEDATE", accuBalanceInfo.get("BALANCEDATE"));
            String acumu = accuBalanceInfo.get("CUMU").toString();
            totACumu = totACumu.add(BigDecimal.valueOf(Double.parseDouble(acumu)));
            totACumu0 = totACumu0.add(BigDecimal.valueOf(Double.parseDouble(acumu)));
            Double aAdjuectCumu = Double.parseDouble(accuBalanceInfo.get("ADJUSTCUMU").toString());
            if (aAdjuectCumu != null) {
                totAAdjustCumu = totAAdjustCumu.add(BigDecimal.valueOf(aAdjuectCumu));
                totAAdjustCumu0 = totAAdjustCumu0.add(BigDecimal.valueOf(aAdjuectCumu));
            }
            accus.add(accuBalanceInfo);
            lastAccount = account;
        }
        /**
         * 添加小计
         */
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("BALANCEDATE", "");
        temp.put("ACCNAME", "");
        temp.put("CUMU", totACumu0);
        temp.put("ADJUSTCUMU", totAAdjustCumu0);
        temp.put("ACCOUNT", "total");
        temp.put("ACCTYPE", "");
        accus.add(temp);
        List<Map<String, Object>> footer = new ArrayList<Map<String, Object>>();
        Map<String, Object> footerInfo = new HashMap<String, Object>();
        footerInfo.put("BALANCEDATE", "");
        footerInfo.put("ACCNAME", "");
        footerInfo.put("CUMU", totACumu);
        footerInfo.put("ADJUSTCUMU", totAAdjustCumu);
        footerInfo.put("ACCOUNT", "AllTotal");
        footerInfo.put("ACCTYPE", "");
        footer.add(footerInfo);
        result.put("rows", accus);
        result.put("footer", footer);
        return result;
    }
    /**
     * <b>功能介绍：</b>加载不足款清单<br>
     * <b>日期：</b>2017年7月13日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcInterestVO> createCanEditLessPay(DataTablesInput pgd, String netCode, String opType,
                                                          String sessionId) {
        return fcInterestDao.createCanEditLessPay(pgd, netCode, opType, sessionId);
    }
    /**
     * <b>功能介绍：</b>查询未冲的预提数据<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> queryCanRushPredrawDtls(DataTablesInput pgd, String netId, String accType,
                                                             Date endInstDate, String account, String opType, String userCode) {
        return fcInterestDao.queryCanRushPredrawDtls(pgd, netId, accType, endInstDate, account, opType, userCode);
    }
    /**
     * <b>功能介绍：</b>查询带冲销账户信息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getPreRushAcc(String netCode, String accTypeCode) {
        return fcInterestDao.getPreRushAcc(netCode, accTypeCode, null);
    }
    /**
     * <b>功能介绍：</b>用户可操作暂存利息信息<br>
     * <b>日期：</b>2017年11月4日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netCode
     * @param accTypeCode
     * @param userCode
     * @param opType
     * @param pageName
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<Object[]> queryCanEditAccTypeInterestInfo(String netCode, String accTypeCode, String userCode,
                                                          String opType, String pageName) {
        Integer[] status = null;
        if ("initList".equals(opType)) {
            status = new Integer[]{InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_STATUE_TEMP};
        } else {
            status = new Integer[]{InterestConstant.INTEREST_WAIT_CHECK};
        }
        if ("batchInterest".equals(pageName)) { // 批量计息页面
            return fcInterestDao.queryAccTypeInterestInfo(netCode, accTypeCode, userCode, status);
        } else if ("batchRush".equals(pageName)) { // 批量冲预提
            return fcInterestDao.queryAccTypePredrawInfo(netCode, accTypeCode, opType);
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>利息清单导出<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param accType
     * @param opType
     * @param impType
     * @param sessionId
     * @return
     * @throws Exception
     * @version 1.0
     */
    public HSSFWorkbook doExportInterestBillXls(String netId, String accType, String opType, String impType,
                                                String sessionId, boolean poolSign, String account) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = null;
        List<InterestBillExportVO> datas = fcInterestDao.getExportXlsFcInterestData(netId, accType, opType, sessionId, account, isInterestZeroSign());
//        if (CollectionUtil.isNotEmpty(datas)){
//            List<String> corpIdS = datas.stream().map(InterestBillExportVO::getCorpId).distinct().collect(Collectors.toList());
//            Map<String, String> nameByIds = corpService.getNameByIds(corpIdS);
//            //interest.type
//            Map<String, DictDTO> interestTypeMap = iDictService.listMap("interest.type");
////            interest.sign
//            Map<String, DictDTO> interestSignMap = iDictService.listMap("interest.sign");
//            //interest.busStatus
//            Map<String, DictDTO> busStatusMap = iDictService.listMap("interest.busStatus");
//            //InnerAccount.AccType
//            Map<String, DictDTO> accTypeMap = iDictService.listMap("InnerAccount.AccType");
//            datas.stream().forEach(vo ->{
//                vo.setCorpName(nameByIds.get(vo.getCorpId()));
//                vo.setInterestName(interestTypeMap.get(vo.getInterestType()) != null ? interestTypeMap.get(vo.getInterestType()).getName(): null );
//                vo.setInterestSignName(interestSignMap.get(vo.getInterestSign()) != null ? interestSignMap.get(vo.getInterestSign()).getName() : null );
//                vo.setBusStatusName( busStatusMap.get(String.valueOf(vo.getBusStatus())) != null ? busStatusMap.get(String.valueOf(vo.getBusStatus())).getName() : null );
//                vo.setAccTypeName( accTypeMap.get(vo.getAccType()) != null ?  accTypeMap.get(vo.getAccType()).getName() : null );
//            });
//
//        }
        //单位
        String headCorpCode = null;
        //币别
        String headCurCode = null;
        //账户类别
        String headAccTypeCode = null;
        //入息标志
        String headIstSign = null;
        //分页标识
        String busKey = null;
        //标题样式
        HSSFCellStyle mainHeadStyle = this.initTitleStyle(workbook);
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 14);
        headFont.setBold(true);
        mainHeadStyle.setFont(headFont);//字体增粗
        //分页表头样式
        HSSFCellStyle rowHeadStyle = this.initTitleStyle(workbook);
        rowHeadStyle.setAlignment(HorizontalAlignment.RIGHT);
        //值样式
        HSSFCellStyle headDataCellStyle = this.initTitleStyle(workbook);
        headDataCellStyle.setAlignment(HorizontalAlignment.LEFT);
        //表头样式
        HSSFCellStyle headCellStyle = this.initColumnHeadStyle(workbook);
        //数据样式
        HSSFCellStyle cellStyle = this.initColumnCenterstyle(workbook);
        HSSFCellStyle dataCellStyle = this.initColumnCenterstyle(workbook);
        dataCellStyle.setAlignment(HorizontalAlignment.RIGHT);
        //footRow
        HSSFCellStyle footCellStyle = this.initColumnCenterstyle(workbook);
        footCellStyle.setAlignment(HorizontalAlignment.LEFT);
        if (!datas.isEmpty()) {
            //处理 corpName\accTypeName
            List<String> corpIds = datas.stream().map(InterestBillExportVO::getCorpId).distinct().collect(Collectors.toList());
            List<String> accTypeIds = datas.stream().map(InterestBillExportVO::getAccType).collect(Collectors.toList());
            accTypeIds = accTypeIds.stream().distinct().collect(Collectors.toList());
            Map<String, String> corpNameMap = corpService.getNameByIds(corpIds);
            Map<String, String> accTypeNameMap = iDictService.getNameByTypeCodesAndTypeGroupCode(accTypeIds,"InnerAccount.AccType");
//            if ("checkList".equals(opType)){
//                //个别计息-企业活期存款-复核 打印利息清单
//                accTypeNameMap = iDictService.getNameByTypeCodesAndTypeGroupCode(accTypeIds,"InnerAccount.AccType");
//            }else {
//               accTypeNameMap = iDictService.getNameByTypeCodesAndTypeGroupCode(accTypeIds,"btBankAcc.accType"); // btBankAcc.accType -- 批量计息页面-经办-导出
//            }

           List<String> corpIdS = datas.stream().map(InterestBillExportVO::getCorpId).distinct().collect(Collectors.toList());
            Map<String, String> nameByIds = corpService.getNameByIds(corpIdS);
            //interest.type
            Map<String, DictDTO> interestTypeMap = iDictService.listMap("interest.type");
//            interest.sign
            Map<String, DictDTO> interestSignMap = iDictService.listMap("interest.sign");
            //interest.busStatus
            Map<String, DictDTO> busStatusMap = iDictService.listMap("interest.busStatus");
            //InnerAccount.AccType
            Map<String, String> accTypeMap = accTypeNameMap;
            datas.stream().forEach(vo ->{
                vo.setCorpName(nameByIds.get(vo.getCorpId()));
                vo.setInterestName(interestTypeMap.get(vo.getInterestType()) != null ? interestTypeMap.get(vo.getInterestType()).getName(): null );
                vo.setInterestSignName(interestSignMap.get(vo.getInterestSign()) != null ? interestSignMap.get(vo.getInterestSign()).getName() : null );
                vo.setBusStatusName( busStatusMap.get(String.valueOf(vo.getBusStatus())) != null ? busStatusMap.get(String.valueOf(vo.getBusStatus())).getName() : null );
                vo.setAccTypeName( null !=accTypeMap.get(vo.getAccType()) ?  accTypeMap.get(vo.getAccType()) : null );
            });

            //获取合计
            Map<String, BigDecimal> totals = this.getExportInterestBillXlsTotal(netId, headAccTypeCode, opType, sessionId);
            int index = 3;
            int count = 0;
            int arg = 0;
            for (InterestBillExportVO data : datas) {
                data.setInterestName(iDictService.get("interest.type",data.getInterestType()));
                data.setInterestSignName(iDictService.get("interest.sign",data.getInterestSign()));
                data.setBusStatusName(iDictService.get("interest.busStatus",data.getBusStatus()));
                String corpCode = data.getCorpId();
//                String corpName = data.getCorpName();
                String corpName = corpNameMap.get(corpCode);
                String curCode = data.getCurId();
                String curName = data.getCurName();
                String accTypeCode = data.getAccType();
//                String accTypeName = data.getAccTypeName();
                String accTypeName = accTypeNameMap.get(accTypeCode);
                String istSign = data.getInterestSign();
                String dataKey = corpCode + "-" + curCode + "-" + accTypeCode + "-" + istSign;
                if (EmptyUtils.isNotEmpty(headCorpCode) && !dataKey.equals(busKey)) {
                    String headKey = headCorpCode + "-" + headCurCode + "-" + headAccTypeCode + "-" + headIstSign;
                    BigDecimal totalIst = totals.get(headKey);
                    Map<String, Object> totalData = new HashMap<String, Object>();
                    totalData.put("interest", totalIst);
                    //创建合计
                    index = this.writeXlsBookFootRow(workbook, sheet, index++, totalIst, footCellStyle, dataCellStyle,
                            headIstSign, headAccTypeCode, poolSign);
                }
                if (StringUtils.isEmpty(busKey) || !dataKey.equals(busKey)) {
                    Map<String, String> msgForCur = new HashMap<String, String>();
                    msgForCur.put("corpName", corpName);
                    msgForCur.put("curName", curName);
                    msgForCur.put("accTypeName", accTypeName);
                    if (FinacialConstant.BOOLEAN_TRUE.equals(impType)) {//分页
                        //增加 arg,确保sheetname唯一
                        sheet = workbook.createSheet(corpName + "-" + curName + "-" + accTypeName + arg);
                        arg++;
                        //创建表头
                        this.writeXlsBookHead(workbook, sheet, msgForCur, mainHeadStyle,
                                rowHeadStyle, headDataCellStyle, headCellStyle);
                        index = 3;
                    } else {
                        if (StringUtils.isEmpty(busKey)) {
                            sheet = workbook.createSheet(getLangStr("table.interet.lxqd"));//利息清单
                            //创建表头
                            this.writeXlsBookHead(workbook, sheet, msgForCur, mainHeadStyle,
                                    rowHeadStyle, headDataCellStyle, headCellStyle);
                            index = 3;
                        } else {
                            index = this.writeXlsPageStyleBookHead(workbook, sheet, msgForCur, mainHeadStyle,
                                    rowHeadStyle, headDataCellStyle, headCellStyle, index);
                        }
                    }
                    busKey = dataKey;
                    headCorpCode = corpCode;
                    headCurCode = curCode;
                    headAccTypeCode = accTypeCode;
                    headIstSign = istSign;
                }
                HSSFRow row = sheet.createRow(index++);
                row.setHeightInPoints(18);
                //创建数据明细
                this.writeXlsBookDatas(workbook, row, data, cellStyle, dataCellStyle);
                if (count == datas.size() - 1) {
                    String headKey = headCorpCode + "-" + headCurCode + "-" + headAccTypeCode + "-" + headIstSign;
                    BigDecimal totalIst = totals.get(headKey);
                    Map<String, Object> totalData = new HashMap<String, Object>();
                    totalData.put("interest", totalIst);
                    //创建合计
                    this.writeXlsBookFootRow(workbook, sheet, index++, totalIst, footCellStyle, dataCellStyle,
                            headIstSign, headAccTypeCode, poolSign);
                }
                count++;
            }
        } else {
            sheet = workbook.createSheet(getLangStr("table.interet.lxqd"));//利息清单
            Map<String, String> msgForCur = new HashMap<String, String>();
            msgForCur.put("corpName", "");
            msgForCur.put("curName", "");
            msgForCur.put("accTypeName", "");
            //创建表头
            this.writeXlsBookHead(workbook, sheet, msgForCur, mainHeadStyle,
                    rowHeadStyle, headDataCellStyle, headCellStyle);
        }
        return workbook;
    }
    /**
     * <b>功能介绍：</b>不分页表头<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @return
     * @version 1.0
     */
    private int writeXlsPageStyleBookHead(HSSFWorkbook workbook, HSSFSheet sheet, Map<String, String> msgForCur,
                                          HSSFCellStyle headStyle, HSSFCellStyle rowHeadStyle, HSSFCellStyle dataCellStyle,
                                          HSSFCellStyle cellStyle, int index) {
        //分页表头
        HSSFRow rowHead1 = sheet.createRow(index++);
        rowHead1.setHeightInPoints(20);
        HSSFCell head1Cell0 = rowHead1.createCell(0);
        head1Cell0.setCellValue(getLangStr("table.fcChargeType.curCode") + "：");//币别
        head1Cell0.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellCorpCodeVal1 = rowHead1.createCell(1);
        cellCorpCodeVal1.setCellValue(msgForCur.get("curName"));
        cellCorpCodeVal1.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress((index - 1), (index - 1), (short) 1, (short) 2));
        HSSFCell cellCur1 = rowHead1.createCell(3);
        cellCur1.setCellValue(getLangStr("table.nisBillhead.corpId") + "：");//单位
        cellCur1.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellCurVal1 = rowHead1.createCell(4);
        cellCurVal1.setCellValue(msgForCur.get("corpName"));
        cellCurVal1.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress((index - 1), (index - 1), (short) 4, (short) 7));
        HSSFCell cellReqDate = rowHead1.createCell(8);
        cellReqDate.setCellValue(getLangStr("table.interet.AccType") + "：");//开户类型
        cellReqDate.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellReqDateVal = rowHead1.createCell(9);
        cellReqDateVal.setCellValue(msgForCur.get("accTypeName"));
        cellReqDateVal.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress((index - 1), (index - 1), (short) 9, (short) 10));
        HSSFRow rowHead3 = sheet.createRow(index++);
        rowHead3.setHeightInPoints(20);
        //"账号", "户名" , "起息日期", "结束日期", "天数", "积数","利率", "利息", "利率类型", "计息类型", "状态"
        String[] heads = new String[]{getLangStr("table.fcAccuBalance.account"),
                getLangStr("table.fcAcc.accName"),
                getLangStr("table.fcInterestDtlTemp.interestStartDate"),
                getLangStr("table.fcInterestDtlTemp.blnInstDate"),
                getLangStr("table.fcInterestDtlTemp.days"),
                getLangStr("table.fcInterestDtlTemp.cumulation"),
                getLangStr("table.fcInterestDtlTemp.interestRate"),
                getLangStr("table.fcInterestDtlTemp.interest"),
                getLangStr("table.fcInterestDtlTemp.rateType"),
                getLangStr("table.fcInterestDtlTemp.interestType"),
                getLangStr("table.fcInterestDtlTemp.busStatus")};
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = rowHead3.createCell(i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    sheet.setColumnWidth(i, 256 * 20);
                    break;
                case 1:
                    sheet.setColumnWidth(i, 256 * 35);
                    break;
                case 2:
                case 3:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                case 4:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                case 5:
                case 6:
                case 7:
                    sheet.setColumnWidth(i, 256 * 20);
                    break;
                case 8:
                case 9:
                case 10:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                default:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
            }
        }
        return index;
    }
    /**
     * <b>功能介绍：</b>创建明细数据<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void writeXlsBookDatas(HSSFWorkbook workbook, HSSFRow row, InterestBillExportVO data,
                                   HSSFCellStyle cellStyle, HSSFCellStyle dataCellStyle) {
        for (int i = 0; i < 11; i++) {
            HSSFCell cell = row.createCell(i);
            switch (i) {
                case 0:
                    cell.setCellValue(data.getAccount());
                    cell.setCellStyle(cellStyle);
                    break;
                case 1:
                    cell.setCellValue(data.getAccName());
                    cell.setCellStyle(cellStyle);
                    break;
                case 2:
                    cell.setCellValue(DateUtils.date2Str(data.getInterestStartDate(), DateUtils.date_sdf()));
                    cell.setCellStyle(cellStyle);
                    break;
                case 3:
                    cell.setCellValue(DateUtils.date2Str(data.getBlnInstDate(), DateUtils.date_sdf()));
                    cell.setCellStyle(cellStyle);
                    break;
                case 4:
                    cell.setCellValue((Integer) data.getDays());
                    cell.setCellStyle(cellStyle);
                    break;
                case 5:
                    cell.setCellValue(data.getCumulation() == null ? "" :
                            NumberUtil.formatNumeric(((BigDecimal) data.getCumulation()).doubleValue(), "#,##0.00"));
                    cell.setCellStyle(dataCellStyle);
                    break;
                case 6:
                    cell.setCellValue(data.getInterestRate() == null ? "" : this.showRate(data.getInterestRate().doubleValue()));
                    cell.setCellStyle(dataCellStyle);
                    break;
                case 7:
                    cell.setCellValue(data.getInterest() == null ? "" :
                            NumberUtil.formatNumeric(((BigDecimal) data.getInterest()).doubleValue(), "#,##0.00"));
                    cell.setCellStyle(dataCellStyle);
                    break;
                case 8:
                    cell.setCellValue(data.getInterestName());
                    cell.setCellStyle(cellStyle);
                    break;
                case 9:
                    cell.setCellValue(data.getInterestSignName());
                    cell.setCellStyle(cellStyle);
                    break;
                case 10:
                    cell.setCellValue(data.getBusStatusName());
                    cell.setCellStyle(cellStyle);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * <b>功能介绍：</b>创建表头<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private void writeXlsBookHead(HSSFWorkbook workbook, HSSFSheet sheet, Map<String, String> msgForCur,
                                  HSSFCellStyle headStyle, HSSFCellStyle rowHeadStyle, HSSFCellStyle dataCellStyle,
                                  HSSFCellStyle cellStyle) {
        //创建 表头
        HSSFRow rowHead = sheet.createRow(0);
        rowHead.setHeight((short) (255 * 2));
        HSSFCell cellHead = rowHead.createCell(0);
        //标题
        cellHead.setCellValue(getLangStr("table.interet.lxqd"));
        cellHead.setCellStyle(headStyle);
        for (int i = 1; i < 11; i++) {
            HSSFCell cell = rowHead.createCell(i);
            cell.setCellValue(new HSSFRichTextString());
            cell.setCellStyle(headStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) 10));
        //分页表头
        HSSFRow rowHead1 = sheet.createRow(1);
        rowHead1.setHeightInPoints(20);
        HSSFCell head1Cell0 = rowHead1.createCell(0);
        head1Cell0.setCellValue(getLangStr("table.fcChargeType.curCode") + "：");//币别
        head1Cell0.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellCorpCodeVal1 = rowHead1.createCell(1);
        cellCorpCodeVal1.setCellValue(msgForCur.get("curName"));
        cellCorpCodeVal1.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 1, (short) 2));
        HSSFCell cellCur1 = rowHead1.createCell(3);
        cellCur1.setCellValue(getLangStr("table.nisBillhead.corpId") + "：");//单位
        cellCur1.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellCurVal1 = rowHead1.createCell(4);
        cellCurVal1.setCellValue(msgForCur.get("corpName"));
        cellCurVal1.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 4, (short) 7));
        HSSFCell cellReqDate = rowHead1.createCell(8);
        cellReqDate.setCellValue(getLangStr("table.interet.AccType") + "：");//开户类型
        cellReqDate.setCellStyle(rowHeadStyle);
        sheet.setColumnWidth(0, 256 * 100);
        HSSFCell cellReqDateVal = rowHead1.createCell(9);
        cellReqDateVal.setCellValue(msgForCur.get("accTypeName"));
        cellReqDateVal.setCellStyle(dataCellStyle);
        sheet.setColumnWidth(0, 256 * 100);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 9, (short) 10));
        HSSFRow rowHead3 = sheet.createRow(2);
        rowHead1.setHeightInPoints(20);
        rowHead3.setHeightInPoints(20);
//		String[] heads = new String[]{"账号", "户名" , "起息日期", "结束日期", "天数", "积数","利率", "利息", "利率类型", "计息类型", "状态"};
        String[] heads = new String[]{getLangStr("table.fcAccuBalance.account"),
                getLangStr("table.fcAcc.accName"),
                getLangStr("table.fcInterestDtlTemp.interestStartDate"),
                getLangStr("table.fcInterestDtlTemp.blnInstDate"),
                getLangStr("table.fcInterestDtlTemp.days"),
                getLangStr("table.fcInterestDtlTemp.cumulation"),
                getLangStr("table.fcInterestDtlTemp.interestRate"),
                getLangStr("table.fcInterestDtlTemp.interest"),
                getLangStr("table.fcInterestDtlTemp.rateType"),
                getLangStr("table.fcInterestDtlTemp.interestType"),
                getLangStr("table.fcInterestDtlTemp.busStatus")};
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = rowHead3.createCell(i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    sheet.setColumnWidth(i, 256 * 20);
                    break;
                case 1:
                    sheet.setColumnWidth(i, 256 * 35);
                    break;
                case 2:
                case 3:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                case 4:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                case 5:
                case 6:
                case 7:
                    sheet.setColumnWidth(i, 256 * 20);
                    break;
                case 8:
                case 9:
                case 10:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
                default:
                    sheet.setColumnWidth(i, 256 * 15);
                    break;
            }
        }
    }
    /**
     * <b>功能介绍：</b>合计行<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    private int writeXlsBookFootRow(HSSFWorkbook workbook, HSSFSheet sheet, int index, BigDecimal totalIst,
                                    HSSFCellStyle cellStyle, HSSFCellStyle dataCellStyle, String istSign, String accTypeCode, boolean poolSign) {
        HSSFRow totoalRow = sheet.createRow(index++);
        totoalRow.setHeightInPoints(20);
        for (int i = 0; i < 6; i++) {
            HSSFCell cell = totoalRow.createCell(i);
            cell.setCellValue(new HSSFRichTextString());
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress((index - 1), (index - 1), (short) 0, (short) 5));
        HSSFCell cell6 = totoalRow.createCell(6);
        cell6.setCellValue(new HSSFRichTextString());
        cell6.setCellStyle(cellStyle);
        HSSFCell cell7 = totoalRow.createCell(7);
        if (null != totalIst) {
            cell7.setCellValue(NumberUtil.formatNumeric(totalIst.doubleValue(), "#,##0.00"));
        } else {
            cell7.setCellValue(NumberUtil.formatNumeric(BigDecimal.ZERO, "#,##0.00"));
        }
        cell7.setCellStyle(dataCellStyle);
        HSSFCell cell8 = totoalRow.createCell(8);
        cell8.setCellValue(new HSSFRichTextString());
        cell8.setCellStyle(cellStyle);
        HSSFCell cell9 = totoalRow.createCell(9);
        cell9.setCellValue(new HSSFRichTextString());
        cell9.setCellStyle(cellStyle);
        HSSFCell cell10 = totoalRow.createCell(10);
        cell10.setCellValue(new HSSFRichTextString());
        cell10.setCellStyle(cellStyle);
        HSSFRow footRow1 = sheet.createRow(index++);
        footRow1.setHeightInPoints(20);
        for (int i = 0; i < 6; i++) {
            HSSFCell cell = footRow1.createCell(i);
            cell.setCellValue(new HSSFRichTextString());
            cell.setCellStyle(cellStyle);
        }
        HSSFRow footRow2 = sheet.createRow(index++);
        footRow2.setHeightInPoints(20);
        for (int i = 0; i < 6; i++) {
            HSSFCell cell = footRow2.createCell(i);
            cell.setCellValue(new HSSFRichTextString());
            cell.setCellStyle(cellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress((index - 2), (index - 1), (short) 0, (short) 5));
        HSSFCellStyle style = this.initColumnCenterstyle(workbook);
        HSSFCell footCell6 = footRow1.createCell(6);
        footCell6.setCellValue(getLangStr("table.interet.vourcherfl"));//凭证分录
        footCell6.setCellStyle(style);
        HSSFCell footCell60 = footRow2.createCell(6);
        footCell60.setCellValue(new HSSFRichTextString());
        footCell60.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress((index - 2), (index - 1), (short) 6, (short) 6));
        String[] vchInfo = new String[]{"", ""};
        if (poolSign) {//资金池分录
            String poolInfo = InterestConstant.POOL_INTEREST_ACCTYPE_VOUCHER_MAPPING.get(accTypeCode);
            poolInfo = multiLangServiceI.getLang(poolInfo);
            vchInfo = poolInfo.split(";");
        } else {
            if (InterestConstant.INTEREST_SIGN_IST.equals(istSign)) {
                vchInfo = InterestConstant.INTEREST_ACCTYPE_VOUCHER_MAPPING.get(accTypeCode);
            } else {
                vchInfo = InterestConstant.PREDRAW_ACCTYPE_VOUCHER_MAPPING.get(accTypeCode);
            }
        }
        HSSFCell footRow1Cell7 = footRow1.createCell(7);
        footRow1Cell7.setCellValue(vchInfo[0]);
        footRow1Cell7.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress((index - 2), (index - 2), (short) 7, (short) 10));
        HSSFCell footRow1Cell8 = footRow1.createCell(8);
        footRow1Cell8.setCellValue(new HSSFRichTextString());
        footRow1Cell8.setCellStyle(cellStyle);
        HSSFCell footRow1Cell9 = footRow1.createCell(9);
        footRow1Cell9.setCellValue(new HSSFRichTextString());
        footRow1Cell9.setCellStyle(cellStyle);
        HSSFCell footRow1Cell10 = footRow1.createCell(10);
        footRow1Cell10.setCellValue(new HSSFRichTextString());
        footRow1Cell10.setCellStyle(cellStyle);
        HSSFCell footRow2Cell7 = footRow2.createCell(7);
        footRow2Cell7.setCellValue(vchInfo[1]);
        footRow2Cell7.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress((index - 1), (index - 1), (short) 7, (short) 10));
        HSSFCell footRow2Cell8 = footRow2.createCell(8);
        footRow2Cell8.setCellValue(new HSSFRichTextString());
        footRow2Cell8.setCellStyle(cellStyle);
        HSSFCell footRow2Cell9 = footRow2.createCell(9);
        footRow2Cell9.setCellValue(new HSSFRichTextString());
        footRow2Cell9.setCellStyle(cellStyle);
        HSSFCell footRow2Cell10 = footRow2.createCell(10);
        footRow2Cell10.setCellValue(new HSSFRichTextString());
        footRow2Cell10.setCellStyle(cellStyle);
        return index + 1;
    }
    /**
     * <b>功能介绍：</b>查询导出合计<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netCode
     * @param accType
     * @param opType
     * @param sessionId
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getExportInterestBillXlsTotal(String netCode, String accType, String opType,
                                                                 String sessionId) {
        List<Map<String, Object>> totals = fcInterestDao.getExportXlsFcInterestTotal(netCode, accType, opType, sessionId);
        Map<String, BigDecimal> totalMaps = new HashMap<String, BigDecimal>();
        for (Map<String, Object> total : totals) {
            String corpId = (String) total.get("CORPID");
            String curId = (String) total.get("CURID");
            String accType1 = (String) total.get("ACCTYPE");
            String istSign = (String) total.get("ISTSIGN");
            String totalIstStr = total.get("TOTALIST").toString();
            BigDecimal totalIst = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(totalIstStr)) {
                totalIst = BigDecimal.valueOf(Double.valueOf(totalIstStr));
            }
            String dataKey = corpId + "-" + curId + "-" + accType1 + "-" + istSign;
            totalMaps.put(dataKey, totalIst);
        }
        return totalMaps;
    }
    /**
     * <b>功能介绍：</b>数据样式<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param wb
     * @return
     * @version 1.0
     */
    public HSSFCellStyle initColumnCenterstyle(HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontName("Consolas");
        font.setFontHeightInPoints((short) 10);
        HSSFCellStyle centerstyle = wb.createCellStyle();
        centerstyle.setFont(font);
        centerstyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中   
        centerstyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中   
        centerstyle.setWrapText(true);
        centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
        centerstyle.setBorderLeft(BorderStyle.THIN);
        centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
        centerstyle.setBorderRight(BorderStyle.THIN);
        centerstyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体   
        centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．   
        centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
        return centerstyle;
    }
    /**
     * <b>功能介绍：</b>标题样式<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param wb
     * @return
     * @version 1.0
     */
    private HSSFCellStyle initTitleStyle(HSSFWorkbook wb) {
        HSSFCellStyle columnHeadStyle = wb.createCellStyle();
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadFont.setBold(true);
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 居中   
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中   
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        return columnHeadStyle;
    }
    /**
     * <b>功能介绍：</b>表头样式<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param wb
     * @return
     * @version 1.0
     */
    private HSSFCellStyle initColumnHeadStyle(HSSFWorkbook wb) {
        HSSFCellStyle columnHeadStyle = wb.createCellStyle();
        HSSFFont columnHeadFont = wb.createFont();
        columnHeadFont.setFontName("宋体");
        columnHeadFont.setFontHeightInPoints((short) 10);
        columnHeadFont.setBold(true);
        columnHeadStyle.setFont(columnHeadFont);
        columnHeadStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中   
        columnHeadStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中   
        columnHeadStyle.setLocked(true);
        columnHeadStyle.setWrapText(true);
        columnHeadStyle.setTopBorderColor(HSSFColor.BLACK.index);
        columnHeadStyle.setBorderTop(BorderStyle.THIN);
        columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色   
        columnHeadStyle.setBorderLeft(BorderStyle.THIN);// 边框的大小   
        columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色   
        columnHeadStyle.setBorderRight(BorderStyle.THIN);// 边框的大小   
        columnHeadStyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体   
        columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色   
        // 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）   
        columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        return columnHeadStyle;
    }
    /**
     * <b>功能介绍：</b>查询利息通知单<br>
     * <b>日期：</b>2017年11月23日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param opType
     * @param netCode
     * @param accType
     * @param tallyDate
     * @param blnInstDate
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<InterestRateNoticeVO> getRateNotice(String opType, String netCode, String accType, String tallyDate, String blnInstDate, String userId, String account,String generateMode) {
        if(EmptyUtils.isEmpty(generateMode)){
            generateMode=InterestConstant.GENERATEMODE_INTEREST_BATCH;
        }
        boolean zeroSign = isInterestZeroSign();
        List<InterestRateNoticeVO> rateNotices = fcInterestDao.getRateNoticeHead(opType, netCode, accType, tallyDate, blnInstDate, userId, account,generateMode, zeroSign);
        List<String> corpIds = rateNotices.stream().map(InterestRateNoticeVO::getCorpId).collect(Collectors.toList());
        List<String> curIds = rateNotices.stream().map(InterestRateNoticeVO::getCurId).collect(Collectors.toList());
        Map<String, String> corpNameMap = corpService.getNameByIds(corpIds);
        Map<String, String> currencyNameMap = btCurrencyService.getCurrencyNameByIds(curIds);
        for (InterestRateNoticeVO rateNotice : rateNotices) {
            List<FcInterestDtlTempVo> dtls = fcInterestDao.getInterestDtlTempByAccountTallyDate(rateNotice.getAccount(), rateNotice.getCurId(), tallyDate,generateMode, zeroSign);
            rateNotice.setInterestDtls(dtls);
            String corpName=corpService.getCorpName(rateNotice.getCorpId());
            rateNotice.setCorpName(corpName);
            BtCurrencyDTO currencyDTO = currencyService.getObjectById(rateNotice.getCurId());
            rateNotice.setCurName(currencyDTO.getCurName());
            rateNotice.setNowDate(DateUtils.date2Str(DateUtils.date_sdf()));
            rateNotice.setCorpName(corpNameMap.get(rateNotice.getCorpId()));
            rateNotice.setCurName(currencyNameMap.get(rateNotice.getCurId()));
            if (!EmptyUtils.isEmptyList(dtls)){
                if ("initList".equals(opType) ) {
//                rateNotice.setPrintName(fcInterestDao.getUserNameById(dtls.get(0).getOperatorCode()));
                    Map<String, String> userNameByIds = iUserService.getUserNameByIds(Lists.newArrayList(dtls.get(0).getOperatorCode()));
                    rateNotice.setPrintName(userNameByIds.get(dtls.get(0).getOperatorCode()));
                } else {
//                rateNotice.setPrintName(fcInterestDao.getUserNameById(dtls.get(0).getOperatorCode()));//经办人
//                rateNotice.setCheckerName(fcInterestDao.getUserNameById(userId));//复核人
                    Map<String, String> userNameByIds = iUserService.getUserNameByIds(Lists.newArrayList(dtls.get(0).getOperatorCode(),userId) );
                    rateNotice.setPrintName(userNameByIds.get(dtls.get(0).getOperatorCode()));
                    rateNotice.setCheckerName(userNameByIds.get(userId));
                }
            }
        }
        return rateNotices;
    }
    /**
     * <b>功能介绍：</b>根据流水号查询利息明细<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param orderNo
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcInterestDtl> getInterestsByOrderNo(String orderNo) {
        return fcInterestDao.getInterestsByOrderNo(orderNo);
    }
    /**
     * <b>功能介绍：</b>删除已计息的冲预提信息<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param depositManagerId
     * @version 1.0
     */
    public void deleteRushPredrawByDepositMangerId(String depositManagerId) {
        fcInterestDao.deleteRushPredrawByDepositMangerId(depositManagerId);
    }
    /**
     * <b>功能介绍：</b>是否存在在途利息<br>
     * <b>日期：</b>2017年9月28日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public boolean existUnOverInterest(String account) {
        return fcInterestDao.existUnOverInterest(account);
    }
    /**
     * <b>功能介绍：</b>处理利息电子回单号<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param orderNo
     * @param eReceiptNo
     * @version 1.0
     */
    public void processEReceiptNo(String account, String orderNo, String eReceiptNo) {
        fcInterestDao.processEReceiptNo(account, orderNo, eReceiptNo);
    }
    /**
     * <b>功能介绍：</b>根据流水号删除利息<br>
     * <b>日期：</b>2017年12月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param orderNo
     * @version 1.0
     */
    public void deleteInterestByOrderNo(String orderNo) {
        fcInterestDao.deleteRepayLoanInterests(orderNo);
    }
    /**
     * <b>功能介绍：</b>格式化利率显示<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param number
     * @return
     * @version 1.0
     */
    public String showRate(Double number) {
        if (number == null) {
            return "";
        }
        Integer rate = configService.getConfig(ConfigConstant.SYS, "rate").toInteger();
        String formatNumber;
        if (rate.equals(1)) {
            DecimalFormat df = new DecimalFormat("0.00");
            formatNumber = df.format(number * 1000) + "‰";
        } else {
            DecimalFormat df = new DecimalFormat("0.0000");
            formatNumber = df.format(number * 100) + "%";
        }
        return formatNumber;
    }
    /**
     * <b>功能介绍：</b>获取国际化<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param str
     * @return
     * @version 1.0
     */
    public String getLangStr(String str) {
        return multiLangServiceI.getLang(str);
    }
    /**
     * <b>功能介绍:</b>获取系统参数-每月批量计息日<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getSysBatchInstDay() {
        return configService.getConfig("ist", "batchInstDay").toString();
    }
    /**
     * <b>功能介绍:</b>获取系统参数-预提冲预提方式<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getSysPredrawRushWay() {
        return configService.getConfig("ist", "predrawRushWay").toString();
    }
    /**
     * <b>功能介绍:</b>获取系统参数-批量结息方式<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getSysBatchInstWay() {
        return configService.getConfig("ist", "batchInstWay").toString();
    }
    /**
     * <b>功能介绍:</b>获取系统参数-定期计息方式<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getSysFixInstWay() {
        return configService.getConfig("ist", "fixInstWay").toString();
    }
    /**
     * <b>功能介绍:</b>获取系统参数-定期月份计算日利率天数：0,30天;1:实际天数<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean fixedActrueDays() {
        return configService.getConfig("ist", "fixedActrueDays").toInteger().intValue() == 1;
    }
    /**
     * <b>功能介绍：</b>获取系统参数-批量计息是否自动复核<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean isBatchInstAutoCheck() {
        return configService.getConfig("ist", "batchInstAutoCheck").toInteger().intValue() == 1;
    }
    /**
     * <b>功能介绍：</b>获取系统参数-利率类型<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getSysInstRateType() {
        Integer rate = configService.getConfig(ConfigConstant.SYS, RATE).toInteger();
        if (rate.equals(1)) {
            return "M";
        } else {
            return "Y";
        }
    }
    /**
     * <b>功能介绍：</b>获取系统参数-批量计息是否自动复核<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean isInterestZeroSign() {
        return configService.getConfig("ist", "zeroInterest").toInteger().intValue() == 1;
    }
    /**
     * <b>功能介绍：</b>获取转换日利率的除数<br>
     * <b>日期：</b>2017年7月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public long getDivisorBySysRateType() {
        return "Y".equals(getSysInstRateType()) ? InterestConstant.RATE_YEAR_DIV : InterestConstant.RATE_MONTH_DIV;
    }
    static final String RATE = "rate";
    public void doPoolAccInterest(String netId, String accTypeCode, String account, Date endInstDate, ICurrentUser user) {
    }
    /**
     * <br>
     * <b>功能：</b> 资金池批量/个别计息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2019/5/22 <br>
     */
    public InterestResult istPoolAccrualByBatch(String netCode, String accTypeCode, Date tallyDate, Date endInstDate,
                                                ICurrentUser userBean, OperateType opType, String account) throws Exception {
        //1、删除此次会话中暂存的预提明细和利息明细.
        fcInterestDao.deletePredraw(netCode, null, InterestConstant.INTEREST_STATUE_TEMP, account, null);
        fcInterestDao.deleteInterest(netCode, null, InterestConstant.INTEREST_STATUE_TEMP, account, null);
        doDeleteByBatch(netCode, accTypeCode, userBean.getId(), account, null);
        // 2、初始化计息容器
        InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
        interestInfoContainer.setNetCode(netCode);
        interestInfoContainer.setTallyDate(tallyDate);
        interestInfoContainer.setEndInstDate(endInstDate);
//        interestInfoContainer.setUserBean(new ShiroUser(userBean.getId(), userBean.getLoginName(), userBean.getUsername()));
        interestInfoContainer.setSessionId(userBean.getId());
        interestInfoContainer.setAccount(account);//个别计息账号
        /*if(EmptyUtils.isNotEmpty(account)) {
		    interestInfoContainer.setOpType(OperateType.PART_INTEREST);//个别计息
        } else {
        }*/
        interestInfoContainer.setPoolSign(true);
        interestInfoContainer.setOpType(opType);
        interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes(accTypeCode, AccTypeAllFilterType.BATCH));
        interestInfoContainer.setAccount(account);
        // 读取系统参数设置
        interestInfoContainer.setAccInCenter(true);
        interestInfoContainer.setSysRateType(new BigDecimal(getDivisorBySysRateType())); //系统利率类型
        // 3、计息
        InterestResult interestResult = new BaseInterestProcessor(interestInfoContainer, iFcAccService).istAccrual(userBean);
        // 4、当提交时更新此次会话中结息账户的积数计息标志和计息时间
        if (opType == OperateType.SUBMIT) {
            if (null != interestInfoContainer.getInterestTime()) {
                Map<String, Object> result = validateInterestVoucher(netCode, accTypeCode, "validList", userBean, true, null,null);
                String msgCode = (String) result.get("msgCode");
                if ("-1".equals(msgCode) || "-2".equals(msgCode) || "-3".equals(msgCode)) {
                    logger.debug("不满足记账凭证条件");
                    throw new ParameterException(InterestMessageEnum.FIN_INTEREST_VOUCHERNOTPASS);
                }
                fcInterestDao.updateAccuForBatch(userBean.getId(), endInstDate,
                        interestInfoContainer.getInterestTime(), InterestConstant.INTEREST_SIGN_YES, netCode);
                fcInterestDao.updateAccuForBatch(userBean.getId(), endInstDate,
                        interestInfoContainer.getInterestTime(), InterestConstant.INTEREST_SIGN_NO, netCode);
                if (isBatchInstAutoCheck()) {
                    processInterestPass(interestInfoContainer, accTypeCode);
                }
            } else {
                logger.debug("本计息周期内没有可以计息的数据，不能计息!");
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);//不满足记账凭证条件
            }
        }
         // 消息提醒 notificationService.complete(NotificationConstant.FINACIAL.INTEREST_CHECK,fcInterestDtlTemp.getId(),userId);
        notificationService.send(netCode, account, NotificationConstant.NETBANK.BANKACC_REQ_HANDLE_SUCCESS, interestResult);
        return interestResult;
    }
    /**
     * 数据保存至FcInterestDtl表
     *
     * @param fcInterestDtls
     */
    public void saveFcInterestDtl(List<FcInterestDtl> fcInterestDtls) {
        List<String> accounts = fcInterestDtls.stream().map(FcInterestDtl::getAccount).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        fcInterestDtls.forEach( a -> {
            FcAccDTO fcAccDTO = map.get(a.getAccount());
            a.setAccName(fcAccDTO.getAccName());
            a.setAccType(fcAccDTO.getAccType());
            a.setNetId(fcAccDTO.getNetId());
            a.setCorpId(fcAccDTO.getCorpId());
            a.setCurId(fcAccDTO.getCurId());
            a.setCurName(fcAccDTO.getCurName());
        });
        fcInterestDao.batchSave(fcInterestDtls);
    }
    /**
     * 单位禁用校验
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (fcInterestDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_INTEREST);
            corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_INTEREST_END_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    /**
     * 查询内部账户
     *
     * @param currentUser 当前登录人
     * @param accType     账户类别
     * @return
     */
    public List<AccountDTO> getInterestAccount(ICurrentUser currentUser, String accType, String resource) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        try {
            // 获取资源下的单位ID
            List<String> userCorpIds = this.corpService.getUserCorpIds(resource, currentUser.getId());
            StringBuilder joinSql = new StringBuilder();
            joinSql.append(" and t.pool_sign = '0' ");
            if (EmptyUtils.isNotEmpty(accType)) {
                joinSql.append(" and t.acc_type= ").append(accType).append(" ");
            }
            List<String> accStates = new ArrayList<>();
            accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
            List<FcAccDTO> listFcAcc = iFcAccService.getFcAccByCorpIdList(userCorpIds, accStates, joinSql.toString());
            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                list = copyFcAccDTOList(listFcAcc);
            }
        } catch (Exception e) {
            logger.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return null;
        }
        return list;
    }
    /**
     * 将FcAccDTO集合cope到AccountDTO集合
     *
     * @param listFcAcc 内部账户集合
     * @return
     */
    public List<AccountDTO> copyFcAccDTOList(List<FcAccDTO> listFcAcc) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        for (FcAccDTO fcAcc : listFcAcc) {
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setBankAccId(fcAcc.getId());
            accountDTO.setCorpId(fcAcc.getCorpId());
            accountDTO.setCorpName(fcAcc.getCorpName());
            accountDTO.setBankAcc(fcAcc.getBlnctrAcc());
            accountDTO.setAccName(fcAcc.getAccName());
            accountDTO.setBankName(fcAcc.getCorpName());
            accountDTO.setBankCode(fcAcc.getBankCode());
            accountDTO.setCurId(fcAcc.getCurId());
            accountDTO.setCurName(fcAcc.getCurName());
            accountDTO.setFcBalance(fcAcc.getFcBalance());
            list.add(accountDTO);
        }
        return list;
    }
    /**
    * <br>
    * <b>功能：</b> 个别计息复核 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/20 0020 <br>
    */
    public void processSingleInterestCheck(AccInterestBean accBean) {
        String netCode = accBean.getNetCode();
        String accTypeCode = accBean.getAccTypeCode();
        Date tallyDate = accBean.getTallyDate();
        Date endInstDate = accBean.getEndInstDate();
        ICurrentUser user = accBean.getUser();
        OperateType opType = accBean.getOpType();
        String account = accBean.getAccount();
        boolean poolSign = accBean.isPoolSign();
        String generateMode = accBean.getGenerateMode();
        String userId = user.getId();
        if (EmptyUtils.isEmpty(generateMode)) {
            generateMode = InterestConstant.GENERATEMODE_INTEREST_PART;
        }
        List<FcInterestDtlTemp> interestDtlTemps = fcInterestDao.queryFcInterestDtlTemps(netCode, accTypeCode, endInstDate, userId, account,generateMode);
        List<FcPredrawDtlTemp> predrawDtlTemps = fcInterestDao.queryFcPredrawDtlTemps(netCode, accTypeCode, endInstDate, userId, account);
        for (FcPredrawDtlTemp preDtl : predrawDtlTemps) {//设置预提对应的利息明细
            preDtl.setInterestDtls(fcInterestDao.getFcInterestDtlTempByPreId(preDtl.getId()));
        }
        if (opType == OperateType.CHECK) {//复核通过
            InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
            interestInfoContainer.setNetCode(netCode);
            interestInfoContainer.setAccount(account);
            interestInfoContainer.setGenerateMode(generateMode);
            interestInfoContainer.setTallyDate(tallyDate);
            interestInfoContainer.setEndInstDate(endInstDate);
//            interestInfoContainer.setUserBean(new ShiroUser(userId, user.getLoginName(), user.getUsername()));
            interestInfoContainer.setOpType(opType);
            interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes(accTypeCode, AccTypeAllFilterType.BATCH));
            interestInfoContainer.setSeasonEnd(InterestHelper.isSeasonEnd(endInstDate)); //是否季末
            interestInfoContainer.setSysInstDay(InterestHelper.isSysInstDay(endInstDate, getSysBatchInstDay())); //是否计息日
            interestInfoContainer.setFixedActrueDays(fixedActrueDays());//定期月份计算日利率天数
            interestInfoContainer.setPoolSign(poolSign);
            interestInfoContainer.setAccBean(accBean);//账户计息Bean对象
            groupInterestByInterestType(interestInfoContainer, interestDtlTemps, predrawDtlTemps);
            processSingleInterestPass(interestInfoContainer, accTypeCode);
        } else if (opType == OperateType.BACK) {//复核打回
            if (EmptyUtils.isEmpty(accTypeCode) || accTypeCode.indexOf(InnerAccountConstant.FCACC_LOAN) != -1) {
                fcInterestDao.checkLessPayBill(netCode, accTypeCode, endInstDate, InterestConstant.INTEREST_BACK);
            }
            fcInterestDao.backInterestDtlTemp(netCode, accTypeCode, endInstDate, userId, account,generateMode);
        }
    }
    /**
    * <br>
    * <b>功能：</b> 处理个别计息复核通过 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/20 0020 <br>
    */
    private void processSingleInterestPass(InterestInfoContainer interestInfoContainer, String accTypeCode) {
        interestInfoContainer.setOpType(OperateType.CHECK);
        new BaseInterestProcessor(interestInfoContainer, iFcAccService).singleInterest();
        //2、不足款清单复核通过
        if (StringUtils.isBlank(accTypeCode) || accTypeCode.indexOf(InnerAccountConstant.FCACC_LOAN) != -1) {
            Date endInstDate = interestInfoContainer.getEndInstDate();
            String netCode = interestInfoContainer.getNetCode();
            fcInterestDao.checkLessPayBill(netCode, InnerAccountConstant.FCACC_LOAN, endInstDate, InterestConstant.INTEREST_STATUE_CHECK);
        }
    }
    /**
     * <br>
     * <b>功能：</b> 更新利息计息标识ID <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void updateInterestSingle(String istId, String singleId) {
        fcInterestDao.updateInterestSingle(istId, singleId);
    }
    /**
    * <br>
    * <b>功能：</b> 删除打回或者暂存的临时利息数据 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/25 0025 <br>
    */
    public void deleteLoanSignleInterest(String singleId) {
        fcInterestDao.deleteInterestSingleDtl(singleId);
    }
    /**
     * <b>功能介绍:</b>计息/提交<br>
     * <b>日期：</b>2017年7月5日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public InterestResult interestLoanSingle(String netCode, String account, String accTypeCode, Date tallyDate, Date endInstDate,
                                                 ICurrentUser userBean, OperateType opType, BigDecimal tellerMoney, String businessId) {
        String generateMode = InterestConstant.GENERATEMODE_INTEREST_PART;
        //1、删除此次会话中暂存的利息明细,还原计息数据
        fcInterestDao.deleteInterest(netCode, null, InterestConstant.INTEREST_STATUE_TEMP, account, generateMode);
        fcInterestDao.deleteInterest(netCode, null, InterestConstant.INTEREST_BACK, account, generateMode);
        // 2、初始化计息容器
        InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
        interestInfoContainer.setGenerateMode(generateMode);
        interestInfoContainer.setAccount(account);
        interestInfoContainer.setNetCode(netCode);
        interestInfoContainer.setEndInstDate(endInstDate);
        interestInfoContainer.setTallyDate(tallyDate);
//        interestInfoContainer.setUserBean(new ShiroUser(userBean.getId(), userBean.getLoginName(), userBean.getUsername()));
        interestInfoContainer.setSessionId(userBean.getId());
        interestInfoContainer.setOpType(opType);
        interestInfoContainer.setAccTypeCodes(InterestHelper.getAccTypes(accTypeCode, AccTypeAllFilterType.BATCH));
        interestInfoContainer.setSeasonEnd(InterestHelper.isSeasonEnd(endInstDate)); //是否季末
        interestInfoContainer.setLastWorkDay(InterestHelper.isLastDayOfMonth(endInstDate)); //是否月末最后一个工作日
        // 读取系统参数设置
        if (configService.getConfig("ist", "entrySign").toInteger().intValue() == 0) {
            interestInfoContainer.setAccInCenter(true);//按结算中心计息
        } else {
            interestInfoContainer.setAccInCenter(false);//按单位或银行计息
        }
        interestInfoContainer.setMoreVoucherDtl(isMoreVoucherDtl());//分录条数
        interestInfoContainer.setSysInstDay(InterestHelper.isSysInstDay(endInstDate, getSysBatchInstDay())); //是否计息日
        interestInfoContainer.setSysPredrawRushWay(getSysPredrawRushWay()); //预提冲预提方式
        interestInfoContainer.setSysBatchInterestWay(getSysBatchInstWay()); //批量结息方式
        interestInfoContainer.setSysFixInterestWay(getSysFixInstWay()); //定期分段计息方式
        interestInfoContainer.setSysRateType(new BigDecimal(getDivisorBySysRateType())); //系统利率类型
        interestInfoContainer.setFixedActrueDays(fixedActrueDays());//定期月份计算日利率天数
        interestInfoContainer.setTellerMoney(tellerMoney);
        interestInfoContainer.setBillCode(businessId);//业务ID
        splitAccyBalances(interestInfoContainer);//按照还款金额拆分积数
        // 3、计息
        InterestResult interestResult = new BaseInterestProcessor(interestInfoContainer, iFcAccService).singleInterestaccrual(userBean);
        updateAccuBalanceSign(interestInfoContainer);//按照还款金额拆分积数
        return interestResult;
    }
    /**
     * <br>
     * <b>功能：</b> 拆分正常积数为还款积数和剩余积数 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    public void splitAccyBalances(InterestInfoContainer interestInfoContainer) {
        //根据取款金额拆分积数
        List<FcAccuBalancef> accuBalances = fcInterestDao.queryAccuBalanceForAcc(interestInfoContainer.getAccount(), interestInfoContainer.getEndInstDate());
        BigDecimal money = interestInfoContainer.getTellerMoney();//取款金额
        List<FcAccuBalancef> newAccuBalances = new ArrayList<FcAccuBalancef>();//剩余未归还积数
        List<FcAccuBalancef> repayAccuBalances = new ArrayList<FcAccuBalancef>();//归还积数
        List<FcAccuBalancef> unPayAccuBalances = new ArrayList<FcAccuBalancef>();//剩余未归还积数
        if (money != null && money.compareTo(BigDecimal.ZERO) > 0) {
            //2、对积数进行拆分
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
                    rePayAccuBalance.setPreSign(InterestConstant.INTEREST_SIGN_NO);
                    rePayAccuBalance.setInterestSign(InterestConstant.INTEREST_SIGN_NO);
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
                    newAccuBalances.add(rePayAccuBalance);
                } else {
                    //积数小于还款金额，积数全部计算
                    BeanUtils.copyProperties(istFcAccuBalance, rePayAccuBalance);
                    rePayAccuBalance.setId(null);
                    repayAccuBalances.add(rePayAccuBalance);
                }
            }
            interestInfoContainer.setAccuBalances(repayAccuBalances);//本次需要计息的积数
            if (OperateType.SUBMIT.equals(interestInfoContainer.getOpType())) {
                //原积数拆分为还款部分积数和剩余未还积数
                getEntityDao().batchSave(newAccuBalances);//保存拆分积数
                getEntityDao().batchUpdate(unPayAccuBalances);//保存拆分积数
            }
        } else {
            interestInfoContainer.setAccuBalances(accuBalances);//本次需要计息的积数
        }
    }
    public void updateAccuBalanceSign(InterestInfoContainer interestInfoContainer) {
        if (!OperateType.SUBMIT.equals(interestInfoContainer.getOpType())) {
            return;
        }
        List<FcAccuBalancef> accuBalances = interestInfoContainer.getAccuBalances();
        List<String> ids = new ArrayList<>();
        for (FcAccuBalancef accuBalancef : accuBalances) {
            ids.add(accuBalancef.getId());
        }
        loanRepayService.saveLoanBalanceDtls(interestInfoContainer.getBillCode(),
                ids, interestInfoContainer.getInterestTime());
    }

    /**
     * 计息到期提醒定时任务
     * @param arguments
     */
    @XxlJob("FcInterestService")
    public void run(String arguments) {
        this.logger.info("---计息到期提醒定时任务开启---");
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.IST, BaseDataConstant.INSWARNDAYS);
        //根据参数获取提前天数
        int days = Integer.parseInt(configDTO.getValue());
        //当前日期
        Date currentDate = DateUtils.getDateYMD();
        //获取每月计息日
        ConfigDTO configDTO1 = configService.getConfig(ConfigConstant.IST, BaseDataConstant.BATCHINSDAYS);
        int days1 = Integer.parseInt(configDTO1.getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, days1);
        Date endDate = calendar.getTime();
        Date startDate = DateUtils.addDay(endDate, -days);
        startDate = DateUtils.getDateYMD(startDate);
        HashMap<String, Date> preMap = null;
        List<Map<String, Object>> preList = null;
        //是否需要计息
        boolean isInterest = false;
        if (currentDate.compareTo(startDate)>=0) {
            Map<String, Object> map = new HashMap<>();
            map.put("interestTime", DateUtils.format(endDate, "yyyy-MM-dd"));
            //获取计息账户
            List<FcAccDTO> fcAccDTOList = iFcAccService.getFcAccByInterest();
            for (FcAccDTO fcAccDTO : fcAccDTOList) {
                //获取账户的上次预提、结息时间
                preList = fcInterestDao.getInterestLastTimeForNone(fcAccDTO.getNetId(), fcAccDTO.getAccType());
                preMap = new HashMap<String, Date>();
                if (!preList.isEmpty()) {
                    for (Map<String, Object> map1 : preList) {
                        String account = (String) map1.get("ACCOUNT");
                        Date startDate1 = (Date) map.get("INTERESTSTARTDATE");
                        Date preDateStr;
                        if(null == map1.get("PREDATE") ){
                            preDateStr = startDate1;
                        } else {
                            preDateStr = (Date) map1.get("PREDATE");
                            preDateStr = DateUtils.addDay(preDateStr, 1);//预提次日
                        }
                        Date preDate;
                        if (preDateStr==null){
                            preDate = null;
                        }else{
                            preDate = DateUtils.parseDate(preDateStr);
                        }
                        preMap.put(account, preDate);
                    }
                }
                //定义是否有计息数据
                boolean interestDtlByAccountAndDate = false;
                if (preMap.get(fcAccDTO.getBlnctrAcc())!=null){
                    interestDtlByAccountAndDate = this.getInterestDtlByAccountAndDate(fcAccDTO.getBlnctrAcc(), DateUtils.addDay(preMap.get(fcAccDTO.getBlnctrAcc()),1), endDate);
                }
                if (interestDtlByAccountAndDate){
                    isInterest = true;
                }
            }
            if (isInterest){
                notificationService.send(null, null,
                        NotificationConstant.FINACIAL.FINACIAL_INTEREST_CALCBYBATCH_HAND, map);
            }
        }
        this.logger.info("---计息到期提醒定时任务结束---");
    }

    /**
     * 查询计息账号
     * @param blnctrAcc
     * @param date
     * @param endDate
     * @return
     */
    private boolean getInterestDtlByAccountAndDate(String blnctrAcc, Date date, Date endDate) {
        if (date.compareTo(endDate)>0){
            return false;
        }
        List<FcInterestDtlVo> list = fcInterestDao.getInterestDtlByAccountAndDate(blnctrAcc, date, endDate);
        if (CollectionUtils.isEmpty(list)||list.size()==0){
            return true;
        }
        return false;
    }
}
