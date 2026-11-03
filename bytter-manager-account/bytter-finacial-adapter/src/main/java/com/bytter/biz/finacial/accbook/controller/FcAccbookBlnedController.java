package com.bytter.biz.finacial.accbook.controller;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookBlned;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.entity.FcAccbookExt;
import com.bytter.biz.finacial.accbook.service.FcAccbookBlnedService;
import com.bytter.biz.finacial.accbook.service.FcAccbookRushService;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookDtlVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookPageVO;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: 冲账控制器
 * @Description: FC_ACCBOOK_BLNED
 * @author onlineGenerator
 * @date 2017-06-19 11:28:14
 * @version 1.0loadRushVoucher
 *
 */
@Controller
@RequestMapping("/finacial/accbook/fcaccbookblned")
public class FcAccbookBlnedController extends BaseController {
	@Autowired(required = false)
	private FcAccbookBlnedService fcAccbookBlnedService;
	@Autowired
//	@Qualifier("fcAccbookService")
	private FcAccbookService fcAccbookService;
	@Autowired
	private FcAccbookRushService rushService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;

	/*@Autowired
	private StreamBridge streamBridge;

	@GetMapping("/test")
	@ResponseBody
	public String test(){
		String msg = "{\n" +
				"\"userName\":\"张三\",\n" +
				"\"userAge\":\"18\"\n" +
				"}";
		MimeType mimeType = MimeType.valueOf("application/json");
		boolean send = streamBridge.send("finacial-to-payment-out-0", MessageBuilder.withPayload(msg).setHeader("ftopkey","ftopkey").build(), mimeType);
		return "test";
	}*/


	/**
	 * FC_ACCBOOK_BLNED列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK_BLNED列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "finacial/accbook/listFcAccbookBlned";
	}

	/**
	 * FC_ACCBOOK_BLNED冲账修改列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK_BLNED列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/editlist")
	public String editlist(Model model) {
		return "finacial/accbook/listEditFcAccbookBlned";
	}

	/**
	 * FC_ACCBOOK_BLNED 复核列表 页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK_BLNED列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/checklist")
	public String checklist(Model model) {
		return "finacial/accbook/listCheckFcAccbookBlned";
	}
	/**
	 * FC_ACCBOOK_BLNED table列表
	 *  待冲账账务列表数据查询
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccbookBlnedService.getCanRushFcAccbookVoList(dataTablesInput,
				ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
	}

	/**
	 * 功能介绍 : 冲账修改账务列表数据查询。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/editdataTable")
    @ResponseBody
	public DataTablesOutput editdataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccbookBlnedService.getEditRushFcAccbookVoList(dataTablesInput,
				ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
	}
	/**
	 * 功能介绍 : 冲账复核账务列表数据查询。<br>
	 * <b>日期：</b>2017年6月19日<br>
	 * @version 1.0
	 * @author liyh
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@PostMapping("/checkdataTable")
    @ResponseBody
	public DataTablesOutput checkdataTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcAccbookBlnedService.getCheckRushFcAccbookVoList(dataTablesInput,
				ResourceConstant.FCL_ACCBOOK_COMMON_QUERY, getUserId());
	}

	/**
	 * 功能介绍 : 账务冲账前判断。<br>
	 * <b>日期：</b>2017年11月2日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@RequestMapping("/rushBefore")
	@ResponseBody
    public String rushBefore(String bookId){
		return rushService.rushBefore(bookId);
    }

	/**
	 * FC_ACCBOOK_BLNED新增页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@GetMapping("/add")
	public String add(FcAccbookBlned fcAccbookBlned, Model model) {
		if (StringUtils.isNotEmpty(fcAccbookBlned.getId())) {
			model.addAttribute("id", fcAccbookBlned.getId());
		}
		return "finacial/accbook/addFcAccbookBlnedTab";
	}

	/**
	 * 功能介绍 : 加载冲账凭证数据。<br>
	 * <b>日期：</b>2017年6月22日<br>
	 * @version 1.0
	 * @author liyh
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@GetMapping("/loadRushVoucher")
	public String loadRushVoucher(String id, String oriId, Model model) {
		if (StringUtils.isNotEmpty(oriId)) {
			//判断是否申请或修改
			FcAccbookPageVO voucher = null;
			if (StringUtils.isEmpty(id)) {//冲账申请以日结前的凭证作为冲账凭证
				voucher = fcAccbookBlnedService.getFcAccbookBlnedPageVOById(oriId,false);
				//当前系统时间
				Date dateNow = DateUtils.getDate();
				Date dayOverDate = dayCloseService.getDayCloseFinalDate(voucher.getAccbook().getNetId());//根据网点获取已日结日期
				//系统当前日期是否小于日结日期
				if (dateNow.compareTo(dayOverDate) <= 0) {
					voucher.getAccbook().setTallyDate(DateUtils.addDay(dayOverDate, 1));
				} else {
					voucher.getAccbook().setTallyDate(dateNow);
				}
			} else {//冲账修改以保存的凭证作为冲账凭证
				voucher = fcAccbookService.getFcAccbookPageVOById(id);
			}
			BigDecimal debitTot = BigDecimal.ZERO;
			for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
				BigDecimal debitMoney = bookDtl.getDebitMoney();
				BigDecimal creditMoney = bookDtl.getCreditMoney();
				if (null == debitMoney) {
					bookDtl.setDebitMoney(BigDecimal.ZERO);
				}
				if (null == creditMoney) {
					bookDtl.setCreditMoney(BigDecimal.ZERO);
				}
				//冲账的负值以正值显示
				if (FinacialConstant.RB_SIGN_R.equals(voucher.getAccbook().getRedblueSign())) {
	                creditMoney = BigDecimal.ZERO.subtract(creditMoney);
	                debitMoney = BigDecimal.ZERO.subtract(debitMoney);
	                bookDtl.setCreditMoney(creditMoney);
	                bookDtl.setDebitMoney(debitMoney);
	            }
				debitTot = debitTot.add(bookDtl.getDebitMoney());
			}
			model.addAttribute("voucher", voucher);
			model.addAttribute("debitTot", debitTot);
			/*model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot.floatValue(), "#,###.00"));*/
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,###.00"));
		}
		return "finacial/accbook/addFcAccbookBlned";
	}
	/**
	 * 查看冲账凭证
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@GetMapping("/viewrush")
	public String viewrush(FcAccbook fcAccbook, Model model) {
		if (StringUtils.isNotEmpty(fcAccbook.getId())) {
			List<FcAccbookVoucherVO> voucherVOs = fcAccbookService.getBookVoucherByBusGlobalId(fcAccbook.getBusGlobalId());
			if (!voucherVOs.isEmpty() && voucherVOs.size() > 1) {
                for (FcAccbookVoucherVO voucherVO : voucherVOs) {
                    if (!fcAccbook.getId().equals(voucherVO.getAccbook().getId())) {
                    	//新录入凭证的ID
                        model.addAttribute("newId", voucherVO.getAccbook().getId());
                    } else {
                    	model.addAttribute("id", voucherVO.getAccbook().getId());
                    	model.addAttribute("busGlobalId", voucherVO.getAccbook().getBusGlobalId());
                    	model.addAttribute("origVoucherNo", voucherVO.getAccbook().getOrigVoucherNo());
					}
                }
                model.addAttribute("checkNew", "checked");
            } else {
            	FcAccbookVoucherVO voucherVO = voucherVOs.get(0);
            	model.addAttribute("id", voucherVO.getAccbook().getId());
            	model.addAttribute("busGlobalId", voucherVO.getAccbook().getBusGlobalId());
            	model.addAttribute("origVoucherNo", voucherVO.getAccbook().getOrigVoucherNo());
            }
		}
		String page = "finacial/accbook/viewFcAccbookBlnedTab";
		return page;
	}

	/**
	 * FC_ACCBOOK_BLNED编辑页面跳转
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@GetMapping("/update")
	public String update(FcAccbook fcAccbook, String opType, Model model) {
		if (StringUtils.isNotEmpty(fcAccbook.getId())) {
			List<FcAccbookVoucherVO> voucherVOs = fcAccbookService.getBookVoucherByBusGlobalId(fcAccbook.getBusGlobalId());
			if (!voucherVOs.isEmpty() && voucherVOs.size() > 1) {
                for (FcAccbookVoucherVO voucherVO : voucherVOs) {
                    if (!fcAccbook.getId().equals(voucherVO.getAccbook().getId())) {
                    	//新录入凭证的ID
                        model.addAttribute("newId", voucherVO.getAccbook().getId());
                    } else {
                    	model.addAttribute("id", voucherVO.getAccbook().getId());
                    	model.addAttribute("busGlobalId", voucherVO.getAccbook().getBusGlobalId());
                    	model.addAttribute("origVoucherNo", voucherVO.getAccbook().getOrigVoucherNo());
					}
                }
                model.addAttribute("checkNew", "checked");
            } else {
            	FcAccbookVoucherVO voucherVO = voucherVOs.get(0);
            	model.addAttribute("id", voucherVO.getAccbook().getId());
            	model.addAttribute("busGlobalId", voucherVO.getAccbook().getBusGlobalId());
            	model.addAttribute("origVoucherNo", voucherVO.getAccbook().getOrigVoucherNo());
            }
		}
		String page = "finacial/accbook/updateFcAccbookBlnedTab";
		if (!StringUtils.isEmpty(opType) && FinacialConstant.PAGE_CHECK.equals(opType)) {
			page = "finacial/accbook/checkFcAccbookBlnedTab";
		}
		return page;
	}
	/**
	 * 已日结账务详情页
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcAccbookPageVO voucher = fcAccbookBlnedService.getFcAccbookBlnedPageVOById(id,false);
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
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,###.00"));
		}
		return "finacial/accbook/viewFcAccbook";
	}

	/**
	 * 冲账凭证查询，页面冲账金额需红色正数显示
	 *
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_QUERY)
	@LogMessage(content = "查询FC_ACCBOOK详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/viewDes")
	public String viewDes(String id, String opType, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcAccbookPageVO voucher = fcAccbookService.getFcAccbookPageVOById(id);
			BigDecimal debitTot = BigDecimal.ZERO;
			for (FcAccbookDtlVO bookDtl : voucher.getBookDtls()) {
				BigDecimal debitMoney = bookDtl.getDebitMoney();
				BigDecimal creditMoney = bookDtl.getCreditMoney();
				if (null == bookDtl.getDebitMoney()) {
					bookDtl.setDebitMoney(BigDecimal.ZERO);
				}
				if (null == bookDtl.getCreditMoney()) {
					bookDtl.setCreditMoney(BigDecimal.ZERO);
				}
				//冲账的负值以正值显示
				if (FinacialConstant.RB_SIGN_R.equals(voucher.getAccbook().getRedblueSign())) {
	                creditMoney = BigDecimal.ZERO.subtract(creditMoney);
	                debitMoney = BigDecimal.ZERO.subtract(debitMoney);
	                bookDtl.setCreditMoney(creditMoney);
	                bookDtl.setDebitMoney(debitMoney);
	            }
				debitTot = debitTot.add(bookDtl.getDebitMoney());
			}
			model.addAttribute("invalidFlag", FinaBookConstant.VoucherStatus.STATUS_INVALID_CHECK.get()
					.equals(voucher.getAccbook().getVoucherState()));
			model.addAttribute("voucher", voucher);
			model.addAttribute("debitTot", debitTot);
			/*model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot.floatValue(), "#,###.00"));*/
			model.addAttribute("debitTotStr", NumberUtil.formatNumeric(debitTot, "#,###.00"));
		}
		return "finacial/accbook/viewRushFcAccbook";
	}
	/**
	 * 记账修改前校验
	 * @param request
	 */
	private void validVoucher(HttpServletRequest request, String bookId, String validType) {
		String pageType = request.getParameter("pageType");//页面类型
		fcAccbookBlnedService.validVoucher(pageType, bookId, validType);
	}
	/**
	 * 冲账修改暂存FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
        processRushVoucher(false, request, voucherVO);
		return success();
	}

	/**
	 * 冲账提交FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
        processRushVoucher(true, request, voucherVO);
		return success();
	}

	/**
	 * 冲账暂存FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "添加FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcAccbookVoucherVO voucherVO) {
        processRushVoucher(false, request, voucherVO);
		return success();
	}

	private void processRushVoucher(boolean isSubmit, HttpServletRequest request,
			FcAccbookVoucherVO voucherVO) {
		String bankCode = voucherVO.getBankCode();
		//被冲账凭证ID
		String origVoucherNo = request.getParameter("origVoucherNo");//被冲账凭证ID
		String accbookId = request.getParameter("id");//冲账凭证产生的ID
		String busGlobalId = request.getParameter("busGlobalId");
		if (EmptyUtils.isEmpty(accbookId)) {//ID为空则是新提交的冲账，不为空则是修改
			validVoucher(request, origVoucherNo, CommonConstant.VALID_SIGN_YES);
		} else {
			validVoucher(request, busGlobalId, CommonConstant.VALID_SIGN_NO);
		}
		String rushTallyDate = request.getParameter("rushtallyDate");//冲账凭证的记账日期
		if (StringUtils.isEmpty(rushTallyDate)) {//记账日期
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
		}
		String rushinterestStartDate = request.getParameter("rushinterestStartDate");//冲账凭证的记账日期
		if (StringUtils.isEmpty(rushinterestStartDate)) {//起息日期
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
		}
		String desReason = request.getParameter("desReason");//冲账凭证的记账日期
		if (StringUtils.isEmpty(desReason)) {//冲账原因
			throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_DES_REASON_NO);
		}
		//录入新凭证ID
        String newId = request.getParameter("newId");

		FcAccbookVoucherVO rushVoucher = new FcAccbookVoucherVO();
		rushVoucher.setRush(true);//标识冲账

		FcAccbook accbook = new FcAccbook();
		accbook.setId(accbookId);//新凭证的ID主键
		accbook.setOrigVoucherNo(origVoucherNo);
		accbook.setBusGlobalId(busGlobalId);
		accbook.setTallyDate(DateUtils.str2Date(rushTallyDate, DateUtils.date_sdf()));//记账日期
		accbook.setInterestStartDate(DateUtils.str2Date(rushinterestStartDate, DateUtils.date_sdf()));//起息日期

		FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setDesReason(desReason);//冲账原因
		rushVoucher.setAccbook(accbook);
		rushVoucher.setBookExt(bookExt);
		VoucherWrapper voucher = setVoucherWrapper(rushVoucher);
		voucher.setBusGlobalId(busGlobalId);
		voucher.setUserCode(getUserVo().getId());//设置操作用户

		//是否录入新凭证
        String addNewVoucher = request.getParameter("addNewVoucher");
        if ("true".equals(addNewVoucher)) {
			voucherVO.setNetCode(voucherVO.getAccbook().getNetId());
        	//检验新录入凭证
        	fcAccbookBlnedService.checkAccbookBlnedData(voucherVO);
        	//判断是否有流水号
        	if (null != voucherVO.getAccbook().getTallyDate()) {
        		voucherVO.getAccbook().setBusGlobalId(busGlobalId);
        		voucherVO.setRush(true);
        		voucherVO.setNewRush(true);//新录入
        		voucher.addTarget(voucherVO);
        	}
        	fcAccbookService.saveExternal(voucherVO,bankCode);
        } else if (StringUtils.isNotBlank(newId)) {//修改时,说明原先已经录入了新凭证,需删除录入的新凭证
        	FcAccbookVoucherVO newVoucher = new FcAccbookVoucherVO();
        	newVoucher.setAccbook(new FcAccbook());
        	newVoucher.getAccbook().setId(newId);
        	newVoucher.setOpType(OperateType.DELETE);//删除操作
        	newVoucher.setBookDtls(new ArrayList<FcAccbookDtl>());
            voucher.addTarget(newVoucher);
		}
		if (isSubmit) {//提交
			rushService.submitRushVoucher(voucher);
        } else if (StringUtils.isBlank(accbook.getId())) {//新增
        	rushService.saveRushVoucher(voucher);
        } else {//更新
        	rushService.updateRushVoucher(voucher);
        }
	}

	private VoucherWrapper setVoucherWrapper(FcAccbookVoucherVO fcAccbook) {
		VoucherWrapper voucher = new VoucherWrapper();
		if (null != fcAccbook) {
			voucher.addTarget(fcAccbook);
		}
		voucher.setProcessType(ProcessType.PROCESS_RUSH);
		return voucher;
	}

	/**
	 * 复核通过FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(HttpServletRequest request) {
		String busGlobalId = request.getParameter("busGlobalId");
		validVoucher(request, busGlobalId, CommonConstant.VALID_SIGN_NO);
		VoucherWrapper voucher = setVoucherWrapper(null);
		voucher.setBusGlobalId(busGlobalId);
		voucher.setUserCode(getUserVo().getId());//设置用户

		rushService.checkRushVoucher(voucher);
		return success();
	}

	@LogMessage(content = "批量冲账复核通过", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchCheck")
	@ResponseBody
	public AjaxJson batchCheck(@RequestParam("ids") String ids) {
		for (String busGlobalId : ids.split(CommonConstant.COMMA_SPLIT)) {
			fcAccbookBlnedService.validVoucher(FinacialConstant.PAGE_CHECK, busGlobalId, CommonConstant.VALID_SIGN_NO);
			VoucherWrapper voucher = setVoucherWrapper(null);
			voucher.setBusGlobalId(busGlobalId);
			voucher.setUserCode(getUserVo().getId());//设置用户
			rushService.checkRushVoucher(voucher);
		}
		return success();
	}



	/**
	 * 打回冲账凭证FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(HttpServletRequest request) {
		String busGlobalId = request.getParameter("busGlobalId");
		validVoucher(request, busGlobalId, CommonConstant.VALID_SIGN_NO);
		String backReason = request.getParameter("backReason");//打回原因
		FcAccbookVoucherVO fcAccbook = new FcAccbookVoucherVO();
		FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setBackReason(backReason);
		fcAccbook.setBookExt(bookExt);
		VoucherWrapper voucher = setVoucherWrapper(fcAccbook);
		voucher.setBusGlobalId(busGlobalId);

		rushService.backRushVoucher(voucher);
		return success();
	}

	@LogMessage(content = "批量冲账打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids") String ids,@RequestParam("backMsg") String backMsg) {
		for (String busGlobalId : ids.split(CommonConstant.COMMA_SPLIT)) {
			fcAccbookBlnedService.validVoucher(FinacialConstant.PAGE_CHECK, busGlobalId, CommonConstant.VALID_SIGN_NO);
			FcAccbookVoucherVO fcAccbook = new FcAccbookVoucherVO();
			FcAccbookExt bookExt = new FcAccbookExt();
			bookExt.setBackReason(backMsg);
			fcAccbook.setBookExt(bookExt);
			VoucherWrapper voucher = setVoucherWrapper(fcAccbook);
			voucher.setBusGlobalId(busGlobalId);
			rushService.backRushVoucher(voucher);
		}
		return success();
	}

	/**
	 * 冲账抹账FC_ACCBOOK_BLNED
	 *
	 * @param request
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FCL_ACCBOOK_COMMON_APPLY)
	@LogMessage(content = "修改FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String busGlobalId = request.getParameter("busGlobalId");
		validVoucher(request, busGlobalId, CommonConstant.VALID_SIGN_NO);
		VoucherWrapper voucher = setVoucherWrapper(null);
		voucher.setBusGlobalId(busGlobalId);
		rushService.deleteRushVoucher(voucher);
		return success();
	}
	/**
	 * 批量删除FC_ACCBOOK_BLNED
	 */
	// //@RequiresPermissions("accbook:fcaccbookblned:delete")
	@LogMessage(content = "批量删除FC_ACCBOOK_BLNED", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			FcAccbookBlned fcAccbookBlned = fcAccbookBlnedService.get(id);
			fcAccbookBlnedService.deleteEntity(fcAccbookBlned);
		}
		return success();
	}
}
