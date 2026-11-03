package com.bytter.biz.netbank.service;
import com.bytter.biz.netbank.dao.BtAssignPayWayDao;
import com.bytter.biz.netbank.dao.BtAssignPayWayUserDao;
import com.bytter.biz.netbank.entity.BtAssignPayWay;
import com.bytter.biz.netbank.entity.BtAssignPayWayUser;
import com.bytter.biz.netbank.vo.BtAssignPayWayUserVO;
import com.bytter.biz.netbank.vo.BtAssignPayWayVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpDeleteVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.dto.UserDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 付款方式分配表
 * @date 2017-05-17 10:08:00
 */
@Service("btAssignPayWayService")
@Transactional
public class BtAssignPayWayService extends BaseService<BtAssignPayWay, String> implements ICorpDeleteVerifyService {
	@Autowired
	private BtAssignPayWayDao btAssignPayWayDao;
	@Autowired
	private BtAssignPayWayUserDao btAssignPayWayUserDao;
	@DubboReference(check = false)
	private IConfigService configService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IUserService userService;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(BtAssignPayWay entity) {
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
	public void save(BtAssignPayWay entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(BtAssignPayWay entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(BtAssignPayWay entity) {
		super.saveOrUpdate(entity);
	}
	@Override
	public BaseDao<BtAssignPayWay, String> getEntityDao() {
		return btAssignPayWayDao;
	}
	/**
	 * 付款方式分配
	 *
	 * @param entity
	 * @param voucherCodes
	 */
	public void assignPayWay(BtAssignPayWayVO entity, String[] voucherCodes, ICurrentUser user) {
		String[] corpIds = entity.getCorpId().split(",");
		String userName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		for (String corpId : corpIds) {
			//先删除单位已分配的付款方式
			List<BtAssignPayWay> list = btAssignPayWayDao.listBy("corpId", corpId);
			for (BtAssignPayWay pay : list) {
				this.deleteEntity(pay);
			}
			if(!EmptyUtils.isEmpty(voucherCodes)) {
				for (String voucherCode : voucherCodes) {
					BtAssignPayWay payWay = new BtAssignPayWay();
					payWay.setCorpId(corpId);
					payWay.setVoucherCode(voucherCode);
					payWay.setDefVoucherCode(entity.getDefVoucherCode());
					payWay.setCreateBy(userName);
					payWay.setCreateDate(curDate);
					payWay.setUpdateBy(userName);
					payWay.setUpdateDate(curDate);
					this.saveOrUpdate(payWay);
				}
			}
		}
	}
	/**
	 * 付款方式分配
	 *
	 * @param entity
	 * @param voucherCodes
	 */
	public void assignPayWayUser(BtAssignPayWayUserVO entity, String[] voucherCodes, ICurrentUser user) {
		String[] userIds = entity.getUserId().split(",");
		String userName = user.getLoginName();
		Date curDate = DateUtils.getDate();
		for (String userId : userIds) {
			//先删除单位已分配的付款方式
			List<BtAssignPayWayUser> list = btAssignPayWayUserDao.listBy("userId", userId);
			for (BtAssignPayWayUser pay : list) {
				btAssignPayWayUserDao.delete(pay);
			}
			if(!EmptyUtils.isEmpty(voucherCodes)) {
				for (String voucherCode : voucherCodes) {
					BtAssignPayWayUser payWayUser = new BtAssignPayWayUser();
					payWayUser.setUserId(userId);
					payWayUser.setVoucherCode(voucherCode);
					payWayUser.setCreateBy(userName);
					payWayUser.setCreateDate(curDate);
					payWayUser.setUpdateBy(userName);
					payWayUser.setUpdateDate(curDate);
					btAssignPayWayUserDao.saveOrUpdate(payWayUser);
				}
			}
		}
	}
	/**
	 * 根据单位ID获取分配的付款方式信息
	 *
	 * @param corpId
	 * @return
	 */
	public List<BtAssignPayWayVO> getByCorpId(String corpId) {
		return btAssignPayWayDao.getByCorpId(corpId);
	}
	/**
	 * 根据单位和用户获取付款方式信息
	 * @param corpId
	 * @param userId
	 * @return
	 */
	public List<BtAssignPayWayVO> getAll(String corpId,String userId) {
		List<BtAssignPayWayVO> btAssignPayWayVOList = btAssignPayWayDao.getByCorpId(corpId);
		List<BtAssignPayWayVO> resultList = new ArrayList<>();
		//是否启用用户分配付款方式
		String isEnableUserAssign = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEUSERASSIGN).getValue();
		//如果开启则付款方式则取交集
		if(NetbankConstant.ISENABLEUSERASSIGN_1.equals(isEnableUserAssign)) {
			List<BtAssignPayWayUserVO> temp = btAssignPayWayUserDao.getByUserId(userId);
			for(BtAssignPayWayUserVO vo : temp){
				BtAssignPayWayVO  btAssignPayWayVO = new BtAssignPayWayVO();
				Boolean isHas = false;
				for(BtAssignPayWayVO payvo :btAssignPayWayVOList){
					if(payvo.getVoucherCode().equals(vo.getVoucherCode())){
						isHas = true;
						btAssignPayWayVO = payvo;
						break;
					}
				}
				if(isHas) {
					resultList.add(btAssignPayWayVO);
				}
			}
		}else{
			resultList.addAll(btAssignPayWayVOList);
		}
		return resultList;
	}
	/**
	 * 根据用户ID获取分配的付款方式信息
	 *
	 * @param userId
	 * @return
	 */
	public List<BtAssignPayWayUserVO> getByUserId(String userId) {
		return btAssignPayWayUserDao.getByUserId(userId);
	}
	/**
	 * 获取已分配单位
	 *
	 * @return
	 */
	public HashMap<String, String> getAssignCorps() {
		HashMap<String, String> map = new HashMap<String, String>();
		List<BtAssignPayWay> list = btAssignPayWayDao.listAll();
		for (BtAssignPayWay b : list) {
			if (!map.containsKey(b.getCorpId())) {
				map.put(b.getCorpId(), b.getCorpId());
			}
		}
		return map;
	}
	/**
	 * 根据付款方式是否已分配
	 *
	 * @param voucherCode
	 * @return false 存在  true 不存在
	 */
	public boolean isUsedVoucherCode(String voucherCode) {
		List<BtAssignPayWay> list = this.btAssignPayWayDao.listBy("voucherCode", voucherCode);
		return EmptyUtils.isEmptyList(list) ? false : true;
	}
	/**
	 * 判断单位是否有分配付款方式权限
	 *
	 * @param corpId
	 * @param id
	 * @return
	 */
	public boolean isExistVoucherType(String corpId, String id) {
		List<BtAssignPayWayVO> list = this.getByCorpId(corpId);
		if (!EmptyUtils.isEmptyList(list)) {
			for (BtAssignPayWayVO vo : list) {
				if (id.equals(vo.getVoucherCode())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 查询付款方式分配信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtAssignPayWayVO> getDataTablesUnion(DataTablesInput dataTablesInput) {
		DataTablesOutput<BtAssignPayWayVO> output =  this.btAssignPayWayDao.getDataTablesUnion(dataTablesInput);
		this.setVoucherName(output);
		return output;
	}
	/**
	 * 查询付款方式分配信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BtAssignPayWayUserVO> getUserDataTablesUnion(DataTablesInput dataTablesInput) {
		DataTablesOutput<BtAssignPayWayUserVO> output =  this.btAssignPayWayUserDao.getDataTablesUnion(dataTablesInput);
		this.setUserVoucherName(output);
		return output;
	}
	/**
	 * 设置付款方式
	 * @param output
	 */
	public void setVoucherName(DataTablesOutput<BtAssignPayWayVO> output){
		if(null != output.getData()){
			List<String> corpIds = btAssignPayWayDao.getCorpIds();
			Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(corpIds);
			for(int i = 0;i<output.getData().size();i++){
				BtAssignPayWayVO vo = output.getData().get(i);
				List<BtAssignPayWayVO> list = this.btAssignPayWayDao.getByCorpId(vo.getCorpId());
				StringBuilder voucherName = new StringBuilder();
				for(int j = 0;j<list.size();j++){
					if(j == 0){
						voucherName.append(list.get(j).getVoucherName());
					}else{
						voucherName.append(CommonConstant.COMMA_SPLIT).append(list.get(j).getVoucherName());
					}
					if (StringUtil.isBlank(vo.getDefVoucherName())) {
						vo.setDefVoucherName(list.get(j).getDefVoucherName());
					}
				}
				vo.setVoucherName(voucherName.toString());
				if (corpMap.containsKey(vo.getCorpId())) {
					CorpDTO corp = corpMap.get(vo.getCorpId());
					vo.setCorpCode(corp.getCode());
					vo.setCorpName(corp.getName());
				}
			}
		}
	}
	/**
	 * 设置付款方式
	 * @param output
	 */
	public void setUserVoucherName(DataTablesOutput<BtAssignPayWayUserVO>  output){
		if(null != output.getData()){
			for(int i = 0;i<output.getData().size();i++){
				BtAssignPayWayUserVO vo = output.getData().get(i);
				List<BtAssignPayWayUserVO> list = this.btAssignPayWayUserDao.getByUserId(vo.getUserId());
				StringBuilder voucherName = new StringBuilder();
				for(int j = 0;j<list.size();j++){
					if(j == 0){
						voucherName.append(list.get(j).getVoucherName());
					}else{
						voucherName.append(CommonConstant.COMMA_SPLIT).append(list.get(j).getVoucherName());
					}
				}
				vo.setVoucherName(voucherName.toString());
			}
		}
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有分配付款方式，不允许删除！
			int num = this.btAssignPayWayDao.getAssignPayWayCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_PAYWAY,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DEL_PAYWAY_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
}
