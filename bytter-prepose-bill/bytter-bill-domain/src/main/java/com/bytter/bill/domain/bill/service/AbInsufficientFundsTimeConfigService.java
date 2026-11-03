package com.bytter.bill.domain.bill.service;


import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.dao.AbInsufficientFundsTimeConfigDao;
import com.bytter.bill.domain.bill.entity.AbInsufficientFundsTimeConfig;
import com.bytter.bill.domain.bill.vo.AbInsufficientFundsTimeConfigVO;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

/**
 * @Description 票据银行余额不足时间配置服务类
 * @author zhous
 * @date 2025/6/24 10:33
 */
@Service("abInsufficientFundsTimeConfigService")
@Transactional
public class AbInsufficientFundsTimeConfigService extends BaseService<AbInsufficientFundsTimeConfig, String> {

    @Autowired
    private AbInsufficientFundsTimeConfigDao abInsufficientFundsTimeConfigDao;

    @Autowired
    private AbCorpService abCorpService;

    @DubboReference(check = false)
    private IDictService dictService;

    @Override
    public BaseDao<AbInsufficientFundsTimeConfig, String> getEntityDao() {
        return abInsufficientFundsTimeConfigDao;
    }

    public DataTablesOutput dataTable(DataTablesInput input) {
        DataTablesOutput<AbInsufficientFundsTimeConfigVO> out = abInsufficientFundsTimeConfigDao.dataTable(input);
        Optional.ofNullable(out.getData()).ifPresent(listData->{
            Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap("AB_INSUFFICIENT_FUNDS_TIME_STATUS");
            for (AbInsufficientFundsTimeConfigVO vo : listData){
                injectValues(vo, listDictMap);
            }
        });
        return out;
    }

    /**
     * @Description 注入字典信息
     * @author zhous
     * @date 2025/2/25 8:45
     */
    private void injectValues(AbInsufficientFundsTimeConfigVO bill, Map<String, Map<String, DictDTO>> listDictMap){
        // 状态
        Optional.ofNullable(bill.getStatus()).ifPresent(status -> {
            Map<String, DictDTO> dictDtoMap = listDictMap.get("AB_INSUFFICIENT_FUNDS_TIME_STATUS");
            Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
                    .ifPresent(dict -> bill.setStatusName(dict.getName()));
        });
    }

    public void saveOrUpdate(AbInsufficientFundsTimeConfigVO vo, ICurrentUser currentUser) {
        // 如果有id 则为修改
        AbInsufficientFundsTimeConfig entity =null;
        if (StringUtils.isNotEmpty(vo.getId())){
            entity = abInsufficientFundsTimeConfigDao.get(vo.getId(), true);
            if(StringUtils.isNotEmpty(vo.getTime())){
                entity.setTime(vo.getTime());
            }
            entity.setUpdateBy(currentUser.getLoginName());
            entity.setUpdateTime(DateUtils.getDate());
        }else{
            int count  = abInsufficientFundsTimeConfigDao.getRepeatData(vo.getBankTypeId());
            if(count > 0){
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BILLTARGETPROPORTION_REPEAT);
            }
            entity = new AbInsufficientFundsTimeConfig();
            BeanUtil.copyProperties(vo, entity);
            entity.setStatus(AccebillConstant.INSUFFICIENT_FUNDS_TIME_CONFIG_STATUS_1);
            entity.setCreateBy(currentUser.getLoginName());
            entity.setCreateTime(DateUtils.getDate());
        }
        saveOrUpdate(entity);
    }

    public void deleteConfig(String id, ICurrentUser currentUser) {
        AbInsufficientFundsTimeConfig abInsufficientFundsTimeConfig = abInsufficientFundsTimeConfigDao.get(id, true);
        if(abInsufficientFundsTimeConfig == null){
            throw new RuntimeException("该配置不存在");
        }
        abInsufficientFundsTimeConfig.setIsDeleted(AccebillConstant.IS_DELETE);
        abInsufficientFundsTimeConfig.setUpdateBy(currentUser.getLoginName());
        abInsufficientFundsTimeConfig.setUpdateTime(DateUtils.getDate());
        updateEntity(abInsufficientFundsTimeConfig);
    }

    public void batchDelete(String ids, ICurrentUser currentUser) {
        String[] delLists = ids.split(",");
        if(delLists.length > 0){
            for (int i = 0; i < delLists.length; i++) {
                deleteConfig(delLists[i],currentUser);
            }
        }
    }

    public void batchDisable(String ids, ICurrentUser currentUser) {
        String[] disableLists = ids.split(",");
        if(disableLists.length > 0){
            for (int i = 0; i < disableLists.length; i++) {
                disableConfig(disableLists[i],currentUser);
            }
        }
    }

    private void disableConfig(String id, ICurrentUser currentUser) {
        AbInsufficientFundsTimeConfig abInsufficientFundsTimeConfig = abInsufficientFundsTimeConfigDao.get(id, true);
        if(abInsufficientFundsTimeConfig == null){
            throw new RuntimeException("该配置不存在");
        }
        abInsufficientFundsTimeConfig.setStatus(AccebillConstant.INSUFFICIENT_FUNDS_TIME_CONFIG_STATUS_0);
        abInsufficientFundsTimeConfig.setUpdateBy(currentUser.getLoginName());
        abInsufficientFundsTimeConfig.setUpdateTime(DateUtils.getDate());
        updateEntity(abInsufficientFundsTimeConfig);
    }
}
