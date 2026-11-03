package com.bytter.biz.saas.controller.wage;

import cn.hutool.core.util.StrUtil;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.biz.wage.service.NisWageReleaseConfigService;
import com.bytter.biz.wage.vo.NisWageReleaseBatchUpdateVO;
import com.bytter.biz.wage.vo.NisWageReleaseConfigVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 代发工资下拨金额配置
 *
 * @author loveCamille
 */
@Api(value = "/v1-payment/wage/wageReleaseConfig",tags = "代发工资下拨金额配置")
@RestController("v1NisWageReleaseController")
@RequiredArgsConstructor
@RequestMapping("/v1-payment/wage/wageReleaseConfig")
public class NisWageReleaseController extends BaseController {

    private final NisWageReleaseConfigService wageReleaseConfigService;

    @DubboReference(check = false)
    private IDictService dictService;

    @ApiOperation("代发工资下拨金额配置列表查询")
    @ResponseBody
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisWageReleaseConfigVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<NisWageReleaseConfigVO> dataTableQuery = wageReleaseConfigService.dataTableQuery(input, getCurrentUser());
        List<NisWageReleaseConfigVO> data = dataTableQuery.getData();
        setDict(data);
        return success(dataTableQuery);
    }

    private void setDict(List<NisWageReleaseConfigVO> data) {
        Map<String, DictDTO> dictDTOMap = dictService.listMap("nis.wage.release.status");
        for (NisWageReleaseConfigVO vo : data) {
            if(vo.getStatus()!=null){
                vo.setStatusName(dictDTOMap.get(vo.getStatus().toString()).getName());
            }
        }
    }


    @ApiOperation("银行账号查询")
    @ResponseBody
    @GetMapping("/getBankAccQuery")
    public AjaxJson<List<BtBankAccDTO>> getBankAccQuery(@RequestParam("corpIds") String corpIds) {
        List<String> corpIdList = null;
        if (StrUtil.isNotBlank(corpIds)) {
            corpIdList = Arrays.asList(corpIds.split(","));
        }
        return AjaxJson.success(wageReleaseConfigService.getBankAccQuery(corpIdList, getCurrentUser()));
    }

    @ApiOperation("批量确认")
    @ResponseBody
    @PostMapping("/batchConfirm")
    public AjaxJson batchConfirm(@RequestBody BisIDsDTO bisIDsDTO) {
        String ids = bisIDsDTO.getIds();
        Integer status = bisIDsDTO.getStatus();
        if (StrUtil.isBlank(ids)) return success();
        List<String> idList = Arrays.asList(ids.split(","));
        wageReleaseConfigService.batchConfirm(idList, status, getCurrentUser());
        return success();
    }


    @ApiOperation("批量删除")
    @ResponseBody
    @PostMapping("/deleteByIds")
    public AjaxJson deleteByIds(@RequestBody BisIDsDTO bisIDsDTO) {
        String ids = bisIDsDTO.getIds();
        if (StrUtil.isNotBlank(ids)) {
            wageReleaseConfigService.batchDelete(Arrays.asList(ids.split(",")));
        }
        return success();
    }

    @ApiOperation("导入")
    @ResponseBody
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisWageReleaseConfigVO> list) {
        wageReleaseConfigService.saveImport(list, this.getCurrentUser());
        return success();
    }

    @ApiOperation("修改")
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(@RequestBody  NisWageReleaseBatchUpdateVO updateVO) {
        String id = updateVO.getId();
        BigDecimal releaseAmount = updateVO.getReleaseAmount();
        wageReleaseConfigService.update(id, releaseAmount, this.getCurrentUser());
        return success();
    }

    @ApiOperation("批量修改")
    @ResponseBody
    @PostMapping("/batchUpdateAmount")
    public AjaxJson batchUpdateAmount(@RequestBody  NisWageReleaseBatchUpdateVO updateVO) {
        if (updateVO.getType() == 0) {
            if (Objects.isNull(updateVO.getAmount())) {
                throw new BusinessException("金额不能为空");
            }
            // 变化金额为0
            if (BigDecimal.ZERO.compareTo(updateVO.getAmount()) == 0) {
                return success();
            }
        } else {
            if (Objects.isNull(updateVO.getRatio())) {
                throw new BusinessException("比率不能为空");
            }
            // 变化比率为0
            if (BigDecimal.ZERO.compareTo(updateVO.getRatio()) == 0) {
                return success();
            }
        }
        wageReleaseConfigService.batchUpdateAmount(updateVO);
        return success();
    }
}
