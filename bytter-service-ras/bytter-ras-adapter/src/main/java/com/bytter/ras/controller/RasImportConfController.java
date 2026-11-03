package com.bytter.ras.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bytter.ras.service.RasImportConfService;
import com.bytter.ras.service.RasLogService;
import com.bytter.ras.vo.RasImportConfVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
/**
 * 
* @Title:  营收/实收数据导入配置
* @Package com.bytter.ras.controller   
* @Description:    TODO   
* @author: Jinyong.cao   
* @date:   2019-5-15 下午6:55:44   
* @version V1.0
 */
@Controller
@RequestMapping("/ras/importConf")
public class RasImportConfController extends BaseController {

	@Autowired
	private RasLogService rasLogService;	
	
	@Autowired
	private RasImportConfService importConfService;	
	
	/**
	 * 
	* @Title: list  
	* @Description: TODO(跳转 营收数据导入配置VM/获取下拉配置)  
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_REVENUEIMPORTCONF_OP)
	@GetMapping("/revenueConf")
	public String list(String fileType,String startLine,Model model){
		model.addAttribute("fileType",fileType);//文件类型
		model.addAttribute("startLine",startLine);//开始行
		RasImportConfVO rasImportConfVO = new RasImportConfVO();
		rasImportConfVO.setFileType(fileType);
		model.addAttribute("headList",importConfService.getTableInfo(rasImportConfVO,RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE));//表头信息
		model.addAttribute("rasBindVoInfo",importConfService.queryRasImportConfVO(rasImportConfVO,RasConstant.RAS_BANK_OPERATE_STATUS_REVENUE));//其它信息
		return "ras/limportRasRevenueConf";
	}
	
	/**
	 * 
	* @Title: list  
	* @Description: TODO(跳转到实收页面)  
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	@GetMapping("/receivedConf")
	public String receivedConfList(Model model){
		 model.addAttribute("receivedList",importConfService.getRasImportConfVOList(RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED));//表头信息
		 return "ras/limportReceivedConf";
	}
	
	
	/**
	 * 
	* @Title: add  
	* @Description: TODO(跳转到实收新增配置页面)  
	* @param @param model
	* @param @return    参数  
	* @return String    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDIMPORTCONF_OP)
	@GetMapping("/add")
	public String add(RasImportConfVO rasBindVoInfo,Model model){
		List<Map<String,String>>  headList=importConfService.getTableInfo(rasBindVoInfo,RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		//避免被覆盖
		if (null != headList && headList.size() > 0
				&& rasBindVoInfo.getFileType().equals("txt")) {
			Map<String, String> fieldMap = null;
			for (Map<String, String> valueMap : headList) {
				fieldMap = new HashMap<String, String>();
				for (Entry<String, String> vo : valueMap.entrySet()) {
					fieldMap.put(vo.getKey(), vo.getValue());
				}
				list.add(fieldMap);
			}
		}
		model.addAttribute("headList",list!=null&&list.size()>0?list:headList);//表头信息
		List<Map<String,String>>  endList=new ArrayList<Map<String,String>>();
		//文件类型为TXT
		if(!StringUtils.isBlank(rasBindVoInfo.getFileType()) 
				&& rasBindVoInfo.getFileType().equals("txt")
				&&(rasBindVoInfo.getIsFormat().equals("0")
				||rasBindVoInfo.getIsFormat().equals("undefined"))){
			if(rasBindVoInfo.getIsFormat().equals("undefined")){
				rasBindVoInfo.setIsFormat("1");//控制是否有分隔符
//				rasBindVoInfo.setIsFormat("1");
			}
			//查询结束endValList(初始化数据)
			endList = importConfService.getTableInfo(rasBindVoInfo);
			model.addAttribute("endList",endList);//表头信息
		}
		//根据数据来源和文件类型获取模板信息
		RasImportConfVO rasImportConfVO = importConfService.queryRasImportConfVO(rasBindVoInfo,RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
	
		if(null!=rasImportConfVO){
			if(!StringUtils.isBlank(rasBindVoInfo.getIsFormat()) 
					&& rasBindVoInfo.getFileType().equals("txt")){
					rasImportConfVO.setIsFormat(rasBindVoInfo.getIsFormat());
			}
			model.addAttribute("pageDateVo",rasImportConfVO);//存在的模板，引用VO对象
		}else{
			model.addAttribute("pageDateVo",rasBindVoInfo);//初始化的模板，引用VO对象
		}
		if(null!=rasBindVoInfo.getUsePower() &&rasBindVoInfo.getUsePower()==1){
			return "ras/editimportReceivedConf";
		}
		return "ras/addimportReceivedConf";
	}
	
	
	
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_RECEIVEDIMPORTCONF_OP)
	@LogMessage(content = "删除实收模板",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/del/{id}")
	@ResponseBody
	public AjaxJson check(@PathVariable("id") String id){
		return importConfService.delRasImportConfVOByid(id);   
	}
	
	
	/**
	 * 
	* @Title: saveOrstorage  
	* @Description: TODO(保存配置)  
	* @param @param operate
	* @param @param rasBankCardBindingVo
	* @param @return    参数  
	* @return AjaxJson    返回类型  
	* @throws
	 */
	//@RequiresPermissions(RasConstant.RAS_RESOURCE_REVENUEIMPORTCONF_OP)
	@LogMessage(content = "保存营收/实收数据导入配置信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/saveOrstorage/{operate}")
	@ResponseBody
	public AjaxJson saveOrstorage(@PathVariable("operate") String operate,RasImportConfVO  rasImportConfVO){
		return importConfService.baseSaveOrUpdate(rasImportConfVO,operate,this.getCurrentUser());
	}
	
}
