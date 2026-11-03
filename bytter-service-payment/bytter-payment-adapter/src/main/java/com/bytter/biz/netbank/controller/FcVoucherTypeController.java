package com.bytter.biz.netbank.controller;
import java.util.ArrayList;
import java.util.List;
import com.bytter.core.constant.CommonConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
/**
 * @Title: Controller
 * @Description: 自定义付款方式表
 * @author onlineGenerator
 * @date 2017-05-12 17:18:41
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/netbank/fcvouchertype")
public class FcVoucherTypeController extends BaseController {
	@Autowired
	private FcVoucherTypeService fcVoucherTypeService;
	/**
	* 自定义付款方式表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "查询自定义付款方式表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listFcVoucherType";
	}
    /**
    * 自定义付款方式表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcVoucherTypeService.getDataTablesUnion(dataTablesInput);
    }
	/**
	* 自定义付款方式表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@GetMapping("/add")
	public String add(FcVoucherType fcVoucherType, Model model) {
		if (StringUtils.isNotEmpty(fcVoucherType.getId())) {
			fcVoucherType = fcVoucherTypeService.get(fcVoucherType.getId());
			model.addAttribute("fcVoucherTypePage", fcVoucherType);
		}
		return "netbank/addFcVoucherType";
	}
	/**
	* 自定义付款方式表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@GetMapping("/update")
	public String update(FcVoucherType fcVoucherType, Model model) {
		if (StringUtils.isNotEmpty(fcVoucherType.getId())) {
			fcVoucherType = fcVoucherTypeService.getById(fcVoucherType.getId());
			model.addAttribute("fcVoucherTypePage", fcVoucherType);
		}
		return "netbank/updateFcVoucherType";
	}
	/**
	* 自定义付款方式表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "查询自定义付款方式表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcVoucherTypeVO fcVoucherType = fcVoucherTypeService.getByIdUnion(id);
		model.addAttribute("fcVoucherTypePage", fcVoucherType);
		return "netbank/viewFcVoucherType";
	}
	/**
	 * 添加自定义付款方式表
	 * @param fcVoucherType
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "添加自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcVoucherType fcVoucherType){
		if (fcVoucherTypeService.findByCode(fcVoucherType.getVoucherType()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_VOUCHERTYPE_UNIQUE);
        }
		if (fcVoucherTypeService.getByVoucherName(fcVoucherType.getVoucherName()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_VOUCHERTYPENAME_UNIQUE);
        }
		if (StringUtils.isEmpty(fcVoucherType.getSysVoucherCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_SYSVOUCHERCODE_NULL);
        }
        if(StringUtils.isEmpty(fcVoucherType.getSendMode())){
            fcVoucherType.setSendMode("0");
        }
		this.setFcVocherType(fcVoucherType);
		fcVoucherType.setCreateBy(this.getCurrentUser().getLoginName());
		fcVoucherType.setCreateDate(DateUtils.getDate());
		this.fcVoucherTypeService.saveFcVoucherType(fcVoucherType, LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	* 修改自定义付款方式表
	* @param fcVoucherType
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "修改自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcVoucherType fcVoucherType){
		if (StringUtils.isEmpty(fcVoucherType.getSysVoucherCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_VOUTYPE_MESSAGE_SYSVOUCHERCODE_NULL);
        }
        if(StringUtils.isEmpty(fcVoucherType.getSendMode())){
            fcVoucherType.setSendMode("0");
        }
		FcVoucherType t = fcVoucherTypeService.get(fcVoucherType.getId(), true);
		BeanUtils.copyNotNullProperties(fcVoucherType, t);
		this.setFcVocherType(t);
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		this.fcVoucherTypeService.updateFcVoucherType(t, LocaleContextHolder.getLocale().toString());
		return  success();
	}
	/**
	* 删除自定义付款方式表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcVoucherType fcVoucherType = fcVoucherTypeService.get(id);
		fcVoucherTypeService.deleteFcVoucherType(fcVoucherType);
		return success();
	}
	/**
	* 批量删除自定义付款方式表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
	@LogMessage(content = "批量删除自定义付款方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids") String ids){
		this.fcVoucherTypeService.batchDelete(ids);
		return success();
	}
	@GetMapping("/json")
    @ResponseBody
    public List<FcVoucherTypeDTO> json(){
        return fcVoucherTypeService.getFcVoucherTypeForAssign();
    }
	/**
	 * 获取付款方式信息
	 * @param id
	 * @return
	 */
	@RequestMapping(params = "getFcVoucherType")
	@ResponseBody
	public AjaxJson getFcVoucherType(String id){
		AjaxJson j=new AjaxJson();
		j.setSuccess(true);
		j.setObj(fcVoucherTypeService.getByIdUnion(id));
		return j;
	}
	/**
	 * 直营付款只过滤代付，转付，拨付，代理支付
	 * @return
	 */
	@GetMapping("/specialVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> specialVoucherType (){
		List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
		List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
		for(FcVoucherTypeDTO type : listAll){
			if(NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_10.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		return listSpecialVoucherType;
    }
	/**
	 * 费用报销查询有效的付款方式-过滤系统结算方式为转付、代付、外付网银、代理支付
	 * @param
	 * @return
	 */
	@GetMapping("/getFcVoucherTypeFilter")
	@ResponseBody
	public List<FcVoucherTypeDTO> getFcVoucherTypeFilter(){
		List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
		List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
		for(FcVoucherTypeDTO type : listAll){
			if(NetbankConstant.PAY_TYPE_02.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_33.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_35.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		return listSpecialVoucherType;
	}
	/**
	 * 付款方式设置
	 * @param fcVoucherType
	 * @return
	 */
	private FcVoucherType setFcVocherType(FcVoucherType fcVoucherType){
		if(NetbankConstant.PAY_TYPE_34.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_48.equals(fcVoucherType.getSysVoucherCode())){
			//外付网银，默认走银企直连且不记账，不允许修改
			fcVoucherType.setIsElectronPay(CommonConstant.YES);
			fcVoucherType.setIsAccount(CommonConstant.NO);
		}else if(NetbankConstant.PAY_TYPE_05.equals(fcVoucherType.getSysVoucherCode())){
			//内转，默认不走银企直连且记账，不允许修改
			fcVoucherType.setIsElectronPay(CommonConstant.NO);
			fcVoucherType.setIsAccount(CommonConstant.YES);
		}else if(NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_50.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_06.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_62.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_63.equals(fcVoucherType.getSysVoucherCode())
				|| NetbankConstant.PAY_TYPE_07.equals(fcVoucherType.getSysVoucherCode())){
			//应收/付票据，默认票据，不走银企直连且不记账
			fcVoucherType.setIsElectronPay(CommonConstant.NO);
			fcVoucherType.setIsAccount(CommonConstant.NO);
		}else if(NetbankConstant.PAY_TYPE_04.equals(fcVoucherType.getSysVoucherCode())){
			//现付，默认不走银企直连，不允许修改
			fcVoucherType.setIsElectronPay(CommonConstant.NO);
		}
		//系统初始化的付款方式，有效标志默认是，不允许修改为否
		/*if(CommonConstant.YES.equals(fcVoucherType.getIsSystem())){
			fcVoucherType.setValidSign(CommonConstant.YES);
		}*/
		return fcVoucherType;
	}
	/**
	 * 功能介绍 : 获取有效的付款方式。<br>
	 * <b>日期：</b>2017年12月19日<br>
	 * @version 1.0
	 */
	@RequestMapping("/getValidVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> getValidVoucherType(){
        return fcVoucherTypeService.getValidFcVoucherType();
    }
	/**
	 * 获取挂资金计划的付款方式
	 * @return
	 */
	@GetMapping("/getVoucherTypeForBudget")
	@ResponseBody
	public List<FcVoucherTypeDTO> getVoucherTypeForBudget(){
		return fcVoucherTypeService.getVoucherTypeForBudget();
	}
	/**
	 * 直营付款只过滤转收，拨付，内转，其他
	 * @return
	 */
	@GetMapping("/getValidVoucherTypeForPool")
	@ResponseBody
	public List<FcVoucherTypeDTO> getValidVoucherTypeForPool (){
		List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
		List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
		for(FcVoucherTypeDTO type : listAll){
			if(NetbankConstant.PAY_TYPE_01.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_10.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_05.equals(type.getSysVoucherCode())
					|| NetbankConstant.PAY_TYPE_11.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		return listSpecialVoucherType;
	}
	/**
	 * 获取挂资金计划的付款方式
	 * @return
	 */
	@GetMapping("/getVoucherTypeForErp")
	@ResponseBody
	public List<FcVoucherTypeDTO> getVoucherTypeForErp(){
		return fcVoucherTypeService.getVoucherTypeForErp();
	}
	/**
	 * 外汇管理获取有效的付款方式-过滤系统结算方式为外付网银
	 * @param
	 * @return
	 */
	@GetMapping("/getFcVoucherTypeForForeign")
	@ResponseBody
	public List<FcVoucherTypeDTO> getFcVoucherTypeForForeign(){
		List<FcVoucherTypeDTO> listSpecialVoucherType = new ArrayList<FcVoucherTypeDTO>();
		List<FcVoucherTypeDTO> listAll = fcVoucherTypeService.getFcVoucherTypeAll();
		for(FcVoucherTypeDTO type : listAll){
			if(NetbankConstant.PAY_TYPE_34.equals(type.getSysVoucherCode())){
				listSpecialVoucherType.add(type);
			}
		}
		return listSpecialVoucherType;
	}
	/**
	 * 查询所有有效供付款方式分配的付款方式（内部账号流水查询）
	 * @return
	 */
	@GetMapping("/getFcVoucherTypeForReport")
	@ResponseBody
	public List<FcVoucherTypeDTO> getFcVoucherTypeForReport(){
		return fcVoucherTypeService.getFcVoucherTypeForReport();
	}
	/**
	 * 查询所有有效且生成指令的的付款方式
	 * @return
	 */
	@GetMapping("/getValidVoucherTypeForExc")
	@ResponseBody
	public List<FcVoucherTypeDTO> getValidVoucherTypeForExc(){
		return fcVoucherTypeService.getValidVoucherTypeForExc();
	}
}
