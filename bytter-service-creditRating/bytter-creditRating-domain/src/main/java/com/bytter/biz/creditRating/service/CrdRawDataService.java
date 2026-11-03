package com.bytter.biz.creditRating.service;


import com.bytter.biz.creditRating.api.constant.CreWorkFlowConstant;
import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrdRawDataDao;
import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.entity.CrdDataSourceCategory;
import com.bytter.biz.creditRating.entity.CrdRawData;
import com.bytter.biz.creditRating.enums.CreditRatingEnum;
import com.bytter.biz.creditRating.util.EmailUtils;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WorkFlow(CreWorkFlowConstant.CREDIT_INSTITUTION_ADD)
@Service(value = "rawDataService")
@Transactional
public class CrdRawDataService extends BaseService<CrdRawData, String> implements IWorkFlowListener, ITaskService {

    @Autowired
    private CrdRawDataDao crdRawDataDao;

    @Autowired
    private CrdDataSourceCategoryService dataSourceCategoryService;

    @DubboReference(check = false)
    private IExternalCorpService iExternalCorpService;

    @Autowired
    private CrInstitutionalRawDataService institutionalRawDataService;

    @Autowired
    private CrdDataSourceCategoryService crdDataSourceCategoryService;

    @DubboReference(check = false)
    private IBtInputBankInfoService btInputBankInfoService;

    @Autowired
    private CrOwnershipStructureDataService ownershipStructureDataService;

    @Autowired
    private CrContactAddressBookService contactAddressBookService;

    @Autowired
    private CrVisitRecordRegistrationService visitRecordRegistrationService;

    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;

    @DubboReference(check = false)
    private IRegionService regionService;

    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @Autowired
    private IWorkFlowService workFlowService;

    @Autowired
    private EvaluationLogService logService;

    @Transactional(readOnly = true)
    public DataTablesOutput dataCollection(ICurrentUser user) {
        List<CrdDataSourceCategoryVo> crdDataSourceCategoryVos = dataSourceCategoryService.dataTable();
        Map<String, CrdDataSourceCategoryVo> dataSCMap = crdDataSourceCategoryVos.stream().collect(Collectors.toMap(CrdDataSourceCategoryVo::getCode, dto -> dto));
        List<ExternalCorpDTO> externalCorpDTOS = iExternalCorpService.queryAll();

        if (externalCorpDTOS.size() < 0) {
            return new DataTablesOutput();
        }
        List<CrdRawData> crdRawDataList = Lists.newArrayList();

        externalCorpDTOS.stream().forEach(dto -> {
            CrdRawData crdRawData = new CrdRawData();
            crdRawData.setDataId(dto.getId());
            crdRawData.setDataName(dto.getName());
            crdRawData.setDataId(dto.getId());
            CrdDataSourceCategoryVo categoryVo = dataSCMap.get(CreditRatingConstant.DATA_SOURCE_CATEGORY_CODE_D001);
            crdRawData.setDataSourceCategoryCode(categoryVo.getCode());
            crdRawData.setDataSourceCategoryName(categoryVo.getName());
            crdRawData.setDataSourceId(categoryVo.getId());
            crdRawData.setStatus(CreditRatingConstant.APPROVED);
            crdRawData.setCreateDate(new Date());
            crdRawData.setCreator(user.getLoginName());
            crdRawDataList.add(crdRawData);
        });
        if (crdRawDataList.size() < 0) {
            return new DataTablesOutput();
        }
        crdRawDataDao.batchSave(crdRawDataList);
        return new DataTablesOutput();
    }

    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(String ids) {
        List<String> idList = Lists.newArrayList();
        if (StringUtils.isNotBlank(ids)) {
            idList = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        }
        return crdRawDataDao.queryAll(idList);
    }

    @Transactional(readOnly = true)
    public List<CrdRawDataVo> queryByIds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return crdRawDataDao.queryByIds(ids);
    }

    public void modify(CrdRawDataVo dataVo, ICurrentUser user) {
        String id = dataVo.getId();
        CrdRawData crdRawData = crdRawDataDao.get(id, true);
        String dataSourceId = crdRawData.getDataSourceId();
        CrdDataSourceCategory category = dataSourceCategoryService.get(dataSourceId, true);
        crdRawData.setDataSourceCategoryName(category.getDataSourceCategoryName());
        crdRawData.setDataSourceCategoryCode(category.getDataSourceCategoryCode());
        crdRawData.setDataName(dataVo.getDataName());
        crdRawData.setEstablishmentDate(dataVo.getEstablishmentDate());
        crdRawData.setRegisteredCapital(dataVo.getRegisteredCapital());
        crdRawData.setLegalRepresentative(dataVo.getLegalRepresentative());
        crdRawData.setBusinessScope(dataVo.getBusinessScope());
        crdRawDataDao.update(crdRawData);
    }


    public boolean deleteById(String id) {
        CrdRawData crdRawData = crdRawDataDao.get(id, true);
        crdRawData.setStatus(CreditRatingConstant.DELETE);
        crdRawDataDao.update(crdRawData);
        return true;
    }

    @Transactional(readOnly = true)
    public InstitutionDetailsVo viewInstitution(String id) {
        CrdRawData crdRawData = crdRawDataDao.get(id, true);
        InstitutionDetailsVo institutionDetailsVo = institutionalRawDataService.queryByRawDataId(id);
        BeanUtils.copyNotNullProperties(crdRawData, institutionDetailsVo);
        injectValue(institutionDetailsVo);
        return institutionDetailsVo;
    }

    private void injectValue(InstitutionDetailsVo vo) {
        if (StringUtils.isNotBlank(vo.getPlace())) {
            String placeCode = vo.getPlace();
            List<SysCountryCustDTO> sysCountryCustDTOS = sysCountryCustService.listDtoById(placeCode);
            if (null != sysCountryCustDTOS && sysCountryCustDTOS.size() > 0) {
                Map<String, String> collect = sysCountryCustDTOS.stream().collect(Collectors.toMap(SysCountryCustDTO::getId, SysCountryCustDTO::getNameZh));
                vo.setPlace(collect.get(placeCode));
            }
        }
        String provId = vo.getProv();
        String cityId = vo.getCity();
        if (StringUtils.isNotBlank(provId) || StringUtils.isNotBlank(cityId)) {
            List<String> regionId = Lists.newArrayList();
            regionId.add(provId);
            regionId.add(cityId);
            List<RegionDTO> regionDTOS = regionService.findByIds(regionId);
            if (null != regionDTOS && regionDTOS.size() > 0) {
                Map<String, String> regionMap = regionDTOS.stream().collect(Collectors.toMap(RegionDTO::getId, RegionDTO::getName));
                vo.setProv(regionMap.get(provId));
                vo.setCity(regionMap.get(cityId));
            }
        }
        if (vo.getStartBusinessPeriod() != null && vo.getEndBusinessPeriod() != null){
            vo.setTilde("~");
        }

    }

    @Transactional(readOnly = true)
    public DataTablesOutput institutionDatatable(DataTablesInput input, ICurrentUser user) {
        return crdRawDataDao.institutionDatatable(input);
    }

    public void  registerInstitution(InstitutionDetailsVo vo, String opType, ICurrentUser user) {
        if (StringUtils.isNotBlank(vo.getId())) {
            return;
        }
        verifyForLength(vo);
        addInstitution(vo, opType, user);
    }

    private void verifyForLength(InstitutionDetailsVo vo) {
        if (StringUtils.isNotBlank(vo.getInstitutionName()) && vo.getInstitutionName().length() > 200 ){
            throw new BusinessException(CreditRatingEnum.CREDIT_INSTITUTION_NAME_TOO_LONG);
        }
        if (StringUtils.isNotBlank(vo.getFinancialLicenseInstCode()) && vo.getFinancialLicenseInstCode().length()>200){
            throw new BusinessException(CreditRatingEnum.CREDIT_FINANCIALLICENSEINST_CODE_TOO_LONG);
        }
        if (StringUtils.isNotBlank(vo.getLegalRepresentative()) && vo.getLegalRepresentative().length()>200){
            throw new BusinessException(CreditRatingEnum.CREDIT_LEGALREPRESENTATIVE_TOO_LONG);
        }
        if (StringUtils.isNotBlank(vo.getEmail()) && vo.getEmail().length()>200){
            throw new BusinessException(CreditRatingEnum.CREDIT_EMAIL_TOO_LONG);
        }
//        unifiedSocialCreditCode
        if (StringUtils.isNotBlank(vo.getUnifiedSocialCreditCode()) && vo.getUnifiedSocialCreditCode().length()>100){
            throw new BusinessException(CreditRatingEnum.CREDIT_UNIFIEDSOCIALCREDITCODE_TOO_LONG);
        }
        if (vo.getEmployeeCount() != null && (vo.getEmployeeCount().compareTo(BigInteger.ZERO) > 0
                || vo.getEmployeeCount().compareTo(BigInteger.ZERO) == 0 ) && String.valueOf(vo.getEmployeeCount()).length()> 8 ){
            throw new BusinessException(CreditRatingEnum.CREDIT_EMPLOYEE_COUNT_TOO_LONG);
        }

    }
    public void modifyInstitution(InstitutionDetailsVo vo, String opType, ICurrentUser user) {
        if (StringUtils.isBlank(vo.getId())) {
            return;
        }
        verifyForLength(vo);
        updateInstitution(vo, opType, user);
    }

    private void setCrInstitutionalRawData(CrInstitutionalRawData insRawData, InstitutionDetailsVo vo, String opType, ICurrentUser user) {
        BeanUtils.copyNotNullProperties(vo, insRawData, "id");

        //总行
        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(vo.getInstitutionType())) {
            insRawData.setInstitutionNameCode(vo.getInstitutionName());
            List<BankTypeDTO> byBankType = bankTypeService.getByBankType(vo.getInstitutionName());
//            insRawData.setInstitutionCode(vo.getInstitutionName());
            if (null != byBankType && !byBankType.isEmpty()) {
                insRawData.setInstitutionName(byBankType.get(0).getTypeName() != null ? byBankType.get(0).getTypeName() : null);
            }

        }

        if (CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(vo.getInstitutionType())) {
            InputBankInfoDTO inputBankInfoDTO = btInputBankInfoService.getByBankCodeOrBankName(null, vo.getInstitutionName());
            insRawData.setInstitutionNameCode(vo.getBankCode());
//            insRawData.setInstitutionCode(vo.getInstitutionName());
            if (null != inputBankInfoDTO) {
                insRawData.setInstitutionName(inputBankInfoDTO.getBankName() != null ? inputBankInfoDTO.getBankName() : null);
            }
        }

        insRawData.setBankTypeCode(vo.getBankType());

        if (CreditRatingConstant.OPERATION_TYPE_ADD_TEMP.equals(opType) || CreditRatingConstant.OPERATION_TYPE_MODIFY_TEMP.equals(opType)) {
            insRawData.setStatus(CreditRatingConstant.INSTITUTION_DATA_STATUS_NOT_ADMITTED);
        }
        if (CreditRatingConstant.OPERATION_TYPE_ADD_SUBMIT.equals(opType) || CreditRatingConstant.OPERATION_TYPE_MODIFY_SUBMIT.equals(opType)) {
            insRawData.setStatus(CreditRatingConstant.INSTITUTION_DATA_STATUS_PENDING_APPROVAL);
        }
        insRawData.setCreator(user.getLoginName());
        insRawData.setCreateDate(new Date());
    }

    private void addInstitution(InstitutionDetailsVo vo, String opType, ICurrentUser user) {
        checkEmail(vo.getEmail());
        CrdRawData crdRawData = new CrdRawData();
        setCrdRawDataVules(vo, crdRawData, opType, user);
        this.saveEntity(crdRawData);

        CrInstitutionalRawData insRawData = new CrInstitutionalRawData();
        insRawData.setRawDataId(crdRawData.getId());
        setCrInstitutionalRawData(insRawData, vo, opType, user);
        insRawData.setInstitutionCode(SequenceSingleton.getSequence(SequenceEnum.INSTITUTION_CODE));
        institutionalRawDataService.saveEntity(insRawData);
        String institutionalRawDataId = insRawData.getId();

        //股权结构数据 ownership_structure_data
        String ssIds = vo.getSsIds();
        if (StringUtils.isNotBlank(ssIds)) {
            List<String> dbIds = ownershipStructureDataService.findByIds(Arrays.stream(ssIds.split(",")).collect(Collectors.toList())).stream().map(CrOwnershipStructureDataVo::getId).collect(Collectors.toList());
            ownershipStructureDataService.updateInstitutionalRawDataIdByIds(dbIds, institutionalRawDataId);
        }

        // 联系人通讯录	Contact_Address_Book
        String bookIds = vo.getBookIds();
        if (StringUtils.isNotBlank(bookIds)) {
            contactAddressBookService.updateInstitutionalRawDataIdByIds(Arrays.asList(bookIds.split(",")), institutionalRawDataId);

        }

        //拜访记录登记	Visit_record_registration
        String visitIds = vo.getVisitIds();
        if (StringUtils.isNotBlank(visitIds)) {
            visitRecordRegistrationService.updateInstitutionalRawDataIdByIds(Arrays.asList(visitIds.split(",")), institutionalRawDataId);
        }


        if (CreditRatingConstant.OPERATION_TYPE_ADD_TEMP.equals(opType)) {
//            logService.saveEvaluationLog(crdRawData.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_CODE
//                    , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.ADD_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION
//                    , user);
            logService.saveEvaluationLog(crdRawData.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_CODE
                    , CreditRatingConstant.EVALUATION_LOG_LANG_ORGADDTEMP, user);
        }
        if (CreditRatingConstant.OPERATION_TYPE_ADD_SUBMIT.equals(opType)) {
            //提交 启动审批流程
            getStartHandler().handler(CreWorkFlowConstant.CREDIT_INSTITUTION_ADD, crdRawData.getId(), crdRawData, user.getCorpId(), user.getId());
//            logService.saveEvaluationLog(crdRawData.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_CODE
//                    , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.ADD_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION, user);
            logService.saveEvaluationLog(crdRawData.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_CODE
                    , CreditRatingConstant.EVALUATION_LOG_LANG_ORGADDSUBMIT, user);
        }

    }

    private void checkEmail(String email){
        if (StringUtils.isNotBlank(email)){
            if(!EmailUtils.isEmail(email)){
                throw new BusinessException(CreditRatingEnum.CREDIT_FORM_VERIFY_EMAIL_ERROR);
            }
        }

    }
    private void updateInstitution(InstitutionDetailsVo vo, String opType, ICurrentUser user) {

        checkEmail(vo.getEmail());

        CrdRawData crdRawDataDb = this.get(vo.getId());

        CrInstitutionalRawData insRawData = new CrInstitutionalRawData();
        CrInstitutionalRawDataVo institutionalRawDataVo = institutionalRawDataService.getByRawDataId(vo.getId());
        BeanUtils.copyNotNullProperties(institutionalRawDataVo, insRawData);

        setCrdRawDataVules(vo, crdRawDataDb, opType, user);
        this.updateEntity(crdRawDataDb);

        insRawData.setRawDataId(crdRawDataDb.getId());
        setCrInstitutionalRawData(insRawData, vo, opType, user);
        institutionalRawDataService.updateEntity(insRawData);

        String institutionalRawDataId = insRawData.getId();

        //股权结构数据 ownership_structure_data
        String ssIds = vo.getSsIds();
        if (StringUtils.isNotBlank(ssIds)) {
            ownershipStructureDataService.updateInstitutionalRawDataIdByIds(Arrays.asList(ssIds.split(",")), institutionalRawDataId);
        }

        // 联系人通讯录	Contact_Address_Book
        String bookIds = vo.getBookIds();
        if (StringUtils.isNotBlank(bookIds)) {
            contactAddressBookService.updateInstitutionalRawDataIdByIds(Arrays.asList(bookIds.split(",")), institutionalRawDataId);

        }

        //拜访记录登记	Visit_record_registration
        String visitIds = vo.getVisitIds();
        if (StringUtils.isNotBlank(visitIds)) {
            visitRecordRegistrationService.updateInstitutionalRawDataIdByIds(Arrays.asList(visitIds.split(",")), institutionalRawDataId);
        }


        if (CreditRatingConstant.OPERATION_TYPE_MODIFY_TEMP.equals(opType)) {
//            logService.saveEvaluationLog(crdRawDataDb.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_CODE
//                    , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.UPDATE_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_DESCRIPTION
//                    , user);
            logService.saveEvaluationLog(crdRawDataDb.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_TEMP_CODE
                    , CreditRatingConstant.EVALUATION_LOG_LANG_ORGEDITTEMP, user);
        }
        if (CreditRatingConstant.OPERATION_TYPE_MODIFY_SUBMIT.equals(opType)) {
            //提交 启动审批流程
            getStartHandler().handler(CreWorkFlowConstant.CREDIT_INSTITUTION_ADD, crdRawDataDb.getId(), crdRawDataDb, user.getCorpId(), user.getId());
//            logService.saveEvaluationLog(crdRawDataDb.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_MODIFY_CODE
//                    , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.UPDATE_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_MODIFY_DESCRIPTION
//                    , user);
            logService.saveEvaluationLog(crdRawDataDb.getId(), CreditRatingConstant.EVALUATION_LOG_TYPE_MODIFY_CODE
                    , CreditRatingConstant.EVALUATION_LOG_LANG_ORGEDITSUBMIT, user);
        }
    }

    private void setCrdRawDataVules(InstitutionDetailsVo vo, CrdRawData crdRawData, String type, ICurrentUser user) {
        List<CrdDataSourceCategoryVo> crdDataSourceCategoryVos = crdDataSourceCategoryService.dataTable();
        Map<String, CrdDataSourceCategoryVo> collect = crdDataSourceCategoryVos.stream().collect(Collectors.toMap(CrdDataSourceCategoryVo::getCode, dto -> dto));
        CrdDataSourceCategoryVo crdDataSourceCategoryVo = collect.get(CreditRatingConstant.DATA_SOURCE_CATEGORY_CODE_D003);
        crdRawData.setDataName(vo.getDataName());
        crdRawData.setDataCode(vo.getDataCode());
        crdRawData.setEstablishmentDate(vo.getEstablishmentDate());
        crdRawData.setRegisteredCapital(vo.getRegisteredCapital());
        crdRawData.setLegalRepresentative(vo.getLegalRepresentative());
        crdRawData.setBusinessScope(vo.getBusinessScope());
        crdRawData.setDataSourceCategoryCode(crdDataSourceCategoryVo.getCode());
        crdRawData.setDataSourceCategoryName(crdDataSourceCategoryVo.getName());
        crdRawData.setDataSourceId(crdDataSourceCategoryVo.getId());
        if (CreditRatingConstant.OPERATION_TYPE_ADD_SUBMIT.equals(type) || CreditRatingConstant.OPERATION_TYPE_ADD_TEMP.equals(type)) {
            crdRawData.setStatus(CreditRatingConstant.APPROVAL_PROCESS_ADD);
        }

        if (CreditRatingConstant.OPERATION_TYPE_MODIFY_SUBMIT.equals(type) || CreditRatingConstant.OPERATION_TYPE_MODIFY_TEMP.equals(type)) {
            crdRawData.setStatus(CreditRatingConstant.APPROVAL_PROCESS_MODIFY);
        }

        crdRawData.setAttachmentId(vo.getAttachmentId());
        crdRawData.setCreator(user.getLoginName());
        crdRawData.setCreateDate(new Date());
    }


    @Transactional(readOnly = true)
    public InstitutionDetailsVo queryDetailsById(String rawDataId) {
        CrdRawData crdRawData = crdRawDataDao.get(rawDataId, true);
        InstitutionDetailsVo detailsVo = institutionalRawDataService.queryByRawDataId(rawDataId);
        BeanUtils.copyNotNullProperties(crdRawData, detailsVo);
        return detailsVo;
    }

    public Boolean delInstitutionByRawDataId(String id, ICurrentUser user) {
        this.deleteById(id);
        institutionalRawDataService.delInstitutionByRawDataId(id, user);
//        logService.saveEvaluationLog(id, CreditRatingConstant.EVALUATION_LOG_TYPE_DEL_CODE
//                , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.DELETED_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_SUBMIT_DESCRIPTION
//                , user);
        logService.saveEvaluationLog(id, CreditRatingConstant.EVALUATION_LOG_TYPE_DEL_CODE
                , CreditRatingConstant.EVALUATION_LOG_LANG_ORGDELSUBMIT
                , user);
        return true;
    }


    @Transactional(readOnly = true)
    public DataTablesOutput institutionDatatableToApprove(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CreWorkFlowConstant.CREDIT_INSTITUTION_ADD, input);
        // workFlowBusiness.getBusinessIdList()是获取当前操作员可以审批单据的业务ID集合
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput output = crdRawDataDao.institutionDatatableToApprove(input, businessList);
        return workFlowBusiness.merge(output);
    }

    @Override
    public BaseDao<CrdRawData, String> getEntityDao() {
        return crdRawDataDao;
    }


    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

        approvePassEnd(workFlowResult.getBusinessKey(), workFlowResult.isPass(), workFlowResult.getUser());
        this.getSession().flush();
        this.getSession().clear();
    }

    /**
     * 审批通过后处理
     *
     * @param
     * @param result
     */
    public void approvePassEnd(String rawDataId, boolean result, ICurrentUser user) {
        CrdRawData crdRawData = this.get(rawDataId, true);
        Integer status = crdRawData.getStatus();
        String operation = null;
        if (CreditRatingConstant.APPROVAL_PROCESS_ADD == status) {
            operation = CreditRatingConstant.OPERATION_TYPE_ADD_SUBMIT;
        }
        if (CreditRatingConstant.APPROVAL_PROCESS_MODIFY == status) {
            operation = CreditRatingConstant.OPERATION_TYPE_MODIFY_SUBMIT;
        }
        crdRawData.setStatus(CreditRatingConstant.APPROVED);
        //审批打回
        if (!result) {
            operation = CreditRatingConstant.OPERATION_TYPE_REJECTED;
            crdRawData.setStatus(CreditRatingConstant.APPROVAL_REJECTED);
        }
        institutionalRawDataService.saveOrDeleteByRawDataId(rawDataId, operation, user);
        this.updateEntity(crdRawData);
        //审批通过:添加/修改
        if (result) {
            if (CreditRatingConstant.APPROVAL_PROCESS_ADD == status) {
//                logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_CODE
//                        , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.ADD_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_DESCRIPTION
//                        , user);
                logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_CODE
                        , CreditRatingConstant.EVALUATION_LOG_LANG_ORGADDAPPROVAL
                        , user);
            }
            if (CreditRatingConstant.APPROVAL_PROCESS_MODIFY == status) {
//                logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_MODIFY_CODE
//                        , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.UPDATE_DESCRIPTION + CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVED_DESCRIPTION
//                        , user);
                logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_MODIFY_CODE
                        , CreditRatingConstant.EVALUATION_LOG_LANG_ORGEDITAPPROVAL
                        , user);
            }

        }
        //审批不通过
        if (!result) {
//            logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE
//                    , CreditRatingConstant.INSTITUTIONAL_MANAGEMENT + CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_DESCRIPTION, user);
            logService.saveEvaluationLog(rawDataId, CreditRatingConstant.EVALUATION_LOG_TYPE_APPROVAL_RETURNED_CODE
                    , CreditRatingConstant.EVALUATION_LOG_LANG_ORGAPPROVALBACK, user);
        }

    }

    public List<InstitutionDetailsVo> findInstitutionByIds(List<String> rawDataIds) {
        List<InstitutionDetailsVo> detailsVos = institutionalRawDataService.findInstitutionByIds(rawDataIds);
        return detailsVos;
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }
}
