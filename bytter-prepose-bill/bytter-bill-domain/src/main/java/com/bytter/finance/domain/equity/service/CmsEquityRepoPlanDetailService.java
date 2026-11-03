package com.bytter.finance.domain.equity.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.dao.CmsEquityRepoPlanDetailDao;
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
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
 * 股权融资回购计划明细
 *
 * @author zhenghj
 */
@Service("cmsEquityRepoPlanDetailService")
@Transactional
public class CmsEquityRepoPlanDetailService extends BaseService<CmsEquityRepoPlanDetail, String>
        implements IUserExcelVerifyHandler<CmsEquityRepoPlanDetailVO> {
    @Autowired
    private CmsEquityRepoPlanDetailDao cmsEquityRepoPlanDetailDao;
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
    public List<CmsEquityRepoPlanDetailVO> listRepoPlanDetailByEquityCodeOrPlanId(String planId, String equityCode) {
        if (StringUtils.isEmpty(equityCode) && StringUtils.isEmpty(planId)) {
            return null;
        }
        return cmsEquityRepoPlanDetailDao.listRepoPlanDetailByEquityCodeOrPlanId(planId, equityCode);
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
    public int deleteRepoPlanDetailByPlanId(String planId, List<String> detailIds) {
        if (StringUtils.isNotEmpty(planId)) {
            return cmsEquityRepoPlanDetailDao.deleteRepoPlanDetailByPlanId(planId, detailIds);
        }
        return 0;
    }
    @Override
    public BaseDao<CmsEquityRepoPlanDetail, String> getEntityDao() {
        return cmsEquityRepoPlanDetailDao;
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsEquityRepoPlanDetailVO cmsEquityRepoPlanDetailVO,
            Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsEquityRepoPlanDetailVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsEquityRepoPlanDetailVO planDetailVO : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(planDetailVO.getRepoDate())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_REPODATE_ISNULL);
                }
                if (EmptyUtils.isEmpty(planDetailVO.getRepoMoney())) {
                    errorMsgList.add(CmsConstant.CMS_EQUITY_REPOMONEY_ISNULL);
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
}
