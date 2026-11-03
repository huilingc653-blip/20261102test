package com.bytter.biz.netbank.service;

import com.bytter.biz.netbank.dao.FcBankAccGroupAreaDao;
import com.bytter.biz.netbank.dao.FcBankAccGroupDao;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcBankAccGroupArea;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linqp
 * @Description 银行账号组类型Service
 */
@Service("fcBankAccGroupAreaService")
@Transactional
public class FcBankAccGroupAreaService extends BaseService<FcBankAccGroupArea, String> {
	@Autowired
	private FcBankAccGroupAreaDao fcBankAccGroupAreaDao;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FcBankAccGroupArea entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		super.delete(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(FcBankAccGroupArea entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FcBankAccGroupArea entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcBankAccGroupArea entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FcBankAccGroupArea, String> getEntityDao() {
		return fcBankAccGroupAreaDao;
	}

	public void deleteByGroupId(String id) {
		if(StringUtils.isEmpty(id)){
			return;
		}
		fcBankAccGroupAreaDao.deleteByGroupId(id);
	}
}
