package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrTaskCorpService;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 任务组织表controller
 * @author huangyl
 */
@Controller
@RequestMapping("/credit/crTaskCorp")
public class CrTaskCorpController extends BaseController {

    @Autowired
    private CrTaskCorpService taskCorpService;

    /**
     * 查询页面跳转
     *
     * @param model model
     * @return 页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/dataCollectionAndClean";
    }

    /**
     * 评价任务数据
     *
     * @return
     */
    @GetMapping(value = "/dataCollection")
    @ResponseBody
    public DataTablesOutput dataCollection() {
        return taskCorpService.dataCollection(getCurrentUser());
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/update")
    public String update(Model model, String id) {
        return "creditRating/editRawData";
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/modify")
    @ResponseBody
    public AjaxJson modify(CrdRawDataVo dataVo) {
        taskCorpService.modify(dataVo, getCurrentUser());
        return success();
    }

    /**
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/view")
    @LogMessage(content = "view原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String view(Model model, String id) {
        return "creditRating/viewRawData";
    }

    @LogMessage(content = "删除原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String rawDataId) {
        AjaxJson j = success();
        boolean flag = taskCorpService.deleteById(rawDataId, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }
}
