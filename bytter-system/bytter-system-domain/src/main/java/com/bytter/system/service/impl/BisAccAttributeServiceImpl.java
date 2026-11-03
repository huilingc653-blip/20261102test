package com.bytter.system.service.impl;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBisAccAttributeService;
import com.bytter.system.api.dto.BisAccAttributeDTO;
import com.bytter.system.entity.BisAccAttribute;
import com.bytter.system.service.BisAccAttributeService;
import com.bytter.system.vo.BisAccAttributeVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @Description 账户属性接口实现类
 * @date 2017.9.14
 */
@Service("bisAccAttributeServiceI")
@DubboService
@Transactional
public class BisAccAttributeServiceImpl implements IBisAccAttributeService {
    @Autowired
    BisAccAttributeService bisAccAttributeService;
    /**
     * 根据编码获取账户属性
     *
     * @param attributeCode
     * @return
     */
    @Override
    public BisAccAttributeDTO findByCode(String attributeCode) {
        BisAccAttribute bisAccAttribute = bisAccAttributeService.findByCode(attributeCode);
        BisAccAttributeDTO bisAccAttributeDTO = new BisAccAttributeDTO();
        if(bisAccAttribute != null) {
            BeanUtils.copyNotNullProperties(bisAccAttribute, bisAccAttributeDTO);
        }
        return bisAccAttributeDTO;
    }
    /**
     * 获取可对外付款的账户属性
     * @return
     */
    @Override
    public List<BisAccAttributeDTO> getAttriButeForEign(){
        List<BisAccAttributeVO> list = bisAccAttributeService.getAttriButeForEign();
        List<BisAccAttributeDTO> listDTO = new ArrayList<>();
        if(!EmptyUtils.isEmptyList(list)) {
            for(BisAccAttributeVO bisAccAttribute:list){
                BisAccAttributeDTO bisAccAttributeDTO = new BisAccAttributeDTO();
                BeanUtils.copyNotNullProperties(bisAccAttribute, bisAccAttributeDTO);
                listDTO.add(bisAccAttributeDTO);
            }
        }
        return  listDTO;
    }
    @Override
    public DataTablesOutput dataTable(DataTablesInput dataTablesInput){
        DataTablesOutput dataTablesOutput = bisAccAttributeService.dataTable(dataTablesInput);
        List<BisAccAttribute> data = dataTablesOutput.getData();
        List<BisAccAttributeDTO> bisAccAttributeVO = BeanUtils.copyListProperties(data, BisAccAttributeDTO.class);
//        for(BisAccAttributeDTO vo :bisAccAttributeVO){
//            vo.setAttributeNameShow(vo.getAttributeName());
//        }
        //inject(bisAccAttributeVO);
        dataTablesOutput.setData(bisAccAttributeVO);
        return dataTablesOutput;
    }
    /**
     * 判断账户属性是否在使用中
     *
     * @return
     */
    public boolean queryUse(String attributeId) {
        return bisAccAttributeService.queryUse(attributeId);
    }
    /**
     * 获取VO对象
     * @param id
     * @return
     */
    @Override
    public BisAccAttributeDTO getVO(String id) {
        BisAccAttributeVO accAttributeVO = bisAccAttributeService.getVO(id);
        BisAccAttributeDTO dto = BeanUtils.copyNewInstanceProperties(accAttributeVO, BisAccAttributeDTO.class);
        //inject(accAttributeVO);
        return dto;
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        bisAccAttributeService.delete(id);
    }
    /**
     * 保存
     *
     * @param dto
     */
    public void save(BisAccAttributeDTO dto, String locale) {
        BisAccAttribute entity = new BisAccAttribute();
        BeanUtils.copyNotNullProperties(dto, entity);
        bisAccAttributeService.save(entity,locale);
    }
    /**
     * 保存or更新
     *
     * @param dto
     */
    public void saveOrUpdate(BisAccAttributeDTO dto,String locale) {
        BisAccAttribute entity = new BisAccAttribute();
        BeanUtils.copyNotNullProperties(dto, entity);
        bisAccAttributeService.saveOrUpdate(entity, locale);
    }
    /**
     * 删除对象
     *
     * @param dto
     */
    public void deleteBisAccAttribute(BisAccAttributeDTO dto) {
        BisAccAttribute entity = new BisAccAttribute();
        if(null != entity) {
            bisAccAttributeService.deleteBisAccAttribute(entity);
        }
    }
    /**
     * 批量删除对象
     *
     * @param ids
     */
    public void batchDelete(String ids) {
        bisAccAttributeService.batchDelete(ids);
    }
    /**
     * 获取有效的账户属性
     *
     * @return
     */
    public List<BisAccAttributeDTO> getValidAttribute() {
        List<BisAccAttributeVO> list= bisAccAttributeService.getValidAttribute();
        List<BisAccAttributeDTO> dtoList = BeanUtils.copyListProperties(list, BisAccAttributeDTO.class);
        return dtoList;
    }
    public Map<String, BisAccAttributeDTO> getValidAttributeMap() {
        List<BisAccAttributeVO> list = bisAccAttributeService.getValidAttribute();
        List<BisAccAttributeDTO> dtoList = BeanUtils.copyListProperties(list, BisAccAttributeDTO.class);
        Map<String, BisAccAttributeDTO> map = new HashMap<>();
        for (BisAccAttributeDTO dto : dtoList) {
            map.put(dto.getId(), dto);
        }
        return map;
    }
}
