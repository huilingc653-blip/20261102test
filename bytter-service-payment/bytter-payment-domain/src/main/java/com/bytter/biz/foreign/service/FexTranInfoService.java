package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranInfoDao;
import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 交易编码表
 * @date 2019-07-15 15:20:33
 */
@Service("fexTranInfoService")
@Transactional
public class FexTranInfoService extends BaseService<FexTranInfo, String> {
    @Autowired
    private FexTranInfoDao fexTranInfoDao;

    @Override
    public BaseDao<FexTranInfo, String> getEntityDao() {
        return fexTranInfoDao;
    }

    /**
     * 获取所有的交易编码（根据编码排序）
     * @return
     */
    public List<FexTranInfo> listOrderByTranCode(){
        return fexTranInfoDao.listOrderByTranCode();
    }

    /**
     * 根据交易编码获取交易编码信息
     * @param tranCode
     * @return
     */
    public FexTranInfo getByTranCode(String tranCode){
        return  fexTranInfoDao.getByTranCode(tranCode);
    }

    /**
     * 根据项目名称获取交易编码信息
     * @param tranName
     * @return
     */
    public FexTranInfo getByTranName(String tranName){
        return fexTranInfoDao.getByTranName(tranName);
    }

}
