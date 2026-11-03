package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.guarantee.domain.entity.ConInfoBusDtl;
import com.bytter.guarantee.domain.service.ConInfoBusdtlService;
import com.bytter.guarantee.domain.vo.ConInfoBusdtlVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @Title: 合同字段明细控制类
 * @Description: 合同字段明细控制类
 * @author: zhenghj
 * @date: 2019-11-10
 */
@Api(value = "/v1-guarantee/contract/conInfoBusdtl", tags = "合同字段明细")
@RestController("v1ConInfoBusdtlController")
@RequestMapping("/v1-guarantee/contract/conInfoBusdtl")
public class ConInfoBusdtlController extends BaseController {
    @Autowired
    private ConInfoBusdtlService conInfoBusdtlService;

    /**
     * 根据mid获取合同业务明细
     */
    @ApiOperation("根据mid获取合同业务明细")
    @PostMapping("/getConInfoBusdtlByMid")
    public AjaxJson<List<ConInfoBusdtlVO>> getConInfoBusdtlByMid(String mid) {
        List<ConInfoBusdtlVO> busdtlList = null;
        List<ConInfoBusDtl> conBusdtlList = conInfoBusdtlService.getConInfoBusDtl(new ConInfoBusdtlVO(mid));
        if (ListUtils.isNotNullOrEmpty(conBusdtlList)) {
            busdtlList = BeanUtils.copyListProperties(conBusdtlList, ConInfoBusdtlVO.class);
        }
        return AjaxJson.success(busdtlList);
    }
}