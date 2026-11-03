package com.bytter.biz.finacial.api.service;
import java.util.List;
import com.bytter.biz.finacial.api.dto.BtSummaryDTO;
/**
 * 核算模块对外接口
 * @author caozx
 * @since 2017-9-12
 */
public interface IFcFinacialParamConfigService {
	/**
	 * 根据ID获取摘要
	 * @param id 摘要Id
	 * @return 摘要实体
	 */
	BtSummaryDTO getBtSummaryDTOById(String id);
    /**
     * 获取有效的摘要词组
	 * @return 摘要实体
     */
	List<BtSummaryDTO> getListByValidSign();
	
	/**
	 * 根据内容获取摘要
	 * @param content
	 * @return 摘要集合
	 */
	List<BtSummaryDTO> getBtSummaryDTOByContent(String content);
	
	/**
	 * 添加摘要
	 * @param summary 摘要实体
	 * @param userId 用户Id
	 */
	public void addBtSummary(BtSummaryDTO summary, String userId);
	
	/**
	 * 保存摘要内容，摘要存在时则不保存。
	 * @param userId 用户Id
	 * @param summaryContents 摘要内容
	 */
    void saveSummary(String userId, String... summaryContents);
	
}
