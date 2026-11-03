package com.bytter.biz.netbank.controller;
import com.bytter.biz.budget.spi.dto.FbsItemDTO;
import com.bytter.biz.budget.spi.service.IFbsItemService;
import com.bytter.biz.netbank.entity.NisBusType;
import com.bytter.biz.netbank.entity.NisCorpBus;
import com.bytter.biz.netbank.service.NisBusTypeService;
import com.bytter.biz.netbank.service.NisCorpBusService;
import com.bytter.biz.netbank.vo.NisBusTypeExcleVO;
import com.bytter.biz.netbank.vo.NisBusTypeRuleVO;
import com.bytter.biz.netbank.vo.NisBusTypeVO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 业务类别表
 * @author onlineGenerator
 * @date 2017-05-17 19:23:52
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/nisbustype")
public class NisBusTypeController extends BaseController {
	@Autowired
	private NisBusTypeService nisBusTypeService;
	@Autowired
	private NisCorpBusService nisCorpBusService;
	@DubboReference(check = false)
	private IFbsItemService fbsItemService;
	/**
	* 业务类别表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "查询业务类别表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listNisBusType";
	}
    /**
    * 业务类别表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return nisBusTypeService.findListBysql(dataTablesInput);
    }
	/**
	* 业务类别表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@GetMapping("/add")
	public String add(NisBusType nisBusType, Model model) {
		if (StringUtils.isNotEmpty(nisBusType.getId())) {
			nisBusType = nisBusTypeService.get(nisBusType.getId());
			model.addAttribute("nisBusTypePage", nisBusType);
		}
		return "netbank/addNisBusType";
	}
	/**
	* 业务类别表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@GetMapping("/update")
	public String update(NisBusType nisBusType, Model model) {
		if (StringUtils.isNotEmpty(nisBusType.getId())) {
			nisBusType = nisBusTypeService.get(nisBusType.getId(),true);
			List<NisBusTypeRuleVO> nisBusTypeRuleVOList =  nisBusTypeService.getByByBusId(nisBusType.getId());
			int length = nisBusTypeRuleVOList.size();
			int sysBusType = 0;
			if(StringUtils.isNotEmpty(nisBusType.getSysBusType())) {
				if (!("1".equals(nisBusType.getSysBusType()) || "2".equals(nisBusType.getSysBusType()) || "3".equals(nisBusType.getSysBusType()))) {
					sysBusType = 1;
				}
			}
			model.addAttribute("nisBusTypeRuleVOList", nisBusTypeRuleVOList);
			model.addAttribute("nisBusTypePage", nisBusType);
			model.addAttribute("length", length);
			model.addAttribute("sysBusType", sysBusType);
			List<NisCorpBus> listCorpBus = nisCorpBusService.getByBusCorp(nisBusType.getId());
			String corpId = "";
			if (EmptyUtils.isNotEmpty(listCorpBus)) {
				for (NisCorpBus nisCorpBus:listCorpBus) {
					corpId = corpId + nisCorpBus.getCorpId() + CommonConstant.COMMA_SPLIT;
				}
			}
			model.addAttribute("corpId", corpId);
		}
		return "netbank/updateNisBusType";
	}
	/**
	* 业务类别表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "查询业务类别表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		NisBusTypeVO nisBusType = nisBusTypeService.getByUnion(id);
		List<NisBusTypeRuleVO> nisBusTypeRuleVOList =  nisBusTypeService.getByByBusId(id);
		int sysBusType = 0;
		if(StringUtils.isNotEmpty(nisBusType.getSysBusType())) {
			if (!("1".equals(nisBusType.getSysBusType()) || "2".equals(nisBusType.getSysBusType()) || "3".equals(nisBusType.getSysBusType()))) {
				sysBusType = 1;
			}
		}
		model.addAttribute("nisBusTypePage", nisBusType);
		model.addAttribute("nisBusTypeRuleVOList", nisBusTypeRuleVOList);
		model.addAttribute("sysBusType", sysBusType);
		return "netbank/viewNisBusType";
	}
	/**
	 * 添加业务类别表
	 * @param nisBusTypeVO
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "添加业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisBusTypeVO nisBusTypeVO, HttpServletRequest request){
		if(nisBusTypeService.findBusType(nisBusTypeVO.getBusType()) != null) {
			throw new ParameterException(NetbankMessageEnum.NIS_ASSIGN_MESSAGE_BUSTYPE_UNIQUE);
		}
		NisBusType nisBusType = new NisBusType();
		BeanUtils.copyNotNullProperties(nisBusTypeVO, nisBusType);
		nisBusType.setCreateBy(this.getCurrentUser().getLoginName());
		nisBusType.setCreateDate(DateUtils.getDate());
		nisBusTypeService.save(nisBusType, LocaleContextHolder.getLocale().toString(),request);
		if (StringUtils.isNotBlank(nisBusTypeVO.getCorpId())) {
			nisCorpBusService.saveByCorpId(nisBusTypeVO.getCorpId(), nisBusType.getId(), this.getCurrentUser());
		}
		return success();
	}
	/**
	* 修改业务类别表
	* @param nisBusTypeVO
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "修改业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(NisBusTypeVO nisBusTypeVO, HttpServletRequest request){
		NisBusType t = nisBusTypeService.get(nisBusTypeVO.getId());
		BeanUtils.copyNotNullProperties(nisBusTypeVO, t);
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		nisBusTypeService.saveOrUpdate(t,LocaleContextHolder.getLocale().toString(),request);
		nisCorpBusService.saveByCorpId(nisBusTypeVO.getCorpId(), t.getId(), this.getCurrentUser());

		return  success();
	}
	/**
	* 删除业务类别表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "删除业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		NisBusType nisBusType = nisBusTypeService.get(id);
		this.nisBusTypeService.deleteNisBusType(nisBusType);
		return success();
	}
	/**
	* 批量删除业务类别表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISBUSTYPE_APPLY)
	@LogMessage(content = "批量删除业务类别表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		this.nisBusTypeService.batchDelete(ids);
		return success();
	}
	/**
	 * 查询所有的业务种类
	 */
	@GetMapping("/json")
    @ResponseBody
    public List<NisBusType> json(){
        return nisBusTypeService.listAll();
    }
	@RequestMapping(params="getNisCorpBus")
	@ResponseBody
	public AjaxJson getNisCorpBus(String corpId,String cdSign){
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		j.setObj(nisCorpBusService.getByBusType(corpId,cdSign));
		return j;
	}
	/**
	 * 根据收付款取计划科目
	 */
	@GetMapping("/getItem")
    @ResponseBody
    public Object getItem(Integer cdSign) {
		if(1 == cdSign){
			cdSign = -1;
		}else{
			cdSign = 1;
		}
		//科目流向根据大科目定义即可，与组件保持一致
		List<FbsItemDTO> fbsItem= fbsItemService.listFbsItemInfoBySign("1", null);
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        for (FbsItemDTO fid : fbsItem) {
        	if(cdSign == null || cdSign.equals(fid.getSign())
					&& !CommonConstant.YES.equals(fid.getIsBeginPeriod()) && !CommonConstant.YES.equals(fid.getIsEndPeriod())){
				if(StringUtils.isNotBlank(fid.getItemRelation()) && StringUtils.isBlank(fid.getSuperItemCode())) {
					continue;
				}
	        	Map<String, Object> map = new HashMap<String, Object>();
	        	map.put("id", fid.getItemCode());
	        	map.put("pId", fid.getSuperItemCode());
	        	map.put("name", "("+fid.getItemCode()+")"+fid.getItemName());
				map.put("canSelect", "1".equals(fid.getIsWrite()));
	        	tempList.add(map);
        	}
		}
        return tempList;
	}
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<NisBusTypeExcleVO> list) {
		this.nisBusTypeService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 获取业务类别信息
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getNisBusType")
	@ResponseBody
	public AjaxJson getNisBusType(String id){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(nisBusTypeService.get(id, true));
		return j;
	}
	/**
	 * 所有有效的业务种类
	 *
	 * @return
	 */
	@PostMapping("/json")
	@ResponseBody
	public List<NisBusType> getListByValidSign() {
		List<NisBusType> list=nisBusTypeService.getValidBusType();
		return list;
	}
}
