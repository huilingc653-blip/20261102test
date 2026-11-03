package com.bytter.biz.foreign.service;

import com.bytter.biz.bdlinks.service.impl.BisForeignExcServiceImpl;
import com.bytter.biz.foreign.dao.FexForeignReqDao;
import com.bytter.biz.foreign.entity.FexForeignReq;
import com.bytter.biz.foreign.entity.FexLinkmanInfo;
import com.bytter.biz.foreign.entity.FexPayerInfo;
import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.biz.foreign.vo.FexForeignReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.AttachmentDTO;
import com.bytter.system.api.IAttachmentService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.dto.BisForeignExcDTO;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.biz.payment.api.service.IErpPayInfoService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 外币汇款申请表
 * @date 2019-07-15 15:20:33
 */
@WorkFlow(FexWorkFlowConstant.FEX_FOREIGN_REQ)
@Service("fexForeignReqService")
@Transactional
public class FexForeignReqService extends BaseService<FexForeignReq, String> implements IWorkFlowListener, IExcelVerifyHandler<FexForeignReqVO>, IWorkFlowRevokeListener {
    @Autowired
    private FexForeignReqDao fexForeignReqDao;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired(required=false)
    private IBisAccBalService bisAccBalService;
    @Autowired(required=false)
    private IWorkFlowService workFlowService;
    @Autowired
    private FexMessageService fexMessageService;
    @Autowired(required=false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private FexLinkmanInfoService fexLinkmanInfoService;
    @Autowired
    private FexPayerInfoService fexPayerInfoService;
    @Autowired
    private FexTranInfoService fexTranInfoService;
    @Autowired(required=false)
    private IBtBankAccService bankAccService;
    @Autowired
    private BisForeignExcServiceImpl bisForeignExcServiceImpl;
    @Autowired(required=false)
    private IConfigService configService;
    @Autowired(required=false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @Autowired(required=false)
    private IFbsItemService fbsItemService;
    @Autowired(required=false)
    private IFbsBudgetService fbsBudgetService;
    @Autowired(required=false)
    private IAccountGrantService accountGrantService;
    @Autowired(required=false)
    private IBtSwiftInfoService btSwiftInfoService;
    @Autowired(required=false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired(required = false)
    private IAttachmentService attachmentService;
    @DubboReference(check = false)
    private IErpPayInfoService erpPayInfoService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FexForeignReq entity) {
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
    public void save(FexForeignReq entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FexForeignReq entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FexForeignReq entity) {
        super.saveOrUpdate(entity);
        getSession().flush();
    }

    @Override
    public BaseDao<FexForeignReq, String> getEntityDao() {
        return fexForeignReqDao;
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
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if ("list".equals(opType)) {
            //外币汇款申请查询打回和暂存的数据
            resource = ResourceConstant.FOREIGN_FOREIGN_FEXFOREIGNREQ_QUERY;
            sql.append(" and (f.create_by ='").append(user.getLoginName()).append("' or f.create_by is null)");
            sql.append(" and f.status in (" + ForeignConstant.STATUE_TEMP + "," + ForeignConstant.STATUE_BACK + ")");
        } else if ("query".equals(opType)) {
            //外币汇款查询，过滤掉删除状态的数据
            resource = ResourceConstant.FOREIGN_FOREIGNQUERY_FEXFOREIGNREQ_QUERY;
            //sql.append(" and f.status<>" + ForeignConstant.STATUE_DEL);
        }

        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);

        return fexForeignReqDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }

    /**
     * 根据ID获取外币汇款申请信息
     *
     * @param id
     * @return
     */
    public FexForeignReqVO getByIdUnion(String id) {
        FexForeignReqVO fexForeignReq = fexForeignReqDao.getByIdUnion(id);
        if (fexForeignReq != null) {
            //获取余额
            BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(fexForeignReq.getPayerAcc(), fexForeignReq.getCurId());
            if (bal != null) {
                fexForeignReq.setBalance(bal.getBal());
            }
            BtBankAccDTO bankAcc = bankAccService.getBankAccById(fexForeignReq.getAccId());
            if(bankAcc != null) {
                fexForeignReq.setIsOnline(bankAcc.getIsOnline());
            }
        }
        return fexForeignReq;
    }

    /**
     * 保存
     *
     * @param fexForeignReq
     */
    public void saveFexForeignReq(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        FexForeignReq entity = new FexForeignReq();
        BeanUtils.copyNotNullProperties(fexForeignReq, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_FOREIGN_REQ));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
        //保存汇款联系人信息
        this.saveLinkmanNotExist(fexForeignReq, user);
        //判断如果是1-境外汇款，保存汇款人信息
        if(ForeignConstant.REMIT_TYPE_1.equals(fexForeignReq.getRemitType())) {
            this.savePayerNotExist(fexForeignReq, user);
        }
        if (isCheckExternalCorp(fexForeignReq)) {
            //保存客商信息
            this.saveIfNotExist(fexForeignReq);
        }

        //写入计划执行
        fexForeignReq.setId(entity.getId());
        fexForeignReq.setBillNo(entity.getBillNo());
        this.writeBudgetProcess(fexForeignReq, user);

        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            fexForeignReqDao.flush();
            FexForeignReqVO fexForeignReqVO = fexForeignReqDao.getByIdUnion(entity.getId());

            getStartHandler().handler(FexWorkFlowConstant.FEX_FOREIGN_REQ, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FOREIGN, user);
        }
    }

    /**
     * 客商信息保存
     *
     * @param fexForeignReq
     */
    private void saveIfNotExist(FexForeignReqVO fexForeignReq) {
        ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
        externalCorpDTO.setCorpId(fexForeignReq.getCorpId());
        externalCorpDTO.setName(fexForeignReq.getPayeeAccName());
        externalCorpDTO.setProvince(fexForeignReq.getPayeeCountryCode());
        externalCorpDTO.setCity(fexForeignReq.getPayeeCountryName());
        externalCorpDTO.setSourceSystem("BFS");
        externalCorpDTO.setAddress(fexForeignReq.getPayeeAdd());
        //国外
        externalCorpDTO.setIsNative(0);
        //是否有效
        externalCorpDTO.setStatus(1);
        List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
        ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
        externalCorpBankAccDTO.setExternalAcc(fexForeignReq.getPayeeAcc());
        externalCorpBankAccDTO.setBankType(fexForeignReq.getBankTypeId());
        externalCorpBankAccDTO.setBank(fexForeignReq.getPayeeBank());
        externalCorpBankAccDTO.setBankSourceCode(fexForeignReq.getPayeeSwiftCode());
        externalCorpBankAccDTO.setAccProvince(fexForeignReq.getPayeeCountryCode());
        externalCorpBankAccDTO.setIsDefaultAcc("0");
        externalCorpBankAccDTO.setAccCity("");
        externalCorpBankAccList.add(externalCorpBankAccDTO);
        externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
        externalCorpService.saveIfNotExist(externalCorpDTO);
    }

    /**
     * 校验收款账户是否是网点或成员单位
     *
     * @param vo
     * @return
     */
    public boolean isCheckExternalCorp(FexForeignReqVO vo) {
        return EmptyUtils.isEmpty(corpService.getCorpIdByName(vo.getPayeeAccName()));
    }

    /**
     * 汇款联系人信息保存
     *
     * @param fexForeignReq
     */
    public void saveLinkmanNotExist(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        FexLinkmanInfo linkmanInfo = new FexLinkmanInfo();
        linkmanInfo.setCorpId(fexForeignReq.getCorpId());
        linkmanInfo.setPayerName(fexForeignReq.getPayerName());
        linkmanInfo.setPayerTel(fexForeignReq.getPayerTel());
        this.fexLinkmanInfoService.saveLinkmanNotExist(linkmanInfo, user);
    }

    /**
     * 汇款人信息保存
     *
     * @param fexForeignReq
     */
    public void savePayerNotExist(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        FexPayerInfo fexPayerInfo = new FexPayerInfo();
        fexPayerInfo.setPayerAcc(fexForeignReq.getPayerAcc());
        fexPayerInfo.setPayerNameEn(fexForeignReq.getPayerNameEn());
        fexPayerInfo.setPayerAddEn(fexForeignReq.getPayerAddEn());
        this.fexPayerInfoService.savePayerNotExist(fexPayerInfo, user);
    }

    /**
     * 写入计划执行
     *
     * @param fexForeignReq
     */
    private void writeBudgetProcess(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(fexForeignReq.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX, fexForeignReq.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < fexForeignReq.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(fexForeignReq, user);
                fbsBudgetProcessDTO.setItemCode(fexForeignReq.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(fexForeignReq.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(fexForeignReq.getProcessMoney()[i]);
                if(fexForeignReq.getExchangeRate()!=null) {
                    fbsBudgetProcessDTO.setExchangeRate(fexForeignReq.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(fexForeignReq.getProcessCurMoney()[i]);
                }
                //资金计划多组织改造-设置计划执行单位（默认业务单据所属单位）
                if (fexForeignReq.getBudgetCorpId() != null) {
                    fbsBudgetProcessDTO.setBudgetCorpId(fexForeignReq.getBudgetCorpId()[i]);
                } else {
                    fbsBudgetProcessDTO.setBudgetCorpId(fexForeignReq.getCorpId());
                }
                //执行明细信息
                if (fexForeignReq.getProcessDetailInfo() != null) {
                    fbsBudgetProcessDTO.setProcessDetailInfo(fexForeignReq.getProcessDetailInfo()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //计划执行状态同步更新（批量提交）
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX,
                    fexForeignReq.getBillNo(), fexForeignReq.getStatus(), user);
        }
    }
    /**
     * 构建计划执行
     *
     * @param fexForeignReq
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setStatus(fexForeignReq.getStatus());
        fbsBudgetProcessDTO.setBillNo(fexForeignReq.getBillNo());
        fbsBudgetProcessDTO.setBillId(fexForeignReq.getId());
        fbsBudgetProcessDTO.setProcessDate(fexForeignReq.getPayDate());
        fbsBudgetProcessDTO.setCorpId(fexForeignReq.getCorpId());
        fbsBudgetProcessDTO.setCurId(fexForeignReq.getCurId());
        fbsBudgetProcessDTO.setSettlType("1");
        fbsBudgetProcessDTO.setPayerAcc(fexForeignReq.getPayerAcc());
        fbsBudgetProcessDTO.setPayeeAcc(fexForeignReq.getPayeeAcc());
        fbsBudgetProcessDTO.setPayeeAccName(fexForeignReq.getPayeeAccName());
        fbsBudgetProcessDTO.setPayeeBank(fexForeignReq.getPayeeBank());
        fbsBudgetProcessDTO.setPurpose(fexForeignReq.getAddWord());
        fbsBudgetProcessDTO.setWishPayday(fexForeignReq.getPayDate());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_FOREIGN);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FEX);
        fbsBudgetProcessDTO.setCashItem(fexForeignReq.getCashItem());
        return fbsBudgetProcessDTO;
    }

    /**
     * 编辑外币汇款单
     *
     * @param fexForeignReq
     */
    public void updateFexForeignReq(FexForeignReqVO fexForeignReq, ICurrentUser user) {
        FexForeignReq entity = fexForeignReqDao.get(fexForeignReq.getId(), true);
        BeanUtils.copyNotNullProperties(fexForeignReq, entity);
        if (EmptyUtils.isBlank(entity.getCreateBy())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //保存汇款联系人信息
        this.saveLinkmanNotExist(fexForeignReq, user);
        //判断如果是1-境外汇款，保存汇款人信息
        if(ForeignConstant.REMIT_TYPE_1.equals(fexForeignReq.getRemitType())) {
            this.savePayerNotExist(fexForeignReq, user);
        }
        //写入计划执行
        this.writeBudgetProcess(fexForeignReq, user);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_UPDATE_SUBMIT_SUCC;
            //调用审批后方法
            FexForeignReqVO fexForeignReqVO = fexForeignReqDao.getByIdUnion(entity.getId());

            getStartHandler().handler(FexWorkFlowConstant.FEX_FOREIGN_REQ, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FOREIGN, user);
        //完成通知提醒
        notificationService.complete(NotificationConstant.FOREIGN.FOREIGNREQ_APPLY, entity.getId(), user.getId());
    }

    /**
     * 逻辑删除外币汇款单
     *
     * @param id
     */
    public void delFexForeignReq(String id, ICurrentUser user, boolean isBatch, String chargeBackType) {
        FexForeignReq entity = this.get(id, true);
        if (ForeignConstant.BILLFROM_1.equals(entity.getBillFrom())
                && StringUtils.isNotBlank(entity.getErpId())) {
            erpPayInfoService.updateErpPayInfo(entity.getErpId(), chargeBackType);
        }
        entity.setStatus(ForeignConstant.STATUE_DEL);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
        //生成日志
        String message = isBatch ? FexMessageConstant.FEX_LOG_BATCH_DELETE_SUCC : FexMessageConstant.FEX_LOG_DELETE_SUCC;
        fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_DELETE, message, FexMessageConstant.BUS_TYPE_FOREIGN, user);
        //完成通知提醒
        notificationService.complete(NotificationConstant.FOREIGN.FOREIGNREQ_APPLY, entity.getId(), user.getId());
    }

    /**
     * 批量删除外币汇款单
     *
     * @param ids
     */
    public void batchDelFexForeignReq(String ids, ICurrentUser user, String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delFexForeignReq(id, user, true, chargeBackType);
        }
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_FOREIGN_REQ, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "f.id", true);
        DataTablesOutput dp = this.fexForeignReqDao.dataTableApprove(dataTablesInput, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 获取审批列表单位树
     *
     * @param user
     * @return
     */
    public Object treeByCorpIds(ICurrentUser user) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_FOREIGN_REQ, user.getId());
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "f.id", true);
        List<String> corpIds = this.fexForeignReqDao.getCorpIdsByDataTableApprove(businessSql);
        if (EmptyUtils.isEmptyList(corpIds)) {
            return tempList;
        }
        List<CorpDTO> corpList = this.corpService.listCorpDTOByCorpIds(corpIds);
        if (EmptyUtils.isEmptyList(corpList)) {
            return tempList;
        }
        for (CorpDTO corp : corpList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", corp.getId());
            map.put("pId", corp.getParentId());
            map.put("name", corp.getName());
            map.put("type", corp.getType());
            map.put("code", corp.getCode());
            map.put("netId", corp.getNetId());
            tempList.add(map);
        }
        return tempList;
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexForeignReq fexForeignReq = this.get(workFlowResult.getBusinessKey(), true);
        this.approvePassEnd(fexForeignReq, workFlowResult.isPass(), user);
        Integer messageType = workFlowResult.isPass() ? FexMessageConstant.MESSAGE_TYPE_CHECK_PASS : FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = workFlowResult.isPass() ? FexMessageConstant.FEX_LOG_APP_SUCC : FexMessageConstant.FEX_LOG_APP_BACK;
        //生成日志
        fexMessageService.saveMessage(fexForeignReq.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FOREIGN, user);
    }

    /**
     * 审批通过后处理
     *
     * @param fexForeignReq
     * @param result
     */
    public void approvePassEnd(FexForeignReq fexForeignReq, boolean result, ICurrentUser user) {
        if (!result) {
            //更新审批状态
            fexForeignReq.setStatus(ForeignConstant.STATUE_BACK);
            fexForeignReq.setUpdateBy(user.getLoginName());
            fexForeignReq.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(fexForeignReq);
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX,
                    fexForeignReq.getBillNo(), ForeignConstant.STATUE_BACK, user);
        } else {
            //更新审批状态
            fexForeignReq.setStatus(ForeignConstant.STATUE_CHECK);
            fexForeignReq.setUpdateBy(user.getLoginName());
            fexForeignReq.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(fexForeignReq);

            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX,
                    fexForeignReq.getBillNo(), ForeignConstant.STATUE_CHECK, user);

            //发送汇款指令
            this.saveBisForeignExc(fexForeignReq, user);
        }
    }

    /**
     * 汇款指令生成
     *
     * @param fexForeignReq
     */
    public void saveBisForeignExc(FexForeignReq fexForeignReq, ICurrentUser user) {
        //是否线下付款为是，不发指令
        if (CommonConstant.YES.equals(fexForeignReq.getIsPayed())) {
            return;
        }
        //获取付款账号
        BtBankAccDTO payerAcc = btBankAccService.getBankAccById(fexForeignReq.getAccId());
        //离线账号不生成指令
        if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
            return;
        }
        //生成付款指令
        BisForeignExcDTO bisExc = new BisForeignExcDTO();
        bisExc.setVoucherFrom(ForeignConstant.FROM_FOREIGN);
        bisExc.setCbsBillId(fexForeignReq.getId());
        bisExc.setCbsBillNo(fexForeignReq.getBillNo());
        bisExc.setCorpId(fexForeignReq.getCorpId());
        bisExc.setReqDate(DateUtils.getDate());
        bisExc.setPayDate(fexForeignReq.getPayDate());
        bisExc.setRemitType(fexForeignReq.getRemitType());
        bisExc.setBankTypeId(fexForeignReq.getBankTypeId());
        //付款方信息
        bisExc.setPayerAcc(payerAcc.getBankAcc());
        bisExc.setPayerAccName(payerAcc.getAccName());
        bisExc.setPayerBank(payerAcc.getBankName());
        bisExc.setPayerName(fexForeignReq.getPayerName());
        bisExc.setPayerTel(fexForeignReq.getPayerTel());
        bisExc.setCurId(fexForeignReq.getCurId());
        bisExc.setPayerCurId(fexForeignReq.getCurId());
        bisExc.setPayMoney(fexForeignReq.getPayMoney());
        bisExc.setBifCode(payerAcc.getBifCode());
        //收款方信息
        bisExc.setPayeeAcc(fexForeignReq.getPayeeAcc());
        bisExc.setPayeeAccName(fexForeignReq.getPayeeAccName());
        bisExc.setPayeeBank(fexForeignReq.getPayeeBank());
        bisExc.setPayeeSwiftCode(fexForeignReq.getPayeeSwiftCode());
        bisExc.setPayeeCountryCode(fexForeignReq.getPayeeCountryCode());
        bisExc.setPayeeAgentSwiftCode(fexForeignReq.getPayeeAgentSwiftCode());
        bisExc.setPayeeAgentBank(fexForeignReq.getPayeeAgentBank());
        bisExc.setPayeeAgentBankAdd(fexForeignReq.getPayeeAgentBankAdd());
        bisExc.setPayeeAgentAcc(fexForeignReq.getPayeeAgentAcc());

        //汇款方名称(英文)
        bisExc.setPayerNameEn(fexForeignReq.getPayerNameEn());
        //汇款方地址(英文)
        bisExc.setPayerAddrEn(fexForeignReq.getPayerAddEn());
        //收款方名称（英文）
        bisExc.setPayeeNameEn(fexForeignReq.getPayeeAccName());
        //收款方地址（英文）
        bisExc.setPayeeAddrEn(fexForeignReq.getPayeeAdd());

        //其他信息
        bisExc.setQuickSign(fexForeignReq.getQuickSign());
        bisExc.setRemitNature(fexForeignReq.getRemitNature());
        bisExc.setRemitMethod(fexForeignReq.getRemitMethod());
        bisExc.setCostSign(fexForeignReq.getCostSign());
        bisExc.setPayType(fexForeignReq.getPayType());
        bisExc.setShipDate(fexForeignReq.getShipDate());
        bisExc.setIsBonded(fexForeignReq.getIsBonded());
        bisExc.setBusNo(fexForeignReq.getBusNo());
        bisExc.setContractNo(fexForeignReq.getContractNo());
        bisExc.setInvoiceNo(fexForeignReq.getInvoiceNo());
        bisExc.setAddWord(fexForeignReq.getAddWord());
        bisExc.setRmk(fexForeignReq.getRmk());
        bisExc.setTranCode1(fexForeignReq.getTranCode1());
        bisExc.setTranAmt1(fexForeignReq.getTranAmt1());
        bisExc.setTranCurId1(fexForeignReq.getTranCurId1());
        bisExc.setTranAddWord1(fexForeignReq.getTranAddWord1());
        bisExc.setTranCode2(fexForeignReq.getTranCode2());
        bisExc.setTranAmt2(fexForeignReq.getTranAmt2());
        bisExc.setTranCurId2(fexForeignReq.getTranCurId2());
        bisExc.setTranAddWord2(fexForeignReq.getTranAddWord2());

        //新增信息
        /**费用交付方式  1-现金 2-支票 3-账户 默认1*/
        bisExc.setCostMode(ForeignConstant.COST_MODE_1);
        /**扣费账号 默认汇款账号*/
        bisExc.setDedAcc(fexForeignReq.getPayerAcc());
        /**钞汇代码 1-钞 2-汇 默认2*/
        bisExc.setBankCurCode(ForeignConstant.BANK_CUR_CODE_2);
        /**起息日期 默认汇款日期*/
        bisExc.setInterestDate(fexForeignReq.getPayDate());
        //中国农业银行特殊字段：账户类别（汇款账号）、收款人类型、汇款去向、汇款用途
        bisExc.setAccType(fexForeignReq.getAccType());
        bisExc.setPayeeType(fexForeignReq.getPayeeType());
        bisExc.setRemitRoute(fexForeignReq.getRemitRoute());
        bisExc.setRemitUse(fexForeignReq.getRemitUse());
        //保存附件名称
        if (StringUtils.isNotBlank(fexForeignReq.getAttachmentId())) {
            List<AttachmentDTO> list = attachmentService.findAttachmentByGroupId(fexForeignReq.getAttachmentId());
            if (EmptyUtils.isNotEmpty(list)){
                //接口只读取单个附件，如果存在多个附件，默认取第一个（调整为路径）
                bisExc.setFileName(list.get(0).getFilePath());
            }
        }
        //办理机构
        bisExc.setBankOrgCode(fexForeignReq.getBranchCode());
        //中国银行取统一社会信用代码
        BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(bisExc.getBifCode());
        if (bifInitDTO != null && "中国银行".equals(bifInitDTO.getName())) {
            CorpDTO corp = corpService.get(bisExc.getCorpId());
            bisExc.setBankOrgCode(corp!=null?corp.getSocCode():"");
        }
        //进口日期
        bisExc.setArrivalDate(fexForeignReq.getArrivalDate());
        //报关单号
        bisExc.setDeclareNo(fexForeignReq.getDeclareNo());
        //资金性质
        bisExc.setAccNature(fexForeignReq.getAccNature());
        //跨行标志
        bisExc.setDifBank(fexForeignReq.getDifBank());
        //支付类型
        bisExc.setBankPayType(fexForeignReq.getBankPayType());
        //收款人类型
        bisExc.setPayeeType(fexForeignReq.getPayeeType());
        //受益人关联关系
        bisExc.setBnfRelate(fexForeignReq.getBnfRelate());
        // 判断指令发送模式
        String bisExcAutoAccount = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BISEXC_AUTOACCOUNT).getValue();
        if (CommonConstant.YES.equalsIgnoreCase(bisExcAutoAccount)) {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_AUTO);
            this.bisForeignExcServiceImpl.autoSendBisForeignExc(bisExc, user);
        } else {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
            this.bisForeignExcServiceImpl.addBisForeignExc(bisExc, user);
        }
    }
    
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexForeignReq fexForeignReq = this.get(workFlowResult.getBusinessKey(), true);
        //更新审批状态
        fexForeignReq.setStatus(NetbankConstant.STATUE_TEMP);
        fexForeignReq.setUpdateBy(user.getLoginName());
        fexForeignReq.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(fexForeignReq);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_FOREIGN, BudgetConstant.PROCESS_SYSNAME_FEX, fexForeignReq.getBillNo(), NetbankConstant.STATUE_TEMP, user);
        //生成日志
        fexMessageService.saveMessage(fexForeignReq.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, FexMessageConstant.FEX_LOG_WITHDRAW_SUCC, FexMessageConstant.BUS_TYPE_FOREIGN, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(FexForeignReqVO vo) {
        List<String> msgList = new ArrayList<>();
        // 判断单位在系统中是否存在
        CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
        if(EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())){
            msgList.add("nis.netbank.message.corp.null");
        }else{
            vo.setCorpId(corp.getId());
        }

        //汇款类型不为空时，校验格式
        if (!ForeignConstant.REMIT_TYPE_0.equals(vo.getRemitType())
                && !ForeignConstant.REMIT_TYPE_1.equals(vo.getRemitType())) {
            msgList.add("fex.foreign.message.remittype.format");
        }

        //汇款日期 默认系统当前日期
        vo.setPayDate(DateUtils.getDateYMD());

        //汇款币种
        String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
        if (EmptyUtils.isBlank(curId)) {
            msgList.add("fex.foreign.message.cur.null");
        } else {
            vo.setCurId(curId);
            vo.setTranCurId1(curId);
            vo.setTranCurId2(curId);
        }

        //银行类别
        BankTypeDTO bankType = bankTypeService.getByTypeName(vo.getBankTypeName());
        if(bankType != null) {
            vo.setBankTypeId(bankType.getId());
        }else{
            msgList.add("nis.netbank.message.btbanktype.null");
        }

        //汇款账号
        BtBankAccDTO bankAcc = null;
        if(StringUtils.isEmpty(vo.getPayerAcc())){
            msgList.add("fex.foreign.message.payeracc.null");
        }else{
            if(StringUtils.isNotBlank(vo.getCorpId())) {
                //获取汇款账号
                bankAcc = this.isExistBankAcc(vo.getCorpId(), vo.getPayerAcc());
                if (EmptyUtils.isEmpty(bankAcc)) {
                    msgList.add("fex.foreign.message.btbankacc.null");
                } else {
                    if(StringUtils.isNotBlank(vo.getBankTypeId()) && !bankAcc.getBankTypeId().equals(vo.getBankTypeId())){
                        msgList.add("fex.foreign.message.banktype.mismatch");
                    }
                    vo.setAccId(bankAcc.getId());
                    vo.setPayerAccName(bankAcc.getAccName());
                    vo.setPayerBank(bankAcc.getBankName());
                }
            }
        }

        //汇款人名称（英文）
        /*if(ForeignConstant.REMIT_TYPE_1.equals(vo.getRemitType()) && EmptyUtils.isBlank(vo.getPayerNameEn())) {
            msgList.add("fex.foreign.message.payernameen.null");
        }*/
        if (EmptyUtils.isBlank(vo.getPayerNameEn())){
            msgList.add("fex.foreign.message.payernameen.null");
        }

        ExternalCorpDTO externalCorp = null;
        ExternalCorpBankAccDTO externalCorpBankAcc = null;
        if(StringUtils.isNotBlank(vo.getCorpId()) && StringUtils.isNotBlank(vo.getPayeeAccName())){
            externalCorp = externalCorpService.findByCorpIdAndName(vo.getCorpId(), vo.getPayeeAccName());
            if(externalCorp != null){
                if(StringUtils.isBlank(vo.getPayeeAdd())) {
                    vo.setPayeeAdd(externalCorp.getAddress());
                }

                externalCorpBankAcc = externalCorpService.getExternalCorpBankAccByIdAndAcc(externalCorp.getId(), vo.getPayeeAcc());
                if (externalCorpBankAcc != null) {
                    if(StringUtils.isBlank(vo.getPayeeSwiftCode())) {
                        vo.setPayeeSwiftCode(externalCorpBankAcc.getBankSourceCode());
                    }
                    if(StringUtils.isBlank(vo.getPayeeBank())) {
                        vo.setPayeeBank(externalCorpBankAcc.getBank());
                    }
                    if(StringUtils.isBlank(vo.getPayeeCountryName())) {
                        vo.setPayeeCountryCode(externalCorpBankAcc.getAccProvince());
                    }
                    if(StringUtils.isBlank(vo.getPayeeBankAdd())) {
                        vo.setPayeeBankAdd(externalCorpBankAcc.getAccAddress());
                    }
                }
            }
        }

        //判断SWIFTCODE在系统中是否存在
        if (!EmptyUtils.isBlank(vo.getPayeeSwiftCode())) {
            BtSwiftInfoDTO btSwiftInfo = this.btSwiftInfoService.getByCode(vo.getPayeeSwiftCode());
            if (EmptyUtils.isEmpty(btSwiftInfo)) {
                msgList.add("fex.foreign.message.bank.null");
            } else {
                vo.setPayeeSwiftCode(btSwiftInfo.getSwiftCode());
                vo.setPayeeBank(btSwiftInfo.getBankName());
                vo.setPayeeCountryCode(btSwiftInfo.getCountryCode());
                vo.setPayeeBankAdd(btSwiftInfo.getBankAdd());
            }
        } else {
            if (EmptyUtils.isBlank(vo.getPayeeBank())) {
                msgList.add("fex.foreign.message.payeebankcode.null");
            } else {
                BtSwiftInfoDTO btSwiftInfo = this.btSwiftInfoService.getByBankName(vo.getPayeeBank());
                if (EmptyUtils.isEmpty(btSwiftInfo)) {
                    msgList.add("fex.foreign.message.bank.null");
                } else {
                    vo.setPayeeSwiftCode(btSwiftInfo.getSwiftCode());
                    vo.setPayeeBank(btSwiftInfo.getBankName());
                    vo.setPayeeCountryCode(btSwiftInfo.getCountryCode());
                    vo.setPayeeBankAdd(btSwiftInfo.getBankAdd());
                }
            }
        }

        //汇款金额不能为空且必须大于0
        if (EmptyUtils.isEmpty(vo.getPayMoney())) {
            msgList.add("fex.foreign.message.paymoney.null");
        } else {
            if (vo.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
                msgList.add("fex.foreign.message.paymoney.mismatch");
            }
        }

        //购汇金额 离线账号汇款时校验
        if(bankAcc != null && CommonConstant.NO.equals(bankAcc.getIsOnline())){
            if(vo.getForeignExrate() == null || vo.getPayMoney() == null){
                vo.setForeignMoney(BigDecimal.ZERO);
                vo.setForeignExrate(BigDecimal.ZERO);
            }else{
                vo.setForeignMoney(vo.getPayMoney().multiply(vo.getForeignExrate()));
            }
        }

        //发电等级
        if (!ForeignConstant.QUICK_SIGN_0.equals(vo.getQuickSign())
                && !ForeignConstant.QUICK_SIGN_1.equals(vo.getQuickSign())) {
            msgList.add("fex.foreign.message.quicksign.format");
        }

        //付汇性质 0-境外汇款时必填
        if(ForeignConstant.REMIT_TYPE_0.equals(vo.getRemitType())) {
            if(StringUtils.isBlank(vo.getRemitNature())){
                msgList.add("fex.foreign.message.remitnature.null");
            }else {
                if (!ForeignConstant.REMIT_NATURE_1.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_2.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_3.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_4.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_5.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_6.equals(vo.getRemitNature())
                        && !ForeignConstant.REMIT_NATURE_7.equals(vo.getRemitNature())) {
                    msgList.add("fex.foreign.message.remitnature.format");
                }
            }
        }else{
            vo.setRemitNature("");
        }

        //汇款方式
        if (!ForeignConstant.REMIT_METHOD_0.equals(vo.getRemitMethod())
                && !ForeignConstant.REMIT_METHOD_1.equals(vo.getRemitMethod())) {
            msgList.add("fex.foreign.message.remitmethod.format");
        }

        //国内外费用承担标志
        if (!ForeignConstant.COST_SIGN_1.equals(vo.getCostSign())
                && !ForeignConstant.COST_SIGN_2.equals(vo.getCostSign())
                && !ForeignConstant.COST_SIGN_3.equals(vo.getCostSign())) {
            msgList.add("fex.foreign.message.costsign.format");
        }

        //付款类型
        if (!ForeignConstant.PAY_TYPE_1.equals(vo.getPayType())
                && !ForeignConstant.PAY_TYPE_2.equals(vo.getPayType())
                && !ForeignConstant.PAY_TYPE_3.equals(vo.getPayType())
                && !ForeignConstant.PAY_TYPE_4.equals(vo.getPayType())) {
            msgList.add("fex.foreign.message.paytype.format");
        }

        //最迟装运日期 付款类型为1-预付货运款时校验
        if(ForeignConstant.PAY_TYPE_1.equals(vo.getPayType())) {
            if (vo.getShipDate() == null) {
                msgList.add("fex.foreign.message.shipdate.null");
            }
        }

        //是否为保税货物项下付款
        if (!CommonConstant.YES.equals(vo.getIsBonded())
                && !CommonConstant.NO.equals(vo.getIsBonded())) {
            msgList.add("fex.foreign.message.isbonded.format");
        }

        //交易编码1
        FexTranInfo fexTranInfo = fexTranInfoService.getByTranName(vo.getTranName1());
        if(fexTranInfo != null){
            vo.setTranCode1(fexTranInfo.getTranCode());
        }else{
            msgList.add("fex.foreign.message.traninfo1.null");
        }

        //相应金额1不能为空且必须大于0
        if (EmptyUtils.isEmpty(vo.getTranAmt1())) {
            msgList.add("fex.foreign.message.tranamt1.null");
        } else {
            if (vo.getTranAmt1().compareTo(BigDecimal.ZERO) <= 0) {
                msgList.add("fex.foreign.message.tranamt1.mismatch");
            }
        }

        //交易附言1
        if (EmptyUtils.isEmpty(vo.getTranAddWord1())) {
            msgList.add("fex.foreign.message.tranaddword1.null");
        }

        //交易编码2
        if(StringUtils.isNotBlank(vo.getTranName2())){
            FexTranInfo fexTranInfo2 = fexTranInfoService.getByTranName(vo.getTranName1());
            if(fexTranInfo2 != null){
                vo.setTranCode1(fexTranInfo2.getTranCode());
            }else{
                msgList.add("fex.foreign.message.traninfo2.null");
            }

            //交易编码1和交易编码2不允许相同
            if(vo.getTranName2().equals(vo.getTranName1())){
                msgList.add("fex.foreign.message.trancode.same");
            }

            //相应金额2不能为空且必须大于0
            if (EmptyUtils.isEmpty(vo.getTranAmt2())) {
                msgList.add("fex.foreign.message.tranamt2.null");
            } else {
                if (vo.getTranAmt2().compareTo(BigDecimal.ZERO) <= 0) {
                    msgList.add("fex.foreign.message.tranamt2.mismatch");
                }
            }

            //交易附言2
            if (EmptyUtils.isEmpty(vo.getTranAddWord2())) {
                msgList.add("fex.foreign.message.tranaddword2.null");
            }
        }

        //判断是否挂计划,计划科目校验
        if (this.isBudgetSel()) {
            if (EmptyUtils.isEmpty(vo.getItemCodeImp())) {
                msgList.add("nis.assign.message.itemcode.null");
            } else {
                FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCodeImp());
                if (EmptyUtils.isEmpty(fbsItemDTO)) {
                    //判断计划科目是否存在
                    msgList.add("nis.assign.message.bustype.itemcode.null");
                } else {
                    vo.setItemCode(new String[]{fbsItemDTO.getItemCode()});
                    vo.setItemName(new String[]{fbsItemDTO.getItemName()});
                    vo.setProcessMoney(new BigDecimal[]{vo.getPayMoney()});
                    FbsBudgetProcessCommonDTO dto = fbsBudgetService.getFbsBudgetProcessByItemCode(vo.getCorpId(), fbsItemDTO.getItemCode(), vo.getPayDate(), vo.getBillNo(), null);
                    //计划执行科目余额控制方式
                    String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
                    if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
                        //受本科目控制
                        if (vo.getPayMoney().doubleValue() > dto.getItemMoney().doubleValue()) {
                            msgList.add("fbs.message.detailmoney.gt.itembalance");
                        }
                    }
                    if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
                        //受上级科目控制
                        if (vo.getPayMoney().doubleValue() > dto.getSuperItemMoney().doubleValue()) {
                            msgList.add("fbs.message.detailmoney.gt.superitembalance");
                        }
                    }
                    if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
                        //受大科目控制
                        if (vo.getPayMoney().doubleValue() > dto.getTopItemMoney().doubleValue()) {
                            msgList.add("fbs.message.detailmoney.gt.topitembalance");
                        }
                    }
                }
            }
        }

        if(EmptyUtils.isBlank(vo.getDifBank())){
            msgList.add("fex.foreign.message.difBank.null");
        } else if (!CommonConstant.YES.equals(vo.getDifBank()) && !CommonConstant.NO.equals(vo.getDifBank())) {
            msgList.add("fex.foreign.message.difBank.mismatch");
        }

        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }

    @Override
    public void verifyHandler(List<FexForeignReqVO> list) {

    }

    /**
     * 判断是否挂资金计划
     * @return
     */
    public boolean isBudgetSel() {
        //挂资金计划参数
        String budgetSel = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BUDGETSEL).getValue();
        if (CommonConstant.VALID_SIGN_YES.equals(budgetSel)) {
            //挂资金计划
            return true;
        }
        return false;
    }

    /**
     * 判断银行账号是否授权
     *
     * @param corpId
     * @param acc
     */
    public BtBankAccDTO isExistBankAcc(String corpId, String acc) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
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
        BtBankAccDTO btBankAccDTO = null;
        if (!EmptyUtils.isEmptyList(listAcc)) {
            for (BtBankAccDTO bankAcc : listAcc) {
                if (bankAcc.getBankAcc().equals(acc)) {
                    return bankAcc;
                }
            }
        }
        return btBankAccDTO;
    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<FexForeignReqVO> list, ICurrentUser user) {
        for (FexForeignReqVO fexForeignReq : list) {
            fexForeignReq.setStatus(ForeignConstant.STATUE_TEMP);
            this.saveFexForeignReq(fexForeignReq, user);
        }
    }
}
