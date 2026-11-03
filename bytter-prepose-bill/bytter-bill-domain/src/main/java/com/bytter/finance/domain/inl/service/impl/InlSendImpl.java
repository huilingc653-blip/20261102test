package com.bytter.finance.domain.inl.service.impl;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.api.IInlSendService;
import com.bytter.finance.constant.InlConstant;
import com.bytter.finance.domain.inl.entity.CmsInlRepay;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.GuaContractCmsInlVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Transactional
@DubboService
@Service("inlSendImpl")
public class InlSendImpl implements IInlSendService {
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService infoService;
    @Autowired
    private InlRepayService inlRepayService;
    @DubboReference(check = false)
    private INisReceivableService nisReceivableService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @Override
    public void backInlSendForNisBillHead(String id, ICurrentUser user) {
        CmsInlSend cmsInlSend = inlSendService.get(id);
        cmsInlSend.setStatus(InlConstant.BACK);
        cmsInlSend.setBizStatus(InlConstant.BACK);
        inlSendService.updateEntity(cmsInlSend);
        NisReceivableDTO nisReceivableDTO = nisReceivableService.getNisReceivableByBillId(cmsInlSend.getId());
        if (null != nisReceivableDTO && StringUtils.isNotEmpty(nisReceivableDTO.getId())) {
            // 由付款单删除触发时，禁止应收再反向回删付款单，避免形成循环调用导致锁等待
            nisReceivableService.deleteNisReceivable(cmsInlSend.getId(), user, false);
        }
    }
    @Override
    public void backInlRepayForNisBillHead(String id, ICurrentUser user) {
        CmsInlRepay cmsInlRepay = inlRepayService.get(id);
        cmsInlRepay.setStatus(InlConstant.BACK);
        cmsInlRepay.setBizStatus(InlConstant.BACK);
        inlRepayService.updateEntity(cmsInlRepay);
        NisReceivableDTO nisReceivableDTO = nisReceivableService.getNisReceivableByBillId(cmsInlRepay.getId());
        if (null != nisReceivableDTO && StringUtils.isNotEmpty(nisReceivableDTO.getId())) {
            // 由付款单删除触发时，禁止应收再反向回删付款单，避免形成循环调用导致锁等待
            nisReceivableService.deleteNisReceivable(cmsInlRepay.getId(), user, false);
        }
    }
    @Override
    public void backInlSendForNisReceivable(String id, ICurrentUser user) {
        CmsInlSend cmsInlSend = inlSendService.get(id);
        cmsInlSend.setStatus(InlConstant.BACK);
        cmsInlSend.setBizStatus(InlConstant.BACK);
        inlSendService.updateEntity(cmsInlSend);
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadByErpId(cmsInlSend.getId());
        if (null != nisBillheadDTO && StringUtils.isNotEmpty(nisBillheadDTO.getId())) {
            nisBillheadService.deteleNisbillHeadById(nisBillheadDTO.getId(), user);
        }
    }
    @Override
    public void backInlRepayForNisReceivable(String id, ICurrentUser user) {
        CmsInlRepay cmsInlRepay = inlRepayService.get(id);
        cmsInlRepay.setStatus(InlConstant.BACK);
        cmsInlRepay.setBizStatus(InlConstant.BACK);
        inlRepayService.updateEntity(cmsInlRepay);
        NisBillheadDTO nisBillheadDTO = nisBillheadService.getNisBillheadByErpId(cmsInlRepay.getId());
        if (null != nisBillheadDTO && StringUtils.isNotEmpty(nisBillheadDTO.getId())) {
            nisBillheadService.deteleNisbillHeadById(nisBillheadDTO.getId(), user);
        }
    }
}
