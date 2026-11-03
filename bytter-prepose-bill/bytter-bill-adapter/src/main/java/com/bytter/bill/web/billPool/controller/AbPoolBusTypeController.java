package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolBusTypeService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolBusTypeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/abpool/bustype")
public class AbPoolBusTypeController extends BaseController{
	@Autowired
	private AbPoolBusTypeService abPoolBusTypeService;
		
    @GetMapping("/list")
    public String list(AbPoolBusTypeVO queryVo,Model model) {
    	queryVo.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
    	model.addAttribute("listInfo", abPoolBusTypeService.listAbPoolBusTypeVO(queryVo));
    	model.addAttribute("queryVO", queryVo);
        return "billPool/listAbBusType";
    }
    
    @GetMapping("/addOrUpdate/{id}")
   	public String addOrUpdate(@PathVariable("id") String id,String opt,Model model) {
    	AbPoolBusTypeVO abPoolBusTypeVO = abPoolBusTypeService.getAbPoolBustTypeVOByBpId(id);
    	if(!StringUtils.isEmpty(opt) && opt.equals("add")){
    		abPoolBusTypeVO = new AbPoolBusTypeVO();
    		abPoolBusTypeVO.setBustypeLevel(AbPoolConstant.AB_POLL_BUSTYPE_LEVEL_1);
    	}
       	model.addAttribute("abPoolBusTypeVO",abPoolBusTypeVO);
   		return "billPool/addOrUpdatePoolBusType";
   	}
    
    @GetMapping("/addNextLevel/{id}")
    public String addNextLevel(@PathVariable("id") String id,String opt,Model model) {
    	AbPoolBusTypeVO abPoolBusTypeVO = abPoolBusTypeService.getAbPoolBustTypeVOByBpId(id);
    	AbPoolBusTypeVO newVO = new AbPoolBusTypeVO();
    	newVO.setBustypeLevel(abPoolBusTypeVO.getBustypeLevel()+1);
    	newVO.setParentId(abPoolBusTypeVO.getId());
    	model.addAttribute("abPoolBusTypeVO",newVO);
    	return "billPool/addOrUpdatePoolBusType";
    }
	@PostMapping("/batchOpt")
	@ResponseBody
	public AjaxJson batchOpt(String ids,Integer opt){
		return abPoolBusTypeService.batchBusTypeOptWith(ids, opt,this.getCurrentUser());
	}
	@PostMapping("/updateAbPoolBusType")
	@ResponseBody
	public AjaxJson updateAbPoolBusType(AbPoolBusTypeVO abPoolBusTypeVO) {
		return abPoolBusTypeService.settingPoolBusType(abPoolBusTypeVO,this.getCurrentUser());
	}
	
    /**
     * 获取树形结构单位信息
     * @return
     */
    @GetMapping("/getTreeData")
    @ResponseBody
    public Object getTreeData(String resource, AbPoolBusTypeVO queryVO) {
    	queryVO.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
        return abPoolBusTypeService.getTreeDataList(resource,queryVO, this.getCurrentUser());
    }
}
