package com.bytter.biz.finacial.inneraccount.service.impl;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.dto.FcAccBalanceDTO;
import com.bytter.biz.finacial.api.service.IFcAccBalanceService;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.inneraccount.entity.FcAccBalance;
import com.bytter.biz.finacial.inneraccount.service.FcAccBalanceService;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账户联机余额对外接口实现类
 *
 * @author liyh
 */
@Service("fcAccbalanceServiceImpl")
@DubboService
@Transactional
public class FcAccBalanceServiceImpl implements IFcAccBalanceService {
    @Autowired
    private FcAccBalanceService accBalanceService;
    /**
     * 判断月账单是否可以核对
     *
     * @param date 月账单
     * @return true 存在，false 不存在
     */
    public boolean existBalance(String date) {
        return accBalanceService.existBalance(date);
    }
    public FcAccBalanceDTO getBalanceByAccountAndDate(String account, Date balanceDate) {
        FcAccBalance accBalance = accBalanceService.getEntityByAccountAndDate(balanceDate, account);
        if (!EmptyUtils.isEmpty(accBalance)) {
            FcAccBalanceDTO accBalanceDTO = new FcAccBalanceDTO();
            BeanUtils.copyProperties(accBalance, accBalanceDTO);
            return accBalanceDTO;
        }
        return null;
    }
    /**
     * 查找
     * @return
     */
    public FcAccBalanceVO getFcAccBalanceById(String id){
        FcAccBalance entity =  accBalanceService.getId(id);
        return BeanUtils.copyNewInstanceProperties(entity, FcAccBalanceVO.class);
    }
    public DataTablesOutput getDataTables(DataTablesInput input) {
        return accBalanceService.getDataTables(input);
    }
    /**
     * 删除对象
     * @param vo
     */
    public void deleteEntity(FcAccBalanceVO vo){
        FcAccBalance entity = BeanUtils.copyNewInstanceProperties(vo, FcAccBalance.class);
        accBalanceService.deleteEntity(entity);
    }
    /**
     * 根据id删除
     * @param id
     */
    public void delete(String id){
        accBalanceService.delete(id);
    }
    /**
     * 保存
     * @param entity
     */
    public void save(FcAccBalanceVO entity){
        FcAccBalance fcAccBalance = BeanUtils.copyNewInstanceProperties(entity, FcAccBalance.class);
        accBalanceService.saveEntity(fcAccBalance);
    }
    /**
     * 更新
     * @param entity
     */
    public void update(FcAccBalanceVO entity){
       FcAccBalance fcAccBalance = BeanUtils.copyNewInstanceProperties(entity, FcAccBalance.class);
        accBalanceService.updateEntity(fcAccBalance);
    }
    /**
     * 保存or更新
     * @param entity
     */
    public void saveOrUpdate(FcAccBalanceVO entity){
        FcAccBalance fcAccBalance = BeanUtils.copyNewInstanceProperties(entity, FcAccBalance.class);
        accBalanceService.saveOrUpdate(fcAccBalance);
    }
    /**
     * 查找某一天的账号对象
     * @param date
     * @param account
     * @return
     */
    public FcAccBalanceVO getEntityByAccountAndDate(Date date, String account){
        FcAccBalance entity= accBalanceService.getEntityByAccountAndDate(date,account);
        FcAccBalanceVO vo=BeanUtils.copyNewInstanceProperties(entity, FcAccBalanceVO.class);
        return vo;
    }
    /**
     * 初始化余额
     * @param balanceDate 余额日期
     * @param blnctrAcc 账号
     * @param accName 户名
     */
    public void initAccountBalance(Date balanceDate, String blnctrAcc, String accName,String curId,String loginName){
        FcAccBalanceVO balance = getEntityByAccountAndDate(balanceDate,blnctrAcc);
        if(balance != null) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_BALANCE_NOT_EXISTS,
                    blnctrAcc, DateUtils.getDateStr(balanceDate,"yyyy-MM-dd"));
        }
        FcAccBalance fcAccBalance = new FcAccBalance();
        fcAccBalance.setBalanceDate(balanceDate);
        fcAccBalance.setAccount(blnctrAcc);
        fcAccBalance.setAccName(accName);
        fcAccBalance.setBalance(BigDecimal.ZERO);
        fcAccBalance.setCnt(0);
        fcAccBalance.setCreditMoney(BigDecimal.ZERO);
        fcAccBalance.setDebitMoney(BigDecimal.ZERO);
        fcAccBalance.setRushCnt(0);
        fcAccBalance.setRushCreditMoney(BigDecimal.ZERO);
        fcAccBalance.setRushDebitMoney(BigDecimal.ZERO);
        fcAccBalance.setTallyDaySign("0");
        fcAccBalance.setTrCnt(0);
        fcAccBalance.setCurId(curId);
        fcAccBalance.setTrCreditMoney(BigDecimal.ZERO);
        fcAccBalance.setTrDebitMoney(BigDecimal.ZERO);
        fcAccBalance.setYesterBalance(BigDecimal.ZERO);
        fcAccBalance.setDebitCnt(0);
        fcAccBalance.setCreditCnt(0);
        fcAccBalance.setCreateTime(DateUtils.getDate());
        fcAccBalance.setCreateBy(loginName);
        accBalanceService.saveOrUpdate(fcAccBalance);
    }
    public void updateAccBalanceByInitAcc(Date date, String account, BigDecimal balance) {
        FcAccBalance fcAccBalance = accBalanceService.getEntityByAccountAndDate(date,account);
        if(fcAccBalance != null) {
            accBalanceService.updateAccBalanceByInitAcc(date, account, fcAccBalance.getBalance().add(balance));
        }
    }
    /**
     * 根据内部账号分组查询笔数
     * @param account
     * @return
     */
    public FcAccBalanceVO getGroupByAccount(String account) {
        return accBalanceService.getGroupByAccount(account);
    }
    /**
     * 按日期分组余额
     * @param dates
     * @return
     */
    public List<FcAccBalanceVO> getGroupByDates(Date[] dates) {
        return accBalanceService.getGroupByDates(dates);
    }

    @Override
    public Map<String, FcAccBalanceVO> getGroupByAccount(List<String> accountList) {
        return accBalanceService.getGroupByAccount(accountList);
    }

    /***
     * 自动同步多级单位内部账号余额
     * @param balanceList
     * @return TODO
     */
    @Override
    public void saveOrUpdateSpecial(List<FcAccBalanceDTO> balanceList) {
        for (FcAccBalanceDTO balance : balanceList){
            // 先检查是否存在当前日期余额
            FcAccBalance fcAccBalance = accBalanceService.getEntityByParams(balance.getBalanceDate(),balance.getAccount(),balance.getCurId());
            if (ObjectUtil.isNotEmpty(fcAccBalance)){
                // 更新余额
                fcAccBalance.setBalance(balance.getBalance());
                fcAccBalance.setUpdateTime(new Date());
                accBalanceService.updateEntity(fcAccBalance);
            }
            // 不存在余额不需要操作
            /*else{
                // 新增余额
                FcAccBalance entity = new FcAccBalance();
                BeanUtils.copyNotNullProperties(balance, entity);
                entity.setCreditMoney(BigDecimal.ZERO);
                entity.setDebitMoney(BigDecimal.ZERO);
                entity.setRushCreditMoney(BigDecimal.ZERO);
                entity.setRushDebitMoney(BigDecimal.ZERO);
                entity.setTallyDaySign("0");
                entity.setTrCreditMoney(BigDecimal.ZERO);
                entity.setTrDebitMoney(BigDecimal.ZERO);
                entity.setDebitCnt(0);
                entity.setCreditCnt(0);
                entity.setCnt(0);
                entity.setRushCnt(0);
                entity.setTrCnt(0);
                entity.setYesterBalance(BigDecimal.ZERO);
                entity.setCreateTime(DateUtils.getDate());
                accBalanceService.save( entity);
            }*/
        }
    }
}
