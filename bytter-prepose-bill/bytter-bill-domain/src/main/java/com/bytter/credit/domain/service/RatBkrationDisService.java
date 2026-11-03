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
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationAddDetailDao;
import com.bytter.credit.domain.dao.RatBkrationDetailDao;
import com.bytter.credit.domain.dao.RatBkrationDisDao;
import com.bytter.credit.domain.dao.RatBkrationDisDetailDao;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 授信分配信息表
 * @date 2017-07-31 16:07:30
 */
@Service("ratBkrationDisService")
@Transactional
@WorkFlow(value = {RatWorkFlowConstant.RAT_BKRATION_DIS})
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_BKRATION_DIS)
public class RatBkrationDisService extends BaseService<RatBkrationDis, String>
        implements IWorkFlowListener, ICorpParentChangVerifyService, ICorpDisableVerifyService {
    @Autowired
    private RatBkrationDisDao ratBkrationDisDao;
    @Autowired
    private RatBkrationDisDetailDao disDetailDao;
    @Autowired
    private RatBkrationDetailDao ratDetailDao;
    @Autowired
    private RatBkrationAddDetailDao addDetailDao;
    @Autowired
    private RatBkrationAddService addService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private RatBkrationDetailService ratDetailService;
    @Autowired
    private RatCreditTypeService creditTypeService;
    @DubboReference
    private IConfigService configService;
    @DubboReference
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference
    private INotificationService notificationService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private RatBkrationAdjustService bkrationAdjustService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkrationDis entity) {
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
    public void save(RatBkrationDis entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkrationDis entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkrationDis entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<RatBkrationDis, String> getEntityDao() {
        return ratBkrationDisDao;
    }

    /**
     * 功能介绍 : 查询授信分配单位。<br>
     * <b>日期：</b>2017年9月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<CorpDTO> queryRationCorp(String corpSql) {
        return ratBkrationDisDao.queryRationCorp(corpSql);
    }

    /**
     * 功能介绍 : 二次授信对象控制模式。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRatctrModel() {
        return configService.getConfig(ConfigConstant.RAT, RationConstant.RAT_CONFIG_RATCTRMODEL);
    }

    /**
     * 功能介绍 : 查询授信录入复核通过待分配数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listDisOutputData(DataTablesInput input, String resource, String userId, List<String> corpIdArr) {
        String corpAuthSql;
        if(CollectionUtils.isEmpty(corpIdArr)){
            corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rb.corp_Id", true);
        } else {
            corpAuthSql = SqlUtil.getInSql(corpIdArr, "rb.corp_Id", true);
        }
        return bkrationService.setOutputPageData(ratBkrationDisDao.listDisOutputData(input, corpAuthSql));
    }

    /**
     * 功能介绍 : 查询银行授信可调剂、占用数据(已分配)。<br>
     * <b>日期：</b>2017年8月13日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRationDisCheckData(DataTablesInput input, String resource, String userId, List<String> corpIds, String itemCodes) {
        String corpAuthSql;
        if(CollectionUtils.isEmpty(corpIds)) {
            corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rb.corp_Id", true);
        }else{
            corpAuthSql = SqlUtil.getInSql(corpIds, "rb.corp_Id", true);
        }
        List<String> itemCodeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(itemCodes)) {
            itemCodeList = Arrays.asList(itemCodes.split(","));
        }
        DataTablesOutput<RatBkrationVO> output = ratBkrationDisDao.queryRationDisCheckData(input, itemCodeList, corpAuthSql);
        return bkrationService.setOutputPageData(output);
    }

    /**
     * 功能介绍 : 查询授信分配审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION_DIS,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratBkrationDisDao.queryRationDisApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }

    /**
     * 功能介绍 : 检查额度类型是否被使用。<br>
     * <b>日期：</b>2017年7月21日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean checkCreditTypeUse(String type) {
        List<RatBkrationDis> list = ratBkrationDisDao.listBy("type", type, MatchTypeEnum.EQ);
        return null != list && !list.isEmpty();
    }

    /**
     * 功能介绍 : 查询授信已分配信息。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRationDisData(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rd.corp_Id", true);
        DataTablesOutput<RatBkrationDisVO> output = ratBkrationDisDao.queryRationDisData(input, corpAuthSql);
        List<RatBkrationDisVO> data = output.getData();
        if (null == data || data.isEmpty()) {
            return output;
        }
        List<String> ratCodes = new ArrayList<String>();
        for (RatBkrationDisVO bkrationVO : data) {
            ratCodes.add(bkrationVO.getId());
        }
        List<RatBkRationDtlVm> dtlDTOs = disDetailService.getBkrationDtlByRatCodes(ratCodes);
        Map<String, List<RatBkRationDtlVm>> ratMap = new HashMap<String, List<RatBkRationDtlVm>>();
        if (null != dtlDTOs) {
            // 授信品种明细按授信编码分组
            for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationDtlVm> ratList = ratMap.get(dtlDTO.getRatCode());
                if (null == ratList) {
                    ratList = new ArrayList<RatBkRationDtlVm>();
                    ratMap.put(dtlDTO.getRatCode(), ratList);
                }
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationDisVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationDtlVm> ratList = ratMap.get(ratCode);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            // 综合品种条数
            Integer compItemSize = Integer.valueOf(0);
            boolean compFlag = false;
            // Integer itemOrder = Integer.valueOf(0);
            if (EmptyUtils.isEmptyList(ratList)) {
                continue;
            }
            for (RatBkRationDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                } else {
                    if (!compFlag) {// 没有设置过，则设置第一个品种的综合标识
                        dtlDTO.setFirstComp("1");
                        compFlag = true;
                    }
                    compItemSize++;
                }
                // 分配显示额度 = 分配额度+被调剂额度-调剂额度
                dtlDTO.setMoney(
                        dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setCompItemSize(compItemSize);// 综合品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }

    /**
     * 功能介绍 : 查询授信录入信息已分配过授信。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDis> getRatBkDisWayByRatCode(String ratCode, String corpId, String disWay) {
        return ratBkrationDisDao.getRatBkDisWayByRatCode(ratCode, corpId, disWay);
    }

    /**
     * 功能介绍 : 获取总额共享下其他单位的品种分配详情。<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public List<RatBkrationDis> getOtherRationDisByDisCode(String ratCode, String disCode, String itemFlag,
                                                           String itemCode) {
        return ratBkrationDisDao.getOtherRationDisByDisCode(ratCode, disCode, itemFlag, itemCode);
    }

    /**
     * 功能介绍 : 获取授信剩余额度。<br>
     * <b>日期：</b>2017年9月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public void getRationLeftMoney(RatBkrationPageVO pageVO) {
        String ratModel = pageVO.getRatMode();// 授信模式
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        // 银行授信应获取未使用额度作为剩余额度
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            RatBkrationMoneyBean ratBean = null;
            if (RationConstant.RATMODE_0.equals(ratModel)) {// 集团授信
                ratBean = getBankRationItemMoney(pageVO.getId(), pageVO.getCorpId(), dtlDTO.getItemFlag(),
                        dtlDTO.getItemCode());
                dtlDTO.setLeftMoney(ratBean.getUnUseMoney());
                dtlDTO.setLeftOpenMoney(ratBean.getUnUseOpenMoney());
                dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());// 剩余额度
                dtlDTO.setUnUserOpenMoney(dtlDTO.getLeftOpenMoney());// 剩余敞口额度
                money = money.add(dtlDTO.getUnUserMoney());
                openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
            } else {// 自主授信,取具体品种的剩余额度
                // 根据自主授信录入信息获取分配ID
                List<RatBkrationDis> disList = getRatBkDisWayByRatCode(pageVO.getId(), pageVO.getCorpId(),
                        RationConstant.DIS_TYPE_AUTO);
                RatBkrationDis dis = null;
                if (!EmptyUtils.isEmptyList(disList)) {
                    dis = disList.get(0);
                }
                if (null == dis) {
                    throw new ParameterException("");// FIXME
                }
                ratBean = getRationItemDisMoney(dis.getId(), dis.getRatCode(), dtlDTO.getItemFlag(),
                        dtlDTO.getItemCode());
                dtlDTO.setLeftMoney(ratBean.getLeftMoney());
                dtlDTO.setLeftOpenMoney(ratBean.getLeftOpenMoney());
                dtlDTO.setUnUserMoney(dtlDTO.getUnUserMoney());// 剩余额度
                dtlDTO.setUnUserOpenMoney(dtlDTO.getUnUserOpenMoney());// 剩余敞口额度
                money = money.add(dtlDTO.getLeftMoney());
                openMoney = openMoney.add(dtlDTO.getLeftOpenMoney());
            }
        }
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        BigDecimal compMoney = BigDecimal.ZERO;// 综合品种额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            RatBkrationMoneyBean ratBean = null;
            if (RationConstant.RATMODE_0.equals(ratModel)) {// 集团授信
                ratBean = getBankRationItemMoney(pageVO.getId(), pageVO.getCorpId(), dtlDTO.getItemFlag(),
                        dtlDTO.getItemCode());
                // 计算综合授信的剩余额度
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    dtlDTO.setLeftMoney(ratBean.getUnUseMoney());
                    dtlDTO.setLeftOpenMoney(ratBean.getUnUseOpenMoney());
                    dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());// 剩余额度
                    dtlDTO.setUnUserOpenMoney(dtlDTO.getLeftOpenMoney());// 剩余敞口额度
                    compMoney = dtlDTO.getUnUserMoney();
                    money = money.add(dtlDTO.getUnUserMoney());
                    openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
                }
            } else {// 自主授信,取具体品种的剩余额度
                List<RatBkrationDis> disList = getRatBkDisWayByRatCode(pageVO.getId(), pageVO.getCorpId(),
                        RationConstant.DIS_TYPE_AUTO);
                RatBkrationDis dis = null;
                if (!EmptyUtils.isEmptyList(disList)) {
                    dis = disList.get(0);
                }
                if (null == dis) {
                    throw new ParameterException("");// FIXME
                }
                ratBean = getRationItemDisMoney(dis.getId(), dis.getRatCode(), dtlDTO.getItemFlag(),
                        dtlDTO.getItemCode());
                // 计算综合授信的剩余额度
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    dtlDTO.setLeftMoney(ratBean.getLeftMoney());
                    dtlDTO.setLeftOpenMoney(ratBean.getLeftOpenMoney());
                    dtlDTO.setUnUserMoney(dtlDTO.getUnUserMoney());// 剩余额度
                    dtlDTO.setUnUserOpenMoney(dtlDTO.getUnUserOpenMoney());// 剩余敞口额度
                    compMoney = dtlDTO.getLeftMoney();
                    money = money.add(dtlDTO.getLeftMoney());
                    openMoney = openMoney.add(dtlDTO.getLeftOpenMoney());
                }
            }
            dtlDTO.setLeftMaxMoney(ratBean.getUnUseMaxMoney());
        }
        // 比较综合额度和综合单项额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            if (compMoney.compareTo(dtlDTO.getLeftMaxMoney()) < 0) {
                dtlDTO.setLeftMaxMoney(compMoney);
            }
        }
        pageVO.setUnUserMoney(money);// 待分配授信金额
        pageVO.setUnUserOpenMoney(openMoney);// 待分配敞口金额
        // TODO
        /*
         * List<RationDisBean> beans = null; if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(pageVO.getDisWay()))
         * {//总额共享 beans = getAmountShareMoney(pageVO); } else { beans = getOwnDisShareMoney(pageVO.getId(), null,
         * null); } Map<String, RationDisBean> disMap = list2Map(beans); //单品种 List<RatBkRationDtlVm> singRationDtls =
         * pageVO.getSingRationDtls(); for (RatBkRationDtlVm dtlDTO : singRationDtls) { RationDisBean disBean =
         * disMap.get(dtlDTO.getItemCode()); if (null != disBean) { dtlDTO.setLeftMoney(disBean.getMoney());
         * dtlDTO.setLeftOpenMoney(disBean.getOpenMoney()); } else {
         * dtlDTO.setLeftMoney(dtlDTO.getMoney().subtract(dtlDTO.getDisMoney()));
         * dtlDTO.setLeftOpenMoney(dtlDTO.getOpenMoney().subtract(dtlDTO.getDisOpenMoney())); }
         * dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());//剩余额度
         * dtlDTO.setUnUserOpenMoney(dtlDTO.getLeftOpenMoney());//剩余敞口额度 } //综合品种 List<RatBkRationDtlVm> compRationDtls
         * = pageVO.getCompRationDtls(); BigDecimal compMoney = BigDecimal.ZERO;//综合品种额度 for (RatBkRationDtlVm dtlDTO :
         * compRationDtls) { //计算综合授信的剩余额度 if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
         * RationDisBean disBean = disMap.get(dtlDTO.getItemCode()); if (null != disBean) {
         * dtlDTO.setLeftMoney(disBean.getMoney()); dtlDTO.setLeftOpenMoney(disBean.getOpenMoney()); } else {
         * dtlDTO.setLeftMoney(dtlDTO.getMoney().subtract(dtlDTO.getDisMoney()));
         * dtlDTO.setLeftOpenMoney(dtlDTO.getOpenMoney().subtract(dtlDTO.getDisOpenMoney())); }
         * dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());//剩余额度
         * dtlDTO.setUnUserOpenMoney(dtlDTO.getLeftOpenMoney());//剩余敞口额度 compMoney = dtlDTO.getUnUserMoney(); }
         * //计算剩余最高额度 RationDisBean disBean = disMap.get(dtlDTO.getItemCode()); if (null != disBean) {
         * dtlDTO.setLeftMaxMoney(disBean.getMaxMoney()); } else { dtlDTO.setLeftMaxMoney(dtlDTO.getMaxMoney()); } }
         *
         * //比较综合额度和综合单项额度 for (RatBkRationDtlVm dtlDTO : compRationDtls) { if
         * (compMoney.compareTo(dtlDTO.getLeftMaxMoney()) < 0) { dtlDTO.setLeftMaxMoney(compMoney); } }
         */
    }

    /**
     * 功能介绍 : 修改分配时查询待分配金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationCpAddListDTO viewRatRationDisInfo(String disCode) {
        RatBkrationDis ratBkrationDis = this.get(disCode, true);
        List<RatBkrationCpAddListDTO> listDTOs = ratBkrationDisDao.queryAllRationDisData(ratBkrationDis.getRatCode(), ratBkrationDis.getCorpId());
        //单位分配的授信只有一笔
        RatBkrationCpAddListDTO listDTO = listDTOs.get(0);
        //比较原录入信息与修改的分配数据
        listDTO.setDisCode(ratBkrationDis.getId());//分配ID
        listDTO.setDisWay(ratBkrationDis.getDistributeWay());//分配方式
        listDTO.setDisCorpId(ratBkrationDis.getCorpId());//二次分配对象单位
        listDTO.setType(ratBkrationDis.getType());//额度类型
        listDTO.setAttachfilepath(ratBkrationDis.getAttachmentId());
        listDTO.setViewFlag(true);
        //取分配明细数据
        List<String> disCodes = Lists.newArrayList();
        //分配编号
        disCodes.add(listDTO.getDisCode());
        //授信分配明细信息
        List<RatBkrationCpAddDtlDTO> dtlDTOs = getCprationDisDtlByDisCodes(disCodes);
        List<RatBkrationCpAddDtlDTO> singDtls = new ArrayList<RatBkrationCpAddDtlDTO>();
        List<RatBkrationCpAddDtlDTO> compDtls = new ArrayList<RatBkrationCpAddDtlDTO>();
        for (RatBkrationCpAddDtlDTO dtlDTO : dtlDTOs) {
            // 分配显示额度 = 分配额度+被调剂额度-调剂额度
            dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney()));
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                compDtls.add(dtlDTO);
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {// 综合授信使用标识
                    listDTO.setCompItemIndex(dtlDTO.getItemOrder());
                }
            } else {
                singDtls.add(dtlDTO);
            }
        }
        listDTO.setSingRationDtls(singDtls);// 单品种列表
        listDTO.setCompRationDtls(compDtls);// 综合品种列表
        listDTO.setSingItemSize(singDtls.size());
        listDTO.setCompItemSize(compDtls.size());
        //设置授信占用明细
        bkrationAdjustService.setRatBkrationApplyDtl(listDTO, ratBkrationDis.getRatCode());

        //分配方式确认
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(ratBkrationDis.getDistributeWay())) {//总额共享
            RatBkrationPageVO pageVO = bkrationService.getBkRationBeanById(ratBkrationDis.getRatCode());//授信录入明细
            pageVO.setViewFlag(true);
            setInitRationDisMoney(pageVO);
            setRationDisDetailMoney(pageVO, ratBkrationDis);
            setAmountShareMoney(pageVO);
        }
        return listDTO;
    }

    /**
     * 功能介绍 : 获取授信剩余额度。<br>
     * <b>日期：</b>2017年9月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public void viewRationLeftMoney(RatBkrationPageVO pageVO) {
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        // 银行授信应获取未使用额度作为剩余额度
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            RatBkrationMoneyBean ratBean = getBankRationItemMoney(pageVO.getId(), pageVO.getCorpId(),
                    dtlDTO.getItemFlag(), dtlDTO.getItemCode());// 品种未分配额度
            dtlDTO.setLeftMoney(ratBean.getLeftMoney());
            dtlDTO.setLeftOpenMoney(ratBean.getUnUseOpenMoney());
            dtlDTO.setUnUserMoney(ratBean.getUnUseMoney());// 剩余额度
            dtlDTO.setUnUserOpenMoney(ratBean.getUnUseOpenMoney());// 剩余敞口额度
            money = money.add(dtlDTO.getUnUserMoney());
            openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
        }
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        BigDecimal compMoney = BigDecimal.ZERO;// 综合品种额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            RatBkrationMoneyBean ratBean = getBankRationItemMoney(pageVO.getId(), pageVO.getCorpId(),
                    dtlDTO.getItemFlag(), dtlDTO.getItemCode());
            // 计算综合授信的剩余额度
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                dtlDTO.setLeftMoney(ratBean.getUnUseMoney());
                dtlDTO.setLeftOpenMoney(ratBean.getUnUseOpenMoney());
                dtlDTO.setUnUserMoney(dtlDTO.getLeftMoney());// 剩余额度
                dtlDTO.setUnUserOpenMoney(dtlDTO.getLeftOpenMoney());// 剩余敞口额度
                compMoney = dtlDTO.getUnUserMoney();
                money = money.add(dtlDTO.getUnUserMoney());
                openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
            }
            dtlDTO.setLeftMaxMoney(ratBean.getLeftMaxMoney());
        }
        // 比较综合额度和综合单项额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            if (compMoney.compareTo(dtlDTO.getLeftMaxMoney()) < 0) {
                dtlDTO.setLeftMaxMoney(compMoney);
            }
        }
        pageVO.setUnUserMoney(money);// 待分配授信金额
        pageVO.setUnUserOpenMoney(openMoney);// 待分配敞口金额
    }

    /**
     * 功能介绍 : 修改分配时查询待分配金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationPageVO editRatRationDisInfo(String disCode) {
        RatBkrationDis rationDis = get(disCode);
        String ratCode = rationDis.getRatCode();//授信编号
        String disWay = rationDis.getDistributeWay();//分配方式
        RatBkrationPageVO pageVO = bkrationService.getBkRationBeanById(ratCode);//授信录入明细
        pageVO.setViewFlag(false);//与录入的状态保持一致
        setInitRationDisMoney(pageVO);
        setRationDisDetailMoney(pageVO, rationDis);

        //分配方式确认
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {//总额共享
            setAmountShareMoney(pageVO);
        }
        return pageVO;
    }

    /**
     * 功能介绍 : 设置原分配信息。<br>
     * <b>日期：</b>2017年8月4日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setRationDisDetailMoney(RatBkrationPageVO pageVO, RatBkrationDis rationDis) {
        // 比较原录入信息与修改的分配数据
        pageVO.setDisCode(rationDis.getId());// 分配ID
        pageVO.setDisWay(rationDis.getDistributeWay());// 分配方式
        pageVO.setDisCorpId(rationDis.getCorpId());// 二次分配对象单位
        pageVO.setType(rationDis.getType());// 额度类型
        pageVO.setAttachfilepath(rationDis.getAttachmentId());
        List<String> disCodes = new ArrayList<String>();
        disCodes.add(rationDis.getId());
        List<RatBkRationDtlVm> dtlDTOs = disDetailService.getBkrationDtlByRatCodes(disCodes);
        Map<String, RatBkRationDtlVm> dtlMap = Maps.uniqueIndex(dtlDTOs, new Function<RatBkRationDtlVm, String>() {
            public String apply(RatBkRationDtlVm input) {
                return input.getItemCode();
            }
        });
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationDtlVm disDTO = dtlMap.get(itemCode);
            if (null != disDTO) {
                dtlDTO.setDisMoney(
                        disDTO.getMoney().add(disDTO.getSuperByReliefMoney()).subtract(disDTO.getSuperReliefMoney()));// 已分配金额
                dtlDTO.setDisOpenMoney(disDTO.getOpenMoney().add(disDTO.getSuperByReliefOpenMoney())
                        .subtract(disDTO.getSuperReliefOpenMoney()));// 已分配敞口额度
            } else {
                dtlDTO.setDisMoney(BigDecimal.ZERO);// 已分配金额
                dtlDTO.setDisOpenMoney(BigDecimal.ZERO);// 已分配敞口额度
            }
            money = money.add(dtlDTO.getDisMoney());
            openMoney = openMoney.add(dtlDTO.getDisOpenMoney());
        }
        // 综合品种
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationDtlVm disDTO = dtlMap.get(itemCode);
            if (null != disDTO) {
                dtlDTO.setDisMoney(
                        disDTO.getMoney().add(disDTO.getSuperByReliefMoney()).subtract(disDTO.getSuperReliefMoney()));// 已分配金额
                dtlDTO.setDisOpenMoney(disDTO.getOpenMoney().add(disDTO.getSuperByReliefOpenMoney())
                        .subtract(disDTO.getSuperReliefOpenMoney()));// 已分配敞口额度
                dtlDTO.setDisMaxMoney(
                        disDTO.getMaxMoney().add(disDTO.getByReliefMaxMoney()).subtract(disDTO.getReliefMaxMoney()));// 最高金额
            } else {
                dtlDTO.setDisMoney(BigDecimal.ZERO);// 已分配金额
                dtlDTO.setDisOpenMoney(BigDecimal.ZERO);// 已分配敞口额度
                dtlDTO.setDisMaxMoney(BigDecimal.ZERO);// 最高金额
            }
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                pageVO.setCompItemIndex(dtlDTO.getItemOrder());
            }
            money = money.add(dtlDTO.getDisMoney());
            openMoney = openMoney.add(dtlDTO.getDisOpenMoney());
            // dtlDTO.setLeftMaxMoney(dtlDTO.getMaxMoney().subtract(dtlDTO.getDisMaxMoney()));
        }
        pageVO.setDisMoney(money);
        pageVO.setDisOpenMoney(openMoney);
    }

    /**
     * 功能介绍 : 计算授信分配页面待分配金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public RatBkrationPageVO getRatRationDisInfo(String ratCode, String disWay) {
        RatBkrationPageVO pageVO = bkrationService.getBkRationBeanById(ratCode);// 授信录入明细
        // 是否已总额共享
        List<RatBkrationDis> hisDisList = getRatBkDisWayByRatCode(ratCode, null, RationConstant.DIS_TYPE_AMOUNT_SHARE);
        if (null != hisDisList && !hisDisList.isEmpty() && hisDisList.size() > 0) {
            disWay = RationConstant.DIS_TYPE_AMOUNT_SHARE;
        } else {
            // 是否自用
            hisDisList = getRatBkDisWayByRatCode(ratCode, null, RationConstant.DIS_TYPE_OWN);
            if (null != hisDisList && !hisDisList.isEmpty() && hisDisList.size() > 0) {
                disWay = RationConstant.DIS_TYPE_OWN;
            } else {
                disWay = RationConstant.DIS_TYPE_MONEY_SHARE;// 默认全额共享
            }
        }
        pageVO.setDisWay(disWay);
        setInitRationDisMoney(pageVO);
        // 分配方式确认
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disWay)) {// 总额共享
            setAmountShareMoney(pageVO);
        } else {
            // setOwnDisShareMoney(pageVO, null, null);
        }
        return pageVO;
    }

    /**
     * 功能介绍 : 初始化分配信息。<br>
     * <b>日期：</b>2017年8月4日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setInitRationDisMoney(RatBkrationPageVO pageVO) {
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            dtlDTO.setDisMoney(BigDecimal.ZERO);// 已分配金额
            dtlDTO.setDisOpenMoney(BigDecimal.ZERO);// 已分配敞口额度
        }
        // 综合品种
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            dtlDTO.setDisMoney(BigDecimal.ZERO);// 已分配金额
            dtlDTO.setDisOpenMoney(BigDecimal.ZERO);// 已分配敞口额度
            dtlDTO.setDisMaxMoney(BigDecimal.ZERO);// 最高金额
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                pageVO.setCompItemIndex(dtlDTO.getItemOrder());
            }
        }
        getRationLeftMoney(pageVO);
    }

    /**
     * 功能介绍 : 金额共享或自用模式下计算未分配额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setAmountShareMoney(RatBkrationPageVO pageVO) {
        List<RationDisBean> beans = getAmountShareMoney(pageVO);
        setUnUserMoney(pageVO, beans);
    }

    /**
     * 功能介绍 : 总额共享模式下计算未分配额度。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getAmountShareMoney(RatBkrationPageVO pageVO) {
        // 从银行授信表中获得所有可分配的额度（银行授信额度+总额共享单位追加额度）
        List<RationDisBean> beanA = ratDetailDao.getRationDisMoney(pageVO.getId());
        // 计算总额共享已占用的金额
        List<RationDisBean> beanB = disDetailDao.getURationDisMoney(pageVO.getId(),
                RationConstant.DIS_TYPE_AMOUNT_SHARE);
        List<RationDisBean> beanF = disDetailDao.getURationDisMoneySingle(pageVO.getId(),
                RationConstant.DIS_TYPE_AMOUNT_SHARE);
        // 计算自用已分配出去的
        List<RationDisBean> beanC = disDetailDao.getRationDisMoney(pageVO.getId(), RationConstant.DIS_TYPE_OWN);
        // 计算金额共享分配的金额
        List<RationDisBean> beanD = disDetailDao.getRationDisMoney(pageVO.getId(), RationConstant.DIS_TYPE_MONEY_SHARE);
        return addBeans(addBeans(addBeans(addBeans(beanA, beanF, "sub"), beanB, "sub"), beanC, "sub"), beanD, "sub");
    }

    /**
     * 功能介绍 : 设置待分配(敞口)金额。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setUnUserMoney(RatBkrationPageVO pageVO, List<RationDisBean> beans) {
        Map<String, RationDisBean> disMap = list2Map(beans);
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        // 单品种
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        for (RatBkRationDtlVm dtlDTO : singRationDtls) {
            RationDisBean disBean = disMap.get(dtlDTO.getItemCode());
            BigDecimal leftMoney = BigDecimal.ZERO;
            BigDecimal leftOpenMoney = BigDecimal.ZERO;
            if (null != disBean) {
                leftMoney = disBean.getMoney();
                leftOpenMoney = disBean.getOpenMoney();
            } else {
                leftMoney = dtlDTO.getMoney().subtract(dtlDTO.getDisMoney());
                leftOpenMoney = dtlDTO.getOpenMoney().subtract(dtlDTO.getDisOpenMoney());
            }
            // if (!pageVO.isViewFlag()) {
            // dtlDTO.setUnUserMoney(leftMoney.add(dtlDTO.getDisMoney()));//剩余额度
            // dtlDTO.setUnUserOpenMoney(leftOpenMoney.add(dtlDTO.getDisOpenMoney()));//剩余敞口额度
            // } else {
            // dtlDTO.setUnUserMoney(leftMoney);//剩余额度
            // dtlDTO.setUnUserOpenMoney(leftOpenMoney);//剩余敞口额度
            // }
            // 设置可用额度
            dtlDTO.setUnUserMoney(leftMoney.subtract(dtlDTO.getUnUserMoney()));// 分配额度-未分配额度
            dtlDTO.setUnUserOpenMoney(leftOpenMoney.subtract(dtlDTO.getUnUserOpenMoney()));
            // 存在总额共享时,设置leftMoney的值为总额共享的授信金额值
            List<RatBkrationDis> hisDisList = getRatBkDisWayByRatCode(dtlDTO.getRatCode(), null,
                    RationConstant.DIS_TYPE_AMOUNT_SHARE);
            if (!EmptyUtils.isEmpty(hisDisList)) {
                for (RatBkrationDis his : hisDisList) {
                    List<RatBkrationCpAddDtlDTO> disDtls = getRationDisDtlByItemCode(his.getId(), dtlDTO.getItemFlag(),
                            dtlDTO.getItemCode());
                    if (!EmptyUtils.isEmpty(disDtls)) {
                        if (pageVO.isViewFlag()) {
                            dtlDTO.setLeftMoney(disDtls.get(0).getMoney().add(disDtls.get(0).getSuperByReliefMoney())
                                    .subtract(disDtls.get(0).getSuperReliefMoney())
                                    .subtract(disDtls.get(0).getSuperProcessMoney())
                                    .subtract(disDtls.get(0).getReliefAndUrationMoney())
                                    .subtract(disDtls.get(0).getVirtualUration())
                                    .subtract(disDtls.get(0).getVirtualReliefMoney()));
                            dtlDTO.setLeftOpenMoney(disDtls.get(0).getOpenMoney());
                        } else {
                            dtlDTO.setLeftMoney(disDtls.get(0).getMoney());
                            dtlDTO.setLeftOpenMoney(disDtls.get(0).getOpenMoney());
                        }
                        break;
                    }
                }
            }
            if (!RationConstant.DIS_TYPE_AMOUNT_SHARE.equalsIgnoreCase(pageVO.getDisWay())) {
                money = money.add(dtlDTO.getUnUserMoney());
                openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
            } else {
                money = money.add(dtlDTO.getLeftMoney());
                openMoney = openMoney.add(dtlDTO.getLeftOpenMoney());
            }
        }
        // 综合品种
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        BigDecimal compMoney = BigDecimal.ZERO;// 综合品种额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            // 计算综合授信的剩余额度
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                RationDisBean disBean = disMap.get(dtlDTO.getItemCode());
                BigDecimal leftMoney = BigDecimal.ZERO;
                BigDecimal leftOpenMoney = BigDecimal.ZERO;
                if (null != disBean) {
                    leftMoney = disBean.getMoney();
                    leftOpenMoney = disBean.getOpenMoney();
                } else {
                    leftMoney = dtlDTO.getMoney().subtract(dtlDTO.getDisMoney());
                    leftOpenMoney = dtlDTO.getOpenMoney().subtract(dtlDTO.getDisOpenMoney());
                }
                // if (!pageVO.isViewFlag()) {
                // dtlDTO.setUnUserMoney(leftMoney.add(dtlDTO.getDisMoney()));//剩余额度
                // dtlDTO.setUnUserOpenMoney(leftOpenMoney.add(dtlDTO.getDisOpenMoney()));//剩余敞口额度
                // } else {
                // dtlDTO.setUnUserMoney(leftMoney);//剩余额度
                // dtlDTO.setUnUserOpenMoney(leftOpenMoney);//剩余敞口额度
                // }
                // 设置可用额度
                dtlDTO.setUnUserMoney(leftMoney.subtract(dtlDTO.getUnUserMoney()));// 分配额度-未分配额度
                dtlDTO.setUnUserOpenMoney(leftOpenMoney.subtract(dtlDTO.getUnUserOpenMoney()));
                // 存在总额共享时,设置leftMoney的值为总额共享的授信金额值
                List<RatBkrationDis> hisDisList = getRatBkDisWayByRatCode(dtlDTO.getRatCode(), null,
                        RationConstant.DIS_TYPE_AMOUNT_SHARE);
                if (!EmptyUtils.isEmpty(hisDisList)) {
                    for (RatBkrationDis his : hisDisList) {
                        List<RatBkrationCpAddDtlDTO> disDtls = getRationDisDtlByItemCode(his.getId(),
                                dtlDTO.getItemFlag(), dtlDTO.getItemCode());
                        if (!EmptyUtils.isEmpty(disDtls)) {
                            if (pageVO.isViewFlag()) {
                                dtlDTO.setLeftMoney(
                                        disDtls.get(0).getMoney().add(disDtls.get(0).getSuperByReliefMoney())
                                                .subtract(disDtls.get(0).getSuperReliefMoney())
                                                .subtract(disDtls.get(0).getSuperProcessMoney())
                                                .subtract(disDtls.get(0).getReliefAndUrationMoney())
                                                .subtract(disDtls.get(0).getVirtualUration())
                                                .subtract(disDtls.get(0).getVirtualReliefMoney()));
                                dtlDTO.setLeftOpenMoney(disDtls.get(0).getOpenMoney());
                            } else {
                                dtlDTO.setLeftMoney(disDtls.get(0).getMoney());
                                dtlDTO.setLeftOpenMoney(disDtls.get(0).getOpenMoney());
                            }
                            break;
                        }
                    }
                }
                if (!RationConstant.DIS_TYPE_AMOUNT_SHARE.equalsIgnoreCase(pageVO.getDisWay())) {
                    compMoney = dtlDTO.getUnUserMoney();
                    money = money.add(dtlDTO.getUnUserMoney());
                    openMoney = openMoney.add(dtlDTO.getUnUserOpenMoney());
                } else {
                    compMoney = dtlDTO.getLeftMoney();
                    money = money.add(dtlDTO.getLeftMoney());
                    openMoney = openMoney.add(dtlDTO.getLeftOpenMoney());
                }
            }
            // 计算剩余最高额度
            RationDisBean disBean = disMap.get(dtlDTO.getItemCode());
            if (null != disBean) {
                if (!pageVO.isViewFlag()) {
                    dtlDTO.setMaxMoney(disBean.getMaxMoney().add(dtlDTO.getDisMaxMoney()));
                } else {
                    dtlDTO.setMaxMoney(disBean.getMaxMoney());
                }
                dtlDTO.setLeftMaxMoney(disBean.getMaxMoney());
            } else {
                dtlDTO.setLeftMaxMoney(dtlDTO.getMaxMoney());
            }
        }
        // 比较综合额度和综合单项额度
        for (RatBkRationDtlVm dtlDTO : compRationDtls) {
            if (compMoney.compareTo(dtlDTO.getLeftMaxMoney()) < 0) {
                dtlDTO.setLeftMaxMoney(compMoney);
            }
        }
        pageVO.setUnUserMoney(money);// 待分配授信金额
        pageVO.setUnUserOpenMoney(openMoney);// 待分配敞口金额
    }

    /**
     * 功能介绍 : 授信品种余额相加减。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private List<RationDisBean> addBeans(List<RationDisBean> beanA, List<RationDisBean> beanB, String opCode) {
        List<RationDisBean> beans = new ArrayList<RationDisBean>();
        if (null == beanB || beanB.isEmpty()) {
            return beanA;
        }
        Map<String, RationDisBean> disMap = list2Map(beanB);
        for (RationDisBean disBean : beanA) {
            String itemCode = disBean.getItemCode();
            RationDisBean tempBean = disMap.get(itemCode);
            if (null != tempBean) {
                if ("add".equals(opCode)) {
                    beans.add(disBean.addBean(tempBean));
                } else {
                    beans.add(disBean.subBean(tempBean));
                }
            } else {
                beans.add(disBean);
            }
        }
        return beans;
    }

    /**
     * 功能介绍 : list to Map。<br>
     * <b>日期：</b>2017年8月3日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private Map<String, RationDisBean> list2Map(List<RationDisBean> beans) {
        Map<String, RationDisBean> map = Maps.uniqueIndex(beans, new Function<RationDisBean, String>() {
            public String apply(RationDisBean from) {
                return from.getItemCode();
            }
        });
        return map;
    }

    /**
     * 功能介绍 ： 保存分配信息。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRatBkrationDis(RatBkrationDisListDTO disDTO, String userId) {
        checkBefore(disDTO);
        // 根据分配方式处理
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disDTO.getDistributeWay())) {
            saveAmountShareRationDis(disDTO, userId);
            // 总额共享模式下不需要修改授信录入明细下的已分配金额
        } else {
            CorpDTO corp = corpService.get(disDTO.getCorpId());
            disDTO.setCorpName(corp.getName());
            saveOwnBkrationDis(disDTO, disDTO.getCorpId(), userId, corp.getCode());
            // TODO 挂审批通过根据授信分配信息更新授信录入信息的已分配金额
            if (CommonConstant.STATUE_CHECK.equals(disDTO.getStatus())) {
                bkrationService.updateRationDisMoney(disDTO);
            }
        }
        notificationService.complete(NotificationConstant.CMS.RAT_DIS, disDTO.getRatCode(), userId);
        getEntityDao().flush();
    }

    /**
     * 功能介绍 : 授信分配前检查额度。<br>
     * <b>日期：</b>2017年9月22日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void checkBefore(RatBkrationDisListDTO disDTO) {
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        if (!EmptyUtils.isEmptyList(rationDtls)) {
            boolean flag = true;
            for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
                if (!dtlDTO.getMoney().equals(BigDecimal.ZERO)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_MONEY_NULL);
            }
        } else {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_MONEY_NULL);
        }
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disDTO.getDistributeWay())) {// 总额共享不校验分配额度
            return;
        }
        // 检查分配后剩余额度
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            RatBkration bkration = bkrationService.get(disDTO.getRatCode(), true);
            RatBkrationMoneyBean ratBean = getBankRationItemMoney(bkration.getId(), bkration.getCorpId(),
                    dtlDTO.getItemFlag(), dtlDTO.getItemCode());
            BigDecimal leftMoney = ratBean.getMoney().subtract(ratBean.getDisMoney());// 剩余额度
            if (dtlDTO.getMoney().compareTo(leftMoney) > 0) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_MONEY_ENOUGH);
            }
            BigDecimal leftOpenMoney = ratBean.getOpenMoney().subtract(ratBean.getDisOpenMoney());// 剩余敞口额度
            if (dtlDTO.getOpenMoney().compareTo(leftOpenMoney) > 0) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_MONEY_ENOUGH);
            }
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {// 综合授信比较最高额度
                BigDecimal leftMaxMoney = ratBean.getMaxMoney().subtract(ratBean.getLeftMaxMoney());
                if (dtlDTO.getMaxMoney().compareTo(leftMaxMoney) > 0) {
                    throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_MONEY_ENOUGH);
                }
            }
        }
    }

    /**
     * 功能介绍 : 总额共享下更新分配额度。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateBkRationDisMoney(RatBkrationDisListDTO disDTO) {
        if (CommonConstant.STATUE_CHECK.equals(disDTO.getStatus())) {
            // 获取单位的授信分配信息
            List<RatBkrationDis> disList = getRatBkDisWayByRatCode(disDTO.getRatCode(), disDTO.getCorpId(), null);
            if (!EmptyUtils.isEmptyList(disList)) {
                RatBkrationDis dis = disList.get(0);
                // 是否是第一个总额共享的单位，如果是，则更新授信录入的分配额度
                if (!StringUtils.isEmpty(dis.getDistributeWay()) && ("2").equals(dis.getDistributeWay())) {
                    RatBkrationDis rationDis = getRationDisByCorpRatCode(disDTO, RationConstant.DIS_TYPE_AMOUNT_SHARE);
                    if (null == rationDis) {// 第一笔才需要写入分配总额
                        bkrationService.updateRationDisMoney(disDTO);
                    } else {
                        // 如果存在新分配的总额共享额度大于已分配的额度，则更新原因的分配额度
                        if (existRationUnDisMoney(disDTO.getRatCode())) {
                            updateRationDisAmountMoney(disDTO);
                        }
                    }
                } else {// 自用与金额
                    bkrationService.updateRationDisMoney(disDTO);
                }
            }
        }
    }

    /**
     * 功能介绍 : 更新总额共享下其他未分配的单位的额度。<br>
     * <b>日期：</b>2017年10月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisAmountMoney(RatBkrationDisListDTO disDTO) {
        // 获取审批通过的其他单位总额共享的分配数据
        List<RatBkrationDis> list = ratBkrationDisDao.getRatOtherCorpDisByRatCode(disDTO.getRatCode(),
                disDTO.getCorpId(), RationConstant.DIS_TYPE_AMOUNT_SHARE);
        if (EmptyUtils.isEmptyList(list)) {
            return;
        }
        Integer count = Integer.valueOf(0);
        for (RatBkrationDis bkrationDis : list) {
            setRationDisAmountMoney(bkrationDis, disDTO.getRationDtls(), count);
            count++;
        }
    }

    /**
     * 功能介绍 : 比较原总额共享额度与新分配的额度大小。<br>
     * <b>日期：</b>2017年10月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setRationDisAmountMoney(RatBkrationDis bkrationDis, List<RatBkrationDisDtlDTO> rationDtls,
                                         Integer count) {
        String disCode = bkrationDis.getId();
        List<RatBkrationDisDetail> disDetails = disDetailService.getRationDisDtlByDisCode(disCode, null, null);// 分配明细
        if (EmptyUtils.isEmptyList(disDetails)) {
            return;
        }
        Map<String, RatBkrationDisDetail> disMap = Maps.uniqueIndex(disDetails,
                new Function<RatBkrationDisDetail, String>() {
                    @Override
                    public String apply(RatBkrationDisDetail input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkrationDetail> bkrationDetails = ratDetailService.getBkrationDetailByRatCode(bkrationDis.getRatCode());// 录入明细
        Map<String, RatBkrationDetail> dtlMap = Maps.uniqueIndex(bkrationDetails,
                new Function<RatBkrationDetail, String>() {
                    @Override
                    public String apply(RatBkrationDetail input) {
                        return input.getItemCode();
                    }
                });
        for (RatBkrationDisDtlDTO disDTO : rationDtls) {
            RatBkrationDisDetail disDetail = disMap.get(disDTO.getItemCode());
            if (null == disDetail) {// 新分配的授信品种,原有的授信分配都新增
                disDetail = new RatBkrationDisDetail();
                BeanUtils.copyProperties(disDTO, disDetail);
                disDetail.setDisCode(disCode);
                disDetail.setCompSign(RationConstant.RAT_COMP_SIGN_0);// 单品种
                disDetailService.saveRatBkrationDisDetail(disDetail);
                updateRationkDisMoney(disDTO, dtlMap, disDetail, count);
            } else {
                if (disDTO.getMoney().compareTo(disDetail.getMoney()) > 0
                        || disDTO.getOpenMoney().compareTo(disDetail.getOpenMoney()) > 0
                        || disDTO.getMaxMoney().compareTo(disDetail.getMaxMoney()) > 0) {// 如果新总额共享的额度大，则以新分配的共享额度为准
                    // 更新已分配额度
                    updateRationkDisMoney(disDTO, dtlMap, disDetail, count);
                    disDetail.setMoney(disDTO.getMoney());// 重新设置授信分配额度
                    disDetail.setOpenMoney(disDTO.getOpenMoney());
                    disDetail.setMaxMoney(disDTO.getMaxMoney());
                    disDetailService.update(disDetail);
                }
            }
        }
    }

    /**
     * 功能介绍 : 更新授信录入已分配额度。<br>
     * <b>日期：</b>2017年10月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void updateRationkDisMoney(RatBkrationDisDtlDTO disDTO, Map<String, RatBkrationDetail> dtlMap,
                                       RatBkrationDisDetail disDetail, Integer count) {
        // 更新已分配额度
        if (count.equals(Integer.valueOf(0))) {
            RatBkrationDetail detail = dtlMap.get(disDTO.getItemCode());// 授信录入品种
            if (null != detail) {
                detail.setSuperDistributeMoney(
                        detail.getSuperDistributeMoney().add(disDTO.getMoney().subtract(disDetail.getMoney())));// 已分配授信金额
                detail.setSuperDistributeOpenMoney(detail.getSuperDistributeOpenMoney()
                        .add(disDTO.getOpenMoney().subtract(disDetail.getOpenMoney())));// 已分配授信金额
                ratDetailService.updateEntity(detail);// 更新已分配金额
            }
        }
    }

    /**
     * 功能介绍 : 获取银行授信 的分配信息。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationDis getRationDisByCorpRatCode(RatBkrationDisListDTO disDTO, String disWay) {
        List<RatBkrationDis> list = ratBkrationDisDao.getRatOtherCorpDisByRatCode(disDTO.getRatCode(),
                disDTO.getCorpId(), disWay);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 功能介绍 : 总额共享分配方式处理授信分配数据。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveAmountShareRationDis(RatBkrationDisListDTO disDTO, String userId) {
        String corpIds = disDTO.getCorpId();
        String[] corpIdArr = corpIds.split(",");// 多个单位共享
        // 是否存在已分配的总额共享分配
        boolean disFlag = false;
        List<RatBkrationDis> hisDisList = ratBkrationDisDao.getOtherRatDisByRatCode(disDTO.getRatCode(), null,
                RationConstant.DIS_TYPE_AMOUNT_SHARE, disDTO.getId());
        if (!EmptyUtils.isEmptyList(hisDisList)) {
            disFlag = true;
        }
        Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(Arrays.asList(corpIdArr));
        for (String corpId : corpIdArr) {
            disDTO.setCorpId(corpId);
            disDTO.setCorpName(corpMap.get(corpId).getName());
            if (disFlag) {
                if (existRationUnDisMoney(disDTO.getRatCode())) {// 存在未分配的额度，则按照页面录入分配
                    saveOwnBkrationDis(disDTO, corpId, userId, corpMap.get(corpId).getCode());
                } else {
                    // 取任意一笔总额共享授信分配数据进行分配
                    RatBkrationDis bkrationDis = hisDisList.get(0);
                    copyAmountShareRationDis(bkrationDis, disDTO, corpId, userId);
                }
            } else {
                saveOwnBkrationDis(disDTO, corpId, userId, corpMap.get(corpId).getCode());
            }
        }
        // TODO 是否存在未分配完的授信额度,是否将未分配完的额度分配给共享的单位
    }

    /**
     * 功能介绍 : 是否存在总额共享下未分配的剩余额度。<br>
     * <b>日期：</b>2017年10月9日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public boolean existRationUnDisMoney(String ratCode) {
        List<RatBkRationDtlVm> dtlDTOs = ratDetailDao.getBkrationDtlDTOByRatCode(ratCode, null);
        if (EmptyUtils.isEmptyList(dtlDTOs)) {
            return true;// 不存在未分配的其他剩余额度
        }
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            BigDecimal leftMoney = dtlDTO.getMoney().add(dtlDTO.getSuperAddMoney())
                    .subtract(dtlDTO.getSuperDistributeMoney());// 剩余额度=录入额度+追加额度-分配额度
            BigDecimal leftOpenMoney = dtlDTO.getOpenMoney().add(dtlDTO.getSuperAddOpenMoney())
                    .subtract(dtlDTO.getSuperDistributeOpenMoney());// 剩余额度=录入额度+追加额度-分配额度
            if (leftMoney.compareTo(BigDecimal.ZERO) != 0 || leftOpenMoney.compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 功能介绍 :根据已有分配数据生成新分配信息。<br>
     * <b>日期：</b>2017年9月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void copyAmountShareRationDis(RatBkrationDis bkrationDis, RatBkrationDisListDTO disDTO, String corpId,
                                         String userId) {
        // 保存前数据校验，同一单位、银行、币别、品种品种是否重复或包含，不允许保存
        checkRatRationDisForAdd(disDTO);
        RatBkrationDis rationDis = new RatBkrationDis();
        if (!StringUtils.isEmpty(disDTO.getId())) {
            rationDis = get(disDTO.getId());
        }
        BeanUtils.copyProperties(bkrationDis, rationDis, "id", "status", "type");
        rationDis.setCorpId(corpId);
        rationDis.setDisCode(
                bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_DISCODE, RationConstant.RAT_BKRATION_DIS_CODE));// 授信分配编号
        rationDis.setIspassed(RationConstant.RAT_COMMON_N);// 未通过
        rationDis.setCreateBy(userId);
        rationDis.setCreateDate(DateUtils.getDate());
        rationDis.setStatus(disDTO.getStatus());// 状态
        rationDis.setType(disDTO.getType());
        ratBkrationDisDao.saveOrUpdate(rationDis);// 保存分配主表
        List<RatBkrationDisDetail> disDetails = disDetailService.getRationDisDtlByDisCode(bkrationDis.getId(), null,
                null);
        // 复制分配信息
        for (RatBkrationDisDetail disDetail : disDetails) {
            RatBkrationDisDetail newDtl = new RatBkrationDisDetail();
            BeanUtils.copyProperties(disDetail, newDtl, "id");
            // 设置占用为虚拟占用额度
            newDtl.setVirtualUration(newDtl.getVirtualUration().add(disDetail.getFreezeMoney()));// 冻结额度
            newDtl.setVirtualOpenUration(newDtl.getVirtualOpenUration().add(disDetail.getFreezeOpenMoney()));// 冻结敞口额度
            newDtl.setReliefAndUrationMoney(newDtl.getReliefAndUrationMoney().add(disDetail.getSuperProcessMoney()));// 占用额度
            newDtl.setUrationOpenMoney(newDtl.getUrationOpenMoney().add(disDetail.getSuperProcessOpenMoney()));// 占用敞口额度
            newDtl.setFreezeMoney(BigDecimal.ZERO);
            newDtl.setFreezeOpenMoney(BigDecimal.ZERO);
            newDtl.setSuperProcessMoney(BigDecimal.ZERO);
            newDtl.setSuperProcessOpenMoney(BigDecimal.ZERO);
            newDtl.setVirtualMaxMoney(
                    newDtl.getVirtualMaxMoney().add(newDtl.getProcessMaxMoney().add(newDtl.getFreezeMaxMoney())));// 虚拟占用额度
            newDtl.setProcessMaxMoney(BigDecimal.ZERO);
            newDtl.setFreezeMaxMoney(BigDecimal.ZERO);
            newDtl.setDisCode(rationDis.getId());// 分配ID
            disDetailService.saveRatBkrationDisDetail(newDtl);
        }
        // 挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(rationDis.getStatus())) {
            getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_DIS, rationDis.getId(), rationDis,
                    rationDis.getRatCorpId(), userId);
        }
    }

    /**
     * 授信自用和单位金额共享分配
     *
     * @param disDTO   新增参数
     * @param corpId   单位id
     * @param userId   用户id
     * @param corpCode 单位名称
     */
    public void saveOwnBkrationDis(RatBkrationDisListDTO disDTO, String corpId, String userId, String corpCode) {
        // 保存前数据校验，同一单位、银行、币别、品种品种是否重复或包含，不允许保存
        checkRatRationDisForAdd(disDTO);
        RatBkrationDis rationDis = new RatBkrationDis();
        if (!StringUtils.isEmpty(disDTO.getId())) {
            rationDis = get(disDTO.getId());
        }
        BeanUtils.copyProperties(disDTO, rationDis);
        rationDis.setCorpId(corpId);
        rationDis.setMoney(disDTO.getMoneysum());// 授信合计金额
        rationDis.setCorpName(disDTO.getCorpName());
        rationDis.setCorpCode(corpCode);
        rationDis.setOpenMoney(disDTO.getOpenMoneysum());// 敞口合计
        rationDis.setDisCode(bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_DISCODE, RationConstant.RAT_BKRATION_DIS_CODE));// 授信分配编号
        rationDis.setIspassed(RationConstant.RAT_COMMON_N);// 未通过
        rationDis.setCreateBy(userId);
        rationDis.setCreateDate(DateUtils.getDate());
        ratBkrationDisDao.saveOrUpdate(rationDis);// 保存分配主表
        List<RatBkrationDisDetail> details = new ArrayList<RatBkrationDisDetail>();
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        details = BeanUtils.copyListProperties(rationDtls, RatBkrationDisDetail.class);
        // 保存分配明细
        for (RatBkrationDisDetail disDetail : details) {
            disDetail.setDisCode(rationDis.getId());// 分配主表ID
            disDetailService.saveRatBkrationDisDetail(disDetail);
        }
        // 记录授信分配操作日志
        // 挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(rationDis.getStatus())) {
            getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_DIS, rationDis.getId(), rationDis,
                    rationDis.getRatCorpId(), userId);
        }
    }

    /**
     * 功能介绍 : 检查授信品种是否可分配。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkRatRationDisForAdd(RatBkrationDisListDTO disDTO) {
        // 授信银行编号+币别+单位+银行+授信日期[不过期]+品种来校验是否可以再次分配给某个单位
        checkCorpRationDisWay(disDTO);
        RatBkRationVm bkrationDTO = bkrationService.geBkrationListDTOByRatCode(disDTO.getRatCode(), null);
        disDTO.setCurCode(bkrationDTO.getCurCode());// 币别
        disDTO.setBankCode(bkrationDTO.getBankCode());// 授信银行
        List<RatBkRationVm> rationVOs = bkrationService.getBkRationDised(disDTO);
        if (null == rationVOs || rationVOs.isEmpty()) {
            return;
        }
        for (RatBkRationVm dto : rationVOs) {
            // 比较授信日期是否包含或交叉
            if (compareDateRelation(bkrationDTO, dto)) {
                Map<String, List<RatBkRationDtlVm>> destMap = groupDtlDtos(dto);
                List<RatBkRationDtlVm> destDtlsSingle = destMap.get(RationConstant.RAT_COMFLAG_0);// 单授信品种
                List<RatBkRationDtlVm> destDtlsComp = destMap.get(RationConstant.RAT_COMFLAG_1);// 综合授信品种
                // 判断授信品种是否相等
                compareRatRationBeanRepeat(bkrationDTO, trans2DtlMap(destDtlsSingle), trans2DtlMap(destDtlsComp));
            }
        }
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        if (EmptyUtils.isEmptyList(rationDtls)) {
            return;
        }
        boolean maxFlag = false;// 最高额度录入标识
        boolean moneyFlag = false;// 综合额度录入标识
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                    moneyFlag = true;// 录入了综合额度
                }
            }
        }
        if (moneyFlag) {
            for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
                if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                    if (dtlDTO.getMaxMoney().compareTo(BigDecimal.ZERO) != 0) {
                        maxFlag = true;
                        break;
                    }
                }
            }
            if (!maxFlag) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_COMPITEM_ZERO);
            }
        }
    }

    /**
     * 功能介绍 : 比较品种是否重复或包含。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void compareRatRationBeanRepeat(RatBkRationVm dest, Map<String, RatBkRationDtlVm> souSingMap,
                                           Map<String, RatBkRationDtlVm> souCompMap) {
        // 比较授信品种是否重复
        Map<String, List<RatBkRationDtlVm>> destMap = groupDtlDtos(dest);
        List<RatBkRationDtlVm> destDtlsSingle = destMap.get(RationConstant.RAT_COMFLAG_0);// 单授信品种
        List<RatBkRationDtlVm> destDtlsComp = destMap.get(RationConstant.RAT_COMFLAG_1);// 综合授信品种
        String destItemCode = "";
        // 单授信品种比较
        for (RatBkRationDtlVm destDTO : destDtlsSingle) {
            destItemCode = destDTO.getItemCode();// 授信品种编码
            RatBkRationDtlVm sourceDTO = souSingMap.get(destItemCode);
            if (null != sourceDTO) {
                // 重复分配，二次授信单位{0}已经分配过授信
                CorpDTO corpDTO = corpService.get(dest.getCorpId());
                String corpName = "";
                if (null != corpDTO) {
                    corpName = corpDTO.getName();
                }
                throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_EXIST, corpName);
            }
        }
        boolean compFlag = false;
        if ((null == destDtlsComp || destDtlsComp.isEmpty()) && souCompMap.size() == 0) {
            return;
        }
        // 综合授信的明细长度一致
        if (destDtlsComp.size() == souCompMap.size()) {
            compFlag = true;
        } else {
            return;
        }
        // 综合授信品种比较,每一个品种都相等
        for (RatBkRationDtlVm destDTO : destDtlsComp) {
            destItemCode = destDTO.getItemCode();// 授信品种编码
            RatBkRationDtlVm sourceDTO = souCompMap.get(destItemCode);
            if (null != sourceDTO) {
                compFlag = true;
                break;
            } else {
                compFlag = false;
            }
        }
        if (compFlag) {
            // 重复分配，二次授信单位{0}已经分配过综合授信
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_EXIST);
        }
    }

    /**
     * 功能介绍 : List2Map。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private Map<String, RatBkRationDtlVm> trans2DtlMap(List<RatBkRationDtlVm> dtlDTOs) {
        Map<String, RatBkRationDtlVm> dtlMap = new HashMap<String, RatBkRationDtlVm>();
        for (RatBkRationDtlVm dtlDTO : dtlDTOs) {
            dtlMap.put(dtlDTO.getItemCode(), dtlDTO);
        }
        return dtlMap;
    }

    /**
     * 功能介绍 : 授信品种按品种标识分类。<br>
     * <b>日期：</b>2017年7月25日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private Map<String, List<RatBkRationDtlVm>> groupDtlDtos(RatBkRationVm dto) {
        Map<String, List<RatBkRationDtlVm>> dtlMap = new HashMap<String, List<RatBkRationDtlVm>>();
        List<RatBkRationDtlVm> dtlsSingle = new ArrayList<RatBkRationDtlVm>();// 单授信品种
        List<RatBkRationDtlVm> dtlsComp = new ArrayList<RatBkRationDtlVm>();// 综合授信品种
        // 先分成单品种和综合品种
        for (RatBkRationDtlVm dtlDTO : dto.getRationDtls()) {
            if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                dtlsSingle.add(dtlDTO);
            } else {
                dtlsComp.add(dtlDTO);
            }
        }
        dtlMap.put(RationConstant.RAT_COMFLAG_0, dtlsSingle);
        dtlMap.put(RationConstant.RAT_COMFLAG_1, dtlsComp);
        return dtlMap;
    }

    /**
     * 功能介绍 : 判断两个授信的日期是否交叉或包含。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private boolean compareDateRelation(RatBkRationVm dest, RatBkRationVm source) {
        Date destStartDate = dest.getBegindate();// 起始日期
        Date destEndDate = dest.getEnddate();// 结束日期
        Date sourceBeginDate = source.getBegindate();// 已保存的起始日期
        Date sourceEndDate = source.getEnddate();// 已保存的结束日期
        // 两条授信时间是一致
        // 两条授信时间重叠
        if ((destStartDate.getTime() == sourceBeginDate.getTime())
                && (destEndDate.getTime() == sourceEndDate.getTime())) {
            return true;
        } else return destStartDate.getTime() >= sourceBeginDate.getTime()
                && destStartDate.getTime() <= sourceEndDate.getTime()
                && destEndDate.getTime() >= sourceEndDate.getTime();
    }

    /**
     * 功能介绍 : 根据分配方式检查单位是否已分配过授信。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkCorpRationDisWay(RatBkrationDisListDTO disDTO) {
        List<RatBkrationDis> list = ratBkrationDisDao.queryCorpRationDis(disDTO);
        if (null != list && !list.isEmpty()) {
            // 二次授信单位{0}已经分配过授信
            CorpDTO corp = corpService.get(disDTO.getCorpId());
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_CORP_EXIST, corp.getName());
        }
    }

    /**
     * 功能介绍 ： 保存分配信息。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void editRatBkrationDis(RatBkrationDisListDTO disDTO, String userId) {
        checkBefore(disDTO);
        // 删除分配明细，再保存修改的分配明细数据
        disDetailService.deleteBkrationDisDtlByDisCode(disDTO);
        notificationService.complete(NotificationConstant.CMS.RAT_DIS, disDTO.getId(), userId);
        // 根据分配方式处理
        if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disDTO.getDistributeWay())) {
            saveAmountShareRationDis(disDTO, userId);
            // 总额共享模式下不需要修改授信录入明细下的已分配金额
        } else {
            CorpDTO corp = corpService.get(disDTO.getCorpId());
            disDTO.setCorpName(corp.getName());
            saveOwnBkrationDis(disDTO, disDTO.getCorpId(), userId, corp.getCode());
            // TODO 挂审批通过根据授信分配信息更新授信录入信息的已分配金额
            if (CommonConstant.STATUE_CHECK.equals(disDTO.getStatus())) {
                bkrationService.updateRationDisMoney(disDTO);
            }
        }
    }

    /**
     * 功能介绍 : 银行授信是否进行了总额共享分配 。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> rationIsTotalMoneyDis(String ratCode) {
        return ratBkrationDisDao.rationIsTotalMoneyDis(ratCode);
    }

    /**
     * 功能介绍 : 查询授信录入信息已审批通过的分配授信。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationDis> getRatBkDisByRatCode(String ratCode, String corpId, String disWay) {
        return ratBkrationDisDao.getRatBkDisByRatCode(ratCode, corpId, disWay);
    }

    /**
     * 功能介绍 : 获取银行授信自用分配的授信明细。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationDisListDTO geBkrationDisListDTOById(String ratCode, String corpId) {
        RatBkrationDisListDTO listDTO = null;
        List<RatBkrationDis> disList = ratBkrationDisDao.getRatBkDisByRatCode(ratCode, corpId, null);
        if (null != disList && !disList.isEmpty()) {
            listDTO = new RatBkrationDisListDTO();
            RatBkrationDis dis = disList.get(0);
            BeanUtils.copyProperties(dis, listDTO);
            // 设置分配明细,如果是总额共享，则所有共享单位的明细都需要修改额度
            List<RatBkrationDisDtlDTO> dtlDTOs = new ArrayList<RatBkrationDisDtlDTO>();
            if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(dis.getDistributeWay())) {
                dtlDTOs = disDetailService.getAllShareCorpRationDisDtl(ratCode);
            } else {
                List<String> ratCodes = new ArrayList<String>();
                ratCodes.add(dis.getId());
                dtlDTOs = disDetailService.getBkrationDisDtlByRatCodes(ratCodes);
            }
            listDTO.setRationDtls(dtlDTOs);
        }
        return listDTO;
    }

    /**
     * 功能介绍: 查询已审批通过的授信分配信息。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationCpAddListDTO queryPassRationDisData(String ratCode, String corpId, String addCode) {
        List<RatBkrationCpAddListDTO> listDTOs = ratBkrationDisDao.queryPassRationDisData(ratCode, corpId);
        if (!CollectionUtils.isEmpty(listDTOs)) {
            RatBkrationCpAddListDTO listDTO = listDTOs.get(0);// 单位分配的授信只有一笔
            // 取分配明细数据
            List<String> disCodes = Lists.newArrayList();
            //分配编号
            disCodes.add(listDTO.getDisCode());
            //授信分配明细信息
            List<RatBkrationCpAddDtlDTO> dtlDTOs = getCprationDisDtlByDisCodes(disCodes);

            Map<String, RatBkRationAddDtlVm> dtlMap = Maps.newHashMap();
            if (!StringUtils.isEmpty(addCode)) {
                dtlMap = addService.getRationAddDtlByAddCode(addCode);
            }
            List<RatBkrationCpAddDtlDTO> singDtls = new ArrayList<RatBkrationCpAddDtlDTO>();
            List<RatBkrationCpAddDtlDTO> compDtls = new ArrayList<RatBkrationCpAddDtlDTO>();
            BigDecimal allAddMoney = BigDecimal.ZERO; // 可追加额度
            BigDecimal allAddOpenMoney = BigDecimal.ZERO; // 可追加敞口额度
            BigDecimal moneySum = BigDecimal.ZERO; // 已追加额度
            BigDecimal openMoneySum = BigDecimal.ZERO; // 已追加敞口额度
            // 计算分配额度，调剂额度，被调剂额度
            RatBkrationCpAddDtlDTO compSignDTO = null;
            for (RatBkrationCpAddDtlDTO dtlDTO : dtlDTOs) {
                String itemCode = dtlDTO.getItemCode();
                RatBkrationMoneyBean ratBean = getRationItemDisMoney(listDTO.getDisCode(), ratCode, dtlDTO.getItemFlag(), itemCode);
                dtlDTO.setMoney(ratBean.getDisMoney());// 已分配授信额度
                dtlDTO.setOpenMoney(ratBean.getDisOpenMoney());// 已分配授信敞口额度
                dtlDTO.setAllDisMoney(ratBean.getLeftMoney());// 可追减额度即剩余额度
                dtlDTO.setAllDisOpenMoney(ratBean.getLeftOpenMoney());// 可追减敞口额度
                dtlDTO.setAllAddMoney(ratBean.getUnUseMoney());// 授信未使用额度
                dtlDTO.setAllAddOpenMoney(ratBean.getUnUseOpenMoney());// 未使用敞口额度
                dtlDTO.setAllDisMaxMoney(ratBean.getLeftMaxMoney());// 剩余最高额度

                allAddMoney = allAddMoney.add(dtlDTO.getAllAddMoney());// 可追加额度合计
                allAddOpenMoney = allAddOpenMoney.add(dtlDTO.getAllAddOpenMoney());// 可追加敞口额度合计
                if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                    compDtls.add(dtlDTO);
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {// 综合授信使用标识
                        compSignDTO = dtlDTO;
                        listDTO.setCompItemIndex(dtlDTO.getItemOrder());
                    }
                } else {
                    singDtls.add(dtlDTO);
                }
                // 设置在途追加授信额度
                if (!StringUtils.isEmpty(addCode)) {
                    setCprationAddMoney(dtlDTO, dtlMap);
                }
                moneySum = moneySum.add(dtlDTO.getAddMoney());// 已 追加合计
                openMoneySum = openMoneySum.add(dtlDTO.getAddOpenMoney());// 已 追加敞口合计
            }
            // TODO综合品种的可追加额度是一样的
            for (RatBkrationCpAddDtlDTO dtlDTO : compDtls) {
                dtlDTO.setAllAddMoney(compSignDTO.getAllAddMoney());
                dtlDTO.setAllAddOpenMoney(compSignDTO.getAllAddOpenMoney());
            }
            listDTO.setSingRationDtls(singDtls);// 单品种列表
            listDTO.setCompRationDtls(compDtls);// 综合品种列表
            listDTO.setSingItemSize(singDtls.size());
            listDTO.setCompItemSize(compDtls.size());
            listDTO.setAllAddMoney(allAddMoney);// 可追加额度合计
            listDTO.setAllAddOpenMoney(allAddOpenMoney);// 可追加敞口额度合计
            listDTO.setMoneysum(moneySum);// 已追加额度合计
            listDTO.setOpenMoneysum(openMoneySum);// 已追加敞口额度合计
            return listDTO;
        }
        return null;
    }

    /**
     * 功能介绍 : 设置集团追加编辑追加的额度。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void setCprationAddMoney(RatBkrationCpAddDtlDTO dtlDTO, Map<String, RatBkRationAddDtlVm> dtlMap) {
        RatBkRationAddDtlVm tempDTO = dtlMap.get(dtlDTO.getItemCode());
        if (null != tempDTO) {
            dtlDTO.setAddMoney(tempDTO.getMoney());// 已追加金额
            dtlDTO.setAddOpenMoney(tempDTO.getOpenMoney());// 已追加敞口额度
            dtlDTO.setAddMaxMoney(tempDTO.getMaxMoney());// 已追加最高金额
        } else {
            dtlDTO.setAddMoney(BigDecimal.ZERO);// 已追加金额
            dtlDTO.setAddOpenMoney(BigDecimal.ZERO);// 已追加敞口额度
            dtlDTO.setAddMaxMoney(BigDecimal.ZERO);// 已追加最高金额
        }
    }

    /**
     * 功能介绍 : 获取金额共享或自用模式下计算未分配额度品种明细(剩余额度)。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RationDisBean> getOwnDisShareMoney(String ratCode, String disId, String addId) {
        // 从银行授信表中获得所有可分配的额度（银行授信额度+追加）
        List<RationDisBean> beanA = ratDetailDao.getOwnRationDisMoney(ratCode);
        // 从银行授信分配表中获得所有已分配的额度（已审批通过的分配数）
        List<RationDisBean> beanF = disDetailDao.getRationDisMaxMoney(ratCode, null);
        // 编辑时的金额
        List<RationDisBean> beanB = disDetailDao.getEditDisUnCheckMoney(ratCode, null);
        // 集团追加的临时额度
        List<RationDisBean> beanC = addDetailDao.getEditAddUnCheckMoney(ratCode, addId);
        // 从银行授信分配表中获得未审批通过的分配额度
        List<RationDisBean> beanD = disDetailDao.getDisUnCheckMoney(ratCode);
        // 从银行授信追加表中获得未审批通过的追加额度
        List<RationDisBean> beanE = addDetailDao.getAddUnCheckMoney(ratCode);
        return addBeans(
                addBeans(addBeans(addBeans(addBeans(beanA, beanF, "sub"), beanB, "add"), beanC, "add"), beanD, "sub"),
                beanE, "sub");
    }

    /**
     * 功能介绍 : 根据授信分配编号获取单位已录入的授信明细集合。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getCprationDisDtlByDisCodes(List<String> disCodes) {
        return disDetailDao.getCprationDisDtlByDisCodes(disCodes);
    }

    /**
     * 功能介绍 : 判断授信所有的分配方式。<br>
     * <b>日期：</b>2017年8月14日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public String queryRationDisWay(String ratCode) {
        RatBkrationPageVO rationVO = bkrationService.getRatBkrationInfo(ratCode);
        String corpId = rationVO.getCorpId();
        List<RatBkrationDis> disList = ratBkrationDisDao.getRatBkDisByRatCode(ratCode);
        /*
         * 根据已分配明细的分配方式获取不同的调剂单位 分配方式分为：自用，金额共享，总额共享，金额共享+自用，金额共享+总额共享
         */
        Integer disWay1 = Integer.valueOf(0);// 自用
        Integer disWay2 = Integer.valueOf(0);// 总额共享-成员单位
        Integer disWay20 = Integer.valueOf(0);// 总额共享-中心本身
        Integer disWay3 = Integer.valueOf(0);// 金额共享
        String disCode = "";
        for (RatBkrationDis bkrationDis : disList) {
            if (RationConstant.DIS_TYPE_OWN.equals(bkrationDis.getDistributeWay())) {// 自用
                disWay1++;
            } else if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(bkrationDis.getDistributeWay())) {// 总额共享
                if (!CommonConstant.STATUE_CHECK.equals(bkrationDis.getStatus())) {
                    disCode += bkrationDis.getDisCode();// 未审批通过
                }
                if (corpId.equals(bkrationDis.getCorpId())) {// 分配给自身
                    disWay20++;// 总额共享-中心本身
                } else {
                    disWay2++;
                }
            } else if (RationConstant.DIS_TYPE_MONEY_SHARE.equals(bkrationDis.getDistributeWay())) {// 金额共享
                disWay3++;
            }
        }
        // 总额共享模式下是否有未审批通过的分配授信
        if (!StringUtils.isEmpty(disCode)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_DIS_NOT_CHECK,
                    disCode.substring(0, disCode.length() - 1));
        }
        String flag = RationConstant.RAT_DISWAY_1;
        if (disWay1 > 0 && disWay2 == 0 && disWay20 == 0 && disWay3 == 0) {// 自用
            flag = RationConstant.RAT_DISWAY_1;
        } else if (disWay1 == 0 && disWay2 > 0 && disWay20 == 0 && disWay3 == 0) {// 成员单位的总额共享
            flag = RationConstant.RAT_DISWAY_2;
        } else if (disWay1 == 0 && disWay2 == 0 && disWay20 == 0 && disWay3 > 0) {// 金额共享
            flag = RationConstant.RAT_DISWAY_3;
        } else if (disWay1 == 0 && disWay20 > 0 && disWay3 == 0) {// 总额共享-中心本身（授信主体分配了总额共享，以授信主体为调剂对象）
            flag = RationConstant.RAT_DISWAY_4;
        } else if (disWay2 == 0 && disWay20 == 0 && disWay1 > 0 && disWay3 > 0) {// 金额共享+自用
            flag = RationConstant.RAT_DISWAY_5;
        } else if (disWay1 == 0 && disWay3 > 0) {// 金额共享+总额共享
            if (disWay20 > 0) {// 授信主体分配了总额共享，以授信主体为调剂对象
                flag = RationConstant.RAT_DISWAY_6;
            } else if (disWay2 > 0) {// 成员单位为调剂对象
                flag = RationConstant.RAT_DISWAY_7;
            }
        }
        return flag;
    }

    /**
     * 功能介绍 : 获取授信分配单位的授信品种。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getItemCustomDTOs(String ratCode, String corpId, String itemFlag) {
        List<RatItemDisVO> disVOs = ratBkrationDisDao.getItemCustomDTOs(ratCode, corpId, itemFlag);
        if (!EmptyUtils.isEmptyList(disVOs)) {
            for (RatItemDisVO disVO : disVOs) {
                RatBkrationMoneyBean ratBean = getRationItemDisMoney(disVO.getDisCode(), ratCode, itemFlag,
                        disVO.getItemCode());
                disVO.setMaxMoney(ratBean.getLeftMaxMoney());// 剩余最高额度
                if (disVO.getCreditType().equals("1")) {
                    disVO.setCreditTypeName("循环授信");
                } else {
                    disVO.setCreditTypeName("不循环授信");
                }
            }
        }
        return disVOs;
    }

    /**
     * 功能介绍 : 获取授信分配单位的授信品种类型。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getCorpItemFlags(String ratCode, String corpId) {
        return ratBkrationDisDao.getCorpItemFlags(ratCode, corpId);
    }

    /**
     * 功能介绍 : 获取授信品种已分配额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationCpAddDtlDTO getRationDisItemMoney(String ratCode, String corpId, String itemFlag,
                                                        String itemCode) {
        RatBkrationCpAddDtlDTO dtlDTO = new RatBkrationCpAddDtlDTO();
        List<RatBkrationCpAddDtlDTO> dtos = disDetailService.getRationDisDtlByItemCode(ratCode, corpId, itemFlag,
                itemCode);
        if (null != dtos && !dtos.isEmpty()) {
            dtlDTO = dtos.get(0);
            // RatBkrationMoneyBean ratBean = getBankRationItemMoney(ratCode, corpId, itemFlag, itemCode);
            RatBkrationMoneyBean ratBean = getRationItemDisMoney(dtlDTO.getDisCode(), ratCode, itemFlag, itemCode);
            dtlDTO.setMoney(ratBean.getLeftMoney());
            dtlDTO.setOpenMoney(ratBean.getLeftOpenMoney());
            dtlDTO.setMaxMoney(ratBean.getLeftMaxMoney());
            // 可调剂的金额为初次分配金额+被调剂过来的金额+调剂出去的金额-正式占用-冻结金额
            /*
             * dtlDTO.setMoney(dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
             * .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
             * .subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
             * .subtract(dtlDTO.getVirtualUration()));
             * dtlDTO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney())
             * .subtract(dtlDTO.getSuperReliefOpenMoney()).subtract(dtlDTO.getFreezeOpenMoney())
             * .subtract(dtlDTO.getSuperProcessOpenMoney()).subtract(dtlDTO.getUrationOpenMoney())
             * .subtract(dtlDTO.getVirtualOpenUration()));
             */
        }
        return dtlDTO;
    }

    /**
     * 功能介绍 : 根据系统授信品种获取授信分配的授信品种。<br>
     * <b>日期：</b>2017年8月22日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatItemDisVO> getRationDisItemCode(String disCode, String sysItemCode) {
        List<RatItemDisVO> disList = new ArrayList<RatItemDisVO>();
        // FIXME 获取授信品种及额度信息
        List<RatItemDisVO> itemDisVOs = disDetailDao.getItemCustomDTOBySysItem(disCode, sysItemCode);
        if (null == itemDisVOs || itemDisVOs.isEmpty()) {
            return disList;
        }
        Map<String, RatBkrationCpAddDtlDTO> dtlMap = new HashMap<String, RatBkrationCpAddDtlDTO>();
        String itemFlag = "";
        String itemCode = "";
        for (RatItemDisVO disVO : itemDisVOs) {
            itemFlag = disVO.getItemFlag();
            itemCode = disVO.getItemCode();// 授信品种
            /*
             * if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) { disList.add(disVO); } else { //获取综合授信品种所有的品种 disList
             * = disDetailDao.getRationDisCompItemCustom(disCode, itemFlag); }
             */
        }
        // FIXME 修改为只显示业务品种对应的授信品种
        for (RatItemDisVO disVO : itemDisVOs) {
            disList.add(disVO);
        }
        if (!EmptyUtils.isEmptyList(disList)) {
            List<RatBkrationCpAddDtlDTO> dtos = disDetailDao.getRationDisDtlByDisDtlId(disCode, itemFlag, itemCode);
            if (!EmptyUtils.isEmptyList(dtos)) {
                dtlMap = Maps.uniqueIndex(dtos, new Function<RatBkrationCpAddDtlDTO, String>() {
                    @Override
                    public String apply(RatBkrationCpAddDtlDTO input) {
                        return input.getItemCode();
                    }
                });
            }
            RatBkrationDis ratDis = get(disCode, true);
            for (RatItemDisVO ratItemDisVO : disList) {
                RatBkrationCpAddDtlDTO dtlDTO = dtlMap.get(ratItemDisVO.getItemCode());
                if (null != dtlDTO) {
                    /*
                     * RatBkrationMoneyBean ratBean = getBankRationItemMoney(ratDis.getRatCode(), ratDis.getCorpId(),
                     * ratItemDisVO.getItemFlag(), ratItemDisVO.getItemCode());
                     */
                    // 获取分配的剩余额度
                    RatBkrationMoneyBean ratBean = getRationItemDisMoney(disCode, ratDis.getRatCode(),
                            ratItemDisVO.getItemFlag(), ratItemDisVO.getItemCode());
                    ratItemDisVO.setMoney(ratBean.getLeftMoney());
                    ratItemDisVO.setOpenMoney(ratBean.getLeftOpenMoney());
                    ratItemDisVO.setMaxMoney(ratBean.getLeftMaxMoney());// 剩余最高额度
                    /*
                     * ratItemDisVO.setMoney(
                     * dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney())
                     * .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getSuperProcessMoney())
                     * .subtract(dtlDTO.getReliefAndUrationMoney()).subtract(dtlDTO.getVirtualUration()));
                     * ratItemDisVO.setOpenMoney(dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney())
                     * .subtract(dtlDTO.getSuperReliefOpenMoney()).subtract(dtlDTO.getFreezeOpenMoney())
                     * .subtract(dtlDTO.getSuperProcessOpenMoney()).subtract(dtlDTO.getUrationOpenMoney())
                     * .subtract(dtlDTO.getVirtualOpenUration())); ratItemDisVO.setMaxMoney(dtlDTO.getMaxMoney());
                     */
                }
            }
        }
        return disList;
    }

    /**
     * 功能介绍 : 自主授信自动分配。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationDis processOwnModeDis(RatBkRationVm bkrationDTO, String userId) {
        // 存在审批通过再次分配数据，先删除之前的分配
        ratBkrationDisDao.deleteBkrationDisByRatCode(bkrationDTO.getId());
        RatBkrationDis dis = new RatBkrationDis();
        BeanUtils.copyProperties(bkrationDTO, dis, "id");
        dis.setRatCode(bkrationDTO.getId());
        dis.setDisCode(
                bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_DISCODE, RationConstant.RAT_BKRATION_DIS_CODE));// 授信分配编号
        dis.setIspassed(RationConstant.RAT_COMMON_Y);// 通过
        // 添加授信使用单位的名称和编号
        CorpDTO corpDTO = corpService.get(bkrationDTO.getCorpId());
        dis.setRatCorpId(bkrationDTO.getCorpId());
        dis.setCorpName(corpDTO.getName());
        dis.setCorpCode(corpDTO.getCode());
        dis.setDistributeWay(RationConstant.DIS_TYPE_AUTO);// 自动分配
        List<RatCreditTypeVO> creditTypes = creditTypeService.getAllCreditType();
        if (!EmptyUtils.isEmptyList(creditTypes)) {
            dis.setType(creditTypes.get(0).getId());// 额度类型-信用
        }
        dis.setCreateBy(userId);
        dis.setCreateDate(DateUtils.getDate());
        saveEntity(dis);
        List<RatBkrationDisDetail> disDetails = new ArrayList<RatBkrationDisDetail>();
        disDetails = BeanUtils.copyListProperties(bkrationDTO.getRationDtls(), RatBkrationDisDetail.class);
        for (RatBkrationDisDetail disDetail : disDetails) {
            disDetail.setDisCode(dis.getId());
            disDetail.setId(null);
            disDetailService.saveRatBkrationDisDetail(disDetail);
        }
        return dis;
        // 记录授信分配日志
    }

    /**
     * 功能介绍 : 查询银单位已分配的授信分配信息。<br>
     * <b>日期：</b>2017年9月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationDisListDTO getCorpBkrationDisListDTO(String ratCode, String corpId) {
        RatBkrationDisListDTO listDTO = null;
        List<RatBkrationDis> disList = ratBkrationDisDao.getRatBkDisByRatCode(ratCode, corpId, null);
        if (null != disList && !disList.isEmpty()) {
            listDTO = new RatBkrationDisListDTO();
            RatBkrationDis dis = disList.get(0);
            BeanUtils.copyProperties(dis, listDTO);
            // 设置分配明细
            List<String> ratCodes = new ArrayList<String>();
            ratCodes.add(dis.getId());
            List<RatBkrationDisDtlDTO> dtlDTOs = disDetailService.getBkrationDisDtlByRatCodes(ratCodes);
            listDTO.setRationDtls(dtlDTOs);
        }
        return listDTO;
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
        getEntityDao().flush();
        if (result) {
            RatBkrationDis dis = get(businessKey, true);
            dis.setStatus(RationConstant.STATUS_CHECK);// 审批通过
            dis.setIspassed(RationConstant.RAT_COMMON_Y);// 通过
            dis.setUpdateBy(userId);
            dis.setUpdateDate(DateUtils.getDate());
            update(dis);
            RatBkrationDisListDTO disDTO = new RatBkrationDisListDTO();
            BeanUtils.copyProperties(dis, disDTO);
            // 设置分配明细
            List<String> ratCodes = new ArrayList<String>();
            ratCodes.add(dis.getId());
            List<RatBkrationDisDtlDTO> dtlDTOs = disDetailService.getBkrationDisDtlByRatCodes(ratCodes);
            disDTO.setRationDtls(dtlDTOs);
            // 更新授信录入中已分配额度
            if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(disDTO.getDistributeWay())) {
                updateBkRationDisMoney(disDTO);
            } else {
                bkrationService.updateRationDisMoney(disDTO);
            }
        } else {
            RatBkrationDis dis = get(businessKey, true);
            dis.setStatus(RationConstant.STATUS_BACK);// 审批打回
            update(dis);
        }
    }

    /**
     * 功能介绍 : 查询授信分配剩余额度。<br>
     * <b>日期：</b>2017年9月20日<br>
     *
     * @version 1.0
     * @author liyh
     */
    // TODO 修改为根据分配ID查询授信品种的可用额度
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByItemCode(String disId, String itemFlag, String itemCode) {
        return disDetailDao.getRationDisDtlByDisDtlId(disId, itemFlag, itemCode);
    }

    /**
     * 功能介绍 : 根据授信编号获取单位已分配的授信品种额度。<br>
     * <b>日期：</b>2017年8月15日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<RatBkrationCpAddDtlDTO> getRationDisDtlByRatCode(String ratCode, String itemFlag, String itemCode) {
        return disDetailDao.getRationDisDtlByRatCode(ratCode, itemFlag, itemCode);
    }

    /**
     * 功能介绍 : 获取集团授信的分配及剩余额度。<br>
     * <b>日期：</b>2017年9月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationMoneyBean getRationItemDisMoney(String disId, String ratCode, String itemFlag, String itemCode) {
        RatBkrationMoneyBean addDtlDTO = new RatBkrationMoneyBean();
        addDtlDTO.setRatCode(ratCode);
        addDtlDTO.setItemCode(itemCode);
        addDtlDTO.setItemFlag(itemFlag);
        List<RatBkrationCpAddDtlDTO> disList = getRationDisDtlByItemCode(disId, itemFlag, itemCode);
        BigDecimal disMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal disOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal leftOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMaxMoney = BigDecimal.ZERO;// 分配剩余最高额度
        // 初始化Zero
        if (!EmptyUtils.isEmptyList(disList)) {// 没有总额共享的
            // 循环计算已分配额度
            for (RatBkrationCpAddDtlDTO dtlDTO : disList) {
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    // 已分配额度-已占用-冻结-虚拟占用额度-虚拟占用
                    disMoney = disMoney.add(getDisMoney(dtlDTO));// 分配额度
                    disOpenMoney = disOpenMoney.add(getDisOpenMoney(dtlDTO));// 分配敞口额度
                    leftMoney = leftMoney.add(getLeftMoney(dtlDTO));// 剩余额度
                    leftOpenMoney = leftOpenMoney.add(getLeftOpenMoney(dtlDTO));// 剩余额度
                    leftMaxMoney = leftMaxMoney.add(getDisMaxMoney(dtlDTO));// 剩余额度
                }
            }
        }
        addDtlDTO.setDisMoney(disMoney);
        addDtlDTO.setDisOpenMoney(disOpenMoney);
        addDtlDTO.setLeftMoney(leftMoney);
        addDtlDTO.setLeftOpenMoney(leftOpenMoney);
        addDtlDTO.setLeftMaxMoney(leftMaxMoney);
        getRationItemLeftMoney(addDtlDTO);
        RatBkrationDis dis = get(disId, true);
        addDtlDTO.setCorpId(dis.getCorpId());
        getRationItemAddMoney(addDtlDTO, RationConstant.RAT_ADD_FLAG_GROUP);// 集团追加
        // TODO 查看该品种的剩余额度
        getBankRationUnseMoney(addDtlDTO);
        return addDtlDTO;
    }

    /**
     * 功能介绍 : 获取银行授信品种未分配的剩余额度。<br>
     * <b>日期：</b>2017年10月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void getBankRationUnseMoney(RatBkrationMoneyBean addDtlDTO) {
        List<RatBkRationAddDtlVm> list = addDetailDao.getBkrationAddDtlByRatCode(addDtlDTO.getRatCode(), null,
                addDtlDTO.getItemFlag(), addDtlDTO.getItemCode(), RationConstant.RAT_ADD_FLAG_BANK);
        if (!EmptyUtils.isEmptyList(list)) {
            for (RatBkRationAddDtlVm dtlDTO : list) {
                if (addDtlDTO.getItemCode().equals(dtlDTO.getItemCode())) {
                    // TODO 银行追减和集团追加在途的额度= 银行授信的剩余额度
                    if (RationConstant.RAT_ADD_FLAG_BANK.equals(dtlDTO.getFlag())) {
                        if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) < 0) {
                            addDtlDTO.setUnUseMoney(addDtlDTO.getUnUseMoney().add(dtlDTO.getMoney()));// 网点剩余的额度
                            addDtlDTO.setUnUseOpenMoney(addDtlDTO.getUnUseOpenMoney().add(dtlDTO.getOpenMoney()));
                            addDtlDTO.setUnUseMaxMoney(addDtlDTO.getUnUseMaxMoney().add(dtlDTO.getMaxMoney()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 功能介绍 : 查询授信追加或追减剩余的额度。<br>
     * <b>日期：</b>2017年10月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public BigDecimal getRationItemAddMoney(RatBkrationMoneyBean addDtlDTO, String flag) {
        String corpId = addDtlDTO.getCorpId();
        String addFlag = flag;
        if (RationConstant.RAT_ADD_FLAG_BANK.equals(flag)) {
            corpId = "";// 银行授信追加，只查询追加在途的额度
            addFlag = "";
        }
        List<RatBkRationAddDtlVm> list = addDetailDao.getBkrationAddDtlByRatCode(addDtlDTO.getRatCode(), corpId,
                addDtlDTO.getItemFlag(), addDtlDTO.getItemCode(), addFlag);
        if (!EmptyUtils.isEmptyList(list)) {
            for (RatBkRationAddDtlVm dtlDTO : list) {
                if (addDtlDTO.getItemCode().equals(dtlDTO.getItemCode())) {
                    // TODO 银行追减和集团追加在途的额度= 银行授信的剩余额度
                    if (RationConstant.RAT_ADD_FLAG_BANK.equals(dtlDTO.getFlag())) {
                        if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) < 0) {
                            addDtlDTO.setUnUseMoney(addDtlDTO.getUnUseMoney().add(dtlDTO.getMoney()));// 网点剩余的额度
                            addDtlDTO.setUnUseOpenMoney(addDtlDTO.getUnUseOpenMoney().add(dtlDTO.getOpenMoney()));
                            addDtlDTO.setUnUseMaxMoney(addDtlDTO.getUnUseMaxMoney().add(dtlDTO.getMaxMoney()));
                        }
                    } else {
                        if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                            addDtlDTO.setUnUseMoney(addDtlDTO.getUnUseMoney().subtract(dtlDTO.getMoney()));// 网点剩余的额度
                            addDtlDTO.setUnUseOpenMoney(addDtlDTO.getUnUseOpenMoney().subtract(dtlDTO.getOpenMoney()));
                            addDtlDTO.setUnUseMaxMoney(addDtlDTO.getUnUseMaxMoney().subtract(dtlDTO.getMaxMoney()));
                        } else {
                            // addDtlDTO.setLeftMoney(addDtlDTO.getLeftMoney().add(dtlDTO.getMoney()));// TODO// 分配的额度
                            // addDtlDTO.setLeftOpenMoney(addDtlDTO.getLeftOpenMoney().add(dtlDTO.getOpenMoney()));//
                            // TODO// 分配的敞口额度
                            // addDtlDTO.setLeftMaxMoney(addDtlDTO.getLeftMaxMoney().add(dtlDTO.getMaxMoney()));
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 功能介绍 : 银行授信品种分配授信额度。<br>
     * <b>日期：</b>2017年9月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationMoneyBean getBankRationItemMoney(String ratCode, String corpId, String itemFlag, String itemCode) {
        RatBkrationMoneyBean addDtlDTO = new RatBkrationMoneyBean();
        addDtlDTO.setRatCode(ratCode);
        addDtlDTO.setCorpId(corpId);
        addDtlDTO.setItemCode(itemCode);
        addDtlDTO.setItemFlag(itemFlag);
        getRationItemLeftMoney(addDtlDTO);
        List<RatBkrationCpAddDtlDTO> disList = new ArrayList<RatBkrationCpAddDtlDTO>();
        // 查询是否有总额共享的分配数据
        List<RatBkrationDis> hisDisList = getRatBkDisWayByRatCode(ratCode, null, RationConstant.DIS_TYPE_AMOUNT_SHARE);
        if (!EmptyUtils.isEmptyList(hisDisList)) {// 没有总额共享的
            // 任意取一笔
            RatBkrationDis dis = hisDisList.get(0);
            // 获取总额分配的数据
            List<RatBkrationCpAddDtlDTO> disDtls = getRationDisDtlByItemCode(dis.getId(), itemFlag, itemCode);
            disList.addAll(disDtls);
        }
        // 获取非总额共享分配方式的已分配明细
        List<RatBkrationCpAddDtlDTO> otherDisList = getRationDisDtlByRatCode(ratCode, itemFlag, itemCode);
        if (!EmptyUtils.isEmptyList(otherDisList)) {// 没有总额共享的
            disList.addAll(otherDisList);
        }
        BigDecimal disMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal disOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal leftOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMaxMoney = BigDecimal.ZERO;// 分配剩余最高额度
        if (!EmptyUtils.isEmptyList(disList)) {// 未分配过
            if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
                // 循环计算已分配额度
                for (RatBkrationCpAddDtlDTO dtlDTO : disList) {
                    if (itemCode.equals(dtlDTO.getItemCode())) {
                        // 已分配额度-已占用-冻结-虚拟占用额度-虚拟占用
                        disMoney = disMoney.add(getDisMoney(dtlDTO));// 分配额度
                        disOpenMoney = disOpenMoney.add(getDisOpenMoney(dtlDTO));// 分配敞口额度
                        leftMoney = leftMoney.add(getLeftMoney(dtlDTO));// 剩余额度
                        leftOpenMoney = leftOpenMoney.add(getLeftOpenMoney(dtlDTO));// 剩余额度
                        leftMaxMoney = leftMaxMoney.add(getDisMaxMoney(dtlDTO));// 剩余额度
                    }
                }
                addDtlDTO.setDisMoney(disMoney);
                addDtlDTO.setDisOpenMoney(disOpenMoney);
                addDtlDTO.setLeftMoney(leftMoney);
                addDtlDTO.setLeftOpenMoney(leftOpenMoney);
                addDtlDTO.setLeftMaxMoney(leftMaxMoney);
            } else {
                for (RatBkrationCpAddDtlDTO dtlDTO : disList) {
                    if (itemCode.equals(dtlDTO.getItemCode())) {
                        // 已分配额度-已占用-冻结-虚拟占用额度-虚拟占用
                        disMoney = disMoney.add(getDisMoney(dtlDTO));// 分配额度
                        disOpenMoney = disOpenMoney.add(getDisOpenMoney(dtlDTO));// 分配敞口额度
                        leftMoney = leftMoney.add(getLeftMoney(dtlDTO));// 剩余额度
                        leftOpenMoney = leftOpenMoney.add(getLeftOpenMoney(dtlDTO));// 剩余额度
                        leftMaxMoney = leftMaxMoney.add(getDisMaxMoney(dtlDTO));// 剩余额度
                    }
                }
                addDtlDTO.setDisMoney(disMoney);
                addDtlDTO.setDisOpenMoney(disOpenMoney);
                addDtlDTO.setLeftMoney(leftMoney);
                addDtlDTO.setLeftOpenMoney(leftOpenMoney);
                addDtlDTO.setLeftMaxMoney(leftMaxMoney);
            }
        } else {
            addDtlDTO.setDisMoney(BigDecimal.ZERO);
            addDtlDTO.setDisOpenMoney(BigDecimal.ZERO);
            addDtlDTO.setLeftMoney(BigDecimal.ZERO);
            addDtlDTO.setLeftOpenMoney(BigDecimal.ZERO);
            addDtlDTO.setLeftMaxMoney(BigDecimal.ZERO);
        }
        addDtlDTO.setUnUseMoney(addDtlDTO.getMoney().subtract(addDtlDTO.getDisMoney()));
        addDtlDTO.setUnUseOpenMoney(addDtlDTO.getOpenMoney().subtract(addDtlDTO.getDisOpenMoney()));
        addDtlDTO.setUnUseMaxMoney(addDtlDTO.getMaxMoney().subtract(addDtlDTO.getLeftMaxMoney()));
        getRationItemAddMoney(addDtlDTO, RationConstant.RAT_ADD_FLAG_BANK);// 整个授信追减或追减的额度
        return addDtlDTO;
    }

    public void getRationItemLeftMoney(RatBkrationMoneyBean addDtlDTO) {
        // 计算某个品种的已分配额度和剩余额度
        List<RatBkrationDetail> ratList = ratDetailDao.getBkrationDtlByRatCode(addDtlDTO.getRatCode(),
                addDtlDTO.getItemFlag(), addDtlDTO.getItemCode());
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        BigDecimal unUseMoney = BigDecimal.ZERO;
        BigDecimal unUseOpenMoney = BigDecimal.ZERO;
        BigDecimal maxMoney = BigDecimal.ZERO;
        for (RatBkrationDetail detail : ratList) {
            if (addDtlDTO.getItemCode().equals(detail.getItemCode())) {
                money = money.add(detail.getMoney().add(detail.getSuperAddMoney()));
                openMoney = openMoney.add(detail.getOpenMoney().add(detail.getSuperAddOpenMoney()));
                unUseMoney = unUseMoney.add(detail.getMoney().add(detail.getSuperAddMoney()))
                        .subtract(detail.getSuperDistributeMoney());
                unUseOpenMoney = unUseOpenMoney.add(detail.getOpenMoney().add(detail.getSuperAddOpenMoney()))
                        .subtract(detail.getSuperDistributeOpenMoney());
                maxMoney = maxMoney.add(detail.getMaxMoney().add(detail.getSuperAddMaxMoney()));// 分配的最高额度
            }
        }
        addDtlDTO.setMoney(money);
        addDtlDTO.setOpenMoney(openMoney);
        addDtlDTO.setUnUseMoney(unUseMoney);
        addDtlDTO.setUnUseOpenMoney(unUseOpenMoney);
        addDtlDTO.setUnUseMaxMoney(maxMoney);
        addDtlDTO.setMaxMoney(maxMoney);
        // getRationItemAddMoney(addDtlDTO);
    }

    public BigDecimal getDisMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 分配额度=分配+被调剂-调剂
        return dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney());
    }

    public BigDecimal getDisOpenMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 分配额度=分配+被调剂-调剂
        return dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney()).subtract(dtlDTO.getSuperReliefOpenMoney());
    }

    public BigDecimal getDisMaxMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 分配额度=分配+被调剂-调剂-冻结-虚拟在途
        return dtlDTO.getMaxMoney();
    }

    public BigDecimal getLeftMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        // 已分配额度-已占用-冻结-虚拟占用额度-虚拟调剂
        return dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney()).subtract(dtlDTO.getSuperReliefMoney())
                .subtract(dtlDTO.getFreezeMoney()).subtract(dtlDTO.getSuperProcessMoney())
                .subtract(dtlDTO.getReliefAndUrationMoney()).subtract(dtlDTO.getVirtualUration())
                .subtract(dtlDTO.getVirtualReliefMoney());// 可追减额度
    }

    public BigDecimal getLeftOpenMoney(RatBkrationCpAddDtlDTO dtlDTO) {
        return dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney()).subtract(dtlDTO.getSuperReliefOpenMoney())
                .subtract(dtlDTO.getFreezeOpenMoney()).subtract(dtlDTO.getSuperProcessOpenMoney())
                .subtract(dtlDTO.getUrationOpenMoney()).subtract(dtlDTO.getVirtualOpenUration())
                .subtract(dtlDTO.getVirtualReliefOpenMoney());// 可追减额度
    }

    /**
     * 获取授信分配总额
     *
     * @param ratCode 授信录入id
     * @param disWay  分配方式
     * @return 授信分配集合
     */
    public List<RationDisBean> getDisDtl(String ratCode, String disWay) {
        return disDetailDao.getRationDisMoneySum(ratCode, disWay);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpParentChang(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            List<RatBkrationDis> disList1 = ratBkrationDisDao.queryDisDataByCorpId(corpId, RationConstant.RAT_DISWAY_2);// 查询分配方式为总额共享的数据
            if (null != disList1 && disList1.size() > 0) {
                for (RatBkrationDis bkrationDis : disList1) {
                    bkrationDis.setStatus(RationConstant.STATUS_INVALID);// 将分配数据状态设置为已作废
                    // bkrationDis.setUpdateBy(user.getId());
                    bkrationDis.setUpdateDate(DateUtils.getDate());
                    this.saveOrUpdate(bkrationDis);
                }
            }
            List<RatBkrationDis> disList2 = ratBkrationDisDao.queryDisDataByCorpId(corpId, RationConstant.RAT_DISWAY_3);// 查询分配方式为金额共享的数据
            if (null != disList2 && disList2.size() > 0) {
                for (RatBkrationDis bkrationDis : disList2) {
                    bkrationDis.setStatus(RationConstant.STATUS_INVALID);// 将分配数据状态设置为已作废
                    // bkrationDis.setUpdateBy(user.getId());
                    bkrationDis.setUpdateDate(DateUtils.getDate());
                    this.saveOrUpdate(bkrationDis);
                }
            }
        }
        return verifyErrorDTOS;
    }

    /**
     * 功能介绍 : 获取集团授信的分配及剩余额度
     *
     * @author luxy
     */
    public RatBkrationMoneyBean getRatItemDisMoney(String disId, String ratCode, String itemFlag, String itemCode) {
        RatBkrationMoneyBean addDtlDTO = new RatBkrationMoneyBean();
        addDtlDTO.setRatCode(ratCode);
        addDtlDTO.setItemCode(itemCode);
        addDtlDTO.setItemFlag(itemFlag);
        List<RatBkrationDisDtlDTO> disDtlDTOS = disDetailDao.getRationDisDtlByParams(disId, itemFlag, itemCode, null);
        BigDecimal disMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal disOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMoney = BigDecimal.ZERO;// 分配剩余额度
        BigDecimal leftOpenMoney = BigDecimal.ZERO;// 分配剩余敞口额度
        BigDecimal leftMaxMoney = BigDecimal.ZERO;// 分配剩余最高额度
        // 初始化Zero
        if (!EmptyUtils.isEmptyList(disDtlDTOS)) {
            // 循环计算已分配额度
            for (RatBkrationDisDtlDTO dtlDTO : disDtlDTOS) {
                if (itemCode.equals(dtlDTO.getItemCode())) {
                    if (dtlDTO.getItemFlag().equals(RationConstant.RAT_COMFLAG_1)) {// 综合品种具体分配明细取maxMoney字段
                        disMoney = dtlDTO.getMaxMoney().add(dtlDTO.getByReliefMaxMoney())
                                .subtract(dtlDTO.getReliefMaxMoney());// 授信额度+被调剂-调剂
                    } else {
                        disMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                                .subtract(dtlDTO.getSuperReliefMoney());
                    }
                    disOpenMoney = dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney())
                            .subtract(dtlDTO.getSuperReliefOpenMoney());// 分配敞口额度
                    leftMoney = dtlDTO.getMoney().add(dtlDTO.getSuperByReliefMoney())
                            .subtract(dtlDTO.getSuperReliefMoney()).subtract(dtlDTO.getFreezeMoney())
                            .subtract(dtlDTO.getSuperProcessMoney()).subtract(dtlDTO.getReliefAndUrationMoney())
                            .subtract(dtlDTO.getVirtualUration()).subtract(dtlDTO.getVirtualReliefMoney());// 剩余额度
                    leftOpenMoney = dtlDTO.getOpenMoney().add(dtlDTO.getSuperByReliefOpenMoney())
                            .subtract(dtlDTO.getSuperReliefOpenMoney()).subtract(dtlDTO.getFreezeOpenMoney())
                            .subtract(dtlDTO.getSuperProcessOpenMoney()).subtract(dtlDTO.getUrationOpenMoney())
                            .subtract(dtlDTO.getVirtualOpenUration()).subtract(dtlDTO.getVirtualReliefOpenMoney());/// 剩余额度
                    leftMaxMoney = dtlDTO.getMaxMoney().subtract(dtlDTO.getProcessMaxMoney())
                            .subtract(dtlDTO.getReliefMaxMoney()).subtract(dtlDTO.getFreezeMaxMoney())
                            .subtract(dtlDTO.getVirtualMaxMoney()).add(dtlDTO.getByReliefMaxMoney());// 剩余额度
                }
            }
        }
        addDtlDTO.setDisMoney(disMoney);
        addDtlDTO.setDisOpenMoney(disOpenMoney);
        addDtlDTO.setLeftMoney(leftMoney);
        addDtlDTO.setLeftOpenMoney(leftOpenMoney);
        addDtlDTO.setLeftMaxMoney(leftMaxMoney);
        getRationItemLeftMoney(addDtlDTO);
        RatBkrationDis dis = get(disId, true);
        addDtlDTO.setCorpId(dis.getCorpId());
        getRationItemAddMoney(addDtlDTO, RationConstant.RAT_ADD_FLAG_GROUP);// 集团追加
        // TODO 查看该品种的剩余额度
        getBankRationUnseMoney(addDtlDTO);
        return addDtlDTO;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize = ratBkrationDisDao.queryBusBillByCorpId(corpId, "-1,0,11");// 授信分配在途数据
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATIONDIS_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }

    public RatBkrationDisDtlDTO getCompRatDisInfo(String disCode) {
        List<RatBkrationDisDtlDTO> disDtlDTOS = disDetailDao.getRationDisDtlByParams(disCode,
                RationConstant.RAT_COMFLAG_1, null, RationConstant.RAT_COMP_SIGN_1);
        RatBkrationDisDtlDTO bkrationDisDtlDTO = null;
        if (null != disDtlDTOS && disDtlDTOS.size() > 0) {
            bkrationDisDtlDTO = disDtlDTOS.get(0);
        }
        return bkrationDisDtlDTO;
    }

    /**
     * 总额共享方式已分配的单位查看
     *
     * @param ratCode   授信主键id
     * @param ratCorpId 授信单位id
     */
    public List<CorpDTO> distributeWayView(String ratCode, String ratCorpId) {
        List<String> corpIdList = ratBkrationDisDao.distributeWayView(ratCode, ratCorpId);
        List<CorpDTO> corpList = corpService.listCorpDTOByCorpIds2(corpIdList);
        if (!CollectionUtils.isEmpty(corpList)) {
            return corpList;
        }
        return Lists.newArrayList();
    }
}
