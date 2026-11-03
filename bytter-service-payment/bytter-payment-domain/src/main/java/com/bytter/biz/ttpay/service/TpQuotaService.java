package com.bytter.biz.ttpay.service;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.ttpay.dao.TpQuotaDao;
import com.bytter.biz.ttpay.entity.TpMerchant;
import com.bytter.biz.ttpay.entity.TpQuota;
import com.bytter.biz.ttpay.enums.TtPayMessageEnum;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author chenf
 * @version 1.0
 * @Title: Service
 * @Description: 限额管理Service
 * @date 2022-05-05 15:43:05
 */
@Service("tpQuotaService")
@Transactional
public class TpQuotaService extends BaseService<TpQuota, String> {
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Autowired
    private TpQuotaDao tpQuotaDao;
    @Autowired
    private TpLogService tpLogService;
    @Autowired
    private TpPaymentService tpPaymentService;
    @Autowired
    private TpMerchantService tpMerchantService;
    @Override
    public BaseDao<TpQuota, String> getEntityDao() {
        return tpQuotaDao;
    }
    @Override
    public DataTablesOutput getDataTables(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = corpService.getUserCorpIds(TtPayConstant.TTPAY_RESOURCE_QUOTA_QUERY, dataTablesInput.getCurrentUserId());
        DataTablesOutput dataTablesOutput = tpQuotaDao.getDataTables(dataTablesInput, corpAuthList);
        List<TpQuotaVO> list = dataTablesOutput.getData();
        Map<String, CorpDTO> corpMap = corpService.getCorpMap();
        for (TpQuotaVO vo:list) {
            if (corpMap.containsKey(vo.getCorpId())) {
                vo.setCorpName(corpMap.get(vo.getCorpId()).getName());
            }
        }
        return dataTablesOutput;
    }
    /**
     * @param infoVO 限额数据信息
     * @param user   用户
     * @return 处理结果
     * @throws
     * @Title: 限额信息数据新增或者修改操作
     */
    public AjaxJson saveOrUpdate(TpQuotaVO infoVO, ICurrentUser user) {
        AjaxJson reJson = new AjaxJson();
        TpQuota quota = new TpQuota();
        String logType = TtPayConstant.TTPAY_LOGTYPE_ADD;
        String msg = TtPayConstant.COMMON_MESSAGE_ADDSUCCESS;
        // 校验限额商户号、单位、支付方式三个值不能出现重复的
        boolean result = this.isRepeatQuotaInfo(infoVO.getId(), infoVO);
        if (result) {
            throw new ParameterException(TtPayMessageEnum.TPQUOTAVO_MESSAGE_CODE_REP);
        }
        // 获取商户信息
        TpMerchant tpMerchant = tpMerchantService.get(infoVO.getMerchantId());
        //id不为空时为修改数据
        if (!StringUtils.isEmpty(infoVO.getId())) {
            quota = tpQuotaDao.get(infoVO.getId(), true);
            BeanUtils.copyProperties(infoVO, quota);
            quota.setMerchantNo(tpMerchant.getMerchantNo());
            quota.setUpdateBy(user.getLoginName());
            quota.setUpdateDate(new Date());
            logType = TtPayConstant.TTPAY_LOGTYPE_UPDATE;
            msg = TtPayConstant.COMMON_MESSAGE_UPDATESUCCESS;
        } else {
            BeanUtils.copyNotNullProperties(infoVO, quota);
            quota.setId(null);
            quota.setMerchantNo(tpMerchant.getMerchantNo());
            quota.setCreateBy(user.getLoginName());
            quota.setCreateDate(new Date());
        }
        tpQuotaDao.saveOrUpdate(quota);
        tpLogService.saveTpLog(quota.getId(), logType, msg, user);
        reJson.setSuccess(true);
        return reJson;
    }
    /**
     * 根据ID获取VO信息
     *
     * @param id
     * @return
     */
    public TpQuotaVO getTpQuotaInfoById(String id) {
        TpQuotaVO vo = tpQuotaDao.getTpQuotaInfoById(id);
        if (vo != null) {
            CorpDTO corp = corpService.get(vo.getCorpId());
            vo.setCorpName(corp.getName());
        }
        return vo;
    }
    /**
     * 根据ID获取VO信息
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public TpQuotaVO getTpQuotaVO(String id) {
        TpQuota info = this.get(id, true);
        TpQuotaVO infoVo = new TpQuotaVO();
        BeanUtils.copyProperties(info, infoVo);
        return infoVo;
    }
    /**
     * 根据客商信息id删除限额信息
     *
     * @param quotaId
     * @return
     */
    public void deleteQuota(String quotaId, ICurrentUser user) {
        TpQuota tpQuota = tpQuotaDao.get(quotaId, true);
        tpQuota.setValidSign(TtPayConstant.TTPAY_VALIDSIGN0);
        this.updateEntity(tpQuota);
        tpLogService.saveTpLog(quotaId, TtPayConstant.TTPAY_LOGTYPE_DELETE, TtPayConstant.COMMON_MESSAGE_DELETESUCCESS, user);
    }
    /**
     * 修改是否有效标识
     *
     * @param ids       需要修改的ID
     * @param validSign 有效状态
     * @return
     */
    public boolean updateValidSign(String ids, String validSign, ICurrentUser user) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            TpQuota info = this.get(id, true);
            info.setValidSign(validSign);
            tpQuotaDao.update(info);
            tpLogService.saveTpLog(id, TtPayConstant.TTPAY_LOGTYPE_UPDATE, TtPayConstant.COMMON_MESSAGE_ISONLINE_UPDATESUCCESS, user);
        }
        return true;
    }
    /**
     * 通过获取限额信息以及剩余额度
     *
     * @param tpQuotaVO  限额对象
     * @param queryType  已用额度查询规则
     * @param wishPayday 期望支付日期
     * @return
     */
    public TpQuotaVO getTpQuotaAvail(TpQuotaVO tpQuotaVO, String queryType, Date wishPayday) {
        if (TtPayConstant.TTPAY_VALIDSIGN0.equals(tpQuotaVO.getValidSign())) {
            return tpQuotaVO;
        }
        String endDateStr = null;
        Date endDate = DateUtils.getDate();
        Date dayDate = endDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //期望支付日期不为空，则根据期望支付日期查询可用余额
        if (StringUtils.isNotEmpty(wishPayday)) {
            endDate = wishPayday;
            dayDate = wishPayday;
        }
        String beginDateStr1 = sdf.format(endDate);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(endDate);
        calendar1.add(Calendar.DATE, 1);
        endDate = calendar1.getTime();
        endDateStr = sdf.format(endDate);
        BigDecimal dayTotalUse = tpPaymentService.getSumPayMoney(tpQuotaVO.getCorpId(), tpQuotaVO.getMerchantNo(), tpQuotaVO.getPayWay(), beginDateStr1, endDateStr);
        //日已用余额
        tpQuotaVO.setDayTotalUse(dayTotalUse);
        // 生效日期
        Date effectDate = tpQuotaVO.getEffectDate();
        // 日累计限额剩余额度
        if (StringUtils.isNotEmpty(tpQuotaVO.getDayTotal()) && dayDate.compareTo(effectDate) >= 0) {
            BigDecimal dayTotalAvail = new BigDecimal(0);
            if (dayTotalUse.compareTo(tpQuotaVO.getDayTotal()) < 0) {
                 dayTotalAvail = tpQuotaVO.getDayTotal().subtract(dayTotalUse);
            }
            tpQuotaVO.setDayTotalAvail(dayTotalAvail);
        }
        Calendar calendar2 = getFirstDayCalender(endDate);
        Date beginDate = calendar2.getTime();
        String beginDateStr2 = sdf.format(beginDate);
        // 月初时间小于开始时间
        if (beginDate.compareTo(effectDate) < 0) {
            beginDateStr2 = sdf.format(effectDate);
        }
        BigDecimal monthTotalUse = tpPaymentService.getSumPayMoney(tpQuotaVO.getCorpId(), tpQuotaVO.getMerchantNo(), tpQuotaVO.getPayWay(), beginDateStr2, endDateStr);
        // 月已用余额
        tpQuotaVO.setMonthTotalUse(monthTotalUse);
        // 月累计限额剩余额度
        if (StringUtils.isNotEmpty(tpQuotaVO.getMonthTotal()) && dayDate.compareTo(effectDate) >= 0) {
            // 如果可用金额大于限额，可用金额设置为0
            BigDecimal monthTotalAvail = new BigDecimal(0);
            if (monthTotalUse.compareTo(tpQuotaVO.getMonthTotal()) < 0) {
                monthTotalAvail = tpQuotaVO.getMonthTotal().subtract(monthTotalUse);
            }
            tpQuotaVO.setMonthTotalAvail(monthTotalAvail);
        }
        return tpQuotaVO;
    }
    /**
     * 通过单位、商户号、支付方式 获取限额信息
     *
     * @param corpId     单位id
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @return
     */
    public TpQuotaVO getTpQuotaInfo(String corpId, String merchantNo, String payType, Date wishPayday) {
        TpQuotaVO tpQuotaVO = tpQuotaDao.getTpQuotaInfo(corpId, merchantNo, payType);
        if (tpQuotaVO != null) {
            tpQuotaVO = getTpQuotaAvail(tpQuotaVO, payType, wishPayday);
        }
        return tpQuotaVO;
    }
    /**
     * 获取给定日期的第一天
     */
    protected Calendar getFirstDayCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    /**
     * 校验重复
     *
     * @param id
     * @param infoVO
     * @return
     */
    public boolean isRepeatQuotaInfo(String id, TpQuotaVO infoVO) {
        List<TpQuotaVO> list = tpQuotaDao.getQuotaInfo(infoVO);
        if (ListUtils.isNullOrEmpty(list)) {
            return false;
        } else {
            if (EmptyUtils.isBlank(id)) {
                return true;
            }
            TpQuotaVO obj = list.get(0);
            return obj.getId().equals(id) ? false : true;
        }
    }
}
