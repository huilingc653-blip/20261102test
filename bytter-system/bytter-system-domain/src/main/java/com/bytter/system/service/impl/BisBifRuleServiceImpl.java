package com.bytter.system.service.impl;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBisBifRuleService;
import com.bytter.system.api.dto.BisBifRuleDTO;
import com.bytter.system.entity.BisBifRule;
import com.bytter.system.service.BisBifRuleService;
import com.bytter.system.vo.BisBifRuleVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/**
 * @author huangx
 * @Description 接口代码业务规则表
 * @date 2022.5.11
 */
@Service("bisBifRuleServiceI")
@DubboService
public class BisBifRuleServiceImpl implements IBisBifRuleService {
    @Autowired
    private BisBifRuleService bisBifRuleService;
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getById(String id) {
        BisBifRuleVO bisBifRuleVO= bisBifRuleService.getById(id);
        BisBifRuleDTO dto = new BisBifRuleDTO();
        if (bisBifRuleVO != null) {
            BeanUtils.copyProperties(bisBifRuleVO, dto);
        }
        return dto;
    }
    @Override
    public BisBifRuleDTO findByBifId(String bifId) {
        BisBifRuleVO bisBifRuleVO = bisBifRuleService.findByBifId(bifId);
        BisBifRuleDTO dto = new BisBifRuleDTO();
        //调用方可能用null判断逻辑
        if (bisBifRuleVO == null) {
            return null;
        }
        BeanUtils.copyProperties(bisBifRuleVO, dto);
        return dto;
    }
    @Override
    public List<BisBifRuleDTO> listAllUnion() {
        List<BisBifRuleVO> list = bisBifRuleService.listAllUnion();
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, BisBifRuleDTO.class);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        bisBifRuleService.delete(id);
    }
    /**
     * 保存
     *
     * @param dto
     */
    public void save(BisBifRuleDTO dto) {
        BisBifRule entity = BeanUtils.copyNewInstanceProperties(dto, BisBifRule.class);
        bisBifRuleService.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param dto
     */
    public void update(BisBifRuleDTO dto) {
        BisBifRule entity = BeanUtils.copyNewInstanceProperties(dto, BisBifRule.class);
        bisBifRuleService.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BisBifRuleDTO dto) {
        BisBifRule entity = BeanUtils.copyNewInstanceProperties(dto, BisBifRule.class);
        bisBifRuleService.saveOrUpdate(entity);
    }
    /**
     * 根据编码获取接口代码业务规则
     *
     * @param bifCode
     * @return
     */
    public BisBifRuleDTO findByBifCode(String bifCode) {
        BisBifRule entity = bisBifRuleService.findByBifCode(bifCode);
        return BeanUtils.copyNewInstanceProperties(entity, BisBifRuleDTO.class);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return bisBifRuleService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getByIdUnion(String id) {
        BisBifRuleVO vo = bisBifRuleService.getByIdUnion(id);
        return BeanUtils.copyNewInstanceProperties(vo, BisBifRuleDTO.class);
    }
}
