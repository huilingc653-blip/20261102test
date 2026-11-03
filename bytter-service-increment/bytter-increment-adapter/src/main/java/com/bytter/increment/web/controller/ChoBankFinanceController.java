package com.bytter.increment.web.controller;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.service.ChoBankFinanceService;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 银行理财表
 * @author onlineGenerator
 * @date 2017-08-18 14:03:30
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/chowmatistic/chobankfinance")
public class ChoBankFinanceController extends BaseController {
	@Resource
	private ChoBankFinanceService choBankFinanceService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IBisAccDtlService bisAccDtlService;
	@Autowired(required = false)
	private IBtBankAccService btBankAccService;
	/**
	* 银行理财表列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询银行理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{busType}")
	public String list(@PathVariable("busType") Integer busType, Model model) {
		model.addAttribute("busType", busType);
        return "chowmatistic/listChoBankFinance" + this.getForwardByBusType(busType);
	} 
    /**
    * 银行理财表 table列表
    *
    * @return
    */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return choBankFinanceService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    
    /**
	* 银行理财表列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询银行理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listBl/{busType}")
	public String listBl(@PathVariable("busType") Integer busType, Model model) {
		model.addAttribute("busType", busType);
        return "chowmatistic/listChoBankFinanceBl";
	}
	/**
	* 银行理财表新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add/{busType}")
	public String add(@PathVariable("busType") Integer busType,ChoBankFinance choBankFinance, Model model) {
		if (StringUtils.isNotEmpty(choBankFinance.getId())) {
			choBankFinance = choBankFinanceService.get(choBankFinance.getId());
			model.addAttribute("choBankFinancePage", choBankFinance);
		}
		model.addAttribute("busType", busType);
		model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
		//初始化参数
		this.initConfig(model, busType);
		return "chowmatistic/addChoBankFinance" + this.getForwardByBusType(busType);
	}
	/**
	* 银行理财表编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(ChoBankFinanceVO choBankFinance, Model model) {
		if (StringUtils.isNotEmpty(choBankFinance.getId())) {
			choBankFinance = choBankFinanceService.getByIdUnion(choBankFinance.getId());
			model.addAttribute("choBankFinancePage", choBankFinance);
		}
		model.addAttribute("sysDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
		//初始化参数
		this.initConfig(model, choBankFinance.getBusType());
		if(BankFinanceConstant.STATUE_CHECK.equals(choBankFinance.getStatus())){
			//展示获取账号的银行类别和购买银行
			BtBankAccDTO btBankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(choBankFinance.getPayerAcc(),choBankFinance.getCurId());
			if(!EmptyUtils.isEmpty(btBankAccDTO)){
				model.addAttribute("bankTypeId", btBankAccDTO.getBankTypeId());
				model.addAttribute("bankName", btBankAccDTO.getBankName());
			}
			return "chowmatistic/blChoBankFinance";
		}else{
			return "chowmatistic/updateChoBankFinance" + this.getForwardByBusType(choBankFinance.getBusType());
		}
	}
    /**
     * 获取账号-带银行类别和购买银行筛选
     * @param corpId
     * @param accType
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAccByAccType")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccByAccType(String corpId,String bankTypeId,String bankName,String accType){
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if(!StringUtils.isEmpty(corpId)){
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if(!EmptyUtils.isEmptyList(listOnlineBankAcc)){
                listAcc.addAll(listOnlineBankAcc);
            }
            if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        //过滤定期账号
        //过滤银行类别
        //过滤购买银行
        List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
        if(!EmptyUtils.isEmptyList(listAcc)){
            for(BtBankAccDTO dto: listAcc){
                if(accType.equals(dto.getAccType())){
                    if(StringUtils.isNotBlank(bankTypeId)){
                        if(bankTypeId.equals(dto.getBankTypeId())){
                            if(StringUtils.isNotBlank(bankName)){
                                if(bankName.equals(dto.getBankName())){
                                    listAccResult.add(dto);
                                }
                            }else{
                                listAccResult.add(dto);
                            }
                        }
                    }else{
                        listAccResult.add(dto);
                    }
                }
            }
        }
        return listAccResult;
    }

	
	/**
	 * 初始化系统参数
	 * @param model
	 * @param busType
	 */
	private void initConfig(Model model, Integer busType){
		if(BankFinanceConstant.BUS_TYPE_C2T.equals(busType)){
			//活期转定期
			model.addAttribute("regularOnset", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARONSET).getValue());
			model.addAttribute("regularKeep", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARKEEP).getValue());
		}else if(BankFinanceConstant.BUS_TYPE_C2N.equals(busType)){
			//活期转通知
			model.addAttribute("noticeOnset", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEONSET).getValue());
			model.addAttribute("noticeKeep", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEKEEP).getValue());
		}else if(BankFinanceConstant.BUS_TYPE_T2C.equals(busType)){
			//定期转活期
			model.addAttribute("regularNum", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARNUM).getValue());
		}else if(BankFinanceConstant.BUS_TYPE_N2C.equals(busType)){
			//通知转活期
			model.addAttribute("noticeMinLimit", configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEMINLIMIT).getValue());
		}
		//是否启用限额管理  1-是 0-否
		model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS,SysConfigConstant.ISOPENLIMIT).getValue());
	}
	/**
	* 银行理财表详情页
	*
	* @return
	*/
	@LogMessage(content = "查询银行理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		ChoBankFinanceVO choBankFinance = choBankFinanceService.getByIdUnion(id);
		model.addAttribute("choBankFinancePage", choBankFinance);
		model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_BANK);
		return "chowmatistic/viewChoBankFinance";
	}
	/**
	 * 添加银行理财表
	 * @param choBankFinance
	 * @return
	 */
	@LogMessage(content = "添加银行理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(ChoBankFinanceVO choBankFinance){
		choBankFinanceService.saveBankFinance(choBankFinance, this.getCurrentUser());
		return success();
	}
	/**
	* 修改银行理财表
	* @param choBankFinance
	* @return
	*/
	@LogMessage(content = "修改银行理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(ChoBankFinanceVO choBankFinance){
		choBankFinanceService.updateBankFinance(choBankFinance, this.getCurrentUser());
		return  success();
	}
	/**
	* 删除银行理财表
	* @param id
	* @return
	*/
	@LogMessage(content = "删除银行理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		choBankFinanceService.delChoBankFinance(id, this.getCurrentUser());
		return success();
	}
	/**
	* 批量删除银行理财表
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除银行理财表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			choBankFinanceService.delChoBankFinance(id, this.getCurrentUser());
		}
		return success();
	}
	
	/**
	 * 根据业务类型返回不同的跳转页面
	 * @param busType
	 * @return
	 */
	public String getForwardByBusType(Integer busType){
		/**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期*/
		String forward = "";
		if(BankFinanceConstant.BUS_TYPE_C2T.equals(busType)){
			forward = "Current2Time";
		}else if(BankFinanceConstant.BUS_TYPE_T2C.equals(busType)){
			forward = "Time2Current";
		}else if(BankFinanceConstant.BUS_TYPE_C2N.equals(busType)){
			forward = "Current2Notice";
		}else if(BankFinanceConstant.BUS_TYPE_N2C.equals(busType)){
			forward = "Notice2Current";
		}
        return forward;
	}
	
	/**
	 * 获取账号
	 * @param corpId
	 * @param busType
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId,Integer busType){
    	List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
    	 if(!StringUtils.isEmpty(corpId)){
    	 	//获取转账权限的银行在线账号
    	 	List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
    	 	//获取转账权限的银行离线账号
    	 	List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
    	 	if(!EmptyUtils.isEmptyList(listOnlineBankAcc)){
    	 		listAcc.addAll(listOnlineBankAcc);
    	 	}
    	 	if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
    	 		listAcc.addAll(listOfflineBankAcc);
    	 	}
    	 }
    	//过滤活期账号
    	List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
    	if(!EmptyUtils.isEmptyList(listAcc)){
    		for(BtBankAccDTO dto: listAcc){
    			if(BtBankAccConstant.ACCTYPE_01.equals(dto.getAccType())){
    				listAccResult.add(dto);
    			} else if(BtBankAccConstant.ACCTYPE_10.equals(dto.getAccType())){
					listAccResult.add(dto);
				}
    		}
    	}
    	return listAccResult;
    }
    
    /**
	 * 获取账号（补录列表界面获取离线账号）
	 * @param corpId
	 * @param busType
	 * @return
	 */
    @SuppressWarnings("unchecked")
	@PostMapping("/getOfflineBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getOfflineBankAcc(String corpId,Integer busType){
    	List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
    	if(!StringUtils.isEmpty(corpId)){
			//获取转账权限的银行在线账号
			List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
			if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
				listAcc.addAll(listOnlineBankAcc);
			}

    		//获取转账权限的银行离线账号
    		List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
    		if(!EmptyUtils.isEmptyList(listOfflineBankAcc)){
    			listAcc.addAll(listOfflineBankAcc);
    		}
    	}
    	//过滤活期账号
    	List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
    	if(!EmptyUtils.isEmptyList(listAcc)){
    		for(BtBankAccDTO dto: listAcc){
    			if(BtBankAccConstant.ACCTYPE_01.equals(dto.getAccType())){
    				listAccResult.add(dto);
    			}
    		}
    	}
    	return listAccResult;
    }
	/**
	 * 跳转至审核列表
	 * @param busType	业务类型
	 * @return
	 */
	@GetMapping("/listApprove/{busType}")
	public String listApprove(@PathVariable Integer busType, String flowStatus, String cycleTime, Model model){
		model.addAttribute("busType", busType);
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "chowmatistic/listChoBankFinance" + this.getForwardByBusType(busType) + "Approve";
	}
	/**
	 * 加载审核列表数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody  DataTablesInput dataTablesInput){
		return this.choBankFinanceService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 * @param id
	 * @param taskId
	 * @param model
	 * @return
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		ChoBankFinanceVO vo = new ChoBankFinanceVO();
		if (StringUtils.isNotEmpty(id)) {
			vo = this.choBankFinanceService.getByIdUnion(id);
			model.addAttribute("choBankFinancePage", vo);
			model.addAttribute("taskId", taskId);
			model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_BANK);
		}
		return "chowmatistic/approveChobankfinance";
	}
	/**
	 * 跳转至手机审核详情界面
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			ChoBankFinanceVO vo = this.choBankFinanceService.getByIdUnion(businessId);
			if(EmptyUtils.isEmpty(vo.getBalance())) {
				vo.setBalance(BigDecimal.ZERO);
			}
			if(EmptyUtils.isEmpty(vo.getBlInterest())) {
				vo.setBlInterest(BigDecimal.ZERO);
			}
			if(EmptyUtils.isEmpty(vo.getBlInterestRate())) {
				vo.setBlInterestRate(BigDecimal.ZERO);
			}
			model.addAttribute("choBankFinancePage", vo);
			model.addAttribute("taskId", taskId);
			model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_BANK);
		}
		return "chowmatistic/viewApproveChoBankFinance";
	}
	/**
	 * 存款类业务查询 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "查询银行理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listQuery")
	public String listQuery(Model model) {
		return "chowmatistic/listChoBankFinanceQuery";
	}
	/**
	 * 存款类业务查询 table列表
	 *
	 * @return
	 */
	@PostMapping("/dataTableQuery")
	@ResponseBody
	public DataTablesOutput dataTableQuery(@RequestBody  DataTablesInput dataTablesInput){
		return choBankFinanceService.getDataTablesQuery(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 银行理财表详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询银行理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewQuery/{id}")
	public String viewQuery(@PathVariable("id") String id, Model model) {
		ChoBankFinanceVO choBankFinance = choBankFinanceService.getByIdUnion(id);
		model.addAttribute("choBankFinancePage", choBankFinance);
		model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_BANK);
		return "chowmatistic/viewChoBankFinanceQuery";
	}
	/**
	 * 银行存款应收未收利息统计表 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "银行存款应收未收利息统计表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listReport")
	public String listReport(Model model) {
		return "chowmatistic/listChoBankFinanceReport";
	}
	/**
	 * 银行存款应收未收利息统计表
	 *
	 * @return
	 */
	@PostMapping("/dataTableReport")
	@ResponseBody
	public DataTablesOutput dataTableReport(@RequestBody  DataTablesInput dataTablesInput){
		return choBankFinanceService.getDataTablesReport(dataTablesInput, this.getCurrentUser());
	}
	/**流水认领组件 begin*/
	@GetMapping("/mulSelectBankFinancePage")
	public String mulSelectBankFinancePage(String corpId,String busType,String id,String row,Model model) {
		if(StringUtils.isNotBlank(corpId)){
			CorpDTO corpDTO = iCorpService.get(corpId);
			model.addAttribute("corp",corpDTO);
			model.addAttribute("busType",busType);
			model.addAttribute("row",row);
		}
		if(StringUtils.isNotBlank(id)){
			BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
			if(StringUtils.isNotEmpty(bisAccDtlDTO)){
				// 获取交易日期
				String date = DateUtils.date2Str(bisAccDtlDTO.getTransTime(), DateUtils.date_sdf());
				model.addAttribute("endDate",date);
				Date dateStart = DateUtils.addDay(bisAccDtlDTO.getTransTime(), -30);
				model.addAttribute("startDate",DateUtils.date2Str(dateStart, DateUtils.date_sdf()));
				// 获取交易金额
				model.addAttribute("dto", bisAccDtlDTO);
			}
		}
		return "chowmatistic/mulSelectBankFinancePage";
	}
	@PostMapping("/datatableForSelect")
	@ResponseBody
	public DataTablesOutput dataTableForSelect(@RequestBody  DataTablesInput dataTablesInput){
		return choBankFinanceService.getDataTablesUnionForSelect(dataTablesInput, this.getCurrentUser());
	}
	/**流水认领组件 end*/
}
