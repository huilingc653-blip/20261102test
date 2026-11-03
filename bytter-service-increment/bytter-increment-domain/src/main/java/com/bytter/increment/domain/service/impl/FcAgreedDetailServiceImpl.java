package com.bytter.increment.domain.service.impl;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.dto.FcAgreeDepositDetailDTO;
import com.bytter.increment.api.service.IFcAgreedDetailService;
import com.bytter.increment.domain.entity.FcAgreedDetail;
import com.bytter.increment.domain.service.FcAgreedDetailService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * 协定存款明细对外接口实现类
 * @author liyh
 *
 */
@Service("fcAgreedDetailServiceImpl")
@Transactional
@DubboService
public class FcAgreedDetailServiceImpl implements IFcAgreedDetailService {
    @Autowired
    private FcAgreedDetailService fcAgreedDetailService;
    /**
     * 功能介绍 : 根据协定存款id查找协定明细。<br>
     * <b>日期：</b>2019年5月21日<br>
     *
     * @param agreedId 协定主表id
     * @version 1.0
     * @author wangzx
     */
    @Override
    public List<FcAgreeDepositDetailDTO> getFcAgreedDetailById(String agreedId) {
        List<FcAgreedDetail> details = fcAgreedDetailService.getDetailList(agreedId);
        if (EmptyUtils.isEmptyList(details)) {
            return null;
        }
        return BeanUtils.copyListProperties(details,FcAgreeDepositDetailDTO.class);
    }
    /**
     * 功能介绍 : 根据协定存款id和行号查找协定明细。<br>
     * <b>日期：</b>2019年5月21日<br>
     *
     * @param agreedId 协定主表id
     * @param lineNo 行号
     * @version 1.0
     * @author wangzx
     */
    @Override
    public FcAgreeDepositDetailDTO getDetailByAgreedIdLineNo(String agreedId, Integer lineNo) {
        FcAgreedDetail detail = fcAgreedDetailService.getDetailByAgreedIdLineNo(agreedId,lineNo);
        FcAgreeDepositDetailDTO dto = null;
        if(!EmptyUtils.isEmpty(detail)){
            dto = new FcAgreeDepositDetailDTO();
            BeanUtils.copyNotNullProperties(detail,dto);
        }
        return dto;
    }
    /**
     * <br>
     * <b>功能：</b> 获取账户协定明细 <br>
     * <b>作者：</b>liyh<br>
     * @param account 账号
     * @param balanceDate 余额时间
     * @param isAdjust 是否调整 true 是 false 否
     * <b>日期：</b> 2019/5/14 <br>
     */
    @Override
    public List<FcAgreeDepositDetailDTO> getFcAgreeDepositDetail(String account, Date balanceDate, boolean isAdjust) {
        return fcAgreedDetailService.getFcAgreeDepositDetail(account, balanceDate, isAdjust);
    }
}
