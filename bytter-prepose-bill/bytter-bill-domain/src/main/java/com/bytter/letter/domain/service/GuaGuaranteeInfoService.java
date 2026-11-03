package com.bytter.letter.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaLogService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaLogDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.constant.LetterWorkFlowConstant;
import com.bytter.letter.domain.dao.GuaGuaranteeInfoDao;
import com.bytter.letter.domain.dao.GuaPerformRegDao;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.entity.GuaPerformReg;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.letter.domain.vo.GuaPerformRegVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeInfoService
 * @Description:
 * @date 2019年1月7日 下午3:50:09
 */
@Service("GuaGuaranteeInfoService")
@Transactional
@WorkFlow(value = {LetterWorkFlowConstant.GUA_GUARANTEE_APPLY })
public class GuaGuaranteeInfoService extends BaseService<GuaGuaranteeInfo, String>
        implements IWorkFlowListener, ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private GuaGuaranteeInfoDao guaLgInfoDao;
    @Autowired
    GuaPerformRegDao guaPerformRegDao;
    @DubboReference
    private ICorpService corpService;
   @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference
    private INotificationService notificationService;
    @DubboReference
    private IConfigService configService;
    @Autowired
    @Qualifier("ratUrationServiceImpl")
    private IRatUrationService urationService;
    @Autowired
    @Qualifier("fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @DubboReference
    private IBtCurrencyService btCurrencyService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private IGuaLogService guaLogService;

    /**
     * 查询保函申请列表
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput LgDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_INFO_QUERY, user.getId());
        return guaLgInfoDao.LgDatatable(input, corpAuth);
    }
    /**
     * 查询保函待审批列表数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(readOnly = true)
    public DataTablesOutput getApproveDataTables(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LetterWorkFlowConstant.GUA_GUARANTEE_APPLY, input);
        String businessSql = null;
        if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        }
        DataTablesOutput dp = guaLgInfoDao.getApproveDataTables(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 保存保函申请信息
     */
    public void tempOrSubmit(GuaGuaranteeInfoVO LgInfoVo, RatUrationDTO urationDTO, ICurrentUser user) {
        GuaGuaranteeInfo LgInfo = null;
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        ConfigDTO bailConfig = getIsHangBail();// 获取参数设置是否挂接保证金
        if (StringUtils.isEmpty(LgInfoVo.getId())) {// 新增数据
            LgInfo = new GuaGuaranteeInfo();
            BeanUtils.copyNotNullProperties(LgInfoVo, LgInfo);
            if (LgInfoVo.getStatus().equals(LetterConstant.STATUS_TEMP)) {// 暂存
                LgInfo.setStatus(LetterConstant.STATUS_TEMP);
                msgType = LetterConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = LetterConstant.MSGA_INPUT + "";// 录入暂存
            } else {// 提交
                LgInfo.setStatus(LetterConstant.STATUS_WAIT_CHECK);
                msgType = LetterConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = LetterConstant.MSGA_INPUT_SUBMIT;// 录入提交
            }
            LgInfo.setCreateBy(user.getId());
            LgInfo.setCreateDate(DateUtils.getDate());
        } else {// 修改数据
            LgInfo = this.get(LgInfoVo.getId());
            BeanUtils.copyProperties(LgInfoVo, LgInfo, "id", "createDate", "createBy");
            if (LgInfoVo.getStatus().equals(LetterConstant.STATUS_TEMP)) {// 暂存
                LgInfo.setStatus(LetterConstant.STATUS_TEMP);
                msgType = LetterConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = LetterConstant.MSGA_MODIFY + "";// 修改暂存
            } else {// 提交
                LgInfo.setStatus(LetterConstant.STATUS_WAIT_CHECK);
                msgType = LetterConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = LetterConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            }
            LgInfo.setUpdateBy(user.getId());
            LgInfo.setUpdateDate(DateUtils.getDate());
        }
        super.saveOrUpdate(LgInfo);
        this.getEntityDao().flush();
        if (StringUtils.isEmpty(LgInfoVo.getId())) {// 新增数据
            // 挂接保证金开始
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                // 判断是否挂接了保证金
                if (!EmptyUtils.isEmpty(LgInfoVo.getIsHookBond())
                        && CommonConstant.TRUE.equals(LgInfoVo.getIsHookBond())) {// 等于1则表示挂接了保证金
                    // 保存保证金的占用信息
                    LgInfoVo.setId(LgInfo.getId());
                    this.addFcBondApply(LgInfoVo, user);
                }
            }
            // 挂接保证金结束
        } else {// 修改数据
                // 挂接保证金开始
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                // 判断是否挂接了保证金
                if ((LgInfoVo.getIsHookBond() != null) && (LgInfoVo.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {// 修改后挂接保证金
                    FcBondApplyDTO dto = fcBondHookService.getFcBondApply(LgInfoVo.getId(), LgInfoVo.getGuaranteeNo(),
                            LgInfoVo.getBondId());// 当前保证金占用信息
                    FcBondApplyDTO fcBondApplyDTOPre = fcBondHookService.getFcBondApply(LgInfoVo.getId(),
                            LgInfoVo.getGuaranteeNo(), LgInfoVo.getBondIdPre());// 修改前保证金占用信息
                    // 判断占用金额是否大于可用余额
                    FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(LgInfoVo.getBondId());
                    BigDecimal applyMoney = LgInfoVo.getBondMoney();
                    BigDecimal availMoney = fcBondDTO.getAvailMoney();
                    if (fcBondApplyDTOPre != null) {
                        availMoney = availMoney.add(fcBondApplyDTOPre.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
                    }
                    BigDecimal dif = availMoney.subtract(applyMoney);
                    if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                        throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
                    }
                    LgInfoVo.setId(LgInfo.getId());
                    if (dto == null) {// 没有则新增
                        if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空则删除之前的占用信息
                            fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                            fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                            fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                        }
                        this.addFcBondApply(LgInfoVo, user);// 新增保证金占用信息
                    } else {
                        // 修改保证金占用信息
                        this.updateFcBondApply(LgInfoVo, dto, user);
                    }
                } else {// 修改后不挂保证金
                    if (LgInfoVo.getBondIdPre() != null && StringUtils.isNotEmpty(LgInfoVo.getBondIdPre())) {// 修改前挂接保证金，需删除保证金信息，释放占用额度
                        FcBondApplyDTO fcBondApplyDTOPre = fcBondHookService.getFcBondApply(LgInfoVo.getId(),
                                LgInfoVo.getGuaranteeNo(), LgInfoVo.getBondIdPre());// 修改前保证金占用信息
                        fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                    }
                }
            }
            // 挂接保证金结束
        }
        // 挂接授信
        if (!EmptyUtils.isEmpty(LgInfoVo.getRatmodel()) && CommonConstant.TRUE.equals(LgInfoVo.getRatmodel())) {// 挂接授信
            setGuaRatUrationDTO(LgInfo, urationDTO);
            if (EmptyUtils.isEmpty(LgInfoVo.getIsOpenRat())) {// 当敞口参数为空时，默认不启用敞口
                urationDTO.setIsOpenRat(LetterConstant.GUA_COMMON_N);
            } else {
                urationDTO.setIsOpenRat(LgInfoVo.getIsOpenRat());
            }
            if (LetterConstant.STATUS_WAIT_CHECK.equals(LgInfo.getStatus())) {// 提交,授信占用冻结
                urationService.addUseRation(urationDTO);
            } else {// 暂存，授信占用暂存，不占用额度
                urationService.tempUseRation(urationDTO);
            }
        } else if (!EmptyUtils.isEmpty(LgInfoVo.getRatmodel()) && CommonConstant.FALSE.equals(LgInfoVo.getRatmodel())) {
            if (!EmptyUtils.isEmpty(LgInfoVo.getId())) {// 如果之前挂了授信
                urationService.deleteUseRation(urationDTO);
            }
        }
        // 挂接授信完
        // 挂接审批
        if (LetterConstant.STATUS_WAIT_CHECK.equals(LgInfo.getStatus())) {
             getStartHandler().handler(LetterWorkFlowConstant.GUA_GUARANTEE_APPLY, LgInfo.getId(), LgInfo,
                    LgInfo.getCorpId(), user.getId());
        }
        // 日志记录
        guaLogService.saveGuaLog(GuaLogDTO.buildGuaLogDTO(LgInfo.getId(), LgInfo.getGuaranteeMoney(), msgType, message, user));
    }



    /**
     * 根据id获取保函申请信息
     */
    @Transactional(readOnly = true)
    public GuaGuaranteeInfoVO getGuaranteeInfoById(String id) {
        return guaLgInfoDao.getLgInfoById(id);
    }
    /**
     * 根据id删除保函申请信息
     */
    public void delete(String id, ICurrentUser user) {
        GuaGuaranteeInfo LgInfo = this.get(id);
        LgInfo.setStatus(LetterConstant.STATUS_DELETE);
        LgInfo.setUpdateBy(user.getId());
        LgInfo.setUpdateDate(DateUtils.getDate());
        this.updateEntity(LgInfo);
        // 删除挂接的授信
        if (!EmptyUtils.isEmpty(LgInfo.getRatmodel()) && CommonConstant.TRUE.equals(LgInfo.getRatmodel())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, id);
            urationService.deleteUseRation(urationDTO);
        }
        // 删除保证金挂接信息，释放占用额度
        if (!EmptyUtils.isEmpty(LgInfo.getIsHookBond())
                && LetterConstant.GUA_COMMON_Y.equals(LgInfo.getIsHookBond())) {// 挂接保证金
            FcBondApplyDTO bondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(LgInfo.getId(),
                    LgInfo.getGuaranteeNo());
            bondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
            fcBondHookService.updateFcBondApplyData(bondApplyDTO);
        }
    }
    /**
     * 根据id打回保函信息
     */
    public void repulse(String id, ICurrentUser user) {
        GuaGuaranteeInfo LgInfo = this.get(id);
        LgInfo.setStatus(LetterConstant.STATUS_BACK);
        LgInfo.setUpdateBy(user.getId());
        LgInfo.setUpdateDate(DateUtils.getDate());
        this.updateEntity(LgInfo);
        // 释放挂接的授信
        if (!EmptyUtils.isEmpty(LgInfo.getRatmodel()) && CommonConstant.TRUE.equals(LgInfo.getRatmodel())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, id);
            urationDTO.setMoney(LgInfo.getGuaranteeMoney());// 所有金额
            urationDTO.setAllRelease(true);// 全部释放
            urationService.releaseRation(urationDTO);
        }
    }
    /**
     * 新增保证金占用的相关信息
     *
     * @param LgInfoVo
     */
    public void addFcBondApply(GuaGuaranteeInfoVO LgInfoVo, ICurrentUser user) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(LgInfoVo, user);
        fcBondHookService.addFcBondApply(dto);// 新增保证金占用的相关信息
    }
    /**
     * 设置FcBondApplyDTO各属性值
     *
     * @param LgInfoVo
     * @return
     */
    public FcBondApplyDTO setFcBondApplyDTO(GuaGuaranteeInfoVO LgInfoVo, ICurrentUser user) {
        FcBondApplyDTO dto = new FcBondApplyDTO();
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_GUA);// 0银行授信1银行借款2承兑汇票3其他4授信追加5保函
        dto.setItemNo(LgInfoVo.getGuaranteeNo());// 项目单号
        dto.setItemId(LgInfoVo.getId());// 占用保证金的项目Id
        dto.setCorpId(LgInfoVo.getCorpId());// 单位id
        dto.setBankId(LgInfoVo.getOpenCorp());// 联行号
        BigDecimal itemMoney = new BigDecimal(0);
        if ((LgInfoVo.getGuaranteeMoney()) != null) {
            itemMoney = itemMoney.add(LgInfoVo.getGuaranteeMoney());
        }
        dto.setItemMoney(itemMoney);// 项目金额
        dto.setBondPercent(LgInfoVo.getBondPercent());
        dto.setStartDate(LgInfoVo.getStartDate());// 项目开始时间
        if (LgInfoVo.getEndDate() == null) {
            dto.setEndDate(LgInfoVo.getExpectEndDate());
        } else {
            dto.setEndDate(LgInfoVo.getEndDate());// 项目结束时间
        }
        dto.setBondContract(LgInfoVo.getBondContract());// 保证金合同
        dto.setBondId(LgInfoVo.getBondId());// 保证金id
        dto.setApplyMoney(LgInfoVo.getBondMoney());// 保证金金额,即占用金额
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 占用状态
        dto.setCreateBy(user.getLoginName());// 创建人登录名称
        dto.setCreateDate(DateUtils.getDate());// 创建日期
        return dto;
    }
    /**
     * 修改保证金占用的相关信息
     *
     * @param LgInfoVo
     */
    public void updateFcBondApply(GuaGuaranteeInfoVO LgInfoVo, FcBondApplyDTO dto, ICurrentUser user) {
        dto.setBondPercent(LgInfoVo.getBondPercent());
        dto.setApplyMoney(LgInfoVo.getBondMoney());
        dto.setBondContract(LgInfoVo.getBondContract());
        dto.setBankId(LgInfoVo.getOpenCorp());
        BigDecimal itemMoney = new BigDecimal(0);
        if ((LgInfoVo.getGuaranteeMoney()) != null) {
            itemMoney = itemMoney.add(LgInfoVo.getGuaranteeMoney());
        }
        dto.setItemMoney(itemMoney);// 项目金额
        dto.setStartDate(LgInfoVo.getStartDate());// 项目开始时间
        if (LgInfoVo.getEndDate() == null) {
            dto.setEndDate(LgInfoVo.getExpectEndDate());
        } else {
            dto.setEndDate(LgInfoVo.getEndDate());// 项目结束时间
        }
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 占用状态
        dto.setBondId(LgInfoVo.getBondId());
        dto.setCorpId(LgInfoVo.getCorpId());// 单位id
        dto.setUpdateBy(user.getLoginName());
        dto.setUpdateDate(DateUtils.getDate());
        fcBondHookService.updateFcBondApply(dto);// 修改保证金占用的相关信息
    }
    /**
     * 根据保函编号查询保函申请信息
     */
    @Transactional(readOnly = true)
    public List<GuaGuaranteeInfoVO> queryLgByNo(String guaranteeNo) {
        return guaLgInfoDao.queryLgByNo(guaranteeNo);
    }
    /**
     * 根据保函申请单位查询保函申请信息
     */
    @Transactional(readOnly = true)
    public List<GuaGuaranteeInfoVO> queryLgByCorpId(GuaGuaranteeInfoVO infoVO) {
        return guaLgInfoDao.queryLgByCorpId(infoVO);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String bussinessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        GuaGuaranteeInfo LgInfo = this.get(bussinessKey,true);
        boolean result = workFlowResult.isPass();// 是否通过
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        if (result) {
            LgInfo.setStatus(LetterConstant.STATUS_CHECK);
            msgType = LetterConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            message = LetterConstant.MSGA_CHECKOK + "";
        } else {
            LgInfo.setStatus(LetterConstant.STATUS_BACK);
            msgType = LetterConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            message = LetterConstant.MSGA_CHECKBACK;
        }
        this.updateEntity(LgInfo);
        // 更改挂接的授信信息
        this.changeRatBkrationMoney(LgInfo, result);
        // 日志记录
        guaLogService.saveGuaLog(GuaLogDTO.buildGuaLogDTO(LgInfo.getId(), LgInfo.getGuaranteeMoney(), msgType, message, user));
    }
    @Override
    public BaseDao<GuaGuaranteeInfo, String> getEntityDao() {
        // TODO Auto-generated method stub
        return guaLgInfoDao;
    }
    /**
     * 查询保函到期列表
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput logoutDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_INFO_LOGOUT, user.getId());
        return guaLgInfoDao.logoutDatatable(input, corpAuth);
    }
    /**
     * 保存保函申请信息
     */
    public void logout(GuaGuaranteeInfoVO LgInfoVo, ICurrentUser user) {
        GuaGuaranteeInfo LgInfo = null;
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        if (StringUtils.isNotEmpty(LgInfoVo.getId())) {// 注销
            LgInfo = this.get(LgInfoVo.getId());
            LgInfo.setStatus(LetterConstant.STATUS_LOGOUT);
            LgInfo.setLogoutDate(LgInfoVo.getLogoutDate());
            this.saveOrUpdate(LgInfo);// 更新保函信息
            msgType = LetterConstant.MESSAGE_TYPE_LOGOUT;// 注销
            message = LetterConstant.MSGA_LOGOUT;// 注销
            // 更改保函申请单履行状态
            GuaPerformRegVO regVO = guaPerformRegDao.getGuaPerformRegByGuaId(LgInfoVo.getId());
            if (regVO != null) {
                GuaPerformReg reg = guaPerformRegDao.get(regVO.getId());
                reg.setStatus(LetterConstant.GUARANTEE_REGISFINISH);// 履行完成
                guaPerformRegDao.saveOrUpdate(reg);// 更新履行信息
            }
            // 释放授信
            if (!EmptyUtils.isEmpty(LgInfo.getRatmodel()) && CommonConstant.TRUE.equals(LgInfo.getRatmodel())) {// 原先挂接授信
                RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, LgInfo.getId());
                urationDTO.setMoney(LgInfo.getGuaranteeMoney());// 所有金额
                urationDTO.setAllRelease(true);// 全部释放
                urationService.releaseRation(urationDTO);// 释放已正式占用的授信
            }
            // 授信额度释放完成
            // 释放保证金
            if (!EmptyUtils.isEmpty(LgInfo.getIsHookBond())
                    && LetterConstant.GUA_COMMON_Y.equals(LgInfo.getIsHookBond())) {// 挂接保证金
                FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(LgInfo.getId(), LgInfo.getGuaranteeNo());
                dto.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondHookService.updateFcBondApplyData(dto);
            }
            // 保证金释放完成
            // 日志记录
            guaLogService.saveGuaLog(GuaLogDTO.buildGuaLogDTO(LgInfo.getId(), LgInfo.getGuaranteeMoney(), msgType, message, user));
        }
    }
    /**
     * 查询保函列表
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput loadAllDatatable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_INFO_QUERY, user.getId());
        return guaLgInfoDao.loadAllDatatable(input, corpAuth);
    }
    @Override
    @XxlJob("GuaGuaranteeInfoService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.GUA, LetterConstant.GUA_GUARANTEE_NOTIFYDAYS);// 保函到期提醒天数
        Integer notifyDays = new Integer(configDTO.getValue());
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询保函即将到期数据
        List<GuaGuaranteeInfo> guaInfos = guaLgInfoDao.getGuaGuaranteeMature(endDate);
        if (!EmptyUtils.isEmptyList(guaInfos)) {
            for (GuaGuaranteeInfo guaInfo : guaInfos) {
                GuaGuaranteeInfoVO guaInfoVo = new GuaGuaranteeInfoVO();
                BeanUtils.copyProperties(guaInfo, guaInfoVo);
                notificationService.send(guaInfo.getId(), guaInfo.getCorpId(),
                        NotificationConstant.CMS.GUA_GUARANTEE_MATURE, guaInfoVo);
            }
        }
    }
    /**
     * 授信占用赋值
     *
     * @param LgInfo
     * @param urationDTO
     */
    private void setGuaRatUrationDTO(GuaGuaranteeInfo LgInfo, RatUrationDTO urationDTO) {
        urationDTO.setBillId(LgInfo.getId());// 业务ID
        urationDTO.setBillNo(LgInfo.getGuaranteeNo());// 业务编号
        urationDTO.setSysCode(ConfigConstant.GUA);// 保函
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_08);// 保函占用
        urationDTO.setContactNo(null);// 合同号
        urationDTO.setBeginDate(LgInfo.getStartDate());// 保函开始日期
        if (null == LgInfo.getEndDate()) {
            urationDTO.setEndDate(LgInfo.getExpectEndDate());// 保函预计结束日期
        } else {
            urationDTO.setEndDate(LgInfo.getEndDate());// 保函结束日期
        }
        urationDTO.setMoney(LgInfo.getGuaranteeMoney());// 保函金额
        urationDTO.setCurCode(LgInfo.getCurId());// 业务币别
        if (null == LgInfo.getIsHookBond()) {
            urationDTO.setBailRate(BigDecimal.ZERO);// 保证金比例
            urationDTO.setBondMoney(BigDecimal.ZERO);// 保证金金额
            urationDTO.setBondCurId(LgInfo.getCurId());// 保证金币别
        } else {
            urationDTO.setBailRate(LgInfo.getBondPercent());// 保证金比例
            urationDTO.setBondMoney(LgInfo.getBondMoney());// 保证金金额
            urationDTO.setBondCurId(LgInfo.getBondCurId());// 保证金币别
        }
        urationDTO.setBillMoney(LgInfo.getGuaranteeMoney());// 业务金额
    }
    /**
     * 审批完成后更改占用的授信
     *
     * @param info
     * @param result
     */
    public void changeRatBkrationMoney(GuaGuaranteeInfo info, boolean result) {
        // 授信挂接，审批通过修改为正式占用
        if (result) {
            if (!EmptyUtils.isEmpty(info.getRatmodel()) && CommonConstant.TRUE.equals(info.getRatmodel())) {
                RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, info.getId());
                urationService.passUseRation(urationDTO);
            }
        } else {// 打回
                // 授信挂接
            if (!EmptyUtils.isEmpty(info.getRatmodel()) && CommonConstant.TRUE.equals(info.getRatmodel())) {
                RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.GUA, info.getId());
                urationService.backUseRation(urationDTO);
            }
        }
    }
    /**
     * 查询保函是否挂接授信
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatContains() {
        return configService.getConfig(ConfigConstant.GUA, "rat_Contains");
    }
    /**
     * 查询保函是否启用敞口授信
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsStartOpenRat() {
        return configService.getConfig(ConfigConstant.GUA, "start_Openrat");
    }
    /**
     * 查询保函是否挂保证金
     *
     * @return
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsHangBail() {
        return configService.getConfig(ConfigConstant.GUA, "is_HangBail");
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = guaLgInfoDao.queryBusBillByCorpId(corpId, null);
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DEL_GUARANTEE_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 保函暂存，打回，待审批的数据
            int dataSize = guaLgInfoDao.queryBusBillByCorpId(corpId, "0,-1,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.GUA_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.GUA_VALID_DISABLE_GUARANTEE_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
    /**
     * 查询保函预计释放额度
     * @param billId
     * @param calEndDate
     * @return
     */
    public BigDecimal queryGuaranteeUseRatMoney(String billId, Date calEndDate) {
        return guaLgInfoDao.queryGuaranteeUseRatMoney(billId,calEndDate);
    }
}
