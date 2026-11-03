package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IRateMaintenceService;
import com.bytter.system.api.dto.InstRateBeanDto;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import com.bytter.system.dao.TbRateMaintenceDao;
import com.bytter.system.entity.TbLoanRateMaintence;
import com.bytter.system.vo.DictVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款利率维护
 * @date 2017-07-26 18:38:18
 */
@Service("rateMaintenceService")
@Transactional
@DubboService
public class RateMaintenceService extends BaseService<TbLoanRateMaintence, String> implements IRateMaintenceService {
    @Autowired
    private TbRateMaintenceDao loanRateMaintenceDao;

    @Autowired
    private DictService dictService;

    @Override
    public BaseDao<TbLoanRateMaintence, String> getEntityDao() {
        return loanRateMaintenceDao;
    }

    /**
     * 功能介绍 : 根据基准利率查询是否关联贷款利率维护<br>
     *
     * @param baseRateId 基准利率ID
     * @return 贷款利率维护关联基准利率：true,否则：false
     */
    @Override
    public boolean checkBaseRateById(String baseRateId) {
        return loanRateMaintenceDao.checkBaseRateById(baseRateId);
    }

    /**
     * 本位币贷款利率维护 - 分页查询
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listLoanRateMaintence(DataTablesInput dataTablesInput, String loginName) {
        DataTablesOutput<LoanRateMaintenceVO> output = loanRateMaintenceDao.listLoanRateMaintence(dataTablesInput, loginName);
        // 数据填充
        Optional.ofNullable(output.getData()).ifPresent(listData -> {
            Map<String, Map<String, DictVO>> listDictMap = dictService.listDictMap("cms.periodCycle", "LoanRate.Status", "LoanRate.IsValid");
            listData.forEach(item -> {
                Optional.ofNullable(item.getPeriodCycle()).flatMap(periodCycle -> Optional.ofNullable(listDictMap.get("cms.periodCycle"))
                                .flatMap(dictMap -> Optional.ofNullable(dictMap.get(periodCycle.toString()))))
                        .ifPresent(dict -> item.setPeriodCycleName(dict.getName()));
                Optional.ofNullable(item.getStatus()).flatMap(status -> Optional.ofNullable(listDictMap.get("LoanRate.Status"))
                                .flatMap(dictMap -> Optional.ofNullable(dictMap.get(status.toString()))))
                        .ifPresent(dict -> item.setStatusName(dict.getName()));
                Optional.ofNullable(item.getIsValid()).flatMap(isValid -> Optional.ofNullable(listDictMap.get("LoanRate.IsValid"))
                                .flatMap(dictMap -> Optional.ofNullable(dictMap.get(isValid))))
                        .ifPresent(dict -> item.setIsValidName(dict.getName()));
            });
        });
        return output;
    }

    /**
     * 查询最大的生效日期
     */
    @Override
    public Date checkTimeOfTakeEffect(TbRateMaintenceDTO tbRateMaintenceDTO) {
        return loanRateMaintenceDao.checkTimeOfTakeEffect(tbRateMaintenceDTO);
    }

    /**
     * 功能介绍 : 根绝贷款期限Id与生效日期查询是否存在重复<br>
     *
     * @return 重复：true ,否则false
     */
    @Override
    public boolean checkRepeatByPeriodIdAndTime(String creditPeriodId, Date timeOfTakeEffect, String moneyType, String baseRateId) {
        return loanRateMaintenceDao.checkRepeatByPeriodIdAndTime(creditPeriodId, timeOfTakeEffect, moneyType, baseRateId);
    }

    /**
     * 获取利率信息
     *
     * @param id
     * @return
     */
    @Override
    public TbRateMaintenceDTO getById(String id) {
        TbLoanRateMaintence tbLoanRateMaintence = loanRateMaintenceDao.get(id);
        if (EmptyUtils.isEmpty(tbLoanRateMaintence)) {
            return null;
        }
        TbRateMaintenceDTO dto = new TbRateMaintenceDTO();
        BeanUtils.copyNotNullProperties(tbLoanRateMaintence, dto);
        return dto;
    }

    @Override
    public Date getNextMinDate(TbRateMaintenceDTO dto) {
        return loanRateMaintenceDao.getNextMinDate(dto);
    }

    /**
     * 保存或更新利率信息
     *
     * @param dto
     */
    @Override
    public void saveOrUpdate(TbRateMaintenceDTO dto) {
        if (EmptyUtils.isEmpty(dto)) {
            return;
        }
        TbLoanRateMaintence tbLoanRateMaintence = new TbLoanRateMaintence();
        BeanUtils.copyNotNullProperties(dto, tbLoanRateMaintence);
        super.saveOrUpdate(tbLoanRateMaintence);
    }

    /**
     * 删除利率信息
     *
     * @param dto
     */
    @Override
    public void deleteByEntity(TbRateMaintenceDTO dto) {
        if (EmptyUtils.isEmpty(dto)) {
            return;
        }
        TbLoanRateMaintence tbLoanRateMaintence = new TbLoanRateMaintence();
        BeanUtils.copyNotNullProperties(dto, tbLoanRateMaintence);
        //TODO xiay 因session已经关闭，死亡实例无法正常删除
//        super.deleteEntity(tbLoanRateMaintence);
        super.deleteEntity((TbLoanRateMaintence) this.getEntityDao().merge(tbLoanRateMaintence));
    }

    /**
     * 更新利率信息
     *
     * @param dto
     */
    @Override
    public void updateByEntity(TbRateMaintenceDTO dto) {
        if (EmptyUtils.isEmpty(dto)) {
            return;
        }
        TbLoanRateMaintence tbLoanRateMaintence = new TbLoanRateMaintence();
        BeanUtils.copyNotNullProperties(dto, tbLoanRateMaintence);
        super.updateEntity(tbLoanRateMaintence);
    }

    /**
     * 功能介绍 : 批量保存利率信息
     *
     * @param rateMaintenceList
     */
    @Override
    public void batchSaveDate(List<TbRateMaintenceDTO> rateMaintenceList) {
        List<TbLoanRateMaintence> tbRateMaintenceList = null;
        if (rateMaintenceList.size() > 0) {
            tbRateMaintenceList = BeanUtils.copyListProperties(rateMaintenceList, TbLoanRateMaintence.class);
            this.batchSave(tbRateMaintenceList);
        }
    }

    /**
     * 功能介绍 : 根据条件查找合适的维护利率
     */
    public List<LoanRateMaintenceVO> getRateMaintence(String plDate, String referenceRateType, String curId,
                                                      String repayCycle) {
        return loanRateMaintenceDao.getRateMaintence(plDate, referenceRateType, curId, repayCycle);
    }

    /**
     * 根据传入参数获取利率列表
     *
     * @param params
     * @return
     */
    @Override
    public List<TbRateMaintenceDTO> listLoanRateMaintence(Map<String, Object> params) {
        List<TbLoanRateMaintence> tbRateMaintenceList = loanRateMaintenceDao.listLoanRateMaintence(params);
        if (ListUtils.isNotNullOrEmpty(tbRateMaintenceList)) {
            List<TbRateMaintenceDTO> rateMaintenceList = BeanUtils.copyListProperties(tbRateMaintenceList, TbRateMaintenceDTO.class);
            return rateMaintenceList;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * @param params
     * @return
     */
    @Override
    public List<TbRateMaintenceDTO> listRateMaintenceSort(Map<String, Object> params) {
        List<TbLoanRateMaintence> tbRateMaintenceList = loanRateMaintenceDao.listRateMaintenceSort(params);
        if (ListUtils.isNotNullOrEmpty(tbRateMaintenceList)) {
            List<TbRateMaintenceDTO> rateMaintenceList = BeanUtils.copyListProperties(tbRateMaintenceList, TbRateMaintenceDTO.class);
            return rateMaintenceList;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 功能介绍 : <br>
     * 根据Id查询贷款利率维护VO并注入字典
     */
    @Override
    public LoanRateMaintenceVO getLoanRateById(String id) {
        return loanRateMaintenceDao.getLoanRateById(id);
    }

    @Override
    public List<InstRateBeanDto> getLoanRateMaintence(Set<String> repayCycles) {
        return loanRateMaintenceDao.getLoanRateMaintence(repayCycles);
    }

    /**
     * 功能介绍：根据生效日期币别基准利率类型查询 贷款利率
     *
     * @param time 生效日期
     * @param cur  币别
     * @param base 基础利率
     * @return
     */
    @Override
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCurBase(Date time, String cur, String base) {
        return loanRateMaintenceDao.getRateMaintenceByTimeCurBase(time, cur, base);
    }

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
    @Override
    public List<LoanRateMaintenceVO> getAllRateMaintenceByTimeCurBase(Date time, String cur, String base) {
        return loanRateMaintenceDao.getAllRateMaintenceByTimeCurBase(time, cur, base);
    }

    /**
     * 功能介绍：根据条件获取利率相关信息
     *
     * @param plDate
     * @param referenceRateType
     * @param curId
     * @param repayCycle
     * @return
     */
    @Override
    public List<LoanRateMaintenceVO> getRateMaintenceByCondition(String plDate, String referenceRateType, String curId, String repayCycle) {
        return loanRateMaintenceDao.getRateMaintence(plDate, referenceRateType, curId, repayCycle);
    }

    /**
     * 功能介绍：根据生效日期币别获取利率类型
     *
     * @param: time 生效日期
     * @param: cur 币别
     */
    public List<LoanRateMaintenceVO> getRateMaintenceByTimeCur(Date time, String cur) {
        return loanRateMaintenceDao.getRateMaintenceByTimeCur(time, cur);
    }
}
