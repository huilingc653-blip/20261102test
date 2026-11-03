package com.bytter.system.api;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import java.util.List;
/**
 * @author zhengg
 * @version 1.0
 * @Title: CorpDeleteVerifyService
 * @Description: 单位删除业务校验接口
 * @date 2020-05-11 17:53:54
 */
public interface ICorpDeleteVerifyService {
    /**
     * 业务通过单位id校验是否允许删除，不通过返回CorpVerifyErrorDTO信息
     *
     * @param corpId 单位id
     * @return 返回错误明细
     */
    List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId);
}
