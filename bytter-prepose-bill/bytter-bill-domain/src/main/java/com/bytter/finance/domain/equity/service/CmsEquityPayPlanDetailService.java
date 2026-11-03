package com.bytter.finance.domain.equity.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.dao.CmsEquityPayPlanDetailDao;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
/**
 * 缴付/过户计划明细登记
 *
 * @author zhenghj
 */
@Service("cmsEquityPayPlanDetailService")
@Transactional
public class CmsEquityPayPlanDetailService extends BaseService<CmsEquityPayPlanDetail, String>
        implements IUserExcelVerifyHandler<CmsEquityPayPlanDetailVO> {
    @Autowired
    private CmsEquityPayPlanDetailDao cmsEquityPayPlanDetailDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 根据计划主表id查询计划明细
     *
     * @param planId
     *            计划主表id
     * @param equityCode
     *            融资编号
     * @return 计划明细
     */
    public List<CmsEquityPayPlanDetailVO> listPayPlanDetailByEquityCodeOrPlanId(String planId, String equityCode) {
        if (StringUtils.isEmpty(equityCode) && StringUtils.isEmpty(planId)) {
            return null;
        }
        return cmsEquityPayPlanDetailDao.listPayPlanDetailByEquityCodeOrPlanId(planId, equityCode);
    }
    /**
     * 根据计划主表id删除计划明细表数据，如果参数明细表ids不为空，则排除
     * 
     * @param planId
     *            计划主表id
     * @param detailIds
     *            计划明细表id
     * @return 结果数
     */
    public int deletePayPlanDetailByPlanId(String planId, List<String> detailIds) {
        if (StringUtils.isNotEmpty(planId)) {
            return cmsEquityPayPlanDetailDao.deletePayPlanDetailByPlanId(planId, detailIds);
        }
        return 0;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsEquityPayPlanDetailVO cmsEquityPayPlanDetailVO,
            Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsEquityPayPlanDetailVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsEquityPayPlanDetailVO planDetailVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(planDetailVO.getPayDate())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_PAYDATE_ISNULL);
                }
                if (EmptyUtils.isEmpty(planDetailVO.getPayMoney())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_PAYMONEY_ISNULL);
                }
                if (!EmptyUtils.isEmpty(planDetailVO.getIsPayName())) {
                    if (planDetailVO.getIsPayName().equals("是")) {
                        planDetailVO.setIsPay("1");
                    } else {
                        planDetailVO.setIsPay("0");
                    }
                } else {
                    planDetailVO.setIsPay("0");
                }
                planDetailVO.addErrorMsg(errorMsgList);
            }
        }
    }
    public AjaxJson saveImport(List<CmsEquityPayPlanDetailVO> list) {
        AjaxJson ajaxJson = new AjaxJson();
        String addIds = null;
        for (CmsEquityPayPlanDetailVO detailVO : list) {
            CmsEquityPayPlanDetail detail = new CmsEquityPayPlanDetail();
            BeanUtils.copyNotNullProperties(detailVO, detail);
            /*
             * String equityCode = this.getEquityCode(); detail.setEquityCode(equityCode); this.saveDetail(detail); if
             * (addIds == null) { addIds = detail.getId(); } else { addIds = addIds + "," + detail.getId(); }
             */
        }
        ajaxJson.setObj(addIds);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }
    @Override
    public BaseDao<CmsEquityPayPlanDetail, String> getEntityDao() {
        return cmsEquityPayPlanDetailDao;
    }
}
