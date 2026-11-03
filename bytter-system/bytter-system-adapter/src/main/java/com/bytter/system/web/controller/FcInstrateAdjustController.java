package com.bytter.system.web.controller;
import com.bytter.basic.entity.FcInstrateAdjust;
import com.bytter.basic.service.FcInstrateAdjustService;
import com.bytter.basic.vo.FcInstrateAdjustSimpleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.PoolContant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 内部利率
 * @date 2017-07-11 10:54:47
 */
@Controller
@RequestMapping("/basic/business/fcinstrateadjust")
public class FcInstrateAdjustController extends BaseController {
    @Autowired
    private FcInstrateAdjustService fcInstrateAdjustService;
    @Autowired
    private ICorpService corpServiceI;
    @Autowired
    private IFcCorpPoolService fcCorpPoolServiceI;
    /**
     * 内部利率列表 页面跳转
     * param poolSign资金池标志 0内部利率 1资金池
     *
     * @return
     */
    @LogMessage(content = "查询内部利率列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{poolSign}")
    public String list(@PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("poolSign", poolSign);
        return "basic/business/listFcInstrateAdjust";
    }
    /**
     * 内部利率 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        DataTablesOutput dataTablesOutput = null;
        if (PoolContant.POOL_SIGN_1.equals(poolSign)) {
            dataTablesOutput = fcInstrateAdjustService.getDataTablesUnionForPool(dataTablesInput, this.getCurrentUser());
        } else {
            dataTablesOutput = fcInstrateAdjustService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        }
        return dataTablesOutput;
    }
    /**
     * 内部table列表
     *
     * @param request
     * @return
     */
    @PostMapping("/dyncTable")
    @ResponseBody
    public List<FcInstrateAdjustSimpleVO> dyncTable(HttpServletRequest request) {
        String account = request.getParameter("account");
        String netId = request.getParameter("netId");
        String corpId = request.getParameter("corpId");
        String subjectId = request.getParameter("subjectId");
        String curId = request.getParameter("curId");
        String instrtType = request.getParameter("instrtType");
        List<FcInstrateAdjustSimpleVO> fcInitAccs = fcInstrateAdjustService.getFcInitAccsByParams(netId, corpId, subjectId, curId, instrtType, account);
        return fcInitAccs;
    }
    /**
     * 内部利率列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询内部利率列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/checkList/{poolSign}")
    public String checkList(@PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("poolSign", poolSign);
        return "basic/business/checkListFcInstrateAdjust";
    }
    /**
     * 查询所有的待复核的调整利率 table列表
     *
     * @return
     */
    @PostMapping("/checkDataTable")
    @ResponseBody
    public DataTablesOutput checkDataTable(@RequestBody DataTablesInput dataTablesInput) {
        String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
        DataTablesOutput dataTablesOutput = null;
        if (PoolContant.POOL_SIGN_1.equals(poolSign)) {
            dataTablesOutput = fcInstrateAdjustService.getWaitCheckDataTablesPoolUnion(dataTablesInput, this.getCurrentUser());
        } else {
            dataTablesOutput = fcInstrateAdjustService.getWaitCheckDataTablesUnion(dataTablesInput, this.getCurrentUser());
        }
        return dataTablesOutput;
    }
    /**
     * 内部利率调整页面跳转
     *
     * @return
     */
    //
    @GetMapping("/adjust/{poolSign}")
    public String adjust(FcInstrateAdjust fcInstrateAdjust, Model model, @PathVariable("poolSign") String poolSign) {
        if (StringUtils.isNotEmpty(fcInstrateAdjust.getId())) {
            fcInstrateAdjust = fcInstrateAdjustService.get(fcInstrateAdjust.getId());
            model.addAttribute("fcInstrateAdjustPage", fcInstrateAdjust);
        }
        model.addAttribute("poolSign", poolSign);
        return "basic/business/addFcInstrateAdjust";
    }
    /**
     * 内部利率编辑页面跳转
     *
     * @return
     */
    //
    @GetMapping("/update/{poolSign}")
    public String update(FcInstrateAdjust fcInstrateAdjust, @PathVariable("poolSign") String poolSign, Model model) {
        commonView(fcInstrateAdjust.getId(), model, poolSign);
        model.addAttribute("poolSign", poolSign);
        return "basic/business/updateFcInstrateAdjust";
    }
    /**
     * 内部利率编辑页面跳转
     *
     * @return
     */
    //
    @GetMapping("/check/{poolSign}")
    public String check(FcInstrateAdjust fcInstrateAdjust, Model model, @PathVariable("poolSign") String poolSign) {
        model.addAttribute("poolSign", poolSign);
        commonView(fcInstrateAdjust.getId(), model, poolSign);
        return "basic/business/viewFcInstrateAdjust";
    }
    private void commonView(String id, Model model, String poolSign) {
        if (StringUtils.isNotEmpty(id)) {
            FcInstrateAdjustSimpleVO adjust = fcInstrateAdjustService.getFcInstrateAdjust(id, poolSign);
            //如果是资金池取资金池代码
            if (PoolContant.POOL_SIGN_1.equals(poolSign)) {
                model.addAttribute("netCode", (StringUtils.isEmpty(adjust.getNetId()) || fcCorpPoolServiceI.getById(adjust.getNetId()) == null) ? "" : fcCorpPoolServiceI.getById(adjust.getNetId()).getPoolCode());
            } else {
                model.addAttribute("netCode", (StringUtils.isEmpty(adjust.getNetId()) || corpServiceI.get(adjust.getNetId()) == null) ? "" : corpServiceI.get(adjust.getNetId()).getCode());
            }
            model.addAttribute("corpCode", (StringUtils.isEmpty(adjust.getCorpId()) || corpServiceI.get(adjust.getCorpId()) == null) ? "" : corpServiceI.get(adjust.getCorpId()).getCode());
            model.addAttribute("fcInstrateAdjustPage", adjust);
        }
    }
    /**
     * 内部利率详情页
     *
     * @return
     */
    //
    @LogMessage(content = "查询内部利率详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}/{poolSign}")
    public String view(@PathVariable("id") String id, @PathVariable("poolSign") String poolSign, Model model) {
        model.addAttribute("isView", "Y");
        model.addAttribute("poolSign", poolSign);
        commonView(id, model, poolSign);
        return "basic/business/viewFcInstrateAdjust";
    }
    /**
     * 添加内部利率
     *
     * @param fcInstrateAdjust
     * @return
     */
    //
    @LogMessage(content = "添加内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{poolSign}")
    @ResponseBody
    public AjaxJson save(@RequestBody List<FcInstrateAdjust> fcInstrateAdjust, @PathVariable("poolSign") String poolSign) {
        fcInstrateAdjustService.save(fcInstrateAdjust, getCurrentUser(), poolSign);
        return success();
    }
    /**
     * 修改内部利率
     *
     * @param fcInstrateAdjust
     * @return
     */
    //
    @LogMessage(content = "修改内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify/{poolSign}")
    @ResponseBody
    public AjaxJson update(FcInstrateAdjust fcInstrateAdjust, @PathVariable("poolSign") String poolSign) {
//		FcInstrateAdjust t = fcInstrateAdjustService.get(fcInstrateAdjust.getId());
//		BeanUtils.copyNotNullProperties(fcInstrateAdjust, t);
        fcInstrateAdjustService.modify(fcInstrateAdjust, getCurrentUser(), poolSign);
        return success();
    }
    /**
     * 复核
     *
     * @param fcInstrateAdjust
     * @return
     */
    //
    @LogMessage(content = "修改内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/apply")
    @ResponseBody
    public AjaxJson apply(FcInstrateAdjust fcInstrateAdjust) {
        String fcId = fcInstrateAdjust.getId();
        if (fcId != null) {
            fcInstrateAdjustService.checkFcInAdjusts(fcInstrateAdjust, getCurrentUser());
        }
        return success();
    }
    /**
     * 删除内部利率
     *
     * @param id
     * @return
     */
    //
    @LogMessage(content = "删除内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        FcInstrateAdjust fcInstrateAdjust = fcInstrateAdjustService.get(id);
        fcInstrateAdjustService.deleteEntity(fcInstrateAdjust);
        return success();
    }
    /**
     * 批量删除内部利率
     *
     * @param ids
     * @return
     */
    //
    @LogMessage(content = "批量删除内部利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        for (String id : ids.split(",")) {
            FcInstrateAdjust fcInstrateAdjust = fcInstrateAdjustService.get(id);
            fcInstrateAdjustService.deleteEntity(fcInstrateAdjust);
        }
        return success();
    }
    /**
     * 获取资金池
     *
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_RATE_ADJUST;
        return fcCorpPoolServiceI.getUserCorpPools(this.getCurrentUser(), resource);
    }
    /**
     * 获取资金池
     *
     * @param poolId
     * @return
     */
    @RequestMapping(params = "getUserCorpPoolChilds")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_RATE_ADJUST;
        return fcCorpPoolServiceI.getUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
}
