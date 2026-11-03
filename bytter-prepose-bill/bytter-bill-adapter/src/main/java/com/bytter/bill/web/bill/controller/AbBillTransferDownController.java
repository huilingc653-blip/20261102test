package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillTransferDownService;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownCorpDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownNetCollectVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 票据划拨公共控制器
 * @author onlineGenerator
 * @date 2017-10-31
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillTransferDown")
public class AbBillTransferDownController extends BaseController {
	@Autowired
	private AbBillTransferDownService abBillTransferDownService;
	/**
	* 票据下拨业务信息 页面跳转
	* @param model 数据传输对象
	* @return 跳转页面
	*/
	@GetMapping("/listQueryDtl")
	public String listQueryDtl(Model model) {
        return "bill/accebill/listAbBillTransferDownQueryDtl";
	}
    /**
	* 加载票据下拨业务信息数据
	* @param dataTablesInput 查询入参
    * @return 返回列表数据信息
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryDtltable")
    @ResponseBody
	@LogMessage(content = "查询票据下拨业务信息列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQueryDtlTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillTransferDownService.listAbBillQueryDtlInfo(dataTablesInput);
    }
	/**
	* 网点票据划拨汇总表 页面跳转
	* @param model 数据传输对象
	* @return 跳转页面
	*/
	@GetMapping("/toNetCollect")
	public String toNetCollect(Model model) {
        return "bill/accebill/listAbBillTransferDownNetCollect";
	}
    /**
	* 加载网点票据划拨汇总表信息数据
	* @param dataTablesInput 查询入参
    * @return 返回列表数据信息
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataNetCollectTable")
    @ResponseBody
	@LogMessage(content = "查询网点票据划拨汇总表信息数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataNetCollectTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillTransferDownService.listNetCollectInfo(dataTablesInput);
    }
	/**
	 * 网点票据划拨汇总表 app页面跳转
	 * @param model 数据传输对象
	 * @return 跳转页面
	 */
	@PostMapping("/toAppNetCollect")
	public String toAppNetCollect(Model model) {
		return "bill/accebill/mobile/mobileAbBillTransferDownNetCollect";
	}
	/**
	 * app页面 加载网点票据划拨汇总表信息数据
	 * @param netCollectVO 查询入参
	 * @return 返回列表数据信息
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataAppNetCollectTable")
	@ResponseBody
	public Object dataAppNetCollectTable(AbBillTransferDownNetCollectVO netCollectVO){
		return abBillTransferDownService.createNetCollectInfoSumBar(netCollectVO,this.getCurrentUser());
	}
	/**
	* 票据调拨明细表 页面跳转
	* @param model 数据传输对象
	* @return 跳转页面
	*/
	@GetMapping("/toCorpDtl")
	public String toCopDtl(Model model, AbBillTransferDownCorpDtlVO corpDtlVO) {
		model.addAttribute("userCorpId",this.getCurrentUser().getCorpId());
		model.addAttribute("queryParams",corpDtlVO);
        return "bill/accebill/listAbBillTransferDownCorpDtl";
	}
    /**
	* 加载票据调拨明细表信息数据
	* @param dataTablesInput 查询入参
    * @return 返回列表数据信息
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataCorpDtlTable")
    @ResponseBody
	@LogMessage(content = "查询票据调拨明细表信息数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataCorpDtlTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillTransferDownService.listCorpDtlInfo(dataTablesInput);
    }
}
