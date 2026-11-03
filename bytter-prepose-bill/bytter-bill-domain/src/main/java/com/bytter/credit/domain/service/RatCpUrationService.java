package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDetailDao;
import com.bytter.credit.domain.dao.RatCpUrationDao;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.RatUrationBean;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.domain.vo.RatUrationUseBeanVO;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCpUrationService
 * @Description:内部授信结转Service
 * @date 2018年11月28日 下午3:54:48
 */
@Service("RatCpUrationService")
@Transactional
public class RatCpUrationService extends BaseService<RatUration, String>
        implements ITaskService, ICorpDisableVerifyService {
    @Autowired
    private RatCpUrationDao ratCpUrationDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private RatBkrationService rationService;
    @Autowired
    private RatCprationService ratCprationService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatCprationApplyService applyService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatItemCustomService itemCustomService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @Autowired
    private RatBkrationDetailDao ratDetailDao;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 查询内部授信可结转数据
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput queryCpRatCanChangeList(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "ru.corp_Id", true);
        return ratCpUrationDao.queryRatURationList(input, corpAuthSql);
    }
    /**
     * 根据系统类型和业务编号获取占用信息
     */
    @Transactional(readOnly = true)
    public List<RatUration> queryRatUrationById(String sysCode, String billNo) {
        return ratCpUrationDao.queryRatUrationById(sysCode, billNo);
    }
    /**
     * 金额非空处理
     */
    @SuppressWarnings("rawtypes")
    public void saveOrUpdateUration(RatUration apply) {
        Class userCla = (Class) apply.getClass();
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
     * 根据系统业务和业务单号获取授信占用信息
     */
    public RatUrationDTO getRatUrationDTOByBillId(String sysCode, String billId) {
        List<RatUration> ratUrations = ratCpUrationDao.queryRatUrationById(sysCode, billId);
        if (!EmptyUtils.isEmptyList(ratUrations)) {
            RatUration uration = ratUrations.get(0);
            RatUrationDTO urationDTO = new RatUrationDTO();
            BeanUtils.copyProperties(uration, urationDTO);
            urationDTO.setRatItemCode(uration.getItemCode());
            urationDTO.setUrationId(uration.getId());
            RatItemCustom itemCustom = itemCustomService.get(uration.getItemCode(), true);
            if (null != itemCustom) {
                urationDTO.setRatItemName(itemCustom.getName());// 品种名称
            }
            String isRmbRat = RationConstant.RAT_COMMON_Y;// 默认折合本位币
            if (uration.getCurCode().equals(uration.getInitCurCode())) {
                isRmbRat = RationConstant.RAT_COMMON_N;
            }
            // 获取额度剩余金额
            List<RatUrationDisDTO> disDTOs = viewCorpDisRation(urationDTO, isRmbRat);
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
     * 检查授信是否可进行结转
     */
    public List<RatUrationBean> checkChangeRationCondition(String urationIds) {
        String[] urationIdArr = urationIds.split(";");
        List<RatUrationBean> urationBeans = new ArrayList<RatUrationBean>();
        for (String urationId : urationIdArr) {
            RatUrationBean urationBean = checkChangeRation(urationId);
            if ("2".equals(urationBean.getResultCode())) {// 存在包含关系的授信,需要选择具体结转的授信明细
                urationBeans.add(urationBean);
            }
        }
        return urationBeans;
    }
    /**
     * 相同授信品种结转
     * 
     * @param
     * @return
     */
    public void changeSameRationCode(String urationIds, String joinFlag, String userId) {
        if (StringUtils.isEmpty(urationIds)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_USE_SELECTNULL);
        }
        String[] urationIdArr = urationIds.split(";");
        for (String urationId : urationIdArr) {
            processRationChange(urationId, joinFlag, userId);
        }
    }
    /**
     * 对某个占用品种进行内部授信结转过程
     * 
     * @param
     * @return
     */
    public void processRationChange(String urationId, String joinFlag, String userId) {
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        // 1.获得授信占用信息
        RatUration uration = null;
        if (!StringUtils.isEmpty(joinFlag)) {
            String urationArr[] = urationId.split(",");
            uration = get(urationArr[0], true);
            urationDTO.setId(urationArr[0]);
            if (urationArr.length > 1) {
                urationDTO.setNewRatCode(urationArr[1]);
            }
        } else {
            uration = get(urationId, true);
            urationDTO.setId(urationId);
        }
        // 2.获得已经占用的内部授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);
        // 3.获取同单位同币别的其他授信数据
        urationDTO.setType(uration.getType());// 业务类型
        urationDTO.setBeginDate(uration.getBeginDate());// 占用开始日期
        urationDTO.setEndDate(uration.getEndDate());// 占用结束日期
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setItemFlag(uration.getItemFlag());
        urationDTO.setItemCode(uration.getItemCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_1);// 相同品种结转
        // checkBefore();
        processCorpRecentRation(urationDTO);
        // 新占用授信品种额度占用
        processRationData(uration, urationDTO, userId);
    }
    /**
     * 检查所选授信是否可结转
     * 
     * @param
     * @return
     */
    public RatUrationBean checkChangeRation(String urationId) {
        // 1.获得授信占用信息
        RatUration uration = get(urationId, true);
        // 2.获得已经占用的银行授信录入信息
        RatBkration bkration = rationService.get(uration.getRatCode(), true);
        // 3.获取同单位银行币别的其他授信数据
        RatUrationBeanDTO urationDTO = new RatUrationBeanDTO();
        urationDTO.setId(urationId);
        urationDTO.setType(uration.getType());// 业务类型
        urationDTO.setBeginDate(uration.getBeginDate());// 占用开始日期
        urationDTO.setEndDate(uration.getEndDate());// 占用结束日期
        urationDTO.setBankCode(bkration.getBankCode());
        urationDTO.setCurCode(bkration.getCurCode());
        urationDTO.setCorpId(uration.getCorpId());// 占用单位ID
        urationDTO.setRatCode(uration.getRatCode());
        urationDTO.setItemFlag(uration.getItemFlag());
        urationDTO.setItemCode(uration.getItemCode());
        urationDTO.setMoney(uration.getMoney());// 占用额度
        urationDTO.setChangeType(RationConstant.RAT_CHANGE_TYPE_1);// 相同品种结转
        RatUrationBean urationBean = new RatUrationBean();
        urationBean.setMoney(uration.getMoney());
        urationBean.setCorpId(uration.getCorpId());// 占用单位ID
        urationBean.setUrationId(urationId);
        urationBean.setUrationCode(uration.getUrationCode());// 占用编号
        List<RatBkRationVm> rationList = ratCprationService.queryCorpOtherRation(urationDTO);
        // 比较授信之间是否具有包含关系
        String resultCode = "1";
        if (EmptyUtils.isEmptyList(rationList)) {
            resultCode = "0";// 没有授信明细
            urationBean.setResultCode(resultCode);
            return urationBean;
        }
        if (rationList.size() == 1) {
            resultCode = "1";// 只有一条新授信录入信息
        }
        Map<String, RatBkRationVm> containMap = new HashMap<String, RatBkRationVm>();
        for (RatBkRationVm rbk1 : rationList) {
            String ratCode1 = rbk1.getId();
            for (RatBkRationVm rbk2 : rationList) {
                String ratCode2 = rbk2.getId();
                if (ratCode1.equals(ratCode2)) {
                    continue;
                }
                // 同终点,不同起点(结束日期小于等于，开始日期大)
                if ((DateUtils.dayDiffs(rbk2.getEnddate(), rbk1.getEnddate()) <= 0
                        && DateUtils.dayDiffs(rbk2.getBegindate(), rbk1.getBegindate()) > 0)) {
                    containMap.put(ratCode2, rbk2);
                    containMap.put(ratCode1, rbk1);
                }
                // 同起点，不同终点(结束日期小于，开始日期大于等于)
                if ((DateUtils.dayDiffs(rbk2.getEnddate(), rbk1.getEnddate()) < 0
                        && DateUtils.dayDiffs(rbk2.getBegindate(), rbk1.getBegindate()) >= 0)) {
                    containMap.put(ratCode2, rbk2);
                    containMap.put(ratCode1, rbk1);
                }
                // 不同起点不同终点
                if ((DateUtils.dayDiffs(rbk2.getEnddate(), rbk1.getEnddate()) < 0
                        && DateUtils.dayDiffs(rbk2.getBegindate(), rbk1.getBegindate()) > 0)) {
                    containMap.put(ratCode2, rbk2);
                    containMap.put(ratCode1, rbk1);
                }
            }
        }
        if (containMap.isEmpty() && rationList.size() == 1) {
            containMap.put(rationList.get(0).getId(), rationList.get(0));
        }
        if (containMap.size() > 1) {
            resultCode = "2";// 存在包含关系的授信数据
        }
        urationBean.setResultCode(resultCode);
        // 授信集合ID
        List<String> ratCodes = new ArrayList<String>();
        ratCodes.addAll(containMap.keySet());
        urationBean.setRatCodes(ratCodes);
        return urationBean;
    }
    /**
     * 获取单位最近的授信录入信息并校验结转条件
     */
    private void processCorpRecentRation(RatUrationBeanDTO uration) {
        List<RatBkRationVm> rationList = null;
        if (StringUtils.isEmpty(uration.getNewRatCode())) {// 没有选择授信额度单，查询授信额度单
            rationList = ratCprationService.queryCorpOtherRation(uration);
        } else {// 直接获取授信额度数据
            rationList = ratCprationService.queryRationByRatCode(uration);
        }
        if (null == rationList || rationList.isEmpty()) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            // 取单品种的第一条数据
            RatBkRationVm rationDTO = rationList.get(0);
            // 新授信明细和原占用单位、品种获取分配明细
            List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(rationDTO.getId(),
                    uration.getCorpId(), uration.getItemFlag(), uration.getItemCode());// 授信分配明细
            // 检查授信额度是否足够结转
            validSingleRationMoney(uration, rationDTO, dtos);
        }
    }
    /**
     * 处理授信结转业务数据
     */
    public void processRationData(RatUration uration, RatUrationBeanDTO urationDTO, String userId) {
        RatUrationApply newApply = null;
        // 从额度占用中直接占用授信
        if (ConfigConstant.RAT.equals(uration.getSyscode())) {// 系统占用类型
            // 原授信占用申请数据不变，将原授信占用数据转为历史数据
            RatUrationApply apply = applyService.get(uration.getBillId(), true);// 授信业务ID
            newApply = new RatUrationApply();
            BeanUtils.copyProperties(apply, newApply, new String[] { "id" });
            newApply.setApplyCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_APPLYCODE,
                    RationConstant.RAT_URATION_APPLY_CODE));// 授信占用申请编号
            newApply.setStatus(RationConstant.STATUS_DIS_HIS);// 占用历史数据
            applyService.saveEntity(newApply);// 保存历史占用申请记录
            this.getSession().flush();
            // 原授信申请记录关联新的授信占用信息
            apply.setItemCode(uration.getItemCode());
            apply.setRatCode(urationDTO.getNewRatCode());// 结转后新的授信编号
            apply.setDisCode(urationDTO.getNewDisCode());// 结转后新的分配编号
            apply.setUpdateBy(userId);
            apply.setUpdateDate(DateUtils.getDate());
            applyService.updateEntity(apply);// 更新到授信占用申请表
            this.getSession().flush();
        }
        RatUration newUration = new RatUration();
        BeanUtils.copyProperties(uration, newUration, new String[] { "id" });
        newUration.setUrationCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_URATIONCODE,
                RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
        newUration.setStatus(RationConstant.RAT_USE_STATUS_CLOSE);// 授信结转
        if (ConfigConstant.RAT.equals(uration.getSyscode())) {// 系统占用类型
            newUration.setBillId(newApply.getId());
            newUration.setBillNo(newApply.getApplyCode());
            newUration.setRatCode(newApply.getRatCode());// 原授信编号
        }
        saveEntity(newUration);// 保存历史授信占用台账
        uration.setRatCode(urationDTO.getNewRatCode());// 结转后新的授信编号
        uration.setDisCode(urationDTO.getNewDisCode());// 结转后新的分配编号
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        updateEntity(uration);
        getEntityDao().flush();
        // 更新分配表中新占用授信编号的授信占用额度
        updateRatRationAndDisInfo(uration);
        // 内部授信转移成功消息通知
        notificationService.send(uration.getId(), uration.getCorpId(), NotificationConstant.CMS.RAT_CPRATION_CHANGE,
                uration);
    }
    /**
     * 检查授信单品种分配额度是否足够结转
     */
    public boolean validSingleRationMoney(RatUrationBeanDTO uration, RatBkRationVm rationDTO,
            List<RatBkrationDisDtlDTO> dtos) {
        boolean flag = false;
        if (null == dtos || dtos.isEmpty()) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DISCODE_NOTEXISTS);
        }
        // 单品种项结转单项
        RatBkrationDisDtlDTO dtlDTO = dtos.get(0);
        // 查询出新授信品种中可使用额度 = 已分配金额+被调剂过来的金额-调剂出去的金额-正式占用-冻结金额
        BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                .subtract(dtlDTO.getSuperProcessMoney());
        if (uration.getMoney().compareTo(disMoney) > 0) {// 占用额度大于剩余额度
            flag = false;
            // 查询新授信品种中可使用的额度
            throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_LOWER, dtlDTO.getItemName());
        }
        uration.setNewRatCode(rationDTO.getId());// 新的授信编号
        uration.setNewDisCode(dtlDTO.getDisCode());// 新的分配编号
        return flag;
    }
    /**
     * 根据新的授信占用信息改变银行授信录入与分配的信息。 原来的授信分配中冻结或占用的金额是没有改变的
     */
    public void updateRatRationAndDisInfo(RatUration uration) {
        applyService.updateRationDisMoney(uration, false);// 不需要更新冻结额度
    }
    /**
     * 授信额度单选择
     */
    public List<RatUrationBean> queryChangeRation(String urationIds) {
        String[] urationIdArr = urationIds.split(";");
        List<RatUrationBean> urationBeans = new ArrayList<RatUrationBean>();
        for (String urationId : urationIdArr) {
            RatUrationBean urationBean = checkChangeRation(urationId);
            if (!EmptyUtils.isEmptyList(urationBean.getRatCodes())) {
                List<RatUrationDisBean> rationDtls = ratCpUrationDao.queryCorpRatUration(urationBean.getCorpId(),
                        urationBean.getRatCodes());
                // 设置占用剩余额度
                for (RatUrationDisBean disBean : rationDtls) {
                    // 在审占用额度+追减
                    BigDecimal approveMoney = disBean.getFreezeMoney();
                    disBean.setApproveMoney(approveMoney);// 在审占用
                    disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney));// 可用额度
                    // 剩余额度 = 在审+可用(包含虚拟在途占用和虚拟在途调剂，这些不能在真实在途占用中)
                    disBean.setLeftMoney(disBean.getApproveMoney().add(disBean.getOverPlusMoney()));
                }
                urationBean.setRationDtls(rationDtls);
            }
            urationBeans.add(urationBean);
        }
        return urationBeans;
    }
    /**
     * 授信挂接数据查询
     */
    public List<RatUrationDisDTO> queryCorpDisRation(UrationParamDTO paramDTO, RatUrationDTO urationDTO) {
        Date beginDate = paramDTO.getBeginDate();
        if (null == beginDate) {// 对录入时间进行处理，去掉时分秒，便于取数据
            beginDate = DateUtils.getDate();
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            String date1 = date.format(beginDate);
            Date date2;
            try {
                date2 = date.parse(date1);
                paramDTO.setBeginDate(date2);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        String isRmbRat = paramDTO.getIsRmbRat();// 折合本位币
        if (RationConstant.RAT_COMMON_Y.equals(isRmbRat)) {
            // 查询本位币
            BtCurrencyDTO currencyDTO = currencyService.getNatualCurrency();
            paramDTO.setCurCode(currencyDTO.getId());// 本位币
        }
        List<RatUrationDisBean> disBeans = ratCpUrationDao.queryCorpRatDisration(paramDTO);
        paramDTO.setRatCode(urationDTO.getRatCode());
        // 对授信明细进行分组
        List<RatUrationDisDTO> disDTOs = new ArrayList<RatUrationDisDTO>();
        Map<String, RatUrationDisDTO> disMap = new HashMap<String, RatUrationDisDTO>();
        String itemCode = paramDTO.getItemCode();// 页面选择的授信品种
        if (!StringUtils.isEmpty(itemCode)) {
            itemCode = itemCode.trim();// 非空限制
        }
        if (EmptyUtils.isEmptyList(disBeans)) {
            disBeans = new ArrayList<RatUrationDisBean>();
        }
        //TODO 做了字段冗余暂不需要
//        BtCurrencyDTO ccyDTO = currencyService.getObjectById(paramDTO.getCurCode());
//        CorpDTO corpDTO = corpService.get(paramDTO.getCorpId());
        for (RatUrationDisBean disBean : disBeans) {
            String ratCode = disBean.getRatCode();
//            Optional.ofNullable(ccyDTO).ifPresent(curr -> disBean.setCurName(curr.getCurName()));
//            Optional.ofNullable(corpDTO).ifPresent(crop -> {
//                disBean.setCorpNo(crop.getCode());
//                disBean.setCorpName(crop.getName());
//            });
            RatUrationDisDTO disDTO = disMap.get(ratCode);
            List<String> ratCodes = new ArrayList<String>();
            ratCodes.add(ratCode);
            if (null == disDTO) {
                disDTO = new RatUrationDisDTO();
                BeanUtils.copyProperties(disBean, disDTO);
                disDTO.setRationDlts(new ArrayList<RatUrationDisDtlDTO>());
                disMap.put(ratCode, disDTO);
            }
            // if(disBean.getDisWay().equals(RationConstant.DIS_WAY_AUTO)){//内部授信分配方式为自动分配时
            // BigDecimal leftMoney=null;
            // List<RatBkRationDtlVm> ratDtl=ratDetailDao.getBkrationDtlByRatCodes(ratCodes);
            // for (RatBkRationDtlVm ratDTO : ratDtl) {
            // if(ratDTO.getItemCode().equals(disBean.getItemCode())){
            // leftMoney=disBean.getMoney().subtract(ratDTO.getDisMoney());//授信录入直接被占用时减去分配出去的额度
            // }
            // }
            // // 在审占用额度+追减
            // BigDecimal approveMoney = disBean.getFreezeMoney();
            // disBean.setApproveMoney(approveMoney);// 在审占用
            // disBean.setLeftMoney(leftMoney.subtract(disBean.getUseMoney()).subtract(approveMoney));//剩余=录入-分配-占用-在审占用
            //
            // }else{
            // 在审占用额度+追减
            BigDecimal approveMoney = disBean.getFreezeMoney();
            disBean.setApproveMoney(approveMoney);// 在审占用
            disBean.setOverPlusMoney(disBean.getMoney().subtract(disBean.getUseMoney()).subtract(approveMoney)
                    .subtract(disBean.getReliefAndUrationMoney()));// 可用额度=授信额度-占用-在审-虚拟占用
            // 剩余额度 =可用
            disBean.setLeftMoney(disBean.getOverPlusMoney());
            // }
            double ratOccupyRatio = 0;
            if (disBean.getMoney().doubleValue() != 0) {
                ratOccupyRatio = ((disBean.getApproveMoney().add(disBean.getUseMoney())).divide(disBean.getMoney(), 4,
                        BigDecimal.ROUND_HALF_UP)).doubleValue();
            }
            disBean.setRatOccupyRatio(ratOccupyRatio);
            RatUrationDisDtlDTO disDtlDTO = new RatUrationDisDtlDTO();
            BeanUtils.copyProperties(disBean, disDtlDTO);
            disDTO.addRationDtl(disDtlDTO);
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
                if(ratMoneyTotal.doubleValue()!=0){
                    ratOccupiedRation = uratMoneyTotal.divide(ratMoneyTotal, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                disDTO.setRatOccupiedRation(ratOccupiedRation);
                disDTO.setRationSize(disDTO.getRationDlts().size());
            }
        }
        // setSelectRationItemMoney(urationDTO, disDTOs);
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
     * 检查是否包含选择的品种
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
     * 授信挂接数据查询
     */
    public List<RatUrationDisDTO> viewCorpDisRation(RatUrationDTO urationDTO, String isRmbRat) {
        List<RatUrationDisDTO> result = new ArrayList<RatUrationDisDTO>();
        List<RatUrationDisDtlDTO> resultDtl = new ArrayList<RatUrationDisDtlDTO>();
        UrationParamDTO paramDTO = new UrationParamDTO();
        BeanUtils.copyProperties(urationDTO, paramDTO);
        paramDTO.setItemCode(urationDTO.getRatItemCode());// 授信品种
        if (RationConstant.RAT_COMMON_Y.equals(isRmbRat)) {
            // 查询本位币
            BtCurrencyDTO currencyDTO = currencyService.getNatualCurrency();
            paramDTO.setCurCode(currencyDTO.getId());// 本位币
        }
        List<RatUrationDisDTO> rations = queryCorpDisRation(paramDTO, urationDTO);
        String itemCode = urationDTO.getRatItemCode();// 根据授信品种判断是单品种还是综合品种
        List<RatUrationDisBean> disBeans = ratCpUrationDao.queryCorpRatDisration(paramDTO);
        Map<String, RatUrationDisBean> disMap = Maps.uniqueIndex(disBeans, new Function<RatUrationDisBean, String>() {
            public String apply(RatUrationDisBean input) {
                return input.getItemCode();
            }
        });
        RatUrationDisBean disBean = disMap.get(itemCode);
        String itemFlag = RationConstant.RAT_COMFLAG_0;
        if (null != disBean) {
            itemFlag = disBean.getItemFlag();// 品种类型
        }
        // 比较授信数据
        for (RatUrationDisDTO disDTO : rations) {
            List<RatUrationDisDtlDTO> rationDtls = disDTO.getRationDlts();
            for (RatUrationDisDtlDTO disDtlDTO : rationDtls) {
                if (disDtlDTO.getItemFlag().equals(itemFlag)) {
                    resultDtl.add(disDtlDTO);
                }
            }
            result.add(disDTO);
        }
        return result;
    }
    /**
     * 授信占用冻结
     */
    public void useCpRation(RatUrationDTO urationDTO, Integer urationStatus) {
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
            // 查询已审批通过的授信分配信息
            RatBkrationDisListDTO disDTO = disService.getCorpBkrationDisListDTO(urationDTO.getRatCode(),
                    urationDTO.getCorpId());
            editUrationInfo(urationDTO, uration, disDTO, urationStatus);
        }
        getEntityDao().flush();// 刷新缓存，保存占用数据
        // 冻结授信占用额度
        if (RationConstant.RAT_USE_STATUS_FREEZE.equals(urationStatus)) {
            applyService.updateRationDisMoney(uration, true);
        }
    }
    /**
     * 授信占用暂存，不占用授信额度
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
     * 授信占用保存更新
     */
    private void editUrationInfo(RatUrationDTO urationDTO, RatUration uration, RatBkrationDisListDTO disDTO,
            Integer urationStatus) {
        String itemCode = urationDTO.getRatItemCode();
        if (EmptyUtils.isEmpty(urationDTO.getUrationId())) {// 新增的试试需要重新生成占用台账编号
            BeanUtils.copyProperties(urationDTO, uration);
            uration.setSyscode(urationDTO.getSysCode());// 系统占用模块
            uration.setRatType(RationConstant.RAT_TYPE_1);// 企业授信
            uration.setItemFlag(RationConstant.RAT_COMFLAG_0);// 单品种授信
            uration.setInitCurCode(urationDTO.getCurCode());// 最初币别
            uration.setUrationCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 内部授信占用台账编号
            uration.setCreateBy(urationDTO.getUserId());
            uration.setCreateDate(DateUtils.getDate());
        } else {
            uration.setUpdateDate(DateUtils.getDate());
            uration.setUpdateBy(urationDTO.getUserId());
        }
        // 获取授信的币别
        RatBkrationDis dis = disService.get(disDTO.getId(), true);// 授信分配信息
        RatBkration ration = rationService.get(dis.getRatCode(), true);// 授信录入信息
        BigDecimal curRate = BigDecimal.valueOf(1l);
        uration.setCurCode(ration.getCurCode());// 占用币别
        if (!urationDTO.getCurCode().equals(ration.getCurCode())) {// 相同币别
            curRate = rateAdjustService.getExchangeRateBy(urationDTO.getCurCode(), urationDTO.getBeginDate());// 获取币别汇率
        }
        uration.setExchangeRate(curRate);// 汇率
        uration.setStatus(urationStatus);
        uration.setPreMoney(urationDTO.getMoney());// 折算前金额
        uration.setDisCode(disDTO.getId());// 授信分配编号
        uration.setItemCode(itemCode);
        RatItemCustom itemCustom = itemCustomService.get(itemCode, true);// 授信品种信息
        if (null != itemCustom) {
            uration.setType(itemCustom.getSysItemCode());// 系统授信品种
        }
        singleRatUration(uration.getMoney(), uration, disDTO);
        saveOrUpdateUration(uration);
        urationDTO.setUrationId(uration.getId());// 返回占用ID
    }
    /**
     * 单品种占用额度检查
     */
    private void singleRatUration(BigDecimal uMoney, RatUration uration, RatBkrationDisListDTO disDTO) {
        List<RatBkrationDisDtlDTO> singRationDtls = disDTO.getRationDtls();
        Map<String, RatBkrationDisDtlDTO> rationMap = Maps.uniqueIndex(singRationDtls,
                new Function<RatBkrationDisDtlDTO, String>() {
                    public String apply(RatBkrationDisDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        RatBkrationDisDtlDTO dtlDTO = rationMap.get(uration.getItemCode());// 占用的分配明细
        if (null != dtlDTO) {
            // 比较授信额度是否足够
            // 品种剩余最高额度 = 已分配最高金额+被调剂过来的最高金额-调剂出去的金额-正式占用-冻结金额
            BigDecimal disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                    .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                    .subtract(dtlDTO.getSuperProcessMoney());
            if (uMoney.compareTo(disMoney) > 0) {// 占用额度大于剩余额度
                // 新授信品种中可使用的额度
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMCODE_MONEY_ENOUGH, dtlDTO.getItemName());
            }
        }
    }
    /**
     * 授信占用后删除
     */
    public void deleteUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID删除，如果没有则根据业务ID和业务系统类型查找来删除
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            releseUration(uration, urationDTO.getUserId());
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                releseUration(uration, urationDTO.getUserId());
            }
        }
    }
    /**
     * 释放授信
     */
    private void releseUration(RatUration uration, String userId) {
        if (!RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {
            uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
            uration.setUpdateBy(userId);
            uration.setUpdateDate(DateUtils.getDate());
            this.updateEntity(uration);
        }
    }
    /**
     * 授信占用释放
     */
    public void releaseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放
        // 新增加授信占用信息，状态为已释放
        RatUration uration = null;
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            uration = get(urationDTO.getUrationId(), true);
        } else {
            // 根据系统种类业务ID查找占用的数据
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
            }
        }
        if (null == uration) {
            return;
        }
        RatUration newUration = new RatUration();
        BeanUtils.copyProperties(uration, newUration, new String[] { "id", "money", "urationCode" });
        if (urationDTO.isAllRelease()) {// 如果是全部释放
            newUration.setMoney(uration.getMoney());// 释放额度为原占用额度
            urationDTO.setMoney(uration.getMoney());
        } else {
            newUration.setMoney(urationDTO.getMoney());
        }
        // 如果是最后一笔释放,则释放全部额度
        BigDecimal uMoney = uration.getMoney();// 原占用额度
        BigDecimal newUMoney = urationDTO.getMoney();// 新释放额度
        BigDecimal leftMoney = uMoney.subtract(newUMoney);// 剩余占用额度
        if (leftMoney.compareTo(BigDecimal.ZERO) <= 0) {
            newUration.setMoney(uration.getMoney());// 释放额度为原占用额度
            uration.setMoney(BigDecimal.ZERO);// 剩余占用额度
            uration.setPreMoney(BigDecimal.ZERO);
            uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 已释放
            super.updateEntity(uration);
        } else {
            if (urationDTO.isAllRelease()) {// 如果是全部释放
                newUration.setMoney(uration.getMoney());// 释放额度为原占用额度
                uration.setMoney(BigDecimal.ZERO);// 剩余占用额度
                uration.setPreMoney(BigDecimal.ZERO);
                uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 已释放
                updateEntity(uration);
            } else {// 释放部分的情况
                uration.setMoney(uMoney.subtract(newUMoney));// 减去已还额度
                uration.setPreMoney(uMoney.subtract(newUMoney));
                updateEntity(uration);
            }
        }
        newUration.setUrationCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_URATIONCODE,
                RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
        newUration.setPreMoney(urationDTO.getMoney());// 折算前金额
        newUration.setCreateBy(urationDTO.getUserId());
        newUration.setCreateDate(DateUtils.getDate());
        saveOrUpdateUration(newUration);// 保存还款的释放额度
        getEntityDao().flush();// 刷新缓存,保证后面更新授信额度时能取到占用额度
        // 根据新的占用授信额度释放额度
        releseUration(newUration, urationDTO.getUserId());
        applyService.updateDisApplyMoney(newUration);
    }
    /**
     * 授信占用审批通过，正式占用额度
     */
    public void passUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID操作，如果没有则根据业务ID和业务系统类型查找来操作
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
     * 授信占用打回，还原冻结额度
     */
    public void backUseCpRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID操作，如果没有则根据业务ID和业务系统类型查找来操作
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
    @Override
    public BaseDao<RatUration, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratCpUrationDao;
    }
    @Override
    @XxlJob("RatCpUrationService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_NOTIFYDAYS);// 授信到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询授信占用到期数据
        List<RatUration> urations = ratCpUrationDao.queryRatUList(endDate);
        if (!EmptyUtils.isEmptyList(urations)) {
            for (RatUration ration : urations) {
                RatUrationUseBeanVO rationVo = new RatUrationUseBeanVO();
                BeanUtils.copyProperties(ration, rationVo);
                notificationService.send(ration.getId(), ration.getCorpId(),
                        NotificationConstant.CMS.RAT_CPRATAPP_MATURE, rationVo);
                // logger.info("内部授信占用到期提醒，票据号：" + ration.getBillNo());
            }
        }
    }
    /**
     * 功能介绍 : 授信占用暂存后直接提交占用冻结额度
     */
    public void submitUseCpRation(RatUrationDTO urationDTO) {
        RatUration uration = null;
        // 如果业务表中有授信占用ID，可直接根据授信占用ID查找，如果没有则根据业务ID和业务系统类型查找
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {// 根据系统种类业务ID查找
            uration = get(urationDTO.getUrationId(), true);
        } else {// 根据业务ID和业务系统类型查找
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
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratCpUrationDao.queryBusBillByCorpId(corpId);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_CPRATOCCUPY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    /**
     * 功能介绍 : 授信占用后再打回，还原占用额度。<br>
     * <b>日期：</b>2021年6月11日<br>
     * 
     * @version 1.0
     * @author qijh
     */
    public void preBackUseRation(RatUrationDTO urationDTO) {
        // 如果业务表中有授信占用ID，可直接根据授信占用ID释放，如果没有则根据业务ID和业务系统类型查找来释放，先还原授信占用额度，再在审占用额度
        if (!EmptyUtils.isEmpty(urationDTO.getUrationId())) {
            RatUration uration = get(urationDTO.getUrationId(), true);
            applyService.updateDisApplyMoney(uration);
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 额度在审占用
            applyService.updateRationDisMoney(uration, true);
        } else {
            // 根据系统种类业务ID查找
            List<RatUration> uList = queryRatUrationById(urationDTO.getSysCode(), urationDTO.getBillId());
            if (!EmptyUtils.isEmptyList(uList)) {
                RatUration uration = uList.get(0);
                applyService.updateDisApplyMoney(uration);
                uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 额度在审占用
                applyService.updateRationDisMoney(uration, true);
            }
        }
    }
}
