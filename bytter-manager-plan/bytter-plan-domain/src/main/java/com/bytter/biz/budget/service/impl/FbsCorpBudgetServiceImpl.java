package com.bytter.biz.budget.service.impl;
import java.util.List;
import java.util.Map;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.budget.service.FbsCorpBudgetService;
import com.bytter.biz.budget.spi.dto.FbsCorpBudgetDTO;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.vo.FbsCorpBudgetVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
/**
 * 单位资金计划设置表接口实现类
 *
 * @author zuoy 2018-06-13
 */
@Service("fbsCorpBudgetServiceImpl")
@Transactional
@DubboService
public class FbsCorpBudgetServiceImpl implements IFbsCorpBudgetService {
	@Autowired
	private FbsCorpBudgetService fbsCorpBudgetService;
	@Override
	public boolean reportBudgetByCorpId(String corpId) {
		return this.fbsCorpBudgetService.reportBudgetByCorpId(corpId);
	}
	@Override
	public boolean controlBudgetByCorpId(String corpId) {
		return this.fbsCorpBudgetService.controlBudgetByCorpId(corpId);
	}
	@Override
	public boolean collectFlagByCorpId(String corpId) {
		return this.fbsCorpBudgetService.collectFlagByCorpId(corpId);
	}
	@Override
	public List<FbsCorpBudgetDTO> listFbsCorpBudgetDTOByReportBudget() {
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listFbsCorpBudgetVOByReportBudget();
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
	}
	@Override
	public List<FbsCorpBudgetDTO> listFbsCorpBudgetDTOByControlBudget() {
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listFbsCorpBudgetDTOByControlBudget();
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
	}
	@Override
	public List<FbsCorpBudgetDTO> listReportBudgetByParentCorpId(String parentCorpId) {
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(parentCorpId);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
	}
	@Override
	public List<FbsCorpBudgetDTO> listReportAndCollectByParentCorpId(String parentCorpId) {
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(parentCorpId);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
	}
	@Override
	public boolean getChildCollectFlag(String corpId) {
		return this.fbsCorpBudgetService.getChildCollectFlag(corpId);
	}
	/**
	 * 获取汇总单位集合
	 *
	 * @param parentCorpIds
	 * @return
	 */
	public Map<String, String> listReportAndCollectByParentCorpId(List<String> parentCorpIds) {
		return this.fbsCorpBudgetService.listReportAndCollectByParentCorpId(parentCorpIds);
	}
	@Override
	public boolean getCollectFlag(String corpId) {
		return this.fbsCorpBudgetService.getCollectFlag(corpId);
	}
	@Override
	public FbsCorpBudgetDTO getByCorpId(String corpId) {
		FbsCorpBudgetVO vo = this.fbsCorpBudgetService.getCorpBudgetVOByCorpId(corpId);
		if (EmptyUtils.isEmpty(vo)) {
			return null;
		}
		FbsCorpBudgetDTO dto = new FbsCorpBudgetDTO();
		BeanUtils.copyNotNullProperties(vo, dto);
		return dto;
	}
	@Override
	public List<FbsCorpBudgetDTO> listByCorpIds(List<String> corpIds) {
		List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listByCorpIds(corpIds);
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
	}
	@Override
	public List<String> listCorpIdsByReportBudget(List<String> corpIds) {
		return this.fbsCorpBudgetService.listCorpIdsByReportBudget(corpIds);
	}

    @Override
    public boolean getChildCollectFlagMap(List<String> ids){
        return this.fbsCorpBudgetService.getChildCollectFlagMap(ids);
    }


    @Override
    public List<FbsCorpBudgetDTO> listReportBudgetByParentCorpId(List<String> parentCorpIds) {
        List<FbsCorpBudgetVO> list = this.fbsCorpBudgetService.listReportBudgetByParentCorpId(parentCorpIds);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return BeanUtils.copyListProperties(list, FbsCorpBudgetDTO.class);
    }


    @Override
    public Map<String, Boolean>queryCollectFlagMap(List<String> corpIds) {
        return this.fbsCorpBudgetService.queryCollectFlagMap(corpIds);
    }


}
