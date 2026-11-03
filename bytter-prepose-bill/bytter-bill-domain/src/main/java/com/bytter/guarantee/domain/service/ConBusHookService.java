package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.domain.dao.ConBusHookDao;
import com.bytter.guarantee.domain.entity.ConBusHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: 合同业务挂接Service
 * @Description: 合同业务挂接处理类
 * @date 2019-11-09
 */
@Service("conBusHookService")
@Transactional
public class ConBusHookService extends BaseService<ConBusHook, String> {
    @Autowired
    private ConBusHookDao conBusHookDao;
    public BaseDao<ConBusHook, String> getEntityDao() {
        return conBusHookDao;
    }
    /**
     * 通过参数获取字段明细业务信息
     * 
     * @param conBusdtlVO
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConBusHook> getConBusHook(ConBusHook conBusHook) {
        Map<String, Object> params = new HashMap<>();
        params.put("validSign", ConConstant.CON_VALID_SIGN_1);
        if (StringUtils.isNotEmpty(conBusHook.getConId())) {
            params.put("conId", conBusHook.getConId());
        }
        if (StringUtils.isNotEmpty(conBusHook.getBusId())) {
            params.put("busId", conBusHook.getBusId());
        }
        return conBusHookDao.getConBusHookByParams(params);
    }
    /**
     * 根据条件设置合同挂接失效
     * 
     * @param conBusHook
     */
    public void updateConBusHookIsOver(ConBusHook conBusHook, ICurrentUser user) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(conBusHook.getConId())) {
            params.put("conId", conBusHook.getConId());
        }
        if (StringUtils.isNotEmpty(conBusHook.getBusId())) {
            params.put("busId", conBusHook.getBusId());
        }
        if (user != null && StringUtils.isNotEmpty(user.getId())) {
            params.put("updateId", user.getId());
            params.put("updateTime", new Date());
        }
        conBusHookDao.updateConBusHookIsOver(params);
    }
}
