package com.bytter.biz.finacial.closedeal.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
@SuppressWarnings("rawtypes")
@Service("fcCloseBaseService")
@Transactional
public class FcCloseBaseService extends BaseService {
	@Override
	public BaseDao getEntityDao() {
		throw new UnsupportedOperationException("请重载getEntityDao方法");
	}
}
