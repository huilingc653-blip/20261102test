package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcAccuBalanceService;
import com.bytter.biz.finacial.api.vo.FcAccuBalanceVO;
import com.bytter.biz.finacial.inneraccount.entity.FcAccuBalance;
import com.bytter.biz.finacial.inneraccount.service.FcAccuBalanceService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
@Service("fcAccuBalanceServiceImpl")
@DubboService
@Transactional
public class FcAccuBalanceServiceImpl implements IFcAccuBalanceService {
    @Autowired
    FcAccuBalanceService fcAccuBalanceService;
    /**
     * 查找某一天的账号对象
     * @param date
     * @param account
     * @return
     */
    public FcAccuBalanceVO getEntityByAccountAndDate(Date date, String account) {
        FcAccuBalance fcAccuBalance = fcAccuBalanceService.getEntityByAccountAndDate(date, account);
        return BeanUtils.copyNewInstanceProperties(fcAccuBalance, FcAccuBalanceVO.class);
    }
    /**
     * 初始化余额积数
     * @param balanceDate
     * @param blnctrAcc
     * @param curId
     * @param cumu
     */
    public void initAccuBalance(Date balanceDate, String blnctrAcc, String curId, BigDecimal cumu, String loginName) {
        fcAccuBalanceService.initAccuBalance(balanceDate, blnctrAcc, curId, cumu, loginName);
    }
    /**
     * 更新账户的积数
     * @param account
     * @param balanceDate
     * @param cumu
     */
    public void updateAccountAccuBalanceByBalanceDate(String account, Date balanceDate, BigDecimal cumu){
        fcAccuBalanceService.updateAccountAccuBalanceByBalanceDate(account, balanceDate, cumu);
    }
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput){
        return fcAccuBalanceService.getDataTables(dataTablesInput);
    }
    public FcAccuBalanceVO getFcAccuBalanceById(String id){
        FcAccuBalance fcAccuBalance = fcAccuBalanceService.getFcAccuBalanceById(id);
        return BeanUtils.copyNewInstanceProperties(fcAccuBalance, FcAccuBalanceVO.class);
    }
    public void save(FcAccuBalanceVO fcAccuBalance){
        FcAccuBalance entity = BeanUtils.copyNewInstanceProperties(fcAccuBalance, FcAccuBalance.class);
        fcAccuBalanceService.save(entity);
    }
    public void saveOrUpdate(FcAccuBalanceVO fcAccuBalance){
        FcAccuBalance entity = BeanUtils.copyNewInstanceProperties(fcAccuBalance, FcAccuBalance.class);
        fcAccuBalanceService.saveOrUpdate(entity);
    }
    public void deleteEntity(FcAccuBalanceVO fcAccuBalance){
        FcAccuBalance entity = BeanUtils.copyNewInstanceProperties(fcAccuBalance, FcAccuBalance.class);
        fcAccuBalanceService.deleteEntity(entity);
    }
}
