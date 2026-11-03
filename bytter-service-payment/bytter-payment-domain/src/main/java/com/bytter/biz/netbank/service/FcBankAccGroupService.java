package com.bytter.biz.netbank.service;

import com.bytter.biz.inneraccount.vo.BtBankAccDtlRpaVO;
import com.bytter.biz.netbank.dao.FcBankAccGroupAreaDao;
import com.bytter.biz.netbank.dao.FcBankAccGroupDao;
import com.bytter.biz.netbank.dao.FcReceiptTypeDao;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcBankAccGroupArea;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IOperationAreaService;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author linqp
 * @Description 银行账号组类型Service
 */
@Service("fcBankAccGroupService")
@Transactional
public class FcBankAccGroupService extends BaseService<FcBankAccGroup, String> implements IUserExcelVerifyHandler<FcBankAccGroupVO> {
	@Autowired
	private FcBankAccGroupDao fcBankAccGroupDao;
	@Autowired
	private FcBankAccGroupAreaService fcBankAccGroupAreaService;
	@Autowired
	private FcBankAccGroupAreaDao fcBankAccGroupAreaDao;
	@DubboReference
	IOperationAreaService operationAreaService;

	@DubboReference
	private IBtBankAccService btBankAccService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(FcBankAccGroup entity) {
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
	public void save(FcBankAccGroup entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FcBankAccGroup entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcBankAccGroup entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<FcBankAccGroup, String> getEntityDao() {
		return fcBankAccGroupDao;
	}

	public DataTablesOutput<FcBankAccGroupVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		return fcBankAccGroupDao.getDataTablesUnion(dataTablesInput);
	}

	/**
	 *
	 * @param fcBankAccGroupVO
	 */
	public void save(FcBankAccGroupVO fcBankAccGroupVO) {
		List<FcBankAccGroup> groupsCode = fcBankAccGroupDao.listByAccGroupCode(fcBankAccGroupVO.getAccGroupCode());
		if(CollectionUtils.isNotEmpty(groupsCode)){
			throw new BusinessException(fcBankAccGroupVO.getAccGroupCode()+"账户组编码已经存在！");
		}

		List<FcBankAccGroup> groupsName = fcBankAccGroupDao.listByAccGroupCode(fcBankAccGroupVO.getAccGroupName());
		if(CollectionUtils.isNotEmpty(groupsName)){
			throw new BusinessException(fcBankAccGroupVO.getAccGroupName()+"账户组名称已经存在！");
		}

		FcBankAccGroup fcBankAccGroup = new FcBankAccGroup();
		fcBankAccGroup.setAccGroupName(fcBankAccGroupVO.getAccGroupName());
		fcBankAccGroup.setAccGroupCode(fcBankAccGroupVO.getAccGroupCode());
		this.save(fcBankAccGroup);

		String areaCodes = fcBankAccGroupVO.getAreaCodes();
		if(StringUtils.isNotEmpty(areaCodes)){
			List<FcBankAccGroupArea> groupAreaList = new ArrayList<>();
			String[] codes = areaCodes.split(",");
			for(String code : codes){
				FcBankAccGroupArea groupArea = new FcBankAccGroupArea();
				groupArea.setGroupId(fcBankAccGroup.getId());
				groupArea.setAreaCode(code);
				groupAreaList.add(groupArea);
			}
			fcBankAccGroupAreaService.batchSave(groupAreaList);
		}
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public FcBankAccGroupVO getById(String id) {
		FcBankAccGroupVO groupVO  = fcBankAccGroupDao.getById(id);

		return groupVO;
	}

	public void update(FcBankAccGroupVO fcBankAccGroupVO) {
		FcBankAccGroup fcBankAccGroup = new FcBankAccGroup();
		fcBankAccGroup.setAccGroupName(fcBankAccGroupVO.getAccGroupName());
		fcBankAccGroup.setAccGroupCode(fcBankAccGroupVO.getAccGroupCode());
		fcBankAccGroup.setId(fcBankAccGroupVO.getId());
		this.saveOrUpdate(fcBankAccGroup);

		fcBankAccGroupAreaService.deleteByGroupId(fcBankAccGroupVO.getId());

		String areaCodes = fcBankAccGroupVO.getAreaCodes();
		if(StringUtils.isNotEmpty(areaCodes)){
			List<FcBankAccGroupArea> groupAreaList = new ArrayList<>();
			String[] codes = areaCodes.split(",");
			for(String code : codes){
				FcBankAccGroupArea groupArea = new FcBankAccGroupArea();
				groupArea.setGroupId(fcBankAccGroup.getId());
				groupArea.setAreaCode(code);
				groupAreaList.add(groupArea);
			}
			fcBankAccGroupAreaService.batchSave(groupAreaList);
		}

	}

	public void deleteById(String id) {
		FcBankAccGroup fcBankAccGroup = this.get(id, true);
		List<BtBankAccDTO> byGroupCode = btBankAccService.getByGroupCode(fcBankAccGroup.getAccGroupCode());
		if(CollectionUtils.isNotEmpty(byGroupCode)){
			List<String> bankAccs = byGroupCode.stream().map(BtBankAccDTO::getBankAcc).collect(Collectors.toList());
			throw new BusinessException("["+StringUtils.join(bankAccs,",")+"]"+"银行账号已经关联了该账户组，不允许删除");
		}else{
			this.delete(id);
			fcBankAccGroupAreaService.deleteByGroupId(id);
		}
	}

	public void saveImport(List<FcBankAccGroupVO> fcBankAccGroupVOList, ICurrentUser currentUser) {

		List<FcBankAccGroupArea> groupAreaList = new ArrayList<>();
		for(FcBankAccGroupVO groupVO : fcBankAccGroupVOList){
			FcBankAccGroup fcBankAccGroup = new FcBankAccGroup();
			fcBankAccGroup.setAccGroupName(groupVO.getAccGroupName());
			fcBankAccGroup.setAccGroupCode(groupVO.getAccGroupCode());
			this.save(fcBankAccGroup);

			String areaCodesStr = groupVO.getAreaCodes();
			if(StringUtils.isNotEmpty(areaCodesStr)){
				String[] areaCodes = areaCodesStr.split(",");
				for(String areaCode:areaCodes){
					FcBankAccGroupArea groupArea = new FcBankAccGroupArea();
					groupArea.setGroupId(fcBankAccGroup.getId());
					groupArea.setAreaCode(areaCode);
					groupAreaList.add(groupArea);
				}
			}
		}
		fcBankAccGroupAreaService.batchSave(groupAreaList);
	}

	@Override
	public ExcelVerifyHanlderResult verifyHandler(FcBankAccGroupVO obj, Object... user) {
		return null;
	}

	@Override
	public void verifyHandler(List<FcBankAccGroupVO> list, Object... user) {

		for (FcBankAccGroupVO groupVO : list) {
			List<String> errorList = new ArrayList<>();
			List<FcBankAccGroup> groupsCode = fcBankAccGroupDao.listByAccGroupCode(groupVO.getAccGroupCode());
			if (CollectionUtils.isNotEmpty(groupsCode)) {
				errorList.add(groupVO.getAccGroupCode() + "账户组编码已经存在！");
			}

			List<FcBankAccGroup> groupsName = fcBankAccGroupDao.listByAccGroupName(groupVO.getAccGroupName());
			if (CollectionUtils.isNotEmpty(groupsName)) {
				errorList.add(groupVO.getAccGroupName() + "账户组名称已经存在！");
			}

			String areaCodesStr = groupVO.getAreaCodes();
			if(StringUtils.isNotEmpty(areaCodesStr)){
				String[] areaCodes = areaCodesStr.split(",");
				for(String areaCode:areaCodes){
					List<OperationAreaDTO> operationAreaDTOS = operationAreaService.queryByCode(areaCode);
					if(CollectionUtils.isEmpty(operationAreaDTOS)){
						errorList.add("["+areaCode+"]"+"不存在！");
					}
				}
			}

			groupVO.addErrorMsg(errorList);
		}
	}

	/**
	 * 获取有效的系统结算方式
	 *
	 * @return
	 */
	public List<FcBankAccGroup> getValidPayType() {
		return fcBankAccGroupAreaDao.getValidPayType();
	}

	public Map<String, String> getAccGroupMapByIds(List<String> codeList) {
		if (EmptyUtils.isEmpty(codeList)) {
			return null;
		}
		List<FcBankAccGroup> groupList = this.fcBankAccGroupAreaDao.listByIds(codeList);
		if (EmptyUtils.isEmpty(groupList)) {
			return null;
		}
		return groupList.stream().collect(Collectors.toMap(FcBankAccGroup::getAccGroupCode, FcBankAccGroup::getAccGroupName, (a, b) -> a));
	}
}
