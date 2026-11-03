package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: FC_ONLINE_LEDGER
 * @author onlineGenerator
 * @date 2017-06-07 10:03:54
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fconlineledger")
public class FcOnlineLedgerController extends BaseController {
	@DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	/**
	* FC_ONLINE_LEDGER列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询FC_ONLINE_LEDGER列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcOnlineLedger";
	}
    /**
    * FC_ONLINE_LEDGER table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcOnlineLedgerService.getDataTables(dataTablesInput);
    }
	/**
	* FC_ONLINE_LEDGER新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(FcOnlineLedgerVO fcOnlineLedger, Model model) {
		if (StringUtils.isNotEmpty(fcOnlineLedger.getId())) {
			fcOnlineLedger = fcOnlineLedgerService.getFcOnlineLedgerById(fcOnlineLedger.getId());
			model.addAttribute("fcOnlineLedgerPage", fcOnlineLedger);
		}
		return "inneraccount/addFcOnlineLedger";
	}
	/**
	* FC_ONLINE_LEDGER编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(FcOnlineLedgerVO fcOnlineLedger, Model model) {
		if (StringUtils.isNotEmpty(fcOnlineLedger.getId())) {
			fcOnlineLedger = fcOnlineLedgerService.getFcOnlineLedgerById(fcOnlineLedger.getId());
			model.addAttribute("fcOnlineLedgerPage", fcOnlineLedger);
		}
		return "inneraccount/updateFcOnlineLedger";
	}
	/**
	* FC_ONLINE_LEDGER详情页
	*
	* @return
	*/
	@LogMessage(content = "查询FC_ONLINE_LEDGER详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getFcOnlineLedgerById(id);
		model.addAttribute("fcOnlineLedgerPage", fcOnlineLedger);
		return "inneraccount/viewFcOnlineLedger";
	}
	/**
	 * 添加FC_ONLINE_LEDGER
	 * @param fcOnlineLedger
	 * @return
	 */
	@LogMessage(content = "添加FC_ONLINE_LEDGER",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcOnlineLedgerVO fcOnlineLedger){
		fcOnlineLedgerService.save(fcOnlineLedger);
		return success();
	}
	/**
	* 修改FC_ONLINE_LEDGER
	* @param fcOnlineLedger
	* @return
	*/
	@LogMessage(content = "修改FC_ONLINE_LEDGER",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcOnlineLedgerVO fcOnlineLedger){
		FcOnlineLedgerVO t = fcOnlineLedgerService.getFcOnlineLedgerById(fcOnlineLedger.getId());
		BeanUtils.copyNotNullProperties(fcOnlineLedger, t);
		fcOnlineLedgerService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_ONLINE_LEDGER
	* @param id
	* @return
	*/
	@LogMessage(content = "删除FC_ONLINE_LEDGER",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getFcOnlineLedgerById(id);
		fcOnlineLedgerService.deleteEntity(fcOnlineLedger);
		return success();
	}
	/**
	* 批量删除FC_ONLINE_LEDGER
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除FC_ONLINE_LEDGER",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getFcOnlineLedgerById(id);
			fcOnlineLedgerService.deleteEntity(fcOnlineLedger);
		}
		return success();
	}
	/**
	 * 根据银行账号得到内部账户信息
	 * @param bankAcc	银行账户
	 * @author zuoy 2017-09-01
	 * @return
	 */
	@RequestMapping(params="getByBankAcc")
	@ResponseBody
	public AjaxJson getByBankAcc(String bankAcc, String curId){
		String blnctrAcc = this.btBankAccService.getBlnctrAcc(bankAcc, curId);
		AjaxJson json = new AjaxJson();
		FcOnlineLedgerDTO dto = this.fcOnlineLedgerService.getByBlnctrAcc(blnctrAcc);
		json.setSuccess(EmptyUtils.isEmpty(dto) ? false : true);
		json.setObj(dto);
		return json;
	}
}
