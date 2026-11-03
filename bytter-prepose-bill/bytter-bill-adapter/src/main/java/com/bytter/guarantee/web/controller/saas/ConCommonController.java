package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.domain.service.ConCorpService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Title: 合同公共功能控制类
 * @Package com.bytter.biz.contract.controller
 * @author: zhengg
 * @date: 2019-11-6 下午2:19:43
 */
@Api(value = "/v1-guarantee/contract/conCommon", tags = "合同公共接口")
@RestController("v1ConCommonController")
@RequestMapping("/v1-guarantee/contract/conCommon")
public class ConCommonController extends BaseController {
    @Autowired
    private ConCommonService conCommonService;
    @Autowired
    private ConCorpService conCorpService;

    /**
     * 选择合同 获取合同 table列表
     */
    @ApiOperation("选择合同列表数据查询")
    @PostMapping("/conSelectData")
    public AjaxJson<DataTablesOutput> conSelectData(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = conCommonService.getConSelectData(input);
        return AjaxJson.success(output);
    }

    /**
     * 查看合同详情
     */
    @ApiOperation("获取合同基础详情")
    @GetMapping("/conView")
    public AjaxJson<Map<String, Object>> conView(ConInfoVO params) {
        Map<String, Object> model = new HashMap<>();
        ConInfoDTO conInfo = conCommonService.getConInfo(params);
        model.put("conInfo", conInfo);
        return AjaxJson.success(model);
    }
}