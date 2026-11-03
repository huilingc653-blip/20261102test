package com.bytter.credit.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationDelayDao;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatBkrationDelay;
import com.bytter.credit.domain.vo.RatBkrationDelayVO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: 授信延期表
 * @author onlineGenerator
 * @date 2017-08-26 09:13:31
 * @version 1.0
 *
 */
@Service("ratBkrationDelayService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_RATION_DELAY })
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_RATION_DELAY )
public class RatBkrationDelayService extends BaseService<RatBkrationDelay, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatBkrationDelayDao ratBkrationDelayDao;
    @Autowired
    private RatBkrationService rationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkrationDelay entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(RatBkrationDelay entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkrationDelay entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkrationDelay entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationDelay, String> getEntityDao() {
        return ratBkrationDelayDao;
    }
    /**
     * 功能介绍 : 查询授信待延期数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput listDelayOutputData(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rb.corp_Id", true);
        return rationService.setOutputPageData(ratBkrationDelayDao.listDelayOutputData(input, corpAuthSql));
    }
    /**
     * 功能介绍 : 查询授信延期待审批数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public DataTablesOutput getDataApplyApproveTables(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_RATION_DELAY,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratBkrationDelayDao.listDelayApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    public RatBkrationDelay getRationDelayByRatCode(String ratCode) {
        List<RatBkrationDelay> delays = ratBkrationDelayDao.getRationDelayHisList(ratCode,
                RationConstant.RAT_DELAY_SIGN_NO);
        if (!EmptyUtils.isEmptyList(delays)) {
            return delays.get(0);
        }
        return null;
    }
    /**
     * 功能介绍 : 延期处理。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRationDelay(RatBkrationDelayVO delayVO, String userId, String loginUserName) {
        notificationService.complete(NotificationConstant.CMS.RAT_DELAY, delayVO.getId(), userId);
        // 判断是否有延期记录,如果有则更新原来的记录为历史延期
        List<RatBkrationDelay> delays = ratBkrationDelayDao.getRationDelayHisList(delayVO.getRatCode(),
                RationConstant.RAT_DELAY_SIGN_NO);
        if (!EmptyUtils.isEmptyList(delays)) {
            for (RatBkrationDelay bkrationDelay : delays) {
                bkrationDelay.setDelaySign(RationConstant.RAT_DELAY_SIGN_YES);// 标识为延期历史
            }
        }
        RatBkration bkRation = rationService.get(delayVO.getRatCode(), true);
        Date endDate = bkRation.getEnddate();// 授信延期日期应大于授信结束日期
        if (!delayVO.getDelayDate().after(endDate)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DELAY_DATEAFTER);
        }
        RatBkrationDelay delay = null;
        if (StringUtils.isEmpty(delayVO.getId())) {
            delay = new RatBkrationDelay();
            BeanUtils.copyProperties(delayVO, delay);
            RatBkration ration = rationService.get(delayVO.getRatCode(), true);
            BeanUtils.copyProperties(ration, delay, "id");
            delay.setRatCode(ration.getId());
        } else {
            delay = get(delayVO.getId(), true);
        }
        delay.setDelayDate(delayVO.getDelayDate());// 延期到期日
        delay.setStatus(delayVO.getStatus());// 状态
        delay.setDelaySign(RationConstant.RAT_DELAY_SIGN_NO);// 标识为当前延期数据
        if (StringUtils.isEmpty(delayVO.getId())) {
            delay.setCreateBy(userId);
            delay.setCreateDate(DateUtils.getDate());
        } else {
            delay.setAttachmentId(delayVO.getAttachmentId());
            delay.setUpdateBy(userId);
            delay.setUpdateDate(DateUtils.getDate());
        }
        saveOrUpdate(delay);
        // todo:挂工作流
        if (RationConstant.STATUS_WAIT_CHECK.equals(delay.getStatus())) {
             getStartHandler().handler(RatWorkFlowConstant.RAT_RATION_DELAY, delay.getId(), delay, delay.getCorpId(),
                    userId);
        }
    }
    /**
     * 功能介绍 ： 审批处理延期数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDelayApprove(String delayCode, boolean flag, String userId, String loginName) {
        RatBkrationDelay delay = get(delayCode, true);
        if (flag) {// 审批通过
            delay.setStatus(RationConstant.STATUS_CHECK);// 审批通过
        } else {// 审批打回
            delay.setStatus(RationConstant.STATUS_BACK);// 审批打回
        }
        delay.setDelaySign(RationConstant.RAT_DELAY_SIGN_NO);//
        delay.setUpdateBy(userId);
        delay.setUpdateDate(DateUtils.getDate());
        updateEntity(delay);
        // 审批通过后更新授信到期日期
        if (flag) {
            RatBkration ration = rationService.get(delay.getRatCode(), true);
            ration.setEnddate(delay.getDelayDate());// 延期日期作为授信结束日期
            rationService.updateEntity(ration);
            // 判断是否挂接了保证金,如果挂接了保证金,那么项目的到期日期应与授信延期到期日期一致
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(ration.getId(), ration.getRatCode());
            if (dto != null) {
                dto.setEndDate(delay.getDelayDate());
                dto.setUpdateBy(loginName);
                dto.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApplyDate(dto);
            }
        }
    }
    /**
     * 功能介绍 : 删除暂存的延期数据。<br>
     * <b>日期：</b>2017年8月26日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteRationDelay(RatBkrationDelayVO delayVO, String userId) {
        // 判断是否有延期记录,如果有则更新原来的记录为历史延期
        List<RatBkrationDelay> delays = ratBkrationDelayDao.getRationDelayHisList(delayVO.getRatCode(),
                RationConstant.RAT_DELAY_SIGN_YES);
        if (!EmptyUtils.isEmptyList(delays)) {
            RatBkrationDelay bkrationDelay = delays.get(0);
            bkrationDelay.setDelaySign(RationConstant.RAT_DELAY_SIGN_NO);// 还原第一笔为未延期历史
        }
        RatBkrationDelay delay = get(delayVO.getId(), true);
        delay.setStatus(RationConstant.STATUS_DELETE);// 删除状态
        delay.setDelaySign(RationConstant.RAT_DELAY_SIGN_YES);// 本次作为历史延期数据
        delay.setUpdateBy(userId);
        delay.setUpdateDate(DateUtils.getDate());
        updateEntity(delay);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        boolean result = workFlowResult.isPass();// 是否通过
        String userId = workFlowResult.getUser().getId();
        String loginUserName = workFlowResult.getUser().getLoginName();
        getEntityDao().flush();
        updateRationDelayApprove(businessKey, result, userId, loginUserName);
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 银行授信占用在途数据
            int dataSize1 = ratBkrationDelayDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_DELAY_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
