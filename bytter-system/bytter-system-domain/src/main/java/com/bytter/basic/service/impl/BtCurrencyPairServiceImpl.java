package com.bytter.basic.service.impl;

import com.bytter.basic.service.BtCurrencyPairService;
import com.bytter.basic.vo.BtCurrencyPairVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyPairService;
import com.bytter.system.api.dto.BtCurrencyPairDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@DubboService
@Service("btCurrencyPairServiceI")
public class BtCurrencyPairServiceImpl implements IBtCurrencyPairService {

    @Autowired
    private BtCurrencyPairService btCurrencyPairService;

    @Override
    public BtCurrencyPairDTO getBtCurrencyPairByCurId(String curId1, String curId2) {
        BtCurrencyPairDTO btCurrencyPairDTO = new BtCurrencyPairDTO();
        BtCurrencyPairVO btCurrencyPairVO = btCurrencyPairService.getBtCurrencyPairByCurId(curId1, curId2);
        if(null != btCurrencyPairVO){
            BeanUtils.copyNotNullProperties(btCurrencyPairVO, btCurrencyPairDTO);
        }
        return  btCurrencyPairDTO;
    }

    @Override
    public Map<String, BtCurrencyPairDTO> getCurrencyPairMap() {
        List<BtCurrencyPairVO> currencyPairList = btCurrencyPairService.getCurrencyPairjson();
        if (EmptyUtils.isEmpty(currencyPairList)){
            return null;
        }
        List<BtCurrencyPairDTO> currencyPairDTOS = new ArrayList<>();
        for (BtCurrencyPairVO currencyPairVO : currencyPairList){
            BtCurrencyPairDTO currencyPairDTO = new BtCurrencyPairDTO();
            BeanUtils.copyNotNullProperties(currencyPairVO, currencyPairDTO);
            currencyPairDTOS.add(currencyPairDTO);
        }
        return currencyPairDTOS.stream()
                // 过滤掉 id 为 null 的元素（可选，根据业务需求）
                .filter(pair -> pair.getId() != null)
                // 转换为 Map，key 是 id，value 是对象本身
                .collect(Collectors.toMap(
                        BtCurrencyPairDTO::getId,  // 生成 key 的函数
                        pair -> pair,             // 生成 value 的函数（此处直接使用对象本身）
                        (existing, replacement) -> existing  // 处理 key 冲突的合并函数
                ));
    }

    @Override
    public BtCurrencyPairDTO getById(String id) {
        BtCurrencyPairVO btCurrencyPairVO = btCurrencyPairService.getById(id);
        if(null != btCurrencyPairVO){
            BtCurrencyPairDTO btCurrencyPairDTO = new BtCurrencyPairDTO();
            BeanUtils.copyNotNullProperties(btCurrencyPairVO, btCurrencyPairDTO);
            return btCurrencyPairDTO;
        }else{
            return null;
        }
    }


}
