package com.bytter.credit.domain.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatUrationDao;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.credit.vo.RatUrationSysBean;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 授信占用台账表
 * @date 2017-08-19 11:36:37
 */
@Slf4j
@Service("ratUrationService")
@Transactional
public class RatUrationService extends BaseService<RatUration, String>
        implements ITaskService, ICorpDisableVerifyService {
    @Autowired
    private RatUrationDao ratUrationDao;

    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference
    private IInputBankInfoService bankInfoService;
    @Autowired
    private RatBkrationService rationService;
    @Autowired
    private RatBkrationDetailService rationDtlService;
    @Autowired
    private RatUrationApplyService applyService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatUrationApplyDtlService applyDtlService;
    @Autowired
    private RatBkrationReliefDetailService reliefDetailService;
    @Autowired
    private RatBkrationAddDetailService addDetailService;
    @Autowired
    private RatItemCustomService itemCustomService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService excrateAdjustService;
    @Autowired
    private RatBusinessRaceService businessRaceService;
    @Autowired
    private IGuaWayServie guaWayService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired(required = false)
    private RatBkUseStatisticsSercive statisticsSercive;
    @Resource
    private IMultiLangService multiLangServiceI;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatUration entity) {
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
    public void save(RatUration entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatUration entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatUration entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<RatUration, String> getEntityDao() {
        return ratUrationDao;
    }

    /**
     * 功能介绍 : 根据系统类型和业务编号获取占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUration> queryRatUrationById(String sysCode, String billNo) {
        return ratUrationDao.queryRatUrationById(sysCode, billNo);
    }

    /**
     * 功能介绍 : 根据系统类型和业务编号获取占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUration> queryRatUrationById(String sysCode, String billNo, String ratType) {
        return ratUrationDao.queryRatUrationById(sysCode, billNo, ratType);
    }

    /**
     * 功能介绍 : 根据系统类型和业务编号获取正式占用信息。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatUration> queryUrationByBillId(String sysCode, String billNo) {
        return ratUrationDao.queryUrationByBillId(sysCode, billNo);
    }

    /**
     * 功能介绍 : 金额非空处理。<br>
     * <b>日期：</b>2017年8月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveOrUpdateUration(RatUration apply) {
        Class userCla = apply.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(apply);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(apply, BigDecimal.ZERO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.saveOrUpdate(apply);
    }

    /**
     * 功能介绍 : 查询授信占用可结转数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput<RatUrationBeanDTO> queryRatURationList(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "ru.corp_Id", true);
        DataTablesOutput datas = ratUrationDao.queryRatURationList(input, corpAuthSql);
        List<RatUrationBeanDTO> ratUrationBeanDTOS = datas.getData();
        for (RatUrationBeanDTO urationBeanDTO : ratUrationBeanDTOS) {
            if (urationBeanDTO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {// 综合品种时需查明细表得到授信品种名称
                List<RatUrationApplyDtlDTO> applyDtlDTOS = applyDtlService
                        .queryRatUrationByApplyId(urationBeanDTO.getBillId());
                if (applyDtlDTOS != null && applyDtlDTOS.size() > 0) {
                    for (RatUrationApplyDtlDTO urationApplyDtlDTO : applyDtlDTOS) {
                        if (urationApplyDtlDTO.getMaxMoney().compareTo(BigDecimal.ZERO) != 0
                                && urationApplyDtlDTO.getCreditType().equals("1")) {// 循环授信才可以结转
                            urationBeanDTO.setItemName(urationApplyDtlDTO.getItemName());
                        }
                    }
                }
            }
        }
        DataTablesOutput<RatUrationBeanDTO> dataTablesOutput = new DataTablesOutput();
        dataTablesOutput.setData(ratUrationBeanDTOS);// 重新放入数据
        dataTablesOutput.setRecordsTotal(datas.getRecordsTotal());// 重新设置，便于分页数据显示
        dataTablesOutput.setDraw(datas.getDraw());
        dataTablesOutput.setRecordsFiltered(datas.getRecordsFiltered());
        return dataTablesOutput;
    }

    /**
     * 功能介绍 : 授信结转单选择。<br>
     * <b>日期：</b>2017年8月31日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationBean> queryChangeRation(String urationIds, String changeType) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        log.info("开始查询可结转的授信额度单......");
        String[] urationIdArr = urationIds.split(CommonConstant.SEMI_SPLIT);
        List<RatUrationBean> urationBeans = new ArrayList<RatUrationBean>();
        for (String urationId : urationIdArr) {
            RatUrationBean urationBean = checkChangeRation(urationId, changeType);
            if (!EmptyUtils.isEmptyList(urationBean.getRatCodes())) {
                List<RatUrationDisBean> rationDtls = ratUrationDao.queryCorpRatUration(urationBean.getCorpId(), urationBean.getRatCodes());
                log.info("查询可结转的授信额度单，queryCorpRatUration：{}", gson.toJson(rationDtls));
                // 设置占用剩余额度
                for (RatUrationDisBean disBean : rationDtls) {
                    // 在审占用额度+追减
                    BigDecimal approveMoney = disBean.getFreezeMoney();
                    disBean.setApproveMoney(approveMoney);// 在审占用
                    disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney)
                            .subtract(disBean.getVirtualRelief()).subtract(disBean.getVirtualUration())
                            .subtract(disBean.getReliefAndUrationMoney()));// 可用额度
                    //剩余额度 = 在审+可用(包含虚拟在途占用和虚拟在途调剂，这些不能在真实在途占用中)
                    disBean.setLeftMoney(disBean.getApproveMoney().add(disBean.getOverPlusMoney()).add(disBean.getVirtualRelief()).add(disBean.getVirtualUration()));
                }
                // 过滤去除可用额度不足的授信合同
                rationDtls = rationDtls.stream().filter(item -> item.getOverPlusMoney().compareTo(urationBean.getMoney()) > 0).collect(Collectors.toList());
                urationBean.setRationDtls(rationDtls);
            }
            urationBeans.add(urationBean);
        }
        log.info("结束查询可结转的授信额度单，返回结果集：{}", gson.toJson(urationBeans));
        return urationBeans;
    }

    /**
     * 功能介绍 : 检查授信结转新的授信条数。<br>
     * <b>日期：</b>2017年8月30日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatUrationBean checkChangeRation(String urationId, String changeType) {
        log.info("查询可结转的授信额度单，传入参数：urationId：{}，changeType：{}", urationId, changeType);
        // 1.获得授信占用信息
        RatUration uration = get(urationId, true);
        // 2.获得已经占用的银行授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);
        // 3.获取同单位银行币别的其他授信数据
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        urationDTO.setId(urationId);
        urationDTO.setType(uration.getType());// 业务类型
        urationDTO.setBeginDate(bkration.getBegindate());// 授信开始日期
        urationDTO.setEndDate(bkration.getEnddate());// 授信结束日期
        urationDTO.setBankCode(bkration.getBankCode());
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setItemFlag(uration.getItemFlag());
        urationDTO.setItemCode(uration.getItemCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_1);// 相同品种结转
        urationDTO.setRatMode(bkration.getRatMode());
        urationDTO.setOldDisCode(uration.getDisCode());

        // 3.获取同单位同银行同币别同授信品种的其他授信数据
        List<RatBkRationVm> rationList = rationService.queryCorpOtherRation(urationDTO, changeType);
        log.info("查询可结转的授信额度单，queryCorpOtherRation：{}", rationList);

        RatUrationBean urationBean = new RatUrationBean();
        urationBean.setMoney(uration.getMoney());
        urationBean.setCorpId(uration.getCorpId());// 占用单位ID
        urationBean.setUrationId(urationId);
        urationBean.setUrationCode(uration.getUrationCode());//占用编号

        // 无其他可结转授信
        if (EmptyUtils.isEmptyList(rationList)) {
            return urationBean;
        }
        // 授信集合ID
        List<String> ratCodes = Lists.newArrayList();
        rationList.forEach(item -> ratCodes.add(item.getId()));
        urationBean.setRatCodes(ratCodes);
        return urationBean;
    }

    /**
     * 功能介绍 : 授信结转。<br>
     * <b>日期：</b>2017年8月30日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void changeRationCode(String urationIds, String changeType, String userId) {
        if (RationConstant.RAT_CHANGE_TYPE_1.equals(changeType)) {// 相同品种结转
            changeSameRationCode(urationIds, userId);
        } else if (RationConstant.RAT_CHANGE_TYPE_2.equals(changeType)) {// 综合转非综合
            changeRationComp2Single(urationIds, userId);
        } else if (RationConstant.RAT_CHANGE_TYPE_3.equals(changeType)) {// 非综合转综合
            changeRationSingle2Comp(urationIds, userId);
        }
    }

    /**
     * 功能介绍 :授信综合转非综合。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void changeRationComp2Single(String urationIds, String userId) {
        // 授信占用明细是否是综合授信
        if (StringUtils.isEmpty(urationIds)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_USE_SELECTNULL);
        }
        String[] urationIdArr = urationIds.split(CommonConstant.SEMI_SPLIT);
        for (String urationId : urationIdArr) {
            processComp2Single(urationId, userId);
        }
    }

    /**
     * 功能介绍 : 综合授信结转非综合。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processComp2Single(String urationId, String userId) {
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        String[] urationArr = urationId.split(CommonConstant.COMMA_SPLIT);
        //1.获得授信占用信息
        RatUration uration = get(urationArr[0], true);
        urationDTO.setId(urationArr[0]);
        urationDTO.setNewRatCode(urationArr[1]);

        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {// 非综合品种占用
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_NOTCOMP_ITEMCODE);
        }
        // 2.获得已经占用的银行授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);
        // 3.获取同单位银行币别的其他授信数据
        checkBefore();

        urationDTO.setBeginDate(bkration.getBegindate());// 授信开始日期
        urationDTO.setEndDate(uration.getEndDate());// 授信结束日期
        urationDTO.setBankCode(bkration.getBankCode());
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setItemFlag(RationConstant.RAT_COMFLAG_0);
        urationDTO.setType(uration.getType());// 占用的业务类型（系统品种）
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_2);// 综合转非综合
        urationDTO.setItemCode(uration.getItemCode());// 占用的授信品种
        urationDTO.setOldDisCode(uration.getDisCode());

        //3.获取结转后的授信录入信息
        RatBkration rationDTO = rationService.get(urationDTO.getNewRatCode(), true);
        if (ObjectUtils.isEmpty(rationDTO)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        // 新授信明细和原占用单位、品种获取分配明细
        List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(rationDTO.getId(), urationDTO.getCorpId(), urationDTO.getItemFlag(), urationDTO.getItemCode());// 授信分配明细
        if (CollectionUtils.isEmpty(dtos)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        // 检查授信额度是否足够结转
        RatBkrationDisDtlDTO dtlDTO = dtos.get(0);
        //查询出新授信品种中可使用额度 = 已分配金额+被调剂过来的金额-调剂出去的金额-正式占用-冻结金额-虚拟调剂额度-虚拟占用额度-虚拟调剂占用额度
        BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                .subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getVirtualReliefMoney()).subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getReliefAndUrationMoney());
        if (uration.getMoney().compareTo(disMoney) > 0) {// 占用额度大于剩余额度
            // 查询新授信品种中可使用的额度
            throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_LOWER, dtlDTO.getItemName());
        }
        urationDTO.setNewRatCode(rationDTO.getId());// 新的授信编号
        urationDTO.setNewDisCode(dtlDTO.getDisCode());// 新的分配编号

        // 新占用授信品种额度占用
        processRationData(uration, urationDTO, userId);
    }

    /**
     * 功能介绍 :授信非综合转综合。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void changeRationSingle2Comp(String urationIds, String userId) {
        if (StringUtils.isEmpty(urationIds)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_USE_SELECTNULL);
        }
        String[] urationIdArr = urationIds.split(CommonConstant.SEMI_SPLIT);
        for (String urationId : urationIdArr) {
            processSingle2Comp(urationId, userId);
        }
    }

    /**
     * 功能介绍 : 非综合授信结转综合。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processSingle2Comp(String urationId, String userId) {
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        String[] urationArr = urationId.split(CommonConstant.COMMA_SPLIT);
        //1.获得授信占用信息
        RatUration uration = get(urationArr[0], true);
        urationDTO.setId(urationArr[0]);
        urationDTO.setNewRatCode(urationArr[1]);

        if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_NOTSING_ITEMCODE);
        }
        //2.获取结转前银行授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);

        checkBefore();

        urationDTO.setBeginDate(bkration.getBegindate());// 授信开始日期
        urationDTO.setEndDate(uration.getEndDate());// 授信结束日期
        urationDTO.setBankCode(bkration.getBankCode());
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setItemFlag(RationConstant.RAT_COMFLAG_1);
        urationDTO.setType(uration.getType());// 占用的业务类型（系统品种）
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_3);// 非综合转综合
        urationDTO.setRatMode(bkration.getRatMode());
        urationDTO.setItemCode(uration.getItemCode());//占用的授信品种
        urationDTO.setOldDisCode(uration.getDisCode());

        //3.获取结转后的授信录入信息
        RatBkration rationDTO = rationService.get(urationDTO.getNewRatCode(), true);
        if (ObjectUtils.isEmpty(rationDTO)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        // 新授信明细和原占用单位、品种获取分配明细
        List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(rationDTO.getId(), urationDTO.getCorpId(), urationDTO.getItemFlag(), urationDTO.getItemCode());// 授信分配明细
        if (CollectionUtils.isEmpty(dtos)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        List<String> itemCodeList = Lists.newArrayList();
        dtos.forEach(item -> itemCodeList.add(item.getItemCode()));
        if (!itemCodeList.contains(urationDTO.getItemCode())) { // 综合授信比较授信品种是否一致
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCOMPCODE_NOTEXISTS);
        }
        //检查授信额度是否足够结转
        this.validCompRationMoney(urationDTO, dtos, rationDTO.getId());
        //新占用授信品种额度占用
        processRationData(uration, urationDTO, userId);
    }

    /**
     * 功能介绍 :授信相同品种间结转。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void changeSameRationCode(String urationIds, String userId) {
        if (StringUtils.isEmpty(urationIds)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_USE_SELECTNULL);
        }
        String[] urationIdArr = urationIds.split(CommonConstant.SEMI_SPLIT);
        for (String urationId : urationIdArr) {
            processRationChange(urationId, userId);
        }
    }

    /**
     * 功能介绍 : 授信相同品种间结转。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processRationChange(String urationId, String userId) {
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        String[] urationArr = urationId.split(CommonConstant.COMMA_SPLIT);
        //1.获得授信占用信息
        RatUration uration = get(urationArr[0], true);
        urationDTO.setId(urationArr[0]);
        urationDTO.setNewRatCode(urationArr[1]);

        // 2.获得已经占用的银行授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);

        // 3.获取同单位银行币别的其他授信数据
        urationDTO.setType(uration.getType());// 业务类型
        urationDTO.setBeginDate(bkration.getBegindate());// 授信开始日期
        urationDTO.setEndDate(uration.getEndDate());// 占用结束日期
        urationDTO.setBankCode(bkration.getBankCode());
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setItemFlag(uration.getItemFlag());
        urationDTO.setItemCode(uration.getItemCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setRatMode(bkration.getRatMode());
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_1);// 相同品种结转
        urationDTO.setCreditType(uration.getCreditType());// 授信方式
        urationDTO.setOldDisCode(uration.getDisCode());
        checkBefore();

        processCorpRecentRation(urationDTO);
        // 新占用授信品种额度占用
        processRationData(uration, urationDTO, userId);
    }

    /**
     * 判断占用能否结转,并进行结转
     *
     * @param oldBkrationId 被结转的授信录入id
     * @param newBkrationId 结转的授信录入id
     */
    public void addBkrationChange(String oldBkrationId, String newBkrationId, String userId) {
        List<Integer> statuss = new ArrayList<>();
        statuss.add(RationConstant.RAT_USE_STATUS_USE);
        List<RatUrationDisBean> ratUrationDisBeans = this.queryByCodeStatus(oldBkrationId, statuss);
        for (RatUrationDisBean ratUrationDisBean : ratUrationDisBeans) {
            RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();// 被结转数据
            RatUration uration = get(ratUrationDisBean.getId());
            // 2.获得已经占用的银行授信录入信息
            RatBkration bkration = rationService.get(uration.getRatCode(), true);
            urationDTO.setNewRatCode(newBkrationId);
            urationDTO.setType(uration.getType());//业务类型
            urationDTO.setBeginDate(bkration.getBegindate());//授信开始日期
            urationDTO.setEndDate(uration.getEndDate());//占用结束日期
            urationDTO.setBankCode(bkration.getBankCode());
            urationDTO.setCurCode(bkration.getCurCode());
            urationDTO.setCorpId(uration.getCorpId());//占用单位ID
            urationDTO.setRatCode(uration.getRatCode());
            urationDTO.setItemFlag(uration.getItemFlag());
            urationDTO.setItemCode(uration.getItemCode());
            urationDTO.setMoney(uration.getMoney());//占用额度
            urationDTO.setRatMode(bkration.getRatMode());
            urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_1);//相同品种结转
            urationDTO.setCreditType(uration.getCreditType());//授信方式
            urationDTO.setStatus(11);
            urationDTO.setOldDisCode(uration.getDisCode());
            checkBefore();

            processCorpRecentRation(urationDTO);
            // 新占用授信品种额度占用
            processRationData(uration, urationDTO, userId);
        }
    }

    /**
     * 功能介绍 : 处理授信结转业务数据。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processRationData(RatUration uration, RatUrationBeanDTO urationDTO, String userId) {
        RatUrationApply newApply;

        RatUration newUration = new RatUration();
        if (ConfigConstant.RAT.equals(uration.getSyscode())) {// 系统占用类型
            //银行台账
            // 原授信占用申请数据不变，将原授信占用数据转为历史数据
            RatUrationApply apply = applyService.get(uration.getBillId(), true);// 授信业务ID

            newApply = new RatUrationApply();
            BeanUtils.copyProperties(apply, newApply, new String[]{"id"});
            newApply.setApplyCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_APPLYCODE, RationConstant.RAT_URATION_APPLY_CODE));// 授信占用申请编号
            newApply.setStatus(RationConstant.STATUS_DIS_HIS);// 占用历史数据

            if (RationConstant.RAT_CHANGE_TYPE_2.equals(urationDTO.getChangeType())) {// 综合授信转非综合授信
                apply.setItemFlag(RationConstant.RAT_COMFLAG_0);
                newApply.setItemFlag(RationConstant.RAT_COMFLAG_1);
                newApply.setItemCode(null);// 授信品种在综合项
            } else if (RationConstant.RAT_CHANGE_TYPE_3.equals(urationDTO.getChangeType())) {// 非综合转综合
                apply.setItemFlag(RationConstant.RAT_COMFLAG_1);
                newApply.setItemFlag(RationConstant.RAT_COMFLAG_0);
            }
            applyService.saveEntity(newApply);// 保存历史占用申请记录

            // 原授信申请记录关联新的授信占用信息
            apply.setItemCode(uration.getItemCode());
            apply.setRatCode(urationDTO.getNewRatCode());// 结转后新的授信编号
            apply.setDisCode(urationDTO.getNewDisCode());// 结转后新的分配编号
            apply.setUpdateBy(userId);
            apply.setUpdateDate(DateUtils.getDate());
            applyService.updateEntity(apply);

            newUration.setBillId(newApply.getId());
            newUration.setBillNo(newApply.getApplyCode());
            newUration.setRatCode(newApply.getRatCode());//原授信编号

            // 综合品种之间结转还需要需要生成或删除明细
            this.createOrRemoveUrationApplyDtl(uration, urationDTO, apply.getId(), newApply.getId(), newApply.getMoney(), RationConstant.RAT_USE_TYPE_0);
        }

        BeanUtils.copyProperties(uration, newUration, new String[]{"id"});
        newUration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE, RationConstant.RAT_URATION_USE_CODE));//授信占用台账编号
        newUration.setStatus(RationConstant.RAT_USE_STATUS_CLOSE);// 授信结转
        if (RationConstant.RAT_CHANGE_TYPE_2.equals(urationDTO.getChangeType())) {// 综合授信转非综合授信
            uration.setItemFlag(RationConstant.RAT_COMFLAG_0);
            newUration.setItemFlag(RationConstant.RAT_COMFLAG_1);
        } else if (RationConstant.RAT_CHANGE_TYPE_3.equals(urationDTO.getChangeType())) {// 非综合转综合
            uration.setItemFlag(RationConstant.RAT_COMFLAG_1);
            newUration.setItemFlag(RationConstant.RAT_COMFLAG_0);
        }
        newUration.setUpdateBy(userId);
        newUration.setUpdateDate(DateUtils.getDate());
        saveEntity(newUration);//保存历史授信占用台账

        uration.setRatCode(urationDTO.getNewRatCode());// 结转后新的授信编号
        uration.setDisCode(urationDTO.getNewDisCode());// 结转后新的分配编号
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        updateEntity(uration);

        // 综合品种之间结转还需要需要生成或删除明细
        this.createOrRemoveUrationApplyDtl(uration, urationDTO, uration.getId(), newUration.getId(), newUration.getMoney(), RationConstant.RAT_USE_TYPE_1);
        getEntityDao().flush();

        // 更新新占用的授信分配中的占用额度，如果是总额共享则还要更新总额共享的虚拟占用额度
        applyService.updateRationDisMoney(uration, false);//不需要更新冻结额度
        // 更新旧占用的授信分配中的占用额度，如果是总额共享则还要更新总额共享的虚拟占用额度
        applyService.updateOldRationDisMoney(newUration);
    }

    /**
     * 综合品种之间结转还需要需要生成或删除明细
     * 综合转综合，生成占用明细
     * 综合转非综合，删除原占用明细
     * 非综合转综合，生成占用明细
     */
    private void createOrRemoveUrationApplyDtl(RatUration uration, RatUrationBeanDTO urationDTO, String id, String nid, BigDecimal money, String useType) {
        if (RationConstant.RAT_COMFLAG_1.equals(urationDTO.getItemFlag())) {
            //现在是综合明细
            if (RationConstant.RAT_CHANGE_TYPE_1.equals(urationDTO.getChangeType())) {//相同品种结转
                List<RatUrationApplyDtl> applyDtls = applyDtlService.queryRatUrationDtlById(id);
                //相同授信结转重新建占用明细数据
                for (RatUrationApplyDtl ratApplyDtl : applyDtls) {
                    ratApplyDtl.setApplyCode(nid);
                    applyDtlService.updateEntity(ratApplyDtl);
                }
                //新占用明细
                List<RatBkrationDisDetail> disDtlDTOs = disDetailService.getRationDisDtlByDisCode(urationDTO.getNewDisCode(), urationDTO.getItemFlag(), null);//获取授信分配信息
                for (RatBkrationDisDetail dtlDTO : disDtlDTOs) {
                    RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
                    applyDtl.setApplyCode(id);
                    applyDtl.setApplyType(useType);//占用类型
                    applyDtl.setItemFlag(dtlDTO.getItemFlag());
                    applyDtl.setItemOrder(dtlDTO.getItemOrder());
                    applyDtl.setItemCode(dtlDTO.getItemCode());
                    if (uration.getItemCode().equals(dtlDTO.getItemCode())) {
                        applyDtl.setMaxMoney(money);
                        applyDtl.setApplyMoney(BigDecimal.ZERO);
                    } else {
                        applyDtl.setMaxMoney(BigDecimal.ZERO);
                        applyDtl.setApplyMoney(money);
                    }
                    applyDtlService.saveEntity(applyDtl);
                }
            } else if (RationConstant.RAT_CHANGE_TYPE_3.equals(urationDTO.getChangeType())) {
                //非综合转综合
                List<RatBkrationDisDetail> disDtlDTOs = disDetailService.getRationDisDtlByDisCode(urationDTO.getNewDisCode(), urationDTO.getItemFlag(), null);//获取授信分配信息
                for (RatBkrationDisDetail dtlDTO : disDtlDTOs) {
                    RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
                    applyDtl.setApplyCode(id);
                    applyDtl.setApplyType(useType);//占用类型
                    applyDtl.setItemFlag(dtlDTO.getItemFlag());
                    applyDtl.setItemOrder(dtlDTO.getItemOrder());
                    applyDtl.setItemCode(dtlDTO.getItemCode());
                    if (uration.getItemCode().equals(dtlDTO.getItemCode())) {
                        applyDtl.setMaxMoney(money);
                        applyDtl.setApplyMoney(BigDecimal.ZERO);
                    } else {
                        applyDtl.setMaxMoney(BigDecimal.ZERO);
                        applyDtl.setApplyMoney(money);
                    }
                    applyDtlService.saveEntity(applyDtl);
                }
            }
        } else {
            if (RationConstant.RAT_CHANGE_TYPE_2.equals(urationDTO.getChangeType())) {
                //综合授信转非综合授信，非综合没有占用明细
                List<RatUrationApplyDtl> applyDtls = applyDtlService.queryRatUrationDtlById(id);
                for (RatUrationApplyDtl ratApplyDtl : applyDtls) {
                    ratApplyDtl.setApplyCode(nid);
                    applyDtlService.updateEntity(ratApplyDtl);
                }
            }
        }
    }

    /**
     * 功能介绍 : 授信结转前检查。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkBefore() {
        // TODO 对于信用证有付汇或押汇待审批的数据就不能进行结转(以后添加信用证业务再添加)
    }

    /**
     * 功能介绍 : 获取单位最近的授信录入信息并校验结转条件。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void processCorpRecentRation(RatUrationBeanDTO uration) {
        //3.获取结转后的授信录入信息
        RatBkration ration = rationService.get(uration.getNewRatCode());
        if (ObjectUtils.isEmpty(ration)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }

        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            // 新授信明细和原占用单位、品种获取分配明细
            List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(ration.getId(), uration.getCorpId(), uration.getItemFlag(), uration.getItemCode());// 授信分配明细
            if (CollectionUtils.isEmpty(dtos)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
            }
            //单品种项结转单项
            RatBkrationDisDtlDTO dtlDTO = dtos.get(0);
            //查询出新授信品种中可使用额度 = 已分配金额+被调剂过来的金额-调剂出去的金额-正式占用-冻结金额-虚拟调剂额度-虚拟占用额度-虚拟调剂占用额度
            BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                    .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                    .subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getVirtualReliefMoney()).subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getReliefAndUrationMoney());
            //检查授信额度是否足够结转
            if (uration.getMoney().compareTo(disMoney) > 0) {//占用额度大于剩余额度
                //查询新授信品种中可使用的额度
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_LOWER, dtlDTO.getItemName());
            }
            uration.setNewRatCode(ration.getId());//新的授信编号
            uration.setNewDisCode(dtlDTO.getDisCode());//新的分配编号
        } else {
            // 设置综合授信品种明细
            List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(ration.getId(), uration.getCorpId(), uration.getItemFlag(), uration.getItemCode());// 授信分配明细
            if (CollectionUtils.isEmpty(dtos)) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
            }
            List<String> itemCodeList = Lists.newArrayList();
            dtos.forEach(item -> itemCodeList.add(item.getItemCode()));
            if (!itemCodeList.contains(uration.getItemCode())) { // 综合授信比较授信品种是否一致
                throw new ParameterException(RatMessageEnum.CMS_RAT_DISCOMPCODE_NOTEXISTS);
            }
            this.validCompRationMoney(uration, dtos, ration.getId());
        }
    }

    /**
     * 功能介绍 : 综合品种结转是否已包含所有占用品种。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private boolean compareRationDtls(List<RatUrationApplyDtlDTO> applyDtls, Map<String, RatBkrationDisDtlDTO> disMap) {
        boolean flag = true;
        // 如果综合占用的所有品种都存在分配明细，则该综合占用的数据可以进行结转（如果额度足够）
        for (RatUrationApplyDtlDTO applyDtlDTO : applyDtls) {
            String itemCode = applyDtlDTO.getItemCode();// 占用综合品种
            RatBkrationDisDtlDTO disDtlDTO = disMap.get(itemCode);
            if (null == disDtlDTO) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 功能介绍 :检查授信综合品种分配额度是否足够结转。<br>
     * <b>日期：</b>2017年8月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void validCompRationMoney(RatUrationBeanDTO uration, List<RatBkrationDisDtlDTO> dtos, String newRatCode) {
        // 综合品种转综合品种
        for (RatBkrationDisDtlDTO dtlDTO : dtos) {
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {// 综合占用额度的品种项
                //查询出新授信品种中可使用额度 = 已分配金额+被调剂过来的金额-调剂出去的金额-正式占用-冻结金额-虚拟调剂额度-虚拟占用额度-虚拟调剂占用额度
                BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                        .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                        .subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getVirtualReliefMoney()).subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getReliefAndUrationMoney());
                if (uration.getMoney().compareTo(disMoney) > 0) {// 占用额度大于剩余额度
                    // 新授信品种中可使用的额度
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_LOWER, dtlDTO.getItemName());
                }
            }
            // 比较综合品种的最高额度是否足够
            String itemCode = dtlDTO.getItemCode();// 综合授信品种
            if (itemCode.equals(uration.getItemCode())) {
                // 品种剩余最高额度 = 已分配最高金额+被调剂过来的最高金额-调剂出去的金额-正式占用最高金额-冻结最高金额
                BigDecimal mdisMoney = dtlDTO.getMaxMoney().add(dtlDTO.getByReliefMaxMoney()).subtract(dtlDTO.getReliefMaxMoney())
                        .subtract(dtlDTO.getFreezeMaxMoney()).subtract(dtlDTO.getProcessMaxMoney());
                if (uration.getMoney().compareTo(mdisMoney) > 0) {// 占用额度大于剩余最高额度
                    // 新授信品种中可使用的额度
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MAXMONEY_LOWER, dtlDTO.getItemName());
                }
            }
            uration.setNewRatCode(newRatCode);//新的授信编号
            uration.setNewDisCode(dtlDTO.getDisCode());//新的分配编号
        }
    }

    /**
     * 功能介绍 : 授信占用暂存后删除。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.deleteUration(uration, urationDTO.getUserId());
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.deleteUration(uration, urationDTO.getUserId());
            }
        }
    }

    /**
     * 功能介绍 : 授信占用暂存后直接提交占用冻结额度。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void submitUseRation(RatUrationDTO urationDTO) {
        RatUration uration = null;
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            uration = get(urationDTO.getUrationId(), true);
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
            }
        }
        if (null != uration) {
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 额度冻结
            uration.setUpdateBy(urationDTO.getUserId());
            uration.setUpdateDate(DateUtils.getDate());
            if (null == urationDTO.getBillMoney()) {
                uration.setBillMoney(urationDTO.getMoney());
            } else {
                uration.setBillMoney(urationDTO.getBillMoney());
            }
            updateEntity(uration);
            getEntityDao().flush();// 刷新缓存，保存占用数据
            // 冻结授信占用额度
            if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {
                applyService.updateRationDisMoney(uration, true);
            }
        }
    }

    /**
     * 功能介绍 : 授信占用打回，还原冻结额度。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void backUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.backUration(uration, urationDTO.getUserId());
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.backUration(uration, urationDTO.getUserId());
            }
        }
        getEntityDao().flush();// 刷新提交，授信相关占用及时获取数据
    }

    /**
     * 功能介绍 : 授信占用后再打回，还原占用额度。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void preBackUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放，先还原授信占用额度，再在审占用额度
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.releaseUration(uration, urationDTO.getUserId());
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 额度在审占用
            applyService.updateRationDisMoney(uration, true);
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.releaseUration(uration, urationDTO.getUserId());
                uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 额度在审占用
                applyService.updateRationDisMoney(uration, true);
            }
        }
    }

    /**
     * 功能介绍 : 授信占用通过。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.passUration(uration, urationDTO.getUserId());
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.passUration(uration, urationDTO.getUserId());
            }
        }
    }

    /**
     * 功能介绍 : 释放已占用的授信。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void releaseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        // 新增加授信占用信息，状态为已释放
        RatUration uration = null;
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            uration = get(urationDTO.getUrationId(), true);
        } else {
            // 根据系统种类业务ID查找占用的数据
            List<RatUration> uList = queryUrationByBillId(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
            }
        }
        // 增加对释放授信数据的判断，如果没有需要释放的授信则不做任何操作 update by liusy
        if (uration != null) {
            // 授信方式为不循环授信,且是否释放不循环授信为否，不释放占用的额度
            if (RationConstant.RAT_RECYCLE_NO.equals(uration.getCreditType()) && !urationDTO.isReleaseNonRecycleRat()) {
                return;
            }
            RatUration newUration = new RatUration();
            BeanUtils.copyProperties(uration, newUration, "id", "urationCode");
            List<RatUrationApplyDtlDTO> oriApplyDtls = new ArrayList<RatUrationApplyDtlDTO>();
            if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
                oriApplyDtls = applyDtlService.queryRatUrationById(uration.getId());// 综合占用最高额度
                List<RatUrationDtlDTO> applyDtls = BeanUtils.copyListProperties(oriApplyDtls, RatUrationDtlDTO.class);
                urationDTO.setApplyDtls(applyDtls);
            }
            if (urationDTO.isAllRelease()) {// 如果是全部释放
                urationDTO.setMoney(uration.getMoney());// 释放额度为原占用额度
            }
            // urationDTO.setMoney(uration.getMoney());
            // setOpenMoney(newUration, urationDTO);//设置敞口额度
            // 设置授信释放金额
            if (uration.getInitCurCode() != null && uration.getCurCode() != null && uration.getInitCurCode().equals(uration.getCurCode())) {// 判断授信币种与业务币种是否一致
                newUration.setMoney(urationDTO.getMoney());
            } else {
                newUration.setMoney(urationDTO.getMoney().multiply(uration.getExchangeRate()));
            }
            // 如果是最后一笔释放,则释放全部额度
            BigDecimal uMoney = uration.getMoney();// 原占用额度
            BigDecimal newUMoney = newUration.getMoney();// 新释放额度
            BigDecimal leftMoney = uMoney.subtract(newUMoney);// 剩余占用额度
            if (leftMoney.compareTo(BigDecimal.ZERO) <= 0) {
                newUration.setMoney(uration.getMoney());// 释放额度为原占用额度
                newUration.setOpenMoney(uration.getOpenMoney());// 释放敞口额度=原占用敞口额度
                uration.setMoney(BigDecimal.ZERO);// 剩余占用额度
                uration.setOpenMoney(BigDecimal.ZERO);
                uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 已释放
                update(uration);
            } else {
                if (urationDTO.isAllRelease()) {// 如果是全部释放
                    newUration.setMoney(uration.getMoney());// 释放额度为原占用额度
                    newUration.setOpenMoney(uration.getOpenMoney());// 释放敞口额度=原占用敞口额度
                    uration.setMoney(BigDecimal.ZERO);// 剩余占用额度
                    uration.setOpenMoney(BigDecimal.ZERO);
                    uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 已释放
                    update(uration);
                } else {// 释放部分的情况
                    uration.setMoney(uration.getMoney().subtract(newUration.getMoney()));// 减去已还额度
                    uration.setOpenMoney(uration.getOpenMoney().subtract(newUration.getOpenMoney()));
                    update(uration);
                }
            }
            newUration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            newUration.setPreMoney(urationDTO.getMoney());// 折算前金额
            newUration.setCreateBy(urationDTO.getUserId());
            newUration.setCreateDate(DateUtils.getDate());
            saveOrUpdateUration(newUration);// 保存还款的释放额度
            if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
                for (RatUrationApplyDtlDTO oriApplyDtl : oriApplyDtls) {
                    RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
                    BeanUtils.copyProperties(oriApplyDtl, applyDtl, "id");
                    applyDtl.setApplyType(RationConstant.RAT_USE_TYPE_1);// 占用类型-业务表
                    applyDtl.setApplyCode(newUration.getId());
                    if (oriApplyDtl.getMaxMoney().compareTo(BigDecimal.ZERO) > 0) {
                        applyDtl.setMaxMoney(newUration.getMoney());// 授信品种的占用额度
                        applyDtl.setApplyMoney(BigDecimal.ZERO);// 授信品种的占用额度
                    } else {
                        applyDtl.setMaxMoney(BigDecimal.ZERO);// 授信品种的占用额度
                        applyDtl.setApplyMoney(newUration.getMoney());// 授信品种的占用额度
                    }
                    applyDtlService.saveEntity(applyDtl);
                    // 更新原占用额度为释放后的额度，原额度减掉本次释放额度
                    RatUrationApplyDtl destApplyDtl = applyDtlService.get(oriApplyDtl.getId(), true);
                    destApplyDtl.setMaxMoney(oriApplyDtl.getMaxMoney().subtract(applyDtl.getMaxMoney()));
                    destApplyDtl.setApplyMoney(oriApplyDtl.getApplyMoney().subtract(applyDtl.getApplyMoney()));
                    applyDtlService.updateEntity(destApplyDtl);
                }
            }
            getEntityDao().flush();// 刷新缓存,保证后面更新授信额度时能取到占用额度
            // 根据新的占用授信额度释放额度
            applyService.releaseUration(newUration, urationDTO.getUserId());
        }
    }

    /**
     * 功能介绍 : 授信占用暂存，不占用授信额度。<br>
     * <b>日期：</b>2017年9月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void tempUseRation(RatUrationDTO urationDTO, Integer urationStatus) {
        String corpId = urationDTO.getCorpId();
        String ratCode = urationDTO.getRatCode();
        // 查询已审批通过的授信分配信息
        RatBkrationDisListDTO disDTO = disService.getCorpBkrationDisListDTO(ratCode, corpId);
        if (null == disDTO) {
            CorpDTO corpDTO = corpService.get(corpId);
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_NOTEXIST,
                    corpDTO.getCode() + corpDTO.getName());
        }
        RatUration uration = new RatUration();
        editUrationInfo(urationDTO, uration, disDTO, urationStatus);
    }
    /**
     * 功能介绍 : 设置保证金和敞口额度。<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    /*
     * private void setOpenMoney(RatUration uration, RatUrationDTO urationDTO) { //是否包含保证金 RatBkration bkration =
     * rationService.get(uration.getRatCode(), true); String includeBailMoney = bkration.getIncludeBailmoney(); if
     * (EmptyUtils.isBlank(includeBailMoney)) { includeBailMoney = RationConstant.RAT_COMMON_Y;//默认包含保证金 } BigDecimal
     * uMoney = urationDTO.getMoney();// 占用额度 uration.setConvertPreMoney(uMoney);//业务金额
     *
     * // 获取授信参数 ConfigDTO config = rationService.getStartOpenRat();// 敞口 if
     * (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {//启用敞口
     * urationDTO.setBailRate(BigDecimal.ZERO);//不启用敞口，则占用额度就是授信额度 } else { if(null == urationDTO.getBailRate()) {
     * urationDTO.setBailRate(BigDecimal.ZERO); } }
     *
     * if(RationConstant.RAT_COMMON_N.equals(includeBailMoney)) {//不包含 if(null == urationDTO.getBondMoney()){
     * urationDTO.setBondMoney(BigDecimal.ZERO); } //占用金额 = 占用额度*(1-保证金比例/100) = 占用额度 - 保证金比例 if
     * (!urationDTO.isReleaseFlag()) {//录入占用取保证金，释放根据保证金比例释放 uMoney = uMoney.subtract(urationDTO.getBondMoney()); } else
     * { uMoney = uMoney.multiply(BigDecimal.ONE.subtract(urationDTO.getBailRate().divide(BigDecimal.valueOf(1L), 2,
     * BigDecimal.ROUND_HALF_UP))); } }
     * uration.setMoney(uMoney.multiply(uration.getExchangeRate()).setScale(2,BigDecimal.ROUND_HALF_UP));//实际占用金额
     * //获取授信保证金比例和利率 List<RatBkRationDtlVm> bkrationDtlDTOs =
     * rationDtlService.getBkrationDtlDTOByRatCode(uration.getRatCode(), null); String itemCode =
     * uration.getItemCode();//授信品种
     *
     * BigDecimal assurepercent = BigDecimal.ZERO;//保证金比例 BigDecimal interestrate = BigDecimal.ZERO;//利率 String itemFlag
     * = uration.getItemFlag();//授信品种 for (RatBkRationDtlVm bkrationDtlDTO : bkrationDtlDTOs) { if
     * (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) { if (bkrationDtlDTO.getItemCode().equals(itemCode)) {
     * assurepercent = bkrationDtlDTO.getAssurepercent();//保证金比例 break; } } else { if
     * (RationConstant.RAT_COMP_SIGN_1.equals(bkrationDtlDTO.getCompSign())) {//综合使用额度 interestrate =
     * bkrationDtlDTO.getInterestrate();//利率 break; } } } BigDecimal openMoney = BigDecimal.ZERO; openMoney =
     * uration.getMoney().multiply(BigDecimal.ONE .subtract(urationDTO.getBailRate().divide(BigDecimal.valueOf(1L), 2,
     * BigDecimal.ROUND_HALF_UP)));
     */
    /*
     * if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) { //授信额度*（1-保证金比例/100） openMoney =
     * uration.getMoney().multiply(BigDecimal.ONE .subtract(assurepercent.divide(BigDecimal.valueOf(1L), 2,
     * BigDecimal.ROUND_HALF_UP))); } else { //授信额度*（1-利率/费率/100） openMoney = uration.getMoney().multiply(BigDecimal.ONE
     * .subtract(interestrate.divide(BigDecimal.valueOf(1L), 2, BigDecimal.ROUND_HALF_UP))); }
     *//*
     * uration.setOpenMoney(openMoney); }
     */

    /**
     * 功能介绍 : 授信占用保存更新。<br>
     * <b>日期：</b>2017年9月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void editUrationInfo(RatUrationDTO urationDTO, RatUration uration, RatBkrationDisListDTO disDTO,
                                 Integer urationStatus) {
        String itemCode = urationDTO.getRatItemCode();
        BeanUtils.copyProperties(urationDTO, uration);
        uration.setDisCode(disDTO.getId());// 授信占用编号
        uration.setSyscode(urationDTO.getSysCode());// 系统占用模块
        uration.setRatType(RationConstant.RAT_TYPE_0);// 银行授信
        uration.setItemFlag(RationConstant.RAT_COMFLAG_0);// 单品种授信
        uration.setItemCode(itemCode);
        if (urationDTO.getBillName() != null) {
            String billName = urationDTO.getBillName().replace(" ", "");// 去空格处理
            if (StringUtils.isNotEmpty(urationDTO.getBillName())) {
                uration.setBillName(billName);
            } else {
                uration.setBillName(null);
            }
        }
        if (null == urationDTO.getBillMoney()) {
            // 当业务金额为空时，则设置占用授信金额为业务金额
            uration.setBillMoney(uration.getMoney());
        }
        RatItemCustom itemCustom = itemCustomService.get(itemCode, true);// 授信品种信息
        if (null != itemCustom) {
            uration.setType(itemCustom.getSysItemCode());// 系统授信品种
        }
        uration.setUrationCode(
                rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE, RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
        uration.setInitCurCode(urationDTO.getCurCode() != null ? urationDTO.getCurCode() : urationDTO.getInitCurCode());// 业务币别
        // 获取授信的币别
        RatBkrationDis dis = disService.get(disDTO.getId(), true);// 授信分配信息
        RatBkration ration = rationService.get(dis.getRatCode(), true);// 授信录入信息
        uration.setCurCode(ration.getCurCode());// 授信币别
        /*
         * BigDecimal curRate = rateAdjustService.getExchangeRateBy(urationDTO.getCurCode(),
         * urationDTO.getBeginDate());//获取币别汇率 uration.setExchangeRate(curRate);//汇率
         */
        uration.setAssurepercent(urationDTO.getBailRate());
        uration.setBondMoney(urationDTO.getBondMoney());
        uration.setStatus(urationStatus);// 授信占用的状态
        uration.setCreateBy(urationDTO.getUserId());
        uration.setCreateDate(DateUtils.getDate());
        List<RatUrationDtlDTO> applyDtls = urationDTO.getApplyDtls();
        if (EmptyUtils.isEmptyList(applyDtls)) {
            // 说明是单品种占用
            uration.setItemFlag(RationConstant.RAT_COMFLAG_0);// 单品种授信
        } else {
            uration.setItemFlag(RationConstant.RAT_COMFLAG_1);// 综合品种授信
        }
        /**
         * 折算前金额，未扣除保证金
         */
        uration.setPreMoney(urationDTO.getMoney());
        // setOpenMoney(uration, urationDTO);//设置敞口额度
        /**
         * 折算后金额，如启用授信敞口，则扣除保证金,占用金额扣除保证金
         */
        this.getRatUrationMoney(urationDTO, uration);// 设置授信正式占用额度
        if (EmptyUtils.isEmptyList(applyDtls)) {
            // 说明是单品种占用
            uration.setItemFlag(RationConstant.RAT_COMFLAG_0);// 单品种授信
            // uration.setItemCode(itemCode);
            singleRatUration(uration.getMoney(), uration, disDTO);
            uration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            saveOrUpdateUration(uration);
        } else {// 综合占用
            List<RatUrationApplyDtl> newApplyDtls = compRatUration(urationDTO, uration.getMoney(), uration, disDTO);
            uration.setItemFlag(RationConstant.RAT_COMFLAG_1);// 综合品种授信
            // uration.setItemCode(null);
            for (RatUrationApplyDtl urationApplyDtl : newApplyDtls) {// 设置占用品种的授信方式（是否循环授信）
                if (uration.getItemCode().equals(urationApplyDtl.getItemCode())) {
                    uration.setCreditType(urationApplyDtl.getCreditType());
                }
            }
            uration.setUrationCode(rationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            saveOrUpdateUration(uration);
            List<RatUrationApplyDtlDTO> applyDtlDTOs = BeanUtils.copyListProperties(newApplyDtls,
                    RatUrationApplyDtlDTO.class);
            applyService.saveRationApplyDtl(uration.getDisCode(), uration.getId(), RationConstant.RAT_USE_TYPE_1,
                    applyDtlDTOs);
            /*
             * for (RatUrationApplyDtl applyDtl : newApplyDtls) { applyDtl.setApplyCode(uration.getId());
             * applyDtl.setMaxMoney(uration.getMoney());//授信品种的占用额度 applyDtl.setApplyMoney(BigDecimal.ZERO);//授信品种的占用额度
             * applyDtlService.saveEntity(applyDtl); }
             */
        }
        urationDTO.setUrationId(uration.getId());// 返回占用ID
    }

    /**
     * 功能介绍 : 授信占用冻结。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void useRation(RatUrationDTO urationDTO, Integer urationStatus) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        RatUration uration = null;
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            uration = get(urationDTO.getUrationId(), true);
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
            }
        }
        if (null == uration) {
            tempUseRation(urationDTO, urationStatus);
            uration = get(urationDTO.getUrationId(), true);
        } else {
            // 先删除明细再冻结占用
            // 删除原占用综合数据
            if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
                applyDtlService.deleteRatUrationDtlByCode(uration.getId());
            }
            // 查询已审批通过的授信分配信息
            RatBkrationDisListDTO disDTO = disService.getCorpBkrationDisListDTO(urationDTO.getRatCode(),
                    urationDTO.getCorpId());
            if (null == disDTO) {
                CorpDTO corpDTO = corpService.get(urationDTO.getCorpId());
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_NOTEXIST,
                        corpDTO.getCode() + corpDTO.getName());
            }
            editUrationInfo(urationDTO, uration, disDTO, urationStatus); //授信占用保存更新
        }
        getEntityDao().flush();// 刷新缓存，保存占用数据
        // 冻结授信占用额度
        if (RationConstant.RAT_USE_STATUS_FREEZE.equals(urationStatus)) {
            applyService.updateRationDisMoney(uration, true);
        }
    }

    /**
     * 功能介绍 : 单品种占用额度检查。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void singleRatUration(BigDecimal uMoney, RatUration uration, RatBkrationDisListDTO disDTO) {
        // 查询授信品种的具体保证金比例
        List<RatBkrationDisDtlDTO> singRationDtls = disDTO.getRationDtls();
        Map<String, RatBkrationDisDtlDTO> rationMap = Maps.uniqueIndex(singRationDtls,
                new Function<RatBkrationDisDtlDTO, String>() {
                    public String apply(RatBkrationDisDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        RatBkrationDisDtlDTO dtlDTO = rationMap.get(uration.getItemCode());// 占用的分配明细
        if (null != dtlDTO) {
            // 设置授信方式
            uration.setCreditType(dtlDTO.getCreditType());
            // 比较授信额度是否足够
            // 品种剩余最高额度 = 已分配最高金额+被调剂过来的最高金额-调剂出去的金额-正式占用-冻结金额
            BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                    .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                    .subtract(dtlDTO.getSuperProcessMoney());
            BigDecimal sub = uMoney.subtract(disMoney);
            if (sub.compareTo(BigDecimal.ZERO) == 1) {// 占用额度大于剩余额度
                // 新授信品种中可使用的额度
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_ENOUGH, dtlDTO.getItemName());
            }
        }
    }

    /**
     * 功能介绍 : 综合品种占用额度检查。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private List<RatUrationApplyDtl> compRatUration(RatUrationDTO urationDTO, BigDecimal uMoney, RatUration uration,
                                                    RatBkrationDisListDTO disDTO) {
        // 综合占用的额度默认为业务金额
        List<RatUrationDtlDTO> urationDtlDTOs = urationDTO.getApplyDtls();
        for (RatUrationDtlDTO dtlDTO : urationDtlDTOs) {
            if (dtlDTO.getItemCode().equals(uration.getItemCode())) {// 页面选择的授信品种
                dtlDTO.setItemMoney(uMoney);
            }
        }
        Map<String, RatUrationDtlDTO> urationMap = Maps.uniqueIndex(urationDtlDTOs,
                new Function<RatUrationDtlDTO, String>() {
                    @Override
                    public String apply(RatUrationDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        // 查询授信品种的具体保证金比例
        List<RatBkrationDisDtlDTO> compRationDtls = disDTO.getRationDtls();
        List<RatUrationApplyDtl> applyDtls = new ArrayList<RatUrationApplyDtl>();
        // 记录综合授信的总额度
        BigDecimal allMoney = null;
        for (RatBkrationDisDtlDTO dtlDTO : compRationDtls) {
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种跳过
                continue;
            }
            if (EmptyUtils.isEmpty(allMoney)) {
                allMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()); // 已分配金额+被调剂过来的金额-调剂出去的金额
            }
            // 设置授信方式
            uration.setCreditType(dtlDTO.getCreditType());
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {// 综合占用额度的品种项
                // 查询出新授信品种中可使用额度 = 已分配金额+被调剂过来的金额-调剂出去的金额-正式占用-冻结金额
                BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                        .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                        .subtract(dtlDTO.getSuperProcessMoney());
                BigDecimal sub = uMoney.subtract(disMoney);
                if (sub.compareTo(BigDecimal.ZERO) == 1) {// 占用额度大于剩余额度
                    // 新授信品种中可使用的额度
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_ENOUGH,
                            dictService.get("RatModel.ItemFlag", RationConstant.RAT_COMFLAG_1));
                }
            }
            // 比较综合品种的最高额度是否足够
            String itemCode = dtlDTO.getItemCode();// 综合授信品种
            // 品种剩余最高额度 = 已分配最高金额+被调剂过来的最高金额-调剂出去的金额-正式占用-冻结金额
            BigDecimal mdisMoney = dtlDTO.getMaxMoney().add(dtlDTO.getByReliefMaxMoney())
                    .subtract(dtlDTO.getReliefMaxMoney()).subtract(dtlDTO.getFreezeMaxMoney())
                    .subtract(dtlDTO.getProcessMaxMoney());
            // 总剩余最高额度
            BigDecimal bdisMoney = allMoney.subtract(dtlDTO.getFreezeMaxMoney()).subtract(dtlDTO.getProcessMaxMoney())
                    .subtract(dtlDTO.getVirtualMaxMoney());
            // 当前品种剩余可用 与 综合授信剩余可用 取较小的值
            BigDecimal disMoney = mdisMoney.compareTo(bdisMoney) <= 0 ? mdisMoney : bdisMoney;
            RatUrationDtlDTO applyDtlDTO = urationMap.get(itemCode);
            if (null != applyDtlDTO) {
                // 设置授信方式
                uration.setCreditType(dtlDTO.getCreditType());
                BigDecimal money = applyDtlDTO.getItemMoney();// 占用额度
                BigDecimal sub = money.subtract(disMoney);
                if (sub != null && sub.compareTo(BigDecimal.ZERO) == 1) {// 占用额度大于剩余额度
                    // 新授信品种中可使用的额度
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MAXMONEY_ENOUGH, dtlDTO.getItemName());
                }
                RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
                applyDtl.setItemFlag(RationConstant.RAT_COMFLAG_1);
                applyDtl.setApplyType(RationConstant.RAT_USE_TYPE_1);// 占用类型-业务表
                applyDtl.setItemOrder(dtlDTO.getItemOrder());
                applyDtl.setItemCode(itemCode);
                applyDtl.setMaxMoney(money);
                applyDtl.setCreditType(dtlDTO.getCreditType());// 设置授信方式
                applyDtls.add(applyDtl);
            }
        }
        return applyDtls;
    }

    /**
     * 功能介绍 : 授信挂接数据查询。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisDTO> queryCorpDisRation(UrationParamDTO paramDTO, RatUrationDTO urationDTO) {
        Date beginDate = paramDTO.getBeginDate();
        if (null == beginDate) {
            beginDate = DateUtils.getDate();
            paramDTO.setBeginDate(beginDate);
        }
        String isRmbRat = paramDTO.getIsRmbRat();// 折合本位币
        if (RationConstant.RAT_COMMON_Y.equals(isRmbRat)) {
            // 查询本位币
            CorpDTO corpDTO = corpService.get(paramDTO.getCorpId());
            paramDTO.setCurCode(corpDTO.getCurId());// 本位币
        }
        List<RatUrationDisBean> disBeans = ratUrationDao.queryCorpRatDisration(paramDTO);
        paramDTO.setRatCode(urationDTO.getRatCode());
        // 对授信明细进行分组
        List<RatUrationDisDTO> disDTOs = new ArrayList<RatUrationDisDTO>();
        Map<String, RatUrationDisDTO> disMap = new HashMap<String, RatUrationDisDTO>();
        String itemCode = paramDTO.getItemCode();// 页面选择的授信品种
        if (!StringUtils.isEmpty(itemCode)) {
            itemCode = itemCode.trim();// 非空限制
        }
        if (EmptyUtils.isEmptyList(disBeans)) {
            disBeans = new ArrayList<>();
        }

        //存储每组综合授信的总额度
        Map<String, BigDecimal> compMoney = new HashMap<>();
        disBeans.forEach(item -> {
            String key = item.getRatCode() + "_" + item.getCorpId();
            if (RationConstant.RAT_COMFLAG_1.equals(item.getItemFlag()) && RationConstant.RAT_COMP_SIGN_1.equals(item.getCompSign())) {
                if (EmptyUtils.isEmpty(compMoney.get(key))) {
                    compMoney.put(key, item.getMoney());
                }
            }
        });

        for (RatUrationDisBean disBean : disBeans) {
            //去掉单品种授信额度为0的数据
            if (disBean.getItemFlag().equals(RationConstant.RAT_COMFLAG_0) && disBean.getMoney().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            //去掉综合品种最高额度为0的数据
            if (disBean.getItemFlag().equals(RationConstant.RAT_COMFLAG_1) && disBean.getMaxMoney().compareTo(BigDecimal.ZERO) <= 0) {// 去掉综合品种最高额度为0的数据
                continue;
            }

            String ratCode = disBean.getRatCode();
            RatUrationDisDTO disDTO = disMap.get(ratCode);
            if (null == disDTO) {
                disDTO = new RatUrationDisDTO();
                BeanUtils.copyProperties(disBean, disDTO);
                disDTO.setRationDlts(new ArrayList<RatUrationDisDtlDTO>());
                disMap.put(ratCode, disDTO);
            }
            // 在审占用额度+追减
            BigDecimal approveMoney = disBean.getFreezeMoney();
            disBean.setApproveMoney(approveMoney);// 在审占用
            disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney)
                    .subtract(disBean.getVirtualRelief()).subtract(disBean.getVirtualUration())
                    .subtract(disBean.getReliefAndUrationMoney()));// 可用额度
            // 剩余额度 = 在审+可用(包含虚拟在途占用和虚拟在途调剂，这些不能在真实在途占用中)
            disBean.setLeftMoney(disBean.getApproveMoney().add(disBean.getOverPlusMoney())
                    .add(disBean.getVirtualRelief()).add(disBean.getVirtualUration()));
            double ratOccupyRatio = 0;
            // 综合品种
            if (RationConstant.RAT_COMFLAG_1.equals(disBean.getItemFlag())) {
                String key = disBean.getRatCode() + "_" + disBean.getCorpId();
                RatUrationDisDtlDTO disDtlDTO = new RatUrationDisDtlDTO();
                BeanUtils.copyProperties(disBean, disDtlDTO);
                disDTO.getRationDlts().add(disDtlDTO);

                // 总授信额度减去总占用及再审占用
                BigDecimal bOverPlusMoney = compMoney.get(key).subtract(disBean.getUseMaxMoney()).subtract(disBean.getFreezeMaxMoney())
                        .subtract(disBean.getVirtualMaxMoney()).subtract(disBean.getVirtualUrationMaxMoney());

                // 当前品种剩余可用额度
                BigDecimal sOverPlusMoney = disBean.getMaxMoney().subtract(disBean.getUseMaxMoney()).subtract(disBean.getFreezeMaxMoney());
                // 当前品种剩余可用 与 综合授信剩余可用 取较小的值
                BigDecimal trueMoney = bOverPlusMoney.compareTo(sOverPlusMoney) <= 0 ? bOverPlusMoney : sOverPlusMoney;

                // 设置综合各品种的额度情况,综合授信的各额度使用情况为最高额度的使用情况
                RatUrationDtlDTO rationDtl = new RatUrationDtlDTO();
                rationDtl.setItemCode(disBean.getItemCode());
                rationDtl.setItemName(disBean.getItemName());
                rationDtl.setItemMoney(trueMoney);// 最高额度 = 分配最高 - 最高占用 - 冻结 - 在审
                rationDtl.setCreditType(disBean.getCreditType());
                disDtlDTO.addRationDtl(rationDtl);

                BeanUtils.copyProperties(disBean, disDtlDTO);
                disDtlDTO.setMoney(disBean.getMaxMoney());// 综合最高额度
                disDtlDTO.setUseMoney(disBean.getUseMaxMoney());// 占用额度
                disDtlDTO.setApproveMoney(disBean.getFreezeMaxMoney());// 在审占用

                disDtlDTO.setOverPlusMoney(trueMoney);// 可用额度
                disDtlDTO.setLeftMoney(disDtlDTO.getApproveMoney().add(disDtlDTO.getOverPlusMoney()));// 剩余额度 = 在审+可用
                ratOccupyRatio = ((disDtlDTO.getApproveMoney().add(disDtlDTO.getUseMoney()))
                        .divide(disDtlDTO.getMoney(), 4, BigDecimal.ROUND_HALF_UP)).doubleValue();
                disDtlDTO.setRatOccupyRatio(ratOccupyRatio);
                // 设置授信比例
                BigDecimal percent = ratUrationDao.getAssurepercentByRatCode(ratCode, disBean.getItemCode());
                if (percent.compareTo(new BigDecimal(0)) == 1) {
                    disDtlDTO.setAssurepercent(percent);
                }
            } else {
                ratOccupyRatio = ((disBean.getApproveMoney().add(disBean.getUseMoney())).divide(disBean.getMoney(), 4, BigDecimal.ROUND_HALF_UP)).doubleValue();
                disBean.setRatOccupyRatio(ratOccupyRatio);
                RatUrationDisDtlDTO disDtlDTO = new RatUrationDisDtlDTO();
                BeanUtils.copyProperties(disBean, disDtlDTO);
                // 设置授信比例
                BigDecimal percent = ratUrationDao.getAssurepercentByRatCode(ratCode, disBean.getItemCode());
                if (percent.compareTo(new BigDecimal(0)) == 1) {
                    disDtlDTO.setAssurepercent(percent);
                }
                disDTO.addRationDtl(disDtlDTO);
            }
        }
        // 判断是否存在指定品种
        for (Entry<String, RatUrationDisDTO> entry : disMap.entrySet()) {
            RatUrationDisDTO disDTO = entry.getValue();
            if (EmptyUtils.isEmpty(itemCode)) {// 未选择品种时
                setUrationDisDTO(disDTO);
                disDTOs.add(disDTO);
            } else {
                // 检查是否包含授信品种
                RatUrationDisDtlDTO disDtlDTO = checkRationItemCode(disDTO, itemCode);
                if (null != disDtlDTO) {
                    disDTO.setInitRationDlts(disDtlDTO);
                    disDTOs.add(disDTO);
                }
            }
        }
        // 过滤可用额度不为0的数据
        disDTOs.forEach(dto -> {
            List<RatUrationDisDtlDTO> rationDlts = dto.getRationDlts();
            rationDlts = rationDlts.stream().filter(item -> null != item.getOverPlusMoney() && item.getOverPlusMoney().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            dto.setRationDlts(rationDlts);
        });
        double ratOccupiedRation = 0;// 已占用比列
        for (RatUrationDisDTO disDTO : disDTOs) {
            BigDecimal ratMoneyTotal = new BigDecimal(0);// 授信总额
            BigDecimal uratMoneyTotal = new BigDecimal(0);// 占用金额+在审
            List<RatUrationDisDtlDTO> rationDtls = disDTO.getRationDlts();
            if (!EmptyUtils.isEmptyList(rationDtls)) {
                boolean isRatComFlag = false;
                for (RatUrationDisDtlDTO disDtlDTO : rationDtls) {
                    if (RationConstant.RAT_COMFLAG_0.equals(disDtlDTO.getItemFlag())) {
                        ratMoneyTotal = ratMoneyTotal.add(disDtlDTO.getMoney());
                        uratMoneyTotal = uratMoneyTotal.add(disDtlDTO.getApproveMoney()).add(disDtlDTO.getUseMoney());
                        disDtlDTO.setRationSize(disDtlDTO.getRationSize() + 1);
                    } else {
                        if (!isRatComFlag) {
                            ratMoneyTotal = ratMoneyTotal.add(disDtlDTO.getMoney());
                            isRatComFlag = true;
                        }
                        uratMoneyTotal = uratMoneyTotal.add(disDtlDTO.getApproveMoney()).add(disDtlDTO.getUseMoney());
                        disDtlDTO.setRationSize(disDtlDTO.getRationSize() + 1);
                    }
                }
                ratOccupiedRation = uratMoneyTotal.divide(ratMoneyTotal, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                disDTO.setRatOccupiedRation(ratOccupiedRation);
                disDTO.setRationSize(disDTO.getRationDlts().size());
            }
        }
        setSelectRationItemMoney(urationDTO, disDTOs);
        return disDTOs;
    }

    /**
     * 功能介绍 ： 设置综合品种明细每条品种的额度都可以选择。<br>
     * <b>日期：</b>2017年9月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setUrationDisDTO(RatUrationDisDTO disDTO) {
        List<RatUrationDisDtlDTO> disDtlDTOs = new ArrayList<RatUrationDisDtlDTO>();
        List<RatUrationDisDtlDTO> rationDtls = disDTO.getRationDlts();
        for (RatUrationDisDtlDTO disDtlDTO : rationDtls) {
            if (RationConstant.RAT_COMFLAG_0.equals(disDtlDTO.getItemFlag())) {
                disDtlDTOs.add(disDtlDTO);
            } else {
                List<RatUrationDtlDTO> rationDtlDTOs = disDtlDTO.getRationDlts();
                for (RatUrationDtlDTO dtlDTO : rationDtlDTOs) {
                    RatUrationDisDtlDTO newDtlDTO = new RatUrationDisDtlDTO();
                    BeanUtils.copyProperties(disDtlDTO, newDtlDTO, "rationDlts");
                    newDtlDTO.addRationDtl(dtlDTO);
                    disDtlDTOs.add(newDtlDTO);
                }
            }
        }
        disDTO.setRationDlts(disDtlDTOs);
    }

    /**
     * 功能介绍 : 设置综合品种已录入额度。<br>
     * <b>日期：</b>2017年9月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setSelectRationItemMoney(RatUrationDTO urationDTO, List<RatUrationDisDTO> disDTOs) {
        if (null == urationDTO || EmptyUtils.isEmpty(urationDTO.getRatCode())) {// 未选择任何授信记录
            return;
        }
        for (RatUrationDisDTO disDTO : disDTOs) {
            if (disDTO.getRatCode().equals(urationDTO.getRatCode())) {
                List<RatUrationDisDtlDTO> rationDlts = disDTO.getRationDlts();
                if (EmptyUtils.isEmptyList(rationDlts)) {
                    continue;
                }
                List<RatUrationDtlDTO> applyDtls = urationDTO.getApplyDtls();
                Map<String, RatUrationDtlDTO> applyMap = null;
                if (!EmptyUtils.isEmptyList(applyDtls)) {// 综合品种才设置
                    applyMap = Maps.uniqueIndex(applyDtls, new Function<RatUrationDtlDTO, String>() {
                        @Override
                        public String apply(RatUrationDtlDTO input) {
                            return input.getItemCode();
                        }
                    });
                    for (RatUrationDisDtlDTO disDtlDTO : rationDlts) {
                        if (RationConstant.RAT_COMP_SIGN_1.equals(disDtlDTO.getItemFlag())) {// 综合品种
                            List<RatUrationDtlDTO> ratDtlDTOs = disDtlDTO.getRationDlts();
                            if (EmptyUtils.isEmptyList(ratDtlDTOs)) {
                                continue;
                            }
                            for (RatUrationDtlDTO dto : ratDtlDTOs) {
                                String itemCode = dto.getItemCode();
                                RatUrationDtlDTO urationDtlDTO = applyMap.get(itemCode);
                                if (!EmptyUtils.isEmpty(urationDtlDTO)) {
                                    dto.setUseMaxMoney(urationDtlDTO.getUseMaxMoney());
                                }
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * 功能介绍 : 检查是否包含选择的品种。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private RatUrationDisDtlDTO checkRationItemCode(RatUrationDisDTO disDTO, String itemCode) {
        List<RatUrationDisDtlDTO> rationDlts = disDTO.getRationDlts();
        for (RatUrationDisDtlDTO dtlDTO : rationDlts) {
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    return dtlDTO;
                }
            } else {
                List<RatUrationDtlDTO> ratDtls = dtlDTO.getRationDlts();
                for (RatUrationDtlDTO dto : ratDtls) {
                    if (itemCode.equals(dto.getItemCode())) {
                        dtlDTO.setItemCode(itemCode);
                        dtlDTO.setItemName(dto.getItemName());
                        dtlDTO.setRationDlts(new ArrayList<RatUrationDtlDTO>());
                        dtlDTO.addRationDtl(dto);
                        return dtlDTO;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能介绍 : 获取综合授信品种。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private RatUrationDisDtlDTO getCompItemDtl(List<RatUrationDisDtlDTO> rationDtls) {
        for (RatUrationDisDtlDTO disDtlDTO : rationDtls) {
            if (RationConstant.RAT_COMFLAG_1.equals(disDtlDTO.getItemFlag())) {
                return disDtlDTO;
            }
        }
        return null;
    }

    /**
     * 功能介绍 : 授信挂接数据查询。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationDisDTO> viewCorpDisRation(RatUrationDTO urationDTO, String isRmbRat) {
        List<RatUrationDisDTO> result = new ArrayList<RatUrationDisDTO>();
        List<RatUrationDisDtlDTO> resultDtl = new ArrayList<RatUrationDisDtlDTO>();
        UrationParamDTO paramDTO = new UrationParamDTO();
        BeanUtils.copyProperties(urationDTO, paramDTO);
        paramDTO.setItemCode(urationDTO.getRatItemCode());// 授信品种
        if (RationConstant.RAT_COMMON_Y.equals(isRmbRat)) {
            // 查询单位本位币
            CorpDTO corpDTO = corpService.get(paramDTO.getCorpId());
            paramDTO.setCurCode(corpDTO.getCurId());// 本位币
        }
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "," + RationConstant.STATUS_INVALID);// 查看授信可以查看作废的信息
        List<RatUrationDisDTO> rations = queryCorpDisRation(paramDTO, urationDTO);
        String itemCode = urationDTO.getRatItemCode();// 根据授信品种判断是单品种还是综合品种
        List<RatUrationDisBean> disBeans = ratUrationDao.queryCorpRatDisration(paramDTO);
        Map<String, RatUrationDisBean> disMap = Maps.uniqueIndex(disBeans, new Function<RatUrationDisBean, String>() {
            @Override
            public String apply(RatUrationDisBean input) {
                return input.getItemCode();
            }
        });
        RatUrationDisBean disBean = disMap.get(itemCode);
        String itemFlag = RationConstant.RAT_COMFLAG_0;
        if (null != disBean) {
            itemFlag = disBean.getItemFlag();// 品种类型
        }
        Map<String, RatUrationDtlDTO> applyMap = null;
        if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {
            List<RatUrationDtlDTO> applyDtls = urationDTO.getApplyDtls();
            applyMap = Maps.uniqueIndex(applyDtls, new Function<RatUrationDtlDTO, String>() {
                public String apply(RatUrationDtlDTO input) {
                    return input.getItemCode();
                }
            });
        }
        // 比较授信数据
        for (RatUrationDisDTO disDTO : rations) {
            List<RatUrationDisDtlDTO> rationDtls = disDTO.getRationDlts();
            for (RatUrationDisDtlDTO disDtlDTO : rationDtls) {
                if (disDtlDTO.getItemFlag().equals(itemFlag)) {
                    if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {
                        setCompRationDtlMoney(disDtlDTO, applyMap);
                    }
                    resultDtl.add(disDtlDTO);
                }
            }
            result.add(disDTO);
        }
        return result;
    }

    /**
     * 功能介绍 : 设置综合授信额度。<br>
     * <b>日期：</b>2017年9月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setCompRationDtlMoney(RatUrationDisDtlDTO disDtlDTO, Map<String, RatUrationDtlDTO> applyMap) {
        List<RatUrationDtlDTO> rationDlts = disDtlDTO.getRationDlts();
        if (!EmptyUtils.isEmptyList(rationDlts)) {
            for (RatUrationDtlDTO dtlDTO : rationDlts) {
                RatUrationDtlDTO urationDtlDTO = applyMap.get(dtlDTO.getItemCode());
                if (null != urationDtlDTO) {
                    dtlDTO.setUseMaxMoney(urationDtlDTO.getUseMaxMoney());
                }
            }
        }
    }

    /**
     * 功能介绍 : 根据系统业务和业务单号获取授信占用信息。<br>
     * <b>日期：</b>2017年9月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId) {
        List<RatUration> ratUrations = ratUrationDao.queryRatUrationById(sysCode, billId);
        if (!EmptyUtils.isEmptyList(ratUrations)) {
            RatUration uration = ratUrations.get(0);
            RatUrationDTO urationDTO = new RatUrationDTO();
            BeanUtils.copyProperties(uration, urationDTO);
            urationDTO.setUrationId(uration.getId());
            RatBkration bkration = rationService.get(uration.getRatCode(), true);
            urationDTO.setRatIsHangGua(bkration.getIsHangGuarantee());// 设置授信是否挂接担保
            urationDTO.setBankCode(bkration.getBankCode());
            urationDTO.setRatItemCode(uration.getItemCode());
            RatItemCustom itemCustom = itemCustomService.get(uration.getItemCode(), true);
            if (null != itemCustom) {
                urationDTO.setRatItemName(itemCustom.getName());// 品种名称
            }
            if (StringUtils.isNotEmpty(uration.getBillName())) {
                urationDTO.setRaceName(businessRaceService.get(uration.getBillName()).getRaceName());
            }
            if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {// 综合授信
                List<RatUrationDtlDTO> applyDtls = new ArrayList<RatUrationDtlDTO>();
                List<RatUrationApplyDtl> rationDtls = applyDtlService.queryRatUrationDtlById(uration.getId());
                for (RatUrationApplyDtl applyDtl : rationDtls) {
                    if (applyDtl.getItemCode().equals(urationDTO.getRatItemCode())) {
                        RatUrationDtlDTO dtlDTO = new RatUrationDtlDTO();
                        dtlDTO.setItemCode(applyDtl.getItemCode());
                        dtlDTO.setUseMaxMoney(applyDtl.getMaxMoney());// 本次授信占用的最高额度
                        applyDtls.add(dtlDTO);
                    }
                }
                urationDTO.setApplyDtls(applyDtls);
            }
            String isRmbRat = RationConstant.RAT_COMMON_Y;// 默认折合本位币
            if (uration.getInitCurCode() != null && uration.getCurCode() != null && uration.getCurCode().equals(uration.getInitCurCode())) {
                isRmbRat = RationConstant.RAT_COMMON_N;
            }
            // 获取额度剩余金额
            List<RatUrationDisDTO> disDTOs = viewCorpDisRation(urationDTO, isRmbRat);
            urationDTO.setIsRmbRat(isRmbRat);
            if (!EmptyUtils.isEmptyList(disDTOs)) {
                RatUrationDisDTO disDTO = disDTOs.get(0);
                List<RatUrationDisDtlDTO> rationDlts = disDTO.getRationDlts();
                if (!EmptyUtils.isEmptyList(rationDlts)) {
                    for (RatUrationDisDtlDTO disDtlDTO : rationDlts) {
                        if (RationConstant.RAT_COMFLAG_0.equals(disDtlDTO.getItemFlag())) {// 单授信
                            if (uration.getItemCode().equals(disDtlDTO.getItemCode())) {
                                urationDTO.setMoney(disDtlDTO.getOverPlusMoney());// 可用额度
                                break;
                            }
                        } else {
                            List<RatUrationDtlDTO> ratDlts = disDtlDTO.getRationDlts();
                            if (!EmptyUtils.isEmptyList(ratDlts)) {
                                for (RatUrationDtlDTO dto : ratDlts) {
                                    if (uration.getItemCode().equals(dto.getItemCode())) {
                                        urationDTO.setMoney(dto.getItemMoney());// 可用额度
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return urationDTO;
        }
        return null;
    }

    /**
     * 功能介绍 : 查询银行授信已分配数据。<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationVO> queryRationDisDtl(UrationParamDTO paramDTO) {
        return ratUrationDao.queryRationDisDtl(paramDTO);
    }

    private void setBeanMoneyType(RatUrationDisBean disBean, Integer moneyType) {
        if (RationConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class userCla = disBean.getClass();
        /*
         * 得到类中的所有属性集合
         */
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = f.get(disBean);// 得到此属性的值
                String type = f.getType().toString();// 得到此属性的类型
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(disBean, BigDecimal.ZERO);
                    } else {
                        int commonMoneyUnit = 1;
                        if (RationConstant.COMMON_MONEYUNIT_10000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_10000;
                        } else if (RationConstant.COMMON_MONEYUNIT_1000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_1000000;
                        } else if (RationConstant.COMMON_MONEYUNIT_100000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_100000000;
                        }
                        BigDecimal value = (BigDecimal) val;
                        f.set(disBean, value.divide(BigDecimal.valueOf(commonMoneyUnit), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<RatUrationSysBean>> groupSysRation(List<RatUrationSysBean> sysBeans) {
        Map<String, List<RatUrationSysBean>> sysRationMap = new HashMap<String, List<RatUrationSysBean>>();
        for (RatUrationSysBean bean : sysBeans) {
            String ratCode = bean.getRatCode();// 授信ID
            String itemCode = bean.getItemCode();// 品种
            String key = ratCode + "_" + itemCode;
            List<RatUrationSysBean> beans = sysRationMap.get(key);
            if (null == beans || EmptyUtils.isEmptyList(beans)) {
                beans = new ArrayList<RatUrationSysBean>();
                sysRationMap.put(key, beans);
            }
            beans.add(bean);
        }
        Map<String, List<RatUrationSysBean>> resultMap = new HashMap<String, List<RatUrationSysBean>>();
        for (Entry<String, List<RatUrationSysBean>> entry : sysRationMap.entrySet()) {
            String key = entry.getKey();
            resultMap.put(key, mergeSysBeans(entry.getValue()));
        }
        return resultMap;
    }

    private List<RatUrationSysBean> mergeSysBeans(List<RatUrationSysBean> sysBeans) {
        List<RatUrationSysBean> results = new ArrayList<RatUrationSysBean>();
        Map<String, RatUrationSysBean> sysMap = new HashMap<String, RatUrationSysBean>();
        for (RatUrationSysBean bean : sysBeans) {
            String status = bean.getStatus();// 占用状态
            String sysCode = bean.getSysCode();// 业务类别
            RatUrationSysBean sysBean = sysMap.get(sysCode);
            if (null == sysBean) {
                sysBean = new RatUrationSysBean();
                BeanUtils.copyProperties(bean, sysBean, "money");
                sysMap.put(sysCode, sysBean);
            }
            if (RationConstant.RAT_USE_STATUS_FREEZE.toString().equals(status)) {
                sysBean.setAppMoney(sysBean.getAppMoney().add(bean.getMoney()));// 冻结金额合计
            } else if (RationConstant.RAT_USE_STATUS_USE.toString().equals(status)) {
                sysBean.setUseMoney(sysBean.getUseMoney().add(bean.getMoney()));// 占用金额合计
            }
        }
        results.addAll(sysMap.values());
        return results;
    }

    public List<RatUrationDisBean> queryRatUseMoney(UrationParamDTO paramDTO) {
        List<RatUrationDisBean> disBeans = ratUrationDao.queryCorpRatDisList(paramDTO);// 授信使用情况
        // 银行类别相同
        if (!EmptyUtils.isEmpty(paramDTO.getBankCtlType()) && paramDTO.getBankCtlType().equals(RationConstant.RAT_BANKTYPE_SAME)) {
            List<String> bankCodes = disBeans.stream().map(RatUrationDisBean::getBankCode).collect(Collectors.toList());
            Map<String, InputBankInfoDTO> bankCodeMap = bankInfoService.getBankListCheck(bankCodes, paramDTO.getBankPrefix());
            disBeans.removeIf(bean -> !bankCodeMap.containsKey(bean.getBankCode()));
        }
        return disBeans;
    }


    /**
     * 对RatUrationSysBean做金额单位换算，和setBeanMoneyType逻辑保持一致
     * @param sysBean 明细bean
     * @param moneyType 金额单位 1/10000/1000000/100000000
     */
    private void setSysBeanMoneyType(RatUrationSysBean sysBean, Integer moneyType) {
        if (RationConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
            return;
        }
        Class<?> userCla = sysBean.getClass();
        try {
            Field[] fs = userCla.getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                Object val = f.get(sysBean);
                String type = f.getType().toString();
                if (type.endsWith("BigDecimal")) {
                    if (null == val) {
                        f.set(sysBean, BigDecimal.ZERO);
                    } else {
                        int commonMoneyUnit = 1;
                        if (RationConstant.COMMON_MONEYUNIT_10000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_10000;
                        } else if (RationConstant.COMMON_MONEYUNIT_1000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_1000000;
                        } else if (RationConstant.COMMON_MONEYUNIT_100000000.equals(moneyType)) {
                            commonMoneyUnit = RationConstant.COMMON_MONEYUNIT_100000000;
                        }
                        BigDecimal value = (BigDecimal) val;
                        f.set(sysBean, value.divide(BigDecimal.valueOf(commonMoneyUnit), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (Exception e) {
            log.error("RatUrationSysBean金额单位换算异常", e);
        }
    }


    public List<RatUrationUseVO> transRationUserDtl(UrationParamDTO paramDTO, List<RatUrationDisBean> disBeans, List<String> ratCodes) {
        List<RatUrationSysBean> sysBeans = queryRatUrationApply(ratCodes);// 授信占用明细
        sysBeans.addAll(ratUrationDao.queryRatReliefUration(paramDTO));// 授信调剂明细
        sysBeans.addAll(ratUrationDao.queryRatAddUration(paramDTO));// 授信追加明细

        Map<String, List<RatUrationSysBean>> sysRationMap = groupSysRation(sysBeans);
        if (!RationConstant.COMMON_MONEYUNIT_1.equals(paramDTO.getMoneyType())) {
            Integer moneyType = paramDTO.getMoneyType();
            for (List<RatUrationSysBean> beanList : sysRationMap.values()) {
                for (RatUrationSysBean sysBean : beanList) {
                    setSysBeanMoneyType(sysBean, moneyType);
                }
            }
        }

        // 对授信明细进行分组
        List<RatUrationUseVO> disDTOs = new ArrayList<RatUrationUseVO>();
        Map<String, RatUrationUseVO> disMap = new HashMap<String, RatUrationUseVO>();
        String itemCode = paramDTO.getItemCode();// 页面选择的授信品种
        if (!EmptyUtils.isEmpty(itemCode)) {
            itemCode = itemCode.trim();// 非空限制
        }
        if (EmptyUtils.isEmptyList(disBeans)) {
            disBeans = new ArrayList<RatUrationDisBean>();
        }
        for (RatUrationDisBean disBean : disBeans) {
            setBeanMoneyType(disBean, paramDTO.getMoneyType());

            String disCode = disBean.getDisCode();
            RatUrationUseVO disDTO = disMap.get(disCode);
            if (null == disDTO) {
                disDTO = new RatUrationUseVO();
                BeanUtils.copyProperties(disBean, disDTO);
                disDTO.setRationDlts(new ArrayList<RatUrationUseDtlVO>());
                disMap.put(disCode, disDTO);
            }
            // 在审占用额度+追减
            BigDecimal approveMoney = disBean.getFreezeMoney();
            disBean.setApproveMoney(approveMoney);// 在审占用
            if (null != disBean.getStatus() && disBean.getStatus().equals(RationConstant.STATUS_INVALID)) {// 作废的分配数据剩余额度及可用额度为0
                disBean.setOverPlusMoney(BigDecimal.ZERO);
                disBean.setLeftMoney(BigDecimal.ZERO);
            } else {
                disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney)
                        .subtract(disBean.getVirtualUration()).subtract(disBean.getReliefAndUrationMoney()));// 可用额度
                // 剩余额度 = 在审+可用(包含虚拟在途占用和虚拟在途调剂，这些不能在真实在途占用中)
                disBean.setLeftMoney(disBean.getApproveMoney().add(disBean.getOverPlusMoney())
                        .add(disBean.getVirtualRelief()).add(disBean.getVirtualUration()));
            }
            // 综合品种
            if (RationConstant.RAT_COMFLAG_1.equals(disBean.getItemFlag())) {
                RatUrationUseDtlVO disDtlDTO = getCompItemUseDtl(disDTO.getRationDlts());
                if (null == disDtlDTO) {
                    disDtlDTO = new RatUrationUseDtlVO();
                    BeanUtils.copyProperties(disBean, disDtlDTO);
                    if (RationConstant.RAT_COMP_SIGN_1.equals(disBean.getCompSign())) {
                        disDTO.addRationDtl(disDtlDTO);
                    }
                }
                // 设置综合各品种的额度情况,综合授信的各额度使用情况为最高额度的使用情况
                RatUrationUseDtlVO rationDtl = new RatUrationUseDtlVO();
                BeanUtils.copyProperties(disBean, rationDtl);
                rationDtl.setMoney(disBean.getMaxMoney());// 综合最高额度
                rationDtl.setUseMoney(disBean.getUseMaxMoney());// 占用额度
                rationDtl.setApproveMoney(disBean.getFreezeMaxMoney());// 在审占用
                if (null != disBean.getStatus() && disBean.getStatus().equals(RationConstant.STATUS_INVALID)) {// 作废的分配数据剩余额度及可用额度为0
                    disBean.setOverPlusMoney(BigDecimal.ZERO);
                    disBean.setLeftMoney(BigDecimal.ZERO);
                } else {
                    rationDtl.setOverPlusMoney(disBean.getMaxMoney().subtract(disBean.getUseMaxMoney())
                            .subtract(disBean.getFreezeMaxMoney()).subtract(disBean.getVirtualMaxMoney())
                            .subtract(disBean.getVirtualUrationMaxMoney()));// 可用额度=分配额度-占用额度-冻结额度
                    rationDtl.setLeftMoney(rationDtl.getApproveMoney().add(rationDtl.getOverPlusMoney())
                            .add(disBean.getVirtualUrationMaxMoney()));// 剩余额度 = 在审+可用
                }
                disDtlDTO.addRationDtl(rationDtl);
            } else {
                RatUrationUseDtlVO disDtlDTO = new RatUrationUseDtlVO();
                BeanUtils.copyProperties(disBean, disDtlDTO);
                disDTO.addRationDtl(disDtlDTO);
            }
        }
        // 判断是否存在指定品种
        for (Entry<String, RatUrationUseVO> entry : disMap.entrySet()) {
            RatUrationUseVO disDTO = entry.getValue();
            if (EmptyUtils.isEmpty(itemCode)) {// 未选择品种时
                // setUrationDisDTO(disDTO);
                disDTOs.add(disDTO);
            } else {
                // 检查是否包含授信品种
                RatUrationUseDtlVO disDtlDTO = checkRationUseItem(disDTO, itemCode);
                if (null != disDtlDTO) {
                    disDTOs.add(disDTO);
                }
            }
        }
        // 计算品种的条数
        Integer size = 0;
        boolean isRatComFlag = false;
        for (RatUrationUseVO disDTO : disDTOs) {
            List<RatUrationUseDtlVO> rationDtls = disDTO.getRationDlts();
            // disDTO.setConId(rationService.get(disDTO.getRatCode()).getConId());
            Integer lineNos = 0;
            if (!EmptyUtils.isEmptyList(rationDtls)) {
                BigDecimal totalUseMoney = BigDecimal.ZERO;// 综合授信整体使用额度合计
                BigDecimal totalApproveMoney = BigDecimal.ZERO;// 综合授信在审占用额度合计
                BigDecimal virtualMaxMoney = BigDecimal.ZERO;// 综合授信其他单位使用额度合计
                BigDecimal virtualUrationMaxMoney = BigDecimal.ZERO;// 综合授信综合授信在审占用额度合计
                for (RatUrationUseDtlVO disDtlDTO : rationDtls) {
                    Integer sysRationSize = setRationDtl(disDtlDTO, sysRationMap);// 按业务类别分类
                    lineNos = lineNos + sysRationSize;
                    if (RationConstant.RAT_COMFLAG_0.equals(disDtlDTO.getItemFlag())) {
                        disDtlDTO.setRationSize(disDtlDTO.getRationSize() + 1);
                        disDTO.setRationSize(disDTO.getRationSize() + 1);
                        size = 1 + size;
                    } else {
                        List<RatUrationUseDtlVO> rations = disDtlDTO.getRationDlts();
                        for (RatUrationUseDtlVO rationDlt : rations) {
                            totalUseMoney = totalUseMoney.add(rationDlt.getUseMoney());
                            totalApproveMoney = totalApproveMoney.add(rationDlt.getApproveMoney());
                            virtualMaxMoney = virtualMaxMoney.add(rationDlt.getVirtualMaxMoney());
                            virtualUrationMaxMoney = virtualUrationMaxMoney.add(rationDlt.getVirtualUrationMaxMoney());
                        }
                        disDtlDTO.setVirtualMaxMoney(virtualMaxMoney);
                        disDtlDTO.setVirtualUrationMaxMoney(virtualUrationMaxMoney);
                        disDtlDTO.setUseMoney(totalUseMoney);
                        disDtlDTO.setApproveMoney(totalApproveMoney);
                        disDtlDTO.setLeftMoney(disDtlDTO.getMoney().subtract(totalUseMoney).subtract(virtualMaxMoney));
                        disDtlDTO.setOverPlusMoney(
                                disDtlDTO.getMoney().subtract(totalUseMoney).subtract(totalApproveMoney)
                                        .subtract(virtualUrationMaxMoney).subtract(virtualMaxMoney));
                        setMaxLeftMoney(disDtlDTO);
                        disDtlDTO.setRationSize(disDtlDTO.getRationDlts().size());
                        size += disDtlDTO.getRationDlts().size();
                        isRatComFlag = true;
                    }
                }
                Collections.sort(rationDtls, new Comparator<RatUrationUseDtlVO>() {
                    @Override
                    public int compare(RatUrationUseDtlVO o1, RatUrationUseDtlVO o2) {
                        if (o1.getItemFlag().equals(o2.getItemFlag())) {
                            return o1.getItemOrder().compareTo(o2.getItemOrder());
                        }
                        return o1.getItemFlag().compareTo(o2.getItemFlag());
                    }
                });
            }
            if (size != 0) {
                if (isRatComFlag) {
                    disDTO.setRationSize(size + 1);
                    disDTO.setLineNos(lineNos + 1);
                } else {
                    disDTO.setRationSize(size);
                    disDTO.setLineNos(lineNos);
                }
                size = 0;
                lineNos = 0;
                isRatComFlag = false;
            }
        }
        return disDTOs;
    }

    /**
     * 功能介绍 : 查询授信额度使用明细。<br>
     * <b>日期：</b>2017年9月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationUseVO> queryRationUseDtl(UrationParamDTO paramDTO) {
        // List<RatUrationDisBean> disBeans = ratUrationDao.queryCorpRatDisration(paramDTO);//授信使用情况
        List<RatUrationDisBean> disBeans = ratUrationDao.queryCorpRatDisList(paramDTO);// 授信使用情况
        // 银行类别相同
        if (!EmptyUtils.isEmpty(paramDTO.getBankCtlType()) && paramDTO.getBankCtlType().equals(RationConstant.RAT_BANKTYPE_SAME)) {
            List<String> bankCodes = disBeans.stream().map(RatUrationDisBean::getBankCode).collect(Collectors.toList());
            Map<String, InputBankInfoDTO> bankCodeMap = bankInfoService.getBankListCheck(bankCodes, paramDTO.getBankPrefix());
            if (CollectionUtil.isNotEmpty(bankCodeMap)) {
                disBeans.removeIf(bean -> !bankCodeMap.containsKey(bean.getBankCode()));
            }
        }
        List<String> ratCodes = calcRationMoney(disBeans);
        return transRationUserDtl(paramDTO, disBeans, ratCodes);
    }

    /**
     * <br>
     * <b>功能：</b> 设置合同的授信品种按业务类别分类 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/9/17 0017 <br>
     */
    private Integer setRationDtl(RatUrationUseDtlVO disDtlDTO, Map<String, List<RatUrationSysBean>> sysRationMap) {
        Integer lineNo = 0;
        if (RationConstant.RAT_COMFLAG_1.equals(disDtlDTO.getItemFlag())) {
            List<RatUrationUseDtlVO> rationDlts = disDtlDTO.getRationDlts();
            if (EmptyUtils.isEmptyList(rationDlts)) {
                return 0;
            }
            for (RatUrationUseDtlVO dtlVO : rationDlts) {
                setSingleRationDtl(dtlVO, sysRationMap);
                Integer sysRationSize = dtlVO.getSysRationSize();
                lineNo = lineNo + sysRationSize;
            }
        } else {
            setSingleRationDtl(disDtlDTO, sysRationMap);
            lineNo = disDtlDTO.getSysRationSize();
        }
        return lineNo;
    }

    private void setSingleRationDtl(RatUrationUseDtlVO disDtlDTO, Map<String, List<RatUrationSysBean>> sysRationMap) {
        String ratId = disDtlDTO.getDisCode();// 授信分配ID
        String itemId = disDtlDTO.getItemCode();// 授信品种ID
        String key = ratId + "_" + itemId;
        List<RatUrationSysBean> sysBeanList = sysRationMap.get(key);
        if (null != sysBeanList && sysBeanList.size() > 0) {
            if (sysBeanList.size() > 1) {
                Collections.sort(sysBeanList, new Comparator<RatUrationSysBean>() {
                    @Override
                    public int compare(RatUrationSysBean o1, RatUrationSysBean o2) {
                        return o1.getSysCode().compareTo(o2.getSysCode());
                    }
                });
            }
        } else {
            sysBeanList = new ArrayList<RatUrationSysBean>();
            RatUrationSysBean bean = new RatUrationSysBean();
            bean.setAppMoney(BigDecimal.ZERO);
            bean.setUseMoney(BigDecimal.ZERO);
            sysBeanList.add(bean);
        }
        BigDecimal approveMoney = BigDecimal.ZERO;
        BigDecimal useMoney = BigDecimal.ZERO;
        for (int i = 0; i < sysBeanList.size(); i++) {
            RatUrationSysBean bean = sysBeanList.get(i);
            bean.setOrder(i + 1);
            approveMoney = approveMoney.add(bean.getAppMoney());
            useMoney = useMoney.add(bean.getUseMoney());
        }
        // 设置占用和在途金额
        // disDtlDTO.setApproveMoney(approveMoney);
        // disDtlDTO.setUseMoney(useMoney);
        // disDtlDTO.setOverPlusMoney(disDtlDTO.getMoney().subtract(disDtlDTO.getUseMoney()).subtract(disDtlDTO.getApproveMoney()));
        // disDtlDTO.setLeftMoney(disDtlDTO.getMoney().subtract(disDtlDTO.getUseMoney()));
        disDtlDTO.setSysBeans(sysBeanList);
        disDtlDTO.setSysRationSize(sysBeanList.size());
    }

    /**
     * 功能介绍 : 设置综合品种的最高额度。<br>
     * <b>日期：</b>2017年9月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setMaxLeftMoney(RatUrationUseDtlVO disDTO) {
        List<RatUrationUseDtlVO> rations = disDTO.getRationDlts();
        if (EmptyUtils.isEmptyList(rations)) {
            return;
        }
        for (RatUrationUseDtlVO dtlVO : rations) {
            if (dtlVO.getOverPlusMoney().compareTo(disDTO.getOverPlusMoney()) > 0) {
                dtlVO.setOverPlusMoney(disDTO.getOverPlusMoney());// 可用额度
            }
            if (dtlVO.getLeftMoney().compareTo(disDTO.getLeftMoney()) > 0) {
                dtlVO.setLeftMoney(disDTO.getLeftMoney());// 剩余额度
            }
        }
    }

    /**
     * 功能介绍 : 获取综合授信品种。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private RatUrationUseDtlVO getCompItemUseDtl(List<RatUrationUseDtlVO> rationDtls) {
        for (RatUrationUseDtlVO disDtlDTO : rationDtls) {
            if (RationConstant.RAT_COMFLAG_1.equals(disDtlDTO.getItemFlag())) {
                return disDtlDTO;
            }
        }
        return null;
    }

    /**
     * 功能介绍 : 检查是否包含选择的品种。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private RatUrationUseDtlVO checkRationUseItem(RatUrationUseVO disDTO, String itemCode) {
        List<RatUrationUseDtlVO> newRationDtls = new ArrayList<RatUrationUseDtlVO>();
        List<RatUrationUseDtlVO> rationDlts = disDTO.getRationDlts();
        for (RatUrationUseDtlVO dtlDTO : rationDlts) {
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    newRationDtls.add(dtlDTO);
                    disDTO.setRationDlts(newRationDtls);
                    return dtlDTO;
                }
            } else {
                List<RatUrationUseDtlVO> ratDtls = dtlDTO.getRationDlts();// 综合品种
                for (RatUrationUseDtlVO dto : ratDtls) {// 综合品种匹配到则显示所有的综合品种信息
                    if (itemCode.equals(dto.getItemCode())) {
                        newRationDtls.add(dtlDTO);
                        disDTO.setRationDlts(newRationDtls);
                        return dtlDTO;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能介绍 : 查询授信占用情况。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationUseBeanVO> queryCorpRationUseDtl(RatUrationQueryVO queryVO) {
        List<RatUrationUseBeanVO> result = new ArrayList<RatUrationUseBeanVO>();
        result.addAll(ratUrationDao.queryCorpRationUseDtl(queryVO));
        if (!EmptyUtils.isEmpty(queryVO.getUseType())
                && RationConstant.RAT_USE_STATUS_FREEZE.equals(queryVO.getUseType())) {
            if (EmptyUtils.isEmpty(queryVO.getBusinessName())
                    || RationConstant.BUSINESS_NAME_05.equals(queryVO.getBusinessName())) {
                result.addAll(reliefDetailService.queryRationReliefUseDtl(queryVO));// 银行调剂在途
                result.addAll(addDetailService.queryRationSubAddDtl(queryVO));// 集团追减在途
            }
        }
        return result;
    }

    /**
     * 银行授信业务品种明细查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput<RatItemDisVO> queryRaturationUsedItemDetail(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONITEMUSE_QUERY, user.getId());
        //todo 标识业务来源
        Map<String, List<String>> businessParams = new HashMap<>();
        DataTablesOutput<RatItemDisVO> dataTablesOutput = ratUrationDao.queryRaturationUsedItemDetail(dataTablesInput, businessParams, corpIds);
        dataTablesOutput.setData(this.setGuaWayName(dataTablesOutput.getData()));
        return dataTablesOutput;
    }

    // 设置列表上显示的担保方式名称
    public List<RatItemDisVO> setGuaWayName(List<RatItemDisVO> list) {
        String guaWayNamesStr = null;
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        for (RatItemDisVO ratItemDisVO : list) {
            guaContractOccupDTO.setBusinessId(ratItemDisVO.getRatCode());
            GuaContractOccupDTO guaContractDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            // guaWayNamesStr = getGuaWay(guaContractDTO.getGuaWay());
            guaWayNamesStr = guaContractDTO.getGuaWayName();
            ratItemDisVO.setGuaWay(guaWayNamesStr);
        }
        return list;
    }

    /**
     * 银行授信占用明细查询
     *
     * @return
     */
    public DataTablesOutput getRaturationApplyDetail(RatUrationQueryVO queryVO) {
        List<RatUrationUseBeanVO> urationUseBeanVOList = this.queryCorpRationUseDtl(queryVO);
        DataTablesOutput dataTablesOutput = new DataTablesOutput();
        dataTablesOutput.setData(urationUseBeanVOList);
        return dataTablesOutput;
    }

    /**
     * 计算授信正式占用金额
     *
     * @param urationDTO
     * @return
     */
    public RatUration getRatUrationMoney(RatUrationDTO urationDTO, RatUration ratUration) {
        CorpDTO corpDTO = corpService.get(urationDTO.getCorpId());
        String natualCurId = corpDTO.getCurId();// 本位币币种ID
        String isOpenRat = urationDTO.getIsOpenRat();// 是否启用敞口授信
        BigDecimal ratUrationMoney = null;// 授信占用金额(计算后)
        String ratCurId = ratUration.getCurCode();// 授信币种
        BigDecimal money = urationDTO.getMoney();// 授信占用金额（计算前）
        String billCurId = urationDTO.getCurCode() == null ? urationDTO.getInitCurCode() : urationDTO.getCurCode();// 业务币种
        BigDecimal bondMoney = BigDecimal.ZERO;
        if (urationDTO.getBondMoney() != null) {
            bondMoney = urationDTO.getBondMoney();// 保证金金额
        }
        String bondCurId = urationDTO.getBondCurId();// 保证金币种
        BigDecimal exchangeRate = BigDecimal.ONE;
        if (!billCurId.equals(ratCurId) || !billCurId.equals(bondCurId)) {// 授信币别与业务币别不一致或业务币别与保证金币别不一致时需要查询汇率
            exchangeRate = excrateAdjustService.getExchangeRateBy(billCurId, natualCurId, new Date(),
                    FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        }
        ratUration.setExchangeRate(exchangeRate);
        if (StringUtils.isNotEmpty(isOpenRat)) {
            if (isOpenRat.equals(RationConstant.OPEN_SIGN_1)) {// 启用敞口授信
                if (StringUtils.isNotEmpty(ratCurId)) {
                    if (ratCurId.equals(natualCurId)) {// 授信币种为本位币
                        if (StringUtils.isNotEmpty(billCurId)) {
                            if (billCurId.equals(natualCurId)) {// 业务币别为本位币
                                ratUrationMoney = money.subtract(bondMoney);// 保证金币种只能为本位币，折算金额=业务金额-保证金金额
                                ratUration.setConvertPreMoney(ratUrationMoney);// 设置折算金额
                            } else {// 业务币别为外币
                                if (bondCurId.equals(natualCurId)) {// 保证金币种为本位币
                                    ratUrationMoney = money.multiply(exchangeRate).subtract(bondMoney);// 折算金额=业务金额*汇率-保证金金额
                                    ratUration.setConvertPreMoney(ratUrationMoney);// 设置折算金额
                                } else {// 保证金币种为外币（与业务币别同种外币）
                                    ratUrationMoney = (money.subtract(bondMoney)).multiply(exchangeRate);// 设置折算金额=（业务金额-保证金金额）*汇率
                                    ratUration.setConvertPreMoney(ratUrationMoney);// 设置折算金额
                                }
                            }
                        }
                    } else {// 授信币种为外币时，业务金额只能是同种外币
                        if (bondCurId.equals(natualCurId)) {// 保证金币种为本位币
                            ratUrationMoney = money
                                    .subtract((bondMoney.divide(exchangeRate, 2, BigDecimal.ROUND_HALF_UP)));// 占用授信金额=业务金额-保证金金额/汇率
                            ratUration.setConvertPreMoney(money.multiply(exchangeRate).subtract(bondMoney));// 设置折算后金额
                        } else {// 保证金币种为外币
                            ratUrationMoney = money.subtract(bondMoney);// 占用授信金额=业务金额-保证金金额
                            ratUration.setConvertPreMoney(ratUrationMoney.multiply(exchangeRate));// 设置折算金额
                        }
                    }
                }
                if (ratUrationMoney.compareTo(BigDecimal.ZERO) == -1) {// 敞口折算后占用金额为负数
                    throw new ParameterException(RatMessageEnum.CMS_RAT_OPENMONEY_CHECK);
                }
            } else if (isOpenRat.equals(RationConstant.OPEN_SIGN_0)) {// 不启用敞口
                if (StringUtils.isNotEmpty(ratCurId)) {
                    if (ratCurId.equals(natualCurId)) {// 授信币种为本位币
                        if (StringUtils.isNotEmpty(billCurId)) {
                            if (billCurId.equals(natualCurId)) {// 业务币别为本位币
                                ratUrationMoney = money;// 占用授信金额=业务金额
                                ratUration.setConvertPreMoney(ratUration.getMoney());// 折算后金额=占用金额
                            } else {// 业务币别为外币
                                ratUrationMoney = money.multiply(exchangeRate);// 占用授信金额=业务金额*汇率
                                ratUration.setConvertPreMoney(ratUrationMoney);// 折算后金额=占用金额*汇率
                            }
                        }
                    } else {// 授信币种为外币时，业务金额只能是同种外币
                        ratUrationMoney = money;// 占用授信金额=业务金额
                        ratUration.setConvertPreMoney(money.multiply(exchangeRate));// 折算后金额=占用金额*汇率
                    }
                }
            }
        } else {
            if (natualCurId.equals(billCurId)) {// 业务币种为本位币，授信只能为本位币
                ratUrationMoney = money;
                ratUration.setConvertPreMoney(money);// 折算后金额
            } else {/// 业务币种为外币，授信可为本位币或者外币
                if (ratCurId.equals(billCurId)) {// 授信币种与业务币种同为外币
                    ratUrationMoney = money;
                    ratUration.setConvertPreMoney(money.multiply(exchangeRate));// 折算后金额
                } else {// 业务币种为外币则占用本位币授信
                    ratUrationMoney = money.multiply(exchangeRate);
                    ratUration.setConvertPreMoney(money.multiply(exchangeRate));// 折算后金额
                }
            }
        }
        ratUration.setMoney(ratUrationMoney);// 占用金额
        return ratUration;
    }

    /**
     * 根据根据业务币别和业务金额计算折合人民币的金额
     *
     * @author luxy
     */
    public BigDecimal getExchangeMoney(String billCurId, String billMoney, String corpId) {
        BigDecimal money = new BigDecimal(billMoney);
        BigDecimal billMoneyRMB = null;
        CorpDTO corpDTO = corpService.get(corpId);
        BigDecimal exchangeRate = excrateAdjustService.getExchangeRateBy(billCurId, corpDTO.getCurId(), new Date(),
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        billMoneyRMB = money.multiply(exchangeRate);
        return billMoneyRMB;
    }

    /**
     * 获取到担保方式
     *
     * @param guaWayCodes
     * @return
     */
    private String getGuaWay(String guaWayCodes) {
        String guaWayNamesStr = "";
        if (StringUtils.isNotEmpty(guaWayCodes)) {
            for (String guaWayCode : guaWayCodes.split(",")) {
                List<GuaWayDTO> guaWays = guaWayService.findAllByCode(guaWayCode, null);
                if (null != guaWays && guaWays.size() > 0) {
                    guaWayNamesStr += "," + guaWays.get(0).getName();
                }
            }
        }
        return guaWayNamesStr;
    }

    /**
     * 获取授信占用List
     *
     * @author luxy
     */
    public List<RatUrationDTO> getRatUrationList(RatUrationDTO ratUrationDTO) {
        List<RatUrationDTO> ratUrationDTOS = new ArrayList<>();
        List<RatUration> ratUrations = ratUrationDao.queryRatUrationByParams(ratUrationDTO);
        for (RatUration uration : ratUrations) {
            RatUrationDTO urationDTO = new RatUrationDTO();
            BeanUtils.copyNotNullProperties(uration, urationDTO);
            urationDTO.setRatItemCode(uration.getItemCode());
            if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {// 综合授信
                List<RatUrationDtlDTO> applyDtls = new ArrayList<RatUrationDtlDTO>();
                List<RatUrationApplyDtl> rationDtls = applyDtlService.queryRatUrationDtlById(uration.getId());
                for (RatUrationApplyDtl applyDtl : rationDtls) {
                    if (applyDtl.getItemCode().equals(urationDTO.getRatItemCode())) {
                        RatUrationDtlDTO dtlDTO = new RatUrationDtlDTO();
                        dtlDTO.setItemCode(applyDtl.getItemCode());
                        dtlDTO.setUseMaxMoney(applyDtl.getMaxMoney());// 本次授信占用的最高额度
                        applyDtls.add(dtlDTO);
                    }
                }
                urationDTO.setApplyDtls(applyDtls);
            }
            urationDTO.setUrationId(uration.getId());
            urationDTO.setRatItemCode(uration.getItemCode());
            ratUrationDTOS.add(urationDTO);
        }
        return ratUrationDTOS;
    }

    /**
     * excel导出
     *
     * @param paramDTO
     * @return
     * @throws Exception
     */
    public HSSFWorkbook exportExcel(UrationParamDTO paramDTO, ICurrentUser user) throws Exception {
        if (ObjectUtil.isEmpty(paramDTO.getCorpId())) {
            List<String> corpIds = corpService.getUserCorpIds(RationConstant.CMS_RATION_RATIONQUERY_QUERY, user.getId());
            if (ObjectUtil.isNotEmpty(corpIds)) {
                paramDTO.setCorpId(String.join(",", corpIds));
            }
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("银行授信额度明细");
        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 10000);
        sheet.setColumnWidth(4, 10000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 7000);
        sheet.setColumnWidth(8, 10000);
        HSSFCellStyle headerStyle = getHeaderStyle(workbook);
        HSSFCellStyle contentStyle = getContentStyle(workbook);
        List<RatUrationUseVO> ratUrationUseVOS = this.queryRationUseDtl(paramDTO);
        int rowNum = 0;
        Map<String, String> langMap = new HashMap<>();
        Map<String, String> dictsMap = new HashMap<>();
        // 设置多语言
        setLang(langMap);
        for (RatUrationUseVO ratUrationUseVO : ratUrationUseVOS) {
            // 创建头部信息
            rowNum = createHeader(sheet, rowNum, ratUrationUseVO, headerStyle, langMap, dictsMap);
            for (RatUrationUseDtlVO rationDlt : ratUrationUseVO.getRationDlts()) {
                // 创建内容信息
                rowNum = createContent(sheet, rowNum, rationDlt, contentStyle, langMap, dictsMap);
            }
        }
        return workbook;
    }

    /**
     * 设置表头样式
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getHeaderStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * 设置内容样式
     *
     * @param workbook
     * @return
     */
    public HSSFCellStyle getContentStyle(HSSFWorkbook workbook) {
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setFont(font);
        return headerStyle;
    }

    /**
     * 创建表头
     *
     * @param sheet
     * @param rowNum
     */
    private Integer createHeader(HSSFSheet sheet, int rowNum, RatUrationUseVO ratUrationUseVO,
                                 HSSFCellStyle headerStyle, Map<String, String> langMap, Map<String, String> dictsMap) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, 0, 2);
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(rowNum, rowNum, 4, 6);
        HSSFRow sheetRow = sheet.createRow(rowNum++);
        HSSFCell cell0 = sheetRow.createCell(0);
        if (StringUtils.isEmpty(dictsMap.get("RatBkration.RatMode_" + ratUrationUseVO.getRatMode()))) {
            dictsMap.put("RatBkration.RatMode_" + ratUrationUseVO.getRatMode(),
                    dictService.get("RatBkration.RatMode", ratUrationUseVO.getRatMode()));
        }
        cell0.setCellValue(langMap.get("table.ratBkration.ratMode") + ":"
                + dictsMap.get("RatBkration.RatMode_" + ratUrationUseVO.getRatMode()));
        HSSFCell cell1 = sheetRow.createCell(1);
        cell1.setCellValue(new HSSFRichTextString());
        HSSFCell cell2 = sheetRow.createCell(2);
        cell2.setCellValue(new HSSFRichTextString());
        sheet.addMergedRegion(cellRangeAddress);
        HSSFCell cell3 = sheetRow.createCell(3);
        cell3.setCellValue(langMap.get("table.ratBkration.corpId") + ":" + ratUrationUseVO.getCorpName());
        HSSFCell cell4 = sheetRow.createCell(4);
        cell4.setCellValue(langMap.get("table.ratBkration.bankCode") + ":" + ratUrationUseVO.getBankName());
        HSSFCell cell5 = sheetRow.createCell(5);
        cell5.setCellValue(new HSSFRichTextString());
        HSSFCell cell6 = sheetRow.createCell(6);
        cell6.setCellValue(new HSSFRichTextString());
        sheet.addMergedRegion(cellRangeAddress1);
        HSSFCell cell7 = sheetRow.createCell(7);
        if (StringUtils.isEmpty(dictsMap.get("RatBkrationDis.DistributeWay_" + ratUrationUseVO.getDisWay()))) {
            dictsMap.put("RatBkrationDis.DistributeWay_" + ratUrationUseVO.getDisWay(),
                    dictService.get("RatBkrationDis.DistributeWay", ratUrationUseVO.getDisWay()));
        }
        cell7.setCellValue(langMap.get("table.ratBkrationDis.distributeWay") + ":"
                + dictsMap.get("RatBkrationDis.DistributeWay_" + ratUrationUseVO.getDisWay()));
        HSSFCell cell8 = sheetRow.createCell(8);
        cell8.setCellValue(langMap.get("table.ratBkration.validDate") + ":" + ratUrationUseVO.getBeginDate() + "~"
                + ratUrationUseVO.getEndDate());
        Iterator<Cell> iterator = sheetRow.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = iterator.next();
            cell.setCellStyle(headerStyle);
        }
        HSSFRow sheetRow1 = sheet.createRow(rowNum++);
        HSSFCell cell10 = sheetRow1.createCell(0);
        cell10.setCellValue(multiLangServiceI.getLang("table.ratBkrationDetail.itemFlag"));// 授信品种类型
        HSSFCell cell21 = sheetRow1.createCell(1);
        cell21.setCellValue(multiLangServiceI.getLang("table.ratBkrationDetail.itemCode"));// 授信品种
        HSSFCell cell22 = sheetRow1.createCell(2);
        cell22.setCellValue(multiLangServiceI.getLang("table.ratBkrationDetail.creditType"));// 授信方式
        HSSFCell cell23 = sheetRow1.createCell(3);
        cell23.setCellValue(multiLangServiceI.getLang("table.ratBkration.money"));// 授信额度
        HSSFCell cell24 = sheetRow1.createCell(4);
        cell24.setCellValue(multiLangServiceI.getLang("table.ratBkration.businessType"));// 业务分类
        HSSFCell cell25 = sheetRow1.createCell(5);
        cell25.setCellValue(multiLangServiceI.getLang("table.ratBkrationDisDetail.superProcessMoney"));// 占用额度
        HSSFCell cell26 = sheetRow1.createCell(6);
        cell26.setCellValue(multiLangServiceI.getLang("table.ratBkrationDisDetail.checkProcessMoney"));// 在审占用额度
        HSSFCell cell27 = sheetRow1.createCell(7);
        cell27.setCellValue(multiLangServiceI.getLang("table.ratBkrationDisDetail.overPlusMoney"));// 可用额度
        HSSFCell cell28 = sheetRow1.createCell(8);
        cell28.setCellValue(multiLangServiceI.getLang("table.ratBkrationDisDetail.leftDisMoney"));// 剩余授信额度
        Iterator<Cell> iterator1 = sheetRow1.cellIterator();
        while (iterator1.hasNext()) {
            Cell cell = iterator1.next();
            cell.setCellStyle(headerStyle);
        }
        return rowNum;
    }

    /**
     * 提前存好国际化提高性能
     *
     * @param langMap
     */
    private void setLang(Map<String, String> langMap) {
        langMap.put("table.ratBkration.ratMode", multiLangServiceI.getLang("table.ratBkration.ratMode"));
        langMap.put("table.ratBkration.corpId", multiLangServiceI.getLang("table.ratBkration.corpId"));
        langMap.put("table.ratBkration.bankCode", multiLangServiceI.getLang("table.ratBkration.bankCode"));
        langMap.put("table.ratBkrationDis.distributeWay",
                multiLangServiceI.getLang("table.ratBkrationDis.distributeWay"));
        langMap.put("table.ratBkration.validDate", multiLangServiceI.getLang("table.ratBkration.validDate"));
        langMap.put("table.ratBkrationDetail.itemFlag", multiLangServiceI.getLang("table.ratBkrationDetail.itemFlag"));
        langMap.put("table.ratBkrationDetail.itemCode", multiLangServiceI.getLang("table.ratBkrationDetail.itemCode"));
        langMap.put("table.ratBkrationDetail.creditType",
                multiLangServiceI.getLang("table.ratBkrationDetail.creditType"));
        langMap.put("table.ratBkration.money", multiLangServiceI.getLang("table.ratBkration.money"));
        langMap.put("table.ratBkration.businessType", multiLangServiceI.getLang("table.ratBkration.businessType"));
        langMap.put("table.ratBkrationDisDetail.superProcessMoney",
                multiLangServiceI.getLang("table.ratBkrationDisDetail.superProcessMoney"));
        langMap.put("table.ratBkrationDisDetail.checkProcessMoney",
                multiLangServiceI.getLang("table.ratBkrationDisDetail.checkProcessMoney"));
        langMap.put("table.ratBkrationDisDetail.overPlusMoney",
                multiLangServiceI.getLang("table.ratBkrationDisDetail.overPlusMoney"));
        langMap.put("table.ratBkrationDisDetail.leftDisMoney",
                multiLangServiceI.getLang("table.ratBkrationDisDetail.leftDisMoney"));
    }

    /**
     * 循环创建内容
     *
     * @param sheet
     * @param rowNum
     * @param rationDlt
     */
    private Integer createContent(HSSFSheet sheet, int rowNum, RatUrationUseDtlVO rationDlt, HSSFCellStyle contentStyle,
                                  Map<String, String> langMap, Map<String, String> dictsMap) {
        // 循环创建单品种授信内容
        if (CommonConstant.NO.equals(rationDlt.getItemFlag())) {
            HSSFRow sheetRow = sheet.createRow(rowNum++);
            HSSFCell cell0 = sheetRow.createCell(0);// 品种类型
            if (StringUtils.isEmpty(dictsMap.get("RatModel.ItemFlag_" + rationDlt.getItemFlag()))) {
                dictsMap.put("RatModel.ItemFlag_" + rationDlt.getItemFlag(),
                        dictService.get("RatModel.ItemFlag", rationDlt.getItemFlag()));
            }
            cell0.setCellValue(dictsMap.get("RatModel.ItemFlag_" + rationDlt.getItemFlag()));
            HSSFCell cell1 = sheetRow.createCell(1);// 授信品种
            cell1.setCellValue(rationDlt.getItemName());
            HSSFCell cell2 = sheetRow.createCell(2);// 授信方式
            if (StringUtils.isEmpty(dictsMap.get("rat.creditType_" + rationDlt.getCreditType()))) {
                dictsMap.put("rat.creditType_" + rationDlt.getCreditType(),
                        dictService.get("rat.creditType", rationDlt.getCreditType()));
            }
            cell2.setCellValue(dictsMap.get("rat.creditType_" + rationDlt.getCreditType()));
            HSSFCell cell3 = sheetRow.createCell(3);// 授信额度
            cell3.setCellValue(rationDlt.getMoney().toPlainString());
            HSSFCell cell4 = sheetRow.createCell(4);// 业务种类
            if (ListUtils.isNotNullOrEmpty(rationDlt.getSysBeans())) {
                for (RatUrationSysBean sysBean : rationDlt.getSysBeans()) {
                    if (StringUtils.isEmpty(dictsMap.get("ratUration.businessName_" + sysBean.getSysCode()))) {
                        dictsMap.put("ratUration.businessName_" + sysBean.getSysCode(),
                                dictService.get("ratUration.businessName", sysBean.getSysCode()));
                    }
                    cell4.setCellValue(dictsMap.get("ratUration.businessName_" + sysBean.getSysCode()));
                }
            } else {
                cell4.setCellValue("");
            }
            HSSFCell cell5 = sheetRow.createCell(5);// 占用额度
            cell5.setCellValue(rationDlt.getUseMoney().toPlainString());
            HSSFCell cell6 = sheetRow.createCell(6);// 再审占用额度
            cell6.setCellValue(rationDlt.getApproveMoney().toPlainString());
            HSSFCell cell7 = sheetRow.createCell(7);// 可用额度
            cell7.setCellValue(rationDlt.getOverPlusMoney().toPlainString());
            HSSFCell cell8 = sheetRow.createCell(8);// 剩余额度
            cell8.setCellValue(rationDlt.getLeftMoney().toPlainString());
            Iterator<Cell> iterator = sheetRow.cellIterator();
            while (iterator.hasNext()) {
                Cell cell = iterator.next();
                cell.setCellStyle(contentStyle);
            }
        } else {
            // 创建综合授信品种
            HSSFRow sheetRow = sheet.createRow(rowNum++);
            HSSFCell cell00 = sheetRow.createCell(0);
            if (StringUtils.isEmpty(dictsMap.get("RatModel.ItemFlag_" + rationDlt.getItemFlag()))) {
                dictsMap.put("RatModel.ItemFlag_" + rationDlt.getItemFlag(),
                        dictService.get("RatModel.ItemFlag", rationDlt.getItemFlag()));
            }
            cell00.setCellValue(dictsMap.get("RatModel.ItemFlag_" + rationDlt.getItemFlag()));
            HSSFCell cell01 = sheetRow.createCell(1);
            cell01.setCellValue("");
            HSSFCell cell02 = sheetRow.createCell(2);
            cell02.setCellValue("");
            HSSFCell cell03 = sheetRow.createCell(3);
            cell03.setCellValue(rationDlt.getMoney().toPlainString());
            HSSFCell cell04 = sheetRow.createCell(4);
            cell04.setCellValue("");
            HSSFCell cell05 = sheetRow.createCell(5);
            cell05.setCellValue(rationDlt.getUseMoney().toPlainString());
            HSSFCell cell06 = sheetRow.createCell(6);
            cell06.setCellValue(rationDlt.getApproveMoney().toPlainString());
            HSSFCell cell07 = sheetRow.createCell(7);
            cell07.setCellValue(rationDlt.getOverPlusMoney().toPlainString());
            HSSFCell cell08 = sheetRow.createCell(8);
            cell08.setCellValue(rationDlt.getLeftMoney().toPlainString());
            Iterator<Cell> iterator = sheetRow.cellIterator();
            while (iterator.hasNext()) {
                Cell cell = iterator.next();
                cell.setCellStyle(contentStyle);
            }
            // 循环创建综合品种内容
            for (RatUrationUseDtlVO dlt : rationDlt.getRationDlts()) {
                HSSFRow sheetRow1 = sheet.createRow(rowNum++);
                HSSFCell cell10 = sheetRow1.createCell(0);
                cell10.setCellValue("");
                HSSFCell cell11 = sheetRow1.createCell(1);
                cell11.setCellValue(dlt.getItemName());
                HSSFCell cell12 = sheetRow1.createCell(2);
                if (StringUtils.isEmpty(dictsMap.get("rat.creditType_" + dlt.getCreditType()))) {
                    dictsMap.put("rat.creditType_" + dlt.getCreditType(),
                            dictService.get("rat.creditType", dlt.getCreditType()));
                }
                cell12.setCellValue(dictsMap.get("rat.creditType_" + dlt.getCreditType()));
                HSSFCell cell13 = sheetRow1.createCell(3);
                cell13.setCellValue(dlt.getMoney().toPlainString());
                HSSFCell cell14 = sheetRow1.createCell(4);
                if (ListUtils.isNotNullOrEmpty(dlt.getSysBeans())) {
                    for (RatUrationSysBean sysBean : dlt.getSysBeans()) {
                        if (StringUtils.isEmpty(dictsMap.get("ratUration.businessName_" + sysBean.getSysCode()))) {
                            dictsMap.put("ratUration.businessName_" + sysBean.getSysCode(),
                                    dictService.get("ratUration.businessName", sysBean.getSysCode()));
                        }
                        cell14.setCellValue(dictsMap.get("ratUration.businessName_" + sysBean.getSysCode()));
                    }
                } else {
                    cell14.setCellValue("");
                }
                HSSFCell cell15 = sheetRow1.createCell(5);
                cell15.setCellValue(dlt.getUseMoney().toPlainString());
                HSSFCell cell16 = sheetRow1.createCell(6);
                cell16.setCellValue(dlt.getApproveMoney().toPlainString());
                HSSFCell cell17 = sheetRow1.createCell(7);
                cell17.setCellValue(dlt.getOverPlusMoney().toPlainString());
                HSSFCell cell18 = sheetRow1.createCell(8);
                cell18.setCellValue(dlt.getLeftMoney().toPlainString());
                Iterator<Cell> iterator1 = sheetRow1.cellIterator();
                while (iterator1.hasNext()) {
                    Cell cell = iterator1.next();
                    cell.setCellStyle(contentStyle);
                }
            }
        }
        return rowNum;
    }

    /**
     * 手工占用授信到期自动释放
     *
     * @param arguments
     */
    @Override
    @XxlJob("RatUrationService")
    public void run(String arguments) {
        Date endDate = DateUtils.getDateYMD();
        if (endDate != null) {
            List<RatUrationBeanDTO> urationBeanDTOList = ratUrationDao.queryRatUApplyList(endDate);
            for (RatUrationBeanDTO urationBeanDTO : urationBeanDTOList) {
                applyService.updateUrationRelease(urationBeanDTO.getBillId(), "");
            }
        }
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratUrationDao.queryBusBillByCorpId(corpId, RationConstant.RAT_TYPE_0);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATOCCUPY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    public List<String> calcRationMoney(List<RatUrationDisBean> disBeans) {
        List<RatBkUseVO> ratLists = BeanUtils.copyListProperties(disBeans, RatBkUseVO.class);
        statisticsSercive.groupbyRatUserVo(ratLists);// 计算授信品种的在审、占用额度情况
        Map<String, RatBkUseVO> ratMap = new HashMap<>();
        for (RatBkUseVO useVO : ratLists) {
            ratMap.put(useVO.getId(), useVO);// 分配ID
        }
        List<String> ratCodes = new ArrayList<>();
        for (RatUrationDisBean disBean : disBeans) {
            String ratCode = disBean.getRatCode();
            RatBkUseVO useVO = ratMap.get(disBean.getId());// 分配ID
            if (null != useVO) {
                disBean.setFreezeMoney(useVO.getFreezeMoney());// 在审
                disBean.setUseMoney(useVO.getUseMoney());// 占用
                disBean.setVirtualUration(useVO.getVirtualUration());// 虚拟在途占用
                disBean.setReliefAndUrationMoney(useVO.getReliefAndUrationMoney());// 虚拟占用
                disBean.setFreezeMaxMoney(useVO.getFreezeMaxMoney());// 在审
                disBean.setUseMaxMoney(useVO.getUseMaxMoney());// 占用
                disBean.setVirtualUrationMaxMoney(useVO.getVirtualUrationMaxMoney());// 虚拟在途占用
                disBean.setVirtualMaxMoney(useVO.getVirtualMaxMoney());// 虚拟占用
            }
            if (!ratCodes.contains(ratCode)) {
                ratCodes.add(ratCode);
            }
        }
        return ratCodes;
    }

    public List<RatUrationSysBean> queryRatUrationApply(List<String> ratCodes) {
        List<RatUrationCurVO> urationCurVOS = statisticsSercive.queryRatApplyMoney(ratCodes);// 授信占用情况
        Map<String, BigDecimal> rateMap = new HashMap<String, BigDecimal>();
        Map<String, RatUrationSysBean> beanMap = new HashMap<>();
        for (RatUrationCurVO curVO : urationCurVOS) {
            String disCode = curVO.getDisCode();
            String sysCode = curVO.getSysCode();
            String itemCode = curVO.getItemCode();
            Integer status = curVO.getStatus();
            String key = disCode + "_" + sysCode + "_" + itemCode + "_" + status;
            RatUrationSysBean sysBean = beanMap.get(key);
            if (null == sysBean) {
                sysBean = new RatUrationSysBean();
                sysBean.setRatCode(disCode);
                sysBean.setItemCode(itemCode);
                sysBean.setSysCode(sysCode);
                sysBean.setStatus(status.toString());
                beanMap.put(key, sysBean);
            }
            BigDecimal money = statisticsSercive.calBillMoney(curVO, rateMap);// 业务金额
            sysBean.setMoney(sysBean.getMoney().add(money));
        }
        return new ArrayList<RatUrationSysBean>(beanMap.values());
    }

    // 获取授信录入对应状态的授信占用数据(循环授信)
    public List<RatUrationDisBean> queryByCodeStatus(String ratCode, List<Integer> statuss) {
        return ratUrationDao.queryByCodeStatus(ratCode, statuss);
    }

    public List<String> queryRationBank() {
        return ratUrationDao.queryRationBank();
    }

    public List<String> queryRationBank(String corpId) {
        return ratUrationDao.queryRationBank(corpId);
    }

    public List<String> queryRationBill() {
        return ratUrationDao.queryRationBill();
    }

    /**
     * 通过业务id及编码查询授信挂接的担保方式
     *
     * @param businessIds
     * @param sysCode
     * @return
     */
    public List<com.bytter.credit.dto.GuaContractOccupDTO> getRatGuaWayNameList(List<String> businessIds, String sysCode) {
        return ratUrationDao.getRatGuaWayNameList(businessIds, sysCode);
    }

    /**
     * 根据授信编号和授信品种类型获取授信信息
     *
     * @param corpId     授信单位ID
     * @param contractNo 授信合同编号
     * @param itemName   授信品种名称
     * @return 授信信息
     */
    public RatUrationDisDTO getRatUrationDTOByParams(String corpId, String contractNo, String itemName) {
        RatUrationDisDTO ratUrationDisDTO = ratUrationDao.getRatUrationDisDTOByParams(corpId, contractNo);
        if (ratUrationDisDTO != null) {
            ratUrationDisDTO.setRationDlts(ratUrationDao.getRatUrationDisDtlDTOByParams(ratUrationDisDTO.getId(), itemName));
        }
        return ratUrationDisDTO;
    }

    /**
     * @Description 查询授信占用中业务单据为空的数据
     * @author zhous
     * @date 2024/8/20 15:55
     */
    public List<RatUration> getRationBillNoIsNullList(String syscode, String businessName) {
        return ratUrationDao.getRationBillNoIsNullList(syscode, businessName);
    }

    /**
     * @Description 补充业务编号
     * @author zhous
     * @date 2024/8/22 9:31
     */
    public void updateBillNoSupply(String id, String origNo) {
        ratUrationDao.updateBillNoSupply(id, origNo);
    }

    /**
     * 银行授信额度占用明细导出
     *
     * @param rations 导出的数据
     * @return 表格
     */
    public HSSFWorkbook exportOccupyDtl(List<RatUrationUseBeanVO> rations) {
        // 创建工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = wb.createSheet("银行授信额度占用明细");
        // 设置列宽
        this.setColumnWidth(sheet);
        // 画表格
        this.createExcel(wb, sheet);
        // 写数据
        this.createData(wb, sheet, rations);
        return wb;
    }

    /**
     * 画表格
     *
     * @param wb    工作薄
     * @param sheet sheet页
     */
    private void createExcel(HSSFWorkbook wb, HSSFSheet sheet) {
        // 获取表头样式
        HSSFCellStyle headStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置字体
        headStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 16));
        // 获取二级表头
        HSSFCellStyle secondStyle = ExportUtils.getCenterCellStyle(wb, true);
        // 设置二级表头字体
        secondStyle.setFont(ExportUtils.getHeadFont(wb, "宋体", (short) 12));

        // 创建第一行
        HSSFRow row1 = sheet.createRow(0);
        // 创建行高
        row1.setHeight((short) 600);
        // 创建第一行第一列
        HSSFCell cell11 = row1.createCell(0);
        cell11.setCellValue(new HSSFRichTextString("银行授信额度占用明细"));
        // 设置表头单元格样式，第一行表头占多列，需要合并，需循环设置每个单元格格式
        this.setCellStyle(row1, headStyle);
        // 第一行合并
        CellRangeAddress range11113 = new CellRangeAddress(0, 0, 0, 12);
        sheet.addMergedRegion(range11113);

        // 创建第二行
        HSSFRow row2 = sheet.createRow(1);
        // 创建行高
        row2.setHeight((short) 400);
        String[] heads = new String[]{"额度使用单位", "业务名称", "占用授信品种", "授信方式", "占用币别", "业务币别", "金额", "业务金额", "汇率", "开始日期", "预计结束日期", "业务单号", "状态"};
        for (int a = 0; a < heads.length; a++) {
            HSSFCell cell = row2.createCell(a);
            cell.setCellValue(heads[a]);
        }
        this.setCellStyle(row2, secondStyle);
    }

    /**
     * 写数据
     *
     * @param wb      工作薄
     * @param sheet   sheet页
     * @param rations 导出数据
     */
    private void createData(HSSFWorkbook wb, HSSFSheet sheet, List<RatUrationUseBeanVO> rations) {
        // 文字居中
        HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(wb, false);
        // 金额靠右
        HSSFCellStyle rightCellStyle = ExportUtils.getRightCellStyle(wb, false);
        DataFormat currencyDataFormat = wb.createDataFormat();
        rightCellStyle.setDataFormat(currencyDataFormat.getFormat("#,##0.00"));

        int rowIndex = 2;// 数据开始行

        for (RatUrationUseBeanVO item : rations) {
            // 创建数据行
            HSSFRow row = sheet.createRow(rowIndex);

            // 创建第1列：额度使用单位
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellStyle(centerCellStyle);
            cell1.setCellValue(new HSSFRichTextString(item.getCorpName()));
            // 创建第2列：业务名称
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellStyle(centerCellStyle);
            cell2.setCellValue(new HSSFRichTextString(dictService.getByName("ratUration.businessName", item.getBusinessName())));
            // 创建第3列：占用授信品种
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellStyle(centerCellStyle);
            cell3.setCellValue(new HSSFRichTextString(item.getItemName()));
            // 创建第4列：授信方式
            HSSFCell cell4 = row.createCell(3);
            cell4.setCellStyle(centerCellStyle);
            cell4.setCellValue(new HSSFRichTextString(dictService.getByName("rat.creditType", item.getCreditType())));
            // 创建第5列：占用币别
            HSSFCell cell5 = row.createCell(4);
            cell5.setCellStyle(centerCellStyle);
            cell5.setCellValue(new HSSFRichTextString(item.getCurName()));
            // 创建第6列：业务币别
            HSSFCell cell6 = row.createCell(5);
            cell6.setCellStyle(centerCellStyle);
            cell6.setCellValue(new HSSFRichTextString(item.getInitCurName()));
            // 创建第7列：金额
            HSSFCell cell7 = row.createCell(6);
            cell7.setCellStyle(rightCellStyle);
            cell7.setCellValue(null == item.getMoney() ? BigDecimal.ZERO.doubleValue() : item.getMoney().doubleValue());
            // 创建第8列：业务金额
            HSSFCell cell8 = row.createCell(7);
            cell8.setCellStyle(rightCellStyle);
            cell8.setCellValue(null == item.getBillMoney() ? BigDecimal.ZERO.doubleValue() : item.getBillMoney().doubleValue());
            // 创建第9列：汇率
            HSSFCell cell9 = row.createCell(8);
            cell9.setCellStyle(centerCellStyle);
            cell9.setCellValue(null == item.getExchangeRate() ? "" : item.getExchangeRate().toString());
            // 创建第10列：开始日期
            HSSFCell cell10 = row.createCell(9);
            cell10.setCellStyle(centerCellStyle);
            cell10.setCellValue(DateUtils.format(item.getBeginDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            // 创建第11列：预计结束日期
            HSSFCell cell11 = row.createCell(10);
            cell11.setCellStyle(centerCellStyle);
            cell11.setCellValue(DateUtils.format(item.getEndDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            // 创建第12列：业务单号
            HSSFCell cell12 = row.createCell(11);
            cell12.setCellStyle(centerCellStyle);
            cell12.setCellValue(new HSSFRichTextString(item.getBillNo()));
            // 创建第13列：状态
            HSSFCell cell13 = row.createCell(12);
            cell13.setCellStyle(centerCellStyle);
            if ("06".equals(item.getBusinessName()) || "07".equals(item.getBusinessName())) {
                cell13.setCellValue(new HSSFRichTextString(dictService.getByName("common.status", item.getStatus().toString())));
            } else {
                cell13.setCellValue(new HSSFRichTextString(dictService.getByName("uration.status", item.getStatus().toString())));
            }

            rowIndex++;
        }
    }

    /**
     * 设置列宽
     *
     * @param sheet sheet页
     */
    private void setColumnWidth(HSSFSheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 18);
        sheet.setColumnWidth(0, 30 * 256);
        for (int i = 1; i < 13; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }
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

    /**
    * @Description 票据指令失败打回
    * @author zhous
    * @date 2025/5/21 17:53
    */
    public void billReleaseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.billReleaseRation(uration, urationDTO.getUserId());
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.billReleaseRation(uration, urationDTO.getUserId());
            }
        }
        getEntityDao().flush();// 刷新提交，授信相关占用及时获取数据
    }
}
