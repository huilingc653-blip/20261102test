package com.bytter.basic.service;

import com.bytter.basic.dao.FcExcrateAdjustDao;
import com.bytter.basic.entity.BtRateType;
import com.bytter.basic.entity.FcExcrateAdjust;
import com.bytter.basic.vo.FcExcrateAdjustVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.system.entity.User;
import com.bytter.system.service.UserService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: FC_EXCRATE_ADJUST
 * @date 2017-09-06 10:01:05
 */
@Service("fcExcrateAdjustServiceBase")
@Transactional
public class FcExcrateAdjustService extends BaseService<FcExcrateAdjust, String> {
    @Autowired
    private FcExcrateAdjustDao fcExcrateAdjustDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private BtRateTypeService btRateTypeService;
    @Autowired
    private IBtCurrencyService btCurrencyServiceI;
    /**
     * 保存
     *
     * @param entity
     */
    public void saveFcExcrateAdjust(FcExcrateAdjust entity, String loginName) {
        checkData(entity);
        entity.setCreateBy(loginName);
        entity.setCreateDate(DateUtils.getDate());
//        entity.setOldExchangeRate(this.getExchangeRateBy(entity.getCurId(), entity.getReferCurId(), , entity.getCreateDate()));
        super.saveEntity(entity);
        boolean statusFlag = CommonConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
        notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST_BACK, entity.getId(), userService.getUserByLoginName(loginName).getId());
        if (statusFlag) {
            sendNotificationForExchangeRate(entity, loginName);
        }
    }
    /**
     * 修改
     *
     * @param entity
     */
    public void updateFcExcrateAdjust(FcExcrateAdjust entity, String loginName) {
        checkData(entity);
        entity.setUpdateBy(loginName);
        entity.setUpdateDate(DateUtils.getDate());
//        entity.setOldExchangeRate(this.getExchangeRateBy(entity.getCurId(), entity.getUpdateDate()));
        super.saveOrUpdate(entity);
        boolean statusFlag = CommonConstant.STATUE_WAIT_CHECK.equals(entity.getStatus());
        notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST_BACK, entity.getId(), userService.getUserByLoginName(loginName).getId());
        if (statusFlag) {
            sendNotificationForExchangeRate(entity, loginName);
        }
    }

    private void checkData(FcExcrateAdjust excrateAdjust) {
        AssertHelper.isTrueBusEx(excrateAdjust.getNewExchangeRate().compareTo(BigDecimal.ZERO) > 0, CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_ZERO);
    }

    @Override
    public void deleteEntity(FcExcrateAdjust entity) {
        super.deleteEntity(entity);
        notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST_BACK, entity.getId(), userService.getUserByLoginName(entity.getCreateBy()).getId());
    }
    @Override
    public BaseDao<FcExcrateAdjust, String> getEntityDao() {
        return fcExcrateAdjustDao;
    }
    /**
     * 执行日期不能小于或者等于上次的执行日期
     *
     * @param entity
     * @return
     */
    public boolean isRepeatPreTime(FcExcrateAdjust entity) {
        FcExcrateAdjust oldFcExcrateAdjust = this.fcExcrateAdjustDao.getPreTimeBy(entity.getCurId(), entity.getId());
        if (EmptyUtils.isEmpty(oldFcExcrateAdjust)) {
            return false;
        } else {
            return entity.getPreTime().compareTo(oldFcExcrateAdjust.getPreTime()) <= 0;
        }
    }
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return this.fcExcrateAdjustDao.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public FcExcrateAdjustVO getByUnion(String id) {
        FcExcrateAdjustVO fcExcrateAdjustVO = this.fcExcrateAdjustDao.getByUnion(id);
        addCurName(Lists.newArrayList(fcExcrateAdjustVO));
        return fcExcrateAdjustVO;
    }

    private void addCurName(List<FcExcrateAdjustVO> fcExcrateAdjustVOList) {
        List<String> curIds = new ArrayList<>();
        fcExcrateAdjustVOList.forEach(item->{
            curIds.add(item.getCurId());
            curIds.add(item.getReferCurId());
        });
        Map<String, String> currencyNameMap = btCurrencyServiceI.getCurrencyNameByIds(curIds);
        fcExcrateAdjustVOList.forEach(item->{
            item.setCurName(currencyNameMap.get(item.getCurId()));
            item.setReferCurName(currencyNameMap.get(item.getReferCurId()));
        });
    }

    /**
     * 批量提交
     *
     * @param ids
     */
    public void batchSubmit(String ids, String loginName) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcExcrateAdjust entity = this.get(id);
            boolean statusFlag = CommonConstant.STATUE_TEMP.equals(entity.getStatus()) || CommonConstant.STATUE_BACK.equals(entity.getStatus());
            if (!statusFlag) {
                throw new ParameterException(CurrencyMessageEnum.FC_EXCRATE_ADJUST_MESSAGE_STATUS);
            }
            entity.setUpdateBy(loginName);
            entity.setUpdateDate(DateUtils.getDate());
            entity.setStatus(CommonConstant.STATUE_WAIT_CHECK);
            this.saveOrUpdate(entity);
            notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST_BACK, entity.getId(), userService.getUserByLoginName(loginName).getId());
            sendNotificationForExchangeRate(entity, loginName);
        }
    }
    /**
     * 批量复核
     *
     * @param ids
     */
    public void batchCheck(String ids, String loginName) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcExcrateAdjust entity = this.get(id);
            entity.setUpdateBy(loginName);
            entity.setUpdateDate(DateUtils.getDate());
            entity.setStatus(CommonConstant.STATUE_CHECK);
            this.saveOrUpdate(entity);
            notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST, id, userService.getUserByLoginName(loginName).getId());
        }
    }
    /**
     * 批量打回
     *
     * @param ids
     */
    public void batchBack(String ids, String loginName) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FcExcrateAdjust entity = this.get(id);
            entity.setUpdateBy(loginName);
            entity.setUpdateDate(DateUtils.getDate());
            entity.setStatus(CommonConstant.STATUE_BACK);
            this.saveOrUpdate(entity);
            notificationService.complete(NotificationConstant.BASESYS.FCEXCRATE_ADJUST, id, userService.getUserByLoginName(loginName).getId());
            notificationService.sendToUserByLoginName(entity.getId(), entity.getCreateBy(),
                    NotificationConstant.BASESYS.FCEXCRATE_ADJUST_BACK, entity);
        }
    }
    /**
     * 查询货币汇率
     *
     * @param curId   币别ID
     * @param preTime 执行日期
     * @return id
     */
    public BigDecimal getExchangeRateBy(String curId, Date preTime) {
        if (EmptyUtils.isBlank(curId)) {
            return null;
        }
        BigDecimal exchangeRate = this.fcExcrateAdjustDao.getExchangeRateBy(curId, preTime, "");
        if (EmptyUtils.isEmpty(exchangeRate)) {
            BtCurrencyDTO dto = btCurrencyServiceI.getObjectById(curId);
            return EmptyUtils.isEmpty(dto) ? null : dto.getCurExrate();
        }
        return exchangeRate;
    }
    public DataTablesOutput getDataTablesCheck(DataTablesInput dataTablesInput, String loginName) {
        return this.fcExcrateAdjustDao.getDataTablesCheck(dataTablesInput, loginName);
    }
    /**
     * 货币汇率修改提交时,给相应用户发送通知消息
     *
     * @param entity
     * @param loginName
     */
    private void sendNotificationForExchangeRate(FcExcrateAdjust entity, String loginName) {
        User user = userService.getByLoginName(loginName);
        notificationService.sendIgnoreMySelf(entity.getId(), user.getCorp().getId(), NotificationConstant.BASESYS.FCEXCRATE_ADJUST, entity, user.getId());
    }
    /**
     * 查询货币汇率
     *
     * @param curId   币别ID
     * @param preTime 执行日期
     * @param typeId  汇率类型id
     * @return id
     */
    public BigDecimal getExchangeRateBy(String curId, Date preTime, String typeId) {
        if (EmptyUtils.isBlank(curId)) {
            return null;
        }
        BigDecimal exchangeRate = this.fcExcrateAdjustDao.getExchangeRateBy(curId, preTime, typeId);
        if (EmptyUtils.isEmpty(exchangeRate)) {
            IBtCurrencyService btCurrencyServiceI = (IBtCurrencyService) this.getBean(IBtCurrencyService.class);
            BtCurrencyDTO dto = btCurrencyServiceI.getObjectById(curId);
            return EmptyUtils.isEmpty(dto) ? null : dto.getCurExrate();
        }
        return exchangeRate;
    }
    public FcExcrateAdjust getExchangeRate(String curI1, String curI2, Date preTime, String typeId) {
        FcExcrateAdjust fcExcrateAdjust = this.fcExcrateAdjustDao.getFcExcrateAdjustBy(curI1, curI2, preTime, typeId);
        if (fcExcrateAdjust == null) {
            return null;
        }
        return fcExcrateAdjust;
    }
    /**
     * 两个币别直接关联
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return 货币汇率
     */
    public BigDecimal getExchangeRateByDirectCurrency(String originalCurId, String convertCurId, Date preTime, String typeId) {
        // 查询汇率
        FcExcrateAdjust fcExcrateAdjust = this.getExchangeRate(originalCurId, convertCurId, preTime, typeId);
        if (fcExcrateAdjust == null) {
            return null;
        }
        BigDecimal exchangeRate = fcExcrateAdjust.getNewExchangeRate();
        String curId1 = fcExcrateAdjust.getCurId();
        //根据汇率类型获取汇率精度
        int decimalPlaces = this.getRateDecimalPlaces(typeId);
        return curId1.equalsIgnoreCase(originalCurId) ? exchangeRate : BigDecimal.ONE.divide(exchangeRate, decimalPlaces, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 通过第三方币别中转关联
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return 货币汇率
     */
    public BigDecimal getExchangeRateByTransferCurrency(String originalCurId, String convertCurId, Date preTime, String typeId) {
        String transferCurId = this.fcExcrateAdjustDao.getTransferCurId(originalCurId, convertCurId, preTime, typeId);
        if (EmptyUtils.isEmpty(transferCurId)) {
            logger.error(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的找不到中间货币");
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
        FcExcrateAdjust x = this.getExchangeRate(originalCurId, transferCurId, preTime, typeId);
        FcExcrateAdjust y = this.getExchangeRate(convertCurId, transferCurId, preTime, typeId);
        return calculateRete(x, y, originalCurId);
    }
    /**
     * 通过第三方币别中转关联(不强制进行错误校验，查询为空返回null)
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return 货币汇率
     */
    public BigDecimal getExchangeRateByTransferCurrencyNoCheck(String originalCurId, String convertCurId, Date preTime, String typeId) {
        String transferCurId = this.fcExcrateAdjustDao.getTransferCurId(originalCurId, convertCurId, preTime, typeId);
        if (EmptyUtils.isEmpty(transferCurId)) {
            logger.error(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率不存在");
            return null;
        }
        FcExcrateAdjust x = this.getExchangeRate(originalCurId, transferCurId, preTime, typeId);
        FcExcrateAdjust y = this.getExchangeRate(convertCurId, transferCurId, preTime, typeId);
        return calculateRete(x, y, originalCurId);
    }
    /**
     * 计算汇率
     *
     * @param x
     * @param y
     * @param originalCurId
     * @return
     */
    public BigDecimal calculateRete(FcExcrateAdjust x, FcExcrateAdjust y, String originalCurId) {
        //根据汇率类型获取汇率精度
        int decimalPlaces = this.getRateDecimalPlaces(x.getTypeId());
        int decimalPlaces1 = decimalPlaces + 2 ;
        BigDecimal rate = BigDecimal.ZERO;
        if (x.getCurId().equalsIgnoreCase(y.getCurId())) {
//             USD/CNY=7.1209=X;
//             USD/JPY=109.04=Y;
//             相除得到 JPY/CNY=0.0653=X/Y,业务如果需要CNY/JPY=1/0.0653=15.3127;
            rate = x.getNewExchangeRate().divide(y.getNewExchangeRate(), decimalPlaces1, BigDecimal.ROUND_HALF_UP);
            return y.getReferCurId().equalsIgnoreCase(originalCurId) ? rate : BigDecimal.ONE.divide(rate, decimalPlaces, BigDecimal.ROUND_HALF_UP);
        } else if (x.getReferCurId().equalsIgnoreCase(y.getReferCurId())) {
//            USD/CNY=7.1209=X;
//            AUD/CNY=4.9191=Y;
//            相除得到USD/AUD=1.4476=X/Y,业务如果需要AUD/USD=1/1.4476=0.6908;
            rate = x.getNewExchangeRate().divide(y.getNewExchangeRate(), decimalPlaces1, BigDecimal.ROUND_HALF_UP);
            return x.getCurId().equalsIgnoreCase(originalCurId) ? rate : BigDecimal.ONE.divide(rate, decimalPlaces, BigDecimal.ROUND_HALF_UP);
        } else if (x.getCurId().equalsIgnoreCase(y.getReferCurId())) {
//            USD/CNY=7.1209=X;
//            AUD/USD=0.6908=Y;
//            相乘得到AUD/CNY=4.9191=Y*X,业务如果需要CNY/AUD=1/4.9191=0.2033;
            rate = x.getNewExchangeRate().multiply(y.getNewExchangeRate());
            return y.getCurId().equalsIgnoreCase(originalCurId) ? rate.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) : BigDecimal.ONE.divide(rate, decimalPlaces, BigDecimal.ROUND_HALF_UP);
        } else if (x.getReferCurId().equalsIgnoreCase(y.getCurId())) {
//            USD/CNY=7.1209=X;
//            CNY/JPY=15.3127=Y;
//            相乘得到USD/JPY=109.0402=X*Y,业务如果需要JPY/USD=1/109.0402=0.0092;
            rate = x.getNewExchangeRate().multiply(y.getNewExchangeRate());
            return x.getCurId().equalsIgnoreCase(originalCurId) ? rate.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP) : BigDecimal.ONE.divide(rate, decimalPlaces, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }
    /**
     * 查询货币汇率
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return 货币汇率/cms/provide/loaninfo/queryLoanDatatable
     */
    public BigDecimal getExchangeRateBy(String originalCurId, String convertCurId, Date preTime, String typeId) {
        if (EmptyUtils.isBlank(typeId)) {
            typeId = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        if (EmptyUtils.isBlank(originalCurId) || EmptyUtils.isBlank(convertCurId)) {
            return null;
        }
        // 原币别和转换币别一致，返回1
        if (originalCurId.equalsIgnoreCase(convertCurId)) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + BigDecimal.ONE);
            return BigDecimal.ONE;
        }
        // 两个币别直接关联
        BigDecimal exchangeRate = getExchangeRateByDirectCurrency(originalCurId, convertCurId, preTime, typeId);
        if (exchangeRate != null) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + exchangeRate);
            return exchangeRate;
        }
        // 通过第三方币别中转关联
        exchangeRate = getExchangeRateByTransferCurrency(originalCurId, convertCurId, preTime, typeId);
        if (exchangeRate != null) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + exchangeRate);
            return exchangeRate;
        }
        logger.error(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率不存在");
        throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
    }
    /**
     * 查询货币汇率(不强制进行错误校验，查询为空返回null)
     *
     * @param originalCurId 原币别ID
     * @param convertCurId  转换币别ID
     * @param preTime       执行日期
     * @param typeId        汇率类型id
     * @return 货币汇率
     */
    public BigDecimal getExchangeRateByNoCheck(String originalCurId, String convertCurId, Date preTime, String typeId) {
        if (EmptyUtils.isBlank(typeId)) {
            typeId = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        if (EmptyUtils.isBlank(originalCurId) || EmptyUtils.isBlank(convertCurId)) {
            return null;
        }
        // 原币别和转换币别一致，返回1
        if (originalCurId.equalsIgnoreCase(convertCurId)) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + BigDecimal.ONE);
            return BigDecimal.ONE;
        }
        // 两个币别直接关联
        BigDecimal exchangeRate = getExchangeRateByDirectCurrency(originalCurId, convertCurId, preTime, typeId);
        if (exchangeRate != null) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + exchangeRate);
            return exchangeRate;
        }
        // 通过第三方币别中转关联
        exchangeRate = getExchangeRateByTransferCurrencyNoCheck(originalCurId, convertCurId, preTime, typeId);
        if (exchangeRate != null) {
            logger.info(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率为：" + exchangeRate);
            return exchangeRate;
        }
        logger.error(" 转换时间：" + DateUtils.formatDateTime(preTime) + "，币种" + originalCurId + "转" + convertCurId + "的汇率不存在");
        return null;
    }
    /**
     * 查询货币汇率 小数位数
     *
     * @param typeId 汇率类型id
     * @return
     */
    public Integer getRateDecimalPlaces(String typeId) {
        Integer decimalPlaces = 4;
        if (EmptyUtils.isBlank(typeId)) {
            typeId = FcExcrateAdjustConstant.RATE_TYPE_CONVERT;
        }
        BtRateType entity = btRateTypeService.get(typeId, true);
        if (!EmptyUtils.isEmpty(entity)) {
            decimalPlaces = entity.getDecimalPlaces();
        }
        return decimalPlaces;
    }
}
