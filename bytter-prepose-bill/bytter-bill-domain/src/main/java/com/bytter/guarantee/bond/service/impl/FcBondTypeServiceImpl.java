package com.bytter.guarantee.bond.service.impl;

import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.guarantee.api.IFcBondTypeService;
import com.bytter.guarantee.bond.entity.FcBondType;
import com.bytter.guarantee.bond.service.FcBondTypeService;
import com.bytter.guarantee.vo.FcBondTypeVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("fcBondTypeServiceImpl")
@DubboService
public class FcBondTypeServiceImpl implements IFcBondTypeService {

    @Autowired
    private FcBondTypeService fcBondTypeService;

    /**
     * 获取类型table
     * @param dataTablesInput
     * @return
     */
    @Override
    public DataTablesOutput<FcBondTypeVO> bondTypeList(DataTablesInput dataTablesInput){
        return fcBondTypeService.bondTypeList(dataTablesInput);
    }
    /**
     * 保存
     * @param entity
     */
    @Override
    public void save(FcBondTypeVO entity, String userId){
        FcBondType en = new FcBondType();
        BeanUtils.copyProperties(entity, en);
        fcBondTypeService.save(en,userId);
    }
    /**
     * 更新
     * @param entity
     */
    @Override
    public void update(FcBondTypeVO entity, String userId){
        FcBondType en = new FcBondType();
        BeanUtils.copyProperties(entity, en);
        fcBondTypeService.update(en,userId);
    }
    /**
     * 保存or更新
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcBondTypeVO entity, String userId){
        FcBondType fcBondType = fcBondTypeService.get(entity.getId(), true);
        BeanUtils.copyNotNullProperties(entity, fcBondType);
        fcBondTypeService.saveOrUpdate(fcBondType, userId);
    }
    /**
     * 根据id查询类型详情
     * @param id
     * @return
     */
    @Override
    public FcBondTypeVO getBondType(String id){
        return fcBondTypeService.getBondType(id);
    }
    /**
     * 获取有效保证金类型
     */
    @Override
    public List<FcBondTypeVO> getAllBondType(){
        return fcBondTypeService.getAllBondType();
    }
    /**
     * 删除对象
     * @param entity
     */
    @Override
    public void deleteEntity(FcBondTypeVO entity) {
        FcBondType en = new FcBondType();
        BeanUtils.copyProperties(entity, en);
        fcBondTypeService.deleteEntity(en);
    }
    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void delete(String id){
        fcBondTypeService.delete(id);
    }
}
