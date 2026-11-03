package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vo.RatBkrationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @author luxy
 * @Title: RatGuaChangeController
 * @ProjectName T2_IDEA
 * @Description: 银行授信担保变更控制层
 * @date 2019/5/5 16:19
 */
@Controller
@RequestMapping("/cms/rat/ratguachange")
public class RatGuaChangeController extends BaseController {
    @Autowired
    private RatBkrationService ratBkrationService;
    @LogMessage(content = "查询银行授信担保变更数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/changelist")
    public String changelist(Model model, Integer start, RatBkrationVO ratBkrationVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        String ratType = RationConstant.RAT_TYPE_0;// 授信种类为银行授信
        if (!StringUtils.isEmpty(ratType)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
            model.addAttribute("ratType", ratType);
        }
        if (!StringUtils.isEmpty(ratBkrationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", ratBkrationVO.getContractNo());
            model.addAttribute("contractNo", ratBkrationVO.getContractNo());
        }
        if (null != ratBkrationVO.getRatDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratBkrationVO.getRatDate()));
        }
        if (null != ratBkrationVO.getgMoney()) {
            input.addFilter(MatchTypeEnum.GE, "money", ratBkrationVO.getgMoney());// 大于等于授信额度
            model.addAttribute("gMoney", ratBkrationVO.getgMoney());
        }
        if (null != ratBkrationVO.getlMoney()) {
            input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney());// 大于等于授信额度
            model.addAttribute("lMoney", ratBkrationVO.getlMoney());
        }
        if (null != ratBkrationVO.getRatMode()) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", ratBkrationVO.getRatMode());// 授信模式
            model.addAttribute("ratMode", ratBkrationVO.getRatMode());
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 录入数据已审批通过
        input.addFilter(MatchTypeEnum.EQ, "isHangGuarantee", RationConstant.IS_HANG_GUA_1);// 挂接担保的数据
        input.addOrder("ratCode", false);
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_GUA_QUERY,
                getUserId());
        Map<String, Object> result = new HashMap<String, Object>();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount = 0;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.put("rations", output.getData());
        result.put("total", pageCount);
        result.put("curr", start);
        model.addAttribute("model", result);
        return "cms/rat/bkration/listRatGuaChange";
    }
}
