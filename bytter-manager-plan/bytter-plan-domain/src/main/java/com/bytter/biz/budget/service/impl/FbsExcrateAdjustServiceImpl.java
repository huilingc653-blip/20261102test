package com.bytter.biz.budget.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.service.FbsExcrateAdjustService;
import com.bytter.biz.budget.spi.dto.FbsExcrateAdjustDTO;
import com.bytter.biz.budget.spi.service.IFbsExcrateAdjustService;
import com.bytter.biz.budget.vo.FbsExcrateAdjustVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
 * @author wangzx
 * @date 2019/8/15
 */
@Service("fbsExcrateAdjustServiceImpl")
@Transactional
@DubboService
public class FbsExcrateAdjustServiceImpl implements IFbsExcrateAdjustService {
	@Autowired
	private FbsExcrateAdjustService fbsExcrateAdjustService;
	/**
	 * 根据原始货币id和目标货币id和日期查找最新一笔计划汇率调整表信息
	 * @param curId 原始货币id
	 * @param referCurId 目标货币id
	 * @param endDate 结束日期
	 * @return
	 */
	@Override
	public FbsExcrateAdjustDTO getFbsExcrateAdjustByCurReferDate(String curId, String referCurId, Date endDate) {
		FbsExcrateAdjustDTO dto = new FbsExcrateAdjustDTO();
		if(curId.equals(referCurId)){
			//同币别，汇率为1
			dto.setExchangeRate(BigDecimal.ONE);
		}else {
			FbsExcrateAdjustVO vo = this.fbsExcrateAdjustService.getFbsExcrateAdjustByCurReferDate(curId, referCurId, endDate);
			if (!EmptyUtils.isEmpty(vo)) {
				BeanUtils.copyNotNullProperties(vo, dto);
			}
		}
		return dto;
	}
}
