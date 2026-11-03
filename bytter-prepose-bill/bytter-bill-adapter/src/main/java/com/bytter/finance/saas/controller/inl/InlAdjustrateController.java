package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlAdjustrate;
import com.bytter.finance.domain.inl.service.InlAdjustrateService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlAdjustrateVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "内部拆借利率调整")
@RestController("v1InlAdjustrateController")
@RequestMapping("/v1-preposebill/inl/adjustrate")
public class InlAdjustrateController extends BaseController {
    @Autowired
    private InlAdjustrateService inlAdjustrateService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlRepayService inlRepayService;

    /**
     * 申请列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ResponseBody
    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> DataTablesOutput(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlAdjustrateService.datatable(input, getCurrentUser()));
    }


    /**
     * 待审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listAbolishApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlAdjustrateService.approveDataTable(input, getCurrentUser()));
    }

    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody CmsInlAdjustrateVO cmsInlAdjustrateVO) {
        inlAdjustrateService.save(cmsInlAdjustrateVO, getCurrentUser());
        return success();
    }

    /**
     * 展期申请新增
     *
     * @param id
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("cmsInlAdjustrateVOs", inlAdjustrateService.getCmsInlAdjustrateVOBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 编辑
     *
     * @param id
     * @return
     */
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlAdjustrate.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAdjustrate.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlAdjustrate", cmsInlAdjustrate);
        model.put("cmsInlAdjustrateVOs",
                inlAdjustrateService.getCmsInlAdjustrateVOBySendId(cmsInlAdjustrate.getInlSendId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlAdjustrate cmsInlAdjustrate = inlAdjustrateService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlAdjustrate.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAdjustrate.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlAdjustrate", cmsInlAdjustrate);
        model.put("cmsInlAdjustrateVOs",
                inlAdjustrateService.getCmsInlAdjustrateVOBySendId(cmsInlAdjustrate.getInlSendId()));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/viewAll")
    public AjaxJson<Map<String, Object>> viewAll(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("cmsInlAdjustrateVOs", inlAdjustrateService.getCmsInlAdjustrateVOBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/deletById")
    public AjaxJson deleteById(String id) {
        inlAdjustrateService.deleteById(id);
        return success();
    }
}
