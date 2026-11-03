package com.bytter.biz.bdlink.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.dao.BisBifMonitorDao;
import com.bytter.biz.bdlink.entity.BisBifMonitor;
import com.bytter.biz.bdlink.vo.BisBifMonitorVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.RoleDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 银行接口监控预警表
 * @date 2022-07-07 17:07:12
 */
@Service("bisBifMonitorServiceBase")
@Transactional
public class BisBifMonitorService extends BaseService<BisBifMonitor, String> implements ITaskService {
    @Autowired
    private BisBifMonitorDao bisBifMonitorDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccServiceI;
    @DubboReference(check = false)
    private IBisBifInitService bisBifInitService;
    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<BisBifMonitor, String> getEntityDao() {
        return bisBifMonitorDao;
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        DataTablesOutput data = bisBifMonitorDao.getDataTablesUnion(dataTablesInput);
        List<BisBifMonitorVO> list = data.getData();
        // 抽取接口ID集合
        List<String> bifIds = list.stream().map(BisBifMonitorVO::getBisBifId).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(bifIds)) {
            return new DataTablesOutput<>();
        }
        // 抽取角色ID集合
        List<String> roleIdList = list.stream().map(BisBifMonitorVO::getRoleIds).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        // 分割后去重
        List<String> roleList = roleIdList.stream().flatMap(str -> Arrays.stream(str.split(CommonConstant.COMMA_SPLIT))).distinct().collect(Collectors.toList());
        // 批量拉取数据
        Map<String, BisBifInitDTO> bifInitMap = bisBifInitService.listBifInitByIds(bifIds);
        Map<String, RoleDTO> roleMap = userService.listRoleByIds(roleList);
        Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("bisBifMonitor.taskType","fcCorpPool.status");
        // 填充数据
        for (BisBifMonitorVO vo : list) {
            if (ObjectUtil.isAllNotEmpty(bifInitMap, vo.getBisBifId())) {
                BisBifInitDTO initDTO = bifInitMap.get(vo.getBisBifId());
                Optional.ofNullable(initDTO).ifPresent(dto -> {
                    vo.setBifName(initDTO.getName());
                    vo.setBifCode(initDTO.getBifCode());
                });
            }
            if (ObjectUtil.isAllNotEmpty(roleMap, vo.getRoleIds())) {
                String roleName = getRoleName(roleMap, vo);
                vo.setRoleNames(roleName);
            }
            if (ObjectUtil.isNotEmpty(listDictMap)) {
                Optional.ofNullable(vo.getTaskType()).ifPresent(taskType -> {
                    Map<String, DictDTO> dictDtoMap = listDictMap.get("bisBifMonitor.taskType");
                    Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(taskType)))
                            .ifPresent(dict -> vo.setTaskTypeName(dict.getName()));
                });
                Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
                    Map<String, DictDTO> dictDtoMap = listDictMap.get("fcCorpPool.status");
                    Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                            .ifPresent(dict -> vo.setStatusName(dict.getName()));
                });
            }
        }
        return data;
    }

    /**
     * 辅助方法：组装角色名。
     * @param roleMap 角色对象Map。
     * @param vo 角色ID对象。
     * @return 角色名。
     */
    private static String getRoleName(Map<String, RoleDTO> roleMap, BisBifMonitorVO vo) {
        String[] roleIds = vo.getRoleIds().split(CommonConstant.COMMA_SPLIT);
        String roleName = "";
        for (String roleId : roleIds) {
            RoleDTO role = null;
            if (roleMap.containsKey(roleId)) {
                role = roleMap.get(roleId);
            }
            if (role != null) {
                if (StringUtils.isBlank(roleName)) {
                    roleName = role.getName();
                } else {
                    roleName = roleName + CommonConstant.COMMA_SPLIT + role.getName();
                }
            }
        }
        return roleName;
    }

    /**
     * 根据ID信息
     *
     * @param id
     * @return
     */
    public BisBifMonitorVO getByIdUnion(String id) {
        BisBifMonitorVO monitorVO = bisBifMonitorDao.getByIdUnion(id);
        if (StrUtil.isNotBlank(monitorVO.getBisBifId())){
            BisBifInitDTO bifInit = bisBifInitService.getObjectById(monitorVO.getBisBifId());
            Optional.ofNullable(bifInit).ifPresent(bif-> monitorVO.setBifName(bifInit.getName()));
        }
        return monitorVO;
    }
    /**
     * 新增
     *
     * @param vo
     * @param user
     */
    public void saveBisBifMonitor(BisBifMonitorVO vo, ICurrentUser user) {
        if (StringUtils.isNotBlank(vo.getBisBifId())) {
            String[] bifIds = vo.getBisBifId().split(CommonConstant.COMMA_SPLIT);
            List<BisBifMonitor> list = new ArrayList<>();
            for (String bifId : bifIds) {
                BisBifMonitor entity = new BisBifMonitor();
                BeanUtils.copyNotNullProperties(vo, entity);
                entity.setBisBifId(bifId);
                entity.setCreateBy(user.getLoginName());
                entity.setCreateDate(DateUtils.getDate());
                list.add(entity);
            }
            this.batchSave(list);
        }
    }
    /**
     * 修改
     *
     * @param vo
     * @param user
     */
    public void updateBisBifMonitor(BisBifMonitorVO vo, ICurrentUser user) {
        BisBifMonitor entity = new BisBifMonitor();
        BeanUtils.copyNotNullProperties(vo, entity);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(entity);
    }
    @Override
    @XxlJob("BisBifMonitorService")
    public void run(String arguments) {
        //获取有效的银行接口监控预警信息
        List<BisBifMonitorVO> bisBifMonitorList = bisBifMonitorDao.listByValid();
        //先清楚之前的消息
        notificationService.completeListByTag(NotificationConstant.MONITOR.MONITOR_BISBIFMONITOR_DISCONNECTIONALERT);
        for (BisBifMonitorVO monitor : bisBifMonitorList) {
            //判断该银行是否存在有效的在线账号
            BtBankAccDTO dto = new BtBankAccDTO();
            dto.setBifCode(monitor.getBisBifId());
            dto.setIsOnline(CommonConstant.YES);
            dto.setValidSign(CommonConstant.YES);
            List<BtBankAccDTO> bankAccList = btBankAccServiceI.getBankAccList(dto);
            if (EmptyUtils.isEmptyList(bankAccList)) {
                continue;
            }
            //获取银行最新的余额获取时间
            Date getMaxBalDate = bisBifMonitorDao.getMaxBalDate(monitor.getBisBifId());
            long diffMinutes = 0;
            if (getMaxBalDate != null) {
                Date nowDate = DateUtils.getDate();
                //获得两个时间的毫秒时间差异
                long diff = nowDate.getTime() - getMaxBalDate.getTime();
                diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                //相差总时长（按分钟计算）
                diffMinutes = diffMinutes + diffHours * 60 + diffDays * 1440;
            }
            if (getMaxBalDate == null || diffMinutes > monitor.getMonitorHz()) {
                String[] roleIds = monitor.getRoleIds().split(CommonConstant.COMMA_SPLIT);
                List<String> roleList = new ArrayList<>();
                for (String roleId : roleIds) {
                    roleList.add(roleId);
                }
                //获取角色对应的用户
                List<String> userIds = userService.listUserIdsByRoleId(roleList);
                notificationService.sendToUser(monitor.getId(), userIds,
                        NotificationConstant.MONITOR.MONITOR_BISBIFMONITOR_DISCONNECTIONALERT, monitor);
            }
        }
    }
}
