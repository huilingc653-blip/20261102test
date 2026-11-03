package com.bytter.subject.service.impl;
import com.bytter.subject.dao.FcSubjectPoolDao;
import com.bytter.system.api.IFcSubjectPoolService;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service("fcSubjectPoolServiceI")
@Transactional
@DubboService
public class FcSubjectPoolServiceImpl implements IFcSubjectPoolService {
	@Autowired
	private FcSubjectPoolDao fcSubjectPoolDao;
	/**
	 * 根据开户种类查询资金池会计科目
	 * @param accType 账户类型
	 * @param valid 是否有效
	 * @return
	 */
	@Override
	public List<FcSubjectPoolDTO> findSubjectPoolByAccType(String accType, String valid) {
		return fcSubjectPoolDao.findSubjectPoolByAccType(accType,valid);
	}
	/**
	 * 根据计息标志查询金池会计科目
	 * @param interestSign
	 * @return
	 */
	@Override
	public List<FcSubjectPoolDTO> findSubjectPoolByInterestSign(String interestSign) {
		return fcSubjectPoolDao.findSubjectPoolByInterestSign(interestSign);
	}
	/**
	 * 查询有效会计科目
	 * @return
	 */
	@Override
	public List<FcSubjectPoolDTO> findSubjectPool() {
		return fcSubjectPoolDao.findSubjectPool();
	}
	@Override
	public List<FcSubjectPoolDTO> getFcSubjectPoolByIds(List<String> ids){
		return fcSubjectPoolDao.getFcSubjectPoolByIds(ids);
	}
}
