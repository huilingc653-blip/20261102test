package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexExposureMainDao;
import com.bytter.biz.foreign.entity.FexExposure;
import com.bytter.biz.foreign.entity.FexExposureDetail;
import com.bytter.biz.foreign.entity.FexExposureMain;
import com.bytter.biz.foreign.vo.FexCorpExposureVO;
import com.bytter.biz.foreign.vo.FexExposureDetailVO;
import com.bytter.biz.foreign.vo.FexExposureMainVO;
import com.bytter.biz.foreign.vo.FexExposureVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.FexWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: Service
 * @Description: 单位敞口上报主表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@WorkFlow(FexWorkFlowConstant.FEX_EXPOSURE_MAIN)
@Service("fexExposureMainService")
@Transactional
public class FexExposureMainService extends BaseService<FexExposureMain, String> implements IWorkFlowListener {
    @Autowired
    private FexExposureMainDao fexExposureMainDao;

    @Autowired(required = false)
    private IBtCurrencyService currencyService;

    @Autowired(required = false)
    private ICorpService corpService;

    @Autowired
    private FexMessageService fexMessageService;

    @Autowired(required=false)
    private IWorkFlowService workFlowService;

    @Autowired
    private FexExposureService fexExposureService;

    @Autowired
    private FexExposureDetailService fexExposureDetailService;

    @Autowired
    private FexCorpExposureService fexCorpExposureService;

    @Override
    public BaseDao<FexExposureMain, String> getEntityDao() {
        return fexExposureMainDao;
    }

    /**
     * 查询敞口上报信息
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        String resource = ResourceConstant.FOREIGN_FEXEXPOSUREINFO_QUERY;
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, user.getId()), "t.corp_Id", true);
        sql.append(" and ").append(corpAuthSql);
        DataTablesOutput data = fexExposureMainDao.getDataTablesUnion(dataTablesInput, sql.toString());
        List<FexExposureMainVO> list = data.getData();
        for(FexExposureMainVO vo:list){
            vo.setEnglishCode(this.getEnglishCode(vo));
        }
        return data;
    }

    public String getEnglishCode(FexExposureMainVO vo){
        String englishCode = "";
        String[] curIds = vo.getCurId().split(CommonConstant.COMMA_SPLIT);
        for (String curId:curIds) {
            BtCurrencyDTO cur = currencyService.getObjectById(curId);
            if(StringUtils.isNotBlank(englishCode)){
                englishCode = englishCode + CommonConstant.COMMA_SPLIT + cur.getEnglishCode();
            } else {
                englishCode = cur.getEnglishCode();
            }
        }
        return englishCode;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public FexExposureMainVO getByIdUnion(String id) {
        FexExposureMainVO vo = this.fexExposureMainDao.getByIdUnion(id);
        vo.setEnglishCode(this.getEnglishCode(vo));
        return vo;
    }

    /**
     * 删除上报明细
     * @param id
     */
    public void deleteById(String id) {
        fexExposureMainDao.deleteById(id);
    }

    /**
     * 获取审批列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(FexWorkFlowConstant.FEX_EXPOSURE_MAIN, dataTablesInput);
        String businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "t.id", true);
        DataTablesOutput dp = this.fexExposureMainDao.dataTableApprove(dataTablesInput, businessSql);
        List<FexExposureMainVO> list = dp.getData();
        for(FexExposureMainVO vo:list){
            vo.setEnglishCode(this.getEnglishCode(vo));
        }
        return workFlowBusiness.merge(dp);
    }

    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        ICurrentUser user = workFlowResult.getUser();
        FexExposureMain fexExposureMain = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = workFlowResult.isPass() ? CommonConstant.STATUE_CHECK : CommonConstant.STATUE_BACK;
        fexExposureMain.setStatus(status);
        fexExposureMain.setUpdateBy(workFlowResult.getUser().getLoginName());
        fexExposureMain.setUpdateDate(DateUtils.getDate());
        this.saveOrUpdate(fexExposureMain);
        Integer messageType = workFlowResult.isPass() ? FexMessageConstant.MESSAGE_TYPE_CHECK_PASS : FexMessageConstant.MESSAGE_TYPE_CHECK_BACK;
        String message = workFlowResult.isPass() ? FexMessageConstant.FEX_LOG_APP_SUCC : FexMessageConstant.FEX_LOG_APP_BACK;
        //生成日志
        fexMessageService.saveMessage(fexExposureMain.getId(), messageType, message, FexMessageConstant.BUS_TYPE_FOREIGN, user);
        //审批通过
        if (workFlowResult.isPass()) {
            this.getSession().flush();
            CorpDTO corpDTO = corpService.get(fexExposureMain.getCorpId());
            //判断单位是否要汇总：单位设置汇总且存在上级单位
            if (StringUtils.isNotBlank(corpDTO.getParentId())
                    && fexCorpExposureService.collectFlagByCorpId(fexExposureMain.getCorpId())) {
                saveFexExposureCollectUnderCorp(fexExposureMain, user);
            }
        }
    }

    /**
     * 生成汇总单位的敞口
     * @param fexExposureMain
     * @param user
     */
    public void saveFexExposureCollectUnderCorp(FexExposureMain fexExposureMain, ICurrentUser user) {
        //获取上级单位
        FexCorpExposureVO parentCorp = fexCorpExposureService.getParentCorpExposureByCorpId(fexExposureMain.getCorpId());
        // 获取上级单位的敞口信息
        Date beginDate = DateUtils.getFirstMonthDate(fexExposureMain.getEnrolDate());
        Date endDate = DateUtils.getLastMonthDate(fexExposureMain.getEnrolDate());
        FexExposureMain parentExposureMain = this.getCorpExposure(parentCorp.getCorpId(), fexExposureMain.getTemplateId(), beginDate, endDate);
        String[] curIds = fexExposureMain.getCurId().split(CommonConstant.COMMA_SPLIT);
        if (parentExposureMain == null) {
            // 初始化主信息
            parentExposureMain = new FexExposureMain();
            BeanUtils.copyNotNullProperties(fexExposureMain, parentExposureMain);
            parentExposureMain.setId(null);
            parentExposureMain.setCorpId(parentCorp.getCorpId());
            parentExposureMain.setStatus(CommonConstant.STATUE_CHECK);
            parentExposureMain.setCreateBy(user.getLoginName());
            parentExposureMain.setCreateDate(DateUtils.getDate());
            this.saveEntity(parentExposureMain);
            for (String curId:curIds) {
                //子单位敞口上报信息
                List<FexExposureVO> listExposure = fexExposureService.listVOByMainId(fexExposureMain.getId(), curId);
                for (FexExposureVO vo : listExposure) {
                    FexExposure exposure = new FexExposure();
                    BeanUtils.copyNotNullProperties(vo, exposure);
                    exposure.setId(null);
                    exposure.setMainId(parentExposureMain.getId());
                    fexExposureService.saveEntity(exposure);
                    List<FexExposureDetailVO> listDtl = vo.getListDtl();
                    List<FexExposureDetail> listParentDtl = new ArrayList<>();
                    for (FexExposureDetailVO dtlVO : listDtl) {
                        FexExposureDetail dtl = new FexExposureDetail();
                        BeanUtils.copyNotNullProperties(dtlVO, dtl);
                        dtl.setId(null);
                        dtl.setExposureId(exposure.getId());
                        listParentDtl.add(dtl);
                    }
                    fexExposureDetailService.batchSave(listParentDtl);
                }
            }
        } else {
            //获取周期
            List<FexExposureVO> listParentPeriod = fexExposureService.listVOByMainId(parentExposureMain.getId(), curIds[0]);
            //先删除汇总单位的计划
            this.fexExposureService.deleteExposure(parentExposureMain.getId(), user);
            //根据上级单位获取所有子单位
            List<FexCorpExposureVO> childCorps = fexCorpExposureService.listCorpExposureByParentId(parentCorp.getCorpId());
            List<String> childCorpIds = new ArrayList<>();
            for (FexCorpExposureVO corpExposureVO:childCorps) {
                childCorpIds.add(corpExposureVO.getCorpId());
            }
            List<String> childMainIdList = fexExposureMainDao.getMainIdsByChildCorp(childCorpIds, parentExposureMain.getTemplateId(), beginDate, endDate);
            List<String> childCurIdList = fexExposureMainDao.getCurIdsByChildCorp(childMainIdList);
            String childCurIds = "";
            for (String curId:childCurIdList) {
                if (StringUtils.isBlank(childCurIds)) {
                    childCurIds = curId;
                } else {
                    childCurIds += CommonConstant.COMMA_SPLIT + curId;
                }
            }
            //重新生成汇总单位的主信息
            FexExposureMain parentExposureMainNew = new FexExposureMain();
            BeanUtils.copyNotNullProperties(parentExposureMain, parentExposureMainNew);
            parentExposureMainNew.setId(null);
            parentExposureMainNew.setCurId(childCurIds);
            parentExposureMainNew.setUpdateBy(user.getLoginName());
            parentExposureMainNew.setUpdateDate(DateUtils.getDate());
            this.saveEntity(parentExposureMainNew);
            //获取周期
            List<FexExposureVO> listPeriod = fexExposureService.listVOByMainId(fexExposureMain.getId(), curIds[0]);
            if (listPeriod == null || listPeriod.size() == 0) {
                listPeriod = listParentPeriod;
            }
            for (String curId:childCurIdList) {
                int i = 0;
                for (FexExposureVO period:listPeriod) {
                    FexExposure parentExposure = new FexExposure();
                    parentExposure.setMainId(parentExposureMainNew.getId());
                    parentExposure.setCurId(curId);
                    parentExposure.setSort(i++);
                    parentExposure.setBeginDate(period.getBeginDate());
                    parentExposure.setEndDate(period.getEndDate());
                    parentExposure.setFillUnit(period.getFillUnit());
                    fexExposureService.saveEntity(parentExposure);
                    // 获取上报明细
                    List<FexExposureDetailVO> listDtl = fexExposureDetailService.getDtlByChildCorp(childMainIdList, curId, period.getBeginDate(),period.getEndDate());
                    List<FexExposureDetail> listParentDtl = new ArrayList<>();
                    for (FexExposureDetailVO dtlVO:listDtl) {
                        FexExposureDetail dtl = new FexExposureDetail();
                        BeanUtils.copyNotNullProperties(dtlVO, dtl);
                        dtl.setId(null);
                        dtl.setExposureId(parentExposure.getId());
                        dtl.setCurId(curId);
                        listParentDtl.add(dtl);
                    }
                    fexExposureDetailService.batchSave(listParentDtl);
                }
            }
        }
    }

    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {

    }

    /**
     * 根据单位ID和单位模板获取对应的敞口上报信息
     *
     * @param corpId
     * @param templateId
     * @param beginDate
     * @param endDate
     * @return
     */
    public FexExposureMain getCorpExposure(String corpId, String templateId, Date beginDate, Date endDate) {
        return fexExposureMainDao.getCorpExposure(corpId, templateId, beginDate, endDate);
    }

    /**
     * 根据单位Id和模板id获取敞口上报信息
     * @param corpId
     * @param templateId
     * @return
     */
    public List<FexExposureMain> listByCorpIdAndTemplateId(String corpId, String templateId) {
        return fexExposureMainDao.listByCorpIdAndTemplateId(corpId, templateId);
    }
}
