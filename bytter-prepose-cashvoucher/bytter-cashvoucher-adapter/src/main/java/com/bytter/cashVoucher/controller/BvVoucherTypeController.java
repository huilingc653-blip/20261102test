package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.entity.BvVoucherType;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvVoucherTypeService;
import com.bytter.cashVoucher.service.BvVoutypeDataFieldService;
import com.bytter.cashVoucher.service.BvVoutypeSceneService;
import com.bytter.cashVoucher.vo.BvVoucherTypeVO;
import com.bytter.cashVoucher.vo.BvVoutypeDataFieldVO;
import com.bytter.cashVoucher.vo.BvVoutypeSceneVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @Title: Controller
 * @Description: 凭证类型维护
 * @author onlineGenerator
 * @date 2017-05-25 14:20:46
 * @version 1.0
 */
@Controller
@RequestMapping("/bill/blankvou/bvvouchertype")
public class BvVoucherTypeController extends BaseController { 
	@Autowired
	private BvVoucherTypeService bvVoucherTypeService;
	
	@Autowired
	private BvVoutypeDataFieldService bvVoutypeDataFieldService;
	
	@Autowired
	private BvVoutypeSceneService bvVoutypeSceneService;
	
	@Autowired
	private BvLogService bvLogService;
	@DubboReference(check = false)
	private Ii18nService ii18nService;
	/**
	 * 凭证类型维护列表 页面跳转
	 * @return
	 */
	@LogMessage(content = "查询凭证类型维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/blankvou/listBvVoucherType";
	}
	/**
	 * 凭证类型维护 table列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return bvVoucherTypeService.listVoucherType(dataTablesInput);
	}
	
	/**
	 * 根据条件查询凭证种类信息
	 * @param opType
	 * @return
	 */
	@PostMapping("/fieldDataTable")
	@ResponseBody
	public List<BvVoutypeDataFieldVO> fieldDataTable(String opType, String vouTid) {
		// 初始化表格
		return bvVoutypeDataFieldService.listBvVoutypeDataField(opType,vouTid);
	}
	
	/**
	 * 根据条件查询打印场景信息
	 * @param vouTid
	 * @return
	 */
	@PostMapping("/sceneDataTable")
	@ResponseBody
	public List<BvVoutypeSceneVO> sceneDataTable(String vouTid) {
		// 初始化表格
		return bvVoutypeSceneService.listBvVoutypeScene(vouTid);
	}
	/**
	 * 凭证类型维护新增页面跳转
	 * @return
	 */
	@GetMapping("/add")
	public String add(BvVoucherType bvVoucherType, Model model) {
		model.addAttribute("id",StringUtils.getUUId());
		return "bill/blankvou/addBvVoucherType";
	}
	/**
	 * 凭证类型维护编辑页面跳转
	 * @return
	 */
	@GetMapping("/update")
	public String update(BvVoucherType bvVoucherType, Model model) {
		if (StringUtils.isNotEmpty(bvVoucherType.getId())) {
			bvVoucherType = bvVoucherTypeService.get(bvVoucherType.getId());
			if(StringUtils.isNotEmpty(ii18nService.getBusinessBaseContext(bvVoucherType.getId()))){
				bvVoucherType.setName(ii18nService.getBusinessBaseContext(bvVoucherType.getId()));
			}
			model.addAttribute("bvVoucherTypePage", bvVoucherType);
		}
		return "bill/blankvou/updateBvVoucherType";
	}
	/**
	 * 凭证类型维护详情页
	 * @return
	 */
	@LogMessage(content = "查询凭证类型维护详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BvVoucherTypeVO bvVoucherTypeVo = bvVoucherTypeService.getVoucherTypeById(id);
		List<BvVoutypeDataFieldVO> dataFieldList=bvVoutypeDataFieldService.listBvVoutypeDataField("view",bvVoucherTypeVo.getId());
		List<BvVoutypeSceneVO> sceneList=bvVoutypeSceneService.listBvVoutypeScene(bvVoucherTypeVo.getId());
		List<BvLog> bvlogList = bvLogService.listBvLog(bvVoucherTypeVo.getId());
		model.addAttribute("bvVoucherTypePage", bvVoucherTypeVo);
		model.addAttribute("bvVouTypeDataFieldListPage",dataFieldList);
		model.addAttribute("bvVouTypeSceneListPage",sceneList);
		model.addAttribute("bvlogList", bvlogList);
		return "bill/blankvou/viewBvVoucherType";
	}
	
	/**
	 * 保存数据
	 * @param bvVoucherTypeVO
	 * @return
	 */
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BvVoucherTypeVO bvVoucherTypeVO) {
		bvVoucherTypeService.saveBvVoucherType(bvVoucherTypeVO, this.getCurrentUser().getLoginName(),this.getCurrentUser().getUsername(),this.getCurrentUser());
		return success();
	}
	/**
	 * 修改凭证类型维护
	 * @param bvVoucherTypeVO
	 * @return
	 */
	@LogMessage(content = "修改凭证类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody BvVoucherTypeVO bvVoucherTypeVO) {		
		bvVoucherTypeService.modifyBvVoucherType(bvVoucherTypeVO, this.getCurrentUser().getLoginName(),this.getCurrentUser().getUsername(),this.getCurrentUser());
		return success();
	}
	/**
	 * 删除凭证类型维护
	 * @param id
	 * @return
	 */
	@LogMessage(content = "删除凭证类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		AjaxJson j = success();
		j.setSuccess(bvVoucherTypeService.deleteBvVoucherType(id,this.getCurrentUser()));
		return j;
	}
	/**
	 * 批量删除凭证类型维护
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "批量删除凭证类型维护", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String ids) {
		AjaxJson j = success();
		boolean flag=bvVoucherTypeService.batchDeleteBvVoucherType(ids,this.getCurrentUser());
		j.setSuccess(flag); 
		return j;
	}
	/**
	 * 判断输入的表或视图是否存在
	 * @param dataSource
	 * @return
	 */
	@PostMapping("/isExistTable")
	@ResponseBody
	public AjaxJson isExistTable(String dataSource) {
		AjaxJson json = success();
		boolean flag=bvVoucherTypeService.isExistTable(dataSource);
		json.setSuccess(flag);
		return json;
	}
	/**
	 * 判断字段在表或视图中是否存在
	 * @param
	 * @return
	 */
	@PostMapping("/isExistColumn")
	@ResponseBody
	public AjaxJson isExistColumn(String fieldCode, String dataSource) {
		AjaxJson json = success();
		boolean flag=bvVoucherTypeService.isExistColumn(fieldCode, dataSource);
		json.setSuccess(flag);
		return json;
	}
	
	/**
	 * 校验输入的凭证种类代码是否重复 
	 * @param code
	 * @return
	 */
	@PostMapping("/checkVoucherTypeCode")
	@ResponseBody
	public AjaxJson checkVoucherTypeCode(String opType,String code,String id) {
		AjaxJson j = success();
		j.setSuccess(bvVoucherTypeService.checkVoucherTypeCode(opType,code,id));
		return j;
	}
	/**
	 * 查询所有凭证类型
	 * @return
	 */
	@GetMapping("/queryVoucherType")
	@ResponseBody
	public List<BvVoucherTypeVO> queryVoucherType() {
		return bvVoucherTypeService.queryVoucherTypeVO();
	}
	/**
	 * 查询所有有效凭证类型
	 * @return
	 */
	@GetMapping("/queryVoucherTypeValid")
	@ResponseBody
	public List<BvVoucherTypeVO> queryVoucherTypeValid() {
		return bvVoucherTypeService.queryVoucherTypeVOValid();
	}
	/**
	 * 获取需要入库且有效的凭证类型
	 * @return
	 */
	@GetMapping("/queryVoucherTypeValidByInbox")
	@ResponseBody
	public List<BvVoucherTypeVO> queryVoucherTypeValidByInbox() {
		return bvVoucherTypeService.queryVoucherTypeVOValidByInbox();
	}
	/**
	 * 根据vouTid获取有效的取值字段明细信息  
	 * @return
	 */
	@GetMapping("/listVoutypeDataFieldValidByVouTid")
	@ResponseBody
	public List<BvVoutypeDataFieldVO> listVoutypeDataFieldValidByVouTid(String id) {
		return bvVoutypeDataFieldService.listVoutypeDataFieldVOValidByVouTid(id);
	}
	/**
	 * 根据vouTid获取有效的打印场景信息  
	 * @return
	 */
	@GetMapping("/listVoutypeSceneValidByVouTid")
	@ResponseBody
	public List<BvVoutypeSceneVO> listVoutypeSceneValidByVouTid(String id) {
		return bvVoutypeSceneService.listVoutypeSceneVOValidByVouTid(id);
	}
}
