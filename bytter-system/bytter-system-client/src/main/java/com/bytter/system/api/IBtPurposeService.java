package com.bytter.system.api;
import com.bytter.system.api.dto.BtPurposeDTO;
import java.util.List;
/**
 * Created by zy on 2017/5/24.
 */
public interface IBtPurposeService {
    /**
     * 根据id查找
     *
     * @param id
     * @return
     */
    BtPurposeDTO getObjectById(String id);
    /**
     * 根据单位ID，用途类型过滤
     *
     * @param corpId
     * @param sysBusType 可为空
     * @return
     */
    List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType);
    /**
     * 保存用途类型
     *
     * @param corpId     单位ID
     * @param purpose    用途
     * @param sysBusType 用途类别
     * @Deprecated by zhoul
     * @see #saveBtPurpose(String, String, String, String)
     */
    @Deprecated
    void saveBtPurpose(String corpId, String purpose, String sysBusType);
    void saveBtPurpose(String corpId, String purpose, String sysBusType, String userLoginName);
}
