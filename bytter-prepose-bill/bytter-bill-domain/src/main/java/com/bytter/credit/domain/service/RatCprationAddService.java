package com.bytter.credit.domain.service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RatWorkFlowConstant;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.dao.RatCprationAddDao;
import com.bytter.credit.domain.dao.RatCprationDao;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.entity.RatBkrationAddDetail;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.entity.RatBkrationDisDetail;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import com.bytter.credit.domain.vm.RatBkRationDtlVm;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationAddPageVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.dto.*;
import com.bytter.system.api.ICorpDisableVerifyService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCprationAddService
 * @Description:
 * @date 2018年11月15日 下午4:49:42
 */
@Service("RatCprationAddService")
@Transactional
@WorkFlow(value = { RatWorkFlowConstant.RAT_CPRATION_ADD })
@DubboService(interfaceClass = IWorkFlowListener.class,group = RatWorkFlowConstant.RAT_CPRATION_ADD )
public class RatCprationAddService extends BaseService<RatBkrationAdd, String>
        implements IWorkFlowListener, ICorpDisableVerifyService {
    @Autowired
    private RatCprationAddDao ratCprationAddDao;
    @Autowired
    private RatCprationDao ratCprationDao;
    @Autowired
    private RatCprationService ratCprationService;
    @Autowired
    private RatItemCustomService itemCustomService;
    @Autowired
    private RatBkrationAddDetailService addDetailService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private RatBkrationDetailService rationDtlService;
    @Autowired
    private RatCprationLogService ratLogService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private RatCprationDisService disService;
    @Autowired
    private RatBkrationDisDetailService disDtlService;
    @Resource
    private IMultiLangService multiLangServiceI;
    public RatBkrationAddPageVO getRatRationAddInfo(String ratCode) {
        // 设置授信已追加额度
        return setInitRationAddMoney(ratCode);
    }
    /**
     * 设置授信追加初始化已授信额度
     */
    public RatBkrationAddPageVO setInitRationAddMoney(String ratCode) {
        RatBkrationAddPageVO addVO = new RatBkrationAddPageVO();
        RatBkrationPageVO pageVO = ratCprationService.queryCprationById(ratCode);// 授信录入明细
        BeanUtils.copyProperties(pageVO, addVO);
//        initAddSingRationDtls(pageVO);
        BigDecimal money = BigDecimal.ZERO;
        // 单品种
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        if (null != singRationDtls && !singRationDtls.isEmpty()) {
            List<RatBkRationAddDtlVm> singDtls = BeanUtils.copyListProperties(singRationDtls,
                    RatBkRationAddDtlVm.class);// 单授信品种
            for (RatBkRationAddDtlVm dtlDTO : singDtls) {
                money = money.add(dtlDTO.getMoney());
            }
            addVO.setSingRationDtls(singDtls);
        }
        addVO.setMoney(money);
        return addVO;
    }
    /**
     * 功能介绍 : 初始化授信品种已追加的额度
     */
    public void initAddSingRationDtls(RatBkrationPageVO pageVO) {
        int itemSize = pageVO.getItemSize();// 所有授信品种数
        List<RatItemCustomDTO> itemList = itemCustomService.getAllItemCustom(RationConstant.RAT_TYPE_1);
        List<RatBkRationDtlVm> singRationDtls = pageVO.getSingRationDtls();
        Map<String, RatBkRationDtlVm> itemMap = Maps.uniqueIndex(singRationDtls,
                new Function<RatBkRationDtlVm, String>() {
                    public String apply(RatBkRationDtlVm input) {
                        return input.getItemCode();
                    }
                });
        List<RatBkRationDtlVm> dtlDTOs = new ArrayList<RatBkRationDtlVm>();
        for (RatItemCustomDTO custom : itemList) {
            // boolean flag = true;
            String itemCode = custom.getId();// 品种编号
            RatBkRationDtlVm dtlDTO = itemMap.get(itemCode);// 单品种中查找
            if (null == dtlDTO) {
                dtlDTO = new RatBkRationDtlVm();// 初始化新品种
                itemSize++;
                setRationDtlDTO(dtlDTO, custom, itemSize);
            }
            dtlDTOs.add(dtlDTO);
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
        dtlDTO.setSuperAddMoney(BigDecimal.ZERO);
    }
    /**
     * 追加明细
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput queryRationAddData(DataTablesInput input, String userId) {
        DataTablesOutput output = ratCprationAddDao.queryRationAddData(input);
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
            for (RatBkRationAddDtlVm dtlDTO : ratList) {
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    singItemSize++;
                }
            }
            bkrationVO.setItemSize(ratList.size());
            bkrationVO.setSingItemSize(singItemSize);// 单品种条数
            bkrationVO.setRationDtls(ratList);
        }
        return output;
    }
    /**
     * 按照品种排序
     */
    private void setRationDtls(List<RatBkRationAddDtlVm> ratList) {
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
     * 新增追加初始化追加信息
     */
    public RatBkrationAddPageVO editRatRationAddInfo(String addCode) {
        return setEditRationAddMoney(addCode);
    }
    /**
     * 保存内部授信追加信息
     *
     * @param saveType
     *            保存类型 0-暂存 1-提交
     * @return
     */
    public void saveRationAdd(RatBkRationAddVm listDTO, String saveType, ICurrentUser user) {
        RatBkrationAdd rationAdd = new RatBkrationAdd();
        if (!StringUtils.isEmpty(listDTO.getId())) {// 编辑追加数据
            rationAdd = get(listDTO.getId());
        } else {// 新增追加数据
            rationAdd.setAddCode(ratCprationService.getRatCodeNo(SequenceEnum.RAT_CPRATION_ADDCODE,
                    RationConstant.RAT_CPRATION_ADD_CODE));// 授信追加编号
        }
        String msg = null;// 操作消息
        Integer messageType = null;// 操作类型
        BeanUtils.copyProperties(listDTO, rationAdd);
        rationAdd.setMoney(listDTO.getMoneysum());// 授信合计金额
        if (!StringUtils.isEmpty(listDTO.getId())) {// 修改数据
            rationAdd.setUpdateBy(user.getId());
            rationAdd.setUpdateDate(DateUtils.getDate());
            if (saveType.equals("0")) {
                rationAdd.setStatus(RationConstant.STATUS_TEMP);
                msg = RationConstant.MSGA_MODIFY + "";// 修改暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
            } else if (saveType.equals("1")) {
                rationAdd.setStatus(RationConstant.STATUS_WAIT_CHECK);
                msg = RationConstant.MSGA_REPAIR_MODIFY + "";// 修改提交
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
            }
        } else {// 新增数据
            rationAdd.setCreateBy(user.getId());
            rationAdd.setCreateDate(DateUtils.getDate());
            if (saveType.equals("0")) {
                rationAdd.setStatus(RationConstant.STATUS_TEMP);
                msg = RationConstant.MSGA_INPUT + "";// 录入暂存
                messageType = RationConstant.MESSAGE_TYPE_TEMP;
            } else if (saveType.equals("1")) {
                rationAdd.setStatus(RationConstant.STATUS_WAIT_CHECK);
                msg = RationConstant.MSGA_INPUT_SUBMIT;// 录入提交
                messageType = RationConstant.MESSAGE_TYPE_SUBMIT;
            }
        }
        ratCprationDao.saveOrUpdate(rationAdd);// 保存追加主表
        // this.getSession().flush();
        List<RatBkrationAddDetail> details = new ArrayList<RatBkrationAddDetail>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        setRationAddDtlOrder(listDTO.getRatCode(), rationDtls);// 设置新追加品种顺序
        details = BeanUtils.copyListProperties(rationDtls, RatBkrationAddDetail.class);
        // 保存追加明细
        if (listDTO.isUpdateSign()) {// 修改时先删除之前追加的明细，再保存
            addDetailService.deleteBkrationDisDtlByDisCode(listDTO);
            for (RatBkrationAddDetail addDetail : details) {
                addDetail.setAddCode(rationAdd.getId());// 追加主表ID
                addDetailService.saveRatBkrationAddDetail(addDetail);
            }
        } else if (!listDTO.isUpdateSign()) {// 新增追加数据时直接保存追加明细
            for (RatBkrationAddDetail addDetail : details) {
                addDetail.setAddCode(rationAdd.getId());// 追加主表ID
                addDetailService.saveRatBkrationAddDetail(addDetail);
            }
        }
        this.getSession().flush();
        // 写入操作日志
        ratLogService.saveRatProcessMessge(rationAdd.getId(), rationAdd.getMoney(), messageType, msg, user);
        // todo:挂接审批
        if (RationConstant.STATUS_WAIT_CHECK.equals(rationAdd.getStatus())) {
             getStartHandler().handler(RatWorkFlowConstant.RAT_CPRATION_ADD, rationAdd.getId(), rationAdd,
                    rationAdd.getCorpId(), user.getId());
        }
    }
    /**
     * 追加品种排序
     */
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
     * 设置授信追加额度
     */
    private RatBkrationAddPageVO setEditRationAddMoney(String addCode) {
        RatBkrationAddPageVO addVO = new RatBkrationAddPageVO();
        RatBkrationAdd bkrationAdd = get(addCode, true);
        addVO = setInitRationAddMoney(bkrationAdd.getRatCode());// 设置已授信额度
        addVO.setAddCode(addCode);
        addVO.setAddMoney(bkrationAdd.getMoney());// 已追加金额
        Map<String, RatBkRationAddDtlVm> dtlMap = getRationAddDtlByAddCode(addCode);
        // 设置本次授信追加的额度
        List<RatBkRationAddDtlVm> singRationDtls = addVO.getSingRationDtls();
        for (RatBkRationAddDtlVm dtlDTO : singRationDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            RatBkRationAddDtlVm tempDTO = dtlMap.get(itemCode);
            if (null != tempDTO) {
                dtlDTO.setAddMoney(tempDTO.getMoney());// 已追加金额
            } else {
                dtlDTO.setAddMoney(BigDecimal.ZERO);// 已追加金额
            }
        }
        return addVO;
    }
    /**
     * 新生成授信录入明细
     */
    private RatBkRationDtlVm createRationDtl(String ratCode, RatBkRationAddDtlVm dtlDTO) {
        RatBkrationDetail detail = new RatBkrationDetail();
        BeanUtils.copyProperties(dtlDTO, detail);
        detail.setRatCode(ratCode);
        detail.setMoney(BigDecimal.ZERO);
        detail.setCompSign(RationConstant.RAT_COMFLAG_0);
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
     * 功能介绍 : 根据追加编号获取本次追加的明细
     */
    public Map<String, RatBkRationAddDtlVm> getRationAddDtlByAddCode(String addCode) {
        List<String> addCodes = new ArrayList<String>();
        addCodes.add(addCode);
        List<RatBkRationAddDtlVm> dtlDTOs = addDetailService.getBkrationDtlByRatCodes(addCodes);
        Map<String, RatBkRationAddDtlVm> dtlMap = Maps.uniqueIndex(dtlDTOs,
                new Function<RatBkRationAddDtlVm, String>() {
                    public String apply(RatBkRationAddDtlVm input) {
                        return input.getItemCode();
                    }
                });
        return dtlMap;
    }
    /**
     * 追加审批列表数据
     */
    @Transactional(readOnly = true)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataTablesOutput listApproveOutputData(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(RatWorkFlowConstant.RAT_CPRATION_ADD,
                input);
        String businessSql = null;
        if (!EmptyUtils.isEmpty(workFlowBusiness.getBusinessIdList())) {
            businessSql = SqlUtil.getInSql(workFlowBusiness.getBusinessIdList(), "rd.id", true);
        }
        DataTablesOutput dp = ratCprationAddDao.queryRatCpAddApproveData(input, businessSql);
        return workFlowBusiness.merge(dp);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 审批完成
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        ICurrentUser user = workFlowResult.getUser();
        RatBkrationAdd ratCpAdd = this.get(businessKey,true);
        boolean result = workFlowResult.isPass();
        Integer messageType = null;// 操作类型
        String msg = null;// 操作消息
        if (result) {
            ratCpAdd.setStatus(RationConstant.STATUS_CHECK);
            msg = RationConstant.MSGA_CHECKOK + "";
            messageType = RationConstant.MESSAGE_TYPE_CHECK_PASS;// 审批通过
            // 审批通过时,发出通知告知已审批通过
            notificationService.send(ratCpAdd.getId(), ratCpAdd.getCorpId(), NotificationConstant.CMS.CPRATION_ADD_PASS,
                    ratCpAdd);
        } else {
            ratCpAdd.setStatus(RationConstant.STATUS_BACK);
            msg = RationConstant.MSGA_CHECKBACK;
            messageType = RationConstant.MESSAGE_TYPE_CHECK_BACK;// 审批打回
            // 审批打回时,发出通知告知审批打回
            notificationService.send(ratCpAdd.getId(), ratCpAdd.getCorpId(), NotificationConstant.CMS.CPRATION_ADD_BACK,
                    ratCpAdd);
        }
        super.updateEntity(ratCpAdd);
        RatBkRationAddVm listDTO = new RatBkRationAddVm();
        BeanUtils.copyProperties(ratCpAdd, listDTO);
        List<String> addCodes = new ArrayList<String>();
        addCodes.add(ratCpAdd.getId());
        List<RatBkRationAddDtlVm> rationDtls = addDetailService.getBkrationDtlByRatCodes(addCodes);// 查出追加明细
        listDTO.setRationDtls(rationDtls);
        updateRationAddMoney(listDTO);// 更新到录入明细表
        updateRatCpRationDis(listDTO);// 更新到分配明细表
        // 记录到追加日志表
        ratLogService.saveRatProcessMessge(ratCpAdd.getId(), ratCpAdd.getMoney(), messageType, msg, user);
    }
    /**
     * 更新授信录入明细中追加(追减)额度
     */
    public void updateRationAddMoney(RatBkRationAddVm listDTO) {
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
            RatBkRationDtlVm dto = ratDtlMap.get(itemCode);// 授信品种明细
            if (null == dto) {
                dto = createRationDtl(listDTO.getRatCode(), dtlDTO);// 新生成授信录入明细
            }
            BigDecimal superMoney = dto.getSuperAddMoney();// 原追加额度
            dto.setSuperAddMoney(money.add(superMoney));// 新追加额度=原追加额度+本次追加额度
            ratCprationAddDao.updateRationDtlAddMoney(dto);
        }
    }
    /**
     * 直接更新到分配表中
     *
     * @param
     * @return
     */
    public void updateRatCpRationDis(RatBkRationAddVm listDTO) {
        getEntityDao().flush();
        // 根据追加单位和授信编号获取单位分配的数据
        RatBkrationDisListDTO disDTO = disService.geCprationDisListDTOById(listDTO.getRatCode(), listDTO.getCorpId());
        if (null == disDTO) {
            return;
        }
        List<RatBkRationAddDtlVm> ratAddDtls = listDTO.getRationDtls();
        List<RatBkrationDisDtlDTO> rationDtls = disDTO.getRationDtls();
        Map<String, List<RatBkrationDisDtlDTO>> ratDtlMap = groupByItemCode(rationDtls);
        for (RatBkRationAddDtlVm dtlDTO : ratAddDtls) {
            String itemCode = dtlDTO.getItemCode();// 授信品种
            List<RatBkrationDisDtlDTO> dtoList = ratDtlMap.get(itemCode);// 授信品种明细
            if (!EmptyUtils.isEmptyList(dtoList)) {
                for (RatBkrationDisDtlDTO dto : dtoList) {
                    updateRationDisMoney(listDTO.getStatus(), disDTO.getId(), dto, dtlDTO);
                }
            } else {
                // 新增追加明细
                RatBkrationDisDetail newDtl = new RatBkrationDisDetail();
                BeanUtils.copyProperties(dtlDTO, newDtl);
                newDtl.setDisCode(disDTO.getId());// 分配ID
                newDtl.setCompSign(RationConstant.RAT_COMFLAG_0);
                disDtlService.saveRatBkrationDisDetail(newDtl);
            }
        }
    }
    /**
     * 根据追加明细更新授信分配明细额度
     */
    public void updateRationDisMoney(Integer status, String disCode, RatBkrationDisDtlDTO dto,
            RatBkRationAddDtlVm dtlDTO) {
        BigDecimal money = dtlDTO.getMoney();// 追加额度
        BigDecimal preMoney = dto.getMoney();// 原分配额度
        if (RationConstant.STATUS_CHECK.equals(status)) {// 审批通过
            dto.setMoney(money.add(preMoney));// 新的额度=原额度+本次追加额度
        }
        disDtlService.updateRationDtlDisMoney(dto);// 更新授信追加额度
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
    @Override
    public BaseDao<RatBkrationAdd, String> getEntityDao() {
        // TODO Auto-generated method stub
        return ratCprationAddDao;
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 内部授信追加在途数据
            int dataSize = ratCprationAddDao.queryBusBillByCorpId(corpId, "-1,0,11");
            if (dataSize > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.RAT_MANAGE,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.RAT_VALID_DISABLE_CPRATIONADD_ERROR)));
            }
        }
        return verifyErrorDTOS;
    }
}
