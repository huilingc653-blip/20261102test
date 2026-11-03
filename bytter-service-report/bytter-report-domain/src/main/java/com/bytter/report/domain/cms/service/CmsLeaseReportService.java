package com.bytter.report.domain.cms.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.report.domain.cms.dao.CmsLeaseReportDao;
import com.bytter.report.domain.cms.entity.CmsFinancingLeaseInfo;
import com.bytter.report.domain.cms.vo.CmsLeaseReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;

import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("cmsLeaseReportService")
@Transactional
public class CmsLeaseReportService extends BaseService<CmsFinancingLeaseInfo, String> implements IWorkFlowListener {
    @Autowired
    private CmsLeaseReportDao cmsLeaseReportDao;

    @DubboReference
    private ICorpService corpService;
    @DubboReference
    private IBtCurrencyService btCurrencyService;
    @Override
    public BaseDao<CmsFinancingLeaseInfo, String> getEntityDao() {
        return cmsLeaseReportDao;
    }

    /**
     * 租赁统计表列表数据查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDatatables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_REPORT_VIEW, user.getId());
        DataTablesOutput dataTablesOutput=cmsLeaseReportDao.getDatatables(dataTablesInput, corpAuth);
        List<CmsLeaseReportVO> list=dataTablesOutput.getData();
        for(CmsLeaseReportVO vo:list){
            String corpName=corpService.getCorpName(vo.getCorpId());
            vo.setCorpName(corpName);
            BtCurrencyDTO currencyDTO= btCurrencyService.getObjectById(vo.getOriCurid());// 设置币别
            vo.setOriCurName(currencyDTO.getCurName());
        }
        return dataTablesOutput;
    }

    /**
     * 租赁统计表列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDetailDatatables(DataTablesInput dataTablesInput, String leaseId) {
        DataTablesOutput<CmsLeaseReportVO> vo = cmsLeaseReportDao.getDetailDatatables(dataTablesInput, leaseId);
        for (int i = 0; i < vo.getData().size(); i++) {
            //计算期次
            CmsLeaseReportVO reportVO = vo.getData().get(i);
            reportVO.setTimes(i + 1);
            long dates = 0;
            if (i == 0) {
                dates = DateUtils.dayDiffs(vo.getData().get(i).getPayDate(), DateUtils.getDateYMD());
            } else {
                dates = DateUtils.dayDiffs(vo.getData().get(i).getPayDate(), vo.getData().get(i - 1).getPayDate());
            }
            // 为负时置为0
            reportVO.setDates(dates < 0 ? 0 : dates);
        }
        return vo;
    }

    public CmsLeaseReportVO getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return new CmsLeaseReportVO();
        }
        List<CmsLeaseReportVO> list = cmsLeaseReportDao.getByLeaseId(leaseId);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsLeaseReportVO();
        }
        // 获取承租人单位名称
        List<String> corpIdList = list.stream().map(CmsLeaseReportVO::getCorpId).distinct().collect(Collectors.toList());
        Map<String, String> corpMap = corpService.getNameByIds(corpIdList);
        if (ObjectUtil.isNotEmpty(corpMap)){
            list.forEach(item-> Optional.ofNullable(item.getCorpId()).flatMap(corpId -> Optional.ofNullable(corpMap.get(corpId))).ifPresent(item::setCorpName));
        }
        return list.get(0);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {

    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }
}
