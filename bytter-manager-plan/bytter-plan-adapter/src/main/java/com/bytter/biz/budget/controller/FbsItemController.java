package com.bytter.biz.budget.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.service.FbsItemService;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
/**   
 * @Title: Controller  
 * @Description: 预算科目
 * @author onlineGenerator
 * @date 2017-07-06 16:40:26
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/budget/fbsitem")
public class FbsItemController extends BaseController {
	@Autowired
	private FbsItemService fbsItemService;
	/**
	* 预算科目列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "查询预算科目列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(FbsItemVO fbsItem,Model model) {
		fbsItem.setShowItem(0);
		model.addAttribute("treeData",fbsItemService.listTreeData(fbsItem,""));
		this.initMaxLevelShow(model);
		model.addAttribute("fbsItemPage", fbsItem);
		return "budget/listFbsItem";
	}
	private void initMaxLevelShow(Model model){
		//获取科目最大级别
		Integer maxLevel = this.fbsItemService.getItemMaxLevel();
		String maxLevelShow="0";
		for(int i=1; i<=maxLevel;i++){
			maxLevelShow += CommonConstant.COMMA_SPLIT+i;
		}
		maxLevelShow += CommonConstant.COMMA_SPLIT+"-1";
		model.addAttribute("maxLevelShow", maxLevelShow);
	}
    /**
    * 预算科目 table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "查询银行账号关系策略维护表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/queryList")
	public String queryList(FbsItemVO fbsItem, Model model) throws Exception{
		model.addAttribute("treeData",fbsItemService.listTreeData(fbsItem,""));
		model.addAttribute("fbsItemPage",fbsItem);
		this.initMaxLevelShow(model);
		return "budget/listFbsItem";
	}
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@GetMapping("/view")
	public String view(String id, String parentId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FbsItemVO fbsItem = fbsItemService.getFbsItemVO(id,null);
			if(StringUtils.isNotBlank(fbsItem.getSuperItemCode())){
				fbsItem.setSuperItemName(fbsItemService.getFbsItemVO(null,fbsItem.getSuperItemCode()).getItemName());
			}else{
				fbsItem.setSuperItemName("");
			}
			model.addAttribute("fbsItemPage", fbsItem);
		}
		if (StringUtils.isNotEmpty(parentId)) {
			FbsItemVO fbsItem = fbsItemService.getFbsItemVO(parentId,null);
			FbsItemVO newItem =new FbsItemVO();
			newItem.setSuperItemCode(fbsItem.getItemCode());
			newItem.setSuperItemName(fbsItem.getItemName());
			newItem.setSign(fbsItem.getSign());
			newItem.setIsBeginPeriod(fbsItem.getIsBeginPeriod());
			newItem.setIsEndPeriod(fbsItem.getIsEndPeriod());
			model.addAttribute("fbsItemPage", newItem);
			model.addAttribute("isSubItem", "1");
			return "budget/addFbsItem";
		}
		return "budget/updateFbsItem";
	}
	@GetMapping("/tree")
	@ResponseBody
	public Object tree(String type) {
		FbsItemVO fbsItem = new FbsItemVO();
		List<FbsItemVO> itemList = fbsItemService.listTreeData(fbsItem,"");
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		for (FbsItemVO item : itemList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", item.getItemCode());
			map.put("pId", item.getSuperItemCode());
			map.put("name", "("+item.getItemCode()+")"+item.getItemName());
			map.put("code", item.getItemCode());
			tempList.add(map);
		}
		return tempList;
	}
	/**
	* 预算科目详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "查询预算科目详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FbsItem fbsItem = fbsItemService.get(id);
		model.addAttribute("fbsItemPage", fbsItem);
		return "budget/viewFbsItem";
	}
	/**
	 * 预算科目新增页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@GetMapping("/add")
	public String add(FbsItem fbsItem, Model model) {
		if (StringUtils.isNotEmpty(fbsItem.getId())) {
			fbsItem = fbsItemService.get(fbsItem.getId());
			model.addAttribute("fbsItemPage", fbsItem);
		}
		return "budget/addFbsItem";
	}
	/**
	 * 添加预算科目
	 * @param fbsItem
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "添加预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FbsItem fbsItem){
		fbsItemService.save(fbsItem, this.getCurrentUser(),LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	* 修改预算科目
	* @param fbsItem
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "修改预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FbsItem fbsItem){
		FbsItem t = fbsItemService.get(fbsItem.getId());
		if(CommonConstant.VALID_SIGN_NO.equals(fbsItem.getValidSign()) && fbsItemService.isEdit(t.getItemCode())){
			throw new ParameterException(FbsBudgetMessageEnum.FBS_BUDGET_ITEM_CODE_MESSAGE_SUB_VALIDSIGN,"("+t.getItemCode()+")"+t.getItemName());
		}
		BeanUtils.copyNotNullProperties(fbsItem, t);
		fbsItemService.saveOrUpdate(t, this.getCurrentUser(),LocaleContextHolder.getLocale().toString());
		return  success();
	}
	/**
	* 删除预算科目
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@LogMessage(content = "删除预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FbsItem fbsItem = fbsItemService.get(id);
		fbsItemService.deleteEntity(fbsItem);
		return success();
	}

	//@RequiresPermissions(ResourceConstant.FBS_FBSITEM_APPLY)
	@GetMapping("/selectBudgetFbsItem")
	public String selectBudgetFbsItem(FbsItemVO fbsItem,Model model) {
		String itemCode = fbsItem.getItemCode();
		fbsItem.setItemCode(null);
		fbsItem.setShowItem(0);
		model.addAttribute("treeData",fbsItemService.listTreeData1(fbsItem, itemCode, "1"));
		this.initMaxLevelShow(model);
		model.addAttribute("fbsItemPage", fbsItem);
		return "budget/selectBudgetFbsItem";
	}
}
