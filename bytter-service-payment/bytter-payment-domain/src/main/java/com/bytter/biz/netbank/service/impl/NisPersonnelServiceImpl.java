package com.bytter.biz.netbank.service.impl;

import com.bytter.biz.netbank.dao.NisPersonnelDetailDao;
import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.payment.api.service.NisPersonnelService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class NisPersonnelServiceImpl implements NisPersonnelService {

    @Autowired
    private NisPersonnelDetailDao nisPersonnelDetailDao;

    /**
     * 根据职工编码判断用户信息是否重复
     *
     * @param id
     * @param workNo
     * @return false 不重复	true 重复
     */
    public boolean isRepeatWorkNo(String id, String workNo) {
        List<NisPersonnelDetail> list = nisPersonnelDetailDao.listBy("workNo", workNo);
        return this.isRepeat(id,list);
    }

    /**
     * 根据身份证号判断用户信息是否重复
     *
     * @param id
     * @param idcard
     * @return false 不重复	true 重复
     */
    public boolean isRepeatIdcard(String id, String idcard) {
        List<NisPersonnelDetail> list = nisPersonnelDetailDao.listBy("idcard", idcard);
        return this.isRepeat(id,list);
    }


    @Override
    public int getPersonnelCountByCorpId(String corpId) {
        return nisPersonnelDetailDao.getPersonnelCountByCorpId(corpId);
    }

    /**
     * 校验重复
     * @param id
     * @param list
     * @return
     */
    public boolean isRepeat(String id, List<NisPersonnelDetail> list){
        if (ListUtils.isNullOrEmpty(list)) {
            return false;
        } else {
            if (EmptyUtils.isBlank(id)) {
                return true;
            }
            NisPersonnelDetail obj = list.get(0);
            return obj.getId().equals(id) ? false : true;
        }
    }
}
