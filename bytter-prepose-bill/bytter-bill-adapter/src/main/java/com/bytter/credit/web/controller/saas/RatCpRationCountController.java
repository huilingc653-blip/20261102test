package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatCpRationCountService;
import com.bytter.credit.domain.vo.RatCpRationCountStatVO;
import com.bytter.credit.domain.vo.RatCpRationUseQueryVO;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 内部授信额度查询统计
 */
@Api(tags = "内部授信额度查询统计", value = "/v1-cms/cms/rat/ratcprationcount")
@RestController("v1RatCpRationCountController")
@RequestMapping("/v1-cms/cms/rat/ratcprationcount")
public class RatCpRationCountController extends BaseController {

    @Autowired
    private RatCpRationCountService ratCpRationCountService;
    @DubboReference(check = false)
    private IBtCurrencyService curService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private RatBkrationService ratBkrationService;

    /**
     * 内部授信额度明细查询
     *
     * @param paramDTO 查询条件
     * @return 额度明细报表数据
     */
    @ApiOperation("内部授信额度明细查询")
    @LogMessage(content = "内部授信额度明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/rationQuery")
    public AjaxJson<RatCpRationUseQueryVO> rationQuery(UrationParamDTO paramDTO) {
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
        BtCurrencyDTO currencyDTO = initParam(paramDTO);// 本位币
        List<RatUrationUseVO> rations = ratCpRationCountService.queryRationUseDtl(paramDTO);
        RatCpRationUseQueryVO result = new RatCpRationUseQueryVO();
        result.setRations(rations);
        result.setCorpId(corpId);
        result.setParamDTO(paramDTO);
        result.setNaturalCur(currencyDTO.getId());
        result.setUserName(getCurrentUser().getUsername());// 制表人
        result.setMoneyType(paramDTO.getMoneyType());// 金额单位
        result.setReportDate(DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));// 报表日期
        result.setRelateCon(ratBkrationService.getRelateCon());
        return success(result);
    }

    /**
     * 内部授信额度统计
     *
     * @param paramDTO 查询条件
     * @return 额度统计报表数据
     */
    @ApiOperation("内部授信额度统计")
    @LogMessage(content = "内部信额度统计", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/rationCountQuery")
    public AjaxJson<RatCpRationCountStatVO> rationCountQuery(UrationParamDTO paramDTO) {
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
        BtCurrencyDTO currencyDTO = initParam(paramDTO);// 本位币
        List<RatUrationDisBean> listCpRat = ratCpRationCountService.queryRationUseCountDtl(paramDTO);
        // 遍历listCpRat,计算合计
        BigDecimal totalMoney = new BigDecimal(0);// 授信额度合计
        BigDecimal totalUseMoney = new BigDecimal(0);// 占用额度合计
        BigDecimal totalApproveMoney = new BigDecimal(0);// 在审占用额度合计
        BigDecimal totalLeftMoney = new BigDecimal(0);// 剩余额度合计
        BigDecimal totalOverPlusMoney = new BigDecimal(0);
        for (RatUrationDisBean bean : listCpRat) {
            totalMoney = totalMoney.add(bean.getMoney());
            totalUseMoney = totalUseMoney.add(bean.getUseMoney());
            totalApproveMoney = totalApproveMoney.add(bean.getApproveMoney());
            totalLeftMoney = totalLeftMoney.add(bean.getLeftMoney());
            totalOverPlusMoney = totalOverPlusMoney.add(bean.getOverPlusMoney());
        }
        // 币别名称
        String curName = curService.getObjectById(paramDTO.getCurCode()).getCurName();
        RatCpRationCountStatVO result = new RatCpRationCountStatVO();
        result.setListCpRat(listCpRat);
        result.setCorpId(corpId);
        result.setParamDTO(paramDTO);
        result.setNaturalCur(currencyDTO.getId());
        result.setUserName(getCurrentUser().getUsername());// 制表人
        result.setMoneyType(paramDTO.getMoneyType());// 金额单位
        result.setReportDate(DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));// 报表日期
        result.setTotalMoney(totalMoney);
        result.setTotalUseMoney(totalUseMoney);
        result.setTotalApproveMoney(totalApproveMoney);
        result.setTotalLeftMoney(totalLeftMoney);
        result.setTotalOverPlusMoney(totalOverPlusMoney);
        result.setCurName(curName);
        return success(result);
    }

    /**
     * 查询内部授信单位
     *
     * @return 单位列表
     */
    @ApiOperation("查询内部授信单位")
    @GetMapping("/corps")
    public AjaxJson<List<CorpDTO>> corps() {
        return success(ratCpRationCountService.queryRationCorp(""));
    }

    private BtCurrencyDTO initParam(UrationParamDTO paramDTO) {
        // 显示单位
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_CPRATION_COUNT_QUERY,
                getCurrentUser().getId());
        if (StringUtils.isEmpty(paramDTO.getCorpId())) {// 未选择查询的单位则默认查询当前用户下所有单位的
            String tempStr = StringUtils.listTtoString(corpAuth);
            paramDTO.setCorpId(tempStr);
        }
        if (EmptyUtils.isEmpty(paramDTO.getMoneyType())) {// 金额单位:元/万元
            paramDTO.setMoneyType(RationConstant.COMMON_MONEYUNIT_1);// 元
        }
        Date beginDate = paramDTO.getBeginDate();// 授信开始日期
        if (null == beginDate) {
            beginDate = DateUtils.getDateYMD();
            paramDTO.setBeginDate(beginDate);
        }
        Date endDate = paramDTO.getEndDate();// 授信结束日期
        if (null == endDate) {
            endDate = DateUtils.getDateYMD();
            paramDTO.setEndDate(endDate);
        }
        BtCurrencyDTO currencyDTO = curService.getNatualCurrency();// 本位币
        if (EmptyUtils.isEmpty(paramDTO.getCurCode())) {
            paramDTO.setCurCode(currencyDTO.getId());
        }
        return currencyDTO;
    }
}
