package com.bytter.biz.finacial.paramconfig.service;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.IFcFinacialParamConfigService;
import com.bytter.biz.finacial.paramconfig.dao.FcBusinessRuleDefineDao;
import com.bytter.biz.finacial.paramconfig.dao.FcVoucherEntryDefineDao;
import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefine;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherEntryDefine;
import com.bytter.biz.finacial.paramconfig.vo.*;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_BUSINESS_RULE_DEFINE
 * @date 2017-05-22 19:23:12
 */
@Service("fcBusinessRuleDefineService")
@Transactional
public class FcBusinessRuleDefineService extends BaseService<FcBusinessRuleDefine, String> {
    @Autowired
    private FcBusinessRuleDefineDao fcBusinessRuleDefineDao;
    @Autowired
    private FcVoucherEntryDefineService voucherEntryService;
    @Autowired
    private FcVoucherEntryDefineDao fcVoucherEntryDefineDao;
    @DubboReference(check = false)
    private IFcFinacialParamConfigService paramConfigService;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(FcBusinessRuleDefine entity) {
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
     * 功能介绍 : 业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getBusninessRuleVoList(DataTablesInput input) {
        DataTablesOutput dataTablesOutput = fcBusinessRuleDefineDao.getBusninessRuleVoList(input);
        List data = dataTablesOutput.getData();
        ArrayList<String> subjectIds = new ArrayList<String>();
        ArrayList<String> netIds = new ArrayList<String>();
        ArrayList<String> curIds = new ArrayList<String>();
        ArrayList<FcBusinessRuleDefineListVo> vos = new ArrayList<>();
        for (Object datum : data) {
            FcBusinessRuleDefineListVo vo = (FcBusinessRuleDefineListVo) datum;
            subjectIds.add(vo.getSubjId());
            if (StringUtils.isNotEmpty(vo.getNetId())) {
                netIds.add(vo.getNetId());
            }
            curIds.add(vo.getCurId());
            vos.add(vo);
        }
        List<FcSubjectDTO> fcSubjectDTOS = iFcSubjectService.getSubjectByIds(subjectIds);
        Map<String, FcSubjectDTO> subjectIdMap = fcSubjectDTOS.stream().collect(Collectors.toMap(FcSubjectDTO::getId, Function.identity(), (a, b) -> a));
        List<CorpDTO> corpDTOS = iCorpService.listCorpDTOByCorpIds(netIds);
        Map<String, CorpDTO> corpIdMap = corpDTOS.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
        Map<String, BtCurrencyDTO> curIdMap = iBtCurrencyService.getCurrencyByIds(curIds);
        vos.forEach(a -> {
            FcSubjectDTO fcSubjectDTO = subjectIdMap.get(a.getSubjId());
            CorpDTO corpDTO = corpIdMap.get(a.getNetId());
            BtCurrencyDTO btCurrencyDTO = curIdMap.get(a.getCurId());
            if (ObjectUtils.isNotEmpty(fcSubjectDTO)) {
                a.setSubjCode(fcSubjectDTO.getSubjectCode());
                a.setSubName(fcSubjectDTO.getSubjectName());
            }
            if (ObjectUtils.isNotEmpty(corpDTO)) {
                a.setNetName(corpDTO.getName());
            }
            if (ObjectUtils.isNotEmpty(btCurrencyDTO)) {
                a.setCurName(btCurrencyDTO.getCurName());
                a.setCurCode(btCurrencyDTO.getCurCode());
            }
        });
        return dataTablesOutput;
    }

    /**
     * 功能介绍 : 初始化业务记账规则列表数据查询。<br>
     * <b>日期：</b>2017年5月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getInitBusninessRuleVoList(DataTablesInput input) {
        DataTablesOutput dataTablesOutput = fcBusinessRuleDefineDao.getInitBusninessRuleVoList(input);
        List data = dataTablesOutput.getData();
        ArrayList<FcBusinessRuleDefineListVo> vos = new ArrayList<>();
        ArrayList<String> subjectCodes = new ArrayList<>();
        for (Object datum : data) {
            FcBusinessRuleDefineListVo vo = (FcBusinessRuleDefineListVo) datum;
            vos.add(vo);
            subjectCodes.add(vo.getSubjCode());
        }
        Map<String, String> subjectMap = iFcSubjectService.getSubjectNameBySubjectCode(subjectCodes);
        vos.forEach(a -> {
            String s = subjectMap.get(a.getSubjCode());
            a.setSubName(s);
        });
        dataTablesOutput.setData(vos);
        return dataTablesOutput;
    }

    /**
     * 功能介绍 : 根据记账规则ID获取记账规则信息和分录明细。<br>
     * <b>日期：</b>2017年5月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public FcBusinessRuleDefineVo getBusinessRuleDefineVoById(String id) {
        FcBusinessRuleDefineVo ruleDefineVo = new FcBusinessRuleDefineVo();
        FcBusinessRuleDefine fcBusinessRuleDefine = this.get(id);
        if (null == fcBusinessRuleDefine) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        BeanUtils.copyProperties(fcBusinessRuleDefine, ruleDefineVo);
        //List<FcVoucherEntryDefine> entryDefines = voucherEntryService.getEntryDefinesByRuleId(id);
        List<FcVoucherEntryDefineVo> entryDefineVos = voucherEntryService.getVoucherEntryDefinesByRuleId(id);
        //List<FcVoucherEntryDefineVo> entryDefineVos = BeanUtils.copyListProperties(entryDefines, FcVoucherEntryDefineVo.class);
        for (FcVoucherEntryDefineVo fcVoucherEntryDefineVo : entryDefineVos) {
            if (fcVoucherEntryDefineVo.getSubjectName() == null) {
                fcVoucherEntryDefineVo.setSubjectCode(null);
            }
        }
        ruleDefineVo.setVouchers(entryDefineVos);
        return ruleDefineVo;
    }

    /**
     * 功能介绍 : 是否存在相同的记账规则。<br>
     * <b>日期：</b>2017年11月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkSameRuleDefine(FcBusinessRuleDefineVo ruleDefineVo) {
        List<FcBusinessRuleDefineVo> ruleDefineVos = fcBusinessRuleDefineDao.existBusinessRule(ruleDefineVo);
        if (!EmptyUtils.isEmptyList(ruleDefineVos)) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_SAME);
        }
    }

    /**
     * 功能介绍：保存记账规则定义和分录数据<br>
     * <b>日期：</b>2017年5月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveBusinessRule(FcBusinessRuleDefineVo fcBusinessRuleDefineVo, ICurrentUser user) {
        List<FcVoucherEntryDefineVo> vouchers = fcBusinessRuleDefineVo.getVouchers();
        //分录不能为空
        if (null == vouchers || vouchers.isEmpty()) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        FcBusinessRuleDefine fcBusinessRuleDefine = new FcBusinessRuleDefine();
        //将网点，科目，币别等的空格过滤掉
        fcBusinessRuleDefineVo.setCurCode(StringUtils.formatNull(fcBusinessRuleDefineVo.getCurCode()).trim());
        fcBusinessRuleDefineVo.setNetId(StringUtils.formatNull(fcBusinessRuleDefineVo.getNetId()).trim());
        fcBusinessRuleDefineVo.setSubjCode(StringUtils.formatNull(fcBusinessRuleDefineVo.getSubjCode()).trim());
        fcBusinessRuleDefineVo.setSysRaceCode(StringUtils.formatNull(fcBusinessRuleDefineVo.getSysRaceCode()).trim());
        //拷贝数据对象
        BeanUtils.copyProperties(fcBusinessRuleDefineVo, fcBusinessRuleDefine);
        //是否已存在相同的记账规则
        checkSameRuleDefine(fcBusinessRuleDefineVo);
        fcBusinessRuleDefine.setCreateBy(user.getId());
        fcBusinessRuleDefine.setCreateTime(DateUtils.getDate());
        super.saveEntity(fcBusinessRuleDefine);
        Set<String> summaryContent = new HashSet<String>();
        //校验分录是否包含借贷标识
        boolean debitFlag = true, creditFlag = true;
        for (FcVoucherEntryDefineVo entryDefine : vouchers) {
            entryDefine.setBusinessRuleDefineId(fcBusinessRuleDefine.getId());
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
                debitFlag = false;
            }
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
                creditFlag = false;
            }
            entryDefine.setBelongType(20);
            if (!EmptyUtils.isEmpty(entryDefine.getSummary())) {
                summaryContent.add(entryDefine.getSummary());
            }
        }
        //提示分录不包含借贷，必须借贷方都要定义
        if (creditFlag || debitFlag) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
        }
        //批量保存分录
        List<FcVoucherEntryDefine> entryDefines = BeanUtils.copyListProperties(vouchers, FcVoucherEntryDefine.class);
        voucherEntryService.batchSave(entryDefines);
    }

    /**
     * 功能介绍：更新记账规则定义和分录数据<br>
     */
    public void updateBusinessRule(FcBusinessRuleDefineVo ruleDefineVo, ICurrentUser user) {
        List<FcVoucherEntryDefine> vouchers = BeanUtils.copyListProperties(ruleDefineVo.getVouchers(), FcVoucherEntryDefine.class);
        //分录不能为空
        if (null == vouchers || vouchers.isEmpty()) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        FcBusinessRuleDefine fcBusinessRuleDefine = this.get(ruleDefineVo.getId());
        //将网点，科目，币别等的空格过滤掉
        verify(ruleDefineVo);
        ruleDefineVo.setCurCode(ruleDefineVo.getCurCode().trim());
        ruleDefineVo.setNetId(ruleDefineVo.getNetId().trim());
        if (ruleDefineVo.getSubjCode() == null) {
            ruleDefineVo.setSubjCode(ruleDefineVo.getSubjCode());
        } else {
            ruleDefineVo.setSubjCode(ruleDefineVo.getSubjCode().trim());
        }
        ruleDefineVo.setSysRaceCode(ruleDefineVo.getSysRaceCode().trim());
        if (!EmptyUtils.isEmpty(fcBusinessRuleDefine)) {
            ruleDefineVo.setId(fcBusinessRuleDefine.getId());
        }
        checkSameRuleDefine(ruleDefineVo);
        //拷贝数据对象
        BeanUtils.copyNotNullProperties(ruleDefineVo, fcBusinessRuleDefine);
        fcBusinessRuleDefine.setUpdateBy(user.getId());
        fcBusinessRuleDefine.setUpdateTime(DateUtils.getDate());
        super.updateEntity(fcBusinessRuleDefine);
        Set<String> summaryContent = new HashSet<String>();
        //校验分录是否包含借贷标识
        boolean debitFlag = true, creditFlag = true;
        for (FcVoucherEntryDefine entryDefine : vouchers) {
            entryDefine.setBusinessRuleDefineId(ruleDefineVo.getId());
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_DEBIT)) {
                debitFlag = false;
            }
            if (entryDefine.getBalanceWay().toString().equals(FinacialConstant.BALANCE_WAY_CREDIT)) {
                creditFlag = false;
            }
            entryDefine.setBelongType(20);
            if (!EmptyUtils.isEmpty(entryDefine.getSummary())) {
                summaryContent.add(entryDefine.getSummary());
            }
        }
        //提示分录不包含借贷，必须借贷方都要定义
        if (creditFlag || debitFlag) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_BALANCE);
        }
        //先删除分录明细，再批量保存分录
        voucherEntryService.deleteEntryDefinesByRuleId(ruleDefineVo.getId());
        voucherEntryService.batchSave(vouchers);
    }

    /**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-8-16<br>
     */
    public List<FcBusinessRuleDefineVo> getBusinessRuleExp(Map<String, Object> map) {
        return fcBusinessRuleDefineDao.getBusinessRuleExp(map);
    }

    /***
     * 查询凭证分录定义
     */
    public List<FcVoucherEntryDefineVo> getVoucher(String defineId) {
        return fcBusinessRuleDefineDao.getVoucher(defineId);
    }

    /**
     * 功能介绍 : 根据记账规则ID删除分录和规则数据<br>
     * <b>日期：</b>2017年5月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteBusinessRule(String ruleId) {
        voucherEntryService.deleteEntryDefinesByRuleId(ruleId);
        this.delete(ruleId);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcBusinessRuleDefine entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcBusinessRuleDefine entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(FcBusinessRuleDefine entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<FcBusinessRuleDefine, String> getEntityDao() {
        return fcBusinessRuleDefineDao;
    }

    /**
     * 获取有效的业务种类
     *
     * @return
     */
    public List<FcSysBusRaceVO> getFcSysBusRace() {
        return fcBusinessRuleDefineDao.getFcSysBusRace();
    }

    /**
     * 功能介绍 : 根据记账规则ID查看记账规则信息和分录明细。<br>
     * <b>日期：</b>2017年9月23日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public FcBusinessRuleDefineVo getBusRuleDefineVoById(String id) {
        FcBusinessRuleDefineVo fcBusinessRuleDefine = this.fcBusinessRuleDefineDao.getBusninessRuleVo(id);
        if (null == fcBusinessRuleDefine) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        List<FcVoucherEntryDefineVo> entryDefineVos = voucherEntryService.getVoucherEntryDefinesByRuleId(id);
        fcBusinessRuleDefine.setVouchers(entryDefineVos);
        String corpId = fcBusinessRuleDefine.getNetId();
        if (StringUtils.isNotBlank(corpId)) {
            CorpDTO corpDTO = iCorpService.get(corpId);
            fcBusinessRuleDefine.setNetId(corpDTO.getName());
        }
        String curId = fcBusinessRuleDefine.getCurId();
        if (StringUtils.isNotBlank(curId)) {
            BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
            fcBusinessRuleDefine.setCurCode(btCurrencyDTO.getCurName());
        }
        String subjectId = fcBusinessRuleDefine.getSubjectId();
        if (StringUtils.isNotBlank(subjectId)) {
            FcSubjectDTO fcSubjectDTO = iFcSubjectService.findSubjectById(subjectId);
            fcBusinessRuleDefine.setSubjCode(fcSubjectDTO.getSubjectName());
        }

        FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(fcBusinessRuleDefine.getVoucherId());
        if (ObjectUtils.isNotEmpty(dto) && StringUtils.isNotBlank(dto.getVoucherName()))
            fcBusinessRuleDefine.setVoucherType(dto.getVoucherName()); //历史遗留
        return fcBusinessRuleDefine;
    }

    /**
     * 功能介绍 : 根据记账规则ID查看记账规则信息和分录明细。<br>
     * <b>日期：</b>2017年9月23日<br>
     *
     * @version 1.0
     * @author yangl
     */
    public FcBusinessRuleDefineVo getInitBusRuleDefineVoById(String id) {
        FcBusinessRuleDefineVo ruleDefineVo = new FcBusinessRuleDefineVo();
        FcBusinessRuleDefine fcBusinessRuleDefine = this.fcBusinessRuleDefineDao.getInitBusninessRuleVo(id);
        if (null == fcBusinessRuleDefine) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_BUSINESS_VOUCHERS_NULL);
        }
        String corpId = fcBusinessRuleDefine.getNetId();
        String curId = fcBusinessRuleDefine.getCurCode();
        String subjectId = fcBusinessRuleDefine.getSubjCode();
        FcSubjectDTO fcSubjectDTO = iFcSubjectService.findSubjectById(subjectId);
        CorpDTO corpDTO = iCorpService.get(corpId);
        BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
        fcBusinessRuleDefine.setNetId(corpDTO.getNetName());
        fcBusinessRuleDefine.setSubjCode(fcSubjectDTO.getSubjectName());
        fcBusinessRuleDefine.setCurCode(btCurrencyDTO.getCurName());
        BeanUtils.copyProperties(fcBusinessRuleDefine, ruleDefineVo);
        List<FcVoucherEntryDefineVo> entryDefineVos = voucherEntryService.getInitVoucherEntryDefinesByRuleId(id);
        ruleDefineVo.setVouchers(entryDefineVos);
        FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeById(fcBusinessRuleDefine.getVoucherType());
        fcBusinessRuleDefine.setVoucherType(dto.getVoucherName());
        return ruleDefineVo;
    }

    public void saveInitBusinessRuleDefine(String idKeys) {
    }

    /**
     * 功能介绍 : 初始化所有的记账规则。<br>
     * <b>日期：</b>2017年11月21日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void initBusinessRuleDefine() {
        // 记账规则初始化
        // 1、初始化之前先删除已初始化过的数据
        fcBusinessRuleDefineDao.deleteInitBusinessRule();
        fcVoucherEntryDefineDao.deleteInitVoucherEntry();

        // 2、再查询初始化数据
        List<FcBusinessRuleDefine> initBusinessRuleList = fcBusinessRuleDefineDao.listInitBusinessRule();
        if (!CollectionUtils.isEmpty(initBusinessRuleList)) {
            initBusinessRuleList.forEach(rule -> {
                // 先保存初始化的记账规则ID
                String businessRuleDefineId = rule.getId();

                // 初始化业务规则表中的付款方式
                String initRuleVoucherType = rule.getVoucherType();
                // 各租户付款方式id
                String ruleVoucherTypeId = this.getVoucherTypeId(initRuleVoucherType);
                rule.setVoucherType(ruleVoucherTypeId);

                // 初始化业务规则表中的会计科目
                String initRuleSubjectCode = rule.getSubjCode();
                // 各租户会计科目id
                String ruleSubjectCodeId = this.getSubjectCodeId(initRuleSubjectCode);
                rule.setSubjCode(ruleSubjectCodeId);

                // 标记为初始化标记
                rule.setIsInit(CommonConstant.YES);
                rule.setId(null);
                // 保存业务规则，返回业务规则主键id
                this.saveEntity(rule);

                List<FcVoucherEntryDefine> initVoucherEntryList = fcVoucherEntryDefineDao.listInitVoucherEntry(businessRuleDefineId);
                if (!CollectionUtils.isEmpty(initVoucherEntryList)) {
                    for (FcVoucherEntryDefine entry : initVoucherEntryList) {// 业务规则主键ID
                        entry.setBusinessRuleDefineId(rule.getId());
                        entry.setIsInit(CommonConstant.YES);

                        // 初始化凭证分录表中的付款方式
                        String initEntryVoucherType = entry.getVoucherType();
                        // 各租户付款方式id
                        String entryVoucherTypeId = this.getVoucherTypeId(initEntryVoucherType);
                        entry.setVoucherType(entryVoucherTypeId);

                        // 初始化业务规则表中的会计科目
                        String initEntrySubjectCode = entry.getSubjectCode();
                        // 各租户会计科目id
                        String entrySubjectCodeId = this.getSubjectCodeId(initEntrySubjectCode);
                        entry.setSubjectCode(entrySubjectCodeId);
                    }
                    // 批量保存凭证分录
                    voucherEntryService.batchSave(initVoucherEntryList);
                }
            });
        }
    }

    /**
     * 根据付款方式编码获取不同租户的付款方式ID
     *
     * @author fangcf
     */
    public String getVoucherTypeId(String initVoucherType) {
        if (StringUtils.isNotEmpty(initVoucherType) && StringUtils.isNotEmpty(initVoucherType.trim())) {
            FcVoucherTypeDTO dto = iFcVoucherTypeService.getFcVoucherTypeByCode(initVoucherType);
            if (ObjectUtils.isNotEmpty(dto)) {
                return dto.getId();
            }
        }
        return null;
    }

    /**
     * 根据会计科目获取不同租户的会计科目id
     *
     * @author fangcf
     */
    public String getSubjectCodeId(String subjectCode) {
        if (StringUtils.isNotEmpty(subjectCode) && StringUtils.isNotEmpty(subjectCode.trim())) {
            FcSubjectDTO dto = iFcSubjectService.findSubjectByCode(subjectCode);
            if (ObjectUtils.isNotEmpty(dto)) {
                return dto.getId();
            }
        }
        return null;
    }

    /**
     * 获取有效的科目
     *
     * @return
     */
    public List<FcSubjectVo> listValidSubject() {
//        return fcBusinessRuleDefineDao.listValidSubject();
        List<FcSubjectDTO> fcSubjectDTOS = iFcSubjectService.listValidSubject();
        return BeanUtils.copyListProperties(fcSubjectDTOS, FcSubjectVo.class);
    }


    /***
     * 验证
     */
    public void verify(FcBusinessRuleDefineVo ruleDefineVo) {
        /** 网点 **/
        if (EmptyUtils.isEmpty(ruleDefineVo.getNetId())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_NETID_NOTNULL);
        }
        /** 科目 **/
//		if (EmptyUtils.isEmpty(ruleDefineVo.getSubjCode())) {
//			throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_SUBJECTID_NOTNULL);
//		}
        /** 币别 **/
        if (EmptyUtils.isEmpty(ruleDefineVo.getCurCode())) {
            throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_CURID_NOTNULL);
        }
    }
}
