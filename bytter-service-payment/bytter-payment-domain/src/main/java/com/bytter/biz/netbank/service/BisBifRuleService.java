package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.vo.BisBifRuleVO;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.system.api.IBisBifRuleService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BisBifRuleDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 接口代码业务规则表
 * @date 2017-05-17 10:16:20
 */
@Service("bisBifRuleService")
public class BisBifRuleService {
    @DubboReference(check = false)
    private IBisBifRuleService bisBifRuleService;
    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getById(String id) {
        return bisBifRuleService.getById(id);
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
     * @param entity
     */
    public void save(BisBifRuleDTO entity) {
        bisBifRuleService.save(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(BisBifRuleDTO entity) {
        bisBifRuleService.update(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(BisBifRuleDTO entity) {
        bisBifRuleService.saveOrUpdate(entity);
    }
    /**
     * 根据编码获取接口代码业务规则
     *
     * @param bifCode
     * @return
     */
    public BisBifRuleDTO findByBifCode(String bifCode) {
        return bisBifRuleService.findByBifCode(bifCode);
    }
    /**
     * 根据接口代码ID获取接口代码业务规则
     *
     * @param bifId
     * @return
     */
    public BisBifRuleDTO findByBifId(String bifId) {
        return bisBifRuleService.findByBifId(bifId);
    }
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<BisBifRuleVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
        return bisBifRuleService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 根据ID获取接口规则
     *
     * @param id
     * @return
     */
    public BisBifRuleDTO getByIdUnion(String id) {
        BisBifRuleDTO dto = bisBifRuleService.getByIdUnion(id);
        if(!ObjectUtils.isEmpty(dto)) {
	        dto.setIsMustBankCode(dictService.get("sf_yn", dto.getIsMustBankCode()));
	        dto.setIsBankOnline(dictService.get("sf_yn", dto.getIsBankOnline()));
	        dto.setIsPurpose(dictService.get("sf_yn", dto.getIsPurpose()));
            dto.setIsAddBillNo(dictService.get("sf_yn", dto.getIsAddBillNo()));
        }
        return dto;
    }
    /**
     * 获取所有接口代码业务规则信息
     *
     * @return
     */
    public List<BisBifRuleDTO> listAllUnion() {
        return bisBifRuleService.listAllUnion();
    }
}
