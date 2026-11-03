package com.bytter.biz.finacial.accbook.service.impl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.service.FcAccbookDtlService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.api.dto.FcAccbookDtlDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
@Transactional
@DubboService
@Service("fcAccbookDtlServiceImpl")
public class FcAccbookDtlServiceImpl implements IFcAccbookDtlService {
    @Autowired
    private FcAccbookDtlService fcAccbookDtlService;
    @Override
    public List<FcAccbookDtlDTO> getPageDtlsByBookId(String bookId) {
        List<FcAccbookDtlVO> fcAccbookDtlVOS = fcAccbookDtlService.getPageDtlsByBookId(bookId);
        return BeanUtils.copyListProperties(fcAccbookDtlVOS, FcAccbookDtlDTO.class);
    }
    @Override
    public List<FcAccbookDtlDTO> getBookDtlsByBusGlobalId(String receiveBusGlobalId) {
        List<FcAccbookDtl> dtlList = fcAccbookDtlService.getBookDtlsByBusGlobalId(receiveBusGlobalId);
        return BeanUtils.copyListProperties(dtlList, FcAccbookDtlDTO.class);
    }

}
