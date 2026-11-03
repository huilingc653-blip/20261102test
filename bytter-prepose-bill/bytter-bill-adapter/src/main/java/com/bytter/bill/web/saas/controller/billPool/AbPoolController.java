package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.dto.AbPoolCheckBpCodeOrBpNameDTO;
import com.bytter.bill.domain.billPool.dto.AbPoolUpdateStatusDTO;
import com.bytter.bill.domain.billPool.service.AbPoolService;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 票据池
 */
@Api(value = "/v1-preposebill/billPool/pool", tags = "票据池")
@RestController("v1AbPoolController")
@RequestMapping({
        "/v1-preposebill/billPool/pool",
        "/v1-preposebill/billpool/pool"
})
public class AbPoolController extends BaseController {

    @Autowired
    private AbPoolService abPoolService;

    /**
     * 票据池table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询票据池")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolService.getDataTablesUnion(input));
    }

    /**
     * 保存票据池
     */
    @ApiOperation("保存票据池")
    @LogMessage(content = "保存票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolVO abPoolVO) {
        abPoolService.saveAbPool(abPoolVO, getCurrentUser());
        return success();
    }

    /**
     * 票据池编辑页面数据
     */
    @ApiOperation("票据池编辑页面数据")
    @GetMapping("/edit/{id}")
    public AjaxJson<AbPoolVO> edit(@PathVariable("id") String id) {
        return success(abPoolService.getAbPoolVO(id));
    }

    /**
     * 票据池查看页面数据
     */
    @ApiOperation("票据池查看页面数据")
    @GetMapping("/view/{id}")
    public AjaxJson<AbPoolVO> view(@PathVariable("id") String id) {
        return success(abPoolService.getAbPoolVO(id));
    }

    /**
     * 修改票据池
     */
    @ApiOperation("修改票据池")
    @LogMessage(content = "修改票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    public AjaxJson update(@RequestBody AbPoolVO abPoolVO) {
        abPoolService.updateAbPoolByAbPoolVO(abPoolVO, getCurrentUser());
        return success();
    }

    /**
     * 删除票据池
     */
    @ApiOperation("删除票据池")
    @LogMessage(content = "删除票据池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        abPoolService.batchDelete(ids, getCurrentUser());
        return success();
    }

    /**
     * 修改票据池状态
     */
    @ApiOperation("修改票据池状态")
    @LogMessage(content = "修改票据池状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateStatus")
    public AjaxJson updateStatus(@RequestBody AbPoolUpdateStatusDTO dto) {
        abPoolService.updateStatus(dto.getId(), dto.getStatus(), getCurrentUser());
        return success();
    }

    /**
     * 判断是否有相同的接口代码或接口名称
     */
    @ApiOperation("判断是否有相同的接口代码或接口名称")
    @PostMapping("/checkBpCodeOrBpName")
    public AjaxJson checkBpCodeOrBpName(@RequestBody AbPoolCheckBpCodeOrBpNameDTO dto) {
        AjaxJson j = success();
        j.setSuccess(abPoolService.checkBpCodeOrBpNameOrPifId(dto.getBpCode(), dto.getBpName(), dto.getId(), dto.getPifId()));
        return j;
    }

    /**
     * 获取维护的启用状态的票据池
     */
    @ApiOperation("获取维护的启用状态的票据池")
    @GetMapping("/getAllPool")
    public AjaxJson<List<AbPoolVO>> getAllPool(AbPoolVO abPoolVO) {
        return success(abPoolService.getAllPool(abPoolVO));
    }

    /**
     * 根据银行类型获取有效票据池（银行自动配置的银行有效且为票据池开票开启的银行）
     */
    @ApiOperation("根据银行类型获取有效票据池")
    @GetMapping("/json")
    public AjaxJson<List<AbPoolVO>> getAbPoolByBankAuto() {
        return success(abPoolService.getAbPoolByBankAuto());
    }
}
