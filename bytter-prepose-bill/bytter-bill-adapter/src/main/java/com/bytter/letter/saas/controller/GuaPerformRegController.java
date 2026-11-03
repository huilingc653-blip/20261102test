package com.bytter.letter.saas.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.guarantee.velocity.tags.FcBondTag;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.entity.GuaPerformReg;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.service.GuaPerformRegService;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.letter.domain.vo.GuaPerformRegDetailVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * 履行登记controller
 *
 * @author fusb
 * @version 1.0
 * @Title GuaGuaranteeReciveController
 * @Description
 * @date 2019年1月7日
 */
@Api(value = "/v1-guarantee/letter/guarantee/guaPerformReg",tags = "履行登记")
@RestController("v1GuaPerformRegController")
@RequestMapping("/v1-guarantee/letter/guarantee/guaPerformReg")
public class GuaPerformRegController extends BaseController {
    @Autowired
    GuaPerformRegService guaPerformRegService;
    @Autowired
    private GuaGuaranteeInfoService guaLgInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    /** 列表内容 **/
    @ApiOperation("列表内容")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(guaPerformRegService.getDataTablesUnion(tablesInput, this.getCurrentUser()));
    }
    /**
     * 履行登记
     *
     * @return
     */
    @LogMessage(content = "履行登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/registerGuaPerform/{id}")
    @ResponseBody
    public AjaxJson<GuaPerformRegDetailVO> receiveGuaGuarantee(@PathVariable("id") String id) {
        GuaPerformRegDetailVO detailVO = new GuaPerformRegDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeInfoVO vo = guaPerformRegService.getGuaGuaranteeInfoById(id);
            detailVO.setVo(vo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                detailVO.setIsHangBail(isHangBailConfig.getValue());
            }
            if (RationConstant.RAT_COMMON_Y.equals(vo.getRatmodel())) {
                detailVO.setUrationDTO(urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id));
            }
            BtCurrencyDTO guaCurDTO = currencyService.getObjectById(vo.getCurId());
            detailVO.setCurExrate(guaCurDTO.getCurExrate());
            detailVO.setNaturalSign(guaCurDTO.getNaturalSign());

            if (StringUtils.isNotBlank(vo.getIsHookBond()) && "1".equals(vo.getIsHookBond())) {
                FcBondTag fcBondTag = new FcBondTag();
                detailVO.setFcBondTag(fcBondTag);
            }
        }
        return success(detailVO);
    }
    /**
     * 保存履行登记相关信息
     */
    @ApiOperation("保存履行登记相关信息")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson saveGuaPerformReg(@RequestBody GuaPerformReg guaPerformReg) {
        guaPerformRegService.saveGuaPerformReg(guaPerformReg, this.getCurrentUser());
        return success();
    }
}
