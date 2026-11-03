package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexLinkmanInfoDao;
import com.bytter.biz.foreign.entity.FexLinkmanInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款联系人信息表
 * @date 2019-07-15 15:20:33
 */
@Service("fexLinkmanInfoService")
@Transactional
public class FexLinkmanInfoService extends BaseService<FexLinkmanInfo, String> {
    @Autowired
    private FexLinkmanInfoDao fexLinkmanInfoDao;

    @Override
    public BaseDao<FexLinkmanInfo, String> getEntityDao() {
        return fexLinkmanInfoDao;
    }

    /**
     * 根据单位Id获取汇款联系人信息
     * @param corpId
     * @return
     */
    public List<FexLinkmanInfo> listByCorpId(String corpId){
       return fexLinkmanInfoDao.listByCorpId(corpId);
    }

    /**
     * 汇款联系人信息保存
     *
     * @param fexLinkmanInfo
     */
    public void saveLinkmanNotExist(FexLinkmanInfo fexLinkmanInfo, ICurrentUser user) {
        FexLinkmanInfo entity = this.existLinkman(fexLinkmanInfo.getCorpId(), fexLinkmanInfo.getPayerName());
        if(entity != null){
            //更新电话信息
            entity.setPayerTel(fexLinkmanInfo.getPayerTel());
            entity.setUpdateBy(user.getLoginName());
            entity.setUpdateDate(DateUtils.getDate());
            this.updateEntity(entity);
        }else{
            //新增汇款联系人信息
            entity = new FexLinkmanInfo();
            BeanUtils.copyNotNullProperties(fexLinkmanInfo, entity);
            entity.setCreateBy(user.getLoginName());
            entity.setCreateDate(DateUtils.getDate());
            this.saveOrUpdate(entity);
        }
    }

    /**
     * 判断是否存在汇款联系人
     * @param corpId
     * @param payerName
     * @return
     */
    public FexLinkmanInfo existLinkman(String corpId, String payerName){
        List<FexLinkmanInfo> list = fexLinkmanInfoDao.listByCorpId(corpId, payerName);
        if(!EmptyUtils.isEmptyList(list)){
            return list.get(0);
        }else {
            return null;
        }
    }
}
