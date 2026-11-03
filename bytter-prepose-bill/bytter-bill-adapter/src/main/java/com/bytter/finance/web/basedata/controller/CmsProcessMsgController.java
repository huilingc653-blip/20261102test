package com.bytter.finance.web.basedata.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.domain.basedata.entity.CmsProcessMsg;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: CMS_PROCESS_MSG
 * @date 2017-09-14 15:07:53
 */
@Controller
@RequestMapping("/cms/basedata/cmsprocessmsg")
public class CmsProcessMsgController extends BaseController {
    @Autowired
    private CmsProcessMsgService cmsProcessMsgService;
    /**
     * CMS_PROCESS_MSG列表 页面跳转
     *
     * @return
     */
    // @LogMessage(content = "查询CMS_PROCESS_MSG列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, String billCode) {
        model.addAttribute("billCode", billCode);
        return "cms/basedata/loanProcessMsg/listCmsProcessMsg";
    }
    /**
     * 日志查询
     *
     * @param billCode
     * @param model
     * @return
     */
    @GetMapping("/list/{billCode}")
    public String list(@PathVariable("billCode") String billCode, Model model) {
        DataTablesInput input = new DataTablesInput();
        List<CmsProcessMsg> listNisMessage = cmsProcessMsgService.listMsgData(billCode, input).getData();
        Collections.sort(listNisMessage, new Comparator<CmsProcessMsg>() {
            @Override
            public int compare(CmsProcessMsg msg1, CmsProcessMsg msg2) {
                return msg2.getCreateDate().compareTo(msg1.getCreateDate());
            }
        });
        model.addAttribute("list", listNisMessage);
        return "cms/basedata/loanProcessMsg/listCmsProcessMsgShow";
    }
    @PostMapping("/listMsgData")
    @ResponseBody
    public DataTablesOutput listMsgData(@RequestBody DataTablesInput input, String billCode) {
        return cmsProcessMsgService.listMsgData(billCode, input);
    }
}
