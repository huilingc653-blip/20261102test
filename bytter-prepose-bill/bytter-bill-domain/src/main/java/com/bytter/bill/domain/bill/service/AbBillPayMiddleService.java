package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.dao.AbBillMiddlePayDao;
import com.bytter.bill.domain.bill.entity.AbBillPayMiddle;
import com.bytter.bill.api.bill.dto.AbBillPayMiddleDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
/**
 * 付款办理挂接已付款票据中间Service层
 *
 * @author ouyj
 */
@Service("abBillPayMiddleService")
@Transactional(rollbackFor = Exception.class)
public class AbBillPayMiddleService extends BaseService<AbBillPayMiddle, String> {
    @Autowired
    private AbBillMiddlePayDao abBillMiddlePayDao;
    @Override
    public BaseDao<AbBillPayMiddle, String> getEntityDao() {
        return abBillMiddlePayDao;
    }
    /**
     * 根据票据从表id获取票据付款中间表集合
     *
     * @param vvid  票据从表id
     * @param payId 付款单id
     * @return 据付款中间表集合
     */
    public List<AbBillPayMiddleDTO> getAbBillPayMiddleList(String vvid, String payId) {
        return abBillMiddlePayDao.getAbBillPayMiddleList(vvid, payId);
    }
    /**
     * 根据付款表id修改票据付款中间表状态
     *
     * @param payId  付款表id
     * @param status 新状态
     */
    public void updateAbBillPayMiddleStatus(String payId, Integer status) {
        abBillMiddlePayDao.updateAbBillPayMiddleStatus(payId, status);
    }
    /**
     * 根据票据付款中间表id修改余额
     *
     * @param id      票据付款中间表id
     * @param balance 新增余额值
     */
    public void updateAbBillPayMiddleBalance(String id, double balance) {
        abBillMiddlePayDao.updateAbBillPayMiddleBalance(id, balance);
    }
    /**
     * 根据付款单id获取票据付款中间表集合
     *
     * @param payId 付款单id
     * @return 据付款中间表集合
     */
    public List<AbBillPayMiddleDTO> getAbBillPayMiddleListByPayId(String payId) {
        return abBillMiddlePayDao.getAbBillPayMiddleListByPayId(payId);
    }
    /**
     * 根据付款id删除对应的票据付款中间表数据
     *
     * @param payId 付款id
     */
    public void deleteAbBillPayMiddleByPayId(String payId) {
        abBillMiddlePayDao.deleteAbBillPayMiddleByPayId(payId);
    }
    /**
     * 根据票据从表id、付款单id删除对应的票据付款中间表数据
     *
     * @param billId 票据从表id
     * @param payId  付款单id
     */
    public void delBillPayMid(String billId, String payId) {
        // 根据票据从表id、付款单id查询对应的票据付款中间表数据
        List<AbBillPayMiddleDTO> abBillPayMiddleList = abBillMiddlePayDao.queryBillPayMid(billId, payId);
        if (!CollectionUtils.isEmpty(abBillPayMiddleList)) {
            // 要删除的票据付款中间表数据
            AbBillPayMiddleDTO delBillPayMiddle = abBillPayMiddleList.get(0);
            // 将票据从表id、付款单id对应的票据付款中间表数据删除
            abBillMiddlePayDao.deleteAbBillPayMiddleByPayId(billId, payId);
            // 查询billId最新的票据中间表数据
            List<AbBillPayMiddleDTO> lastPayMidList = abBillMiddlePayDao.getAbBillPayMiddleList(billId, null);
            if (!CollectionUtils.isEmpty(lastPayMidList)) {
                // 将删除的票据中间表记录数据的付款金额添加到当前最新票据中间表数据的余额中
                AbBillPayMiddleDTO lastPayMid = lastPayMidList.get(0);
                // 如果删除的这笔数据创建时间大于当前最新票据中间表数据，则不用新增余额
                if (lastPayMid.getCreateDate() != null &&
                        lastPayMid.getCreateDate().compareTo(delBillPayMiddle.getCreateDate()) > 0) {
                    abBillMiddlePayDao.updateAbBillPayMiddleBalance(lastPayMid.getId(), delBillPayMiddle.getPayMoney().doubleValue());
                }
            }
        }
    }
}
