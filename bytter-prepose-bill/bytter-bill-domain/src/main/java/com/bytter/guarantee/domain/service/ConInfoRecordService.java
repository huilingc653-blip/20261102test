package com.bytter.guarantee.domain.service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.guarantee.domain.dao.ConInfoRecordDao;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.guarantee.domain.vo.ConInfoRecordVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.ConConstant;
import com.bytter.guarantee.enums.ConMessageEnum;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同业务流水表业务Service
 * @Description: 合同业务流水表业务处理类
 * @date 2019-11-04
 */
@Service("conInfoRecordService")
@Transactional
public class ConInfoRecordService extends BaseService<ConInfoRecord, String> {
    @Autowired
    private ConInfoRecordDao conInfoRecordDao;
    public BaseDao<ConInfoRecord, String> getEntityDao() {
        return conInfoRecordDao;
    }
    /**
     * 批量修改操作
     * 
     * @param rid
     *            流水id
     * @return
     */
    public int updateRecordById(String rid, Integer status, String validSign, ICurrentUser userVo) {
        if (StringUtils.isEmpty(rid)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        String[] ids = { rid };
        params.put("ids", ids);
        if (status != null) {
            params.put("status", status);
        }
        if (StringUtils.isNotEmpty(validSign)) {
            params.put("validSign", validSign);
        }
        if (userVo != null) {
            params.put("updateId", userVo.getId());
        }
        params.put("updateTime", new Date());
        return conInfoRecordDao.updateRecordByParams(params);
    }
    /**
     * 根据合同号修改 流水表登记\已签订业务流水的有效标记
     * 
     * @param rid
     *            流水id
     * @return
     */
    public int updateRecordValidSignByConId(String conId, String validSign, ICurrentUser userVo) {
        if (StringUtils.isEmpty(conId)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("conIdW", conId);
        if (ConConstant.CON_VALID_SIGN_1.equals(validSign)) {
            params.put("validSign", ConConstant.CON_VALID_SIGN_1);
            params.put("validSignW", ConConstant.CON_VALID_SIGN_2);
        } else {
            params.put("validSign", ConConstant.CON_VALID_SIGN_2);
            params.put("validSignW", ConConstant.CON_VALID_SIGN_1);
        }
        String[] busType = { ConConstant.CON_BUS_TYPE_02, ConConstant.CON_BUS_TYPE_03 };
        params.put("busTypesW", busType);
        params.put("statusW", ConConstant.CON_OTHER_STATUS_95);
        if (userVo != null) {
            params.put("updateId", userVo.getId());
        }
        params.put("updateTime", new Date());
        return conInfoRecordDao.updateRecordByParams(params);
    }
    /**
     * 通过参数获取字段明细业务信息
     * 
     * @param params
     * @return
     */
    @Transactional(readOnly = true)
    public List<ConInfoRecord> getConInfoRecord(ConInfoRecordVO conRecordVO) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(conRecordVO.getMid())) {
            params.put("mid", conRecordVO.getMid());
        }
        if (conRecordVO.getBusTypes() != null) {
            params.put("busTypes", conRecordVO.getBusTypes());
        }
        if (conRecordVO.getStatus() != null) {
            params.put("status", conRecordVO.getStatus());
        }
        if (conRecordVO.getValidSign() != null) {
            params.put("validSign", conRecordVO.getValidSign());
        }
        return conInfoRecordDao.getConInfoRecordByParams(params);
    }
}
