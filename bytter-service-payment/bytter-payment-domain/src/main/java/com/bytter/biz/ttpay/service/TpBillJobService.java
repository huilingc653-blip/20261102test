package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpBillJobDao;
import com.bytter.biz.ttpay.vo.TpBillJobVO;
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
 * @Description: 商户信息对账单Service
 * @author 郑刚
 * @date 2019-09-30 15:43:05
 * @version 1.0
 */
@Service("tpBillJobService")
@Transactional
public class TpBillJobService extends BaseService<T, String> {
	@Autowired
	private TpBillJobDao tpBillJobDao;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	@Override
	public BaseDao<T, String> getEntityDao() {
		return tpBillJobDao;
	}
	
	/**
	 * 
	* @Title: 查询商户信息对账单数据
	* @param input
	* @return 列表数据  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
		List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_BILLJOB_OP,input.getCurrentUser().getId());
		DataTablesOutput data=tpBillJobDao.getDataTablesUnion(input,corpAuthList);
		return this.setJoinField(data);
	}
	/**
	 * 设置付款办理的关联字段
	 * @param output
	 * @return
	 */
	public DataTablesOutput setJoinField(DataTablesOutput output) {
		if(!EmptyUtils.isEmpty(output.getData())) {
			List<TpBillJobVO> list = output.getData();
			Map<String, CorpDTO> corpMap = corpService.getCorpMap();
			Map<String, Map<String, DictDTO>> listDictMap = dictService.listDictMap(TtPayConstant.TTPAY_PAYTYPE_DICT,TtPayConstant.TTPAY_BILLJOB_STATUS_DICT);
			for (TpBillJobVO vo:list) {
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
	private void injectValues(TpBillJobVO vo, Map<String, Map<String, DictDTO>> listDictMap) {
		// 支付方式
		Optional.ofNullable(vo.getPayType()).ifPresent(payType -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_PAYTYPE_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(payType)))
					.ifPresent(dict -> vo.setPayTypeName(dict.getName()));
		});
		// 状态
		Optional.ofNullable(vo.getStatus()).ifPresent(status -> {
			Map<String, DictDTO> dictDtoMap = listDictMap.get(TtPayConstant.TTPAY_BILLJOB_STATUS_DICT);
			Optional.ofNullable(dictDtoMap).flatMap(dictMap -> Optional.ofNullable(dictMap.get(status)))
					.ifPresent(dict -> vo.setStatusName(dict.getName()));
		});
	}
}
