package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BisBifRuleDTO;
import java.util.List;
/**
 * Created by huangx on 2022/5/11 0022.
 */
public interface IBisBifRuleService {
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getById(String id);
    /**
     * 根据接口代码ID获取接口代码规则信息
     *
     * @param bifId
     * @return
     */
    BisBifRuleDTO findByBifId(String bifId);
    /**
     * 获取接口代码规则信息
     *
     * @return
     */
    List<BisBifRuleDTO> listAllUnion();
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id);
    /**
     * 保存
     *
     * @param dto
     */
    public void save(BisBifRuleDTO dto);
    /**
     * 更新
     *
     * @param dto
     */
    public void update(BisBifRuleDTO dto);
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BisBifRuleDTO dto);
    /**
     * 根据编码获取接口代码业务规则
     *
     * @param bifCode
     * @return
     */
    public BisBifRuleDTO findByBifCode(String bifCode);
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput);
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getByIdUnion(String id);
}
