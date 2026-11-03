package com.bytter.ras.controller;

import com.bytter.ras.service.RasReceivedService;
import com.bytter.ras.vo.RasReceivedClaimVO;
import com.bytter.ras.vo.RasReceivedDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.ras.api.service.IExcelCacheService;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核实收数据Controller
 * @date 2019/5/15 13:39
 */
@Controller
@RequestMapping("/ras/received")
public class RasReceivedController extends BaseController {

    @Autowired
    private RasReceivedService rasReceivedService;

	@Autowired(required=false)
    private IExcelCacheService excelCacheService;


    /**
     * 跳转实收数据查询页面
     *
     * @param model
     * @return
     */
    @GetMapping("/queryList")
    public String list(Model model) {
        return "ras/listReceivedQuery";
    }

    /**
     * 跳转实收数据认领查询页面
     *
     * @param model
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @GetMapping("/claimList")
    public String claimList(Model model) {
        return "ras/listReceivedClaim";
    }

    /**
     * 实收数据查询列表加载
     *
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "实收数据查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return rasReceivedService.getDataTablesUnion(dataTablesInput, RasConstant.GRANT_TYPE_QUERY, RasConstant.RAS_RESOURCE_RECEIVEDQUERY_OP);
    }


    /*****########实收数据导入块#########***********************/

    /**
     * 实收数据查询列表加载
     *
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "实收导入数据查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/importDatatable")
    @ResponseBody
    public DataTablesOutput importDatatable(@RequestBody DataTablesInput dataTablesInput) {
        dataTablesInput.getFormdata().put("ini_status", "0,1");
        return rasReceivedService.getDataTablesUnion(dataTablesInput, RasConstant.GRANT_TYPE_AUDIT, RasConstant.RAS_RESOURCE_RECEIVEDIMPORT_OP);
    }

    /**
     * 跳转实收数据导入查询页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDIMPORT_OP)
    public String importList(Model model) {
        return "ras/listReceivedImportDate";
    }


    /**
     * @param @param  list
     * @param @return 参数
     * @return AjaxJson    返回类型
     * @throws
     * @Title: saveImport
     * @Description: TODO(实收数据导入)
     */
    @LogMessage(content = "导入实收数据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestParam("cacheCode") String cacheCode, String datasource) {
    	List<RasReceivedDtlVO> list = excelCacheService.getListExcelDataCache(cacheCode, RasReceivedDtlVO.class);
        return rasReceivedService.saveImportRasReceivedDtl(list, this.getCurrentUser());
    }

    /**
     * @param @param  operate
     * @param @param  ids
     * @param @return 参数
     * @return AjaxJson    返回类型
     * @throws
     * @Title: batchStorage
     * @Description: TODO(实收作废)
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDIMPORT_OP)
    @LogMessage(content = "批量作废/单条作废", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHSUCCE)
    @PostMapping("/batchStorage/{operate}")
    @ResponseBody
    public AjaxJson batchStorage(@PathVariable("operate") String operate, String ids) {
        return rasReceivedService.batchOperation(ids, this.getCurrentUser());
    }

    /**
     * 实收数据认领查询列表加载
     *
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/claimDatatable")
    @ResponseBody
    public DataTablesOutput claimDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return rasReceivedService.getClaimListDataTablesUnion(dataTablesInput);
    }

    /**
     * 实收数据认领批量认领
     *
     * @param rids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领批量认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/claim")
    @ResponseBody
    public AjaxJson claim(String rids, String payType, String rasShopId) {
        AjaxJson ajaxJson = success();
        rasReceivedService.claim(rids, payType, rasShopId, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 实收数据认领自动匹配认领
     *
     * @param rids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领自动匹配认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/autoClaim")
    @ResponseBody
    public AjaxJson autoClaim(String rids) {
        AjaxJson ajaxJson = success();
        rasReceivedService.autoClaim(rids, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 实收数据认领根据查询条件自动匹配认领全部
     *
     * @param rasReceivedClaimVO
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领自动匹配认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/autoClaimAll")
    @ResponseBody
    public AjaxJson autoClaimAll(@RequestBody RasReceivedClaimVO rasReceivedClaimVO) {
        AjaxJson ajaxJson = success();
        rasReceivedClaimVO.setUser(this.getCurrentUser());
        ajaxJson.setSuccess(rasReceivedService.autoClaimAll(rasReceivedClaimVO));
        return ajaxJson;
    }


    /**
     * 实收数据认领批量剔除
     *
     * @param rids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领批量剔除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHDEL)
    @PostMapping("/exclude")
    @ResponseBody
    public AjaxJson exclude(String rids) {
        AjaxJson ajaxJson = success();
        rasReceivedService.exclude(rids, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 实收数据认领批量取消认领
     *
     * @param rids
     * @return
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP)
    @LogMessage(content = "实收数据认领批量取消认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/cancelClaim")
    @ResponseBody
    public AjaxJson cancelClaim(String rids) {
        AjaxJson ajaxJson = success();
        rasReceivedService.cancelClaim(rids, this.getCurrentUser());
        return ajaxJson;
    }

    /**
     * 判断传递日期是否有未认领的数据
     *
     * @param transDate 交易日期 yyyy-MM-dd HH:mm:ss
     * @return
     */
    @PostMapping("/isNotClaimNum")
    @ResponseBody
    public AjaxJson isNotClaimNum(String transDate, String shopId) {
        AjaxJson reInfo = success();
        reInfo.setSuccess(rasReceivedService.isNotClaimNum(transDate, shopId, this.getCurrentUser()));
        return reInfo;
    }
}
