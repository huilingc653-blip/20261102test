package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.FcAccbookVoucherDTO;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.service.BisAccDtlTallyService;
import com.bytter.tally.service.BtNetChalkService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName BisAccDtlTallyController
 * @Author zx
 * @Description 下拨手工记账
 * @Date 2024/10/29 14:50
 */
@Controller
@RequestMapping("/finacial/bankCollect/bisAccDtlTally")
public class BisAccDtlTallyController extends BaseController {
    @Autowired
    private BisAccDtlTallyService bisAccDtlTallyService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;

    /**
     * 下拨手工记账页面跳转
     *
     * @param type
     * @param model
     * @return
     */
    @LogMessage(content = "下拨手工记账页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, Model model) {
        model.addAttribute("opType", type);
        return "finacial/bankcollect/btnetchalk/listBtNetChalkDialDown";
    }


    /**
     * 下拨获取总账户付款流水
     *
     * @param dataTablesInput
     * @param type
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String type) {
        return bisAccDtlTallyService.queryBisAccDtlTally(dataTablesInput, type, ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY, getUserId());
    }

    /**
     * 通过流水id查询数据，构建凭证
     *
     * @param id
     * @param type
     * @return
     */
    @PostMapping("/queryAndCreateAccbook")
    @ResponseBody
    public FcAccbookPageDTO queryAndCreateAccbook(String id, String type) {
        return bisAccDtlTallyService.createAccbook(id, type, getUserId());
    }

    /**
     * 查询有权限的内部帐号
     */
    @PostMapping("/getFcAcc")
    @ResponseBody
    public List<FcAccDTO> getFcAcc(HttpServletRequest request, String curId, String netId, String corpId) {
        List<FcAccDTO> list = innerAccountService.listAccountByParams(curId, netId, corpId);
        return EmptyUtils.isEmptyList(list) ? new ArrayList<FcAccDTO>() : list;
    }

    /**
     * 暂存下拨流水记账凭证
     *
     * @param request
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "暂存下拨流水记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        bisAccDtlTallyService.save(fcAccbook, key, getUserId());
        return success();
    }

    /**
     * 提交下拨流水记账凭证
     */
    @LogMessage(content = "提交下拨流水记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        bisAccDtlTallyService.submit(fcAccbook, key, getCurrentUser().getId());
        return success();
    }

    /**
     * 汇总记账，构建凭证
     *
     * @param ids
     * @param type
     * @return
     */
    @PostMapping("/queryAndCreateAccbookTotal")
    @ResponseBody
    public FcAccbookPageDTO queryAndCreateAccbookTotal(String ids, String type) {
        return bisAccDtlTallyService.createAccbookTotal(ids, type, getUserId());
    }

    /**
     * 合并暂存下拨流水记账凭证
     *
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "合并暂存下拨流水记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergeSave")
    @ResponseBody
    public AjaxJson mergeSave(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        bisAccDtlTallyService.mergeSave(fcAccbook, idList, getUserId());
        return success();
    }

    /**
     * 通过bookId查询下拨流水
     *
     * @param bookId
     * @return
     */
    @PostMapping("/listByBookId")
    @ResponseBody
    public List<String> listByBookId(String bookId) {
        return bisAccDtlTallyService.listByBookId(bookId);
    }

    /**
     * 合并记账构建凭证
     *
     * @param ids
     * @param type
     * @return
     */
    @PostMapping("/batchQueryBisAccDtl")
    @ResponseBody
    public FcAccbookPageDTO batchQueryBisAccDtl(String ids, String type) {
        List<String> idList = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        FcAccbookPageDTO entity = bisAccDtlTallyService.batchCreateAccbook(idList, type, getUserId());
        return entity;
    }

    /**
     * 合并提交下拨流水记账凭证
     *
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "合并提交下拨流水记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergeSubmit")
    @ResponseBody
    public AjaxJson mergeSubmit(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        bisAccDtlTallyService.mergeSubmit(fcAccbook, idList, getCurrentUser().getId());
        return success();
    }

    /**
     * 取消BIS_ACC_DTL
     *
     * @param ids
     * @return
     */
    @PostMapping("/cancelBisAccDtl")
    @ResponseBody
    public AjaxJson cancelBisAccDtl(String ids) {
        String[] temp = ids.split(",");
        for (String id : temp) {
            bisAccDtlTallyService.cancelBtNetChalk(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 打回下拨流水记账
     *
     * @param request
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "打回下拨流水记账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        bisAccDtlTallyService.backFcAccbook(fcAccbook, key, getCurrentUser());
        return success();
    }

    /**
     * 下拨流水合并打回
     *
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "下拨流水合并打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_REPULSE)
    @PostMapping("/mergeBack")
    @ResponseBody
    public AjaxJson mergeBack(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        bisAccDtlTallyService.mergeBackFcAccbook(fcAccbook, idList, getCurrentUser());
        return success();
    }

    /**
     * 复核下拨流水记账
     *
     * @param request
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "复核下拨流水记账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/pass")
    @ResponseBody
    public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        bisAccDtlTallyService.createVoucher(fcAccbook, key, getCurrentUser());
        return success();
    }

    /**
     * 合并复核下拨流水记账
     *
     * @param fcAccbook
     * @param key
     * @return
     */
    @LogMessage(content = "合并复核下拨流水记账", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergePass")
    @ResponseBody
    public AjaxJson mergePass(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        bisAccDtlTallyService.mergeCreateVoucher(fcAccbook, idList, getCurrentUser());
        return success();
    }

}
