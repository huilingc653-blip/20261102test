package com.bytter.biz.netbank.service;

import com.bytter.biz.netbank.dao.BisPayPolicyPayeeDao;
import com.bytter.biz.netbank.entity.BisPayPolicyPayee;
import com.bytter.biz.netbank.vo.BisPayPolicyPayeeVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.ExternalCorpDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 自动支付策略表
 * @date 2020-04-17 14:38:52
 */
@Service("bisPayPolicyPayeeService")
@Transactional
public class BisPayPolicyPayeeService extends BaseService<BisPayPolicyPayee, String> {
    @Autowired
    private BisPayPolicyPayeeDao bisPayPolicyPayeeDao;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    @Override
    public BaseDao<BisPayPolicyPayee, String> getEntityDao() {
        return bisPayPolicyPayeeDao;
    }

    /**
     * 根据策略ID获取支付收款方关联信息
     *
     * @param policyId 策略id
     * @return 支付收款人关联信息
     */
    public String getPayeeIds(String policyId) {
        List<BisPayPolicyPayee> resultList = bisPayPolicyPayeeDao.listByPolicyId(policyId);
        // 收款人不为空，且为支付收款人（兼容历史数据，支付收款人标志is_payee可能为空，不为空则等于1）
        List<String> payeeIdList = resultList
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getPayeeId()) && (StringUtils.isEmpty(item.getIsPayee()) || CommonConstant.YES.equals(item.getIsPayee())))
                .collect(Collectors.toList())
                .stream()
                .map(BisPayPolicyPayee::getPayeeId).collect(Collectors.toList());
        // 拼接支付收款人id，用“,”隔开
        StringBuilder payees = new StringBuilder();
        payeeIdList.forEach(payeeId -> payees.append(payeeId).append(CommonConstant.COMMA_SPLIT));
        return StringUtils.isNotEmpty(payees) ? payees.substring(0, payees.length() - 1) : "";
    }

    /**
     * 根据策略ID获取不支付收款方关联信息
     *
     * @param policyId 策略id
     * @return 不支付收款人关联信息
     */
    public List<BisPayPolicyPayee> getNoPayeeList(String policyId) {
        List<BisPayPolicyPayee> resultList = bisPayPolicyPayeeDao.listByPolicyId(policyId);
        // 收款账号或收款户名不为空，且为不支付收款人（不支付收款人标志is_payee等于0）
        return resultList
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getIsPayee()) && CommonConstant.NO.equals(item.getIsPayee()) && (StringUtils.isNotEmpty(item.getPayeeAcc()) || StringUtils.isNotEmpty(item.getPayeeName())))
                .collect(Collectors.toList());
    }

    /**
     * 根据策略ID获取收款关联信息
     *
     * @param policyId 策略id
     * @return 收款关联信息
     */
    public BisPayPolicyPayeeVO getByPolicyId(String policyId) {
        // 据策略ID获取业务类别关联信息
        List<BisPayPolicyPayeeVO> resultList = bisPayPolicyPayeeDao.listByPolicyIdJoin(policyId);
        StringBuilder payeeIds = new StringBuilder();
        StringBuilder payees = new StringBuilder();

        StringBuilder noPayeeAccs = new StringBuilder();
        StringBuilder noPayeeNames = new StringBuilder();

        // 过滤支付收款人数据
        List<String> payeeIdList = resultList
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getPayeeId()) && (StringUtils.isEmpty(item.getIsPayee()) || CommonConstant.YES.equals(item.getIsPayee())))
                .collect(Collectors.toList())
                .stream()
                .map(BisPayPolicyPayeeVO::getPayeeId).collect(Collectors.toList());

        payeeIdList.forEach(payeeId -> {
            ExternalCorpDTO externalCorpDTO = externalCorpService.getExternalCorpVoById(payeeId);
            // 支付收款人
            payeeIds.append(payeeId).append(CommonConstant.COMMA_SPLIT);
            if (null != externalCorpDTO) {
                payees.append(externalCorpDTO.getName()).append(CommonConstant.COMMA_SPLIT);
            }
        });

        // 过滤不支付收款人数据
        List<BisPayPolicyPayeeVO> noPayeeList = resultList
                .stream()
                .filter(item -> StringUtils.isNotEmpty(item.getIsPayee()) && CommonConstant.NO.equals(item.getIsPayee()) && (StringUtils.isNotEmpty(item.getPayeeAcc()) || StringUtils.isNotEmpty(item.getPayeeName())))
                .collect(Collectors.toList());

        noPayeeList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getPayeeAcc())) {
                // 不支付收款人账号
                noPayeeAccs.append(item.getPayeeAcc()).append(CommonConstant.COMMA_SPLIT);
            }
            if (StringUtils.isNotEmpty(item.getPayeeName())) {
                // 不支付收款人户名
                noPayeeNames.append(item.getPayeeName()).append(CommonConstant.COMMA_SPLIT);
            }
        });

        BisPayPolicyPayeeVO payee = new BisPayPolicyPayeeVO();
        payee.setPayeeId(StringUtils.isNotEmpty(payeeIds) ? payeeIds.substring(0, payeeIds.length() - 1) : "");
        payee.setPayee(StringUtils.isNotEmpty(payees) ? payees.substring(0, payees.length() - 1) : "");

        payee.setPayeeAcc(StringUtils.isNotEmpty(noPayeeAccs) ? noPayeeAccs.substring(0, noPayeeAccs.length() - 1) : "");
        payee.setPayeeName(StringUtils.isNotEmpty(noPayeeNames) ? noPayeeNames.substring(0, noPayeeNames.length() - 1) : "");
        return payee;
    }

    /**
     * 新增或更新关联收款人
     *
     * @param policyId    策略id
     * @param payees      支付收款人
     * @param noPayeeList 不支付收款人集合
     */
    public void saveOrUpdate(String policyId, String payees, List<BisPayPolicyPayee> noPayeeList) {
        // 先删除
        List<BisPayPolicyPayee> list = bisPayPolicyPayeeDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyPayee::getId).collect(Collectors.toList());
            this.delete(idList);
        }
        // 定义关联收款人实体集合
        List<BisPayPolicyPayee> entityList = Lists.newArrayList();
        // 再新增
        if (StringUtils.isNotEmpty(payees)) {
            // 支付收款人
            List<String> payeeIdList = Arrays.asList(payees.split(CommonConstant.COMMA_SPLIT));
            payeeIdList.forEach(payeeId -> {
                BisPayPolicyPayee entity = new BisPayPolicyPayee();
                entity.setPolicyId(policyId);
                entity.setPayeeId(payeeId);
                entity.setIsPayee(CommonConstant.YES);
                entityList.add(entity);
            });
        }
        if (!CollectionUtils.isEmpty(noPayeeList)) {
            noPayeeList = noPayeeList.stream().filter(item -> StringUtils.isNotEmpty(item.getPayeeAcc()) || StringUtils.isNotEmpty(item.getPayeeName())).collect(Collectors.toList());
            // 不支付收款人
            noPayeeList.forEach(noPayee -> {
                BisPayPolicyPayee entity = new BisPayPolicyPayee();
                entity.setPolicyId(policyId);
                entity.setPayeeAcc(noPayee.getPayeeAcc());
                entity.setPayeeName(noPayee.getPayeeName());
                entity.setIsPayee(CommonConstant.NO);
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
        // 据策略ID获取业务收款方关联信息
        List<BisPayPolicyPayee> list = bisPayPolicyPayeeDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyPayee::getId).collect(Collectors.toList());
            this.delete(idList);
        }
    }
}
