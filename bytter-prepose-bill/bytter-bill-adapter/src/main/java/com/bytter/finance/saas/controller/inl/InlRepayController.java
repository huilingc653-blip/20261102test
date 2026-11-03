package com.bytter.finance.saas.controller.inl;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.service.InlInfoService;
import com.bytter.finance.domain.inl.service.InlRepayService;
import com.bytter.finance.domain.inl.service.InlSendService;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.CmsInlRepayVO;
import com.bytter.finance.domain.inl.vo.CmsInlSendVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "内部拆借还款")
@RestController("v1InlRepayController")
@RequestMapping("/v1-preposebill/inl/repay")
public class InlRepayController extends BaseController {
    @Autowired
    private InlRepayService inlRepayService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private InlInfoService inlInfoService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 申请列表数据
     *
     * @return
     */
    @PostMapping("/dataTable")
    public AjaxJson<DataTablesOutput> DataTablesOutput(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlRepayService.datatable(input, getCurrentUser()));
    }

    /**
     * 查询暂存打回数据
     *
     * @return
     */
    @PostMapping("/listSendUpdateDataTable")
    public AjaxJson<DataTablesOutput> listSendUpdateDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlRepayService.listRepayUpdateDataTable(input, getCurrentUser()));
    }

    /**
     * 查询待确认数据
     *
     * @return
     */
    @PostMapping("/listRepayConfirmDataTable")
    public AjaxJson<DataTablesOutput> listRepayConfirmDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlRepayService.listRepayConfirmDataTable(input, getCurrentUser()));
    }

    /**
     * 还款确认
     *
     * @param id
     * @param realRepayDate
     * @return
     */
    @PostMapping("/repayConfirm")
    public AjaxJson repayConfirm(String id, Date realRepayDate) {
        inlRepayService.otherDo(realRepayDate, id);
        return success();
    }


    /**
     * 待审批列表数据
     *
     * @return
     */

    @PostMapping("/approveDataTable")
    public AjaxJson<DataTablesOutput> listAbolishApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(inlRepayService.approveDataTable(input, getCurrentUser()));
    }

    /**
     * 数据保存方法
     *
     * @return
     */
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsInlRepayVO cmsInlRepayVO) {
        inlRepayService.save(cmsInlRepayVO, getCurrentUser());
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
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
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
        CmsInlRepayVO cmsInlRepayVO = inlRepayService.getById(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlRepayVO.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlRepayVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlRepay", cmsInlRepayVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
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
        CmsInlRepayVO cmsInlRepayVO = inlRepayService.getById(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(cmsInlRepayVO.getInlSendId());
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlRepayVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlRepay", cmsInlRepayVO);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        /**
         * 资金计划相关参数
         */
        Map<String, Object> config = (Map<String, Object>) configService.listByType(ConfigConstant.INL);
        model.put("config", config);
        model.put("businessName", BudgetConstant.BUSINESS_NAME_INL_REPAY);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(model);
    }

    /**
     * 通过id删除暂存数据
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public AjaxJson deleteById(String id) {
        inlRepayService.deleteById(id);
        return success();
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
        List<CmsInlRepayVO> cmsInlRepayVOS = inlRepayService.getAllRepayInfoBySendId(id);
        CmsInlSendVO cmsInlSendVO = inlSendService.getInlSendById(id);
        CmsInlInfoVO cmsInlInfoVO = inlInfoService.getInfoById(cmsInlSendVO.getInlInfoId());
        model.put("inlSend", cmsInlSendVO);
        model.put("inlInfo", cmsInlInfoVO);
        model.put("inlRepays", cmsInlRepayVOS);
        InlUtil.setRepayInfo(inlRepayService, cmsInlSendVO.getId(), model);
        return AjaxJson.success(model);
    }
}
