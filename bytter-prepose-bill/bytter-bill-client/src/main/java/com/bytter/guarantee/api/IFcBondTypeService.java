package com.bytter.guarantee.api;

import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.vo.FcBondTypeVO;

import java.util.List;

public interface IFcBondTypeService {

    /**
     * 获取类型table
     * @param dataTablesInput
     * @return
     */
     DataTablesOutput<FcBondTypeVO> bondTypeList(DataTablesInput dataTablesInput);
    /**
     * 保存
     * @param entity
     */
     void save(FcBondTypeVO entity,String userId);
    /**
     * 更新
     * @param entity
     */
     void update(FcBondTypeVO entity,String userId);
    /**
     * 保存or更新
     * @param entity
     */
     void saveOrUpdate(FcBondTypeVO entity,String userId);
    /**
     * 根据id查询类型详情
     * @param id
     * @return
     */
     FcBondTypeVO getBondType(String id);
    /**
     * 获取有效保证金类型
     */
     List<FcBondTypeVO> getAllBondType();
    /**
     * 删除对象
     * @param entity
     */
     void deleteEntity(FcBondTypeVO entity);
    /**
     * 根据id删除
     * @param id
     */
     void delete(String id);

}
