package com.bytter.biz.inneraccount.controller;

import com.bytter.biz.inneraccount.service.FcFreezeService;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部账号冻结
 * @date 2017-06-16 17:31:26
 */
@Controller
@RequestMapping("/inneraccount/fcfreeze")
public class FcFreezeController extends BaseController {
    @Autowired
    private FcFreezeService fcFreezeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 内部账号冻结列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_QUERY)
    @LogMessage(content = "查询内部账号冻结解冻列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "inneraccount/listFcFreeze";
    }
    /**
     * 内部账号冻结复核列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_QUERY)
    @LogMessage(content = "查询内部账号冻结解冻列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/checkList")
    public String checkList(Model model) {
        return "inneraccount/checkListFcFreeze";
    }
    /**
     * 内部账号冻结 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcFreezeService.getDataTablesUnion(dataTablesInput, getCurrentUser());
    }
    /**
     * 内部账号冻结新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_APPLY)
    @GetMapping("/add")
    public String add(FcFreezeVO fcFreeze, Model model) {
        if (StringUtils.isNotEmpty(fcFreeze.getId())) {
            fcFreeze = fcFreezeService.getById(fcFreeze.getId());
            model.addAttribute("fcFreezePage", fcFreeze);
        }
        return "inneraccount/addFcFreeze";
    }
    /**
     * 内部账号冻结编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value={ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_APPLY,ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_QUERY},
//			logical = Logical.OR)
    @GetMapping("/updateOrView")
    public String updateOrView(FcFreezeVO fcFreeze, Model model) {
        if (StringUtils.isNotEmpty(fcFreeze.getId())) {
            FcFreezeListVO fcFreezeVO = fcFreezeService.getEntityById(fcFreeze);
            if (fcFreezeVO == null) {
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS, fcFreeze.getBlnctrAcc());
            }
            model.addAttribute("fcFreezePage", fcFreezeVO);
            model.addAttribute("netCode", corpService.get(fcFreezeVO.getNetId()).getCode());
            model.addAttribute("corpCode", corpService.get(fcFreezeVO.getCorpId()).getCode());
        }
        if (CommonConstant.SUBMIT_MODIFY.equals(fcFreeze.getOpType())) {
            return "inneraccount/updateFcFreeze";
        } else if (CommonConstant.SUBMIT_CHECK.equals(fcFreeze.getOpType())) {
            return "inneraccount/checkFcFreeze";
        } else {
            return "inneraccount/viewFcFreeze";
        }
    }
    /**
     * 添加内部账号冻结
     *
     * @param fcFreeze
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_APPLY)
    @LogMessage(content = "添加内部账号冻结解冻", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcFreezeVO fcFreeze) {
        if (CommonConstant.SUBMIT_CHECK.equals(fcFreeze.getOpType())) {
            fcFreezeService.checkEntity(fcFreeze, getCurrentUser());
        } else {
            fcFreezeService.saveEntity(fcFreeze, getCurrentUser());
        }
        return success();
    }
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_APPLY)
    @LogMessage(content = "内部账号冻结复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/check")
    @ResponseBody
    public AjaxJson check(@RequestBody FcFreezeVO fcFreeze) {
        fcFreezeService.checkEntity(fcFreeze, getCurrentUser());
        return success();
    }
    /**
     * 删除内部账号冻结
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_APPLY)
    @LogMessage(content = "删除内部账号冻结", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fcFreezeService.delete(id, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍 : 获取有效的内部账户。
     * 以后扩展为有权限的内部账户<br>
     * <b>日期：</b>2017年6月12日<br>
     */
    @GetMapping("/getValidAccountByCorpId/{corpId}")
    @ResponseBody
    public List<FcFreezeListVO> getValidAccountByCorpId(@PathVariable("corpId") String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            return fcFreezeService.getValidAccountByCorpId(corpId);
        }
        return null;
    }
    /**
     * 功能介绍 : 获取历史账号。<br>
     * <b>日期：</b>2017年6月12日<br>
     */
    @GetMapping("/getHisListByAccount/{account}")
    @ResponseBody
    public List<FcFreezeVO> getHisListByAccount(@PathVariable("account") String account) {
        if (StringUtils.isNotEmpty(account)) {
            return fcFreezeService.getHisListByAccount(account);
        }
        return null;
    }
    /**
     * 内部账号冻结解冻查询列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询内部账号冻结解冻信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listAll")
    public String listAll(Model model) {
        return "inneraccount/listAllFcFreeze";
    }
    /**
     * 内部账号冻结解冻查询
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_ALLQUERY)
    @PostMapping("/dataTableAll")
    @ResponseBody
    public DataTablesOutput dataTableAll(@RequestBody DataTablesInput dataTablesInput) {
        return fcFreezeService.getDataTableAll(dataTablesInput, getCurrentUser());
    }
    /**
     * 可冻冻记录列表
     *
     * @param request
     * @return
     */
    @PostMapping("/getTableData")
    @ResponseBody
    public List<FcFreezeListVO> getTableData(HttpServletRequest request) {
        String corpId = request.getParameter("corpId");
        if (StringUtils.isNotEmpty(corpId)) {
            return fcFreezeService.getValidAccountByCorpId(corpId);
        }
        return new ArrayList<>();
    }
}
