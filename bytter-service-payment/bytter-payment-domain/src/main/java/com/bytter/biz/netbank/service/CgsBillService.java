package com.bytter.biz.netbank.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.biz.netbank.dao.CgsBillDao;
import com.bytter.biz.netbank.entity.CgsBill;
import com.bytter.biz.netbank.vo.CgsBillVO;
import com.bytter.biz.payment.api.constant.CgsBillConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 委托收款
 * @date 2017-05-26 11:14:58
 */
@WorkFlow({
        NisWorkFlowConstant.NIS_CGSBILL_PAYEE,
        NisWorkFlowConstant.NIS_CGSBILL_PAYER})
@Service("cgsBillService")
@Transactional
public class CgsBillService extends BaseService<CgsBill, String> implements IWorkFlowListener, IExcelVerifyHandler<CgsBillVO> {
    @Autowired
    private CgsBillDao cgsBillDao;
    @DubboReference(check = false)
	private IConfigService configService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
	private IInnerAccountService iInnerAccountService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private ICorpService iCorpService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IDictService iDictService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(CgsBill entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CgsBill entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CgsBill entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(CgsBill entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<CgsBill, String> getEntityDao() {
        return cgsBillDao;
    }
    /**
     * 计算手续费
     *
     * @param money
     * @return
     */
    public BigDecimal chargeMoney(BigDecimal money) {
        String chargeRate = configService.getConfig("nis", "chargeRate").getValue();
        BigDecimal chargeMoney = money.multiply(new BigDecimal(chargeRate))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        return chargeMoney;
    }
    /**
     * 保存付款单
     *
     * @param entity
     * @param flag
     */
    public void saveCgsBill(CgsBill entity, boolean flag, ICurrentUser user) {
        entity.setChargeMoney(this.chargeMoney(entity.getMoney()));
        if (CommonConstant.STATUE_TEMP.equals(entity.getStatus()) && !flag) {//flag标志,false为暂存，true为提交。为区别暂存，批量提交
            entity.setStatus(CgsBillConstant.STATUE_TEMP);
        } else {
            entity.setStatus(CgsBillConstant.STATUE_WAIT_CHECK);
        }
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        entity.setGatherAuditState(0);
        entity.setSignFlag(0);
        entity.setConfirmAuditState(0);
        entity.setTallyFlag(0);
        entity.setFinishFlag(0);
        super.saveEntity(entity);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        //启动流程
        if (CgsBillConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(NisWorkFlowConstant.NIS_CGSBILL_PAYEE, entity.getId(), entity, entity.getPayeeCorpId(), user.getId());
        }
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_CGS, user);
    }
    /**
     * 编辑付款单
     *
     * @param cgsBill
     */
    public void updateCgsBill(CgsBill cgsBill, ICurrentUser user) {
        CgsBill entity = cgsBillDao.get(cgsBill.getId());
        BeanUtils.copyNotNullProperties(cgsBill, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setGatherAuditState(0);
        entity.setSignFlag(0);
        entity.setConfirmAuditState(0);
        entity.setTallyFlag(0);
        entity.setFinishFlag(0);
        this.saveOrUpdate(entity);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        //启动流程
        if (CgsBillConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
             getStartHandler().handler(NisWorkFlowConstant.NIS_CGSBILL_PAYEE, entity.getId(), entity, entity.getPayeeCorpId(), user.getId());
        }
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, "", NisMessageConstant.BUS_TYPE_CGS, user);
    }
    /**
     * 逻辑删除付款单
     */
    public void deleteCgsBill(String id, ICurrentUser user) {
        CgsBill cgsBill = this.get(id);
        cgsBill.setStatus(CgsBillConstant.STATUE_DEL);
        cgsBill.setUpdateBy(user.getLoginName());
        cgsBill.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(cgsBill);
        //生成日志
        nisMessageService.saveMessage(cgsBill.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, "", NisMessageConstant.BUS_TYPE_PAY, user);
    }
    /**
     * 查询内部托收单据列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<CgsBillVO> findListBysql(DataTablesInput input, ICurrentUser user) {
        String opType = (String) input.getFormdata().get("opType");
        StringBuilder sql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(iCorpService.getUserCorpIds(ResourceConstant.NETBANK_NETBANK_CGSBILL_QUERY, user.getId()), "s.id", true);
        sql.append(corpAuthSql);
        if (!"query".equals(opType)) {
            sql.append(" and c.create_by ='").append(user.getLoginName()).append("'");
        }
        return cgsBillDao.findListBysql(input, sql.toString());
    }
    /**
     * 根据id查找内部托收单据
     *
     * @param id
     * @return
     */
    public CgsBillVO getByUnion(String id) {
        return cgsBillDao.getByUnion(id);
    }
    /**
     * 账号列表页面
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput<FcAccDTO> listByPayeeAccDetail(DataTablesInput dataTablesInput) {
        String corpIdTemp = (String) dataTablesInput.getPara("payeeCorpId");
        List<String> corpId = new ArrayList<String>();
        corpId.add(corpIdTemp);
        List<FcAccDTO> fcAccInfo = iInnerAccountService.listFcAccInFoByCorpIds(corpId, "N");
        DataTablesOutput<FcAccDTO> output = new DataTablesOutput();
        output.setData(fcAccInfo);
        return output;
    }
    
    /**
     * 账号列表详细页面
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput<FcAccListVO> listMulAccInfoDetail(DataTablesInput dataTablesInput) {
        DataTablesOutput output = fcAccService.getFcAcc(dataTablesInput);
        List<FcAccListVO> fcAccListVOS = output.getData();
        List<String> netIds = fcAccListVOS.stream().map(FcAccListVO::getNetId).distinct().collect(Collectors.toList());
        List<String> corpIds = fcAccListVOS.stream().map(FcAccListVO::getCorpId).distinct().collect(Collectors.toList());
        List<String> accTypes = fcAccListVOS.stream().map(FcAccListVO::getAccType).collect(Collectors.toList());
        Set<String> curIds = fcAccListVOS.stream().map(FcAccListVO::getCurId).collect(Collectors.toSet());
        corpIds.addAll(netIds);
        corpIds = corpIds.stream().distinct().collect(Collectors.toList());
        Map<String, String> corpNameMap = iCorpService.getNameByIds(corpIds);
        Map<String, String> accTypeNameMap;
        if (!accTypes.isEmpty()){
            accTypeNameMap = iDictService.getNameByTypeCodesAndTypeGroupCode(accTypes, "InnerAccount.AccType");
        } else {
            accTypeNameMap = new HashMap<>();
        }

        Map<String, String> currencyNameMap = iBtCurrencyService.getCurrencyNameByIds(new ArrayList<>(curIds));
        fcAccListVOS.forEach(a -> {
            if (ObjectUtils.isNotNull(corpNameMap)){
                a.setNetName(corpNameMap.get(a.getNetId()));
            }
            if (ObjectUtils.isNotNull(corpNameMap)){
                a.setCorpName(corpNameMap.get(a.getCorpId()));
            }
            if (ObjectUtils.isNotNull(accTypeNameMap)){
                a.setAccTypeName(accTypeNameMap.get(a.getAccType()));
            }
            if (ObjectUtils.isNotNull(currencyNameMap)){
                a.setCurName(currencyNameMap.get(a.getCurId()));
            }
        });
        return output;
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param ids
     * @return
     */
    public DataTablesOutput<CgsBillVO> dataTableApprove(DataTablesInput dataTablesInput, List<String> ids) {
        return this.cgsBillDao.dataTableApprove(dataTablesInput, ids);
    }
    /**
     * 审批回调方法
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        if (NisWorkFlowConstant.NIS_CGSBILL_PAYEE.equals(workFlowResult.getProcessDefinitionKey())) {
            this.approvePassEndPayee(workFlowResult.getBusinessKey(), workFlowResult.isPass(), "", user);
        } else {
            this.approvePassEndPayer(workFlowResult.getBusinessKey(), workFlowResult.isPass(), "", user);
        }
    }
    /**
     * 启动时处理
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    	CgsBill entity=new CgsBill();
    	if(StringUtils.isNotEmpty(workFlowResult.getBusinessKey())){
    		 entity = this.get(workFlowResult.getBusinessKey(), true);
    	}
        if (NisWorkFlowConstant.NIS_CGSBILL_PAYEE.equals(workFlowResult.getProcessDefinitionKey())) {
            entity.setGatherAuditStep(0);// 审核步骤为0(初始化)
            entity.setGatherAuditState(CgsBillConstant.BILL_GATHER_AUDIT_STATE_WAIT);//收款先审批，状态为审批中审核状态(委托收款)
            entity.setConfirmAuditStep(0);//当前审核步骤(付款确认)
            entity.setConfirmAuditState(0);//审核状态(付款确认)
            entity.setJudgeResult(0);  //安全认证结果
            entity.setSignFlag(0);//签收标识
            entity.setTallyFlag(0);//记账标识
            entity.setFinishFlag(0);//业务结束标志
        }
    }
    /**
     * 根据业务id查找托收单据
     *
     * @param buId
     * @return
     */
    public CgsBill getCgsBill(String buId) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        CgsBill cgsBill = this.get(buId, true);
        if (cgsBill == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        return cgsBill;
    }
    /**
     * 收款审批完成处理，启动付款审批
     *
     * @param buId
     * @param isPassOK
     * @param appMsg
     */
    public void approvePassEndPayee(String buId, boolean isPassOK, String appMsg, ICurrentUser user) {
        CgsBill entity = getCgsBill(buId);
        Integer status = isPassOK ? CgsBillConstant.STATUE_CHECK : CgsBillConstant.STATUE_BACK;
        entity.setStatus(status);
        if (CgsBillConstant.STATUE_CHECK.equals(entity.getStatus())) {
            entity.setGatherAuditState(3);
            entity.setStatus(12);
            entity.setGatherAuditDate(DateUtils.getDate());
            entity.setConfirmAuditStep(0);//付款审批开始
            entity.setConfirmAuditState(2);//付款审批中
            this.save(entity);
             getStartHandler().handler(NisWorkFlowConstant.NIS_CGSBILL_PAYER, entity.getId(), entity, entity.getPayerCorpId(), user.getId());
        }
        String replyDays = configService.getConfig("nis", "replyDays").getValue();//最多等待
        String noReplyAct = configService.getConfig("nis", "noReplyAct").getValue();//无答复时， 0拒绝付款  1同意付款
        Integer messageType = isPassOK ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, appMsg, NisMessageConstant.BUS_TYPE_CGS, user);
    }
    /**
     * 付款审批完成处理
     *
     * @param buId
     * @param isPassOK
     * @param appMsg
     */
    public void approvePassEndPayer(String buId, boolean isPassOK, String appMsg, ICurrentUser user) {
        if (StringUtils.isEmpty(buId)) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        CgsBill cgsBill = this.get(buId, true);
        if (cgsBill == null) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        Integer status = isPassOK ? CgsBillConstant.STATUE_CHECK : CgsBillConstant.STATUE_BACK;
        cgsBill.setStatus(status);
        cgsBill.setConfirmAuditState(3);//审核状态(付款确认)
        cgsBill.setConfirmAuditDate(DateUtils.getDate());
        Integer messageType = isPassOK ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        //生成日志
        nisMessageService.saveMessage(cgsBill.getId(), messageType, appMsg, NisMessageConstant.BUS_TYPE_CGS, user);
    }
    /**
     * 导入保存
     *
     * @param list
     */
    public void saveImport(List<CgsBillVO> list, ICurrentUser user) {
        String chargeRate = configService.getConfig("nis", "chargeRate").getValue();
        for (CgsBillVO cbVO : list) {
            BigDecimal money = cbVO.getMoney();
            BigDecimal chargeMoney = money.multiply(new BigDecimal(chargeRate)).divide(new BigDecimal(100)).setScale(2,
                    BigDecimal.ROUND_HALF_UP);
            CgsBill cb = new CgsBill();
            BeanUtils.copyNotNullProperties(cbVO, cb);
            cb.setConsignCode(SequenceSingleton.getSequence(SequenceEnum.CGS_BILL_CONSIGN_CODE));
            cb.setChargeMoney(chargeMoney);
            cb.setPayerAcc("记账时确定");
            cb.setGatherAuditState(0);
            cb.setSignFlag(0);
            cb.setConfirmAuditState(0);
            cb.setTallyFlag(0);
            cb.setFinishFlag(0);
            cb.setStatus(CgsBillConstant.STATUE_TEMP);
            this.saveCgsBill(cb, false, user);
        }
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CgsBillVO cbVO) {
        List<String> errMessage = new ArrayList<String>();
        // 判断收款单位在系统中是否存在
        CorpDTO payeeCorp = iCorpService.getCorpIdByName(cbVO.getPayeeCorpName());
        if (EmptyUtils.isEmpty(payeeCorp)) {
            errMessage.add("nis.cgsbill.message.payeecorpname.null");
        } else {
            cbVO.setPayeeCorpId(payeeCorp.getId());
        }
        // 判断付款单位在系统中是否存在
        CorpDTO payerCorp = iCorpService.getCorpIdByName(cbVO.getPayerCorpName());
        if (EmptyUtils.isEmpty(payerCorp)) {
            errMessage.add("nis.cgsbill.message.payercorpname.null");
        } else {
            cbVO.setPayerCorpId(payerCorp.getId());
        }
        //判断收款单位和收款账号是否匹配
        FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(cbVO.getPayeeAcc());
        if (EmptyUtils.isEmpty(fcAccDTO)) {
            errMessage.add("nis.cgsbill.message.payeeAcc.null");
        } else {
            if (!(payeeCorp.getId().equals(fcAccDTO.getCorpId()))) {
                errMessage.add("nis.cgsbill.message.payeeacc.notmate");
            }
        }
        //收款单位和付款单位不能相同
        if (!EmptyUtils.isEmpty(payeeCorp) && !EmptyUtils.isEmpty(payerCorp) && payeeCorp.getId().equals(payerCorp.getId())) {
            errMessage.add("nis.cgsbill.message.cgscode.unique");
        }
        return new ExcelVerifyHanlderResult(false, errMessage);
    }
    @Override
    public void verifyHandler(List<CgsBillVO> list) {
    }
}
