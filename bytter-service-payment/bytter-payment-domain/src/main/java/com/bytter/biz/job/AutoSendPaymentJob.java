package com.bytter.biz.job;

import com.bytter.biz.bdlinks.service.BisExcService;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自动发送付款执行
 */
@Slf4j
@Component
public class AutoSendPaymentJob implements ITaskService {
    @Autowired
    private NisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IUserService userService;

    /**
     * 自动发送付款执行
     * @param arguments
     */
    @XxlJob("AutoSendPaymentJob")
    @Override
    public void run(String arguments) {
        List<NisBillheadVO> list =  nisBillheadService.listByAutoSendPaymentJob();
        if(EmptyUtils.isNotEmpty(list) && list.size()>0){
            for(NisBillheadVO vo : list){
                String loginName = vo.getCreateBy();
                UserDTO userDTO = userService.getByLoginName(loginName);
                ICurrentUser user = new ShiroUser(userDTO.getId(),userDTO.getLoginName(),userDTO.getUsername());
                NisBillhead entity = new NisBillhead();
                BeanUtils.copyNotNullProperties(vo,entity);
                entity.setAccId(vo.getRealPayerAccId());
                XxlJobHelper.log("结算中心实际付款账号"+vo.getRealPayerAccId());
                XxlJobHelper.log(entity.getAccId());
                XxlJobHelper.log(entity.getCurId());
                if(EmptyUtils.isNotEmpty(vo.getRealPayerAccId())) {
                    nisBillheadService.sendNisBillhead(entity, user);
                }
            }
        }
    }
}
