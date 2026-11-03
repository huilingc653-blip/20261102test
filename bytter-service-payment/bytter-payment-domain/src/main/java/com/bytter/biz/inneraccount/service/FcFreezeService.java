package com.bytter.biz.inneraccount.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.inneraccount.dao.FcFreezeDao;
import com.bytter.biz.inneraccount.entity.FcFreeze;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeSearchListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcFreezeDTO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.vo.FcAccVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
* @Title: Service
* @Description: 内部账号冻结解冻
* @author onlineGenerator
* @date 2017-06-16 17:31:26
* @version 1.0
*
*/
@Service("fcFreezeService")
@Transactional
public class FcFreezeService extends BaseService<FcFreeze, String> {
	@Autowired
	private FcFreezeDao fcFreezeDao;
	@Autowired
	private FcAccManageService fcAccService;
	@DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;//调核算接口
    @DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcAccLogService fcAccLogService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IFcAccService iFcAccService;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	/**
	 * 更新
	 *
	 * @param entity
	 */
	public void update(FcFreeze entity) {
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 *
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcFreeze entity) {
		super.saveOrUpdate(entity);
	}
	/**
	 * 根据id删除
	 *
	 * @param id
	 */
	public void delete(String id) {
		FcFreeze freeze = this.get(id, true);
		super.deleteEntity(freeze);
	}
	@Override
	public FcFreezeDao getEntityDao() {
		return fcFreezeDao;
	}
	public FcFreezeListVO getEntityById(String id){
		return fcFreezeDao.getEntityById(id);
	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params, ICurrentUser userVO) {
		DataTablesOutput output = fcFreezeDao.getDataTablesUnion(dataTablesInput, userVO.getLoginName(), joinSql, params);
		return output;
	}
	/**
	 * 获取有效的内部账户。
	 *
	 * @param corpId
	 * @return
	 */
	public List<FcFreezeListVO> getValidAccountByCorpId(String corpId) {
		FcAccVO fcAccVO = new FcAccVO();
		fcAccVO.setAccType(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
		fcAccVO.setCorpId(corpId);
		String joinSql = " and t.acc_State in ('"+ InnerAccountConstant.ACC_STATE_NORMAL + "','" + InnerAccountConstant.ACC_STATE_PART_FREEZE + "')";
		List<FcAccVO> fcAccList = fcAccService.getFcAccList(fcAccVO, joinSql, Maps.newHashMap());
		if (CollectionUtil.isEmpty(fcAccList)) {
			return Lists.newArrayList();
		}
		//todo 查询了冻结数据
		List<String> freeList = fcFreezeDao.getAllFreeBydBusType(InnerAccountConstant.BUS_TYPE_FREEZE).stream()
				.map(FcFreezeListVO::getBlnctrAcc).collect(Collectors.toList());
		List<FcFreezeListVO> filterFcAcc = fcAccList.stream().filter(item -> !freeList.contains(item.getBlnctrAcc())).
		map(item->BeanUtils.copyNewInstanceProperties(item,FcFreezeListVO.class)).collect(Collectors.toList());
		if(EmptyUtils.isNotEmpty(filterFcAcc)){
			Map<String, BtCurrencyDTO> currency = btCurrencyService.getCurrency();
			if(null != currency){
				List<String> blnctrAccs = filterFcAcc.stream().map(FcFreezeListVO::getBlnctrAcc).collect(Collectors.toList());
				List<FcOnlineLedgerDTO> fcOnlineLedgerDTOList=fcOnlineLedgerService.getFcOnlineLedgerByAccList(blnctrAccs);
				Map<String,FcOnlineLedgerDTO> fcOnlineLedgerDTOMap=new HashMap<>();
				for(FcOnlineLedgerDTO fcOnlineLedgerDTO:fcOnlineLedgerDTOList){
					fcOnlineLedgerDTOMap.put(fcOnlineLedgerDTO.getAccount(),fcOnlineLedgerDTO);
				}
				filterFcAcc.forEach(a -> {
					BtCurrencyDTO btCurrencyDTO = currency.get(a.getCurId());
					if(null != btCurrencyDTO){
						a.setCurName(btCurrencyDTO.getCurName());
					}
					FcOnlineLedgerDTO fcOnlineLedgerDTO=fcOnlineLedgerDTOMap.get(a.getBlnctrAcc());
					//LEDGER.CHECK_BALANCE-LEDGER.FREEZE_MONEY AS activeMoney,LEDGER.FREEZE_MONEY AS freezeMoney
					a.setActiveMoney(fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney()));
					a.setFreezeMoney(fcOnlineLedgerDTO.getFreezeMoney());
				});
			}
		}
		return filterFcAcc;
	}
	/**
	 * 获取历史账号。
	 *
	 * @param account
	 * @return
	 */
	public List<FcFreezeVO> getHisListByAccount(String account) {
		List<FcFreezeVO> freezeVOS = fcFreezeDao.getHisListByAccount(account);
		Map<String, DictDTO> dictDTOMap = dictService.listMap("fcFreeze.freezeType");
		freezeVOS.forEach(item->{
			item.setFreezeTypeName(dictDTOMap.getOrDefault(item.getFreezeType(),DictDTO.EMPTY).getName());
		});
		return freezeVOS;
	}
	/**
	 * 根据账号查
	 *
	 * @param vo
	 * @param hql
	 * @param params
	 * @return
	 */
	public List<FcFreezeVO> getFcFreezeVOList(FcFreezeVO vo,String hql,Map<String,Object> params){
		return fcFreezeDao.getFcFreezeVOList( vo, hql, params);
	}
	/**
	 * 保存
	 *
	 * @param entity
	 */
	public void saveEntity(FcFreeze entity, ICurrentUser user) {
		checkNotifyNo(entity);
		super.saveOrUpdate(entity);
	}

	public FcFreezeVO getById(String id) {
		return fcFreezeDao.getById(id);
	}

	/**
	 * 功能介绍 : 存在冻结或解冻扣划未复核的记录。<br>
	 * <b>日期：</b>2017年7月12日<br>
	 * @version 1.0
	 * @author liyh
	 * 表 fc_freeze 被迁移到结算模块
	 */
	public List<FcFreezeDTO> findNoCheckFreeze(Date closeDate, List<String> accList) {
		return fcFreezeDao.findNocheckFreeze(closeDate,accList);
	}

	/**
	 * 内部账号冻结解冻查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput, String joinSql, Map<String, Object> params) {
		return fcFreezeDao.getDataTableAll(dataTablesInput, joinSql.toString(), params);
	}
	/**
	 * 查询网点冻结 到期账户
	 *
	 * @param netId
	 * @param endDate
	 */
	public List<FcFreezeSearchListVO> getList(String netId, Date endDate, String deductSign) {
		List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByNetId(netId);
		Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
		List<String> blnctrAccs = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
		List<FcFreezeSearchListVO> listVO = fcFreezeDao.getList(blnctrAccs, endDate, deductSign);
		//f.corp_id as corpId,f.net_id as netId,f.cur_id as curId
		//f.acc_name as accName
		listVO.forEach( a -> {
			FcAccDTO fcAccDTO = map.get(a.getBlnctrAcc());
			a.setCorpId(fcAccDTO.getCorpId());
			a.setNetId(fcAccDTO.getNetId());
			a.setCurId(fcAccDTO.getCurId());
			a.setAccName(fcAccDTO.getAccName());
		});
		return listVO;
	}
	public List<FcFreezeVO> findAllBy(FcFreezeVO freeze) {
		return fcFreezeDao.findAllBy(freeze);
	}
	public DataTablesOutput findCanCheckUnFreezes(DataTablesInput dataTablesInput, String joinSql,Map<String,Object> params){
		return fcFreezeDao.findCanCheckUnFreezes( dataTablesInput,  joinSql, params);
	}




	public FcFreezeListVO getEntityById(FcFreezeVO fcFreezeVo) {
		FcFreeze fcFreeze = get(fcFreezeVo.getId(),true);
		Map<String,Object> params=new HashMap();
		FcAccVO fcAcc =new FcAccVO();
		fcAcc.setBlnctrAcc(fcFreeze.getBlnctrAcc());
		FcAccVO accVO = fcAccService.getFcAcc(fcAcc, null, params);
		FcFreezeListVO vo = new FcFreezeListVO();
		BeanUtils.copyNotNullProperties(accVO, vo);
		if (null != fcFreeze) {
			BeanUtils.copyNotNullProperties(fcFreeze, vo);
			vo.setFreezeTypeName(dictService.get("fcFreeze.freezeType", fcFreeze.getFreezeType()));
			vo.setStateName(dictService.get("fcfreeze.status", fcFreeze.getStatus()));
		}
		FcOnlineLedgerDTO balance = fcOnlineLedgerService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
		if (Objects.nonNull(balance)) {
			vo.setActiveMoney(balance.getCheckBalance().subtract(balance.getFreezeMoney()));
			vo.setAccuFreezeMoney(balance.getFreezeMoney());
		}
		return vo;
	}

	/**
	* 根据id删除
	* @param id
	*/
 	public void delete(String id,ICurrentUser user){
		delete(id);
		FcFreezeVO freeze = getById(id);
		String accState = freeze.getCurAccState();
        if (StringUtils.isNotBlank(accState)) {
            //更新账户状态为冻结前
			fcAccService.changeState(freeze.getBlnctrAcc(), accState);
			//日志记录
			saveLog(freeze, CommonConstant.SUBMIT_DELETE, InnerAccountConstant.BIZ_TYPE_FCFREEZE_INFO,user.getLoginName());
        }
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_BACK,freeze.getId(),user.getId());
 	}
	/**
	* 更新
	* @param vo
	*/
	public void update(FcFreezeVO vo){
		FcFreeze entity = BeanUtils.copyNewInstanceProperties(vo, FcFreeze.class);
		updateEntity(entity);
	}
	/**
	* 保存or更新
	* @param vo
	*/
	public FcFreeze saveOrUpdate(FcFreezeVO vo){
		FcFreeze entity = BeanUtils.copyNewInstanceProperties(vo, FcFreeze.class);
		saveOrUpdate(entity);
		return entity;
 	}
	/**
	 * 查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,ICurrentUser userVO) {
		StringBuilder joinSql=new StringBuilder();
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_QUERY,userVO.getId()),"t.corp_id",params);
		joinSql.append(" and ").append(corpAuthSql);
		//先查询出账号
		FcAccVO fcAcc =new FcAccVO();
		fcAcc.setAccType("01");
		String likes_blnctrAcc = (String) dataTablesInput.getFormdata().get("likes_blnctrAcc");
		if(StringUtils.isNotEmpty(likes_blnctrAcc)) {
			fcAcc.setBlnctrAcc(likes_blnctrAcc);
		}
		String netId = (String) dataTablesInput.getFormdata().get("eqs_netId");
		dataTablesInput.getFormdata().remove("eqs_netId");
		if(StringUtils.isNotEmpty(netId)) {
			fcAcc.setNetId(netId);
		}
		String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
		dataTablesInput.getFormdata().remove("ins_corpId");
		if(StringUtils.isNotEmpty(corpId)) {
			fcAcc.setCorpId(corpId);
		}
		String curId = (String) dataTablesInput.getFormdata().get("eqs_curId");
		dataTablesInput.getFormdata().remove("eqs_curId");
		if(StringUtils.isNotEmpty(curId)) {
			fcAcc.setCurId(curId);
		}
		List<FcAccVO> fcAccVOList=fcAccService.getFcAccList( fcAcc, joinSql.toString(),params);
		DataTablesOutput output = new DataTablesOutput();
		if(fcAccVOList.size()>0) {
			Map<String, FcAccVO> map = fcAccVOList.stream().collect(Collectors.toMap(FcAccVO::getBlnctrAcc, Function.identity()));
			List<String> blnctrAccs = fcAccVOList.stream().map(FcAccVO::getBlnctrAcc).collect(Collectors.toList());
//			//TODO 查询fc_freeze（内部账号冻结解冻） freeze_sign='C'
//			if (CollectionUtil.isEmpty(blnctrAccs)){
//				return output;
//			}
//			List<FcFreezeListVO> fcFreezeListVOList = fcFreezeDao.findCanUnFreezes(blnctrAccs);
//			if (CollectionUtil.isEmpty(fcFreezeListVOList)){
//				return output;
//			}
//			blnctrAccs = fcFreezeListVOList.stream().map(FcFreezeListVO::getBlnctrAcc).collect(Collectors.toList());
			List<FcOnlineLedgerDTO> fcOnlineLedgerDTOList=fcOnlineLedgerService.getFcOnlineLedgerByAccList(blnctrAccs);
			Map<String,BigDecimal> fcOnlineLedgerDTOMap=new HashMap<>();
			for(FcOnlineLedgerDTO fcOnlineLedgerDTO:fcOnlineLedgerDTOList){
				fcOnlineLedgerDTOMap.put(fcOnlineLedgerDTO.getAccount(),fcOnlineLedgerDTO.getFreezeMoney());
			}
			StringBuilder andSql = new StringBuilder();
			Map<String, Object> param = new HashMap();
			String accSql = SqlUtil.getInSqlByParams(blnctrAccs, "FREEZE.blnctr_acc", param);
			andSql.append(" and ").append(accSql);
			output = getDataTablesUnion(dataTablesInput,andSql.toString(),param,userVO);
			List<FcFreezeListVO> lists = output.getData();
			lists.forEach(a -> {

				FcAccVO fcAccDTO = map.get(a.getBlnctrAcc());

				a.setCorpId(fcAccDTO.getCorpId());
				a.setCorpName(fcAccDTO.getCorpName());
				a.setNetId(fcAccDTO.getNetId());
				a.setNetName(fcAccDTO.getNetName());
				a.setCurId(fcAccDTO.getCurId());
				a.setCurName(fcAccDTO.getCurName());
				a.setAccName(fcAccDTO.getAccName());
				a.setAccuFreezeMoney(fcOnlineLedgerDTOMap.get(a.getBlnctrAcc()));
			});
		}
		return output;
	}

	public FcFreezeVO getFcFreezeVO(FcFreezeVO vo,String hql,Map<String,Object> params){
		List<FcFreezeVO> list= getFcFreezeVOList(vo,hql,params);
		if(list.size()==1){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 查询可解冻记录
	 * @param accList
	 * @return
	 */
	public List<FcFreezeListVO> findCanUnFreezes(List<String> accList) {
		return fcFreezeDao.findCanUnFreezes(accList);
	}
	public void saveEntity(FcFreezeVO freeze) {
		saveOrUpdate(freeze);
	}
	/**
	 * 保存
	 * @param freeze
	 */
	public void saveEntity(FcFreezeVO freeze,ICurrentUser user){
		fixFcFreeze(freeze,user.getLoginName());
		if (CommonConstant.SUBMIT_ADD.equals(freeze.getOpType())) {
			freeze.setId(null);
		}
        if (freeze.getFreezeSign().equals(InnerAccountConstant.BUS_TYPE_FREEZE)){
			FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(freeze.getBlnctrAcc());
            freeze.setCurAccState(fcAcc.getAccState());
            //更新账户状态为冻结申请
			//修改只有提交时才会更新账户状态
			if(CommonConstant.STATUE_WAIT_CHECK.equals(freeze.getStatus())){
				fcAccService.changeState(freeze.getBlnctrAcc(),InnerAccountConstant.ACC_STATE_V);
			}
        }
        checkNotifyNo(freeze);
		//可解冻金额 = 冻结金额-已解冻金额
		freeze.setUnFreezeMoney(freeze.getFreezeMoney());
		FcFreeze fcFreeze = saveOrUpdate(freeze);
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_BACK,fcFreeze.getId(),user.getId());
		if(CommonConstant.STATUE_WAIT_CHECK.equals(fcFreeze.getStatus())) {
			//日志记录
			saveLog(freeze, freeze.getOpType(), InnerAccountConstant.BIZ_TYPE_SUBMIT,user.getLoginName());
			int accAutoCheck = configService.getConfig("fcs", "freezeAutoCheck").toInteger(0).intValue();
			if (accAutoCheck == 1) {
				check(fcFreeze,user);
				//日志记录
				saveLog(freeze, freeze.getOpType(), InnerAccountConstant.BIZ_TYPE_CHECK_AUTO_SUCCESS,user.getLoginName());
			}else{
				FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(freeze.getBlnctrAcc());
				if(fcAcc !=null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
					notificationService.sendIgnoreMySelf(freeze.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_CHECK,freeze,user.getId());
				}
			}
		}else{
			//日志记录
			saveLog(freeze, freeze.getOpType(), InnerAccountConstant.BIZ_TYPE_TEMP,user.getLoginName());
		}
	}
    /**
     * 知书重复校验
     * @param freeze
     */
	public void checkNotifyNo(FcFreeze freeze) {
        List<FcFreeze> list=fcFreezeDao.checkNotifyNo(freeze);
		String title;
        if(InnerAccountConstant.BUS_TYPE_FREEZE.equals(freeze.getFreezeSign())){
			title = dictService.get("innerAccount.freezeSign",InnerAccountConstant.BUS_TYPE_FREEZE);
		}else{
			title = dictService.get("innerAccount.freezeSign",InnerAccountConstant.BUS_TYPE_UNFREEZE);
		}
        if (!list.isEmpty() && list.size()>0 ) {
            throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOTIFYNO_EXISTS,title,freeze.getFreezeNo());
        }
    }
	/**
	 * 知书重复校验
	 *
	 * @param vo
	 */
	public void checkNotifyNo(FcFreezeVO vo) {
		FcFreeze freeze = BeanUtils.copyNewInstanceProperties(vo, FcFreeze.class);
		checkNotifyNo(freeze);
	}
	/**
	 * 复核
	 * 复核冻结，1、更新状态；2、设置累计冻结金额为冻结金额；3、更新联机表的冻结金额；4、更新账户状态
	 * @param entity
	 */
	public void checkEntity(FcFreezeVO entity,ICurrentUser user){
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_CHECK,entity.getId(),user.getId());
		FcFreezeVO acc = getById(entity.getId());
		if(!CommonConstant.STATUE_WAIT_CHECK.equals(acc.getStatus())){
			throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,acc.getBlnctrAcc());
		}
		if(CommonConstant.STATUE_CHECK.equals(entity.getStatus())){
			check(BeanUtils.copyNewInstanceProperties(acc, FcFreeze.class),user);
			//日志记录
			saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS,user.getLoginName());
        }else{
			acc.setCheckerCode(user.getLoginName());
			acc.setCheckTime(DateUtils.getDate());
			acc.setRefuseWhy(entity.getRefuseWhy());
			acc.setStatus(entity.getStatus());
			acc.setUpdateBy(user.getLoginName());
			acc.setUpdateDate(DateUtils.getDate());
			update(acc);
			FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(acc.getBlnctrAcc());
			if(fcAcc !=null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
				notificationService.sendToUserByLoginName(acc.getId(), acc.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_BACK,acc);
			}
			//日志记录
			saveLog(acc, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_BACK,user.getLoginName());
        }
	}
	/**
	 * 复核通过
	 * @param entity
	 */
	private void check(FcFreeze entity,ICurrentUser user){
		entity.setCheckerCode(user.getLoginName());
		entity.setCheckTime(DateUtils.getDate());
		entity.setStatus(CommonConstant.STATUE_CHECK);
		//2修改累计冻结金额 = 已冻结金额 + 原冻结金额
		FcOnlineLedgerVO fcOnlineLedger=fcOnlineLedgerService.getEntityByAccount(entity.getBlnctrAcc());
		BigDecimal freezeMoney = entity.getFreezeMoney().add(fcOnlineLedger.getFreezeMoney());
		entity.setAccuFreezeMoney(freezeMoney);
		update(entity);
		//3、更新联机表的冻结金额
		fcOnlineLedgerService.updateFreezeMoney(fcOnlineLedger, entity.getFreezeMoney(), true,user.getLoginName());
		//4、更新账户状态
		changeAccFreezeState(entity);
	}
	/**
	 * 设置冻结/解冻的基础信息
	 * @param freeze
	 */
	public void fixFcFreeze(FcFreezeVO freeze,String loginName) {
		Date curDate=DateUtils.getCalendar().getTime();
		freeze.setFreezeOperator(loginName);
		freeze.setOperateTime(curDate);
		freeze.setCreateBy(loginName);
		freeze.setCreateDate(curDate);
		if (InnerAccountConstant.BUS_TYPE_FREEZE.equals(freeze.getFreezeSign())) {
			freeze.setFreezeEndSign(InnerAccountConstant.BOOLEAN_TYPE_NO);
		}else if (InnerAccountConstant.BUS_TYPE_UNFREEZE.equals(freeze.getFreezeSign())) {
			freeze.setFreezeEndSign(InnerAccountConstant.BOOLEAN_TYPE_YES);
		} else {
			freeze.setFreezeEndSign(InnerAccountConstant.BOOLEAN_TYPE_NO);
		}
	}
	/**
	 * 冻结复核后，更新账户状态
	 * @param freeze
	 */
	private void changeAccFreezeState(FcFreeze freeze) {
		String accState;
		if (InnerAccountConstant.FREEZE_TYPE_PART.equals(freeze.getFreezeType())) {
			accState = InnerAccountConstant.ACC_STATE_PART_FREEZE;
		} else if (InnerAccountConstant.FREEZE_TYPE_S.equals(freeze.getFreezeType())) {
			accState = InnerAccountConstant.ACC_STATE_S;
		} else {
			accState = InnerAccountConstant.ACC_STATE_ALL_FREEZE;
		}
        fcAccService.changeState(freeze.getBlnctrAcc(), accState);
	}
	/**
	 * 内部账号冻结解冻查询
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput getDataTableAll(DataTablesInput dataTablesInput,ICurrentUser userVO) {
		StringBuilder joinSql=new StringBuilder();
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_ALLQUERY,userVO.getId()),"t.corp_Id",params);
		joinSql.append(" and ").append(corpAuthSql);
//		DataTablesOutput output = fcFreezeDao.getDataTableAll(dataTablesInput,joinSql.toString(),params);
		//先查询出账号
		FcAccVO fcAcc =new FcAccVO();
		fcAcc.setAccType("01");
		String likes_blnctrAcc = (String) dataTablesInput.getFormdata().get("likes_blnctrAcc");
		if(StringUtils.isNotEmpty(likes_blnctrAcc)) {
			fcAcc.setBlnctrAcc(likes_blnctrAcc);
		}
		String netid = (String) dataTablesInput.getFormdata().get("eqs_netId");
		if(StringUtils.isNotEmpty(netid)) {
			fcAcc.setNetId(netid);
			dataTablesInput.getFormdata().remove("eqs_netId");
		}
		String corpId = (String) dataTablesInput.getFormdata().get("ins_corpId");
		if(StringUtils.isNotEmpty(corpId)) {
			fcAcc.setCorpId(corpId);
			dataTablesInput.getFormdata().remove("ins_corpId");
		}
		String curId = (String) dataTablesInput.getFormdata().get("eqs_curId");
		if(StringUtils.isNotEmpty(curId)) {
			fcAcc.setCurId(curId);
			dataTablesInput.getFormdata().remove("eqs_curId");
		}
		List<FcAccVO> fcAccVOList=fcAccService.getFcAccList( fcAcc, joinSql.toString(),params);
		DataTablesOutput output = new DataTablesOutput();
		if(fcAccVOList.size()>0) {
			Map<String, FcAccVO> map = fcAccVOList.stream().collect(Collectors.toMap(FcAccVO::getBlnctrAcc, Function.identity()));
			List<String> blnctrAccs = fcAccVOList.stream().map(FcAccVO::getBlnctrAcc).collect(Collectors.toList());
			StringBuilder andSql = new StringBuilder();
			Map<String, Object> param = new HashMap();
			String accSql = SqlUtil.getInSqlByParams(blnctrAccs, "t.blnctr_acc", param);
			andSql.append(" and ").append(accSql);
			output = getDataTableAll(dataTablesInput, andSql.toString(), param);
			List<FcFreezeSearchListVO> lists = output.getData();
			lists.forEach(a -> {
				FcAccVO fcAccDTO = map.get(a.getBlnctrAcc());
				a.setCorpId(fcAccDTO.getCorpId());
				a.setCorpName(fcAccDTO.getCorpName());
				a.setNetId(fcAccDTO.getNetId());
				a.setNetName(fcAccDTO.getNetName());
				a.setCurId(fcAccDTO.getCurId());
				a.setCurName(fcAccDTO.getCurName());
				a.setAccName(fcAccDTO.getAccName());
			});
		}
		return output;
	}
	/**
	 *
	 * 查询网点冻结 到期账户
	 * @param netId
	 * @param endDate
	 */
	public List<FcFreezeSearchListVO> getList(String netId, Date endDate){
		List<FcFreezeSearchListVO> listVO=getList( netId, endDate,null);
		return listVO;
	}
	/**
	 * 保存冻结日志
	 * @param acc
	 * @param opType　操作类型
	 * @param bizMsg　业务信息
	 */
	private void saveLog(FcFreezeVO acc,String opType,String bizMsg,String loginName){
		FcAccLogDTO fcAccLog = new FcAccLogDTO();
		fcAccLog.setBizId(acc.getId());
		fcAccLog.setOpType(opType);
		fcAccLog.setAccType("");
		fcAccLog.setBizMsg(bizMsg);
		fcAccLog.setAccount(acc.getBlnctrAcc());
		fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_FCFREEZE);
		fcAccLogService.saveLog(fcAccLog,loginName);
	}

	/**
	 * 内部账户冻结到期 提醒
	 * @param arguments
	 */
	@XxlJob("FcFreezeService")
	public void run(String arguments) {
		int freezeUnitAlertDays = configService.getConfig("fcs","freezeUnitAlertDays").toInteger(0).intValue();
		List<FcFreezeSearchListVO> listVO= fcFreezeDao.getFreezeDueList(null, null,CommonConstant.SEND_SIGN_NO);
		for(FcFreezeSearchListVO vo : listVO){
			Date date = DateUtils.addDay(vo.getEndDate(),0-freezeUnitAlertDays);
			if(DateUtils.getDateYMD().compareTo(DateUtils.getDateYMD(date)) == 0
					|| DateUtils.getDateYMD().compareTo(DateUtils.getDateYMD(date)) > 0){ //消息提醒
				notificationService.send(vo.getId(),vo.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_EXPIRE, vo);
				logger.info("内部账户冻结到期提醒，编码："+vo.getFreezeNo()+"，账号："+vo.getBlnctrAcc());
				FcFreezeVO fcFreeze = getById(vo.getId());
				fcFreeze.setDeductSign(CommonConstant.SEND_SIGN_YES);
				update(fcFreeze);
			}
		}
	}
}
