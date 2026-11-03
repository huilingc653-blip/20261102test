package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolHoldBookDao;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.vo.AbPoolHoldBookVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * @Title: Service
 * @Description: 票据池持有账簿信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Service("abPoolHoldBookService")
@Transactional
public class AbPoolHoldBookService extends BaseService<AbPoolHoldBookVO, String> {
	@Autowired
	private AbPoolHoldBookDao abPoolHoldBookDao;
	@Autowired
	private AbCorpService abCorpService;
	
	public BaseDao<AbPoolHoldBookVO, String> getEntityDao() {
		return abPoolHoldBookDao;
	}
	/**
	 * "查询票据池持有账簿信息列表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_QUERY_HOLD_OP,dataTablesInput.getCurrentUserId());
		return abPoolHoldBookDao.datatableQuery(dataTablesInput,corpAuthList);
	}
	
	/**
	 * 根据ID获取持有账簿信息
	 * @param id
	 * @return
	 */
	public AbPoolHoldBookVO getAbPoolHoldBookVOById(String id) {
		List<AbPoolHoldBookVO> list=abPoolHoldBookDao.getAbPoolHoldBookVOById(id);
		if(ListUtils.isNotNullOrEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
