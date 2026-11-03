package com.bytter.workflow.web.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.domain.entity.FlowDelegation;
import com.bytter.workflow.domain.param.FlowDelegationQuery;
import com.bytter.workflow.domain.service.FlowDelegationRelService;
import com.bytter.workflow.domain.service.FlowDelegationService;
import com.bytter.workflow.domain.vo.FlowDelegationVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Created by lifh on 2018/11/19
 * 流程转授功能
 */
@Controller
@RequestMapping("/bpm/delegation")
public class FlowDelegationController extends BaseController {
    private final FlowDelegationService flowDelegationService;
    private final FlowDelegationRelService flowDelegationRelService;
    public FlowDelegationController(FlowDelegationService flowDelegationService, FlowDelegationRelService flowDelegationRelService) {
        this.flowDelegationService = flowDelegationService;
        this.flowDelegationRelService = flowDelegationRelService;
    }
    //列表页
    @GetMapping("/list")
    public String list(Model model) {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "bpm/delegation/list";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        ICurrentUser iCurrentUser = UserContextHelper.helper().getCurrentUser();
        FlowDelegationQuery delegationQuery = changeDateInput2Query(dataTablesInput);
        if (!iCurrentUser.isAdmin()) {
            delegationQuery.setPrincipal(iCurrentUser.getId());
            delegationQuery.setPrincipalCorpId(iCurrentUser.getCorpId());
        }
        return flowDelegationService.getDataTablesUnion(delegationQuery);
    }
    private FlowDelegationQuery changeDateInput2Query(DataTablesInput dataTablesInput) {
        FlowDelegationQuery delegationQuery = new FlowDelegationQuery();
        delegationQuery.setStart(dataTablesInput.getStart());
        delegationQuery.setPageSize(dataTablesInput.getPageSize());
        delegationQuery.setDraw(dataTablesInput.getDraw());
        String gedStartTime = (String) dataTablesInput.getFormdata().get("ged_startTime");
        String ledStartTime = (String) dataTablesInput.getFormdata().get("led_startTime");
        if (StringUtils.isNotEmpty(gedStartTime)) {
            gedStartTime = gedStartTime + " 00:00:00";
            delegationQuery.setStartTime(DateUtils.parse(gedStartTime));
        }
        if (StringUtils.isNotEmpty(ledStartTime)) {
            ledStartTime = ledStartTime + " 23:59:59";
            delegationQuery.setEndTime(DateUtils.parse(ledStartTime));
        }
        return delegationQuery;
    }
    //流程转授确认页面
    @GetMapping("/confirm/list")
    public String confirm(Model model) {
        model.addAttribute("currentUser", UserContextHelper.helper().getCurrentUser());
        return "bpm/delegation/confirm";
    }
    //表格
    @PostMapping("/confirm/datatable")
    @ResponseBody
    public DataTablesOutput confirmDataTable(@RequestBody DataTablesInput dataTablesInput) {
        FlowDelegationQuery delegationQuery = changeDateInput2Query(dataTablesInput);
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        delegationQuery.setProxy(currentUser.getId());
        delegationQuery.setStatus(FlowDelegationVo.Status.WAIT);
        return flowDelegationService.getDataTablesUnion(delegationQuery);
    }
    /**
     * 新增页面
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        //当前用户
        model.addAttribute("currentUser", UserContextHelper.helper().getCurrentUser());
        model.addAttribute("today", DateUtils.format(new Date()));
        return "bpm/delegation/add";
    }
    /**
     * 转授配置选择业务
     *
     * @param model
     * @return
     */
    @RequestMapping("/chooseCategory")
    public String chooseCategory(Model model) {
        return "bpm/delegation/chooseCategory";
    }
    //提交
    @RequestMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FlowDelegationVo flowDelegationVo) throws ParseException {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        flowDelegationService.commit(flowDelegationVo, currentUser);
        return success();
    }

    //暂存
    @RequestMapping("/storage")
    @ResponseBody
    public AjaxJson storage(@RequestBody FlowDelegationVo delegationVo) throws ParseException {
        flowDelegationService.storage(delegationVo, getCurrentUser());
        return success();
    }

    /**
     * 编辑页面
     *
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        //根据网点获取用户，用于下拉框初始化
        //当前用户
        FlowDelegation delegation = flowDelegationService.getById(id);
        FlowDelegationVo delegationVo = new FlowDelegationVo();
        BeanUtils.copyProperties(delegation, delegationVo);
        ICurrentUser currentUser = getCurrentUser();
        addFlowCateIdList(delegation.getId(), model);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("delegation", delegationVo);
        model.addAttribute("today", LocalDate.now());
        return "bpm/delegation/edit";
    }

    private void addFlowCateIdList(String deleId, Model model) {
        List<String> cateIds = flowDelegationRelService.listCateIdsByDelegationId(deleId);
        if (CollectionUtil.isNotEmpty(cateIds)) {
            model.addAttribute("flowCategoryIdList", String.join(",", cateIds));
        }
    }

    /**
     * 查看转授的业务
     *
     * @param id    授权配置的id
     * @param model
     * @return
     */
    @GetMapping("/detailCategory")
    public String detailCategory(String id, Model model) {
        addFlowCateIdList(id, model);
        return "bpm/delegation/detailCategory";
    }
    //批量复制
    @PostMapping("/copyBatch")
    @ResponseBody
    public AjaxJson copyBatch(@RequestBody List<String> idList) throws ParseException {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        flowDelegationService.copyBatch(idList, currentUser);
        return success();
    }

    //批量删除
    @PostMapping("/deleteBatch")
    @ResponseBody
    public AjaxJson deleteBatch(@RequestBody List<String> idList) {
        ICurrentUser currentUser = getCurrentUser();
        flowDelegationService.deleteBatch(idList, currentUser);
        return success();

    }

    //打回
    @PostMapping("/reject")
    @ResponseBody
    public AjaxJson reject(String id) {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        flowDelegationService.reject(id, currentUser);
        return success();
    }
    //确认
    @PostMapping("/sure")
    @ResponseBody
    public AjaxJson confirm(String id) {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        flowDelegationService.sure(id, currentUser);
        return success();
    }
    //转回
    @PostMapping("/finish")
    @ResponseBody
    public AjaxJson finish(String id) {
        ICurrentUser currentUser = UserContextHelper.helper().getCurrentUser();
        flowDelegationService.finish(id, currentUser);
        return success();
    }
}
