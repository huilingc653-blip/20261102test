package com.bytter.bill.domain.bill.service;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbClaimDao;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbBillRuleBillNo;
import com.bytter.bill.domain.bill.entity.AbClaimMiddle;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlBillVO;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Lists;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * 票据认领处理service
 *
 * @author ouyj
 */
@Service("AbClaimService")
@Transactional(rollbackFor = Exception.class)
public class AbClaimService extends BaseService<AbClaimVo, String> implements ITaskService {
    @Autowired
    private AbClaimDao abClaimDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbAccountGrantService accountGrantService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbClaimMiddleService abClaimMiddleService;
    @DubboReference(check = false)
    private IBisAccDtlService abBtBankAccService;
    @Autowired
    private AbBillCollectionAccountService abBillCollectionAccountService;
    @Autowired
    private AbBillDiscountAccountService abBillDiscountAccountService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbUserService abUserService;
    @Autowired
    private AbBillRuleBillNoService abBillRuleBillNoService;
    @Autowired
    private AbClaimAccountService abClaimAccountService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbBillRuleBillDtlRmkService abBillRuleBillDtlRmkService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    @DubboReference(check = false)
    private ICalendarService iCalendarService;

    @Override
    public BaseDao<AbClaimVo, String> getEntityDao() {
        return abClaimDao;
    }
    /**
     * 查询票据认领处理列表数据
     *
     * @param dataTablesInput 前端参数
     * @return 票据认领处理列表数据
     */
    public DataTablesOutput listAbClaimInfo(DataTablesInput dataTablesInput) {
        String resource = StringUtils.isEmpty(String.valueOf(dataTablesInput.getFormdata().get("claimQuery")))
                ? AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY : AccebillConstant.AB_BILL_RESOURCE_CLAIMQUERY_QUERY;
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(resource,
                        dataTablesInput.getCurrentUserId()), "P.CORP_ID", true);
        boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        String claimStartString = abConfigService.getConfig(SysConfigConstant.Module.ACCEBILL, SysConfigConstant.CLAIM_START_DATE).getValue();
        if (StringUtils.isEmpty(claimStartString)) {
            claimStartString = "1999-01-01";
        }
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, resource, corpIds);
        DataTablesOutput dataTablesOutput = abClaimDao.listAbClaimInfo(dataTablesInput, corpAuthSql, isEmitMake, DateUtils.str2Date(claimStartString, DateUtils.date_sdf()));
        List<AbClaimVo> list = dataTablesOutput.getData();
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
                "AB_BILL_CLAIM_TYPE",
                "BILL_PAYERPAYEE_TYPE",
                "AB_BILL_CLAIM_STATUS",
                "AB_BILL_TYPE",
                "AB_BILL_FLAG",
                "AB_BILL_NEW_STM_ISSUOFPPR_IND",
                "AB_BILL_SEND_SIGN");
        for (AbClaimVo vo : list) {
            vo.injectValues(listDictMap);
        }
        List<BankTypeByAccDTO> bankTypeByAccDTOS = iBankTypeService.listBankTypeByAcc(list.stream().filter(a->StringUtils.isNotBlank(a.getBankAcc())).map(AbClaimVo::getBankAcc).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(bankTypeByAccDTOS)) {
            return dataTablesOutput;
        }
        //给银行账号set详细信息
        Map<String, BankTypeByAccDTO> accDTOMap = bankTypeByAccDTOS.stream().collect(Collectors.toMap(BankTypeByAccDTO::getBankAcc, Function.identity()));
        Set<String> corpIdSet = new HashSet<>();
        for (AbClaimVo vo : list) {
            if (StringUtil.isEmpty(vo.getBankAcc())) {
                continue;
            }
            BankTypeByAccDTO dto = accDTOMap.get(vo.getBankAcc());
            if (dto!=null){
                vo.setBankTypeId(dto.getBankTypeId());
                vo.setBankTypeName(dto.getBankTypeName());
            }else{
                if(StrUtil.isNotBlank(vo.getBankTypeId())){
                    BankTypeDTO bankTypeDTO = iBankTypeService.getObjectById(vo.getBankTypeId());
                    if(bankTypeDTO!=null){
                        vo.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
            }
            if(StrUtil.isNotBlank(vo.getApplyCorpId())){
                corpIdSet.add(vo.getApplyCorpId());
            }
            if(StrUtil.isNotBlank(vo.getCorpId())){
                corpIdSet.add(vo.getCorpId());
            }
            if(StrUtil.isNotBlank(vo.getBusCorpId())){
                corpIdSet.add(vo.getBusCorpId());
            }
        }
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (Object bill : listData){
                AbClaimVo bill2 = (AbClaimVo)bill;
                CorpDTO corpDTO = stringCorpDTOMap.get(bill2.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill2.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill2.getBusCorpId());
                bill2.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill2.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill2.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill2.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill2.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 查询票据认领处理银行流水列表数据
     *
     * @param dataTablesInput 前端参数
     * @return 票据认领处理银行流水列表数据
     */
    public DataTablesOutput listAbClaimInfoAcc(DataTablesInput dataTablesInput) {
        String resource = StringUtils.isEmpty(String.valueOf(dataTablesInput.getFormdata().get("claimQuery")))
                ? AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY : AccebillConstant.AB_BILL_RESOURCE_CLAIMQUERY_QUERY;
//        String corpAuthSql = SqlUtil.getInSql(
//                abCorpService.getUserCorpIds(resource, dataTablesInput.getCurrentUserId()), "b.corp_id", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, resource, corpIds);
        List<listAbClaimInfoAccVO> listAbClaimInfoAcc = abClaimDao.listAbClaimInfoAcc(dataTablesInput);
        Map<String, listAbClaimInfoAccVO> accountMap = listAbClaimInfoAcc.stream().collect(Collectors.toMap(listAbClaimInfoAccVO::getId, Function.identity()));
        DataTablesOutput<BisAccDtlBillVO> dataTablesOutput = iBisAccBalService.listAbClaimConfirmedInfoAcc(dataTablesInput, new ArrayList<>(accountMap.keySet()));
        if (!CollectionUtils.isEmpty(listAbClaimInfoAcc)) {
            List<BisAccDtlBillVO> data = dataTablesOutput.getData();
            for (BisAccDtlBillVO vo : data) {
                listAbClaimInfoAccVO accVO = accountMap.get(vo.getClaimId());
                vo.setClaimSequence(accVO.getClaimSequence());
                vo.setClaimRmk(accVO.getClaimRmk());
                vo.setOrigNo(accVO.getOrigNo());
            }
        }
        List<BisAccDtlBillVO> list=dataTablesOutput.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BankTypeDTO> typeMap= iBankTypeService.getBankTypeMap();
            for (BisAccDtlBillVO vo : list) {
                if (StrUtil.isBlank(vo.getBankAcc())) {
                    continue;
                }
                BtBankAccDTO btBankAccDTO = bankAccService.getBankAccInfo(vo.getBankAcc());
                if (btBankAccDTO == null) {
                    continue;
                }
                CorpDTO corpDTO = corpMap.get(btBankAccDTO.getCorpId());
                if (corpDTO != null) {
                    vo.setCorpName(corpDTO.getName());
                }
                BankTypeDTO bankTypeDTO = typeMap.get(btBankAccDTO.getBankTypeId());
                if (bankTypeDTO != null) {
                    vo.setBankTypeName(bankTypeDTO.getTypeName());
                }
            }
        }
        return dataTablesOutput;
        // return abClaimDao.listAbClaimInfoAcc(dataTablesInput, corpAuthSql);
    }
    /**
     * 提交认领
     *
     * @param abClaimMiddleVo 票据认领中间表实体
     * @param currentUser     当前用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitClaim(AbClaimMiddleVo abClaimMiddleVo, ICurrentUser currentUser) {
        String accDtlIds = abClaimMiddleVo.getAccDtlIds();
        String vvIds = abClaimMiddleVo.getVvids();
        if (StringUtils.isEmpty(accDtlIds) || "undefined".equals(accDtlIds)
                || StringUtils.isEmpty(vvIds) || "undefined".equals(vvIds)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            AbClaimMiddle abClaimMiddle = new AbClaimMiddle();
            BeanUtils.copyProperties(abClaimMiddleVo, abClaimMiddle);
            //当前登录用户名称
            String username = currentUser.getUsername();
            //当前时间
            Date date = new Date();
            //设置认领人名称
            abClaimMiddle.setClaimPerson(username);
            //认领时间
            abClaimMiddle.setClaimTime(date);
            //序列号
            String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_CLAIM_MIDDLE_SEQUENCE);
            //认领序列
            abClaimMiddle.setClaimSequence(appNo);
            abClaimMiddle.setCreateBy(currentUser.getLoginName());
            abClaimMiddle.setCreateDate(date);
            //修改状态为 已确认95
            abClaimMiddle.setClaimStatus(AccebillConstant.ABLOG_LOGTYPE_APP);
            String[] vvIdArr = vvIds.split(",");
            String rmk = "";
            // 如果有多张票据则将对应的票据号和票据金额存在备注中
            String[] moneyArr = abClaimMiddleVo.getBillMoneys().split(",");
            String[] origNoArr = abClaimMiddleVo.getOrigNos().split(",");
            if (vvIdArr.length > 1) {
                DecimalFormat df = new DecimalFormat("0.00");
                for (int i = 0; i < moneyArr.length; i++) {
                    if (StringUtils.isEmpty(rmk)) {
                        rmk += origNoArr[i] + "|" + df.format(Double.parseDouble(moneyArr[i]));
                    } else {
                        rmk += "," + origNoArr[i] + "|" + df.format(Double.parseDouble(moneyArr[i]));
                    }
                }
            }
            abClaimMiddle.setOrigNo(origNoArr[0]);
            abClaimMiddle.setRmk(rmk);
            abClaimMiddleService.saveEntity(abClaimMiddle);
            if (StringUtils.isNotEmpty(abClaimMiddleVo.getClaimIds())) {
                String[] claimIdArr = abClaimMiddleVo.getClaimIds().split(",");
                //根据票据认领中间表id集合将票据从表以及流水明细表中对应的claim_id设置为null
                abVoucherinfoViceService.updateByClaimArr(claimIdArr);
                abBtBankAccService.updateByClaimArr(claimIdArr);
            }
            //将对应的从表数据的票据认领中间表id设置为当前新增数据id
            abVoucherinfoViceService.updateClaimByIds(vvIdArr, abClaimMiddle.getId(), AccebillConstant.BILL_CLAIM_STATUS95);
            String[] accIdArr = accDtlIds.split(",");
            //将对应的银行账号明细表数据的票据认领中间表id设置为当前新增数据id
            for (String accId : accIdArr) {
                BisAccDtlDTO bisAccDtl = abBtBankAccService.getById(accId);
                if (bisAccDtl != null) {
                    bisAccDtl.setClaimId(abClaimMiddle.getId());
                    abBtBankAccService.updateDtlByDto(bisAccDtl);
                }
            }
            //收款人或付款人(选择了几条票据数据，就得有几个收款人)
            String signeroks = "";
            for (int i = 0; i < vvIdArr.length; i++) {
                if (StringUtils.isEmpty(signeroks)) {
                    signeroks += username;
                } else {
                    signeroks += "," + username;
                }
            }
            //贴现
            if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimMiddleVo.getBriefId())) {
                AbBillDiscountAccountVO abBillDiscountAccountVo = new AbBillDiscountAccountVO();
                abBillDiscountAccountVo.setRealMoneys(abClaimMiddleVo.getRealMoneys());
                abBillDiscountAccountVo.setBusinessDate(date);
                abBillDiscountAccountVo.setSigneroks(signeroks);
                abBillDiscountAccountVo.setAccIds(abClaimMiddleVo.getAids());
                abBillDiscountAccountService.autoApprovalAbBillDiscountAccount(abBillDiscountAccountVo, currentUser);
                //贴现业务认领流水后，依据流水回写台账数据，更新票据业务流水数据
                updateDiscountBillData(abClaimMiddleVo);
            } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimMiddleVo.getBriefId())
                    || AccebillConstant.BRIEF_STATUS_021.equals(abClaimMiddleVo.getBriefId())) {
                // 托收
                AbBillCollectionAccountVo abBillCollectionAccountVo = new AbBillCollectionAccountVo();
                abBillCollectionAccountVo.setBusinessDate(date);
                BeanUtils.copyProperties(abClaimMiddleVo, abBillCollectionAccountVo);
                abBillCollectionAccountVo.setSignerok(signeroks);
                abBillCollectionAccountService.autoApprovalCollectionApply(abBillCollectionAccountVo, currentUser);
            }
        }
    }

    /**
     * 对贴现的数据比较票据和流水金额后反写认领后的数据到流水表中
     *
     * @param abClaimMiddleVo 认领参数
     **/
    private void updateDiscountBillData(AbClaimMiddleVo abClaimMiddleVo) {
        String aids = abClaimMiddleVo.getAids();
        String[] aidArr = aids.split(",");
        String accDtlIds = abClaimMiddleVo.getAccDtlIds();
        String[] accIdArr = accDtlIds.split(",");
        //票据记录与银行流水在一对多或一对一的情况才需要回写数据到流水表，多对一不回写
        if(aidArr.length > 1){
            return;
        }
        BigDecimal totalClaimAmt = BigDecimal.ZERO;
        for (String accId : accIdArr) {
            BisAccDtlDTO bisAccDtl = abBtBankAccService.getById(accId);
            totalClaimAmt = totalClaimAmt.add(bisAccDtl.getAmt());
        }
        abBillDiscountAccountService.updateAbBillDiscountInterestAndRealMoney(totalClaimAmt,aidArr[0]);
    }

    /**
     * 票据认领已确认列表
     *
     * @param dataTablesInput 查询参数
     * @return 前端列表数据
     **/
    public DataTablesOutput listAbClaimConfirmedInfo(DataTablesInput dataTablesInput) {
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY,
                        dataTablesInput.getCurrentUserId()), "P.CORP_ID", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY, corpIds);
        DataTablesOutput<AbClaimVo> dataTablesOutput = abClaimDao.listAbClaimConfirmedInfo(dataTablesInput, corpAuthSql);
        List<AbClaimVo> data = dataTablesOutput.getData();
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
                "AB_BILL_CLAIM_TYPE",
                "BILL_PAYERPAYEE_TYPE",
                "AB_BILL_CLAIM_STATUS",
                "AB_BILL_TYPE",
                "AB_BILL_FLAG",
                "AB_BILL_NEW_STM_ISSUOFPPR_IND",
                "AB_BILL_SEND_SIGN");
        for (AbClaimVo vo : data) {
            vo.injectValues(listDictMap);
        }
        //设置是否过滤票据账号
        List<BankTypeByAccDTO> bankTypeByAccDTOS = iBankTypeService.listBankTypeByAcc(data.stream().map(AbClaimVo::getBillBankAcc).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(bankTypeByAccDTOS)) {
            return dataTablesOutput;
        }
        //给银行账号set详细信息
        Map<String, BankTypeByAccDTO> accDTOMap = bankTypeByAccDTOS.stream().collect(Collectors.toMap(BankTypeByAccDTO::getBankAcc, Function.identity()));
        Set<String> corpIdSet = new HashSet<>();
        for (AbClaimVo vo : data) {
            if (StringUtil.isEmpty(vo.getBillBankAcc())) {
                continue;
            }
            BankTypeByAccDTO dto = accDTOMap.get(vo.getBillBankAcc());
            if(dto!=null){
                vo.setBankTypeId(dto.getBankTypeId());
                vo.setBankTypeName(dto.getBankTypeName());
            }else{
                if(StrUtil.isNotBlank(vo.getBankTypeId())){
                    BankTypeDTO bankTypeDTO = iBankTypeService.getObjectById(vo.getBankTypeId());
                    if(bankTypeDTO!=null){
                        vo.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
            }
            if(StrUtil.isNotBlank(vo.getApplyCorpId())){
                corpIdSet.add(vo.getApplyCorpId());
            }
            if(StrUtil.isNotBlank(vo.getCorpId())){
                corpIdSet.add(vo.getCorpId());
            }
            if(StrUtil.isNotBlank(vo.getBusCorpId())){
                corpIdSet.add(vo.getBusCorpId());
            }
        }
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (Object bill : listData){
                AbClaimVo bill2 = (AbClaimVo)bill;
                CorpDTO corpDTO = stringCorpDTOMap.get(bill2.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill2.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill2.getBusCorpId());
                bill2.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill2.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill2.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill2.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill2.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 票据认领已确认银行流水列表
     *
     * @param dataTablesInput 查询参数
     * @return 前端列表数据
     **/
    public DataTablesOutput listAbClaimConfirmedInfoAcc(DataTablesInput dataTablesInput) {
//        String corpAuthSql = SqlUtil.getInSql(
//                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY,
//                        dataTablesInput.getCurrentUserId()), "b.corp_id", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY, corpIds);
        List<listAbClaimInfoAccVO> listAbClaimInfoAcc = abClaimDao.listAbClaimConfirmedInfoAcc(dataTablesInput);
        Map<String, listAbClaimInfoAccVO> accountMap = listAbClaimInfoAcc.stream().collect(Collectors.toMap(listAbClaimInfoAccVO::getId, Function.identity()));
        DataTablesOutput<BisAccDtlBillVO> dataTablesOutput = iBisAccBalService.listAbClaimConfirmedInfoAcc(dataTablesInput, new ArrayList<>(accountMap.keySet()));
        if (!CollectionUtils.isEmpty(listAbClaimInfoAcc)) {
            List<BisAccDtlBillVO> data = dataTablesOutput.getData();
            for (BisAccDtlBillVO vo : data) {
                listAbClaimInfoAccVO accVO = accountMap.get(vo.getClaimId());
                vo.setClaimSequence(accVO.getClaimSequence());
                vo.setClaimRmk(accVO.getClaimRmk());
                vo.setOrigNo(accVO.getOrigNo());
            }
        }
        return dataTablesOutput;
    }
    /**
     * 转认领
     *
     * @param abClaimMiddleVo 中间表vo
     * @param user            用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void transferClaim(AbClaimMiddleVo abClaimMiddleVo, ICurrentUser user) {
        String aids = abClaimMiddleVo.getAids();
        String vvids = abClaimMiddleVo.getVvids();
        String claimIds = abClaimMiddleVo.getClaimIds();
        String briefIds = abClaimMiddleVo.getBriefIds();
        if (StringUtils.isEmpty(aids) || "undefined".equals(aids)
                || StringUtils.isEmpty(claimIds) || "undefined".equals(claimIds)
                || StringUtils.isEmpty(briefIds) || "undefined".equals(briefIds)
                || StringUtils.isEmpty(vvids) || "undefined".equals(vvids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            String[] briefIdArr = briefIds.split(",");
            String[] claimIdArr = claimIds.split(",");
            List<String> vvidList = new ArrayList<>();
            for (int i = 0; i < briefIdArr.length; i++) {
                //会出现多条票据记录对应一条流水信息, 所以在打回其中某一条时需要将对应所有的票据信息打回
                List<AbClaimVo> abClaimVoList = abClaimDao.getBillListByClaimId(claimIdArr[i]);
                for (AbClaimVo abClaimVo : abClaimVoList) {
                    if (!vvidList.contains(abClaimVo.getVvid())) {
                        vvidList.add(abClaimVo.getVvid());
                        //贴现、托收业务需要处理流水数据
                        if (AccebillConstant.BRIEF_STATUS_6012.equals(briefIdArr[i])
                                || AccebillConstant.BRIEF_STATUS_6020.equals(briefIdArr[i])) {
                            backClaimFlow(user, abClaimVo.getAid());
                        }
                    }
                }
            }
            //根据票据认领中间表id集合将银行流水明细对应的claim_id设置为null
            abBtBankAccService.updateByClaimArr(claimIdArr);
            //设置当前从表的票据认领中间表id为null,状态为打回
            abVoucherinfoViceService.updateClaimByIds(vvidList.toArray(new String[0]), null, AccebillConstant.BILL_CLAIM_STATUS_1);
        }
    }
    /**
     * 提交确认
     *
     * @param abClaimMiddleVo 票据认领中间表数据
     * @param user            用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitConfirm(AbClaimMiddleVo abClaimMiddleVo, ICurrentUser user) {
        String claimIds = abClaimMiddleVo.getClaimIds();
        if ((StringUtils.isEmpty(claimIds) || "undefined".equals(claimIds))) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            Date nowDate = new Date();
            String username = user.getUsername();
            String[] claimIdArr = claimIds.split(",");
            // 去除重复的claimId
            claimIdArr = new HashSet<>(Arrays.asList(claimIdArr)).toArray(new String[0]);
            AbClaimMiddle abClaimMiddle = new AbClaimMiddle();
            abClaimMiddle.setClaimTime(nowDate);
            abClaimMiddle.setClaimStatus(AccebillConstant.BILL_CLAIM_STATUS95);
            abClaimMiddle.setClaimPerson(username);
            // 修改票据中间表信息
            abClaimMiddleService.updateByIds(claimIdArr, abClaimMiddle);
            boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
            String claimStartString = abConfigService.getConfig(SysConfigConstant.Module.ACCEBILL, SysConfigConstant.CLAIM_START_DATE).getValue();
            // 可能有一条银行流水对应多条票据记录的情况(假如传入两条相同的claimId，需判断是否已执行)
            for (String claimId : claimIdArr) {
                List<AbClaimVo> claimVoList = abClaimDao.selectClaimVoList(null, isEmitMake, DateUtils.str2Date(claimStartString, DateUtils.date_sdf()), claimId,false);
                if (!CollectionUtils.isEmpty(claimVoList)) {
                    String[] vvIdArr = new String[claimVoList.size()];
                    String[] vIdArr = new String[claimVoList.size()];
                    String[] aIdArr = new String[claimVoList.size()];
                    String[] realMonryArr = new String[claimVoList.size()];
                    String briefId = claimVoList.get(0).getBriefId();
                    //收款人或付款人(选择了几条票据数据，就得有几个收款人)
                    String signeroks = "";
                    for (int i = 0; i < claimVoList.size(); i++) {
                        vIdArr[i] = claimVoList.get(i).getVid();
                        vvIdArr[i] = claimVoList.get(i).getVvid();
                        aIdArr[i] = claimVoList.get(i).getAid();
                        realMonryArr[i] = String.valueOf(claimVoList.get(i).getRealMoney());
                        signeroks = StringUtils.isEmpty(signeroks) ? username : signeroks + "," + username;
                    }
                    // 将对应的从表数据的票据认领中间表状态为95
                    abVoucherinfoViceService.updateClaimByIds(vvIdArr, claimId, AccebillConstant.BILL_CLAIM_STATUS95);
                    //贴现
                    if (AccebillConstant.BRIEF_STATUS_011.equals(briefId)) {
                        AbBillDiscountAccountVO abBillDiscountAccountVo = new AbBillDiscountAccountVO();
                        abBillDiscountAccountVo.setRealMoneys(StringUtils.join(realMonryArr, ","));
                        abBillDiscountAccountVo.setBusinessDate(nowDate);
                        abBillDiscountAccountVo.setSigneroks(signeroks);
                        abBillDiscountAccountVo.setAccIds(StringUtils.join(aIdArr, ","));
                        abBillDiscountAccountService.autoApprovalAbBillDiscountAccount(abBillDiscountAccountVo, user);
                    } else if (AccebillConstant.BRIEF_STATUS_020.equals(briefId)
                            || AccebillConstant.BRIEF_STATUS_021.equals(briefId)) {
                        // 托收
                        AbBillCollectionAccountVo abBillCollectionAccountVo = new AbBillCollectionAccountVo();
                        abBillCollectionAccountVo.setBusinessDate(nowDate);
                        BeanUtils.copyProperties(abClaimMiddleVo, abBillCollectionAccountVo);
                        abBillCollectionAccountVo.setVids(StringUtils.join(vIdArr, ","));
                        abBillCollectionAccountVo.setVvids(StringUtils.join(vvIdArr, ","));
                        abBillCollectionAccountVo.setAids(StringUtils.join(aIdArr, ","));
                        abBillCollectionAccountVo.setSignerok(signeroks);
                        abBillCollectionAccountService.autoApprovalCollectionApply(abBillCollectionAccountVo, user);
                    }
                }
            }
        }
    }
    /**
     * 待确认转待认领
     *
     * @param abClaimMiddleVo 票据认领中间表数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmToClaim(AbClaimMiddleVo abClaimMiddleVo) {
        String vvids = abClaimMiddleVo.getVvids();
        String claimIds = abClaimMiddleVo.getClaimIds();
        if (StringUtils.isEmpty(claimIds) || "undefined".equals(claimIds)
                || StringUtils.isEmpty(vvids) || "undefined".equals(vvids)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            String[] claimIdArr = claimIds.split(",");
            //根据票据认领中间表id集合将银行流水明细对应的claim_id设置为null
            abBtBankAccService.updateByClaimArr(claimIdArr);
            //设置当前从表的票据认领中间表id为null,状态为打回
            abVoucherinfoViceService.updateByClaimArr(claimIdArr);
        }
    }
    /**
     * 处理票据认领流水数据
     *
     * @param user 用户
     * @param id   流水id
     */
    private void backClaimFlow(ICurrentUser user, String id) {
        AbAccrecords abAccrecords = abAccrecordsService.get(id, true);
        // 票据当前流水改为作废
        abAccrecordsService.updatAppPass(abAccrecords.getId(), true, user);
        // 流水删除 status改为-2
        int num = abAccrecordsService.approve(abAccrecords.getId(), AccebillConstant.AB_BILL_BUS_STATUS_2, user);
        if (num != 1) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
        }
        // 上一条流水VALID_SIGN=1
        abAccrecordsService.updatAppPass(abAccrecords.getLastid(), false, user);
        // 从表box_status=11
        abVoucherinfoViceService.update(abAccrecords.getVvid(), AccebillConstant.AB_BOX_STATUS_Y, null, user);
        // 4从表添加打回标记
        abVoucherinfoViceService.updateBackBriefId(abAccrecords.getVvid(), abAccrecords.getBriefId(), abAccrecords.getId());
        abLogService.saveAbLog(abAccrecords.getVvid(), abAccrecords.getId(), abAccrecords.getBriefId(), AccebillConstant.ABLOG_LOGTYPE_REPULSE,
                AccebillConstant.LANG_LOG_APP_BACK, user);
    }
    /**
     * 查询待确认界面的票据记录数据
     *
     * @param dataTablesInput 前端查询参数
     * @return 列表数据
     */
    public DataTablesOutput listWaitConfirmInfo(DataTablesInput dataTablesInput) {
        String corpAuthSql = SqlUtil.getInSql(
                abCorpService.getUserCorpIds(AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY,
                        dataTablesInput.getCurrentUserId()), "P.CORP_ID", true);
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_corpId"));
        //设置是否过滤票据账号
        accountGrantService.setElectricBillAccount(dataTablesInput, AccebillConstant.AB_BILL_RESOURCE_CLAIM_QUERY, corpIds);
        DataTablesOutput dataTablesOutput = abClaimDao.listWaitConfirmConfirmedInfo(dataTablesInput, corpAuthSql);
        List<AbClaimVo> list = dataTablesOutput.getData();
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
                "AB_BILL_CLAIM_TYPE",
                "BILL_PAYERPAYEE_TYPE",
                "AB_BILL_CLAIM_STATUS",
                "AB_BILL_TYPE",
                "AB_BILL_FLAG",
                "AB_BILL_NEW_STM_ISSUOFPPR_IND",
                "AB_BILL_SEND_SIGN");
        for (AbClaimVo vo : list) {
            vo.injectValues(listDictMap);
        }
        List<BankTypeByAccDTO> bankTypeByAccDTOS = iBankTypeService.listBankTypeByAcc(list.stream().map(AbClaimVo::getBillBankAcc).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(bankTypeByAccDTOS)) {
            return dataTablesOutput;
        }
        //给银行账号set详细信息
        Map<String, BankTypeByAccDTO> accDTOMap = bankTypeByAccDTOS.stream().collect(Collectors.toMap(BankTypeByAccDTO::getBankAcc, Function.identity()));
        Set<String> corpIdSet = new HashSet<>();
        for (AbClaimVo vo : list) {
            if (StringUtil.isEmpty(vo.getBillBankAcc())) {
                continue;
            }
            BankTypeByAccDTO dto = accDTOMap.get(vo.getBillBankAcc());
            if (dto!=null){
                vo.setBankTypeId(dto.getBankTypeId());
                vo.setBankTypeName(dto.getBankTypeName());
            }else{
                if(StrUtil.isNotBlank(vo.getBankTypeId())){
                    BankTypeDTO bankTypeDTO = iBankTypeService.getObjectById(vo.getBankTypeId());
                    if(bankTypeDTO!=null){
                        vo.setBankTypeName(bankTypeDTO.getTypeName());
                    }
                }
            }
            if(StrUtil.isNotBlank(vo.getApplyCorpId())){
                corpIdSet.add(vo.getApplyCorpId());
            }
            if(StrUtil.isNotBlank(vo.getCorpId())){
                corpIdSet.add(vo.getCorpId());
            }
            if(StrUtil.isNotBlank(vo.getBusCorpId())){
                corpIdSet.add(vo.getBusCorpId());
            }
        }
        if(corpIdSet.size() <= 0){
            return dataTablesOutput;
        }
        Map<String, CorpDTO> stringCorpDTOMap = abCorpService.listCorpDtoByCorpIds(new ArrayList<>(corpIdSet));
        if(CollectionUtil.isEmpty(stringCorpDTOMap)){
            return dataTablesOutput;
        }
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData->{
            for (Object bill : listData){
                AbClaimVo bill2 = (AbClaimVo)bill;
                CorpDTO corpDTO = stringCorpDTOMap.get(bill2.getApplyCorpId());
                CorpDTO corpDTO2 = stringCorpDTOMap.get(bill2.getCorpId());
                CorpDTO corpDTO3 = stringCorpDTOMap.get(bill2.getBusCorpId());
                bill2.setApplyCorpName(corpDTO == null ? null : corpDTO.getName());
                bill2.setApplyCorpCode(corpDTO == null ? null : corpDTO.getCode());
                bill2.setCorpName(corpDTO2 == null ? null : corpDTO2.getName());
                bill2.setCorpCode(corpDTO2 == null ? null : corpDTO2.getCode());
                bill2.setBusCorpName(corpDTO3 == null ? null : corpDTO3.getName());
            }
        });
        return dataTablesOutput;
    }
    /**
     * 自动认领银行流水定时任务触发执行
     *
     * @param arguments 参数
     */
    @Override
    @XxlJob("AbClaimService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        logger.info("定时任务接收参数：【{}】", arguments);
        try {
            ICurrentUser userVo = abUserService.getScheduler();
            UserDTO user = null;
            //默认用户
            if (null == userVo){
                user = new UserDTO();
                user.setId("402880425b6b0a58015b6b0abeb10000");
                user.setLoginName("admin");
                user.setUsername("admin");
                userVo = user;
            }
            this.logger.info("---启用承兑汇票-自动认领银行流水，开始执行定时任务---");
            autoClaimBankAcc(userVo, arguments);
            this.logger.info("---启用承兑汇票-自动认领银行流水，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 自动认领银行流水
     *
     * @param userVo 定时任务默认用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void autoClaimBankAcc(ICurrentUser userVo,String arguments) {
        boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
        // 票据参数集合
        Map<String, Object> billConfigMap = (Map<String, Object>) (abConfigService.listByType(AccebillConstant.AB_CONFIG_TYPE)).get(AccebillConstant.AB_CONFIG_TYPE);
        // 认领起始时间
        String claimStartString = String.valueOf(billConfigMap.get(SysConfigConstant.CLAIM_START_DATE));
        if (StringUtils.isBlank(claimStartString) || "null".equals(claimStartString)) {
            //若没有设置认领起始时间,则按默认认领时间计算
            claimStartString = "2025-01-01";
        }
        Date endDate = DateUtils.addDay(DateUtils.getDateYMD(),1);
        // 票据记录集合
        List<AbClaimVo> abClaimVoList = abClaimDao.selectClaimVoList(null, isEmitMake, DateUtils.str2Date(claimStartString, DateUtils.date_sdf()), null, endDate,true);
        if (CollectionUtils.isEmpty(abClaimVoList)) {
            this.logger.info("---启用承兑汇票-自动认领银行流水，没有票据记录集合---{}",claimStartString);
            return;
        }else{
            this.logger.info("---启用承兑汇票-自动认领银行流水，票据记录总集合数量---{}",abClaimVoList.size());
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(
                    "AB_BILL_CLAIM_TYPE",
                    "BILL_PAYERPAYEE_TYPE",
                    "AB_BILL_CLAIM_STATUS",
                    "AB_BILL_TYPE",
                    "AB_BILL_FLAG",
                    "AB_BILL_NEW_STM_ISSUOFPPR_IND",
                    "AB_BILL_SEND_SIGN");
            for(AbClaimVo vo :abClaimVoList){
                if(StringUtils.isEmpty(vo.getBankTypeId())){
                    BtBankAccDTO bankAcc = bankAccService.getByBankAcc(vo.getBillBankAcc());
                    if(bankAcc != null){
                        vo.setBankTypeId(bankAcc.getBankTypeId());
                        vo.setBankTypeName(bankAcc.getBankTypeName());
                    }
                }
                vo.injectValues(listDictMap);
            }
        }
        // 银行流水集合
        Map<String, Object> paramsAccDtl = new HashMap<String, Object>(8);
        //默认只勾兑近10天的交易明细
        if(StringUtils.isEmpty(arguments)){
            paramsAccDtl.put("transTimeLeft",DateUtils.formatDate(DateUtils.addDay(new Date(),-10)));
        }else if(arguments.length()<5){
            paramsAccDtl.put("transTimeLeft",DateUtils.formatDate(DateUtils.addDay(new Date(),-1*Integer.parseInt(arguments))));
        } else {
            paramsAccDtl.put("transTimeLeft", arguments);
        }
        String transTimeLeft=(String)paramsAccDtl.get("transTimeLeft");
        List<BisAccDtlVo> accDtlVoList = abClaimDao.selectBankAccList(paramsAccDtl);
        if (CollectionUtils.isEmpty(accDtlVoList)) {
            this.logger.info("---启用承兑汇票-自动认领银行流水，没有银行流水集合---{}",arguments);
            return;
        }else{
            this.logger.info("---启用承兑汇票-自动认领银行流水，银行流水集合数量---{}",accDtlVoList.size());
        }
        // 将借贷标志和银行类型作为key，保存对应的银行流水信息
        HashMap<String, ArrayList<BisAccDtlVo>> accDtlHashMap = new HashMap<>(16);
        for (BisAccDtlVo bisAccDtlVo : accDtlVoList) {
            String key = bisAccDtlVo.getCdSign() + "-" + bisAccDtlVo.getBankTypeId();
            ArrayList<BisAccDtlVo> bisAccDtlVos = accDtlHashMap.get(key);
            if (!CollectionUtils.isEmpty(bisAccDtlVos)) {
                bisAccDtlVos.add(bisAccDtlVo);
            } else {
                accDtlHashMap.put(key, Lists.newArrayList(bisAccDtlVo));
            }
        }
        // 认领参数
        // 票号规则参数
        boolean ruleBillNoConfig = AccebillConstant.BILL_CONFIG_CLAIM1.equals(billConfigMap.get("ruleBillNo"));
        // 日期、金额、账号相同
        boolean sameDateMoneyAccConfig = AccebillConstant.BILL_CONFIG_CLAIM1.equals(billConfigMap.get("sameDateMoneyAcc"));
        // 日期相同、金额相同，账号在规则范围内
        boolean sameDateMoneyConfig = AccebillConstant.BILL_CONFIG_CLAIM1.equals(billConfigMap.get("sameDateMoney"));
        // 日期相同、账号相同或在规则范围内，金额符合金额规则在上浮或下浮范围内
        boolean sameDateAccConfig = AccebillConstant.BILL_CONFIG_CLAIM1.equals(billConfigMap.get("sameDateAcc"));
        // 金额相同、账号相同或在规则范围内，日期在延期范围内
        boolean sameMoneyAccConfig = AccebillConstant.BILL_CONFIG_CLAIM1.equals(billConfigMap.get("sameMoneyAcc"));
        // 账号规则参数
        // 是否勾选贴现：贴现办理入账账户参数
        boolean billBackDesc = AccebillConstant.BILL_CONFIG_CHECK1.equals(billConfigMap.get("billBackDesc"));
        // 是否勾选托收：票据所属账户参数
        boolean isDiscountConfirm = AccebillConstant.BILL_CONFIG_CHECK1.equals(billConfigMap.get("isDiscountConfirm"));
        // 是否勾选兑付：银票
        boolean accountByCashing1 = AccebillConstant.BILL_CONFIG_CHECK1.equals(billConfigMap.get("accountByCashing"));
        // 是否勾选兑付：商票
        boolean accountByCashing2 = AccebillConstant.BILL_CONFIG_CHECK1.equals(billConfigMap.get("accountByCashing2"));
        // 金额规则参数
        // 贴现
        String[] moneyByDiscount = new String[]{String.valueOf(billConfigMap.get("moneyByDiscount")),
                String.valueOf(billConfigMap.get("moneyByDiscount1")),
                String.valueOf(billConfigMap.get("ratesFloatPer1")),
                String.valueOf(billConfigMap.get("ratesFloatPer2"))
        };
        // 兑付票面金额
        String[] moneyByCashing = new String[]{String.valueOf(billConfigMap.get("moneyByCashing")),
                String.valueOf(billConfigMap.get("moneyByCashing1")),
                String.valueOf(billConfigMap.get("ratesFloatPer3")),
                String.valueOf(billConfigMap.get("ratesFloatPer4"))
        };
        // 兑付解付金额
        String[] moneyByPayCashing = new String[]{String.valueOf(billConfigMap.get("moneyByPayCashing")),
                String.valueOf(billConfigMap.get("upMoney")),
                String.valueOf(billConfigMap.get("downMoney"))
        };
        // 日期规则参数
        // 贴现
        String[] dateByDiscount = new String[]{String.valueOf(billConfigMap.get("dateByDiscount")),
                String.valueOf(billConfigMap.get("isDateByDiscountDay")),
                String.valueOf(billConfigMap.get("dateByDiscountDayNum"))
        };
        // 托收
        String[] dateByCollection = new String[]{String.valueOf(billConfigMap.get("dateByCollection")),
                String.valueOf(billConfigMap.get("isDateByCollectionDay")),
                String.valueOf(billConfigMap.get("dateByCollectionDayNum"))
        };
        // 兑付
        String[] dateByCashing = new String[]{String.valueOf(billConfigMap.get("dateByCashing")),
                String.valueOf(billConfigMap.get("isDateByCashingDay")),
                String.valueOf(billConfigMap.get("dateByCashingDayNum"))
        };
        // 存储已认领的票据号集合
        ArrayList<String> origNoList = new ArrayList<>();
        // 存储已认领的银行流水id集合
        List<String> accIdList = new ArrayList<>();
        // 票号规则集合
        List<AbBillRuleBillNo> billRuleBillNoList = abBillRuleBillNoService.listValidInfo();
        HashMap<String, AbBillRuleBillNo> billRuleHashMap = new HashMap<>(8);
        for (AbBillRuleBillNo billRuleBillNo : billRuleBillNoList) {
            billRuleHashMap.put(billRuleBillNo.getBusType() + "-" + billRuleBillNo.getBankTypeId(), billRuleBillNo);
        }
        //获取票据流水备注规则集合
        List<AbBillRuleBillDtlRmkVO> rules = abBillRuleBillDtlRmkService.listValidInfo();
        HashMap<String, List<AbBillRuleBillDtlRmkDtlVO>> billRmkRuleHashMap = new HashMap<>(8);
        if(!CollectionUtils.isEmpty(rules)){
            for (AbBillRuleBillDtlRmkVO vo : rules) {
                List<AbBillRuleBillDtlRmkDtlVO> ruleDtls = abBillRuleBillDtlRmkService.getRulesByBankTypeIdAndBusType(vo.getBankTypeId(),vo.getBustype());
                billRmkRuleHashMap.put(vo.getBustype()+ "-" + vo.getBankTypeId(), ruleDtls);
            }
        }

        //对需要汇总数据和非汇总数据进行区分
        List<AbClaimVo> abClaimVoSumList = new ArrayList<>();
        List<AbClaimVo> abClaimVoOtherList = new ArrayList<>();
        abClaimVoList.forEach(abClaimVo->{
            // 判断银行类型和业务类型判断是否有对应票号规则
            String ruleKey = "003-" + abClaimVo.getBankTypeId();
            AbBillRuleBillNo abBillRuleBillNo = billRuleHashMap.get(ruleKey);
            if(abBillRuleBillNo!=null && AccebillConstant.IS_SUMMARY_CLAIM_BILL.equals(abBillRuleBillNo.getIsSummaryClaimBill())){
                abClaimVoSumList.add(abClaimVo);
            }else{
                abClaimVoOtherList.add(abClaimVo);
            }
        });

        //兑付多张票据对应一笔流水的自动认领
        if(CollUtil.isNotEmpty(abClaimVoSumList)){
            //abClaimVoList根据到期日enddate和账号bankacc分组
            Map<String, List<AbClaimVo>>  abClaimVoMapGroupByEndDateAndBankAcc = abClaimVoSumList.stream().collect(Collectors.groupingBy(e -> e.getBankAcc() + "-" + DateUtils.formatDate(e.getEndDate())));
            Set<String> keySet = abClaimVoMapGroupByEndDateAndBankAcc.keySet();
            int addDay = Integer.parseInt(dateByCashing[2]==null?"0":dateByCashing[2]);
            for(String key:keySet){
                List<AbClaimVo> abClaimVoSumKeyList = abClaimVoMapGroupByEndDateAndBankAcc.get(key);
                if(CollUtil.isNotEmpty(abClaimVoSumKeyList)){
                    AbClaimVo abClaimVo = abClaimVoSumKeyList.get(0);
                    // 业务类型
                    String claimTypeName = abClaimVo.getClaimTypeName();
                    String busType = "托收".equals(claimTypeName) ? "001" : "贴现".equals(claimTypeName) ? "002" : "003";
                    // 借贷标志
                    String cdSign = AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(abClaimVo.getBillSort())
                            ? AccebillConstant.AB_BILL_CD_SIGN_1 : AccebillConstant.AB_BILL_CD_SIGN_0;
                    //获取票据流水备注规则
                    List<AbBillRuleBillDtlRmkDtlVO> abBillRuleBillDtlRmkDtlVOS = billRmkRuleHashMap.get(busType + "-" + abClaimVo.getBankTypeId());
                    String billRmkRuleCondition = null;
                    if (!CollectionUtils.isEmpty(abBillRuleBillDtlRmkDtlVOS)) {
                        billRmkRuleCondition = assembleRuleCondition(abBillRuleBillDtlRmkDtlVOS);
                    }
                    abClaimVo.setTransTimeLeft(transTimeLeft);
                    oneToManyBillAutoMatch(userVo,abClaimVoMapGroupByEndDateAndBankAcc,ruleBillNoConfig,
                            abClaimVo,cdSign,busType,billRuleHashMap,moneyByPayCashing,billRmkRuleCondition,accIdList,origNoList,addDay);
                }
            }
        }

        boolean executeFlag = false;
        // 校验当前日期是否工作日，是工作日才执行
        // 有银行的票是非工作日到期，但是银行在工作日才付款生成流水，导致票号规则认领不到，认领不到会往后走其他规则匹配，导致认领到错误的流水
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            executeFlag = true;
        }
        if(CollUtil.isNotEmpty(abClaimVoOtherList) && executeFlag) {
            abClaimVoList = abClaimVoOtherList;
            // 遍历票据记录列表
            for (AbClaimVo abClaimVo : abClaimVoList) {
                abClaimVo.setTransTimeLeft(transTimeLeft);
                // 业务类型
                String claimTypeName = abClaimVo.getClaimTypeName();
                String busType = "托收".equals(claimTypeName) ? "001" : "贴现".equals(claimTypeName) ? "002" : "003";
                // 借贷标志
                String cdSign = AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(abClaimVo.getBillSort())
                        ? AccebillConstant.AB_BILL_CD_SIGN_1 : AccebillConstant.AB_BILL_CD_SIGN_0;
                // 账号
                String bankAcc = abClaimVo.getBankAcc();
                if (origNoList.contains(abClaimVo.getOrigNo()) || StringUtils.isEmpty(bankAcc) || StringUtils.isEmpty(abClaimVo.getOrigNo())
                        || StringUtils.isEmpty(abClaimVo.getBankTypeId()) || abClaimVo.getBillMoney() == null) {
                    continue;
                }
                // 票号规则匹配
                if (origNoRuleMatch(userVo, isEmitMake, claimStartString, accDtlHashMap, ruleBillNoConfig, origNoList,
                        billRuleHashMap, abClaimVo, busType, cdSign, accIdList)) {
                    continue;
                }
                // 日期+金额+账号匹配
                if (sameDateMoneyAccMatch(userVo, sameDateMoneyAccConfig, origNoList, abClaimVo, cdSign, accIdList)) {
                    continue;
                }
                // 根据账号规则获取对应的账号集合
                String[] bankAccArr = getBankAccByRule(billBackDesc, isDiscountConfirm, accountByCashing1, accountByCashing2, abClaimVo);
                // 日期、金额相同，账号在账号规则范围内
                if (!StringUtils.isEmpty(bankAccArr)) {
                    if (sameDateMoneyMatch(userVo, sameDateMoneyConfig, origNoList, abClaimVo, cdSign, bankAccArr, accIdList)) {
                        continue;
                    }
                } else {
                    bankAccArr = new String[]{bankAcc};
                }
                //获取票据流水备注规则
                List<AbBillRuleBillDtlRmkDtlVO> abBillRuleBillDtlRmkDtlVOS = billRmkRuleHashMap.get(busType + "-" + abClaimVo.getBankTypeId());
                String billRmkRuleCondition = null;
                if (!CollectionUtils.isEmpty(abBillRuleBillDtlRmkDtlVOS)) {
                    billRmkRuleCondition = assembleRuleCondition(abBillRuleBillDtlRmkDtlVOS);
                }
                // 日期相同、账号相同或在规则范围内，金额符合金额规则在上浮或下浮范围内
                if (sameDateAccMatch(userVo, sameDateAccConfig, moneyByDiscount, moneyByCashing, moneyByPayCashing, origNoList, abClaimVo,
                        cdSign, bankAccArr, accIdList, billRmkRuleCondition)) {
                    continue;
                }
                // 金额相同、账号相同或在规则范围内，日期在延期范围内
                if (sameMoneyAccMatch(userVo, sameMoneyAccConfig, dateByDiscount, dateByCollection, dateByCashing, origNoList,
                        abClaimVo, cdSign, bankAccArr, accIdList)) {
                    continue;
                }

                // 日期相同、账号相同或在规则范围内，金额符合金额规则在上浮或下浮范围内 ，日期在延期范围内
                if (sameDateMoneyAccMatch(userVo, sameMoneyAccConfig,
                        moneyByDiscount, moneyByCashing, moneyByPayCashing,
                        dateByDiscount, dateByCollection, dateByCashing, origNoList,
                        abClaimVo, cdSign, bankAccArr, accIdList, billRmkRuleCondition)) {
                    continue;
                }
//            //兑付多张票据对应一笔流水的自动认领
//            if(oneToManyBillAutoMatch(userVo,abClaimVoMapGroupByEndDateAndBankAcc,ruleBillNoConfig,abClaimVo,cdSign,busType,billRuleHashMap,moneyByPayCashing,billRmkRuleCondition,accIdList,origNoList)){
//                continue;
//            }
            }
        }
    }

    /**
    * @Description 兑付多张票据对应一笔流水的自动认领
    * @author zhous
    * @date 2025/5/16 17:41
    */
    private boolean oneToManyBillAutoMatch(ICurrentUser userVo,Map<String, List<AbClaimVo>> abClaimVoMapGroupByEndDateAndBankAcc,boolean ruleBillNoConfig, AbClaimVo abClaimVo, String cdSign,String busType, HashMap<String, AbBillRuleBillNo> billRuleHashMap, String[] moneyByPayCashing,
                                           String billRmkRuleCondition, List<String> accIdList, ArrayList<String> origNoList,int addDays) {
        if (!CollectionUtils.isEmpty(billRuleHashMap)) {
            HashMap<String, Object> params = new HashMap<>(8);
            if(!"003".equals(busType)){
                return false;
            }
            String bankTypeId = abClaimVo.getBankTypeId();
            Date claimDate = abClaimVo.getClaimDate();
            // 判断银行类型和业务类型判断是否有对应票号规则
            String ruleKey = busType + "-" + bankTypeId;
            AbBillRuleBillNo abBillRuleBillNo = billRuleHashMap.get(ruleKey);
            // 获取根据到期日和账号分组的票据集合
            String key = abClaimVo.getBankAcc() + "-" + DateUtils.formatDate(abClaimVo.getEndDate());
            if (abBillRuleBillNo != null && AccebillConstant.IS_SUMMARY_CLAIM_BILL.equals(abBillRuleBillNo.getIsSummaryClaimBill()) && abClaimVoMapGroupByEndDateAndBankAcc.containsKey(key) ) {
                List<AbClaimVo> abClaimVos = abClaimVoMapGroupByEndDateAndBankAcc.get(key);
                if(CollUtil.isEmpty(abClaimVos)){
                    return false;
                }
                BigDecimal totalPayMoney = abClaimVos.stream().map(AbClaimVo::getPayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                if(StringUtils.isNotEmpty(moneyByPayCashing)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByPayCashing[0])){
                    BigDecimal upMoney = BigDecimal.ZERO;
                    BigDecimal downMoney = BigDecimal.ZERO;
                    if(StrUtil.isNotEmpty(moneyByPayCashing[1])){
                        upMoney = new BigDecimal(moneyByPayCashing[1]);
                    }
                    if(StrUtil.isNotEmpty(moneyByPayCashing[2])){
                        downMoney = new BigDecimal(moneyByPayCashing[2]);
                    }
                    params.put("billPayMoneyLeft", totalPayMoney.subtract(downMoney).setScale(2, BigDecimal.ROUND_UP));
                    params.put("billPayMoneyRight", totalPayMoney.add(upMoney).setScale(2, BigDecimal.ROUND_UP));
                }
                params.put("cdSign", cdSign);
                params.put("bankAcc",  new String[]{abClaimVo.getBankAcc()});
                params.put("bankTypeId", bankTypeId);
                params.put("accIdList", accIdList.toArray(new String[0]));
                if(StrUtil.isNotEmpty(abClaimVo.getTransTimeLeft())){
                    params.put("transTimeLeft", abClaimVo.getTransTimeLeft());
                }
                if(StringUtils.isNotBlank(billRmkRuleCondition)){
                    params.put("ruleSql", billRmkRuleCondition);
                }
                params.put("claimDateLeft", DateUtils.date2Str(claimDate, DateUtils.date_sdf()));
                params.put("claimDateRight", DateUtils.date2Str(DateUtils.addDay(claimDate, addDays), DateUtils.date_sdf()));
                List<BisAccDtlVo> accDtlVos = abClaimDao.selectBankAccList(params);
                if(accDtlVos.size() == 1){
                    String rmk = "";
                    List<String> origNosList = new ArrayList<>();
                    String[] vvIdArr = new String[abClaimVos.size()];
                    // 一条流水数据如果对应多条票据信息时，需要将票据号和票据金额存入备注中
                    if (abClaimVos.size() > 0) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        for (int i = 0; i < abClaimVos.size(); i++) {
                            AbClaimVo claimVo = abClaimVos.get(i);
                            if (StringUtils.isEmpty(rmk)) {
                                rmk = claimVo.getOrigNo() + "|" + df.format(claimVo.getBillMoney());
                            } else {
                                rmk += "," + claimVo.getOrigNo() + "|" + df.format(claimVo.getBillMoney());
                            }
                            origNosList.add(claimVo.getOrigNo());
                            vvIdArr[i] = claimVo.getVvid();
                        }
                    }
                    String username = userVo.getUsername();
                    AbClaimMiddle abClaimMiddle = new AbClaimMiddle();
                    Date nowDate = new Date();
                    abClaimMiddle.setCreateDate(nowDate);
                    abClaimMiddle.setCreateBy(userVo.getLoginName());
                    abClaimMiddle.setClaimTime(nowDate);
                    abClaimMiddle.setClaimPerson(username);
                    abClaimMiddle.setClaimStatus(ruleBillNoConfig ? AccebillConstant.BILL_CLAIM_STATUS95 : AccebillConstant.BILL_CLAIM_STATUS94);
                    abClaimMiddle.setClaimSequence(SequenceSingleton.getSequence(SequenceEnum.BILL_CLAIM_MIDDLE_SEQUENCE));
                    abClaimMiddle.setOrigNo(abClaimVo.getOrigNo());
                    abClaimMiddle.setRmk(rmk);
                    origNoList.addAll(origNosList);
                    abClaimMiddleService.saveEntity(abClaimMiddle);
                    // 将对应的银行账号明细表数据的票据认领中间表id设置为当前新增数据id
                    for (BisAccDtlVo accDtlVo : accDtlVos) {
                        accIdList.add(accDtlVo.getId());
                        BisAccDtlDTO bisAccDtl = BeanUtils.copyNewInstanceProperties(accDtlVo, BisAccDtlDTO.class);
                        bisAccDtl.setClaimId(abClaimMiddle.getId());
                        abBtBankAccService.updateDtlByDto(bisAccDtl);
                    }
                    try {
                        // 将对应的从表数据的票据认领中间表id设置为当前新增数据id
                        abVoucherinfoViceService.updateClaimByIds(vvIdArr, abClaimMiddle.getId(), abClaimMiddle.getClaimStatus());
                    }catch (Exception ex){
                        logger.error("String[] vvidArr={"+Arrays.toString(vvIdArr)+"}, String claimId={"+abClaimMiddle.getId()+"}, String claimStatus={"+abClaimMiddle.getClaimStatus()+"} key="+key+"  accDtlVos="+accDtlVos.toString()+"  abClaimVos:"+abClaimVos.toString()+" abClaimMiddle="+abClaimMiddle.toString());
                        throw new RuntimeException(ex.getMessage(),ex);
                    }
                }else{
                    //未查询到流水或者查询到多条流水不匹配
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 金额相同、账号相同或在规则范围内，日期在延期范围内
     *
     * @param userVo             用户
     * @param sameMoneyAccConfig 是否自动认领
     * @param dateByDiscount     日期规则参数：贴现
     * @param dateByCollection   日期规则参数：托收
     * @param dateByCashing      日期规则参数：兑付
     * @param origNoList         票据号集合
     * @param abClaimVo          票据记录vo
     * @param cdSign             借贷标志
     * @param bankAccArr         银行账号数组
     * @param accIdList          银行流水记录id集合
     */
    private boolean sameMoneyAccMatch(ICurrentUser userVo, boolean sameMoneyAccConfig, String[] dateByDiscount, String[] dateByCollection,
                                   String[] dateByCashing, ArrayList<String> origNoList, AbClaimVo abClaimVo, String cdSign,
                                   String[] bankAccArr, List<String> accIdList) {
        boolean dateFlag;
        int addDays;
        boolean judgeBrief11 = AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId());
        boolean judgeBrief20And21 = AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId());
        if (judgeBrief11) {
            dateFlag = StringUtils.isNotEmpty(dateByDiscount)
                    && (dateByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByDiscount[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByDiscount[1]);
        } else if (judgeBrief20And21) {
            dateFlag = StringUtils.isNotEmpty(dateByCollection)
                    && (dateByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCollection[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCollection[1]);
        } else {
            dateFlag = StringUtils.isNotEmpty(dateByCashing)
                    && (dateByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCashing[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCashing[1]);
        }
        if (dateFlag) {
            Date claimDate = abClaimVo.getClaimDate();
            HashMap<String, Object> params = new HashMap<>(8);
            addDays = Integer.parseInt(judgeBrief11 ? dateByDiscount[2] : judgeBrief20And21 ? dateByCollection[2] : dateByCashing[2]);
            params.put("billMoney", abClaimVo.getBillMoney());
            params.put("bankAcc", bankAccArr);
            params.put("cdSign", cdSign);
            params.put("accIdList", accIdList.toArray(new String[0]));
            params.put("claimDateLeft", DateUtils.date2Str(claimDate, DateUtils.date_sdf()));
            params.put("claimDateRight", DateUtils.date2Str(DateUtils.addDay(claimDate, addDays), DateUtils.date_sdf()));
            return dateMoneyAccMatch(userVo, sameMoneyAccConfig, origNoList, abClaimVo, params, accIdList);
        }
        return false;
    }
    /**
     * 日期相同、账号相同或在规则范围内，金额符合金额规则在上浮或下浮范围内
     *
     * @param userVo            用户
     * @param sameDateAccConfig 是否自动认领
     * @param moneyByDiscount   金额规则参数：贴现
     * @param moneyByCashing    金额规则参数：兑付 票面金额
     * @param moneyByPayCashing    金额规则参数：兑付 解付金额
     * @param origNoList        票据号集合
     * @param abClaimVo         票据记录vo
     * @param cdSign            借贷标志
     * @param bankAccArr        银行账号数组
     * @param accIdList         银行流水记录id集合
     * @return 是否匹配成功
     */
    private boolean sameDateAccMatch(ICurrentUser userVo, boolean sameDateAccConfig, String[] moneyByDiscount,
                                     String[] moneyByCashing,String[] moneyByPayCashing, ArrayList<String> origNoList, AbClaimVo abClaimVo,
                                     String cdSign, String[] bankAccArr, List<String> accIdList,String billRmkRuleCondition) {
        boolean moneyFlag;
        BigDecimal billMoney = abClaimVo.getBillMoney();
        // 银票或商票的上浮或下浮率
        BigDecimal rate;
        if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
            // 贴现业务
            moneyFlag = StringUtils.isNotEmpty(moneyByDiscount)
                    && (moneyByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByDiscount[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByDiscount[1]);
        } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId())) {
            // 托收业务不进行该规则匹配
            moneyFlag = false;
        } else {
            // 兑付
            moneyFlag = (StringUtils.isNotEmpty(moneyByCashing)
                    && (moneyByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[1]))
                    ||
                    (StringUtils.isNotEmpty(moneyByPayCashing)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByPayCashing[0]));
        }
        if (moneyFlag) {
            HashMap<String, Object> params = new HashMap<>(8);
            BigDecimal money = billMoney;
            BigDecimal payMoney = abClaimVo.getPayMoney();;
            if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
                rate = new BigDecimal(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abClaimVo.getBillType()))
                        ? moneyByDiscount[2] : moneyByDiscount[3]);
                money = money.multiply(new BigDecimal(1).subtract(rate));
                params.put("billMoneyLeft", money);
                params.put("billMoneyRight", billMoney);
            } else {
                if(StringUtils.isNotEmpty(moneyByCashing)
                        && (moneyByDiscount.length > 1)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[0])
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[1])){
                    rate = new BigDecimal(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abClaimVo.getBillType()))
                            ? moneyByCashing[2] : moneyByCashing[3]);
                    money = money.multiply(new BigDecimal(1).add(rate));
                    params.put("billMoneyLeft", billMoney);
                    params.put("billMoneyRight", money);
                }
                if(StringUtils.isNotEmpty(moneyByPayCashing)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByPayCashing[0])){
                    BigDecimal upMoney = BigDecimal.ZERO;
                    BigDecimal downMoney = BigDecimal.ZERO;
                    if(StrUtil.isNotEmpty(moneyByPayCashing[1])){
                        upMoney = new BigDecimal(moneyByPayCashing[1]);
                    }
                    if(StrUtil.isNotEmpty(moneyByPayCashing[2])){
                        downMoney = new BigDecimal(moneyByPayCashing[2]);
                    }
                    params.put("billPayMoneyLeft", payMoney.subtract(downMoney).setScale(2, BigDecimal.ROUND_UP));
                    params.put("billPayMoneyRight", payMoney.add(upMoney).setScale(2, BigDecimal.ROUND_UP));
                }
            }
            params.put("bankAcc", bankAccArr);
            params.put("cdSign", cdSign);
            params.put("accIdList", accIdList.toArray(new String[0]));
            params.put("claimDate", DateUtils.date2Str(abClaimVo.getClaimDate(), DateUtils.date_sdf()));
            if(StringUtils.isNotBlank(billRmkRuleCondition)){
                params.put("ruleSql", billRmkRuleCondition);
            }
            return dateMoneyAccMatch(userVo, sameDateAccConfig, origNoList, abClaimVo, params, accIdList);
        }
        return false;
    }

    /**
    * @Description 规则条件组装
    * @author zhous
    * @date 2025/2/25 14:56
    */
    private String assembleRuleCondition(List<AbBillRuleBillDtlRmkDtlVO> rules) {
        StringBuilder ruleCondition = new StringBuilder( " and ( ");
        if(!CollectionUtils.isEmpty(rules)){
            for (AbBillRuleBillDtlRmkDtlVO rule : rules){
                if(ObjectUtil.isNotEmpty(rule.getMatchElements()) && ObjectUtil.isNotEmpty(rule.getMatchRelations())){
                    if (ObjectUtil.isNotEmpty(rule.getMatchRule()) && ObjectUtil.isNotEmpty(rule.getMatchRule().trim())){
                        if ("01".equals(rule.getMatchRule())){
                            ruleCondition.append(" or ");
                        }else if ("02".equals(rule.getMatchRule())){
                            ruleCondition.append(" and ");
                        }
                    }else{
                        if(!" and ( ".equals(ruleCondition.toString())){
                            ruleCondition.append(" and ");
                        }
                    }
                    if (ObjectUtil.isNotEmpty(rule.getLeftParenthesis()) && ObjectUtil.isNotEmpty(rule.getLeftParenthesis().trim())){
                        ruleCondition.append(" ( ");
                    }
                    String matchElements = rule.getMatchElements();
                    /**匹配关系 1-包含、2-不包含、3-等于、4-不等于*/
                    String matchRelations = rule.getMatchRelations().trim();
                    if ("1".equals(matchRelations)){
                        matchRelations = " like '%tagContent%' ";
                    }else if ("2".equals(matchRelations)){
                        matchRelations = " not like '%tagContent%' ";
                    }else if ("3".equals(matchRelations)){
                        matchRelations = " = 'tagContent' ";
                    }else if ("4".equals(matchRelations)){
                        matchRelations = " <> 'tagContent' ";
                    }
                    String matchContent = rule.getMatchContent();
                    matchRelations = matchRelations.replace("tagContent",matchContent);
                    if(StringUtils.isNotBlank(matchElements)){
                        ruleCondition.append(" acc.");
                        ruleCondition.append(matchElements);
                        ruleCondition.append(" ");
                        ruleCondition.append(matchRelations);
                    }
                    if (ObjectUtil.isNotEmpty(rule.getRightParenthesis()) && ObjectUtil.isNotEmpty(rule.getRightParenthesis().trim())){
                        ruleCondition.append(" ) ");
                    }
                }
            }
            ruleCondition.append(" ) ");
        }
        return ruleCondition.toString();
    }

    /**
     * 日期相同、账号相同或在规则范围内，金额符合金额规则在上浮或下浮范围内 日期状延期范围内
     *
     * @param userVo            用户
     * @param sameDateAccConfig 是否自动认领
     * @param moneyByDiscount   金额规则参数：贴现
     * @param moneyByCashing    金额规则参数：兑付 票面金额
     * @param moneyByPayCashing    金额规则参数：兑付 解付金额
     * @param origNoList        票据号集合
     * @param abClaimVo         票据记录vo
     * @param cdSign            借贷标志
     * @param bankAccArr        银行账号数组
     * @param accIdList         银行流水记录id集合
     * @return 是否匹配成功
     */
    private boolean sameDateMoneyAccMatch(ICurrentUser userVo, boolean sameDateAccConfig,
                                          String[] moneyByDiscount, String[] moneyByCashing,String[] moneyByPayCashing,
                                          String[] dateByDiscount, String[] dateByCollection,String[] dateByCashing,
                                          ArrayList<String> origNoList, AbClaimVo abClaimVo,
                                     String cdSign, String[] bankAccArr, List<String> accIdList,String billRmkRuleCondition) {
        boolean moneyFlag;
        BigDecimal billMoney = abClaimVo.getBillMoney();
        // 银票或商票的上浮或下浮率
        BigDecimal rate;
        if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
            // 贴现业务
            moneyFlag = StringUtils.isNotEmpty(moneyByDiscount)
                    && (moneyByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByDiscount[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByDiscount[1]);
        } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId())) {
            // 托收业务不进行该规则匹配
            moneyFlag = false;
        } else {
            // 兑付
            moneyFlag = (StringUtils.isNotEmpty(moneyByCashing)
                    && (moneyByDiscount.length > 1)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[0])
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[1]))
                    ||
                    (StringUtils.isNotEmpty(moneyByPayCashing)
                    && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByPayCashing[0]));
        }
        //金额浮动
        if (moneyFlag) {
            HashMap<String, Object> params = new HashMap<>(8);
            BigDecimal money = billMoney;
            BigDecimal payMoney = abClaimVo.getPayMoney();;
            if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
                rate = new BigDecimal(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abClaimVo.getBillType()))
                        ? moneyByDiscount[2] : moneyByDiscount[3]);
                money = money.multiply(new BigDecimal(1).subtract(rate));
                params.put("billMoneyLeft", money);
                params.put("billMoneyRight", billMoney);
            } else {
                if(StringUtils.isNotEmpty(moneyByCashing)
                        && (moneyByDiscount.length > 1)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[0])
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByCashing[1])){
                    rate = new BigDecimal(AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abClaimVo.getBillType()))
                            ? moneyByCashing[2] : moneyByCashing[3]);
                    money = money.multiply(new BigDecimal(1).add(rate));
                    params.put("billMoneyLeft", billMoney);
                    params.put("billMoneyRight", money);
                }
                if(StringUtils.isNotEmpty(moneyByPayCashing)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(moneyByPayCashing[0])){
                    BigDecimal upMoney = BigDecimal.ZERO;
                    BigDecimal downMoney = BigDecimal.ZERO;
                    if(StrUtil.isNotEmpty(moneyByPayCashing[1])){
                        upMoney = new BigDecimal(moneyByPayCashing[1]);
                    }
                    if(StrUtil.isNotEmpty(moneyByPayCashing[2])){
                        downMoney = new BigDecimal(moneyByPayCashing[2]);
                    }
                    params.put("billPayMoneyLeft", payMoney.subtract(downMoney).setScale(2, BigDecimal.ROUND_UP));
                    params.put("billPayMoneyRight", payMoney.add(upMoney).setScale(2, BigDecimal.ROUND_UP));
                }
            }
            params.put("bankAcc", bankAccArr);
            params.put("cdSign", cdSign);
            params.put("accIdList", accIdList.toArray(new String[0]));

            if(StringUtils.isNotBlank(billRmkRuleCondition)){
                params.put("ruleSql", billRmkRuleCondition);
            }

            //日期浮动
            boolean dateFlag;
            int addDays;
            boolean judgeBrief11 = AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId());
            boolean judgeBrief20And21 = AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                    || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId());
            if (judgeBrief11) {
                dateFlag = StringUtils.isNotEmpty(dateByDiscount)
                        && (dateByDiscount.length > 1)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByDiscount[0])
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByDiscount[1]);
            } else if (judgeBrief20And21) {
                dateFlag = StringUtils.isNotEmpty(dateByCollection)
                        && (dateByDiscount.length > 1)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCollection[0])
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCollection[1]);
            } else {
                dateFlag = StringUtils.isNotEmpty(dateByCashing)
                        && (dateByDiscount.length > 1)
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCashing[0])
                        && AccebillConstant.BILL_CONFIG_CHECK1.equals(dateByCashing[1]);
            }
            if(dateFlag){
                Date claimDate = abClaimVo.getClaimDate();
                addDays = Integer.parseInt(judgeBrief11 ? dateByDiscount[2] : judgeBrief20And21 ? dateByCollection[2] : dateByCashing[2]);
                params.put("claimDateLeft", DateUtils.date2Str(claimDate, DateUtils.date_sdf()));
                params.put("claimDateRight", DateUtils.date2Str(DateUtils.addDay(claimDate, addDays), DateUtils.date_sdf()));
                return dateMoneyAccMatch(userVo, sameDateAccConfig, origNoList, abClaimVo, params, accIdList);
            }
        }
        return false;
    }

    /**
     * 日期、金额相同，账号在账号规则范围内匹配
     *
     * @param userVo              用户
     * @param sameDateMoneyConfig 是否自动认领
     * @param origNoList          票据号集合
     * @param abClaimVo           票据记录vo
     * @param cdSign              借贷标志
     * @param bankAccArr          银行账号数组
     * @param accIdList           银行流水id集合
     * @return 是否匹配成功
     */
    private boolean sameDateMoneyMatch(ICurrentUser userVo, boolean sameDateMoneyConfig, ArrayList<String> origNoList,
                                       AbClaimVo abClaimVo, String cdSign, String[] bankAccArr, List<String> accIdList) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("bankAcc", bankAccArr);
        params.put("cdSign", cdSign);
        params.put("accIdList", accIdList.toArray(new String[0]));
        params.put("billMoney", abClaimVo.getBillMoney());
        params.put("claimDate", DateUtils.date2Str(abClaimVo.getClaimDate(), DateUtils.date_sdf()));
        return dateMoneyAccMatch(userVo, sameDateMoneyConfig, origNoList, abClaimVo, params, accIdList);
    }
    /**
     * 根据账号规则获取对应的账号集合
     *
     * @param billBackDesc      是否勾选贴现：贴现办理入账账户参数
     * @param isDiscountConfirm 是否勾选托收：票据所属账户参数
     * @param accountByCashing1 是否勾选兑付：银票
     * @param accountByCashing2 是否勾选兑付：商票
     * @param abClaimVo         票据记录vo
     * @return 是否匹配成功
     */
    private String[] getBankAccByRule(boolean billBackDesc, boolean isDiscountConfirm, boolean accountByCashing1,
                                      boolean accountByCashing2, AbClaimVo abClaimVo) {
        boolean accFlag;
        if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
            // 业务类型是贴现
            accFlag = billBackDesc;
        } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId())) {
            // 业务类型是托收
            accFlag = isDiscountConfirm;
        } else {
            // 兑付
            accFlag = AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abClaimVo.getBillType()))
                    ? accountByCashing1 : accountByCashing2;
        }
        String claimTypeName = abClaimVo.getClaimTypeName();
        String businessType = "贴现".equals(claimTypeName) ? "1" : "托收".equals(claimTypeName) ? "2" : "3";
        HashMap<String, Object> accParams = new HashMap<>(8);
        accParams.put("corpId", abClaimVo.getCorpId());
        accParams.put("businessType", businessType);
        List<AbClaimAccountVo> accRuleList = accFlag ? abClaimAccountService.listAbClaimAccountInfo(accParams) : null;
        String[] bankAccArr = null;
        if (!CollectionUtils.isEmpty(accRuleList)) {
            bankAccArr = new String[accRuleList.size() + 1];
            for (int i = 0; i < accRuleList.size(); i++) {
                bankAccArr[i] = accRuleList.get(i).getBankAcc();
            }
            // 票据记录的账号
            bankAccArr[accRuleList.size()] = abClaimVo.getBankAcc();
        }
        return bankAccArr;
    }
    /**
     * 日期+金额+账号匹配
     *
     * @param userVo                 用户
     * @param sameDateMoneyAccConfig 是否自动认领
     * @param origNoList             票据号集合
     * @param abClaimVo              票据记录vo
     * @param cdSign                 借贷标志
     * @param accIdList              银行流水记录id集合
     * @return 是否匹配成功
     */
    private boolean sameDateMoneyAccMatch(ICurrentUser userVo, boolean sameDateMoneyAccConfig, ArrayList<String> origNoList,
                                          AbClaimVo abClaimVo, String cdSign, List<String> accIdList) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("bankAcc", new String[]{abClaimVo.getBankAcc()});
        params.put("cdSign", cdSign);
        params.put("billMoney", abClaimVo.getBillMoney());
        params.put("claimDate", DateUtils.date2Str(abClaimVo.getClaimDate(), DateUtils.date_sdf()));
        params.put("accIdList", accIdList.toArray(new String[0]));
        return dateMoneyAccMatch(userVo, sameDateMoneyAccConfig, origNoList, abClaimVo, params, accIdList);
    }
    /**
     * 票号匹配规则
     *
     * @param userVo           用户
     * @param isEmitMake       是否补录
     * @param claimStartString 起始认领时间
     * @param accDtlHashMap    银行流水map
     * @param ruleBillNoConfig 是否自动认领
     * @param origNoList       票据号集合
     * @param billRuleHashMap  票号规则map
     * @param abClaimVo        票据记录vo
     * @param busType          业务类型
     * @param cdSign           借贷标志
     * @param accIdList        银行流水记录id集合
     * @return 是否匹配成功
     */
    private boolean origNoRuleMatch(ICurrentUser userVo, boolean isEmitMake, String claimStartString, HashMap<String, ArrayList<BisAccDtlVo>> accDtlHashMap,
                                    boolean ruleBillNoConfig, ArrayList<String> origNoList, HashMap<String, AbBillRuleBillNo> billRuleHashMap,
                                    AbClaimVo abClaimVo, String busType, String cdSign, List<String> accIdList) {
        if (!CollectionUtils.isEmpty(billRuleHashMap)) {
            String bankTypeId = abClaimVo.getBankTypeId();
            // 判断银行类型和业务类型判断是否有对应票号规则
            String ruleKey = busType + "-" + bankTypeId;
            if (billRuleHashMap.containsKey(ruleKey)) {
                String origNo = abClaimVo.getOrigNo();
                AbBillRuleBillNo ruleBillNo = billRuleHashMap.get(ruleKey);
                if(!AccebillConstant.IS_SUMMARY_CLAIM_BILL.equals(ruleBillNo.getIsSummaryClaimBill())){
                    // 票据号字段
                    String billNumField = ruleBillNo.getBillNumField();
                    // 字段名 0摘要 1附言 2用途
                    String field = "0".equals(billNumField) ? "remark" : "1".equals(billNumField) ? "postscript" : "uses";
                    // 分隔符(默认是空格)
                    String separated = StringUtils.isEmpty(ruleBillNo.getSeparated()) ? " " : ruleBillNo.getSeparated();
                    // 根据借贷标志和银行类型，查找是否有对应的银行流水信息
                    String key = cdSign + "-" + bankTypeId;
                    if (accDtlHashMap.containsKey(key)) {
                        HashMap<String, Object> params = new HashMap<>(8);
                        params.put("cdSign", cdSign);
                        params.put("bankTypeId", bankTypeId);
                        params.put("field", field);
                        params.put("accIdList", accIdList.toArray(new String[0]));
                        params.put("origNo", "%" + origNo + "%");
                        if(StrUtil.isNotEmpty(abClaimVo.getTransTimeLeft())){
                            params.put("transTimeLeft", abClaimVo.getTransTimeLeft());
                        }
                        // 根据票号规则查询出对应银行流水数据
                        List<BisAccDtlVo> accDtlVos = abClaimDao.selectBankAccList(params);
                        // 使用like查询，可能会导致票据号为100查询出1000的数据，所以需要分割后再次判断获取真正匹配的银行流水数据
                        ArrayList<BisAccDtlVo> bisAccDtlVoList = new ArrayList<>();
                        try {
                            Field declaredField = BisAccDtlVo.class.getDeclaredField(field);
                            declaredField.setAccessible(true);
                            for (BisAccDtlVo accDtlVo : accDtlVos) {
                                // 按分割符进行分割
                                String[] origNos = String.valueOf(declaredField.get(accDtlVo)).split(separated);
                                List<String> origNosList = Arrays.stream(origNos)
                                        .filter(s -> StrUtil.isNotEmpty(s))
                                        .collect(Collectors.toList());
                                if (origNosList.contains(origNo)) {
                                    bisAccDtlVoList.add(accDtlVo);
                                }
                            }

                            if (!CollectionUtils.isEmpty(bisAccDtlVoList)) {
                                // 备注
                                String rmk = "";
                                String[] origNos = String.valueOf(declaredField.get(bisAccDtlVoList.get(0))).split(separated);
                                List<String> origNosList = Arrays.stream(origNos)
                                        .filter(s -> StrUtil.isNotEmpty(s))
                                        .collect(Collectors.toList());
                                List<AbClaimVo> claimVoList = abClaimDao.selectClaimVoList(origNosList.toArray(new String[0]), isEmitMake,
                                        DateUtils.str2Date(claimStartString, DateUtils.date_sdf()), null,true);
                                // 一条流水数据如果对应多条票据信息时，需要将票据号和票据金额存入备注中
                                if (claimVoList.size() > 1) {
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    for (AbClaimVo claimVo : claimVoList) {
                                        if (StringUtils.isEmpty(rmk)) {
                                            rmk = claimVo.getOrigNo() + "|" + df.format(claimVo.getBillMoney());
                                        } else {
                                            rmk += "," + claimVo.getOrigNo() + "|" + df.format(claimVo.getBillMoney());
                                        }
                                    }
                                }
                                String username = userVo.getUsername();
                                AbClaimMiddle abClaimMiddle = new AbClaimMiddle();
                                Date nowDate = new Date();
                                abClaimMiddle.setCreateDate(nowDate);
                                abClaimMiddle.setCreateBy(userVo.getLoginName());
                                abClaimMiddle.setClaimTime(nowDate);
                                abClaimMiddle.setClaimPerson(username);
                                abClaimMiddle.setClaimStatus(ruleBillNoConfig ? AccebillConstant.BILL_CLAIM_STATUS95 : AccebillConstant.BILL_CLAIM_STATUS94);
                                abClaimMiddle.setClaimSequence(SequenceSingleton.getSequence(SequenceEnum.BILL_CLAIM_MIDDLE_SEQUENCE));
                                abClaimMiddle.setOrigNo(origNo);
                                abClaimMiddle.setRmk(rmk);
                                origNoList.addAll(origNosList);
                                String[] vvIdArr = new String[claimVoList.size()];
                                String[] vIdArr = new String[claimVoList.size()];
                                String[] aIdArr = new String[claimVoList.size()];
                                String[] realMonryArr = new String[claimVoList.size()];
                                for (int i = 0; i < claimVoList.size(); i++) {
                                    vIdArr[i] = claimVoList.get(i).getVid();
                                    vvIdArr[i] = claimVoList.get(i).getVvid();
                                    aIdArr[i] = claimVoList.get(i).getAid();
                                    realMonryArr[i] = String.valueOf(claimVoList.get(i).getRealMoney());
                                }
                                abClaimMiddleService.saveEntity(abClaimMiddle);
                                // 将对应的银行账号明细表数据的票据认领中间表id设置为当前新增数据id
                                for (BisAccDtlVo accDtlVo : bisAccDtlVoList) {
                                    accIdList.add(accDtlVo.getId());
                                    BisAccDtlDTO bisAccDtl = BeanUtils.copyNewInstanceProperties(accDtlVo, BisAccDtlDTO.class);
                                    bisAccDtl.setClaimId(abClaimMiddle.getId());
                                    abBtBankAccService.updateDtlByDto(bisAccDtl);
                                }
                                // 将对应的从表数据的票据认领中间表id设置为当前新增数据id
                                abVoucherinfoViceService.updateClaimByIds(vvIdArr, abClaimMiddle.getId(), abClaimMiddle.getClaimStatus());
                                // 收款人或付款人(选择了几条票据数据，就得有几个收款人)
                                String signeroks = "";
                                for (int i = 0; i < vvIdArr.length; i++) {
                                    signeroks = StringUtils.isEmpty(signeroks) ? username : signeroks + "," + username;
                                }
                                // 自动认领执行
                                if (ruleBillNoConfig) {
                                    //贴现
                                    if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
                                        AbBillDiscountAccountVO abBillDiscountAccountVo = new AbBillDiscountAccountVO();
                                        abBillDiscountAccountVo.setRealMoneys(StringUtils.join(realMonryArr, ","));
                                        abBillDiscountAccountVo.setBusinessDate(nowDate);
                                        abBillDiscountAccountVo.setSigneroks(signeroks);
                                        abBillDiscountAccountVo.setAccIds(StringUtils.join(aIdArr, ","));
                                        abBillDiscountAccountService.autoApprovalAbBillDiscountAccount(abBillDiscountAccountVo, userVo);
                                    } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                                            || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId())) {
                                        // 托收
                                        AbBillCollectionAccountVo abBillCollectionAccountVo = new AbBillCollectionAccountVo();
                                        BeanUtils.copyProperties(abClaimVo, abBillCollectionAccountVo);
                                        abBillCollectionAccountVo.setBusinessDate(nowDate);
                                        abBillCollectionAccountVo.setVids(StringUtils.join(vIdArr, ","));
                                        abBillCollectionAccountVo.setVvids(StringUtils.join(vvIdArr, ","));
                                        abBillCollectionAccountVo.setAids(StringUtils.join(aIdArr, ","));
                                        abBillCollectionAccountVo.setSignerok(signeroks);
                                        abBillCollectionAccountService.autoApprovalCollectionApply(abBillCollectionAccountVo, userVo);
                                    }
                                }
                                return true;
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * 根据日期、金额、账号进行匹配
     *
     * @param userVo     用户
     * @param config     自动认领或手动认领
     * @param origNoList 已匹配的票据号
     * @param abClaimVo  票据记录vo
     * @param params     银行流水查询参数
     * @param accIdList  银行流水id集合
     * @return 是否匹配成功
     */
    private boolean dateMoneyAccMatch(ICurrentUser userVo, boolean config, ArrayList<String> origNoList,
                                      AbClaimVo abClaimVo, Map<String, Object> params, List<String> accIdList) {

        if(abClaimVo!=null && StrUtil.isNotEmpty(abClaimVo.getTransTimeLeft())){
            params.put("transTimeLeft", abClaimVo.getTransTimeLeft());
        }
        List<BisAccDtlVo> accDtlVos = abClaimDao.selectBankAccList(params);
        if (!CollectionUtils.isEmpty(accDtlVos)) {
            accIdList.add(accDtlVos.get(0).getId());
            String origNo = abClaimVo.getOrigNo();
            String username = userVo.getUsername();
            AbClaimMiddle abClaimMiddle = new AbClaimMiddle();
            Date nowDate = new Date();
            abClaimMiddle.setCreateDate(nowDate);
            abClaimMiddle.setCreateBy(userVo.getLoginName());
            abClaimMiddle.setClaimTime(nowDate);
            abClaimMiddle.setClaimPerson(username);
            // 如果匹配到多条流水数据，则转为待确认
            abClaimMiddle.setClaimStatus(accDtlVos.size() > 1 || !config ? AccebillConstant.BILL_CLAIM_STATUS94 : AccebillConstant.BILL_CLAIM_STATUS95);
            abClaimMiddle.setClaimSequence(SequenceSingleton.getSequence(SequenceEnum.BILL_CLAIM_MIDDLE_SEQUENCE));
            abClaimMiddle.setOrigNo(origNo);
            origNoList.add(origNo);
            // 保存票据认领中间表数据
            abClaimMiddleService.saveEntity(abClaimMiddle);
            // 将对应的银行账号明细表数据的票据认领中间表id设置为当前新增数据id
            BisAccDtlDTO bisAccDtl = BeanUtils.copyNewInstanceProperties(accDtlVos.get(0), BisAccDtlDTO.class);
            bisAccDtl.setClaimId(abClaimMiddle.getId());
            abBtBankAccService.updateDtlByDto(bisAccDtl);
            // 将对应的从表数据的票据认领中间表id设置为当前新增数据id
            abVoucherinfoViceService.updateClaimByIds(new String[]{abClaimVo.getVvid()}, abClaimMiddle.getId(), abClaimMiddle.getClaimStatus());
            if (config) {
                //贴现
                if (AccebillConstant.BRIEF_STATUS_011.equals(abClaimVo.getBriefId())) {
                    AbBillDiscountAccountVO abBillDiscountAccountVo = new AbBillDiscountAccountVO();
                    abBillDiscountAccountVo.setRealMoneys(String.valueOf(abClaimVo.getRealMoney()));
                    abBillDiscountAccountVo.setBusinessDate(nowDate);
                    abBillDiscountAccountVo.setSigneroks(username);
                    abBillDiscountAccountVo.setAccIds(abClaimVo.getAid());
                    abBillDiscountAccountService.autoApprovalAbBillDiscountAccount(abBillDiscountAccountVo, userVo);
                } else if (AccebillConstant.BRIEF_STATUS_020.equals(abClaimVo.getBriefId())
                        || AccebillConstant.BRIEF_STATUS_021.equals(abClaimVo.getBriefId())) {
                    // 托收
                    AbBillCollectionAccountVo abBillCollectionAccountVo = new AbBillCollectionAccountVo();
                    BeanUtils.copyProperties(abClaimVo, abBillCollectionAccountVo);
                    abBillCollectionAccountVo.setBusinessDate(nowDate);
                    abBillCollectionAccountVo.setVids(abClaimVo.getVid());
                    abBillCollectionAccountVo.setVvids(abClaimVo.getVvid());
                    abBillCollectionAccountVo.setAids(abClaimVo.getAid());
                    abBillCollectionAccountVo.setSignerok(username);
                    abBillCollectionAccountService.autoApprovalCollectionApply(abBillCollectionAccountVo, userVo);
                }
            }
            return true;
        }
        return false;
    }

    /**
    * @Description 获取已认领的业务类型是兑付的票据
    * @author zhous
    * @date 2025/2/6 13:52
    */
    public List<AbEmitBillPayVo> getClaimedBillPayInfo() {
        boolean isEmitMake = abVoucherinfoViceService.isEmitMake();
        String claimStartString = abConfigService.getConfig(SysConfigConstant.Module.ACCEBILL, SysConfigConstant.CLAIM_START_DATE).getValue();
        if (StringUtils.isEmpty(claimStartString)) {
            claimStartString = "1999-01-01";
        }
        return abClaimDao.getClaimedBillPayInfo(isEmitMake,DateUtils.str2Date(claimStartString, DateUtils.date_sdf()));
    }

    /**
     * 获取银行交易明细查询配置信息
     * @param vo
     * @return
     */
    public Map<String,String> getBankAccDtlQueryConfig(AbClaimVo vo) {
        Map<String,String>  map = new HashMap<>();
        //设置兑付解付浮动比例
        if("兑付".equals(vo.getClaimTypeName())){
            ConfigDTO moneyByPayCashing = abConfigService.getBillConfig("moneyByPayCashing");
            if(moneyByPayCashing!=null
                    &&"1".equals(moneyByPayCashing.getValue())){
//                ConfigDTO ratesFloatPer5 = abConfigService.getBillConfig("ratesFloatPer5");
//                if(ratesFloatPer5!=null
//                        &&StrUtil.isNotEmpty(ratesFloatPer5.getValue())){
//                    map.put("ratesFloatPer",ratesFloatPer5.getValue());
//                }
                ConfigDTO upMoney = abConfigService.getBillConfig("upMoney");
                ConfigDTO downMoney = abConfigService.getBillConfig("downMoney");
                if(upMoney!=null){
                    map.put("upMoney",StrUtil.isNotEmpty(upMoney.getValue())?upMoney.getValue():"0");
                }
                if(downMoney!=null){
                    map.put("downMoney",StrUtil.isNotEmpty(downMoney.getValue())?downMoney.getValue():"0");
                }
            }
            vo.setBusType("003");
        } else if ("贴现".equals(vo.getClaimTypeName())) {
            vo.setBusType("002");
        } else if ("托收".equals(vo.getClaimTypeName())) {
            vo.setBusType("001");
        }
        List<AbClaimVo> dtlQueryConfigList = abClaimDao.getDtlQueryConfig(vo);
        if(CollectionUtil.isNotEmpty(dtlQueryConfigList)){
            dtlQueryConfigList.forEach(info->{
                if(!map.containsKey(info.getMatchElements())) {
                    map.put(info.getMatchElements(),info.getMatchContent());
                }
            });
        }
//        //获取银行的摘要、用途、备注默认值
//        map.put("uses","摘要");
//        map.put("postscript","用途");
//        map.put("remark","备注");
        return map;
    }
}
