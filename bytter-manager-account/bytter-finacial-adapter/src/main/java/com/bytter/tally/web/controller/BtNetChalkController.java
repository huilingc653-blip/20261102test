package com.bytter.tally.web.controller;

import com.bytter.biz.finacial.accbook.vo.FcAccbookPageVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.BtNetChalkDTO;
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
import com.bytter.tally.entity.TallyBtNetChalk;
import com.bytter.tally.service.BtNetChalkService;
import com.bytter.tally.vo.BtNetChalkVo;
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
//import org.apache.shiro.authz.annotation.Logical;
//import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author
 * @version 1.0
 * @Title: Controller   银企接口收款记账
 * @Description: BT_NET_CHALK
 * @date 2017-07-04 14:58:22
 */
@Controller
@RequestMapping("/finacial/bankCollect/btnetchalk")
public class BtNetChalkController extends BaseController {
    @Autowired
    private BtNetChalkService btNetChalkService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;

    //@RequiresPermissions(value={ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY, ResourceConstant.FCL_BT_NET_CHALK_COMMON_CHECK},logical = Logical.OR)
    @LogMessage(content = "查询BT_NET_CHALK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") String type, Model model) {
        model.addAttribute("opType", type);
        return "finacial/bankcollect/btnetchalk/" + getForwardByType(type);
    }

    /**
     * 通过id查询BT_NET_CHALK
     */
    ////@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
    @PostMapping("/queryBtNetChalk")
    @ResponseBody
    public FcAccbookPageDTO queryBtNetChalk(String id, String type) {
        return btNetChalkService.createAccbook(id, type, getUserId());
    }

    /**
     * 汇总记账，构建凭证
     */
    ////@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
    @PostMapping("/queryBtNetChalkTotal")
    @ResponseBody
    public FcAccbookPageDTO queryBtNetChalkTotal(String ids, String type) {
        return btNetChalkService.createAccbookTotal(ids, type, getUserId());
    }


    /**
     * 合并记账构建凭证
     *
     * @date 2024-01-29
     * @author fangcf
     */
    @PostMapping("/batchQueryBtNetChalk")
    @ResponseBody
    public FcAccbookPageDTO batchQueryBtNetChalk(String ids, String type) {
        List<String> idList = Arrays.asList(ids.split(CommonConstant.COMMA_SPLIT));
        FcAccbookPageDTO entity = btNetChalkService.batchCreateAccbook(idList, type, getUserId());
        return entity;
    }

    /**
     * 根据类型返回不同的跳转页面
     */
    public String getForwardByType(String type) {
        /**银企接口 Init-记账 Edit-改账 Check-复核*/
        String forward = "";
        if ("InitTab".equals(type)) {
            forward = "listBtNetChalkTab";
        } else if ("initCancel".equals(type)) {
            forward = "listBtNetChalkCancel";
        } else if("InitEditTab".equals(type)){
            forward = "listBtNetChalkEditTab";
        } else if ("InitCheckTab".equals(type)){
            forward = "listBtNetChalkCheckTab";
        } else {
            forward = "listBtNetChalk";
        }
        return forward;
    }

    ////@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String type) {
        return btNetChalkService.queryBtNetChalk(dataTablesInput, type, ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY, getUserId());
    }

    /**
     * BT_NET_CHALK新增页面跳转
     */
    ////@RequiresPermissions("bankCollect:btnetchalk:save")
    @GetMapping("/query")
    public String add(TallyBtNetChalk btNetChalk, Model model) {
        return "finacial/bankcollect/btnetchalk/queryList";
    }

    /**
     * 取消BT_NET_CHALK
     */
    @PostMapping("/cancelBtNetChalk")
    @ResponseBody
    public AjaxJson cancelBtNetChalk(String ids) {
        String[] temp = ids.split(",");
        for (String id : temp) {
            btNetChalkService.cancelBtNetChalk(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 提交FC_ACCBOOK
     */
    //@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
    @LogMessage(content = "提交bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        btNetChalkService.submit(fcAccbook, key, getCurrentUser().getId());
        return success();
    }

//    /**
//     * 汇总提交FC_ACCBOOK
//     */
//    //@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
//    @LogMessage(content = "汇总提交bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
//    @PostMapping("/totalSubmit")
//    @ResponseBody
//    public AjaxJson totalSubmit(FcAccbookVoucherDTO fcAccbook, String key) {
//        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
//        if (CollectionUtils.isEmpty(idList)) {
//            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
//        }
//        btNetChalkService.totalSubmit(fcAccbook, idList, getCurrentUser().getId());
//        return success();
//    }

    /**
     * 合并提交FC_ACCBOOK
     */
    @LogMessage(content = "合并提交bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergeSubmit")
    @ResponseBody
    public AjaxJson mergeSubmit(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        btNetChalkService.mergeSubmit(fcAccbook, idList, getCurrentUser().getId());
        return success();
    }

    //@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
    @LogMessage(content = "暂存bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        btNetChalkService.save(fcAccbook, key, getUserId());
        return success();
    }

//    //@RequiresPermissions(ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY)
//    @LogMessage(content = "汇总暂存bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
//    @PostMapping("/totalSave")
//    @ResponseBody
//    public AjaxJson totalSave(FcAccbookVoucherDTO fcAccbook, String key) {
//        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
//        if (CollectionUtils.isEmpty(idList)) {
//            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
//        }
//
//        btNetChalkService.totalSave(fcAccbook, idList, getUserId());
//        return success();
//    }

    @LogMessage(content = "合并暂存bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergeSave")
    @ResponseBody
    public AjaxJson mergeSave(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        btNetChalkService.mergeSave(fcAccbook, idList, getUserId());
        return success();
    }

    /**
     * 恢复BT_NET_CHALK
     */
    @PostMapping("/recoveryBtNetChalk")
    @ResponseBody
    public AjaxJson recoveryBtNetChalk(String ids) {
        String[] temp = ids.split(",");
        for (String id : temp) {
            btNetChalkService.recoveryBtNetChalk(id);
        }
        return success();
    }

    //@RequiresPermissions(value={ResourceConstant.FCL_BT_NET_CHALK_COMMON_APPLY, ResourceConstant.FCL_BT_NET_CHALK_COMMON_CHECK},logical = Logical.OR)
    @LogMessage(content = "打回bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        btNetChalkService.backFcAccbook(fcAccbook, key, getCurrentUser());
        return success();
    }

    @LogMessage(content = "合并打回bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_REPULSE)
    @PostMapping("/mergeBack")
    @ResponseBody
    public AjaxJson mergeBack(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        btNetChalkService.mergeBackFcAccbook(fcAccbook, idList, getCurrentUser());
        return success();
    }

    @LogMessage(content = "复核bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/pass")
    @ResponseBody
    public AjaxJson pass(HttpServletRequest request, FcAccbookVoucherDTO fcAccbook, String key) {
        btNetChalkService.createVoucher(fcAccbook, key, getCurrentUser());
        return success();
    }

    @LogMessage(content = "合并复核bt_net_chalk", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/mergePass")
    @ResponseBody
    public AjaxJson mergePass(FcAccbookVoucherDTO fcAccbook, String key) {
        List<String> idList = Arrays.asList(key.split(CommonConstant.COMMA_SPLIT));
        if (CollectionUtils.isEmpty(idList)) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DTL_NULL);
        }
        btNetChalkService.mergeCreateVoucher(fcAccbook, idList, getCurrentUser());
        return success();
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
	 * 通过bookId查询BT_NET_CHALK
	 */
	@PostMapping("/listByBookId")
	@ResponseBody
	public List<String> listByBookId(String bookId) {
		return btNetChalkService.listByBookId(bookId);
	}

	/**
	 * 查询银企收款记账详情页
	 *
	 * @return String
	 */
	@LogMessage(content = "查询银企收款记账详情页", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@GetMapping("/view")
	public String view(String id, Model model) {
		BtNetChalkDTO entity = btNetChalkService.queryBtNetChalkVo(id);
		model.addAttribute("btNetChalkPage", entity);
		return "finacial/bankcollect/btnetchalk/viewBtNetChalk";
	}
}
