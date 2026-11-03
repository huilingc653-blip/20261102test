package com.bytter.finance.domain.provide.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.provide.dao.LoanInfoDao;
import com.bytter.finance.domain.provide.dao.ProvideSyndicatedDao;
import com.bytter.finance.domain.provide.entity.CmsLoanInfoViewPageVO;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideSyndicated;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.*;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.*;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author onlineGeneratortempOrSubmit
 * @version 1.0
 * @Title: Service
 * @Description: 借款信息表
 * @date 2017-07-20 10:45:09
 */
@Service("loanInfoService")
@Transactional
@WorkFlow(value = {CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO, CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO_REPAIR})
public class LoanInfoService extends BaseService<CmsProvideLoanInfo, String>
        implements IWorkFlowListener, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private LoanInfoDao loanInfoDao;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtBankService btBankService;
    @Autowired
    private IRatUrationService urationService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookServiceImp;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServieImpl;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService guaPledgeOccupyServiceImpl;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired(required = false)
    private CmsProvideInterestService cmsProvideInterestService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private ProvideSyndicatedDao loanSyndicatedDao;
    @Autowired
    private RepayTypeService repayTypeService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    /**
     * 功能介绍 : 银行借款是否挂授信。<br>
     * <b>日期：</b>2017年9月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelateRat() {
        return configService.getConfig(ConfigConstant.CMS, "relate_rat");
    }

    /**
     * 是否启用授信敞口
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsRatOpen() {
        return configService.getConfig(ConfigConstant.CMS, "isRatOpen");
    }

    /**
     * 功能介绍 : 查询修改列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataUpdateTables(DataTablesInput dataTablesInput, String resource, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, user.getId());
        if(EmptyUtils.isEmpty(corpAuth)){
            return new DataTablesOutput();
        }
        DataTablesOutput dataUpdateTables = loanInfoDao.getDataUpdateTables(dataTablesInput, user.getLoginName(), corpAuth);
        addDictValue(dataUpdateTables.getData());
        return dataUpdateTables;
    }

    /**
     * 功能介绍 : 查询借款利率调整列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author songkk
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLoanRateTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        DataTablesOutput dataLoanRateTables = loanInfoDao.getDataLoanRateTables(dataTablesInput, corpAuth);
        addDictValue(dataLoanRateTables.getData());
        return dataLoanRateTables;
    }

    /**
     * 功能介绍 : 申请审批列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO, dataTablesInput);
        DataTablesOutput dp = loanInfoDao.getDataInfoApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        addDictValue(dp.getData());
        return workFlowBusiness.merge(dp);
    }

    /**
     * 功能介绍 : 查询补录列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepairTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAIR_QUERY, user.getId());
        DataTablesOutput dataRepairTables = loanInfoDao.getDataRepairTables(dataTablesInput, user.getLoginName(), corpAuth);
        addDictValue(dataRepairTables.getData());
        return dataRepairTables;
    }

    /**
     * 注入字典相关值
     */
    private void addDictValue(List<LoanInfoVO> infoVOList) {
        if (CollectionUtil.isEmpty(infoVOList)) {
            return;
        }
        Map<String, DictDTO> contractTerm = dictService.listMap("Provide.ContractTerm");
        Map<String, DictDTO> ternUnitTerm = dictService.listMap("PrecativeLoanInfo.ternUnit");
        infoVOList.forEach(item -> {
            if(EmptyUtils.isNotEmpty(item.getContractTerm())){
                DictDTO dictDTO = contractTerm.get(item.getContractTerm());
                if (Objects.nonNull(dictDTO)) {
                    item.setContractTermName(dictDTO.getName());
                }
            }

            if(EmptyUtils.isNotEmpty(item.getTernUnit())) {
                DictDTO ternUnitDict = ternUnitTerm.get(item.getTernUnit());
                if (Objects.nonNull(ternUnitDict)) {
                    item.setTernUnitName(ternUnitDict.getName());
                }
            }
        });
    }

    /**
     * 功能介绍 : 补录审批列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2017-8-1<br>
     *                        <br>
     * @author lvwl
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepairApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO_REPAIR, dataTablesInput);
        DataTablesOutput dp = loanInfoDao.getDataRepairApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        addDictValue(dp.getData());
        return workFlowBusiness.merge(dp);
    }

    /**
     * 功能介绍 : app报表借款查询<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryLoanForMobile(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        return loanInfoDao.queryLoanForMobile(dataTablesInput, corpAuth);
    }

    /**
     * 功能介绍 : 借款查询<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryLoan(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        DataTablesOutput dataTablesOutput = loanInfoDao.queryLoan(dataTablesInput, corpAuth);
        setDictField(dataTablesOutput.getData());
        return dataTablesOutput;
    }

    private void setDictField(List<LoanQueryVO> loanQueryVOS) {
        Map<String, DictDTO> dictDTOMap = dictService.listMap("cms.bizStatus");
        for (LoanQueryVO loanQueryVO : loanQueryVOS) {
            if (StringUtils.isNotEmpty(loanQueryVO.getSendBizStatus())) {
                DictDTO dictDTO = dictDTOMap.getOrDefault(loanQueryVO.getSendBizStatus(), DictDTO.EMPTY);
                loanQueryVO.setSendBizStatusName(dictDTO.getName());
            }
            if (StringUtils.isNotEmpty(loanQueryVO.getInfoBizStatus())) {
                DictDTO dictDTO = dictDTOMap.getOrDefault(loanQueryVO.getInfoBizStatus(), DictDTO.EMPTY);
                loanQueryVO.setInfoBizStatusName(dictDTO.getName());
            }
        }
    }

    /**
     * 功能介绍：根绝id查询LoanInfoVO对象
     */
    @Transactional(readOnly = true)
    public LoanInfoVO getLoanInfoVOById(String id) {
        return loanInfoDao.getLoanInfoVOById(id);
    }

    /**
     * 功能介绍 : 借款数据转换为授信接口数据。<br>
     * <b>日期：</b>2017年9月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setLoanRatUrationDTO(CmsProvideLoanInfo cmsProvideLoanInfo, RatUrationDTO urationDTO) {
        urationDTO.setCurCode(cmsProvideLoanInfo.getCurId());// 币别ID
        urationDTO.setBillId(cmsProvideLoanInfo.getId());// 业务ID
        urationDTO.setBillNo(cmsProvideLoanInfo.getBillCode());// 业务编号
        urationDTO.setSysCode(ConfigConstant.CMS);// 信贷
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_02);// 信贷占用
        urationDTO.setContactNo(cmsProvideLoanInfo.getContractNo());// 合同号
        urationDTO.setBeginDate(cmsProvideLoanInfo.getPlDate());// 借款开始日期
        urationDTO.setEndDate(cmsProvideLoanInfo.getRepayDate());// 借款还款日期
        urationDTO.setMoney(cmsProvideLoanInfo.getPlMoney());// 借款金额
        urationDTO.setBillMoney(cmsProvideLoanInfo.getPlMoney());
        urationDTO.setBailRate(cmsProvideLoanInfo.getBailmoneyRate());// 保证金比例
        urationDTO.setBondMoney(
                cmsProvideLoanInfo.getBailmoney() == null ? BigDecimal.ZERO : cmsProvideLoanInfo.getBailmoney());// 保证金金额
        urationDTO.setBondCurId(cmsProvideLoanInfo.getBondCurId());// 保证金币别
        if (StringUtils.isEmpty(cmsProvideLoanInfo.getIsOpenRat())) {
            urationDTO.setIsOpenRat("0");// 是否启用授信敞口
        } else {
            urationDTO.setIsOpenRat(cmsProvideLoanInfo.getIsOpenRat());// 是否启用授信敞口
        }
    }

    /**
     * 功能介绍 : 提交或暂存<br>
     *
     * @param vo
     * @param saveType 1提交，0暂存 <b>日期：</b>2017-7-31<br>
     *                 <br>
     * @author lvwl
     */
    public void tempOrSubmit(LoanInfoVO vo, String saveType, String myflag, RatUrationDTO urationDTO, ICurrentUser user) {
        CmsProvideLoanInfo cmsProvideLoanInfo = new CmsProvideLoanInfo();
        if (StringUtils.isEmpty(vo.getIsRmbRat())) {
            vo.setIsRmbRat(RationConstant.RAT_COMMON_N);// 不折合人民币
        }
        // vo.setPlMoney(new BigDecimal(vo.getPlMoney().doubleValue()));
        BeanUtils.copyNotNullProperties(vo, cmsProvideLoanInfo);
        // modify zouxs 2022-12-29 前端提交传 还款方式时只传了一个id，对应的描述没有传
        if (StringUtils.isNoneBlank(cmsProvideLoanInfo.getRepayType())
                && StringUtils.isAllBlank(cmsProvideLoanInfo.getRepayTypeDesc())) {
            // 调用翻译
            RepayTypeVO repayTypeVO = repayTypeService.getRepayTypeById(cmsProvideLoanInfo.getRepayType());
            if (repayTypeVO != null) {
                cmsProvideLoanInfo.setRepayTypeDesc(repayTypeVO.getRepayName());
            }
        }
        // end 2022-12-29
        cmsProvideLoanInfo.setPlType(vo.getGuaWay());
        validata(cmsProvideLoanInfo);
        CorpDTO corp = corpServiceImpl.get(cmsProvideLoanInfo.getCorpId());
        if (StringUtils.isEmpty(corp.getNetId())) {// 顶级网点 没有netid
            cmsProvideLoanInfo.setNetId(corp.getId());
            cmsProvideLoanInfo.setCorpName(corp.getName());
            cmsProvideLoanInfo.setCorpCode(corp.getCode());
        } else {
            cmsProvideLoanInfo.setNetId(corp.getNetId());
            cmsProvideLoanInfo.setCorpName(corp.getName());
            cmsProvideLoanInfo.setCorpCode(corp.getCode());
        }
        if (!"01".equals(cmsProvideLoanInfo.getFloatWay())) {// 非固定利率
            LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(
                    DateUtils.formatDate(cmsProvideLoanInfo.getPlDate(), "yyyy-MM-dd"),
                    cmsProvideLoanInfo.getReferenceRateType(), cmsProvideLoanInfo.getCurId(),
                    cmsProvideLoanInfo.getRepayCycle());
            cmsProvideLoanInfo.setBenchmarkRate(rate.getRate());// 设置基础利率
        }
        btBankService.saveBtBank(cmsProvideLoanInfo.getBankId(), cmsProvideLoanInfo.getBankType());// 保存银行信息
        // 用途
        cmsProvideLoanInfo.setPlPurpose(vo.getPlPurpose());
        cmsProvideLoanInfo.setBondBankAcc(vo.getBondBankAcc());
        // 担保挂接处理状态
        int guaStatus = 0;
        if ("1".equals(saveType)) {// 提交
            cmsProvideLoanInfo.setStatus(CmsConstant.COMMITAPPROVE);
            cmsProvideLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_15);
            guaStatus = 1;
        } else {// 暂存
            cmsProvideLoanInfo.setStatus(CmsConstant.TEMP);
            cmsProvideLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_10);
        }
        if (StringUtils.isNotEmpty(cmsProvideLoanInfo.getBankId())) {
            // 保存银行信息到btBank表
            btBankService.saveBtBank(cmsProvideLoanInfo.getBankId(), cmsProvideLoanInfo.getBankType());
        }
        CmsProvideLoanInfo loan = null;
        if (StringUtils.isEmpty(cmsProvideLoanInfo.getId())) {
            // cmsProvideLoanInfo.setBillCode(getLoanInfoBillCode());
            loan = cmsProvideLoanInfo;
            this.save(loan, user);
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            ProvideUtils.handleGua(loan, null, guaContractOccupServieImpl, guaStatus, vo.getGuaContract());
        } else {
            loan = this.get(cmsProvideLoanInfo.getId());
            BeanUtils.copyNotNullProperties(cmsProvideLoanInfo, loan);
            this.saveOrUpdate(loan);// 使用saveOrUpdate无法新增数据
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            ProvideUtils.handleGua(cmsProvideLoanInfo, loan, guaContractOccupServieImpl, guaStatus, vo.getGuaContract());
        }
        // 授信挂接
        if (!StringUtils.isEmpty(cmsProvideLoanInfo.getRatmodel())
                && CmsConstant.IS_TRUE.equals(cmsProvideLoanInfo.getRatmodel())) {
            setLoanRatUrationDTO(cmsProvideLoanInfo, urationDTO);
            loan.setIsHangCredit(CommonConstant.YES);
            if ("1".equals(saveType)) {// 提交,授信占用冻结
                urationService.addUseRation(urationDTO);
            } else {// 暂存，授信占用暂存，不占用额度
                urationService.tempUseRation(urationDTO);
            }
        } else {
            if (!StringUtils.isEmpty(cmsProvideLoanInfo.getId())) {// 如果之前挂了授信
                urationService.deleteUseRation(urationDTO);
                loan.setIsHangCredit(CommonConstant.NO);
            }
        }
        // 判断是否挂接了保证金
        if ((vo.getIsHookBond() != null) && (vo.getIsHookBond().equals(CommonConstant.TRUE))) {
            // 判断是新增还是修改,查询结果是包含了已删除的
            FcBondApplyDTO fcBondApplyDTO = fcBondHookServiceImp.getFcBondApply(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode(), vo.getBondId());
            FcBondApplyDTO fcBondApplyDTOPre = fcBondHookServiceImp.getFcBondApply(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode(), vo.getBondIdPre());
            BigDecimal availMoney = null;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookServiceImp.getFcBondMsgById(vo.getBondId());
            BigDecimal applyMoney = cmsProvideLoanInfo.getBailmoney();
            if (fcBondApplyDTOPre == null) {// 之前未挂接过保证金
                availMoney = fcBondDTO.getAvailMoney();
            } else {
                availMoney = fcBondDTO.getAvailMoney().add(fcBondApplyDTOPre.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                throw new ParameterException(LoanMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空即为修改
                this.updateFcBondApply(vo, cmsProvideLoanInfo, saveType, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookServiceImp.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(vo, cmsProvideLoanInfo, saveType);// 新增占用信息
            }
        }
        // 保存第三方费率信息
        if (vo.getCostTypeDtl().size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("id", vo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : vo.getCostTypeDtl()) {
                if (!cost.getCostTypeId().trim().equals("")) {
                    cost.setDirectFinanceId(loan.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // 当借款种类为“银团贷款”时，保存银团明细信息
        if (BaseDataConstant.SYNDICATED.equals(vo.getPlClass())) {
            if (vo.getSyndicatedList().size() > 0) {
                Map<String, Object> param = new HashMap<>();
                param.put("id", vo.getId());
                loanSyndicatedDao.deleteLoanSyndicated(param);
                int isLeadingBank = 0;
                List<ProvideSyndicated> syndicatedList = new ArrayList<>();
                for (ProvideSyndicated syndicated : vo.getSyndicatedList()) {
                    // 通过银行开户行号筛选保存的数据
                    if (!StringUtils.isEmpty(syndicated.getBankCode())) {
                        // 判断牵头行是否只有一个
                        if (CmsConstant.IS_TRUE.equals(syndicated.getLeadingBank())) {
                            isLeadingBank = isLeadingBank + 1;
                        }
                        if (isLeadingBank > 1) {
                            throw new ParameterException(ProvideMessageEnum.CMS_CMSLOANINFO_LEADINGBANK_UNIQUE);
                        }
                        syndicated.setDirectFinanceId(loan.getId());
                        syndicated.setStatus(CommonConstant.STATUE_CHECK);
                        syndicatedList.add(syndicated);
                    }
                }
                loanSyndicatedDao.batchSave(syndicatedList);
            }
        }
        //todo:注释保证金代码
        if ((vo.getIsHookBond() == null) || (vo.getIsHookBond().equals(CommonConstant.FALSE))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookServiceImp.getFcBondApplyByItemIds(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(cmsProvideLoanInfo.getUpdateBy());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookServiceImp.updateFcBondApply(fcBondApplyDTO);
            }
        }
        if (vo.getIsRelatePledge() != null && vo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
            List<GuaPledgeOccupyDTO> newOccupyDTOS = this.setPledgeOccupyInfo(vo, loan.getId());
            guaPledgeOccupyServiceImpl.freezePledgeOccupy(newOccupyDTOS);// 设置抵质押物占用数据
        } else {// 将原占用信息释放
            guaPledgeOccupyServiceImpl.releasePledgeOccupy(cmsProvideLoanInfo.getId(), vo.getBillCode());
        }
        if (!EmptyUtils.isEmpty(loan.getIsOverRepair()) && CommonConstant.YES.equals(loan.getIsOverRepair())) {// 跳过补录
            // 一次性发放 并且 跳过发放
            if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loan.getLoanSendOutType())
                    && CommonConstant.YES.equals(loan.getAcrossSend())) {
                if (EmptyUtils.isEmpty(vo.getPlanId())) {// 还款计划
                    RepayTypeVO typeVO = repayTypeService.getRepayTypeById(loan.getRepayType());
                    // 利息本金均为随借随还
                    if (!BaseDataConstant.FREQUENCY_TYPE_N.equals(typeVO.getInterestFrequencyType())
                            && !BaseDataConstant.FREQUENCY_TYPE_N.equals(typeVO.getCapitalFrequencyType())) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY);
                    } else {
                        repaymentScheduleDao.deleteRepaymentByBillCode(cmsProvideLoanInfo.getId());
                    }
                } else if ("1".equals(vo.getPlanStatus())) {
                    throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
                } else {
                    repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                            cmsProvideLoanInfo.getId());// 更新还款计划的编号和类型
                }
            } else {
                if (!EmptyUtils.isEmpty(vo.getPlanId())) {
                    if ("1".equals(vo.getPlanStatus())) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
                    } else {
                        repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                                loan.getId());// 更新还款计划的编号和类型
                    }
                }
            }
            if (EmptyUtils.isNotEmpty(loan.getAcrossSend())) {
                if (loan.getAcrossSend().equals("1")) {// 跳过发放
                    // 资金预算
                    if (this.isBudget(loan.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                            BaseDataConstant.PLOAN_SEND_HANG_BUDGET)) {
                        // 占用资金计划
                        this.batchEngross(loan, vo, user, saveType);
                    }
                } else if (fbsBudgetProcessService.getBudgetProcessByBillNo(
                        BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION, BudgetConstant.PROCESS_SYSNAME_CMS,
                        loan.getBillCode()) != null) {
                    this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                            BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode());
                }
            }
        } else {
            // 还款计划
            if (!StringUtils.isEmpty(vo.getPlanId())) {
                if ("1".equals(vo.getPlanStatus())) {
                    throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
                } else {
                    repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                            loan.getId());// 更新还款计划的编号和类型
                }
            }
        }
        if (CommonConstant.YES.equals(myflag)) {
            // todo:提交 启动审批流程
            if (CommonConstant.YES.equals(saveType)) {
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "", user);
                getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO, loan.getId(), loan,
                        loan.getCorpId(), user.getId());
            } else {
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_MODIFY + "", user);
            }
        } else {
            if (CommonConstant.YES.equals(saveType)) {
                // todo:提交 启动审批流程
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_INPUT_SUBMIT, user);
                getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO, loan.getId(), loan,
                        loan.getCorpId(), user.getId());
            } else {
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_INPUT + "", user);
            }
        }
        notificationService.complete(NotificationConstant.CMS.PROVIDE_ADD, loan.getId(), user.getId());
    }

    /**
     * 功能介绍 : 补录提交或暂存<br>
     *
     * @param vo
     * @param saveType 1提交，0暂存 <b>日期：</b>2017-8-2<br>
     *                 <br>
     * @author lvwl
     */
    public void repairTempOrSubmit(LoanInfoVO vo, String saveType, RatUrationDTO urationDTO, ICurrentUser user) {
        CmsProvideLoanInfo cmsProvideLoanInfo = new CmsProvideLoanInfo();
        BeanUtils.copyNotNullProperties(vo, cmsProvideLoanInfo);
        cmsProvideLoanInfo.setPlType(vo.getGuaWay());
        repairValidata(cmsProvideLoanInfo);
        if ("1".equals(saveType)) {// 提交
            cmsProvideLoanInfo.setStatus(CmsConstant.REPAIRAPPRAVE);
            cmsProvideLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_25);
            cmsProvideLoanInfo.setRepairOp(user.getId());
        } else {// 暂存
            cmsProvideLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_20);
        }
        if (!"01".equals(cmsProvideLoanInfo.getFloatWay())) {// 非固定利率
            LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(
                    DateUtils.formatDate(cmsProvideLoanInfo.getPlDate(), "yyyy-MM-dd"),
                    cmsProvideLoanInfo.getReferenceRateType(), cmsProvideLoanInfo.getCurId(),
                    cmsProvideLoanInfo.getRepayCycle());
            cmsProvideLoanInfo.setBenchmarkRate(rate.getRate());// 设置基础利率
        }
        String ratModel = CmsConstant.IS_FALSE;// 原借款信息挂接授信方式，默认不挂
        CmsProvideLoanInfo loan = this.get(cmsProvideLoanInfo.getId());
        if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
            ratModel = CmsConstant.IS_TRUE;// 挂接
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        ProvideUtils.handleGua(cmsProvideLoanInfo, loan, guaContractOccupServieImpl, 1, vo.getGuaContract());
        BeanUtils.copyNotNullProperties(cmsProvideLoanInfo, loan);
        this.saveOrUpdate(loan);
        // 一次性发放 并且 跳过发放
        if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loan.getLoanSendOutType())
                && CommonConstant.TRUE.equals(loan.getAcrossSend())) {
            if (EmptyUtils.isEmpty(vo.getPlanId())) {// 还款计划
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY);
            } else if ("1".equals(vo.getPlanStatus())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
            } else {
                repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        cmsProvideLoanInfo.getId());// 更新还款计划的编号和类型
            }
        } else {
            if (EmptyUtils.isEmpty(vo.getPlanId())) {
            } else if ("1".equals(vo.getPlanStatus())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
            } else {
                repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        loan.getId());// 更新还款计划的编号和类型
            }
        }
        // 授信合同释放和重新占用处理
        if (CmsConstant.IS_TRUE.equals(ratModel)) {// 原先挂接授信合同
            RatUrationDTO oriUrationDTO = new RatUrationDTO(ConfigConstant.CMS, loan.getId());
            urationService.backUseRation(oriUrationDTO);// 释放原授信在审占用
        }
        // 新占用录入的授信挂接授信在审占用
        if (!EmptyUtils.isEmpty(cmsProvideLoanInfo.getRatmodel())
                && CmsConstant.IS_TRUE.equals(cmsProvideLoanInfo.getRatmodel())) {
            loan.setIsHangCredit(CommonConstant.YES);
            setLoanRatUrationDTO(cmsProvideLoanInfo, urationDTO);
            urationService.addUseRation(urationDTO);// 在审占用
        }
        if (EmptyUtils.isNotEmpty(loan.getAcrossSend())) {
            if (loan.getAcrossSend().equals("1")) {// 跳过发放
                // 资金预算
                if (this.isBudget(loan.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                        BaseDataConstant.PLOAN_SEND_HANG_BUDGET)) {
                    // 占用资金计划
                    this.batchEngross(loan, vo, user, saveType);
                }
            } else if (fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode()) != null) {
                this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode());
            }
        }
        if ((vo.getIsHookBond() != null) && (vo.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {
            // 判断是新增还是修改,查询结果是包含了已删除的
            FcBondApplyDTO fcBondApplyDTO = fcBondHookServiceImp.getFcBondApply(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode(), vo.getBondId());
            FcBondApplyDTO fcBondApplyDTOPre = fcBondHookServiceImp.getFcBondApply(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode(), vo.getBondIdPre());
            BigDecimal availMoney = null;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookServiceImp.getFcBondMsgById(vo.getBondId());
            BigDecimal applyMoney = cmsProvideLoanInfo.getBailmoney();
            if (fcBondApplyDTOPre == null) {// 之前未挂接过保证金
                availMoney = fcBondDTO.getAvailMoney();
            } else {
                availMoney = fcBondDTO.getAvailMoney().add(fcBondApplyDTOPre.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                throw new ParameterException(LoanMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空即为修改
                this.updateFcBondApply(vo, cmsProvideLoanInfo, saveType, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookServiceImp.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(vo, cmsProvideLoanInfo, saveType);// 新增占用信息
            }
        }
        //todo:保证金
        if ((vo.getIsHookBond() == null) || (vo.getIsHookBond().equals(FcBondConstant.IS_HOOK_N))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookServiceImp.getFcBondApplyByItemIds(cmsProvideLoanInfo.getId(),
                    cmsProvideLoanInfo.getBillCode());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(cmsProvideLoanInfo.getUpdateBy());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookServiceImp.updateFcBondApply(fcBondApplyDTO);
            }
        }
        // 保存第三方费率信息
        if (vo.getCostTypeDtl().size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("id", vo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : vo.getCostTypeDtl()) {
                if (!cost.getCostTypeId().trim().equals("")) {
                    cost.setDirectFinanceId(loan.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // 当借款种类为“银团贷款”时，保存银团明细信息
        if (BaseDataConstant.SYNDICATED.equals(loan.getPlClass())) {
            if (vo.getSyndicatedList().size() > 0) {
                Map<String, Object> param = new HashMap<>();
                param.put("id", vo.getId());
                loanSyndicatedDao.deleteLoanSyndicated(param);
                List<ProvideSyndicated> syndicatedList = new ArrayList<>();
                for (ProvideSyndicated syndicated : vo.getSyndicatedList()) {
                    // 通过银行开户行号筛选保存的数据
                    if (!StringUtils.isEmpty(syndicated.getBankCode())) {
                        syndicated.setDirectFinanceId(loan.getId());
                        syndicatedList.add(syndicated);
                    }
                }
                loanSyndicatedDao.batchSave(syndicatedList);
            }
        }
        // 合同处理
        if (null != vo.getRelateCon() && vo.getRelateCon().equals(CmsConstant.RELATED_CONTRACT_YES)) {// 对接合同管理
            if (null != vo.getRelateConType() && vo.getRelateConType().equals(CmsConstant.BUSINESS_SELECT_CON)) {// 业务调用合同
                conCommonService.hookConOp(vo.getConId(), vo.getId(), user);
            }
        }
        if (vo.getIsRelatePledge() != null && vo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
            List<GuaPledgeOccupyDTO> newOccupyDTOS = this.setPledgeOccupyInfo(vo, loan.getId());
            guaPledgeOccupyServiceImpl.freezePledgeOccupy(newOccupyDTOS);
        } else {// 将原占用信息释放
            guaPledgeOccupyServiceImpl.releasePledgeOccupy(vo.getId(), vo.getBillCode());
        }
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAIR, loan.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
            msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAIR_SUBMIT, user);
            getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO_REPAIR, loan.getId(), loan,
                    loan.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAIR_TEMP, user);
        }
    }

    /**
     * 占用预算
     *
     * @param entity
     */
    public void batchEngross(CmsProvideLoanInfo entity, LoanInfoVO vo, ICurrentUser user, String saveType) {
        if (!EmptyUtils.isEmptyArray(vo.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < vo.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if ("1".equals(saveType)) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setBillId(entity.getId());
                fbsBudgetProcessDTO.setItemCode(vo.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(vo.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(vo.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(entity.getPlDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setProcessCurMoney(vo.getProcessCurMoney()[i]);
                fbsBudgetProcessDTO.setExchangeRate(vo.getExchangeRate()[i]);
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }

    /**
     * 是否挂机预算系统
     *
     * @param corpId   单位ID
     * @param briefId1 业务是否挂预算
     * @param briefId2 业务挂预算科目
     * @return
     */
    public boolean isBudget(String corpId, String briefId1, String briefId2) {
        return StringUtils.isNotEmpty(corpId) && fbsBudgetProcessService.corpCtlBudget(corpId)
                && isBudget(briefId1, briefId2);
    }

    /**
     * 是否挂机预算系统
     *
     * @param briefId1 业务是否挂预算
     * @param briefId2 业务挂预算科目
     * @return
     */
    public boolean isBudget(String briefId1, String briefId2) {
        if (configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId1).toBoolean()) {
            String budgetItem = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId2).toString();
            return StringUtils.isNotEmpty(budgetItem);
        }
        return false;
    }

    /**
     * 功能介绍 : 补录打回<br>
     */
    public void repairBack(String id, String refuseWhy, ICurrentUser user) {
        CmsProvideLoanInfo loan = this.get(id);
        loan.setStatus(CmsConstant.BACK);
        loan.setBizStatus(CmsConstant.BUSINESSSTATE_12);
        loan.setRefuseWhy(refuseWhy);
        this.saveOrUpdate(loan);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAIR, id, user.getId());
        notificationService.send(loan.getId(), loan.getCorpId(), NotificationConstant.CMS.PROVIDE_ADD, loan);// 添加录入申请的消息提示
        msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_REPAIR_BACK, user);
        // 授信挂接
        if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
            // 补录打回，释放授信在审占用
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, id);
            urationService.backUseRation(urationDTO);
        }
        // 担保处理 1在审使用，2使用中，3释放,4打回,5发放打回
        ProvideUtils.handleGua(loan, null, guaContractOccupServieImpl, 4, null);
        // 释放资金计划
        if (this.isBudget(loan.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                BaseDataConstant.PLOAN_SEND_HANG_BUDGET)) {
            fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                    BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode());
        }
    }

    /**
     * 功能介绍 : 删除借款申请<br>
     */
    public void delLoanInfo(String id, ICurrentUser user) {
        // TODO 删除其他附带信息
        CmsProvideLoanInfo info = this.get(id);
        repaymentScheduleDao.deleteRepaymentByBillCode(info.getId());// 删除还款计划 -- 物理删除
        // 资金计划
        this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                BudgetConstant.PROCESS_SYSNAME_CMS, id);
        // 授信挂接
        if (!EmptyUtils.isEmpty(info.getRatmodel()) && CmsConstant.IS_TRUE.equals(info.getRatmodel())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, id);
            urationService.deleteUseRation(urationDTO);
        }
        info.setStatus(CmsConstant.DELETED);
        info.setBizStatus(CmsConstant.BUSINESSSTATE_2_);
        this.update(info, user);
        // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
        FcBondApplyDTO dto = fcBondHookServiceImp.getFcBondApplyByItemIds(info.getId(), info.getBillCode());
        if (dto != null) {// 不为空说明挂接了保证金
            dto.setStatus(FcBondConstant.STATUE_RELEASE);
            dto.setUpdateBy(user.getLoginName());
            dto.setUpdateDate(DateUtils.getDate());
            fcBondHookServiceImp.updateFcBondApply(dto);
        }
        notificationService.complete(NotificationConstant.CMS.PROVIDE_ADD, id, user.getId());
        // 抵质押物处理,释放冻结额度
        if (EmptyUtils.isNotEmpty(info.getIsRelatePledge()) && CmsConstant.IS_TRUE.equals(info.getIsRelatePledge())) {
            guaPledgeOccupyServiceImpl.releasePledgeOccupy(info.getId(), info.getBillCode());
        }
        // this.deleteEntity(info);
    }

    /**
     * 功能介绍 : 根据billCode查询LoanInfo对象<br>
     */
    @Transactional(readOnly = true)
    public CmsProvideLoanInfo getLoanInfoByBillCode(String billCode) {
        return loanInfoDao.getLoanInfoByBillCode(billCode);
    }

    /**
     * 功能介绍 : 根据BillCOde查询LoanInfoVO对象<br>
     */
    @Transactional(readOnly = true)
    public LoanInfoVO getLoanInfoVOBySendBillCode(String billCode) {
        LoanInfoVO loanInfoVO = loanInfoDao.getLoanInfoVOBySendBillCode(billCode);
        //TODO 补充利率信息
        Optional.ofNullable(loanInfoVO).ifPresent(loanInfoDO -> {
            if (StringUtils.isNotEmpty(loanInfoDO.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(loanInfoDO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> {
                    loanInfoVO.setReferenceRateTypeName(rate.getBaseTypeName());
                });
            }
        });

        loanInfoVO.setIsAutoPayName(dictService.get("sf_yn", loanInfoVO.getIsAutoPay()));
        loanInfoVO.setFloatWayName(dictService.get("Cms.FloatWay", loanInfoVO.getFloatWay()));
        loanInfoVO.setContractTermName(dictService.get("Provide.ContractTerm", loanInfoVO.getContractTerm()));
        return loanInfoVO;
    }

    @Transactional(readOnly = true)
    public LoanInfoVO getLoanInfoVOByBillCode(String billCode) {
        LoanInfoVO loanInfoVO = loanInfoDao.getLoanInfoVOByBillCode(billCode);
        //TODO 补充利率信息
        Optional.ofNullable(loanInfoVO).ifPresent(loanInfoDO -> {
            if (StringUtils.isNotEmpty(loanInfoDO.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(loanInfoDO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> {
                    loanInfoVO.setReferenceRateTypeName(rate.getBaseTypeName());
                    loanInfoVO.setIsBankBaseRate(rate.getIsBankBaseRate());
                });
            }
        });
        loanInfoVO.setFloatWayName(dictService.get("Cms.FloatWay", loanInfoVO.getFloatWay()));
        loanInfoVO.setIsAutoPayName(dictService.get("nisBillhead.isPayed", loanInfoVO.getIsAutoPay()));
        loanInfoVO.setTernUnitName(dictService.get("PrecativeLoanInfo.ternUnit", loanInfoVO.getTernUnit()));
        // 担保方式
        loanInfoVO.setPlTypeName(guaWayServie.getGuaWayNameByCodes(loanInfoVO.getPlType()));
        // 点数或浮动比例
        if (StringUtils.isNotEmpty(loanInfoVO.getPointsProportionSelect())) {
            loanInfoVO.setPointsProportionSelectName(dictService.get("Cms.PointsProportion", loanInfoVO.getPointsProportionSelect()));
        }
        return loanInfoVO;
    }

    /**
     * 功能介绍 : 判断借款合同号是否重复<br>
     *
     * @return 重复：true 否则false
     */
    @Transactional(readOnly = true)
    public boolean checkContractNoRepeat(CmsProvideLoanInfo loan) {
        return loanInfoDao.checkContractNoRepeat(loan);
    }

    /**
     * 功能介绍: 通过单位及类型查出借款用途集合。<br>
     * <b>日期：</b>2017年10月20日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @Transactional(readOnly = true)
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        return btPurposeService.listByCorpIdOrBusType(corpId, BtPurposeConstant.SYS_BUS_TYPE_CMS);
    }

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsProvideLoanInfo entity) {
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsProvideLoanInfo entity, ICurrentUser user) {
        entity.setCreateName(user.getUsername());
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getLoginName());
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsProvideLoanInfo entity, ICurrentUser user) {
        entity.setUpdateName(user.getUsername());
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getLoginName());
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsProvideLoanInfo entity, ICurrentUser user) {
        if (EmptyUtils.isEmpty(entity.getId())) {
            entity.setCreateName(user.getUsername());
            entity.setCreateDate(new Date());
            entity.setCreateBy(user.getLoginName());
        } else {
            entity.setUpdateName(user.getUsername());
            entity.setUpdateDate(new Date());
            entity.setUpdateBy(user.getLoginName());
        }
        super.saveOrUpdate(entity);
    }

    public BaseDao<CmsProvideLoanInfo, String> getEntityDao() {
        return loanInfoDao;
    }

    /**
     * <b>功能介绍：</b>生成贷款编号<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getLoanInfoBillCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PROVIDE_LOANINFO_BILL_NO,
                ProvideConstant.INFO_BILL_CODE_PREFIX);
    }
    /*********** 数据验证 ************/
    /**
     * 功能介绍 : 借款录入验证<br>
     */
    private void validata(CmsProvideLoanInfo loan) {
        if (EmptyUtils.isEmpty(loan.getCorpId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CORPEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlClass())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLCLASSEMPTY);
        }
        /*
         * if(EmptyUtils.isEmpty(loan.getBankType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKTYPEEMPTY); }
         */
        if (EmptyUtils.isEmpty(loan.getBankId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKIDEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlMoney())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLMONEYEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getCurId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CURIDEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlTerm())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTERMEMPTY);
        }
        if (loan.getPlDate() != null && loan.getRepayDate() != null) {
            if (loan.getPlDate().getTime() > loan.getRepayDate().getTime()) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLDATELEREPAYDATE);
            }
        }
        if (EmptyUtils.isEmpty(loan.getRepayType())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_REPAYTYPEEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getContractTerm())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CONTRACTTERMEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getFloatWay())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_FLOATWAYEMPTY);
        }
        if (!CmsConstant.FLOAT_WAY_01.equals(loan.getFloatWay()) && loan.getRateEntriesEffectDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_EFFECTDATEEMPTY);
        }
        /**
         * 利率验证
         */
        // if(loan.getPlRate() == null || loan.getPlRate().compareTo(BigDecimal.ZERO) == 0){
        // throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PlRATEEMPTY);
        // }
        if (EmptyUtils.isEmpty(loan.getPlPurpose())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLPURPOSEEMPTY);
        }
        if (loan.getPlPurpose().length() > 40) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PURPOSEEMPTY_LENGTH);
        }
        /*
         * if(EmptyUtils.isEmpty(loan.getMainPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_MAINPLTYPEEMPTY); }
         * if(EmptyUtils.isEmpty(loan.getPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTYPEEMPTY); }
         */
    }

    /**
     * 功能介绍 : 补录保存验证<br>
     */
    private void repairValidata(CmsProvideLoanInfo loan) {
        /*
         * if(EmptyUtils.isEmpty(loan.getBankType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKTYPEEMPTY); }
         */
        if (EmptyUtils.isEmpty(loan.getBankId())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_BANKIDEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlMoney())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLMONEYEMPTY);
        }
        if (EmptyUtils.isEmpty(loan.getPlTerm())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTERMEMPTY);
        }
        if (loan.getPlDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLDATEEMPTY);
        }
        if (loan.getRepayDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_REPAYDATEEMPTY);
        }
        if (loan.getPlDate() != null && loan.getRepayDate() != null) {
            if (loan.getPlDate().getTime() > loan.getRepayDate().getTime()) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLDATELEREPAYDATE);
            }
        }
        if (EmptyUtils.isEmpty(loan.getContractNo())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CONTRACTNOEMPTY);
        }
        if (checkContractNoRepeat(loan)) {// 验证合同是否已存在
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_CONTRACTNOREPEAT);
        }
        if (EmptyUtils.isEmpty(loan.getFloatWay())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_FLOATWAYEMPTY);
        }
        if (!CmsConstant.FLOAT_WAY_01.equals(loan.getFloatWay()) && loan.getRateEntriesEffectDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_EFFECTDATEEMPTY);
        }
        /**
         * 利率验证
         */
        // if(loan.getPlRate() == null || loan.getPlRate().compareTo(BigDecimal.ZERO) == 0){
        // throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PlRATEEMPTY);
        // }
        /*
         * if(EmptyUtils.isEmpty(loan.getPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTYPEEMPTY); }
         * if(EmptyUtils.isEmpty(loan.getPlType())){ throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PLTYPEEMPTY); }
         */
        if (EmptyUtils.isEmpty(loan.getOverdueRate())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_OVERDUERATEEMPTY);
        }
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    /**
     * 审批流程结束 执行
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsProvideLoanInfo loan = this.get(businessKey, true);
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO.equals(processDefinitionKey)) {// 申请审批
            if (result) {// 审批通过
                loan.setStatus(CmsConstant.APPROVED);
                loan.setBizStatus(CmsConstant.BUSINESSSTATE_21);
                if (CommonConstant.YES.equals(loan.getIsOverRepair())) {// 跳过补录
                    loan.setStatus(CmsConstant.PREPARESEND);
                    loan.setBizStatus(CmsConstant.BUSINESSSTATE_31);
                    msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK + "", user);
                    /*** 合同处理开始 **/
                    String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                            .getValue();// 是否对接合同管理
                    String relateConType = configService
                            .getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE).getValue();// 对接合同管理管理模式
                    // 合同处理
                    if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {// 对接合同管理
                        if (null != relateConType && relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)) {// 业务生成合同
                            UserDTO userDTO = userService.getByLoginName(loan.getCreateBy());
                            setInfoDTO(loan, userDTO);// 设置合同信息并生成合同
                        }
                    }
                    /*** 合同处理结束 **/
                    cmsProvideLoanSendService.createSendByInfo(loan, user);// 跳过发放
                    // 授信挂接，补录审批通过修改为正式占用
                    if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
                        RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, businessKey);
                        urationService.passUseRation(urationDTO);
                    }
                    // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                    ProvideUtils.handleGua(loan, null, guaContractOccupServieImpl, 2, null);
                    this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                            BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode(), BudgetConstant.STATUE_CHECK, user);
                    /*** 抵质押物处理开始(补录审批通过后正式占用额度) **/
                    if (loan.getIsRelatePledge() != null && loan.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
                        List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(loan.getId(),
                                loan.getBillCode());
                        guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
                    }
                } else {
                    msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                            CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
                    notificationService.send(loan.getId(), loan.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAIR,
                            loan);// 添加补录的消息提示
                }
            } else {// 打回
                loan.setStatus(CmsConstant.BACK);
                loan.setBizStatus(CmsConstant.BUSINESSSTATE_17);
                // 授信挂接
                if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
                    RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, businessKey);
                    urationService.backUseRation(urationDTO);
                }
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                ProvideUtils.handleGua(loan, null, guaContractOccupServieImpl, 4, null);
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
                notificationService.send(loan.getId(), loan.getCorpId(), NotificationConstant.CMS.PROVIDE_ADD, loan);// 添加录入申请的消息提示
            }
        }
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_INFO_REPAIR.equals(processDefinitionKey)) {// 补录审批
            if (result) {// 审批通过
                loan.setStatus(CmsConstant.PREPARESEND);
                loan.setBizStatus(CmsConstant.BUSINESSSTATE_31);
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK + "", user);
                /*** 合同处理开始 **/
                String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                        .getValue();// 是否对接合同管理
                String relateConType = configService
                        .getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE).getValue();// 对接合同管理管理模式
                // 合同处理
                if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {// 对接合同管理
                    if (null != relateConType && relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)) {// 业务生成合同
                        UserDTO userDTO = userService.getByLoginName(loan.getCreateBy());
                        setInfoDTO(loan, userDTO);// 设置合同信息并生成合同
                    }
                }
                /*** 合同处理结束 **/
                cmsProvideLoanSendService.createSendByInfo(loan, user);// 跳过发放
                // 授信挂接，补录审批通过修改为正式占用
                if (!EmptyUtils.isEmpty(loan.getRatmodel()) && CmsConstant.IS_TRUE.equals(loan.getRatmodel())) {
                    RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, businessKey);
                    urationService.passUseRation(urationDTO);
                }
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                ProvideUtils.handleGua(loan, null, guaContractOccupServieImpl, 2, null);
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode(), BudgetConstant.STATUE_CHECK, user);
                /*** 抵质押物处理开始(补录审批通过后正式占用额度) **/
                if (loan.getIsRelatePledge() != null && loan.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
                    List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(loan.getId(),
                            loan.getBillCode());
                    guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
                }
                /*** 抵质押物处理结束 **/
            } else {// 打回
                loan.setStatus(CmsConstant.APPROVED);// 重新补录
                loan.setBizStatus(CmsConstant.BUSINESSSTATE_26);
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode(), BudgetConstant.STATUE_BACK, user);
                msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKBACK, user);
                notificationService.send(loan.getId(), loan.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAIR, loan);// 添加补录的消息提示
            }
        }
        this.saveOrUpdate(loan);
        this.getSession().flush();
        this.getSession().clear();
    }
    /***************** 审批END ********************/
    /**
     * 新增保证金占用信息
     *
     * @param vo
     * @param cmsProvideLoanInfo
     */
    public void addFcBondApply(LoanInfoVO vo, CmsProvideLoanInfo cmsProvideLoanInfo, String saveType) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(vo, cmsProvideLoanInfo, saveType, new FcBondApplyDTO());
        dto.setCreateBy(cmsProvideLoanInfo.getCreateBy());// 创建人登录名称
        dto.setCreateDate(DateUtils.getDate());// 创建日期
        fcBondHookServiceImp.addFcBondApply(dto);
    }

    /**
     * 更新保证金占用信息
     *
     * @param vo
     * @param cmsProvideLoanInfo
     * @param saveType
     */
    public void updateFcBondApply(LoanInfoVO vo, CmsProvideLoanInfo cmsProvideLoanInfo, String saveType,
                                  FcBondApplyDTO dto) {
        FcBondApplyDTO fcBondApplyDTO = this.setFcBondApplyDTO(vo, cmsProvideLoanInfo, saveType, dto);
        fcBondApplyDTO.setUpdateBy(cmsProvideLoanInfo.getUpdateBy());// 创建人登录名称
        fcBondApplyDTO.setUpdateDate(DateUtils.getDate());// 创建日期
        fcBondHookServiceImp.updateFcBondApply(fcBondApplyDTO);
    }

    /**
     * 设置保证金占用信息的值
     *
     * @param vo
     * @param cmsProvideLoanInfo
     * @return
     */
    public FcBondApplyDTO setFcBondApplyDTO(LoanInfoVO vo, CmsProvideLoanInfo cmsProvideLoanInfo, String saveType,
                                            FcBondApplyDTO dto) {
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_LOAN);// 0银行授信1银行借款2承兑汇票
        dto.setItemNo(cmsProvideLoanInfo.getBillCode());// 项目单号
        dto.setItemId(cmsProvideLoanInfo.getId());// 占用保证金的项目id
        dto.setCorpId(vo.getCorpId());// 单位id
        dto.setBankId(vo.getBankId());// 联行号
        dto.setItemMoney(vo.getPlMoney());// 项目金额
        dto.setBondPercent(vo.getBailmoneyRate());// 保证金比例
        dto.setStartDate(vo.getPlDate());// 项目开始时间
        dto.setEndDate(vo.getRepayDate());// 项目结束时间
        // dto.setItemUnit(bkrationDTO.getItemUnit());//项目期限
        dto.setBondContract(vo.getBailmoneyContract());// 保证金合同
        dto.setBondId(vo.getBondId());// 保证金id
        dto.setApplyMoney(vo.getBailmoney());// 保证金金额,即占用金额
        dto.setRemark(vo.getRmk());// 备注
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 状态为占用
        // if(saveType.equals("1")){
        // dto.setStatus(FcBondConstant.STATUE_WAIT_CHECK);//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
        // }else{
        // dto.setStatus(FcBondConstant.STATUE_TEMP);//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
        // }
        return dto;
    }

    /**
     * todo 授信可提供一个是个挂接api改造 检查授信是否已经挂接了保证金
     *
     * @param id
     * @return
     */
    public FcBondApplyDTO checkRatHookBond(String id) {
        RatUrationDTO ratBkration = urationService.getRatUrationDTOById(id);
        FcBondApplyDTO dto = fcBondHookServiceImp.getFcBondApplyByItemIds(ratBkration.getBillId(),
                ratBkration.getRatCode());
        return dto;
    }

    /**
     * 生成合同信息
     */
    public void setInfoDTO(CmsProvideLoanInfo loan, ICurrentUser user) {
        this.getEntityDao().flush();
        ConInfoDTO conInfoDTO = new ConInfoDTO();
        LoanInfoVO vo = getLoanInfoVOByBillCode(loan.getBillCode());
        conInfoDTO.setConName(vo.getCorpName() + CmsConstant.PROVIDE_LOAN_CON);// 合同名称 发起单位+产品名称+“合同”
        conInfoDTO.setCorpId(vo.getCorpId());// 合同发起人:申请单位
        conInfoDTO.setConNo(vo.getContractNo());// 合同号
        conInfoDTO.setBusId(vo.getId());
        conInfoDTO.setTypeId(vo.getConTypeId());// 合同类型 TODO 这个合同类型ID不能为空; 可能是 新增时没有合同信息
        conInfoDTO.setBeginDate(vo.getPlDate());// 生效日期
        conInfoDTO.setEndDate(vo.getRepayDate());// 失效日期
        conInfoDTO.setOutBusData(vo);
        String conId = conCommonService.registerConOpBankBorrow(conInfoDTO, user);
        loan.setConId(conId);
    }

    /**
     * 取费率信息
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("directFinanceId", id);
        return dfIntermediateCostDao.listIntermediateCostByDfId(param);
    }

    /**
     * 功能介绍 :查询借款补录完成的数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryLoanInfoData(DataTablesInput dataTablesInput, ICurrentUser user) {
        // List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        List<String> corpAuth = null;
        return loanInfoDao.queryLoanInfoData(dataTablesInput, corpAuth);
    }

    /**
     * 更新业务挂接担保合同参数
     *
     * @param id
     * @param configSatus
     */
    public void updateGuaranteeConfig(String id, String configSatus) {
        CmsProvideLoanInfo provideLoanInfo = this.get(id);
        provideLoanInfo.setIsHangGuarantee(configSatus);// 更新挂接担保合同参数
        this.saveOrUpdate(provideLoanInfo);
    }

    /**
     * 银行借款查询返回json数据
     *
     * @param list
     * @return
     */
    public Object appJsonObject(List<LoanQueryVO> list) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> returnAjaxJson = new HashMap<String, Object>();
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (LoanQueryVO loanVo : list) {
                Map<String, Object> parmsMap = new HashMap<String, Object>();
                parmsMap.put("corpName", loanVo.getCorpName());
                parmsMap.put("businessId", loanVo.getInfoBillCode());
                parmsMap.put("sendCode", loanVo.getSendBillCode());
                parmsMap.put("plMoney", NumberUtil.formatNumeric(loanVo.getPlMoney()));
                parmsMap.put("sumRepayMoney", NumberUtil.formatNumeric(loanVo.getSumRepayMoney()));
                parmsMap.put("sendMoney", NumberUtil.formatNumeric(loanVo.getSendMoney()));
                parmsMap.put("sendDate", loanVo.getSendDate() == null ? "" : loanVo.getSendDate());
                parmsMap.put("infoBillCode", loanVo.getInfoBillCode());
                parmsMap.put("repayDate", loanVo.getRepayDate() == null ? "" : loanVo.getRepayDate());
                parmsMap.put("bankName", loanVo.getBankName());
                resultList.add(parmsMap);
            }
        }
        Map<String, List<LoanQueryVO>> curMap = new HashMap<>();// 分币别存放数据
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (LoanQueryVO loanVo : list) {
                List<LoanQueryVO> queryVOS = curMap.get(loanVo.getCurId());
                if (EmptyUtils.isEmpty(queryVOS)) {
                    queryVOS = new ArrayList<>();
                    queryVOS.add(loanVo);
                } else {
                    queryVOS.add(loanVo);
                }
                curMap.put(loanVo.getCurId(), queryVOS);
            }
        }
        List<Map<String, Object>> sumList = new ArrayList<Map<String, Object>>();// 分币别汇总集合
        for (String key : curMap.keySet()) {
            List<LoanQueryVO> loans = curMap.get(key);
            Map<String, Object> parmsMap = new HashMap<String, Object>();
            // 借款总额
            BigDecimal plSumMoney = BigDecimal.ZERO;
            // 发放总额
            BigDecimal sendSumMoney = BigDecimal.ZERO;
            // 还款在途总额
            BigDecimal sumRepayPassageMoney = BigDecimal.ZERO;
            // 还款总额
            BigDecimal sumRepayMoney = BigDecimal.ZERO;
            for (LoanQueryVO loanQueryVO : loans) {
                plSumMoney = plSumMoney
                        .add(loanQueryVO.getPlMoney() == null ? BigDecimal.ZERO : loanQueryVO.getPlMoney());
                sendSumMoney = sendSumMoney
                        .add(loanQueryVO.getSendMoney() == null ? BigDecimal.ZERO : loanQueryVO.getSendMoney());
                sumRepayPassageMoney = sumRepayPassageMoney.add(loanQueryVO.getSumRepayPassageMoney() == null
                        ? BigDecimal.ZERO : loanQueryVO.getSumRepayPassageMoney());
                sumRepayMoney = sumRepayMoney
                        .add(null == loanQueryVO.getSumRepayMoney() ? BigDecimal.ZERO : loanQueryVO.getSumRepayMoney());
            }
            parmsMap.put("curName", loans.get(0).getCurName());
            parmsMap.put("plSumMoney", NumberUtil.formatNumeric(plSumMoney));
            parmsMap.put("sendSumMoney", NumberUtil.formatNumeric(sendSumMoney));
            parmsMap.put("sumRepayPassageMoney", NumberUtil.formatNumeric(sumRepayPassageMoney));
            parmsMap.put("sumRepayMoney", NumberUtil.formatNumeric(sumRepayMoney));
            parmsMap.put("sendBalanceMoney",
                    NumberUtil.formatNumeric(sendSumMoney.subtract(sumRepayMoney).subtract(sumRepayPassageMoney)));
            sumList.add(parmsMap);
        }
        returnAjaxJson.put("flag", true);
        returnAjaxJson.put("msg", "操作成功");
        returnAjaxJson.put("data", resultList);
        returnAjaxJson.put("sumData", sumList);
        returnAjaxJson.put("count", ListUtils.isNotNullOrEmpty(resultList) ? resultList.size() : "0");
        return returnAjaxJson;
    }

    /**
     * 已还未还数据
     *
     * @param sendBillCode
     */
    public void loadRepayWithSendBillCode(String sendBillCode, Model model) {
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestList(sendBillCode, "", "", "", "", "", "");
        List<CmsProvideInterestVO> yRepayList = new LinkedList<CmsProvideInterestVO>();
        List<CmsProvideInterestVO> nRepayList = new LinkedList<CmsProvideInterestVO>();
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (CmsProvideInterestVO busVo : list) {
                if (busVo.getStatus().equals(Integer.valueOf(CmsConstant.SORT_TYPE_0))) {
                    nRepayList.add(busVo);
                } else {
                    yRepayList.add(busVo);
                }
            }
        }
        model.addAttribute("yRepayList", yRepayList);
        model.addAttribute("nRepayList", nRepayList);
    }

    public void loadRepayWithSendBillCode(String sendBillCode, Map<String,Object> model) {
        List<CmsProvideInterestVO> list = cmsProvideInterestService.queryInterestList(sendBillCode, "", "", "", "", "", "");
        List<CmsProvideInterestVO> yRepayList = new LinkedList<CmsProvideInterestVO>();
        List<CmsProvideInterestVO> nRepayList = new LinkedList<CmsProvideInterestVO>();
        if (ListUtils.isNotNullOrEmpty(list)) {
            for (CmsProvideInterestVO busVo : list) {
                if (busVo.getStatus().equals(Integer.valueOf(CmsConstant.SORT_TYPE_0))) {
                    nRepayList.add(busVo);
                } else {
                    yRepayList.add(busVo);
                }
            }
        }
        model.put("yRepayList", yRepayList);
        model.put("nRepayList", nRepayList);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = loanInfoDao.queryBusBillByCorpId(corpId, null, null);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DEL_PROLOANAPPLY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 申请暂存，待审批，审批打回
            int dataSize1 = loanInfoDao.queryBusBillByCorpId(corpId,
                    CmsConstant.TEMP + "," + CmsConstant.COMMITAPPROVE + "," + CmsConstant.BACK, null);
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_ERROR)));
            }
            // 借款申请待补录，补录暂存，补录待审批，补录审批打回
            int dataSize2 = loanInfoDao.queryBusBillByCorpId(corpId, null,
                    CmsConstant.BUSINESSSTATE_20 + "," + CmsConstant.BUSINESSSTATE_21 + ","
                            + CmsConstant.BUSINESSSTATE_25 + "," + CmsConstant.BUSINESSSTATE_26);// 待补录，补录暂存，补录待审批，补录审批打回
            if (dataSize2 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_REPAIR_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    /**
     * 设置占用抵质押物数据
     *
     * @param infoVO
     * @param businessId
     * @return
     */
    public List<GuaPledgeOccupyDTO> setPledgeOccupyInfo(LoanInfoVO infoVO, String businessId) {
        List<GuaPledgeOccupyDTO> pledgeOccupyDTOS = new ArrayList<>();
        if (!EmptyUtils.isEmptyArray(infoVO.getPledgeId()) && infoVO.getPledgeId().length > 0) {
            for (int i = 0; i < infoVO.getPledgeId().length; i++) {
                if (EmptyUtils.isEmpty(infoVO.getPledgeId()[i]) || EmptyUtils.isEmpty(infoVO.getPledgeMoney()[i])) {
                    continue;
                }
                GuaPledgeOccupyDTO pledgeOccupyDTO = new GuaPledgeOccupyDTO();
                pledgeOccupyDTO.setSysCode(ConfigConstant.PROVIDE);// 银行借款
                pledgeOccupyDTO.setBusinessId(businessId);
                pledgeOccupyDTO.setItemNo(infoVO.getBillCode());
                pledgeOccupyDTO.setStartDate(infoVO.getPlDate());
                pledgeOccupyDTO.setEndDate(infoVO.getRepayDate());
                pledgeOccupyDTO.setPledgeId(infoVO.getPledgeId()[i]);
                pledgeOccupyDTO.setOccupyMoney(infoVO.getPledgeMoney()[i]);
                pledgeOccupyDTO.setCurId(infoVO.getCurId());
                pledgeOccupyDTOS.add(pledgeOccupyDTO);
            }
        }
        return pledgeOccupyDTOS;
    }

    /**
     * 查询所有补录审批通过单未还款完成数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listChangeDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> ids = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_CHANGE, user.getId());
        return loanInfoDao.listChangeDataTable(input, ids);
    }

    /**
     * 借款变更，增加或者释放抵质押物
     *
     * @param vo
     */
    public void changeLoan(LoanInfoVO vo) {
        CmsProvideLoanInfo loan = loanInfoDao.get(vo.getId());
        List<GuaPledgeOccupyDTO> newOccupyDTOS = this.setPledgeOccupyInfo(vo, loan.getId());
        if (CommonConstant.YES.equals(loan.getIsRelatePledge())) {
            if (CommonConstant.YES.equals(vo.getIsRelatePledge())) {
                // 先释放，
                guaPledgeOccupyServiceImpl.releasePledgeOccupy(loan.getId(), loan.getBillCode());
                // 在冻结
                guaPledgeOccupyServiceImpl.freezePledgeOccupy(newOccupyDTOS);
                List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(loan.getId(),
                        loan.getBillCode());
                // 在占用
                guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
            } else {
                // 直接释放
                guaPledgeOccupyServiceImpl.releasePledgeOccupy(loan.getId(), loan.getBillCode());
            }
        } else {
            if (CommonConstant.YES.equals(vo.getIsRelatePledge())) {
                // 先冻结
                guaPledgeOccupyServiceImpl.freezePledgeOccupy(newOccupyDTOS);
                List<GuaPledgeOccupyDTO> occupyDTOS = guaPledgeOccupyServiceImpl.getPledgeOccupy(loan.getId(),
                        loan.getBillCode());
                // 在占用
                guaPledgeOccupyServiceImpl.addPledgeOccupy(occupyDTOS);
            }
        }
        loan.setIsRelatePledge(vo.getIsRelatePledge());
        loanInfoDao.update(loan);
    }

    /**
     * 设置银团杰借款数据
     *
     * @param plClass 借款类型
     * @param id      借款主键id
     * @param model
     */
    public void setSyndicated(String plClass, String id, Model model) {
        if (BaseDataConstant.SYNDICATED.equals(plClass)) {
            model.addAttribute("syndicated", this.getByDirectFinanceId(id));
        }
    }
    public void setSyndicated(String plClass, String id, CmsLoanInfoViewPageVO model) {
        if (BaseDataConstant.SYNDICATED.equals(plClass)) {
            model.setSyndicated(  this.getByDirectFinanceId(id));
        }
    }

    /**
     * 查询银团贷款数据
     *
     * @param id
     * @return
     */
    public List<ProvideSyndicatedVo> getByDirectFinanceId(String id) {
        return loanSyndicatedDao.getByDirectFinanceId(id);
    }

    /**
     * 获取挂接授信的借款ID集合
     *
     * @return
     */
    public List<String> getIdListByRat() {
        return loanInfoDao.getIdListByRat();
    }

    public DataTablesOutput<GuaContractCmsProvideLoanVO> getGuaForInl(DataTablesInput input, ICurrentUser user) {
        String sysCode = input.getFormdata().get("eqs_sysCode").toString();
        String startDate = input.getFormdata().get("ged_useDate").toString();
        String endDate = input.getFormdata().get("led_useDate").toString();
        String status = input.getFormdata().get("eqg_status").toString();
        String contractId = input.getFormdata().get("eqs_contractId") == null ? "" : input.getFormdata().get("eqs_contractId").toString();
        input.setFormdata(null);
        //获取挂接授信的业务id集合和封装参数
        List<GuaContractOccupDTO> guaList = guaContractOccupServieImpl.getGua(sysCode, startDate, endDate, status,contractId);
        List<String> corpIds = corpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
        if (EmptyUtils.isEmpty(guaList)) {
            return new DataTablesOutput<>();
        }
        Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
        List<String> businessIdList = new ArrayList<>();
        guaList.forEach(item -> {
            guaMap.put(item.getBusinessId(), item);
            businessIdList.add(item.getBusinessId());
        });
        //组装参数
        DataTablesOutput<GuaContractCmsProvideLoanVO> dataTablesOutput = loanInfoDao.getGuaForCmsProvideLoan(input, corpIds, businessIdList);
        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
            for (GuaContractCmsProvideLoanVO item : dataTablesOutput.getData()) {
                if (guaMap.containsKey(item.getId())) {
                    item.setStatus(guaMap.get(item.getId()).getStatus());
                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
                }
            }
        }
        return dataTablesOutput;
    }

    public List<BtBankDTO> queryRationBank() {
        //获取已授信的联行号
        List<String> codes = urationService.queryRationBank();
        if (EmptyUtils.isNotEmpty(codes)) {
            return btBankService.getListByCodes(codes);
        }
        return new ArrayList<>();
    }

    public List<BtBankDTO> queryRationBank(String corpId) {
        //获取已授信的联行号
        List<String> codes = urationService.queryRationBank(corpId);
        if (EmptyUtils.isNotEmpty(codes)) {
            return btBankService.getListByCodes(codes);
        }
        return new ArrayList<>();
    }

    /**
     * 功能介绍 : 月度贷款明细表 - 数据
     *
     * @param queryParam 查询参数
     * @param user       用户对象
     * @return 月度贷款明细表集合
     */
    public List<MonthlyLoanVO> queryMonthlyLoanDtlData(MonthlyLoanVO queryParam, ICurrentUser user) {
        List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_MONTHLYLOAN, user.getId());
        if (CollectionUtils.isEmpty(corpAuthList)) {
            return Lists.newArrayList();
        }
        List<MonthlyLoanVO> resultList = loanInfoDao.queryMonthlyLoanDtl(queryParam, corpAuthList);
        if (CollectionUtils.isEmpty(resultList)) {
            return Lists.newArrayList();
        }
        resultList.forEach(vo -> {
            // 设置贷款年限显示值
            if ("D".equals(vo.getTernUnit())) {
                vo.setPlTerm("1 年");
            }
            if ("M".equals(vo.getTernUnit())) {
                BigDecimal plTerm = new BigDecimal(vo.getPlTerm()).divide(BigDecimal.valueOf(12), 2, BigDecimal.ROUND_HALF_UP);
                if (plTerm.compareTo(BigDecimal.valueOf(1)) < 0) {
                    vo.setPlTerm(vo.getPlTerm() + " 月");
                } else {
                    vo.setPlTerm(plTerm.stripTrailingZeros().toPlainString() + " 年");
                }
            }
        });

        // 00062025050740 月度贷款明细报表优化：通过贷款品种分类汇总，以及合计
        // 合计行：所有数据总和
        MonthlyLoanVO totalRow = this.calculateRow("合计", "", resultList);
        // 总部行：单位编码=100000总和
        List<MonthlyLoanVO> hqResultList = resultList.stream().filter(item -> "100000".equals(item.getCorpCode())).collect(Collectors.toList());
        MonthlyLoanVO hqRow = this.calculateRow("总部", "", hqResultList);
        // 地区行：单位编码不等于100000总和
        List<MonthlyLoanVO> areaResultList = resultList.stream().filter(item -> !"100000".equals(item.getCorpCode())).collect(Collectors.toList());
        MonthlyLoanVO areaRow = this.calculateRow("地区", "", areaResultList);

        // 定义最终返回结果
        LinkedList<MonthlyLoanVO> finalResultList = new LinkedList<>();
        // 根据贷款品种（借款类型）进行分组，并排序
        LinkedHashMap<String, LinkedList<MonthlyLoanVO>> plClassMap = resultList
                .stream()
                .collect(Collectors.groupingBy(item -> item.getPlClass() + CommonConstant.COMMA_SPLIT + item.getPlClassName(), TreeMap::new, Collectors.toCollection(LinkedList::new)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())// 通过key，此时的key为贷款品种（借款类型）
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        plClassMap.forEach((k, v) -> {
            // 贷款品种（借款类型）
            String plClassName = k.split(CommonConstant.COMMA_SPLIT)[1];
            // 贷款品种（借款类型）分组，每组的合计行
            MonthlyLoanVO plClassNameRow = this.calculateRow("-1", plClassName, v);
            // 按照申请单位和发放日期排序
            v = v.stream().sorted(Comparator.comparing(MonthlyLoanVO::getCorpId).thenComparing(MonthlyLoanVO::getSendDate)).collect(Collectors.toCollection(LinkedList::new));

            // 放入第一行
            v.addFirst(plClassNameRow);

            finalResultList.addAll(v);
        });
        // 末行加入合计行
        finalResultList.addLast(totalRow);
        // 末行加入总部行
        finalResultList.addLast(hqRow);
        // 末行加入地区行
        finalResultList.addLast(areaRow);
        return finalResultList;
    }

    /**
     * 计算合计，总部，地区行数据，以及贷款品种分类合计
     *
     * @param corpName    合计，总部，地区
     * @param plClassName 贷款品种名称
     * @param resultList  月度贷款明细表结果集
     * @return 行数据
     */
    public MonthlyLoanVO calculateRow(String corpName, String plClassName, List<MonthlyLoanVO> resultList) {
        // 发放金额
        BigDecimal sendMoneySum = resultList.stream().map(MonthlyLoanVO::getSendMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 已还款金额
        BigDecimal sumRepayMoneySum = resultList.stream().map(MonthlyLoanVO::getSumRepayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 待还款金额
        BigDecimal sumUnRepayMoneySum = resultList.stream().map(MonthlyLoanVO::getSumUnRepayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new MonthlyLoanVO(corpName, plClassName, sendMoneySum, sumRepayMoneySum, sumUnRepayMoneySum);
    }

    /**
     * 导出月度贷款明细表
     *
     * @param resultList 导出结果
     * @return 导出对象
     */
    public HSSFWorkbook exportMonthlyLoanDtl(List<MonthlyLoanVO> resultList) {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet("月度贷款明细表");
        // 设置列宽
        this.setColumnWidth(sheet);
        // 创建表格样式
        this.createExcel(workbook, sheet);
        // 填充数据
        this.createData(workbook, sheet, resultList);
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidth(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 18);
        // 自定义列宽
        sheet.setColumnWidth(0, 30 * 256);//设置第一列列宽
        sheet.setColumnWidth(1, 30 * 256);//设置第二列列宽
        sheet.setColumnWidth(2, 20 * 256);//设置第三列列宽
        sheet.setColumnWidth(3, 30 * 256);//设置第四列列宽
        sheet.setColumnWidth(4, 25 * 256);//设置第五列列宽
        for (int i = 5; i <= 7; i++) {
            sheet.setColumnWidth(i, 20 * 256);//设置第6-8列列宽
        }
        sheet.setColumnWidth(8, 12 * 256);//设置第九列列宽
        sheet.setColumnWidth(9, 15 * 256);//设置第十列列宽
        sheet.setColumnWidth(10, 15 * 256);//设置第十一列列宽
        sheet.setColumnWidth(11, 12 * 256);//设置第十二列列宽
        sheet.setColumnWidth(12, 12 * 256);//设置第十三列列宽
        sheet.setColumnWidth(13, 30 * 256);//设置第十四列列宽
        sheet.setColumnWidth(14, 12 * 256);//设置第十四列列宽
    }

    /**
     * 创建表格样式
     *
     * @param wb    workbook对象
     * @param sheet sheet页
     */
    public void createExcel(HSSFWorkbook wb, HSSFSheet sheet) {
        // 获取居中样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置一级表头字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 14));
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        // 创建第一行
        HSSFRow row0 = sheet.createRow(0);
        // 创建行高
        row0.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell00 = row0.createCell(0);
        cell00.setCellValue(new HSSFRichTextString("月度贷款明细表"));
        this.setCellStyle(row0, headStyle);
        // 第一行合并
        CellRangeAddress range0 = new CellRangeAddress(0, 0, 0, 14);
        sheet.addMergedRegion(range0);
        // 创建第二行
        HSSFRow row1 = sheet.createRow(1);
        String[] heads = new String[]{"区域", "申请单位", "贷款品种", "贷款银行名称", "银行账号", "发放金额", "已还金额", "待还金额", "贷款年限", "发放日期", "到期日期", "借款利率", "最新利率", "备注", "确认人"};
        row1.setHeightInPoints(20);
        for (int i = 0; i < heads.length; i++) {
            HSSFCell cell = row1.createCell((short) i);
            cell.setCellValue(heads[i]);
            cell.setCellStyle(secondStyle);
        }
    }

    /**
     * 填充数据
     *
     * @param wb         结果集
     * @param sheet      sheet页
     * @param resultList workbook对象
     */
    private void createData(HSSFWorkbook wb, HSSFSheet sheet, List<MonthlyLoanVO> resultList) {
        // 文字居中
        HSSFCellStyle centerCellStyleTotal = ExportUtils.getCenterCellStyle(wb, false);
        // 设置字体
        centerCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(wb, false);
        // 金额靠右（合计行）
        HSSFCellStyle rightCellStyleTotal = ExportUtils.getRightCellStyle(wb, false);
        DataFormat currencyDataFormat = wb.createDataFormat();
        // 设置字体
        rightCellStyleTotal.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));
        rightCellStyleTotal.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));
        // 金额靠右
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));
        // 百分比靠右
        HSSFCellStyle rightRateCellStyle = ExportUtils.getRightCellStyle(wb, false);
        rightRateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

        // 数据开始行
        AtomicInteger rowIndex = new AtomicInteger(2);
        resultList.forEach(item -> {
            // 创建数据行
            HSSFRow row = sheet.createRow(rowIndex.get());
            if ("-1".equals(item.getCorpName())) {
                // 借款类型分组合计行
                // 创建第1列
                this.addTotalDataRow("", item.getPlClassName(), item, sheet, row, rowIndex.get(), centerCellStyleTotal, rightCellStyleTotal);
            } else if ("合计".equals(item.getCorpName()) || "总部".equals(item.getCorpName()) || "地区".equals(item.getCorpName())) {
                // 合计行
                // 创建第1列
                this.addTotalDataRow(item.getCorpName(), "", item, sheet, row, rowIndex.get(), centerCellStyleTotal, rightCellStyleTotal);
            } else {
                // 基础数据行
                this.addBasicDataRow(item, row, centerCellStyle, rightCellStyle, rightRateCellStyle);
            }
            rowIndex.getAndIncrement();
        });
    }

    /**
     * 新增合计行：贷款品种分组合计，以及总计。
     *
     * @param corpName             单位名称
     * @param plClassName          贷款品种
     * @param item                 基础数据对象
     * @param sheet                sheet页
     * @param row                  行
     * @param rowIndex             第几行
     * @param centerCellStyleTotal 单元格居中
     * @param rightCellStyleTotal  单元格居右
     */
    private void addTotalDataRow(String corpName, String plClassName, MonthlyLoanVO item, HSSFSheet sheet, HSSFRow row, int rowIndex,
                                 HSSFCellStyle centerCellStyleTotal, HSSFCellStyle rightCellStyleTotal) {
        // 创建第1列
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyleTotal); // 加粗样式rightCellStyleTotal
        if (StringUtils.isNotEmpty(corpName)) {
            cell1.setCellValue(new HSSFRichTextString(item.getCorpName()));
        }
        if (StringUtils.isNotEmpty(plClassName)) {
            cell1.setCellValue(new HSSFRichTextString(item.getPlClassName()));
        }
        // 创建2-5空列
        for (int a = 1; a <= 4; a++) {
            row.createCell(a).setCellStyle(centerCellStyleTotal);
        }
        // 第三行的第1-3列合并
        CellRangeAddress range3313 = new CellRangeAddress(rowIndex, rowIndex, 0, 2);
        sheet.addMergedRegion(range3313);

        // 创建第6列发放金额列
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightCellStyleTotal);
        cell6.setCellValue(item.getSendMoney().doubleValue());
        // 创建第7列已还金额列
        HSSFCell cell7 = row.createCell(6);
        cell7.setCellStyle(rightCellStyleTotal);
        cell7.setCellValue(item.getSumRepayMoney().doubleValue());
        // 创建第8列待还金额列
        HSSFCell cell8 = row.createCell(7);
        cell8.setCellStyle(rightCellStyleTotal);
        cell8.setCellValue(item.getSumUnRepayMoney().doubleValue());
        // 创建9-15空列
        for (int a = 8; a <= 14; a++) {
            row.createCell(a).setCellStyle(centerCellStyleTotal);
        }
    }

    /**
     * 新增基础数据行
     *
     * @param item               基础数据对象
     * @param row                行
     * @param centerCellStyle    单元格居中
     * @param rightCellStyle     单元格居右
     * @param rightRateCellStyle 利率单元格居右
     */
    private void addBasicDataRow(MonthlyLoanVO item, HSSFRow row, HSSFCellStyle centerCellStyle, HSSFCellStyle rightCellStyle, HSSFCellStyle rightRateCellStyle) {
        // 创建第1列
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellStyle(centerCellStyle); // 加粗样式rightCellStyleTotal
        cell1.setCellValue(new HSSFRichTextString(item.getCellName()));
        HSSFCell cell2 = row.createCell(1);
        cell2.setCellStyle(centerCellStyle);
        cell2.setCellValue(new HSSFRichTextString(item.getCorpName()));
        HSSFCell cell3 = row.createCell(2);
        cell3.setCellStyle(centerCellStyle);
        cell3.setCellValue(new HSSFRichTextString(item.getPlClassName()));
        HSSFCell cell4 = row.createCell(3);
        cell4.setCellStyle(centerCellStyle);
        cell4.setCellValue(new HSSFRichTextString(item.getBankName()));
        HSSFCell cell5 = row.createCell(4);
        cell5.setCellStyle(centerCellStyle);
        cell5.setCellValue(new HSSFRichTextString(item.getSendAccNo()));
        HSSFCell cell6 = row.createCell(5);
        cell6.setCellStyle(rightCellStyle);
        cell6.setCellValue(item.getSendMoney().doubleValue());
        HSSFCell cell7 = row.createCell(6);
        cell7.setCellStyle(rightCellStyle);
        cell7.setCellValue(item.getSumRepayMoney().doubleValue());
        HSSFCell cell8 = row.createCell(7);
        cell8.setCellStyle(rightCellStyle);
        cell8.setCellValue(item.getSumUnRepayMoney().doubleValue());
        HSSFCell cell9 = row.createCell(8);
        cell9.setCellStyle(centerCellStyle);
        cell9.setCellValue(new HSSFRichTextString(item.getPlTerm()));
        HSSFCell cell10 = row.createCell(9);
        cell10.setCellStyle(centerCellStyle);
        cell10.setCellValue(new HSSFRichTextString(item.getSendDate()));
        HSSFCell cell11 = row.createCell(10);
        cell11.setCellStyle(centerCellStyle);
        cell11.setCellValue(new HSSFRichTextString(item.getRepayDateStr()));
        HSSFCell cell12 = row.createCell(11);
        cell12.setCellStyle(rightRateCellStyle);
        if (null != item.getPlRate()) {
            cell12.setCellValue(item.getPlRate().doubleValue());
        } else {
            cell12.setCellValue(new HSSFRichTextString());
        }
        HSSFCell cell13 = row.createCell(12);
        cell13.setCellStyle(rightRateCellStyle);
        if (null != item.getLatestRate()) {
            cell13.setCellValue(item.getLatestRate().doubleValue());
        } else {
            cell13.setCellValue(new HSSFRichTextString());
        }
        HSSFCell cell14 = row.createCell(13);
        cell14.setCellStyle(centerCellStyle);
        cell14.setCellValue(new HSSFRichTextString(item.getRmk()));
        HSSFCell cell15 = row.createCell(14);
        cell15.setCellStyle(centerCellStyle);
        cell15.setCellValue(new HSSFRichTextString(item.getSendUser()));
    }

    /**
     * 设置表头单元格样式
     *
     * @param row       某行
     * @param cellStyle 单元格样式
     */
    private void setCellStyle(HSSFRow row, HSSFCellStyle cellStyle) {
        Iterator<Cell> iterator = row.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(cellStyle);
        }
    }

    public List<BtBankAccDTO> queryPaymentAccList(ICurrentUser currentUser) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_MONTHLYLOAN, currentUser.getId());
        return btBankAccService.getListByCorpIds(corpAuth);
    }
}
