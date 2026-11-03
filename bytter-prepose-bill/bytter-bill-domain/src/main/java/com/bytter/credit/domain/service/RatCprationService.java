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
import com.bytter.credit.domain.dao.RatBkrationDetailDao;
import com.bytter.credit.domain.dao.RatCprationDao;
import com.bytter.credit.domain.dao.RatCprationDisDao;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
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
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCprationAddService
 * @Description:
 * @date 2018年11月13日 下午5:42:23
 */
@Service("RatCprationService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_CPRATION })
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_CPRATION )
public class RatCprationService extends BaseService<RatBkration, String>
        implements IWorkFlowListener, ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private RatCprationDao ratCprationDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private RatBkrationDetailService detailService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtBankService btBankService;
    @Autowired
    private RatBkrationDetailDao ratBkrationDetailDao;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private RatBkrationDetailDao detailDao;
    @Autowired
    private RatCprationLogService ratLogService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private RatCprationDisService ratCpDisService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private RatCprationDisDao ratCprationDisDao;
    /**
     * 查询内部授信数据列表
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
    public DataTablesOutput queryRatCprationData(DataTablesInput input,String resource, String userId) {
        // 添加数据权限
        List<String> corpIdList = corpService.getUserCorpIds(resource, userId);
        DataTablesOutput output = ratCprationDao.listOutputData(input, corpIdList);
        return setOutputPageData(output);
    }
    /**
     * 设置单品种明细
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput setOutputPageData(DataTablesOutput<RatBkrationVO> output) {
        List<String> ratCodes = new ArrayList<String>();
        List<RatBkrationVO> data = output.getData();
        if (null == data) {
            return output;
        }
        Date nowDate = DateUtils.getDate();// 当前日期
        for (RatBkrationVO bkrationVO : data) {
            if (DateUtils.dayDiffs(bkrationVO.getEndDate(), nowDate) < 0) {
                bkrationVO.setOverdueSign(RationConstant.RAT_COMMON_Y);// 结束日期小于当前日期标识过期
            }
            ratCodes.add(bkrationVO.getId());
        }
        // 设置明细集合
        List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatCodes(ratCodes);
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
        for (RatBkrationVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationDtlVm> ratList = ratMap.get(ratCode);
            // 设置品种单项和综合条数
            setRationDtls(ratList);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            if (!EmptyUtils.isEmptyList(ratList)) {
                for (RatBkRationDtlVm dtlDTO : ratList) {
                    dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney()));
                    dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperAddOpenMoney()));
                    if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                        singItemSize++;
                    }
                }
                bkrationVO.setItemSize(ratList.size());
            }
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }
    /**
     * 按照品种类型和品种编号排序
     */
    private void setRationDtls(List<RatBkRationDtlVm> ratList) {
        if (EmptyUtils.isEmptyList(ratList)) {
            return;
        }
        // 按照单品种排序
        Collections.sort(ratList, new Comparator<RatBkRationDtlVm>() {
            public int compare(RatBkRationDtlVm o1, RatBkRationDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        int size = 1;
        for (RatBkRationDtlVm addDtlDTO : ratList) {
            addDtlDTO.setItemOrder(size++);
        }
    }
    /**
     * 检查合同号是否重复
     *
     * @author luxy
     * @param contractNo
     *            合同号
     * @return true-不存在相同的合同号 false-存在相同合同号
     */
    public boolean checkContractNo(String contractNo) {
        List<RatBkration> list = ratCprationDao.checkContractNo(contractNo);
        return list.size() <= 0;
    }
    /**
     * 保存授信信息
     *
     * @author luxy
     * @param saveType
     *            保存标志：0-暂存 1-提交
     */
    public void tempOrSubmit(RatBkRationVm cprationDTO, String saveType, ICurrentUser user) {
        /**
         * 额度包含或者交叉允许选择 先判断是否允许交叉
         */
        ConfigDTO configDTO = getRatContains();
        // 不允许则出现交叉或包含都不能保存
        if (RationConstant.RAT_CONTAIN_0.equals(configDTO.getValue())) {
            checkContianRatBankRation(cprationDTO, "1");
        }
        String msg = null;
        Integer messageType = null;
        Integer type = Integer.valueOf(saveType);// 保存类型
        RatBkration cpRation = null;
        if (!cprationDTO.isUpdateSign()) {// 新增数据
            if (type.equals(RationConstant.STATUS_TEMP)) {
                cprationDTO.setStatus(RationConstant.STATUS_TEMP);// 设置状态为暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
                msg = RationConstant.MSGA_INPUT + "";// 录入暂存
            } else {
                cprationDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);// 设置状态为待审批
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
                msg = RationConstant.MSGA_INPUT_SUBMIT;// 录入提交
            }
            cpRation = new RatBkration();
            BeanUtils.copyNotNullProperties(cprationDTO, cpRation);
            CorpDTO corpDTO = corpService.get(cprationDTO.getCorpId());
            if (null != corpDTO) {// 单位不存在则过滤掉单位信息
                cpRation.setCorpName(corpDTO.getName());
                cpRation.setCorpNo(corpDTO.getCode());
                if (CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())) {
                    cpRation.setNetCode(corpDTO.getId());
                } else {
                    CorpDTO netDTO = corpService.getNetByCorpId(cprationDTO.getCorpId());
                    if (null != netDTO) {// 没有直属网点则不设置网点
                        cpRation.setNetCode(netDTO.getId());
                    }
                }
            }
            cpRation.setRatCode(getRatCodeNo(SequenceEnum.RAT_CPRATION_CODE, RationConstant.RAT_CPRATION_CODE));// 授信编号
            cpRation.setMoney(cprationDTO.getMoneysum());
            cpRation.setCreateBy(user.getId());
            cpRation.setCreateDate(DateUtils.getDate());
            saveOrUpdate(cpRation);
            this.getEntityDao().flush();
            // 保存授信明细
            List<RatBkrationDetail> details = BeanUtils.copyListProperties(cprationDTO.getRationDtls(),
                    RatBkrationDetail.class);
            for (RatBkrationDetail detail : details) {
                detail.setRatCode(cpRation.getId());
                detailService.saveRatBkrationDetail(detail);
            }
        } else if (cprationDTO.isUpdateSign()) {// 修改数据
            if (type.equals(RationConstant.STATUS_TEMP)) {
                cprationDTO.setStatus(RationConstant.STATUS_TEMP);// 设置状态为暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
                msg = RationConstant.MSGA_MODIFY + "";// 修改暂存
            } else {
                cprationDTO.setStatus(RationConstant.STATUS_WAIT_CHECK);// 设置状态为待审批
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
                msg = RationConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            }
            cpRation = get(cprationDTO.getId(), true);
            BeanUtils.copyProperties(cprationDTO, cpRation, "id", "ratCode", "createDate", "createBy");
            cpRation.setMoney(cprationDTO.getMoneysum());
            cpRation.setUpdateBy(user.getUsername());
            cpRation.setUpdateDate(DateUtils.getDate());
            saveOrUpdate(cpRation);
            this.getEntityDao().flush();
            // 先删除授信明细，再保存修改后的明细
            detailService.deleteBkrationDtlByRatCode(cprationDTO);
            List<RatBkrationDetail> details = new ArrayList<RatBkrationDetail>();
            details = BeanUtils.copyListProperties(cprationDTO.getRationDtls(), RatBkrationDetail.class);
            for (RatBkrationDetail detail : details) {
                detail.setRatCode(cpRation.getId());
                detailService.saveRatBkrationDetail(detail);
            }
        }
        /**
         * 合同挂接
         */
        flowStartConHandle(cpRation, user);
        // 记录到日志表
        ratLogService.saveRatProcessMessge(cpRation.getId(), cpRation.getMoney(), messageType, msg, user);
        // todo:审批调用
        if (RationConstant.STATUS_WAIT_CHECK.equals(cpRation.getStatus())) {
             getStartHandler().handler(RatWorkFlowConstant.RAT_CPRATION, cpRation.getId(), cpRation,
                    cpRation.getCorpId(), user.getId());
            // 完成通知提醒
            notificationService.complete(NotificationConstant.CMS.RAT_CPRATION_RATION, cpRation.getId(), user.getId());
        }
    }
    /**
     * 生成授信编号
     *
     * @author luxy
     */
    protected String getRatCodeNo(SequenceEnum senum, String prefix) {
        return SequenceSingleton.getSequence(senum, prefix);
    }
    /**
     * 功能介绍 : 额度包含或者交叉允许选择
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatContains() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RATCONTAINS);
    }
    /**
     * 获取是否对接合同管理配置
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelateCon() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RELATECON);
    }
    /**
     * 对接合同管理模式配置
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatConType() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RATCONTYPE);
    }
    /**
     * 检查授信是否包含
     */
    public void checkContianRatBankRation(RatBkRationVm bkrationDTO, String checkType) {
        List<RatBkRationVm> dtos = getRatBankRationListByCorpId(bkrationDTO);
        if (null == dtos || dtos.isEmpty()) {
            return;
        }
        for (RatBkRationVm dto : dtos) {
            compareDateRelation(bkrationDTO, dto, checkType);
        }
    }
    /**
     * 比较授信品种日期关系
     *
     * @param
     * @return
     */
    private boolean compareDateRelation(RatBkRationVm dest, RatBkRationVm source, String checkType) {
        Date destStartDate = dest.getBegindate();// 起始日期
        Date destEndDate = dest.getEnddate();// 结束日期
        Date sourceBeginDate = source.getBegindate();// 已保存的起始日期
        Date sourceEndDate = source.getEnddate();// 已保存的结束日期
        if ("2".equals(checkType)) {
            // 时间有交叉,输入的开始时间大于 以前的开始时间 且小于以前的结束时间且 现在的结束大于以前的结束时间
            if (destStartDate.getTime() > sourceBeginDate.getTime() && destStartDate.getTime() < sourceEndDate.getTime()
                    && destEndDate.getTime() > sourceEndDate.getTime()) {
                // 更新原授信为已过期
                if (dest.isUpdateSign()) {
                    // invalidRatbkration(source);
                } else {
                    dest.setResultFlag("2");// 存在交叉的授信
                }
            }
            if (destStartDate.getTime() < sourceBeginDate.getTime() && destEndDate.getTime() > sourceBeginDate.getTime()
                    && destEndDate.getTime() < sourceEndDate.getTime()) {
                // 更新现在的授信为已过期 ，即为不能保存
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_BEFORE);
            }
        } else {
            if (destStartDate.getTime() > sourceBeginDate.getTime()) {
                if (destStartDate.getTime() <= sourceEndDate.getTime()) {
                    // 包含或交叉，不能提交
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
                }
            } else if (destStartDate.getTime() == sourceBeginDate.getTime()) {
                // 不管后面时间都是包含或重复
                throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
            } else if (destStartDate.getTime() < sourceBeginDate.getTime()) {
                if (destEndDate.getTime() >= sourceBeginDate.getTime()) {// 包含或包含
                    throw new ParameterException(RatMessageEnum.CMS_RAT_ITEMDATE_CONTAIN);
                }
            }
        }
        return true;
    }
    /**
     * 功能介绍 : 获取单位已录入的同银行同币别的授信明细
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> getRatBankRationListByCorpId(RatBkRationVm bkrationDTO) {
        List<RatBkRationVm> dtos = ratCprationDao.getCorpBkrationListDTOs(bkrationDTO);
        List<RatBkRationDtlVm> dtlDTOs = detailDao.getCorpBkrationDtlDTOs(bkrationDTO);
        setRattionListDTO(dtos, dtlDTOs);
        return dtos;
    }
    /**
     * 分组设置授信录入明细数据
     *
     * @param
     * @return
     */
    public void setRattionListDTO(List<RatBkRationVm> dtos, List<RatBkRationDtlVm> dtlDTOs) {
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (dtlDTOs.size() > 0) {
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
        if (dtos.size() > 0) {
            for (RatBkRationVm dto : dtos) {
                String id = dto.getId();// 授信主键
                List<RatBkRationDtlVm> rationDtls = ratMap.get(id);
                if (null != rationDtls) {
                    dto.setRationDtls(rationDtls);// 授信明细
                }
            }
        }
    }
    /**
     * 根据授信单号查询内部授信
     */
    @Transactional(readOnly = true)
    public RatBkRationVm getCpRationByRatCode(String ratCode) {
        RatBkRationVm cprationDTO = new RatBkRationVm();
        RatBkration cpration = get(ratCode, true);
        BeanUtils.copyProperties(cpration, cprationDTO);
        if (StringUtils.isNotEmpty(cpration.getBankCode())) {
            BtBankDTO bankDTO = btBankService.getByBankCode(cpration.getBankCode());
            if (null != bankDTO) {
                cprationDTO.setBankName(bankDTO.getBankName());
            }
        }
        List<String> itemCodeList = new ArrayList<String>();// 品种集合
        List<RatBkRationDtlVm> rationDtls = detailService.getBkrationDtlDTOByRatCode(ratCode, null);
        for (RatBkRationDtlVm dtlDTO : rationDtls) {
            itemCodeList.add(dtlDTO.getItemCode());
        }
        cprationDTO.setRationDtls(rationDtls);
        cprationDTO.setItemSize(rationDtls.size());// 总品种数
        cprationDTO.setSingItemSize(cprationDTO.getItemSize());// 单品种数
        return cprationDTO;
    }
    /**
     * 删除授信明细
     */
    public void deleteCprationDtlByRatCode(RatBkRationVm cprationDTO) {
        ratBkrationDetailDao.deleteBkrationDtlByRatCode(cprationDTO);
    }
    /**
     * 根据id查询授信录入信息
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO queryCprationById(String id) {
        RatBkrationPageVO pageVO = getRatBkrationInfo(id);
        List<String> ratCodes = new ArrayList<String>();
        ratCodes.add(id);
        List<RatBkRationDtlVm> dtlDTOs = detailService.getBkrationDtlByRatCodes(ratCodes);// 根据授信编号获取授信明细
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            List<RatBkrationDisDtlDTO> dtos = disDetailService.getRationDisDtlByCorpItem(id, pageVO.getCorpId(),
                    dtlDTO.getItemFlag(), dtlDTO.getItemCode());// 某个品种授信分配明细
            if (dtos.size() > 0) {// 已分配
                RatBkrationDisDtlDTO disDTl = dtos.get(0);
                BigDecimal leftMoney = disDTl.getMoney().add(disDTl.getSuperByReliefMoney())
                        .subtract(disDTl.getSuperReliefMoney()).subtract(disDTl.getFreezeMoney())
                        .subtract(disDTl.getSuperProcessMoney());
                dtlDTO.setLeftMoney(leftMoney);
            } else {// 未分配(暂存状态)
                dtlDTO.setLeftMoney(dtlDTO.getMoney());
            }
            dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney()));// 录入+追加
        }
        List<RatBkRationDtlVm> singRationDtls = new ArrayList<RatBkRationDtlVm>();
        pageVO.setSingRationDtls(singRationDtls);// 单品种明细
        BigDecimal money = BigDecimal.ZERO;
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            money = money.add(dtlDTO.getMoney());
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                singRationDtls.add(dtlDTO);
            }
        }
        pageVO.setMoney(money);
        pageVO.setItemSize(dtlDTOs.size());
        pageVO.setSingItemSize(singRationDtls.size());// 单品种条数
        return pageVO;
    }
    /**
     * 获取内部授信录入数据
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO getRatBkrationInfo(String id) {
        return ratCprationDao.getCpRationById(id);
    }
    /**
     * 获取审批列表数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTable(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_CPRATION,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rb.id", true);
        }
        DataTablesOutput dp = ratCprationDao.getApproveDataTable(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 更新内部授信录入明细表中的分配金额
     */
    public void updateRationDisMoney(RatBkrationDisListDTO disDTO) {
        String ratCode = disDTO.getRatCode();// 授信录入ID
        List<RatBkrationDetail> bkrationDetails = detailService.getBkrationDetailByRatCode(ratCode);
        Map<String, RatBkrationDetail> dtlMap = new HashMap<String, RatBkrationDetail>();
        // 按授信品种分类
        for (RatBkrationDetail detail : bkrationDetails) {
            dtlMap.put(detail.getItemCode(), detail);
        }
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkrationDetail detail = dtlMap.get(itemCode);
            if (null == detail) {
                continue;
            }
            BigDecimal money = NumberUtil.formatNumericNull(detail.getSuperDistributeMoney());// 授信额度
            detail.setSuperDistributeMoney(money.add(dtlDTO.getMoney()));// 已分配授信金额
            detailService.updateEntity(detail);// 更新已分配金额
        }
    }
    /**
     * 获取相同单位同币别的其他已分配授信录入信息
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> queryCorpOtherRation(RatUrationBeanDTO uration) {
        return ratCprationDao.queryCorpOtherRation(uration);
    }
    /**
     * 根据授信额度单ID获取授信信息
     */
    @Transactional(readOnly = true)
    public List<RatBkRationVm> queryRationByRatCode(RatUrationBeanDTO uration) {
        return ratCprationDao.queryRationByRatCode(uration);
    }
    @Override
    public BaseDao<RatBkration, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratCprationDao;
    }
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
        getEntityDao().flush();
        RatBkration ration = this.get(businessKey,true);
        if (result) {
            ration.setStatus(RationConstant.STATUS_CHECK);
            messageType = RationConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            msg = RationConstant.MSGA_CHECKOK + "";
            RatBkrationPageVO ratCpration = queryCprationById(businessKey);// 查询录入明细
            ratCpDisService.processAutoSaveDis(ratCpration, user);// 审批通过后写入分配表和分配明细表
            // 将授信录入额度写入录入明细表的分配额度字段
            RatBkRationVm cprationDTO = new RatBkRationVm();
            cprationDTO.setId(businessKey);
            cprationDTO.setRationDtls(ratCpration.getSingRationDtls());
            // 先删除授信明细，再保存修改后的明细
            detailService.deleteBkrationDtlByRatCode(cprationDTO);
            List<RatBkrationDetail> details = new ArrayList<RatBkrationDetail>();
            details = BeanUtils.copyListProperties(cprationDTO.getRationDtls(), RatBkrationDetail.class);
            for (RatBkrationDetail detail : details) {
                detail.setRatCode(businessKey);
                detail.setSuperDistributeMoney(detail.getMoney());
                detail.setId(null);
                detailService.saveRatBkrationDetail(detail);
            }
            // 审批通过时,发出通知告知已审批通过
            notificationService.send(ration.getId(), ration.getCorpId(), NotificationConstant.CMS.CPRATION_RATION_PASS,
                    ration);
        } else {
            ration.setStatus(RationConstant.STATUS_BACK);
            messageType = RationConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            msg = RationConstant.MSGA_CHECKBACK;
            // 审批未通过时,发出待处理消息告知已打回
            notificationService.send(ration.getId(), ration.getCorpId(), NotificationConstant.CMS.CPRATION_RATION_BACK,
                    ration);
        }
        /**
         * 合同挂接处理
         */
        flowCompletedConHandle(ration, userService.getByUserId(ration.getCreateBy()), result);
        super.updateEntity(ration);
        // 记录到日志表
        ratLogService.saveRatProcessMessge(ration.getId(), ration.getMoney(), messageType, msg, user);
    }
    @Override
    @XxlJob("RatCprationService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_NOTIFYDAYS);// 授信到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询授信到期数据
        List<RatBkration> urations = ratCprationDao.queryCpRatList(endDate);
        if (!EmptyUtils.isEmptyList(urations)) {
            for (RatBkration ration : urations) {
                RatBkrationVO rationVo = new RatBkrationVO();
                BeanUtils.copyProperties(ration, rationVo);
                notificationService.send(ration.getId(), ration.getCorpId(), NotificationConstant.CMS.RAT_CPRAT_MATURE,
                        rationVo);
                logger.info("内部授信到期提醒，授信编号：" + ration.getRatCode() + "合同号：" + ration.getContractNo());
            }
        }
    }
    /**
     * 授信挂接合同处理
     *
     * @param ratBkration
     * @param currentUser
     */
    private void flowCompletedConHandle(RatBkration ratBkration, ICurrentUser currentUser, boolean result) {
        String relateCon = getRelateCon().getValue();
        String ratConType = getRatConType().getValue();
        // 判断是否挂接合同
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (RationConstant.RAT_CONFIG_RATCONTYPE_0.equals(ratConType)) {// 判断挂接合同类型
                if (result) {
                    RatBkrationVO ratBkrationVO = new RatBkrationVO();
                    String corpName = corpService.get(ratBkration.getCorpId()).getName();
                    ConInfoDTO conInfoDTO = new ConInfoDTO();
                    conInfoDTO.setCorpId(ratBkration.getCorpId());
                    conInfoDTO.setBusId(ratBkration.getId());
                    conInfoDTO.setConName(corpName + "内部授信合同");
                    conInfoDTO.setTypeId(ratBkration.getConTypeId());
                    conInfoDTO.setBeginDate(ratBkration.getBegindate());
                    conInfoDTO.setEndDate(ratBkration.getEnddate());
                    BeanUtils.copyProperties(ratBkration, ratBkrationVO);
                    ratBkrationVO.setBeginDate(ratBkration.getBegindate());
                    ratBkrationVO.setEndDate(ratBkration.getEnddate());
                    ratBkrationVO.setCurName(currencyService.getObjectById(ratBkration.getCurCode()).getCurName());
                    conInfoDTO.setOutBusData(ratBkrationVO);
                    conInfoDTO.setConNo(ratBkration.getContractNo());
                    // 授信生成合同
                    conCommonService.registerConOp(conInfoDTO, RationConstant.RAT_CONFIG_CONPDTYPE, currentUser);
                    ratBkration.setConId(conInfoDTO.getId());
                }
            } else {
                if (!result) {
                    conCommonService.unHookConOp(ratBkration.getConId(), ratBkration.getId(), currentUser);
                    ratBkration.setConId(null);
                    ratBkration.setConTypeId(null);
                }
            }
        }
    }
    private void flowStartConHandle(RatBkration ratBkration, ICurrentUser currentUser) {
        String relateCon = getRelateCon().getValue();
        String ratConType = getRatConType().getValue();
        // 判断是否挂接合同
        if (RationConstant.RAT_COMMON_Y.equals(relateCon)) {
            if (RationConstant.RAT_CONFIG_RATCONTYPE_1.equals(ratConType)) {// 判断挂接合同类型
                if (StringUtils.isEmpty(ratBkration.getConId())) {
                    conCommonService.hookConOp(ratBkration.getConId(), ratBkration.getId(), currentUser);
                }
            }
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratCprationDao.queryBusBillByCorpId(corpId, null);// 授信录入数据
            int disDataSize = ratCprationDisDao.queryBusBillByCorpId(corpId, null);// 分配数据
            if (dataSize > 0 || disDataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DEL_CPRATION_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratCprationDao.queryBusBillByCorpId(corpId, "-1,0,11");// 授信录入在途数据
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_CPRATION_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
