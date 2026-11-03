package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.guarantee.domain.service.ConTypeService;
import com.bytter.guarantee.domain.vo.ConTypeVO;
import com.bytter.guarantee.enums.ConMessageEnum;
import com.bytter.guarantee.enums.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 
 * @Title: 合同类型控制类
 * @Package com.bytter.biz.contract.controller
 * @Description: TODO
 * @author: Jinyong.cao
 * @date: 2019-11-4 下午4:19:43
 * @version V1.0
 */
@Controller
@RequestMapping("/contract/contractType")
public class ConTypeController extends BaseController {
    @Autowired
    private ConTypeService conTypeService;
    /**
     * 
     * @Title: list @Description: TODO(合同类型管理入口) @param @param model @param @return 参数 @return String 返回类型 @throws
     */
    @SuppressWarnings("deprecation")
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("isCommon", PermissionEnum.all);
        return "contract/contractTypeManage";
    }
    /**
     * 
     * @Title: dataTable @Description: TODO(查询数据) @param @param dataTablesInput @param @return 参数 @return
     *         DataTablesOutput 返回类型 @throws
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable.json")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return conTypeService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 
     * @Title: add @Description: TODO(Jump新增合同类型) @param @param model @param @return 参数 @return String 返回类型 @throws
     */
    @SuppressWarnings("deprecation")
    @GetMapping("/add")
    public String add(String id, String power, Model model) {
        // 传过来的权限
        model.addAttribute("currPower", power);
        // 当前用户拥有的权限
        model.addAttribute("isCommon", PermissionEnum.all);
        return "contract/addContractType";
    }
    /**
     * 
     * @Title: save @Description: TODO(新增方法) @param @param externalCorpVO @param @return 参数 @return AjaxJson
     *         返回类型 @throws
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ConTypeVO conTypeVO) {
        if (conTypeService.queryConTractIsExist(conTypeVO.getTypeCode(), null))
            throw new BusinessException(ConMessageEnum.CONTRACT_CODE_ISEXIST);
        conTypeService.save(conTypeVO, getCurrentUser());
        return success();
    }
    /**
     * 
     * @Title: update @Description: TODO(修改方法) @param @param externalCorpVO @param @return 参数 @return AjaxJson
     *         返回类型 @throws
     */
    @PostMapping("/updateConType")
    @ResponseBody
    public AjaxJson updateConType(ConTypeVO conTypeVO) {
        if (conTypeService.queryConTractIsExist(conTypeVO.getTypeCode(), conTypeVO.getId()))
            throw new BusinessException(ConMessageEnum.CONTRACT_CODE_ISEXIST);
        conTypeService.updateContractType(conTypeVO, getCurrentUser());
        return success();
    }
    /**
     * 
     * @Title: 编辑 @Description: TODO(Jump编辑合同类型) @param @param model @param @return 参数 @return String 返回类型 @throws
     */
    @SuppressWarnings("deprecation")
    @GetMapping("/update")
    public String update(String id, String parms, Model model) {
        model.addAttribute("isCommon", PermissionEnum.all);
        model.addAttribute("currPower", parms);
        model.addAttribute("conTypeInfo", conTypeService.getConTypeView(id));
        return "contract/updateContractType";
    }
    /**
     * 
     * @Title: 查看合同类型详细信息 @Description: TODO(查看合同详细信息) @param @param model @param @return 参数 @return String 返回类型 @throws
     */
    @GetMapping("/view")
    public String view(String id, String corpId, Model model, String actionType) {
        model.addAttribute("conTypeInfo", conTypeService.getConTypeView(id));
        return "contract/viewContractType";
    }
    /**
     * 获取维护的有效合同类型
     * 
     * @return
     */
    @GetMapping("/getAllConType")
    @ResponseBody
    public List<ConTypeVO> getAllConType(ConTypeVO params) {
        return conTypeService.getAllConType(params, this.getCurrentUser());
    }
    /**
     * 内部业务系统选择合同类型页面
     * 
     * @param params
     *            传递查询参数
     * @param model
     * @return
     */
    @GetMapping("/conTypeSelect")
    public String conView(ConTypeVO params, Model model) {
        model.addAttribute("params", params);
        return "contract/conTypeSelect";
    }
    /**
     * 
     * @Title: delete @Description: TODO(删除) @param @param externalIds @param @return 参数 @return AjaxJson 返回类型 @throws
     */
    @PostMapping("/del")
    @ResponseBody
    public AjaxJson delete(@RequestParam("externalIds[]") List<String> externalIds) {
        // 判断合同类型是否被其它引用
        if (null != externalIds && conTypeService.vnGtOccupy(externalIds)) {
            throw new BusinessException(ConMessageEnum.CONSTACT_MESSAGE_ERROR_OCCUPY);
        }
        conTypeService.delete(externalIds);
        return success();
    }
    @PostMapping("/conFiled.json")
    @ResponseBody
    public Object conFiled(String id) {
        return conTypeService.getTypeFieldByTypeId(id);
    }
}
