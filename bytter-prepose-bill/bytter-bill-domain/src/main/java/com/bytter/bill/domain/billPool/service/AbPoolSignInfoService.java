package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolSignInfoDao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolSignInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Title: Service
 * @Description: 票据池票据池签约信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Service("abPoolSignInfoService")
@Transactional
public class AbPoolSignInfoService extends BaseService<AbPoolSignInfoVO, String> {
	@Autowired
	private AbPoolSignInfoDao abPoolSignInfoDao;
	@Autowired
	private AbCorpService abCorpService;
	@DubboReference(check = false)
	private IDictService dictService;
	
	public BaseDao<AbPoolSignInfoVO, String> getEntityDao() {
		return abPoolSignInfoDao;
	}
	/**
	 * "查询票据池票据池签约信息列表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_POOLSIGN_OP,dataTablesInput.getCurrentUserId());
		DataTablesOutput<AbPoolSignInfoVO> dataTablesOutput = abPoolSignInfoDao.datatableQuery(dataTablesInput, corpAuthList);
		Optional.ofNullable(dataTablesOutput.getData()).ifPresent(listData -> {
			Map<String, DictDTO> dictDTOMap = dictService.listMap("AB_POOL_SIGN_STATUS");
			listData.forEach(item -> {
				Optional.ofNullable(dictDTOMap).flatMap(dictMap -> Optional.ofNullable(item.getValidSign()))
						.flatMap(validSign -> Optional.ofNullable(dictDTOMap.get(validSign)))
						.ifPresent(dict -> item.setValidSignName(dict.getName()));
			});
		});
		return dataTablesOutput;
	}
	
	/**
	 * 根据ID获取票据池签约信息
	 * @param id
	 * @return
	 */
	public AbPoolSignInfoVO getAbPoolSignInfoVOById(String id) {
		List<AbPoolSignInfoVO> list=abPoolSignInfoDao.getAbPoolSignInfoVOById(id);
		if(ListUtils.isNotNullOrEmpty(list)){
			Map<String, DictDTO> dictDTOMap = dictService.listMap("AB_POOL_SIGN_STATUS");
			AbPoolSignInfoVO signInfoVO = list.get(0);
			Optional.ofNullable(dictDTOMap).flatMap(dictMap -> Optional.ofNullable(signInfoVO.getValidSign()))
					.flatMap(validSign -> Optional.ofNullable(dictDTOMap.get(validSign)))
					.ifPresent(dict -> signInfoVO.setValidSignName(dict.getName()));
			return signInfoVO;
		}
		return null;
	}
}
