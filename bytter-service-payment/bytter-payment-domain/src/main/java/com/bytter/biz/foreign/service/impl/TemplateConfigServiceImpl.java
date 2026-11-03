package com.bytter.biz.foreign.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigDetailVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigParamVO;
import com.bytter.biz.payment.api.dto.FexTranTemplateConfigDetailDTO;
import com.bytter.biz.payment.api.service.ITemplateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("templateConfigServiceImpl")
@Transactional
public class TemplateConfigServiceImpl implements ITemplateConfigService {


    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;

    /**
     * 根据银行类型ID和支付类型is查询配置模板
     * @param bankPayTypeIds
     * @param bankTypeIds
     * @return
     */
    public  List<FexTranTemplateConfigDetailDTO> listOfChildrenByBankPayIds(List<String> bankPayTypeIds,List<String> bankTypeIds)  {
        FexTranTemplateConfigParamVO vo=new FexTranTemplateConfigParamVO();
        vo.setBankTypeIds(bankTypeIds);
        vo.setPayTypeIds(bankPayTypeIds);
        List<FexTranTemplateConfigDetailVO> list = fexTranConfigTemplateService.listOfChildren(vo);
        if (!list.isEmpty()) {
            return BeanUtil.copyToList(list, FexTranTemplateConfigDetailDTO.class);
        }
        return new ArrayList<>();
    }
}
