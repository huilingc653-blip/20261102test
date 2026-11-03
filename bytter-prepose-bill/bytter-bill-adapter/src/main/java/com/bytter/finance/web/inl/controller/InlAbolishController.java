package com.bytter.finance.web.inl.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.service.InlAbolishService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.vo.CmsInlAbolishVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/inl/abolish")
public class InlAbolishController extends BaseController {
    @Autowired
    private InlAbolishService inlAbolishService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private InlInfoService inlInfoService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Resource
    private IConService conCommonService;
    @GetMapping("/listInlAbolish")
    public String listInlAbolish() {
        return "cms/inl/abolish/listInlAbolish";
    }
    @GetMapping("/listApprove")
    public String listApproveInlAbolish(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/inl/abolish/listApproveInlAbolish";
    }
    /**
     * 新增
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String add(String id, Model model) {
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(id);
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        return "cms/inl/abolish/addInlAbolish";
    }
    @ResponseBody
    @PostMapping("/listAbolishInfoDataTable")
    public DataTablesOutput listAbolishInfoDataTable(@RequestBody DataTablesInput input) {
        return inlAbolishService.listAbolishInfoDataTable(input, getCurrentUser());
    }
    /**
     * 待审批列表
     *
     * @param input
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public DataTablesOutput listAbolishApprove(@RequestBody DataTablesInput input) {
        return inlAbolishService.approveDataTable(input, getCurrentUser());
    }
    /**
     * 保存
     *
     * @param cmsInlAbolishVO
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(CmsInlAbolishVO cmsInlAbolishVO) {
        inlAbolishService.save(cmsInlAbolishVO, getCurrentUser());
        return success();
    }
    /**
     * 查看
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/view")
    public String viewInlAbolish(String id, Model model) {
        CmsInlAbolishVO cmsInlAbolishVO = inlAbolishService.getCmsInlAbolishById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAbolishVO.getInlInfoId());
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlAbolish", cmsInlAbolishVO);
        return "cms/inl/abolish/viewInlAbolish";
    }
    /**
     * 编辑
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        CmsInlAbolishVO cmsInlAbolishVO = inlAbolishService.getCmsInlAbolishById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAbolishVO.getInlInfoId());
        model.addAttribute("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.addAttribute("inlInfo", cmsInlInfoVO);
        model.addAttribute("inlAbolish", cmsInlAbolishVO);
        return "cms/inl/abolish/editInlAbolish";
    }
    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/inl/abolish/viewApproveInlAbolish";
    }
    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @param model
     * @return
     */
    @ResponseBody
    @PostMapping("/deletById")
    public AjaxJson deleteById(String id, Model model) {
        inlAbolishService.deleteById(id);
        return success();
    }
}
