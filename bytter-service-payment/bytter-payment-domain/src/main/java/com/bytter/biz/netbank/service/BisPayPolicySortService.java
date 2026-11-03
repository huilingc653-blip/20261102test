package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.BisPayPolicySortDao;
import com.bytter.biz.netbank.entity.BisPayPolicySort;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
/**
 * @Title: Service
 * @Description: 自动支付策略表
 * @author huangx
 * @date 2020-04-17 14:38:52
 * @version 1.0
 *
 */
@Service("bisPayPolicySortService")
@Transactional
public class BisPayPolicySortService extends BaseService<BisPayPolicySort, String> {
    @Autowired
    private BisPayPolicySortDao bisPayPolicySortDao;
    @Override
    public BaseDao<BisPayPolicySort, String> getEntityDao() {
        return bisPayPolicySortDao;
    }
    /**
     * 据策略ID获取业务优先策略关联信息
     * @param policyId
     * @return
     */
    public List<BisPayPolicySort> listByPolicyId(String policyId){
        return bisPayPolicySortDao.listByPolicyId(policyId);
    }
    /**
     * 新增或更新
     *
     * @param policyId
     * @param sorts
     * @param policyTypes
     * @return
     */
    public void saveOrUpdate(String policyId, List<String> sorts, List<String> policyTypes) {
        //先删除再新增
        List<BisPayPolicySort> list = bisPayPolicySortDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            for (BisPayPolicySort busType : list) {
                this.deleteEntity(busType);
            }
        }
        if(!EmptyUtils.isEmpty(policyTypes)) {
            for (int i = 0; i < policyTypes.size(); i++) {
                if (!CommonConstant.NO.equals(policyTypes.get(i))) {
                    BisPayPolicySort bisPayPolicySort = new BisPayPolicySort();
                    bisPayPolicySort.setPolicyId(policyId);
                    bisPayPolicySort.setPolicyType(policyTypes.get(i));
                    bisPayPolicySort.setSort(Integer.parseInt(sorts.get(i)));
                    this.saveOrUpdate(bisPayPolicySort);
                }
            }
        }
    }
    /**
     * 根据策略ID删除
     * @param policyId
     */
    public void delByPolicyId(String policyId){
        List<BisPayPolicySort> list = bisPayPolicySortDao.listByPolicyId(policyId);
        if (!EmptyUtils.isEmptyList(list)) {
            for (BisPayPolicySort busType : list) {
                this.deleteEntity(busType);
            }
        }
    }
}
