package com.bytter.check.service;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.XxlJobConstant;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.dao.FcAutoCheckConfigDao;
import com.bytter.check.dao.FcCompareCenterDao;
import com.bytter.check.entity.FcAutoCheckConfig;
import com.bytter.check.entity.FcAutoCheckCorp;
import com.bytter.check.entity.FcAutoCheckResult;
import com.bytter.check.vo.FcAutoCheckConfigVO;
import com.bytter.check.vo.FcBankCheckSearchVo;
import com.bytter.check.vo.FcBankCheckVO;
import com.bytter.check.vo.FcBankErpBalInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.SysTaskDTO;
import com.bytter.system.api.vo.XxlJobInfo;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
@Service("fcAutoCheckConfigService")
@Transactional
public class FcAutoCheckConfigService extends BaseService<FcAutoCheckConfig, String> implements ITaskService {
    @Autowired
    private FcAutoCheckConfigDao fcAutoCheckConfigDao;
    @Autowired
    private FcCompareCenterService fcCompareCenterService;
    @Autowired
    private FcAutoCheckCorpService fcAutoCheckCorpService;
    @Autowired
    private FcAutoCheckResultService fcAutoCheckResultService;
    @Autowired
    private FcCompareErpService fcCompareErpService;
    @DubboReference(check = false)
	private IUserService userServiceImpl;
    @Autowired
    private FcCompareCenterDao fcCompareAccDao;
    @DubboReference(check = false)
	private ITaskSwitchService taskSwitchServiceImpl;
    @DubboReference(check = false)
    private IXxlJobTemplate xxlJobTemplate;
    @DubboReference(check = false)
    private IDictService dictService;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public BaseDao<FcAutoCheckConfig, String> getEntityDao() {
        return fcAutoCheckConfigDao;
    }
    /**
     * 自动对账策略信息
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput) {
        return fcAutoCheckConfigDao.getDataTable(dataTablesInput);
    }
    public void save(FcAutoCheckConfigVO fcAutoCheckConfigVO, ICurrentUser user) {
        if (fcAutoCheckConfigDao.getAutoCheckByCOde(fcAutoCheckConfigVO.getAutoCheckCode(), null).size() > 0) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CONFIG_CODE_ERROR);
        }
        FcAutoCheckConfig fcAutoCheckConfig = new FcAutoCheckConfig();
        BeanUtils.copyNotNullProperties(fcAutoCheckConfigVO, fcAutoCheckConfig);
        fcAutoCheckConfig.setCreateBy(user.getUsername());
        fcAutoCheckConfig.setCreateDate(new Date());
        this.saveEntity(fcAutoCheckConfig);
        // 同步到XXL-Job控制中心
        this.saveOrUpdateXxlJob(fcAutoCheckConfig, user);
        this.updateEntity(fcAutoCheckConfig);
    }
    public void modify(FcAutoCheckConfigVO fcAutoCheckConfigVO, ICurrentUser user, HttpServletRequest request) {
        if (fcAutoCheckConfigDao.getAutoCheckByCOde(fcAutoCheckConfigVO.getAutoCheckCode(), fcAutoCheckConfigVO.getId()).size() > 0) {
            throw new ParameterException(CheckMessageEnum.FIN_CHECK_CONFIG_CODE_ERROR);
        }
        FcAutoCheckConfig fcAutoCheckConfig = this.get(fcAutoCheckConfigVO.getId());
        //修改了对账类型，或者为无效则去掉所关联的单位
        if (!fcAutoCheckConfig.getCheckType().equals(fcAutoCheckConfigVO.getCheckType()) || fcAutoCheckConfigVO.getValidSign().equals("0")) {
            fcAutoCheckCorpService.deleteByCode(fcAutoCheckConfig.getAutoCheckCode());
        }
        //修改了策略代码则修改关联单位的策略代码
        if (!fcAutoCheckConfig.getAutoCheckCode().equals(fcAutoCheckConfigVO.getAutoCheckCode())) {
            fcAutoCheckCorpService.updateByCode(fcAutoCheckConfigVO.getAutoCheckCode(), fcAutoCheckConfig.getAutoCheckCode());
        }
        if (fcAutoCheckConfig.getCheckType().equals("0")) {//清空session对应数据缓存
            request.getSession().setAttribute("fcAutoCheckConfig", null);
        } else {
            request.getSession().setAttribute("erpAutoCheckConfig", null);
        }
        BeanUtils.copyNotNullProperties(fcAutoCheckConfigVO, fcAutoCheckConfig);
        fcAutoCheckConfig.setUpdateBy(user.getUsername());
        fcAutoCheckConfig.setUpdateDate(new Date());
        // 同步到XXL-Job控制中心
        this.saveOrUpdateXxlJob(fcAutoCheckConfig, user);
        this.updateEntity(fcAutoCheckConfig);
    }
    public void delete(String id, ICurrentUser user) {
        FcAutoCheckConfig fcAutoCheckConfig = this.get(id);
        fcAutoCheckConfig.setUpdateBy(user.getUsername());
        fcAutoCheckConfig.setUpdateDate(new Date());
        fcAutoCheckConfig.setStatus(FinacialConstant.DELETED);
        fcAutoCheckCorpService.deleteByCode(fcAutoCheckConfig.getAutoCheckCode());
        if(StringUtils.isNotEmpty(fcAutoCheckConfig.getTaskId())) {
            // 删除对应定时任务
            // taskSwitchServiceImpl.delTask(fcAutoCheckConfig.getTaskId());
            Integer taskId = Convert.toInt(fcAutoCheckConfig.getTaskId(), 0);
            logger.info("删除XXL-JOb定时任务：【{}】",taskId);
            xxlJobTemplate.removeJob(taskId);
        }
        this.updateEntity(fcAutoCheckConfig);
    }
    /**
     * 新增或者修改定时任务
     *
     * @param fcAutoCheckConfig 自动对账配置
     */
    public void saveOrUpdateTaskInfo(FcAutoCheckConfig fcAutoCheckConfig, ICurrentUser currentUser) {
        if (fcAutoCheckConfig != null) {
            SysTaskDTO dto = new SysTaskDTO();
            dto.setValidSign(CommonConstant.YES);
            dto.setCurrentUser(currentUser);
            dto.setId(fcAutoCheckConfig.getTaskId());
            dto.setCron("0 0 8 * * ?");
            dto.setActive(0);//默认停止的
//			资金预警-策略类型-策略代码策略名称
            dto.setTaskname("自动对账"
                    + "-" + fcAutoCheckConfig.getAutoCheckCode() + fcAutoCheckConfig.getAutoCheckName());
            dto.setCommand(FinacialConstant.FC_AUTOCHECK_TASKCOMMAND);
            dto.setArguments(fcAutoCheckConfig.getId());
            dto.setPeriod(1);
            if (StringUtils.isNotEmpty(fcAutoCheckConfig.getTaskId())) {
                dto.setEndDate(DateUtils.addYear(fcAutoCheckConfig.getLaunchTime(), 100));
                taskSwitchServiceImpl.updateTask(dto);
            } else {
                dto.setStartDate(fcAutoCheckConfig.getLaunchTime());
                dto.setEndDate(DateUtils.addYear(fcAutoCheckConfig.getLaunchTime(), 100));
                taskSwitchServiceImpl.addTask(dto);
            }
            fcAutoCheckConfig.setTaskId(dto.getId()); //添加任务之后，有新的id？
        }
    }

    /**
     * 新增或修改XXL定时任务信息。
     *
     * @param checkConfig 自动对账配置。
     * @param currentUser       当前登陆用户。
     */
    public void saveOrUpdateXxlJob(FcAutoCheckConfig checkConfig, ICurrentUser currentUser) {
        Map<String, DictDTO> dictMap = dictService.listMap("fcAutoCheckConfig.checkType");
        XxlJobInfo xxlJobInfo = null;
        Integer validSign = Convert.toInt(checkConfig.getValidSign(), 0);
        if (StringUtils.isBlank(checkConfig.getTaskId())) {
            xxlJobInfo = new XxlJobInfo();
            xxlJobInfo.setAuthor(currentUser.getUsername());
            xxlJobInfo.setScheduleConf("0 0 8 * * ?");
            xxlJobInfo.setScheduleType("CRON");
            xxlJobInfo.setExecutorHandler(XxlJobConstant.AUTO_CHECK_CONFIG);
            xxlJobInfo.setTriggerStatus(validSign);
            xxlJobInfo.setExecutorParam(checkConfig.getId());
            xxlJobInfo.setExecutorRouteStrategy("ROUND");
        } else {
            xxlJobInfo = xxlJobTemplate.getJobByExecutorHandler(appName, checkConfig.getTaskId());
            xxlJobInfo.setAuthor(currentUser.getUsername());
            xxlJobInfo.setScheduleConf("0 0 8 * * ?");
            xxlJobInfo.setScheduleType("CRON");
            xxlJobInfo.setExecutorHandler(XxlJobConstant.AUTO_CHECK_CONFIG);
            xxlJobInfo.setTriggerStatus(validSign);
        }
        // 自动对账-对账类型-策略代码策略名称
        xxlJobInfo.setJobDesc("自动对账-"
                + "-" + dictMap.get(checkConfig.getCheckType()).getName()
                + "-" + checkConfig.getAutoCheckCode() + checkConfig.getAutoCheckName());
        if (StringUtils.isBlank(checkConfig.getTaskId())) {
            // 添加XXL定时任务后会返回任务ID
            Integer id = xxlJobTemplate.addJob(xxlJobInfo, appName);
            checkConfig.setTaskId(String.valueOf(id));
        } else {
            xxlJobTemplate.updateJob(xxlJobInfo);
        }
    }

    @Override
    @XxlJob(XxlJobConstant.AUTO_CHECK_CONFIG)
    public void run(String arguments) {
        try {
            String jobParam = XxlJobHelper.getJobParam();
            logger.info("自动对账定时任务参数：【{}】", jobParam);
            FcAutoCheckConfig fcAutoCheckConfig = this.get(jobParam);
            List<String> corpIds = new ArrayList<>();
            List<FcAutoCheckCorp> checkCorps = fcAutoCheckCorpService.getByCode(fcAutoCheckConfig.getAutoCheckCode());
            for (FcAutoCheckCorp fcAutoCheckCorp : checkCorps) {
                corpIds.add(fcAutoCheckCorp.getCorpId());
            }
            ICurrentUser user = userServiceImpl.getScheduler();
            List<FcAutoCheckResult> fcAutoCheckResults = new ArrayList<>();
            FcAutoCheckConfigVO fcAutoCheckConfigVO = new FcAutoCheckConfigVO();
            BeanUtils.copyNotNullProperties(fcAutoCheckConfig, fcAutoCheckConfigVO);
            List<FcBankCheckVO> fcBankCheckVOS;
            if (fcAutoCheckConfig.getCheckType().equals("0")) {
                fcBankCheckVOS = fcAutoCheckConfigDao.getAutoFcBankCheckVO(corpIds);//中心对账数据
                //中心对账
                for (FcBankCheckVO fcBankCheckVO : fcBankCheckVOS) {
                    FcAutoCheckCorp fcAutoCheckCorp = fcAutoCheckCorpService.getCheckCorp("0", fcBankCheckVO.getCorpId());
                    if (!EmptyUtils.isEmpty(fcAutoCheckCorp)) {
                        if (fcAutoCheckConfigVO.getValidSign().equals("1")) {//策略生效日期大于当前日期 策略是有效的
                            FcBankCheckSearchVo fcBankCheckSearchVo = new FcBankCheckSearchVo();
                            Date checkDate = null;
                            if (!EmptyUtils.isEmpty(fcBankCheckVO.getEndDate()) && !"null".equals(fcBankCheckVO.getEndDate())) {
                                checkDate = fcBankCheckVO.getEndDate();
                            } else {
                                checkDate = fcBankCheckVO.getCheckDate();
                            }
                            // fcBankCheckSearchVo 属性赋值
                            BeanUtils.copyNotNullProperties(fcBankCheckVO, fcBankCheckSearchVo);
                            fcBankCheckSearchVo.setCheckType(fcAutoCheckCorp.getCheckTheStandard());//核对标准
                            fcBankCheckSearchVo.setParamType("0");//银企对账类型（中心对账）
                            Date endDate = this.getDateBycgeckDate(checkDate, fcAutoCheckConfigVO.getCheckCycle());
                            fcBankCheckSearchVo.setTransTime(endDate);//结束日期
                            //对账结果数据
                            FcAutoCheckResult fcAutoCheckResult = new FcAutoCheckResult();
                            List<BigDecimal> list = fcCompareAccDao.queryAccBalanceByDate(fcBankCheckVO.getAccount(), endDate);//查询结束日期是否有日结余额
                            if (!EmptyUtils.isEmptyList(list)) {
                                FcBankErpBalInfoVO balInfo = fcCompareCenterService.getAccBalInfo(fcBankCheckVO.getAccount(), fcBankCheckVO.getBankAcc(), DateUtils.formatDate(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE), "0", "1", fcBankCheckVO.getCurId());
                                fcBankCheckSearchVo.setBlnBalance(balInfo.getAccBal());
                                fcBankCheckSearchVo.setBankBalance(balInfo.getBankBal());
                                this.doCheckAndAdjust(fcBankCheckSearchVo, checkDate, user, fcAutoCheckResult);
                            } else {
                                fcAutoCheckResult.setIsSuccess(FinacialConstant.BOOLEAN_FALSE);
                            }
                            fcAutoCheckResult.setCorpId(fcBankCheckVO.getCorpId());
                            fcAutoCheckResult.setCorpName(fcBankCheckVO.getCorpName());
                            fcAutoCheckResult.setBankAcc(fcBankCheckVO.getBankAcc());
                            fcAutoCheckResult.setBankAccName(fcBankCheckVO.getAccName());
                            fcAutoCheckResult.setCheckType(fcAutoCheckConfigVO.getCheckType());
                            fcAutoCheckResult.setRunTime(new Date());
                            fcAutoCheckResult.setLaunchTime(fcAutoCheckConfigVO.getLaunchTime());
                            fcAutoCheckResult.setCheckCycle(fcAutoCheckConfigVO.getCheckCycle());
                            fcAutoCheckResult.setAutoCheckCode(fcAutoCheckConfigVO.getAutoCheckCode());
                            fcAutoCheckResult.setAutoCheckName(fcAutoCheckConfigVO.getAutoCheckName());
                            fcAutoCheckResult.setCreateBy(user.getUsername());
                            fcAutoCheckResult.setCreateDate(new Date());
                            fcAutoCheckResults.add(fcAutoCheckResult);
                        }
                    }
                }
            } else {
                fcBankCheckVOS = fcAutoCheckConfigDao.getErpFcBankCheckVO(corpIds);//ERP对账数据
                //ERP对账
                for (FcBankCheckVO fcBankCheckVO : fcBankCheckVOS) {
                    FcAutoCheckCorp fcAutoCheckCorp = fcAutoCheckCorpService.getCheckCorp("1", fcBankCheckVO.getCorpId());
                    if (!EmptyUtils.isEmpty(fcAutoCheckCorp)) {
                        if (fcAutoCheckConfigVO.getValidSign().equals("1")) {//策略生效日期大于当前日期 策略是有效的
                            FcBankCheckSearchVo fcBankCheckSearchVo = new FcBankCheckSearchVo();
                            Date checkDate = null;
                            if (!EmptyUtils.isEmpty(fcBankCheckVO.getEndDate()) && !"null".equals(fcBankCheckVO.getEndDate())) {
                                checkDate = fcBankCheckVO.getEndDate();
                            } else {
                                checkDate = fcBankCheckVO.getCheckDate();
                            }
                            // fcBankCheckSearchVo 属性赋值
                            BeanUtils.copyNotNullProperties(fcBankCheckVO, fcBankCheckSearchVo);
                            fcBankCheckSearchVo.setCheckType(fcAutoCheckCorp.getCheckTheStandard());//核对标准
                            fcBankCheckSearchVo.setParamType("1");//银企对账类型（中心对账）
                            Date endDate = this.getDateBycgeckDate(checkDate, fcAutoCheckConfigVO.getCheckCycle());
                            fcBankCheckSearchVo.setTransTime(endDate);//结束日期
                            FcBankErpBalInfoVO balInfo = fcCompareErpService.getAccBalInfo(fcBankCheckVO.getAccount(), fcBankCheckVO.getBankAcc(), fcBankCheckVO.getCurId(), DateUtils.formatDate(endDate, DateUtils.PATTERN_CLASSICAL_SIMPLE), "1", "0");
                            fcBankCheckSearchVo.setBlnBalance(balInfo.getAccBal());
                            fcBankCheckSearchVo.setBankBalance(balInfo.getBankBal());
                            //对账结果数据
                            FcAutoCheckResult fcAutoCheckResult = new FcAutoCheckResult();
                            fcAutoCheckResult.setCorpId(fcBankCheckVO.getCorpId());
                            fcAutoCheckResult.setCorpName(fcBankCheckVO.getCorpName());
                            fcAutoCheckResult.setBankAcc(fcBankCheckVO.getBankAcc());
                            fcAutoCheckResult.setBankAccName(fcBankCheckVO.getAccName());
                            fcAutoCheckResult.setCheckType(fcAutoCheckConfigVO.getCheckType());
                            fcAutoCheckResult.setRunTime(new Date());
                            fcAutoCheckResult.setLaunchTime(fcAutoCheckConfigVO.getLaunchTime());
                            fcAutoCheckResult.setCheckCycle(fcAutoCheckConfigVO.getCheckCycle());
                            fcAutoCheckResult.setAutoCheckCode(fcAutoCheckConfigVO.getAutoCheckCode());
                            fcAutoCheckResult.setAutoCheckName(fcAutoCheckConfigVO.getAutoCheckName());
                            fcAutoCheckResult.setCreateBy(user.getUsername());
                            fcAutoCheckResult.setCreateDate(new Date());
                            this.doErpCheckAndAdjust(fcBankCheckSearchVo, checkDate, user, fcAutoCheckResult);
                            fcAutoCheckResults.add(fcAutoCheckResult);
                        }
                    }
                }
            }
            fcAutoCheckResultService.batchSave(fcAutoCheckResults);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    /*
    计算由配置周期得到对账结束日期
     */
    public Date getDateBycgeckDate(Date checkDate, String checkCycle) {
        Date endDate = new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(checkDate);
        cale.add(Calendar.DATE, 1);//开始日期为上次结束后一天
        if (checkCycle.equals("30")) {
            cale.add(Calendar.MONTH, 1);
        } else if (checkCycle.equals("90")) {
            int month = cale.get(cale.MONTH) + 1;
            if (month >= 1 && month <= 3) {
                cale.set(Calendar.MONTH, 3);
            } else if (month >= 4 && month <= 6) {
                cale.set(Calendar.MONTH, 6);
            } else if (month >= 7 && month <= 9) {
                cale.set(Calendar.MONTH, 9);
            } else {
                cale.add(Calendar.YEAR, 1);
                cale.set(Calendar.MONTH, 0);
            }
        } else if (checkCycle.equals("180")) {
            int month = cale.get(cale.MONTH) + 1;
            if (month >= 1 && month <= 6) {
                cale.set(Calendar.MONTH, 6);
            } else {
                cale.add(Calendar.YEAR, 1);
                cale.set(Calendar.MONTH, 0);
            }
        }
        cale.set(Calendar.DAY_OF_MONTH, 0);//由开始日期得到对应周期的结束日期
        endDate = cale.getTime();
        return endDate;
    }
    /**
     * 进行对账与结转（中心对账）
     */
    public void doCheckAndAdjust(FcBankCheckSearchVo fca, Date checkDate, ICurrentUser user, FcAutoCheckResult fcAutoCheckResult) throws Exception {
        fcCompareCenterService.doCheck(fca, checkDate, user);
        Map<String, Object> result = new HashMap<String, Object>();
        result = fcCompareCenterService.loadAdjustBalance(fca.getBlnBalance(), fca.getBankBalance(), fca.getBankAcc(), fca.getAccount(), fca.getTransTime());
        Double blnAdjustBalanceD = (Double) result.get("blnAdjustBalance");
        Double bankAdjustBalanceD = (Double) result.get("bankAdjustBalance");
        BigDecimal blnAdjustBalance = BigDecimal.valueOf(blnAdjustBalanceD);
        BigDecimal bankAdjustBalance = BigDecimal.valueOf(bankAdjustBalanceD);
        if (blnAdjustBalance.compareTo(bankAdjustBalance) == 0) {
            fcCompareCenterService.doAdjustOverOther(fca, checkDate, user, "1");
            fcAutoCheckResult.setIsSuccess(FinacialConstant.BOOLEAN_TRUE);
        } else {//对账失败
            fcAutoCheckResult.setIsSuccess(FinacialConstant.BOOLEAN_FALSE);
        }
    }
    /**
     * 进行对账与结转（ERP对账）
     */
    public void doErpCheckAndAdjust(FcBankCheckSearchVo fca, Date checkDate, ICurrentUser user, FcAutoCheckResult fcAutoCheckResult) throws Exception {
        fcCompareErpService.doCheck(fca, checkDate, user);
        Map<String, Object> result = new HashMap<String, Object>();
        result = fcCompareErpService.loadAdjustErpBalance(fca.getBlnBalance(), fca.getBankBalance(), fca.getBankAcc(), fca.getAccount(), fca.getTransTime(), fca.getCurId());
        Double blnAdjustBalanceD = (Double) result.get("blnAdjustBalance");
        Double bankAdjustBalanceD = (Double) result.get("bankAdjustBalance");
        BigDecimal blnAdjustBalance = BigDecimal.valueOf(blnAdjustBalanceD);
        BigDecimal bankAdjustBalance = BigDecimal.valueOf(bankAdjustBalanceD);
        if (blnAdjustBalance.compareTo(bankAdjustBalance) == 0) {
            fcCompareErpService.doAdjustOverOther(fca, checkDate, user, "1");
            fcAutoCheckResult.setIsSuccess(FinacialConstant.BOOLEAN_TRUE);
        } else {//对账失败
            fcAutoCheckResult.setIsSuccess(FinacialConstant.BOOLEAN_FALSE);
        }
    }
}
