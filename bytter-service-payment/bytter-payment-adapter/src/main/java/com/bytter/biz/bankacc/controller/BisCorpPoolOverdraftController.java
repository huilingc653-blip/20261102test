package com.bytter.biz.bankacc.controller;

import com.bytter.biz.inneraccount.service.BisCorpPoolOverdraftService;
import com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 多级单位余额核算-单位归集透支金额表
 * @author huangx
 * @date 2025年6月20日11:03:04
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bankacc/biscorppooloverdraft")
public class BisCorpPoolOverdraftController extends BaseController {

    @Autowired
    private BisCorpPoolOverdraftService bisCorpPoolOverdraftService;

    /**
     * 跳转 单位归集透支金额维护 列表
     * @return
     */
    @LogMessage(content = "查询单位归集透支金额表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "bankacc/listBisCorpPoolOverdraft";
    }

    /**
     * 查询 单位归集透支金额维护 列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisCorpPoolOverdraftService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 跳转 单位归集透支金额数据 新增
     * @param BisCorpPoolOverdraft
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(BisCorpPoolOverdraftVO BisCorpPoolOverdraft, Model model) {
        if (StringUtils.isNotEmpty(BisCorpPoolOverdraft.getId())) {
            BisCorpPoolOverdraft = bisCorpPoolOverdraftService.getByIdUnion(BisCorpPoolOverdraft.getId());
            model.addAttribute("bisCorpPoolOverdraftPage", BisCorpPoolOverdraft);
        }
        return "bankacc/addBisCorpPoolOverdraft";
    }

    /**
     * 跳转 单位归集透支金额数据 编辑
     * @param BisCorpPoolOverdraft
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String update(BisCorpPoolOverdraftVO BisCorpPoolOverdraft, Model model) {
        if (StringUtils.isNotEmpty(BisCorpPoolOverdraft.getId())) {
            BisCorpPoolOverdraft = bisCorpPoolOverdraftService.getByIdUnion(BisCorpPoolOverdraft.getId());
            model.addAttribute("bisCorpPoolOverdraftPage", BisCorpPoolOverdraft);
        }
        return "bankacc/updateBisCorpPoolOverdraft";
    }

    /**
     * 新增 单位归集透支金额数据 保存
     * @param BisCorpPoolOverdraft
     * @return
     */
    @LogMessage(content = "新增单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        if (checkData(bisCorpPoolOverdraft)) {
            bisCorpPoolOverdraftService.saveBisCorpPoolOverdraft(bisCorpPoolOverdraft, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 编辑 单位归集透支金额数据 保存
     * @param bisCorpPoolOverdraft
     * @return
     */
    @LogMessage(content = "修改单位归集透支金额表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        if (checkData(bisCorpPoolOverdraft)) {
            bisCorpPoolOverdraftService.updateBisCorpPoolOverdraft(bisCorpPoolOverdraft, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 导入 单位归集透支金额数据
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BisCorpPoolOverdraftVO> list) {
        bisCorpPoolOverdraftService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 存储校验
     * @param bisCorpPoolOverdraft
     */
    public boolean checkData(BisCorpPoolOverdraftVO bisCorpPoolOverdraft) {
        // 同单位，同币种只能有一条数据
        if (!bisCorpPoolOverdraftService.validRepeat(bisCorpPoolOverdraft)) {
            throw new ParameterException(NetbankMessageEnum.NETBANK_MESSAGE_BISCORPPOOLOVERDRAFT_REPEAT);
        }
        return true;
    }

    /**
     * 删除 单位归集透支金额数据
     * @param id
     * @return
     */
    @LogMessage(content = "删除单位归集透支金额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        bisCorpPoolOverdraftService.delBisCorpPoolOverdraft(id);
        return success();
    }

    /**
     * 批量删除 单位归集透支金额数据
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除单位归集透支金额表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.bisCorpPoolOverdraftService.delBisCorpPoolOverdraft(ids);
        return success();
    }
}
