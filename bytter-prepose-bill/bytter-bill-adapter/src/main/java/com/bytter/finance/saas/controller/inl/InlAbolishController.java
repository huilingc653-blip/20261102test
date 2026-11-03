package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "内部拆借作废")
@RestController("v1InlAbolishController")
@RequestMapping("/v1-preposebill/inl/abolish")
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

    /**
     * 新增
     *
     * @param id
     * @return
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, Object>> add(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(id);
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("inlInfo", cmsInlInfoVO);
        return AjaxJson.success(model);
    }


    @ResponseBody
    @PostMapping("/listAbolishInfoDataTable")
    public AjaxJson<DataTablesOutput> listAbolishInfoDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlAbolishService.listAbolishInfoDataTable(input, getCurrentUser()));
    }

    /**
     * 待审批列表
     *
     * @param dataTablesInput
     * @return
     */
    @ResponseBody
    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listAbolishApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlAbolishService.approveDataTable(input, getCurrentUser()));
    }

    /**
     * 保存
     *
     * @param cmsInlAbolishVO
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsInlAbolishVO cmsInlAbolishVO) {
        inlAbolishService.save(cmsInlAbolishVO, getCurrentUser());
        return success();
    }

    /**
     * 查看
     *
     * @param id
     * @return
     */
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> viewInlAbolish(String id) {
        Map<String, Object> model = new HashMap<>();
        CmsInlAbolishVO cmsInlAbolishVO = inlAbolishService.getCmsInlAbolishById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAbolishVO.getInlInfoId());
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlAbolish", cmsInlAbolishVO);
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
        CmsInlAbolishVO cmsInlAbolishVO = inlAbolishService.getCmsInlAbolishById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlAbolishVO.getInlInfoId());
        model.put("corp", corpService.get(cmsInlInfoVO.getBorrowInCorp()));
        InlUtil.setParams(configService, model, cmsInlInfoVO.getId(), inlInfoService, conCommonService,
                guaContractOccupServie);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlAbolish", cmsInlAbolishVO);
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
        inlAbolishService.deleteById(id);
        return success();
    }
}
