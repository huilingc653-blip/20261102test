package com.bytter.guarantee.domain.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.*;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.dao.ConCommonDao;
import com.bytter.guarantee.domain.entity.*;
import com.bytter.guarantee.domain.service.*;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.ConInfoBusdtlDTO;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @Title: 合同公共功能业务处理类
 * @Package com.bytter.biz.contract.service
 * @author: zhengg
 * @date: 2019-11-6 下午2:19:43
 * @version V1.0
 */
@DubboService
@Service
@Transactional
public class ConCommonService extends BaseService<T, String> implements IConService, ICorpDisableVerifyService {
    @Autowired
    private ConCommonDao conCommonDao;
    @Autowired
    private ConCorpService conCorpService;
    @Autowired
    private ConInfoService conInfoService;
    @Autowired
    private ConTypeService conTypeService;
    @Autowired
    private ConBusHookService conBusHookService;
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;
    @Autowired
    private ConInfoRecordService conInfoRecordService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<T, String> getEntityDao() {
        return conCommonDao;
    }
    /** 用于业务并发缓存处理 **/
    private static final Map conIdCache = new ConcurrentHashMap();
    public boolean isInCache(String username) {
        if (conIdCache.containsKey(username)) {
            return true;
        } else {
            conIdCache.put(username, username);
            return false;
        }
    }
    public void removeFromCache(String username) {
        conIdCache.remove(username);
    }
    /**
     * 选择合同 获取合同 table列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getConSelectData(DataTablesInput input) {
        String opResource = input.getFormdata().get("opResource") == null ? null
                : input.getFormdata().get("opResource").toString();
        List<String> corpAuthList = null;
        if (StringUtils.isNotEmpty(opResource)) {
            corpAuthList = conCorpService.getUserCorpIds(opResource, input.getCurrentUserId());
        }
        DataTablesOutput<ConInfoVO> tablesOutput = conCommonDao.getConSelectData(input, corpAuthList);
        Optional.ofNullable(tablesOutput.getData()).ifPresent(listData -> {
            Map<String, DictDTO> dictMap = dictService.listMap("CON_SIGN_TYPE");
            //根据corpId查询code todo 找到所有合同保存入口 增加单位code
            Map<String, CorpDTO> corpMap = corpServiceImpl.listCorpDtoByCorpIds(listData.stream().map(ConInfoVO::getCorpId).collect(Collectors.toList()));
            listData.forEach(item -> {
                Optional.ofNullable(item.getSignType())
                        .flatMap(signType -> Optional.ofNullable(dictMap.get(signType)))
                        .ifPresent(dict -> item.setSignTypeName(dict.getName()));
                item.setCorpCode(corpMap.get(item.getCorpId()).getCode());
            });
        });
        return tablesOutput;
    }
    @Override
    @Transactional(readOnly = true)
    public ConInfoDTO getConInfo(String conId, String busId) {
        return getConInfo(new ConInfoVO(conId, busId));
    }
    @Transactional(readOnly = true)
    public ConInfoDTO getConInfo(ConInfoVO params) {
        if (StringUtils.isEmpty(params.getId()) && StringUtils.isEmpty(params.getBusId())
                && StringUtils.isEmpty(params.getRecordId())) {
            return null;
        }
        ConInfo conInfo = null;
        // 先通过合同ID获取合同信息
        if (StringUtils.isNotEmpty(params.getId())) {
            List<ConInfo> conList = conInfoService.getConInfo(new ConInfoVO(params.getId(), null));
            if (conList != null && !conList.isEmpty()) {
                conInfo = conList.get(0);
            }
        }
        // 合同ID为空或者合同ID未取到值再通过业务挂接ID获取合同信息
        if (conInfo == null && StringUtils.isNotEmpty(params.getBusId())) {
            List<ConInfo> conList = conInfoService.getConInfo(new ConInfoVO(null, params.getBusId()));
            if (conList != null && !conList.isEmpty()) {
                conInfo = conList.get(0);
            }
        }
        // 合同ID为空或者流水ID不为空通过流水来获取主表合同主信息
        if (conInfo == null && StringUtils.isNotEmpty(params.getRecordId())) {
            List<ConInfo> conList = conInfoService.getConInfo(new ConInfoVO(params.getRecordId()));
            if (conList != null && !conList.isEmpty()) {
                conInfo = conList.get(0);
            }
        }
        if (conInfo != null) {
            List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService
                    .getConInfoBusDtl(new ConInfoBusdtlVO(conInfo.getId()));
            String[] busTypes = { ConConstant.CON_BUS_TYPE_04, ConConstant.CON_BUS_TYPE_05,
                    ConConstant.CON_BUS_TYPE_06 };
            List<ConInfoRecord> conRecordList = conInfoRecordService
                    .getConInfoRecord(new ConInfoRecordVO(conInfo.getId(), busTypes));
            ConInfoDTO conDTO = new ConInfoDTO();
            BeanUtils.copyNotNullProperties(conInfo, conDTO);
            ConTypeVO conType = conTypeService.getConTypeView(conInfo.getTypeId());
            if (conType != null){
                conDTO.setTypeName(conType.getName());
            }
            conDTO.setRecordId(params.getRecordId());
            if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
                List<ConInfoBusdtlDTO> busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlDTO.class);
                conDTO.setBusdtlList(busdtlList);
            }
            // 冗余状态数据
            Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("CON_OTHER_STATUS", "CON_INFO_STATUS");
            if (ListUtils.isNotNullOrEmpty(conRecordList)) {
                List<ConInfoRecordDTO> recordList = BeanUtils.copyListProperties(conRecordList, ConInfoRecordDTO.class);
                for (ConInfoRecordDTO recordDTO : recordList) {
                    if (recordDTO.getStatus() != null) {
                        recordDTO.setStatusName(dictMap.getOrDefault("CON_OTHER_STATUS", new HashMap<>())
                                .getOrDefault(String.valueOf(recordDTO.getStatus()), DictDTO.EMPTY).getName());
                    }
                }
                conDTO.setRecordList(recordList);
            }
            //字典字段批量注入 todo 公共方法？
            if (conDTO.getStatus() != null) {
                conDTO.setStatusName(dictMap.getOrDefault("CON_INFO_STATUS", new HashMap<>())
                        .getOrDefault(String.valueOf(conDTO.getStatus()), DictDTO.EMPTY).getName());
            }
            return conDTO;
        }
        return null;
    }
    @Override
    public String registerConOpInnerLoan(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_INNERLOAN, user);
        return conInfoDTO.getId();
    }
    @Override
    public String registerConOpEntrustLoan(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_ENTRUSTLOAN, user);
        return conInfoDTO.getId();
    }
    @Override
    public String registerConOpBankBorrow(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_BANKBORROW, user);
        return conInfoDTO.getId();
    }
    @Override
    public void registerConOpRat(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_RAT, user);
    }
    @Override
    public void registerConOpGua(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_GUA, user);
    }
    @Override
    public void registerConOpExtGua(ConInfoDTO conInfoDTO, ICurrentUser user) {
        this.registerConOp(conInfoDTO, ConConstant.CON_PD_EXTGUA, user);
    }
    /**
     * 业务生成合同登记操作 corpId 合同发起人单位ID 必填为空提示异常 busId 挂接业务ID 必填为空提示异常 conName 合同名称 必填为空提示异常 conNO 合同编号 可以为空 typeId 合同类型ID
     * 必填为空提示异常 beginDate 合同生效日期 必填为空提示异常 endDate 合同失效日期 可以为空 outBusData 外部业务挂接数据 必填为空或者匹配规则必填项空提示异常
     *
     * @param conInfoDTO
     *            合同信息 return conInfoDTO 业务完成以后会回写合同ID到DTO中
     */
    @Override
    public void registerConOp(ConInfoDTO conInfoDTO, String conPdType, ICurrentUser user) {
        if (conInfoDTO == null || StringUtils.isEmpty(conInfoDTO.getCorpId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CORPID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(conInfoDTO.getBusId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_BUSID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(conInfoDTO.getConName())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CONNAME_ISNOTNULL);
        }
        if (StringUtils.isEmpty(conInfoDTO.getTypeId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_TYPEID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(conPdType)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CONPDTYPE_ISNOTNULL);
        }
        if (conInfoDTO.getBeginDate() == null) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_BEGINDATE_ISNOTNULL);
        }
        if (conInfoDTO.getOutBusData() == null) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_OUTBUSDATA_ISNOTNULL);
        }
        ConInfo conInfo = initConInfo(conInfoDTO);
        //TODO 补充单位名称
        CorpDTO corpDTO = corpServiceImpl.get(conInfoDTO.getCorpId());
        Optional.ofNullable(corpDTO).ifPresent(corpDO -> {
            conInfo.setCorpName(corpDO.getName());
            conInfo.setCorpCode(corpDO.getCode());
        });
        conInfoService.saveEntity(conInfo);
        conInfoDTO.setId(conInfo.getId());
        List<ConInfoBusDtl> conBusDtl = initConInfoBusDtlList(conInfoDTO, conPdType);
        if (ListUtils.isNotNullOrEmpty(conBusDtl)) {
            conInfoBusdtlService.batchSave(conBusDtl);
        }
        ConInfoRecord conRecord = initConInfoRecord(conInfoDTO, user);
        conInfoRecordService.saveEntity(conRecord);
        hookConOp(conInfo.getId(), conInfoDTO.getBusId(), user);
    }
    /**
     * 初始化合同主信息
     *
     * @param conInfoDTO
     * @return
     */
    public ConInfo initConInfo(ConInfoDTO conInfoDTO) {
        ConInfo conInfo = new ConInfo();
        // Date todayDate=new Date();
        BeanUtils.copyNotNullProperties(conInfoDTO, conInfo);
        conInfo.setSignType(ConConstant.CON_SIGN_TYPE_0);
        conInfo.setStatus(ConConstant.CON_INFO_STATUS_50);
        conInfo.setDataSource("1");
        // conInfo.setDraftDate(todayDate);
        // conInfo.setSignDdate(todayDate);
        return conInfo;
    }
    /**
     * 初始化合同业务明细信息
     *
     * @param conInfoDTO
     * @return
     */
    public List<ConInfoBusDtl> initConInfoBusDtlList(ConInfoDTO conInfoDTO, String conPdType) {
        List<ConTypeFieldVO> fieldVOList = conCommonDao.getConTypeFieldVOAndPdDtlCode(conInfoDTO.getTypeId(),
                conPdType);
        List<ConInfoBusDtl> busDtlList = new ArrayList<>();
        if (ListUtils.isNotNullOrEmpty(fieldVOList)) {
            Map<String, Object> mapValue = BeanUtils.beanToMap(conInfoDTO.getOutBusData());
            ConInfoBusDtl conBusDtl = null;
            for (ConTypeFieldVO fieldVO : fieldVOList) {
                conBusDtl = new ConInfoBusDtl();
                BeanUtils.copyNotNullProperties(fieldVO, conBusDtl);
                if (ConConstant.CON_REQUIREDSIGN_1.equals(fieldVO.getRequiredSign())) {
                    if (StringUtils.isEmpty(fieldVO.getPdDtlcode())
                            || StringUtils.isEmpty(fieldVO.getPdDtlcode().trim())) {
                        throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_PDDTLCODE_ISNOTNULL,
                                multiLangServiceI.getLang(fieldVO.getFieldName()));
                    } else if (!mapValue.containsKey(fieldVO.getPdDtlcode())) {
                        throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_PDDTLCODE_FIELDISNULL,
                                fieldVO.getPdDtlcode());
                    } else if (mapValue.get(fieldVO.getPdDtlcode()) == null) {
                        throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_PDDTLCODE_VALUEISNULL,
                                fieldVO.getPdDtlcode());
                    }
                }
                if (StringUtils.isNotEmpty(fieldVO.getPdDtlcode()) && mapValue.containsKey(fieldVO.getPdDtlcode())
                        && mapValue.get(fieldVO.getPdDtlcode()) != null) {
                    Object val = mapValue.get(fieldVO.getPdDtlcode());
                    String pattern = "#0.00";
                    if (ConConstant.CON_FIELD_TYPE_05.equals(fieldVO.getFieldType())) {
                        pattern = "#0.000000";
                    }
                    if (val instanceof Date) {
                        conBusDtl.setVal(DateUtils.date2Str((Date) val, DateUtils.date_sdf()));
                    } else if (val instanceof Timestamp) {
                        conBusDtl.setVal(
                                DateUtils.date2Str(new Date(((Timestamp) val).getTime()), DateUtils.date_sdf()));
                    } else if (val instanceof BigDecimal) {
                        conBusDtl.setVal(NumberUtil.formatNumeric((BigDecimal) val, pattern));
                    } else if (val instanceof Double) {
                        conBusDtl.setVal(NumberUtil.formatNumeric((Double) val, pattern));
                    } else if (val instanceof Float) {
                        conBusDtl.setVal(NumberUtil.formatNumeric((Float) val, pattern));
                    } else {
                        conBusDtl.setVal(String.valueOf(val));
                    }
                }
                conBusDtl.setMid(conInfoDTO.getId());
                conBusDtl.setName(fieldVO.getFieldName());
                busDtlList.add(conBusDtl);
            }
        }
        return busDtlList;
    }
    /**
     * 初始化自动登记明细信息
     *
     * @param conInfoDTO
     * @return
     */
    public ConInfoRecord initConInfoRecord(ConInfoDTO conInfoDTO, ICurrentUser user) {
        ConInfoRecord recordInfo = new ConInfoRecord();
        recordInfo.setMid(conInfoDTO.getId());
        recordInfo.setOuterId(conInfoDTO.getBusId());
        recordInfo.setBusType(ConConstant.CON_BUS_TYPE_03);
        recordInfo.setStatus(ConConstant.CON_DRAFT_STATUS_95);
        recordInfo.setValidSign(ConConstant.CON_VALID_SIGN_1);
        recordInfo.setCreateTime(new Date());
        recordInfo.setCreateId(user.getId());
        return recordInfo;
    }
    @Override
    public void hookConOp(String conId, String busId, ICurrentUser user) {
        List<ConBusHook> hookList = conBusHookService.getConBusHook(new ConBusHook(conId, null));
        if (isInCache(conId) || ListUtils.isNotNullOrEmpty(hookList)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ISEXIST);
        }
        if (StringUtils.isEmpty(conId)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CONID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(busId)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_BUSID_ISNOTNULL);
        }
        List<ConInfo> conInfoList = conInfoService.getConInfo(new ConInfoVO(conId, null));
        if (ListUtils.isNullOrEmpty(conInfoList)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CON_ISNOTEXIST);
        }
        ConBusHook entity = new ConBusHook();
        entity.setBusId(busId);
        entity.setConId(conId);
        entity.setValidSign(ConConstant.CON_BUSHOOK_VALID_SIGN_1);
        entity.setCreateId(user.getId());
        entity.setCreateTime(new Date());
        conBusHookService.saveEntity(entity);
        int size = conInfoService.hookConOp(conId, true);
        if (size < 0) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ISEXIST);
        }
        removeFromCache(conId);
    }
    @Override
    public void unHookConOp(String conId, String busId, ICurrentUser user) {
        List<ConInfo> conInfoList = conInfoService.getConInfo(new ConInfoVO(conId, busId));
        if (ListUtils.isNullOrEmpty(conInfoList)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CON_ISNOTEXIST);
        }
        conInfoService.hookConOp(conId, false);
        if (StringUtils.isNotEmpty(conInfoList.get(0).getDataSource())
                && "1".equals(conInfoList.get(0).getDataSource())) {
            conInfoService.updateConInfoStatusById(conInfoList.get(0).getId(), ConConstant.CON_DRAFT_STATUS_99);
        }
        conBusHookService.updateConBusHookIsOver(new ConBusHook(conId, busId), user);
    }
    /**
     * 进行合同履约操作 conId 合同ID 必填为空提示异常 outerId 履约业务挂接ID 必填为空提示异常 opMsg 履约原因 必填为空提示异常 opDate 履约时间 必填为空提示异常 opNo 履约申请编号 选填
     * opMoney 履约金额 选填
     *
     * @param recordDTO
     *            挂接信息参数 return recordDTO对象中会把履约流水ID回写
     */
    @Override
    public void performanceConOp(ConInfoRecordDTO recordDTO, ICurrentUser user) {
        if (StringUtils.isEmpty(recordDTO.getConId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CONID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(recordDTO.getOuterId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_BUSID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(recordDTO.getOpMsg())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_LY_OPMSG_ISNOTNULL);
        }
        if (recordDTO.getOpDate() == null) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_LY_OPDATE_ISNOTNULL);
        }
        List<ConBusHook> hookList = conBusHookService.getConBusHook(new ConBusHook(recordDTO.getConId(), null));
        if (ListUtils.isNullOrEmpty(hookList)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ISNOtEXIST);
        }
        ConInfo conInfo = conInfoService.get(recordDTO.getConId(), true);
        if (conInfo != null) {
            ConInfoVO conInfoVO = new ConInfoVO();
            BeanUtils.copyNotNullProperties(conInfo, conInfoVO);
            // 合同状态为【XXX】，不允许履约，请检查并完成合同业务流程!
            if (!ConConstant.CON_INFO_STATUS_50.equals(conInfoVO.getStatus())) {
                Map<String, DictDTO> dictDTOMap = dictService.listMap(ConConstant.CON_INFO_STATUS);
                throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_LY_STATUSISERROR,dictDTOMap.getOrDefault(String.valueOf(conInfoVO.getStatus()),DictDTO.EMPTY).getName());
            }
        } else {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CON_ISNOTEXIST);
        }
        saveConInfoRecord(recordDTO, ConConstant.CON_BUS_TYPE_05, user);
        // 对合同次数进行叠加操作
        conInfoService.updateConInfoByConInfoVO(new ConInfoVO(recordDTO.getConId(), "1", null));
    }
    /**
     * 进行合同终止操作 conId 合同ID 必填为空提示异常 outerId 终止业务挂接ID 必填为空提示异常 opMsg 终止原因 必填为空提示异常 opDate 终止时间 必填为空提示异常 opNo 终止申请编号 选填
     *
     * @param recordDTO
     *            挂接信息参数 return recordDTO对象中会把终止流水ID回写
     */
    @Override
    public void terminateConOp(ConInfoRecordDTO recordDTO, ICurrentUser user) {
        if (StringUtils.isEmpty(recordDTO.getConId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CONID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(recordDTO.getOuterId())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_BUSID_ISNOTNULL);
        }
        if (StringUtils.isEmpty(recordDTO.getOpMsg())) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ZZ_OPMSG_ISNOTNULL);
        }
        if (recordDTO.getOpDate() == null) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ZZ_OPDATE_ISNOTNULL);
        }
        List<ConBusHook> hookList = conBusHookService.getConBusHook(new ConBusHook(recordDTO.getConId(), null));
        if (ListUtils.isNullOrEmpty(hookList)) {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ISNOtEXIST);
        }
        ConInfo conInfo = conInfoService.get(recordDTO.getConId(), true);
        if (conInfo != null) {
            ConInfoVO conInfoVO = new ConInfoVO();
            BeanUtils.copyNotNullProperties(conInfo, conInfoVO);
            // 合同状态为【XXX】，不允许终止，请检查并完成合同业务流程!
            if (!ConConstant.CON_INFO_STATUS_50.equals(conInfoVO.getStatus())) {
                throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_ZZ_STATUSISERROR, conInfoVO.getStatusName());
            }
        } else {
            throw new BusinessException(ConMessageEnum.CONTRACT_HOOK_CON_ISNOTEXIST);
        }
        saveConInfoRecord(recordDTO, ConConstant.CON_BUS_TYPE_06, user);
        // 对合同状态进行终止操作
        conInfoService.updateConInfoStatusById(recordDTO.getConId(), ConConstant.CON_INFO_STATUS_90);
        // 对登记或者签署流水有效标记设置为2已使用
        conInfoRecordService.updateRecordValidSignByConId(recordDTO.getConId(), ConConstant.CON_VALID_SIGN_2, user);
    }
    /**
     * 初始化自动登记明细信息
     *
     * @param conInfoDTO
     * @return
     */
    public void saveConInfoRecord(ConInfoRecordDTO recordDTO, String busType, ICurrentUser user) {
        ConInfoRecord recordInfo = new ConInfoRecord();
        recordInfo.setMid(recordDTO.getConId());
        recordInfo.setBusType(busType);
        recordInfo.setOuterId(recordDTO.getOuterId());
        recordInfo.setStatus(ConConstant.CON_DRAFT_STATUS_95);
        recordInfo.setValidSign(ConConstant.CON_VALID_SIGN_1);
        if (ConConstant.CON_BUS_TYPE_05.equals(busType)) {
            recordInfo.setOpMoney(recordDTO.getOpMoney());
        }
        recordInfo.setOpMsg(recordDTO.getOpMsg());
        recordInfo.setOpDate(recordDTO.getOpDate());
        recordInfo.setOpNo(recordDTO.getOpNo());
        recordInfo.setCreateTime(new Date());
        recordInfo.setCreateId(user.getId());
        recordInfo.setRmk(recordDTO.getRmk());
        conInfoRecordService.saveEntity(recordInfo);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<>();
            // 获取有效的合同数据
            int num = conCommonDao.getConInfoByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CON,
                        SysConfigConstant.BusType.CON_TRACT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CON_VALID_DISABLE_CONTRACT_ERROR)));
            }
            if (verifyErrorDTOS.size() > 0) {
                return verifyErrorDTOS;
            }
        }
        return null;
    }
}
