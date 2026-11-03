package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexCashFlowCustInfoDao;
import com.bytter.biz.foreign.entity.FexCashFlowCustInfo;
import com.bytter.biz.foreign.vo.FexCashFlowCustInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("fexCashFlowCustInfoService")
@Transactional
public class FexCashFlowCustInfoService extends BaseService<FexCashFlowCustInfo, String> {
    @Autowired
    private FexCashFlowCustInfoDao fexCashFlowCustInfoDao;

    @Override
    public BaseDao<FexCashFlowCustInfo, String> getEntityDao() {
        return fexCashFlowCustInfoDao;
    }

    /**
     * 获取保存得自定义数据
     * @param id
     * @return
     */
    public List<FexCashFlowCustInfoVO> listCustInfoByBusId(String id){
        return fexCashFlowCustInfoDao.listCustInfoByBusId(id);
    }
}
