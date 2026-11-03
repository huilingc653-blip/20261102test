package com.bytter.bill.domain.bill.service;

import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("btCurrencyService")
@Transactional
public class BtCurrencyService extends BaseService<String, AbAccrecords> {

    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    @Override
    public BaseDao<String, AbAccrecords> getEntityDao() {
        return null;
    }

    /**
     * 通过ID查找网点、单位
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public BtCurrencyDTO get(String id){
        return btCurrencyService.getObjectById(id);
    }

    /**
     * 根据币种名称查询币种
     *
     * @param curName 币种名称
     * @return 币种DTO
     */
    @Transactional(readOnly = true)
    public BtCurrencyDTO getByCurName(String curName) {
        return btCurrencyService.getByCurNameOrEnglishCode(curName);
    }


}
