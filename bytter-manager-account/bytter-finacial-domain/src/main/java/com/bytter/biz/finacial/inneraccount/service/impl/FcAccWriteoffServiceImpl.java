package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcAccWriteoffService;
import com.bytter.biz.finacial.api.vo.FcAccWriteoffVO;
import com.bytter.biz.finacial.inneraccount.entity.FcAccWriteoff;
import com.bytter.biz.finacial.inneraccount.service.FcAccWriteoffService;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
@Service("fcAccWriteoffServiceImpl")
@DubboService
@Transactional
public class FcAccWriteoffServiceImpl implements IFcAccWriteoffService {
    @Autowired
    private FcAccWriteoffService fcAccWriteoffService;
    /**
     * 添加账户注销记录
     *
     * @param blnctrAcc
     */
    public String saveObject(String blnctrAcc, String transferAcc, Date dayOver, String loginName) {
        return fcAccWriteoffService.saveObject(blnctrAcc, transferAcc, dayOver, loginName);
    }
    /**
     * 修改全局id
     *
     * @param blnctrAcc
     */
    public FcAccWriteoffVO getFcAccWriteoff(String blnctrAcc) {
        FcAccWriteoff fcAccWriteoff = fcAccWriteoffService.getFcAccWriteoff(blnctrAcc);
        return BeanUtils.copyNewInstanceProperties(fcAccWriteoff, FcAccWriteoffVO.class);
    }
    /**
     * 修改全局id
     *
     * @param blnctrAcc
     */
    public void updateBusGlobalId(String blnctrAcc, String busGlobalId) {
        fcAccWriteoffService.updateBusGlobalId(blnctrAcc, busGlobalId);
    }
    /**
     * 修改账户注销记录
     *
     * @param blnctrAcc
     */
    public void updateObject(String blnctrAcc, String loginName) {
        fcAccWriteoffService.updateObject(blnctrAcc, loginName);
    }
    /**
     * 通过内部账号删除账户销户明细
     *
     * @param blnctrAcc
     * @return
     */
    public int delWriteOffByBlnctrAcc(String blnctrAcc) {
        return fcAccWriteoffService.delWriteOffByBlnctrAcc(blnctrAcc);
    }
}
