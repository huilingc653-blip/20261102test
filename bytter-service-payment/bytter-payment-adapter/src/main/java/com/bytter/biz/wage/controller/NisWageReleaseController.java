package com.bytter.biz.wage.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.wage.service.NisWageReleaseConfigService;
import com.bytter.biz.wage.vo.NisWageReleaseBatchUpdateVO;
import com.bytter.biz.wage.vo.NisWageReleaseConfigVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.system.api.dto.BtBankAccDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 代发工资下拨金额配置
 *
 * @author loveCamille
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/wage/wageReleaseConfig")
public class NisWageReleaseController extends BaseController {

    private final NisWageReleaseConfigService wageReleaseConfigService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("releaseDate", DateUtil.format(DateTime.now(), DatePattern.NORM_MONTH_PATTERN));
        return "wage/listNisWageReleaseConfig";
    }

    @GetMapping("/batchSetWageRelease")
    public String batchSetWageRelease(String ids, Model model) {
        model.addAttribute("ids", ids);
        return "wage/batchSetWageRelease";
    }

    @GetMapping("/edit")
    public String edit(String id, BigDecimal releaseAmount, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("releaseAmount", releaseAmount);
        return "wage/editWageReleaseConfig";
    }

    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput<NisWageReleaseConfigVO> dataTable(@RequestBody DataTablesInput input) {
        return wageReleaseConfigService.dataTableQuery(input, getCurrentUser());
    }

    @ResponseBody
    @PostMapping("/batchConfirm")
    public AjaxJson batchConfirm(@RequestParam("ids") String ids, Integer status) {
        if (StrUtil.isBlank(ids)) return success();
        List<String> idList = Arrays.asList(ids.split(","));
        wageReleaseConfigService.batchConfirm(idList, status, getCurrentUser());
        return success();
    }

    @ResponseBody
    @PostMapping("/getBankAccQuery")
    public List<BtBankAccDTO> getBankAccQuery(String corpIds) {
        List<String> corpIdList = null;
        if (StrUtil.isNotBlank(corpIds)) {
            corpIdList = Arrays.asList(corpIds.split(","));
        }
        return wageReleaseConfigService.getBankAccQuery(corpIdList, getCurrentUser());
    }

    @ResponseBody
    @PostMapping("/deleteByIds")
    public AjaxJson deleteByIds(String ids) {
        if (StrUtil.isNotBlank(ids)) {
            wageReleaseConfigService.batchDelete(Arrays.asList(ids.split(",")));
        }
        return success();
    }

    @ResponseBody
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisWageReleaseConfigVO> list) {
        wageReleaseConfigService.saveImport(list, this.getCurrentUser());
        return success();
    }

    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(@RequestParam("id") String id, @RequestParam("releaseAmount") BigDecimal releaseAmount) {
        wageReleaseConfigService.update(id, releaseAmount, this.getCurrentUser());
        return success();
    }

    @ResponseBody
    @PostMapping("/batchUpdateAmount")
    public AjaxJson batchUpdateAmount(NisWageReleaseBatchUpdateVO updateVO) {
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
