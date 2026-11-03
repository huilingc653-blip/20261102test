package com.bytter.letter.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.letter.domain.service.GuaGuaranteeChangeInfoService;
import com.bytter.letter.domain.service.GuaGuaranteeInfoService;
import com.bytter.letter.domain.vo.GuaGuaranteeChangeInfoVO;
import com.bytter.letter.domain.vo.GuaGuaranteeInfoVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@Controller
@RequestMapping("/letter/guarantee/guaGuaranteeChangeInfo")
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
     * 保函更改列表页面
     */
    @LogMessage(content = "跳转到保函更改列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaGuaranteeChangeInfo";
    }
    /**
     * 保函更改列表数据
     */
    @LogMessage(content = "查询保函更改列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return LgChangeInfoService.datatableList(input, this.getCurrentUser());
    }
    /**
     * 保函更改申请页面
     */
    @LogMessage(content = "跳转到保函更改申请页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/addChange")
    public String addChange(Model model) {
        model.addAttribute("inputDate", DateUtils.date2Str(DateUtils.date_sdf()));
        String guaraChangeNo = LgChangeInfoService.getlgChangeNo(SequenceEnum.CMS_GUARANTEE_CHANGE_NO,
                LetterConstant.GUA_GUARANTEE_CHANGE_CODE);// 生成更改申请编号
        model.addAttribute("guaraChangeNo", guaraChangeNo);
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
        }
        return "cms/guarantee/addGuaGuaranteeChangeInfo";
    }
    /**
     * 保存保函更改信息
     */
    @LogMessage(content = "保存保函更改信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(GuaGuaranteeChangeInfoVO lgChangeInfoVo, RatUrationDTO urationDTO) {
        LgChangeInfoService.tempOrSubmit(lgChangeInfoVo, urationDTO, this.getCurrentUser());
        return success();
    }
    /**
     * 根据申请单位查询保函申请列表
     */
    @LogMessage(content = "根据申请单位查询保函申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLgInfo")
    @ResponseBody
    public List<GuaGuaranteeInfoVO> queryLgByCorpId(GuaGuaranteeInfoVO infoVO) {
        List<GuaGuaranteeInfoVO> lglist = new ArrayList<GuaGuaranteeInfoVO>();
        if (StringUtils.isNotEmpty(infoVO.getCorpId())) {
            lglist = guaLgInfoService.queryLgByCorpId(infoVO);
        }
        return lglist;
    }
    /**
     * 根据id查询保函申请详情
     */
    @LogMessage(content = "根据id查询保函申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/queryLgInfoById")
    @ResponseBody
    public GuaGuaranteeInfoVO queryLgInfoById(String guaranid) {
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
        return lgInfoVo;
    }
    /**
     * 查看保函更改详情
     */
    @LogMessage(content = "跳转到保函更改查看页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
            model.addAttribute("lgChangeInfoVo", lgChangeInfoVo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        lgChangeInfoVo.getGuaraId());
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
                String curId = lgChangeInfoVo.getCurId();
                String corpId = lgChangeInfoVo.getCorpId();
                CorpDTO corpDTO = corpService.get(corpId);
                if (null != corpDTO) {
                    if (curId.equals(corpDTO.getCurId())) {
                        model.addAttribute("naturalSign", LetterConstant.GUA_COMMON_Y);// 设置本位币标识
                    } else {
                        model.addAttribute("naturalSign", LetterConstant.GUA_COMMON_N);// 设置本位币标识
                    }
                }
            }
        }
        return "cms/guarantee/viewGuaGuaranteeChangeInfo";
    }
    /**
     * 修改保函更改申请
     */
    @LogMessage(content = "跳转到保函更改修改页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
            model.addAttribute("lgChangeInfoVo", lgChangeInfoVo);
            ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
            if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
                model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
            }
            // 挂接的授信信息
            if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
                RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                        lgChangeInfoVo.getGuaraId());
                model.addAttribute("urationDTO", urationDTO);// 授信明细
                model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
                String curId = lgChangeInfoVo.getCurId();
                String corpId = lgChangeInfoVo.getCorpId();
                CorpDTO corpDTO = corpService.get(corpId);
                if (null != corpDTO) {
                    if (curId.equals(corpDTO.getCurId())) {
                        model.addAttribute("naturalSign", LetterConstant.GUA_COMMON_Y);// 设置本位币标识
                    } else {
                        model.addAttribute("naturalSign", LetterConstant.GUA_COMMON_N);// 设置本位币标识
                    }
                }
            }
        }
        return "cms/guarantee/updateGuaGuaranteeChangeInfo";
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
     * 审批列表页面
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "跳转到保函更改申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaGuaranteeChangeInfoApprove";
    }
    /**
     * 查询审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput dataTablesInput) {
        return LgChangeInfoService.getApproveDataTables(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 审批详情
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(id);
        model.addAttribute("lgChangeInfoVo", lgChangeInfoVo);
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
            RatUrationDTO urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                    lgChangeInfoVo.getGuaraId());
            model.addAttribute("ratBankCode", urationDTO.getBankCode());// 授信银行
            model.addAttribute("urationDTO", urationDTO);// 授信明细
        }
        ConfigDTO isHangBailConfig = guaLgInfoService.getIsHangBail();
        if (isHangBailConfig.getValue().equals(LetterConstant.GUA_COMMON_Y)) {
            model.addAttribute("isHangBail", isHangBailConfig.getValue());// 参数设置是否挂接保证金
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/viewGuaGuaranteeChangeInfoApprove";
    }
    @LogMessage(content = "根据保函id查询授信信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/queryRatUration")
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
    public String mobileApprove(String businessId, String taskId, Model model) {
        GuaGuaranteeChangeInfoVO lgChangeInfoVo = LgChangeInfoService.queryChangeInfoById(businessId);
        model.addAttribute("lgChangeInfoVo", lgChangeInfoVo);
        // 挂接的授信信息
        if (RationConstant.RAT_COMMON_Y.equals(lgChangeInfoVo.getRatmodel())) {
            RatUrationDTO ratUrationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.GUA,
                    lgChangeInfoVo.getGuaraId());
            String isRmbRat = "";// 是否本位币
            List<RatUrationDisDTO> rations = urationService.viewCorpDisRation(ratUrationDTO, isRmbRat);
            model.addAttribute("rations", rations);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/mobileGuaGuaranteeChangeInfo";
    }
}
