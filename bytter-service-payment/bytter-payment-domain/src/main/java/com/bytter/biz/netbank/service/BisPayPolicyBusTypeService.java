package com.bytter.biz.netbank.service;

import com.bytter.biz.netbank.dao.BisPayPolicyBusTypeDao;
import com.bytter.biz.netbank.dao.NisShareBusTypeDao;
import com.bytter.biz.netbank.entity.BisPayPolicyBusType;
import com.bytter.biz.netbank.entity.NisShareBusType;
import com.bytter.biz.netbank.vo.BisPayPolicyBusTypeVO;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自动支付策略业务类别关联表
 *
 * @author huangx, fangcf
 * @since 2025-05-19
 */
@Service("bisPayPolicyBusTypeService")
@Transactional
public class BisPayPolicyBusTypeService extends BaseService<BisPayPolicyBusType, String> {

    @Autowired
    private BisPayPolicyBusTypeDao bisPayPolicyBusTypeDao;

    @Autowired
    private NisShareBusTypeDao nisShareBusTypeDao;

    @Override
    public BaseDao<BisPayPolicyBusType, String> getEntityDao() {
        return bisPayPolicyBusTypeDao;
    }

    /**
     * 根据策略ID获取业务类别关联信息
     *
     * @param policyId 策略id
     * @return 业务类型拼接字符串
     * @author fangcf
     */
    public String getBusTypeIds(String policyId) {
        List<BisPayPolicyBusType> resultList = bisPayPolicyBusTypeDao.listByPolicyId(policyId);
        // 过滤业务类型为空的数据，业务类型非必填
        resultList = resultList.stream().filter(item -> StringUtils.isNotEmpty(item.getBusTypeId())).collect(Collectors.toList());
        List<String> busTypeList = resultList
                .stream()
                .map(BisPayPolicyBusType::getBusTypeId).collect(Collectors.toList())
                .stream()
                // 去重
                .distinct().collect(Collectors.toList());

        // 拼接业务类型id，用“,”隔开
        StringBuilder busTypeIds = new StringBuilder();
        busTypeList.forEach(busType -> busTypeIds.append(busType).append(CommonConstant.COMMA_SPLIT));
        return StringUtils.isNotEmpty(busTypeIds) ? busTypeIds.substring(0, busTypeIds.length() - 1) : "";
    }

    /**
     * 根据策略id获取共享业务类型关联信息
     *
     * @param policyId 策略id
     * @return 共享业务类型拼接字符串
     * @author fangcf
     */
    public String getShareBusTypeIds(String policyId) {
        // 跟据策略ID获取业务类别信息集合
        List<BisPayPolicyBusType> resultList = bisPayPolicyBusTypeDao.listByPolicyId(policyId);
        // 过滤共享业务类型为空的数据，共享业务类型非必填
        resultList = resultList.stream().filter(item -> StringUtils.isNotEmpty(item.getShareBusTypeId())).collect(Collectors.toList());
        List<String> shareBusTypeList = resultList
                .stream()
                .map(BisPayPolicyBusType::getShareBusTypeId).collect(Collectors.toList())
                .stream()
                // 去重
                .distinct().collect(Collectors.toList());
        // 拼接共享业务类型id，用“,”隔开
        StringBuilder shareBusTypeIds = new StringBuilder();
        shareBusTypeList.forEach(shareBusType -> shareBusTypeIds.append(shareBusType).append(CommonConstant.COMMA_SPLIT));
        return StringUtils.isNotEmpty(shareBusTypeIds) ? shareBusTypeIds.substring(0, shareBusTypeIds.length() - 1) : "";
    }

    /**
     * 根据策略ID获取业务类别关联信息
     *
     * @param policyId 策略id
     * @return 业务类别与共享业务类型信息
     */
    public BisPayPolicyBusTypeVO getByPolicyId(String policyId) {
        // 据策略ID获取业务类别关联信息
        List<BisPayPolicyBusTypeVO> resultList = bisPayPolicyBusTypeDao.listBusTypeByPolicyId(policyId);
        StringBuilder busTypeIds = new StringBuilder();
        StringBuilder busTypeNames = new StringBuilder();
        StringBuilder shareBusTypeIds = new StringBuilder();
        StringBuilder shareBusTypeNames = new StringBuilder();

        // 过滤业务类型为空数据
        List<BisPayPolicyBusTypeVO> busTypeList = resultList.stream().filter(item -> StringUtils.isNotEmpty(item.getBusTypeId())).collect(Collectors.toList());

        // 去重
        busTypeList = new ArrayList<>(busTypeList
                .stream()
                .collect(Collectors.toMap(BisPayPolicyBusTypeVO::getBusTypeId, Function.identity(), (oldValue, newValue) -> oldValue))
                .values());

        busTypeList.forEach(item -> {
            busTypeIds.append(item.getBusTypeId()).append(CommonConstant.COMMA_SPLIT);
            busTypeNames.append(item.getBusTypeName()).append(CommonConstant.COMMA_SPLIT);
        });
        // 定义返回的对象
        BisPayPolicyBusTypeVO busType = new BisPayPolicyBusTypeVO();
        busType.setBusTypeId(StringUtils.isNotEmpty(busTypeIds) ? busTypeIds.substring(0, busTypeIds.length() - 1) : "");
        busType.setBusTypeName(StringUtils.isNotEmpty(busTypeNames) ? busTypeNames.substring(0, busTypeNames.length() - 1) : "");

        // 过滤共享业务类型为空数据
        List<BisPayPolicyBusTypeVO> shareBusTypeList = resultList.stream().filter(item -> StringUtils.isNotEmpty(item.getShareBusTypeId())).collect(Collectors.toList());

        // 去重
        shareBusTypeList = new ArrayList<>(shareBusTypeList
                .stream()
                .collect(Collectors.toMap(BisPayPolicyBusTypeVO::getShareBusTypeId, Function.identity(), (oldValue, newValue) -> oldValue))
                .values());

        shareBusTypeList.forEach(item -> {
            shareBusTypeIds.append(item.getShareBusTypeId()).append(CommonConstant.COMMA_SPLIT);
            shareBusTypeNames.append(item.getShareBusTypeName()).append(CommonConstant.COMMA_SPLIT);
        });
        busType.setShareBusTypeId(StringUtils.isNotEmpty(shareBusTypeIds) ? shareBusTypeIds.substring(0, shareBusTypeIds.length() - 1) : "");
        busType.setShareBusTypeName(StringUtils.isNotEmpty(shareBusTypeNames) ? shareBusTypeNames.substring(0, shareBusTypeNames.length() - 1) : "");
        return busType;
    }

    /**
     * 新增或更新关联业务类别
     *
     * @param policyId        策略id
     * @param busTypeIds      业务类型id
     */
    public void saveOrUpdate(String policyId, String busTypeIds) {
        // 先删除
        List<BisPayPolicyBusType> list = bisPayPolicyBusTypeDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyBusType::getId).collect(Collectors.toList());
            this.delete(idList);
        }
        // 再新增
        // 定义关联业务类别实体集合
        List<BisPayPolicyBusType> entityList = Lists.newArrayList();
        if (StringUtils.isNotEmpty(busTypeIds)) {
            List<String> busTypeIdList = Arrays.asList(busTypeIds.split(CommonConstant.COMMA_SPLIT));
            // 策略一对多业务类别（大类），业务类别（大类）一对多共享业务类型（小类）
            busTypeIdList.forEach(busTypeId -> {
                // 共享业务类型（小类）为空
                BisPayPolicyBusType entity = new BisPayPolicyBusType();
                entity.setPolicyId(policyId);
                entity.setBusTypeId(busTypeId);
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
        // 据策略ID获取业务类别关联信息
        List<BisPayPolicyBusType> list = bisPayPolicyBusTypeDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> idList = list.stream().map(BisPayPolicyBusType::getId).collect(Collectors.toList());
            this.delete(idList);
        }
    }
}
