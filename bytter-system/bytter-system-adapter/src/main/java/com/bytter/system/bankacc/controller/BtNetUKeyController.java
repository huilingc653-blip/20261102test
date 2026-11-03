package com.bytter.system.bankacc.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtNetUKeyBankAcc;
import com.bytter.system.service.BtBankAccService;
import com.bytter.system.service.BtNetUKeyBankAccService;
import com.bytter.system.service.BtNetUKeyService;
import com.bytter.system.vo.BtNetUKeyVO;
/**
 * @author qinl
 * @version 1.0
 * @Title: Controller
 * @Description: BT_NET_UKEY
 * @date 2020-04-15
 */
@Controller
@RequestMapping("/bankacc/btnetukey")
public class BtNetUKeyController extends BaseController {
	@Autowired
	private BtNetUKeyService btNetUKeyService;
	@Autowired
	private BtNetUKeyBankAccService btNetUKeyBankAccService;
	@Autowired
	private BtBankAccService btBankAccService;
	/**
	 * list页面
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY)
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("user",this.getCurrentUser().getUsername());
		return "bankacc/listBtNetUKey";
	}
	/**
	 * 数据集
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btNetUKeyService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
	}
	/**
	 * 新增頁面
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@GetMapping("/add")
	public String add() {
		return "bankacc/addBtNetUKey";
	}
	/**
	 * BT_BANK_ACC编辑页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@GetMapping("/update/{id}")
	public String update(@PathVariable String id, Model model) {
		BtNetUKeyBankAcc btNetUKeyBankAcc = btNetUKeyBankAccService.get(id);
		BtNetUKeyVO btNetUKey = btNetUKeyService.getVOById(btNetUKeyBankAcc.getuKeyId());
		//获取所有的账号ID
		List<BtNetUKeyBankAcc> listByUKeyId = btNetUKeyBankAccService.getListByUKeyId(btNetUKeyBankAcc.getuKeyId());
		StringBuilder bankIds = new StringBuilder();
		for(BtNetUKeyBankAcc entity:listByUKeyId){
			bankIds.append(entity.getBankAccId()).append(",");
		}
		model.addAttribute("vo",btNetUKey);
		model.addAttribute("bankAcc",bankIds.toString());
		return "bankacc/updateBtNetUKey";
	}
	/**
	 * BT_BANK_ACC详情页
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_QUERY)
	@GetMapping("/view/{id}")
	public String view(@PathVariable String id, Model model) {
		BtNetUKeyBankAcc btNetUKeyBankAcc = btNetUKeyBankAccService.get(id);
		BtNetUKeyVO btNetUKey = btNetUKeyService.getVOById(btNetUKeyBankAcc.getuKeyId());
		BtBankAcc btBankAcc = btBankAccService.get(btNetUKeyBankAcc.getBankAccId());
		model.addAttribute("vo",btNetUKey);
		model.addAttribute("bankAcc",btBankAcc.getBankAcc());
		return "bankacc/viewBtNetUKey";
	}
	/**
	 * 保存操作
	 *
	 * @param btNetUKeyVO
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtNetUKeyVO btNetUKeyVO) {
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
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtNetUKeyVO btNetUKeyVO,String uKeyNoCheck) {
		btNetUKeyService.modify(btNetUKeyVO,uKeyNoCheck);
		return success();
	}
	/**
	 * 删除
	 *
	 * @param id
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTNETUKEY_UPDATE)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id,String username) {
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
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(String ids,String username) {
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
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtNetUKeyVO> list) {
		this.btNetUKeyService.saveImport(list, this.getCurrentUser());
		return success();
	}
}
