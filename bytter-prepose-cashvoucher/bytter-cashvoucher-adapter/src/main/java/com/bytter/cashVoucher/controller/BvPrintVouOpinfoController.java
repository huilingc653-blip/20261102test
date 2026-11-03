package com.bytter.cashVoucher.controller;

import com.bytter.cashVoucher.service.BvPrintVouOpinfoService;
import com.bytter.cashVoucher.vo.BvPrintFormatVo;
import com.bytter.cashVoucher.vo.BvPrintVouOpinfoVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 凭证打印操作
 * @author onlineGenerator
 * @date 2017-06-13 11:18:30
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvprintvouopinfo")
public class BvPrintVouOpinfoController extends BaseController {
	@Autowired
	private BvPrintVouOpinfoService bvPrintVouOpinfoService;
	/**
	* 凭证打印操作列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model,String vouTid) {
		bvPrintVouOpinfoService.toListInit(model, vouTid);
        return "bill/blankvou/listBvPrintVouOpinfo";
	}
	
    /**
    * 凭证打印操作 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	@LogMessage(content = "查询凭证打印操作列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bvPrintVouOpinfoService.listBvPrintVouOpInfo(dataTablesInput);
    }
	
	/**
	* 凭证打印操作详情页
	*
	* @return
	*/
	@LogMessage(content = "查询凭证打印操作详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(String popId,String dataId,String vouTid, Model model) {
		bvPrintVouOpinfoService.toViewInit(model, popId,dataId,vouTid);
		return "bill/blankvou/viewBvPrintVouOpinfo";
	}
	
	/**
	 * 套打时获取匹配的套打规则模版
	 * @param bvPrintVouOpinfoVo
	 * @return
	 */
	@PostMapping("/loadPrintFormat")
	@ResponseBody
	public List<BvPrintFormatVo> loadPrintFormat(@RequestBody BvPrintVouOpinfoVo bvPrintVouOpinfoVo){
		return bvPrintVouOpinfoService.loadPrintFormat(bvPrintVouOpinfoVo,this.getCurrentUser());
	}
	
	/**
	 * 凭证打印进行占用凭证操作
	 * @param bvPrintVouOpinfoVo
	 * @return
	 */
	@LogMessage(content = "凭证打印占用凭证操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_PRINT)
	@PostMapping("/printOccupyVouNo")
	@ResponseBody
	public AjaxJson printOccupyVouNo(@RequestBody BvPrintVouOpinfoVo bvPrintVouOpinfoVo){
		bvPrintVouOpinfoService.printOccupyVouNo(bvPrintVouOpinfoVo,this.getCurrentUser());
		return success();
	}
	/**
	 * 套打格式设置预览功能
	 * @param bvPrintFormatVo
	 * @return
	 */
	@PostMapping("/printView")
	@ResponseBody
	public List<BvPrintFormatVo> printView(@RequestBody BvPrintFormatVo bvPrintFormatVo){
		return bvPrintVouOpinfoService.printView(bvPrintFormatVo,this.getCurrentUser());
	}
}
