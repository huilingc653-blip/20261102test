package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAttachmentService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.domain.bill.vo.AbBillAttachmentVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 票据附件操作 Controller
 *
 * @author onlineGenerator
 * @date 2017-10-24 14:33:51
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillattachment", tags = "票据附件操作")
@RestController("v1AbBillAttachmentController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillattachment")
public class AbBillAttachmentController extends BaseController {

    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    /**
     * 票据附件操作详情页面初始化数据
     */
    @GetMapping("/view")
    @ApiOperation("票据附件详情页面初始化数据")
    public AjaxJson<Map<String, Object>> attachmentInfo(AbAccrecordsVO acVoInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        AbBillAttachmentVO attachmentInfo = abBillAttachmentService.getAbBillAttachmentVO(acVoInfo);
        if (StringUtils.isNotEmpty(acVoInfo.getBriefId()) && AccebillConstant.BRIEF_STATUS_011.equals(acVoInfo.getBriefId())) {
            AbBillAttachmentVO attachmentInfoDiscount = abBillAttachmentService.getAbBillAttachmentVOByDiscount(acVoInfo);
            resultMap.put("attachmentInfoDiscount", attachmentInfoDiscount);
            //不启用贴现申请功能直接贴现办理   不显示贴现申请附件
            resultMap.put("isNotDiscountApply", abVoucherinfoViceService.isNotDiscountApply());
        }
        resultMap.put("attachmentInfo", attachmentInfo);
        return AjaxJson.success(resultMap);
    }

    /**
     * 添加/编辑票据附件操作
     */
    @PostMapping("/opInfo")
    @ApiOperation("保存票据附件信息")
    public AjaxJson<Void> opInfo(@RequestBody AbAccrecordsVO acVoInfo) {
        abBillAttachmentService.opAttachmentInfo(acVoInfo, this.getCurrentUser());
        return AjaxJson.success();
    }
}
