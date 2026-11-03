package com.bytter.biz.inneraccount.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.dao.BisCorpPoolConfigDao;
import com.bytter.biz.inneraccount.dao.BisCorpPoolConfigRuleDao;
import com.bytter.biz.inneraccount.entity.BisCorpPoolConfigRule;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigRuleVO;
import com.bytter.biz.inneraccount.vo.BisCorpPoolConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service("bisCorpPoolConfigRuleService")
public class BisCorpPoolConfigRuleService extends BaseService<BisCorpPoolConfigRule, String> {

    @Autowired
    private BisCorpPoolConfigDao bisCorpPoolConfigDao;

    @Autowired
    private BisCorpPoolConfigRuleDao bisCorpPoolConfigRuleDao;

    @Autowired
    private BisCorpPoolConfigService bisCorpPoolConfigService;

    @Autowired(required = false)
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;

    @Autowired
    private BisCorpPoolDtlService bisCorpPoolDtlService;

    @Override
    public BaseDao<BisCorpPoolConfigRule, String> getEntityDao() {
        return bisCorpPoolConfigRuleDao;
    }

    /**
     * 定时任务：单位归集流水自动认领
     */
    @XxlJob("BisCorpPoolConfigRuleService")
    public void bankAccInterestDtlAutoClaimJob() {
        XxlJobHelper.log("定时任务：单位归集流水自动认领 开始》》》》》》》》》");
        //按业务类型进行区分 归集，下拨 获取配置数据
        List<BisCorpPoolConfigVO> corpPoolConfigList = bisCorpPoolConfigDao.getCorpPoolConfigList();
        if (EmptyUtils.isEmptyList(corpPoolConfigList)) {
            XxlJobHelper.log("定时任务：单位归集流水自动认领-暂未维护匹配规则 结束》》》》》》》》》");
            return;
        }
        //按类型来做规则汇总
        Map<String, List<String>> typeConfigRuleSqlMap = getConfigRuleMap(corpPoolConfigList);
        if (EmptyUtils.isEmpty(typeConfigRuleSqlMap)) {
            XxlJobHelper.log("定时任务：单位归集流水自动认领-暂未维护匹配规则 结束》》》》》》》》》");
            return;
        }

        for (String type : typeConfigRuleSqlMap.keySet()) {
            String typeName = StringUtils.equals(type, "0") ? "归集" : "下拨";
            List<String> typeRuleSqlList = typeConfigRuleSqlMap.get(type);
            XxlJobHelper.log("当前获取类型为"+typeName+"的【{}】条单位归集流水规则记录。", typeRuleSqlList.size());
            // 多线程异步单位归集流水认领
            List<CompletableFuture<Integer>> futures = new ArrayList<>();
            for (String ruleSql : typeRuleSqlList) {
                CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() ->
                                //按类型处理单位归集自动认领
                                bisCorpPoolDtlService.autoBatchClaimSave(typeName, ruleSql),
                        asyncThreadPoolExecutor);
                futures.add(future);
            }
            // 等待所有银行数据处理完成
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            Integer total = 0;
            try {
                for (CompletableFuture<Integer> future : futures) {
                    total += future.get();
                }
            } catch (InterruptedException | ExecutionException e) {
                log.error("类型为"+typeName+"的单位归集流水认领异步线程任务异常：{}", e.getMessage());
                throw new RuntimeException(e);
            }
            XxlJobHelper.log("当前任务共处理了类型为"+typeName+"的单位归集【{}】条待认领流水。", total);
        }
        XxlJobHelper.log("定时任务：单位归集流水自动认领 结束》》》》》》》》》");
    }

    /**
     * 按类型将规则进行汇总
     *
     * @param corpPoolConfigList
     * @return
     */
    public Map<String, List<String>> getConfigRuleMap(List<BisCorpPoolConfigVO> corpPoolConfigList) {
        if (EmptyUtils.isEmpty(corpPoolConfigList)) {
            return null;
        }
        Map<String, List<BisCorpPoolConfigVO>> typeConfigMap = corpPoolConfigList.stream().collect(Collectors.groupingBy(BisCorpPoolConfigVO::getBusType));
        //按类型进行规则
        Map<String, List<String>> typeSqlMap = new HashMap<>();
        for (String type : typeConfigMap.keySet()) {
            List<BisCorpPoolConfigVO> sinConfigList = typeConfigMap.get(type);
            List<String> ruleSqlList = new ArrayList<>();
            for (BisCorpPoolConfigVO configVO : sinConfigList) {
                List<BisCorpPoolConfigRuleVO> bisCorpPoolConfigRuleList = bisCorpPoolConfigService.getConfigRuleByConfigId(configVO.getId());
                if (EmptyUtils.isEmptyList(bisCorpPoolConfigRuleList)) {
                    continue;
                }
                String ruleSql = assembleRuleCondition(bisCorpPoolConfigRuleList);
                ruleSqlList.add(ruleSql);
            }
            if (EmptyUtils.isEmptyList(ruleSqlList)) {
                continue;
            }
            typeSqlMap.put(type, ruleSqlList);
        }
        if (typeSqlMap.size() < 1) {
            return null;
        }
        return typeSqlMap;
    }

    /**
     * @Description 规则条件组装
     * @author zhous
     * @date 2025/2/25 14:56
     */
    private String assembleRuleCondition(List<BisCorpPoolConfigRuleVO> rules) {
        StringBuilder ruleCondition = new StringBuilder( " and ( ");
        if(!CollectionUtils.isEmpty(rules)){
            for (BisCorpPoolConfigRuleVO rule : rules){
                if(ObjectUtil.isNotEmpty(rule.getMatchElements()) && ObjectUtil.isNotEmpty(rule.getMatchRelations())){
                    if (ObjectUtil.isNotEmpty(rule.getMatchRule()) && ObjectUtil.isNotEmpty(rule.getMatchRule().trim())){
                        if ("01".equals(rule.getMatchRule())){
                            ruleCondition.append(" or ");
                        }else if ("02".equals(rule.getMatchRule())){
                            ruleCondition.append(" and ");
                        }
                    }else{
                        if(!" and ( ".equals(ruleCondition.toString())){
                            ruleCondition.append(" and ");
                        }
                    }
                    if (ObjectUtil.isNotEmpty(rule.getLeftParenthesis()) && ObjectUtil.isNotEmpty(rule.getLeftParenthesis().trim())){
                        ruleCondition.append(" (");
                    }
                    String matchElements = rule.getMatchElements();
                    /**匹配关系 1-包含、2-不包含、3-等于、4-不等于*/
                    String matchRelations = rule.getMatchRelations().trim();
                    if ("1".equals(matchRelations)){
                        matchRelations = " like '%tagContent%' ";
                        if (StringUtils.equals(matchElements, "amt")) {
                            setOddAmtCommonJoin(rule, matchElements, ruleCondition);
                        } else {
                            setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                        }
                    }else if ("2".equals(matchRelations)){
                        matchRelations = " not like '%tagContent%' ";
                        if (StringUtils.equals(matchElements, "amt")) {
                            setOddAmtCommonJoin(rule, matchElements, ruleCondition);
                        } else {
                            setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                        }
                    }else if ("3".equals(matchRelations)){
                        matchRelations = " = 'tagContent' ";
                        if (StringUtils.equals(matchElements, "amt")) {
                            setOddAmtCommonJoin(rule, matchElements, ruleCondition);
                        } else {
                            setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                        }
                    }else if ("4".equals(matchRelations)){
                        matchRelations = " <> 'tagContent' ";
                        if (StringUtils.equals(matchElements, "amt")) {
                            setOddAmtCommonJoin(rule, matchElements, ruleCondition);
                        } else {
                            setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                        }
                        // 金额才会出现大于或小于情况
                    } else if ("5".equals(matchRelations)) {
                        matchRelations = " > tagContent ";
                        setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                    } else if ("6".equals(matchRelations)) {
                        matchRelations = " >= tagContent ";
                        setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                    } else if ("7".equals(matchRelations)) {
                        matchRelations = " < tagContent ";
                        setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                    } else if ("8".equals(matchRelations)) {
                        matchRelations = " <= tagContent ";
                        setCommonJoin(rule, matchRelations, matchElements, ruleCondition);
                        //在什么之内
                    } else if ("9".equals(matchRelations)) {
                        setSpecialJoin(rule, "in", matchElements, ruleCondition);
                        //不在什么之内
                    } else if ("10".equals(matchRelations)) {
                        setSpecialJoin(rule,"not in", matchElements, ruleCondition);
                    }

                }
            }
            ruleCondition.append(" ) ");
        }
        return ruleCondition.toString();
    }

    /**
     * 特殊条件拼接
     *
     * @param rule
     * @param matchElements
     * @param ruleCondition
     */
    public void setSpecialJoin(BisCorpPoolConfigRuleVO rule, String matchRelations, String matchElements, StringBuilder ruleCondition) {
        String matchContent = rule.getMatchContent();
        if (StringUtils.isNotBlank(matchElements)) {
//            ruleCondition.append(" t.");
//            ruleCondition.append(matchElements);
            ruleCondition.append(" ").append(setFieldConvert(matchElements));
            ruleCondition.append(" ").append(matchRelations).append(" (");
            String[] matchContents = matchContent.split(",");
            StringBuffer buffer = new StringBuffer();
            if (StringUtils.equals(matchElements, "amt")) {
                for (int i = 0; i < matchContents.length; i++) {
                    if (i == matchContents.length - 1) {
                        buffer.append(matchContents[i]);
                        continue;
                    }
                    buffer.append(matchContents[i]).append(",");
                }
            } else {
                for (int i = 0; i < matchContents.length; i++) {
                    if (i == matchContents.length - 1) {
                        buffer.append("'").append(matchContents[i]).append("'");
                        continue;
                    }
                    buffer.append("'").append(matchContents[i]).append("'").append(",");
                }
            }
            ruleCondition.append(buffer.toString()).append(")");
        }
        if (ObjectUtil.isNotEmpty(rule.getRightParenthesis()) && ObjectUtil.isNotEmpty(rule.getRightParenthesis().trim())) {
            ruleCondition.append(" ) ");
        }
    }

    /**
     * 金额奇偶条件拼接
     *
     * @param rule
     * @param matchElements
     * @param ruleCondition
     */
    public void setOddAmtCommonJoin(BisCorpPoolConfigRuleVO rule, String matchElements, StringBuilder ruleCondition) {
        String matchContent = rule.getMatchContent();
        String matchRelations = "";
        if (StringUtils.equals(matchContent, "奇数")) {
            matchRelations = "%2 > 1";
        } else if (StringUtils.equals(matchContent, "偶数")) {
            matchRelations = "%2 <= 1";
        } else {
            matchRelations = " is not null";
        }
        if (StringUtils.isNotBlank(matchElements)) {
//            ruleCondition.append(" t.");
//            ruleCondition.append(matchElements);
            ruleCondition.append(" ").append(setFieldConvert(matchElements));
            ruleCondition.append(matchRelations);
        }
        if (ObjectUtil.isNotEmpty(rule.getRightParenthesis()) && ObjectUtil.isNotEmpty(rule.getRightParenthesis().trim())) {
            ruleCondition.append(" ) ");
        }
    }

    /**
     * 通用条件拼接
     *
     * @param rule
     * @param matchRelations
     * @param matchElements
     * @param ruleCondition
     */
    public void setCommonJoin(BisCorpPoolConfigRuleVO rule, String matchRelations, String matchElements, StringBuilder ruleCondition) {
        String matchContent = rule.getMatchContent();
        matchRelations = matchRelations.replace("tagContent", matchContent);
        if (StringUtils.isNotBlank(matchElements)) {
//            ruleCondition.append(" t.");
//            ruleCondition.append(matchElements);
            ruleCondition.append(" ").append(setFieldConvert(matchElements));
            ruleCondition.append(" ");
            ruleCondition.append(matchRelations);
        }
        if (ObjectUtil.isNotEmpty(rule.getRightParenthesis()) && ObjectUtil.isNotEmpty(rule.getRightParenthesis().trim())) {
            ruleCondition.append(" ) ");
        }
    }

    /**
     * 参数设置
     *
     * @param matchElement
     * @return
     */
    public String setFieldConvert(String matchElement) {
        StringBuffer buffer = new StringBuffer();
        if (StringUtils.equals(matchElement, "bankAcc")) {
            buffer = buffer.append("t.").append("bank_acc");
        } else if (StringUtils.equals(matchElement, "oppAccName")) {
            buffer = buffer.append("t.").append("opp_acc_name");
        } else if (StringUtils.equals(matchElement, "oppAccNo")) {
            buffer = buffer.append("t.").append("opp_acc_no");
        } else if (StringUtils.equals(matchElement, "accName")) {
            buffer = buffer.append("t.").append("acc_name");
        } else if (StringUtils.equals(matchElement, "curId")) {
            buffer = buffer.append("cur.").append("cur_name");
        } else if (StringUtils.equals(matchElement, "corpId")) {
            buffer = buffer.append("c.").append("name");
        } else {
            buffer = buffer.append("t.").append(matchElement);
        }
        return buffer.toString();
    }
}
