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
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatBkrationAddDao;
import com.bytter.credit.domain.entity.*;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationAddPageVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.credit.dto.RatItemCustomDTO;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Service
 * @Description: 授信追加信息表
 * @author onlineGenerator
 * @date 2017-07-31 16:00:15
 * @version 1.0
 *
 */
@Service("ratBkrationAddService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_BKRATION_ADD, RatWorkFlowConstant.RAT_BKRATION_CPADD })
public class RatBkrationAddService extends BaseService<RatBkrationAdd, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatBkrationAddDao ratBkrationAddDao;
    @Autowired
    private RatBkrationService bkrationService;
    @Autowired
    private RatBkrationAddDetailService addDetailService;
    @Autowired
    private RatBkrationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @Autowired
    private RatBkrationDetailService rationDtlService;
    @Autowired
    private RatItemCustomService itemCustomService;
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
    private static int INDEX = 0;
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(RatBkrationAdd entity) {
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
    public void save(RatBkrationAdd entity) {
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(RatBkrationAdd entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(RatBkrationAdd entity) {
        super.saveOrUpdate(entity);
    }
    public BaseDao<RatBkrationAdd, String> getEntityDao() {
        return ratBkrationAddDao;
    }
    /**
     * 功能介绍 : 查询集团授信可追加数据(需追加过)。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     * @param resource
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryCpRationAddData(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rd.corp_Id", true);
        DataTablesOutput output = ratBkrationAddDao.queryCpRationAddData(input, corpAuthSql);
        disDtlService.setDisOutputPageData(output);
        return output;
    }
    /**
     * 功能介绍 : 查询授信可占用数据（已分配）。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     * @param resource
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryApplyRationAddData(DataTablesInput input, String corpId, String resource,
            String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rbd.corp_Id", true);
        DataTablesOutput output = ratBkrationAddDao.queryApplyRationAddData(input, corpId, corpAuthSql);
        disDtlService.setDisOutputPageData(output);// 修改为查询已分配追加后的授信额度
        return output;
    }
    /**
     * 功能介绍 : 查询授信追加审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION_ADD,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratBkrationAddDao.queryRationAddApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 查询集团授信追加审批数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listCpApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_BKRATION_CPADD,
                input);
        String businessSql = null;
        if(!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())){
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratBkrationAddDao.queryRationAddApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 根据授信单号查询所有授信追加数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryRationAddData(DataTablesInput input, String resource, String userId) {
        String corpAuthSql = SqlUtil.getInSql(corpService.getUserCorpIds(resource, userId), "rd.corp_Id", true);
        DataTablesOutput output = ratBkrationAddDao.queryRationAddData(input, corpAuthSql);
        List<RatBkrationAddListVO> data = output.getData();
        if (null == data || data.isEmpty()) {
            return output;
        }
        List<String> ratCodes = new ArrayList<String>();
        for (RatBkrationAddListVO bkrationVO : data) {
            ratCodes.add(bkrationVO.getId());
        }
        List<RatBkRationAddDtlVm> dtlDTOs = addDetailService.getBkrationDtlByRatCodes(ratCodes);
        Map<String, List<RatBkRationAddDtlVm>> ratMap = new HashMap<String, List<RatBkRationAddDtlVm>>();
        if (null != dtlDTOs) {
            // 授信品种明细按授信编码分组
            for (RatBkRationAddDtlVm dtlDTO : dtlDTOs) {
                List<RatBkRationAddDtlVm> ratList = ratMap.get(dtlDTO.getRatCode());
                if (null == ratList) {
                    ratList = new ArrayList<RatBkRationAddDtlVm>();
                    ratMap.put(dtlDTO.getRatCode(), ratList);
                }
                ratList.add(dtlDTO);
            }
        }
        for (RatBkrationAddListVO bkrationVO : data) {
            String ratCode = bkrationVO.getId();
            List<RatBkRationAddDtlVm> ratList = ratMap.get(ratCode);
            if (EmptyUtils.isEmptyList(ratList)) {
                ratList = new ArrayList<RatBkRationAddDtlVm>();
            }
            setRationDtls(ratList);
            // 单品种条数
            Integer singItemSize = Integer.valueOf(0);
            // 综合品种条数
            Integer compItemSize = Integer.valueOf(0);
            boolean compFlag = false;
            // Integer itemOrder = Integer.valueOf(0);
            for (RatBkRationAddDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                } else {
                    if (!compFlag) {// 没有设置过，则设置第一个品种的综合标识
                        dtlDTO.setFirstComp("1");
                        compFlag = true;
                    }
                    compItemSize++;
                }
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setCompItemSize(compItemSize);// 综合品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }
    /**
     * 功能介绍 : 按照品种类型和品种编号排序。<br>
     * <b>日期：</b>2017年8月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private void setRationDtls(List<RatBkRationAddDtlVm> ratList) {
        // 按照单品种和综合品种排序
        if (EmptyUtils.isEmptyList(ratList)) {
            ratList = new ArrayList<RatBkRationAddDtlVm>();
        }
        Collections.sort(ratList, new Comparator<RatBkRationAddDtlVm>() {
            public int compare(RatBkRationAddDtlVm o1, RatBkRationAddDtlVm o2) {
                if (o1.getItemFlag().equals(o2.getItemFlag())) {
                    return o1.getItemOrder().compareTo(o2.getItemOrder());
                }
                return o1.getItemFlag().compareTo(o2.getItemFlag());
            }
        });
        int size = 1;
        for (RatBkRationAddDtlVm addDtlDTO : ratList) {
            addDtlDTO.setItemOrder(size++);
        }
    }
    /**
     * 功能介绍 : 新增追加初始化追加信息。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationAddPageVO editRatRationAddInfo(String addCode) {
        return setEditRationAddMoney(addCode);
    }
    /**
     * 功能介绍 : 根据追加编号获取本次追加的明细。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public Map<String, RatBkRationAddDtlVm> getRationAddDtlByAddCode(String addCode) {
        List<String> addCodes = Lists.newArrayList();
        addCodes.add(addCode);
        List<RatBkRationAddDtlVm> dtlDTOs = addDetailService.getBkrationDtlByRatCodes(addCodes);
        return Maps.uniqueIndex(dtlDTOs, RatBkRationAddDtlVm::getItemCode);
    }
    private void setAddLeftMoney(String itemFlag, RatBkRationAddDtlVm dtlDTO, RatBkrationPageVO pageVO) {
        if (RationConstant.RAT_COMFLAG_0.equals(itemFlag)) {
            // 单品种
            List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
            Map<String, RatBkRationDtlVm> dtlMap = Maps.uniqueIndex(singRationDtls,
                    new Function<RatBkRationDtlVm, String>() {
                        @Override
                        public String apply(RatBkRationDtlVm input) {
                            return input.getItemCode();
                        }
                    });
            RatBkRationDtlVm ratDtl = dtlMap.get(dtlDTO.getItemCode());// 授信品种
            if (null != ratDtl) {
                dtlDTO.setLeftMoney(ratDtl.getLeftMoney());
                dtlDTO.setLeftOpenMoney(ratDtl.getLeftOpenMoney());
            } else {
                dtlDTO.setLeftMoney(dtlDTO.getMoney());
                dtlDTO.setLeftOpenMoney(dtlDTO.getOpenMoney());
            }
        } else {
            // 综合品种
            List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
            Map<String, RatBkRationDtlVm> dtlMap = Maps.uniqueIndex(compRationDtls,
                    new Function<RatBkRationDtlVm, String>() {
                        @Override
                        public String apply(RatBkRationDtlVm input) {
                            return input.getItemCode();
                        }
                    });
            RatBkRationDtlVm ratDtl = dtlMap.get(dtlDTO.getItemCode());// 授信品种
            if (null != ratDtl) {
                dtlDTO.setLeftMoney(ratDtl.getLeftMoney());
                dtlDTO.setLeftOpenMoney(ratDtl.getLeftOpenMoney());
                dtlDTO.setLeftMaxMoney(ratDtl.getLeftMaxMoney());
            } else {
                dtlDTO.setLeftMoney(dtlDTO.getMoney());
                dtlDTO.setLeftOpenMoney(dtlDTO.getMoney());
                dtlDTO.setLeftMaxMoney(dtlDTO.getLeftMoney());
            }
        }
    }
    /**
     * 功能介绍 : 设置授信追加额度。<br>
     * <b>日期：</b>2017年8月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private RatBkrationAddPageVO setEditRationAddMoney(String addCode) {
        RatBkrationAddPageVO addVO = new RatBkrationAddPageVO();
        RatBkrationAdd bkrationAdd = get(addCode, true);
        addVO = setInitRationAddMoney(bkrationAdd.getRatCode());// 设置已授信额度
        addVO.setAddCode(addCode);
        // addVO.setRatCode(bkrationAdd.getRatCode());
        addVO.setAddMoney(bkrationAdd.getMoney());// 已追加金额
        addVO.setAddOpenMoney(bkrationAdd.getOpenMoney());// 已追加敞口额度
        addVO.setRatAddCode(bkrationAdd.getAddCode());
        addVO.setAttachmentId(bkrationAdd.getAttachmentId());
        RatBkrationPageVO pageVO = bkrationService.getBkRationBeanById(bkrationAdd.getRatCode());// 授信录入明细
        // FIXME 设置剩余额度
        disService.getRationLeftMoney(pageVO);
        Map<String, RatBkRationAddDtlVm> dtlMap = getRationAddDtlByAddCode(addCode);
        // 设置本次授信追加的额度
        List<RatBkRationAddDtlVm> singRationDtls = addVO.getSingRationDtls();
        for (RatBkRationAddDtlVm dtlDTO : singRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationAddDtlVm tempDTO = dtlMap.get(itemCode);
            if (null != tempDTO && RationConstant.RAT_COMFLAG_0.equals(tempDTO.getItemFlag())) {
                dtlDTO.setAddMoney(tempDTO.getMoney());// 已追加金额
                dtlDTO.setAddOpenMoney(tempDTO.getOpenMoney());// 已追加敞口额度
                if (dtlDTO.getMaxMoney() != null && dtlDTO.getMaxMoney().compareTo(BigDecimal.ZERO) == 0) {
                    addVO.setItemSize(addVO.getItemSize() + 1);
                }
            } else {
                dtlDTO.setAddMoney(BigDecimal.ZERO);// 已追加金额
                dtlDTO.setAddOpenMoney(BigDecimal.ZERO);// 已追加敞口额度
            }
            setAddLeftMoney(RationConstant.RAT_COMFLAG_0, dtlDTO, pageVO);
        }
        // 综合品种
        List<RatBkRationAddDtlVm> compRationDtls = addVO.getCompRationDtls();
        List<String> keyList = new ArrayList<String>(dtlMap.keySet());
        for (RatBkRationAddDtlVm dtlDTO : compRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationAddDtlVm tempDTO = dtlMap.get(itemCode);
            if (null != tempDTO && RationConstant.RAT_COMFLAG_1.equals(tempDTO.getItemFlag())) {
                dtlDTO.setAddMoney(tempDTO.getMoney());// 已追加金额
                dtlDTO.setAddOpenMoney(tempDTO.getOpenMoney());// 已追加敞口额度
                dtlDTO.setAddMaxMoney(tempDTO.getMaxMoney());// 已追加最高金额
            } else {
                dtlDTO.setAddMoney(BigDecimal.ZERO);// 已追加金额
                dtlDTO.setAddOpenMoney(BigDecimal.ZERO);// 已追加敞口额度
                dtlDTO.setAddMaxMoney(BigDecimal.ZERO);// 最高金额
            }
            keyList.remove(itemCode);
            setAddLeftMoney(RationConstant.RAT_COMFLAG_1, dtlDTO, pageVO);
        }
        if (keyList != null) {
            for (String k : keyList) {
                RatBkRationAddDtlVm tempDTO = dtlMap.get(k);
                if (null != tempDTO) {
                    if (tempDTO.getMaxMoney().compareTo(BigDecimal.ZERO) == 1) {
                        tempDTO.setAddMoney(tempDTO.getMoney());// 已追加金额
                        tempDTO.setAddOpenMoney(tempDTO.getOpenMoney());// 已追加敞口额度
                        tempDTO.setAddMaxMoney(tempDTO.getMaxMoney());// 已追加最高金额
                        tempDTO.setIsNowAdd("1");
                        pageVO.setCompItemSize(pageVO.getCompItemSize() + 1);
                        compRationDtls.add(tempDTO);
                        setAddLeftMoney(RationConstant.RAT_COMFLAG_1, tempDTO, pageVO);
                        addVO.setCompItemSize(pageVO.getCompItemSize() + 1);
                    }
                }
            }
        }
        return addVO;
    }
    /**
     * 功能介绍 : 新增追加初始化追加信息。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationAddPageVO getRatRationAddInfo(String ratCode) {
        // 设置授信已追加额度
        return setInitRationAddMoney(ratCode);
    }
    /**
     * 功能介绍 : 初始化授信品种已追加的额度。<br>
     * <b>日期：</b>2017年8月9日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void initAddSingRationDtls(RatBkrationPageVO pageVO) {
        int itemSize = pageVO.getItemSize();// 所有授信品种数
        List<RatItemCustomDTO> itemList = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_0);
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        Map<String, RatBkRationDtlVm> itemMap = Maps.uniqueIndex(singRationDtls,
                new Function<RatBkRationDtlVm, String>() {
                    public String apply(RatBkRationDtlVm input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        Map<String, RatBkRationDtlVm> compMap = Maps.uniqueIndex(compRationDtls,
                new Function<RatBkRationDtlVm, String>() {
                    public String apply(RatBkRationDtlVm input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkRationDtlVm> dtlDTOs = new ArrayList<RatBkRationDtlVm>();
        for (RatItemCustomDTO custom : itemList) {
            boolean flag = true;
            String itemCode = custom.getId();// 品种编号
            RatBkRationDtlVm dtlDTO = itemMap.get(itemCode);// 单品种中查找
            if (null == dtlDTO) {
                dtlDTO = compMap.get(itemCode);// 综合品种查找、
                if (null == dtlDTO) {
                    dtlDTO = new RatBkRationDtlVm();// 初始化新品种
                    itemSize++;
                    setRationDtlDTO(dtlDTO, custom, itemSize);
                } else {
                    flag = false;
                }
            }
            if (flag) {
                dtlDTOs.add(dtlDTO);
            }
        }
        pageVO.setSingRationDtls(dtlDTOs);
        pageVO.setSingItemSize(dtlDTOs.size());
    }
    public void setRationDtlDTO(RatBkRationDtlVm dtlDTO, RatItemCustomDTO custom, Integer itemOrder) {
        dtlDTO.setItemName(custom.getName());
        dtlDTO.setItemCode(custom.getId());
        dtlDTO.setItemOrder(itemOrder);
        dtlDTO.setItemFlag(RationConstant.RAT_COMFLAG_0);// 单品种
        dtlDTO.setMoney(BigDecimal.ZERO);
        dtlDTO.setOpenMoney(BigDecimal.ZERO);
        dtlDTO.setSuperAddMoney(BigDecimal.ZERO);
        dtlDTO.setSuperAddOpenMoney(BigDecimal.ZERO);
    }
    /**
     * 功能介绍 : 获得追加单位审批通过的银行授信追加信息。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationDisListDTO getPassRatBkRationByRatCode(String disCode) {
        return null;
    }
    /**
     * 功能介绍 : 设置授信追加初始化已授信额度。<br>
     * <b>日期：</b>2017年8月8日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationAddPageVO setInitRationAddMoney(String ratCode) {
        RatBkrationAddPageVO addVO = new RatBkrationAddPageVO();
        RatBkrationPageVO pageVO = bkrationService.getBkRationBeanById(ratCode);// 授信录入明细
        BeanUtils.copyProperties(pageVO, addVO);
        initAddSingRationDtls(pageVO);
        // FIXME 设置剩余额度
        disService.getRationLeftMoney(pageVO);
        BigDecimal money = BigDecimal.ZERO;
        BigDecimal openMoney = BigDecimal.ZERO;
        // 单品种
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        if (null != singRationDtls && !singRationDtls.isEmpty()) {
            List<RatBkRationAddDtlVm> singDtls = BeanUtils.copyListProperties(singRationDtls,
                    RatBkRationAddDtlVm.class);// 单授信品种
            for (RatBkRationAddDtlVm dtlDTO : singDtls) {
                money = money.add(dtlDTO.getMoney());
                openMoney = openMoney.add(dtlDTO.getOpenMoney());
            }
            addVO.setSingRationDtls(singDtls);
        }
        // 综合品种
        List<RatBkRationDtlVm> compRationDtls = pageVO.getCompRationDtls();
        if (null != compRationDtls && !compRationDtls.isEmpty()) {
            List<RatBkRationAddDtlVm> compDtls = BeanUtils.copyListProperties(compRationDtls,
                    RatBkRationAddDtlVm.class);// 综合授信品种
            for (RatBkRationAddDtlVm dtlDTO : compDtls) {
                money = money.add(dtlDTO.getMoney());
                openMoney = openMoney.add(dtlDTO.getOpenMoney());
                if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getCompSign())) {
                    addVO.setCompItemIndex(dtlDTO.getItemOrder());
                }
            }
            addVO.setCompRationDtls(compDtls);
        }
        addVO.setMoney(money);
        addVO.setOpenMoney(openMoney);
        addVO.setTypeName(pageVO.getBankTypeName());
        return addVO;
    }
    /**
     * 功能介绍 : 保存追加信息。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void editRationAdd(RatBkRationAddVm listDTO, String userId, String loginName) {
        notificationService.complete(NotificationConstant.CMS.RAT_ADD, listDTO.getId(), userId);
        // 删除原追加明细信息,重新保存新录入的授信追加明细
        addDetailService.deleteBkrationDisDtlByDisCode(listDTO);
        saveRationAdd(listDTO, userId, loginName);
    }
    /**
     * 功能介绍 : 保存追加信息。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveRationAdd(RatBkRationAddVm listDTO, String userId, String loginName) {
        // 如果只在同品种追加，则不用校验追加的品种是否已存在授信区间
        checkRatRationForAdd(listDTO);// 检查追加品种是否已存在交叉授信
        checkGroupBankRatAdd(listDTO);// 校验集团授信额度是否超过
        RatBkrationAdd rationAdd = new RatBkrationAdd();
        if (!StringUtils.isEmpty(listDTO.getId())) {
            rationAdd = get(listDTO.getId());
        } else {
            rationAdd.setAddCode(bkrationService.getRatCodeNo(SequenceEnum.RAT_BKRATION_ADDCODE,
                    RationConstant.RAT_BKRATION_ADD_CODE));// 授信追加编号
        }
        BeanUtils.copyProperties(listDTO, rationAdd);
        rationAdd.setMoney(listDTO.getMoneysum());// 授信合计金额
        rationAdd.setOpenMoney(listDTO.getOpenMoneysum());// 敞口合计
        if (!StringUtils.isEmpty(listDTO.getId())) {
            rationAdd.setUpdateBy(userId);
            rationAdd.setUpdateDate(DateUtils.getDate());
        } else {
            rationAdd.setCreateBy(userId);
            rationAdd.setCreateDate(DateUtils.getDate());
        }
        ratBkrationAddDao.saveOrUpdate(rationAdd);// 保存追加主表
        List<RatBkrationAddDetail> details = new ArrayList<RatBkrationAddDetail>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        setRationAddDtlOrder(listDTO.getRatCode(), rationDtls);// 设置新追加品种顺序
        details = BeanUtils.copyListProperties(rationDtls, RatBkrationAddDetail.class);
        // 保存追加明细
        for (RatBkrationAddDetail addDetail : details) {
            addDetail.setAddCode(rationAdd.getId());// 追加主表ID
            addDetailService.saveRatBkrationAddDetail(addDetail);
        }
        // 授信追减冻结在审占用额度
        freezeGroupRationAdd(listDTO);
        // todo:以下是追加操作占用保证金
        saveOrUpdateFcBondApply(listDTO, rationAdd, loginName);
        // 记录授信追加操作日志
        // todo:挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(rationAdd.getStatus())) {
            if (RationConstant.RAT_ADD_FLAG_BANK.equals(listDTO.getFlag())) {// 银行追加
                 getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_ADD, rationAdd.getId(), rationAdd,
                        rationAdd.getCorpId(), userId);
            } else {// 集团追加
                 getStartHandler().handler(RatWorkFlowConstant.RAT_BKRATION_CPADD, rationAdd.getId(), rationAdd,
                        rationAdd.getCorpId(), userId);
            }
        }
    }
    /**
     * 追加操作占用保证金
     *
     * @param listDTO
     * @param rationAdd
     */
    public void saveOrUpdateFcBondApply(RatBkRationAddVm listDTO, RatBkrationAdd rationAdd, String loginName) {
        List<FcBondApplyDTO> listFc = fcBondHookService.getFcBondApplyListByItemId(rationAdd.getRatCode());
        FcBondApplyDTO mdto = null;
        if (listFc != null && listFc.size() > 0) {
            mdto = listFc.get(0);
        }
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(rationAdd.getRatCode(), rationAdd.getAddCode());
        if (mdto != null && dto == null && StringUtils.isNotEmpty(listDTO.getBondAddMoney())) {
            if (listDTO.getBondAddMoney().intValue() != 0) {
                FcBondApplyDTO dto1 = new FcBondApplyDTO();
                BeanUtils.copyNotNullProperties(mdto, dto1, "id");
                dto1.setApplyMoney(listDTO.getBondAddMoney());
                dto1.setItemType(FcBondConstant.ITEM_TYPE_ADD);
                dto1.setItemNo(rationAdd.getAddCode());
                dto1.setStatus(FcBondConstant.STATUE_APPLY);// 占用
                dto1.setUpdateBy(loginName);// 当前登录名
                dto1.setUpdateDate(DateUtils.getDate());// 当前时间
                fcBondHookService.addFcBondApply(dto1);// 跟新
            }
        }
        if (mdto != null && dto != null) {
            dto.setApplyMoney(listDTO.getBondAddMoney());
            dto.setStatus(FcBondConstant.STATUE_APPLY);
            BigDecimal addMoney = new BigDecimal(0);
            if (listDTO.getBondAddMoney() != null) {
                addMoney = addMoney.add(listDTO.getBondAddMoney());
            }
            dto.setApplyMoney(addMoney);
            dto.setUpdateBy(loginName);// 当前登录名
            dto.setUpdateDate(DateUtils.getDate());// 当前时间
            fcBondHookService.updateFcBondApply(dto);
        }
    }
    /**
     * 功能介绍 : 集团授信额度追减情况下冻结额度。<br>
     * <b>日期：</b>2017年10月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void freezeGroupRationAdd(RatBkRationAddVm listDTO) {
        // 银行授信追加并且是集团授信不做控制
        RatBkrationPageVO ration = bkrationService.getRatBkrationInfo(listDTO.getRatCode());
        if (RationConstant.RATMODE_0.equals(ration.getRatMode())
                && RationConstant.RAT_ADD_FLAG_BANK.equals(listDTO.getFlag())) {
            return;
        }
        if (!RationConstant.STATUS_WAIT_CHECK.equals(listDTO.getStatus())) {// 待审批才冻结
            return;
        }
        updateRatBkRationDis(listDTO);
    }
    private void setRationAddDtlOrder(String ratCode, List<RatBkRationAddDtlVm> rationDtls) {
        List<RatBkRationDtlVm> dtlDTOs = rationDtlService.getBkrationDtlDTOByRatCode(ratCode, null);
        Map<String, RatBkRationDtlVm> dtlMap = Maps.uniqueIndex(dtlDTOs, new Function<RatBkRationDtlVm, String>() {
            @Override
            public String apply(RatBkRationDtlVm input) {
                return input.getItemCode();
            }
        });
        Integer itemOrder = dtlDTOs.size() + 1;
        for (RatBkRationAddDtlVm addDtlDTO : rationDtls) {
            RatBkRationDtlVm dtlDTO = dtlMap.get(addDtlDTO.getItemCode());
            if (null == dtlDTO) {
                addDtlDTO.setItemOrder(itemOrder);
                itemOrder++;
            }
        }
    }
    /**
     * 功能介绍 : 银行授信追加通过更新授信录入和追加信息。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationInfo(RatBkRationAddVm listDTO) {
        // TODO 挂审批通过追加追加数据
        if (CommonConstant.STATUE_CHECK.equals(listDTO.getStatus())) {
            RatBkrationPageVO ration = bkrationService.getRatBkrationInfo(listDTO.getRatCode());
            if (RationConstant.RATMODE_0.equals(ration.getRatMode())) {// 集团统一授信
                if (RationConstant.RAT_ADD_FLAG_BANK.equals(listDTO.getFlag())) {// 银行授信
                    updateRatBkRation(listDTO);// 追加的直接更新到银行授信信息中去
                } else {
                    updateRatBkRationDis(listDTO);// 追加的更新到银行授信分配中去
                }
            } else {// 自主录入
                updateRatBkRation(listDTO);// 追加的直接更新到银行授信信息中去
                updateRatBkRationDis(listDTO);// 追加的更新到银行授信分配中去
            }
        }
    }
    private Map<String, List<RatBkrationDisDtlDTO>> groupByItemCode(List<RatBkrationDisDtlDTO> rationDtls) {
        Map<String, List<RatBkrationDisDtlDTO>> ratDtlMap = new HashMap<String, List<RatBkrationDisDtlDTO>>();
        for (RatBkrationDisDtlDTO disDtlDTO : rationDtls) {
            List<RatBkrationDisDtlDTO> disList = ratDtlMap.get(disDtlDTO.getItemCode());
            if (null == disList) {
                disList = new ArrayList<RatBkrationDisDtlDTO>();
                ratDtlMap.put(disDtlDTO.getItemCode(), disList);
            }
            disList.add(disDtlDTO);
        }
        return ratDtlMap;
    }
    /**
     * 功能介绍 : 根据追加明细更新授信分配额度。<br>
     * <b>日期：</b>2017年10月12日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisMoney(Integer status, String disCode, RatBkrationDisDtlDTO dto,
            RatBkRationAddDtlVm dtlDTO, String ratCode) {
        BigDecimal money = dtlDTO.getMoney();// 追加额度
        BigDecimal openMoney = dtlDTO.getOpenMoney();// 追加敞口额度
        BigDecimal maxMoney = dtlDTO.getMaxMoney();// 追加额度
        BigDecimal preMoney = dto.getMoney();// 原分配额度
        BigDecimal preOpenMoney = dto.getOpenMoney();// 原分配敞口额度
        if (money.compareTo(BigDecimal.ZERO) < 0 || (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())
                && maxMoney.compareTo(BigDecimal.ZERO) < 0)) {// 追减业务
            // 银行授信追加并且是集团授信不做控制
            RatBkrationPageVO ration = bkrationService.getRatBkrationInfo(ratCode);
            if (RationConstant.RATMODE_0.equals(ration.getRatMode())
                    && RationConstant.RAT_ADD_FLAG_BANK.equals(dtlDTO.getFlag())) {
                return;
            }
            if (RationConstant.STATUS_WAIT_CHECK.equals(status)) {// 待审批
                if (disCode.equals(dto.getDisCode())) {// 当前授信追减
                    dto.setFreezeMoney(dto.getFreezeMoney().subtract(money));// 冻结额度增加
                    dto.setFreezeOpenMoney(dto.getFreezeOpenMoney().subtract(openMoney));// 冻结敞口额度增加
                    dto.setFreezeMaxMoney(dto.getFreezeMaxMoney().subtract(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 冻结最高额度增加
                } else {
                    dto.setVirtualUration(dto.getVirtualUration().subtract(money));// 虚拟冻结额度增加
                    dto.setVirtualOpenUration(dto.getVirtualOpenUration().subtract(openMoney));// 虚拟冻结敞口额度增加
                    dto.setVirtualMaxMoney(dto.getVirtualMaxMoney().subtract(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 虚拟冻结最高额度增加
                }
            } else if (RationConstant.STATUS_BACK.equals(status)) {// 打回
                if (disCode.equals(dto.getDisCode())) {// 当前授信追减
                    dto.setFreezeMoney(dto.getFreezeMoney().add(money));// 冻结额度减少
                    dto.setFreezeOpenMoney(dto.getFreezeOpenMoney().add(openMoney));// 冻结敞口额度减少
                    dto.setFreezeMaxMoney(dto.getFreezeMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 冻结最高额度减少
                } else {
                    dto.setVirtualUration(dto.getVirtualUration().add(money));// 虚拟冻结额度减少
                    dto.setVirtualOpenUration(dto.getVirtualOpenUration().add(openMoney));// 虚拟冻结敞口额度减少
                    dto.setVirtualMaxMoney(dto.getVirtualMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 虚拟冻结最高额度减少
                }
            } else if (RationConstant.STATUS_CHECK.equals(status)) {// 审批通过,还原冻结额度，追减分配额度
                if (disCode.equals(dto.getDisCode())) {// 当前授信追减
                    dto.setFreezeMoney(dto.getFreezeMoney().add(money));// 冻结额度减少
                    dto.setFreezeOpenMoney(dto.getFreezeOpenMoney().add(openMoney));// 冻结敞口额度减少
                    dto.setFreezeMaxMoney(dto.getFreezeMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 冻结最高额度减少
                } else {
                    dto.setVirtualUration(dto.getVirtualUration().add(money));// 虚拟冻结额度减少
                    dto.setVirtualOpenUration(dto.getVirtualOpenUration().add(openMoney));// 虚拟冻结敞口额度减少
                    dto.setVirtualMaxMoney(dto.getVirtualMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 虚拟冻结最高额度减少
                }
                dto.setMoney(money.add(preMoney));// 新追加额度=原追加额度+本次追加额度
                dto.setOpenMoney(openMoney.add(preOpenMoney));// 新追加敞口额度=原追加敞口额度+本次追加敞口额度
                dto.setMaxMoney(dto.getMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 新追加最高额度=原追加最高额度+本次追加最高额度
            }
        } else {
            if (RationConstant.STATUS_CHECK.equals(status)) {// 审批通过
                dto.setMoney(money.add(preMoney));// 新追加额度=原追加额度+本次追加额度
                dto.setOpenMoney(openMoney.add(preOpenMoney));// 新追加敞口额度=原追加敞口额度+本次追加敞口额度
                dto.setMaxMoney(dto.getMaxMoney().add(dtlDTO.getMaxMoney() == null ? BigDecimal.ZERO : dtlDTO.getMaxMoney()));// 新追加最高额度=原追加最高额度+本次追加最高额度
            }
        }
        disDtlService.updateRationDtlDisMoney(dto);// 更新授信追加额度
    }
    /**
     * 功能介绍 : 自主录入的授信的直接更新到追加表中(集团授信追加)。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatBkRationDis(RatBkRationAddVm listDTO) {
        getEntityDao().flush();
        // 根据追加单位和授信编号获取单位追加的数据
        RatBkrationDisListDTO disDTO = disService.geBkrationDisListDTOById(listDTO.getRatCode(), listDTO.getCorpId());
        if (null == disDTO) {
            return;
        }
        List<RatBkRationAddDtlVm> ratAddDtls = listDTO.getRationDtls();
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        Map<String, List<RatBkrationDisDtlDTO>> ratDtlMap = groupByItemCode(rationDtls);
        boolean isExistComp = false;
        for (RatBkrationDisDtlDTO dtls : rationDtls) {
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtls.getItemFlag())) {
                isExistComp = true;
                break;
            }
        }
        for (RatBkRationAddDtlVm dtlDTO : ratAddDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            BigDecimal money = dtlDTO.getMoney();// 追加额度
            BigDecimal openMoney = dtlDTO.getOpenMoney();// 追加敞口额度
            List<RatBkrationDisDtlDTO> dtoList = ratDtlMap.get(itemCode);// 授信品种明细
            if (!EmptyUtils.isEmptyList(dtoList)) {
                for (RatBkrationDisDtlDTO dto : dtoList) {
                    updateRationDisMoney(listDTO.getStatus(), disDTO.getId(), dto, dtlDTO, listDTO.getRatCode());
                    /*
                     * BigDecimal preMoney = dto.getMoney();// 原追加额度 BigDecimal preOpenMoney = dto.getOpenMoney();//
                     * 原追加敞口额度 dto.setMoney(money.add(preMoney));// 新追加额度=原追加额度+本次追加额度
                     * dto.setOpenMoney(openMoney.add(preOpenMoney));// 新追加敞口额度=原追加敞口额度+本次追加敞口额度
                     * dto.setMaxMoney(dto.getMaxMoney().add(dtlDTO.getMaxMoney()));// 新追加最高额度=原追加最高额度+本次追加最高额度
                     * disDtlService.updateRationDtlDisMoney(dto);// 更新授信追加额度
                     */
                }
            } else {
                // 获取共享条数
                RatBkrationDis dis = disService.get(disDTO.getId(), true);
                if (RationConstant.DIS_TYPE_AMOUNT_SHARE.equals(dis.getDistributeWay())) {
                    List<RatBkrationDis> shareList = disService.getRatBkDisWayByRatCode(listDTO.getRatCode(), null,
                            dis.getDistributeWay());
                    if (!EmptyUtils.isEmptyList(dtoList)) {
                        for (RatBkrationDis bkrationDis : shareList) {
                            RatBkrationDisDetail newDtl = new RatBkrationDisDetail();
                            BeanUtils.copyProperties(dtlDTO, newDtl);
                            newDtl.setDisCode(bkrationDis.getId());// 分配ID
                            newDtl.setCompSign(RationConstant.RAT_COMFLAG_0);
                            disDtlService.saveRatBkrationDisDetail(newDtl);
                        }
                    }
                } else {
                    // 新增追加明细
                    RatBkrationDisDetail newDtl = new RatBkrationDisDetail();
                    BeanUtils.copyProperties(dtlDTO, newDtl);
                    newDtl.setDisCode(disDTO.getId());// 分配ID
                    if (RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getItemFlag())) {
                        newDtl.setMaxMoney(BigDecimal.ZERO);
                        if (isExistComp == false && RatBkrationAddService.INDEX == 0) {
                            newDtl.setCompSign(RationConstant.RAT_COMFLAG_1);
                        } else {
                            newDtl.setCompSign(RationConstant.RAT_COMFLAG_0);
                        }
                        RatBkrationAddService.INDEX++;
                    } else {
                        newDtl.setMoney(BigDecimal.ZERO);
                        newDtl.setOpenMoney(BigDecimal.ZERO);
                        newDtl.setCompSign(RationConstant.RAT_COMFLAG_0);
                    }
                    disDtlService.saveRatBkrationDisDetail(newDtl);
                }
            }
        }
        // 更新追加额度
        updateRationDisAddMoney(listDTO, isExistComp);// 更新授信明细中追加额度
        RatBkrationAddService.INDEX = 0;
    }
    /**
     * 功能介绍 :更新授信录入明细中追加(追加)额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDisAddMoney(RatBkRationAddVm listDTO, boolean isExistComp) {
        if (!RationConstant.STATUS_CHECK.equals(listDTO.getStatus())) {// 审批通过
            return;
        }
        List<String> ratCodes = new ArrayList<String>();
        ratCodes.add(listDTO.getRatCode());
        List<RatBkRationDtlVm> rationDtls = rationDtlService.getBkrationDtlByRatCodes(ratCodes);
        Map<String, RatBkRationDtlVm> ratDtlMap = Maps.uniqueIndex(rationDtls,
                new Function<RatBkRationDtlVm, String>() {
                    public String apply(RatBkRationDtlVm input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkRationAddDtlVm> ratAddDtls = listDTO.getRationDtls();
        for (RatBkRationAddDtlVm dtlDTO : ratAddDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            BigDecimal money = dtlDTO.getMoney();// 追加额度
            BigDecimal openMoney = dtlDTO.getOpenMoney();// 追加敞口额度
            RatBkRationDtlVm dto = ratDtlMap.get(itemCode);// 授信品种明细
            if (null == dto) {
                dto = createRationDtl(listDTO.getRatCode(), dtlDTO, isExistComp);// 新生成授信录入明细
            }
            // dto.setTotalMoneyAddMoney(dto.getTotalMoneyAddMoney().add(money));
            // dto.setTotalMoneyAddOpenMoney(dto.getTotalMoneyAddOpenMoney().add(openMoney));
            dto.setSuperDistributeMoney(dto.getDisMoney().add(money));// 已追加额度
            dto.setSuperDistributeOpenMoney(dto.getDisOpenMoney().add(openMoney));// 已追加敞口额度
            rationDtlService.updateRationDtlAddMoney(dto);
        }
    }
    /**
     * 功能介绍 : 更新原银行录入授信。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRatBkRation(RatBkRationAddVm listDTO) {
        RatBkRationVm bkrationDTO = bkrationService.geBkrationListDTOByRatCode(listDTO.getRatCode(), null);
        // 追加额度是否自用或者总额共享
        List<RatBkrationDis> bkrationDis = disService.rationIsTotalMoneyDis(listDTO.getRatCode());
        if (null == bkrationDis || bkrationDis.isEmpty()) {
            updateRationDtlAddMoney(listDTO, bkrationDTO, "1");// 未进行过授信追加
        } else {
            updateRationDtlAddMoney(listDTO, bkrationDTO, "0");
        }
    }
    /**
     * 功能介绍 :更新授信录入明细中追加(追加)额度。<br>
     * <b>日期：</b>2017年8月7日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateRationDtlAddMoney(RatBkRationAddVm listDTO, RatBkRationVm bkrationDTO, String flag) {
        List<RatBkRationDtlVm> rationDtls = bkrationDTO.getRationDtls();
        Map<String, RatBkRationDtlVm> ratDtlMap = Maps.uniqueIndex(rationDtls,
                new Function<RatBkRationDtlVm, String>() {
                    public String apply(RatBkRationDtlVm input) {
                        return input.getItemCode();
                    }
                });
        boolean isExistComp = false;
        for (RatBkRationDtlVm dtls : rationDtls) {
            if (RationConstant.RAT_COMP_SIGN_1.equals(dtls.getItemFlag())) {
                isExistComp = true;
                break;
            }
        }
        List<RatBkRationAddDtlVm> ratAddDtls = listDTO.getRationDtls();
        for (RatBkRationAddDtlVm dtlDTO : ratAddDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            BigDecimal money = dtlDTO.getMoney();// 追加额度
            BigDecimal openMoney = dtlDTO.getOpenMoney();// 追加敞口额度
            RatBkRationDtlVm dto = ratDtlMap.get(itemCode);// 授信品种明细
            if (null == dto) {
                dto = createRationDtl(bkrationDTO.getId(), dtlDTO, isExistComp);// 新生成授信录入明细
            }
            BigDecimal superMoney = dto.getSuperAddMoney();// 原追加额度
            BigDecimal superOpenMoney = dto.getSuperAddOpenMoney();// 原追加敞口额度
            dto.setSuperAddMoney(money.add(superMoney));// 新追加额度=原追加额度+本次追加额度
            dto.setSuperAddOpenMoney(openMoney.add(superOpenMoney));// 新追加敞口额度=原追加敞口额度+本次追加敞口额度
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                dto.setSuperAddMaxMoney(dtlDTO.getMaxMoney().add(dto.getSuperAddMaxMoney()));// 新追加最高额度=原追加最高额度+本次追加最高额度
            }
            if ("1".equals(flag)) {// 自用或金额共享追加或还未进行追加
                dto.setTotalMoneyAddMoney(dto.getTotalMoneyAddMoney().add(money));
                dto.setTotalMoneyAddOpenMoney(dto.getTotalMoneyAddOpenMoney().add(openMoney));
            } else if ("2".equals(flag)) {// 更新追加额度
                dto.setSuperDistributeMoney(dto.getSuperDistributeMoney().add(money));// 已追加额度
                dto.setSuperDistributeOpenMoney(dto.getSuperDistributeOpenMoney().add(openMoney));// 已追加敞口额度
            }
            rationDtlService.updateRationDtlAddMoney(dto);
        }
        RatBkrationAddService.INDEX = 0;
    }
    /**
     * 功能介绍 : 新生成授信录入明细。<br>
     * <b>日期：</b>2017年8月9日<br>
     *
     * @version 1.0
     * @author liyh
     * @return
     */
    private RatBkRationDtlVm createRationDtl(String ratCode, RatBkRationAddDtlVm dtlDTO, boolean isExistComp) {
        RatBkrationDetail detail = new RatBkrationDetail();
        BeanUtils.copyProperties(dtlDTO, detail);
        detail.setRatCode(ratCode);
        if (isExistComp == false && RationConstant.RAT_COMP_SIGN_1.equals(dtlDTO.getItemFlag())) {
            if (RatBkrationAddService.INDEX == 0) {
                detail.setCompSign(RationConstant.RAT_COMFLAG_1);
            } else {
                detail.setCompSign(RationConstant.RAT_COMFLAG_0);
            }
            RatBkrationAddService.INDEX++;
        }
        detail.setMoney(BigDecimal.ZERO);
        detail.setOpenMoney(BigDecimal.ZERO);// 新产生的品种没有授信额度和敞口额度
        detail.setMaxMoney(BigDecimal.ZERO);
        detail.setItemOrder(getMaxItemOrder(ratCode) + 1);
        rationDtlService.saveRatBkrationDetail(detail);
        getEntityDao().flush();
        RatBkRationDtlVm dto = new RatBkRationDtlVm();
        BeanUtils.copyProperties(detail, dto);
        return dto;
    }
    // 获取授信录入顺序
    public Integer getMaxItemOrder(String ratCode) {
        List<RatBkRationDtlVm> dtlDTOs = rationDtlService.getBkrationDtlDTOByRatCode(ratCode, null);
        if (!EmptyUtils.isEmptyList(dtlDTOs)) {
            return dtlDTOs.size();
        }
        return Integer.valueOf(0);
    }
    /**
     * 功能介绍 : 集团追加校验。<br>
     * <b>日期：</b>2017年8月5日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkGroupBankRatAdd(RatBkRationAddVm listDTO) {
        // 银行追加不做校验
        if (RationConstant.RAT_ADD_FLAG_BANK.equals(listDTO.getFlag())) {
            return;
        }
        // 授信追加追减额度不能超过剩余额度
    }
    public RatBkrationCpAddListDTO initCpRationAddData(String ratCode, String corpId) {
        RatBkrationCpAddListDTO cpAddDTO = disService.queryPassRationDisData(ratCode, corpId, null);
        /*
         * List<RatBkrationCpAddDtlDTO> singRationDtls = cpAddDTO.getSingRationDtls();//单品种追加额度 Map<String,
         * RatBkrationCpAddDtlDTO> addMap = Maps.uniqueIndex(singRationDtls, new Function<RatBkrationCpAddDtlDTO,
         * String>() {
         *
         * @Override public String apply(RatBkrationCpAddDtlDTO input) { return input.getItemCode(); } }); List<String>
         * ratCodes = new ArrayList<String>(); ratCodes.add(ratCode); List<RatBkRationDtlVm> dtlDTOs =
         * rationDtlService.getBkrationDtlByRatCodes(ratCodes); for (RatBkRationDtlVm dtlDTO : dtlDTOs) { String
         * itemCode = dtlDTO.getItemCode(); if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {//单品种
         * RatBkrationCpAddDtlDTO addDtlDTO = addMap.get(itemCode); if (null == addDtlDTO) { //初始化新的集团追加明细
         * RatBkrationCpAddDtlDTO newDtl = new RatBkrationCpAddDtlDTO(); BeanUtils.copyProperties(dtlDTO, newDtl);
         * RatBkrationMoneyBean ratBean = disService.getBankRationItemMoney(ratCode, corpId, dtlDTO.getItemFlag(),
         * itemCode); BigDecimal money = ratBean.getUnUseMoney(); BigDecimal openMoney = ratBean.getUnUseOpenMoney();
         * newDtl.setAllDisMoney(BigDecimal.ZERO); newDtl.setAllDisOpenMoney(BigDecimal.ZERO);
         * newDtl.setAllAddMoney(money); newDtl.setAllAddOpenMoney(openMoney); singRationDtls.add(newDtl); } } }
         */
        return cpAddDTO;
    }
    /**
     * 功能介绍: 查询已审批通过的授信追加信息。<br>
     * <b>日期：</b>2017年8月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public RatBkrationCpAddListDTO editCpRatRationAddInfo(RatBkrationAdd rationAdd) {
        rationAdd = get(rationAdd.getId(), true);
        RatBkrationCpAddListDTO cpAddListDTO = disService.queryPassRationDisData(rationAdd.getRatCode(),
                rationAdd.getCorpId(), rationAdd.getId());
        cpAddListDTO.setAttachmentId(rationAdd.getAttachmentId());
        return cpAddListDTO;
    }
    /**
     * 功能介绍 : 检查授信品种是否可追加。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void checkRatRationForAdd(RatBkRationAddVm disDTO) {
        // 检查授信追加是否全为0
        List<RatBkRationAddDtlVm> rationDtls = disDTO.getRationDtls();
        if (!EmptyUtils.isEmptyList(rationDtls)) {
            boolean flag = true;
            for (RatBkRationAddDtlVm dtlDTO : rationDtls) {
                if (!dtlDTO.getMoney().equals(BigDecimal.ZERO)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_MONEY_NULL);
            }
        } else {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_MONEY_NULL);
        }
        // 授信是否允许重复
        /**
         * 额度包含或者交叉允许选择 先判断是否允许交叉
         */
        ConfigDTO configDTO = bkrationService.getRatContains();
        // 不允许则出现交叉或包含都不能保存
        if (RationConstant.RAT_CONTAIN_0.equals(configDTO.getValue())) {
            // 授信银行编号+币别+单位+银行+授信日期[不过期]+品种来校验是否可以再次追加给某个单位
            RatBkRationVm bkrationDTO = bkrationService.geBkrationListDTOByRatCode(disDTO.getRatCode(), null);
            disDTO.setCurCode(bkrationDTO.getCurCode());// 币别
            disDTO.setBankCode(bkrationDTO.getBankCode());// 授信银行
            List<RatBkRationVm> rationVOs = bkrationService.getRatBankRationListByCorpId(bkrationDTO);
            if (null == rationVOs || rationVOs.isEmpty()) {
                return;
            }
            for (RatBkRationVm dto : rationVOs) {
                // 相同授信不比较
                if (dto.getId().equals(bkrationDTO.getId())) {
                    continue;
                }
                // 比较授信日期是否包含或交叉
                if (compareDateRelation(bkrationDTO, dto)) {
                    Map<String, List<RatBkRationDtlVm>> destMap = groupDtlDtos(dto);
                    List<RatBkRationDtlVm> destDtlsSingle = destMap.get(RationConstant.RAT_COMFLAG_0);// 单授信品种
                    // 判断授信品种是否相等
                    compareRatRationBeanRepeat(bkrationDTO, trans2DtlMap(destDtlsSingle));
                }
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
    public void compareRatRationBeanRepeat(RatBkRationVm dest, Map<String, RatBkRationDtlVm> souSingMap) {
        // 比较授信品种是否重复
        Map<String, List<RatBkRationDtlVm>> destMap = groupDtlDtos(dest);
        List<RatBkRationDtlVm> destDtlsSingle = destMap.get(RationConstant.RAT_COMFLAG_0);// 单授信品种
        String destItemCode = "";
        // 单授信品种比较
        for (RatBkRationDtlVm destDTO : destDtlsSingle) {
            destItemCode = destDTO.getItemCode();// 授信品种编码
            RatBkRationDtlVm sourceDTO = souSingMap.get(destItemCode);
            if (null != sourceDTO) {
                // 重复追加，单位{0}已经录入过授信
                throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_ITEM_EXIST, destDTO.getItemName());
            }
        }
        // 综合授信只能追加之前录入的明细品种，因此不需要校验
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
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();// 业务ID
        boolean result = workFlowResult.isPass();// 是否通过
        getEntityDao().flush();
        RatBkrationAdd rationAdd = get(businessKey, true);
        if (result) {
            rationAdd.setStatus(RationConstant.STATUS_CHECK);// 审批通过
        } else {
            rationAdd.setStatus(RationConstant.STATUS_BACK);// 审批打回
        }
        RatBkRationAddVm listDTO = new RatBkRationAddVm();
        BeanUtils.copyProperties(rationAdd, listDTO);
        List<String> addCodes = new ArrayList<String>();
        addCodes.add(businessKey);
        List<RatBkRationAddDtlVm> rationDtls = addDetailService.getBkrationDtlByRatCodes(addCodes);
        listDTO.setRationDtls(rationDtls);
        if (result) {
            updateRationInfo(listDTO);
        } else {
            updateRatBkRationDis(listDTO);
        }
        update(rationAdd);
        // 审批通过后更新追加占用的保证金状态
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(rationAdd.getRatCode(), rationAdd.getAddCode());
        if (result) {
            if (dto != null) {
                dto.setStatus(FcBondConstant.STATUE_APPLY);
                BigDecimal itemMoney = dto.getItemMoney();
                dto.setItemMoney(itemMoney.add(rationAdd.getMoney()));
                fcBondHookService.updateFcBondApply(dto);
                List<FcBondApplyDTO> list = fcBondHookService.getFcBondApplyListByItemId(dto.getItemId());
                for (FcBondApplyDTO dto1 : list) {
                    if (!dto.getId().equals(dto1.getId())) {
                        BigDecimal temp = dto1.getItemMoney();
                        dto1.setItemMoney(temp.add(rationAdd.getMoney()));
                        fcBondHookService.updateFcBondItemMoney(dto1);
                    }
                }
            }
        }
        // else{
        // if(dto!=null){
        // dto.setStatus(FcBondConstant.STATUE_BACK);
        // fcBondHookService.updateFcBondApply(dto);
        // }
        // }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 银行授信追加在途数据
            int dataSize1 = ratBkrationAddDao.queryBusBillByCorpId(corpId, "-1,0,11", RationConstant.RAT_ADD_FLAG_BANK);
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE, multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_BANKADD_ERROR)));
            }
            // 集团授信追加在途数据
            int dataSize2 = ratBkrationAddDao.queryBusBillByCorpId(corpId, "-1,0,11",
                    RationConstant.RAT_ADD_FLAG_GROUP);
            if (dataSize2 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_BANKRATION_CPADD_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
