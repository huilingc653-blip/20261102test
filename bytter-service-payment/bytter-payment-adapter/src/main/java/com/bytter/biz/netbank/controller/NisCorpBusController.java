package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.vo.NisCorpBusVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 单位业务类别对照表
 * @date 2017-05-18 16:36:11
 */
@Controller
@RequestMapping("/netbank/niscorpbus")
@Slf4j
public class NisCorpBusController extends BaseController {
	@Autowired
	private NisCorpBusService nisCorpBusService;
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * 查询单位业务种类设置列表
	 *
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@LogMessage(content = "查询单位业务种类设置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/list")
	public String list(Model model) {
		return "netbank/listNisCorpBus";
	}
	/**
	 * 单位业务类别显示 table列表
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableList")
	@ResponseBody
	public DataTablesOutput dataTableList(@RequestBody DataTablesInput dataTablesInput) {
		return nisCorpBusService.getDataTablesUnion(dataTablesInput, getCurrentUser().getId());
	}

	/**
	 * 单位业务类别对照表新增页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@GetMapping("/add")
	public String add(NisCorpBus nisCorpBus, Model model) {
		if (StringUtils.isNotEmpty(nisCorpBus.getId())) {
			nisCorpBus = nisCorpBusService.get(nisCorpBus.getId());
			model.addAttribute("nisCorpBusPage", nisCorpBus);
		}
		//获取有效的业务种类
		//model.addAttribute("nisBusTypes", nisBusTypeService.getValidBusType());
		//获取分配的单位
		model.addAttribute("corps", nisCorpBusService.getCorpIdList());
		//获取业务种类列表
		model.addAttribute("nisBusTypeList", nisBusTypeService.getNisBusType("1"));
		return "netbank/addNisCorpBus";
	}
	/**
	 * 单位业务类别对照表新增页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@GetMapping("/copy")
	public String copy(NisCorpBus nisCorpBus, Model model) {
		if (StringUtils.isNotEmpty(nisCorpBus.getId())) {
			nisCorpBus = nisCorpBusService.get(nisCorpBus.getId());
			model.addAttribute("nisCorpBusPage", nisCorpBus);
		}
		String busTypes = "";
		if (StringUtils.isNotEmpty(nisCorpBus.getCorpId())) {
			busTypes = nisCorpBusService.findBusTypeName(nisCorpBus.getCorpId());
		}
		log.debug("------------busTypes="+busTypes);
		model.addAttribute("copyedNisBusTypes", busTypes);
		//获取有效的业务种类
		//model.addAttribute("nisBusTypes", nisBusTypeService.getValidBusType());
		//获取分配的单位
		model.addAttribute("corps", nisCorpBusService.getCorpIdList());
		//获取业务种类列表
		model.addAttribute("nisBusTypeList", nisBusTypeService.getNisBusType("1"));
		return "netbank/copyNisCorpBus";
	}
	/**
	 * 添加单位业务类别对照表
	 *
	 * @param nisCorpBus
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@LogMessage(content = "添加单位业务类别对照表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisCorpBus nisCorpBus, @RequestParam("busIds[]") List<String> busIds) {
		if (busIds.isEmpty() || (busIds.size() == 1 && CommonConstant.NO.equals(busIds.get(0)))) {
			busIds = new ArrayList<>();
		}
		nisCorpBusService.assignBusCorp(nisCorpBus, busIds);
		return success();
	}
	@RequestMapping(params = "getNisCorpBus")
	@ResponseBody
	public AjaxJson getNisCorpBus(String corpId, String cdSign) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setObj(nisCorpBusService.getByBusType(corpId, cdSign));
		return j;
	}
	@RequestMapping(params = "getBusIdsByCorpId")
	@ResponseBody
	public AjaxJson getBusIdsByCorpId(String corpId) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setObj(nisCorpBusService.getBusIdsByCorpId(corpId));
		return j;
	}
	/**
	 * 根据收付款标志获取分配的业务种类
	 *
	 * @param cdSign
	 * @return
	 */
	@RequestMapping(params = "getNisCorpBusByCdSign")
	@ResponseBody
	public AjaxJson getNisCorpBusByCdSign(String cdSign) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setObj(nisCorpBusService.getNisCorpBusByCdSign(cdSign));
		return j;
	}
	/**
	 * 获取分配的业务种类
	 *
	 * @param corpId
	 * @param cdSign
	 * @return
	 */
    @RequestMapping( "/listNisCorpBusVOByCorpIdAndCdSign")
    @ResponseBody
    public List<NisCorpBusVO> listNisCorpBusVOByCorpIdAndCdSign(String corpId, String cdSign) {
        return nisCorpBusService.getByBusType(corpId, cdSign);
    }
	/**
	 * 获取分配的业务种类
	 *
	 * @param cdSign
	 * @return
	 */
	@RequestMapping(params = "listNisCorpBusVOByCdSign")
	@ResponseBody
	public List<NisCorpBusVO> listNisCorpBusVOByCdSign(String cdSign) {
		return nisCorpBusService.getByBusType(null, cdSign);
	}
	/**
	 * 获取分配的业务种类
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "查询分配的业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{corpId}")
	public String view(@PathVariable("corpId") String corpId, Model model) {
		model.addAttribute("corp", corpService.get(corpId));
		return "netbank/viewNisCorpBus";
	}
	/**
	 * 单位业务类别显示 table列表
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISCORPBUS_APPLY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableView")
	@ResponseBody
	public DataTablesOutput dataTableView(@RequestBody DataTablesInput dataTablesInput) {
		return nisCorpBusService.getDataTablesViewUnion(dataTablesInput);
	}
}
