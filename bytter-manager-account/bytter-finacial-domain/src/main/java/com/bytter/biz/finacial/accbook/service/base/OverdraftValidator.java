package com.bytter.biz.finacial.accbook.service.base;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
/**
 * 账户联机余额透支检查
 * 
 * @author liyh
 *
 */
@Component
@Service("overdraftValidator")
@Transactional
public class OverdraftValidator extends BaseService {
    @Autowired
    private CheckBalanceHandler checkBalanceHandler;
    @Autowired
    private OnlineBalanceHandler onlineBalanceHandler;
	
	/**
	 * 功能介绍 : 处理账户余额计算。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	public Map<String, FcOnlineLedgerDTO> processVoucherBalance(VoucherWrapper voucher) {
		if (OperateType.CHECK == voucher.getOpType()) {
			return checkBalanceHandler.processAccountBalance(voucher);
		} else {
			return onlineBalanceHandler.processAccountBalance(voucher);
		}
	}
	
	/**
	 * 功能介绍 : 处理账户余额计算。<br>
	 * <b>日期：</b>2017年6月6日<br>
	 * 
	 * @version 1.0
	 * @author liyh
	 */
	public Map<String, FcOnlineLedgerDTO> checkProcessVoucherBalance(VoucherWrapper voucher) {
		if (OperateType.CHECK == voucher.getOpType()) {
			return checkBalanceHandler.checkProcessAccountBalance(voucher);
		} else {
			return onlineBalanceHandler.checkProcessAccountBalance(voucher);
		}
	}
	
	/**
	 * 获取联机余额信息
	 * @param voucher
	 * @return
	 */
	public List<FcOnlineLedgerDTO> listFcOnlineLedger(VoucherWrapper voucher){
		return checkBalanceHandler.ListAccoutOnlineInfo(voucher);
	}
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
