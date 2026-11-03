package com.bytter.system.saas.controller.bankacc;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtNetUKeyBankAcc;
import com.bytter.system.service.BtBankAccService;
import com.bytter.system.service.BtNetUKeyBankAccService;
import com.bytter.system.service.BtNetUKeyService;
import com.bytter.system.vo.BtNetUKeyVO;
import com.bytter.system.vo.v1.BtNetUKeyPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qinl
 * @version 1.0
 * @Title: Controller
 * @Description: BT_NET_UKEY
 * @date 2020-04-15
 */
@Api(value = "网银信息维护", tags = "网银信息维护")
@RestController("v1BtNetUKeyController")
@RequestMapping("/v1-system/bankacc/btnetukey")
public class BtNetUKeyController extends BaseController {
	@Autowired
	private BtNetUKeyService btNetUKeyService;
	@Autowired
	private BtNetUKeyBankAccService btNetUKeyBankAccService;
	@Autowired
	private BtBankAccService btBankAccService;
	/**
	 * 数据集
	 *
	 * @return
	 */
	@ApiOperation("网银信息维护 table列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BtNetUKeyVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(btNetUKeyService.getDataTablesUnion(input,this.getCurrentUser()));
	}
	/**
	 * BT_BANK_ACC编辑页面跳转
	 *
	 * @return
	 */
	@ApiOperation("网银信息维护编辑页面数据获取")
	@GetMapping("/update/{id}")
	@ResponseBody
	public AjaxJson<BtNetUKeyPageVO> update(@PathVariable String id) {
		BtNetUKeyBankAcc btNetUKeyBankAcc = btNetUKeyBankAccService.get(id);
		BtNetUKeyVO btNetUKey = btNetUKeyService.getVOById(btNetUKeyBankAcc.getuKeyId());
		//获取所有的账号ID
		List<BtNetUKeyBankAcc> listByUKeyId = btNetUKeyBankAccService.getListByUKeyId(btNetUKeyBankAcc.getuKeyId());
		StringBuilder bankIds = new StringBuilder();
		for(BtNetUKeyBankAcc entity:listByUKeyId){
			bankIds.append(entity.getBankAccId()).append(",");
		}
		BtNetUKeyPageVO pageVO = new BtNetUKeyPageVO();
		btNetUKey.setBankAccIds(bankIds.toString());
		pageVO.setVo(btNetUKey);
		pageVO.setBankAcc(bankIds.toString());
		return success(pageVO);
	}
	/**
	 * BT_BANK_ACC详情页
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY)
	@ApiOperation("网银信息维护查看页面数据获取")
	@GetMapping("/view/{id}")
	@ResponseBody
	public AjaxJson<BtNetUKeyPageVO> view(@PathVariable String id) {
		BtNetUKeyBankAcc btNetUKeyBankAcc = btNetUKeyBankAccService.get(id);
		BtNetUKeyVO btNetUKey = btNetUKeyService.getVOById(btNetUKeyBankAcc.getuKeyId());
		BtBankAcc btBankAcc = btBankAccService.get(btNetUKeyBankAcc.getBankAccId());
		BtNetUKeyPageVO pageVO = new BtNetUKeyPageVO();
		pageVO.setVo(btNetUKey);
		pageVO.setBankAcc(btBankAcc.getBankAcc());
		return success(pageVO);
	}
	/**
	 * 保存操作
	 *
	 * @param btNetUKeyVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@ApiOperation("网银信息维护保存")
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BtNetUKeyVO btNetUKeyVO) {
		btNetUKeyService.save(btNetUKeyVO);
		return success();
	}
	/**
	 * 修改数据
	 *
	 * @param btNetUKeyVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@ApiOperation("网银信息维护修改")
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(@RequestBody BtNetUKeyVO btNetUKeyVO) {
		btNetUKeyService.modify(btNetUKeyVO,btNetUKeyVO.getuKeyNoCheck());
		return success();
	}
	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@ApiOperation("删除网银信息")
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		btNetUKeyService.delete(id,this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@ApiOperation("批量删除网银信息")
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			btNetUKeyService.delete(id,this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@ApiOperation("保存导入数据")
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtNetUKeyVO> list) {
		this.btNetUKeyService.saveImport(list, this.getCurrentUser());
		return success();
	}
}
