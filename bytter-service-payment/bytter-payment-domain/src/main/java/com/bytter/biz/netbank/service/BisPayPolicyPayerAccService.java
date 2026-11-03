package com.bytter.biz.netbank.service;

import com.bytter.biz.netbank.dao.BisPayPolicyPayerAccDao;
import com.bytter.biz.netbank.entity.BisPayPolicyPayerAcc;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自动支付策略支付账号关联
 *
 * @author fangcf
 * @since 2025-05-19
 */
@Service("bisPayPolicyPayerAccService")
@Transactional
public class BisPayPolicyPayerAccService extends BaseService<BisPayPolicyPayerAcc, String> {

    @Autowired
    private BisPayPolicyPayerAccDao bisPayPolicyPayerAccDao;

    @Override
    public BaseDao<BisPayPolicyPayerAcc, String> getEntityDao() {
        return bisPayPolicyPayerAccDao;
    }

    /**
     * 根据策略ID获取支付账号关联信息
     *
     * @param policyId 策略id
     * @return 支付账号关联信息
     */
    public String getAccIds(String policyId) {
        List<BisPayPolicyPayerAcc> resultList = bisPayPolicyPayerAccDao.listByPolicyId(policyId);

        // 过滤支付账号为空的数据，支付账号非必填
        resultList = resultList.stream().filter(item -> StringUtils.isNotEmpty(item.getAccId())).collect(Collectors.toList());
        List<String> payerAccIdList = resultList
                .stream()
                .map(BisPayPolicyPayerAcc::getAccId).collect(Collectors.toList())
                .stream()
                // 去重
                .distinct().collect(Collectors.toList());

        // 拼接支付账号id，用“,”隔开
        StringBuilder payerAccIds = new StringBuilder();
        payerAccIdList.forEach(payerAccId -> payerAccIds.append(payerAccId).append(CommonConstant.COMMA_SPLIT));
        return StringUtils.isNotEmpty(payerAccIds) ? payerAccIds.substring(0, payerAccIds.length() - 1) : "";
    }

    /**
     * 新增或更新
     *
     * @param policyId    策略id
     * @param payerAccIds 支付账号
     */
    public void saveOrUpdate(String policyId, String payerAccIds) {
        // 先删除
        List<BisPayPolicyPayerAcc> list = bisPayPolicyPayerAccDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyPayerAcc::getId).collect(Collectors.toList());
            this.delete(idList);
        }
        // 再新增
        List<BisPayPolicyPayerAcc> entityList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(payerAccIds)) {
            List<String> payerAccIdList = Arrays.asList(payerAccIds.split(CommonConstant.COMMA_SPLIT));
            payerAccIdList.forEach(payerAccId -> {
                BisPayPolicyPayerAcc entity = new BisPayPolicyPayerAcc();
                entity.setPolicyId(policyId);
                entity.setAccId(payerAccId);
                entityList.add(entity);
            });
        }
        if (!CollectionUtils.isEmpty(entityList)) {
            this.batchSave(entityList);
        }
    }

    /**
     * 根据策略ID删除
     *
     * @param policyId 策略id
     */
    public void delByPolicyId(String policyId) {
        List<BisPayPolicyPayerAcc> list = bisPayPolicyPayerAccDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyPayerAcc::getId).collect(Collectors.toList());
            this.delete(idList);
        }
    }
}
