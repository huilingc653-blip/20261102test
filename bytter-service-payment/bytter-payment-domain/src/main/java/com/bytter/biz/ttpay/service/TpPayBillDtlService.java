package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpPayBillDtlDao;
import com.bytter.biz.ttpay.vo.TpPayBillBalVO;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @Title: Service
 * @Description: 商户信息对账单明细Service
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
@Service("tpPayBillDtlService")
@Transactional
public class TpPayBillDtlService extends BaseService<T, String> {
	@Autowired
	private TpPayBillDtlDao tpPayBillDtlDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	@Override
	public BaseDao<T, String> getEntityDao() {
		return tpPayBillDtlDao;
	}
	
	/**
	 * 
	 * @Title: 商户信息流水查询信息查询列表  
	 * @param input
	 * @return 列表数据  
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_PAYDTL_OP,input.getCurrentUser().getId());
		DataTablesOutput data=tpPayBillDtlDao.getDataTablesUnion(input,corpAuthList);
		return data;
	}
	
	/**
	 * 
	 * @Title: 查询商户信息对账单明细数据
	 * @param input
	 * @return 列表数据  
	 * @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDtlViewDataTablesUnion(DataTablesInput input) {
		DataTablesOutput data=tpPayBillDtlDao.getDtlViewDataTablesUnion(input);
		return this.setJoinField(data);
	}
	/**
	 * 设置付款办理的关联字段
	 * @param output
	 * @return
	 */
	public DataTablesOutput setJoinField(DataTablesOutput output) {
		if(!EmptyUtils.isEmpty(output.getData())) {
			List<TpPayBillBalVO> list = output.getData();
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT);
			for (TpPayBillBalVO vo:list) {
				if (corpMap.containsKey(vo.getCorpId())) {
					vo.setCorpCode(corpMap.get(vo.getCorpId()).getCode());
					vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
				}
				//字典注入
				this.injectValues(vo, listDictMap);
			}
		}
		return output;
	}
	/**
	 * 字典注入
	 *
	 * @param vo
	 * @param listDictMap
	 */
	private void injectValues(TpPayBillBalVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
		// 支付方式
		Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
					.ifPresent(dict -> vo.setPayTypeName(dict.getName()));
		});
	}
	
	/**
	 * 
	* @Title: 查询商户信息交易流水明细
	* @param id
	* @param payType
	* @return 明细数据
	* @throws
	 */
	@Transactional(readOnly = true)
	public Map<String,Object> getPayBillDtlInfoByIdOrPayType(String id,String payType) {
		Map<String,Object> data=tpPayBillDtlDao.getPayBillDtlInfoByIdOrPayType(id,payType);
		return data;
	}
	/**
	 * 查询交易所有退款
	 * @param dtlId
	 * @return 明细数据
	 * @throws
	 */
	@Transactional(readOnly = true)
	public List<TpRefundOrderVO> listRefundInfo(String dtlId) {
		List<TpRefundOrderVO> list=tpPayBillDtlDao.listRefundInfo(dtlId);
		return list;
	}
	/**
	 * 
	 * @Title: 获取各支付方式对账单字段和表信息
	 * @return list    返回类型  
	 * @throws
	 */
	public List<TpPayBillViewDtlVO> getTpMerchantVOList(){
		return tpPayBillDtlDao.getTpMerchantVOList();
	}
}
