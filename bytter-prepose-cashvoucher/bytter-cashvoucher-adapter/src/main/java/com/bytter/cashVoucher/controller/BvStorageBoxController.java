package com.bytter.cashVoucher.controller;

import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.entity.BvStorageBox;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvStorageBoxService;
import com.bytter.cashVoucher.vo.BvStorageBoxVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 库箱维护
 * @author onlineGenerator
 * @date 2017-05-25 18:17:27
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvstoragebox")
public class BvStorageBoxController extends BaseController {
	@Autowired
	private BvStorageBoxService bvStorageBoxService;
	
	@Autowired
	private BvLogService bvLogService;
    @DubboReference(check = false)
	private Ii18nService ii18nService;
	/**
	* BV_STORAGE_BOX列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions("bill.blankvou:bvstoragebox:index")
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/blankvou/listBvStorageBox";
	}
    /**
    * BV_STORAGE_BOX table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bvStorageBoxService.getBvStorageBoxs(dataTablesInput);	    		
    }
    
    
    
	/**
	* BV_STORAGE_BOX新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(BvStorageBox bvStorageBox, Model model) {
		if (StringUtils.isNotEmpty(bvStorageBox.getId())) {
			bvStorageBox = bvStorageBoxService.get(bvStorageBox.getId());
		}else {
			bvStorageBox.setId(StringUtils.getUUId());
		}
		model.addAttribute("bvStorageBoxPage", bvStorageBox);
		return "bill/blankvou/addBvStorageBox";
	}
	/**
	* BV_STORAGE_BOX编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(BvStorageBox bvStorageBox, Model model) {
		if (StringUtils.isNotEmpty(bvStorageBox.getId())) {
			boolean isUpdate=bvStorageBoxService.isUpdate(bvStorageBox.getId());
			bvStorageBox = bvStorageBoxService.get(bvStorageBox.getId());
			if(StringUtils.isNotEmpty(ii18nService.getBusinessBaseContext(bvStorageBox.getId()))){
				bvStorageBox.setName(ii18nService.getBusinessBaseContext(bvStorageBox.getId()));
			}
			model.addAttribute("bvStorageBoxPage", bvStorageBox);
			model.addAttribute("isUpdate", !isUpdate);
		}
		return "bill/blankvou/updateBvStorageBox";
	}
	/**
	* BV_STORAGE_BOX详情页
	*
	* @return
	*/
	@SuppressWarnings("unchecked")
	//@RequiresPermissions("bill.blankvou:bvstoragebox:view")
	@LogMessage(content = "查询BV_STORAGE_BOX详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {	
    	List<BvStorageBoxVo> rowdata=  bvStorageBoxService.getBvStorageBoxLook(id).getData();
		model.addAttribute("bvStorageBoxPage", rowdata.get(0));
		List<BvLog> bvlogList = bvLogService.listBvLog(id);
		model.addAttribute("bvlogList", bvlogList);
		return "bill/blankvou/viewBvStorageBox";
	}
	
	/**
	* BV_STORAGE_BOX设置停用
	* @param id
	* @return
	*/
	@LogMessage(content = "BV_STORAGE_BOX设置停用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/stop/{id}")
	@ResponseBody
	public AjaxJson stop(@PathVariable("id") String id){
		AjaxJson j=success();
		return bvStorageBoxService.stop(j, id,this.getCurrentUser());		
	}
	
	/**
	* 判断该库箱是否可以修改
	* @param id
	* @return
	*/
	@PostMapping("/isUpdate/{id}")
	@ResponseBody
	public AjaxJson isUpdate(@PathVariable("id") String id){
		AjaxJson j=success();
		j.setSuccess(true);
		boolean flag=bvStorageBoxService.isUpdate(id);
		if(!flag){
			j.setSuccess(false);
		}	
		return j;		
	}
	
	
	
	/**
	* BV_STORAGE_BOX设置启用
	* @param id
	* @return
	*/
	@LogMessage(content = "BV_STORAGE_BOX设置启用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/enable/{id}")
	@ResponseBody
	public AjaxJson enable(@PathVariable("id") String id){
		BvStorageBox bvStorageBox = bvStorageBoxService.get(id);
		bvStorageBoxService.updateValidSign(bvStorageBox,1,this.getCurrentUser());		
		return success();
	}
	
	/**
	 * 添加BV_STORAGE_BOX
	 * @return
	 * @throws IOException 
	 * @throws Exception 
	 */
	@LogMessage(content = "添加BV_STORAGE_BOX",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BvStorageBox box) {
		bvStorageBoxService.save(box,this.getCurrentUser());
		return success();
	}
	
	/**
	* 修改BV_STORAGE_BOX
	* @return
	*/
	@LogMessage(content = "修改BV_STORAGE_BOX",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BvStorageBox box){
		BvStorageBox t = bvStorageBoxService.get(box.getId());
		BeanUtils.copyNotNullProperties(box, t);
		bvStorageBoxService.saveOrUpdate(t, this.getCurrentUser());
        return success();	
	}
	/**
	* 删除BV_STORAGE_BOX
	* @param id
	* @return
	*/
	@LogMessage(content = "删除BV_STORAGE_BOX",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		BvStorageBox bvStorageBox = bvStorageBoxService.get(id);
		bvStorageBoxService.updateValidSign(bvStorageBox,2,this.getCurrentUser());
		return success();
	}
		
	/**
	 *  查询所有有效的库箱数据(标记为【启用】)                              
	 * @return list
	 */
	@GetMapping("/selectData")
	@ResponseBody
	public List<BvStorageBoxVo> qeruyStorageBoxByValid(){
		List list = bvStorageBoxService.listStorageBoxVOByValid(this.getCurrentUser());
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<>();
	}
	
	/**
	* 判断相同单位下，是否code也相同
	* @param id
	* @return
	*/
	@PostMapping("/checkCorpIdCode")
	@ResponseBody
    public AjaxJson checkCorpIdCode(String code,String corpId,String id)
    {
		AjaxJson j=success();
		j.setSuccess(bvStorageBoxService.storageBoxVerification(code,corpId,id));
		return j;
    }
	/**
	 * 根据单位id得到库箱列表
	 *
	 * @return
	 */
	@GetMapping("/jsonListByCorpId/{corpId}")
	@ResponseBody
	public List<BvStorageBoxVo> listStorageBoxByCord(@PathVariable("corpId") String corpId){
		return bvStorageBoxService.listStorageBoxByCord(corpId, this.getCurrentUser());
	}
	
	/**
	 * 根据多个单位id得到库箱列表
	 *
	 * @return
	 */
	@GetMapping("/jsonListByCorpIds/{corpId}")
	@ResponseBody
	public List<BvStorageBoxVo> listStorageBoxByCords(@PathVariable("corpId") String corpIds){
		System.out.println("根据单位id得到库箱列表 corpId = " + corpIds);
		List<BvStorageBoxVo> list = bvStorageBoxService.listStorageBoxVOByCords(corpIds, this.getCurrentUser());
		if(list != null && list.size() > 0){
			return list;
		}
		return new ArrayList<>();
	}
	
}
