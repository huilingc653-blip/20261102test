package com.bytter.credit.domain.service;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatCprationApplyDao;
import com.bytter.credit.domain.dao.RatUrationApplyDao;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.entity.RatUrationApplyDtl;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.dto.*;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCprationApplyService
 * @Description:
 * @date 2018年11月21日 上午9:20:12
 */
@Service("RatCprationApplyService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_CPRATION_APPLY })
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_CPRATION_APPLY )
public class RatCprationApplyService extends BaseService<RatUrationApply, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatCprationApplyDao ratCprationApplyDao;
    @Autowired
    private RatCprationDisService ratCprationDisService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired
    private RatCprationService ratCprationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private RatUrationApplyDtlService applyDtlService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @Autowired
    private RatUrationApplyDao ratUrationApplyDao;
    @Autowired
    private RatCprationLogService ratLogService;
    @Autowired
    private RatUrationService urationService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 初始化授信占用数据
     */
    public RatBkrationDisVO initRatCprationApplyInfo(RatUrationApply ratApply) {
        RatBkrationDisVO ratDis = ratCprationDisService.queryRatCpRationDisInfo(ratApply.getDisCode());
        setCpRationApplyLeftMoney(ratDis);
        return ratDis;
    }
    /**
     * 设置品种列表剩余占用额度
     */
    public void setCpRationApplyLeftMoney(RatBkrationDisVO ratDis) {
        Map<String, RatUrationApplyListDTO> dtlMap = new HashMap<String, RatUrationApplyListDTO>();
        List<RatUrationApplyListDTO> applySum = null;
        List<RatBkRationDtlVm> dtls = ratDis.getRationDtls();// 总分配明细
        BigDecimal money = BigDecimal.ZERO;
        if (!ratDis.getDistributeWay().equals(RationConstant.RAT_DISWAY_5)) {// 自动分配时已计算好剩余可占用额度
            applySum = ratCprationApplyDao.queryRationApplyPass(ratDis.getDisCode());// 同分配单号下已占用信息集合
            dtlMap = Maps.uniqueIndex(applySum, new Function<RatUrationApplyListDTO, String>() {
                @Override
                public String apply(RatUrationApplyListDTO input) {
                    return input.getItemCode();
                }
            });
            List<RatBkRationDtlVm> rationDtls = ratDis.getRationDtls();// 总分配明细
            for (RatBkRationDtlVm dtlDTO : rationDtls) {
                String itemCode = dtlDTO.getItemCode();// 授信品种
                RatUrationApplyListDTO applyDTO = dtlMap.get(itemCode);
                // 总额度=分配+调剂-被调剂
                dtlDTO.setMoney(
                        dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
                if (null != applyDTO) {
                    // 剩余可占用金额=总额度-占用
                    dtlDTO.setLeftMoney(dtlDTO.getMoney().subtract(applyDTO.getMoney()));
                } else {
                    dtlDTO.setLeftMoney(dtlDTO.getMoney());
                }
                money = dtlDTO.getMoney().add(money);
            }
        }
        ratDis.setRationDtls(dtls);
        ratDis.setMoney(money);
    }
    /**
     * 设置某占用品种的剩余占用额度
     */
    public void setCpRationApplyLeftMoney(RatBkrationDisVO ratDis, RatUrationApplyListDTO applyDTO) {
        Map<String, RatUrationApplyListDTO> dtlMap = new HashMap<String, RatUrationApplyListDTO>();
        List<RatUrationApplyListDTO> applySum = ratCprationApplyDao.queryRationApplyPass(ratDis.getDisCode());
        dtlMap = Maps.uniqueIndex(applySum, new Function<RatUrationApplyListDTO, String>() {
            @Override
            public String apply(RatUrationApplyListDTO input) {
                return input.getItemCode();
            }
        });
        List<RatBkRationDtlVm> rationDtls = ratDis.getRationDtls();// 总分配明细
        BigDecimal money = null;
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatUrationApplyListDTO disDTO = dtlMap.get(itemCode);
            if (null != disDTO) {
                money = dtlDTO.getMoney().subtract(disDTO.getMoney());
            } else {
                money = dtlDTO.getMoney();
            }
        }
        applyDTO.setLeftMoney(money);
    }
    /**
     * 查询审批通过的授信占用信息
     */
    @Transactional(readOnly = true)
    public List<RatUrationApplyListDTO> queryRatApplyInfo(String disCode) {
        // 查询已审批通过的占用信息
        List<RatUrationApplyListDTO> applyList = ratCprationApplyDao.queryPassRationApplyData(disCode);
        return applyList;
    }
    /**
     * 获取企业授信已分配授信品种对应的系统品种
     *
     * @param disCode-分配id
     * @return
     */
    @Transactional(readOnly = true)
    public List<RatSysItemDTO> getCpRationDisSysItem(String disCode) {
        return ratCprationApplyDao.getCpRationDisSysItem(disCode);
    }
    /**
     * 保存授信占用信息
     */
    public void saveRatCprationApply(RatUrationApplyDTO applyDTO, ICurrentUser user, String saveType) {
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        BtCurrencyDTO curDTO = currencyService.getObjectById(applyDTO.getCurCode());
        if (null != applyDtls && applyDtls.size() > 0) {
            for (RatUrationApplyListDTO listDTO : applyDtls) {
                listDTO.setExchange(curDTO.getCurExrate());// 币别汇率
                processRatUrationApply(listDTO, user, saveType);
            }
        }
    }
    /**
     * 录入保存授信占用明细
     */
    public void processRatUrationApply(RatUrationApplyListDTO applyDTO, ICurrentUser user, String saveType) {
        RatUrationApply apply = new RatUrationApply();
        String msg = null;// 操作消息
        Integer messageType = null;// 操作类型
        if (saveType.equals("0")) {
            applyDTO.setStatus(CommonConstant.STATUE_TEMP);
            msg = RationConstant.MSGA_INPUT + "";// 录入暂存
            messageType = RationConstant.MESSAGE_TYPE_TEMP;
        } else if (saveType.equals("1")) {
            applyDTO.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            msg = RationConstant.MSGA_INPUT_SUBMIT;// 录入提交
            messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
        }
        BeanUtils.copyProperties(applyDTO, apply);
        apply.setApplyCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_APPLYCODE,
                RationConstant.RAT_CPRATION_APPLY_CODE));// 授信占用申请编号
        apply.setIsCur(RationConstant.RAT_COMMON_N);// 不折合
        apply.setCreateBy(user.getId());
        apply.setCreateDate(DateUtils.getDate());
        if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
            apply.setItemCode(applyDTO.getItemCode());
        }
        if (StrUtil.isBlank(apply.getCurName()) && StrUtil.isNotBlank(apply.getCurCode())) {
            BtCurrencyDTO currency = currencyService.getObjectById(apply.getCurCode());
            if (ObjectUtil.isNotEmpty(currency)) {
                apply.setCurName(currency.getCurName());
            }
        }
        saveOrUpdateUrationApply(apply);
        // 记录到日志表
        ratLogService.saveRatProcessMessge(apply.getId(), apply.getMoney(), messageType, msg, user);
        if (RationConstant.STATUS_WAIT_CHECK.equals(apply.getStatus())) {
            RatUration uration = new RatUration();
            BeanUtils.copyProperties(apply, uration, "id");
            uration.setUrationCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_URATIONCODE,
                    RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            uration.setInitCurCode(apply.getCurCode());// 最初币别
            uration.setPreMoney(apply.getMoney());// 折算前金额
            uration.setConvertPreMoney(apply.getMoney());// 业务金额
            uration.setExchangeRate(apply.getExchange());// 汇率
            uration.setCreateBy(user.getId());
            uration.setCreateDate(DateUtils.getDate());
            uration.setBillId(apply.getId());// 占用申请ID
            uration.setBillNo(apply.getApplyCode());// 占用申请编号
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 授信占用的状态
            uration.setSyscode(ConfigConstant.RAT);// 授信
            uration.setBusinessName(RationConstant.BUSINESS_NAME_01);// 企业台账
            if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
                uration.setItemCode(applyDTO.getItemCode());
            }
            urationService.saveOrUpdateUration(uration);
            updateRationDisMoney(uration, true);// 冻结授信占用额度
            // todo:启动审批
             getStartHandler().handler(RatWorkFlowConstant.RAT_CPRATION_APPLY, apply.getId(), apply, apply.getCorpId(),
                    user.getId());
        }
    }
    /**
     * 更新后保存授信占用信息
     */
    public void updateRatCprationApply(RatUrationApplyListDTO applyDTO, ICurrentUser user, String saveType) {
        RatUrationApply apply = get(applyDTO.getId(), true);
        String msg = null;// 操作消息
        Integer messageType = null;// 操作类型
        BeanUtils.copyProperties(applyDTO, apply);
        if (saveType.equals("0")) {
            apply.setStatus(CommonConstant.STATUE_TEMP);
            msg = RationConstant.MSGA_MODIFY + "";// 修改暂存
            messageType = RationConstant.MESSAGE_TYPE_TEMP;
        } else if (saveType.equals("1")) {
            apply.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            msg = RationConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
        }
        apply.setUpdateBy(user.getId());
        apply.setUpdateDate(DateUtils.getDate());
        if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
            apply.setItemCode(applyDTO.getItemCode());
        }
        saveOrUpdateUrationApply(apply);// 将数据保存到授信占用申请表
        // 记录到日志表
        ratLogService.saveRatProcessMessge(apply.getId(), apply.getMoney(), messageType, msg, user);
        if (RationConstant.STATUS_WAIT_CHECK.equals(apply.getStatus())) {
            // 获取原占用授信信息
            RatUration uration = null;
            List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId(),
                    apply.getRatType());
            if (!EmptyUtils.isEmptyList(uList)) {
                uration = uList.get(0);
                // 删除原占用综合数据
                if (RationConstant.RAT_COMFLAG_1.equals(uration.getItemFlag())) {
                    applyDtlService.deleteRatUrationDtlByCode(uration.getId());
                }
                uration.setUpdateBy(user.getId());
                uration.setUpdateDate(DateUtils.getDate());
                if (uration.getStatus().equals(RationConstant.RAT_USE_STATUS_FREEZE)) {
                    uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);
                    updateRationDisMoney(uration, true);// 先释放前面冻结的
                }
            } else {
                uration = new RatUration();
                uration.setCreateBy(user.getId());
                uration.setCreateDate(DateUtils.getDate());
                uration.setUrationCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_URATIONCODE,
                        RationConstant.RAT_URATION_USE_CODE));// 授信占用台账编号
            }
            // RatUration uration = new RatUration();
            BeanUtils.copyProperties(apply, uration, "id");
            uration.setInitCurCode(apply.getCurCode());// 最初币别
            uration.setPreMoney(apply.getMoney());// 折算前金额
            uration.setConvertPreMoney(apply.getMoney());// 业务金额
            uration.setExchangeRate(apply.getExchange());// 汇率
            uration.setBillId(apply.getId());// 占用申请ID
            uration.setBillNo(apply.getApplyCode());// 占用申请编号
            uration.setStatus(RationConstant.RAT_USE_STATUS_FREEZE);// 授信占用的状态
            uration.setSyscode(ConfigConstant.RAT);// 授信
            uration.setBusinessName(RationConstant.BUSINESS_NAME_01);// 企业台账
            if (RationConstant.RAT_COMFLAG_0.equals(applyDTO.getItemFlag())) {
                uration.setItemCode(applyDTO.getItemCode());
            }
            urationService.saveOrUpdateUration(uration);
            updateRationDisMoney(uration, true);// 冻结授信占用额度
            // 启动审批
             getStartHandler().handler(RatWorkFlowConstant.RAT_CPRATION_APPLY, apply.getId(), apply, apply.getCorpId(),
                    user.getId());
            // 完成通知提醒
            notificationService.complete(NotificationConstant.CMS.RAT_CPRATION_ADD, apply.getId(), user.getId());
        }
    }
    /**
     * 金额非空处理
     */
    @SuppressWarnings("rawtypes")
    public void saveOrUpdateUrationApply(RatUrationApply apply) {
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
     * 保存占用明细
     */
    public void saveRationApplyDtl(String disCode, String applyCode, String applyType,
            List<RatUrationApplyDtlDTO> applyDtls) {
        Map<String, RatUrationApplyDtlDTO> applyMap = Maps.uniqueIndex(applyDtls,
                new Function<RatUrationApplyDtlDTO, String>() {
                    @Override
                    public String apply(RatUrationApplyDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkrationDisDetail> disDtlDTOs = disDtlService.getRationDisDtlByDisCode(disCode,
                RationConstant.RAT_COMFLAG_1, null);
        for (RatBkrationDisDetail dtlDTO : disDtlDTOs) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            Integer itemOrder = dtlDTO.getItemOrder();// 品种顺序
            RatUrationApplyDtlDTO applyDTO = applyMap.get(itemCode);
            BigDecimal maxMoney = BigDecimal.ZERO;
            if (null != applyDTO) {
                maxMoney = applyDTO.getMaxMoney();// 综合占用额度
            }
            RatUrationApplyDtl applyDtl = new RatUrationApplyDtl();
            applyDtl.setApplyType(applyType);// 占用类型
            applyDtl.setApplyCode(applyCode);
            applyDtl.setItemFlag(dtlDTO.getItemFlag());
            applyDtl.setItemOrder(itemOrder);
            applyDtl.setItemCode(itemCode);
            applyDtl.setMaxMoney(maxMoney);
            applyDtl.setApplyMoney(BigDecimal.ZERO);
            applyDtlService.saveEntity(applyDtl);
        }
    }
    public RatUrationApplyListDTO editRatUrationInfo(RatUrationApply ratApply) {
        RatUrationApplyListDTO applyDTO = ratCprationApplyDao.queryRatApplyrationById(ratApply.getId());// 根据id获取到占用对象
        RatBkrationDisVO ratDis = ratCprationDisService.queryRatCpRationDisInfo(ratApply.getDisCode());
        setCpRationApplyLeftMoney(ratDis, applyDTO);// 设置某占用品种的剩余占用额度
        return applyDTO;
    }
    /**
     * 查询待审批数据列表
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
    public DataTablesOutput getDataApplyApproveTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_CPRATION_APPLY,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "ru.id", true);
        }
        DataTablesOutput dp = ratUrationApplyDao.getDataApplyApproveTables(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 授信占用初始化数据
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        ICurrentUser user = workFlowResult.getUser();
        boolean result = workFlowResult.isPass();// 是否通过
        Integer messageType = null;// 操作类型
        String msg = null;// 操作消息
        RatUrationApply apply = this.get(businessKey,true);
        getEntityDao().flush();
        if (result) {// 审批通过
            updateUrationPass(businessKey, user);
            msg = RationConstant.MSGA_CHECKOK + "";
            messageType = RationConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            // 审批通过时,发出通知告知已审批通过
            notificationService.send(apply.getId(), apply.getCorpId(), NotificationConstant.CMS.CPRATION_APPLY_PASS,
                    apply);
        } else {// 审批打回
            updateUrationBack(businessKey, user);
            msg = RationConstant.MSGA_CHECKBACK;
            messageType = RationConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            // 审批打回时,发出通知告知被审批打回
            notificationService.send(apply.getId(), apply.getCorpId(), NotificationConstant.CMS.CPRATION_APPLY_BACK,
                    apply);
        }
        super.updateEntity(apply);
        // 记录到日志表
        ratLogService.saveRatProcessMessge(apply.getId(), apply.getMoney(), messageType, msg, user);
    }
    /**
     * 更新内部授信申请表
     */
    public void updateUrationPass(String businessKey, ICurrentUser user) {
        RatUrationApply apply = this.get(businessKey,true);
        apply.setStatus(CommonConstant.STATUE_CHECK);
        apply.setUpdateBy(user.getId());
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId(),
                apply.getRatType());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            passUration(uration, user.getId());
        }
    }
    /**
     * 更新授信占用表
     */
    public void passUration(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_USE);// 正式占用
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        // 更新分配表中占用额度super_process_money,冻结金额移动到占用金额
        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            // 更新分配表中占用额度，冻结金额减少
            updateRationDisMoney(uration, true);
        }
    }
    /**
     * 审批打回，更新占用申请(-1)和占用台账状态(3释放占用)
     */
    public void updateUrationBack(String urationId, ICurrentUser user) {
        RatUrationApply apply = get(urationId, true);
        apply.setStatus(CommonConstant.STATUE_BACK);
        apply.setUpdateBy(user.getId());
        apply.setUpdateDate(DateUtils.getDate());
        updateEntity(apply);
        List<RatUration> uList = urationService.queryRatUrationById(ConfigConstant.RAT, apply.getId());
        if (!EmptyUtils.isEmptyList(uList)) {
            RatUration uration = uList.get(0);
            backUration(uration, user.getId());
        }
    }
    /**
     * 内部授信占用打回（还原冻结额度）
     */
    public void backUration(RatUration uration, String userId) {
        uration.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);// 额度释放
        uration.setUpdateBy(userId);
        uration.setUpdateDate(DateUtils.getDate());
        urationService.updateEntity(uration);
        // 还原分配表中冻结金额,如果是总额共享的情况下还需要更新其他单位的虚拟占用额度
        if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
            // 更新分配表中占用额度，冻结金额减少
            updateRationDisMoney(uration, true);
        }
    }
    /**
     * 更新授信分配表
     */
    public void updateRationDisMoney(RatUration uration, boolean freezeFlag) {
        getEntityDao().flush();
        List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(),
                uration.getItemFlag(), uration.getItemCode());
        RatBkrationDisDetail disDetail = disDetails.get(0);
        if (RationConstant.RAT_USE_STATUS_FREEZE.equals(uration.getStatus())) {// 占用待审批
            disDetail.setFreezeMoney(disDetail.getFreezeMoney().add(uration.getMoney()));// 冻结额度增加
        } else if (RationConstant.RAT_USE_STATUS_USE.equals(uration.getStatus())) {// 占用审批通过
            disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().add(uration.getMoney()));// 占用额度增加
            if (freezeFlag) {// 需要更新冻结额度
                if (uration.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                    disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));// 冻结额度减少
                }
            }
        } else if (RationConstant.RAT_USE_STATUS_RELEASE.equals(uration.getStatus())) {// 打回
            disDetail.setFreezeMoney(disDetail.getFreezeMoney().subtract(uration.getMoney()));// 冻结额度减少
        }
        disDtlService.merge(disDetail);
    }
    /**
     * 授信占用后执行释放更新分配表
     */
    public void updateDisApplyMoney(RatUration uration) {
        getEntityDao().flush();
        List<RatBkrationDisDetail> disDetails = disDtlService.getRationDisDtlByDisCode(uration.getDisCode(),
                uration.getItemFlag(), uration.getItemCode());
        if (RationConstant.RAT_COMFLAG_0.equals(uration.getItemFlag())) {
            RatBkrationDisDetail disDetail = disDetails.get(0);
            disDetail.setSuperProcessMoney(disDetail.getSuperProcessMoney().subtract(uration.getMoney()));// 占用额度还原
            disDtlService.merge(disDetail);
        }
    }
    @Override
    public BaseDao<RatUrationApply, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratCprationApplyDao;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 内部授信分配在途数据
            int dataSize = ratCprationApplyDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_CPRATIONAPPLY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
