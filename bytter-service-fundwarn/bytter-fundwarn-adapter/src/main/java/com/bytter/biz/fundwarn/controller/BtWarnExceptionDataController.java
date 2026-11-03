package com.bytter.biz.fundwarn.controller;

import com.bytter.biz.fundwarn.entity.BtWarnExceptionData;
import com.bytter.biz.fundwarn.service.BtWarnExceptionDataService;
import com.bytter.biz.fundwarn.util.BtFundWarnUtils;
import com.bytter.biz.fundwarn.vo.BtWarnExceptionDataVO;
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
* @Description 异常告警控制器类
* @author zhous
* @date 2024/8/21 16:49
*/
@Controller
@RequestMapping("/fundwarn/exceptionData")
public class BtWarnExceptionDataController extends BaseController {

    @Autowired
    private BtWarnExceptionDataService btWarnExceptionDataService;

    /**
     * 异常告警主数据信息列表页面跳转
     * @return
     */
    @LogMessage(content = "异常告警数据信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "fundwarn/listBtWarnExceptionData";
    }

    /**
     * 加载异常告警数据信息列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btWarnExceptionDataService.listWarnData(dataTablesInput);
    }


    /**
     * 异常告警详情查看页面
     * @return
     */
    @LogMessage(content = "异常告警详情查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id,Model model) {
        BtWarnExceptionDataVO vo = btWarnExceptionDataService.getWarnExceptionDataById(id);
        model.addAttribute("dataInfo",vo);
        return "fundwarn/viewBtWarnExceptionData";
    }
}
