package com.bytter.basic.service.impl;
import com.bytter.basic.service.BtPurposeService;
import com.bytter.basic.vo.BtPurposeVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtPurposeService;
import com.bytter.system.api.dto.BtPurposeDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 用途维护Service接口类
 *
 * @author zuoy
 * @date 2017/5/24.
 */
@Service("btPurposeServiceI")
@DubboService
@Transactional
public class BtPurposeServiceImpl implements IBtPurposeService {
    @Autowired
    private BtPurposeService btPurposeService;
    @Override
    public BtPurposeDTO getObjectById(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtPurposeVO btPurpose = btPurposeService.getByUnion(id);
        if (EmptyUtils.isEmpty(btPurpose)) {
            return null;
        }
        BtPurposeDTO btPurposeDTO = new BtPurposeDTO();
        BeanUtils.copyProperties(btPurpose, btPurposeDTO);
        return btPurposeDTO;
    }
    @Override
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        List<BtPurposeVO> btPurposeList = btPurposeService.listBy(corpId, sysBusType);
        if (EmptyUtils.isEmptyList(btPurposeList)) {
            return null;
        }
        return BeanUtils.copyListProperties(btPurposeList, BtPurposeDTO.class);
    }
    @Override
    @Deprecated
    public void saveBtPurpose(String corpId, String purpose, String sysBusType) {
        this.btPurposeService.saveBtPurpose(corpId, purpose, sysBusType, "");
    }
    @Override
    public void saveBtPurpose(String corpId, String purpose, String sysBusType, String userLoginName) {
        this.btPurposeService.saveBtPurpose(corpId, purpose, sysBusType, userLoginName);
    }
}
