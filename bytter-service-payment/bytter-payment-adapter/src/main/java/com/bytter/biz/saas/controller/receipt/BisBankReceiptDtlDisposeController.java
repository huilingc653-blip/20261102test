package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlDisposeService;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@Api(value = "/v1-payment/receipt/bisbankreceiptdtldispose", tags = "电子回单关联配置")
@RestController("v1BisBankReceiptDtlDisposeController")
@RequestMapping("/v1-payment/receipt/bisbankreceiptdtldispose")
public class BisBankReceiptDtlDisposeController extends BaseController {
    @DubboReference
    private IBisBankReceiptDtlDisposeService bisBankReceiptDtlDisposeService;

    /*
    数据的获取
     */
    @ApiOperation("数据的获取")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisBankReceiptDtlDisposeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<BisBankReceiptDtlDisposeVO> dataTablesOutput = bisBankReceiptDtlDisposeService.getDataTablesUnion(input);
        return AjaxJson.success(dataTablesOutput);
    }

    //根据id查询信息
    @ApiOperation("根据id查询信息")
    @GetMapping("/view/{id}")
    public AjaxJson<BisBankReceiptDtlDisposeVO> view(@PathVariable("id") String id) {
        BisBankReceiptDtlDisposeVO bisBankReceiptDtlDisposeVO = bisBankReceiptDtlDisposeService.EntityById(id);
        return AjaxJson.success(bisBankReceiptDtlDisposeVO);
    }


    //编辑更新数据页面跳转
    @ApiOperation("编辑更新数据页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<BisBankReceiptDtlDisposeVO> updatepage(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            return AjaxJson.success(bisBankReceiptDtlDisposeService.EntityById(id));
        }
        return AjaxJson.success();
    }

    @ApiOperation("增加数据信息")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        if (bisBankReceiptDtlDisposeService.bankTypeDate(bisBankReceiptDtlDispose.getBankTypeId())) {
            AjaxJson ajaxJson = new AjaxJson();
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("此银行字段信息已经配置过");
            return ajaxJson;
        } else {
            bisBankReceiptDtlDisposeService.save(bisBankReceiptDtlDispose, getCurrentUser());
            return success();
        }
    }

    //删除数据
    @ApiOperation("删除数据")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable String id) {
        BisBankReceiptDtlDispose bisBankReceiptDtlDispose = bisBankReceiptDtlDisposeService.get(id);
        bisBankReceiptDtlDisposeService.deleteEntity(bisBankReceiptDtlDispose);
        return success();
    }

    //编辑更新数据信息
    @ApiOperation("编辑更新数据信息")
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        bisBankReceiptDtlDisposeService.update(bisBankReceiptDtlDispose, getCurrentUser());
        return success();
    }

    //禁用启用
    @ApiOperation("禁用启用")
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(String id, String isenable) {
        BisBankReceiptDtlDispose bisBankReceiptDtlDispose = bisBankReceiptDtlDisposeService.get(id);
        bisBankReceiptDtlDispose.setIsEnable(isenable);
        bisBankReceiptDtlDisposeService.updateEntity(bisBankReceiptDtlDispose);
        return success();
    }
}
