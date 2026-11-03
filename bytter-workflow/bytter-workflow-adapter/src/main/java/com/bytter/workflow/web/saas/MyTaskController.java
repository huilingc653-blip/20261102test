package com.bytter.workflow.web.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.MapStringConverter;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.INotificationService;
import com.bytter.workflow.domain.service.FlowCategoryService;
import com.bytter.workflow.domain.service.FlowTaskService;
import com.bytter.workflow.domain.vo.TaskDetailVo;
import com.bytter.workflow.domain.vo.TodoNumberVo;
import com.bytter.workflow.vo.WorkflowDetailPageVO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/7/13.
 * 我的详情任务
 */
@Api(value = "审批任务详情查询",tags = "审批任务详情查询")
@RestController("v1MyTaskController")
@RequestMapping("/v1-workflow/bpm/my")
public class MyTaskController extends BaseController {
    private final FlowCategoryService flowCategoryService;
    private final FlowTaskService flowTaskService;
    @DubboReference
    private INotificationService notificationService;
    public MyTaskController(FlowCategoryService flowCategoryService, FlowTaskService flowTaskService,
                            RepositoryService repositoryService, HistoryService historyService) {
        this.flowTaskService = flowTaskService;
        this.flowCategoryService = flowCategoryService;
    }
    /**
     * 我的任务首页
     */
    @GetMapping("/index")
    public void index() {
    }
    @GetMapping("/todoInfo")
    public void todoInfo(Model model) {
        HashMap<String, Object> flowNumber = new HashMap<String, Object>() {{
        	List<TodoNumberVo> userToApproveData = flowTaskService.getUserTodoData(getUserId());//待审批汇总
        	List<TodoNumberVo> userTrackData = flowTaskService.countTrackNumberGpCateKey(getUserId());//流程跟踪汇总
        	List<TodoNumberVo> userApprovedData = flowTaskService.countFinishGpCateKeyNew(getUserId());//已审批汇总
        	String userTodoData = notificationService.getUserNotification(getUserId());//代办
        	Map<String, Object> userTodoDataMap = MapStringConverter.stringToMap(userTodoData);
        	Long totalToApprove = userToApproveData.stream()
        			.filter(p -> Objects.nonNull(p.getNum())).mapToLong(TodoNumberVo::getNum).sum();// 求和
        	Long totalTrack = userTrackData.stream()
        			.filter(p -> Objects.nonNull(p.getNum())).mapToLong(TodoNumberVo::getNum).sum();// 求和
        	Long totalApproved = userApprovedData.stream()
        			.filter(p -> Objects.nonNull(p.getNum())).mapToLong(TodoNumberVo::getNum).sum();// 求和
        	List<Map<String, Object>> totaluserTodo = (List<Map<String, Object>>) userTodoDataMap.get("counts"); 
        	Integer totalTodo = (Integer) ((Object) totaluserTodo.stream()
                    .map(map -> map.get("num"))      // 获取num值
                    .filter(Objects::nonNull)        // 过滤null值
                    .mapToInt(value -> {
                        if (value instanceof Integer) {
                            return (int) value;  
                        } else if (value instanceof String) {
                            try {
                                return Integer.parseInt((String) value);  // 尝试转换字符串
                            } catch (NumberFormatException e) {
                                return 0;  // 无效字符串返回0
                            }
                        }
                        return 0;  // 其他类型返回0
                    }).sum());
        	
        	
            put("todoCount", totalToApprove);//待审批
            put("startCount", totalTrack);//流程跟踪 审批人或者发起人
            put("notificationCount", totalTodo);//待办
            put("finishedCount", totalApproved > 999 ? 999 : totalApproved);//已审批
        }};
        model.addAttribute("data", flowNumber);
    }
    @GetMapping("/category")
    public void category(Model model) {
        model.addAttribute("categoryList", flowCategoryService.listAllApproveCategoryTree());
    }
    /**
     * 获取当前用户待办数量
     *
     * @return 待办数量
     */
    @GetMapping("/todoNumber")
    @ResponseBody
    public Object todoNumber() {
        return flowTaskService.todoNumber(getUserId());
    }
    /**
     * 用户待办页面
     *
     * @param model
     */
    @GetMapping("/todoPage")
    public void todoPage(Model model) {
        buildPage(model, flowTaskService.getUserTodoData(getUserId()));
    }
    @GetMapping("/trackPage")
    public void trackPage(Model model) {
        buildPage(model, flowTaskService.countTrackNumberGpCateKey(getUserId()));
    }
    private void buildPage(Model model,List<TodoNumberVo> userTodoData) {
        List<String> cateKeys = userTodoData.stream().map(
                TodoNumberVo::getCateKey).collect(Collectors.toList());
        model.addAttribute("categoryList", flowCategoryService.listApproveCategoryTreeInKey(cateKeys));
        model.addAttribute("todoCounts", userTodoData);
    }
    private Map<String,Object> buildMap(List<TodoNumberVo> userTodoData) {
        Map<String, Object> resp = new HashMap<>();
        List<String> cateKeys = userTodoData.stream().map(
                TodoNumberVo::getCateKey).collect(Collectors.toList());
        resp.put("categoryList", flowCategoryService.listApproveCategoryTreeInKey(cateKeys));
        resp.put("todoCounts", userTodoData);
        return resp;
    }
    /**
     * 用户已办理页面
     *
     * @param model
     */
    @GetMapping("/approvedPage")
    public void finishesPage(Model model) {
        buildPage(model, flowTaskService.countFinishGpCateKey(getUserId(), null));
    }
    /**
     * todo 保留以前接口 暂时不知道哪儿在用 后续考虑删除
     * @param map
     */
    @PostMapping("/finishCount")
    @ResponseBody
    public Map<String,Object> finishedPage(@RequestBody Map<String,String> map) {
        return buildMap(flowTaskService.countFinishGpCateKey(getUserId(), map.get("cycleTime")));
    }
    @GetMapping("/startByMySelfPage")
    public void startByMySelfPage() {
    }
    /**
     * 查看审批详情页面
     * @param processInstanceId 流程实例ID
     * @param businessId 业务ID
     */
    @GetMapping("/detailPage")
    public AjaxJson<WorkflowDetailPageVO> detailPage(String processInstanceId, String businessId) {
        WorkflowDetailPageVO pageVO = new WorkflowDetailPageVO();
        if (StringUtils.isBlank(processInstanceId) && StringUtils.isNotBlank(businessId)) {
            processInstanceId = flowTaskService.getProcessInstanceId(businessId);
        }
        if (StringUtils.isBlank(processInstanceId)) {
            pageVO.setNoFlow(true);
            return success(pageVO);
        }
        List<TaskDetailVo> taskDetailVos = flowTaskService.listTaskResponse(processInstanceId);
        pageVO.setDetails(taskDetailVos);
        pageVO.setProcessInstanceId(processInstanceId);
        return success(pageVO);
    }
}
