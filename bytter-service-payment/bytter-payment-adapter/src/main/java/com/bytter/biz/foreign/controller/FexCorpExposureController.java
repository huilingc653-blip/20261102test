package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexCorpExposureService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 单位敞口设置表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexCorpExposure")
public class FexCorpExposureController extends BaseController {
    @Autowired
    private FexCorpExposureService fexCorpExposureService;

    /**
     * 单位敞口设置
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURE_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
        String orgType = this.fexCorpExposureService.getOrgType();
        model.addAttribute("orgType", orgType);
        model.addAttribute("corpList", this.fexCorpExposureService.listCorpByOrgType(orgType, null));
        model.addAttribute("corpExposureMap", this.fexCorpExposureService.listFbsCorpExposure(orgType, null));
        return "foreign/listFexCorpExposure";
    }

    /**
     * 查询
     *
     * @param search
     * @param model
     * @return
     */
    @LogMessage(content = "单位敞口列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryList")
    public String queryList(String orgType, String search, Model model) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(search)){
            search = URLDecoder.decode(search, "UTF-8");
        }
        model.addAttribute("corpList", this.fexCorpExposureService.listCorpByOrgType(CommonConstant.NO, search));
        model.addAttribute("corpExposureMap", this.fexCorpExposureService.listFbsCorpExposure(orgType, search));
        model.addAttribute("search", search);
        model.addAttribute("orgType", this.fexCorpExposureService.getOrgType());
        return "foreign/listFexCorpExposure";
    }

    /**
     * 单位敞口保存
     *
     * @param collectCorpIds
     * @param naturalHedgeCorpIds
     * @return
     */
    @LogMessage(content = "添加单位敞口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveCorpExposure")
    @ResponseBody
    public AjaxJson saveCorpExposure(@RequestParam("orgType") String orgType,
                                     @RequestParam("collectCorpIds[]") List<String> collectCorpIds, @RequestParam("naturalHedgeCorpIds[]") List<String> naturalHedgeCorpIds) {
        fexCorpExposureService.saveCorpExposure(orgType, collectCorpIds, naturalHedgeCorpIds, this.getCurrentUser());
        return success();
    }

    /**
     * 校验单位是否是汇总单位
     * @param corpId
     * @return
     */
    @RequestMapping(params = "isCollectCorp")
    @ResponseBody
    public AjaxJson isCollectCorp(String corpId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(fexCorpExposureService.isCollectCorp(corpId));
        return j;
    }
}
