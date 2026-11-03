package com.bytter.biz.wage.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.service.BisGatherPolicyAccountService;
import com.bytter.biz.allocation.service.BisGatherPolicyAgreementService;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.netbank.dao.NisDemandDao;
import com.bytter.biz.netbank.entity.NisDemand;
import com.bytter.biz.netbank.service.NisDemandService;
import com.bytter.biz.netbank.vo.NisDemandVO;
import com.bytter.biz.payment.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.wage.dao.NisWageGenerateDemandDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigEntityDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.EquityMaintenanceDTO;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 工资自动生成需求清单
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/11/23
 */
@Service
public class NisWageGenerateDemandService {
    @Autowired
    private NisWageGenerateDemandDao nisWageGenerateDemandDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private NisDemandService nisDemandService;
    @DubboReference(check = false)
    private ICalendarService iCalendarService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private BisGatherPolicyAccountService policyAccountService;
    @DubboReference(check = false)
    private IConfigService iConfigService;
    @DubboReference(check = false)
    private IEquityMaintenanceService equityMaintenanceService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private BisGatherPolicyAgreementService policyAgreementService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;


    private static final String SPLIT = "_";

    /**
     * 获取工资配置表待生成需求清单数据
     * @param demandDate
     * @return TODO
     */
    public List<NisDemandVO> listSalaryConfigData(Date demandDate,List<String> accIds) {
        return nisWageGenerateDemandDao.listSalaryConfigData(demandDate,accIds);
    }

    /**
     * 获取工资表待生成需求清单数据
     * @param demandDate
     * @return TODO
     */
    public List<NisDemandVO> listSalaryTableData(Date demandDate,List<String> accIdList) {
        return nisWageGenerateDemandDao.listSalaryTableData(demandDate,accIdList);
    }

    /**
     * 工资类生成需求清单
     * @param dataList
     * @param demandType
     */
    public void generateDemand(List<NisDemandVO> dataList,String demandType) {
        // 获取所有corpId
        List<String> corpIds = dataList.stream().filter(Objects::nonNull).map(NisDemandVO::getCorpId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 根据corpIds一次性查出所有单位信息
        Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);
        // dataList根据单位ID和账号分组数据
        Map<String, List<NisDemandVO>> dataMap = dataList.stream().collect(Collectors.groupingBy(vo -> vo.getCorpId() + SPLIT + vo.getBankAcc()));
        for (Map.Entry<String, List<NisDemandVO>> entry : dataMap.entrySet()){
            String key = entry.getKey();
            List<NisDemandVO> value = entry.getValue();
            try {
                String configId = value.get(0).getPayBillId();
                // 如果是配置表，value中其实只有一条数据，如果是工资单据，这里可能是多条，所以需要汇总数据
                if (NisDemandConstant.DEMANDTYPE_05.equals(demandType)){
                    NisDemandVO demandVO = value.get(0);
                    BigDecimal sum = value.stream().map(NisDemandVO::getDemandMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
                    // 工资单据还需要校验汇总金额是否超过配置表已下拨金额，如果超过则需要生成已超过部分的需求清单
                    List<NisDemandVO> salaryConfigList = nisWageGenerateDemandDao.getSalaryConfigData(demandVO.getCorpId(),demandVO.getBankAcc(),demandVO.getDemandDate());
                    if (salaryConfigList != null && !salaryConfigList.isEmpty()){
                        NisDemandVO salaryConfigVO = salaryConfigList.get(0);
                        configId = salaryConfigVO.getPayBillId();
                        BigDecimal allDown = salaryConfigVO.getDemandMoney().add(salaryConfigVO.getDemandAllocationMoney());
                        sum = sum.subtract(allDown);
                    }

                    value.clear();
                    if (sum.compareTo(BigDecimal.ZERO) > 0){
                        demandVO.setDemandMoney(sum);
                        value.add(demandVO);
                    }else{
                        XxlJobHelper.log("代发工资单据金额重新计算后为:{}，此次不生成需求清单",sum);
                    }
                }

                // 如果 value 有数据，则表示需要生成需求清单
                if (value != null && !value.isEmpty()){
                    NisDemandVO demandVO = value.get(0);
                    NisDemand entity = new NisDemand();
                    BeanUtils.copyNotNullProperties(demandVO, entity);
                    entity.setDemandAllocationMoney(demandVO.getDemandMoney());
                    //状态是否为已确定
                    Boolean isCheck = false;
                    if (NisDemandConstant.DEMANDTYPE_04.equals(demandType)){
                        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_D));
                    }
                    if (NisDemandConstant.DEMANDTYPE_05.equals(demandType)){
                        entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_DEMAND_E));
                    }

                    //单位信息
                    CorpDTO corpDTO = corpMap.get(demandVO.getCorpId());
                    if(!EmptyUtils.isEmpty(corpDTO)){
                        String parentids = corpDTO.getParentIds();
                        String[] parentId = parentids.split(",");
                        entity.setCorpLevel(parentId.length);
                    }

                    //上级账号信息
                    if(EmptyUtils.isNotEmpty(demandVO.getSuperBankAccId())) {
                        BtBankAccDTO superBankAcc = btBankAccService.getBankAccById(demandVO.getSuperBankAccId());
                        if (!EmptyUtils.isEmpty(superBankAcc)) {
                            isCheck = true;
                            entity.setSuperBankAccId(superBankAcc.getId());
                            entity.setSuperBankAcc(superBankAcc.getBankAcc());
                            entity.setSuperBankName(superBankAcc.getBankName());
                            entity.setSuperAccName(superBankAcc.getAccName());
                            entity.setSuperCorpId(superBankAcc.getCorpId());
                        }
                    }

                    //状态
                    if(isCheck) {
                        entity.setStatus(NisDemandConstant.STATUE_CHECK);
                    }else{
                        entity.setStatus(NisDemandConstant.STATUE_TEMP);
                    }
                    entity.setIsConverge(CommonConstant.NO);
                    entity.setIsPos(CommonConstant.NO);
                    entity.setIsSummary(CommonConstant.NO);
                    entity.setCreateBy("admin");
                    entity.setCreateDate(DateUtils.getDate());

                    // 保存数据
                    nisDemandService.saveEntity( entity);

                    // 如果是配置表，则需要更新配置表 generate_require=1
                    if (NisDemandConstant.DEMANDTYPE_04.equals(demandType)){
                        nisWageGenerateDemandDao.updateConfigGenerateRequire(configId,null);
                    }
                    // 如果是工资单据 则更新配置表 total_release_money += 资金需求金额
                    if (NisDemandConstant.DEMANDTYPE_05.equals(demandType)){
                        nisWageGenerateDemandDao.updateConfigGenerateRequire(configId,entity.getDemandMoney());
                    }
                    XxlJobHelper.log("单位：{}，账号：{}生成需求清单成功",key.split(SPLIT)[0],key.split(SPLIT)[1]);
                }
            }catch (Exception e){
                XxlJobHelper.log("单位：{}，账号：{}生成需求清单异常，本次跳过",key.split(SPLIT)[0],key.split(SPLIT)[1]);
                XxlJobHelper.log(e);
            }
        }
    }


    public void demandGenerateDownExc(String demandType) {
        // 校验当前日期是否工作日，是工作日才执行
        Integer workDaySign = iCalendarService.getWorkDaySignByTime(new Date());
        if (ObjectUtil.isNotEmpty(workDaySign) && 1 == workDaySign) {
            // 付款方式：拨付
            FcVoucherTypeDTO voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_10);
            if (EmptyUtils.isEmpty(voucherType)){
                XxlJobHelper.log("当前系统结算方式：10-拨付,不存在，请先维护！");
                return;
            }
            // 待下拨需求清单
            List<NisDemandVO> payList = nisDemandService.listByAutoAllocation(demandType);
            if (EmptyUtils.isNotEmpty(payList)){
                XxlJobHelper.log("当前有：{}条需求单需要生成下拨指令。",payList.size());
                ICurrentUser user = userService.getByLoginName("admin");
                // 资金中心调拨账户配置
                List<BisGatherPolicyAccountVO> policyAccList = policyAccountService.getAllIsDown();

                // 获取所有需求单位id
                List<String> corpIds = payList.stream().map(NisDemandVO::getCorpId).distinct().collect(Collectors.toList());
                // 生成下拨指令
                List<ConfigEntityDTO> configList = iConfigService.listConfigEntityDTO("nis-isChaeOpenCorp", CommonConstant.YES,corpIds);
                if (EmptyUtils.isEmpty(configList)){
                    XxlJobHelper.log("当前所有需求单位都未启用自动下拨功能！");
                    return;
                }
                // 如果需求单位启用了自动下拨功能，那么configMap会有该单位的数据
                Map<String, String> configMap = configList.stream().collect(Collectors.toMap(ConfigEntityDTO::getName, ConfigEntityDTO::getValue));
                configList.clear();
                // 所有需求单位
                Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);

                // 一次性查出所有需求单位的营收账号
                Map<String, List<BtBankAccDTO>> corpAccMap = nisDemandService.listMapBankAcc1004(corpIds);

                // 获取所有父账号 superBankAccId
                List<String> superBankAccIds = payList.stream().map(NisDemandVO::getSuperBankAccId).distinct().collect(Collectors.toList());
                List<BtBankAccDTO> superBankAccList = btBankAccService.getBankAccByIdList(superBankAccIds);
                superBankAccIds.clear();
                // 根据账号ID组成Map，key为账号ID，value为账号对象
                Map<String, BtBankAccDTO> superBankAccMap = superBankAccList.stream().collect(Collectors.toMap(BtBankAccDTO::getId, v -> v));
                superBankAccList.clear();

                // 获取所有需求账号 bankAccId
                List<String> bankAccIds = payList.stream().map(NisDemandVO::getBankAccId).distinct().collect(Collectors.toList());
                List<BtBankAccDTO> bankAccList = btBankAccService.getBankAccByIdList(bankAccIds);
                bankAccIds.clear();
                // 根据账号ID组成Map，key为账号ID，value为账号对象
                Map<String, BtBankAccDTO> bankAccMap = bankAccList.stream().collect(Collectors.toMap(BtBankAccDTO::getId, v -> v));
                bankAccList.clear();

                // 是否记账
                String isAccount = iConfigService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue();
                // 判断是否自动记账
                String isAutoAccount = iConfigService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue();

                for (NisDemandVO demandVO : payList){
                    try {
                        // 需求日期改成当天
                        demandVO.setDemandDate(DateUtils.getDateYMD());

                        // 1) 判断需求单位是否启用自动下拨
                        if (!configMap.containsKey(demandVO.getCorpId()) || !corpMap.containsKey(demandVO.getCorpId())){
                            XxlJobHelper.log("需求单位：{}未启用自动下拨功能或无效。",demandVO.getCorpName());
                            // 修改需求单状态为下拨失败
                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,demandVO.getCorpName()+"未启用自动下拨功能或无效",user.getLoginName());
                            continue;
                        }

                        // 2）判断收付款账号是否存在
                        BtBankAccDTO payerAcc = superBankAccMap.get(demandVO.getSuperBankAccId());
                        if (ObjectUtil.isEmpty(payerAcc)){
                            String msg = demandVO.getSuperBankAcc() == null ? demandVO.getSuperBankAccId() : demandVO.getSuperBankAcc();
                            XxlJobHelper.log("需求单：{}上级账号：{}无效。",demandVO.getBillNo(),msg);
                            // 修改需求单状态为下拨失败
                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,msg+"为无效账号",user.getLoginName());
                            continue;
                        }

                        // 收款账号
                        BtBankAccDTO payeeAcc = bankAccMap.get(demandVO.getBankAccId());
                        if (ObjectUtil.isEmpty(payeeAcc)){
                            String msg = demandVO.getBankAcc() == null ? demandVO.getBankAccId() : demandVO.getBankAcc();
                            XxlJobHelper.log("需求单：{}需求账号：{}无效。",demandVO.getBillNo(),msg);
                            // 修改需求单状态为下拨失败
                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,msg+"为无效账号",user.getLoginName());
                            continue;
                        }

                        // 3）计算下拨金额
                        BigDecimal allocationMoney = demandVO.getDemandMoney();

                        CorpDTO corpDTO = corpMap.get(demandVO.getCorpId());
                        EquityMaintenanceDTO equityMaintenanceDTO = equityMaintenanceService.queryById(corpDTO.getEquityMaintenanceId());
                        if (!"0001".equals(equityMaintenanceDTO.getEquityCode()) && !"0002".equals(equityMaintenanceDTO.getEquityCode())){
                            XxlJobHelper.log("需求单位：{}股权类型不是全资也不是控股，暂不处理。",demandVO.getCorpName());
                            // 修改需求单状态为下拨失败
                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,demandVO.getCorpName()+"股权类型不是全资也不是控股，暂不处理。",user.getLoginName());
                            continue;
                        }

                        // 需求账号余额
                        BisAccBalDTO bankAccBal = nisDemandService.getLastBisAccBalBy(payeeAcc.getBankAcc(),payeeAcc.getCurId());
                        if ("0002".equals(equityMaintenanceDTO.getEquityCode())){// 控股类，需要根据需求单位的营收账号重算下拨金额
                            // 实际下拨金额 = 汇总的需求下拨金额-（资金需求账号系统可用余额-资金需求账号留底金额）
                            // -（营收账号的当前系统可用余额-营收账号留底金额）
                            // +（营收账号数量*1000）

                            // 资金需求账号余额，留底金额；（资金需求账号系统可用余额-资金需求账号留底金额）
                            BigDecimal demandBal = bankAccBal.getAvailBal().subtract(bankAccBal.getLeaveMoney());
                            allocationMoney = allocationMoney.subtract(demandBal);
                            // 营收账号余额，留底金额；（营收账号系统可用余额-营收账号留底金额）
                            List<BtBankAccDTO> btBankAccDTOS = corpAccMap.get(demandVO.getCorpId());
                            if (ObjectUtil.isNotEmpty(btBankAccDTOS)){
                                BigDecimal sum1004 = BigDecimal.ZERO;
                                for (BtBankAccDTO btBankAccDTO : btBankAccDTOS){
                                    BigDecimal subtract = btBankAccDTO.getAvailBal().subtract(btBankAccDTO.getLeaveMoney());
                                    sum1004 = sum1004.add(subtract);
                                }
                                allocationMoney = allocationMoney.subtract(sum1004);

                                // 手续费：营收账号数量*1000
                                BigDecimal handFee = new BigDecimal("1000").multiply(new BigDecimal(btBankAccDTOS.size()));
                                allocationMoney = allocationMoney.add(handFee);

                                // 付款类存在营收账号同时触发头寸调拨
                                nisDemandService.generatePos(demandVO,user);
                                XxlJobHelper.log("需求单：{}为付款类触发营收账号的头寸调拨",demandVO.getBillNo());
                            }
                        }
                        XxlJobHelper.log("需求单：{}实际下拨金额为：{}",demandVO.getBillNo(),allocationMoney);

                        // 如果实际下拨金额小于等于0，则不需要下拨。直接更新成功
                        if (allocationMoney.compareTo(BigDecimal.ZERO) <=0 ){
                            // 修改需求单状态为下拨成功
                            nisDemandService.updateNisDemandStatusSuccess(Arrays.asList(demandVO.getId()),user.getLoginName());
                            continue;
                        }

                        // 控股类判断需求账号本身余额是否满足下拨金额
                        BigDecimal newBalance = bankAccBal.getAvailBal().subtract(bankAccBal.getLeaveMoney());
                        if ("0002".equals(equityMaintenanceDTO.getEquityCode()) && newBalance.compareTo(demandVO.getDemandMoney()) >= 0){
                            // 满足
                            XxlJobHelper.log("需求单：{}需求账号余额：{}大于实际下拨金额：{}直接更新状态下拨成功(3)",demandVO.getBillNo(),bankAccBal.getAvailBal(),allocationMoney);
                            // 修改需求单状态为下拨成功
                            nisDemandService.updateNisDemandStatusSuccess(Arrays.asList(demandVO.getId()),user.getLoginName());
                            continue;
                        }

                        // 4）根据单位层级生成下拨指令
                        if (demandVO.getCorpLevel() <= 3){
                            // 最顶级的直接生成下拨指令
                            generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user,equityMaintenanceDTO);
                        }else {
                            // 大于3级的，判断是否存在上级单据
                            if (ObjectUtil.isNotEmpty(demandVO.getSuperId())){
                                // 存在上级的，看父级单据是否交易成功
                                NisDemand superDemand = nisDemandService.get(demandVO.getSuperId(),true);
                                if ( NisDemandConstant.STATUE_ALLOCATION.equals(superDemand.getStatus())){
                                    // 看上级单据是否进行了头寸调拨，如果进行了头寸调拨，则需要判断头寸调拨是否成功
                                    if (CommonConstant.YES.equals(superDemand.getIsPos())){
                                        if (NisDemandConstant.WAIT_RESULT.equals(superDemand.getPosStatus())){
                                            // 等待结果的，则跳过
                                            XxlJobHelper.log("需求单：{}的上级需求单：{}的营收账号未完成头寸调拨。",demandVO.getBillNo(),superDemand.getBillNo());
                                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_CHECK,"上级需求单："+superDemand.getBillNo()+"的营收账号头寸调拨未完成",user.getLoginName());
                                            continue;
                                        }else if(NisDemandConstant.SUCCESS.equals(superDemand.getPosStatus())){
                                            // 头寸调拨成功的，则生成下拨指令
                                            nisDemandService.generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user);
                                        }else{
                                            // 存在失败的。则更新此需求单为失败
                                            XxlJobHelper.log("需求单：{}的上级需求单：{}的营收账号头寸调拨存在失败指令。",demandVO.getBillNo(),superDemand.getBillNo());
                                            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,"上级需求单："+superDemand.getBillNo()+"的营收账号头寸调拨存在失败指令",user.getLoginName());
                                            continue;
                                        }
                                    }else{
                                        // 如果上级没有头寸调拨，则直接生成下拨指令
                                        generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user,equityMaintenanceDTO);
                                    }
                                }else if (NisDemandConstant.STATUE_ALLOCATION_FAIL.equals(superDemand.getStatus())){
                                    // 如果上级单据下拨失败，则直接更新此需求单为下拨失败
                                    XxlJobHelper.log("需求单：{}的上级需求单：{}下拨失败，直接更新此需求单为下拨失败。",demandVO.getBillNo(),superDemand.getBillNo());
                                    nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,"上级需求单："+superDemand.getBillNo()+"下拨失败",user.getLoginName());
                                    continue;
                                }else{
                                    // 没有明确结果的，等待上级下拨状态
                                    XxlJobHelper.log("需求单：{}的上级需求单：{}未下拨完成，等待上级单据下拨完成。",demandVO.getBillNo(),superDemand.getBillNo());
                                    nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_CHECK,"等待上级需求单："+superDemand.getBillNo()+"下拨完成",user.getLoginName());
                                    continue;
                                }
                            }else{
                                // 不存在上级的，直接生成下拨指令
                                generateDownExc(demandVO,allocationMoney,corpMap.get(demandVO.getCorpId()),payerAcc,payeeAcc,voucherType,policyAccList,isAccount,isAutoAccount,user,equityMaintenanceDTO);
                            }
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        // 修改需求单状态为下拨失败
                        nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,e.getLocalizedMessage(),user.getLoginName());
                        XxlJobHelper.log("需求单：{}生成下拨指令失败：{}",demandVO.getBillNo(), e.getMessage());
                        XxlJobHelper.log(e);
                    }
                }
            }else{
                XxlJobHelper.log("没有需要生成下拨指令的需求单。");
            }
        }else {
            XxlJobHelper.log("当前日期为非工作日不生成下拨指令");
        }
    }
    /***
     * 生成下拨指令
     * @param demandVO
     * @param allocationMoney
     * @param corpDTO
     * @param payerAcc
     * @param payeeAcc
     * @param voucherType
     * @param policyAccList
     * @param isAccount
     * @param isAutoAccount
     * @param user
     * @param equityMaintenanceDTO
     * @return TODO
     */
    @Transactional
    public void generateDownExc(NisDemandVO demandVO, BigDecimal allocationMoney, CorpDTO corpDTO,BtBankAccDTO payerAcc, BtBankAccDTO payeeAcc,FcVoucherTypeDTO voucherType, List<BisGatherPolicyAccountVO> policyAccList, String isAccount, String isAutoAccount, ICurrentUser user,EquityMaintenanceDTO equityMaintenanceDTO) {
        Boolean isGenerate = true;
        String errorMsg = "";
        // 5） 生成付款指令对象
        BisExcDTO bisExcDTO = nisDemandService.buildBisExc(demandVO, payerAcc, payeeAcc, voucherType);

        // 6）根据付款账号可用余额和策略配置的“是否允许下拨”分为下面2种情况：
        BisAccBalDTO payerAccBal = nisDemandService.getLastBisAccBalBy(payerAcc.getBankAcc(), payerAcc.getCurId());
        String finalPayerAcc = payerAcc.getBankAcc();
        BisGatherPolicyAccountVO policyAccountVO = policyAccList.stream().filter(policyAccount ->
                finalPayerAcc.equals(policyAccount.getBankAcc())).findFirst().orElse(null);

        // 6.1 当前资金中心账户“可用余额”小于需要下拨的金额时或者当前资金中心账户不允许下拨时：
        if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(allocationMoney) < 0 )
                || (ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.NO.equals(policyAccountVO.getIsDown()))){
            // 是否存在可用的资金中心调拨账户
            boolean hasPayAcc = false;
            BtBankAccDTO bankAccInfo = null;
            BisAccBalDTO tempAccBal = null;
            if (demandVO.getCorpLevel() == 3){
                for (BisGatherPolicyAccountVO policyAccount : policyAccList){
                    if (CommonConstant.NO.equals(policyAccount.getIsDown())){
                        continue;
                    }
                    bankAccInfo = btBankAccService.getBankAccInfo(policyAccount.getBankAcc());
                    bankAccInfo.setCurId(BtCurrencyConstant.RMBID);
                    tempAccBal = nisDemandService.getLastBisAccBalBy(bankAccInfo.getBankAcc(), bankAccInfo.getCurId());
                    if (ObjectUtil.isNotEmpty(tempAccBal) && tempAccBal.getAvailBal().compareTo(allocationMoney) >= 0){
                        hasPayAcc = true;
                        payerAcc = bankAccInfo;
                        payerAccBal = tempAccBal;
                        break;
                    }
                }
            }
            // 存在可用的调拨账号，则替换指令中的付款账号为该账号
            if (hasPayAcc){
                bisExcDTO.setPayerAcc(payerAcc.getBankAcc());
                bisExcDTO.setPayerAccName(payerAcc.getAccName());
                bisExcDTO.setPayerBank(payerAcc.getBankName());
                bisExcDTO.setPayerCity(payerAcc.getCity());
                bisExcDTO.setPayerProv(payerAcc.getProv());
                bisExcDTO.setCurId(payerAcc.getCurId());
                bisExcDTO.setBifCode(payerAcc.getBifCode());
                bisExcDTO.setCorpId(payerAcc.getCorpId());
                nisDemandService.setDifBankAndAreaSign(bisExcDTO,payerAcc,payeeAcc);
            }else{
                isGenerate = false;
                errorMsg = "上级账号的可用余额都小于需要下拨的金额";
                XxlJobHelper.log("需求单：{}中止，上级账号的可用余额都小于需要下拨的金额：{}", demandVO.getBillNo(),allocationMoney);
            }
        }

        // 6.2 当资金中心账户“可用余额”大于需要下拨的金额时 需要再根据需求单位层级判断是否允许下拨
        if ((ObjectUtil.isNotEmpty(payerAccBal) && payerAccBal.getAvailBal().compareTo(allocationMoney) >= 0 ) &&
                (( demandVO.getCorpLevel()<=3 && ObjectUtil.isNotEmpty(policyAccountVO) && CommonConstant.YES.equals(policyAccountVO.getIsDown()))
                        || demandVO.getCorpLevel()>3)){
            // 判断需求单位的股权属性
            BigDecimal demandMoney = allocationMoney;
            if ("0002".equals(equityMaintenanceDTO.getEquityCode()) ) {
                // 是否差额下拨
                boolean differentSign = false;
                // 不紧急消息
                boolean notUrgentFlag = false;
                // 内部资金归集管理协议签订：是否签订，未签订不做下拨，已签订再根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                BisGatherPolicyAgreementVO policyAgreementVO = policyAgreementService.getByCorpId(payeeAcc.getCorpId());
                if (ObjectUtil.isNotEmpty(policyAgreementVO) && CommonConstant.YES.equals(policyAgreementVO.getIsAgreement())) {
                    // 查询收款账号对应的内部账号 counterAccId
                    FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(payeeAcc.getCounterAccId());
                    if (ObjectUtil.isNotEmpty(fcOnlineLedgerDTO)) {
                        // 下拨金额：allocatedMoney
                        // 根据内部账号配置是否允许透支、透支额度及内部账户余额判断，当满足条件时进行下拨
                        BigDecimal checkBalance = fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney());
                        if (InnerAccountConstant.OVERDRAFT_SIGN_N.equals(fcOnlineLedgerDTO.getOverdraftSign())) {
                            // 不允许透支
                            // 1）内部账号余额大于等于下拨余额时，自动下拨

                            // 2）内部账号余额小下拨余额时，将下拨金额调整成当前内部账号余额，
                            // 如：下拨金额为20000，内部账号余额为15000，则下拨指令的金额调整成15000进行下拨；
                            // （增加企业微信余额不足提醒，提醒人员新增界面进行配置）
                            if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0) {
                                allocationMoney = checkBalance;
                                differentSign = true;
                            }
                            // 如果内部账号余额小于等于0，则不下拨
                            if (checkBalance.compareTo(BigDecimal.ZERO) <= 0) {
                                isGenerate = false;
                                notUrgentFlag = true;
                                errorMsg = "需求账号对应的内部账户可用余额不足";
                                XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(), payeeAcc.getBankAcc(),fcOnlineLedgerDTO.getAccount(), checkBalance);
                            }
                        } else if (InnerAccountConstant.OVERDRAFT_SIGN_R.equals(fcOnlineLedgerDTO.getOverdraftSign())) {
                            // 受限透支 加上透支金额
                            checkBalance = checkBalance.add(fcOnlineLedgerDTO.getOverdraftMoney());
                            // 1） 内部账号余额大于等于下拨余额时，自动下拨

                            // 2）内部账号余额小于下拨余额时，将下拨金额调整成当前内部账号余额，
                            if (checkBalance.compareTo(allocationMoney) < 0 && checkBalance.compareTo(BigDecimal.ZERO) > 0) {
                                allocationMoney = checkBalance;
                                differentSign = true;
                            }
                            // 如果内部账号余额小于等于0，则不下拨
                            if (checkBalance.compareTo(BigDecimal.ZERO) <= 0) {
                                isGenerate = false;
                                notUrgentFlag = true;
                                errorMsg = "需求账号对应的内部账户可用余额不足";
                                XxlJobHelper.log("需求单：{}中止，账号：{}对应的内部账户：{}可用余额为：{}", demandVO.getBillNo(), payeeAcc.getBankAcc(), fcOnlineLedgerDTO.getAccount(),checkBalance);
                            }
                        }
                        // InnerAccountConstant.OVERDRAFT_SIGN_D
                        // 3) 不受限透支，直接下拨
                    } else {
                        isGenerate = false;
                        errorMsg = "需求账号未关联内部账号";
                        XxlJobHelper.log("需求单：{}中止，账号：{}未关联内部账号不满足下拨条件", demandVO.getBillNo(), payeeAcc.getBankAcc());
                    }
                } else {
                    isGenerate = false;
                    errorMsg = "需求单位未签订资金归集管理协议";
                    XxlJobHelper.log("需求单：{}中止，单位：{}未签订资金归集管理协议", demandVO.getBillNo(), corpDTO.getName());
                }

                //  差额下拨发送消息
                if (differentSign ){
                    // 发送紧急消息
                    Map<String, Object> map = new HashMap<>();
                    map.put("billNo", demandVO.getBillNo());
                    map.put("payer", bisExcDTO.getPayerAccName());
                    map.put("payerAcc", bisExcDTO.getPayerAcc());
                    map.put("payerBank", bisExcDTO.getPayerBank());
                    map.put("payee", bisExcDTO.getPayeeAccName());
                    map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                    map.put("payeeBank", bisExcDTO.getPayeeBank());
                    map.put("downAmt", allocationMoney);
                    map.put("amt", demandVO.getDemandMoney());
                    map.put("diffAmt", demandMoney.subtract(allocationMoney));
                    String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_FAILURE_DIFFERENCE;
                    notificationService.send(demandVO.getId(),demandVO.getCorpId(), tag, map);
                    XxlJobHelper.log("发送紧急消息：{}",map.toString());
                }

                // 触发不紧急消息提醒
                if(notUrgentFlag){
                    Map<String, Object> map = new HashMap<>();
                    map.put("reason","需求账号对应的内部账户可用余额不足");
                    map.put("billNo", demandVO.getBillNo());
                    map.put("payer", bisExcDTO.getPayerAccName());
                    map.put("payerAcc", bisExcDTO.getPayerAcc());
                    map.put("payerBank", bisExcDTO.getPayerBank());
                    map.put("payee", bisExcDTO.getPayeeAccName());
                    map.put("payeeAcc", bisExcDTO.getPayeeAcc());
                    map.put("payeeBank", bisExcDTO.getPayeeBank());
                    map.put("downAmt", 0);
                    map.put("amt", demandVO.getDemandMoney());
                    String tag = NotificationConstant.NETBANK.NETBANK_ALLOCATION_NOT_URGENT_FAILURE;
                    notificationService.send(demandVO.getId(),demandVO.getCorpId(), tag, map);
                    XxlJobHelper.log("发送不紧急消息：{}",map.toString());
                }
            }



        }else{
            XxlJobHelper.log("需求单：{}进入特殊情况下拨。", demandVO.getBillNo());
        }

        // 7） 当计算出来的下拨付款指令金额为0或者负数时，不生成下拨指令；
        if (allocationMoney.compareTo(BigDecimal.ZERO) <= 0){
            isGenerate = false;
            errorMsg = "实际下拨小于等于0";
            XxlJobHelper.log("需求单：{}中止，实际下拨金额：{}小于等于0", demandVO.getBillNo(), allocationMoney);
        }
        if(isGenerate) {
            bisExcDTO.setPayMoney(allocationMoney);
            BisExcDTO result = bisExcService.addBisExcReturn(bisExcDTO, user);
            demandVO.setStatus(NisDemandConstant.STATUE_WAIT_ALLOCATION);
            demandVO.setUpdateBy(user.getLoginName());
            demandVO.setUpdateDate(DateUtils.getDate());
            if (!EmptyUtils.isEmpty(result)) {
                demandVO.setAllocationNo(result.getVoucherNo());
                demandVO.setAllocationId(result.getId());
                XxlJobHelper.log("需求单：{}生成下拨指令成功：{}", demandVO.getBillNo(),result.getVoucherNo());
            }
            //自动记账
            if (CommonConstant.YES.equals(isAccount) && CommonConstant.YES.equals(isAutoAccount)) {
                XxlJobHelper.log("需求单：{}开始记账。。。", demandVO.getBillNo());
                nisDemandService.submitVoucher(demandVO, bisExcDTO, payerAcc, payeeAcc, user);
            }
            //编辑需求单，控制原状态为0-暂存/1已确认
            Integer[] oldStatus = {NisDemandConstant.STATUE_TEMP, NisDemandConstant.STATUE_CHECK};
            nisDemandService.updateNisDemandStatus(demandVO, oldStatus);
        }else{
            nisDemandService.updateNisDemandStatus(demandVO.getId(),NisDemandConstant.STATUE_ALLOCATION_FAIL,errorMsg,user.getLoginName());
            XxlJobHelper.log("需求单：{}中止，条件不满足！", demandVO.getBillNo());
        }

    }
}
