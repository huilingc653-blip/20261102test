package com.bytter.guarantee.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.guarantee.domain.entity.ConInfoRecord;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 合同单位处理服务类
 * @Description: 合同单位处理服务类
 * @date 2019-11-05
 */
@Service("conCorpService")
@Transactional
public class ConCorpService extends BaseService<String, ConInfoRecord> {
    @DubboReference(check = false)
    private ICorpService corpService;
    @Override
    public BaseDao<String, ConInfoRecord> getEntityDao() {
        return null;
    }
    /**
     * 通过ID查找网点、单位
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public CorpDTO get(String id) {
        return corpService.get(id);
    }
    /**
     * 获取当前用户对应资源的单位或网点id
     *
     * @param resource
     *            资源
     * @param userId
     *            用户ID
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> getUserCorpIds(String resource, String userId) {
        return corpService.getUserCorpIds(resource, userId);
    }
    /**
     * 根据网点ID，获取该网点下直属的单位(不跨网点获取)
     *
     * @param netId
     * @return
     */
    @Transactional(readOnly = true)
    public List<CorpDTO> listUnderCorpByNetId(String netId) {
        return corpService.listUnderCorpByNetId(netId);
    }
    /**
     * 根据单位名称查找单位
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    public CorpDTO getCorpIdByName(String name) {
        return corpService.getCorpIdByName(name);
    }
    /**
     * 根据单位代码查找单位信息
     *
     * @param code
     * @return
     */
    @Transactional(readOnly = true)
    public CorpDTO getCorpByCode(String code) {
        return corpService.getCorpByCode(code);
    }
    /**
     * 根据单位ID，获取该单位的直属网点
     *
     * @param corpId
     * @return
     */
    @Transactional(readOnly = true)
    public CorpDTO getNetByCorpId(String corpId) {
        return corpService.getNetByCorpId(corpId);
    }
}
