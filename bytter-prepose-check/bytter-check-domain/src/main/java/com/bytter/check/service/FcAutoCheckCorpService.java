package com.bytter.check.service;
import com.bytter.check.constant.FinacialResourceConstant;
import com.bytter.check.dao.FcAutoCheckCorpDao;
import com.bytter.check.entity.FcAutoCheckCorp;
import com.bytter.check.vo.FcAutoCheckCorpVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("fcAutoCheckCorpService")
@Transactional
public class FcAutoCheckCorpService extends BaseService<FcAutoCheckCorp, String> {
    @Autowired
    private FcAutoCheckCorpDao fcAutoCheckCorpDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Override
    public BaseDao<FcAutoCheckCorp, String> getEntityDao() {
        return fcAutoCheckCorpDao;
    }
    /**
     * 功能介绍 :根据对账类型获取对账策略和单位之间的信息
     */
    public List<FcAutoCheckCorpVO> getAllCheckCorp(String checkType, ICurrentUser user) {
        String resource = FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_QUERY;
        List<String> corpIds = corpService.getUserCorpIds(resource, user.getId());
        List<CorpDTO> corpDTOS = corpService.listTreeCorpDTOByCorpIds(corpIds);
        List<FcAutoCheckCorpVO> fcAutoCheckCorpVOS = new ArrayList<>();
        for (CorpDTO corp : corpDTOS) {
            FcAutoCheckCorpVO fcAutoCheckCorpVO = new FcAutoCheckCorpVO();
            fcAutoCheckCorpVO.setCorpId(corp.getId());
            fcAutoCheckCorpVO.setParentId(corp.getParentId());
            fcAutoCheckCorpVO.setCorpName(corp.getName());
            FcAutoCheckCorp checkCorp = fcAutoCheckCorpDao.getCheckCorp(checkType, corp.getId());
            if (!EmptyUtils.isEmpty(checkCorp)) {
                fcAutoCheckCorpVO.setAutoCheckCode(checkCorp.getAutoCheckCode());
                fcAutoCheckCorpVO.setCheckType(checkCorp.getCheckType());
                fcAutoCheckCorpVO.setCheckTheStandard(checkCorp.getCheckTheStandard());
            }
            fcAutoCheckCorpVOS.add(fcAutoCheckCorpVO);
        }
        return fcAutoCheckCorpVOS;
    }
    public void saveWithCorp(Map<String, String> parm, ICurrentUser user) {
        String autoCheckCode = parm.get("autoCheckCode");
        String checkType = parm.get("checkType");
        parm.remove("autoCheckCode");
        parm.remove("checkType");
        List<FcAutoCheckCorp> fcAutoCheckCorps = new ArrayList<>();
        for (String key : parm.keySet()) {
            String value = parm.get(key);
            if (!EmptyUtils.isEmpty(value)) {//为新增的关联单位，或者修改了核对标准
                FcAutoCheckCorp checkCorp = fcAutoCheckCorpDao.getCheckCorp(checkType, key);
                if (EmptyUtils.isEmpty(checkCorp)) {//新增关联单位
                    FcAutoCheckCorp fcAutoCheckCorp = new FcAutoCheckCorp();
                    fcAutoCheckCorp.setAutoCheckCode(autoCheckCode);
                    fcAutoCheckCorp.setCheckType(checkType);
                    fcAutoCheckCorp.setCorpId(key);
                    fcAutoCheckCorp.setCheckTheStandard(value);
                    fcAutoCheckCorp.setCreateBy(user.getUsername());
                    fcAutoCheckCorp.setCreateDate(new Date());
                    fcAutoCheckCorps.add(fcAutoCheckCorp);
                } else {//对已关联的修改了核对标准
                    checkCorp.setCheckTheStandard(value);
                }
            } else {//取消了之前已有的关联
                FcAutoCheckCorp checkCorp = fcAutoCheckCorpDao.getCheckCorp(checkType, key);
                this.delete(checkCorp.getId());
            }
        }
        this.batchSave(fcAutoCheckCorps);
    }
    /**
     * 功能介绍 :根据对账类型获和单位取对账策略与单位的连接
     */
    public FcAutoCheckCorp getCheckCorp(String checkType, String corpId) {
        return fcAutoCheckCorpDao.getCheckCorp(checkType, corpId);
    }
    /**
     * 功能介绍 :根据对账策略代码删除相关的关联单位数据
     */
    public void deleteByCode(String autoCheckCode) {
        fcAutoCheckCorpDao.deleteByCode(autoCheckCode);
    }
    /**
     * 功能介绍 :根据对账策略代码修改成新的策略代码
     */
    public void updateByCode(String autoCheckCode, String oldCode) {
        fcAutoCheckCorpDao.updateByCode(autoCheckCode, oldCode);
    }
    /**
     * 功能介绍 :得到对应策略对应的关联单位信息
     */
    public List<FcAutoCheckCorp> getByCode(String autoCheckCode) {
        return fcAutoCheckCorpDao.getByCode(autoCheckCode);
    }
}
