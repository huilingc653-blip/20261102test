package com.bytter.biz.finacial.accbook.controller;

import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlned;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.service.FcAccbookBlnedService;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.accbook.service.FinancialQueryService;
import com.bytter.biz.finacial.accbook.vo.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.biz.finacial.closedeal.service.FcDayCloseService;
import com.bytter.biz.finacial.paramconfig.service.FcCashItemService;
import com.bytter.biz.finacial.paramconfig.service.FcVoucherCashService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherCashVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccPlusDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

import static com.bytter.biz.finacial.api.constant.ResourceConstant.FCL_ACCBOOK_COMMON_QUERY;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * 文件所在位置bt-biz-finacial\src\main\resources\views\finacial\accbook\accountVoucherQuery.vm
 */
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: FC_ACCBOOK
 * @date 2017-05-31 15:02:10
 */
@Controller
@RequestMapping("/finacial/accbook/fcaccbook")
public class FcAccbookController extends BaseController {
    @Autowired
    private FcAccbookService fcAccbookService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@Autowired
	private FinancialQueryService financialQueryService;
	@Autowired
	private FcVoucherCashService fcVoucherCashService;
	@Autowired
	private FcDayCloseService fcDayCloseService;
	@Autowired
	private FcCashItemService fcCashItemService;
    @Autowired
    private FcAccbookBlnedService fcAccbookBlnedService;
	/**
	 * FC_ACCBOOK列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "finacial/accbook/listFcAccbook";
	}
    /**
     * 记账复核
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/checklist")
    public String checklist(Model model) {
        return "finacial/accbook/listCheckFcAccbook";
    }
    /**
     * 关键要素复核
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "查询FC_ACCBOOK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/checkKeylist")
    public String checkKeylist(Model model) {
        return "finacial/accbook/listCheckKeyFcAccbook";
    }
    /**
     * FC_ACCBOOK table列表
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcAccbookService.getEditFcAccbookVoList(dataTablesInput, FCL_ACCBOOK_COMMON_QUERY, getUserId());
    }
    /**
     * FC_ACCBOOK table列表
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @PostMapping("/checkdataTable")
    @ResponseBody
    public DataTablesOutput checkdataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcAccbookService.getCheckFcAccbookVoList(dataTablesInput, FCL_ACCBOOK_COMMON_QUERY, getUserId());
    }
    /**
     * FC_ACCBOOK新增页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @GetMapping("/add")
    public String add(FcAccbook fcAccbook, String opType, Model model) {
        if (StringUtils.isNotEmpty(fcAccbook.getId())) {
            fcAccbook = fcAccbookService.get(fcAccbook.getId());
            model.addAttribute("fcAccbookPage", fcAccbook);
        }
        model.addAttribute("opType", StringUtils.isEmpty(opType));
        model.addAttribute("multiIframes", false);
        if ("newRush".equals(opType)) {
            model.addAttribute("multiIframes", true);
        }
        model.addAttribute("externalSign", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        return "finacial/accbook/addFcAccbook2";
    }
    /**
     * 功能介绍 : 获取当前用户所在结算中心的业务种类。<br>
     * <b>日期：</b>2017年6月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/getUserBusRace")
    @ResponseBody
    public List<FcBusRaceVo> getUserBusRace() {
        return fcAccbookService.getUserBusRace(getCurrentUser());
    }
    /**
     * 功能介绍 : 获取业务种类。<br>
     * <b>日期：</b>2017年6月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/getBusRace")
    @ResponseBody
    public List<FcBusRaceVo> getBusRace() {
        return fcAccbookService.getBusRace();
    }
    /**
     * FC_ACCBOOK编辑页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @GetMapping("/update")
    public String update(String id, String opType, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAccbookVoucherVO voucher = fcAccbookService.getFcAccbookVoucherVO(id);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtl bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("voucher", voucher);
            model.addAttribute("debitTot", debitTot);
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
            model.addAttribute("bookDtlSize", voucher.getBookDtls().size());
        } else {
            model.addAttribute("bookDtlSize", 0);
            FcAccbookVoucherVO voucher = new FcAccbookVoucherVO();
            FcAccbookExt bookExt = new FcAccbookExt();
            bookExt.setExternalType("1");
            bookExt.setTypeSign("1");
            voucher.setBookExt(bookExt);
            model.addAttribute("voucher", voucher);
        }
        if ("newRush".equals(opType)) {
            model.addAttribute("multiIframes", true);
        } else {
            model.addAttribute("multiIframes", false);
        }
        model.addAttribute("opType", StringUtils.isEmpty(opType));
        model.addAttribute("externalSign", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        return "finacial/accbook/updateFcAccbook2";
    }
    /**
     * 批量打印记账凭证
     *
     * @param ids
     * @param opType
     * @param showFlag 记账凭证查询界面打印记账凭证是否显示上传控件true-显示 false-隐藏
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "批量打印记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/batchPrint")
    public String batchPrint(String ids, String opType, String showFlag, Model model) {
        //listVoucher,传递数据
        List<FcAccbookPageVO> listVoucher = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            if (StringUtils.isNotEmpty(id)) {
                FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
                BigDecimal debitTot = BigDecimal.ZERO;
                for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                    if (null == bookDtl.getDebitMoney()) {
                        bookDtl.setDebitMoney(BigDecimal.ZERO);
                    }
                    if (null == bookDtl.getCreditMoney()) {
                        bookDtl.setCreditMoney(BigDecimal.ZERO);
                    }
                    debitTot = debitTot.add(bookDtl.getDebitMoney());
                }
                listVoucher.add(voucher);
                model.addAttribute("listVoucher", listVoucher);
                model.addAttribute("debitTot", debitTot);
                //model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
                list.add(NumberUtil.formatNumeric(debitTot, "#,##0.00"));
            }
            model.addAttribute("opType", StringUtils.isEmpty(opType));
            model.addAttribute("showFlag", StringUtils.isNotEmpty(showFlag));
            model.addAttribute("debitTotStr", list);
        }
        return "finacial/accbook/batchPrintFcAccbook";
    }
    /**
     * FC_ACCBOOK详情页
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String id, String opType, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
                    .equals(voucher.getAccbook().getVoucherState()));
            model.addAttribute("voucher", voucher);
            model.addAttribute("debitTot", debitTot);
            /*model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot.floatValue(), "#,###.00"));*/
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
        }
        model.addAttribute("opType", StringUtils.isEmpty(opType));
        return "finacial/accbook/viewFcAccbook";
    }
    /**
     * FC_ACCBOOK详情页
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCommon/{voucherNo}")
    public String viewCommon(@PathVariable("voucherNo") String voucherNo, Model model) {
        if (StringUtils.isNotEmpty(voucherNo)) {
            FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOByVoucherNo(voucherNo);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
                    .equals(voucher.getAccbook().getVoucherState()));
            model.addAttribute("voucher", voucher);
            model.addAttribute("debitTot", debitTot);
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
        }
        return "finacial/accbook/viewFcAccbook";
    }
    /**
     * FC_ACCBOOK详情页
     *
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDes")
    public String viewDes(String id, String opType, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
                    .equals(voucher.getAccbook().getVoucherState()));
            model.addAttribute("voucher", voucher);
            model.addAttribute("debitTot", debitTot);
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot.floatValue(), "#,##0.00"));
        }
        return "finacial/accbook/viewRushFcAccbook";
    }
    /**
     * FC_ACCBOOK复核
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/check/{id}")
    public String check(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
                    .equals(voucher.getAccbook().getVoucherState()));
            model.addAttribute("voucher", voucher);
            model.addAttribute("debitTot", debitTot);
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
        }
        return "finacial/accbook/checkFcAccbook";
    }
    /**
     * FC_ACCBOOK关键要素复核
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/checkKey/{id}")
    public String checkKey(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
            BigDecimal debitTot = BigDecimal.ZERO;
            for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                if (null == bookDtl.getDebitMoney()) {
                    bookDtl.setDebitMoney(BigDecimal.ZERO);
                }
                if (null == bookDtl.getCreditMoney()) {
                    bookDtl.setCreditMoney(BigDecimal.ZERO);
                }
                debitTot = debitTot.add(bookDtl.getDebitMoney());
            }
            model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
                    .equals(voucher.getAccbook().getVoucherState()));
            model.addAttribute("voucher", voucher);
            model.addAttribute("bookDtlSize", voucher.getBookDtls().size());
            model.addAttribute("debitTot", debitTot);
            model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
        }
        return "finacial/accbook/checkKeyFcAccbook";
    }
	/**
	 * 添加FC_ACCBOOK
	 *
	 * @param bookId
	 * @return
	 */
	// //@RequiresPermissions("accbook:fcaccbook:save")
	@PostMapping("/getFcAccBookDtls")
	@ResponseBody
	public List<FcAccbookDtlVO> getFcAccBookDtls(String bookId) {
		List<FcAccbookDtl> accbookDtls = new ArrayList<FcAccbookDtl>();
		if (StringUtils.isNotEmpty(bookId)) {
			accbookDtls = fcAccbookService.getFcAccBookDtlByBooKId(bookId);
		}
		List<FcAccbookDtlVO> fcAccbookDtlVOS = BeanUtils.copyListProperties(accbookDtls, FcAccbookDtlVO.class);
		for (FcAccbookDtlVO fcAccbookDtlVO : fcAccbookDtlVOS) {
			if (StringUtils.isNotEmpty(fcAccbookDtlVO.getCashFlux())) {
				fcAccbookDtlVO.setCashFluxName(fcCashItemService.get(fcAccbookDtlVO.getCashFlux()).getItemName());
			}
		}
		return fcAccbookDtlVOS;
	}

    /**
     * 记账修改前校验
     *
     * @param request
     */
    private void validVoucher(HttpServletRequest request, String bookId) {
        String pageType = request.getParameter("pageType");//页面类型
        fcAccbookService.validVoucher(pageType, bookId);
    }
    /**
     * 保存FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "暂存FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        String bankCode = fcAccbook.getBankCode();
        fcAccbook = fcAccbookService.setAccBookVoucher(fcAccbook);
        validVoucher(request, fcAccbook.getAccbook().getId());
        fcAccbookService.saveFcAccbook(fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser()));
        fcAccbookService.saveExternal(fcAccbook, bankCode);
        return success();
    }
    /**
     * 提交FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "提交FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        String bankCode = fcAccbook.getBankCode();
        fcAccbook = fcAccbookService.setAccBookVoucher(fcAccbook);
        validVoucher(request, fcAccbook.getAccbook().getId());
        fcAccbookService.submitFcAccbook(fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser()));
        fcAccbookService.saveExternal(fcAccbook, bankCode);
        return success();
    }
    /**
     * 记账复核FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "复核FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/checkPass")
    @ResponseBody
    public AjaxJson checkPass(HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        FcAccbook accbook = fcAccbookService.get(fcAccbook.getAccbook().getId(), true);
        validVoucher(request, accbook.getId());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(accbook.getBusGlobalId());
        fcAccbookService.checkFcAccbook(voucher);
        return success();
    }
    /**
     * 打回FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "打回FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        FcAccbook accbook = fcAccbookService.get(fcAccbook.getAccbook().getId(), true);
        validVoucher(request, accbook.getId());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(accbook.getBusGlobalId());
        String backReason = request.getParameter("backReason");//打回原因
        FcAccbookExt bookExt = new FcAccbookExt();
        bookExt.setBackReason(backReason);
        fcAccbook.setBookExt(bookExt);
        fcAccbookService.backFcAccbook(voucher);
        return success();
    }
    /**
     * 修改FC_ACCBOOK
     *
     * @param fcAccbook
     * @return
     */
    // //@RequiresPermissions("accbook:fcaccbook:update")
    @LogMessage(content = "修改FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FcAccbook fcAccbook) {
        FcAccbook t = fcAccbookService.get(fcAccbook.getId());
        BeanUtils.copyNotNullProperties(fcAccbook, t);
        fcAccbookService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除FC_ACCBOOK
     *
     * @param request
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "删除FC_ACCBOOK", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        FcAccbook accbook = fcAccbookService.get(fcAccbook.getAccbook().getId(), true);
        if (null == accbook) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
        }
        validVoucher(request, accbook.getId());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(accbook.getBusGlobalId());
        fcAccbookService.deleteFcAccbook(voucher);
        return success();
    }
    /**
     * 查询有转账权限的表内账户
     */
    @PostMapping("/getTallyAccs")
    @ResponseBody
    public List<FcAccPlusDTO> getTallyAccs() {
        return fcAccbookService.getTallyAccount(getUserId(), AccountGrantConstant.TRANSFER_ACCOUNT, ResourceConstant.FCL_ACCBOOK_COMMON_APPLY);
    }
    /**
     * 获取有查询权限的表内账户
     */
    @PostMapping("/getTallyQueryAccs")
    @ResponseBody
    public List<FcAccPlusDTO> getTallyQueryAccs() {
        return fcAccbookService.getTallyAccount(getUserId(), AccountGrantConstant.QUERY_DETAIL, FCL_ACCBOOK_COMMON_QUERY);
    }
    /**
     * 批量打印记账凭证
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(FCL_ACCBOOK_COMMON_QUERY)
    @LogMessage(content = "批量打印资金池记账凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/batchPrintPool")
    public String batchPrintPool(String ids, String opType, Model model) {
        //listVoucher,传递数据
        List<FcAccbookPageVO> listVoucher = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            if (StringUtils.isNotEmpty(id)) {
                FcAccbookPageVO voucher = fcAccbookService.getPoolFcAccbookPageVOById(id);
                BigDecimal debitTot = BigDecimal.ZERO;
                for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
                    if (null == bookDtl.getDebitMoney()) {
                        bookDtl.setDebitMoney(BigDecimal.ZERO);
                    }
                    if (null == bookDtl.getCreditMoney()) {
                        bookDtl.setCreditMoney(BigDecimal.ZERO);
                    }
                    debitTot = debitTot.add(bookDtl.getDebitMoney());
                }
                listVoucher.add(voucher);
                model.addAttribute("listVoucher", listVoucher);
                model.addAttribute("debitTot", debitTot);
                model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
            }
            model.addAttribute("opType", StringUtils.isEmpty(opType));
        }
        return "finacial/accbook/batchPrintFcAccbook";
    }
	/**
	 *
	 * 功能介绍 : 获取银行账号
	 */
	@PostMapping("/jsonBankAcc")
	@ResponseBody
	public List<BtBankAccDTO> jsonBankAcc(String sign){
		List<BtBankAccDTO> list = iBtBankAccService.getBankAccByUserId(getUserId(),sign);
		return list;
	}
	/**
	 *
	 * @param vo
	 * @param model
	 * @param start
	 * @param len
	 * @return
	 */
	@GetMapping("/listVoucherBook")
	public String listVoucherBook(VFcAccbookAndBlnedsVO vo, Model model, Integer start, Integer len){
		DataTablesInput input = new DataTablesInput();
		String opt = vo.getOpt();
		DataTablesOutput output = null;
		if (null == start) {
			start = Integer.valueOf(1);
		}
		if (null != len) {
			input.setLength(len);//页面数据条数
		}
		input.setStart((start - 1) * input.getLength());
		if(FinacialConstant.PAGE_QUERY.equals(opt)){
			getQueryParam(model,input,vo);
		}
		output = financialQueryService.listUpdateCashItemDataTable(input, ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
		Map<String, Object> result = new HashMap<String, Object>();
		long resultCount = output.getRecordsTotal();
		int pageSize = input.getLength();
		int pageCount = 0;
		if (resultCount % pageSize == 0) {
			//整除则直接取整相除
			pageCount = (int) (resultCount / pageSize);
		} else {
			//否则取整相除后加一
			pageCount = (int) ((resultCount / pageSize) + 1);
		}
		result.put("bookAndBlneds", output.getData());
		result.put("total", output.getRecordsTotal());
		result.put("curr", start);
		result.put("len", input.getLength());
		model.addAttribute("model", result);
		return "finacial/accbook/listVoucherBook";
	}
	/**
	 * 查询参数
	 * @param input
	 * @return
	 */
	private DataTablesInput getQueryParam(Model model,DataTablesInput input,VFcAccbookAndBlnedsVO vo) {
		if(!EmptyUtils.isEmpty(vo.getNetCode())){//网点
			input.addFilter(MatchTypeEnum.EQ, "netId", vo.getNetCode());
			model.addAttribute("netId", vo.getNetCode());
		}
		if(!EmptyUtils.isEmpty(vo.getSysRaceCode())){//系统业务种类
			input.addFilter(MatchTypeEnum.EQ, "sysRaceCode", vo.getSysRaceCode());
			model.addAttribute("sysRaceCode", vo.getSysRaceCode());
		}
		if(!EmptyUtils.isEmpty(vo.getLedTallyDate())){//
			input.addFilter(MatchTypeEnum.LE, "tallyDate", DateUtils.formatDate(vo.getLedTallyDate()), Date.class);//大于等于记账开始日期
			model.addAttribute("ledTallyDate", DateUtils.date2Str(vo.getLedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getGedTallyDate())){//
			input.addFilter(MatchTypeEnum.GE, "tallyDate", DateUtils.formatDate(vo.getGedTallyDate()), Date.class);//小于等于记账结束日期
			model.addAttribute("gedTallyDate", DateUtils.date2Str(vo.getGedTallyDate(),DateUtils.date_sdf()));
		}
		if(!EmptyUtils.isEmpty(vo.getAccount()) && !"undefined".equals(vo.getAccount())){//帐号
			input.addFilter(MatchTypeEnum.EQ, "account", vo.getAccount());
			model.addAttribute("account", vo.getAccount());
		}
		if(!EmptyUtils.isEmpty(vo.getAccName()) && !"undefined".equals(vo.getAccName())){//帐号名称
			input.addFilter(MatchTypeEnum.LIKE, "accName", vo.getAccName());
			model.addAttribute("accName", vo.getAccName());
		}
		if(!EmptyUtils.isEmpty(vo.getLesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.LE, "voucherNo", vo.getLesVoucherNo());
			model.addAttribute("lesVoucherNo", vo.getLesVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGesVoucherNo())){//凭证号
			input.addFilter(MatchTypeEnum.GE, "voucherNo", vo.getGesVoucherNo());
			model.addAttribute("gesVoucherNo", vo.getGesVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherType())){//结算方式
			input.addFilter(MatchTypeEnum.EQ, "voucherType", vo.getVoucherType());
			model.addAttribute("voucherType", vo.getVoucherType());
		}
		if(!EmptyUtils.isEmpty(vo.getCurCode())){//币别
			input.addFilter(MatchTypeEnum.EQ, "curCode", vo.getCurCode());
			model.addAttribute("curCode", vo.getCurCode());
		}
		if(!EmptyUtils.isEmpty(vo.getLesOrderNo())){//流水号
			input.addFilter(MatchTypeEnum.LE, "orderNo", vo.getLesOrderNo());
			model.addAttribute("lesOrderNo", vo.getLesOrderNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGesOrderNo())){//流水号
			input.addFilter(MatchTypeEnum.GE, "orderNo", vo.getGesOrderNo());
			model.addAttribute("gesOrderNo", vo.getGesOrderNo());
		}
		if(!EmptyUtils.isEmpty(vo.getOrigVoucherNo())){//原始凭证号
			input.addFilter(MatchTypeEnum.EQ, "origVoucherNo", vo.getOrigVoucherNo());
			model.addAttribute("origVoucherNo", vo.getOrigVoucherNo());
		}
		if(!EmptyUtils.isEmpty(vo.getGegMoney())){//借方发生额
			input.addFilter(MatchTypeEnum.GE, "debitMoney", vo.getGegMoney());
			//input.addFilter(MatchTypeEnum.GE, "creditMoney", vo.getGegMoney());
			model.addAttribute("gegMoney", vo.getGegMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getLegMoney())){//贷方发生额
			input.addFilter(MatchTypeEnum.LE, "debitMoney", vo.getLegMoney());
			//input.addFilter(MatchTypeEnum.LE, "creditMoney", vo.getLegMoney());
			model.addAttribute("legMoney", vo.getLegMoney());
		}
		if(!EmptyUtils.isEmpty(vo.getVoucherState())){//凭证状态
			input.addFilter(MatchTypeEnum.EQ, "voucherState", vo.getVoucherState());
			model.addAttribute("voucherState", vo.getVoucherState());
		}
		return input;
	}
	@GetMapping("/viewVoucherBookItem")
	public String viewVoucherBookItem(String ids,String opType, Model model) {
		//listVoucher,传递数据
		List<FcAccbookPageVO> listVoucher =new ArrayList<>();
		List<String> list = new ArrayList<>();
		FcVoucherCashVo fcVoucherCashVo;
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)){
			if (StringUtils.isNotEmpty(id)) {
				FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
				FcDayClose fcDayClose = fcDayCloseService.getMonthCloseDate(voucher.getAccbook().getNetId());
				Date nextCloseMonth = DateUtils.addMonth(fcDayClose.getCloseDate(), 1);
				if (DateUtils.dayDiffs(nextCloseMonth, voucher.getAccbook().getTallyDate()) > 0) {
					throw new ParameterException(AccbookMessageEnum.FIN_BOOK_IS_CLOSE_MONTH);
				}
				BigDecimal debitTot = BigDecimal.ZERO;
				for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
//					借方发生额为0
					if (null == bookDtl.getDebitMoney()) {
						bookDtl.setDebitMoney(BigDecimal.ZERO);
					}
//					贷方发生额为0
					if (null == bookDtl.getCreditMoney()) {
						bookDtl.setCreditMoney(BigDecimal.ZERO);
					}
					if (CommonConstant.NO.equals(voucher.getAccbook().getIsChangeCashflux())) {
						if ((bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0 || bookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) < 0) && StringUtils.isEmpty(bookDtl.getCashFlux())) {
							fcVoucherCashVo = fcVoucherCashService.getItemBySubject(bookDtl.getSubjectId(), null);
							if (null != fcVoucherCashVo) {
								bookDtl.setCashFlux(fcVoucherCashVo.getCreditItem());
							}
						}
						if ((bookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0 || bookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) < 0) && StringUtils.isEmpty(bookDtl.getCashFlux())) {
							fcVoucherCashVo = fcVoucherCashService.getItemBySubject(null, bookDtl.getSubjectId());
							if (null != fcVoucherCashVo) {
								bookDtl.setCashFlux(fcVoucherCashVo.getDebitItem());
							}
						}
					}
					debitTot = debitTot.add(bookDtl.getDebitMoney());
				}
				listVoucher.add(voucher);
				model.addAttribute("listVoucher", listVoucher);
				model.addAttribute("debitTot", debitTot);
				//model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,##0.00"));
				list.add(NumberUtil.formatNumeric(debitTot, "#,##0.00"));
			}
			model.addAttribute("opType", StringUtils.isEmpty(opType));
			model.addAttribute("debitTotStr", list);
		}
		return "finacial/accbook/viewVoucherBookItem";
	}
	/**
	 *
	 * @param fcAccbookVoucherVO
	 */
	@PostMapping("/updateVoucherBookItem")
	@ResponseBody
	public AjaxJson updateVoucherBookItem(FcAccbookVoucherVO fcAccbookVoucherVO){
		fcAccbookService.updateVoucherBookItem(fcAccbookVoucherVO, getCurrentUser());
		return success();
	}
	@PostMapping("/updateAttachmentId")
	@ResponseBody
	public AjaxJson updateAttachmentId(FcAccbook fcAccbook) {
        FcAccbook t = fcAccbookService.get(fcAccbook.getId(), true);
        if (t != null) {
            BeanUtils.copyNotNullProperties(fcAccbook, t);
            fcAccbookService.update(t);
        } else {
            //已日结凭证
            FcAccbookBlned accbookBlned = fcAccbookBlnedService.get(fcAccbook.getId(), true);
            if (accbookBlned != null) {
                BeanUtils.copyNotNullProperties(fcAccbook, accbookBlned);
                fcAccbookBlnedService.update(accbookBlned);
            }
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量提交凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids")String ids) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            submitFcAccbook(id);
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量删除凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids")String ids,HttpServletRequest request, FcAccbookVoucherVO fcAccbook) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
            FcAccbook accbook = fcAccbookService.get(id, true);
            if (null == accbook) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHER_CHANGE);//状态已改变，刷新页面重试
            }
            validVoucher(request, accbook.getId());
            VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
            voucher.setBusGlobalId(accbook.getBusGlobalId());
            fcAccbookService.deleteFcAccbook(voucher);
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量复核凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchPass")
    @ResponseBody
    public AjaxJson batchPass(@RequestParam("ids")String ids) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            checkFcAccbook(id);
        }
        return success();
    }
    /**
     * 批量提交凭证
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
    @LogMessage(content = "批量打回凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(@RequestParam("ids")String ids, @RequestParam("backReason")String backReason) {
        for(String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            backFcAccbook(id, backReason);
        }
        return success();
    }
    /**
     * 功能介绍 : 记账提交凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     * @version 1.0
     * @author liyh
     */
    public void submitFcAccbook(String id) {
        FcAccbookVoucherVO fcAccbook = fcAccbookService.getFcAccbookVoucherVO(id);
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        //增加状态校验
        fcAccbookService.submitFcAccbook(voucher);
    }
    /**
     * 功能介绍 : 记账提交凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     * @version 1.0
     * @author liyh
     */
    public void checkFcAccbook(String id) {
        FcAccbookVoucherVO fcAccbook = fcAccbookService.getFcAccbookVoucherVO(id);
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        fcAccbookService.checkFcAccbook(voucher);
        //增加状态校验
    }
    /**
     * 功能介绍 : 记账提交凭证对象。<br>
     * <b>日期：</b>2017年5月31日<br>
     * @version 1.0
     * @author liyh
     */
    public void backFcAccbook(String id, String backReason) {
        FcAccbookVoucherVO fcAccbook = new FcAccbookVoucherVO();
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        fcAccbook = fcAccbookService.getFcAccbookVoucherVO(id);
        fcAccbook.getAccbook().setBusOriType(FinaBookConstant.BusOriType.TAILLY.get());
        VoucherWrapper voucher = fcAccbookService.setVoucherWrapper(fcAccbook, getUserId(), getCurrentUser());
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        FcAccbookExt bookExt = fcAccbook.getBookExt();
        bookExt.setBackReason(backReason);
        fcAccbookService.backFcAccbook(voucher);
        //增加状态校验
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/export")
    public void export(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        String title = "记账复核分录明细";
        if(EmptyUtils.isNotEmpty(id)) {
            String ids[] = id.split(",");
            List<FcAccbookPageVO> list = new ArrayList<>();
            for(int i =0;i<ids.length;i++){
                FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(ids[i]);
                list.add(voucher);
            }
            fcAccbookService.export(request, response, list, title);
        }
    }
}
