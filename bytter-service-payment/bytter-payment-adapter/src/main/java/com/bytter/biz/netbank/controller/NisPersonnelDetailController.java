package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.netbank.service.NisPersonnelDetailService;
import com.bytter.biz.netbank.vo.NisPersonnelDetailVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 职工信息维护表
 * @author onlineGenerator
 * @date 2017-05-26 09:31:22
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/nispersonneldetail")
public class NisPersonnelDetailController extends BaseController {
	@Autowired
	private NisPersonnelDetailService nisPersonnelDetailService;
	/**
	* 职工信息维护表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_QUERY)
	@LogMessage(content = "查询职工信息维护表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list() {
        return "netbank/listNisPersonnelDetail";
	}
    /**
    * 职工信息维护表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_QUERY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return nisPersonnelDetailService.getDataTablesUnion(dataTablesInput,getCurrentUser());
    }
	/**
	* 职工信息维护表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@GetMapping("/add")
	public String add(NisPersonnelDetail nisPersonnelDetail, Model model) {
		if (StringUtils.isNotEmpty(nisPersonnelDetail.getId())) {
			nisPersonnelDetail = nisPersonnelDetailService.getById(nisPersonnelDetail.getId());
			model.addAttribute("nisPersonnelDetailPage", nisPersonnelDetail);
		}
		return "netbank/addNisPersonnelDetail";
	}
	/**
	* 职工信息维护表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@GetMapping("/update")
	public String update(NisPersonnelDetail nisPersonnelDetail, Model model) {
		if (StringUtils.isNotEmpty(nisPersonnelDetail.getId())) {
			nisPersonnelDetail = nisPersonnelDetailService.getById(nisPersonnelDetail.getId());
			model.addAttribute("nisPersonnelDetailPage", nisPersonnelDetail);
		}
		return "netbank/updateNisPersonnelDetail";
	}
	/**
	* 职工信息维护表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_QUERY)
	@LogMessage(content = "查询职工信息维护表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		NisPersonnelDetailVO nisPersonnelDetail = nisPersonnelDetailService.getByUnion(id);
		model.addAttribute("nisPersonnelDetailPage", nisPersonnelDetail);
		return "netbank/viewNisPersonnelDetail";
	}
	/**
	 * 添加职工 信息维护表
	 * @param nisPersonnelDetail
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@LogMessage(content = "添加职工信息维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(NisPersonnelDetail nisPersonnelDetail){
		nisPersonnelDetailService.save(nisPersonnelDetail, this.getCurrentUser());
		return success();
	}
	/**
	* 修改职工信息维护表
	* @param nisPersonnelDetail
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@LogMessage(content = "修改职工信息维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(NisPersonnelDetail nisPersonnelDetail){
		NisPersonnelDetail t = nisPersonnelDetailService.getById(nisPersonnelDetail.getId());
		BeanUtils.copyProperties(nisPersonnelDetail, t);
		nisPersonnelDetailService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除职工信息维护表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@LogMessage(content = "删除职工信息维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		NisPersonnelDetail nisPersonnelDetail = nisPersonnelDetailService.getById(id);
		nisPersonnelDetailService.deleteNisPersonnelDetail(nisPersonnelDetail);
		return success();
	}
	/**
	* 批量删除职工信息维护表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_NISPERSONNELDETAIL_APPLY)
	@LogMessage(content = "批量删除职工信息维护表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids") String ids) {
		this.nisPersonnelDetailService.batchDelete(ids);
		return success();
	}
	/**
	 * 选择职工信息
	 * @return
	 */
	@GetMapping("/select")
	public String select(String corpId, Model model){
		/*if(StringUtils.isEmpty(corpId)){
			corpId = "--";
		}
		model.addAttribute("corpId", corpId);*/
		return "netbank/selectNisPersonnelDetail";
	}
	/**
	 * 保存导入数据
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<NisPersonnelDetailVO> list) {
		this.nisPersonnelDetailService.saveImport(list, this.getCurrentUser());
		return success();
	}
}
