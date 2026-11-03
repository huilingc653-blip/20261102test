package com.bytter.system.api;

import com.bytter.system.api.dto.InstRateBeanDto;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRateMaintenceService {
    /**
     * 功能介绍 : 根据基准利率查询是否关联贷款利率维护
     * Params:
     * baseRateId – 基准利率ID
     * Returns:
     * 贷款利率维护关联基准利率：true,否则：false
     *
     * @param baseRateId
     * @return
     */
    boolean checkBaseRateById(String baseRateId);

    /**
     * 查询最大的生效日期
     */
    Date checkTimeOfTakeEffect(TbRateMaintenceDTO rateMaintenceDTO);

    /**
     * 功能介绍 : 根绝贷款期限Id与生效日期查询是否存在重复<br>
     *
     * @return 重复：true ,否则false
     */
    boolean checkRepeatByPeriodIdAndTime(String creditPeriodId, Date timeOfTakeEffect, String moneyType, String baseRateId);

    /**
     * 获取利率信息
     *
     * @param id
     * @return
     */
    TbRateMaintenceDTO getById(String id);

    Date getNextMinDate(TbRateMaintenceDTO rateMaintenceDTO);

    /**
     * 删除利率相关信息
     *
     * @param rateMaintenceDTO
     */
    void deleteByEntity(TbRateMaintenceDTO rateMaintenceDTO);

    /**
     * 更新利率相关信息
     *
     * @param rateMaintenceDTO
     */
    void updateByEntity(TbRateMaintenceDTO rateMaintenceDTO);

    /**
     * 保存或更新利率相关信息
     *
     * @param rateMaintenceDTO
     */
    void saveOrUpdate(TbRateMaintenceDTO rateMaintenceDTO);

    /**
     * 批量保存利率信息
     *
     * @param rateMaintenceDTO
     * @return
     */
    void batchSaveDate(List<TbRateMaintenceDTO> rateMaintenceList);

    /**
     * 功能介绍：根据传入参数获取利率相关列表
     *
     * @param params
     * @return
     */
    List<TbRateMaintenceDTO> listLoanRateMaintence(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<TbRateMaintenceDTO> listRateMaintenceSort(Map<String, Object> params);

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param time 生效日期
     * @param cur  币别
     * @param base 基础利率
     * @return
     */
    List<LoanRateMaintenceVO> getRateMaintenceByTimeCurBase(Date time, String cur, String base);

    /**
     * 功能介绍：根据条件获取利率相关信息
     *
     * @param plDate
     * @param referenceRateType
     * @param curId
     * @param repayCycle
     * @return
     */
    List<LoanRateMaintenceVO> getRateMaintenceByCondition(String plDate, String referenceRateType, String curId, String repayCycle);

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param: time
     * 生效日期
     * @param: cur
     * 币别
     * @param: base
     * 生效日期
     */
    List<LoanRateMaintenceVO> getAllRateMaintenceByTimeCurBase(Date time, String cur, String base);

    /**
     * 功能介绍 : <br>
     * 根据Id查询贷款利率维护VO并注入字典
     */
    LoanRateMaintenceVO getLoanRateById(String id);

    /**
     * 获取基础利率数据
     *
     * @param repayCycles 利率id
     * @return 利率集合
     */
    List<InstRateBeanDto> getLoanRateMaintence(Set<String> repayCycles);

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    List<LoanRateMaintenceVO> getRateMaintenceByTimeCur(Date time, String cur);
}
