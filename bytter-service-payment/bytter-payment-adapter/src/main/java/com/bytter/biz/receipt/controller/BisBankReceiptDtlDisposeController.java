package com.bytter.biz.receipt.controller;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlDisposeService;
import com.bytter.biz.bdlink.entity.BisBankReceiptDtlDispose;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlDisposeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/receipt/bisbankreceiptdtldispose")
public class BisBankReceiptDtlDisposeController extends BaseController {
    @DubboReference
    private IBisBankReceiptDtlDisposeService bisBankReceiptDtlDisposeService;
    /*
    电子回单关联配置页面跳转
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:query")
    @GetMapping("/list")
    public String list(Model model) {
        return "receipt/bankbill/listBisBankReceiptDtlDispose";
    }
    /*
    数据的获取
     */
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:query")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = bisBankReceiptDtlDisposeService.getDataTablesUnion(dataTablesInput);
        return dataTablesOutput;
    }
    //根据id查询信息
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:query")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisBankReceiptDtlDisposeVO bisBankReceiptDtlDisposeVO = bisBankReceiptDtlDisposeService.EntityById(id);
        model.addAttribute("bisBankReceiptDtlDisposeVO", bisBankReceiptDtlDisposeVO);
        return "receipt/bankbill/viewBisBankReceiptDtlDispose";
    }
    //增加数据页面
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @GetMapping("/savepage")
    public String savepage(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, Model model) {
        if (StringUtils.isNotEmpty(bisBankReceiptDtlDispose.getId())) {
            bisBankReceiptDtlDispose = bisBankReceiptDtlDisposeService.get(bisBankReceiptDtlDispose.getId());
            model.addAttribute("bisBankReceiptDtlDisposePage", bisBankReceiptDtlDispose);
        }
        return "receipt/bankbill/addBisBankReceiptDtlDispose";
    }
    //增加数据信息
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
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
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable String id) {
        BisBankReceiptDtlDispose bisBankReceiptDtlDispose = bisBankReceiptDtlDisposeService.get(id);
        bisBankReceiptDtlDisposeService.deleteEntity(bisBankReceiptDtlDispose);
        return success();
    }
    //编辑更新数据页面跳转
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @GetMapping("/updatepage")
    public String updatepage(BisBankReceiptDtlDispose bisBankReceiptDtlDispose, Model model) {
        if (StringUtils.isNotEmpty(bisBankReceiptDtlDispose.getId())) {
            BisBankReceiptDtlDisposeVO bisBankReceiptDtlDisposeVO = bisBankReceiptDtlDisposeService.EntityById(bisBankReceiptDtlDispose.getId());
            model.addAttribute("bisBankReceiptDtlDisposePage", bisBankReceiptDtlDisposeVO);
        }
        return "receipt/bankbill/updateBisBankReceiptDtlDispose";
    }
    //编辑更新数据信息
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(BisBankReceiptDtlDispose bisBankReceiptDtlDispose) {
        bisBankReceiptDtlDisposeService.update(bisBankReceiptDtlDispose, getCurrentUser());
        return success();
    }
    //禁用启用
    //@RequiresPermissions("netbank:receipt:bisbankreceiptdtldispose:apply")
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(String id, String isenable) {
        BisBankReceiptDtlDispose bisBankReceiptDtlDispose= bisBankReceiptDtlDisposeService.get(id);
        bisBankReceiptDtlDispose.setIsEnable(isenable);
        bisBankReceiptDtlDisposeService.updateEntity(bisBankReceiptDtlDispose);
        return success();
    }
}
