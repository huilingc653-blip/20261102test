package com.bytter.biz.wage.service.impl;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcBackService;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author huangx
 */
@Service("wageBisExcBackService")
@Transactional
@DubboService(group="nisWage")
public class WageBisExcBackServiceImpl implements IBisExcBackService {
    @Autowired
    private NisWageService nisWageService;
    @Autowired
    private NisWageDetailService nisWageDetailService;
    @Override
    public void backBisExc(BisExcDTO bisExc, ICurrentUser user) {
    }
    @Override
    public void defeatBisExc(BisExcDTO bisExc, ICurrentUser user) {
    }
    @Override
    public void sendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {
    }
    @Override
    public void resendBisExc(BisExcDTO bisExc, ICurrentUser user) {
    }
    @Override
    public void setPayResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
        //更新代发工资明细的交易状态
        if(StringUtils.isNotBlank(bisExc.getExpenseDtlIds())) {
            //勾选的明细更新为失败
            this.nisWageDetailService.updateVoucherStatById(bisExc.getVoucherNo(), bisExc.getCbsBillId(), bisExc.getVoucherStat(), bisExc.getExpenseDtlIds(), true);
            //剩余的明细更新为成功
            this.nisWageDetailService.updateVoucherStatById(bisExc.getVoucherNo(), bisExc.getCbsBillId(), BdlinkConstant.SUCCESS, bisExc.getExpenseDtlIds(), false);
        }else {
            //同步更新等待结果的明细状态为成功/失败
            this.nisWageDetailService.updateVoucherStatById(bisExc.getVoucherNo(), bisExc.getCbsBillId(), bisExc.getVoucherStat());
        }
        // 回盘全部成功时把工资主单在途占用转为成功用量；存在失败或待定明细时继续保留占用，等待重发或业务打回。
        this.nisWageService.markEnterpriseLimitSuccess(bisExc.getCbsBillId(), user);
    }
    @Override
    public void queryResultBisExc(BisExcDTO bisExc, ICurrentUser user) {
    }
	@Override
	public void checkSendBisExcAccounting(BisExcDTO bisExc, ICurrentUser user) {
		// TODO Auto-generated method stub
		
	}
}
