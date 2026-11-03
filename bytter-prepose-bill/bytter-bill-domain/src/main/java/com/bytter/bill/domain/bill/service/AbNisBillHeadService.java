package com.bytter.bill.domain.bill.service;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 付款接口服务类
 * @author zhenghj
 *
 */
@Service("abNisBillHeadService")
@Transactional
public class AbNisBillHeadService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private INisBillheadService iNisBillheadService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 更新付款单（票据反写接口）
	 * 状态：-1-打回；95-审批通过；99-票据完成
	 * @param id 付款单ID
	 * @param status 状态
	 * @param msg 信息
	 */
	public void updateNisBillhead(String id, Integer status, String msg, ICurrentUser user){
		iNisBillheadService.updateNisBillhead(id, status, msg, user);
	}
	/**
	 * 更新下拨账号（记账反写接口）
	 * @param billNo 付款单号
	 * @param downAccId 下拨账号Id
	 */
	public void updateNisBillheadDownAccId(String billNo, String downAccId){
		iNisBillheadService.updateNisBillheadDownAccId(billNo, downAccId);
	}
	/**
	 * 更新接口代码（记账反写接口）
	 * @param billNo 付款单号
	 * @param bankAcc 银行账号
	 */
	public void updateNisBillheadForindividual(String billNo, String bankAcc){
		iNisBillheadService.updateNisBillheadForindividual(billNo, bankAcc);
	}
	/**
	 * 付款单生成（外模块生成付款单）
	 * @param entity
	 */
	public void saveNisBillhead(NisBillheadDTO entity, ICurrentUser user){
		iNisBillheadService.saveNisBillhead(entity, user);
	}
	/**
	 * 票据支付失败处理
	 * @param id 付款单ID
	 * @param failureList 失败集合
	 * @param successList 成功集合
	 */
	public void backNisBillheadForBill(String id, List<AbVoucherinfoDTO> failureList, List<AbVoucherinfoDTO> successList){
		// todo:数据转换
		List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO> failList = BeanUtils.copyListProperties(failureList, com.bytter.biz.payment.api.dto.AbVoucherinfoDTO.class);
		List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO> succList = BeanUtils.copyListProperties(successList, com.bytter.biz.payment.api.dto.AbVoucherinfoDTO.class);
		iNisBillheadService.backNisBillheadForBill(id,failList,succList);
	}

	/**
	 * 票据支付成功处理
	 * @param ids 付款单IDs 多个,隔开
	 */
//	public void successNisBillheadForBill(String id){
//		iNisBillheadService.successNisBillheadForBill(id);
	public void successNisBillheadForBill(String ids,Integer status){
		iNisBillheadService.successNisBillheadForBill(ids,status);
	}

	/**
	 * 根据外部单号获取付款单信息
	 * @param erpId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<NisBillheadDTO> listNisBillheadByErpId(String erpId){
		return iNisBillheadService.listNisBillheadByErpId(erpId);
	}

	/**
	 * 根据共享结算单号list获取付款信息
	 *
	 * @param bursarBillNoList
	 */
	public List<NisBillheadDTO> listNisBillheadByBursarBillNos(List<String> bursarBillNoList) {
		return iNisBillheadService.listNisBillheadByBursarBillNos(bursarBillNoList);
	}
}
