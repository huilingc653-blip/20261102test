package com.bytter.biz.netbank.service.impl;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.service.NisBillService;
import com.bytter.biz.payment.api.dto.NisBillDTO;
import com.bytter.biz.payment.api.service.INisBillService;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


import java.util.List;
import java.util.stream.Collectors;

@Service("nisBillServiceImpl")
@Transactional
@DubboService
public class NisBillServiceImpl implements INisBillService {
    @Autowired
    NisBillService nisBillService;
    @Override
    public String getBillBalances(String ids) {
        return nisBillService.getBillBalances(ids);
    }
    @Override
    public String getBillBalances(String ids,String payId) {
        return nisBillService.getBillBalances(ids,payId);
    }

    /**
     * 对付款占用票据进行批量写入
     * 会根据付款ID先删除再批量重新写入
     * @param nisBillDTOList
     */
    @Override
    public void batchNisBill(List<NisBillDTO> nisBillDTOList) {
        if(CollectionUtil.isNotEmpty(nisBillDTOList)) {
            List<String> payIdList=nisBillDTOList.stream().map(o->o.getPayId()).collect(Collectors.toList());
            nisBillService.delByPayIds(payIdList);
            List<NisBill> nisBillList = BeanUtil.copyToList(nisBillDTOList, NisBill.class);
            nisBillService.batchSave(nisBillList);
        }
    }
    @Override
    public String getBillIds(String payId) {
        return nisBillService.getBillIds(payId);
    }
    @Override
    public Map<String,String> getBillIdsByPayIds(String payIds) {
        if(payIds == null){
            return null;
        }
        Map<String,String> resultMap = new HashMap<>();
        String[] splitIdArr = payIds.split(",");
        for(String payId : splitIdArr){
            String billIds = nisBillService.getBillIds(payId);
            if(StringUtils.isNotBlank(billIds)){
                resultMap.put(payId,billIds);
            }
        }
        return resultMap;
    }
    public void saveOrUpdateSync(String payId, String billIds){
        nisBillService.saveOrUpdateSync(payId,billIds,null);
    }
}
