package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.check.dao.FcBankBillTempDao;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.entity.FcBankBillTemp;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.vo.FcBankBillTempVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
* @Title: Service
* @Description: 银行未达账初始化
* @author onlineGenerator
* @date 2017-06-08 09:16:58
* @version 1.0
*
*/
@Service("fcBankBillTempService")
@Transactional
public class FcBankBillTempService extends BaseService<FcBankBillTemp,String> implements IUserExcelVerifyHandler<FcBankBillTempVo> {
	@Autowired
	private FcBankBillTempDao fcBankBillTempDao;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private FcBankBillService fcBankBillService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcBankBillTemp entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id){
 		super.delete(id);
 	}
	/**
	* 保存
	* @param entity
	*/
 	public void save(FcBankBillTemp entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcBankBillTemp entity){
		super.updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcBankBillTemp entity){
 		super.saveOrUpdate(entity);
 	}
	public BaseDao<FcBankBillTemp, String> getEntityDao() {
		return fcBankBillTempDao;
	}
	
	public void saveImport(List<FcBankBillTempVo> list, ICurrentUser user) {
		if(EmptyUtils.isEmptyList(list)){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_EXCEL_NULL);
		}
		List<FcBankBillTemp> tempList = new ArrayList<FcBankBillTemp>();
		List<FcBankBill> temp = new ArrayList<FcBankBill>();
		String resource = ResourceConstant.FINACIAL_CHECK_FCBANKBILLTEMP_APPLY;
		//获取当前用户有查询权限的银行账号
		//UserDTO byLoginName = userService.getByLoginName(user.getLoginName());
		List<BtBankAccDTO> bankList = accountGrantService.listDTOByMultiVal(user.getId(),resource,null,AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
		Map<String , BtBankAccDTO> accounts = new HashMap<String , BtBankAccDTO>();
		//获取本位币
		BtCurrencyDTO naturnCur = btCurrencyService.getNatualCurrency();
		String natureCurId = "";
		if(!EmptyUtils.isEmpty(naturnCur)){
			natureCurId = naturnCur.getId();
		}
		if(!EmptyUtils.isEmptyList(bankList)){
			for (BtBankAccDTO btBankAccDTO : bankList) {
				accounts.put(btBankAccDTO.getBankAcc() , btBankAccDTO);
			}
		}else{
    		throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_NOT_BANKACC_QUERY_AUTHORITY);
		}
		for (int i = 0; i < list.size(); i++) {
	    	FcBankBillTempVo fc = list.get(i);
	    	int index = i + 1;
	    	String curId = "";
			String the = multiLangServiceI.getLang("common.table.the");//第
			String no = multiLangServiceI.getLang("table.fcReportFormat.orderNo");//行
	    	String lineMsg = the + index + no;
	    	checkDate(fc , lineMsg);
	    	if(!accounts.containsKey(fc.getBankAcc())){
	    		throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_NOT_BANKACC_QUERY_AUTHORITY,fc.getBankAcc()+"");
	    	}
			if(!EmptyUtils.isEmpty(fc.getCurName())){
				curId = btCurrencyService.getCurrencyByName(fc.getCurName());
			}else{
				curId = natureCurId;
			}
			BtBankAccDTO dto = accounts.get(fc.getBankAcc());
			FcBankBill fcBankBill = new FcBankBill();
			String red = multiLangServiceI.getLang("table.btBankAccDtl.rbSign.red");//红
			String redBlueSign = fc.getRedblueSign() == null ? red : fc.getRedblueSign();
			String string = dictService.getCodeByName("Finacial.RedBlueSign", redBlueSign);
			BeanUtils.copyNotNullProperties(fc, fcBankBill);
			fcBankBill.setImportDate(new Date());
			fcBankBill.setImportUser(user.getId());
			fcBankBill.setCurId(curId);
			fcBankBill.setBankAccId(dto.getId());
			fcBankBill.setBankTypeId(dto.getBankTypeId());
			fcBankBill.setBankId(fcBankBillService.getBankId("11111"));
			fcBankBill.setImportType(FinacialConstant.IMPORT_TYPE_FILE_UNSTAY);
			fcBankBill.setCompareSign(FinacialConstant.COMPARE_SIGN_STAY_UP);
			fcBankBill.setRedblueSign(string);
			fcBankBill.setAccount(fc.getAccount());
			temp.add(fcBankBill);
			
			FcBankBillTemp fb = new FcBankBillTemp();
			BeanUtils.copyNotNullProperties(fc, fb);
			fb.setImportType(FinacialConstant.IMPORT_TYPE_FILE_UNSTAY);
			fb.setRedblueSign(string);
			fb.setCompareSign(FinacialConstant.COMPARE_SIGN_STAY);
			tempList.add(fb);
		}
		this.doSaveCompareStayBank(temp,tempList);
	}
	
	private void checkDate(FcBankBillTempVo fbb  , String lineMsg) {
 		
		if(EmptyUtils.isEmpty(fbb.getBankAcc())){
			//银行账号不能为空
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_NETBANK_NULL ,lineMsg);
		}
		/*if(EmptyUtils.isEmpty(fbb.getAccount())){
			//内部账号/erp企业科目不能为空不能为空
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_ACCOUNT_EMPTY ,lineMsg);
		}*/
		if(EmptyUtils.isEmpty(fbb.getTallyDate())){
			//记账日期不能为空
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_TALLYDATE_NULL , lineMsg);
		}
		if(EmptyUtils.isEmpty(fbb.getDebitMoney())){
			//借方发生额不能为空
			fbb.setDebitMoney(BigDecimal.ZERO);
		}
		if(EmptyUtils.isEmpty(fbb.getCreditMoney())){
			//贷方发生额不能为空
			fbb.setCreditMoney(BigDecimal.ZERO);
		}
		if(fbb.getDebitMoney().compareTo(BigDecimal.ZERO) == 0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO)== 0){
			//借贷方不能都为0
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_DE_CR_ZERO , lineMsg);
		}
		if(fbb.getDebitMoney().compareTo(BigDecimal.ZERO)>0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO)>0){
			//借贷方不能都不为0
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILL_DE_CR_NOT_ZERO , lineMsg);
		}
	}
	/**
	 * 未达账初始化保存
	 */
	private void doSaveCompareStayBank(List<FcBankBill> temp,List<FcBankBillTemp> tempList) {
		//清空临时表数据
		fcBankBillTempDao.deleteTemp();
		this.batchSave(tempList);
		fcBankBillService.batchSave(temp);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FcBankBillTempVo fcBankBillTempVo, Object... objects) {
		return null;
	}
	@Override
	public void verifyHandler(List<FcBankBillTempVo> list, Object... objects) {
		String userId = "";
		if(objects instanceof Object[]) {
			Object[] arr = (Object[]) objects;
			ICurrentUser user = (ICurrentUser)arr[0];
			userId = user.getId();
		}
		String resource = ResourceConstant.FINACIAL_CHECK_FCBANKBILLTEMP_APPLY;
		List<BtBankAccDTO> bankList = accountGrantService.listDTOByMultiVal(userId,resource,null,AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
		for (int i=0;i<list.size();i++) {
			FcBankBillTempVo fbb = list.get(i);
			List<String> msgList = new ArrayList<>();
			if(EmptyUtils.isEmpty(fbb.getBankAcc())){
				//银行账号不能为空
				msgList.add("fin.check.fcbankbilltemp.netbank.null");
			}
			/*if(EmptyUtils.isEmpty(fbb.getAccount())){
				//内部账号/erp企业科目不能为空不能为空
				msgList.add("fin.check.centraliydatamanager.account.empty");
			}*/
			if(EmptyUtils.isEmpty(fbb.getTallyDate())){
				//记账日期不能为空
				msgList.add("fin.check.fcbankbill.tallydate.null");
			}
			if(EmptyUtils.isEmpty(fbb.getDebitMoney())){
				//借方发生额不能为空
				fbb.setDebitMoney(BigDecimal.ZERO);
			}
			if(EmptyUtils.isEmpty(fbb.getCreditMoney())){
				//贷方发生额不能为空
				fbb.setCreditMoney(BigDecimal.ZERO);
			}
			if(fbb.getDebitMoney().compareTo(BigDecimal.ZERO) == 0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO)== 0){
				//借贷方不能都为0
				msgList.add("fin.check.fcbankbill.de.cr.zero");
			}
			if(fbb.getDebitMoney().compareTo(BigDecimal.ZERO)>0 && fbb.getCreditMoney().compareTo(BigDecimal.ZERO)>0){
				//借贷方不能都不为0
				msgList.add("fin.check.fcbankbill.de.cr.not.zero");
			}
			Map<String , BtBankAccDTO> accounts = new HashMap<String , BtBankAccDTO>();
			if(!EmptyUtils.isEmptyList(bankList)){
				for (BtBankAccDTO btBankAccDTO : bankList) {
					accounts.put(btBankAccDTO.getBankAcc() , btBankAccDTO);
				}
			}else{
				msgList.add("fin.check.fcbankbill.not.account.query.authority");
			}
			if (!accounts.containsKey(fbb.getBankAcc())) {
				msgList.add("fin.check.fcbankbill.not.account.query.authority");
			}
			list.get(i).addErrorMsg(msgList);
		}
	}
}
