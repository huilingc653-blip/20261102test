package com.bytter.credit.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.entity.RatCreditType;
import com.bytter.credit.domain.service.*;
import com.bytter.credit.domain.vo.RatBkrationDisQueryVO;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.domain.vo.RatCreditTypeVO;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 授信分配信息表
 * @date 2017-07-31 16:07:30
 */
@Controller
@RequestMapping("/cms/rat/ratbkrationdis")
public class RatBkrationDisController extends BaseController {
    @Autowired
    private RatBkrationDisService ratBkrationDisService;
    @Autowired
    private RatBkrationService ratBkrationService;
    @Autowired
    private RatCreditTypeService creditTypeService;
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatBkrationAdjustService bkrationAdjustService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 授信分配信息表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询授信分配信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String list(Model model, Integer start, Integer len, RatBkrationDisQueryVO queryVO) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = 1;
        }
        if (null != len) {
            input.setLength(len);
        }
        input.setStart((start - 1) * input.getLength());
        // 查询参数设置
        this.setSearchParams(model, input, queryVO);
        List<String> corpIdArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(queryVO.getCorpIds())) {
            corpIdArr = Arrays.asList(queryVO.getCorpIds().split(","));
        }

        DataTablesOutput output = ratBkrationDisService.listDisOutputData(input, RationConstant.CMS_RATION_DIS_QUERY, getUserId(), corpIdArr);

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
        result.put("curr", start);
        result.put("pageCount", pageCount);
        result.put("pageSize", pageSize);
        result.put("resultCount", resultCount);
        model.addAttribute("model", result);
        return "cms/rat/bkration/listRatBkrationDis";
    }

    /**
     * 银行授信额度分配导出
     *
     * @param request  request
     * @param response response
     * @param vo       查询条件
     * @param model    model
     */
    @LogMessage(content = "银行授信额度分配导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/exportListBkrationDis")
    public void exportListRatBkration(HttpServletRequest request, HttpServletResponse response, RatBkrationDisQueryVO vo, Model model) throws Exception {
        // 导出的文件名
        String fileName = "银行授信额度分配表.xls";
        //判断浏览器类型
        if (ServletUtils.isIE(request)) {
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        DataTablesInput input = new DataTablesInput();
        if (CommonConstant.NO.equals(vo.getExportType())) {
            // 导出当前页
            if (null == vo.getStart()) {
                vo.setStart(1);
            }
            if (null == vo.getPageSize()) {
                vo.setPageSize(10);
            }
            input.setStart((vo.getStart() - 1) * vo.getPageSize());
            input.setLength(vo.getPageSize());
        } else {
            input.setStart(1);
            input.setLength(99999999);
        }
        //查询参数设置
        this.setSearchParams(model, input, vo);
        List<String> corpIdArr = Lists.newArrayList();
        if (StringUtils.isNotBlank(vo.getCorpIds())) {
            corpIdArr = Arrays.asList(vo.getCorpIds().split(","));
        }
        DataTablesOutput output = ratBkrationDisService.listDisOutputData(input, ResourceConstant.CMS_RATION_DIS_QUERY, getUserId(), corpIdArr);
        //导出数据
        HSSFWorkbook workbook = ratBkrationService.exportListRatBkration(output, "银行授信额度分配表", CommonConstant.NO, CommonConstant.NO, CommonConstant.NO, CommonConstant.NO);
        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    private void setSearchParams(Model model, DataTablesInput input, RatBkrationDisQueryVO vo) {
        //授信种类为银行授信
        String ratType = RationConstant.RAT_TYPE_0;
        input.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        model.addAttribute("ratType", ratType);
        //授信单位
        if (!StringUtils.isEmpty(vo.getCorpIds())) {
            model.addAttribute("corpId", vo.getCorpIds());
        }
        //银行信息
        if (!StringUtils.isEmpty(vo.getBankCodes())) {
            String bankCode = vo.getBankCodes().substring(0, vo.getBankCodes().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "bankCode", bankCode);
            model.addAttribute("bankCode", bankCode);
        }
        //授信品种
        if (!StringUtils.isEmpty(vo.getItemCodes())) {
            String itemCode = vo.getItemCodes().substring(0, vo.getItemCodes().length() - 1);
            input.addFilter(MatchTypeEnum.IN, "item_code", itemCode);
            model.addAttribute("itemCode", itemCode);//itemCode
        }
        //授信编号
        if (!StringUtils.isEmpty(vo.getRatCode())) {
            input.addFilter(MatchTypeEnum.LIKE, "ratCode", vo.getRatCode());
            model.addAttribute("ratCode", vo.getRatCode());
        }
        //合同号
        if (!StringUtils.isEmpty(vo.getContractNo())) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", vo.getContractNo());
            model.addAttribute("contractNo", vo.getContractNo());
        }
        //授信开始时间段最小时间
        if (null != vo.getgBeginDate()) {
            input.getFormdata().put("ged_beginDate", DateUtils.formatDate(vo.getgBeginDate()));
            model.addAttribute("gBeginDate", DateUtils.formatDate(vo.getgBeginDate()));
        }
        // 授信开始时间段最大时间
        if (null != vo.getlBeginDate()) {
            input.getFormdata().put("led_beginDate", DateUtils.formatDate(vo.getlBeginDate()));
            model.addAttribute("lBeginDate", DateUtils.formatDate(vo.getlBeginDate()));
        }
        //授信结束时间段最小时间
        if (null != vo.getgEndDate()) {
            input.getFormdata().put("ged_endDate", DateUtils.formatDate(vo.getgEndDate()));
            model.addAttribute("gEndDate", DateUtils.formatDate(vo.getgEndDate()));
        }
        //授信结束时间段最大时间
        if (null != vo.getlEndDate()) {
            input.getFormdata().put("led_endDate", DateUtils.formatDate(vo.getlEndDate()));
            model.addAttribute("lEndDate", DateUtils.formatDate(vo.getlEndDate()));
        }
        //集团授信,自主授信已自动分配
        input.addFilter(MatchTypeEnum.EQ, "ratMode", RationConstant.RATMODE_0);
        //录入数据已审批通过，可以进行分配
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);
        input.addOrder("ratCode", false);
    }

    /**
     * 授信分配信息表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return ratBkrationDisService.getDataTables(dataTablesInput);
    }

    /**
     * 授信分配信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(String ratCode, String disWay, Model model) {
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationPageVO ratBkration = ratBkrationDisService.getRatRationDisInfo(ratCode, disWay);
            model.addAttribute("ratBkrationPage", ratBkration);
            // 获取待分配额度和待分配敞口额度
        }
        // 授信额度类型
        List<RatCreditTypeVO> creditTypes = creditTypeService.getAllCreditType();
        if (creditTypes.size() > 0) {
            model.addAttribute("creditType", creditTypes.get(0).getId());
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "addRatBkrationDis";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "addRatBkrationDisOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信分配信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(RatBkrationDis rationDis, Model model) {
        if (StringUtils.isNotEmpty(rationDis.getId())) {
            RatBkrationPageVO ratBkration = ratBkrationDisService.editRatRationDisInfo(rationDis.getId());
            model.addAttribute("ratBkrationPage", ratBkration);
        }
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "updateRatBkrationDis";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "updateRatBkrationDisOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信分配信息列表查询
     *
     * @return
     */
    @LogMessage(content = "授信分配信息列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/disList")
    public String disList(RatBkrationDisVO disVO, Model model, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(disVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", disVO.getCorpId());// 单位
        }
        if (!StringUtils.isEmpty(disVO.getDistributeWay())) {
            input.addFilter(MatchTypeEnum.EQ, "distributeWay", disVO.getDistributeWay());// 分配方式
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", disVO.getRatCode());// 授信录入主键编号
        DataTablesOutput output = ratBkrationDisService.queryRationDisData(input, RationConstant.CMS_RATION_DIS_QUERY,
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
        model.addAttribute("disVO", disVO);
        model.addAttribute("model", result);
        return "cms/rat/bkration/listRatBkrationDised";
    }

    /**
     * 授信分配信息表详情页（旧）
     *
     * @return
     */
    @LogMessage(content = "查询授信分配信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(id);
            CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
            dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
            if (StringUtils.isNotEmpty(dto.getType())) {
                RatCreditType creditType = creditTypeService.get(dto.getType());
                dto.setCreditTypeName(creditType.getName());
            }
            model.addAttribute("ratBkrationPage", dto);
        }
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationDis";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDisOpen";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    /**
     * 授信分配信息表详情页
     *
     * @return
     */
    @LogMessage(content = "查询授信分配信息表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDis/{id}")
    public String viewDis(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(id);
            CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
            dto.setDisCorpName(corpDTO.getName());// 二次授信单位名称
            if (null != dto.getType()) {
                RatCreditType creditType = creditTypeService.get(dto.getType());
                dto.setCreditTypeName(creditType.getName());
            }
            model.addAttribute("ratBkrationPage", dto);
        }
        model.addAttribute("viewFlag", "1");
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationDisDtl";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDisOpenDtl";// 敞口跳转
        }
        return "cms/rat/bkration/" + page;
    }

    private void setRatBkrationDisDtls(RatBkrationDisListDTO ratBkrationDis) {
        List<RatBkrationDisDtlDTO> dtls = new ArrayList<RatBkrationDisDtlDTO>();
        List<RatBkrationDisDtlDTO> rationDtls = ratBkrationDis.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkrationDisDtlDTO>();
        }
        boolean compFlag = false;
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(dtlDTO.getMoney());
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        dtls.add(dtlDTO);
                    }
                } else {
                    if (null != dtlDTO.getMoney() && dtlDTO.getMoney().compareTo(BigDecimal.ZERO) > 0) {
                        compFlag = true;// 第一个综合项判断，表示已经录入过综合项，后续都全部录入
                    }
                    if (compFlag) {
                        dtls.add(dtlDTO);
                    }
                }
            }
        }
        // 按照单品种和综合品种排序
        Collections.sort(dtls, new Comparator<RatBkrationDisDtlDTO>() {
            public int compare(RatBkrationDisDtlDTO o1, RatBkrationDisDtlDTO o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        // 重设itemorder
        Integer index = 1;
        for (RatBkrationDisDtlDTO dtlDTO : dtls) {
            dtlDTO.setItemOrder(index);
            index++;
            if (RationConstant.RAT_COMFLAG_1.equals(dtlDTO.getItemFlag())) {
                // 所有的综合授信品种都设置同一个授信金额
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
                if (null == dtlDTO.getOpenMoney()) {
                    dtlDTO.setOpenMoney(BigDecimal.ZERO);
                }
            }
            if (null == dtlDTO.getOpenMoney()) {
                dtlDTO.setOpenMoney(dtlDTO.getMoney());
            }
        }
        ratBkrationDis.setRationDtls(dtls);
        if (null == ratBkrationDis.getOpenMoneysum()) {
            ratBkrationDis.setOpenMoneysum(ratBkrationDis.getMoneysum());
        }
    }

    /**
     * 暂存授信分配信息表
     *
     * @param ratBkrationDis
     * @return
     */
    @LogMessage(content = "暂存授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationDisService.saveRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    /**
     * 提交授信分配信息表
     *
     * @param ratBkrationDis
     * @return
     */
    @LogMessage(content = "提交授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationDisService.saveRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    /**
     * 修改暂存授信分配信息表
     *
     * @param ratBkrationDis
     * @return
     */
    @LogMessage(content = "修改暂存授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/editSave")
    @ResponseBody
    public AjaxJson editSave(RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_TEMP);
        ratBkrationDisService.editRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    /**
     * 修改提交授信分配信息表
     *
     * @param ratBkrationDis
     * @return
     */
    @LogMessage(content = "修改提交授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/editSubmit")
    @ResponseBody
    public AjaxJson editSubmit(RatBkrationDisListDTO ratBkrationDis) {
        setRatBkrationDisDtls(ratBkrationDis);
        ratBkrationDis.setStatus(RationConstant.STATUS_WAIT_CHECK);
        ratBkrationDisService.editRatBkrationDis(ratBkrationDis, getUserId());
        return success();
    }

    /**
     * 修改授信分配信息表
     *
     * @param ratBkrationDis
     * @return
     */
    //
    @LogMessage(content = "修改授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(RatBkrationDis ratBkrationDis) {
        RatBkrationDis t = ratBkrationDisService.get(ratBkrationDis.getId());
        BeanUtils.copyNotNullProperties(ratBkrationDis, t);
        ratBkrationDisService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除授信分配信息表
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDis ratBkrationDis = ratBkrationDisService.get(id);
        ratBkrationDis.setStatus(RationConstant.STATUS_DELETE);
        ratBkrationDisService.updateEntity(ratBkrationDis);
        return success();
    }

    /**
     * 批量删除授信分配信息表
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除授信分配信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            RatBkrationDis ratBkrationDis = ratBkrationDisService.get(id);
            ratBkrationDisService.deleteEntity(ratBkrationDis);
        }
        return success();
    }

    /**
     * 功能介绍 : 根据分配类型获取单位的直接下属单位。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/listNetCorp")
    @ResponseBody
    public List<CorpDTO> listMultNetCorp(String netId, String disWay) {
        return listNetCorp(netId, disWay);
    }

    /**
     * 功能介绍 : 根据分配类型获取单位的直接下属单位。<br>
     * <b>日期：</b>2017年8月1日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @GetMapping("/listNetCorp")
    @ResponseBody
    public List<CorpDTO> listNetCorp(String netId, String disWay) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        CorpDTO netCorp = corpService.get(netId);// 当前资金中心的单位
        if (RationConstant.DIS_TYPE_OWN.equals(disWay)) {
            netCorp.setName("(" + netCorp.getCode() + ")" + netCorp.getName());
        }
        netCorp.setParentId(null);
        corpDTOs.add(netCorp);
        if (RationConstant.DIS_TYPE_OWN.equals(disWay)) {
            return corpDTOs;
        }
        // 根据二次授信对象控制模式确认单位列表
        ConfigDTO config = ratBkrationDisService.getRatctrModel();
        if (RationConstant.RATCTRMODEL_RESOURCE.equals(config.getValue())) {
            corpDTOs = corpService.listUserCorp(RationConstant.CMS_RATION_DIS_APPLY, getUserId());
        } else {
            corpDTOs.addAll(getChildCorp(netId, null));
        }
        // 组装树，并排序
        return TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                if (o1.getSort().compareTo(o2.getSort()) > 0) {
                    return 1;
                } else if (o1.getSort().compareTo(o2.getSort()) < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 功能介绍 : 获取父单位ID的所有子单位。<br>
     * <b>日期：</b>2017年8月2日<br>
     *
     * @version 1.0
     * @author liyh
     */
    private List<CorpDTO> getChildCorp(String parentId, Map<String, List<CorpDTO>> corpMap) {
        List<CorpDTO> result = new ArrayList<CorpDTO>();
        List<CorpDTO> corpDTOs = corpService.listUnderCorpByParentCorpId(parentId);
        if (EmptyUtils.isEmptyList(corpDTOs)) {
            return result;
        }
        // 递归获取子单位的下级成员单位
        for (CorpDTO corpDTO : corpDTOs) {
            List<CorpDTO> corps = getChildCorp(corpDTO.getId(), corpMap);
            result.add(corpDTO);
            result.addAll(corps);
        }
        return result;
    }

    /**
     * 授信录入审批 - 列表页面跳转
     */
    @LogMessage(content = "查询授信录入发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/bkration/approve/listRatBkrationDisApprove";
    }

    /**
     * 授信录入审批 - 数据
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return ratBkrationDisService.listApproveOutputData(input);
    }

    /**
     * 授信录入审批 - 详情页面
     */
    @GetMapping("/viewBkrationDisApprove")
    public String viewBkrationDisApprove(String businessId, String taskId, Model model) {
        RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(businessId);
        CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
        dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        if (null != dto.getType()) {
            RatCreditType creditType = creditTypeService.get(dto.getType());
            if (null != creditType) {
                dto.setCreditTypeName(creditType.getName());
            }
        }
        model.addAttribute("ratBkrationPage", dto);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信参数
        ConfigDTO config = ratBkrationService.getStartOpenRat();// 敞口
        String page = "viewRatBkrationDis";
        if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
            page = "viewRatBkrationDisOpen";// 敞口跳转
        }
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/viewRatBkrationApprove";
    }

    /**
     * 授信分配手机审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileBkrationDisApprove(String businessId, String taskId, Model model) {
        RatBkrationCpAddListDTO dto = ratBkrationDisService.viewRatRationDisInfo(businessId);
        CorpDTO corpDTO = corpService.get(dto.getDisCorpId());
        dto.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        if (null != dto.getType()) {
            RatCreditType creditType = creditTypeService.get(dto.getType());
            if (null != creditType) {
                dto.setCreditTypeName(creditType.getName());
            }
        }
        model.addAttribute("ratBkrationPage", dto);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 获取授信参数
        // ConfigDTO config = ratBkrationService.getStartOpenRat();//敞口
        String page = "mobileRatBkrationDis";
        // if (RationConstant.OPEN_SIGN_1.equals(config.getValue())) {
        // page = "mobileRatBkrationDisOpen";//敞口跳转
        // }
        model.addAttribute("viewPage", page);
        return "cms/rat/bkration/approve/" + page;
    }

    /**
     * 总额共享方式已分配的单位查看
     */
    @LogMessage(content = "总额共享方式已分配的单位查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/distributeWayView/{ratCode}/{ratCorpId}")
    public String distributeWayView(@PathVariable("ratCode") String ratCode, @PathVariable("ratCorpId") String ratCorpId, Model model) {
        List<CorpDTO> corpList = ratBkrationDisService.distributeWayView(ratCode, ratCorpId);
        model.addAttribute("corpList", corpList);
        return "cms/rat/bkration/viewRatDistributeWay";
    }


}
