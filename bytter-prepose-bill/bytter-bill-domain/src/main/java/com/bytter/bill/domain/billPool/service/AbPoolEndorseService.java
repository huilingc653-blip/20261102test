package com.bytter.bill.domain.billPool.service;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.api.billPool.constant.BillPoolWorkFlowConstant;
import com.bytter.bill.api.billPool.enums.AbPoolMessageEnum;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.entity.AbVoucherinfoVice;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.billPool.dao.AbPoolEndorseDao;
import com.bytter.bill.domain.billPool.entity.AbPoolDtl;
import com.bytter.bill.domain.billPool.vo.AbPooIntoBeanVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEndorseVO;
import com.bytter.bill.domain.billPool.vo.AbPoolVO;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 背书申请service
 * @Description:背书申请业务处理类
 * @date 2020年3月6日
 */
@WorkFlow(BillPoolWorkFlowConstant.AB_POLL_ENDORSE_APPLY)
@DubboService(interfaceClass = IWorkFlowListener.class, group = BillPoolWorkFlowConstant.AB_POLL_ENDORSE_APPLY)
@Service("abPoolEndorseService")
@Transactional
public class AbPoolEndorseService extends BaseService<AbPoolDtl, String> implements IWorkFlowListener, ITaskService {
    @Autowired
    private AbPoolEndorseDao abPoolEndorseDao; 
    @Autowired
    private AbPoolService abPoolService;
    @Autowired
    private AbCorpService abCorpService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbWorkFlowService abWorkFlowService;
    @Autowired
    private AbAccrecordsService abAccrecordsService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbVoucherinfoService abVoucherinfoService;
    @Autowired
    private AbPoolOutApplyService abPoolOutApplyService;
    @Autowired
    private AbPoolDiscountService abPoolDiscountService;
    @DubboReference(check = false)
	private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
	private IUserService uerService;
    @DubboReference(check = false)
	private IAccebillService accebillService;
    @Override
    public BaseDao<AbPoolDtl, String> getEntityDao() {
        return abPoolEndorseDao;
    }
    /**
     * 查询背书转让数据
     *
     * @param
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTablesUnion(DataTablesInput input) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = abCorpService.getUserCorpIds(AbPoolConstant.AB_POLL_ENDORSE_RESOURCE_OP,
                input.getCurrentUserId());
        return abPoolEndorseDao.listAbPoolEndorseTab(input, corpAuthList);
    }
    /**
     * 功能介绍 : 保存背书转让信息<br>
     *
     * @param abPoolEndorseVO 背书信息
     * @param userVo          操作用户
     * @version 1.0
     * @author zhenghj
     */
    public boolean saveAbPoolEndorseInfo(AbPoolEndorseVO abPoolEndorseVO, ICurrentUser userVo) {
        if (abPoolEndorseVO != null && StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
            //票据池交易明细id
            String[] id = abPoolEndorseVO.getId().split(",");
            StringBuilder billIds=new StringBuilder();
            for (int i = 0; i < id.length; i++) {
                AbPoolDtl poolDtlNew = new AbPoolDtl();
                AbPoolDtl poolDtlold = abPoolEndorseDao.get(id[i]);
                List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
                //修改交易流水为在途
                poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
                poolDtlold.setUpdateTime(new Date());
                //新增背书申请交易流水
                poolDtlNew.setAppNo(poolDtlold.getAppNo());
                poolDtlNew.setVvId(poolDtlold.getVvId());
                poolDtlNew.setOrigNo(poolDtlold.getOrigNo());
                poolDtlNew.setBpId(poolDtlold.getBpId());
                poolDtlNew.setStatus(AbPoolConstant.AB_POOL_STATUS11);
                poolDtlNew.setInoutType(poolDtlold.getInoutType());
                poolDtlNew.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_0);
                poolDtlNew.setLastid(poolDtlold.getId());
                poolDtlNew.setBriefId(AbPoolConstant.AB_POOL_BRIEF_010);
                poolDtlNew.setCreateTime(new Date());
                if (userVo != null) {
                    poolDtlNew.setCreateId(userVo.getId());
                    poolDtlold.setUpdateId(userVo.getId());
                }
                poolDtls.add(poolDtlold);
                int num = abPoolEndorseDao.batchUpdate(poolDtls);
                if (num != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                abPoolEndorseDao.save(poolDtlNew);
                //票据池明细id保存返回给付款单
                if(billIds.length()>0){
                    billIds.append(CommonConstant.COMMA_SPLIT);
                }
                billIds.append(poolDtlNew.getId());
                //获取票据从表
                AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(poolDtlold.getVvId());
                //修改从表box_status为10 正在处理
                num = abVoucherinfoViceService.updateBoxStatus(poolDtlold.getVvId(), AccebillConstant.AB_BOX_STATUS_N, userVo);
                if (num != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                // 再次提交需要把打回从表的BackBriefId为空
                abVoucherinfoViceService.updateBackBriefIdIsNull(poolDtlold.getVvId());
                //根据票据池id获取票据池信息,池接口信息,质权人信息
                AbPoolVO abPoolVO = abPoolService.getAbPoolVOByPId(poolDtlNew.getBpId());
                abPoolEndorseVO.setBifCode(abPoolVO.getBifCode());
                //判断票据池所属银行
                //当为建设银行时  农行
                if (AbPoolConstant.AB_POOL_INTO_APPLE_9018.equals(abPoolVO.getBifCode())) {
                    //新增背书申请流水
                    //根据票据从表id和票据池交易明细上一笔id查询流水信息
                    AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtlNew.getVvId(), poolDtlNew.getLastid());
                    if (!this.saveAccrecords(accrecords, poolDtlNew, abPoolEndorseVO, userVo, false)) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_2);
                    }
                }//当为兴业银行时 浙商银行
                else if (AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolVO.getBifCode())
                		|AbPoolConstant.AB_POOL_INTO_APPLE_CZB.equals(abPoolVO.getBifCode())
                		|AbPoolConstant.AB_POOL_INTO_APPLE_ABC.equals(abPoolVO.getBifCode())
                		|AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolVO.getBifCode())
                		|AbPoolConstant.BIF_CODE_BOC_9021.equals(abPoolVO.getBifCode())) {
                    AbPooIntoBeanVO intoBeanVO = new AbPooIntoBeanVO();
                    intoBeanVO.setPdId(poolDtlNew.getId());
                    intoBeanVO.setVvid(poolDtlNew.getVvId());
                    intoBeanVO.setRecNm(abPoolEndorseVO.getRecNm());// 被背书人名称
                    intoBeanVO.setRecAcctsvcr(abPoolEndorseVO.getRecAcctsvcr());// 被背书人行号
                    intoBeanVO.setRecAcctId(abPoolEndorseVO.getRecAcctId());// 被背书人账号
                    intoBeanVO.setBanendrsmtmk(abPoolEndorseVO.getBanendrsmtmk());//是否可转让标识
                    intoBeanVO.setLastPdId(poolDtlold.getId());
                    //调用出池接口新增流水
                    abPoolOutApplyService.outPoolRefFegin(intoBeanVO, userVo, false);
                }
                // 启动工作流
                 getStartHandler().handler(BillPoolWorkFlowConstant.AB_POLL_ENDORSE_APPLY, poolDtlNew.getId(), abPoolEndorseVO,
                        abVoucherinfoVice.getCorpId(), userVo.getId());
                abLogService.saveAbLog(null, id[i], AbPoolConstant.AB_POOL_BRIEF_010, AccebillConstant.ABLOG_LOGTYPE_BATCHSUBMIT,
                        AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
            }
            abPoolEndorseVO.setBillIds(billIds.toString());
            return true;
        }
        return false;
    }
    /**
     * 付款办理选择票据池票据背书转让<br>
     *
     * @param abPoolEndorseVO 背书信息
     * @param userVo          操作用户
     */
    public boolean saveAbPoolEndorseInfoByNisBillPay(AbPoolEndorseVO abPoolEndorseVO, ICurrentUser userVo) {
        if (abPoolEndorseVO != null && StringUtils.isNotEmpty(abPoolEndorseVO.getId())) {
            //票据池交易明细id
            String[] id = abPoolEndorseVO.getId().split(",");
            StringBuilder billIds=new StringBuilder();
            for (int i = 0; i < id.length; i++) {
                AbPoolDtl poolDtlNew = new AbPoolDtl();
                AbPoolDtl poolDtlold = abPoolEndorseDao.get(id[i]);
                List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
                //修改交易流水为为作废
                poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
                poolDtlold.setUpdateTime(new Date());
                //新增背书申请交易流水
                poolDtlNew.setAppNo(poolDtlold.getAppNo());
                poolDtlNew.setVvId(poolDtlold.getVvId());
                poolDtlNew.setOrigNo(poolDtlold.getOrigNo());
                poolDtlNew.setBpId(poolDtlold.getBpId());
                // 审批通过
                // 指令状态为发送中
                poolDtlNew.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
                poolDtlNew.setStatus(AbPoolConstant.AB_POOL_STATUS95);
                poolDtlNew.setInoutType(poolDtlold.getInoutType());
                poolDtlNew.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
                poolDtlNew.setLastid(poolDtlold.getId());
                poolDtlNew.setBriefId(AbPoolConstant.AB_POOL_BRIEF_010);
                poolDtlNew.setCreateTime(new Date());
                if (userVo != null) {
                    poolDtlNew.setCreateId(userVo.getId());
                    poolDtlold.setUpdateId(userVo.getId());
                }
                poolDtls.add(poolDtlold);
                int num = abPoolEndorseDao.batchUpdate(poolDtls);
                if (num != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                abPoolEndorseDao.save(poolDtlNew);
                //票据池明细id保存返回给付款单
                if(billIds.length()>0){
                    billIds.append(CommonConstant.COMMA_SPLIT);
                }
                billIds.append(poolDtlNew.getId());
                //获取票据从表
                AbVoucherinfoVice abVoucherinfoVice = abVoucherinfoViceService.get(poolDtlold.getVvId());
                //修改从表box_status为11 业务完成
                num = abVoucherinfoViceService.updateBoxStatus(poolDtlold.getVvId(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
                // 再次提交需要把打回从表的BackBriefId为空
                abVoucherinfoViceService.updateBackBriefIdIsNull(poolDtlold.getVvId());
                //根据票据池id获取票据池信息,池接口信息,质权人信息
                AbPoolVO abPoolVO = abPoolService.getAbPoolVOByPId(poolDtlNew.getBpId());
                abPoolEndorseVO.setBifCode(abPoolVO.getBifCode());
                //判断票据池所属银行
                //当为建设银行时  农行
                //根据票据从表id和票据池交易明细上一笔id查询流水信息
                AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtlNew.getVvId(), poolDtlNew.getLastid());
                if (AbPoolConstant.AB_POOL_INTO_APPLE_9018.equals(abPoolVO.getBifCode())) {
                    //新增背书申请流水
                    if (!this.saveAccrecords(accrecords,poolDtlNew, abPoolEndorseVO, userVo,true)) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_2);
                    }
                }//当为兴业银行时 浙商银行
                else if (AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolVO.getBifCode())
                        |AbPoolConstant.AB_POOL_INTO_APPLE_CZB.equals(abPoolVO.getBifCode())
                        |AbPoolConstant.AB_POOL_INTO_APPLE_ABC.equals(abPoolVO.getBifCode())) {
                    AbPooIntoBeanVO intoBeanVO = new AbPooIntoBeanVO();
                    intoBeanVO.setPdId(poolDtlNew.getId());
                    intoBeanVO.setVvid(poolDtlNew.getVvId());
                    intoBeanVO.setRecNm(abPoolEndorseVO.getRecNm());// 被背书人名称
                    intoBeanVO.setRecAcctsvcr(abPoolEndorseVO.getRecAcctsvcr());// 被背书人行号
                    intoBeanVO.setRecAcctId(abPoolEndorseVO.getRecAcctId());// 被背书人账号
                    intoBeanVO.setBanendrsmtmk(abPoolEndorseVO.getBanendrsmtmk());//是否可转让标识
                    intoBeanVO.setLastPdId(poolDtlold.getId());
                    //调用出池接口新增流水
                    abPoolOutApplyService.outPoolRefFegin(intoBeanVO, userVo, true);
                }
                if (accrecords != null) {
                    //修改票据上一笔流水为已删除
                    int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                }
                abLogService.saveAbLog(null, id[i], AbPoolConstant.AB_POOL_BRIEF_010, AccebillConstant.ABLOG_LOGTYPE_AUTO,
                        AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
            }
            abPoolEndorseVO.setBillIds(billIds.toString());
            return true;
        }
        return false;
    }
    /**
     * 保存背书申请流水
     *
     * @param accrecords 上一笔id查询流水信息
     * @param poolDtlNew 新增的票据池交易明细信息
     * @param endorseVO  背书申请信息
     * @param userVo     操作用户
     * @param flag     是否审批通过
     * @return
     */
    public boolean saveAccrecords(AbAccrecords accrecords,AbPoolDtl poolDtlNew, AbPoolEndorseVO endorseVO, ICurrentUser userVo, boolean flag) {
        boolean resule = false;
        if (accrecords != null) {
            //新增票据背书申请流水
            // 主信息表
            AbVoucherinfo vouInfo = abVoucherinfoService.get(accrecords.getVid());
            // 从表
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(accrecords.getVvid());
            // 生成背书申请流水
            AbAccrecords accRecordsNew = new AbAccrecords();
            accRecordsNew.setVid(vouInfo.getId());
            accRecordsNew.setVvid(viceInfo.getId());
            accRecordsNew.setLastid(accrecords.getId());// 上一笔流水id
            accRecordsNew.setLastbillcode(vouInfo.getBillStatus());// 上一票据状态代码
            if (!flag) {
                accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志
                accRecordsNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS16);// 业务处理状态
            }else{
                accRecordsNew.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_0);// 有效标志
                accRecordsNew.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS15);// 业务处理状态
            }
            accRecordsNew.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);// 发送标志
            accRecordsNew.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态
            accRecordsNew.setBriefId(AccebillConstant.BRIEF_STATUS_010);// 业务类型
            accRecordsNew.setApplyDate(new Date());// 申请日期
            accRecordsNew.setBillFlag(vouInfo.getBillFlag());
            accRecordsNew.setBusinessDate(new Date());// 业务办理日期
            accRecordsNew.setBankendrsmtmk(endorseVO.getBanendrsmtmk());//是否可转让标记
            // 是否内部业务标识
            accRecordsNew.setInsideSing(Integer.parseInt(AccebillConstant.AB_INSIDE_SING_N));
            accRecordsNew.setReqAcctId(viceInfo.getBankAcc()); // 背书人账号
            accRecordsNew.setRecNm(endorseVO.getRecNm());// 被背书人名称
            accRecordsNew.setRecAcctsvcr(endorseVO.getRecAcctsvcr());// 被背书人行号
            accRecordsNew.setRecAcctId(endorseVO.getRecAcctId());// 被背书人账号
            accRecordsNew.setVouInfo(vouInfo);
            accRecordsNew.setVouViceInfo(viceInfo);
            accRecordsNew.setPdId(poolDtlNew.getId());//票据池交易明细id
            accRecordsNew.setBifCode(StringUtils.isNotEmpty(viceInfo.getBifCode())?viceInfo.getBifCode():endorseVO.getBifCode());
            // 新增流水
            resule = abAccrecordsService.saveAccrecords(accRecordsNew, userVo);
            abLogService.saveAbLog(accRecordsNew.getVvid(), accRecordsNew.getId(), accRecordsNew.getBriefId(), AccebillConstant.ABLOG_LOGTYPE_SUBMIT, AccebillConstant.LANG_LOG_SUBMIT_SUCC, userVo);
        }
        return resule;
    }
    /**
     * 功能介绍 : 继续或中止操作<br>
     *
     * @param pdId   票据池交易明细id
     * @param opt    操作类型 0
     * @param userVo 操作用户
     * @param isTask 是否定时任务调用
     * @version 1.0
     * @author zhenghj
     */
    public boolean operation(String pdId, String opt, ICurrentUser userVo,boolean isTask) {
        if (StringUtils.isEmpty(pdId) || StringUtils.isEmpty(opt)) {
            throw new BusinessException(AbPoolMessageEnum.AB_POOL_MESSAGE_ERROR_VALISNOTNULL);
        } else {
            AbPoolDtl poolDtl = abPoolEndorseDao.get(pdId);
            //根据票据从表id和票据池交易明细id查询流水信息最早的一笔
            AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvIdOrderByAsc(poolDtl.getVvId(), poolDtl.getId());
            String[] ids = {accrecords.getId()};
            String logType = "";
            String msg = "";
            //继续操作
            if (AbPoolConstant.AB_POOL_OPT_1.equals(opt)) {
                //当申请流水指令失败时
                if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecords.getOptStatus()) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(accrecords.getOptStatus())) {
                    // 指令状态为待发送状态
                    poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
                    //修改流水信息
                    AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
                    accRecordsVO.setIdArray(ids);
                    accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
                    accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
                    abAccrecordsService.batchUpdate(accRecordsVO, userVo);
                }
                //当申请流水指令状态为成功时，修改下一笔流水
                if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(accrecords.getOptStatus())) {
                    // 指令状态为银行处理中
                    poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS18);
                    //根据票据从表id和票据池交易明细id查询流水信息最新的一笔
                    AbAccrecords accrecordsNew = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getId());
                    if (accrecordsNew != null) {
                        if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecordsNew.getOptStatus()) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(accrecordsNew.getOptStatus())) {
                            String[] lastId = {accrecordsNew.getId()};
                            //修改背书申请票据流水为待发送
                            AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
                            accRecordsVO.setIdArray(lastId);
                            accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
                            accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
                            abAccrecordsService.batchUpdate(accRecordsVO, userVo);
                        }
                    }
                }
                logType = AccebillConstant.ABLOG_LOGTYPE_INSSEND;
                msg = AccebillConstant.LANG_LOG_UPDATE_SUCC;
                abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
                        AccebillConstant.ABLOG_LOGTYPE_INSSEND, AccebillConstant.LANG_LOG_UPDATE_SUCC, userVo);
                //终止操作
            } else if (AbPoolConstant.AB_POOL_OPT_0.equals(opt)) {
                // 指令状态为交易打回
                poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS_2);
                //有效标识修改为无效
                poolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
                //修改从表box_status为11 业务完成
                abVoucherinfoViceService.updateBoxStatus(poolDtl.getVvId(), AccebillConstant.AB_BOX_STATUS_Y, userVo);
                //根据票据池id获取票据池信息,池接口信息,质权人信息
                AbPoolVO abPoolVO = abPoolService.getAbPoolVOByPId(poolDtl.getBpId());
                //判断票据池所属银行
                //当为建设银行时
                if (AbPoolConstant.AB_POOL_INTO_APPLE_9018.equals(abPoolVO.getBifCode())) {
                    //修改上一笔交易流水为有效
                    AbPoolDtl poolDtlold = abPoolEndorseDao.get(poolDtl.getLastid());
                    List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
                    poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
                    poolDtlold.setUpdateTime(new Date());
                    poolDtlold.setUpdateId(userVo.getId());
                    poolDtls.add(poolDtlold);
                    int num = abPoolEndorseDao.batchUpdate(poolDtls);
                    if (num != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    //修改上一笔票据流水为可用
                    int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                    if (upLastValidSignNum != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    //修改本次背书票据流水为作废
                    upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                    if (upLastValidSignNum != 1) {
                        throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    abLogService.saveAbLog(accrecords.getVvid(), accrecords.getId(), accrecords.getBriefId(),
                            AccebillConstant.ABLOG_LOGTYPE_INSREPULSE, AccebillConstant.LANG_LOG_WITHDRAW_SUCC, userVo);
                }
                //当为兴业银行时
                else if (AbPoolConstant.AB_POOL_INTO_APPLE_9015.equals(abPoolVO.getBifCode())
                		| AbPoolConstant.AB_POOL_INTO_APPLE_CZB.equals(abPoolVO.getBifCode())) {
                    //判断质押解除出池申请票据流水指令状态
                    if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(accrecords.getOptStatus())) {
                        boolean isUpdate=true;
                        //根据票据从表id和票据池交易明细id查询流水信息最新的一笔流水（背书申请流水）
                        AbAccrecords accrecordsNew = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getId());
                        if (accrecordsNew != null) {
                            //修改背书申请流水为无效
                            abAccrecordsService.updateValidSign(accrecordsNew.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                            //修改解除质押签收流水为有效
                            abAccrecordsService.updateValidSign(accrecordsNew.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                            isUpdate=false;
                        }
                        //获取票据池交易流水的上一笔流水
                        AbAccrecords accrecordsLast = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getLastid());
                        if(accrecordsLast!=null && accrecordsLast.getLastid()!=null && isUpdate){
                            //修改移存的上一笔流水为有效
                            abAccrecordsService.updateValidSign(accrecordsLast.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                        }
                        //修改从表票据池状态为池外
                        abVoucherinfoViceService.updatePoolSatatus(poolDtl.getVvId(),AbPoolConstant.AB_BILL_POOL_STATUS_0,userVo);
                    }
                    //出池申请流水指令状态失败时,则修改上一笔票据流水为可用、修改出池申请票据流水为无效、修改上一笔票据交易明细为可用
                    else if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecords.getOptStatus()) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(accrecords.getOptStatus())) {
                        //修改出池申请票据流水为无效
                        int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                        if (upLastValidSignNum != 1) {
                            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //修改上一笔票据流水为可用
                        if(StringUtils.isNotEmpty(accrecords.getLastid())){
                            upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                            if (upLastValidSignNum != 1) {
                                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                            }
                        }
                        //修改上一笔交易流水为有效
                        AbPoolDtl poolDtlold = abPoolEndorseDao.get(poolDtl.getLastid());
                        List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
                        poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
                        poolDtlold.setUpdateTime(new Date());
                        poolDtlold.setUpdateId(userVo.getId());
                        poolDtls.add(poolDtlold);
                        int num = abPoolEndorseDao.batchUpdate(poolDtls);
                        if (num != 1) {
                            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                    }
                }
                //当为中信、中国银行时
                else if (AbPoolConstant.BIF_CODE_CITIC_9001.equals(abPoolVO.getBifCode())
                        | AbPoolConstant.BIF_CODE_BOC_9021.equals(abPoolVO.getBifCode())) {
                    //判断质押解除出池申请票据流水指令状态
                    if (AccebillConstant.AB_BILL_OPT_STATUS19.equals(accrecords.getOptStatus())) {
                        //根据票据从表id和票据池交易明细id查询流水信息最新的一笔流水（背书申请流水）
                        AbAccrecords accrecordsNew = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getId());
                        if (accrecordsNew != null) {
                            //修改背书申请流水为无效
                            abAccrecordsService.updateValidSign(accrecordsNew.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                            //修改解除质押签收流水为有效
                           /* abAccrecordsService.updateValidSign(accrecordsNew.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                            */
                        }
                        //获取票据池交易流水的上一笔流水
                        AbAccrecords accrecordsLast = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getLastid());
                        if(accrecordsLast!=null && accrecordsLast.getLastid()!=null){
                            //修改移存的上一笔流水为有效
                            abAccrecordsService.updateValidSign(accrecordsLast.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                        }
                        //修改从表票据池状态为池外
                        abVoucherinfoViceService.updatePoolSatatus(poolDtl.getVvId(),AbPoolConstant.AB_BILL_POOL_STATUS_0,userVo);
                    }
                    //出池申请流水指令状态失败时,则修改上一笔票据流水为可用、修改出池申请票据流水为无效、修改上一笔票据交易明细为可用
                    else if (AccebillConstant.AB_BILL_OPT_STATUS_1.equals(accrecords.getOptStatus()) || AccebillConstant.AB_BILL_OPT_STATUS_4.equals(accrecords.getOptStatus())) {
                        //修改出池申请票据流水为无效
                        int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, userVo);
                        if (upLastValidSignNum != 1) {
                            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //修改上一笔票据流水为可用
                        if(StringUtils.isNotEmpty(accrecords.getLastid())){
                            upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getLastid(), AccebillConstant.AB_BILL_VALID_SIGN_1, userVo);
                            if (upLastValidSignNum != 1) {
                                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                            }
                        }
                        //修改上一笔交易流水为有效
                        AbPoolDtl poolDtlold = abPoolEndorseDao.get(poolDtl.getLastid());
                        List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
                        poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
                        poolDtlold.setUpdateTime(new Date());
                        poolDtlold.setUpdateId(userVo.getId());
                        poolDtls.add(poolDtlold);
                        int num = abPoolEndorseDao.batchUpdate(poolDtls);
                        if (num != 1) {
                            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                    }
                }
                logType = AccebillConstant.ABLOG_LOGTYPE_INSREPULSE;
                msg = AccebillConstant.LANG_LOG_WITHDRAW_SUCC;
            }
            poolDtl.setUpdateId(userVo.getId());
            poolDtl.setUpdateTime(new Date());
            abPoolEndorseDao.update(poolDtl);
            //非定时任务调用时
            if(!isTask){
                //终止时判断有没有付款单、有则修改付款单
                List<AbVoucherinfoDTO> poolEndorsePay = abPoolEndorseDao.queryBillPoolEndorsePayErrorInfo(poolDtl.getId());
                if(!CollectionUtils.isEmpty(poolEndorsePay)){
                    // todo:数据转换
                    List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO> voucherList = BeanUtils.copyListProperties(poolEndorsePay, com.bytter.biz.payment.api.dto.AbVoucherinfoDTO.class);
                    Map<String, Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>>> payReturnMap = new HashMap();
                    Map<String,String> notPayinfo=new HashMap<>();
                    for(com.bytter.biz.payment.api.dto.AbVoucherinfoDTO info:voucherList){
                        if(notPayinfo.containsKey(info.getPayId())){
                            continue;
                        }
                        if(!payReturnMap.containsKey(info.getPayId())){
                            Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>> listMap=new HashMap<>();
                            listMap.put("error", new ArrayList<>());
                            payReturnMap.put(info.getPayId(),listMap);
                        }else if(!payReturnMap.get(info.getPayId()).containsKey("error")){
                            payReturnMap.get(info.getPayId()).put("error", new ArrayList<>());
                        }
                        payReturnMap.get(info.getPayId()).get("error").add(info);
                        for (String key : payReturnMap.keySet()) {
                            nisBillheadService.backNisBillheadForBill(key, payReturnMap.get(key).get("error"), payReturnMap.get(key).get("success"));
                        }
                    }
                }
            }
            // 生成日志
            abLogService.saveAbLog(null, poolDtl.getLastid(), AbPoolConstant.AB_POOL_BRIEF_010, logType, msg, userVo);
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
        AbPoolDtl poolDtl = abPoolEndorseDao.get(workFlowResult.getBusinessKey());
        Integer status = workFlowResult.isPass() ? AbPoolConstant.AB_POOL_STATUS95 : AbPoolConstant.AB_POOL_STATUS_1;
        poolDtl.setStatus(status);
        String logType = "";
        String msg = "";
        //根据票据池id获取票据池信息,池接口信息,质权人信息
        AbPoolVO abPoolVO = abPoolService.getAbPoolVOByPId(poolDtl.getBpId());
        //根据票据从表id和票据池交易明细id查询流水信息
        AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getId());
        if (AbPoolConstant.AB_POOL_STATUS95.equals(status)) {
            // 审批通过
            // 指令状态为发送中
            poolDtl.setOptStatus(AbPoolConstant.AB_POOL_OPT_STATUS16);
            //有效标识修改为有效
            poolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            logType = AccebillConstant.ABLOG_LOGTYPE_APP;
            msg = AccebillConstant.LANG_LOG_APP_SUCC;
            //修改上一笔交易流水为作废
            AbPoolDtl poolDtlold = abPoolEndorseDao.get(poolDtl.getLastid());
            List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
            poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            poolDtlold.setUpdateTime(new Date());
            poolDtlold.setUpdateId(user.getId());
            poolDtls.add(poolDtlold);
            int num = abPoolEndorseDao.batchUpdate(poolDtls);
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            if (accrecords != null) {
                //修改票据流水为有效
                int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecords.getId(), AccebillConstant.AB_BILL_VALID_SIGN_1, user);
                if (upLastValidSignNum != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                //修改票据流水指令状态为发送中(16)，保证指令测试页面(过滤opt_status=15)能查到
                String[] ids = {accrecords.getId()};
                AbAccrecordsVO accRecordsVO = new AbAccrecordsVO();
                accRecordsVO.setIdArray(ids);
                accRecordsVO.setOptStatusNew(AccebillConstant.AB_BILL_OPT_STATUS16);
                accRecordsVO.setReturnCode(AccebillConstant.AB_BILL_RETURN_CODE_3);
                abAccrecordsService.batchUpdate(accRecordsVO, user);
            }
            //根据票据从表id和票据池交易明细上一笔id查询流水信息
            AbAccrecords accrecordsLast = abAccrecordsService.getBillByPdIdAndVvId(poolDtl.getVvId(), poolDtl.getLastid());
            if (accrecordsLast != null) {
                //修改票据流水为已删除
                int upLastValidSignNum = abAccrecordsService.updateValidSign(accrecordsLast.getId(), AccebillConstant.AB_BILL_VALID_SIGN_2, user);
            }
        } else {
            // 审批打回
            logType = AccebillConstant.ABLOG_LOGTYPE_REPULSE;
            msg = AccebillConstant.LANG_LOG_APP_BACK;
            //有效标识修改为无效
            poolDtl.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_2);
            //修改上一笔交易流水为有效
            AbPoolDtl poolDtlold = abPoolEndorseDao.get(poolDtl.getLastid());
            List<AbPoolDtl> poolDtls = new ArrayList<AbPoolDtl>();
            poolDtlold.setValidSign(AbPoolConstant.AB_POLL_VALID_SIGN_1);
            poolDtlold.setUpdateTime(new Date());
            poolDtlold.setUpdateId(user.getId());
            poolDtls.add(poolDtlold);
            int num = abPoolEndorseDao.batchUpdate(poolDtls);
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            //修改从表box_status为11 业务完成
            num = abVoucherinfoViceService.updateBoxStatus(poolDtlold.getVvId(), AccebillConstant.AB_BOX_STATUS_Y, user);
            if (num != 1) {
                throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
            if (accrecords != null) {
                //修改票据流水为无效
                String[] aids = {accrecords.getId()};
                // 撤回状态的流水作废
                int upLastValidSignNum = abAccrecordsService.batchUpdate(aids, AccebillConstant.AB_BILL_BUS_STATUS_2, null, AccebillConstant.AB_BILL_VALID_SIGN_2, user);
                if (upLastValidSignNum != 1) {
                    throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_DATAISUPDATEERROR);
                }
                //审批打回设置从表打回业务和值
                abVoucherinfoViceService.updateBackBriefId(accrecords.getVvid(), AbPoolConstant.AB_POOL_BRIEF_010, accrecords.getId());
            }
           
        }
        poolDtl.setUpdateId(user.getId());
        poolDtl.setUpdateTime(new Date());
        abPoolEndorseDao.update(poolDtl);
        //打回时判断有没有付款单、有则修改付款单为打回
        List<AbVoucherinfoDTO> poolEndorsePay = abPoolEndorseDao.queryBillPoolEndorsePayErrorInfo(poolDtl.getId());
        if(!CollectionUtils.isEmpty(poolEndorsePay)){
            // todo:数据转换
            List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO> voucherList = BeanUtils.copyListProperties(poolEndorsePay, com.bytter.biz.payment.api.dto.AbVoucherinfoDTO.class);
            Map<String, Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>>> payReturnMap = new HashMap();
            Map<String,String> notPayinfo=new HashMap<>();
            for(com.bytter.biz.payment.api.dto.AbVoucherinfoDTO info:voucherList){
                if(notPayinfo.containsKey(info.getPayId())){
                    continue;
                }
                if(!payReturnMap.containsKey(info.getPayId())){
                    Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>> listMap=new HashMap<>();
                    listMap.put("error",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                    payReturnMap.put(info.getPayId(),listMap);
                }else if(!payReturnMap.get(info.getPayId()).containsKey("error")){
                    payReturnMap.get(info.getPayId()).put("error",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                }
                payReturnMap.get(info.getPayId()).get("error").add(info);
                for (String key : payReturnMap.keySet()) {
                    nisBillheadService.backNisBillheadForBill(key, payReturnMap.get(key).get("error"), payReturnMap.get(key).get("success"));
                }
            }
        }
        // 生成日志
        abLogService.saveAbLog(null, poolDtl.getLastid(), AbPoolConstant.AB_POOL_BRIEF_010, logType, msg, user);
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = abWorkFlowService
                .getWorkFlowBusiness(BillPoolWorkFlowConstant.AB_POLL_ENDORSE_APPLY, dataTablesInput);
        List<String> businessList = workFlowBusiness.getBusinessIdList();
        //调用公共查询当前审批人审批
        DataTablesOutput dp = abPoolEndorseDao.dataTableApprove(dataTablesInput, businessList);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 根据票据池交易明细iId查询背书转让信息
     *
     * @param pdId 票据池交易明细id
     * @return
     */
    public AbPoolEndorseVO getAbPoolEndorseVOByPdId(String pdId) {
        if (StringUtils.isNotEmpty(pdId)) {
            List<AbPoolEndorseVO> list = abPoolEndorseDao.getAbPoolEndorseVOByPdId(pdId);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }
    /**
     * 加载详细审核数据
     *
     * @param vvid
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolEndorseVO getAbBillEndorseApplyVOByVvid(String vvid, String pdId) {
        AbPoolEndorseVO reInfo = new AbPoolEndorseVO();
        if (StringUtils.isNotEmpty(vvid)) {
            AbVoucherinfoVice viceInfo = abVoucherinfoViceService.get(vvid, true);
            AbVoucherinfo vouInfo = abVoucherinfoService.get(viceInfo.getVid(), true);
            BeanUtils.copyNotNullProperties(vouInfo, reInfo);
            BeanUtils.copyNotNullProperties(viceInfo, reInfo);
            if (StringUtils.isNotEmpty(pdId)) {
                //根据票据从表id和票据池交易明细id查询流水信息
                AbAccrecords accrecords = abAccrecordsService.getBillByPdIdAndVvId(vvid, pdId);
                if (accrecords != null) {
                    BeanUtils.copyNotNullProperties(accrecords, reInfo);
                    reInfo.setAccId(accrecords.getId());
                }
            }
        }
        return reInfo;
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
        if (StringUtils.isNotEmpty(pdId)) {
            params.put("lastId", pdId);
            return abPoolDiscountService.getAbPoolDtlByParams(params);
        }
        return null;
    }
    /**
     * 根据pdId获取票据池交易明细数据
     *
     * @param pdId 票据池交易明细id
     * @return
     */
    @Transactional(readOnly = true)
    public AbPoolDtl getAbPoolDtlByPdId(String pdId) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(pdId)) {
            params.put("id", pdId);
            return abPoolDiscountService.getAbPoolDtlByParams(params);
        }
        return null;
    }
    @Override
    @XxlJob("AbPoolEndorseService")
    public void run(String arguments) {
        //票据池付款背书失败打回给结算系统
        this.runPoolEndorsePayError();
        //票据池付款背书成功给结算系统
        this.runPoolEndorsePaySuccess();
    }
    /**
     * 获取付款票据池背书失败信息返回给结算系统
     */
    public void runPoolEndorsePayError() {
        this.logger.info("---应收票据付款-票据池背书失败结果返回结算系统，开始执行定时任务---");
        List<AbVoucherinfoDTO> infoList = abPoolEndorseDao.queryBillPoolEndorsePayErrorInfo();
        if (infoList == null || infoList.isEmpty()) {
            this.logger.info("---应收票据付款-票据池背书失败结果返回结算系统，【0】条数据。");
        }else {
            Map<String, Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>>> payReturnMap = new HashMap();
            //记录不需要返回数据，存在背书未完成信息不返回到付款（背书要么成功、拒签、打回、撤回）
            Map<String,String> notPayinfo=new HashMap<>();
            ICurrentUser userVo = uerService.getScheduler();
            // todo:数据转换
            List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO> voucherList = BeanUtils.copyListProperties(infoList, com.bytter.biz.payment.api.dto.AbVoucherinfoDTO.class);
            for(com.bytter.biz.payment.api.dto.AbVoucherinfoDTO info:voucherList){
                if(notPayinfo.containsKey(info.getPayId())){
                    continue;
                }
                if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())
                        ||AccebillConstant.AB_BILL_OPT_STATUS_1.equals(info.getOptStatus())
                        ||AccebillConstant.AB_BILL_OPT_STATUS_2.equals(info.getOptStatus())
                        ||AccebillConstant.AB_BILL_OPT_STATUS_3.equals(info.getOptStatus())
                        ||AccebillConstant.AB_BILL_OPT_STATUS_4.equals(info.getOptStatus())){
                    if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())){
                        if(!payReturnMap.containsKey(info.getPayId())){
                            Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>> listMap=new HashMap<>();
                            listMap.put("success",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                            payReturnMap.put(info.getPayId(),listMap);
                        }else if(!payReturnMap.get(info.getPayId()).containsKey("success")){
                            payReturnMap.get(info.getPayId()).put("success",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                        }
                        payReturnMap.get(info.getPayId()).get("success").add(info);
                    }else{
                        if(!payReturnMap.containsKey(info.getPayId())){
                            Map<String, List<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>> listMap=new HashMap<>();
                            listMap.put("error",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                            payReturnMap.put(info.getPayId(),listMap);
                        }else if(!payReturnMap.get(info.getPayId()).containsKey("error")){
                            payReturnMap.get(info.getPayId()).put("error",new ArrayList<com.bytter.biz.payment.api.dto.AbVoucherinfoDTO>());
                        }
                        payReturnMap.get(info.getPayId()).get("error").add(info);
                        //调用终止操作
                        this.operation(info.getId(),AbPoolConstant.AB_POOL_OPT_0,userVo,true);
                    }
                }else{
                    payReturnMap.remove(info.getPayId());
                    notPayinfo.put(info.getPayId(),info.getPayId());
                }
            }
            if (payReturnMap.isEmpty()) {
                this.logger.info("---应收票据付款-票据池背书失败结果返回结算系统，【0】条数据。");
            } else {
                this.logger.info("---应收票据付款-票据池背书失败结果返回结算系统，【" + payReturnMap.size() + "】条付款数据。");
                for (String key : payReturnMap.keySet()) {
                    nisBillheadService.backNisBillheadForBill(key, payReturnMap.get(key).get("error"), payReturnMap.get(key).get("success"));
                    for(com.bytter.biz.payment.api.dto.AbVoucherinfoDTO info:payReturnMap.get(key).get("error")){
                        accebillService.delBillPayMid(info.getVvid(),info.getPayId());
                        accebillService.saveBillMiddle(info.getId(), AccebillConstant.BILL_MIDDLE_TYPE_010PAYERROR, userVo);
                    }
                }
            }
        }
        this.logger.info("---应收票据付款-票据池背书失败结果返回结算系统，结束执行定时任务---");
    }
    /**
     * 获取付款票据池背书成功信息返回给结算系统
     */
    public void runPoolEndorsePaySuccess() {
        this.logger.info("---应收票据付款-票据池背书成功结果返回结算系统，开始执行定时任务---");
        List<AbVoucherinfoDTO> infoList = abPoolEndorseDao.queryBillPoolEndorsePaySuccessInfo();
        if (infoList == null || infoList.isEmpty()) {
            this.logger.info("---应收票据付款-票据池背书成功结果返回结算系统，【0】条数据。");
        }else {
            Map<String, Map<String, List<AbVoucherinfoDTO>>> payReturnMap = new HashMap();
            //记录不需要返回数据，存在背书未完成信息不返回到付款（背书要么成功、拒签、打回、撤回）
            Map<String,String> notPayinfo=new HashMap<>();
            for(AbVoucherinfoDTO info:infoList){
                if(notPayinfo.containsKey(info.getPayId())){
                    continue;
                }
                if(AccebillConstant.AB_BILL_OPT_STATUS19.equals(info.getOptStatus())){
                    if(!payReturnMap.containsKey(info.getPayId())){
                        Map<String, List<AbVoucherinfoDTO>> listMap=new HashMap<>();
                        listMap.put("success",new ArrayList<AbVoucherinfoDTO>());
                        payReturnMap.put(info.getPayId(),listMap);
                    }else if(!payReturnMap.get(info.getPayId()).containsKey("success")){
                        payReturnMap.get(info.getPayId()).put("success",new ArrayList<AbVoucherinfoDTO>());
                    }
                    payReturnMap.get(info.getPayId()).get("success").add(info);
                }else{
                    payReturnMap.remove(info.getPayId());
                    notPayinfo.put(info.getPayId(),info.getPayId());
                }
            }
            if (payReturnMap.isEmpty()) {
                this.logger.info("---应收票据付款-票据池背书成功结果返回结算系统，【0】条数据。");
            } else {
                ICurrentUser userVo = uerService.getScheduler();
                this.logger.info("---应收票据付款-票据池背书成功结果返回结算系统，【" + payReturnMap.size() + "】条付款数据。");
                for (String key : payReturnMap.keySet()) {
                    nisBillheadService.successNisBillheadForBill(key, NetbankConstant.STATUE_BILL_COMPLETE);
                    for(AbVoucherinfoDTO info:payReturnMap.get(key).get("success")){
                        accebillService.saveBillMiddle(info.getId(), AccebillConstant.BILL_MIDDLE_TYPE_010PAYERROR, userVo);
                    }
                }
            }
        }
        this.logger.info("---应收票据付款-票据池背书成功结果返回结算系统，结束执行定时任务---");
    }
}
