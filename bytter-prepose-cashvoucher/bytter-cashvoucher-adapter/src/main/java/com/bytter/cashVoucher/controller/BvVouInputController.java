package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.entity.BvVouInput;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvVouInputService;
import com.bytter.cashVoucher.vo.BvVouInputVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**   
 * @Title: Controller  
 * @Description: 凭证入库信息表
 * @author onlineGenerator
 * @date 2017-05-25 17:06:40
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvvouinput")
public class BvVouInputController extends BaseController {
	@Autowired
	private BvVouInputService bvVouInputService;
	@Autowired
	private BvLogService bvLogService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	/**
	* 凭证入库信息表列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/blankvou/listBvVouInput";
	}
	
    /**
    * 凭证入库信息表 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询凭证入库信息列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput input){
    	return bvVouInputService.getDataTablesUnion(input,this.getCurrentUser());
    }
	
	/**
	* 凭证入库信息表新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(BvVouInput bvVouInput, Model model) {
		if (StringUtils.isNotEmpty(bvVouInput.getId())) {
			bvVouInput = bvVouInputService.get(bvVouInput.getId());
			model.addAttribute("bvVouInputPage", bvVouInput);
		}
		return "bill/blankvou/addBvVouInput";
	}
	
	/**
	* 凭证入库信息表编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id ,Model model) {
		BvVouInputVo vo = bvVouInputService.getDataTablesRow(id,BlankvouConstant.BILL_OPERATE_EDIT,this.getCurrentUser());
		model.addAttribute("bvVouInputVo", vo);
		return "bill/blankvou/updateBvVouInput";
	}
	
	/**
	* 凭证入库信息表详情页
	*
	* @return
	*/
	@LogMessage(content = "查询凭证入库信息详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BvVouInputVo vo = bvVouInputService.getDataTablesRow(id,null,this.getCurrentUser());
		List<BvLog> bvlogList = bvLogService.listBvLog(vo.getId());
		model.addAttribute("bvlogList", bvlogList);
		model.addAttribute("bvVouInputPage", vo);
		return "bill/blankvou/viewBvVouInput";
	}
	/**
	 * 添加凭证入库信息表--保存
	 * @param bvVouInput
	 * @return
	 */
	@LogMessage(content = "添加凭证入库信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BvVouInput bvVouInput){
		boolean flag = checkVoucherNoUnique(bvVouInput, BlankvouConstant.BILL_OPERATE_SAVE);
		AjaxJson ajaxJson = success();
		if(flag){
			bvVouInputService.save(bvVouInput,this.getCurrentUser());
		}else{
			ajaxJson.setMsg(null);
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}
	
	/**
	* 修改凭证入库信息表
	* @param bvVouInput
	* @return
	*/
	@LogMessage(content = "修改凭证入库信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BvVouInput bvVouInput){
		boolean flag = checkVoucherNoUnique(bvVouInput, BlankvouConstant.BILL_OPERATE_EDIT);
		AjaxJson ajaxJson = success();
		if(flag){
			bvVouInputService.updateById(bvVouInput,bvVouInput.getId(),this.getCurrentUser());
		}else{
			ajaxJson.setSuccess(false);
		}
		return  ajaxJson;
	}
	
	/**
	* 删除凭证入库信息表
	* @param id
	* @return
	*/
	@LogMessage(content = "删除凭证入库信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(bvVouInputService.deleteById(id,this.getCurrentUser()));
		return ajaxJson;
	}
	
	/**
	* 批量删除凭证入库信息表
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除凭证入库信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String ids){
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(bvVouInputService.batchDelete(ids,this.getCurrentUser()));
		return ajaxJson;
	}
	
	/**
	 * 将凭证信息入库到凭证信息明细--操作栏点击入库
	 * @param id
	 * @return
	 */
	@LogMessage(content = "空白凭证入库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/storageOperat/{id}")
	@ResponseBody
	public AjaxJson storage(@PathVariable("id") String id){
		BvVouInput bvVouInput = bvVouInputService.get(id);
		boolean flag = checkVoucherNoUnique(bvVouInput, BlankvouConstant.BILL_OPERATE_STORAGE);
		AjaxJson ajaxJson = success();
		if(flag){
			bvVouInputService.storage(bvVouInput, BlankvouConstant.BILL_OPERATE_EDIT,this.getCurrentUser());
		}else{
			ajaxJson.setSuccess(false);
		}
		return  ajaxJson;
	}
	
	/**
	* 将凭证信息入库到凭证信息明细--操作栏点击入库
	* @return
	*/
	@LogMessage(content = "空白凭证批量入库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchStorage")
	@ResponseBody
	public AjaxJson batchStorage(String ids){
		AjaxJson ajaxJson = success();
		bvVouInputService.batchBtorage(ids,BlankvouConstant.BILL_OPERATE_EDIT,this.getCurrentUser());
		return  ajaxJson;
	}
	
	/**
	* 将凭证信息入库到凭证信息明细--新增或修改页面点击入库
	* @param bvVouInput
	* @return
	*/
	@LogMessage(content = "空白凭证入库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/storage/{operate}")
	@ResponseBody
	public AjaxJson storage(BvVouInput bvVouInput,@PathVariable("operate") String operate){
		boolean flag = checkVoucherNoUnique(bvVouInput, operate);
		String beginNo = bvVouInput.getBeginNo();
		String endNo = bvVouInput.getEndNo();
		AjaxJson ajaxJson = success();
		if(flag){
			bvVouInput.setBeginNo(beginNo);
			bvVouInput.setEndNo(endNo);
			bvVouInputService.storage(bvVouInput, operate,this.getCurrentUser());
		}else{
			ajaxJson.setSuccess(false);
		}
		return  ajaxJson;
	}
	
	/**
	* 检查主数据凭证号是否重复
	* @param bvVouInput
	* @return
	*/
	public boolean checkVoucherNoUnique(BvVouInput bvVouInput,String operate){
		return bvVouInputService.checkVoucherNoUnique(bvVouInput,operate);
	}
    
    /**
	 * 根据银行账号获取银行信息
	 * @param bankAcc  单位id
	 * @return
	 */
    @GetMapping("/getBankDataByBankAcc/{bankAcc}")
	@ResponseBody
	public List<BtBankAccDTO> getBankDataByBankAcc(@PathVariable("bankAcc") String bankAcc){
		return bvVouInputService.getBankDataByBankAcc(bankAcc);
	}
	/**
	 * 根据单位ids获取银行账号信息
	 * @param corpIds  单位id
	 * @return
	 */
	@GetMapping("/getBankAccListByCorpIds/{corpIds}")
	@ResponseBody
	public List<BtBankAccDTO> getBankAccListByCorpIds(@PathVariable("corpIds") String corpIds){
		String[] ids = corpIds.split (",");
		List<String> idList = new ArrayList<String>(Arrays.asList(ids));
		// todo:模拟银行账户信息
		return getListByCorpIds(idList);
	}
	@GetMapping("/getBankAccList/json")
	@ResponseBody
	public List<BtBankAccDTO> getBankAccList(){
		return   btBankAccService.getDistinctBankAccList();
	}

	public List<BtBankAccDTO> getListByCorpIds(List<String> corpIds){
		List<BtBankAccDTO> bankAccList =  btBankAccService.getListByCorpIds(corpIds);
		return bankAccList.stream().filter(bankAcc -> corpIds.contains(bankAcc.getCorpId())).collect(Collectors.toList());
	}
	/**
	 * 导入保证申请
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入凭证入库操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BvVouInputVo> list) {
		AjaxJson json=success();
		boolean flag=bvVouInputService.saveImport(list,this.getCurrentUser());
		json.setSuccess(flag);
		return success();
	}
}
