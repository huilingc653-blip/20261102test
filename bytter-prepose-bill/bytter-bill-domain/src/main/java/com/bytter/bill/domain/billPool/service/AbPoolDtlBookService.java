package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.service.AbCorpService;
import com.bytter.bill.domain.billPool.dao.AbPoolDtlBookDao;
import com.bytter.bill.domain.billPool.vo.AbPoolDtlBookVO;
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
 * @Description: 票据池明细账簿信息DO
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 *
 */
@Service("abPoolDtlBookService")
@Transactional
public class AbPoolDtlBookService extends BaseService<AbPoolDtlBookVO, String> {
	@Autowired
	private AbPoolDtlBookDao abPoolDtlBookDao;
	@Autowired
	private AbCorpService abCorpService;
	
	public BaseDao<AbPoolDtlBookVO, String> getEntityDao() {
		return abPoolDtlBookDao;
	}
	/**
	 * "查询票据池明细账簿信息列表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true)
	public DataTablesOutput datatableQuery(DataTablesInput dataTablesInput) {
		List<String> corpAuthList=abCorpService.getUserCorpIds(dataTablesInput.getResource(),dataTablesInput.getCurrentUserId());
		return abPoolDtlBookDao.datatableQuery(dataTablesInput,corpAuthList);
	}
	
	/**
	 * 根据ID获取明细账簿信息
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbPoolDtlBookVO getAbPoolDtlBookVOById(String id) {
		List<AbPoolDtlBookVO> list=abPoolDtlBookDao.getAbPoolDtlBookVOById(id);
		if(ListUtils.isNotNullOrEmpty(list)){
			return list.get(0);
		}
		return null;
	}
}
