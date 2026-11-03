package com.bytter.letter.saas.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.letter.domain.constant.LetterConstant;
import com.bytter.letter.domain.dto.GuaGuaranteeChangeSubmitDTO;
import com.bytter.letter.domain.service.GuaGuaranteeChangeInfoService;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeAddVO;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeDetailVO;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeInfoController
 * @Description:保函更改申请控制层
 * @date 2019年1月9日 上午10:47:18
 */
@Api(tags = "保函更改申请控制层",value = "/v1-guarantee/letter/guarantee/guaGuaranteeChangeInfo")
@RestController("v1GuaGuaranteeChangeInfoController")
@RequestMapping("/v1-guarantee/letter/guarantee/guaGuaranteeChangeInfo")
public class GuaGuaranteeChangeInfoController extends BaseController {
    @Autowired
    private GuaGuaranteeChangeInfoService LgChangeInfoService;
    @Autowired
    private GuaGuaranteeInfoService guaLgInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 保函更改列表数据
     */
    @LogMessage(content = "查询保函更改列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput tablesInput = input.convertAndInitColumns();
        return success(LgChangeInfoService.datatableList(tablesInput, this.getCurrentUser()));
    }
    /**
     * 保函更改申请页面
     */
    @LogMessage(content = "跳转到保函更改申请页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addChange")
    @ResponseBody
    public AjaxJson<GuaGuaranteeChangeAddVO> addChange() {
        GuaGuaranteeChangeAddVO vo = new GuaGuaranteeChangeAddVO();
        vo.setInputDate(DateUtils.date2Str(DateUtils.date_sdf()));
        String guaraChangeNo = LgChangeInfoService.getlgChangeNo(SequenceEnum.CMS_GUARANTEE_CHANGE_NO,
                LetterConstant.GUA_GUARANTEE_CHANGE_CODE);
        vo.setGuaraChangeNo(guaraChangeNo);
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            vo.setIsHangBail(isHangBailConfig.getValue());
        }
        return success(vo);
    }
    /**
     * 保存保函更改信息
     */
    @LogMessage(content = "保存保函更改信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(@RequestBody GuaGuaranteeChangeSubmitDTO request) {
        GuaGuaranteeChangeInfoVO lgChangeInfoVo = request.getLgChangeInfoVo();
        RatUrationDTO urationDTO = request.getUrationDTO();
        LgChangeInfoService.tempOrSubmit(lgChangeInfoVo, urationDTO, this.getCurrentUser());
        return success();
    }
    /**
     * 根据申请单位查询保函申请列表
     */
    @ApiOperation(value = "根据申请单位查询保函申请列表")
    @LogMessage(content = "根据申请单位查询保函申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLgInfo")
    @ResponseBody
    public AjaxJson<List<GuaGuaranteeInfoVO>> queryLgByCorpId(GuaGuaranteeInfoVO infoVO) {
        List<GuaGuaranteeInfoVO> lglist = new ArrayList<GuaGuaranteeInfoVO>();
        if (StringUtils.isNotEmpty(infoVO.getCorpId())) {
            lglist = guaLgInfoService.queryLgByCorpId(infoVO);
        }
        return success(lglist);
    }
    /**
     * 根据id查询保函申请详情
     */
    @ApiOperation(value = "根据id查询保函申请详情")
    @LogMessage(content = "根据id查询保函申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryLgInfoById")
    @ResponseBody
    public AjaxJson<GuaGuaranteeInfoVO> queryLgInfoById(String guaranid) {
        GuaGuaranteeInfoVO lgInfoVo = guaLgInfoService.getGuaranteeInfoById(guaranid);
        if (null != lgInfoVo) {
            // 查询针对同一保函申请做过变更的所有更改列表
            List<GuaGuaranteeChangeInfoVO> lgChangeList = LgChangeInfoService.queryChangeListByGuaId(guaranid);
            if (lgChangeList.size() > 0) {
                Integer changeCount = lgChangeList.get(0).getChangeCount();// 按更改次数倒序排序，取第一条
                lgInfoVo.setChangeCount(changeCount + 1);
            } else {
                lgInfoVo.setChangeCount(1);
            }
        }
        String curId = lgInfoVo.getCurId();
        String corpId = lgInfoVo.getCorpId();
        CorpDTO corpDTO = corpService.get(corpId);
        if (null != corpDTO) {
            if (curId.equals(corpDTO.getCurId())) {
                lgInfoVo.setNaturalSign(LetterConstant.GUA_COMMON_Y);// 设置本位币标识
            } else {
                lgInfoVo.setNaturalSign(LetterConstant.GUA_COMMON_N);// 设置本位币标识
            }
        }
        return success(lgInfoVo);
    }
    /**
     * 查看保函更改详情
     */
    @LogMessage(content = "跳转到保函更改查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<GuaGuaranteeChangeDetailVO> view(@PathVariable("id") String id) {
        GuaGuaranteeChangeDetailVO detailVO = new GuaGuaranteeChangeDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
            detailVO.setLgChangeInfoVo(lgChangeInfoVo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                detailVO.setIsHangBail(isHangBailConfig.getValue());
            }
            if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        lgChangeInfoVo.getGuaraId());
                detailVO.setUrationDTO(urationDTO);
                detailVO.setRatBankCode(urationDTO.getBankCode());
                String curId = lgChangeInfoVo.getCurId();
                String corpId = lgChangeInfoVo.getCorpId();
                CorpDTO corpDTO = corpService.get(corpId);
                if (null != corpDTO) {
                    if (curId.equals(corpDTO.getCurId())) {
                        detailVO.setNaturalSign(LetterConstant.GUA_COMMON_Y);
                    } else {
                        detailVO.setNaturalSign(LetterConstant.GUA_COMMON_N);
                    }
                }
            }
        }
        return success(detailVO);
    }
    /**
     * 修改保函更改申请
     */
    @LogMessage(content = "跳转到保函更改修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/update/{id}")
    @ResponseBody
    public AjaxJson<GuaGuaranteeChangeDetailVO> update(@PathVariable("id") String id) {
        GuaGuaranteeChangeDetailVO detailVO = new GuaGuaranteeChangeDetailVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
            detailVO.setLgChangeInfoVo(lgChangeInfoVo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                detailVO.setIsHangBail(isHangBailConfig.getValue());
            }
            if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        lgChangeInfoVo.getGuaraId());
                detailVO.setUrationDTO(urationDTO);
                detailVO.setRatBankCode(urationDTO.getBankCode());
                String curId = lgChangeInfoVo.getCurId();
                String corpId = lgChangeInfoVo.getCorpId();
                CorpDTO corpDTO = corpService.get(corpId);
                if (null != corpDTO) {
                    if (curId.equals(corpDTO.getCurId())) {
                        detailVO.setNaturalSign(LetterConstant.GUA_COMMON_Y);
                    } else {
                        detailVO.setNaturalSign(LetterConstant.GUA_COMMON_N);
                    }
                }
            }
        }
        return success(detailVO);
    }
    /**
     * 删除保函更改申请
     */
    @LogMessage(content = "删除保函更改信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        LgChangeInfoService.deleteById(id, this.getCurrentUser());
        return success();
    }

    /**
     * 查询审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "查询审批列表数据")
    @PostMapping("/approveDataList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> approveDataList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(LgChangeInfoService.getApproveDataTables(tablesInput, this.getCurrentUser()));
    }
    /**
     * 审批详情
     */
    @GetMapping("/viewApprove")
    @ResponseBody
    public AjaxJson<GuaGuaranteeChangeDetailVO> viewApprove(String id, String taskId) {
        GuaGuaranteeChangeDetailVO detailVO = new GuaGuaranteeChangeDetailVO();
        GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
        detailVO.setLgChangeInfoVo(lgChangeInfoVo);
        if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
            RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                    lgChangeInfoVo.getGuaraId());
            detailVO.setRatBankCode(urationDTO.getBankCode());
            detailVO.setUrationDTO(urationDTO);
        }
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            detailVO.setIsHangBail(isHangBailConfig.getValue());
        }
        detailVO.setTaskId(taskId);
        return success(detailVO);
    }
    @ApiOperation(value = "根据保函id查询授信信息")
    @LogMessage(content = "根据保函id查询授信信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/queryRatUration")
    @ResponseBody
    public RatUrationDTO getRatUrationDTOByBillId(String id) {
        RatUrationDTO ratUrationDTO = null;
        if (StringUtils.isNotEmpty("id")) {
            GuaGuaranteeInfoVO LgInfoVo = guaLgInfoService.getGuaranteeInfoById(id);
            if (RationConstant.RAT_COMMON_Y.equals(LgInfoVo.getRatmodel())) {
                ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA, id);// 授信明细
            }
        }
        return ratUrationDTO;
    }
    /**
     * 手机审批详情
     */
    @GetMapping("/mobileApprove")
    @ResponseBody
    public AjaxJson<GuaGuaranteeChangeDetailVO> mobileApprove(String businessId, String taskId) {
        GuaGuaranteeChangeDetailVO detailVO = new GuaGuaranteeChangeDetailVO();
        GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(businessId);
        detailVO.setLgChangeInfoVo(lgChangeInfoVo);
        if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                    lgChangeInfoVo.getGuaraId());
            String isRmbRat = "";
            List<RatUrationDisDTO> rations = urationService.viewCorpDisRation(ratUrationDTO, isRmbRat);
            detailVO.setRations(rations);
        }
        detailVO.setTaskId(taskId);
        return success(detailVO);
    }
}
