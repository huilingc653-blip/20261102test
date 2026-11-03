package com.bytter.biz.finacial.paramconfig.service.impl;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.finacial.paramconfig.entity.BtSummary;
import com.bytter.biz.finacial.paramconfig.service.BtSummaryService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.biz.finacial.api.dto.BtSummaryDTO;
import com.bytter.biz.finacial.api.service.IFcFinacialParamConfigService;
@Service("fcFinacialParamConfigServiceImpl")
@Transactional
@DubboService
public class FcFinacialParamConfigServiceImpl implements IFcFinacialParamConfigService {
	@Autowired
	private BtSummaryService btSummaryService;
	
	/**
	 * <b>功能介绍：</b>根据ID获取摘要<br>
	 * <b>日期：</b>2017年9月12日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param id
	 * @return
	 */
	@Override
    public BtSummaryDTO getBtSummaryDTOById(String id){
		BtSummaryDTO btSummaryDTO = new BtSummaryDTO();
		BtSummary btSummary = btSummaryService.get(id, true);
		if(btSummary != null) {
			BeanUtils.copyNotNullProperties(btSummary, btSummaryDTO);
		}
		return btSummaryDTO;
	}
	@Override
	public void addBtSummary(BtSummaryDTO summaryDto, String userId) {
		BtSummary summary = new BtSummary();
		if(summaryDto != null) {
			BeanUtils.copyNotNullProperties(summaryDto, summary);
		}
		btSummaryService.save(summary, userId);
	}
	@Override
	public List<BtSummaryDTO> getBtSummaryDTOByContent(String content) {
		List<BtSummaryDTO> summarys = BeanUtils.copyListProperties(btSummaryService.listByContent(content), BtSummaryDTO.class);
		return summarys;
	}
	@Override
	public List<BtSummaryDTO> getListByValidSign(){
		List<BtSummaryDTO> summarys = BeanUtils.copyListProperties(btSummaryService.getListByValidSign(), BtSummaryDTO.class);
		return summarys;
	}
	@Override
	public void saveSummary(String userId, String... summaryContents) {
		btSummaryService.saveSummaryByOuterSys(userId,summaryContents);
	}
}
