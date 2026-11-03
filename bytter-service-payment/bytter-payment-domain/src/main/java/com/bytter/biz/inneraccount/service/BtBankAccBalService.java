package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.inneraccount.dao.BtBankAccBalDao;
import com.bytter.biz.inneraccount.entity.BtBankAccBal;
import com.bytter.biz.inneraccount.vo.BtBankAccBalVO;
import com.bytter.biz.payment.api.dto.BtBankAccBalDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC_BAL
 * @date 2022-05-25 17:50:16
 */
@Service("btBankAccBalService")
@Transactional
public class BtBankAccBalService extends BaseService<BtBankAccBal, String> implements ITaskService {
    @Autowired
    private BtBankAccBalDao btBankAccBalDao;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Override
    public BaseDao<BtBankAccBal, String> getEntityDao() {
        return btBankAccBalDao;
    }
    @Override
    @XxlJob("BtBankAccBalService")
    public void run(String arguments) {
        List<BtBankAccBal> listBankAccBal = this.listAll();
        //初次跑批，初始化所有银行账号的最新余额
        if(listBankAccBal.isEmpty()){
            List<BtBankAccBal> balList = btBankAccBalDao.getBankAccBalInit(null);
            List<BtBankAccBal> initList = new ArrayList<>();
            HashMap<String, BtBankAccBal> balMap = new HashMap<>();
            for(BtBankAccBal bal:balList){
                String key = bal.getBankAcc()+bal.getCurId()+ DateUtils.format(bal.getBalDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                if(!balMap.containsKey(key)){
                    initList.add(bal);
                    balMap.put(key, bal);
                }
            }
            btBankAccBalDao.batchSave(initList);
        } else {
            //获取更新过余额的账号，更新账户余额
            List<BtBankAccBalVO> updateBalVOList = btBankAccBalDao.getBankAccBalMax();
            if(!EmptyUtils.isEmpty(updateBalVOList)) {
                for(BtBankAccBalVO balVO:updateBalVOList) {
                    BtBankAccBal bal = new BtBankAccBal();
                    BeanUtils.copyNotNullProperties(balVO, bal);
                    this.getSession().merge(bal);
                }
            }
            //获取待初始化的账号
            List<String> bankAccList = btBankAccBalDao.getBankAccInit();
            String joinSql = SqlUtil.getInSql(bankAccList, "b.bank_acc", true);
            List<BtBankAccBal> balList = btBankAccBalDao.getBankAccBalInit(joinSql);
            btBankAccBalDao.batchSave(balList);
        }
    }

    public void initBankAccBal() {
        List<BtBankAccBal> listBankAccBal = this.listAll();
        //初次跑批，初始化所有银行账号的最新余额
        if(listBankAccBal.isEmpty()) {
            List<BtBankAccBal> balList = btBankAccBalDao.getBankAccBalInit(null);
            List<BtBankAccBal> initList = new ArrayList<>();
            HashMap<String, BtBankAccBal> balMap = new HashMap<>();
            for(BtBankAccBal bal:balList){
                String key = bal.getBankAcc()+bal.getCurId()+ DateUtils.format(bal.getBalDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
                if(!balMap.containsKey(key)){
                    initList.add(bal);
                    balMap.put(key, bal);
                }
            }
            btBankAccBalDao.batchSave(initList);
        }
    }

	public void batchSaveByDTO(List<BtBankAccBalDTO> balList) {
		List<BtBankAccBal> list = BeanUtils.copyListProperties(balList, BtBankAccBal.class);
		btBankAccBalDao.batchSave(list);
		
	}

	public List<AccountDTO> getAccountInfo(AccountSearchDTO accountSearchDTO){
        List<AccountDTO> list = accountGrantService.listBy(accountSearchDTO);
        for(AccountDTO accountDTO : list){
            BisAccBalDTO bal = bisAccBalService.getBisAccBalBy(accountDTO.getBankAcc(), accountDTO.getCurId(), null, true);
            if(bal != null){
                accountDTO.setBalance(bal.getBal());
            }else{
                accountDTO.setBalance(BigDecimal.ZERO);
            }
        }
        return list;

    }
    public Map<String, BigDecimal> getBalByAccAndCur(String bankAcc, String cur){
        return btBankAccBalDao.getBalByAccAndCur(bankAcc,cur);
    }


    public Integer selectNotInitBalCount(String bankAcc, String accCurId) {
        return btBankAccBalDao.selectNotInitBalCount(bankAcc,accCurId);
    }
}
