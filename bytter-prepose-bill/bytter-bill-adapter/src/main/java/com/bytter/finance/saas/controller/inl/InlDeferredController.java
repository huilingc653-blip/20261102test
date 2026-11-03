package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.ListUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.entity.CmsInlDeferred;
import com.bytter.finance.domain.inl.service.InlDeferredService;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlDeferredVO;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "内部拆解展期")
@RestController("v1InlDeferredController")
@RequestMapping("/v1-preposebill/inl/deferred")
public class InlDeferredController extends BaseController {
    @Autowired
    private InlDeferredService inlDeferredService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @Autowired
    private InlRepayService inlRepayService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 申请列表数据
     *
     * @param dataTablesInput
     * @return
     */

    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> DataTablesOutput(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlDeferredService.datatable(input, getCurrentUser()));
    }

    /**
     * 申请列表
     *
     * @return
     */
    @GetMapping("/listInlDeferred")
    public AjaxJson<ConfigDTO> listInlInfo() {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.INL, BaseDataConstant.DEFERRED_NUM);
        return AjaxJson.success(configDTO);
    }

    /**
     * 待审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listAbolishApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlDeferredService.approveDataTable(input, getCurrentUser()));
    }

    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsInlDeferredVO cmsInlDeferredVO) {
        inlDeferredService.save(cmsInlDeferredVO, getCurrentUser());
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
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService.getAllCmsInlDeferredBySendId(id);
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.put("lastDeferredDate", cmsInlDeferredVOS.get(0).getNowDeferredDate());
            model.put("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.put("deferredCount", 0);
        }
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlDeferred.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlDeferred.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("cmsInlDeferred", cmsInlDeferred);
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService
                .getAllCmsInlDeferredBySendId(cmsInlDeferred.getInlSendId());
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.put("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.put("deferredCount", 0);
        }
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlDeferred.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlDeferred.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("cmsInlDeferred", cmsInlDeferred);
        List<CmsInlDeferredVO> cmsInlDeferredVOS = inlDeferredService
                .getAllCmsInlDeferredBySendId(cmsInlDeferred.getInlSendId());
        if (ListUtils.isNotNullOrEmpty(cmsInlDeferredVOS)) {
            model.put("deferredCount", cmsInlDeferredVOS.size());
        } else {
            model.put("deferredCount", 0);
        }
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
        CmsInlDeferred cmsInlDeferred = inlDeferredService.get(id);
        model.put("cmsInlDeferreds", inlDeferredService.getAllCmsInlDeferredBySendId(id));
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }

    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @return
     */

    @PostMapping("/deletById")
    public AjaxJson deleteById(String id) {
        inlDeferredService.deleteById(id);
        return success();
    }
}
