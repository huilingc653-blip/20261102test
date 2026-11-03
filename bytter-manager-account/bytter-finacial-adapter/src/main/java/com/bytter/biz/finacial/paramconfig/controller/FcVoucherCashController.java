package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.paramconfig.entity.FcVoucherCash;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author
 * @version V1.0
 * @Title: dao
 * @Description: 现金流量项目controller
 * @date 2020-09-10 09:30:39
 */
@Controller
@RequestMapping("/finacial/paramconfig/voucherCash")
public class FcVoucherCashController extends BaseController {
    @Autowired
    private FcVoucherCashService voucherCashService;
    @DubboReference(check = false)
	private Ii18nService ii18nService;
    @DubboReference(check = false)
	private IFcAccService fcAccService;
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @LogMessage(content = "查询现金流量科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/paramconfig/voucherCash/listVoucherCash";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return voucherCashService.listVoucherCash(dataTablesInput);
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @LogMessage(content = "查询现金流量科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        return "finacial/paramconfig/voucherCash/addVoucherCash";
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcVoucherCash voucherCash = voucherCashService.get(id, true);
            model.addAttribute("voucherCash", voucherCash);
        }
        return "finacial/paramconfig/voucherCash/updateVoucherCash";
    }
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(FcVoucherCash voucherCash, Model model) {
        voucherCashService.submitVoucherCash(voucherCash, getCurrentUser());
        return success();
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String voucherCashId, Model model) {
        voucherCashService.deleteVoucherCash(voucherCashId, getCurrentUser());
        return success();
    }
    //@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_VOUCHERCASH_APPLY)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcVoucherCashVo fcVoucherCashVO = voucherCashService.getById(id);
            model.addAttribute("voucherCash", fcVoucherCashVO);
        }
        return "finacial/paramconfig/voucherCash/viewVoucherCash";
    }
    /**
     *
     * @param fcAcc
     * @param debit
     * @param credit
     * @return
     */
    @PostMapping("/getFcVoucherCash")
    @ResponseBody
    public AjaxJson getFcVoucherCash(String fcAcc,
                                              @RequestParam(required = false) String debit,
                                              @RequestParam(required = false) String credit,
                                              @RequestParam(required = false) String subjectId) {
        AjaxJson json = new AjaxJson();
        FcAccDTO fcAccDTO = fcAccService.getAccDTOByAccount(fcAcc);
        FcVoucherCashVo fcVoucherCashVo = null;
        if (null == fcAccDTO) {
            if (StringUtils.isEmpty(subjectId)) {
                json.setSuccess(false);
                return json;
            } else {
                fcAccDTO = new FcAccDTO();
                fcAccDTO.setSubjectId(subjectId);
            }
        }
        if (StringUtils.isNotEmpty(debit)) {
            fcVoucherCashVo = voucherCashService.getItemBySubject(fcAccDTO.getSubjectId(), null);
        } else if (StringUtils.isNotEmpty(credit)){
            fcVoucherCashVo = voucherCashService.getItemBySubject(null, fcAccDTO.getSubjectId());
        }
        /*if (null == fcVoucherCashVo) {
            json.setSuccess(false);
        } else {
            json.setSuccess(true);
            json.setObj(fcVoucherCashVo);
        }*/
        json.setSuccess(true);
        json.setObj(fcVoucherCashVo);
        return json;
    }
}
