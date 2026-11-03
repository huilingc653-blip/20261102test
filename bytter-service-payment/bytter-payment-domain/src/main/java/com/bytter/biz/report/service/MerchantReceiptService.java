package com.bytter.biz.report.service;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.report.dao.MerchantReceiptDao;
import com.bytter.biz.report.vo.NisBillheadVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * @author zhuop
 * @Description 客商收入资金量统计情况
 * @date 2018.8.15
 */
@Service("merchantReceiptService")
@Transactional
public class MerchantReceiptService extends BaseService<NisBillheadVO, String>{
    @Autowired
	private MerchantReceiptDao merchantReceiptDao;
    @DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBtCurrencyService currencyService;
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@Override
	public BaseDao<NisBillheadVO, String> getEntityDao() {
		return merchantReceiptDao;
	}
	
	public DataTablesOutput getMerchantData (DataTablesInput input,ICurrentUser user){
		String changeCur=(String)input.getFormdata().get("changeCur");
		String reducedCurId=(String)input.getFormdata().get("reducedCurId");
		String rateType=(String)input.getFormdata().get("rateType");
		Date rateDate= DateUtils.getIntegralEndTime(DateUtils.parse((String)input.getFormdata().get("rateDate"), "yyyy-MM-dd"));
		List<String> corpAuth=corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY, user.getId());
		DataTablesOutput datas = merchantReceiptDao.getMerchantData(input,corpAuth);
		//获取汇率
		if (datas != null && datas.getData() != null) {
			List<NisBillheadVO> list = datas.getData();
			HashMap<String, BigDecimal> curExrateMap = new HashMap<>();
			for(NisBillheadVO vo:list){
				BigDecimal curExrate = BigDecimal.ONE;
				if(CommonConstant.YES.equals(changeCur)) {
					if (curExrateMap.containsKey(vo.getCurId())) {
						curExrate = curExrateMap.get(vo.getCurId());
					} else {
						curExrate = fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), reducedCurId, DateUtils.setSpecifiedDateOfTime(rateDate), rateType);
						curExrateMap.put(vo.getCurId(), curExrate);
					}
				}
				vo.setCurExrate(curExrate);
			}
		}
		return datas;
	}
	
   public DataTablesOutput getViewData (DataTablesInput input,ICurrentUser user){
	   List<String> corpAuth=corpService.getUserCorpIds(ResourceConstant.NETBANK_REPORT_MERCHANTRECEIPT_QUERY, user.getId());
		return merchantReceiptDao.getDetailData(input,corpAuth);
	}
}
