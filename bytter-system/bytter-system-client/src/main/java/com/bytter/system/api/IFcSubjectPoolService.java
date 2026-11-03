package com.bytter.system.api;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import java.util.List;
/**
 * 资金池会计科目对外接口
 * @author caozx
 * @since 2019-5-6
 */
public interface IFcSubjectPoolService {
    /**
     * 根据开户种类查询资金池会计科目
     * @param accType 账户类型
     * @param valid 是否有效
     * @return 资金池会计科目集合
     */
    List<FcSubjectPoolDTO> findSubjectPoolByAccType(String accType, String valid);
    /**
     * 根据计息标志查询金池会计科目
     * @param interestSign 计息标志
     * @return 资金池会计科目集合
     */
    List<FcSubjectPoolDTO> findSubjectPoolByInterestSign(String interestSign);
    /**
     * 查询有效的会计科目
     * @return 资金池会计科目集合
     */
    List<FcSubjectPoolDTO> findSubjectPool();
    /**
     * 根据 ids 返回
     * @param ids
     * @return
     */
    List<FcSubjectPoolDTO> getFcSubjectPoolByIds(List<String> ids);
}
