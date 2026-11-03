package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.receipt.entity.EVisaType;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 签章类型维护
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Api(value = "/v1-payment/evisatype/evisatype",tags = "电子签章类型")
@RestController("v1EVisaTypeController")
@RequestMapping("/v1-payment/evisatype/evisatype")
public class EVisaTypeController extends BaseController {
	@Autowired
	private EVisaTypeService eVisaTypeService;

    /**
    * 签章类型维护 table列表
    *
    * @return
    */
	@ApiOperation("签章类型维护 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(eVisaTypeService.findListBysql(input, this.getCurrentUser()));
    }

	/**
	* 签章类型维护编辑页面跳转
	*
	* @return
	*/
	@ApiOperation("签章类型维护编辑页面跳转")
	@GetMapping("/update/{id}")
	public AjaxJson<EVisaType> update(EVisaType eVisaType) {
		if (StringUtils.isNotEmpty(eVisaType.getId())) {
			return AjaxJson.success(eVisaTypeService.getById(eVisaType.getId()));
		}
		return AjaxJson.success(eVisaType);
	}
	/**
	 * 操作签章类型
	 * @param id
	 * @param opType 0 作废 1 恢复
	 * @return
	 */
	@ApiOperation("操作签章类型")
	@LogMessage(content = "作废签章类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/operate")
	public AjaxJson operate(String id,String opType){
		if("0".equals(opType)){
			eVisaTypeService.updateOperate(id,"0");
		}else{
			eVisaTypeService.updateOperate(id,"1");
		}
		return success();
	}
	/**
	* 签章类型维护详情页
	*
	* @return
	*/
	@ApiOperation("签章类型维护详情页")
	@LogMessage(content = "查询签章类型维护详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<EVisaTypeVO> view(@PathVariable("id") String id) {
		return AjaxJson.success( eVisaTypeService.getByIdUnion(id));
	}
	/**
	 * 添加签章类型维护
	 * @param eVisaType
	 * @return
	 */
	@ApiOperation("添加签章类型维护")
	@LogMessage(content = "添加签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	public AjaxJson save(@RequestBody EVisaType eVisaType){
		eVisaTypeService.save(eVisaType,getCurrentUser());
		return success();
	}
	/**
	* 修改签章类型维护
	* @param eVisaType
	* @return
	*/
	@ApiOperation("修改签章类型维护")
	@LogMessage(content = "修改签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	public AjaxJson modify(@RequestBody EVisaType eVisaType){
		EVisaType t = eVisaTypeService.getById(eVisaType.getId());
		BeanUtils.copyNotNullProperties(eVisaType, t);
		t.setUpdateBy(getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		eVisaTypeService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除签章类型维护
	* @param id
	* @return
	*/
	@ApiOperation("删除签章类型维护")
	@LogMessage(content = "删除签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	public AjaxJson delete(@PathVariable("id") String id){
		EVisaType eVisaType = eVisaTypeService.get(id);
		eVisaTypeService.deleteEntity(eVisaType);
		return success();
	}
	/**
	* 批量删除签章类型维护
	* @param ids
	* @return
	*/
	@ApiOperation("批量删除签章类型维护")
	@LogMessage(content = "批量删除签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			EVisaType eVisaType = eVisaTypeService.get(id);
			eVisaTypeService.deleteEntity(eVisaType);
		}
		return success();
	}

}
