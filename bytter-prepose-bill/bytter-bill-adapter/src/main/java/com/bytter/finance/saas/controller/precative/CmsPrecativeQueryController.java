package com.bytter.finance.saas.controller.precative;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeQueryService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value = "/v1-cms/cms/precative/precativeQuery", tags = "委托贷款发放表")
@RestController("v1CmsPrecativeQueryController")
@RequestMapping("/v1-cms/cms/precative/precativeQuery")
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
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> model = new HashMap<>();
        String relateCon = cmsPrecativeLoanInfoService.relateCon();
        model.put("relateCon", relateCon);
        return AjaxJson.success(model);
    }

    /**
     * 查询页面跳转
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTablePrecativeQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsPrecativeQueryService.dataTableQuery(input, getUserId()));
    }

    /****
     * 已还记录查询
     */
    @GetMapping("/repayList/{billCode}")
    public AjaxJson<Map<String, Object>> repayList(@PathVariable("billCode") String billCode) {
        Map<String, Object> model = new HashMap<>();
        List<CmsPrecativeLoanRepayVo> list = cmsPrecativeQueryService.queryRepayList(billCode);
        // cmsPrecativeQueryService.checkRepayMessage(list);
        model.put("repayList", list);
        return AjaxJson.success(model);
    }

    /****
     * 在途记录查询
     */
    @GetMapping("/passageList/{billCode}")
    public AjaxJson<Map<String, Object>> passageList(@PathVariable("billCode") String billCode) {
        Map<String, Object> model = new HashMap<>();
        List<CmsPrecativeLoanRepayVo> list = cmsPrecativeQueryService.queryPassageList(billCode);
        model.put("repayList", list);
        return AjaxJson.success(model);
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
