package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.dao.AbBillBankAutoDtlDao;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.vo.AbBillBankAutoDtlVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BisBifInitDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * @Title: Service
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Service("abBillBankAutoDtlService")
@Transactional
public class AbBillBankAutoDtlService extends BaseService<AbBillBankAutoDtl,String> {
	@Autowired
	private AbBillBankAutoDtlDao aAbBillBankAutoDtlDao;
	@Autowired
	private AbBisBifInitService abBisBifInitService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	/**
	* 删除对象
	* @param entity
	*/
	@Override
	public void deleteEntity(AbBillBankAutoDtl entity){
		super.deleteEntity(entity);
	}
	/**
	* 根据id删除
	* @param id
	*/
 	@Override
	public void delete(String id){
 		super.delete(id);
 	}
 	
	/**
	* 保存
	* @param entity
	*/
 	public void save(AbBillBankAutoDtl entity){
		injectValues(entity);
		super.saveEntity(entity);
 	}
	/**
	 * 填充冗余字段。
	 *
	 * @param entity 票据银行自动业务配置对象。
	 */
	private void injectValues(AbBillBankAutoDtl entity) {
		// 冗余字段填充
		Optional.ofNullable(entity.getBifCode()).ifPresent(bifId -> {
			BisBifInitDTO bifInfo = bisBifInitService.getObjectById(bifId);
			Optional.ofNullable(bifInfo).ifPresent(bif -> {
				entity.setBifCode(bif.getId());
				entity.setBifName(bif.getName());
				entity.setNewBifId(bif.getId());
				entity.setNewBifCode(bif.getBifCode());
				Optional.ofNullable(bif.getBankTypeId()).ifPresent(bankTypeId -> {
					BankTypeDTO bankType = bankTypeService.getObjectById(bankTypeId);
					Optional.ofNullable(bankType).ifPresent(bankTypeDTO -> {
						entity.setBankTypeId(bankTypeId);
						entity.setBankType(bankTypeDTO.getBankType());
						entity.setTypeName(bankTypeDTO.getTypeName());
					});
				});
			});
		});
	}
	/**
	* 更新
	* @param entity
	*/
	public void update(AbBillBankAutoDtl entity){
		super.updateEntity(entity);
	}
	
	/**
	* 保存or更新
	* @param entity
	*/
 	@Override
	public void saveOrUpdate(AbBillBankAutoDtl entity){
		injectValues(entity);
 		super.saveOrUpdate(entity);
 	}
	@Override
	public BaseDao<AbBillBankAutoDtl, String> getEntityDao() {
		return aAbBillBankAutoDtlDao;
	}
	/**
	 *列表查询显示
	 *@param dataTablesInput
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput<AbBillBankAutoDtlVO> listAbBillBankAutoDtl(DataTablesInput dataTablesInput) {
		return aAbBillBankAutoDtlDao.listAbBillBankAutoDtl(dataTablesInput);
	}
	/**
	 * 根据银行接口类型Id判断是否存在多条
	 * @param bifCode
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isExistMulByBifCode(String bifCode){
		boolean flag = false;
		List<AbBillBankAutoDtl> list =aAbBillBankAutoDtlDao.listByBifCode(bifCode);
		if(list!=null && !list.isEmpty()){
			flag = true;
		}
		return flag;
	}
	/**
	 * 根据银行接口类型Id获取数据
	 *
	 * @param bifCode
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillBankAutoDtl getDataByBifCode(String bifCode){
		AbBillBankAutoDtl billBankAutoDtl = null;
		if(StringUtils.isBlank(bifCode)){
			return null;
		}
		List<AbBillBankAutoDtl> list =aAbBillBankAutoDtlDao.listByBifCode(bifCode);
		if(list!=null && !list.isEmpty()){
			billBankAutoDtl = list.get(0);
		}
		return billBankAutoDtl;
	}
	
	/**
	 * 根据业务类型获取银行有效的自动业务（用于做业务时排除这些业务功能）
	 * @param autoType isEAutoAccept电票自动提示承兑，isEAutoInvoice电票自动提示收票，isEAutoPay电票自动提示付款
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getBifCodeByAutoType(String autoType){
		List<String> bifCodeList=new ArrayList<>();
		Map<String, Object> params=new HashMap<>();
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		if(AccebillConstant.AB_CONFIG_ISEAUTOACCEPT.equals(autoType)){
			params.put("autoAccept", AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else if(AccebillConstant.AB_CONFIG_ISEAUTOINVOICE.equals(autoType)){
			params.put("autoInvoice", AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else if(AccebillConstant.AB_CONFIG_ISEAUTOPAY.equals(autoType)){
			params.put("autoPay", AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else if(AccebillConstant.AB_CONFIG_ISPOOLENDORSE.equals(autoType)){
			params.put("poolEndorse", AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else if(AccebillConstant.AB_CONFIG_ISPOOLEMITBILL.equals(autoType)){
			params.put("poolEmitBill", AccebillConstant.AB_BILL_VALID_SIGN_1);
		}else{
			return bifCodeList;
		}
		List<AbBillBankAutoDtl> autoDtlList=aAbBillBankAutoDtlDao.listAbBillBankAutoDtlByParams(params);
		if(autoDtlList!=null&&!autoDtlList.isEmpty()){
			for(AbBillBankAutoDtl autoDtl:autoDtlList){
				BisBifInitDTO bifInfo=abBisBifInitService.getObjectById(autoDtl.getBifCode());
				if(bifInfo!=null){
					bifCodeList.add(bifInfo.getBifCode());
				}
			}
		}
		return bifCodeList;
	}
}
