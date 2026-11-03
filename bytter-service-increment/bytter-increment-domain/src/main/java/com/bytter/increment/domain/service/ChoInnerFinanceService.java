package com.bytter.increment.domain.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTableOutPutOrderUtils;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.domain.dao.ChoInnerFinanceDao;
import com.bytter.increment.domain.entity.ChoInnerFinance;
import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.pricing.dto.DepositRateTypeDTO;
import com.bytter.system.pricing.service.IDepositRateTypeService;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@WorkFlow({
        ChoWorkFlowConstant.CHO_INNER_C2T,
        ChoWorkFlowConstant.CHO_INNER_T2C,
        ChoWorkFlowConstant.CHO_INNER_C2N,
        ChoWorkFlowConstant.CHO_INNER_N2B,
        ChoWorkFlowConstant.CHO_INNER_B2N,
        ChoWorkFlowConstant.CHO_INNER_N2C})
@Service("choInnerFinanceService")
@Transactional
public class ChoInnerFinanceService extends BaseService<ChoInnerFinance,String> implements IWorkFlowListener, IWorkFlowRevokeListener, ITaskService, ICorpDisableVerifyService {
    @Autowired
    private ChoInnerFinanceDao choInnerFinanceDao;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private IWorkFlowService workFlowServiceImpl;
    @Autowired
    private ChoMessageService choMessageService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
	private IFcBusRaceService fcBusRaceService;
    @DubboReference(check = false)
	private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
	private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
	private IFcInterestService fcInterestService;
    @DubboReference(check = false)
	private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;

    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @DubboReference(check = false)
    private IDepositRateTypeService depositRateTypeService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(ChoInnerFinance entity) {
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
    public void save(ChoInnerFinance entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(ChoInnerFinance entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(ChoInnerFinance entity) {
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<ChoInnerFinance, String> getEntityDao() {
        return choInnerFinanceDao;
    }
    /**
     * 查询理财信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String busType = (String) dataTablesInput.getFormdata().get("busType");
        String opType = (String) dataTablesInput.getFormdata().get("opType");//区分定转活 通知转活
        String joinSql = "";
        String resource = "";
        if (StringUtils.isNotEmpty(opType) && "listSel".equals(opType)) {
            //获取已补录且未取款完成的的活期转定或活期转通知
            joinSql = " and ((select bt_nvl_num(sum(t.pay_money), 0) " +
                    " from cho_inner_finance t" +
                    " where t.related_Bill_Id = f.id" +
                    " and t.bus_type in ("+InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")" +
                    " and t.status >="+InnerFinanceConstant.STATUE_BACK+") < f.pay_money)";
            joinSql += " and not exists (select 1 from cho_inner_finance c where f.id = c.related_bill_id and c.bus_type = 15 and c.status <> -2)" ;
            joinSql += " and f.bus_type=" + busType;
            if(InnerFinanceConstant.BUS_TYPE_N2B.toString().equals(busType)){
                joinSql += " and f.status="+InnerFinanceConstant.STATUE_CHECK;
            }else{
                joinSql += " and f.status="+InnerFinanceConstant.STATUE_TALLY_CHECKPASS;
                //取款控制起息日期小于等于系统当前日期
                if(InnerFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)
                        || InnerFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)){
                    String curDate = DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE);
                    joinSql += " and f.interest_Start_Date<=bt_to_date('"+curDate+"','yyyy-MM-dd')";
                }
                //定期存款过滤已自动处理的单据
                if(InnerFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)){
                    joinSql += " and f.pay_Date is null";
                }
            }
            DataTablesOutput output= choInnerFinanceDao.getDataTablesQuery(dataTablesInput, joinSql);
            List<ChoInnerFinanceVO> data = output.getData();
            if ( null != data && !data.isEmpty()){
                List<String> payerAccs = data.stream().map(ChoInnerFinanceVO::getPayerAcc).collect(Collectors.toList());
                List<FcAccDTO> fcAccs = fcAccService.getFcAccByBlnctrAccs(payerAccs);
                if ( null != fcAccs && !fcAccs.isEmpty()){
                    Map<String, String> fcAccsMap = fcAccs.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
                    data.stream().forEach(vo ->{
                        if (StringUtils.isBlank(vo.getPayerAccName())) {
                            vo.setPayerAccName(fcAccsMap.get(vo.getPayerAcc()));
                        }
                        if (StringUtils.isBlank(vo.getInterestAcc())) {
                            vo.setInterestAccName(fcAccsMap.get(vo.getInterestAcc()));
                        }
                    });
                }
            }
            output.setData(data);
            return output;
        } else {
            //获取对应业务类型的暂存和打回状态的数据
            if (StringUtils.isNotEmpty(busType)) {
                if (InnerFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCEC2T_CHOINNERFINANCE_QUERY;
                } else if (InnerFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCEC2N_CHOINNERFINANCE_QUERY;
                } else if (InnerFinanceConstant.BUS_TYPE_T2C.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCET2C_CHOINNERFINANCE_QUERY;
                } else if (InnerFinanceConstant.BUS_TYPE_N2C.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCEN2C_CHOINNERFINANCE_QUERY;
                } else if (InnerFinanceConstant.BUS_TYPE_N2B.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCEN2B_CHOINNERFINANCE_QUERY;
                } else if (InnerFinanceConstant.BUS_TYPE_B2N.toString().equals(busType)) {
                    resource = ResourceConstant.CHO_INNERFINANCEB2N_CHOINNERFINANCE_QUERY;
                }
                joinSql = " and f.status in (" + InnerFinanceConstant.STATUE_TEMP + "," + InnerFinanceConstant.STATUE_BACK + ")";
                joinSql += " and f.bus_type=" + busType + " and f.create_by ='" + user.getLoginName() + "'";
            } else {
                resource = ResourceConstant.CHO_INNERFINANCE_CHOINNERFINANCE_QUERY;
            }
            if (!StringUtils.isBlank(resource)) {
                String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
                joinSql += " and " + corpAuthSql;
            }
            DataTablesOutput output = choInnerFinanceDao.getDataTablesUnion(dataTablesInput, joinSql);
            List<ChoInnerFinanceVO> data = output.getData();
            if (CollectionUtil.isNotEmpty(data)){
                Map<String, FcAccDTO> fcAccMap = fcAccService.listAllFcAcc();
                if(fcAccMap != null){
                    for(ChoInnerFinanceVO vo : data){
                        if(fcAccMap.containsKey(vo.getPayerAcc())){
                            FcAccDTO fcAccDTO = fcAccMap.get(vo.getPayerAcc());
                            if(fcAccDTO != null){
                                vo.setPayeeAccName(fcAccDTO.getAccName());
                            }
                        }
                    }
                }
            }
            return output;
        }
    }
    /**
     * 根据ID获取理财信息
     *
     * @param id
     * @return
     */
    public ChoInnerFinanceVO getByIdUnion(String id) {
        ChoInnerFinanceVO choInnerFinanceVO = choInnerFinanceDao.getByIdUnion(id);
        if(!EmptyUtils.isEmpty(choInnerFinanceVO.getPayMoney()) && !EmptyUtils.isEmpty(choInnerFinanceVO.getDrawMoney())) {
            choInnerFinanceVO.setTimeBalance(choInnerFinanceVO.getPayMoney().subtract(choInnerFinanceVO.getDrawMoney()));
            choInnerFinanceVO.setAllowBookMoney(choInnerFinanceVO.getTimeBalance().subtract(choInnerFinanceVO.getBookMoney()));
        }
        com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(choInnerFinanceVO.getPayerAcc());
        if (bal != null) {
            //内部账号余额 = 可用余额 - 冻结金额
            choInnerFinanceVO.setBalance(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
        }
        return choInnerFinanceVO;
    }
    /**
     * 根据billNo获取理财信息
     *
     * @param billNo
     * @return
     */
    public ChoInnerFinanceVO getByBillNo(String billNo) {
        ChoInnerFinanceVO choInnerFinanceVO = choInnerFinanceDao.getByBillNo(billNo);
        if(choInnerFinanceVO==null){
            return null;
        }
        if(!EmptyUtils.isEmpty(choInnerFinanceVO.getPayMoney()) && !EmptyUtils.isEmpty(choInnerFinanceVO.getDrawMoney())) {
            choInnerFinanceVO.setTimeBalance(choInnerFinanceVO.getPayMoney().subtract(choInnerFinanceVO.getDrawMoney()));
            choInnerFinanceVO.setAllowBookMoney(choInnerFinanceVO.getTimeBalance().subtract(choInnerFinanceVO.getBookMoney()));
        }
        com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(choInnerFinanceVO.getPayerAcc());
        if (bal != null) {
            //内部账号余额 = 可用余额 - 冻结金额
            choInnerFinanceVO.setBalance(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
        }
        return choInnerFinanceVO;
    }
    /**
     * 保存内部理财信息
     *
     * @param choInnerFinance
     */
    public void saveInnerFinance(ChoInnerFinanceVO choInnerFinance, ICurrentUser user) {
        ChoInnerFinance entity = new ChoInnerFinance();
        BeanUtils.copyNotNullProperties(choInnerFinance, entity);
        entity.setBillNo(this.getBillNoByBusType(choInnerFinance.getBusType()));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        entity.setPeriodName(choInnerFinance.getDepositRateTypeName());
        // 冗余单位和币种
        Optional.ofNullable(entity.getCorpId()).flatMap(corpId -> Optional.ofNullable(corpService.get(corpId)))
                .ifPresent(corp -> entity.setCorpName(corp.getName()));
        Optional.ofNullable(entity.getCurId()).flatMap(curId -> Optional.ofNullable(btCurrencyService.getObjectById(curId)))
                .ifPresent(currency -> entity.setCurName(currency.getCurName()));
        DepositRateTypeDTO depositRateType = depositRateTypeService.getDepositRateTypeById(entity.getDepositRateType());
        //entity.setDepositRateType("");period_name
        if (null != depositRateType){
            entity.setPeriodName(depositRateType.getName());
        }
        FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(choInnerFinance.getPayerAcc());//查询账户
        if(null != fcAccDTO){
            entity.setPayerAccName(fcAccDTO.getAccName());
        }
        this.saveEntity(entity);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        if (InnerFinanceConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
            //调用审批后方法
             getStartHandler().handler(this.getWorkFlowByBusType(entity.getBusType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_INNER, user);
    }
    /**
     * 根据业务类型返回不同的业务单号
     *
     * @param busType
     * @return
     */
    private String getBillNoByBusType(Integer busType) {
        /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
        String billNo = "";
        if (InnerFinanceConstant.BUS_TYPE_C2T.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_C2T);
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_T2C);
        } else if (InnerFinanceConstant.BUS_TYPE_C2N.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_C2N);
        } else if (InnerFinanceConstant.BUS_TYPE_N2C.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_N2C);
        } else if (InnerFinanceConstant.BUS_TYPE_N2B.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_N2B);
        } else if (InnerFinanceConstant.BUS_TYPE_B2N.intValue() == busType.intValue()) {
            billNo = SequenceSingleton.getSequence(SequenceEnum.CHO_INNER_FINANCE_B2N);
        }
        return billNo;
    }
    /**
     * 修改内部理财信息
     *
     * @param choInnerFinance
     */
    public void updateInnerFinance(ChoInnerFinanceVO choInnerFinance, ICurrentUser user) {
        ChoInnerFinance entity = this.get(choInnerFinance.getId());
        BeanUtils.copyNotNullProperties(choInnerFinance, entity);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_TEMP;
        BeanUtils.copyNotNullProperties(choInnerFinance, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        entity.setTallySign(null);
        this.saveOrUpdate(entity);
        if (InnerFinanceConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_SUBMIT;
            //调用审批后方法
             getStartHandler().handler(this.getWorkFlowByBusType(entity.getBusType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_INNER, user);
        //完成通知提醒
        String tag = "";
        if (InnerFinanceConstant.BUS_TYPE_C2T.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2T;
        } else if (InnerFinanceConstant.BUS_TYPE_C2N.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_T2C;
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2N;
        } else if (InnerFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_N2C;
        } else if (InnerFinanceConstant.BUS_TYPE_N2B.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_N2B;
        } else if (InnerFinanceConstant.BUS_TYPE_B2N.equals(entity.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_B2N;
        }
        if (!StringUtils.isBlank(tag)) {
            //消息提醒
            notificationService.complete(tag, entity.getId(), user.getId());
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoInnerFinance entity = this.get(workFlowResult.getBusinessKey(), true);
        Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        if (workFlowResult.isPass()) {
            messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            entity.setStatus(InnerFinanceConstant.STATUE_CHECK);//审批通过
        } else {
            entity.setStatus(InnerFinanceConstant.STATUE_BACK);//审批打回
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //审批通过，才更新预约信息
        if (workFlowResult.isPass()) {
            if(InnerFinanceConstant.BUS_TYPE_B2N.equals(entity.getBusType())){
                //取消通知预约
                //更新预约信息状态为取消预约
                ChoInnerFinance relatedBill = this.get(entity.getRelatedBillId(), true);
                relatedBill.setStatus(InnerFinanceConstant.STATUE_CANCEL);
                this.saveOrUpdate(relatedBill);
            }
            if(InnerFinanceConstant.BUS_TYPE_C2T.equals(entity.getBusType())||
                    InnerFinanceConstant.BUS_TYPE_T2C.equals(entity.getBusType())||
                    InnerFinanceConstant.BUS_TYPE_C2N.equals(entity.getBusType())||
                    InnerFinanceConstant.BUS_TYPE_N2C.equals(entity.getBusType())) {
                //如果审批通过自动生成记账待审批
                //完成通知提醒
                String tag = NotificationConstant.FINACIAL.INNER_DEPOSIT_TALLY;
                if (!StringUtils.isBlank(tag)) {
                    //消息提醒
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("money", NumberUtil.formatNumeric(entity.getPayMoney(), "#,##0.00"));
                    notificationService.sendToUserByResPermission(entity.getId(), com.bytter.biz.finacial.api.constant.ResourceConstant.FINACIAL_INNERDEPOSIT_APPLY, tag, map, entity.getCorpId());
                }
            }
        }
        //生成日志
        choMessageService.saveMessage(entity.getId(), messageType, "", ChoMessageConstant.BUS_TYPE_INNER, user);
    }
    /**
     * 根据业务类型返回不同的跳转页面
     *
     * @param busType
     * @return
     */
    public String getWorkFlowByBusType(Integer busType) {
        /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
        String workFlow = "";
        if (InnerFinanceConstant.BUS_TYPE_C2T.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_C2T;
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_T2C;
        } else if (InnerFinanceConstant.BUS_TYPE_C2N.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_C2N;
        } else if (InnerFinanceConstant.BUS_TYPE_N2C.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_N2C;
        } else if (InnerFinanceConstant.BUS_TYPE_N2B.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_N2B;
        } else if (InnerFinanceConstant.BUS_TYPE_B2N.intValue() == busType.intValue()) {
            workFlow = ChoWorkFlowConstant.CHO_INNER_B2N;
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
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqi_busType"))) {
            String type = String.valueOf(dataTablesInput.getFormdata().get("eqi_busType"));
            if (EmptyUtils.isNumeric(type)) {
                sign = this.getWorkFlowByBusType(new Integer(type));
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "f.id", true);
        DataTablesOutput<ChoInnerFinanceVO> dp = this.choInnerFinanceDao.dataTableApprove(dataTablesInput, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 逻辑删除理财信息
     *
     * @param id
     */
    public void delChoInnerFinance(String id, ICurrentUser user) {
        ChoInnerFinance choInnerFinance = this.get(id);
        choInnerFinance.setStatus(InnerFinanceConstant.STATUE_DEL);
        choInnerFinance.setUpdateBy(user.getLoginName());
        choInnerFinance.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(choInnerFinance);
        //生成日志
        choMessageService.saveMessage(choInnerFinance.getId(), ChoMessageConstant.MESSAGE_TYPE_DELETE, "", ChoMessageConstant.BUS_TYPE_INNER, user);
        //完成通知提醒
        String tag = "";
        if (InnerFinanceConstant.BUS_TYPE_C2T.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2T;
        } else if (InnerFinanceConstant.BUS_TYPE_C2N.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_T2C;
        } else if (InnerFinanceConstant.BUS_TYPE_T2C.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2N;
        } else if (InnerFinanceConstant.BUS_TYPE_N2C.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_N2C;
        } else if (InnerFinanceConstant.BUS_TYPE_N2B.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_N2B;
        } else if (InnerFinanceConstant.BUS_TYPE_B2N.equals(choInnerFinance.getBusType())) {
            tag = NotificationConstant.CHOWMATISTIC.INNER_FINANCE_B2N;
        }
        if (!StringUtils.isBlank(tag)) {
            //消息提醒
            notificationService.complete(tag, choInnerFinance.getId(), user.getId());
        }
    }
    /**
     * 存款类业务查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, ICurrentUser user) {
        String resource = ResourceConstant.CHO_INNERFINANCE_CHOINNERFINANCE_QUERY;;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
        String joinSql = " and " + corpAuthSql;
        DataTablesOutput dataTablesQuery = choInnerFinanceDao.getDataTablesQuery(dataTablesInput, joinSql);
        List<ChoInnerFinanceVO> data = dataTablesQuery.getData();
        if ( null != data && !data.isEmpty()){
            List<String> accs = data.stream().map(ChoInnerFinanceVO::getPayerAcc).collect(Collectors.toList());
            accs.addAll(data.stream().map(ChoInnerFinanceVO::getPayeeAcc).collect(Collectors.toList()));
            List<String> accsNew = Lists.newArrayList();
            for (String acc : accs) {
                if (StringUtils.isNotBlank(acc) && !accsNew.contains(acc)){
                    accsNew.add(acc);
                }
            }
            List<FcAccDTO> fcAccs = fcAccService.getFcAccByBlnctrAccs(accsNew);
            if ( null != fcAccs && !fcAccs.isEmpty()){
                Map<String, String> fcAccsMap = fcAccs.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, FcAccDTO::getAccName));
                data.stream().forEach(vo ->{
                    if (StringUtils.isBlank(vo.getPayeeAccName())) {
                        vo.setPayeeAccName(fcAccsMap.get(vo.getPayeeAcc()));
                    }
                    if (StringUtils.isBlank(vo.getPayerAccName())) {
                        vo.setPayerAccName(fcAccsMap.get(vo.getPayerAcc()));
                    }
                });
            }
        }
        DataTableOutPutOrderUtils.orderOutputList(dataTablesInput, data);
        dataTablesQuery.setData(data);
        return dataTablesQuery;
    }
    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        ChoInnerFinance entity = this.get(workFlowResult.getBusinessKey(), true);
        entity.setStatus(InnerFinanceConstant.STATUE_TEMP);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        //生成日志
        choMessageService.saveMessage(entity.getId(), ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", ChoMessageConstant.BUS_TYPE_INNER, user);
    }
    /**
     * 根据业务类型和状态获取内部理财信息集合
     * @param busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
     * @param status 状态
     * @return
     */
    public DataTablesOutput listByBusType(DataTablesInput input,Integer busType, String status,String opType, String userId, List<String> corpAuth) {
        return choInnerFinanceDao.listByBusType(input,busType, status,opType,userId,corpAuth);
    }
    /**
     * 根据ID获取理财信息
     * @param id
     * @return
     */
    public ChoInnerFinanceDTO getInnerDepositById(String id){
        return choInnerFinanceDao.getInnerDepositById(id);
    }
    /**
     * 根据billNo获取理财信息
     * @param billNo
     * @return
     */
    public ChoInnerFinanceDTO getInnerDepositByBillNo(String billNo){
        return choInnerFinanceDao.getInnerDepositByBillNo(billNo);
    }
    /**
     * 银行存款应收未收利息统计表
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesReport(DataTablesInput dataTablesInput, ICurrentUser user) {
        String joinSql = "";
        String resource = ResourceConstant.CHO_INNERFINANCE_CHOINNERFINANCE_REPORT_QUERY;
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "f.corp_Id", true);
            joinSql += " and " + corpAuthSql;
        }
        //获取取款成功的明细
        DataTablesOutput datas = choInnerFinanceDao.getDataTablesReport(dataTablesInput, joinSql);
        if (datas != null && datas.getData() != null) {
            List<ChoInnerFinanceVO> list = datas.getData();
            for (ChoInnerFinanceVO vo : list) {
                if(vo.getInterestRate()==null){
                    vo.setInterestRate(BigDecimal.ZERO);
                }
                if(EmptyUtils.isEmpty(vo.getAccInterestRate())){
                    vo.setAccInterestRate(BigDecimal.ZERO);
                }
                //计算天数 = 截止日期 - 起息日期
                int calDays = DateUtils.dayDiffs(vo.getCalEndDate(), vo.getInterestStartDate());
                if(InnerFinanceConstant.BUS_TYPE_C2T.equals(vo.getBusType())){
                    if(DateUtils.dayDiffs(vo.getCalEndDate(), vo.getDeadlineDate())==0){
                        //定期存款：截止日期等于到期日期
                        //应收利息 = 金额 * 定期利率 * （截止日期-起息日期）/360
                        vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getInterestRate()));
                        vo.setInterestReceivable(vo.getInterestReceivable().multiply(new BigDecimal(calDays)));
                        vo.setInterestReceivable(vo.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                    }else if(DateUtils.dayDiffs(vo.getCalEndDate(), vo.getDeadlineDate())>0){
                        //定期存款：截止日期大于到期日期
                        //应收利息 = 金额 * 利率 * （到期日期-起息日期）/360+金额 * 活期利率 * （截止日期-到期日期）
                        vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getInterestRate()));
                        vo.setInterestReceivable(vo.getInterestReceivable().multiply(new BigDecimal(DateUtils.dayDiffs(vo.getDeadlineDate(), vo.getInterestStartDate()))));
                        vo.setInterestReceivable(vo.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, BigDecimal.ROUND_HALF_UP));
                        //到期处理方式：0-自动终止，1-本金续存，2-本息续存
                        BigDecimal curInterest = vo.getPayMoney();
                        if (InnerFinanceConstant.CERTIFICATE_WAY_2.equals(vo.getCertificateWay().toString())) {
                            curInterest = curInterest.add(vo.getInterestReceivable());
                        }
                        curInterest = curInterest.multiply(vo.getAccInterestRate());
                        curInterest = curInterest.multiply(new BigDecimal(DateUtils.dayDiffs(vo.getCalEndDate(), vo.getDeadlineDate())));
                        curInterest = curInterest.divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP);
                        vo.setInterestReceivable(vo.getInterestReceivable().add(curInterest));
                    }else {
                        //定期存款：截止日期小于等于到期日期
                        //应收利息 = 金额 * 活期利率 * （截止日期-起息日期）/360
                        vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getAccInterestRate()));
                        vo.setInterestReceivable(vo.getInterestReceivable().multiply(new BigDecimal(calDays)));
                        vo.setInterestReceivable(vo.getInterestReceivable().divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                    }
                }else{
                    //通知存款：根据通知类型计算利息
                    if(CommonConstant.YES.equals(vo.getNoticeType())){
                        //1天通知
                        vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getInterestRate()));
                        vo.setInterestReceivable(vo.getInterestReceivable().multiply(new BigDecimal(calDays)));
                        vo.setInterestReceivable(vo.getInterestReceivable().divide( new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                    }else{
                        vo.setInterestReceivable(vo.getPayMoney().multiply(vo.getInterestRate()));
                        vo.setInterestReceivable(vo.getInterestReceivable().multiply(new BigDecimal((calDays/7*7))));
                        vo.setInterestReceivable(vo.getInterestReceivable().divide( new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                        if(vo.getAccInterestRate() == null){
                            vo.setAccInterestRate(BigDecimal.ZERO);
                        }
                        BigDecimal curInterest = vo.getPayMoney().multiply(vo.getAccInterestRate());
                        curInterest = curInterest.multiply(new BigDecimal((calDays%7)));
                        curInterest = curInterest.divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP);
                        vo.setInterestReceivable(vo.getInterestReceivable().add(curInterest));
                    }
                }
                //截止日内存在支取：应收利息 = 转账金额*支取利率*（转账日期-起息日期）+（金额-转账金额）*补录利率*（截止日期-转账日期）
                //获取定转活或通知转活的数据
                List<ChoInnerFinanceVO> drawList = choInnerFinanceDao.listByRelatedBillId(vo.getId());
                BigDecimal sumDrawMoney = BigDecimal.ZERO;//累计取款金额
                BigDecimal sumInterest = BigDecimal.ZERO;//累计实收利息
                for (ChoInnerFinanceVO draw:drawList) {
                    //计算截止日期内已完成的取款
                    if(DateUtils.dayDiffs(vo.getCalEndDate(), draw.getPayDate())>=0) {
                        sumDrawMoney = sumDrawMoney.add(draw.getPayMoney());
                        sumInterest = sumInterest.add(draw.getExpectInterest());
                    }
                }
                vo.setDrawMoney(sumDrawMoney);
                vo.setInterestEarned(sumInterest);
                vo.setCalDays(calDays);//计算天数
                vo.setInterestReceivableNot(vo.getInterestReceivable().subtract(vo.getInterestEarned()));//应收未收利息 = 应收利息-实收利息
            }
        }
        return datas;
    }
    /**
     * 指令交易成功/失败消息提醒
     */
    @Override
    @XxlJob("ChoInnerFinanceService")
    public void run(String arguments) {
        String jobParam = XxlJobHelper.getJobParam();
        arguments = jobParam;
        logger.info("定时任务接收参数：【{}】", jobParam);
        List<ChoInnerFinanceVO> list = null;
        String alertDays = null;
        Date beginDate = DateUtils.parseDate(DateUtils.formatDate(DateUtils.getDateYMD(),DateUtils.PATTERN_CLASSICAL_SIMPLE));
        Date endDate = beginDate;
        if (NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2T_EXPIRATION.equals(arguments)) {
            //定期存款提醒
            alertDays = configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_REGULARALERTDAYS).getValue();
            endDate = DateUtils.addDay(beginDate, Integer.parseInt(alertDays));
            list = this.choInnerFinanceDao.listByBusType(InnerFinanceConstant.BUS_TYPE_C2T, beginDate, endDate);
        } else if (NotificationConstant.CHOWMATISTIC.INNER_FINANCE_C2N_EXPIRATION.equals(arguments)) {
            //通知存款提醒
            alertDays = configService.getConfig(InnerFinanceConstant.CHO_INNER_CONFIG_TYPE, InnerFinanceConstant.CHO_INNER_CONFIG_NOTICEALERTDAYS).getValue();
            endDate = DateUtils.addDay(beginDate, Integer.parseInt(alertDays));
            list = this.choInnerFinanceDao.listByBusType(InnerFinanceConstant.BUS_TYPE_C2N, beginDate, endDate);
        } else if (NotificationConstant.CHOWMATISTIC.INNER_FINANCE_N2B_EXPIRATION.equals(arguments)) {
            //通知预约提醒
            list = this.choInnerFinanceDao.listByBusType(InnerFinanceConstant.BUS_TYPE_N2B, beginDate, endDate);
        }
        if (!EmptyUtils.isEmptyList(list)) {
            //完成消息提醒
            notificationService.completeListByTag(arguments);
            //生成利息明细
            List<FcInterestDtlDTO> fcInterestDtlList = new ArrayList<>();
            for (ChoInnerFinanceVO vo : list) {
                if(InnerFinanceConstant.BUS_TYPE_N2B.equals(vo.getBusType()) && (vo.getDrawMoney() == null || vo.getDrawMoney().compareTo(BigDecimal.ZERO) == 0)){
                    //通知预约自动取款
                    this.autoCreateInnerFinance(vo, CommonConstant.YES);
                    //更新标识，表示取款完成
                    ChoInnerFinance entity = this.get(vo.getId(), true);
                    entity.setStatus(InnerFinanceConstant.STATUE_COMPLETE);
                    this.saveOrUpdate(entity);
                }else {
                    //定期存款和通知存款，直接根据到期处理方式处理
                    //当天到期，无需提醒
                    if (DateUtils.dayDiffs(vo.getDeadlineDate(), beginDate) == 0 && (vo.getDrawMoney() == null || vo.getDrawMoney().compareTo(BigDecimal.ZERO) == 0)) {
                        //到期处理方式：0-自动终止，1-本金续存，2-本息续存
                        if (InnerFinanceConstant.CERTIFICATE_WAY_0.equals(vo.getCertificateWay().toString())) {
                            //0-自动终止：生成审批通过的取款信息（取款金额=存款金额）
                            this.autoCreateInnerFinance(vo, CommonConstant.YES);
                        } else {
                            //获取网点的利息支出账号
                            //List<FcAccDTO> listDebitFcAcc = innerAccountService.listAccountByParamsAndGrant(vo.getCurId(), vo.getNetId(), vo.getNetId(), null, "5211", null);
                            //获取定期/通知账户
                            FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(vo.getPayeeAcc());
                            FcAccDTO debitFcAcc = null;
                            if(fcAccDTO!=null && StringUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {
                                debitFcAcc = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                            }else {
                                //修改：获取利息支出账号根据批量计息分录方式来
                                int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                                List<FcAccDTO> listDebitFcAcc = null;
                                if (entrySign == 0) {
                                    listDebitFcAcc = innerAccountService.listAccountByParamsAndGrant(vo.getCurId(), vo.getNetId(), vo.getNetId(), null, "5211", null);
                                } else {
                                    listDebitFcAcc = innerAccountService.listAccountByParamsAndGrant(vo.getCurId(), vo.getNetId(), vo.getCorpId(), null, "5211", null);
                                }
                                if (!EmptyUtils.isEmptyList(listDebitFcAcc)) {
                                    debitFcAcc = listDebitFcAcc.get(0);
                                }
                            }
                            if (debitFcAcc != null) {
                                if (InnerFinanceConstant.CERTIFICATE_WAY_1.equals(vo.getCertificateWay().toString())) {
                                    //1-本金续存：生成审批通过的取款信息（取款金额=0），生成审批通过的存款信息
                                    this.autoCreateInnerFinance(vo, CommonConstant.YES);
                                    this.autoCreateInnerFinance(vo, CommonConstant.NO);
                                } else if (InnerFinanceConstant.CERTIFICATE_WAY_2.equals(vo.getCertificateWay().toString())) {
                                    //2-本息续存：生成审批通过的取款信息（取款金额=0），生成审批通过的存款信息
                                    this.autoCreateInnerFinance(vo, CommonConstant.YES);
                                    this.autoCreateInnerFinance(vo, CommonConstant.NO);
                                }
                                //记利息凭证
                                //本金续存到期日当天会记利息账：借：利息支出-定期存款    贷：企业活期存款
                                //本息续存到期日当天会记利息账：借：利息支出-定期存款   贷：企业定期存款
                                VoucherWrapperDTO voucher=this.submitVoucher(vo, debitFcAcc);
                                FcInterestDtlDTO fcInterestDtlDTO = new FcInterestDtlDTO();
                                fcInterestDtlDTO.setInterestTime(DateUtils.getDateYMD());
                                //获取定期/通知账号
                                FcAccDTO fcAcc = innerAccountService.getByBlnctrAcc(vo.getPayeeAcc());
                                fcInterestDtlDTO.setAccount(vo.getPayeeAcc());
                                fcInterestDtlDTO.setCurCode(fcAcc.getCurId());
                                fcInterestDtlDTO.setInterestType(InterestConstant.INSTRT_TYPE_NORMAL);
                                fcInterestDtlDTO.setGenerateMode(InterestConstant.GENERATEMODE_INTEREST_PART);
                                fcInterestDtlDTO.setInterestStartDate(vo.getInterestStartDate());
                                fcInterestDtlDTO.setBlnInstDate(DateUtils.addDay(DateUtils.getDateYMD(),-1));
                                //FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
                                fcInterestDtlDTO.setInterestRate(vo.getInterestRate());
                                //利息=存款金额*（取款日期-起息日期）*利率/360
                                int dayLimit = DateUtils.dayDiffs(DateUtils.getDateYMD(),vo.getInterestStartDate());
                                /*
                                if (bal != null) {
                                    fcInterestDtlDTO.setCumulation((bal.getCheckBalance().subtract(bal.getFreezeMoney())));
                                } else {
                                    fcInterestDtlDTO.setCumulation(BigDecimal.ZERO);
                                }*/
                                fcInterestDtlDTO.setCumulation(vo.getPayMoney().multiply(new BigDecimal(dayLimit)));
                                fcInterestDtlDTO.setDays(dayLimit);
                                fcInterestDtlDTO.setInterest(vo.getPayMoney().multiply(new BigDecimal(dayLimit)).multiply(vo.getInterestRate()).divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                                fcInterestDtlDTO.setRepayMoney(vo.getPayMoney());
                                fcInterestDtlDTO.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
                                fcInterestDtlDTO.setTallyDate(DateUtils.getDateYMD());
                                fcInterestDtlDTO.setCreateTime(DateUtils.getDate());
                                UserDTO user = userService.getByLoginName(vo.getCreateBy());
                                fcInterestDtlDTO.setCreateBy(vo.getCreateBy());
                                fcInterestDtlDTO.setOperatorCode(user.getId());
                                fcInterestDtlDTO.setVoucherNo(voucher.getVoucherNo());
                                fcInterestDtlDTO.setOrderNo(voucher.getBookVoucher().getAccbook().getOrderNo());
                                fcInterestDtlList.add(fcInterestDtlDTO);
                                //更新取款日期，表示取款完成
                                ChoInnerFinance entity = this.get(vo.getId(), true);
                                entity.setPayDate(DateUtils.getDateYMD());
                                this.saveOrUpdate(entity);
                            }
                        }
                    } else {
                        //消息提醒，先清空提醒
                        notificationService.send(vo.getId(), vo.getCorpId(), arguments, vo);
                    }
                }
            }
            //生成利息明细
            if(!EmptyUtils.isEmptyList(fcInterestDtlList)){
                this.fcInterestService.saveFcInterestDtl(fcInterestDtlList, null);
            }
        }
    }
    /**
     * 自动生成存/取款信息
     * @param vo
     */
    public ChoInnerFinance autoCreateInnerFinance(ChoInnerFinanceVO vo, String flag){
        ChoInnerFinance entity = new ChoInnerFinance();
        CorpDTO corpDTO = corpService.get(vo.getCorpId());
        if(null != corpDTO){
            entity.setCorpName(corpDTO.getName());
        }
        BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(vo.getCurId());
        if(null != btCurrencyDTO){
            entity.setCurName(btCurrencyDTO.getCurName());
        }
        entity.setCorpId(vo.getCorpId());
        entity.setRelatedBillId(vo.getId());
        if(CommonConstant.YES.equals(flag)) {
            //自动生成取款信息
            if (InnerFinanceConstant.BUS_TYPE_C2T.equals(vo.getBusType())) {
                //定期取款
                entity.setBillNo(this.getBillNoByBusType(InnerFinanceConstant.BUS_TYPE_T2C));
                entity.setBusType(InnerFinanceConstant.BUS_TYPE_T2C);
            } else if (InnerFinanceConstant.BUS_TYPE_C2N.equals(vo.getBusType()) || InnerFinanceConstant.BUS_TYPE_N2B.equals(vo.getBusType())) {
                //通知取款
                entity.setBillNo(this.getBillNoByBusType(InnerFinanceConstant.BUS_TYPE_N2C));
                entity.setBusType(InnerFinanceConstant.BUS_TYPE_N2C);
            }
            //取款付款方信息=存款收款方信息
            entity.setPayerAccId(vo.getPayeeAccId());
            entity.setPayerAcc(vo.getPayeeAcc());
            //取款收款方信息=存款付款方信息
            entity.setPayeeAccId(vo.getPayerAccId());
            entity.setPayeeAcc(vo.getPayerAcc());
            entity.setCapitalAccId(vo.getPayeeAccId());
            entity.setInterestAccId(vo.getPayeeAccId());
            entity.setInterestStartDate(vo.getInterestStartDate());
            entity.setDeadlineDate(vo.getDeadlineDate());
            entity.setInterestRate(vo.getInterestRate());
            entity.setPayDate(vo.getDeadlineDate());
            //通知预约取款，生成暂存的取款信息
            if (InnerFinanceConstant.BUS_TYPE_N2B.equals(vo.getBusType())) {
                entity.setPayMoney(vo.getPayMoney());
                entity.setExpectInterest(vo.getExpectInterest());
                entity.setStatus(InnerFinanceConstant.STATUE_TEMP);
                //通知预约生成的取款信息单号取存款单的单号
                entity.setRelatedBillId(vo.getRelatedBillId());
            }else {
                //利息=存款金额*（到期日期-起息日期）*利率/360
                int dayLimit = DateUtils.dayDiffs(vo.getDeadlineDate(), vo.getInterestStartDate());
                entity.setExpectInterest(vo.getPayMoney().multiply(new BigDecimal(dayLimit)).multiply(vo.getInterestRate()).divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
                //到期处理方式：0-自动终止，1-本金续存，2-本息续存
                if (InnerFinanceConstant.CERTIFICATE_WAY_0.equals(vo.getCertificateWay().toString())) {
                    //0-自动终止：生成审批通过的取款信息（取款金额=存款金额）
                    entity.setPayMoney(vo.getPayMoney());
                    entity.setStatus(InnerFinanceConstant.STATUE_CHECK);
                } else {
                    //1-本金续存：生成审批通过的取款信息（取款金额=0）
                    //2-本息续存：生成审批通过的取款信息（取款金额=0）
                    entity.setPayMoney(BigDecimal.ZERO);
                    entity.setStatus(InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
                }
            }
        }else{
            //自动生成存款信息
            entity.setBillNo(this.getBillNoByBusType(vo.getBusType()));
            entity.setBusType(vo.getBusType());
            //取款付款方信息=存款付款方信息
            entity.setPayerAccId(vo.getPayerAccId());
            entity.setPayerAcc(vo.getPayerAcc());
            //取款收款方信息=存款收款方信息
            entity.setPayeeAccId(vo.getPayeeAccId());
            entity.setPayeeAcc(vo.getPayeeAcc());
            //续存起息日期=原到期日期
            entity.setInterestStartDate(vo.getDeadlineDate());
            //续存起息日期=原到期日期
            entity.setDeadlineDate(DateUtils.addDay(vo.getDeadlineDate(), DateUtils.dayDiffs(vo.getDeadlineDate(), vo.getInterestStartDate())));
            entity.setInterestRate(vo.getInterestRate());
            //利息=存款金额*（到期日期-起息日期）*利率/360
            int dayLimit = DateUtils.dayDiffs(vo.getDeadlineDate(), vo.getInterestStartDate());
            entity.setExpectInterest(vo.getPayMoney().multiply(new BigDecimal(dayLimit)).multiply(vo.getInterestRate()).divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP));
            //到期处理方式：0-自动终止，1-本金续存，2-本息续存
            if (InnerFinanceConstant.CERTIFICATE_WAY_1.equals(vo.getCertificateWay().toString())) {
                //1-本金续存：生成审批通过的存款信息（存款金额=本金）
                entity.setPayMoney(vo.getPayMoney());
            } else {
                //2-本息续存：生成审批通过的取款信息（存款金额=本金+利息）
                entity.setPayMoney(vo.getPayMoney().add(entity.getExpectInterest()));
            }
            entity.setStatus(InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        }
        entity.setDepositRateType(vo.getDepositRateType());
        entity.setCurId(vo.getCurId());
        entity.setDayLimit(vo.getDayLimit());
        entity.setDateUnit(vo.getDateUnit());
        entity.setNoticeType(vo.getNoticeType());
        entity.setCertificateWay(vo.getCertificateWay());
        entity.setPurpose(vo.getPurpose());
        entity.setAddWord(vo.getAddWord());
        entity.setCreateDate(DateUtils.getDate());
        entity.setCreateBy(vo.getCreateBy());
        entity.setNetId(vo.getNetId());
        this.save(entity);
        return entity;
    }
    /**
     * 自动记账
     *
     * @param choInnerFinance
     * @param fcAccDTO
     */
    public VoucherWrapperDTO submitVoucher(ChoInnerFinanceVO choInnerFinance, FcAccDTO fcAccDTO) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        //构建账务账务凭证主档表记录
        FcAccbookDTO accBook = new FcAccbookDTO();
        //备注
        accBook.setRmk(choInnerFinance.getPurpose());
        accBook.setNetId(choInnerFinance.getNetId());
        //获取记账规则ID
        String sysRaceCode = "";
        Integer busOriType;
        //活期转定期
        sysRaceCode = FinaBookConstant.SysRaceCode.DPS_FIX_DRAW.get();
        busOriType = FinaBookConstant.BusOriType.CH_DEPOSIT_REG_TO_CURRENT_PAYMENT.get();
        accBook.setBusOriType(busOriType);
        //accBook.setBusRaceId(fcBusRaceService.getNetBusRace(sysRaceCode, accBook.getNetId(), user.getId()));
        accBook.setBusRaceId(fcBusRaceService.getNetBusRace(sysRaceCode, accBook.getNetId(), null));
        FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(sysRaceCode);
        //构建账务凭证分录明细记录
        List<FcAccbookDtlDTO> accbookDtls = getAccbookDtls(choInnerFinance, fcAccDTO, ruleDefine);
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(DateUtils.getDate());
        accBook.setInterestStartDate(DateUtils.getDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        voucher.setBusGlobalId(entity.getAccbook().getBusGlobalId());
        voucher.addTarget(entity);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        //设置用户
        //voucher.setUserCode(user.getId());
        voucher.setUserCode(null);
        voucher.setAutoCheck(true);
        voucher.setAutoAccounter(true);
        this.fcAccbookService.submitVoucher(voucher);
        //返回记账凭证号
        return voucher;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        if(EmptyUtils.isEmpty(fcBusinessRuleDefineService)){
            fcBusinessRuleDefineService = (IFcBusinessRuleDefineService) ApplicationContextUtil.getContext().getBean("fcBusinessRuleDefineServiceImpl");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;//ok
    }
    /**
     * 构建账务凭证分录明细记录
     * @param choInnerFinance
     * @return
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(ChoInnerFinanceVO choInnerFinance, FcAccDTO fcAccDTO, FcBusinessRuleDefineDTO ruleDefine) {
        //根据业务判断构建分录
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        //利息=存款金额*（到期日期-起息日期）*利率/360
        int dayLimit = DateUtils.dayDiffs(choInnerFinance.getDeadlineDate(), choInnerFinance.getInterestStartDate());
        BigDecimal payMoney = choInnerFinance.getPayMoney().multiply(new BigDecimal(dayLimit));
        payMoney = payMoney.multiply(choInnerFinance.getInterestRate());
        payMoney = payMoney.divide(new BigDecimal(InnerFinanceConstant.YEAR_DAYS), 6, RoundingMode.HALF_UP);
        //借方
        FcAccbookDtlDTO debitAccbookDtl = new FcAccbookDtlDTO();
        debitAccbookDtl.setAccount(fcAccDTO.getBlnctrAcc());
        debitAccbookDtl.setCurCode(fcAccDTO.getCurId());
        //设置借方发生额
        debitAccbookDtl.setDebitMoney(payMoney);
        debitAccbookDtl.setCreditMoney(BigDecimal.ZERO);
        debitAccbookDtl.setAccName(fcAccDTO.getAccName());
        debitAccbookDtl.setOrigVoucherNo(choInnerFinance.getBillNo());
        debitAccbookDtl.setOrigVoucherId(choInnerFinance.getId());
        debitAccbookDtl.setSummaryContent(choInnerFinance.getPurpose());
        accbookDtls.add(debitAccbookDtl);
        //贷方
        FcAccDTO creditFcAcc = null;
        //到期处理方式：1-本金续存，2-本息续存
        if (InnerFinanceConstant.CERTIFICATE_WAY_1.equals(choInnerFinance.getCertificateWay().toString())) {
            //本金续存到期日当天会记利息账：借：利息支出-定期存款    贷：企业活期存款
            creditFcAcc = innerAccountService.getByBlnctrAcc(choInnerFinance.getPayerAcc());
        } else {
            //本息续存到期日当天会记利息账：借：利息支出-定期存款   贷：企业定期存款
            creditFcAcc = innerAccountService.getByBlnctrAcc(choInnerFinance.getPayeeAcc());
        }
        FcAccbookDtlDTO creditAccbookDtl = new FcAccbookDtlDTO();
        creditAccbookDtl.setAccount(creditFcAcc.getBlnctrAcc());
        creditAccbookDtl.setCurCode(creditFcAcc.getCurId());
        //设置贷方发生额
        creditAccbookDtl.setCreditMoney(payMoney);
        creditAccbookDtl.setDebitMoney(BigDecimal.ZERO);
        creditAccbookDtl.setAccName(creditFcAcc.getAccName());
        creditAccbookDtl.setOrigVoucherNo(choInnerFinance.getBillNo());
        creditAccbookDtl.setOrigVoucherId(choInnerFinance.getId());
        creditAccbookDtl.setSummaryContent(choInnerFinance.getPurpose());
        accbookDtls.add(creditAccbookDtl);
        //获取预提金额
        BigDecimal rushPreDrawMoney = fcInterestService.getRushPreDrawMoney(choInnerFinance.getPayeeAcc(),null,null);
        if(rushPreDrawMoney != null && rushPreDrawMoney.compareTo(BigDecimal.ZERO)>0){
            //冲预提
            List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
            this.collectionSort(entrys);
            for (FcVoucherEntryDefineDTO entry : entrys) {
                FcAccbookDtlDTO accbookDtl = new FcAccbookDtlDTO();
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT) == entry.getBalanceWay().intValue() &&
                        Integer.valueOf(FinacialConstant.ENTRY_TYPE_30) == entry.getEntryType().intValue()) {//冲预提
                    if (null != entry.getSubjectCode()) {
                        //修改：获取利息支出账号根据批量计息分录方式来
                        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                        List<FcAccDTO> fcAccDTOList = null;
                        //获取定期/通知账号
                        FcAccDTO payeeAccDTO = innerAccountService.getByBlnctrAcc(choInnerFinance.getPayeeAcc());
                        if(payeeAccDTO!=null && StringUtils.isNotEmpty(payeeAccDTO.getInterestRecPay())){
                            fcAccDTOList = new ArrayList<>();
                            fcAccDTOList.add(innerAccountService.getByBlnctrAcc(payeeAccDTO.getInterestRecPay()));
                        }else {
                            if (entrySign == 0) {
                                fcAccDTOList = innerAccountService.listAccountByParamsAndGrant(choInnerFinance.getCurId(), choInnerFinance.getNetId(), choInnerFinance.getNetId(), entry.getSubjectCode(), null, null);
                            } else {
                                fcAccDTOList = innerAccountService.listAccountByParamsAndGrant(choInnerFinance.getCurId(), choInnerFinance.getNetId(), choInnerFinance.getCorpId(), entry.getSubjectCode(), null, null);
                            }
                        }
                        //贷：201002应付利息-企业定期存款    冲预提
                        if(!EmptyUtils.isEmptyList(fcAccDTOList)) {
                            accbookDtl.setAccount(fcAccDTOList.get(0).getBlnctrAcc());
                            accbookDtl.setCurCode(fcAccDTOList.get(0).getCurId());
                            accbookDtl.setAccName(fcAccDTOList.get(0).getAccName());
                            accbookDtl.setDebitMoney(BigDecimal.ZERO);
                            accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(rushPreDrawMoney));
                            accbookDtl.setSubjectCode(entry.getSubjectCode());
                            accbookDtl.setOrigVoucherNo(choInnerFinance.getBillNo());
                            accbookDtl.setOrigVoucherId(choInnerFinance.getId());
                            accbookDtl.setSummaryContent(choInnerFinance.getPurpose());
                            accbookDtls.add(accbookDtl);
                            if ((BigDecimal.ZERO.subtract(rushPreDrawMoney)).compareTo(BigDecimal.ZERO) == 0) {
                                continue;
                            }
                        }
                    }
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT) == entry.getBalanceWay().intValue() &&
                        Integer.valueOf(FinacialConstant.ENTRY_TYPE_30) == entry.getEntryType().intValue()) {//冲预提
                    if (null != entry.getSubjectCode()) {
                        //修改：获取利息支出账号根据批量计息分录方式来
                        int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                        List<FcAccDTO> fcAccDTOList = null;
                        if(fcAccDTO!=null){
                            fcAccDTOList = new ArrayList<>();
                            fcAccDTOList.add(fcAccDTO);
                        }else {
                            if (entrySign == 0) {
                                fcAccDTOList = innerAccountService.listAccountByParamsAndGrant(choInnerFinance.getCurId(), choInnerFinance.getNetId(), choInnerFinance.getNetId(), entry.getSubjectCode(), null, null);
                            } else {
                                fcAccDTOList = innerAccountService.listAccountByParamsAndGrant(choInnerFinance.getCurId(), choInnerFinance.getNetId(), choInnerFinance.getCorpId(), entry.getSubjectCode(), null, null);
                            }
                        }
                        //借：521001利息支出—企业定期存款   冲预提
                        if(!EmptyUtils.isEmptyList(fcAccDTOList)) {
                            accbookDtl.setAccount(fcAccDTOList.get(0).getBlnctrAcc());
                            accbookDtl.setCurCode(fcAccDTOList.get(0).getCurId());
                            accbookDtl.setAccName(fcAccDTOList.get(0).getAccName());
                            accbookDtl.setCreditMoney(BigDecimal.ZERO);
                            accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(rushPreDrawMoney));
                            accbookDtl.setSubjectCode(entry.getSubjectCode());
                            accbookDtl.setOrigVoucherNo(choInnerFinance.getBillNo());
                            accbookDtl.setOrigVoucherId(choInnerFinance.getId());
                            accbookDtl.setSummaryContent(choInnerFinance.getPurpose());
                            accbookDtls.add(accbookDtl);
                            if ((BigDecimal.ZERO.subtract(rushPreDrawMoney)).compareTo(BigDecimal.ZERO) == 0) {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return accbookDtls;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**生成日志*/
    public void tallyBackMessage(String id, ICurrentUser user) {
        choMessageService.saveMessage(id, ChoMessageConstant.MESSAGE_TYPE_TALLY_BACK, "", ChoMessageConstant.BUS_TYPE_INNER, user);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的内部存款（活期转定期）单据，不允许禁用！
            int num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_C2T);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_C2T,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_C2T_ERROR)));
            }
            //单位有在途的内部存款（定期转活期）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_T2C);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_T2C,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_T2C_ERROR)));
            }
            //单位有在途的内部存款（活期转通知）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_C2N);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_C2N,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_C2N_ERROR)));
            }
            //单位有在途的内部存款（通知预约）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_N2B);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_N2B,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_N2B_ERROR)));
            }
            //单位有在途的内部存款（取消通知预约）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_B2N);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_B2N,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_B2N_ERROR)));
            }
            //单位有在途的内部存款（通知转活期）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_N2C);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_N2C,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_N2C_ERROR)));
            }
            //单位有记账中的内部存款单，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceCountByCorpIdForTally(corpId);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FCL,
                        SysConfigConstant.BusType.FCL_INNER_TALLY,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_TALLY_ERROR)));
            }
            //单位有未完成取款的内部存款（活期转定期）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceDrawCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_C2T);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_C2T,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_C2T_DRAW_ERROR)));
            }
            //单位有未完成取款的内部存款（活期转通知）单据，不允许禁用！
            num = this.choInnerFinanceDao.getInnerFinanceDrawCountByCorpId(corpId, InnerFinanceConstant.BUS_TYPE_C2N);
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.CHOWMATISTIC,
                        SysConfigConstant.BusType.CHO_INNERFINANCE_C2N,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_INNERFINANCE_C2N_DRAW_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }
    /**
     * 查询待记账数据
     */
    public List<ChoInnerFinanceDTO> queryTallyByBusType(Integer busType) {
        // todo:调用核算接口
        return choInnerFinanceDao.queryTallyByBusType(busType);
    }
    /**
     * <br>
     * <b>功能：</b> 查询通知取款最近的通知预约记录 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/16 0016 <br>
     */
    public ChoInnerFinanceDTO queryLastN2BData(ChoInnerFinanceDTO entity) {
        return choInnerFinanceDao.queryLastN2BData(entity);
    }
    /**
     * <br>
     * <b>功能：</b> 更新通知预约记录 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/16 0016 <br>
     */
    public void updateLastN2BData(ChoInnerFinanceDTO entity) {
        choInnerFinanceDao.updateLastN2BData(entity);
    }
    public List<ChoInnerFinanceDTO> getChoInnerFinance(Integer[] status, Integer billType) {
        return choInnerFinanceDao.getChoInnerFinance(status, billType);
    }

    /**
     * 用于上面 this.getNoticeAccount 方法的 企业通知预约用到过滤
     * @return
     */
    public List<String> filterBankAcc(List<String> blnctrAccs){
        return choInnerFinanceDao.filterBankAcc(blnctrAccs);
    }
}