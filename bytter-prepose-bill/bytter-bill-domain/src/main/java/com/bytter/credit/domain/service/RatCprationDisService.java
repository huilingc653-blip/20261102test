package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.*;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.entity.RatDisDetailHis;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationMoneyBean;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatDisDetailHisVO;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.*;
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
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author luxy
 * @version 1.0
 * @Title:RatCprationDisService
 * @Description:
 * @date 2018年11月19日 下午13:45:57
 */
@Service("RatCprationDisService")
@Transactional
@WorkFlow(value = {RatWorkFlowConstant.RAT_CPRATION_DIS})
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_CPRATION_DIS)
public class RatCprationDisService extends BaseService<RatBkrationDis, String>
        implements IWorkFlowListener, ITaskService, ICorpDisableVerifyService {
    @Autowired
    private RatCprationDisDao ratCprationDisDao;
    @Autowired
    private RatCprationService ratCprationService;
    @Autowired
    private RatBkrationDetailDao ratDetailDao;
    @Autowired
    private RatBkrationDisDetailDao disDetailDao;
    @Autowired
    private RatBkrationAddDetailDao addDetailDao;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private RatCprationLogService ratLogService;
    @Autowired
    private RatDisDetailHisService ratDisHisService;
    @Autowired
    private RatDisDetailHisDao ratDetailHisDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 查询内部授信分配
     */
    @Transactional(readOnly = true)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput listDisOutputData(DataTablesInput input, String userId) {
         String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(RationConstant.CMS_CPRATION_DIS_QUERY, userId), "rb.corp_Id", true);
        return ratCprationService.setOutputPageData(ratCprationDisDao.listDisOutputData(input, corpAuthSql));
    }
    /**
     * 获取授信分配信息
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO getRatCpRationDisInfo(String ratCode) {
        RatBkrationPageVO pageVO = ratCprationService.queryCprationById(ratCode);// 内部授信录入明细
        getRationLeftMoney(pageVO);// 计算剩余分配额度
        return pageVO;
    }
    /**
     * 获取剩余分配额度
     */
    @Transactional(readOnly = true)
    public void getRationLeftMoney(RatBkrationPageVO pageVO) {
        BigDecimal money = BigDecimal.ZERO;
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        // 获取未使用额度作为剩余额度
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            // if(dtlDTO.getDisMoney().equals(BigDecimal.ZERO)){//内部授信第一次分配
            // dtlDTO.setLeftMoney(dtlDTO.getMoney());
            // dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());//剩余额度
            // money = money.add(dtlDTO.getUnUserMoney());
            // }else{
            RatBkrationMoneyBean ratBean = getBankRationItemMoney(pageVO.getId(), pageVO.getCorpId(),
                    dtlDTO.getItemFlag(), dtlDTO.getItemCode());
            dtlDTO.setLeftMoney(ratBean.getUnUseMoney());// 剩余额度
            dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());
            money = money.add(dtlDTO.getUnUserMoney());
            // }
        }
        pageVO.setUnUserMoney(money);// 待分配授信金额
    }
    /**
     * 获取单个品种的授信明细
     */
    public RatBkrationMoneyBean getBankRationItemMoney(String ratCode, String corpId, String itemFlag,
                                                       String itemCode) {
        RatBkrationMoneyBean ratCpMoneyBean = new RatBkrationMoneyBean();
        ratCpMoneyBean.setRatCode(ratCode);
        ratCpMoneyBean.setCorpId(corpId);
        ratCpMoneyBean.setItemCode(itemCode);
        ratCpMoneyBean.setItemFlag(itemFlag);
        getRationItemLeftMoney(ratCpMoneyBean);// 获取单个品种的剩余分配额度
        List<RatBkrationCpAddDtlDTO> disList = new ArrayList<RatBkrationCpAddDtlDTO>();
        // 获取除自动分配外已分配明细
        List<RatBkrationCpAddDtlDTO> otherDisList = getRationDisDtlByRatCode(ratCode, itemFlag, itemCode);
        if (!EmptyUtils.isEmptyList(otherDisList)) {
            disList.addAll(otherDisList);
        }
        // 获取自动分配的分配明细
        RatBkrationCpAddDtlDTO autoDisList = getRationDisDtl(ratCode, itemFlag, itemCode);
        BigDecimal disMoney = BigDecimal.ZERO;// 分配额度
        BigDecimal reliefMoney = BigDecimal.ZERO;// 调剂额度
        BigDecimal leftMoney = BigDecimal.ZERO;// 分配剩余额度
        if (!EmptyUtils.isEmptyList(disList)) {// 分配过
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                // 循环计算已分配额度
                for (RatBkrationCpAddDtlDTO dtlDTO : disList) {
                    if (itemCode.equals(dtlDTO.getItemCode())) {
                        disMoney = disMoney.add(getDisMoney(dtlDTO));// 分配额度
                        reliefMoney = reliefMoney
                                .add(dtlDTO.getSuperByReliefMoney().subtract(dtlDTO.getSuperReliefMoney()));// 可用额度
                        leftMoney = ratCpMoneyBean.getMoney().subtract(disMoney).add(reliefMoney);
                    }
                }
                ratCpMoneyBean.setDisMoney(disMoney);
                ratCpMoneyBean.setLeftMoney(leftMoney);
            }
        } else {// 未分配过
            ratCpMoneyBean.setDisMoney(BigDecimal.ZERO);
            ratCpMoneyBean.setLeftMoney(ratCpMoneyBean.getMoney());
        }
        // 根据授信编号和品种编号查询待审批数据
        RatDisDetailHisVO ratDisDetailHisVO = ratDisHisService.queryDisDetailApprove(ratCode, itemCode);
        BigDecimal approveMoney = BigDecimal.ZERO;
        if (ratDisDetailHisVO != null) {
            approveMoney = approveMoney.add(ratDisDetailHisVO.getMoney());
        }
        // 品种的未使用额度=分配剩余额度-自动分配授信占用额度-分配待审批额度
        if (autoDisList != null) {
            ratCpMoneyBean.setUnUseMoney(ratCpMoneyBean.getLeftMoney().subtract(autoDisList.getSuperProcessMoney()).subtract(approveMoney));
        } else {
            ratCpMoneyBean.setUnUseMoney(new BigDecimal("100000"));
        }
        // 整个授信追减或追减的额度
        getRationItemAddMoney(ratCpMoneyBean, RationConstant.RAT_ADD_FLAG_COMPANY);
        return ratCpMoneyBean;
    }
    /**
     * 计算某个品种剩余授信分配额度
     */
    public void getRationItemLeftMoney(RatBkrationMoneyBean RatCpMoneyBean) {
        // 计算某个品种的已分配额度和剩余额度
        List<RatBkrationDetail> ratList = ratDetailDao.getBkrationDtlByRatCode(RatCpMoneyBean.getRatCode(),
                RatCpMoneyBean.getItemFlag(), RatCpMoneyBean.getItemCode());
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal unUseMoney = BigDecimal.ZERO;
        for (RatBkrationDetail detail : ratList) {
            if (RatCpMoneyBean.getItemCode().equals(detail.getItemCode())) {
                money = money.add(detail.getMoney().add(detail.getSuperAddMoney()));
                unUseMoney = unUseMoney.add(detail.getMoney().add(detail.getSuperAddMoney()))
                        .subtract(detail.getSuperDistributeMoney());// 未使用额度
            }
        }
        RatCpMoneyBean.setMoney(money);
        RatCpMoneyBean.setUnUseMoney(unUseMoney);
    }
    /**
     * 获取已分配授信明细
     */
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByRatCode(String ratCode, String itemFlag, String itemCode) {
        return disDetailDao.getCpRationDisDtlByRatCode(ratCode, itemFlag, itemCode);
    }
    /**
     * 获取内部授信自动分配明细
     */
    public RatBkrationCpAddDtlDTO getRationDisDtl(String ratCode, String itemFlag, String itemCode) {
        return disDetailDao.getCpRationDisDtl(ratCode, itemFlag, itemCode);
    }
    /**
     * 查询授信追加或追减剩余的额度
     */
    public BigDecimal getRationItemAddMoney(RatBkrationMoneyBean addDtlDTO, String flag) {
        String corpId = addDtlDTO.getCorpId();
        String addFlag = flag;
        if (RationConstant.RAT_ADD_FLAG_COMPANY.equals(flag)) {
            corpId = "";// 只查询追加在途的额度
            addFlag = "";
        }
        List<RatBkRationAddDtlVm> list = addDetailDao.getBkrationAddDtlByRatCode(addDtlDTO.getRatCode(), corpId,
                addDtlDTO.getItemFlag(), addDtlDTO.getItemCode(), addFlag);// 获取追加明细
        if (!EmptyUtils.isEmptyList(list)) {
            for (RatBkRationAddDtlVm dtlDTO : list) {
                if (addDtlDTO.getItemCode().equals(dtlDTO.getItemCode())) {
                    // TODO 内部授信的剩余额度
                    if (RationConstant.RAT_ADD_FLAG_COMPANY.equals(dtlDTO.getFlag())) {
                        if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) < 0) {// 追加
                            addDtlDTO.setUnUseMoney(addDtlDTO.getUnUseMoney().add(dtlDTO.getMoney()));// 网点剩余的额度
                            addDtlDTO.setUnUseMaxMoney(addDtlDTO.getUnUseMaxMoney().add(dtlDTO.getMaxMoney()));
                        }
                    }
                }
            }
        }
        return null;
    }
    /**
     * 计算分配额度
     */
    public BigDecimal getDisMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 分配额度=分配+被调剂-调剂
        return dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney());
    }
    public BigDecimal getLeftMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 已分配额度+被调剂-调剂-冻结-占用
        return dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())// 被调剂
                .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                .subtract(dtlDTO.getSuperProcessMoney());
    }
    /**
     * 内部授信审批通过后直接存入分配表和分配明细表
     */
    public void processAutoSaveDis(RatBkrationPageVO bkrationDTO, ICurrentUser user) {
        RatBkrationDis dis = new RatBkrationDis();
        BeanUtils.copyProperties(bkrationDTO, dis, "id");
        dis.setRatCode(bkrationDTO.getId());
        dis.setDisCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_DISCODE,
                RationConstant.RAT_CPRATION_DIS_CODE));// 授信分配编号
        dis.setRatCorpId(bkrationDTO.getCorpId());
        dis.setStatus(RationConstant.STATUS_CHECK);
        dis.setDistributeWay(RationConstant.DIS_WAY_AUTO);// 设置内部授信分配方式为自动分配
        dis.setCreateBy(user.getId());
        dis.setCreateDate(DateUtils.getDate());
        super.saveEntity(dis);
        List<RatBkrationDisDetail> disDetails = new ArrayList<RatBkrationDisDetail>();
        disDetails = BeanUtils.copyListProperties(bkrationDTO.getSingRationDtls(), RatBkrationDisDetail.class);
        for (RatBkrationDisDetail disDetail : disDetails) {
            disDetail.setDisCode(dis.getId());
            disDetail.setId(null);
            disDetailService.saveRatBkrationDisDetail(disDetail);
        }
    }
    /**
     * 保存授信分配
     */
    public void tempOrSubmit(RatBkrationDisListDTO disDTO, ICurrentUser user, String saveType) {
        RatBkrationDis rationDis = new RatBkrationDis();
        Integer messageType = null;// 操作类型
        String msg = null;// 操作消息
        if (!StringUtils.isEmpty(disDTO.getId())) {
            rationDis = get(disDTO.getId());
        }
        BeanUtils.copyProperties(disDTO, rationDis);
        if (!StringUtils.isEmpty(disDTO.getId())) {// 修改数据
            rationDis.setUpdateBy(user.getId());
            rationDis.setUpdateDate(DateUtils.getDate());
            if (saveType.equals("0")) {
                rationDis.setStatus(RationConstant.STATUS_TEMP);
                msg = RationConstant.MSGA_MODIFY + "";// 修改暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
            } else if (saveType.equals("1")) {
                rationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
                msg = RationConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
            }
        } else {// 新增数据
            checkRatCpRationForDis(disDTO);
            rationDis.setDisCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_DISCODE,
                    RationConstant.RAT_CPRATION_DIS_CODE));// 授信分配编号
            rationDis.setCreateBy(user.getId());
            rationDis.setCreateDate(DateUtils.getDate());
            if (saveType.equals("0")) {
                rationDis.setStatus(RationConstant.STATUS_TEMP);
                msg = RationConstant.MSGA_INPUT + "";// 录入暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
            } else if (saveType.equals("1")) {
                rationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
                msg = RationConstant.MSGA_INPUT_SUBMIT;// 录入提交
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
            }
        }
        rationDis.setDistributeWay(RationConstant.DIS_WAY_MANUAL);// 设置内部授信分配方式为手动分配
        rationDis.setCorpId(disDTO.getCorpId());
        CorpDTO corp = corpService.get(disDTO.getCorpId());
        rationDis.setCorpName(corp.getName());
        rationDis.setMoney(disDTO.getMoneysum());// 授信合计金额
        ratCprationDisDao.saveOrUpdate(rationDis);// 保存分配主表
        this.getSession().flush();
        // 保存到分配历史明细表
        List<RatDisDetailHis> detailsHis = new ArrayList<RatDisDetailHis>();
        detailsHis = BeanUtils.copyListProperties(disDTO.getRationDtls(), RatDisDetailHis.class);
        BeanUtils.copyProperties(disDTO.getRationDtls(), detailsHis);
        if (!StringUtils.isEmpty(disDTO.getId())) {// 修改数据状态，更新历史分配明细表
            ratDisHisService.deleteDisDtlHisByDisCode(disDTO);// 保存授信分配历史明细前先将原历史授信明细删除
            for (RatDisDetailHis disHis : detailsHis) {
                disHis.setDisCode(rationDis.getId());// 分配主表ID
                ratDisHisService.saveDisDetailHis(disHis);
                this.getSession().flush();
            }
        } else {// 新增分配数据,存入到历史分配明细表
            for (RatDisDetailHis disHis : detailsHis) {
                disHis.setDisCode(rationDis.getId());// 分配主表ID
                ratDisHisService.saveDisDetailHis(disHis);
                this.getSession().flush();
            }
        }
        ratLogService.saveRatProcessMessge(rationDis.getId(), rationDis.getMoney(), messageType, msg, user);
        // 挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(rationDis.getStatus())) {
             getStartHandler().handler(RatWorkFlowConstant.RAT_CPRATION_DIS, rationDis.getId(), rationDis,
                    rationDis.getRatCorpId(), user.getId());
        }
    }
    /**
     * 针对同一单位新增内部授信分配时更新内部授信分配明细
     */
    public void saveDisDetail(RatBkrationDisListDTO disDTO, String disCode) {
        List<RatBkrationDisDtlDTO> listDtls = ratCprationDisDao.queryDisDetailByDisCode(disCode);// 从数据库查出来的已分配明细
        List<RatBkrationDisDetail> listdetails = BeanUtils.copyListProperties(listDtls, RatBkrationDisDetail.class);
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();// 当前审批的分配明细
        List<RatBkrationDisDetail> details = new ArrayList<RatBkrationDisDetail>();
        details = BeanUtils.copyListProperties(rationDtls, RatBkrationDisDetail.class);
        for (RatBkrationDisDetail dtlDTO1 : listdetails) {// 更新内部授信分配明细表的分配金额，在已分配明细的基础上加上当前审批的明细
            for (RatBkrationDisDetail dtlDTO2 : details) {
                if (dtlDTO1.getItemCode().equals(dtlDTO2.getItemCode())) {
                    dtlDTO1.setMoney(dtlDTO1.getMoney().add(dtlDTO2.getMoney()));
                }
            }
        }
        disDetailService.deleteBkrationDisDtlByDisCode(disDTO);// 在保存之前先删除之前的明细
        // 保存分配明细
        for (RatBkrationDisDetail disDetail : listdetails) {
            disDetail.setMoney(disDetail.getMoney());
            disDetailService.saveRatBkrationDisDetail(disDetail);
        }
    }
    /**
     * 查看已分配信息列表
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput queryRationDisData(DataTablesInput input, String userId) {
        DataTablesOutput<RatBkrationDisVO> output = ratCprationDisDao.queryRationDisData(input);
        List<RatBkrationDisVO> data = output.getData();
        if (null == data || data.isEmpty()) {
            return output;
        }
        List<String> disCodes = new ArrayList<String>();
        for (RatBkrationDisVO bkrationVO : data) {
            disCodes.add(bkrationVO.getId());
        }
        List<RatBkRationDtlVm> dtlDTOs = ratDisHisService.getCprationDtlByDisCodes(disCodes);
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (null != dtlDTOs) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.get(dtlDTO.getRatCode());
                if (null == ratList) {
                    ratList = new ArrayList<RatBkRationDtlVm>();
                    ratMap.put(dtlDTO.getRatCode(), ratList);
                }
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationDisVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationDtlVm> ratList = ratMap.get(ratCode);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            if (EmptyUtils.isEmptyList(ratList)) {
                continue;
            }
            for (RatBkRationDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                }
                if(String.valueOf(dtlDTO.getSuperByReliefMoney()).equals("null")){
                    dtlDTO.setSuperByReliefMoney(BigDecimal.ZERO);
                }
                if(String.valueOf(dtlDTO.getSuperReliefMoney()).equals("null")){
                    dtlDTO.setSuperReliefMoney(BigDecimal.ZERO);
                }
                // 分配显示额度 = 分配额度+被调剂额度-调剂额度
                dtlDTO.setMoney(
                        dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }
    /**
     * 根据id获取授信分配数据
     */
    public RatBkrationPageVO queryRatRationDisInfo(String id) {
        RatBkrationDis rationDis = get(id);// 根据id获取分配主表数据
        String ratCode = rationDis.getRatCode();// 授信编号
        RatBkrationPageVO pageVO = ratCprationService.queryCprationById(ratCode);// 授信录入明细
        pageVO.setViewFlag(true);
        getRationLeftMoney(pageVO);// 计算剩余分配额度
        setRationDisDetailMoney(pageVO, rationDis);
        return pageVO;
    }
    /**
     * 设置原分配信息
     */
    private void setRationDisDetailMoney(RatBkrationPageVO pageVO, RatBkrationDis rationDis) {
        // 比较原录入信息与修改的分配数据
        pageVO.setDisCode(rationDis.getId());// 分配ID
        pageVO.setDisWay(rationDis.getDistributeWay());// 分配方式
        pageVO.setDisCorpId(rationDis.getCorpId());// 二次分配对象单位
        List<String> disCodes = new ArrayList<String>();
        disCodes.add(rationDis.getId());
        List<RatBkRationDtlVm> dtlDTOs = ratDisHisService.getCprationDtlByDisCodes(disCodes);
        Map<String, RatBkRationDtlVm> dtlMap = Maps.uniqueIndex(dtlDTOs, new Function<RatBkRationDtlVm, String>() {
            public String apply(RatBkRationDtlVm input) {
                return input.getItemCode();
            }
        });
        BigDecimal money = BigDecimal.ZERO;
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationDtlVm disDTO = dtlMap.get(itemCode);
            if (null != disDTO) {
                dtlDTO.setDisMoney(
                        disDTO.getMoney().add(Optional.ofNullable(disDTO.getSuperByReliefMoney()).orElse(BigDecimal.ZERO))
                                .subtract(Optional.ofNullable( disDTO.getSuperReliefMoney()).orElse(BigDecimal.ZERO)));// 已分配金额
            } else {
                dtlDTO.setDisMoney(BigDecimal.ZERO);// 已分配金额
            }
            money = money.add(dtlDTO.getDisMoney());
        }
        pageVO.setDisMoney(money);
    }
    /**
     * 内部授信审批列表数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_CPRATION_DIS,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratCprationDisDao.queryRationDisApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 检查同单位是否已分配未审批
     */
    public void checkRatCpRationForDis(RatBkrationDisListDTO disDTO) {
        List<RatBkrationDisListDTO> list = ratCprationDisDao.getRatCpRationDisList(disDTO);
        if (list.size() <= 0) {
            return;
        } else {
            CorpDTO corpDTO = corpService.get(disDTO.getCorpId());
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_EXIST, corpDTO.getName());
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 根据网点和输入条件查询已审批通过的内部授信分配数据列表
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput queryCpRationDisData(DataTablesInput input, String corpId, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rd.corp_Id", true);
        DataTablesOutput output = ratCprationDisDao.queryCpRationDisData(input, corpId, corpAuthSql);
        disDetailService.setDisOutputPageData(output);
        return output;
    }
    /**
     * 查询某条内部授信分配详情
     */
    public RatBkrationPageVO queryPassRationDisData(String ratCode, String corpId) {
        RatBkrationPageVO pageVO = ratCprationService.queryCprationById(ratCode);// 授信录入明细
        getRationLeftMoney(pageVO);// 计算剩余分配额度
        // setRationDisDetailMoney(pageVO, rationDis);
        return pageVO;
    }
    /**
     * 审批完成
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        RatBkrationDis ratCpDis = this.get(businessKey,true);
        boolean result = workFlowResult.isPass();
        Integer messageType = null;// 操作类型
        String msg = null;// 操作消息
        if (result) {// 审批通过
            RatBkrationDisListDTO disDTO = new RatBkrationDisListDTO();
            BeanUtils.copyProperties(ratCpDis, disDTO);
            List<String> ratCodes = new ArrayList<String>();
            ratCodes.add(ratCpDis.getRatCode());
            // 根据授信编号和单位id获取单位已分配的授信明细集合
            List<RatBkrationDisDtlDTO> dtlDTOs = ratDisHisService.queryDisDetailsPass(ratCodes, ratCpDis.getCorpId());
            // 获取当前审批的分配明细数据
            List<RatBkrationDisDetail> currentCheck = ratDetailHisDao.queryDisDetailByStatus(ratCpDis.getId(),
                    RationConstant.STATUS_WAIT_CHECK);
            List<RatBkrationDisDtlDTO> detailsHis = new ArrayList<RatBkrationDisDtlDTO>();
            detailsHis = BeanUtils.copyListProperties(currentCheck, RatBkrationDisDtlDTO.class);
            if (dtlDTOs.size() > 0) {// 表示该单位非第一次分配
                ratCpDis.setStatus(RationConstant.STATUS_CHECKED);// 设置该笔分配数据的状态为审批通过
                disDTO.setRationDtls(detailsHis);
                disDTO.setId(dtlDTOs.get(0).getDisCode());// 将id设置成第一次分配时的id
                saveDisDetail(disDTO, dtlDTOs.get(0).getDisCode()); // 更新授信分配明细
            } else {// 表示对单位第一次进行授信分配
                ratCpDis.setStatus(RationConstant.STATUS_CHECK);// 设置该笔分配数据的状态为审批通过
                // 保存分配明细
                for (RatBkrationDisDetail disDetail : currentCheck) {
                    disDetailService.saveRatBkrationDisDetail(disDetail);
                }
                disDTO.setRationDtls(detailsHis);
            }
            updateCpRationDisMoney(disDTO);// 更细分配表中自动分配额度
            // ratCprationService.updateRationDisMoney(disDTO);//将分配金额更新到授信录入明细表
            messageType = RationConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            msg = RationConstant.MSGA_CHECKOK + "";
            // 审批通过时,发出通知告知已审批通过
            notificationService.send(ratCpDis.getId(), ratCpDis.getCorpId(), NotificationConstant.CMS.CPRATION_DIS_PASS,
                    ratCpDis);
        } else {
            ratCpDis.setStatus(RationConstant.STATUS_BACK);
            messageType = RationConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            msg = RationConstant.MSGA_CHECKBACK;
            // 审批打回时,发出通知告知被审批打回
            notificationService.send(ratCpDis.getId(), ratCpDis.getCorpId(), NotificationConstant.CMS.CPRATION_DIS_BACK,
                    ratCpDis);
        }
        super.updateEntity(ratCpDis);
        // 记录到日志表
        ratLogService.saveRatProcessMessge(ratCpDis.getId(), ratCpDis.getMoney(), messageType, msg, user);
    }
    /**
     * 功能介绍 : 获取集团授信的分配及剩余额度。<br>
     * <b>日期：</b>2017年9月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationMoneyBean getRationItemDisMoney(String disId, String ratCode, String itemFlag, String itemCode) {
        RatBkrationMoneyBean addDtlDTO = new RatBkrationMoneyBean();
        addDtlDTO.setRatCode(ratCode);
        addDtlDTO.setItemCode(itemCode);
        addDtlDTO.setItemFlag(itemFlag);
        List<RatBkrationCpAddDtlDTO> disList = getRationDisDtlByItemCode(disId, itemFlag, itemCode);
        BigDecimal disMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal disOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal leftOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMaxMoney = BigDecimal.ZERO;// 分配剩余最高额度
        // 初始化Zero
        if (!EmptyUtils.isEmptyList(disList)) {// 没有总额共享的
            // 循环计算已分配额度
            for (RatBkrationCpAddDtlDTO dtlDTO : disList) {
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    // 已分配额度-已占用-冻结-虚拟占用额度-虚拟占用
                    disMoney = disMoney.add(getDisMoney(dtlDTO));// 分配额度
                    leftMoney = leftMoney.add(getLeftMoney(dtlDTO));// 剩余额度
                }
            }
        }
        addDtlDTO.setDisMoney(disMoney);
        addDtlDTO.setDisOpenMoney(disOpenMoney);
        addDtlDTO.setLeftMoney(leftMoney);
        addDtlDTO.setLeftOpenMoney(leftOpenMoney);
        addDtlDTO.setLeftMaxMoney(leftMaxMoney);
        getRationItemLeftMoney(addDtlDTO);
        RatBkrationDis dis = get(disId, true);
        addDtlDTO.setCorpId(dis.getCorpId());
        getRationItemAddMoney(addDtlDTO, RationConstant.RAT_ADD_FLAG_COMPANY);// 集团追加
        // TODO 查看该品种的剩余额度
        getBankRationUnseMoney(addDtlDTO);
        return addDtlDTO;
    }
    /**
     * 获取授信品种未分配的剩余额度
     */
    public void getBankRationUnseMoney(RatBkrationMoneyBean addDtlDTO) {
        List<RatBkRationAddDtlVm> list = addDetailDao.getBkrationAddDtlByRatCode(addDtlDTO.getRatCode(), null,
                addDtlDTO.getItemFlag(), addDtlDTO.getItemCode(), RationConstant.RAT_ADD_FLAG_COMPANY);
        if (!EmptyUtils.isEmptyList(list)) {
            for (RatBkRationAddDtlVm dtlDTO : list) {
                if (addDtlDTO.getItemCode().equals(dtlDTO.getItemCode())) {
                    // TODO 银行追减和集团追加在途的额度= 银行授信的剩余额度
                    if (RationConstant.RAT_ADD_FLAG_COMPANY.equals(dtlDTO.getFlag())) {
                        if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) < 0) {
                            addDtlDTO.setUnUseMoney(addDtlDTO.getUnUseMoney().add(dtlDTO.getMoney()));// 网点剩余的额度
                        }
                    }
                }
            }
        }
    }
    /**
     * 查询授信分配剩余额度
     */
    // TODO 修改为根据分配ID查询授信品种的可用额度
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByItemCode(String disId, String itemFlag, String itemCode) {
        return disDetailDao.getRationDisDtlByDisDtlId(disId, itemFlag, itemCode);
    }
    /**
     * 获取授信分配数据（占用时的数据查询）
     */
    public RatBkrationDisVO queryRatCpRationDisInfo(String id) {
        RatBkrationDisVO pageVO = ratCprationDisDao.queryCpRationDisDataById(id);// 根据id获取授信分配信息
        setInitCpRationDisMoney(pageVO, id);
        return pageVO;
    }
    /**
     * 初始化分配明细数据
     */
    public void setInitCpRationDisMoney(RatBkrationDisVO pageVO, String id) {
        List<String> disCodes = new ArrayList<String>();
        List<String> ratCodes = new ArrayList<String>();
        ratCodes.add(pageVO.getRatId());
        List<RatBkRationDtlVm> ratDisDtls = new ArrayList<RatBkRationDtlVm>();
        disCodes.add(pageVO.getDisCode());
        List<RatBkrationDisDtlDTO> dtlDTOs = null;
        List<RatBkRationDtlVm> ratDtl = null;
        if (pageVO.getDistributeWay().equals(RationConstant.RAT_DISWAY_5)) {// 自动分配时
            dtlDTOs = disDetailService.getBkrationDisDtlByRatCodes(disCodes);// 分配明细
            ratDtl = ratDetailDao.getBkrationDtlByRatCodes(ratCodes);// 录入明细
            for (RatBkRationDtlVm cpRatDtl : ratDtl) {
                for (RatBkrationDisDtlDTO cpDisDtl : dtlDTOs) {
                    if (cpRatDtl.getItemCode().equals(cpDisDtl.getItemCode())) {
                        // 总额度=分配+调剂-被调剂
                        cpRatDtl.setMoney(cpDisDtl.getMoney().add(cpDisDtl.getSuperByReliefMoney())
                                .subtract(cpDisDtl.getSuperReliefMoney()));
                        // 剩余=总额度-占用-在审
                        cpRatDtl.setLeftMoney(cpRatDtl.getMoney().subtract(cpDisDtl.getSuperProcessMoney())
                                .subtract(cpDisDtl.getFreezeMoney()));
                        ratDisDtls.add(cpRatDtl);
                    }
                }
            }
            pageVO.setRationDtls(ratDisDtls);
        } else {
            ratDtl = disDetailService.getBkrationDtlByRatCodes(disCodes);
            pageVO.setRationDtls(ratDtl);
        }
    }
    /**
     * 获取内部授信分配明细
     */
    public RatBkrationDisListDTO geCprationDisListDTOById(String ratCode, String corpId) {
        RatBkrationDisListDTO listDTO = null;
        List<RatBkrationDis> disList = ratCprationDisDao.getRatCpDisByRatCode(ratCode, corpId,
                RationConstant.DIS_WAY_AUTO);
        if (null != disList && !disList.isEmpty()) {
            listDTO = new RatBkrationDisListDTO();
            RatBkrationDis dis = disList.get(0);
            BeanUtils.copyProperties(dis, listDTO);
            // 设置分配明细
            List<RatBkrationDisDtlDTO> dtlDTOs = new ArrayList<RatBkrationDisDtlDTO>();
            List<String> ratCodes = new ArrayList<String>();
            ratCodes.add(dis.getId());
            dtlDTOs = disDetailService.getBkrationDisDtlByRatCodes(ratCodes);
            listDTO.setRationDtls(dtlDTOs);
        }
        return listDTO;
    }
    /**
     * 更新分配明细表中的自动分配金额
     */
    public void updateCpRationDisMoney(RatBkrationDisListDTO disDTO) {
        List<String> disCodes = new ArrayList<String>();
        RatBkrationDis rationDis = new RatBkrationDis();
        // 获取自动分配的主表数据
        List<RatBkrationDis> disList = ratCprationDisDao.getRatCpDisByRatCode(disDTO.getRatCode(),
                disDTO.getRatCorpId(), RationConstant.DIS_WAY_AUTO);
        List<RatBkrationDisDtlDTO> newDis = disDTO.getRationDtls();// 最新分配明细
        Map<String, RatBkrationDisDtlDTO> dtlMap = Maps.uniqueIndex(newDis,
                new Function<RatBkrationDisDtlDTO, String>() {
                    public String apply(RatBkrationDisDtlDTO input) {
                        return input.getItemCode();
                    }
                });
        if (disList.size() > 0) {
            rationDis = disList.get(0);
            BigDecimal disMoney = BigDecimal.ZERO;
            disCodes.add(rationDis.getId());
            List<RatBkrationDisDtlDTO> disDtls = new ArrayList<RatBkrationDisDtlDTO>();
            List<RatBkrationDisDtlDTO> autoDisDtls = disDetailService.getBkrationDisDtlByRatCodes(disCodes);// 获取自动分配明细数据
            for (RatBkrationDisDtlDTO disDtlDTO : autoDisDtls) {
                String itemCode = disDtlDTO.getItemCode();// 授信品种
                RatBkrationDisDtlDTO newDisDTO = dtlMap.get(itemCode);
                disDtlDTO.setMoney(disDtlDTO.getMoney().subtract(newDisDTO.getMoney()));// 在原分配的基础上减去分配给其他单位的金额
                disMoney = disMoney.add(disDtlDTO.getMoney());
                disDtls.add(disDtlDTO);
            }
            RatBkrationDisListDTO disListDTO = new RatBkrationDisListDTO();
            disListDTO.setRationDtls(disDtls);
            disListDTO.setId(rationDis.getId());
            // 更新内部授信自动分配明细
            List<RatBkrationDisDetail> listdetails = BeanUtils.copyListProperties(disDtls, RatBkrationDisDetail.class);
            disDetailService.deleteBkrationDisDtlByDisCode(disListDTO);// 在保存之前先删除之前的明细
            for (RatBkrationDisDetail disDetail : listdetails) {// 保存分配明细
                disDetail.setMoney(disDetail.getMoney());
                disDetailService.saveRatBkrationDisDetail(disDetail);
            }
            rationDis.setMoney(disMoney);
        }
        ratCprationDisDao.saveOrUpdate(rationDis);// 保存分配主表
    }
    @Override
    @XxlJob("RatCprationDisService")
    public void run(String arguments) {
        // TODO Auto-generated method stub
    }
    @Override
    public BaseDao<RatBkrationDis, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratCprationDisDao;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 内部授信分配在途数据
            int dataSize = ratCprationDisDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_CPRATIONDIS_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
