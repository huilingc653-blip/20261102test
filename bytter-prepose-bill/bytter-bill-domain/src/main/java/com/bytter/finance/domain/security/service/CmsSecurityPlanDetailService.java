package com.bytter.finance.domain.security.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.security.dao.CmsSecurityPlanDetailDao;
import com.bytter.finance.domain.security.entity.CmsSecurityPlanDetail;
import com.bytter.finance.domain.security.vo.CmsSecurityPlanDetailVO;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("cmsSecurityPlanDetailService")
@Transactional(rollbackFor = Exception.class)
public class CmsSecurityPlanDetailService extends BaseService<CmsSecurityPlanDetail, String>
        implements IUserExcelVerifyHandler<CmsSecurityPlanDetailVO> {
    @Autowired
    private CmsSecurityPlanDetailDao cmsSecurityPlanDetailDao;
    @Override
    public BaseDao<CmsSecurityPlanDetail, String> getEntityDao() {
        return cmsSecurityPlanDetailDao;
    }
    /*
     * 根据编号获取计划明细
     */
    public List<CmsSecurityPlanDetailVO> getPlanDetailByCode(String code) {
        return cmsSecurityPlanDetailDao.getPlanDetailByCode(code);
    }
    /**
     * 根据计划主表id删除计划明细表数据
     *
     * @param planId
     *            计划主表id
     * @param detailIds
     *            计划明细表id
     * @return 结果数
     */
    public void deleteOtherDetail(String planId, List<String> detailIds) {
        if (StringUtils.isNotEmpty(planId)) {
            cmsSecurityPlanDetailDao.deleteOtherDetail(planId, detailIds);
        }
    }
    @Override
    public ExcelVerifyHanlderResult verifyHandler(CmsSecurityPlanDetailVO cmsSecurityPlanDetailVO, Object... objects) {
        return null;
    }
    @Override
    public void verifyHandler(List<CmsSecurityPlanDetailVO> list, Object... objects) {
        if (!ListUtils.isNullOrEmpty(list)) {
            for (CmsSecurityPlanDetailVO vo : list) {
                List<String> errorMsgList = new ArrayList<>();
                if (EmptyUtils.isEmpty(vo.getRepayDate())) {
                    errorMsgList.add("还款日期不能为空");
                }
                if (EmptyUtils.isEmpty(vo.getPlanRepayDate())) {
                    errorMsgList.add("计划还款日期不能为空");
                }
                if (EmptyUtils.isEmpty(vo.getRepayMoney())) {
                    errorMsgList.add("还款本金不能为空");
                }
                if (EmptyUtils.isEmpty(vo.getRepayInterest())) {
                    errorMsgList.add("还款利息不能为空");
                }
                if (EmptyUtils.isEmpty(vo.getIsRepay())) {
                    vo.setIsRepay("0");
                } else {
                    if (vo.getIsRepay().equals("是")) {
                        vo.setIsRepay("1");
                    } else {
                        vo.setIsRepay("0");
                    }
                }
                vo.addErrorMsg(errorMsgList);
            }
        }
    }
}
