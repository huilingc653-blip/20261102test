package com.bytter.increment.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.IWorkFlowRevokeListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.constant.ChoWorkFlowConstant;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.api.enums.ChoProductMessageEnum;
import com.bytter.increment.domain.dao.ChoProductRedeemDao;
import com.bytter.increment.domain.dao.ChoProductRedeemDetailDao;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.*;
import com.bytter.increment.domain.vo.v1.ChoProductRedeemSelectDetailVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@WorkFlow(ChoWorkFlowConstant.CHO_PRODUCT_REDEEM)
@Service
@Transactional
public class ChoProductRedeemService extends BaseService<ChoProductRedeem, String> implements IWorkFlowListener, IWorkFlowRevokeListener, ICorpDisableVerifyService {
	@Autowired
	private ChoProductRedeemDao choProductRedeemDao;
	@Autowired
	private ChoProductRedeemDetailDao choProductRedeemDetailDao;
	@Autowired
	private ChoProductReqService choProductReqService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Autowired
	private ChoMessageService choMessageService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IDictService dictService;
	@Autowired
	private ChoProductNetValService choProductNetValService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	@Override
	public BaseDao<ChoProductRedeem, String> getEntityDao() {
		return choProductRedeemDao;
	}
	/**
	 * 保存对象
	 *
	 * @param entity
	 */
	public void save(ChoProductRedeem entity) {
		super.saveEntity(entity);
	}
	/**
	 * 更新对象
	 *
	 * @param entity
	 */
	public void update(ChoProductRedeem entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存或更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(ChoProductRedeem entity) {
		super.saveOrUpdate(entity);
	}
	/**
	 * 删除对象  逻辑删除
	 *
	 * @param entity
	 */
	@Override
	public void deleteEntity(ChoProductRedeem entity) {
		resertLastRedeemStatus(entity);
		entity.setStatus(ChoProductReqConstant.STATUE_DEL);
		this.saveOrUpdate(entity);
	}
	/**
	 * 根据Id删除
	 *
	 * @param id
	 */
	@Override
	public void delete(String id) {
		ChoProductRedeem entity = get(id);
		entity.setStatus(ChoProductReqConstant.STATUE_DEL);
		this.saveOrUpdate(entity);
	}
	/**
	 * 保存对象
	 *
	 * @param entity
	 * @param user
	 */
	public void saveChoProductRedeem(ChoProductRedeem entity, ChoProductRedeemBeanVO choProductRedeemBeanVO, ICurrentUser user, boolean isAuto) {
		List<ChoProductRedeemDetail> list = new ArrayList<>();
		for(int i=0; i< choProductRedeemBeanVO.getReqId().length;i++){
			ChoProductRedeemDetail choProductRedeemDetail = new ChoProductRedeemDetail();
			choProductRedeemDetail.setReqId(choProductRedeemBeanVO.getReqId()[i]);
			choProductRedeemDetail.setRedeemMoney(choProductRedeemBeanVO.getRedeemMoney()[i]);
			choProductRedeemDetail.setRedeemDate(choProductRedeemBeanVO.getRedeemDate()[i]);
			choProductRedeemDetail.setTransferDate(choProductRedeemBeanVO.getTransferDate()[i]);
			choProductRedeemDetail.setIncomeMoney(choProductRedeemBeanVO.getIncomeMoney()[i]);
			choProductRedeemDetail.setIncomeRate(choProductRedeemBeanVO.getIncomeRate()[i]);
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRmk())) {
				choProductRedeemDetail.setRmk(choProductRedeemBeanVO.getRmk()[i]);;
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getSubFeeMoney())) {
				choProductRedeemDetail.setSubFeeMoney(choProductRedeemBeanVO.getSubFeeMoney()[i]);
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemChargeMoney())) {
				choProductRedeemDetail.setRedeemChargeMoney(choProductRedeemBeanVO.getRedeemChargeMoney()[i]);
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getChargeMoney())) {
				choProductRedeemDetail.setChargeMoney(choProductRedeemBeanVO.getChargeMoney()[i]);
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getCollectionConfirm())) {
				choProductRedeemDetail.setCollectionConfirm(choProductRedeemBeanVO.getCollectionConfirm()[i]);
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemDailyNetVal())) {
				choProductRedeemDetail.setRedeemDailyNetVal(choProductRedeemBeanVO.getRedeemDailyNetVal()[i]);
			}
			if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemPortion())) {
				choProductRedeemDetail.setRedeemPortion(choProductRedeemBeanVO.getRedeemPortion()[i]);
			}
			//验证
			checkRedeemMoney(choProductRedeemDetail,entity.getFinancialType());
			list.add(choProductRedeemDetail);
		}
		entity.setBillNo(SequenceSingleton.getSequence(SequenceEnum.CHO_PRODUCT_REDEEM));
		if (StringUtils.isBlank(entity.getId())) {
			entity.setCreateBy(user.getLoginName());
			entity.setCreateDate(DateUtils.getDate());
		}
		save(entity);
		for(ChoProductRedeemDetail choProductRedeemDetail :list ){
			choProductRedeemDetail.setCprId(entity.getId());
		}
		choProductRedeemDetailDao.batchSave(list);
		boolean isCheck = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
		Integer messageType = isCheck ? ChoMessageConstant.MESSAGE_TYPE_SUBMIT : ChoMessageConstant.MESSAGE_TYPE_TEMP;
		Integer bizStatus = isCheck ?
				ChoProductReqConstant.BIZ_STATUS6 : ChoProductReqConstant.BIZ_STATUS5;
		for(ChoProductRedeemDetail choProductRedeemDetail :list ){
			ChoProductReq choProductReq = this.choProductReqService.get(choProductRedeemDetail.getReqId(), true);
			choProductReq.setBizStatus(bizStatus);
			choProductReq.setRedeemStatus(entity.getStatus());
			choProductReqService.merge(choProductReq);
		}
		this.getSession().flush();
		this.getSession().clear();
		if(!isAuto){
			notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_REDEEM, entity.getId(), user.getId());
			//生成日志
			choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM, user);
		}
		if (isCheck) {
			getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_REDEEM, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
	}
	/**
	 * 获取列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
		if (EmptyUtils.isEmpty(moneyUint)) {
			moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
		}
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREDEEM_CHOPRODUCTREDEEM_QUERY, user.getId()), "prd.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput output = choProductRedeemDao.getDataTablesUnion(dataTablesInput, sql.toString());
		// 注入远程信息
		Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
		Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
		List<ChoProductRedeemListVo> list = output.getData();
		for (ChoProductRedeemListVo product : list) {
			setBeanMoneyTypeCopy(product, moneyUint);
			// 注入远程数据
			injectValuesCopy(listTypeMap, listPayWayMap, product);
		}
		output.setData(list);
		return output;
	}

	/**
	 * 注入远程数据。
	 *
	 * @param listTypeMap 理财类型数据。
	 * @param listPayWayMap 付息方式数据。
	 * @param product 理财产品信息。
	 */
	private void injectValuesCopy(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductRedeemListVo product) {
		if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
			Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
		}
		if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
			Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
		}

	}

	/**
	 * 注入远程数据。
	 *
	 * @param listTypeMap 理财类型数据。
	 * @param listPayWayMap 付息方式数据。
	 * @param product 理财产品信息。
	 */
	private void injectValues(Map<String, DictDTO> listTypeMap, Map<String, DictDTO> listPayWayMap, ChoProductRedeemVo product) {
		if (ObjectUtil.isAllNotEmpty(product.getFinancialType(), listTypeMap)) {
			Optional.ofNullable(listTypeMap.get(product.getFinancialType())).ifPresent(dict -> product.setFinancialTypeName(dict.getName()));
		}
		if (ObjectUtil.isAllNotEmpty(product.getPayWay(), listPayWayMap)) {
			Optional.ofNullable(listPayWayMap.get(product.getPayWay())).ifPresent(dict -> product.setPayWayName(dict.getName()));
		}
		if(!EmptyUtils.isEmpty(product.getCorpId())){
			CorpDTO corpDTO = corpService.get(product.getCorpId());
			if(!EmptyUtils.isEmpty(corpDTO)){
				product.setCorpName(corpDTO.getName());
			}
		}
		if(!EmptyUtils.isEmpty(product.getCurId())){
			BtCurrencyDTO btCurrencyDTO = btCurrencyService.getObjectById(product.getCurId());
			if(!EmptyUtils.isEmpty(btCurrencyDTO)){
				product.setCurName(btCurrencyDTO.getCurName());
			}
		}
		if(!EmptyUtils.isEmpty(product.getBankTypeId())){
			BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(product.getBankTypeId());
			if(!EmptyUtils.isEmpty(bankTypeDTO)){
				product.setBankTypeName(bankTypeDTO.getTypeName());
			}
		}
	}
	/**
	 * 获取列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput, ICurrentUser user) {
		String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
		if (EmptyUtils.isEmpty(moneyUint)) {
			moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
		}
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(ResourceConstant.CHO_PRODUCTREDEEM_CHOPRODUCTREDEEM_QUERY, user.getId()), "pr.corp_id", true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput output = choProductRedeemDao.getDataTablesUnionForSelect(dataTablesInput,sql.toString());
		List<ChoProductRedeemSelectDetailVO> list = output.getData();
		for (ChoProductRedeemSelectDetailVO vo : list) {
			setBeanDetailMoneyType(vo, moneyUint);
		}
		output.setData(list);
		return output;
	}

	private void setBeanDetailMoneyType(ChoProductRedeemDetailVO ratVO, String moneyType) {
		if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
			return;
		}
		Class userCla = (Class) ratVO.getClass();
		/*
		 * 得到类中的所有属性集合
		 */
		try {
			Field[] fs = userCla.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); //设置些属性是可以访问的
				Object val = f.get(ratVO);// 得到此属性的值
				String type = f.getType().toString();// 得到此属性的类型
				String name = f.getName();//得到属性的名称
				if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
					if (null == val) {
						f.set(ratVO, BigDecimal.ZERO);
					} else {
						BigDecimal value = (BigDecimal) val;
						f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.valueOf(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void setBeanDetailMoneyType(ChoProductRedeemSelectDetailVO ratVO, String moneyType) {
		if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
			return;
		}
		Class userCla = (Class) ratVO.getClass();
		/*
		 * 得到类中的所有属性集合
		 */
		try {
			Field[] fs = userCla.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); //设置些属性是可以访问的
				Object val = f.get(ratVO);// 得到此属性的值
				String type = f.getType().toString();// 得到此属性的类型
				String name = f.getName();//得到属性的名称
				if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
					if (null == val) {
						f.set(ratVO, BigDecimal.ZERO);
					} else {
						BigDecimal value = (BigDecimal) val;
						f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.valueOf(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量提交记录
	 *
	 * @param ids
	 * @param
	 */
	public void batchSubmit(String ids, ICurrentUser user) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			ChoProductRedeem entity = this.get(id);
			entity.setStatus(ChoProductReqConstant.STATUE_WAIT_CHECK);
			this.toModify(entity,null, user);
		}
	}
	/**
	 * 更新对象
	 *
	 * @param entity
	 * @param user
	 */
	public void toModify(ChoProductRedeem entity,ChoProductRedeemBeanVO choProductRedeemBeanVO, ICurrentUser user) {
		List<ChoProductRedeemDetail> list = null;
		if(!EmptyUtils.isEmpty(choProductRedeemBeanVO)) {
			list = new ArrayList<>();
			//先删除赎回明细
			choProductRedeemDetailDao.delByCprId(entity.getId());
			for (int i = 0; i < choProductRedeemBeanVO.getReqId().length; i++) {
				ChoProductRedeemDetail choProductRedeemDetail = new ChoProductRedeemDetail();
				choProductRedeemDetail.setReqId(choProductRedeemBeanVO.getReqId()[i]);
				choProductRedeemDetail.setRedeemMoney(choProductRedeemBeanVO.getRedeemMoney()[i]);
				choProductRedeemDetail.setRedeemDate(choProductRedeemBeanVO.getRedeemDate()[i]);
				choProductRedeemDetail.setTransferDate(choProductRedeemBeanVO.getTransferDate()[i]);
				choProductRedeemDetail.setIncomeMoney(choProductRedeemBeanVO.getIncomeMoney()[i]);
				choProductRedeemDetail.setIncomeRate(choProductRedeemBeanVO.getIncomeRate()[i]);
				if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRmk())) {
					choProductRedeemDetail.setRmk(choProductRedeemBeanVO.getRmk()[i]);;
				}
				if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getSubFeeMoney())) {
					choProductRedeemDetail.setSubFeeMoney(choProductRedeemBeanVO.getSubFeeMoney()[i]);
				}
				if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemChargeMoney())) {
					choProductRedeemDetail.setRedeemChargeMoney(choProductRedeemBeanVO.getRedeemChargeMoney()[i]);
				}
				if(!EmptyUtils.isEmpty(choProductRedeemBeanVO.getChargeMoney())) {
					choProductRedeemDetail.setChargeMoney(choProductRedeemBeanVO.getChargeMoney()[i]);
				}
				if (!EmptyUtils.isEmpty(choProductRedeemBeanVO.getCollectionConfirm())) {
					choProductRedeemDetail.setCollectionConfirm(choProductRedeemBeanVO.getCollectionConfirm()[i]);
				}
				if (!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemDailyNetVal())) {
					choProductRedeemDetail.setRedeemDailyNetVal(choProductRedeemBeanVO.getRedeemDailyNetVal()[i]);
				}
				if (!EmptyUtils.isEmpty(choProductRedeemBeanVO.getRedeemPortion())) {
					choProductRedeemDetail.setRedeemPortion(choProductRedeemBeanVO.getRedeemPortion()[i]);
				}
				choProductRedeemDetail.setCprId(entity.getId());
				//验证
				checkRedeemMoney(choProductRedeemDetail, entity.getFinancialType());
				list.add(choProductRedeemDetail);
			}
			choProductRedeemDetailDao.batchSave(list);
		}else{
			list = choProductRedeemDetailDao.listbyCprId(entity.getId());
			for (int i = 0; i < list.size(); i++) {
				ChoProductRedeemDetail choProductRedeemDetail = list.get(i);
				//验证
				checkRedeemMoney(choProductRedeemDetail, entity.getFinancialType());
			}
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		boolean isCheck = ChoProductReqConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
		Integer messageType = isCheck ? ChoMessageConstant.MESSAGE_TYPE_SUBMIT : ChoMessageConstant.MESSAGE_TYPE_TEMP;
		Integer bizStatus = isCheck ? ChoProductReqConstant.BIZ_STATUS6 : ChoProductReqConstant.BIZ_STATUS5;
		for(ChoProductRedeemDetail choProductRedeemDetail :list ) {
			ChoProductReq choProductReq = this.choProductReqService.get(choProductRedeemDetail.getReqId(), true);
			choProductReq.setBizStatus(bizStatus);
			choProductReq.setRedeemStatus(entity.getStatus());
			choProductReqService.merge(choProductReq);
		}
		this.getSession().flush();
		this.getSession().clear();
		//生成日志
		choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM, user);
		notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_REDEEM, entity.getId(), user.getId());
		if (isCheck) {
			getStartHandler().handler(ChoWorkFlowConstant.CHO_PRODUCT_REDEEM, entity.getId(), entity, entity.getCorpId(), user.getId());
		}

	}
	/**
	 * 检查保存的赎回金额是否超过了可赎回的金额
	 *
	 * @param entity
	 * @return
	 */
	private boolean checkRedeemMoney(ChoProductRedeemDetail entity,String financialType) {
		// 非保本浮动收益型在前端已控制
		if (ChoProductReqConstant.FINANCIAL_TYPE2.equals(financialType)) {
			return false;
		}
		if (!checkLastTimeRedeemComplete(entity)) {
			throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_REDEEM_HAVE_UNCOMPLATE_RECORD);
		}
		BigDecimal leftMoney = getLeftRedeemMoney(entity.getReqId());
		if (leftMoney.compareTo(entity.getRedeemMoney()) == -1) {
			throw new ParameterException(ChoProductMessageEnum.CHO_PRODUCT_REDEEM_REDEEMMONEY_EXCEED_LEFTMONEY, leftMoney.toString());
		}
		return false;
	}
	/**
	 * 删除暂存的赎回记录时,还原登记记录的赎回状态
	 *
	 * @param entity
	 */
	private void resertLastRedeemStatus(ChoProductRedeem entity) {
		List<ChoProductRedeemDetail> list = choProductRedeemDetailDao.listbyCprId(entity.getId());
		for(ChoProductRedeemDetail choProductRedeemDetail :list ) {
			List<ChoProductRedeem> productRedeems = choProductRedeemDao.listRedeemUnlessDelByReqId(choProductRedeemDetail.getReqId());
			if (EmptyUtils.isEmpty(productRedeems) ||
					(productRedeems.size() == 1 && productRedeems.get(0).getId().equalsIgnoreCase(entity.getId()))) {
				//没有赎回记录或者赎回记录只有一条且为自身暂存的记录,设置状态为审批通过(理财登记)
				ChoProductReq req = choProductReqService.get(choProductRedeemDetail.getReqId());
				req.setRedeemStatus(null);
				req.setBizStatus(ChoProductReqConstant.BIZ_STATUS4);
				choProductReqService.update(req);
			} else {
				//有已完成的赎回记录,设置状态为赎回审批通过
				ChoProductReq req = choProductReqService.get(choProductRedeemDetail.getReqId());
				req.setRedeemStatus(ChoProductReqConstant.STATUE_CHECK);
				req.setBizStatus(ChoProductReqConstant.BIZ_STATUS8);
				choProductReqService.update(req);
			}
		}
	}
	/**
	 * 检查此次产品前期有无赎回记录,赎回流程是否完成
	 *
	 * @param entity
	 * @return
	 */
	private boolean checkLastTimeRedeemComplete(ChoProductRedeemDetail entity) {
		List<ChoProductRedeem> productRedeems = choProductRedeemDao.listRedeemUnlessDelByReqId(entity.getReqId());
		for (ChoProductRedeem redeem : productRedeems) {
			if (!ChoProductReqConstant.STATUE_CHECK.equals(redeem.getStatus()) && !redeem.getId().equalsIgnoreCase(entity.getCprId())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 计算该产品剩余的可赎回金额
	 *
	 * @param reqId
	 * @return
	 */
	private BigDecimal getLeftRedeemMoney(String reqId) {
		ChoProductReq productReq = choProductReqService.get(reqId);
		BigDecimal leftMoney = productReq.getBuyMoney();
		List<ChoProductRedeemDetail> choProductRedeemDetailList = choProductRedeemDao.listProductRedeemed(reqId);
		for (ChoProductRedeemDetail choProductRedeemDetail : choProductRedeemDetailList) {
			if (!choProductRedeemDetail.getId().equalsIgnoreCase(reqId)) {
				leftMoney = leftMoney.subtract(choProductRedeemDetail.getRedeemMoney());
			}
		}
		return leftMoney;
	}
	public BigDecimal getLeftMoneyByReqId(String reqId) {
		ChoProductRedeem redeem = new ChoProductRedeem();
		redeem.setReqId(reqId);
		return getLeftRedeemMoney(redeem.getReqId());
	}
	public ChoProductRedeemVo getChoProductRedeemVoById(String id) {
		ChoProductRedeemVo redeemVo = choProductRedeemDao.getChoProductRedeemVoById(id);
		Optional.ofNullable(redeemVo).ifPresent(redeem->{
			// 注入远程信息
			Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
			Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
			injectValues(listTypeMap, listPayWayMap, redeemVo);
		});
		return redeemVo;
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		ChoProductRedeem entity = this.get(workFlowResult.getBusinessKey(), true);
		Integer messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		Integer bizStatus = ChoProductReqConstant.BIZ_STATUS8;
		if (workFlowResult.isPass()) {
			messageType = ChoMessageConstant.MESSAGE_TYPE_CHECK_PASS;
			entity.setStatus(ChoProductReqConstant.STATUE_CHECK);//审批通过
		} else {
			bizStatus = ChoProductReqConstant.BIZ_STATUS7;
			entity.setStatus(ChoProductReqConstant.STATUE_BACK);//审批打回
		}
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//生成日志
		choMessageService.saveMessage(entity.getBillNo(), messageType, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM, user);
		List<ChoProductRedeemDetail> list = choProductRedeemDetailDao.listbyCprId(entity.getId());
		for (ChoProductRedeemDetail choProductRedeemDetail : list) {
			// 修改登记状态
			ChoProductReq req = new ChoProductReq();
			req.setId(choProductRedeemDetail.getReqId());
			req.setBizStatus(bizStatus);
			req.setRedeemStatus(entity.getStatus());
			if (leftMoneyIsZero(choProductRedeemDetail,entity.getStatus())) {
				req.setStatus(ChoProductReqConstant.STATUE_FINISH);
				req.setBizStatus(ChoProductReqConstant.BIZ_STATUS20);
				//当理财产品到期日期达到提醒时间,通知里显示待处理,此处关闭通知
				notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_EXPIRE, choProductRedeemDetail.getReqId(), user.getId());

				//如果是续存，根据续存模式生成一笔新的登记暂存数据
				String isStore = entity.getIsStore();
				if(!EmptyUtils.isEmpty(isStore) && isStore.equals("1")){
					Integer certificateWay = entity.getCertificateWay();
					dealIsStore(certificateWay, choProductRedeemDetail, user);
				}
			}
			choProductReqService.updateChoProductReq(req);
			this.getSession().flush();
			this.getSession().clear();
			// 如果是非保本浮动收益类型，且是否维护日净值为是，则需要维护日净值数据
			ChoProductReqVo choProductReq = choProductReqService.getChoProductReqVoById(choProductRedeemDetail.getReqId());
			if (workFlowResult.isPass() && ChoProductReqConstant.FINANCIAL_TYPE2.equals(choProductReq.getFinancialType())
					&& (StringUtils.isEmpty(choProductReq.getIsMaintainVal())
					|| CommonConstant.YES.equals(choProductReq.getIsMaintainVal()))) {
				// 赎回日期
				Date redeemDate = choProductRedeemDetail.getRedeemDate();
				// 获取当前修改日期的前一天
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(redeemDate);
				calendar.add(Calendar.DATE, -1);
				Date preDate = calendar.getTime();
				// 查询该理财产品在赎回日期前一天后的所有净值维护数据
				List<ChoProductNetVal> choProductNetValList = choProductNetValService.listByReqId(choProductRedeemDetail.getReqId(), preDate, ">=");
				// 存储更新后的净值维护实体集合
				ArrayList<ChoProductNetVal> updateNetValList = new ArrayList<>();
				// 获取当前修改日期的前一天净值维护数据
				if (!CollectionUtils.isEmpty(choProductNetValList)) {
					ChoProductNetVal lastChoProductNetVal = choProductNetValList.get(choProductNetValList.size() - 1);
					// 赎回金额
					BigDecimal redeemMoney = choProductRedeemDetail.getRedeemMoney();
					// 赎回份额
					BigDecimal redeemPortion = choProductRedeemDetail.getRedeemPortion();
					// 赎回日净值
					BigDecimal redeemDailyNetVal = choProductRedeemDetail.getRedeemDailyNetVal();
					if (lastChoProductNetVal.getNetValDate().compareTo(redeemDate) == 0) {
						// 表示是购买日期当天赎回
						lastChoProductNetVal.setBuyMoney(lastChoProductNetVal.getBuyMoney().subtract(redeemMoney));
						lastChoProductNetVal.setBuyPortion(lastChoProductNetVal.getBuyPortion().subtract(redeemPortion));
						lastChoProductNetVal.setDailyNetVal(redeemDailyNetVal);
						lastChoProductNetVal.setBal(lastChoProductNetVal.getBuyMoney());
						updateNetValList.add(lastChoProductNetVal);
					}
					// 当前赎回的收益金额
					BigDecimal incomeMoney = choProductRedeemDetail.getIncomeMoney();
					for (int i = choProductNetValList.size() - 2; i >= 0; i--) {
						// 若为当前修改日期，则使用修改后的净值维护数据
						ChoProductNetVal temp = choProductNetValList.get(i);
						// 查询该理财产品对应nextDate日期所有赎回审批通过的数据
						List<ChoProductRedeem> choProductRedeemList = listProductRedeemApproved(temp.getReqId(), temp.getNetValDate());
						// 赎回总金额
						BigDecimal redeemMoneySum = new BigDecimal(0);
						// 赎回总份额
						BigDecimal redeemPortionSum = new BigDecimal(0);
						// 上日日净值
						BigDecimal lastDayNetVal = lastChoProductNetVal.getDailyNetVal();
						temp.setBuyMoney(lastChoProductNetVal.getBuyMoney());
						temp.setBuyPortion(lastChoProductNetVal.getBuyPortion());
						if (!CollectionUtils.isEmpty(choProductRedeemList)) {
							for (ChoProductRedeem choProductRedeem : choProductRedeemList) {
								redeemMoneySum = redeemMoneySum.add(choProductRedeem.getRedeemMoney());
								redeemPortionSum = redeemPortionSum.add(choProductRedeem.getRedeemPortion());
							}
							// 购买金额 = 上日购买金额 - 本日赎回金额
							temp.setBuyMoney(lastChoProductNetVal.getBuyMoney().subtract(redeemMoneySum));
							// 购买份额 = 上日购买份额 - 本日赎回份额
							temp.setBuyPortion(lastChoProductNetVal.getBuyPortion().subtract(redeemPortionSum));
							// 当前赎回日期的收益金额大于当前日净值实际收益
							if (redeemDate.compareTo(temp.getNetValDate()) == 0 && incomeMoney.compareTo(temp.getActualIncome()) > 0) {
								// 实际收益
								temp.setActualIncome(incomeMoney);
								// 当日收益 = 实际收益 - 上日实际收益
								temp.setDayIncome(temp.getActualIncome().subtract(lastChoProductNetVal.getActualIncome()));
								// 当前日净值 = 当日收益 / 购买份额 + 上日净值
								temp.setDailyNetVal(temp.getDayIncome().divide(lastChoProductNetVal.getBuyPortion(), 6, BigDecimal.ROUND_HALF_UP).add(lastDayNetVal));
							} else {
								// 当日收益=(本日净值-上日净值)*（购买份额-赎回份额）已在购买金额中减去了赎回金额
								temp.setDayIncome(temp.getDailyNetVal().subtract(lastDayNetVal).multiply(temp.getBuyPortion()));
								// 赎回实际收益 = （上日净值-购买日净值）*赎回份额
								BigDecimal redeemIncome = lastChoProductNetVal.getDailyNetVal().subtract(choProductReq.getDailyNetVal()).multiply(redeemPortionSum);
								// 实际收益=上日实际收益-（上日净值-购买日净值）*赎回份额+当日收益
								if (BigDecimal.ZERO.compareTo(lastChoProductNetVal.getActualIncome().subtract(redeemIncome).add(temp.getDayIncome())) != 0
										&& BigDecimal.ZERO.compareTo(temp.getBuyPortion()) == 0) {
									temp.setActualIncome(BigDecimal.ZERO);
								} else {
									temp.setActualIncome(lastChoProductNetVal.getActualIncome().subtract(redeemIncome).add(temp.getDayIncome()));
								}
							}
							// 当日每万份收益=（本期日净值-上期日净值）*10000
							temp.setTenThousandIncome(temp.getDailyNetVal().subtract(lastDayNetVal).multiply(new BigDecimal(10000)));
							// 赎回余额 = （赎回份额*上日净值）
							BigDecimal redeemBal = redeemPortionSum.multiply(lastDayNetVal);
							// 余额=上日余额-（赎回份额*上日净值）+当日收益
							if (BigDecimal.ZERO.compareTo(lastChoProductNetVal.getBal().subtract(redeemBal).add(temp.getDayIncome())) != 0
									&& BigDecimal.ZERO.compareTo(temp.getBuyPortion()) == 0) {
								temp.setBal(BigDecimal.ZERO);
							} else {
								temp.setBal(lastChoProductNetVal.getBal().subtract(redeemBal).add(temp.getDayIncome()));
							}
						} else {
							// 当日收益=（本期日净值-上期日净值）*购买份额
							temp.setDayIncome(temp.getDailyNetVal().subtract(lastDayNetVal).multiply(temp.getBuyPortion()));
							// 实际收益=上日实际收益+本日当日收益
							temp.setActualIncome(lastChoProductNetVal.getActualIncome().add(temp.getDayIncome()));
							// 余额=购买金额+实际收益
							temp.setBal(temp.getBuyMoney().add(temp.getActualIncome()));
						}
						lastChoProductNetVal = temp;
						updateNetValList.add(temp);
					}
					choProductNetValService.batchUpdate(updateNetValList);
				}
			}
		}
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	/**
	 * 计算此次赎回审批通过时,此登记的产品业务是否已经赎回完成,若赎回完成设置状态为业务结束 110
	 *
	 * @param entity
	 */
	public boolean leftMoneyIsZero(ChoProductRedeemDetail entity,Integer status) {
		BigDecimal totalRedeemMoney = BigDecimal.ZERO;
		List<ChoProductRedeemDetail> redeemList = choProductRedeemDao.listProductRedeemApproved(entity.getReqId());
		ChoProductReq redeemVo = choProductReqService.get(entity.getReqId());
		if (ChoProductReqConstant.STATUE_CHECK.equals(status)) {
			totalRedeemMoney = totalRedeemMoney.add(entity.getRedeemMoney());
		} else {
			return false;
		}
		for (ChoProductRedeemDetail redeem : redeemList) {
			totalRedeemMoney = totalRedeemMoney.add(redeem.getRedeemMoney());
		}
		if (totalRedeemMoney.compareTo(redeemVo.getBuyMoney()) == 0) {
			return true;
		}
		return false;
	}
	/**
	 * 审批列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(ChoWorkFlowConstant.CHO_PRODUCT_REDEEM, dataTablesInput);
		String businessSql = null;
		if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
			businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "prd.id", true);
		}
		DataTablesOutput dp = this.choProductRedeemDao.dataTableApprove(dataTablesInput, businessSql);
		String moneyUint = (String) dataTablesInput.getFormdata().get("moneyUnit");
		if (EmptyUtils.isEmpty(moneyUint)) {
			moneyUint = ChoProductReqConstant.COMMON_MONEYUNIT_1;
		}
		// 注入远程信息
		Map<String, DictDTO> listTypeMap = dictService.listMap("financial.type");
		Map<String, DictDTO> listPayWayMap = dictService.listMap("financial.payWay");
		List<ChoProductRedeemVo> list = dp.getData();
		for (ChoProductRedeemVo vo : list) {
			setBeanMoneyType(vo, moneyUint);
			// 注入远程数据
			injectValues(listTypeMap, listPayWayMap, vo);
			//审批页面可选
			vo.setCheckboxHide("0");
		}
		dp.setData(list);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 查询指定登记id且赎回状态为审批通过和待审批的赎回记录
	 *
	 * @param reqId
	 * @return
	 */
	public List<ChoProductRedeem> listChoProductRedeemByReqId(String reqId) {
		return choProductRedeemDao.listChoProductRedeemByReqId(reqId);
	}
	/**
	 * 查询指定登记id且赎回状态为审批通过的赎回记录
	 *
	 * @param reqId
	 * @return
	 */
	public List<ChoProductRedeemDetail> listProductRedeemApproved(String reqId) {
		return choProductRedeemDao.listProductRedeemApproved(reqId);
	}
	/**
	 * 查询指定登记id且赎回状态为审批通过的赎回记录
	 *
	 * @param reqId 登记id
	 * @param date  最新净值维护日期
	 * @return 赎回集合
	 */
	public List<ChoProductRedeem> listProductRedeemApproved(String reqId, Date date) {
		return choProductRedeemDao.listProductRedeemApproved(reqId, date);
	}
	/**
	 * 查询指定登记id且赎回状态为审批通过的赎回记录
	 *
	 * @param reqIds
	 * @return
	 */
	public List<ChoProductRedeem> listRedeemedApprovedByReqIds(Set<String> reqIds) {
		return choProductRedeemDao.listRedeemedApprovedByReqIds(reqIds);
	}
	/**
	 * 根据产品的登记Id和赎回日期查询赎回记录
	 *
	 * @param reqId
	 * @param redeemDate
	 * @return
	 */
	public List<ChoProductRedeemDetail> listByReqIdRedeemDate(String reqId, Date redeemDate) {
		return choProductRedeemDao.listByReqIdRedeemDate(reqId, redeemDate);
	}

	/**
	 * 金额单位转换
	 *
	 * @param ratVO
	 * @param moneyType
	 */
	private void setBeanMoneyTypeCopy(ChoProductRedeemListVo ratVO, String moneyType) {
		if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
			return;
		}
		Class userCla = (Class) ratVO.getClass();
		/*
		 * 得到类中的所有属性集合
		 */
		try {
			Field[] fs = userCla.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); //设置些属性是可以访问的
				Object val = f.get(ratVO);// 得到此属性的值
				String type = f.getType().toString();// 得到此属性的类型
				String name = f.getName();//得到属性的名称
				if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
					if (null == val) {
						f.set(ratVO, BigDecimal.ZERO);
					} else {
						BigDecimal value = (BigDecimal) val;
						f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.valueOf(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 金额单位转换
	 *
	 * @param ratVO
	 * @param moneyType
	 */
	private void setBeanMoneyType(ChoProductRedeemVo ratVO, String moneyType) {
		if (ChoProductReqConstant.COMMON_MONEYUNIT_1.equals(moneyType)) {
			return;
		}
		Class userCla = (Class) ratVO.getClass();
		/*
		 * 得到类中的所有属性集合
		 */
		try {
			Field[] fs = userCla.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); //设置些属性是可以访问的
				Object val = f.get(ratVO);// 得到此属性的值
				String type = f.getType().toString();// 得到此属性的类型
				String name = f.getName();//得到属性的名称
				if (type.endsWith("BigDecimal") && name.endsWith("Money")) {
					if (null == val) {
						f.set(ratVO, BigDecimal.ZERO);
					} else {
						BigDecimal value = (BigDecimal) val;
						f.set(ratVO, value.divide(BigDecimal.valueOf(Integer.valueOf(moneyType)), 6, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 撤回
	 *
	 * @param workFlowResult
	 */
	@Override
	public void flowRevoke(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		ChoProductRedeem entity = this.get(workFlowResult.getBusinessKey(), true);
		entity.setStatus(ChoProductReqConstant.STATUE_TEMP);
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.saveOrUpdate(entity);
		//产品赎回时,更新该产品对应的赎回状态
		choProductReqService.updateRedeemStatus(entity.getReqId(), entity.getStatus());
		choProductReqService.updateBizStatus(entity.getReqId(), ChoProductReqConstant.BIZ_STATUS5);
		//生成日志
		choMessageService.saveMessage(entity.getBillNo(), ChoMessageConstant.MESSAGE_TYPE_CHECK_REVOKE, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM, user);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在途的理财赎回单据，不允许禁用！
			int num = this.choProductRedeemDao.getProductRedeemCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.CHOWMATISTIC,
						SysConfigConstant.BusType.CHO_PRODUCTREDEEM,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.CHO_VALID_DISABLE_PRODUCTREDEEM_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
	/**
	 * 跳转到编辑页
	 *
	 * @param choProductRedeem 赎回实体
	 * @param model            model
	 * @return 编辑页路径
	 */
	public String toUpdate(ChoProductRedeem choProductRedeem, Model model) {
		if (StringUtils.isNotEmpty(choProductRedeem.getId())) {
			//查询赎回主表
			ChoProductRedeemVo choProductRedeemVo = getChoProductRedeemVoById(choProductRedeem.getId());
			//查询赎回明细表
			List<ChoProductRedeemDetailVO> list = this.getChoProductRedeemDetailVOById(choProductRedeem.getId());
			//设置上次赎回日期和待赎回金额
			for(ChoProductRedeemDetailVO choProductRedeemDetailVO : list){
				List<ChoProductRedeemDetail> choProductRedeems = listProductRedeemApproved(choProductRedeemDetailVO.getReqId());
				if (!CommonConstant.NO.equals(choProductRedeemDetailVO.getIsMaintainVal())) { //需要维护日净值
					Date minDate = choProductRedeemDetailVO.getInterestStartDate();
					if (!CollectionUtils.isEmpty(choProductRedeems)) {
						minDate = choProductRedeems.get(choProductRedeems.size() - 1).getRedeemDate();
						choProductRedeemDetailVO.setLastRedeemDate(minDate);
					}
					String reqId = choProductRedeemDetailVO.getReqId();
					if (StringUtils.isNotEmpty(reqId) && ChoProductReqConstant.FINANCIAL_TYPE2.equals(choProductRedeemDetailVO.getFinancialType())) {
						List<ChoProductNetVal> choProductNetValList = choProductNetValService.listByReqId(reqId, choProductRedeemDetailVO.getRedeemDate(), "=");
						if (!CollectionUtils.isEmpty(choProductNetValList)) {
							choProductRedeemDetailVO.setLeftMoney(choProductNetValList.get(0).getBal());
							choProductRedeemDetailVO.setRedeemableMoney(choProductNetValList.get(0).getBuyMoney());
							choProductRedeemDetailVO.setMaxRedeemDate(choProductNetValList.get(0).getNetValDate());
							choProductRedeemDetailVO.setMaxPortion(choProductNetValList.get(0).getBuyPortion());
						}
					}
					choProductRedeemDetailVO.setMinDate(minDate);
					if(EmptyUtils.isEmpty(choProductRedeemDetailVO.getLeftMoney())){
						BigDecimal leftMoney = this.getLeftMoneyByReqId(choProductRedeemDetailVO.getReqId());
						choProductRedeemDetailVO.setLeftMoney(leftMoney);
					}
				} else {
					// 可赎回份额
					BigDecimal maxRedeemPortion = choProductRedeemDetailVO.getPortion();
					// 可赎回本金
					BigDecimal maxRedeemMoney = choProductRedeemDetailVO.getBuyMoney();
					if (!CollectionUtils.isEmpty(choProductRedeems)) {
						for (ChoProductRedeemDetail redeem : choProductRedeems) {
							maxRedeemPortion = maxRedeemPortion.subtract(redeem.getRedeemPortion());
							maxRedeemMoney = maxRedeemMoney.subtract(redeem.getRedeemMoney());
						}
					}
					choProductRedeemDetailVO.setMaxPortion(maxRedeemPortion);
					choProductRedeemDetailVO.setRedeemableMoney(maxRedeemMoney);
				}
			}

			model.addAttribute("choProductRedeemPage", choProductRedeemVo);
			model.addAttribute("list", list);
		}
		return "chowmatistic/updateChoProductRedeemBatch";
	}

	public List<ChoProductRedeemDetailVO> getChoProductRedeemDetailVOById(String id) {
		return choProductRedeemDetailDao.getChoProductRedeemDetailVOById(id);
	}

	public BigDecimal getLeftRedeemPortionByReqId(String reqId) {
		ChoProductReq productReq = choProductReqService.get(reqId);
		BigDecimal leftRedeemPortion = productReq.getPortion();
		if(EmptyUtils.isEmpty(leftRedeemPortion)){
			leftRedeemPortion = BigDecimal.ZERO;
		}
		List<ChoProductRedeemDetail> choProductRedeemDetailList = choProductRedeemDao.listProductRedeemed(reqId);
		for (ChoProductRedeemDetail choProductRedeemDetail : choProductRedeemDetailList) {
			if (!choProductRedeemDetail.getId().equalsIgnoreCase(reqId)) {
				BigDecimal redeemPortion = choProductRedeemDetail.getRedeemPortion();
				if(EmptyUtils.isEmpty(redeemPortion)){
					redeemPortion = BigDecimal.ZERO;
				}
				leftRedeemPortion = leftRedeemPortion.subtract(redeemPortion);
			}
		}
		return leftRedeemPortion;
	}

	/**
	 * 查询指定登记id且赎回状态为审批通过的赎回记录
	 *
	 * @param reqId
	 * @return
	 */
	public List<ChoProductRedeemDetailVO> listProductRedeemDetailVOApproved(String reqId) {
		return choProductRedeemDao.listProductRedeemDetailVOApproved(reqId);
	}

	/**
	 * 处理续存的数据，生成一笔登记暂存的数据
	 * @param certificateWay
	 */
	public void dealIsStore(Integer certificateWay, ChoProductRedeemDetail choProductRedeemDetail, ICurrentUser user){
		ChoProductReq choProductReq = choProductReqService.get(choProductRedeemDetail.getReqId(), true);
		if(!EmptyUtils.isEmpty(choProductReq)){
			ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(choProductRedeemDetail.getReqId());
			if(!EmptyUtils.isEmpty(choProductReqVo.getFinancialType()) && choProductReqVo.getFinancialType().equals("0")){
				ChoProductReq choProductReqNew = new ChoProductReq();
				BeanUtils.copyProperties(choProductReq, choProductReqNew);
				choProductReqNew.setId(null);
				choProductReqNew.setBillNo(SequenceSingleton.getSequence(SequenceEnum.CHO_PRODUCT_REQ));
				choProductReqNew.setStatus(ChoProductReqConstant.STATUE_TEMP);
				choProductReqNew.setBizStatus(ChoProductReqConstant.BIZ_STATUS1);
				choProductReqNew.setRedeemStatus(null);

				BigDecimal totalRedeemMoney = choProductRedeemDetail.getRedeemMoney();
				BigDecimal totalIncomeMoney = choProductRedeemDetail.getIncomeMoney();
				List<ChoProductRedeemDetail> redeemList = choProductRedeemDao.listProductRedeemApproved(choProductRedeemDetail.getReqId());
				for (ChoProductRedeemDetail redeem : redeemList) {
					totalRedeemMoney = totalRedeemMoney.add(redeem.getRedeemMoney());
					totalIncomeMoney = totalIncomeMoney.add(redeem.getIncomeMoney());
				}
				BigDecimal buyMoney = totalRedeemMoney;
				if(!EmptyUtils.isEmpty(certificateWay) && certificateWay.equals(2)){
					buyMoney = buyMoney.add(totalIncomeMoney);
				}
				choProductReqNew.setBuyMoney(buyMoney);

				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				choProductReqNew.setBuyDate(calendar.getTime());
				choProductReqNew.setInterestStartDate(calendar.getTime());
				choProductReqNew.setCreateDate(calendar.getTime());
				calendar.add(Calendar.DAY_OF_YEAR, choProductReqNew.getDayLimit());
				choProductReqNew.setDeadlineDate(calendar.getTime());

				choProductReqNew.setCreateBy(user.getLoginName());
				choProductReqNew.setUpdateBy(null);
				choProductReqNew.setUpdateDate(null);
				choProductReqNew.setAttachmentId(null);

				BigDecimal referIncomeMoney = choProductReqNew.getBuyMoney().multiply(choProductReqNew.getReferIncomeRate())
						.multiply(BigDecimal.valueOf(choProductReqNew.getDayLimit())).divide(BigDecimal.valueOf(choProductReqNew.getIncomeClause()), 2, BigDecimal.ROUND_UP);
				choProductReqNew.setReferIncomeMoney(referIncomeMoney);
				BigDecimal endReferIncomeMoney = choProductReqNew.getBuyMoney().multiply(choProductReqNew.getEndReferIncomeRate())
						.multiply(BigDecimal.valueOf(choProductReqNew.getDayLimit())).divide(BigDecimal.valueOf(choProductReqNew.getIncomeClause()), 2, BigDecimal.ROUND_UP);
				choProductReqNew.setEndReferIncomeMoney(endReferIncomeMoney);

				choProductReqService.save(choProductReqNew);

				notificationService.complete(NotificationConstant.CHOWMATISTIC.CHO_PRODUCT_REQ, choProductReqNew.getId(), user.getId());
				//生成日志
				choMessageService.saveMessage(choProductReqNew.getBillNo(), ChoMessageConstant.MESSAGE_TYPE_TEMP, "", ChoMessageConstant.BUS_TYPE_PRODUCT_REQ, user);
			}
		}
	}

	public void updateRedeemDetail(String redeemDetailId, BigDecimal incomeMoney, BigDecimal incomeRate){
		ChoProductRedeemDetail choProductRedeemDetail = choProductRedeemDetailDao.get(redeemDetailId, true);
		if(!EmptyUtils.isEmpty(choProductRedeemDetail)){
			choProductRedeemDetail.setIncomeMoney(incomeMoney);
			choProductRedeemDetail.setIncomeRate(incomeRate.setScale(8, BigDecimal.ROUND_HALF_DOWN));
			choProductRedeemDetailDao.saveOrUpdate(choProductRedeemDetail);
		}
	}
}
