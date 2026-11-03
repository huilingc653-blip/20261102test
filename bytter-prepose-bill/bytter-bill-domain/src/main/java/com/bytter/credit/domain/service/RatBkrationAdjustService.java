package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationAdjustDao;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseDtlVO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 授信调整服务层
 *
 * @author luxy Title: RatBkrationAdjustService
 */
@Service("ratBkrationAdjustService")
@Transactional
@WorkFlow(value = {RatWorkFlowConstant.RAT_BKRATION_ADJUST})
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_BKRATION_ADJUST)
public class RatBkrationAdjustService extends BaseService<RatBkrationAdjust, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatBkrationAdjustDao ratBkrationAdjustDao;
    @Autowired
    private RatBkrationAdjustDtlService adjustDtlService;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference
    private ICorpService corpService;
    @Autowired
    private RatBkrationAddService bkrationAddService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratBkrationDisService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatBkrationDetailService detailService;
    @Autowired
    private RatUrationService urationService;
    @Override
    public BaseDao<RatBkrationAdjust, String> getEntityDao() {
        return ratBkrationAdjustDao;
    }
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 保存授信调整数据
     *
     * @param ratBkrationAdjustVO 授信调整VO
     * @param user                当前用户
     */
    public void saveData(RatBkrationAdjustVO ratBkrationAdjustVO, ICurrentUser user) {
        RatBkrationAdjust ratBkrationAdjust = new RatBkrationAdjust();
        if (StringUtils.isEmpty(ratBkrationAdjustVO.getId())) {// 新增数据
            BeanUtils.copyNotNullProperties(ratBkrationAdjustVO, ratBkrationAdjust);
            ratBkrationAdjust.setAdjustCode(bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_ADDCODE, RationConstant.RAT_BKRATION_ADJUST_CODE));// 授信调整编号
            ratBkrationAdjust.setCreateBy(user.getId());
            ratBkrationAdjust.setCreateDate(DateUtils.getDate());
        } else {
            ratBkrationAdjust = this.get(ratBkrationAdjustVO.getId(), true);
            ratBkrationAdjust.setStatus(ratBkrationAdjustVO.getStatus());
            ratBkrationAdjust.setRemark(ratBkrationAdjustVO.getRemark());
            ratBkrationAdjust.setAttachmentId(ratBkrationAdjustVO.getAttachmentId());
            ratBkrationAdjust.setUpdateBy(user.getId());
            ratBkrationAdjust.setUpdateDate(DateUtils.getDate());
            adjustDtlService.deleteAdjustDtl(ratBkrationAdjustVO.getId());
        }
        this.saveOrUpdate(ratBkrationAdjust);
        getEntityDao().flush();
        ratBkrationAdjustVO.setId(ratBkrationAdjust.getId());
        saveAdjustDtl(ratBkrationAdjustVO);// 保存调整明细

        if (ratBkrationAdjust.getStatus().equals(RationConstant.STATUS_WAIT_CHECK)) {// 挂接审批
//            updateRatBkMsg(ratBkrationAdjustVO);
            getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_ADJUST, ratBkrationAdjust.getId(),
                    ratBkrationAdjust, ratBkrationAdjust.getCorpId(), user.getId());
        }
    }
    /**
     * 保存授信调整明细数据
     *
     * @param ratBkrationAdjustVO 授信调整VO
     */
    public void saveAdjustDtl(RatBkrationAdjustVO ratBkrationAdjustVO) {
        List<RatBkrationAdjustDtlVO> adjustDtlVOList = ratBkrationAdjustVO.getAdjustDtls();
        List<RatBkrationAdjustDetail> adjustDtlList = BeanUtils.copyListProperties(adjustDtlVOList, RatBkrationAdjustDetail.class);
        adjustDtlList.forEach(item -> item.setAdjustCode(ratBkrationAdjustVO.getId()));
        adjustDtlService.batchSave(adjustDtlList);
    }
    /**
     * 查询授信调整
     *
     * @param input 查询条件
     * @param user  当前用户
     * @return 授信调整数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryAdjustList(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_RATION_ADJUST_QUERY, user.getId());
        String corpAuthSql = SqlUtil.getInSql(corpAuth, "ra.corp_Id", true);
        DataTablesOutput<RatBkrationAdjustVO> output = new DataTablesOutput<>();
        List<RatBkrationAdjustVO> bkrationAdjustVOS = new ArrayList<>();
        String businessType = input.getPara("businessType");
        if (StringUtils.isNotEmpty(businessType)) {
            if (businessType.equals(RationConstant.RAT_INPUT_ADJUST)) {
                output = ratBkrationAdjustDao.getRatInputAdjustList(input, corpAuthSql);
            } else if (businessType.equals(RationConstant.RAT_DIS_ADJUST)) {
                output = ratBkrationAdjustDao.getRatDisAdjustList(input, corpAuthSql);
            }
        }
        setRatBkAdjustDtl(output);// 设置调整明细
        return output;
    }
    /**
     * 设置调整明细
     *
     * @param output 授信调整数据
     */
    public void setRatBkAdjustDtl(DataTablesOutput output) {
        List<RatBkrationAdjustVO> bkrationAdjustVOS = output.getData();
        if (null != bkrationAdjustVOS && bkrationAdjustVOS.size() > 0) {
            for (RatBkrationAdjustVO bkrationAdjustVO : bkrationAdjustVOS) {
                List<RatBkrationAdjustDtlVO> dtlVOList = adjustDtlService.queryAdjustDtlBy(bkrationAdjustVO.getId());
                bkrationAdjustVO.setItemSize(dtlVOList.size());
                bkrationAdjustVO.setAdjustDtls(dtlVOList);
            }
        }
    }
    /**
     * 根据条件查询授信调整主表数据
     *
     * @param input 查询条件
     * @return 授信调整主表数据
     */
    public DataTablesOutput queryAdjustDataByParams(DataTablesInput input) {
        DataTablesOutput inputData = null;
        DataTablesOutput disData = null;
        if (StringUtils.isNotEmpty(input.getPara("businessType"))) {
            if (input.getPara("businessType").equals("0")) {
                inputData = ratBkrationAdjustDao.getRatInputAdjustList(input);
            } else if (input.getPara("businessType").equals("1")) {
                disData = ratBkrationAdjustDao.getRatDisAdjustList(input);
            }
        } else {
            inputData = ratBkrationAdjustDao.getRatInputAdjustList(input);
            disData = ratBkrationAdjustDao.getRatDisAdjustList(input);
        }
        List<RatBkrationAdjustVO> bkrationAdjustVOS = new ArrayList<>();
        if (null != inputData && inputData.getData().size() > 0) {
            bkrationAdjustVOS = inputData.getData();
        }
        if (null != disData && disData.getData().size() > 0) {
            bkrationAdjustVOS.addAll(disData.getData());
        }
        DataTablesOutput<RatBkrationAdjustVO> output = new DataTablesOutput<>();
        output.setData(bkrationAdjustVOS);
        return output;
    }

    /**
     * 设置授信占用明细
     *
     * @param pageVO
     * @param ratCode
     */
    public void setRatBkrationApplyDtl(RatBkrationCpAddListDTO pageVO, String ratCode) {
        List<RatBkrationCpAddDtlDTO> compDtls = pageVO.getCompRationDtls();
        UrationParamDTO paramDTO = new UrationParamDTO();
        paramDTO.setCorpId(pageVO.getCorpId());
        paramDTO.setRatCode(ratCode);
        paramDTO.setCurCode(pageVO.getCurCode());
        paramDTO.setBankCode(pageVO.getBankCode());
        paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        paramDTO.setStatus(RationConstant.STATUS_CHECK + "");
        List<RatUrationUseVO> useVOS = urationService.queryRationUseDtl(paramDTO);
        if (CollectionUtils.isNotEmpty(useVOS)) {
            List<RatUrationUseDtlVO> ratUDtl = useVOS.get(0).getRationDlts().get(0).getRationDlts();
            for (RatBkrationCpAddDtlDTO ratDtl : compDtls) {// 设置最高可调整额度
                RatBkrationMoneyBean ratBean = ratBkrationDisService.getBankRationItemMoney(ratCode, pageVO.getRatCorpId(), RationConstant.RAT_COMFLAG_1, ratDtl.getItemCode());
                ratDtl.setAllAddMoney(ratBean.getUnUseMaxMoney());
            }
            Map<String, RatUrationUseDtlVO> ratUMap = new HashMap<String, RatUrationUseDtlVO>();
            if (null != ratUDtl && ratUDtl.size() > 0) {
                for (RatUrationUseDtlVO urationUseDtlVO : ratUDtl) {
                    ratUMap.put(urationUseDtlVO.getItemCode(), urationUseDtlVO);
                }
                if (ratUMap.size() > 0) {
                    for (RatBkrationCpAddDtlDTO ratDtl : compDtls) {
                        BigDecimal superProcessMoney = ratUMap.get(ratDtl.getItemCode()).getUseMoney();// 占用额度
                        BigDecimal approveMoney = ratUMap.get(ratDtl.getItemCode()).getApproveMoney();// 在审占用额度
                        ratDtl.setSuperProcessMoney(superProcessMoney.add(approveMoney));// 占用+在审
                    }
                }
            }
        }
    }

    /**
     * 查询授信调整待审批数据
     *
     * @param input 查询条件
     * @return 授信调整待审批数据
     */
    public DataTablesOutput queryApproveData(DataTablesInput input) {
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            input.addFilter(MatchTypeEnum.EQ, "status", 11);
        }
        DataTablesOutput dataTablesOutput = this.queryAdjustDataByParams(input);
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION_ADJUST,
                input);
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 将授信调整状态改为删除
     *
     * @param id   授信调整id
     * @param user 当前用户
     */
    public void deleteAdjust(String id, ICurrentUser user) {
        ratBkrationAdjustDao.delRatAdjust(id);// 删除主表
        adjustDtlService.deleteAdjustDtl(id);// 删除明细表
    }
    /**
     * 审批完成
     *
     * @param workFlowResult 审批完成的结果
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        RatBkrationAdjust adjust = this.get(businessKey,true);
        if (workFlowResult.isPass()) {
            // 审批通过，更新授信录入明细表或授信分配明细表最高额度
            adjust.setStatus(RationConstant.STATUS_CHECK);
            // 业务类型：0-银行录入，1-银行分配
            String businessType = adjust.getBusinessType();
            if (RationConstant.RAT_INPUT_ADJUST.equals(businessType)) {
                RatBkration rb = ratBkrationService.get(adjust.getBusinessId(), true);
                // 授信录入最大金额调整明细
                List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryInputAdjustDtl(adjust.getId());
                // 每个授信品种对应调整的最大额度
                Map<String, BigDecimal> maxMoneyMap = adjustDtlList
                        .stream()
                        .collect(Collectors.groupingBy(RatBkrationAdjustDtlVO::getItemCode))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0).getMoney()));
                // 授信模式：0-集团授信，1-自主授信
                String ratMode = rb.getRatMode();
                if (RationConstant.RATMODE_1.equals(ratMode)) {
                    // 1、授信录入且自主授信：修改rat_bkration_detail表和rat_bkration_dis_detail表的max_money
                    this.updateDetailMaxMoney(rb.getId(), maxMoneyMap);
                    // 自主授信分配表有且仅有一条单位分配数据
                    RatBkrationDis ratBkrationDis = ratBkrationDisService.getRatBkDisByRatCode(rb.getId(), rb.getCorpId(), null).get(0);
                    this.updateDisDetailMaxMoney(ratBkrationDis.getId(), maxMoneyMap);
                } else {
                    // 2、授信录入且集团授信，修改rat_bkration_detail表
                    this.updateDetailMaxMoney(rb.getId(), maxMoneyMap);
                }
            } else if (RationConstant.RAT_DIS_ADJUST.equals(businessType)) {
                // 3、授信分配且集团授信。（授信分配不存在自主授信模式）
                RatBkrationDis rbd = ratBkrationDisService.get(adjust.getBusinessId(), true);
                RatBkration rb = ratBkrationService.get(rbd.getRatCode(), true);
                // 授信模式：0-集团授信，1-自主授信
                String ratMode = rb.getRatMode();
                // 授信分配最大金额调整明细
                List<RatBkrationAdjustDtlVO> adjustDtlList = adjustDtlService.queryDisAdjustDtl(adjust.getId());
                // 每个授信品种对应调整的最大额度
                Map<String, BigDecimal> maxMoneyMap = adjustDtlList
                        .stream()
                        .collect(Collectors.groupingBy(RatBkrationAdjustDtlVO::getItemCode))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0).getMoney()));
                if (RationConstant.RATMODE_0.equals(ratMode)) {
                    this.updateDisDetailMaxMoney(rbd.getId(), maxMoneyMap);
                }
            }
        } else {
            // 审批打回
            adjust.setStatus(RationConstant.STATUS_BACK);
        }
        this.saveOrUpdate(adjust);
    }

    /**
     * 更新授信录入明细表最大额度
     */
    private void updateDetailMaxMoney(String ratCode, Map<String, BigDecimal> maxMoneyMap) {
        List<RatBkrationDetail> dtlList = detailService.getBkrationDetailByRatCode(ratCode);
        dtlList = dtlList.stream().filter(item -> RationConstant.RAT_COMFLAG_1.equals(item.getItemFlag())).collect(Collectors.toList());
        dtlList.forEach(item -> {
            if (maxMoneyMap.containsKey(item.getItemCode())) {
                item.setMaxMoney(maxMoneyMap.get(item.getItemCode()));
                // V24010214【广和通】-授信最高额问题，最高额度调整提交时，需将追加的最高额度置零
                item.setSuperAddMaxMoney(BigDecimal.ZERO);
            }
        });
        detailService.batchSaveOrUpdate(dtlList);
    }

    /**
     * 更新授信分配明细表最大额度
     */
    private void updateDisDetailMaxMoney(String disCode, Map<String, BigDecimal> maxMoneyMap) {
        List<RatBkrationDisDetail> disDtlList = disDetailService.getRationDisDtlByDisCode(disCode, RationConstant.RAT_COMFLAG_1, null);//分配明细
        disDtlList.forEach(item -> {
            if (maxMoneyMap.containsKey(item.getItemCode())) {
                item.setMaxMoney(maxMoneyMap.get(item.getItemCode()));
            }
        });
        disDetailService.batchSaveOrUpdate(disDtlList);
    }

    /**
     * 启动审批
     *
     * @param workFlowResult 启动完成的结果
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 更新到授信录入表或分配表
     *
     * @param ratBkrationAdjustVO 授信调整VO
     */
    public void updateRatBkMsg(RatBkrationAdjustVO ratBkrationAdjustVO) {
        RatBkRationAddVm bkrationAddListDTO = new RatBkRationAddVm();
        List<RatBkRationAddDtlVm> bkrationAddDtlDTOList = new ArrayList<>();
        List<RatBkrationAdjustDtlVO> adjustDtlVOList = adjustDtlService.queryAdjustDtlBy(ratBkrationAdjustVO.getId());
        RatBkrationPageVO ratBkration = null;
        String shareFlag = "";// 分配方式为总额共享
        RatBkrationDis shareDis = null;
        if (ratBkrationAdjustVO.getBusinessType().equals(RationConstant.RAT_DIS_ADJUST)) {// 分配数据调整
            RatBkrationDis bkrationDis = ratBkrationDisService.get(ratBkrationAdjustVO.getBusinessId());
            ratBkration = ratBkrationService.getBkRationBeanById(bkrationDis.getRatCode());
            ratBkration.setRatCode(bkrationDis.getRatCode());
            ratBkration.setCorpId(bkrationDis.getCorpId());
        } else {
            ratBkration = ratBkrationService.getBkRationBeanById(ratBkrationAdjustVO.getBusinessId());
            List<RatBkrationDis> disHis = ratBkrationDisService.getRatBkDisWayByRatCode(ratBkration.getId(), "",
                    RationConstant.RAT_DISWAY_2);
            if (disHis.size() == 0) {// 不存在总额共享的数据
                shareFlag = RationConstant.RAT_COMMON_N;
            } else {
                shareDis = disHis.get(0);// 取其中一条
                shareFlag = RationConstant.RAT_COMMON_Y;
            }
        }
        if (ratBkrationAdjustVO.getBusinessType().equals(RationConstant.RAT_INPUT_ADJUST)) {//
            bkrationAddListDTO.setFlag(RationConstant.RAT_ADD_FLAG_BANK);// 银行追加
        } else if (ratBkrationAdjustVO.getBusinessType().equals(RationConstant.RAT_DIS_ADJUST)) {
            bkrationAddListDTO.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);// 集团追加
        }
        for (RatBkrationAdjustDtlVO dtlVO : adjustDtlVOList) {
            RatBkRationAddDtlVm addDtlDTO = new RatBkRationAddDtlVm();
            addDtlDTO.setItemCode(dtlVO.getItemCode());
            addDtlDTO.setItemFlag(dtlVO.getItemFlag());
            addDtlDTO.setMoney(BigDecimal.ZERO);
            addDtlDTO.setMaxMoney(dtlVO.getAdjustMoney());
            addDtlDTO.setOpenMoney(BigDecimal.ZERO);
            addDtlDTO.setFlag(bkrationAddListDTO.getFlag());// 追加标识 0——银行追加 1——集团追加
            bkrationAddDtlDTOList.add(addDtlDTO);
        }
        bkrationAddListDTO.setRatCode(ratBkration.getId());
        bkrationAddListDTO.setCorpId(ratBkration.getCorpId());
        bkrationAddListDTO.setStatus(ratBkrationAdjustVO.getStatus());
        bkrationAddListDTO.setRationDtls(bkrationAddDtlDTOList);
        if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_WAIT_CHECK)) {// 冻结额度
            bkrationAddService.freezeGroupRationAdd(bkrationAddListDTO);
        } else if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_CHECK)) {// 审批通过
            bkrationAddService.updateRationInfo(bkrationAddListDTO);// 更新到授信录入表或授信分配表
        } else if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_BACK)) {// 审批打回
            bkrationAddService.updateRatBkRationDis(bkrationAddListDTO);
        }
        if (StringUtils.isNotEmpty(shareFlag) && shareFlag.equals(RationConstant.RAT_COMMON_Y)) {// 如果是总额共享分配数据，更新到授信分配表
            bkrationAddListDTO.setFlag(RationConstant.RAT_ADD_FLAG_GROUP);
            bkrationAddListDTO.setCorpId(shareDis.getCorpId());
            if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_WAIT_CHECK)) {// 冻结额度
                bkrationAddService.freezeGroupRationAdd(bkrationAddListDTO);
            } else if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_CHECK)) {// 审批通过
                bkrationAddService.updateRationInfo(bkrationAddListDTO);// 更新到授信录入表或授信分配表
            } else if (ratBkrationAdjustVO.getStatus().equals(RationConstant.STATUS_BACK)) {// 审批打回
                bkrationAddService.updateRatBkRationDis(bkrationAddListDTO);
            }
        }
    }
    /**
     * 查询满足条件的授信录入可调整数据
     *
     * @param input
     * @param resource
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatBkInputCanAdjust(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rb.corp_Id", true);
        DataTablesOutput output = ratBkrationAdjustDao.ratBkInputListData(input, corpAuthSql);
        return bkrationService.setOutputPageData(output);
    }
    /**
     * 查询满足条件的授信分配可调整数据
     *
     * @param input    查询条件
     * @param resource 资源权限
     * @param userId   用户id
     * @return 授信分配可调整数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatBkDisCanAdjust(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rd.corp_Id", true);
        DataTablesOutput<RatBkrationDisVO> output = ratBkrationAdjustDao.ratBkDisListData(input, corpAuthSql);
        List<RatBkrationDisVO> data = output.getData();
        if (null == data || data.isEmpty()) {
            return output;
        }
        List<String> disCodes = new ArrayList<String>();
        Date nowDate = DateUtils.getDate();//当前日期
        for (RatBkrationDisVO disVo : data) {
            if (DateUtils.dayDiffs(disVo.getEndDate(), nowDate) < 0) {
                //结束日期小于当前日期标识过期
                disVo.setOverdueSign(RationConstant.RAT_COMMON_Y);
            }
            disCodes.add(disVo.getId());
        }
        List<RatBkRationDtlVm> dtlDTOs = disDetailService.getBkrationDtlByRatCodes(disCodes);
        //将授信明细中最高额度进行赋值
        setMaxMoneyData(dtlDTOs);
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(dtlDTOs)) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.computeIfAbsent(dtlDTO.getRatCode(), k -> new ArrayList<>());
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationDisVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationDtlVm> ratList = ratMap.get(ratCode);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            // 综合品种条数
            Integer compItemSize = Integer.valueOf(0);
            boolean compFlag = false;
            if (EmptyUtils.isEmptyList(ratList)) {
                continue;
            }
            for (RatBkRationDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                } else {
                    if (!compFlag) {// 没有设置过，则设置第一个品种的综合标识
                        dtlDTO.setFirstComp("1");
                        compFlag = true;
                    }
                    compItemSize++;
                }
                // 分配显示额度 = 分配额度+被调剂额度-调剂额度
                dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
                dtlDTO.setMaxMoney(dtlDTO.getMaxMoney().add(dtlDTO.getByReliefMaxMoney()).subtract(dtlDTO.getReliefMaxMoney()));
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setCompItemSize(compItemSize);// 综合品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }

    /**
     * 最高额度赋值
     *
     * @param dtlDTOs
     */
    public void setMaxMoneyData(List<RatBkRationDtlVm> dtlDTOs) {
        if (EmptyUtils.isEmptyList(dtlDTOs)) {
            return;
        }
        for (RatBkRationDtlVm ratDtlDO : dtlDTOs) {
            if (EmptyUtils.isEmpty(ratDtlDO.getMaxMoney()) || ratDtlDO.getMaxMoney().compareTo(BigDecimal.ZERO) == 0) {
                ratDtlDO.setMaxMoney(ratDtlDO.getMoney());
            }
        }
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 银行授信调整在途数据
            int dataSize1 = ratBkrationAdjustDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_ADJUST_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
