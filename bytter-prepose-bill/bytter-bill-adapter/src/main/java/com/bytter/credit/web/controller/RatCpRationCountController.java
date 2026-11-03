package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.service.RatCpRationCountService;
import com.bytter.credit.domain.vo.RatUrationDisBean;
import com.bytter.credit.dto.UrationParamDTO;
import com.bytter.credit.vo.RatUrationUseVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author fusb
 * @version 1.0
 * @Title RatCprationCountController
 * @Description
 * @date 2018年11月28日
 */
@Controller
@RequestMapping("/cms/rat/ratcprationcount")
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
     * @param paramDTO
     * @param model
     * @return
     */
    @LogMessage(content = "内部授信额度明细查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/rationQuery")
    public String rationQuery(UrationParamDTO paramDTO, Model model) {
        // 显示单位
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_CPRATION_COUNT_QUERY,
                this.getCurrentUser().getId());
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
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
        List<RatUrationUseVO> rations = ratCpRationCountService.queryRationUseDtl(paramDTO);
        model.addAttribute("rations", rations);
        model.addAttribute("corpId", corpId);
        model.addAttribute("paramDTO", paramDTO);
        model.addAttribute("naturalCur", currencyDTO.getId());
        // 报表描述
        model.addAttribute("userName", getCurrentUser().getUsername());// 制表人
        model.addAttribute("moneyType", paramDTO.getMoneyType());// 金额单位
        model.addAttribute("reportDate", DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));// 报表日期
        model.addAttribute("relateCon", ratBkrationService.getRelateCon());
        return "cms/rat/cpration/listCpRationQuery";
    }
    /**
     * 内部授信额度统计
     * 
     * @param paramDTO
     * @param model
     * @return
     */
    @LogMessage(content = "内部信额度统计", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/rationCountQuery")
    public String rationCountQuery(UrationParamDTO paramDTO, Model model) {
        // 显示单位
        List<String> corpAuth = corpService.getUserCorpIds(RationConstant.CMS_CPRATION_COUNT_QUERY,
                this.getCurrentUser().getId());
        String corpId = paramDTO.getCorpId();// 保存每次查询的单位
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
        // 报表描述
        model.addAttribute("userName", getCurrentUser().getUsername());// 制表人
        model.addAttribute("moneyType", paramDTO.getMoneyType());// 金额单位
        model.addAttribute("reportDate", DateUtils.date2Str(DateUtils.getDate(), DateUtils.date_sdf()));// 报表日期
        model.addAttribute("listCpRat", listCpRat);
        model.addAttribute("corpId", corpId);
        model.addAttribute("paramDTO", paramDTO);
        model.addAttribute("naturalCur", currencyDTO.getId());
        model.addAttribute("totalMoney", totalMoney);
        model.addAttribute("totalUseMoney", totalUseMoney);
        model.addAttribute("totalApproveMoney", totalApproveMoney);
        model.addAttribute("totalLeftMoney", totalLeftMoney);
        model.addAttribute("totalOverPlusMoney", totalOverPlusMoney);
        model.addAttribute("curName", curName);
        return "cms/rat/cpration/listCpRationCountQuery";
    }
    /**
     * 查询内部授信单位
     * 
     * @return
     */
    @PostMapping("/corps")
    @ResponseBody
    public List<CorpDTO> corpDTOs() {
        List<CorpDTO> list = ratCpRationCountService.queryRationCorp("");// 单位
        return list;
    }
}
