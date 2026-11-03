package com.bytter.dual.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.dual.constants.DualManagerConstant;
import com.bytter.dual.dao.DualManagerDao;
import com.bytter.dual.entity.DualManager;
import com.bytter.dual.factory.DualManagerFactory;
import com.bytter.dual.interfaces.DualManagerCommand;
import com.bytter.dual.vo.DualManagerVO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.service.UserService;
import com.bytter.workflow.bpm.IWorkFlowCompletedListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * todo 现兼容T6写法 需要修改
 * 双管理员服务类
 * Created by zhoul on 2017/8/31.
 */
@WorkFlow(DualManagerConstant.PROCESS_INSTANCE_KEY)
@Service("dualManagerService")
@Transactional
public class DualManagerService extends BaseService<DualManager, String> implements IWorkFlowCompletedListener {
    @Resource
    private DualManagerDao dualManagerDao;
    @Resource
    private UserService userService;
    @Resource
    private IConfigService configServiceImpl;
    public boolean isOpenDualManager() {
        Integer flag = configServiceImpl.getConfig(ConfigConstant.SYS, "dualManager").toInteger();
        return flag.equals(1);
    }
    /**
     * 启动多笔审批，数据量较少的
     *
     * @param command
     * @param list
     */
    public <T> void start(Class<? extends DualManagerCommand<T>> command, List<T> list, String startUserId) {
        for (T str : list) {
            start(command, (String)str, str,startUserId);
        }
    }
    public <T> void start(Class<? extends DualManagerCommand<T>> command, String businessKey, T variables,String startUserId) {
        if (!isOpenDualManager()) {
            findDualManagerCommand(command, variables);
        }
        //todo 工作流不在本服务启动
    }
    private <T> void findDualManagerCommand(Class<? extends DualManagerCommand<T>> command, T variables) {
        DualManagerCommand<T> dualManagerCommand = DualManagerFactory.getCommand(command);
        dualManagerCommand.execute(variables);
    }
    private <T> void executeCommand(DualManager dualManager){
        DualManagerCommand<T> dualManagerCommand = DualManagerFactory.getCommand(dualManager.getManagerType());
        Assert.notNull(dualManagerCommand,"DualManagerCommand should not be null");
        dualManagerCommand.execute(dualManagerCommand.serializableReversal(dualManager.getContent()));
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        DualManager dualManager = get(workFlowResult.getBusinessKey());
        dualManager.setStatus(1);
        if(workFlowResult.isPass()){
            executeCommand(dualManager);
        }
    }
    public <T> T getSerializableData(Class<? extends DualManagerCommand> command, String businessKey) {
        return getSerializableData(command.getName(), businessKey);
    }
    public <T> T getSerializableData(String command, String businessKey) {
        DualManagerCommand<T> dualManagerCommand = DualManagerFactory.getCommand(command);
        Assert.notNull(dualManagerCommand,"DualManagerCommand should not be null");
        DualManager dualManager = get(businessKey);
        return dualManagerCommand.serializableReversal(dualManager.getContent());
    }
    public List<DualManagerVO> listByIds(Integer first, Integer maxResult, List<String> ids) {
        List<DualManager> dualManagers = dualManagerDao.listByIds(first, maxResult, ids);
        List<DualManagerVO> dualManagerVOList = BeanUtils.copyListProperties(dualManagers, DualManagerVO.class);
        List<DualManagerVO> temp = new ArrayList<>();
        for (DualManagerVO dualManagerVO : dualManagerVOList) {
            dualManagerVO.setApprovePage(getApproveURLByCommand(dualManagerVO.getManagerType()));
            temp.add(dualManagerVO);
        }
        return temp;
    }
    /**
     * 获取审批的地址
     *
     * @param command
     * @return
     */
    public String getApproveURLByCommand(String command) {
        DualManagerCommand dualManagerCommand = DualManagerFactory.getCommand(command);
        String approvePage = null;
        if (dualManagerCommand != null) {
            approvePage = dualManagerCommand.getApprovePageUrl();
        }
        return approvePage;
    }
    @Override
    public BaseDao<DualManager, String> getEntityDao() {
        return dualManagerDao;
    }
}
