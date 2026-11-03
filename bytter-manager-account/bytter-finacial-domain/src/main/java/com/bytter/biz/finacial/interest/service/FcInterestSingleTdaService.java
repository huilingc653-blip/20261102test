package com.bytter.biz.finacial.interest.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.api.constant.*;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.bean.AccInterestBean;
import com.bytter.biz.finacial.interest.bean.AccTypeInterestInfoBean;
import com.bytter.biz.finacial.interest.dao.FcInterestSingleTdaDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcInterestSingle;
import com.bytter.biz.finacial.interest.vo.*;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.FcAccSubjectDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Service("fcInterestSingleTdaService")
@Transactional
public class FcInterestSingleTdaService extends BaseService<FcInterestSingle, String> {
    @Autowired
    private FcInterestSingleTdaDao fcInterestSingleDdaDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcAccService iFcAccService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired(required = false)
    private FcInterestService fcInterestService;
    @DubboReference(check = false)
	private IFcInterestService interestService;
    @DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
    @DubboReference(check = false)
	private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @Autowired
    private FcAccbookService fcAccbookService;
    @DubboReference(check = false)
	private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
    @Autowired
    private FcVoucherCashService fcVoucherCashService;
    @Autowired(required = false)
    protected IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
	private IFcBusRaceService iFcBusRaceService;
    @Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Override
    public BaseDao<FcInterestSingle, String> getEntityDao() {
        return fcInterestSingleDdaDao;
    }
    public FcInterestSingleVO getEntityById(String id) {
        FcInterestSingleVO vo = fcInterestSingleDdaDao.getFcInterestSingleDda(id); //这里没有全局ID？
        if (ObjectUtils.isNotEmpty(vo)){
            FcAccVO fcAccVOByBlnctrAcc = iFcAccService.getFcAccVOByBlnctrAcc(vo.getAccount());
            vo.setCurId(fcAccVOByBlnctrAcc.getCurId());
            vo.setNetId(fcAccVOByBlnctrAcc.getNetId());
            vo.setPoolSign(fcAccVOByBlnctrAcc.getPoolSign());
        }
        return vo;
    }
    /**
     * <br>
     * <b>功能：</b> 查询个别计息数据 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listAccount(DataTablesInput input, String resource, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        String eqs_accType = (String) input.getFormdata().get("eqs_accType");
        if (EmptyUtils.isEmpty(eqs_accType)) {
            input.getFormdata().put("ins_accType", "01,05,03,08");
        }
        List<FcAccSubjectDTO> fcAccSubjectDTOS = iFcAccService.getAccCurDataByCorpIds(corpAuth);
        List<String> accounts = fcAccSubjectDTOS.stream().map(FcAccSubjectDTO::getBlnctrAcc).collect(Collectors.toList());
        Map<String, FcAccSubjectDTO> accountMap = fcAccSubjectDTOS.stream().collect(Collectors.toMap(FcAccSubjectDTO::getBlnctrAcc, Function.identity()));
        DataTablesOutput dataTablesOutput = fcInterestSingleDdaDao.listAccount(input, corpAuth);
        List data = dataTablesOutput.getData();
        ArrayList<FcInterestDtlVo> fcInterestDtlVos = new ArrayList<>();
        for (Object datum : data) {
            FcInterestDtlVo vo = (FcInterestDtlVo)datum;
            FcAccSubjectDTO fcAccSubjectDTO = accountMap.get(vo.getAccount());
            vo.setAccName(fcAccSubjectDTO.getAccName());
            vo.setCurName(fcAccSubjectDTO.getCurName());
            fcInterestDtlVos.add(vo);
        }
        dataTablesOutput.setData(fcInterestDtlVos);
        return dataTablesOutput;
    }
    /**
     * 查询当前登录人管理单位下所有企业定期的个别计息数据
     *
     * @param input       请求对象
     * @param currentUser 当前登录人
     * @return
     */
    public DataTablesOutput queryFcInterestSingleDda(DataTablesInput input, ICurrentUser currentUser) {
        String opType = (String) input.getFormdata().get("opType");
        String resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_DDA_HAND;
        if (EmptyUtils.isNotEmpty(opType) && opType.equals("checkList")) {
            resource = ResourceConstant.FINACIAL_INTEREST_SINGLE_DDA_CHECK;
        }
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, currentUser.getId()), "t.corp_Id", true);
        Object ins_corpId = input.getFormdata().get("ins_corpId");
        String commonSql = null;
        if (!EmptyUtils.isEmpty(corpAuthSql)) {
            commonSql = " and " + corpAuthSql;
        } else {
            commonSql = " and 1 <> 1 ";
        }
        DataTablesOutput dataTablesOutput = fcInterestSingleDdaDao.queryFcInterestSingleDda(input, currentUser.getId(), commonSql);
        List data = dataTablesOutput.getData();
        if (CollectionUtil.isNotEmpty(data)){
            ArrayList<String> accounts = new ArrayList<>();
            ArrayList<FcInterestSingleVO> fcInterestSingleVOS = new ArrayList<>();
            for (Object datum : data) {
                FcInterestSingleVO vo = (FcInterestSingleVO) datum;
                accounts.add(vo.getAccount());
                fcInterestSingleVOS.add(vo);
            }
            List<com.bytter.system.api.dto.FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
            Map<String, com.bytter.system.api.dto.FcAccDTO> accountMap = fcAccDTOS.stream().collect(Collectors.toMap(com.bytter.system.api.dto.FcAccDTO::getBlnctrAcc, Function.identity()));
            Map<String, String> curMap = currencyService.getCurrencyNameMap();
            fcInterestSingleVOS.forEach(a -> {
                com.bytter.system.api.dto.FcAccDTO fcAccDTO = accountMap.get(a.getAccount());
                a.setCurId(fcAccDTO.getCurId());
                a.setCurName(curMap.get(fcAccDTO.getCurId()));
                a.setNetId(fcAccDTO.getNetId());
                a.setCorpId(fcAccDTO.getCorpId());
                a.setCorpName(corpService.getCorpName(fcAccDTO.getCorpId()));
                a.setPoolSign(fcAccDTO.getPoolSign());
                a.setAccName(fcAccDTO.getAccName());
            });
            dataTablesOutput.setData(fcInterestSingleVOS);
        }

        return dataTablesOutput;
    }
    /**
     * 查询指定账户个别计息谢谢
     *
     * @param account 内部账户
     * @return
     */
    public FcInterestSingleVO queryFcInterestSingle(String account) {
        return fcInterestSingleDdaDao.queryFcInterestSingle(account);
    }
    /**
     * 查询利息明细的记账日期和起息日期
     *
     * @param fcAcc    内部账户对象
     * @param userCode 当前登录人
     * @param opType   initList-经办 check-复核
     * @return
     */
    @Transactional(readOnly = true)
    public List<AccTypeInterestInfoBean> queryCanEditAccTypeInterestInfo(FcAccDTO fcAcc, String userCode, String opType) {
        Integer[] status = new Integer[]{InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_STATUE_TEMP};
        if (EmptyUtils.isNotEmpty(opType) && !"initList".equals(opType)) {
            status = new Integer[]{InterestConstant.INTEREST_WAIT_CHECK};
        }
        // FC_Acc 在基础模块，此处关联 只取一个 acc_type
        //return fcInterestSingleDdaDao.queryAccTypeInterestInfo(fcAcc.getNetId(), fcAcc.getAccType(), fcAcc.getBlnctrAcc(), userCode, status);
        List<AccTypeInterestInfoBean> beanList = fcInterestSingleDdaDao.queryAccTypeInterestInfo(fcAcc.getNetId(), fcAcc.getAccType(), fcAcc.getBlnctrAcc(), userCode, status);
        beanList.forEach(a ->{
            a.setAccType(fcAcc.getAccType());
        });
        return beanList;
    }
    /**
     * <b>功能介绍：</b>初始化日期显示<br>
     *
     * @param accTypeInterestInfos 查询利息明细的记账日期和起息日期信息
     * @param closeDate            网点的日结日期
     * @return
     */
    public Map<String, Object> tallyDateInit(List<AccTypeInterestInfoBean> accTypeInterestInfos, Date closeDate) {
        Date blnInstDate = null;
        Date tallyDate = null;
        boolean isShowBlnInstDate = true;
        Long compareBlnInstDateTime = null;
        if (accTypeInterestInfos != null && accTypeInterestInfos.size() > 0) {
            for (AccTypeInterestInfoBean accTypeInterestInfo : accTypeInterestInfos) {
                Long blnInstDateTime = accTypeInterestInfo.getBlnInstDate().getTime();
                if (compareBlnInstDateTime == null) {
                    compareBlnInstDateTime = blnInstDateTime;
                    blnInstDate = accTypeInterestInfo.getBlnInstDate();
                    tallyDate = accTypeInterestInfo.getTallyDate();
                    continue;
                }
                if (compareBlnInstDateTime.longValue() != blnInstDateTime.longValue()) {
                    isShowBlnInstDate = false;
                    break;
                }
            }
        }
        if (!isShowBlnInstDate) {
            blnInstDate = null;
            tallyDate = null;
        }
        //判断止息日期是否为空，若为空则网点日结日期延后一天
        if (blnInstDate != null) {
            blnInstDate = DateUtils.addDay(blnInstDate, 1);
        } else {
            blnInstDate = DateUtils.addDay(closeDate, 1);
        }
        //判断记账日期是否为空，若为空则网点日结日期延后一天
        if (tallyDate == null) {
            tallyDate = DateUtils.addDay(closeDate, 1);
        }
        Map<String, Object> dataInfo = new HashMap<>();
        dataInfo.put("blnInstDate", blnInstDate);
        dataInfo.put("tallyDate", tallyDate);
        return dataInfo;
    }
    /**
     * 经办界面根据账户主键查询所属网点日结日期计算出记账日期和止息日期
     *
     * @param accountId   账户ID
     * @param opType      操作类型  initList-经办 checkList-复核
     * @param currentUser 当前登录人
     * @return
     */
    public Map<String, Object> tallyDateInit(String accountId, String opType, ICurrentUser currentUser) {
        com.bytter.system.api.dto.FcAccDTO fcAcc = iFcAccService.getFcAccDTO(accountId);//根据内部账户id查询内部账户信息
        if (fcAcc == null) {
            return null;
        }
        Date closeDate = dayCloseService.getDayCloseFinalDate(fcAcc.getNetId());//根据内部账户所属网点查询该网点的日结日期
        // todo:数据转换
        FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(fcAcc, FcAccDTO.class);
        List<AccTypeInterestInfoBean> accTypeInterestInfos = queryCanEditAccTypeInterestInfo(fcAccDTO, currentUser.getId(), opType);
        Map<String, Object> map = tallyDateInit(accTypeInterestInfos, closeDate);
        if (map != null) {
            map.put("netId", fcAcc.getNetId());//网点ID
            map.put("netName", fcAcc.getNetName());//网点名称
            map.put("curId", fcAcc.getCurId());//币别ID
            map.put("curName", fcAcc.getCurName());//币别名称
            map.put("poolSign", InnerAccountConstant.POOL_SIGN_0);// 资金池标识 0-非资金池；1-资金池
            map.put("interestSign", fcAcc.getInterestSign());//计息标志
        }
        return map;
    }
    /**
     * 保存或修改个别计息
     *
     * @param entityVo       个别计息对象
     * @param interestResult 计息结果对象
     * @param currentUser    当前登录人
     * @return
     */
    public String saveOrUpdateFcInterestSingleEntity(FcInterestSingleVO entityVo, InterestResult interestResult,
                                                     ICurrentUser currentUser, OperateType opType) {
        if (interestResult == null || interestResult.getAccInterestInfo() == null || CollectionUtils.isEmpty(interestResult.getAccInterestInfo().getInterestDtls())) {
            logger.debug("本计息周期内没有可以计息的数据，不能计息!");
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_INTERESTNODATA);//不满足记账凭证条件
        }
        Date startDate = entityVo.getStartDate();
        Date endDate = entityVo.getEndDate();
        Date interestTime = DateUtils.getDate();
        BigDecimal interest = new BigDecimal(0.00);
        List<FcInterestDtlTemp> list = interestResult.getAccInterestInfo().getInterestDtls();
        List<Date> startDateList = new ArrayList<>();
        List<Date> endDateList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(list)) {
            FcInterestDtlTemp fidt = list.get(0);
            for (FcInterestDtlTemp entity : list) {
                interest = interest.add(entity.getInterest());
                startDateList.add(entity.getInterestStartDate());
                endDateList.add(entity.getBlnInstDate());
                interestTime = entity.getInterestTime();
            }
            startDate = Collections.min(startDateList);
            endDate = Collections.max(endDateList);
        } else {
            startDate = null;//无利息开始日
        }
        FcInterestSingle fis = new FcInterestSingle();
        if (EmptyUtils.isNotEmpty(entityVo.getId())) {
            fis = get(entityVo.getId());
        }
        fis.setCorpId(entityVo.getCorpId());
        fis.setInterestTime(interestTime);
        fis.setAccount(entityVo.getAccount());
        fis.setAccType(entityVo.getAccType());
        fis.setStartDate(startDate);
        fis.setEndDate(endDate);
        fis.setMoney(entityVo.getMoney());
        fis.setInterest(interest);
        fis.setTallyDate(entityVo.getTallyDate());
        fis.setOrderNo(entityVo.getOrderNo());
        fis.setBusGlobalId(entityVo.getBusGlobalId());
        fis.setStatus(entityVo.getStatus());
        fis.setAffixAmount(entityVo.getAffixAmount());
        switch (entityVo.getPageFlag()) {
            case 0:
                fis.setCreateBy(currentUser.getId());
                fis.setCreateDate(new Date());
                break;
            case 1:
                fis.setUpdateBy(currentUser.getId());
                fis.setUpdateDate(new Date());
                break;
            case 2:
                fis.setCheckerCode(currentUser.getId());
                fis.setCheckDate(new Date());
                break;
            case 3:
                break;
            default:
                break;
        }
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(fis.getAccount());
        fis.setCurId(fcAccDTO.getCurId());
        fis.setNetId(fcAccDTO.getNetId());
        fis.setPoolSign(fcAccDTO.getPoolSign());
        fis.setAccName(fcAccDTO.getAccName());
        saveOrUpdate(fis);
        getEntityDao().flush();
        String singleId = fis.getId();
        if (EmptyUtils.isNotEmpty(list)) {
            for (FcInterestDtlTemp entity : list) {
                entity.setSingleId(fis.getId());
                fcInterestService.updateInterestSingle(entity.getId(), singleId);
            }
        }
        entityVo.setId(singleId);
        return singleId;
    }
    /**
     * 将FcAccDTO集合cope到AccountDTO集合
     *
     * @param listFcAcc 内部账户集合
     * @return
     */
    public List<AccountDTO> copyFcAccDTOList(List<com.bytter.system.api.dto.FcAccDTO> listFcAcc) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        for (com.bytter.system.api.dto.FcAccDTO fcAcc : listFcAcc) {
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
     * 查询内部账户
     *
     * @param currentUser 当前登录人
     * @param accType     账户类别
     * @return
     */
    public List<AccountDTO> getInterestAccount(ICurrentUser currentUser, String accType, String resource) {
        List<AccountDTO> list = new ArrayList<AccountDTO>();
        // 获取资源下的单位ID
        List<String> userCorpIds = this.corpService.getUserCorpIds(resource, currentUser.getId());
        StringBuilder joinSql = new StringBuilder();
        joinSql.append(" and t.pool_sign = '0' ");
        if (EmptyUtils.isNotEmpty(accType)) {
            joinSql.append(" and t.acc_type= ").append(accType).append(" ");
        }
        List<String> accStates = new ArrayList<>();
        accStates.add(InnerAccountConstant.ACC_STATE_NORMAL);
        List<com.bytter.system.api.dto.FcAccDTO> listFcAcc = fcInterestSingleDdaDao.getFcAccByCorpIdList(userCorpIds, accStates, joinSql.toString());
        if (!EmptyUtils.isEmptyList(listFcAcc)) {
            list = copyFcAccDTOList(listFcAcc);
        }
        return list;
    }
    /**
     * 更新个别利息表
     *
     * @param ids
     * @param currentUser
     * @param status
     */
    public void updateFcInterestSingleById(List<String> ids, ICurrentUser currentUser, Integer status) {
        fcInterestSingleDdaDao.updateFcInterestSingleById(ids, currentUser.getId(), status);
    }
    /**
     * <br>
     * <b>功能：</b> 个别计息定期 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public ConstantBean<String> getSysRaceCode() {
        return FinaBookConstant.SysRaceCode.IST_INTEREST_FIX;
    }
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.IST_INTEREST_FIX.get();
    }
    public Map<String, Object> createInterestVoucher(FcInterestSingleVO entity, String opType, ICurrentUser user) {
        entity = getEntityById(entity.getId());
        Map<String, Object> result = new HashMap<String, Object>();
        List<FcFinacialAccbookVO> accbook = new ArrayList<FcFinacialAccbookVO>();
        FcFinacialAccbookVO accbookVO = new FcFinacialAccbookVO();
        accbook.add(accbookVO);
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, opType, user);
        if (null == voucherDTO) {
            return result;
        }
        FcAccbookDTO book = voucherDTO.getAccbook();
        if (null == book.getBusRaceName()) {
            FcBusRaceDTO raceDTO = iFcBusRaceService.findFcBusRaceById(book.getBusRaceId());
            if (null != raceDTO) {
                book.setBusRaceName(raceDTO.getRaceName());
            }
        }
        BeanUtils.copyProperties(book, accbookVO);
        List<FcFinacialAccbookDtlVO> fcAccbookDtls = new ArrayList<>();
        for (FcAccbookDtlDTO dtlDTO : voucherDTO.getBookDtls()) {
            FcFinacialAccbookDtlVO dtlVO = new FcFinacialAccbookDtlVO();
            BeanUtils.copyProperties(dtlDTO, dtlVO);
            if (null == dtlDTO.getAccDTO()) {
                // todo:数据转换
                com.bytter.system.api.dto.FcAccDTO accDTOByAccount = iFcAccService.getAccDTOByAccount(dtlDTO.getAccount());
                FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(accDTOByAccount, FcAccDTO.class);
                dtlDTO.setAccDTO(fcAccDTO);
            }
            dtlVO.setFcAcc(dtlDTO.getAccDTO());
            fcAccbookDtls.add(dtlVO);
        }
        accbookVO.setFcAccbookDtls(fcAccbookDtls);
        BigDecimal accbookTot = BigDecimal.ZERO;
        BigDecimal accbookCreditTot = BigDecimal.ZERO;
        for (FcFinacialAccbookVO fcFinacialAccbook : accbook) {
            accbookTot = BigDecimal.ZERO;
            accbookCreditTot = BigDecimal.ZERO;
            fcFinacialAccbook.setTallyDate(entity.getTallyDate());
            fcFinacialAccbook.setInterestStartDate(entity.getTallyDate());
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
        FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(accbook.get(0).getVoucherType());
        if (ObjectUtils.isNotEmpty(dto)){
            result.put("voucherTypeName", dto.getVoucherName());
        }else {
            logger.warn("记账规则管理-具体数据的凭证种类没有设置");
        }
        return result;
    }
    /**
     * <br>
     * <b>功能：</b> 构建计息凭证传输对象 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public FcAccbookVoucherDTO createFcAccbook(FcInterestSingleVO entity, String opType, ICurrentUser user) {
        FcAccbookVoucherDTO accbook = new FcAccbookVoucherDTO();
        FcAccbookDTO book = new FcAccbookDTO();
        List<FcAccbookDtlDTO> bookDtls = null;
        if (EmptyUtils.isEmpty(entity)) {
            return null;
        }
        FcInterestSingle single = get(entity.getId());
        Date endDate = DateUtils.addDay(single.getEndDate(), 1);
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal accruedMoney = BigDecimal.ZERO;//冲预提金额
        if (!EmptyUtils.isEmpty(entity) && !NbconsoleConstant.OpType.CHECK.get().equals(opType)) {
            String orderNo = "";
            if (EmptyUtils.isNotEmpty(entity.getId())) {
                orderNo = entity.getOrderNo();
            }
            FcOnlineLedgerDTO onlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
            BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
            if (checkBalance.compareTo(entity.getMoney()) > 0) {//部分取款
                if(null != entity.getInterest() && entity.getInterest().compareTo(BigDecimal.ZERO)>0) {
                    interest=entity.getInterest();
                }else{
                    interest = interestService.calTellerMoneyInterest(entity.getAccount(), entity.getMoney(), orderNo, endDate, user);
                }
                accruedMoney = interestService.getRushPreDrawMoney(entity.getAccount(), entity.getMoney(), entity.getId());//冲预提金额
            } else {//全部取款
                interest = interestService.calTellerMoneyInterest(entity.getAccount(), null, orderNo, endDate, user);
                accruedMoney = interestService.getRushPreDrawMoney(entity.getAccount(), null, entity.getId());//冲预提金额
            }
            entity.setInterest(interest);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", getSysRaceCode().get());//个别计息定期
        FcBusinessRuleDefineDTO ruleDefine = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        if (EmptyUtils.isNotEmpty(opType) && "initList".equals(opType)
                && !InterestConstant.INTEREST_BACK.equals(entity.getStatus())) {//记账
            accbook.setNetCode(innerAccountService.getByBlnctrAcc(entity.getAccount()).getNetId());
            bookDtls = getAccbookDtls(entity, ruleDefine, accruedMoney, user.getId());
            accbook.setBookDtls(bookDtls);
            book.setBusOriType(getOriType());
            String raceId = iFcBusRaceService.getNetBusRace(getSysRaceCode().get(), accbook.getNetCode(), user.getId());
            book.setBusRaceId(raceId);
            book.setBusRaceName(getSysRaceCode().getName());
            book.setTallyDate(entity.getTallyDate());
            book.setInterestStartDate(entity.getTallyDate());
            book.setVoucherType(ruleDefine.getVoucherType());
            book.setAffixAmount(entity.getAffixAmount());//附件张数
            book.setEmergencySign(FinaBookConstant.EmergencySign.EMERGENCY_SIGN_GENERAL.get());
            accbook.setAccbook(book);
        } else {
            String BusGlobalId = entity.getBusGlobalId();
            //if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
            accbook = iFcAccbookService.getVoucher(getOriType(), BusGlobalId).getBookVoucher();
            if (DateUtils.dayDiffs(entity.getTallyDate(), accbook.getAccbook().getTallyDate()) != 0) {
                bookDtls = getAccbookDtls(entity, ruleDefine, accruedMoney, user.getId());
                accbook.setBookDtls(bookDtls);
            }
            //}
        }
        return accbook;
    }
    /**
     * <br>
     * <b>功能：</b> 根据参数获取利息类科目账户 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/27 0027 <br>
     */
    public FcAccDTO queryCorpSubjectAcc(String subjectId, String netId, String corpId, String curId, String userId) {
        FcSubjectDTO subjectDTO = fcSubjectService.findSubjectById(subjectId);
        if (null == subjectDTO) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SUBJECT_NULL);
        }
        List<FcAccDTO> list = fcAccbookService.getInterestAcc(curId, netId, corpId,
                subjectId, "", "", userId, subjectDTO.getName());
        FcAccDTO dto = list.get(0);
        return dto;
    }
    /**
     * <br>
     * <b>功能：</b> 查询单位企业活期账号 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/27 0027 <br>
     */
    public FcAccDTO queryCorpFcAcc(String subjectId, String netId, String corpId, String curId){
        FcSubjectDTO subjectDTO = fcSubjectService.findSubjectById(subjectId);
        if (null == subjectDTO) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SUBJECT_NULL);
        }
        List<com.bytter.system.api.dto.FcAccDTO> dtos = null;
        Set<String> corpIds = new HashSet<>();
        corpIds.add(corpId);
        String msgCorp = "";
        dtos = iFcAccService.getFcAccBySysSubjectIdFilterBankCode(subjectId,null, netId, corpIds, curId) ;
        if(dtos.size() < 1) {
            msgCorp = corpService.get(netId).getName();
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ACCOUNT_ISNULL, msgCorp, subjectDTO.getName());
        }
        com.bytter.system.api.dto.FcAccDTO dto = dtos.get(0);
        // todo:数据转换
        return BeanUtils.copyNewInstanceProperties(dto,FcAccDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 生成账务明细 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/2/4 0004 <br>
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcInterestSingleVO entity, FcBusinessRuleDefineDTO ruleDefine, BigDecimal accruedMoney, String userId) {
        FcAccbookDtlDTO accbookDtl = null;
        String account = entity.getAccount();//计息账号
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(account);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        BigDecimal rushMoney = BigDecimal.ZERO;
        //摘要
        String accTypeRmk = multiLangServiceI.getLang("table.fcacc.accType05");
        String interestRmk = multiLangServiceI.getLang("finacial.interest.single");
        String rushRmk = multiLangServiceI.getLang("table.interet.predrawRush");
        for (FcVoucherEntryDefineDTO define : ruleDefine.getVouchers()) {
            /**
             *  借：利息支出-企业定期存款      利息
             *      企业定期存款              其他
             *   贷：企业活期存款             其他
             * 借:利息支出-企业定期存款      冲预提
             *    贷：应付利息-企业定期存款  冲预提
             */
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            com.bytter.system.api.dto.FcAccDTO acc = null;
            if (define.getEntryType().intValue() != FinacialConstant.ENTRY_TYPE_30) {//非冲预提
                if(define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_40) {//结息 利息支出-企业定期存款
                    if (entity.getInterest().compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//利息支出
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                        accbookDtl.setBlnctrAcc(dto.getBlnctrAcc());
                    }
                    accbookDtl.setDebitMoney(entity.getInterest());
                } else {
                    if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).equals(define.getBalanceWay())) {//其他-借 企业定期存款
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAccDTO, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                        accbookDtl.setBlnctrAcc(account);
                        accbookDtl.setDebitMoney(entity.getMoney());
                    } else {//其他-贷 企业活期存款
                        //查询本单位企业活期存款账户
                        FcAccDTO dto = queryCorpFcAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId());
                        accbookDtl.setAccDTO(dto);
                        accbookDtl.setCreditMoney(entity.getInterest().add(entity.getMoney()));
                    }
                }
                accbookDtl.setBlnctrAcc(accbookDtl.getAccDTO().getBlnctrAcc());
                accbookDtl.setVoucherType(define.getVoucherType());
                accbookDtl.setSummaryContent(accTypeRmk + interestRmk + "-" + account);//计息摘要
            } else if (define.getEntryType().intValue() == FinacialConstant.ENTRY_TYPE_30) {//冲预提
                if (accruedMoney.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                if (define.getBalanceWay().intValue() == Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue()) {//借 利息收出-企业定期存款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {//利息支出
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(accruedMoney));
                } else {//贷  应付利息-企业定期存款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {//应付利息
                        acc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                        // todo:数据转换
                        FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(acc, FcAccDTO.class);
                        accbookDtl.setAccDTO(accDTO);
                    } else {
                        FcAccDTO dto = queryCorpSubjectAcc(define.getSubjectCode(), fcAccDTO.getNetId(), fcAccDTO.getCorpId(), fcAccDTO.getCurId(), userId);
                        accbookDtl.setAccDTO(dto);
                    }
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(accruedMoney));
                }
                accbookDtl.setVoucherType(define.getVoucherType());
                accbookDtl.setSummaryContent(rushRmk);
            }
            if (!EmptyUtils.isEmpty(accbookDtl.getAccDTO()) && EmptyUtils.isNotEmpty(accbookDtl.getAccDTO().getBlnctrAcc())) {
                com.bytter.system.api.dto.FcAccDTO fcAcc = innerAccountService.getByBlnctrAcc(accbookDtl.getAccDTO().getBlnctrAcc());
                if (!EmptyUtils.isEmpty(fcAcc)) {
                    // todo:数据转换
                    FcAccDTO accDTO = BeanUtils.copyNewInstanceProperties(fcAcc, FcAccDTO.class);
                    accbookDtl.setAccDTO(accDTO);
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
            }
            accbookDtl.setOrigVoucherId(entity.getId());
            accbookDtl.setRushMoney(rushMoney);
            FcVoucherCashVo fcVoucherCashVo;
//			设置现金流项目
            if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) {
                fcVoucherCashVo = fcVoucherCashService.getItemBySubject(accbookDtl.getAccDTO().getSubjectId(), null);
                if (null != fcVoucherCashVo) {
                    accbookDtl.setCashFlux(fcVoucherCashVo.getCreditItem());
                }
            } else if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 || accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) {
                fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, accbookDtl.getAccDTO().getSubjectId());
                if (null != fcVoucherCashVo) {
                    accbookDtl.setCashFlux(fcVoucherCashVo.getDebitItem());
                }
            }
            if (accbookDtl.getCreditMoney().doubleValue() != 0D || accbookDtl.getDebitMoney().doubleValue() != 0D) {
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /**
     * 个别计息与提交
     *
     * @param entity      个别计息请求参数对象
     * @param currentUser 当前登录人
     */
    public AjaxJson doInterestaccrualBySingle(FcInterestSingleVO entity, ICurrentUser currentUser) {
        AjaxJson json = new AjaxJson();
        boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(entity.getPoolSign());
        OperateType opType = null;
        if ("interest".equals(entity.getOpType())) {
            opType = OperateType.INTEREST;
            entity.setStatus(InterestConstant.INTEREST_STATUE_TEMP);
        } else if ("submit".equals(entity.getOpType())) {
            opType = OperateType.SUBMIT;
            entity.setStatus(InterestConstant.INTEREST_WAIT_CHECK);
        }
        boolean excute = true;//执行标志 true-执行 false-停止
        FcInterestSingleVO fcInterestSingle = queryFcInterestSingle(entity.getAccount());
        String itsSuccess = "";
        //界面标志pageFlag 0-新增界面 1-修改界面 默认空也是新增界面
        if (EmptyUtils.isEmpty(entity.getPageFlag())) {
            entity.setPageFlag(0);
        }
        if (entity.getPageFlag() == 0) {
            //判断是否存在个别计息数据，不存在则计息，存在则提示
            if (fcInterestSingle != null) {
                if (EmptyUtils.isNotEmpty(entity.getId())) {
                    if (entity.getId().equals(fcInterestSingle.getId())) {
                        excute = true;
                    } else {
                        excute = false;
                    }
                } else {
                    excute = false;
                }
                itsSuccess = multiLangServiceI.getLang("finacial.interest.data.exist.account");//该账户已存在个别计息数据！
            }
        }
        if (excute) {
            if (null != fcInterestSingle && EmptyUtils.isNotEmpty(fcInterestSingle.getBusGlobalId())) {
                //删除凭证
                deleteInterestVoucher(fcInterestSingle.getBusGlobalId(), currentUser);
            }
            if (opType == OperateType.SUBMIT) {
                processSubmitInterestVoucher(entity, currentUser);//生成凭证
            }
            InterestResult interestResult = fcInterestService.interestaccrualByBatch(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                    entity.getEndDate(), currentUser, opType, false, poolFlag, entity.getAccount(),
                    InterestConstant.GENERATEMODE_INTEREST_PART, entity.getMoney());
            //里面有校验 saveOrUpdateFcInterestSingleEntity
            String istId = saveOrUpdateFcInterestSingleEntity(entity, interestResult, currentUser, opType);
            if (opType == OperateType.SUBMIT) {
                //processAutoCheckSingle(entity, currentUser, OperateType.CHECK);//判断是否自动复核
            }
            if (excute && opType == OperateType.INTEREST) {
                itsSuccess = multiLangServiceI.getLang("finacial.interest.interest.success");
            } else if (excute && opType == OperateType.SUBMIT) {
                itsSuccess = multiLangServiceI.getLang("common.message.submit.success");
            }
            json.setParams(new String[]{istId});
        }
        json.setSuccess(excute);
        json.setObj(itsSuccess);
        json.setMsg(itsSuccess);
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 处理自动复核 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/29 0029 <br>
     */
    public void processAutoCheckSingle(FcInterestSingleVO entity, ICurrentUser user, OperateType opType) {
        Boolean autoCheck = fcInterestService.isBatchInstAutoCheck();//自动复核参数
        if (autoCheck) {
            checkSingleInterest(entity, opType, user, InterestConstant.INTEREST_STATUE_CHECK);
        }
    }
    /**
     * <br>
     * <b>功能：</b> 提交处理账务 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/19 0019 <br>
     */
    public void processSubmitInterestVoucher(FcInterestSingleVO entity, ICurrentUser user) {
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, "initList", user);
        //提交凭证
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.addTarget(voucherDTO);
        voucher.setUserCode(user.getId());
        voucher.setUser(user);
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);//提交凭证
//        FcAccbookDTO accbook = voucher.getTarget().get(0).getAccbook();
        entity.setOrderNo(accbook.getOrderNo());//凭证流水号
        entity.setBusGlobalId(accbook.getBusGlobalId());//全局ID
    }
    public void processCheckInterestVoucher(FcInterestSingleVO entity, ICurrentUser user, OperateType opType) {
        FcAccbookVoucherDTO voucherDTO = createFcAccbook(entity, NbconsoleConstant.OpType.CHECK.get(), user);
        //提交凭证
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.addTarget(voucherDTO);
        voucher.setUserCode(user.getId());
        voucher.setUser(user);
        voucher.setBusGlobalId(entity.getBusGlobalId());
        if (OperateType.CHECK.equals(opType)) {
            FcOnlineLedgerDTO onlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(entity.getAccount());//账户余额
            BigDecimal checkBalance = onlineLedgerDTO.getCheckBalance();//联机余额
            iFcAccbookService.checkFcAccbook(voucher);//复核凭证
            //处理冲预提
            Date interestTime = DateUtils.getDate();
            if (checkBalance.compareTo(entity.getMoney()) > 0) {//部分取款
                fcInterestService.interestRushPredraw(entity.getAccount(), user, entity.getId(), entity.getMoney(), interestTime);
            } else {
                fcInterestService.interestRushPredraw(entity.getAccount(), user, entity.getId(), null, interestTime);
            }
        } else {
            iFcAccbookService.backFcAccbook(voucher);//打回凭证
        }
    }
    /**
     * 企业定期-个别计息复核/打回
     *
     * @param fisList     待处理数据
     * @param currentUser 当前登录人
     * @return
     */
    /*public AjaxJson doProcessInterestCheck(List<FcInterestSingleVO> fisList, ICurrentUser currentUser) {
        AjaxJson json = new AjaxJson();
        if (fisList == null || fisList.size() == 0) {
            json.setSuccess(false);
            json.setMsg(multiLangServiceI.getLang("common.message.confirm.selectData"));
            return json;
        }
        Integer status = null;
        OperateType opType = null;
        if ("check".equals(fisList.get(0).getOpType())) {
            opType = OperateType.CHECK;
            status = InterestConstant.INTEREST_STATUE_CHECK;
        } else if ("back".equals(fisList.get(0).getOpType())) {
            opType = OperateType.BACK;
            status = InterestConstant.INTEREST_BACK;
        }
        List<String> ids = new ArrayList<>();
        for (FcInterestSingleVO entity : fisList) {
            ids.add(entity.getId());
            boolean poolFlag = InnerAccountConstant.POOL_SIGN_1.equals(entity.getPoolSign());
            entity = getEntityById(entity.getId());
            AccInterestBean accInterestBean = new AccInterestBean(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                    DateUtils.addDay(entity.getEndDate(), 1), currentUser, opType, entity.getAccount(), poolFlag,
                    InterestConstant.GENERATEMODE_INTEREST_PART, entity.getAffixAmount(), FinaBookConstant.BusOriType.IST_INTEREST_FIX.get());
            processCheckInterestVoucher(entity, currentUser, opType);
            fcInterestService.processSingleInterestCheck(accInterestBean);
            entity.setStatus(status);
            if (OperateType.CHECK.equals(opType)) {
                entity.setCheckDate(new Date());
                entity.setCheckerCode(currentUser.getId());
            }
            json.setSuccess(true);
        }
        if (json.isSuccess()) {
            updateFcInterestSingleById(ids, currentUser, status);
        }
        return json;
    }*/
    /**
     * <br>
     * <b>功能：</b> 个别计息复核 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/29 0029 <br>
     */
    public void checkSingleInterest(FcInterestSingleVO entity, OperateType opType, ICurrentUser currentUser, Integer status) {
        List<String> ids = new ArrayList<>();
        ids.add(entity.getId());
        boolean poolFlag = false;
        entity = getEntityById(entity.getId());
        AccInterestBean accInterestBean = new AccInterestBean(entity.getNetId(), entity.getAccType(), entity.getTallyDate(),
                DateUtils.addDay(entity.getEndDate(), 1), currentUser, opType, entity.getAccount(), poolFlag,
                InterestConstant.GENERATEMODE_INTEREST_PART, entity.getAffixAmount(), FinaBookConstant.BusOriType.IST_INTEREST_FIX.get());
        processCheckInterestVoucher(entity, currentUser, opType);
        fcInterestService.processSingleInterestCheck(accInterestBean);
        entity.setStatus(status);
        if (OperateType.CHECK.equals(opType)) {
            entity.setCheckDate(new Date());
            entity.setCheckerCode(currentUser.getId());
        }
        updateFcInterestSingleById(ids, currentUser, status);
    }
    /**
     * 删除个别计息
     *
     * @param id          主键
     * @param account     账户
     * @param netCode     网点ID
     * @param accTypeCode 账户类型
     * @param user        当前登录人
     * @return
     */
    public AjaxJson deleteInterestSingle(String id, String account, String netCode, String accTypeCode, ICurrentUser user) throws Exception {
        AjaxJson json = new AjaxJson();
        //fcInterestSingleDdaDao.delete(id);
        FcInterestSingle entity = get(id);
        if (StringUtils.isNotEmpty(accTypeCode) && accTypeCode.startsWith(",")) {
            accTypeCode = accTypeCode.substring(accTypeCode.indexOf(",") + 1);
        }
        fcInterestService.doDeleteByBatch(netCode, accTypeCode, user.getId(), account, InterestConstant.GENERATEMODE_INTEREST_PART);
        //删除凭证
        deleteInterestVoucher(entity.getBusGlobalId(), user);
        fcInterestSingleDdaDao.deleteInterestSingle(entity, InterestConstant.INTEREST_STATUE_DELETE);
        json.setSuccess(true);
        return json;
    }
    /**
     * <br>
     * <b>功能：</b> 删除个别计息凭证 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/20 0020 <br>
     */
    public void deleteInterestVoucher(String busGlobalId, ICurrentUser user) {
        //删除凭证
        if (EmptyUtils.isNotEmpty(busGlobalId)) {
            VoucherWrapperDTO voucher = new VoucherWrapperDTO();
            voucher.setUserCode(user.getId());
            voucher.setUser(user);
            voucher.setBusGlobalId(busGlobalId);//全局业务ID
            iFcAccbookService.deleteVoucher(voucher);
        }
    }
}
