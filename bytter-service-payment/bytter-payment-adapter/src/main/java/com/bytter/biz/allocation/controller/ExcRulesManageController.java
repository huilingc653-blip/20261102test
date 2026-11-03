package com.bytter.biz.allocation.controller;
import com.bytter.biz.allocation.entity.ExcRulesManage;
import com.bytter.biz.allocation.service.ExcRulesManageService;
import com.bytter.biz.allocation.vo.ExcRulesManageAdCorpVO;
import com.bytter.biz.allocation.vo.ExcRulesManageVO;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author zhoup
 * @Description 例外规则表
 * @date 2018/2/6
 */
@Controller
@RequestMapping("/allocation/excrulesman")
public class ExcRulesManageController extends BaseController{
	@Autowired
	private ExcRulesManageService  excRulesManageService;
	@DubboReference(check = false)
	private IAccountGrantService  accountGrantService;
	
	/**
	 * 展示列表
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_QUERY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model){
		return "allocation/listExcRulesManage";
	}
	
	/**
	 * 展示列表数据
	 * @param datatablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput datatable(@RequestBody DataTablesInput datatablesInput){
		return excRulesManageService.getDataTablesUnion(datatablesInput,this.getCurrentUser() );
	}
	
	/**
	 * 新增页面
	 * @param excRluesManage
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@GetMapping("/add")
	public String add(ExcRulesManage excRluesManage,Model model ){
		if(StringUtils.isNotEmpty(excRluesManage.getId())){
			excRluesManage=excRulesManageService.get(excRluesManage.getId());
			model.addAttribute("excRluesManage",excRluesManage);
		}
		model.addAttribute("filterCode","1000");
		return "allocation/addExcRulesManage";
	}
	
	/**
	 * 展示页面
	 * @param id
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_QUERY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id,Model model){
		ExcRulesManageAdCorpVO excRulesManage = excRulesManageService.getByUnion(id);
		model.addAttribute("excRulesManage", excRulesManage);
		return "allocation/viewExcRulesManage";
	}
	
	/**
	 * 更新页面
	 * @param excRluesManage
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@GetMapping("/update")
	public String update(ExcRulesManage excRluesManage,Model model){
		if (StringUtils.isNotEmpty(excRluesManage.getId())) {
			excRluesManage = excRulesManageService.findById(excRluesManage.getId());
			model.addAttribute("excRluesManage", excRluesManage);
		}
		return "allocation/updateExcRulesManage";
	}
	
	/**
	 * 新增保存数据
	 * @param excRluesManageVO
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(ExcRulesManageVO excRluesManageVO){
		 excRulesManageService.save(excRluesManageVO, getCurrentUser());
		 return success();	
	}
	
	/**
	 * 修改后保存数据
	 * @param excRluesManage
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(ExcRulesManage excRluesManage){
		//判断收付款账号是否相同
		if(excRluesManage.getPayerAccNo().equals(excRluesManage.getPayeeAccNo())){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEQUALPAYEENO, excRluesManage.getPayerAccNo());
		}
		List<ExcRulesManage> listExc=excRulesManageService.getByPayeeNoAndPayerNo(excRluesManage.getPayeeAccNo(), excRluesManage.getPayerAccNo());
		int i=listExc.size();
		for(ExcRulesManage iteratorExc:listExc){
			if(iteratorExc.getId().equals(excRluesManage.getId())){
				i--;
			}
		}
		if(i>0){
			//判断此条数据是否已存在
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEXIST,excRluesManage.getPayerAccNo(),excRluesManage.getPayeeAccNo());
		} 	
		ExcRulesManage exRule = excRulesManageService.get(excRluesManage.getId(), true);
		BeanUtils.copyNotNullProperties(excRluesManage, exRule);
		exRule.setUpdateBy(getCurrentUser().getLoginName());
		exRule.setUpdateDate(DateUtils.getDate());
		excRulesManageService.saveOrUpdate(exRule);
		return success();
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		ExcRulesManage excRulesManage = excRulesManageService.get(id);
		excRulesManageService.deleteEntity(excRulesManage);
		return success();
	}
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_EXCRULESMANAGE_APPLY)
	@LogMessage(content = "例外规则管理表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids") String ids){
		this.excRulesManageService.batchDelete(ids);
		return success();
	}
	
	/**
	 * 根据收款账号Id获取例外规则管理表
	 * @param payerAccId
	 * @return
	 */
	@PostMapping("/getExcRulesManage")
	@ResponseBody
	public List<ExcRulesManage> getFcAccBookDtls(String payerAccId) {
		List<ExcRulesManage> excRluesManage = new ArrayList<ExcRulesManage>();
		if (StringUtils.isNotEmpty(payerAccId)) {
			excRluesManage = excRulesManageService.findBypayerAccId(payerAccId);
		} 
		return excRluesManage;
	}
	
	/**
	 * 获取付款账号
	 * @param corpId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId){
		List<BtBankAccDTO> listBankAcc=new  ArrayList<BtBankAccDTO>();
		if(!StringUtils.isEmpty(corpId)){
		List<BtBankAccDTO> getBankAcc=accountGrantService.listBy(corpId,AccountGrantConstant.ONLINE_BANK_TYPE,AccountGrantConstant.TRANSFER_ACCOUNT);
		if(!EmptyUtils.isEmptyList(getBankAcc)){
			listBankAcc.addAll(getBankAcc);
		}
		}
		return listBankAcc;
	}
	
	
	
	
	
}
