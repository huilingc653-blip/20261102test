package com.bytter.biz.erp.controller;


import com.bytter.bill.api.bill.dto.*;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.erp.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Controller
 * @Description: 票据业务员erp
 * @date 2019-06-12
 */
@Controller
@RequestMapping("/erp/abbill")
public class AbBillErpController {


    @DubboReference(check = false)
    private IAccebillService accebillService;

    /**
     * 应付票据开票暂存
     *
     * @param erpAbEmitBillDTO 应付开票暂存参数
     */
    @PostMapping("/emitSave")
    public void emitSave(@RequestBody ErpAbEmitBillDTO erpAbEmitBillDTO) {
        AbEmitBillDTO abEmitBillDTO = new AbEmitBillDTO();
        BeanUtils.copyNotNullProperties(erpAbEmitBillDTO, abEmitBillDTO);
        accebillService.saveAbEmitBillERP(abEmitBillDTO);
    }

    /**
     * 应付票据线下兑付确认
     *
     * @param list
     */
    @PostMapping("/emitCashingConfirmation")
    @ResponseBody
    public void emitCashingConfirmation(@RequestBody List<ErpAbEmitBillDTO> list) {
        List<AbEmitBillDTO> emitList = BeanUtils.copyListProperties(list, AbEmitBillDTO.class);
        this.accebillService.cashingConfirmationERP(emitList);
    }


    /**
     * 应收票据登记暂存
     *
     * @param erpAbReceiveBillDTO 应收票据登记暂存参数
     */
    @PostMapping("/receiveSave")
    public void receiveBillSave(@RequestBody ErpAbReceiveBillDTO erpAbReceiveBillDTO) {
        AbReceiveBillDTO abReceiveBillDTO = new AbReceiveBillDTO();
        BeanUtils.copyNotNullProperties(erpAbReceiveBillDTO, abReceiveBillDTO);
        accebillService.saveAbReceiveBillERP(abReceiveBillDTO);
    }

    /**
     * 应收票据背书
     *
     * @param list
     */
    @PostMapping("/endorse")
    @ResponseBody
    public void endorse(@RequestBody List<ErpAbBillEndorseDTO> list) {
        List<AbBillEndorseDTO> endorseList = BeanUtils.copyListProperties(list, AbBillEndorseDTO.class);
        this.accebillService.endorseERP(endorseList);
    }

    /**
     * 应收票据质押申请
     *
     * @param list
     */
    @PostMapping("/pledgeApply")
    @ResponseBody
    public void pledgeApply(@RequestBody List<ErpAbBillPledgeApplyDTO> list) {
        List<AbBillPledgeApplyDTO> pledgeList = BeanUtils.copyListProperties(list, AbBillPledgeApplyDTO.class);
        this.accebillService.pledgeApplyERP(pledgeList);
    }

    /**
     * 应收票据贴现申请
     *
     * @param list
     */
    @PostMapping("/discountApply")
    @ResponseBody
    public void discountApply(@RequestBody List<ErpAbBillDiscountApplyDTO> list) {
        List<AbBillDiscountApplyDTO> discountList = BeanUtils.copyListProperties(list, AbBillDiscountApplyDTO.class);
        this.accebillService.discountApplyERP(discountList);
    }

    /**
     * 应收票据托收申请
     *
     * @param list
     */
    @PostMapping("/collectionApply")
    @ResponseBody
    public void collectionApply(@RequestBody List<ErpAbBillCollectionApplyDTO> list) {
        List<AbBillCollectionApplyDTO> collectionList = BeanUtils.copyListProperties(list, AbBillCollectionApplyDTO.class);
        this.accebillService.collectionApplyERP(collectionList);
    }

    /**
     * 电票签收接口
     * @param abReceivebiz 参数对象
     * origNo      票据号码
     * signmsgType 签收类型（002承兑签收、003收票签收、010背书签收、012回购式贴现赎回签收、
     *                    018质押签收、019质押解除签收、020提示付款签收、021逾期提示付款签收）
     * signUpFlag  签收标识 SU00同意签收、SU01拒收
     * bankAcc     签收人账号
     * dshnrcd     拒付理由
     *                    DC00与自己有直接债权债务关系的持票人未履行约定义务；
     *                    DC01持票人以欺诈、偷盗或者胁迫等手段取得票据；
     *                    DC02持票人明知有欺诈、偷盗或者胁迫等情形，出于恶意取得票据；
     *                    DC03持票人明知债务人与出票人或者持票人的前手之间存在抗辩事由而取得票据；
     *                    DC04持票人因重大过失取得不符合《票据法》规定的票据；
     *                    DC05超过提示付款期；
     *                    DC06被法院冻结或收到法院止付通知书；
     *                    DC07票据未到期；
     *                    DC08商业承兑汇票承兑人账户余额不足。
     *                    DC09其他（必须注明）
     * dshnrrsn    拒付理由备注
     */
    @PostMapping("/billSignOp")
    @ResponseBody
    public void billSignOp(@RequestBody AbReceivebizDTO abReceivebiz) {
        this.accebillService.billSignOp(abReceivebiz);
    }
}
