package com.bytter.finance.web.precative.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeQueryService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Controller
@RequestMapping("/cms/precative/precativeQuery")
public class CmsPrecativeQueryController extends BaseController {
    @Autowired
    private CmsPrecativeQueryService cmsPrecativeQueryService;
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    /**
     * 委托贷款查询页面跳转
     */
    @GetMapping("/list")
    public String list(Model model) {
        String relateCon = cmsPrecativeLoanInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        return "cms/precative/listCmsPrecativeQuery";
    }
    /**
     * 查询页面跳转
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTablePrecativeQuery(@RequestBody DataTablesInput input) {
        return cmsPrecativeQueryService.dataTableQuery(input, getUserId());
    }
    /****
     * 已还记录查询
     */
    @GetMapping("/repayList/{billCode}")
    public String repayList(@PathVariable("billCode") String billCode, Model model) {
        List<CmsPrecativeLoanRepayVo> list = cmsPrecativeQueryService.queryRepayList(billCode);
        // cmsPrecativeQueryService.checkRepayMessage(list);
        model.addAttribute("repayList", list);
        return "cms/precative/listRepay";
    }
    /****
     * 在途记录查询
     */
    @GetMapping("/passageList/{billCode}")
    public String passageList(@PathVariable("billCode") String billCode, Model model) {
        List<CmsPrecativeLoanRepayVo> list = cmsPrecativeQueryService.queryPassageList(billCode);
        model.addAttribute("repayList", list);
        return "cms/precative/listRepay";
    }
    /**
     * 委托人和借款人有权限的单位合集
     */
    @GetMapping("/getCorps")
    @ResponseBody
    public List<CorpDTO> getCorps() {
        List<CorpDTO> corpDTOs = new ArrayList<CorpDTO>();
        List<String> ids = iCorpService.getUserCorpIds(ResourceConstant.CMS_PRECATIVE_REPORT_QUERY, getUserId());
        if (EmptyUtils.isEmpty(ids)) {
            return new ArrayList<CorpDTO>();
        }
        cmsPrecativeLoanInfoService.getLoanOpps(ids);
        for (String id : ids) {
            CorpDTO temp = iCorpService.get(id);
            if (temp != null) {
                corpDTOs.add(temp);
            }
        }
        return TreeUtil.getTreeNodeList(corpDTOs, new Comparator<CorpDTO>() {
            @Override
            public int compare(CorpDTO o1, CorpDTO o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });
    }
}
