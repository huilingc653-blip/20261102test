package com.bytter.biz.bond.controller;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondTypeService;
import com.bytter.guarantee.vo.FcBondTypeVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: 保证金类型维护列表
 */
@Controller
@RequestMapping("/bond/fcbondtype")
public class FcBondTypeController extends BaseController {
	@DubboReference(check = false)
	private IFcBondTypeService fcBondTypeService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	/**
	* 保证金类型维护列表 页面跳转
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@LogMessage(content = "查询保证金类型维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bond/listBondType";
	}
    /**
    * 保证金类型维护列表 table列表
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return fcBondTypeService.bondTypeList(dataTablesInput);
    }
	/**
	* 保证金类型维护 新增页面跳转
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@GetMapping("/add")
	public String add(FcBondTypeVO fcBondType, Model model) {
		if (StringUtils.isNotEmpty(fcBondType.getId())) {
			fcBondType= fcBondTypeService.getBondType(fcBondType.getId());
			model.addAttribute("fcBondTypePage", fcBondType);
		}
		return "bond/addBondType";
	}
	/**
	* 保证金类型维护 编辑页面跳转
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@GetMapping("/update")
	public String update(FcBondTypeVO fcBondType, Model model) {
		if (StringUtils.isNotEmpty(fcBondType.getId())) {
			fcBondType = fcBondTypeService.getBondType(fcBondType.getId());
			model.addAttribute("fcBondTypePage", fcBondType);
		}
		return "bond/updateBondType";
	}
	/**
	* 保证金类型维护详情页
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcBondTypeVO fcBondTypeVO = fcBondTypeService.getBondType(id);
		model.addAttribute("fcBondTypePage", fcBondTypeVO);
		return "bond/viewBondType";
	}
	/**
	 * 保存保证金类型
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@LogMessage(content = "保存保证金类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBondTypeVO fcBondType){
		fcBondTypeService.save(fcBondType,getUserId());
		return success();
	}
	/**
	* 修改保证金类型
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@LogMessage(content = "修改保证金类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBondTypeVO fcBondType){
		fcBondTypeService.saveOrUpdate(fcBondType,getUserId());
		return  success();
	}
	/**
	* 删除保证金类型
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@LogMessage(content = "删除保证金类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcBondTypeVO fcBondTypeVO = new FcBondTypeVO();
		fcBondTypeVO.setId(id);
		fcBondTypeService.deleteEntity(fcBondTypeVO);
		return success();
	}
	/***
	 * 获取有效保证金类型
	 * @return
	 */
	////////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDTYPE_APPLY)
	@GetMapping("/getBondType")
	@ResponseBody
	public List<FcBondTypeVO> json(){
		return fcBondTypeService.getAllBondType();
	}
	/**
	 * 获取转出账号：活期、票据活期两类
	 */
	@PostMapping("/getOutBankAcc")
	@ResponseBody
	public List<BtBankAccDTO> getOutBankAcc(String corpId,String curId) {
		List<BtBankAccDTO> bankAccs = new ArrayList<BtBankAccDTO>();
		if (StringUtils.isNotEmpty(corpId)) {
			bankAccs = iBtBankAccService.getBankAccByUserIdCurId(getUserId(),corpId,curId,"01,10");
			List<String > countAccS=new ArrayList<>();
			for(BtBankAccDTO btBankAccDTO:bankAccs) {
				countAccS.add(btBankAccDTO.getCounterAccId());

			}
			List<FcOnlineLedgerDTO> fcOnlineLedgerDTOList=fcOnlineLedgerService.getFcOnlineLedgerByAccList(countAccS);
			Map<String, FcOnlineLedgerDTO> fcOnlineLedgerDTOMap=new HashMap<>();
			for(FcOnlineLedgerDTO fcOnlineLedgerDTO:fcOnlineLedgerDTOList){
				fcOnlineLedgerDTOMap.put(fcOnlineLedgerDTO.getAccount(),fcOnlineLedgerDTO);
			}

			for(BtBankAccDTO btBankAccDTO:bankAccs) {//fcBalance fc.check_Balance - fc.freeze_Money
				FcOnlineLedgerDTO fcOnlineLedgerDTO= fcOnlineLedgerDTOMap.get(btBankAccDTO.getCounterAccId());
				if(null != fcOnlineLedgerDTO) {
					btBankAccDTO.setFcBalance(fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney()));
				}else{
					btBankAccDTO.setFcBalance(BigDecimal.ZERO);
				}
				BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
				if (null != bisAccBalDTO) {
					btBankAccDTO.setBalance(bisAccBalDTO.getAvailBal());
				}else {
					btBankAccDTO.setBalance(BigDecimal.ZERO);
				}

			}

		}
		return bankAccs;
	}
}
