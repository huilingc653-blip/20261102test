package com.bytter.finance.saas.controller.provide;

import com.bytter.finance.domain.provide.service.CmsInterestWayService;
import com.bytter.finance.domain.provide.vo.CmsInterestWayVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 计息方式修改
 * @date 2017-08-09 16:40:48
 */
@Api(value = "/v1-preposebill/cms/provide/cmsinterestway", tags = "计息方式修改")
@RestController("v1CmsInterestWayController")
@RequestMapping("/v1-preposebill/cms/provide/cmsinterestway")
public class CmsInterestWayController extends BaseController {

    @Autowired
    private CmsInterestWayService cmsInterestWayService;

    /**
     * 根据id、sendCode查询计息方式详情
     */
    @GetMapping("/getInfo")
    public AjaxJson<Map<String, Object>> getInfo(String sendCode, String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInterestWayVO cmsInterestWayVO = cmsInterestWayService.getCmsInterestWay(id, sendCode);
        model.put("cmsInterestWayVO", cmsInterestWayVO);
        return AjaxJson.success(model);
    }

    /**
     * 计息方式 table分页查询
     */
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<CmsInterestWayVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String id) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsInterestWayService.listCmsInterestWay(input, id));
    }

    /**
     * 获取json数组
     */
    @GetMapping("/json")
    public AjaxJson<Object> json() {
        return AjaxJson.success(cmsInterestWayService.getJSONArray());
    }

    /**
     * 保存计息方式
     */
    @LogMessage(content = "添加CmsInterestWay", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsInterestWayVO cmsInterestWayVO) {
        cmsInterestWayService.save(cmsInterestWayVO, getCurrentUser());
        return AjaxJson.success();
    }
}