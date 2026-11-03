package com.bytter.biz.creditRating.service;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.dao.CrInstitutionalRawDataDao;
import com.bytter.biz.creditRating.entity.CrInstitutionalRawData;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IDictService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service(value = "crInstitutionalRawDataService")
@Transactional
public class CrInstitutionalRawDataService extends BaseService<CrInstitutionalRawData, String> implements IWorkFlowListener, ITaskService, DictInjectProvider {

    @Autowired
    private CrOwnershipStructureDataService ownershipStructureDataService;

    @Autowired
    private CrContactAddressBookService contactAddressBookService;

    @Autowired
    private CrVisitRecordRegistrationService visitRecordRegistrationService;

    @Autowired
    private CrInstitutionalRawDataDao crInstitutionalRawDataDao;

    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<CrInstitutionalRawData, String> getEntityDao() {
        return crInstitutionalRawDataDao;
    }

    public InstitutionDetailsVo queryByRawDataId(String rawDataId) {
        InstitutionDetailsVo institutionDetailsVo = crInstitutionalRawDataDao.queryByRawDataId(rawDataId);
        if (null == institutionDetailsVo) {
            return institutionDetailsVo;
        }
        if (StringUtils.isNotBlank(institutionDetailsVo.getPlace()) && institutionDetailsVo.getPlace().contains("-")) {
            String[] places = institutionDetailsVo.getPlace().split("-");
            institutionDetailsVo.setProv(places[0]);
            institutionDetailsVo.setCity(places[1]);
        }

        String insId = institutionDetailsVo.getInsId();
        List<CrOwnershipStructureDataVo> voList = ownershipStructureDataService.queryByInstitutionalRawDataId(insId);
        if (null != voList && 0 < voList.size()) {
            StringBuilder ssIds = new StringBuilder();
            voList.stream().forEach(vo -> {
                ssIds.append(vo.getId()).append(",");
            });
            institutionDetailsVo.setSsIds(ssIds.substring(0, ssIds.length() - 1));
        }

        List<CrContactAddressBookVo> bookVos = contactAddressBookService.queryByInstitutionalRawDataId(insId);
        if (null != bookVos && 0 < bookVos.size()) {
            StringBuilder bookIds = new StringBuilder();
            bookVos.stream().forEach(vo -> {
                bookIds.append(vo.getId()).append(",");
            });
            institutionDetailsVo.setBookIds(bookIds.substring(0, bookIds.length() - 1));
        }

        List<CrVisitRecordRegistrationVo> crVisitRecordRegistrationVos = visitRecordRegistrationService.queryByInstitutionalRawDataId(insId);
        if (null != crVisitRecordRegistrationVos && 0 < crVisitRecordRegistrationVos.size()) {
            StringBuilder visitids = new StringBuilder();
            crVisitRecordRegistrationVos.stream().forEach(vo -> {
                visitids.append(vo.getId()).append(",");
            });
            institutionDetailsVo.setVisitIds(visitids.substring(0, visitids.length() - 1));
        }

        String regionalCode = institutionDetailsVo.getRegionalCode();
        if (StringUtils.isNotBlank(regionalCode)) {
            SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(regionalCode);
            institutionDetailsVo.setRegionalCodeName(sysCountryCustDTO.getNameZh());
        }
        return institutionDetailsVo;
    }

    public CrInstitutionalRawDataVo getByRawDataId(String rawDataId) {
        return crInstitutionalRawDataDao.getByRawDataId(rawDataId);
    }

    @Transactional(readOnly = true)
    public List<CrInstitutionalRawDataVo> queryByTypes(String types) {
        List<CrInstitutionalRawDataVo> list = new ArrayList<>();
        List<String> typeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(types)) {
            typeList = Arrays.stream(types.split(",")).collect(Collectors.toList());
            list = crInstitutionalRawDataDao.queryByTypes(typeList);
        }
        return list;
    }


    public void delInstitutionByRawDataId(String rawDataId, ICurrentUser user) {
        InstitutionDetailsVo institutionDetailsVo = crInstitutionalRawDataDao.queryByRawDataId(rawDataId);
        String insId = institutionDetailsVo.getInsId();

        List<CrOwnershipStructureDataVo> voList = ownershipStructureDataService.queryByInstitutionalRawDataId(insId);
        if (null != voList && 0 < voList.size()) {
            List<String> osdIds = voList.stream().map(CrOwnershipStructureDataVo::getId).collect(Collectors.toList());
            ownershipStructureDataService.delete(osdIds);
        }

        List<CrContactAddressBookVo> bookList = contactAddressBookService.queryByInstitutionalRawDataId(insId);
        if (null != bookList && 0 < bookList.size()) {
            List<String> bookIds = bookList.stream().map(CrContactAddressBookVo::getId).collect(Collectors.toList());
            contactAddressBookService.delete(bookIds);
        }

        List<CrVisitRecordRegistrationVo> visitList = visitRecordRegistrationService.queryByInstitutionalRawDataId(insId);
        if (null != visitList && 0 < visitList.size()) {
            List<String> visitIds = visitList.stream().map(CrVisitRecordRegistrationVo::getId).collect(Collectors.toList());
            visitRecordRegistrationService.delete(visitIds);
        }

        CrInstitutionalRawData institutionalRawData = crInstitutionalRawDataDao.get(insId);
        institutionalRawData.setStatus(CreditRatingConstant.INSTITUTION_DATA_STATUS_DEL);
        institutionalRawData.setCreateDate(new Date());
        crInstitutionalRawDataDao.update(institutionalRawData);

    }

    public List<InstitutionDetailsVo> findInstitutionByIds(List<String> ids) {

        return crInstitutionalRawDataDao.findInstitutionByIds(ids);

    }

    public void saveOrDeleteByRawDataId(String rawDataId, String operationType, ICurrentUser user) {
        //删除
        if (CreditRatingConstant.OPERATION_TYPE_DEL.equals(operationType)) {
            delInstitutionByRawDataId(rawDataId, user);
        }
        //保存
        if (CreditRatingConstant.OPERATION_TYPE_ADD_SUBMIT.equals(operationType) || CreditRatingConstant.OPERATION_TYPE_MODIFY_SUBMIT.equals(operationType)) {
//          如果不配做流程设计 则会报 org.hibernate.NonUniqueObjectException；所以需要 flush和clear
            crInstitutionalRawDataDao.flush();
            crInstitutionalRawDataDao.clear();
            CrInstitutionalRawDataVo crInstitutionalRawDataVoDb = crInstitutionalRawDataDao.getByRawDataId(rawDataId);
            if (crInstitutionalRawDataVoDb != null){
                crInstitutionalRawDataDao.updateStatusById(crInstitutionalRawDataVoDb.getId(), CreditRatingConstant.INSTITUTION_DATA_STATUS_PASS);
//                if (updateNum == 0){
//                    throw new BusinessException("更新失败");
//                }
//                CrInstitutionalRawData crInstitutionalRawData = new CrInstitutionalRawData();
//                BeanUtils.copyNotNullProperties(crInstitutionalRawDataVoDb, crInstitutionalRawData);
//                crInstitutionalRawData.setStatus(CreditRatingConstant.INSTITUTION_DATA_STATUS_PASS);
//                crInstitutionalRawData.setBankTypeCode(crInstitutionalRawDataVoDb.getBankType());
//                this.updateEntity(crInstitutionalRawData);
//                this.saveOrUpdate(crInstitutionalRawData);
            }

        }
        if (CreditRatingConstant.OPERATION_TYPE_REJECTED.equals(operationType)) {
            //修改
            CrInstitutionalRawDataVo crInstitutionalRawDataVoDb = crInstitutionalRawDataDao.getByRawDataId(rawDataId);
            if (crInstitutionalRawDataVoDb != null){
                crInstitutionalRawDataDao.updateStatusById(crInstitutionalRawDataVoDb.getId(), CreditRatingConstant.INSTITUTION_DATA_STATUS_REJECTED);
            }
        }

    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void run(String arguments) {

    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
