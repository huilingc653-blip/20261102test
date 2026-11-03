package com.bytter.accountReceivable.docManage.service;

import com.bytter.accountReceivable.constant.ResourceConstant;
import com.bytter.accountReceivable.constant.AccountReceivableConstant;
import com.bytter.accountReceivable.docManage.dao.DocManageDao;
import com.bytter.accountReceivable.docManage.entity.DocManage;
import com.bytter.accountReceivable.docManage.entity.InvoiceDoc;
import com.bytter.accountReceivable.docManage.vo.DocManageVo;
import com.bytter.accountReceivable.docManage.vo.InvoiceDocVo;
import com.bytter.accountReceivable.writeOff.constant.WriteoffConstant;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yangxd
 * @version 1.0
 * @Title:
 * @Description:
 * @date 2025-03-26
 */
@WorkFlow(AccountReceivableConstant.DOC_MANAGE_APPROVE)
@Service("docManageService")
@Transactional
public class DocManageService extends BaseService<DocManage, String> implements IUserExcelVerifyHandler<DocManageVo>, IWorkFlowListener {

    @Autowired
    private DocManageDao docManageDao;
    @Autowired
    private InvoiceDocService invoiceDocService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private INisMessageService messageService;

    @Override
    public BaseDao<DocManage, String> getEntityDao() {
        return docManageDao;
    }

    /**
     * 生成一个单据号码
     *
     * @date 2025-03-26
     */
    public String getDocManageCode() {
        return SequenceSingleton.getSequence(SequenceEnum.ACCOUNT_RECEIVABLE_DOC_MANAGE);
    }

    /**
     * 保存应收单
     */
    public void saveDocManage(DocManageVo manageVo, ICurrentUser user) {
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        DocManage docManage = new DocManage();
        List<InvoiceDoc> invoiceDocs = new ArrayList<>();
        BeanUtils.copyProperties(manageVo, docManage);
        if(!EmptyUtils.isEmpty(manageVo.getContractMoney())){
            docManage.setContractMoney(manageVo.getContractMoney());
        }
        docManage.setTenantId(user.getTenantId());
        docManage.setCreateBy(user.getId());
        docManage.setWriteOffStatus(AccountReceivableConstant.WRITEOFFSTATUS_SAVE);
        docManageDao.save(docManage);
        if (!ObjectUtils.isEmpty(manageVo.getInvoiceVos())) {
            for (InvoiceDocVo invoiceVo : manageVo.getInvoiceVos()) {
                if (isInvoiceDocVo(invoiceVo)){
                    continue;
                }
                InvoiceDoc invoiceDoc = new InvoiceDoc();
                BeanUtils.copyNotNullProperties(invoiceVo, invoiceDoc);
                invoiceDoc.setCreateBy(user.getId());
                invoiceDoc.setTenantId(user.getTenantId());
                invoiceDoc.setBillId(docManage.getId());
                invoiceDoc.setBillNo(manageVo.getCode());
                invoiceDocs.add(invoiceDoc);
            }
            invoiceDocService.batchSave(invoiceDocs);
        }
        //资金预算
        if (this.isBudget(docManage.getCorpId(),
                BudgetConstant.BUSINESS_NAME_BUDGETSEL,
                BudgetConstant.BUSINESS_NAME_DEFAULT_BUDGETSEL)) {
            //占用资金计划
            bulidFbsBudgetProcessDTO(manageVo,Integer.valueOf(AccountReceivableConstant.STATUS_WAIT_CHECK), docManage.getId(), user);
        }
        //启动工作流
        if (AccountReceivableConstant.STATUS_WAIT_CHECK.equals(docManage.getStatus())) {
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(AccountReceivableConstant.DOC_MANAGE_APPROVE, docManage.getId(), docManage, docManage.getCorpId(), user.getId());
        }

        messageService.saveMessage(docManage.getId(),messageType,message,NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());

    }


    /**
     * 编辑，变更应收单
     */
    public void updateDocManage(DocManageVo manageVo,ICurrentUser user) {
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        DocManage docManage = new DocManage();
        List<InvoiceDoc> invoiceDocs = new ArrayList<>();
        BeanUtils.copyProperties(manageVo, docManage);
        docManage.setTenantId(user.getTenantId());
        docManage.setUpdateBy(user.getId());
        if(!EmptyUtils.isEmpty(manageVo.getContractMoney())){
            docManage.setContractMoney(manageVo.getContractMoney());
        }
        docManage.setWriteOffStatus(AccountReceivableConstant.WRITEOFFSTATUS_SAVE);
        if (AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString().equals(docManage.getStatus())) {
            docManage.setReasonStatus(String.valueOf(docManage.getStatus()));
        }

        if (!ObjectUtils.isEmpty(manageVo.getInvoiceVos())) {
            invoiceDocService.delInvoiceDocByBillId(docManage.getId());
            for (InvoiceDocVo invoiceVo : manageVo.getInvoiceVos()) {
                if (isInvoiceDocVo(invoiceVo)){
                    continue;
                }
                InvoiceDoc invoiceDoc = new InvoiceDoc();
                BeanUtils.copyNotNullProperties(invoiceVo, invoiceDoc);
                invoiceDoc.setUpdateBy(user.getId());
                invoiceDoc.setTenantId(user.getTenantId());
                invoiceDoc.setBillId(docManage.getId());
                invoiceDoc.setBillNo(manageVo.getCode());
                invoiceDocs.add(invoiceDoc);
            }

        }
        //资金预算
        if (this.isBudget(manageVo.getCorpId(),
                BudgetConstant.BUSINESS_NAME_BUDGETSEL,
                BudgetConstant.BUSINESS_NAME_DEFAULT_BUDGETSEL)) {
            //占用资金计划
            bulidFbsBudgetProcessDTO(manageVo,Integer.valueOf(AccountReceivableConstant.STATUS_CHECK),docManage.getId(), user);
        }
        if(AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString().equals(docManage.getReasonStatus())){
            docManage.setStatus(AccountReceivableConstant.STATUS_WAIT_CHECK);
        }
        docManageDao.update(docManage);
        //启动编辑、变更工作流
        if (AccountReceivableConstant.STATUS_WAIT_CHECK.equals(docManage.getStatus()) || AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString().equals(docManage.getReasonStatus())) {
            if(AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString().equals(docManage.getReasonStatus())){
                String reasonTitle = docManageDao.queryDictTypeNameByGrepAndTypeCode("account.receivable.doc.reasonTitle", manageVo.getReasonTitle());
                message =String.format("应收单变更，调整原因：[%s]，说明：[%s]",reasonTitle, manageVo.getReasonContent());
            }else{
                message = NisMessageConstant.NIS_LOG_UPDATE_SUBMIT_SUCC;
            }
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(AccountReceivableConstant.DOC_MANAGE_APPROVE, docManage.getId(), docManage, docManage.getCorpId(), user.getId());
        }
        /*//变更工作流
        if (AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString().equals(docManage.getReasonStatus())) {
            String reasonTitle = docManageDao.queryDictTypeNameByGrepAndTypeCode("account.receivable.doc.reasonTitle", manageVo.getReasonTitle());
            message =String.format("应收单变更，调整原因：[%s]，说明：[%s]",reasonTitle, manageVo.getReasonContent());
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            getStartHandler().handler(AccountReceivableConstant.DOC_MANAGE_APPROVE, docManage.getId(), docManage, docManage.getCorpId(), user.getId());
            docManage.setStatus(AccountReceivableConstant.STATUS_WAIT_CHECK);

        }*/
        if (!ObjectUtils.isEmpty(manageVo.getInvoiceVos()))
            invoiceDocService.batchSave(invoiceDocs);

        messageService.saveMessage(docManage.getId(),messageType,message,NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());
    }

    /**
     * 批量提交
     * */
    @Deprecated
    public void batchSub(String ids,String status, ICurrentUser user) {
        String message = NisMessageConstant.NIS_LOG_BATCH_SUBMIT_SUCC;
        List<String> DocIds = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        List<DocManage> docManages = this.queryDocManageByids(DocIds,null);
        for (DocManage docManage : docManages) {
            docManage.setStatus(AccountReceivableConstant.STATUS_WAIT_CHECK);
            docManage.setUpdateBy(user.getId());
            docManage.setTenantId(user.getTenantId());
            docManage.setWriteOffStatus(AccountReceivableConstant.WRITEOFFSTATUS_SAVE);
            docManage.setStatus(status);
            this.updateEntity(docManage);
            //批量提交
            if (AccountReceivableConstant.STATUS_WAIT_CHECK.equals(status)) {
                getStartHandler().handler(AccountReceivableConstant.DOC_MANAGE_APPROVE, docManage.getId(), docManage, docManage.getCorpId(), user.getId());
            }
            messageService.saveMessage(docManage.getId(),NisMessageConstant.MESSAGE_TYPE_SUBMIT,message,
                    NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());
        }

    }

    public DocManageVo queryDocManageById(String id) {
        DocManageVo docManageVos = docManageDao.queryDocManageById(id);
        List<InvoiceDocVo> invoiceDocVos = invoiceDocService.queryInvoiceDocbYDocId(id);
        docManageVos.setInvoiceVos(invoiceDocVos);
        return docManageVos;
    }

    /**
     * 删除应收单,逻辑删除
     */
    public void deleteDocManage(List<String> ids) {
        ICurrentUser user = getCurrentUser();
        String condition = "STATUS in (-1,0)";
        List<DocManage> docManages = queryDocManageByids(ids, condition);
        docManages.stream().forEach(docManage -> {docManage.setStatus(AccountReceivableConstant.STATUS_DELETE);});
        this.batchUpdate(docManages);
        for (String id : ids) {
            messageService.saveMessage(id,NisMessageConstant.MESSAGE_TYPE_DELETE,NisMessageConstant.NIS_LOG_DELETE_SUCC,
                    NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());
        }
    }

    public List<DocManage> queryDocManageByids(List<String> ids, String condition){
        String idIn = SqlUtil.getInSql(ids, "id", true);
        List<DocManageVo> docManageVos = docManageDao.queryDocManageByIds(idIn, condition);
        docManageVos.stream().peek(DocManageVo-> DocManageVo.setStatus(AccountReceivableConstant.STATUS_DELETE.toString())).forEach(DocManageVo->{});
        return BeanUtils.copyListProperties(docManageVos, DocManage.class);
    }

    public DataTablesOutput listDocManage(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_QUERY, user.getId());
        String corpIdInSql = SqlUtil.getInSql(corpIds, "dm.corp_id", true);
        return docManageDao.listDocManage(input, corpIdInSql);
    }


    public DataTablesOutput listDocManageApprove(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(AccountReceivableConstant.DOC_MANAGE_APPROVE, input);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "dm.id", true);
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.SYS_FLOW_APPROVE_DOC_MANAGE, user.getId());
        String corpIdInSql = SqlUtil.getInSql(corpIds, "dm.corp_id", true);
        DataTablesOutput output = docManageDao.listDocManageApprove(input, corpIdInSql,businessSql);
        return workFlowBusiness.merge(output);
    }

    /**
     * 作废
     * @param reasonTitle 原因标题
     * @param reasonContent 原因内容
     * */
    public void changeOrInvalidated(String reasonTitle, String reasonContent, String id,String status) {
        String reasonTitleName = docManageDao.queryDictTypeNameByGrepAndTypeCode("account.receivable.doc.reasonTitle",reasonTitle);
        String message = String.format("应收单作废，调整原因：[%s]，说明：[%s]", reasonTitleName, reasonContent);
        DocManage docManage = docManageDao.get(id, true);
        ICurrentUser user = getCurrentUser();
        docManage.setReasonTitle(reasonTitle);
        docManage.setReasonContent(reasonContent);
        docManage.setReasonStatus(status);
        docManage.setUpdateBy(user.getId());
        docManage.setStatus(AccountReceivableConstant.STATUS_WAIT_CHECK);
        docManage.setReasonStatus(status);
        //作废
        if (AccountReceivableConstant.STATUS_WAIT_CHANGE_REFUSE.equals(status)) {
            getStartHandler().handler(AccountReceivableConstant.DOC_MANAGE_APPROVE, docManage.getId(), docManage, docManage.getCorpId(), user.getId());
        }
        this.merge(docManage);
        messageService.saveMessage(docManage.getId(),NisMessageConstant.MESSAGE_TYPE_SUBMIT,message,NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());

    }

    /**用于页面查询发票信息*/
    public  List<InvoiceDocVo> queryeditInvoiceDocById(String id){
        return invoiceDocService.queryInvoiceDocbYDocId(id);

    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<DocManageVo> list, ICurrentUser user) {
        String message = String.format("应收单导入，导入人：[%s]",user.getUsername());
        List<DocManage> manages = new ArrayList<>();
        for (DocManageVo docManageVo : list) {
            DocManage docManage = new DocManage();
            BeanUtils.copyNotNullProperties(docManageVo, docManage);
            docManage.setCode(this.getDocManageCode());
            docManage.setWriteOffStatus(WriteoffConstant.WRITE_OFF_STATUS_0);
            docManage.setTenantId(user.getTenantId());
            docManage.setCreateBy(user.getId());
            docManage.setStatus(AccountReceivableConstant.STATUS_TEMP);
            manages.add(docManage);
        }
        batchSave(manages);
        for (DocManage manage : manages) {
            messageService.saveMessage(manage.getId(),NisMessageConstant.MESSAGE_TYPE_SUBMIT,message,NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());
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
        if (StringUtils.isNotEmpty(corpId)
                && fbsBudgetProcessService.corpCtlBudget(corpId)
                && isBudget(briefId1, briefId2)) {
            return true;
        }
        return false;
    }

    /**
     * 是否挂机预算系统
     *
     * @param briefId1 业务是否挂预算
     * @param briefId2 业务挂预算科目
     * @return
     */
    public boolean isBudget(String briefId1, String briefId2) {
        ConfigDTO config = configService.getConfig(BudgetConstant.BUSINESS_NAME_BASIC, briefId1);
        if (config != null && CommonConstant.YES.equals(config.getValue())) {//budgetSel
            ConfigDTO config2 = configService.getConfig(BudgetConstant.BUSINESS_NAME_BASIC, briefId2);
            if (!ObjectUtils.isEmpty(config2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建计划执行
     *
     * @param
     * @return
     */
    private void bulidFbsBudgetProcessDTO(DocManageVo manage,Integer status, String DocManageId, ICurrentUser user) {
        List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
        for (int i = 0; i < manage.getItemCode().length; i++) {
            FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
            fbsBudgetProcessDTO.setItemCode(manage.getItemCode()[i]);//科目代码
            fbsBudgetProcessDTO.setItemName(manage.getItemName()[i]);//科目名称
            fbsBudgetProcessDTO.setProcessMoney(manage.getProcessMoney()[i]);//执行金额
            fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
            fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
            fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
            fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
            fbsBudgetProcessDTO.setStatus(status);
            fbsBudgetProcessDTO.setBillNo(manage.getCode());
            fbsBudgetProcessDTO.setBillId(DocManageId);
            fbsBudgetProcessDTO.setProcessDate(manage.getReceiptDate());
            fbsBudgetProcessDTO.setCorpId(manage.getCorpId());
            fbsBudgetProcessDTO.setCurId(manage.getCurId());
            fbsBudgetProcessDTO.setPayerAcc(manage.getPayerAccNo());
            fbsBudgetProcessDTO.setPayeeAcc(manage.getPayeeAcc());
            fbsBudgetProcessDTO.setPayeeAccName(manage.getPayeeAccName());
            fbsBudgetProcessDTO.setPayeeBank(manage.getPayeeBankName());
            fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_BUDGETSEL);
            fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.BUSINESS_NAME_BASIC);
            fbsBudgetProcessDTO.setTenantId(user.getTenantId());
            list.add(fbsBudgetProcessDTO);
        }
        // 修改时，先删除之前的计划执行数据再保存
        this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_BUDGETSEL, BudgetConstant.BUSINESS_NAME_BASIC, manage.getCode());
        this.fbsBudgetProcessService.writeBudgetProcess(list, user);
    }


    /**
     * 导入校验方法
     *
     * @return
     */
    @Override
    public ExcelVerifyHanlderResult verifyHandler(DocManageVo manageVo, Object... objects) {
        return null;
    }

    /**
     * 导入校验方法
     *
     * @return
     */
    @Override
    public void verifyHandler(List<DocManageVo> list, Object... objects) {
        if (!EmptyUtils.isEmptyList(list)) {
            for (DocManageVo vo : list) {
                List<String> msgList = assertImportDocManage(vo);
                // 判断单位在系统中是否存在
                List<String> corpIds = this.corpService.getCorpIdsByUserResource(ResourceConstant.ACCOUNT_RECEIVABLE_DOC_MANAGE_QUERY, getCurrentUser().getId());
                CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
                if (!EmptyUtils.isEmpty(corp) && corpIds.contains(corp.getId())){
                    vo.setCorpId(corp.getId());
                }else {
                    msgList.add("单位名称有误或者该用户菜单下没有所导入单位的管理权限");
                }

                if (EmptyUtils.isEmpty(vo.getBatchBusTypeName())){
                    msgList.add("doc.manage.message.batchBusTypeName.not.null");
                }else if(EmptyUtils.isNotEmpty(vo.getCorpId())){
                    vo.setBatchBusType(docManageDao.getByBusTypeByName(vo.getCorpId(), "0", vo.getBatchBusTypeName(), getCurrentUser().getTenantId()));
                    if (EmptyUtils.isEmpty(vo.getBatchBusType())){
                        msgList.add("doc.manage.message.batchBusTypeName.not.exist");
                    }
                }
                // 按客商名称匹配，编号由客商资料自动回填。
                validateImportedExternalCorp(vo, corp, msgList);

                //校验币别
                String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                if (EmptyUtils.isBlank(curId))
                    msgList.add("nis.netbank.message.cur.null");
                vo.setCurId(curId);
                vo.addErrorMsg(msgList);
                vo.setSource("1");
            }

        }
    }

    void validateImportedExternalCorp(DocManageVo vo, CorpDTO corp, List<String> msgList) {
        String name = StringUtils.trimToEmpty(vo.getExternalName());
        vo.setExternalName(name);
        if (StringUtils.isBlank(name)) {
            msgList.add("sys.externalCorp.message.name.null");
            return;
        }
        List<ExternalCorpDTO> candidates = externalCorpService.findExternalCorpLikeName(name);
        List<ExternalCorpDTO> matches = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(candidates)) {
            for (ExternalCorpDTO candidate : candidates) {
                if (name.equals(candidate.getName())) {
                    matches.add(candidate);
                }
            }
        }
        if (matches.isEmpty()) {
            msgList.add("doc.manage.message.external.not.exist");
            return;
        }
        if (matches.size() > 1) {
            msgList.add("客商名称在系统中存在多条记录，无法确定客商");
            return;
        }
        ExternalCorpDTO externalCorp = matches.get(0);
        if (StringUtils.isBlank(externalCorp.getCode())) {
            msgList.add("该客商未维护客商编号，请先完善客商资料");
            return;
        }
        if (EmptyUtils.isEmpty(corp)) {
            return;
        }
        if (externalCorpService.queryExternalCorpAndCorpId(externalCorp.getId(), corp.getId())) {
            msgList.add("当前导入客商未共享给所导入单位");
            return;
        }
        vo.setExternalCode(externalCorp.getCode());
        vo.setExternalName(externalCorp.getName());
        vo.setExternalId(externalCorp.getId());
        vo.setExternalType(externalCorp.getExternalType());
    }

    private List<String> assertImportDocManage(DocManageVo vo) {
        List<String> msgList = new ArrayList<>();
        if (EmptyUtils.isEmpty(vo.getCorpName()))
            msgList.add("单位不能为空");
        if (EmptyUtils.isEmpty(vo.getVoucherTypeName())){
            msgList.add("doc.manage.message.voucherTypeName.not.null");
        }else {
            vo.setVoucherType(dictService.getCodeByName("account.receivable.doc.voucherType", vo.getVoucherTypeName()));
            if (EmptyUtils.isEmpty(vo.getVoucherType())){
                msgList.add("doc.manage.message.voucherTypeName.not.exist");
            }
        }
        if (EmptyUtils.isEmpty(vo.getAgingDate()))
            msgList.add("账龄起算日不能为空或者格式错误");
        if (EmptyUtils.isEmpty(vo.getReceiptDate()))
            msgList.add("单据日期不能为空或者格式错误");
        if (EmptyUtils.isEmpty(vo.getEndDate()))
            msgList.add("到期日期不能为空或者格式错误");
        if (EmptyUtils.isEmpty(vo.getReceivableMoney()))
            msgList.add("应收金额格式错误或或者金额为空");
        else if(!Pattern.matches(AccountReceivableConstant.DOC_BIGDECIMAL_REGEX,vo.getReceivableMoney().toString()))
            msgList.add("应收金额金额超出范围：正确格式【12位正整数，2位小数】");
        //单据日期>到期日期>账龄起算日
        if (!EmptyUtils.isEmpty(vo.getReceiptDate())&&!EmptyUtils.isEmpty(vo.getAgingDate()) && vo.getReceiptDate().compareTo(vo.getAgingDate()) > 0)
            msgList.add("doc.manage.message.receiptDate.mismatch");
        if (!EmptyUtils.isEmpty(vo.getEndDate())&&!EmptyUtils.isEmpty(vo.getAgingDate()) && vo.getEndDate().compareTo(vo.getAgingDate()) < 0)
            msgList.add("doc.manage.message.agingDate.mismatch");
        //应收金额 = 余额
        if (!EmptyUtils.isEmpty(vo.getReceivableMoney()) && vo.getReceivableMoney().compareTo(BigDecimal.ZERO) <= 0)
            msgList.add("doc.manage.message.receivableMoney.gt.zero");
        return msgList;

    }


    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        boolean isPass = workFlowResult.isPass();
        String message = isPass ? NisMessageConstant.NIS_LOG_APP_SUCC : NisMessageConstant.NIS_LOG_APP_BACK;
        Integer messageType = isPass ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;

        String businessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        DocManage docManage = this.get(businessKey, true);
        if (isPass){
            if (AccountReceivableConstant.STATUS_WAIT_CHECK.equals(docManage.getStatus())
                || AccountReceivableConstant.STATUS__WAIT_INVALIDATED.equals(docManage.getReasonStatus())) {
                //计划执行状态同步更新
                if (this.isBudget(docManage.getCorpId(), BudgetConstant.BUSINESS_NAME_BUDGETSEL, BudgetConstant.BUSINESS_NAME_DEFAULT_BUDGETSEL)) {
                    fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_BUDGETSEL, BudgetConstant.BUSINESS_NAME_BASIC,
                            docManage.getCode(), NetbankConstant.STATUE_CHECK, user);
                }
            }
            //审批
            if (docManage.getStatus().equals(AccountReceivableConstant.STATUS_WAIT_CHECK))
                docManage.setStatus(AccountReceivableConstant.STATUS_CHECK);
            if (StringUtils.isNotEmpty(docManage.getReasonStatus())) {
                //变更审批-变更审批通过和打回都是审批通过
                if (docManage.getReasonStatus().equals(AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString()))
                    docManage.setReasonStatus(AccountReceivableConstant.STATUS_CHECK.toString());
                //作废审批
                if (docManage.getReasonStatus().equals(AccountReceivableConstant.STATUS_WAIT_CHANGE_REFUSE.toString())){
                    docManage.setReasonStatus(AccountReceivableConstant.STATUS_CHANGE_REFUSE.toString());
                    docManage.setStatus(AccountReceivableConstant.STATUS_CHANGE_REFUSE);
                }
            }
        }else {
            //审批打回
            if (docManage.getStatus().equals(AccountReceivableConstant.STATUS_WAIT_CHECK))
                docManage.setStatus(AccountReceivableConstant.STATUS_BACK);
            //计划执行状态同步更新
            if (this.isBudget(docManage.getCorpId(),
                    BudgetConstant.BUSINESS_NAME_BUDGETSEL,
                    BudgetConstant.BUSINESS_NAME_DEFAULT_BUDGETSEL)) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_BUDGETSEL, BudgetConstant.BUSINESS_NAME_BASIC,
                        docManage.getCode(), NetbankConstant.STATUE_BACK, user);
            }
            if (StringUtils.isNotEmpty(docManage.getReasonStatus())) {
                //变更审批打回
                if (docManage.getReasonStatus().equals(AccountReceivableConstant.STATUS__WAIT_INVALIDATED.toString()))
                    docManage.setReasonStatus(AccountReceivableConstant.STATUS_BACK_CHANGE_REFUSE);
                //作废审批打回
                if (docManage.getReasonStatus().equals(AccountReceivableConstant.STATUS_WAIT_CHANGE_REFUSE.toString()))
                    docManage.setReasonStatus(AccountReceivableConstant.STATUS_BACK_CHANGE_REFUSE);
            }
        }
        this.merge(docManage);
        messageService.saveMessage(docManage.getId(),messageType,message,NisMessageConstant.BUS_TYPE_DOCMANAGE,user.getLoginName(),user.getUsername());
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    /**
     * 应收查询 列表查询
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listQueryDocManage(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.ACCOUNT_RECEIVABLE_QUERYDOCMANAGE_QUERY, user.getId());
        String corpIdInSql = SqlUtil.getInSql(corpIds, "dm.corp_id", true);
        return docManageDao.listQueryDocManage(input, corpIdInSql);
    }

    /**
     * 根据id获取应收数据
     * @param id
     * @return
     */
    public DocManageVo getDocManageDetailById(String id){
        DocManageVo docManageVo = docManageDao.getDocManageDetailById(id);
        //获取发票信息
        List<InvoiceDocVo> invoiceDocVos = invoiceDocService.queryInvoiceDocbYDocId(id);
        docManageVo.setInvoiceVos(invoiceDocVos);
        return docManageVo;
    }

    private boolean isInvoiceDocVo(InvoiceDocVo vo){
        if (StringUtils.isEmpty(vo.getCode())&&StringUtils.isEmpty(vo.getCheckCode())&&
                StringUtils.isEmpty(vo.getBillId())&&StringUtils.isEmpty(vo.getBillNo())&&
                StringUtils.isEmpty(vo.getNumber())&& EmptyUtils.isEmpty(vo.getParAmt())&&
                EmptyUtils.isEmpty(vo.getTaxincAmt())&&
                StringUtils.isEmpty(vo.getType()) &&EmptyUtils.isEmpty(vo.getDraDate())){
            return true;
        }
        return false;

    }
}
