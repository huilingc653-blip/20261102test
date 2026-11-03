package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexPayerInfoDao;
import com.bytter.biz.foreign.entity.FexPayerInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款人信息表
 * @date 2019-07-15 15:20:33
 */
@Service("fexPayerInfoService")
@Transactional
public class FexPayerInfoService extends BaseService<FexPayerInfo, String> {
    @Autowired
    private FexPayerInfoDao fexPayerInfoDao;

    @Override
    public BaseDao<FexPayerInfo, String> getEntityDao() {
        return fexPayerInfoDao;
    }

    /**
     * 根据汇款账号获取汇款人信息
     *
     * @param payerAcc
     * @return
     */
    public FexPayerInfo getByPayerAcc(String payerAcc) {
        return this.fexPayerInfoDao.getByPayerAcc(payerAcc);
    }

    /**
     * 汇款人信息保存
     *
     * @param fexPayerInfo
     */
    public void savePayerNotExist(FexPayerInfo fexPayerInfo, ICurrentUser user) {
        FexPayerInfo entity = this.getByPayerAcc(fexPayerInfo.getPayerAcc());
        if(entity != null){
            //更新汇款人信息
            entity.setPayerNameEn(fexPayerInfo.getPayerNameEn());
            entity.setPayerAddEn(fexPayerInfo.getPayerAddEn());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            this.updateEntity(entity);
        }else{
            //新增汇款联系人信息
            entity = new FexPayerInfo();
            BeanUtils.copyNotNullProperties(fexPayerInfo, entity);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            this.saveOrUpdate(entity);
        }
    }

}
