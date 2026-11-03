package com.bytter.credit.domain.service;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDisDetailDao;
import com.bytter.credit.domain.dao.RatBkrationReliefDao;
import com.bytter.credit.domain.dao.RatBkrationReliefDetailDao;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatBkrationRelief;
import com.bytter.credit.domain.entity.RatBkrationReliefDetail;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationReliefDtlPageVO;
import com.bytter.credit.domain.vo.RatBkrationReliefPageVO;
import com.bytter.credit.domain.vo.RatItemDisVO;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 授信额度调剂表
 * @author onlineGenerator
 * @date 2017-07-31 16:10:53
 * @version 1.0
 *
 */
@Service("ratBkrationReliefService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_BKRATION_RELIEF })
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_BKRATION_RELIEF )
public class RatBkrationReliefService extends BaseService<RatBkrationRelief, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatBkrationReliefDao ratBkrationReliefDao;
    @Autowired
    private RatBkrationReliefDetailDao refDtlDao;
    @Autowired
    private RatBkrationReliefDetailService refDetailService;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private RatBkrationDetailService rationDtlService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @Autowired
    private RatBkrationDisDetailDao disDtlDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkrationRelief entity) {
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
    public void save(RatBkrationRelief entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkrationRelief entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkrationRelief entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationRelief, String> getEntityDao() {
        return ratBkrationReliefDao;
    }
    /**
     * 功能介绍 : 查询调剂列表数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatBkRationReliefByRatCode(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rb.corp_Id", true);
        DataTablesOutput output = ratBkrationReliefDao.queryRatBkRationReliefByRatCode(input, corpAuthSql);
        return output;
    }
    /**
     * 功能介绍 : 查询调剂审批列表数据。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION_RELIEF,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rb.id", true);
        }
        DataTablesOutput dp = ratBkrationReliefDao.queryRationReliefApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 初始化调剂信息。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationReliefPageVO initRationReliefPage(String ratCode) {
        RatBkrationReliefPageVO refVO = new RatBkrationReliefPageVO();
        RatBkrationPageVO pageVO = bkrationService.getRatBkrationInfo(ratCode);// 授信明细
        BeanUtils.copyProperties(pageVO, refVO);
        return refVO;
    }
    /**
     * 功能介绍 : 获取授信调剂品种的剩余额度。<br>
     * <b>日期：</b>2017年10月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    private RatBkrationCpAddDtlDTO getReliefRationMoney(RatBkrationReliefPageVO refVO) {
        String ratCode = refVO.getRatCode();
        String reliefItemFlag = refVO.getReliefItemFlag();
        String reliefItemCode = refVO.getReliefItemCode();
        if (RationConstant.RAT_COMFLAG_1.equals(reliefItemFlag)) {
            // 取综合授信品种
            List<RatBkrationDisDetail> dtlDTOs = disDtlService.getRationDisDtlByDisCode(refVO.getReliefCode(),
                    reliefItemFlag, null);
            if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                for (RatBkrationDisDetail disDetail : dtlDTOs) {
                    if (RationConstant.RAT_COMP_SIGN_1.equals(disDetail.getCompSign())) {
                        reliefItemCode = disDetail.getItemCode();
                        break;
                    }
                }
            }
        }
        RatBkrationCpAddDtlDTO disMoneyDTO = disService.getRationDisItemMoney(ratCode, refVO.getReliefCorp(),
                reliefItemFlag, reliefItemCode);
        return disMoneyDTO;
    }
    /**
     * 功能介绍 : 修改调剂信息。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public Map<String, Object> editRationReliefPage(String refId) {
        Map<String, Object> result = new HashMap<String, Object>();
        RatBkrationReliefPageVO refVO = getRationReliefPage(refId);
        String ratCode = refVO.getRatCode();
        RatBkrationPageVO pageVO = bkrationService.getRatBkrationInfo(ratCode);// 授信明细
        // 调剂方信息
        List<RatItemDisVO> reliefItemFlags = getCorpItemFlags(ratCode, refVO.getReliefCorp());
        List<RatItemDisVO> reliefItems = getItemCustomDTOs(ratCode, refVO.getReliefCorp(), refVO.getReliefItemFlag());
        RatBkrationCpAddDtlDTO disMoneyDTO = getReliefRationMoney(refVO);// 获取调剂品种的剩余额度
        // 被调剂方信息
        List<RatItemDisVO> byReliefItemFlags = getCorpItemFlags(ratCode, refVO.getByReliefCorp());
        List<RatItemDisVO> byReliefItems = getItemCustomDTOs(ratCode, refVO.getByReliefCorp(),
                refVO.getByReliefItemFlag());
        BigDecimal assurepercent = BigDecimal.ZERO;// 保证金
        BigDecimal interestrate = BigDecimal.ZERO;// 利率
        if (RationConstant.RAT_COMFLAG_1.equals(refVO.getReliefItemFlag())
                || RationConstant.RAT_COMFLAG_1.equals(refVO.getByReliefItemFlag())) {// 如果调剂方或被调剂方为综合品种，查找综合品种的调剂额度
            List<RatBkrationReliefDetail> refDtls = refDetailService.getRationReliefDtlByRefCode(refId);
            List<RatBkrationReliefDetail> refList = new ArrayList<RatBkrationReliefDetail>();
            List<RatBkrationReliefDetail> byRefList = new ArrayList<RatBkrationReliefDetail>();
            for (RatBkrationReliefDetail detail : refDtls) {
                if (RationConstant.RAT_REF_TYPE_0.equals(detail.getRefType())) {// 调剂方
                    refList.add(detail);
                } else {
                    byRefList.add(detail);
                }
            }
            Map<String, RatBkrationReliefDetail> refMap = list2Map(refList);
            Map<String, RatBkrationReliefDetail> byRefMap = list2Map(byRefList);
            if (null != reliefItems && !reliefItems.isEmpty()) {
                for (RatItemDisVO disVO : reliefItems) {
                    String itemCode = disVO.getItemCode();
                    RatBkrationReliefDetail detail = refMap.get(itemCode);
                    if (null != detail) {
                        disVO.setMoney(detail.getReliefMaxMoney());// 调剂最高额度
                    } else {
                        disVO.setMoney(BigDecimal.ZERO);// 调剂最高额度
                    }
                }
            }
            // 被调剂方
            if (RationConstant.RAT_COMFLAG_1.equals(refVO.getByReliefItemFlag()) && null != byReliefItems
                    && !byReliefItems.isEmpty()) {
                for (RatItemDisVO disVO : byReliefItems) {
                    String itemCode = disVO.getItemCode();
                    RatBkrationReliefDetail detail = byRefMap.get(itemCode);
                    if (null != detail) {
                        disVO.setMoney(detail.getReliefMaxMoney());// 调剂最高额度
                    } else {
                        disVO.setMoney(BigDecimal.ZERO);// 调剂最高额度
                    }
                    if (RationConstant.RAT_COMFLAG_1.equals(disVO.getCompSign())) {
                        assurepercent = disVO.getAssurepercent();// 保证金
                        interestrate = disVO.getInterestrate();// 比例
                    }
                }
            }
        }
        RatItemDisVO byItemAssure = null;
        if (RationConstant.RAT_COMFLAG_0.equals(refVO.getByReliefItemFlag())) {
            byItemAssure = rationDtlService.getItemAssure(ratCode, refVO.getByReliefItemCode());
        } else {// 综合品种取综合品种中的保证金比例
            byItemAssure = new RatItemDisVO();
            byItemAssure.setAssurepercent(assurepercent);
            byItemAssure.setInterestrate(interestrate);
        }
        result.put("ratBkrationReliefPage", refVO);
        result.put("ratBkrationPage", pageVO);
        result.put("reliefItemFlags", reliefItemFlags);
        result.put("reliefItems", reliefItems);
        result.put("disMoneyDTO", disMoneyDTO);
        result.put("byReliefItemFlags", byReliefItemFlags);
        result.put("byReliefItems", byReliefItems);
        result.put("byItemAssure", byItemAssure);
        result.put("flag", disService.queryRationDisWay(ratCode));// 所有分配方式
        return result;
    }
    private Map<String, RatBkrationReliefDetail> list2Map(List<RatBkrationReliefDetail> list) {
        return Maps.uniqueIndex(list, new Function<RatBkrationReliefDetail, String>() {
            public String apply(RatBkrationReliefDetail input) {
                return input.getReliefItemCode();
            }
        });
    }
    /**
     * 功能介绍 : 修改调剂信息。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationReliefPageVO getRationReliefPage(String refId) {
        List<RatBkrationReliefPageVO> refVO = ratBkrationReliefDao.queryRatBkRationReliefById(refId);
        if (null != refVO && !refVO.isEmpty()) {
            // 调剂明细
            return refVO.get(0);
        }
        return null;
    }
    /**
     * 功能介绍 : 根据授信分配方式获取调剂单位。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<CorpDTO> queryRatReliefCorp(String flag, String ratCode, String corpId) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        if (RationConstant.RAT_DISWAY_1.equals(flag) || RationConstant.RAT_DISWAY_2.equals(flag)
                || RationConstant.RAT_DISWAY_3.equals(flag)) {// 自用或金额共享或成员单位总额共享
            corpDTOs = ratBkrationReliefDao.getAllRatDisCorpByRatCode(ratCode, null);
        } else if (RationConstant.RAT_DISWAY_4.equals(flag)) {// 授信主体的总额共享，不管有无成员单位，只能以授信主体调剂
            corpDTOs.add(corpService.get(corpId));// 授信主体的单位
        } else if (RationConstant.RAT_DISWAY_5.equals(flag)) {// 金额共享+自用
            corpDTOs = ratBkrationReliefDao.getNotAmountDisCorpByRatCode(ratCode, null);
        } else if (RationConstant.RAT_DISWAY_6.equals(flag)) {// 金额共享+总额共享（含授信主体）
            corpDTOs = ratBkrationReliefDao.getMixCorpByRatCodeDisWay(ratCode, corpId);// 授信主体单位
        } else if (RationConstant.RAT_DISWAY_7.equals(flag)) {// 金额共享+总额共享（无授信主体）
            corpDTOs = ratBkrationReliefDao.getAllRatDisCorpByRatCode(ratCode, null);
        }
        // 单位名称
        if (ObjectUtil.isNotEmpty(corpDTOs)) {
            List<String> corpIdList = corpDTOs.stream().filter(item -> StrUtil.isBlank(item.getName()))
                    .map(CorpDTO::getId).collect(Collectors.toList());
            Map<String, CorpDTO> corpDTOMap = corpService.listCorpDtoByCorpIds(corpIdList);
            if (ObjectUtil.isNotEmpty(corpDTOMap)) {
                corpDTOs.forEach(item -> {
                    CorpDTO corpDTO = corpDTOMap.get(item.getId());
                    Optional.ofNullable(corpDTO).ifPresent(corp -> {
                        item.setName(corp.getName());
                        item.setCode(corp.getCode());
                    });
                });
            }
        }
        return corpDTOs;
    }
    /**
     * 功能介绍 : 获取授信分配单位的授信品种明细。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatItemDisVO> getItemCustomDTOs(String ratCode, String corpId, String itemFlag) {
        return disService.getItemCustomDTOs(ratCode, corpId, itemFlag);
    }
    /**
     * 功能介绍 : 获取授信分配单位的授信品种类型。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatItemDisVO> getCorpItemFlags(String ratCode, String corpId) {
        return disService.getCorpItemFlags(ratCode, corpId);
    }
    /**
     * 功能介绍 ： 删除授信调剂明细。<br>
     * <b>日期：</b>2017年8月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationRefStatus(String refCode) {
        RatBkrationRelief relief = get(refCode, true);
        relief.setStatus(CommonConstant.STATUE_DELETE);// 删除状态
        updateEntity(relief);
        // 授信操作日志
    }
    /**
     * 功能介绍 ： 删除授信调剂明细。<br>
     * <b>日期：</b>2017年8月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteRationRefDtl(String refCode) {
        refDetailService.deleteBkrationRefDtlByCode(refCode);
    }
    /**
     * 功能介绍 : 更新授信调剂。<br>
     * <b>日期：</b>2017年8月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationRelief(RatBkrationReliefPageVO refVO, String userId) {
        notificationService.complete(NotificationConstant.CMS.RAT_RELIEF, refVO.getId(), userId);
        // 先删除调剂明细
        deleteRationRefDtl(refVO.getId());
        // 保存调剂明细
        saveRationRelief(refVO, userId);
    }
    /**
     * 功能介绍 : 银行授信调剂保存。<br>
     * <b>日期：</b>2017年8月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRationRelief(RatBkrationReliefPageVO refVO, String userId) {
        // 保存前检查调剂数据
        preCheck(refVO);
        // 保存授信调剂数据
        RatBkrationRelief relief = new RatBkrationRelief();
        if (StringUtils.isEmpty(refVO.getId())) {
            BeanUtils.copyProperties(refVO, relief);
            relief.setRefCode(bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_REFCODE,
                    RationConstant.RAT_BKRATION_REF_CODE));// 授信调剂编号
            relief.setCreateBy(userId);
            relief.setCreateDate(DateUtils.getDate());
            saveEntity(relief);// 保存授信调剂
            refVO.setId(relief.getId());
        } else {
            relief = get(refVO.getId(), true);
            // 修改后的调剂信息
            relief.setStatus(refVO.getStatus());
            relief.setReliefItemFlag(refVO.getReliefItemFlag());
            relief.setReliefItemCode(refVO.getReliefItemCode());
            relief.setByReliefItemFlag(refVO.getByReliefItemFlag());
            relief.setByReliefItemCode(refVO.getByReliefItemCode());
            relief.setReliefMoney(refVO.getReliefMoney());
            relief.setReliefOpenMoney(refVO.getReliefOpenMoney());
            relief.setUpdateBy(userId);
            relief.setUpdateDate(DateUtils.getDate());
            relief.setAttachmentId(refVO.getAttachmentId());
            updateEntity(relief);// 更新授信调剂
        }
        List<RatBkrationReliefDtlPageVO> refDetails = new ArrayList<RatBkrationReliefDtlPageVO>();
        List<RatBkrationReliefDtlPageVO> byRefDetails = new ArrayList<RatBkrationReliefDtlPageVO>();
        // 保存授信调剂综合明细
        List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();
        for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
            if (!StringUtils.isEmpty(dtlPageVO.getReliefItemCode())) {
                RatBkrationReliefDetail refDetail = new RatBkrationReliefDetail();
                refDetail.setRefCode(relief.getId());// 调剂ID
                refDetail.setRefType(RationConstant.RAT_REF_TYPE_0);// 调剂方
                refDetail.setReliefCode(refVO.getReliefCode());// 调剂额度分配编号
                refDetail.setItemFlag(refVO.getReliefItemFlag());
                refDetail.setItemOrder(dtlPageVO.getItemOrder());
                refDetail.setReliefItemCode(dtlPageVO.getReliefItemCode());// 调剂品种
                if (RationConstant.RAT_COMFLAG_1.equals(refVO.getReliefItemFlag())) {// 综合品种
                    refDetail.setReliefMaxMoney(dtlPageVO.getReliefMaxMoney());// 调剂最高额度
                } else {
                    refDetail.setReliefMaxMoney(BigDecimal.ZERO);
                }
                refDetail.setReliefMoney(BigDecimal.ZERO);
                refDetails.add(dtlPageVO);
                refDetailService.saveRatBkrationRefDetail(refDetail);
            }
            if (!StringUtils.isEmpty(dtlPageVO.getByReliefItemCode())) {
                RatBkrationReliefDetail refDetail = new RatBkrationReliefDetail();
                refDetail.setRefCode(relief.getId());// 调剂ID
                refDetail.setRefType(RationConstant.RAT_REF_TYPE_1);// 被调剂方
                refDetail.setReliefCode(refVO.getByReliefCode());// 被调剂额度分配编号
                refDetail.setItemFlag(refVO.getByReliefItemFlag());
                refDetail.setItemOrder(dtlPageVO.getByItemOrder());
                refDetail.setReliefItemCode(dtlPageVO.getByReliefItemCode());// 调剂品种
                if (RationConstant.RAT_COMFLAG_1.equals(refVO.getByReliefItemFlag())) {// 综合品种
                    refDetail.setReliefMaxMoney(dtlPageVO.getByReliefMaxMoney());// 调剂最高额度
                } else {
                    refDetail.setReliefMaxMoney(BigDecimal.ZERO);
                }
                refDetail.setReliefMoney(BigDecimal.ZERO);
                byRefDetails.add(dtlPageVO);
                refDetailService.saveRatBkrationRefDetail(refDetail);
            }
        }
        if (RationConstant.STATUS_WAIT_CHECK.equals(refVO.getStatus())) {// 提交才需要更改调剂额度
            processReliefRation(refVO, refDetails, byRefDetails);
        }
        // 如果含有总额共享，则需要更新其他单位的虚拟调剂额度
        // ratAmountShareReliefMoney(refVO, refDetails);
        // 挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(relief.getStatus())) {
             getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_RELIEF, relief.getId(), relief,
                    relief.getCorpId(), userId);
        }
    }
    /**
     * 功能介绍 : 审批处理。<br>
     * <b>日期：</b>2017年9月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void processPassReliefRation(String refId) {
        RatBkrationReliefPageVO refVO = getRationReliefPage(refId);
        List<RatBkrationReliefDetail> reliefDetails = refDetailService.getRationReliefDtlByRefCode(refId);
        List<RatBkrationReliefDtlPageVO> refDetails = new ArrayList<RatBkrationReliefDtlPageVO>();
        List<RatBkrationReliefDtlPageVO> byRefDetails = new ArrayList<RatBkrationReliefDtlPageVO>();
        // 保存授信调剂综合明细
        for (RatBkrationReliefDetail reliefDetail : reliefDetails) {
            RatBkrationReliefDtlPageVO reliefDtlPageVO = new RatBkrationReliefDtlPageVO();
            BeanUtils.copyProperties(reliefDetail, reliefDtlPageVO);
            if (RationConstant.RAT_REF_TYPE_0.equals(reliefDetail.getRefType())) {
                reliefDtlPageVO.setReliefItemFlag(reliefDetail.getItemFlag());// 调剂品种类型
                reliefDtlPageVO.setReliefItemCode(reliefDetail.getReliefItemCode());// 调剂品种
                reliefDtlPageVO.setReliefMaxMoney(reliefDetail.getReliefMaxMoney());// 调剂额度
                reliefDtlPageVO.setReliefMoney(reliefDetail.getReliefMoney());// 附加调剂额度
                refDetails.add(reliefDtlPageVO);
            } else {
                reliefDtlPageVO.setByReliefItemFlag(reliefDetail.getItemFlag());// 调剂品种类型
                reliefDtlPageVO.setByReliefItemCode(reliefDetail.getReliefItemCode());// 调剂品种
                reliefDtlPageVO.setByReliefMaxMoney(reliefDetail.getReliefMaxMoney());// 调剂额度
                reliefDtlPageVO.setReliefMoney(reliefDetail.getReliefMoney());// 附加调剂额度
                byRefDetails.add(reliefDtlPageVO);
            }
        }
        processReliefRation(refVO, refDetails, byRefDetails);
        // 如果含有总额共享，则需要更新其他单位的虚拟调剂额度
        // ratAmountShareReliefMoney(refVO, refDetails);
    }
    /**
     * 功能介绍 : 授信全额共享模式下更新其他单位的虚拟调剂额度。<br>
     * <b>日期：</b>2017年8月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void ratAmountShareReliefMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs) {
        List<RatBkrationDis> disList = disService.getRatBkDisByRatCode(refVO.getRatCode(), null,
                RationConstant.DIS_TYPE_AMOUNT_SHARE);
        if (disList == null || disList.isEmpty()) {
            return;
        }
        String reliefCode = refVO.getReliefCode();// 调剂分配授信编号
        Integer refSize = Integer.valueOf(0);
        Integer refCorpSize = Integer.valueOf(0);
        for (RatBkrationDis dis : disList) {
            if (dis.getId().equals(reliefCode)) {
                refSize++;
            } else {
                refCorpSize++;
            }
        }
        if (refSize <= 0) {
            return;
        }
        if (refCorpSize <= 0) {
            return;
        }
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        // 获取调剂的主品种
        String reliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            reliefItemCode = refDtlVOs.get(0).getReliefItemCode();
        }
        for (RatBkrationDis dis : disList) {
            if (!dis.getId().equals(reliefCode)) {
                updateVirtualReliefMoney(dis.getId(), refVO, itemFlag, reliefItemCode);
            }
        }
    }
    /**
     * 功能介绍 : 更新虚拟调剂额度。<br>
     * <b>日期：</b>2017年8月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void updateVirtualReliefMoney(String disCode, RatBkrationReliefPageVO refVO, String itemFlag,
            String reliefItemCode) {
        // 根据调剂方的授信分配信息
        List<RatBkrationDisDtlDTO> disListDTOs = disDtlService.getRationDisDtlByRatCodes(refVO.getReliefCode(),
                itemFlag, reliefItemCode);// 授信分配明细
        for (RatBkrationDisDtlDTO dtlDTO : disListDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(reliefItemCode)) {
                    BigDecimal reliefMoney = dtlDTO.getVirtualReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
                    BigDecimal reliefOpenMoney = dtlDTO.getVirtualReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂敞口金额+调剂金额
                    dtlDTO.setVirtualReliefMoney(reliefMoney);
                    dtlDTO.setVirtualReliefOpenMoney(reliefOpenMoney);
                    disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新调剂额度
                    break;
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    BigDecimal reliefMoney = dtlDTO.getSuperReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
                    BigDecimal reliefOpenMoney = dtlDTO.getSuperReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂敞口金额+调剂金额
                    dtlDTO.setSuperReliefMoney(reliefMoney);
                    dtlDTO.setSuperReliefOpenMoney(reliefOpenMoney);
                    disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新调剂额度
                    break;
                }
                // 处理共享单位的最高调剂额度
            }
        }
    }
    /**
     * 功能介绍 : 统一处理授信调剂额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void processReliefRation(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDetails,
            List<RatBkrationReliefDtlPageVO> byRefDetails) {
        freezeRationDisMoney(refVO, refDetails);
        // 调剂打回，还原冻结额度
        backRationDisMoney(refVO, refDetails);
        // 如果调剂通过,则更新已分配额度
        passRationDisMoney(refVO, refDetails, byRefDetails);
        // 调整调剂的最高额度
        setReliefMaxMoney(refVO, refVO.getId());
        // 如果是总额共享，则共享单位的调剂方和被调剂方都需要处理调剂额度
    }
    /**
     * 功能介绍 : 处理调剂后最高额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setReliefMaxMoney(RatBkrationReliefPageVO refVO, String refCode) {
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {
            List<RatBkRationDtlVm> rationDtls = refDtlDao.getRationReliefDtl(refCode, RationConstant.RAT_REF_TYPE_0);
            setRationDisMaxMoney(refCode, refVO.getStatus(), refVO.getReliefCode(), rationDtls, true, false);
            // 总额共享下更新共享单位的调剂最高额度
            setAmountShareMaxMoney(refVO, refCode, rationDtls, true);
        }
        // 被调剂品种方
        String byItemFlag = refVO.getByReliefItemFlag();
        if (RationConstant.RAT_COMFLAG_1.equals(byItemFlag)) {
            List<RatBkRationDtlVm> rationDtls = refDtlDao.getRationReliefDtl(refCode, RationConstant.RAT_REF_TYPE_1);
            setRationDisMaxMoney(refCode, refVO.getStatus(), refVO.getByReliefCode(), rationDtls, false, false);
            // 总额共享下更新共享单位的调剂最高额度
            setAmountShareMaxMoney(refVO, refCode, rationDtls, false);
        }
    }
    /**
     * 功能介绍 : 总额共享下更新其他共享单位的调剂最高额度。<br>
     * <b>日期：</b>2017年8月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setAmountShareMaxMoney(RatBkrationReliefPageVO refVO, String refCode,
            List<RatBkRationDtlVm> rationDtls, boolean reliefFlag) {
        List<RatBkrationDis> disList = disService.getRatBkDisByRatCode(refVO.getRatCode(), null,
                RationConstant.DIS_TYPE_AMOUNT_SHARE);
        if (EmptyUtils.isEmptyList(disList)) {
            return;
        }
        String disCode = refVO.getReliefCode();
        if (!reliefFlag) {
            disCode = refVO.getByReliefCode();
        }
        for (RatBkrationDis dis : disList) {
            if (!dis.getId().equals(disCode)) {
                setRationDisMaxMoney(refCode, refVO.getStatus(), dis.getId(), rationDtls, reliefFlag, true);
            }
        }
    }
    /**
     * 功能介绍 : 授信分配最高额度冻结。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     * @param reliefFlag
     *            调剂标识
     * @param shareFlag
     *            总额共享标识
     */
    public void setRationDisMaxMoney(String refCode, Integer status, String disCode, List<RatBkRationDtlVm> rationDtls,
            boolean reliefFlag, boolean shareFlag) {
        if (null == rationDtls || rationDtls.isEmpty()) {
            return;
        }
        List<String> disCodes = new ArrayList<String>();
        disCodes.add(disCode);// 分配表ID
        List<RatBkrationDisDtlDTO> disDtlDTOs = disDtlService.getBkrationDisDtlByRatCodes(disCodes);
        Map<String, RatBkrationDisDtlDTO> disMap = Maps.uniqueIndex(disDtlDTOs,
                new Function<RatBkrationDisDtlDTO, String>() {
                    public String apply(RatBkrationDisDtlDTO input) {
                        return input.getItemCode();
                    }
                });// 授信分配信息
        // 授信调剂信息
        BigDecimal leftMoney = BigDecimal.ZERO;
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            String itemCode = dtlDTO.getItemCode();
            RatBkrationDisDtlDTO disDTO = disMap.get(itemCode);// 授信分配信息
            if (null == disDTO) {
                continue;
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getCompSign())) {
                if (CommonConstant.STATUE_WAIT_CHECK.equals(status) || CommonConstant.STATUE_CHECK.equals(status)) {// 待审批
                    // 获取调剂方冻结额度后剩余额度
                    if (reliefFlag) {
                        leftMoney = disDTO.getMoney().add(disDTO.getSuperByReliefMoney())
                                .subtract(disDTO.getSuperReliefMoney()).subtract(disDTO.getFreezeMoney())
                                .subtract(disDTO.getSuperProcessMoney()).subtract(disDTO.getReliefAndUrationMoney())
                                .subtract(disDTO.getVirtualUration());
                    }
                }
            }
        }
        Map<String, RatBkRationDtlVm> refMap = Maps.uniqueIndex(rationDtls, new Function<RatBkRationDtlVm, String>() {
            public String apply(RatBkRationDtlVm input) {
                return input.getItemCode();
            }
        });// 授信调剂信息
        for (RatBkrationDisDtlDTO disDTO : disDtlDTOs) {
            if (!RationConstant.RAT_COMFLAG_1.equals(disDTO.getItemFlag())) {// 非综合跳过
                continue;
            }
            String itemCode = disDTO.getItemCode();
            RatBkRationDtlVm dtlDTO = refMap.get(itemCode);// 授信调剂品种
            // 判断额度是否足够
            BigDecimal reliefMoney = BigDecimal.ZERO;
            BigDecimal money = BigDecimal.ZERO;
            if (null != dtlDTO) {
                reliefMoney = dtlDTO.getReliefMoney();// 实际应该调剂额度
                money = dtlDTO.getMoney();// 综合调剂额度
            }
            if (CommonConstant.STATUE_BACK.equals(status)) {// 审批打回
                // 更新调剂方冻结额度
                if (reliefFlag) {
                    if (shareFlag) {// 总额共享情况下
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(reliefMoney.add(money)));
                    } else {
                        disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().subtract(money));
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(reliefMoney));
                    }
                }
            }
            if (CommonConstant.STATUE_WAIT_CHECK.equals(status)) {// 待审批
                // 更新调剂方冻结额度
                if (reliefFlag) {
                    if (shareFlag) {// 总额共享情况下
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().add(reliefMoney.add(money)));
                    } else {
                        disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().add(money));
                        BigDecimal leftMaxMoney = disDTO.getMaxMoney().add(disDTO.getByReliefMaxMoney())
                                .subtract(disDTO.getReliefMaxMoney()).subtract(disDTO.getFreezeMaxMoney())
                                .subtract(disDTO.getProcessMaxMoney()).subtract(disDTO.getVirtualMaxMoney());// 剩余最高额度
                        if (leftMaxMoney.compareTo(leftMoney) > 0) {
                            BigDecimal virtualMaxMoney = disDTO.getVirtualMaxMoney()
                                    .add(leftMaxMoney.subtract(leftMoney));// 增加虚拟占用的最高额度
                            disDTO.setVirtualMaxMoney(virtualMaxMoney);
                            // 更新最高调整额度
                            reliefMoney = leftMaxMoney.subtract(leftMoney);// 修改调剂额度
                            if (null != dtlDTO) {
                                dtlDTO.setReliefMoney(reliefMoney);
                                refDtlDao.updateBkrationReliefMaxMoney(dtlDTO);// 更新调剂额度
                            } else {
                                RatBkrationReliefDetail refDetail = new RatBkrationReliefDetail();
                                refDetail.setRefCode(refCode);// 调剂ID
                                refDetail.setRefType(RationConstant.RAT_REF_TYPE_0);// 调剂方
                                refDetail.setReliefCode(disCode);// 调剂额度分配编号
                                refDetail.setItemFlag(RationConstant.RAT_COMFLAG_1);
                                refDetail.setItemOrder(disDTO.getItemOrder());
                                refDetail.setReliefItemCode(disDTO.getItemCode());// 调剂品种
                                refDetail.setReliefMoney(reliefMoney);// 实际调剂额度
                                refDetail.setReliefMaxMoney(money);
                                refDetailService.saveRatBkrationRefDetail(refDetail);
                            }
                        }
                    }
                }
            }
            if (CommonConstant.STATUE_CHECK.equals(status)) {// 审批通过
                if (reliefFlag) {// 调剂方
                    if (shareFlag) {// 总额共享情况下
                        disDTO.setReliefMaxMoney(disDTO.getReliefMaxMoney().add(money).add(reliefMoney));
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(reliefMoney.add(money)));
                    } else {
                        disDTO.setReliefMaxMoney(disDTO.getReliefMaxMoney().add(money).add(reliefMoney));
                        disDTO.setFreezeMaxMoney(disDTO.getFreezeMaxMoney().subtract(money));
                        disDTO.setVirtualMaxMoney(disDTO.getVirtualMaxMoney().subtract(reliefMoney));
                    }
                } else {// 被调剂方
                    disDTO.setByReliefMaxMoney(disDTO.getByReliefMaxMoney().add(money));
                }
            }
            // 更新调剂金额
            disDtlService.updateRationDisMaxMoney(disDTO);
        }
    }
    /**
     * 功能介绍 : 审批通过更新授信分配已调节额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passRationDisMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs,
            List<RatBkrationReliefDtlPageVO> byRefDtlVOs) {
        // 审批时冻结了调剂额度，打回时需释放调剂的额度
        if (refVO.getStatus() != CommonConstant.STATUE_CHECK) {
            return;
        }
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        // 获取调剂的主品种
        String reliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            reliefItemCode = refDtlVOs.get(0).getReliefItemCode();
        }
        // 根据调剂方的授信分配信息
        List<RatBkrationDisDtlDTO> disListDTOs = disDtlService.getRationDisDtlByRatCodes(refVO.getReliefCode(),
                itemFlag, reliefItemCode);// 授信分配明细
        for (RatBkrationDisDtlDTO dtlDTO : disListDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(reliefItemCode)) {
                    passRationReliefMoney(dtlDTO, refVO);
                    break;
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    passRationReliefMoney(dtlDTO, refVO);
                    break;
                }
            }
        }
        // 总额共享下成员单位的虚拟调剂额度都需要跟着调整(还原虚拟调剂额度，更新调剂额度)
        List<RatBkrationDisDtlDTO> corpDisDTOs = disDtlService.getAmountShareRationDisDtl(refVO.getRatCode(),
                refVO.getReliefCode(), itemFlag, reliefItemCode, refVO.getReliefCorp());
        if (!EmptyUtils.isEmptyList(corpDisDTOs)) {
            for (RatBkrationDisDtlDTO dtlDTO : corpDisDTOs) {
                String itemCode = dtlDTO.getItemCode();
                // 比较调剂品种
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                    if (itemCode.equals(reliefItemCode)) {
                        passAmountReliefMoney(dtlDTO, refVO);
                    }
                } else {// 综合品种，直接取综合品种的综合使用标识
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                        passAmountReliefMoney(dtlDTO, refVO);
                    }
                }
            }
        }
        // 调整被调剂方额度
        byReliefCorpDisrationMoney(refVO, byRefDtlVOs);
        // 更新授信录入明细表额度使用情况
        updateRationDtlMoney(refVO, refDtlVOs, byRefDtlVOs);
    }
    /**
     * 功能介绍 : 审批通过后调整调剂方额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passRationReliefMoney(RatBkrationDisDtlDTO dtlDTO, RatBkrationReliefPageVO refVO) {
        BigDecimal reliefMoney = dtlDTO.getSuperReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
        BigDecimal reliefOpenMoney = dtlDTO.getSuperReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂敞口金额+调剂金额
        dtlDTO.setSuperReliefMoney(reliefMoney);
        dtlDTO.setSuperReliefOpenMoney(reliefOpenMoney);
        // 冻结额度还原
        BigDecimal freezeMoney = dtlDTO.getFreezeMoney().subtract(refVO.getReliefMoney());// 原冻结额度-调剂金额
        BigDecimal freezeOpenMoney = dtlDTO.getFreezeOpenMoney().subtract(refVO.getReliefOpenMoney());// 原冻结额度-调剂金额
        dtlDTO.setFreezeMoney(freezeMoney);
        dtlDTO.setFreezeOpenMoney(freezeOpenMoney);
        disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
    }
    /**
     * 功能介绍 : 审批通过后调整调剂方额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passAmountReliefMoney(RatBkrationDisDtlDTO dtlDTO, RatBkrationReliefPageVO refVO) {
        BigDecimal reliefMoney = dtlDTO.getSuperReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
        BigDecimal reliefOpenMoney = dtlDTO.getSuperReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂敞口金额+调剂金额
        dtlDTO.setSuperReliefMoney(reliefMoney);
        dtlDTO.setSuperReliefOpenMoney(reliefOpenMoney);
        BigDecimal virtualReliefMoney = dtlDTO.getVirtualReliefMoney().subtract(refVO.getReliefMoney());// 原调剂金额-调剂金额
        BigDecimal virtualReliefOpenMoney = dtlDTO.getVirtualReliefOpenMoney().subtract(refVO.getReliefOpenMoney());// 原调剂金额-调剂金额
        dtlDTO.setVirtualReliefMoney(virtualReliefMoney);
        dtlDTO.setVirtualReliefOpenMoney(virtualReliefOpenMoney);
        disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
    }
    /**
     * 功能介绍 : 更新授信录入中的额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs,
            List<RatBkrationReliefDtlPageVO> byRefDtlVOs) {
        // 调剂品种相同则不需要调整额度
        String itemFlag = refVO.getReliefItemFlag();
        String byItemFlag = refVO.getByReliefItemFlag();
        String reliefItemCode = refVO.getReliefItemCode();// 调剂品种
        String byReliefItemCode = refVO.getByReliefItemCode();// 被调剂品种
        if (itemFlag.equals(byItemFlag)) {
            if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {// 综合品种
                return;
            } else {
                reliefItemCode = refDtlVOs.get(0).getReliefItemCode();
                byReliefItemCode = byRefDtlVOs.get(0).getByReliefItemCode();
                if (reliefItemCode.equals(byReliefItemCode)) {// 单品种相同
                    return;
                }
            }
        }
        Map<String, RatBkrationReliefDtlPageVO> refMap = null;
        Map<String, RatBkrationReliefDtlPageVO> byRefMap = null;
        if (RationConstant.RAT_COMFLAG_1.equals(itemFlag)) {// 综合品种
            refMap = Maps.uniqueIndex(refDtlVOs, new Function<RatBkrationReliefDtlPageVO, String>() {
                public String apply(RatBkrationReliefDtlPageVO input) {
                    return input.getReliefItemCode();
                }
            });
        }
        if (RationConstant.RAT_COMFLAG_1.equals(byItemFlag)) {// 综合品种
            byRefMap = Maps.uniqueIndex(byRefDtlVOs, new Function<RatBkrationReliefDtlPageVO, String>() {
                public String apply(RatBkrationReliefDtlPageVO input) {
                    return input.getByReliefItemCode();
                }
            });
        }
        // 更新授信录入调剂额度
        List<RatBkRationDtlVm> dtlDTOs = rationDtlService.getBkrationDtlDTOByRatCode(refVO.getRatCode(), null);
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemFlag.equals(dtlDTO.getItemFlag())) {// 调剂方额度减少
                    if (itemCode.equals(reliefItemCode)) {
                        calRationDisMoney(dtlDTO, refVO, true);
                    }
                }
                if (byItemFlag.equals(dtlDTO.getItemFlag())) {// 被调剂方额度增加
                    if (itemCode.equals(byReliefItemCode)) {
                        calRationDisMoney(dtlDTO, refVO, false);
                    }
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (itemFlag.equals(dtlDTO.getItemFlag())) {// 调剂方额度减少
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                        calRationDisMoney(dtlDTO, refVO, true);
                    }
                    // 综合品种要更新最高额度
                    // 最高额度减少
                    RatBkrationReliefDtlPageVO dtlVO = refMap.get(itemCode);
                    if (null != dtlVO) {// 存在调剂才调整
                        dtlDTO.setMaxMoney(
                                dtlDTO.getMaxMoney().subtract(dtlVO.getReliefMaxMoney().add(dtlVO.getReliefMoney())));// 最高额度减少
                        // 只更新最高额度
                        rationDtlService.updateRationDtlMaxMoney(dtlDTO);
                    }
                }
                if (byItemFlag.equals(dtlDTO.getItemFlag())) {// 被调剂方额度增加
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                        calRationDisMoney(dtlDTO, refVO, false);
                    }
                    // 最高额度增加
                    RatBkrationReliefDtlPageVO dtlVO = byRefMap.get(itemCode);
                    if (null != dtlVO) {// 存在被调剂才调整
                        dtlDTO.setMaxMoney(dtlDTO.getMaxMoney().add(dtlVO.getByReliefMaxMoney()));// 最高额度增加
                        // 只更新最高额度
                        rationDtlService.updateRationDtlMaxMoney(dtlDTO);
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 ： 处理调剂额度，调剂方额度减少，被调剂方额度增加。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     * @param reliefFlag
     *            是否调剂方
     */
    public void calRationDisMoney(RatBkRationDtlVm dtlDTO, RatBkrationReliefPageVO refVO, boolean reliefFlag) {
        if (reliefFlag) {// 调剂方额度减少
            dtlDTO.setMoney(dtlDTO.getMoney().subtract(refVO.getReliefMoney()));// 录入额度=原额度-调剂额度
            dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().subtract(refVO.getReliefOpenMoney()));// 录入敞口额度=原额度-调剂额度
            dtlDTO.setSuperDistributeMoney(dtlDTO.getSuperDistributeMoney().subtract(refVO.getReliefMoney()));// 已分配额度=原额度-调剂额度
            dtlDTO.setSuperDistributeOpenMoney(
                    dtlDTO.getSuperDistributeOpenMoney().subtract(refVO.getReliefOpenMoney()));// 已分配敞口额度=原额度-调剂额度
            rationDtlService.updateRationDtlReliefMoney(dtlDTO);// 更新调剂额度
        } else {// 被调剂方额度增加
            dtlDTO.setMoney(dtlDTO.getMoney().add(refVO.getReliefMoney()));// 录入额度=原额度+调剂额度
            dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(refVO.getReliefOpenMoney()));// 录入敞口额度=原额度+调剂额度
            dtlDTO.setSuperDistributeMoney(dtlDTO.getSuperDistributeMoney().add(refVO.getReliefMoney()));// 已分配额度=原额度+调剂额度
            dtlDTO.setSuperDistributeOpenMoney(dtlDTO.getSuperDistributeOpenMoney().add(refVO.getReliefOpenMoney()));// 已分配敞口额度=原额度+调剂额度
            rationDtlService.updateRationDtlReliefMoney(dtlDTO);// 更新调剂额度
        }
    }
    /**
     * 功能介绍 : 被调剂方额度调整。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void byReliefCorpDisrationMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs) {
        // 被调剂品种方
        String byItemFlag = refVO.getByReliefItemFlag();
        // 获取调剂的主品种
        String byReliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(byItemFlag)) {// 单品种
            byReliefItemCode = refDtlVOs.get(0).getReliefItemCode();
        }
        // 根据调剂方的授信分配信息
        List<RatBkrationDisDtlDTO> disListDTOs = disDtlService.getRationDisDtlByRatCodes(refVO.getByReliefCode(),
                byItemFlag, byReliefItemCode);// 授信分配明细
        if (!EmptyUtils.isEmptyList(disListDTOs)) {
            for (RatBkrationDisDtlDTO dtlDTO : disListDTOs) {
                String itemCode = dtlDTO.getItemCode();
                // 比较调剂品种
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                    if (itemCode.equals(byReliefItemCode)) {
                        passRationByReliefMoney(dtlDTO, refVO);
                        break;
                    }
                } else {// 综合品种，直接取综合品种的综合使用标识
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                        passRationByReliefMoney(dtlDTO, refVO);
                        break;
                    }
                }
            }
        }
        // 总额共享下成员单位的被调剂额度都需要跟着调整
        List<RatBkrationDisDtlDTO> corpDisDTOs = disDtlService.getAmountShareRationDisDtl(refVO.getRatCode(),
                refVO.getByReliefCode(), byItemFlag, byReliefItemCode, refVO.getByReliefCorp());
        if (!EmptyUtils.isEmptyList(corpDisDTOs)) {
            for (RatBkrationDisDtlDTO dtlDTO : corpDisDTOs) {
                String itemCode = dtlDTO.getItemCode();
                // 比较调剂品种
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                    if (itemCode.equals(byReliefItemCode)) {
                        passRationByReliefMoney(dtlDTO, refVO);
                    }
                } else {// 综合品种，直接取综合品种的综合使用标识
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                        passRationByReliefMoney(dtlDTO, refVO);
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 审批通过后调整被调剂方额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void passRationByReliefMoney(RatBkrationDisDtlDTO dtlDTO, RatBkrationReliefPageVO refVO) {
        BigDecimal byReliefMoney = dtlDTO.getSuperByReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
        BigDecimal byReliefOpenMoney = dtlDTO.getSuperByReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂敞口金额+调剂金额
        dtlDTO.setSuperByReliefMoney(byReliefMoney);// 被调剂额度
        dtlDTO.setSuperByReliefOpenMoney(byReliefOpenMoney);// 被调剂敞口额度
        disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新被调剂额度
    }
    /**
     * 功能介绍 : 处理审批打回授信分配额度处理。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void backRationDisMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs) {
        // 审批时冻结了调剂额度，打回时需释放调剂的额度
        if (refVO.getStatus() != CommonConstant.STATUE_BACK) {
            return;
        }
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        // 获取调剂的主品种
        String reliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            reliefItemCode = refDtlVOs.get(0).getReliefItemCode();
        }
        // 根据调剂方的授信分配信息
        List<RatBkrationDisDtlDTO> disListDTOs = disDtlService.getRationDisDtlByRatCodes(refVO.getReliefCode(),
                itemFlag, reliefItemCode);// 授信分配明细
        for (RatBkrationDisDtlDTO dtlDTO : disListDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(reliefItemCode)) {
                    BigDecimal freezeMoney = dtlDTO.getFreezeMoney().subtract(refVO.getReliefMoney());// 原冻结额度-调剂金额
                    BigDecimal freezeOpenMoney = dtlDTO.getFreezeOpenMoney().subtract(refVO.getReliefOpenMoney());// 原冻结额度-调剂金额
                    dtlDTO.setFreezeMoney(freezeMoney);
                    dtlDTO.setFreezeOpenMoney(freezeOpenMoney);
                    disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    BigDecimal freezeMoney = dtlDTO.getFreezeMoney().subtract(refVO.getReliefMoney());// 原冻结额度-调剂金额
                    BigDecimal freezeOpenMoney = dtlDTO.getFreezeOpenMoney().subtract(refVO.getReliefOpenMoney());// 原冻结额度-调剂金额
                    dtlDTO.setFreezeMoney(freezeMoney);
                    dtlDTO.setFreezeOpenMoney(freezeOpenMoney);
                    disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
                }
            }
        }
        // 还原总额共享下成员单位的虚拟调剂额度virtual_relief_money
        processAmountFreezeMoney(refVO, false);
    }
    /**
     * 功能介绍 : 设置冻结额度。<br>
     * <b>日期：</b>2017年8月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void freezeRationDisMoney(RatBkrationReliefPageVO refVO, List<RatBkrationReliefDtlPageVO> refDtlVOs) {
        if (refVO.getStatus() < CommonConstant.STATUE_WAIT_CHECK || refVO.getStatus() >= CommonConstant.STATUE_CHECK) {
            return;
        }
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        // 获取调剂的主品种
        String reliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            reliefItemCode = refVO.getReliefItemCode();
        }
        // 根据调剂方的授信分配信息
        List<RatBkrationDisDtlDTO> disListDTOs = disDtlService.getRationDisDtlByRatCodes(refVO.getReliefCode(),
                itemFlag, reliefItemCode);// 授信分配明细
        for (RatBkrationDisDtlDTO dtlDTO : disListDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(reliefItemCode)) {
                    freezeRationReliefMoney(dtlDTO, refVO, true);
                    break;
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    freezeRationReliefMoney(dtlDTO, refVO, true);
                    break;
                }
            }
        }
        // 冻结总额共享下成员单位的虚拟调剂额度virtual_relief_money
        processAmountFreezeMoney(refVO, true);
    }
    /**
     * 功能介绍 : 处理总额共享单位的冻结额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     * @param freezeFlag:
     *            冻结标识
     */
    public void processAmountFreezeMoney(RatBkrationReliefPageVO refVO, boolean freezeFlag) {
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        // 获取调剂的主品种
        String reliefItemCode = "";
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            reliefItemCode = refVO.getReliefItemCode();
        }
        // 冻结总额共享下成员单位的虚拟调剂额度virtual_relief_money
        List<RatBkrationDisDtlDTO> corpDisDTOs = disDtlService.getAmountShareRationDisDtl(refVO.getRatCode(),
                refVO.getReliefCode(), itemFlag, reliefItemCode, refVO.getReliefCorp());
        for (RatBkrationDisDtlDTO dtlDTO : corpDisDTOs) {
            String itemCode = dtlDTO.getItemCode();
            // 比较调剂品种
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {// 单品种
                if (itemCode.equals(reliefItemCode)) {
                    freezeVirtualReliefMoney(dtlDTO, refVO, freezeFlag);
                }
            } else {// 综合品种，直接取综合品种的综合使用标识
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    freezeVirtualReliefMoney(dtlDTO, refVO, freezeFlag);
                }
            }
        }
    }
    /**
     * 功能介绍 : 共享单位的调剂额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void freezeRationReliefMoney(RatBkrationDisDtlDTO dtlDTO, RatBkrationReliefPageVO refVO,
            boolean freezeFlag) {
        BigDecimal freezeMoney = BigDecimal.ZERO;
        BigDecimal freezeOpenMoney = BigDecimal.ZERO;
        if (freezeFlag) {// 冻结
            freezeMoney = dtlDTO.getFreezeMoney().add(refVO.getReliefMoney());// 调剂金额+原冻结额度
            freezeOpenMoney = dtlDTO.getFreezeOpenMoney().add(refVO.getReliefOpenMoney());// 调剂金额+原冻结额度
        } else {// 冻结还原
            freezeMoney = dtlDTO.getFreezeMoney().subtract(refVO.getReliefMoney());// 调剂金额-原冻结额度
            freezeOpenMoney = dtlDTO.getFreezeOpenMoney().subtract(refVO.getReliefOpenMoney());// 调剂金额-原冻结额度
        }
        dtlDTO.setFreezeMoney(freezeMoney);
        dtlDTO.setFreezeOpenMoney(freezeOpenMoney);
        disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
    }
    /**
     * 功能介绍 : 共享单位的虚拟调剂额度。<br>
     * <b>日期：</b>2017年9月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void freezeVirtualReliefMoney(RatBkrationDisDtlDTO dtlDTO, RatBkrationReliefPageVO refVO,
            boolean freezeFlag) {
        BigDecimal virtualReliefMoney = BigDecimal.ZERO;
        BigDecimal virtualReliefOpenMoney = BigDecimal.ZERO;
        if (freezeFlag) {// 冻结
            virtualReliefMoney = dtlDTO.getVirtualReliefMoney().add(refVO.getReliefMoney());// 原调剂金额+调剂金额
            virtualReliefOpenMoney = dtlDTO.getVirtualReliefOpenMoney().add(refVO.getReliefOpenMoney());// 原调剂金额+调剂金额
        } else {// 冻结还原
            virtualReliefMoney = dtlDTO.getVirtualReliefMoney().subtract(refVO.getReliefMoney());// 原调剂金额-调剂金额
            virtualReliefOpenMoney = dtlDTO.getVirtualReliefOpenMoney().subtract(refVO.getReliefOpenMoney());// 原调剂金额-调剂金额
        }
        dtlDTO.setVirtualReliefMoney(virtualReliefMoney);
        dtlDTO.setVirtualReliefOpenMoney(virtualReliefOpenMoney);
        disDtlService.updateRationDisFreezeMoney(dtlDTO);// 更新冻结额度
    }
    /**
     * 功能介绍 : 调剂前检查。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void preCheck(RatBkrationReliefPageVO refVO) {
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        String byItemFlag = refVO.getByReliefItemFlag();// 被调剂方
        // 获取调剂的主品种
        String reliefItemCode = RationConstant.RAT_COMFLAG_1;
        String byReliefItemCode = RationConstant.RAT_COMFLAG_1;
        BigDecimal reliefMoney = refVO.getReliefMoney();// 调剂额度
        if (reliefMoney.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_MONEY_ZERO);
        }
        List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();
        for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
            if (!StringUtils.isEmpty(dtlPageVO.getReliefItemCode())) {
                if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
                    reliefItemCode = dtlPageVO.getReliefItemCode();
                    refVO.setReliefItemCode(reliefItemCode);
                }
            }
            if (!StringUtils.isEmpty(dtlPageVO.getByReliefItemCode())) {
                if (RationConstant.RAT_COMFLAG_0.equals(byItemFlag)) {// 单品种
                    byReliefItemCode = dtlPageVO.getByReliefItemCode();
                    refVO.setByReliefItemCode(byReliefItemCode);
                }
            }
        }
        // 同合同同一单位同一品种不能调剂
        if (refVO.getReliefCode().equals(refVO.getByReliefCode())) {
            if (refVO.getReliefCorp().equals(refVO.getByReliefCorp())) {
                if (itemFlag.equals(byItemFlag)) {// 相同品种间调剂
                    if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                        if (refVO.getReliefItemCode().equals(refVO.getByReliefItemCode())) {
                            throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_SAME_CORP);
                        }
                    } else {
                        throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_SAME_CORP);
                    }
                }
            }
        } else {
            // 调剂的单位是总额共享的单位并且品种相同
            RatBkrationDis reliefDis = disService.get(refVO.getReliefCode(), true);// 调剂方
            RatBkrationDis byReliefDis = disService.get(refVO.getByReliefCode(), true);// 被调剂方
            if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(reliefDis.getDistributeWay())
                    && RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(byReliefDis.getDistributeWay())) {
                if (itemFlag.equals(byItemFlag)) {// 相同品种间调剂
                    if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                        if (refVO.getReliefItemCode().equals(refVO.getByReliefItemCode())) {
                            throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_AMOUNT_SAME_CORP);
                        }
                    } else {
                        throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_AMOUNT_SAME_CORP);
                    }
                }
            }
        }
        // 同单位同一授信品种不能同时存在具有包含关系的二笔授信中
        // 存在两笔同银行同单位同品种授信额度度，如两笔授信额度的授信时间交叉，在交叉时间范围内，只能对后一笔授信额度做调剂
        checkDisLeftMoney(refVO);
    }
    /**
     * 功能介绍 : 检查分配后剩余额度是否足够。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkDisLeftMoney(RatBkrationReliefPageVO refVO) {
        // 调剂品种方
        String itemFlag = refVO.getReliefItemFlag();
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {// 单品种
            String itemCode = refVO.getReliefItemCode();
            List<RatBkrationCpAddDtlDTO> dtlDTOs = getRationDisDtlByItemCode(refVO.getReliefCode(), itemFlag, itemCode);
            if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                RatBkrationCpAddDtlDTO dtlDTO = dtlDTOs.get(0);
                BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney())
                        .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
                        .subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getSuperReliefMoney())
                        .add(dtlDTO.getSuperByReliefMoney());// 剩余额度
                if (money.compareTo(refVO.getReliefMoney()) < 0) {// 小于调剂额度，则不能调剂
                    throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_MONEY_BIGGER);
                }
            }
        } else {// 综合品种调剂
            List<RatBkrationReliefDtlPageVO> refDetails = new ArrayList<RatBkrationReliefDtlPageVO>();
            List<RatBkrationReliefDtlPageVO> reliefDtls = refVO.getReliefDtls();// 调剂方明细集合
            for (RatBkrationReliefDtlPageVO dtlPageVO : reliefDtls) {
                if (!StringUtils.isEmpty(dtlPageVO.getReliefItemCode())) {// 调剂品种不为空
                    refDetails.add(dtlPageVO);
                }
            }
            List<RatBkrationCpAddDtlDTO> dtlDTOs = getRationDisDtlByItemCode(refVO.getReliefCode(), itemFlag, null);
            Map<String, RatBkrationCpAddDtlDTO> dtlMap = new HashMap<String, RatBkrationCpAddDtlDTO>();
            if (!EmptyUtils.isEmptyList(dtlDTOs)) {
                dtlMap = Maps.uniqueIndex(dtlDTOs, new Function<RatBkrationCpAddDtlDTO, String>() {
                    @Override
                    public String apply(RatBkrationCpAddDtlDTO input) {
                        return input.getItemCode();
                    }
                });
            }
            if (!EmptyUtils.isEmptyList(refDetails)) {
                BigDecimal leftMoney = BigDecimal.ZERO;
                for (RatBkrationReliefDtlPageVO refDtl : refDetails) {
                    String itemCode = refDtl.getReliefItemCode();// 调剂综合品种
                    RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(itemCode);
                    if (null != dtlDTO) {
                        if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getCompSign())) {// 综合使用标识
                            BigDecimal money = dtlDTO.getMoney().subtract(dtlDTO.getSuperProcessMoney())
                                    .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
                                    .subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getSuperReliefMoney())
                                    .add(dtlDTO.getSuperByReliefMoney());// 剩余额度
                            if (money.compareTo(refVO.getReliefMoney()) < 0) {// 小于调剂额度，则不能调剂
                                throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_MONEY_BIGGER);
                            }
                            leftMoney = money.subtract(refVO.getReliefMoney());// 调剂后剩余额度
                            break;
                        }
                    }
                }
                for (RatBkrationReliefDtlPageVO refDtl : refDetails) {
                    String itemCode = refDtl.getReliefItemCode();// 调剂综合品种
                    RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(itemCode);
                    if (null != dtlDTO) {
                        if (dtlDTO.getMaxMoney().compareTo(refVO.getReliefMoney()) < 0) {// 最高可用额度小于调剂额度，则不能调剂
                            throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_MAXMONEY_BIGGER,
                                    dtlDTO.getItemName());
                        }
                        // BigDecimal leftMaxMoney =
                        // dtlDTO.getMaxMoney().subtract(refDtl.getReliefMaxMoney());//调剂后剩余最高额度
                        // if (leftMaxMoney.compareTo(leftMoney) > 0) {//最高剩余额度大于综合品种的可用额度,不能调剂
                        // throw new ParameterException(RatMessageEnum.CMS_RAT_RELIEF_MAXMONEY_LEFT,
                        // dtlDTO.getItemName());
                        // }
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 查询授信分配剩余额度。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByItemCode(String disId, String itemFlag, String itemCode) {
        return disDtlDao.getRationDisDtlByDisDtlId(disId, itemFlag, itemCode);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        boolean result = workFlowResult.isPass();// 是否通过
        getEntityDao().flush();
        RatBkrationRelief relief = get(businessKey, true);
        if (result) {
            relief.setStatus(RationConstant.STATUS_CHECK);// 审批通过
            update(relief);
            // TODO 审批通过后处理
        } else {
            relief.setStatus(RationConstant.STATUS_BACK);// 审批打回
            update(relief);
        }
        getEntityDao().flush();
        processPassReliefRation(businessKey);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 银行授信占用在途数据
            int dataSize1 = ratBkrationReliefDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_RELIEF_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
