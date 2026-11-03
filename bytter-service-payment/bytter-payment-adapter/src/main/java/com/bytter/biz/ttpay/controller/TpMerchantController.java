package com.bytter.biz.ttpay.controller;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.bytter.biz.ttpay.vo.TpBussinessTextVO;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.bytter.biz.ttpay.service.TpMerchantService;
import com.bytter.biz.ttpay.vo.TpMerchantVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
/**
 * @author 郑刚
 * @version 1.0
 * @Title: Controller
 * @Description: 商户信息对应Controller
 * @date 2019-09-26 15:43:05
 */
@Controller
@RequestMapping("/ttpay/merchant")
public class TpMerchantController extends BaseController {
    @Autowired
    private TpMerchantService tpMerchantService;
    /**
     * 商户维护列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    public String list(TpMerchantVO queryVO, Model model) {
        model.addAttribute("queryVO", queryVO);
        model.addAttribute("listInfo", tpMerchantService.getTpMerchantVOTreeList(queryVO, this.getCurrentUser()));
        return "ttpay/listTpMerchant";
    }
    /**
     * 获取树形结构商户信息
     *
     * @param queryVO
     * @return
     */
    @GetMapping("/treeData")
    @ResponseBody
    public Object treeData(TpMerchantVO queryVO) {
        return tpMerchantService.getTpMerchantVOTreeMapList(queryVO, this.getCurrentUser());
    }
//
//	/**
//	 * 商户维护列表数据获取
//	 * @param input
//	 * @return 列表数据
//	 */
//	@SuppressWarnings("rawtypes")
//	//////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
//	@LogMessage(content = "查询商户信息维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
//	@PostMapping("/datatable")
//	@ResponseBody
//	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
//		return tpMerchantService.getDataTablesUnion(input);
//	}
    /**
     * @param model
     * @return 页面地址
     * @Title: 商户维护新增页面
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    @GetMapping("/add")
    public String add(Model model) {
        return "ttpay/addTpMerchant";
    }
    /**
     * @param id
     * @param model
     * @return 页面地址
     * @Title: 商户维护修改页面
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    @GetMapping("/update")
    public String update(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            TpMerchantVO info = tpMerchantService.getTpMerchantVO(id);
            model.addAttribute("info", info);
        }
        return "ttpay/updateTpMerchant";
    }
    /**
     * @param info
     * @return 处理结果
     * @Title: 商户维护新增或修改操作
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    @LogMessage(content = "商户信息新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(TpMerchantVO info, HttpServletRequest request) {
        tpMerchantService.saveOrUpdate(info, this.getCurrentUser());
        return success();
    }
    /**
     * 判断相同缴款方式下，是否merchantNo商户号是否有相同的维护
     *
     * @param merchantNo 商户号
     * @param payType    支付方式
     * @param mchId      商户ID
     * @return
     */
    @PostMapping("/codeCheck")
    @ResponseBody
    public AjaxJson merchantNoCheck(String merchantNo, String payType, String mchId) {
        tpMerchantService.merchantNoCheck(merchantNo, payType, mchId);
        return success();
    }
    /**
     * @param id 需要删除的ID
     * @return AjaxJson    返回类型
     * @throws
     * @Title: 通过ID删除商户
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    @LogMessage(content = "商户信息删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id) {
        tpMerchantService.deleteMerchant(id, this.getCurrentUser());
        return success();
    }
    /**
     * @param id       需要修改的ID
     * @param isOnline 在线离线标记 1在线0离线
     * @return AjaxJson    返回类型
     * @throws
     * @Title: 修改在线离线标记
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_MERCHANT_OP)
    @LogMessage(content = "修改在线离线标记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/updateIsOnline")
    @ResponseBody
    public AjaxJson updateIsOnline(String id, String isOnline) {
        tpMerchantService.updateIsOnline(id, isOnline, this.getCurrentUser());
        return success();
    }
    /**
     * 导入
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<TpMerchantVO> list) {
        tpMerchantService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * 根据单位ID，支付方式过滤商户信息
     *
     * @return 商户信息
     */
    @GetMapping("/getByList")
    @ResponseBody
    public List<TpMerchantVO> getByList(String corpId, String payType) {
        List<TpMerchantVO> tpMerchantVOList = new ArrayList<TpMerchantVO>();
        if (StringUtils.isEmpty(corpId)) {
            return tpMerchantVOList;
        }
        tpMerchantVOList = tpMerchantService.getByList(corpId, payType);
        tpMerchantVOList = tpMerchantService.getNewMerchantNo(tpMerchantVOList);
        return tpMerchantVOList;
    }
}
