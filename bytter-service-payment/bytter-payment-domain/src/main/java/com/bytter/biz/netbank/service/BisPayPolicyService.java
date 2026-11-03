package com.bytter.biz.netbank.service;

import com.bytter.saas.management.exchange.api.dto.PaymentLimitResultDto;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.netbank.dao.BisPayPolicyDao;
import com.bytter.biz.netbank.entity.BisPayPolicy;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.google.common.collect.Maps;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@Service("bisPayPolicyService")
@Transactional
public class BisPayPolicyService extends BaseService<BisPayPolicy, String> implements ITaskService {
    @Autowired
    private BisPayPolicyDao bisPayPolicyDao;
    @Autowired
    private NisCorpBusService nisCorpBusService;
    @Autowired
    private BisPayPolicyBusTypeService bisPayPolicyBusTypeService;
    @Autowired
    private BisPayPolicyPayeeService bisPayPolicyPayeeService;
    @Autowired
    private BisPayPolicyPayerAccService bisPayPolicyPayerAccService;
    @Autowired
    private BisPayPolicySortService bisPayPolicySortService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private NisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private NisMessageService nisMessageService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    @DubboReference(check = false)
    private    IDictService dictService;

    @Override
    public BaseDao<BisPayPolicy, String> getEntityDao() {
        return bisPayPolicyDao;
    }

    /**
     * 查询自动支付策略
     *
     * @param input
     * @return
     */
    public DataTablesOutput<BisPayPolicyVO> getDataTablesUnion(DataTablesInput input, ICurrentUser user) {
        // 获取资源权限单位集合
        List<String> authCorpList = corpService.getUserCorpIds(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY, user.getId());
        if (CollectionUtils.isEmpty(authCorpList)) {
            return new DataTablesOutput<>();
        }
        DataTablesOutput<BisPayPolicyVO> datas = bisPayPolicyDao.getDataTablesUnion(input, authCorpList);
        List<BisPayPolicyVO> resultList = datas.getData();
        if (!CollectionUtils.isEmpty(resultList)) {
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(authCorpList);
            for (BisPayPolicyVO vo : resultList) {
                if (!EmptyUtils.isEmpty(corpMap) && corpMap.containsKey(vo.getCorpId())) {
                    vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
                } else {
                    vo.setCorpName("");
                }
                vo.setCurName(btCurrencyService.getObjectById(vo.getCurId()).getCurName());
                //设置收款人和业务类别
                BisPayPolicyPayeeVO payee = bisPayPolicyPayeeService.getByPolicyId(vo.getId());
                vo.setPayeeId(payee.getPayeeId());
                vo.setPayeeName(payee.getPayee());
              //  vo.setNoPayeeName(payee.getPayeeName());

                BisPayPolicyBusTypeVO busType = bisPayPolicyBusTypeService.getByPolicyId(vo.getId());
                vo.setBusTypeId(busType.getBusTypeId());
                vo.setBusTypeName(busType.getBusTypeName());
              //  vo.setShareBusTypeId(busType.getShareBusTypeId());
              //  vo.setShareBusTypeName(busType.getShareBusTypeName());
            }
        }
        return datas;
    }

    /**
     * 根据ID查询自动支付策略
     *
     * @param id 策略id
     * @return 策略信息
     */
    public BisPayPolicyVO getByUnion(String id) {
        BisPayPolicyVO bisPayPolicy = this.bisPayPolicyDao.getByUnion(id);

        // 根据策略ID获取业务类别关联信息
        BisPayPolicyBusTypeVO busType = bisPayPolicyBusTypeService.getByPolicyId(id);
        // 业务类型（大类）
        bisPayPolicy.setBusTypeName(busType.getBusTypeName());
        bisPayPolicy.setBusTypeId(bisPayPolicyBusTypeService.getBusTypeIds(bisPayPolicy.getId()));
        // 共享业务类型（小类）
    /*    bisPayPolicy.setShareBusTypeName(busType.getShareBusTypeName());
        bisPayPolicy.setShareBusTypeId(bisPayPolicyBusTypeService.getShareBusTypeIds(bisPayPolicy.getId()));*/

        // 支付收款人
        bisPayPolicy.setPayeeId(bisPayPolicyPayeeService.getPayeeIds(bisPayPolicy.getId()));
        // 不支付收款人
       // bisPayPolicy.setNoPayeeList(bisPayPolicyPayeeService.getNoPayeeList(bisPayPolicy.getId()));

        // 支付账号
        bisPayPolicy.setPayerAccId(bisPayPolicyPayerAccService.getAccIds(bisPayPolicy.getId()));

        //单位
        bisPayPolicy.setCorpName(corpService.getCorpName(bisPayPolicy.getCorpId()));
        bisPayPolicy.setCurName(btCurrencyService.getObjectById(bisPayPolicy.getCurId()).getCurName());
        return bisPayPolicy;
    }

    /**
     * 重复校验
     *
     * @param propertyName  字段名
     * @param propertyValue 字段值
     * @param id            自身id
     * @return 对象实体
     */
    public BisPayPolicy validByPropertyName(String propertyName, String propertyValue, String id) {
        return bisPayPolicyDao.validByPropertyName(propertyName, propertyValue, id);
    }

    /**
     * 保存自动支付策略
     *
     * @param bisPayPolicyVo 表单数据
     */
    public void saveBisPayPolicy(BisPayPolicyVO bisPayPolicyVo, List<String> sort, List<String> policyType, ICurrentUser user) {
        String[] corpIdArr = bisPayPolicyVo.getCorpId().split(CommonConstant.COMMA_SPLIT);

        // 一个单位对应一个策略
        for (int i = 0; i < corpIdArr.length; i++) {
            BisPayPolicyVO bisPayPolicy = new BisPayPolicyVO();
            BeanUtils.copyProperties(bisPayPolicyVo, bisPayPolicy);
            bisPayPolicy.setCorpId(corpIdArr[i]);
            bisPayPolicy.setPolicyCode(bisPayPolicy.getPolicyCode());
            bisPayPolicy.setPolicyName(bisPayPolicy.getPolicyName());

            BisPayPolicy entity = new BisPayPolicy();
            BeanUtils.copyNotNullProperties(bisPayPolicy, entity);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            this.saveEntity(entity);
            // 保存业务类别，共享业务类型，当业务类型大类为空时，共享业务类型小类必为空
            if (!StringUtils.isBlank(bisPayPolicy.getBusTypeId())) {
                // 定义满足条件的业务类型
                StringBuilder bs = new StringBuilder();
                // 需校验该单位是否已维护需保存的业务类别
                List<NisCorpBusVO> corpBusList = nisCorpBusService.getByBusType(corpIdArr[i], CommonConstant.YES);
                if (!CollectionUtils.isEmpty(corpBusList)) {
                    // “单位业务种类设置”菜单中已维护的业务类型id集合
                    List<String> corpBusIdList = corpBusList.stream().map(NisCorpBusVO::getBusId).collect(Collectors.toList());
                    // 表单传入业务类型id集合
                    List<String> busTypeIdList = Arrays.asList(bisPayPolicy.getBusTypeId().split(CommonConstant.COMMA_SPLIT));
                    busTypeIdList.forEach(busTypeId -> {
                        if (corpBusIdList.contains(busTypeId)) {
                            // “单位业务种类设置”菜单中已维护，则保存
                            bs.append(busTypeId).append(CommonConstant.COMMA_SPLIT);
                        }
                    });
                }
                String busTypeIds = StringUtils.isNotEmpty(bs) ? bs.substring(0, bs.length() - 1) : "";
                bisPayPolicyBusTypeService.saveOrUpdate(entity.getId(), busTypeIds);
            }
            // 保存支付收款人和不支付收款人
            if (!StringUtils.isBlank(bisPayPolicy.getPayeeId()) ) {
                bisPayPolicyPayeeService.saveOrUpdate(entity.getId(), bisPayPolicy.getPayeeId(), null);
            }
            // 保存支付账号
            if (!StringUtils.isBlank(bisPayPolicy.getPayerAccId())) {
                bisPayPolicyPayerAccService.saveOrUpdate(entity.getId(), bisPayPolicy.getPayerAccId());
            }
            // 保存优先策略
            if (!EmptyUtils.isEmpty(policyType)) {
                bisPayPolicySortService.saveOrUpdate(entity.getId(), sort, policyType);
            }
            // 保存日志
            nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT, "", NisMessageConstant.BUS_TYPE_PAYPOLICY, user);
        }
    }

    /**
     * 更新自动支付策略
     *
     * @param bisPayPolicy 表单数据
     */
    public void updateBisPayPolicy(BisPayPolicyVO bisPayPolicy, List<String> sort, List<String> policyType, ICurrentUser user) {
        BisPayPolicy entity = bisPayPolicyDao.get(bisPayPolicy.getId(), true);
        BeanUtils.copyNotNullProperties(bisPayPolicy, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setCreateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
        // 保存业务类别，共享业务类型，当业务类型大类为空时，共享业务类型小类必为空
        bisPayPolicyBusTypeService.saveOrUpdate(entity.getId(), bisPayPolicy.getBusTypeId());
        // 保存支付收款人和不支付收款人
        bisPayPolicyPayeeService.saveOrUpdate(entity.getId(), bisPayPolicy.getPayeeId(), bisPayPolicy.getNoPayeeList());
        // 保存支付账号
        bisPayPolicyPayerAccService.saveOrUpdate(entity.getId(), bisPayPolicy.getPayerAccId());
        // 保存优先策略
        bisPayPolicySortService.saveOrUpdate(entity.getId(), sort, policyType);
        // 保存日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_SUBMIT, "", NisMessageConstant.BUS_TYPE_PAYPOLICY, user);
    }

    /**
     * 更新自动支付策略
     *
     * @param id 策略id
     */
    public void delBisPayPolicy(String id, ICurrentUser user) {
        BisPayPolicy entity = bisPayPolicyDao.get(id, true);
        this.deleteEntity(entity);
        //删除业务类别（大类）和共享业务类型（小类）
        bisPayPolicyBusTypeService.delByPolicyId(entity.getId());
        //删除支付收款人和不支付收款人
        bisPayPolicyPayeeService.delByPolicyId(entity.getId());
        //删除支付账号
        bisPayPolicyPayerAccService.delByPolicyId(entity.getId());
        //删除优先策略
        bisPayPolicySortService.delByPolicyId(entity.getId());
        //保存日志
        nisMessageService.saveMessage(entity.getId(), NisMessageConstant.MESSAGE_TYPE_DELETE, "", NisMessageConstant.BUS_TYPE_PAYPOLICY, user);
    }

    /**
     * 校验同收款人，同币种不可以有相同的策略
     *
     * @param bisPayPolicy 策略对象
     * @return 是否
     */
    public boolean validByPayee(BisPayPolicyVO bisPayPolicy) {
        boolean result = false;
        if (StringUtils.isNotBlank(bisPayPolicy.getPayeeId())) {
            String[] payee = bisPayPolicy.getPayeeId().split(CommonConstant.COMMA_SPLIT);
            for (String payeeId : payee) {
                BisPayPolicyVO policy = new BisPayPolicyVO();
                BeanUtils.copyNotNullProperties(bisPayPolicy, policy);
                policy.setPayeeId(payeeId);
                List<BisPayPolicyVO> policyList = this.bisPayPolicyDao.listByPolicy(policy);
                if (!EmptyUtils.isEmpty(policyList)) {
                    result = true;
                    break;
                }
            }
        } else {
            List<BisPayPolicyVO> policyList = this.bisPayPolicyDao.listByPolicy(bisPayPolicy);
            if (!EmptyUtils.isEmpty(policyList)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取启用的自动支付策略
     *
     * @return 自动支付策略集合
     */
    public List<BisPayPolicyVO> getValidBisPayPolicy() {
        List<BisPayPolicyVO> policyList = bisPayPolicyDao.getValidBisPayPolicy();
        for (BisPayPolicyVO bisPayPolicy : policyList) {
            // 业务类型（大类）
            bisPayPolicy.setBusTypeId(bisPayPolicyBusTypeService.getBusTypeIds(bisPayPolicy.getId()));
        /*    // 共享业务类型（小类）
            bisPayPolicy.setShareBusTypeId(bisPayPolicyBusTypeService.getShareBusTypeIds(bisPayPolicy.getId()));*/
            // 支付收款人
            bisPayPolicy.setPayeeName(bisPayPolicyPayeeService.getByPolicyId(bisPayPolicy.getId()).getPayee());
            // 不支付收款人
        /*    bisPayPolicy.setNoPayeeAcc(bisPayPolicyPayeeService.getByPolicyId(bisPayPolicy.getId()).getPayeeAcc());
            bisPayPolicy.setNoPayeeName(bisPayPolicyPayeeService.getByPolicyId(bisPayPolicy.getId()).getPayeeName());*/
            // 支付账号
            bisPayPolicy.setPayerAccId(bisPayPolicyPayerAccService.getAccIds(bisPayPolicy.getId()));
            bisPayPolicy.setPolicyTypeList(bisPayPolicySortService.listByPolicyId(bisPayPolicy.getId()));
        }
        return policyList;
    }

    /**
     * 自动支付
     */
    @Override
    @XxlJob("BisPayPolicyService")
    public void run(String arguments) {
        arguments = XxlJobHelper.getJobParam();
        XxlJobHelper.log("【自动支付】定时任务开始，开始时间：【{}】，接收参数：【{}】", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL), arguments);
        logger.info("【自动支付】定时任务开始，开始时间：【{}】，接收参数：【{}】", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL), arguments);

        if (NotificationConstant.NETBANK.BISPAYPOLICY_AUTOPAY.equals(arguments)) {

            // 定义所有满足条件的付款单
            LinkedList<NisBillheadVO> allBillHeadList = new LinkedList<>();

            // 获取启用的自动支付策略
            List<BisPayPolicyVO> policyList = this.getValidBisPayPolicy();
            for (BisPayPolicyVO policy : policyList) {
                // 校验是否需要跳过节假日
                String isWorkDay = policy.getIsWorkDay();
                if (CommonConstant.YES.equals(isWorkDay)) {
                    // 跳过节假日
                    Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
                    if (workDaySign != null && 1 != workDaySign) {
                        // 当天为节假日，则当前自动支付策略则跳过
                        continue;
                    }
                }
                // 当前支付金额
                BigDecimal payMoney = BigDecimal.ZERO;
                // 获取匹配策略的付款单
                List<NisBillheadVO> nisBillheadList = nisBillheadService.listByBisPayPolicy(policy);
                if (!EmptyUtils.isEmpty(nisBillheadList)) {
                    for (NisBillheadVO entity : nisBillheadList) {
                        if (this.dataCheck(entity)) {
                            // 当日支付限额
                            payMoney = payMoney.add(entity.getPayMoney());
                            if (null != policy.getPayTotal()) {
                                // 当日已支付金额
                                BigDecimal payMoneySum = nisBillheadService.getSumPayMoneyByPolicyId(policy.getId()).add(payMoney);
                                if (policy.getPayTotal().compareTo(payMoneySum) <= 0) {
                                    break;
                                }
                            }
                            entity.setPolicyId(policy.getId());
                            // 满足条件的付款单
                            allBillHeadList.addLast(entity);
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(allBillHeadList)) {
                XxlJobHelper.log("【自动支付】定时任务运行中，待处理：{} 条数据", allBillHeadList.size());
                logger.info("【自动支付】定时任务运行中，待处理：{} 条数据", allBillHeadList.size());
                // 分组获取所有的付款账号以及币别
                Map<String, List<NisBillheadVO>> map = allBillHeadList.stream().collect(Collectors.groupingBy(item -> item.getPayerAcc() + CommonConstant.COMMA_SPLIT + item.getCurId()));
                // 付款账号一对一账号余额
                Map<String, BigDecimal> payerAccToBalMap = Maps.newHashMap();
                map.forEach((k, v) -> {
                    String payerAcc = k.split(CommonConstant.COMMA_SPLIT)[0];
                    String curId = k.split(CommonConstant.COMMA_SPLIT)[1];
                    BigDecimal availBal = BigDecimal.ZERO;
                    // 获取对应账号对应币别的最新余额
                    BisAccBalDTO lastBal = bisAccBalService.getLastBisAccBalBy(payerAcc, curId);
                    if (!EmptyUtils.isEmpty(lastBal)) {
                        availBal = lastBal.getAvailBal();
                    }
                    payerAccToBalMap.put(k, availBal);
                });

                Integer sum = 0;
                for (NisBillheadVO entity : allBillHeadList) {
                    String key = entity.getPayerAcc() + CommonConstant.COMMA_SPLIT + entity.getCurId();
                    BigDecimal availBal = null != payerAccToBalMap.get(key) ? payerAccToBalMap.get(key) : BigDecimal.ZERO;
                    if (availBal.compareTo(entity.getPayMoney()) < 0) {
                        // 可用余额不足，则跳过
                        continue;
                    }
                    try {
                        // 自动付款由企业端按限额返回码处理，业务拒绝只记录原因并跳过当前单据。
                        PaymentLimitResultDto result = nisBillheadService.autoPayBill(entity);
                        if (result == null || !result.isSuccess()) {
                            XxlJobHelper.log("付款单:{}自动支付未通过，返回码：{}，原因：{}", entity.getId(),
                                    result == null ? "LIMIT_RESULT_MISSING" : result.getCode(),
                                    result == null ? "内管未返回限额处理结果" : result.getMessage());
                            logger.info("【自动支付】付款单:{}未通过，返回码：{}，原因：{}", entity.getId(),
                                    result == null ? "LIMIT_RESULT_MISSING" : result.getCode(),
                                    result == null ? "内管未返回限额处理结果" : result.getMessage());
                            continue;
                        }
                        // 每生成一个付款单，则可用余额减去付款金额
                        payerAccToBalMap.put(key, availBal.subtract(entity.getPayMoney()));
                        XxlJobHelper.log(" 当前付款单自动支付成功，付款单：{}", entity.getId());
                        sum += 1;
                    }catch (Exception e){
                        XxlJobHelper.log("付款单:{}自动支付异常，异常信息：{}", entity.getId(),e.getMessage());
                        XxlJobHelper.log(e);
                        logger.info("【自动支付】定时任务异常，异常信息：{}", e.getMessage());
                        logger.info("【自动支付】定时任务异常：", e);
                    }

                }

                XxlJobHelper.log("【自动支付】定时任务成功笔数：{}", sum);
            }else{
                XxlJobHelper.log("【自动支付】定时任务中止，没有待处理数据");
                logger.info("【自动支付】定时任务中止，没有待处理数据");
            }
        }

        XxlJobHelper.log("【自动支付】定时任务结束，结束时间：【{}】", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
        logger.info("【自动支付】定时任务结束，结束时间：【{}】", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL));
    }

    /**
     * 付款单校验
     *
     * @param nisBillhead 需校验的数据对象
     * @return 校验结果
     */
    private boolean dataCheck(NisBillheadVO nisBillhead) {
        //状态必须是暂存
        if (!NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus())) {
            return false;
        }
        //用途不为空
        if (StringUtils.isEmpty(nisBillhead.getPurpose())) {
            return false;
        }
        //收付款账号不能相同
        if (StringUtils.isEmpty(nisBillhead.getPayeeAcc())) {
            return false;
        }
        //收付款账号不能相同
        if (nisBillhead.getPayeeAcc().equals(nisBillhead.getPayerAcc())) {
            return false;
        }
        //跨行标志不能为空
        if (StringUtils.isBlank(nisBillhead.getDifBank())) {
            return false;
        }
        //地域标志不能为空
        if (StringUtils.isBlank(nisBillhead.getAreaSign())) {
            return false;
        }
        //省市
        if (StringUtils.isBlank(nisBillhead.getProv())) {
            return false;
        }
        if (StringUtils.isBlank(nisBillhead.getCity())) {
            return false;
        }
        //账户余额校验
        /*BigDecimal bal = bisAccBalService.getBalByBalDate(nisBillhead.getPayerAcc(), nisBillhead.getCurId(), nisBillhead.getWishPayday(), MatchTypeEnum.LE.getValue());
        if(bal==null){
            return false;
        }
        if(bal!=null && bal.compareTo(nisBillhead.getPayMoney())<0){
            return false;
        }*/
        //判断是否对私，是否存在明细
        if (NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())) {
            List<NisExpenseDetailVO> list = nisExpenseDetailService.listByBillId(nisBillhead.getId());
            if (EmptyUtils.isEmpty(list)) {
                return false;
            }
        }
        //校验资金计划
        String budgetSel = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
        if (CommonConstant.YES.equals(budgetSel) && CommonConstant.YES.equals(nisBillhead.getIsBudget())) {
            if (fbsCorpBudgetService.reportBudgetByCorpId(nisBillhead.getCorpId())) {
                List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());
                if (EmptyUtils.isEmpty(list)) {
                    return false;
                }
            }
        }
        return true;
    }
}
