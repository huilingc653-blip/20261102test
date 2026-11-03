package com.bytter.workflow.domain.service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.bytter.core.common.context.UserContextHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.camunda.bpm.engine.ParseException;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.CompletionCondition;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.MultiInstanceLoopCharacteristics;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.mybatis.DbDialectHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.constant.WorkFlowConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.mapper.FlowModelMapper;
import com.bytter.workflow.domain.param.AddModelData;
import com.bytter.workflow.domain.param.CreateModelForm;
import com.bytter.workflow.domain.param.FlowCopyParam;
import com.bytter.workflow.domain.param.ModelQuery;
import com.bytter.workflow.domain.util.BpmnModelUtils;
import com.bytter.workflow.domain.vo.FlowModelVo;
import com.bytter.workflow.enums.ModelMessageEnum;
import com.bytter.workflow.enums.WorkFlowMessageEnum;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程模型定义，修改
 */
@Service
@Slf4j
public class FlowModelService extends ServiceImpl<FlowModelMapper, FlowModel> {
    private final RepositoryService repositoryService;
    private final FlowCategoryService flowCategoryService;
    private final RuntimeService runtimeService;
    private final DbDialectHelper dbDialectHelper;
    @DubboReference
    private IUserService userService;
    @DubboReference
    private ICorpService corpService;
    public FlowModelService(RepositoryService repositoryService,
                            FlowCategoryService flowCategoryService, RuntimeService runtimeService,
                            DbDialectHelper dbDialectHelper) {
        this.flowCategoryService = flowCategoryService;
        this.repositoryService = repositoryService;
        this.runtimeService = runtimeService;
        this.dbDialectHelper = dbDialectHelper;
    }
    /**
     *
     * @param modelParam 创建模型参数
     * @return 是否成功
     */
    public boolean createModel(CreateModelForm modelParam) {
        existModel(modelParam.getFlowCategoryId(), modelParam.getCorpId());
        return super.save(modelParam.toEntity(flowCategoryService));
    }
    /**
     * 校验单位下某个分类是否已经存在
     * @param cateId 分类ID
     * @param corpId 单位ID
     */
    private void existModel(String cateId, String corpId) {
        Optional<FlowModel> model = this.findOneByCateAndCorpId(cateId, corpId);
        AssertHelper.isTrue(!model.isPresent(), ModelMessageEnum.BPM_MODEL_CORP_ALREADY_EXIST);
    }
    public Optional<FlowModel> findOneByCateAndCorpId(String cateId, String corpId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getCategoryId, cateId)
                .eq(FlowModel::getCorpId, corpId)
                .oneOpt();
    }
    public Optional<FlowModel> findOneByCateKeyAndCorpId(String cateKey, String corpId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getProDefKey, cateKey)
                .eq(FlowModel::getCorpId, corpId)
                .oneOpt();
    }

    /**
     * 查询发布过的流程
     * @return
     */
    public Optional<FlowModel> findDeployByCateKeyAndCorpId(String cateKey, String corpId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getProDefKey, cateKey)
                .eq(FlowModel::getCorpId, corpId)
                .isNotNull(FlowModel::getDeploymentId)
                .oneOpt();
    }

    public Optional<FlowModel> findOneByProDefId(String proDefId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getProDefId, proDefId)
                .last(dbDialectHelper.limitOne())
                .oneOpt();
    }
    /**
     * @param addModelData 模型数据
     * @return 是否保存成功
     */
    public boolean saveCustomer(AddModelData addModelData) {
        FlowModel flowModel = super.getById(addModelData.getModelId());
        AssertHelper.isTrue(Objects.nonNull(flowModel), ModelMessageEnum.BPM_MODEL_NOT_EXIST);
        String modelData = Base64.decodeStr(addModelData.getModelData());
        validateModelTaskNode(modelData);
        flowModel.setModelData(modelData);
        flowModel.setModelVersion(flowModel.getModelVersion() + 1);
        return super.saveOrUpdate(flowModel);
    }
    /**
     * 流程部署批量
     * @param ids 模型ids
     */
    public boolean batchDeploy(List<String> ids) {
        //假批量
        for (String id : ids) {
            deploy(id);
        }
        return Boolean.TRUE;
    }
    /**
     * 流程部署
     * @param id 模型id
     */
    public boolean deploy(String id) {
        FlowModel flowModel = super.getById(id);
        AssertHelper.isTrue(Objects.nonNull(flowModel), ModelMessageEnum.BPM_MODEL_NOT_EXIST);
        AssertHelper.isTrue(StringUtil.isNotEmpty(flowModel.getModelData()),ModelMessageEnum.BPM_MODEL_DATA_EXIST);
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(flowModel.getModelData().getBytes(StandardCharsets.UTF_8)));
        //扩展变量
        validateModel(modelInstance);
        //setCamundaCandidate(modelInstance, flowModel.getCorpId()); //部署阶段设置候选人 改为 动态解析角色
        assembleUserTaskMultiInstanceConfig(modelInstance); // 设置任务的多实例配置
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        DeploymentWithDefinitions deployment;
        try {
             deployment = deploymentBuilder.name(flowModel.getName())
                    .addString(flowModel.getName()+".bpmn20.xml", Bpmn.convertToString(modelInstance))
//                    .tenantId(flowModel.getCorpId()).deployWithResult();
                    .tenantId(UserContextHelper.helper().getTenantId()).deployWithResult();
        } catch (ParseException exception) {
            log.error("流程设计异常", exception);
            throw new BusinessException(WorkFlowMessageEnum.WORK_FLOW_DESIGN_ERROR);
        }

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        if (Objects.nonNull(processDefinition)) {
            // 设置流程定义ID
            flowModel.setProDefId(processDefinition.getId());
        }
        flowModel.setDeploymentId(deployment.getId());
        Integer preDeployVersion = flowModel.getDeploymentVersion();
        //线程安全问题
        flowModel.setDeploymentVersion(Objects.isNull(preDeployVersion) ? 1 : ++preDeployVersion);
        return super.saveOrUpdate(flowModel);
    }
    /**
     * UserTask多实例扩展
     *
     * @param modelInstance 流程模型实例
     */
    private void assembleUserTaskMultiInstanceConfig(BpmnModelInstance modelInstance) {
        // 获取BPMN模型中的用户任务列表
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        for (UserTask userTask : userTasks) {
            // 设置任务的多实例配置
            setUserTaskMultiInstanceConfig(modelInstance, userTask);
        }
    }

    /**
     *设置人选 部署阶段设置候选人 --> 改为动态解析审批人：AssigneeListener
     * @param modelInstance
     * @param corpId
     */
    private void setCamundaCandidate(BpmnModelInstance modelInstance,String corpId) {
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        for (UserTask userTask : userTasks) {
            //审批用户
            Set<String> candidates = new HashSet<>();
            //用户设置
            List<String> candidatesByUid = userTask.getCamundaCandidateUsersList();
            if (!CollectionUtils.isEmpty(candidatesByUid)) {
                candidates.addAll(candidatesByUid);
            }
            List<String> roleIds = userTask.getCamundaCandidateGroupsList();
            List<String> candidatesByRid = userService.listApproveUserIdsByRoleIdAndCorpId(roleIds, corpId, null);
            if(!CollectionUtils.isEmpty(candidatesByRid)){
                candidates.addAll(candidatesByRid);
            }
            AssertHelper.isTrue(candidates.size() > 0, userTask.getName() + "未配置审批用户");
            userTask.setCamundaCandidateUsersList(new ArrayList<>(candidates));
        }
    }

    /**
     * 设置任务的多实例配置
     */
    private void setUserTaskMultiInstanceConfig(BpmnModelInstance modelInstance, UserTask userTask) {
        Collection<MultiInstanceLoopCharacteristics> loopCharacteristics = userTask
                .getChildElementsByType(MultiInstanceLoopCharacteristics.class);
        if (!CollectionUtils.isEmpty(loopCharacteristics)) {
            // 移除子元素再重新添加
            for (MultiInstanceLoopCharacteristics mlc : loopCharacteristics) {
                userTask.removeChildElement(mlc);
            }
        }
        // 获取最少审批人数
        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = modelInstance
                .newInstance(MultiInstanceLoopCharacteristics.class);
        // 获取最少审批人数
        String passNumberStr = BpmnModelUtils.getUserTaskPassNumberString(userTask);

        /*
            Camunda引擎中支持多实例的行为主要是：MultiInstanceActivityBehavior，
            以及集成这个类的：SequentialMultiInstanceActivityBehavior和ParallelMultiInstanceActivityBehavior，
            这两个类的响应是通过多实例扩展元素的属性isSequential="true" 来决定的。
        */
        multiInstanceLoopCharacteristics.setSequential(false);
        // completionCondition  表示是任务往下跳转的完成条件，返回true是，表示条件成立，流程会跳至下一审批环节。
        CompletionCondition completionCondition = modelInstance.newInstance(CompletionCondition.class);
        /*
            nrOfInstances：实例总数
            nrOfActiveInstances：当前活动的，还没完成的实例数量。 对于顺序执行的多实例，值一直为1。
            nrOfCompletedInstances：已经完成实例的数目。
            完成条件格式：${nrOfCompletedInstances/nrOfInstances >= passNumber/nrOfInstances} ，passNumber为最少审批人数
        */
        completionCondition.setTextContent(String.format("${nrOfCompletedInstances/nrOfInstances >= %s/nrOfInstances}", passNumberStr));
        multiInstanceLoopCharacteristics.setCompletionCondition(completionCondition);
        // 通过类方法返回审批人的集合 multiInstanceLoopCharacteristics.setCamundaCollection("${counterSignService.getApproveUsers(execution)}")
        multiInstanceLoopCharacteristics.setCamundaElementVariable("assignee");
        multiInstanceLoopCharacteristics.setCamundaCollection("assigneeList");
        userTask.setCamundaAssignee("${assignee}");
        userTask.addChildElement(multiInstanceLoopCharacteristics);
    }
    /**
     * 流程模型验证
     * @param modelInstance 模型实例
     */
    public void validateModel(BpmnModelInstance modelInstance){
        Bpmn.validateModel(modelInstance);
        Collection<StartEvent> startEvents = modelInstance.getModelElementsByType(StartEvent.class);
        AssertHelper.isTrue(!startEvents.isEmpty(), ModelMessageEnum.NO_START_EVENT);
        Collection<EndEvent> endEvents = modelInstance.getModelElementsByType(EndEvent.class);
        AssertHelper.isTrue(!endEvents.isEmpty(), ModelMessageEnum.NO_END_EVENT);
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        AssertHelper.isTrue(!userTasks.isEmpty(), ModelMessageEnum.NO_USER_TASK);
        userTasks.forEach(userTask -> {
            AssertHelper.isTrue(StringUtil.isNotEmpty(userTask.getName()), "用户节点名称不能为空");

            List<String> candidateUsersList = userTask.getCamundaCandidateUsersList();
            List<String> candidateGroupsList = userTask.getCamundaCandidateGroupsList();
            List<String> candidates = new ArrayList<>();
            candidates.addAll(candidateUsersList);
            candidates.addAll(candidateGroupsList);
            AssertHelper.isTrue(candidates.size() > 0, userTask.getName() + "未配置审批用户");
        });
    }

    /**
     * 保存是校验用户节点名称
     */
    public void validateModelTaskNode(String modelData) {
        if (StringUtil.isBlank(modelData)) {
            return;
        }
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(new ByteArrayInputStream(modelData.getBytes(StandardCharsets.UTF_8)));
        Collection<UserTask> userTasks = modelInstance.getModelElementsByType(UserTask.class);
        userTasks.forEach(userTask -> {
            AssertHelper.isTrue(StringUtil.isNotEmpty(userTask.getName()), "用户节点名称不能为空");
        });
    }
    /**
     * 查询流程模型
     * @param modelQuery 查询条件
     * @return
     */
    public TableGridResponse<FlowModelVo> pageModel(ModelQuery modelQuery,String userId) {
        //获取用户权限单位
        List<String> userCorpIds = corpService.getUserCorpIds(WorkFlowConstant.RESOURCE_BPM_MODE_OP, userId);
        Page<FlowModel> page = Page.of(modelQuery.getPageNo(), modelQuery.getPageSize());
        IPage<FlowModelVo> iPage = null;
        if(modelQuery.getStatus()==null) {
            /*iPage = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
                    .eq(Objects.nonNull(modelQuery.getStatus()), FlowModel::getStatus, modelQuery.getStatus())
                    .page(page).convert(FlowModelVo::from);*/
            LambdaQueryChainWrapper<FlowModel> queryChainWrapper = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
                    .eq(Objects.nonNull(modelQuery.getStatus()), FlowModel::getStatus, modelQuery.getStatus());
            if (modelQuery.getIsCreateTime() != null){
                queryChainWrapper.orderBy(true, modelQuery.isAsc(), modelQuery.getIsCreateTime()?FlowModel::getCreateDate:FlowModel::getUpdateDate);
            }
            iPage = queryChainWrapper.page(page).convert(FlowModelVo::from);
        }else if(modelQuery.getStatus()==0) {
        	/*iPage = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
//                    .eq(Objects.nonNull(modelQuery.getStatus()), FlowModel::getStatus, modelQuery.getStatus())
                    .isNull(FlowModel::getDeploymentVersion)
                    .page(page).convert(FlowModelVo::from);*/
            LambdaQueryChainWrapper<FlowModel> queryChainWrapper = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
                    .isNull(FlowModel::getDeploymentVersion);
            if (modelQuery.getIsCreateTime() != null){
                queryChainWrapper.orderBy(true, modelQuery.isAsc(), modelQuery.getIsCreateTime()?FlowModel::getCreateDate:FlowModel::getUpdateDate);
            }
            iPage = queryChainWrapper.page(page).convert(FlowModelVo::from);
        }else if(modelQuery.getStatus()==1){
        	/*iPage = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
                    .eq(Objects.nonNull(modelQuery.getStatus()), FlowModel::getStatus, modelQuery.getStatus())
                    .isNotNull(FlowModel::getDeploymentVersion)
                    .page(page).convert(FlowModelVo::from);*/
            LambdaQueryChainWrapper<FlowModel> queryChainWrapper = new LambdaQueryChainWrapper<>(FlowModel.class)
                    .eq(StringUtil.isNotEmpty(modelQuery.getCategoryId()),FlowModel::getCategoryId, modelQuery.getCategoryId())
                    .eq(StringUtil.isNotEmpty(modelQuery.getCorpId()), FlowModel::getCorpId, modelQuery.getCorpId())
                    .in(CollectionUtil.isNotEmpty(userCorpIds), FlowModel::getCorpId, userCorpIds)
                    .eq(Objects.nonNull(modelQuery.getStatus()), FlowModel::getStatus, modelQuery.getStatus())
                    .isNotNull(FlowModel::getDeploymentVersion);
            if (modelQuery.getIsCreateTime() != null){
                queryChainWrapper.orderBy(true, modelQuery.isAsc(), modelQuery.getIsCreateTime()?FlowModel::getCreateDate:FlowModel::getUpdateDate);
            }
            iPage = queryChainWrapper.page(page).convert(FlowModelVo::from);
        }

        return new TableGridResponse(modelQuery.getPageNo(),modelQuery.getPageSize(),BigDecimal.valueOf(iPage.getTotal()).intValue(),iPage.getRecords());
    }
    /**
     * 批量删除 todo 使用batchService
     * @param ids 待删除流程定义ID
     * @return 是否删除成功
     */
    public Boolean batchDelete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        for (String id : ids) {
            // 20191226 只删除工作流模型，不删除历史记录，运行记录 update
            delete(id);
        }
        return Boolean.TRUE;
    }
    public Boolean delete(String id) {
        FlowModel flowModel = getById(id);
        if (Objects.nonNull(flowModel)) {
            // 已经部署的流程：删除之前判断是否有运行中的流程，如果有则不允许删除
            String deploymentId = flowModel.getDeploymentId();
            if (StringUtil.isNotEmpty(deploymentId)) {
                long runningProcessInstanceCount = runtimeService.createProcessInstanceQuery()
                        .deploymentId(flowModel.getDeploymentId())
                        .count();
                AssertHelper.isTrue(runningProcessInstanceCount <= 0, "存在待审批数据不能删除");
            }
            removeById(id);
            if (!StringUtils.isEmpty(deploymentId)) {
                // cascade  是否级联删除流程实例
                repositoryService.deleteDeployment(deploymentId, false);
            }
        }
        return Boolean.TRUE;
    }
    /**
     * 更新模型是否运行撤回
     * @param modelId modelId
     * @param revokeAble 是否允许撤回
     * @return 更新是否成功
     */
    public Boolean updateRevoke(String modelId, String revokeAble) {
        FlowModel flowModel = getById(modelId);
        flowModel.setRevokeAble(CommonConstant.YES.equals(revokeAble));
        return updateById(flowModel);
    }
    public FlowModelVo findVoById(String modelId) {
        FlowModelVo flowModelVo = FlowModelVo.from(getById(modelId));
        //流程定义key 当流程未发布时key可能为空
        if (StringUtil.isBlank(flowModelVo.getProDefKey())) {
            FlowCategory flowCategory = flowCategoryService.getById(flowModelVo.getCategoryId());
            flowModelVo.setProDefKey(flowCategory.getCateKey());
        }
        return flowModelVo;
    }

    /**
     * 查询流程ID下已有模型
     * @param cateId 分类ID
     * @return
     */
    public List<FlowModel> listByCateId(String cateId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getCategoryId, cateId).list();
    }

    /**
     * 查询单位已分配模型
     * @param corpId 单位Id
     * @return
     */
    public List<FlowModel> listByCorpId(String corpId) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(FlowModel::getCorpId, corpId).list();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean batchCopy(List<String> copyKeys, String modelId, FlowCopyParam.CopyType copyType) {
        FlowModel flowModel = getById(modelId);
        AssertHelper.isTrueBusEx(Objects.nonNull(flowModel), ModelMessageEnum.BPM_MODEL_NOT_EXIST);
        FlowCopyParam flowCopyParam = FlowCopyParam.buildCopyParam(copyType, copyKeys, flowModel);
        List<FlowModel> flowModels = listFlowModels(flowCopyParam);
        Map<String, FlowModel> flowModelMap = build2Map(flowModels, FlowCopyParam.CopyType.CORP == copyType);
        Map<String, CorpDTO> corpMap;
        Map<String, FlowCategory> categoryMap;
        if (FlowCopyParam.CopyType.CORP == copyType) {
            categoryMap = null;
            corpMap = corpService.listCorpDtoByCorpIds(copyKeys);
        }else {
            corpMap = null;
            categoryMap = flowCategoryService.mapGroupById(copyKeys);
        }
        List<FlowModel> copyModels = copyKeys.stream().filter(copyKey -> !flowModelMap.containsKey(copyKey))
                .map(copyKey ->FlowCopyParam.CopyType.CORP == copyType ?
                        buildCorpCopyModel(flowModel, corpMap, copyKey):buildCateCopyModel(flowModel,categoryMap,copyKey)
                ).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(copyModels)) {
            return false;
        }
        return saveBatch(copyModels, 100);
    }

    private FlowModel buildCorpCopyModel(FlowModel oriModel,Map<String, CorpDTO> corpMap,String corpId) {
        FlowModel copyModel = new FlowModel();
        BeanUtils.copyProperties(oriModel, copyModel);
        //单位复制复制
        CorpDTO corpDTO = corpMap.get(corpId);
        copyModel.setCorpId(corpDTO.getId());
        copyModel.setCorpCode(corpDTO.getCode());
        copyModel.setCorpName(corpDTO.getName());
        addDefaultValue(copyModel);
        return copyModel;
    }

    private FlowModel buildCateCopyModel(FlowModel oriModel, Map<String, FlowCategory> categoryMap, String cateId)  {
        FlowModel copyModel = new FlowModel();
        BeanUtils.copyProperties(oriModel, copyModel);
        //分类复制
        FlowCategory flowCategory = categoryMap.get(cateId);
        if (StringUtil.isNotEmpty(oriModel.getModelData())) {
            try {
                Document document = DocumentHelper.parseText(oriModel.getModelData());
                Element rootElement = document.getRootElement();
                Element process = rootElement.element("process");
                Attribute id = process.attribute("id");
                id.setValue(flowCategory.getCateKey());
                Attribute name = process.attribute("name");
                name.setValue(flowCategory.getName());
                Element diagram = rootElement.element("BPMNDiagram");
                if(diagram!=null){
                    Element element = diagram.element("BPMNPlane");
                    Attribute bpmnElement = element.attribute("bpmnElement");
                    bpmnElement.setValue(flowCategory.getCateKey());
                }
                copyModel.setModelData(document.asXML());
            } catch (Exception exception) {
                log.error("模型解析异常：：", exception);
                throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_UNKNOWN_ERROR);
            }
        }
        copyModel.setCategoryId(flowCategory.getId());
        copyModel.setName(flowCategory.getName());
        copyModel.setProDefKey(flowCategory.getCateKey());
        addDefaultValue(copyModel);
        return copyModel;
    }

    private void addDefaultValue(FlowModel flowModel) {
        flowModel.setId(null);
        flowModel.setModelVersion(1);
        flowModel.setStatus(FlowModel.Status.START.getCode());
        flowModel.setVersion(1);
        flowModel.setDeploymentId(null);
        flowModel.setDeploymentVersion(null);
        flowModel.setCreateDate(new Date());
        flowModel.setUpdateDate(null);
        flowModel.setProDefId(null);
    }

    private Map<String, FlowModel> build2Map(List<FlowModel> flowModels,boolean isCorp) {
        return flowModels.stream()
                .collect(Collectors.toMap(item -> isCorp ? item.getCorpId() : item.getCategoryId(), item -> item));

    }

    public List<FlowModel> listFlowModels(FlowCopyParam copyParam) {
        return new LambdaQueryChainWrapper<>(FlowModel.class)
                .eq(StringUtil.isNotEmpty(copyParam.getCateId()), FlowModel::getCategoryId, copyParam.getCateId())
                .eq(StringUtil.isNotEmpty(copyParam.getCorpId()), FlowModel::getCorpId, copyParam.getCorpId())
                .in(!CollectionUtils.isEmpty(copyParam.getCateIds()), FlowModel::getCategoryId, copyParam.getCateIds())
                .in(!CollectionUtils.isEmpty(copyParam.getCorpIds()), FlowModel::getCorpId, copyParam.getCorpIds())
                .list();

    }
}
