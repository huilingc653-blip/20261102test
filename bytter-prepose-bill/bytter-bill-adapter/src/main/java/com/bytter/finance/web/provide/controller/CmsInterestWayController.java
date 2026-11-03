package com.bytter.finance.web.provide.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.finance.domain.provide.service.CmsInterestWayService;
import com.bytter.finance.domain.provide.vo.CmsInterestWayVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 计息方式修改
 * @date 2017-08-09 16:40:48
 */
@Controller
@RequestMapping("/cms/provide/cmsinterestway")
public class CmsInterestWayController extends BaseController {
    @Autowired
    private CmsInterestWayService cmsInterestWayService;
    /**
     * 计息方式 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model, String sendCode, String id) {
        CmsInterestWayVO cmsInterestWayVO = cmsInterestWayService.getCmsInterestWay(id, sendCode);
        model.addAttribute("cmsInterestWayVO", cmsInterestWayVO);
        return "cms/provide/send/listCmsInterestWay";
    }
    /**
     * 功能介绍 :计息方式 查询页面table表<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author songkk
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String id) {
        return cmsInterestWayService.listCmsInterestWay(dataTablesInput, id);
    }
    @GetMapping("/json")
    @ResponseBody
    public Object json() {
        return cmsInterestWayService.getJSONArray();
    }
    /**
     * 保存计息方式
     *
     * @param
     * @return
     */
    @LogMessage(content = "添加CmsInterestWay", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsInterestWayVO cmsInterestWayVO) {
        cmsInterestWayService.save(cmsInterestWayVO, getCurrentUser());
        return success();
    }
}
