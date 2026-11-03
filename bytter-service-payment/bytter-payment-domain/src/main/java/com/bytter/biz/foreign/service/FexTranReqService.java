package com.bytter.biz.foreign.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;
import com.bytter.biz.bdlinks.service.BisForeignExcService;
import com.bytter.biz.foreign.dao.FexTranCustInfoDao;
import com.bytter.biz.foreign.dao.FexTranReqDao;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexCustInfoTenant;
import com.bytter.biz.foreign.entity.FexLinkmanInfo;
import com.bytter.biz.foreign.entity.FexMessage;
import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.entity.FexTranCustInfo;
import com.bytter.biz.foreign.entity.FexTranDeal;
import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.entity.FexTranReqExt;
import com.bytter.biz.foreign.enums.FexTranTypeEnum;
import com.bytter.biz.foreign.utils.FexTranReqUtil;
import com.bytter.biz.foreign.vo.FexCorpBankAccDtlVO;
import com.bytter.biz.foreign.vo.FexCorpBankAccVO;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexCustFieldRuleVO;
import com.bytter.biz.foreign.vo.FexCustInfoTenantVO;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqExtVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigDetailVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigParamVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAttachmentService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.biz.payment.api.manage.LockManager;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyPairDTO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.*;
import com.bytter.system.api.ITaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@WorkFlow({
        FexWorkFlowConstant.FEX_TRAN_REQ_SPOT_FOREIGN,
        FexWorkFlowConstant.FEX_TRAN_REQ_FORWARD_FOREIGN,
        FexWorkFlowConstant.FEX_TRAN_REQ_SWAP_FOREIGN,
        FexWorkFlowConstant.FEX_TRAN_REQ_OPTION,
        FexWorkFlowConstant.FEX_TRAN_REQ_ALLOCATION,
        FexWorkFlowConstant.FEX_TRAN_REQ_PAY})
@Service("fexTranReqService")
@Transactional
public class FexTranReqService extends BaseService<FexTranReq, String> implements IWorkFlowListener, ITaskService, IWorkFlowRevokeListener, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private FexTranReqDao fexTranReqDao;
    @Autowired
    private FexMessageService fexMessageService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private FexLinkmanInfoService fexLinkmanInfoService;
    @Autowired(required=false)
    private IWorkFlowService workFlowServiceImpl;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexTranCustInfoDao fexTranCustInfoDao;
    @Autowired
    private FexCorpBankAccDtlService fexCorpBankAccDtlService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired
    private FexTranDealService fexTranDealService;
    @DubboReference(check = false)
    private IMultiLangService multiLangService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IAttachmentService attachmentService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @Autowired
    private FexTranReqExtService fexTranReqExtService;

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @DubboReference(check = false)
    private IBtCurrencyPairService currencyPairService;

    @Autowired(required = false)
    private BisForeignExcService bisForeignExcService;

    @Autowired(required = false)
    private FexProductService fexProductService;

    @Autowired
    private FexCotpService fexCotpService;

    @Autowired
    private FexCotpBankAccService fexCotpBankAccService;

    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @Autowired
    private FexCustFieldRuleService fexCustFieldRuleService;


    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;

    @DubboReference(check = false)
    private  IRegionService regionService;

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;

    @Value("${xxl.job.operator}")
    private String schedulerUser;

    @Autowired
    private FexTranInfoService fexTranInfoService;

    @Autowired
    private FexCustInfoTenantService fexCustInfoTenantService;

    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;

    private final LockManager lockManager = new LockManager();

    @Override
    public BaseDao<FexTranReq, String> getEntityDao() {
        return fexTranReqDao;
    }

    /**
     * 查询交易信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        String tranType = (String) dataTablesInput.getFormdata().get("tranType");
        StringBuilder sql = new StringBuilder();
        sql.append(" and t.tran_type='" + tranType+"'");
        String resource = "";
        String joinSql = "";
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if ("list".equals(opType)) {
            if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_FOREIGN_QUERY;
                sql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_CHECK +"," + ForeignConstant.STATUE_WAIT_CHECK + ")");
            } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_OPTION_QUERY;
                sql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_CHECK +","
                        + ForeignConstant.STATUE_INVALID +"," + ForeignConstant.STATUE_ENTERAVG +"," + ForeignConstant.STATUE_EXERCISE +","
                        + ForeignConstant.STATUE_KNOCKOUT +"," + ForeignConstant.STATUE_KNOCKIN +"," + ForeignConstant.ONGOING +"," + ForeignConstant.STATUE_WAIT_CHECK +")");
            } else if (ForeignConstant.TRAN_TYPE_3.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_ALLOCATION_QUERY;
                sql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK +"," + ForeignConstant.STATUE_WAIT_CHECK + ")");
            } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_PAY_QUERY;
                sql.append(" and t.status  in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK +"," + ForeignConstant.STATUE_WAIT_CHECK + ")");
            }
        }else{
            if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_FOREIGN_QUERY;
            } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_OPTION_QUERY;
            } else if (ForeignConstant.TRAN_TYPE_3.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_ALLOCATION_QUERY;
            } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
                resource = ResourceConstant.FOREIGN_FEXTRANREQ_QUERY_PAY_QUERY;
                //支付状态
                String payStatusSearch = (String) dataTablesInput.getFormdata().get("eqs_payStatusSearch");
                if(StringUtils.isNotEmpty(payStatusSearch)) {
                    if ("w".equals(payStatusSearch)) {
                        //待付款
                        dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.NO);
                        dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.YES);
                        sql.append(" and exc.voucher_Stat is null");
                    } else if ("p".equals(payStatusSearch)) {
                        //已付款
                        dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.YES);
                        dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.YES);
                        sql.append(" and exc.voucher_Stat is null");
                    } else if ("w1".equals(payStatusSearch)) {
                        //待收款
                        dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.NO);
                        dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.NO);
                        sql.append(" and exc.voucher_Stat is null");
                    } else if ("p1".equals(payStatusSearch)) {
                        //已收款
                        dataTablesInput.getFormdata().put("eqs_payStatus", CommonConstant.YES);
                        dataTablesInput.getFormdata().put("eqs_cdSign", CommonConstant.NO);
                        sql.append(" and exc.voucher_Stat is null");
                    } else {
                        dataTablesInput.getFormdata().put("eqs_voucherStat", payStatusSearch);
                    }
                }
            }
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
            joinSql += " and " + corpAuthSql;
        }
        sql.append(joinSql);
        appendExternalCorpNameFilters(dataTablesInput, sql);
        DataTablesOutput<FexTranReqVO> output;
        if(ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
            output = this.setDataTablesUnion(fexTranReqDao.getDataTablesUnion(dataTablesInput, sql.toString()));


        }else{
            output = fexTranReqDao.getDataTablesUnion(dataTablesInput, sql.toString());
            //如果是外汇期权交易，这里补充一些交易扩展信息
            if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
                // 设置货币对
                Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
                List<FexTranReqVO> fexTranReqVos = (List<FexTranReqVO>) output.getData();
                for (FexTranReqVO fexTranReqVo : fexTranReqVos) {
                    BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqVo.getTranCurPairId());
                    if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                        fexTranReqVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                    }
                }
            }
            //updateTemplateName( output);
        }
        injectOutputValue(output);
        //列表查询组装审批人信息
        if ("query".equals(opType)) {
            return this.setDataTablesApproveInfo(output);
        }
        return output;
    }

    private void updateTemplateName(DataTablesOutput<FexTranReqVO> output) {
        List<FexTranReqVO> data = output.getData();
        if (!EmptyUtils.isEmpty(data)) {
            List<String> templateIds = data.stream().map(FexTranReqVO::getTemplateId).filter(templateId -> !EmptyUtils.isEmpty(templateId)).distinct().collect(Collectors.toList());
            if (!EmptyUtils.isEmpty(templateIds)) {
                FexTranTemplateConfigParamVO param = new FexTranTemplateConfigParamVO();
                param.setIds(templateIds);
                List<FexTranTemplateConfigDetailVO> templates = fexTranConfigTemplateService.listOfChildren(param);
                if (!EmptyUtils.isEmpty(templates)) {
                    Map<String, FexTranTemplateConfigDetailVO> map = templates.stream().collect(Collectors.toMap(FexTranTemplateConfigDetailVO::getId, Function.identity()));
                    data.forEach(e -> {
                        FexTranTemplateConfigDetailVO detail = map.get(e.getTemplateId());
                        if (detail != null) {
                            e.setTemplateName(String.format(FexTranConfigTemplateService.childTemplateNameFormat, detail.getTemplateNameParent(), detail.getBankTypeName(), detail.getBifName(), detail.getCountryZhName(), detail.getCustomCode(), detail.getCustomName()));
                        }
                    });
                    output.setData(data);
                }
            }
        }
    }

    /**
     *
     * 买卖交易管理列表
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput getDataTablesUnionForeign(DataTablesInput dataTablesInput, ICurrentUser user) {
        String type = (String) dataTablesInput.getFormdata().get("type");
        String tranType = (String) dataTablesInput.getFormdata().get("tranType");
        if (StringUtils.isEmpty(tranType)) {
            tranType = "1";
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" and t.tran_type='" + tranType+"'");
        String resource = "";
        String joinSql = "";

        if (FexTranTypeEnum.SPOT.getCode().equals(type)) {
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_FOREIGN_SPOT_QUERY;
            sql.append(" and t.fex_tran_type='0' and t.is_swap_tran='0'");
            sql.append(" and t.status in (" + FexTranReqUtil.listToString(FexTranReqUtil.SPOT_STATUS) + ")");
        } else if (FexTranTypeEnum.FORWARD.getCode().equals(type)) {
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_FOREIGN_FORWARD_QUERY;
            sql.append(" and t.fex_tran_type='1' and t.is_swap_tran='0' ");
            sql.append(" and t.status in (" +  FexTranReqUtil.listToString(FexTranReqUtil.FORWARD_STATUS) + ")");
        } else if (FexTranTypeEnum.SWAP.getCode().equals(type)) {
            sql.append(" and t.is_swap_tran='1' ");
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_FOREIGN_SWAP_QUERY;
            sql.append(" and t.status in (" +  FexTranReqUtil.listToString(FexTranReqUtil.SWAP_STATUS) + ")");
        }
        if(tranType.equals(ForeignConstant.TRAN_TYPE_2)){
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_OPTION_QUERY;
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
            joinSql += " and " + corpAuthSql;
        }
        sql.append(joinSql);
        if (dataTablesInput.getFormdata().get("currencyPairId") != null && !StringUtils.isBlank(dataTablesInput.getFormdata().get("currencyPairId").toString())) {
            String currencyPairId = dataTablesInput.getFormdata().get("currencyPairId").toString();
            String[] cur = StringUtils.split(currencyPairId, "_");
            sql.append(" and t.tran_cur1 = '" + cur[0] + "'  and t.tran_cur2 = '" + cur[1] + "' ");
        }
        appendExternalCorpNameFilters(dataTablesInput, sql);
        DataTablesOutput output;
        if(ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
            output = this.setDataTablesUnion(fexTranReqDao.getDataTablesUnionForeign(dataTablesInput, sql.toString()));
        }else{
            output = fexTranReqDao.getDataTablesUnionForeign(dataTablesInput, sql.toString());
            //如果是外汇期权交易，这里补充一些交易扩展信息
            if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
                // 设置货币对
                Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
                List<FexTranReqVO> fexTranReqVos = (List<FexTranReqVO>) output.getData();
                for (FexTranReqVO fexTranReqVo : fexTranReqVos) {
                    BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqVo.getTranCurPairId());
                    if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                        fexTranReqVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                    }
                }
            }
        }
        // 注入币种、单位、字典信息和生命周期信息
        setData(output);
        return output;
    }



    /**
     * 针对外汇买卖交易做封装
     * @param datas
     * @return
     */
    public DataTablesOutput setDataTablesApproveInfo(DataTablesOutput datas) {
        Map<String, UserDTO> userMap = new HashMap<>();
        if (datas != null && datas.getData() != null) {
            List<FexTranReqVO> list = datas.getData();
            for (FexTranReqVO vo : list) {
                //针对审批通过的交易设置审批人
                UserDTO user = userMap.get(vo.getCreateBy());
                if (user == null) {
                    user = userService.getByLoginName(vo.getCreateBy());
                    userMap.put(vo.getCreateBy(), user);
                }
                if (user != null) {
                    vo.setCreateName(user.getUsername());
                }
                List<FexMessage> messages = fexMessageService.listMessageApprove(vo.getId(),  FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
                for (int i=0;i<messages.size();i++) {
                    if (StringUtils.isBlank(vo.getApproveUser())) {
                        vo.setApproveUser(messages.get(i).getOpName());
                    } else {
                        if (vo.getApproveUser().indexOf(messages.get(i).getOpName()) == -1) {
                            vo.setApproveUser(vo.getApproveUser() + CommonConstant.COMMA_SPLIT + messages.get(i).getOpName());
                        }
                    }
                    //取最后一笔审批通过的日志，获取审批时间
                    if (i == messages.size()-1) {
                        vo.setApproveTime(messages.get(i).getEnrolDate());
                    }
                }
            }

            if(CollectionUtil.isNotEmpty(list)){
                // 注入币种、单位、字典信息
                injectValue(list);
            }
        }
        return datas;
    }


    /**
     * 针对外汇买卖交易做封装
     * @param datas
     * @return
     */
    public DataTablesOutput<FexTranReqVO> setDataTablesUnion(DataTablesOutput<FexTranReqVO> datas) {
        if (datas != null && datas.getData() != null) {
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            List<FexTranReqVO> list = datas.getData();
            for (FexTranReqVO vo : list) {
                //针对审批通过的交易，判断交割情况
                if(CommonConstant.STATUE_CHECK.equals(vo.getStatus())){
                    List<FexTranDeal> dealList = fexTranDealService.listByTranReqId(vo.getId());
                    if(!EmptyUtils.isEmpty(dealList)){
                        /**操作类型 1-提前/展期交割，2-违约，3-NDF交割*/
                        vo.setOpType(dealList.get(0).getOpType());
                    }else{
                        vo.setOpType(CommonConstant.NO);
                    }
                }
                // 设置货币对
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(vo.getTranCurPairId());
                if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                    vo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
            }
        }
        return datas;
    }

    /**
     *
     * 注入币种、单位、字典信息和生命周期信息
     *
     * @param output
     */
    private void setData(DataTablesOutput output){
        if (EmptyUtils.isNotEmpty(output.getData())) {
            List<FexTranReqVO> fexTranReqVos = (List<FexTranReqVO>) output.getData();
            // 注入币种、单位、字典信息
            injectValue(fexTranReqVos);

            // 查询是否有暂存或审批中生命周期，如存在则不允许做生命周期操作
            List<String> reqIds = fexTranReqVos.stream().map(FexTranReqVO::getId).collect(Collectors.toList());
            Map<String, List<FexTranLifeCycleVo>> reqIdToLifeCyclesMap = fexTranLifeCycleService
                    .getTemporaryLifeCycleList(reqIds)
                    .stream()
                    .filter(vo -> vo.getReqId() != null)
                    .collect(Collectors.groupingBy(FexTranLifeCycleVo::getReqId));
            fexTranReqVos.forEach(reqVO -> {
                boolean hasTemporaryLifeCycle = Optional.ofNullable(reqIdToLifeCyclesMap.get(reqVO.getId()))
                        .filter(EmptyUtils::isNotEmpty)
                        .isPresent();
                // 设置标识（存在则不显示，否则显示）
                reqVO.setIsShowLifeCycle(hasTemporaryLifeCycle ? CommonConstant.NO : CommonConstant.YES);
            });
        }
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranReqVO getByIdUnion(String id){
        FexTranReqVO fexTranReq = fexTranReqDao.getByIdUnion(id);
        injectValue(fexTranReq);
        //获取支付方式
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setCorpId(fexTranReq.getCorpId());
        corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        corpSearchVO.setProId(fexTranReq.getProId());
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            //支付方式
            fexTranReq.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            fexTranReq.setVoucherTypeName(corpBankAcc.getVoucherName());
        }
        if(fexTranReq.getPremiumRate() != null){
            fexTranReq.setPremiumRateStr(fexTranReq.getPremiumRate().intValue()+"");
        }
        // 资金调拨获取收款人地址
        if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
            CorpDTO corp = corpService.get(fexTranReq.getTargetCorp());
            if (corp != null){
                fexTranReq.setTargetAddr(corp.getAddressEn());
            }
            BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getTargetBankAcc());
            if (bankAccDTO != null) {
                fexTranReq.setLocalClearNo(bankAccDTO.getLocalClearNo());
            }
        } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            BtBankAccDTO bankAcc = btBankAccService.getBankAccInfoByBankAcc(fexTranReq.getCorpBankAcc());
            if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getLocalClearNo())) {
                fexTranReq.setPayerLocalClearNo(bankAcc.getLocalClearNo());
            }
            // 反写交易对手银行地址/币种
            fillCotpBankAccInfo(fexTranReq);
            // 历史数据初始化交易对手信息
            if (StringUtils.isBlank(fexTranReq.getCotpCountry()) && StringUtils.isNotBlank(fexTranReq.getCotpId())) {
                fillCotpInfo(fexTranReq);
            }
        } else if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            //外汇买卖交易，设置收付款方账号的SWIFT CODE
            BtBankAccDTO corpBankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());
            fexTranReq.setCorpBankAccSwiftCode(corpBankAccDTO == null ? "":corpBankAccDTO.getSwiftCode());
            BtBankAccDTO srcBankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getSrcBankAcc());
            fexTranReq.setSrcBankAccSwiftCode(srcBankAccDTO == null ? "":srcBankAccDTO.getSwiftCode());


            // 扩展信息
            FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
            if(!EmptyUtils.isEmpty(fexTranReqExtVo)) {
                FexTranReqUtil.setFexTranReqExtData(fexTranReq, fexTranReqExtVo);

                // 设置货币对
                Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqExtVo.getTranCurPairId());
                if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                    fexTranReq.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
            }
        }else if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            // 扩展信息
            FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
            if(!EmptyUtils.isEmpty(fexTranReqExtVo)) {
                FexTranReqUtil.setFexTranReqExtData(fexTranReq, fexTranReqExtVo);
                // 设置货币对
                Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqExtVo.getTranCurPairId());
                if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                    fexTranReq.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                    fexTranReq.setTranCurPairBaseCode(btCurrencyPairDTO.getBaseEnglishCode());
                    fexTranReq.setTranCurPairBaseId(btCurrencyPairDTO.getBaseCurId());
                    //如果englishiCode1 == baseEnglishCode(基准i中)，则报价币种币种为englishCode2
                    if (btCurrencyPairDTO.getEnglishCode1().equals(btCurrencyPairDTO.getBaseEnglishCode())){
                        fexTranReq.setTranCurPairReferCode(btCurrencyPairDTO.getEnglishCode2());
                        fexTranReq.setTranCurPairReferId(btCurrencyPairDTO.getCurId2());
                    }else{
                        fexTranReq.setTranCurPairReferCode(btCurrencyPairDTO.getEnglishCode1());
                        fexTranReq.setTranCurPairReferId(btCurrencyPairDTO.getCurId1());
                    }
                }
            }
        }

        return fexTranReq;
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranReqVO getNewByIdUnion(String id){
        FexTranReqVO fexTranReq = fexTranReqDao.getByIdUnion(id);
        injectValue(fexTranReq);
        //获取支付方式
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setCorpId(fexTranReq.getCorpId());
        corpSearchVO.setOppCorpId(fexTranReq.getOppId());
        corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        corpSearchVO.setProId(fexTranReq.getProId());
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getNewBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            //支付方式
            fexTranReq.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            fexTranReq.setVoucherTypeName(corpBankAcc.getVoucherName());
        }
        fillOptionFieldsFromProductIfAbsent(fexTranReq);
        if(fexTranReq.getPremiumRate() != null){
            fexTranReq.setPremiumRateStr(fexTranReq.getPremiumRate().intValue()+"");
        }
        // 资金调拨获取收款人地址
        if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
            CorpDTO corp = corpService.get(fexTranReq.getTargetCorp());
            if (corp != null){
                fexTranReq.setTargetAddr(corp.getAddressEn());
            }
            BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getTargetBankAcc());
            if (bankAccDTO != null) {
                fexTranReq.setLocalClearNo(bankAccDTO.getLocalClearNo());
            }
        } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            BtBankAccDTO bankAcc = btBankAccService.getBankAccInfoByBankAcc(fexTranReq.getCorpBankAcc());
            if (bankAcc != null && StringUtils.isNotBlank(bankAcc.getLocalClearNo())) {
                fexTranReq.setPayerLocalClearNo(bankAcc.getLocalClearNo());
            }
            // 反写交易对手银行地址/币种
            fillCotpBankAccInfo(fexTranReq);

            // 历史数据初始化交易对手信息
            if (StringUtils.isBlank(fexTranReq.getCotpCountry()) && StringUtils.isNotBlank(fexTranReq.getCotpId())) {
                fillCotpInfo(fexTranReq);
            }
        } else if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            //外汇买卖交易，设置收付款方账号的SWIFT CODE
            BtBankAccDTO corpBankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getCorpBankAcc());
            fexTranReq.setCorpBankAccSwiftCode(corpBankAccDTO == null ? "":corpBankAccDTO.getSwiftCode());
            BtBankAccDTO srcBankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getSrcBankAcc());
            fexTranReq.setSrcBankAccSwiftCode(srcBankAccDTO == null ? "":srcBankAccDTO.getSwiftCode());
        }
        // 扩展信息
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
        if(!EmptyUtils.isEmpty(fexTranReqExtVo)) {
            FexTranReqUtil.setFexTranReqExtData(fexTranReq, fexTranReqExtVo);

            // 设置货币对
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            if (currencyPairMap != null) {
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqExtVo.getTranCurPairId());
                if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                    fexTranReq.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
            }
            fexTranReq.setProfitLossCur(fexTranReqExtVo.getProfitLossCur());
        }

        // 设置省市信息
        setRegion(fexTranReq);
        return fexTranReq;
    }

    /**
     * 根据billNo获取交易信息
     * @param billNo
     * @return
     */
    public FexTranReqVO getByBillNoUnion(String billNo){
        FexTranReqVO fexTranReq = fexTranReqDao.getByBillNoUnion(billNo);
        injectValue(fexTranReq);
        //获取支付方式
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setCorpId(fexTranReq.getCorpId());
        corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        corpSearchVO.setProId(fexTranReq.getProId());
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            //支付方式
            fexTranReq.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            fexTranReq.setVoucherTypeName(corpBankAcc.getVoucherName());
        }
        if(fexTranReq.getPremiumRate() != null){
            fexTranReq.setPremiumRateStr(fexTranReq.getPremiumRate().intValue()+"");
        }
        // 资金调拨获取收款人地址
        if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
            CorpDTO corp = corpService.get(fexTranReq.getTargetCorp());
            if (corp != null){
                fexTranReq.setTargetAddr(corp.getAddressEn());
            }
            BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(fexTranReq.getTargetBankAcc());
            if (bankAccDTO != null) {
                fexTranReq.setLocalClearNo(bankAccDTO.getLocalClearNo());
            }
        }
        return fexTranReq;
    }

    /**
     * 根据交易组获取交易信息
     * @param tranGroup
     * @return
     */
    public List<FexTranReqVO> listVOByTranGroup(String tranGroup){
        List<FexTranReqVO> list = fexTranReqDao.listVOByTranGroup(tranGroup);
        injectValue(list);
        return list;
    }

    /**
     * 保存交易组信息
     *
     * @param fexTranReq
     */
    public void saveFexTranReq(FexTranReqVO fexTranReq, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(fexTranReq)) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        FexTranReq entity = new FexTranReq();
        BeanUtils.copyNotNullProperties(fexTranReq, entity);
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        //近端交易
        if(ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                && ForeignConstant.SWAP_TYPE_1.equals(entity.getSwapType())){
            entity.setPartnerId(entity.getBillNo());
            fexTranReq.setBillNo(entity.getBillNo());
        }
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            entity.setCountryId(this.getCountryIdForForeign(fexTranReq));
        } else if(ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())){
            if(StringUtils.isEmpty(fexTranReq.getTemplateId())){
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TEMPLATE_NULL, fexTranReq.getBillNo());
            }
        }
        // 单据来源
        if (StringUtils.isBlank(entity.getBillFrom())) {
            entity.setBillFrom(ForeignConstant.BILL_FROM_0);
        }

        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_3)) {
            //TODO 生成一笔 交易对手记录信息
            String cotpId = saveCotpRecord(fexTranReq, user, false);
            fexTranReq.setCotpId(cotpId);
            //资金收付交易产生一笔 交易银行账户数据
            if (StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {
                String cotpBankAccId = saveCotpBankAcc(fexTranReq, user, cotpId, false);
                entity.setCotpAccId(cotpBankAccId);
                // 同步更新客商资料信息
                updateExternalCorpInfo(fexTranReq, user);
            }
            entity.setCotpId(cotpId);
        }
        this.saveEntity(entity);

        //新增企业自定义信息
        List<FexCustInfoTenant> fexTranCustInfos = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(fexTranReq.getFexTranCustInfos())) {
            for (FexCustInfoTenantVO fexTranCustInfoVO : fexTranReq.getFexTranCustInfos()) {
                if (StringUtils.isNotEmpty(fexTranCustInfoVO.getFieldId()) || StringUtils.isNotEmpty(fexTranCustInfoVO.getFieldName())) {
                    FexCustInfoTenant fexTranCustInfo = new FexCustInfoTenant();
                    BeanUtils.copyProperties(fexTranCustInfoVO, fexTranCustInfo);
                    fexTranCustInfo.setBusId(entity.getId());
                    fexTranCustInfos.add(fexTranCustInfo);
                }
            }
            fexCustInfoTenantService.batchSave(fexTranCustInfos);
        }
        // 保存收付交易自定义信息
        if(!CollectionUtil.isEmpty(fexTranReq.getFexCustInfos())){
            saveFexTranReqCust(fexTranReq.getFexCustInfos(), user, entity.getId());
        }

        // 添加扩展信息
        saveFexTranReqExt(fexTranReq, user, entity.getId());

        //保存主体单位联系人
        this.saveLinkmanNotExist(fexTranReq, user);

        //写入计划执行
        fexTranReq.setId(entity.getId());
        fexTranReq.setBillNo(entity.getBillNo());
        if(StringUtils.isNotBlank(fexTranReq.getBudgetInfo())){
            this.writeBudgetProcess(fexTranReq, fexTranReq.getBudgetInfo(), user);
        }else {
            this.writeBudgetProcess(fexTranReq, user);
        }

        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            // 资金收付付款交易在主单提交时即校验并占用支付限额，失败返回码转业务异常并回滚本次提交。
            fexCashFlowService.occupyEnterpriseLimitForTranSubmit(entity, user);
            messageType = FexMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_ADD_SUBMIT_SUCC;
            //调用审批后方法
            //inject(fexTranReq);
            getStartHandler().handler(this.getWorkFlowByTranType(entity.getTranType(), entity.getIsSwapTran(), entity.getFexTranType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        if(user !=null) {
            //生成日志
            fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        }
    }

    /**
     *
     * 保存扩展信息
     *
     * @param fexTranReq
     * @param user
     * @param repId
     */
    public void saveFexTranReqExt(FexTranReqVO fexTranReq, ICurrentUser user, String repId) {
        // 扩展信息
        FexTranReqExt fexTranReqExt = new FexTranReqExt();
        BeanUtils.copyNotNullProperties(fexTranReq, fexTranReqExt);
        fexTranReqExt.setReqId(repId);
        fexTranReqExtService.saveEntity(fexTranReqExt);
    }

    /**
     *
     * 保存自定义信息
     *
     * @param fexCustInfos
     * @param user
     * @param reqId
     */
    private void saveFexTranReqCust(Map<String, String> fexCustInfos, ICurrentUser user, String reqId) {
        List<FexTranCustInfo> fexTranCustInfos = new ArrayList<>();
        List<String> fexCustFieCodeList = new ArrayList<>(fexCustInfos.keySet());

        List<FexCustFieldRuleVO> fexCustFieldRuleVOS = fexCustFieldRuleService.getByFieCodes(fexCustFieCodeList);
        for (FexCustFieldRuleVO fexCustFieldRuleVO : fexCustFieldRuleVOS) {
            FexTranCustInfo fexTranCustInfo = new FexTranCustInfo();
            fexTranCustInfo.setFieldVal(fexCustInfos.get(fexCustFieldRuleVO.getFieldCode()));
            fexTranCustInfo.setBusId(reqId);
            fexTranCustInfo.setFieldId(fexCustFieldRuleVO.getId());
            fexTranCustInfos.add(fexTranCustInfo);
        }
        fexTranCustInfoDao.batchSave(fexTranCustInfos);
    }

    /**
     * 产生一笔交易对手账户数据
     *
     * @param fexTranReq
     * @param user
     * @param cotpId
     * @param editFlag
     * @return
     */
    public String saveCotpBankAcc(FexTranReqVO fexTranReq, ICurrentUser user, String cotpId, boolean editFlag) {
        return fexCotpBankAccService.saveCotpBankAcc(fexTranReq, user, cotpId, editFlag);
    }

    /**
     * 产生一笔交易对手信息
     *
     * @param fexTranReq
     * @return
     */
    public String saveCotpRecord(FexTranReqVO fexTranReq, ICurrentUser user, Boolean editFlag) {
        return fexCotpService.saveCotpRecord(fexTranReq, user, editFlag);
    }

    private void fillCotpBankAccInfo(FexTranReqVO fexTranReq) {
        FexCotpBankAccVO cotpBankAcc = getFexCotpBankAccById(fexTranReq.getCotpAccId());
        if (cotpBankAcc == null) {
            return;
        }
        // 反写交易对手银行地址
        fexTranReq.setCotpBankAdrr(cotpBankAcc.getBankAddr());
        // 资金收付交易，交易对手币种由单币种改造为多币种，考虑历史和在途数据，币种为空时才反写
        if (StringUtils.isNotBlank(cotpBankAcc.getCurId()) && StringUtils.isBlank(fexTranReq.getCotpAccCurId())) {
            String curId = cotpBankAcc.getCurId().split(CommonConstant.COMMA_SPLIT)[0];
            fexTranReq.setCotpAccCurId(curId);
            fexTranReq.setCotpAccEnglishCode(StringUtils.isNotBlank(cotpBankAcc.getCurName()) ? cotpBankAcc.getCurName().split(CommonConstant.COMMA_SPLIT)[0] : getCurrencyEnglishCode(curId));
        }
    }

    private FexCotpBankAccVO getFexCotpBankAccById(String id) {
        return StringUtils.isBlank(id) ? null : fexCotpBankAccService.getFexFexCotpBankAcc(id);
    }

    private void fillCotpInfo(FexTranReqVO fexTranReq) {
        FexCotpVO cotp = getFexCotpById(fexTranReq.getCotpId());
        if (cotp == null) {
            return;
        }
        fexTranReq.setCotpCountry(cotp.getCountryId());
        fexTranReq.setCotpAddress(cotp.getAddress());
        fexTranReq.setCotpProv(cotp.getProv());
        fexTranReq.setCotpCity(cotp.getCity());
        fexTranReq.setCotpCountryName(cotp.getCountryName());
        fexTranReq.setCotpAddressName(cotp.getAddressName());
        fexTranReq.setCotpAddressNameEn(cotp.getAddressNameEn());
        fexTranReq.setCotpProvName(cotp.getProvName());
        fexTranReq.setCotpCityName(cotp.getCityName());
    }

    /**
     * 期权交易回显时，单据没有交割类型、期权费折算汇率则从产品带入。
     */
    private void fillOptionFieldsFromProductIfAbsent(FexTranReqVO fexTranReq) {
        if (fexTranReq == null || !ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())
                || StringUtils.isEmpty(fexTranReq.getProId())) {
            return;
        }
        boolean needDeliveryType = StringUtils.isEmpty(fexTranReq.getDeliveryType());
        boolean needPremiumRate = fexTranReq.getPremiumRate() == null;
        if (!needDeliveryType && !needPremiumRate) {
            return;
        }
        if (fexProductService == null) {
            return;
        }
        FexProduct product = fexProductService.get(fexTranReq.getProId());
        if (product == null) {
            return;
        }
        if (needDeliveryType && StringUtils.isNotEmpty(product.getDeliveryType())) {
            fexTranReq.setDeliveryType(product.getDeliveryType());
        }
        if (needPremiumRate && StringUtils.isNotEmpty(product.getFexOptionRate())) {
            fexTranReq.setPremiumRate(new BigDecimal(product.getFexOptionRate()));
        }
    }

    private FexCotpVO getFexCotpById(String id) {
        return StringUtils.isBlank(id) ? null : fexCotpService.getFexcotpById(id);
    }

    private String getCountryName(String id) {
        SysCountryCustDTO country = getCountry(id);
        return country == null ? "" : country.getNameZh();
    }

    private String getCountryNameEn(String id) {
        SysCountryCustDTO country = getCountry(id);
        return country == null ? "" : country.getNameEn();
    }

    private SysCountryCustDTO getCountry(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return sysCountryCustService.getById(id);
    }

    private String getCurrencyEnglishCode(String curId) {
        if (StringUtils.isBlank(curId)) {
            return "";
        }
        BtCurrencyDTO currency = btCurrencyService.getObjectById(curId);
        if (currency == null) {
            return "";
        }
        return StringUtils.isNotBlank(currency.getEnglishCode()) ? currency.getEnglishCode() : currency.getCurName();
    }

    /**
     * 更新客商信息
     *
     * @param fexTranReq
     * @param user
     * @return
     */
    public void updateExternalCorpInfo(FexTranReqVO fexTranReq, ICurrentUser user) {
        fexCotpService.updateExternalCorpInfo(fexTranReq, user);
    }
    /**
     * 保存交易信息
     *
     * @param fexTranReq
     */
    public void saveFexTranReqForeign(FexTranReqVO fexTranReq, ICurrentUser user) {
        // 近端交易数据
        FexTranReqVO nearTran = FexTranReqUtil.converterNearTran(fexTranReq, user);
        nearTran.setCountryId(this.getCountryIdForForeign(nearTran));
        if(CommonConstant.YES.equals(fexTranReq.getIsSwapTran())){
            nearTran.setSwapType(ForeignConstant.SWAP_TYPE_1);
            this.saveFexTranReq(nearTran, user);
            // 远端交易数据
            FexTranReqVO remoteTran = FexTranReqUtil.converterRemoteTran(fexTranReq, user);
            remoteTran.setPartnerId(nearTran.getBillNo());
            remoteTran.setCountryId(this.getCountryIdForForeign(remoteTran));
            remoteTran.setRelBillNo(nearTran.getRelBillNo());
            remoteTran.setSwapType(ForeignConstant.SWAP_TYPE_2);
            this.saveFexTranReq(remoteTran, user);
        }else{
            this.saveFexTranReq(nearTran, user);
        }
    }

    /**
     * 设置外汇买卖收款方国别
     * @param fexTranReq
     */
    public String getCountryIdForForeign(FexTranReqVO fexTranReq) {
        String countryId = "";
        if (StringUtils.isNotBlank(fexTranReq.getSrcAccId())) {
            BtBankAccDTO bankAccDTO = btBankAccService.getBankAccById(fexTranReq.getSrcAccId());
            if (bankAccDTO != null) {
                if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank())) {
                    // 境外
                    countryId = bankAccDTO.getProv();
                } else {
                    // 境内
                    SysCountryCustDTO custDTO = sysCountryCustService.getByEnCode(BdlinkConstant.COUNTRY_CODE_CHINA);
                    if (custDTO != null) {
                        countryId = custDTO.getId();
                    }
                }
            }
        }
        return countryId;
    }

    /**
     * 资金计划挂接
     * @param fexTranReq
     * @param budgetInfo
     * @param user
     */
    private void writeBudgetProcess(FexTranReqVO fexTranReq, String budgetInfo, ICurrentUser user){
        if (!StringUtils.isBlank(budgetInfo)) {
            List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
            String[] itemCodes = budgetInfo.split(",");
            for (String itemCode : itemCodes) {
                String[] budget = itemCode.split(BudgetConstant.BUDGET_PROCESS_SPLIT);
                FbsBudgetProcessDTO dto = this.bulidFbsBudgetProcessDTO(fexTranReq, user);
                dto.setItemCode(budget[0]);
                dto.setItemName(budget[1]);
                dto.setProcessMoney(new BigDecimal(budget[2]));
                if(!EmptyUtils.isEmpty(budget[3])) {
                    dto.setExchangeRate(new BigDecimal(budget[3]));
                }
                if(!EmptyUtils.isEmpty(budget[4])) {
                    dto.setProcessCurMoney(new BigDecimal(budget[4]));
                }
                //资金计划多组织改造-设置计划执行单位（默认业务单据所属单位）
                if (budget.length > 6) {
                    dto.setBudgetCorpId(budget[6]);
                } else {
                    dto.setBudgetCorpId(fexTranReq.getCorpId());
                }
                //执行明细信息
                if (budget.length > 5) {
                    dto.setProcessDetailInfo(budget[5]);
                }
                listBudget.add(dto);
            }
            if (!EmptyUtils.isEmptyList(listBudget)) {
                fbsBudgetProcessService.writeBudgetProcess(listBudget, user);
            }
        }
    }
    /**
     * 根据交易类型返回不同的跳转页面
     *
     * @param tranType
     * @return
     */
    public String getWorkFlowByTranType(String tranType, String isSwapTran, String fexTranType) {
        /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
        String workFlow = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(tranType) && CommonConstant.YES.equals(isSwapTran)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_SWAP_FOREIGN;
        } else if(ForeignConstant.TRAN_TYPE_1.equals(tranType) && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_SPOT_FOREIGN;
        }else if(ForeignConstant.TRAN_TYPE_1.equals(tranType) && ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_FORWARD_FOREIGN;
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_OPTION;
        } else if (ForeignConstant.TRAN_TYPE_3.equals(tranType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_ALLOCATION;
        } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
            workFlow = FexWorkFlowConstant.FEX_TRAN_REQ_PAY;
        }
        return workFlow;
    }

    /**
     * 根据交易类型返回不同的跳转页面
     *
     * @param tranType
     * @return
     */
    public String getTagByTranType(String tranType) {
        /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
        String tag = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(tranType)) {
            tag = NotificationConstant.FOREIGN.TRANREQ_FOREIGN_APPLY;
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            tag = NotificationConstant.FOREIGN.TRANREQ_OPTION_APPLY;
        } else if (ForeignConstant.TRAN_TYPE_3.equals(tranType)) {
            tag = NotificationConstant.FOREIGN.TRANREQ_ALLOCATION_APPLY;
        } else if (ForeignConstant.TRAN_TYPE_4.equals(tranType)) {
            tag = NotificationConstant.FOREIGN.TRANREQ_PAY_APPLY;
        }
        return tag;
    }

    /**
     * 汇款联系人信息保存
     *
     * @param fexTranReq
     */
    public void saveLinkmanNotExist(FexTranReqVO fexTranReq, ICurrentUser user) {
        FexLinkmanInfo linkmanInfo = new FexLinkmanInfo();
        linkmanInfo.setCorpId(fexTranReq.getCorpId());
        if(StringUtils.isNotBlank(fexTranReq.getCorpContact())) {
            linkmanInfo.setPayerName(fexTranReq.getCorpContact());
            linkmanInfo.setPayerTel(fexTranReq.getCorpContactTel());
            this.fexLinkmanInfoService.saveLinkmanNotExist(linkmanInfo, user);
        }
    }

    /**
     * 写入计划执行
     *
     * @param fexTranReq
     */
    private void writeBudgetProcess(FexTranReqVO fexTranReq, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(fexTranReq.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranReq.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < fexTranReq.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(fexTranReq, user);
                fbsBudgetProcessDTO.setItemCode(fexTranReq.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(fexTranReq.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(fexTranReq.getProcessMoney()[i]);
                if(fexTranReq.getExchangeRate()!=null) {
                    fbsBudgetProcessDTO.setExchangeRate(fexTranReq.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(fexTranReq.getProcessCurMoney()[i]);
                }
                //资金计划多组织改造-设置计划执行单位（默认业务单据所属单位）
                if (fexTranReq.getBudgetCorpId() != null) {
                    fbsBudgetProcessDTO.setBudgetCorpId(fexTranReq.getBudgetCorpId()[i]);
                } else {
                    fbsBudgetProcessDTO.setBudgetCorpId(fexTranReq.getCorpId());
                }
                //执行明细信息
                if (fexTranReq.getProcessDetailInfo() != null) {
                    fbsBudgetProcessDTO.setProcessDetailInfo(fexTranReq.getProcessDetailInfo()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //计划执行状态同步更新（批量提交）
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX,
                    fexTranReq.getBillNo(), fexTranReq.getStatus(), user);
        }
    }
    /**
     * 构建计划执行
     *
     * @param fexTranReq
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(FexTranReqVO fexTranReq, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setStatus(fexTranReq.getStatus());
        fbsBudgetProcessDTO.setBillNo(fexTranReq.getBillNo());
        fbsBudgetProcessDTO.setBillId(fexTranReq.getId());
        fbsBudgetProcessDTO.setProcessDate(fexTranReq.getSettleDate());
        fbsBudgetProcessDTO.setCorpId(fexTranReq.getCorpId());
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())){
            //外汇买卖交易-卖出币种
            fbsBudgetProcessDTO.setCurId(fexTranReq.getSellCurId());
        }else{
            fbsBudgetProcessDTO.setCurId(fexTranReq.getCurId());
        }
        fbsBudgetProcessDTO.setSettlType("1");
        fbsBudgetProcessDTO.setCashItem(fexTranReq.getCashItem());
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())
                || ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){
            fbsBudgetProcessDTO.setPayerAcc(fexTranReq.getSrcBankAcc());
            fbsBudgetProcessDTO.setPayeeAcc(fexTranReq.getTargetBankAcc());
            fbsBudgetProcessDTO.setPayeeAccName(fexTranReq.getTargetAccName());
            fbsBudgetProcessDTO.setPayeeBank(fexTranReq.getTargetBankName());
        }else if(ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())){
            fbsBudgetProcessDTO.setPayerAcc(fexTranReq.getSrcBankAcc());
            fbsBudgetProcessDTO.setPayeeAcc(fexTranReq.getTargetBankAcc());
            fbsBudgetProcessDTO.setPayeeAccName(fexTranReq.getTargetAccName());
            fbsBudgetProcessDTO.setPayeeBank(fexTranReq.getTargetBankName());
        }else if(ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())){
            fbsBudgetProcessDTO.setPayerAcc(fexTranReq.getCorpBankAcc());
            fbsBudgetProcessDTO.setPayeeAcc(fexTranReq.getCotpBankAcc());
            fbsBudgetProcessDTO.setPayeeAccName(fexTranReq.getCotpAccName());
            fbsBudgetProcessDTO.setPayeeBank(fexTranReq.getCotpBank());
        }
        fbsBudgetProcessDTO.setPurpose(fexTranReq.getRmk());
        fbsBudgetProcessDTO.setWishPayday(fexTranReq.getSettleDate());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_TRAN);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_FEX);
        return fbsBudgetProcessDTO;
    }

    /**
     * 修改交易组信息
     *
     * @param fexTranReq
     */
    public void updateFexTranReq(FexTranReqVO fexTranReq, ICurrentUser user) {
        FexTranReq entity = this.get(fexTranReq.getId(), true);
        //暂存和编辑状态才可以提交
        if (entity != null && !ForeignConstant.STATUE_TEMP.equals(entity.getStatus()) && !ForeignConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        // 必填项校验
        if(ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType()) && StringUtils.isEmpty(fexTranReq.getTemplateId())){
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TEMPLATE_NULL, fexTranReq.getBillNo());
        }
        /*BeanUtils.copyNotNullProperties(fexTranReq, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            entity.setCountryId(this.getCountryIdForForeign(fexTranReq));
        }
        this.saveOrUpdate(entity);*/
        fexTranReq.setUpdateBy(user.getLoginName());
        fexTranReq.setUpdateDate(DateUtils.getDate());
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            fexTranReq.setCountryId(this.getCountryIdForForeign(fexTranReq));
        }
        // 页面禁用买卖方向时可能未提交该字段，避免把库里的值覆盖成空
        if (StringUtils.isBlank(fexTranReq.getBusDirection()) && StringUtils.isNotBlank(entity.getBusDirection())) {
            fexTranReq.setBusDirection(entity.getBusDirection());
        }
        // 编辑拆分明细/合并明细单时，页面未提交拆分、合并关系字段，避免把库里的值覆盖成空
        if (StringUtils.isBlank(fexTranReq.getSplitId()) && StringUtils.isNotBlank(entity.getSplitId())) {
            fexTranReq.setSplitId(entity.getSplitId());
        }
        if (StringUtils.isBlank(fexTranReq.getMergeId()) && StringUtils.isNotBlank(entity.getMergeId())) {
            fexTranReq.setMergeId(entity.getMergeId());
        }
        if (StringUtils.isBlank(fexTranReq.getOrigBillNo()) && StringUtils.isNotBlank(entity.getOrigBillNo())) {
            fexTranReq.setOrigBillNo(entity.getOrigBillNo());
        }
        //编辑付款单，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};

        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_3)) {
            //TODO 生成一笔 交易对手记录信息
            String cotpId = saveCotpRecord(fexTranReq, user, true);
            //先回写客商ID，保证下面 updateExternalCorpInfo 同步的是本次判定出的客商
            //（与新增链路 saveFexTranReq 的顺序保持一致）
            fexTranReq.setCotpId(cotpId);
            //资金收付交易产生一笔 交易银行账户数据
            if (StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {
                String cotpBankAccId = saveCotpBankAcc(fexTranReq, user, cotpId, true);
                fexTranReq.setCotpAccId(cotpBankAccId);
                // 同步更新客商资料信息
                updateExternalCorpInfo(fexTranReq, user);
            }
            fexTranReq.setCotpId(cotpId);
        }
        this.updateFexTranReqStatus(fexTranReq, oldStatus);
        //新增自定义信息
        fexCustInfoTenantService.deleteCustFieldInfoByBusId(fexTranReq.getId());
        List<FexCustInfoTenant> fexTranCustInfos = new ArrayList<>();
        if (!EmptyUtils.isEmptyList(fexTranReq.getFexTranCustInfos())) {
            for (FexCustInfoTenantVO fexTranCustInfoVO : fexTranReq.getFexTranCustInfos()) {
                if (StringUtils.isNotEmpty(fexTranCustInfoVO.getFieldId()) || StringUtils.isNotEmpty(fexTranCustInfoVO.getFieldName())) {
                    FexCustInfoTenant fexTranCustInfo = new FexCustInfoTenant();
                    BeanUtils.copyProperties(fexTranCustInfoVO, fexTranCustInfo);
                    fexTranCustInfo.setBusId(entity.getId());
                    fexTranCustInfos.add(fexTranCustInfo);
                }
            }
            fexCustInfoTenantService.batchSave(fexTranCustInfos);
        }


        // 保存收付交易自定义信息
        if(!CollectionUtil.isEmpty(fexTranReq.getFexCustInfos())){
            saveFexTranReqCust(fexTranReq.getFexCustInfos(), user, entity.getId());
        }

        //更新扩展信息
        FexTranReqExtVo byReqId = fexTranReqExtService.getByReqId(fexTranReq.getId());
        if (null != byReqId){
            FexTranReqExt fexTranReqExt = fexTranReqExtService.get(byReqId.getId());
            //记录原有id，防止被覆盖
            String extId = fexTranReqExt.getId();
            BeanUtils.copyNotNullProperties(fexTranReq, fexTranReqExt);
            fexTranReqExt.setId(extId);
            fexTranReqExtService.updateEntity(fexTranReqExt);
        }

        //保存主体单位联系人
        this.saveLinkmanNotExist(fexTranReq, user);

        //写入计划执行
        this.writeBudgetProcess(fexTranReq, user);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexTranReq.getStatus())) {
            // 修改并提交同样在启动审批前同步支付限额，防止只覆盖批量提交入口。
            occupyEnterpriseLimitForTranSubmit(entity, fexTranReq, user);
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_UPDATE_SUBMIT_SUCC;
            //调用审批后方法

            getStartHandler().handler(this.getWorkFlowByTranType(entity.getTranType(), entity.getIsSwapTran(), entity.getFexTranType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        //完成通知提醒
        notificationService.complete(this.getTagByTranType(entity.getTranType()), entity.getId(), user.getId());
    }

    /**
     * 修改交易组信息
     *
     * @param fexTranReq
     */
    public void updateFexTranReqSubmit(FexTranReqVO fexTranReq, ICurrentUser user) {
        FexTranReq entity = this.get(fexTranReq.getId(), true);
        //暂存和编辑状态才可以提交
        if (entity != null && !ForeignConstant.STATUE_TEMP.equals(entity.getStatus()) && !ForeignConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        // 必填项校验
        checkRequired(fexTranReq);
        /*BeanUtils.copyNotNullProperties(fexTranReq, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.updateEntity(entity);*/
        fexTranReq.setUpdateBy(user.getLoginName());
        fexTranReq.setUpdateDate(DateUtils.getDate());
        //编辑付款单，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};

        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_3)) {
            //TODO 生成一笔 交易对手记录信息
            String cotpId = saveCotpRecord(fexTranReq, user, true);
            //先回写客商ID，保证下面 updateExternalCorpInfo 同步的是本次判定出的客商
            //（与新增链路 saveFexTranReq 的顺序保持一致）
            fexTranReq.setCotpId(cotpId);
            //资金收付交易产生一笔 交易银行账户数据
            if (StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {
                String cotpBankAccId = saveCotpBankAcc(fexTranReq, user, cotpId, true);
                fexTranReq.setCotpAccId(cotpBankAccId);
                // 同步更新客商资料信息
                updateExternalCorpInfo(fexTranReq, user);
            }
            fexTranReq.setCotpId(cotpId);
        }

        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexTranReq.getStatus())) {
            // 批量提交在状态落库和启动审批前先完成支付限额校验，占用失败时整笔事务回滚。
            occupyEnterpriseLimitForTranSubmit(entity, fexTranReq, user);
        }
        this.updateFexTranReqStatus(fexTranReq, oldStatus);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_TEMP;
        String message = FexMessageConstant.FEX_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (ForeignConstant.STATUE_WAIT_CHECK.equals(fexTranReq.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = FexMessageConstant.FEX_LOG_UPDATE_SUBMIT_SUCC;
            //调用审批后方法
            getStartHandler().handler(this.getWorkFlowByTranType(entity.getTranType(), entity.getIsSwapTran(), entity.getFexTranType()), entity.getId(), entity, entity.getCorpId(), user.getId());
        }
        //生成日志
        fexMessageService.saveMessage(entity.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        //完成通知提醒
        notificationService.complete(this.getTagByTranType(entity.getTranType()), entity.getId(), user.getId());
    }

    /**
     * 使用页面提交值覆盖数据库旧快照后校验资金收付支付限额。
     * 页面 VO 包含本次修改的金额、账户、币种及收付方向，不能直接使用修改前的 entity。
     */
    private void occupyEnterpriseLimitForTranSubmit(FexTranReq entity, FexTranReqVO request, ICurrentUser user) {
        FexTranReq snapshot = new FexTranReq();
        BeanUtils.copyNotNullProperties(entity, snapshot);
        BeanUtils.copyNotNullProperties(request, snapshot);
        // ID、单号属于稳定来源标识，页面未传或被清空时仍以数据库记录为准。
        snapshot.setId(entity.getId());
        snapshot.setBillNo(entity.getBillNo());
        snapshot.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        fexCashFlowService.occupyEnterpriseLimitForTranSubmit(snapshot, user);
    }

    /**
     * 结算回退
     *
     * @param id
     */
    public void backFexTranReq(String id, ICurrentUser user) {
        FexTranReq fexTranReq = this.get(id, true);
        FexTranReqVO fexTranReqVO = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranReq, fexTranReqVO);
        fexTranReqVO.setStatus(CommonConstant.STATUE_BACK);
        fexTranReqVO.setUpdateBy(user.getLoginName());
        fexTranReqVO.setUpdateDate(DateUtils.getDate());
        //审批付款单，更新原状态控制为待审批
        Integer[] oldStatus = {NetbankConstant.STATUE_CHECK};
        this.updateFexTranReqStatus(fexTranReqVO, oldStatus);
        if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            // 撤回后主交易不再占用支付限额，重新提交时重新校验。
            fexCashFlowService.releaseEnterpriseLimitForTranReq(fexTranReq.getId(), user);
        }
        //删除交割信息
        List<FexTranDeal> list = fexTranDealService.listByTranReqId(fexTranReq.getId());
        for(FexTranDeal fexTranDeal:list) {
            fexTranDealService.deleteEntity(fexTranDeal);
        }
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX,
                fexTranReq.getBillNo(), CommonConstant.STATUE_BACK, user);
    }

    /**
     * 删除交易组信息
     *
     * @param id
     */
    public void delFexTranReq(String id, String deleteMsg, ICurrentUser user) {
        FexTranReq entity = this.get(id, true);
        entity.setStatus(ForeignConstant.STATUE_DEL);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {
            // 删除资金收付主交易时清理提交阶段或后续现金流共用的限额占用。
            fexCashFlowService.releaseEnterpriseLimitForTranReq(entity.getId(), user);
        }
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
        //生成日志
        fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_DELETE, StringUtils.isNotBlank(deleteMsg)?deleteMsg:FexMessageConstant.FEX_LOG_DELETE_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        //完成通知提醒
        notificationService.complete(this.getTagByTranType(entity.getTranType()), entity.getId(), user.getId());
    }

    /**
     * 批量删除交易单
     *
     * @param ids
     */
    public void batchDelFexTranReq(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delFexTranReq(id, null, user);
        }
    }

    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexTranReq fexTranReq = this.get(workFlowResult.getBusinessKey(), true);
        FexTranReqVO fexTranReqVO = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(fexTranReq, fexTranReqVO);
        //更新审批状态
        fexTranReqVO.setStatus(CommonConstant.STATUE_TEMP);
        fexTranReqVO.setUpdateBy(user.getLoginName());
        fexTranReqVO.setUpdateDate(DateUtils.getDate());
        //审批付款单，更新原状态控制为待审批
        Integer[] oldStatus = {NetbankConstant.STATUE_WAIT_CHECK};
        this.updateFexTranReqStatus(fexTranReqVO, oldStatus);
        if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            // 审批撤回恢复暂存后释放主交易额度占用。
            fexCashFlowService.releaseEnterpriseLimitForTranReq(fexTranReq.getId(), user);
        }
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranReq.getBillNo(), CommonConstant.STATUE_TEMP, user);
        //生成日志
        fexMessageService.saveMessage(fexTranReq.getId(), FexMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, FexMessageConstant.FEX_LOG_WITHDRAW_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        this.getSession().flush();
        this.getSession().clear();
        FexTranReq entity = this.get(workFlowResult.getBusinessKey(), true);
        FexTranReqVO fexTranReqVO = this.getByIdUnion(entity.getId());
        if(!NetbankConstant.STATUE_WAIT_CHECK.equals(fexTranReqVO.getStatus())){
            throw new BusinessException(NetbankMessageEnum.BILL_CHECKED);
        }
        //查询扩展信息
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(entity.getId());
        if (!EmptyUtils.isEmpty(fexTranReqExtVo)) {
            BeanUtils.copyNotNullProperties(fexTranReqExtVo, fexTranReqVO);
        }
        //这里放后面是防止id被覆盖掉，影响后面更新
        BeanUtils.copyNotNullProperties(entity, fexTranReqVO);
        Integer messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        if (workFlowResult.isPass()) {
            messageType = FexMessageConstant.MESSAGE_TYPE_CHECK_PASS;
            fexTranReqVO.setStatus(CommonConstant.STATUE_CHECK);//审批通过
        } else {
            fexTranReqVO.setStatus(CommonConstant.STATUE_BACK);//审批打回
        }
        fexTranReqVO.setUpdateBy(user.getLoginName());
        fexTranReqVO.setUpdateDate(DateUtils.getDate());
        //审批付款单，更新原状态控制为待审批
        Integer[] oldStatus = {NetbankConstant.STATUE_WAIT_CHECK};
        // 获取业务数据ID对应的锁
        Lock lock = lockManager.getLock(fexTranReqVO.getId());
        //尝试获取锁
        boolean isLockAcquired = lock.tryLock();
        Boolean isSendExc = Boolean.FALSE;
        //如果获取锁成功
        if (isLockAcquired) {
            try {
                //审批通过，生成现金流
                if (CommonConstant.STATUE_CHECK.equals(fexTranReqVO.getStatus())) {
                    //计划执行状态同步更新
                    this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX,
                            entity.getBillNo(), CommonConstant.STATUE_CHECK, user);


                    if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                        if (!CommonConstant.YES.equals(entity.getIsSwapTran())) {
                            //固定金额：1-买入金额（收款流水） 2-卖出金额（支出流水）
                            if (StringUtils.isNotBlank(entity.getFixedMoney())) {
                                if (entity.getFixedMoney().indexOf(ForeignConstant.FIXED_MONEY_2) > -1) {
                                    isSendExc = this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.YES, user, true);
                                    if (ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqVO.getTranRateType())) { // 合同汇率生成两条现金流，挂牌汇率一条
                                        this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.NO, user, false);
                                    }
                                }
                                if (entity.getFixedMoney().indexOf(ForeignConstant.FIXED_MONEY_1) > -1) {
                                    isSendExc = this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.NO, user, true);
                                    if (ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqVO.getTranRateType())) {
                                        this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.YES, user, false);
                                    }
                                }
                            }
                        } else {
                            this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.YES, user, false);
                            this.saveExpectFexCashFLow(fexTranReqVO, CommonConstant.NO, user, false);
                        }

                        // 扩展信息
                        getDefProfitLossCur(fexTranReqVO);
                        // 保存损益
                        fexProfitLossManageService.saveManageProfitLoss(fexTranReqVO, user.getLoginName());
                    } else if (ForeignConstant.TRAN_TYPE_2.equals(entity.getTranType())) {
                        if (ForeignConstant.DELIVERY_SIGN_1.equals(entity.getBusDirection())) {
                            this.saveFexCashFLow(fexTranReqVO, CommonConstant.YES, user);
                        } else {
                            this.saveFexCashFLow(fexTranReqVO, CommonConstant.NO, user);
                        }
                        // 扩展信息
                        getDefProfitLossCur(fexTranReqVO);

                        FexProduct fexProduct = fexProductService.get(fexTranReqVO.getProId());
                        fexTranReqVO.setFexOption1(fexProduct.getFexOption1());
                        fexTranReqVO.setFexOption2(fexProduct.getFexOption2());
                        fexTranReqVO.setFexOption3(fexProduct.getFexOption3());
                        // 保存损益
                        fexProfitLossManageService.saveManageProfitLoss(fexTranReqVO, user.getLoginName());
                    } else if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                        //资金调拨交易-生成收/支两笔现金流
                        //付款账号币种与收款账号币种一致才生产付款现金流
                        if (entity.getCurId().equals(entity.getTargetAccCurId())) {
                            this.saveFexCashFLow(fexTranReqVO, CommonConstant.YES, user);
                        }
                        this.saveFexCashFLow(fexTranReqVO, CommonConstant.NO, user);
                    } else if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {
                        //资金收付交易-生成一笔现金流
                        this.saveFexCashFLow(fexTranReqVO, null, user);
                    }
                } else {
                    //计划执行状态同步更新
                    this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX,
                            entity.getBillNo(), CommonConstant.STATUE_BACK, user);
                }
                if (workFlowResult.isPass()) {
                    fexTranReqVO.setStatus(CommonConstant.STATUE_CHECK);//审批通过
                    if (fexTranReqVO.getTranType().equals(ForeignConstant.TRAN_TYPE_1)){
                        if (CommonConstant.YES.equals(fexTranReqVO.getIsPayed()) || !isSendExc) { // 线下交易变为续存状态或没有发指令
                            if (ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqExtVo.getTranRateType())) {
                                fexTranReqVO.setStatus(ForeignConstant.ONGOING);
                            } else {
                                fexTranReqVO.setStatus(ForeignConstant.STATUE_TOBE_RATE_CONFIRM);
                            }

                        }
                    } else if (fexTranReqVO.getTranType().equals(ForeignConstant.TRAN_TYPE_2)){ //如果是期权交易
                        //持仓金额不为0
                        if(!EmptyUtils.isEmpty(fexTranReqVO.getPositionMoney()) && fexTranReqVO.getPositionMoney().compareTo(BigDecimal.ZERO) != 0){
                            fexTranReqVO.setStatus(ForeignConstant.ONGOING);
                        }else{//否则就变为结束
                            fexTranReqVO.setStatus(ForeignConstant.STATUE_END);
                        }
                    }
                }
                this.updateFexTranReqStatus(fexTranReqVO, oldStatus);
                if (!workFlowResult.isPass() && ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())) {
                    // 状态成功打回后释放主交易提交阶段的支付限额占用。
                    fexCashFlowService.releaseEnterpriseLimitForTranReq(entity.getId(), user);
                }
                //生成日志
                fexMessageService.saveMessage(entity.getId(), messageType, "", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
                this.getSession().flush();

                // 即期自动交割 && 线下自动交割（线上的需要交易成功）
                if (workFlowResult.isPass()
                        &&  ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                        && ForeignConstant.FEX_TRAN_TYPE_0.equals(entity.getFexTranType())
                        && ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqExtVo.getTranRateType()) &&
                        !isSendExc){
                    fexTranLifeCycleService.autoSpotDelivery(fexTranReqVO, user, null);
                }

            } finally {
                //解锁
                lock.unlock();
            }
        }else{
            //获取锁失败
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_OPTION_DOING, fexTranReqVO.getBillNo());
        }
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        String tranType = (String) dataTablesInput.getFormdata().get("eqi_tranType");
        String sign = "";
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqi_tranType"))) {
            String isSwpTran = String.valueOf(dataTablesInput.getFormdata().get("isSwpTran"));
            String fexTranType = String.valueOf(dataTablesInput.getFormdata().get("fexTranType"));
            if (EmptyUtils.isNumeric(tranType)) {
                sign = this.getWorkFlowByTranType(tranType, isSwpTran, fexTranType);
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
        StringBuilder businessSql = new StringBuilder(SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true));
        appendExternalCorpNameFilters(dataTablesInput, businessSql);
        DataTablesOutput<FexTranReqVO> dp = this.fexTranReqDao.dataTableApprove(dataTablesInput, businessSql.toString());
        //如果是外汇期权交易，这里补充一些交易扩展信息
        if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            // 设置货币对
            Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();
            List<FexTranReqVO> fexTranReqVos = (List<FexTranReqVO>) dp.getData();
            for (FexTranReqVO fexTranReqVo : fexTranReqVos) {
                BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqVo.getTranCurPairId());
                if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
                    fexTranReqVo.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
                }
            }
        }
        injectOutputValue(dp);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 现金流生成
     * @param fexTranReq
     */
    public void saveFexCashFLow(FexTranReqVO fexTranReq, String cdSign, ICurrentUser user) {
        FexCashFlow entity = new FexCashFlow();
        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_CASH_FLOW));
        entity.setTranBillId(fexTranReq.getId());
        entity.setCashFlowDate(DateUtils.getDate());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        //现金流类型：0本金，1利息,2-违约金，3-期权费，4-退款
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_3);
        }else{
            entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        }
        //现金流项目
        entity.setCashItem(fexTranReq.getCashItem());
        //entity.setSettleDate(fexTranReq.getSettleDate());
        if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            entity.setSettleDate(fexTranReq.getPremiumDate());
        } else {
            entity.setSettleDate(fexTranReq.getSettleDate());
        }
        entity.setTranType(fexTranReq.getTranType());
        //跨行标志
        entity.setDifBank(fexTranReq.getDifBank());
        //远期交易备注属于交易信息，不写入现金流备注
        if (!ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())
                || !ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReq.getFexTranType())) {
            entity.setRmk(fexTranReq.getRmk());
        }
        //单据来源
        entity.setBillFrom(ForeignConstant.BILL_FROM_3);
        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();
        //获取交易对手账户
        FexCorpBankAccVO cotpSearchVO = new FexCorpBankAccVO();
        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())){
            entity.setCorpId(fexTranReq.getCorpId());
            entity.setCotpId(fexTranReq.getCotpId());
            if(CommonConstant.YES.equals(cdSign)){
                entity.setCurId(fexTranReq.getSellCurId());
                entity.setPayMoney(fexTranReq.getSellMoney());

                entity.setCorpAccId(fexTranReq.getSrcAccId());
                entity.setCorpBank(fexTranReq.getSrcBank());
                entity.setCorpAccName(fexTranReq.getSrcAccName());
                entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());

                entity.setCotpBank(fexTranReq.getTargetBank());
                entity.setCotpAccId(fexTranReq.getTargetAccId());
                entity.setCotpAccName(fexTranReq.getTargetAccName());
                entity.setCotpBankAcc(fexTranReq.getTargetBankAcc());
            }else{
                entity.setCurId(fexTranReq.getBuyCurId());
                entity.setPayMoney(fexTranReq.getBuyMoney());

                entity.setCorpAccId(fexTranReq.getCorpAccId());
                entity.setCorpBank(fexTranReq.getCorpBank());
                entity.setCorpAccName(fexTranReq.getCorpAccName());
                entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());

                entity.setCotpBank(fexTranReq.getCotpBank());
                entity.setCotpAccId(fexTranReq.getCotpAccId());
                entity.setCotpAccName(fexTranReq.getCotpAccName());
                entity.setCotpBankAcc(fexTranReq.getCotpBankAcc());
            }
            entity.setCdSign(cdSign);
            //外汇买卖交易-现金流付款账号对应的卖出币种
            entity.setPayerCurId(fexTranReq.getSellCurId());

            corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
            corpSearchVO.setTranType(fexTranReq.getTranType());
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            corpSearchVO.setProId(fexTranReq.getProId());
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                //支付方式
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }
        }else if(ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())){
            entity.setCorpId(fexTranReq.getCorpId());
            entity.setCotpId(fexTranReq.getCotpId());
            entity.setCurId(fexTranReq.getCurId());
            //外汇期权交易-现金流付款账号对应的结算币种
            entity.setPayerCurId(fexTranReq.getCurId());
            entity.setPayMoney(fexTranReq.getPremiumMoney());
            entity.setCorpAccId(fexTranReq.getCorpAccId());
            entity.setCorpBank(fexTranReq.getCorpBank());
            entity.setCorpAccName(fexTranReq.getCorpAccName());
            entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());

            entity.setCotpBank(fexTranReq.getCotpBank());
            entity.setCotpAccId(fexTranReq.getCotpAccId());
            entity.setCotpAccName(fexTranReq.getCotpAccName());
            entity.setCotpBankAcc(fexTranReq.getCotpBankAcc());
            entity.setCdSign(cdSign);

            corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
            corpSearchVO.setTranType(fexTranReq.getTranType());
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_3);
            corpSearchVO.setProId(fexTranReq.getProId());
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                //支付方式
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }
        }else if(ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())){
            if(!fexTranReq.getCurId().equals(fexTranReq.getTargetAccCurId())){
                entity.setCurId(fexTranReq.getTargetAccCurId());
            }else {
                entity.setCurId(fexTranReq.getCurId());
            }
            //资金调拨交易-现金流付款账号币种对应付款方币种
            entity.setPayerCurId(fexTranReq.getCurId());
            if(!fexTranReq.getCurId().equals(fexTranReq.getTargetAccCurId())){
                entity.setPayeeCurId(fexTranReq.getCurId());
            }else {
                entity.setPayeeCurId(fexTranReq.getTargetAccCurId());
            }
            entity.setPayMoney(fexTranReq.getPayMoney());
            //资金调拨交易-生成两笔现金流
            if(CommonConstant.YES.equals(cdSign)){
                //付款-源单位
                entity.setCorpId(fexTranReq.getCorpId());
                entity.setCorpAccId(fexTranReq.getSrcAccId());
                entity.setCorpBank(fexTranReq.getSrcBank());
                entity.setCorpAccName(fexTranReq.getSrcAccName());
                entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());
                entity.setCdSign(CommonConstant.YES);
                //交易对手-目标单位
                entity.setCotpId(fexTranReq.getTargetCorp());
                entity.setCotpBank(fexTranReq.getTargetBank());
                entity.setCotpAccId(fexTranReq.getTargetAccId());
                entity.setCotpAccName(fexTranReq.getTargetAccName());
                entity.setCotpBankAcc(fexTranReq.getTargetBankAcc());;

                corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
                corpSearchVO.setTranType(fexTranReq.getTranType());
                corpSearchVO.setCorpId(fexTranReq.getCorpId());
                corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
                corpSearchVO.setProId(fexTranReq.getProId());
                FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
                if(!EmptyUtils.isEmpty(corpBankAcc)){
                    //支付方式
                    entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
                }
            }else{
                //收款-目标单位
                entity.setCorpId(fexTranReq.getTargetCorp());
                entity.setCorpBank(fexTranReq.getTargetBank());
                entity.setCorpAccId(fexTranReq.getTargetAccId());
                entity.setCorpAccName(fexTranReq.getTargetAccName());
                entity.setCorpBankAcc(fexTranReq.getTargetBankAcc());
                entity.setCdSign(CommonConstant.NO);
                //交易对手-源单位
                entity.setCotpId(fexTranReq.getCorpId());
                entity.setCotpAccId(fexTranReq.getSrcAccId());
                entity.setCotpBank(fexTranReq.getSrcBank());
                entity.setCotpAccName(fexTranReq.getSrcAccName());
                entity.setCotpBankAcc(fexTranReq.getSrcBankAcc());

                cotpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_COTP);
                cotpSearchVO.setTranType(fexTranReq.getTranType());
                cotpSearchVO.setCorpId(fexTranReq.getCotpId());
                cotpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
                cotpSearchVO.setProId(fexTranReq.getProId());
                FexCorpBankAccDtlVO cotpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
                if(!EmptyUtils.isEmpty(cotpBankAcc)){
                    //支付方式
                    entity.setVoucherTypeId(cotpBankAcc.getVoucherTypeId());
                }
            }
        }else if(ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())){
            //资金收付交易-生成一笔现金流
            entity.setCorpId(fexTranReq.getCorpId());
            //主体单位信息
            entity.setCorpAccId(fexTranReq.getCorpAccId());
            entity.setCorpBank(fexTranReq.getCorpBank());
            entity.setCorpAccName(fexTranReq.getCorpAccName());
            entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());
            //交易对手信息
            entity.setCotpId(fexTranReq.getCotpId());
            entity.setCotpBank(fexTranReq.getCotpBank());
            entity.setCotpAccId(fexTranReq.getCotpAccId());
            entity.setCotpAccName(fexTranReq.getCotpAccName());
            entity.setCotpBankAcc(fexTranReq.getCotpBankAcc());
            entity.setCurId(fexTranReq.getCurId());
            if (StringUtils.isEmpty(entity.getCotpBank()) && StringUtils.isNotEmpty(fexTranReq.getCotpAccId())) {
                FexCotpBankAccVO cotpBankAcc = getFexCotpBankAccById(fexTranReq.getCotpAccId());
                if (null != cotpBankAcc){
                    entity.setCotpBank(cotpBankAcc.getBankName());
                }
            }
            //资金收付交易-现金流付款账号币种根据收付款标志判断
            if(CommonConstant.YES.equals(fexTranReq.getCdSign())){
                //付款取主体单位账号币种
                entity.setPayerCurId(fexTranReq.getCorpAccCurId());
            }else{
                //收款去交易对手账号币种
                entity.setPayerCurId(fexTranReq.getCotpAccCurId());
            }
            //写入交易对手币种
            entity.setPayeeCurId(fexTranReq.getCotpAccCurId());
            entity.setPayMoney(fexTranReq.getPayMoney());
            entity.setCdSign(fexTranReq.getCdSign());

            corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
            corpSearchVO.setTranType(fexTranReq.getTranType());
            corpSearchVO.setCorpId(fexTranReq.getCorpId());
            corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
            corpSearchVO.setProId(fexTranReq.getProId());
            FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
            if(!EmptyUtils.isEmpty(corpBankAcc)){
                //支付方式
                entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
            }
        }
        //是否跳过结算执行
        String isSkipSettle = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISSKIPSETTLE).getValue();
        boolean saveExc = false;//是否生成指令，默认否
        if(CommonConstant.YES.equals(isSkipSettle)) {
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(CommonConstant.YES);
            saveExc = true;//跳过结算执行，默认生成指令
            if (CommonConstant.YES.equals(entity.getCdSign())) {
                //资金收付交易且结算类型为全额审批通过后产生指令
                if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                        && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())
                        && !CommonConstant.YES.equals(fexTranReq.getIsPayed())) {
                    entity.setPayStatus(CommonConstant.NO);
                } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                        && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())) {
                    //即期结汇的外汇买卖交易付款现金流产生付款指令
                    if (ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                            && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())) {
                        entity.setPayStatus(CommonConstant.NO);
                    }
                }else if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())
                        && !CommonConstant.YES.equals(fexTranReq.getIsPayed())) {
                    entity.setPayStatus(CommonConstant.NO);
                }
            }else if(CommonConstant.NO.equals(entity.getCdSign())) {
                if (ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    //资金调拨（不同币种）收款现金流生成付款指令
                    if(!fexTranReq.getCurId().equals(fexTranReq.getTargetAccCurId())) {
                        entity.setPayStatus(CommonConstant.NO);
                    }
                } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                    //外汇买卖交易：即期、购汇、本金交割，固定金额为1-买入金额，非掉期交易
                    if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                            && ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq.getFexBusType())
                            && CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                            && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                        if (CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                            entity.setPayStatus(CommonConstant.NO);
                        }
                    } else if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                            && ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                            && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                        //即期、结汇、非掉期交易
                        entity.setPayStatus(CommonConstant.NO);
                    }
                }
            }
            //获取付款账号
            BtBankAccDTO payerAcc = null;
            if(CommonConstant.NO.equals(entity.getCdSign())) {
                //收款现金流 交易对手为付款方
                payerAcc = btBankAccService.getBankAccById(entity.getCotpAccId());
            }else{
                //付款现金流 交易对手为收款方
                payerAcc = btBankAccService.getBankAccById(entity.getCorpAccId());
            }
            //线下付款标志为1-是，不生成指令
            if(CommonConstant.YES.equals(fexTranReq.getIsPayed())){
                entity.setStatus(CommonConstant.STATUE_CHECK);
                entity.setPayStatus(CommonConstant.YES);
                saveExc = false;
            }
            if (!CommonConstant.NO.equals(entity.getCdSign()) && !ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                //离线账号不生成指令
                if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
                    entity.setStatus(CommonConstant.STATUE_CHECK);
                    entity.setPayStatus(CommonConstant.YES);
                    saveExc = false;
                }
            }
        }else{
            entity.setStatus(CommonConstant.STATUE_TEMP);
            entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        }
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        fexCashFlowService.saveOrUpdate(entity);

        //生成指令
        if(CommonConstant.YES.equals(isSkipSettle) && saveExc) {
            if (CommonConstant.YES.equals(entity.getCdSign())) {
                //主体账户为空
                if(StringUtils.isBlank(entity.getCorpAccId())){
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORPACC_NULL);
                }
                //资金收付交易且结算类型为全额审批通过后产生指令
                if (ForeignConstant.TRAN_TYPE_4.equals(entity.getTranType())
                        && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())
                        && !CommonConstant.YES.equals(fexTranReq.getIsPayed())) {
                    //发送汇款指令
                    fexCashFlowService.saveBisForeignExc(entity, user);
                } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())
                        && ForeignConstant.SETTLE_TYPE_1.equals(entity.getSettleType())) {
                    //即期结汇的外汇买卖交易付款现金流产生付款指令
                    if (ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                            && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())) {
                        //发送汇款指令
                        fexCashFlowService.saveBisForeignExc(entity, user);
                    } else if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                            && ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq.getFexBusType())
                            && CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                            && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                        entity.setPayStatus(CommonConstant.NO);
                        //外汇买卖交易：即期、购汇、本金交割，非掉期交易
                        fexCashFlowService.saveBisForeignExc(entity, user);
                    }
                }else if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())){
                    //发送汇款指令
                    fexCashFlowService.saveBisForeignExc(entity, user);
                }
            }else if(CommonConstant.NO.equals(entity.getCdSign())){
                if(ForeignConstant.TRAN_TYPE_3.equals(entity.getTranType())) {
                    //资金调拨（不同币种）收款现金流生成付款指令
                    if(!fexTranReq.getCurId().equals(fexTranReq.getTargetAccCurId())
                            && !CommonConstant.YES.equals(fexTranReq.getIsPayed())) {
                        fexCashFlowService.saveBisForeignExc(entity, user);
                    }
                } else if (ForeignConstant.TRAN_TYPE_1.equals(entity.getTranType())) {
                    //外汇买卖交易：即期、购汇、本金交割，固定金额为1-买入金额，非掉期交易
                    if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                            && ForeignConstant.FEX_BUS_TYPE_2.equals(fexTranReq.getFexBusType())
                            && CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                            && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                        if (CommonConstant.YES.equals(fexTranReq.getFixedMoney())) {
                            fexCashFlowService.saveBisForeignExc(entity, user);
                        }
                    } else if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                            && ForeignConstant.FEX_BUS_TYPE_1.equals(fexTranReq.getFexBusType())
                            && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())) {
                        //即期、结汇、非掉期交易
                        fexCashFlowService.saveBisForeignExc(entity, user);
                    }
                }
            }
        }
    }
    /**
     * 预测现金流生成(用于买卖交易，审批通过时生成预测现金流，不推结算执行)与实际现金流的数据区别在于不会生成billcode
     * @param fexTranReq
     */
    public Boolean saveExpectFexCashFLow(FexTranReqVO fexTranReq, String cdSign, ICurrentUser user, Boolean sendExcFlag) {
        FexCashFlow entity = new FexCashFlow();
        entity.setTranBillId(fexTranReq.getId());
        entity.setCashFlowDate(DateUtils.getDate());
        //结算类型：1-全额，2-净额
        entity.setSettleType(CommonConstant.YES);
        //现金流类型：0本金，1利息,2-违约金，3-期权费，4-退款
        entity.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        //现金流项目
        entity.setCashItem(fexTranReq.getCashItem());
        entity.setSettleDate(fexTranReq.getSettleDate());
        entity.setTranType(fexTranReq.getTranType());
        //跨行标志
        entity.setDifBank(fexTranReq.getDifBank());
        //远期交易备注属于交易信息，不写入现金流备注
        if (!ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReq.getFexTranType())) {
            entity.setRmk(fexTranReq.getRmk());
        }
        //单据来源
        entity.setBillFrom(ForeignConstant.BILL_FROM_3);
        //获取主体单位账户
        FexCorpBankAccVO corpSearchVO = new FexCorpBankAccVO();

        entity.setCorpId(fexTranReq.getCorpId());
        entity.setCotpId(fexTranReq.getCotpId());
        if(CommonConstant.YES.equals(cdSign)){
            entity.setCurId(fexTranReq.getSellCurId());
            entity.setPayMoney(fexTranReq.getSellMoney());

            entity.setCorpAccId(fexTranReq.getSrcAccId());
            entity.setCorpBank(fexTranReq.getSrcBank());
            entity.setCorpAccName(fexTranReq.getSrcAccName());
            entity.setCorpBankAcc(fexTranReq.getSrcBankAcc());

            entity.setCotpBank(fexTranReq.getCorpBank());
        }else{
            entity.setCurId(fexTranReq.getBuyCurId());
            entity.setPayMoney(fexTranReq.getBuyMoney());

            entity.setCorpAccId(fexTranReq.getCorpAccId());
            entity.setCorpBank(fexTranReq.getCorpBank());
            entity.setCorpAccName(fexTranReq.getCorpAccName());
            entity.setCorpBankAcc(fexTranReq.getCorpBankAcc());

            entity.setCotpBank(fexTranReq.getSrcBank());
        }
        entity.setCdSign(cdSign);
        //外汇买卖交易-现金流付款账号对应的卖出币种
        entity.setPayerCurId(fexTranReq.getSellCurId());

        corpSearchVO.setBusType(ForeignConstant.FEX_BUS_TYPE_CORP);
        corpSearchVO.setTranType(fexTranReq.getTranType());
        corpSearchVO.setCorpId(fexTranReq.getCorpId());
        corpSearchVO.setCashFlowType(ForeignConstant.CASH_FLOW_TYPE_0);
        corpSearchVO.setProId(fexTranReq.getProId());
        FexCorpBankAccDtlVO corpBankAcc = fexCorpBankAccDtlService.getBankAccToTranUnique(corpSearchVO);
        if(!EmptyUtils.isEmpty(corpBankAcc)){
            //支付方式
            entity.setVoucherTypeId(corpBankAcc.getVoucherTypeId());
        }

        //是否生成指令，默认生成指令
        boolean saveExc = Boolean.TRUE;
        //获取付款账号
        BtBankAccDTO payerAcc = btBankAccService.getBankAccById(entity.getCorpAccId());
        //线下付款标志为1-是，不生成指令
        if(CommonConstant.YES.equals(fexTranReq.getIsPayed())){
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(CommonConstant.YES);
            saveExc = false;
        } else if (payerAcc == null || BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) { //离线账号不生成指令
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(CommonConstant.YES);
            saveExc = false;
        }
        // 掉期不产生指令
        if(CommonConstant.YES.equals(fexTranReq.getIsSwapTran()) || !sendExcFlag) {
            saveExc = false;
        }
        // 掉期及生命周期产生关联交易号的不需要发送指令
        if (StringUtils.isNotEmpty(fexTranReq.getRelBillNo())) {
            saveExc = false;
        }
        //是否跳过结算执行
        String isSkipSettle = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISSKIPSETTLE).getValue();
        if(CommonConstant.YES.equals(isSkipSettle)) {
            entity.setStatus(CommonConstant.STATUE_CHECK);
            entity.setPayStatus(CommonConstant.NO);
        } else {
            entity.setStatus(CommonConstant.STATUE_TEMP);
            entity.setPayStatus(ForeignConstant.PAY_STATUS_0);
        }
        entity.setCreateBy(fexTranReq.getCreateBy());
        entity.setCreateDate(DateUtils.getDate());
        fexCashFlowService.saveOrUpdate(entity);

        Boolean isSendExc = Boolean.FALSE;
        //生成指令
        if(saveExc) {
            //主体账户为空
            if(StringUtils.isBlank(entity.getCorpAccId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORPACC_NULL);
            }
            isSendExc = fexCashFlowService.saveBisForeignExc(entity, user, fexTranReq.getBillNo());
        }
        return isSendExc;
    }
    /**
     * 查询交易信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesForeignReport(DataTablesInput dataTablesInput, ICurrentUser user) {
        //外汇交易类型 0-即期、1-远期
        String fexTranType = (String) dataTablesInput.getFormdata().get("fexTranType");
        StringBuilder sql = new StringBuilder();
        sql.append(" and t.fex_Tran_Type='" + fexTranType+"'");
        String resource = "";
        String joinSql = "";
        if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranType)) {
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_FEXTRANTYPE0_REPORT_QUERY;
        } else if (ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranType)) {
            resource = ResourceConstant.FOREIGN_FEXTRANREQ_FEXTRANTYPE1_REPORT_QUERY;
        }
        if (!StringUtils.isBlank(resource)) {
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
            joinSql += " and " + corpAuthSql;
        }
        sql.append(joinSql);
        if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranType)) {
            //过滤掉交易失败的数据
            sql.append(" and not exists (select 1 from fex_cash_flow w left join bis_foreign_exc exc on w.id = exc.cbs_bill_id where w.tran_bill_id = t.id and exc.voucher_stat in ('2','9'))");
        }
        appendExternalCorpNameFilters(dataTablesInput, sql);
        //记账汇率类型
        String recordRateType = (String) dataTablesInput.getFormdata().get("recordRateType");
        //即期汇率类型
        String spotRateType = (String) dataTablesInput.getFormdata().get("spotRateType");
        if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranType)) {
            spotRateType = recordRateType;
        }
        //重估日期
        String expectDate = (String) dataTablesInput.getFormdata().get("expectDate");
        String standardCur = (String) dataTablesInput.getFormdata().get("standardCur");
        //获取取款成功的明细
        DataTablesOutput datas = fexTranReqDao.getDataTablesUnion(dataTablesInput, sql.toString());
        if (datas != null && datas.getData() != null) {
            List<FexTranReqVO> list = datas.getData();
            injectValue(list);
            for (FexTranReqVO vo : list) {
                //买入币种记账汇率 实际为 记账本位币汇率；货币对记账汇率 实际为 记账汇率
                //买入币种记账汇率：买入币种 + 记账本位币 + 记账汇率类型
                BigDecimal buyRecordRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getBuyCurId(),standardCur,DateUtils.parseDate(expectDate), recordRateType);
                if(buyRecordRate == null){
                    vo.setId("1");//标识：用于提示汇率未维护消息
                }
                //货币对记账汇率：卖出币种 + 买入币种 + 记账汇率类型
                BigDecimal recordRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getSellCurId(),vo.getBuyCurId(),DateUtils.parseDate(expectDate), recordRateType);
                if(recordRate == null){
                    vo.setId("2");//标识：用于提示汇率未维护消息
                }
                //设置记账汇率
                vo.setRecordRate(recordRate);
                //设置记账损益：[买入金额 - 卖出金额 * 货币对记账汇率] *买入币种记账汇率
                if(buyRecordRate == null || recordRate == null || vo.getSellMoney() == null || vo.getBuyMoney()==null){
                    vo.setRecordProfit(null);
                }else{
                    BigDecimal sell = vo.getSellMoney().multiply(recordRate);
                    BigDecimal subtract = vo.getBuyMoney().subtract(sell);
                    vo.setRecordProfit(subtract.multiply(buyRecordRate));
                }

                //远期处理-设置即期汇率
                if (ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranType)) {
                    //买入币种即期汇率：买入币种 + 记账本位币 + 即期汇率类型
                    BigDecimal buySpotRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getBuyCurId(),standardCur, DateUtils.parseDate(vo.getSettleDate()), spotRateType);
                    if(buySpotRate == null){
                        vo.setId("1");//标识：用于提示汇率未维护消息
                    }
                    //货币对即期汇率：卖出币种 + 买入币种 + 即期汇率类型
                    BigDecimal spotRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getSellCurId(),vo.getBuyCurId(),DateUtils.parseDate(expectDate), spotRateType);
                    if(spotRate == null){
                        vo.setId("2");//标识：用于提示汇率未维护消息
                    }

                    //设置即期汇率
                    vo.setSpotRate(spotRate);
                    //设置实际损益：[ 买入金额 - 卖出金额 * 货币对即期汇率 ] *买入币种即期汇率
                    if(buySpotRate == null || spotRate == null || vo.getSellMoney() == null || vo.getBuyMoney()==null){
                        vo.setRealProfit(null);
                    }else{
                        BigDecimal sell = vo.getSellMoney().multiply(spotRate);
                        BigDecimal subtract = vo.getBuyMoney().subtract(sell);
                        vo.setRealProfit(subtract.multiply(buySpotRate));
                    }

                    if(buySpotRate != null && spotRate != null ){
                        //交割或展期 [ 实际损益 = (累计买入金额 - 累计卖出金额 * 货币对即期汇率) * 买入币种即期汇率 ]
                        List<FexTranDeal> fexTranDeals1 = fexTranDealService.listByTranReqId(ForeignConstant.OP_TYPE_1,vo.getId());
                        if(fexTranDeals1 != null && fexTranDeals1.size()>0){
                            //累计买入金额
                            BigDecimal sumBuy = new BigDecimal(0);
                            //累计卖出金额
                            BigDecimal sumSell = new BigDecimal(0);
                            for(FexTranDeal e :fexTranDeals1){
                                if(CommonConstant.YES.equals(e.getDeliverySign())){
                                    sumBuy = sumBuy.add(e.getSettleMoney());
                                }else{
                                    sumSell = sumSell.add(e.getSettleMoney());
                                }
                            }
                            BigDecimal multiply = sumSell.multiply(spotRate);
                            BigDecimal subtract = sumBuy.subtract(multiply);
                            vo.setRealProfit(subtract.multiply(buySpotRate));

                            //设置远期的记账损益 [ 记账损益 = (累计买入金额 - 累计卖出金额 * 货币对记账汇率) * 买入币种记账汇率 ]
                            if(recordRate != null && buySpotRate != null){
                                BigDecimal multiply2 = sumSell.multiply(buySpotRate);
                                BigDecimal subtract2 = sumBuy.subtract(multiply2);
                                vo.setRecordProfit(subtract2.multiply(recordRate));
                            }
                            continue;
                        }
                        //违约 [ 实际损益 = -违约金*买入币种即期汇率 ]
                        List<FexTranDeal> fexTranDeals2 = fexTranDealService.listByTranReqId(ForeignConstant.OP_TYPE_2,vo.getId());
                        if(fexTranDeals2 != null && fexTranDeals2.size()>0){
                            FexTranDeal fexTranDeal2 = fexTranDeals2.get(0);
                            BigDecimal multiply = fexTranDeal2.getDefaultMoney().multiply(buySpotRate);
                            vo.setRealProfit(multiply.negate());

                            //设置远期的记账损益 [ 记账损益 = -违约金*买入币种记账汇率 ]
                            if(recordRate != null && buySpotRate != null){
                                BigDecimal multiply2 = fexTranDeal2.getDefaultMoney().multiply(recordRate);
                                vo.setRecordProfit(multiply2.negate());
                            }
                            continue;
                        }
                        //NDF交割 [ 实际损益 = NDF交割金额 * 买入币种即期汇率 ]
                        List<FexTranDeal> fexTranDeals3 = fexTranDealService.listByTranReqId(ForeignConstant.OP_TYPE_3,vo.getId());
                        if(fexTranDeals3 != null && fexTranDeals3.size()>0){
                            FexTranDeal fexTranDeal3 = fexTranDeals3.get(0);
                            BigDecimal multiply = fexTranDeal3.getSettleMoney().multiply(spotRate);
                            vo.setRealProfit(multiply);

                            //设置远期的记账损益 [ 记账损益 = NDF交割金额 * 货币对记账汇率(交割) ]
                            BigDecimal deliveryRate = fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getDeliveryCurId(),vo.getBuyCurId(),DateUtils.parseDate(expectDate), recordRateType);
                            if(deliveryRate != null){
                                BigDecimal multiply2 = fexTranDeal3.getSettleMoney().multiply(deliveryRate);
                                vo.setRecordProfit(multiply2);
                            }
                        }
                    }
                }
            }
        }
        return datas;
    }

    /**
     * 条件检查项
     *
     * @param input 条件输入流
     * @return true or false
     */
    public boolean checkConditions(DataTablesInput input) {
        if (input.getDraw() == 1) {
            return true;
        }
        String recordRateType = (String) input.getFormdata().get("recordRateType");
        String expectDate = (String) input.getFormdata().get("expectDate");
        String standardCur = (String) input.getFormdata().get("standardCur");

        String fexTranType = (String) input.getFormdata().get("fexTranType");
        if (ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranType)) {
            String spotRateType = (String) input.getFormdata().get("spotRateType");
            if (StringUtils.isEmpty(spotRateType)) {
                return true;
            }
        }

        if (StringUtils.isEmpty(recordRateType)){
            return true;
        }
        if (StringUtils.isEmpty(expectDate)) {
            return true;
        }
        return StringUtils.isEmpty(standardCur);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有外汇买卖交易单据，不允许删除！
            int num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_1,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANFOREIGN,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DEL_TRANFOREIGN_ERROR)));
            }
            //单位有外汇期权交易单据，不允许删除！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_2,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANOPTION,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DEL_TRANOPTION_ERROR)));
            }
            //单位有资金调拨交易单据，不允许删除！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_3,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANALLOCATION,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DEL_TRANALLOCATION_ERROR)));
            }
            //单位有资金收付单据，不允许删除！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_4,"del");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANPAY,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DEL_TRANPAY_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if(StringUtils.isNotEmpty(corpId)){
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的外汇买卖交易单据，不允许禁用！
            int num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_1,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANFOREIGN,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANFOREIGN_ERROR)));
            }
            //单位有在途的外汇期权交易单据，不允许禁用！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_2,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANOPTION,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANOPTION_ERROR)));
            }
            //单位有在途的资金调拨交易单据，不允许禁用！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_3,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANALLOCATION,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANALLOCATION_ERROR)));
            }
            //单位有在途的资金收付单据，不允许禁用！
            num = this.fexTranReqDao.getTranReqCountByCorpId(corpId, ForeignConstant.TRAN_TYPE_4,"disable");
            if(num>0){
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FEX,
                        SysConfigConstant.BusType.FEX_TRANPAY,
                        multiLangService.getLang(CorpVerifyMessageConstant.FEX_VALID_DISABLE_TRANPAY_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    /**
     * 批量选择付款账号
     *
     * @param fexTranReqVO
     * @param user
     * @param isChangeTemplate 是否需要改变模板
     */
    public void batchSetPayerAcc(FexTranReqVO fexTranReqVO, ICurrentUser user, Boolean isChangeTemplate){
        if (StringUtils.isNotEmpty(fexTranReqVO.getId())) {
            String[] ids = fexTranReqVO.getId().split(",");
            // 自定义交易需要修改模板
            for (String id : ids) {
                FexTranReq entity = this.get(id);
                // 修改模板ID
                String bankPayType = entity.getBankPayTypeId();
                if (isChangeTemplate){
                    // 银行变更时清空模板
                    if (!StringUtils.equals(entity.getCorpBank(), fexTranReqVO.getCorpBank())) {
                        entity.setTemplateId(null);
                        entity.setTemplateName(null);
                        entity.setBankPayType(null);
                        entity.setBankPayTypeName(null);
                    } else {
                        FexTranTemplateConfigVO templateConfig = fexTranConfigTemplateService.getTemplateByBankAcc(fexTranReqVO.getCorpBankAcc(), bankPayType);
                        if (null != templateConfig){
                            entity.setTemplateId(templateConfig.getId());
                            entity.setTemplateName(templateConfig.getTemplateName());
                            entity.setBankPayType(templateConfig.getPayTypeId());
                            entity.setBankPayTypeName(templateConfig.getPayTypeName());
                        } else {
                            entity.setTemplateId(null);
                            entity.setTemplateName(null);
                            entity.setBankPayType(null);
                            entity.setBankPayTypeName(null);
                        }
                    }
                }

                //暂存和编辑状态才可以提交
                if (entity != null && !ForeignConstant.STATUE_TEMP.equals(entity.getStatus()) && !ForeignConstant.STATUE_BACK.equals(entity.getStatus())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
                }
                entity.setCorpBank(fexTranReqVO.getCorpBank());
                entity.setCorpBankAcc(fexTranReqVO.getCorpBankAcc());
                entity.setAccType(fexTranReqVO.getAccType());
                entity.setCorpAccId(fexTranReqVO.getCorpAccId());
                entity.setCorpAccName(fexTranReqVO.getCorpAccName());
                entity.setCorpAccCurId(fexTranReqVO.getCorpAccCurId());
                //费用支出账号同步调整
                if (CommonConstant.YES.equals(entity.getCostSign())) {
                    entity.setCostAccId(fexTranReqVO.getCorpAccId());
                    entity.setCostAcc(fexTranReqVO.getCorpBankAcc());
                }
                this.updateEntity(entity);
                //生成日志
                fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_SET_PAYERACC, "", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
            }
        }
    }

    /**
     * 判断是否是合并单据
     *
     * @param id
     * @return
     */
    public boolean isMergeBill(String id) {
        //获取合并明细
        List<FexTranReq> list = fexTranReqDao.listBy("mergeId", id);
        return !EmptyUtils.isEmptyList(list);
    }

    /**
     * 拆分保存
     *
     * @param fexTranReqVO
     * @param payMoneys
     * @param user
     */
    public void splitSave(FexTranReqVO fexTranReqVO, List<BigDecimal> payMoneys,List<String> budgets,
                          List<BigDecimal> tranAmt1s, List<BigDecimal> tranAmt2s, ICurrentUser user) {
        // 批量获取付款流水号
        List<String> listSequence = SequenceSingleton.listSequence(SequenceEnum.FEX_TRAN_REQ, payMoneys.size());
        //更新原单状态为删除
        FexTranReq entity = this.get(fexTranReqVO.getId(),true);
        //生成拆分单据
        for (int i = 0; i < payMoneys.size(); i++) {
            FexTranReq fexTranReq = new FexTranReq();
            BeanUtils.copyNotNullProperties(fexTranReqVO, fexTranReq);
            fexTranReq.setBillNo(listSequence.get(i));
            fexTranReq.setId(null);
            fexTranReq.setPayMoney(payMoneys.get(i));
            if (EmptyUtils.isNotEmpty(tranAmt1s)) {
                fexTranReq.setTranAmt1(tranAmt1s.get(i));
            }
            if (EmptyUtils.isNotEmpty(tranAmt2s)) {
                fexTranReq.setTranAmt2(tranAmt2s.get(i));
            }
            fexTranReq.setSplitId(fexTranReqVO.getId());//保存拆分前单号
            fexTranReq.setCreateBy(user.getLoginName());
            fexTranReq.setCreateDate(DateUtils.getDate());
            //原始拆分前单号
            if (StringUtils.isNotBlank(entity.getOrigBillNo())) {
                fexTranReq.setOrigBillNo(entity.getOrigBillNo());//原单据号
                fexTranReq.setRelBillNo(entity.getOrigBillNo());
            } else {
                fexTranReq.setOrigBillNo(entity.getBillNo());//原单据号
                fexTranReq.setRelBillNo(entity.getBillNo());
            }
            //设置为删除状态
            fexTranReq.setStatus(CommonConstant.STATUE_TEMP);
            fexTranReq.setBillFrom(ForeignConstant.BILL_FROM_10);
            this.saveEntity(fexTranReq);

            //判断是否挂计划
            if (!StringUtils.isBlank(budgets.get(i))) {
                List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
                FexTranReqVO vo = new FexTranReqVO();
                BeanUtils.copyNotNullProperties(fexTranReq, vo);
                String[] itemCodes = budgets.get(i).split(",");
                for (String itemCode : itemCodes) {
                    String[] budget = itemCode.split(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    FbsBudgetProcessDTO dto = this.bulidFbsBudgetProcessDTO(vo, user);
                    dto.setItemCode(budget[0]);
                    dto.setItemName(budget[1]);
                    dto.setProcessMoney(new BigDecimal(budget[2]));
                    dto.setExchangeRate(new BigDecimal(budget[3]));
                    dto.setProcessCurMoney(new BigDecimal(budget[4]));
                    listBudget.add(dto);
                }
                if (!EmptyUtils.isEmptyList(listBudget)) {
                    fbsBudgetProcessService.writeBudgetProcess(listBudget, user);
                }
            }
            //生成日志
            fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_TEMP, FexMessageConstant.FEX_LOG_SPLIT_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);

        }
        /*entity.setStatus(CommonConstant.STATUE_DELETE);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.updateEntity(entity);*/
        //更新原单状态为删除
        FexTranReqVO vo = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(CommonConstant.STATUE_SPLIT);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        //拆分付款单，控制原状态为 0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateFexTranReqStatus(vo, oldStatus);
        //释放原单据资金计划
        List<FbsBudgetProcessDTO> listProcess = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
        if (!EmptyUtils.isEmptyList(listProcess)) {
            fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
        }
    }

    /**
     * 撤销拆分
     *
     * @param id
     */
    public void splitCancel(String id, ICurrentUser user) {
        FexTranReq fexTranReq = this.get(id, true);
        //根据拆分id，获取同一批次拆分单据
        List<FexTranReq> fexTranReqList = this.fexTranReqDao.listBy("splitId",fexTranReq.getSplitId());
        if (!EmptyUtils.isEmptyList(fexTranReqList)) {
            String billNos = "";
            for(FexTranReq entity : fexTranReqList){
                if (!CommonConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                        && !CommonConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())) {
                    if (StringUtils.isBlank(billNos)) {
                        billNos = entity.getBillNo();
                    } else {
                        billNos = billNos + CommonConstant.COMMA_SPLIT + entity.getBillNo();
                    }
                }
            }
            if (StringUtils.isNotBlank(billNos)) {
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_SPLITCANCEL_VALID, billNos);
            }
        }
        //还原原单据状态
        FexTranReq oldFexTranReq = this.get(fexTranReq.getSplitId(), true);
        /*oldFexTranReq.setStatus(CommonConstant.STATUE_TEMP);
        oldFexTranReq.setUpdateDate(DateUtils.getDate());
        oldFexTranReq.setUpdateBy(user.getLoginName());
        this.updateEntity(oldFexTranReq);*/
        //还原拆分前单据
        FexTranReqVO vo = new FexTranReqVO();
        BeanUtils.copyNotNullProperties(oldFexTranReq, vo);
        vo.setStatus(CommonConstant.STATUE_TEMP);
        vo.setUpdateDate(DateUtils.getDate());
        vo.setUpdateBy(user.getLoginName());
        //撤销拆分，控制原付款单状态为-2已删除
        Integer[] oldStatus = {CommonConstant.STATUE_SPLIT};
        this.updateFexTranReqStatus(vo, oldStatus);

        //删除拆分单据
        for(FexTranReq entity : fexTranReqList){
            /*entity.setStatus(CommonConstant.STATUE_DELETE);
            entity.setUpdateDate(DateUtils.getDate());
            entity.setUpdateBy(user.getLoginName());
            entity.setSplitId(null);
            entity.setOrigBillNo(null);*/
            FexTranReqVO dtlVo = new FexTranReqVO();
            BeanUtils.copyNotNullProperties(entity, dtlVo);
            dtlVo.setStatus(CommonConstant.STATUE_DELETE);
            dtlVo.setUpdateDate(DateUtils.getDate());
            dtlVo.setUpdateBy(user.getLoginName());
            dtlVo.setSplitId(null);
            dtlVo.setOrigBillNo(null);
            //撤销拆分，控制拆分明细单状态为0暂存/-1打回
            Integer[] oldStatusDtl = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateFexTranReqStatus(dtlVo, oldStatusDtl);

            //释放单据资金计划
            List<FbsBudgetProcessDTO> listProcess = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
            if (!EmptyUtils.isEmptyList(listProcess)) {
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, entity.getBillNo());
            }
            //this.updateEntity(entity);
            fexMessageService.saveMessage(entity.getId(), FexMessageConstant.MESSAGE_TYPE_TEMP, FexMessageConstant.FEX_LOG_SPLIT_CANCEL_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
        }
    }

    /**
     * 付款单合并
     *
     * @param ids
     */
    public void merge(String ids, ICurrentUser user) {
        List<FexTranReq> list = new ArrayList<FexTranReq>();
        String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
        if (idAtr.length > ForeignConstant.FEX_TRAN_REQ_MERGE_MAX_COUNT) {
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_MERGE_MAX);
        }
        String origBillNo = "";
        Set<String> templateIdSet = new HashSet<>();
        //获取第一笔单据
        FexTranReq firstFex = fexTranReqDao.get(idAtr[0], true);
        //获取第一笔单据的资金计划
        List<FbsBudgetProcessDTO> firstBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, firstFex.getBillNo());
        for(String id : idAtr){
            FexTranReq fexTranReq = this.fexTranReqDao.get(id,true);
            // 合并单据,模板要一致
            templateIdSet.add(fexTranReq.getTemplateId());
            if (1 < templateIdSet.size()){
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TEMPLATE_DIFF);
            }

            if(StringUtils.isBlank(origBillNo)){
                origBillNo = fexTranReq.getBillNo();
            }else{
                origBillNo = origBillNo + "," + fexTranReq.getBillNo();
            }

            list.add(fexTranReq);
            List<FbsBudgetProcessDTO> fexTranReqBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranReq.getBillNo());
            if ((!EmptyUtils.isEmptyList(fexTranReqBudget) && EmptyUtils.isEmptyList(firstBudget)) ||
                    (!EmptyUtils.isEmptyList(firstBudget) && EmptyUtils.isEmptyList(fexTranReqBudget))) {
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_FBSBUDGET_CONSISTENT);
            }
        }
        //初始化金额
        BigDecimal payMoney = BigDecimal.ZERO;
        BigDecimal tranAmt1 = BigDecimal.ZERO;
        BigDecimal tranAmt2 = BigDecimal.ZERO;
        //对“合同号、交易附言、交易地点、外汇局批件/备案表号/业务编号、发票号、附言”进行判断，一致则直接合并展示，不一致则通过分号隔开展示
        String contractNo = "";
        String tranAddWord1 = "";
        String tranAddWord2 = "";
        String tranAddr = "";
        String busNo = "";
        String invoiceNo = "";
        String addWord = "";
        //合并附件
        List<String> attachmentList = new ArrayList<String>();
        //合并计划
        List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
        Boolean tranAmtFlag = false;//合并单据中是否有相应金额1
        for (FexTranReq f : list) {
            //合计金额
            payMoney = payMoney.add(f.getPayMoney());
            if (!StringUtils.isBlank(f.getAttachmentId())) {
                attachmentList.add(f.getAttachmentId());
            }
            if (!ForeignConstant.REMIT_TYPE_2.equals(f.getRemitType())) {
                if (f.getTranAmt1() != null) {
                    tranAmt1 = tranAmt1.add(f.getTranAmt1());
                }
                if (f.getTranAmt2() != null) {
                    tranAmt2 = tranAmt2.add(f.getTranAmt2());
                    tranAmtFlag = true;
                }
            }
            // 合同号
            if (StringUtils.isBlank(contractNo)) {
                contractNo = f.getContractNo();
            } else {
                if (contractNo.indexOf(f.getContractNo()) == -1) {
                    contractNo = contractNo + CommonConstant.SEMI_SPLIT + f.getContractNo();
                }
            }
            // 交易附言
            if (StringUtils.isBlank(tranAddWord1)) {
                tranAddWord1 = f.getTranAddWord1();
            } else {
                if (tranAddWord1.indexOf(f.getTranAddWord1()) == -1) {
                    tranAddWord1 = tranAddWord1 + CommonConstant.SEMI_SPLIT + f.getTranAddWord1();
                }
            }
            if (StringUtils.isBlank(tranAddWord2)) {
                tranAddWord2 = f.getTranAddWord2();
            } else {
                if (tranAddWord2.indexOf(f.getTranAddWord2()) == -1) {
                    tranAddWord2 = tranAddWord2 + CommonConstant.SEMI_SPLIT + f.getTranAddWord2();
                }
            }
            // 交易地点
            if (StringUtils.isBlank(tranAddr)) {
                tranAddr = f.getTranAddr();
            } else {
                if (tranAddr.indexOf(f.getTranAddr()) == -1) {
                    tranAddr = tranAddr + CommonConstant.SEMI_SPLIT + f.getTranAddr();
                }
            }
            // 外汇局批件/备案表号/业务编号
            if (StringUtils.isBlank(busNo)) {
                busNo = f.getBusNo();
            } else {
                if (busNo.indexOf(f.getBusNo()) == -1) {
                    busNo = busNo + CommonConstant.SEMI_SPLIT + f.getBusNo();
                }
            }
            // 发票号
            if (StringUtils.isBlank(invoiceNo)) {
                invoiceNo = f.getInvoiceNo();
            } else {
                if (invoiceNo.indexOf(f.getInvoiceNo()) == -1) {
                    invoiceNo = invoiceNo + CommonConstant.SEMI_SPLIT + f.getInvoiceNo();
                }
            }
            // 附言
            if (StringUtils.isBlank(addWord)) {
                addWord = f.getAddWord();
            } else {
                if (addWord.indexOf(f.getAddWord()) == -1) {
                    addWord = addWord + CommonConstant.SEMI_SPLIT + f.getAddWord();
                }
            }

            //获取单据的资金计划
            List<FbsBudgetProcessDTO> listProcess = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, f.getBillNo());
            if (!EmptyUtils.isEmptyList(listProcess)) {
                listBudget.addAll(listProcess);
                //释放单据的资金计划
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, f.getBillNo());
            }
        }
        FexTranReq fexTranReq = new FexTranReq();
        BeanUtils.copyNotNullProperties(firstFex, fexTranReq);
        //合并附件
        if (!EmptyUtils.isEmptyList(attachmentList)) {
            fexTranReq.setAttachmentId(attachmentService.mergeGroupIds(attachmentList));
        }
        fexTranReq.setId(null);
        fexTranReq.setBillNo(SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_REQ));
        fexTranReq.setOrigBillNo(origBillNo);
        fexTranReq.setRelBillNo(origBillNo);
        //汇总金额
        fexTranReq.setPayMoney(payMoney);
        if (!ForeignConstant.REMIT_TYPE_2.equals(fexTranReq.getRemitType())) {
            fexTranReq.setTranAmt1(tranAmt1);
            if (tranAmtFlag) {
                fexTranReq.setTranAmt2(tranAmt2);
            }
        }
        // 合同号
        fexTranReq.setContractNo(contractNo);
        // 交易附言
        fexTranReq.setTranAddWord1(tranAddWord1);
        fexTranReq.setTranAddWord2(tranAddWord2);
        // 交易地点
        fexTranReq.setTranAddr(tranAddr);
        // 外汇局批件/备案表号/业务编号
        fexTranReq.setBusNo(busNo);
        // 发票号
        fexTranReq.setInvoiceNo(invoiceNo);
        // 附言
        fexTranReq.setAddWord(addWord);
        fexTranReq.setStatus(NetbankConstant.STATUE_TEMP);
        fexTranReq.setBillFrom(ForeignConstant.BILL_FROM_15);
        fexTranReq.setCreateBy(user.getLoginName());
        fexTranReq.setCreateDate(DateUtils.getDate());
        fexTranReq.setMergeId(StringUtils.getUUId());
        //合并单据入库
        this.saveEntity(fexTranReq);

        //生成日志
        fexMessageService.saveMessage(fexTranReq.getId(), FexMessageConstant.MESSAGE_TYPE_TEMP, FexMessageConstant.FEX_LOG_MERGE_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);

        //合并资金计划
        if (!EmptyUtils.isEmptyList(listBudget)) {
            HashMap<String, FbsBudgetProcessDTO> mapBudget = new HashMap<String, FbsBudgetProcessDTO>();
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                if (!mapBudget.containsKey(fbsBudgetProcessDTO.getItemCode())) {
                    mapBudget.put(fbsBudgetProcessDTO.getItemCode(), fbsBudgetProcessDTO);
                } else {
                    //相同科目合计金额
                    FbsBudgetProcessDTO dto = mapBudget.get(fbsBudgetProcessDTO.getItemCode());
                    dto.setProcessMoney(dto.getProcessMoney().add(fbsBudgetProcessDTO.getProcessMoney()));
                    if (!EmptyUtils.isEmpty(dto.getProcessCurMoney()) && !EmptyUtils.isEmpty(fbsBudgetProcessDTO.getProcessCurMoney())) {
                        dto.setProcessCurMoney(dto.getProcessCurMoney().add(fbsBudgetProcessDTO.getProcessCurMoney()));
                    }
                    mapBudget.put(dto.getItemCode(), dto);
                }
                if (EmptyUtils.isEmpty(fbsBudgetProcessDTO.getProcessDate())) {
                    //结算日期
                    fbsBudgetProcessDTO.setProcessDate(fexTranReq.getSettleDate());
                }
                if (EmptyUtils.isEmpty(fbsBudgetProcessDTO.getWishPayday())) {
                    //结算日期
                    fbsBudgetProcessDTO.setWishPayday(fexTranReq.getSettleDate());
                }
            }
            listBudget = new ArrayList<FbsBudgetProcessDTO>();
            for (Map.Entry<String, FbsBudgetProcessDTO> entry : mapBudget.entrySet()) {
                listBudget.add(entry.getValue());
            }
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                fbsBudgetProcessDTO.setId(null);
                fbsBudgetProcessDTO.setBillId(fexTranReq.getId());
                fbsBudgetProcessDTO.setBillNo(fexTranReq.getBillNo());
                fbsBudgetProcessDTO.setCreateBy(fexTranReq.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(fexTranReq.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);
            }
            fbsBudgetProcessService.writeBudgetProcess(listBudget, user);
        }

        //更新合并明细单状态
        for (FexTranReq f : list) {
            /*f.setStatus(CommonConstant.STATUE_DELETE);
            f.setUpdateBy(user.getLoginName());
            f.setUpdateDate(DateUtils.getDate());
            f.setMergeId(fexTranReq.getId());
            this.fexTranReqDao.update(f);*/
            FexTranReqVO vo = new FexTranReqVO();
            BeanUtils.copyNotNullProperties(f, vo);
            vo.setStatus(CommonConstant.STATUE_MERGE);
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            vo.setMergeId(fexTranReq.getId());
            //合并付款单，控制原状态为0暂存/-1打回
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateFexTranReqStatus(vo, oldStatus);
        }
    }

    /**
     * 撤销合并
     *
     * @param ids
     */
    public void mergeCancel(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexTranReq fexTranReq = this.get(id,true);
            //获取合并明细
            List<FexTranReq> list = this.fexTranReqDao.listBy("mergeId", id);
            if (list == null || list.size() <= 0) {
                //合并单据，不可进行撤销合并操作
                throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_MERGECANCEL_VALID);
            } else {
                for (FexTranReq f : list) {
                    f.setStatus(CommonConstant.STATUE_TEMP);
                    f.setUpdateBy(user.getLoginName());
                    f.setUpdateDate(DateUtils.getDate());
                    f.setMergeId(null);
                    this.fexTranReqDao.update(f);
                    //撤销合并还原预算执行
                    fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, f.getBillNo(), BudgetConstant.STATUE_TEMP, user);
                    //生成日志
                    fexMessageService.saveMessage(fexTranReq.getId(), FexMessageConstant.MESSAGE_TYPE_TEMP, FexMessageConstant.FEX_LOG_MERGE_CANCEL_SUCC, FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ, user);
                }
                fexTranReq.setOrigBillNo(null);
                fexTranReq.setStatus(CommonConstant.STATUE_DELETE);
                fexTranReq.setUpdateBy(user.getLoginName());
                fexTranReq.setUpdateDate(DateUtils.getDate());
                this.fexTranReqDao.update(fexTranReq);

                //删除合并预算
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_TRAN, BudgetConstant.PROCESS_SYSNAME_FEX, fexTranReq.getBillNo());
            }
        }
    }

    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<FexTranReqVO> listByMergeId(String mergeId) {
        return this.fexTranReqDao.listByMergeId(mergeId);
    }

    /**
     * 根据关联交易号码获取现金流
     * @param relBillNo
     * @return
     */
    public FexTranReq getByRelBillNo(String relBillNo) {
        return fexTranReqDao.getUniqueBy("relBillNo", relBillNo);
    }

    /**
     * 判断是否存在相同的时间戳
     *
     * @param fexTranReq
     * @return
     */
    public boolean isExistTimeStamp(FexTranReqVO fexTranReq) {
        if (StringUtils.isNotBlank(fexTranReq.getTimeStamp())) {
            List<FexTranReq> list = this.fexTranReqDao.listBy("timeStamp", fexTranReq.getTimeStamp());
            if(CommonConstant.YES.equals(fexTranReq.getIsSwapTran()) && EmptyUtils.isNotEmpty(list)){
                list = list.stream().filter(item -> {
                    // 远端数据需排除近端数据校验
                    return !(item.getBillNo().equals(fexTranReq.getPartnerId())
                            && ForeignConstant.FEX_TRAN_TYPE_0.equals(item.getFexTranType()));
                }).collect(Collectors.toList());
            }
            return EmptyUtils.isNotEmpty(list);
        }
        return false;
    }

    /**
     * 带原状态更新付款单
     *
     * @param vo        付款对象
     * @param oldStatus 原状态控制
     * @return
     */
    public void updateFexTranReqStatus(FexTranReqVO vo, Integer[] oldStatus) {
        int result = 0;
        result = this.fexTranReqDao.updateFexTranReqStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }
    /**
     * 带原状态更新付款单
     *
     * @param ids        付款对象
     * @param newStatus        新状态
     * @param oldStatus 原状态控制
     * @return
     */
    public void updateFexTranReqStatus(List<String> ids, Integer newStatus,Integer[] oldStatus) {
        int result = 0;
        result = this.fexTranReqDao.updateFexTranReqStatus(ids,newStatus, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE);
        }
    }

    public DataTablesOutput dataTableForeignApprove(DataTablesInput dataTablesInput) {
        String sign = "";
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("tranType"))) {
            String tranType = String.valueOf(dataTablesInput.getFormdata().get("tranType"));
            String isSwpTran = String.valueOf(dataTablesInput.getFormdata().get("isSwpTran"));
            String fexTranType = String.valueOf(dataTablesInput.getFormdata().get("fexTranType"));
            if (EmptyUtils.isNumeric(tranType)) {
                sign = this.getWorkFlowByTranType(tranType, isSwpTran, fexTranType);
            }
        }
        WorkFlowBusiness workFlowBusiness = workFlowServiceImpl.getWorkFlowBusiness(sign, dataTablesInput);
        StringBuilder businessSql = new StringBuilder(SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true));
        appendExternalCorpNameFilters(dataTablesInput, businessSql);
        DataTablesOutput<FexTranReqVO> dp = this.fexTranReqDao.getDataTablesForeignApprove(dataTablesInput, businessSql.toString());
        DataTablesOutput<FexTranReqVO> output = this.setDataTablesUnion(dp);
        injectOutputValue(output);
        return workFlowBusiness.merge(output);
    }

    /**
     * 监听指令发送成功后修改交易状态
     * @param arguments
     */
    @XxlJob("FexTranReqService")
    @Override
    public void run(String arguments) {
        try {
            logger.info("指令成功后更新交易状态任务开始！");
            long start = System.currentTimeMillis();
            //获取审批通过的数据的id
            List<String> ids = this.fexTranReqDao.getIdsByStatus(CommonConstant.STATUE_CHECK);
            //通过数据的id集合查询是否指令通过
            List<String> excSuccessIds = bisForeignExcService.listFexTranReqIdsByStatus(BdlinkConstant.SUCCESS, ids);
            //通过指令通过的id修改交易状态
            //定义是挂牌汇率的交易id集合
            List<String> fexTranReqIds = new ArrayList<>();
            //定义不是挂牌汇率的交易id集合
            List<String> fexTranReqIds2 = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(excSuccessIds)) {
                logger.info("指令成功回写ID" + JsonUtils.toJSONString(excSuccessIds));
                List<FexTranReqExtVo> fexTranReqExtVos = fexTranReqExtService.listByReqIds(excSuccessIds);
                for (FexTranReqExtVo fexTranReqExtVo : fexTranReqExtVos) {
                    if (ForeignConstant.FEX_TRAN_RATE_TYPE_LISTED.equals(fexTranReqExtVo.getTranRateType())) {
                        fexTranReqIds.add(fexTranReqExtVo.getReqId());
                    }else{
                        fexTranReqIds2.add(fexTranReqExtVo.getReqId());
                    }
                }
                //更新交易表状态
                Integer[] oldStatus = {CommonConstant.STATUE_CHECK};
                if (CollectionUtils.isNotEmpty(fexTranReqIds)) {
                    this.updateFexTranReqStatus(fexTranReqIds, ForeignConstant.STATUE_TOBE_RATE_CONFIRM, oldStatus);
                }
                if (CollectionUtils.isNotEmpty(fexTranReqIds2)) {
                    this.updateFexTranReqStatus(fexTranReqIds2, ForeignConstant.ONGOING, oldStatus);
                }

                String adminUser = schedulerUser;
                if(StringUtils.isEmpty(adminUser)){
                    adminUser = "scheduler";
                }
                //默认管理员执行
                UserDTO userDTO = this.userService.getByLoginName(adminUser);
                for (String reqId : excSuccessIds) {
                    // 即期自动交割 && 线上自动交割
                    FexTranReqVO fexTranReqVO = this.getByIdUnion(reqId);
                    if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReqVO.getFexTranType()) &&
                            ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReqVO.getTranRateType()) &&
                            CommonConstant.NO.equals(fexTranReqVO.getIsPayed())) {
                        fexTranLifeCycleService.autoSpotDelivery(fexTranReqVO, userDTO, null);
                    }
                }
                long end = System.currentTimeMillis();
                //记录耗时
                logger.info("指令成功后更新交易状态任务结束，耗时：" + (end - start) + "ms");
            }
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取默认损益币种
     */
    public void getDefProfitLossCur(FexTranReqVO fexTranReq){

        // 扩展信息
        FexTranReqExtVo fexTranReqExtVo = fexTranReqExtService.getByReqId(fexTranReq.getId());
        if(EmptyUtils.isEmpty(fexTranReqExtVo)) {
            return;
        }

        FexTranReqUtil.setFexTranReqExtData(fexTranReq, fexTranReqExtVo);

        // 设置货币对
        Map<String, BtCurrencyPairDTO> currencyPairMap = currencyPairService.getCurrencyPairMap();

        BtCurrencyPairDTO btCurrencyPairDTO = currencyPairMap.get(fexTranReqExtVo.getTranCurPairId());
        if (!EmptyUtils.isEmpty(btCurrencyPairDTO)) {
            fexTranReq.setTranCurPairIdName(btCurrencyPairDTO.getPairCode());
        }


        String fefaultProfitLossCur = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFPROFITANDLOSSCUR).getValue();
        if ("1".equals(fefaultProfitLossCur) && StringUtils.isEmpty(fexTranReq.getProfitLossCur())) { // 标准币种
            fexTranReq.setProfitLossCur(fexTranReq.getTranCurPairBaseId());
        } else if ("2".equals(fefaultProfitLossCur) && StringUtils.isEmpty(fexTranReq.getProfitLossCur())) { // 报价币种
            fexTranReq.setProfitLossCur(fexTranReq.getTranCurPairReferId());
        }

    }

    /**
     *
     * 获取未结束交易单
     *
     * @param templateIds
     * @return
     */
    public List<String> getUnFinishedBillNos(List<String> templateIds) {
        return fexTranReqDao.getUnFinishedBillNos(templateIds);
    }

    /**
     *
     * 必填项校验
     *
     * @param fexTranReq
     */
    private void checkRequired(FexTranReqVO fexTranReq){
        if (!StringUtils.equals(fexTranReq.getTranType(), ForeignConstant.TRAN_TYPE_4)) {
            return;
        }
        if(StringUtils.isEmpty(fexTranReq.getTemplateId())){
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TEMPLATE_NULL, fexTranReq.getBillNo());
        }
        if(StringUtils.isNotEmpty(fexTranReq.getTemplateId()) && StringUtils.isEmpty(fexTranReq.getVersionNo())){
            fexTranReq.setVersionNo(fexTranReqConfigService.getMaxVersionNo(fexTranReq.getTemplateId()));
        }

        List<FexTranReqConfigVo> requiredFieldList = fexTranReqConfigService.getRequiredFields(fexTranReq.getTemplateId(), fexTranReq.getVersionNo());
        if (CollectionUtil.isEmpty(requiredFieldList)){
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_FORM_UN_FILL, fexTranReq.getBillNo());
        }
        List<FexTranCustInfoVO> fexTranCustInfoVOS = fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId());
        Map<String, FexTranCustInfoVO> custInfoMap = Collections.emptyMap();
        if(CollectionUtil.isNotEmpty(fexTranCustInfoVOS)){
            custInfoMap = fexTranCustInfoVOS.stream()
                    .collect(Collectors.toMap(FexTranCustInfoVO::getFieldCode, vo->vo, (o,n)->n));
        }
        for (FexTranReqConfigVo configVo : requiredFieldList) {
            // 获取配置的【字段编码】
            String fieldCode = configVo.getFieldCode();
            if (StringUtils.isEmpty(fieldCode) || FexTranReqUtil.UN_REQUIRED_SET.contains(fieldCode)) {
                continue;
            }
            try {
                // 前端和后端实体类字段映射
                String fieldMapCode = FexTranReqUtil.FIELD_MAP.get(fieldCode);
                if (StringUtils.isNotEmpty(fieldMapCode)){
                    fieldCode = fieldMapCode;
                }
                Object fieldValue = ReflectUtil.getFieldValue(fexTranReq, fieldCode);
                if("otherInfo".equalsIgnoreCase(configVo.getFieldArea())){
                    FexTranCustInfoVO fexTranCustInfoVO = custInfoMap.get(fieldCode);
                    if(null != fexTranCustInfoVO){
                        fieldValue = fexTranCustInfoVO.getFieldVal();
                    }
                }
                if (ObjectUtils.isEmpty(fieldValue)) {
                    throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_FORM_UN_FILL, fexTranReq.getBillNo()+"-" + configVo.getFieldName());
                }
            } catch (ParameterException parameterException) {
                logger.info("FEX_TRAN_MESSAGE_FORM_UN_FILL BillNo= " + fexTranReq.getBillNo() + ";fieldCode=" + fieldCode);
                throw parameterException;
            }catch (Exception e) {
                logger.info("FEX_TRAN_MESSAGE_FORM_UN_FILL BillNo= " + fexTranReq.getBillNo() + ";fieldCode=" + fieldCode);
            }
        }
    }

    /**
     *
     * 设置省市信息
     *
     * @param fexTranReq
     */
    private void setRegion(FexTranReqVO fexTranReq){
        List<String> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(fexTranReq.getCotpCity())){
            ids.add(fexTranReq.getCotpCity());
        }
        if (StringUtils.isNotEmpty(fexTranReq.getCotpProv())){
            ids.add(fexTranReq.getCotpProv());
        }
        if(CollectionUtil.isEmpty(ids)){
            return;
        }
        Map<String, RegionDTO> regionMap = regionService.findRegionMapByIds(ids);
        if(CollectionUtil.isNotEmpty(regionMap)){
            RegionDTO cotpCity = regionMap.get(fexTranReq.getCotpCity());
            RegionDTO cotpProv = regionMap.get(fexTranReq.getCotpProv());
            if(null != cotpCity){
                fexTranReq.setCotpCityName(cotpCity.getName());
            }
            if(null != cotpProv){
                fexTranReq.setCotpProvName(cotpProv.getName());
            }
        }
    }

    /**
     * 注入单个交易申请VO展示字段。
     *
     * @param fexTranReqVO 交易申请VO
     */
    public void injectValue(FexTranReqVO fexTranReqVO){
        if(EmptyUtils.isEmpty(fexTranReqVO)){
            return;
        }
        this.injectValue(Arrays.asList(fexTranReqVO));
    }

    /**
     * 批量注入交易申请VO展示字段。
     * <p>
     * 先批量加载币种、单位、客商、交易对手账户、字典、交易编码和银行类型，再逐条回填名称/编码，
     * 避免列表场景循环调用远程服务或数据库查询。
     * </p>
     *
     * @param fexTranReqVOList 交易申请VO列表
     */
    public void injectValue(List<FexTranReqVO> fexTranReqVOList){
        if(EmptyUtils.isEmptyList(fexTranReqVOList)){
            return;
        }
        List<FexTranReqVO> list = fexTranReqVOList.stream()
                .filter(fexTranReqVO -> !EmptyUtils.isEmpty(fexTranReqVO))
                .collect(Collectors.toList());
        if(EmptyUtils.isEmptyList(list)){
            return;
        }

        Set<String> corpIds = new HashSet<>();
        Set<String> bankTypeIds = new HashSet<>();
        Set<String> tranCodes = new HashSet<>();
        Set<String> externalCorpIds = new HashSet<>();
        Set<String> externalCorpBankAccIds = new HashSet<>();
        Set<String> regionIds = new HashSet<>();
        for(FexTranReqVO fexTranReqVO : list){
            addIfNotEmpty(corpIds, fexTranReqVO.getPayCorpId());
            addIfNotEmpty(corpIds, fexTranReqVO.getCorpId());
            addIfNotEmpty(corpIds, fexTranReqVO.getTargetCorp());
            addIfNotEmpty(corpIds, fexTranReqVO.getRptGateEntId());
            addIfNotEmpty(bankTypeIds, fexTranReqVO.getCorpBank());
            addIfNotEmpty(bankTypeIds, fexTranReqVO.getSrcBank());
            addIfNotEmpty(bankTypeIds, fexTranReqVO.getTargetBank());
            addIfNotEmpty(tranCodes, fexTranReqVO.getTranCode1());
            addIfNotEmpty(tranCodes, fexTranReqVO.getTranCode2());
            addIfNotEmpty(externalCorpIds, fexTranReqVO.getCotpId());
            addIfNotEmpty(externalCorpIds, fexTranReqVO.getOppId());
            addIfNotEmpty(externalCorpIds, fexTranReqVO.getCotpAgent());
            addIfNotEmpty(externalCorpIds, fexTranReqVO.getCotpClearHouse());
            addIfNotEmpty(externalCorpBankAccIds, fexTranReqVO.getCotpAccId());
            addIfNotEmpty(regionIds, fexTranReqVO.getCotpProv());
            addIfNotEmpty(regionIds, fexTranReqVO.getCotpCity());
        }
        Map<String, ExternalCorpDTO> externalCorpMap = getExternalCorpMap(externalCorpIds);
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccMap = getExternalCorpBankAccMap(externalCorpBankAccIds);
        Map<String, RegionDTO> regionMap = getRegionMap(regionIds);
        // 收集批量查询所需的基础资料ID。
        Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrencyMap();
        // 单位
        Map<String, CorpDTO> corpMap = CollectionUtil.isEmpty(corpIds) ? new HashMap<>() : corpService.listCorpDtoByCorpIds(new ArrayList<>(corpIds));
        if(corpMap == null){
            corpMap = new HashMap<>();
        }
        // 字典
        Map<String, DictDTO> cdSignDictMap = dictService.listMap("nisBusType.cdSign");
        if(cdSignDictMap == null){
            cdSignDictMap = new HashMap<>();
        }
        // 落地限额处理结果文案：批量取一次，供前后端分离接口直接展示 landingLimitResultName
        Map<String, DictDTO> landingResultDictMap = dictService.listMap(EnterpriseLimitControlService.LANDING_RESULT_DICT);
        if(landingResultDictMap == null){
            landingResultDictMap = new HashMap<>();
        }
        // 交易编码
        Map<String, FexTranInfo> tranInfoMap = getTranInfoMap(tranCodes);
        if(tranInfoMap == null){
            tranInfoMap = new HashMap<>();
        }
        // 银行类型
        Map<String, BankTypeDTO> bankTypeDTOMap = CollectionUtil.isEmpty(bankTypeIds) ? new HashMap<>() : iBankTypeService.listBankTypeMapByIds(new ArrayList<>(bankTypeIds));
        if(bankTypeDTOMap == null){
            bankTypeDTOMap = new HashMap<>();
        }
        // 使用已加载的基础资料逐条回填展示字段。
        for(FexTranReqVO fexTranReqVO : list){
            injectExternalCorpValue(fexTranReqVO, externalCorpMap, externalCorpBankAccMap);
            injectCurrencyValue(fexTranReqVO, currencyMap);
            injectCorpValue(fexTranReqVO, corpMap);
            injectDictValue(fexTranReqVO, cdSignDictMap);
            injectLandingLimitResultValue(fexTranReqVO, landingResultDictMap);
            injectTranInfoValue(fexTranReqVO, tranInfoMap);
            injectBankTypeValue(fexTranReqVO, bankTypeDTOMap);
            injectCotpRegionValue(fexTranReqVO, regionMap);
        }
    }

    /**
     * 注入币种名称和英文编码。
     */
    private void injectCurrencyValue(FexTranReqVO fexTranReqVO, Map<String, BtCurrencyDTO> currencyMap){
        BtCurrencyDTO btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getDeliveryCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setDeliveryCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setDeliveryEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getBuyCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setBuyCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setBuyEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getSellCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setSellCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setSellEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getBondCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setBondCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setBondEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getCotpAccCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setCotpAccCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setCotpAccEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getCorpAccCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setCorpAccCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setCorpAccEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getTranCurId1());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setTranCurName1(btCurrencyDTO.getCurName());
            fexTranReqVO.setTranEnglishCode1(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getTranCurId2());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setTranCurName2(btCurrencyDTO.getCurName());
            fexTranReqVO.setTranEnglishCode2(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getTargetAccCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setTargetAccCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setTargetAccEnglishCode(btCurrencyDTO.getEnglishCode());
        }
        btCurrencyDTO = getCurrency(currencyMap, fexTranReqVO.getPremiumCur());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            fexTranReqVO.setPremiumCurName(btCurrencyDTO.getCurName());
            fexTranReqVO.setPremiumEnglishCode(btCurrencyDTO.getEnglishCode());
        }
    }

    /**
     * 注入单位名称及主体英文信息。
     */
    private void injectCorpValue(FexTranReqVO fexTranReqVO, Map<String, CorpDTO> corpMap){
        CorpDTO corpDTO = getCorp(corpMap, fexTranReqVO.getPayCorpId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            fexTranReqVO.setPayCorpName(corpDTO.getName());
        }
        corpDTO = getCorp(corpMap, fexTranReqVO.getCorpId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            fexTranReqVO.setCorpName(corpDTO.getName());
            // 用户自己未维护时，默认取主体单位已维护的单位英文名称和单位地址英文名称
            if (EmptyUtils.isEmpty(fexTranReqVO.getCorpNameEn())) {
                fexTranReqVO.setCorpNameEn(corpDTO.getNameEn());
            }
            if (EmptyUtils.isEmpty(fexTranReqVO.getCorpAddrEn())) {
                fexTranReqVO.setCorpAddrEn(corpDTO.getAddressEn());
            }
        }
        corpDTO = getCorp(corpMap, fexTranReqVO.getTargetCorp());
        if(!EmptyUtils.isEmpty(corpDTO)){
            fexTranReqVO.setTargetCorpName(corpDTO.getName());
        }
        corpDTO = getCorp(corpMap, fexTranReqVO.getRptGateEntId());
        if(!EmptyUtils.isEmpty(corpDTO)){
            fexTranReqVO.setRptGateEntName(corpDTO.getName());
        }
    }

    /**
     * 注入收付标识字典名称。
     */
    private void injectDictValue(FexTranReqVO fexTranReqVO, Map<String, DictDTO> cdSignDictMap){
        if(StringUtils.isEmpty(fexTranReqVO.getCdSign())){
            return;
        }
        DictDTO dictDTO = cdSignDictMap.get(fexTranReqVO.getCdSign());
        if(!EmptyUtils.isEmpty(dictDTO)){
            fexTranReqVO.setCdSignName(dictDTO.getName());
        }
    }

    /**
     * 注入落地限额处理结果文案。
     * <p>落地标识取自关联现金流（0 等待银行限额审批 / A 同意 / R 拒绝），未涉及落地为 null，
     * 此时字典取不到、名称留空。</p>
     */
    private void injectLandingLimitResultValue(FexTranReqVO fexTranReqVO, Map<String, DictDTO> landingResultDictMap){
        if(StringUtils.isEmpty(fexTranReqVO.getLandingLimitResult())){
            return;
        }
        DictDTO dictDTO = landingResultDictMap.get(fexTranReqVO.getLandingLimitResult());
        if(!EmptyUtils.isEmpty(dictDTO)){
            fexTranReqVO.setLandingLimitResultName(dictDTO.getName());
        }
    }

    /**
     * 注入交易编码名称。
     */
    private void injectTranInfoValue(FexTranReqVO fexTranReqVO, Map<String, FexTranInfo> tranInfoMap){
        FexTranInfo fexTranInfo = getTranInfo(tranInfoMap, fexTranReqVO.getTranCode1());
        if(!EmptyUtils.isEmpty(fexTranInfo)){
            fexTranReqVO.setTranName1(fexTranInfo.getTranName());
        }
        fexTranInfo = getTranInfo(tranInfoMap, fexTranReqVO.getTranCode2());
        if(!EmptyUtils.isEmpty(fexTranInfo)){
            fexTranReqVO.setTranName2(fexTranInfo.getTranName());
        }
    }

    /**
     * 注入银行类型名称。
     */
    private void injectBankTypeValue(FexTranReqVO fexTranReqVO, Map<String, BankTypeDTO> bankTypeDTOMap){
        // 银行类型
        BankTypeDTO bankTypeDTO = getBankType(bankTypeDTOMap, fexTranReqVO.getCorpBank());
        if(!EmptyUtils.isEmpty(bankTypeDTO)){
            fexTranReqVO.setCorpBankName(bankTypeDTO.getTypeName());
        }
        bankTypeDTO = getBankType(bankTypeDTOMap, fexTranReqVO.getSrcBank());
        if(!EmptyUtils.isEmpty(bankTypeDTO)){
            fexTranReqVO.setSrcBankName(bankTypeDTO.getTypeName());
        }
        bankTypeDTO = getBankType(bankTypeDTOMap, fexTranReqVO.getTargetBank());
        if(!EmptyUtils.isEmpty(bankTypeDTO)){
            fexTranReqVO.setTargetBankName(bankTypeDTO.getTypeName());
        }
    }

    /**
     * 注入客商和客商账户展示字段。
     */
    private void injectExternalCorpValue(FexTranReqVO fexTranReqVO, Map<String, ExternalCorpDTO> externalCorpMap,
                                         Map<String, ExternalCorpBankAccDTO> externalCorpBankAccMap){
        ExternalCorpDTO externalCorp = getExternalCorp(externalCorpMap, getExternalCorpId(fexTranReqVO));
        if(externalCorp != null){
            // cotpId 以交易表查询结果为准，客商服务只负责回填名称等展示信息。
            if(StringUtils.isBlank(fexTranReqVO.getOppId())){
                fexTranReqVO.setOppId(fexTranReqVO.getCotpId());
            }
            fexTranReqVO.setCotpName(externalCorp.getName());
            if(StringUtils.isNotBlank(externalCorp.getNameEn())){
                fexTranReqVO.setCotpEnName(externalCorp.getNameEn());
            }
            if(StringUtils.isNotBlank(externalCorp.getAddress())){
                fexTranReqVO.setCotpAdrr(externalCorp.getAddress());
            }
            if(StringUtils.isNotBlank(externalCorp.getAddressEn())){
                fexTranReqVO.setCotpAddressNameEn(externalCorp.getAddressEn());
            }
        }
        ExternalCorpBankAccDTO bankAccDTO = getExternalCorpBankAcc(externalCorpBankAccMap, fexTranReqVO.getCotpAccId());
        if(bankAccDTO != null){
            if(StringUtils.isNotBlank(bankAccDTO.getExternalAcc())){
                fexTranReqVO.setCotpBankAcc(bankAccDTO.getExternalAcc());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getExternalAccName())){
                fexTranReqVO.setCotpAccName(bankAccDTO.getExternalAccName());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getBank())){
                fexTranReqVO.setCotpBankName(bankAccDTO.getBank());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getSwiftCode())){
                fexTranReqVO.setCotpSwiftCode(bankAccDTO.getSwiftCode());
                fexTranReqVO.setCotpBicCode(bankAccDTO.getSwiftCode());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getLocalClearNo())){
                fexTranReqVO.setLocalClearNo(bankAccDTO.getLocalClearNo());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getIntermediaryBankAddress())){
                fexTranReqVO.setCotpAgentBankAddr(bankAccDTO.getIntermediaryBankAddress());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getIntermediaryBank())){
                fexTranReqVO.setCotpAgentBank(bankAccDTO.getIntermediaryBank());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getIntermediaryBankAcc())){
                fexTranReqVO.setCotpAgentBankAcc(bankAccDTO.getIntermediaryBankAcc());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getIntermediaryBankSwiftCode())){
                fexTranReqVO.setCotpAgentSwiftCode(bankAccDTO.getIntermediaryBankSwiftCode());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getCurId())){
                fexTranReqVO.setCotpAccCurId(bankAccDTO.getCurId());
            }
            if(StringUtils.isNotBlank(bankAccDTO.getPayExcType())){
                fexTranReqVO.setBankPayType(bankAccDTO.getPayExcType());
            }
        }else if(!ForeignConstant.TRAN_TYPE_4.equals(fexTranReqVO.getTranType()) && externalCorp != null
                && StringUtils.isBlank(fexTranReqVO.getCotpBankName())){
            fexTranReqVO.setCotpBankName(externalCorp.getName());
        }
        ExternalCorpDTO cotpAgent = getExternalCorp(externalCorpMap, fexTranReqVO.getCotpAgent());
        if(cotpAgent != null){
            fexTranReqVO.setCotpAgentName(cotpAgent.getName());
        }
        ExternalCorpDTO cotpClearHouse = getExternalCorp(externalCorpMap, fexTranReqVO.getCotpClearHouse());
        if(cotpClearHouse != null){
            fexTranReqVO.setCotpClearHouseName(cotpClearHouse.getName());
        }
    }

    /**
     * 注入交易对手省市名称。
     */
    private void injectCotpRegionValue(FexTranReqVO fexTranReqVO, Map<String, RegionDTO> regionMap){
        RegionDTO cotpProv = getRegion(regionMap, fexTranReqVO.getCotpProv());
        if(cotpProv != null){
            fexTranReqVO.setCotpProvName(cotpProv.getName());
        }
        RegionDTO cotpCity = getRegion(regionMap, fexTranReqVO.getCotpCity());
        if(cotpCity != null){
            fexTranReqVO.setCotpCityName(cotpCity.getName());
            if(StringUtils.isBlank(fexTranReqVO.getCotpAddressName())){
                fexTranReqVO.setCotpAddressName(cotpCity.getName());
            }
        }
    }

    /**
     * 非空ID加入集合。
     */
    private void addIfNotEmpty(Set<String> ids, String id){
        if(StringUtils.isNotEmpty(id)){
            ids.add(id);
        }
    }

    /**
     * 从币种缓存中安全获取币种信息。
     */
    private BtCurrencyDTO getCurrency(Map<String, BtCurrencyDTO> currencyMap, String curId){
        if(currencyMap == null || currencyMap.isEmpty() || StringUtils.isEmpty(curId)){
            return null;
        }
        return currencyMap.get(curId);
    }

    /**
     * 从单位缓存中安全获取单位信息。
     */
    private CorpDTO getCorp(Map<String, CorpDTO> corpMap, String corpId){
        if(corpMap == null || corpMap.isEmpty() || StringUtils.isEmpty(corpId)){
            return null;
        }
        return corpMap.get(corpId);
    }

    /**
     * 从交易编码缓存中安全获取交易编码信息。
     */
    private FexTranInfo getTranInfo(Map<String, FexTranInfo> tranInfoMap, String tranCode){
        if(tranInfoMap == null || tranInfoMap.isEmpty() || StringUtils.isEmpty(tranCode)){
            return null;
        }
        return tranInfoMap.get(tranCode);
    }

    /**
     * 从银行类型缓存中安全获取银行类型信息。
     */
    private BankTypeDTO getBankType(Map<String, BankTypeDTO> bankTypeDTOMap, String bankTypeId){
        if(bankTypeDTOMap == null || bankTypeDTOMap.isEmpty() || StringUtils.isEmpty(bankTypeId)){
            return null;
        }
        return bankTypeDTOMap.get(bankTypeId);
    }

    /**
     * 批量加载客商信息。
     */
    private Map<String, ExternalCorpDTO> getExternalCorpMap(Set<String> externalCorpIds){
        Map<String, ExternalCorpDTO> externalCorpMap = new HashMap<>();
        if(CollectionUtil.isEmpty(externalCorpIds) || externalCorpService == null){
            return externalCorpMap;
        }
        Map<String, ExternalCorpDTO> batchMap = externalCorpService.queryFexExternalCorpMapByIds(new ArrayList<>(externalCorpIds));
        if(batchMap != null){
            externalCorpMap.putAll(batchMap);
        }
        for(String externalCorpId : externalCorpIds){
            if(externalCorpMap.containsKey(externalCorpId)){
                continue;
            }
            ExternalCorpDTO externalCorp = externalCorpService.getById(externalCorpId);
            if(externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())){
                externalCorpMap.put(externalCorp.getId(), externalCorp);
            }
        }
        return externalCorpMap;
    }

    /**
     * 批量加载客商账户信息。
     */
    private Map<String, ExternalCorpBankAccDTO> getExternalCorpBankAccMap(Set<String> externalCorpBankAccIds){
        Map<String, ExternalCorpBankAccDTO> externalCorpBankAccMap = new HashMap<>();
        if(CollectionUtil.isEmpty(externalCorpBankAccIds) || externalCorpService == null){
            return externalCorpBankAccMap;
        }
        Map<String, ExternalCorpBankAccDTO> batchMap = externalCorpService.queryFexExternalCorpBankAccMapByIds(new ArrayList<>(externalCorpBankAccIds));
        if(batchMap != null){
            externalCorpBankAccMap.putAll(batchMap);
        }
        return externalCorpBankAccMap;
    }

    /**
     * 批量加载区域信息。
     */
    private Map<String, RegionDTO> getRegionMap(Set<String> regionIds){
        if(CollectionUtil.isEmpty(regionIds) || regionService == null){
            return new HashMap<>();
        }
        Map<String, RegionDTO> regionMap = regionService.findRegionMapByIds(new ArrayList<>(regionIds));
        return regionMap == null ? new HashMap<>() : regionMap;
    }

    /**
     * 获取当前交易对手对应的客商ID。
     */
    private String getExternalCorpId(FexTranReqVO fexTranReqVO){
        if(fexTranReqVO == null){
            return null;
        }
        if(StringUtils.isNotBlank(fexTranReqVO.getOppId())){
            return fexTranReqVO.getOppId();
        }
        return fexTranReqVO.getCotpId();
    }

    /**
     * 从客商缓存中安全获取客商信息。
     */
    private ExternalCorpDTO getExternalCorp(Map<String, ExternalCorpDTO> externalCorpMap, String externalCorpId){
        if(externalCorpMap == null || externalCorpMap.isEmpty() || StringUtils.isBlank(externalCorpId)){
            return null;
        }
        return externalCorpMap.get(externalCorpId);
    }

    /**
     * 从客商账户缓存中安全获取账户信息。
     */
    private ExternalCorpBankAccDTO getExternalCorpBankAcc(Map<String, ExternalCorpBankAccDTO> externalCorpBankAccMap, String externalCorpBankAccId){
        if(externalCorpBankAccMap == null || externalCorpBankAccMap.isEmpty() || StringUtils.isBlank(externalCorpBankAccId)){
            return null;
        }
        return externalCorpBankAccMap.get(externalCorpBankAccId);
    }

    /**
     * 从区域缓存中安全获取区域信息。
     */
    private RegionDTO getRegion(Map<String, RegionDTO> regionMap, String regionId){
        if(regionMap == null || regionMap.isEmpty() || StringUtils.isBlank(regionId)){
            return null;
        }
        return regionMap.get(regionId);
    }

    /**
     * 将交易对手名称筛选转换为客商ID筛选。
     */
    private void appendExternalCorpNameFilters(DataTablesInput dataTablesInput, StringBuilder sql){
        if(dataTablesInput == null || sql == null){
            return;
        }
        disableExternalCorpNameOrder(dataTablesInput);
        if(EmptyUtils.isEmpty(dataTablesInput.getFormdata())){
            return;
        }
        appendExternalCorpNameFilter(dataTablesInput.getFormdata(), sql, "likes_cotpName");
        appendExternalCorpNameFilter(dataTablesInput.getFormdata(), sql, "eqs_cotpName");
    }

    /**
     * 交易对手名称由客商服务回填，不能直接参与 payment 本地 SQL 排序。
     */
    private void disableExternalCorpNameOrder(DataTablesInput dataTablesInput){
        if(dataTablesInput.getColumns() == null){
            return;
        }
        for(Column column : dataTablesInput.getColumns()){
            if(column != null && ("cotpName".equals(column.getName()) || "cotpName".equals(column.getData()))){
                column.setOrderable(false);
            }
        }
    }

    private void appendExternalCorpNameFilter(Map<String, Object> params, StringBuilder sql, String field){
        List<String> externalCorpIds = resolveExternalCorpIdsByName(params, field);
        if(externalCorpIds == null){
            return;
        }
        if(EmptyUtils.isEmpty(externalCorpIds)){
            sql.append(" and 1=0");
            return;
        }
        sql.append(" and ").append(SqlUtil.getInSql(externalCorpIds, "t.cotp_Id", true));
    }

    private List<String> resolveExternalCorpIdsByName(Map<String, Object> params, String field){
        if(params == null || StringUtils.isBlank(field)){
            return null;
        }
        Object name = params.remove(field);
        if(name == null || StringUtils.isBlank(name.toString())){
            return null;
        }
        if(externalCorpService == null){
            return new ArrayList<>();
        }
        List<ExternalCorpDTO> externalCorps = externalCorpService.findExternalCorpLikeName(name.toString());
        List<String> externalCorpIds = new ArrayList<>();
        if(EmptyUtils.isEmpty(externalCorps)){
            return externalCorpIds;
        }
        for(ExternalCorpDTO externalCorp : externalCorps){
            if(externalCorp != null && StringUtils.isNotBlank(externalCorp.getId())
                    && !externalCorpIds.contains(externalCorp.getId())){
                externalCorpIds.add(externalCorp.getId());
            }
        }
        return externalCorpIds;
    }

    /**
     * 批量回填交易申请列表展示字段。
     */
    private void injectOutputValue(DataTablesOutput<FexTranReqVO> output){
        if(output == null || EmptyUtils.isEmpty(output.getData())){
            return;
        }
        injectValue(output.getData());
    }

    /**
     * 加载当前批次涉及的交易编码信息。
     */
    private Map<String, FexTranInfo> getTranInfoMap(Set<String> tranCodes){
        Map<String, FexTranInfo> tranInfoMap = new HashMap<>();
        if(CollectionUtil.isEmpty(tranCodes)){
            return tranInfoMap;
        }
        List<FexTranInfo> fexTranInfos = fexTranInfoService.listOrderByTranCode();
        if(EmptyUtils.isEmptyList(fexTranInfos)){
            return tranInfoMap;
        }
        for(FexTranInfo fexTranInfo : fexTranInfos){
            if(!EmptyUtils.isEmpty(fexTranInfo) && tranCodes.contains(fexTranInfo.getTranCode())){
                tranInfoMap.put(fexTranInfo.getTranCode(), fexTranInfo);
            }
        }
        return tranInfoMap;
    }
}
