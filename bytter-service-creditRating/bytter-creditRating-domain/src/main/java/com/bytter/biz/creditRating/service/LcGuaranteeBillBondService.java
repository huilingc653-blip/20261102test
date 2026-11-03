package com.bytter.biz.creditRating.service;


import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesOutput;

import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.ILcGuaranteeBillBondService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.CrBillBondRateDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.LcEnrolDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 票据和信用证保证金比例数据源服务
 */
@Service("lcGuaranteeBillBondService")
public class LcGuaranteeBillBondService extends BaseService {

    @DubboReference(check = false)
    private ILcGuaranteeBillBondService lcGuaranteeBillBondService;

    @Autowired
    private CrdRawDataService crdRawDataService;

    @DubboReference(check = false)
    private IFcBondHookService fcBondHookService;

    @Override
    public BaseDao getEntityDao() {
        return null;
    }

    /**
     * 获取组织与机构之间的票据和信用证的保证金比例
     *
     * @param corpId           评价组织
     * @param institutionIds   评价机构
     * @param scoringStartDate 评价开始时间
     * @param scoringEndDate   评价结束时间
     * @return
     */
    public Map<String, BigDecimal> getLcGuaranteeBillBond(String corpId, List<String> institutionIds, String scoringStartDate, String scoringEndDate) {
        Map<String, BigDecimal> map = new HashMap<>();
        //获取机构信息
        List<InstitutionDetailsVo> institutionDetailsVos = crdRawDataService.findInstitutionByIds(institutionIds);
        if (null != institutionDetailsVos && institutionDetailsVos.size() > 0) {
            for (InstitutionDetailsVo item : institutionDetailsVos) {
                List<BigDecimal> rateList = new ArrayList<>();
                List<BigDecimal>  billBondRateBig =new ArrayList<>();
                List<BigDecimal> creditBondRateBig =new ArrayList<>();
                if (item.getBankCode() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO.equals(item.getInstitutionType())) {
                    //分行处理逻辑
                    billBondRateBig = billBondRate(corpId, scoringStartDate, scoringEndDate, CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO, item.getBankCode());
                    creditBondRateBig = creditBondRate(corpId, scoringStartDate, scoringEndDate, CreditRatingConstant.INSTITUTION_TYPE_CODE_TWO, item.getBankCode());

                }
                if (item.getBankTypeId() != null && CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE.equals(item.getInstitutionType())) {
                    //总行处理逻辑
                    billBondRateBig = billBondRate(corpId, scoringStartDate, scoringEndDate, CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE, item.getBankTypeId());
                    creditBondRateBig = creditBondRate(corpId, scoringStartDate, scoringEndDate, CreditRatingConstant.INSTITUTION_TYPE_CODE_ONE, item.getBankTypeId());

                }
                if(null != billBondRateBig && billBondRateBig.size() > 0){
                    rateList.addAll(billBondRateBig);
                }
                if(null != creditBondRateBig && creditBondRateBig.size() > 0){
                    rateList.addAll(creditBondRateBig);
                }
                //判断集合是否有值
                if (null != rateList && rateList.size() > 0) {
                    //求平均值
                    BigDecimal resultBig = new BigDecimal("0");
                    for (BigDecimal rate : rateList) {
                        resultBig = resultBig.add(rate);
                    }
                    map.put(item.getInsId(), resultBig.divide(new BigDecimal(rateList.size()), 6, RoundingMode.HALF_UP));
                }else {
                    map.put(item.getInsId(), new BigDecimal("0"));
                }
            }
        }
        return map;
    }

    /**
     * 计算组织与机构之间的票据和信用证的保证金比例
     *
     * @param corpId
     * @param bankCode
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public BigDecimal countLcGuaranteeBillBond(String corpId, String bankCode, String scoringStartDate, String scoringEndDate, String bankType) {

        //1.获取票据保证金比例 FC_BOND_APPLY
        //1.1 获取银行保证金比例
        //2.获取信用证保证金比例
        return null;
    }

    /**
     * 获取承兑票据保证金比例
     *
     * @param corpId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<BigDecimal> billBondRate(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId) {
        //FC_BOND_APPLY.ITEM_ID  = ab_voucherinfo_vice.ID
        DataTablesOutput abVoucherInfoIds = lcGuaranteeBillBondService.findAbVoucherInfoId(corpId, scoringStartDate, scoringEndDate, bankType, bankId);

        List<BigDecimal> divideList = new ArrayList<>();
        if (null != abVoucherInfoIds && abVoucherInfoIds.getData().size() > 0) {
            List<CrBillBondRateDTO> data = abVoucherInfoIds.getData();

            //获取承兑票据保证金比例
            List<String> vvids = data.stream().map(CrBillBondRateDTO::getVvid).collect(Collectors.toList());
            List<FcBondApplyDTO> fcBondApplyByItemIds = fcBondHookService.getFcBondApplyByItemIdsType(vvids, FcBondConstant.ITEM_TYPE_BILL);
            for (FcBondApplyDTO item : fcBondApplyByItemIds) {
                divideList.add(item.getBondPercent());
            }
        }
        return divideList;
    }

    /**
     * 获取承兑票据保证金比例
     *
     * @param corpId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<BigDecimal> creditBondRate(String corpId, String scoringStartDate, String scoringEndDate, String bankType, String bankId) {

        List<BigDecimal> divideList = new ArrayList<>();
        List<LcEnrolDTO> lcEnrol = lcGuaranteeBillBondService.findLcEnrol(corpId, scoringStartDate, scoringEndDate, bankType, bankId);
        if (null != lcEnrol && lcEnrol.size() > 0){
            List<String> ids = lcEnrol.stream().map(LcEnrolDTO::getId).collect(Collectors.toList());
            List<FcBondApplyDTO> fcBondApplyByItemIds = fcBondHookService.getFcBondApplyByItemIdsType(ids,FcBondConstant.ITEM_TYPE_INTERNATIONAL);
            for (FcBondApplyDTO item : fcBondApplyByItemIds) {
                divideList.add(item.getBondPercent());
            }
        }
        return divideList;
    }
}
