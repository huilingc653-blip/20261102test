package com.bytter.workflow.domain.util;
import com.google.common.collect.Maps;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * 流程模型工具类
 *
 * @author chenlw 2021-05-11
 * @since 1.0.0
 */
public class BpmnModelUtils {
    /**
     * 获取流程模型实例对象
     *
     * @param bpmnXmlString 流程模型XML数据
     * @return 流程模型实例对象
     */
    public static BpmnModelInstance getBpmnModelInstance(String bpmnXmlString) {
        return Bpmn.readModelFromStream(new ByteArrayInputStream(
                bpmnXmlString.getBytes(StandardCharsets.UTF_8)));
    }
    /**
     * 获取最少审批人数
     *
     * @param userTask 用户任务
     * @return 最少审批人数
     */
    public static String getUserTaskPassNumberString(UserTask userTask) {
        Map<String, String> extensionParams = getModelExtensionParams(userTask);
        return extensionParams.get("passNumber");
    }
    /**
     * 获取扩展参数
     *
     * @param userTask 审批任务
     * @return 扩展参数Map
     */
    public static Map<String, String> getModelExtensionParams(UserTask userTask) {
        ExtensionElements extensionElements = userTask.getExtensionElements();
        Map<String, String> extensionParams = Maps.newHashMap();
        if (Objects.nonNull(extensionElements)) {
            Collection<ModelElementInstance> modelElementInstances = extensionElements.getElements();
            for (ModelElementInstance modelElementInstance : modelElementInstances) {
                if (modelElementInstance instanceof CamundaPropertiesImpl) {
                    CamundaPropertiesImpl camundaProperties = (CamundaPropertiesImpl) modelElementInstance;
                    camundaProperties.getCamundaProperties().stream().forEach((camundaProperty -> {
                        extensionParams.put(camundaProperty.getCamundaName(), camundaProperty.getCamundaValue());
                    }));
                }
            }
        }
        return extensionParams;
    }
    /**
     * 查找指定的用户审批节点
     *
     * @param modelInstance 流程模型实例
     * @param activityId    审批节点ID
     * @return 用户审批节点
     */
    public static UserTask findUserTask(BpmnModelInstance modelInstance, String activityId) {
        if (Objects.isNull(modelInstance)
                || StringUtils.isEmpty(activityId)) {
            return null;
        }
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        for (UserTask userTask : userTasks) {
            if (activityId.contains(userTask.getId())) {
                return userTask;
            }
        }
        return null;
    }
    /**
     * 查找上一级的审批任务集合
     *
     * @param flowNode      节点
     * @param backUserTasks 上一级的审批任务集合
     */
    public static void findBackUserTask(FlowNode flowNode, List<UserTask> backUserTasks) {
        if (Objects.isNull(flowNode)) {
            return;
        }
        if (flowNode instanceof StartEvent) {
            return;
        }
        if (flowNode instanceof UserTask) {
            backUserTasks.add((UserTask) flowNode);
            return;
        }
        // 获取前边的元素集合
        List<FlowNode> previousNodes = flowNode.getPreviousNodes().list();
        if (CollectionUtils.isEmpty(previousNodes)) {
            return;
        }
        for (FlowNode previousNode : previousNodes) {
            findBackUserTask(previousNode, backUserTasks);
        }
    }
}
