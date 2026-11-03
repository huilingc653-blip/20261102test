package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.inneraccount.dto.v1.FcInitAccCheckDTO;
import com.bytter.biz.inneraccount.dto.v1.FcInitAccSearchDTO;
import com.bytter.biz.inneraccount.entity.FcInitAcc;
import com.bytter.biz.inneraccount.service.FcInitAccService;
import com.bytter.biz.inneraccount.vo.FcInitAccListVO;
import com.bytter.biz.inneraccount.vo.FcInitAccVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 内部账号初始化
 * @author onlineGenerator
 * @date 2017-06-12 18:12:02
 * @version 1.0
 *
 */
@Api(value = "内部账号初始化",tags = "内部账号初始化")
@RestController("v1FcInitAccController")
@RequestMapping("/v1-payment/inneraccount/fcinitacc")
public class FcInitAccController extends BaseController {
	@Autowired
	private FcInitAccService fcInitAccService;

    /**
    * 内部账号初始化复核 table列表
    *
    * @return
    */
	@ApiOperation("内部账户复核 初始化")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcInitAccVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcInitAccService.getDataTablesUnion(input,getCurrentUser()));
    }
	/**
	 * 内部账号初始化table列表
	 *
	 * @return
	 */
	@ApiOperation("内部账户申请 初始化")
	@PostMapping("/tableInit")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcInitAccVO>> tableInit(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcInitAccService.tableInit(input,getCurrentUser()));
	}
	/**
	 * 添加内部账号初始化
	 * @param fcInitAccList
	 * @return
	 */
	@ApiOperation("添加内部账号初始化")
	@LogMessage(content = "添加内部账号初始化",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public AjaxJson save(@RequestBody FcInitAccListVO fcInitAccList){
		fcInitAccService.saveFcAccbook(fcInitAccList, CommonConstant.SUBMIT_ADD,getCurrentUser());
		return success();
	}

	@ApiOperation("内部账号初始化复核")
	@LogMessage(content = "内部账号初始化复核",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(@RequestBody FcInitAccCheckDTO fcInitAccCheckDTO){
		fcInitAccService.check(fcInitAccCheckDTO.getIds(),fcInitAccCheckDTO.getStatus(),getCurrentUser());
		return success();
	}
	/**
	* 删除内部账号初始化
	* @param id
	* @return
	*/
	@ApiOperation("删除内部账号初始化")
	@LogMessage(content = "删除内部账号初始化",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcInitAcc fcInitAcc = fcInitAccService.getById(id);
		fcInitAccService.deleteEntity(fcInitAcc,getCurrentUser());
		return success();
	}
	/**
	 * 导入
	 * @param list
	 * @return
	 */
	@ApiOperation("导入")
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FcInitAccVO> list) {
		fcInitAccService.saveImport(list,getCurrentUser());
		return success();
	}
}
