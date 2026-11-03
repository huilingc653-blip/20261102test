package com.bytter.report.domain.cms.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.report.api.service.IBiRemainTermService;
import com.bytter.report.domain.cms.dao.CmsExpireAnalysisDao;
import com.bytter.report.domain.cms.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.report.api.dto.BiRemainTermDTO;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.system.api.*;
import com.github.abel533.echarts.*;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */

@SuppressWarnings("rawtypes")
@Service("cmsExpireAnalysisService")
@Transactional
public class CmsExpireAnalysisService extends BaseService {

    @Autowired(required = false)
    private IBiRemainTermService iBiRemainTermService;

    @Autowired
    private CmsExpireAnalysisDao cmsExpireAnalysisDao;

    @Resource
    private IMultiLangService multiLangServiceI;

    @DubboReference(check = false)
    private IDictService dictService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;

    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 返回报表数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput getCmsLoanDataTable(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        List<String> corpIds = new ArrayList<>();
        if (null != params.get("corpId") && StringUtils.isNotEmpty(params.get("corpId").toString())) {
            corpIds.addAll(Arrays.asList(params.get("corpId").toString().split(",")));
        } else {
            corpIds.addAll(corpService.getUserCorpIds(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY, user.getId()));
        }
        DataTablesOutput output = cmsExpireAnalysisDao.getCmsLoanDataTable(createSql(params), input, corpIds);
        List<CmsExpireAnalysisVO> list = output.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(CmsExpireAnalysisVO vo : list){
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
//        折算金额
        List<CmsExpireAnalysisVO> cmsExpireAnalysisVOS = convertMoney(output.getData(), params);
        output.setData(cmsExpireAnalysisVOS);
        return output;
    }


    /**
    *app报表查询
    *@return
    */
    public List<CmsExpireAnalysisVO> getMobileCmsLoanDataTable(Map<String, Object> params, ICurrentUser user){
        List<String> corpIds = new ArrayList<>();
        if (params != null && null != params.get("corpId") && StringUtils.isNotEmpty(params.get("corpId").toString())) {
            corpIds.addAll(Arrays.asList(params.get("corpId").toString().split(",")));
        } else {
            corpIds.addAll(corpService.getUserCorpIds(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY, user.getId()));
        }
        List<CmsExpireAnalysisVO> cmsExpireAnalysisVOS = convertMoney(cmsExpireAnalysisDao.getMobileCmsLoanDataTable(createSql(params),params,corpIds),params);
        List<String> curIds = cmsExpireAnalysisVOS.stream().map(CmsExpireAnalysisVO::getCurId).collect(Collectors.toList());
        Map<String, String> curMap = currencyService.getCurrencyNameByIds(curIds);
        String loanTypeName = null;
        if (ObjectUtils.isNotEmpty(params) && ObjectUtils.isNotEmpty(params.get("loanType"))){
            loanTypeName = dictService.getByName("Loan.Sign", params.get("loanType").toString());
        }

        List<DictDTO> list = dictService.list("Loan.Sign");
        Map<String, DictDTO> dictMap = list.stream().collect(Collectors.toMap(DictDTO::getCode, Function.identity(), (a, b) -> a));
        for (CmsExpireAnalysisVO a : cmsExpireAnalysisVOS) {
            a.setCurName(curMap.get(a.getCurId()));
            if (StringUtils.isNotBlank(loanTypeName)){
                a.setLoanTypeName(loanTypeName); //这里用于条件 融资种类筛选
            }
            if (StringUtils.isBlank(a.getLoanTypeName())){
                a.setLoanTypeName(dictMap.get(a.getLoanType()).getName());
            }
        }

        CmsExpireAnalysisVO cmsExpireAnalysisVOTotal = new CmsExpireAnalysisVO();
        cmsExpireAnalysisVOTotal.setMoney(BigDecimal.ZERO);
        cmsExpireAnalysisVOTotal.setConvertMoney(BigDecimal.ZERO);
        cmsExpireAnalysisVOTotal.setCorpName("合计");
        for (CmsExpireAnalysisVO cmsExpireAnalysisVO : cmsExpireAnalysisVOS) {
            cmsExpireAnalysisVOTotal.setConvertMoney(cmsExpireAnalysisVOTotal.getConvertMoney().add(cmsExpireAnalysisVO.getConvertMoney()));
            cmsExpireAnalysisVOTotal.setMoney(cmsExpireAnalysisVOTotal.getMoney().add(cmsExpireAnalysisVO.getMoney()));
        }
        cmsExpireAnalysisVOS.add(cmsExpireAnalysisVOTotal);

        return cmsExpireAnalysisVOS;
    }

    /**
    *折算为指定币种金额
    *@Param cmsExpireAnalysisVOS
    *@Param isConvert
    *@Param convertCurId
    *@return
    */
    public List<CmsExpireAnalysisVO> convertMoney(List<CmsExpireAnalysisVO> cmsExpireAnalysisVOS, Map<String, Object> params) {
        boolean isConvert = false;
//        折合币种
        String convertCurId = "";
        /**
         * 是否折合本位币
         */
        if (null != params && null != params.get("isConvert") && StringUtils.isNotEmpty(params.get("isConvert").toString())) {
            if (CmsConstant.IS_TRUE.equals(params.get("isConvert").toString())) {
                isConvert = true;
            }
        }
        if (null != params && null != params.get("convertCurId") && StringUtils.isNotEmpty(params.get("convertCurId").toString())) {
            convertCurId = params.get("convertCurId").toString();
        }
        for (CmsExpireAnalysisVO cmsExpireAnalysisVO : cmsExpireAnalysisVOS) {
            if (StringUtils.isNotEmpty(convertCurId)) {
                BigDecimal exchangeRate = BigDecimal.ONE;
                if (StringUtils.isNotEmpty(cmsExpireAnalysisVO.getCurId()) && StringUtils.isNotEmpty(convertCurId) && isConvert) {
//                折合汇率
                    exchangeRate = fcExcrateAdjustService.getExchangeRateBy(cmsExpireAnalysisVO.getCurId(), convertCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
//                    exchangeRate = new BigDecimal(7.3);
                }
                if (isConvert) {
                    //cmsExpireAnalysisVO.setMoney(cmsExpireAnalysisVO.getMoney().multiply(exchangeRate));
                    cmsExpireAnalysisVO.setConvertMoney(cmsExpireAnalysisVO.getConvertMoney().multiply(exchangeRate));
                } else {
                    cmsExpireAnalysisVO.setConvertMoney(cmsExpireAnalysisVO.getMoney().multiply(exchangeRate));
                }
            }
        }
        return cmsExpireAnalysisVOS;
    }

    /**
     * 内部贷款明细
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public DataTablesOutput getCmsLoanDtl(String corpId, String remainTermId, String curId){
        DataTablesOutput dataTablesOutput = cmsExpireAnalysisDao.getCmsLoanDtl(corpId, curId, getSqlCondition(remainTermId, "cls.repay_date"));
        List<CmsLoanDtlVO> list = dataTablesOutput.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(CmsLoanDtlVO vo : list){
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 委托贷款明细
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public DataTablesOutput getCmsPrecativeDtl(String corpId, String remainTermId, String curId){
        DataTablesOutput dataTablesOutput = cmsExpireAnalysisDao.getCmsPrecativeDtl(corpId, curId, getSqlCondition(remainTermId, "cpls.deadline_date"));
        List<CmsPrecativeDtlVO> list = dataTablesOutput.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(CmsPrecativeDtlVO vo : list){
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 银行借款明细（移动端）
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public List<CmsProvideDtlVO> getMobilePrivodeDtl(String corpId, String remainTermId, String curId){
        List<CmsProvideDtlVO> cmsProvideDtlVOS = cmsExpireAnalysisDao.getMobileProvideDtl(corpId, curId, getSqlCondition(remainTermId, "cpls.repay_date"));
        CmsProvideDtlVO cmsProvideDtlVOTotal = new CmsProvideDtlVO();
        cmsProvideDtlVOTotal.setBankName("合计");
        cmsProvideDtlVOTotal.setMoney(BigDecimal.ZERO);
        cmsProvideDtlVOTotal.setSendMoney(BigDecimal.ZERO);
        cmsProvideDtlVOTotal.setRepayMoney(BigDecimal.ZERO);
        cmsProvideDtlVOTotal.setLeftMoney(BigDecimal.ZERO);

        List<String> curIds = cmsProvideDtlVOS.stream().map(CmsProvideDtlVO::getCurId).collect(Collectors.toList());
        Map<String, String> curMap = currencyService.getCurrencyNameByIds(curIds);

        for (CmsProvideDtlVO cmsProvideDtlVO : cmsProvideDtlVOS) {
            cmsProvideDtlVOTotal.setMoney(cmsProvideDtlVOTotal.getMoney().add(cmsProvideDtlVO.getMoney()));
            cmsProvideDtlVOTotal.setSendMoney(cmsProvideDtlVOTotal.getSendMoney().add(cmsProvideDtlVO.getSendMoney()));
            cmsProvideDtlVOTotal.setRepayMoney(cmsProvideDtlVOTotal.getRepayMoney().add(cmsProvideDtlVO.getRepayMoney()));
            cmsProvideDtlVOTotal.setLeftMoney(cmsProvideDtlVOTotal.getLeftMoney().add(cmsProvideDtlVO.getLeftMoney()));
            cmsProvideDtlVO.setCurName(curMap.get(cmsProvideDtlVO.getCurId()));
            cmsProvideDtlVOTotal.setCurName(curMap.get(cmsProvideDtlVO.getCurId()));
        }
        cmsProvideDtlVOS.add(cmsProvideDtlVOTotal);
        return cmsProvideDtlVOS;
    }

    /**
     * 内部贷款明细（移动端）
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public List<CmsLoanDtlVO> getMobileLoanDtl(String corpId, String remainTermId, String curId){
        List<CmsLoanDtlVO> cmsLoanDtlVOS = cmsExpireAnalysisDao.getMobileLoanDtl(corpId, curId, getSqlCondition(remainTermId, "cls.repay_date"));
        CmsLoanDtlVO cmsLoanDtlVOTotal = new CmsLoanDtlVO();
        cmsLoanDtlVOTotal.setBillCode("合计");
        cmsLoanDtlVOTotal.setMoney(BigDecimal.ZERO);
        cmsLoanDtlVOTotal.setSendMoney(BigDecimal.ZERO);
        cmsLoanDtlVOTotal.setRepayingMoney(BigDecimal.ZERO);
        cmsLoanDtlVOTotal.setLeftMoney(BigDecimal.ZERO);

        List<String> curIds = cmsLoanDtlVOS.stream().map(CmsLoanDtlVO::getCurId).collect(Collectors.toList());
        Map<String, String> curMap = currencyService.getCurrencyNameByIds(curIds);
        for (CmsLoanDtlVO cmsLoanDtlVO : cmsLoanDtlVOS) {
            cmsLoanDtlVOTotal.setMoney(cmsLoanDtlVOTotal.getMoney().add(cmsLoanDtlVO.getMoney()));
            cmsLoanDtlVOTotal.setSendMoney(cmsLoanDtlVOTotal.getSendMoney().add(cmsLoanDtlVO.getSendMoney()));
            cmsLoanDtlVOTotal.setRepayingMoney(cmsLoanDtlVOTotal.getRepayingMoney().add(cmsLoanDtlVO.getRepayingMoney()));
            cmsLoanDtlVOTotal.setLeftMoney(cmsLoanDtlVOTotal.getLeftMoney().add(cmsLoanDtlVO.getLeftMoney()));
            cmsLoanDtlVO.setCurName(curMap.get(cmsLoanDtlVO.getCurId()));
            cmsLoanDtlVOTotal.setCurName(curMap.get(cmsLoanDtlVO.getCurId()));
        }
        cmsLoanDtlVOS.add(cmsLoanDtlVOTotal);
        return cmsLoanDtlVOS;
    }

    /**
     * 委托贷款明细（移动端）
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public List<CmsPrecativeDtlVO> getMobilePrecativeDtl(String corpId, String remainTermId, String curId){
        List<CmsPrecativeDtlVO> cmsPrecativeDtlVOS = cmsExpireAnalysisDao.getMobilePrecativeDtl(corpId, curId, getSqlCondition(remainTermId, "cpls.deadline_date"));
        CmsPrecativeDtlVO cmsPrecativeDtlVOTotal = new CmsPrecativeDtlVO();
        cmsPrecativeDtlVOTotal.setBillCode("合计");
        cmsPrecativeDtlVOTotal.setMoney(BigDecimal.ZERO);
        cmsPrecativeDtlVOTotal.setSendMoney(BigDecimal.ZERO);
        cmsPrecativeDtlVOTotal.setRepayMoney(BigDecimal.ZERO);
        cmsPrecativeDtlVOTotal.setLeftMoney(BigDecimal.ZERO);

        List<String> curIds = cmsPrecativeDtlVOS.stream().map(CmsPrecativeDtlVO::getCurId).collect(Collectors.toList());
        Map<String, String> curMap = currencyService.getCurrencyNameByIds(curIds);

        for (CmsPrecativeDtlVO cmsPrecativeDtlVO : cmsPrecativeDtlVOS) {
            cmsPrecativeDtlVOTotal.setMoney(cmsPrecativeDtlVOTotal.getMoney().add(cmsPrecativeDtlVO.getMoney()));
            cmsPrecativeDtlVOTotal.setSendMoney(cmsPrecativeDtlVOTotal.getSendMoney().add(cmsPrecativeDtlVO.getSendMoney()));
            cmsPrecativeDtlVOTotal.setRepayMoney(cmsPrecativeDtlVOTotal.getRepayMoney().add(cmsPrecativeDtlVO.getRepayMoney()));
            cmsPrecativeDtlVOTotal.setLeftMoney(cmsPrecativeDtlVOTotal.getLeftMoney().add(cmsPrecativeDtlVO.getLeftMoney()));
            cmsPrecativeDtlVO.setCurName(curMap.get(cmsPrecativeDtlVO.getCurId()));
            cmsPrecativeDtlVOTotal.setCurName(curMap.get(cmsPrecativeDtlVO.getCurId()));
        }
        cmsPrecativeDtlVOS.add(cmsPrecativeDtlVOTotal);
        return cmsPrecativeDtlVOS;
    }

    /**
     * 银行借款明细
     * @param corpId
     * @param remainTermId
     * @param curId
     * @return
     */
    public DataTablesOutput getCmsPrivodeDtl(String corpId, String remainTermId, String curId){
        DataTablesOutput dataTablesOutput = cmsExpireAnalysisDao.getCmsProvideDtl(corpId, curId, getSqlCondition(remainTermId, "cpls.repay_date"));
        List<CmsProvideDtlVO> list = dataTablesOutput.getData();
        if(!CollectionUtils.isEmpty(list)){
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
            for(CmsProvideDtlVO vo : list){
                if(null != currencyMap){
                    if(currencyMap.containsKey(vo.getCurId())){
                        BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                        if(null != btCurrencyDTO){
                            vo.setCurName(btCurrencyDTO.getCurName());
                        }
                    }
                }
            }
        }
        return dataTablesOutput;
    }

    /**
     * 条件拼接
     * @param remainTermId
     * @param field
     * @return
     */
    private String getSqlCondition(String remainTermId,String field){
        List<BiRemainTermDTO> biRemainTermDTOS  = iBiRemainTermService.getBiRemainTermDTOs(field);
        String sqlCondition = "";
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            if (biRemainTermDTO.getId().equals(remainTermId)) {
                sqlCondition = biRemainTermDTO.getSearchSql();
            }
        }
        return sqlCondition;
    }

    /**
     * 获取剩余期限
     * @return
     */
    public List<BiRemainTermDTO> getBiRemainTerm(){
        return iBiRemainTermService.getBiRemainTermDTOList();
    }

    /**
     * sql查询语句构建
     * @return
     */
    private StringBuilder createSql(Map<String, Object> params){
//        Map<String, Object> params =input.getFormdata();
        String isInclude = " status = 102 ";
        String moneyType = "1";
        boolean isConvert = false;
//        是否包含在途金额
        if (params != null) {
            if (null != params.get("isInclude") && StringUtils.isNotEmpty(params.get("isInclude").toString())) {
                if (CmsConstant.IS_FALSE.equals(params.get("isInclude").toString())) {
                    isInclude = " status >= 11 ";
                }
            }
            /**
             * 金额单位
             */
            if (null != params.get("moneyType") && StringUtils.isNotEmpty(params.get("moneyType").toString())) {
                moneyType = params.get("moneyType").toString();
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" select corp.name    corpName,");
        sql.append("        cmsLoanInfo.corpId corpId,");
        sql.append("        cmsLoanInfo.curId curId,");
//        sql.append("        cur.cur_name curName,");
        sql.append("        loanType,");
        sql.append("        brt.term_name expireAnalysis,");
        sql.append("        brt.id remainTermId, ");
//        金额单位
        if (CmsConstant.MONEY_TYPE_1000.equals(moneyType)) {
            sql.append("       (money/1000) money,");
//            sql.append("       (convertMoney/1000)  convertMoney");
            sql.append("       (money/1000)  convertMoney");
        } else if (CmsConstant.MONEY_TYPE_10000.equals(moneyType)) {
            sql.append("       (money/10000) money,");
//            sql.append("       convertMoney/10000 convertMoney");
            sql.append("       (money/10000) convertMoney");
        } else if (CmsConstant.MONEY_TYPE_100000000.equals(moneyType)) {
            sql.append("       (money/100000000) money,");
//            sql.append("       (convertMoney/100000000) convertMoney ");
            sql.append("       (money/100000000) convertMoney ");
        } else{
            sql.append("       money,");
//            sql.append("       convertMoney");
            sql.append("       money convertMoney");
        }
        sql.append(" from ( ");
        sql.append("        select loanInfo.corpId ,");
        sql.append("               loanInfo.curId,");
        sql.append("               loanType,");
        sql.append("               BiRemainTermId expireAnalysis,");
//        是否折合本位币
/*        if (isConvert) {
            sql.append("               sum(leftMoney * bt_nvl_num(newExchangeRate,1)) as money,");
        } else {
            sql.append("               sum(leftMoney) as money,");
        }*/
        sql.append("               sum(leftMoney) as money");
//        sql.append("               sum(leftMoney * bt_nvl_num(newExchangeRate,1)) as convertMoney");
        sql.append("          from (");
        sql.append(createProvideSql(isInclude));
        sql.append("         union ");
        sql.append(createPrecativeSql(isInclude));
        sql.append("         union ");
        sql.append(createLoanSql(isInclude));
        sql.append("          ) loanInfo");
/*        sql.append("          left join (");
        sql.append(getCommonExchangeRate());
        sql.append("          ) fea on loanInfo.curId = fea.curId");*/
        sql.append("         group by BiRemainTermId,loanType,loanInfo.corpId,loanInfo.curId");
        sql.append(" ) cmsLoanInfo ");
        sql.append("  left join sys_corp corp");
        sql.append("    on cmsLoanInfo.corpId = corp.id");
//        sql.append("  left join bt_currency cur");
//        sql.append("    on cmsLoanInfo.curId = cur.id");
        sql.append("  left join bi_remain_term brt");
        sql.append("    on cmsLoanInfo.expireAnalysis = brt.id");
        sql.append(" where 1 = 1  ");
        return sql;
    }

    /**
     * 构建银行借款sql
     * @return
     */
    private StringBuilder createProvideSql(String isInclude) {
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOs("cpls.repay_date");
        StringBuilder sql = new StringBuilder();
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            if (StringUtils.isNotEmpty(sql)) {
                sql.append(" union ");
            }
            sql.append("select cpls.corp_id corpId, ");
            sql.append("       '02' as loanType, ");
            sql.append("       '"+biRemainTermDTO.getId()+"' as BiRemainTermId,");
/*            sql.append("        case ");
            sql.append("when " + biRemainTermDTO.getSearchSql() + " then '" + biRemainTermDTO.getId() + "'");
            sql.append("        end as BiRemainTermId, ");*/
            sql.append("       cpls.cur_code as curId, ");
            sql.append("       bt_nvl_num((cpls.money - repay_info.repayMoney),cpls.money) leftMoney ");
            sql.append("  from cms_provide_loan_send cpls ");
            sql.append("  left join (select bt_nvl_num(sum(REPAY_MONEY),0) repayMoney, send_code ");
            sql.append("               from CMS_PROVIDE_LOAN_REPAY t ");
            sql.append("              where ");
            sql.append(isInclude);
            sql.append("              group by send_code) repay_info ");
            sql.append("    on cpls.bill_code = repay_info.send_code ");
            sql.append("  where cpls.status = 102 ");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())){
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
        }
        return sql;
    }

    /**
     * 构建委托贷款sql
     * @return
     */
    private StringBuilder createPrecativeSql(String isInclude) {
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOs("cpls.deadline_date");
        StringBuilder sql = new StringBuilder();
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            if (StringUtils.isNotEmpty(sql)) {
                sql.append(" union ");
            }
            sql.append("select cpli.LOAN_OPP corpId, ");
            sql.append("       '03' as loanType, ");
            sql.append("       '" + biRemainTermDTO.getId() + "' as BiRemainTermId, ");
/*            sql.append("        case ");
            sql.append("when " + biRemainTermDTO.getSearchSql() + " then '" + biRemainTermDTO.getId() + "'");
            sql.append("        end as BiRemainTermId, ");*/
            sql.append("       cpli.cur_id curId, ");
            sql.append("       bt_nvl_num((cpls.money - repay_info.repayMoney),cpls.money) as leftMoney ");
            sql.append("  from CMS_PRECATIVE_LOAN_SEND cpls ");
            sql.append("  left join CMS_PRECATIVE_LOAN_INFO cpli ");
            sql.append("    on cpls.father_code = cpli.bill_code ");
            sql.append("  left join (select bt_nvl_num(sum(REPAY_MONEY),0) repayMoney, send_code ");
            sql.append("               from CMS_PRECATIVE_LOAN_REPAY ");
            sql.append("              where ");
            sql.append(isInclude);
            sql.append("              group by send_code) repay_info ");
            sql.append("    on cpls.bill_code = repay_info.send_code ");
            sql.append(" where cpls.status = 102 ");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())){
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
        }
        return sql;
    }


    /**
     * 构建内部贷款sql
     * @return
     */
    private StringBuilder createLoanSql(String isInclude) {
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOs("cls.repay_date");
        StringBuilder sql = new StringBuilder();
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            if (StringUtils.isNotEmpty(sql)) {
                sql.append(" union ");
            }
            sql.append("select cls.corp_id, ");
            sql.append("       '01' as loanType, ");
            sql.append("       '" + biRemainTermDTO.getId() + "' as BiRemainTermId, ");
/*            sql.append("        case ");
            sql.append("when " + biRemainTermDTO.getSearchSql() + " then '" + biRemainTermDTO.getId() + "'");
            sql.append("        end as BiRemainTermId, ");*/
            sql.append("       cls.cur_code curId, ");
            sql.append("       bt_nvl_num((cls.loan_money - repay_info.repayMoney), cls.loan_money) as leftMoney ");
            sql.append("  from CMS_LOAN_SEND cls ");
            sql.append("  left join (select bt_nvl_num(sum(repay_money), 0) repayMoney, send_code ");
            sql.append("               from CMS_LOAN_REPAY ");
            sql.append("              where ");
            sql.append(isInclude);
            sql.append("              group by send_code) repay_info ");
            sql.append("    on cls.bill_code = repay_info.send_code ");
            sql.append(" where cls.status = 102 ");
            if (ObjectUtil.isNotEmpty(biRemainTermDTO.getSearchSql())){
                sql.append(" and ").append(biRemainTermDTO.getSearchSql());
            }
        }
        return sql;
    }

    /**
     * 构建柱状图option
     * @param isInclude
     * @param curId
     * @param moneyTyp
     * @param corpIds
     * @return
     */
    public String getBarOption(String isInclude, String curId, String convertCurId, String moneyTyp, List<String> corpIds, String isConvert) {
        return createBarOption(isInclude, curId, convertCurId, moneyTyp, corpIds, isConvert);
    }

    /**
     * 构建饼图option
     *
     * @param isInclude
     * @param curId
     * @param moneyTyp
     * @param corpIds
     * @return
     */
    public String getPieOption(String isInclude, String curId, String convertCurId, String moneyTyp, List<String> corpIds, String isConvert) {
        return createPieOption(isInclude, curId, convertCurId, moneyTyp, corpIds, isConvert);
    }

    private String createPieOption(String isInclude, String curId, String convertCurId, String moneyType, List<String> corpIds, String isConvert) {
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOList();
        //        内部贷款
        List<BigDecimal> loans = getbarData("cls.repay_date", isInclude, curId, convertCurId, moneyType, "1", corpIds, isConvert);
//        委托贷款
        List<BigDecimal> Precatives = getbarData("cpls.deadline_date", isInclude, curId, convertCurId, moneyType, "2", corpIds, isConvert);
//        银行借款
        List<BigDecimal> provides = getbarData("cpls.repay_date", isInclude, curId, convertCurId, moneyType, "3", corpIds, isConvert);

        Option option = new Option();
        List<Series> series = new ArrayList<Series>();
//        设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.setShow(true);
        toolbox.feature(Tool.saveAsImage, Tool.restore, null);
        Tooltip tooltip = new Tooltip();
        option.setToolbox(toolbox);
        option.setTooltip(tooltip);
        //        设置图例
//        List<Data> datas = new ArrayList<>();
        Data[] datas = new Data[biRemainTermDTOS.size()];
        List<String> legends = new ArrayList<>();
        for (int i = 0; i < biRemainTermDTOS.size(); i++) {
            legends.add(biRemainTermDTOS.get(i).getTermName());
            com.github.abel533.echarts.data.Data data = new Data();
            data.setName(biRemainTermDTOS.get(i).getTermName());
            data.setValue(loans.get(i).add(Precatives.get(i)).add(provides.get(i)));
            datas[i] = data;
        }
        Legend legend = new Legend();
        legend.setData(legends);
        legend.setOrient(Orient.vertical);
        legend.setTop("30%");
        legend.setLeft("91%");
        option.setLegend(legend);

        Pie pie = new Pie("");
        pie.setType(SeriesType.pie);
        pie.radius("75%");
        String center[] = {"50%", "60%"};
        pie.setCenter(center);
        pie.data(datas);
        series.add(pie);
        option.setSeries(series);

        //        标题设置
        Title title = new Title();
        title.setLeft("10%");
        title.setShow(true);
        String titleStr = multiLangServiceI.getLang("table.remainTerm.unit") + ":" + dictService.get("cms.MoneyType", moneyType);
        String curName;
        if (CmsConstant.IS_TRUE.equals(isConvert)) {
            curName = currencyService.getObjectById(convertCurId).getCurName();
        } else {
            curName = currencyService.getObjectById(curId).getCurName();
        }
//            curName = currencyService.getObjectById(curId).getCurName();
        titleStr += "\t      " + multiLangServiceI.getLang("table.ratBkrationRelief.curCode") + ":" + curName;
        title.setText(titleStr);
        option.setTitle(title);
        return JsonUtils.toJSONString(option);
    }


    /**
     * 构建option
     * @param isInclude
     * @param curId
     * @param moneyType
     * @param corpIds
     * @return
     */
    private String createBarOption(String isInclude,String curId, String convertCurId,String moneyType,List<String> corpIds, String isConvert){
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOList();
        Option option = new Option();
        List<Series> series = new ArrayList<Series>();
        List<Bar> bars = new ArrayList<>();
//        设置工具栏
        Toolbox toolbox = new Toolbox();
        toolbox.setShow(true);
        toolbox.feature(Tool.saveAsImage, Tool.restore, null);
        Tooltip tooltip = new Tooltip();
        option.setToolbox(toolbox);
        option.setTooltip(tooltip);
//        设置图例
        List<String> legends = new ArrayList<>();
//        内部贷款
        legends.add(multiLangServiceI.getLang("cms.report.loan"));
//        委托贷款
        legends.add(multiLangServiceI.getLang("cms.loan.precative"));
//        银行借款
        legends.add(multiLangServiceI.getLang("cms.report.loan.provide"));
        Legend legend = new Legend();
        legend.setData(legends);
        legend.setOrient(Orient.vertical);
        legend.setTop("30%");
        legend.setLeft("91%");
        option.setLegend(legend);
//        标题设置
        Title title = new Title();
        title.setLeft("10%");
        title.setShow(true);
        String titleStr = multiLangServiceI.getLang("table.remainTerm.unit") + ":" + dictService.get("cms.MoneyType", moneyType);
        String curName;
        if (CmsConstant.IS_TRUE.equals(isConvert)) {
            curName = currencyService.getObjectById(convertCurId).getCurName();
        } else {
            curName = currencyService.getObjectById(curId).getCurName();
        }
//            curName = currencyService.getObjectById(curId).getCurName();
        titleStr += "\t      "+multiLangServiceI.getLang("table.ratBkrationRelief.curCode") + ":" + curName;
        title.setText(titleStr);
        option.setTitle(title);
//        设置X轴坐标
        List xAxisStr = new ArrayList();
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            xAxisStr.add(biRemainTermDTO.getTermName());
        }
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setData(xAxisStr);
//        设置X轴和Y轴
        option.xAxis(xAxis);
        option.yAxis(new ValueAxis());

//        内部贷款
        Bar barLoan = new Bar();
        barLoan.setName(multiLangServiceI.getLang("cms.report.loan"));
        barLoan.setType(SeriesType.bar);
        barLoan.setData(getbarData("cls.repay_date", isInclude, curId, convertCurId, moneyType, "1", corpIds, isConvert));
        bars.add(barLoan);

        //        委托贷款
        Bar barPrecative = new Bar();
        barPrecative.setName(multiLangServiceI.getLang("cms.loan.precative"));
        barPrecative.setType(SeriesType.bar);
        barPrecative.setData(getbarData("cpls.deadline_date", isInclude, curId, convertCurId, moneyType, "2", corpIds, isConvert));
        bars.add(barPrecative);

        //        银行借款
        Bar barProvide = new Bar();
        barProvide.setName(multiLangServiceI.getLang("cms.report.loan.provide"));
        barProvide.setType(SeriesType.bar);
        barProvide.setData(getbarData("cpls.repay_date", isInclude, curId, convertCurId, moneyType, "3", corpIds, isConvert));
        bars.add(barProvide);
        series.addAll(bars);
        option.setSeries(series);
        return JsonUtils.toJSONString(option);
    }


    /**
     * 数据构建
     *
     * @param field
     * @param isInclude
     * @param curId
     * @param moneyType
     * @param flag
     * @param corpIds
     * @return
     */
    private ArrayList<BigDecimal> getbarData(String field, String isInclude, String curId, String convertCurId, String moneyType, String flag, List<String> corpIds, String isConvert) {
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOs(field);
        ArrayList<BigDecimal> leftMoneys = new ArrayList<>();
        List<CmsExpireAnalysisCurVO> cmsExpireAnalysisCurVOS = new ArrayList<>();
        BigDecimal leftMoney = BigDecimal.ZERO;
        BigDecimal exchangeRate = BigDecimal.ONE;
        leftMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
        for (BiRemainTermDTO biRemainTermDTO : biRemainTermDTOS) {
            if ("1".equals(flag)) {
//                内部贷款
                cmsExpireAnalysisCurVOS = cmsExpireAnalysisDao.getCmsLoanBar(isInclude, curId, biRemainTermDTO.getSearchSql(), moneyType, corpIds, isConvert, getCommonExchangeRate());
            } else if ("2".equals(flag)) {
//                委托贷款
                cmsExpireAnalysisCurVOS = cmsExpireAnalysisDao.getCmsPrecativeBar(isInclude, curId, biRemainTermDTO.getSearchSql(), moneyType, corpIds, isConvert, getCommonExchangeRate());
            } else if ("3".equals(flag)) {
                //                银行借款
                cmsExpireAnalysisCurVOS = cmsExpireAnalysisDao.getCmsProvideBar(isInclude, curId, biRemainTermDTO.getSearchSql(), moneyType, corpIds, isConvert, getCommonExchangeRate());
            }
            if (null != cmsExpireAnalysisCurVOS && cmsExpireAnalysisCurVOS.size() > 0) {
                for (CmsExpireAnalysisCurVO cmsExpireAnalysisCurVO : cmsExpireAnalysisCurVOS) {
                    if (CmsConstant.IS_TRUE.equals(isConvert)) {
                        if (StringUtils.isNotEmpty(cmsExpireAnalysisCurVO.getCurId()) && StringUtils.isNotEmpty(convertCurId)) {
                            exchangeRate = fcExcrateAdjustService.getExchangeRateBy(cmsExpireAnalysisCurVO.getCurId(), convertCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                            leftMoney = leftMoney.add(cmsExpireAnalysisCurVO.getLeftMoney().multiply(exchangeRate));
                        }
                    } else {
                        leftMoney = leftMoney.add(cmsExpireAnalysisCurVO.getLeftMoney());
                    }
                }
            }
            leftMoneys.add(leftMoney);
        }
        return leftMoneys;
    }

    /**
     * 报表下方表格数据输出
     *
     * @param input
     * @return
     */
    public DataTablesOutput getchartsDataTable(DataTablesInput input, ICurrentUser user) {
        Map<String, Object> params = input.getFormdata();
        String isInclude = params.get("isInclude").toString();
        String curId = params.get("curId").toString();
        String moneyType = params.get("moneyType").toString();
        String isConvert = params.get("isConvert").toString();
        String convertCurId = params.get("convertCurId").toString();
        List<String> corpIds = null;
        if (null != params.get("corpIds") && StringUtils.isNotEmpty(params.get("corpIds").toString())) {
            String corpIdstr = params.get("corpIds").toString();
            if (corpIdstr.contains(",")) {
                corpIds = Arrays.asList(corpIdstr.split(","));
            }
        } else {
            corpIds = corpService.getUserCorpIds(ResourceConstant.CMS_EXPIREANALYSIS_REPORT_QUERY, user.getId());
        }
        List<CmsExpireAnalysisDataTableVO> dataTableVOS = new ArrayList<>();
//        内部贷款
        List<BigDecimal> loans = getbarData("cls.repay_date", isInclude, curId, convertCurId, moneyType, "1", corpIds, isConvert);
//        委托贷款
        List<BigDecimal> Precatives = getbarData("cpls.deadline_date", isInclude, curId, convertCurId, moneyType, "2", corpIds, isConvert);
//        银行借款
        List<BigDecimal> provides = getbarData("cpls.repay_date", isInclude, curId, convertCurId, moneyType, "3", corpIds, isConvert);

        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOList();
//        循环赋值
        for (int i = 0; i < biRemainTermDTOS.size(); i++) {
            CmsExpireAnalysisDataTableVO dataTableVO = new CmsExpireAnalysisDataTableVO();
            dataTableVO.setRemainTermName(biRemainTermDTOS.get(i).getTermName());
            dataTableVO.setLoanData(loans.get(i));
            dataTableVO.setPrecativeData(Precatives.get(i));
            dataTableVO.setPrivodeData(provides.get(i));
            dataTableVO.setTotalData(loans.get(i).add(Precatives.get(i)).add(provides.get(i)));
            dataTableVOS.add(dataTableVO);
        }
        DataTablesOutput output = new DataTablesOutput();
        output.setData(dataTableVOS);
        return output;
    }

    /**
     * 返回公共汇率sql
     * @return
     */
    private String getCommonExchangeRate(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select cur.id curId, ");
        sql.append("        bt_nvl_num(fea3.newExchangeRate, cur.cur_exrate) newExchangeRate ");
        sql.append("   from bt_currency cur ");
        sql.append("   left join (select fea1.cur_id            curId, ");
        sql.append("                     fea1.new_exchange_rate newExchangeRate ");
        sql.append("                from FC_EXCRATE_ADJUST fea1 ");
        sql.append("               inner join (select cur_id, max(pre_time) as pre_time ");
        sql.append("                            from fc_excrate_adjust ");
        sql.append("                           where status = 95 ");
        sql.append("                             and pre_Time <= sysdate ");
        sql.append("                           group by cur_id) fea2 ");
        sql.append("                  on fea1.cur_id = fea2.cur_id ");
        sql.append("                 and fea1.pre_time = fea2.pre_time ");
        sql.append("               where fea1.status = 95) fea3 ");
        sql.append("     on cur.id = fea3.curId         ");
        return sql.toString();
    }

    /**
    *币种转换校验
    *@Param origCurId
    *@Param convertCurId
    *@return
    */
    public void checkConvertCurId(String origCurId, String convertCurId){
        BigDecimal curExrate=BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(convertCurId) && StringUtils.isNotEmpty(origCurId)){
            curExrate = fcExcrateAdjustService.getExchangeRateBy(origCurId,convertCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//距查询日期最近的币别汇率
        }
        if (BigDecimal.ZERO.compareTo(curExrate) == 0) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
    }

    public List<BiRemainTermDTO> getBiRemainTerms(){
        return iBiRemainTermService.getBiRemainTermDTOList();
    }

    /**
    * 构造手机图表数据
    *@Param isInclude
    *@Param curId
    *@Param convertCurId
    *@Param moneyType
    *@Param flag
    *@Param corpIds
    *@Param isConvert
    *@return
    */
    public List<Map<String,Object>> getMobileData(String isInclude, String curId, String convertCurId, String moneyType, List<String> corpIds, String isConvert){
        BigDecimal exchangeRate = BigDecimal.ONE;
        if (StringUtils.isNotEmpty(curId) && StringUtils.isNotEmpty(convertCurId)) {
            exchangeRate = fcExcrateAdjustService.getExchangeRateBy(curId, convertCurId, new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        }
        List<Map<String,Object>> mobileDatas = new ArrayList<>();
        List<BiRemainTermDTO> biRemainTermDTOS = iBiRemainTermService.getBiRemainTermDTOs("cls.repay_date");
        if (null == corpIds || corpIds.size() == 0) {
            return mobileDatas;
        }
        List<String> corpIdsTmp = new ArrayList<>();
        Integer maxYValue = 4;
        for (String corpId : corpIds) {
            corpIdsTmp.add(corpId);
//        内部贷款
            List<BigDecimal> LoanData = getbarData("cls.repay_date", isInclude, curId, convertCurId, moneyType, "1", corpIdsTmp, isConvert);

            //        委托贷款
            List<BigDecimal> PrecativeData = getbarData("cpls.deadline_date", isInclude, curId, convertCurId, moneyType, "2", corpIdsTmp, isConvert);

            //        银行借款
            List<BigDecimal> providData = getbarData("cpls.repay_date", isInclude, curId, convertCurId, moneyType, "3", corpIdsTmp, isConvert);

            List<MobileCharDataVO> corpData = new ArrayList<>();
            for (int i = 0; i < biRemainTermDTOS.size(); i++) {
                //            内部贷款
                MobileCharDataVO mobileVO1 = new MobileCharDataVO();
                mobileVO1.setCategoryName("内部贷款");
                mobileVO1.setxValue(biRemainTermDTOS.get(i).getTermName());
                mobileVO1.setyValue(LoanData.get(i).multiply(exchangeRate));
                Integer TempMaxValue =Integer.valueOf(mobileVO1.getyValue().multiply(new BigDecimal(1.2)).setScale(0, BigDecimal.ROUND_UP).toPlainString());
                maxYValue = maxYValue > TempMaxValue ? maxYValue : TempMaxValue;
//            委托贷款
                MobileCharDataVO mobileVO2 = new MobileCharDataVO();
                mobileVO2.setCategoryName("委托贷款");
                mobileVO2.setxValue(biRemainTermDTOS.get(i).getTermName());
                mobileVO2.setyValue(PrecativeData.get(i).multiply(exchangeRate));
                TempMaxValue =Integer.valueOf(mobileVO2.getyValue().multiply(new BigDecimal(1.2)).setScale(0, BigDecimal.ROUND_UP).toPlainString());
                maxYValue = maxYValue > TempMaxValue ? maxYValue : TempMaxValue;
//            银行借款
                MobileCharDataVO mobileVO3 = new MobileCharDataVO();
                mobileVO3.setCategoryName("银行借款");
                mobileVO3.setxValue(biRemainTermDTOS.get(i).getTermName());
                mobileVO3.setyValue(providData.get(i).multiply(exchangeRate));
                TempMaxValue =Integer.valueOf(mobileVO3.getyValue().multiply(new BigDecimal(1.2)).setScale(0, BigDecimal.ROUND_UP).toPlainString());
                maxYValue = maxYValue > TempMaxValue ? maxYValue : TempMaxValue;
                corpData.add(mobileVO1);
                corpData.add(mobileVO2);
                corpData.add(mobileVO3);
            }
            Map<String, Object> mobileData = new HashMap<>();
            mobileData.put("corpMbileData", corpData);
            if (StringUtils.isNotEmpty(corpId)) {
                mobileData.put("corpName", corpService.get(corpId).getName());
            } else {
                mobileData.put("corpName", "");
            }
            mobileData.put("maxYValue", maxYValue);
            maxYValue = 4;
            mobileDatas.add(mobileData);
            corpIdsTmp.clear();
        }
        return mobileDatas;
    }

    @Override
    public BaseDao getEntityDao() {
        return cmsExpireAnalysisDao;
    }
}
