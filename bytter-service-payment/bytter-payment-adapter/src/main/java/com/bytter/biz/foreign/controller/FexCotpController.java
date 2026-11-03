package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexCotp;
import com.bytter.biz.foreign.service.FexCotpService;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.vo.FexCotpVO;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/foreign/fexCotp")
public class FexCotpController  extends BaseController {

    @Autowired
    private FexCotpService fexCotpService;
    @Autowired
    private FexProductService fexProductService;
    /**
     * 页面跳转
     *
     * @return
     */
    @GetMapping("/listFexCotp")
    public String listFexCotp(Model model) {
        model.addAttribute("userId", getCurrentUser().getId());
        return "fexCotp/listFexCotp";
    }

    /**
     * 待审批列表
     *
     * @return
     */
    @GetMapping("/listFexCotpApprove")
    public String listFexCotpApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "fexCotp/listFexCotpApprove";
    }


    /**
     * 选择交易对手组
     * @return
     */
    @GetMapping("/selectFexCotpGroup")
    public String selectFexCotpGroup(){
        return "fexCotp/selectFexCotpGroup";
    }

    /**
     * 选择交易对手/交易对手组
     * @return
     */
    @GetMapping("/selectFexCotp")
    public String selectFexCotp(){
        return "fexCotp/multipleSelectFexCotpGroup";
    }

    /**
     * 新增跳转
     * @param cotpLeve
     * @return
     */
    @GetMapping("/add")
    public String add(String cotpLeve,Model model){
        String url = "";
        if (ForeignConstant.FEX_COTP_LEVEL_0.equals(cotpLeve)) {
            url = "addFexCotpGroup";
        } else {
            url = "addFexCotp";
        }
//        model.addAttribute("custFieldS", fexCotpService.getCustfield());
        return "fexCotp/" + url;
    }

    /**
     * 编辑
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id,Model model){
        FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(id);
        String cotpLeve = fexCotpVO.getCotpLevel();
        String url = "";
        if (ForeignConstant.FEX_COTP_LEVEL_0.equals(cotpLeve)) {
            url = "editFexCotpGroup";
        } else {
            url = "editFexCotp";
        }
        model.addAttribute("fexCotpVO", fexCotpVO);
        model.addAttribute("custFieldS", fexCotpService.listCustInfoByBusId(id));
        return "fexCotp/" + url;
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id){
        FexCotp fexCotp = fexCotpService.get(id);
        fexCotp.setStatus(ForeignConstant.STATUE_DEL);
        fexCotpService.updateEntity(fexCotp);
        return success();
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/deleteChildById")
    @ResponseBody
    public AjaxJson deleteChildById(String id){
        FexCotp fexCotp = fexCotpService.get(id);
        fexCotp.setParentId(null);
        fexCotpService.updateEntity(fexCotp);
        return success();
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/appendChild")
    @ResponseBody
    public AjaxJson appendChild(String parentId, String ids){
        Map<String, Object> params = new HashMap<>();
        params.put("parentId", parentId);
        params.put("ids", Arrays.asList(ids.split(",")));
        fexCotpService.appendChild(params);
        return success();
    }

    /**
     * @return
     */
    @GetMapping("/view")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(id);
        String cotpLeve = fexCotpVO.getCotpLevel();
        String url = "";
        if (ForeignConstant.FEX_COTP_LEVEL_0.equals(cotpLeve)) {
            url = "viewFexCotpGroup";
        } else {
            url = "viewFexCotp";
        }
        model.addAttribute("showClosBtn", flag);
        model.addAttribute("fexCotpVO", fexCotpVO);
        model.addAttribute("custFieldS", fexCotpService.listCustInfoByBusId(id));
        if(EmptyUtils.isNotEmpty(fexCotpVO.getProductId())){
            FexProductVO fexProductVO = fexProductService.getByIdUnion(fexCotpVO.getProductId());
            model.addAttribute("proName", fexProductVO.getProName());
        }
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_COTP);
        return "fexCotp/" + url;
    }

    /**
     * 查看页面跳转
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model){
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "fexCotp/viewFexCotpApprove";
    }

    /**
     * 获取暂存或者打回数据
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput getfexCotpDatatable(@RequestBody DataTablesInput input){
        return fexCotpService.getDataTable(input, getCurrentUser());
    }

    /**
     * 获取待审批数据
     * @return
     */
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput getfexCotpDatatableApprove(@RequestBody DataTablesInput input){
        return fexCotpService.getDataTableApprove(input, getCurrentUser());
    }

    /**
     * 获取组内数据
     * @return
     */
    @PostMapping("/getChireddataTable")
    @ResponseBody
    public DataTablesOutput getChildDataTable(@RequestBody DataTablesInput input){
        return fexCotpService.getChildDataTable(input);
    }

    @PostMapping("/getChildDataTableByIds")
    @ResponseBody
    public DataTablesOutput getChildDataTableByIds(@RequestBody DataTablesInput input){
        return fexCotpService.getChildDataTableByIds(input);
    }

    /**
     * 获取所有组数据
     * @return
     */
    @PostMapping("/getCotpGroupDataTable")
    @ResponseBody
    public DataTablesOutput getCotpGroupDataTable(@RequestBody DataTablesInput input){
        return fexCotpService.getCotpGroupDataTable(input, getCurrentUser());
    }

    /**
     * 获取所无组数据
     * @return
     */
    @PostMapping("/getNullParentCotpDataTable")
    @ResponseBody
    public DataTablesOutput getNullParentCotpDataTable(@RequestBody DataTablesInput input){
        return fexCotpService.getNullParentCotpDataTable(input, getCurrentUser());
    }

    /**
     * 保存
     * @param fexCotpVO
     * @return
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxJson save(FexCotpVO fexCotpVO) {
        if(fexCotpService.checkName(fexCotpVO.getCotpName(), fexCotpVO.getId())){
            fexCotpService.save(fexCotpVO,getCurrentUser());
        }else{
            throw new ParameterException(ForeignMessageEnum.FEX_COTP_NAMEEXISTS);
        }
        return success();
    }

    /**
     * 作废/启用
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/tovoidOrEnable")
    @ResponseBody
    public AjaxJson tovoidOrEnable(String id, Integer status) {
        FexCotp fexCotp = fexCotpService.get(id);
        fexCotp.setStatus(status);
        fexCotpService.updateEntity(fexCotp);
        return success();
    }

    /**
     * 检查名称是否相同
     * @param name
     * @param id
     * @return
     */
    @PostMapping("/checkName")
    @ResponseBody
    public AjaxJson checkName(String name,@RequestParam(required = false,defaultValue = "") String id){
        AjaxJson json = new AjaxJson();
        json.setSuccess(fexCotpService.checkName(name, id));
        return json;
    }

    /**
     * 手机审批
     *
     * @return
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, Model model) {
        FexCotpVO fexCotpVO = fexCotpService.getFexcotpById(businessId);
        String cotpLeve = fexCotpVO.getCotpLevel();
        String url = "";
        if (ForeignConstant.FEX_COTP_LEVEL_0.equals(cotpLeve)) {
            model.addAttribute("ChildrenData", fexCotpService.listChildren(fexCotpVO.getId()));
            url = "mobileViewFexCotpGroup";
        } else {
            url = "mobileViewFexCotp";
        }
        model.addAttribute("fexCotpVO", fexCotpVO);
        model.addAttribute("custFieldS", fexCotpService.listCustInfoByBusId(businessId));
        return "fexCotp/" + url;
    }

    /**
     * 下拉列表获取权限内交易对手组数据
     * @return
     */
    @GetMapping("/listCotpGroup")
    @ResponseBody
    public List<FexCotpVO> listCotpGroup(){
        return fexCotpService.listCotpGroup(getCurrentUser());
    }

    /**
     * 获取权限内交易对手,也可通过交易对手类别来查询
     * @param cotpType
     * @return
     */
    @GetMapping("/listBankCotp")
    @ResponseBody
    public List<FexCotpVO> listBankCotp(@RequestParam(name = "cotpType",required = false) String cotpType){
        return fexCotpService.listBankCotp(cotpType);
    }

    /**
     *
     * @return
     */
    @GetMapping("/listCotp")
    @ResponseBody
    public List<FexCotpVO> listCotp(){
        return fexCotpService.listCotp();
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<FexCotpVO> list) {
        this.fexCotpService.saveImport(list, this.getCurrentUser());
        return success();
    }

    @GetMapping("/json")
    @ResponseBody
    public List<FexProductVO> json() {
        List<FexProductVO> list = fexProductService.listByProLevelAndTranType(ForeignConstant.PRO_LEVEL_1, ForeignConstant.TRAN_TYPE_4);
        return list;
    }
}
