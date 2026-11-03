package com.bytter.biz.transfer.swap.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bytter.biz.transfer.swap.dao.ErpTemplateDao;
import com.bytter.biz.transfer.swap.dao.ErpTemplateStoreDao;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.entity.ErpTemplateStore;
import com.bytter.check.api.ITemplateService;
import com.bytter.check.vo.ErpTemplateStoreVO;
import com.bytter.check.vo.ErpTemplateVO;

@Service("templateService")
@DubboService
public class TemplateService implements ITemplateService{
	 @Autowired
	private ErpTemplateDao rrpTemplateDao;
	 @Autowired
	private ErpTemplateStoreDao rrpTemplateStoreDao;
	
	public ErpTemplateVO getErpTemplate(String templateCode) {
		String hql = " from ErpTemplate where templateCode='" + templateCode + "'";
		List<ErpTemplate> list = rrpTemplateDao.listByHQL(hql);
		if (list.size() > 0) {
			ErpTemplate tp = list.get(0);
			ErpTemplateVO vo = new ErpTemplateVO();
			BeanUtils.copyProperties(tp, vo);
			
			return vo;
		}
		return null;
	}

	@Override
	public void save(ErpTemplateStoreVO store) {
		if(ObjectUtils.isNotEmpty(store)) {
			ErpTemplateStore entity = new ErpTemplateStore();
			BeanUtils.copyProperties(store, entity);
			rrpTemplateStoreDao.save(entity);
		}
	}

}
