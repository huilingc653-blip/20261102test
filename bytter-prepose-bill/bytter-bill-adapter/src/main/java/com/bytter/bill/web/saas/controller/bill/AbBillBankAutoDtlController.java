package com.bytter.bill.web.saas.controller.bill;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.bill.domain.bill.vo.AbBillBankAutoDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillbankautodtl", tags = "票据银行对于自动业务配置")
@RestController("v1AbBillBankAutoDtlController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillbankautodtl")
public class AbBillBankAutoDtlController extends BaseController {
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
 
    /**
    * 票据银行对于自动业务配置 table列表
    *
    * @return
    */
	@ApiOperation("票据银行对于自动业务配置 table列表")
	@PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<AbBillBankAutoDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
		return AjaxJson.success(abBillBankAutoDtlService.listAbBillBankAutoDtl(input));
    }

    @ApiOperation("根据银行接口类型Id获取数据")
	@GetMapping("/getBillBankAutoDtlByBifCode")
	public AjaxJson<AbBillBankAutoDtl> getBillBankAutoDtlByBifCode(String bifCode){
		return AjaxJson.success(abBillBankAutoDtlService.getDataByBifCode(bifCode));
	}


	/**
	* 票据银行对于自动业务配置维护编辑页面跳转
	*
	* @return
	*/
    @ApiOperation("票据银行对于自动业务配置维护编辑页面跳转")
	@GetMapping("/update/{id}")
	public AjaxJson<AbBillBankAutoDtl> update(@PathVariable String id) {
		if (StringUtils.isNotEmpty(id)) {
           return AjaxJson.success( abBillBankAutoDtlService.get(id));
		}
		return AjaxJson.success();
	}
	/**
	* 票据银行对于自动业务配置维护详情页
	*
	* @return
	*/
    @ApiOperation("票据银行对于自动业务配置维护详情页")
	@LogMessage(content = "查询票据银行对于自动业务配置维护详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<AbBillBankAutoDtl> view(@PathVariable("id") String id) {
		AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
		return AjaxJson.success(abBillBankAutoDtl);
	}
	/**
	 * 添加票据银行对于自动业务配置
	 * @param abBillBankAutoDtl
	 * @return
	 */
    @ApiOperation("添加票据银行对于自动业务配置")
	@LogMessage(content = "添加票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	public AjaxJson save(@RequestBody  AbBillBankAutoDtl abBillBankAutoDtl){
		if (abBillBankAutoDtlService.isExistMulByBifCode(abBillBankAutoDtl.getBifCode())) {
			throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BANKAUTO_BIFCODE_REPEAT);
		}
		// 创建人信息
		if(EmptyUtils.isBlank(abBillBankAutoDtl.getCreateBy())){
			abBillBankAutoDtl.setCreateBy(this.getCurrentUser().getLoginName());
			abBillBankAutoDtl.setCreateDate(DateUtils.getDate());
		}
		abBillBankAutoDtlService.save(abBillBankAutoDtl);
		return success();
	}
	/**
	* 修改票据银行对于自动业务配置
	* @param abBillBankAutoDtl
	* @return
	*/
    @ApiOperation("修改票据银行对于自动业务配置")
	@LogMessage(content = "修改票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	public AjaxJson update(@RequestBody AbBillBankAutoDtl abBillBankAutoDtl){
		AbBillBankAutoDtl t = abBillBankAutoDtlService.get(abBillBankAutoDtl.getId());
		BeanUtils.copyNotNullProperties(abBillBankAutoDtl, t);
		// 修改人信息
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		abBillBankAutoDtlService.saveOrUpdate(t);
		return  success();
	}

	/**
	* 删除票据银行对于自动业务配置
	* @param id
	* @return
	*/
    @ApiOperation("删除票据银行对于自动业务配置")
	@LogMessage(content = "删除票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable("id") String id){
		AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
		abBillBankAutoDtlService.deleteEntity(abBillBankAutoDtl);
		return success();
	}

	/**
	* 批量删除银行类型维护
	* @param ids
	* @return
	*/
    @ApiOperation("批量删除银行类型维护")
	@LogMessage(content = "批量删除票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete/{ids}")
	public AjaxJson deleteDelete(@PathVariable("ids")String ids){
		for(String id : ids.split(",")){
			AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
			abBillBankAutoDtlService.deleteEntity(abBillBankAutoDtl);
		}
		return success();
	}
}
