package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatBkrationService;
import com.bytter.credit.domain.vo.RatBkrationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 银行授信担保变更
 */
@Api(tags = "银行授信担保变更", value = "/v1-cms/cms/rat/ratguachange")
@RestController("v1RatGuaChangeController")
@RequestMapping("/v1-cms/cms/rat/ratguachange")
public class RatGuaChangeController extends BaseController {

    @Autowired
    private RatBkrationService ratBkrationService;

    /**
     * 银行授信担保变更数据列表
     *
     * @param ratBkrationVO 查询条件
     * @param start         当前页
     * @param len           每页条数
     * @return 担保变更数据列表
     */
    @ApiOperation("查询银行授信担保变更数据列表")
    @LogMessage(content = "查询银行授信担保变更数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/changelist")
    public AjaxJson<DataTablesOutput> changelist(RatBkrationVO ratBkrationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        input.addFilter(MatchTypeEnum.LIKE, "ratType", RationConstant.RAT_TYPE_0);// 授信种类为银行授信
        if (!StringUtils.isEmpty(ratBkrationVO.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", ratBkrationVO.getContractNo());
        }
        if (null != ratBkrationVO.getRatDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratBkrationVO.getRatDate()), Date.class);// 小于等于授信结束日期
        }
        if (null != ratBkrationVO.getgMoney()) {
            input.addFilter(MatchTypeEnum.GE, "money", ratBkrationVO.getgMoney());// 大于等于授信额度
        }
        if (null != ratBkrationVO.getlMoney()) {
            input.addFilter(MatchTypeEnum.LE, "money", ratBkrationVO.getlMoney());// 小于等于授信额度
        }
        if (null != ratBkrationVO.getRatMode()) {
            input.addFilter(MatchTypeEnum.EQ, "ratMode", ratBkrationVO.getRatMode());// 授信模式
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 录入数据已审批通过
        input.addFilter(MatchTypeEnum.EQ, "isHangGuarantee", RationConstant.IS_HANG_GUA_1);// 挂接担保的数据
        input.addOrder("ratCode", false);
        DataTablesOutput output = ratBkrationService.queryRatBkrationData(input, RationConstant.CMS_RATION_GUA_QUERY,
                getUserId());
        return success(output);
    }
}
