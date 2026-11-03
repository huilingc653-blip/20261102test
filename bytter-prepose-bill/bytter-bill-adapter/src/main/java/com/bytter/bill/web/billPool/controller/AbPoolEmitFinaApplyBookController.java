package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolFinaApplyBookService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolFinaApplyBookVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Controller
 * @Description: 票据池明细账簿信息控制器
 * @date 2020-03-13 15:25:10
 */
@Controller
@RequestMapping("/billPool/finaApplyBook")
public class AbPoolEmitFinaApplyBookController extends BaseController {
    @Autowired
    private AbPoolFinaApplyBookService abPoolFinaApplyBookService;
    /**
     * 跳转融资申请登记簿查询列表页面
     *
     * @return
     */
    @GetMapping("/list")
    public String listFinaApplyBook(Model model) {
        return "billPool/listAbPoolBillFinaApplyBook";
    }
    /**
     * 跳转融资申请登记簿明细页面
     *
     * @return
     */
    @GetMapping("/viewFinaApplyInfo")
    public String viewFinaApplyInfo(String id, Model model) {
        model.addAttribute("info", abPoolFinaApplyBookService.getAbPoolDtlBookVOById(id));
        return "billPool/viewAbPoolFinaApplyBook";
    }
    /**
     * 查询融资申请登记簿信息列表数据
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableFinaApplyQuery")
    @ResponseBody
    @LogMessage(content = "查询出票融资申请账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableFinaApplyQuery(@RequestBody DataTablesInput dataTablesInput) {
        return abPoolFinaApplyBookService.datatableQueryBook(dataTablesInput);
    }
    /**
     * 查看融资申请登记簿明细账簿信息列表数据
     * @param vo 查询条件
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/finaApplyQueryDtl")
    @LogMessage(content = "查看融资申请登记簿账簿明细信息列表数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public String finaApplyQueryDtl(AbPoolFinaApplyBookVO vo, Model model) {
        model.addAttribute("infoParams", vo);
        return "billPool/listAbPoolBillFinaApplyDtl";
    }
    /**
     * 查看融资申请登记簿明细账簿信息列表数据
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableFinaApplyQueryDtl")
    @ResponseBody
    @LogMessage(content = "查询出票融资申请账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableFinaApplyQueryDtl(@RequestBody DataTablesInput dataTablesInput) {
        return abPoolFinaApplyBookService.datatableQueryDtl(dataTablesInput);
    }
}
