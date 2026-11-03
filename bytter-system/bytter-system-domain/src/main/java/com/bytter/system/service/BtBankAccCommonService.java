package com.bytter.system.service;

import com.bytter.basic.service.BtInputBankInfoService;
import com.bytter.basic.vo.BtInputBankInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.dao.BtBankAccCommonDao;
import com.bytter.system.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Service("btBankAccCommonService")
@Transactional
public class BtBankAccCommonService extends BaseService<BtBankAcc, String> {
	@Autowired
	private IDictService dictServiceI;
	@Autowired
	private BtBankAccCommonDao btBankAccCommonDao;

	@Autowired
	private BtInputBankInfoService inputBankInfoService;


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
	public List<BtBankAcc> listEffectiveBtBankAcc() {
		return this.btBankAccCommonDao.listEffectiveBtBankAcc();
	}
	/**
	 * 获取有效的账户信息
	 *
	 * @return
	 */
	public List<BtBankAccDTO> listEffectiveBtBankAccs() {
		return this.btBankAccCommonDao.listEffectiveBtBankAccs();
	}
	/**
	 * 获取银行账户表的币别信息
	 *
	 * @param bankAccId 账户ID
	 * @return
	 */
	public List<BtBankAccCur> listBtBankAccCurById(String bankAccId) {
		return this.btBankAccCommonDao.listBtBankAccCurById(bankAccId);
	}
	/**
	 * 获取银行账户申请表的币别信息
	 *
	 * @param appId 账户ID
	 * @return
	 */
	public List<BtBankAccAppCur> listBtBankAccAppCurById(String appId) {
		return this.btBankAccCommonDao.listBtBankAccAppCurById(appId);
	}
	/**
	 * 根据id得到币别
	 *
	 * @param id
	 * @return
	 * @author zuoy 2018-02-28
	 */
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
	public List<BtCurrencyDTO> listCurAccAppById(String id) {
		return this.btBankAccCommonDao.listCurAccAppById(id);
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	public BtBankAcc getBtBankAcc(String id) {
		return this.btBankAccCommonDao.getBtBankAcc(id);
	}
	/**
	 * 根据id得到entity
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccApp getBtBankAccApp(String id) {
		return this.btBankAccCommonDao.getBtBankAccApp(id);
	}
	/**
	 * 根据账号得到银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
	public BtBankAccApp getBtBankAccAppByBankAcc(String bankAcc) {
		return this.btBankAccCommonDao.getBtBankAccAppByBankAcc(bankAcc);
	}
	/**
	 * 根据账号得到银行账号信息
	 *
	 * @param bankAcc
	 * @return
	 */
	public BtBankAcc getBtBankAccByBankAcc(String bankAcc) {
		BtBankAcc btBankAcc = this.btBankAccCommonDao.getBtBankAccByBankAcc(bankAcc);
		if (btBankAcc != null && StringUtils.isBlank(btBankAcc.getIsOnlineHandle())) {
			btBankAcc.setIsOnlineHandle(CommonConstant.NO);
		}
		return btBankAcc;
	}
	/**
	 * 显示账号表详情
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccVO getBtBankAccVOById(String id) {
		BtBankAccVO vo = this.btBankAccCommonDao.getBtBankAccVOById(id, null);
		if (!EmptyUtils.isEmpty(vo)) {
			this.setCurIdAndCurName( id,vo, "acc");
			if(StringUtils.isNotEmpty(vo.getAccType())) {
				// 银行账号-账户类别 btBankAcc.accType
				String accTypeName = dictServiceI.get("btBankAcc.accType", vo.getAccType());
				vo.setAccTypeName(accTypeName);
			}
			if(StringUtils.isNotEmpty(vo.getIsOnline())) {
				vo.setIsOnlineStr( dictServiceI.get("sf_yn", vo.getIsOnline()));
			}
		}

		return vo;
	}
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccAppVO getBtBankAccAppVOById(String id) {
		BtBankAccAppVO vo = this.btBankAccCommonDao.getBtBankAccAppVOById(id, null);
		if (!EmptyUtils.isEmpty(vo)) {
			this.setCurIdAndCurName(id,vo, "app");
		}
		return vo;
	}
	/**
	 * 设置币别
	 *
	 * @param id
	 * @return
	 */
	public void setCurIdAndCurName(String id,BtBankAccVO vo, String type) {
		StringBuilder curIds = new StringBuilder();
		StringBuilder curIdNames = new StringBuilder();
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
					curIdNames.append(dto.getCurName());
				} else {
					curIds.append(CommonConstant.COMMA_SPLIT).append(dto.getId());
					curIdNames.append(CommonConstant.COMMA_SPLIT).append(dto.getCurName());
				}
			}
		}
		vo.setCurId(curIds.toString());
		vo.setCurName(curIdNames.toString());
	}
	/**
	 * 设置币别
	 *
	 * @param id
	 * @return
	 */
	public void setCurIdAndCurName(String id,BtBankAccAppVO vo, String type) {
		StringBuilder curIds = new StringBuilder();
		StringBuilder curIdNames = new StringBuilder();
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
					curIdNames.append(dto.getCurName());
				} else {
					curIds.append(CommonConstant.COMMA_SPLIT).append(dto.getId());
					curIdNames.append(CommonConstant.COMMA_SPLIT).append(dto.getCurName());
				}
			}
		}
		vo.setCurId(curIds.toString());
		vo.setCurName(curIdNames.toString());
	}
	/**
	 * 设置币别
	 *
	 * @param output
	 */
	public void setCurName(DataTablesOutput output) {
		if (null != output.getData()) {
			for (int i = 0; i < output.getData().size(); i++) {
				List<BtCurrencyDTO> list = null;
				if (output.getData().get(i) instanceof BtBankAccVO) {
					BtBankAccVO vo = (BtBankAccVO) output.getData().get(i);
					list = this.listCurAccById(vo.getId());
				} else {
					BtBankAccAppVO vo = (BtBankAccAppVO) output.getData().get(i);
					if ("app".equals(vo.getIsForm())) {
						list = this.listCurAccAppById(vo.getId());
					} else {
						list = this.listCurAccById(vo.getId());
					}
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
				} else {
					BtBankAccAppVO vo = (BtBankAccAppVO) output.getData().get(i);
					vo.setCurName(curName.toString());
				}
			}
		}
	}
	/**
	 * 判断是否有相同的银行账号
	 *
	 * @param bankAcc
	 * @return
	 */
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
	public BtBankAccAppCur getBtBankAccAppCur(String appId, String curId) {
		return this.btBankAccCommonDao.getBtBankAccAppCur(appId, curId);
	}
	/**
	 * 获取银行账号表币别中间表
	 *
	 * @param bankAccId
	 * @param curId
	 * @return
	 */
	public BtBankAccCur getBtBankAccCur(String bankAccId, String curId) {
		return this.btBankAccCommonDao.getBtBankAccCur(bankAccId, curId);
	}
	/**
	 * 获取有效的父帐号银行信息
	 *
	 * @return
	 */
	public List<BtBankAccVO> listPrentBankAcc() {
		return this.btBankAccCommonDao.listPrentBankAcc();
	}
	/**
	 * 判断是否有子账号
	 *
	 * @param prentAccId
	 * @return
	 */
	public boolean isChildBankAccByPrentAccId(String prentAccId) {
		return this.btBankAccCommonDao.isChildBankAccByPrentAccId(prentAccId);
	}
	/**
	 * 显示账号表详情
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccVO getBtBankAccVOById(String id, String curId) {
		BtBankAccVO vo = btBankAccCommonDao.getBtBankAccVOById(id, curId);
		if (!EmptyUtils.isEmpty(vo) && EmptyUtils.isBlank(vo.getCurId())) {
			this.setCurIdAndCurName(id,vo, "acc");
		}
		return vo;
	}
	/**
	 * 显示账号申请表详情
	 *
	 * @param id
	 * @return
	 */
	public BtBankAccAppVO getBtBankAccAppVOById(String id, String curId) {
		BtBankAccAppVO vo = btBankAccCommonDao.getBtBankAccAppVOById(id, curId);
		if (!EmptyUtils.isEmpty(vo) && EmptyUtils.isBlank(vo.getCurId())) {
			this.setCurIdAndCurName(id,vo, "acc");
		}
		return vo;
	}
	/**
	 * 获取账户性质
	 *
	 * @param natureName
	 * @return
	 */
	public String getNatureId(String natureName) {
		return this.btBankAccCommonDao.getNatureId(natureName);
	}
	/**
	 * 获取账户属性
	 *
	 * @param attributeName
	 * @return
	 */
	public String getAttributeId(String attributeName) {
		return this.btBankAccCommonDao.getAttributeId(attributeName);
	}
	/**
	 * 获取银行账号
	 *
	 * @return
	 */
	public List<String> getByBankAccs(Set<String> bankAccs) {
		return this.btBankAccCommonDao.getByBankAccs(bankAccs);
	}
	/**
	 * 获取所有的AccNature
	 *
	 * @return
	 */
	public List<BtAccNature> getAllBtAccNature() {
		return this.btBankAccCommonDao.getAllBtAccNature();
	}
	/**
	 * 获取所有账户属性
	 *
	 * @return
	 */
	public List<BisAccAttribute> getAllBisAccAttributes() {
		return this.btBankAccCommonDao.getAllBisAccAttributes();
	}
	public Long countEffectiveBtBankAcc(){
		return this.btBankAccCommonDao.countEffectiveBtBankAcc();
	}

	/**
	 * 根据开户行行号获取SwiftCode
	 * @param bankCode
	 * @return
	 */
	public String getSwiftCodeByBankCode(String bankCode) {
		if (StringUtils.isNotBlank(bankCode)) {
			BtInputBankInfoVO bankInfoVO = inputBankInfoService.getByBankCode(bankCode, null);
			if (bankInfoVO != null) {
				return bankInfoVO.getSwiftCode();
			}
		}
		return "";
	}
}
