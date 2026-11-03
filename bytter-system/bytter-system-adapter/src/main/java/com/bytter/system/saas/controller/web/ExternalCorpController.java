package com.bytter.system.saas.controller.web;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.ExternalCorpAuditLogService;
import com.bytter.system.service.ExternalCorpService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.CorpVO;
import com.bytter.system.vo.ExternalCorpBankAccDetailVO;
import com.bytter.system.vo.ExternalCorpVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zhoul on 2017/6/21.
 */
@Api(value = "内部单位相关",tags = "内部单位相关")
@RestController("v1ExternalCorpController")
@RequestMapping("/v1-system/sys/externalCorp")
public class ExternalCorpController extends BaseController {
    @Autowired
    private CorpService corpService;
    @Autowired
    private UserService userService;
    @Autowired
    private ExternalCorpService externalCorpService;
    @Autowired
    private ExternalCorpAuditLogService externalCorpAuditLogService;

    @ApiOperation("获取首页数据")
    @GetMapping("/index")
    public AjaxJson<List<CorpVO>> index() {
        return AjaxJson.success(corpService.listAllTree());
    }

    /**
     * 查询已选择账号
     *
     * @return
     */
    @ApiOperation("支付收款人查询已选择账号")
    @PostMapping("/selectExternalCorpView")
    public AjaxJson<List<ExternalCorpVO>> selectExternalCorpView(@RequestBody ExternalCorpVO externalCorp ) {
        List<ExternalCorpVO> list = externalCorpService.selectExternalCorpView(externalCorp);
        return AjaxJson.success(list);
    }

    /**
     * 分页查询支付收款人
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("分页查询支付收款人")
    @PostMapping("/dataTableForSelect")
    public AjaxJson<DataTablesOutput<ExternalCorpVO>> dataTableForSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Map<String, Object> vo = dataTablesInput.getParams();
        if (vo != null && !vo.isEmpty()) {
            Optional.ofNullable(vo.get("auditStatus"))
                    .ifPresent(status -> input.addFormData(MatchTypeEnum.EQ, "auditStatus",
                            status, Integer.class));
            Optional.ofNullable(vo.get("status"))
                    .ifPresent(status -> input.addFormData(MatchTypeEnum.EQ, "status",
                            status, Integer.class));
            Optional.ofNullable(vo.get("bwType"))
                    .ifPresent(bwType -> input.addFormData(MatchTypeEnum.EQ, "bwTypeName",
                            bwType, String.class));
            Optional.ofNullable(vo.get("code"))
                    .ifPresent(code -> input.addFormData(MatchTypeEnum.LIKE, "code",
                            code, String.class));
            Optional.ofNullable(vo.get("name"))
                    .ifPresent(name -> input.addFormData(MatchTypeEnum.LIKE, "name",
                            name, String.class));
        }
        return AjaxJson.success(externalCorpService.dataTableForSelect(input));
    }


    /**
     * 分页查询客商银行账户（国内+国外）
     * <p>params.fex 类型：1-国内（默认），0-国外，2-国内外全部</p>
     */
    @PostMapping("/selectExternalCorp.json")
    @ResponseBody
    public DataTablesOutput selectExternalCorp(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input= dataTablesInput.convertAndInitColumns();
        //客商类型：1-国内（默认），0-国外，2-国内外全部
        Optional.ofNullable(dataTablesInput.getParams())
                .map(params -> params.get("fex"))
                .ifPresent(fex -> input.addFormData("fex", fex));
        IPageResult<ExternalCorpBankAccDetailVO> pageResult = externalCorpService.listByExternalDetail(input);
        return pageResult.convert(DataTablesOutput.class);
    }

}
