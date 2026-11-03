package com.bytter.biz.netbank.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.BisGatherPolicyAccountService;
import com.bytter.biz.allocation.service.BisGatherPolicyAgreementService;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.vo.BtBankAccBalVO;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.netbank.dao.NisDemandDao;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.vo.NisAccbookVO;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 *资金需求单
 */
@Service("nisDemandService")
public class NisDemandService extends BaseService<NisDemand, String> {
    @Autowired
    private NisDemandDao nisDemandDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @Autowired
    private NisAccbookService nisAccbookService;
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IEquityMaintenanceService equityMaintenanceService;
    @Autowired
    private BisGatherPolicyAgreementService policyAgreementService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private BisGatherPolicyAccountService policyAccountService;

    @Override
    public BaseDao<NisDemand, String> getEntityDao() {
        return nisDemandDao;
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<NisDemandVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.NETBANK_NISDEMAND_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null or n.create_by='')");
        sql.append(" and n.status in (" + NisDemandConstant.STATUE_TEMP + "," + NisDemandConstant.STATUE_CHECK + ") ");
        DataTablesOutput dataTablesOutput = nisDemandDao.getDataTablesUnion(dataTablesInput, sql.toString());
        return dataTablesOutput;
    }

    /**
     * 保存
     * @param nisDemandVO
     */
    @Transactional
    public String saveNisDemand(NisDemandVO nisDemandVO, ICurrentUser user) {
        NisDemand entity = new NisDemand();
        BeanUtils.copyNotNullProperties(nisDemandVO, entity);
        //当需求类型为付款时
        if(NisDemandConstant.DEMANDTYPE_01.equals(nisDemandVO.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_B));
            //当需求类型为承兑到期时
        }else if(NisDemandConstant.DEMANDTYPE_02.equals(nisDemandVO.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_A));
            //当需求类型为融资到期时
        }else if(NisDemandConstant.DEMANDTYPE_03.equals(nisDemandVO.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_C));
        }
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        //单位信息
        CorpDTO corpDTO = corpService.get(entity.getCorpId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            String parentids = corpDTO.getParentIds();
            String[] parentId = parentids.split(",");
            entity.setCorpLevel(parentId.length);
        }
        super.saveEntity(entity);
        return entity.getBillNo();
    }

    /**
     * 批量做废需求单
     *
     * @param ids
     */
    public void batchDefeatNisDemand(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.defeatNisDemand(id, user);
        }
    }

    /**
     * 做废需求单
     * @param id
     * @param user
     */
    @Transactional
    public void defeatNisDemand(String id, ICurrentUser user) {
        NisDemand entity = this.get(id, true);
        //做废需求单，控制原状态为0-暂存/1已确认
        Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
        NisDemandVO vo = new NisDemandVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(NisDemandConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        vo.setPayBillDate(null);
        vo.setPayBillId(null);
        vo.setPayBillNo(null);
        this.updateNisDemandStatus(vo, oldStatus);
        this.nisDemandDao.updateNisDemandClearSuperId(id);
    }

    /**
     * 带原状态更新付款单
     *
     * @param vo        付款对象
     * @param oldStatus 原状态控制
     * @return
     */
    @Transactional
    public void updateNisDemandStatus(NisDemandVO vo, Integer[] oldStatus) {
        int result = this.nisDemandDao.updateNisDemandStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }

    /**
     * 编辑需求单
     * @param nisDemandVO
     */
    @Transactional
    public void updateNisDemand(NisDemandVO nisDemandVO, ICurrentUser user) {
        if (EmptyUtils.isBlank(nisDemandVO.getCreateBy())) {
            nisDemandVO.setCreateBy(user.getLoginName());
        }
        if (nisDemandVO.getCreateDate() == null) {
            nisDemandVO.setCreateDate(DateUtils.getDate());
        }
        nisDemandVO.setUpdateBy(user.getLoginName());
        nisDemandVO.setUpdateDate(DateUtils.getDate());
        //编辑需求单，控制原状态为0-暂存/1已确认
        Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
        this.updateNisDemandStatus(nisDemandVO, oldStatus);
    }

    /**
     * 根据付款ID获取需求单
     * @param id
     * @return
     */
    public NisDemandVO getByIdUnion(String id){
        NisDemandVO nisDemandVO = nisDemandDao.getByIdUnion(id);
        //获取余额
        getBalInfo(nisDemandVO);
        return nisDemandVO;
    }

    /**
     * 根据付款billNo获取需求单
     * @param billNo
     * @return
     */
    public NisDemandVO getByBillNoUnion(String billNo){
        NisDemandVO nisDemandVO = nisDemandDao.getByBillNoUnion(billNo);
        //获取余额
        getBalInfo(nisDemandVO);
        return nisDemandVO;
    }

    /**
     * 获取余额
     * @param nisDemandVO
     */
    private void getBalInfo(NisDemandVO nisDemandVO) {
        if (!EmptyUtils.isEmpty(nisDemandVO)) {
            if (EmptyUtils.isNotEmpty(nisDemandVO.getBankAccId())) {
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(nisDemandVO.getBankAccId());
                if (!EmptyUtils.isEmpty(btBankAccDTO)) {
                    BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
                    if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                        nisDemandVO.setBal(bisAccBalDTO.getBal());
                        nisDemandVO.setAvailBal(bisAccBalDTO.getAvailBal());
                    } else {
                        nisDemandVO.setBal(BigDecimal.ZERO);
                        nisDemandVO.setAvailBal(BigDecimal.ZERO);
                    }
                }
            }
            if(EmptyUtils.isNotEmpty(nisDemandVO.getSuperBankAccId())){
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(nisDemandVO.getSuperBankAccId());
                if (!EmptyUtils.isEmpty(btBankAccDTO)) {
                    BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
                    if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                        nisDemandVO.setSuperBal(bisAccBalDTO.getBal());
                        nisDemandVO.setSuperAvailBal(bisAccBalDTO.getAvailBal());
                    } else {
                        nisDemandVO.setSuperBal(BigDecimal.ZERO);
                        nisDemandVO.setSuperAvailBal(BigDecimal.ZERO);
                    }
                }
            }
        }
    }


    /**
     * 先汇总再生成上级子账户需求
     * @param
     */
    @Transactional
    public void generateDemand(List<String> ids, ICurrentUser user) {
        List<NisDemandVO> list = this.nisDemandDao.getByIdUnion(ids);
        // 先过滤已汇总的数据
        list = list.stream()
                .filter(nisDemandVO -> !CommonConstant.YES.equals(nisDemandVO.getIsSummary()))
                .collect(Collectors.toList());

        if (ObjectUtil.isEmpty(list)) return;

        // 根据单位层级corpLevel分成2个list，corpLevel=4的为一个，小于4的为一个
        Map<Boolean, List<NisDemandVO>> partitionedLists = list.stream()
                .collect(Collectors.partitioningBy(nisDemandVO -> nisDemandVO.getCorpLevel() == 4));

        // 先处理层级为4的数据
        List<NisDemandVO> levelFourList = partitionedLists.get(true); // corpLevel等于4的列表
        List<NisDemand> tempList = null;
        if (ObjectUtil.isNotEmpty(levelFourList)){
            //key规则，需求账号+需求类型+日期+上级账户
            Map<String,List<NisDemandVO>> mapList = new HashMap<>();
            groupSummaryDataList(levelFourList,mapList,new ArrayList<>());
            //循环map生成上级需求单
            tempList = summarizeNisDemand(mapList,false);

            // 在汇总4级单位汇总的明细
            List<NisDemandVO> cList = BeanUtils.copyListProperties(tempList,NisDemandVO.class);
            mapList = new HashMap<>();
            ids = new ArrayList<>();
            groupSummaryDataList(cList,mapList,ids);
            tempList = summarizeNisDemand(mapList,true);


        }

        // 处理层级为3的数据
        List<NisDemandVO> otherList = partitionedLists.get(false);    // corpLevel小于4的列表
        if (ObjectUtil.isNotEmpty(tempList)){
            List<NisDemandVO> tempVos = BeanUtils.copyListProperties(tempList,NisDemandVO.class);
            otherList.addAll(tempVos);
        }
        if (ObjectUtil.isNotEmpty(otherList)){
            //key规则，需求账号+需求类型+日期+上级账户
            Map<String,List<NisDemandVO>> mapList = new HashMap<>();
            groupSummaryDataList(otherList,mapList,new ArrayList<>());
            //循环map生成上级需求单
            summarizeNisDemand(mapList,true);
        }

    }

    /***
     * 汇总需求清单
     * @param mapList
     * @return TODO
     */
    @Transactional
    public List<NisDemand> summarizeNisDemand(Map<String, List<NisDemandVO>> mapList,Boolean flag) {
        // 层级为4的单子把汇总单返回
        List<NisDemand> list = new ArrayList<>();
        for(Map.Entry<String, List<NisDemandVO>> entry: mapList.entrySet()){
            List<NisDemandVO> tempList = entry.getValue();
            List<String> tempidList = tempList.stream().map(NisDemandVO::getId).collect(Collectors.toList());
            NisDemandVO tempNisDemandVO = tempList.get(0);
            if(EmptyUtils.isNotEmpty(tempNisDemandVO.getSuperBankAccId())) {
                //判断是生成上级需求单。还是汇总单 true为汇总单
                Boolean isSummary = false;
                // 原单，单位层级
                Integer corpLevel = tempNisDemandVO.getCorpLevel();
                NisDemand entity = new NisDemand();
                BeanUtils.copyNotNullProperties(tempList.get(0),entity);
                entity.setId("");
                //当需求类型为付款时
                if(NisDemandConstant.DEMANDTYPE_01.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_B));
                    //当需求类型为承兑到期时
                }else if(NisDemandConstant.DEMANDTYPE_02.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_A));
                    //当需求类型为融资到期时
                }else if(NisDemandConstant.DEMANDTYPE_03.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_C));
                }
                //付款单据信息
                entity.setPayBillId(null);
                entity.setPayBillNo(null);
                entity.setPayBillDate(null);
                //合计金额
                BigDecimal sumMoney =  tempList.stream()
                        .map(vo -> vo.getDemandMoney() == null ? BigDecimal.ZERO : vo.getDemandMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                entity.setDemandMoney(sumMoney);
                entity.setDemandAllocationMoney(sumMoney);
                if (corpLevel == 4){
                    //  由于先处理4级的，所以把汇总单返回，用于跟其他3级的数据汇总
                    list.add(entity);
                }

                //账号信息
                BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccById(tempNisDemandVO.getSuperBankAccId());
                //上级需求单。账号信息为上级账号。。上级账号信息为上级账号的上级账号。如果上级账号没有上级账号。则生成需求单为暂存，
                if(EmptyUtils.isNotEmpty(btBankAccDTO.getPrentAccId()) && flag) {
                    if (!EmptyUtils.isEmpty(btBankAccDTO)) {
                        entity.setCorpId(btBankAccDTO.getCorpId());
                        entity.setBankAcc(btBankAccDTO.getBankAcc());
                        entity.setBankAccId(btBankAccDTO.getId());
                        entity.setAccName(btBankAccDTO.getAccName());
                        entity.setBankName(btBankAccDTO.getBankName());
                    }
                    //单位信息
                    CorpDTO corpDTO = corpService.get(btBankAccDTO.getCorpId());
                    if (!EmptyUtils.isEmpty(corpDTO)) {
                        String[] parentIds = corpDTO.getParentIds().split(",");
                        entity.setCorpLevel(parentIds.length);
                    }
                    //上级账号信息
                    BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(btBankAccDTO.getPrentAccId());
                    if (!EmptyUtils.isEmpty(superBankAcc)) {
                        entity.setSuperBankAccId(superBankAcc.getId());
                        entity.setSuperBankAcc(superBankAcc.getBankAcc());
                        entity.setSuperBankName(superBankAcc.getBankName());
                        entity.setSuperAccName(superBankAcc.getAccName());
                        entity.setSuperCorpId(superBankAcc.getCorpId());
                        entity.setStatus(NisDemandConstant.STATUE_CHECK);
                        entity.setSuperCorpId(superBankAcc.getCorpId());

                        if (tempList.size() > 1){
                            isSummary = true;
                            entity.setIsConverge(CommonConstant.YES);
                            entity.setIsSummary(CommonConstant.YES);
                        }

                    } else {
                        continue;
                    }
                }else{
                    if(tempList.size() <= 1){
                        if (!flag){
                            // 处理4级的时候如果4级只有一个单据，需要把ID复制到新对象，否则，下次汇总的时候没办法更新上层单据Id
                            entity.setId(tempList.get(0).getId());
                        }
                        continue;
                    }
                    isSummary = true;
                    entity.setIsConverge(CommonConstant.YES);
                    entity.setIsSummary(CommonConstant.YES);
                }

                //保存需求单
                this.saveEntity(entity);
                // 更新需求清单状态
                this.updateNisDemandSetSuperId(tempidList,entity.getId(),isSummary);

            }

        }
        return list;
    }

    /**
     * 生成下拨付款指令
     * @param
     */
    @Transactional
    public void generateAllocation(String id, ICurrentUser user) {
        NisDemandVO nisDemandVO = this.getByIdUnion(id);
        Boolean isGenerate = true;
        String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
        // 判断是否自动记账
        String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
        if(!EmptyUtils.isEmpty(nisDemandVO)) {
            //生成付款指令
            BisExcDTO bisExcDTO = new BisExcDTO();
            bisExcDTO.setCbsBillId(nisDemandVO.getId());
            bisExcDTO.setCbsBillNo(nisDemandVO.getBillNo());
            bisExcDTO.setCorpId(nisDemandVO.getCorpId());
            bisExcDTO.setReqDate(DateUtils.getDate());
            //付款方信息
            BtBankAccDTO payerAcc = btBankAccService.getBankAccById(nisDemandVO.getSuperBankAccId());
            if(!EmptyUtils.isEmpty(payerAcc)) {
                bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
                bisExcDTO.setPayerAccName(payerAcc.getAccName());
                bisExcDTO.setPayerBank(payerAcc.getBankName());
                bisExcDTO.setPayerCity(payerAcc.getCity());
                bisExcDTO.setPayerProv(payerAcc.getProv());
                bisExcDTO.setCurId(payerAcc.getCurId());
                bisExcDTO.setBifCode(payerAcc.getBifCode());
                bisExcDTO.setPayMoney(nisDemandVO.getDemandAllocationMoney());
            }else{
                isGenerate = false;
            }
            //收款方信息
            BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisDemandVO.getBankAccId());
            if(!EmptyUtils.isEmpty(payerAcc)) {
                bisExcDTO.setPayeeAcc(payeeAcc.getBankAcc());
                bisExcDTO.setPayeeAccName(payeeAcc.getAccName());
                bisExcDTO.setPayeeBank(payeeAcc.getBankName());
                bisExcDTO.setPayeeProv(payeeAcc.getProv());
                bisExcDTO.setPayeeCity(payeeAcc.getCity());
                bisExcDTO.setPayeeBankCode(payeeAcc.getBankCode());
                bisExcDTO.setWishPayday(nisDemandVO.getDemandDate());
            }else {
                isGenerate = false;
            }
            //交易模式 3-对公 4-对私 默认4
            bisExcDTO.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
            bisExcDTO.setIsQuick("0");
            this.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
            bisExcDTO.setPurpose("资金下拨");
            bisExcDTO.setAddWord("资金下拨");
            bisExcDTO.setVoucherFrom(BdlinkConstant.FROM_NIS_DEMAND);
            bisExcDTO.setBusOriType(BdlinkConstant.BUSORITYPE_32);
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
            if (voucherType != null) {
                bisExcDTO.setVoucherTypeId(voucherType.getId());
            }
            bisExcDTO.setVoucherStat(BdlinkConstant.WAIT_FILL);
            if(isGenerate){
                BisExcDTO result = bisExcService.addBisExcReturn(bisExcDTO,user);
                nisDemandVO.setStatus(NisDemandConstant.STATUE_WAIT_ALLOCATION);
                nisDemandVO.setUpdateBy(user.getLoginName());
                nisDemandVO.setUpdateDate(DateUtils.getDate());
                if (!EmptyUtils.isEmpty(result)) {
                    nisDemandVO.setAllocationNo(result.getVoucherNo());
                    nisDemandVO.setAllocationId(result.getId());
                }
                //自动记账
                if (CommonConstant.YES.equals(isAccount) && CommonConstant.YES.equals(isAutoAccount)){
                    submitVoucher(nisDemandVO,bisExcDTO,payerAcc,payeeAcc,user);
                }
                //编辑需求单，控制原状态为0-暂存/1已确认
                Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
                this.updateNisDemandStatus(nisDemandVO, oldStatus);
            }
        }
    }

    /**
     * 生成调拨指令 用于定时任务
     * @param
     */
    @Transactional
    public void generatePos(NisDemandVO nisDemandVO, ICurrentUser user) {
        XxlJobHelper.log("需求单：{}开始生成头寸调拨单。", nisDemandVO.getBillNo());
        if(!EmptyUtils.isEmpty(nisDemandVO)) {
            //设置需求单需求时间为当天
            nisDemandVO.setDemandDate(DateUtils.getDateYMD());
            //收款方信息
            BtBankAccDTO payeeAcc = btBankAccService.getBankAccById(nisDemandVO.getBankAccId());
            BigDecimal allocatedMoney = nisDemandVO.getDemandAllocationMoney();
            //系统参数需求清单是否自动提交头寸调拨
            ConfigDTO aotuSubmitPos = configService.getConfig("allot", "aotuSubmitPos");
            if(!EmptyUtils.isEmpty(payeeAcc)) {
                //判断收款账号的单位
                ConfigDTO configDTO = configService.getConfig("nis-isChaeOpenCorp", payeeAcc.getCorpId());
                //收款账号余额
                BisAccBalDTO payeeBal = getLastBisAccBalBy(payeeAcc.getBankAcc(), payeeAcc.getCurId());
                BigDecimal newAvailBal = payeeBal.getAvailBal().subtract(payeeBal.getLeaveMoney());
                //当收款账号余额小于需求金额才调拨
                if(EmptyUtils.isEmpty(payeeBal) || newAvailBal.compareTo(allocatedMoney) < 0){
                    //差额下拨启动单位才进行自动资金调拨
                    if ("1".equals(configDTO.getValue())) {
                        //获取收款单位的营收账号
                        List<BtBankAccDTO> btBankAccDTOList = listBankAcc1004(payeeAcc.getCorpId(),payeeAcc.getCorpName());
                        // 生成调拨单数量
                        int count = 0;
                        if (EmptyUtils.isNotEmpty(btBankAccDTOList)) {
                            for (BtBankAccDTO acc : btBankAccDTOList) {
                                if (allocatedMoney.compareTo(new BigDecimal("0")) <= 0 ){
                                    break;
                                }
                                //付款账号和收款账号不相等
                                if (!acc.getBankAcc().equals(payeeAcc.getBankAcc())) {
                                    //付款账号余额
                                    //BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(acc.getBankAcc(), acc.getCurId());
                                    if (!EmptyUtils.isEmpty(acc.getAvailBal())) {
                                        //生成调拨单
                                        NisAllocationReq entity = new NisAllocationReq();
                                        //付款账号
                                        BtBankAccDTO payerAcc = btBankAccService.getBankAccById(acc.getId());
                                        String billNo = SequenceSingleton.getSequence(SequenceEnum.NIS_ALLOCATION_REQ); //生成单据号
                                        BigDecimal money = BigDecimal.ZERO;
                                        entity.setBillNo(billNo);
                                        entity.setType(13);
                                        entity.setCorpId(payeeAcc.getCorpId());

                                        entity.setPayerAccId(payerAcc.getId());
                                        entity.setPayerAcc(payerAcc.getBankAcc());
                                        entity.setPayerAccName(payerAcc.getAccName());

                                        entity.setPayeeAcc(payeeAcc.getBankAcc());
                                        entity.setPayeeAccName(payeeAcc.getAccName());
                                        entity.setPayeeAccId(payeeAcc.getId());

                                        entity.setCurId(payeeAcc.getCurId());
                                        //如果营收账号余额大于需求单下拨金额，直接一个账号拨全
                                        BigDecimal availBal = acc.getAvailBal();
                                        availBal = availBal.subtract(new BigDecimal("1000"));
                                        // 减去营收账号得留底
                                        //BigDecimal leaveMoney = nisDemandDao.getLeaveMoney(acc.getBankAcc());
                                        BigDecimal leaveMoney = acc.getLeaveMoney();
                                        availBal = availBal.subtract(leaveMoney);
                                        if (availBal.compareTo(allocatedMoney) >= 0) {
                                            money = allocatedMoney;
                                        } else {
                                            // 1) 本身可用余额大于(1000+营收账号留底)金额
                                            BigDecimal tempMoney = new BigDecimal("1000").add(leaveMoney);
                                            if (acc.getAvailBal().compareTo(tempMoney)>0){
                                                money = availBal;
                                            }else{
                                                continue;
                                            }
                                        }
                                        allocatedMoney = allocatedMoney.subtract(money);
                                        entity.setMoney(money);
                                        entity.setWishPayday(DateUtils.getDateYMD());
                                        entity.setIsQuick("0");
                                        entity.setCreateBy(user.getLoginName());
                                        entity.setCreateDate(DateUtils.getDate());
                                        //资金池标识 0-非资金池 1-资金池 默认0
                                        entity.setPoolSign(AllocationConstant.POOL_SIGN_0);
                                        entity.setStatus(0);
                                        if (CommonConstant.YES.equals(aotuSubmitPos.getValue())) {
                                            entity.setStatus(11);
                                        }
                                        this.setDifBankAndAreaSign(entity, payerAcc, payeeAcc);
                                        entity.setPurpose("往来款");
                                        entity.setBillFrom(AllocationConstant.BILL_FROM_5);
                                        entity.setErpId(nisDemandVO.getId());
                                        //保存调拨单
                                        //nisAllocationReqService.saveNisAllocationReqPosForJob(entity, user);
                                        nisAllocationReqService.save(entity);
                                        if (CommonConstant.YES.equals(aotuSubmitPos.getValue())){
                                            nisAllocationReqService.approvePassEnd(entity.getId(), true, user);
                                        }
                                        count +=1;
                                    }
                                }
                            }
                        }
                        if (count > 0){
                            // 修改需求单是否生成头寸调拨，并且更新需求单生成头寸调拨单数量
                            nisDemandDao.updatenisDemandIsPos(nisDemandVO.getId(),CommonConstant.YES,count);
                        }
                    }
                }else{
                    XxlJobHelper.log("需求单：{}账号余额：{}大于需求金额：{}不进行头寸。", nisDemandVO.getBillNo(),newAvailBal,nisDemandVO.getDemandAllocationMoney());
                }
            }
        }
    }

    /***
     * 生成下拨付款指令 用于定时任务
     * @param nisDemandVO
     * @param user
     * @param type nis-付款下拨，bill-票据下拨，cms-融资下拨
     * @param policyAccounts
     * @return TODO
     */
    @Transactional
    public void generateAllocation(NisDemandVO nisDemandVO, ICurrentUser user,String type,List<BisGatherPolicyAccountVO> policyAccounts) {
        Boolean isGenerate = true;
        String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
        // 判断是否自动记账
        String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();
        BtBankAccDTO payerAcc = null;
        BtBankAccDTO payeeAcc = null;
        if(!EmptyUtils.isEmpty(nisDemandVO)) {
            //生成付款指令
            BisExcDTO bisExcDTO = new BisExcDTO();
            bisExcDTO.setCbsBillId(nisDemandVO.getId());
            bisExcDTO.setCbsBillNo(nisDemandVO.getBillNo());
            bisExcDTO.setCorpId(nisDemandVO.getCorpId());
            bisExcDTO.setReqDate(DateUtils.getDate());
            //付款方信息
            payerAcc = btBankAccService.getBankAccById(nisDemandVO.getSuperBankAccId());
            if(!EmptyUtils.isEmpty(payerAcc)) {
                bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
                bisExcDTO.setPayerAccName(payerAcc.getAccName());
                bisExcDTO.setPayerBank(payerAcc.getBankName());
                bisExcDTO.setPayerCity(payerAcc.getCity());
                bisExcDTO.setPayerProv(payerAcc.getProv());
                bisExcDTO.setCurId(payerAcc.getCurId());
                bisExcDTO.setBifCode(payerAcc.getBifCode());
            }else{
                isGenerate = false;
                XxlJobHelper.log("需求单【{}】中止，付款账号【{}】信息不存在！", nisDemandVO.getBillNo(), nisDemandVO.getSuperBankAcc());
            }
            //收款方信息
            payeeAcc = btBankAccService.getBankAccById(nisDemandVO.getBankAccId());
            if(!EmptyUtils.isEmpty(payeeAcc)) {
                //判断收款账号的单位
                ConfigDTO configDTO = configService.getConfig("nis-isChaeOpenCorp",payeeAcc.getCorpId());
                //差额下拨启动单位
                if("1".equals(configDTO.getValue())) {
                    bisExcDTO.setPayeeAcc(payeeAcc.getBankAcc());
                    bisExcDTO.setPayeeAccName(payeeAcc.getAccName());
                    bisExcDTO.setPayeeBank(payeeAcc.getBankName());
                    bisExcDTO.setPayeeProv(payeeAcc.getProv());
                    bisExcDTO.setPayeeCity(payeeAcc.getCity());
                    bisExcDTO.setPayeeBankCode(payeeAcc.getBankCode());
                    bisExcDTO.setWishPayday(nisDemandVO.getDemandDate());
                }else{
                    isGenerate = false;
                    XxlJobHelper.log("需求单【{}】中止，单位【{}】差额下拨未启用！", nisDemandVO.getBillNo(), payeeAcc.getCorpName());
                }
            }else {
                isGenerate = false;
                XxlJobHelper.log("需求单【{}】中止，收款账号【{}】信息不存在！", nisDemandVO.getBillNo(), nisDemandVO.getBankAcc());

            }
            //交易模式 3-对公 4-对私 默认4
            bisExcDTO.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
            bisExcDTO.setIsQuick("0");
            this.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
            bisExcDTO.setPurpose("需求资金下拨");
            bisExcDTO.setAddWord("需求资金下拨");
            bisExcDTO.setVoucherFrom(BdlinkConstant.FROM_NIS_DEMAND);
            bisExcDTO.setBusOriType(BdlinkConstant.BUSORITYPE_32);
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
            if (voucherType != null) {
                bisExcDTO.setVoucherTypeId(voucherType.getId());
            }
            bisExcDTO.setVoucherStat(BdlinkConstant.PRE_SEND);
            // 需要下拨
            if(isGenerate){
                // 下拨金额
                BigDecimal allocatedMoney = nisDemandVO.getDemandAllocationMoney();
                // 根据付款账号可用余额和策略配置的“是否允许下拨”分为下面2种情况：
                BisAccBalDTO payerAccBal = bisAccBalService.getLastBisAccBalBy(payerAcc.getBankAcc(), payerAcc.getCurId());
                BtBankAccDTO finalPayerAcc = payerAcc;
                BisGatherPolicyAccountVO policyAccountVO = policyAccounts.stream().filter(policyAccount ->
                        finalPayerAcc.getBankAcc().equals(policyAccount.getBankAcc())).findFirst().orElse(null);
                // 当前资金中心账户“可用余额”小于需要下拨的金额时或者当前资金中心账户不允许下拨时：
                if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(nisDemandVO.getDemandAllocationMoney()) < 0 )
                        || (ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.NO.equals(policyAccountVO.getIsDown()))){
                    boolean flag = false;
                    BtBankAccDTO bankAccInfo = null;
                    BisAccBalDTO tempAccBal = null;
                    for (BisGatherPolicyAccountVO policyAccount : policyAccounts){
                        if (CommonConstant.NO.equals(policyAccount.getIsDown())){
                            continue;
                        }
                        bankAccInfo = btBankAccService.getBankAccInfo(policyAccount.getBankAcc());
                        bankAccInfo.setCurId(BtCurrencyConstant.RMBID);
                        tempAccBal = bisAccBalService.getLastBisAccBalBy(bankAccInfo.getBankAcc(), bankAccInfo.getCurId());
                        if (ObjectUtil.isNotEmpty(tempAccBal) && tempAccBal.getAvailBal().compareTo(nisDemandVO.getDemandAllocationMoney()) >= 0){
                            flag = true;
                            payerAcc = bankAccInfo;
                            payerAccBal = tempAccBal;
                            break;
                        }
                    }
                    // 修改付款方信息
                    if (flag){
                        bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
                        bisExcDTO.setPayerAccName(payerAcc.getAccName());
                        bisExcDTO.setPayerBank(payerAcc.getBankName());
                        bisExcDTO.setPayerCity(payerAcc.getCity());
                        bisExcDTO.setPayerProv(payerAcc.getProv());
                        bisExcDTO.setCurId(payerAcc.getCurId());
                        bisExcDTO.setBifCode(payerAcc.getBifCode());
                        this.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
                    }else{
                        isGenerate = false;
                        XxlJobHelper.log("需求单【{}】中止，资金中心账号的可用余额都小于需要下拨的金额！", nisDemandVO.getBillNo());
                        //发送消息
                        Map<String, Object> map = new HashMap<>();
                        map.put("billNo", nisDemandVO.getBillNo());
                        map.put("payer", bisExcDTO.getPayerAccName());
                        map.put("payerAcc", bisExcDTO.getPayerAcc());
                        map.put("payerBank", bisExcDTO.getPayerBank());
                        map.put("payee", bisExcDTO.getPayeeAccName());
                        map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                        map.put("payeeBank", bisExcDTO.getPayeeBank());
                        map.put("amt", nisDemandVO.getDemandAllocationMoney());
                        String tag = "";
                        if ("nis".equals(type)) tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_DIALING_FAILURE;
                        if ("cms".equals(type)) tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_CMS_FAILURE;
                        if ("bill".equals(type)) tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_BILL_FAILURE;
                        notificationService.send(nisDemandVO.getId(),"", tag, map);
                    }
                }
                // 当资金中心账户“可用余额”大于需要下拨的金额时且资金中心账号为允许下拨的账号时：
                if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(nisDemandVO.getDemandAllocationMoney()) >= 0 )
                        && (ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.YES.equals(policyAccountVO.getIsDown()))){
                    CorpDTO corpDTO = corpService.get(payeeAcc.getCorpId());
                    // 获取股权:0001全资；0002控股
                    if (ObjectUtil.isNotEmpty(corpDTO.getEquityMaintenanceId())){
                        EquityMaintenanceDTO equityMaintenanceDTO = equityMaintenanceService.queryById(corpDTO.getEquityMaintenanceId());
                        // 是否差额下拨
                        boolean differentSign = false;
                        if ("0001".equals(equityMaintenanceDTO.getEquityCode())){
                            // 除了付款类的，融资，票据直接下拨
                            if ("nis".equals( type)){
                                // 查询收款账号对应的内部账号 counterAccId
                                FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(payeeAcc.getCounterAccId());
                                if (ObjectUtil.isNotEmpty(fcOnlineLedgerDTO)){
                                    // 下拨金额：allocatedMoney
                                    // 根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                                    BigDecimal checkBalance =fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney());
                                    if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                        // 不允许透支
                                        // 1）内部账号余额大于等于下拨余额时，自动下拨

                                        // 2）内部账号余额小下拨余额时，将下拨金额调整成当前内部账号余额，
                                        // 如：下拨金额为20000，内部账号余额为15000，则下拨指令的金额调整成15000进行下拨；
                                        // （增加企业微信余额不足提醒，提醒人员新增界面进行配置）
                                        if (checkBalance.compareTo(allocatedMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                            allocatedMoney = checkBalance;
                                            differentSign = true;
                                        }
                                        // 如果内部账号余额小于等于0，则不下拨
                                        if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                            isGenerate = false;
                                            XxlJobHelper.log("需求单【{}】中止，账号【{}】内部账户可用余额为：{}", nisDemandVO.getBillNo(),payeeAcc.getBankAcc(),checkBalance);
                                        }
                                    }else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                        // 受限透支 加上透支金额
                                        checkBalance = checkBalance.add(fcOnlineLedgerDTO.getOverdraftMoney());
                                        // 1） 内部账号余额大于等于下拨余额时，自动下拨

                                        // 2）内部账号余额小于下拨余额时，将下拨金额调整成当前内部账号余额，
                                        if (checkBalance.compareTo(allocatedMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                            allocatedMoney = checkBalance;
                                            differentSign = true;
                                        }
                                        // 如果内部账号余额小于等于0，则不下拨
                                        if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                            isGenerate = false;
                                            XxlJobHelper.log("需求单【{}】中止，账号【{}】内部账户可用余额为：{}", nisDemandVO.getBillNo(),payeeAcc.getBankAcc(),checkBalance);
                                        }
                                    }
                                    // InnerAccountConstant.OVERDRAFT_SIGN_D
                                    // 3) 不受限透支，直接下拨
                                }else{
                                    isGenerate = false;
                                    XxlJobHelper.log("需求单【{}】中止，账号【{}】未关联内部账号不满足下拨条件", nisDemandVO.getBillNo(),payeeAcc.getBankAcc());
                                }
                            }

                        }else if ("0002".equals(equityMaintenanceDTO.getEquityCode()) ){
                            // 资金归集管理协议是否签订，未签订不做下拨，已签订再根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                            BisGatherPolicyAgreementVO policyAgreementVO = policyAgreementService.getByCorpId(payeeAcc.getCorpId());
                            if (ObjectUtil.isNotEmpty(policyAgreementVO) && CommonConstant.YES.equals(policyAgreementVO.getIsAgreement())){
                                // 除了付款类的，融资，票据直接下拨
                                if ("nis".equals(type)){
                                    // 查询收款账号对应的内部账号 counterAccId
                                    FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(payeeAcc.getCounterAccId());
                                    if (ObjectUtil.isNotEmpty(fcOnlineLedgerDTO)){
                                        // 下拨金额：allocatedMoney
                                        // 根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                                        BigDecimal checkBalance =fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney());
                                        if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                            // 不允许透支
                                            // 1）内部账号余额大于等于下拨余额时，自动下拨

                                            // 2）内部账号余额小下拨余额时，将下拨金额调整成当前内部账号余额，
                                            // 如：下拨金额为20000，内部账号余额为15000，则下拨指令的金额调整成15000进行下拨；
                                            // （增加企业微信余额不足提醒，提醒人员新增界面进行配置）
                                            if (checkBalance.compareTo(allocatedMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                                allocatedMoney = checkBalance;
                                                differentSign = true;
                                            }
                                            // 如果内部账号余额小于等于0，则不下拨
                                            if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                                isGenerate = false;
                                                XxlJobHelper.log("需求单【{}】中止，账号【{}】内部账户可用余额为：{}", nisDemandVO.getBillNo(),payeeAcc.getBankAcc(),checkBalance);
                                            }
                                        }else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                            // 受限透支 加上透支金额
                                            checkBalance = checkBalance.add(fcOnlineLedgerDTO.getOverdraftMoney());
                                            // 1） 内部账号余额大于等于下拨余额时，自动下拨

                                            // 2）内部账号余额小于下拨余额时，将下拨金额调整成当前内部账号余额，
                                            if (checkBalance.compareTo(allocatedMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                                allocatedMoney = checkBalance;
                                                differentSign = true;
                                            }
                                            // 如果内部账号余额小于等于0，则不下拨
                                            if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                                isGenerate = false;
                                                XxlJobHelper.log("需求单【{}】中止，账号【{}】内部账户可用余额为：{}", nisDemandVO.getBillNo(),payeeAcc.getBankAcc(),checkBalance);
                                            }
                                        }
                                        // InnerAccountConstant.OVERDRAFT_SIGN_D
                                        // 3) 不受限透支，直接下拨
                                    }else{
                                        isGenerate = false;
                                        XxlJobHelper.log("需求单【{}】中止，账号【{}】未关联内部账号不满足下拨条件", nisDemandVO.getBillNo(),payeeAcc.getBankAcc());
                                    }
                                }
                            }else{
                                isGenerate = false;
                                XxlJobHelper.log("需求单【{}】中止，单位【{}】未签订资金归集管理协议", nisDemandVO.getBillNo(),corpDTO.getName());
                            }
                        }else{
                            isGenerate = false;
                            XxlJobHelper.log("需求单【{}】中止，单位【{}】的股权类型不满足下拨条件", nisDemandVO.getBillNo(),corpDTO.getName());
                        }
                        //  差额下拨发送消息
                        if (differentSign ){
                            // 发送消息
                            Map<String, Object> map = new HashMap<>();
                            map.put("billNo", nisDemandVO.getBillNo());
                            map.put("payer", bisExcDTO.getPayerAccName());
                            map.put("payerAcc", bisExcDTO.getPayerAcc());
                            map.put("payerBank", bisExcDTO.getPayerBank());
                            map.put("payee", bisExcDTO.getPayeeAccName());
                            map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                            map.put("payeeBank", bisExcDTO.getPayeeBank());
                            map.put("downAmt", allocatedMoney);
                            map.put("amt", nisDemandVO.getDemandAllocationMoney());
                            map.put("diffAmt", nisDemandVO.getDemandAllocationMoney().subtract(allocatedMoney));
                            String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_FAILURE_DIFFERENCE;
                            notificationService.send(nisDemandVO.getId(),"", tag, map);
                        }

                    }else{
                        isGenerate = false;
                        XxlJobHelper.log("需求单【{}】中止，单位【{}】的股权维护信息为空", nisDemandVO.getBillNo(),corpDTO.getName());
                    }
                    // 如果是付款类重新计算下拨金额
                    if ("nis".equals(type)){
                        allocatedMoney = this.getAllocatedMoney(payeeAcc,allocatedMoney, nisDemandVO.getBillNo());
                    }
                }
                //1.当计算出来的下拨付款指令金额为0或者负数时，不生成下拨指令；2.只有差额下拨启用单位配置列表中是“是”的才产生指令。
                if(allocatedMoney.compareTo(BigDecimal.ZERO) > 0 && isGenerate) {
                    bisExcDTO.setPayMoney(allocatedMoney);
                    BisExcDTO result = bisExcService.addBisExcReturn(bisExcDTO, user);
                    nisDemandVO.setStatus(NisDemandConstant.STATUE_WAIT_ALLOCATION);
                    nisDemandVO.setUpdateBy(user.getLoginName());
                    nisDemandVO.setUpdateDate(DateUtils.getDate());
                    if (!EmptyUtils.isEmpty(result)) {
                        nisDemandVO.setAllocationNo(result.getVoucherNo());
                        nisDemandVO.setAllocationId(result.getId());
                        XxlJobHelper.log("需求单【{}】生成下拨指令成功【{}】", nisDemandVO.getBillNo(),result.getVoucherNo());
                    }
                    //自动记账
                    if (CommonConstant.YES.equals(isAccount) && CommonConstant.YES.equals(isAutoAccount)) {
                        XxlJobHelper.log("需求单【{}】开始记账。。。", nisDemandVO.getBillNo());
                        submitVoucher(nisDemandVO, bisExcDTO, payerAcc, payeeAcc, user);
                    }
                    //编辑需求单，控制原状态为0-暂存/1已确认
                    Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
                    this.updateNisDemandStatus(nisDemandVO, oldStatus);
                }else{
                    XxlJobHelper.log("需求单【{}】中止，下拨金额或者条件不满足！", nisDemandVO.getBillNo());
                }
            }
        }
    }

    /**
     * 计算下拨金额
     * @param bankAccDTO 收款账号
     * @param money 汇总的需求下拨金额
     * @return
     */
    private BigDecimal getAllocatedMoney(BtBankAccDTO bankAccDTO,BigDecimal money,String billNo){
        BigDecimal result = money;
        XxlJobHelper.log("需求单【{}】开始计算需要下拨金额开始，需求单下拨金额为：{}",billNo,money);
        BigDecimal sum = BigDecimal.ZERO;
        //获取收款单位的营收账号
        List<BtBankAccDTO> btBankAccDTOList = listBankAcc1004(bankAccDTO.getCorpId(),bankAccDTO.getCorpName());
        if(EmptyUtils.isNotEmpty(btBankAccDTOList)){
            for(BtBankAccDTO acc : btBankAccDTOList){
                BisAccBalDTO bisAccBalDTO =  bisAccBalService.getLastBisAccBalBy(acc.getBankAcc(),acc.getCurId());
                if(!EmptyUtils.isEmpty(bisAccBalDTO)) {
                    sum = sum.add(bisAccBalDTO.getAvailBal());
                    XxlJobHelper.log("需求单【{}】获取收款单位的营收账号【{}】的可用余额为：{}",billNo,acc.getBankAcc(),bisAccBalDTO.getAvailBal());

                }
                // 加上营收账号得留底
                BigDecimal leaveMoney = nisDemandDao.getLeaveMoney(acc.getBankAcc());
                result = result.add(leaveMoney);
                XxlJobHelper.log("需求单【{}】下拨金额[{}]+营收账号留底金额[{}]=[{}]",billNo,money,leaveMoney,result);

            }
            XxlJobHelper.log("需求单【{}】下拨金额[{}]-营收账号可用金额合计[{}]",billNo,result,sum);
            result = result.subtract(sum);
            XxlJobHelper.log("需求单【{}】下拨金额-营收账号可用金额合计=[{}]",billNo,result);
            // （营收账号数量*1000）
            XxlJobHelper.log("需求单【{}】下拨金额[{}]+(营收账号数量[{}]*1000)",billNo,result,btBankAccDTOList.size());
            result = result.add(BigDecimal.valueOf(btBankAccDTOList.size()).multiply(BigDecimal.valueOf(1000)));
            XxlJobHelper.log("需求单【{}】下拨金额+(营收账号数量*1000)=[{}]",billNo,result);
        }
        //如果账号是营收账号
        if(EmptyUtils.isNotEmpty(bankAccDTO.getAccUseCode()) && bankAccDTO.getAccUseCode().contains("1004")){

        }else{
            //如果收款账号不是营收账号还要减去本账号的余额
            BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(bankAccDTO.getBankAcc(),bankAccDTO.getCurId());
            if(!EmptyUtils.isEmpty(bisAccBalDTO)) {
                XxlJobHelper.log("需求单【{}】不是营收账号减去本账号的余额；下拨金额[{}]-账号可用余额[{}]",billNo,result,bisAccBalDTO.getAvailBal());
                result = result.subtract(bisAccBalDTO.getAvailBal());
                XxlJobHelper.log("需求单【{}】不是营收账号减去本账号的余额；下拨金额-账号可用余额=[{}]",billNo,result);

            }

            // 加上需求账号留底金额
            BigDecimal leaveMoney = nisDemandDao.getLeaveMoney(bankAccDTO.getBankAcc());
            XxlJobHelper.log("需求单【{}】下拨金额[{}]+需求账号留底金额[{}]",billNo,result,leaveMoney);
            result = result.add(leaveMoney);
            XxlJobHelper.log("需求单【{}】下拨金额+需求账号留底金额=[{}]",billNo,result);
        }

        return result;
    }

    public void setDifBankAndAreaSign(BisExcDTO bisExcDTO,BtBankAccDTO payerAcc,BtBankAccDTO payeeAcc){
        if (!EmptyUtils.isEmpty(payerAcc) && !EmptyUtils.isEmpty(payeeAcc)) {
            //跨行标志
            if (StringUtils.isEmpty(payerAcc.getBankCode()) || StringUtils.isEmpty(payeeAcc.getBankCode())) {
                bisExcDTO.setDifBank(NetbankConstant.DIFBANK_0);
            } else if (payerAcc.getBankCode().substring(0, 3).equals(payeeAcc.getBankCode().substring(0, 3))) {
                if ("313".equals(payerAcc.getBankCode().substring(0, 3)) || "402".equals(payerAcc.getBankCode().substring(0, 3))
                        || "314".equals(payerAcc.getBankCode().substring(0, 3))) {
                    if (StringUtils.isNotBlank(payerAcc.getBankNamePrefix()) && payeeAcc.getBankCode().indexOf(payerAcc.getBankNamePrefix()) != -1) {
                        bisExcDTO.setDifBank(NetbankConstant.DIFBANK_0);
                    } else {
                        bisExcDTO.setDifBank(NetbankConstant.DIFBANK_1);
                    }
                } else {
                    bisExcDTO.setDifBank(NetbankConstant.DIFBANK_0);
                }
            } else {
                bisExcDTO.setDifBank(NetbankConstant.DIFBANK_1);
            }
            //地域标志
            if (StringUtils.isEmpty(payerAcc.getProv()) || StringUtils.isEmpty(payeeAcc.getProv())) {
                bisExcDTO.setAreaSign(NetbankConstant.AREASIGN_0);
            } else if (payerAcc.getProv().equals(payeeAcc.getProv()) && payerAcc.getCity().equals(payeeAcc.getCity())) {
                bisExcDTO.setAreaSign(NetbankConstant.AREASIGN_0);
            } else {
                bisExcDTO.setAreaSign(NetbankConstant.AREASIGN_1);
            }
        }
    }

    private void setDifBankAndAreaSign(NisAllocationReq entity,BtBankAccDTO payerAcc,BtBankAccDTO payeeAcc){
        if (!EmptyUtils.isEmpty(payerAcc) && !EmptyUtils.isEmpty(payeeAcc)) {
            //跨行标志
            if (StringUtils.isEmpty(payerAcc.getBankCode()) || StringUtils.isEmpty(payeeAcc.getBankCode())) {
                entity.setDifBank(NetbankConstant.DIFBANK_0);
            } else if (payerAcc.getBankCode().substring(0, 3).equals(payeeAcc.getBankCode().substring(0, 3))) {
                if ("313".equals(payerAcc.getBankCode().substring(0, 3)) || "402".equals(payerAcc.getBankCode().substring(0, 3))
                        || "314".equals(payerAcc.getBankCode().substring(0, 3))) {
                    if (StringUtils.isNotBlank(payerAcc.getBankNamePrefix()) && payeeAcc.getBankCode().indexOf(payerAcc.getBankNamePrefix()) != -1) {
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
            if (StringUtils.isEmpty(payerAcc.getProv()) || StringUtils.isEmpty(payeeAcc.getProv())) {
                entity.setAreaSign(NetbankConstant.AREASIGN_0);
            } else if (payerAcc.getProv().equals(payeeAcc.getProv()) && payerAcc.getCity().equals(payeeAcc.getCity())) {
                entity.setAreaSign(NetbankConstant.AREASIGN_0);
            } else {
                entity.setAreaSign(NetbankConstant.AREASIGN_1);
            }
        }
    }

    /***
     * 获取营收账号
     * @param corpId
     * @return TODO
     */
    private List<BtBankAccDTO> listBankAcc1004(String corpId,String corpName){
        return queryBankAcc1004(Arrays.asList(corpId));
    }
    /***
     * 获取单位营收账号
     * @param corpIds
     * @return TODO
     */
    public Map<String, List<BtBankAccDTO>> listMapBankAcc1004(List<String> corpIds){
        Map<String, List<BtBankAccDTO>> resultMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(corpIds)){
            List<BtBankAccDTO> btBankAccDTOS = queryBankAcc1004(corpIds);
            if (ObjectUtil.isNotEmpty(btBankAccDTOS)){
                // 根据corpId进行分组 组成map
                resultMap = btBankAccDTOS.stream().collect(Collectors.groupingBy(BtBankAccDTO::getCorpId));
            }
        }
        return resultMap;
    }


    private List<BtBankAccDTO> queryBankAcc1004(List<String> corpIds) {
        List<BtBankAccDTO> resultList = new ArrayList<>();
        List<BtBankAccDTO> btBankAccDTOS = btBankAccService.listBankAcc1004(corpIds);
        XxlJobHelper.log("查询到的营收账号数量为：{}",btBankAccDTOS.size());
        // 判断
        if (ObjectUtil.isNotEmpty(btBankAccDTOS)){
            // 获取所有银行账号
            List<String> bankAccs = btBankAccDTOS.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
            // 把bankAccs 转为字符串
            XxlJobHelper.log("查询到的营收账号为：{}",String.join(",", bankAccs));
            // 获取所有币种
            List<String> curIds = btBankAccDTOS.stream().map(BtBankAccDTO::getCurId).distinct().collect(Collectors.toList());
            XxlJobHelper.log("查询到的营收账号的币种为：{}",String.join(",", curIds));
            List<BtBankAccBalVO> btBankAccBalVOS = bisAccBalService.queryBankAccBalanceById(bankAccs, curIds);
            Map<String, BigDecimal> balMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(btBankAccBalVOS)){
                for (BtBankAccBalVO vo : btBankAccBalVOS){
                    if (ObjectUtil.isEmpty(vo.getBal())){
                        // 保证bal不为空null
                        vo.setBal(BigDecimal.ZERO);
                    }
                }
                balMap = btBankAccBalVOS.stream()
                        .filter(vo -> vo != null && vo.getBankAcc() != null && vo.getCurId() != null) // 过滤 null
                        .collect(Collectors.toMap(
                                vo -> vo.getBankAcc() + "-" + vo.getCurId(),
                                BtBankAccBalVO::getBal
                        ));
            }

            // 判断可用余额是否大于(留底金额+1000)
            for (BtBankAccDTO btBankAccDTO : btBankAccDTOS){
                String key = btBankAccDTO.getBankAcc() + "-" + btBankAccDTO.getCurId();
                if (balMap.containsKey(key)){
                    BigDecimal leaveMoney = nisDemandDao.getLeaveMoney(btBankAccDTO.getBankAcc());
                    // 留底+1000手续费
                    BigDecimal compareMoney = leaveMoney.add(new BigDecimal("1000"));
                    if (balMap.get( key).compareTo(compareMoney) > 0){
                        btBankAccDTO.setAvailBal(balMap.get(key));
                        btBankAccDTO.setLeaveMoney(leaveMoney);
                        resultList.add(btBankAccDTO);
                    }
                }
            }
        }

        return resultList;
    }

    /**
     * 自动记账
     * @param nisDemandVO
     * @param bisExc
     * @param user
     */
    @Transactional
    public void submitVoucher(NisDemandVO nisDemandVO,BisExcDTO bisExc,BtBankAccDTO payerAcc,BtBankAccDTO payeeAcc,ICurrentUser user) {
        if(EmptyUtils.isNotEmpty(payeeAcc.getCounterAccId())) {
            NisAccbookVO accBook = new NisAccbookVO();
            accBook.setBillNo(nisDemandVO.getBillNo());
            accBook.setBillId(nisDemandVO.getId());
            accBook.setFcAcc(innerAccountService.getByBlnctrAcc(payeeAcc.getCounterAccId()));
            accBook.setBankAcc(payerAcc);
            accBook.setPayeeAcc(bisExc.getPayeeAcc());
            accBook.setVoucherTypeId(bisExc.getVoucherTypeId());
            accBook.setWishPayday(bisExc.getWishPayday());
            accBook.setIsQuick(bisExc.getIsQuick());
            accBook.setPurpose(bisExc.getPurpose());
            accBook.setPayMoney(bisExc.getPayMoney());
            accBook.setBusOriType(bisExc.getBusOriType());
            accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_CNT_DOWN_PAYMENT.get());
            String voucherNo = nisAccbookService.submitVoucher(accBook, user); //自动记账
            //记录凭证号
            nisDemandVO.setVoucherNo(voucherNo);
            //更新记账标识
            nisDemandVO.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        }
    }

    /**
     * 查询所有待汇总的需求单
     * @return
     */
    public List<NisDemandVO> listByAutoSummary(List<String> demandTypes){
        return nisDemandDao.listByAutoSummary(demandTypes);
    }


    /**
     * 更新汇总标志
     * @param ids
     * @return
     */
    @Transactional
    public int updateSummary(List<String> ids){
        return nisDemandDao.updateSummary(ids);
    }

    /**
     * 查询待生成指令的需求单 付款类
     * @return
     */
    public List<NisDemandVO> listByAutoAllocationPay(){
        return nisDemandDao.listByAutoAllocationPay();
    }

    /**
     * 查询待生成指令的需求单 付款类
     * @return
     */
    public List<NisDemandVO> listByAutoAllocationPayForPos(){
        return nisDemandDao.listByAutoAllocationPayForPos();
    }

    /**
     * 查询待生成下拨指令的需求单
     * @return
     */
    public List<NisDemandVO> listByAutoAllocation(String demandType){
        return nisDemandDao.listByAutoAllocation(demandType);
    }

    /**
     * 根据ids 更新上级id
     * @param ids
     * @param superId
     */
    @Transactional
    public int updateNisDemandSetSuperId(List<String> ids, String superId,Boolean isSummary) {
        int count = nisDemandDao.updateNisDemandSetSuperId(ids,superId,isSummary);
        if(isSummary){
            //因为上级因汇总作废了，则更新上级需求单为上级的上级。
            int temp = nisDemandDao.updateNisDemandSetSuperIdbySuperId(ids,superId);
        }
        return count;
    }

    /**
     * 根据ids 更新上级id
     * @param id
     * @param superId
     */
    @Transactional
    public int updateNisDemandSetSuperId(String id, String superId) {
        return nisDemandDao.updateNisDemandSetSuperId(id,superId);
    }

    /**
     * 根据单据id获取需求单
     * @param payBillId
     * @return
     */
    public List<NisDemandVO>  listByPayBillId(String payBillId){
        return nisDemandDao.listByPayBillId(payBillId);
    }

    /**
     * 根据单据id获取需求单 不带状态
     * @param payBillId
     * @return
     */
    public NisDemandVO listByPayBillIdNoStatus(String payBillId){
        List<NisDemandVO> list = nisDemandDao.listByPayBillIdNoStatus(payBillId);
        NisDemandVO nisDemandVO = null;
        if(EmptyUtils.isNotEmpty(list) && list.size() > 0){
            nisDemandVO = list.get(0);
        }
        return nisDemandVO;
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisDemandVO> getDataTablesUnionQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.NETBANK_NISDEMANDQUERY_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        /*String eqi_status = (String)dataTablesInput.getFormdata().get("eqi_status");
        //已下拨和下拨失败取指令状态
        if("3".equals(eqi_status) || "4".equals(eqi_status)){
            dataTablesInput.getFormdata().remove("eqi_status");
            //已下拨
            if("3".equals(eqi_status)){
                sql.append(" and b.voucher_Stat in ('"+ com.bytter.core.constant.BdlinkConstant.SUCCESS+"')");
                //下拨失败
            }else if("4".equals(eqi_status)){
                sql.append(" and b.voucher_Stat in ('" + com.bytter.biz.bdlink.api.constant.BdlinkConstant.BANK_FAILED + "','" + com.bytter.biz.bdlink.api.constant.BdlinkConstant.INTERFACE_FAILED + "')");
            }
        }else if("2".equals(eqi_status)){
            sql.append(" and b.voucher_Stat not in ('" + com.bytter.biz.bdlink.api.constant.BdlinkConstant.BANK_FAILED + "','" + com.bytter.biz.bdlink.api.constant.BdlinkConstant.INTERFACE_FAILED + "','"+ com.bytter.core.constant.BdlinkConstant.SUCCESS+"')");
        }*/
        DataTablesOutput<NisDemandVO> dataTablesOutput = nisDemandDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<NisDemandVO> list = dataTablesOutput.getData();
        if(EmptyUtils.isNotEmpty(list)){
            //账号
            Map<String,BtBankAccDTO> bankMap = new HashMap<>();
            //余额
            Map<String,BisAccBalDTO> balMap = new HashMap<>();
            for(NisDemandVO vo : list){
                BtBankAccDTO btBankAccDTO;
                BisAccBalDTO bisAccBalDTO;
                if(!bankMap.containsKey(vo.getBankAccId())){
                    btBankAccDTO  = btBankAccService.getBankAccById(vo.getBankAccId());
                    bankMap.put(vo.getBankAccId(),btBankAccDTO);
                }else{
                    btBankAccDTO = bankMap.get(vo.getBankAccId());
                }
                if(!EmptyUtils.isEmpty(btBankAccDTO)) {
                    if (!balMap.containsKey(vo.getBankAccId())) {
                        bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
                        balMap.put(vo.getBankAccId(), bisAccBalDTO);
                    } else {
                        bisAccBalDTO = balMap.get(vo.getBankAccId());
                    }
                    if(!EmptyUtils.isEmpty(bisAccBalDTO)){
                        if(!EmptyUtils.isEmpty(bisAccBalDTO.getBal())){
                            vo.setBal(bisAccBalDTO.getBal());
                        }else{
                            vo.setBal(BigDecimal.ZERO);
                        }
                        if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
                            vo.setAvailBal(bisAccBalDTO.getAvailBal());
                        }else{
                            vo.setAvailBal(BigDecimal.ZERO);
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 批量选择上级账号
     * @param nisDemandVO
     * @param user
     */
    @Transactional
    public void batchSetSuperAcc(NisDemandVO nisDemandVO, ICurrentUser user) {
        if (StringUtils.isNotEmpty(nisDemandVO.getId())) {
            String[] ids = nisDemandVO.getId().split(",");
            for (String id : ids) {
                NisDemand entity = this.get(id);
                //暂存和编辑状态才可以提交
                if (entity != null && !NisDemandConstant.STATUE_TEMP.equals(entity.getStatus()) && !NisDemandConstant.STATUE_CHECK.equals(entity.getStatus())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
                }
                entity.setSuperCorpId(nisDemandVO.getSuperCorpId());
                entity.setSuperBankAccId(nisDemandVO.getSuperBankAccId());
                entity.setSuperBankAcc(nisDemandVO.getSuperBankAcc());
                entity.setSuperAccName(nisDemandVO.getSuperAccName());
                entity.setSuperBankName(nisDemandVO.getSuperBankName());
                entity.setUpdateBy(user.getLoginName());
                entity.setUpdateDate(DateUtils.getDate());
                this.saveOrUpdate(entity);
            }
        }
    }

    /***
     *  根据需求账号+需求类型+日期+上级账户分组数据
     * @param nisDemandVOList
     * @param mapList
     * @param ids
     * @return TODO
     */
    public void groupSummaryDataList(List<NisDemandVO> nisDemandVOList, Map<String, List<NisDemandVO>> mapList, List<String> ids) {
        for(NisDemandVO vo : nisDemandVOList){
            ids.add(vo.getId());
            String key = "";
            if(EmptyUtils.isNotEmpty(vo.getBankAcc())) {
                key += vo.getBankAcc();
            }
            if(EmptyUtils.isNotEmpty(vo.getDemandType())) {
                key += vo.getDemandType();
            }
            if(!EmptyUtils.isEmpty(vo.getDemandDate())) {
                key += vo.getDemandDate();
            }
            if(EmptyUtils.isNotEmpty(vo.getSuperBankAcc())){
                key += vo.getSuperBankAcc();
            }
            if(EmptyUtils.isNotEmpty(key)){
                if(mapList.containsKey(key)){
                    mapList.get(key).add(vo);
                }else{
                    List<NisDemandVO> tempList = new ArrayList<>();
                    tempList.add(vo);
                    mapList.put(key,tempList);
                }
            }
        }
    }

    /**
     * 更新需求单状态为下拨成功
     * @param ids
     * @param updateBy
     * @return TODO
     */
    @Transactional
    public void updateNisDemandStatusSuccess(List<String> ids,String updateBy) {
        nisDemandDao.updateNisDemandStatusSuccess(ids,updateBy);
    }

    /**
     * 更新需求单状态
     * @param id
     * @param status
     * @param failReason
     * @param updateBy
     * @return TODO
     */
    @Transactional
    public void updateNisDemandStatus(String id,Integer status ,String failReason,String updateBy) {
        nisDemandDao.updateNisDemandStatus(id,status,failReason,updateBy);
    }

    /***
     * 更新需求单的头寸调拨状态
     * @param ids
     * @param posStatus
     * @return TODO
     */
    @Transactional
    public void updateNisDemandPosStatus(List<String> ids,String posStatus) {
        nisDemandDao.updateNisDemandPosStatus(ids,posStatus);
    }

    /**
     * 需求单根据类型生成下拨指令
     * @param demandType
     * @return TODO
     */
    public void demandGenerateDownExc(String demandType) {
        // 校验当前日期是否工作日，是工作日才执行
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            // 付款方式：拨付
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
            if (EmptyUtils.isEmpty(voucherType)){
                XxlJobHelper.log("当前系统结算方式：10-拨付,不存在，请先维护！");
                return;
            }
            // 待下拨需求清单
            List<NisDemandVO> payList = listByAutoAllocation(demandType);
            if (EmptyUtils.isNotEmpty(payList)){
                XxlJobHelper.log("当前有：{}条需求单需要生成下拨指令。",payList.size());
                ICurrentUser user = userService.getByLoginName("admin");
                // 资金中心调拨账户配置
                List<BisGatherPolicyAccountVO> policyAccList = policyAccountService.getAllIsDown();

                // 获取所有需求单位id
                List<String> corpIds = payList.stream().map(NisDemandVO::getCorpId).distinct().collect(Collectors.toList());
                // 生成下拨指令
                List<ConfigEntityDTO> configList = configService.listConfigEntityDTO("nis-isChaeOpenCorp", CommonConstant.YES,corpIds);
                if (EmptyUtils.isEmpty(configList)){
                    XxlJobHelper.log("当前所有需求单位都未启用自动下拨功能！");
                    return;
                }
                // 如果需求单位启用了自动下拨功能，那么configMap会有该单位的数据
                Map<String, String> configMap = configList.stream().collect(Collectors.toMap(ConfigEntityDTO::getName, ConfigEntityDTO::getValue));
                configList.clear();
                // 所有需求单位
                Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);

                // 一次性查出所有需求单位的营收账号
                Map<String, List<BtBankAccDTO>> corpAccMap = listMapBankAcc1004(corpIds);

                // 获取所有父账号 superBankAccId
                List<String> superBankAccIds = payList.stream().map(NisDemandVO::getSuperBankAccId).distinct().collect(Collectors.toList());
                List<BtBankAccDTO> superBankAccList = btBankAccService.getBankAccByIdList(superBankAccIds);
                superBankAccIds.clear();
                // 根据账号ID组成Map，key为账号ID，value为账号对象
                Map<String, BtBankAccDTO> superBankAccMap = superBankAccList.stream().collect(Collectors.toMap(BtBankAccDTO::getId, v -> v));
                superBankAccList.clear();

                // 获取所有需求账号 bankAccId
                List<String> bankAccIds = payList.stream().map(NisDemandVO::getBankAccId).distinct().collect(Collectors.toList());
                List<BtBankAccDTO> bankAccList = btBankAccService.getBankAccByIdList(bankAccIds);
                bankAccIds.clear();
                // 根据账号ID组成Map，key为账号ID，value为账号对象
                Map<String, BtBankAccDTO> bankAccMap = bankAccList.stream().collect(Collectors.toMap(BtBankAccDTO::getId, v -> v));
                bankAccList.clear();

                // 是否记账
                String isAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
                // 判断是否自动记账
                String isAutoAccount = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();

                for (NisDemandVO demandVO : payList){
                    try {
                        // 需求日期改成当天
                        demandVO.setDemandDate(DateUtils.getDateYMD());

                        // 1) 判断需求单位是否启用自动下拨
                        if (!configMap.containsKey(demandVO.getCorpId()) || !corpMap.containsKey(demandVO.getCorpId())){
                            XxlJobHelper.log("需求单位：{}未启用自动下拨功能或无效。",demandVO.getCorpName());
                            // 修改需求单状态为下拨失败
                            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,demandVO.getCorpName()+"未启用自动下拨功能或无效",user.getLoginName());
                            continue;
                        }

                        // 2）判断收付款账号是否存在
                        BtBankAccDTO payerAcc = superBankAccMap.get(demandVO.getSuperBankAccId());
                        if (ObjectUtil.isEmpty(payerAcc)){
                            String msg = demandVO.getSuperBankAcc() == null ? demandVO.getSuperBankAccId() : demandVO.getSuperBankAcc();
                            XxlJobHelper.log("需求单：{}上级账号：{}无效。",demandVO.getBillNo(),msg);
                            // 修改需求单状态为下拨失败
                            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,msg+"为无效账号",user.getLoginName());
                            continue;
                        }

                        // 收款账号
                        BtBankAccDTO payeeAcc = bankAccMap.get(demandVO.getBankAccId());
                        if (ObjectUtil.isEmpty(payeeAcc)){
                            String msg = demandVO.getBankAcc() == null ? demandVO.getBankAccId() : demandVO.getBankAcc();
                            XxlJobHelper.log("需求单：{}需求账号：{}无效。",demandVO.getBillNo(),msg);
                            // 修改需求单状态为下拨失败
                            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,msg+"为无效账号",user.getLoginName());
                            continue;
                        }

                        // 3）计算下拨金额
                        BigDecimal allocationMoney = demandVO.getDemandMoney();
                        // 需求账号余额
                        BisAccBalDTO bankAccBal = getLastBisAccBalBy(payeeAcc.getBankAcc(),payeeAcc.getCurId());
                        if (NisDemandConstant.DEMANDTYPE_01.equals(demandType)){// 付款类，需要根据需求单位的营收账号重算下拨金额
                            // 实际下拨金额 = 汇总的需求下拨金额-（资金需求账号系统可用余额-资金需求账号留底金额）
                            // -（营收账号的当前系统可用余额-营收账号留底金额）
                            // +（营收账号数量*1000）

                            // 资金需求账号余额，留底金额；（资金需求账号系统可用余额-资金需求账号留底金额）
                            BigDecimal demandBal = bankAccBal.getAvailBal().subtract(bankAccBal.getLeaveMoney());
                            allocationMoney = allocationMoney.subtract(demandBal);
                            // 营收账号余额，留底金额；（营收账号系统可用余额-营收账号留底金额）
                            List<BtBankAccDTO> btBankAccDTOS = corpAccMap.get(demandVO.getCorpId());
                            if (ObjectUtil.isNotEmpty(btBankAccDTOS)){
                                BigDecimal sum1004 = BigDecimal.ZERO;
                                for (BtBankAccDTO btBankAccDTO : btBankAccDTOS){
                                    BigDecimal subtract = btBankAccDTO.getAvailBal().subtract(btBankAccDTO.getLeaveMoney());
                                    sum1004 = sum1004.add(subtract);
                                }
                                allocationMoney = allocationMoney.subtract(sum1004);

                                // 手续费：营收账号数量*1000
                                BigDecimal handFee = new BigDecimal("1000").multiply(new BigDecimal(btBankAccDTOS.size()));
                                allocationMoney = allocationMoney.add(handFee);

                                // 付款类存在营收账号同时触发头寸调拨
                                generatePos(demandVO,user);
                                XxlJobHelper.log("需求单：{}为付款类触发营收账号的头寸调拨",demandVO.getBillNo());
                            }
                        }
                        XxlJobHelper.log("需求单：{}实际下拨金额为：{}",demandVO.getBillNo(),allocationMoney);

                        // 如果实际下拨金额小于等于0，则不需要下拨。直接更新成功
                        if (allocationMoney.compareTo(BigDecimal.ZERO) <=0 ){
                            // 修改需求单状态为下拨成功
                            updateNisDemandStatusSuccess(Arrays.asList(demandVO.getId()),user.getLoginName());
                            continue;
                        }

                        // 付款类判断需求账号本身余额是否满足下拨金额
                        BigDecimal newBalance = bankAccBal.getAvailBal().subtract(bankAccBal.getLeaveMoney());
                        if (NisDemandConstant.DEMANDTYPE_01.equals(demandType) && newBalance.compareTo(demandVO.getDemandMoney()) >= 0){
                            // 满足
                            XxlJobHelper.log("需求单：{}需求账号余额：{}大于实际下拨金额：{}直接更新状态下拨成功(3)",demandVO.getBillNo(),bankAccBal.getAvailBal(),allocationMoney);
                            // 修改需求单状态为下拨成功
                            updateNisDemandStatusSuccess(Arrays.asList(demandVO.getId()),user.getLoginName());
                            continue;
                        }

                        // 4）根据单位层级生成下拨指令
                        if (demandVO.getCorpLevel() <= 3){
                            // 最顶级的直接生成下拨指令
                            generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user);
                        }else {
                            // 大于3级的，判断是否存在上级单据
                            if (ObjectUtil.isNotEmpty(demandVO.getSuperId())){
                                // 存在上级的，看父级单据是否交易成功
                                NisDemand superDemand = this.get(demandVO.getSuperId(),true);
                                if ( NisDemandConstant.STATUE_ALLOCATION.equals(superDemand.getStatus())){
                                    // 看上级单据是否进行了头寸调拨，如果进行了头寸调拨，则需要判断头寸调拨是否成功
                                    if (CommonConstant.YES.equals(superDemand.getIsPos())){
                                        if (NisDemandConstant.WAIT_RESULT.equals(superDemand.getPosStatus())){
                                            // 等待结果的，则跳过
                                            XxlJobHelper.log("需求单：{}的上级需求单：{}的营收账号未完成头寸调拨。",demandVO.getBillNo(),superDemand.getBillNo());
                                            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_CHECK,"上级需求单："+superDemand.getBillNo()+"的营收账号头寸调拨未完成",user.getLoginName());
                                            continue;
                                        }else if(NisDemandConstant.SUCCESS.equals(superDemand.getPosStatus())){
                                            // 头寸调拨成功的，则生成下拨指令
                                            generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user);
                                        }else{
                                            // 存在失败的。则更新此需求单为失败
                                            XxlJobHelper.log("需求单：{}的上级需求单：{}的营收账号头寸调拨存在失败指令。",demandVO.getBillNo(),superDemand.getBillNo());
                                            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,"上级需求单："+superDemand.getBillNo()+"的营收账号头寸调拨存在失败指令",user.getLoginName());
                                            continue;
                                        }
                                    }else{
                                        // 如果上级没有头寸调拨，则直接生成下拨指令
                                        generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user);
                                    }
                                }else if (NisDemandConstant.STATUE_ALLOCATION_FAIL.equals(superDemand.getStatus())){
                                    // 如果上级单据下拨失败，则直接更新此需求单为下拨失败
                                    XxlJobHelper.log("需求单：{}的上级需求单：{}下拨失败，直接更新此需求单为下拨失败。",demandVO.getBillNo(),superDemand.getBillNo());
                                    updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,"上级需求单："+superDemand.getBillNo()+"下拨失败",user.getLoginName());
                                    continue;
                                }else{
                                    // 没有明确结果的，等待上级下拨状态
                                    XxlJobHelper.log("需求单：{}的上级需求单：{}未下拨完成，等待上级单据下拨完成。",demandVO.getBillNo(),superDemand.getBillNo());
                                    updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_CHECK,"等待上级需求单："+superDemand.getBillNo()+"下拨完成",user.getLoginName());
                                    continue;
                                }
                            }else{
                                // 不存在上级的，直接生成下拨指令
                                generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user);
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        // 修改需求单状态为下拨失败
                        updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,e.getLocalizedMessage(),user.getLoginName());
                        XxlJobHelper.log("需求单：{}生成下拨指令失败：{}",demandVO.getBillNo(), e.getMessage());
                        XxlJobHelper.log(e);
                    }
                }
            }else{
                XxlJobHelper.log("没有需要生成下拨指令的需求单。");
            }
        }else {
            XxlJobHelper.log("当前日期为非工作日不生成下拨指令");
        }
    }

    /***
     * 生成下拨指令
     * @param demandVO
     * @param allocationMoney
     * @param corpDTO
     * @param payerAcc
     * @param payeeAcc
     * @param voucherType
     * @param policyAccList
     * @param isAccount
     * @param isAutoAccount
     * @param user
     * @return TODO
     */
    @Transactional
    public void generateDownExc(NisDemandVO demandVO, BigDecimal allocationMoney, CorpDTO corpDTO,BtBankAccDTO payerAcc, BtBankAccDTO payeeAcc,FcVoucherTypeDTO voucherType, List<BisGatherPolicyAccountVO> policyAccList, String isAccount, String isAutoAccount, ICurrentUser user) {
        Boolean isGenerate = true;
        String errorMsg = "";
        // 5） 生成付款指令对象
        BisExcDTO bisExcDTO = buildBisExc(demandVO, payerAcc, payeeAcc, voucherType);

        // 6）根据付款账号可用余额和策略配置的“是否允许下拨”分为下面2种情况：
        BisAccBalDTO payerAccBal = getLastBisAccBalBy(payerAcc.getBankAcc(), payerAcc.getCurId());
        String finalPayerAcc = payerAcc.getBankAcc();
        BisGatherPolicyAccountVO policyAccountVO = policyAccList.stream().filter(policyAccount ->
                finalPayerAcc.equals(policyAccount.getBankAcc())).findFirst().orElse(null);

        // 6.1 当前资金中心账户“可用余额”小于需要下拨的金额时或者当前资金中心账户不允许下拨时：
        if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(allocationMoney) < 0 )
                || (ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.NO.equals(policyAccountVO.getIsDown()))){
            // 是否存在可用的资金中心调拨账户
            boolean hasPayAcc = false;
            BtBankAccDTO bankAccInfo = null;
            BisAccBalDTO tempAccBal = null;
            if (demandVO.getCorpLevel() == 3){
                for (BisGatherPolicyAccountVO policyAccount : policyAccList){
                    if (CommonConstant.NO.equals(policyAccount.getIsDown())){
                        continue;
                    }
                    bankAccInfo = btBankAccService.getBankAccInfo(policyAccount.getBankAcc());
                    bankAccInfo.setCurId(BtCurrencyConstant.RMBID);
                    tempAccBal = getLastBisAccBalBy(bankAccInfo.getBankAcc(), bankAccInfo.getCurId());
                    if (ObjectUtil.isNotEmpty(tempAccBal) && tempAccBal.getAvailBal().compareTo(allocationMoney) >= 0){
                        hasPayAcc = true;
                        payerAcc = bankAccInfo;
                        payerAccBal = tempAccBal;
                        break;
                    }
                }
            }
            // 存在可用的调拨账号，则替换指令中的付款账号为该账号
            if (hasPayAcc){
                bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
                bisExcDTO.setPayerAccName(payerAcc.getAccName());
                bisExcDTO.setPayerBank(payerAcc.getBankName());
                bisExcDTO.setPayerCity(payerAcc.getCity());
                bisExcDTO.setPayerProv(payerAcc.getProv());
                bisExcDTO.setCurId(payerAcc.getCurId());
                bisExcDTO.setBifCode(payerAcc.getBifCode());
                bisExcDTO.setCorpId(payerAcc.getCorpId());
                this.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
            }else{
                isGenerate = false;
                errorMsg = "上级账号的可用余额都小于需要下拨的金额";
                XxlJobHelper.log("需求单：{}中止，上级账号的可用余额都小于需要下拨的金额：{}", demandVO.getBillNo(),allocationMoney);
            }
        }

        // 6.2 当资金中心账户“可用余额”大于需要下拨的金额时 需要再根据需求单位层级判断是否允许下拨
        if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(allocationMoney) >= 0 ) &&
                (( demandVO.getCorpLevel()<=3 && ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.YES.equals(policyAccountVO.getIsDown()))
                        || demandVO.getCorpLevel()>3)){
            // 判断需求单位的股权属性
            if (ObjectUtil.isNotEmpty(corpDTO.getEquityMaintenanceId())){
                EquityMaintenanceDTO equityMaintenanceDTO = equityMaintenanceService.queryById(corpDTO.getEquityMaintenanceId());
                // 是否差额下拨
                boolean differentSign = false;
                BigDecimal demandMoney = allocationMoney;
                if ("0001".equals(equityMaintenanceDTO.getEquityCode())){
                    // 融资，票据直接下拨,付款类特殊处理
                    if (NisDemandConstant.DEMANDTYPE_01.equals(demandVO.getDemandType())){
                        // 查询收款账号对应的内部账号 counterAccId
                        FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(payeeAcc.getCounterAccId());
                        if (ObjectUtil.isNotEmpty(fcOnlineLedgerDTO)){
                            boolean notUrgentFlag = false;
                            // 根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                            BigDecimal checkBalance = fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney());
                            if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                // 不允许透支
                                // 1）内部账号余额大于等于下拨余额时，自动下拨

                                // 2）内部账号余额小下拨余额时，将下拨金额调整成当前内部账号余额，
                                // 如：下拨金额为20000，内部账号余额为15000，则下拨指令的金额调整成15000进行下拨；
                                // （增加企业微信余额不足提醒，提醒人员新增界面进行配置）
                                if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                    allocationMoney = checkBalance;
                                    differentSign = true;
                                }
                                // 如果内部账号余额小于等于0，则不下拨
                                if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                    notUrgentFlag = true;
                                    isGenerate = false;
                                    errorMsg = "需求账号对应的内部账户可用余额不足";
                                    XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(),payeeAcc.getBankAcc(),fcOnlineLedgerDTO.getAccount(),checkBalance);
                                }
                            }else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcOnlineLedgerDTO.getOverdraftSign())){
                                // 受限透支 加上透支金额
                                checkBalance = checkBalance.add(fcOnlineLedgerDTO.getOverdraftMoney());
                                // 1） 内部账号余额大于等于下拨余额时，自动下拨

                                // 2）内部账号余额小于下拨余额时，将下拨金额调整成当前内部账号余额，
                                if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0){
                                    allocationMoney = checkBalance;
                                    differentSign = true;
                                }
                                // 如果内部账号余额小于等于0，则不下拨
                                if (checkBalance.compareTo(BigDecimal.ZERO) <= 0){
                                    notUrgentFlag = true;
                                    isGenerate = false;
                                    errorMsg = "需求账号对应的内部账户可用余额不足";
                                    XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(),payeeAcc.getBankAcc(),fcOnlineLedgerDTO.getAccount(),checkBalance);
                                }
                            }
                            // InnerAccountConstant.OVERDRAFT_SIGN_D
                            // 3) 不受限透支，直接下拨

                            if (notUrgentFlag){
                                // 触发不紧急消息提醒
                                Map<String, Object> map = new HashMap<>();
                                map.put("reason","需求账号对应的内部账户可用余额不足");
                                map.put("billNo", demandVO.getBillNo());
                                map.put("payer", bisExcDTO.getPayerAccName());
                                map.put("payerAcc", bisExcDTO.getPayerAcc());
                                map.put("payerBank", bisExcDTO.getPayerBank());
                                map.put("payee", bisExcDTO.getPayeeAccName());
                                map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                                map.put("payeeBank", bisExcDTO.getPayeeBank());
                                map.put("downAmt", 0);
                                map.put("amt", demandVO.getDemandMoney());
                                String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_NOT_URGENT_FAILURE;
                                notificationService.send(demandVO.getId(),demandVO.getCorpId(), tag, map);
                                XxlJobHelper.log("发送不紧急消息：{}",map.toString());
                            }
                        }else{
                            isGenerate = false;
                            errorMsg = "需求账号未关联内部账号";
                            XxlJobHelper.log("需求单：{}中止，账号：{}未关联内部账号不满足下拨条件", demandVO.getBillNo(),payeeAcc.getBankAcc());
                        }
                    }
                }else if ("0002".equals(equityMaintenanceDTO.getEquityCode()) ) {
                    // 内部资金归集管理协议签订：是否签订，未签订不做下拨，已签订再根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                    BisGatherPolicyAgreementVO policyAgreementVO = policyAgreementService.getByCorpId(payeeAcc.getCorpId());
                    if (ObjectUtil.isNotEmpty(policyAgreementVO) && CommonConstant.YES.equals(policyAgreementVO.getIsAgreement())) {
                        //  融资，票据直接下拨,付款类特殊处理
                        if (NisDemandConstant.DEMANDTYPE_01.equals(demandVO.getDemandType())) {
                            // 查询收款账号对应的内部账号 counterAccId
                            FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(payeeAcc.getCounterAccId());
                            if (ObjectUtil.isNotEmpty(fcOnlineLedgerDTO)) {
                                // 下拨金额：allocatedMoney
                                // 根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                                BigDecimal checkBalance = fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney());
                                if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcOnlineLedgerDTO.getOverdraftSign())) {
                                    // 不允许透支
                                    // 1）内部账号余额大于等于下拨余额时，自动下拨

                                    // 2）内部账号余额小下拨余额时，将下拨金额调整成当前内部账号余额，
                                    // 如：下拨金额为20000，内部账号余额为15000，则下拨指令的金额调整成15000进行下拨；
                                    // （增加企业微信余额不足提醒，提醒人员新增界面进行配置）
                                    if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0) {
                                        allocationMoney = checkBalance;
                                        differentSign = true;
                                    }
                                    // 如果内部账号余额小于等于0，则不下拨
                                    if (checkBalance.compareTo(BigDecimal.ZERO) <= 0) {
                                        isGenerate = false;
                                        errorMsg = "需求账号对应的内部账户可用余额不足";
                                        XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(), payeeAcc.getBankAcc(),fcOnlineLedgerDTO.getAccount(), checkBalance);
                                    }
                                } else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcOnlineLedgerDTO.getOverdraftSign())) {
                                    // 受限透支 加上透支金额
                                    checkBalance = checkBalance.add(fcOnlineLedgerDTO.getOverdraftMoney());
                                    // 1） 内部账号余额大于等于下拨余额时，自动下拨

                                    // 2）内部账号余额小于下拨余额时，将下拨金额调整成当前内部账号余额，
                                    if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0) {
                                        allocationMoney = checkBalance;
                                        differentSign = true;
                                    }
                                    // 如果内部账号余额小于等于0，则不下拨
                                    if (checkBalance.compareTo(BigDecimal.ZERO) <= 0) {
                                        isGenerate = false;
                                        errorMsg = "需求账号对应的内部账户可用余额不足";
                                        XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(), payeeAcc.getBankAcc(), fcOnlineLedgerDTO.getAccount(),checkBalance);
                                    }
                                }
                                // InnerAccountConstant.OVERDRAFT_SIGN_D
                                // 3) 不受限透支，直接下拨
                            } else {
                                isGenerate = false;
                                errorMsg = "需求账号未关联内部账号";
                                XxlJobHelper.log("需求单：{}中止，账号：{}未关联内部账号不满足下拨条件", demandVO.getBillNo(), payeeAcc.getBankAcc());
                            }
                        }
                    } else {
                        // 如果是票据或者融资需要触发头寸调拨
                        if (!NisDemandConstant.DEMANDTYPE_01.equals(demandVO.getDemandType())){
                            generatePos(demandVO,user);
                        }
                        isGenerate = false;
                        errorMsg = "需求单位未签订资金归集管理协议";
                        XxlJobHelper.log("需求单：{}中止，单位：{}未签订资金归集管理协议", demandVO.getBillNo(), corpDTO.getName());
                    }
                }else{
                    isGenerate = false;
                    errorMsg = "需求单位的股权属性不是全资或控股";
                    XxlJobHelper.log("需求单：{}中止，单位：{}的股权属性不是全资或控股", demandVO.getBillNo(),corpDTO.getName());
                }

                //  差额下拨发送消息
                if (differentSign ){
                    // 发送紧急消息
                    Map<String, Object> map = new HashMap<>();
                    map.put("billNo", demandVO.getBillNo());
                    map.put("payer", bisExcDTO.getPayerAccName());
                    map.put("payerAcc", bisExcDTO.getPayerAcc());
                    map.put("payerBank", bisExcDTO.getPayerBank());
                    map.put("payee", bisExcDTO.getPayeeAccName());
                    map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                    map.put("payeeBank", bisExcDTO.getPayeeBank());
                    map.put("downAmt", allocationMoney);
                    map.put("amt", demandVO.getDemandMoney());
                    map.put("diffAmt", demandMoney.subtract(allocationMoney));
                    String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_FAILURE_DIFFERENCE;
                    notificationService.send(demandVO.getId(),demandVO.getCorpId(), tag, map);
                    XxlJobHelper.log("发送紧急消息：{}",map.toString());
                }
            }else{
                isGenerate = false;
                errorMsg = "需求单位的股权维护信息为空";
                XxlJobHelper.log("需求单：{}中止，单位：{}的股权维护信息为空", demandVO.getBillNo(),corpDTO.getName());
            }
        }

        // 7） 当计算出来的下拨付款指令金额为0或者负数时，不生成下拨指令；
        if (allocationMoney.compareTo(BigDecimal.ZERO) <= 0){
            isGenerate = false;
            errorMsg = "实际下拨小于等于0";
            XxlJobHelper.log("需求单：{}中止，实际下拨金额：{}小于等于0", demandVO.getBillNo(), allocationMoney);
        }
        if(isGenerate) {
            bisExcDTO.setPayMoney(allocationMoney);
            BisExcDTO result = bisExcService.addBisExcReturn(bisExcDTO, user);
            demandVO.setStatus(NisDemandConstant.STATUE_WAIT_ALLOCATION);
            demandVO.setUpdateBy(user.getLoginName());
            demandVO.setUpdateDate(DateUtils.getDate());
            if (!EmptyUtils.isEmpty(result)) {
                demandVO.setAllocationNo(result.getVoucherNo());
                demandVO.setAllocationId(result.getId());
                XxlJobHelper.log("需求单：{}生成下拨指令成功：{}", demandVO.getBillNo(),result.getVoucherNo());
            }
            //自动记账
            if (CommonConstant.YES.equals(isAccount) && CommonConstant.YES.equals(isAutoAccount)) {
                XxlJobHelper.log("需求单：{}开始记账。。。", demandVO.getBillNo());
                submitVoucher(demandVO, bisExcDTO, payerAcc, payeeAcc, user);
            }
            //编辑需求单，控制原状态为0-暂存/1已确认
            Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
            this.updateNisDemandStatus(demandVO, oldStatus);
        }else{
            updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,errorMsg,user.getLoginName());
            XxlJobHelper.log("需求单：{}中止，条件不满足！", demandVO.getBillNo());
        }

    }

    /***
     * 创建指令对象
     * @param demandVO
     * @param payerAcc
     * @param payeeAcc
     * @param voucherType
     * @return TODO
     */
    public BisExcDTO buildBisExc(NisDemandVO demandVO, BtBankAccDTO payerAcc, BtBankAccDTO payeeAcc, FcVoucherTypeDTO voucherType) {
        BisExcDTO bisExcDTO = new BisExcDTO();
        bisExcDTO.setCbsBillId(demandVO.getId());
        bisExcDTO.setCbsBillNo(demandVO.getBillNo());
        bisExcDTO.setReqDate(demandVO.getDemandDate());
        bisExcDTO.setWishPayday(demandVO.getDemandDate());
        // 付款方
        bisExcDTO.setCorpId(payerAcc.getCorpId());
        bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
        bisExcDTO.setPayerAccName(payerAcc.getAccName());
        bisExcDTO.setPayerBank(payerAcc.getBankName());
        bisExcDTO.setPayerCity(payerAcc.getCity());
        bisExcDTO.setPayerProv(payerAcc.getProv());
        bisExcDTO.setCurId(payerAcc.getCurId());
        bisExcDTO.setBifCode(payerAcc.getBifCode());
        // 收款方
        bisExcDTO.setPayeeAcc(payeeAcc.getBankAcc());
        bisExcDTO.setPayeeAccName(payeeAcc.getAccName());
        bisExcDTO.setPayeeBank(payeeAcc.getBankName());
        bisExcDTO.setPayeeProv(payeeAcc.getProv());
        bisExcDTO.setPayeeCity(payeeAcc.getCity());
        bisExcDTO.setPayeeBankCode(payeeAcc.getBankCode());
        //交易模式 3-对公 4-对私 默认4
        bisExcDTO.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
        bisExcDTO.setIsQuick("0");
        this.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
        bisExcDTO.setPurpose("需求资金下拨");
        bisExcDTO.setAddWord("需求资金下拨");
        bisExcDTO.setVoucherFrom(BdlinkConstant.FROM_NIS_DEMAND);
        bisExcDTO.setBusOriType(BdlinkConstant.BUSORITYPE_32);
        bisExcDTO.setVoucherTypeId(voucherType.getId());
        bisExcDTO.setVoucherStat(BdlinkConstant.PRE_SEND);

        return bisExcDTO;
    }


    /**
     * 根据账号，币种或者最新余额与留底金额
     * @param bankAcc
     * @param curId
     * @return TODO
     */
    public BisAccBalDTO getLastBisAccBalBy(String bankAcc, String curId) {
        BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(bankAcc, curId);
        // 查询留底金额
        if (ObjectUtil.isEmpty(bisAccBalDTO)) {
            bisAccBalDTO = new BisAccBalDTO();
            bisAccBalDTO.setAvailBal(BigDecimal.ZERO);
        }
        BigDecimal leaveMoney = nisDemandDao.getLeaveMoney(bankAcc);
        bisAccBalDTO.setLeaveMoney(leaveMoney);
        return bisAccBalDTO;
    }

    /**
     * 自动汇总需求单： 如果本层级只有一笔则直接更新isSummary = 1；如果本层级有多笔则生成新的汇总单，原来的单据作废
     * @param levelList
     * @param user
     * @return TODO
     */
    @Transactional
    public void autoSummaryDemand(int level, List<NisDemandVO> levelList, Map<Integer, List<NisDemandVO>> mapList, ICurrentUser user) {
        // 根据需求账号+需求类型+日期+上级账户分组数据
        Map<String, List<NisDemandVO>> demandMap = groupDemandList(levelList);
        for(Map.Entry<String, List<NisDemandVO>> entry : demandMap.entrySet()){
            String key = entry.getKey();
            List<NisDemandVO> demandList = entry.getValue();
            // 判断是否需要生成新的汇总单
            String isSummary = CommonConstant.YES;
            Integer status = NisDemandConstant.STATUE_CHECK;
            if (demandList.size() > 1){
                // 如果需求单数量大于1，则需要生成一笔汇总单据，那么原单作废，原单isSummary = 0
                isSummary = CommonConstant.NO;
                status = NisDemandConstant.STATUE_DEL;
                XxlJobHelper.log("需求单位层级为：{}存在多笔数据，需汇总成一笔新的需求清单。",level);
                // 生成汇总单据
                buildSummaryDemand(demandList,user);
            }

            // 更新当前需求清单状态和汇总标识
            List<String> idList = demandList.stream().map(demandVO -> demandVO.getId()).collect(Collectors.toList());
            nisDemandDao.updateNisDemandSummaryStatus(idList,status,isSummary,user.getLoginName());
            String billNoList = demandList.stream().map(demandVO -> demandVO.getBillNo()+ ",").collect(Collectors.joining());
            XxlJobHelper.log("更新需求清单：{}状态为：{}是否汇总为：{}",billNoList,status,isSummary);

            // 判断需求清单的上级账号是否有上级账号
            BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(demandList.get(0).getSuperBankAccId());
            if(ObjectUtil.isNotEmpty(superBankAcc.getPrentAccId())){
                XxlJobHelper.log("需求清单：{}的上级账号：{}存在上级账号，继续生成新的需求清单",billNoList,demandList.get(0).getSuperBankAcc());
                // 存在上级账号则生成上级往本级账号的需求单
                BtBankAccDTO sSuperBankAcc = btBankAccService.getBankAccById(superBankAcc.getPrentAccId());
                // sSuperBankAcc->superBankAcc的需求清单
                //单位信息
                CorpDTO corpDTO = corpService.get(superBankAcc.getCorpId());
                // 新的需求清单
                NisDemand entity = new NisDemand();
                entity.setDemandType(demandList.get(0).getDemandType());
                //当需求类型为付款时
                if(NisDemandConstant.DEMANDTYPE_01.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_B));
                    //当需求类型为承兑到期时
                }else if(NisDemandConstant.DEMANDTYPE_02.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_A));
                    //当需求类型为融资到期时
                }else if(NisDemandConstant.DEMANDTYPE_03.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_C));
                }else if(NisDemandConstant.DEMANDTYPE_04.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_D));
                }else if(NisDemandConstant.DEMANDTYPE_05.equals(entity.getDemandType())){
                    entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_E));
                }
                //付款单据信息
                entity.setPayBillId(null);
                entity.setPayBillNo(null);
                entity.setPayBillDate(null);
                entity.setCorpId(superBankAcc.getCorpId());
                entity.setBankAcc(superBankAcc.getBankAcc());
                entity.setBankAccId(superBankAcc.getId());
                entity.setAccName(superBankAcc.getAccName());
                entity.setBankName(superBankAcc.getBankName());
                if (!EmptyUtils.isEmpty(corpDTO)) {
                    String[] parentIds = corpDTO.getParentIds().split(",");
                    entity.setCorpLevel(parentIds.length);
                }else{
                    entity.setCorpLevel(level-1);
                }
                entity.setSuperBankAccId(sSuperBankAcc.getId());
                entity.setSuperBankAcc(sSuperBankAcc.getBankAcc());
                entity.setSuperBankName(sSuperBankAcc.getBankName());
                entity.setSuperAccName(sSuperBankAcc.getAccName());
                entity.setSuperCorpId(sSuperBankAcc.getCorpId());
                entity.setStatus(NisDemandConstant.STATUE_CHECK);
                entity.setSuperCorpId(sSuperBankAcc.getCorpId());
                //合计金额
                BigDecimal sumMoney =  demandList.stream()
                        .map(vo -> vo.getDemandMoney() == null ? BigDecimal.ZERO : vo.getDemandMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
                entity.setDemandMoney(sumMoney);
                entity.setDemandAllocationMoney(sumMoney);
                entity.setDemandDate(DateUtils.getDateYMD());
                entity.setIsPos(CommonConstant.NO);
                entity.setIsSummary(CommonConstant.NO);
                entity.setCreateBy(user.getLoginName());
                entity.setCreateDate(DateUtils.getDate());
                //保存需求单
                this.saveEntity(entity);
                // 把新增生成的需求清单存放到mapList中，方便上级做汇总
                NisDemandVO demandVO = new NisDemandVO();
                BeanUtils.copyNotNullProperties(entity, demandVO);
                List<NisDemandVO> list = mapList.get(level - 1);
                if (ObjectUtil.isEmpty(list)){
                    list = new ArrayList<>();
                }
                list.add(demandVO);
                mapList.put(level-1, list);

                // 更新原来需求清单的super_id为新生成需求的ID
                // 通过类型，日期，上级需求账号来更新
                nisDemandDao.updateNisDemandSuperId(demandVO.getId(),demandVO.getDemandType(),demandVO.getDemandDate(),demandVO.getBankAccId(),user.getLoginName());
            }

        }
    }

    @Transactional
    public void buildSummaryDemand(List<NisDemandVO> demandList,ICurrentUser user) {
        NisDemandVO demandVO = demandList.get(0);
        NisDemand entity = new NisDemand();
        BeanUtils.copyNotNullProperties(demandVO,entity);
        entity.setId("");
        //当需求类型为付款时
        if(NisDemandConstant.DEMANDTYPE_01.equals(entity.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_B));
            //当需求类型为承兑到期时
        }else if(NisDemandConstant.DEMANDTYPE_02.equals(entity.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_A));
            //当需求类型为融资到期时
        }else if(NisDemandConstant.DEMANDTYPE_03.equals(entity.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_C));
        }else if(NisDemandConstant.DEMANDTYPE_04.equals(entity.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_D));
        }else if(NisDemandConstant.DEMANDTYPE_05.equals(entity.getDemandType())){
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_E));
        }
        //付款单据信息
        entity.setPayBillId(null);
        entity.setPayBillNo(null);
        entity.setPayBillDate(null);
        //合计金额
        BigDecimal sumMoney =  demandList.stream()
                .map(vo -> vo.getDemandMoney() == null ? BigDecimal.ZERO : vo.getDemandMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
        entity.setDemandMoney(sumMoney);
        entity.setDemandAllocationMoney(sumMoney);
        entity.setIsPos(CommonConstant.NO);
        entity.setIsConverge(CommonConstant.YES);
        entity.setIsSummary(CommonConstant.YES);
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        //保存需求单
        this.saveEntity(entity);

        // 更新原来需求清单的super_id为新生成需求的ID
        // 通过类型，日期，上级需求账号来更新
        nisDemandDao.updateNisDemandSuperId(entity.getId(),entity.getDemandType(),entity.getDemandDate(),entity.getBankAccId(),user.getLoginName());
        nisDemandDao.flush();// 如果不执行的话。后面要更新这条数据的时候会找不到
    }

    /***
     * 根据需求账号+需求类型+日期+上级账户分组数据
     * @param levelList
     * @return TODO
     */
    private Map<String, List<NisDemandVO>> groupDemandList(List<NisDemandVO> levelList) {
        Map<String, List<NisDemandVO>> demandMap = new HashMap<>();
        for(NisDemandVO vo : levelList){
            String key = "";
            if(EmptyUtils.isNotEmpty(vo.getBankAcc())) {
                key += vo.getBankAcc();
            }
            if(EmptyUtils.isNotEmpty(vo.getDemandType())) {
                key += vo.getDemandType();
            }
            if(!EmptyUtils.isEmpty(vo.getDemandDate())) {
                key += DateUtils.formatDate(vo.getDemandDate());
            }
            if(EmptyUtils.isNotEmpty(vo.getSuperBankAcc())){
                key += vo.getSuperBankAcc();
            }
            if(EmptyUtils.isNotEmpty(key)){
                if(demandMap.containsKey(key)){
                    demandMap.get(key).add(vo);
                }else{
                    List<NisDemandVO> tempList = new ArrayList<>();
                    tempList.add(vo);
                    demandMap.put(key,tempList);
                }
            }
        }
        return demandMap;
    }


}
