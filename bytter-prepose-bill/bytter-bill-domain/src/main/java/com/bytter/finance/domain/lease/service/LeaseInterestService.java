package com.bytter.finance.domain.lease.service;
import com.bytter.finance.domain.lease.dao.LeaseInterestDao;
import com.bytter.finance.domain.lease.entity.LeaseInterest;
import com.bytter.finance.domain.lease.vo.LeaseInterestVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 租赁利息预计明细
 * @date 2022-04-20 15:50:26
 */
@Service("leaseInterestService")
@Transactional
public class LeaseInterestService extends BaseService<LeaseInterest, String> {
    @Autowired
    private LeaseInterestDao leaseInterestDao;
    @Override
    public BaseDao<LeaseInterest, String> getEntityDao() {
        return leaseInterestDao;
    }
    /************* 以下为旧利息测算明细 ***************/
    @Transactional(readOnly = true)
    public List<LeaseInterestVo> queryInterestList(String sendBillCode) {
        List list = leaseInterestDao.queryInterestList(sendBillCode);
        List<LeaseInterestVo> listVo = BeanUtils.copyListProperties(list, LeaseInterestVo.class);
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        String signRepayData = "";
        BigDecimal countRepayMoney = BigDecimal.ZERO;// 合计还款金额
        BigDecimal countPayInterest = BigDecimal.ZERO;// 合计实付利息
        BigDecimal countRealInterest = BigDecimal.ZERO;// 合计实付金额
        int count = 0;
        for (int i = listVo.size() - 1; i >= 0; i--) {
            LeaseInterestVo vo = listVo.get(i);
            if (signRepayData.equals(DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd"))
                    || "".equals(signRepayData)) {// 同一个还款日期的利息明细数据
                count++;
            } else {
                listVo.get(i + 1).setCount(count);
                listVo.get(i + 1).setCountRepayMoney(countRepayMoney);
                listVo.get(i + 1).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {// 未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(i + 1).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }
            countRepayMoney = countRepayMoney.add(vo.getRepayMoney());
            countPayInterest = countPayInterest.add(vo.getPayInterest());
            if (vo.getStatus() == 0) {// 未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                countRealInterest = countRealInterest.add(vo.getRealInterest());
            }
            signRepayData = DateUtils.format(vo.getSignRepayDate(), "yyyy-MM-dd");
            if (i == 0) {
                listVo.get(0).setCount(count);
                listVo.get(0).setCountRepayMoney(countRepayMoney);
                listVo.get(0).setCountPayInterest(countPayInterest);
                countPayInterest = BigDecimal.ZERO;
                countRepayMoney = BigDecimal.ZERO;
                if (vo.getStatus() == 0) {// 未还的实付金额累加，还款之后实付金额全部修改成总实付金额
                    listVo.get(0).setRealInterest(countRealInterest);
                    countRealInterest = BigDecimal.ZERO;
                }
                count = 1;
            }
        }
        return listVo;
    }
}
