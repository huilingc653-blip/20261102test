package com.bytter.biz.budget.saas.controller.budget;

import com.bytter.biz.budget.entity.FbsItem;
import com.bytter.biz.budget.service.FbsItemService;
import com.bytter.biz.budget.spi.dto.FbsItemViewDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.biz.vo.FbsItemListVO;
import com.bytter.biz.vo.FbsItemViewVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 预算科目
 * @author onlineGenerator
 * @date 2017-07-06 16:40:26
 * @version 1.0
 *
 */
@Api(value = "/v1-plan/budget/fbsitem", tags = "预算科目")
@RestController("v1FbsItemController")
@RequestMapping("/v1-plan/budget/fbsitem")
public class FbsItemController extends BaseController {
	@Autowired
	private FbsItemService fbsItemService;
	/**
	* 预算科目列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询预算科目列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public AjaxJson<FbsItemListVO> list(@RequestBody FbsItemVO fbsItem) {
		FbsItemListVO vo = new FbsItemListVO();
		fbsItem.setShowItem(0);
		vo.setTreeData(fbsItemService.listTreeData(fbsItem, ""));
		this.initMaxLevelShow(vo);
		vo.setFbsItemPage(fbsItem);
		return AjaxJson.success(vo);
	}


	private void initMaxLevelShow(FbsItemListVO model){
		//获取科目最大级别
		Integer maxLevel = this.fbsItemService.getItemMaxLevel();
		String maxLevelShow="0";
		for(int i=1; i<=maxLevel;i++){
			maxLevelShow += CommonConstant.COMMA_SPLIT+i;
		}
		maxLevelShow += CommonConstant.COMMA_SPLIT+"-1";
		model.setMaxLevelShow(  maxLevelShow);
	}
    /**
    * 预算科目 table列表
    *
    * @return
    */

	@LogMessage(content = "查询银行账号关系策略维护表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/queryList")
	public AjaxJson<FbsItemListVO> queryList(@RequestBody FbsItemVO fbsItem) throws Exception{
		FbsItemListVO vo = new FbsItemListVO();
		vo.setTreeData( fbsItemService.listTreeData(fbsItem,""));
		vo.setFbsItemPage( fbsItem);
		this.initMaxLevelShow(vo);
		return AjaxJson.success(vo);
	}

	@GetMapping("/view")
	public AjaxJson<FbsItemViewVO> view(String id,String parentId) {
		FbsItemViewVO vo=new FbsItemViewVO();
		if (StringUtils.isNotEmpty(id)) {
			FbsItemVO fbsItem = fbsItemService.getFbsItemVO(id,null);
			if(StringUtils.isNotBlank(fbsItem.getSuperItemCode())){
				fbsItem.setSuperItemName(fbsItemService.getFbsItemVO(null,fbsItem.getSuperItemCode()).getItemName());
			}else{
				fbsItem.setSuperItemName("");
			}
			vo.setFbsItemPage(  fbsItem);
		}
		if (StringUtils.isNotEmpty(parentId)) {
			FbsItemVO fbsItem = fbsItemService.getFbsItemVO(parentId,null);
			FbsItemVO newItem =new FbsItemVO();
			newItem.setSuperItemCode(fbsItem.getItemCode());
			newItem.setSuperItemName(fbsItem.getItemName());
			newItem.setSign(fbsItem.getSign());
			newItem.setIsBeginPeriod(fbsItem.getIsBeginPeriod());
			newItem.setIsEndPeriod(fbsItem.getIsEndPeriod());
			vo.setFbsItemPage(  newItem);
			vo.setIsSubItem(  "1");
			vo.setForwardUrl("budget/addFbsItem");
			return AjaxJson.success(vo);
		}
		vo.setForwardUrl("budget/updateFbsItem");
		return AjaxJson.success(vo);
	}


	@GetMapping("/tree")
	public AjaxJson<List<Map<String, Object>> > tree() {
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
		return AjaxJson.success(tempList);
	}

	/**
	* 预算科目详情页
	*
	* @return
	*/
	@LogMessage(content = "查询预算科目详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<FbsItem> view(@PathVariable("id") String id) {
		FbsItem fbsItem = fbsItemService.get(id);
		return AjaxJson.success(fbsItem);
	}


	/**
	 * 添加预算科目
	 * @param fbsItem
	 * @return
	 */

	@LogMessage(content = "添加预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")

	public AjaxJson save(@RequestBody  FbsItem fbsItem){
		fbsItemService.save(fbsItem, this.getCurrentUser(),LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	* 修改预算科目
	* @param fbsItem
	* @return
	*/

	@LogMessage(content = "修改预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")

	public AjaxJson update(@RequestBody FbsItem fbsItem){
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

	@LogMessage(content = "删除预算科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable("id") String id){
		FbsItem fbsItem = fbsItemService.get(id);
		fbsItemService.deleteEntity(fbsItem);
		return success();
	}


	@GetMapping("/selectBudgetFbsItem")
	public AjaxJson<FbsItemListVO> selectBudgetFbsItem(@RequestBody FbsItemVO fbsItem) {
		FbsItemListVO vo=new FbsItemListVO();
		String itemCode = fbsItem.getItemCode();
		fbsItem.setItemCode(null);
		fbsItem.setShowItem(0);
		vo.setTreeData( fbsItemService.listTreeData1(fbsItem, itemCode, "1"));
		this.initMaxLevelShow(vo);
		vo.setFbsItemPage(  fbsItem);
		return AjaxJson.success(vo);
	}
}
