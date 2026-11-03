package com.bytter.credit.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.entity.RatBkrationAdd;
import com.bytter.credit.domain.service.RatCprationAddService;
import com.bytter.credit.domain.service.RatCprationService;
import com.bytter.credit.domain.vo.RatBkrationAddListVO;
import com.bytter.credit.domain.vo.RatBkrationAddPageVO;
import com.bytter.credit.domain.vo.RatBkrationVO;
import com.bytter.credit.domain.vm.RatBkRationAddDtlVm;
import com.bytter.credit.domain.vm.RatBkRationAddVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author luxy
 * @version 1.0
 * @Title:RatCprationAddController
 * @Description:
 * @date 2018年11月15日 下午4:43:34
 */
@Controller
@RequestMapping("/cms/rat/ratcprationadd")
public class RatCprationAddController extends BaseController {
    @Autowired
    private RatCprationAddService ratCprationAddService;
    @Autowired
    private RatCprationService ratCprationService;
    /**
     * 查询内部授信追加列表
     *
     * @param
     * @return
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
        input.addFilter(MatchTypeEnum.EQ, "status", RationConstant.STATUS_CHECK);// 授信审批通过
        DataTablesOutput output = ratCprationService.queryRatCprationData(input,RationConstant.CMS_CPRATION_EDIT_QUERY,getUserId());
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
        return "cms/rat/cpration/listRatCprationAdd";
    }
    /**
     * 授信追加信息表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(RatBkrationAdd ratCprationAdd, Model model) {
        if (StringUtils.isNotEmpty(ratCprationAdd.getRatCode())) {
            RatBkrationAddPageVO ratCpration = ratCprationAddService.getRatRationAddInfo(ratCprationAdd.getRatCode());
            model.addAttribute("ratCprationPage", ratCpration);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信额度追加
        return "cms/rat/cpration/addRatCprationAdd";
    }
    /**
     * 保存内部授信追加信息表
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(RatBkRationAddVm listDTO, String saveType) {
        setRatCprationAddDtls(listDTO);
        ratCprationAddService.saveRationAdd(listDTO, saveType, this.getCurrentUser());
        return success();
    }
    /**
     * 内部授信追加列表
     */
    @GetMapping("/addList")
    public String addList(RatBkrationAddListVO addVO, Model model, Integer start) {
        queryRationAddData(addVO, model, start, RationConstant.RAT_ADD_FLAG_COMPANY,
                RationConstant.CMS_RATION_ADD_QUERY);// 银行授信追加
        return "cms/rat/cpration/listRatCprationAdded";
    }
    /**
     * 追加明细
     */
    @SuppressWarnings("rawtypes")
    public void queryRationAddData(RatBkrationAddListVO addVO, Model model, Integer start, String flag,
            String resource) {
        DataTablesInput input = new DataTablesInput();
        if (null == start) {
            start = Integer.valueOf(1);
        }
        input.setStart((start - 1) * input.getLength());
        if (!StringUtils.isEmpty(addVO.getCorpId())) {
            input.addFilter(MatchTypeEnum.EQ, "corpId", addVO.getCorpId());// 单位
        }
        input.addFilter(MatchTypeEnum.EQ, "ratCode", addVO.getRatCode());// 授信录入主键编号
        input.addFilter(MatchTypeEnum.EQ, "flag", flag);// 内部授信追加
        DataTablesOutput output = ratCprationAddService.queryRationAddData(input, getUserId());
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
        model.addAttribute("addVO", addVO);
        model.addAttribute("model", result);
    }
    /**
     * 查看追加信息
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            RatBkrationAddPageVO ratCpration = ratCprationAddService.editRatRationAddInfo(id);
            model.addAttribute("ratCprationPage", ratCpration);
        }
        model.addAttribute("viewFlag", "1");
        return "cms/rat/cpration/viewRatCprationAdd";
    }
    /**
     * 删除追加列表
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationAdd ratCprationAdd = ratCprationAddService.get(id);
        ratCprationAdd.setStatus(RationConstant.STATUS_DELETE);
        ratCprationAddService.updateEntity(ratCprationAdd);
        return success();
    }
    /**
     * 修改内部授信追加信息
     */
    @GetMapping("/update")
    public String update(RatBkrationAdd rationAdd, Model model) {
        if (StringUtils.isNotEmpty(rationAdd.getId())) {
            RatBkrationAddPageVO ratCpration = ratCprationAddService.editRatRationAddInfo(rationAdd.getId());
            model.addAttribute("ratCprationPage", ratCpration);
        }
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信额度追加
        return "cms/rat/cpration/editRatCprationAdd";
    }
    /**
     * 设置追加明细
     *
     * @param
     * @return
     */
    private void setRatCprationAddDtls(RatBkRationAddVm listDTO) {
        List<RatBkRationAddDtlVm> dtls = new ArrayList<RatBkRationAddDtlVm>();
        List<RatBkRationAddDtlVm> rationDtls = listDTO.getRationDtls();
        if (null == rationDtls) {
            rationDtls = new ArrayList<RatBkRationAddDtlVm>();
        }
        for (RatBkRationAddDtlVm dtlDTO : rationDtls) {
            if (null != dtlDTO) {
                if (EmptyUtils.isEmpty(dtlDTO.getItemCode())) {
                    continue;
                }
                if (RationConstant.RAT_COMFLAG_0.equals(dtlDTO.getItemFlag())) {
                    if (null == dtlDTO.getMoney()) {
                        dtlDTO.setMoney(BigDecimal.ZERO);
                    }
                    if (dtlDTO.getMoney().compareTo(BigDecimal.ZERO) != 0) {
                        dtls.add(dtlDTO);
                    }
                }
            }
        }
        // 按照单品种排序
        Collections.sort(dtls, new Comparator<RatBkRationAddDtlVm>() {
            public int compare(RatBkRationAddDtlVm o1, RatBkRationAddDtlVm o2) {
                return o1.getItemOrder().compareTo(o2.getItemOrder());
            }
        });
        listDTO.setRationDtls(dtls);
    }
    /**
     * 追加审批列表页面
     */
    @GetMapping("/listRatCpAddApprove")
    public String listRatCpAddApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("flag", RationConstant.RAT_ADD_FLAG_COMPANY);// 内部授信
        return "cms/rat/cpration/listRatCprationAddApprove";
    }
    /**
     * 追加审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/RatCpAddDataApprove")
    @ResponseBody
    public DataTablesOutput RatCpAddDataApprove(@RequestBody DataTablesInput input) {
        Object startDate2 = input.getFormdata().get("startDate2");
        if (!EmptyUtils.isEmpty(startDate2.toString())) {
            Date endDate = DateUtils.str2Date(startDate2.toString(), DateUtils.date_sdf());
            input.addFilter(MatchTypeEnum.LT, "startDate2",
                    DateUtils.format(DateUtils.addDay(endDate, 1), "yyyy-MM-dd"), Date.class);// 小于等于授信结束日期
        }
        return ratCprationAddService.listApproveOutputData(input);
    }
    /**
     * 追加审批详情页面
     */
    @GetMapping("/viewRatCpApprove")
    public String viewRatCpApprove(String id, Model model, String taskId) {
        RatBkrationAddPageVO ratCpration = ratCprationAddService.editRatRationAddInfo(id);
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/viewRatCprationAddApprove";
    }
    /**
     * 追加手机审批详情页面
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, Model model, String taskId) {
        RatBkrationAddPageVO ratCpration = ratCprationAddService.editRatRationAddInfo(businessId);
        model.addAttribute("ratCprationPage", ratCpration);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/rat/cpration/mobileRatCprationAdd";
    }
}
