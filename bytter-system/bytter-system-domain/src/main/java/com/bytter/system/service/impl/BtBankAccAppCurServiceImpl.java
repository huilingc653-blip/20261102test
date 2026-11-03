package com.bytter.system.service.impl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccAppCurService;
import com.bytter.system.api.dto.BtBankAccAppCurDTO;
import com.bytter.system.dao.BtBankAccAppCurDao;
import com.bytter.system.entity.BtBankAccAppCur;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service("btBankAccAppCurServiceI")
@DubboService
@Transactional
public class BtBankAccAppCurServiceImpl extends BaseService<BtBankAccAppCur, String> implements IBtBankAccAppCurService {
    @Autowired
    private BtBankAccAppCurDao btBankAccAppCurDao;
    @Override
    public BaseDao<BtBankAccAppCur, String> getEntityDao() {
        return btBankAccAppCurDao;
    }
    @Override
    public List<BtBankAccAppCurDTO> listByAppId(String appId) {
        List<BtBankAccAppCur> list = btBankAccAppCurDao.listBy("appId", appId);
        if (EmptyUtils.isEmptyList(list)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyListProperties(list, BtBankAccAppCurDTO.class);
    }
    @Override
    public void delByAppId(String appId) {
        List<BtBankAccAppCur> appCurList = btBankAccAppCurDao.listBy("appId", appId);
		if (!EmptyUtils.isEmpty(appCurList)) {
			for (BtBankAccAppCur cur : appCurList) {
				this.getEntityDao().delete(this.getEntityDao().merge(cur));
			}
		}
    }
    @Override
    public void saveByAppId(String appId, String[] curIds) {
        for (int i = 0; i < curIds.length; i++) {
            if ("all".equals(curIds[i])) {
                continue;
            }
			BtBankAccAppCur cur = this.btBankAccAppCurDao.getBtBankAccAppCur(appId, curIds[i]);
			if (EmptyUtils.isEmpty(cur)) {
				BtBankAccAppCur accCur = new BtBankAccAppCur();
				accCur.setAppId(appId);
				accCur.setCurId(curIds[i]);
				this.getEntityDao().save(accCur);
			}
        }
    }
}
