package com.bytter.biz.inneraccount.service.impl;
import com.bytter.biz.inneraccount.service.FcInitAccService;
import com.bytter.biz.payment.api.dto.FcAccInitCheckBeanDTO;
import com.bytter.biz.payment.api.dto.FcDayCloseDTO;
import com.bytter.biz.payment.api.dto.FcInitAccDTO;
import com.bytter.biz.payment.api.dto.FcInitSubjectDTO;
import com.bytter.biz.payment.api.service.IFcInitAccService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @author YonJar
 * @date 2023/4/27.
 * @description
 */
@DubboService
@Service("fcInitAccServiceImpl")
public class FcInitAccServiceImpl implements IFcInitAccService {
    @Autowired
    private FcInitAccService fcInitAccService;
    @Override
    public List<FcAccInitCheckBeanDTO> findNoCheckAccInit(FcDayCloseDTO dayClose) {
        return fcInitAccService.findNoCheckAccInit(dayClose);
    }
    @Override
    public List<FcInitSubjectDTO> getInitSubjectDTO(String netId) {
        return fcInitAccService.getInitSubjectDTO(netId);
    }

    @Override
    public List<FcInitAccDTO> queryInitAccByNetId(String netId) {
        return fcInitAccService.queryInitAccByNetId(netId);
    }
}
