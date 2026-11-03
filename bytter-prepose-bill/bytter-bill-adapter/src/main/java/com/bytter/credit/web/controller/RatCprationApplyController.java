package com.bytter.credit.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatUrationApply;
import com.bytter.credit.domain.service.RatCprationApplyService;
import com.bytter.credit.domain.service.RatCprationDisService;
import com.bytter.credit.domain.service.RatCprationLogService;
import com.bytter.credit.domain.service.RatUrationApplyService;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.dto.RatSysItemDTO;
import com.bytter.credit.dto.RatUrationApplyDTO;
import com.bytter.credit.dto.RatUrationApplyDtlDTO;
import com.bytter.credit.dto.RatUrationApplyListDTO;
import com.bytter.credit.enums.RatMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationApplyController
 * @Description:
 * @date 2018年11月21日 上午9:17:43
 */
@Controller
@RequestMapping("/cms/rat/cprationapply")
public class RatCprationApplyController extends BaseController {
    @Autowired
    private RatCprationApplyService ratCprationApplyService;
    @Autowired
    private RatCprationDisService ratCprationDisService;
    @Autowired
    private RatUrationApplyService ratUrationApplyService;
    @Autowired
    private RatCprationLogService ratLogService;
    /**
     * 查询待占用列表数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/list")
    public String list(RatBkrationVO rationVO, Model model, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        String contractNo = rationVO.getContractNo();
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        Date ratDate = rationVO.getBeginDate();
        if (null != rationVO.getBeginDate()) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratDate), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratDate), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratDate));
        }
        DataTablesOutput output = null;
        if (!StringUtils.isEmpty(rationVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "disCorp", rationVO.getCorpId());// 单位ID
            output = ratCprationDisService.queryCpRationDisData(input, rationVO.getCorpId(),
                    RationConstant.CMS_CPRATION_DIS_QUERY, getUserId());
            model.addAttribute("corpId", rationVO.getCorpId());
        } else {
            output = new DataTablesOutput();
        }
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
        model.addAttribute("model", result);
        return "cms/rat/cpration/listRatCprationApply";
    }
    /**
     * 查看分配信息
     */
    @GetMapping("/viewApply")
    public String viewApply(RatUrationApply ratApply, Model model) {
        if (StringUtils.isNotEmpty(ratApply.getDisCode())) {
            RatBkrationDisVO ratCprationDis = ratCprationApplyService.initRatCprationApplyInfo(ratApply);
            model.addAttribute("ratCprationPage", ratCprationDis);
        }
        model.addAttribute("viewFlag", "1");
        return "cms/rat/cpration/viewRatCprationApply";
    }
    /**
     * 新增内部授信额度占用
     */
    @GetMapping("/add")
    public String add(RatUrationApply ratApply, Model model) {
        if (StringUtils.isNotEmpty(ratApply.getDisCode())) {
            RatBkrationDisVO ratCprationDis = ratCprationApplyService.initRatCprationApplyInfo(ratApply);
            model.addAttribute("ratCprationPage", ratCprationDis);
        }
        return "cms/rat/cpration/addRatCprationApply";
    }
    /**
     * 获取内部授信分配信息系统授信品种可占用的品种
     *
     */
    @PostMapping("/getCpRationDisSysItem")
    @ResponseBody
    public List<RatSysItemDTO> getRationDisSysItem(String disCode) {
        return ratCprationApplyService.getCpRationDisSysItem(disCode);
    }
    /**
     * 录入提交或暂存授信占用信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(RatUrationApplyDTO applyDTO, String saveType) {
        setCprationApply(applyDTO);// 设置授信占用明细
        ratCprationApplyService.saveRatCprationApply(applyDTO, this.getCurrentUser(), saveType);
        return success();
    }
    /**
     * 编辑提交或暂存授信占用信息
     */
    @PostMapping("/editTempOrSubmit")
    @ResponseBody
    public AjaxJson editTempOrSubmit(RatUrationApplyListDTO applyDTO, String saveType) {
        ratCprationApplyService.updateRatCprationApply(applyDTO, this.getCurrentUser(), saveType);
        return success();
    }
    /**
     * 内部授信占用列表
     */
    @GetMapping("/applyList")
    public String applyList(String disCode, Model model, Integer start) {
        RatBkrationDisVO listDTO = ratCprationDisService.queryRatCpRationDisInfo(disCode);
        model.addAttribute("disVO", listDTO);
        return "cms/rat/cpration/listRatCprationApplyed";
    }
    /**
     * 授信占用dataTable
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String ratCode, String corpId) {
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "ratCode", ratCode);
        dataTablesInput.addFilter(MatchTypeEnum.EQ, "corpId", corpId);
        dataTablesInput.addColumn("createDate", false, true, null);
        dataTablesInput.addOrder("createDate", false);
        String ratType = RationConstant.RAT_TYPE_1;// 授信种类为企业授信
        if (!StringUtils.isEmpty(ratType)) {
            dataTablesInput.addFilter(MatchTypeEnum.LIKE, "ratType", ratType);
        }
        return ratUrationApplyService.queryURationApplyData(dataTablesInput, RationConstant.CMS_CPRATION_APPLY_QUERY,
                getUserId());
    }
    /**
     * 编辑内部授信额度占用
     * 
     * @param
     * @return
     */
    @GetMapping("/update")
    public String update(RatUrationApply ratApply, Model model) {
        if (StringUtils.isNotEmpty(ratApply.getId())) {
            RatUrationApplyListDTO applyDTO = ratCprationApplyService.editRatUrationInfo(ratApply);
            RatBkrationDisVO ratCprationDis = ratCprationApplyService.initRatCprationApplyInfo(ratApply);
            model.addAttribute("ratCprationPage", ratCprationDis);
            model.addAttribute("applyDTO", applyDTO);
        }
        return "cms/rat/cpration/editRatCprationApply";
    }
    /**
     * 改变授信占用状态为删除
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ratUrationApplyService.updateUrationDel(id, getUserId());
        return success();
    }
    /**
     * 查看占用详情
     */
    @GetMapping("/view")
    public String view(RatUrationApply ratApply, Model model) {
        RatUrationApplyListDTO applyDTO = ratCprationApplyService.editRatUrationInfo(ratApply);
        RatBkrationDisVO ratCprationDis = ratCprationDisService.queryRatCpRationDisInfo(applyDTO.getDisCode());
        model.addAttribute("ratCprationPage", ratCprationDis);
        model.addAttribute("applyDTO", applyDTO);
        model.addAttribute("viewFlag", "1");
        return "cms/rat/cpration/viewRatCprationApplyed";
    }
    /**
     * 释放占用信息
     */
    @PostMapping("/release")
    @ResponseBody
    public AjaxJson release(RatUrationApplyListDTO applyDTO, HttpServletRequest request) {
        ratUrationApplyService.updateUrationRelease(applyDTO.getId(), getUserId());
        String msg = RationConstant.MSGA_RELEASE_RAT;// 操作信息：释放额度
        Integer messageType = RationConstant.MESSAGE_TYPE_RELEASE;// 操作类型
        // 记录到日志表
        ratLogService.saveRatProcessMessge(applyDTO.getId(), applyDTO.getMoney(), messageType, msg,
                this.getCurrentUser());
        return success();
    }
    /**
     * 内部授信占用审批页面
     */
    @GetMapping("/listApprove")
    public String listSendApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/cpration/listRatCprationApplyApprove";
    }
    /**
     * 审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput input) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return ratCprationApplyService.getDataApplyApproveTables(input);
    }
    /**
     * 授信占用审批 - 详情页面
     */
    @GetMapping("/viewApplyApprove")
    public String viewApplyApprove(String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(id)) {
            RatUrationApply ratApply = ratCprationApplyService.get(id);
            RatUrationApplyListDTO applyDTO = ratCprationApplyService.editRatUrationInfo(ratApply);
            RatBkrationDisVO ratCprationDis = ratCprationDisService.queryRatCpRationDisInfo(applyDTO.getDisCode());
            model.addAttribute("ratCprationPage", ratCprationDis);
            model.addAttribute("applyDTO", applyDTO);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/viewRatCpUrationApplyApprove";
    }
    private void setCprationApply(RatUrationApplyDTO applyDTO) {
        List<RatUrationApplyListDTO> applyDtls = applyDTO.getApplyDtls();
        if (EmptyUtils.isEmptyList(applyDtls)) {
            throw new ParameterException(RatMessageEnum.CMS_RAT_ADD_ITEM_NULL);
        }
        List<RatUrationApplyListDTO> dtls = new ArrayList<RatUrationApplyListDTO>();
        for (RatUrationApplyListDTO listDTO : applyDtls) {
            if (StringUtils.isEmpty(listDTO.getType())) {
                continue;
            }
            setUrationApplyList(listDTO);
            listDTO.setRatType(applyDTO.getRatType());
            listDTO.setRatCode(applyDTO.getRatCode());
            listDTO.setDisCode(applyDTO.getDisCode());
            listDTO.setCurCode(applyDTO.getCurCode());
            listDTO.setCorpId(applyDTO.getCorpId());
            listDTO.setStatus(applyDTO.getStatus());
            dtls.add(listDTO);
        }
        applyDTO.setApplyDtls(dtls);
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
    }
    /**
     * 授信占用手机审批 - 详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            RatUrationApply ratApply = ratCprationApplyService.get(businessId);
            RatUrationApplyListDTO applyDTO = ratCprationApplyService.editRatUrationInfo(ratApply);
            RatBkrationDisVO ratCprationDis = ratCprationDisService.queryRatCpRationDisInfo(applyDTO.getDisCode());
            model.addAttribute("ratCprationPage", ratCprationDis);
            model.addAttribute("applyDTO", applyDTO);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/mobileRatCprationApply";
    }
}
