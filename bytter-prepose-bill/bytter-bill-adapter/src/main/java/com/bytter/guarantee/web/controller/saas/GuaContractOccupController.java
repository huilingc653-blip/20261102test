package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.domain.service.GuaContractOccupService;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author luxy
 * @Title: GuaContractOccupController
 * @ProjectName T2_IDEA
 * @Description: 担保合同占用控制层
 * @date 2019/4/30 19:47
 */
@Api(tags = "担保合同占用控制层")
@RestController("v1GuaContractOccupController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaContractOccup")
public class GuaContractOccupController extends BaseController {

	@Autowired
    private GuaContractOccupService guaContractOccupService;


    @ApiOperation(value = "获取担保合同占用列表")
    @PostMapping("/getUseContractList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> getUseContractList(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(guaContractOccupService.getUseContractList(tablesInput, getCurrentUser()));
    }

    @ResponseBody
    @ApiOperation(value = "获取担保合同挂接占用列表")
    @GetMapping("/getGuaContractOccupyDetail")
    public AjaxJson<GuaContractUseDTO> getGuaContractOccupyDetail(String businessId, String itemNo){
        GuaContractUseDTO occupyDtlDTO = guaContractOccupService.getGuaContractOccupyDtlDTO(businessId, itemNo);
        return success(occupyDtlDTO);
    }
}
