package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 会计科目接口服务类
 * @author zhenghj
 *
 */
@Service("abFcSubjectService")
@Transactional
public class AbFcSubjectService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IFcSubjectService fcSubjectService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	 /**
	 * 根据ID查询会计科目DTO对象
	 * @param id
	 * @return FcSubjectDTO对象
	 */
	 @Transactional(readOnly = true)
	 public FcSubjectDTO findSubjectById(String id){
	 	return fcSubjectService.findSubjectById(id);
	 }
}
