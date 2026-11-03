package com.bytter.finance.saas.controller.basedata;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.basedata.entity.CmsProcessMsg;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: CMS_PROCESS_MSG
 * @date 2017-09-14 15:07:53
 */
@Api(value = "/v1-preposebill/cms/basedata/cmsprocessmsg",tags = "信贷业务处理消息表")
@RestController("v1CmsProcessMsgController")
@RequestMapping("/v1-preposebill/cms/basedata/cmsprocessmsg")
public class CmsProcessMsgController extends BaseController {

    @Autowired
    private CmsProcessMsgService cmsProcessMsgService;

    /**
     * 日志查询
     *
     * @param billCode
     * @return
     */
    @ApiOperation("日志查询")
    @GetMapping("/list/{billCode}")
    public AjaxJson<List<CmsProcessMsg>> list(@PathVariable("billCode") String billCode) {
        DataTablesInput input = new DataTablesInput();
        List<CmsProcessMsg> listNisMessage = cmsProcessMsgService.listMsgData(billCode, input).getData();
        listNisMessage = listNisMessage.stream()
                .sorted(Comparator.comparing(CmsProcessMsg::getCreateDate).reversed())
                .collect(Collectors.toList());
        return success(listNisMessage);
    }
    @ApiOperation("查询所有数据日志")
    @PostMapping("/listMsgData")
    @ResponseBody
    public AjaxJson<DataTablesOutput> listMsgData(@RequestBody V1DataTablesInput input, String billCode) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(cmsProcessMsgService.listMsgData(billCode, dataTablesInput));
    }
}
