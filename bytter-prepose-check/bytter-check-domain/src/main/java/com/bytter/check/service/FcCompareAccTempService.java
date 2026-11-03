package com.bytter.check.service;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.check.constant.FinacialConstant;
import com.bytter.check.dao.FcBankBillTempDao;
import com.bytter.check.dao.FcCompareAccTempDao;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.entity.FcCompareAccTemp;
import com.bytter.check.enums.CheckMessageEnum;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.check.vo.FcCompareAccTempVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Service("fcCompareAccTempService")
@Transactional
public class FcCompareAccTempService extends BaseService<FcCompareAccTemp,String> implements IUserExcelVerifyHandler<FcCompareAccTempVo> {
	@Autowired
	private FcBankBillTempDao fcBankBillTempDao;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private FcCompareAccService fcCompareAccService;
	@Autowired
	private FcCompareAccTempDao fcCompareAccTempDao;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	/**
	* 删除对象
	* @param entity
	*/
	public void deleteEntity(FcCompareAccTemp entity){
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
 	public void save(FcCompareAccTemp entity){
 		super.saveEntity(entity);
 	}
	/**
	* 更新
	* @param entity
	*/
	public void update(FcCompareAccTemp entity){
		super.updateEntity(entity);
	}
	@Override
	public BaseDao<FcCompareAccTemp, String> getEntityDao() {
		return fcCompareAccTempDao;
	}
	/**
	* 保存or更新
	* @param entity
	*/
 	public void saveOrUpdate(FcCompareAccTemp entity){
 		super.saveOrUpdate(entity);
 	}
 	
 	/**
 	 *erp未达账初始化上传
 	 */
 	public void uploadFcCompareErpTemp(List<FcCompareAccTempVo> list){
 		if(EmptyUtils.isEmptyList(list)){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_EXCEL_NULL);
		}
	    List<FcCompareAccTemp> tempList = new ArrayList<FcCompareAccTemp>();
	    List<FcCompareAcc> temp = new ArrayList<FcCompareAcc>();
        List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
        Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<String, BtCurrencyDTO>();
        for(BtCurrencyDTO currencyDTO : currencyDTOList) {
            currencyDTOMap.put(currencyDTO.getCurName(), currencyDTO);
        }
	    for (int i = 0; i < list.size(); i++) {
	    	FcCompareAccTempVo fc = list.get(i);
	    	int index = i + 1;
	    	//记账日期
			if(EmptyUtils.isEmpty(fc.getTallyDate())){
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_TALLYDATE_NULL,index+"");   		
			}
			
			//借方金额
			if(EmptyUtils.isEmpty(fc.getDebitMoney())){
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_DEBITMONEY_NULL,index+"");
			}
			//贷方金额
			if(EmptyUtils.isEmpty(fc.getCreditMoney())){
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_CREDITMONEY_NULL,index+"");
			}
			//凭证号
			if(EmptyUtils.isEmpty(fc.getVoucherNo())){
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_VOUCHERNO_NULL,index+"");
			}
			//数据来源
			if(EmptyUtils.isEmpty(fc.getDataSource())){
				throw new ParameterException(CheckMessageEnum.FIN_CHECK_FCBANKBILLTEMP_DATASOURCE_NULL,index+"");
			}
			//红蓝标志
			String string = dictService.getCodeByName("Finacial.RedBlueSign", fc.getRedblueSign());
			if(EmptyUtils.isEmpty(string)){
				string = FinacialConstant.RB_SIGN_B;
			}
			String curId = "";
			String curName = fc.getCurName();//币别
            BtCurrencyDTO currencyDTO = currencyDTOMap.get(curName);
            if (null == currencyDTO) {
                throw new ParameterException(DataExchangeMessageEnum.DATAEXCHANGE_MESSAGE_CURRENT_NOTEXISTS, curName);
            }
            curId = currencyDTO.getId();
			FcCompareAccTemp fb = new FcCompareAccTemp();
			BeanUtils.copyNotNullProperties(fc, fb);
			fb.setImportType(FinacialConstant.IMPORT_TYPE_FILE_UNSTAY);
			//TODO 设置流水号OrderNo   序号BankId  起息日期 InterestStartDate
			fb.setOrderNo("11gg54g5tg54ru564w3");
            fb.setCurCode(curId);
			fb.setBankId(SequenceSingleton.getSequence(SequenceEnum.FC_BANK_BILL_BANK_ID, "111111"));
			fb.setInterestStartDate(new Date());
			fb.setCheckType(FinacialConstant.CHECK_TYPE_ERP);
			fb.setCompareSign(FinacialConstant.COMPARE_SIGN_STAY_UP);
			fb.setRedblueSign(string);
			fb.setLineNo(1111);
			tempList.add(fb);
			FcCompareAcc fca = new FcCompareAcc();
			BeanUtils.copyNotNullProperties(fb,fca);
			fca.setCurId(curId);
			fca.setLineNo(BigDecimal.valueOf(1111));
			temp.add(fca);
		}
	    this.saveErpImport(temp,tempList);
 	}
	public void saveErpImport(List<FcCompareAcc> temp,List<FcCompareAccTemp> tempList) {
		fcCompareAccTempDao.deleteFcCompareAccTemp();
		fcCompareAccService.batchSave(temp);
		fcCompareAccTempDao.batchSave(tempList);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(FcCompareAccTempVo fcBankBillTempVo, Object... objects) {
		return null;
	}
	@Override
	public void verifyHandler(List<FcCompareAccTempVo> list, Object... objects) {
		String userId = "";
		if(objects instanceof Object[]) {
			Object[] arr = (Object[]) objects;
			ICurrentUser user = (ICurrentUser)arr[0];
			userId = user.getId();
		}
        List<BtCurrencyDTO> currencyDTOList = btCurrencyService.getBtCurrency();
        Map<String, BtCurrencyDTO> currencyDTOMap = new HashMap<String, BtCurrencyDTO>();
        for(BtCurrencyDTO currencyDTO : currencyDTOList) {
            currencyDTOMap.put(currencyDTO.getCurName(), currencyDTO);
        }
		String resource = ResourceConstant.FINACIAL_CHECK_FCCOMPAREACCTEMP_APPLY;
		List<BtBankAccDTO> bankList = accountGrantService.listDTOByMultiVal(userId,resource,null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
		for (int i=0;i<list.size();i++) {
			FcCompareAccTempVo fbb = list.get(i);
			List<String> msgList = new ArrayList<>();
			if(EmptyUtils.isEmpty(fbb.getCompAcc())){
				//银行账号不能为空
				msgList.add("fin.check.fcbankbilltemp.netbank.null");
			}
            String curName = fbb.getCurName();
            BtCurrencyDTO currencyDTO = currencyDTOMap.get(curName);
            if (EmptyUtils.isEmpty(currencyDTO)) {
                msgList.add("dataexchange.message.current.notexists");
            }
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
			if (!accounts.containsKey(fbb.getCompAcc())) {
				msgList.add("fin.check.fcbankbill.not.account.query.authority");
			}
			list.get(i).addErrorMsg(msgList);
		}
	}
}
