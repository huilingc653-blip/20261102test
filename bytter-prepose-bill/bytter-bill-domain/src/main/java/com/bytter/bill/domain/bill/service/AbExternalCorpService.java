package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 客商资料接口
 * @author zhenghj
 *
 */
@Service("abExternalCorpService")
@Transactional
public class AbExternalCorpService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IExternalCorpService externalCorpService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 根据客商ID和客商名称判断是否需要新增客商信息
	 *
	 * @param externalCorpDTO
	 */
	public void saveIfNotExist(ExternalCorpDTO externalCorpDTO){
		externalCorpService.saveIfNotExist(externalCorpDTO);
	}
}
