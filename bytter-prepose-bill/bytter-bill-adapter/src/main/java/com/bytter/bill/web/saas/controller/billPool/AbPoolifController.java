package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.dto.AbPoolCheckPifCodeOrPifNameDTO;
import com.bytter.bill.domain.billPool.dto.AbPoolUpdateStatusDTO;
import com.bytter.bill.domain.billPool.service.AbPoolifService;
import com.bytter.bill.domain.billPool.vo.AbPoolifVO;
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
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池接口Controller
 * @Description: 票据池接口控制器类
 * @date 2019-12-30
 */
@Api(value = "/v1-preposebill/billPool/poolif", tags = "票据池接口")
@RestController("v1AbPoolifController")
@RequestMapping("/v1-preposebill/billPool/poolif")
public class AbPoolifController extends BaseController {
    @Autowired
    private AbPoolifService abPoolifService;

    /**
     * 票据池接口 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询票据池接口")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolifService.getDataTablesUnion(input));
    }

    /**
     * 保存票据池接口
     */
    @LogMessage(content = "保存票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存票据池接口")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolifVO abPoolifVO) {
        abPoolifService.saveAbPoolif(abPoolifVO, getCurrentUser());
        return success();
    }

    /**
     * 票据池接口编辑页面数据
     */
    @ApiOperation("票据池接口编辑页面数据")
    @GetMapping("/edit/{id}")
    public AjaxJson<AbPoolifVO> edit(@PathVariable("id") String id) {
        return success(abPoolifService.getAbPoolifVO(id));
    }

    /**
     * 修改票据池接口
     */
    @LogMessage(content = "修改票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改票据池接口")
    @PostMapping("/update")
    public AjaxJson update(@RequestBody AbPoolifVO abPoolifVO) {
        abPoolifService.updateAbPoolifByAbPoolifVO(abPoolifVO, getCurrentUser());
        return success();
    }

    /**
     * 删除票据池接口
     */
    @LogMessage(content = "删除票据池接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除票据池接口")
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        abPoolifService.batchDelete(ids, getCurrentUser());
        return success();
    }

    /**
     * 修改票据池接口状态
     */
    @LogMessage(content = "修改票据池接口状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("修改票据池接口状态")
    @PostMapping("/updateStatus")
    public AjaxJson updateStatus(@RequestBody AbPoolUpdateStatusDTO dto) {
        abPoolifService.updateStatus(dto.getId(), dto.getStatus(), getCurrentUser());
        return success();
    }

    /**
     * 判断是否有相同的接口代码、接口名称、银行接口代码
     */
    @ApiOperation("判断是否有相同的接口代码、接口名称、银行接口代码")
    @PostMapping("/checkPifCodeOrPifName")
    public AjaxJson checkPifCodeOrPifName(@RequestBody AbPoolCheckPifCodeOrPifNameDTO dto) {
        AjaxJson j = success();
        j.setSuccess(abPoolifService.checkPifCodeOrPifNameOrBifcode(dto.getPifCode(), dto.getPifName(), dto.getId(), dto.getBifCode()));
        return j;
    }

    /**
     * 获取维护的启用状态的票据池接口
     */
    @ApiOperation("获取维护的启用状态的票据池接口")
    @GetMapping("/getAllPoolif")
    public AjaxJson<List<AbPoolifVO>> getAllPoolif(AbPoolifVO abPoolifVO) {
        return success(abPoolifService.getAllPoolif(abPoolifVO));
    }
}
