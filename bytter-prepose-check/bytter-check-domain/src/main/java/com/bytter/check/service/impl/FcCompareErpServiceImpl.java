package com.bytter.check.service.impl;
import com.bytter.check.api.IFcCompareErpService;
import com.bytter.check.dto.FcCompareAccDTO;
import com.bytter.check.service.FcCompareErpService;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("fcCompareErpServiceImpl")
@DubboService
@Transactional
public class FcCompareErpServiceImpl implements IFcCompareErpService {
    @Autowired
    private FcCompareErpService fcCompareErpService;
    /**
     * 根据开始时间，结束时间查询对账信息
     * @param tallyStartDate
     * @param tallyEndDate
     * @return
     */
    @Override
    public List<FcCompareAccDTO> listForEBill(Date tallyStartDate, Date tallyEndDate) {
        List<FcCompareAccDTO> list = new ArrayList<>();
        List<FcCompareAccVO> fcCompareAccVOList = fcCompareErpService.listForEBill(tallyStartDate,tallyEndDate);
        if(EmptyUtils.isNotEmpty(fcCompareAccVOList)){
            list = BeanUtils.copyListProperties(fcCompareAccVOList, FcCompareAccDTO.class);
        }
        return list;
    }
}
