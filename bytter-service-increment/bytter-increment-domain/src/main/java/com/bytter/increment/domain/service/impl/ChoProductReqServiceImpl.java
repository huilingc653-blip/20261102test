package com.bytter.increment.domain.service.impl;

import com.bytter.increment.api.dto.ChoProductReqDTO;
import com.bytter.increment.api.service.IChoProductReqService;
import com.bytter.increment.domain.service.ChoProductReqService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: 理财产品接口实现
 * @Description: CHO_PRODUCT_REQ
 * @date 2024-05-15
 */
@Service("choProductReqServiceImpl")
@Transactional
@DubboService
public class ChoProductReqServiceImpl implements IChoProductReqService {

    @Autowired
    private ChoProductReqService choProductReqService;

    @Override
    public List<ChoProductReqDTO> listChoProductReqByDate(String corpId,Date deadlineDate) {
        return choProductReqService.listChoProductReqByDate(corpId,deadlineDate);
    }
}
