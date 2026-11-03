package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.billPool.dao.*;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: AbPoolEmitApplyService
 * @Description: 出票申请Service
 * @author wangzy
 * @date 2020/2/26 15:09
 *
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY)
@Service("abPoolEmitApplyService")
@Transactional
public class AbPoolEmitApplyService  extends BaseService<AbPoolDtl, String> implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private AbPoolEmitApplyDao abPoolEmitApplyDao;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbPoolService abPoolService;
    @Autowired
    private AbPoolifService abPoolifService;
    @DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private AbPoolBailDtlDao abPoolBailDtlDao;
    @Autowired
    private AbPoolLimitAllotCheckDao abPoolLimitAllotCheckDao;
    @Autowired
    private AbPoolLimitAllotDao abPoolLimitAllotDao;
    @Autowired
    private AbPoolLimitApplyCheckDao abPoolLimitApplyCheckDao;
    @Autowired
    private AbPoolSignDao abPoolSignDao;
    @Autowired
    private AbPoolLimitApplyDao abPoolLimitApplyDao;
    @Override
    public BaseDao<AbPoolDtl, String> getEntityDao() {
        return abPoolEmitApplyDao;
    }
    /**
     * 出票申请列表查询
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        String bailResource = AbPoolConstant.AB_POLL_EMIT_APPLY_RESOURCE_OP;
        List<String> corpAuthList = abCorpService.getUserCorpIds(bailResource,input.getCurrentUser().getId());
        return abPoolEmitApplyDao.getDataTablesUnion(input, corpAuthList);
    }
    /**
     * 出票申请信息保存或提交
     * @param operate 保存或提交
     * @param abPoolEmitApplyVO 信息
     * @param user 当前用户
     */
    public AjaxJson abPoolEmitApplySaveOrSubmit(String operate, AbPoolEmitApplyVO abPoolEmitApplyVO, ICurrentUser user) {
        // 申请人银行账号和票据池必须是同一银行类型
        if (abPoolEmitApplyVO == null || StringUtils.isEmpty(abPoolEmitApplyVO.getPayeracc())
                || StringUtils.isEmpty(abPoolEmitApplyVO.getBpId())){
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPool abPool = abPoolService.get(abPoolEmitApplyVO.getBpId());
        BtBankAccDTO bankAccInfo=abAccrecordsService.getBtBankAccInfo(abPoolEmitApplyVO.getPayeracc());
        if (abPool == null || bankAccInfo == null || StringUtils.isEmpty(abPool.getPifId())
                || StringUtils.isEmpty(bankAccInfo.getBifCode())){
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
        BisBifInitDTO bisBifInitInfo = iBisBifInitService.getObjectById(bankAccInfo.getBifCode());
        if(abPoolif == null || bisBifInitInfo == null ||!StringUtils.equals(abPoolif.getBifCode(), bisBifInitInfo.getBifCode())){
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_EMIT_ERROR);
        }
        // 保存信息
        AjaxJson ajaxJson = new AjaxJson();
        String logType = AccebillConstant.ABLOG_LOGTYPE_ADD;
        String msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
        // 保存信息
        this.saveEmitInfo(operate, abPoolEmitApplyVO, user, logType, msg);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    /**
     * 出票申请信息批量提交
     * @param ids 流水表ids
     * @param userVo 当前用户
     */
    public boolean batchSubmit(String ids, ICurrentUser userVo) {
        if (StringUtils.isEmpty(ids)) {
            return false;
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            AbAccrecords abAccrecords = abAccrecordsService.get(id);
            AbPoolDtl abPoolDtl = this.get(abAccrecords.getPdId());
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
            if (AbPoolConstant.AB_POOL_BRIEF_8001.equals(abPoolDtl.getBriefId())) {
                // 当数据为融资申请时，生成新的流水和明细
                this.savePoolDtlAndAccrecord(abAccrecords, abPoolDtl,abVoucherinfoVice, userVo);
            }else {
                // 失败重新提交时重置流水指令信息
                abAccrecords.setOptStatus(null);
                abAccrecords.setReturnMsg(null);
                abAccrecords.setReturnCode(null);
                abAccrecords.setUpdateDate(new Date());
                abAccrecords.setUpdateName(userVo.getUsername());
                abAccrecordsService.updateEntity(abAccrecords);
                abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
                abPoolDtl.setOptStatus(null);
                abPoolDtl.setUpdateId(userVo.getId());
                abPoolDtl.setUpdateTime(new Date());
                this.updateEntity(abPoolDtl);
                // 挂存入审批流
                 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY, id, abVoucherinfoVice, abVoucherinfoVice.getCorpId(), userVo.getId());
                // 生成日志
                abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_001,
                        AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
            }
        }
        return true;
    }
    /**
     * 出票申请信息批量删除
     * @param ids 明细Ids
     * @param userVo 当前用户
     */
    public boolean batchDelete(String ids, ICurrentUser userVo) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            AbPoolDtl abPoolDtl = this.get(id);
            if (AbPoolConstant.AB_POOL_BRIEF_8001.equals(abPoolDtl.getBriefId())) {
                throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_EMITDELETE);
            }
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            abPoolDtl.setUpdateId(userVo.getId());
            abPoolDtl.setUpdateTime(new Date());
            this.updateEntity(abPoolDtl);
            String logType = AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE;
            String msg = AccebillConstant.LANG_LOG_BATCHDELETE_SUCC;
            // 生成日志
            abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_001, logType, msg, userVo);
        }
        return true;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abPoolEmitApplyDao.listApproveDatatable(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据流水ID查询开票信息
     * @param dtlId 明细表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolEmitApplyVO getAbPoolEmitApplyVOByDtlId(String dtlId) {
        if (StringUtils.isEmpty(dtlId)) {
            return null;
        }
        AbPoolEmitApplyVO abPoolEmitApplyVO = abPoolEmitApplyDao.getAbPoolEmitApplyVOInfo(dtlId);
        return abPoolEmitApplyVO;
    }
    /**
     * 提交对来自融资申请的数据进行处理
     * @param abAccrecords 流水信息
     * @param abPoolDtl 明细信息
     */
    public void savePoolDtlAndAccrecord(AbAccrecords abAccrecords, AbPoolDtl abPoolDtl,AbVoucherinfoVice abVoucherinfoVice, ICurrentUser userVo) {
        // 新流水
        AbAccrecords abAccrecordsNew = new AbAccrecords();
        // 新明细表信息
        AbPoolDtl abPoolDtlNew = new AbPoolDtl();
        BeanUtils.copyNotNullProperties(abAccrecords, abAccrecordsNew);
        BeanUtils.copyNotNullProperties(abPoolDtl, abPoolDtlNew);
        // 设置新明细信息
        abPoolDtlNew.setId(null);
        abPoolDtlNew.setOptStatus(null);
        abPoolDtlNew.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
        abPoolDtlNew.setStatus(AbPoolConstant.AB_POOL_STATUS11);
        abPoolDtlNew.setBriefId(AbPoolConstant.AB_POOL_BRIEF_001);
//        String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_EMIT_APPNO);
        abPoolDtlNew.setAppNo(abPoolDtl.getAppNo());
        abPoolDtlNew.setCreateId(userVo.getId());
        abPoolDtlNew.setCreateTime(new Date());
        abPoolDtlNew.setLastid(abPoolDtl.getId());
        this.saveEntity(abPoolDtlNew);
        // 设置新流水信息
        abAccrecordsNew.setId(null);
        abAccrecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        abAccrecordsNew.setStatus(AbPoolConstant.AB_POOL_STATUS11);
        abAccrecordsNew.setApplyDate(new Date());
        abAccrecordsNew.setBusinessDate(new Date());
        abAccrecordsNew.setBriefId(AbPoolConstant.AB_POOL_BRIEF_001);
        abAccrecordsNew.setCreateBy(userVo.getId());
        abAccrecordsNew.setCreateDate(new Date());
        abAccrecordsNew.setPdId(abPoolDtlNew.getId());
        abAccrecordsNew.setLastid(abAccrecords.getId());
        abAccrecordsNew.setOptStatus(null);
        abAccrecordsNew.setReturnCode(null);
        abAccrecordsNew.setReturnMsg(null);
        abAccrecordsService.saveEntity(abAccrecordsNew);
        // 设置老流水和明细为无效
        abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
        abAccrecords.setUpdateBy(userVo.getId());
        abAccrecords.setUpdateDate(new Date());
        abAccrecordsService.updateEntity(abAccrecords);
        abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
        abPoolDtl.setUpdateId(userVo.getId());
        abPoolDtl.setUpdateTime(new Date());
        this.updateEntity(abPoolDtl);
        abVoucherinfoVice.setAppNo(abPoolDtl.getAppNo());
        abVoucherinfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_N);
        abVoucherinfoViceService.updateEntity(abVoucherinfoVice);
        // 挂存入审批流
         getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY, abAccrecordsNew.getId(), abVoucherinfoVice, abVoucherinfoVice.getCorpId(), userVo.getId());
        // 生成日志
        abLogService.saveAbLog(abPoolDtlNew.getVvId(), abPoolDtlNew.getId(), AbPoolConstant.AB_POOL_BRIEF_001,
                AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        AbAccrecords abAccrecords = abAccrecordsService.get(workFlowResult.getBusinessKey());
        AbPoolDtl abPoolDtl = this.get(abAccrecords.getPdId());
        Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
        abPoolDtl.setStatus(status);
        abAccrecords.setStatus(status);
        String logType = "";
        String msg = "";
        if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
            abAccrecords.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abAccrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
            abAccrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
            abAccrecords.setApplyDate(new Date());
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abAccrecords.getVvid());
            abVoucherinfoVice.setType(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
            abVoucherinfoVice.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
            AbVoucherinfo abVoucherinfo = abVoucherinfoService.get(abAccrecords.getVid());
            abVoucherinfo.setBoxStatus(AccebillConstant.AB_BOX_STATUS_Y);
            // 保存从表信息
            abVoucherinfoViceService.updateEntity(abVoucherinfoVice);
            // 保存主表信息
            abVoucherinfoService.updateEntity(abVoucherinfo);
            logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            msg = AccebillConstant.LANG_LOG_APP_SUCC;
        } else {
            logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            msg = AccebillConstant.LANG_LOG_APP_BACK;
        }
        abPoolDtl.setUpdateId(user.getId());
        abPoolDtl.setUpdateTime(new Date());
        this.updateEntity(abPoolDtl);
        abAccrecords.setUpdateDate(new Date());
        abAccrecords.setUpdateBy(user.getId());
        abAccrecordsService.updateEntity(abAccrecords);
        //生成日志
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_001, logType, msg, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    public void saveEmitInfo(String operate, AbPoolEmitApplyVO abPoolEmitApplyVO,
                                 ICurrentUser user, String logType, String msg) {
        // 主表信息
        AbVoucherinfo abVoucherinfo = null;
        // 从表信息
        AbVoucherinfoVice abVoucherinfoVice = null;
        // 流水表信息
        AbAccrecords abAccrecords = null;
        // 票据池明细表
        AbPoolDtl abPoolDtl = null;
        // 明细ID不为空时为修改，否为新增
        if (StringUtils.isNotEmpty(abPoolEmitApplyVO.getDtlId())) {
            logType = AccebillConstant.ABLOG_LOGTYPE_UPDATE;
            msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
            abPoolDtl = this.get(abPoolEmitApplyVO.getDtlId());
            abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
            abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid());
            abAccrecords = abAccrecordsService.get(abPoolEmitApplyVO.getAid());
            abVoucherinfo.setUpdateDate(new Date());
            abVoucherinfo.setUpdateName(user.getUsername());
            abVoucherinfo.setUpdateBy(user.getLoginName());
            abVoucherinfoVice.setUpdateName(user.getUsername());
            abVoucherinfoVice.setUpdateDate(new Date());
            abVoucherinfoVice.setUpdateBy(user.getLoginName());
            abAccrecords.setUpdateName(user.getUsername());
            abAccrecords.setUpdateDate(new Date());
            abAccrecords.setUpdateBy(user.getLoginName());
            abPoolDtl.setUpdateTime(new Date());
            abPoolDtl.setUpdateId(user.getId());
        }else {
            abVoucherinfo = new AbVoucherinfo();
            abVoucherinfoVice = new AbVoucherinfoVice();
            abAccrecords = new AbAccrecords();
            abPoolDtl = new AbPoolDtl();
            // 主表
            abVoucherinfo.setCreateBy(user.getLoginName());
            abVoucherinfo.setCreateDate(new Date());
            abVoucherinfo.setCreateName(user.getUsername());
            // 从表
            abVoucherinfoVice.setCreateBy(user.getLoginName());
            abVoucherinfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
            abVoucherinfoVice.setCreateDate(new Date());
            abVoucherinfoVice.setCreateName(user.getUsername());
            // 流水表
            abAccrecords.setCreateBy(user.getLoginName());
            abAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_001);
            abAccrecords.setCreateDate(new Date());
            abAccrecords.setCreateName(user.getUsername());
            // 票据池明细表
            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS0);
            abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_001);
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_EMIT_APPNO);
            abPoolDtl.setAppNo(appNo);
            abPoolDtl.setCreateId(user.getId());
            abPoolDtl.setCreateTime(new Date());
            abVoucherinfoVice.setAppNo(appNo);
        }
        // 保存各个表数据
        this.setInfo(abPoolEmitApplyVO, abPoolDtl, abVoucherinfo, abVoucherinfoVice, abAccrecords);
        abVoucherinfoService.saveOrUpdate(abVoucherinfo);
        abVoucherinfoVice.setVid(abVoucherinfo.getId());
        abVoucherinfoViceService.saveOrUpdate(abVoucherinfoVice);
        abPoolDtl.setVvId(abVoucherinfoVice.getId());
        abAccrecords.setVid(abVoucherinfo.getId());
        abAccrecords.setVvid(abVoucherinfoVice.getId());
        // 挂审批流
        if ("submit".equals(operate)) {
            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
            abPoolDtl.setOptStatus(null);
            this.saveOrUpdate(abPoolDtl);
            abAccrecords.setPdId(abPoolDtl.getId());
            abAccrecords.setReturnMsg(null);
            abAccrecords.setReturnCode(null);
            abAccrecords.setOptStatus(null);
            abAccrecordsService.saveOrUpdate(abAccrecords);
            // 挂存入审批流
             getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_EMIT_APPLY, abAccrecords.getId(), abVoucherinfoVice, abVoucherinfoVice.getCorpId(), user.getId());
            logType = AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
            msg = AccebillConstant.LANG_LOG_SUBMIT_SUCC;
        }else {
            this.saveOrUpdate(abPoolDtl);
            abAccrecords.setPdId(abPoolDtl.getId());
            abAccrecordsService.saveOrUpdate(abAccrecords);
        }
        // 生成日志
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_001, logType, msg, user);
    }
    /**
     * 设置出票申请信息
     *
     */
    public void setInfo(AbPoolEmitApplyVO abPoolEmitApplyVO, AbPoolDtl abPoolDtl, AbVoucherinfo abVoucherinfo, AbVoucherinfoVice abVoucherinfoVice, AbAccrecords abAccrecords) {
        //保存票据主表信息
        BeanUtils.copyNotNullProperties(abPoolEmitApplyVO, abVoucherinfo);
        //保存票据从表信息
        BeanUtils.copyNotNullProperties(abPoolEmitApplyVO, abVoucherinfoVice);
        // 保存bif_Code信息
        AbPool abPool = abPoolService.get(abPoolEmitApplyVO.getBpId());
        if(abPool == null || StringUtils.isEmpty(abPool.getPifId())){
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
        if(abPoolif == null || StringUtils.isEmpty(abPoolif.getBifCode())){
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        abVoucherinfoVice.setBifCode(abPoolif.getBifCode());
        abVoucherinfoVice.setApplyCorpId(abVoucherinfoVice.getCorpId());
        abVoucherinfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        abVoucherinfoVice.setBankAcc(abPoolEmitApplyVO.getPayeracc());
        abVoucherinfoVice.setBankCode(abPoolEmitApplyVO.getPayerbankno());
        abVoucherinfoVice.setBankName(abPoolEmitApplyVO.getPayerBankName());
        //保存流水表信息
        BeanUtils.copyNotNullProperties(abPoolEmitApplyVO, abAccrecords);
        abAccrecords.setReqAcctId(abPoolEmitApplyVO.getPayeracc());
        abAccrecords.setReqNm(abPoolEmitApplyVO.getPayer());
        abAccrecords.setReqAcctsvcr(abPoolEmitApplyVO.getPayerbankno());
        abAccrecords.setRecAcctId(abPoolEmitApplyVO.getPayeeacc());
        abAccrecords.setRecNm(abPoolEmitApplyVO.getPayee());
        abAccrecords.setBifCode(abPoolif.getBifCode());
        abAccrecords.setRecAcctsvcr(abPoolEmitApplyVO.getPayeebankno());
        //票据池明细信息
        BeanUtils.copyNotNullProperties(abPoolEmitApplyVO, abPoolDtl);
    }
    /**
     * 根据流水ID查询开票信息
     * @param aid 流水表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolEmitApplyVO getAbPoolEmitApplyVOByAid(String aid) {
        if (StringUtils.isEmpty(aid)){
            return null;
        }
        AbAccrecords abAccrecords = abAccrecordsService.get(aid);
        AbPoolEmitApplyVO abPoolEmitApplyVO = abPoolEmitApplyDao.getAbPoolEmitApplyVOInfo(abAccrecords.getPdId());
        return abPoolEmitApplyVO;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS=new ArrayList<>();
            // 单位有额度申领申请单，不允许禁用！
            int num = abPoolLimitApplyDao.getPoolLimitApplyByCorpId(corpId);
            if(num > 0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.BILLPOOL,
                        SysConfigConstant.BusType.POOL_LIMIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLLIMIT_ERROR)));
            }
            // 单位有额度申请复核数据，不允许禁用！
            num = abPoolLimitApplyCheckDao.getPoolLimitApplyCheckByCorpId(corpId);
            if(num > 0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.BILLPOOL,
                        SysConfigConstant.BusType.POOL_LIMIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLLIMIT_CHECK_ERROR)));
            }
            // 单位有额度分配数据，不允许禁用！
            num = abPoolLimitAllotDao.getPoolLimitAllotByCorpId(corpId);
            if(num > 0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.BILLPOOL,
                        SysConfigConstant.BusType.POOL_LIMIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLALLOT_ERROR)));
            }
            // 单位有额度分配核准数据，不允许禁用！
            num = abPoolLimitAllotCheckDao.getPoolLimitAllotCheckByCorpId(corpId);
            if(num > 0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.BILLPOOL,
                        SysConfigConstant.BusType.POOL_LIMIT,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLALLOT_CHECK_ERROR)));
            }
            //单位有保证金申请单，不允许禁用！
            num = abPoolBailDtlDao.getPoolBailDtlByCorpId(corpId);
            if(num > 0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.BILLPOOL,
                        SysConfigConstant.BusType.POOL_BAIL,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLBAIL_ERROR)));
            }
            // 票据池业务信息
            this.verifyBillPoolCorpDisable(corpId, verifyErrorDTOS);
            if(ListUtils.isNotNullOrEmpty(verifyErrorDTOS)){
                return verifyErrorDTOS;
            }
        }
        return null;
    }
    /**
     * 检查票据池业务
     * @param corpId
     * @param verifyErrorDTOS
     * @return
     */
    public void verifyBillPoolCorpDisable(String corpId, List<CorpVerifyErrorDTO> verifyErrorDTOS) {
        //单位有入池申请单，不允许禁用！
        int num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_8018);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_MANAGE,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLMANAGE_ERROR)));
        }
        //单位有出池申请单，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_8019);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_MANAGE,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOL_OUT_ERROR)));
        }
        //单位有质押托管转换申请单，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,new String[]{AbPoolConstant.AB_POOL_BRIEF_8118, AbPoolConstant.AB_POOL_BRIEF_8218});
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_MANAGE,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOL_PDTST_ERROR)));
        }
        // 单位有电票签收中的数据，不允许禁用！
        num = abPoolSignDao.getPoolSignByCorpId(corpId);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_ACCEBILL,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLASIGN_ERROR)));
        }
        // 单位有出票申请中的数据，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_001);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_ACCEBILL,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLACCEBILL_ERROR)));
        }
        // 单位有出票融资申请中的数据，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_8001);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_EMIT_FINA,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLEMITFINA_ERROR)));
        }
        // 单位有贴现申请中的数据，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId,AbPoolConstant.AB_POOL_BRIEF_011);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_ACCEBILL,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLBILLBRIEF_ERROR)));
        }
        // 单位有背书申请中的数据，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId, AbPoolConstant.AB_POOL_BRIEF_010);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_ACCEBILL,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLBILL_BRIEF010_ERROR)));
        }
        // 单位有提示付款中的数据，不允许禁用！
        num = abPoolEmitApplyDao.getValidPoolDtlNumByCorpId(corpId, AbPoolConstant.AB_POOL_BRIEF_020);
        if(num > 0){
            verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                    SysConfigConstant.Module.BILLPOOL,
                    SysConfigConstant.BusType.POOL_ACCEBILL,
                    multiLangServiceI.getLang(CorpVerifyMessageConstant.BILLPOOL_VALID_DISABLE_POOLBILL_BRIEF020_ERROR)));
        }
    }
}
