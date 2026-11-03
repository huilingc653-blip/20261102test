package com.bytter.biz.finacial.inneraccount.service.impl;
import com.bytter.biz.finacial.api.service.IFcOutAccBalanceService;
import com.bytter.biz.finacial.api.vo.FcOutAccBalanceVO;
import com.bytter.biz.finacial.inneraccount.entity.FcOutAccBalance;
import com.bytter.biz.finacial.inneraccount.service.FcOutAccBalanceService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
@Service("fcOutAccBalanceServiceImpl")
@Transactional
@DubboService
public class FcOutAccBalanceServiceImpl implements IFcOutAccBalanceService {
    @Autowired
    private FcOutAccBalanceService fcOutAccBalanceService;
    /**
     * 删除对象
     * @param vo
     */
    @Override
    public void deleteEntity(FcOutAccBalanceVO vo){
        FcOutAccBalance entity = new FcOutAccBalance();
        BeanUtils.copyProperties(vo, entity);
        fcOutAccBalanceService.deleteEntity(entity);
    }
    /**
     * 保存
     * @param vo
     */
    public void save(FcOutAccBalanceVO vo){
        FcOutAccBalance entity = new FcOutAccBalance();
        BeanUtils.copyProperties(vo, entity);
        fcOutAccBalanceService.saveEntity(entity);
    }
    /**
     * 更新
     * @param vo
     */
    public void update(FcOutAccBalanceVO vo){
        FcOutAccBalance entity = new FcOutAccBalance();
        BeanUtils.copyProperties(vo, entity);
        fcOutAccBalanceService.updateEntity(entity);
    }
    /**
     * 保存or更新
     * @param vo
     */
    @Override
    public void saveOrUpdate(FcOutAccBalanceVO vo){
        FcOutAccBalance entity = new FcOutAccBalance();
        BeanUtils.copyProperties(vo, entity);
        fcOutAccBalanceService.saveOrUpdate(entity);
    }
    /**
     * 初始化余额
     * @param balanceDate 余额日期
     * @param blnctrAcc 账号
     * @param accName 户名
     */
    public void initAccountBalance(java.util.Date balanceDate, String blnctrAcc, String accName,String curId,String loginName){
        FcOutAccBalance fcAccBalance = new FcOutAccBalance();
        fcAccBalance.setAccount(blnctrAcc);
        fcAccBalance.setCreditCount(0);
        fcAccBalance.setCreditMoney(BigDecimal.ZERO);
        fcAccBalance.setCreditNum(0);
        fcAccBalance.setCreditPageNum(0);
        fcAccBalance.setDebitCount(0);
        fcAccBalance.setDebitMoney(BigDecimal.ZERO);
        fcAccBalance.setDebitNum(0);
        fcAccBalance.setDebitPageNum(0);
        fcAccBalance.setOptDate(balanceDate);
        fcAccBalance.setTBalance(BigDecimal.ZERO);
        fcAccBalance.setTCount(0);
        fcAccBalance.setTPageNum(0);
        fcAccBalance.setYBalance(BigDecimal.ZERO);
        fcAccBalance.setYCount(0);
        fcAccBalance.setCurCode(curId);
        fcAccBalance.setYPageNum(0);
        fcAccBalance.setCreateTime(DateUtils.getDate());
        fcAccBalance.setCreateBy(loginName);
        fcOutAccBalanceService.saveOrUpdate(fcAccBalance);
    }
    @Override
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        return null;
    }
    @Override
    public FcOutAccBalanceVO getFcOutAccBalanceById(String id) {
        return null;
    }
}
