package com.bytter.subject.service.impl;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.dao.FcSubjectDao;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.service.FcSubjectService;
import com.bytter.subject.vo.FcSubjectVo;
import com.bytter.system.api.IFcSubjectService;
import com.bytter.system.api.dto.FcSubjectDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("fcSubjectServiceI")
@Transactional
@DubboService
public class FcSubjectServiceImpl implements IFcSubjectService {
	@Autowired
	private FcSubjectDao fcSubjectDao;
	@Autowired
	private FcSubjectService fcSubjectService;
	@Override
	public List<FcSubjectDTO> getSubjectByIds(List<String> ids) {
		return fcSubjectService.getSubjectByIds(ids);
	}
	@Override
	public void update(FcSubjectDTO fcSubjectDTO, String loginName) {
		fcSubjectService.update(fcSubjectDTO,loginName);
	}
	@Override
	public List<String> getSubsetBySubjectSort(String id) {
		return fcSubjectService.getSubsetBySubjectSort(id);
	}
	/**
	 * 根据科目类型查询id的全部子集包含自己
	 *
	 * @param id
	 * @param subjectSort 科目类型
	 */
	@Override
	public List<String> getSubsetBySubjectSort(String id, String subjectSort) {
		return getSubsetBySubjectSort(id, subjectSort, true);
	}
	private List<String> getSubsetBySubjectSort(String id, String subjectSort, boolean sign) {
		List<String> result = this.getFatherId(id, fcSubjectDao.listSubjectBySort(subjectSort));
		if (sign) {
			result.add(id);
		}
		return result;
	}
	private List<String> getFatherId(String id, List<FcSubject> list) {
		List<String> result = new ArrayList<String>();
		if (EmptyUtils.isEmpty(list) || EmptyUtils.isEmpty(id)) {
			return result;
		}
		for (FcSubject sub : list) {
			String fatherId = sub.getFatherId();
			if (EmptyUtils.isEmpty(fatherId)) {
				continue;
			}
			if (id.equals(fatherId)) {
				result.add(sub.getId());
				result.addAll(this.getFatherId(sub.getId(), list));
			}
		}
		return result;
	}
	@Override
    public FcSubjectDTO findSubjectById(String id){
		return fcSubjectDao.findSubjectById(id);
	}
	@Override
	public FcSubjectDTO findSubjectByCode(String subjectCode) {
		return fcSubjectDao.findSubjectByCode(subjectCode);
	}
	@Override
	public List<FcSubjectDTO> findSubjectBySysCode(String sysSubjcetCode, String valid) {
		return fcSubjectDao.findSubjectBySysCode(sysSubjcetCode,valid);
	}
	@Override
	public List<FcSubjectDTO> findSubjectByAccType(String accType, String valid) {
		return fcSubjectDao.findSubjectByAccType(accType, valid);
	}
	@Override
	public List<FcSubjectDTO> findSubjectBySort(String sort, String valid) {
		return fcSubjectDao.findSubjectBySort(sort, valid);
	}
	@Override
	public FcSubjectDTO findSubjectByAccTypeSubCode(String sysSubjcetCode, String accType) {
		return fcSubjectDao.findSubjectByAccTypeSubCode(sysSubjcetCode, accType);
	}
	@Override
	public List<FcSubjectDTO> findSubjectByFatherId(){
		return fcSubjectDao.findSubjectByFatherId();
	}
	@Override
	public Map<String,String> getSubjectNameBySubjectCode(List<String> subjectCodes){
		return fcSubjectService.getSubjectNameBySubjectCode(subjectCodes);
	}
	@Override
	public List<FcSubjectDTO> listValidSubject(){
		List<FcSubjectVo> fcSubjectVos = fcSubjectService.listValidSubject(null);
		return BeanUtils.copyListProperties(fcSubjectVos,FcSubjectDTO.class);
	}
	@Override
	public String getSubjectNameById(String id){
		return fcSubjectService.getSubjectNameById(id);
	}

    @Override
    public boolean checkCashSubject(List<String> subIds) {
		if (EmptyUtils.isEmpty(subIds) || subIds.size() < 1) {
			return false;
		}
		List<FcSubjectDTO> subList = fcSubjectDao.getSubListByIds(subIds);
		if (EmptyUtils.isEmpty(subList) || subList.size() < 1) {
			return false;
		}

		List<FcSubjectDTO> fcSubList = subList.stream().filter(subDO -> StringUtils.equals(subDO.getIsCashItem().toString(), CommonConstant.VALID_SIGN_YES)).collect(Collectors.toList());
		if (fcSubList.size() < 1) {
			return false;
		}
		return true;
    }
}
