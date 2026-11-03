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
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaLogService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaLogDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.constant.LetterWorkFlowConstant;
import com.bytter.letter.domain.dao.GuaGuaranteeChangeInfoDao;
import com.bytter.letter.domain.entity.GuaGuaranteeChangeInfo;
import com.bytter.letter.domain.entity.GuaGuaranteeInfo;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeInfoService
 * @Description:保函更改申请业务逻辑层
 * @date 2019年1月9日 上午10:56:45
 */
@Service("GuaGuaranteeChangeInfoService")
@Transactional
@WorkFlow(value = {LetterWorkFlowConstant.GUA_GUARANTEE_CHANGE })
public class GuaGuaranteeChangeInfoService extends BaseService<GuaGuaranteeChangeInfo, String>
        implements IWorkFlowListener {
    @Autowired
    private GuaGuaranteeChangeInfoDao LgChangeInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private GuaGuaranteeInfoService lgInfoService;
    @Autowired
    @Qualifier("ratUrationServiceImpl")
    private IRatUrationService urationService;
    @Autowired
    @Qualifier("fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @Autowired
    private IGuaLogService guaLogService;
    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 查询保函申请更改列表
     */
    @Transactional(readOnly = true)
    @SuppressWarnings("rawtypes")
    public DataTablesOutput datatableList(DataTablesInput input, ICurrentUser user) {

        List<String> corpAuth = corpService.getUserCorpIds(LetterConstant.GUA_GUARANTEE_CHANGE_QUERY, user.getId());
        return LgChangeInfoDao.datatableList(input, corpAuth);
    }

    /**
     * 生成保函更改编号
     *
     * @author luxy
     */
    public String getlgChangeNo(SequenceEnum senum, String prefix) {
        return SequenceSingleton.getSequence(senum, prefix);
    }

    /**
     * 保存保函更改申请信息
     */
    public void tempOrSubmit(GuaGuaranteeChangeInfoVO lgChangeInfoVo, RatUrationDTO urationDTO, ICurrentUser user) {
        GuaGuaranteeChangeInfo lgChangeInfo = null;
        GuaGuaranteeInfo lgInfo = lgInfoService.get(lgChangeInfoVo.getGuaraId());
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        ConfigDTO bailConfig = lgInfoService.getIsHangBail();// 获取参数设置是否挂接保证金
        if (StringUtils.isEmpty(lgChangeInfoVo.getId())) {// 新增数据
            lgChangeInfo = new GuaGuaranteeChangeInfo();
            BeanUtils.copyNotNullProperties(lgChangeInfoVo, lgChangeInfo);
            if (lgChangeInfoVo.getStatus().equals(LetterConstant.STATUS_TEMP)) {// 暂存
                lgChangeInfo.setStatus(LetterConstant.STATUS_TEMP);
                msgType = LetterConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = LetterConstant.MSGA_INPUT + "";// 录入暂存
            } else {// 提交
                lgChangeInfo.setStatus(LetterConstant.STATUS_WAIT_CHECK);
                msgType = LetterConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = LetterConstant.MSGA_INPUT_SUBMIT;// 录入提交
            }
            lgChangeInfo.setCreateBy(user.getId());
            lgChangeInfo.setCreateDate(DateUtils.getDate());
            // 授信处理开始
            if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                dealWithRatMsg(lgChangeInfoVo, lgInfo, urationDTO);
            }
            // 授信处理结束
            // 挂接保证金开始
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                // 判断是否挂接了保证金
                if (!EmptyUtils.isEmpty(lgInfo.getIsHookBond()) && CommonConstant.TRUE.equals(lgInfo.getIsHookBond())) {// 等于1则表示挂接了保证金
                    setBondMsg(lgChangeInfo, lgInfo, user);
                }
            }
            // 挂接保证金结束
        } else {// 修改数据
            lgChangeInfo = this.get(lgChangeInfoVo.getId());
            BeanUtils.copyProperties(lgChangeInfoVo, lgChangeInfo, "id", "guaranteeNo", "createDate", "createBy");
            if (lgChangeInfoVo.getStatus().equals(LetterConstant.STATUS_TEMP)) {// 暂存
                lgChangeInfo.setStatus(LetterConstant.STATUS_TEMP);
                msgType = LetterConstant.MESSAGE_TYPE_TEMP;// 暂存
                message = LetterConstant.MSGA_MODIFY + "";// 修改暂存
            } else {// 提交
                lgChangeInfo.setStatus(LetterConstant.STATUS_WAIT_CHECK);
                msgType = LetterConstant.MESSAGE_TYPE_SUBMIT;// 提交
                message = LetterConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
            }
            lgChangeInfo.setUpdateBy(user.getId());
            lgChangeInfo.setUpdateDate(DateUtils.getDate());
            // 授信处理开始
            if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                dealWithRatMsg(lgChangeInfoVo, lgInfo, urationDTO);
            }
            // 授信处理结束
            // 挂接保证金开始
            if (bailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                // 判断是否挂接了保证金
                if (!EmptyUtils.isEmpty(lgInfo.getIsHookBond()) && CommonConstant.TRUE.equals(lgInfo.getIsHookBond())) {// 等于1则表示挂接了保证金
                    setBondMsg(lgChangeInfo, lgInfo, user);
                }
            }
            // 挂接保证金结束
        }
        lgInfo.setStatus(LetterConstant.STATUS_CHANGEING);// 将保函申请状态改为更改中
        super.saveOrUpdate(lgChangeInfo);
        lgInfoService.saveOrUpdate(lgInfo);
        // 挂接审批
        if (LetterConstant.STATUS_WAIT_CHECK.equals(lgChangeInfo.getStatus())) {
            getStartHandler().handler(LetterWorkFlowConstant.GUA_GUARANTEE_CHANGE, lgChangeInfo.getId(), lgChangeInfo,
                    lgInfo.getCorpId(), user.getId());
        }
        // 日志记录 异步上报可能会存在丢失
        guaLogService.saveGuaLog(GuaLogDTO.buildGuaLogDTO(lgChangeInfo.getId(), lgChangeInfo.getGuaranteeMoney(), msgType, message, user));
    }

    /**
     * 根据保函申请id查询保函申请更改列表
     */
    @Transactional(readOnly = true)
    public List<GuaGuaranteeChangeInfoVO> queryChangeListByGuaId(String guaranId) {
        return LgChangeInfoDao.queryChangeListByGuaId(guaranId);
    }

    /**
     * 根据id查询保函更改申请信息
     */
    @Transactional(readOnly = true)
    public GuaGuaranteeChangeInfoVO queryChangeInfoById(String id) {
        return LgChangeInfoDao.queryChangeInfoById(id);
    }

    /**
     * 根据id将保函更改申请状态改为删除
     */
    public void deleteById(String id, ICurrentUser user) {
        GuaGuaranteeChangeInfo lgChangeInfo = super.get(id);
        lgChangeInfo.setStatus(LetterConstant.STATUS_DELETE);
        lgChangeInfo.setUpdateBy(user.getId());
        lgChangeInfo.setUpdateDate(DateUtils.getDate());
        super.updateEntity(lgChangeInfo);
        GuaGuaranteeInfo lgInfo = lgInfoService.get(lgChangeInfo.getGuaraId());
        lgInfo.setStatus(LetterConstant.GUARANTEE_RECEIVED);
        // 对保证金做了修改保证金还原到之前的情况
        if (!EmptyUtils.isEmpty(lgInfo.getIsHookBond()) && CommonConstant.TRUE.equals(lgInfo.getIsHookBond())) {// 等于1则表示挂接了保证金
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(lgInfo, LgInfoVo);
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(lgInfo.getId(), lgInfo.getGuaranteeNo());
            LgInfoVo.setBondId(dto.getBondId());
            // 修改保证金占用信息
            lgInfoService.updateFcBondApply(LgInfoVo, dto, user);
        }
        // 挂接保证金结束
        // 如果挂接了授信还原到原来保函申请的情况
        if ((lgInfo.getGuaranteeMoney()).compareTo(lgChangeInfo.getGuaranteeMoney()) != 0
                || (lgInfo.getIsOpenRat().equals(LetterConstant.GUA_COMMON_Y)
                && lgInfo.getBondMoney().compareTo(lgChangeInfo.getBondMoney()) != 0)) {
            // 授信合同打回和重新占用处理
            if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                // 挂接的授信信息
                if (CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {
                    RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                            lgChangeInfo.getGuaraId());
                    RatUrationDTO ratUrationDTO = new RatUrationDTO();
                    ratUrationDTO.setSysCode(ConfigConstant.GUA);
                    ratUrationDTO.setBillId(lgChangeInfo.getGuaraId());
                    ratUrationDTO.setStatus(RationConstant.RAT_USE_STATUS_RELEASE);
                    List<RatUrationDTO> ratUrationDTOS = urationService.getRatUrationList(ratUrationDTO);
                    RatUrationDTO oriUrationDTO = ratUrationDTOS.get(0);// 取最近一次释放的数据
                    urationService.backUseRation(urationDTO);// 还原在审额度
                    urationService.deleteUseRation(urationDTO);
                    // 以下操作是在删除后将授信数据还原到保函申请审批后（未进行过更改操作）或者上一次更改完成的数据状态
                    oriUrationDTO.setMoney(lgInfo.getGuaranteeMoney());
                    oriUrationDTO.setSysCode(ConfigConstant.GUA);
                    urationService.deleteUseRation(oriUrationDTO);// 删除原有授信
                    oriUrationDTO.setCurCode(lgInfo.getCurId());// 这里传的值是业务币别
                    urationService.directUseRation(oriUrationDTO);// 占用原有的授信
                }
            }
        }
        // 挂接授信完
        lgInfoService.saveOrUpdate(lgInfo);// 将保函申请状态改为已领取
    }

    /**
     * 查询保函更改待审批列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput getApproveDataTables(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(LetterWorkFlowConstant.GUA_GUARANTEE_CHANGE, input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "b.id", true);
        }
        DataTablesOutput dp = LgChangeInfoDao.approveDatatableList(input, businessSql);
        return workFlowBusiness.merge(dp);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String BussinessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        boolean result = workFlowResult.isPass();
        GuaGuaranteeChangeInfo lgChangeInfo = super.get(BussinessKey, true);
        String message = "";// 日志信息
        String msgType = "";// 日志类型
        if (result) {
            lgChangeInfo.setStatus(LetterConstant.STATUS_CHECK);// 审批通过
            msgType = LetterConstant.MESSAGE_TYPE_CHECK_PASS;
            message = LetterConstant.MSGA_CHECKOK + "";
        } else {
            lgChangeInfo.setStatus(LetterConstant.STATUS_BACK);// 审批打回
            msgType = LetterConstant.MESSAGE_TYPE_CHECK_BACK;
            message = LetterConstant.MSGA_CHECKBACK;
        }
        super.updateEntity(lgChangeInfo);
        // 日志记录
        guaLogService.saveGuaLog(GuaLogDTO.buildGuaLogDTO(lgChangeInfo.getId(), lgChangeInfo.getGuaranteeMoney(), msgType, message, user));
    }

    /**
     * 授信占用赋值
     *
     * @param LgInfo
     * @param urationDTO
     */
    @Transactional(readOnly = true)
    public void setGuaRatUrationDTO(GuaGuaranteeChangeInfoVO LgInfo, GuaGuaranteeInfo info, RatUrationDTO urationDTO) {
        urationDTO.setCurCode(info.getCurId());// 业务币别ID
        urationDTO.setBillId(info.getId());// 业务ID
        urationDTO.setBillNo(info.getGuaranteeNo());// 业务编号
        urationDTO.setSysCode(ConfigConstant.GUA);// 保函
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_08);// 保函占用
        urationDTO.setContactNo(null);// 合同号
        urationDTO.setCorpId(info.getCorpId());// 单位
        urationDTO.setBeginDate(LgInfo.getStartDate());// 保函开始日期
        if (null == LgInfo.getEndDate()) {
            urationDTO.setEndDate(LgInfo.getExpectEndDate());// 保函预计结束日期
        } else {
            urationDTO.setEndDate(LgInfo.getEndDate());// 保函结束日期
        }
        urationDTO.setMoney(LgInfo.getGuaranteeMoney());// 保函金额
        urationDTO.setBailRate(info.getBondPercent());// 保证金比例
        urationDTO.setBondMoney(LgInfo.getBondMoney());// 保证金金额
        urationDTO.setCurCode(LgInfo.getCurId());// 业务币别
        urationDTO.setBondCurId(LgInfo.getBondCurId());// 保证金币别
        urationDTO.setBillMoney(LgInfo.getGuaranteeMoney());// 业务金额
        urationDTO.setIsOpenRat(info.getIsOpenRat());
    }

    @Override
    public BaseDao<GuaGuaranteeChangeInfo, String> getEntityDao() {
        // TODO Auto-generated method stub
        return LgChangeInfoDao;
    }

    /**
     * 处理保证金业务
     */
    public void setBondMsg(GuaGuaranteeChangeInfo lgChangeInfo, GuaGuaranteeInfo lgInfo, ICurrentUser user) {
        if (lgInfo.getBondMoney().compareTo(lgChangeInfo.getBondMoney()) != 0
                || lgInfo.getGuaranteeMoney().compareTo(lgChangeInfo.getGuaranteeMoney()) != 0) {// 保证金或者保函申请额度发生变化
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(lgInfo.getId(), lgInfo.getGuaranteeNo());
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(dto.getBondId());
            BigDecimal applyMoney = lgChangeInfo.getBondMoney();
            BigDecimal availMoney = fcBondDTO.getAvailMoney();
            if (dto != null) {
                availMoney = availMoney.add(dto.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHANGECHECK, availMoney.toString());
            }
            GuaGuaranteeInfoVO LgInfoVo = new GuaGuaranteeInfoVO();
            BeanUtils.copyNotNullProperties(lgInfo, LgInfoVo);
            LgInfoVo.setBondId(dto.getBondId());
            LgInfoVo.setBondMoney(lgChangeInfo.getBondMoney());
            LgInfoVo.setGuaranteeMoney(lgChangeInfo.getGuaranteeMoney());
            BigDecimal bondPercent = null;// 保证金比例
            if (lgInfo.getCurId().equals(lgInfo.getBondCurId())) {// 保函币别与保证金一致时
                bondPercent = lgChangeInfo.getBondMoney().divide(lgChangeInfo.getGuaranteeMoney(), 4,
                        BigDecimal.ROUND_HALF_UP);
            } else {// 币别不一致时需进行汇率折算
                CorpDTO corpDTO = corpService.get(lgInfo.getCorpId());// 保函申请单位
                BigDecimal exchangeRate = BigDecimal.ONE;
                if (null != corpDTO) {
                    String corpCurId = corpDTO.getCurId();
                    exchangeRate = rateAdjustService.getExchangeRateBy(lgInfo.getCurId(), corpCurId,
                            lgInfo.getStartDate(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                }
                BigDecimal exchangeMoney = exchangeRate.multiply(lgInfo.getGuaranteeMoney());// 折算后的金额
                bondPercent = lgChangeInfo.getBondMoney().divide(exchangeMoney, 4, BigDecimal.ROUND_HALF_UP);
            }
            LgInfoVo.setBondPercent(bondPercent);
            // 修改保证金占用信息
            lgInfoService.updateFcBondApply(LgInfoVo, dto, user);
        }
    }

    /**
     * 处理授信业务
     */
    public void dealWithRatMsg(GuaGuaranteeChangeInfoVO lgChangeInfoVo, GuaGuaranteeInfo lgInfo,
                               RatUrationDTO urationDTO) {
        if (StringUtils.isEmpty(lgChangeInfoVo.getId())) {// 新增数据
            // 挂接授信
            if ((lgInfo.getGuaranteeMoney()).compareTo(lgChangeInfoVo.getGuaranteeMoney()) != 0
                    || (lgInfo.getIsOpenRat().equals(LetterConstant.GUA_COMMON_Y)
                    && lgInfo.getBondMoney().compareTo(lgChangeInfoVo.getBondMoney()) != 0)) {
                // 授信合同释放和重新占用处理
                // 释放原来占用的授信的两种情况
                // 1.变更申请在新增数据的时候如果占用了授信并且变更前后的保函额度发生了变化
                // 2.挂接授信为敞口授信，保证金额度发生了变化
                String billName = "";
                if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                    RatUrationDTO ratUrationDTO = new RatUrationDTO();
                    ratUrationDTO.setSysCode(ConfigConstant.GUA);
                    ratUrationDTO.setBillId(lgInfo.getId());
                    ratUrationDTO.setStatus(RationConstant.RAT_USE_STATUS_USE);
                    List<RatUrationDTO> ratUrationDTOS = urationService.getRatUrationList(ratUrationDTO);
                    RatUrationDTO oriUrationDTO = ratUrationDTOS.get(0);
                    oriUrationDTO.setMoney(lgInfo.getGuaranteeMoney());// 所有金额
                    oriUrationDTO.setAllRelease(true);// 全部释放
                    oriUrationDTO.setReleaseNonRecycleRat(true);// 当为不循环授信时，需要释放授信
                    billName = oriUrationDTO.getBillName();
                    urationService.releaseRation(oriUrationDTO);// 释放已正式占用的授信
                    this.getSession().flush();
                }
                // 授信额度释放完成
                // 原授信合同释放后，再重新占用授信的在审额度
                if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                    // 重新占用授信的在审占用额度
                    setGuaRatUrationDTO(lgChangeInfoVo, lgInfo, urationDTO);
                    urationDTO.setBillName(billName);// 授信业务品种
                    urationService.addUseRation(urationDTO);
                }
            }
            // 挂接授信完
        } else {// 修改数据
            // 挂接授信
            if ((lgInfo.getGuaranteeMoney()).compareTo(lgChangeInfoVo.getGuaranteeMoney()) != 0
                    || (lgInfo.getIsOpenRat().equals(LetterConstant.GUA_COMMON_Y)
                    && lgInfo.getBondMoney().compareTo(lgChangeInfoVo.getBondMoney()) != 0)) {
                // 原授信合同释放后，再重新占用授信的在审额度
                if (!EmptyUtils.isEmpty(lgInfo.getRatmodel()) && CommonConstant.TRUE.equals(lgInfo.getRatmodel())) {// 原先挂接授信
                    // 释放原授信在审占用
                    urationService.backUseRation(new RatUrationDTO(ConfigConstant.GUA, lgInfo.getId()));
                    // 重新占用授信的在审占用额度
                    setGuaRatUrationDTO(lgChangeInfoVo, lgInfo, urationDTO);
                    urationService.addUseRation(urationDTO);
                }
            }
            // 挂接授信完
        }
    }
}
