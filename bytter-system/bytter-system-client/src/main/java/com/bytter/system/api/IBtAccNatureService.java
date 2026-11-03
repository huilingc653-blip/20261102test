package com.bytter.system.api;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.dto.BtAccNatureDTO;
import java.util.List;
import java.util.Map;
/**
 * 账户属性接口类
 *
 * @author huangx
 * @since 2019-03-18 09:49:44
 */
public interface IBtAccNatureService {
    /**
     * 根据编码获取账户性质
     *
     * @param natureCode 账户性质编码
     * @return 账户性质
     */
    BtAccNatureDTO findByCode(String natureCode);
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput);
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id);
    public BtAccNatureDTO getDTO(String id);
    /**
     * 保存
     *
     * @param entity
     */
    public void save(BtAccNatureDTO entity, String locale);
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BtAccNatureDTO dto, String locale);
    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteBtAccNature(String id);
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
    public List<BtAccNatureDTO> getValidAccNature();
    public Map<String,BtAccNatureDTO> getValidAccNatureMap();
    /**
     * 判断币别编码是否重复
     *
     * @param id
     * @param natureCode
     * @return
     */
    public boolean isRepeatCurCode(String id, String natureCode);
}
