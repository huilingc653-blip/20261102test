package com.bytter.increment.domain.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.increment.api.constant.ResourceConstant;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.vo.ChoProductDetailedShowVo;
import com.bytter.increment.domain.vo.ChoProductDetailedVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.increment.domain.vo.ChoRedeemDetailedVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.CorpConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @Title: Service
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Transactional
@Service("choProductDetailedService")
public class ChoProductDetailedService extends BaseService<ChoProductDetailedVo,String> {
    @Autowired
    private ChoProductReqService choProductReqService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Override
    public BaseDao<ChoProductDetailedVo, String> getEntityDao() {
        return null;
    }
    public List<ChoProductDetailedVo> listChoProductDetail(ICurrentUser user,ChoProductDetailedVo detailedVo) {
        List<ChoProductDetailedVo> listProduct = new ArrayList<>();
        List<String> corpIds = getCorpIdsByUser(user);
        List<ChoProductReqVo> choProductReqs = choProductReqService.listChoProducted(corpIds,detailedVo);
        for (ChoProductReqVo vo:choProductReqs){
            ChoProductDetailedVo choProductDetailedVo = BeanUtils.copyNewInstanceProperties(vo,ChoProductDetailedVo.class);
            choProductDetailedVo.setReqId(vo.getId());
            choProductDetailedVo.setCurrentStartDate(detailedVo.getCurrentStartDate());
            choProductDetailedVo.setCurrentEndDate(detailedVo.getCurrentEndDate());
            List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listProductRedeemApproved(choProductDetailedVo.getReqId());
            List<ChoRedeemDetailedVo> redeemDetaileds = redeemSetValue(choProductDetailedVo,productRedeems);
            choProductDetailedVo.setRedeemDetailedVos(redeemDetaileds);
            listProduct.add(choProductDetailedVo);
        }
        return listProduct;
    }
    public List<ChoProductDetailedShowVo> listChoRedeemDetail(ICurrentUser user, ChoProductDetailedVo detailedVo) {
        List<ChoProductDetailedShowVo> listRedeem = new ArrayList<>();
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.CHO_REPORT_CHOPRODUCTDETAILED_QUERY, user.getId());
        List<ChoProductReqVo> choProductReqs = choProductReqService.listChoProducted(corpIds,detailedVo);
        for (int i=0;i<choProductReqs.size();i++){
            ChoProductDetailedVo choProductDetailedVo = BeanUtils.copyNewInstanceProperties(choProductReqs.get(i),ChoProductDetailedVo.class);
            choProductDetailedVo.setOrderNumber(i+1);
            choProductDetailedVo.setReqId(choProductReqs.get(i).getId());
            choProductDetailedVo.setCurrentStartDate(detailedVo.getCurrentStartDate());
            choProductDetailedVo.setCurrentEndDate(detailedVo.getCurrentEndDate());
            List<ChoProductRedeemDetail> productRedeems = choProductRedeemService.listProductRedeemApproved(choProductDetailedVo.getReqId());
            List<ChoRedeemDetailedVo> redeemDetaileds = redeemSetValue(choProductDetailedVo,productRedeems);
            if (null == redeemDetaileds || redeemDetaileds.size() == 0){
                ChoProductDetailedShowVo redeemDetailedVo = new ChoProductDetailedShowVo();
                //封装产品登记记录
                BeanUtils.copyProperties(choProductDetailedVo, redeemDetailedVo);
                listRedeem.add(redeemDetailedVo);
            }else {
                for (ChoRedeemDetailedVo redeemVo : redeemDetaileds) {
                    ChoProductDetailedShowVo redeemDetailedVo = new ChoProductDetailedShowVo();
                    //封装赎回记录
                    BeanUtils.copyProperties(redeemVo, redeemDetailedVo);
                    //封装产品登记记录
                    BeanUtils.copyProperties(choProductDetailedVo, redeemDetailedVo);
                    listRedeem.add(redeemDetailedVo);
                }
            }
        }
        return listRedeem;
    }
    /**
     * 设置每条产品登记记录的赎回信息,并返回计算处理好的各项赎回值
     * @param productRedeems
     * @return
     */
    private List<ChoRedeemDetailedVo> redeemSetValue(ChoProductDetailedVo choProductDetailedVo, List<ChoProductRedeemDetail> productRedeems) {
        List<ChoRedeemDetailedVo> listRedeemDetails = new ArrayList<>();
        BigDecimal totalRedeemedMoney = BigDecimal.ZERO;//累计赎回金额
        BigDecimal totalRedeemedsIncomeMoney = BigDecimal.ZERO;//赎回实收总利息
        BigDecimal totalRedeemedsWeightAverageInvest = BigDecimal.ZERO;//赎回加权平均总投资金额
        BigDecimal currentTotalIncomeMoney = BigDecimal.ZERO;//当期已赎回的利息之和
        BigDecimal currentTotalRedeemedsWeightAverageInvest = BigDecimal.ZERO;//当期赎回加权平均投资金额之和
        // 如果是非保本浮动收益，没有参考收益率
        if (choProductDetailedVo.getReferIncomeRate() == null) {
            choProductDetailedVo.setReferIncomeRate(BigDecimal.ZERO);
        }
        for (ChoProductRedeemDetail redeem : productRedeems) {
            ChoRedeemDetailedVo detailedVo = new ChoRedeemDetailedVo();
            //赎回金额
            detailedVo.setRedeemedMoney(redeem.getRedeemMoney());
            //赎回日期
            detailedVo.setRedeemedDate(redeem.getRedeemDate());
            //赎回理财天数(赎回日期-起息日期)
            detailedVo.setRedeemedDay(calDayLimit(redeem.getRedeemDate(), choProductDetailedVo.getInterestStartDate()));
            //赎回理财权数(赎回的理财天数除以收益条款)
            detailedVo.setRedeemedWeight(BigDecimal.valueOf(detailedVo.getRedeemedDay() * 1.0f / choProductDetailedVo.getIncomeClause()).setScale(2, RoundingMode.HALF_UP));
            //应收利息(赎回金额*参考收益率*赎回理财权数)
            detailedVo.setRedeemedReferIncomeMoney(redeem.getRedeemMoney().multiply(choProductDetailedVo.getReferIncomeRate()).multiply(detailedVo.getRedeemedWeight()));
            //赎回实收利息
            detailedVo.setRedeemedIncomeMoney(redeem.getIncomeMoney());
            //赎回加权平均投资金额(赎回金额*赎回理财权数)
            detailedVo.setRedeemedWeightAverageInvest(redeem.getRedeemMoney().multiply(detailedVo.getRedeemedWeight()));
            //赎回年化收益率
            detailedVo.setRedeemedAnnualizedReturn(bigDivide(detailedVo.getRedeemedIncomeMoney(), detailedVo.getRedeemedWeightAverageInvest()));
            /*计算整个赎回的各项指标*/
            totalRedeemedMoney = totalRedeemedMoney.add(detailedVo.getRedeemedMoney());
            totalRedeemedsIncomeMoney = totalRedeemedsIncomeMoney.add(detailedVo.getRedeemedIncomeMoney());
            totalRedeemedsWeightAverageInvest = totalRedeemedsWeightAverageInvest.add(detailedVo.getRedeemedWeightAverageInvest());
            /**当期收益情况*/
            Date[] currentDate = calStartEndDay(choProductDetailedVo.getCurrentEndDate(), choProductDetailedVo.getCurrentStartDate(), redeem.getRedeemDate(), choProductDetailedVo.getInterestStartDate());
            Date calStartDate = currentDate[0];
            Date calEndDate = currentDate[1];
            if (calStartDate.before(calEndDate)) {
                //当期已赎回理财天数
                detailedVo.setCurrentDay(calDayLimit(calEndDate, calStartDate));
            }else{
                detailedVo.setCurrentDay(0);
            }
            //当期已赎回理财权数
            detailedVo.setCurrentWeight(BigDecimal.valueOf(detailedVo.getCurrentDay() * 1.0f / choProductDetailedVo.getIncomeClause()));
            //当期已赎回利息
            if(detailedVo.getCurrentDay().intValue() == 0){
                detailedVo.setCurrentIncomeMoney(BigDecimal.ZERO);
            }else {
                detailedVo.setCurrentIncomeMoney(redeem.getIncomeMoney().multiply(BigDecimal.valueOf(detailedVo.getCurrentDay() * 1.0f / detailedVo.getRedeemedDay())));
            }
            //当期已赎回加权平均投资金额
            detailedVo.setCurrentRedeemedWeightAverageInvest(redeem.getRedeemMoney().multiply(detailedVo.getCurrentWeight()));
            /*计算当期赎回的各项指标合计*/
            currentTotalIncomeMoney = currentTotalIncomeMoney.add(detailedVo.getCurrentIncomeMoney());
            currentTotalRedeemedsWeightAverageInvest = currentTotalRedeemedsWeightAverageInvest.add(detailedVo.getCurrentRedeemedWeightAverageInvest());
            //设置最近一条赎回日期,因为赎回记录已经排序,循环设置到最后一条
            choProductDetailedVo.setLastRedeemedDate(redeem.getRedeemDate());
            listRedeemDetails.add(detailedVo);
        }
        //登记产品的已赎回金额
        choProductDetailedVo.setRedeemedsMoney(totalRedeemedMoney);
        //登记产品的待赎回金额
        choProductDetailedVo.setWaitRedeemMoney(choProductDetailedVo.getBuyMoney().subtract(choProductDetailedVo.getRedeemedsMoney()));
        //登记产品已赎回的累计利息
        choProductDetailedVo.setRedeemedsIncomeMoney(totalRedeemedsIncomeMoney);
        //登记产品已赎回的加权平均总投资金额
        choProductDetailedVo.setRedeemedsWeightAverageInvest(totalRedeemedsWeightAverageInvest);
        //累计赎回年化收益率
        choProductDetailedVo.setRedeemedsAnnualizedReturn(bigDivide(choProductDetailedVo.getRedeemedsIncomeMoney(), choProductDetailedVo.getRedeemedsWeightAverageInvest()));
        //当期赎回年化收益率(当期已赎回的利息之和/当期赎回加权平均投资金额之和)
        choProductDetailedVo.setCurrentRedeensAnnualizedReturn(bigDivide(currentTotalIncomeMoney, currentTotalRedeemedsWeightAverageInvest));
        //待赎回还有金额时才进行待赎回相关参数的求值
        if (choProductDetailedVo.getWaitRedeemMoney().compareTo(BigDecimal.ZERO) > 0) {
            //当前待赎回理财天数
            Date[] currentDate = calStartEndDay(choProductDetailedVo.getCurrentEndDate(), choProductDetailedVo.getCurrentStartDate(),
                    choProductDetailedVo.getDeadlineDate(), choProductDetailedVo.getInterestStartDate());
            choProductDetailedVo.setCurrentWaitRedeemDay(calDayLimit(currentDate[1], currentDate[0]));
        }else{
            choProductDetailedVo.setCurrentWaitRedeemDay(0);
        }
        //当前待赎回理财权数当前待赎回理财权数(当期待赎回理财天数/收益条款)
        choProductDetailedVo.setCurrentWaitRedeemWeight(bigDivide(BigDecimal.valueOf(choProductDetailedVo.getCurrentWaitRedeemDay())
                , BigDecimal.valueOf(choProductDetailedVo.getIncomeClause())));
        //当期待赎回利息(待赎回金额*参考收益率*当期待赎回理财权数)
        choProductDetailedVo.setCurrentWaitRedeemIncomeMoney(choProductDetailedVo.getWaitRedeemMoney().multiply(choProductDetailedVo.getReferIncomeRate()).multiply(choProductDetailedVo.getCurrentWaitRedeemWeight()));
        //当期待赎回加权平均投资金额(待赎回金额*当期待赎回理财权数)
        choProductDetailedVo.setCurrentWaitRedeemWeightAverageInvest(choProductDetailedVo.getWaitRedeemMoney().multiply(choProductDetailedVo.getCurrentWaitRedeemWeight()));
        //当期待赎回年化收益率(当期待赎回利息/当期待赎回加权平均投资金额)
        choProductDetailedVo.setCurrentWaitRedeemAnnualizedReturn(bigDivide(choProductDetailedVo.getCurrentWaitRedeemIncomeMoney(), choProductDetailedVo.getCurrentWaitRedeemWeightAverageInvest()));
        //当期理财利息
        if (null != choProductDetailedVo.getCurrentWaitRedeemIncomeMoney()) {
            choProductDetailedVo.setCurrentReferIncomeRate(currentTotalIncomeMoney.add(choProductDetailedVo.getCurrentWaitRedeemIncomeMoney()));
        } else {
            choProductDetailedVo.setCurrentReferIncomeRate(currentTotalIncomeMoney);
        }
        //当期理财加权平均投资金额
        if (null != choProductDetailedVo.getCurrentWaitRedeemWeightAverageInvest()) {
            choProductDetailedVo.setCurrentWeightAverageInvest(currentTotalRedeemedsWeightAverageInvest. add(choProductDetailedVo.getCurrentWaitRedeemWeightAverageInvest()));
        } else {
            choProductDetailedVo.setCurrentWeightAverageInvest(currentTotalRedeemedsWeightAverageInvest);
        }
        //当期年化收益率
        choProductDetailedVo.setCurrentAnnualizedReturn(bigDivide(choProductDetailedVo.getCurrentReferIncomeRate(),  choProductDetailedVo.getCurrentWeightAverageInvest()));
        return listRedeemDetails;
    }
    /**
     * 根据当前用户属于网点或者单位获取该网点下面的所以单位
     * @param user
     * @return
     */
    private List<String> getCorpIdsByUser(ICurrentUser user) {
        List<String> corpIds = new ArrayList<>();
        CorpDTO corpDTO = corpService.get(user.getCorpId());
        corpIds.add(corpDTO.getId());
        if (!EmptyUtils.isEmpty(corpDTO) && CorpConstant.CORP_TYPE_CODE_NET.equals(corpDTO.getType())){
            List<CorpDTO> corpDTOS = corpService.listCorpByNetId(corpDTO.getId());
            for (CorpDTO corp:corpDTOS){
                corpIds.add(corp.getId());
            }
        }
        return corpIds;
    }
    /**
     * 计算两个日期间的间隔天数
     * @param endDate
     * @param startDate
     * @return
     */
    private Integer calDayLimit(Date endDate, Date startDate) {
        return DateUtils.dayDiffs(endDate,startDate);
    }
    /**
     * 限制截止日期大于登记产品的到期日期,小于产品的起息日期时,进行最大值最小值限制
     * @param endDate 查询的结束日期
     * @param startDate 查询的开始日期
     * @param maxDate 产品的到期日期
     * @param minDate 产品的起息日期
     * @return Date[2]:Date[0] 计算开始日期,Date[1] 计算结束日期
     */
    private Date[] calStartEndDay(Date endDate,Date startDate,Date maxDate,Date minDate) {
        Date[] date = new Date[2];
        //当产品的到期日期为空时,所以计算按查询结束日期计算
        if (null == maxDate){
            maxDate = endDate;
        }
        if (endDate.after(maxDate)){
            date[1] = maxDate;
        }else{
            date[1] = endDate;
        }
        if (startDate.before(minDate)){
            date[0] = minDate;
        }else{
            date[0] = startDate;
        }
        return date;
    }
    /**
     * BigDecimal 的除法运算,
     * @param num1  被除数
     * @param num2  除数
     * @return
     */
    public BigDecimal bigDivide(BigDecimal num1,BigDecimal num2){
        if (num2.compareTo(BigDecimal.ZERO) == 0){
            return BigDecimal.ZERO;
        }
        return num1.divide(num2,8, BigDecimal.ROUND_HALF_UP);
    }
}
