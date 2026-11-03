package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billPool.dao.AbPoolSignDao;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.billPool.vo.AbPoolSignVO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
　 * @Title: 电票签收service
　 * @author jy.cao
  * @Description:AbPoolSignService.java
　 * @date 2020年2月26日
　 * @version 1.0
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_SIGN_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_SIGN_APPLY)
@Service("abPoolSignService")
@Transactional
public class AbPoolSignService  extends BaseService<AbPoolDtl, String> implements IWorkFlowListener{
	@Autowired
    private AbPoolSignDao abPoolSignDao;
	@Autowired
	private AbCorpService abCorpService;
	@Autowired
	private AbLogService abLogService;
	@Autowired
	private AbWorkFlowService abWorkFlowService;
	@Autowired
	private AbBillReceivebizService abBillReceivebizService;
	@Autowired
	private AbAccrecordsService abAccrecordsService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbPoolDiscountService abPoolDiscountService;
	@Autowired
	private AbBillNotificationService abBillNotificationService;
	@Override
	public BaseDao<AbPoolDtl, String> getEntityDao() {
		return abPoolSignDao;
	}
	/**
     * 查询待签收数据
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_SIGN_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolSignDao.listAbPoolSignTab(input,corpAuthList);
    }
	/**
	 * 功能介绍 : 保存签收信息<br>
	 *
	 * @param abPoolSignVO 签收信息
	 * @param userVo 操作用户
	 * @version 1.0
	 * @author zhenghj
	 */
	public boolean saveAbPoolSignInfo(AbPoolSignVO abPoolSignVO, ICurrentUser userVo) {
		if (abPoolSignVO != null && StringUtils.isNotEmpty(abPoolSignVO.getId())) {
			//签收id
			String[] rid = abPoolSignVO.getId().split(",");
			if(StringUtils.isNotEmpty(abPoolSignVO.getAccId())){
				String[] accId = abPoolSignVO.getAccId().split(",");
				for(int i = 0; i<accId.length;i++){
					//获取流水中的票据池明细表id,修改票据池明细表为无效
					AbAccrecords abAccrecords = abAccrecordsService.get(accId[i]);
					if(abAccrecords!=null){
						//修改票据签收流水为已删除
						abAccrecordsService.updateValidSign(abAccrecords.getId(),AccebillConstant.AB_BILL_VALID_SIGN_2,userVo);
						abBillNotificationService.completeMsg(abAccrecords,userVo);
						if(StringUtils.isNotEmpty(abAccrecords.getPdId())){
							AbPoolDtl poolDtl = abPoolSignDao.get(abAccrecords.getPdId());
							if(poolDtl!=null){
								poolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
								poolDtl.setUpdateId(userVo.getId());
								poolDtl.setUpdateTime(new Date());
								abPoolSignDao.update(poolDtl);
							}
						}
					}
				}
			}
			for (int i = 0; i < rid.length; i++) {
				AbBillReceivebizVO receivebizVO	= new  AbBillReceivebizVO();
				receivebizVO.setId(rid[i]);
				receivebizVO.setSgnupmk(AccebillConstant.AB_SGNUPMK_Y);
				//调用修改签收状态、保存票据主从表信息接口
				receivebizVO = abBillReceivebizService.billPoolSign(receivebizVO,userVo);
				AbAccrecords abAccrecords = new AbAccrecords();
				abAccrecords.setRecid(receivebizVO.getId());
				abAccrecords.setBriefId("9"+receivebizVO.getMsgtype());
				abBillNotificationService.completeMsg(abAccrecords,userVo);
				//判断是否入池
				//if(AbPoolConstant.AB_POOL_IS_INTO_1.equals(abPoolSignVO.getIsIntoPool())){
					AbPoolDtl poolDtl = new AbPoolDtl();
					poolDtl.setVvId(receivebizVO.getVvid());
					poolDtl.setOrigNo(receivebizVO.getOrigNo());
					if(AbPoolConstant.AB_POOL_IS_INTO_1.equals(abPoolSignVO.getIsIntoPool())) {
						poolDtl.setBpId(abPoolSignVO.getBpId());
					}
					poolDtl.setStatus(AbPoolConstant.AB_POOL_STATUS11);
					poolDtl.setInoutType(AbPoolConstant.AB_POOL_INOUT_TYPE_1);
					poolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
					poolDtl.setCreateTime(new Date());
					if(userVo!=null){
						poolDtl.setCreateId(userVo.getId());
					}
					abPoolSignDao.save(poolDtl);
					receivebizVO.setPdId(poolDtl.getId());
					//修改从表box_status为10 正在处理
					abVoucherinfoViceService.updateBoxStatus(receivebizVO.getVvid(),AccebillConstant.AB_BOX_STATUS_N,userVo);
					//新增票据签收流水
					abBillReceivebizService.billPoolSignAccrecords(receivebizVO,AccebillConstant.AB_BILL_VALID_SIGN_0,userVo);
					//修改签收表为已签收
					AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
					billReceivebizVo.setId(rid[i]);
					billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
					//修改签收表状态为待签收
					abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo,userVo);
					// 再次提交需要把打回从表的BackBriefId为空
					abVoucherinfoViceService.updateBackBriefIdIsNull(receivebizVO.getVvid());
					// 启动工作流
					 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_SIGN_APPLY, poolDtl.getId(), abPoolSignVO,
							receivebizVO.getCorpId(), userVo.getId());
					abLogService.saveAbLog(null, poolDtl.getId(), null, AccebillConstant.ABLOG_LOGTYPE_SIGN,
							AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
				/*}else{
					//修改从表box_status为11 已完成
					abVoucherinfoViceService.updateBoxStatus(receivebizVO.getVvid(),AccebillConstant.AB_BOX_STATUS_N,userVo);
					//新增票据签收流水
					abBillReceivebizService.billPoolSignAccrecords(receivebizVO,AccebillConstant.AB_BILL_VALID_SIGN_1,userVo);
				}*/
			}
			return true;
		}
		return false;
	}
	/**
	 * 功能介绍 : 继续或中止操作
	 * @param pdId 票据池交易明细id
	 * @param opt 操作类型 0
	 * @param userVo 操作用户
	 * @version 1.0
	 * @author zhenghj
	 */
	public boolean operation(String pdId, String opt, ICurrentUser userVo) {
		if (StringUtils.isEmpty(pdId) || StringUtils.isEmpty(opt)) {
			throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
		} else {
			AbPoolDtl poolDtl = abPoolSignDao.get(pdId);
			//根据票据从表id和票据池交易明细id查询流水信息
			AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(),poolDtl.getId());
			String[] ids={accrecords.getId()};
			String logType = "";
			String msg = "";
			//继续操作
			if(AbPoolConstant.AB_POOL_OPT_1.equals(opt)){
				//当签收指令流水失败时
				if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecords.getOptStatus())){
					// 指令状态为待发送状态
					poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
					//修改流水信息
					AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
					accRecordsVO.setIdArray(ids);
					accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
					accRecordsVO.setOptStatusOld(AccebillConstant.AB_BILL_OPT_STATUS_1);
					accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
					abAccrecordsService.batchUpdate(accRecordsVO, userVo);
				}
				//当签收流水指令状态为成功时，修改入池申请票据流水和票据池流水的指令状态为重新发送
				if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(accrecords.getOptStatus())){
					// 指令状态为银行处理中
					poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS18);
					//获取入池申请流水信息,根据签收流水查询新生成的入池流水信息
					List<AbAccrecords> list = abAccrecordsService.getAbAccrecordsbyLastid(accrecords.getId());
					if (list != null && !list.isEmpty()) {
						AbAccrecords abAccrecords =list.get(0);
						if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(abAccrecords.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS_4.equals(abAccrecords.getOptStatus())){
							String[] lastId={abAccrecords.getId()};
							//修改入池申请票据流水为待发送
							AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
							accRecordsVO.setIdArray(lastId);
							accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
							accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
							abAccrecordsService.batchUpdate(accRecordsVO, userVo);
						}else{
							//获取票据入池流水信息,根据入池申请流水查询新生成的票据入池流水信息
							List<AbAccrecords> intoList = abAccrecordsService.getAbAccrecordsbyLastid(abAccrecords.getId());
							if (intoList != null && !intoList.isEmpty()) {
									String[] lastId={intoList.get(0).getId()};
									//修改入池申请票据流水为待发送
									AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
									accRecordsVO.setIdArray(lastId);
									accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
									accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
									abAccrecordsService.batchUpdate(accRecordsVO, userVo);
							}
						}
						//修改票据池入池流水指令状态为待发送
						AbPoolDtl poolDtlInto = abPoolSignDao.get(abAccrecords.getPdId());
						if(poolDtlInto!=null){
							// 指令状态为待发送状态
							poolDtlInto.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
							poolDtlInto.setUpdateId(userVo.getId());
							poolDtlInto.setUpdateTime(new Date());
							abPoolSignDao.update(poolDtlInto);
						}
					}
				}
				logType = AccebillConstant.ABLOG_LOGTYPE_INSSEND;
				msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
				abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
						AccebillConstant.ABLOG_LOGTYPE_INSSEND, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
			//终止操作
			}else if(AbPoolConstant.AB_POOL_OPT_0.equals(opt)){
				// 指令状态为交易打回
				poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS_2);
				AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
				//修改从表box_status为11
				abVoucherinfoViceService.updateBoxStatus(accrecords.getVvid(),AccebillConstant.AB_BOX_STATUS_Y,userVo);
				//当签收流水指令状态为成功时，
				if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(accrecords.getOptStatus())){
					//获取入池申请流水信息,根据签收流水查询新生成的入池流水信息
					List<AbAccrecords> list = abAccrecordsService.getAbAccrecordsbyLastid(accrecords.getId());
					if (list != null && !list.isEmpty()) {
						AbAccrecords abAccrecords =list.get(0);
						//根据票据池交易明细id修改所有流水为无效,打回
						Map<String, Object> refMap=new HashMap<String, Object>();
						if(StringUtils.isNotEmpty(abAccrecords.getPdId())){
							refMap.put("pdId",abAccrecords.getPdId());
							refMap.put("validSign",AccebillConstant.AB_BILL_VALID_SIGN_2);
							refMap.put("optStatus",String.valueOf(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE));
							int num = abAccrecordsService.updateStatusByPdId(refMap,userVo);
						}
						//修改票据池入池流水为无效
						AbPoolDtl poolDtlInto = abPoolSignDao.get(abAccrecords.getPdId());
						if(poolDtlInto!=null){
							// 指令状态为无效状态
							poolDtlInto.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
							poolDtlInto.setUpdateId(userVo.getId());
							poolDtlInto.setUpdateTime(new Date());
							abPoolSignDao.update(poolDtlInto);
						}
						//当签收流水指令状态为失败时
						if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(abAccrecords.getOptStatus())
							|| AccebillConstant.AB_BILL_OPT_STATUS_4.equals(abAccrecords.getOptStatus())){
							//修改流水信息opt_status为-1的变成都变成-2
							accRecordsVO.setIdArray(ids);
							accRecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
							abAccrecordsService.batchUpdate(accRecordsVO, userVo);
							//回滚票据签收表待签收
							AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
							billReceivebizVo.setId(accrecords.getRecid());
							billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
							//修改签收表状态为待签收
							abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo,userVo);
						}
					}
				}
				//当签收流水指令状态为失败时
				if(AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecords.getOptStatus())){
					//修改流水信息opt_status为-1的变成都变成-2
					accRecordsVO.setIdArray(ids);
					accRecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
					abAccrecordsService.batchUpdate(accRecordsVO, userVo);
					//回滚票据签收表待签收
					AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
					billReceivebizVo.setId(accrecords.getRecid());
					billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
					//修改签收表状态为待签收
					abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo,userVo);
				}
				abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
						AccebillConstant.ABLOG_LOGTYPE_INSREPULSE, AccebillConstant.LANG_LOG_WITHDRAW_SUCC, userVo);
				logType = AccebillConstant.ABLOG_LOGTYPE_INSREPULSE;
				msg = AccebillConstant.LANG_LOG_WITHDRAW_SUCC;
			}
			poolDtl.setUpdateId(userVo.getId());
			poolDtl.setUpdateTime(new Date());
			abPoolSignDao.update(poolDtl);
			// 生成日志
			abLogService.saveAbLog(null, poolDtl.getId(), null, logType, msg, userVo);
		}
		return true;
	}
	
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		AbPoolDtl poolDtl = abPoolSignDao.get(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
		poolDtl.setStatus(status);
		String logType = "";
		String msg = "";
		//根据票据从表id和票据池交易明细id查询流水信息
		AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(),poolDtl.getId());
		if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
			//修改从表box_status为11 处理完成
			abVoucherinfoViceService.updateBoxStatus(accrecords.getVvid(),AccebillConstant.AB_BOX_STATUS_Y,user);
			// 审批通过
			// 指令状态为待发送状态
			poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
			logType = AccebillConstant.ABLOG_LOGTYPE_APP;
			msg = AccebillConstant.LANG_LOG_APP_SUCC;
			//修改票据签收流水为有效
			abAccrecordsService.updateValidSign(accrecords.getId(),AccebillConstant.AB_BILL_VALID_SIGN_1,user);
			abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
					AccebillConstant.ABLOG_LOGTYPE_SIGN, AccebillConstant.LANG_LOG_SUBMIT_SUCC, user);
		} else {
			//修改从表box_status为11 处理完成
			abVoucherinfoViceService.updateBoxStatus(poolDtl.getVvId(),AccebillConstant.AB_BOX_STATUS_Y,user);
			AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
			//修改流水信息opt_status为-2
			String[] ids={accrecords.getId()};
			accRecordsVO.setIdArray(ids);
			accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS_1);
			accRecordsVO.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
			abAccrecordsService.batchUpdate(accRecordsVO, user);
			AbBillReceivebizVO billReceivebizVo = new AbBillReceivebizVO();
			billReceivebizVo.setId(accrecords.getRecid());
			billReceivebizVo.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
			//修改签收表状态为待签收
			abBillReceivebizService.updateAbBillReceivebiz(billReceivebizVo,user);
			//审批打回设置从表打回业务和值
			abVoucherinfoViceService.updateBackBriefId(accrecords.getVvid(), accrecords.getBriefId(), accrecords.getId());
			abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
					AccebillConstant.ABLOG_LOGTYPE_SIGN, AccebillConstant.LANG_LOG_APP_BACK, user);
			// 审批打回
			logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
			msg = AccebillConstant.LANG_LOG_APP_BACK;
		}
		poolDtl.setUpdateId(user.getId());
		poolDtl.setUpdateTime(new Date());
		abPoolSignDao.update(poolDtl);
		// 生成日志
		abLogService.saveAbLog(null, poolDtl.getId(), null, logType, msg, user);
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = true)
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = abWorkFlowService
				.getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_SIGN_APPLY, dataTablesInput);
		List<String> businessList = workFlowBusiness.getBusinessIdList();
		//调用公共查询当前审批人审批
		DataTablesOutput dp = abPoolSignDao.dataTableApprove(dataTablesInput, businessList);
		return workFlowBusiness.merge(dp);
	}
	/**
	 * 根据票据池交易明细iId查询签收入池信息
	 *
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	public AbPoolSignVO getAbPoolSignVOByPdId(String pdId) {
		if (StringUtils.isNotEmpty(pdId)) {
			List<AbPoolSignVO> list = abPoolSignDao.getAbPoolSignVOByPdId(pdId);
			if (list != null && !list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}
	/**
	 * 根据ID查询签收信息
	 *
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbBillReceivebizVO getAbBillReceivebizVOById(String id) {
		return abBillReceivebizService.getAbBillReceivebizVOById(id);
	}
	/**
	 * 根据pdId获取票据池交易明细数据最新生成的lasid
	 *
	 * @param pdId 票据池交易明细id
	 * @return
	 */
	@Transactional(readOnly = true)
	public AbPoolDtl getAbPoolDtlByLastPdId(String pdId) {
		Map<String, Object> params = new HashMap<>();
		if(StringUtils.isNotEmpty(pdId)){
			params.put("lastId",pdId);
			return abPoolDiscountService.getAbPoolDtlByParams(params);
		}
		return null;
	}
}
