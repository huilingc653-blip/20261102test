package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranCustInfoDao;
import com.bytter.biz.foreign.entity.FexTranCustInfo;
import com.bytter.biz.foreign.vo.FexTranCustInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("fexTranCustInfoService")
@Transactional
public class FexTranCustInfoService extends BaseService<FexTranCustInfo, String> {
    @Autowired
    private FexTranCustInfoDao fexTranCustInfoDao;

    @Override
    public BaseDao<FexTranCustInfo, String> getEntityDao() {
        return fexTranCustInfoDao;
    }

    /**
     * 获取保存得自定义数据
     * @param id
     * @return
     */
    public List<FexTranCustInfoVO> listCustInfoByBusId(String id){
        return fexTranCustInfoDao.listCustInfoByBusId(id);
    }
}
