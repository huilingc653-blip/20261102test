package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.entity.TallyFcUnionAccbook;
import com.bytter.tally.service.FcUnionAccbookService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 网点往来账务信息表
 * @date 2017-07-13 10:02:21
 */
@Controller
@RequestMapping("/finacial/bankCollect/fcunionaccbook")
public class FcUnionAccbookController extends BaseController {
    @Autowired
    private FcUnionAccbookService fcUnionAccbookService;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    /**
     * 网点往来账务信息表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions("fcunionaccbook:fcunionaccbook:index")
    @LogMessage(content = "查询网点往来账务信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, Model model) {
        model.addAttribute("opType", type);
        return "finacial/bankcollect/fcunionaccbook/listFcUnionAccbook";
    }
    /**
              * 网点往来账务信息表 table列表
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCUNIONACCBOOK_FCUNIONACCBOOK_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String type) {
        return fcUnionAccbookService.queryFcUnionAccbook(dataTablesInput, type, getUserId());
    }
    /**
     * 通过id查询FcUnionAccbook
     * 网点间来账-改账查询
     * @param type
     * @return
     */
    @PostMapping("/queryFcUnionAccbook")
    @ResponseBody
    public FcAccbookPageDTO queryFcUnionAccbook(String key, String type) {
        return fcUnionAccbookService.createAccbook(key, type, getCurrentUser());
    }
    /**
     * 提交FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    // //@RequiresPermissions("accbook:fcaccbook:save")
    @LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        TallyFcUnionAccbook entity = fcUnionAccbookService.queryFcUnionAccbook(key).get(0);
        //修改状态
        fcUnionAccbookService.submit(fcAccbook, entity, getUserId());
        //init时修改状态
        boolean isAutoCheck = fcUnionAccbookService.isAutoCheck();
        String status = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        String sign = "3";
        if (isAutoCheck) {
            status = NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue();
            sign = "1";
        }
        fcUnionAccbookService.changeStatus(entity, status, fcAccbook, getCurrentUser(), sign);
        return success();
    }
    @LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccBook, String key) {
        TallyFcUnionAccbook entity = fcUnionAccbookService.queryFcUnionAccbook(key).get(0);
        //修改状态
        fcUnionAccbookService.save(fcAccBook, entity, getUserId());
        fcUnionAccbookService.changeStatus(entity, NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue(), null, null, null);
        return success();
    }
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        TallyFcUnionAccbook entity = fcUnionAccbookService.queryFcUnionAccbook(key).get(0);
        //修改状态
        entity.setReturnMsg(fcAccbook.getBookExt().getInvalidReason());
        String opType = fcAccbook.getOpType().name();
        //记账时打回数据
        if ("SUBMIT".equals(opType)) {
            entity.setReceiveOrder(null);
            entity.setReceiveBusGlobalId(null);
            fcUnionAccbookService.changeStatus(entity, NbconsoleConstant.UnionAccbookBillState.Q.getValue(), null, null, null); //TODO 暂时设置为Q，以后需要时根据业务修改
            return success();
        }
        fcUnionAccbookService.backFcAccbook(fcAccbook, entity, opType, getUserId());
        //改账时打回数据
        if ("MODIFY".equals(opType)) {
            entity.setReceiveOrder(null);
            entity.setReceiveBusGlobalId(null);
            fcUnionAccbookService.changeStatus(entity, NbconsoleConstant.NbcEstate.E_STATE_BACK.getValue(), fcAccbook, getCurrentUser(), "6");
        } else if ("CHECK".equals(opType)) {//复核打回数据
            fcUnionAccbookService.changeStatus(entity, NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue(), fcAccbook, getCurrentUser(), "4");
        }
        return success();
    }
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/pass")
    @ResponseBody
    public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        TallyFcUnionAccbook entity = fcUnionAccbookService.queryFcUnionAccbook(key).get(0);
        //修改状态
        fcUnionAccbookService.checkFcAccbook(fcAccbook, entity, getUserId());
        fcUnionAccbookService.changeStatus(entity, NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue(), fcAccbook, getCurrentUser(), "1");
        return success();
    }
    /*查询有权限的内部账号*/
    @PostMapping("/getValidAccount")
    @ResponseBody
    public List<FcAccDTO> getValidAccount(String corpId) {
        return iInnerAccountService.listAccountByParamsAndGrant(null, null, corpId, null, null, getCurrentUser().getId());
    }
}
