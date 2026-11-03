package com.bytter.bill.web.bill.controller;

import com.bytter.bill.domain.bill.entity.AbEmitBailInterestInfo;
import com.bytter.bill.domain.bill.service.AbEmitBailInterestInfoService;
import com.bytter.bill.domain.bill.vo.AbEmitBailInterestInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
* @Description 票据保证金利息维护
* @author zhous
* @date 2025/3/5 14:38
*/
@Controller
@RequestMapping("/bill/accebill/bailInterestInfo")
public class AbEmitBailInterestInfoController extends BaseController {

    @Autowired
    private AbEmitBailInterestInfoService abEmitBailInterestInfoService;

    /**
     * 票据保证金利息维护列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbEmitBailInterestInfo";
    }

    /**
     * 票据保证金利息维护列表查询
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "票据保证金利息维护列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abEmitBailInterestInfoService.dataTable(input);
    }

    /**
     * 手动新增票据保证金利息
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "bill/accebill/addAbEmitBailInterestInfo";
    }

    /**
     * 编辑票据保证金利息
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String id, Model model) {
        if(StringUtils.isNotBlank(id)){
            AbEmitBailInterestInfo abEmitBailInterestInfo = abEmitBailInterestInfoService.get(id, true);
            model.addAttribute("info",abEmitBailInterestInfo);
        }
        return "bill/accebill/updateAbEmitBailInterestInfo";
    }

    /**
    * @Description 票据保证金利息维护列表新增或修改操作
    * @author zhous
    * @date 2025/3/5 17:06
    */
    @LogMessage(content = "票据保证金利息维护列表新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(AbEmitBailInterestInfoVO info, HttpServletRequest request) {
        abEmitBailInterestInfoService.saveOrUpdate(info, this.getCurrentUser());
        return success();
    }

    /**
     * 计算利息页面
     *
     * @return
     */
    @GetMapping("/addCalculatedInterest")
    public String addCalculatedInterest() {
        return "bill/accebill/calculatedInterest";
    }

    /**
    * @Description 自动计算生成保证金利息记录
    * @author zhous
    * @date 2025/3/6 9:24
    */
    @LogMessage(content = "自动计算生成保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/autoCalculatedInterest")
    @ResponseBody
    public AjaxJson autoCalculatedInterest(AbEmitBailInterestInfoVO vo) {
        abEmitBailInterestInfoService.autoCalculatedInterest(vo, this.getCurrentUser());
        return success();
    }

    /**
     * @Description 批量确认保证金利息记录
     * @author zhous
     * @date 2025/3/6 9:24
     */
    @LogMessage(content = "批量确认保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSure")
    @ResponseBody
    public AjaxJson batchSure(String ids) {
        abEmitBailInterestInfoService.batchSureOrNotSure(true,ids, this.getCurrentUser());
        return success();
    }

    /**
     * @Description 批量反确认保证金利息记录
     * @author zhous
     * @date 2025/3/6 9:24
     */
    @LogMessage(content = "批量反确认保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchNotSure")
    @ResponseBody
    public AjaxJson batchNotSure(String ids) {
        abEmitBailInterestInfoService.batchSureOrNotSure(false,ids, this.getCurrentUser());
        return success();
    }

    /**
     * @Description 推送共享保证金利息记录
     * @author zhous
     * @date 2025/3/6 9:24
     */
    @LogMessage(content = "推送共享保证金利息记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/pushSharing")
    @ResponseBody
    public AjaxJson pushSharing(String ids) {
        abEmitBailInterestInfoService.pushSharing(ids, this.getCurrentUser());
        return success();
    }

    /**
    * @Description 删除记录
    * @author zhous
    * @date 2025/3/6 9:49
    */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abEmitBailInterestInfoService.delete(id);
        return success();
    }

}
