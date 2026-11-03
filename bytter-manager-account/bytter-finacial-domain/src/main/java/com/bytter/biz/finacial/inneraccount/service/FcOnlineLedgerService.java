package com.bytter.biz.finacial.inneraccount.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.vo.FcAccVO;
import com.bytter.biz.finacial.inneraccount.dao.FcOnlineLedgerDao;
import com.bytter.biz.finacial.inneraccount.entity.FcOnlineLedger;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.ISystemForeignService;
import com.bytter.system.api.dto.FcAccCorpCurrencyDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_ONLINE_LEDGER
 * @date 2017-06-07 10:03:54
 */
@Service("fcOnlineLedgerService")
@Transactional
public class FcOnlineLedgerService extends BaseService<FcOnlineLedger, String> {
    @Autowired
    private FcOnlineLedgerDao fcOnlineLedgerDao;
    @DubboReference(check = false)
    private ISystemForeignService iSystemForeignService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    /**
     * 删除对象
     *
     * @param entity
     */
    @Override
    public void deleteEntity(FcOnlineLedger entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(FcOnlineLedger entity) {
        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
        entity.setAccName(fcAccDTO.getAccName());
        entity.setAccForDeduct(fcAccDTO.getAccForDeduct());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(FcOnlineLedger entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    @Override
    public void saveOrUpdate(FcOnlineLedger entity) {
        FcAccDTO fcAccDTO = iFcAccService.getAccDTOByAccount(entity.getAccount());
        entity.setAccName(fcAccDTO.getAccName());
        entity.setAccForDeduct(fcAccDTO.getAccForDeduct());
        super.saveOrUpdate(entity);
    }
    @Override
    public BaseDao<FcOnlineLedger, String> getEntityDao() {
        return fcOnlineLedgerDao;
    }
    /**
     * 初始化联机余额（含表外）
     *
     * @param acc
     */
    public void insertFclOnlineLedger(FcAccVO acc, String loginName) {
        FcOnlineLedger fcOnlineLedger = this.getEntityByAccount(acc.getBlnctrAcc());
        if (EmptyUtils.isEmpty(fcOnlineLedger)) {
            fcOnlineLedger = new FcOnlineLedger();
        }
        fcOnlineLedger.setAccount(acc.getBlnctrAcc());
        fcOnlineLedger.setAccName(acc.getAccName());
        fcOnlineLedger.setAccForDeduct(acc.getAccName());
        fcOnlineLedger.setCheckBalance(BigDecimal.ZERO);
        fcOnlineLedger.setCheckCnt(0);
        fcOnlineLedger.setFreezeMoney(BigDecimal.ZERO);
        fcOnlineLedger.setOnlineBalance(BigDecimal.ZERO);
        fcOnlineLedger.setTallyCnt(0);
        fcOnlineLedger.setCurCode(acc.getCurId());
        fcOnlineLedger.setCreateTime(DateUtils.getDate());
        fcOnlineLedger.setCreateBy(loginName);
        super.saveOrUpdate(fcOnlineLedger);
    }
    /**
     * 根据账号找对象
     *
     * @param account
     * @return
     */
    public FcOnlineLedger getEntityByAccount(String account) {
        return fcOnlineLedgerDao.getEntityByAccount(account);
    }
    /**
     * 账户初始化更新账户余额
     *
     * @param account
     * @param balance
     */
    public void updateOnlineBalanceByInitAcc(String account, BigDecimal balance) {
        FcOnlineLedger onlineLedger = getEntityByAccount(account);
        BigDecimal onlineBalance = onlineLedger.getOnlineBalance();
        balance = balance.add(onlineBalance);
        fcOnlineLedgerDao.updateAccBal(account, balance);
    }
    /**
     * 更新冻结累计金额
     *
     * @param onlineLedger
     * @param money
     * @param isFreeze     冻结或解冻 true:冻结 false：解冻
     */
    public void updateFreezeMoney(FcOnlineLedger onlineLedger, BigDecimal money, boolean isFreeze, String loginName) {
        BigDecimal freezeMoney = onlineLedger.getFreezeMoney();
        if (isFreeze) {
            freezeMoney = freezeMoney.add(money);
        } else {
            freezeMoney = freezeMoney.subtract(money);
        }
        fcOnlineLedgerDao.updateFreezeMoney(freezeMoney, DateUtils.getDate(), loginName, onlineLedger.getAccount());
    }
    /**
     * 更新冻结累计金额
     *
     * @param account
     * @param money
     * @param isFreeze 冻结或解冻 true:冻结 false：解冻
     */
    public void updateFreezeMoney(String account, BigDecimal money, boolean isFreeze) {
        FcOnlineLedger onlineLedger = fcOnlineLedgerDao.getUniqueBy("account", account);
        BigDecimal freezeMoney = onlineLedger.getFreezeMoney();
        if (isFreeze) {
            freezeMoney = freezeMoney.add(money);
        } else {
            freezeMoney = freezeMoney.subtract(money);
        }
        fcOnlineLedgerDao.updateFreezeMoney(account, freezeMoney, onlineLedger.getFreezeMoney());
    }
    /**
     * 功能介绍 : 根据账号查询账户联机余额。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcOnlineLedgerDTO> getLedgerDTOsByAccSet(List<String> accList) {
        List<FcAccCorpCurrencyDTO> fcAccCorpCurrencyDTOS = iSystemForeignService.getAccCorpCurDataByAccounts(accList);
        Map<String, FcAccCorpCurrencyDTO> map = fcAccCorpCurrencyDTOS.stream().collect(Collectors.toMap(FcAccCorpCurrencyDTO::getBlnctrAcc, Function.identity()));
        // sql.append(" select a.blnctr_acc as blnctrAcc, a.cur_id as curId, a.overdraft_sign as overdraftSign, a.overdraft_money as overdraftMoney, a.balance_way as balanceWay, " );
        // sql.append(" a.net_id as netId, a.acc_type as accType, a.charge_acc as chargeAcc, a.subject_id as subjectId, b.code as netCode, c.cur_code as curCode from fc_acc a ");
        List<FcOnlineLedgerDTO> dtos = fcOnlineLedgerDao.getLedgerDTOsByAccSet(accList);
        if(MapUtils.isEmpty(map) || CollectionUtils.isEmpty(dtos)) {
        	return null;
        }
        dtos.forEach(a -> {
            FcAccCorpCurrencyDTO fcAccCorpCurrencyDTO = map.get(a.getAccount());
            a.setCurId(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getCurId()) ? "" : fcAccCorpCurrencyDTO.getCurId());
            a.setOverdraftSign(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getOverdraftSign()) ? "" : fcAccCorpCurrencyDTO.getOverdraftSign());
            a.setOverdraftMoney(fcAccCorpCurrencyDTO.getOverdraftMoney()!=null ? fcAccCorpCurrencyDTO.getOverdraftMoney() : new BigDecimal(0));
            a.setBalanceWay(fcAccCorpCurrencyDTO.getBalanceWay()!=null ? fcAccCorpCurrencyDTO.getBalanceWay() : 0);
            a.setNetCode(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getNetCode()) ? "" : fcAccCorpCurrencyDTO.getNetCode());
            a.setAccType(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getAccType()) ? "" : fcAccCorpCurrencyDTO.getAccType());
            a.setChargeAcc(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getChargeAcc()) ? "" : fcAccCorpCurrencyDTO.getChargeAcc());
            a.setSubjectCode(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getSubjectId()) ? "" : fcAccCorpCurrencyDTO.getSubjectId());
            a.setNetId(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getNetId()) ? "" : fcAccCorpCurrencyDTO.getNetId());
            a.setCurCode(StringUtils.isEmpty(fcAccCorpCurrencyDTO.getCurCode()) ? "" : fcAccCorpCurrencyDTO.getCurCode());
        });
        return dtos;
    }
    /**
     * 功能介绍 : 根据账号查询账户联机余额--资金池。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcOnlineLedgerDTO> getPoolLedgerDTOsByAccSet(List<String> accList) {
        List<FcOnlineLedgerDTO> fcOnlineLedgerDTOS = fcOnlineLedgerDao.getPoolLedgerDTOsByAccSet(accList);
        List<FcAccCorpCurrencyDTO> fcAccCorpCurrencyDTOS = iFcAccService.getAccCorpPoolCurDataByAccounts(accList);
        Map<String, FcAccCorpCurrencyDTO> map = fcAccCorpCurrencyDTOS.stream().collect(Collectors.toMap(FcAccCorpCurrencyDTO::getBlnctrAcc, Function.identity()));
        fcOnlineLedgerDTOS.forEach(a -> {
            FcAccCorpCurrencyDTO fcAccCorpCurrencyDTO = map.get(a.getAccount());
            a.setCurId(fcAccCorpCurrencyDTO.getCurId());
            a.setOverdraftSign(fcAccCorpCurrencyDTO.getOverdraftSign());
            a.setOverdraftMoney(fcAccCorpCurrencyDTO.getOverdraftMoney());
            a.setBalanceWay(fcAccCorpCurrencyDTO.getBalanceWay());
            a.setNetCode(fcAccCorpCurrencyDTO.getNetCode());
            a.setAccType(fcAccCorpCurrencyDTO.getAccType());
            a.setChargeAcc(fcAccCorpCurrencyDTO.getChargeAcc());
            a.setSubjectCode(fcAccCorpCurrencyDTO.getSubjectId());
            a.setNetCode(fcAccCorpCurrencyDTO.getNetCode());
            a.setCurCode(fcAccCorpCurrencyDTO.getCurCode());
        });
        return fcOnlineLedgerDTOS;
    }
    /**
     * 功能介绍 : 根据账号查询账户联机余额--资金池。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcOnlineLedgerDTO> getFcOnlineLedgerByAccList(List<String> accList) {
        List<FcOnlineLedgerDTO> fcOnlineLedgerDTOS = fcOnlineLedgerDao.getLedgerDTOsByAccSet(accList);
        return fcOnlineLedgerDTOS;
    }
    /**
     * 功能介绍 : 更新账户记账余额信息。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs) {
        for (FcOnlineLedgerDTO ledgerDTO : ledgerDTOs) {
            fcOnlineLedgerDao.updateOnlineBalance(ledgerDTO);
        }
    }
    /**
     * @param ledgerDTOs
     * @param origLedgerDTOs
     */
    public void updateOnlineBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs, List<FcOnlineLedgerDTO> origLedgerDTOs) {
        for (FcOnlineLedgerDTO ledgerDTO : ledgerDTOs) {
            String account = ledgerDTO.getAccount();
            if (1 != fcOnlineLedgerDao.updateAccOnlineBalance(account, ledgerDTO.getBookMoney(), ledgerDTO.getBookCnt())) {//联机余额更新失败
                throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_ONLINE_BALANCE, ledgerDTO.getAccount());
            }
        }
    }
    /**
     * 功能介绍 : 更新账户复核余额信息。<br>
     * <b>日期：</b>2017年6月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateCheckBalanceList(List<FcOnlineLedgerDTO> ledgerDTOs) {
        for (FcOnlineLedgerDTO ledgerDTO : ledgerDTOs) {
            //fcOnlineLedgerDao.updateCheckBalance(ledgerDTO);
            fcOnlineLedgerDao.updateAccCheckBalance(ledgerDTO.getAccount(), ledgerDTO.getBookMoney(), ledgerDTO.getBookCnt());
        }
    }
    public FcOnlineLedgerDTO getByBlnctrAcc(String blnctrAcc) {
        List<String> accList = new ArrayList<String>();
        accList.add(blnctrAcc);
        List<FcOnlineLedgerDTO> list = this.getLedgerDTOsByAccSet(accList);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }

    public List<FcOnlineLedgerDTO> getByBlnctrAccByList(List<String> blnctrAcc) {
        List<FcOnlineLedgerDTO> list = this.getLedgerDTOsByAccSet(blnctrAcc);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list;
    }
    public List<FcOnlineLedgerDTO> getBalanceByAcc(List<String> accList) {
        return fcOnlineLedgerDao.getBalanceByAcc(accList);
    }

    public Map<String, FcOnlineLedgerDTO> getBalanceByAccMap(List<String> accList) {
        if (CollectionUtil.isEmpty(accList)) {
            return Maps.newHashMap();
        }
        String sql = "select * from fc_online_ledger where  account in (:accList)";
        Map<String, Object> param = new HashMap<>();
        param.put("accList", accList);
        List<FcOnlineLedgerDTO> fcOnlineLedgerDTOS = fcOnlineLedgerDao.listBySQLAliasToBean(sql, param, FcOnlineLedgerDTO.class);
        return fcOnlineLedgerDTOS.stream().collect(Collectors.toMap(FcOnlineLedgerDTO::getAccount, Function.identity(), (pre, next) -> next));
    }

    public FcOnlineLedgerDTO getByBlnctrAcc(String payeeCounterAcc, String poolSign) {
        List<String> accList = new ArrayList<String>();
        accList.add(payeeCounterAcc);
        List<FcOnlineLedgerDTO> list;
        if (CommonConstant.YES.equals(poolSign)) {
            list = this.getPoolLedgerDTOsByAccSet(accList);
        }else {
            list = this.getLedgerDTOsByAccSet(accList);
        }
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    public void updateBalance(List<FcOnlineLedgerDTO> balanceList) {
        for (FcOnlineLedgerDTO balance : balanceList){
            fcOnlineLedgerDao.updateBalance(balance);
        }
    }
}
