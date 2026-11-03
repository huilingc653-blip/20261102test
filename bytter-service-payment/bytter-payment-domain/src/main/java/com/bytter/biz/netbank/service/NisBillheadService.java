package com.bytter.biz.netbank.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.*;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAbVoucherinfoViceService;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.billPool.dto.AbPoolEmitFinaApplyDTO;
import com.bytter.bill.api.billPool.service.IAccebillPoolService;
import com.bytter.bill.api.scf.dto.ScfPayBindDTO;
import com.bytter.bill.api.scf.dto.ScfPayGenerateDTO;
import com.bytter.bill.api.scf.service.IScfVoucherPayService;
import com.bytter.bill.api.scf.service.IScfVoucherReceivableService;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.dto.BisExcChangeMsg;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.fundwarn.spi.constant.FundWarnConstant;
import com.bytter.biz.fundwarn.spi.service.IFundWarnService;
import com.bytter.biz.limit.enums.EnterpriseLimitMessageEnum;
import com.bytter.biz.limit.service.EnterpriseLimitControlService;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitContextDto;
import com.bytter.biz.mq.message.service.ProducerMessageRecordService;
import com.bytter.biz.netbank.dao.BtAssignPayWayUserDao;
import com.bytter.biz.netbank.dao.NisBillDao;
import com.bytter.biz.netbank.dao.NisBillheadDao;
import com.bytter.biz.netbank.entity.BisPayPolicySort;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.entity.NisScfInfo;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.enums.BusinessType;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IErpPayInfoService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.IWorkFlowSubmitVerifyService;
import com.bytter.core.common.WorkFlowVerifyException;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.finance.api.IInlSendService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitContextDto;
import com.bytter.saas.management.exchange.api.dto.PaymentLimitResultDto;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.api.IWorkFlowTaskCompletedListener;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 付款通知表
 * @date 2017-05-23 15:20:33
 */
@WorkFlow(NisWorkFlowConstant.NIS_BILLHEAD)
@Service("nisBillheadService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.NIS_BILLHEAD)
@Transactional
public class NisBillheadService extends BaseService<NisBillhead, String> implements IWorkFlowListener, IExcelVerifyHandler<NisBillheadVO>, IWorkFlowRevokeListener, IWorkFlowSubmitVerifyService, ICorpDisableVerifyService, IWorkFlowTaskCompletedListener {
    @Autowired
    private NisBillheadDao nisBillheadDao;
    @Autowired
    private EnterpriseLimitControlService enterpriseLimitControlService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false, timeout = 120000)
    private IAccebillService iAccebillService;
    @DubboReference(check = false)
    private IAccebillPoolService accebillPoolService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @Autowired
    private NisAccbookService nisAccbookService;
    @Autowired
    private NisScfInfoService nisScfInfoService;
    @DubboReference(check = false)
    private IScfVoucherPayService scfVoucherPayService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Autowired
    private BtAssignPayWayService btAssignPayWayService;
    @DubboReference(check = false)
    private IInputBankInfoService btBankService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private ICAService caService;
    @DubboReference(check = false)
    private IAttachmentService attachmentService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private NisBillService nisBillService;
    @Autowired
    private NisBillDao nisBillDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @DubboReference(check = false)
    private IFbsItemService fbsItemService;
    @DubboReference(check = false)
    private IFbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
    private IFundWarnService fundWarnService;
    @DubboReference(check = false)
    private IErpPayInfoService erpPayInfoService;
    @DubboReference(check = false)
    private IBtBankService bankService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @Autowired(required = false)
    private BtAssignPayWayUserDao btAssignPayWayUserDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IInlSendService inlSendService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @Autowired
    private NisBillheadPoolService nisBillheadPoolService;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IRegionService regionService;
    @DubboReference(check = false)
    IBtCurrencyService iBtCurrencyService;
    @Autowired
    NisDemandService nisDemandService;
    @DubboReference(check = false)
    IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @Autowired
    private IBisExcBackService billheadBisExcBackService;

    @Autowired
    private ProducerMessageRecordService producerMessageRecordService;

    @DubboReference(check = false)
    private IAbVoucherinfoViceService abVoucherinfoViceService;


    @Value("${local.massage.billendorsement}")
    private String localMassageBillendorsement;
    @Autowired
    private BisBifRuleService bisBifRuleService;

    @DubboReference(check = false)
    private IScfVoucherReceivableService iScfVoucherReceivableService;

    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(NisBillhead entity) {
        ICurrentUser user = getCurrentUser();
        // 物理删除时仅外付网银付款单可能占用支付限额，其他结算方式不调释放。
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(entity, user));
        super.deleteEntity(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        ICurrentUser user = getCurrentUser();
        NisBillhead entity = this.get(id, true);
        // 按ID删除也先判断付款方式，避免普通付款单发起无效释放请求。
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(entity, user));
        super.delete(id);
    }

    /**
     * 保存
     *
     * @param entity
     */
    public void save(NisBillhead entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(NisBillhead entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(NisBillhead entity) {
        super.saveOrUpdate(entity);
        getSession().flush();
    }

    @Override
    public BaseDao<NisBillhead, String> getEntityDao() {
        return nisBillheadDao;
    }

    /**
     * 根据用户获取有转账权限的账号集合
     *
     * @param user
     * @return
     */
    public HashMap<String, BtBankAccDTO> getAccountGrantMap(ICurrentUser user) {
        HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
        //获取银行账号
        List<String> bankAccIdList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY, null,
                AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<BtBankAccDTO> bankAccGrantList = bankAccService.getBankAccNotUnionByIdList(bankAccIdList);
        if (EmptyUtils.isNotEmpty(bankAccGrantList)) {
            for (BtBankAccDTO bankAcc : bankAccGrantList) {
                bankAccMap.put(bankAcc.getBankAcc(), bankAcc);
            }
        }
        //获取内部账号
        List<String> accIdList = accountGrantService.listByMultiValAccIds(user.getId(), ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY, null,
                AccountGrantConstant.INNER_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);
        List<FcAccDTO> accGrantList = innerAccountService.listFcAccInfo(accIdList);
        if (EmptyUtils.isNotEmpty(accGrantList)) {
            for (FcAccDTO fcAcc : accGrantList) {
                BtBankAccDTO bankAcc = new BtBankAccDTO();
                bankAcc.setId(fcAcc.getId());
                bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                bankAcc.setAccName(fcAcc.getAccName());
                bankAcc.setCurId(fcAcc.getCurId());
                bankAcc.setCurName(fcAcc.getCurName());
                bankAcc.setBankName(fcAcc.getNetName());
                bankAcc.setBalance(fcAcc.getFcBalance());
                bankAcc.setCorpId(fcAcc.getCorpId());
                bankAcc.setCorpName(fcAcc.getCorpName());
                bankAccMap.put(fcAcc.getBlnctrAcc(), bankAcc);
            }
        }
        return bankAccMap;
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisBillheadVO> getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        List<String> bursarBillNoList = new ArrayList<>();
        //如果会计凭证号是从excel复制出来的，格式是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                sql.append(" and ").append(SqlUtil.getInSql(bursarBillNoList, "n.bursar_Bill_No", true));
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
            }
        }
        String resource = "";
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        //是否启用用户分配付款方式
        String isEnableUserAssign = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEUSERASSIGN).getValue();
        String paySql = "";
        //是否启用拆分合并
        List<String> voucherStats = null;
        String voucherStat = EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("ins_voucherStat")) ? null : String.valueOf(dataTablesInput.getFormdata().get("ins_voucherStat"));
        if (StringUtils.isNotEmpty(voucherStats)) {
            voucherStats = Arrays.asList(voucherStat.split(","));
        }
        String isMergeAndSplit = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLESPLIT).getValue();
        //如果开启了用户分配付款方式则筛选付款方式
        if (NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
            List<BtAssignPayWayUserVO> temp = btAssignPayWayUserDao.getByUserId(user.getId());
            List<String> payList = new ArrayList<>();
            for (BtAssignPayWayUserVO vo : temp) {
                payList.add(vo.getVoucherCode());
            }
            paySql = SqlUtil.getInSql(payList, "n.voucher_type_id", true);
        }
        //付款办理查询打回和暂存和暂挂的付款数据,以及过滤掉费用报销的汇总单
        if ("list".equals(opType)) {
            resource = ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY;
            sql.append(" and (n.create_by ='").append(user.getLoginName()).append("' or n.create_by is null or n.create_by='')");
            sql.append(" and n.status in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + "," + NetbankConstant.STATUE_PENDING + ") and n.bill_from <>'" + NetbankConstant.BILLFROM_2 + "'");
            //如果开启了用户分配付款方式则筛选付款方式
            if (NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
                sql.append(" and " + paySql);
            }
        } else if ("expenseDeal".equals(opType)) {
            //费用报销处理
            resource = ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_HANDLEQUERY;
            sql.append(" and n.create_by ='").append(user.getLoginName()).append("'");
            sql.append(" and n.status in (" + NetbankConstant.STATUE_TEMP + "," + NetbankConstant.STATUE_BACK + ") and n.bill_from ='" + NetbankConstant.BILLFROM_2 + "'");
        } else if ("listExecute".equals(opType)) {
            //付款执行 先记账后付款参数时付款执行不显示数据
            if (NetbankConstant.ACCOUNTMODE_MANUAL.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
                //先记账后付款
                if (NetbankConstant.ACCOUNTORDER_ACCOUNTPAY.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue())) {
                    return new DataTablesOutput();
                }
                //是否已付款为否的数据
                sql.append(" and (n.is_payed = '0' or n.is_payed is null)");
            }
            resource = ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEQUERY;
            sql.append(" and n.status in (" + NetbankConstant.STATUE_CHECK + ") and (n.voucher_no is null or n.voucher_no='') and (n.order_no is null or n.order_no='') "
                    + "and f.sys_voucher_code in ('" + NetbankConstant.PAY_TYPE_02 + "','" + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_35 + "')");
        } else if ("query".equals(opType) || "queryDel".equals(opType)) {
            //付款查询
            //过滤掉删除状态的付款单
            if ("query".equals(opType)) {
                resource = ResourceConstant.NETBANK_NETBANKQUERY_NISBILLHEAD_QUERY;
                sql.append(" and n.status<>" + NetbankConstant.STATUE_DEL);
            } else {
                resource = ResourceConstant.NETBANK_NETBANKQUERYDEL_NISBILLHEAD_QUERY;
                sql.append(" and n.status = " + NetbankConstant.STATUE_DEL);
            }
            String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
            sql.append(" and ").append(corpAuthSql);
            //如果开启了用户分配付款方式则筛选付款方式
            if (NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
                sql.append(" and " + paySql);
            }
            //记账状态
            String status = (String) dataTablesInput.getFormdata().get("eqi_status");
            if (StringUtils.isNotEmpty(status) && Integer.parseInt(status) >= NetbankConstant.STATUE_CHECK && Integer.parseInt(status) < NetbankConstant.STATUE_TALLY_CHECKPASS) {
                sql.append(" and n.status>=" + NetbankConstant.STATUE_CHECK + " and n.status<" + NetbankConstant.STATUE_TALLY_CHECKPASS);
                if (NetbankConstant.STATUE_TALLY_WAIT_CHECK.toString().equals(status)) {
                    //记账提交待复核
                    dataTablesInput.getFormdata().put("eqs_tallySign", NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
                } else if (NetbankConstant.STATUE_TALLY_BACK.toString().equals(status)) {
                    //记账复核打回
                    dataTablesInput.getFormdata().put("eqs_tallySign", NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
                } else if (NetbankConstant.STATUE_TALLY_TEMP.toString().equals(status)) {
                    //记账暂存
                    dataTablesInput.getFormdata().put("eqs_tallySign", NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
                }
            }
            String tallySign = (String) dataTablesInput.getFormdata().get("eqs_tallySign");
            if (StringUtils.isNotBlank(tallySign)) {
                dataTablesInput.getFormdata().remove("eqi_status");
            }

            cn.hutool.core.date.StopWatch stopWatch = new cn.hutool.core.date.StopWatch();
            stopWatch.start("付款查询耗时:");
            DataTablesOutput dataTablesOutput = nisBillheadDao.getDataTablesQuery(dataTablesInput, sql.toString(), isMergeAndSplit);
            stopWatch.stop();
            stopWatch.start("setJoinField:");
            dataTablesOutput = this.setJoinField(dataTablesOutput, opType, voucherStats);
            stopWatch.stop();
            if (bursarBillNoList.size() >= 2) {
                List<NisBillheadVO> list = dataTablesOutput.getData();
                List<NisBillheadVO> list1 = new ArrayList<>();
                for (String bursarBillNo : bursarBillNoList) {
                    for (NisBillheadVO nisBillheadVO : list) {
                        if (bursarBillNo.equals(nisBillheadVO.getBursarBillNo())) {
                            list1.add(nisBillheadVO);
                        }
                    }
                }
                dataTablesOutput.setData(list1);
            }
            stopWatch.start("setTallyStatus:");
            dataTablesOutput = this.setTallyStatus(dataTablesOutput);
            stopWatch.stop();
            stopWatch.start("setActualPayDate:");
            dataTablesOutput = this.setActualPayDate(dataTablesOutput);
            stopWatch.stop();
            logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
            // T24090014838 付款办理优化：付款办理，付款查询，代发工资审批页面，鼠标光标移动到付款账户列时，显示付款账户可用余额
            this.getAccountAvailBal(dataTablesOutput.getData());
            return dataTablesOutput;
        } else {
            resource = ResourceConstant.NETBANK_NETBANKQUERY_NISBILLHEAD_QUERY;
        }
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput<NisBillheadVO> dataTablesOutput = null;
        if ("listExecute".equals(opType)) {
//        	List<String> cbsBillIds = bisExcService.getBisExcCbsBillIdsByVoucherstat();
            dataTablesOutput = nisBillheadDao.getDataTablesExecuteNew(dataTablesInput, sql.toString(), null);
            List<NisBillheadVO> listNisBillheadVO = dataTablesOutput.getData();
            List<String> downAccIds = listNisBillheadVO.stream().map(NisBillheadVO::getDownAccId).collect(Collectors.toList());
            List<String> curIds = listNisBillheadVO.stream().map(NisBillheadVO::getCurId).collect(Collectors.toList());
            List<BtBankAccDTO> listDto = btBankAccService.listBankAccByNillHead(downAccIds, curIds);
            Map<String, BtBankAccDTO> btBankAccMap = listDto.stream().collect(Collectors.toMap(BtBankAccDTO::getId, Function.identity()));
            if (!CollectionUtils.isEmpty(listNisBillheadVO)) {
                for (NisBillheadVO vo : listNisBillheadVO) {
                    if (btBankAccMap.containsKey(vo.getDownAccId())) {
                        BtBankAccDTO dto = (BtBankAccDTO) btBankAccMap.get(vo.getDownAccId());
                        vo.setDownAcc(StringUtils.isBlank(dto.getDownAcc()) ? "" : dto.getDownAcc());
                        vo.setDownAccName(StringUtils.isBlank(dto.getDownAccName()) ? "" : dto.getDownAccName());
                        vo.setDownAccPrent(StringUtils.isBlank(dto.getDownAccPrent()) ? "" : dto.getDownAccPrent());
                        vo.setDownAccNamePrent(StringUtils.isBlank(dto.getDownAccNamePrent()) ? "" : dto.getDownAccNamePrent());
                    }
                }
                dataTablesOutput.setData(listNisBillheadVO);//重新装载
            }

        } else {
            dataTablesOutput = nisBillheadDao.getDataTablesUnion(dataTablesInput, sql.toString(), isMergeAndSplit);
            if ("list".equals(opType)) {
                dataTablesOutput = this.setAcceBillDataTables(dataTablesOutput);
            }
        }
        dataTablesOutput = this.setJoinField(dataTablesOutput, opType, voucherStats);
        if (bursarBillNoList.size() >= 2) {
            List<NisBillheadVO> list = dataTablesOutput.getData();
            List<NisBillheadVO> list1 = new ArrayList<>();
            for (String bursarBillNo : bursarBillNoList) {
                for (NisBillheadVO nisBillheadVO : list) {
                    if (bursarBillNo.equals(nisBillheadVO.getBursarBillNo())) {
                        list1.add(nisBillheadVO);
                    }
                }
            }
            dataTablesOutput.setData(list1);
        }
        // T24090014838 付款办理优化：付款办理，付款查询，代发工资审批页面，鼠标光标移动到付款账户列时，显示付款账户可用余额
        this.getAccountAvailBal(dataTablesOutput.getData());
        return dataTablesOutput;
    }

    /**
     * 用于 查询符合 资金需求清单 条件的数据
     *
     * @return
     */
    public List<NisBillheadVO> listForFundingRequirements() {
        List<NisBillheadVO> list = new ArrayList<>();
        List<NisBillheadVO> list1 = nisBillheadDao.listForFundingRequirements();
        List<NisBillheadVO> list2 = nisBillheadDao.listForFundingRequirementsToHasDemand();

        if (EmptyUtils.isNotEmpty(list1)) {
            list.addAll(list1);
        }
        if (EmptyUtils.isNotEmpty(list2)) {
            list.addAll(list2);
        }
        return list;
    }

    /**
     * 封装票据信息
     *
     * @param datas
     * @return
     */
    public DataTablesOutput setAcceBillDataTables(DataTablesOutput datas) {
        if (datas != null && datas.getData() != null) {
            List<NisBillheadVO> list = datas.getData();
            for (NisBillheadVO vo : list) {
                if ((NetbankConstant.PAY_TYPE_49.equals(vo.getSysVoucherCode()) && vo.getIsOpenBill().equals(CommonConstant.YES))
                        || NetbankConstant.PAY_TYPE_50.equals(vo.getSysVoucherCode())) {
                    vo.setIsSelectedAcceBill(CommonConstant.NO);
                    List<NisBill> bills = nisBillService.listByPayId(vo.getId());
                    if (EmptyUtils.isNotEmpty(bills)) {
                        vo.setIsSelectedAcceBill(CommonConstant.YES);
                    }
                    vo.setIsSelectedAcceBillName(this.dictService.get("sf_yn", vo.getIsSelectedAcceBill()));
                }
            }
            datas.setData(list);
        }
        return datas;
    }

    /**
     * 设置付款办理的关联字段
     *
     * @param output
     * @param opType
     * @return
     */
    public DataTablesOutput setJoinField(DataTablesOutput output, String opType, List<String> voucherStat) {
        if (!EmptyUtils.isEmpty(output.getData())) {
            List<NisBillheadVO> list = output.getData();
            List<NisBillheadVO> list1 = new ArrayList<>();
            Map<String, CorpDTO> corpMap = corpService.getCorpMap();
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(NetbankConstant.NETBANK_STATUS_DICT, NetbankConstant.NETBANK_BILLFROM_DICT,
                    NetbankConstant.NETBANK_BILLTYPE_DICT, NetbankConstant.NETBANK_BILLFLAG_DICT, CommonConstant.COMMON_SF_YN_DICT, BdlinkConstant.EXC_VOUCHERSTAT_DICT,
                    EnterpriseLimitControlService.LANDING_RESULT_DICT);
            List<String> billIds = new ArrayList<>();
            for (NisBillheadVO vo : list) {
                if (corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                }
                if (curMap.containsKey(vo.getCurId())) {
                    vo.setCurName(curMap.get(vo.getCurId()).getCurName());
                }
                if (StringUtils.isNotBlank(vo.getId())) {
                    billIds.add(vo.getId());
                }
                //字典注入
                this.injectValues(vo, listDictMap);
            }
            // 主查询中，已添加
//             if ("query".equals(opType)) {
// //                Map<String, com.bytter.biz.bdlink.api.dto.BisExcDTO> bisExcMap = bisExcService.getBisExcMapByCbsBillIds(billIds, BdlinkConstant.FROM_NIS_PAY);
//                 Map<String, BisExcDTO> bisExcMap = bisExcService.getBisExcMapByCbsBillIds(billIds, BdlinkConstant.FROM_NIS_PAY,voucherStat);
//                 for (NisBillheadVO vo : list) {
//                     BisExcDTO bisExcDTO = bisExcMap.get(vo.getId());
//                     if (bisExcDTO != null) {
//                         vo.setBisExcId(bisExcDTO.getId());
//                         vo.setVoucherStat(bisExcDTO.getVoucherStat());
// //                        vo.setVoucherNo(bisExcDTO.getVoucherNo());没有对应凭证不要强行赋值
//                     }
//                 }
//                 if(voucherStat!=null){
//                     list=new ArrayList<>();
//                     list1 = list.stream().filter(a->{
//                         if (bisExcMap.get(a.getId()) != null){
//                             return true;
//                         }
//                         return false;
//                     } ).collect(Collectors.toList());
//                     list=list1;
//                 }
//             }
            output.setData(list);
        }
        return output;
    }

    /**
     * 付款信息字典注入
     *
     * @param vo
     * @param listDictMap
     */
    private void injectValues(NisBillheadVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
        // 票据类型
        Optional.ofNullable(vo.getBillType()).ifPresent(billType -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLTYPE_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billType + "")))
                    .ifPresent(dict -> vo.setBillTypeName(dict.getName()));
        });
        // 流通介质
        Optional.ofNullable(vo.getBillFlag()).ifPresent(billFlag -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLFLAG_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFlag + "")))
                    .ifPresent(dict -> vo.setBillFlagName(dict.getName()));
        });
        // 单据状态
        Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_STATUS_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status + "")))
                    .ifPresent(dict -> vo.setStatusName(dict.getName()));
        });
        // 单据来源
        Optional.ofNullable(vo.getBillFrom()).ifPresent(billFrom -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(NetbankConstant.NETBANK_BILLFROM_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(billFrom + "")))
                    .ifPresent(dict -> vo.setBillFromName(dict.getName()));
        });
        // 是否已付款
        Optional.ofNullable(vo.getIsPayed()).ifPresent(isPayed -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isPayed)))
                    .ifPresent(dict -> vo.setIsPayedName(dict.getName()));
        });
        // 是否自动付款
        if (ObjectUtil.isEmpty(vo.getIsAutoPay())) {
            vo.setIsAutoPay(CommonConstant.NO);
        }
        Optional.ofNullable(vo.getIsAutoPay()).ifPresent(isAutoPay -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isAutoPay)))
                    .ifPresent(dict -> vo.setIsAutoPayName(dict.getName()));
        });
        // 是否对私付款
        Optional.ofNullable(vo.getIsForindividual()).ifPresent(isForindividual -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(CommonConstant.COMMON_SF_YN_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(isForindividual)))
                    .ifPresent(dict -> vo.setIsForindividualName(dict.getName()));
        });
        // 交易状态
        Optional.ofNullable(vo.getVoucherStat()).ifPresent(voucherStat -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(BdlinkConstant.EXC_VOUCHERSTAT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(voucherStat)))
                    .ifPresent(dict -> vo.setVoucherStatName(dict.getName()));
        });
        // 落地限额处理结果名称：0 等待银行限额审批 / A 银行同意支付 / R 银行拒绝支付；未涉及落地为 null 则名称留空
        Optional.ofNullable(vo.getLandingLimitResult()).ifPresent(landingLimitResult -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get(EnterpriseLimitControlService.LANDING_RESULT_DICT);
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(landingLimitResult)))
                    .ifPresent(dict -> vo.setLandingLimitResultName(dict.getName()));
        });
    }

    /**
     * 设置记账状态
     *
     * @param dataTablesOutput
     * @return
     */
    private DataTablesOutput setTallyStatus(DataTablesOutput dataTablesOutput) {
        if (dataTablesOutput == null) {
            return dataTablesOutput;
        }
        List<NisBillheadVO> list = dataTablesOutput.getData();
        Map<String, DictDTO> dictMap = dictService.listMap("nisBillhead.status");
        for (NisBillheadVO vo : list) {
            if (StringUtils.isNotBlank(vo.getTallySign()) && vo.getStatus() >= NetbankConstant.STATUE_CHECK && vo.getStatus() < NetbankConstant.STATUE_TALLY_CHECKPASS) {
                if (NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue().equals(vo.getTallySign())) {
                    //记账复核通过
                    vo.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
                } else if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(vo.getTallySign())) {
                    //记账提交待复核
                    vo.setStatus(NetbankConstant.STATUE_TALLY_WAIT_CHECK);
                } else if (NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(vo.getTallySign())) {
                    //记账复核打回
                    vo.setStatus(NetbankConstant.STATUE_TALLY_BACK);
                } else if (NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(vo.getTallySign())) {
                    //记账暂存
                    vo.setStatus(NetbankConstant.STATUE_TALLY_TEMP);
                }
                // vo.setStatusName(this.dictService.get("nisBillhead.status", vo.getStatus()));
                Optional.ofNullable(vo.getStatus()).flatMap(status ->
                                Optional.ofNullable(dictMap.get(String.valueOf(status))))
                        .ifPresent(item -> vo.setStatusName(item.getName()));
            }
        }
        dataTablesOutput.setData(list);
        return dataTablesOutput;
    }

    /**
     * 设置实际付款日期
     *
     * @param dataTablesOutput
     * @return
     */
    private DataTablesOutput setActualPayDate(DataTablesOutput dataTablesOutput) {
        if (dataTablesOutput == null) {
            return dataTablesOutput;
        }
        List<NisBillheadVO> list = dataTablesOutput.getData();
        cn.hutool.core.date.StopWatch stopWatch = new cn.hutool.core.date.StopWatch();
        stopWatch.start("票据付款");
        // 票据付款
        List<String> payIdList = list.stream().filter(item -> (NetbankConstant.PAY_TYPE_49.equals(item.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_50.equals(item.getSysVoucherCode())))
                .map(NisBillheadVO::getId).collect(Collectors.toList());
        List<NisBillDataVo> payDataList = nisBillDao.listDataByPayId(payIdList);
        Map<String, NisBillDataVo> mapPayData = payDataList.stream().collect(Collectors.toMap(NisBillDataVo::getPayId, Function.identity(), (pre, next) -> next));
        stopWatch.stop();
        stopWatch.start("其它请求");
        // 其它请求
        List<String> billIdList = list.stream().filter(item -> !StrUtil.equalsAny(item.getSysVoucherCode(), NetbankConstant.PAY_TYPE_34,
                        NetbankConstant.PAY_TYPE_49, NetbankConstant.PAY_TYPE_50))
                .map(NisBillheadVO::getId).collect(Collectors.toList());
        List<NisBillDataVo> billDataList = nisBillheadDao.listDataByBillId(billIdList);
        Map<String, NisBillDataVo> mapBillData = billDataList.stream().collect(Collectors.toMap(NisBillDataVo::getBillId, Function.identity(), (pre, next) -> next));
        stopWatch.stop();
        stopWatch.start("数据组装");
        for (NisBillheadVO vo : list) {
            //如果是外付网银取付款指令的交易日期
            if (NetbankConstant.PAY_TYPE_34.equals(vo.getSysVoucherCode())) {
                if (!EmptyUtils.isEmpty(vo.getTransTime())) {
                    vo.setActualPayDate(vo.getTransTime());
                }
                //如果是票据付款，则取票据业务的业务办理日期
            } else if (NetbankConstant.PAY_TYPE_49.equals(vo.getSysVoucherCode()) || NetbankConstant.PAY_TYPE_50.equals(vo.getSysVoucherCode())) {
                // List<NisBill> bills = nisBillService.listByPayId(vo.getId());
                // if (EmptyUtils.isNotEmpty(bills) && bills.size() > 0) {
                //     NisBill bill = bills.get(0);
                //     List<AbAccrecordsDTO> abAccrecordsDTOS = iAccebillService.getAccrecordsById(bill.getBillId());
                //     if(EmptyUtils.isNotEmpty(abAccrecordsDTOS) && abAccrecordsDTOS.size() > 0){
                //         AbAccrecordsDTO abAccrecordsDTO = abAccrecordsDTOS.get(0);
                //         if(!EmptyUtils.isEmpty(abAccrecordsDTO) && !EmptyUtils.isEmpty(abAccrecordsDTO.getBusinessDate())){
                //             vo.setActualPayDate(abAccrecordsDTO.getBusinessDate());
                //         }
                //     }
                // }
                Optional.ofNullable(mapPayData.get(vo.getId())).ifPresent(item -> {
                    vo.setActualPayDate(item.getBusinessDate());
                });
            } else {
                //看付款明细勾对 流水的
                // List<String> dtlIdList = nisBillheadDao.getDtlIdByBillId(vo.getId());
                // if(EmptyUtils.isNotEmpty(dtlIdList) && dtlIdList.size() > 0){
                //     String dtlId = dtlIdList.get(0);
                //     com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(dtlId);
                //     if(!EmptyUtils.isEmpty(bisAccDtlDTO) && !EmptyUtils.isEmpty(bisAccDtlDTO.getTransTime())){
                //         vo.setActualPayDate(bisAccDtlDTO.getTransTime());
                //     }
                // }
                Optional.ofNullable(mapBillData.get(vo.getId())).ifPresent(item -> {
                    vo.setActualPayDate(item.getTransTime());
                });
            }
        }
        stopWatch.stop();
        dataTablesOutput.setData(list);
        logger.error(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return dataTablesOutput;
    }

    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        nisBillheadDao.updatePrint(id);
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadVO getByIdUnion(String id) {
        NisBillheadVO nisBillhead = nisBillheadDao.getByIdUnion(id);
        //获取余额
        if (nisBillhead != null) {
            CorpDTO corpDTO = corpService.get(nisBillhead.getCorpId());
            nisBillhead.setCorpName(corpDTO == null ? "" : corpDTO.getName());
            if (!StringUtil.isBlank(nisBillhead.getProv())) {
                RegionDTO prov = regionService.getById(nisBillhead.getProv());
                nisBillhead.setProvName(prov == null ? "" : prov.getName());
            }
            if (!StringUtil.isBlank(nisBillhead.getCity())) {
                RegionDTO city = regionService.getById(nisBillhead.getCity());
                nisBillhead.setCityName(city == null ? "" : city.getName());
            }
            BtCurrencyDTO currencyDTO = btCurrencyService.getObjectById(nisBillhead.getCurId());
            nisBillhead.setCurName(currencyDTO == null ? "" : currencyDTO.getCurName());
            // 账户余额默认为0
            nisBillhead.setBalance(BigDecimal.ZERO);
            if (NetbankConstant.DIRECTPAYFLAG_Y.equals(nisBillhead.getDirectPayFlag())) {
                BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
                if (bal != null) {
                    nisBillhead.setBalance(bal.getAvailBal());
                    nisBillhead.setAvailBal(bal.getAvailBal());
                }
            } else {
                FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(nisBillhead.getPayerAcc());
                if (bal != null) {
                    //内部账号余额 = 可用余额 - 冻结金额
                    nisBillhead.setBalance(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
                    nisBillhead.setAvailBal(bal.getCheckBalance().subtract(bal.getFreezeMoney()));
                }
            }
            if (nisBillhead.getBalance() == null) {
                nisBillhead.setBalance(BigDecimal.ZERO);
            }
            //设置付款账号的可用余额
            if (StringUtils.isBlank(nisBillhead.getPayerAcc())) {
                //账号为空的时候，账户余额默认为0
                nisBillhead.setAvailBal(BigDecimal.ZERO);
            } else {
                //获取账户在途余额，大参林不需要减去在途金额2025-06-24
//                BigDecimal useBal = nisBillheadDao.getUseBal(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
//                BigDecimal availBal = nisBillhead.getBalance().subtract(useBal);
//                nisBillhead.setAvailBal(nisBillhead.getBalance().add(nisBillhead.getPayMoney()));
            }
            if (NetbankConstant.PAY_TYPE_50.equals(nisBillhead.getSysVoucherCode())
                    ||
//                    (
                    NetbankConstant.PAY_TYPE_49.equals(nisBillhead.getSysVoucherCode())
//                    && NetbankConstant.ISOPENBILL_1.equals(nisBillhead.getIsOpenBill()))
            ) {
                //获取票据关联信息
                nisBillhead.setBillId(nisBillService.getBillIds(nisBillhead.getId()));
            }
            if (StringUtils.isBlank(nisBillhead.getDownAcc()) && StringUtils.isNotBlank(nisBillhead.getDownAccId())) {
                BtBankAccDTO bankAcc = bankAccService.getBankAccById(nisBillhead.getDownAccId());
                nisBillhead.setDownAcc(bankAcc != null ? bankAcc.getBankAcc() : " ");
                nisBillhead.setDownAccName(bankAcc != null ? bankAcc.getAccName() : "");
            }
            if (EmptyUtils.isNotEmpty(nisBillhead.getRealPayerAccId())) {
                BtBankAccDTO bankAcc = bankAccService.getBankAccById(nisBillhead.getRealPayerAccId());
                if (!EmptyUtils.isEmpty(bankAcc)) {
                    nisBillhead.setRealPayerAcc(bankAcc.getBankAcc());
                    nisBillhead.setRealPayerAccAccName(bankAcc.getAccName());
                    BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(bankAcc.getBankAcc(), bankAcc.getCurId());
                    if (!EmptyUtils.isEmpty(bal)) {
                        nisBillhead.setRealPayerAccBal(bal.getBal());
                    }
                }
            }

        }
        return nisBillhead;
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadVO getVoById(String id) {
        return nisBillheadDao.getVoById(id);
    }

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public List<NisBillheadVO> listByErpId(String erpId) {
        return nisBillheadDao.listByErpId(erpId);
    }

    /**
     * 根据共享结算单号list获取付款信息
     *
     * @param bursarBillNoList
     */
    public List<NisBillheadVO> listByBursarBillNos(List<String> bursarBillNoList) {
        return nisBillheadDao.listByBursarBillNos(bursarBillNoList);
    }

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public NisBillheadVO getByErpId(String erpId) {
        return nisBillheadDao.getByErpId(erpId);
    }

    /**
     * 根据单号获取付款单信息
     *
     * @param billNo
     * @return
     */
    public NisBillheadVO getByBillNo(String billNo) {
        NisBillheadVO nisBillheadVO = nisBillheadDao.getByBillNo(billNo);
        //填充币别名称和单位名称
        BtCurrencyDTO curDTO = iBtCurrencyService.getObjectById(nisBillheadVO.getCurId());
        String corpName = corpService.getCorpName(nisBillheadVO.getCorpId());
        nisBillheadVO.setCurName(curDTO.getCurName());
        nisBillheadVO.setCorpName(corpName);
        if (StringUtils.isNotBlank(nisBillheadVO.getPayeeBankCode())) {
            InputBankInfoDTO inputBankInfoDTO = btBankService.getByBankCode(nisBillheadVO.getPayeeBankCode());
            if (null != inputBankInfoDTO) {
                nisBillheadVO.setProvName(inputBankInfoDTO.getProvName());
                nisBillheadVO.setCityName(inputBankInfoDTO.getCityName());
            }
        }
        BtBankAccDTO dto = btBankAccService.getBankAccById(nisBillheadVO.getDownAccId());
        if (Objects.nonNull(dto)) {
            nisBillheadVO.setDownAcc(dto.getBankAcc());
            nisBillheadVO.setDownAccName(dto.getAccName());
        }
        return nisBillheadVO;
    }

    /**
     * 校验付款账号和接口代码
     *
     * @param nisBillhead
     */
    private void validatePayAcc(NisBillheadVO nisBillhead) {
        //票据兑付生成不需要在此校验这些，因为此时还没有付款账号，它生成一笔暂存的外付网银付款单，需要到付款办理补全资料后才能提交
        //债券还款生成不需要在此校验这些，
        if(com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_3.equals(nisBillhead.getBillFrom())
             ||com.bytter.core.constant.NetbankConstant.BILLFROM_10.equals(nisBillhead.getBillFrom())
                ||com.bytter.core.constant.NetbankConstant.BILLFROM_4.equals(nisBillhead.getBillFrom())
                ||com.bytter.core.constant.NetbankConstant.BILLFROM_8.equals(nisBillhead.getBillFrom())
             ||com.bytter.core.constant.NetbankConstant.BILLFROM_11.equals(nisBillhead.getBillFrom())
             ||com.bytter.core.constant.NetbankConstant.BILLFROM_9.equals(nisBillhead.getBillFrom())){
            return;
        }
        //获取系统结算方式
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId());
        //代付
        if (NetbankConstant.ISACCOUNT_0.equals(voucherType.getIsAccount())) {
            if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_48.equals(voucherType.getSysVoucherCode())) {
                //如果是否已付款为是的话不生成指令
                if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                    BtBankAccDTO payerAcc = null;
                    //代付
                    if (NetbankConstant.PAY_TYPE_33.equals(nisBillhead.getSysVoucherCode())){
                        payerAcc = btBankAccService.getBankAccById(nisBillhead.getRealPayerAccId());
                    }else {
                        //获取付款账号
                        payerAcc = btBankAccService.getBankAccById(nisBillhead.getAccId());
                    }
                    if (payerAcc == null) {
                        throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisBillhead.getPayerAcc());
                    }
                }
            }
        }
        //接口代码业务规则
        if (NetbankConstant.ISACCOUNT_0.equals(voucherType.getIsAccount())) {
            if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_48.equals(voucherType.getSysVoucherCode())) {
                //如果是否已付款为是的话不生成指令
                if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                    BisBifInitDTO bifInitDTO = bisBifInitService.getObjectById(nisBillhead.getBifCode());
                    if(ObjectUtils.isEmpty(bifInitDTO)) {
                        throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BIFCODE_NULL, nisBillhead.getPayerAcc());
                    }
                    BisBifRuleDTO bisBifRule = bisBifRuleService.findByBifId(nisBillhead.getBifCode());
                    if(ObjectUtils.isEmpty(bisBifRule)) {
                        throw new ParameterException(NetbankMessageEnum.NIS_WAGE_MESSAGE_BISBIFRULE_NULL, bifInitDTO.getBifCode(), bifInitDTO.getName());
                    }
                }

            }
        }
    }

    /**
     * 保存
     *
     * @param nisBillhead
     */
    public String saveNisBillhead(NisBillheadVO nisBillhead, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(nisBillhead.getTimeStamp())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        //校验付款账号
        validatePayAcc(nisBillhead);
        //客商保存  兼容前后端分离
        if(NetbankConstant.IS_EXTERNAL_CORP_0.equals(nisBillhead.getIsExternalCorp())){
            nisBillhead.setIsExternalCorp(null);
        }
        NisBillhead entity = new NisBillhead();

        BeanUtils.copyNotNullProperties(nisBillhead, entity);
        entity.setScfTransferAmt(nisBillhead.getScfTransferAmt());
        //判断是否挂证书
        if (!StringUtils.isEmpty(nisBillhead.getCaCoding())) {
            SysCaInfoDTO caDto = new SysCaInfoDTO();
            caDto.setRandom(nisBillhead.getRandom());
            caDto.setEncryDate(DateUtils.getDate());
            caDto.setEncrySource(nisBillhead.getEncrySource());
            caDto.setCaCoding(nisBillhead.getCaCoding());
            entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto)); // -->调用system模块
        }
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        }
        //非手工录入的单据不写创建人
        if (StringUtils.isBlank(nisBillhead.getErpId())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        } else {
            entity.setCreateBy(null);
            entity.setCreateDate(null);
        }
        //是否已付款标志 默认否
        if (StringUtils.isEmpty(entity.getIsPayed())) {
            entity.setIsPayed(NetbankConstant.ISPAYED_0);
        }
        String innerSign = getInnerSign(nisBillhead);
        //内部资金计划内转标识
        entity.setInnerSign(innerSign);
        nisBillhead.setInnerSign(innerSign);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        nisBillhead.setSysVoucherCode(voucherType.getSysVoucherCode());
        super.saveEntity(entity); //保存付款单据
        // 保存取得单据ID后同步限额：暂存走释放分支，符合占用条件的付款单校验并占用。
        EnterpriseLimitControlService.requireSuccess(occupyEnterpriseLimit(entity, user, false));

        // 初始化联行号信息 -->调用system模块
        this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());
        if (isCheckExternalCorp(nisBillhead) && !EmptyUtils.isEmpty(nisBillhead.getIsExternalCorp())) {
            //保存客商信息
            this.saveIfNotExist(nisBillhead);
        }
        //判断是否是对私付款，对私付款需生成对私付款明细
        if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
            //费用报销汇总改造不能传数组，所以临时字段切割设置值
            if (StringUtils.isNotBlank(nisBillhead.getExpenseIdStr())) {
                nisBillhead.setExpenseId(nisBillhead.getExpenseIdStr().split(","));
                nisBillhead.setDifBanks(nisBillhead.getDifBankStr().split(","));
                nisBillhead.setAreaSigns(nisBillhead.getAreaSignStr().split(","));
            }
            //判断是否批量对私付款
            if (!EmptyUtils.isEmptyArray(nisBillhead.getExpenseId())) {
                for (int i = 0; i < nisBillhead.getExpenseId().length; i++) {
                    NisExpenseDetail nisExpenseDetail = nisExpenseDetailService.get(nisBillhead.getExpenseId()[i], true);
                    //审批通过状态才可以提交
                    if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
                        throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
                    }
                    NisExpenseDetailVO vo = new NisExpenseDetailVO();
                    BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
                    //更新为汇总状态
                    vo.setStatus(ExpenseConstant.STATUE_COLLECT);
                    vo.setVoucherStat(BdlinkConstant.PRE_SEND);
                    vo.setFirstReturnMsg("");
                    vo.setNextReturnMsg("");
                    vo.setReturnTime(null);
                    vo.setDifBank(nisBillhead.getDifBanks()[i]);
                    vo.setAreaSign(nisBillhead.getAreaSigns()[i]);
                    vo.setBillId(entity.getId());
                    vo.setUpdateBy(user.getLoginName());
                    vo.setUpdateDate(DateUtils.getDate());
                    if (EmptyUtils.isBlank(nisExpenseDetail.getCreateBy())) {
                        vo.setCreateBy(user.getLoginName());
                    }
                    if (vo.getCreateDate() == null) {
                        vo.setCreateDate(DateUtils.getDate());
                    }
                    //费用报销汇总，控制费用报销明细状态为审批通过
                    Integer[] oldStatus = {ExpenseConstant.STATUE_CHECK};
                    nisExpenseDetailService.updateNisExpenseDetailStatus(vo, oldStatus); //带原状态更新费用报销单
                    nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_COLLECT, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
                }
            } else {
                //非费用报销过来的对私付款单，则判断新增或者编辑对私明细
                if (!NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
                    //保存对私明细
                    this.saveNisExpenseDetail(entity, user);
                }
            }
        }

        //写入计划执行
        nisBillhead.setId(entity.getId());
        nisBillhead.setBillNo(entity.getBillNo());
        this.writeBudgetProcess(nisBillhead, user);

        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;

        // 初始化联行号信息 -- 上面有相同的调用
//        this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());

        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
            if (!StringUtils.isBlank(nisBillhead.getBillId())) {
                //用独立事务写入关联关系
                NisBillService nisBillService = (NisBillService) ApplicationContextUtil.getContext().getBean("nisBillService");
                //保存票据关联信息
                nisBillService.saveOrUpdateSync(entity.getId(), nisBillhead.getBillId(), nisBillhead.getBillBalance());
                String[] abAccrecordsIds = nisBillhead.getBillId().split(CommonConstant.COMMA_SPLIT);
                //保存前先解锁票据
                this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                //系统结算方式为50-应收票据时且是否已付款为是时，调用新的接口
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                    String billPoolIds = nisBillhead.getBillPoolIds();
                    if (StringUtils.isNotEmpty(billPoolIds)) {
                        HashSet<String> hashSet = new HashSet<>(Arrays.asList(abAccrecordsIds));
                        String[] billPoolIdsArr = billPoolIds.split(",");
                        for (String s : billPoolIdsArr) {
                            hashSet.remove(s);
                        }
                        abAccrecordsIds = CollectionUtils.isEmpty(hashSet) ? null : (String[]) hashSet.toArray();
                    }
                    if (abAccrecordsIds != null) {
                        //应收票据批量解除占用背书申请流水
                        this.iAccebillService.batchUnHoldEndorse(abAccrecordsIds, user);
                        //应收票据批量占用背书申请流水
                        this.iAccebillService.batchHoldEndorse(abAccrecordsIds, user);
                    }
                } else {
                    //系统结算方式为50-应收票据时，锁定票据
                    //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据```
                    this.iAccebillService.batchLock(abAccrecordsIds, user);
                }
                //保存后做额度占用
                this.iAccebillService.batchPayLockMoneyAdd(nisBillService.listNisBillVoByPayId(entity.getId()));
            }
        } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(entity.getIsOpenBill())
                && StringUtils.isNotBlank(nisBillhead.getOpenBillInfo())) {
            AbEmitBillVoDTO voDTO = convertToAbEmitBillVoDTO(nisBillhead.getOpenBillInfo());
            if (StringUtils.isBlank(voDTO.getPayeracc())) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
            if (voDTO.getBillMoney() == null || BigDecimal.ZERO.compareTo(voDTO.getBillMoney()) == 0 || (voDTO.getBillMoney().compareTo(nisBillhead.getPayMoney()) != 0)) {
                throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
            }
            //更新操作不做保存票据信息操作
            if (StringUtils.isBlank(voDTO.getAid())) {
                voDTO.setRmk(StringUtils.isBlank(voDTO.getRmk()) ? nisBillhead.getPurpose() : voDTO.getRmk());
                saveAbEmitBillNew(entity.getId(), user, voDTO); //里面有一堆逻辑；挂担保 授信 保证金等
            }
            //提交的时候票据信息不能为空
            if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus()) && StringUtils.isBlank(nisBillhead.getOpenBillInfo())) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
        }
        //供应链金融：51-应付债权凭证 写入中间表 nis_scf_info 关联
        //51-未开立时凭证ID为空，仅保存平台/产品上下文，待审批通过生成暂存凭证后由审批后方法补写凭证ID
        if (NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
            String scfVoucherIds = CommonConstant.YES.equals(entity.getScfIsOpened()) ? nisBillhead.getScfVoucherIds() : null;
            nisScfInfoService.saveOrUpdateSync(entity.getId(), entity.getBillNo(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE,
                    scfVoucherIds, nisBillhead.getScfPlatformId(), nisBillhead.getScfProductId());
        }
        if (NetbankConstant.PAY_TYPE_52.equals(voucherType.getSysVoucherCode())) {
            //供应链金融：52-应收债权凭证 写入中间表 nis_scf_info 关联（保存/提交都触发转让数据保存，凭证id存 voucher_id，转让草稿id存 transfer_id）
            String voucherIds = nisBillhead.getScfReceivableVoucherIds();
            nisScfInfoService.saveOrUpdateSync(entity.getId(), entity.getBillNo(), NetbankConstant.SCF_VOUCHER_TYPE_RECEIVABLE,
                    voucherIds, nisBillhead.getScfTransferIds(), nisBillhead.getScfPlatformId(), nisBillhead.getScfProductId());
        }

        //启动审批 --> entity.getStatus() 默认是11
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;
            //传入付款方式审批条件
            NisBillheadVO nisBillheadVO = new NisBillheadVO();
            BeanUtils.copyNotNullProperties(entity, nisBillheadVO);
            nisBillheadVO.setVoucherType(voucherType.getVoucherType());
            nisBillheadVO.setVoucherName(voucherType.getVoucherName());
            //增加付款账户是否上线审批流（账户属性，账户性质审批流，客户（中信泰富）需要，需求暂未下来暂时屏蔽）
            BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(nisBillheadVO.getPayerAcc());
            if (!EmptyUtils.isEmpty(bankAccInfo)) {
                nisBillheadVO.setIsOnline(bankAccInfo.getIsOnline());
                //nisBillheadVO.setAttributeId(bankAccInfo.getAttributeId());
                //nisBillheadVO.setNatureId(bankAccInfo.getNatureId());
            }
            //调用审批后方法
            getStartHandler().handler(NisWorkFlowConstant.NIS_BILLHEAD, entity.getId(), nisBillheadVO, entity.getCorpId(), user.getId());
        }

        if (user != null) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
        return entity.getBillNo();
    }


    private AbEmitBillVoDTO convertToAbEmitBillVoDTO(String openBillInfo) {
        OpenBillInfoDTO ss = JsonUtils.parseObject(openBillInfo, OpenBillInfoDTO.class);
        AbEmitBillVoDTO voDTO = new AbEmitBillVoDTO();
        BeanUtils.copyProperties(ss, voDTO, "itemCode", "iteName", "processMoney", "exchangeRate", "processCurMoney");
        voDTO.setProcessCurMoney(new BigDecimal[]{ss.getProcessCurMoney()});
        voDTO.setExchangeRate(new BigDecimal[]{ss.getExchangeRate()});
        voDTO.setProcessMoney(new BigDecimal[]{ss.getProcessMoney()});
        voDTO.setItemCode(new String[]{ss.getItemCode()});
        voDTO.setItemName(new String[]{ss.getItemName()});
        voDTO.setOpenBillInfo(openBillInfo);
        return voDTO;
    }

    /**
     * TCC
     * @param nisBillhead
     */
    /*@Transactional
    public String saveNisBillhead2(String id, NisBillheadVO nisBillhead, ICurrentUser user) {
        //校验时间戳，是否存在重复提交
        if (this.isExistTimeStamp(nisBillhead.getTimeStamp())) {
            throw new BusinessException(NetbankMessageEnum.NIS_MESSAGE_REPEAT_SUBMIT);
        }
        NisBillhead entity = new NisBillhead();

        BeanUtils.copyNotNullProperties(nisBillhead, entity);
        //判断是否挂证书
        if (!StringUtils.isEmpty(nisBillhead.getCaCoding())) {
            SysCaInfoDTO caDto = new SysCaInfoDTO();
            caDto.setRandom(nisBillhead.getRandom());
            caDto.setEncryDate(DateUtils.getDate());
            caDto.setEncrySource(nisBillhead.getEncrySource());
            caDto.setCaCoding(nisBillhead.getCaCoding());
            entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto)); // TODO -->调用system模块
        }
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        }
        //非手工录入的单据不写创建人
        if (StringUtils.isBlank(nisBillhead.getErpId())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        } else {
            entity.setCreateBy(null);
            entity.setCreateDate(null);
        }
        //是否已付款标志 默认否
        if (StringUtils.isEmpty(entity.getIsPayed())) {
            entity.setIsPayed(NetbankConstant.ISPAYED_0);
        }
        String innerSign = getInnerSign(nisBillhead);
        //内部资金计划内转标识
        entity.setInnerSign(innerSign);
        nisBillhead.setInnerSign(innerSign);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        nisBillhead.setSysVoucherCode(voucherType.getSysVoucherCode());
        entity.setId(id);
        super.saveEntity(entity); //保存付款单据


        // 初始化联行号信息 -->调用system模块
        this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId()); //TODO
        if (isCheckExternalCorp(nisBillhead) && !EmptyUtils.isEmpty(nisBillhead.getIsExternalCorp())) {
            //保存客商信息
            this.saveIfNotExist(nisBillhead); //TODO
        }
        //判断是否是对私付款，对私付款需生成对私付款明细
        if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
            //费用报销汇总改造不能传数组，所以临时字段切割设置值
            if (StringUtils.isNotBlank(nisBillhead.getExpenseIdStr())) {
                nisBillhead.setExpenseId(nisBillhead.getExpenseIdStr().split(","));
                nisBillhead.setDifBanks(nisBillhead.getDifBankStr().split(","));
                nisBillhead.setAreaSigns(nisBillhead.getAreaSignStr().split(","));
            }
            //判断是否批量对私付款
            if (!EmptyUtils.isEmptyArray(nisBillhead.getExpenseId())) {
                for (int i = 0; i < nisBillhead.getExpenseId().length; i++) {
                    NisExpenseDetail nisExpenseDetail = nisExpenseDetailService.get(nisBillhead.getExpenseId()[i], true);
                    //审批通过状态才可以提交
                    if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
                        throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
                    }
                    NisExpenseDetailVO vo = new NisExpenseDetailVO();
                    BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
                    //更新为汇总状态
                    vo.setStatus(ExpenseConstant.STATUE_COLLECT);
                    vo.setVoucherStat(BdlinkConstant.PRE_SEND);
                    vo.setFirstReturnMsg("");
                    vo.setNextReturnMsg("");
                    vo.setReturnTime(null);
                    vo.setDifBank(nisBillhead.getDifBanks()[i]);
                    vo.setAreaSign(nisBillhead.getAreaSigns()[i]);
                    vo.setBillId(entity.getId());
                    vo.setUpdateBy(user.getLoginName());
                    vo.setUpdateDate(DateUtils.getDate());
                    if (EmptyUtils.isBlank(nisExpenseDetail.getCreateBy())) {
                        vo.setCreateBy(user.getLoginName());
                    }
                    if (vo.getCreateDate() == null) {
                        vo.setCreateDate(DateUtils.getDate());
                    }
                    //费用报销汇总，控制费用报销明细状态为审批通过
                    Integer[] oldStatus = {ExpenseConstant.STATUE_CHECK};
                    nisExpenseDetailService.updateNisExpenseDetailStatus(vo, oldStatus); //带原状态更新费用报销单 TODO
                    nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_COLLECT, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
                }
            } else {
                //非费用报销过来的对私付款单，则判断新增或者编辑对私明细
                if (!NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
                    //保存对私明细
                    this.saveNisExpenseDetail(entity, user); //TODO
                }
            }
        }

        //票据
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
            if (!StringUtils.isBlank(nisBillhead.getBillId())) {
                //用独立事务写入关联关系
                NisBillService nisBillService = (NisBillService) ApplicationContextUtil.getContext().getBean("nisBillService");
                //保存票据关联信息
                nisBillService.saveOrUpdateSync(entity.getId(), nisBillhead.getBillId(), nisBillhead.getBillBalance()); //NisBill payId=付款单ID
                String[] abAccrecordsIds = nisBillhead.getBillId().split(CommonConstant.COMMA_SPLIT);
                //保存前先解锁票据
                this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                //系统结算方式为50-应收票据时且是否已付款为是时，调用新的接口
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                    String billPoolIds = nisBillhead.getBillPoolIds();
                    if (StringUtils.isNotEmpty(billPoolIds)) {
                        HashSet<String> hashSet = new HashSet<>(Arrays.asList(abAccrecordsIds));
                        String[] billPoolIdsArr = billPoolIds.split(",");
                        for (String s : billPoolIdsArr) {
                            hashSet.remove(s);
                        }
                        abAccrecordsIds = CollectionUtils.isEmpty(hashSet) ? null : (String[]) hashSet.toArray();
                    }
                    if (abAccrecordsIds != null) {
                        //应收票据批量解除占用背书申请流水
                        this.iAccebillService.batchUnHoldEndorse(abAccrecordsIds, user);
                        //应收票据批量占用背书申请流水
                        this.iAccebillService.batchHoldEndorse(abAccrecordsIds, user);
                    }
                } else {
                    //系统结算方式为50-应收票据时，锁定票据
                    //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                    this.iAccebillService.batchLock(abAccrecordsIds, user);
                }
                //保存后做额度占用
                this.iAccebillService.batchPayLockMoneyAdd(nisBillService.listNisBillVoByPayId(entity.getId()));
            }
        } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(entity.getIsOpenBill())
                && StringUtils.isNotBlank(nisBillhead.getOpenBillInfo())) {
            AbEmitBillVoDTO voDTO =convertToAbEmitBillVoDTO(nisBillhead.getOpenBillInfo());
            if (voDTO == null || StringUtils.isBlank(voDTO.getPayeracc())) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
            voDTO.setOpenBillInfo(nisBillhead.getOpenBillInfo());
            if (voDTO.getBillMoney() == null || BigDecimal.ZERO.compareTo(voDTO.getBillMoney()) == 0 || (voDTO.getBillMoney().compareTo(nisBillhead.getPayMoney()) != 0)) {
                throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
            }
            //更新操作不做保存票据信息操作
            if (StringUtils.isBlank(voDTO.getAid())) {
                voDTO.setRmk(StringUtils.isBlank(voDTO.getRmk()) ? nisBillhead.getPurpose() : voDTO.getRmk());
                saveAbEmitBillNew(entity.getId(), user, voDTO); //里面有一堆逻辑；挂担保 授信 保证金等
            }
            //提交的时候票据信息不能为空
            if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus()) && StringUtils.isBlank(nisBillhead.getOpenBillInfo())) {
                throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_MATCHRULE_PARENTHESIS);
            }
        }


        //写入计划执行
        nisBillhead.setId(entity.getId());
        nisBillhead.setBillNo(entity.getBillNo());
//        this.writeBudgetProcess(nisBillhead, user); //TODO 放到手动审批，走原来逻辑

        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_ADD_TEMPSAVE_SUCC;
        //启动审批 --> entity.getStatus() 默认是11
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = NisMessageConstant.NIS_LOG_ADD_SUBMIT_SUCC;
            //传入付款方式审批条件
            NisBillheadVO nisBillheadVO = new NisBillheadVO();
            BeanUtils.copyNotNullProperties(entity, nisBillheadVO);
            nisBillheadVO.setVoucherType(voucherType.getVoucherType());
            nisBillheadVO.setVoucherName(voucherType.getVoucherName());
            //增加付款账户是否上线审批流（账户属性，账户性质审批流，客户（中信泰富）需要，需求暂未下来暂时屏蔽）
            BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(nisBillheadVO.getPayerAcc());
            if (!EmptyUtils.isEmpty(bankAccInfo)) {
                nisBillheadVO.setIsOnline(bankAccInfo.getIsOnline());
            }
            //调用审批后方法
            //getStartHandler().handler(NisWorkFlowConstant.NIS_BILLHEAD, entity.getId(), nisBillheadVO, entity.getCorpId(), user.getId());

            //判断是否有工作流模型，没有则走自动审批
            Boolean b = getStartHandler().hasModel(NisWorkFlowConstant.NIS_BILLHEAD, entity.getCorpId());
            if (b) {
                this.writeBudgetProcess(nisBillhead, user);
                //手动审批
                getStartHandler().handler(NisWorkFlowConstant.NIS_BILLHEAD, entity.getId(), nisBillheadVO, entity.getCorpId(), user.getId()); //TODO 兼容之前的逻辑
            }else {
                this.writeBudgetProcess2(nisBillhead, user); //TODO 放到自动审批，改造后的TCC ，在二阶段直接修改状态审批通过 -->调用plan模块 TCC
                //自动审批
                String businessId = entity.getId();
                NisBillhead entity2 = nisBillheadDao.get(businessId, true);
                this.approvePassEnd2(entity2, true, user); //入口
            }
        }
        if (user != null) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }

        return entity.getBillNo();
    }*/

    /**
     * 保存单笔对私明细
     *
     * @param entity
     */
    private void saveNisExpenseDetail(NisBillhead entity, ICurrentUser user) {
        List<NisExpenseDetailVO> list = nisExpenseDetailService.listByBillId(entity.getId());
        NisExpenseDetailVO nisExpenseDetail = null;
        if (EmptyUtils.isEmptyList(list)) {
            nisExpenseDetail = new NisExpenseDetailVO();
            nisExpenseDetail.setExpenseDate(entity.getWishPayday());
            nisExpenseDetail.setCorpId(entity.getCorpId());
            //默认02-费用报销
            nisExpenseDetail.setBusType(ExpenseConstant.BUSTYPE_02);
            //默认03-市场费用报销
            nisExpenseDetail.setOpType(ExpenseConstant.OPTYPE_02_03);
            nisExpenseDetail.setDifBank(entity.getDifBank());
            nisExpenseDetail.setAreaSign(entity.getAreaSign());
            nisExpenseDetail.setExpenseMoney(entity.getPayMoney());
            nisExpenseDetail.setBankCode(entity.getPayeeBankCode());
            nisExpenseDetail.setBankName(entity.getPayeeBank());
            //默认4-银行卡
            nisExpenseDetail.setCardType(ExpenseConstant.CARDTYPE_4);
            nisExpenseDetail.setBankAcc(entity.getPayeeAcc());
            nisExpenseDetail.setAccName(entity.getPayeeAccName());
            nisExpenseDetail.setProv(entity.getProv());
            nisExpenseDetail.setCity(entity.getCity());
            nisExpenseDetail.setBillId(entity.getId());
            nisExpenseDetail.setPurpose(entity.getPurpose());
            nisExpenseDetail.setRmk(entity.getAddWord());
            nisExpenseDetail.setStatus(ExpenseConstant.STATUE_COLLECT);
            nisExpenseDetail.setVoucherStat(BdlinkConstant.PRE_SEND);
            nisExpenseDetail.setBillFrom(ExpenseConstant.BILLFROM_1);
            nisExpenseDetail.setCreateBy(user.getLoginName());
            nisExpenseDetail.setCreateDate(DateUtils.getDate());
            nisExpenseDetail.setBifCode(entity.getBifCode());
            nisExpenseDetailService.saveExpenseDetail(nisExpenseDetail, user);
        } else {
            nisExpenseDetail = list.get(0);
            nisExpenseDetail.setExpenseDate(entity.getWishPayday());
            nisExpenseDetail.setCorpId(entity.getCorpId());
            nisExpenseDetail.setDifBank(entity.getDifBank());
            nisExpenseDetail.setAreaSign(entity.getAreaSign());
            nisExpenseDetail.setExpenseMoney(entity.getPayMoney());
            nisExpenseDetail.setBankCode(entity.getPayeeBankCode());
            nisExpenseDetail.setBankName(entity.getPayeeBank());
            //默认4-银行卡
            nisExpenseDetail.setCardType(ExpenseConstant.CARDTYPE_4);
            nisExpenseDetail.setBankAcc(entity.getPayeeAcc());
            nisExpenseDetail.setAccName(entity.getPayeeAccName());
            nisExpenseDetail.setProv(entity.getProv());
            nisExpenseDetail.setCity(entity.getCity());
            nisExpenseDetail.setPurpose(entity.getPurpose());
            nisExpenseDetail.setRmk(entity.getAddWord());
            nisExpenseDetail.setStatus(ExpenseConstant.STATUE_COLLECT);
            nisExpenseDetail.setUpdateBy(user.getLoginName());
            nisExpenseDetail.setUpdateDate(DateUtils.getDate());
            nisExpenseDetail.setBifCode(entity.getBifCode());
            nisExpenseDetail.setVoucherStat(BdlinkConstant.PRE_SEND);
            nisExpenseDetail.setFirstReturnMsg("");
            nisExpenseDetail.setNextReturnMsg("");
            nisExpenseDetail.setReturnTime(null);
            nisExpenseDetailService.updateExpenseDetail(nisExpenseDetail, user);
        }
    }

    /**
     * 写入计划执行
     *
     * @param nisBillhead
     */
    public void writeBudgetProcess(NisBillheadVO nisBillhead, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(nisBillhead.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < nisBillhead.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(nisBillhead, user);
                fbsBudgetProcessDTO.setItemCode(nisBillhead.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(nisBillhead.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(nisBillhead.getProcessMoney()[i]);
                if (nisBillhead.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(nisBillhead.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(nisBillhead.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //计划执行状态同步更新（批量提交）
            // 应付票据状态为暂存，过滤应付票据
            if (!NetbankConstant.PAY_TYPE_49.equalsIgnoreCase(nisBillhead.getSysVoucherCode())) { // 计划科目为空 && 结算方式!= 应付票据，都需要更新？ 有这样的业务吗？
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                        nisBillhead.getBillNo(), nisBillhead.getStatus(), user);
            }
        }
    }

    /**
     * TCC
     * 写入计划执行
     * @param nisBillhead
     */
    /*@Transactional
    public void writeBudgetProcess2(NisBillheadVO nisBillhead, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(nisBillhead.getItemCode())) {
            //写入之前先删除
//            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());  //放到了 try 阶段
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < nisBillhead.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(nisBillhead, user);
                fbsBudgetProcessDTO.setItemCode(nisBillhead.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(nisBillhead.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(nisBillhead.getProcessMoney()[i]);
                if (nisBillhead.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(nisBillhead.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(nisBillhead.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            //需要先查询数据，放到上下文中，rollback 时恢复数据
            List<FbsBudgetProcessDTO> resetDTOList = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());

            fbsBudgetProcessService.prepareSave(list, resetDTOList, nisBillhead.getBillNo(),  user);//TODO -->调用plan模块 TCC; 数据先序列化
//            this.fbsBudgetProcessService.writeBudgetProcess(list, user); // 放到 commit
        } else {
            //计划执行状态同步更新（批量提交）
            // 应付票据状态为暂存，过滤应付票据
            if (!NetbankConstant.PAY_TYPE_49.equalsIgnoreCase(nisBillhead.getSysVoucherCode())) { // 计划科目为空 && 结算方式!= 应付票据，都需要更新？ 有这样的业务吗？
                //this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo(), nisBillhead.getStatus(), user);
                fbsBudgetProcessService.prepareUpdate(nisBillhead.getBillNo(), nisBillhead.getStatus(), user);//TODO -->调用plan模块 TCC
            }
        }
    }*/

    /**
     * 构建计划执行
     *
     * @param nisBillhead
     * @return
     */
    private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(NisBillheadVO nisBillhead, ICurrentUser user) {
        FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
        fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
        fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
        fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
        fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
        //获取系统结算方式
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId());
        // 应付票据挂接时，状态为暂存
        if (NetbankConstant.PAY_TYPE_49.equalsIgnoreCase(voucherType.getSysVoucherCode())) {
            fbsBudgetProcessDTO.setStatus(NetbankConstant.STATUE_TEMP);
        } else {
            fbsBudgetProcessDTO.setStatus(nisBillhead.getStatus());
        }
        fbsBudgetProcessDTO.setBillNo(nisBillhead.getBillNo());
        fbsBudgetProcessDTO.setBillId(nisBillhead.getId());
        fbsBudgetProcessDTO.setProcessDate(nisBillhead.getWishPayday());
        fbsBudgetProcessDTO.setCorpId(nisBillhead.getCorpId());
        fbsBudgetProcessDTO.setCurId(nisBillhead.getCurId());
        fbsBudgetProcessDTO.setSettlType(voucherType.getSettlType());
        fbsBudgetProcessDTO.setPayerAcc(nisBillhead.getPayerAcc());
        fbsBudgetProcessDTO.setPayeeAcc(nisBillhead.getPayeeAcc());
        fbsBudgetProcessDTO.setPayeeAccName(nisBillhead.getPayeeAccName());
        fbsBudgetProcessDTO.setPayeeBank(nisBillhead.getPayeeBank());
        fbsBudgetProcessDTO.setPurpose(nisBillhead.getPurpose());
        fbsBudgetProcessDTO.setWishPayday(nisBillhead.getWishPayday());
        fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        fbsBudgetProcessDTO.setInnerSign(getInnerSign(nisBillhead));
        return fbsBudgetProcessDTO;
    }

    /**
     * ERP接口防篡改校验
     *
     * @param billVO 前台传入的单据信息
     * @param billDB 数据库原始单据信息
     */
    private String checkErpData(NisBillheadVO billVO, NisBillhead billDB) {
        if (billVO.getPayerAcc() == null) {
            billVO.setPayerAcc("");
        }
        if (billVO.getPayerAccName() == null) {
            billVO.setPayerAccName("");
        }
        if (billVO.getCurId() == null) {
            billVO.setCurId("");
        }
        if (billVO.getPayeeAcc() == null) {
            billVO.setPayeeAcc("");
        }
        if (billVO.getPayeeAccName() == null) {
            billVO.setPayeeAccName("");
        }
        if (StringUtils.isBlank(billVO.getPayeeBank())) {
            billVO.setPayeeBank("");
        }
        if (billVO.getWishPayday() == null) {
            billVO.setWishPayday(DateUtils.getDateYMD());
        }

        //置空，后面判断使用
        if (billDB.getPayerAcc() == null) {
            billDB.setPayerAcc("");
        }
        if (billDB.getPayerAccName() == null) {
            billDB.setPayerAccName("");
        }
        if (billDB.getCurId() == null) {
            billDB.setCurId("");
        }
        if (billDB.getPayeeAcc() == null) {
            billDB.setPayeeAcc("");
        }
        if (billDB.getPayeeAccName() == null) {
            billDB.setPayeeAccName("");
        }
        if (StringUtils.isBlank(billDB.getPayeeBank())) {
            billDB.setPayeeBank("");
        }
        if (billDB.getWishPayday() == null) {
            billDB.setWishPayday(DateUtils.getDateYMD());
        }

        //来源于ERP的比对关键信息是否被篡改
        if (NetbankConstant.BILLFROM_6.equals(billDB.getBillFrom())) {
            String voucherTypeId = configService.getConfig(ConfigConstant.ERP, "voucherTypeId").getValue();
            String payerAcc = configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue();
            String payMoney = configService.getConfig(ConfigConstant.ERP, "payMoney").getValue();
            String payeeAcc = configService.getConfig(ConfigConstant.ERP, "payeeAcc").getValue();
            String payeeAccName = configService.getConfig(ConfigConstant.ERP, "payeeAccName").getValue();
            String payeeBank = configService.getConfig(ConfigConstant.ERP, "payeeBank").getValue();
            String purpose = configService.getConfig(ConfigConstant.ERP, "purpose").getValue();
            String isForindividual = configService.getConfig(ConfigConstant.ERP, "isForindividual").getValue();
            String billType = configService.getConfig(ConfigConstant.ERP, "billType").getValue();
            String billFlag = configService.getConfig(ConfigConstant.ERP, "billFlag").getValue();
            //付款方式不允许调整
            if (CommonConstant.NO.equals(voucherTypeId) && !billDB.getVoucherTypeId().equals(billVO.getVoucherTypeId())) {
                FcVoucherTypeVO fcVoucherTypeDB = fcVoucherTypeService.getByIdUnion(billDB.getVoucherTypeId());
                FcVoucherTypeVO fcVoucherTypeVO = fcVoucherTypeService.getByIdUnion(billVO.getVoucherTypeId());
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_CHANGE, fcVoucherTypeDB.getVoucherName(), fcVoucherTypeVO.getVoucherName());
            }
            //付款账号不允许调整
            if (CommonConstant.NO.equals(payerAcc)) {
                //账号不相同或者账号相同币种不相同，考虑账号多币种的情况
                if (!billVO.getPayerAcc().equals(billDB.getPayerAcc()) || !billVO.getCurId().equals(billDB.getCurId())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_CHANGE, billDB.getPayerAcc(), billVO.getPayerAcc());
                }
            }
            //付款金额不允许调整
            if (CommonConstant.NO.equals(payMoney) && billDB.getPayMoney().subtract(billVO.getPayMoney()).intValue() != 0) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYMONEY_CHANGE,
                        NumberUtil.formatNumeric(billDB.getPayMoney(), 2), NumberUtil.formatNumeric(billVO.getPayMoney(), 2));
            }
            //收款账号不允许调整
            if (CommonConstant.NO.equals(payeeAcc) && !billVO.getPayeeAcc().equals(billDB.getPayeeAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_CHANGE, billDB.getPayeeAcc(), billVO.getPayeeAcc());
            }
            //收款户名不允许调整
            if (CommonConstant.NO.equals(payeeAccName) && !billVO.getPayeeAccName().equals(billDB.getPayeeAccName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACCNAME_CHANGE, billDB.getPayeeAccName(), billVO.getPayeeAccName());
            }
            //收款银行不允许调整
            if (CommonConstant.NO.equals(payeeBank) && !billVO.getPayeeBank().equals(billDB.getPayeeBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEBANK_CHANGE, billDB.getPayeeBank(), billVO.getPayeeBank());
            }
            //用途不允许调整
            if (CommonConstant.NO.equals(purpose) && !billVO.getPurpose().equals(billDB.getPurpose())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PURPOSE_CHANGE, billDB.getPurpose(), billVO.getPurpose());
            }
            //对私标志不允许调整
            if (CommonConstant.NO.equals(isForindividual) && StringUtils.isNotBlank(billVO.getIsForindividual())
                    && !billDB.getIsForindividual().equals(billVO.getIsForindividual())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_ISFORINDIVIDUAL_CHANGE,
                        dictService.get("sf_yn", billDB.getIsForindividual()),
                        dictService.get("sf_yn", billVO.getIsForindividual()));
            }
            //票据类型不允许调整
            if (CommonConstant.NO.equals(billType) && billDB.getBillType() != billVO.getBillType()) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILLTYPE_CHANGE,
                        billDB.getBillType() == null ? "空" : dictService.get("AB_BILL_TYPE", billDB.getBillType()),
                        billVO.getBillType() == null ? "空" : dictService.get("AB_BILL_TYPE", billVO.getBillType()));
            }
            //票据流通介质不允许调整
            if (CommonConstant.NO.equals(billFlag) && billDB.getBillFlag() != billVO.getBillFlag()) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILLFLAG_CHANGE,
                        billDB.getBillFlag() == null ? "空" : dictService.get("AB_BILL_FLAG", billDB.getBillFlag()),
                        billVO.getBillFlag() == null ? "空" : dictService.get("AB_BILL_FLAG", billVO.getBillFlag()));
            }
        }
        StringBuilder updateMsg = new StringBuilder();
        //付款方式调整
        if (!billDB.getVoucherTypeId().equals(billVO.getVoucherTypeId())) {
            FcVoucherTypeVO fcVoucherTypeDB = StringUtils.isBlank(billDB.getVoucherTypeId()) ? null : fcVoucherTypeService.getByIdUnion(billDB.getVoucherTypeId());
            FcVoucherTypeVO fcVoucherTypeVO = StringUtils.isBlank(billVO.getVoucherTypeId()) ? null : fcVoucherTypeService.getByIdUnion(billVO.getVoucherTypeId());
            updateMsg.append("付款方式：" + (fcVoucherTypeDB == null ? "空" : fcVoucherTypeDB.getVoucherName()) + "更新为" + (fcVoucherTypeVO == null ? "空" : fcVoucherTypeVO.getVoucherName()) + "；");
        }
        //付款账号调整
        if (!billVO.getPayerAcc().equals(billDB.getPayerAcc())) {
            updateMsg.append("付款账号：" + (StringUtils.isBlank(billDB.getPayerAcc()) ? "空" : billDB.getPayerAcc()) + "更新为" + billVO.getPayerAcc() + "；");
        }
        //币种
        if (!billVO.getCurId().equals(billDB.getCurId())) {
            BtCurrencyDTO curDB = StringUtils.isBlank(billDB.getCurId()) ? null : btCurrencyService.getObjectById(billDB.getCurId());
            BtCurrencyDTO curVO = StringUtils.isBlank(billVO.getCurId()) ? null : btCurrencyService.getObjectById(billVO.getCurId());
            updateMsg.append("币种：" + (curDB == null ? "空" : curDB.getCurName()) + "更新为" + (curVO == null ? "空" : curVO.getCurName()) + "；");
        }
        //付款金额调整
        if (billDB.getPayMoney().subtract(billVO.getPayMoney()).intValue() != 0) {
            updateMsg.append("金额：" + NumberUtil.formatNumeric(billDB.getPayMoney(), 2) + "更新为" + NumberUtil.formatNumeric(billVO.getPayMoney(), 2) + "；");
        }
        //收款账号调整
        if (!billVO.getPayeeAcc().equals(billDB.getPayeeAcc())) {
            updateMsg.append("收款账号：" + (StringUtils.isBlank(billDB.getPayeeAcc()) ? "空" : billDB.getPayeeAcc()) + "更新为" + billVO.getPayeeAcc() + "；");
        }
        //收款户名调整
        if (!billVO.getPayeeAccName().equals(billDB.getPayeeAccName())) {
            updateMsg.append("收款账号名称：" + (StringUtils.isBlank(billDB.getPayeeAccName()) ? "空" : billDB.getPayeeAccName()) + "更新为" + billVO.getPayeeAccName() + "；");
        }
        //收款银行调整
        if (!billVO.getPayeeBank().equals(billDB.getPayeeBank())) {
            updateMsg.append("收款银行：" + (StringUtils.isBlank(billDB.getPayeeBank()) ? "空" : billDB.getPayeeBank()) + "更新为" + billVO.getPayeeBank() + "；");
        }
        //用途调整
        if (!billVO.getPurpose().equals(billDB.getPurpose())) {
            updateMsg.append("用途：" + billDB.getPurpose() + "更新为" + billVO.getPurpose() + "；");
        }
        //对私付款标识
        if (StringUtils.isNotBlank(billVO.getIsForindividual()) && !billVO.getIsForindividual().equals(billDB.getIsForindividual())) {
            updateMsg.append("对私付款标识：" + (StringUtils.isBlank(billDB.getIsForindividual()) ? "空" : dictService.get("sf_yn", billDB.getIsForindividual())) + "更新为" + dictService.get("sf_yn", billVO.getIsForindividual()) + "；");
        }
        //期望支付日期
        if (!DateUtils.format(billVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE).equals(DateUtils.format(billDB.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE))) {
            updateMsg.append("期望支付日期：" + DateUtils.format(billDB.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + "更新为" + DateUtils.format(billVO.getWishPayday(), DateUtils.PATTERN_CLASSICAL_SIMPLE) + "；");
        }
        //是否加急
        if (!billVO.getIsQuick().equals(billDB.getIsQuick())) {
            updateMsg.append("是否加急：" + (StringUtils.isBlank(billDB.getIsQuick()) ? "空" : dictService.get("sf_yn", billDB.getIsQuick())) + "更新为" + dictService.get("sf_yn", billVO.getIsQuick()) + "；");
        }
        //是否已付款
        if (StringUtils.isBlank(billVO.getIsPayed())) {
            billVO.setIsPayed(CommonConstant.NO);
        }
        if (!billVO.getIsPayed().equals(billDB.getIsPayed())) {
            updateMsg.append("是否已付款：" + dictService.get("sf_yn", billDB.getIsPayed()) + "更新为" + dictService.get("sf_yn", billVO.getIsPayed()) + "；");
        }
        return updateMsg.toString();
    }

    /**
     * 编辑付款单
     *
     * @param nisBillhead
     */
    public void updateNisBillhead(NisBillheadVO nisBillhead, ICurrentUser user, Boolean isBatch) {
        NisBillhead entity = null;
        String updateMsg = "";
        if(NetbankConstant.IS_EXTERNAL_CORP_0.equals(nisBillhead.getIsExternalCorp())){
            nisBillhead.setIsExternalCorp(null);
        }
        if (isBatch) {
            entity = new NisBillhead();
        } else {
            entity = nisBillheadDao.get(nisBillhead.getId(), true);
            //ERP单据校验单据信息
            updateMsg = this.checkErpData(nisBillhead, entity);
            //退款勾兑打回后，重新提交设置银行流水号为空
            nisBillhead.setBankSerialNo("");
            //获取票据ID
            String billIds = nisBillService.getBillIds(entity.getId());
            //判断更新之前是否是50-应收票据时，解锁票据
            if (!StringUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                //解锁做额度释放
                this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(entity.getId()));
            }
        }

        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(nisBillhead.getIsOpenBill())) {
            String billInfoStr = nisBillhead.getOpenBillInfo();
            if (StringUtils.isBlank(billInfoStr)) {
                String billIds = nisBillService.getBillIds(nisBillhead.getId());
                if (StringUtils.isBlank(billIds)) {
                    logger.error("付款票据关联信息不能为空");
                    throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_NISBILL_NULL);
                }
            }
            if (billInfoStr != null && billInfoStr.contains("{")) {
                AbEmitBillVoDTO voDTO = JsonUtils.parseObject(billInfoStr, AbEmitBillVoDTO.class);
                voDTO.setOpenBillInfo(nisBillhead.getOpenBillInfo());
                if (voDTO == null) {
                    logger.error("付款开票登记信息不能为空");
                    throw new BusinessException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_ABEMITBILL_NULL);
                }
                //暂存付款单之前没关联票据的，新增
                if (StringUtils.isBlank(voDTO.getAid())) {
                    logger.error("关联票据id:" + voDTO.getAid());
                    voDTO.setRmk(StringUtils.isBlank(voDTO.getRmk()) ? nisBillhead.getPurpose() : voDTO.getRmk());
                    saveAbEmitBillNew(nisBillhead.getId(), user, voDTO);
                }
                if (voDTO.getBillMoney() == null || BigDecimal.ZERO.compareTo(voDTO.getBillMoney()) == 0 || (voDTO.getBillMoney().compareTo(nisBillhead.getPayMoney()) != 0)) {
                    throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
                }
            }
            if (StringUtils.isNotBlank(nisBillhead.getOpenBillBatchNo())) {
                String billIds = nisBillService.getBillIds(nisBillhead.getId());
                AbAccrecordsDTO accDto = new AbAccrecordsDTO();
                accDto.setBatchNo(nisBillhead.getOpenBillBatchNo());
                Map<String, Integer> voucherIdBatchDtlNoMap = nisBillhead.getVoucherIdBatchDtlNoMap();
                accDto.setBatchDtlNo(voucherIdBatchDtlNoMap.get(nisBillhead.getId()));
                iAccebillService.updateBillAccByid(accDto, billIds, user);
            }
        }

        if (EmptyUtils.isBlank(nisBillhead.getCreateBy())) {
            nisBillhead.setCreateBy(user.getLoginName());
        }
        if (nisBillhead.getCreateDate() == null) {
            nisBillhead.setCreateDate(DateUtils.getDate());
        }
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        //判断是否挂证书
        if (!StringUtils.isEmpty(nisBillhead.getCaCoding())) {
            SysCaInfoDTO caDto = new SysCaInfoDTO();
            caDto.setRandom(nisBillhead.getRandom());
            caDto.setEncryDate(DateUtils.getDate());
            caDto.setEncrySource(nisBillhead.getEncrySource());
            caDto.setCaCoding(nisBillhead.getCaCoding());
            if (!StringUtils.isEmpty(nisBillhead.getCaInfoId())) {
                caDto.setId(nisBillhead.getCaInfoId());
            }
            caService.saveOrUpdateCaInfo(caDto);
            nisBillhead.setCaInfoId(caDto.getId());
        }
        //内部资金计划内转标识
        String innerSign = getInnerSign(nisBillhead);
        nisBillhead.setInnerSign(innerSign);
        //针对ERP接口推送的付款单可能存在Acc_id为空的情况，由资金系统补全
        if (StringUtils.isEmpty(nisBillhead.getAccId())) {
            BtBankAccDTO payerAcc = btBankAccService.getBankAccByBankAccAndCurId(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
            if (payerAcc != null) {
                nisBillhead.setAccId(payerAcc.getId());
            }
        }
        //编辑付款单，控制原状态为0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateNisBillheadStatus(nisBillhead, oldStatus);
        BeanUtils.copyNotNullProperties(nisBillhead, entity);
        // 编辑后可能暂存或重新提交，按最新金额、账户、币种、支付日期和状态同步限额。
        // 暂存/打回走释放；普通提交校验并占用，批量提交核对已有占用、缺失时补占。
        EnterpriseLimitControlService.requireSuccess(occupyEnterpriseLimit(entity, user, isBatch));
        if (!isBatch) {
            // 初始化联行号信息
            this.bankService.saveBtBank(entity.getPayeeBankCode(), entity.getBankTypeId());
        }
        if (isCheckExternalCorp(nisBillhead) && !EmptyUtils.isEmpty(nisBillhead.getIsExternalCorp())) {
            //保存客商信息
            this.saveIfNotExist(nisBillhead);
        }
        //判断是否是对私付款，对私付款需同步更新对私付款明细
        if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
            //费用报销汇总改造不能传数组，所以临时字段切割设置值
            if (StringUtils.isNotBlank(nisBillhead.getExpenseIdStr())) {
                nisBillhead.setExpenseId(nisBillhead.getExpenseIdStr().split(","));
                nisBillhead.setDifBanks(nisBillhead.getDifBankStr().split(","));
                nisBillhead.setAreaSigns(nisBillhead.getAreaSignStr().split(","));
            }
            //判断是否批量对私付款
            if (!EmptyUtils.isEmptyArray(nisBillhead.getExpenseId())) {
                BigDecimal sumExpenseMoney = BigDecimal.ZERO;
                for (int i = 0; i < nisBillhead.getExpenseId().length; i++) {
                    NisExpenseDetail nisExpenseDetail = nisExpenseDetailService.get(nisBillhead.getExpenseId()[i]);
                    NisExpenseDetailVO vo = new NisExpenseDetailVO();
                    BeanUtils.copyNotNullProperties(nisExpenseDetail, vo);
                    //更新为汇总状态
                    vo.setStatus(ExpenseConstant.STATUE_COLLECT);
                    //失败状态的明细控制交易状态调整
                    if (BdlinkConstant.BANK_FAILED.equals(vo.getVoucherStat())
                            || BdlinkConstant.INTERFACE_FAILED.equals(vo.getVoucherStat())) {
                        vo.setVoucherStat(BdlinkConstant.PRE_SEND);
                        vo.setFirstReturnMsg("");
                        vo.setNextReturnMsg("");
                        vo.setReturnTime(null);
                        //累计失败的金额
                        sumExpenseMoney = sumExpenseMoney.add(nisExpenseDetail.getExpenseMoney());
                    }
                    vo.setDifBank(nisBillhead.getDifBanks()[i]);
                    vo.setAreaSign(nisBillhead.getAreaSigns()[i]);
                    vo.setBillId(nisBillhead.getId());
                    vo.setUpdateBy(user.getLoginName());
                    vo.setUpdateDate(DateUtils.getDate());
                    if (StringUtils.isNotBlank(entity.getBifCode())) {
                        vo.setBifCode(entity.getBifCode());
                    }
                    //费用报销处理，控制费用报销明细状态为已汇总
                    Integer[] oldDtlStatus = {ExpenseConstant.STATUE_COLLECT};
                    nisExpenseDetailService.updateNisExpenseDetailStatus(vo, oldDtlStatus);
                    nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_COLLECT, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
                }
                if (sumExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumExpenseMoney.compareTo(entity.getPayMoney()) != 0) {
                    throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_FAIL_DEAL, entity.getBillNo());
                }
            } else {
                //非费用报销过来的对私付款单，则判断新增或者编辑对私明细
                if (!NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
                    //保存对私明细
                    this.saveNisExpenseDetail(entity, user);
                } else {
                    //校验是否是部分成功部分失败(查询失败明细的总金额，与总金额校验)
                    BigDecimal sumExpenseMoney = nisExpenseDetailService.getSumFailExpenseMoneyByBillId(entity.getId());
                    if (sumExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumExpenseMoney.compareTo(entity.getPayMoney()) != 0) {
                        throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_FAIL_DEAL, entity.getBillNo());
                    } else {
                        //更新失败的明细交易状态为准备发送
                        this.nisExpenseDetailService.updateVoucherStatByBillId(entity.getId());
                    }
                }
            }
        }

        nisBillhead.setSysVoucherCode(voucherType.getSysVoucherCode());
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
            if (!StringUtils.isBlank(nisBillhead.getBillId())) {
                //用独立事务写入关联关系
                NisBillService nisBillService = (NisBillService) ApplicationContextUtil.getContext().getBean("nisBillService");
                //保存票据关联信息
                nisBillService.saveOrUpdateSync(entity.getId(), nisBillhead.getBillId(), nisBillhead.getBillBalance());
                //系统结算方式为50-应收票据时，锁定票据
                //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                String[] abAccrecordsIds = nisBillhead.getBillId().split(CommonConstant.COMMA_SPLIT);
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                        || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill()))) {
                    //如果是批量提交就不再锁定应收票据
                    if (!isBatch) {
                        //保存前先解锁票据
                        this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                        //系统结算方式为50-应收票据时且是否已付款为是时，调用新的接口
                        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                            String billPoolIds = nisBillhead.getBillPoolIds();
                            if (StringUtils.isNotEmpty(billPoolIds)) {
                                HashSet<String> hashSet = new HashSet<>(Arrays.asList(abAccrecordsIds));
                                String[] billPoolIdsArr = billPoolIds.split(",");
                                for (String s : billPoolIdsArr) {
                                    hashSet.remove(s);
                                }
                                abAccrecordsIds = CollectionUtils.isEmpty(hashSet) ? null : (String[]) hashSet.toArray();
                            }
                            if (abAccrecordsIds != null) {
                                //应收票据批量解除占用背书申请流水
                                this.iAccebillService.batchUnHoldEndorse(abAccrecordsIds, user);
                                //应收票据批量占用背书申请流水
                                this.iAccebillService.batchHoldEndorse(abAccrecordsIds, user);
                            }
                        } else {
                            //系统结算方式为50-应收票据时，锁定票据
                            //系统结算方式为49-应付票据,且是否已开票为是时，锁定票据
                            this.iAccebillService.batchLock(abAccrecordsIds, user);
                        }
                        //保持做额度占用
                        this.iAccebillService.batchPayLockMoneyAdd(nisBillService.listNisBillVoByPayId(entity.getId()));
                        // 根据付款单id删除对应的票据付款中间表集合
                        delBillPayMiddleByPayId(entity.getId());

//                        //是否已付款为否且应收票据不需要做BillPayMiddle部分额度占用处理
//                        if(!(NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
//                                && NetbankConstant.ISPAYED_0.equals(nisBillhead.getIsPayed()))) {
//                            // 根据付款单id删除对应的票据付款中间表集合
//                            delBillPayMiddleByPayId(entity.getId());
//                            // 新增票据付款中间表信息
//                            String vvids = nisBillhead.getVvid();
//                            if (StringUtils.isEmpty(vvids)) {
//                                throw new RuntimeException();
//                            }
//                            // 封装票据与挂接付款金额的对应关系
//                            Map<String, String> map = Maps.newHashMap();
//                            String[] vvidsAr = vvids.split(CommonConstant.COMMA_SPLIT);
//                            String[] billBalances = nisBillhead.getBillBalance().split(CommonConstant.COMMA_SPLIT);
//                            for (int a = 0; a < vvidsAr.length; a++) {
//                                map.put(vvidsAr[a], billBalances[a]);
//                            }
//                            addBillPayMiddle(entity, vvids, map);
//                        }
                    }
                }
            }
        } else {
            //更新付款方式非票据，需要删除关联
            // 根据付款单id删除对应的票据付款中间表集合
            delBillPayMiddleByPayId(entity.getId());
            // 删除原付款单挂接的票据信息
            if (!NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) || NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill())) {
                nisBillService.delByPayId(entity.getId());
            }
        }
        //供应链金融：51-应付债权凭证 修改时重写中间表 nis_scf_info 关联
        //51-未开立时凭证ID为空，仅保存平台/产品上下文，待审批通过生成暂存凭证后由审批后方法补写凭证ID
        if (NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
            String scfVoucherIds = CommonConstant.YES.equals(entity.getScfIsOpened()) ? nisBillhead.getScfVoucherIds() : null;
            nisScfInfoService.saveOrUpdateSync(entity.getId(), entity.getBillNo(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE,
                    scfVoucherIds, nisBillhead.getScfPlatformId(), nisBillhead.getScfProductId());
        }
        //供应链金融：52-应收债权凭证 修改时重写中间表 nis_scf_info 关联（保存/提交都触发转让数据保存，凭证id存 voucher_id，转让草稿id存 transfer_id）
        if (NetbankConstant.PAY_TYPE_52.equals(voucherType.getSysVoucherCode())) {
            String voucherIds = nisBillhead.getScfReceivableVoucherIds();
            nisScfInfoService.saveOrUpdateSync(entity.getId(), entity.getBillNo(), NetbankConstant.SCF_VOUCHER_TYPE_RECEIVABLE,
                    voucherIds, nisBillhead.getScfTransferIds(), nisBillhead.getScfPlatformId(), nisBillhead.getScfProductId());
        }
        //写入计划执行
        this.writeBudgetProcess(nisBillhead, user);
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_TEMP;
        String message = NisMessageConstant.NIS_LOG_UPDATE_TEMPSAVE_SUCC;
        //启动审批
        if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
            messageType = NisMessageConstant.MESSAGE_TYPE_SUBMIT;
            message = nisBillhead.getBatch() ? NisMessageConstant.NIS_LOG_BATCH_SUBMIT_SUCC : NisMessageConstant.NIS_LOG_UPDATE_SUBMIT_SUCC;
            //传入付款方式审批条件
            NisBillheadVO nisBillheadVO = new NisBillheadVO();
            BeanUtils.copyNotNullProperties(entity, nisBillheadVO);
            nisBillheadVO.setVoucherType(voucherType.getVoucherType());
            nisBillheadVO.setVoucherName(voucherType.getVoucherName());
            //增加付款账户是否上线审批流（账户属性，账户性质审批流，客户（中信泰富）需要，需求暂未下来暂时屏蔽）
            BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(nisBillheadVO.getPayerAcc());
            if (!EmptyUtils.isEmpty(bankAccInfo)) {
                nisBillheadVO.setIsOnline(bankAccInfo.getIsOnline());
                //nisBillheadVO.setAttributeId(bankAccInfo.getAttributeId());
                //nisBillheadVO.setNatureId(bankAccInfo.getNatureId());
            }
            //调用审批后方法
            getStartHandler().handler(NisWorkFlowConstant.NIS_BILLHEAD, entity.getId(), nisBillheadVO, entity.getCorpId(), user.getId());
        }
        if (StringUtils.isNotBlank(updateMsg)) {
            message = updateMsg;
        }
        //生成日志
        nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, entity.getId(), user.getId());
        String tag = NotificationConstant.NETBANK.PAY_APPLY;
        if (NetbankConstant.BILLFROM_2.equals(entity.getBillFrom())) {
            tag = NotificationConstant.NETBANK.EXPENSE_DEAL;
        }
        notificationService.complete(tag, entity.getId(), user.getId());
    }

    /**
     * 新增票据付款中间表数据
     *
     * @param entity 付款单信息
     * @param vvids  票据从表集合
     * @param map    map集合
     */
    private void addBillPayMiddle(NisBillhead entity, String vvids, Map<String, String> map) {
        if (StringUtils.isNotEmpty(vvids)) {
            // 付款方式为：应收票据、应付票据，选择一张或多张已开票的票据
            BigDecimal payMoney = entity.getPayMoney();
            List<AbBillPayMiddleDTO> abBillPayMidList = iAccebillService.getAbBillPayMiddleList(vvids, null);
            for (AbBillPayMiddleDTO abBillPayMiddleDTO : abBillPayMidList) {
                BigDecimal balance = abBillPayMiddleDTO.getBalance();
                BigDecimal newPayMoney = balance.compareTo(payMoney) >= 0 ? payMoney : balance;
                if ("1".equals(abBillPayMiddleDTO.getNewStmIssuofpprInd()) && "1".equals(abBillPayMiddleDTO.getBlgrdbagcrclPrmtInd())) {
                    newPayMoney = map.get(abBillPayMiddleDTO.getBillId()) == null ? abBillPayMiddleDTO.getBalance() : new BigDecimal(map.get(abBillPayMiddleDTO.getBillId()));
                }
                payMoney = payMoney.subtract(newPayMoney);
                abBillPayMiddleDTO.setId(null);
                abBillPayMiddleDTO.setPayId(entity.getId());
                if ("1".equals(abBillPayMiddleDTO.getNewStmIssuofpprInd()) && "1".equals(abBillPayMiddleDTO.getBlgrdbagcrclPrmtInd())) {
                    // 新系统出票并且允许分包
                    abBillPayMiddleDTO.setPayMoney(new BigDecimal(map.get(abBillPayMiddleDTO.getBillId())));
                    abBillPayMiddleDTO.setBalance(BigDecimal.ZERO);
                } else {
                    abBillPayMiddleDTO.setPayMoney(newPayMoney);
                    abBillPayMiddleDTO.setBalance(balance.subtract(newPayMoney));
                }
                abBillPayMiddleDTO.setCreateDate(new Date());
                abBillPayMiddleDTO.setStatus(0);
                iAccebillService.saveBillPayMiddle(abBillPayMiddleDTO);
                if (payMoney.intValue() == 0) {
                    break;
                }
            }
        }
    }

    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchBackRPA(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.backNisBillhead(id, user, NetbankConstant.BACK_TYPE_6);
        }
    }

    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchUpdateVoucherStatRPA(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.updateVoucherStatRPA(id, user);
        }
    }

    /**
     * 打回付款单
     *
     * @param id
     */
    public void updateVoucherStatRPA(String id, ICurrentUser user) {
        NisBillhead nisBillhead = this.get(id, true);
        nisBillhead.setVoucherStat(BdlinkConstant.PRE_SEND);
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(nisBillhead);
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_RPA_UPDATE, null, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 修改RPA处理异常状态的数据
     *
     * @param ids,user,updateStat
     */
    public void batchUpdateExceptionVoucherStatRPA(String ids, ICurrentUser user,String updateStat) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.updateExceptionVoucherStatRPA(id, user,updateStat);
        }
    }

    /**
     * 修改RPA处理异常状态的数据
     *
     * @param id
     */
    public void updateExceptionVoucherStatRPA(String id, ICurrentUser user,String updateStat) {
        NisBillhead nisBillhead = this.get(id, true);
        nisBillhead.setVoucherStat(updateStat);
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(nisBillhead);
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_RPA_UPDATE, null, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 修改RPA返回信息
     *
     * @param ids,user,updateMsg
     */
    public void batchUpdateReturnMsg(String ids, ICurrentUser user,String updateMsg) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.updateReturnMsg(id, user,updateMsg);
        }
    }

    /**
     * 修改RPA返回信息
     *
     * @param id
     */
    public void updateReturnMsg(String id, ICurrentUser user,String updateMsg) {
        NisBillhead nisBillhead = this.get(id, true);
        nisBillhead.setReturnMsg(updateMsg);
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(nisBillhead);
    }

    /**
     * 审批通过后处理
     *
     * @param nisBillhead
     * @param result
     */
    public void approvePassEnd(NisBillhead nisBillhead, boolean result, ICurrentUser user) {
        this.approvePassEnd(nisBillhead, result, user, false);
    }

    /**
     * 审批通过/打回收尾实现（内部方法，含落地回调"单据已提前置 95"入口）。
     *
     * @param alreadyApproved 单据是否已处于审批通过（status=95，落地限额回调入口）：
     *                        是则终态 CAS 旧状态白名单仅 {95}；否则与 flowCompleted 入口一致（打回 {11}、通过
     *                        {TEMP/BACK/11/100}）。内容执行与 {@link #approvePassEnd(NisBillhead, boolean, ICurrentUser)} 完全相同。
     */
    private void approvePassEnd(NisBillhead nisBillhead, boolean result, ICurrentUser user, boolean alreadyApproved) {
        if (EmptyUtils.isBlank(nisBillhead.getCreateBy())) {
            nisBillhead.setCreateBy(user.getLoginName());
        }
        if (nisBillhead.getCreateDate() == null) {
            nisBillhead.setCreateDate(DateUtils.getDate());
        }
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillhead, vo);
        //更新审批时间
        vo.setApproveDate(DateUtils.getDate());
        if (!result) { //打回
            //更新审批状态
            vo.setStatus(NetbankConstant.STATUE_BACK);
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            vo.setVoucherStat(null);
            //审批付款单，更新原状态控制为待审批（落地回调入口单据已是 95，白名单仅 {95}）
            Integer[] oldStatus = alreadyApproved
                    ? new Integer[]{NetbankConstant.STATUE_CHECK} : new Integer[]{NetbankConstant.STATUE_WAIT_CHECK};
            this.updateNisBillheadStatus(vo, oldStatus); //TODO
            if (!alreadyApproved && !isExpenseBill(nisBillhead)) {
                // 普通付款办理审批打回在本业务事务内释放；费用报销汇总付款单保留占用，
                // 后续由费用报销处理的删除/打回动作统一释放或缩减。
                EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(nisBillhead, user));
            }
            //计划执行状态同步更新
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                    nisBillhead.getBillNo(), NetbankConstant.STATUE_BACK, user); //TODO
        } else { //通过
            BisExcDTO bisExcDTO = null;
            //更新审批状态
            vo.setStatus(NetbankConstant.STATUE_CHECK); //审批通过
            //获取系统结算方式
            FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId());
            vo.setSysVoucherCode(voucherType.getSysVoucherCode());
            vo.setVoucherType(voucherType.getVoucherType());
            vo.setVoucherName(voucherType.getVoucherName());
            vo.setSendMode(voucherType.getSendMode());
            //不记账，且系统结算方式为34-外付网银、02-转付、33-代付、48-代发工资，直接发指令
            if (NetbankConstant.ISACCOUNT_0.equals(voucherType.getIsAccount())) {
                if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_48.equals(voucherType.getSysVoucherCode())) {
                    //如果是否已付款为是的话不生成指令
                    if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                        bisExcDTO = this.saveBisExc(vo, user);
                    }
                    //不记账取付款单号
                    vo.setVoucherNo(nisBillhead.getId());
                }
            } else {
                //记账，且系统方式为02-转付、33-代付数据流转到付款执行，其他付款方式生成记账凭证
                if (!NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                        && !NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                        && !NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())) {
                    //判断是自动记账还是手工记账
                    if (NetbankConstant.ACCOUNTMODE_AUTO.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
                        //自动记账
                        this.submitVoucher(nisBillhead, null, user); //记账
                        vo.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
                        vo.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
                        vo.setVoucherNo(nisBillhead.getVoucherNo());
                    }
                }
            }
            // RPA付款，交易状态默认写入3-准备发送
            if (NetbankConstant.PAY_TYPE_07.equals(voucherType.getSysVoucherCode())) {
                vo.setVoucherStat(BdlinkConstant.PRE_SEND);
            } else {
                vo.setVoucherStat("");
            }
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            //审批付款单，更新原状态控制为待审批（落地回调入口单据已是 95，白名单仅 {95}）
            Integer[] oldStatus = alreadyApproved
                    ? new Integer[]{NetbankConstant.STATUE_CHECK}
                    : new Integer[]{NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK,
                    NetbankConstant.STATUE_WAIT_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS};
            this.updateNisBillheadStatus(vo, oldStatus);//TODO finish ; TCC:commit
            //自动指令调用记账
            if (CommonConstant.YES.equalsIgnoreCase(vo.getSendMode()) && Objects.nonNull(bisExcDTO)) {
                BisExcDTO excDTO = BeanUtils.copyNewInstanceProperties(bisExcDTO, BisExcDTO.class);
                billheadBisExcBackService.sendBisExcAccounting(excDTO, user); //记账;付款指令发送 后记账
            }


            // 应付票据不修改资金计划
            if (!NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                        nisBillhead.getBillNo(), NetbankConstant.STATUE_CHECK, user);//TODO 数据量多了也会卡住; TCC:commit
            }
            //系统结算方式为50-应收票据时，票据背书；系统结算方式为49-应付票据时，生成自开票据
            if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                // 审批通过后修改挂接付款金额为有效
                iAccebillService.updateAbBillPayMiddleStatus(vo.getId(), 1);//TODO ab_bill_pay_middle加联合索引  (PAY_ID+STATUS)
                //获取票据ID
                String billIds = nisBillService.getBillIds(nisBillhead.getId());
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                    //付款方式为应收票据且是否已付款为否的时候，新增修改选票以后占用接口。
                    if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                        //普通票据或票据池票据处理
                        StringBuilder endorseBillIds = endorseByNisBillHead(nisBillhead, user, abAccrecordsIds);
                        if (StringUtils.isNotBlank(endorseBillIds) && StringUtils.isNotBlank(nisBillhead.getPurpose())) {
                            AbAccrecordsDTO dto = new AbAccrecordsDTO();
                            dto.setUpdateDate(new Date());
                            dto.setRmk(nisBillhead.getPurpose());
                            //T24100020240【付款办理】用途内容默认到票据业务备注
                            iAccebillService.updateBillAccByid(dto, StringUtils.join(endorseBillIds, ","), user);
                        }
//                        //把票据id放到票据挂接中间表
//                        if (StringUtils.isNotEmpty(endorseBillIds)) {
//                            nisBillService.saveOrUpdate(nisBillhead.getId(), endorseBillIds.toString(), endorseBillBalances);//TODO NIS_BILL加联合索引 (PAY_ID+BILL_ID)
//                        }
                    }
                } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                    if (NetbankConstant.ISOPENBILL_0.equals(nisBillhead.getIsOpenBill())) {
                        //判断是普通开票还是票据池融资开票
                        if (StringUtils.isNotEmpty(nisBillhead.getBpId()) && StringUtils.isNotEmpty(nisBillhead.getBpId().trim())) {
                            saveEmitFinaApplyByNisPay(nisBillhead, user);
                        } else {

                            String billIdstrs = nisBillService.getBillIds(nisBillhead.getId());
                            if (StringUtils.isBlank(billIdstrs)) {
                                saveAbEmitBill(nisBillhead, user);
                            } else {
                                // 付款开票 改造
                                List<AbAccrecordsDTO> dtos = iAccebillService.getAccrecordsById(billIdstrs);
                                if (!CollectionUtil.isEmpty(dtos)) {
                                    ICurrentUser userCreate = null;
                                    userCreate = StringUtils.isBlank(nisBillhead.getCreateBy()) ? user
                                            : (((userCreate = userService.getByUsername(nisBillhead.getCreateBy())) == null || userCreate.getLoginName() == null) ? user : userCreate);
                                    payOpenBillBatchSubmit(dtos.get(0).getVid(), dtos.get(0).getVvid(), dtos.get(0).getId(), userCreate);
                                    nisBillhead.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BILL_COMPLETE);//付款单完成
                                    nisBillheadDao.update(nisBillhead);
                                    AbAccrecordsDTO updDto = new AbAccrecordsDTO();
                                    updDto.setCreateBy(nisBillhead.getCreateBy());
                                    UserDTO userDTO = userService.getByLoginName(nisBillhead.getCreateBy());
                                    updDto.setCreateName(userDTO.getUsername());
                                    updDto.setExcId("3");//将票据流水付款开票标志更正为其他
                                    iAccebillService.updateBillAccByid(updDto, billIdstrs, user);
                                }
                            }
                        }
                    } else {
                        abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                        //解锁
                        this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                        //判断是否挂资金计划
                        boolean isBudget = false;
                        if (!EmptyUtils.isEmptyList(this.fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo()))) {
                            //计划执行状态同步更新
                            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                                    nisBillhead.getBillNo(), NetbankConstant.STATUE_CHECK, user);
                            isBudget = true;
                        }
                        //修改从表付款状态和付款单id
                        this.iAccebillService.updateBillPayInfo(abAccrecordsIds, NetbankConstant.ISPAYED_1, nisBillhead.getId(), user, isBudget);
                    }
                }
            }
            if (NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
                //供应链金融（51）审批通过后处理
                this.handleScfAfterApproval(nisBillhead, voucherType, user);
            }
            //供应链金融（52）审批通过后的转让执行（executeTransferByIds）另期实现，
            //此处仅处理前后端交互：中间表 nis_scf_info 的 voucher_id 存应收债权凭证ID，审批执行时按凭证ID发起转让
            if (NetbankConstant.PAY_TYPE_52.equals(voucherType.getSysVoucherCode())) {
                //供应链金融（52）审批通过后处理：查询应收债权凭证ID，按凭证ID发起转让（submitTransfer 内部按凭证ID反查暂存转让信息）
                String voucherIds = nisScfInfoService.getVoucherIds(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_RECEIVABLE);
                iScfVoucherReceivableService.submitTransfer(voucherIds);
            }
        }
    }


    /**
     * 自动审批通过后处理
     *
     * @param nisBillhead
     * @param result
     */
    public void approvePassEnd2(NisBillhead nisBillhead, boolean result, ICurrentUser user) {
        if (EmptyUtils.isBlank(nisBillhead.getCreateBy())) {
            nisBillhead.setCreateBy(user.getLoginName());
        }
        if (nisBillhead.getCreateDate() == null) {
            nisBillhead.setCreateDate(DateUtils.getDate());
        }
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillhead, vo);
        //更新审批时间
        vo.setApproveDate(DateUtils.getDate());
        if (!result) { //打回

        } else { //通过
            BisExcDTO bisExcDTO = null;
            //更新审批状态
            vo.setStatus(NetbankConstant.STATUE_CHECK); //审批通过
            //获取系统结算方式
            FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId());
            vo.setSysVoucherCode(voucherType.getSysVoucherCode());
            vo.setVoucherType(voucherType.getVoucherType());
            vo.setVoucherName(voucherType.getVoucherName());
            vo.setSendMode(voucherType.getSendMode());
            //不记账，且系统结算方式为34-外付网银、02-转付、33-代付、48-代发工资，直接发指令
            if (NetbankConstant.ISACCOUNT_0.equals(voucherType.getIsAccount())) {
                if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_48.equals(voucherType.getSysVoucherCode())) {
                    //如果是否已付款为是的话不生成指令
                    if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                        bisExcDTO = this.saveBisExc(vo, user); //TODO 里面会 设置vo.setVoucherStat
                    }
                    //不记账取付款单号
                    vo.setVoucherNo(nisBillhead.getId());
                }
            } else {
                //记账，且系统方式为02-转付、33-代付数据流转到付款执行，其他付款方式生成记账凭证
                if (!NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                        && !NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                        && !NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())) {
                    //判断是自动记账还是手工记账
                    if (NetbankConstant.ACCOUNTMODE_AUTO.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
                        //自动记账
                        this.submitVoucher(nisBillhead, null, user); //记账 TODO
                        vo.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
                        vo.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
                        vo.setVoucherNo(nisBillhead.getVoucherNo());
                    }
                }
            }
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            //审批付款单，更新原状态控制为待审批
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK, NetbankConstant.STATUE_WAIT_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS};
            this.updateNisBillheadStatus(vo, oldStatus);// finish ; TODO 走的是自动审批，可在 try 阶段进行状态更新

            //自动指令调用记账
            if (CommonConstant.YES.equalsIgnoreCase(vo.getSendMode()) && Objects.nonNull(bisExcDTO)) {
                BisExcDTO excDTO = BeanUtils.copyNewInstanceProperties(bisExcDTO, BisExcDTO.class);
                billheadBisExcBackService.sendBisExcAccounting(excDTO, user); //记账;付款指令发送 后记账 TODO
            }

            // 应付票据不修改资金计划
            if (!NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                //计划执行状态同步更新; TODO 这里的状态设置放在commit执行
                //this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo(), NetbankConstant.STATUE_CHECK, user);//TODO 数据量多了也会卡住; TCC:commit
            }
            //系统结算方式为50-应收票据时，票据背书；系统结算方式为49-应付票据时，生成自开票据
            if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                // 审批通过后修改挂接付款金额为有效
                iAccebillService.updateAbBillPayMiddleStatus(vo.getId(), 1);//TODO ab_bill_pay_middle加联合索引  (PAY_ID+STATUS)
                //获取票据ID
                String billIds = nisBillService.getBillIds(nisBillhead.getId());
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                    //付款方式为应收票据且是否已付款为否的时候，新增修改选票以后占用接口。
                    if (!NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
                        //普通票据或票据池票据处理
                        StringBuilder endorseBillIds = endorseByNisBillHead(nisBillhead, user, abAccrecordsIds);
                        if (StringUtils.isNotBlank(endorseBillIds) && StringUtils.isNotBlank(nisBillhead.getPurpose())) {
                            AbAccrecordsDTO dto = new AbAccrecordsDTO();
                            dto.setUpdateDate(new Date());
                            dto.setRmk(nisBillhead.getPurpose());
                            //T24100020240【付款办理】用途内容默认到票据业务备注
                            iAccebillService.updateBillAccByid(dto, StringUtils.join(endorseBillIds, ","), user);
                        }
//                        //把票据id放到票据挂接中间表
//                        if (StringUtils.isNotEmpty(endorseBillIds)) {
//                            nisBillService.saveOrUpdate(nisBillhead.getId(), endorseBillIds.toString(), endorseBillBalances);//TODO NIS_BILL加联合索引 (PAY_ID+BILL_ID)
//                        }
                    }
                } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                    if (NetbankConstant.ISOPENBILL_0.equals(nisBillhead.getIsOpenBill())) {
                        //判断是普通开票还是票据池融资开票
                        if (StringUtils.isNotEmpty(nisBillhead.getBpId()) && StringUtils.isNotEmpty(nisBillhead.getBpId().trim())) {
                            saveEmitFinaApplyByNisPay(nisBillhead, user);
                        } else {
                            String billIdstrs = nisBillService.getBillIds(nisBillhead.getId());
                            if (StringUtils.isBlank(billIdstrs)) {
                                saveAbEmitBill(nisBillhead, user);
                            } else {
                                // 付款开票 改造
                                List<AbAccrecordsDTO> dtos = iAccebillService.getAccrecordsById(billIdstrs);
                                if (!CollectionUtil.isEmpty(dtos)) {
                                    AbAccrecordsDTO updDto = new AbAccrecordsDTO();
                                    updDto.setCreateBy(nisBillhead.getCreateBy());
                                    UserDTO userDTO = userService.getByLoginName(nisBillhead.getCreateBy());
                                    updDto.setCreateName(userDTO.getUsername());
                                    updDto.setExcId("3");//将票据流水付款开票标志更正为其他
                                    iAccebillService.updateBillAccByid(updDto, billIdstrs, user);
                                }
                            }
                        }
                    } else {
                        abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                        //解锁
                        this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                        //判断是否挂资金计划
                        boolean isBudget = false;
                        if (!EmptyUtils.isEmptyList(this.fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo()))) {
                            //计划执行状态同步更新
                            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS,
                                    nisBillhead.getBillNo(), NetbankConstant.STATUE_CHECK, user);
                            isBudget = true;
                        }
                        //修改从表付款状态和付款单id
                        this.iAccebillService.updateBillPayInfo(abAccrecordsIds, NetbankConstant.ISPAYED_1, nisBillhead.getId(), user, isBudget);
                    }
                }
            }
            //供应链金融（51）审批通过后处理
            this.handleScfAfterApproval(nisBillhead, voucherType, user);
        }
    }

    /**
     * 供应链金融（51）审批通过后处理
     * <p>
     * 51-已开立：校验并绑定已关联的开立凭证；
     * 51-未开立：生成暂存应付债权凭证并写入中间表 nis_scf_info。
     * 线上线下标志按付款单"线下付款（不产生指令）"推导：是=线下(0)，否=线上(1)。
     *
     * @param nisBillhead  付款单
     * @param voucherType  系统结算方式
     * @param user         当前用户
     */
    private void handleScfAfterApproval(NisBillhead nisBillhead, FcVoucherTypeVO voucherType, ICurrentUser user) {
        if (voucherType == null) {
            return;
        }
        if (!NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
            return;
        }
        //线上线下标志：线下付款（不产生指令）为"是"时=线下(0)，为"否"时=线上(1)
        String onlineSign = CommonConstant.YES.equals(nisBillhead.getIsPayed()) ? "0" : "1";
        if (NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
            if (CommonConstant.YES.equals(nisBillhead.getScfIsOpened())) {
                //51-已开立：审批通过后校验并绑定已关联的开立凭证
                String voucherIds = nisScfInfoService.getVoucherIds(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE);
                if (StringUtils.isNotBlank(voucherIds)) {
                    ScfPayBindDTO bindDTO = new ScfPayBindDTO();
                    bindDTO.setPayId(nisBillhead.getId());
                    bindDTO.setPayNo(nisBillhead.getBillNo());
                    bindDTO.setVoucherIds(Arrays.asList(voucherIds.split(CommonConstant.COMMA_SPLIT)));
                    bindDTO.setUpdateBy(user.getLoginName());
                    scfVoucherPayService.bindPayableVoucher(bindDTO);
                }
            } else {
                //51-未开立：审批通过后生成暂存应付债权凭证，并写入中间表 nis_scf_info 关联
                //金额直接取付款单金额（scfPayableOpenAmt 是已开立场景选凭证合计，未开立不使用）
                ScfPayGenerateDTO generateDTO = new ScfPayGenerateDTO();
                generateDTO.setPayId(nisBillhead.getId());
                generateDTO.setPayNo(nisBillhead.getBillNo());
                generateDTO.setCorpId(nisBillhead.getCorpId());
                generateDTO.setCurId(nisBillhead.getCurId());
                generateDTO.setVoucherMoney(nisBillhead.getPayMoney());
                generateDTO.setSupplyName(nisBillhead.getPayeeAccName());
                // 供应商统一社会信用代码 = 付款单收款人统一社会信用代码
                generateDTO.setSupplyCreditCode(nisBillhead.getBlRcvpymtpsUnnIdNo());
                generateDTO.setReceiveAccount(nisBillhead.getPayeeAcc());
                generateDTO.setReceiveBank(nisBillhead.getPayeeBank());
                generateDTO.setOnlineSign(onlineSign);
                generateDTO.setCreateBy(user.getLoginName());
                //平台/产品：保存付款单时写入中间表上下文，异步审批后重新读取
                NisScfInfo ctx = nisScfInfoService.getContext(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE);
                if (ctx != null) {
                    generateDTO.setPlatformId(ctx.getPlatformId());
                    generateDTO.setProductId(ctx.getProductId());
                }
                String voucherId = scfVoucherPayService.generateTempPayableVoucher(generateDTO);
                //补写凭证ID，并保留平台/产品上下文，保证回显一致
                nisScfInfoService.saveOrUpdateSync(nisBillhead.getId(), nisBillhead.getBillNo(),
                        NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE, voucherId,
                        ctx == null ? null : ctx.getPlatformId(), ctx == null ? null : ctx.getProductId());
            }
        }
    }

    /**
     * 应付债权凭证开立审批通过后，同步付款单绑定的平台/融资产品（仅当不一致时回写中间表 nis_scf_info）
     * <p>51-未开立：付款单保存时仅写入平台/产品上下文（无凭证ID），审批通过生成暂存凭证并补写凭证ID；
     * 该凭证后续开立审批通过时平台/产品可能被维护过，此处按凭证ID反查绑定付款单并回写，保证两处一致。
     *
     * @param voucherId  应付债权凭证ID
     * @param platformId 凭证当前平台ID
     * @param productId  凭证当前融资产品ID
     */
    public void syncScfPlatformProduct(String voucherId, String platformId, String productId) {
        if (StringUtils.isBlank(voucherId)) {
            return;
        }
        List<NisScfInfo> list = nisScfInfoService.listByVoucherId(voucherId);
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        for (NisScfInfo info : list) {
            boolean changed = false;
            // 凭证平台/产品有值且与付款单绑定的不一致时才回写，避免用空值覆盖已有上下文
            if (StringUtils.isNotBlank(platformId) && !Objects.equals(info.getPlatformId(), platformId)) {
                info.setPlatformId(platformId);
                changed = true;
            }
            if (StringUtils.isNotBlank(productId) && !Objects.equals(info.getProductId(), productId)) {
                info.setProductId(productId);
                changed = true;
            }
            if (changed) {
                nisScfInfoService.saveOrUpdate(info);
            }
        }
    }


    /**
     * 应付票据-出票登记申请
     *
     * @param nisBillhead
     * @param user
     */
    private void saveAbEmitBill(NisBillhead nisBillhead, ICurrentUser user) {
        AbEmitBillDTO abEmit = new AbEmitBillDTO();
        //票据单位，根据付款账号获取所属单位
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(nisBillhead.getPayerAcc());
        if (bankAccInfo != null) {
            abEmit.setCorpId(bankAccInfo.getCorpId());
        } else {
            abEmit.setCorpId(nisBillhead.getCorpId());
        }
        //申请单位
        abEmit.setApplyCorpId(nisBillhead.getCorpId());
        //业务单位
        abEmit.setBusCorpId(nisBillhead.getBusCorpId());
        //业务人员
        abEmit.setBusPersonnel(nisBillhead.getBusPersonnel());
        abEmit.setVoucherId(nisBillhead.getId());
        //出票人信息
        abEmit.setPayeracc(nisBillhead.getPayerAcc());
        abEmit.setPayer(nisBillhead.getPayerAccName());
        abEmit.setPayerBankName(nisBillhead.getPayerBank());
        abEmit.setPayeeacc(nisBillhead.getPayeeAcc());
        abEmit.setBillMoney(nisBillhead.getPayMoney());
        //收票人信息
        abEmit.setPayee(nisBillhead.getPayeeAccName());
        abEmit.setPayeebankno(nisBillhead.getPayeeBankCode());
        abEmit.setPayeeBankName(nisBillhead.getPayeeBank());
        abEmit.setBillType(nisBillhead.getBillType());
        abEmit.setBillFlag(nisBillhead.getBillFlag());
        this.iAccebillService.saveAbEmitBill(abEmit, user);
        //把票据id放到票据挂接中间表
        if (StringUtils.isNotEmpty(abEmit.getBillId())) {
            nisBillService.saveOrUpdate(nisBillhead.getId(), abEmit.getBillId());
        }
    }

    /**
     * 应付票据-出票登记申请
     *
     * @param
     * @param user
     */
    private void saveAbEmitBillNew(String payId, ICurrentUser user, AbEmitBillVoDTO voDTO) {
        voDTO.setVoucherId(payId);
        logger.error("关联付款id:" + payId);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("付款调用开票接口耗时:");
        AbEmitBillVoDTO voDTORetrun = this.iAccebillService.saveAbEmitBillNew(voDTO, user);
        stopWatch.stop();
        logger.info("付款调用开票接口耗时:" + stopWatch.prettyPrint());
        //把票据id放到票据挂接中间表
        if (StringUtils.isNotEmpty(voDTORetrun.getId())) {
            nisBillService.saveOrUpdate(payId, voDTORetrun.getId());
        }
    }

    private void payOpenBillBatchSubmit(String vids, String vvids, String aids, ICurrentUser userVo) {
        iAccebillService.payOpenBillBatchSubmit(vids, vvids, aids, userVo);
    }

    /**
     * 应付票据-票据池出票融资开票申请
     *
     * @param nisBillhead
     * @param user
     */
    private void saveEmitFinaApplyByNisPay(NisBillhead nisBillhead, ICurrentUser user) {
        AbPoolEmitFinaApplyDTO applyDto = new AbPoolEmitFinaApplyDTO();
        //票据单位，根据付款账号获取所属单位
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(nisBillhead.getPayerAcc());
        if (bankAccInfo != null) {
            applyDto.setCorpId(bankAccInfo.getCorpId());
        } else {
            applyDto.setCorpId(nisBillhead.getCorpId());
        }
        applyDto.setBpId(nisBillhead.getBpId());
        applyDto.setVoucherId(nisBillhead.getId());
        applyDto.setBanendrsmtmk(AccebillConstant.AB_BANEM_Y);
        applyDto.setStartDate(new Date());
        //出票人信息
        applyDto.setPayeracc(nisBillhead.getPayerAcc());
        applyDto.setPayer(nisBillhead.getPayerAccName());
        applyDto.setPayerBankName(nisBillhead.getPayerBank());
        applyDto.setBillMoney(nisBillhead.getPayMoney());
        //收票人信息
        applyDto.setPayeeacc(nisBillhead.getPayeeAcc());
        applyDto.setPayee(nisBillhead.getPayeeAccName());
        applyDto.setPayeebankno(nisBillhead.getPayeeBankCode());
        applyDto.setPayeeBankName(nisBillhead.getPayeeBank());
        applyDto.setBillType(nisBillhead.getBillType());
        applyDto.setBillFlag(nisBillhead.getBillFlag());
        this.accebillPoolService.saveEmitFinaApplyByNisPay(applyDto, user);
        //把票据id放到票据挂接中间表
        if (StringUtils.isNotEmpty(applyDto.getId())) {
            nisBillService.saveOrUpdate(nisBillhead.getId(), applyDto.getId());
        }
    }

    /**
     * 普通票据或票据池票据处理
     *
     * @param nisBillhead     付款单信息
     * @param user            操作用户
     * @param abAccrecordsIds 流水id
     * @return 新生成的流水id
     */
    private StringBuilder endorseByNisBillHead(NisBillhead nisBillhead, ICurrentUser user, String[] abAccrecordsIds) {
        if (abAccrecordsIds == null || abAccrecordsIds.length == 0) {
            //传递的参数值不能为空
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }

        //进行消息异步处理
        if (localMassageBillendorsement.equalsIgnoreCase("open")) {
            NisBillheadDTO nisBillheadDTO = new NisBillheadDTO();
            BeanUtils.copyNotNullProperties(nisBillhead, nisBillheadDTO);
            producerMessageRecordService.saveProducerMsgRecord(BusinessType.BILL_ENDORSEMENT, nisBillhead.getId(), "payment-bill-exchange", "bill", nisBillheadDTO, user);
            return null;
        }

        //解锁
        this.iAccebillService.batchUnLock(abAccrecordsIds, user);
        //存在并发问题重新查询一次
        String billIds = nisBillService.getBillIds(nisBillhead.getId());
        abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
        //普通票据流水id
        StringBuilder abAccIds = new StringBuilder();
        //票据池明细id
        StringBuilder poolIds = new StringBuilder();
        int index = 0, poolIdsIndx = 0, abAccIdsIndx = 0;
        Map<Integer, Integer> poolIdsIndxMap = new HashMap<>();
        Map<Integer, Integer> abAccIdsIndxMap = new HashMap<>();
        for (String accId : abAccrecordsIds) {
            //根据id判断是否是票据池明细数据,是则选择的是票据池已入池的数据
            if (this.iAccebillService.isPoolDtl(accId)) {
                if (poolIds.length() > 0) {
                    poolIds.append(CommonConstant.COMMA_SPLIT);
                }
                poolIds.append(accId);
                poolIdsIndxMap.put(poolIdsIndx, index);
                poolIdsIndx++;
            } else {
                if (abAccIds.length() > 0) {
                    abAccIds.append(CommonConstant.COMMA_SPLIT);
                }
                abAccIds.append(accId);
                abAccIdsIndxMap.put(abAccIdsIndx, index);
                abAccIdsIndx++;
            }
            index++;
        }
        String[] abAccIdsArr = abAccIds.toString().split(CommonConstant.COMMA_SPLIT);
        String endorseBillAbAccIds = null;
        String endorseBillPoolIds = null;
        if (StringUtils.isNotEmpty(abAccIdsArr)) {
            String billBalances = nisBillService.getBillBalances(abAccIdsArr, nisBillhead.getId());
            //普通票据背书
            endorseBillAbAccIds = this.iAccebillService.batchEndorse(abAccIdsArr, nisBillhead.getPayeeAccName(), nisBillhead.getPayeeAcc(), nisBillhead.getPayeeBankCode(), nisBillhead.getId(), billBalances, nisBillhead.getWishPayday(), false, user);
        }
        if (!EmptyUtils.isBlank(poolIds.toString())) {
            //票据池背书
            endorseBillPoolIds = this.accebillPoolService.saveAbPoolEndorseInfoByNisPay(poolIds.toString(), nisBillhead.getPayeeAccName(), nisBillhead.getPayeeAcc(), nisBillhead.getPayeeBankCode(), nisBillhead.getWishPayday(), user);
        }

        //设置背书返回明细和挂接明细一致避免挂接余额顺序不准确
        String[] endorseBillIds = new String[abAccrecordsIds.length];
        if (StrUtil.isNotEmpty(endorseBillAbAccIds)) {
            String[] endorseBillAbAccIdsArr = endorseBillAbAccIds.split(CommonConstant.COMMA_SPLIT);
            for (int i = 0; i < endorseBillAbAccIdsArr.length; i++) {
                endorseBillIds[abAccIdsIndxMap.get(i)] = endorseBillAbAccIdsArr[i];
            }
        }
        if (StrUtil.isNotEmpty(endorseBillPoolIds)) {
            String[] endorseBillPoolIdsArr = endorseBillPoolIds.split(CommonConstant.COMMA_SPLIT);
            for (int i = 0; i < endorseBillPoolIdsArr.length; i++) {
                endorseBillIds[poolIdsIndxMap.get(i)] = endorseBillPoolIdsArr[i];
            }
        }
        StringBuilder endorseBillIdsStr = new StringBuilder(ArrayUtil.join(endorseBillIds, ","));
//        return new StringBuilder(ArrayUtil.join(endorseBillIds, ","));
        //把票据id放到票据挂接中间表
        if (StringUtils.isNotEmpty(endorseBillIds)) {
            // 获取票据id一一对应的挂接付款金额
            String endorseBillBalances = nisBillService.getBillBalances(billIds, nisBillhead.getId());
            nisBillService.saveOrUpdateSync(nisBillhead.getId(), endorseBillIdsStr.toString(), endorseBillBalances);
        }
        return endorseBillIdsStr;
    }

    /**
     * 付款指令生成
     *
     * @param nisBillhead
     */
    public BisExcDTO saveBisExc(NisBillheadVO nisBillhead, ICurrentUser user) {
        BtBankAccDTO payerAcc = null;
        //代付
        if (NetbankConstant.PAY_TYPE_33.equals(nisBillhead.getSysVoucherCode())){
            payerAcc = btBankAccService.getBankAccById(nisBillhead.getRealPayerAccId());
        }else {
            //获取付款账号
            payerAcc = btBankAccService.getBankAccById(nisBillhead.getAccId());
        }

        if (payerAcc == null) {
            throw new BusinessException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BANKACC_ISNOTEXIST, nisBillhead.getPayerAcc());
        }
        //离线账号不生成指令
        if (BtBankAccConstant.ISONLINE_0.equals(payerAcc.getIsOnline())) {
            return null;
        }
        //生成付款指令
        BisExcDTO bisExc = new BisExcDTO();
        bisExc.setCbsBillId(nisBillhead.getId());
        bisExc.setCbsBillNo(nisBillhead.getBillNo());
        bisExc.setCorpId(nisBillhead.getCorpId());
        bisExc.setReqDate(DateUtils.getDate());
        //获取系统结算方式
        //FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId());
        //判断是代付
        if (NetbankConstant.PAY_TYPE_33.equals(nisBillhead.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_35.equals(nisBillhead.getSysVoucherCode())) {
            //代付
            if (NetbankConstant.PAY_TYPE_35.equals(nisBillhead.getSysVoucherCode())){
                payerAcc = btBankAccService.getParentAccByAccId(nisBillhead.getAccId(), nisBillhead.getCurId());
                if (payerAcc == null) {
                    throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_PARENTACC_NULL, nisBillhead.getPayerAcc());
                }
            }
            //代付记录中间付款方信息
            bisExc.setMidPayerAccName(nisBillhead.getPayerAccName());
            bisExc.setMidPayerAccNo(nisBillhead.getPayerAcc());
            bisExc.setMidPayerBankName(nisBillhead.getPayerBank());
        }
        //判断是转付
        if (NetbankConstant.PAY_TYPE_02.equals(nisBillhead.getSysVoucherCode())) {
            //转付是需要产生下拨指令的，需要判断付款账号是否存在父账号
            BtBankAccDTO parentAcc = btBankAccService.getParentAccByAccId(nisBillhead.getAccId(), nisBillhead.getCurId());
            if (parentAcc == null) {
                throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_PARENTACC_NULL, nisBillhead.getPayerAcc());
            }
        }
        //付款方信息
        bisExc.setPayerAcc(payerAcc.getBankAcc());
        bisExc.setPayerAccName(payerAcc.getAccName());
        bisExc.setPayerBank(payerAcc.getBankName());
        bisExc.setPayerProv(payerAcc.getProv());
        bisExc.setPayerCity(payerAcc.getCity());
        bisExc.setCurId(nisBillhead.getCurId());
        bisExc.setPayMoney(nisBillhead.getPayMoney());
        bisExc.setVoucherTypeId(nisBillhead.getVoucherTypeId());
        bisExc.setBifCode(payerAcc.getBifCode());
        //收款方信息
        bisExc.setPayeeAcc(nisBillhead.getPayeeAcc());
        bisExc.setPayeeAccName(nisBillhead.getPayeeAccName());
        bisExc.setPayeeBank(nisBillhead.getPayeeBank());
        bisExc.setPayeeBankCode(nisBillhead.getPayeeBankCode());
        bisExc.setPayeeProv(nisBillhead.getProv());
        bisExc.setPayeeCity(nisBillhead.getCity());
        bisExc.setWishPayday(nisBillhead.getWishPayday());
        //交易模式 3-对公 4-对私 默认4
        bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
        if (NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())) {
            bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PRIVATE);
            bisExc.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_0);
            if (NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
                //费用报销
                bisExc.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_1);
            }
        }
        bisExc.setIsQuick(nisBillhead.getIsQuick());
        bisExc.setDifBank(nisBillhead.getDifBank());
        bisExc.setAreaSign(nisBillhead.getAreaSign());
        bisExc.setPurpose(nisBillhead.getPurpose());
        bisExc.setAddWord(nisBillhead.getAddWord());
        bisExc.setVoucherFrom(BdlinkConstant.FROM_NIS_PAY);
        bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_30);
        bisExc.setVoucherStat(BdlinkConstant.WAIT_FILL);
        // 判断付款方式是否自动生成指令
        if (CommonConstant.YES.equalsIgnoreCase(nisBillhead.getSendMode())) {
            //自动生成指令直接设置状态未准备发送，防止消息延迟状态修改不成功
            this.bisExcService.autoSendBisExcNoAccounting(bisExc, user);
            nisBillhead.setVoucherStat(BdlinkConstant.PRE_SEND);
        } else {
            bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
            this.bisExcService.addBisExc(bisExc, user);
            nisBillhead.setVoucherStat(BdlinkConstant.WAIT_FILL);
        }
        return bisExc;
    }

    /**
     * 获取拆分明细
     *
     * @param splitId
     * @return
     */
    public List<NisBillhead> listNisBillheadBySplitId(String splitId) {
        return nisBillheadDao.listNisBillheadBySplitId(splitId);
    }

    /**
     * 逻辑删除付款单
     *
     * @param id
     */
    public void delNisBillhead(String id, ICurrentUser user, boolean isBatch, String chargeBackType) {
        NisBillhead nisBillhead = this.get(id, true);
        delBillPayMiddleByPayId(id);
        //供应链金融：删除付款单时清理中间表 nis_scf_info 关联
        nisScfInfoService.delByPayId(id);
        //合并单据，更新原始单据
        List<NisBillheadVO> listDtl = this.listByMergeId(nisBillhead.getId());
        if (EmptyUtils.isNotEmpty(listDtl)) {
            for (NisBillheadVO vo : listDtl) {
                //判断是否是ERP过来的数据，如果是则反写退单类型
                if (StringUtils.isNotBlank(vo.getErpId()) && NetbankConstant.BILLFROM_6.equals(vo.getBillFrom())) {
                    erpPayInfoService.updateErpPayInfo(vo.getErpId(), chargeBackType);
                }
                //判断是否是待付款池过来的数据，如果是就退回
                if (StringUtils.isNotBlank(vo.getNisBillheadPoolId())) {
                    nisBillheadPoolService.returnNisBillheadPool(vo.getNisBillheadPoolId());
                }
            }
        } else {
            //判断是否是ERP过来的数据，如果是则反写退单类型
            if (StringUtils.isNotBlank(nisBillhead.getErpId()) && NetbankConstant.BILLFROM_6.equals(nisBillhead.getBillFrom())) {
                erpPayInfoService.updateErpPayInfo(nisBillhead.getErpId(), chargeBackType);
            }
            //判断是否是待付款池过来的数据，如果是就退回
            if (StringUtils.isNotBlank(nisBillhead.getNisBillheadPoolId())) {
                nisBillheadPoolService.returnNisBillheadPool(nisBillhead.getNisBillheadPoolId());
            }
        }
        //删除付款单时候，做废已产生的需求单
        List<NisDemandVO> nisDemandVOList = nisDemandService.listByPayBillId(id);
        if (EmptyUtils.isNotEmpty(nisDemandVOList)) {
            for (NisDemandVO nisDemandVO : nisDemandVOList) {
                nisDemandService.defeatNisDemand(nisDemandVO.getId(), user);
            }
        }
        this.deleteNisBillhead(nisBillhead, user, isBatch, false);
    }

    /**
     * 根据付款单id删除对应的票据付款中间表数据
     *
     * @param id 付款单id
     */
    private void delBillPayMiddleByPayId(String id) {
        // 根据付款单id查询出对应的票据付款中间表集合
        List<AbBillPayMiddleDTO> middleList = iAccebillService.getAbBillPayMiddleListByPayId(id);
        if (!CollectionUtils.isEmpty(middleList)) {
            // 删除付款单id对应的所有票据付款中间表数据
            iAccebillService.deleteAbBillPayMiddleByPayId(id);
            // 根据票据从表id查询出对应未删除的最新票据付款中间表数据
            String vvids = "";
            HashMap<String, AbBillPayMiddleDTO> map = new HashMap<>();
            for (AbBillPayMiddleDTO billPayMiddleDTO : middleList) {
                vvids = StringUtils.isEmpty(vvids) ? billPayMiddleDTO.getBillId() : vvids + "," + billPayMiddleDTO.getBillId();
                map.put(billPayMiddleDTO.getBillId(), billPayMiddleDTO);
            }
            List<AbBillPayMiddleDTO> abBillPayMiddleList = iAccebillService.getAbBillPayMiddleList(vvids, null);
            // 将删除的票据中间表记录数据的付款金额添加到上一笔票据中间表的余额中
            for (AbBillPayMiddleDTO abBillPayMiddleDTO : abBillPayMiddleList) {
                AbBillPayMiddleDTO delBillPayMiddle = map.get(abBillPayMiddleDTO.getBillId());
                // 如果删除的这笔数据创建时间最晚，则不用新增余额
                if (abBillPayMiddleDTO.getCreateDate() != null &&
                        abBillPayMiddleDTO.getCreateDate().compareTo(delBillPayMiddle.getCreateDate()) > 0) {
                    iAccebillService.updateAbBillPayMiddleBalance(abBillPayMiddleDTO.getId(), delBillPayMiddle.getPayMoney().doubleValue());
//                    NisBillhead billhead = this.get(abBillPayMiddleDTO.getPayId());
//                    billhead.setBillMoney(billhead.getBillMoney().add(delBillPayMiddle.getPayMoney()));
//                    this.update(billhead);
                }
            }
        }
    }

    public void deleteNisBillhead(NisBillhead entity, ICurrentUser user, boolean isBatch, boolean backDetail) {
        //删除付款单，控制原状态为0-暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(entity, vo);
        vo.setStatus(NetbankConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateNisBillheadStatus(vo, oldStatus);
        // 逻辑删除时仅释放可能占用过限额的外付网银付款单。
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(entity, user));
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, entity.getBillNo());
        // 不是费用报销打回失败明细
        if (!backDetail) {
            //判断是否是对私付款，对私付款需要同步删除费用报销明细
            if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
                List<NisExpenseDetailVO> listDtl = nisExpenseDetailService.listByBillId(entity.getId());
                if (!EmptyUtils.isEmptyList(listDtl)) {
                    for (NisExpenseDetailVO expense : listDtl) {
                        nisExpenseDetailService.delExpenseDetail(expense.getId(), user);
                    }
                }
            }
        }
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        //获取票据ID
        String billIds = nisBillService.getBillIds(entity.getId());
        String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
        if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(entity.getIsOpenBill())))
                && !EmptyUtils.isBlank(billIds)) {
            //解锁
            this.iAccebillService.batchUnLock(abAccrecordsIds, user);
            //解锁做额度释放
            this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(entity.getId()));
            //系统结算方式为50-应收票据时且是否已付款为是时，调用解除占用背书申请流水
            if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISPAYED_1.equals(entity.getIsPayed())) {
                //应收票据批量解除占用背书申请流水
                this.iAccebillService.batchUnHoldEndorse(abAccrecordsIds, user);
            }
            // 释放拆分付款单挂接的票据余额
            delBillPayMiddleByPayId(entity.getId());
            // 删除原付款单挂接的票据信息
            nisBillService.delByPayAndBillId(abAccrecordsIds, new String[]{entity.getId()});
        } else if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
            // 删除原付款单挂接的票据信息
            nisBillService.delByPayAndBillId(abAccrecordsIds, new String[]{entity.getId()});
            //删除票据信息
            iAccebillService.deleteOpenBillInfo(StringUtils.join(abAccrecordsIds, ","), "付款开票删除", user);
        }
        //信贷管理-内部拆借过来的单据，删除时联动调整拆借数据
        if (NetbankConstant.BILLFROM_4.equals(entity.getBillFrom())) {
            inlSendService.backInlSendForNisBillHead(entity.getErpId(), user);
        } else if (NetbankConstant.BILLFROM_8.equals(entity.getBillFrom())) {
            inlSendService.backInlRepayForNisBillHead(entity.getErpId(), user);
        }
        if (backDetail) {
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, NetbankConstant.NETBANK_LOG_BACKDETAIL_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
        } else {
            String message = isBatch ? NisMessageConstant.NIS_LOG_BATCH_DELETE_SUCC : NisMessageConstant.NIS_LOG_DELETE_SUCC;
            //生成日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, entity.getId(), user.getId());
        String tag = NotificationConstant.NETBANK.PAY_APPLY;
        if (NetbankConstant.BILLFROM_2.equals(entity.getBillFrom())) {
            tag = NotificationConstant.NETBANK.EXPENSE_DEAL;
        }
        notificationService.complete(tag, entity.getId(), user.getId());
    }

    /**
     * 拆分保存
     *
     * @param t
     * @param payMoneys
     * @param budgets
     */
    @Transactional
    public void split(NisBillhead t, List<BigDecimal> payMoneys, List<String> budgets, ICurrentUser user) {
        FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
        boolean isBill = NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill()));
        if (isBill) {
            // 如果挂接了票据则拆分后将票据金额置为0，需要重新挂接票据
            t.setBillMoney(BigDecimal.ZERO);
        }
        // 批量获取付款流水号
        List<String> listSequence = SequenceSingleton.listSequence(SequenceEnum.NIS_BILLHEAD, payMoneys.size());
        //生成拆分单据
        for (int i = 0; i < payMoneys.size(); i++) {
            NisBillhead nisBillhead = new NisBillhead();
            BeanUtils.copyNotNullProperties(t, nisBillhead);
            nisBillhead.setBillNo(listSequence.get(i));
            nisBillhead.setId(null);
            nisBillhead.setPayMoney(payMoneys.get(i));
            //保存拆分前单号
            nisBillhead.setSplitId(t.getId());
            //原始拆分前单号
            if (StringUtils.isNotBlank(t.getOrigSplitId())) {
                nisBillhead.setOrigSplitId(t.getOrigSplitId());
            } else {
                nisBillhead.setOrigSplitId(t.getId());
            }
            nisBillhead.setCreateBy(user.getLoginName());
            nisBillhead.setCreateDate(DateUtils.getDate());
            this.save(nisBillhead);
            //生成日志
            nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_SPLIT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
            //判断是否挂计划
            if (!StringUtils.isBlank(budgets.get(i))) {
                List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
                NisBillheadVO nisBillheadVO = new NisBillheadVO();
                BeanUtils.copyNotNullProperties(nisBillhead, nisBillheadVO);
                String[] itemCodes = budgets.get(i).split(",");
                for (String itemCode : itemCodes) {
                    String[] budget = itemCode.split(BudgetConstant.BUDGET_PROCESS_SPLIT);
                    FbsBudgetProcessDTO dto = this.bulidFbsBudgetProcessDTO(nisBillheadVO, user);
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
        }
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
        if (isBill) {
            //获取票据ID
            String billIds = nisBillService.getBillIds(t.getId());
            if (!EmptyUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                //解锁
                this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                //解锁做额度释放
                this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(t.getId()));
                // 释放拆分付款单挂接的票据余额
                delBillPayMiddleByPayId(t.getId());
                // 删除原付款单挂接的票据信息
                nisBillService.delByPayAndBillId(abAccrecordsIds, new String[]{t.getId()});
            }
        }
        //更新原单状态为删除
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(t, vo);
        vo.setStatus(NetbankConstant.STATUE_DEL);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        //拆分付款单，控制原状态为 0暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        this.updateNisBillheadStatus(vo, oldStatus);
        // 释放原单挂接的计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, t.getId(), user.getId());
    }

    /**
     * 撤销拆分
     *
     * @param id
     */
    public void splitCancel(String id, ICurrentUser user) {
        NisBillhead nisBillhead = this.get(id, true);
        //根据拆分单号获取拆分前单据
        NisBillhead old = nisBillheadDao.getUniqueBy("id", nisBillhead.getSplitId());
        //获取拆分明细
        List<NisBillhead> list = nisBillheadDao.listNisBillheadBySplitId(old.getId());
        //校验拆分明细单状态必须都是暂存或者打回才允许撤销拆分
        if (!EmptyUtils.isEmptyList(list)) {
            String billNos = "";
            for (NisBillhead entity : list) {
                if (!NetbankConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                        && !NetbankConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())) {
                    if (StringUtils.isBlank(billNos)) {
                        billNos = entity.getBillNo();
                    } else {
                        billNos = billNos + CommonConstant.COMMA_SPLIT + entity.getBillNo();
                    }
                }
            }
            if (StringUtils.isNotBlank(billNos)) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLITCANCEL_VALID, billNos);
            }
        }
        //还原拆分前单据
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(old, vo);
        vo.setStatus(NetbankConstant.STATUE_TEMP);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        //撤销拆分，控制原付款单状态为-2已删除
        Integer[] oldStatus = {NetbankConstant.STATUE_DEL};
        this.updateNisBillheadStatus(vo, oldStatus);
        // 还原拆分前单据的计划
        fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, old.getBillNo(), NetbankConstant.STATUE_TEMP, user);
        //更新拆分明细单状态
        for (NisBillhead t : list) {
            NisBillheadVO dtlVo = new NisBillheadVO();
            BeanUtils.copyNotNullProperties(t, dtlVo);
            dtlVo.setStatus(NetbankConstant.STATUE_DEL);
            dtlVo.setSplitId(null);
            dtlVo.setOrigSplitId(null);
            dtlVo.setErpId(null);
            dtlVo.setUpdateBy(user.getLoginName());
            dtlVo.setUpdateDate(DateUtils.getDate());
            //撤销拆分，控制拆分明细单状态为0暂存/-1打回
            Integer[] oldStatusDtl = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateNisBillheadStatus(dtlVo, oldStatusDtl);
            // 是否拆分明细单挂接的计划
            fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());

            FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(),true);
            // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
            if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
                //获取票据ID
                String billIds = nisBillService.getBillIds(t.getId());
                if (!EmptyUtils.isBlank(billIds)) {
                    String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                    //解锁
                    this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                    //解锁做额度释放
                    this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(t.getId()));
                    // 释放拆分付款单挂接的票据余额
                    delBillPayMiddleByPayId(t.getId());
                    // 删除原付款单挂接的票据信息
                    nisBillService.delByPayAndBillId(abAccrecordsIds, new String[]{t.getId()});
                }
            }
        }

        FcVoucherType voucherType = fcVoucherTypeService.get(old.getVoucherTypeId(),true);
        // 49-应付票据（是否已开票为是） 或者 50-应收票据时，锁定票据
        if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(old.getIsOpenBill())))) {
            //获取票据ID
            String billIds = nisBillService.getBillIds(old.getId());
            if (!EmptyUtils.isBlank(billIds)) {
                String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                //锁定
                this.iAccebillService.batchLock(abAccrecordsIds, user);
            }
        }
        //完成通知提醒
        notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, nisBillhead.getId(), user.getId());
    }

    /**
     * 付款单合并
     *
     * @param ids
     */
    @Transactional
    public void merge(String ids, ICurrentUser user) {
        List<NisBillhead> list = new ArrayList<NisBillhead>();
        String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
        //获取第一笔合并单据
        NisBillhead t1 = nisBillheadDao.get(idAtr[0], true);
        //获取第一笔合并单据的资金计划
        List<FbsBudgetProcessDTO> t1ListBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t1.getBillNo());
        //数据校验
        for (String id : idAtr) {
            NisBillhead t = nisBillheadDao.get(id, true);
            FcVoucherType voucherType = fcVoucherTypeService.getById(t.getVoucherTypeId());
            list.add(t);
            //非原始单拆分单据不可进行合并
            if (!StringUtils.isEmpty(t.getSplitId()) && !StringUtils.isEmpty(t1.getSplitId()) &&EmptyUtils.isNotEmpty(t.getOrigSplitId())&& !t.getOrigSplitId().equals(t1.getOrigSplitId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_NOT_MERGE);
            }
            //合并单据不可进行合并
            if (this.isMergeBill(t.getId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_MERGE);
            }
            //系统结算方式为50-应收票据，不可进行合并
			/*if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILL_MERGE);
			}*/
            //单据来源为承兑汇票，不可进行合并
            if (NetbankConstant.BILLFROM_3.equals(t.getBillFrom())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILlFROM_BILL_MERGE);
            }
            if (t1.getId().equals(t.getId())) {
                continue;
            }
            //付款单位一致
            if (!t1.getCorpId().equals(t.getCorpId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_SAME);
            }
            //付款方式一致
            if (!t1.getVoucherTypeId().equals(t.getVoucherTypeId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_SAME);
            }
            //如果是转付，且要记账，则需判断银行账号是否一致
            if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
                if (!t1.getDownAccId().equals(t.getDownAccId())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_DOWNACC_SAME);
                }
            }
            //付款账号一致
            if ((StringUtils.isNotEmpty(t1.getPayerAcc()) && !t1.getPayerAcc().equals(t.getPayerAcc()))
                    || (StringUtils.isNotEmpty(t.getPayerAcc()) && !t.getPayerAcc().equals(t1.getPayerAcc()))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_SAME);
            }
            //付款币别一致
            if (!t1.getCurId().equals(t.getCurId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_SAME);
            }
            //收款人一致
            if (!t1.getPayeeAccName().equals(t.getPayeeAccName())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACCNAME_SAME);
            }
            //收款账号一致
            if (!t1.getPayeeAcc().equals(t.getPayeeAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEACC_SAME);
            }
            //收款开户行一致
            if (!StringUtils.isEmpty(t1.getPayeeBank()) && !t1.getPayeeBank().equals(t.getPayeeBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYEEBANK_SAME);
            }
            //对私标志一致
            if (!t1.getIsForindividual().equals(t.getIsForindividual())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_FORINDIVIDUAL_SAME);
            }
            //加急标志一致
            if (!t1.getIsQuick().equals(t.getIsQuick())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_QUICK_SAME);
            }
            List<FbsBudgetProcessDTO> tListBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());
            if ((!EmptyUtils.isEmptyList(tListBudget) && EmptyUtils.isEmptyList(t1ListBudget)) ||
                    (!EmptyUtils.isEmptyList(t1ListBudget) && EmptyUtils.isEmptyList(tListBudget))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_IS_FBSBUDGET);
            }
        }
        BigDecimal payMoney = BigDecimal.ZERO;
        //合并附件
        List<String> attachmentList = new ArrayList<String>();
        //合并计划
        List<FbsBudgetProcessDTO> listBudget = new ArrayList<FbsBudgetProcessDTO>();
        //合并票据
        // String billIdMerge = "";
        NisBillhead nisBillhead = new NisBillhead();
        for (NisBillhead t : list) {
            BeanUtils.copyNotNullProperties(t, nisBillhead);
            // 根据付款单id删除对应的票据付款中间表数据
            delBillPayMiddleByPayId(t.getId());
            //合计金额
            payMoney = payMoney.add(t.getPayMoney());
            if (!StringUtils.isBlank(t.getAttachmentId())) {
                attachmentList.add(t.getAttachmentId());
            }
            List<FbsBudgetProcessDTO> listProcess = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());
            if (!EmptyUtils.isEmptyList(listProcess)) {
                listBudget.addAll(listProcess);
                //释放合并单据的资金计划
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());
            }
            // 票据号（新的付款单需要重新挂接票据）
            // billIdMerge += nisBillService.getBillIds(t.getId()) + CommonConstant.COMMA_SPLIT;
            // 删除挂接的票据信息
            FcVoucherType voucherType = fcVoucherTypeService.getById(t.getVoucherTypeId());
            // 49-应付票据（是否已开票为是） 或者 50-应收票据时，锁定票据
            if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
                t.setBillMoney(BigDecimal.ZERO);
                String billIds = nisBillService.getBillIds(t.getId());
                if (StringUtils.isNotEmpty(billIds)) {
                    nisBillhead.setBillMoney(BigDecimal.ZERO);
                    String[] billArr = billIds.split(CommonConstant.COMMA_SPLIT);
                    //解锁
                    this.iAccebillService.batchUnLock(billArr, user);
                    //解锁做额度释放
                    this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(t.getId()));
                    // 删除付款单挂接的票据信息
                    nisBillService.delByPayAndBillId(billArr, new String[]{t.getId()});
                }
            }
        }
        //合并附件
        if (!EmptyUtils.isEmptyList(attachmentList)) {
            nisBillhead.setAttachmentId(attachmentService.mergeGroupIds(attachmentList));
        }
        nisBillhead.setId(null);
        nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        nisBillhead.setSplitId(null);
        nisBillhead.setOrigSplitId(null);
        //汇总金额
        nisBillhead.setPayMoney(payMoney);
        nisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
        nisBillhead.setCreateBy(user.getLoginName());
        nisBillhead.setCreateDate(DateUtils.getDate());
        //生成合并单据
        this.save(nisBillhead);
        // if (!StringUtils.isBlank(billIdMerge)) {
        //     //保存票据关联信息
        //     nisBillService.saveOrUpdate(nisBillhead.getId(), billIdMerge);
        // }
        //生成日志
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NetbankConstant.NETBANK_LOG_MERGE_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
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
                    fbsBudgetProcessDTO.setProcessDate(nisBillhead.getWishPayday());
                }
                if (EmptyUtils.isEmpty(fbsBudgetProcessDTO.getWishPayday())) {
                    fbsBudgetProcessDTO.setWishPayday(nisBillhead.getWishPayday());
                }
            }
            listBudget = new ArrayList<FbsBudgetProcessDTO>();
            for (Entry<String, FbsBudgetProcessDTO> entry : mapBudget.entrySet()) {
                listBudget.add(entry.getValue());
            }
            for (FbsBudgetProcessDTO fbsBudgetProcessDTO : listBudget) {
                fbsBudgetProcessDTO.setId(null);
                fbsBudgetProcessDTO.setBillId(nisBillhead.getId());
                fbsBudgetProcessDTO.setBillNo(nisBillhead.getBillNo());
                fbsBudgetProcessDTO.setCreateBy(nisBillhead.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(nisBillhead.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);
            }
            fbsBudgetProcessService.writeBudgetProcess(listBudget, user);
        }
        //更新合并明细单状态
        for (NisBillhead t : list) {
            NisBillheadVO vo = new NisBillheadVO();
            BeanUtils.copyNotNullProperties(t, vo);
            vo.setStatus(NetbankConstant.STATUE_DEL);
            //保存合并单号
            vo.setMergeId(nisBillhead.getId());
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            //合并付款单，控制原状态为0暂存/-1打回
            Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
            this.updateNisBillheadStatus(vo, oldStatus);
            //完成通知提醒
            notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, t.getId(), user.getId());
        }
    }

    /**
     * 撤销合并
     *
     * @param ids
     */
    public void mergeCancel(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //获取合并明细
            List<NisBillhead> list = nisBillheadDao.listBy("mergeId", id);
            if (list == null || list.size() <= 0) {
                //非撤销合并单据，不可进行撤销合并操作
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGECANCEL_VALID);
            } else {
                NisBillhead t = this.get(id, true);
                FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId(), true);

                // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
                if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                        || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(t.getIsOpenBill())))) {
                    //获取票据ID
                    String billIds = nisBillService.getBillIds(t.getId());
                    if (!EmptyUtils.isBlank(billIds)) {
                        String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                        //解锁
                        this.iAccebillService.batchUnLock(abAccrecordsIds, user);
                        //解锁做额度释放
                        this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(t.getId()));
                    }
                }

                for (NisBillhead n : list) {
                    NisBillheadVO dtlVo = new NisBillheadVO();
                    BeanUtils.copyNotNullProperties(n, dtlVo);
                    dtlVo.setStatus(NetbankConstant.STATUE_TEMP);
                    dtlVo.setMergeId(null);
                    dtlVo.setUpdateBy(user.getLoginName());
                    dtlVo.setUpdateDate(DateUtils.getDate());
                    //撤销合并付款单，控制原付款单的原状态为-2已删除
                    Integer[] oldStatus = {NetbankConstant.STATUE_DEL};
                    this.updateNisBillheadStatus(dtlVo, oldStatus);
                    //撤销合并还原预算执行
                    fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, n.getBillNo(), BudgetConstant.STATUE_TEMP, user);
                    // 49-应付票据（是否已开票为是） 或者 50-应收票据时，解锁票据
                    if ((NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                            || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_1.equals(n.getIsOpenBill())))) {
                        //获取票据ID
                        String billIds = nisBillService.getBillIds(n.getId());
                        if (!EmptyUtils.isBlank(billIds)) {
                            String[] abAccrecordsIds = billIds.split(CommonConstant.COMMA_SPLIT);
                            //重新锁定
                            this.iAccebillService.batchLock(abAccrecordsIds, user);
                        }
                    }
                }
                //删除合并单据
                NisBillheadVO vo = new NisBillheadVO();
                BeanUtils.copyNotNullProperties(t, vo);
                vo.setStatus(NetbankConstant.STATUE_DEL);
                vo.setUpdateBy(user.getLoginName());
                vo.setUpdateDate(DateUtils.getDate());
                //撤销合并付款单，控制合并付款单的原状态为0暂存/-1打回
                Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
                this.updateNisBillheadStatus(vo, oldStatus);
                //删除合并预算
                fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, t.getBillNo());
                //完成通知提醒
                notificationService.complete(NotificationConstant.NETBANK.PAY_APPLY, t.getId(), user.getId());
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
        List<NisBillhead> list = nisBillheadDao.listBy("mergeId", id);
        return !EmptyUtils.isEmptyList(list);
    }

    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<NisBillheadVO> listByMergeId(String mergeId) {
        return this.nisBillheadDao.listByMergeId(mergeId);
    }

    /**
     * 付款执行发送
     *
     * @param nisBillhead
     */
    public void sendNisBillhead(NisBillhead nisBillhead, ICurrentUser user) {
        NisBillhead entity = this.get(nisBillhead.getId(), true);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        //获取付款账号
        BtBankAccDTO payerAcc = new BtBankAccDTO();
        if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
            //转付，付款方取付款执行界面选择的银行账号
            payerAcc = btBankAccService.getBankAccByIdAndCurId(nisBillhead.getAccId(), entity.getCurId());
            //更新付款单的下拨账号
            entity.setDownAccId(payerAcc.getId());
            entity.setCurId(payerAcc.getCurId());
        } else {
            if (EmptyUtils.isEmpty(nisBillhead.getAccId())) {
                nisBillhead.setAccId(nisBillhead.getRealPayerAccId());
            }
            //代付取发送界面所选银行账号对应的父账号
            payerAcc = btBankAccService.getBankAccByIdAndCurId(nisBillhead.getAccId(), entity.getCurId());
            entity.setBifCode(payerAcc.getBifCode());
        }
        //生成付款指令
        BisExcDTO bisExc = new BisExcDTO();
        bisExc.setCbsBillId(entity.getId());
        bisExc.setCbsBillNo(entity.getBillNo());
        bisExc.setCorpId(entity.getCorpId());
        bisExc.setReqDate(DateUtils.getDate());
        //付款方信息
        bisExc.setPayerAcc(payerAcc.getBankAcc());
        bisExc.setPayerAccName(payerAcc.getAccName());
        bisExc.setPayerBank(payerAcc.getBankName());
        bisExc.setPayerProv(payerAcc.getProv());
        bisExc.setPayerCity(payerAcc.getCity());
        bisExc.setBifCode(payerAcc.getBifCode());
        bisExc.setCurId(entity.getCurId());
        bisExc.setPayMoney(entity.getPayMoney());
        bisExc.setVoucherTypeId(entity.getVoucherTypeId());
        bisExc.setCounterAcc(btBankAccService.getBlnctrAcc(payerAcc.getBankAcc(), nisBillhead.getCurId()));
        //收款方信息
        bisExc.setPayeeAcc(entity.getPayeeAcc());
        bisExc.setPayeeAccName(entity.getPayeeAccName());
        bisExc.setPayeeBank(entity.getPayeeBank());
        bisExc.setPayeeBankCode(entity.getPayeeBankCode());
        bisExc.setPayeeProv(entity.getProv());
        bisExc.setPayeeCity(entity.getCity());
        bisExc.setWishPayday(entity.getWishPayday());
        //交易模式 3-对公 4-对私 默认4
        bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PUBLIC);
        if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
            bisExc.setTransMode(BdlinkConstant.TRANS_MODE_PRIVATE);
            bisExc.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_0);
            if (NetbankConstant.BILLFROM_2.equals(entity.getBillFrom())) {
                //费用报销
                bisExc.setVoucherFromPrivate(BdlinkConstant.VOUCHER_FROM_PRIVATE_1);
            }
        }
        bisExc.setIsQuick(entity.getIsQuick());
        bisExc.setDifBank(nisBillhead.getDifBank());
        bisExc.setAreaSign(nisBillhead.getAreaSign());
        bisExc.setPurpose(entity.getPurpose());
        bisExc.setAddWord(entity.getAddWord());
        bisExc.setVoucherFrom(BdlinkConstant.FROM_NIS_PAY);
        bisExc.setBusOriType(BdlinkConstant.BUSORITYPE_30);
        bisExc.setCmdType(BdlinkConstant.CMDTYPE_MANUAL);
        bisExc.setVoucherStat(BdlinkConstant.WAIT_FILL);
        //代付记录中间付款方信息
        if (NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())) {
            bisExc.setMidPayerAccName(entity.getPayerAccName());
            bisExc.setMidPayerAccNo(entity.getPayerAcc());
            bisExc.setMidPayerBankName(entity.getPayerBank());
        }
        //是否已付款为否，发送产生付款指令
        if (NetbankConstant.ISPAYED_0.equals(entity.getIsPayed())) {
            // 判断付款方式是否自动生成指令
            if (CommonConstant.YES.equalsIgnoreCase(voucherType.getSendMode())) {
                this.bisExcService.autoSendBisExc(bisExc, user);
                entity.setVoucherStat(BdlinkConstant.PRE_SEND);
            } else {
                this.bisExcService.addBisExc(bisExc, user);
                entity.setVoucherStat(BdlinkConstant.WAIT_FILL);
            }
        } else {
            //是否已付款为是，且自动记账，则记账
            //判断是自动记账还是手工记账
            if (NetbankConstant.ACCOUNTMODE_AUTO.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
                this.submitVoucher(entity, bisExc, user);
                entity.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
            }
        }
        if (StringUtils.isBlank(entity.getVoucherNo())) {
            entity.setVoucherNo(entity.getId());
        }
        this.updateEntity(entity);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_EXECUTE_SEND, NetbankConstant.NETBANK_LOG_EXECUTE_SEND_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 打回付款单
     *
     * @param id
     */
    public void backNisBillhead(String id, ICurrentUser user, String backType) {
        NisBillhead nisBillhead = this.get(id, true);
        nisBillhead.setStatus(NetbankConstant.STATUE_BACK);
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        String message = "";
        Integer messageType = NisMessageConstant.MESSAGE_TYPE_EXECUTE_BACK;
        if (NetbankConstant.BACK_TYPE_2.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_EXECUTE_BACK_SUCC;
        } else if (NetbankConstant.BACK_TYPE_3.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_BATCH_EXECUTE_BACK_SUCC;
        } else if (NetbankConstant.BACK_TYPE_4.equalsIgnoreCase(backType)) {
            nisBillhead.setOrderNo(null);
            nisBillhead.setTallySign(null);
            message = NetbankConstant.NETBANK_LOG_CMSTALLY_BACK_SUCC;
            messageType = NisMessageConstant.MESSAGE_TYPE_CMSTALLY_BACK;
        } else if (NetbankConstant.BACK_TYPE_5.equalsIgnoreCase(backType)) {
            message = NetbankConstant.NETBANK_LOG_REFUNDBLENDING_BACK_SUCC;
            messageType = NisMessageConstant.MESSAGE_TYPE_REFUNDBLENDING_BACK;
        }else if (NetbankConstant.BACK_TYPE_6.equalsIgnoreCase(backType)) {
            messageType = NisMessageConstant.MESSAGE_TYPE_RPA_BACK;
        }
        this.saveOrUpdate(nisBillhead);
        // 打回时仅外付网银付款单需释放本次占用，重新提交时再按最新单据校验。
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(nisBillhead, user));
        //更新资金计划
        fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo(), NetbankConstant.STATUE_BACK, user);
        //生成日志(手工记账打回无需再生成日志，账务本身已生成)
        if (!NetbankConstant.BACK_TYPE_4.equalsIgnoreCase(backType)) {
            nisMessageService.saveMessage(nisBillhead.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        //NisBillhead nisBillhead = nisBillheadDao.get(workFlowResult.getBusinessKey());
        NisBillhead nisBillhead = nisBillheadDao.get(workFlowResult.getBusinessKey(), true); //获取流程分类 netbankPay，用户，业务id
        boolean pass = Boolean.TRUE.equals(workFlowResult.isPass());
        // 落地限额（银行端限额服务）：企业端审批通过后，仅对"审批通过后会生成线上银行支付指令"的付款判断是否
        // 达到落地限额（见 isLandingLimitReached）；命中则 markApprovePassForLanding 把单据<b>提前置为审批通过
        // （status 11→95）</b>，但"审批通过的内容"（发指令/记账/资金计划置通过）<b>暂不执行</b>——延后到内管端
        // 落地限额审批通过后的回调 completeTranApprove(->approvePassEnd) 才真正执行，落地标识 LANDING_LIMIT_RESULT
        // 置 0（待执行）作幂等依据（银行未通过前不动资金计划、不发指令）。
        if (pass
                && isOnlineInstruction(nisBillhead, fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId()))
                && isLandingLimitReached(nisBillhead)) {
            // 企业端审批已通过（workflow 已 complete）：此处把单据提前置为 95（内容不执行，见 markApprovePassForLanding），
            // 等内管端审批通过回调后由 approvePassEnd 执行指令发送等收尾内容（回调前单据为 95 且 LANDING_LIMIT_RESULT=0）。
            markApprovePassForLanding(nisBillhead, user);
            nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT,
                    "已提交银行端落地限额审批，审批通过后自动生效", NisMessageConstant.BUS_TYPE_PAY, user);
            return;
        }
        this.approvePassEnd(nisBillhead, pass, user);
        Integer messageType = pass ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = pass ? NisMessageConstant.NIS_LOG_APP_SUCC : NisMessageConstant.NIS_LOG_APP_BACK;
        //生成日志
        nisMessageService.saveMessage(nisBillhead.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 落地限额处理结果回调收尾（银行端限额服务 -> 企业端）：
     * 按处理结果完成审批通过（pass=true）/ 打回（pass=false）收尾。
     * 与 flowCompleted 的区别：不再判断落地限额（处理单已生成，直接按结果收尾）。
     * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
     * <p>
     * 落地预置单据（markApprovePassForLanding 已提前置 95、审批内容未执行）回调时，按 LANDING_LIMIT_RESULT=0 识别：
     * 先把落地标识条件翻转（仅 result=0 可翻）为 A/R 作为并发互斥闸门——翻转成功者才收尾；失败（0 行）说明
     * 已被并发回调收尾，抛状态变更异常（单据已 95 且 result 非 0，重试将落入 handler 幂等分支，不会双收尾）。
     * 翻转成功后单据已是 95，直接以"已提前置 95"入口调 approvePassEnd（终态 CAS 白名单 {95}），不再假复位 95→11。
     * approvePassEnd 内部不打审批日志（flowCompleted 是调用后补记），回调收尾后此处按原口径补记处理结果日志。
     */
    public void completeTranApprove(String businessId, boolean pass, ICurrentUser user) {
        NisBillhead entity = nisBillheadDao.get(businessId, true);
        if (entity == null) {
            throw new BusinessException(NetbankMessageEnum.BILL_CHECKED);
        }
        if (user == null) {
            user = buildFallbackUser(entity);
        }
        if (isLandingPending(entity)) {
            // 落地标识条件翻转（仅 LANDING_LIMIT_RESULT=0 可翻）作互斥闸门：0 行 = 已被并发回调翻转收尾，中止
            if (nisBillheadDao.updateLandingLimitResultIfPending(entity.getId(),
                    pass ? EnterpriseLimitControlService.LANDING_RESULT_AGREE
                         : EnterpriseLimitControlService.LANDING_RESULT_REJECT) < 1) {
                throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
            // 单据已是 95：以"已提前置 95"入口直接执行收尾内容
            this.approvePassEnd(entity, pass, user, true);
        } else {
            this.approvePassEnd(entity, pass, user, false);
        }
        // 审批日志与 flowCompleted 同构（approvePassEnd 内部不打），落地预置/普通 11 两条回调路径统一在此补记
        Integer messageType = pass ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = pass ? "银行端落地限额审批通过，付款生效" : "银行端落地限额审批打回";
        nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 落地限额拒绝并完成付款单打回后，幂等释放当前付款尝试的占用。
     *
     * @param billId 付款单ID
     * @param user 回调操作人
     */
    public void releaseEnterpriseLimitForBillhead(String billId, ICurrentUser user) {
        if (StringUtils.isBlank(billId)) {
            return;
        }
        NisBillhead entity = this.get(billId, true);
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(entity, user));
    }

    /**
     * 是否"线上生成银行支付指令"的付款办理（落地限额仅对该类付款生效，线下不走）。
     * 口径与 {@link #approvePassEnd(NisBillhead, boolean, ICurrentUser)} 通过分支 saveBisExc 发指令
     * 触发条件同源：不记账（isAccount=0）且系统结算方式为 02-转付、33-代付、35、34-外付网银、48-代发工资
     * 且未付款。若线上口径后续变化，在此处同步调整。
     */
    private boolean isOnlineInstruction(NisBillhead nisBillhead, FcVoucherTypeVO voucherType) {
        if (voucherType == null || nisBillhead == null) {
            return false;
        }
//        if (!NetbankConstant.ISACCOUNT_0.equals(voucherType.getIsAccount())) {
//            return false;
//        }
        if (!NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())
                && !NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                && !NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())
                && !NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode())
                && !NetbankConstant.PAY_TYPE_48.equals(voucherType.getSysVoucherCode())) {
            return false;
        }
        //如果是否已付款为是的话不生成指令
        return !NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed());
    }

    /**
     * 落地校验：付款办理是否达到落地限额（人工审批/回调入口，取当前登录用户组上下文）。
     * context == null（非线上/账户不在线等）= 不落地，返回 false。
     * 内管判断失败（服务不可用/返回异常）时抛错中止审批收尾，不放行。
     */
    private boolean isLandingLimitReached(NisBillhead nisBillhead) {
        return isLandingLimitReached(nisBillhead, getCurrentUser());
    }

    /**
     * 落地校验：仅系统结算方式34-外付网银交由内管判断（判定与占用同走 NIS_BILLHEAD 维度，
     * 自动支付预置单与人工落地同维度）。非外付网银或 context == null（账户不在线等）= 不落地；
     * 内管判断失败（无返回/异常）抛错中止，不放行。
     *
     * @param user 操作人（后台任务无当前登录用户时传单据创建人，如自动支付策略任务）
     */
    private boolean isLandingLimitReached(NisBillhead nisBillhead, ICurrentUser user) {
        if (!isExternalOnlineBankPayment(nisBillhead)) {
            // 付款办理落地预判断只适用于系统结算方式34-外付网银，其他付款方式不调用限额服务。
            return false;
        }
        PaymentLimitContextDto ctx = buildEnterpriseLimitContext(nisBillhead, user);
        if (ctx == null) {
            return false;
        }
        PaymentLimitResultDto<Boolean> result = enterpriseLimitControlService.isLandingApprovalRequired(ctx);
        // 落地预判断失败码统一映射企业端限额消息枚举，避免降级为未知错误。
        EnterpriseLimitControlService.requireSuccess(result);
        if (result.getData() == null) {
            throw new BusinessException("付款办理落地限额判断失败: 内管未返回判断结果");
        }
        return result.getData();
    }

    /**
     * 落地限额命中：审批通过即把单据提前置为 95（记审批信息），但审批通过的内容（指令/记账/资金计划）延后到
     * 内管落地审批回调 {@link #completeTranApprove} 再执行。状态更新守卫与 approvePassEnd 通过分支同源
     * （同一旧状态白名单，并发已变则抛状态变更异常）；落地标识单独落库（HQL 不含此列）。
     */
    private void markApprovePassForLanding(NisBillhead nisBillhead, ICurrentUser user) {
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillhead, vo);
        // 提前置为审批通过，但内容（指令/记账/资金计划）不执行，待回调 approvePassEnd
        vo.setStatus(NetbankConstant.STATUE_CHECK);
        vo.setApproveDate(DateUtils.getDate());
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        // 旧状态白名单与 approvePassEnd(pass) 通过分支一致
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK,
                NetbankConstant.STATUE_WAIT_CHECK, NetbankConstant.STATUE_TALLY_CHECKPASS};
        this.updateNisBillheadStatus(vo, oldStatus);
        // 落地标识=待执行（内容未跑，等内管回调）。updateNisBillheadStatus 的 HQL 不含此列，单独落库。
        nisBillheadDao.updateLandingLimitResult(nisBillhead.getId(), EnterpriseLimitControlService.LANDING_RESULT_PENDING);
    }

    /**
     * 内管端回调可能不带审批人 user，取单据 updateBy（兜底 createBy）作为操作人。
     */
    private ICurrentUser buildFallbackUser(NisBillhead entity) {
        String loginName = StringUtils.isNotBlank(entity.getUpdateBy()) ? entity.getUpdateBy() : entity.getCreateBy();
        UserDTO user = new UserDTO();
        user.setId(loginName);
        user.setLoginName(loginName);
        user.setUsername(loginName);
        return user;
    }

    /**
     * 组装落地限额上下文（供内管 isLandingApprovalRequired 判定）：仅线上付款（账户在线、金额>0）返回 ctx，否则 null。
     * 来源业务类型固定 NIS_BILLHEAD——自动支付预置单与人工落地同维度、不单独建维度。
     */
    private PaymentLimitContextDto buildEnterpriseLimitContext(NisBillhead entity, ICurrentUser user) {
        if (entity == null || StringUtils.isBlank(entity.getId())
                || entity.getPayMoney() == null || entity.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }
        // 前置账户在线校验：仅线上付款账号才会产生落地限额校验；离线/账号不存在则视为不落地
        BtBankAccDTO payerAcc = btBankAccService.getBankAccById(entity.getAccId());
        if (payerAcc == null || !CommonConstant.YES.equals(payerAcc.getIsOnline())) {
            return null;
        }
        PaymentLimitContextDto context = new PaymentLimitContextDto();
        context.setTenantId(resolveEnterpriseLimitTenantId(user));
        context.setSourceBizType(EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD);
        context.setSourceBizId(entity.getId());
        context.setBusinessNo(entity.getBillNo());
        context.setUnitId(entity.getCorpId());
        context.setAccountId(entity.getAccId());
        context.setAccountMask(entity.getPayerAcc());
        // 限额跨服务契约传企业端原币种 ID，管理端按 ID 完成规则匹配和汇率折算。
        context.setCurrencyId(entity.getCurId());
        context.setAmount(entity.getPayMoney());
        context.setBusinessDate(entity.getWishPayday());
        context.setBizStatus(String.valueOf(entity.getStatus()));
        context.setOperatorId(user == null ? null : user.getId());
        context.setOperatorName(user == null ? null : user.getUsername());
        return context;
    }

    private boolean isLandingPending(NisBillhead entity) {
        return entity != null
                && NetbankConstant.STATUE_CHECK.equals(entity.getStatus())
                && EnterpriseLimitControlService.LANDING_RESULT_PENDING.equals(entity.getLandingLimitResult());
    }

    /**
     * 优先使用线程/Dubbo租户上下文，兼容后台任务用户对象未携带tenantId的场景。
     */
    private String resolveEnterpriseLimitTenantId(ICurrentUser user) {
        String tenantId = UserContextHelper.helper().getTenantId();
        return StringUtils.isNotBlank(tenantId) || user == null ? tenantId : user.getTenantId();
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<WorkFlowPersistence> dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.NIS_BILLHEAD, dataTablesInput);
        List<String> businessIdList = workFlowBusiness.getBusinessIdList();

        StringBuilder businessSql = new StringBuilder();
        List<String> bursarBillNoList = new ArrayList<>();
        //如果会计凭证号是从excel复制出来的，格式是通过空格隔开的，做一个批量查询
        if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("likes_bursarBillNo"))) {
            String bursarBillNo = dataTablesInput.getFormdata().get("likes_bursarBillNo").toString();
            bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
            if (bursarBillNoList.size() >= 2) {
                businessSql.append(" and ").append(SqlUtil.getInSql(bursarBillNoList, "n.bursar_Bill_No", true));
                dataTablesInput.getFormdata().remove("likes_bursarBillNo");
            }
        }
        if (!EmptyUtils.isEmpty(businessIdList)) {
            businessSql.append(" and " + SqlUtil.getInSql(businessIdList, "n.id", true));
        }
        DataTablesOutput dp = this.nisBillheadDao.dataTableApprove(dataTablesInput, businessSql.toString());
        dp = this.setJoinField(dp, "query", null);
        if (dp != null && dp.getData() != null) {
            List<NisBillheadVO> list = dp.getData();
            for (NisBillheadVO nisBillhead : list) {
                if (NetbankConstant.PAY_TYPE_34.equals(nisBillhead.getDirectPayFlag())) {
                    BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
                    if (bal != null) {
                        nisBillhead.setBalance(bal.getBal());
                    }
                }
            }
        }
        return workFlowBusiness.merge(dp);
    }

    /**
     * 自动记账
     *
     * @param nisBillhead
     * @param bisExc
     */
    public void submitVoucher(NisBillhead nisBillhead, BisExcDTO bisExc, ICurrentUser user) {
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        //获取付款账号
        BtBankAccDTO bankAcc = new BtBankAccDTO();
        if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
            //转付，付款方取银行账号对应的父账号（网点对应的下拨账号）
            BtBankAccDTO prenAcc = btBankAccService.getParentAccByAccId(nisBillhead.getDownAccId(), nisBillhead.getCurId());
            if (prenAcc != null) {
                bankAcc = btBankAccService.getBankAccByIdAndCurId(prenAcc.getId(), nisBillhead.getCurId());
            }
        } else if (NetbankConstant.PAY_TYPE_33.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_35.equals(voucherType.getSysVoucherCode())) {
            //代付取付款指令对应的银行账号
            bankAcc = btBankAccService.getBankAccByBankAccAndCurId(bisExc.getPayerAcc(), bisExc.getCurId());
            if (!EmptyUtils.isEmpty(bankAcc) && StringUtils.isEmpty(bankAcc.getCounterAccId())) {
                bankAcc = btBankAccService.getBankAccById(bankAcc.getId());
            }
        }
        NisAccbookVO accBook = new NisAccbookVO();
        accBook.setBillNo(nisBillhead.getBillNo());
        accBook.setBillId(nisBillhead.getId());
        accBook.setFcAcc(innerAccountService.getByBlnctrAcc(nisBillhead.getPayerAcc()));
        accBook.setBankAcc(bankAcc);
        //内转
        accBook.setPayeeAcc(nisBillhead.getPayeeAcc());
        accBook.setVoucherTypeId(nisBillhead.getVoucherTypeId());
        accBook.setWishPayday(nisBillhead.getWishPayday());
        accBook.setIsQuick(nisBillhead.getIsQuick());
        accBook.setPurpose(nisBillhead.getPurpose());
        accBook.setPayMoney(nisBillhead.getPayMoney());
        accBook.setBusOriType(FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get());
        //内转
        if (NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode())) {
            accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_UNIT_COMMON_PAYMENT_SAME.get());
        } else {
            accBook.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_UNIT_COMMON_PAYMENT_OUT.get());
        }
        //非内转的付款方式需要保存外单位信息
        if (!NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode())) {
            ExternalCorpDTO externalCorpDTO = externalCorpService.findByCorpIdAndName(nisBillhead.getCorpId(), nisBillhead.getPayeeAccName());
            if (externalCorpDTO != null) {
                accBook.setExternalId(externalCorpDTO.getId());
            }
            accBook.setExternalCorp(nisBillhead.getPayeeAccName());
            accBook.setExternalAcc(nisBillhead.getPayeeAcc());
            accBook.setBank(nisBillhead.getPayeeBank());
            accBook.setSysBankCode(nisBillhead.getPayeeBankCode());
            accBook.setProv(nisBillhead.getProv());
            accBook.setCity(nisBillhead.getCity());
        }
        //内转标识
        accBook.setInnerSign(nisBillhead.getInnerSign());
        String voucherNo = nisAccbookService.submitVoucher(accBook, user); //自动记账
        //记录凭证号
        nisBillhead.setVoucherNo(voucherNo);
        //更新记账标识
        nisBillhead.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        nisBillhead.setStatus(NetbankConstant.STATUE_TALLY_CHECKPASS);
        this.update(nisBillhead);
    }

    /**
     * 批量提交付款单
     *
     * @param nisBillhead
     * @param random
     * @param sourceData
     * @param caCoding
     * @param user
     */
    public void batchSubmitNisBillhead(NisBillheadVO nisBillhead, String random, String sourceData, String caCoding, ICurrentUser user) {
        nisBillhead.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
        nisBillhead.setBatch(true);
        if (CommonConstant.NO.equals(nisBillhead.getIsPayed())) {
            //期望支付日期小于系统当前日期，则默认更新为系统当前日期
            if (nisBillhead.getWishPayday().before(DateUtils.getDateYMD())) {
                nisBillhead.setWishPayday(DateUtils.getDateYMD());
                //计划执行日期同步调整
                this.fbsBudgetProcessService.updateBudgetProcessDate(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo(), DateUtils.getDateYMD(), user);
            }
        }
        this.updateNisBillhead(nisBillhead, user, true);
        //付款预警增加
        this.warnExecuteNis(nisBillhead, user);
    }

    /**
     * 批量删除付款单
     *
     * @param ids
     */
    public void batchDelNisBillhead(String ids, ICurrentUser user, String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.delNisBillhead(id, user, true, chargeBackType);
        }
    }

    /**
     * 批量暂挂付款单
     *
     * @param ids
     */
    public void pendingBatchNisBillhead(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.pendingNisBillhead(id, user);
        }
    }

    /**
     * 暂挂付款单
     *
     * @param id
     * @param user
     */
    public void pendingNisBillhead(String id, ICurrentUser user) {
        NisBillhead nisBillhead = this.get(id, true);
        //暂挂付款单，控制原状态为0-暂存/-1打回
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP, NetbankConstant.STATUE_BACK};
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillhead, vo);
        vo.setStatus(NetbankConstant.STATUE_PENDING);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateNisBillheadStatus(vo, oldStatus);
        nisBillhead.setStatus(NetbankConstant.STATUE_PENDING);
        // 暂挂状态按当前口径需要占用限额，使用变更后的状态校验，避免按原暂存/打回状态释放。
        EnterpriseLimitControlService.requireSuccess(occupyEnterpriseLimit(nisBillhead, user, false));
    }

    /**
     * 批量暂挂付款单
     *
     * @param ids
     */
    public void relievePendingBatch(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.relievePendingNisBillhead(id, user);
        }
    }

    /**
     * 解除暂挂付款单
     *
     * @param id
     * @param user
     */
    public void relievePendingNisBillhead(String id, ICurrentUser user) {
        NisBillhead nisBillhead = this.get(id, true);
        //解除暂挂付款单，控制原状态为-3暂挂
        Integer[] oldStatus = {NetbankConstant.STATUE_PENDING};
        NisBillheadVO vo = new NisBillheadVO();
        BeanUtils.copyNotNullProperties(nisBillhead, vo);
        vo.setStatus(NetbankConstant.STATUE_TEMP);
        vo.setUpdateBy(user.getLoginName());
        vo.setUpdateDate(DateUtils.getDate());
        this.updateNisBillheadStatus(vo, oldStatus);
        // 解除暂挂后仅外付网银付款单释放暂挂期间的限额占用。
        EnterpriseLimitControlService.requireSuccess(releaseEnterpriseLimitIfApplicable(nisBillhead, user));
    }

    /**
     * 校验报销明细是否部分成功部分失败
     *
     * @param billId
     * @return
     */
    public String validDetail(String billId) {
        String result = "0";
        NisBillheadVO nisBillhead = this.getByIdUnion(billId);
        //获取成功的明细金额
        BigDecimal sumSuccessExpenseMoney = nisExpenseDetailService.getSumSuccessExpenseMoneyByBillId(nisBillhead.getId());
        if (sumSuccessExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumSuccessExpenseMoney.compareTo(nisBillhead.getPayMoney()) != 0) {
            result = NumberUtil.formatNumeric(sumSuccessExpenseMoney, 2);
        }
        return result;
    }

    /**
     * 费用报销处理-打回
     *
     * @param id
     */
    public void backDetail(String id, ICurrentUser user, boolean isFail) {
        //根据付款单ID获取失败的费用报销明细
        List<NisExpenseDetailVO> listExpenseDetail = null;
        if (isFail) {
            listExpenseDetail = nisExpenseDetailService.listByBillIdFailure(id);
        } else {
            listExpenseDetail = nisExpenseDetailService.listByBillId(id);
        }
        if (!EmptyUtils.isEmptyList(listExpenseDetail)) {
            NisBillhead nisBillhead = this.get(id, true);
            //获取成功的明细金额
            BigDecimal sumSuccessExpenseMoney = nisExpenseDetailService.getSumSuccessExpenseMoneyByBillId(nisBillhead.getId());
            if (sumSuccessExpenseMoney.compareTo(BigDecimal.ZERO) > 0 && sumSuccessExpenseMoney.compareTo(nisBillhead.getPayMoney()) != 0) {
                //部分成功部分失败的情况，更新原付款单的金额为成功明细的总金额
                FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
                if (CommonConstant.YES.equals(voucherType.getIsAccount())) {
                    //记账复核通过
                    nisBillhead.setStatus(CommonConstant.STATUE_DRAW_CHECK);
                } else {
                    //不记账
                    nisBillhead.setStatus(CommonConstant.STATUE_CHECK);
                }
                nisBillhead.setPayMoney(sumSuccessExpenseMoney);
                nisBillhead.setUpdateBy(user.getLoginName());
                nisBillhead.setUpdateDate(DateUtils.getDate());
                this.update(nisBillhead);
                PaymentLimitContextDto limitContext = buildEnterpriseLimitContext(nisBillhead, user);
                if (limitContext != null) {
                    // 失败明细打回后，按保留的成功金额缩减占用并转为成功用量，保留已支付部分的限额统计。
                    EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.retainSuccessfulAmount(limitContext));
                }
                BisExcDTO bisExcDTO = bisExcService.getBisExcByCbsBillNo(nisBillhead.getBillNo(), BdlinkConstant.FROM_NIS_PAY);
                if (bisExcDTO != null) {
                    bisExcDTO.setPayMoney(sumSuccessExpenseMoney);
                    bisExcDTO.setVoucherStat(BdlinkConstant.SUCCESS);
                    bisExcDTO.setUpdateBy(user.getLoginName());
                    bisExcDTO.setUpdateDate(DateUtils.getDate());
                    bisExcService.updateBisExc(bisExcDTO, user);
                }
            } else {
                //逻辑删除付款单据
                this.deleteNisBillhead(nisBillhead, user, false, true);
            }
            for (NisExpenseDetailVO detail : listExpenseDetail) {
                //交易成功的明细不处理
                if (!BdlinkConstant.SUCCESS.equals(detail.getVoucherStat()) && !BdlinkConstant.WAIT_RESULT.equals(detail.getVoucherStat())) {
                    nisExpenseDetailService.backExpenseDetail(detail.getId(), user);
                }
            }
        } else {
            if (isFail) {
                //不存在交易失败的费用报销明细，打回失败
                throw new ParameterException(NetbankMessageEnum.NIS_EXPENSE_MESSAGE_FAILUREDTL_NULL);
            }
        }
    }

    /**
     * 导入
     *
     * @param list
     */
    public void saveImport(List<NisBillheadVO> list, ICurrentUser user) {
        //判断是否挂计划,计划科目校验
        if (this.isBudgetSel()) {
            HashMap<String, FbsBudgetProcessDTO> map = new HashMap<>();
            for (NisBillheadVO nisBillhead : list) {
                if (EmptyUtils.isEmpty(nisBillhead.getCorpId()) || EmptyUtils.isEmpty(nisBillhead.getCorpId().trim())) {
                    continue;
                }
                //判断单位是否上报计划
                if (CommonConstant.YES.equals(nisBillhead.getIsBudget()) && !EmptyUtils.isEmpty(nisBillhead.getItemCode()) && StringUtils.isNotBlank(nisBillhead.getItemCode()[0])) {
                    String key = nisBillhead.getCorpId() + nisBillhead.getItemCode()[0] + nisBillhead.getWishPayday();
                    FbsBudgetProcessDTO dto = null;
                    if (map.containsKey(key)) {
                        dto = map.get(key);
                        dto.setProcessMoney(dto.getProcessMoney().add(nisBillhead.getPayMoney()));
                        map.put(key, dto);
                    } else {
                        dto = new FbsBudgetProcessDTO();
                        dto.setCorpId(nisBillhead.getCorpId());
                        dto.setItemCode(nisBillhead.getItemCode()[0]);
                        dto.setProcessMoney(nisBillhead.getPayMoney());
                        dto.setProcessDate(nisBillhead.getWishPayday());
                        map.put(key, dto);
                    }
                }
            }
            //资金计划余额校验
            List<FbsBudgetProcessDTO> listBudget = new ArrayList<>();
            for (String key : map.keySet()) {
                listBudget.add(map.get(key));
            }
            for (FbsBudgetProcessDTO processDTO : listBudget) {
                //判断科目是否受控
                if (CommonConstant.YES.equals(processDTO.getIsControl())) {
                    FbsBudgetProcessCommonDTO dto = fbsBudgetService.getFbsBudgetProcessByItemCode(processDTO.getCorpId(), processDTO.getItemCode(), processDTO.getProcessDate(), null, null);
                    //计划执行规则为5-先执行后控制时无需校验
                    if (!BudgetConstant.PROCESS_RULE_5.equals(dto.getProcessRule())) {
                        //计划执行科目余额控制方式
                        String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
                            //受本科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE);
                            }
                        }
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
                            //受上级科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getSuperItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE);
                            }
                        }
                        if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
                            //受大科目控制
                            if (processDTO.getProcessMoney().doubleValue() > dto.getTopItemMoney().doubleValue()) {
                                throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE);
                            }
                        }
                    }
                }
            }
        }
        HashMap<String, NisBillheadVO> bankMap = new HashMap<>();
        // 先批量获取付款流水号
        List<String> listSequence = SequenceSingleton.listSequence(SequenceEnum.NIS_BILLHEAD, list.size());
        int index = 0;
        for (NisBillheadVO nisBillhead : list) {
            if (EmptyUtils.isEmpty(nisBillhead.getCorpId()) || EmptyUtils.isEmpty(nisBillhead.getCorpId().trim())) {
                continue;
            }
            nisBillhead.setBillFrom(NetbankConstant.BILLFROM_1);
            nisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
            //是否已付款默认设置为否
            //nisBillhead.setIsPayed(NetbankConstant.ISPAYED_0);
            nisBillhead.setBillNo(listSequence.get(index));
            this.saveNisBillheadForImport(nisBillhead, user);
            index++;
            if (StringUtils.isNotBlank(nisBillhead.getPayeeBankCode())) {
                if (!bankMap.containsKey(nisBillhead.getPayeeBankCode())) {
                    bankMap.put(nisBillhead.getPayeeBankCode(), nisBillhead);
                }
            }
        }
        for (String payeeBankCode : bankMap.keySet()) {
            // 初始化联行号信息
            this.bankService.saveBtBank(payeeBankCode, bankMap.get(payeeBankCode).getBankTypeId());
        }
    }

    /**
     * 导入保存
     *
     * @param nisBillhead
     */
    public void saveNisBillheadForImport(NisBillheadVO nisBillhead, ICurrentUser user) {
        NisBillhead entity = new NisBillhead();
        BeanUtils.copyNotNullProperties(nisBillhead, entity);
        //判断是否挂证书
        if (!StringUtils.isEmpty(nisBillhead.getCaCoding())) {
            SysCaInfoDTO caDto = new SysCaInfoDTO();
            caDto.setRandom(nisBillhead.getRandom());
            caDto.setEncryDate(DateUtils.getDate());
            caDto.setEncrySource(nisBillhead.getEncrySource());
            caDto.setCaCoding(nisBillhead.getCaCoding());
            entity.setCaInfoId(caService.saveOrUpdateCaInfo(caDto));
        }
        if (StringUtils.isBlank(entity.getBillNo())) {
            entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
        }
        if (StringUtils.isBlank(nisBillhead.getErpId())) {
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
        }
        super.saveEntity(entity);
        // 导入入口同样同步单据限额；当前批量导入为暂存状态，走释放分支，待提交时再占用。
        EnterpriseLimitControlService.requireSuccess(occupyEnterpriseLimit(entity, user, false));
        //保存票据关联信息
        if (!StringUtils.isBlank(nisBillhead.getBillId())) {
            nisBillService.saveOrUpdate(entity.getId(), nisBillhead.getBillId());
        }
        //判断是否是对私付款，对私付款需生成对私付款明细
        if (NetbankConstant.FORINDIVIDUAL_1.equals(entity.getIsForindividual())) {
            //判断是否批量对私付款
            if (!EmptyUtils.isEmptyArray(nisBillhead.getExpenseId())) {
                for (int i = 0; i < nisBillhead.getExpenseId().length; i++) {
                    NisExpenseDetail nisExpenseDetail = nisExpenseDetailService.get(nisBillhead.getExpenseId()[i]);
                    //更新为汇总状态
                    nisExpenseDetail.setStatus(ExpenseConstant.STATUE_COLLECT);
                    nisExpenseDetail.setVoucherStat(BdlinkConstant.PRE_SEND);
                    nisExpenseDetail.setFirstReturnMsg("");
                    nisExpenseDetail.setNextReturnMsg("");
                    nisExpenseDetail.setReturnTime(null);
                    nisExpenseDetail.setDifBank(nisBillhead.getDifBanks()[i]);
                    nisExpenseDetail.setAreaSign(nisBillhead.getAreaSigns()[i]);
                    nisExpenseDetail.setBillId(entity.getId());
                    nisExpenseDetail.setUpdateBy(user.getLoginName());
                    nisExpenseDetail.setUpdateDate(DateUtils.getDate());
                    nisExpenseDetail.setBifCode(entity.getBifCode());
                    nisExpenseDetailService.saveOrUpdate(nisExpenseDetail);
                    nisMessageService.saveMessage(nisExpenseDetail.getId(), NisMessageConstant.MESSAGE_TYPE_COLLECT, "", NisMessageConstant.BUS_TYPE_EXPENSE, user);
                }
            } else {
                //非费用报销过来的对私付款单，则判断新增或者编辑对私明细
                if (!NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
                    //保存对私明细
                    this.saveNisExpenseDetail(entity, user);
                }
            }
        }
        //写入计划执行
        nisBillhead.setId(entity.getId());
        nisBillhead.setBillNo(entity.getBillNo());
        this.writeBudgetProcess(nisBillhead, user);
        //生成日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_TEMP, NisMessageConstant.NIS_LOG_IMPORT_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(NisBillheadVO vo) {
        return null;
    }

    @Override
    public void verifyHandler(List<NisBillheadVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            //显示已付款是标识
            String showIsPayed = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue();
            boolean isBudgetSel = this.isBudgetSel();
            //单位集合
            HashMap<String, CorpDTO> corpMap = new HashMap<>();
            //付款方式集合
            HashMap<String, FcVoucherType> voucherTypeMap = new HashMap<>();
            //币种集合
            HashMap<String, String> curMap = new HashMap<>();
            //银行账号集合
            HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
            //内部账号集合
            HashMap<String, FcAccDTO> fcAccMap = new HashMap<>();
            //内转收款账号集合
            HashMap<String, List<FcAccDTO>> fcAccPayeeMap = new HashMap<>();
            //单位计划上报集合
            HashMap<String, Boolean> reportBudgetMap = new HashMap<>();
            //获取人民币
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            //总数
            int count = list.size();
            //总金额
            BigDecimal sum = BigDecimal.ZERO;
            for (NisBillheadVO vo : list) {
                sum = sum.add(vo.getPayMoney());
                List<String> msgList = new ArrayList<>();
                // 判断单位在系统中是否存在
                CorpDTO corp = corpMap.get(vo.getCorpName());
                if (corp == null) {
                    corp = this.corpService.getCorpIdByName(vo.getCorpName());
                    corpMap.put(vo.getCorpName(), corp);
                }
                if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
                    corp = new CorpDTO();
                    msgList.add("nis.netbank.message.corp.null");
                } else {
                    vo.setCorpId(corp.getId());
                }
                //判断付款方式是否存在
                FcVoucherType voucherType = voucherTypeMap.get(vo.getVoucherName());
                if (voucherType == null) {
                    voucherType = fcVoucherTypeService.getByVoucherName(vo.getVoucherName());
                    voucherTypeMap.put(vo.getVoucherName(), voucherType);
                }
                if (EmptyUtils.isEmpty(voucherType)) {
                    msgList.add("nis.netbank.message.vouchertype.null");
                } else if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                    msgList.add("nis.netbank.message.vouchertype.bill.mismatch");
                } else {
                    //判断单位是否分配付款方式的权限
                    if (!EmptyUtils.isEmpty(corp) && !EmptyUtils.isEmpty(voucherType)) {
                        if (!btAssignPayWayService.isExistVoucherType(corp.getId(), voucherType.getId())) {
                            msgList.add("nis.netbank.message.corpvouchertype.null");
                        } else {
                            vo.setVoucherTypeId(voucherType.getId());
                            vo.setIsBudget(voucherType.getIsBudget());
                        }
                        //校验币别
                        String curId = curMap.get(vo.getCurName());
                        if (StringUtils.isBlank(curId)) {
                            curId = btCurrencyService.getCurrencyByName(vo.getCurName());
                            curMap.put(vo.getCurName(), curId);
                        }
                        if (EmptyUtils.isBlank(curId)) {
                            msgList.add("nis.netbank.message.cur.null");
                        } else {
                            vo.setCurId(curId);
                            //付款账号校验
                            if (StringUtils.isEmpty(vo.getPayerAcc())) {
                                //应收/付票据，无需校验付款账号
                                if (!NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                                        && !NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                                    msgList.add("nis.netbank.message.payeracc.null");
                                }
                            } else {
                                //校验付款账号,记账时取内部账号，不记账时取银行账号
                                if (NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())) {
                                    FcAccDTO fcAcc = fcAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (fcAcc == null) {
                                        fcAcc = this.isExistFcAcc(corp.getUserAccountCode(), vo.getPayerAcc());
                                        fcAccMap.put(vo.getCorpId() + vo.getPayerAcc(), fcAcc);
                                    }
                                    if (EmptyUtils.isEmpty(fcAcc)) {
                                        msgList.add("nis.netbank.message.fcacc.null");
                                    } else {
                                        vo.setAccId(fcAcc.getId());
                                        vo.setPayerAccName(fcAcc.getAccName());
                                        vo.setPayerBank(fcAcc.getNetName());
                                        vo.setDirectPayFlag(NetbankConstant.DIRECTPAYFLAG_N);
                                    }
                                    //转付记账时，需要校验银行账号
                                    if (NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
                                        if (StringUtils.isEmpty(vo.getDownAcc())) {
                                            msgList.add("nis.netbank.message.downacc.null");
                                        } else {
                                            if (StringUtils.isNotEmpty(vo.getAccId())) {
                                                BtBankAccDTO bankAcc = this.isExistDownAcc(corp.getUserAccountCode(), vo.getPayerAcc(), curId, vo.getDownAcc());
                                                if (bankAcc == null) {
                                                    msgList.add("nis.netbank.message.downacc.mismatch");
                                                } else {
                                                    vo.setDownAccId(bankAcc.getId());
                                                }
                                            }
                                        }
                                    }
                                } else {
                            /*        if (EmptyUtils.isNotEmpty(vo.getPayeeAccName())) {
                                        if (vo.getPayeeAccName().length() > 5) {
                                            vo.setIsForindividual("0");
                                        } else {
                                            vo.setIsForindividual("1");
                                        }
                                    }*/
                                    //获取付款账号
                                    BtBankAccDTO bankAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (bankAcc == null) {
                                        bankAcc = this.isExistBankAcc(corp.getUserAccountCode(), vo.getPayerAcc(), curId);
                                        bankAccMap.put(vo.getCorpId() + vo.getPayerAcc(), bankAcc);
                                    }
                                    if (EmptyUtils.isEmpty(bankAcc)) {
                                        msgList.add("nis.netbank.message.btbankacc.null");
                                    } else {
                                        vo.setAccId(bankAcc.getId());
                                        vo.setPayerAccName(bankAcc.getAccName());
                                        vo.setPayerBank(bankAcc.getBankName());
                                        vo.setBifCode(bankAcc.getBifCode());
                                        vo.setDirectPayFlag(NetbankConstant.DIRECTPAYFLAG_Y);
                                    }
                                    //不记账情况校验跨行标志
//                                    if (!NetbankConstant.DIFBANK_0.equals(vo.getDifBank())
//                                            && !NetbankConstant.DIFBANK_1.equals(vo.getDifBank())) {
//                                        msgList.add("nis.netbank.message.difbank.format");
//                                    }
                                    if (!EmptyUtils.isEmpty(bankAcc) && !EmptyUtils.isBlank(vo.getPayeeBankCode())) {
                                        String payeepri = vo.getPayeeBankCode().substring(0, 3);
                                        String payerpri = bankAcc.getBankCode().substring(0, 3);
                                        String difbank = "";
                                        String areaSign = "";
                                        if (payeepri.equals(payerpri)) {
                                            if ("313".equals(payeepri) || "402".equals(payeepri) || "314".equals(payeepri)) {
                                                if (StringUtils.isNotBlank(bankAcc.getBankNamePrefix())
                                                        && StringUtils.isNotBlank(vo.getPayeeBank())
                                                        && vo.getPayeeBank().indexOf(bankAcc.getBankNamePrefix()) != -1) {
                                                    difbank = NetbankConstant.DIFBANK_0;
                                                } else {
                                                    difbank = NetbankConstant.DIFBANK_1;
                                                }
                                            } else {
                                                difbank = NetbankConstant.DIFBANK_0;
                                            }
                                        } else {
                                            difbank = NetbankConstant.DIFBANK_1;
                                        }
                                        if (EmptyUtils.isNotEmpty(difbank)) {
                                            vo.setDifBank(difbank);
                                        }
                                        InputBankInfoDTO inputBankInfoDTO = btBankService.getByBankCode(vo.getPayeeBankCode());
                                        if (!EmptyUtils.isEmpty(inputBankInfoDTO)) {
                                            //地域标志
                                            if (StringUtils.isEmpty(bankAcc.getProv()) || StringUtils.isEmpty(inputBankInfoDTO.getProv())) {
                                                areaSign = NetbankConstant.AREASIGN_0;
                                            } else if (bankAcc.getProv().equals(inputBankInfoDTO.getProv()) && bankAcc.getCity().equals(inputBankInfoDTO.getCity())) {
                                                areaSign = NetbankConstant.AREASIGN_0;
                                            } else {
                                                areaSign = NetbankConstant.AREASIGN_1;
                                            }
                                        }
                                        if (EmptyUtils.isNotEmpty(areaSign)) {
                                            vo.setAreaSign(areaSign);
                                        }
//                                        if (!EmptyUtils.isBlank(difbank)) {
//                                            msgList.add("nis.netbank.message.difbank.format");
//                                        }
                                    }
//                                    //不记账情况校验地域标志
//                                    if (!NetbankConstant.AREASIGN_0.equals(vo.getAreaSign())
//                                            && !NetbankConstant.AREASIGN_1.equals(vo.getAreaSign())) {
//                                        msgList.add("nis.netbank.message.areasign.format");
//                                    }
                                }
                                //付款方式为应付票据、应收票据时付款账号必须为人民币的票据账户
                                if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                                        || NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                                    //获取付款账号
                                    BtBankAccDTO payerAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                    if (payerAcc == null) {
                                        payerAcc = btBankAccService.getBankAccInfo(vo.getPayerAcc());
                                    }
                                    if (!EmptyUtils.isEmpty(payerAcc) && BtBankAccConstant.ELECTRICBILL_YES.equals(payerAcc.getElectricBill())) {
                                        if (!EmptyUtils.isEmpty(rmbCurrency) && !vo.getCurId().equals(rmbCurrency.getId())) {
                                            msgList.add("nis.netbank.message.accbill.mustbermb");
                                        }
                                    } else {
                                        msgList.add("nis.netbank.message.accbill.mustbermb");
                                    }
                                }
                            }
                        }
                        //校验收款账号，内转时校验内部账号
                        if (NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode()) && corp != null) {
                            List<FcAccDTO> listFcAcc = fcAccPayeeMap.get(vo.getCurId() + corp.getNetId());
                            if (EmptyUtils.isEmpty(listFcAcc)) {
                                listFcAcc = innerAccountService.listAccountByParams(vo.getCurId(), corp.getNetId(), null, null);
                                fcAccPayeeMap.put(vo.getCurId() + corp.getNetId(), listFcAcc);
                            }
                            FcAccDTO fcAcc = null;
                            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                                for (FcAccDTO dto : listFcAcc) {
                                    if (vo.getPayeeAcc().equals(dto.getBlnctrAcc())) {
                                        fcAcc = new FcAccDTO();
                                        BeanUtils.copyNotNullProperties(dto, fcAcc);
                                        continue;
                                    }
                                }
                                if (fcAcc != null) {
                                    vo.setPayeeAcc(fcAcc.getBlnctrAcc());
                                    vo.setPayeeBank(fcAcc.getNetName());
                                    if (!fcAcc.getAccName().equals(vo.getPayeeAccName())) {
                                        msgList.add("nis.netbank.message.payeeaccname.mismatch");
                                    }
                                } else {
                                    msgList.add("nis.netbank.message.payeeacc.mismatch");
                                }
                            }
                        } else {
                            //判断开户行在系统中是否存在
                            if (!EmptyUtils.isBlank(vo.getPayeeBankCode())) {
                                InputBankInfoDTO btBankDTO = this.btBankService.getByBankCodeAndImport(vo.getPayeeBankCode());
                                if (EmptyUtils.isEmpty(btBankDTO)) {
                                    msgList.add("nis.netbank.message.bank.null");
                                } else {
//                                    //开户行名称不为空且与开户行代码对应的名称不一致
//                                    if (!EmptyUtils.isBlank(vo.getPayeeBank()) && !vo.getPayeeBank().equals(btBankDTO.getBankName())) {
//                                        msgList.add("nis.netbank.message.payeebank.mismatch");
//                                    } else {
                                    vo.setPayeeBankCode(btBankDTO.getBankCode());
                                    vo.setPayeeBank(btBankDTO.getBankName());
                                    vo.setProv(btBankDTO.getProv());
                                    vo.setCity(btBankDTO.getCity());
//                                    }
                                }
                            } else {
                                if (EmptyUtils.isBlank(vo.getPayeeBank())) {
                                    msgList.add("nis.netbank.message.payeebankcode.null");
                                } else {
                                    InputBankInfoDTO btBankDTO = this.btBankService.getByBankNameAndImport(vo.getPayeeBank());
                                    if (EmptyUtils.isEmpty(btBankDTO)) {
                                        msgList.add("nis.netbank.message.bank.null");
                                    } else {
                                        vo.setPayeeBankCode(btBankDTO.getBankCode());
                                        vo.setPayeeBank(btBankDTO.getBankName());
                                        vo.setProv(btBankDTO.getProv());
                                        vo.setCity(btBankDTO.getCity());
                                        //如果跨行标志为空
                                        if (EmptyUtils.isEmpty(vo.getDifBank()) ||
                                                EmptyUtils.isEmpty(vo.getAreaSign())) {
                                            //获取付款账号
                                            BtBankAccDTO bankAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                                            String payeepri = vo.getPayeeBankCode().substring(0, 3);
                                            String payerpri = bankAcc.getBankCode().substring(0, 3);
                                            String difbank = "";
                                            String areaSign = "";
                                            if (payeepri.equals(payerpri)) {
                                                if ("313".equals(payeepri) || "402".equals(payeepri) || "314".equals(payeepri)) {
                                                    if (StringUtils.isNotBlank(bankAcc.getBankNamePrefix())
                                                            && StringUtils.isNotBlank(vo.getPayeeBank())
                                                            && vo.getPayeeBank().indexOf(bankAcc.getBankNamePrefix()) != -1) {
                                                        difbank = NetbankConstant.DIFBANK_0;
                                                    } else {
                                                        difbank = NetbankConstant.DIFBANK_1;
                                                    }
                                                } else {
                                                    difbank = NetbankConstant.DIFBANK_0;
                                                }
                                            } else {
                                                difbank = NetbankConstant.DIFBANK_1;
                                            }
                                            if (EmptyUtils.isNotEmpty(difbank)) {
                                                vo.setDifBank(difbank);
                                            }
                                            InputBankInfoDTO inputBankInfoDTO = btBankService.getByBankCode(vo.getPayeeBankCode());
                                            if (!EmptyUtils.isEmpty(inputBankInfoDTO)) {
                                                //地域标志
                                                if (StringUtils.isEmpty(bankAcc.getProv()) || StringUtils.isEmpty(inputBankInfoDTO.getProv())) {
                                                    areaSign = NetbankConstant.AREASIGN_0;
                                                } else if (bankAcc.getProv().equals(inputBankInfoDTO.getProv()) && bankAcc.getCity().equals(inputBankInfoDTO.getCity())) {
                                                    areaSign = NetbankConstant.AREASIGN_0;
                                                } else {
                                                    areaSign = NetbankConstant.AREASIGN_1;
                                                }
                                            }
                                            if (EmptyUtils.isNotEmpty(areaSign)) {
                                                vo.setAreaSign(areaSign);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (!StringUtils.isEmpty(vo.getPayerAcc())) {
                            //收付款账号不能相同
                            if (vo.getPayerAcc().equals(vo.getPayeeAcc())) {
                                msgList.add("nis.netbank.message.payeeacc.noteq.payeracc");
                            }
                        }
                        //应收票据校验票据信息
                        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
                            if (!EmptyUtils.isBlank(vo.getBillId())) {
                                //校验票据是否存在且可用
                            } else {
                                msgList.add("nis.netbank.message.billid.null");
                            }
                        }
                    }
                }
                //金额不能为空且必须大于0
                if (EmptyUtils.isEmpty(vo.getPayMoney())) {
                    msgList.add("nis.netbank.message.money.null");
                } else {
                    if (vo.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
                        msgList.add("nis.netbank.message.money.mismatch");
                    }
                }
                //是否对私付款
                if (!NetbankConstant.FORINDIVIDUAL_0.equals(vo.getIsForindividual())
                        && !NetbankConstant.FORINDIVIDUAL_1.equals(vo.getIsForindividual())) {
                    msgList.add("nis.netbank.message.forindividual.format");
                }
                //是否已付款
                if (CommonConstant.YES.equals(showIsPayed) && StringUtils.isNotBlank(vo.getIsPayed())) {
                    if (!CommonConstant.NO.equals(vo.getIsPayed())
                            && !CommonConstant.YES.equals(vo.getIsPayed())) {
                        msgList.add("nis.netbank.message.ispayed.format");
                    }
                } else {
                    vo.setIsPayed(CommonConstant.NO);
                }
                //加急标志
                if (!NetbankConstant.ISQUICK_0.equals(vo.getIsQuick())
                        && !NetbankConstant.ISQUICK_1.equals(vo.getIsQuick())) {
                    msgList.add("nis.netbank.message.isquick.format");
                }
                //期望支付日期
                if (vo.getWishPayday() != null) {
                    if (vo.getWishPayday().compareTo(DateUtils.getDateYMD()) == -1) {
                        msgList.add("nis.netbank.message.wishpayday.mismatch");
                    }
                }
                //附言：农业银行长度不超过70字段
                BtBankAccDTO bankAcc = bankAccMap.get(vo.getCorpId() + vo.getPayerAcc());
                if (!EmptyUtils.isEmpty(bankAcc) && StringUtils.isNotEmpty(bankAcc.getBankTypeId())) {
                    BankTypeDTO bankType = bankTypeService.getObjectById(bankAcc.getBankTypeId());
                    if ("2000".equals(bankType.getBankType())) {
                        // 2000为中国农业银行：付款账号的银行类别为农业银行时，附言长度不超过70字段。
                        if (StringUtils.isNotEmpty(vo.getAddWord()) && vo.getAddWord().length() > 70) {
                            msgList.add("付款账号的银行类别为中国农业银行时，附言长度不超过70字段");
                        }
                    }
                }
                //业务类别校验
                if (!EmptyUtils.isEmpty(vo.getBusTypeName())) {
                    NisCorpBusVO busType = nisCorpBusService.getBusTypeByName(vo.getCorpId(), "1", vo.getBusTypeName());
                    if (busType == null) {
                        msgList.add("nis.netbank.message.bustype.mismatch");
                    } else {
                        vo.setBusType(busType.getBusId());
                    }
                }
                if (voucherType != null) {
                    //是否已开票
                    if (StringUtils.isEmpty(vo.getIsOpenBill())) {
                        //应付票据是否已开票不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())) {
                            msgList.add("nis.netbank.message.isopenbill.null");
                        } else {
                            vo.setIsOpenBill(NetbankConstant.ISOPENBILL_0);
                        }
                    } else {
                        //是否已开票不为空时，校验格式
                        if (!NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())
                                && !NetbankConstant.ISOPENBILL_1.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.isopenbill.format");
                        }
                    }
                    //票据类型
                    if (EmptyUtils.isEmpty(vo.getBillType())) {
                        //付款方式为应付票据，是否已开票为否时，票据类型不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.billType.null");
                        }
                    } else {
                        String billType = dictService.get("AB_BILL_TYPE", vo.getBillType());
                        if (StringUtils.isEmpty(billType)) {
                            //票据类型格式不符
                            msgList.add("nis.netbank.message.billType.format");
                        }
                    }
                    //票据流通介质
                    if (EmptyUtils.isEmpty(vo.getBillFlag())) {
                        //付款方式为应付票据，是否已开票为否时，票据流通介质不能为空
                        if (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(vo.getIsOpenBill())) {
                            msgList.add("nis.netbank.message.billFlag.null");
                        }
                    } else {
                        String billFlag = dictService.get("AB_BILL_FLAG", vo.getBillFlag());
                        if (StringUtils.isEmpty(billFlag)) {
                            //票据流通介质格式不符
                            msgList.add("nis.netbank.message.billFlag.format");
                        }
                    }
                    //判断是否挂计划,计划科目校验
                    if (isBudgetSel && StringUtils.isNotEmpty(vo.getCorpId())) {
                        //判断付款方式是否挂计划
                        String isBudget = "0";
                        if (voucherType != null) {
                            isBudget = voucherType.getIsBudget();
                        }
                        Boolean reportBudget = reportBudgetMap.get(vo.getCorpId());
                        if (reportBudget == null) {
                            reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(vo.getCorpId());
                            reportBudgetMap.put(vo.getCorpId(), reportBudget);
                        }
                        //判断单位是否上报计划
                        if (CommonConstant.YES.equals(isBudget) && reportBudget) {
                            if (EmptyUtils.isEmpty(vo.getItemCodeImp())) {
                                msgList.add("nis.assign.message.itemcode.null");
                            } else {
                                FbsItemDTO fbsItemDTO = this.fbsItemService.getFbsItemByCode(vo.getItemCodeImp());
                                if (EmptyUtils.isEmpty(fbsItemDTO)) {
                                    //判断计划科目是否存在
                                    msgList.add("nis.assign.message.bustype.itemcode.null");
                                } else {
                                    vo.setItemCode(new String[]{fbsItemDTO.getItemCode()});
                                    vo.setItemName(new String[]{fbsItemDTO.getItemName()});
                                    vo.setProcessMoney(new BigDecimal[]{vo.getPayMoney()});
                                    vo.setIsBudget(isBudget);
                                }
                            }
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }

    /**
     * 判断内部账号是否授权
     *
     * @param userAccountCode
     * @param acc
     */
    public FcAccDTO isExistFcAcc(String userAccountCode, String acc) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        FcAccDTO fcAccDTO = null;
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            List<FcAccDTO> listFcAcc = accountGrantService.listInnerAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listFcAcc)) {
                for (FcAccDTO fcAcc : listFcAcc) {
                    if (fcAcc.getBlnctrAcc().equals(acc)) {
                        return fcAcc;
                    }
                }
            }
        }
        return fcAccDTO;
    }

    /**
     * 判断银行账号是否授权
     *
     * @param userAccountCode
     * @param acc
     * @param curId
     */
    public BtBankAccDTO isExistBankAcc(String userAccountCode, String acc, String curId) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
                listAcc.addAll(listOnlineBankAcc);
            }
            if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        BtBankAccDTO btBankAccDTO = null;
        if (!EmptyUtils.isEmptyList(listAcc)) {
            for (BtBankAccDTO bankAcc : listAcc) {
                if (bankAcc.getBankAcc().equals(acc) && bankAcc.getCurId().equals(curId)) {
                    return bankAcc;
                }
            }
        }
        return btBankAccDTO;
    }

    /**
     * 判断银行账号与内部账号是否匹配
     *
     * @param userAccountCode
     * @param account
     * @param curId
     */
    public BtBankAccDTO isExistDownAcc(String userAccountCode, String account, String curId, String downAcc) {
        if (StringUtils.isBlank(userAccountCode)) {
            return null;
        }
        BtBankAccDTO btBankAccDTO = null;
        String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
        for (String corpId : userAccountCodes) {
            //获取具有转账权限的银行账号
            List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                for (BtBankAccDTO bankAcc : listBankAcc) {
                    if (bankAcc.getCurId().equals(curId) && account.equals(bankAcc.getCounterAccId()) && downAcc.equals(bankAcc.getBankAcc())) {
                        return bankAcc;
                    }
                }
            }
        }
        return btBankAccDTO;
    }

    /**
     * 判断账号付款是否使用中
     *
     * @param blnctrAcc
     * @return
     */
    public boolean isExistBlnctrAcc(String blnctrAcc) {
        List<NisBillhead> list = nisBillheadDao.listByPayerAcc(blnctrAcc);
        return !EmptyUtils.isEmpty(list);
    }

    /**
     * 客商信息保存
     *
     * @param nisBillhead
     */
    private void saveIfNotExist(NisBillheadVO nisBillhead) {
        //内转不需要保存供应商信息
        if (NetbankConstant.PAY_TYPE_05.equals(nisBillhead.getSysVoucherCode())) {
            return;
        }
        ExternalCorpDTO externalCorpDTO = new ExternalCorpDTO();
        externalCorpDTO.setCorpId(nisBillhead.getCorpId());
        externalCorpDTO.setName(nisBillhead.getPayeeAccName());
        externalCorpDTO.setProvince(nisBillhead.getProv());
        externalCorpDTO.setCity(nisBillhead.getCity());
        externalCorpDTO.setSourceSystem("BFS");
        //是否有效
        externalCorpDTO.setStatus(1);
        //是否共享
        externalCorpDTO.setIsCommon(nisBillhead.getIsCommon());
        List<ExternalCorpBankAccDTO> externalCorpBankAccList = new ArrayList<ExternalCorpBankAccDTO>();
        ExternalCorpBankAccDTO externalCorpBankAccDTO = new ExternalCorpBankAccDTO();
        externalCorpBankAccDTO.setExternalAcc(nisBillhead.getPayeeAcc());
        externalCorpBankAccDTO.setBankType(nisBillhead.getBankTypeId());
        externalCorpBankAccDTO.setBank(nisBillhead.getPayeeBank());
        externalCorpBankAccDTO.setBankSourceCode(nisBillhead.getPayeeBankCode());
        externalCorpBankAccDTO.setAccProvince(nisBillhead.getProv());
        externalCorpBankAccDTO.setAccCity(nisBillhead.getCity());
        externalCorpBankAccDTO.setIsDefaultAcc("0");
        externalCorpBankAccList.add(externalCorpBankAccDTO);
        externalCorpDTO.setExternalCorpBankAccList(externalCorpBankAccList);
        externalCorpService.saveIfNotExist(externalCorpDTO);
    }

    /**
     * 重复付款校验
     *
     * @param corpId   单位
     * @param payMoney 金额
     * @param payeeAcc 收款账号
     * @param id       单据号（编辑时校验需要）
     * @param tempFlag true - 查询暂存打回状态， false-查询已提交之后的状态数据
     * @return
     */
    public Boolean isExistRepay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, boolean tempFlag) {
        String repayAlertDaysStr = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REPAYALERTDAYS).getValue();
        if (!StringUtils.isBlank(repayAlertDaysStr)) {
            Integer repayAlertDays = Integer.parseInt(repayAlertDaysStr);
            String endDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
            String beginDate = endDate;
            if (repayAlertDays != 0) {
                beginDate = DateUtils.format(DateUtils.addDay(DateUtils.getDate(), -repayAlertDays), "yyyy-MM-dd");
            }
            beginDate = beginDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            List<NisBillhead> list = nisBillheadDao.listByRePay(corpId, payMoney, payeeAcc, beginDate, endDate, curId, id, tempFlag);
            return !EmptyUtils.isEmptyList(list);
        }
        return false;
    }

    /**
     * 手工记账待记账的付款单
     *
     * @return
     */
    public DataTablesOutput getDataTablesUnions(DataTablesInput dataTablesInput, ICurrentUser user) {
        DataTablesOutput output = null;
        //如果是手工记账
        if (NetbankConstant.ACCOUNTMODE_MANUAL.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
            if (NetbankConstant.ACCOUNTORDER_ACCOUNTPAY.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue())) {
                //先记账后付款
                StringBuilder sql = new StringBuilder();
                String resource = com.bytter.core.constant.ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_QUERY;
                List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
                if (CollectionUtil.isNotEmpty(corpList)) {
                    String corpAuthSql = SqlUtil.getInSql(corpList, "n.corp_Id", true);
                    sql.append(" and ").append(corpAuthSql);
                }
                output = nisBillheadDao.getDataTablesUnionByaccountPay(dataTablesInput, user, sql.toString());
            } else {
                //先付款后记账
                StringBuilder sql = new StringBuilder();
                String resource = com.bytter.core.constant.ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_QUERY;
                List<String> corpList = corpService.getUserCorpIds(resource, user.getId());
                String corpAuthSql = SqlUtil.getInSql(corpList, "n.corp_Id", true);
                sql.append(" and ").append(corpAuthSql);
                output = nisBillheadDao.getDataTablesUnionByPayAccount(dataTablesInput, user, sql.toString());
            }
        } else {
            //是否按金额记账
            String isAccountByMoney = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISACCOUNTBYMONEY).getValue();
            if (CommonConstant.YES.equals(isAccountByMoney)) {
                //记账金额
                String accountByMoney = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ACCOUNTMONEY).getValue();
                if (StringUtils.isNotBlank(accountByMoney)) {
                    StringBuilder sql = new StringBuilder();
                    //大于等于记账金额的付款单需要人工记账
                    sql.append(" and n.pay_money >= " + new BigDecimal(accountByMoney));
                    output = nisBillheadDao.getDataTablesUnionByAccountMoney(dataTablesInput, sql.toString(), user);
                }
            }
        }

        if (null == output) {
            output = new DataTablesOutput(dataTablesInput.getDraw(), new ArrayList(0), 0, 0);
        } else {
            List<NisBillheadVO> data = output.getData();
//            ArrayList<NisBillheadVO> nisBillheadVOS = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(data)) {
                List<String> curIds = data.stream().map(NisBillheadVO::getCurId).collect(Collectors.toList());
                Map<String, BtCurrencyDTO> currencyByIds = btCurrencyService.getCurrencyByIds(curIds);
                List<String> corpIds = data.stream().map(NisBillheadVO::getCorpId).collect(Collectors.toList());
                Map<String, String> corpMap = corpService.listCorpDTOByCorpIds(corpIds).stream().collect(Collectors.toMap(CorpDTO::getId, CorpDTO::getName));
                for (NisBillheadVO vo : data) {
                    vo.setCurName(currencyByIds.get(vo.getCurId()).getCurName());
                    vo.setCorpName(corpMap.get(vo.getCorpId()));
                    BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(vo.getCurId());
                }
            }
            output.setData(data);
        }
        List<NisBillheadVO> data = output.getData();
        List<NisBillheadDTO> nisBillheadDTOS = BeanUtils.copyListProperties(data, NisBillheadDTO.class);
        output.setData(nisBillheadDTOS);
        return output;
    }

    /**
     * 功能介绍 : 是否挂资金计划。<br>
     * <b>日期：</b>2018年3月29日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean isBudgetSel() {
        //挂资金计划参数
        String budgetSel = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
        //挂资金计划
        return CommonConstant.VALID_SIGN_YES.equals(budgetSel);
    }

    /**
     * 获取内转标志
     *
     * @param nisBillhead
     * @return
     */
    public String getInnerSign(NisBillheadVO nisBillhead) {
        if (!isBudgetSel()) {
            //不挂资金计划,则默认全部非内转业务
            return BudgetConstant.INNER_SIGN_0;
        }
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        //内转付款方式 并且内转挂计划才返回内转标志
        if (NetbankConstant.PAY_TYPE_05.equals(voucherType.getSysVoucherCode()) && CommonConstant.VALID_SIGN_YES.equals(voucherType.getIsBudget())) {
            return BudgetConstant.INNER_SIGN_1;
        } else {
            // 其他付款方式校验是否对内付款
            // 1.收款账号是否为内部单位账号，且跟付款方同网点
            BtBankAccDTO bankAcc = bankAccService.getBankAccInfo(nisBillhead.getPayeeAcc());
            boolean accIsTheSameNet = false;
            if (bankAcc != null) {
                accIsTheSameNet = this.corpService.isTheSameNetByCorpId(nisBillhead.getCorpId(), bankAcc.getCorpId());
            }
            // 2.收款户名是否为系统单位，且跟付款方同网点
            CorpDTO corpDTO = this.corpService.getCorpIdByName(nisBillhead.getPayeeAccName());
            boolean corpIsTheSameNet = false;
            if (!EmptyUtils.isEmpty(corpDTO)) {
                corpIsTheSameNet = this.corpService.isTheSameNetByCorpId(nisBillhead.getCorpId(), corpDTO.getId());
            }
            if (accIsTheSameNet || corpIsTheSameNet) {
                return BudgetConstant.INNER_SIGN_1;
            } else {
                return BudgetConstant.INNER_SIGN_0;
            }
        }
    }

    /**
     * 撤回
     *
     * @param workFlowResult
     */
    @Override
    public void flowRevoke(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        NisBillhead nisBillhead = this.get(workFlowResult.getBusinessKey(), true);
        //更新审批状态
        nisBillhead.setStatus(NetbankConstant.STATUE_TEMP);
        nisBillhead.setUpdateBy(user.getLoginName());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(nisBillhead);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo(), NetbankConstant.STATUE_TEMP, user);
        //生成日志
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, NisMessageConstant.NIS_LOG_WITHDRAW_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 获取审批列表单位树
     *
     * @param flowStatus track 流程跟踪 todo 审批中
     * @param user
     * @return
     */
    public Object treeByCorpIds(ICurrentUser user, String flowStatus) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        String businessSql = getBusinessSql(NisWorkFlowConstant.NIS_BILLHEAD, user.getId(), flowStatus);
        List<String> corpIds = this.nisBillheadDao.getCorpIdsByDataTableApprove(businessSql);
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
     * 校验收款账户是否是网点或成员单位
     *
     * @param vo
     * @return
     */
    public boolean isCheckExternalCorp(NisBillheadVO vo) {
        //内转不需要保存供应商信息
        if (NetbankConstant.PAY_TYPE_05.equals(vo.getSysVoucherCode())) {
            return false;
        }
        // 根据收款账号查下银行账号表校验是否是存在的内部单位账号
        BtBankAccDTO bankAcc = btBankAccService.getBankAccInfo(vo.getPayeeAcc());
        return bankAcc == null || !bankAcc.getAccName().equals(vo.getPayeeAccName());
        //return EmptyUtils.isEmpty(corpService.getCorpIdByName(vo.getPayeeAccName()));
    }

    /**
     * <br>
     * <b>功能：</b>查询付款计划（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    public BigDecimal queryNisBillhead(String corpflag, String corpCode, Date wishPayday) {
        return this.nisBillheadDao.queryNisBillhead(corpflag, corpCode, wishPayday);
    }

    /**
     * 根据付款方式调用相应的资金预警
     *
     * @param entity
     * @param user
     */
    public void warnExecuteNis(NisBillheadVO entity, ICurrentUser user) {
        //这里调用预警服务 如果失败 不应该直接失败总流程
        try{
            //判断是否记账 0，否 1，是
            if (NetbankConstant.ISACCOUNT_0.equals(entity.getIsAccount())) {
                fundWarnService.warnExecuteByEqCorpPay(entity, user);
                fundWarnService.warnExecuteBankAccPay(entity, user);
                //是否启动审批，如果审批调用单日累计支付预警
                if (NetbankConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())) {
                    fundWarnService.warnExecuteDaySumPay(entity, user);
                }
            } else {
                fundWarnService.warnExecuteCashPay(entity, user);
            }
        } catch (RpcException e){
            logger.error("调用fundwarn付款预警服务异常",e);
        }

    }

    /**
     * 批量选择付款账号
     *
     * @param nisBillhead
     * @param user
     */
    public void batchSetPayerAcc(NisBillheadVO nisBillhead, ICurrentUser user) {
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            String[] ids = nisBillhead.getId().split(",");
            for (String id : ids) {
                NisBillhead entity = this.get(id);
                //暂存和编辑状态才可以提交
                if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
                }
                entity.setAccId(nisBillhead.getAccId());
                entity.setPayerAcc(nisBillhead.getPayerAcc());
                entity.setPayerAccName(nisBillhead.getPayerAccName());
                entity.setPayerBank(nisBillhead.getPayerBank());
                entity.setCurId(nisBillhead.getCurId());
                entity.setBifCode(nisBillhead.getBifCode());
                entity.setDownAccId(nisBillhead.getDownAccId());
                //跨行标志和地域标志设置
                //获取付款账号
                BtBankAccDTO bankAcc = btBankAccService.getBankAccById(entity.getAccId());
                if (bankAcc != null) {
                    //跨行标志
                    if (StringUtils.isEmpty(bankAcc.getBankCode()) || StringUtils.isEmpty(entity.getPayeeBankCode())) {
                        entity.setDifBank(NetbankConstant.DIFBANK_0);
                    } else if (bankAcc.getBankCode().substring(0, 3).equals(entity.getPayeeBankCode().substring(0, 3))) {
                        if ("313".equals(bankAcc.getBankCode().substring(0, 3)) || "402".equals(bankAcc.getBankCode().substring(0, 3))
                                || "314".equals(bankAcc.getBankCode().substring(0, 3))) {
                            if (StringUtils.isNotBlank(bankAcc.getBankNamePrefix()) && entity.getPayeeBank().indexOf(bankAcc.getBankNamePrefix()) != -1) {
                                entity.setDifBank(NetbankConstant.DIFBANK_0);
                            } else {
                                entity.setDifBank(NetbankConstant.DIFBANK_1);
                            }
                        } else {
                            entity.setDifBank(NetbankConstant.DIFBANK_0);
                        }
                    } else {
                        entity.setDifBank(NetbankConstant.DIFBANK_1);
                    }
                    //地域标志
                    if (StringUtils.isEmpty(bankAcc.getProv()) || StringUtils.isEmpty(entity.getProv())) {
                        entity.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else if (bankAcc.getProv().equals(entity.getProv()) && bankAcc.getCity().equals(entity.getCity())) {
                        entity.setAreaSign(NetbankConstant.AREASIGN_0);
                    } else {
                        entity.setAreaSign(NetbankConstant.AREASIGN_1);
                    }
                }
                this.saveOrUpdate(entity);
                //生成日志
                nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SET_PAYERACC, NisMessageConstant.NIS_LOG_UPDATE_SAVE_SUCC, NisMessageConstant.BUS_TYPE_PAY, user);
            }
        }
    }

    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadVO> listByTime(String beginDate, String endDate) {
        return nisBillheadDao.listByTime(beginDate, endDate);
    }

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @param billFrom 数据来源
     * @return
     * @author zuoy 2019-03-06
     */
    public NisBillheadVO getNisBillheadByErpId(String erpId, String billFrom) {
        return nisBillheadDao.getNisBillheadByErpId(erpId, billFrom);
    }

    /**
     * 根据用户流程查询审批付款单据中的账号
     *
     * @param user
     * @return
     */
    public List<NisBillheadVO> getPayerAcc(ICurrentUser user, String flowStatus) {
        String businessSql = getBusinessSql(NisWorkFlowConstant.NIS_BILLHEAD, user.getId(), flowStatus);
        return nisBillheadDao.getPayerAcc(businessSql);
    }

    /**
     * 根据银行流水获取付款单
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput listNisBillheadByDtl(DataTablesInput dataTablesInput, String joinSql) {
        //手动调拨单是否要与付款明细勾对
        String allocationAllowBlending = configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ALLOCATIONALLOWBLENDING).getValue();
        //todo 视图需要移除
        String table;
        if ("1".equals(allocationAllowBlending)) {
            table = vNisAllocation(dataTablesInput);
        } else {
            table = vNisPayBill(dataTablesInput);
        }
        return nisBillheadDao.getDataTablesUnionForDtl(dataTablesInput, table, joinSql);
    }

    private String vNisAllocation(DataTablesInput dataTablesInput) {
        String paySql = "select n.id as id,\n" +
                "       n.bill_No            as billNo,\n" +
                "       n.corp_Id            as corpId,\n" +
                "       f.voucher_name       as voucherName,\n" +
                "       case when b.payer_acc is not null then b.payer_acc\n" +
                "       else n.payer_Acc  end    AS payerAcc,\n" +
                "       n.payer_Acc_Name     as payerAccName,\n" +
                "       n.cur_Id             as curId,\n" +
                "       n.wish_payday        as wishPayday,\n" +
                "       n.pay_Money          as payMoney,\n" +
                "       n.payee_Acc          as payeeAcc,\n" +
                "       n.payee_Acc_Name     as payeeAccName,\n" +
                "       n.purpose            as purpose,\n" +
                "       n.bus_Type           as busType,\n" +
                "       n.status             as status,\n" +
                "       n.blend_sign         as blendSign,\n" +
                "       'nis_billhead'               as isFrom\n" +
                "  from Nis_Billhead n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join Nis_Bus_Type t on n.bus_Type = t.id\n" +
                "  left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1'\n" +
                "  left join bt_bank_acc c on n.down_Acc_Id = c.id\n" +
                " where 1 = 1\n" +
                "   and n.status = (case\n" +
                "         when f.is_account = 1 then\n" +
                "          100\n" +
                "         else\n" +
                "          95\n" +
                "       end)\n" +
                "   and ((n.is_payed = 1) or\n" +
                "       (n.is_payed = 0 and\n" +
                "       (b.voucher_stat = '0' or b.voucher_stat is null))) ";
        String wageSql = " union all\n" +
                "  select n.id as id,\n" +
                "         n.wage_no as billNo,\n" +
                "         n.corp_id as corpId,\n" +
                "         '代发工资' as voucherName,\n" +
                "         n.payer_acc as payerAcc,\n" +
                "         n.payer_acc_name as payerAccName,\n" +
                "         n.cur_id as curId,\n" +
                "         n.wish_payday   as wishPayday,\n" +
                "         n.wages as payMoney,\n" +
                "         '' as payeeAcc,\n" +
                "         '' as payeeAccName,\n" +
                "         '代发工资' as purpose,\n" +
                "         n.bus_type as busType,\n" +
                "         n.status as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_wage'             as isFrom\n" +
                "  from nis_wage n\n" +
                "  where 1=1 and n.status = 95 ";
        String flowSql = "  union all\n" +
                "  select n.id                 as id,\n" +
                "         n.bill_no            as billNo,\n" +
                "         n.corp_id            as corpId,\n" +
                "         f.voucher_name       as voucherName,\n" +
                "         n.corp_bank_acc      as payerAcc,\n" +
                "         n.corp_acc_name      as payerAccName,\n" +
                "         n.cur_id             as curId,\n" +
                "         null                 as wishPayday,\n" +
                "         n.pay_money          as payMoney,\n" +
                "         n.COTP_BANK_ACC      as payeeAcc,\n" +
                "         n.COTP_ACC_NAME      as payeeAccName,\n" +
                "         req.purpose          as purpose,\n" +
                "         pro.pro_name         as busType,\n" +
                "         n.status             as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'fex_cash_flow'      as isFrom\n" +
                "  from fex_cash_flow n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join fex_tran_req req on req.id = n.TRAN_BILL_ID\n" +
                "  left join fex_product pro on req.pro_id = pro.id\n" +
                "  where 1=1 and n.status =95 and n.PAY_STATUS = '1' and n.CD_SIGN = '1' ";
        String allocationSql = " union all\n" +
                "    select n.id                 as id,\n" +
                "         n.bill_no              as billNo,\n" +
                "         n.corp_id              as corpId,\n" +
                "         case when n.type = '10' then '上划'\n" +
                "              when n.type = '11' then '下拨'\n" +
                "              when n.type = '12' then '请款'\n" +
                "              when n.type = '13' then '头寸调拨'\n" +
                "              when n.type = '14' then '上缴'\n" +
                "           end                  as voucherName,\n" +
                "         n.PAYER_ACC        as payerAcc,\n" +
                "         n.PAYER_ACC_NAME        as payerAccName,\n" +
                "         n.cur_id               as curId,\n" +
                "         n.Wish_Payday          as wishPayday,\n" +
                "         n.MONEY                as payMoney,\n" +
                "         n.payee_acc            as payeeAcc,\n" +
                "         n.PAYEE_ACC_NAME       as payeeAccName,\n" +
                "         n.purpose              as purpose,\n" +
                "         null                   as busType,\n" +
                "         n.status               as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_allocation_req'      as isFrom\n" +
                "  from nis_allocation_req n\n" +
                "  where 1=1 and n.status >= 95 ";
        String payeeAccName = (String) dataTablesInput.getFormdata().get("likes_payeeAccName");
        if (EmptyUtils.isNotEmpty(payeeAccName)) {
            paySql = paySql + " and  n.payee_Acc_Name  like '%" + payeeAccName + "%' ";
            flowSql = flowSql + " and  n.COTP_ACC_NAME like '%" + payeeAccName + "%' ";
            wageSql = wageSql + " and 1!=1 ";
            allocationSql = allocationSql + " and n.PAYEE_ACC_NAME like '%" + payeeAccName + "%' ";
            dataTablesInput.getFormdata().remove("likes_payeeAccName");
        }

        String payerAccName = (String) dataTablesInput.getFormdata().get("likes_payerAccName");
        if (EmptyUtils.isNotEmpty(payerAccName)) {
            paySql = paySql + " and  n.payer_Acc_Name  like '%" + payerAccName + "%' ";
            wageSql = wageSql + " and  n.payer_acc_name like '%" + payerAccName + "%' ";
            flowSql = flowSql + " and  n.corp_acc_name like '%" + payerAccName + "%' ";
            allocationSql = allocationSql + " and n.PAYER_ACC_NAME like '%" + payerAccName + "%' ";
            dataTablesInput.getFormdata().remove("likes_payerAccName");
        }

        String sql = " (" + paySql + wageSql + flowSql + allocationSql + ") ";
        return sql;
    }

    private String vNisPayBill(DataTablesInput dataTablesInput) {
        String paySql = "select n.id as id,\n" +
                "       n.bill_No            as billNo,\n" +
                "       n.corp_Id            as corpId,\n" +
                "       f.voucher_name       as voucherName,\n" +
                "       case when b.payer_acc is not null then b.payer_acc\n" +
                "       else n.payer_Acc  end    AS payerAcc,\n" +
                "       n.payer_Acc_Name     as payerAccName,\n" +
                "       n.cur_Id             as curId,\n" +
                "       n.wish_payday        as wishPayday,\n" +
                "       n.pay_Money          as payMoney,\n" +
                "       n.payee_Acc          as payeeAcc,\n" +
                "       n.payee_Acc_Name     as payeeAccName,\n" +
                "       n.purpose            as purpose,\n" +
                "       n.bus_Type           as busType,\n" +
                "       n.status             as status,\n" +
                "       n.blend_sign         as blendSign,\n" +
                "       'nis_billhead'               as isFrom\n" +
                "  from Nis_Billhead n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join Nis_Bus_Type t on n.bus_Type = t.id\n" +
                "  left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1'\n" +
                "  left join bt_bank_acc c on n.down_Acc_Id = c.id\n" +
                " where 1 = 1\n" +
                "   and n.status = (case\n" +
                "         when f.is_account = 1 then\n" +
                "          100\n" +
                "         else\n" +
                "          95\n" +
                "       end)\n" +
                "   and ((n.is_payed = 1) or\n" +
                "       (n.is_payed = 0 and\n" +
                "       (b.voucher_stat = '0' or b.voucher_stat is null))) ";
        String wageSql = " union all\n" +
                "  select n.id as id,\n" +
                "         n.wage_no as billNo,\n" +
                "         n.corp_id as corpId,\n" +
                "         '代发工资' as voucherName,\n" +
                "         n.payer_acc as payerAcc,\n" +
                "         n.payer_acc_name as payerAccName,\n" +
                "         n.cur_id as curId,\n" +
                "         n.wish_payday   as wishPayday,\n" +
                "         n.wages as payMoney,\n" +
                "         '' as payeeAcc,\n" +
                "         '' as payeeAccName,\n" +
                "         '代发工资' as purpose,\n" +
                "         n.bus_type as busType,\n" +
                "         n.status as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'nis_wage'             as isFrom\n" +
                "  from nis_wage n\n" +
                "  where 1=1 and n.status = 95 ";
        String flowSql = "  union all\n" +
                "  select n.id                 as id,\n" +
                "         n.bill_no            as billNo,\n" +
                "         n.corp_id            as corpId,\n" +
                "         f.voucher_name       as voucherName,\n" +
                "         n.corp_bank_acc      as payerAcc,\n" +
                "         n.corp_acc_name      as payerAccName,\n" +
                "         n.cur_id             as curId,\n" +
                "         null                 as wishPayday,\n" +
                "         n.pay_money          as payMoney,\n" +
                "         n.COTP_BANK_ACC      as payeeAcc,\n" +
                "         n.COTP_ACC_NAME      as payeeAccName,\n" +
                "         req.purpose          as purpose,\n" +
                "         pro.pro_name         as busType,\n" +
                "         n.status             as status,\n" +
                "         n.blend_sign         as blendSign,\n" +
                "         'fex_cash_flow'      as isFrom\n" +
                "  from fex_cash_flow n\n" +
                "  left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id\n" +
                "  left join fex_tran_req req on req.id = n.TRAN_BILL_ID\n" +
                "  left join fex_product pro on req.pro_id = pro.id\n" +
                "  where 1=1 and n.status =95 and n.PAY_STATUS = '1' and n.CD_SIGN = '1' ";
        String payeeAccName = (String) dataTablesInput.getFormdata().get("likes_payeeAccName");
        if (EmptyUtils.isNotEmpty(payeeAccName)) {
            paySql = paySql + " and n.payee_Acc like '%" + payeeAccName + "%' ";
            flowSql = flowSql + " and n.COTP_ACC_NAME like '%" + payeeAccName + "%' ";
            wageSql = wageSql + " and 1!=1 ";
            dataTablesInput.getFormdata().remove("likes_payeeAccName");
        }

        String payerAccName = (String) dataTablesInput.getFormdata().get("likes_payerAccName");
        if (EmptyUtils.isNotEmpty(payerAccName)) {
            paySql = paySql + " and n.payer_Acc_Name like '%" + payerAccName + "%' ";
            wageSql = wageSql + " and n.payer_acc_name like '%" + payerAccName + "%' ";
            flowSql = flowSql + " and n.corp_acc_name like '%" + payerAccName + "%' ";
            dataTablesInput.getFormdata().remove("likes_payerAccName");
        }
        String sql = " (" + paySql + wageSql + flowSql + ") ";

        return sql;
    }

    /**
     * 根据银行流水获取付款单（退款勾兑）
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput listNisBillheadByRefundblending(DataTablesInput dataTablesInput) {
        DataTablesOutput output = nisBillheadDao.getDataTablesUnionForRefundblending(dataTablesInput);
        List<NisBillheadDTO> list = output.getData();
        if (EmptyUtils.isNotEmpty(list)) {
            Map<String, BtCurrencyDTO> curMap = btCurrencyService.getCurrency();
            for (NisBillheadDTO nisBillheadDTO : list) {
                if (!EmptyUtils.isEmpty(curMap)) {
                    BtCurrencyDTO btCurrencyDTO = curMap.get(nisBillheadDTO.getCurId());
                    if (!EmptyUtils.isEmpty(btCurrencyDTO)) {
                        nisBillheadDTO.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }
        }
        return output;
    }

    /**
     * 根据开始时间结束时间获取付款单
     * * 功能：退款勾兑定时任务
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadDTO> getDataTablesUnionForRefundblending(Date beginDate, Date endDate) {
        return nisBillheadDao.getDataTablesUnionForRefundblending(beginDate, endDate);
    }

    public List<NisBillheadDTO> getDataTablesUnionForRefundblendingNew(Date beginDate, Date endDate, int rownum) {
        return nisBillheadDao.getDataTablesUnionForRefundblendingNew(beginDate, endDate, rownum);
    }

    public int getCountDataTablesUnionForRefundblending(Date beginDate, Date endDate) {
        return nisBillheadDao.getCountDataTablesUnionForRefundblending(beginDate, endDate);
    }

    /**
     * 根据流水号查询付款单
     *
     * @param bankSerialNo
     * @return
     */
    public List<NisBillheadDTO> listNisBillheadByBankSerialNo(String bankSerialNo) {
        List<NisBillheadDTO> nisBillheadDTOList = new ArrayList<>();
        List<NisBillheadVO> nisBillheadVOList = nisBillheadDao.listNisBillheadByBankSerialNo(bankSerialNo);
        if (!EmptyUtils.isEmptyList(nisBillheadVOList)) {
            nisBillheadDTOList = BeanUtils.copyListProperties(nisBillheadVOList, NisBillheadDTO.class);
        }
        return nisBillheadDTOList;
    }

    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     *
     * @param bankAcc 付款账号
     * @param curId   币别
     * @return
     */
    public BigDecimal getSum(String bankAcc, String curId, String startTime, String endTime) {
        List<NisBillhead> list = nisBillheadDao.getSum(bankAcc, curId, startTime, endTime);
        BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(bankAcc);
        CorpDTO corpDTO = corpService.get(bankAccInfo.getCorpId());
        BigDecimal result = new BigDecimal(0);//总金额
        //汇率转换
        for (NisBillhead entity : list) {
            //获取汇率，单位ID，当前金额币种ID，当前时间
            BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(entity.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
            }
            BigDecimal multiply = entity.getPayMoney().multiply(rate);
            result = result.add(multiply);
        }
        return result;
    }

    /**
     * 根据单位和币别获取付款单的付款金额总和
     *
     * @param corpId 单位
     * @param curId  币别
     * @return
     */
    public BigDecimal getSumForCorp(String corpId, String curId, String startTime, String endTime) {
        List<NisBillhead> list = nisBillheadDao.getListByCorpAndCurId(corpId, startTime, endTime);
        List<String> payerAccs = list.stream().filter(head -> StringUtils.isEmpty(head.getCorpId())).map(NisBillhead::getPayerAcc).collect(Collectors.toList());
        Map<String, BtBankAccDTO> maps = bankAccService.getBankAccByInfoAccs(payerAccs);
        for (NisBillhead hed : list) {
            if (StringUtils.isEmpty(hed.getCorpId())) {
                hed.setCorpId(maps.get(hed.getPayerAcc()).getCorpId());
            }
        }
        BigDecimal result = new BigDecimal(0);//总金额
        CorpDTO corpDTO = corpService.get(corpId);
        //汇率转换
        for (NisBillhead entity : list) {
            //获取汇率，单位ID，当前金额币种ID，当前时间
            BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(entity.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
            }
            BigDecimal multiply = entity.getPayMoney().multiply(rate);
            result = result.add(multiply);
        }
        return result;
    }
    /**
     * 根据id查询数据金额
     *
     * @param id 数据项Id
     * @return
     */
    /**
     * 根据id查询数据金额
     *
     * @param id 数据项Id
     * @return
     */
    public NisBillheadDTO getMoneyById(String id) {
        List<NisBillhead> list = nisBillheadDao.getMoneyById(id);
        if (list == null || list.size() == 0) {
            return null;
        }

        NisBillhead nisBillhead = list.get(0);
        CorpDTO corpDTO = corpService.get(nisBillhead.getCorpId());
        BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(nisBillhead.getCurId(), corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        if (rate == null) {
            rate = new BigDecimal(1);
        }
        nisBillhead.setPayMoney(nisBillhead.getPayMoney().multiply(rate));
        return BeanUtils.copyNewInstanceProperties(nisBillhead, NisBillheadDTO.class);
    }

    /**
     * Erp接口（WebService）获取付款单交易状态信息
     *
     * @param serialNoErp 外部系统id
     * @return
     */
    public List<Map<Object, Object>> getVoucherStatByWs(String serialNoErp) {
        return nisBillheadDao.getVoucherStatByWs(serialNoErp);
    }

    private String getTaskWorkSql(String processDefinitionKey, String userId) {
        String businessSql = "select exe.BUSINESS_KEY_ from ACT_RU_TASK task " +
                " INNER JOIN ACT_RE_PROCDEF pd ON task.PROC_DEF_ID_ = pd.ID_ " +
                " INNER JOIN ACT_RU_EXECUTION exe ON exe.PROC_DEF_ID_ = pd.ID_ AND exe.PARENT_ID_ is null AND exe.ID_ = task.PROC_INST_ID_ AND exe.PROC_INST_ID_ = task.PROC_INST_ID_ " +
                " WHERE task.SUSPENSION_STATE_ = 1 " +
                " AND task.ASSIGNEE_ = '" + userId + "'" +
                " AND pd.KEY_ = '" + processDefinitionKey + "'";
        return businessSql;
    }

    private String getTrackWorkSql(String processDefinitionKey, String userId) {
        String businessSql = "select RES.BUSINESS_KEY_ from ACT_HI_PROCINST RES " +
                " left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_ " +
                " WHERE RES.END_TIME_ IS NULL " +
                " AND DEF.KEY_ = '" + processDefinitionKey + "'" +
                " and ((exists (select LINK.USER_ID_ from ACT_HI_IDENTITYLINK LINK " +
                " where USER_ID_ =  '" + userId + "'" +
                " and LINK.PROC_INST_ID_ = RES.ID_)) or " +
                " RES.START_USER_ID_ = '" + userId + "')";
        return businessSql;
    }

    private String getPassWorkSql(String processDefinitionKey, String userId) {
        String businessSql = " select DISTINCT ahp.BUSINESS_KEY_ " +
                " from ACT_HI_TASKINST RES " +
                " LEFT JOIN ACT_HI_PROCINST ahp ON RES.PROC_INST_ID_ = ahp.PROC_INST_ID_ " +
                " LEFT JOIN ACT_RE_PROCDEF pd ON RES.PROC_DEF_ID_  = pd.ID_ " +
                " WHERE  RES.END_TIME_ is not NULL  AND pd.KEY_ = '" + processDefinitionKey + "'" +
                " AND ( EXISTS( " +
                " SELECT LINK.USER_ID_ " +
                " FROM ACT_HI_IDENTITYLINK LINK " +
                " WHERE USER_ID_ = '" + userId + "'" +
                " AND LINK.TASK_ID_ = RES.ID_) " +
                " OR RES.ASSIGNEE_ = '" + userId + "'" +
                " OR RES.OWNER_ = '" + userId + "')";
        return businessSql;
    }

    private String getBusinessSql(String processDefinitionKey, String userId, String flowStatus) {
        String businessSql = "";
//        switch (flowStatus) {
//            case IWorkFlowService.TODO:
//                businessSql = getTaskWorkSql(processDefinitionKey, userId);
//                break;
//            case IWorkFlowService.TRACK:
//                businessSql = getTrackWorkSql(processDefinitionKey, userId);
//                break;
//            case IWorkFlowService.PASS:
//                businessSql = getPassWorkSql(processDefinitionKey, userId);
//                break;
//            default:
//        }
        Map<String, Object> formdata = new HashMap<String, Object>();
        formdata.put("flowStatus", flowStatus);
        DataTablesInput dataTablesInput = new DataTablesInput();
        dataTablesInput.setFormdata(formdata);
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(processDefinitionKey, dataTablesInput);
        List<String> businessIdList = workFlowBusiness.getBusinessIdList();
        if (!EmptyUtils.isEmpty(businessIdList)) {
            businessSql = SqlUtil.getInSql(businessIdList, "n.id", true);
        }
        return businessSql;
    }

    /**
     * 根据支付策略获取付款单
     *
     * @return 满足策略的付款单集合
     */
    public List<NisBillheadVO> listByBisPayPolicy(BisPayPolicyVO payPolicy) {
        Map<String, Object> params = Maps.newHashMap();
        StringBuffer sql = new StringBuffer();
        sql.append(" and n.corp_Id = '").append(payPolicy.getCorpId()).append("' and n.cur_Id = '").append(payPolicy.getCurId()).append("'");
        // 业务类型（大类）
        if (StringUtils.isNotBlank(payPolicy.getBusTypeId())) {
            String[] busTypeStr = payPolicy.getBusTypeId().split(CommonConstant.COMMA_SPLIT);
            List<String> busType = new ArrayList<>(Arrays.asList(busTypeStr));
            String busTypeAuthSql = SqlUtil.getInSql(busType, "n.bus_type", true);
            sql.append(" and ").append(busTypeAuthSql);
        }
        // 共享业务类型（小类）
    /*    if (StringUtils.isNotBlank(payPolicy.getShareBusTypeId())) {
            List<String> shareBusTypeList = Arrays.asList(payPolicy.getShareBusTypeId().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and (n.SHARE_BUS_TYPE_ID is null or n.SHARE_BUS_TYPE_ID = '' or n.SHARE_BUS_TYPE_ID in (:shareBusTypeList)) ");
            params.put("shareBusTypeList", shareBusTypeList);
        }*/
        // 支付收款人
        if (StringUtils.isNotBlank(payPolicy.getPayeeName())) {
            String[] payeeStr = payPolicy.getPayeeName().split(CommonConstant.COMMA_SPLIT);
            List<String> payee = new ArrayList<>(Arrays.asList(payeeStr));
            String payeeAuthSql = SqlUtil.getInSql(payee, "n.payee_Acc_Name", true);
            sql.append(" and ").append(payeeAuthSql);
        }
        // 不支付收款人账号
        if (StringUtils.isNotBlank(payPolicy.getNoPayeeAcc())) {
            List<String> noPayeeAccList = Arrays.asList(payPolicy.getNoPayeeAcc().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and n.payee_Acc not in (:noPayeeAccList) ");
            params.put("noPayeeAccList", noPayeeAccList);
        }
        // 不支付收款人户名
        if (StringUtils.isNotBlank(payPolicy.getNoPayeeName())) {
            List<String> noPayeeNameList = Arrays.asList(payPolicy.getNoPayeeName().split(CommonConstant.COMMA_SPLIT));
            sql.append(" and n.payee_Acc_Name not in (:noPayeeNameList) ");
            params.put("noPayeeNameList", noPayeeNameList);
        }
        // 支付账号
        if (StringUtils.isNotBlank(payPolicy.getPayerAccId())) {
            String[] payerAccStr = payPolicy.getPayerAccId().split(CommonConstant.COMMA_SPLIT);
            List<String> payerAcc = new ArrayList<>(Arrays.asList(payerAccStr));
            String payerAccAuthSql = SqlUtil.getInSql(payerAcc, "n.acc_Id", true);
            sql.append(" and ").append(payerAccAuthSql);
        }
        // 不支付用途
        if (StringUtils.isNotBlank(payPolicy.getNoPayPurpose())) {
            List<String> noPayPurposeList = Arrays.asList(payPolicy.getNoPayPurpose().split("#"));
            noPayPurposeList.forEach(noPayPurpose -> sql.append(" and n.purpose not like '%").append(noPayPurpose).append("%' "));
        }
        //单笔限额
        if (payPolicy.getRangeVal1() != null) {
            sql.append(" and (n.pay_money").append(getCompareSql(payPolicy.getRange1())).append(payPolicy.getRangeVal1());
            if (payPolicy.getRange2() != null) {
                sql.append(" " + getRelationSql(payPolicy.getRangeJoin()));
                sql.append(" n.pay_money").append(getCompareSql(payPolicy.getRange2())).append(payPolicy.getRangeVal2() + ")");
            } else {
                sql.append(")");
            }
        }
        //排序
        StringBuffer orderBy = new StringBuffer(" order by ");
        if (!EmptyUtils.isEmpty(payPolicy.getPolicyTypeList())) {
            //排序规则：1-同行优先，2-小额优先，3-早制单优先，4-加急支付优先，5-同城优先
            for (BisPayPolicySort sort : payPolicy.getPolicyTypeList()) {
                if (NetbankConstant.POLICY_TYPE_1.equals(sort.getPolicyType())) {
                    orderBy.append(" n.dif_bank" + CommonConstant.COMMA_SPLIT);
                } else if (NetbankConstant.POLICY_TYPE_2.equals(sort.getPolicyType())) {
                    orderBy.append(" n.pay_money" + CommonConstant.COMMA_SPLIT);
                } else if (NetbankConstant.POLICY_TYPE_3.equals(sort.getPolicyType())) {
                    orderBy.append(" n.create_date" + CommonConstant.COMMA_SPLIT);
                } else if (NetbankConstant.POLICY_TYPE_4.equals(sort.getPolicyType())) {
                    orderBy.append(" n.is_quick desc" + CommonConstant.COMMA_SPLIT);
                } else if (NetbankConstant.POLICY_TYPE_5.equals(sort.getPolicyType())) {
                    orderBy.append(" n.area_sign" + CommonConstant.COMMA_SPLIT);
                }
            }
            sql.append(orderBy.substring(0, orderBy.length() - 1));
        } else {
            //默认根据创建时间排序
            orderBy.append(" n.create_date ");
            sql.append(orderBy);
        }
        return this.nisBillheadDao.listByBisPayPolicy(sql.toString(), params);
    }

    /**
     * 获取字段比较 运算符
     *
     * @param operator
     * @return
     */
    public static String getCompareSql(String operator) {
        if (FundWarnConstant.BJ_OPERATOR_GT.equals(operator)) {
            return " > ";
        } else if (FundWarnConstant.BJ_OPERATOR_GTEQ.equals(operator)) {
            return " >= ";
        } else if (FundWarnConstant.BJ_OPERATOR_LT.equals(operator)) {
            return " < ";
        } else if (FundWarnConstant.BJ_OPERATOR_LTEQ.equals(operator)) {
            return " <= ";
        } else {
            return " = ";
        }
    }

    /**
     * 获取条件关系 运算符
     *
     * @param numOperator
     * @return
     */
    public static String getRelationSql(String numOperator) {
        if (FundWarnConstant.GX_OPERATOR_OR.equals(numOperator)) {
            return " OR ";
        } else if (FundWarnConstant.GX_OPERATOR_AND.equals(numOperator)) {
            return " AND ";
        } else {
            return " ";
        }
    }

    /**
     * 根据策略获取付款单
     *
     * @param policyId
     */
    public BigDecimal getSumPayMoneyByPolicyId(String policyId) {
        BigDecimal payMoney = BigDecimal.ZERO;
        List<NisBillheadVO> list = this.nisBillheadDao.listByPolicyId(policyId);
        for (NisBillheadVO nisBillhead : list) {
            payMoney = payMoney.add(nisBillhead.getPayMoney());
        }
        return payMoney;
    }

    /**
     * 按支付策略自动办理付款（定时任务入口）。限额占用失败回滚并返回失败结果；
     * 命中银行端落地限额时单据预置 95 + 落地标识=0 等内管审批（不生成指令、事务提交）。
     *
     * @param nisBillheadVO 策略匹配的付款单
     * @return isSuccess=false = 未生成指令：限额占用失败（单据回滚）或已提交落地限额审批
     *         （单据 95 待审批，通过后由回调 {@link #completeTranApproveAutoPay} 自动生效）
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentLimitResultDto autoPayBill(NisBillheadVO nisBillheadVO) {
        NisBillheadVO nisBillhead = nisBillheadDao.getByIdUnion(nisBillheadVO.getId());
        //内部资金计划内转标识
        String innerSign = getInnerSign(nisBillheadVO);
        nisBillhead.setInnerSign(innerSign);
        nisBillhead.setVoucherNo(nisBillhead.getId());
        nisBillhead.setIsAutoPay(CommonConstant.YES);
        nisBillhead.setStatus(NetbankConstant.STATUE_CHECK);
        nisBillhead.setPolicyId(nisBillheadVO.getPolicyId());
        // 将期望日期为今天以前的自动支付付款单的期望付款日期改成今天
        if (DateUtils.getDate().compareTo(nisBillhead.getWishPayday()) > 0) {
            nisBillhead.setWishPayday(DateUtils.parseDate(DateUtils.getDate()));
        }
        nisBillhead.setUpdateBy(nisBillhead.getCreateBy());
        nisBillhead.setUpdateDate(DateUtils.getDate());
        //自动付款，控制原状态为0暂存
        Integer[] oldStatus = {NetbankConstant.STATUE_TEMP};
        this.updateNisBillheadStatus(nisBillhead, oldStatus);
        UserDTO limitUser = userService.getByLoginName(nisBillhead.getCreateBy());
        if (limitUser == null || StringUtils.isBlank(limitUser.getId())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_AUTO_PAY_APPLICANT_MISSING);
        }
        NisBillhead limitEntity = this.get(nisBillhead.getId(), true);
        // 自动付款也需在生成指令前核对限额；以创建人为操作人，已有占用核对、无占用补占。
        PaymentLimitResultDto limitResult = occupyEnterpriseLimit(limitEntity, limitUser, true);
        if (limitResult == null || !limitResult.isSuccess()) {
            // 限额占用失败：回滚（单据维持 0 暂存），任务记录原因后跳过、下轮可重试
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return limitResult == null
                    ? PaymentLimitResultDto.failure("LIMIT_RESULT_MISSING", "内管未返回限额处理结果")
                    : limitResult;
        }
        // 落地限额（与人工 flowCompleted 口径一致）：仅线上指令型付款触发；命中不生成指令，单据保持 95 + 落地标识=0
        // 等内管审批，通过/打回由回调 handler 按 isAutoPay 分流到 completeTranApproveAutoPay 收尾
        if (isOnlineInstruction(limitEntity, fcVoucherTypeService.getByIdUnion(limitEntity.getVoucherTypeId()))
                && isLandingLimitReached(limitEntity, limitUser)) {
            // 落地标识=待执行（内容未跑，等内管回调；状态更新 HQL 不含此列，单独落库）
            nisBillheadDao.updateLandingLimitResult(limitEntity.getId(), EnterpriseLimitControlService.LANDING_RESULT_PENDING);
            nisMessageService.saveMessage(limitEntity.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT,
                    "已提交银行端落地限额审批，审批通过后自动生效", NisMessageConstant.BUS_TYPE_PAY, limitUser);
            return PaymentLimitResultDto.failure("LANDING_APPROVAL_PENDING", "已提交银行端落地限额审批，审批通过后自动生效");
        }

        UserDTO user = new UserDTO();
        user.setUsername("auto");
        user.setLoginName("auto");
        // 指令段（含自动记账与日志）与落地审批通过回调共用
        this.saveAutoPayExc(nisBillhead, user);
        return PaymentLimitResultDto.success();
    }

    /**
     * 自动支付指令段：生成付款指令并（sendMode=YES）调用记账，非落地路径与落地审批通过回调共用。
     *
     * @return 生成的付款指令（记账类型时可能为 null，调用方无需关心）
     */
    private BisExcDTO saveAutoPayExc(NisBillheadVO nisBillhead, ICurrentUser user) {
        BisExcDTO bisExcDTO = this.saveBisExc(nisBillhead, user);
        //自动指令调用记账
        if (CommonConstant.YES.equalsIgnoreCase(nisBillhead.getSendMode()) && Objects.nonNull(bisExcDTO)) {
            BisExcDTO excDTO = BeanUtils.copyNewInstanceProperties(bisExcDTO, BisExcDTO.class);
            billheadBisExcBackService.sendBisExcAccounting(excDTO, user);
        }
        //保存日志
        nisMessageService.saveMessage(nisBillhead.getId(), NisMessageConstant.MESSAGE_TYPE_AUTO_PAY, NisMessageConstant.NIS_LOG_AUTO_PAY, NisMessageConstant.BUS_TYPE_PAY, user);
        return bisExcDTO;
    }

    /**
     * 自动支付落地限额审批回调收尾（银行端->企业端，付款单落地 handler 按单据 isAutoPay 分流，与人工落地回调
     * {@link #completeTranApprove} 同构）。通过=直接执行自动支付指令段 {@link #saveAutoPayExc}（单据保持 95）；
     * 打回=95-&gt;-1 打回制单处，本事务提交后由回调 handler 释放限额占用。落地标识条件翻转（仅 0 可翻）作互斥闸门，0 行=已被并发收尾，
     * 抛状态变更异常（重试由 handler 幂等分支拦截）。内管回调可能不带审批人 user，取单据 updateBy（兜底 createBy）。
     */
    public void completeTranApproveAutoPay(String businessId, boolean pass, ICurrentUser user) {
        NisBillhead entity = nisBillheadDao.get(businessId, true);
        if (entity == null) {
            throw new BusinessException(NetbankMessageEnum.BILL_CHECKED);
        }
        if (user == null) {
            user = buildFallbackUser(entity);
        }
        if (!isLandingPending(entity)) {
            // 只接受"95+落地标识=0"收尾：已翻转（95+A / -1+R）由 handler 幂等分支拦截，能走到此处即并发竞态
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        // 互斥闸门：仅 0 可翻，0 行 = 已被并发回调收尾
        if (nisBillheadDao.updateLandingLimitResultIfPending(entity.getId(),
                pass ? EnterpriseLimitControlService.LANDING_RESULT_AGREE
                     : EnterpriseLimitControlService.LANDING_RESULT_REJECT) < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        NisBillheadVO vo = nisBillheadDao.getByIdUnion(entity.getId());
        if (vo == null) {
            throw new BusinessException(NetbankMessageEnum.BILL_CHECKED);
        }
        if (pass) {
            // 通过：执行自动支付指令段（单据保持 95）
            this.saveAutoPayExc(vo, user);
        } else {
            // 打回：95->-1 打回制单处（voucherStat 置空、CAS 白名单 {95}，与 approvePassEnd 打回口径一致）
            vo.setStatus(NetbankConstant.STATUE_BACK);
            vo.setUpdateBy(user.getLoginName());
            vo.setUpdateDate(DateUtils.getDate());
            vo.setVoucherStat(null);
            this.updateNisBillheadStatus(vo, new Integer[]{NetbankConstant.STATUE_CHECK});
            // 限额释放由回调 handler 在当前打回事务提交后执行。
        }
        // 审批日志（同 completeTranApprove 口径，统一在此补记）
        Integer messageType = pass ? NisMessageConstant.MESSAGE_TYPE_CHECK_PASS : NisMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = pass ? "银行端落地限额审批通过，自动支付生效" : "银行端落地限额审批打回，自动支付未生效";
        nisMessageService.saveMessage(entity.getId(), messageType, message, NisMessageConstant.BUS_TYPE_PAY, user);
    }

    /**
     * 工作流审批校验
     *
     * @param params       Map<流程分类KEY, 业务ID列表>
     * @param workflowPass 审批操作标识：1，同意；3，打回上一级；4，打回制单处。
     * @param isNeedVerify 是否要校验工作流审批：true表示要校验；false表示不用校验。用户在看到校验失败提示信息后，仍然确定提交审批，则isNeedVerify为false。
     * @throws WorkFlowVerifyException
     */
    @Override
    public void submitVerify(Map<String, List<String>> params, String workflowPass, boolean isNeedVerify) throws WorkFlowVerifyException {
        if (isNeedVerify) {// 模拟校验审批
            if ("1".equals(workflowPass)) {
                //银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
                String bankaccOver = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BANKACCOVER_APPROVE).getValue();
                //付款余额控制方式  0-账户余额 1-可用余额
                String balControlType = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BALCONTROLTYPE).getValue();
                if ("0".equals(bankaccOver)) {
                    return;
                }
                List<String> ids = params.get(NisWorkFlowConstant.NIS_BILLHEAD);
                List<NisBillheadVO> listBill = this.listByIdsForApprove(ids);
                //账户余额（批量提交校验账户余额)
                HashMap<String, BigDecimal> balMap = new HashMap<>();
                for (NisBillheadVO nisBillhead : listBill) {
                    //校验账户余额
                    if (NetbankConstant.PAY_TYPE_34.equals(nisBillhead.getSysVoucherCode())) {
                        BigDecimal curBal = nisBillhead.getBalance();
                        if (CommonConstant.YES.equals(balControlType)) {
                            curBal = nisBillhead.getAvailBal();
                        } else {
                            if (balMap.containsKey(nisBillhead.getPayerAcc())) {
                                curBal = balMap.get(nisBillhead.getPayerAcc());
                            }
                        }
                        if (nisBillhead.getPayMoney().compareTo(curBal) > 0) {
                            if ("1".equals(bankaccOver)) {
                                //1-只提醒不控制
                                throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_CONFIRM_BALANCE, WorkFlowConstant.VERITY_STATUS_CONFIRM);
                            } else if ("2".equals(bankaccOver)) {
                                //2-控制
                                throw new WorkFlowVerifyException(WorkFlowMessageEnum.NETBANK_MESSAGE_BALANCE, WorkFlowConstant.VERITY_STATUS_ALERT);
                            }
                        }
                        curBal = curBal.subtract(nisBillhead.getPayMoney());
                        balMap.put(nisBillhead.getPayerAcc(), curBal);
                    }
                }
            }
        }
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
            //单位有在途的付款单据，不允许禁用！
            int num = this.nisBillheadDao.getBillheadCountByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.NIS,
                        SysConfigConstant.BusType.NIS_BILLHEAD,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BILLHEAD_ERROR)));
            }
            //单位有记账中的付款单，不允许禁用！
            num = this.nisBillheadDao.getBillheadCountByCorpIdForTally(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.FCL,
                        SysConfigConstant.BusType.FCL_BILLHEAD_TALLY,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BILLHEAD_TALLY_ERROR)));
            }
            return verifyErrorDTOS;
        }
        return null;
    }

    /**
     * 待记账查询
     */
    public List<NisBillheadDTO> queryTallyUnions() {
        List<NisBillheadDTO> list = null;
        //如果是手工记账
        if (NetbankConstant.ACCOUNTMODE_MANUAL.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
            if (NetbankConstant.ACCOUNTORDER_ACCOUNTPAY.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue())) {
                //先记账后付款
                list = nisBillheadDao.queryTallyUnionByAccountPay();
            } else {
                //先付款后记账
                list = nisBillheadDao.queryTallyByPayAccount();
            }
        }
        if (null == list) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 根据单位获取有权限的用户
     *
     * @param corpId
     * @param user
     * @return
     */
    public List<UserDTO> getUserByCorpId(String corpId, ICurrentUser user) {
        //付款办理新增资源
        String resource = ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_ADD;
        List<UserDTO> userDTOList = new ArrayList<>();
        List<String> corpIdList = new ArrayList<>();
        //如果单位为空过滤有权限的单位
        if (EmptyUtils.isEmpty(corpId)) {
            corpIdList = corpService.getUserCorpIds(resource, user.getId());
        } else {
            String[] corpIds = corpId.split(CommonConstant.COMMA_SPLIT);
            Collections.addAll(corpIdList, corpIds);
        }
        List<String> userList = userService.listUserIdsByCorpIdAndPermission(corpIdList, resource);
        if (!EmptyUtils.isEmpty(userList)) {
            userDTOList = userService.listByUserIds(userList);
        }
        return userDTOList;
    }

    /**
     * 判断是否存在相同的时间戳
     *
     * @param timeStamp
     * @return
     */
    public boolean isExistTimeStamp(String timeStamp) {
        if (StringUtils.isNotBlank(timeStamp)) {
            List<NisBillhead> list = this.nisBillheadDao.listBy("timeStamp", timeStamp);
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
    public void updateNisBillheadStatus(NisBillheadVO vo, Integer[] oldStatus) {
        int result = this.nisBillheadDao.updateNisBillheadStatus(vo, oldStatus);
        if (result < 1) {
            throw new BusinessException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, vo.getBillNo());
        }
    }

    /**
     * 设置账户可用余额
     *
     * @param resultList 返回列表集合
     */
    public void getAccountAvailBal(List<NisBillheadVO> resultList) {
        resultList.forEach(nisBillhead -> {
            if (NetbankConstant.DIRECTPAYFLAG_Y.equals(nisBillhead.getDirectPayFlag())) {
                BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
                if (!EmptyUtils.isEmpty(bal)) {
                    BigDecimal avaiBal = BigDecimal.ZERO;
                    if (!EmptyUtils.isEmpty(bal.getAvailBal())) {
                        avaiBal = bal.getAvailBal();
                    }
                    nisBillhead.setAvailBal(avaiBal);
                }
            } else {
                FcOnlineLedgerDTO bal = fcOnlineLedgerService.getByBlnctrAcc(nisBillhead.getPayerAcc());
                if (!EmptyUtils.isEmpty(bal)) {
                    //内部账号余额 = 可用余额 - 冻结金额
                    BigDecimal checkBal = BigDecimal.ZERO;
                    BigDecimal freeMoney = BigDecimal.ZERO;
                    if (!EmptyUtils.isEmpty(bal.getCheckBalance())) {
                        checkBal = bal.getCheckBalance();
                    }
                    if (!EmptyUtils.isEmpty(bal.getFreezeMoney())) {
                        freeMoney = bal.getFreezeMoney();
                    }
                    nisBillhead.setAvailBal(checkBal.subtract(freeMoney));
                }
            }
        });


    }

//    public List<NisBillheadVO> getAccountAvailBalByList(List<NisBillheadVO> resultList) {
//    	List<String> listPayerAcc = (List<String>) resultList.stream().map(NisBillheadVO::getPayerAcc).filter(payerAcc -> payerAcc != null && !payerAcc.isEmpty()).collect(Collectors.toList());
//    	List<String> listCurId = (List<String>) resultList.stream().map(NisBillheadVO::getCurId).distinct().filter(curId -> curId != null && !curId.isEmpty()).collect(Collectors.toList());
//    	//通过in(list)查询
//    	List<BisAccBalDTO> balList = bisAccBalService.getLastBisAccBalByList(listPayerAcc, listCurId);
//    	List<FcOnlineLedgerDTO> fcOnlineLedgerList = fcOnlineLedgerService.getByBlnctrAccByList(listPayerAcc);
//    	//转换map
//    	Map<String, BisAccBalDTO> mapBisAccBal = new HashMap<String, BisAccBalDTO>();
//    	Map<String,FcOnlineLedgerDTO> mapfcOnlineLedger = new HashMap<String,FcOnlineLedgerDTO>();
//    	if(!CollectionUtils.isEmpty(balList)) {
//    		mapBisAccBal = balList.stream().collect(
//        			Collectors.toMap( p -> StringUtils.joinWith("-", p.getBankAcc(), p.getCurId()), Function.identity(),(existing, replacement) -> existing));
//    	}
//
//    	if(!CollectionUtils.isEmpty(fcOnlineLedgerList)) {
//    		mapfcOnlineLedger = fcOnlineLedgerList.stream().collect(Collectors.toMap(FcOnlineLedgerDTO::getAccount, Function.identity()));
//    	}
//    	//遍历设置值
//    	for(NisBillheadVO nisBillhead : resultList) {
//            if (NetbankConstant.DIRECTPAYFLAG_Y.equals(nisBillhead.getDirectPayFlag())) {
//            	if(!MapUtils.isEmpty(mapBisAccBal)) {
//	            	if(mapBisAccBal.containsKey(nisBillhead.getPayerAcc()+"-"+nisBillhead.getCurId())) {
//	            		BisAccBalDTO bal = mapBisAccBal.get(nisBillhead.getPayerAcc()+"-"+nisBillhead.getCurId());
//	                    if (!EmptyUtils.isEmpty(bal)) {
//	                        BigDecimal avaiBal = BigDecimal.ZERO;
//	                        if(!EmptyUtils.isEmpty(bal.getAvailBal())){
//	                            avaiBal = bal.getAvailBal();
//	                        }
//	                        nisBillhead.setAvailBal(avaiBal);
//	                    }
//	            	 }
//	              }
//            } else {
//            	if(!MapUtils.isEmpty(mapfcOnlineLedger)) {
//	            	if(mapfcOnlineLedger.containsKey(nisBillhead.getPayerAcc())) {
//	            		FcOnlineLedgerDTO bal = mapfcOnlineLedger.get(nisBillhead.getPayerAcc());
//	                    if (!EmptyUtils.isEmpty(bal)) {
//	                        //内部账号余额 = 可用余额 - 冻结金额
//	                        BigDecimal checkBal = BigDecimal.ZERO;
//	                        BigDecimal freeMoney = BigDecimal.ZERO;
//	                        if(!EmptyUtils.isEmpty(bal.getCheckBalance())){
//	                            checkBal = bal.getCheckBalance();
//	                        }
//	                        if(!EmptyUtils.isEmpty(bal.getFreezeMoney())){
//	                            freeMoney = bal.getFreezeMoney();
//	                        }
//	                        nisBillhead.setAvailBal(checkBal.subtract(freeMoney));
//	                    }
//	            	}
//                }
//            }
//    	}
//        return resultList;
//    }

    /**
     * 获取账户在途余额
     *
     * @param account
     * @param curId
     * @return
     */
    public BigDecimal getAccountBal(String account, String curId, String isAccount) {
        BigDecimal bal = BigDecimal.ZERO;
        if (StringUtils.isBlank(account)) {
            return bal;
        }
        if (CommonConstant.YES.equals(isAccount)) {
            FcOnlineLedgerDTO accBal = fcOnlineLedgerService.getByBlnctrAcc(account);
            if (accBal != null) {
                //内部账号余额 = 可用余额 - 冻结金额
                bal = accBal.getCheckBalance().subtract(accBal.getFreezeMoney());
            }
        } else {
            BisAccBalDTO bankBal = bisAccBalService.getLastBisAccBalBy(account, curId);
            if (bankBal != null) {
                //银行账户可用余额
                bal = bankBal.getAvailBal();
            }
        }
        if (bal == null) {
            bal = BigDecimal.ZERO;
        }
        //获取账户在途余额
        BigDecimal useBal = nisBillheadDao.getUseBal(account, curId);
        bal = bal.subtract(useBal);
        return bal;
    }

    /**
     * 根据付款单号获取对应的流水
     *
     * @param id 付款单号
     * @return
     */
    public List<EBankMoreBillVO> getNisBillheadDtl(String id) {
        return this.nisBillheadDao.getMoredtl(id);
    }

    /**
     * 根据流水号获取对应的电子回单号
     *
     * @param id 流水号
     * @return
     */
    public List<BisBankDtlBillVO> getDtlreceipt(String id) {
        return this.nisBillheadDao.getMorereceipt(id);
    }

    public DataTablesOutput setDtlReceipt(DataTablesOutput out) {
        if (!EmptyUtils.isEmpty(out.getData())) {
            for (int i = 0; i < out.getData().size(); i++) {
                NisBillheadVO nisBillheadVO = (NisBillheadVO) out.getData().get(i);
                List<EBankMoreBillVO> eBankMoreBillVOList = this.getNisBillheadDtl(nisBillheadVO.getId());
                nisBillheadVO.setDtlNum(eBankMoreBillVOList.size());
                if (eBankMoreBillVOList.size() == 1) {
                    nisBillheadVO.setDtlId(eBankMoreBillVOList.get(0).getDtlId());
                }
                List<BisBankDtlBillVO> bisBankDtlBillVOList = new ArrayList<>();
                for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOList) {
                    for (BisBankDtlBillVO bisBankDtlBillVO : this.getDtlreceipt(eBankMoreBillVO.getDtlId())) {
                        bisBankDtlBillVOList.add(bisBankDtlBillVO);
                    }
                }
                nisBillheadVO.setReceiptNum(bisBankDtlBillVOList.size());
                if (bisBankDtlBillVOList.size() == 1) {
                    BisBankReceiptDtlDTO bisBankReceiptDtlDTO = bisBankReceiptDtlService.getById(bisBankDtlBillVOList.get(0).getReceiptId());
                    if (!EmptyUtils.isEmpty(bisBankReceiptDtlDTO)) {
                        nisBillheadVO.setReceiptFlag(CommonConstant.NO); //电子回单
                        nisBillheadVO.setReceiptId(bisBankReceiptDtlDTO.getId());
                    } else {
                        //电子回单id为空，则获取历史电子回单
                        BisBankReceiptDtlHisVO bisBankReceiptDtlHis = bisBankReceiptDtlHisService.getByIdUnion(bisBankReceiptDtlDTO.getId());
                        if (!EmptyUtils.isEmpty(bisBankReceiptDtlHis.getId())) {
                            nisBillheadVO.setReceiptFlag(CommonConstant.YES);
                            nisBillheadVO.setReceiptId(bisBankReceiptDtlDTO.getId());
                        }
                    }
                }
            }
        }
        return out;
    }

    /**
     * 获取ERP待处理单据
     *
     * @return
     */
    public List<NisBillheadVO> listErpTodoBill() {
        return nisBillheadDao.listErpTodoBill();
    }

    public List<NisBillheadVO> listByErpIds(List<String> erpIds) {
        return nisBillheadDao.listByErpIds(erpIds);
    }


    /**
     * 根据付款ID获取付款信息
     *
     * @param ids
     * @return
     */
    public List<NisBillheadVO> listByIdUnion(String ids) {
        List<NisBillheadVO> list = nisBillheadDao.listByIdUnion(ids);
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        Map<String, String> curMap = btCurrencyService.getCurrencyNameMap();
        Map<String, RegionDTO> mapRegion = regionService.getRegionMap();
        for (NisBillheadVO vo : list) {
            if (corpMap.containsKey(vo.getCorpId())) {
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
            }
            if (curMap.containsKey(vo.getCurId())) {
                vo.setCurName(curMap.get(vo.getCurId()));
            }
            if (mapRegion.containsKey(vo.getProv())) {
                vo.setProvName(mapRegion.get(vo.getProv()).getProvName());
            }
            if (mapRegion.containsKey(vo.getCity())) {
                vo.setCityName(mapRegion.get(vo.getCity()).getName());
            }
        }
        return list;
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param ids
     * @return
     */
    public List<NisBillheadVO> listByIdsForApprove(List<String> ids) {
        List<NisBillheadVO> listBill = nisBillheadDao.listByIdsForApprove(ids);
        //账户余额（批量提交校验账户余额)
        HashMap<String, BigDecimal> balMap = new HashMap<>();
        HashMap<String, BigDecimal> availBalMap = new HashMap<>();
        for (NisBillheadVO nisBillhead : listBill) {
            // 账户余额默认为0
            nisBillhead.setBalance(BigDecimal.ZERO);
            nisBillhead.setAvailBal(BigDecimal.ZERO);
            if (NetbankConstant.PAY_TYPE_34.equals(nisBillhead.getSysVoucherCode())) {
                //获取账户在途余额
                if (balMap.containsKey(nisBillhead.getPayerAcc())) {
                    nisBillhead.setBalance(balMap.get(nisBillhead.getPayerAcc()));
                    nisBillhead.setAvailBal(availBalMap.get(nisBillhead.getPayerAcc()));
                } else {
                    BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
                    if (bal != null) {
                        nisBillhead.setBalance(bal.getBal());
                    }
                    BigDecimal useBal = nisBillheadDao.getUseBal(nisBillhead.getPayerAcc(), nisBillhead.getCurId());
                    BigDecimal availBal = nisBillhead.getBalance().subtract(useBal);
                    nisBillhead.setAvailBal(availBal.add(nisBillhead.getPayMoney()));
                    availBalMap.put(nisBillhead.getPayerAcc(), nisBillhead.getAvailBal());
                    balMap.put(nisBillhead.getPayerAcc(), nisBillhead.getBalance());
                }
            }
        }
        return listBill;
    }

    @Override
    public void taskCompleted(IWorkFlowResult workFlowResult) {
    }

    public List<String> listBillId(List<String> listId) {
        return nisBillheadDao.listBillId(listId);
    }

    public List<String> listPayIdByBill(List<String> listId) {
        return nisBillheadDao.listPayIdByBill(listId);
    }

    public List<NisBillheadDTO> getCmsInlSendId(List<String> ids) {
        return nisBillheadDao.getCmsInlSendId(ids);
    }

    public int corpLimitCheck(String id, String corpId, String curId, BigDecimal balance, String temp, String wishPayDate) {
        Date date = DateUtils.getDate();//当前系统日期
        Date lastMonthDate = DateUtils.getLastMonthDate(date);
        Date firstMonthDate = DateUtils.getFirstMonthDate(date);
        if (StringUtils.isNotBlank(wishPayDate)) {
            date = DateUtils.parse(wishPayDate, "yyyy-MM-dd");
            lastMonthDate = DateUtils.getLastMonthDate(date);
            firstMonthDate = DateUtils.getFirstMonthDate(date);
        } else {
            wishPayDate = DateUtils.format(date, "yyyy-MM-dd");
        }
        String format = DateUtils.format(date, "yyyy-MM-dd");
        String lastFormat = DateUtils.format(lastMonthDate, "yyyy-MM-dd");
        String firstFormat = DateUtils.format(firstMonthDate, "yyyy-MM-dd");
        //获取单位，日累计限额，月累计限额
        CorpDTO corp = corpService.get(corpId);
        if (corp == null) {
            corp = corpService.getCorpIdByName(corpId);
            if (corp == null) {
                BtBankAccDTO bankAccById = bankAccService.getBankAccById(corpId);
                corp = corpService.get(bankAccById.getCorpId());
                if (corp == null) {
                    return 0;
                } else {
                    corpId = corp.getId();
                }
            } else {
                corpId = corp.getId();
            }
        }
        //校验限额是否启动
        if (corp.getIsLimitQuota() == null || corp.getIsLimitQuota() == 0) {
            return 0;
        }
        if (CommonConstant.YES.equals(temp)) {//新增数据校验
            //判断日累计限额
            if (corp.getDayLimitQuota() != null && corp.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSumForCorp(corpId, curId, format, format);
                if (corp.getDayLimitQuota().compareTo(dayLimit.add(balance)) < 0) {
                    return 1;
                }
            }
            //判断月累计限额
            if (corp.getMonthLimitQuota() != null && corp.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSumForCorp(corpId, curId, firstFormat, lastFormat);
                if (corp.getMonthLimitQuota().compareTo(monthLimit.add(balance)) < 0) {
                    return 2;
                }
            }
        } else if (CommonConstant.NO.equals(temp)) {//修改数据校验
            NisBillheadDTO money = this.getMoneyById(id);
            if (money != null) {
                //如果原时间不等于现时间，说明用户更改了期望日期，按更改时间查询数据
                String originalDate = DateUtils.format(money.getCreateDate(), "yyyy-MM-dd");
                if (!originalDate.equals(wishPayDate)) {
                    format = wishPayDate;
                    Date parse = DateUtils.parse(format, "yyyy-MM-dd");
                    lastMonthDate = DateUtils.getLastMonthDate(parse);
                    firstMonthDate = DateUtils.getFirstMonthDate(parse);
                    lastFormat = DateUtils.format(lastMonthDate, "yyyy-MM-dd");
                    firstFormat = DateUtils.format(firstMonthDate, "yyyy-MM-dd");
                }
            } else {
                money = new NisBillheadDTO();
                money.setPayMoney(new BigDecimal(0));
            }
            //判断日累计限额
            if (corp.getDayLimitQuota() != null && corp.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSumForCorp(corpId, curId, format, format);
                if (corp.getDayLimitQuota().compareTo(dayLimit.subtract(money.getPayMoney()).add(balance)) < 0) {
                    return 1;
                }
            }
            //判断月累计限额
            if (corp.getMonthLimitQuota() != null && corp.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSumForCorp(corpId, curId, firstFormat, lastFormat);
                if (corp.getMonthLimitQuota().compareTo(monthLimit.subtract(money.getPayMoney()).add(balance)) < 0) {
                    return 2;
                }
            }
        } else {//批量提交数据校验
            //判断日累计限额
            if (corp.getDayLimitQuota() != null && corp.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSumForCorp(corpId, curId, format, format);
                if (corp.getDayLimitQuota().compareTo(dayLimit) < 0) {
                    return 1;
                }
            }
            //判断月累计限额
            if (corp.getMonthLimitQuota() != null && corp.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSumForCorp(corpId, curId, firstFormat, lastFormat);
                if (corp.getMonthLimitQuota().compareTo(monthLimit) < 0) {
                    return 2;
                }
            }
        }
        return 0;
    }

    public int bankAccLimitCheck(String id, String bankAcc, String curId, BigDecimal balance, String temp, String wishPayDate) {
        Date date = DateUtils.getDate();//当前系统日期
        Date lastMonthDate = DateUtils.getLastMonthDate(date);
        Date firstMonthDate = DateUtils.getFirstMonthDate(date);
        if (StringUtils.isNotBlank(wishPayDate)) {
            date = DateUtils.parse(wishPayDate, "yyyy-MM-dd");
            lastMonthDate = DateUtils.getLastMonthDate(date);
            firstMonthDate = DateUtils.getFirstMonthDate(date);
        } else {
            wishPayDate = DateUtils.format(date, "yyyy-MM-dd");
        }
        String format = DateUtils.format(date, "yyyy-MM-dd");
        String lastFormat = DateUtils.format(lastMonthDate, "yyyy-MM-dd");
        String firstFormat = DateUtils.format(firstMonthDate, "yyyy-MM-dd");
        //获取账号，单日限额，日累计限额，月累计限额
        BtBankAccDTO acc = bankAccService.getBankAccInfo(bankAcc);
        if (acc == null) {
            return 0;
        }
        //校验限额是否启动
        if (acc.getIsLimitQuota() == null || CommonConstant.NO.equals(acc.getIsLimitQuota())) {
            return 0;
        }
        //校验账号，如果不是在线就退出
        if (CommonConstant.NO.equals(acc.getIsOnline())) {
            return 0;
        }
        BigDecimal rate = new BigDecimal(1);
        if (curId != null) {
            CorpDTO corpDTO = corpService.get(acc.getCorpId());
            rate = fcExcrateAdjustService.getExchangeRateBy(curId, corpDTO.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
            if (rate == null) {
                rate = new BigDecimal(1);
            }
            balance = balance.multiply(rate);
        }
        if (CommonConstant.YES.equals(temp)) {//新增数据校验
            //判断日累计限额
            if (acc.getDayLimitQuota() != null && acc.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSum(bankAcc, curId, format, format);
                if (acc.getDayLimitQuota().compareTo(dayLimit.add(balance)) < 0) {
                    return 2;
                }
            }
            //判断月累计限额
            if (acc.getMonthLimitQuota() != null && acc.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSum(bankAcc, curId, firstFormat, lastFormat);
                if (acc.getMonthLimitQuota().compareTo(monthLimit.add(balance)) < 0) {
                    return 3;
                }
            }
        } else if (CommonConstant.NO.equals(temp)) {//修改数据校验
            NisBillheadDTO money = this.getMoneyById(id);//查询修改数据
            if (money != null) {
                //如果原时间不等于现时间，说明用户更改了期望日期，按更改时间查询数据
                String originalDate = DateUtils.format(money.getCreateDate(), "yyyy-MM-dd");
                if (!originalDate.equals(wishPayDate)) {
                    format = wishPayDate;
                    Date parse = DateUtils.parse(format, "yyyy-MM-dd");
                    lastMonthDate = DateUtils.getLastMonthDate(parse);
                    firstMonthDate = DateUtils.getFirstMonthDate(parse);
                    lastFormat = DateUtils.format(lastMonthDate, "yyyy-MM-dd");
                    firstFormat = DateUtils.format(firstMonthDate, "yyyy-MM-dd");
                }
            } else {
                money = new NisBillheadDTO();
                money.setPayMoney(new BigDecimal(0));
            }
            //判断日累计限额
            if (acc.getDayLimitQuota() != null && acc.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSum(bankAcc, curId, format, format);
                if (acc.getDayLimitQuota().compareTo(dayLimit.subtract(money.getPayMoney()).add(balance)) < 0) {
                    return 2;
                }
            }
            //判断月累计限额
            if (acc.getMonthLimitQuota() != null && acc.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSum(bankAcc, curId, firstFormat, lastFormat);
                if (acc.getMonthLimitQuota().compareTo(monthLimit.subtract(money.getPayMoney()).add(balance)) < 0) {
                    return 3;
                }
            }
        } else {//批量提交数据校验
            //判断日累计限额
            if (acc.getDayLimitQuota() != null && acc.getDayLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal dayLimit = this.getSum(bankAcc, curId, format, format);
                if (acc.getDayLimitQuota().compareTo(dayLimit) < 0) {
                    return 2;
                }
            }
            //判断月累计限额
            if (acc.getMonthLimitQuota() != null && acc.getMonthLimitQuota().compareTo(BigDecimal.ZERO) != 0) {//限额不为零才判断
                BigDecimal monthLimit = this.getSum(bankAcc, curId, firstFormat, lastFormat);
                if (acc.getMonthLimitQuota().compareTo(monthLimit) < 0) {
                    return 3;
                }
            }
        }
        //判断单次支付限额
        if (acc.getPayLimitQuota().compareTo(balance) < 0) {
            return 1;
        }
        return 0;
    }

    public int updateEditPassSign(String businessId, String tallySign) {
        return nisBillheadDao.updateEditPassSign(businessId, tallySign);
    }

    public int updateAutoCheckSign(String id, String orderNo, String userId, String voucherNo, Integer statueTallyCheckPass) {
        return nisBillheadDao.updateAutoCheckSign(id, orderNo, userId, voucherNo, statueTallyCheckPass);
    }

    public int updateTallySign(String id, String orderNo, String userId) {
        return nisBillheadDao.updateTallySign(id, orderNo, userId);
    }

    public int updateCheckSign(String id, String voucherNo, Integer statueTallyCheckPass) {
        return nisBillheadDao.updateCheckSign(id, voucherNo, statueTallyCheckPass);
    }

    public int updateEditBackSign(String businessId, String tallySign) {
        return nisBillheadDao.updateEditBackSign(businessId, tallySign);
    }

    public int updateCheckBackSign(String id, String voucherNo) {
        return nisBillheadDao.updateCheckBackSign(id, voucherNo);
    }

    /**
     * 指令变更后同步付款单数据
     */
    public int updateFromBisExcChange(BisExcChangeMsg bisExcChangeMsg) {
        int rows = nisBillheadDao.updateVoucherState(bisExcChangeMsg.getCbsBillId(), bisExcChangeMsg.getVoucherStat());
        if (BdlinkConstant.SUCCESS.equals(bisExcChangeMsg.getVoucherStat())) {
            NisBillhead entity = this.get(bisExcChangeMsg.getCbsBillId(), true);
            if (isExternalOnlineBankPayment(entity)) {
                // 仅34-外付网银指令成功后将在途占用转为成功用量。
                EnterpriseLimitControlService.requireSuccess(enterpriseLimitControlService.markSuccess(UserContextHelper.helper().getTenantId(),
                        EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD, bisExcChangeMsg.getCbsBillId()));
            }
        }
        return rows;
    }

    /**
     * 应收选票选择付款账号
     *
     * @param nisBillhead
     * @param user
     */
    public void batchSelectAccebill(NisBillheadVO nisBillhead, ICurrentUser user) {
        if (NetbankConstant.PAY_TYPE_50.equals(nisBillhead.getSysVoucherCode()) && StringUtils.isNotBlank(nisBillhead.getBillId())
                && !NetbankConstant.ISPAYED_1.equals(nisBillhead.getIsPayed())) {
            //1.解锁原票据
            String billIds = nisBillService.getBillIds(nisBillhead.getId());
            if (!StringUtils.isBlank(billIds)) {
                this.iAccebillService.batchUnLock(billIds.split(CommonConstant.COMMA_SPLIT), user);
                //解锁做额度释放
                this.iAccebillService.batchPayLockMoneySubtract(nisBillService.listNisBillVoByPayId(nisBillhead.getId()));
                // 根据付款单id删除对应的票据付款中间表集合
                delBillPayMiddleByPayId(nisBillhead.getId());
                // 删除原付款单挂接的票据信息
                nisBillService.delByPayId(nisBillhead.getId());
            }
            //2.锁定新票据
            //保存票据关联信息
            nisBillService.saveOrUpdateSync(nisBillhead.getId(), nisBillhead.getBillId(), nisBillhead.getPayMoney().toString());
            String[] abAccrecordsIds = nisBillhead.getBillId().split(CommonConstant.COMMA_SPLIT);
            //保存前先解锁票据
            this.iAccebillService.batchUnLock(abAccrecordsIds, user);
            //系统结算方式为50-应收票据时，锁定票据
            this.iAccebillService.batchLock(abAccrecordsIds, user);
            //保持做额度占用
            this.iAccebillService.batchPayLockMoneyAdd(nisBillService.listNisBillVoByPayId(nisBillhead.getId()));
        }
    }

    /**
     * 付款方式为‘代付’的付款执行自动发送
     *
     * @return
     */
    public List<NisBillheadVO> listByAutoSendPaymentJob() {
        //付款执行 先记账后付款参数时付款执行不显示数据
        if (NetbankConstant.ACCOUNTMODE_MANUAL.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISAUTOACCOUNT).getValue())) {
            //先记账后付款
            if (NetbankConstant.ACCOUNTORDER_ACCOUNTPAY.equals(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYACCOUNT).getValue())) {
                return new ArrayList<>();
            }
        }
        return this.nisBillheadDao.listByAutoSendPaymentJob();
    }

    /**
     * 判断银行账号是否授权
     *
     * @param corpIds
     * @param accs
     * @param curId
     */
    public List<BtBankAccDTO> getBankAccDTOList(List<String> corpIds, List<String> accs, String curId) {

        if (EmptyUtils.isEmptyList(corpIds)) {
            return null;
        }
        List<BtBankAccDTO> list = new ArrayList<BtBankAccDTO>();
        List<BtBankAccDTO> listOnline = accountGrantService.listByMultiVal(corpIds, AccountGrantConstant.ONLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);//在线
        List<BtBankAccDTO> listOffline = accountGrantService.listByMultiVal(corpIds, AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.TRANSFER_ACCOUNT);//离线
        if (!EmptyUtils.isEmptyList(listOnline)) {
            for (BtBankAccDTO bankAcc : listOnline) {
                for (String acc : accs) {
                    if (bankAcc.getBankAcc().equals(acc) && bankAcc.getCurId().equals(curId)) {
                        list.add(bankAcc);
                    }
                }
            }
        }
        if (!EmptyUtils.isEmptyList(listOffline)) {
            for (BtBankAccDTO bankAcc : listOffline) {
                for (String acc : accs) {
                    if (bankAcc.getBankAcc().equals(acc) && bankAcc.getCurId().equals(curId)) {
                        list.add(bankAcc);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 账号余额不足
     *
     * @param ids
     * @return
     */
    public String checkBal(String ids) {
        //银行账号
        Map<String, BigDecimal> bankAccMap = new HashMap<>();
        //内部账号
        Map<String, BigDecimal> innerAccMap = new HashMap<>();
        //获取所有的付款方式
        List<FcVoucherTypeDTO> fcVoucherTypeDTOList = fcVoucherTypeService.getFcVoucherTypeAll();
        Map<String, FcVoucherTypeDTO> fcVoucherTypeDTOMap = new HashMap<>();
        for (FcVoucherTypeDTO dto : fcVoucherTypeDTOList) {
            fcVoucherTypeDTOMap.put(dto.getId(), dto);
        }
        //付款余额控制方式  0-账户余额 1-可用余额 2-可用余额（不包含暂存）
        String balControlType = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BALCONTROLTYPE).getValue();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillheadVO nisBillheadVO = this.getVoById(id);
            //考虑其他单据来源过来的付款单，付款账号为空的情况
            if (StringUtils.isBlank(nisBillheadVO.getPayerAcc())) {
                continue;
            }
            //获取付款单的付款类型
            FcVoucherTypeDTO fcVoucherTypeDTO = fcVoucherTypeDTOMap.get(nisBillheadVO.getVoucherTypeId());
            BigDecimal payMoney = nisBillheadVO.getPayMoney();
            if ("2".equals(balControlType) && NetbankConstant.STATUE_TEMP.toString().equals(nisBillheadVO.getStatus())) {
                //不包含暂存
                payMoney = BigDecimal.ZERO;
            }
            if (NetbankConstant.ISACCOUNT_0.equals(fcVoucherTypeDTO.getIsAccount())) {
                if (bankAccMap.containsKey(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId())) {
                    bankAccMap.put(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId(), payMoney.add(bankAccMap.get(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId())));
                } else {
                    bankAccMap.put(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId(), payMoney);
                }
            } else {
                if (innerAccMap.containsKey(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId())) {
                    innerAccMap.put(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId(), payMoney.add(innerAccMap.get(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId())));
                } else {
                    innerAccMap.put(nisBillheadVO.getPayerAcc() + CommonConstant.COMMA_SPLIT + nisBillheadVO.getCurId(), payMoney);
                }
            }
        }
        String msgHead = "{table id=\"appTable\" class=\"layui-table layui-form breakRow  dark-layui-table layui-table-border\" style=\"width:100%;\"}";
        msgHead += "{thead}{tr}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}账号{/th}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}币种{/th}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}账户余额{/th}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}支付金额{/th}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}差额{/th}";
        msgHead += "{/tr}{/thead}";
        String msg = "";
        //银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        String bankOver = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BANKACCOVER).getValue();
        //内部账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
        String accountOver = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ACCOUNTOVER).getValue();
        //银行账号可用余额验证
        if (!CommonConstant.NO.equals(bankOver)) {
            for (String bankAcc : bankAccMap.keySet()) {
                BigDecimal bal = null;
                String[] bank = bankAcc.split(CommonConstant.COMMA_SPLIT);
                BigDecimal payMoney = bankAccMap.get(bankAcc);
                //获取银行账号可用余额
                if (CommonConstant.NO.equals(balControlType)) {
                    BisAccBalDTO bisAccBal = bisAccBalService.getBisAccBalBy(bank[0], bank[1], null, true);
                    if (ObjectUtil.isNotEmpty(bisAccBal)) {
                        bal = bisAccBal.getAvailBal();
                    }
                } else {
                    //可用余额需加上本次的合计支付金额
                    bal = this.getAccountBal(bank[0], bank[1], CommonConstant.NO).add(payMoney);
                }
                if (EmptyUtils.isEmpty(bal)) {
                    bal = BigDecimal.ZERO;
                }
                if (payMoney.compareTo(bal) > 0) {
                    if (StringUtils.isBlank(msg)) {
                        msg = msg + msgHead;
                    }
                    msg += "{tr}";
                    msg += "{td align=\"center\" style=\"padding:0 10px\"}" + bank[0] + "{/td}";
                    msg += "{td align=\"center\" style=\"padding:0 10px\"}" + btCurrencyService.getObjectById(bank[1]).getCurName() + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(bal) + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(payMoney) + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(bal.subtract(payMoney).abs()) + "{/td}";
                    msg += "{/tr}";
                }
            }
        }
        //内部账号可用余额验证
        if (!CommonConstant.NO.equals(accountOver)) {
            for (String inneracc : innerAccMap.keySet()) {
                BigDecimal bal = BigDecimal.ZERO;
                String[] bank = inneracc.split(CommonConstant.COMMA_SPLIT);
                BigDecimal payMoney = innerAccMap.get(inneracc);
                //获取银行账号可用余额
                if (CommonConstant.YES.equals(balControlType)) {
                    FcOnlineLedgerDTO fcAccBalanceDTO = fcOnlineLedgerService.getByBlnctrAcc(bank[0]);
                    if (!EmptyUtils.isEmpty(fcAccBalanceDTO)) {
                        bal = fcAccBalanceDTO.getCheckBalance();
                    }
                } else {
                    bal = this.getAccountBal(bank[0], bank[1], CommonConstant.YES).add(payMoney);
                }
                if (EmptyUtils.isEmpty(bal)) {
                    bal = BigDecimal.ZERO;
                }
                if (payMoney.compareTo(bal) > 0) {
                    if (StringUtils.isBlank(msg)) {
                        msg = msg + msgHead;
                    }
                    msg += "{tr}";
                    msg += "{td align=\"center\" style=\"padding:0 10px\"}" + bank[0] + "{/td}";
                    msg += "{td align=\"center\" style=\"padding:0 10px\"}" + btCurrencyService.getObjectById(bank[1]).getCurName() + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(bal) + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(payMoney) + "{/td}";
                    msg += "{td align=\"right\" style=\"padding:0 10px\"}" + NumberUtil.formatNumeric(bal.subtract(payMoney).abs()) + "{/td}";
                    msg += "{/tr}";
                }
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            msg += "{/table}";
        }
        return msg;
    }

    /**
     * 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票
     *
     * @param ids 付款单主键id
     * @return 字符串
     */
    public String checkReceiveBill(String ids) {
        List<NisBill> nisBillList = Lists.newArrayList();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //获取票据ID
            List<NisBill> list = nisBillDao.listByPayId(id);
            if (!CollectionUtils.isEmpty(list)) {
                nisBillList.addAll(list);
            }
        }

        String msgHead = "{table id=\"ReceiveBillTable\" class=\"layui-table layui-form breakRow  dark-layui-table layui-table-border\" style=\"width:100%;\"}";
        msgHead += "{thead}{tr}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}付款单号{/th}";
        msgHead += "{th style=\"text-align:center;font-weight:700;\"}票据号{/th}";
        msgHead += "{/tr}{/thead}";
        String msg = "";

        // 查询付款单
        List<NisBillheadVO> nisBillheadList = this.listByIdUnion(ids);
        if (CollectionUtils.isEmpty(nisBillheadList)) {
            return msg;
        }
        // 查询票据流水
        List<String> billIdList = nisBillList.stream().map(NisBill::getBillId).collect(Collectors.toList());
        String aids = String.join(CommonConstant.COMMA_SPLIT, billIdList);
        List<AbAccrecordsDTO> abAccrecordsList = iAccebillService.getAccrecordsById(aids);
        if (CollectionUtils.isEmpty(abAccrecordsList)) {
            return msg;
        }

        for (NisBill nisBill : nisBillList) {
            NisBillheadVO nis = nisBillheadList.stream().filter(item -> item.getId().equals(nisBill.getPayId())).findFirst().orElse(null);
            if (null == nis) continue;
            AbAccrecordsDTO abAccrecords = abAccrecordsList.stream().filter(item -> AccebillConstant.BRIEF_STATUS_010.equals(item.getBriefId()) && item.getId().equals(nisBill.getBillId())).findFirst().orElse(null);
            if (null == abAccrecords) continue;
            String origNo = abVoucherinfoViceService.getOrigNoByVid(abAccrecords.getVvid());
            if (StringUtils.isBlank(msg)) {
                msg = msg + msgHead;
            }
            msg += "{tr}";
            msg += "{td align=\"center\" style=\"padding:0 10px\"}" + nis.getBillNo() + "{/td}";
            msg += "{td align=\"center\" style=\"padding:0 10px\"}" + origNo + "{/td}";
            msg += "{/tr}";
        }
        if (StringUtils.isNotBlank(msg)) {
            msg += "{/table}";
        }
        return msg;
    }

    public List<NisBillheadVO> listByRefundOrgId(String refundOrgId) {
        List<NisBillhead> list = nisBillDao.listByRefundOrgId(refundOrgId);
        return BeanUtils.copyListProperties(list, NisBillheadVO.class);
    }

    public void setFbsBudgetDetailDTO(NisBillheadVO nisBillhead, FbsBudgetProjectDetailDTO dto) {
        dto.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        dto.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        dto.setCorpId(nisBillhead.getCorpId());
        dto.setProcessDate(nisBillhead.getWishPayday());
        dto.setBillNo(nisBillhead.getBillNo());
        dto.setCurId(nisBillhead.getCurId());
    }

    /**
     * 按业务单据快照校验并占用企业端银行限额。
     *
     * @param entity 付款单
     * @param user 当前用户
     * @param verifyOnly 是否优先核对已有占用，缺失或快照变化时补做校验占用
     * @return 内管处理结果；调用方必须检查失败码，不得直接继续付款
     */
    private PaymentLimitResultDto occupyEnterpriseLimit(NisBillhead entity, ICurrentUser user, boolean verifyOnly) {
        if (entity == null || StringUtils.isBlank(entity.getId())) {
            return PaymentLimitResultDto.success();
        }
        if (user == null || StringUtils.isBlank(user.getId())) {
            throw new BusinessException(EnterpriseLimitMessageEnum.LIMIT_CONTROL_NIS_OPERATOR_MISSING);
        }
        String tenantId = resolveEnterpriseLimitTenantId(user);
        if (!isExternalOnlineBankPayment(entity)) {
            // 非34-外付网银付款单不参与支付限额，不发起无效释放请求。
            return PaymentLimitResultDto.success();
        }
        if (isEnterpriseLimitNonOccupyingStatus(entity.getStatus())
                || NetbankConstant.ISPAYED_1.equals(entity.getIsPayed()) || entity.getPayMoney() == null
                || entity.getPayMoney().compareTo(BigDecimal.ZERO) <= 0) {
            if (isExpenseBill(entity) && isEnterpriseLimitNonOccupyingStatus(entity.getStatus())) {
                // 费用报销汇总付款单在企业审批/指令退回后保留原占用，批量重提时由 verifyOrOccupy 幂等核对。
                return PaymentLimitResultDto.success();
            }
            // 外付网银单据退出占用状态时，清理之前可能保留的限额。
            return enterpriseLimitControlService.release(tenantId, EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD,
                    entity.getId(), user.getId());
        }
        PaymentLimitContextDto context = buildEnterpriseLimitContext(entity, user);
        if (context == null) {
            // 付款账户不存在或不是在线账户时，释放此前按在线付款保留的限额占用。
            return enterpriseLimitControlService.release(tenantId, EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD,
                    entity.getId(), user.getId());
        }
        if (verifyOnly) {
            // 批量提交、自动付款先核对已有占用；按内管接口契约，无占用时补做校验和占用。
            return enterpriseLimitControlService.verifyOrOccupy(context);
        } else {
            // 普通保存/提交将最新付款快照交给内管，由内管执行支付限额校验及占用。
            return enterpriseLimitControlService.checkAndOccupy(context);
        }
    }

    /**
     * 识别付款单的非占用状态。费用报销退回单是特例：保留原占用，
     * 直到费用报销处理执行删除或打回。
     */
    private boolean isEnterpriseLimitNonOccupyingStatus(Integer status) {
        return NetbankConstant.STATUE_TEMP.equals(status)
                || NetbankConstant.STATUE_DEL.equals(status)
                || NetbankConstant.STATUE_BACK.equals(status);
    }

    /**
     * 判断付款单是否采用34-外付网银；支付限额仅适用于该系统结算方式。
     * 以付款方式主数据为准，避免新增、编辑等入口未及时回填瞬态 sysVoucherCode 导致误判。
     */
    private boolean isExternalOnlineBankPayment(NisBillhead entity) {
        if (entity == null || StringUtils.isBlank(entity.getVoucherTypeId())) {
            return false;
        }
        FcVoucherType voucherType = fcVoucherTypeService.get(entity.getVoucherTypeId(), true);
        return voucherType != null && NetbankConstant.PAY_TYPE_34.equals(voucherType.getSysVoucherCode());
    }

    /**
     * 仅对可能占用过支付限额的34-外付网银付款单执行释放。
     */
    private PaymentLimitResultDto releaseEnterpriseLimitIfApplicable(NisBillhead entity, ICurrentUser user) {
        if (entity == null || StringUtils.isBlank(entity.getId()) || !isExternalOnlineBankPayment(entity)) {
            return PaymentLimitResultDto.success();
        }
        return enterpriseLimitControlService.release(resolveEnterpriseLimitTenantId(user),
                EnterpriseLimitControlService.BIZ_TYPE_NIS_BILLHEAD, entity.getId(),
                user == null ? null : user.getId());
    }

    /**
     * 判断付款单是否由费用报销汇总生成。
     */
    private boolean isExpenseBill(NisBillhead entity) {
        return entity != null && NetbankConstant.BILLFROM_2.equals(entity.getBillFrom());
    }

}
