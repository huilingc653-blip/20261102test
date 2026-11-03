package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaContractOccupService;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.GuaContractReleaseDTO;
import com.bytter.guarantee.dto.GuaContractSubmitDTO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 被担保合同变更
 */
@Api(tags = "被担保合同变更",value = "/v1-guarantee/cms/guarantee/guaContractChange")
@RestController("v1GuaContractChangeController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaContractChange")
public class GuaContractChangeController extends BaseController {
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private GuaContractOccupService guaContractOccupService;
    /**
     * 担保合同变更列表
     *
     * @param input 查询参数
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "担保合同变更列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        Map<String, Object> map = dataTablesInput.getFormdata();
        map.put("eqs_bussinessType", GuaranteeConstant.CON_TYPE_0);// 担保合同类型为被担保合同
        return success(guaContractChangeService.queryContractChangeList(dataTablesInput, GuaranteeConstant.GUA_CONTRACT_INFO_QUERY,
                this.getCurrentUser()));
    }
    /**
     * 变更页面跳转
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "跳转担保合同变更数据获取")
    @GetMapping("/editContractInfo/{id}")
    public AjaxJson<GuaContractInfoPageVO> editContractInfo(@PathVariable("id") String id) {
        GuaContractInfoPageVO pageVO = new GuaContractInfoPageVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO info = guaContractChangeService.queryContractInfoById(id);
            guaContractInfoService.getGuaPerson(info);
            pageVO.setInfo( info);
            String guaWays = guaContractChangeService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                pageVO.setGuaWays(guaWays);
            }
        }
        return success(pageVO);
    }
    /**
     * 保存担保合同信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(@RequestBody GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        guaContractChangeService.tempOrSubmit(contractVO, this.getCurrentUser());
        return success();
    }
    /**
     * 历史详情变更页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @ApiOperation(value = "历史详情变更数据获取")
    @GetMapping("/queryGuaContractHistory/{id}")
    public AjaxJson<List<GuaConrrantChangeTrackVO>> queryGuaContractHistory(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            List<GuaConrrantChangeTrackVO> trackList = guaContractChangeService.queryGuaContractHistory(id);
            return success(trackList);
        }
        return success();
    }
    private void handelData(GuaContractInfoVO contractVO) {
        // 去掉无效数据
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        if (EmptyUtils.isEmptyList(guaDtls)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL);
        }
        List<GuaContractWayVO> wayList = new ArrayList<>();
        for (GuaContractWayVO guaWay : guaDtls) {
            if (guaWay.getMoney() == null) {
                continue;
            }
            if (null != guaWay.getWayCodes()) {
                wayList.add(guaWay);
            }
        }
        contractVO.setGuaDtls(wayList);
    }
    /**
     * 提交担保调整
     *
     * @return
     */
    @LogMessage(content = "调整担保提交操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(@RequestBody GuaContractSubmitDTO contractDTO) {
        if (contractDTO.getContractIds() != null) {
            if (contractDTO.getContractIds().contains(",")) {
                for (String contractId : contractDTO.getContractIds().split(",")) {
                    GuaContractOccup guaContractOccup = new GuaContractOccup();
                    guaContractOccup.setBusinessId(contractDTO.getBusinessId());
                    guaContractOccup.setContractId(contractId);
                    guaContractOccup.setItemNo(contractDTO.getBillCode());
                    guaContractOccup.setSysCode(contractDTO.getSysCode());
                    guaContractOccup.setUseDate(DateUtils.getDate());
                    guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_USED);
                    guaContractOccupService.saveEntity(guaContractOccup);// 将挂接担保信息写入担保占用表GUA_CONTRACT_OCCUP
                }
            } else {
                GuaContractOccup guaContractOccup = new GuaContractOccup();
                guaContractOccup.setBusinessId(contractDTO.getBusinessId());
                guaContractOccup.setContractId(contractDTO.getContractIds());
                guaContractOccup.setItemNo(contractDTO.getBillCode());
                guaContractOccup.setSysCode(contractDTO.getSysCode());
                guaContractOccup.setUseDate(DateUtils.getDate());
                guaContractOccup.setStatus(GuaranteeConstant.GUA_CONTRACT_USED);
                guaContractOccupService.saveEntity(guaContractOccup);// 将挂接担保信息写入担保占用表GUA_CONTRACT_OCCUP
            }
        }
        return success();
    }
    /**
     * 跳转到调整担保页面
     *
     * @param id
     * @return
     */
    @LogMessage(content = "跳转到调整担保页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/changeGuaContract")
    public AjaxJson<GuaChangeGuaContractVO> changeGuaContract(String id, String sysCode, String corpId, String curId, String billCode) {
        GuaChangeGuaContractVO contractVO = new GuaChangeGuaContractVO();
        List<GuaContractOccup> guaContractList = guaContractOccupService.queryByBusinessId(id,
                GuaranteeConstant.GUA_CONTRACT_USED);
        String contractIds = "";
        for (GuaContractOccup guaContractOccup : guaContractList) {
            contractIds += guaContractOccup.getContractId() + ",";
        }
        ParamsVO vo = new ParamsVO();
        vo.setContractIds(contractIds);
        vo.setCorpId(corpId);
        vo.setCurId(curId);
        // vo.setStartDate();
        // vo.setEndDate();
        if (contractIds != null || contractIds != "") {
            List<GuaContractInfoVO> list = guaContractInfoService.getContractList(vo);
            contractVO.setContractlist(list);
        }
        contractVO.setParamsVO(vo);
        contractVO.setBusinessId(id);
        contractVO.setSysCode(sysCode);
        contractVO.setBillCode(billCode);
        contractVO.setCorpId(corpId);
        return success(contractVO);
    }
    /**
     * 释放担保合同操作
     *
     */
    @LogMessage(content = "释放担保合同操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/release")
    @ResponseBody
    public AjaxJson release(@RequestBody GuaContractReleaseDTO guaContractReleaseDTO) {
        // 改变担保合同占用表的数据状态
        GuaContractOccup guaContractOccup = guaContractOccupService.queryByParams(guaContractReleaseDTO.getBusinessId(),
                guaContractReleaseDTO.getContractId(), GuaranteeConstant.GUA_CONTRACT_USED);
        Integer status = GuaranteeConstant.GUA_CONTRACT_RELEASE;// 释放状态
        guaContractOccupService.changeStatusById(guaContractOccup.getId(), status);
        return success();
    }
}
