package com.bytter.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.BtBankAccAppCurVO;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.dao.BtBankAccCommonDao;
import com.bytter.system.dao.NisBindingBankaccDao;
import com.bytter.system.entity.*;
import com.bytter.system.service.BisBifRuleService;
import com.bytter.system.vo.BisBifRuleVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Service("btBankAccCommonServiceI")
@Transactional
@DubboService
class BtBankAccCommonServiceImpl extends BaseService<BtBankAcc, String> implements IBtBankAccCommonService{
	@Autowired
	private BtBankAccCommonDao btBankAccCommonDao;
	@Autowired
	private ICorpService corpServiceI;
	@Autowired
	private IBankTypeService bankTypeService;
	@Autowired
	private IRegionService regionService;
	@Autowired
	private IDictService dictServiceI;
	@Autowired
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private BisBifRuleService bisBifRuleService;
	@Autowired
	private NisBindingBankaccDao nisBindingBankaccDao;
	@Autowired
	private IBisBifInitService bisBifInitService;

	@Override
	public BaseDao<BtBankAcc, String> getEntityDao() {
		return this.btBankAccCommonDao;
	}
	public BtBankAccVO getVO(String id, boolean eager) {
		BtBankAcc btBankAcc = super.get(id, eager);
		BtBankAccVO btBankAccVO = BeanUtils.copyNewInstanceProperties(btBankAcc, BtBankAccVO.class);
		return btBankAccVO;
	}
	/**
	 * 获取有效的账户信息
	 *
	 * @return
	 */
	@Override
	public List<BtBankAccDTO> listEffectiveBtBankAcc() {
		List<BtBankAcc> list = this.btBankAccCommonDao.listEffectiveBtBankAcc();
		List<BtBankAccDTO> listVO = BeanUtils.copyListProperties(list, BtBankAccDTO.class);
		return listVO;
	}
	/**
	 * 获取银行账户表的币别信息
	 *
	 * @param bankAccId 账户ID
	 * @return
	 */
	@Override
	public List<BtBankAccCurDTO> listBtBankAccCurById(String bankAccId) {
		List<BtBankAccCur> list = this.btBankAccCommonDao.listBtBankAccCurById(bankAccId);
		List<BtBankAccCurDTO> listVO = BeanUtils.copyListProperties(list, BtBankAccCurDTO.class);
		return listVO;
	}
	/**
	 * 获取银行账户申请表的币别信息
	 *
	 * @param appId 账户ID
	 * @return
	 */
	@Override
	public List<BtBankAccAppCurVO> listBtBankAccAppCurById(String appId) {
		List<BtBankAccAppCur> list = this.btBankAccCommonDao.listBtBankAccAppCurById(appId);
		List<BtBankAccAppCurVO> listVO = BeanUtils.copyListProperties(list, BtBankAccAppCurVO.class);
		return listVO;
	}
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	@Override
	public List<BtCurrencyDTO> listCurAccById(String id) {
		return this.btBankAccCommonDao.listCurAccById(id);
	}
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
	@Override
	public List<BtCurrencyDTO> listCurAccAppById(String id) {
		return this.btBankAccCommonDao.listCurAccAppById(id);
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BtBankAccDTO getBtBankAcc(String id) {
		BtBankAcc btBankAcc = this.btBankAccCommonDao.getBtBankAcc(id);
		BtBankAccDTO btBankAccVo = new BtBankAccDTO();
		BeanUtils.copyProperties(btBankAcc, btBankAccVo);
		return btBankAccVo;
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BtBankAccAppDTO getBtBankAccApp(String id) {
		BtBankAccApp btBankAccApp = this.btBankAccCommonDao.getBtBankAccApp(id);
		BtBankAccAppDTO btBankAccAppDTO = new BtBankAccAppDTO();
		BeanUtils.copyProperties(btBankAccApp, btBankAccAppDTO);
		return btBankAccAppDTO;
	}

	public void setJoinField(BtBankAccDTO dto) {
		Map<String, DictDTO> isDomesticMap = dictServiceI.listMap(BtBankAccConstant.BANKACC_ISDOMESTICBANK_DICT);
		dto.setIsDomesticBankName(isDomesticMap.containsKey(dto.getIsDomesticBank()) ? isDomesticMap.get(dto.getIsDomesticBank()).getName():"");
	}

	/**
	 * 根据账号得到银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
	@Override
	public BtBankAccAppDTO getBtBankAccAppByBankAcc(String bankAcc) {
		BtBankAccApp btBankAccApp = this.btBankAccCommonDao.getBtBankAccAppByBankAcc(bankAcc);
		BtBankAccAppDTO btBankAccAppVo = null;
		if(null != btBankAccApp) {
			btBankAccAppVo = new BtBankAccAppDTO();
			BeanUtils.copyProperties(btBankAccApp, btBankAccAppVo);
		}
		return btBankAccAppVo;
	}
	/**
	 * 根据账号得到银行账号信息
	 *
	 * @param bankAcc
	 * @return
	 */
	@Override
	public BtBankAccDTO getBtBankAccByBankAcc(String bankAcc) {
		BtBankAcc btBankAcc = this.btBankAccCommonDao.getBtBankAccByBankAcc(bankAcc);
		BtBankAccDTO btBankAccVo = new BtBankAccDTO();
		BeanUtils.copyProperties(btBankAcc, btBankAccVo);
		if (btBankAcc != null && StringUtils.isBlank(btBankAcc.getIsOnlineHandle())) {
			btBankAcc.setIsOnlineHandle(CommonConstant.NO);
		}
		return btBankAccVo;
	}
	@Override
	public BtBankAccDTO getBtBankAccVOById(String id) {
		BtBankAcc btBankAcc = this.btBankAccCommonDao.getBtBankAcc(id);
		BtBankAccDTO btBankAccVo = null;
		if (btBankAcc != null) {
			try {
				btBankAcc.setLoginPassword(BytterAESUtils.decrypt(btBankAcc.getLoginPassword()));
				btBankAcc.setPayPassword(BytterAESUtils.decrypt(btBankAcc.getPayPassword()));
			}catch(Exception e) {
				e.printStackTrace();
			}
			btBankAccVo = new BtBankAccDTO();
			BeanUtils.copyProperties(btBankAcc, btBankAccVo);
			if(StringUtils.isBlank(btBankAcc.getIsOnlineHandle())){
				btBankAccVo.setIsOnlineHandle(CommonConstant.NO);
			}
			btBankAccVo.setCurId(this.setCurId(id, ""));
			btBankAccVo.setCurName(this.setCurName(id, ""));
			btBankAccVo.setNatureName(btBankAccCommonDao.getNatureNameById(btBankAccVo.getNatureId()));
			btBankAccVo.setAttributeName(btBankAccCommonDao.getAttributeNameById(btBankAccVo.getAttributeId()));
			CorpDTO corpDTO = corpServiceI.get(btBankAccVo.getCorpId());
			if(!EmptyUtils.isEmpty(corpDTO)){
				btBankAccVo.setCorpName(corpDTO.getName());
			}
			BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccVo.getBankTypeId());
			if(!EmptyUtils.isEmpty(bankTypeDTO)){
				btBankAccVo.setBankTypeName(bankTypeDTO.getTypeName());
			}
			RegionDTO city = regionService.getById(btBankAccVo.getCity());
			if(!EmptyUtils.isEmpty(city)){
				btBankAccVo.setCityName(city.getName());
				btBankAccVo.setProvName(city.getProvName());
			}
			String electricBillName = dictServiceI.get("sf_yn", btBankAccVo.getElectricBill());
			if(!EmptyUtils.isEmpty(electricBillName)){
				btBankAccVo.setElectricBillName(electricBillName);
			}
			BisBifInitDTO bisBifInitDTO = bisBifInitService.getObjectById(btBankAcc.getBifCode());
			if(bisBifInitDTO!=null){
				btBankAccVo.setBifName(bisBifInitDTO.getName());
			}
			List<String> bindingList = nisBindingBankaccDao.listBybankId(btBankAcc.getId());
			if(EmptyUtils.isNotEmpty(bindingList) && bindingList.size() > 0){
				String bindingBankAcc = "";
				for(String bankacc : bindingList){
					if(EmptyUtils.isEmpty(bindingBankAcc)){
						bindingBankAcc = bankacc;
					}else{
						bindingBankAcc = bindingBankAcc+CommonConstant.COMMA_SPLIT+bankacc;
					}
				}
				btBankAccVo.setBindingBankAcc(bindingBankAcc);
			}

			// accType 账户类别
			String accTypeName = dictServiceI.get("btBankAcc.accType", btBankAccVo.getAccType());
			if (ObjectUtil.isNotEmpty(accTypeName)){
				btBankAccVo.setAccTypeName(accTypeName);
			}

			// circulationMode 流通方式
			String circulationModeName = dictServiceI.get("bankAcc.circulationMode", btBankAccVo.getCirculationMode());
			if (ObjectUtil.isNotEmpty(circulationModeName)){
				btBankAccVo.setCirculationModeName(circulationModeName);
			}
			// 是否浮动利率 ratesFloatName
			String ratesFloatName = dictServiceI.get("sf_yn", btBankAccVo.getRatesFloat());
			if (ObjectUtil.isNotEmpty(ratesFloatName)){
				btBankAccVo.setRatesFloatName(ratesFloatName);
			}
			this.setJoinField(btBankAccVo);

			String isOnline = dictServiceI.get("sf_yn", btBankAccVo.getIsOnline());
			String validSign = dictServiceI.get("bankAcc.validSign", btBankAccVo.getValidSign());
			String status = dictServiceI.get("bankAcc.handleStatu.query", btBankAccVo.getStatus());
			btBankAccVo.setIsOnlineStr(isOnline);
			btBankAccVo.setValidSignStr(validSign);
			btBankAccVo.setStatusStr(status);
			if (btBankAcc.getRegDate() != null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				btBankAccVo.setRegDateStr(sdf.format(btBankAcc.getRegDate()));
			}
		}
		return btBankAccVo;
	}
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BtBankAccAppDTO getBtBankAccAppVOById(String id) {
		BtBankAccAppVO vo = this.btBankAccCommonDao.getBtBankAccAppVOById(id, null);
		BtBankAccAppDTO dto = new BtBankAccAppDTO();
		BtBankAcc btBankAcc = btBankAccCommonDao.getBtBankAccByBankAcc(vo.getBankAcc());

		if (!EmptyUtils.isEmpty(vo)) {
			vo.setCurId(this.setCurId(id, "app"));
			vo.setCurName(this.setCurName(id, "app"));
			CorpDTO corpDTO = corpServiceI.get(vo.getCorpId());
			if(!EmptyUtils.isEmpty(corpDTO)){
				vo.setCorpName(corpDTO.getName());
			}
			BeanUtils.copyNotNullProperties(vo, dto);
		}
		if (btBankAcc!=null){
			dto.setReceiptCode(btBankAcc.getReceiptCode());
			dto.setFcAccGroupCode(btBankAcc.getFcAccGroupCode());
		}
		return dto;
	}
	/**
	 * 设置币别
	 *
	 * @param id
	 * @return
	 */
	@Override
	public String setCurId(String id, String type) {
		StringBuilder curIds = new StringBuilder();
		List<BtCurrencyDTO> list = null;
		if ("app".equalsIgnoreCase(type)) {
			list = this.listCurAccAppById(id);
		} else {
			list = this.listCurAccById(id);
		}
		if (!EmptyUtils.isEmptyList(list)) {
			for (BtCurrencyDTO dto : list) {
				if (EmptyUtils.isBlank(curIds.toString())) {
					curIds.append(dto.getId());
				} else {
					curIds.append(CommonConstant.COMMA_SPLIT).append(dto.getId());
				}
			}
		}
		return curIds.toString();
	}
	public String setCurName(String id, String type) {
		StringBuilder curNames = new StringBuilder();
		List<BtCurrencyDTO> list = null;
		if ("app".equalsIgnoreCase(type)) {
			list = this.listCurAccAppById(id);
		} else {
			list = this.listCurAccById(id);
		}
		if (!EmptyUtils.isEmptyList(list)) {
			for (BtCurrencyDTO dto : list) {
				if (EmptyUtils.isBlank(curNames.toString())) {
					curNames.append(dto.getCurName());
				} else {
					curNames.append(CommonConstant.COMMA_SPLIT).append(dto.getCurName());
				}
			}
		}
		return curNames.toString();
	}
	/**
	 * 设置币别
	 *
	 * @param output
	 */
	@Override
	public DataTablesOutput setCurName(DataTablesOutput output) {
		if (null != output.getData()) {
			for (int i = 0; i < output.getData().size(); i++) {
				List<BtCurrencyDTO> list = null;
				if (output.getData().get(i) instanceof BtBankAccVO) {
					BtBankAccVO vo = (BtBankAccVO) output.getData().get(i);
					list = this.listCurAccById(vo.getId());
				} else if (output.getData().get(i) instanceof BtBankAccAppDTO) {
					BtBankAccAppDTO dto = (BtBankAccAppDTO) output.getData().get(i);
					if ("app".equals(dto.getIsForm())) {
						list = this.listCurAccAppById(dto.getId());
					} else {
						list = this.listCurAccById(dto.getId());
					}
				} else {
					BtBankAccAppVO vo = (BtBankAccAppVO) output.getData().get(i);
					if ("app".equals(vo.getIsForm())) {
						list = this.listCurAccAppById(vo.getId());
					} else {
						list = this.listCurAccById(vo.getId());
					}
				}
				// 拼接前按币种名称升序排序,保证最小币种名在最前,与 SQL 排序用的 MIN(cur.cur_name) 一致
				if (list != null && !list.isEmpty()) {
					list.sort(Comparator.comparing(BtCurrencyDTO::getCurName,
							Comparator.nullsLast(Comparator.naturalOrder())));
				}
				StringBuilder curName = new StringBuilder();
				for (int j = 0; j < list.size(); j++) {
					if (j == 0) {
						curName.append(list.get(j).getCurName());
					} else {
						curName.append(CommonConstant.COMMA_SPLIT).append(list.get(j).getCurName());
					}
				}
				if (output.getData().get(i) instanceof BtBankAccVO) {
					BtBankAccVO vo = (BtBankAccVO) output.getData().get(i);
					vo.setCurName(curName.toString());
				} else if (output.getData().get(i) instanceof BtBankAccAppDTO) {
					BtBankAccAppDTO dto = (BtBankAccAppDTO) output.getData().get(i);
					dto.setCurName(curName.toString());
				} else {
					BtBankAccAppVO vo = (BtBankAccAppVO) output.getData().get(i);
					vo.setCurName(curName.toString());
				}
			}
		}
		return output;
	}
	/**
	 * 判断是否有相同的银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
	@Override
	public boolean isBankAcc(String bankAcc) {
		BtBankAcc btBankAcc = this.btBankAccCommonDao.getBtBankAccByBankAcc(bankAcc);
		return EmptyUtils.isEmpty(btBankAcc);
	}
	/**
	 * 获取银行账号申请表币别中间表
	 *
	 * @param appId
	 * @param curId
	 * @return
	 */
	@Override
	public BtBankAccAppCurDTO getBtBankAccAppCur(String appId, String curId) {
		BtBankAccAppCur btBankAccCur = this.btBankAccCommonDao.getBtBankAccAppCur(appId, curId);
		BtBankAccAppCurDTO btBankAccAppCurVO = new BtBankAccAppCurDTO();
		BeanUtils.copyProperties(btBankAccCur, btBankAccAppCurVO);
		return btBankAccAppCurVO;
	}
	/**
	 * 获取银行账号表币别中间表
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	@Override
	public BtBankAccCurDTO getBtBankAccCur(String bankAccId, String curId) {
		BtBankAccCur btBankAccCur = this.btBankAccCommonDao.getBtBankAccCur(bankAccId, curId);
		BtBankAccCurDTO btBankAccCurVo = null;
		if(null != btBankAccCur) {
			btBankAccCurVo = new BtBankAccCurDTO();
			BeanUtils.copyProperties(btBankAccCur, btBankAccCurVo);
		}
		return btBankAccCurVo;
	}
	/**
	 * 获取有效的父帐号银行信息
	 *
	 * @return
	 */
	@Override
	public List<BtBankAccVO> listPrentBankAcc() {
		List<BtBankAccVO> list = this.btBankAccCommonDao.listPrentBankAcc();
		return list;
	}
	/**
	 * 判断是否有子账号
	 *
	 * @param prentAccId
	 * @return
	 */
	@Override
	public boolean isChildBankAccByPrentAccId(String prentAccId) {
		return this.btBankAccCommonDao.isChildBankAccByPrentAccId(prentAccId);
	}
	/**
	 * 显示账号表详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BtBankAccDTO getBtBankAccVOById(String id, String curId) {
		BtBankAccVO acc = btBankAccCommonDao.getBtBankAccVOById(id, curId);
		try {
			acc.setLoginPassword(BytterAESUtils.decrypt(acc.getLoginPassword()));
			acc.setPayPassword(BytterAESUtils.decrypt(acc.getPayPassword()));
		}catch (Exception e){
			e.getMessage();
		}
		BtBankAccDTO dto = new BtBankAccDTO();
		//获取绑定账号
		List<String> ids = nisBindingBankaccDao.listBybankId(acc.getId());
		if(EmptyUtils.isNotEmpty(ids)){
			String bindingBankAcc = "";
			String bindingBankAccStr = "";
			String bindingBankAccBankName = "";
			for(String entity : ids){
				if(EmptyUtils.isEmpty(bindingBankAcc)){
					bindingBankAcc += entity;
				}else{
					bindingBankAcc += ","+entity;
				}
				BtBankAcc btBankAcc = btBankAccCommonDao.getBtBankAcc(entity);
				if(!EmptyUtils.isEmpty(btBankAcc)) {
					if (EmptyUtils.isEmpty(bindingBankAccStr)) {
						bindingBankAccStr += btBankAcc.getBankAcc();
					} else {
						bindingBankAccStr += ","+btBankAcc.getBankAcc();
					}
					// 如果是第三方账户的，显示开户行
					BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(acc.getBankTypeId());
					if (ObjectUtil.isNotEmpty(bankTypeDTO)){
						if ("8500".equals(bankTypeDTO.getBankType())){
							if (ObjectUtil.isEmpty(bindingBankAccBankName)){
								bindingBankAccBankName += btBankAcc.getBankName();
							}else{
								bindingBankAccBankName += ","+btBankAcc.getBankName();
							}
						}
					}

				}
			}
			acc.setBindingBankAcc(bindingBankAcc);
			acc.setBindingBankAccStr(bindingBankAccStr);
			acc.setBindingBankAccBankName(bindingBankAccBankName);
		}
		if (!EmptyUtils.isEmpty(acc) && EmptyUtils.isBlank(acc.getCurId())) {
			acc.setCurId(this.setCurId(id, "acc"));
			//this.inject(vo);
		}
		if(StringUtils.isBlank(acc.getIsOnlineHandle())){
			dto.setIsOnlineHandle(CommonConstant.NO);
		}
		org.springframework.beans.BeanUtils.copyProperties(acc, dto);
		if ((EmptyUtils.isNotEmpty(acc.getCurId()))){
			List<String> curIds = Arrays.asList(acc.getCurId().split(","));
			if (curIds.size()==1){
				dto.setCurName(btCurrencyService.getObjectById(acc.getCurId()).getCurName());
			}else{
				Map<String, BtCurrencyDTO> currencyByIds = btCurrencyService.getCurrencyByIds(curIds);
				String curName = currencyByIds.values().stream()
						.map(BtCurrencyDTO::getCurName)
						.collect(Collectors.joining(","));
				dto.setCurName(curName);
			}
		}
		String accTypeName = dictServiceI.getByName("btBankAcc.accType", dto.getAccType());
		dto.setAccTypeName(accTypeName);
		//	@Dict(dictGroup = "sf_yn", inject = "isOnlineStr")
		String isOnline = dictServiceI.getByName("sf_yn", dto.getIsOnline());
		dto.setIsOnlineStr(isOnline);
		return dto;
	}
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public BtBankAccAppDTO getBtBankAccAppVOById(String id, String curId) {
		BtBankAccAppVO vo = btBankAccCommonDao.getBtBankAccAppVOById(id, curId);
		BtBankAccAppDTO dto = new BtBankAccAppDTO();
		if (!EmptyUtils.isEmpty(vo) && EmptyUtils.isBlank(vo.getCurId())) {
			vo.setCurId(this.setCurId(id, "acc"));
			//this.inject(vo);
		}
		BeanUtils.copyNotNullProperties(vo, dto);
		return dto;
	}
	/**
	 * 获取账户性质
	 *
	 * @param natureName
	 * @return
	 */
	@Override
	public String getNatureId(String natureName) {
		return this.btBankAccCommonDao.getNatureId(natureName);
	}
	/**
	 * 获取账户属性
	 *
	 * @param attributeName
	 * @return
	 */
	@Override
	public String getAttributeId(String attributeName) {
		return this.btBankAccCommonDao.getAttributeId(attributeName);
	}
	/**
	 * 获取银行账号
	 *
	 * @return
	 */
	@Override
	public List<String> getByBankAccs(Set<String> bankAccs) {
		return this.btBankAccCommonDao.getByBankAccs(bankAccs);
	}
	/**
	 * 获取所有的AccNature
	 *
	 * @return
	 */
	@Override
	public List<BtAccNatureDTO> getAllBtAccNature() {
		List<BtAccNature> list = this.btBankAccCommonDao.getAllBtAccNature();
		List<BtAccNatureDTO> listVO = BeanUtils.copyListProperties(list, BtAccNatureDTO.class);
		return listVO;
	}
	/**
	 * 获取所有账户属性
	 *
	 * @return
	 */
	@Override
	public List<BisAccAttributeDTO> getAllBisAccAttributes() {
		List<BisAccAttribute> list = this.btBankAccCommonDao.getAllBisAccAttributes();
		List<BisAccAttributeDTO> listVO = BeanUtils.copyListProperties(list, BisAccAttributeDTO.class);
		return listVO;
	}
	@Override
	public Long countEffectiveBtBankAcc(){
		return this.btBankAccCommonDao.countEffectiveBtBankAcc();
	}
	/**
	 * 通过单据类型id查询银行账户信息
	 * @param receiptCode
	 * @return
	 */
	@Override
	public List<BtBankAccVO> getBtBankAccVOByReceiptType(String receiptCode) {
		return btBankAccCommonDao.getBtBankAccVOByReceiptType(receiptCode);
	}

	/**
	 * 通过账户code查询银行账户信息
	 * @param accGroupCode
	 * @return
	 */
	@Override
	public List<BtBankAccVO> getBtBankAccVOByAccGroupCode(String accGroupCode) {
		return btBankAccCommonDao.getBtBankAccVOByAccGroupCode(accGroupCode);
	}

	@Override
	public void saveOrUpdateNew(BtBankAccDTO btBankAcc) {
		BtBankAcc acc = new BtBankAcc();
		BeanUtils.copyNotNullProperties(btBankAcc, acc);
		btBankAccCommonDao.saveOrUpdate(acc);

	}
}