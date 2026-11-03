package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.credit.domain.entity.RatUration;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vo.*;
import com.bytter.credit.dto.*;
import com.bytter.credit.enums.RatMessageEnum;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信占用申请表
 * @date 2017-08-19 11:36:28
 */
@Api(tags = "授信占用申请表")
@RestController("v1RatUrationApplyController")
@RequestMapping("/v1-cms/cms/rat/raturationapply")
public class RatUrationApplyController extends BaseController {
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatBkrationDisService ratDisService;
    @Autowired
    private RatBkrationDisDetailService disDetailService;
    @Autowired
    private RatBkrationAddService ratAddService;
    @Autowired
    private RatItemCustomService ratItemCustomService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @Autowired
    private RatUrationService ratUrationService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;

    /**
     * 授信占用申请表列表
     *
     * @return 授信占用申请表列表数据
     */
    @ApiOperation("查询授信占用申请表列表")
    @LogMessage(content = "查询授信占用申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<RatUrationApplyListVO> list(RatBkrationVO rationVO, Integer start, Integer len) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        // 查询参数设置
        this.setSearchParams(input, rationVO);

        DataTablesOutput output;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            output = ratAddService.queryApplyRationAddData(input, rationVO.getCorpId(), RationConstant.CMS_RATION_APPLY_QUERY, getUserId());
        } else {
            output = new DataTablesOutput();
        }
        RatUrationApplyListVO result = new RatUrationApplyListVO();
        long resultCount = output.getRecordsTotal();
        int pageSize = input.getLength();
        int pageCount;
        if (resultCount % pageSize == 0) {
            // 整除则直接取整相除
            pageCount = (int) (resultCount / pageSize);
        } else {
            // 否则取整相除后加一
            pageCount = (int) ((resultCount / pageSize) + 1);
        }
        result.setRations(output.getData());
        result.setCurr(start);
        result.setPageCount(pageCount);
        result.setPageSize(pageSize);
        result.setResultCount(resultCount);
        result.setCorpId(rationVO.getCorpId());
        result.setContractNo(rationVO.getContractNo());
        result.setRatCode(rationVO.getRatCode());
        result.setBankName(rationVO.getBankName());
        result.setgBeginDate(rationVO.getgBeginDate() != null ? DateUtils.formatDate(rationVO.getgBeginDate()) : null);
        result.setlBeginDate(rationVO.getlBeginDate() != null ? DateUtils.formatDate(rationVO.getlBeginDate()) : null);
        result.setgEndDate(rationVO.getgEndDate() != null ? DateUtils.formatDate(rationVO.getgEndDate()) : null);
        result.setlEndDate(rationVO.getlEndDate() != null ? DateUtils.formatDate(rationVO.getlEndDate()) : null);
        return success(result);
    }

    /**
     * 银行额度系统占用导出
     *
     * @param request  请求
     * @param response 响应
     * @param rationVO 查询条件
     */
    @ApiOperation("银行额度系统占用导出")
    @LogMessage(content = "银行额度系统占用导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListRatUrationApply")
    public void exportListRatUrationApply(HttpServletRequest request, HttpServletResponse response, RatBkrationVO rationVO) throws Exception {
        // 导出的文件名
        String fileName = "银行额度系统占用表.xls";
        fileName = URLEncoder.encode(fileName, "UTF8");
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(rationVO.getExportType())) {
            // 导出当前页
            if (null == rationVO.getStart()) {
                rationVO.setStart(1);
            }
            if (null == rationVO.getPageSize()) {
                rationVO.setPageSize(10);
            }
            input.setStart((rationVO.getStart() - 1) * rationVO.getPageSize());
            input.setLength(rationVO.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }

        //查询参数设置
        setSearchParams(input, rationVO);
        DataTablesOutput output;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            output = ratAddService.queryApplyRationAddData(input, rationVO.getCorpId(), RationConstant.CMS_RATION_APPLY_QUERY, getUserId());
        } else {
            output = new DataTablesOutput();
        }
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行额度系统占用表", CommonConstant.NO, CommonConstant.YES, CommonConstant.NO, CommonConstant.NO);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    /**
     * 查询参数设置
     *
     * @param input    input输入参数
     * @param rationVO 对象
     */
    private void setSearchParams(DataTablesInput input, RatBkrationVO rationVO) {
        // 授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        // 授信合同号
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
        }
        // 授信编号
        String ratCode = rationVO.getRatCode();
        if (!StringUtils.isEmpty(ratCode)) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", ratCode);
        }
        // 银行信息
        String bankName = rationVO.getBankName();
        if (!StringUtils.isEmpty(bankName)) {
            input.addFilter(MatchTypeEnum.LIKE, "bankName", bankName);
        }
        // 授信开始时间段最小时间
        if (null != rationVO.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(rationVO.getgBeginDate()));
        }
        // 授信开始时间段最大时间
        if (null != rationVO.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(rationVO.getlBeginDate()));
        }
        // 授信结束时间段最小时间
        if (null != rationVO.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(rationVO.getgEndDate()));
        }
        // 授信结束时间段最大时间
        if (null != rationVO.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(rationVO.getlEndDate()));
        }
    }

    /**
     * 授信占用申请表 table列表
     *
     * @return
     */
    @ApiOperation("查询授信占用申请表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String ratCode, String corpId) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        tablesInput.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        tablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
        tablesInput.addColumn("createDate", false, true, null);
        tablesInput.addOrder("createDate", false);
        String ratType = RationConstant.RAT_TYPE_0;// 授信种类为银行授信
        if (!StringUtils.isEmpty(ratType)) {
            tablesInput.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        }
        return success(ratUrationApplyService.queryURationApplyData(tablesInput, RationConstant.CMS_RATION_APPLY_QUERY,
                getUserId()));
    }

    /**
     * 银行授信单据已占用列表
     *
     * @return
     */
    @ApiOperation("银行授信单据已占用列表数据")
    @LogMessage(content = "银行授信单据已占用列表跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addList")
    public AjaxJson<RatBkrationDisListDTO> addList(RatBkrationPageVO rationVO) {
        RatBkrationDisListDTO listDTO = ratDisService.geBkrationDisListDTOById(rationVO.getRatCode(),
                rationVO.getCorpId());
        return success(listDTO);
    }

    /**
     * 获取授信分配信息系统授信品种获取可占用的品种
     */
    @ApiOperation("获取可占用授信品种")
    @GetMapping("/getRationDisSysItem")
    public AjaxJson<List<RatSysItemDTO>> getRationDisSysItem(String disCode) {
        return success(disDetailService.getRationDisSysItem(disCode));
    }

    /**
     * 根据编号获取系统授信品种
     */
    @ApiOperation("根据编号获取系统授信品种")
    @GetMapping("/getRatSysItem")
    public AjaxJson<List<RatSysItemDTO>> getRatSysItem(String sysItemCode) {
        return success(ratItemCustomService.getRatSysItemByCode(sysItemCode));
    }

    /**
     * 获取授信分配信息系统授信品种获取可占用的品种
     */
    @ApiOperation("获取可占用授信品种")
    @GetMapping("/getUrationItem")
    public AjaxJson<List<RatItemDisVO>> getUrationItem(String disCode, String sysItemCode) {
        return success(ratDisService.getRationDisItemCode(disCode, sysItemCode));
    }

    /**
     * 授信占用申请表新增页面数据
     *
     * @return
     */
    @ApiOperation("授信占用申请表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatUrationApplyAddVO> add(RatUrationApply ratApply) {
        RatUrationApplyAddVO result = new RatUrationApplyAddVO();
        if (StringUtils.isNotEmpty(ratApply.getRatCode())) {
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(ratApply.getRatCode(),
                    ratApply.getCorpId());
            result.setRatBkrationPage(pageVO);
        }
        this.setPageConfig(result, ratApply);
        return success(result);
    }

    /**
     * 授信占用申请表编辑页面数据
     *
     * @return
     */
    @ApiOperation("授信占用申请表编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatUrationApplyEditVO> updatePage(RatUrationApply ratApply) {
        RatUrationApplyEditVO result = new RatUrationApplyEditVO();
        if (StringUtils.isNotEmpty(ratApply.getRatCode())) {
            RatUrationApplyListDTO applyDTO = ratUrationApplyService.editRatUrationInfo(ratApply.getId());
            RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                    applyDTO.getCorpId());//授信分配信息
            result.setRatBkrationPage(pageVO);
            result.setApplyDTO(applyDTO);

            RatBkration ratBkration = ratBkrationService.get(ratApply.getRatCode(), true);
            // 获取授信开始时间
            Date beginDate = ratBkration.getBegindate();
            result.setBeginDate(DateUtils.getDateStr(beginDate, "yyyy-MM-dd"));
            // 授信币种与业务币种不同，则需要通过汇率转换
            if (!applyDTO.getCurCode().equals(applyDTO.getInitCurCode())) {
                BigDecimal exchangeMoneyRate = rateAdjustService.getExchangeRateBy(applyDTO.getInitCurCode(), applyDTO.getCurCode(), beginDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//汇率类型为折算汇率
                result.setExchangeMoneyRate(exchangeMoneyRate);
            } else {
                result.setExchangeMoneyRate(BigDecimal.ONE);
            }

            // 业务币种与保证金币种不同，则需要通过汇率转换
            if (!applyDTO.getInitCurCode().equals(applyDTO.getBondCurId())) {
                BigDecimal exchangeBondMoneyRate = rateAdjustService.getExchangeRateBy(applyDTO.getInitCurCode(), applyDTO.getBondCurId(), DateUtils.getDateYMD(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);//汇率类型为折算汇率
                result.setExchangeBondMoneyRate(exchangeBondMoneyRate);
            } else {
                result.setExchangeBondMoneyRate(BigDecimal.ONE);
            }
        }
        this.setPageConfig(result, ratApply);
        return success(result);
    }

    /**
     * 授信占用申请表详情
     *
     * @return
     */
    @ApiOperation("查询授信占用申请表详情")
    @LogMessage(content = "查询授信占用申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<RatUrationApplyViewVO> view(String id) {
        RatUrationApplyListDTO applyDTO = ratUrationApplyService.editRatUrationInfo(id);
        RatBkrationCpAddListDTO pageVO = ratUrationApplyService.initRatUrationInfo(applyDTO.getOriRatCode(),
                applyDTO.getCorpId());// 授信分配信息
        RatUrationApplyViewVO result = new RatUrationApplyViewVO();
        result.setRatBkrationPage(pageVO);
        result.setApplyDTO(applyDTO);
        result.setViewFlag("1");
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        result.setIsOpenRat(ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                String guaWayNames = guaWayServie.getGuaWayNameByCodes(applyDTO.getGuaWay());
                result.setGuaWayNames(guaWayNames);
            }
        }
        return success(result);
    }

    private void setUrationApply(RatUrationApplyDTO applyDTO) {
        // 去掉无效数据
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        if (EmptyUtils.isEmptyList(applyDtls)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_ITEM_NULL);
        }
        List<RatUrationApplyListDTO> dtls = new ArrayList<RatUrationApplyListDTO>();
        for (RatUrationApplyListDTO listDTO : applyDtls) {
            if (StringUtils.isEmpty(listDTO.getItemCode())) {
                continue;
            }
            setUrationApplyList(listDTO);
            listDTO.setRatType(applyDTO.getRatType());
            listDTO.setRatCode(applyDTO.getRatCode());
            listDTO.setDisCode(applyDTO.getDisCode());
            listDTO.setCorpId(applyDTO.getCorpId());
            listDTO.setStatus(applyDTO.getStatus());
            listDTO.setCurCode(applyDTO.getCurCode());
            if (StringUtils.isNotEmpty(applyDTO.getGuaWay())) {
                listDTO.setGuaWay(applyDTO.getGuaWay());
            }
            dtls.add(listDTO);
        }
        applyDTO.setApplyDtls(dtls);
    }

    /**
     * 暂存授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @ApiOperation("暂存授信占用申请表")
    @LogMessage(content = "添加授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatUrationApplyDTO applyDTO) {
        applyDTO.setStatus(CommonConstant.STATUE_TEMP);
        setUrationApply(applyDTO);
        ratUrationApplyService.saveRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 功能介绍 : 提交授信占用申请。<br>
     * <b>日期：</b>2017年8月23日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @ApiOperation("提交授信占用申请表")
    @LogMessage(content = "提交授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody RatUrationApplyDTO applyDTO) {
        applyDTO.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        setUrationApply(applyDTO);
        ratUrationApplyService.saveRatUrationApply(applyDTO, getUserId());
        return success();
    }

    private void setUrationApplyList(RatUrationApplyListDTO applyDTO) {
        // 去掉无效数据
        List<RatUrationApplyDtlDTO> applyDtls = applyDTO.getApplyDtls();
        List<RatUrationApplyDtlDTO> dtls = new ArrayList<RatUrationApplyDtlDTO>();
        if (!EmptyUtils.isEmptyList(applyDtls)) {
            for (RatUrationApplyDtlDTO listDTO : applyDtls) {
                if (StringUtils.isEmpty(listDTO.getItemCode())) {
                    continue;
                }
                if (null == listDTO.getMaxMoney()) {
                    listDTO.setMaxMoney(BigDecimal.ZERO);
                }
                dtls.add(listDTO);
            }
            applyDTO.setApplyDtls(dtls);
        }
        if (null == applyDTO.getOpenMoney()) {
            applyDTO.setOpenMoney(applyDTO.getMoney());// 默认敞口额度=占用额度
        }
    }

    /**
     * 修改暂存授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @ApiOperation("修改暂存授信占用申请表")
    @LogMessage(content = "添加授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSave")
    public AjaxJson editSave(@RequestBody RatUrationApplyListDTO applyDTO) {
        applyDTO.setStatus(CommonConstant.STATUE_TEMP);
        setUrationApplyList(applyDTO);
        ratUrationApplyService.updateRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 修改提交授信占用申请表
     *
     * @param applyDTO
     * @return
     */
    @ApiOperation("修改提交授信占用申请表")
    @LogMessage(content = "修改提交授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/editSubmit")
    public AjaxJson editSubmit(@RequestBody RatUrationApplyListDTO applyDTO) {
        applyDTO.setStatus(CommonConstant.STATUE_WAIT_CHECK);
        setUrationApplyList(applyDTO);
        ratUrationApplyService.updateRatUrationApply(applyDTO, getUserId());
        return success();
    }

    /**
     * 释放授信占用申请
     *
     * @return
     */
    @ApiOperation("释放授信占用申请")
    @LogMessage(content = "释放授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/release")
    public AjaxJson release(@RequestBody RatUrationApplyListDTO applyDTO) {
        ratUrationApplyService.updateUrationRelease(applyDTO.getId(), getUserId());
        return success();
    }

    /**
     * 删除授信占用申请表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除授信占用申请表")
    @LogMessage(content = "删除授信占用申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        ratUrationApplyService.updateUrationDel(id, getUserId());
        return success();
    }

    /**
     * 根据授信币别获取可选的业务币别
     */
    @ApiOperation("根据授信币别获取可选的业务币别")
    @GetMapping("/getBillCurList")
    public AjaxJson<List<BtCurrencyDTO>> getBillCurList(String ratCurId) {
        List<BtCurrencyDTO> btCurrencyDTOS = new ArrayList<BtCurrencyDTO>();
        BtCurrencyDTO RMBBtCurrencyDTO = iBtCurrencyService.getRMBCurrency();
        if (ratCurId != null) {
            if (!ratCurId.equals(RMBBtCurrencyDTO.getId())) {// 当授信币种为外币时，业务币别只能为相应的外币
                BtCurrencyDTO btCurrencyDTO = iBtCurrencyService.getObjectById(ratCurId);
                btCurrencyDTOS.add(btCurrencyDTO);
            } else if (ratCurId.equals(RMBBtCurrencyDTO.getId())) {// 授信币种为人民币，业务币种为所有的币种
                btCurrencyDTOS = iBtCurrencyService.getBtCurrency();
            }
        }
        return success(btCurrencyDTOS);
    }

    /**
     * 根据业务币别获取可选的保证金币别
     */
    @ApiOperation("根据业务币别获取可选的保证金币别")
    @GetMapping("/getBondCurList")
    public AjaxJson<List<BtCurrencyDTO>> getBondCurList(String billCurId, String corpId) {
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBondCurList(billCurId, corpId);
        return success(btCurrencyDTOS);
    }

    /**
     * 根据根据业务金额、保证金金额以及是否启用授信敞口计算授信占用金额
     */
    @ApiOperation("计算授信占用金额")
    @PostMapping("/getRatUrationMoney")
    public AjaxJson<BigDecimal> getRatUrationMoney(@RequestBody RatUrationApplyListDTO applyDTO) {
        RatUrationDTO ratUrationDTO = new RatUrationDTO();
        BeanUtils.copyNotNullProperties(applyDTO, ratUrationDTO);
        RatUration ratUration = new RatUration();
        ratUration.setCurCode(ratUrationDTO.getCurCode());// 设置授信币种
        ratUration.setInitCurCode(ratUrationDTO.getInitCurCode());// 设置业务币种
        return success(ratUrationService.getRatUrationMoney(ratUrationDTO, ratUration).getMoney());
    }

    /**
     * 根据业务币别和业务金额计算折合人民币的金额
     */
    @ApiOperation("计算折合人民币金额")
    @GetMapping("/getExchangeMoney")
    public AjaxJson<BigDecimal> getExchangeMoney(String billCurId, String billMoney, String corpId) {
        return success(ratUrationService.getExchangeMoney(billCurId, billMoney, corpId));
    }

    /**
     * 获取所有担保方式
     *
     * @return
     */
    public String getAllGuaWayCode() {
        String guaWayCodes = "";
        List<GuaWayDTO> guaWayDTOS = guaWayServie.getAllGuaWay();
        if (guaWayDTOS != null || guaWayDTOS.size() > 0) {
            guaWayCodes = guaWayDTOS.stream().map(GuaWayDTO::getCode).collect(Collectors.joining(CommonConstant.COMMA_SPLIT));
        }
        return guaWayCodes;
    }

    /**
     * 设置新增/编辑页面公共配置数据
     */
    private void setPageConfig(RatUrationApplyBaseVO result, RatUrationApply ratApply) {
        // 获取授信敞口参数
        ConfigDTO ratConfig = ratBkrationService.getIsOpenRat();// 是否启用敞口
        result.setIsOpenRat(ratConfig.getValue());
        ConfigDTO guaConfig = ratBkrationService.getMountGuarantee();// 参数设置是否挂接担保
        result.setIsGuarantee(guaConfig.getValue());
        if (guaConfig.getValue().equals(RationConstant.RAT_COMMON_Y)) {
            RatBkration ratBkration = ratBkrationService.get(ratApply.getRatCode());
            result.setIsHangGuarantee(ratBkration.getIsHangGuarantee());
            if (ratBkration.getIsHangGuarantee() != null
                    && ratBkration.getIsHangGuarantee().equals(RationConstant.RAT_COMMON_Y)) {
                this.setGuaCotractMsg(ratApply, result);
            }
        }
    }

    /**
     * 设置担保合同相关信息
     */
    public void setGuaCotractMsg(RatUrationApply ratApply, RatUrationApplyBaseVO result) {
        String allGuaWayCode = this.getAllGuaWayCode();
        GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
        guaContractOccupDTO.setBusinessId(ratApply.getRatCode());
        GuaContractOccupDTO guaContractDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
        result.setGuaWay(guaContractDTO.getGuaWay());
        result.setGuaWayName(guaContractDTO.getGuaWayName());
        result.setAllGuaWayCode(allGuaWayCode);
    }

    @ApiOperation("获取折算汇率")
    @GetMapping("/getNearCurRat")
    public AjaxJson<BigDecimal> getNearCurRat(String curId, String convertCurId, Date date) {
        // TODO 调用货币汇率调整接口，获取折合本位币汇率
        return success(rateAdjustService.getExchangeRateBy(curId, convertCurId, date,
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT));// 汇率类型为折算汇率
    }

    /**
     * 更新授信分配信息占用额度
     *
     * @return
     */
    @ApiOperation("更新授信占用额度")
    @LogMessage(content = "更新授信占用额度", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateRatDis")
    public AjaxJson updateRatDis(@RequestBody RatUrationUpdateRatDisDTO dto) {
        ratUrationApplyService.updateRatUrationApply(dto.getDisId(), dto.getRatCode(), getUserId());
        return success();
    }
}
