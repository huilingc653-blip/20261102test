package com.bytter.workflow.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.param.AddModelData;
import com.bytter.workflow.domain.param.CreateModelForm;
import com.bytter.workflow.domain.param.FlowCopyParam;
import com.bytter.workflow.domain.param.ModelQuery;
import com.bytter.workflow.domain.service.FlowCategoryService;
import com.bytter.workflow.domain.service.FlowModelService;
import com.bytter.workflow.domain.vo.FlowModelVo;
import com.bytter.workflow.enums.ModelMessageEnum;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/7/13.
 * 模型工作区
 */
@Controller
@RequestMapping("/bpm/model")
public class FlowModelController extends BaseController  {
    private final FlowModelService flowModelService;
    private final FlowCategoryService categoryService;
    public FlowModelController(FlowModelService flowModelService,
                               FlowCategoryService flowCategoryService) {
        this.categoryService = flowCategoryService;
        this.flowModelService = flowModelService;
    }
    @GetMapping("/list")
    public void list() {
        //do nothing
    }
    /**
     * 创建模型不包含模型xml数据
     * @param createModelForm 模型定义
     * @return 是否操作成功
     */
    @PostMapping(value = "/create")
    @ResponseBody
    public AjaxJson createModel(CreateModelForm createModelForm) {
        return success(flowModelService.createModel(createModelForm));
    }
    /**
     * 保存流程模型数据
     * @param addModelData 流程数据
     * @return 是否成功
     */
    @PostMapping(value = "/saveCustomer")
    @ResponseBody
    public AjaxJson saveCustomer(@RequestBody AddModelData addModelData) {
        return success(flowModelService.saveCustomer(addModelData));
    }
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(@RequestParam("ids[]") List<String> ids) {
        return success(flowModelService.batchDelete(ids));
    }
    @GetMapping("/update")
    public String update(@Param("modelId") String modelId, Model model) {
        //一个moedlId只会有一条记录
        FlowModel flowModel = flowModelService.getById(modelId);
        model.addAttribute("name", flowModel.getName());
        model.addAttribute("corpName", flowModel.getCorpName());
        model.addAttribute("corpCode", flowModel.getCorpCode());
        model.addAttribute("modelId", flowModel.getId());
        model.addAttribute("revokeAble", flowModel.isRevokeAble() ? CommonConstant.YES : CommonConstant.NO);
        return "bpm/model/update";
    }
    @PostMapping(value = "/update")
    @ResponseBody
    public Object updateModel(String modelId, String revokeAble) {
        return success(flowModelService.updateRevoke(modelId, revokeAble));
    }
    /**
     * 部署
     *
     * @param ids 模型Ids
     * @return 是否成功
     */
    @PostMapping("/deploy")
    @ResponseBody
    public AjaxJson deploy(@RequestParam("ids[]") List<String> ids) {
        return success(flowModelService.batchDeploy(ids));
    }
    /**
     * 查询分类下流程模型
     */
    @PostMapping("/tableGrid")
    @ResponseBody
    public TableGridResponse<FlowModelVo> tableGrid(@RequestBody TableGridInput input) {
        //todo
        ModelQuery modelQuery = new ModelQuery().setCategoryId(input.getParameter().getPara("categoryId"))
                .setCorpId(input.getParameter().getPara("corpId"));
        if (StringUtil.isNotEmpty(input.getParameter().getPara("status"))) {
            modelQuery.setStatus(input.getParameter().getParaToInt("status"));
        }
        if (StringUtil.isNotEmpty(input.getParameter().getPara("sidx"))) {
            String sidx = input.getParameter().getPara("sidx").toString();
            modelQuery.setIsCreateTime(sidx.equals("modelCreateTime"));
        }
        if (StringUtil.isNotEmpty(input.getParameter().getPara("sord"))) {
            String sord = input.getParameter().getPara("sord").toString();
            modelQuery.setAsc(sord.equals("asc"));
        }
        modelQuery.setStart(input.getStart());
        modelQuery.setPageSize(input.getPageSize());
        modelQuery.setPageNo(input.getPageNumber());
        return flowModelService.pageModel(modelQuery,getUserId());
    }
    @GetMapping("/view/{flowCategoryId}")
    public String view(@PathVariable("flowCategoryId") String flowCategoryId, Model model) {
        FlowCategory flowCategory = categoryService.getById(flowCategoryId);
        model.addAttribute("flowCategory", flowCategory);
        return "bpm/model/view";
    }
    @GetMapping("/{modelId}/json")
    @ResponseBody
    public ObjectNode getEditorJson(@PathVariable("modelId") String modelId){
        FlowModel flowModel = flowModelService.getById(modelId);
        AssertHelper.isTrue(Objects.nonNull(flowModel), ModelMessageEnum.NO_START_EVENT);
        logger.info("模型数据{}", flowModel.getModelData());
        ObjectNode objNode = JsonUtils.createObjNode();
        objNode.put("name", flowModel.getName());
        objNode.put("model_id", flowModel.getId());
        return objNode;
    }
    @GetMapping("/{modelId}")
    @ResponseBody
    public AjaxJson getModelId(@PathVariable("modelId") String modelId){
        return success(flowModelService.findVoById(modelId));
    }

    /**
     * 流程复制功能
     * @param modelId 模型ID
     */
    @GetMapping("/copyPage")
    public void copyModelPage(String modelId, Model model) {
        FlowModel flowModel = flowModelService.getById(modelId);
        FlowCategory flowCategory = categoryService.getById(flowModel.getCategoryId());
        //流程分类已经分配单位
        List<FlowModel> existFlowModels = flowModelService.listByCateId(flowCategory.getId());
        List<String> existCorpIds = existFlowModels.stream()
                .map(FlowModel::getCorpId).collect(Collectors.toList());
        //找出该单位已有流程设计的流程分类
        List<FlowModel> existCorpModels = flowModelService.listByCorpId(flowModel.getCorpId());
        List<String> existCategoryIdList = existCorpModels.stream()
                .map(FlowModel::getCategoryId).collect(Collectors.toList());
        model.addAttribute("corpIdList", existCorpIds);
        model.addAttribute("categoryIdList", existCategoryIdList);
        model.addAttribute("flowCategory", flowCategory);
        model.addAttribute("modelId", modelId);
    }

    /**
     * 批量复制流程
     *
     * @param modelId     流程id
     * @param corpIdGroup 目标网点或单位id
     * @return
     * @author lifh
     * @time 2018年6月14日16:14:44修改
     */
    @PostMapping("/copy")
    @ResponseBody
    public Object copyModel(String modelId, @RequestParam(value = "corpId[]") List<String> corpIdGroup) {
        AssertHelper.isTrue(CollectionUtil.isNotEmpty(corpIdGroup), "未选择复制单位");
        return success(flowModelService.batchCopy(corpIdGroup,modelId, FlowCopyParam.CopyType.CORP));
    }

    /**
     * 批量复制流程（跨流程分类复制）
     *
     * @param modelId         流程id
     * @param categoryIdGroup 流程分类id
     * @return
     * @author lifh
     * @time 2019年6月6日
     */

    @PostMapping("/copyCrossCategory")
    @ResponseBody
    public Object copyModelCrossCategory(String modelId, @RequestParam(value = "categoryId[]") List categoryIdGroup) {
        AssertHelper.isTrue(CollectionUtil.isNotEmpty(categoryIdGroup), "未选择复制流程");
        return success(flowModelService.batchCopy(categoryIdGroup,modelId, FlowCopyParam.CopyType.BUSINESS));
    }
}
