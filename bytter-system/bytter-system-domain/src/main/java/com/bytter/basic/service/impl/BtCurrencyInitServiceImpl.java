package com.bytter.basic.service.impl;
import com.bytter.basic.entity.BtCurrencyInit;
import com.bytter.basic.service.BtCurrencyInitService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyInitService;
import com.bytter.system.api.dto.BtCurrencyInitDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@DubboService
@Service("btCurrencyInitServiceI")
public class BtCurrencyInitServiceImpl implements IBtCurrencyInitService {
    @Autowired
    private BtCurrencyInitService btCurrencyInitService;
    @Override
    public BtCurrencyInitDTO getObjectById(String id) {
        if (EmptyUtils.isBlank(id)) {
            return null;
        }
        BtCurrencyInit btCurrencyInit = btCurrencyInitService.get(id, true);
        if (EmptyUtils.isEmpty(btCurrencyInit)) {
            return null;
        }
        BtCurrencyInitDTO btCurrencyInitDTO = new BtCurrencyInitDTO();
        BeanUtils.copyProperties(btCurrencyInit, btCurrencyInitDTO);
        return btCurrencyInitDTO;
    }
}
