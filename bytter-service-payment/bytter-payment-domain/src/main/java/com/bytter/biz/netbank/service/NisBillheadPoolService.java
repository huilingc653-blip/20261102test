package com.bytter.biz.netbank.service;

import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.netbank.dao.BtAssignPayWayUserDao;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.netbank.dao.NisBillheadPoolDao;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBillheadPool;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.payment.api.dto.NisBillheadPoolDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IErpPayInfoService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
/**
 * 待付款池
 */
@Service("nisBillheadPoolService")
@Transactional
public class NisBillheadPoolService extends BaseService<NisBillheadPool, String> implements IExcelVerifyHandler<NisBillheadPoolVO>, ICorpDisableVerifyService, ITaskService {
    @Autowired
    private NisBillheadPoolDao nisBillheadPoolDao;
    @Autowired
    private NisBillheadDao nisBillheadDao;
    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IAccebillService accebillService;
    @DubboReference(check = false)
	private IAccebillPoolService accebillPoolService;
    @DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private NisAccbookService nisAccbookService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private BtAssignPayWayService btAssignPayWayService;
    @DubboReference(check = false)
	private IInputBankInfoService btBankService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
	private IAttachmentService attachmentService;
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired
    private NisBillService nisBillService;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
	private IErpPayInfoService erpPayInfoService;
    @DubboReference(check = false)
	private IBtBankService bankService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired(required = false)
    private BtAssignPayWayUserDao btAssignPayWayUserDao;
    @DubboReference(check = false)
	private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IUserService userService;
    @DubboReference(check = false)
	private IBtBankAccService bankAccService;
    @DubboReference(check = false)
	private ICAService caService;
    @DubboReference(check = false)
    private IRegionService regionService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(NisBillheadPool entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(NisBillheadPool entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(NisBillheadPool entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(NisBillheadPool entity) {
        super.saveOrUpdate(entity);
        getSession().flush();
    }
    @Override
    public BaseDao<NisBillheadPool, String> getEntityDao() {
        return nisBillheadPoolDao;
    }
    /**
     * 根据用户获取有转账权限的账号集合
     * @param user
     * @return
     */
    public HashMap<String, BtBankAccDTO> getAccountGrantMap(ICurrentUser user){
        HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
        //获取银行账号
        List<String> bankAccIdList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY, null,
                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<BtBankAccDTO> bankAccGrantList = bankAccService.getBankAccNotUnionByIdList(bankAccIdList);
        if(EmptyUtils.isNotEmpty(bankAccGrantList)) {
            for (BtBankAccDTO bankAcc : bankAccGrantList) {
                bankAccMap.put(bankAcc.getBankAcc(), bankAcc);
            }
        }
        //获取内部账号
        List<String> accIdList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<FcAccDTO> accGrantList = innerAccountService.listFcAccInfo(accIdList);
        if(EmptyUtils.isNotEmpty(accGrantList)) {
            for (FcAccDTO fcAcc : accGrantList) {
                BtBankAccDTO bankAcc = new BtBankAccDTO();
                bankAcc.setId(fcAcc.getId());
                bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                bankAcc.setAccName(fcAcc.getAccName());
                bankAcc.setCurId(fcAcc.getCurId());
                bankAcc.setCurName(fcAcc.getCurName());
                bankAcc.setBankName(fcAcc.getNetName());
                bankAcc.setBalance(fcAcc.getFcBalance());
                bankAccMap.put(fcAcc.getBlnctrAcc(), bankAcc);
            }
        }
        return bankAccMap;
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = "";
        //是否启用用户分配付款方式
        String isEnableUserAssign = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEUSERASSIGN).getValue();
        String paySql = "";
        //是否启用拆分合并
        String isMergeAndSplit = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLESPLIT).getValue();
        //如果开启了用户分配付款方式则筛选付款方式
        if (NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
            List<BtAssignPayWayUserVO> temp = btAssignPayWayUserDao.getByUserId(user.getId());
            List<String> payList = new ArrayList<>();
            for (BtAssignPayWayUserVO vo : temp) {
                payList.add(vo.getVoucherCode());
            }
            paySql = SqlUtil.getInSql(payList, "n.voucher_type_id", true);
        }
        resource = ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY;
        sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null or n.create_by='')");
        sql.append(" and n.status <> "+ NetbankConstant.STATUE_DEL);
        //如果开启了用户分配付款方式则筛选付款方式
        if (NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
            sql.append(" and " + paySql);
        }
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput out = nisBillheadPoolDao.getDataTablesUnion(dataTablesInput, sql.toString(), isMergeAndSplit);
        return this.setJoinField(out);
    }
    /**
     * 设置付款办理的关联字段
     * @param output
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output) {
        if(!EmptyUtils.isEmpty(output.getData())) {
            List<NisBillheadPoolVO> list = output.getData();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(NetbankConstant.POOL_STATUS_DICT, NetbankConstant.NETBANK_BILLFROM_DICT,
                    NetbankConstant.NETBANK_BILLTYPE_DICT,NetbankConstant.NETBANK_BILLFLAG_DICT,CommonConstant.COMMON_SF_YN_DICT);
            for (NisBillheadPoolVO vo:list) {
                if(NetbankConstant.STATUE_TEMP.equals(vo.getStatus()) || NetbankConstant.STATUE_BACK.equals(vo.getStatus())){
                    vo.setCheckboxHide(CommonConstant.NO);
                } else {
                    vo.setCheckboxHide(CommonConstant.YES);
                }
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                if (curMap.containsKey(vo.getCurId())) {
                    vo.setCurName(curMap.get(vo.getCurId()).getCurName());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
        }
        return output;
    }
    /**
     * 付款信息字典注入
     * @param vo
     * @param listDictMap
     */
    private void injectValues(NisBillheadPoolVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 票据类型
        Optional.ofNullable(vo.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> vo.setBillTypeName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(vo.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLFLAG_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> vo.setBillFlagName(dict.getName()));
        });
        // 单据状态
        Optional.ofNullable(vo.getStatus().toString()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.POOL_STATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> vo.setStatusName(dict.getName()));
        });
        // 单据来源
        Optional.ofNullable(vo.getBillFrom()).ifPresent(billFrom -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLFROM_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFrom + "")))
                    .ifPresent(dict -> vo.setBillFromName(dict.getName()));
        });
        // 是否已付款
        Optional.ofNullable(vo.getIsPayed()).ifPresent(isPayed -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isPayed)))
                    .ifPresent(dict -> vo.setIsPayedName(dict.getName()));
        });
        // 是否对私付款
        Optional.ofNullable(vo.getIsForindividual()).ifPresent(isForindividual -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isForindividual)))
                    .ifPresent(dict -> vo.setIsForindividualName(dict.getName()));
        });
    }
    /**
     * 设置记账状态
     * @param dataTablesOutput
     * @return
     */
    private DataTablesOutput setTallyStatus(DataTablesOutput dataTablesOutput) {
        if (dataTablesOutput == null) {
            return dataTablesOutput;
        }
        List<NisBillheadPoolVO> list = dataTablesOutput.getData();
        for(NisBillheadPoolVO vo:list){
            if(StringUtils.isNotBlank(vo.getTallySign()) && vo.getStatus()>= NetbankConstant.STATUE_CHECK && vo.getStatus()< NetbankConstant.STATUE_TALLY_CHECKPASS){
                if(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue().equals(vo.getTallySign())){
                    //记账复核通过
                    vo.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
                } else if(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(vo.getTallySign())){
                    //记账提交待复核
                    vo.setStatus(NetbankConstant.STATUE_TALLY_WAIT_CHECK);
                } else if(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(vo.getTallySign())){
                    //记账复核打回
                    vo.setStatus(NetbankConstant.STATUE_TALLY_BACK);
                } else if(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(vo.getTallySign())){
                    //记账暂存
                    vo.setStatus(NetbankConstant.STATUE_TALLY_TEMP);
                }
                vo.setStatusName(this.dictService.get("NisBillhead.status", vo.getStatus()));
            }
        }
        dataTablesOutput.setData(list);
        return dataTablesOutput;
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        nisBillheadPoolDao.updatePrint(id);
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadPoolVO getByIdUnion(String id) {
        NisBillheadPoolVO nisBillheadPoolVO = nisBillheadPoolDao.getByIdUnion(id);
        //获取余额
        if (nisBillheadPoolVO != null) {
            CorpDTO corpDTO = corpService.get(nisBillheadPoolVO.getCorpId());
            nisBillheadPoolVO.setCorpName(corpDTO == null ? "" : corpDTO.getName());
            if (!StringUtil.isBlank(nisBillheadPoolVO.getProv())) {
                RegionDTO prov = regionService.getById(nisBillheadPoolVO.getProv());
                nisBillheadPoolVO.setProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(nisBillheadPoolVO.getCity())) {
                RegionDTO city = regionService.getById(nisBillheadPoolVO.getCity());
                nisBillheadPoolVO.setCityName(city == null ? "" : city.getName());
            }
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(nisBillheadPoolVO.getCurId());
            nisBillheadPoolVO.setCurName(currencyDTO == null ? "" : currencyDTO.getCurName());
            // 账户余额默认为0
            nisBillheadPoolVO.setBalance(BigDecimal.ZERO);
            if (NetbankConstant.DIRECTPAYFLAG_Y.equals(nisBillheadPoolVO.getDirectPayFlag())) {
                BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisBillheadPoolVO.getPayerAcc(), nisBillheadPoolVO.getCurId());
                if (bal != null) {
                    nisBillheadPoolVO.setBalance(bal.getBal());
                }
            } else {
                FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(nisBillheadPoolVO.getPayerAcc());
                if (bal != null) {
                    //内部账号余额 = 可用余额 - 冻结金额
                    nisBillheadPoolVO.setBalance(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
                }
            }
            if (nisBillheadPoolVO.getBalance() == null) {
                nisBillheadPoolVO.setBalance(BigDecimal.ZERO);
            }
            //设置付款账号的可用余额
            if(StringUtils.isBlank(nisBillheadPoolVO.getPayerAcc())){
                //账号为空的时候，账户余额默认为0
                nisBillheadPoolVO.setAvailBal(BigDecimal.ZERO);
            } else {
                //获取账户在途余额
                BigDecimal useBal = nisBillheadDao.getUseBal(nisBillheadPoolVO.getPayerAcc(), nisBillheadPoolVO.getCurId());
                BigDecimal availBal = nisBillheadPoolVO.getBalance().subtract(useBal);
                nisBillheadPoolVO.setAvailBal(availBal.add(nisBillheadPoolVO.getPayMoney()));
            }
            if (NetbankConstant.PAY_TYPE_50.equals(nisBillheadPoolVO.getSysVoucherCode())
                    || (NetbankConstant.PAY_TYPE_49.equals(nisBillheadPoolVO.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(nisBillheadPoolVO.getIsOpenBill()))) {
                //获取票据关联信息
                nisBillheadPoolVO.setBillId(nisBillService.getBillIds(nisBillheadPoolVO.getId()));
            }
        }
        return nisBillheadPoolVO;
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadPoolVO getVoById(String id) {
        return nisBillheadPoolDao.getVoById(id);
    }
    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public List<NisBillheadPoolVO> listByErpId(String erpId) {
        return nisBillheadPoolDao.listByErpId(erpId);
    }
    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public NisBillheadPoolVO getByErpId(String erpId) {
        return nisBillheadPoolDao.getByErpId(erpId);
    }
    /**
     * 根据单号获取付款单信息
     *
     * @param billNo
     * @return
     */
    public NisBillheadPoolVO getByBillNo(String billNo) {
        return nisBillheadPoolDao.getByBillNo(billNo);
    }
    /**
     * 保存
     *
     * @param nisBillheadPoolVO
     */
    public void saveNisBillheadPool(NisBillheadPoolVO nisBillheadPoolVO, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(nisBillheadPoolVO.getTimeStamp())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        NisBillheadPool entity = new NisBillheadPool();
        BeanUtils.copyNotNullProperties(nisBillheadPoolVO, entity);
        //判断是否挂证书
        if (!StringUtils.isEmpty(nisBillheadPoolVO.getCaCoding())) {
            SysCaInfoDTO caDto = new SysCaInfoDTO();
            caDto.setRandom(nisBillheadPoolVO.getRandom());
            caDto.setEncryDate(DateUtils.getDate());
            caDto.setEncrySource(nisBillheadPoolVO.getEncrySource());
            caDto.setCaCoding(nisBillheadPoolVO.getCaCoding());
            entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto));
        }
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD_POOL));
        }
        //非手工录入的单据不写创建人
        if (StringUtils.isBlank(nisBillheadPoolVO.getErpId())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        } else {
            entity.setCreateBy(null);
            entity.setCreateDate(null);
        }
        //是否已付款标志 默认否
        if (StringUtils.isEmpty(entity.getIsPayed())) {
            entity.setIsPayed(NetbankConstant.ISPAYED_0);
        }
        String innerSign = getInnerSign(nisBillheadPoolVO);
        //内部资金计划内转标识
        entity.setInnerSign(innerSign);
        nisBillheadPoolVO.setInnerSign(innerSign);
        if(entity.getStatus().equals(NetbankConstant.STATUE_CHECK)){
            entity.setSchedulingDate(DateUtils.getDate());
            nisBillheadPoolVO.setSchedulingDate(DateUtils.getDate());
        }
        super.saveEntity(entity);
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());
        if (isCheckExternalCorp(nisBillheadPoolVO) && !EmptyUtils.isEmpty(nisBillheadPoolVO.getIsExternalCorp())) {
            //保存客商信息
            this.saveIfNotExist(nisBillheadPoolVO);
        }
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        nisBillheadPoolVO.setSysVoucherCode(voucherType.getSysVoucherCode());
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
            if (!StringUtils.isBlank(nisBillheadPoolVO.getBillId())) {
                //保存票据关联信息
                nisBillService.saveOrUpdate(entity.getId(), nisBillheadPoolVO.getBillId());
                String[] abAccrecordsIds = nisBillheadPoolVO.getBillId().split(CommonConstant.COMMA_SPLIT);
                //保存前先解锁票据
                this.accebillService.batchUnLock(abAccrecordsIds, user);
                //系统结算方式为50-应收票据时且是否已付款为是时，调用新的接口
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(nisBillheadPoolVO.getIsPayed())) {
                    String billPoolIds = nisBillheadPoolVO.getBillPoolIds();
                    if (StringUtils.isNotEmpty(billPoolIds)) {
                        HashSet<String> hashSet = new HashSet<>(Arrays.asList(abAccrecordsIds));
                        String[] billPoolIdsArr = billPoolIds.split(",");
                        for (String s : billPoolIdsArr) {
                            hashSet.remove(s);
                        }
                        abAccrecordsIds = CollectionUtils.isEmpty(hashSet) ? null : (String[]) hashSet.toArray();
                    }
                    if (abAccrecordsIds != null) {
                        //应收票据批量解除占用背书申请流水
                        this.accebillService.batchUnHoldEndorse(abAccrecordsIds, user);
                        //应收票据批量占用背书申请流水
                        this.accebillService.batchHoldEndorse(abAccrecordsIds, user);
                    }
                } else {
                    //系统结算方式为50-应收票据时，锁定票据
                    //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                    this.accebillService.batchLock(abAccrecordsIds, user);
                }
            }
        }
        nisBillheadPoolVO.setId(entity.getId());
        nisBillheadPoolVO.setBillNo(entity.getBillNo());
        if(nisBillheadPoolVO.getStatus().equals(NetbankConstant.STATUE_TEMP)) {
            //写入计划执行
            this.writeBudgetProcess(nisBillheadPoolVO, user);
        }else if(nisBillheadPoolVO.getStatus().equals(NetbankConstant.STATUE_CHECK)){
            //
            String id = this.saveNisBillhead(nisBillheadPoolVO,user);
            //保存付款单id
            entity.setNisBillheadId(id);
        }
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        if (user != null) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
        // 初始化联行号信息
        this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());
    }
    /**
     * 保存付款单
     * @param nisBillheadPoolVO
     */
    private String saveNisBillhead(NisBillheadPoolVO nisBillheadPoolVO, ICurrentUser user){
        //是否启用待付款池
        String enablePendingPaymentPool = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL).getValue();
        if("0".equals(enablePendingPaymentPool)){
            throw new ParameterException(NetbankMessageEnum.COMMON_MESSAGE_PENDINGPAYMENTPOOL_NOT);
        }
        NisBillheadVO nisBillheadVO = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillheadPoolVO,nisBillheadVO);
        nisBillheadVO.setStatus(NetbankConstant.STATUE_TEMP);
        nisBillheadVO.setId("");
        nisBillheadVO.setNisBillheadPoolId(nisBillheadPoolVO.getId());
        nisBillheadVO.setSplitId("");
        nisBillheadVO.setMergeId("");
        nisBillheadVO.setOrigSplitId("");
        nisBillheadVO.setErpId("pool");
        //推送付款先删除计划
//        fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillheadPoolVO.getBillNo());
        String id = nisBillheadService.saveNisBillhead(nisBillheadVO,user);
        return id;
    }
    /**
     * 写入计划执行
     *
     * @param nisBillhead
     */
    private void writeBudgetProcess(NisBillheadPoolVO nisBillhead, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(nisBillhead.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < nisBillhead.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(nisBillhead, user);
                fbsBudgetProcessDTO.setItemCode(nisBillhead.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(nisBillhead.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(nisBillhead.getProcessMoney()[i]);
                if (nisBillhead.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(nisBillhead.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(nisBillhead.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //计划执行状态同步更新（批量提交）
            // 应付票据状态为暂存，过滤应付票据
            if (!NetbankConstant.PAY_TYPE_49.equalsIgnoreCase(nisBillhead.getSysVoucherCode())) {
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                        nisBillhead.getBillNo(), nisBillhead.getStatus(), user);
            }
        }
    }
    /**
     * 构建计划执行
     *
     * @param NisBillheadPool
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(NisBillheadPoolVO NisBillheadPool, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        //获取系统结算方式
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(NisBillheadPool.getVoucherTypeId());
        // 应付票据挂接时，状态为暂存
        if (NetbankConstant.PAY_TYPE_49.equalsIgnoreCase(voucherType.getSysVoucherCode())) {
            fbsBudgetProcessDTO.setStatus(NetbankConstant.STATUE_TEMP);
        } else {
            fbsBudgetProcessDTO.setStatus(NisBillheadPool.getStatus());
        }
        fbsBudgetProcessDTO.setBillNo(NisBillheadPool.getBillNo());
        fbsBudgetProcessDTO.setBillId(NisBillheadPool.getId());
        fbsBudgetProcessDTO.setProcessDate(NisBillheadPool.getWishPayday());
        fbsBudgetProcessDTO.setCorpId(NisBillheadPool.getCorpId());
        fbsBudgetProcessDTO.setCurId(NisBillheadPool.getCurId());
        fbsBudgetProcessDTO.setSettlType(voucherType.getSettlType());
        fbsBudgetProcessDTO.setPayerAcc(NisBillheadPool.getPayerAcc());
        fbsBudgetProcessDTO.setPayeeAcc(NisBillheadPool.getPayeeAcc());
        fbsBudgetProcessDTO.setPayeeAccName(NisBillheadPool.getPayeeAccName());
        fbsBudgetProcessDTO.setPayeeBank(NisBillheadPool.getPayeeBank());
        fbsBudgetProcessDTO.setPurpose(NisBillheadPool.getPurpose());
        fbsBudgetProcessDTO.setWishPayday(NisBillheadPool.getWishPayday());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        fbsBudgetProcessDTO.setInnerSign(getInnerSign(NisBillheadPool));
        return fbsBudgetProcessDTO;
    }
    /**
     * ERP接口防篡改校验
     *
     * @param billVO 前台传入的单据信息
     * @param billDB 数据库原始单据信息
     */
    private String checkErpData(NisBillheadPoolVO billVO, NisBillheadPool billDB) {
        if (billVO.getPayerAcc() == null) {
            billVO.setPayerAcc("");
        }
        if (billVO.getPayerAccName() == null) {
            billVO.setPayerAccName("");
        }
        if (billVO.getCurId() == null) {
            billVO.setCurId("");
        }
        if (billVO.getPayeeAcc() == null) {
            billVO.setPayeeAcc("");
        }
        if (billVO.getPayeeAccName() == null) {
            billVO.setPayeeAccName("");
        }
        if (StringUtils.isBlank(billVO.getPayeeBank())) {
            billVO.setPayeeBank("");
        }
        if (billVO.getWishPayday() == null) {
            billVO.setWishPayday(DateUtils.getDateYMD());
        }
        //来源于ERP的比对关键信息是否被篡改
        if (NetbankConstant.BILLFROM_6.equals(billDB.getBillFrom())) {
            String voucherTypeId = configService.getConfig(ConfigConstant.ERP, "voucherTypeId").getValue();
            String payerAcc = configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue();
            String payMoney = configService.getConfig(ConfigConstant.ERP, "payMoney").getValue();
            String payeeAcc = configService.getConfig(ConfigConstant.ERP, "payeeAcc").getValue();
            String payeeAccName = configService.getConfig(ConfigConstant.ERP, "payeeAccName").getValue();
            String payeeBank = configService.getConfig(ConfigConstant.ERP, "payeeBank").getValue();
            String purpose = configService.getConfig(ConfigConstant.ERP, "purpose").getValue();
            String isForindividual = configService.getConfig(ConfigConstant.ERP, "isForindividual").getValue();
            String billType = configService.getConfig(ConfigConstant.ERP, "billType").getValue();
            String billFlag = configService.getConfig(ConfigConstant.ERP, "billFlag").getValue();
            //付款方式不允许调整
            if (CommonConstant.NO.equals(voucherTypeId) && !billDB.getVoucherTypeId().equals(billVO.getVoucherTypeId())) {
                FcVoucherTypeVO fcVoucherTypeDB = fcVoucherTypeService.getByIdUnion(billDB.getVoucherTypeId());
                FcVoucherTypeVO fcVoucherTypeVO = fcVoucherTypeService.getByIdUnion(billVO.getVoucherTypeId());
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_CHANGE, fcVoucherTypeDB.getVoucherName(), fcVoucherTypeVO.getVoucherName());
            }
            //付款账号不允许调整
            if (CommonConstant.NO.equals(payerAcc)) {
                //账号不相同或者账号相同币种不相同，考虑账号多币种的情况
                if (!billVO.getPayerAcc().equals(billDB.getPayerAcc()) || !billVO.getCurId().equals(billDB.getCurId())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_CHANGE, billDB.getPayerAcc(), billVO.getPayerAcc());
                }
            }
            //付款金额不允许调整
            if (CommonConstant.NO.equals(payMoney) && billDB.getPayMoney().subtract(billVO.getPayMoney()).intValue() != 0) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYMONEY_CHANGE,
                        NumberUtil.formatNumeric(billDB.getPayMoney(), 2), NumberUtil.formatNumeric(billVO.getPayMoney(), 2));
            }
            //收款账号不允许调整
            if (CommonConstant.NO.equals(payeeAcc) && !billVO.getPayeeAcc().equals(billDB.getPayeeAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_CHANGE, billDB.getPayeeAcc(), billVO.getPayeeAcc());
            }
            //收款户名不允许调整
            if (CommonConstant.NO.equals(payeeAccName) && !billVO.getPayeeAccName().equals(billDB.getPayeeAccName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACCNAME_CHANGE, billDB.getPayeeAccName(), billVO.getPayeeAccName());
            }
            //收款银行不允许调整
            if (CommonConstant.NO.equals(payeeBank) && !billVO.getPayeeBank().equals(billDB.getPayeeBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEBANK_CHANGE, billDB.getPayeeBank(), billVO.getPayeeBank());
            }
            //用途不允许调整
            if (CommonConstant.NO.equals(purpose) && !billVO.getPurpose().equals(billDB.getPurpose())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PURPOSE_CHANGE, billDB.getPurpose(), billVO.getPurpose());
            }
            //对私标志不允许调整
            if (CommonConstant.NO.equals(isForindividual) && StringUtils.isNotBlank(billVO.getIsForindividual())
                    && !billDB.getIsForindividual().equals(billVO.getIsForindividual())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_ISFORINDIVIDUAL_CHANGE,
                        dictService.get("sf_yn", billDB.getIsForindividual()),
                        dictService.get("sf_yn", billVO.getIsForindividual()));
            }
            //票据类型不允许调整
            if (CommonConstant.NO.equals(billType) && billDB.getBillType() != billVO.getBillType()) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILLTYPE_CHANGE,
                        billDB.getBillType() == null ? "空" : dictService.get("AB_BILL_TYPE", billDB.getBillType()),
                        billVO.getBillType() == null ? "空" : dictService.get("AB_BILL_TYPE", billVO.getBillType()));
            }
            //票据流通介质不允许调整
            if (CommonConstant.NO.equals(billFlag) && billDB.getBillFlag() != billVO.getBillFlag()) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILLFLAG_CHANGE,
                        billDB.getBillFlag() == null ? "空" : dictService.get("AB_BILL_FLAG", billDB.getBillFlag()),
                        billVO.getBillFlag() == null ? "空" : dictService.get("AB_BILL_FLAG", billVO.getBillFlag()));
            }
        }
        StringBuilder updateMsg = new StringBuilder();
        //付款方式调整
        if (!billDB.getVoucherTypeId().equals(billVO.getVoucherTypeId())) {
            FcVoucherTypeVO fcVoucherTypeDB = StringUtils.isBlank(billDB.getVoucherTypeId()) ? null : fcVoucherTypeService.getByIdUnion(billDB.getVoucherTypeId());
            FcVoucherTypeVO fcVoucherTypeVO = StringUtils.isBlank(billVO.getVoucherTypeId()) ? null : fcVoucherTypeService.getByIdUnion(billVO.getVoucherTypeId());
            updateMsg.append("付款方式：" + (fcVoucherTypeDB == null ? "空" : fcVoucherTypeDB.getVoucherName()) + "更新为" + (fcVoucherTypeVO == null ? "空" : fcVoucherTypeVO.getVoucherName()) + "；");
        }
        //付款账号调整
        if (!billVO.getPayerAcc().equals(billDB.getPayerAcc())) {
            updateMsg.append("付款账号：" + (StringUtils.isBlank(billDB.getPayerAcc()) ? "空" : billDB.getPayerAcc()) + "更新为" + billVO.getPayerAcc() + "；");
        }
        //币种
        if (!billVO.getCurId().equals(billDB.getCurId())) {
            BtCurrencyDTO curDB = StringUtils.isBlank(billDB.getCurId()) ? null : btCurrencyService.getObjectById(billDB.getCurId());
            BtCurrencyDTO curVO = StringUtils.isBlank(billVO.getCurId()) ? null : btCurrencyService.getObjectById(billVO.getCurId());
            updateMsg.append("币种：" + (curDB == null ? "空" : curDB.getCurName()) + "更新为" + (curVO == null ? "空" : curVO.getCurName()) + "；");
        }
        //付款金额调整
        if (billDB.getPayMoney().subtract(billVO.getPayMoney()).intValue() != 0) {
            updateMsg.append("金额：" + NumberUtil.formatNumeric(billDB.getPayMoney(), 2) + "更新为" + NumberUtil.formatNumeric(billVO.getPayMoney(), 2) + "；");
        }
        //收款账号调整
        if (!billVO.getPayeeAcc().equals(billDB.getPayeeAcc())) {
            updateMsg.append("收款账号：" + (StringUtils.isBlank(billDB.getPayeeAcc()) ? "空" : billDB.getPayeeAcc()) + "更新为" + billVO.getPayeeAcc() + "；");
        }
        //收款户名调整
        if (!billVO.getPayeeAccName().equals(billDB.getPayeeAccName())) {
            updateMsg.append("收款账号名称：" + (StringUtils.isBlank(billDB.getPayeeAccName()) ? "空" : billDB.getPayeeAccName()) + "更新为" + billVO.getPayeeAccName() + "；");
        }
        //收款银行调整
        if (!billVO.getPayeeBank().equals(billDB.getPayeeBank())) {
            updateMsg.append("收款银行：" + (StringUtils.isBlank(billDB.getPayeeBank()) ? "空" : billDB.getPayeeBank()) + "更新为" + billVO.getPayeeBank() + "；");
        }
        //用途调整
        if (!billVO.getPurpose().equals(billDB.getPurpose())) {
            updateMsg.append("用途：" + billDB.getPurpose() + "更新为" + billVO.getPurpose() + "；");
        }
        //对私付款标识
        if (StringUtils.isNotBlank(billVO.getIsForindividual()) && !billVO.getIsForindividual().equals(billDB.getIsForindividual())) {
            updateMsg.append("对私付款标识：" + (StringUtils.isBlank(billDB.getIsForindividual()) ? "空" : dictService.get("sf_yn", billDB.getIsForindividual())) + "更新为" + dictService.get("sf_yn", billVO.getIsForindividual()) + "；");
        }
        //期望支付日期
        if (!DateUtils.format(billVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE).equals(DateUtils.format(billDB.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE))) {
            updateMsg.append("期望支付日期：" + DateUtils.format(billDB.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + "更新为" + DateUtils.format(billVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + "；");
        }
        //是否加急
        if (!billVO.getIsQuick().equals(billDB.getIsQuick())) {
            updateMsg.append("是否加急：" + (StringUtils.isBlank(billDB.getIsQuick()) ? "空" : dictService.get("sf_yn", billDB.getIsQuick())) + "更新为" + dictService.get("sf_yn", billVO.getIsQuick()) + "；");
        }
        //是否已付款
        if (StringUtils.isBlank(billVO.getIsPayed())) {
            billVO.setIsPayed(CommonConstant.NO);
        }
        if (!billVO.getIsPayed().equals(billDB.getIsPayed())) {
            updateMsg.append("是否已付款：" + dictService.get("sf_yn", billDB.getIsPayed()) + "更新为" + dictService.get("sf_yn", billVO.getIsPayed()) + "；");
        }
        return updateMsg.toString();
    }
    /**
     * 编辑付款单
     *
     * @param nisBillheadPoolVO
     */
    public void updateNisBillheadPool(NisBillheadPoolVO nisBillheadPoolVO, ICurrentUser user, Boolean isBatch) {
        NisBillheadPool entity = null;
        String updateMsg = "";
        if (isBatch) {
            entity = new NisBillheadPool();
        } else {
            entity = nisBillheadPoolDao.get(nisBillheadPoolVO.getId(), true);
            //ERP单据校验单据信息
            updateMsg = this.checkErpData(nisBillheadPoolVO, entity);
            //退款勾兑打回后，重新提交设置银行流水号为空
            nisBillheadPoolVO.setBankSerialNo("");
            //获取票据ID
            String billIds = nisBillService.getBillIds(entity.getId());
            //判断更新之前是否是50-应收票据时，解锁票据
            if (!StringUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                this.accebillService.batchUnLock(abAccrecordsIds, user);
            }
        }
        if (EmptyUtils.isBlank(nisBillheadPoolVO.getCreateBy())) {
            nisBillheadPoolVO.setCreateBy(user.getLoginName());
        }
        if (nisBillheadPoolVO.getCreateDate() == null) {
            nisBillheadPoolVO.setCreateDate(DateUtils.getDate());
        }
        nisBillheadPoolVO.setUpdateBy(user.getLoginName());
        nisBillheadPoolVO.setUpdateDate(DateUtils.getDate());
        //内部资金计划内转标识
        String innerSign = getInnerSign(nisBillheadPoolVO);
        nisBillheadPoolVO.setInnerSign(innerSign);
        //针对ERP接口推送的付款单可能存在Acc_id为空的情况，由资金系统补全
        if (StringUtils.isEmpty(nisBillheadPoolVO.getAccId())) {
            BtBankAccDTO payerAcc = btBankAccService.getBankAccByBankAccAndCurId(nisBillheadPoolVO.getPayerAcc(), nisBillheadPoolVO.getCurId());
            if (payerAcc != null) {
                nisBillheadPoolVO.setAccId(payerAcc.getId());
            }
        }
        if (!isBatch) {
            // 初始化联行号信息
            this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());
        }
        if (isCheckExternalCorp(nisBillheadPoolVO) && !EmptyUtils.isEmpty(nisBillheadPoolVO.getIsExternalCorp())) {
            //保存客商信息
            this.saveIfNotExist(nisBillheadPoolVO);
        }
        if(EmptyUtils.isEmpty(nisBillheadPoolVO.getSchedulingDate())) {
            nisBillheadPoolVO.setSchedulingDate(DateUtils.getDate());
        }
        //95状态提交到付款单
        if(nisBillheadPoolVO.getStatus().equals(NetbankConstant.STATUE_CHECK)){
            String billId = this.saveNisBillhead(nisBillheadPoolVO,user);
            nisBillheadPoolVO.setNisBillheadId(billId);
        }
        //编辑付款单，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateNisBillheadPoolStatus(nisBillheadPoolVO, oldStatus);
        BeanUtils.copyNotNullProperties(nisBillheadPoolVO, entity);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        nisBillheadPoolVO.setSysVoucherCode(voucherType.getSysVoucherCode());
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
            if (!StringUtils.isBlank(nisBillheadPoolVO.getBillId())) {
                //保存票据关联信息
                nisBillService.saveOrUpdate(entity.getId(), nisBillheadPoolVO.getBillId());
                //系统结算方式为50-应收票据时，锁定票据
                //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                String[] abAccrecordsIds = nisBillheadPoolVO.getBillId().split(CommonConstant.COMMA_SPLIT);
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                        || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
                    //如果是批量提交就不再锁定应收票据
                    if (!isBatch) {
                        //保存前先解锁票据
                        this.accebillService.batchUnLock(abAccrecordsIds, user);
                        //系统结算方式为50-应收票据时且是否已付款为是时，调用新的接口
                        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(nisBillheadPoolVO.getIsPayed())) {
                            String billPoolIds = nisBillheadPoolVO.getBillPoolIds();
                            if (StringUtils.isNotEmpty(billPoolIds)) {
                                HashSet<String> hashSet = new HashSet<>(Arrays.asList(abAccrecordsIds));
                                String[] billPoolIdsArr = billPoolIds.split(",");
                                for (String s : billPoolIdsArr) {
                                    hashSet.remove(s);
                                }
                                abAccrecordsIds = CollectionUtils.isEmpty(hashSet) ? null : (String[]) hashSet.toArray();
                            }
                            if (abAccrecordsIds != null) {
                                //应收票据批量解除占用背书申请流水
                                this.accebillService.batchUnHoldEndorse(abAccrecordsIds, user);
                                //应收票据批量占用背书申请流水
                                this.accebillService.batchHoldEndorse(abAccrecordsIds, user);
                            }
                        } else {
                            //系统结算方式为50-应收票据时，锁定票据
                            //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                            this.accebillService.batchLock(abAccrecordsIds, user);
                        }
                        // 新增票据付款中间表信息
                        String vvids = nisBillheadPoolVO.getVvid();
                        if (StringUtils.isEmpty(vvids)) {
                            throw new RuntimeException();
                        }
                    }
                }
            }
        }
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        if (StringUtils.isNotBlank(updateMsg)) {
            message = updateMsg;
        }
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
    }
    /**
     * 普通票据或票据池票据处理
     * @param NisBillheadPool 付款单信息
     * @param user 操作用户
     * @param abAccrecordsIds 流水id
     * @return 新生成的流水id
     */
    private StringBuilder endorseByNisBillheadPool(NisBillheadPool NisBillheadPool, ICurrentUser user, String[] abAccrecordsIds) {
        if(abAccrecordsIds==null||abAccrecordsIds.length==0){
            //传递的参数值不能为空
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        //解锁
        this.accebillService.batchUnLock(abAccrecordsIds, user);
        //普通票据流水id
        StringBuilder abAccIds=new StringBuilder();
        //票据池明细id
        StringBuilder poolIds=new StringBuilder();
        for(String accId : abAccrecordsIds){
            //根据id判断是否是票据池明细数据,是则选择的是票据池已入池的数据
            if(this.accebillService.isPoolDtl(accId)){
                if(poolIds.length()>0){
                    poolIds.append(CommonConstant.COMMA_SPLIT);
                }
                poolIds.append(accId);
            }else{
                if(abAccIds.length()>0){
                    abAccIds.append(CommonConstant.COMMA_SPLIT);
                }
                abAccIds.append(accId);
            }
        }
        String[] abAccIdsArr = abAccIds.toString().split(CommonConstant.COMMA_SPLIT);
        StringBuilder endorseBillIds=new StringBuilder();
        if(StringUtils.isNotEmpty(abAccIdsArr)){
            String billBalances=nisBillService.getBillBalances(abAccIdsArr, NisBillheadPool.getId());
            //普通票据背书
            endorseBillIds.append(this.accebillService.batchEndorse(abAccIdsArr, NisBillheadPool.getPayeeAccName(), NisBillheadPool.getPayeeAcc(), NisBillheadPool.getPayeeBankCode(), NisBillheadPool.getId(), billBalances, NisBillheadPool.getWishPayday(), false, user));
        }
        if (!EmptyUtils.isBlank(poolIds.toString())) {
            if(endorseBillIds.length()>0){
                endorseBillIds.append(CommonConstant.COMMA_SPLIT);
            }
            //票据池背书
            endorseBillIds.append(this.accebillPoolService.saveAbPoolEndorseInfoByNisPay(poolIds.toString(), NisBillheadPool.getPayeeAccName(), NisBillheadPool.getPayeeAcc(), NisBillheadPool.getPayeeBankCode(), NisBillheadPool.getWishPayday(), user));
        }
        return endorseBillIds;
    }
    /**
     * 获取拆分明细
     *
     * @param splitId
     * @return
     */
    public List<NisBillheadPool> listNisBillheadPoolBySplitId(String splitId) {
        return nisBillheadPoolDao.listNisBillheadBySplitId(splitId);
    }
    /**
     * 逻辑删除付款单
     *
     * @param id
     */
    public void delNisBillheadPool(String id, ICurrentUser user, boolean isBatch, String reply) {
        NisBillheadPool nisBillheadPool = this.get(id, true);
        nisBillheadPool.setReply(reply);
        this.deleteNisBillheadPool(nisBillheadPool, user, isBatch, false);
    }
    public void deleteNisBillheadPool(NisBillheadPool entity, ICurrentUser user, boolean isBatch, boolean backDetail) {
        //删除付款单，控制原状态为0-暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        NisBillheadPoolVO vo = new NisBillheadPoolVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(NetbankConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateNisBillheadPoolStatus(vo, oldStatus);
        // 不是费用报销打回失败明细
        if (!backDetail) {
            //判断是否是对私付款，对私付款需要同步删除费用报销明细
            if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
                List<NisExpenseDetailVO> listDtl = nisExpenseDetailService.listByBillId(entity.getId());
                if (!EmptyUtils.isEmptyList(listDtl)) {
                    for (NisExpenseDetailVO expense : listDtl) {
                        nisExpenseDetailService.delExpenseDetail(expense.getId(), user);
                    }
                }
            }
        }
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        //获取票据ID
        String billIds = nisBillService.getBillIds(entity.getId());
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
        if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill())))
                && !EmptyUtils.isBlank(billIds)) {
            String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
            //解锁
            this.accebillService.batchUnLock(abAccrecordsIds, user);
            //系统结算方式为50-应收票据时且是否已付款为是时，调用解除占用背书申请流水
            if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(entity.getIsPayed())) {
                //应收票据批量解除占用背书申请流水
                this.accebillService.batchUnHoldEndorse(abAccrecordsIds, user);
            }
        }
        if (backDetail) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, NetbankConstant.NETBANK_LOG_BACKDETAIL_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
        } else {
            String message = isBatch ? NisMessageConstant.NIS_LOG_BATCH_DELETE_SUCC : NisMessageConstant.NIS_LOG_DELETE_SUCC;
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }
    /**
     * 拆分保存
     *
     * @param t
     * @param payMoneys
     * @param budgets
     */
    public void split(NisBillheadPool t, List<BigDecimal> payMoneys, List<String> budgets, ICurrentUser user) {
        // 批量获取付款流水号
        List<String> listSequence = SequenceSingleton.listSequence(SequenceEnum.NIS_BILLHEAD_POOL, payMoneys.size());
        //生成拆分单据
        for (int i = 0; i < payMoneys.size(); i++) {
            NisBillheadPool nisBillheadPool = new NisBillheadPool();
            BeanUtils.copyNotNullProperties(t, nisBillheadPool);
            nisBillheadPool.setBillNo(listSequence.get(i));
            nisBillheadPool.setId(null);
            nisBillheadPool.setPayMoney(payMoneys.get(i));
            //保存拆分前单号
            nisBillheadPool.setSplitId(t.getId());
            //原始拆分前单号
            if (StringUtils.isNotBlank(t.getOrigSplitId())) {
                nisBillheadPool.setOrigSplitId(t.getOrigSplitId());
            } else {
                nisBillheadPool.setOrigSplitId(t.getId());
            }
            nisBillheadPool.setCreateBy(user.getLoginName());
            nisBillheadPool.setCreateDate(DateUtils.getDate());
            nisBillheadPool.setSchedulingElementsId(null);
            nisBillheadPool.setSchedulingDate(null);
            this.save(nisBillheadPool);
            //生成日志
            nisMessageService.saveMessage(nisBillheadPool.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_SPLIT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
        }
        FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
        if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
            //获取票据ID
            String billIds = nisBillService.getBillIds(t.getId());
            if (!EmptyUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                //解锁
                this.accebillService.batchUnLock(abAccrecordsIds, user);
            }
        }
        //更新原单状态为删除
        NisBillheadPoolVO vo = new NisBillheadPoolVO();
        BeanUtils.copyNotNullProperties(t, vo);
        vo.setStatus(NetbankConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        vo.setSchedulingElementsId(null);
        vo.setSchedulingDate(null);
        //拆分付款单，控制原状态为 0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateNisBillheadPoolStatus(vo, oldStatus);
    }
    /**
     * 撤销拆分
     *
     * @param id
     */
    public void splitCancel(String id, ICurrentUser user) {
        NisBillheadPool NisBillheadPool = this.get(id, true);
        //根据拆分单号获取拆分前单据
        com.bytter.biz.netbank.entity.NisBillheadPool old = nisBillheadPoolDao.getUniqueBy("id", NisBillheadPool.getSplitId());
        //获取拆分明细
        List<com.bytter.biz.netbank.entity.NisBillheadPool> list = nisBillheadPoolDao.listNisBillheadBySplitId(old.getId());
        //校验拆分明细单状态必须都是暂存或者打回才允许撤销拆分
        if (!EmptyUtils.isEmptyList(list)) {
            String billNos = "";
            for (com.bytter.biz.netbank.entity.NisBillheadPool entity : list) {
                if (!NetbankConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                        && !NetbankConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())) {
                    if (StringUtils.isBlank(billNos)) {
                        billNos = entity.getBillNo();
                    } else {
                        billNos = billNos + CommonConstant.COMMA_SPLIT + entity.getBillNo();
                    }
                }
            }
            if (StringUtils.isNotBlank(billNos)) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLITCANCEL_VALID, billNos);
            }
        }
        //还原拆分前单据
        NisBillheadPoolVO vo = new NisBillheadPoolVO();
        BeanUtils.copyNotNullProperties(old, vo);
        vo.setStatus(NetbankConstant.STATUE_TEMP);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        vo.setSchedulingElementsId(null);
        vo.setSchedulingDate(null);
        //撤销拆分，控制原付款单状态为-2已删除
        Integer[] oldStatus = {NetbankConstant.STATUE_DEL};
        this.updateNisBillheadPoolStatus(vo, oldStatus);
        //更新拆分明细单状态
        for (com.bytter.biz.netbank.entity.NisBillheadPool t : list) {
            NisBillheadPoolVO dtlVo = new NisBillheadPoolVO();
            BeanUtils.copyNotNullProperties(t, dtlVo);
            dtlVo.setStatus(NetbankConstant.STATUE_DEL);
            dtlVo.setSplitId(null);
            dtlVo.setUpdateBy(user.getLoginName());
            dtlVo.setUpdateDate(DateUtils.getDate());
            dtlVo.setSchedulingElementsId(null);
            dtlVo.setSchedulingDate(null);
            //撤销拆分，控制拆分明细单状态为0暂存/-1打回
            Integer[] oldStatusDtl = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateNisBillheadPoolStatus(dtlVo, oldStatusDtl);
            FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);
            // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
            if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
                //获取票据ID
                String billIds = nisBillService.getBillIds(t.getId());
                if (!EmptyUtils.isBlank(billIds)) {
                    String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                    //解锁
                    this.accebillService.batchUnLock(abAccrecordsIds, user);
                }
            }
        }
        FcVoucherType voucherType = fcVoucherTypeService.get(old.getVoucherTypeId(), true);
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，锁定票据
        if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(old.getIsOpenBill())))) {
            //获取票据ID
            String billIds = nisBillService.getBillIds(old.getId());
            if (!EmptyUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                //锁定
                this.accebillService.batchLock(abAccrecordsIds, user);
            }
        }
    }
    /**
     * 付款单合并
     *
     * @param ids
     */
    public void merge(String ids, ICurrentUser user) {
        List<NisBillheadPool> list = new ArrayList<NisBillheadPool>();
        String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
        //获取第一笔合并单据
        NisBillheadPool t1 = nisBillheadPoolDao.get(idAtr[0], true);
        //数据校验
        for (String id : idAtr) {
            NisBillheadPool t = nisBillheadPoolDao.get(id, true);
            FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);
            list.add(t);
            //非原始单拆分单据不可进行合并
            if (!StringUtils.isEmpty(t.getSplitId()) && !StringUtils.isEmpty(t1.getSplitId()) && !t.getOrigSplitId().equals(t1.getOrigSplitId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_NOT_MERGE);
            }
            //合并单据不可进行合并
            if (this.isMergeBill(t.getId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_MERGE);
            }
            //系统结算方式为50-应收票据，不可进行合并
			/*if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILL_MERGE);
			}*/
            //单据来源为承兑汇票，不可进行合并
            if (NetbankConstant.BILLFROM_3.equals(t.getBillFrom())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILlFROM_BILL_MERGE);
            }
            if (t1.getId().equals(t.getId())) {
                continue;
            }
            //付款单位一致
            if (!t1.getCorpId().equals(t.getCorpId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_SAME);
            }
            //付款方式一致
            if (!t1.getVoucherTypeId().equals(t.getVoucherTypeId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_SAME);
            }
            //如果是转付，且要记账，则需判断银行账号是否一致
            if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
                if (!t1.getDownAccId().equals(t.getDownAccId())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_DOWNACC_SAME);
                }
            }
            //付款账号一致
            if ((StringUtils.isNotEmpty(t1.getPayerAcc()) && !t1.getPayerAcc().equals(t.getPayerAcc()))
                    || (StringUtils.isNotEmpty(t.getPayerAcc()) && !t.getPayerAcc().equals(t1.getPayerAcc()))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_SAME);
            }
            //付款币别一致
            if (!t1.getCurId().equals(t.getCurId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_SAME);
            }
            //收款人一致
            if (!t1.getPayeeAccName().equals(t.getPayeeAccName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACCNAME_SAME);
            }
            //收款账号一致
            if (!t1.getPayeeAcc().equals(t.getPayeeAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_SAME);
            }
            //收款开户行一致
            if (!StringUtils.isEmpty(t1.getPayeeBank()) && !t1.getPayeeBank().equals(t.getPayeeBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEBANK_SAME);
            }
            //对私标志一致
            if (!t1.getIsForindividual().equals(t.getIsForindividual())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_FORINDIVIDUAL_SAME);
            }
            //加急标志一致
            if (!t1.getIsQuick().equals(t.getIsQuick())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_QUICK_SAME);
            }
        }
        BigDecimal payMoney = BigDecimal.ZERO;
        //合并附件
        List<String> attachmentList = new ArrayList<String>();
        //合并计划
        List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
        //合并票据
        String billIdMerge = "";
        NisBillheadPool NisBillheadPool = new NisBillheadPool();
        for (com.bytter.biz.netbank.entity.NisBillheadPool t : list) {
            BeanUtils.copyNotNullProperties(t, NisBillheadPool);
            //合计金额
            payMoney = payMoney.add(t.getPayMoney());
            if (!StringUtils.isBlank(t.getAttachmentId())) {
                attachmentList.add(t.getAttachmentId());
            }
            //票据号
            billIdMerge += nisBillService.getBillIds(t.getId()) + CommonConstant.COMMA_SPLIT;
        }
        //合并附件
        if (!EmptyUtils.isEmptyList(attachmentList)) {
            NisBillheadPool.setAttachmentId(attachmentService.mergeGroupIds(attachmentList));
        }
        NisBillheadPool.setId(null);
        NisBillheadPool.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD_POOL));
        NisBillheadPool.setSplitId(null);
        NisBillheadPool.setOrigSplitId(null);
        //汇总金额
        NisBillheadPool.setPayMoney(payMoney);
        NisBillheadPool.setStatus(NetbankConstant.STATUE_TEMP);
        NisBillheadPool.setCreateBy(user.getLoginName());
        NisBillheadPool.setCreateDate(DateUtils.getDate());
        NisBillheadPool.setSchedulingElementsId(null);
        NisBillheadPool.setSchedulingDate(null);
        //生成合并单据
        this.save(NisBillheadPool);
        if (!StringUtils.isBlank(billIdMerge)) {
            //保存票据关联信息
            nisBillService.saveOrUpdate(NisBillheadPool.getId(), billIdMerge);
        }
        //生成日志
        nisMessageService.saveMessage(NisBillheadPool.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_MERGE_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
        //合并资金计划
        if (!EmptyUtils.isEmptyList(listBudget)) {
            HashMap<String, FbsBudgetProcessDTO> mapBudget = new HashMap<String, FbsBudgetProcessDTO>();
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                if (!mapBudget.containsKey(fbsBudgetProcessDTO.getItemCode())) {
                    mapBudget.put(fbsBudgetProcessDTO.getItemCode(), fbsBudgetProcessDTO);
                } else {
                    //相同科目合计金额
                    FbsBudgetProcessDTO dto = mapBudget.get(fbsBudgetProcessDTO.getItemCode());
                    dto.setProcessMoney(dto.getProcessMoney().add(fbsBudgetProcessDTO.getProcessMoney()));
                    if (!EmptyUtils.isEmpty(dto.getProcessCurMoney()) && !EmptyUtils.isEmpty(fbsBudgetProcessDTO.getProcessCurMoney())) {
                        dto.setProcessCurMoney(dto.getProcessCurMoney().add(fbsBudgetProcessDTO.getProcessCurMoney()));
                    }
                    mapBudget.put(dto.getItemCode(), dto);
                }
                if (EmptyUtils.isEmpty(fbsBudgetProcessDTO.getProcessDate())) {
                    fbsBudgetProcessDTO.setProcessDate(NisBillheadPool.getWishPayday());
                }
                if (EmptyUtils.isEmpty(fbsBudgetProcessDTO.getWishPayday())) {
                    fbsBudgetProcessDTO.setWishPayday(NisBillheadPool.getWishPayday());
                }
            }
            listBudget = new ArrayList<FbsBudgetProcessDTO>();
            for (Entry<String, FbsBudgetProcessDTO> entry : mapBudget.entrySet()) {
                listBudget.add(entry.getValue());
            }
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                fbsBudgetProcessDTO.setId(null);
                fbsBudgetProcessDTO.setBillId(NisBillheadPool.getId());
                fbsBudgetProcessDTO.setBillNo(NisBillheadPool.getBillNo());
                fbsBudgetProcessDTO.setCreateBy(NisBillheadPool.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(NisBillheadPool.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);
            }
        }
        //更新合并明细单状态
        for (com.bytter.biz.netbank.entity.NisBillheadPool t : list) {
            NisBillheadPoolVO vo = new NisBillheadPoolVO();
            BeanUtils.copyNotNullProperties(t, vo);
            vo.setStatus(NetbankConstant.STATUE_DEL);
            //保存合并单号
            vo.setMergeId(NisBillheadPool.getId());
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            vo.setSchedulingDate(null);
            vo.setSchedulingElementsId(null);
            //合并付款单，控制原状态为0暂存/-1打回
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateNisBillheadPoolStatus(vo, oldStatus);
        }
    }
    /**
     * 撤销合并
     *
     * @param ids
     */
    public void mergeCancel(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //获取合并明细
            List<NisBillheadPool> list = nisBillheadPoolDao.listBy("mergeId", id);
            if (list == null || list.size() <= 0) {
                //非撤销合并单据，不可进行撤销合并操作
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGECANCEL_VALID);
            } else {
                NisBillheadPool t = this.get(id, true);
                FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);
                // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
                if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                        || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
                    //获取票据ID
                    String billIds = nisBillService.getBillIds(t.getId());
                    if (!EmptyUtils.isBlank(billIds)) {
                        String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                        //解锁
                        this.accebillService.batchUnLock(abAccrecordsIds, user);
                    }
                }
                for (NisBillheadPool n : list) {
                    NisBillheadPoolVO dtlVo = new NisBillheadPoolVO();
                    BeanUtils.copyNotNullProperties(n, dtlVo);
                    dtlVo.setStatus(NetbankConstant.STATUE_TEMP);
                    dtlVo.setMergeId(null);
                    dtlVo.setUpdateBy(user.getLoginName());
                    dtlVo.setUpdateDate(DateUtils.getDate());
                    //撤销合并付款单，控制原付款单的原状态为-2已删除
                    Integer[] oldStatus = {NetbankConstant.STATUE_DEL};
                    this.updateNisBillheadPoolStatus(dtlVo, oldStatus);
                    // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
                    if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                            || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(n.getIsOpenBill())))) {
                        //获取票据ID
                        String billIds = nisBillService.getBillIds(n.getId());
                        if (!EmptyUtils.isBlank(billIds)) {
                            String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                            //重新锁定
                            this.accebillService.batchLock(abAccrecordsIds, user);
                        }
                    }
                }
                //删除合并单据
                NisBillheadPoolVO vo = new NisBillheadPoolVO();
                BeanUtils.copyNotNullProperties(t, vo);
                vo.setStatus(NetbankConstant.STATUE_DEL);
                vo.setUpdateBy(user.getLoginName());
                vo.setUpdateDate(DateUtils.getDate());
                vo.setSchedulingElementsId(null);
                vo.setSchedulingDate(null);
                //撤销合并付款单，控制合并付款单的原状态为0暂存/-1打回
                Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
                this.updateNisBillheadPoolStatus(vo, oldStatus);
            }
        }
    }
    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    public boolean isMergeBill(String id) {
        //获取合并明细
        List<NisBillheadPool> list = nisBillheadPoolDao.listBy("mergeId", id);
        if (EmptyUtils.isEmptyList(list)) {
            return false;
        }
        return true;
    }
    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<NisBillheadPoolVO> listByMergeId(String mergeId) {
        return this.nisBillheadPoolDao.listByMergeId(mergeId);
    }
    /**
     * 打回付款单
     *
     * @param id
     */
    public void backNisBillheadPool(String id, ICurrentUser user, String backType) {
        NisBillheadPool NisBillheadPool = this.get(id, true);
        NisBillheadPool.setStatus(NetbankConstant.STATUE_BACK);
        NisBillheadPool.setUpdateBy(user.getLoginName());
        NisBillheadPool.setUpdateDate(DateUtils.getDate());
        String message = "";
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_EXECUTE_BACK;
        if (NetbankConstant.BACK_TYPE_2.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_EXECUTE_BACK_SUCC;
        } else if (NetbankConstant.BACK_TYPE_3.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_BATCH_EXECUTE_BACK_SUCC;
        } else if (NetbankConstant.BACK_TYPE_4.equalsIgnoreCase(backType)) {
            NisBillheadPool.setOrderNo(null);
            NisBillheadPool.setTallySign(null);
            message = NetbankConstant.NETBANK_LOG_CMSTALLY_BACK_SUCC;
            messageType = NisMessageConstant.MESSAGE_TYPE_CMSTALLY_BACK;
        } else if (NetbankConstant.BACK_TYPE_5.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_REFUNDBLENDING_BACK_SUCC;
            messageType = NisMessageConstant.MESSAGE_TYPE_REFUNDBLENDING_BACK;
        }
        this.saveOrUpdate(NisBillheadPool);
        //生成日志(手工记账打回无需再生成日志，账务本身已生成)
        if (!NetbankConstant.BACK_TYPE_4.equalsIgnoreCase(backType)) {
            nisMessageService.saveMessage(NisBillheadPool.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }
    /**
     * 批量提交付款单
     *
     * @param NisBillheadPool
     * @param random
     * @param sourceData
     * @param caCoding
     * @param user
     */
    public void batchSubmitNisBillheadPool(NisBillheadPoolVO NisBillheadPool, String random, String sourceData, String caCoding, ICurrentUser user) {
        NisBillheadPool.setStatus(NetbankConstant.STATUE_CHECK);
        NisBillheadPool.setBatch(true);
        if (CommonConstant.NO.equals(NisBillheadPool.getIsPayed())) {
            //期望支付日期小于系统当前日期，则默认更新为系统当前日期
            if (NisBillheadPool.getWishPayday().before(DateUtils.getDateYMD())) {
                NisBillheadPool.setWishPayday(DateUtils.getDateYMD());
            }
        }
        this.updateNisBillheadPool(NisBillheadPool, user, true);
    }
    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchDelNisBillheadPool(String ids, ICurrentUser user, String reply) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delNisBillheadPool(id, user, true, reply);
        }
    }
    /**
     * 费用报销处理-打回
     *
     * @param id
     */
    public void backDetail(String id, ICurrentUser user, boolean isFail) {
        //根据付款单ID获取失败的费用报销明细
        List<NisExpenseDetailVO> listExpenseDetail = null;
        if (isFail) {
            listExpenseDetail = nisExpenseDetailService.listByBillIdFailure(id);
        } else {
            listExpenseDetail = nisExpenseDetailService.listByBillId(id);
        }
        if (!EmptyUtils.isEmptyList(listExpenseDetail)) {
            NisBillheadPool NisBillheadPool = this.get(id, true);
            //逻辑删除付款单据
            this.deleteNisBillheadPool(NisBillheadPool, user, false, true);
            for (NisExpenseDetailVO detail : listExpenseDetail) {
                //交易成功的明细不处理
                if (!BdlinkConstant.SUCCESS.equals(detail.getVoucherStat()) && !BdlinkConstant.WAIT_RESULT.equals(detail.getVoucherStat())) {
                    nisExpenseDetailService.backExpenseDetail(detail.getId(), user);
                }
            }
        } else {
            if (isFail) {
                //不存在交易失败的费用报销明细，打回失败
                throw new ParameterException(NetbankMessageEnum.NIS_EXPENSE_MESSAGE_FAILUREDTL_NULL);
            }
        }
    }
    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<NisBillheadPoolVO> list, ICurrentUser user) {
        HashMap<String, NisBillheadPoolVO> bankMap = new HashMap<>();
        // 先批量获取付款流水号
        List<String> listSequence = SequenceSingleton.listSequence(SequenceEnum.NIS_BILLHEAD_POOL, list.size());
        int index = 0;
        for (NisBillheadPoolVO nisBillheadPoolVO : list) {
            nisBillheadPoolVO.setBillFrom(NetbankConstant.BILLFROM_1);
            nisBillheadPoolVO.setStatus(NetbankConstant.STATUE_TEMP);
            //是否已付款默认设置为否
            //NisBillheadPool.setIsPayed(NetbankConstant.ISPAYED_0);
            nisBillheadPoolVO.setBillNo(listSequence.get(index));
            this.saveNisBillheadPoolForImport(nisBillheadPoolVO, user);
            index++;
            if (StringUtils.isNotBlank(nisBillheadPoolVO.getPayeeBankCode())) {
                if (!bankMap.containsKey(nisBillheadPoolVO.getPayeeBankCode())) {
                    bankMap.put(nisBillheadPoolVO.getPayeeBankCode(), nisBillheadPoolVO);
                }
            }
        }
        for (String payeeBankCode : bankMap.keySet()) {
            // 初始化联行号信息
            this.bankService.saveBtBank(payeeBankCode, bankMap.get(payeeBankCode).getBankTypeId());
        }
    }
    /**
     * 导入保存
     *
     * @param nisBillheadPool
     */
    public void saveNisBillheadPoolForImport(NisBillheadPoolVO nisBillheadPool, ICurrentUser user) {
        com.bytter.biz.netbank.entity.NisBillheadPool entity = new NisBillheadPool();
        BeanUtils.copyNotNullProperties(nisBillheadPool, entity);
        if (StringUtils.isBlank(entity.getBillNo())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD_POOL));
        }
        if (StringUtils.isBlank(nisBillheadPool.getErpId())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        super.saveEntity(entity);
        //保存票据关联信息
        if (!StringUtils.isBlank(nisBillheadPool.getBillId())) {
            nisBillService.saveOrUpdate(entity.getId(), nisBillheadPool.getBillId());
        }
        nisBillheadPool.setId(entity.getId());
        nisBillheadPool.setBillNo(entity.getBillNo());
        //写入计划执行
        this.writeBudgetProcess(nisBillheadPool, user);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NisMessageConstant.NIS_LOG_IMPORT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisBillheadPoolVO vo) {
        return null;
    }
    @Override
    public void verifyHandler(List<NisBillheadPoolVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //显示已付款是标识
            String showIsPayed = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue();
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //付款方式集合
            HashMap<String, FcVoucherType> voucherTypeMap = new HashMap<>();
            //币种集合
            HashMap<String, String> curMap = new HashMap<>();
            //银行账号集合
            HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
            //内部账号集合
            HashMap<String, FcAccDTO> fcAccMap = new HashMap<>();
            //内转收款账号集合
            HashMap<String, List<FcAccDTO>> fcAccPayeeMap = new HashMap<>();
            //单位计划上报集合
            HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
            //获取人民币
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            for (NisBillheadPoolVO vo : list) {
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    corp = new CorpDTO();
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    vo.setCorpId(corp.getId());
                }
                //判断付款方式是否存在
                FcVoucherType voucherType = voucherTypeMap.get(vo.getVoucherName());
                if (voucherType == null) {
                    voucherType = fcVoucherTypeService.getByVoucherName(vo.getVoucherName());
                    voucherTypeMap.put(vo.getVoucherName(), voucherType);
                }
                if (EmptyUtils.isEmpty(voucherType)) {
                    msgList.add("nis.netbank.message.vouchertype.null");
                } else if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                    msgList.add("nis.netbank.message.vouchertype.bill.mismatch");
                } else {
                    //判断单位是否分配付款方式的权限
                    if (!EmptyUtils.isEmpty(corp) && !EmptyUtils.isEmpty(voucherType)) {
                        if (!btAssignPayWayService.isExistVoucherType(corp.getId(), voucherType.getId())) {
                            msgList.add("nis.netbank.message.corpvouchertype.null");
                        } else {
                            vo.setVoucherTypeId(voucherType.getId());
                            vo.setIsBudget(voucherType.getIsBudget());
                        }
                        //校验币别
                        String curId = curMap.get(vo.getCurName());
                        if (StringUtils.isBlank(curId)) {
                            curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                            curMap.put(vo.getCurName(), curId);
                        }
                        if (EmptyUtils.isBlank(curId)) {
                            msgList.add("nis.netbank.message.cur.null");
                        } else {
                            vo.setCurId(curId);
                            //付款账号校验
                            if (StringUtils.isEmpty(vo.getPayerAcc())) {
                                //应收/付票据，无需校验付款账号
                                if (!NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                                        && !NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                                    msgList.add("nis.netbank.message.payeracc.null");
                                }
                            } else {
                                //校验付款账号,记账时取内部账号，不记账时取银行账号
                                if (NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
                                    FcAccDTO fcAcc = fcAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (fcAcc == null) {
                                        fcAcc = this.isExistFcAcc(corp.getUserAccountCode(), vo.getPayerAcc());
                                        fcAccMap.put(vo.getCorpId() + vo.getPayerAcc(), fcAcc);
                                    }
                                    if (EmptyUtils.isEmpty(fcAcc)) {
                                        msgList.add("nis.netbank.message.fcacc.null");
                                    } else {
                                        vo.setAccId(fcAcc.getId());
                                        vo.setPayerAccName(fcAcc.getAccName());
                                        vo.setPayerBank(fcAcc.getNetName());
                                        vo.setDirectPayFlag(NetbankConstant.DIRECTPAYFLAG_N);
                                    }
                                    //转付记账时，需要校验银行账号
                                    if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
                                        if (StringUtils.isEmpty(vo.getDownAcc())) {
                                            msgList.add("nis.netbank.message.downacc.null");
                                        } else {
                                            if (StringUtils.isNotEmpty(vo.getAccId())) {
                                                BtBankAccDTO bankAcc = this.isExistDownAcc(corp.getUserAccountCode(), vo.getPayerAcc(), curId, vo.getDownAcc());
                                                if (bankAcc == null) {
                                                    msgList.add("nis.netbank.message.downacc.mismatch");
                                                } else {
                                                    vo.setDownAccId(bankAcc.getId());
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    //获取付款账号
                                    BtBankAccDTO bankAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (bankAcc == null) {
                                        bankAcc = this.isExistBankAcc(corp.getUserAccountCode(), vo.getPayerAcc(), curId);
                                        bankAccMap.put(vo.getCorpId() + vo.getPayerAcc(), bankAcc);
                                    }
                                    if (EmptyUtils.isEmpty(bankAcc)) {
                                        msgList.add("nis.netbank.message.btbankacc.null");
                                    } else {
                                        vo.setAccId(bankAcc.getId());
                                        vo.setPayerAccName(bankAcc.getAccName());
                                        vo.setPayerBank(bankAcc.getBankName());
                                        vo.setBifCode(bankAcc.getBifCode());
                                        vo.setDirectPayFlag(NetbankConstant.DIRECTPAYFLAG_Y);
                                    }
                                    //不记账情况校验跨行标志
                                    if (!NetbankConstant.DIFBANK_0.equals(vo.getDifBank())
                                            && !NetbankConstant.DIFBANK_1.equals(vo.getDifBank())) {
                                        msgList.add("nis.netbank.message.difbank.format");
                                    }
                                    if (!EmptyUtils.isEmpty(bankAcc) && !EmptyUtils.isBlank(vo.getPayeeBankCode())) {
                                        String payeepri = vo.getPayeeBankCode().substring(0, 3);
                                        String payerpri = bankAcc.getBankCode().substring(0, 3);
                                        String difbank = "";
                                        if (payeepri.equals(payerpri)) {
                                            if ("313".equals(payeepri) || "402".equals(payeepri) || "314".equals(payeepri)) {
                                                if (StringUtils.isNotBlank(bankAcc.getBankNamePrefix())
                                                        && StringUtils.isNotBlank(vo.getPayeeBank())
                                                        && vo.getPayeeBank().indexOf(bankAcc.getBankNamePrefix()) != -1) {
                                                    difbank = NetbankConstant.DIFBANK_0;
                                                } else {
                                                    difbank = NetbankConstant.DIFBANK_1;
                                                }
                                            } else {
                                                difbank = NetbankConstant.DIFBANK_0;
                                            }
                                        } else {
                                            difbank = NetbankConstant.DIFBANK_1;
                                        }
                                        if (!EmptyUtils.isBlank(difbank) && !difbank.equals(vo.getDifBank())) {
                                            msgList.add("nis.netbank.message.difbank.format");
                                        }
                                    }
                                    //不记账情况校验地域标志
                                    if (!NetbankConstant.AREASIGN_0.equals(vo.getAreaSign())
                                            && !NetbankConstant.AREASIGN_1.equals(vo.getAreaSign())) {
                                        msgList.add("nis.netbank.message.areasign.format");
                                    }
                                }
                                //付款方式为应付票据、应收票据时付款账号必须为人民币的票据账户
                                if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                                        || NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                                    //获取付款账号
                                    BtBankAccDTO payerAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (payerAcc == null) {
                                        payerAcc = btBankAccService.getBankAccInfo(vo.getPayerAcc());
                                    }
                                    if (!EmptyUtils.isEmpty(payerAcc) && BtBankAccConstant.ELECTRICBILL_YES.equals(payerAcc.getElectricBill())) {
                                        if (!EmptyUtils.isEmpty(rmbCurrency) && !vo.getCurId().equals(rmbCurrency.getId())) {
                                            msgList.add("nis.netbank.message.accbill.mustbermb");
                                        }
                                    } else {
                                        msgList.add("nis.netbank.message.accbill.mustbermb");
                                    }
                                }
                            }
                        }
                        //校验收款账号，内转时校验内部账号
                        if (NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode()) && corp != null) {
                            List<FcAccDTO> listFcAcc = fcAccPayeeMap.get(vo.getCurId() + corp.getNetId());
                            if (EmptyUtils.isEmpty(listFcAcc)) {
                                listFcAcc = innerAccountService.listAccountByParams(vo.getCurId(), corp.getNetId(), null, null);
                                fcAccPayeeMap.put(vo.getCurId() + corp.getNetId(), listFcAcc);
                            }
                            FcAccDTO fcAcc = null;
                            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                                for (FcAccDTO dto : listFcAcc) {
                                    if (vo.getPayeeAcc().equals(dto.getBlnctrAcc())) {
                                        fcAcc = new FcAccDTO();
                                        BeanUtils.copyNotNullProperties(dto, fcAcc);
                                        continue;
                                    }
                                }
                                if (fcAcc != null) {
                                    vo.setPayeeAcc(fcAcc.getBlnctrAcc());
                                    vo.setPayeeBank(fcAcc.getNetName());
                                    if (!fcAcc.getAccName().equals(vo.getPayeeAccName())) {
                                        msgList.add("nis.netbank.message.payeeaccname.mismatch");
                                    }
                                } else {
                                    msgList.add("nis.netbank.message.payeeacc.mismatch");
                                }
                            }
                        } else {
                            //判断开户行在系统中是否存在
                            if (!EmptyUtils.isBlank(vo.getPayeeBankCode())) {
                                InputBankInfoDTO btBankDTO = this.btBankService.getByBankCodeAndImport(vo.getPayeeBankCode());
                                if (EmptyUtils.isEmpty(btBankDTO)) {
                                    msgList.add("nis.netbank.message.bank.null");
                                } else {
                                    //开户行名称不为空且与开户行代码对应的名称不一致
                                    if (!EmptyUtils.isBlank(vo.getPayeeBank()) && !vo.getPayeeBank().equals(btBankDTO.getBankName())) {
                                        msgList.add("nis.netbank.message.payeebank.mismatch");
                                    } else {
                                        vo.setPayeeBankCode(btBankDTO.getBankCode());
                                        vo.setPayeeBank(btBankDTO.getBankName());
                                        vo.setProv(btBankDTO.getProv());
                                        vo.setCity(btBankDTO.getCity());
                                    }
                                }
                            } else {
                                if (EmptyUtils.isBlank(vo.getPayeeBank())) {
                                    msgList.add("nis.netbank.message.payeebankcode.null");
                                } else {
                                    InputBankInfoDTO btBankDTO = this.btBankService.getByBankNameAndImport(vo.getPayeeBank());
                                    if (EmptyUtils.isEmpty(btBankDTO)) {
                                        msgList.add("nis.netbank.message.bank.null");
                                    } else {
                                        vo.setPayeeBankCode(btBankDTO.getBankCode());
                                        vo.setPayeeBank(btBankDTO.getBankName());
                                        vo.setProv(btBankDTO.getProv());
                                        vo.setCity(btBankDTO.getCity());
                                    }
                                }
                            }
                        }
                        if (!StringUtils.isEmpty(vo.getPayerAcc())) {
                            //收付款账号不能相同
                            if (vo.getPayerAcc().equals(vo.getPayeeAcc())) {
                                msgList.add("nis.netbank.message.payeeacc.noteq.payeracc");
                            }
                        }
                        //应收票据校验票据信息
                        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                            if (!EmptyUtils.isBlank(vo.getBillId())) {
                                //校验票据是否存在且可用
                            } else {
                                msgList.add("nis.netbank.message.billid.null");
                            }
                        }
                    }
                }
                //金额不能为空且必须大于0
                if (EmptyUtils.isEmpty(vo.getPayMoney())) {
                    msgList.add("nis.netbank.message.money.null");
                } else {
                    if (vo.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
                        msgList.add("nis.netbank.message.money.mismatch");
                    }
                }
                //是否对私付款
                if (!NetbankConstant.FORINDIVIDUAL_0.equals(vo.getIsForindividual())
                        && !NetbankConstant.FORINDIVIDUAL_1.equals(vo.getIsForindividual())) {
                    msgList.add("nis.netbank.message.forindividual.format");
                }
                //是否已付款
                if (CommonConstant.YES.equals(showIsPayed) && StringUtils.isNotBlank(vo.getIsPayed())) {
                    if (!CommonConstant.NO.equals(vo.getIsPayed())
                            && !CommonConstant.YES.equals(vo.getIsPayed())) {
                        msgList.add("nis.netbank.message.ispayed.format");
                    }
                } else {
                    vo.setIsPayed(CommonConstant.NO);
                }
                //加急标志
                if (!NetbankConstant.ISQUICK_0.equals(vo.getIsQuick())
                        && !NetbankConstant.ISQUICK_1.equals(vo.getIsQuick())) {
                    msgList.add("nis.netbank.message.isquick.format");
                }
                //期望支付日期
                if (vo.getWishPayday() != null) {
                    if (vo.getWishPayday().compareTo(DateUtils.getDateYMD()) == -1) {
                        msgList.add("nis.netbank.message.wishpayday.mismatch");
                    }
                }
                //业务类别校验
                if (!EmptyUtils.isEmpty(vo.getBusTypeName())) {
                    NisCorpBusVO busType = nisCorpBusService.getBusTypeByName(vo.getCorpId(), "1", vo.getBusTypeName());
                    if (busType == null) {
                        msgList.add("nis.netbank.message.bustype.mismatch");
                    } else {
                        vo.setBusType(busType.getBusId());
                    }
                }
                if (voucherType != null) {
                    //是否已开票
                    if (StringUtils.isEmpty(vo.getIsOpenBill())) {
                        //应付票据是否已开票不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                            msgList.add("nis.netbank.message.isopenbill.null");
                        } else {
                            vo.setIsOpenBill(NetbankConstant.ISOPENBILL_0);
                        }
                    } else {
                        //是否已开票不为空时，校验格式
                        if (!NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())
                                && !NetbankConstant.ISOPENBILL_1.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.isopenbill.format");
                        }
                    }
                    //票据类型
                    if (EmptyUtils.isEmpty(vo.getBillType())) {
                        //付款方式为应付票据，是否已开票为否时，票据类型不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.billType.null");
                        }
                    } else {
                        String billType = dictService.get("AB_BILL_TYPE", vo.getBillType());
                        if (StringUtils.isEmpty(billType)) {
                            //票据类型格式不符
                            msgList.add("nis.netbank.message.billType.format");
                        }
                    }
                    //票据流通介质
                    if (EmptyUtils.isEmpty(vo.getBillFlag())) {
                        //付款方式为应付票据，是否已开票为否时，票据流通介质不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.billFlag.null");
                        }
                    } else {
                        String billFlag = dictService.get("AB_BILL_FLAG", vo.getBillFlag());
                        if (StringUtils.isEmpty(billFlag)) {
                            //票据流通介质格式不符
                            msgList.add("nis.netbank.message.billFlag.format");
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }
    /**
     * 判断内部账号是否授权
     *
     * @param userAccountCode
     * @param acc
     */
    public FcAccDTO isExistFcAcc(String userAccountCode, String acc) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        FcAccDTO fcAccDTO = null;
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            List<FcAccDTO> listFcAcc = accountGrantService.listInnerAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                for (FcAccDTO fcAcc : listFcAcc) {
                    if (fcAcc.getBlnctrAcc().equals(acc)) {
                        return fcAcc;
                    }
                }
            }
        }
        return fcAccDTO;
    }
    /**
     * 判断银行账号是否授权
     *
     * @param userAccountCode
     * @param acc
     * @param curId
     */
    public BtBankAccDTO isExistBankAcc(String userAccountCode, String acc, String curId) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
                listAcc.addAll(listOnlineBankAcc);
            }
            if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        BtBankAccDTO btBankAccDTO = null;
        if (!EmptyUtils.isEmptyList(listAcc)) {
            for (BtBankAccDTO bankAcc : listAcc) {
                if (bankAcc.getBankAcc().equals(acc) && bankAcc.getCurId().equals(curId)) {
                    return bankAcc;
                }
            }
        }
        return btBankAccDTO;
    }
    /**
     * 判断银行账号与内部账号是否匹配
     *
     * @param userAccountCode
     * @param account
     * @param curId
     */
    public BtBankAccDTO isExistDownAcc(String userAccountCode, String account, String curId, String downAcc) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        BtBankAccDTO btBankAccDTO = null;
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            //获取具有转账权限的银行账号
            List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                for (BtBankAccDTO bankAcc : listBankAcc) {
                    if (bankAcc.getCurId().equals(curId) && account.equals(bankAcc.getCounterAccId()) && downAcc.equals(bankAcc.getBankAcc())) {
                        return bankAcc;
                    }
                }
            }
        }
        return btBankAccDTO;
    }
    /**
     * 判断账号付款是否使用中
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExistBlnctrAcc(String blnctrAcc) {
        List<NisBillheadPool> list = nisBillheadPoolDao.listByPayerAcc(blnctrAcc);
        if (EmptyUtils.isEmpty(list)) {
            return false;
        }
        return true;
    }
    /**
     * 客商信息保存
     *
     * @param NisBillheadPool
     */
    private void saveIfNotExist(NisBillheadPoolVO NisBillheadPool) {
        //内转不需要保存供应商信息
        if (NetbankConstant.PAY_TYPE_05.equals(NisBillheadPool.getSysVoucherCode())) {
            return;
        }
        ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
        externalCorpDTO.setCorpId(NisBillheadPool.getCorpId());
        externalCorpDTO.setName(NisBillheadPool.getPayeeAccName());
        externalCorpDTO.setProvince(NisBillheadPool.getProv());
        externalCorpDTO.setCity(NisBillheadPool.getCity());
        externalCorpDTO.setSourceSystem("BFS");
        //是否有效
        externalCorpDTO.setStatus(1);
        //是否共享
        externalCorpDTO.setIsCommon(NisBillheadPool.getIsCommon());
        List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
        ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
        externalCorpBankAccDTO.setExternalAcc(NisBillheadPool.getPayeeAcc());
        externalCorpBankAccDTO.setBankType(NisBillheadPool.getBankTypeId());
        externalCorpBankAccDTO.setBank(NisBillheadPool.getPayeeBank());
        externalCorpBankAccDTO.setBankSourceCode(NisBillheadPool.getPayeeBankCode());
        externalCorpBankAccDTO.setAccProvince(NisBillheadPool.getProv());
        externalCorpBankAccDTO.setAccCity(NisBillheadPool.getCity());
        externalCorpBankAccDTO.setIsDefaultAcc("0");
        externalCorpBankAccList.add(externalCorpBankAccDTO);
        externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
        externalCorpService.saveIfNotExist(externalCorpDTO);
    }
    /**
     * 重复付款校验
     *
     * @param corpId   单位
     * @param payMoney 金额
     * @param payeeAcc 收款账号
     * @param id       单据号（编辑时校验需要）
     * @param tempFlag true - 查询暂存打回状态， false-查询已提交之后的状态数据
     * @return
     */
    public boolean isExistRepay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, boolean tempFlag) {
        String repayAlertDaysStr = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REPAYALERTDAYS).getValue();
        if (!StringUtils.isBlank(repayAlertDaysStr)) {
            Integer repayAlertDays = Integer.parseInt(repayAlertDaysStr);
            String endDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
            String beginDate = endDate;
            if (repayAlertDays != 0) {
                beginDate = DateUtils.format(DateUtils.addDay(DateUtils.getDate(), -repayAlertDays), "yyyy-MM-dd");
            }
            beginDate = beginDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            List<NisBillheadPool> list = nisBillheadPoolDao.listByRePay(corpId, payMoney, payeeAcc, beginDate, endDate, curId, id, tempFlag);
            if (!EmptyUtils.isEmptyList(list)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取内转标志
     *
     * @param nisBillheadPool
     * @return
     */
    public String getInnerSign(NisBillheadPoolVO nisBillheadPool) {
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillheadPool.getVoucherTypeId(), true);
        //内转付款方式 并且内转挂计划才返回内转标志
        if (NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode()) && CommonConstant.VALID_SIGN_YES.equals(voucherType.getIsBudget())) {
            return BudgetConstant.INNER_SIGN_1;
        } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())) {
            CorpDTO corpDTO = this.corpService.getCorpIdByName(nisBillheadPool.getPayeeAccName());
            if (EmptyUtils.isEmpty(corpDTO)) {
                return BudgetConstant.INNER_SIGN_0;
            }
            boolean isTheSameNet = this.corpService.isTheSameNetByCorpId(nisBillheadPool.getCorpId(), corpDTO.getId());
            return isTheSameNet ? BudgetConstant.INNER_SIGN_1 : BudgetConstant.INNER_SIGN_0;
        } else {
            return BudgetConstant.INNER_SIGN_0;
        }
    }
    /**
     * 获取审批列表单位树
     *
     * @param flowStatus track 流程跟踪 todo 审批中
     * @param user
     * @return
     */
    /**
     * 校验收款账户是否是网点或成员单位
     *
     * @param vo
     * @return
     */
    public boolean isCheckExternalCorp(NisBillheadPoolVO vo) {
        //内转不需要保存供应商信息
        if (NetbankConstant.PAY_TYPE_05.equals(vo.getSysVoucherCode())) {
            return false;
        }
        // 根据收款账号查下银行账号表校验是否是存在的内部单位账号
        BtBankAccDTO bankAcc = btBankAccService.getBankAccInfo(vo.getPayeeAcc());
        if (bankAcc != null && bankAcc.getAccName().equals(vo.getPayeeAccName())) {
            return false;
        }
        return true;
    }
    /**
     * 批量选择付款账号
     *
     * @param NisBillheadPool
     * @param user
     */
    public void batchSetPayerAcc(NisBillheadPoolVO NisBillheadPool, ICurrentUser user) {
        if (StringUtils.isNotEmpty(NisBillheadPool.getId())) {
            String[] ids = NisBillheadPool.getId().split(",");
            for (String id : ids) {
                com.bytter.biz.netbank.entity.NisBillheadPool entity = this.get(id);
                //暂存和编辑状态才可以提交
                if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
                }
                entity.setAccId(NisBillheadPool.getAccId());
                entity.setPayerAcc(NisBillheadPool.getPayerAcc());
                entity.setPayerAccName(NisBillheadPool.getPayerAccName());
                entity.setPayerBank(NisBillheadPool.getPayerBank());
                entity.setCurId(NisBillheadPool.getCurId());
                entity.setBifCode(NisBillheadPool.getBifCode());
                entity.setDownAccId(NisBillheadPool.getDownAccId());
                //跨行标志和地域标志设置
                //获取付款账号
                BtBankAccDTO bankAcc = btBankAccService.getBankAccById(entity.getAccId());
                if (bankAcc != null) {
                    //跨行标志
                    if (StringUtils.isEmpty(bankAcc.getBankCode()) || StringUtils.isEmpty(entity.getPayeeBankCode())) {
                        entity.setDifBank(NetbankConstant.DIFBANK_0);
                    } else if (bankAcc.getBankCode().substring(0, 3).equals(entity.getPayeeBankCode().substring(0, 3))) {
                        if ("313".equals(bankAcc.getBankCode().substring(0, 3)) || "402".equals(bankAcc.getBankCode().substring(0, 3))
                                || "314".equals(bankAcc.getBankCode().substring(0, 3))) {
                            if (StringUtils.isNotBlank(bankAcc.getBankNamePrefix()) && entity.getPayeeBank().indexOf(bankAcc.getBankNamePrefix()) != -1) {
                                entity.setDifBank(NetbankConstant.DIFBANK_0);
                            } else {
                                entity.setDifBank(NetbankConstant.DIFBANK_1);
                            }
                        } else {
                            entity.setDifBank(NetbankConstant.DIFBANK_0);
                        }
                    } else {
                        entity.setDifBank(NetbankConstant.DIFBANK_1);
                    }
                    //地域标志
                    if (StringUtils.isEmpty(bankAcc.getProv()) || StringUtils.isEmpty(entity.getProv())) {
                        entity.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else if (bankAcc.getProv().equals(entity.getProv()) && bankAcc.getCity().equals(entity.getCity())) {
                        entity.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else {
                        entity.setAreaSign(NetbankConstant.AREASIGN_1);
                    }
                }
                this.saveOrUpdate(entity);
                //生成日志
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SET_PAYERACC, NisMessageConstant.NIS_LOG_UPDATE_SAVE_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
            }
        }
    }
    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadPoolVO> listByTime(String beginDate, String endDate) {
        return nisBillheadPoolDao.listByTime(beginDate, endDate);
    }
    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @param billFrom 数据来源
     * @return
     * @author zuoy 2019-03-06
     */
    public NisBillheadPoolVO getNisBillheadPoolByErpId(String erpId, String billFrom) {
        return nisBillheadPoolDao.getNisBillheadByErpId(erpId, billFrom);
    }
    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     *
     * @param bankAcc 付款账号
     * @param curId   币别
     * @return
     */
    public BigDecimal getSum(String bankAcc, String curId, String startTime, String endTime) {
        List<NisBillheadPool> list = nisBillheadPoolDao.getSum(bankAcc, curId, startTime, endTime);
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(bankAcc);
        CorpDTO corpDTO = corpService.get(bankAccInfo.getCorpId());
        BigDecimal result = new BigDecimal(0);//总金额
        //汇率转换
        for (NisBillheadPool entity : list) {
            //获取汇率，单位ID，当前金额币种ID，当前时间
            BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(entity.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
            }
            BigDecimal multiply = entity.getPayMoney().multiply(rate);
            result = result.add(multiply);
        }
        return result;
    }
    /**
     * 根据单位和币别获取付款单的付款金额总和
     *
     * @param corpId 单位
     * @param curId  币别
     * @return
     */
    public BigDecimal getSumForCorp(String corpId, String curId, String startTime, String endTime) {
        List<NisBillheadPool> list = nisBillheadPoolDao.getListByCorpAndCurId(corpId, startTime, endTime);
        BigDecimal result = new BigDecimal(0);//总金额
        CorpDTO corpDTO = corpService.get(corpId);
        //汇率转换
        for (NisBillheadPool entity : list) {
            //获取汇率，单位ID，当前金额币种ID，当前时间
            BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(entity.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
            }
            BigDecimal multiply = entity.getPayMoney().multiply(rate);
            result = result.add(multiply);
        }
        return result;
    }
    /**
     * 根据id查询数据金额
     *
     * @param id 数据项Id
     * @return
     */
    public NisBillheadPoolDTO getMoneyById(String id) {
        List<NisBillheadPool> list = nisBillheadPoolDao.getMoneyById(id);
        if (list == null || list.size() == 0) {
            return null;
        }
        NisBillheadPool NisBillheadPool = list.get(0);
        CorpDTO corpDTO = corpService.get(NisBillheadPool.getCorpId());
        BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(NisBillheadPool.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        if (rate == null) {
            rate = new BigDecimal(1);
        }
        NisBillheadPool.setPayMoney(NisBillheadPool.getPayMoney().multiply(rate));
        return BeanUtils.copyNewInstanceProperties(NisBillheadPool, NisBillheadPoolDTO.class);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的付款单据，不允许禁用！
            int num = this.nisBillheadPoolDao.getBillheadCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BILLHEAD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BILLHEAD_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 根据单位获取有权限的用户
     *
     * @param corpId
     * @param user
     * @return
     */
    public List<UserDTO> getUserByCorpId(String corpId, ICurrentUser user) {
        //付款办理新增资源
        String resource = ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY;
        List<UserDTO> userDTOList = new ArrayList<>();
        List<String> corpIdList = new ArrayList<>();
        //如果单位为空过滤有权限的单位
        if (EmptyUtils.isEmpty(corpId)) {
            corpIdList = corpService.getUserCorpIds(resource, user.getId());
        } else {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            for (int i = 0; i < corpIds.length; i++) {
                corpIdList.add(corpIds[i]);
            }
        }
        List<String> userList = userService.listUserIdsByCorpIdAndPermission(corpIdList, resource);
        if (!EmptyUtils.isEmpty(userList)) {
            userDTOList = userService.listByUserIds(userList);
        }
        return userDTOList;
    }
    /**
     * 判断是否存在相同的时间戳
     *
     * @param timeStamp
     * @return
     */
    public boolean isExistTimeStamp(String timeStamp) {
        if (StringUtils.isNotBlank(timeStamp)) {
            List<NisBillheadPool> list = this.nisBillheadPoolDao.listBy("timeStamp", timeStamp);
            if (EmptyUtils.isNotEmpty(list)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 带原状态更新付款单
     *
     * @param vo        付款对象
     * @param oldStatus 原状态控制
     * @return
     */
    public void updateNisBillheadPoolStatus(NisBillheadPoolVO vo, Integer[] oldStatus) {
        int result = this.nisBillheadPoolDao.updateNisBillheadStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }
    /**
     * 获取ERP待处理单据
     * @return
     */
    public List<NisBillheadPoolVO> listErpTodoBill() {
        return nisBillheadPoolDao.listErpTodoBill();
    }
    /**
     * w
     * @param id
     */
    public void returnNisBillheadPool(String id){
        NisBillheadPool entity = nisBillheadPoolDao.get(id,true);
        if(!EmptyUtils.isEmpty(entity)) {
            entity.setStatus(NetbankConstant.STATUE_BACK);
            nisBillheadPoolDao.update(entity);
        }
    }
    @Override
    @XxlJob("NisBillheadPoolService")
    public void run(String arguments) {
        //是否启用待付款池
        String enablePendingPaymentPool = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL).getValue();
        if("1".equals(enablePendingPaymentPool)){
            try {
                this.logger.info("---开启待付款池自动转移到付款单定时任务---");
                List<NisBillheadPoolVO> list = nisBillheadPoolDao.listPoolTodoBill();
                ICurrentUser user = this.getCurrentUser();
                Integer[] oldStatus = {NetbankConstant.STATUE_TEMP};
                if (EmptyUtils.isNotEmpty(list)) {
                    for (int i = 0; i < list.size(); i++) {
                        NisBillheadPoolVO nisBillheadPoolVO = list.get(i);
                        this.saveNisBillhead(nisBillheadPoolVO,user);
                        nisBillheadPoolVO.setStatus(NetbankConstant.STATUE_CHECK);
                        this.updateNisBillheadPoolStatus(nisBillheadPoolVO,oldStatus);
                    }
                }
                this.logger.info("---结束待付款池自动转移到付款单定时任务---");
            } catch (Exception e) {
                this.logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
    protected ICurrentUser getCurrentUser() {
        return null;//(ShiroUser) SecurityUtils.getSubject().getPrincipal();
    }

}
