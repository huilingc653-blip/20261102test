package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillSignStrategyService;
import com.bytter.bill.domain.bill.vo.AbBillSignStrategyVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: AbBillSignStrategyController
 * @Description: 电票自动签收配置Controller
 * @author wangzy
 * @date 2020/7/13 14:54
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillsignstrategy")
public class AbBillSignStrategyController extends BaseController {
    @Autowired
    public AbBillSignStrategyService abBillSignStrategyService;
    /**
     * 电票自动签收配置列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
		return "bill/accebill/listAbBillSignStrategy";
    }
    /**
     * 电票自动签收配置 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
	@LogMessage(content = "查询电票自动签收配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillSignStrategyService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 电票自动签收配置新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("autoSignNo", SequenceSingleton.getSequence(SequenceEnum.BILL_AUTO_SIGN_CODE));
        return "bill/accebill/addAbBillSignStrategy";
    }
    /**
     * 电票自动签收配置编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
    	AbBillSignStrategyVO abBillSignStrategyVO = new AbBillSignStrategyVO();
    	abBillSignStrategyVO = abBillSignStrategyService.combinationBillVo(abBillSignStrategyVO,id);
        model.addAttribute("abBillSignStrategyVO",abBillSignStrategyVO);
        return "bill/accebill/updateAbBillSignStrategy";
    }
    /**
     * 新增/修改-保存电票自动签收配置数据
     * @param abBillSignStrategyVO 自动签收配置vo
     * @param
     * @return
     */
    @LogMessage(content = "新增-电票自动签收配置数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operator}")
    @ResponseBody
    public AjaxJson saveOrUpdate(@PathVariable("operator")String operator, AbBillSignStrategyVO abBillSignStrategyVO) {
        return abBillSignStrategyService.saveOrUpdate(operator, abBillSignStrategyVO, getCurrentUser());
    }
    
	@LogMessage(content = "删除-电票自动签收配置数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchDel")
	@ResponseBody
    public AjaxJson batchDel(String ids){
		return  abBillSignStrategyService.saveOrUpdate(ids,this.getCurrentUser());
	}
}
