package com.bytter.biz.receivable.service;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.service.NisMessageService;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.NisWorkFlowConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.receivable.dao.NisReceivableDao;
import com.bytter.biz.receivable.entity.NisReceivable;
import com.bytter.biz.receivable.vo.NisReceivableVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.finance.api.IInlSendService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 收款通知表
 * @date 2019-07-02 17:30:33
 */
@WorkFlow(NisWorkFlowConstant.NIS_RECEIVABLE)
@Service("nisReceivableService")
@Transactional
public class NisReceivableService extends BaseService<NisReceivable, String> implements IWorkFlowListener, IUserExcelVerifyHandler<NisReceivableVO>, IWorkFlowRevokeListener, ICorpDisableVerifyService, DictInjectProvider  {
    @Autowired
    private NisReceivableDao nisReceivableDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private IInputBankInfoService btBankService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IInlSendService inlSendService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Override
    public BaseDao<NisReceivable, String> getEntityDao() {
        return nisReceivableDao;
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(NisReceivable entity) {
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
    public void save(NisReceivable entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(NisReceivable entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(NisReceivable entity) {
        super.saveOrUpdate(entity);
        getSession().flush();
    }
    /**
     * 查询收款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = "";
        String corpAuthSql = "";
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        //收款办理查询打回和暂存的付款数据
        if ("list".equals(opType)) {
            resource = ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_QUERY;
            sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null)");
            sql.append(" and n.status in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + ")");
        }else if("query".equals(opType)){
            //收款查询
            resource = ResourceConstant.NETBANK_NETBANKQUERY_NISRECEIVABLE_QUERY;
            sql.append(" and n.status <> " + NetbankConstant.STATUE_DEL);
        }else{
            sql.append(" and n.status = " + NetbankConstant.STATUE_CHECK);
        }
        if("list".equals(opType) || "query".equals(opType)){
            corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
        }
        DataTablesOutput dataTablesOutput = nisReceivableDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<NisReceivableVO> nisReceivableVOList = dataTablesOutput.getData();
        for(NisReceivableVO nisReceivableVO:nisReceivableVOList) {
            injectValue(nisReceivableVO);
            if (StringUtils.isEmpty(nisReceivableVO.getAccName())) {
                BtBankAccDTO dto = btBankAccService.getBankAccInfo(nisReceivableVO.getBankAcc());
                if (!EmptyUtils.isEmpty(dto)) {
                    nisReceivableVO.setAccName(dto.getAccName());
                    nisReceivableVO.setBankName(dto.getBankName());
                }
            }
            if (StringUtils.isEmpty(nisReceivableVO.getOppBankCode())) {
                InputBankInfoDTO btBankDTO = this.btBankService.getByBankName(nisReceivableVO.getOppAccBank());
                if (!EmptyUtils.isEmpty(btBankDTO)) {
                    nisReceivableVO.setOppBankCode(btBankDTO.getBankCode());
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 根据收款ID获取收款信息
     *
     * @param id
     * @return
     */
    public NisReceivableVO getByIdUnion(String id) {
        NisReceivableVO nisReceivable = nisReceivableDao.getByIdUnion(id);
        if (EmptyUtils.isEmpty(nisReceivable)) {
            return null;
        }
        if(StringUtils.isEmpty(nisReceivable.getAccName())){
            BtBankAccDTO dto = btBankAccService.getBankAccInfo(nisReceivable.getBankAcc());
            if (!EmptyUtils.isEmpty(dto)) {
                nisReceivable.setAccName(dto.getAccName());
                nisReceivable.setBankName(dto.getBankName());
            }
        }
        if(StringUtils.isEmpty(nisReceivable.getOppBankCode()) && StringUtils.isNotEmpty(nisReceivable.getOppAccBank())){
            InputBankInfoDTO byBankName = btBankService.getByBankName(nisReceivable.getOppAccBank());
            if (!EmptyUtils.isEmpty(byBankName)) {
                nisReceivable.setOppBankCode(byBankName.getBankCode());
            }
        }
        injectValue(nisReceivable);
        //获取余额
        if (nisReceivable != null) {
        	BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisReceivable.getBankAcc(), nisReceivable.getCurId());
            if (bal != null) {
                nisReceivable.setBalance(bal.getBal());
            }
            if (nisReceivable.getBalance() == null) {
                nisReceivable.setBalance(BigDecimal.ZERO);
            }
        }
        return nisReceivable;
    }
    /**
     * 根据收款单号获取收款信息
     *
     * @param billNo
     * @return
     */
    public NisReceivableVO getByBillNoUnion(String billNo) {
        NisReceivableVO nisReceivable = nisReceivableDao.getByBillNoUnion(billNo);
        return nisReceivable;
    }
    /**
     * 保存
     *
     * @param nisReceivable
     */
    public void saveNisReceivable(NisReceivableVO nisReceivable, ICurrentUser user) {
        NisReceivable entity = new NisReceivable();
        BeanUtils.copyNotNullProperties(nisReceivable, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_RECEIVABLE));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        super.saveEntity(entity);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;
            //传入付款方式审批条件
            NisReceivableVO nisReceivableVO = new NisReceivableVO();
            BeanUtils.copyNotNullProperties(entity, nisReceivableVO);
            //调用审批后方法
             getStartHandler().handler(NisWorkFlowConstant.NIS_RECEIVABLE, entity.getId(), nisReceivableVO, entity.getCorpId(), user.getId());
        }
        if(user !=null) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
        }
    }
    /**
     * 编辑收款单
     *
     * @param nisReceivable
     */
    public void updateNisReceivable(NisReceivableVO nisReceivable, ICurrentUser user) {
        NisReceivable entity = nisReceivableDao.get(nisReceivable.getId(), true);
        BeanUtils.copyNotNullProperties(nisReceivable, entity);
        if (EmptyUtils.isBlank(entity.getCreateBy())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = nisReceivable.getBatch() ? NisMessageConstant.NIS_LOG_BATCH_SUBMIT_SUCC : NisMessageConstant.NIS_LOG_UPDATE_SUBMIT_SUCC;
            //传入付款方式审批条件
            NisReceivableVO nisReceivableVO = new NisReceivableVO();
            BeanUtils.copyNotNullProperties(entity, nisReceivableVO);
            //调用审批后方法
             getStartHandler().handler(NisWorkFlowConstant.NIS_RECEIVABLE, entity.getId(), nisReceivableVO, entity.getCorpId(), user.getId());
        }
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
    }
    /**
     * 逻辑删除收款单
     *
     * @param id
     * @param user
     * @param isBatch
     */
    public void deleteNisReceivable(String id, ICurrentUser user, boolean isBatch) {
        deleteNisReceivable(id, user, isBatch, true);
    }

    /**
     * 逻辑删除收款单
     *
     * @param id 收款单id
     * @param user 当前用户
     * @param isBatch 是否批量
     * @param triggerBillheadRollback 是否触发反向回删付款单
     */
    public void deleteNisReceivable(String id, ICurrentUser user, boolean isBatch, boolean triggerBillheadRollback) {
        NisReceivable entity = this.get(id, true);
        entity.setStatus(NetbankConstant.STATUE_DEL);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //信贷管理-内部拆借过来的单据，删除时联动调整拆借数据
        if (triggerBillheadRollback && NetbankConstant.BILLFROM_4.equals(entity.getBillFrom())) {
            inlSendService.backInlSendForNisReceivable(entity.getBillId(),user);
        } else if (triggerBillheadRollback && NetbankConstant.BILLFROM_8.equals(entity.getBillFrom())) {
            inlSendService.backInlRepayForNisReceivable(entity.getBillId(),user);
        }
        String message = isBatch ? NisMessageConstant.NIS_LOG_BATCH_DELETE_SUCC : NisMessageConstant.NIS_LOG_DELETE_SUCC;
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, message, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
        getEntityDao().flush();
    }
    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchDelReceivable(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.deleteNisReceivable(id, user, true);
        }
    }
    /**
     * 批量提交收款单
     *
     * @param ids
     */
    public void batchSubmitNisReceivable(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisReceivableVO nisReceivable = this.getByIdUnion(id);
            nisReceivable.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
            nisReceivable.setBatch(true);
            this.updateNisReceivable(nisReceivable, user);
        }
    }
    /**
     * 审批结束
     * @param workFlowResult
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        NisReceivable nisReceivable = this.get(workFlowResult.getBusinessKey(), true);
        this.approvePassEnd(nisReceivable, workFlowResult.isPass(), user);
        Integer messageType = workFlowResult.isPass() ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = workFlowResult.isPass() ? NisMessageConstant.NIS_LOG_APP_SUCC : NisMessageConstant.NIS_LOG_APP_BACK;
        //生成日志
        nisMessageService.saveMessage(nisReceivable.getId(), messageType, message, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
    }
    /**
     * 审批撤回
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        NisReceivable nisReceivable = this.get(workFlowResult.getBusinessKey(), true);
        //更新审批状态
        nisReceivable.setStatus(NetbankConstant.STATUE_TEMP);
        nisReceivable.setUpdateBy(user.getLoginName());
        nisReceivable.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(nisReceivable);
        //生成日志
        nisMessageService.saveMessage(nisReceivable.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 获取审批列表单位树
     *
     * @param user
     * @return
     */
    public Object treeByCorpIds(ICurrentUser user) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.NIS_RECEIVABLE, user.getId());
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "n.id", true);
        List<String> corpIds = this.nisReceivableDao.getCorpIdsByDataTableApprove(businessSql);
        if (EmptyUtils.isEmptyList(corpIds)) {
            return tempList;
        }
        List<CorpDTO> corpList = this.corpService.listCorpDTOByCorpIds2(corpIds);
        if (EmptyUtils.isEmptyList(corpList)) {
            return tempList;
        }
        for (CorpDTO corp : corpList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", corp.getId());
            map.put("pId", corp.getParentId());
            map.put("name", corp.getName());
            map.put("type", corp.getType());
            map.put("code", corp.getCode());
            map.put("netId", corp.getNetId());
            tempList.add(map);
        }
        return tempList;
    }
    /**
     * 审批通过后处理
     *
     * @param nisReceivable
     * @param result
     */
    public void approvePassEnd(NisReceivable nisReceivable, boolean result, ICurrentUser user) {
        if (EmptyUtils.isBlank(nisReceivable.getCreateBy())) {
            nisReceivable.setCreateBy(user.getLoginName());
            nisReceivable.setCreateDate(DateUtils.getDate());
        }
        //更新审批时间
        nisReceivable.setApproveDate(DateUtils.getDate());
        if (!result) {
            //更新审批状态-打回
            nisReceivable.setStatus(NetbankConstant.STATUE_BACK);
            nisReceivable.setUpdateBy(user.getLoginName());
            nisReceivable.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(nisReceivable);
        } else {
            //更新审批状态-通过
            nisReceivable.setStatus(NetbankConstant.STATUE_CHECK);
            nisReceivable.setUpdateBy(user.getLoginName());
            nisReceivable.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(nisReceivable);
        }
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.NIS_RECEIVABLE, dataTablesInput);
        List<String> businessIdList = workFlowBusiness.getBusinessIdList();
        String businessSql = "";
        if(!EmptyUtils.isEmpty(businessIdList)){
            businessSql = SqlUtil.getInSql(businessIdList, "n.id", true);
        }
        DataTablesOutput dp = this.nisReceivableDao.dataTableApprove(dataTablesInput, businessSql);
        List<NisReceivableVO> nisReceivableVOList = dp.getData();
        for(NisReceivableVO nisReceivableVO:nisReceivableVOList){
            injectValue(nisReceivableVO);
        }
        return workFlowBusiness.merge(dp);
    }
    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<NisReceivableVO> list, ICurrentUser user) {
        for (NisReceivableVO nisReceivable : list) {
            nisReceivable.setBillFrom(NetbankConstant.RECEIVABLE_BILLFROM_1);
            nisReceivable.setStatus(NetbankConstant.STATUE_TEMP);
            this.saveNisReceivableForImport(nisReceivable, user);
        }
    }
    /**
     * 导入保存
     *
     * @param nisReceivable
     * @param user
     */
    public void saveNisReceivableForImport(NisReceivableVO nisReceivable, ICurrentUser user) {
        NisReceivable entity = new NisReceivable();
        BeanUtils.copyNotNullProperties(nisReceivable, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_RECEIVABLE));
        super.saveEntity(entity);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NisMessageConstant.NIS_LOG_IMPORT_SUCC, NisMessageConstant.BUS_TYPE_RECEIVABLE, user);
    }
    /**
     * 选择收款单
     *
     * @param ids
     */
    public void batchSelect(String ids, String bankSerialNo) {
        //先清空以前关联的
        nisReceivableDao.updateNisReceivableByBankSerialNo(bankSerialNo);
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisReceivableVO nisReceivable = this.getByIdUnion(id);
            NisReceivable entity = new NisReceivable();
            BeanUtils.copyNotNullProperties(nisReceivable, entity);
            entity.setBankSerialNo(bankSerialNo);
            entity.setStatus(NetbankConstant.RECEIVABLE_STATUE_ISREC);
            this.saveOrUpdate(entity);
        }
    }
    /**
     * 根据银行流水号查询收款单信息
     *
     * @param bankSerialNo 银行流水号
     */
    public List<NisReceivableVO> getNisReceivableByBankSerialNo(String bankSerialNo){
        return nisReceivableDao.getNisReceivableByBankSerialNo(bankSerialNo);
    }
    /**
     * 根据流水信息查询收款信息
     *
     * @param vo 收款信息
     */
    public NisReceivableVO getNisRecivableByInfo(NisReceivableVO vo){
        List<NisReceivableVO> listVO = nisReceivableDao.getNisRecivableByInfo(vo);
        if(!EmptyUtils.isEmptyList(listVO)){
            return listVO.get(0);
        }else{
            return null;
        }
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisReceivableVO vo, Object... user) {
        List<String> msgList = new ArrayList<>();
        // 判断单位在系统中是否存在
        CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
        if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
            msgList.add("nis.netbank.message.corp.null");
        } else {
            //校验当前用户是否有单位收款办理权限
            if (user[0] != null) {
                ICurrentUser userInfo = (ICurrentUser) user[0];
                List<String> userCorpIds = this.corpService.getUserCorpIds(ResourceConstant.NETBANK_NETBANK_NISRECEIVABLE_APPLY, userInfo.getId());
                if(userCorpIds.contains(corp.getId())){
                    vo.setCorpId(corp.getId());
                }else{
                    msgList.add("nis.netbank.message.corp.mismatch");
                }
            }
        }
        //业务类别校验
        if (!EmptyUtils.isEmpty(vo.getBusTypeName())) {
            NisCorpBusVO busType = nisCorpBusService.getBusTypeByName(vo.getCorpId(), "0", vo.getBusTypeName());
            if (busType == null) {
                msgList.add("nis.netbank.message.bustype.mismatch");
            } else {
                vo.setBusType(busType.getBusId());
            }
        }else{
            msgList.add("nis.netbank.message.bustype.null");
        }
        //校验币别
        String curId = btCurrencyService.getCurrencyByName(vo.getCurName());
        if (EmptyUtils.isBlank(curId)) {
            msgList.add("nis.netbank.message.cur.null");
        }
        //校验账号校验
        BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(vo.getBankAcc(),curId);
        if (EmptyUtils.isEmpty(btBankAccDTO)) {
            msgList.add("nis.netbank.message.bankaccinfo.cur.null");
        } else {
            vo.setBankAcc(btBankAccDTO.getBankAcc());
            vo.setAccName(btBankAccDTO.getAccName());
            vo.setBankName(btBankAccDTO.getBankName());
            vo.setCurId(btBankAccDTO.getCurId());
        }
        //交易时间
        if (vo.getTransTime() == null) {
            msgList.add("nis.netbank.message.transtime.null");
        }
        //判断开户行在系统中是否存在
        if (StringUtils.isNotBlank(vo.getOppBankCode()) && StringUtils.isNotBlank(vo.getOppAccBank())) {
            InputBankInfoDTO btBankDTO = this.btBankService.getByBankCodeAndImport(vo.getOppBankCode());
            if (EmptyUtils.isEmpty(btBankDTO)) {
                msgList.add("nis.netbank.message.oppAccBank.null");
            } else {
                //开户行名称不为空且与开户行代码对应的名称不一致
                if (!EmptyUtils.isBlank(vo.getOppBankCode()) && !vo.getOppAccBank().equals(btBankDTO.getBankName())) {
                    msgList.add("nis.netbank.message.payeebank.mismatch");
                } else {
                    vo.setOppBankCode(btBankDTO.getBankCode());
                    vo.setOppAccBank(btBankDTO.getBankName());
                }
            }
        }
        //金额不能为空且必须大于0
        if (EmptyUtils.isEmpty(vo.getAmt())) {
            msgList.add("nis.netbank.message.money.null");
        } else {
            if (vo.getAmt().compareTo(BigDecimal.ZERO) <= 0) {
                msgList.add("nis.netbank.message.money.mismatch");
            }
        }
        if (EmptyUtils.isEmptyList(msgList)) {
            return new ExcelVerifyHanlderResult(true, msgList);
        } else {
            return new ExcelVerifyHanlderResult(false, msgList);
        }
    }
    @Override
    public void verifyHandler(List<NisReceivableVO> list, Object... objects) {
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的收款单据，不允许禁用！
            int num = this.nisReceivableDao.getReceivableCountByCorpId(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_RECEIVABLE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_RECEIVABLE_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 根据收款单号获取收款信息
     * @param billId
     * @return
     */
    public NisReceivableVO getByBillId(String billId) {
        return nisReceivableDao.getByBillId(billId);
    }
    public void injectValue(NisReceivableVO nisReceivableVO){
        CorpDTO corpDTO = corpService.get(nisReceivableVO.getCorpId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            nisReceivableVO.setCorpName(corpDTO.getName());
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(nisReceivableVO.getCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            nisReceivableVO.setCurName(btCurrencyDTO.getCurName());
        }
    }

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
