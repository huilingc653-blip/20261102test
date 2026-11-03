package com.bytter.biz.budget.controller;
import com.bytter.biz.budget.entity.FbsExcrateAdjust;
import com.bytter.biz.budget.service.FbsExcrateAdjustService;
import com.bytter.biz.budget.vo.FbsExcrateAdjustVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Controller
 * @Description: FBS_EXCRATE_ADJUST
 * @date 2019-08-15 10:46:42
 */
@Controller
@RequestMapping("/budget/fbsexcrateadjust")
public class FbsExcrateAdjustController extends BaseController {
	@Autowired
	private FbsExcrateAdjustService fbsExcrateAdjustService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private Ii18nService i18nService;
	/**
	 * 计划汇率调整表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询计划汇率调整表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "budget/listFbsExcrateAdjust";
	}
	/**
	 * 计划汇率调整表 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fbsExcrateAdjustService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 计划汇率调整表新增页面跳转
	 *
	 * @return
	 */
	@GetMapping("/add")
	public String add(FbsExcrateAdjust fbsExcrateAdjust, Model model) {
		if (StringUtils.isNotEmpty(fbsExcrateAdjust.getId())) {
			fbsExcrateAdjust = fbsExcrateAdjustService.get(fbsExcrateAdjust.getId());
			model.addAttribute("fbsExcrateAdjustPage", fbsExcrateAdjust);
		}
		return "budget/addFbsExcrateAdjust";
	}
	/**
	 * 计划汇率调整表编辑页面跳转
	 *
	 * @return
	 */
	@GetMapping("/update")
	public String update(FbsExcrateAdjustVO fbsExcrateAdjust, Model model) {
		if (StringUtils.isNotEmpty(fbsExcrateAdjust.getId())) {
			fbsExcrateAdjust = fbsExcrateAdjustService.getByIdUnion(fbsExcrateAdjust.getId());
			model.addAttribute("fbsExcrateAdjustPage", fbsExcrateAdjust);
		}
		return "budget/updateFbsExcrateAdjust";
	}
	/**
	 * 计划汇率调整表详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询计划汇率调整表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FbsExcrateAdjustVO fbsExcrateAdjust = fbsExcrateAdjustService.getByIdUnion(id);
		String businessBaseContext = i18nService.getBusinessBaseContext(fbsExcrateAdjust.getCurId());
		String businessBaseContextRefer = i18nService.getBusinessBaseContext(fbsExcrateAdjust.getReferCurId());
		if(StringUtils.isNotBlank(businessBaseContext)){
			fbsExcrateAdjust.setCurName(businessBaseContext);
		}
		if(StringUtils.isNotBlank(businessBaseContextRefer)){
			fbsExcrateAdjust.setReferCurName(businessBaseContextRefer);
		}
		model.addAttribute("fbsExcrateAdjustPage", fbsExcrateAdjust);
		return "budget/viewFbsExcrateAdjust";
	}
	/**
	 * 添加计划汇率调整表
	 *
	 * @param fbsExcrateAdjust
	 * @return
	 */
	@LogMessage(content = "添加计划汇率调整表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FbsExcrateAdjust fbsExcrateAdjust) {
		fbsExcrateAdjust.setCreateBy(this.getCurrentUser().getLoginName());
		fbsExcrateAdjust.setCreateDate(DateUtils.getDate());
		fbsExcrateAdjust.setPreTime(DateUtils.getDate());
		fbsExcrateAdjustService.save(fbsExcrateAdjust);
		return success();
	}
	/**
	 * 修改计划汇率调整表
	 *
	 * @param fbsExcrateAdjust
	 * @return
	 */
	@LogMessage(content = "修改计划汇率调整表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FbsExcrateAdjust fbsExcrateAdjust) {
		FbsExcrateAdjust f = fbsExcrateAdjustService.get(fbsExcrateAdjust.getId());
		fbsExcrateAdjust.setUpdateBy(this.getCurrentUser().getLoginName());
		fbsExcrateAdjust.setUpdateDate(DateUtils.getDate());
		fbsExcrateAdjust.setPreTime(DateUtils.getDate());
		BeanUtils.copyNotNullProperties(fbsExcrateAdjust, f);
		fbsExcrateAdjustService.saveOrUpdate(f);
		return  success();
	}
	/**
	 * 删除计划汇率调整表
	 * @param id
	 * @return
	 */
	@LogMessage(content = "删除计划汇率调整表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FbsExcrateAdjust fbsExcrateAdjust = fbsExcrateAdjustService.get(id);
		this.fbsExcrateAdjustService.deleteEntity(fbsExcrateAdjust);
		return success();
	}
	/**
	 * 获取汇率信息
	 *
	 * @param curId
	 * @return
	 */
	@RequestMapping(params = "getFbsExcrateAdjust")
	@ResponseBody
	public AjaxJson getFbsExcrateAdjust(String curId) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(true);
		BtCurrencyDTO currencyDTO = btCurrencyService.getNatualCurrency();
		FbsExcrateAdjustVO fbsExcrateAdjustVO = null;
		if (curId.equals(currencyDTO.getId())) {
			fbsExcrateAdjustVO = new FbsExcrateAdjustVO();
			//本位币设置为1
			fbsExcrateAdjustVO.setExchangeRate(BigDecimal.ONE);
		}else{
			fbsExcrateAdjustVO = fbsExcrateAdjustService.getFbsExcrateAdjustByCurReferDate(curId, currencyDTO.getId(), null);
		}
		j.setObj(fbsExcrateAdjustVO);
		return j;
	}
}
