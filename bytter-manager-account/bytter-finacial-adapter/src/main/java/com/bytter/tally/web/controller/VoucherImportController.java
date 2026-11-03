package com.bytter.tally.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.tally.service.VoucherImportService;
import com.bytter.tally.vo.FcAccbookImportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 凭证导入
 * @author zhengxn
 * @date 2019-3-5
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/bankcollect2/voucherimport")
public class VoucherImportController extends BaseController{
	@Autowired
	private VoucherImportService voucherImportService;
	
	
	/**
	 * 功能介绍 :凭证导入页面 <br>
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_VOUCHERIMPORT_APPLY)
	@LogMessage(content = "凭证导入页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/improtData")
	public String improtData(Model model) {
        return "finacial/bankcollect/voucherImportList";
	}
	
	
	/**凭证导入列表数据**/
	@PostMapping("/voucherImpDatatable")
	@ResponseBody
	public DataTablesOutput voucherImpDatatable(@RequestBody  DataTablesInput dataTablesInput){
		return voucherImportService.voucherImpDatatable(dataTablesInput,getCurrentUser().getId());
	}
	
	/**
	 * 功能介绍 : 导入文件<br>
	 */
	@LogMessage(content = "凭证数据导入",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/impDataSave")
	@ResponseBody
	public AjaxJson impDataSave(@RequestBody List<FcAccbookImportVO> list){
		voucherImportService.impDataSave(list,getCurrentUser());
		return success();
	}
	/**
	 * 功能介绍 : 导入提交<br>
	 */
	@LogMessage(content = "凭证提交",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(@RequestParam("ids") String ids){
		String[] id = ids.split(",");
		List<String> idss = new ArrayList<String>();
		if(id != null && id.length > 0){
			for(int i = 0; i < id.length; i++){
				idss.add(id[i]);
			}
		}
		voucherImportService.submit(idss);
		return success();
	}
	/**
	 * 功能介绍 : 导入删除<br>
	 */
	@LogMessage(content = "凭证导入删除",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/deletevoucher")
	@ResponseBody
	public AjaxJson deleteVoucher(@RequestParam("ids") String ids){
		String[] id = ids.split(",");
		List<String> idss = new ArrayList<String>();
		if(id != null && id.length > 0){
			for(int i = 0; i < id.length; i++){
				idss.add(id[i]);
			}
		}
		voucherImportService.batchDelete(idss);
		return success();
	}
	
}
