package com.bytter.ras.service;

import com.bytter.ras.dao.RasReceivedDao;
import com.bytter.ras.entity.RasBankCardBinding;
import com.bytter.ras.entity.RasOtherCardBinding;
import com.bytter.ras.entity.RasPosBinding;
import com.bytter.ras.entity.RasReceivedDtl;
import com.bytter.ras.utils.RasUtils;
import com.bytter.ras.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.ras.api.constant.RasConstant;
import com.bytter.ras.api.enums.RasMessageEnum;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.poi.excel.entity.params.ImportCustomRules;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.ICustomVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuwj
 * @projectName T2
 * @description: 稽核实收数据Service
 * @date 2019/5/15 13:39
 */
@Service("rasReceivedService")
@Transactional
public class RasReceivedService extends BaseService<RasReceivedDtl, String>
        implements ICustomVerifyHandler<RasReceivedDtlVO> {

    @Autowired
    public RasReceivedDao rasReceivedDao;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired
    private RasShopGrantService rasShopGrantService;
    @Autowired
    private RasLogService rasLogService;
    @Autowired
    private RasBankCardBindingService rasBankCardBindingService;
    @Autowired
    private RasPosBindingService rasPosBindingService;
    @Autowired
    private RasPosBorrowService rasPosBorrowService;
    @Autowired
    private RasOtherCardBindingService rasOtherCardBindingService;
    @Autowired
    private RasImportConfService rasImportConfService;
    @Autowired
    private RasShopService rasShopService;
    @Autowired(required=false)
    private IDictService dictService;

    @Override
    public BaseDao<RasReceivedDtl, String> getEntityDao() {
        return rasReceivedDao;
    }

    /**
     * 实收数据查询列表加载
     *
     * @param dataTablesInput
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String grantType, String resource) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(resource, dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(grantType, dataTablesInput.getCurrentUser());
        return rasReceivedDao.getDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }


    /**
     * 根据ids批量修改实收表数据状态
     *
     * @param ids       ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废0待取认）
     * @param statusOld 根据对应旧状态处理状态值修改
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, Integer statusNew, Integer statusOld, ICurrentUser userVo) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if (statusOld != null) {
            params.put("statusOld", statusOld);
        }
        int num = rasReceivedDao.batchUpdate(params);
        return num;
    }

    /**
     * 根据稽核ids批量修改实收表数据状态
     *
     * @param aids      ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废0待取认）
     * @param statusOld 根据对应旧状态处理状态值修改
     * @return 修改成功的笔数
     */
    public int batchUpdateByAid(String[] aids, Integer statusNew, Integer statusOld, ICurrentUser userVo) {
        if (aids == null || aids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("aids", aids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if (statusOld != null) {
            params.put("statusOld", statusOld);
        }
        int num = rasReceivedDao.batchUpdate(params);
        return num;
    }

    /**
     * 实收数据自动稽核状态操作
     *
     * @param params
     * @return
     */
    public int autoOpAuditStatusControl(Map<String, Object> params) {
        params.put(RasConstant.RAS_AUTOTASK_OPKEY, "1");
        return rasReceivedDao.batchUpdate(params);
    }


    /**
     * 根据参数获取稽核数据
     *
     * @param params
     * @return
     */
    public List<RasReceivedDtl> getRasReceivedDtlByParams(Map<String, Object> params) {
        return rasReceivedDao.getRasReceivedDtlByParams(params);
    }


    /**
     * 根据参数获取稽核明细汇总数据
     *
     * @param params
     * @return
     */
    public List<RasReceivedDtl> getRasReceivedDtlSumByParams(Map<String, Object> params) {
        return rasReceivedDao.getRasReceivedDtlSumByParams(params);
    }

    /**
     * 根据ids批量修改实收表数据状态
     *
     * @param ids       ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废0待取认）
     * @param statusOld 根据对应旧状态处理状态值修改
     * @param shopId    门店id
     * @param payType   缴款方式
     * @param claimType 认领标记
     * @return 修改成功的笔数
     */
    public int batchUpdate(String[] ids, Integer statusNew, Integer statusOld, ICurrentUser userVo, String shopId, String payType, Integer claimType) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        if (statusOld != null) {
            params.put("statusOld", statusOld);
        }
        params.put("shopId", shopId);
        params.put("payType", payType);
        params.put("claimType", claimType);
        int num = rasReceivedDao.batchUpdate(params);
        return num;
    }

    /**
     * 根据ids批量修改实收表数据状态
     *
     * @param ids       ID
     * @param statusNew 修改为对的处理状态值（状态:1待稽核2稽核中3已稽核4差异稽核5稽核完成-1稽核打回-2作废0待取认）
     * @param shopId    门店id
     * @param payType   缴款方式
     * @param claimType 认领标记
     * @return 修改成功的笔数
     */
    public int batchUpdateNoCheckStatus(String[] ids, Integer statusNew, ICurrentUser userVo, String shopId, String payType, Integer claimType) {
        if (ids == null || ids.length == 0 || statusNew == null) {
            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_VALISNOTNULL);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);
        if (userVo != null) {
            params.put("updateBy", userVo.getLoginName());
            params.put("updateName", userVo.getUsername());
        }
        params.put("updateDate", new Date());
        params.put("statusNew", statusNew);
        params.put("shopId", shopId);
        params.put("payType", payType);
        params.put("claimType", claimType);
        int num = rasReceivedDao.batchUpdateNoCheckStatus(params);
        return num;
    }

    /**
     * 实收数据认领查询列表加载
     *
     * @param dataTablesInput
     */
    public DataTablesOutput getClaimListDataTablesUnion(DataTablesInput dataTablesInput) {
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP, dataTablesInput.getCurrentUserId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_AUDIT, dataTablesInput.getCurrentUser());
        return rasReceivedDao.getClaimListDataTablesUnion(dataTablesInput, corpAuthList, shopAuthList);
    }

    /**
     * 批量剔除
     *
     * @param rids   实收明细表ids
     * @param userVo 操作用户
     */
    public boolean exclude(String rids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(rids)) {
            String[] ridArray = rids.split(",");
            RasReceivedDtl rasReceivedDtl = null;
            for (int i = 0; i < ridArray.length; i++) {
                rasReceivedDtl = rasReceivedDao.get(ridArray[i]);
                if (rasReceivedDtl != null) {
                    //判断是否是待稽核、待确认、稽核打回的数据
                    if (RasConstant.RAS_AUDIT_STATUS_1.equals(rasReceivedDtl.getStatus())
                            || RasConstant.RAS_AUDIT_STATUS_0.equals(rasReceivedDtl.getStatus())
                            || RasConstant.RAS_AUDIT_STATUS_RETURN_1.equals(rasReceivedDtl.getStatus())) {
                        String[] rid = {rasReceivedDtl.getId()};
                        //更新实收数据明细表状态为-2已作废
                        int bindingNum = batchUpdate(rid, RasConstant.RAS_AUDIT_STATUS_RETURN_2, rasReceivedDtl.getStatus(), userVo);
                        if (bindingNum == 0) {
                            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //添加日志
                        rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_DELETE, "营收稽核实收数据剔除", userVo);
                    } else {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 取消认领
     *
     * @param rids   实收明细表ids
     * @param userVo 操作用户
     */
    public boolean cancelClaim(String rids, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(rids)) {
            String[] ridArray = rids.split(",");
            RasReceivedDtl rasReceivedDtl = null;
            for (int i = 0; i < ridArray.length; i++) {
                rasReceivedDtl = rasReceivedDao.get(ridArray[i]);
                if (rasReceivedDtl != null) {
                    //判断是否是已认领的,已认领的数据才可取消认领
                    if (RasConstant.RAS_CLAIM_TYPE_1.equals(rasReceivedDtl.getClaimType())) {
                        String[] rid = {rasReceivedDtl.getId()};
                        //更新实收数据明细表状态为待确认状态,门店和缴款方式清空,认领标识为未认领
                        int bindingNum = batchUpdate(rid, RasConstant.RAS_AUDIT_STATUS_0, rasReceivedDtl.getStatus(), userVo,
                                null, null, RasConstant.RAS_CLAIM_TYPE_0);
                        if (bindingNum == 0) {
                            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //添加日志
                        rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_CLAIM_CANCELCLAIM, "营收稽核实收数据取消认领", userVo);
                    } else {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 实收数据认领批量认领
     *
     * @param rids      实收明细表ids
     * @param payType   缴款方式
     * @param rasShopId 门店id
     * @param userVo    操作用户
     */
    public boolean claim(String rids, String payType, String rasShopId, ICurrentUser userVo) {
        if (StringUtils.isNotEmpty(rids)) {
            String[] ridArray = rids.split(",");
            RasReceivedDtl rasReceivedDtl = null;
            Integer claimType = RasConstant.RAS_CLAIM_TYPE_0;
            Integer status = RasConstant.RAS_AUDIT_STATUS_0;
            for (int i = 0; i < ridArray.length; i++) {
                rasReceivedDtl = rasReceivedDao.get(ridArray[i]);
                if (rasReceivedDtl != null) {
                    //判断是否是待确认或者待稽核
                    if (RasConstant.RAS_AUDIT_STATUS_0.equals(rasReceivedDtl.getStatus())
                            || RasConstant.RAS_AUDIT_STATUS_1.equals(rasReceivedDtl.getStatus())) {
                        String[] rid = {rasReceivedDtl.getId()};
                        if (StringUtils.isNotEmpty(rasShopId.trim())) {
                            rasReceivedDtl.setShopId(rasShopId.trim());
                        }
                        if (StringUtils.isNotEmpty(payType.trim())) {
                            rasReceivedDtl.setPayType(payType.trim());
                        }
                        //缴款方式、门店id都不为空，则认领标识为已认领状态,稽核状态为待稽核
                        if (StringUtils.isNotEmpty(rasReceivedDtl.getShopId()) && StringUtils.isNotEmpty(rasReceivedDtl.getPayType())) {
                            claimType = RasConstant.RAS_CLAIM_TYPE_1;
                            status = RasConstant.RAS_AUDIT_STATUS_1;
                        }
                        //更新实收数据明细表
                        int bindingNum = batchUpdateNoCheckStatus(rid, status, userVo,
                                rasReceivedDtl.getShopId(), rasReceivedDtl.getPayType(), claimType);
                        if (bindingNum == 0) {
                            throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                        }
                        //添加日志
                        if (claimType.equals(RasConstant.RAS_CLAIM_TYPE_1)) {
                            //已认领日志
                            rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_CLAIM, "营收稽核实收数据确认认领", userVo);
                        } else {
                            //修改日志
                            rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_UPDATE, "营收稽核实收数据认领修改", userVo);
                        }
                    } else {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 实收数据认领自动匹配认领
     *
     * @param rids   实收明细表ids
     * @param userVo 操作用户
     */
    public boolean autoClaim(String rids, ICurrentUser userVo) {
        boolean result = false;
        if (StringUtils.isNotEmpty(rids)) {
            String[] ridArray = rids.split(",");
            RasReceivedDtl rasReceivedDtl = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //修改成功数据条数
            int successNum = 0;
            for (int i = 0; i < ridArray.length; i++) {
                rasReceivedDtl = rasReceivedDao.get(ridArray[i]);
                if (rasReceivedDtl != null) {
                    //判断是否是待确认或者待稽核
                    if (RasConstant.RAS_AUDIT_STATUS_0.equals(rasReceivedDtl.getStatus())
                            || RasConstant.RAS_AUDIT_STATUS_1.equals(rasReceivedDtl.getStatus())) {
                        String[] rid = {rasReceivedDtl.getId()};
                        //缴款方式
                        String payType = rasReceivedDtl.getPayType();
                        //门店id
                        String shopId = rasReceivedDtl.getShopId();

                        /*
                         * 根据银行账号匹配
                         */
                        //取收款账号
                        if (StringUtils.isNotEmpty(rasReceivedDtl.getPayeeAcc()) || StringUtils.isNotEmpty(rasReceivedDtl.getRmk())) {
                            //判断账号是否有绑定的银行账号信息
                            if (StringUtils.isNotEmpty(rasReceivedDtl.getPayeeAcc())) {
                                List<RasBankCardBinding> bankCarList = rasBankCardBindingService.getRasBankCardBindingByParams(null, rasReceivedDtl.getPayeeAcc(), null);
                                if (bankCarList != null && !bankCarList.isEmpty()) {
                                    RasBankCardBinding bankCar = bankCarList.get(0);
                                    //如果有数据其缴款方式为“现金”
                                    if (StringUtils.isEmpty(payType)) {
                                        payType = RasConstant.RAS_PAYTYPE_001;
                                    }
                                    //设置门店
                                    if (StringUtils.isEmpty(shopId)) {
                                        shopId = bankCar.getShopId();
                                    }
                                }
                            }
                            //取备注
                            if (StringUtils.isNotEmpty(rasReceivedDtl.getRmk())) {
                                List<RasBankCardBinding> bankCarList2 = rasBankCardBindingService.getRasBankCardBindingByParams(null, rasReceivedDtl.getRmk(), null);
                                if (bankCarList2 != null && !bankCarList2.isEmpty()) {
                                    RasBankCardBinding bankCar = bankCarList2.get(0);
                                    //如果有数据其缴款方式为“现金”
                                    if (StringUtils.isEmpty(payType)) {
                                        payType = RasConstant.RAS_PAYTYPE_001;
                                    }
                                    //设置门店
                                    if (StringUtils.isEmpty(shopId)) {
                                        shopId = bankCar.getShopId();
                                    }
                                }
                            }
                        }
                        /*
                         * 根据银联POS机匹配
                         */
                        //实收数据中有没有同步/导入时分秒（交易时间）
                        boolean isTime = true;
                        if (StringUtils.isNotEmpty(rasReceivedDtl.getTransDate())) {
                            //没有时分秒的
                            if (rasReceivedDtl.getTransDate().toString().contains("00:00:00.0")) {
                                isTime = false;
                            }
                        }
                        //其缴款方式为“银联POS机”且交易时间有时分秒的且门店id为空,搜索收款账号或备注中是否有POS终端号，并根据当前已启用的门店绑定终端关系识别其门店、单位
                        if (RasConstant.RAS_PAYTYPE_004.equals(rasReceivedDtl.getPayType()) && isTime && StringUtils.isEmpty(shopId)) {
                            RasPosBinding rasPosBinding = null;
                            //判断终端号是否存在
                            if (StringUtils.isNotEmpty(rasReceivedDtl.getRmk())) {
                                rasPosBinding = rasPosBindingService.getRasPosBindingByParams(rasReceivedDtl.getRmk(), RasConstant.RAS_VALID_SIGN_1);
                            }
                            if (rasPosBinding == null && StringUtils.isNotEmpty(rasReceivedDtl.getPayeeAcc())) {
                                rasPosBinding = rasPosBindingService.getRasPosBindingByParams(rasReceivedDtl.getPayeeAcc(), RasConstant.RAS_VALID_SIGN_1);
                            }
                            if (rasPosBinding != null) {
                                //判断状态是否是正常或已借出状态
                                if (RasConstant.RAS_POS_BD_STATUS_01.equals(rasPosBinding.getStatus()) || RasConstant.RAS_POS_BD_STATUS_03.equals(rasPosBinding.getStatus())) {
                                    boolean isMatch = false;
                                    //根据pos机绑定id查询pos机借入记录
                                    List<RasPosBorrowVO> posBorrowList = rasPosBorrowService.getRasPosBorrowByBdId(rasPosBinding.getId(), new String[]{RasConstant.RAS_POS_BR_STATUS_02, RasConstant.RAS_POS_BR_STATUS_03});
                                    if (posBorrowList != null && !posBorrowList.isEmpty()) {
                                        //交易时间格式化
                                        String transDate = format.format(rasReceivedDtl.getTransDate());
                                        for (RasPosBorrowVO posBorrow : posBorrowList) {
                                            //绑定日期
                                            String bdDate = posBorrow.getBdDate() == null ? null : format.format(posBorrow.getBdDate());
                                            //解绑日期
                                            String ubdDate = posBorrow.getUbdDate() == null ? null : format.format(posBorrow.getUbdDate());
                                            if (bdDate != null) {
                                                //是否是借用状态,判断交易日期是否大于等于绑定日期
                                                if (RasConstant.RAS_POS_BR_STATUS_02.equals(posBorrow.getStatus()) &&
                                                        (transDate.compareTo(bdDate) == 0 || transDate.compareTo(bdDate) > 0)) {
                                                    //借用且状态在借用时间内则把所属门店赋值
                                                    shopId = posBorrow.getShopId();
                                                    isMatch = true;
                                                    //是否是已归还状态,判断交易日期是否大于等于绑定日期且小于解绑日期
                                                } else if (RasConstant.RAS_POS_BR_STATUS_03.equals(posBorrow.getStatus())
                                                        && (transDate.compareTo(bdDate) == 0 || transDate.compareTo(bdDate) > 0)
                                                        && ubdDate != null && transDate.compareTo(ubdDate) < 0) {
                                                    //所属门店赋值
                                                    shopId = posBorrow.getShopId();
                                                    isMatch = true;
                                                }
                                            }
                                        }
                                    }
                                    //如果无匹配到则取pos机绑定表
                                    if (!isMatch) {
                                        //所属门店赋值
                                        shopId = rasPosBinding.getShopId();
                                    }
                                }
                            }
                        }

                        /*
                         * 根据第三方支付账号匹配
                         */
                        if (StringUtils.isNotEmpty(rasReceivedDtl.getPayeeAcc()) && StringUtils.isEmpty(shopId)) {
                            //判断账号是否有绑定的第三方支付账号信息
                            List<RasOtherCardBinding> otherCardList = null;
                            Map<String, Object> params = new HashMap<>();
                            params.put("validSign", RasConstant.RAS_VALID_SIGN_1);
                            //判收款账号是否有绑定的第三方支付账号信息
                            if (StringUtils.isNotEmpty(rasReceivedDtl.getPayeeAcc())) {
                                params.put("accNo", rasReceivedDtl.getPayeeAcc());
                                otherCardList = rasOtherCardBindingService.getRasOtherCardBindingByParams(params);
                            }
                            //取备注
                           /* if ((otherCardList == null || otherCardList.isEmpty()) && StringUtils.isNotEmpty(rasReceivedDtl.getRmk())) {
                                params.put("accNo", rasReceivedDtl.getRmk());
                                otherCardList = rasOtherCardBindingService.getRasOtherCardBindingByParams(params);
                            }*/
                            if (otherCardList != null && !otherCardList.isEmpty()) {
                                if (otherCardList.size() == 1) {
                                    RasOtherCardBinding otherCard = otherCardList.get(0);
                                    //设置缴款方式
                                    if (StringUtils.isEmpty(payType)) {
                                        payType = otherCard.getPayType();
                                    }
                                    //设置门店
                                    shopId = otherCard.getShopId();
                                } else {
                                    //优化根据缴款方式去匹配
                                    for(RasOtherCardBinding otherCard :otherCardList){
                                        if (StringUtils.isNotEmpty(payType) && payType.equals(otherCard.getPayType())) {
                                            //设置门店
                                            shopId = otherCard.getShopId();
                                        }
                                    }
                                }
                            }
                        }

                        //是否更新实收明细数据
                        boolean isUpdate = false;
                        if (StringUtils.isNotEmpty(payType)) {
                            rasReceivedDtl.setPayType(payType);
                            isUpdate = true;
                        }
                        if (StringUtils.isNotEmpty(shopId)) {
                            rasReceivedDtl.setShopId(shopId);
                            isUpdate = true;
                        }
                        if (isUpdate) {
                            Integer claimType = RasConstant.RAS_CLAIM_TYPE_0;
                            Integer status = RasConstant.RAS_AUDIT_STATUS_0;
                            //缴款方式、门店id都不为空，则认领标识为已认领状态,稽核状态为待稽核
                            if (StringUtils.isNotEmpty(rasReceivedDtl.getPayType()) && StringUtils.isNotEmpty(rasReceivedDtl.getShopId())) {
                                claimType = RasConstant.RAS_CLAIM_TYPE_1;
                                status = RasConstant.RAS_AUDIT_STATUS_1;
                            }
                            //更新实收数据明细表
                            int bindingNum = batchUpdateNoCheckStatus(rid, status, userVo,
                                    rasReceivedDtl.getShopId(), rasReceivedDtl.getPayType(), claimType);
                            if (bindingNum == 0) {
                                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                            }
                            //添加日志
                            if (claimType.equals(RasConstant.RAS_CLAIM_TYPE_1)) {
                                //成功条数增加
                                successNum++;
                                //已认领日志
                                rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_CLAIM, "营收稽核实收数据自动匹配认领", userVo);
                            } else {
                                //修改日志
                                rasLogService.saveRasLog(rasReceivedDtl.getId(), RasConstant.RAS_LOGTYPE_UPDATE, "营收稽核实收数据认领自动匹配修改", userVo);
                            }
                        }
                    } else {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                }
            }
            if (successNum == 0) {
                throw new BusinessException(RasMessageEnum.RAS_RECEIVED_CLAIM_NOCLAIMNUM);
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * 实收数据认领根据查询条件自动匹配认领全部
     *
     * @param rasReceivedClaimVO   查询条件参数
     */
    public boolean autoClaimAll(RasReceivedClaimVO rasReceivedClaimVO) {
        boolean result = false;
        int resultNum = 0;
        //获取该用户有权限的单位id集
        List<String> corpAuthList = corpService.getUserCorpIds(RasConstant.RAS_RESOURCE_RECEIVEDCLAIM_OP, rasReceivedClaimVO.getUser().getId());
        //获取该用户有（查询）权限的门店id集
        List<String> shopAuthList = rasShopGrantService.getShopIdByUserOrGrantType(RasConstant.GRANT_TYPE_QUERY, rasReceivedClaimVO.getUser());
       /* List<RasReceivedClaimVO> receList = null;
        do{
            receList = rasReceivedDao.getClaimListData(rasReceivedClaimVO,corpAuthList,shopAuthList);
            if (receList != null && !receList.isEmpty()) {
                if (((RasReceivedService)this.getBean(this.getClass())).batchAutoClaimAll(receList)) {
                    resultNum++;
                }
            }
        } while (receList!=null && receList.size()==RasConstant.RAS_AUTOT_CLAIM_MAXRESULT);*/
        List<RasReceivedClaimVO> receList = rasReceivedDao.getClaimListData(rasReceivedClaimVO,corpAuthList,shopAuthList);
        if (receList != null && !receList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                // 2.分页数据信息
                int totalSize = receList.size(); // 总记录数
                int pageSize = 1000; // 每页N条
                int totalPage = totalSize / pageSize; // 共N页
                if (totalSize % pageSize != 0) {
                    totalPage += 1;
                    if (totalSize < pageSize) {
                        pageSize = receList.size();
                    }
                }
                List<RasReceivedClaimVO> temList = null;
                for (int pageNum = 1; pageNum < totalPage + 1; pageNum++) {
                    int starNum = (pageNum - 1) * pageSize;
                    int endNum = pageNum * pageSize > totalSize ? (totalSize) : pageNum * pageSize;
                    temList = receList.subList(starNum, endNum);
                    if (((RasReceivedService)this.getBean(this.getClass())).batchAutoClaimAll(temList)) {
                        resultNum++;
                    }
                }
        }
        if (resultNum>0) {
            result = true;
        } else {
            throw new BusinessException(RasMessageEnum.RAS_RECEIVED_CLAIM_NOCLAIMNUM);
        }
        return result;
    }
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public boolean batchAutoClaimAll(List<RasReceivedClaimVO> receList ) {
        boolean result = false;
        //修改成功数据条数
        int successNum = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        for(RasReceivedClaimVO receivedClaim : receList){
            //判断是否是待确认或者待稽核
            if (RasConstant.RAS_AUDIT_STATUS_0.equals(receivedClaim.getStatus())
                    || RasConstant.RAS_AUDIT_STATUS_1.equals(receivedClaim.getStatus())) {
                String[] rid = {receivedClaim.getId()};
                //缴款方式
                String payType = receivedClaim.getPayType();
                //门店id
                String shopId = receivedClaim.getShopId();

                    /*
                     * 根据银行账号匹配
                     */
                //取收款账号
                if (StringUtils.isNotEmpty(receivedClaim.getPayeeAcc()) || StringUtils.isNotEmpty(receivedClaim.getRmk())) {
                    List<RasBankCardBinding> bankCarList = null;
                    if (StringUtils.isNotEmpty(receivedClaim.getPayeeAcc())) {
                        //判断账号是否有绑定的银行账号信息
                        bankCarList = rasBankCardBindingService.getRasBankCardBindingByParams(null, receivedClaim.getPayeeAcc(), null);
                        if (bankCarList != null && !bankCarList.isEmpty()) {
                            RasBankCardBinding bankCar = bankCarList.get(0);
                            //如果有数据其缴款方式为“现金”
                            if (StringUtils.isEmpty(payType)) {
                                payType = RasConstant.RAS_PAYTYPE_001;
                            }
                            //设置门店
                            if (StringUtils.isEmpty(shopId)) {
                                shopId = bankCar.getShopId();
                            }
                            //取备注
                        }
                    }
                    if (StringUtils.isNotEmpty(receivedClaim.getRmk()) && bankCarList == null) {
                        List<RasBankCardBinding> bankCarList2 = rasBankCardBindingService.getRasBankCardBindingByParams(null, receivedClaim.getRmk(), null);
                        if (bankCarList2 != null && !bankCarList2.isEmpty()) {
                            RasBankCardBinding bankCar = bankCarList2.get(0);
                            //如果有数据其缴款方式为“现金”
                            if (StringUtils.isEmpty(payType)) {
                                payType = RasConstant.RAS_PAYTYPE_001;
                            }
                            //设置门店
                            if (StringUtils.isEmpty(shopId)) {
                                shopId = bankCar.getShopId();
                            }
                        }
                    }
                }
                    /*
                     * 根据银联POS机匹配
                     */
                //实收数据中有没有同步/导入时分秒（交易时间）
                boolean isTime = true;
                if (StringUtils.isNotEmpty(receivedClaim.getTransDate())) {
                    //没有时分秒的
                    if (receivedClaim.getTransDate().toString().contains("00:00:00.0") || receivedClaim.getTransDate().toString().contains("00:00:00.000000")) {
                        isTime = false;
                    }
                }
                //其缴款方式为“银联POS机”且交易时间有时分秒的且门店id为空,搜索收款账号或备注中是否有POS终端号，并根据当前已启用的门店绑定终端关系识别其门店、单位
                if (RasConstant.RAS_PAYTYPE_004.equals(receivedClaim.getPayType()) && isTime && StringUtils.isEmpty(shopId)) {
                    RasPosBinding rasPosBinding = null;
                    //判断终端号是否存在
                    if (StringUtils.isNotEmpty(receivedClaim.getRmk())) {
                        rasPosBinding = rasPosBindingService.getRasPosBindingByParams(receivedClaim.getRmk(), RasConstant.RAS_VALID_SIGN_1);
                    }
                    if (rasPosBinding == null && StringUtils.isNotEmpty(receivedClaim.getPayeeAcc())) {
                        rasPosBinding = rasPosBindingService.getRasPosBindingByParams(receivedClaim.getPayeeAcc(), RasConstant.RAS_VALID_SIGN_1);
                    }
                    if (rasPosBinding != null) {
                        //判断状态是否是正常或已借出状态
                        if (RasConstant.RAS_POS_BD_STATUS_01.equals(rasPosBinding.getStatus()) || RasConstant.RAS_POS_BD_STATUS_03.equals(rasPosBinding.getStatus())) {
                            boolean isMatch = false;
                            //根据pos机绑定id查询pos机借入记录
                            List<RasPosBorrowVO> posBorrowList = rasPosBorrowService.getRasPosBorrowByBdId(rasPosBinding.getId(), new String[]{RasConstant.RAS_POS_BR_STATUS_02, RasConstant.RAS_POS_BR_STATUS_03});
                            if (posBorrowList != null && !posBorrowList.isEmpty()) {
                                //交易时间格式化
                                String transDate = format.format(receivedClaim.getTransDate());
                                for (RasPosBorrowVO posBorrow : posBorrowList) {
                                    //绑定日期
                                    String bdDate = posBorrow.getBdDate() == null ? null : format.format(posBorrow.getBdDate());
                                    //解绑日期
                                    String ubdDate = posBorrow.getUbdDate() == null ? null : format.format(posBorrow.getUbdDate());
                                    if (bdDate != null) {
                                        //是否是借用状态,判断交易日期是否大于等于绑定日期
                                        if (RasConstant.RAS_POS_BR_STATUS_02.equals(posBorrow.getStatus()) &&
                                                (transDate.compareTo(bdDate) == 0 || transDate.compareTo(bdDate) > 0)) {
                                            //借用且状态在借用时间内则把所属门店赋值
                                            shopId = posBorrow.getShopId();
                                            isMatch = true;
                                            //是否是已归还状态,判断交易日期是否大于等于绑定日期且小于解绑日期
                                        } else if (RasConstant.RAS_POS_BR_STATUS_03.equals(posBorrow.getStatus())
                                                && (transDate.compareTo(bdDate) == 0 || transDate.compareTo(bdDate) > 0)
                                                && ubdDate != null && transDate.compareTo(ubdDate) < 0) {
                                            //所属门店赋值
                                            shopId = posBorrow.getShopId();
                                            isMatch = true;
                                        }
                                    }
                                }
                            }
                            //如果无匹配到则取pos机绑定表
                            if (!isMatch) {
                                //所属门店赋值
                                shopId = rasPosBinding.getShopId();
                            }
                        }
                    }
                }
                    /*
                     * 根据第三方支付账号匹配
                     */
                if (StringUtils.isNotEmpty(receivedClaim.getPayeeAcc()) && StringUtils.isEmpty(shopId)) {
                    //判断账号是否有绑定的第三方支付账号信息
                    List<RasOtherCardBinding> otherCardList = null;
                    Map<String, Object> params = new HashMap<>();
                    params.put("validSign", RasConstant.RAS_VALID_SIGN_1);
                    //判收款账号是否有绑定的第三方支付账号信息
                    if (StringUtils.isNotEmpty(receivedClaim.getPayeeAcc())) {
                        params.put("accNo", receivedClaim.getPayeeAcc());
                        otherCardList = rasOtherCardBindingService.getRasOtherCardBindingByParams(params);
                    }
                    //取备注
                           /* if ((otherCardList == null || otherCardList.isEmpty()) && StringUtils.isNotEmpty(rasReceivedDtl.getRmk())) {
                                params.put("accNo", rasReceivedDtl.getRmk());
                                otherCardList = rasOtherCardBindingService.getRasOtherCardBindingByParams(params);
                            }*/
                    if (otherCardList != null && !otherCardList.isEmpty()) {
                        if (otherCardList.size() == 1) {
                            RasOtherCardBinding otherCard = otherCardList.get(0);
                            //设置缴款方式
                            if (StringUtils.isEmpty(payType)) {
                                payType = otherCard.getPayType();
                            }
                            //设置门店
                            shopId = otherCard.getShopId();
                        } else {
                            //优化根据缴款方式去匹配
                            for(RasOtherCardBinding otherCard :otherCardList){
                                if (StringUtils.isNotEmpty(payType) && payType.equals(otherCard.getPayType())) {
                                    //设置门店
                                    shopId = otherCard.getShopId();
                                }
                            }
                        }
                    }
                }

                //是否更新实收明细数据
                boolean isUpdate = false;
                if (StringUtils.isNotEmpty(payType)) {
                    receivedClaim.setPayType(payType);
                    isUpdate = true;
                }
                if (StringUtils.isNotEmpty(shopId)) {
                    receivedClaim.setShopId(shopId);
                    isUpdate = true;
                }
                if (isUpdate) {
                    Integer claimType = RasConstant.RAS_CLAIM_TYPE_0;
                    Integer status = RasConstant.RAS_AUDIT_STATUS_0;
                    //缴款方式、门店id都不为空，则认领标识为已认领状态,稽核状态为待稽核
                    if (StringUtils.isNotEmpty(receivedClaim.getPayType()) && StringUtils.isNotEmpty(receivedClaim.getShopId())) {
                        claimType = RasConstant.RAS_CLAIM_TYPE_1;
                        status = RasConstant.RAS_AUDIT_STATUS_1;
                    }
                    //更新实收数据明细表
                    int bindingNum = batchUpdateNoCheckStatus(rid, status, receivedClaim.getUser(),
                            receivedClaim.getShopId(), receivedClaim.getPayType(), claimType);
                    if (bindingNum == 0) {
                        throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
                    }
                    //添加日志
                    if (claimType.equals(RasConstant.RAS_CLAIM_TYPE_1)) {
                        //成功条数增加
                        successNum++;
                        //已认领日志
                        rasLogService.saveRasLog(receivedClaim.getId(), RasConstant.RAS_LOGTYPE_CLAIM, "营收稽核实收数据自动匹配认领", receivedClaim.getUser());
                    } else {
                        //修改日志
                        rasLogService.saveRasLog(receivedClaim.getId(), RasConstant.RAS_LOGTYPE_UPDATE, "营收稽核实收数据认领自动匹配修改", receivedClaim.getUser());
                    }
                }
            } else {
                throw new BusinessException(RasMessageEnum.RAS_MESSAGE_ERROR_DATAISUPDATEERROR);
            }
        }
        if (successNum > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 判断传递日期是否有未认领的数据
     *
     * @param 交易日期 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public boolean isNotClaimNum(String transDate, String shopId, ICurrentUser user) {
        boolean reBoo = true;
        if (StringUtils.isNotEmpty(transDate) && transDate.length() > 10) {
            Map<String, Object> params = new HashMap<>();
            Date startDate = DateUtils.str2Date(transDate.substring(0, 10), DateUtils.date_sdf());
            params.put("startDate", startDate);
            params.put("endDate", DateUtils.addDay(startDate, 1));
            params.put("wClaimType", RasConstant.RAS_CLAIM_TYPE_0);
            params.put("wAuditStatus", RasConstant.RAS_AUDIT_STATUS_0);
            params.put("wManualShopId", shopId);
            params.put("wManualUserCode", user.getLoginName());
            params.put("isShopIdAndCreateUser", "1");
            List<RasReceivedDtl> receivedDtlList = rasReceivedDao.getRasReceivedDtlByParams(params);
            if (receivedDtlList != null && receivedDtlList.size() > 0) {
                reBoo = false;
            }
        }
        return reBoo;
    }

    /**
     * @param @param  ids
     * @param @param  operate
     * @param @param  userVo
     * @param @return 参数
     * @return AjaxJson    返回类型
     * @throws
     * @Title: batchOperation
     * @Description: TODO(批量作废/单条作废)
     */
    public AjaxJson batchOperation(String ids, ICurrentUser userVo) {
        AjaxJson reJson = new AjaxJson();
        Map<String, Object> params = null;
        if (StringUtils.isNotEmpty(ids)) {
            try {
                String[] idArray = StringUtils.split(ids, ",");
                for (int i = 0; i < idArray.length; i++) {
                	RasReceivedDtl rasReceivedDtl =rasReceivedDao.get(idArray[i]);
                    params = new HashMap<String, Object>();
                	if(rasReceivedDtl.getIsSum()!=null
						&& rasReceivedDtl.getIsSum()==1){
                		params.put("ids", idArray[i]);
   	                    if (rasReceivedDao.tovoid(params) > 0) {
							params.put("sameDate", rasReceivedDtl.getTransDate());
							params.put("payType", rasReceivedDtl.getPayType());
							params.put("shopId", rasReceivedDtl.getShopId());
							if(rasReceivedDao.reduction(params)>0){
								reJson.setMsg(RasConstant.COMMON_MESSAGE_SUCCESS);
								reJson.setSuccess(true);
								rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_RETURN,RasConstant.RAS_CANCELLATION_MESSAGE_SUCESS, userVo);
							}
   	                    }
    				}else{
	                    params.put("ids", idArray[i]);
	                    if (rasReceivedDao.tovoid(params) > 0) {
	                        reJson.setMsg(RasConstant.COMMON_MESSAGE_SUCCESS);
	                        reJson.setSuccess(true);
	                    }
	                    rasLogService.saveRasLog(idArray[i], RasConstant.RAS_LOGTYPE_RETURN, RasConstant.RAS_CANCELLATION_MESSAGE_SUCESS, userVo);
    				}
                }
            } catch (Exception e) {
                reJson.setMsg(RasConstant.COMMON_MESSAGE_OPERATE_FAIL);
                reJson.setSuccess(false);
            } 
        }
        return reJson;
    }


    /**
     * @param @param list
     * @param @param userVo    参数
     * @return void    返回类型
     * @throws
     * @Title: saveImportRasReceived
     * @Description: TODO(导入实收数据方法)
     */
    public AjaxJson saveImportRasReceivedDtl(List<RasReceivedDtlVO> list, ICurrentUser userVo) {
        AjaxJson reJson = new AjaxJson();
        reJson.setSuccess(false);
        for (int i = 0; i < list.size(); i++) {
            RasReceivedDtlVO rasReceivedDtlVO = list.get(i);
            RasReceivedDtl entity = new RasReceivedDtl();
            BeanUtils.copyProperties(rasReceivedDtlVO, entity);
            entity.setTransDate(rasReceivedDtlVO.getTransDateStr());
            entity.setPayeebankName(rasReceivedDtlVO.getPayeeBankName());
            entity.setCreateBy(userVo.getLoginName());
            entity.setCreateDate(new Date());
            entity.setCreateName(userVo.getUsername());
            entity.setUpdateBy(userVo.getLoginName());
            entity.setUpdateDate(new Date());
            entity.setUpdateName(userVo.getUsername());
            rasReceivedDao.save(entity);
            reJson.setSuccess(true);
            rasLogService.saveRasLog(entity.getId(), RasConstant.RAS_LOGTYPE_IMP, RasConstant.RAS_BANK_SAVE_IMPORT, userVo);
        }
        return reJson;
    }

    @Override
	public ExcelVerifyHanlderResult verifyHandler(RasReceivedDtlVO info,
			Object... objs) {
    	return null;
    }

    @Override
    public  void verifyHandler(List<RasReceivedDtlVO> list, Object... objs) {
		ICurrentUser userVo =(ICurrentUser) objs[0];
		ImportCustomRules importCustomRules = (ImportCustomRules) objs[1];
		RasImportConfVO queryVo = new RasImportConfVO();
		queryVo.setDateSource(importCustomRules.getDataSources());
		queryVo.setFileType(importCustomRules.getSuffix());
		//缴款方式临时处理
		final List<String> dicList = dictService.listDictNameByDictGroup(RasConstant.RAS_OTHER_CARD_DICT);
		final List<DictDTO> listDictDTO = dictService.list(RasConstant.RAS_OTHER_CARD_DICT);
		//获取所有门店信息存储
		List<RasShopVO> shopCeleList = rasShopService.hisRasShop(userVo.getId());
		//重复值临时存储
//		List<RasReceivedDtlVO> rasReceivedCeleList = new ArrayList<RasReceivedDtlVO>();
		// 配置模板信息
		RasImportConfVO rasImportConfVO = rasImportConfService.queryRasImportConfVO(queryVo,RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
		if (list != null && !list.isEmpty()) {
			for(RasReceivedDtlVO info :list){
				List<String> errorMsgList = new ArrayList<String>();
				if (!StringUtils.isBlank(info.getShopName())) {
					RasShopVO rasShopVO = null;
					if(null!=shopCeleList && shopCeleList.size()>0){
						for(RasShopVO shopVo:shopCeleList){
							if(shopVo.getName().equals(info.getShopName())
									||shopVo.getCode().equals(info.getShopName())){
								rasShopVO=shopVo;
								continue;
							}
						}
					}
					//判断门店信息
					if(null==rasShopVO){
						errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_NOT_EXIST);
					}else{
						//判断门店是否禁用
						if(rasShopVO.getvSign().equals("0"))
							errorMsgList.add(RasConstant.RAS_EXCEL_MESSAGE_SHOP_VALIDSIGN_ISNOT);
						else
							//判断账户是否对改账户是否有稽核操作权限
							if(rasShopVO.getIsAudit().equals("1")){
								info.setShopId(rasShopVO.getId());
							}else{
								//没有稽核权限
								errorMsgList.add(RasConstant.RAS_IMPORT_SHOP_NOT_POWER);
							}
					}
				}
				info.setDateSource(rasImportConfVO.getDateSource());
				// 实收金额
				if (null == info.getMoney()) {
					errorMsgList.add(RasConstant.RAS_RASRECEIVEDDTL_IS_NOTNULL);
				}
				// 交易金额
				if (null == info.getTransMoney()) {
					errorMsgList.add("ras.rasreceiveddtl.transmoney.is.notnull");
				}
				//未设置缴款方式
				info.setStatus(0);
				info.setClaimType(0);
				//其它来源 缴款方式处理
				if(rasImportConfVO.getDateSource().equals("006")){
					if(!StringUtils.isBlank(rasImportConfVO.getPayType())){
						info.setPayType(rasImportConfVO.getPayType());
					}else{
						if(!StringUtils.isBlank(info.getPayTypeName())){
							if(!dicList.contains(info.getPayTypeName())){
								errorMsgList.add(RasConstant.RAS_MESSAGE_RASBANKCARDBINDING_PAYTYPE_EXIST);	
							}else{
								for(DictDTO dictDTO :listDictDTO){
									if(dictDTO.getName().equals(info.getPayTypeName())){
										info.setPayType(dictDTO.getCode());//转换类型
										continue;
									}
								}
							}
						}
					}
					//设置了头部缴款方式 且除其它来源之外的
					if(!StringUtils.isBlank(info.getShopId()) && !StringUtils.isBlank(rasImportConfVO.getPayType())
							||!StringUtils.isBlank(info.getShopId()) && !StringUtils.isBlank(info.getPayType())){
							info.setStatus(1);
							info.setClaimType(1);
					}
				}else{
					//除其它来源之外，默认还是取头部默认配置的缴款方式
					if(!StringUtils.isBlank(rasImportConfVO.getPayType())){
						info.setPayType(rasImportConfVO.getPayType());
					}	
					//设置了头部缴款方式 且除其它来源之外的
					if(!StringUtils.isBlank(info.getShopId()) 
							&& !StringUtils.isBlank(rasImportConfVO.getPayType())){
							info.setStatus(1);
							info.setClaimType(1);
					}
				}
				
				// 交易时间
				if (null == info.getTransDateStr()) {
					errorMsgList.add(RasConstant.RAS_IMPORT_TRANSDATE_NOTNULL);
				} else {
					info.setTransDate(info.getTransDateStr());
					info.setStrTransDate(RasUtils.formatToYYYYMMDDStr(info.getTransDateStr()));
				}
				// 判断是否有重复数据
				/*如果“交易流水号”、“实收订单号”、“门店编码”、“实收金额”都不为空时，
				判断数据是否有重复，有重复不让导入；以上字段有空值的不做重复判断，允许导入；
				*/
//				if (!StringUtils.isBlank(info.getShopId())
//						&& !StringUtils.isBlank(info.getSerialNo())
//						&& !StringUtils.isBlank(info.getOrderNo())
//						&& null != info.getMoney()) {
//					boolean layConFlag=true;
//					if(null!=rasReceivedCeleList && rasReceivedCeleList.size()>0){
//						for(RasReceivedDtlVO rasReceivedDtlVO:rasReceivedCeleList){
//							if(info.getShopId().equals(rasReceivedDtlVO.getShopId())
//								&&info.getSerialNo().equals(rasReceivedDtlVO.getSerialNo())
//								&&info.getOrderNo().equals(rasReceivedDtlVO.getOrderNo())
//								&&info.getMoney().equals(rasReceivedDtlVO.getMoney())){
//									layConFlag=false;
//									errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_REPEAT_EXIST);
//								continue;
//							}
//						}
//					}
//					if(layConFlag){
//						rasReceivedCeleList.add(info);
//						// 判断数据是否有重复
//						boolean flag = rasReceivedDao.getIsRasReceivedDtlVO(info);
//						if (flag) {
//							errorMsgList.add(RasConstant.RAS_IMPORT_REVENUE_REPEAT_EXIST);
//						}
//					}
//				}
				info.addErrorMsg(errorMsgList);
			}
		}
    }

    //解析内容
    public ImportCustomRules getImportCustomRules(Object... obj) {
        ImportCustomRules customRules = new ImportCustomRules();
        @SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) obj[0];
        String suffix = String.valueOf(params.get("suffix"));//文件后缀名
        String[] datasource = (String[]) params.get("datasource");//数据来源
        RasImportConfVO queryVo = new RasImportConfVO();
        queryVo.setFileType(suffix);
        queryVo.setDateSource(String.valueOf(datasource[0]));
        //数据来源为空
        if (StringUtils.isBlank(queryVo.getDateSource())) {
            throw new ParameterException(RasMessageEnum.RAS_MESSAGE_DATASOURE_NOTNULL);
        }
        //配置模板信息
        RasImportConfVO rasImportConfVO
                = rasImportConfService.queryRasImportConfVO(queryVo, RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
        if (null == rasImportConfVO) {
            throw new ParameterException(RasMessageEnum.RAS_IMPORT_REVENUE_NOT_CONFIGURED_EXIST);
        }
        //取配置行列数据
        List<Map<String, String>> rasImportConfVOList
                = "txt".equalsIgnoreCase(suffix) ? rasImportConfService.getTxtModelAnalysis(rasImportConfVO, RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED) : rasImportConfService.getTableInfo(queryVo, RasConstant.RAS_BANK_OPERATE_STATUS_RECEIVED);
        //存在列头
        if ("csv".equalsIgnoreCase(suffix)
                || "xls".equalsIgnoreCase(suffix)
                || "xlsx".equalsIgnoreCase(suffix)) {
            Map<String, String> coluMap = new HashMap<String, String>();
            for (Map<String, String> lineTopMap : rasImportConfVOList) {
                if (lineTopMap.get("value") != null) {
                    coluMap.put(lineTopMap.get("required").toString().equals("true")
                            ? "*" + lineTopMap.get("nameCNY").toString() : lineTopMap.get("nameCNY").toString(), lineTopMap.get("value"));
                }
            }
            int startRow = rasImportConfVO.getStartLine();
            //设置起始行
            customRules.setStartRow(startRow);
            //设置无列标题
            customRules.setHead(false);
            //设置列标题
            customRules.setColumnPositio(coluMap);
        } else if ("txt".equalsIgnoreCase(suffix)) {
            int startRow = rasImportConfVO.getStartLine();//起始行
            //设置起始行
            customRules.setStartRow(startRow);
            if (rasImportConfVO.getIsFormat().equals("1")) {//是否有分隔符
                Map<String, Integer> separatorPositionMap = new HashMap<String, Integer>();
                for (Map<String, String> lineTopMap : rasImportConfVOList) {
                    if (lineTopMap.get("value") != null) {
                        //不存在分隔符要拼接value
                        separatorPositionMap.put(lineTopMap.get("required").toString().equals("true")
                                ? "*" + lineTopMap.get("nameCNY").toString() : lineTopMap.get("nameCNY").toString(), Integer.valueOf(lineTopMap.get("value")));
                    }
                }
                //设置无列标题
                customRules.setHead(false);
                //设置是否存在分隔符
                customRules.setSeparator(true);
                //设置分隔符
                customRules.setSeparator(rasImportConfVO.getTxtFormat());
                //设置分隔符位置
                customRules.setSeparatorPosition(separatorPositionMap);
            } else {//不存在分隔符
                Map<String, String> notSeparatorColumnPositio = new HashMap<String, String>();
                for (Map<String, String> lineTopMap : rasImportConfVOList) {
                    if (!lineTopMap.get("value").equals("null-null")) {
                        notSeparatorColumnPositio.put(lineTopMap.get("required").toString().equals("true")
                                ? "*" + lineTopMap.get("nameCNY").toString() : lineTopMap.get("nameCNY").toString(), lineTopMap.get("value"));
                    }
                }
                //设置无列标题
                customRules.setHead(false);
                customRules.setNotSeparatorColumnPositio(notSeparatorColumnPositio);
            }
        } else {
            throw new ParameterException(RasMessageEnum.RAS_IMPORT_REVENUE_NOT_CONFIGURED_EXIST);
        }
        customRules.setSuffix(suffix);
        customRules.setDataSources(String.valueOf(datasource[0]));
        return customRules;
    }
    
    /**
     * 根据条件获取营收表数据
     * @param params
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRES_NEW)
	public List<RasReceivedDtlVO> getAllRasReceivedDtlVOInfo(Map<String,Object> params){
    	return rasReceivedDao.getAllRasReceivedDtlVOInfo(params);
    }
}
