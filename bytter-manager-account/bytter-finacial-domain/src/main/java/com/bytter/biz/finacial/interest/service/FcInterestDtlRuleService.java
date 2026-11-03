package com.bytter.biz.finacial.interest.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.interest.dao.FcInterestDtlRuleDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlRule;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlRuleVo;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 利息流水规则业务类。
 *
 * @author zhaohc
 * @date 2024/4/29 17:39
 */
@Slf4j
@Service("fcInterestDtlRuleService")
public class FcInterestDtlRuleService extends BaseService<FcInterestDtlRule, String> {

    @Autowired
    private FcInterestDtlRuleDao fcInterestDtlRuleDao;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBankTypeService bankTypeService;
    @DubboReference(check = false)
    private INisBusTypeService nisBusTypeService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    @Autowired(required = false)
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;

    @Override
    public BaseDao<FcInterestDtlRule, String> getEntityDao() {
        return fcInterestDtlRuleDao;
    }

    /**
     * 定时任务：银行账号利息流水自动认领
     */
    @XxlJob("bankAccInterestDtlAutoClaimJob")
    public void bankAccInterestDtlAutoClaimJob() {
        // 任务参数，可为空，不为空则以输入参数为自动认领业务编号
        String jobParam = XxlJobHelper.getJobParam();
        XxlJobHelper.log("定时任务：银行账号利息流水自动认领，输入自动认领业务编号参数【{}】 开始》》》》》》》》》", jobParam);
        // 如果输入参数则默认以输入参数为业务编号，未输入则取默认业务编号
        String busType = ObjectUtil.isNotEmpty(jobParam) ? jobParam : NisBusTypeConstant.SYS_BUS_TYPE_BS004;
        XxlJobHelper.log("获取业务类型【{}】！", busType);
        NisBusTypeDTO busTypeDTO = nisBusTypeService.getByBusType(busType);
        if (ObjectUtil.isEmpty(busTypeDTO)) {
            XxlJobHelper.log("获取业务类型【{}】失败，请先添加相关业务类型！", busType);
            return;
        }
        // 获取当前设置的银行利息流水规则
        List<FcInterestDtlRule> fcInterestDtlRules = fcInterestDtlRuleDao.listBy("validSign", CommonConstant.VALID_SIGN_YES);
        XxlJobHelper.log("当前获取【{}】条银行利息流水规则记录。", fcInterestDtlRules.size());
        if (ObjectUtil.isNotEmpty(fcInterestDtlRules)) {
            // 多线程异步认领银行流水
            List<CompletableFuture<Integer>> futures = new ArrayList<>();
            for (FcInterestDtlRule dtlRule : fcInterestDtlRules) {
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() ->
                                bisAccDtlService.interestDtlAutoClaim(dtlRule.getBankTypeId(), dtlRule.getRemark(), dtlRule.getTransTime(), busTypeDTO.getId()),
                        asyncThreadPoolExecutor);
                futures.add(future);
            }
            // 等待所有银行数据处理完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            // 收集所有结果，此处只是为了演示有返回值的异步线程池用法，可以不用返回
            Integer total = 0;
            try {
                for (CompletableFuture<Integer> future : futures) {
                    total += future.get();
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("异步线程任务异常：{}", e.getMessage());
                throw new RuntimeException(e);
            }
            XxlJobHelper.log("当前任务共处理了【{}】条银行流水。", total);
        }
        XxlJobHelper.log("定时任务：银行账号利息流水自动认领 结束》》》》》》》》》");
    }

    @Transactional(readOnly = true)
    public DataTablesOutput<FcInterestDtlRuleVo> listDataTable(DataTablesInput input) {
        DataTablesOutput<FcInterestDtlRuleVo> dataTablesOutput = fcInterestDtlRuleDao.listInterestRule(input);
        Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
            Map<String, DictDTO> dictMap = dictService.listMap("fcInterestDtlRule.validSign");
            listData.forEach(item -> Optional.ofNullable(dictMap.get(item.getValidSign())).ifPresent(dict -> item.setValidSign(dict.getName())));
        });
        return dataTablesOutput;
    }

    @Transactional(readOnly = true)
    public FcInterestDtlRuleVo getInterestRuleById(String id) {
        FcInterestDtlRule fcInterestDtlRule = fcInterestDtlRuleDao.get(id, true);
        return BeanUtils.copyNewInstanceProperties(fcInterestDtlRule, FcInterestDtlRuleVo.class);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveOrModify(FcInterestDtlRuleVo interestDtlRuleVo) {
        FcInterestDtlRule fcInterestDtlRule = BeanUtils.copyNewInstanceProperties(interestDtlRuleVo, FcInterestDtlRule.class);
        // 限制一个银行只能添加一条规则
        if (ObjectUtil.isNotEmpty(interestDtlRuleVo.getBankTypeId())) {
            List<FcInterestDtlRule> listInterestRule = fcInterestDtlRuleDao.listBy("bankTypeId", interestDtlRuleVo.getBankTypeId());
            if (ObjectUtil.isNotEmpty(listInterestRule) && ObjectUtil.notEqual(listInterestRule.get(0).getId(), interestDtlRuleVo.getId())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTLRULE_BANKTYPE_UNIQUE);
            }
        }
        // 填充银行名称
        Optional.ofNullable(fcInterestDtlRule.getBankTypeId()).flatMap(bankTypeId ->
                        Optional.ofNullable(bankTypeService.getObjectById(bankTypeId)))
                .ifPresent(bank -> fcInterestDtlRule.setBankName(bank.getTypeName()));
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        if (ObjectUtil.isEmpty(fcInterestDtlRule.getId())) {
            if (Objects.nonNull(currentUser)) {
                fcInterestDtlRule.setCreateBy(currentUser.getUsername());
            }
            fcInterestDtlRule.setCreateDate(DateUtil.date());
            saveEntity(fcInterestDtlRule);
        } else {
            if (Objects.nonNull(currentUser)) {
                fcInterestDtlRule.setUpdateBy(currentUser.getUsername());
            }
            fcInterestDtlRule.setUpdateDate(DateUtil.date());
            merge(fcInterestDtlRule);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteInterestDtlRule(FcInterestDtlRuleVo interestDtlRuleVo) {
        FcInterestDtlRule fcInterestDtlRule = fcInterestDtlRuleDao.get(interestDtlRuleVo.getId(), true);
        deleteEntity(fcInterestDtlRule);
    }

}
