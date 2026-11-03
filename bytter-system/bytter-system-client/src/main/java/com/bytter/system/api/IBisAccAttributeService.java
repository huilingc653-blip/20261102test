package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.dto.BisAccAttributeDTO;
import java.util.List;
import java.util.Map;
/**
 * 账户性质接口类
 *
 * @author huangx
 * @since 2019-03-18 09:49:44
 */
public interface IBisAccAttributeService {
    /**
     * 根据编码获取账户属性
     *
     * @param attributeCode 账户性质编码
     * @return 账户性质
     */
    BisAccAttributeDTO findByCode(String attributeCode);
    /**
     * 获取可对外付款的账户属性
     *
     * @return 账户性质集合
     */
    List<BisAccAttributeDTO> getAttriButeForEign();
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput);
    /**
     * 判断账户属性是否在使用中
     *
     * @return
     */
    public boolean queryUse(String attributeId) ;
    /**
     * 获取VO对象
     * @param id
     * @return
     */
    public BisAccAttributeDTO getVO(String id);
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) ;
    /**
     * 保存
     *
     * @param dto
     */
    public void save(BisAccAttributeDTO dto, String locale) ;
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BisAccAttributeDTO dto,String locale);
    /**
     * 删除对象
     *
     * @param dto
     */
    public void deleteBisAccAttribute(BisAccAttributeDTO dto);
    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids);
    /**
     * 获取有效的账户属性
     *
     * @return
     */
    public List<BisAccAttributeDTO> getValidAttribute();
    public Map<String,BisAccAttributeDTO> getValidAttributeMap();
}
