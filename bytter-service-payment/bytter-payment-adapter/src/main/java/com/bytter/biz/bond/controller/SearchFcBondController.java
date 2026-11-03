package com.bytter.biz.bond.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.*;
import com.bytter.guarantee.api.ISearchFcBondService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.vo.*;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.export.ExcelExportServer;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtBankDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.DictDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 
 * @author fusb
 * @version 1.0
 * @Title SearchFcBondController
 * @Description 查询保证金信息 Controller
 * @date 2018年9月12日
 */
@Controller
@RequestMapping("/bond/fcbondInfo")
public class SearchFcBondController extends BaseController {
	@DubboReference(check = false)
	ISearchFcBondService searchFcBondService;
	@DubboReference(check = false)
	private IBisExcService iBisExcService;
	@Autowired
	NisAllocationReqService nisAllocationReqService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference
	private IDictService dictService;
    @DubboReference
    private IBtBankService btBankService;
	/**
	 * 保证金项目查看页面跳转
	 * 
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BOND_FCBONDINFO_APPLY)
	@LogMessage(content = "保证进行项目查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/listitem")
	public String listItem(Model model, String bondId) {
		model.addAttribute("bondId", bondId);
		return "bond/listSearchFcBondItem";
	}
	/**
	 * 保证金项目查询列表
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@LogMessage(content = "保证金项目查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return searchFcBondService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 保证金账户查询
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@LogMessage(content = "保证金账户列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable1")
	@ResponseBody
	public DataTablesOutput dataTable1(@RequestBody DataTablesInput dataTablesInput) {
		NisAllocationReqDTO dto=new NisAllocationReqDTO();
		List<NisAllocationReqDTO> list= nisAllocationReqService.getNisAllocationReqBillFrom(dto);
		if(list.size()>0) {
			String erpIds="";
			for (NisAllocationReqDTO nisAll : list) {
				erpIds+=nisAll.getErpId()+",";
			}
			erpIds=erpIds.substring(0,erpIds.length()-1);
			dataTablesInput.getFormdata().put("erpIds",erpIds);
		}

		return searchFcBondService.getDataTablesUnion1(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 保证金查看页面
	 */
	@LogMessage(content = "保证金转回操作页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/viewacc/{bondId}")
	public String backDetail(@PathVariable("bondId") String bondId, Model model) {
		if (StringUtils.isNotEmpty(bondId)) {
			// 得到保证金账户信息
			FcBondBackVO fcBondBackVO = searchFcBondService.getByIdFcBondBackVO(bondId);
			// 得到账户项目信息
			List<FcBondApplyVO> listFcBondApply = searchFcBondService.getByIdFcBondApply(bondId);

			// 得到保证金转回历史记录
			List<FcBondBackVO> listFcBondBack = searchFcBondService.getByIdFcBondBack(bondId);
			model.addAttribute("listFcBondBack", listFcBondBack);
			model.addAttribute("fcBondBackVO", fcBondBackVO);
			//model.addAttribute("listFcBondAdd", listFcBondAdd);
			model.addAttribute("listFcBondApply", listFcBondApply);
		}
		return "bond/viewSearchFcBondAcc";
	}

	@PostMapping("/getFcBondAdds")
	@ResponseBody
	public DataTablesOutput getFcBondAdds(@RequestBody DataTablesInput input){
		DataTablesOutput output = new DataTablesOutput();
		output.setDraw(input.getDraw());
		// 得到保证金账户追加明细
		List<FcBondAddDTO> listFcBondAdd = searchFcBondService.getFcBondAddsByBondId(input.getFormdata().get("bondId").toString());
		if (CollectionUtils.isEmpty(listFcBondAdd)) return output;
		output.setRecordsTotal(listFcBondAdd.size());
		output.setRecordsFiltered(listFcBondAdd.size());
		int start = input.getStart();
		int length = input.getLength();
		if(start >= listFcBondAdd.size()){
			return output;
		}
		if(listFcBondAdd.size() - start <= length){
			length = listFcBondAdd.size() - start;
		}
		String order = !CollectionUtil.isEmpty(input.getOrder()) && "asc".equals(input.getOrder().get(0).getDir()) ? "asc" : "desc";
		if("desc".equals(order)){
			listFcBondAdd = listFcBondAdd.stream().sorted(Comparator.comparing(FcBondAddDTO::getAddDate).reversed()).collect(Collectors.toList());
		}else {
			listFcBondAdd = listFcBondAdd.stream().sorted(Comparator.comparing(FcBondAddDTO::getAddDate)).collect(Collectors.toList());
		}
		List<FcBondAddDTO> newList = new ArrayList<>();
		for(int i = 0; i < listFcBondAdd.size(); i++){
			if(i < start){
				continue;
			}
			FcBondAddDTO fcBondAddDTO = listFcBondAdd.get(i);
			fcBondAddDTO.setBillNo((i-start+1)+"");
			newList.add(fcBondAddDTO);
			if(newList.size() == length){
				break;
			}
		}
		output.setData(newList);
		return output;
	}

	/**
	 * 通过id查询得到FcBondApplyVO
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@GetMapping("view/{id}")
	public String getFcBondApplyVOById(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondApplyVO fcBondApplyVO = searchFcBondService.getFcBondApplyVO(id);
			model.addAttribute("fcBondApplyVO", fcBondApplyVO);
		}
		return "bond/viewSearchFcBondItem";
	}
	
	/**
	 * 通过项目id和项目单号查询项目占用的保证金信息
	 * @param itemId
	 * @param itemNo
	 * @param model
	 * @return
	 */
	@GetMapping("/viewApplyInfo/{itemId}/{itemNo}")
	public String getFcBondApplyInfoVO(@PathVariable("itemId") String itemId,@PathVariable("itemNo") String itemNo,Model model){
		FcBondApplyInfoVO info = searchFcBondService.getFcBondApplyInfo(itemId,itemNo);
		model.addAttribute("info", info);
		return "bond/viewFcBondApplyInfo";
	}
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBOND_QUERY)
	@LogMessage(content = "跳转到保证金额度占用统计列表页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/queryBondUse")
	public String queryBondUse(FcBondApplyVO applyVO,Model model) {
		//显示存入保证金的银行信息
		List<FcBondVO> fcBondVOList = searchFcBondService.queryBondBank();
		List<BtBankDTO> bankDTOs = BeanUtils.copyListProperties(fcBondVOList, BtBankDTO.class);
		//显示占用保证金的单位信息
		List<FcBondApplyDTO> FcBondApplyCorp = searchFcBondService.queryBondCorp(FcBondConstant.NETBANK_FCBOND_QUERY, getCurrentUser());
		List<CorpDTO> corpDTOS = new ArrayList<CorpDTO>();
		if(!CollectionUtils.isEmpty(FcBondApplyCorp)) {
			for(FcBondApplyDTO fcBondApplyDTO : FcBondApplyCorp) {
				CorpDTO corpDTO = new CorpDTO();
				corpDTO.setId(fcBondApplyDTO.getCorpId());
				corpDTO.setName(fcBondApplyDTO.getCorpName());
				corpDTOS.add(corpDTO);
			}
		}
		List<FcBondUseVO> bondUseVOS=searchFcBondService.getBondUseList(applyVO, FcBondConstant.NETBANK_FCBOND_QUERY, getCurrentUser());
		model.addAttribute("bondUseVOS",bondUseVOS);
		model.addAttribute("bankDTOs",bankDTOs);
		model.addAttribute("corpDTOS",corpDTOS);
		model.addAttribute("applyVO",applyVO);
		return "bond/listBondUse";
	}
	@RequestMapping("/viewBondUseDtl")
	@LogMessage(content = "跳转到保证金占用明细页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	public String viewBondUseDtl(FcBondApplyVO applyVO, Model model){
		//显示存入保证金的银行信息
		List<FcBondVO> fcBondVOList = searchFcBondService.queryBondBank();
        String bankId = applyVO.getBankId();
        if(StringUtils.isNotBlank(bankId)){
			BtBankDTO btBankDTO = btBankService.getByBankCode(bankId);
            if(!EmptyUtils.isEmpty(btBankDTO)){
                applyVO.setBankName(btBankDTO.getBankName());
            }
        }
		List<FcBondUseDtlVO> useDtlVOS = searchFcBondService.queryBondUseDtl(applyVO);
		model.addAttribute("useDtlVOS",useDtlVOS);
		model.addAttribute("applyVO",applyVO);
		return "bond/viewBondUseDetail";
	}
	/**
	 * 保证金项目明细
	 */
	@LogMessage(content = "保证金项目明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/BondApplyDtl/{bondId}/{param}")
	public String BondApplyDtl(@PathVariable("bondId") String bondId,@PathVariable("param") String param, Model model) {
		if (StringUtils.isNotEmpty(bondId)) {
			// 得到账户项目信息
			List<FcBondApplyVO> listFcBondApply = searchFcBondService.getByIdFcBondApply(bondId,param);
			model.addAttribute("listFcBondApply", listFcBondApply);
			String status = "";
			String payBillNo = "";
			String bursarBillNo = "";
			String origNo = "";
			String startDate = "";
			String endDate = "";
			if("null".equals(param)){
				param = "";
			}else {
				if(EmptyUtils.isNotEmpty(param)){
					if(param.contains(",")){
						if(param.split(",").length >=1) {
							status = param.split(",")[0];
						}
						if(param.split(",").length >=2) {
							payBillNo = param.split(",")[1];
						}
						if(param.split(",").length >=3) {
							bursarBillNo = param.split(",")[2];
						}
						if(param.split(",").length >=4) {
							origNo = param.split(",")[3];
						}
						if(param.split(",").length >=5) {
							startDate = param.split(",")[4];
						}
						if(param.split(",").length >=6) {
							endDate = param.split(",")[5];
						}
					}
				}
			}
			model.addAttribute("status", status);
			model.addAttribute("payBillNo", payBillNo);
			model.addAttribute("bursarBillNo", bursarBillNo);
			model.addAttribute("origNo", origNo);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("param", param);
		}
		return "bond/viewBondApplyDtll";
	}

	@LogMessage(content = "账号项目信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/excelBondApplyDtl")
	public void excelBondApplyDtl(String bondId,String param, HttpServletResponse response){
		if (StringUtils.isNotEmpty(bondId)) {
			// 得到账户项目信息
			List<FcBondApplyVO> listFcBondApply = searchFcBondService.getByIdFcBondApply(bondId,param);
			if("null".equals(param)){
				param = "";
			}
			String filename = "账号项目信息.xls";//报表名称：银行授信使用统计表.xls
			// 声明一个工作薄
			HSSFWorkbook wb = new HSSFWorkbook();
			//声明一个单子并命名
			HSSFSheet sheet = wb.createSheet(filename);
			//给单子名称一个长度
			sheet.setDefaultColumnWidth((short) 18);
			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();
			//创建第一行（也可以称为表头）
			HSSFRow row = sheet.createRow(0);
			//设置表头字体
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 14);// 字体大小
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			style.setFont(headfont);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			this.setBorder(style);
			//给表头第一行一次创建单元格
			String[] heads = new String[]{
					"序号","项目单号","项目类型","大区","营运区","公司类型","开户行","付款单号","共享单号","票据号码","项目开始日期","项目到期日期","项目占用金额","状态","释放日期"
			};
			row.setHeightInPoints(20);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell((short) i);
				cell.setCellValue(heads[i]);
				cell.setCellStyle(style);
				sheet.setColumnWidth(i, 256 * 18);
			}
			//填充明细数据
			DecimalFormat df = new DecimalFormat("#,##0.00");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
			HSSFCellStyle rightCellStyle = getRightCellStyle(wb);

			for(int i = 0; i < listFcBondApply.size(); i++) {
				FcBondApplyVO fcBondApplyVO = listFcBondApply.get(i);
				HSSFRow row1 = sheet.createRow(i+1);
				HSSFCell cell1 = row1.createCell(0);
				cell1.setCellStyle(centerCellStyle);
				if("汇总".equals(fcBondApplyVO.getOrigNo())){
					cell1.setCellValue("");
				}else {
					cell1.setCellValue(i + 1);
				}

				HSSFCell cell2 = row1.createCell(1);
				cell2.setCellStyle(centerCellStyle);
				cell2.setCellValue(fcBondApplyVO.getItemNo());

				HSSFCell cell3 = row1.createCell(2);
				cell3.setCellStyle(centerCellStyle);
				String itemType = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getItemType())) {
					itemType = dictService.get("fcBond.itemType", fcBondApplyVO.getItemType());
				}
				cell3.setCellValue(itemType);

				HSSFCell cell31 = row1.createCell(3);
				cell31.setCellStyle(centerCellStyle);
				String parentName = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getParentName())) {
					parentName = fcBondApplyVO.getParentName();
				}
				cell31.setCellValue(parentName);

				HSSFCell cell32 = row1.createCell(4);
				cell32.setCellStyle(centerCellStyle);
				String operationAreaName = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getOperationAreaName())) {
					operationAreaName = fcBondApplyVO.getOperationAreaName();
				}
				cell32.setCellValue(operationAreaName);

				HSSFCell cell33 = row1.createCell(5);
				cell33.setCellStyle(centerCellStyle);
				String equityName = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getEquityName())) {
					equityName = fcBondApplyVO.getEquityName();
				}
				cell33.setCellValue(equityName);

				HSSFCell cell34 = row1.createCell(6);
				cell34.setCellStyle(centerCellStyle);
				String bankName = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getBankName())) {
					bankName = fcBondApplyVO.getBankName();
				}
				cell34.setCellValue(bankName);

				HSSFCell cell35 = row1.createCell(7);
				cell35.setCellStyle(centerCellStyle);
				String payBillNo = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getPayBillNo())) {
					payBillNo = fcBondApplyVO.getPayBillNo();
				}
				cell35.setCellValue(payBillNo);

				HSSFCell cell36 = row1.createCell(8);
				cell36.setCellStyle(centerCellStyle);
				String bursarBillNo = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getBursarBillNo())) {
					bursarBillNo = fcBondApplyVO.getBursarBillNo();
				}
				cell36.setCellValue(bursarBillNo);

				HSSFCell cell37 = row1.createCell(9);
				cell37.setCellStyle(centerCellStyle);
				String origNo = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getOrigNo())) {
					origNo = fcBondApplyVO.getOrigNo();
				}
				cell37.setCellValue(origNo);

				HSSFCell cell4 = row1.createCell(10);
				cell4.setCellStyle(centerCellStyle);
				String startDate = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getStartDate())) {
					startDate = simpleDateFormat.format(fcBondApplyVO.getStartDate());
				}
				cell4.setCellValue(startDate);

				HSSFCell cell5 = row1.createCell(11);
				cell5.setCellStyle(centerCellStyle);
				String endDate = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getEndDate())) {
					endDate = simpleDateFormat.format(fcBondApplyVO.getEndDate());
				}
				cell5.setCellValue(endDate);

				HSSFCell cell6 = row1.createCell(12);
				cell6.setCellStyle(rightCellStyle);
				String applyMoney = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getApplyMoney())) {
					applyMoney = df.format(fcBondApplyVO.getApplyMoney());
				}
				cell6.setCellValue(applyMoney);

				HSSFCell cell7 = row1.createCell(13);
				cell7.setCellStyle(centerCellStyle);
				String statusName = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getStatus())) {
					statusName = dictService.get("fcBond.status", fcBondApplyVO.getStatus());
				}
				cell7.setCellValue(statusName);

				HSSFCell cell8 = row1.createCell(14);
				cell8.setCellStyle(centerCellStyle);
				String releaseDate = "";
				if(!EmptyUtils.isEmpty(fcBondApplyVO.getReleaseDate())) {
					releaseDate = simpleDateFormat.format(fcBondApplyVO.getReleaseDate());
				}
				cell8.setCellValue(releaseDate);
			}
			try {
				//实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
				OutputStream out = response.getOutputStream();
				response.reset();
				response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
				response.setContentType("application/x-download");
				wb.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 保证金交易明细
	 */
	@LogMessage(content = "保证金交易明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/BondTransDtl/{bondId}")
	public String BondTransDtl(@PathVariable("bondId") String bondId, Model model) {
		if (StringUtils.isNotEmpty(bondId)) {
			// 得到账户项目信息
			List<FcBondUseDtlVO> transDtl = searchFcBondService.getBondTransDtl(bondId);
			List<String> billIds=new ArrayList<>();
			//查询资金调拨制单表
			for(FcBondUseDtlVO vo:transDtl){
				NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getNisAllocationReqByErpId(vo.getId());
				if(!EmptyUtils.isEmpty(nisAllocationReqListDTO)){
					vo.setNisStatus(nisAllocationReqListDTO.getStatus());
					vo.setNisBillNo(nisAllocationReqListDTO.getBillNo());
					vo.setCbsBillId(nisAllocationReqListDTO.getId());
					billIds.add(vo.getCbsBillId());
				}
			}
			//查询指令
			Map<String, BisExcDTO> excMap= iBisExcService.getBisExcMapByCbsBillIds( billIds, "");
			for(FcBondUseDtlVO vo:transDtl){
				if(StringUtils.isNotEmpty(vo.getCbsBillId())) {
					BisExcDTO bisExcDTO = excMap.get(vo.getCbsBillId());
					if(!EmptyUtils.isEmpty(bisExcDTO)){
						vo.setVoucherStat(bisExcDTO.getVoucherStat());
					}
				}
			}

			model.addAttribute("transDtl", transDtl);
			model.addAttribute("bondId", bondId);
		}
		return "bond/viewBondTransDtl";
	}

	@LogMessage(content = "保证金交易明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/excelBondTransDtl")
	public void excelBondTransDtl(String bondId, HttpServletResponse response){
		if (StringUtils.isNotEmpty(bondId)) {
			// 得到账户项目信息
			List<FcBondUseDtlVO> transDtl = searchFcBondService.getBondTransDtl(bondId);
			List<String> billIds=new ArrayList<>();
			//查询资金调拨制单表
			for(FcBondUseDtlVO vo:transDtl){
				NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getNisAllocationReqByErpId(vo.getId());
				if(!EmptyUtils.isEmpty(nisAllocationReqListDTO)){
					vo.setNisStatus(nisAllocationReqListDTO.getStatus());
					vo.setNisBillNo(nisAllocationReqListDTO.getBillNo());
					vo.setCbsBillId(nisAllocationReqListDTO.getId());
					billIds.add(vo.getCbsBillId());
				}
			}
			//查询指令
			Map<String, BisExcDTO> excMap= iBisExcService.getBisExcMapByCbsBillIds( billIds, "");
			for(FcBondUseDtlVO vo:transDtl){
				if(StringUtils.isNotEmpty(vo.getCbsBillId())) {
					BisExcDTO bisExcDTO = excMap.get(vo.getCbsBillId());
					if(!EmptyUtils.isEmpty(bisExcDTO)){
						vo.setVoucherStat(bisExcDTO.getVoucherStat());
					}
				}
			}
			String filename = "保证金交易明细.xls";//报表名称：银行授信使用统计表.xls
			// 声明一个工作薄
			HSSFWorkbook wb = new HSSFWorkbook();
			//声明一个单子并命名
			HSSFSheet sheet = wb.createSheet(filename);
			//给单子名称一个长度
			sheet.setDefaultColumnWidth((short) 18);
			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();
			//创建第一行（也可以称为表头）
			HSSFRow row = sheet.createRow(0);
			//设置表头字体
			HSSFFont headfont = wb.createFont();
			headfont.setFontName("宋体");
			headfont.setFontHeightInPoints((short) 14);// 字体大小
			headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
			style.setFont(headfont);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
			this.setBorder(style);
			//给表头第一行一次创建单元格
			String[] heads = new String[]{
					"序号","操作人","操作时间","操作类型","交易日期","转出/转回账号","交易金额","状态","调拨单号","指令状态"
			};
			row.setHeightInPoints(20);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell((short) i);
				cell.setCellValue(heads[i]);
				cell.setCellStyle(style);
				sheet.setColumnWidth(i, 256 * 18);
			}
			//填充明细数据
			DecimalFormat df = new DecimalFormat("#,##0.00");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
			HSSFCellStyle rightCellStyle = getRightCellStyle(wb);

			for(int i = 0; i < transDtl.size(); i++) {
				FcBondUseDtlVO fcBondUseDtlVO = transDtl.get(i);
				HSSFRow row1 = sheet.createRow(i+1);
				HSSFCell cell1 = row1.createCell(0);
				cell1.setCellStyle(centerCellStyle);
				cell1.setCellValue(i+1);

				HSSFCell cell2 = row1.createCell(1);
				cell2.setCellStyle(centerCellStyle);
				cell2.setCellValue(fcBondUseDtlVO.getCreateBy());

				HSSFCell cell3 = row1.createCell(2);
				cell3.setCellStyle(centerCellStyle);
				cell3.setCellValue(simpleDateFormat.format(fcBondUseDtlVO.getCreateDate()));

				HSSFCell cell4 = row1.createCell(3);
				cell4.setCellStyle(centerCellStyle);
				String busSign = "";
				if("b".equals(fcBondUseDtlVO.getBusSign())){
					busSign = "保证金存入";
				}else if("a".equals(fcBondUseDtlVO.getBusSign())){
					busSign = "保证金追加";
				} else if ("c".equals(fcBondUseDtlVO.getBusSign())) {
					busSign = "保证金转回";
				}
				cell4.setCellValue(busSign);

				HSSFCell cell5 = row1.createCell(4);
				cell5.setCellStyle(centerCellStyle);
				cell5.setCellValue(simpleDateFormat.format(fcBondUseDtlVO.getStartDate()));

				HSSFCell cell6 = row1.createCell(5);
				cell6.setCellStyle(centerCellStyle);
				cell6.setCellValue(fcBondUseDtlVO.getOutBankAcc());

				HSSFCell cell7 = row1.createCell(6);
				cell7.setCellStyle(rightCellStyle);
				cell7.setCellValue(df.format(fcBondUseDtlVO.getMoney()));

				HSSFCell cell8 = row1.createCell(7);
				cell8.setCellStyle(centerCellStyle);
				String statusName = "";
				if(!EmptyUtils.isEmpty(fcBondUseDtlVO.getNisStatus())){
					statusName = dictService.get("netbank.statusAll", fcBondUseDtlVO.getNisStatus());
				}else{
					statusName = dictService.get("common.status", fcBondUseDtlVO.getStatus());
				}
				cell8.setCellValue(statusName);

				HSSFCell cell9 = row1.createCell(8);
				cell9.setCellStyle(centerCellStyle);
				String nisBillNo = "";
				if(EmptyUtils.isNotEmpty(fcBondUseDtlVO.getNisBillNo())){
					nisBillNo = fcBondUseDtlVO.getNisBillNo();
				}else{
					nisBillNo = "-";
				}
				cell9.setCellValue(nisBillNo);

				HSSFCell cell10 = row1.createCell(9);
				cell10.setCellStyle(centerCellStyle);
				String voucherStat = "";
				if(EmptyUtils.isNotEmpty(fcBondUseDtlVO.getVoucherStat())){
					voucherStat = fcBondUseDtlVO.getVoucherStat();
				}else{
					voucherStat = "-";
				}
				cell10.setCellValue(voucherStat);



			}
			try {
				//实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
				OutputStream out = response.getOutputStream();
				response.reset();
				response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
				response.setContentType("application/x-download");
				wb.write(out);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@LogMessage(content = "导出保证金占用明细统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@RequestMapping("/excel")
	public String excel(FcBondApplyVO applyVO, HttpServletResponse response, HttpServletRequest request) {
		List<FcBondUseDtlVO> list = searchFcBondService.queryBondUseDtl(applyVO);
		String filename = "保证金占用明细统计表.xls";//报表名称：银行授信使用统计表.xls
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet(filename);
		sheet.setColumnWidth(1, 30 * 256);
		sheet.setColumnWidth(7, 25 * 256);
		sheet.setColumnWidth(8, 30 * 256);
		sheet.setColumnWidth(9, 40 * 256);
//		sheet.setColumnWidth(5, 25 * 256);//设置第六列列宽
//		for (int i = 10; i < 18; i++) {
//			sheet.setColumnWidth(i, 20 * 256);//设置第11-18列列宽
//		}
		//创建表格样式
		creatExcel(wb, sheet);
		//填充数据
		wb = this.addDataToSheet(list, sheet, wb);
		try {
			//实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
			OutputStream out = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
			response.setContentType("application/x-download");
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping("/excelBondAdd")
	public String excelBondAdd(@RequestBody FcBondAddDTO applyVO, HttpServletResponse response, HttpServletRequest request) {
		String bondId =  applyVO.getBondId();
		// 得到保证金账户信息
		FcBondBackVO fcBondBackVO = searchFcBondService.getByIdFcBondBackVO(bondId);
		if(fcBondBackVO == null){
			return null;
		}
		List<FcBondAddDTO> listFcBondAdd = searchFcBondService.getFcBondAddsByBondId(bondId);
		if(CollectionUtil.isEmpty(listFcBondAdd)){
			return null;
		}
		if(applyVO.getOrder().contains("asc")){
			listFcBondAdd = listFcBondAdd.stream().sorted(Comparator.comparing(FcBondAddDTO :: getAddDate)).collect(Collectors.toList());
		}else {
			listFcBondAdd = listFcBondAdd.stream().sorted(Comparator.comparing(FcBondAddDTO :: getAddDate).reversed()).collect(Collectors.toList());
		}

		for(int i = 0; i < listFcBondAdd.size(); i++){
			FcBondAddDTO dto = listFcBondAdd.get(i);
			dto.setBillNo((i+1)+"");
		}
		String filename = "保证金账户历史追加明细.xls";//报表名称：银行授信使用统计表.xls
//		// 声明一个工作薄
		//HSSFWorkbook wb = new HSSFWorkbook();
		Workbook wb = new XSSFWorkbook();
		//(new ExcelExportServer()).createSheetForMap(wb, new ExportParams(), new ArrayList<>(), new HashSet<>());
//		//声明一个单子并命名
		Sheet sheet = wb.createSheet(filename);
//		sheet.setColumnWidth(1, 30 * 256);
//		sheet.setColumnWidth(7, 25 * 256);
//		sheet.setColumnWidth(8, 30 * 256);
//		sheet.setColumnWidth(9, 40 * 256);
////		sheet.setColumnWidth(5, 25 * 256);//设置第六列列宽
////		for (int i = 10; i < 18; i++) {
////			sheet.setColumnWidth(i, 20 * 256);//设置第11-18列列宽
////		}
//		//创建表格样式
		creatExcel2(wb, sheet);
//		//填充数据
		wb = this.addDataToSheet2(fcBondBackVO, sheet, wb);

		//创建表格样式
		creatExcel3(wb, sheet);
//
		addDataToSheet3(listFcBondAdd,sheet,wb);
		try {
//			//实现Excel以文件流的形式到浏览器中,浏览器左下角出现Excel的下载提示，
			OutputStream out = response.getOutputStream();
			//response.reset();
			//String encodedFileName = java.net.URLEncoder.encode(filename, "UTF-8");
			//response.setHeader("Conten -disposition", "attachment; filename*=UTF-8''" + encodedFileName);
			//response.setHeader("Content-disposition", "attachment; filename=" + new String(filename.getBytes(), "iso-8859-1"));
			//response.setContentType("application/x-download");

			//判断浏览器类型
			filename = URLEncoder.encode(filename, "UTF8");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Conten -disposition", "attachment; filename*=UTF-8''" + filename);
			response.setHeader("Pragma", "No-cache");
			wb.write(out);
			out.flush();
			out.close();
			wb.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 填充数据
	 */
	private Workbook addDataToSheet3(List<FcBondAddDTO> listFcBondAdd, Sheet sheet, Workbook wb) {
		//填充明细数据
		DecimalFormat df = new DecimalFormat("#,##0.00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CellStyle centerCellStyle = getCenterCellStyle2(wb);
		CellStyle rightCellStyle = getRightCellStyle2(wb);

		for(int i = 0; i < listFcBondAdd.size(); i++) {
			FcBondAddDTO dto = listFcBondAdd.get(i);
			Row row = sheet.createRow(i+5);
			Cell cell1 = row.createCell(0);
			cell1.setCellStyle(centerCellStyle);
			cell1.setCellValue(dto.getBillNo());
			Cell cell2 = row.createCell(1);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue(dto.getCreateByName());
			Cell cell3 = row.createCell(2);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue(simpleDateFormat.format(dto.getAddDate()));
			Cell cell4 = row.createCell(3);
			cell4.setCellStyle(centerCellStyle);
			cell4.setCellValue(dto.getAddMoney().toString());
		}

		return wb;
	}

	private Workbook addDataToSheet2(FcBondBackVO fcBondBackVO, Sheet sheet, Workbook wb) {
		//填充明细数据
		DecimalFormat df = new DecimalFormat("#,##0.00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CellStyle centerCellStyle = getCenterCellStyle2(wb);
		CellStyle rightCellStyle = getRightCellStyle2(wb);
		Row row = sheet.createRow(2);
		//"单位名称","存入账号","账户名称","银行名称","币别","保证金类型","存出账号","是否存入定期","利率","是否已付款","用途","备注","当前账户余额","占用金额","可用金额","利息收入"
		Cell cell1 = row.createCell(0);
		cell1.setCellStyle(centerCellStyle);
		cell1.setCellValue(fcBondBackVO.getCorpName());
		Cell cell2 = row.createCell(1);
		cell2.setCellStyle(centerCellStyle);
		cell2.setCellValue(fcBondBackVO.getBankAcc());
		Cell cell3 = row.createCell(2);
		cell3.setCellStyle(centerCellStyle);
		cell3.setCellValue(fcBondBackVO.getAccName());
		Cell cell4 = row.createCell(3);
		cell4.setCellStyle(centerCellStyle);
		//cell4.setCellValue(dictService.get("fcBond.itemType", fcBondUseDtlVO.getItemType()));
		cell4.setCellValue(fcBondBackVO.getBankName());
		Cell cell5 = row.createCell(4);
		cell5.setCellStyle(centerCellStyle);
		cell5.setCellValue(fcBondBackVO.getCurName());
		Cell cell6 = row.createCell(5);
		cell6.setCellStyle(centerCellStyle);
		cell6.setCellValue(fcBondBackVO.getBondTypeName());
		Cell cell7 = row.createCell(6);
		cell7.setCellStyle(centerCellStyle);
		cell7.setCellValue(fcBondBackVO.getOutBankAcc());
		Cell cell8 = row.createCell(7);
		cell8.setCellStyle(centerCellStyle);
		cell8.setCellValue(StringUtils.isBlank(fcBondBackVO.getIsRegular()) ? "" : ("1".equals(fcBondBackVO.getIsRegular()) ? "是" : "否"));
		Cell cell9 = row.createCell(8);
		cell9.setCellStyle(centerCellStyle);
		cell9.setCellValue(fcBondBackVO.getInterestRate() == null ? "" : fcBondBackVO.getInterestRate().multiply(new BigDecimal("100")).toString() + "%");
		Cell cell10 = row.createCell(9);
		cell10.setCellStyle(centerCellStyle);
		cell10.setCellValue(StringUtils.isBlank(fcBondBackVO.getIsPayed()) ? "" : (fcBondBackVO.getIsPayed().equals("1") ? "是" : "否"));
		Cell cell11 = row.createCell(10);
		cell11.setCellStyle(rightCellStyle);
		cell11.setCellValue(fcBondBackVO.getPurpose());
		Cell cell12 = row.createCell(11);
		cell12.setCellStyle(rightCellStyle);
		cell12.setCellValue(fcBondBackVO.getRemark());
		Cell cell13 = row.createCell(12);
		cell13.setCellStyle(rightCellStyle);
		cell13.setCellValue(fcBondBackVO.getBalMoney() == null ? "" : fcBondBackVO.getBalMoney().toString());
		Cell cell14 = row.createCell(13);
		cell14.setCellStyle(rightCellStyle);
		cell14.setCellValue(fcBondBackVO.getApplyMoney() == null ? "" : fcBondBackVO.getApplyMoney().toString());
		Cell cell15 = row.createCell(14);
		cell15.setCellStyle(rightCellStyle);
		cell15.setCellValue(fcBondBackVO.getAvailMoney() == null ? "" : fcBondBackVO.getAvailMoney().toString());
		Cell cell16 = row.createCell(15);
		cell16.setCellStyle(rightCellStyle);
		cell16.setCellValue(fcBondBackVO.getBackInterest() == null ?  "" : fcBondBackVO.getBackInterest().toString());
		return wb;
	}


	/**
	 * 创建表格样式
	 */
	private void creatExcel2(Workbook wb, Sheet sheet) {
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 18);
		// 生成一个样式
		CellStyle style = wb.createCellStyle();
		//创建第一行（也可以称为表头）
		Row row = sheet.createRow(0);
		// 创建合并区域，参数说明：起始行号，终止行号， 起始列号，终止列号
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 15);
		sheet.addMergedRegion(cellRangeAddress);
		//设置表头字体
		Font headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 14);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		style.setFont(headfont);
		style.setAlignment(HorizontalAlignment.CENTER); // 居中
		this.setBorder2(style);
		row.setHeightInPoints(20);
		Cell cell = row.createCell((short) 0);
		cell.setCellValue("保证金账户信息");
		cell.setCellStyle(style);
		//给表头第一行一次创建单元格
		String[] heads = new String[]{
				"单位名称","存入账号","账户名称","银行名称","币别","保证金类型","存出账号","是否存入定期","利率","是否已付款","用途","备注","当前账户余额","占用金额","可用金额","利息收入"
		};
		Row row1 = sheet.createRow(1);
		row.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			Cell cell1 = row1.createCell((short) i);
			cell1.setCellValue(heads[i]);
			cell1.setCellStyle(style);
			sheet.setColumnWidth(i, 256 * 18);
		}
	}

	/**
	 * 创建表格样式
	 */
	private void creatExcel3(Workbook wb, Sheet sheet) {
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 18);
		// 生成一个样式
		CellStyle style = wb.createCellStyle();
		//创建第一行（也可以称为表头）
		Row row = sheet.createRow(3);
		// 创建合并区域，参数说明：起始行号，终止行号， 起始列号，终止列号
		CellRangeAddress cellRangeAddress = new CellRangeAddress(3, 3, 0, 3);
		sheet.addMergedRegion(cellRangeAddress);
		//设置表头字体
		Font headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 14);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		style.setFont(headfont);
		style.setAlignment(HorizontalAlignment.CENTER); // 居中
		this.setBorder2(style);
		row.setHeightInPoints(20);
		Cell cell = row.createCell((short) 0);
		cell.setCellValue("历史追加明细");
		cell.setCellStyle(style);
		//给表头第一行一次创建单元格
		String[] heads = new String[]{
				"序号","追加操作人","追加日期","追加金额"
		};
		Row row1 = sheet.createRow(4);
		row.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			Cell cell1 = row1.createCell((short) i);
			cell1.setCellValue(heads[i]);
			cell1.setCellStyle(style);
			sheet.setColumnWidth(i, 256 * 18);
		}
	}

	/**
	 * 创建表格样式
	 */
	public void creatExcel(HSSFWorkbook wb, HSSFSheet sheet) {
		//给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 18);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		//创建第一行（也可以称为表头）
		HSSFRow row = sheet.createRow(0);
		//设置表头字体
		HSSFFont headfont = wb.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 14);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		style.setFont(headfont);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		this.setBorder(style);
		//给表头第一行一次创建单元格
		String[] heads = new String[]{
				multiLangServiceI.getLang("table.fcBond.corpCode"),//单位代码
				multiLangServiceI.getLang("table.fcBond.corpName"),//单位名称
				multiLangServiceI.getLang("table.fcBond.billNo"),//项目单号
				multiLangServiceI.getLang("table.fcBond.itemType"),//项目类型
				multiLangServiceI.getLang("table.fcBond.itemStartDate"),//项目开始日期
				multiLangServiceI.getLang("table.fcBond.itemEndDate"),//项目到期日期
				multiLangServiceI.getLang("table.fcBond.itemUnit") + "(" + multiLangServiceI.getLang("table.fcBond.month") + ")",//项目期限(月)
				multiLangServiceI.getLang("table.fcBond.depositaccount"),//存入账户
				multiLangServiceI.getLang("table.fcBond.accountname"),//账号名称
				multiLangServiceI.getLang("table.fcBond.bankName"),//存入银行
				multiLangServiceI.getLang("table.fcBond.bondmoney"),//保证金金额
				multiLangServiceI.getLang("table.fcBond.occupyMoney"),//占用金额
		};
		row.setHeightInPoints(20);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell cell = row.createCell((short) i);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(style);
			//sheet.setColumnWidth(i, 256 * 18);
		}
	}

	/**
	 * 设置表格所有边框
	 */
	public void setBorder(HSSFCellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	}

	public void setBorder2(CellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
	}

	/**
	 * 填充数据
	 */
	private HSSFWorkbook addDataToSheet(List<FcBondUseDtlVO> list, HSSFSheet sheet, HSSFWorkbook wb) {
		//填充明细数据
		DecimalFormat df = new DecimalFormat("#,##0.00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		HSSFCellStyle centerCellStyle = getCenterCellStyle(wb);
		HSSFCellStyle rightCellStyle = getRightCellStyle(wb);

		for(int i = 0; i < list.size(); i++) {
			FcBondUseDtlVO fcBondUseDtlVO = list.get(i);
			HSSFRow row = sheet.createRow(i+1);
			HSSFCell cell1 = row.createCell(0);
			cell1.setCellStyle(centerCellStyle);
			cell1.setCellValue(fcBondUseDtlVO.getCorpCode());
			HSSFCell cell2 = row.createCell(1);
			cell2.setCellStyle(centerCellStyle);
			cell2.setCellValue(fcBondUseDtlVO.getCorpName());
			HSSFCell cell3 = row.createCell(2);
			cell3.setCellStyle(centerCellStyle);
			cell3.setCellValue(fcBondUseDtlVO.getItemNo());
			HSSFCell cell4 = row.createCell(3);
			cell4.setCellStyle(centerCellStyle);
			cell4.setCellValue(dictService.get("fcBond.itemType", fcBondUseDtlVO.getItemType()));
			HSSFCell cell5 = row.createCell(4);
			cell5.setCellStyle(centerCellStyle);
			cell5.setCellValue(simpleDateFormat.format(fcBondUseDtlVO.getStartDate()));
			HSSFCell cell6 = row.createCell(5);
			cell6.setCellStyle(centerCellStyle);
			cell6.setCellValue(simpleDateFormat.format(fcBondUseDtlVO.getEndDate()));
			HSSFCell cell7 = row.createCell(6);
			cell7.setCellStyle(centerCellStyle);
			cell7.setCellValue(fcBondUseDtlVO.getItemUnit());
			HSSFCell cell8 = row.createCell(7);
			cell8.setCellStyle(centerCellStyle);
			cell8.setCellValue(fcBondUseDtlVO.getBankAcc());
			HSSFCell cell9 = row.createCell(8);
			cell9.setCellStyle(centerCellStyle);
			cell9.setCellValue(fcBondUseDtlVO.getAccName());
			HSSFCell cell10 = row.createCell(9);
			cell10.setCellStyle(centerCellStyle);
			cell10.setCellValue(fcBondUseDtlVO.getBankName());
			HSSFCell cell11 = row.createCell(10);
			cell11.setCellStyle(rightCellStyle);
			cell11.setCellValue(df.format(fcBondUseDtlVO.getBondMoney()));
			HSSFCell cell12 = row.createCell(11);
			cell12.setCellStyle(rightCellStyle);
			cell12.setCellValue(df.format(fcBondUseDtlVO.getApplyMoney()));
		}

		return wb;
	}

	/**
	 * 获取居中样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getCenterCellStyle(HSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.CENTER);
	}

	public CellStyle getCenterCellStyle2(Workbook workbook) {
		return initStyle2(workbook, HorizontalAlignment.CENTER);
	}
	/**
	 * 获取靠右样式
	 *
	 * @throws Exception
	 */
	public HSSFCellStyle getRightCellStyle(HSSFWorkbook workbook) {
		return initStyle(workbook, HorizontalAlignment.RIGHT);
	}

	public CellStyle getRightCellStyle2(Workbook workbook) {
		return initStyle2(workbook, HorizontalAlignment.RIGHT);
	}

	/**
	 * 获取一个初始的样式
	 */
	public HSSFCellStyle initStyle(HSSFWorkbook workbook, HorizontalAlignment align) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(align);
		return style;
	}

	/**
	 * 获取一个初始的样式
	 */
	public CellStyle initStyle2(Workbook workbook, HorizontalAlignment align) {
		CellStyle style = workbook.createCellStyle();
		// 边框设置
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		// 字体居中
		style.setAlignment(align);
		return style;
	}
}