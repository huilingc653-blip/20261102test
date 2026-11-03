package com.bytter.biz.finacial.paramconfig.controller;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.paramconfig.entity.FcReportFormat;
import com.bytter.biz.finacial.paramconfig.service.FcReportFormatService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcReportFormatVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
/**   
 * @Title: Controller  
 * @Description: 格式定义表
 * @author onlineGenerator
 * @date 2017-08-30 17:58:29
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/paramconfig/fcreportformat")
public class FcReportFormatController extends BaseController {
	@Autowired
	private FcReportFormatService fcReportFormatService;
	/**
	* 格式定义表列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCREPORTFORMAT_APPLY)
	@LogMessage(content = "查询格式定义表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		//model.addAttribute("netCode", "402880425b8abcfb015b8ac16e980000");
		model.addAttribute("reportType", 1);
        //return "finacial/paramconfig/reportformat/" + getForwardByType(type);
		return "finacial/paramconfig/reportformat/addFcReportFormat";
	}
	/**
	 * 添加格式定义表
	 * @param fcReportFormat
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCREPORTFORMAT_APPLY)
	@LogMessage(content = "添加格式定义表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcReportFormat fcReportFormat){
		fcReportFormatService.save(fcReportFormat);
		return success();
	}
	@PostMapping("/getReportFormat")
	@ResponseBody
	public List<FcReportFormat> getReportFormat(String netCode,String reportType) {
		if("".equals(netCode) || "".equals(reportType)){
			return new ArrayList<>();
		}
		List<FcReportFormat> list  = fcReportFormatService.getReportFormat(netCode,reportType);
		return EmptyUtils.isEmptyList(list) ? new ArrayList<FcReportFormat>() : list;
	}
	
	/**
	 * 监听提交
	 */
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request, FcReportFormatVO format) {
		fcReportFormatService.submit(format);
		return success();
	}
	
	/**
	 * 根据类型返回不同的跳转页面
	 * @param type
	 * @return
	 */
	public String getForwardByType(String type){
		/**0tab页 */
		String forward = "";
		if("0".equals(type)){
			forward = "listFcFormatTab";
		}else if("2".equals(type)){
			forward = "addFcReportFormate";
		}else{
			forward = "addFcReportFormat";
		}
        return forward;
	}
	
	/**
	* 业务种类批量修改详情页
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCREPORTFORMAT_APPLY)
	@LogMessage(content = "查询业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/copy")
	public String copy(FcReportFormatVO format, Model model) {
		//FcBusRace fcBusRace = fcBusRaceService.get(ids);
		//fcReportFormatService.check(format);
		model.addAttribute("format", format);
		return "finacial/paramconfig/reportformat/copyFormat";
	}
	
	/**
	 * 监听提交
	 */
	@PostMapping("/copySubmit")
	@ResponseBody
	public AjaxJson copySubmit(HttpServletRequest request, FcReportFormatVO format) {
		fcReportFormatService.copySubmit(format);
		return success();
	}
}
