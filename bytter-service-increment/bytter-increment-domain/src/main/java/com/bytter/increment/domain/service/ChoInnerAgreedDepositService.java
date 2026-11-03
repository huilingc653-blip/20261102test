package com.bytter.increment.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.constant.NisMessageConstant;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.service.IChoMessageService;
import com.bytter.increment.domain.dao.ChoInnerAgreedDepositDao;
import com.bytter.increment.domain.dao.FcAgreedDepositDao;
import com.bytter.increment.domain.dao.FcAgreedDetailDao;
import com.bytter.increment.domain.entity.FcAgreedDeposit;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.vo.FcAccVO;
import com.bytter.increment.domain.vo.FcAgreedDepositVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.AccountGrantDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Service
 * @Description: 协定存款登记表数据类
 * @date 2019-05-10 16:44:38
 */
@WorkFlow({
        ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_REG,
        ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_POS,
        ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_TERM})
@Service("choInnerAgreedDepositService")
@Transactional
public class ChoInnerAgreedDepositService extends BaseService<FcAgreedDeposit, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ITaskService {
    @Autowired
    private ChoInnerAgreedDepositDao choInnerAgreedDepositDao;
    @Autowired
    private FcAgreedDetailDao fcAgreedDetailDao;
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    @DubboReference(check = false)
	private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private IChoMessageService choMessageService;
    @DubboReference(check = false)
	private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Autowired
    private FcAgreedDepositDao fcAgreedDepositDao;
    @Override
    public BaseDao<FcAgreedDeposit, String> getEntityDao() {
        return choInnerAgreedDepositDao;
    }
    /**
     * 内部存款-协定存款登记/延期/终止查询列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        String opType = (String) input.getFormdata().get("opType");
        String resource = ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_REG_QUERY;
        StringBuilder joinSql = new StringBuilder();
        if (InnerFinanceConstant.BUS_TYPE_1.equals(opType)) {
            resource = ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_POS_QUERY;
        } else if (InnerFinanceConstant.BUS_TYPE_2.equals(opType)) {
            resource = ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_TERM_QUERY;
        }
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "d.corp_Id", true);
        joinSql.append(" and ").append(corpAuthSql);
        return choInnerAgreedDepositDao.getDataTablesUnion(input, user, joinSql.toString());
    }
    /**
     * 内部存款-协定存款查询列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput getDataTableQuery(DataTablesInput input, ICurrentUser user) {
        StringBuilder joinSql = new StringBuilder();
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_QUERY, user.getId()), "d.corp_Id", true);
        joinSql.append(" and ").append(corpAuthSql);
        String certificateWay=(String) input.getFormdata().get("eqs_certificateWay");
        if(StringUtil.isNotEmpty(certificateWay)){
            joinSql.append(" and d.certificate_way= "+certificateWay +" ");
            input.getFormdata().remove("eqs_certificateWay");
        }
        DataTablesOutput dataTableQuery = choInnerAgreedDepositDao.getDataTableQuery(input, user, joinSql.toString());
        List<FcAgreedDepositVO> data = (List<FcAgreedDepositVO>) dataTableQuery.getData();
        /*用于存放过滤条件后的数据*/
        List<FcAgreedDepositVO> showData = new ArrayList<>();
        String choStatus = (String) input.getFormdata().get("choStatus");
        for (FcAgreedDepositVO vo : data) {
            /*根据登记id通过递归查找这笔协定合同的最后一笔记录的id*/
            String endId = this.findEndId(vo.getId(), 1);
            FcAgreedDeposit entity = this.get(endId);
            vo.setCertificateWay(entity.getCertificateWay());
            if(InnerFinanceConstant.BUS_TYPE_2.equals(entity.getBusType())){
                /*如果终止新结束日期取终止日期*/
                vo.setEndDate(entity.getStopDate());
            }else if(InnerFinanceConstant.BUS_TYPE_1.equals(entity.getBusType())){
                /*如果到期处理新结束日期取结束日期*/
                vo.setEndDate(entity.getEndDate());
            }
            /*根据最终延期/终止id确认状态和到期处理方式*/
            if (InnerFinanceConstant.BUS_TYPE_0.equals(entity.getBusType())) {
                if (entity.getStatus() < InnerFinanceConstant.STATUE_CHECK) {
                    /*签署中*/
                    vo.setStatusName("0");
                } else {
                    /*正常*/
                    vo.setStatusName("1");
                }
            } else if (InnerFinanceConstant.BUS_TYPE_1.equals(entity.getBusType())) {
                if (entity.getStatus() < InnerFinanceConstant.STATUE_CHECK) {
                    /*变更*/
                    vo.setStatusName("2");
                } else {
                    /*正常*/
                    vo.setStatusName("1");
                }
            } else {
                if (entity.getStatus() < InnerFinanceConstant.STATUE_CHECK) {
                    /*变更*/
                    vo.setStatusName("2");
                } else {
                    /*已终止*/
                    vo.setStatusName("3");
                }
            }
            /*前台过滤查询状态*/
            if (StringUtils.isNotBlank(choStatus) && choStatus.equals(vo.getStatusName())) {
                showData.add(vo);
            }
        }
        if (StringUtils.isNotBlank(choStatus)) {
            dataTableQuery.setData(showData);
        } else {
            dataTableQuery.setData(data);
        }
        return dataTableQuery;
    }
    /**
     * 内部存款-协定存款明细查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTableQueryDtl(DataTablesInput dataTablesInput) {
        String id = (String) dataTablesInput.getFormdata().get("id");
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        final List<String> ids = this.findId(null, id, 1);
        formdata.put("oriIds", ids);
        dataTablesInput.setFormdata(formdata);
        return choInnerAgreedDepositDao.getAgreedDepositDtlById(dataTablesInput);
    }
    /**
     * 根据开户单位,单据类型查找正常状态非资金池内部账号
     *
     * @param user
     * @param corpId
     * @param busType
     * @return
     */
    public List<FcAccVO> getBlnctrAccByCorpId(ICurrentUser user, String corpId, String busType) {
//        String accAuthSql = SqlUtil.getInSql(accountGrantService.listByMultiVal(user.getId(), corpId, null, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT), "f.id", true);
//        return choInnerAgreedDepositDao.getBlnctrAccByCorpId(corpId, busType, accAuthSql);
        // String accAuthSql = SqlUtil.getInSql(accountGrantService.listByMultiVal(user.getId(), corpId, null, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT), "f.id", true);
        List<String> listAccId = fcAgreedDepositDao.getAccIdForDeposit(busType);
        List<FcAccDTO> blnctrAcc = fcAccService.getBlnctrAccByCorpId(user.getUsername().trim(), corpId, busType, listAccId);
        return BeanUtils.copyListProperties(blnctrAcc, FcAccVO.class);
    }
    /**
     * 根据ID查询协定存款登记信息
     *
     * @param id
     * @return
     */
    public FcAgreedDepositVO getByUnion(String id) {
        return choInnerAgreedDepositDao.getByUnion(id);
    }
    /**
     * 根据账户id,单据类型查找协定信息
     *
     * @param accId
     * @return
     */
    public FcAgreedDepositVO getAgreeInfo(String accId, String busType) {
        List<FcAgreedDepositVO> list = choInnerAgreedDepositDao.getChoAgreeInfo(accId, busType);
        FcAgreedDepositVO vo = null;
        if (!EmptyUtils.isEmptyList(list)) {
            vo = list.get(0);
            List<FcAgreedDetail> details = fcAgreedDetailDao.getDetailByAgreedIdLineNo(vo.getId(), null);
            vo.setDetailList(details);
        }
        return vo;
    }
    /**
     * 根据账号和日期查找协定信息
     *
     * @param blnctrAcc
     * @return
     */
    public FcAgreedDepositVO getChoAgreeInfoByAccout(String blnctrAcc,Date queryDate) {
        List<FcAgreedDepositVO> list = choInnerAgreedDepositDao.getChoAgreeInfoByAccout(
                blnctrAcc,queryDate);
        FcAgreedDepositVO vo = null;
        if (!EmptyUtils.isEmptyList(list)) {
            vo = list.get(0);
            List<FcAgreedDetail> details = fcAgreedDetailDao.getDetailByAgreedIdLineNo(vo.getId(), null);
            vo.setDetailList(details);
        }
        return vo;
    }
    /**
     * 根据协定存款协定账号和日期查找协定信息
     *
     * @param agrAcc
     * @return
     */
    public FcAgreedDepositVO getChoAgreeInfoByAgrAcc(String agrAcc,Date queryDate) {
        return choInnerAgreedDepositDao.getChoAgreeInfoByAgrAcc(agrAcc,queryDate);
    }
    /**
     * 编辑查询vo公用方法
     *
     * @param fcAgreedDeposit
     * @return
     */
    public FcAgreedDepositVO getAgreeVo(FcAgreedDepositVO fcAgreedDeposit) {
        FcAgreedDepositVO fcAgreedDepositVO = null;
        if (StringUtils.isNotEmpty(fcAgreedDeposit.getId())) {
            fcAgreedDepositVO = this.getByUnion(fcAgreedDeposit.getId());
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(fcAgreedDeposit.getId());
            fcAgreedDepositVO.setDetailList(details);
        }
        return fcAgreedDepositVO;
    }
    /**
     * 保存协定信息
     *
     * @param fcAgreedDepositVO
     * @param user
     */
    public void saveChoAgreedDeposit(FcAgreedDepositVO fcAgreedDepositVO, ICurrentUser user) {
        FcAgreedDeposit fcAgreedDeposit = new FcAgreedDeposit();
        BeanUtils.copyNotNullProperties(fcAgreedDepositVO, fcAgreedDeposit);
        CorpDTO netCorp = corpService.getNetByCorpId(fcAgreedDeposit.getCorpId());

        if (EmptyUtils.isEmpty(netCorp)) {
            fcAgreedDeposit.setNetId(fcAgreedDeposit.getCorpId());
            fcAgreedDeposit.setNetName(fcAgreedDeposit.getNetName());
        } else {
            fcAgreedDeposit.setNetId(netCorp.getId());
            fcAgreedDeposit.setNetName(netCorp.getName()); //null
        }
        FcAccDTO byBlnctrAcc = iInnerAccountService.getByBlnctrAcc(fcAgreedDepositVO.getBlnctrAcc());
        fcAgreedDeposit.setCorpName(byBlnctrAcc.getCorpName());
        fcAgreedDeposit.setAccName(byBlnctrAcc.getAccName());
        fcAgreedDeposit.setCurName(byBlnctrAcc.getCurName());
        fcAgreedDeposit.setCreateBy(user.getLoginName());
        fcAgreedDeposit.setCreateDate(DateUtils.getDate());
        fcAgreedDeposit.setPoolSign(PoolContant.POOL_SIGN_0);
        fcAgreedDeposit.setBillStatus(InnerFinanceConstant.BILL_STATUS_10);
        if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())) {
            fcAgreedDeposit.setBusType(InnerFinanceConstant.BUS_TYPE_1);
        } else if (InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())) {
            fcAgreedDeposit.setBusType(InnerFinanceConstant.BUS_TYPE_2);
        } else {
            fcAgreedDeposit.setBusType(InnerFinanceConstant.BUS_TYPE_0);
        }
        /*登记时判断是否存入协定账号*/
        if (InnerFinanceConstant.BUS_TYPE_0.equals(fcAgreedDepositVO.getOpType())) {
            if (StringUtils.isNotBlank(fcAgreedDepositVO.getIsAgreedDepositAcc()) && CommonConstant.YES.equals(fcAgreedDepositVO.getIsAgreedDepositAcc())) {
                FcAccDTO accDTO = fcAgreedDepositVO.getDto();
                accDTO.setAutoCheck(true);
                String accout = iInnerAccountService.saveOrUpdate(accDTO, user);
                FcAccDTO accDto = iInnerAccountService.getByBlnctrAcc(accout);
                fcAgreedDeposit.setAgrAccId(accDto.getId());
                //新增账户自动授权
                AccountGrantDTO grantDTO = new AccountGrantDTO();
                grantDTO.setAccountId(accDto.getId());
                grantDTO.setUserId(user.getId());
                grantDTO.setCorpId(accDto.getCorpId());
                grantDTO.setAccountType(Integer.valueOf(AccountGrantConstant.INNER_BANK_TYPE));
                accountGrantService.saveAccountGrant(grantDTO);
            }
        }
        /*保存协定信息*/
        this.saveEntity(fcAgreedDeposit);
        /*保存明细*/
        this.saveDetail(fcAgreedDeposit, fcAgreedDepositVO);
        /*如果是到期处理/终止 更新原单据为延期/终止*/
        if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType()) || InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())) {
            FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
            entity.setBillStatus(InnerFinanceConstant.BILL_STATUS_11);
            this.updateEntity(entity);
        }
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        /*启动审批*/
        if (InnerFinanceConstant.STATUE_WAIT_CHECK.equals(fcAgreedDeposit.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;
            String workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_REG;
            if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDepositVO.getOpType())) {
                workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_POS;
            } else if (InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDepositVO.getOpType())) {
                workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_TERM;
            }
            /*调用审批后方法*/
             getStartHandler().handler(workFlowType, fcAgreedDeposit.getId(), fcAgreedDepositVO, fcAgreedDeposit.getCorpId(), user.getId());
        }
        choMessageService.saveMessage(fcAgreedDeposit.getId(), messageType, message, ChoMessageConstant.BUS_TYPE_AGREE, user.getLoginName(), user.getUsername());
    }
    /**
     * 获取协定存款消息通知的常量类型
     *
     * @param type 协定存款类型
     * @return
     */
    public String getTag(String type) {
        String tag = "";
        if (InnerFinanceConstant.BUS_TYPE_0.equals(type)) {
            tag = NotificationConstant.CHOWMATISTIC.CHO_INNERAGREED_REG;
        } else if (InnerFinanceConstant.BUS_TYPE_1.equals(type)) {
            tag = NotificationConstant.CHOWMATISTIC.CHO_INNERAGREED_POS;
        } else if (InnerFinanceConstant.BUS_TYPE_2.equals(type)) {
            tag = NotificationConstant.CHOWMATISTIC.CHO_INNERAGREED_TERM;
        }
        return tag;
    }
    /**
     * 修改协定信息
     *
     * @param fcAgreedDepositVO
     * @param user
     */
    public void updateChoAgreedDeposit(FcAgreedDepositVO fcAgreedDepositVO, ICurrentUser user, boolean isBatch) {
        FcAgreedDeposit fcAgreedDeposit = this.get(fcAgreedDepositVO.getId());
        BeanUtils.copyNotNullProperties(fcAgreedDepositVO, fcAgreedDeposit);
        CorpDTO netCorp = corpService.getNetByCorpId(fcAgreedDeposit.getCorpId());
        /*如果没有直属网点就是本网点*/
        if (EmptyUtils.isEmpty(netCorp)) {
            fcAgreedDeposit.setNetId(fcAgreedDeposit.getCorpId());
        } else {
            fcAgreedDeposit.setNetId(netCorp.getId());
        }
        fcAgreedDeposit.setUpdateBy(user.getLoginName());
        fcAgreedDeposit.setUpdateDate(DateUtils.getDate());
        fcAgreedDeposit.setBillStatus(InnerFinanceConstant.BILL_STATUS_10);
        this.saveOrUpdate(fcAgreedDeposit);
        /*不是批量提交才进行明细删除后新增*/
        if (!isBatch) {
            List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(fcAgreedDeposit.getId());
            if (!EmptyUtils.isEmptyList(details)) {
                for (FcAgreedDetail detail : details) {
                    fcAgreedDetailService.delete(detail.getId());
                }
            }
            /*保存明细*/
            this.saveDetail(fcAgreedDeposit, fcAgreedDepositVO);
        }
        /*如果是到期处理/终止 更新原单据为延期/终止*/
        if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getBusType()) || InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
            FcAgreedDeposit entity = this.get(fcAgreedDeposit.getAgreedDepositOriId());
            entity.setBillStatus(InnerFinanceConstant.BILL_STATUS_11);
            this.updateEntity(entity);
        }
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        /*启动审批*/
        if (InnerFinanceConstant.STATUE_WAIT_CHECK.equals(fcAgreedDeposit.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
            String workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_REG;
            if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getBusType())) {
                workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_POS;
            } else if (InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
                workFlowType = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_TERM;
            }
            /*调用审批后方法*/
             getStartHandler().handler(workFlowType, fcAgreedDeposit.getId(), fcAgreedDepositVO, fcAgreedDeposit.getCorpId(), user.getId());
        }
        choMessageService.saveMessage(fcAgreedDeposit.getId(), messageType, message, ChoMessageConstant.BUS_TYPE_AGREE, user.getLoginName(), user.getUsername());
        //完成通知提醒
        String tag = this.getTag(fcAgreedDeposit.getBusType());
        if (!StringUtils.isBlank(tag)) {
            notificationService.complete(tag, fcAgreedDeposit.getId(), user.getId());
        }
    }
    /**
     * 保存明细
     *
     * @param fcAgreedDeposit
     */
    public void saveDetail(FcAgreedDeposit fcAgreedDeposit, FcAgreedDepositVO vo) {
        int line = 0;
        if (!EmptyUtils.isEmptyList(vo.getDetailList())) {
            for (FcAgreedDetail detail : vo.getDetailList()) {
                line++;
                FcAgreedDetail entity = new FcAgreedDetail();
                entity.setAgreedDepositId(fcAgreedDeposit.getId());
                entity.setMinMoney(detail.getMinMoney());
                entity.setMaxMoney(detail.getMaxMoney());
                entity.setAgreeRate(detail.getAgreeRate());
                entity.setLineNo(line);
                fcAgreedDetailService.saveEntity(entity);
            }
        }
    }
    /**
     * 删除协定信息
     *
     * @param id
     */
    public void delChoAgreedDeposit(String id, ICurrentUser user) {
        FcAgreedDeposit fcAgreedDeposit = this.get(id);
        fcAgreedDeposit.setStatus(InnerFinanceConstant.STATUE_DEL);
        this.updateEntity(fcAgreedDeposit);
        /*如果是到期处理/终止 更新原单据为登记*/
        if (InnerFinanceConstant.BUS_TYPE_1.equals(fcAgreedDeposit.getBusType()) || InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
            FcAgreedDeposit deposit = choInnerAgreedDepositDao.get(fcAgreedDeposit.getAgreedDepositOriId());
            if (!EmptyUtils.isEmpty(deposit)) {
                deposit.setBillStatus(InnerFinanceConstant.BILL_STATUS_10);
                this.updateEntity(fcAgreedDeposit);
            }
        } else {
            /*如果是协定登记判断是否存入协定账号，如果是，删除协定信息时，把协定账户注销*/
            if (CommonConstant.YES.equals(fcAgreedDeposit.getIsAgreedDepositAcc()) && StringUtils.isNotBlank(fcAgreedDeposit.getAgrAcc())) {
                iInnerAccountService.cancelFcAcc(fcAgreedDeposit.getAgrAcc(), fcAgreedDeposit.getBeginDate(), user);
            }
        }
        String message = NisMessageConstant.NIS_LOG_DELETE_SUCC;
        choMessageService.saveMessage(fcAgreedDeposit.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, message, ChoMessageConstant.BUS_TYPE_AGREE, user.getLoginName(), user.getUsername());
    }
    /**
     * 根据单据类型返回不同的跳转页面
     *
     * @param busType
     * @return
     */
    public String getWorkFlowByBusType(Integer busType) {
        /**单据类型 0-登记 1-到期处理 2-终止*/
        String workFlow = "";
        if (Integer.valueOf(InnerFinanceConstant.BUS_TYPE_0).intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_REG;
        } else if (Integer.valueOf(InnerFinanceConstant.BUS_TYPE_1).intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_POS;
        } else if (Integer.valueOf(InnerFinanceConstant.BUS_TYPE_2).intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_AGREEDDEPOSIT_TERM;
        }
        return workFlow;
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        String sign = "";
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_busType"))) {
            String type = String.valueOf(dataTablesInput.getFormdata().get("eqs_busType"));
            if (EmptyUtils.isNumeric(type)) {
                sign = this.getWorkFlowByBusType(new Integer(type));
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(sign, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "d.id", true);
        DataTablesOutput<FcAgreedDepositVO> dp = this.choInnerAgreedDepositDao.dataTableApprove(dataTablesInput, businessSql);
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FcAgreedDeposit fcAgreedDeposit = this.get(workFlowResult.getBusinessKey(), true);
        this.approvePassEnd(fcAgreedDeposit, workFlowResult.isPass(), user);
        Integer messageType = workFlowResult.isPass() ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = workFlowResult.isPass() ? NisMessageConstant.NIS_LOG_APP_SUCC : NisMessageConstant.NIS_LOG_APP_BACK;
        /*生成日志*/
        choMessageService.saveMessage(fcAgreedDeposit.getId(), messageType, message, ChoMessageConstant.BUS_TYPE_AGREE, user.getLoginName(), user.getUsername());
    }
    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FcAgreedDeposit entity = this.get(workFlowResult.getBusinessKey(), true);
        entity.setStatus(InnerFinanceConstant.STATUE_TEMP);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getId(), ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", ChoMessageConstant.BUS_TYPE_AGREE, user.getLoginName(), user.getUsername());
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批通过后处理
     *
     * @param fcAgreedDeposit
     * @param result
     */
    public void approvePassEnd(FcAgreedDeposit fcAgreedDeposit, boolean result, ICurrentUser user) {
        if (EmptyUtils.isBlank(fcAgreedDeposit.getCreateBy())) {
            fcAgreedDeposit.setCreateBy(user.getLoginName());
            fcAgreedDeposit.setCreateDate(DateUtils.getDate());
        }
        if (!result) {
            /*更新审批状态-审批打回*/
            fcAgreedDeposit.setStatus(NetbankConstant.STATUE_BACK);
            fcAgreedDeposit.setUpdateBy(user.getLoginName());
            fcAgreedDeposit.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(fcAgreedDeposit);
        } else {
            /*更新审批状态-审批通过*/
            fcAgreedDeposit.setStatus(NetbankConstant.STATUE_CHECK);
            fcAgreedDeposit.setUpdateBy(user.getLoginName());
            fcAgreedDeposit.setUpdateDate(DateUtils.getDate());
            /*如果是终止修改单据状态为11终止，业务结束*/
            if (InnerFinanceConstant.BUS_TYPE_2.equals(fcAgreedDeposit.getBusType())) {
                fcAgreedDeposit.setBillStatus(InnerFinanceConstant.BILL_STATUS_11);
            }
            this.saveOrUpdate(fcAgreedDeposit);
        }
    }
    /**
     * 根据账户id,单据类型,状态查找协定信息
     *
     * @param accId
     * @param busType
     * @param status
     * @return
     */
    public List<FcAgreedDepositVO> getAgreedInfo(String accId, String busType, List<Integer> status) {
        return choInnerAgreedDepositDao.getAgreedInfo(accId, busType, status);
    }
    /**
     * 通过登记id递归查找所有延期/终止id
     *
     * @param ids
     * @param depositOriId
     * @param n
     * @return
     */
    public List<String> findId(List<String> ids, String depositOriId, int n) {
        if (n == 1) {
            ids = new ArrayList<>();
        }
        String oriId = depositOriId;
        FcAgreedDeposit info = choInnerAgreedDepositDao.getInfoByDepositOriId(oriId, null, null);
        if (!EmptyUtils.isEmpty(info)) {
            ids.add(oriId);
            oriId = info.getId();
            findId(ids, oriId, n + 1);
        }
        return ids;
    }
    /**
     * 通过登记id递归查找最后一笔延期id
     *
     * @param depositOriId
     * @param n
     * @return
     */
    public String findEndId(String depositOriId, int n) {
        if (n == 1) {
            depositOriId = depositOriId;
        }
        FcAgreedDeposit info = choInnerAgreedDepositDao.getInfoByDepositOriId(depositOriId, null, null);
        if (!EmptyUtils.isEmpty(info)) {
            depositOriId = info.getId();
            return findEndId(depositOriId, n + 1);
        }
        return depositOriId;
    }
    /**
     * 根据合同号查找协定信息
     *
     * @param contractNo
     * @return
     */
    public List<FcAgreedDepositVO> getInfoByContractNo(String contractNo) {
        return choInnerAgreedDepositDao.getInfoByContractNo(contractNo);
    }
    /**
     * 自动提示付款勾兑
     */
    @Override
    @XxlJob("ChoInnerAgreedDepositService")
    public void run(String arguments) {
        /*您有一笔账号为{blnctrAcc}的协定存款，将于${endDate}到期！*/
        try {
            Date curDate = DateUtils.parseDate(DateUtils.formatDate(DateUtils.getDate()));
            this.logger.info(curDate + "---启用协定存款到期提醒，开始执行定时任务---");
            List<FcAgreedDepositVO> voList = choInnerAgreedDepositDao.getPassAgreedInfo();
            for (FcAgreedDepositVO vo : voList) {
                ConfigDTO configDTO = configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_AGREEDDEPOSITALERTDAYS);
                List<String> agreedDays = configDTO.toList();
                if(EmptyUtils.isEmpty(agreedDays)){
                    agreedDays = new ArrayList<String>();
                    agreedDays.add("60");
                    agreedDays.add("15");
                }
                for(String agreedDay:agreedDays) {
                    int day = 0;
                    if (StringUtils.isNotBlank(agreedDay)) {
                        day = Integer.valueOf(agreedDay);
                    }
                    /*到期时间 - 协定存款到期提醒天数 = 当前日期，提醒*/
                    if (DateUtils.addDay(vo.getEndDate(), (-day)).equals(curDate)) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("blnctrAcc", vo.getBlnctrAcc());
                        map.put("endDate", vo.getEndDate());
                        /*通知提醒*/
                        notificationService.sendToUserByResPermission(ResourceConstant.CHO_INNERAGREEDDEPOSIT_AGREEDDEPOSIT_QUERY, NotificationConstant.CHOWMATISTIC.CHO_INNER_AGREED, map);
                    }
                    /*到期日当天根据到期处理方式自动生成新的协定数据*/
                    if (vo.getEndDate().equals(curDate)) {
                        FcAgreedDeposit entity = this.get(vo.getId());
                        /*修改原单为延期/终止11*/
                        entity.setBillStatus(InnerFinanceConstant.BILL_STATUS_11);
                        this.updateEntity(entity);
                        /*生成新的延期/终止数据*/
                        FcAgreedDeposit newEntity = new FcAgreedDeposit();
                        BeanUtils.copyNotNullProperties(vo, newEntity);
                        newEntity.setId("");
                        newEntity.setAgreedDepositOriId(vo.getId());
                        newEntity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FC_AGREED_DEPOSIT));
                        newEntity.setBillStatus(InnerFinanceConstant.BILL_STATUS_10);
                        newEntity.setStatus(InnerFinanceConstant.STATUE_CHECK);
                        newEntity.setCreateBy("定时器生成");
                        newEntity.setCreateDate(curDate);
                        newEntity.setUpdateBy("");
                        newEntity.setUpdateDate(null);
                        /*延期*/
                        if (InnerFinanceConstant.AGR_CERTIFICATE_WAY_0.equals(vo.getCertificateWay())) {
                            int newDays = DateUtils.getOffsetDays(vo.getBeginDate(), vo.getEndDate());
                            newEntity.setAgreedDepositOriId(vo.getId());
                            newEntity.setEndDate(DateUtils.addDay(vo.getEndDate(), newDays));
                            newEntity.setBusType(InnerFinanceConstant.BUS_TYPE_1);
                        } else {
                            /*终止*/
                            newEntity.setBusType(InnerFinanceConstant.BUS_TYPE_2);
                            newEntity.setStopDate(curDate);
                        }
                        this.saveEntity(newEntity);
                        /*保存明细*/
                        List<FcAgreedDetail> detail = fcAgreedDetailService.getDetailList(vo.getId());
                        vo.setDetailList(detail);
                        this.saveDetail(newEntity, vo);
                    }
                }
            }
            this.logger.info("---启用协定存款到期提醒，结束执行定时任务---");
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
