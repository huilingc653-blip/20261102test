package com.bytter.biz.budget.spi.service;
import java.util.Date;
import com.bytter.biz.budget.spi.dto.FbsExcrateAdjustDTO;
/**
 * 计划汇率查询
 */
public interface IFbsExcrateAdjustService {
	/**
	 * 根据原始货币id和目标货币id和日期查找最新一笔计划汇率调整表信息
	 * @param curId 原始货币id
	 * @param referCurId 目标货币id
	 * @param endDate 结束日期
	 * @return 计划汇率DTO
	 */
	FbsExcrateAdjustDTO getFbsExcrateAdjustByCurReferDate(String curId, String referCurId, Date endDate);
}
