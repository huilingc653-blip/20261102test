package com.bytter.biz.ttpay.service;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpPaymentQueryDao;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 第三方支付查询Service
 * @date 2022-05-16 15:43:05
 */
@Service("tpPaymentQueryService")
@Transactional
public class TpPaymentQueryService  extends BaseService<TpPayment, String> {
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private TpPaymentQueryDao tpPaymentQueryDao;
    @Autowired
    private TpPaymentService tpPaymentService;
    @Override
    public BaseDao<TpPayment, String> getEntityDao() {
        return tpPaymentQueryDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder(" and n.status<>" + NetbankConstant.STATUE_DEL);
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_PAYMENTQUERY_QUERY, user.getId()), "n.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput data = tpPaymentQueryDao.getDataTablesQuery(dataTablesInput, sql.toString(), null);
        return tpPaymentService.setJoinField(data);
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public TpPaymentVO getByIdUnion(String id) {
        TpPaymentVO tpPayment = tpPaymentQueryDao.getByIdUnion(id);
        //获取余额
        if (tpPayment != null) {
            // 账户余额默认为0
            /*tpPayment.setBalance(BigDecimal.ZERO);
            BisAccBalDTO bal = bisAccBalService.getLastBisAccBalBy(tpPayment.getPayerAcc(), tpPayment.getCurId());
            if (bal != null) {
                tpPayment.setBalance(bal.getBal());
            }*/
            if (tpPayment.getBalance() == null) {
                tpPayment.setBalance(BigDecimal.ZERO);
            }
            //设置付款账号的可用余额
            if(StringUtils.isBlank(tpPayment.getPayerAcc())){
                //账号为空的时候，账户余额默认为0
                tpPayment.setAvailBal(BigDecimal.ZERO);
            } else {
                //获取账户在途余额
                /*BigDecimal useBal = tpPaymentDao.getUseBal(tpPayment.getPayerAcc(), tpPayment.getCurId());
                BigDecimal availBal = tpPayment.getBalance().subtract(useBal);
                tpPayment.setAvailBal(availBal.add(tpPayment.getPayMoney()));*/
            }
        }
        return tpPayment;
    }
    /**
     * 批量退回付款失败单据
     *
     * @param ids
     */
    public void batchRecallTpPayment(String ids, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            tpPaymentService.recallTpPayment(id, user);
        }
    }
}
