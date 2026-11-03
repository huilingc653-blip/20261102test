package com.bytter.system.api;
import com.bytter.system.api.dto.SysCaInfoDTO;
/**
 * Created by zhoul on 2017/10/23.
 */
public interface ICAService {
    /**
     * 后台验签方法
     *
     * @param userCadn
     * @param userCode
     * @param sourceData
     * @param signData
     * @return
     */
    boolean verify(String userCadn, String userCode, String sourceData, String signData);
    /**
     * 保存加密信息
     *
     * @param dto
     */
    String saveOrUpdateCaInfo(SysCaInfoDTO dto);
}
