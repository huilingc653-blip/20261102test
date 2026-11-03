package com.bytter.biz.netbank.service;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.netbank.dao.NisPersonnelDetailDao;
import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.netbank.vo.NisPersonnelDetailVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.payment.api.enums.PersonnelDetailMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 职工信息维护表
 * @date 2017-05-26 09:31:22
 */
@Service("nisPersonnelDetailService")
@Transactional
public class NisPersonnelDetailService extends BaseService<NisPersonnelDetail, String> implements IExcelVerifyHandler<NisPersonnelDetailVO>, ICorpDeleteVerifyService {
	@Autowired
	private NisPersonnelDetailDao nisPersonnelDetailDao;
	@DubboReference(check = false)
	private IInputBankInfoService inputBankInfoService;
	@DubboReference(check = false)
	private IRegionService regionService;
	@Autowired
	private NisExpenseDetailService expenseDetailService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private IDictService dictService;
	@Resource
    private IMultiLangService multiLangServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(NisPersonnelDetail entity) {
		super.deleteEntity(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		this.nisPersonnelDetailDao.deleteById(id);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void save(NisPersonnelDetail entity, ICurrentUser user) {
		// 判断员工编码 是否重复
		boolean result = this.isRepeatWorkNo(entity.getId(), entity.getWorkNo());
		if (result) {
			throw new ParameterException(PersonnelDetailMessageEnum.NIS_PERSONNELDETAIL_MESSAGE_WORKNO_UNIQUE);
		}
		entity.setCreateBy(user.getLoginName());
		entity.setCreateDate(DateUtils.getDate());
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(NisPersonnelDetail entity, ICurrentUser user) {
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	public void saveOrUpdate(NisPersonnelDetail entity, ICurrentUser user) {
		// 判断员工编码是否重复
		boolean result = this.isRepeatWorkNo(entity.getId(), entity.getWorkNo());
		if (result) {
			throw new ParameterException(PersonnelDetailMessageEnum.NIS_PERSONNELDETAIL_MESSAGE_WORKNO_UNIQUE);
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		super.saveOrUpdate(entity);
	}
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void deleteNisPersonnelDetail(NisPersonnelDetail entity) {
		// 判断职工信息是否被占用
		boolean flag = this.expenseDetailService.isUsedPersonnel(entity.getId());
		if (flag) {
			throw new ParameterException(PersonnelDetailMessageEnum.NIS_PERSONNELDETAIL_MESSAGE_WORKID_USE, entity.getUserName());
		}
		this.delete(entity.getId());
	}
	/**
	 * 批量删除对象
	 *
	 * @param ids
	 */
	public void batchDelete(String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			NisPersonnelDetail nisPersonnelDetail = this.get(id);
			this.deleteNisPersonnelDetail(nisPersonnelDetail);
		}
	}
	@Override
	public BaseDao<NisPersonnelDetail, String> getEntityDao() {
		return nisPersonnelDetailDao;
	}
	/**
	 * 查询职工信息列表
	 *
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<NisPersonnelDetailVO> getDataTablesUnion(DataTablesInput input,ICurrentUser user) {
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_NISPERSONNELDETAIL_QUERY,user.getId()),"p.corp_Id",params);
		DataTablesOutput<NisPersonnelDetailVO> output = nisPersonnelDetailDao.getDataTablesUnion(input,corpAuthSql,params);
		List<NisPersonnelDetailVO> list = output.getData();
		if(list.size()>0) {
			List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_NISPERSONNELDETAIL_QUERY, user.getId());
			Map<String, CorpDTO> corpMap = corpList.stream().collect(Collectors.toMap(CorpDTO::getId, Function.identity()));
			for (NisPersonnelDetailVO vo : list) {
				CorpDTO corp = corpMap.get(vo.getCorpId());
				vo.setCorpName(corp.getName());

				RegionDTO regionDTO = regionService.getById(vo.getCity());
				if(null != regionDTO) {
					vo.setProvName(regionDTO.getProvName());
					vo.setCityName(regionDTO.getName());
				}
			}
		}
		return output;
	}
	/**
	 * 根据id查找职工信息
	 *
	 * @param id
	 * @return
	 */
	public NisPersonnelDetail getById(String id) {
		return nisPersonnelDetailDao.getById(id);
	}
	/**
	 * 根据id查找职工信息
	 *
	 * @param id
	 * @return
	 */
	public NisPersonnelDetailVO getByUnion(String id) {
		NisPersonnelDetailVO vo = nisPersonnelDetailDao.getByUnion(id);
		if(null != vo) {
			//单位
			CorpDTO corp = corpService.get(vo.getCorpId());
			vo.setCorpName(corp.getName());
			if(StringUtils.isNotBlank(vo.getBifCode())) {
				BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(vo.getBifCode());
				if(bisBifInitDTO != null && StringUtils.isNotBlank(bisBifInitDTO.getName())) 
				{
					vo.setBifName(bisBifInitDTO.getName());
				}
				
			}
			RegionDTO regionDTO = regionService.getById(vo.getCity());
			if(null != regionDTO) {
				vo.setProvName(regionDTO.getProvName());
				vo.setCityName(regionDTO.getName());
			}
		}
		return vo;
	}
	/**
	 * 根据职工编码判断用户信息是否重复
	 *
	 * @param id
	 * @param workNo
	 * @return false 不重复	true 重复
	 */
	public boolean isRepeatWorkNo(String id, String workNo) {
		List<NisPersonnelDetail> list = nisPersonnelDetailDao.listBy("workNo", workNo);
		return this.isRepeat(id,list);
	}
	/**
	 * 根据身份证号判断用户信息是否重复
	 *
	 * @param id
	 * @param idcard
	 * @return false 不重复	true 重复
	 */
	public boolean isRepeatIdcard(String id, String idcard) {
		List<NisPersonnelDetail> list = nisPersonnelDetailDao.listBy("idcard", idcard);
		return this.isRepeat(id,list);
	}
	/**
	 * 校验重复
	 * @param id
	 * @param list
	 * @return
	 */
	public boolean isRepeat(String id, List<NisPersonnelDetail> list){
		if (ListUtils.isNullOrEmpty(list)) {
			return false;
		} else {
			if (EmptyUtils.isBlank(id)) {
				return true;
			}
			NisPersonnelDetail obj = list.get(0);
			return obj.getId().equals(id) ? false : true;
		}
	}
	/**
	 * 导入功能
	 *
	 * @param list
	 */
	public void saveImport(List<NisPersonnelDetailVO> list, ICurrentUser user) {
		List<NisPersonnelDetail> entityList = new ArrayList<NisPersonnelDetail>();
		String loginName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		for (NisPersonnelDetailVO vo : list) {
			NisPersonnelDetail personnelDetail = new NisPersonnelDetail();
			BeanUtils.copyNotNullProperties(vo, personnelDetail);
			personnelDetail.setStatus(NetbankConstant.STATUE_TEMP);
			personnelDetail.setCreateBy(loginName);
			personnelDetail.setCreateDate(curDate);
			if (dictService.get("sex","1").equals(vo.getSex()) || "1".equals(vo.getSex())){
				personnelDetail.setSex("1");
			} else {
				personnelDetail.setSex("0");
			}
			entityList.add(personnelDetail);
		}
		batchSave(entityList);
	}
	@Override
	public ExcelVerifyHanlderResult verifyHandler(NisPersonnelDetailVO vo) {
		List<String> msgList = new ArrayList<>();
		// 判断编码是否重复
		boolean result = this.isRepeatWorkNo(vo.getId(), vo.getWorkNo());
		if (result) {
			msgList.add("nis.personneldetail.message.workno.unique");
		}
		// 判断身份证号是否重复
		boolean resultCard = this.isRepeatIdcard(vo.getId(), vo.getIdcard());
		if (resultCard) {
			msgList.add("nis.personneldetail.message.idcard.unique");
		}
		// 检验身份证号是否正确
		if (!EmptyUtils.isEmpty(vo.getIdcard()) && !IdcardUtils.validateCard(vo.getIdcard())) {
			msgList.add("nis.netbank.message.idcard");
		}
		// 判断单位在系统中是否存在
		CorpDTO corp = this.corpService.getCorpIdByName(vo.getCorpName());
		if (EmptyUtils.isEmpty(corp) || EmptyUtils.isBlank(corp.getId())) {
			msgList.add("nis.netbank.message.corp.null");
		} else {
			vo.setCorpId(corp.getId());
		}
		// 判断开户行在系统中是否存在
		if (!EmptyUtils.isBlank(vo.getBankCode())) {
			InputBankInfoDTO btBankDTO = inputBankInfoService.getByBankCodeAndImport(vo.getBankCode());
			if (EmptyUtils.isEmpty(btBankDTO)) {
				msgList.add("nis.netbank.message.bank.null");
			} else {
				vo.setBankCode(btBankDTO.getBankCode());
				vo.setBankName(btBankDTO.getBankName());
			}
		}
		// 判断接口代码在系统中是否存在
		if (!EmptyUtils.isBlank(vo.getBifName())) {
			BisBifInitDTO bisBifInitDTO = bisBifInitService.getByName(vo.getBifName());
			if (EmptyUtils.isEmpty(bisBifInitDTO)) {
				msgList.add("nis.netbank.message.bisbifinit.null");
			} else {
				vo.setBifCode(bisBifInitDTO.getId());
			}
		}
		// 判断账户类型
		if (!EmptyUtils.isBlank(vo.getCardTypeName())) {
			String cardType = dictService.getCodeByName("nisPersonnelDetail.cardType", vo.getCardTypeName());
			if (EmptyUtils.isBlank(cardType)) {
				msgList.add("nis.netbank.message.cardtype.mismatch");
			} else {
				vo.setCardType(cardType);
			}
		}
		// 判断省市在系统中是否存在
		RegionDTO provRegion = this.regionService.getByName(vo.getProvName(),true);
		RegionDTO cityRegion = this.regionService.getByName(vo.getCityName(),false);
		if (EmptyUtils.isEmpty(provRegion)) {
			msgList.add("nis.netbank.message.provname.null");
		} else if (EmptyUtils.isEmpty(cityRegion)) {
			msgList.add("nis.netbank.message.cityname.null");
		} else {
			//判断省市是否一致
			if (!cityRegion.getParentId().equals(provRegion.getId())) {
				msgList.add("nis.netbank.message.cityname.mismatch");
			} else {
				vo.setProv(provRegion.getId());
				vo.setCity(cityRegion.getId());
			}
		}
		if (EmptyUtils.isEmptyList(msgList)) {
			return new ExcelVerifyHanlderResult(true, msgList);
		} else {
			return new ExcelVerifyHanlderResult(false, msgList);
		}
	}
	@Override
	public void verifyHandler(List<NisPersonnelDetailVO> list) {
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有维护职工信息，不允许删除！
			int num = this.nisPersonnelDetailDao.getPersonnelCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_PERSONNEL,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_PERSONNEL_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
}
