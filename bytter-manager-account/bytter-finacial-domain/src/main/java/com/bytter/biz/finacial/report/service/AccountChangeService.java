package com.bytter.biz.finacial.report.service;

import com.bytter.biz.finacial.report.dao.AccountChangeDao;
import com.bytter.biz.finacial.report.vo.AccountChangeVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("accountChangeService")
@Transactional
public class AccountChangeService  extends  BaseService<AccountChangeVo,String> implements DictInjectProvider{
	
	@Autowired
	private AccountChangeDao accountChangeDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IInnerAccountService innerAccountService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyServiceImpl;
	@DubboReference(check = false)
	private IDictService dictService;
	
	public List<AccountChangeVo> listAll(AccountChangeVo vo,String resource,String userId){
		List<String> corpAuth = corpService.getUserCorpIds(resource ,userId);	
		List<AccountChangeVo> list =  judgeAccount(vo,corpAuth);
		List<AccountChangeVo> res = new ArrayList<AccountChangeVo>();
		AccountChangeVo accountVo = null;
		BigDecimal sumStartMoney = BigDecimal.ZERO;
		BigDecimal sumDebitMoney = BigDecimal.ZERO;
		BigDecimal sumCreditMoney = BigDecimal.ZERO;
		BigDecimal sumEndMoney = BigDecimal.ZERO;
		String netCode = "";
		String curCode = "";
		String subCode = "";
		String subName = "";
		String curName = "";
		String kg = "_";
		
		for (AccountChangeVo accountChangeVo : list) {
			//String curId = btCurrencyServiceImpl.getCurrencyByName(curName);
			if(!EmptyUtils.isEmpty(subCode) && !subCode.equals(accountChangeVo.getSubjectId())){//科目不相同
				accountVo = new AccountChangeVo();
				accountVo.setAccount("合计");
				accountVo.setAccName(subName);
				accountVo.setBalanceName(dictService.get("balance.query.report",0));//空
				accountVo.setYesterBalance(sumStartMoney);
				accountVo.setDebitMoney(sumDebitMoney);
				accountVo.setCreditMoney(sumCreditMoney);
				accountVo.setBalance(sumEndMoney);
				res.add(accountVo);
				sumStartMoney = BigDecimal.ZERO;
				sumDebitMoney = BigDecimal.ZERO;
				sumCreditMoney = BigDecimal.ZERO;
				sumEndMoney = BigDecimal.ZERO;
			}
			
			if(EmptyUtils.isEmpty(curName) || !curName.equals(accountChangeVo.getCurName())){//币别不相同
				curName = accountChangeVo.getCurName();
				String maxDate = null;
				if(vo.getGe_balanceDate() != null){
					Date maxDate1 = vo.getGe_balanceDate();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					maxDate = sdf.format(maxDate1);
				}
				String minDate = null;
				if(vo.getLe_balanceDate() != null){
					Date minDate1 = vo.getLe_balanceDate();
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					minDate = sdf1.format(minDate1);
				}
				AccountChangeVo accountVo1 = new AccountChangeVo();
				accountVo1.setAccount("币别:"+curName+kg+"日期:"+minDate+"到"+maxDate);
				accountVo1.setBalanceName(dictService.get("balance.query.report",0));//空
				res.add(accountVo1);
			}		
			
			sumStartMoney = sumStartMoney.add(accountChangeVo.getYesterBalance());//期初
			sumDebitMoney = sumDebitMoney.add(accountChangeVo.getDebitMoney());
			sumCreditMoney = sumCreditMoney.add(accountChangeVo.getCreditMoney());
			sumEndMoney = sumEndMoney.add(accountChangeVo.getBalance());//期末
			
			netCode = accountChangeVo.getNetId();
			curCode = accountChangeVo.getCurId();
			curName = accountChangeVo.getCurName();
			subCode = accountChangeVo.getSubjectId();
			subName = accountChangeVo.getSubjectName();	
			res.add(accountChangeVo);		
		}
		accountVo = new AccountChangeVo();
		accountVo.setAccount("合计");
		accountVo.setAccName(subName);
		accountVo.setBalanceName(dictService.get("balance.query.report",0));
		accountVo.setYesterBalance(sumStartMoney);
		accountVo.setDebitMoney(sumDebitMoney);
		accountVo.setCreditMoney(sumCreditMoney);
		accountVo.setBalance(sumEndMoney);
		res.add(accountVo);
		return res;	
			
	}
		
		
	public List<AccountChangeVo> judgeAccount(AccountChangeVo vo,List<String> corpAuth){
		List<AccountChangeVo> list =  accountChangeDao.listAll(vo,corpAuth);
		List<AccountChangeVo> res = new ArrayList<AccountChangeVo>();
		AccountChangeVo accountVo = null;
		
		String account = "";
		String accName = "";
		Integer balanceWay = 0;
		BigDecimal yesterBalance = BigDecimal.ZERO;
		BigDecimal balance = BigDecimal.ZERO;
		BigDecimal creditMoney = BigDecimal.ZERO;
		BigDecimal debitMoney = BigDecimal.ZERO;
		String subjectId = "";
		String curId = "";
		String subjectName = "";
		String curName = "";
		
		int count = 0;
		//Iterator<AccountChangeVo> iter =  list.iterator();
		//while(iter.hasNext()){
		for(int i = 0; i < list.size(); i++){
			AccountChangeVo acv = list.get(i);	
		
			//判断账号是否相同
			if(acv.getAccount().equals(account)){//账号相同
				count ++;
			}else{
				if(count == 1){//上个账号 只存在一条数据
					//将上个账号的 期末至取出
					AccountChangeVo upAcv = list.get(i - 1);//上个账号信息
					AccountChangeVo acvVo = res.get(res.size()-1);
					acvVo.setBalance(upAcv.getBalance());	
				}
				count = 1;
			}
		
			if(count == 1){//第一次相等
				accountVo = new AccountChangeVo();
				account = acv.getAccount();
				accName = acv.getAccName();
				balanceWay = acv.getBalanceWay();
				yesterBalance = acv.getYesterBalance();
				creditMoney = acv.getCreditMoney();
				debitMoney = acv.getDebitMoney();
				subjectId = acv.getSubjectId();
				curId = acv.getCurId();
				subjectName = acv.getSubjectName();
				curName = acv.getCurName();
				accountVo.setAccount(account);
				accountVo.setAccName(accName);
				accountVo.setBalanceName(dictService.get("balance.query.report", balanceWay));
				accountVo.setYesterBalance(yesterBalance);//期初
				accountVo.setBalance(balance);
				accountVo.setCreditMoney(creditMoney);
				accountVo.setDebitMoney(debitMoney);
				accountVo.setSubjectId(subjectId);
				accountVo.setCurId(curId);
				accountVo.setSubjectName(subjectName);
				accountVo.setCurName(curName);
				res.add(accountVo);
				if(list.size()>0 && (i + 1) == list.size() && count == 1){//是最后一个元素 && 上个账号只存在一条数据
					//取当前账号的余额作为期末
					AccountChangeVo upAcv = list.get(i);//当前账号信息
					AccountChangeVo acvVo = res.get(res.size()-1);
					acvVo.setBalance(upAcv.getBalance());//设置期末
				}
			}else{//第二次
				AccountChangeVo acvVo = res.get(res.size()-1);
				acvVo.setBalance(acv.getBalance());//设置期末
			}
			account = acv.getAccount();
		}
		return res;
	}
	
	
	public List<FcAccDTO> queryListAccount(String netId, String corpId){
		return	innerAccountService.listAccountByParams(null, netId, corpId, null);
	}	
	
	@Override
	public BaseDao<AccountChangeVo, String> getEntityDao() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public Map<String, Map<String, DictInDTO>> listDictMap(List<String> dictGroups) {
        return dictService.listDictMap(dictGroups);
    }
}
