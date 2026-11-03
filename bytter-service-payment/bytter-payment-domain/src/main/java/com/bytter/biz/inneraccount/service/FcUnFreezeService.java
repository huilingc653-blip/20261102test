package com.bytter.biz.inneraccount.service;

import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.inneraccount.dao.FcUnFreezeDao;
import com.bytter.biz.inneraccount.entity.FcFreeze;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.FcAccLogDTO;
import com.bytter.system.api.vo.FcAccVO;
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
 * @Description: 内部账号解冻
 * @author onlineGenerator
 * @date 2017-06-16 17:31:26
 * @version 1.0
 *
 */
@Service("fcUnFreezeService")
@Transactional
public class FcUnFreezeService extends BaseService<FcFreeze, String> {
	@Autowired
	private FcAccManageService fcAccService;
	@Autowired
	private FcUnFreezeDao fcUnFreezeDao;

	@DubboReference(check = false)
	private IFcOnlineLedgerService fcOnlineLedgerService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private IFcAccLogService fcAccLogService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IDictService dictService;

	@Override
	public FcUnFreezeDao getEntityDao() {
		return fcUnFreezeDao;
	}
	@Autowired
	private FcFreezeService fcFreezeService;
	/**
	 * 保存
	 * @param entity
	 */
	public void save(FcFreeze entity){
		super.saveEntity(entity);
	}
	/**
	 * 更新
	 * @param entity
	 */
	public void update(FcFreeze entity){
		super.updateEntity(entity);
	}
	/**
	 * 保存or更新
	 * @param entity
	 */
	@Override
	public void saveOrUpdate(FcFreeze entity){
		super.saveOrUpdate(entity);
	}
	/**
	 * 根据id查对象
	 *
	 * @param id
	 * @return
	 */
	public FcFreezeVO getById(String id) {
		return fcFreezeService.getById(id);
	}
	/**
	 * 根据id删除
	 * @param id
	 */
	public void delete(String id,ICurrentUser user){
		fcFreezeService.delete(id);
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_UNBACK,id,user.getId());
	}

	/**
	 * 解冻查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput findCanModifyUnFreezes(DataTablesInput dataTablesInput, ICurrentUser userVO) {
		StringBuilder joinSql = new StringBuilder();
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY,userVO.getId()),"CORP_ID",params);
//		joinSql.append(" AND FREEZE.FREEZE_OPERATOR=:loginName");
		joinSql.append(" and ").append(corpAuthSql);
//		params.put("loginName",userVO.getLoginName());
//		DataTablesOutput output = fcUnFreezeDao.findCanModifyUnFreezes(dataTablesInput,joinSql.toString(),params);
		DataTablesOutput output = new DataTablesOutput();

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
		List<FcAccVO> fcAccVOList=fcAccService.getFcAccList(fcAcc, joinSql.toString(),params);
		if(fcAccVOList.size()>0) {
			Map<String, FcAccVO> fcAccMap = fcAccVOList.stream().collect(Collectors.toMap(FcAccVO::getBlnctrAcc, Function.identity()));
			List<String> blnctrAccs = fcAccVOList.stream().map(FcAccVO::getBlnctrAcc).collect(Collectors.toList());
			StringBuilder andSql = new StringBuilder();
			Map<String, Object> param = new HashMap();
			String accSql = SqlUtil.getInSqlByParams(blnctrAccs, "blnctr_acc", param);
			andSql.append(" and ").append(accSql);

			output = fcUnFreezeDao.findCanModifyUnFreezes(dataTablesInput,andSql.toString(),param);
			List<FcFreezeVO> lists = output.getData();
			lists.forEach(a -> {
				FcAccVO fcAccDTO = fcAccMap.get(a.getBlnctrAcc());
				a.setCorpId(fcAccDTO.getCorpId());
				a.setCorpName(fcAccDTO.getCorpName());
				a.setNetId(fcAccDTO.getNetId());
				a.setNetName(fcAccDTO.getNetName());
				a.setCurId(fcAccDTO.getCurId());
				a.setCurName(fcAccDTO.getCurName());
				a.setAccName(fcAccDTO.getAccName());
				a.setFreezeTypeName(dictService.get("fcFreeze.freezeType",a.getFreezeType()));
				a.setStateName(dictService.get("fcfreeze.status",a.getStatus()));
			});
		}

		return output;
	}
	/**
	 * 复核解冻查询数据集
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput findCanCheckUnFreezes(DataTablesInput dataTablesInput,ICurrentUser user) {
		StringBuilder joinSql = new StringBuilder();
		Map<String,Object> params=new HashMap();
		String corpAuthSql=SqlUtil.getInSqlByParams(corpService.getUserCorpIds(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY,user.getId()),"CORP_ID",params);
		joinSql.append(" and ").append(corpAuthSql);
//FREEZE.BLNCTR_ACC = fcAcc.BLNCTR_ACC
//		DataTablesOutput output = fcUnFreezeDao.findCanCheckUnFreezes(dataTablesInput,joinSql.toString(),params);
		FcAccVO fcAcc =new FcAccVO();
		List<FcAccVO> fcAccVOList=fcAccService.getFcAccList(fcAcc, joinSql.toString(),params);
		List<String> accList=new ArrayList<>();
		Map<String,FcAccVO> accMap=new HashMap<>();
		for(FcAccVO accVO:fcAccVOList){
			accList.add(accVO.getBlnctrAcc());
			accMap.put(accVO.getBlnctrAcc(),accVO);
		}
		StringBuilder andSql = new StringBuilder();
		andSql.append(" AND FREEZE.FREEZE_OPERATOR!=:loginName ");
		Map<String,Object> param=new HashMap();
		param.put("loginName",user.getLoginName());
		andSql.append(" and "+SqlUtil.getInSqlByParams(accList,"FREEZE.BLNCTR_ACC",param));
		DataTablesOutput output = fcUnFreezeDao.findCanCheckUnFreezes(dataTablesInput,andSql.toString(),param);
		List<FcFreezeVO> fcFreezeVOList= output.getData();
		for(FcFreezeVO vo:fcFreezeVOList){
			FcAccVO accVO=accMap.get(vo.getBlnctrAcc());
			vo.setAccName(accVO.getAccName());
			vo.setCurName(accVO.getCurName());
			vo.setNetId(accVO.getNetId());
			vo.setNetName(accVO.getNetName());
			vo.setCorpId(accVO.getCorpId());
			vo.setCorpName(accVO.getCorpName());
			vo.setFreezeTypeName(dictService.get("fcFreeze.freezeType",vo.getFreezeType()));
			vo.setStateName(dictService.get("fcfreeze.status",vo.getStatus()));
		}
		return output;
	}
	public List<FcFreezeListVO> findCanUnFreezes(String corpId){
		Map<String,Object> params=new HashMap();
		List<FcFreezeListVO> fcFreezeVOList=new ArrayList<>();
		FcAccVO fcAcc =new FcAccVO();
		fcAcc.setAccType(InnerAccountConstant.FCACC_BUSINESS_ACCOUNT);
		fcAcc.setCorpId(corpId);
		List<FcAccVO> fcAccVOList=fcAccService.getFcAccList(fcAcc, null,params);
		if(fcAccVOList.size()>0) {
			Map<String,FcAccVO> accMap=new HashMap<>();
			for(FcAccVO accVO:fcAccVOList){
				accMap.put(accVO.getBlnctrAcc(),accVO);
			}
			List<String> blnctrAccs = fcAccVOList.stream().map(FcAccVO::getBlnctrAcc).collect(Collectors.toList());
			fcFreezeVOList= fcUnFreezeDao.findCanUnFreezes(blnctrAccs);

			Map<String, DictDTO> dictDTOMap = dictService.listMap("fcFreeze.freezeType");
			fcFreezeVOList.forEach(item->{
				item.setFreezeTypeName(dictDTOMap.getOrDefault(item.getFreezeType(),DictDTO.EMPTY).getName());
				FcAccVO accVO=accMap.get(item.getBlnctrAcc());
				item.setNetId(accVO.getNetId());
				item.setNetName(accVO.getNetName());
				item.setCorpId(accVO.getCorpId());
				item.setCorpName(accVO.getCorpName());
				item.setAccName(accVO.getAccName());
				item.setCurId(accVO.getCurId());
				item.setCurName(accVO.getCurName());
			});
		}
		return fcFreezeVOList;
	}
	/**
	 * 查询可解冻记录
	 * @param corpId
	 * @return
	 */
	public List<FcFreezeListVO> findCanUnFreezes(String corpId,String loginName) {
//		List<FcFreezeListVO> freezes= fcUnFreezeDao.findCanUnFreezes(corpId);
		List<FcFreezeListVO> freezes= findCanUnFreezes(corpId);
		Map<String, List<FcFreezeListVO>> freezeInfos = new LinkedHashMap<String, List<FcFreezeListVO>>();
		for (FcFreezeListVO freeze : freezes) {
			List<FcFreezeListVO> cacheFreeze = freezeInfos.get(freeze.getBlnctrAcc());
			if (cacheFreeze == null) {
				cacheFreeze = new ArrayList<FcFreezeListVO>();
				freezeInfos.put(freeze.getBlnctrAcc(), cacheFreeze);
			}
			cacheFreeze.add(freeze);
		}
		//按解冻的优先级过滤冻结记录
		List<FcFreezeListVO> result = new ArrayList<FcFreezeListVO>();
		for (Map.Entry<String, List<FcFreezeListVO>> entry : freezeInfos.entrySet()) {
			List<FcFreezeListVO> finalFreezes = new ArrayList<FcFreezeListVO>();
			boolean isNotPart = false;
			for (FcFreezeListVO freeze : entry.getValue()) {
				if (freeze.getFreezeType().equals(InnerAccountConstant.FREEZE_TYPE_ALL)) {
					finalFreezes.clear();
					finalFreezes.add(freeze);
					break;
				} else if (freeze.getFreezeType().equals(InnerAccountConstant.FREEZE_TYPE_S)) {
//					finalFreezes.clear();不知道意思先关上
					finalFreezes.add(freeze);
//					isNotPart = true;
//					continue;
				} else if (!isNotPart) {
					finalFreezes.add(freeze);
				}
			}
			result.addAll(finalFreezes);
		}
		//账户排序
		Collections.sort(result, new Comparator<FcFreezeListVO>() {
			@Override
			public int compare(FcFreezeListVO o1, FcFreezeListVO o2) {
				return o1.getBlnctrAcc().compareTo(o2.getBlnctrAcc());
			}
		});
		return result;
	}
	/**
	 * 保存
	 * @param entity
	 */
	public void saveEntity(FcFreezeVO entity,ICurrentUser user){
		FcFreezeVO freeze = new FcFreezeVO();
		if(StringUtils.isEmpty(entity.getId())){
			BeanUtils.copyNotNullProperties(entity,freeze);
			freeze.setId(null);
		}else{
			freeze=fcFreezeService.getById(entity.getId());
			freeze.setExecCardNo(entity.getExecCardNo());
			freeze.setExecOfficialNo(entity.getExecOfficialNo());
			freeze.setFreezeMoney(entity.getFreezeMoney());
			freeze.setFreezeReason(entity.getFreezeReason());
			freeze.setFreezeNo(entity.getFreezeNo());
			freeze.setFreezeOrg(entity.getFreezeOrg());
			freeze.setStatus(entity.getStatus());
		}
		fcFreezeService.fixFcFreeze(freeze,user.getLoginName());
		fcFreezeService.checkNotifyNo(freeze);
		FcFreeze free=fcFreezeService.saveOrUpdate(freeze);
		freeze.setId(free.getId());
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_UNBACK,freeze.getId(),user.getId());
		if(CommonConstant.STATUE_WAIT_CHECK.equals(freeze.getStatus())) {
			//日志记录
			saveLog(freeze, entity.getOpType(), InnerAccountConstant.BIZ_TYPE_SUBMIT,user.getLoginName());
			int accAutoCheck = configService.getConfig("fcs", "logoutAutoCheck").toInteger(0).intValue();
			if (accAutoCheck == 1) {
				check(freeze,user);
				//日志记录
				saveLog(freeze, entity.getOpType(), InnerAccountConstant.BIZ_TYPE_CHECK_AUTO_SUCCESS,user.getLoginName());
			}else{
				FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(freeze.getBlnctrAcc());
				if(fcAcc !=null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
					notificationService.sendIgnoreMySelf(freeze.getId(), fcAcc.getCorpId(), NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_UNCHECK,freeze,user.getId());
				}
			}
		}else{
			//日志记录
			saveLog(freeze, entity.getOpType(), InnerAccountConstant.BIZ_TYPE_TEMP,user.getLoginName());
		}
	}
	public FcFreezeListVO getEntityById(FcFreezeVO fcFreezeVo){
		FcFreezeListVO fcFreeze = fcUnFreezeDao.getEntityById(fcFreezeVo.getId());
		Map<String,Object> params=new HashMap();
		FcAccVO fcAcc =new FcAccVO();
		fcAcc.setBlnctrAcc(fcFreezeVo.getBlnctrAcc());
		FcAccVO accVO = fcAccService.getFcAcc(fcAcc, null, params);
		fcFreeze.setCurName(accVO.getCurName());
		if (null != fcFreeze) {
			fcFreeze.setFreezeTypeName(dictService.get("fcFreeze.freezeType", fcFreeze.getFreezeType()));
			fcFreeze.setStateName(dictService.get("fcfreeze.status", fcFreeze.getStatus()));
		}
		FcOnlineLedgerDTO balance = fcOnlineLedgerService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
		if (Objects.nonNull(balance)) {
			fcFreeze.setActiveMoney(balance.getCheckBalance().subtract(balance.getFreezeMoney()));
			fcFreeze.setAccuFreezeMoney(balance.getFreezeMoney());
		}
		CorpDTO corpDTO = corpService.get(fcFreeze.getCorpId());
		CorpDTO netDTO = corpService.get(fcFreeze.getNetId());
		if(null !=corpDTO){
			fcFreeze.setCorpName(corpDTO.getName());
		}
		if(null != netDTO){
			fcFreeze.setNetName(netDTO.getName());
		}
		return fcFreeze;
	}
	/**
	 * 复核打回
	 * @param freeze
	 */
	public void back(FcFreezeVO freeze) {
		freeze.setRefuseWhy(freeze.getRefuseWhy());
		freeze.setStatus(CommonConstant.STATUE_BACK);
		fcFreezeService.update(freeze);
	}
	/**
	 * <b>功能：</b>复核解冻。<br>
	 * </b>1、更新解冻记录状态，累计冻结金额；2、更新对应冻结记录的冻结结束状态<br>
	 * </b>3、更新联机表的冻结金额；4、更新账户状态
	 * @param entity
	 */
	public void checkUnFreeze(FcFreezeVO entity,ICurrentUser user) {
		FcFreezeVO freeze = fcFreezeService.getById(entity.getId());
		if(!CommonConstant.STATUE_WAIT_CHECK.equals(freeze.getStatus())){
			throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,freeze.getBlnctrAcc());
		}
		//完成通知提醒
		notificationService.complete(NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_UNCHECK,entity.getId(),user.getId());
		if(CommonConstant.STATUE_BACK.equals(entity.getStatus())){
			freeze.setRefuseWhy(entity.getRefuseWhy());
			back(freeze);
			FcAccVO fcAcc = fcAccService.findFcAccByBlnctrAcc(freeze.getBlnctrAcc());
			if(fcAcc !=null && StringUtils.isNotEmpty(fcAcc.getCorpId())) {
				notificationService.sendToUserByLoginName(freeze.getId(), freeze.getCreateBy(), NotificationConstant.NETBANK.INNERACCOUNT_FCFREEZE_UNBACK,freeze);
			}
			//日志记录
			saveLog(freeze, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_BACK,user.getLoginName());
		}else {
			check(freeze,user);
			//日志记录
			saveLog(freeze, CommonConstant.SUBMIT_CHECK, InnerAccountConstant.BIZ_TYPE_CHECK_SUCCESS,user.getLoginName());
		}
	}
	/**
	 * 复核通过
	 * 1、更新解冻记录状态，累计冻结金额；
	 * 2、更新对应冻结记录的冻结结束状态
	 * 3、更新联机表的冻结金额；
	 * 4、更新账户状态
	 * @param  freeze
	 */
	private void check(FcFreezeVO freeze,ICurrentUser user){
		//获取解冻记录
		freeze.setStatus(CommonConstant.STATUE_CHECK);
		freeze.setCheckerCode(user.getLoginName());
		freeze.setCheckTime(Calendar.getInstance().getTime());

		if(StringUtils.isNotBlank(freeze.getUnFreezeId())) {
			//获取解冻对应的冻结记录
			FcFreezeVO oriFreeze = fcFreezeService.getById(freeze.getUnFreezeId());
			BigDecimal oriAccuFreezeMoney = oriFreeze.getAccuFreezeMoney().subtract(freeze.getFreezeMoney());
			oriFreeze.setAccuFreezeMoney(oriAccuFreezeMoney);
			freeze.setAccuFreezeMoney(oriAccuFreezeMoney);
			boolean isPartFreezeEnd = judgPartFreezeIsFreezeEnd(oriFreeze);
			if (isPartFreezeEnd) {
				oriFreeze.setFreezeEndSign(InnerAccountConstant.BOOLEAN_TYPE_YES);
			}
			FcFreeze fcFreeze = BeanUtils.copyNewInstanceProperties(oriFreeze, FcFreeze.class);
			fcFreezeService.merge(fcFreeze);
			fcOnlineLedgerService.updateFreezeMoney(freeze.getBlnctrAcc(), freeze.getFreezeMoney(), false);
			changeAccUnFreezeState(freeze, isPartFreezeEnd);
		}
		FcFreeze entity = BeanUtils.copyNewInstanceProperties(freeze, FcFreeze.class);
		fcFreezeService.merge(entity);
	}
	/**
	 * 判断部分冻结的记录是否已全部解除冻结
	 * @param freeze
	 * @return
	 */
	private boolean judgPartFreezeIsFreezeEnd(FcFreezeVO freeze) {
		return !(InnerAccountConstant.FREEZE_TYPE_PART.equals(freeze.getFreezeType()) && freeze
				.getAccuFreezeMoney().compareTo(BigDecimal.ZERO) > 0);
	}
	/**
	 * 解冻复核后，更新账户状态
	 * @param freeze
	 * @param isPartFreezeEnd
	 */
	private void changeAccUnFreezeState(FcFreezeVO freeze, boolean isPartFreezeEnd) {
		List<FcFreezeVO> otherFreezes = fcFreezeService.findAllBy(freeze);
		String accState;
		if (otherFreezes.isEmpty()) {
			if (isPartFreezeEnd) {
				accState = InnerAccountConstant.ACC_STATE_NORMAL;
			} else {
				accState = InnerAccountConstant.ACC_STATE_PART_FREEZE;
			}
		} else {
			accState = judgeAccStateByFreezes(otherFreezes);
		}
		if (accState != null) {
			fcAccService.changeState(freeze.getBlnctrAcc(), accState);
		}
	}
	/**
	 * 根据当前的冻结记录判断账号的状态
	 * @param freezes
	 * @return
	 */
	public String judgeAccStateByFreezes(List<FcFreezeVO> freezes) {
		String accState;
		boolean isHasAllFreeze = false;
		boolean isHasSFreeze = false;
		for (FcFreezeVO otherFreeze : freezes) {
			if (InnerAccountConstant.FREEZE_TYPE_S.equals(otherFreeze.getFreezeType())) {
				isHasSFreeze = true;
			} else if (InnerAccountConstant.FREEZE_TYPE_ALL.equals(otherFreeze.getFreezeType())) {
				isHasAllFreeze = true;
				break;
			}
		}
		if (isHasAllFreeze) {
			return null;
		} else if (isHasSFreeze) {
			accState = InnerAccountConstant.ACC_STATE_S;
		} else {
			accState = InnerAccountConstant.ACC_STATE_PART_FREEZE;
		}
		return accState;
	}
	/**
	 * 保存解冻日志
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
		fcAccLog.setBizType(InnerAccountConstant.BIZ_TYPE_FCUNFREEZE);
		fcAccLogService.saveLog(fcAccLog,loginName);
	}
}
