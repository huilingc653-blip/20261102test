package com.bytter.workflow.domain.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.domain.entity.FlowDelegation;
import com.bytter.workflow.domain.entity.FlowDelegationRel;
import com.bytter.workflow.domain.mapper.FlowDelegationMapper;
import com.bytter.workflow.domain.param.FlowDelegationQuery;
import com.bytter.workflow.domain.vo.FlowDelegationVo;
import com.bytter.workflow.enums.DelegationStatusEnum;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.assertj.core.util.Lists;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.DelegationState;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author lifh
 */
@Service("DelegationService")
@Transactional
@Slf4j
public class FlowDelegationService extends ServiceImpl<FlowDelegationMapper, FlowDelegation> {
    @DubboReference
    private IUserService userService;
    @Autowired
    private FlowDelegationRelService flowDelegationRelService;
    @Autowired
    private FlowCategoryService flowCategoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private FlowDelegationMapper flowDelegationMapper;
    private static final String DELEGATION_CACHE_NAME = "delegationCache";
    /**
     * 暂存转授配置
     *
     * @param delegationVo
     * @param currentUser
     */
    @CacheEvict(value = DELEGATION_CACHE_NAME, allEntries = true)
    public void storage(FlowDelegationVo delegationVo, ICurrentUser currentUser) throws ParseException {
        delegationVo.setStatus(FlowDelegationVo.Status.STORAGE.getCode());
        this.save(delegationVo, currentUser);
    }
    /**
     * 提交转授配置
     *
     * @param delegationVo
     * @param currentUser
     */
    @CacheEvict(value = DELEGATION_CACHE_NAME, allEntries = true)
    public void commit(FlowDelegationVo delegationVo, ICurrentUser currentUser) throws ParseException {
        //获得已存在转授的申请
        List<String> categoryIdList = checkCategory(delegationVo);
        if (CollectionUtils.isEmpty(categoryIdList)) {
            delegationVo.setStatus(FlowDelegationVo.Status.WAIT.getCode());
            this.save(delegationVo, currentUser);
        }
       //todo 校验是否已经存在逻辑
    }
    /**
     * 打回
     *
     * @param id
     * @param currentUser
     */
    public void reject(String id, ICurrentUser currentUser) {
        FlowDelegation delegation = this.getById(id);
        AssertHelper.isTrue(FlowDelegationVo.Status.WAIT.getCode().equals(delegation.getStatus()), WorkFlowMessageEnum.BPM_DELEGATION_STATUS_ERROR);
        delegation.setStatus(FlowDelegationVo.Status.REJEC.getCode());
        updateStatus(delegation, currentUser);
    }
    /**
     * 确认
     *
     * @param id
     * @param currentUser
     */
    public void sure(String id, ICurrentUser currentUser) {
        Date sureTime = new Date();
        //判断当前时间。转授的时候如果时间是在开始时间之后，
        FlowDelegation delegation = this.getById(id);
        AssertHelper.isTrue(FlowDelegationVo.Status.WAIT.getCode().equals(delegation.getStatus()), WorkFlowMessageEnum.BPM_DELEGATION_STATUS_ERROR);
        delegation.setStatus(FlowDelegationVo.Status.SURE.getCode());
        updateStatus(delegation, currentUser);
        //确认时间在开始时间之后,才会去检查未审批的流程
        if (sureTime.compareTo(delegation.getStartTime()) < 0) {
            return;
        }
        delegateUnfinishedTask(delegation);
    }
    /**
     * 通过转授配置，把未审批的task转给被转授人
     *
     * @param delegation 状态需为已确认DelegationStatusEnum.SURE.getStatus()
     */
    public void delegateUnfinishedTask(FlowDelegation delegation) {
        /**
         * 业务需求：原来提交至授权人审批的，流转至被授权人审批；
         */
        String[] categoryKeys = flowDelegationRelService.listCateKeysByDelegationId(delegation.getId());
        if (categoryKeys.length == 0) {
            return;
        }
        //确认时，找通过网点（tenant）和assign的内容。找到task。
        //最后通过分类去修改task的处理人，设置成委托事件
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(delegation.getPrincipal())
                .taskDelegationState(null)
                .processDefinitionKeyIn(categoryKeys)
                .list();
        if (CollectionUtils.isEmpty(taskList)) {
            return;
        }
        for (Task task : taskList) {
            taskService.delegateTask(task.getId(), delegation.getProxy());
        }
    }
    /**
     * 转回。
     * 业务需求：被授权人还没审批结束就转回的业务，不再提交至被授权人审批。
     *
     * @param id
     * @param currentUser
     */
    public void finish(String id, ICurrentUser currentUser) {
        Date endTime = new Date();
        FlowDelegation delegation = this.getById(id);
        if (FlowDelegationVo.Status.SURE.getCode().equals(delegation.getStatus())) {
            throw new BusinessException(WorkFlowMessageEnum.BPM_DELEGATION_STATUS_ERROR);
        }
        delegation.setStatus(FlowDelegationVo.Status.FINISH.getCode());
        delegation.setEndTime(endTime);
        updateStatus(delegation, currentUser);
        turnBackTask(delegation);
    }
    /**
     * 通过转授配置，把未审批的task转回转授人
     *
     * @param delegation 状态需为 结束 DelegationStatusEnum.FINISH.getStatus()
     */
    public void turnBackTask(FlowDelegation delegation) {
        String[] categoryKeys = flowDelegationRelService.listCateKeysByDelegationId(delegation.getId());
        if (categoryKeys.length == 0) {
            return;
        }
        /**
         * 业务需求：被授权人还没审批结束就转回的业务，不再提交至被转授人审批
         */
        //确认时，找通过网点（tenant）和assign的内容。找到task。
        //最后通过分类去修改task的处理人，设置成委托事件
        //查找已经被转授的task，
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(delegation.getProxy())
                .taskOwner(delegation.getPrincipal())
                .taskDelegationState(DelegationState.PENDING)
                .processDefinitionKeyIn(categoryKeys)
                .list();
        for (Task task : taskList) {
            task.setAssignee(task.getOwner());
            task.setOwner(null);
            task.setDelegationState(null);
            taskService.saveTask(task);
        }
    }
    /**
     * 删除：暂存和打回的状态下可以删除
     */
    @CacheEvict(value = DELEGATION_CACHE_NAME, allEntries = true)
    public void deleteBatch(List<String> idList, ICurrentUser currentUser) {
        List<FlowDelegation> delegationList = this.listByIds(idList);
        //1、身份验证：不是管理员，或者不是 授权人，不能进行删除
        //如果不是该网点的管理员
        //1、操作者不是本人或者不是该网点的人的话，不允许删除
        //2、状态验证：不是暂存或者不是打回的不能删除
        for (FlowDelegation delegation : delegationList) {
            AssertHelper.isTrue(currentUser.isAdmin() || delegation.getPrincipal().equals(currentUser.getId()), WorkFlowMessageEnum.BPM_DELEGATION_ROLE_NOT_DELETABLE);
            AssertHelper.isTrue(isStorageOrReject(delegation.getStatus()),WorkFlowMessageEnum.BPM_DELEGATION_STATUS_NOT_DELETABLE);
        }
        this.removeBatchByIds(idList);
        //2、删除关联的流程分类
        flowDelegationRelService.deleteByDelegationIds(idList);
    }
    private boolean isStorageOrReject(Integer status) {
        return Objects.equals(status, FlowDelegationVo.Status.STORAGE.getCode())
                || Objects.equals(status, FlowDelegationVo.Status.REJEC.getCode());
    }
    /**
     * 批量复制
     */
    public void copyBatch(List<String> idList, ICurrentUser currentUser) throws ParseException {
        List<FlowDelegation> delegationList = this.listByIds(idList);
        for (FlowDelegation delegation : delegationList) {
            FlowDelegationVo delegationVo = new FlowDelegationVo();
            BeanUtils.copyProperties(delegation, delegationVo);
            delegationVo.setId(null);
            delegationVo.setDelegationNo(null);
            delegationVo.setEndTime(null);
            delegationVo.setStatus(FlowDelegationVo.Status.STORAGE.getCode());
            List<FlowDelegationRel> delegationCategoryList = flowDelegationRelService.listByDelegationId(delegation.getId());
            List<String> cateIds = delegationCategoryList.stream().map(FlowDelegationRel::getCateId).collect(Collectors.toList());
            delegationVo.setFlowCategoryIdList(cateIds);
            this.save(delegationVo, currentUser);
        }
    }
    /**
     * 暂存和提交时调用
     *
     * @param delegationVo
     */
    private void save(FlowDelegationVo delegationVo, ICurrentUser currentUser) {
        String principal = delegationVo.getPrincipal();
        String proxy = delegationVo.getProxy();
        Map<String, UserDTO> userDTOMap = userService.listUserByGpId(Lists.newArrayList(principal, proxy));
        FlowDelegation delegation;
        //没有id意味着没有序列号
        if (StringUtils.isBlank(delegationVo.getId())) {
            delegation = new FlowDelegation();
            delegation.setDelegationNo(SequenceSingleton.getSequence(SequenceEnum.FLOW_DELEGATION_NO));
        } else {
            delegation = this.getById(delegationVo.getId());
        }
        delegation.setStatus(delegationVo.getStatus());
        delegation.setStartTime(DateUtils.parseDate(delegationVo.getStartTimeStr()));
        //delegation.setStartTime(delegationVo.getStartTime());
        delegation.setPrincipal(principal);
        delegation.setPrincipalName(userDTOMap.get(principal).getUsername());
        delegation.setPrincipalCorpId(userDTOMap.get(principal).getCorpId());
        delegation.setProxy(proxy);
        delegation.setProxyName(userDTOMap.get(proxy).getUsername());
        delegation.setProxyCorpId(userDTOMap.get(proxy).getCorpId());
        this.saveOrUpdate(delegation);
        flowDelegationRelService.cleanAndSaveByIds(delegation.getId(), delegationVo.getFlowCategoryIdList());
    }
    /**
     * 修改状态,打回，确认，结束的时候调用
     */
    private void updateStatus(FlowDelegation delegation, ICurrentUser currentUser) {
        Date date = DateUtils.getDate();
        delegation.setUpdateBy(currentUser.getId());
        if (FlowDelegationVo.Status.FINISH.getCode().equals(delegation.getStatus()) && delegation.getEndTime() == null) {
            delegation.setEndTime(date);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", delegation.getStatus());
        params.put("updateBy", delegation.getUpdateBy());
        params.put("id", delegation.getId());
        this.saveOrUpdate(delegation);
    }
    @Deprecated
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        String gedStartTime = (String) dataTablesInput.getFormdata().get("ged_startTime");
        String ledStartTime = (String) dataTablesInput.getFormdata().get("led_startTime");
        if (StringUtils.isNotEmpty(gedStartTime)) {
            gedStartTime = gedStartTime + " 00:00:00";
        }
        if (StringUtils.isNotEmpty(ledStartTime)) {
            ledStartTime = ledStartTime + " 23:59:59";
        }
        Page<FlowDelegation> page = Page.of(dataTablesInput.getStart(), dataTablesInput.getPageSize());
        IPage<FlowDelegationVo> page1 = new LambdaQueryChainWrapper<>(FlowDelegation.class)
                .ge(FlowDelegation::getStartTime, gedStartTime)
                .le(FlowDelegation::getStartTime, ledStartTime)
                .page(page).convert(FlowDelegationVo::from);
        return new DataTablesOutput<>(dataTablesInput.getDraw(), page1.getRecords(), page1.getTotal(), page1.getTotal());
    }
    /**
     * 检查转授的业务分类
     * 判断提交的转授业务，是否该转授人已提出，并尚未结束（即状态为待确认和已确认）
     * <p>
     * 开始时间比较
     *
     * @param delegationVo
     */
    public List<String> checkCategory(FlowDelegationVo delegationVo) {
       //todo
        return null;
    }
    /**
     * 查看选择转授的业务
     *
     * @param delegationId
     * @return
     */
    public List<String> findCategoryListByDelegationId(String delegationId) {
        return flowDelegationRelService.listCateIdsByDelegationId(delegationId);
    }
    @Cacheable(value = DELEGATION_CACHE_NAME, key = "#principal + #categoryId")
    public FlowDelegation getOneByPrincipalAndCategory(String principal, String categoryId) {
        return this.baseMapper.getOneByPrincipalAndCategory(principal, categoryId);
    }
    /**
     * 查询转授与确认列表
     * @param delegationQuery
     * @return
     */
    public DataTablesOutput getDataTablesUnion(FlowDelegationQuery delegationQuery) {
        Page<FlowDelegation> page = Page.of(delegationQuery.getStart(), delegationQuery.getPageSize());
        IPage<FlowDelegationVo> page1 = new LambdaQueryChainWrapper<>(FlowDelegation.class)
                .ge(Objects.nonNull(delegationQuery.getStartTime()), FlowDelegation::getStartTime, delegationQuery.getStartTime())
                .le(Objects.nonNull(delegationQuery.getEndTime()), FlowDelegation::getStartTime, delegationQuery.getEndTime())
                .eq(StringUtils.isNoneBlank(delegationQuery.getPrincipal()), FlowDelegation::getPrincipal, delegationQuery.getPrincipal())
                .eq(StringUtils.isNoneBlank(delegationQuery.getProxy()), FlowDelegation::getProxy, delegationQuery.getProxy())
                .eq(Objects.nonNull(delegationQuery.getStatus()), FlowDelegation::getStatus, Objects.nonNull(delegationQuery.getStatus()) ? delegationQuery.getStatus().getCode() : null)
                .page(page).convert(FlowDelegationVo::from);
        return new DataTablesOutput<>(delegationQuery.getDraw(), page1.getRecords(), page1.getTotal(), page1.getTotal());
    }


    /**
     * 定时任务检查是否有task需要转授或者转回
     *
     */
    @XxlJob("DelegationService")
    public void delegationJob() {
        log.info("===开始定时任务：将流程转授给被转授人===");
        // 查找需要流转转授的情况
        QueryWrapper<FlowDelegation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", DelegationStatusEnum.SURE.getStatus());
        queryWrapper.le("start_time", new Date());
        List<FlowDelegation> delegationListSure = flowDelegationMapper.selectList(queryWrapper);
        for (FlowDelegation delegation : delegationListSure) {
            delegateUnfinishedTask(delegation);
        }
        log.info("===定时任务结束：将流程转授给被转授人===");
    }

}
