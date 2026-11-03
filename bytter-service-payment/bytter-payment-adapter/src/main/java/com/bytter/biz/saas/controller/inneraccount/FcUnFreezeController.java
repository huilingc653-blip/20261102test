package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.inneraccount.dto.v1.FcUnFreezeSearchDTO;
import com.bytter.biz.inneraccount.service.FcUnFreezeService;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.inneraccount.vo.v1.FcAccUnFreezePageVO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
////import org.apache.shiro.authz.annotation.Logical;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Title: Controller  
 * @Description: 内部账号解冻
 * @author onlineGenerator
 * @date 2017-06-16 17:31:26
 * @version 1.0
 *
 */
@Api(value = "内部账号解冻",tags = "内部账号解冻")
@RestController("v1FcUnFreezeController")
@RequestMapping("/v1-payment/inneraccount/fcUnfreeze")
public class FcUnFreezeController extends BaseController {
	@Autowired
	private FcUnFreezeService fcUnFreezeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
    * 内部账号解冻 table列表
    *
    * @return
    */
	@ApiOperation("内部账号解冻 table列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcFreezeVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcUnFreezeService.findCanModifyUnFreezes(input,getCurrentUser()));
    }
	/**
	 * 内部账号解冻 table列表
	 *
	 * @return
	 */
	@ApiOperation("内部账号解冻复核 table列表")
	@PostMapping("/checkDataTable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcFreezeVO>> checkDataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcUnFreezeService.findCanCheckUnFreezes(input,getCurrentUser()));
	}
	/**
	 * 可解冻记录列表
	 * @return
	 */

	@ApiOperation("可解冻记录列表")
	@PostMapping("/getTableData")
	@ResponseBody
	public AjaxJson<List<FcFreezeListVO>> getTableData(@RequestBody FcUnFreezeSearchDTO fcUnFreezeSearchDTO) {
		String corpId = fcUnFreezeSearchDTO.getCorpId();
		if (StringUtils.isNotEmpty(corpId)) {
			return success(fcUnFreezeService.findCanUnFreezes(corpId, getUserLoginName()));
		}
		return success();
	}
	@ApiOperation("根据id查询冻结信息")
	@ResponseBody
	@GetMapping("/getById")
	public AjaxJson<FcFreezeVO> getById(String id){
		FcFreezeVO fcFreeze = fcUnFreezeService.getById(id);
		return success(fcFreeze);
	}
	/**
	* 内部账号冻结解冻编辑页面跳转
	*
	* @return
	*/
	@ApiOperation("内部账号冻结编辑页面跳转前 数据获取")
	@GetMapping("/updateOrView")
	public AjaxJson<FcAccUnFreezePageVO> updateOrView(FcUnFreezeSearchDTO fcUnFreezeSearchDTO) {
		if (StringUtils.isNotEmpty(fcUnFreezeSearchDTO.getId())) {
			FcAccUnFreezePageVO pageVO = new FcAccUnFreezePageVO();
			FcFreezeVO fcFreeze = new FcFreezeVO();
			fcFreeze.setId(fcUnFreezeSearchDTO.getId());
			fcFreeze.setBlnctrAcc(fcUnFreezeSearchDTO.getBlnctrAcc());
			FcFreezeListVO fcFreezeListVO= fcUnFreezeService.getEntityById(fcFreeze);
			if(fcFreezeListVO == null){
				throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,fcFreeze.getBlnctrAcc());
			}
			pageVO.setFcFreezePage(fcFreezeListVO);
			pageVO.setOpType(fcUnFreezeSearchDTO.getOpType());
			pageVO.setNetCode(corpService.get(fcFreezeListVO.getNetId()).getCode());
			pageVO.setCorpCode(corpService.get(fcFreezeListVO.getCorpId()).getCode());
			return success(pageVO);
		}
		return success();
	}
	/**
	 * 添加内部账号冻结解冻
	 * @param fcFreeze
	 * @return
	 */
	@ApiOperation(value = "添加/编辑内部账号解冻",notes = "新增与编辑都用该接口  \n" +
			"通过 opType 来区别  新增则为A   修改为M\n" +
			"通过 传输对象的status来区别 暂存 和 提交   0为暂存  11为提交")
	@LogMessage(content = "添加/编辑内部账号解冻",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody FcFreezeVO fcFreeze){
		fcUnFreezeService.saveEntity(fcFreeze,getCurrentUser());
		return success();
	}

	@ApiOperation(value = "内部账号解冻复核",
			notes = "复核传对象status 95  打回传对象status-1")
	@LogMessage(content = "内部账号解冻复核", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(@RequestBody FcFreezeVO fcFreeze) {
		fcUnFreezeService.checkUnFreeze(fcFreeze, getCurrentUser());
		return success();
	}

	/**
	* 删除内部账号冻结解冻
	* @param id
	* @return
	*/
	@ApiOperation("删除内部账号冻结解冻")
	@LogMessage(content = "删除内部账号冻结解冻",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcUnFreezeService.delete(id,getCurrentUser());
		return success();
	}
}
