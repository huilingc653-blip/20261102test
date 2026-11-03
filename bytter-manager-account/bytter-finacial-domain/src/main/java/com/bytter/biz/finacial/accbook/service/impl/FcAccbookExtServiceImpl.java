package com.bytter.biz.finacial.accbook.service.impl;
import com.bytter.biz.finacial.accbook.service.FcAccbookExtService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookExtVO;
import com.bytter.biz.finacial.api.dto.FcAccbookExtDTO;
import com.bytter.biz.finacial.api.service.IFcAccbookExtService;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author YonJar
 * @date 2023/4/23.
 * @description
 */
@Service("fcAccbookExtServiceImpl")
@DubboService
public class FcAccbookExtServiceImpl implements IFcAccbookExtService {
    @Autowired
    private FcAccbookExtService fcAccbookExtService;
    @Override
    public FcAccbookExtDTO getBookExtVOByBookId(String bookId) {
        FcAccbookExtVO fcAccbookExtVO = fcAccbookExtService.getBookExtVOByBookId(bookId);
        FcAccbookExtDTO fcAccbookExtDTO = new FcAccbookExtDTO();
        BeanUtils.copyProperties(fcAccbookExtVO,fcAccbookExtDTO);
        return fcAccbookExtDTO;
    }
}
