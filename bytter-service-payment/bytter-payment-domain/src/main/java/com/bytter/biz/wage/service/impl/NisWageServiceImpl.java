package com.bytter.biz.wage.service.impl;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.payment.api.dto.NisWageDTO;
import com.bytter.biz.payment.api.dto.NisWageDetailDTO;
import com.bytter.biz.payment.api.service.INisWageService;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;

/**
 * @author qijh
 */
@Service("nisWageServiceImpl")
@Transactional
@DubboService
public class NisWageServiceImpl implements INisWageService {
    @Autowired
    private NisWageService nisWageService;
    @Autowired
    private NisWageDetailService nisWageDetailService;
    @Override
    public void saveNisWage(NisWageDTO entity, ICurrentUser user) {
        NisWageVO nisWageVO = new NisWageVO();
        BeanUtils.copyNotNullProperties(entity, nisWageVO);
        List<NisWageDetailVO> nisWageDetailVOList = BeanUtils.copyListProperties(entity.getNisWageDetailList(),NisWageDetailVO.class);
        nisWageVO.setNisWageDetailList(nisWageDetailVOList);
        nisWageService.saveNisWage(nisWageVO,user);
    }
    @Override
    public boolean isCheckWageTitle(String corpId, String wageTitle) {
        return nisWageService.isCheckWageTitle(corpId, wageTitle, null);
    }
    @Override
    public List<NisWageDTO> listErpTodoBill() {
        List<NisWageVO> listErp = nisWageService.listErpTodoBill();
        if (EmptyUtils.isEmptyList(listErp)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(listErp, NisWageDTO.class);
    }
    /**
     * 根据指令编号获取代发工资明细
     * @param voucherNo 指令编号
     * @param wageId 代发工资ID
     * @return
     */
    @Override
    public List<NisWageDetailDTO> listByVoucherNo(String voucherNo, String wageId) {
        List<NisWageDetailVO> list = nisWageDetailService.listByVoucherNo(voucherNo, wageId);
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, NisWageDetailDTO.class);
    }
    /**
     * 根据指令编号更新代发工资明细
     * @param voucherNo 指令编号
     * @param wageId 代发工资ID
     * @return
     */
    @Override
    public int updateVoucherStatByVoucherNo(String voucherNo, String wageId, String newVoucherNo) {
        return nisWageDetailService.updateVoucherStatByVoucherNo(voucherNo, wageId, newVoucherNo);
    }
    /**
     * 根据指令编号和代发工资ID获取等待结果的费用报销明细列表
     * @param voucherNo 付款指令编号
     * @param wageId 代发工资ID
     * @return 代发工资明细集合
     */
    @Override
    public List<NisWageDetailDTO> listByVoucherNoWaitResult(String voucherNo, String wageId) {
        List<NisWageDetailVO> list = nisWageDetailService.listByVoucherNoWaitResult(voucherNo, wageId);
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, NisWageDetailDTO.class);
    }
}
