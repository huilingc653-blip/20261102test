package com.bytter.finance.domain.provide.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.dao.CmsProvideLoanRateDao;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRate;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.CmsProvideLoanRateVO;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.dto.FcInstrateAdjustDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: CMS_PROVIDE_LOAN_RATE
 * @date 2017-08-29 10:36:00
 */
@Service("cmsProvideLoanRateService")
@Transactional
public class CmsProvideLoanRateService extends BaseService<CmsProvideLoanRate, String> {
    @Autowired
    private CmsProvideLoanRateDao cmsProvideLoanRateDao;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @DubboReference(check = false)
    private IFcInstrateAdjustService fcInstrateAdjustService;
    /**
     * 查询CmsProvideLoanRate DataTable数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput, String billCode) {
        return cmsProvideLoanRateDao.getDataTables(dataTablesInput, billCode);
    }
    /**
     * 保存借款利率调整
     *
     * @param entity
     */
    public void save(CmsProvideLoanRate entity, ICurrentUser user) {
        CmsProvideLoanRateVO oldEntity = this.getCmsProvideLoanRateVO(entity.getBillCode(), null);
        entity.setCreateName(user.getUsername());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        entity.setStatus(CmsConstant.TEMP);
        super.saveEntity(entity);
        CmsProvideLoanInfo info = loanInfoService.get(entity.getBillCode());
        List<ProvideLoanSend> sends = cmsProvideLoanSendService.getLoanSendCompeletotalByBillCode(info.getBillCode());
        logger.debug(" save before adjustProvideInterest sends: " + JsonUtils.toJSONString(sends));
        if (!EmptyUtils.isEmpty(sends)) {
            for (ProvideLoanSend send : sends) {
                cmsProvideInterestService.adjustProvideInterest(send, user);
                if (!EmptyUtils.isEmpty(send.getLoanAcc())) {
                    FcInstrateAdjustDTO dto = new FcInstrateAdjustDTO();// 借款帐号的利率调整记录
                    dto.setAccount(send.getLoanAcc());
                    dto.setOperatorCode(user.getLoginName());
                    dto.setAdjustTime(entity.getEnterDate());
                    // dto.setInstrateId();
                    dto.setStatus(CommonConstant.STATUE_CHECK);
                    dto.setChangtype(FinacialConstant.INSTRATE_CHANGT_YPE);
                    if (!EmptyUtils.isEmpty(entity.getPlRate()) && !EmptyUtils.isEmpty(oldEntity.getPlRate())) {
                        FcInstrateAdjustDTO normalDto = new FcInstrateAdjustDTO();
                        BeanUtils.copyProperties(dto, normalDto);
                        normalDto.setInstrtType(InterestConstant.INSTRT_TYPE_NORMAL);
                        normalDto.setOldInstrate(oldEntity.getPlRate());
                        normalDto.setNewInstrate(entity.getPlRate());
                        fcInstrateAdjustService.saveFcInstrateAdjust(normalDto);
                    }
                    if (!EmptyUtils.isEmpty(entity.getOverdueRate())
                            && !EmptyUtils.isEmpty(oldEntity.getOverdueRate())) {
                        FcInstrateAdjustDTO overdueDto = new FcInstrateAdjustDTO();
                        BeanUtils.copyProperties(dto, overdueDto);
                        overdueDto.setInstrtType(InterestConstant.INSTRT_TYPE_OVERDUE);
                        overdueDto.setOldInstrate(oldEntity.getOverdueRate());
                        overdueDto.setNewInstrate(entity.getOverdueRate());
                        fcInstrateAdjustService.saveFcInstrateAdjust(overdueDto);
                    }
                    if (!EmptyUtils.isEmpty(entity.getDeferredRate())
                            && !EmptyUtils.isEmpty(oldEntity.getDeferredRate())) {
                        FcInstrateAdjustDTO delayDto = new FcInstrateAdjustDTO();
                        BeanUtils.copyProperties(dto, delayDto);
                        delayDto.setInstrtType(InterestConstant.INSTRT_TYPE_DELAY);
                        delayDto.setOldInstrate(oldEntity.getDeferredRate());
                        delayDto.setNewInstrate(entity.getDeferredRate());
                        fcInstrateAdjustService.saveFcInstrateAdjust(delayDto);
                    }
                }
            }
        }
    }
    /**
     * 获取CmsProvideLoanRateVO
     */
    @Transactional(readOnly = true)
    public CmsProvideLoanRateVO getCmsProvideLoanRateVO(String id, String billCode) {
        List<CmsProvideLoanRateVO> cmsList = cmsProvideLoanRateDao.listCmsProvideLoanRateVO(id);
        CmsProvideLoanRateVO cmsVo;
        String date = null;
        if (StringUtils.isNotEmpty(billCode)) {
            date = DateUtils.date2Str(loanInfoService.getLoanInfoVOByBillCode(billCode).getPlDate(),
                    DateUtils.date_sdf());
        } else {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if (cmsList != null && cmsList.size() > 0) {
            cmsVo = cmsList.get(0);
            cmsVo.setToDayDate(DateUtils.date2Str(cmsVo.getEnterDate(), DateUtils.date_sdf()));
        } else {
            cmsVo = new CmsProvideLoanRateVO();
            CmsProvideLoanInfo loanInfo = loanInfoService.get(id);
            cmsVo.setDeferredRate(loanInfo.getDeferredRate());
            cmsVo.setOverdueRate(loanInfo.getOverdueRate());
            cmsVo.setPlRate(loanInfo.getPlRate());
            cmsVo.setToDayDate(date);
        }
        cmsVo.setBillCode(billCode);
        cmsVo.setId(id);
        return cmsVo;
    }
    public BaseDao<CmsProvideLoanRate, String> getEntityDao() {
        return cmsProvideLoanRateDao;
    }
}
