package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.utils.AbAccebillUtils;
import com.bytter.bill.domain.billPool.dao.AbPoolEmitFinaApplyDao;
import com.bytter.bill.domain.billPool.entity.AbPool;
import com.bytter.bill.domain.billPool.entity.AbPoolContract;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.entity.AbPoolif;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
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
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IInputBankInfoService;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.InputBankInfoDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Clob;
import java.util.*;
/**
 * @author wangzy
 * @Title: AbPoolEmitFinaApplyService
 * @Description: 融资申请Service
 * @date 2020/2/10 18:09
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POOL_EMITFINA_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POOL_EMITFINA_APPLY)
@Service("abPoolEmitFinaApplyService")
@Transactional
public class AbPoolEmitFinaApplyService extends BaseService<AbPoolContract, String> implements IWorkFlowListener, IExcelVerifyHandler<AbPoolEmitFinaApplyVO> {
    @Autowired
    private AbNisBillHeadService abNisBillHeadService;
    @Autowired
    private AbPoolEmitFinaApplyDao abPoolEmitFinaApplyDao;
    @Autowired
    private AbCorpService corpService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbPoolEmitApplyService abPoolEmitApplyService;
    @Autowired
    private AbPoolService abPoolService;
    @Autowired
    private AbPoolifService abPoolifService;
    @DubboReference(check = false)
	private IBisBifInitService iBisBifInitService;
    @Autowired
    private AbDictService abDictService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbBtBankAccService abBtBankAccService;
    @DubboReference(check = false)
    private IInputBankInfoService inputBankInfoService;
    @Autowired
    private AbBisBifInitService abBisBifInitService;
    @Override
    public BaseDao<AbPoolContract, String> getEntityDao() {
        return abPoolEmitFinaApplyDao;
    }
    /**
     * 融资申请列表查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        String bailResource = AbPoolConstant.AB_POLL_EMITFINA_APPLY_RESOURCE_OP;
        List<String> corpAuthList = corpService.getUserCorpIds(bailResource, input.getCurrentUser().getId());
        return abPoolEmitFinaApplyDao.getDataTablesUnion(input, corpAuthList);
    }
    /**
     * 融资申请信息保存或提交
     *
     * @param abPoolEmitFinaApplyVO 信息
     * @param user                  当前用户
     */
    public AjaxJson abPoolEmitFinaApplySaveOrSubmit(String operate, AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO, ICurrentUser user) {
        // 融资申请只支持兴业银行、中信银行
        if (abPoolEmitFinaApplyVO == null || StringUtils.isEmpty(abPoolEmitFinaApplyVO.getPayeracc())
                || StringUtils.isEmpty(abPoolEmitFinaApplyVO.getBpId())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPool abPool = abPoolService.get(abPoolEmitFinaApplyVO.getBpId());
        BtBankAccDTO bankAccInfo = abAccrecordsService.getBtBankAccInfo(abPoolEmitFinaApplyVO.getPayeracc());
        if (abPool == null || bankAccInfo == null || StringUtils.isEmpty(abPool.getPifId())
                || StringUtils.isEmpty(bankAccInfo.getBifCode())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        abPoolEmitFinaApplyVO.setPayerbankno(bankAccInfo.getBankCode());
        AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
        BisBifInitDTO bisBifInitInfo = iBisBifInitService.getObjectById(bankAccInfo.getBifCode());
        if (bisBifInitInfo == null || abPoolif == null
                || !((AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(bisBifInitInfo.getBifCode())
                && AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolif.getBifCode()))
                || (AbPoolConstant.BIF_CODE_CITIC_9001.equals(bisBifInitInfo.getBifCode())
                && AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolif.getBifCode())))) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_EMITFINA_ERROR);
        }
        // 保存信息
        AjaxJson ajaxJson = new AjaxJson();
        String logType = AccebillConstant.ABLOG_LOGTYPE_ADD;
        String msg = AccebillConstant.LANG_LOG_SAVE_SUCC;
        if (abPoolEmitFinaApplyVO != null) {
            // 保存信息
            this.saveEmitFinaInfo(operate, abPoolEmitFinaApplyVO, user, logType, msg);
            ajaxJson.setSuccess(true);
        }
        return ajaxJson;
    }
    /**
     * 融资申请信息批量提交
     *
     * @param ids    明细表ids
     * @param userVo 当前用户
     */
    public boolean batchSubmit(String ids, ICurrentUser userVo) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            AbAccrecords abAccrecords = abAccrecordsService.get(id);
            abAccrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS11);
            // 失败重新提交时重置流水指令信息
            abAccrecords.setOptStatus(null);
            abAccrecords.setReturnMsg(null);
            abAccrecords.setReturnCode(null);
            abAccrecords.setUpdateDate(new Date());
            abAccrecords.setUpdateName(userVo.getUsername());
            AbPoolDtl abPoolDtl = abPoolEmitApplyService.get(abAccrecords.getPdId());
            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
            abPoolDtl.setOptStatus(null);
            abPoolDtl.setUpdateId(userVo.getId());
            abPoolDtl.setUpdateTime(new Date());
            abAccrecordsService.updateEntity(abAccrecords);
            abPoolEmitApplyService.updateEntity(abPoolDtl);
            AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
            // 挂存入审批流
             getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_EMITFINA_APPLY, id, abVoucherinfoVice, abVoucherinfoVice.getCorpId(), userVo.getId());
            // 生成日志
            abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_8001,
                    AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT, AccebillConstant.LANG_LOG_BATCHSUBMIT_SUCC, userVo);
        }
        return true;
    }
    /**
     * 融资申请信息批量删除
     *
     * @param ids    明细Ids
     * @param userVo 当前用户
     */
    public boolean batchDelete(String ids,String reason, ICurrentUser userVo) {
        String[] idArray = ids.split(",");
        AbVoucherinfoVice abVoucherinfoVice = null;
        for (String id : idArray) {
            AbPoolDtl abPoolDtl = abPoolEmitApplyService.get(id);
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            abPoolDtl.setUpdateId(userVo.getId());
            abPoolDtl.setUpdateTime(new Date());
            abPoolEmitApplyService.updateEntity(abPoolDtl);
            abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
            // 付款打回
            if (abVoucherinfoVice != null && StringUtils.isNotEmpty(abVoucherinfoVice.getVoucherId())
                    && StringUtils.isNotEmpty(reason)) {
                abNisBillHeadService.updateNisBillhead(abVoucherinfoVice.getVoucherId(),
                        NetbankConstant.STATUE_BACK, reason,userVo);
            }
            String logType = AccebillConstant.ABLOG_LOGTYPE_BATCHDELETE;
            String msg = AccebillConstant.LANG_LOG_BATCHDELETE_SUCC;
            // 生成日志
            abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_8001, logType, msg, userVo);
        }
        return true;
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput listApproveDatatable(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POOL_EMITFINA_APPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        DataTablesOutput dp = abPoolEmitFinaApplyDao.listApproveDatatable(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据流水ID查询开票信息
     *
     * @param dtlId 明细表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolEmitFinaApplyVO getAbPoolEmitFinaApplyVOByDtlId(String dtlId) {
        if (StringUtils.isEmpty(dtlId)) {
            return null;
        }
        AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO = abPoolEmitFinaApplyDao.getAbPoolEmitFinaApplyVOInfo(dtlId);
        return abPoolEmitFinaApplyVO;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        AbAccrecords abAccrecords = abAccrecordsService.get(workFlowResult.getBusinessKey());
        AbPoolDtl abPoolDtl = abPoolEmitApplyService.get(abAccrecords.getPdId());
        Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
        abPoolDtl.setStatus(status);
        abAccrecords.setStatus(status);
        String logType = "";
        String msg = "";
        if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
            logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            msg = AccebillConstant.LANG_LOG_APP_SUCC;
            // 指令状态变为待发送
            abPoolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abAccrecords.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
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
        } else {
            logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            msg = AccebillConstant.LANG_LOG_APP_BACK;
        }
        abPoolDtl.setUpdateId(user.getId());
        abPoolDtl.setUpdateTime(new Date());
        abPoolEmitApplyService.updateEntity(abPoolDtl);
        // 流水表
        abAccrecords.setUpdateBy(user.getId());
        abAccrecords.setUpdateDate(new Date());
        abAccrecordsService.updateEntity(abAccrecords);
        //生成日志
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_8001, logType, msg, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 保存融资申请信息
     */
    public void saveEmitFinaInfo(String operate, AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO,
                                 ICurrentUser user, String logType, String msg) {
        // 主表信息
        AbVoucherinfo abVoucherinfo = null;
        // 从表信息
        AbVoucherinfoVice abVoucherinfoVice = null;
        // 流水表信息
        AbAccrecords abAccrecords = null;
        // 票据池明细表
        AbPoolDtl abPoolDtl = null;
        // id不为空时为修改
        if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getDtlId())) {
            logType = AccebillConstant.ABLOG_LOGTYPE_UPDATE;
            msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
            abPoolDtl = abPoolEmitApplyService.get(abPoolEmitFinaApplyVO.getDtlId());
            abVoucherinfoVice = abVoucherinfoViceService.get(abPoolDtl.getVvId());
            abVoucherinfo = abVoucherinfoService.get(abVoucherinfoVice.getVid());
            abAccrecords = abAccrecordsService.get(abPoolEmitFinaApplyVO.getAid());
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
        } else {
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
            if(StringUtils.isEmpty(abPoolEmitFinaApplyVO.getSource())){
                //开票数据来源--录入
                abVoucherinfoVice.setSource(AccebillConstant.AB_ACCEBILL_SOURCE11);
            }
            // 流水表
            abAccrecords.setCreateBy(user.getLoginName());
            abAccrecords.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8001);
            abAccrecords.setCreateDate(new Date());
            abAccrecords.setCreateName(user.getUsername());
            // 票据池明细表
            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS0);
            abPoolDtl.setBriefId(AbPoolConstant.AB_POOL_BRIEF_8001);
            abPoolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_POOL_EMITFINA_APPNO);
            abPoolDtl.setAppNo(appNo);
            abPoolDtl.setCreateId(user.getId());
            abPoolDtl.setCreateTime(new Date());
            abVoucherinfoVice.setAppNo(appNo);
        }
        // 保存各个表数据
        this.setInfo(abPoolEmitFinaApplyVO, abPoolDtl, abVoucherinfo, abVoucherinfoVice, abAccrecords);
        abVoucherinfoService.saveOrUpdate(abVoucherinfo);
        abVoucherinfoVice.setVid(abVoucherinfo.getId());
        abVoucherinfoViceService.saveOrUpdate(abVoucherinfoVice);
        abPoolDtl.setVvId(abVoucherinfoVice.getId());
        abAccrecords.setVid(abVoucherinfo.getId());
        abAccrecords.setVvid(abVoucherinfoVice.getId());
        // 挂审批流
        if (AccebillConstant.BILL_OPERATE_SUBMIT.equals(operate)) {
            abPoolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
            abPoolDtl.setOptStatus(null);
            abPoolEmitApplyService.saveOrUpdate(abPoolDtl);
            abAccrecords.setPdId(abPoolDtl.getId());
            abAccrecords.setStatus(AbPoolConstant.AB_POOL_STATUS11);
            abAccrecords.setReturnMsg(null);
            abAccrecords.setReturnCode(null);
            abAccrecords.setOptStatus(null);
            abAccrecordsService.saveOrUpdate(abAccrecords);
            // 挂存入审批流
             getStartHandler().handler(BillPoolWorkFlowConstant.AB_POOL_EMITFINA_APPLY, abAccrecords.getId(), abVoucherinfoVice, abVoucherinfoVice.getCorpId(), user.getId());
            logType = AccebillConstant.ABLOG_LOGTYPE_SUBMIT;
            msg = AccebillConstant.LANG_LOG_SUBMIT_SUCC;
        } else {
            abPoolEmitApplyService.saveOrUpdate(abPoolDtl);
            abAccrecords.setPdId(abPoolDtl.getId());
            abAccrecordsService.saveOrUpdate(abAccrecords);
        }
        abPoolEmitFinaApplyVO.setId(abAccrecords.getId());
        // 生成日志
        abLogService.saveAbLog(abPoolDtl.getVvId(), abPoolDtl.getId(), AbPoolConstant.AB_POOL_BRIEF_8001, logType, msg, user);
    }
    /**
     * 设置融资申请信息
     */
    public void setInfo(AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO, AbPoolDtl abPoolDtl, AbVoucherinfo abVoucherinfo,
                        AbVoucherinfoVice abVoucherinfoVice, AbAccrecords abAccrecords) {
        //保存票据主表信息
        BeanUtils.copyNotNullProperties(abPoolEmitFinaApplyVO, abVoucherinfo);
        abVoucherinfo.setPayerbankno(abPoolEmitFinaApplyVO.getPayerbankno());
        //保存票据从表信息
        BeanUtils.copyNotNullProperties(abPoolEmitFinaApplyVO, abVoucherinfoVice);
        // 保存bif_Code信息
        AbPool abPool = abPoolService.get(abPoolEmitFinaApplyVO.getBpId());
        if (abPool == null || StringUtils.isEmpty(abPool.getPifId())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
        if (abPoolif == null || StringUtils.isEmpty(abPoolif.getBifCode())) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        String bifCode=	abPoolif.getBifCode();
        BisBifInitDTO bifInfo=abBisBifInitService.getByBifCode(bifCode);
        if(bifInfo!=null){
            abVoucherinfoVice.setBifCode(bifInfo.getId());
        }
        abVoucherinfoVice.setApplyCorpId(abVoucherinfoVice.getCorpId());
        abVoucherinfoVice.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
        abVoucherinfoVice.setBankAcc(abPoolEmitFinaApplyVO.getPayeracc());
        abVoucherinfoVice.setBankCode(abPoolEmitFinaApplyVO.getPayerbankno());
        abVoucherinfoVice.setBankName(abPoolEmitFinaApplyVO.getPayerBankName());
        abVoucherinfoVice.setVoucherId(abPoolEmitFinaApplyVO.getVoucherId());
        //保存流水表信息
        BeanUtils.copyNotNullProperties(abPoolEmitFinaApplyVO, abAccrecords);
        abAccrecords.setBifCode(abPoolif.getBifCode());
        abAccrecords.setReqAcctId(abPoolEmitFinaApplyVO.getPayeracc());
        abAccrecords.setReqNm(abPoolEmitFinaApplyVO.getPayer());
        abAccrecords.setReqAcctsvcr(abPoolEmitFinaApplyVO.getPayerbankno());
        abAccrecords.setRecAcctId(abPoolEmitFinaApplyVO.getPayeeacc());
        abAccrecords.setRecNm(abPoolEmitFinaApplyVO.getPayee());
        abAccrecords.setRecAcctsvcr(abPoolEmitFinaApplyVO.getPayeebankno());
        //票据池明细信息
        BeanUtils.copyNotNullProperties(abPoolEmitFinaApplyVO, abPoolDtl);
    }
    /**
     * @param conId 合同ID
     * @param user  用户
     * @return 处理结果
     * @throws
     * @Title: 重新生成合同
     */
    public AjaxJson regenerateContract(String conId, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(conId)) {
            AbPoolContract abPoolContract = this.get(conId);
            abPoolContract.setStatus(AbPoolConstant.AB_POOL_CONTRACT_STATUS2);
            abPoolContract.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abPoolContract.setUpdateTime(new Date());
            abPoolContract.setUpdateId(user.getId());
            this.saveOrUpdate(abPoolContract);
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 合同确认
     *
     * @param id   合同id
     * @param user 当前用户
     * @return 处理结果
     */
    public AjaxJson contractConfirm(String id, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        if (StringUtils.isNotEmpty(id)) {
            AbPoolContract abPoolContract = this.get(id);
            abPoolContract.setStatus(AbPoolConstant.AB_POOL_CONTRACT_STATUS1);
            abPoolContract.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            abPoolContract.setUpdateTime(new Date());
            abPoolContract.setUpdateId(user.getId());
            this.saveOrUpdate(abPoolContract);
            reJson.setSuccess(true);
        }
        return reJson;
    }
    /**
     * 质权人信息
     *
     * @param bpId 票据池id
     * @return 处理结果
     */
    public AbPoolEmitFinaApplyVO selectPledgee(String bpId) {
        return abPoolEmitFinaApplyDao.selectPledgee(bpId);
    }
    /**
     * 合同查看
     *
     * @param id 合同id
     * @return 处理结果
     */
    public void viewContract(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
        AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO = abPoolEmitFinaApplyDao.viewContract(id);
        Clob conInfo = abPoolEmitFinaApplyVO.getConInfo();
        String name = abPoolEmitFinaApplyVO.getConName() + "." + abPoolEmitFinaApplyVO.getConFtype();
        if (StringUtils.isEmpty(name) || !StringUtils.isNotEmpty(conInfo)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        }
        // 转码，免得文件名中文乱码
        String filename = URLEncoder.encode(name, "UTF-8");
        try {
            Reader info = conInfo.getCharacterStream();
            BufferedReader br = new BufferedReader(info);
            String s = br.readLine();
            StringBuffer sb = new StringBuffer();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
            }
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(Base64Utils.decodeFromString(sb.toString()));
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response.getOutputStream() != null) {
                response.getOutputStream().close();
            }
        }
    }
    /**
     * 根据流水ID查询开票信息
     *
     * @param aid 流水表Id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolEmitFinaApplyVO getAbPoolEmitFinaApplyVOByAid(String aid) {
        if (StringUtils.isEmpty(aid)) {
            return null;
        }
        AbAccrecords abAccrecords = abAccrecordsService.get(aid);
        AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO = abPoolEmitFinaApplyDao.getAbPoolEmitFinaApplyVOInfo(abAccrecords.getPdId());
        return abPoolEmitFinaApplyVO;
    }
    /**
     * 根据明细表Id查询合同信息
     *
     * @param dtlId 明细表Id
     * @return
     */
    @Transactional(readOnly = true)
    public List<AbPoolEmitFinaApplyVO> listConInfo(String dtlId) {
        if (StringUtils.isEmpty(dtlId)) {
            return null;
        }
        return abPoolEmitFinaApplyDao.listConInfo(dtlId);
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO) {
        return null;
    }
    /**
     * 导入校验
     *
     * @param list 校验数据
     */
    @Override
    @Transactional(readOnly = true)
    public void verifyHandler(List<AbPoolEmitFinaApplyVO> list) {
        if (list != null && !list.isEmpty()) {
            List<String> errorMsgList = null;
            BtBankAccDTO btBankAccDTO = null;
            CorpDTO corpDTO = null;
            for (AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO : list) {
                errorMsgList = new ArrayList<>();
                //默认票据类型为银票，票据介质为电票
                abPoolEmitFinaApplyVO.setBillType(Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK));
                abPoolEmitFinaApplyVO.setBillFlag(Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
                abPoolEmitFinaApplyVO.setBillFlagName(AccebillConstant.AB_BILL_FLAG_ELECT);
                /** 所属单位代码和所属单位名称校验 */
                if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getCorpName())) {
                    errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_CORP_CODEANDNAME_NOTNULL);
                } else {
                    if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getCorpCode())) {
                        // 根据所属单位代码获取单位信息
                        corpDTO = abCorpService.getCorpByCode(abPoolEmitFinaApplyVO.getCorpCode());
                        if (corpDTO != null) {
                            if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getCorpName())
                                    && !abPoolEmitFinaApplyVO.getCorpName().equals(corpDTO.getName())) {
                                errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPNAME_ISNOT);
                            } else {
                                abPoolEmitFinaApplyVO.setCorpName(corpDTO.getName());
                            }
                        } else {
                            errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_CORPCODE_ERROR);
                        }
                    } else {
                        // 只填单位名称
                        corpDTO = abCorpService.getCorpIdByName(abPoolEmitFinaApplyVO.getCorpName());
                        if (corpDTO == null) {
                            errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_CORPID_ISNOTEXIST);
                        } else {
                            abPoolEmitFinaApplyVO.setCorpCode(corpDTO.getCode());
                        }
                    }
                }
                /** 根据corpName获取corpId */
                if (corpDTO != null) {
                    abPoolEmitFinaApplyVO.setCorpId(corpDTO.getId());
                }
                /** 票据池校验 */
                Map<String, Object> params = new HashMap<>();
                if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getPbCodeAndName())) {
                    params.put("bpName", abPoolEmitFinaApplyVO.getPbCodeAndName());
                }
                params.put("validSign", new String[]{AbPoolConstant.AB_POLL_VALID_SIGN_0, AbPoolConstant.AB_POLL_VALID_SIGN_1});
                List<AbPoolVO> abPoolVOList = abPoolService.getAbPoolVOByParams(params);
                if (abPoolVOList.isEmpty() || !StringUtils.isNotEmpty((abPoolVOList.get(0)))) {
                    errorMsgList.add(AccebillConstant.AB_BILLPOOL_EXCEL_ERRORMSG_PBCODEANDNAME_ISNOTEXIST);
                } else {
                    abPoolEmitFinaApplyVO.setBpId(abPoolVOList.get(0).getId());
                }
                /** 申请人账号校验 */
                if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getCorpId())
                        && StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getPayeracc())) {
                    btBankAccDTO = getBtBankAccDTOExcel(abPoolEmitFinaApplyVO.getPayeracc());
                    if (StringUtils.isNotEmpty(btBankAccDTO)) {
                        if (abPoolEmitFinaApplyVO.getCorpId().equals(btBankAccDTO.getCorpId())) {
                            /** 融资申请只支持兴业银行 */
                            if (StringUtils.isNotEmpty(abPoolEmitFinaApplyVO.getBpId())) {
                                AbPool abPool = abPoolService.get(abPoolEmitFinaApplyVO.getBpId());
                                if (abPool == null || StringUtils.isEmpty(abPool.getPifId()) || StringUtils.isEmpty(btBankAccDTO.getBifCode())) {
                                    errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERACC_ISNOTEXIST);
                                } else {
                                    AbPoolif abPoolif = abPoolifService.get(abPool.getPifId());
                                    BisBifInitDTO bisBifInitInfo = iBisBifInitService.getObjectById(btBankAccDTO.getBifCode());
                                    if (bisBifInitInfo == null || abPoolif == null || !(AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(bisBifInitInfo.getBifCode())
                                            && AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolif.getBifCode()))) {
                                        errorMsgList.add(AccebillConstant.AB_BILLPOOL_EXCEL_ERRORMSG_EMITFINA_ERROR);
                                    }
                                }
                                abPoolEmitFinaApplyVO.setPayer(btBankAccDTO.getAccName());
                                abPoolEmitFinaApplyVO.setPayerBankName(btBankAccDTO.getBankName());
                                abPoolEmitFinaApplyVO.setPayerbankno(btBankAccDTO.getBankCode());
                            }
                        } else {
                            errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERCORP_MATCHERROR);
                        }
                    } else {
                        errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_PAYERACC_ISNOTEXIST);
                    }
                }
                /** 日期校验:开票日期，到期日期，业务办理日期校验 */
                if (abPoolEmitFinaApplyVO.getStartDate() != null && abPoolEmitFinaApplyVO.getEndDate() != null) {
                    if (abPoolEmitFinaApplyVO.getStartDate().getTime() >= abPoolEmitFinaApplyVO.getEndDate().getTime()) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_COMSTARTTOEND);
                    }
                }
                /** 金额校验 */
                if (abPoolEmitFinaApplyVO.getBillMoney() != null) {
                    // 金额最小为0.01
                    if (abPoolEmitFinaApplyVO.getBillMoney().compareTo(BigDecimal.valueOf(0.01)) < 0) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_BILLMONEYMIX);
                    }
                    // 电票不能超过10亿
                    if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(abPoolEmitFinaApplyVO.getBillFlagName())
                            && (abPoolEmitFinaApplyVO.getBillMoney().compareTo(BigDecimal.valueOf(1000000000.00)) > 0)) {
                        errorMsgList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLMONEY_BILLFLAG11);
                    }
                    // 纸票金额不能超过设置的额度
                    if (AccebillConstant.AB_BILL_FLAG_PAPER.equals(abPoolEmitFinaApplyVO.getBillFlagName())
                            && (abPoolEmitFinaApplyVO.getBillMoney().compareTo(abVoucherinfoViceService.getMaxBillMoney()) > 0)) {
                        errorMsgList.add(AccebillConstant.TABLE_ABVOUCHERINFO_BILLFLAG10_MAXBILLMONEY);
                    }
                }
                /** 收票人账号校验 */
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(abPoolEmitFinaApplyVO.getBillFlagName())
                        && StringUtils.isEmpty(abPoolEmitFinaApplyVO.getPayeeacc())
                        && !AbAccebillUtils.isBillTypeTP(abPoolEmitFinaApplyVO.getBillType())) {
                    errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_PAYEEACC_BILLFLAG11_ISNOTNULL);
                }
                /** 承兑人校验 */
                if (AccebillConstant.AB_BILL_FLAG_ELECT.equals(abPoolEmitFinaApplyVO.getBillFlagName())
                        && !AbAccebillUtils.isBillTypeTP(abPoolEmitFinaApplyVO.getBillType())) {
                    // 电票且非第三方票据类型时承兑人不能为空
                    if (AccebillConstant.AB_BILL_TYPE_MERCH.equals(String.valueOf(abPoolEmitFinaApplyVO.getBillType()))) {
                        if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptor())) {
                            abPoolEmitFinaApplyVO.setAcceptor(abPoolEmitFinaApplyVO.getPayer());
                            abPoolEmitFinaApplyVO.setAcceptoracc(abPoolEmitFinaApplyVO.getPayeracc());
                            abPoolEmitFinaApplyVO.setAcceptorbankno(abPoolEmitFinaApplyVO.getPayerbankno());
                            abPoolEmitFinaApplyVO.setAcceptorBankName(abPoolEmitFinaApplyVO.getPayerBankName());
                        }
                    }
                    // 电票、银票，且承兑行名称不为空
                    if (!StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptor())
                            && AccebillConstant.AB_BILL_TYPE_BANK.equals(String.valueOf(abPoolEmitFinaApplyVO.getBillType()))) {
                        InputBankInfoDTO inputBankInfoDTO = inputBankInfoService.getByBankName(abPoolEmitFinaApplyVO.getAcceptorBankName());
                        if (inputBankInfoDTO == null) {
                            errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNULL);
                        } else {
                            abPoolEmitFinaApplyVO.setAcceptorbankno(inputBankInfoDTO.getBankCode());
                            abPoolEmitFinaApplyVO.setAcceptor(inputBankInfoDTO.getBankName());
                        }
                    }
                    if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptoracc())) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORACC_BILLFLAG11_ISNOTNULL);
                    }
                    if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptor())) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTOR_ISNOTNULL);
                    }
                    if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptorbankno())) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNO_BILLFLAG11_ISNOTNULL);
                    }
                    if (StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptorBankName())) {
                        errorMsgList.add(AccebillConstant.ACCEBILL_MESSAGE_ACCEPTORBANKNAME_BILLFLAG11_ISNOTNULL);
                    }
                }
                /** 不得转让标记校验 */
                if (AccebillConstant.AB_BANEM_Y.equals(abPoolEmitFinaApplyVO.getBanendrsmtmkName())
                        || AccebillConstant.AB_BANEM_N.equals(abPoolEmitFinaApplyVO.getBanendrsmtmkName())) {
                    abPoolEmitFinaApplyVO.setBanendrsmtmk(abPoolEmitFinaApplyVO.getBanendrsmtmkName());
                } else {
                    errorMsgList.add(AccebillConstant.AB_BILL_EXCEL_ERRORMSG_BANENDRSMTMK_ISNOTEXIST);
                }
                //当票据为银票时，且承兑人信息为空，则默认把出票人开户银行信息填入承兑人信息中
                if (AccebillConstant.AB_BILL_TYPE_BANK.equals(abPoolEmitFinaApplyVO.getBillTypeName())
                        && StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptor())
                        && StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptoracc())
                        && StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptorbankno())
                        && StringUtils.isEmpty(abPoolEmitFinaApplyVO.getAcceptorBankName())) {
                    abPoolEmitFinaApplyVO.setAcceptor(abPoolEmitFinaApplyVO.getPayerBankName());
                    abPoolEmitFinaApplyVO.setAcceptoracc("0");
                    abPoolEmitFinaApplyVO.setAcceptorbankno(abPoolEmitFinaApplyVO.getPayerbankno());
                    abPoolEmitFinaApplyVO.setAcceptorBankName(abPoolEmitFinaApplyVO.getPayerBankName());
                }
                abPoolEmitFinaApplyVO.addErrorMsg(errorMsgList);
            }
        }
    }
    /**
     * 根据账号获取账户信息,用于EXCEL导入校验--票据专户
     *
     * @return
     */
    @Transactional(readOnly = true)
    public BtBankAccDTO getBtBankAccDTOExcel(String bankAcc) {
        BtBankAccDTO bankAccDTO = null;
        BtBankAccDTO params = new BtBankAccDTO();
        params.setBankAcc(bankAcc);
        params.setElectricBill("1");
        List<BtBankAccDTO> bankaccInfoList = abBtBankAccService.getBankAccList(params);
        if (bankaccInfoList != null && !bankaccInfoList.isEmpty()) {
            bankAccDTO = bankaccInfoList.get(0);
        }
        return bankAccDTO;
    }
    /**
     * 导入功能
     *
     * @param list
     */
    public void saveImport(List<AbPoolEmitFinaApplyVO> list, ICurrentUser userVo) {
        if (list != null && !list.isEmpty()) {
            for (AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO : list) {
                String optStatus = "add";
                this.abPoolEmitFinaApplySaveOrSubmit(optStatus, abPoolEmitFinaApplyVO, userVo);
            }
        }
    }
}
