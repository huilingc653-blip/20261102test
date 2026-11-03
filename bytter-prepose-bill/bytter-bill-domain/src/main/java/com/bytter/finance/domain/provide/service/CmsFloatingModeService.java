package com.bytter.finance.domain.provide.service;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.provide.dao.CmsFloatingModeDao;
import com.bytter.finance.domain.provide.entity.CmsFloatingMode;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.CmsFloatingModeVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: CMS_Floating_Mode
 * @author songkk
 * @date 2017-08-24 11:09:43
 * @version 1.0
 *
 */
@Service("cmsFloatingModeService")
@Transactional
public class CmsFloatingModeService extends BaseService<CmsFloatingMode, String> {
    @Autowired
    private CmsFloatingModeDao cmsFloatingModeDao;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private LoanInfoService loanInfoService;
    /**
     * 查询CmsFloatingModeVO DataTable数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput listcmsFloatingMode(DataTablesInput dataTablesInput, String billCode) {
        return cmsFloatingModeDao.listCmsFloatingMode(dataTablesInput, billCode);
    }
    /**
     * 根据单号查询最后一笔数据
     *
     * @param billCode
     * @return
     */
    @Transactional(readOnly = true)
    public CmsFloatingModeVO getCmsFloatingMode(String id, String billCode) {
        List<CmsFloatingModeVO> cmsList = cmsFloatingModeDao.getCmsFloatingMode(id);
        CmsFloatingModeVO vo;
        String date = null;
        if (StringUtils.isNotEmpty(billCode)) {
            date = DateUtils.date2Str(cmsProvideLoanSendService.getLoanSendVOBySendBillCode(billCode).getSendDate(),
                    DateUtils.date_sdf());
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (cmsList != null && cmsList.size() > 0) {
            vo = cmsList.get(0);
            vo.setToDayDate(DateUtils.date2Str(vo.getEnterDate(), DateUtils.date_sdf()));
        } else {
            vo = new CmsFloatingModeVO();
            vo.setToDayDate(date);
        }
        vo.setBillCode(billCode);
        vo.setId(id);
        return vo;
    }
    /**
     * 保存浮动方式表
     *
     * @param cmsFloatingModeVO
     * @param user
     */
    public void save(CmsFloatingModeVO cmsFloatingModeVO, ICurrentUser user) {
        CmsFloatingMode entity = new CmsFloatingMode();
        entity.setBillCode(cmsFloatingModeVO.getId());
        entity.setFloatingMode(cmsFloatingModeVO.getFloatingMode());
        // 当为固定利率时 :floatModeNext
        if (!cmsFloatingModeVO.getFloatingMode().equals(CmsConstant.FLOAT_WAY_02)
                && !cmsFloatingModeVO.getFloatingMode().equals(CmsConstant.FLOAT_WAY_01)) {
            entity.setFloatModeNext(cmsFloatingModeVO.getFloatModeNext());
        }
        entity.setPointsText(cmsFloatingModeVO.getPointsText());
        entity.setReferenceRateType(cmsFloatingModeVO.getReferenceRateType());
        entity.setRepayCycle(cmsFloatingModeVO.getRepayCycle());
        if (CmsConstant.FLOAT_WAY_01.equals(cmsFloatingModeVO.getFloatingMode())
                && !EmptyUtils.isEmpty(cmsFloatingModeVO.getFixedRate())) {
            // 报价日期和点数是浮动方式为固定利率时来计算固定利率的，这里的固定利率是pl利率即加过点数或百分比的
            // 在浮动方式为非固定利率时，报价日期和固定利率是没有意义的所以为空
            entity.setRateQuotedPriceDate(cmsFloatingModeVO.getRateQuotedPriceDate());
            entity.setFixedRate(cmsFloatingModeVO.getFixedRate());
        }
        entity.setFloatingModeName(cmsFloatingModeVO.getFloatingModeName());
        entity.setEnterDate(cmsFloatingModeVO.getEnterDate());
        entity.setCreateName(user.getUsername());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        // 保存基础利率
        ProvideLoanSend send = cmsProvideLoanSendService.get(cmsFloatingModeVO.getId());
        if (send != null) {
            LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(
                    DateUtils.formatDate(send.getSendDate(), "yyyy-MM-dd"), send.getReferenceRateType(),
                    send.getCurCode(), send.getRepayCycle());
            entity.setRate(rate.getRate());
        }
        super.saveEntity(entity);
        // 调整个别借款利息测算数据
        cmsProvideInterestService.adjustProvideInterest(send, user);
    }
    @Override
    public BaseDao<CmsFloatingMode, String> getEntityDao() {
        // TODO Auto-generated method stub
        return cmsFloatingModeDao;
    }
}
