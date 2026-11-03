package com.bytter.system.api;

import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.pricing.dto.FcInstrateDTO;

import java.math.BigDecimal;

/**
 * 科目利率对外接口
 */
public interface IFcInstrateService {
    //根据科目获取利率
    BigDecimal getFcInstrateByObjectId(String objectId, String curId);

    /**
     * 删除对象
     * @param entity
     */
    public void deleteEntity(FcInstrateDTO entity);
    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id);
    /**
     * 保存
     * @param entity
     */
    public void save(FcInstrateDTO entity,String loginName);
    /**
     * 更新
     * @param entity
     */
    public void update(FcInstrateDTO entity);

    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(FcInstrateDTO entity);
    /**
     * 获取科目利率table
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput fcInstrateList(DataTablesInput dataTablesInput);

    /**
     * 根据id查询科目利率详情
     * @param id
     * @return
     */
    public FcInstrateDTO getfcInstrate(String id);
    /**
     * 删除科目利率
     * @param id
     */
    public void deleteFcInstrate(String id);

    /**
     * 获取最近一次的科目利率
     * @param subjectCode
     * @param curCode
     * @param instrtType
     * @param endUnit
     * @return
     */
    public FcInstrateDTO getLastInstrate(String subjectCode, String curCode, String instrtType, String endUnit);

}
