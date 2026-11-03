package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.NbconsoleConstant.PayType;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.*;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.biz.payment.api.service.INisMessageService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.api.constant.FinaBookConstant;
import com.bytter.tally.api.constant.FinacialConstant;
import com.bytter.tally.api.constant.FinacialManualConstant;
import com.bytter.tally.api.constant.NbconsoleConstant;
import com.bytter.tally.entity.TallyFcAccbook;
import com.bytter.tally.util.FinacialManualUtil;
import com.bytter.tally.vo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
@Service("finacialManualService")
@Transactional
public class FinacialManualService {
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    /*@Autowired
    private FcBusRaceService busRaceService;*/
    @DubboReference
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference
    private ITallyService tallyService;
    @DubboReference(check = false)
    private IInnerAccountService iInnerAccountService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService iFcBusinessRuleDefineService;
    @DubboReference(check = false)
    private IConfigService iConfigService;
    @DubboReference(check = false)
    private IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadServiceImpl;
    @DubboReference(check = false)
    private IFcAccbookDtlService iFcAccbookDtlService;
    @DubboReference(check = false)
    private IFcAccbookExtService iFcAccbookExtService;
    @DubboReference(check = false)
    private INisAllocationReqService inisAllocationReqService;
    @DubboReference(check = false)
    private INisMessageService iNisMessageService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @Autowired
    private FinacialTallyRemindService finacialTallyRemindService;
    /**
     * 构建会计分录
     */
    public FcAccbookPageVO processAccbook(String key, String type, String menuType, ICurrentUser user) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
        Class<?> classs = Class.forName(clazz);
        Method findMethod = ReflectionUtils.findMethod(classs, FinacialManualConstant.FC_PROCESS_ACCBOOK, String.class, String.class, ICurrentUser.class);
        Object invoke = ReflectionUtils.invokeMethod(findMethod, classs.newInstance(), key, type, user);
        return (FcAccbookPageVO) invoke;
    }
    // 查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, ICurrentUser user) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String menuType = (String) dataTablesInput.getFormdata().get("menuType");
        String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
        Class<?> class1 = Class.forName(clazz);
        Method findMethod = ReflectionUtils.findMethod(class1, FinacialManualConstant.FC_QUERY_DATE, DataTablesInput.class, ICurrentUser.class);
        Object invoke = ReflectionUtils.invokeMethod(findMethod, class1.newInstance(), dataTablesInput, user);
        return (DataTablesOutput) invoke;
    }
    /**
     * 查询有权限的内部帐号
     */
    public List<FcAccDTO> getFcAcc(String menuType, String key, ICurrentUser user) {
        try {
            String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
            Class<?> class1 = Class.forName(clazz);
            Method findMethod = ReflectionUtils.findMethod(class1, FinacialManualConstant.FC_GET_FCACC, String.class, ICurrentUser.class);
            Object invokeMethod = ReflectionUtils.invokeMethod(findMethod, class1.newInstance(), key, user);
            return (List<FcAccDTO>) invokeMethod;
        } catch (Exception e) {
            ParameterException targetEx = (ParameterException) e;
            throw new ParameterException(targetEx.getIMessageEnum(), targetEx.getParams());
        }
        //return null;
    }
    /**
     * 查询有权限的内部帐号
     */
    public List<FcAccDTO> getFcAccDebit(String menuType, String key, ICurrentUser user) {
        try {
            String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
            Class<?> class1 = Class.forName(clazz);
            Method findMethod = ReflectionUtils.findMethod(class1, FinacialManualConstant.FC_GET_FCACC_DEBIT, String.class, ICurrentUser.class);
            Object invokeMethod = ReflectionUtils.invokeMethod(findMethod, class1.newInstance(), key, user);
            return (List<FcAccDTO>) invokeMethod;
        } catch (Exception e) {
            ParameterException targetEx = (ParameterException) e;
            throw new ParameterException(targetEx.getIMessageEnum(), targetEx.getParams());
        }
        //return null;
    }
    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, ConstantBean<String> sysRaceBean, String userId) {
        CorpDTO corp = iCorpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
//    	List<FcBusRaceVo> raceList = busRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!EmptyUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(sysRaceBean.get());
            tempRace.setRaceName(sysRaceBean.getName());
            tempRace.setSysRaceCode(sysRaceBean.get());
			/*FcBusRace fcBusRace = new FcBusRace();
			BeanUtils.copyProperties(tempRace,fcBusRace);
			busRaceService.save(fcBusRace, new String[]{netCode},userId);
			raceList = busRaceService.findBySysRaceCode(sysRaceBean.get());
			tempRace = raceList.get(0);*/
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(sysRaceBean.get());
            BeanUtils.copyProperties(fcBusRaceDTOS1.get(0), tempRace);
        }
        return tempRace;
    }
    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlVo> getControlBeanEdit(int size) {
        List<VoucherRenderControlVo> controBeans = new ArrayList<VoucherRenderControlVo>();
        VoucherRenderControlVo controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlVo();
            controBean.setSummaryEditable(true);
            controBean.setAccountEditable(false);
            controBean.setVoucherTypeEditable(true);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = iFcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }
    /**
     * 数据回写
     */
    public void backData(TallyFcAccbook accBook, String businessId, String flowType) {
        List<InsertSqlBean> list = this.backDataGain(accBook, businessId, flowType);
        for (InsertSqlBean bean : list) {
            // 处理 核算管理 网络记账  单位申请付款  记账  页面提交 报错信息发现
            //注释原因：因为NIS_BILLHEAD（付款通知单）不是本领域的包，是payment 结算管理 领域的表，所以通过dubbo调用payment服务
//            tallyService.createSQLQueryFinacialColumn(bean.getInsertSql().toString(), bean.getParams());
            nisBillheadServiceImpl.createSQLQueryFinacialColumn(bean.getInsertSql().toString(), bean.getParams());
        }
    }
    /**
     * 回写 业务表数据 准备
     *
     * @param accBook
     */
    private List<InsertSqlBean> backDataGain(TallyFcAccbook accBook, String businessId, String flowType) {
//		FcBusRace busRace = busRaceService.get(accBook.getBusRaceId());//查询使用的记账规则
        FcBusRaceDTO fcBusRaceDTO = iFcBusRaceService.get(accBook.getBusRaceId());//查询使用的记账规则
        FcManualFinacialTypeDTO vo = tallyService.getTypeBysysCodeAndFlow(fcBusRaceDTO.getSysRaceCode(), flowType);
        if (EmptyUtils.isEmpty(vo)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_FINACIAL_TYPE_ISNULL, fcBusRaceDTO.getSysRaceCode());
        }
        List<FcManualFinacialColumnDTO> columnList = tallyService.listColumnVoBySysRaceCode(vo.getId());//已排序数据
        //拼装insert语句
        List<InsertSqlBean> updateSqlList = new ArrayList<InsertSqlBean>();
        StringBuilder updateSqlValue = new StringBuilder();
        String tableName = null;
        String businessValue = null;//关联字段
        Map<String, Object> params = new HashMap<String, Object>();
        Iterator<FcManualFinacialColumnDTO> columnIterator = columnList.iterator();
        while (columnIterator.hasNext()) {
            FcManualFinacialColumnDTO column = columnIterator.next();
            if (EmptyUtils.isEmpty(column.getWriteBackTable())) {
                continue;
            }
            if (!column.getWriteBackTable().equals(tableName)) {//表名不同
                if (columnIterator.hasNext()) {
                    StringBuilder updateStr = FinacialManualUtil.updateSql(tableName, updateSqlValue, businessValue, businessId);
                    params.put(businessValue, businessId);
                    if (!EmptyUtils.isEmpty(updateStr)) {
                        params.put(businessValue, businessId);
                        InsertSqlBean bena = new InsertSqlBean();
                        bena.setInsertSql(updateStr);
                        bena.setParams(params);
                        updateSqlList.add(bena);
                    }
                    updateSqlValue = new StringBuilder();
                    params = new HashMap<String, Object>();
                }
            }
            tableName = column.getWriteBackTable();
            businessValue = column.getBusinessId();
            //字符串拼接
            Object colVal = FinacialManualUtil.getColumnValue(accBook, column);
            StringBuilder setStr = new StringBuilder(column.getWriteBackColumn());
            setStr = FinacialManualUtil.StringAddSymbol(setStr, ":" + column.getWriteBackColumn(), "="); //column = :column
            updateSqlValue = FinacialManualUtil.StringAddSymbol(updateSqlValue, setStr.toString(), ",");
            params.put(column.getWriteBackColumn(), colVal);
            if (!columnIterator.hasNext()) {//最后一次循环
                StringBuilder updateStr = FinacialManualUtil.updateSql(tableName, updateSqlValue, businessValue, businessId);
                params.put(businessValue, businessId);
                if (!EmptyUtils.isEmpty(updateStr)) {
                    params.put(businessValue, businessId);
                    InsertSqlBean bena = new InsertSqlBean();
                    bena.setInsertSql(updateStr);
                    bena.setParams(params);
                    updateSqlList.add(bena);
                }
            }
        }
        return updateSqlList;
    }
    /**
     * 查询全部的 记账类型  分页
     */
    public DataTablesOutput<FcManualFinacialTypeDTO> getTypeDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        return tallyService.getDataTablesAllType(dataTablesInput, user);
    }
    /**
     * 根据typeId查询全部的回写对象
     *
     * @param id
     */
    public List<FcManualFinacialColumnDTO> getColumnByTypeId(String id) {
        if (EmptyUtils.isEmpty(id)) {
            return new ArrayList<FcManualFinacialColumnDTO>();
        }
        return tallyService.listColumnVoBySysRaceCode(id);
    }
    /**
     * 根据typeId查询type
     *
     * @param id
     */
    public FcManualFinacialTypeDTO getType(String id) {
        return tallyService.getType(id);
    }
    /**
     * 保存手工帐规则
     */
    public void saveType(FcManualFinacialTypeDTO type) {
        tallyService.saveOrUpdateManualFinacial(type);
        tallyService.deleteColumnByTypeId(type.getId());
        List<FcManualFinacialColumnDTO> list = new ArrayList<>();
        for (FcManualFinacialColumnDTO col : type.getColList()) {
            if (EmptyUtils.isNotEmpty(col.getWriteBackTable())) {
                FcManualFinacialColumnDTO ffc = new FcManualFinacialColumnDTO();
                BeanUtils.copyNotNullProperties(col, ffc);
                ffc.setTypeId(type.getId());
                list.add(ffc);
            }
        }
        if (!CollectionUtils.isEmpty(list)){
            tallyService.batchSaveManualFinacialColumn(list);
        }
    }
    public void deleteType(String id) {
        tallyService.deleteTypeById(id);
        tallyService.deleteColumnByTypeId(id);
    }
    /**
     * 记账前校验
     *
     * @param opType
     * @param menuType
     * @param key
     */
    public void validBefore(String opType, String menuType, String key) {
        try {
            String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
            Class<?> class1 = Class.forName(clazz);
            Method findMethod = ReflectionUtils.findMethod(class1, FinacialManualConstant.VALID_BEFORE, String.class, String.class);
            ReflectionUtils.invokeMethod(findMethod, class1.newInstance(), opType, key);
        } catch (Exception e) {
            ParameterException targetEx = (ParameterException) e;
            throw new ParameterException(targetEx.getIMessageEnum(), targetEx.getParams());
        }
    }
    /**
     * <br>
     * <b>功能：</b> 检查是否设置账务省市信息 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/9/22 0022 <br>
     */
    public void setFcAccBookExtInfo(FcAccbookVoucherDTO fcAccbook, String key) {
        FcAccbookDTO accbook = fcAccbook.getAccbook();
        Integer busOriType = accbook.getBusOriType();//单位申请付款
        if (FinaBookConstant.BusOriType.NB_UNIT_COMMON_PAYMENT.get().equals(busOriType)) {
            NisBillheadDTO nisBillhead = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
            //检查账务明细的省市是否为空
            FcAccbookExtDTO bookExt = fcAccbook.getBookExt();
            if (null == bookExt) {
                bookExt = new FcAccbookExtDTO();
                bookExt.setProv(nisBillhead.getProv());
                bookExt.setCity(nisBillhead.getCity());
                bookExt.setExternalProv(nisBillhead.getProv());
                bookExt.setExternalCity(nisBillhead.getCity());
                bookExt.setExternalAcc(nisBillhead.getPayeeAcc());//外单位账号
                bookExt.setExternalCorp(nisBillhead.getPayeeAccName());//外单位名
                bookExt.setExternalBank(nisBillhead.getPayeeBank());
                bookExt.setPaysysbankcode(nisBillhead.getPayeeBankCode());//收款方联行号
                //bookExt.setPayerAcc("");//FIXME 付款账号
            } else {
                if (EmptyUtils.isEmpty(bookExt.getProv()) || "".equals(bookExt.getProv()) || " ".equals(bookExt.getProv())) {
                    bookExt.setProv(nisBillhead.getProv());
                    bookExt.setExternalProv(nisBillhead.getProv());
                }
                if (EmptyUtils.isEmpty(bookExt.getCity()) || "".equals(bookExt.getCity()) || " ".equals(bookExt.getCity())) {
                    bookExt.setCity(nisBillhead.getCity());
                    bookExt.setExternalCity(nisBillhead.getCity());
                }
            }
        }
    }
    /**
     * 功能介绍 : 记账提交凭证对象
     *
     * @param fcAccbook 记账凭证传输对象
     * @param key       记账业务表key
     * @param user      当前用户对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user, String downAcc, String menuType, String opType) {
        if (!EmptyUtils.isEmpty(downAcc)) {//付款办理修改下拨账号
            nisBillheadServiceImpl.updateNisBillheadDownAccId(key, downAcc);
        }
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        setFcAccBookExtInfo(fcAccbook, key);
        this.createFcAcc(fcAccbook.getBookDtls(), user);
        boolean autoCheck = this.isAutoCheck();//判断是否自动复核
        if (autoCheck) {
            setCheckVoucherInfo(menuType, voucher, user, key);
            voucher.setAutoCheck(true);
        }
        //获取付款账号
        voucher = iFcAccbookService.submitVoucher(voucher);
        String businessId = "";
        TallyFcAccbook fa = new TallyFcAccbook();
        BeanUtils.copyNotNullProperties(voucher.getTarget().get(0).getAccbook(), fa);
//		FcBusRace busRace = busRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        FcBusRaceDTO fcBusRaceDTO = iFcBusRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        if (autoCheck) {
            this.backData(fa, key, "3");
        } else {
            this.backData(fa, key, "1");
        }
        if ("2005".equals(fcBusRaceDTO.getSysRaceCode()) || "2003".equals(fcBusRaceDTO.getSysRaceCode())) {
            //单位申请付款
            NisBillheadDTO nisBillheadDTO = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
            businessId = nisBillheadDTO.getId();
        } else {
            //头寸调拨
            NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
            businessId = nisDTO.getId();
        }
        completeEditNotificationInfo(voucher, user, menuType, businessId);
        completeCheckNotificationInfo(voucher, user, menuType, businessId);
        if ("2005".equals(fcBusRaceDTO.getSysRaceCode()) || "2003".equals(fcBusRaceDTO.getSysRaceCode())) {
            //单位申请付款
            iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT, "记账提交", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, businessId, user.getId());
        } else {
            //头寸调拨
            iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_SUBMIT, "记账提交", NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
            notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, businessId, user.getId());
        }
        if (!autoCheck) {
            sendCheckNotificationInfo(voucher, user, menuType, businessId);
        }
    }
    /**
     * 功能介绍 : 记账暂存凭证对象
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user) {
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        setFcAccBookExtInfo(fcAccbook, key);
        this.createFcAcc(fcAccbook.getBookDtls(), user);
        voucher = iFcAccbookService.saveVoucher(voucher);
        TallyFcAccbook fa = new TallyFcAccbook();
        BeanUtils.copyNotNullProperties(voucher.getTarget().get(0).getAccbook(), fa);
        this.backData(fa, key, "2");
//		FcBusRace busRace = busRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        FcBusRaceDTO fcBusRaceDTO = iFcBusRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        if ("2005".equals(fcBusRaceDTO.getSysRaceCode()) || "2003".equals(fcBusRaceDTO.getSysRaceCode())) {
            //单位申请付款
            //NisBillheadDTO nisBillheadDTO = iNisBillheadService.getNisBillheadByBillNo(key);
            iNisMessageService.saveMessage(key, NisMessageConstant.MESSAGE_TYPE_TALLY_TEMP, "记账暂存", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, key, user.getId());
        } else {
            //头寸调拨
            //NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
            iNisMessageService.saveMessage(key, NisMessageConstant.MESSAGE_TYPE_TALLY_TEMP, "记账暂存", NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
            notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, key, user.getId());
        }
    }
    /**
     * 功能介绍 : 记账复核凭证对象
     */
    public void pass(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user, String downAcc, String menuType) {
        if (!EmptyUtils.isEmpty(downAcc)) {//付款办理修改下拨账号
            nisBillheadServiceImpl.updateNisBillheadDownAccId(key, downAcc);
        }
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        voucher.setUser(user);
        setCheckVoucherInfo(menuType, voucher, user, key); //里面有设置是否产生指令逻辑
        //这里有相关数据保存
        VoucherWrapperDTO voucherWrapperDTO = iFcAccbookService.checkVoucher(voucher); //调用核算 有指令操作

        FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
        TallyFcAccbook fa = new TallyFcAccbook();
        BeanUtils.copyNotNullProperties(accbook, fa);
        this.backData(fa, key, "3");
//		FcBusRace busRace = busRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        FcBusRaceDTO busRace = iFcBusRaceService.get(fa.getBusRaceId());//查询使用的记账规则
        String businessKey = "";
        if ("2005".equals(busRace.getSysRaceCode()) || "2003".equals(busRace.getSysRaceCode())) {
            //单位申请付款
            NisBillheadDTO nisBillheadDTO = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
            businessKey = nisBillheadDTO.getId();
            iNisMessageService.saveMessage(businessKey, NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_PASS, "记账复核通过", NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
        } else {
            //头寸调拨
            NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
            businessKey = nisDTO.getId();
            iNisMessageService.saveMessage(businessKey, NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_PASS, "记账复核通过", NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
        }
        completeCheckNotificationInfo(voucher, user, menuType, businessKey);
        completeEditNotificationInfo(voucher, user, menuType, businessKey);
    }
    /**
     * 功能介绍 : 设置复核相关参数。<br>
     * <b>日期：</b>2018-05-16<br>
     *
     * @param key
     * @version 1.0
     * @author liyh
     */
    public void setCheckVoucherInfo(String menuType, VoucherWrapperDTO wrapperDTO, ICurrentUser user, String key) {
        try {
            String clazz = FinacialManualConstant.ClassMap.get(menuType.substring(0, 1));
            if (!EmptyUtils.isEmpty(clazz)) {
                Class<?> class1 = Class.forName(clazz);
                Method findMethod = ReflectionUtils.findMethod(class1, FinacialManualConstant.SET_CHECK_VOUCHER, VoucherWrapperDTO.class, ICurrentUser.class, String.class);
                ReflectionUtils.invokeMethod(findMethod, class1.newInstance(), wrapperDTO, user, key);
            }
        } catch (Exception e) {
            ParameterException targetEx = (ParameterException) e;
            throw new ParameterException(targetEx.getIMessageEnum(), targetEx.getParams());
        }
    }
    /**
     * 功能介绍 : 记账复核凭证对象
     */
    public void payPass(String orderNo, String downAcc, String billNo, ICurrentUser user, String menuType) {
        if (EmptyUtils.isEmpty(orderNo)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
        }
        FcAccbookVoucherDTO dto = transVoucher(orderNo);
        pass(dto, billNo, user, downAcc, menuType);
    }
    /**
     * 功能介绍 : 记账复核凭证对象
     */
    public void back(FcAccbookVoucherDTO fcAccbook, String key, ICurrentUser user, String menuType) {
        String opType = fcAccbook.getOpType().name();
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        TallyFcAccbook fa = new TallyFcAccbook();
//		FcBusRace busRace = null;
        FcBusRaceDTO busRace = null;
        if ("MODIFY".equals(opType)) {//改账打回
            VoucherWrapperDTO voucherWrapperDTO = iFcAccbookService.deleteVoucher(voucher);
            FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
            BeanUtils.copyNotNullProperties(accbook, fa);
//			busRace = busRaceService.get(fa.getBusRaceId(),true);//查询使用的记账规则
            busRace = iFcBusRaceService.get(fa.getBusRaceId(), true);//查询使用的记账规则
            this.backData(fa, key, "4");
            if ("2005".equals(busRace.getSysRaceCode()) || "2003".equals(busRace.getSysRaceCode())) {
                //单位申请付款
                NisBillheadDTO nisBillheadDTO = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
                iNisMessageService.saveMessage(nisBillheadDTO.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHANGE_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(nisBillheadDTO.getPayMoney(), "#,##0.00"));
                completeCheckNotificationInfo(voucher, user, menuType, nisBillheadDTO.getId());
                completeEditNotificationInfo(voucher, user, menuType, nisBillheadDTO.getId());
                notificationService.sendToUserByResPermission(nisBillheadDTO.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, map, nisBillheadDTO.getCorpId());
            } else {//头寸调拨
                NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
                iNisMessageService.saveMessage(nisDTO.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHANGE_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(nisDTO.getMoney(), "#,##0.00"));
                completeCheckNotificationInfo(voucher, user, menuType, nisDTO.getId());
                completeEditNotificationInfo(voucher, user, menuType, nisDTO.getId());
                notificationService.sendToUserByResPermission(nisDTO.getId(), ResourceConstant.FCL_NISALLOCATIONREQ_TALLY_APPLY, NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, map, nisDTO.getCorpId());
            }
        } else if ("CHECK".equals(opType)) {//复核打回
            VoucherWrapperDTO voucherWrapperDTO = iFcAccbookService.backVoucher(voucher);
            FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
            BeanUtils.copyNotNullProperties(accbook, fa);
//			busRace = busRaceService.get(fa.getBusRaceId(),true);//查询使用的记账规则
            busRace = iFcBusRaceService.get(fa.getBusRaceId(), true);//查询使用的记账规则
            this.backData(fa, key, "5");
            String businessId = "";
            if ("2005".equals(busRace.getSysRaceCode()) || "2003".equals(busRace.getSysRaceCode())) {
                //单位申请付款
                NisBillheadDTO nisBillheadDTO = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
                businessId = nisBillheadDTO.getId();
                iNisMessageService.saveMessage(businessId, NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            } else {
                //头寸调拨
                NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
                businessId = nisDTO.getId();
                iNisMessageService.saveMessage(nisDTO.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_CHECK_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
            }
            completeCheckNotificationInfo(voucher, user, menuType, businessId);
            sendBackNotificationInfo(voucher, menuType, businessId);
        } else if ("BACK".equals(opType)) {//记账打回
//			busRace = busRaceService.get(voucher.getTarget().get(0).getAccbook().getBusRaceId(),true);//查询使用的记账规则
            busRace = iFcBusRaceService.get(voucher.getTarget().get(0).getAccbook().getBusRaceId(), true);//查询使用的记账规则
            if ("5".equals(menuType)) {//申请付款
                NisBillheadDTO nbHead = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
                nbHead.setOrderNo(null);
                nbHead.setTallySign(null);
                nbHead.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
                nisBillheadServiceImpl.updateNisBillheadByDto(nbHead, user);
                finacialTallyRemindService.deleteByBusId(nbHead.getId());
                notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_TALLY, nbHead.getId(), user.getId());
            } else {
                NisAllocationReqDTO nar = inisAllocationReqService.getNisAllocationReqByBillNo(key);
                nar.setOrderNo(null);
                nar.setTallySign(null);
                nar.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
                inisAllocationReqService.updateNisAllocationReq(nar, user);
                finacialTallyRemindService.deleteByBusId(nar.getId());
                notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_TALLY, nar.getId(), user.getId());
            }
            if ("2005".equals(busRace.getSysRaceCode()) || "2003".equals(busRace.getSysRaceCode())) {
                //单位申请付款
                NisBillheadDTO nisBillheadDTO = nisBillheadServiceImpl.getNisBillheadByBillNo(key);
                iNisMessageService.saveMessage(nisBillheadDTO.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_PAY, user.getLoginName(), user.getUsername());
            } else {
                //头寸调拨
                NisAllocationReqDTO nisDTO = inisAllocationReqService.getNisAllocationReqByBillNo(key);
                iNisMessageService.saveMessage(nisDTO.getId(), NisMessageConstant.MESSAGE_TYPE_TALLY_BACK, fcAccbook.getBookExt().getInvalidReason(), NisMessageConstant.BUS_TYPE_ALLOCATION, user.getLoginName(), user.getUsername());
            }
        }
    }
    /**
     * 内部帐号开户
     */
    private void createFcAcc(List<FcAccbookDtlDTO> bookDtls, ICurrentUser user) {
        if (!EmptyUtils.isEmptyList(bookDtls)) {
            for (FcAccbookDtlDTO dtl : bookDtls) {
                com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtl.getAccDTO();
                if (!EmptyUtils.isEmpty(accDTO)) {
                    accDTO.setAutoCheck(true);
                    // todo:数据转换
                    FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);
                    iInnerAccountService.saveOrUpdate(fcAccDTO, user);
                    dtl.setAccDTO(null);
                }
            }
        }
    }
    // 凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);// 设置用户
        voucher.setAutoAccounter(false);//设置是否自动记账  否
        return voucher;
    }
    public List<FcAccDTO> queryBySomeCondPgd(String condition) {
        return iFcAccService.queryBySomeCondPgd(condition);
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(iConfigService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * 判断是否先记账后付款
     */
    public boolean paySequence() {
        boolean isAutoCheck = CmsConstant.IS_FALSE.equals(iConfigService.getConfig("nis", "payAccount").getValue());
        return isAutoCheck;
    }
    /**
     * 判断是否先记账后付款
     */
    public boolean paySequenceAllot() {
        boolean isAutoCheck = CmsConstant.IS_FALSE.equals(iConfigService.getConfig("allot", "payAccount").getValue());
        return isAutoCheck;
    }
    /**
     * 判断是否允许中心修改下拨账号
     */
    public boolean isChangeDownAcc() {
        boolean isChangeDownAcc = CmsConstant.IS_TRUE.equals(iConfigService.getConfig("nis", "allowEditDownacc").getValue());
        return isChangeDownAcc;
    }
    /**
     * 判断是否转付
     */
    public boolean chenckVoucherType(String voucherType) {
        boolean chenckVoucherType = false;
        FcVoucherTypeDTO voucher = iFcVoucherTypeService.getFcVoucherTypeById(voucherType);
        if (PayType.PAY_TYPE_02.getValue().equals(voucher.getSysVoucherCode())) {
            chenckVoucherType = true;
        }
        return chenckVoucherType;
    }
    /**
     * 判断是否内转
     */
    public boolean chenckVoucherTypeInner(String voucherType) {
        boolean chenckVoucherType = false;
        FcVoucherTypeDTO voucher = iFcVoucherTypeService.getFcVoucherTypeById(voucherType);
        if (PayType.PAY_TYPE_05.getValue().equals(voucher.getSysVoucherCode())) {
            chenckVoucherType = true;
        }
        return chenckVoucherType;
    }
    public FcAccbookVoucherDTO transVoucher(String orderNo) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
		/*FcAccbookVO books = fcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
		FcAccbookDTO accbook = new FcAccbookDTO();
    	if(!EmptyUtils.isEmpty(books)){
    		BeanUtils.copyNotNullProperties(books, accbook);
    		accbook.setRaceName(books.getRaceName());
    		entity.setAccbook(accbook);
    	}*/
        FcAccbookDTO accbook = iFcAccbookService.getBookVOByOrderNo(orderNo, FinacialConstant.BOOLEAN_FALSE);
        entity.setAccbook(accbook);
    	/*List<FcAccbookDtlVO> bookDtls = fcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
		f (null != bookDtls && !bookDtls.isEmpty()) {
			List<FcAccbookDtlDTO> nbookDtls = BeanUtils.copyListProperties(bookDtls, FcAccbookDtlDTO.class);
			entity.setBookDtls(nbookDtls);
		}*/
        List<FcAccbookDtlDTO> fcAccbookDtlDTOS = iFcAccbookDtlService.getPageDtlsByBookId(accbook.getId());
        entity.setBookDtls(fcAccbookDtlDTOS);
    	/*FcAccbookExtVO bookExt = fcAccbookExtService.getBookExtVOByBookId(accbook.getId());
		if(!EmptyUtils.isEmpty(bookExt)){
    		FcAccbookExtDTO ext = new FcAccbookExtDTO();
    		BeanUtils.copyNotNullProperties(bookExt, ext);
    		entity.setBookExt(ext);
		}*/
        FcAccbookExtDTO fcAccbookExtDTO = iFcAccbookExtService.getBookExtVOByBookId(accbook.getId());
        entity.setBookExt(fcAccbookExtDTO);
        return entity;
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            if ("5".equals(menuType)) {
                notificationService.sendIgnoreMySelf(businessId, accbook.getNetId(),
                        NotificationConstant.FINACIAL.CORPPAYMENT_CHECK, notifyVO, user.getId());
            } else {
                notificationService.sendIgnoreMySelf(businessId, accbook.getNetId(),
                        NotificationConstant.FINACIAL.MONEYTRANSFERS_CHECK, notifyVO, user.getId());
            }
        }
    }
    /**
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher, String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            if ("5".equals(menuType)) {
                notificationService.sendToUser(businessId,
                        accbook.getOperatorCode(), NotificationConstant.FINACIAL.CORPPAYMENT_EDIT, notifyVO);
            } else {
                notificationService.sendToUser(businessId,
                        accbook.getOperatorCode(), NotificationConstant.FINACIAL.MONEYTRANSFERS_EDIT, notifyVO);
            }
        }
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            if ("5".equals(menuType)) {
                notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_EDIT, businessId, user.getId());
            } else {
                notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_EDIT, businessId, user.getId());
            }
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user, String menuType, String businessId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            if ("5".equals(menuType)) {
                notificationService.complete(NotificationConstant.FINACIAL.CORPPAYMENT_CHECK, businessId, user.getId());
            } else {
                notificationService.complete(NotificationConstant.FINACIAL.MONEYTRANSFERS_CHECK, businessId, user.getId());
            }
        }
    }
}
