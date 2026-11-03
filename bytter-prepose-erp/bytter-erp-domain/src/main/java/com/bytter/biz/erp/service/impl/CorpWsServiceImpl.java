package com.bytter.biz.erp.service.impl;

import com.bytter.biz.erp.base.AbstractCxfService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;

import javax.jws.WebService;
import java.util.List;

/**
 * @author zhoul
 */
@WebService(serviceName = "corpWs"//服务名,自己定义
//        targetNamespace = "http://component.erp.biz.bytter.com/",//同接口类的命名空间
//        endpointInterface = "com.bytter.erp.service.CorpWsService"
        )//接口类相对路径
public class CorpWsServiceImpl extends AbstractCxfService implements com.bytter.erp.service.CorpWsService {
    @DubboReference(check = false)
    private ICorpService corpService;

    @Override
    public String save(String params) {
        List<CorpDTO> corpDTOList = JsonUtils.json2List(params, CorpDTO.class);
        if (EmptyUtils.isEmpty(corpDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        corpService.save(corpDTOList);
        return success();
    }

    @Override
    public String update(String params) {
        List<CorpDTO> corpDTOList = JsonUtils.json2List(params, CorpDTO.class);
        if (EmptyUtils.isEmpty(corpDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        corpService.update(corpDTOList);
        return success();
    }

    @Override
    public String delete(String params) {
        List<CorpDTO> corpDTOList = JsonUtils.json2List(params, CorpDTO.class);
        if (EmptyUtils.isEmpty(corpDTOList)) {
            throw new RuntimeException("报文体非json格式或格式不正确!");
        }
        for (CorpDTO corpDTO : corpDTOList) {
            if(EmptyUtils.isEmpty(corpDTO.getCode()))
                throw new RuntimeException("单位编码不能为空!");
        }
        corpService.delete(corpDTOList);
        return success();
    }

    @Override
    public String getCorpByCode(String params){
        CorpDTO corpDTO = JsonUtils.parseObject(params, CorpDTO.class);
        if (EmptyUtils.isEmpty(corpDTO.getCode())) {
            throw new RuntimeException("单位编码不能为空!");
        }
        CorpDTO dto = corpService.getCorpByCode(corpDTO.getCode());
        if(dto!=null) return success(dto);
        else return failure(String.format("不存在单位编码为[%s]的数据!", corpDTO.getCode()));
    }

}
