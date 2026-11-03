package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationDis;
import com.bytter.credit.domain.service.RatCprationDisService;
import com.bytter.credit.domain.vo.RatBkrationDisVO;
import com.bytter.credit.domain.vo.RatBkrationPageVO;
import com.bytter.credit.dto.RatBkrationDisDtlDTO;
import com.bytter.credit.dto.RatBkrationDisListDTO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCprationDisController
 * @Description:
 * @date 2018年11月19日 下午13:37:40
 */
@Controller
@RequestMapping("/cms/rat/ratcprationdis")
public class RatCprationDisController extends BaseController {
    @Autowired
    private RatCprationDisService ratCprationDisService;
    @DubboReference(check = false)
    private ICorpService corpService;
    /**
     * 内部授信分配列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询内部授信额度录入列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/list")
    public String getRatCprationDisList(Model model, String contractNo, Date ratDate, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(contractNo)) {
            input.addFilter(MatchTypeEnum.LIKE, "contractNo", contractNo);
            model.addAttribute("contractNo", contractNo);
        }
        if (null != ratDate) {
            input.addFilter(MatchTypeEnum.LE, "sDate", DateUtils.formatDate(ratDate), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "eDate", DateUtils.formatDate(ratDate), Date.class);// 小于等于授信结束日期
            model.addAttribute("ratDate", DateUtils.formatDate(ratDate));
        }
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 录入数据已审批通过，可以进行分配
        input.addOrder("ratCode", false);
        DataTablesOutput output = ratCprationDisService.listDisOutputData(input, getUserId());
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
        return "cms/rat/cpration/listRatCprationDis";
    }
    /**
     * 新增授信分配
     */
    @LogMessage(content = "新增内部授信额度录入", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/add")
    public String addRatDis(String ratCode, Model model) {
        if (StringUtils.isNotEmpty(ratCode)) {
            RatBkrationPageVO ratCpration = ratCprationDisService.getRatCpRationDisInfo(ratCode);
            model.addAttribute("ratCprationPage", ratCpration);
        }
        return "cms/rat/cpration/addRatCprationDis";
    }
    /**
     * 获取二次授信单位
     */
    @PostMapping("/listNetCorp")
    @ResponseBody
    public List<CorpDTO> listMultNetCorp(String netId) {
        return listNetCorp(netId);
    }
    @GetMapping("/listNetCorp")
    @ResponseBody
    public List<CorpDTO> listNetCorp(String netId) {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        corpDTOs = corpService.getCpRationGroupCorpNet(RationConstant.CMS_CPRATION_DIS_APPLY, netId, this.getUserId());
        return TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }
    /**
     * 保存分配信息
     * 
     * @param
     * @return
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrsubmit(RatBkrationDisListDTO ratCprationDis, String saveType) {
        setRatCprationDisDtls(ratCprationDis);
        ratCprationDisService.tempOrSubmit(ratCprationDis, this.getCurrentUser(), saveType);
        return success();
    }
    private void setRatCprationDisDtls(RatBkrationDisListDTO ratCprationDis) {
        List<RatBkrationDisDtlDTO> dtls = new ArrayList<RatBkrationDisDtlDTO>();
        List<RatBkrationDisDtlDTO> rationDtls = ratCprationDis.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkrationDisDtlDTO>();
        }
        for (RatBkrationDisDtlDTO dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) >= 0) {
                        dtls.add(dtlDTO);
                    }
                }
                if (null == dtlDTO.getMoney()) {
                    dtlDTO.setMoney(BigDecimal.ZERO);
                }
            }
        }
        // 按照单品种排序
        Collections.sort(dtls, new Comparator<RatBkrationDisDtlDTO>() {
            public int compare(RatBkrationDisDtlDTO o1, RatBkrationDisDtlDTO o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        ratCprationDis.setRationDtls(dtls);
    }
    /**
     * 分配列表
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/disList")
    public String disList(RatBkrationDisVO disVO, Model model, Integer start) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(disVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", disVO.getCorpId());// 单位
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", disVO.getRatCode());// 授信录入主键编号
        DataTablesOutput output = ratCprationDisService.queryRationDisData(input, getUserId());
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
        return "cms/rat/cpration/listRatCprationDised";
    }
    /**
     * 内部授信分配编辑页面
     */
    @GetMapping("/update")
    public String update(RatBkrationDis rationDis, Model model) {
        if (StringUtils.isNotEmpty(rationDis.getId())) {
            RatBkrationPageVO ratCpration = ratCprationDisService.queryRatRationDisInfo(rationDis.getId());
            model.addAttribute("ratCprationPage", ratCpration);
        }
        return "cms/rat/cpration/editRatCprationDis";
    }
    /**
     * 删除授信分配表
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDis ratCprationDis = ratCprationDisService.get(id);
        ratCprationDis.setStatus(RationConstant.STATUS_DELETE);
        ratCprationDisService.updateEntity(ratCprationDis);
        return success();
    }
    /**
     * 查看内部授信分配信息
     */
    @GetMapping("/viewDis/{id}")
    public String viewDis(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationPageVO ratCpration = ratCprationDisService.queryRatRationDisInfo(id);
            CorpDTO corpDTO = corpService.get(ratCpration.getDisCorpId());
            ratCpration.setDisCorpName(corpDTO.getName());// 二次授信单位名称
            model.addAttribute("ratCprationPage", ratCpration);
        }
        model.addAttribute("viewFlag", "1");
        return "cms/rat/cpration/viewRatCprationDisDtl";
    }
    /**
     * 内部授信分配审批 - 列表页面跳转
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/rat/cpration/listRatCprationDisApprove";
    }
    /**
     * 内部授信分配审批 - 数据
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
        return ratCprationDisService.listApproveOutputData(input);
    }
    /**
     * 内部授信分配审批详情页面
     */
    @GetMapping("/viewCprationDisApprove")
    public String viewBkrationDisApprove(String id, String taskId, Model model) {
        RatBkrationPageVO ratCpration = ratCprationDisService.queryRatRationDisInfo(id);
        CorpDTO corpDTO = corpService.get(ratCpration.getDisCorpId());
        ratCpration.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/viewCprationDisApprove";
    }
    /**
     * 内部授信分配手机审批详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        RatBkrationPageVO ratCpration = ratCprationDisService.queryRatRationDisInfo(businessId);
        CorpDTO corpDTO = corpService.get(ratCpration.getDisCorpId());
        ratCpration.setDisCorpName("(" + corpDTO.getCode() + ")" + corpDTO.getName());// 二次授信单位名称
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/mobileRatCprationDis";
    }
}
