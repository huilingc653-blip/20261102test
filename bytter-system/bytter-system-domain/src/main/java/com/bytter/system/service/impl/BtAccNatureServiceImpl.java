package com.bytter.system.service.impl;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.IBtAccNatureService;
import com.bytter.system.api.dto.BtAccNatureDTO;
import com.bytter.system.entity.BtAccNature;
import com.bytter.system.service.BtAccNatureService;
import com.bytter.system.vo.BtAccNatureVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 账户性质维护
 * @date 2017.9.14
 */
@Service("btAccNatureServiceI")
@DubboService
@Transactional
public class BtAccNatureServiceImpl implements IBtAccNatureService {
    @Autowired
    BtAccNatureService btAccNatureService;
    /**
     * 根据编码获取账户性质
     *
     * @param natureCode
     * @return
     */
    @Override
    public BtAccNatureDTO findByCode(String natureCode) {
        BtAccNature btAccNature = btAccNatureService.findByCode(natureCode);
        BtAccNatureDTO btAccNatureDTO = new BtAccNatureDTO();
        if (btAccNature != null) {
            BeanUtils.copyNotNullProperties(btAccNature, btAccNatureDTO);
        }
        return btAccNatureDTO;
    }
    @Override
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput) {
        return btAccNatureService.dataTable(dataTablesInput);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        btAccNatureService.delete(id);
    }
    @Override
    public BtAccNatureDTO getDTO(String id) {
        BtAccNatureVO vo = btAccNatureService.getVO(id);
        BtAccNatureDTO btAccNatureDTO = new BtAccNatureDTO();
        if (vo != null) {
            BeanUtils.copyNotNullProperties(vo, btAccNatureDTO);
        }
        return btAccNatureDTO;
    }
    /**
     * 保存
     *
     * @param dto
     */
    @Override
    public void save(BtAccNatureDTO dto, String locale) {
        BtAccNature btAccNature = new BtAccNature();
        if (dto != null) {
            BeanUtils.copyNotNullProperties(dto, btAccNature);
        }
        btAccNatureService.save(btAccNature, locale);
    }
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BtAccNatureDTO dto, String locale) {
        BtAccNature btAccNature = new BtAccNature();
        if (dto != null) {
            BeanUtils.copyNotNullProperties(dto, btAccNature);
        }
        btAccNatureService.saveOrUpdate(btAccNature, locale);
    }
    /**
     * 删除对象
     *
     * @param id
     */
    public void deleteBtAccNature(String id) {
        BtAccNature entity = btAccNatureService.get(id);
        if (null != entity) {
            btAccNatureService.deleteBtAccNature(entity);
        }
    }
    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        btAccNatureService.batchDelete(ids);
    }
    /**
     * 获取有效的账户属性
     *
     * @return
     */
    public List<BtAccNatureDTO> getValidAccNature() {
        List<BtAccNatureVO> list = btAccNatureService.getValidAccNature();
        List<BtAccNatureDTO> btAccNatureDTOList = BeanUtils.copyListProperties(list, BtAccNatureDTO.class);
        return btAccNatureDTOList;
    }
    /**
     * 获取有效的账户属性
     *
     * @return
     */
    public Map<String,BtAccNatureDTO> getValidAccNatureMap() {
        List<BtAccNatureVO> list = btAccNatureService.getValidAccNature();
        List<BtAccNatureDTO> btAccNatureDTOList = BeanUtils.copyListProperties(list, BtAccNatureDTO.class);
        Map<String ,BtAccNatureDTO> map=new HashMap<>();
        for(BtAccNatureDTO dto:btAccNatureDTOList){
            map.put(dto.getId(),dto);
        }
        return map;
    }
    /**
     * 判断币别编码是否重复
     *
     * @param id
     * @param natureCode
     * @return
     */
    public boolean isRepeatCurCode(String id, String natureCode) {
        return btAccNatureService.isRepeatCurCode(id, natureCode);
    }
}
