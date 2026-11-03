package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexProduct;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 外汇产品表Controller
 * @author huangx
 * @date 2020-02-16 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexproduct")
public class FexProductController extends BaseController {

    @Autowired
    private FexProductService fexProductService;

    /**
     * 外汇产品表列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXPRODUCT_APPLY)
    @LogMessage(content = "查询内部理财表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{tranType}")
    public String list(@PathVariable("tranType") String tranType, Model model) {
        model.addAttribute("tranType", tranType);
        return "foreign/listFexProduct" + this.getForwardByTranType(tranType);
    }

    /**
     * 根据交易类型返回不同的跳转页面
     * @param tranType
     * @return
     */
    public String getForwardByTranType(String tranType){
        /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
        /** 7-外汇即期交易、8-外汇远期交易、9-外汇掉期交易*/
        String forward = "";
        if(ForeignConstant.TRAN_TYPE_1.equals(tranType) || ForeignConstant.TRAN_TYPE_7.equals(tranType)
                    ||ForeignConstant.TRAN_TYPE_8.equals(tranType) ||ForeignConstant.TRAN_TYPE_9.equals(tranType)){
            //外汇买卖产品
            forward = "Foreign";
        }else if(ForeignConstant.TRAN_TYPE_2.equals(tranType)){
            //外汇期权产品
            forward = "Option";
        }else if(ForeignConstant.TRAN_TYPE_3.equals(tranType)){
            //资金调拨产品
            forward = "Allocation";
        }else if(ForeignConstant.TRAN_TYPE_4.equals(tranType)){
            //资金收付产品
            forward = "Pay";
        }else if(ForeignConstant.TRAN_TYPE_5.equals(tranType)){
            //暂时未规划
        }else if(ForeignConstant.TRAN_TYPE_6.equals(tranType)){
            //暂时未规划
        }else{
            //交易组产品
            forward = "Trade";
        }
        return forward;
    }

    /**
     * 外汇产品表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexProductService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外汇产品表新增页面跳转（产品组）
     *
     * @return
     */
    @GetMapping("/addProductGroup/{tranType}")
    public String addProductGroup(@PathVariable("tranType") String tranType, FexProduct fexProduct, Model model) {
        if (StringUtils.isNotEmpty(fexProduct.getId())) {
            fexProduct = fexProductService.get(fexProduct.getId());
            model.addAttribute("fexProductPage", fexProduct);
        }
        model.addAttribute("tranType", tranType);
        return "foreign/addFexProductGroup";
    }

    /**
     * 外汇产品表新增页面跳转（产品）
     *
     * @return
     */
    @GetMapping("/add/{tranType}")
    public String add(@PathVariable("tranType") String tranType, FexProduct fexProduct, Model model) {
        if (StringUtils.isNotEmpty(fexProduct.getId())) {
            fexProduct = fexProductService.get(fexProduct.getId());
            model.addAttribute("fexProductPage", fexProduct);
        }
        model.addAttribute("tranType", tranType);
        return "foreign/addFexProduct" + this.getForwardByTranType(tranType);
    }

    /**
     * 外汇产品表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public String update(FexProductVO fexProduct, Model model) {
        if (StringUtils.isNotEmpty(fexProduct.getId())) {
            fexProduct = fexProductService.getByIdUnion(fexProduct.getId());
            model.addAttribute("fexProductPage", fexProduct);
        }
        if(ForeignConstant.PRO_LEVEL_0.equals(fexProduct.getProLevel())){
            List<FexProduct> listData = fexProductService.listByProId(fexProduct.getId());
            String billId = "";
            for(FexProduct product:listData){
                billId = billId+product.getId()+ CommonConstant.COMMA_SPLIT;
            }
            fexProduct.setBillId(billId);
            return "foreign/updateFexProductGroup";
        }else {
            return "foreign/updateFexProduct" + this.getForwardByTranType(fexProduct.getTranType());
        }
    }

    /**
     * 外汇产品表详情页
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXPRODUCT_APPLY)
    @LogMessage(content = "查询内部理财表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexProductVO fexProduct = fexProductService.getByIdUnion(id);
        if(ForeignConstant.PRO_LEVEL_0.equals(fexProduct.getProLevel())){
            List<FexProduct> listData = fexProductService.listByProId(fexProduct.getId());
            String billId = "";
            for(FexProduct product:listData){
                billId = billId+product.getId()+ CommonConstant.COMMA_SPLIT;
            }
            fexProduct.setBillId(billId);
        }
        model.addAttribute("fexProductPage", fexProduct);
        String page ="";
        if(ForeignConstant.PRO_LEVEL_1.equals(fexProduct.getProLevel()) && (ForeignConstant.TRAN_TYPE_7.equals(fexProduct.getTranType())
                || ForeignConstant.TRAN_TYPE_8.equals(fexProduct.getTranType())
                || ForeignConstant.TRAN_TYPE_9.equals(fexProduct.getTranType()))){
            page = "foreign/viewFexProductForeign";
        }else if(ForeignConstant.PRO_LEVEL_1.equals(fexProduct.getProLevel()) &&ForeignConstant.TRAN_TYPE_2.equals(fexProduct.getTranType())){
            page = "foreign/viewFexProductOption";
        }else{
            page = "foreign/viewFexProduct";
        }

        return page;
    }

    /**
     * 获取有效的外汇产品组
     *
     * @return
     */
    @GetMapping("/listProductGroup/{tranType}")
    @ResponseBody
    public List<FexProductVO> listProductGroup(@PathVariable("tranType") String tranType) {
        List<FexProductVO> list = fexProductService.listByProLevelAndTranType(ForeignConstant.PRO_LEVEL_0, tranType);
        return list;
    }

    /**
     * 获取有效的外汇产品
     *
     * @return
     */
    @GetMapping("/listProduct/{tranType}")
    @ResponseBody
    public List<FexProductVO> listProduct(@PathVariable("tranType") String tranType) {
        List<FexProductVO> list = fexProductService.listByProLevelAndTranType(ForeignConstant.PRO_LEVEL_1, tranType);
        return list;
    }

    /**
     * 获取有效的外汇产品及产品组
     *
     * @return
     */
    @GetMapping("/listProductAndGroup")
    @ResponseBody
    public List<FexProductVO> listProductAndGroup(@RequestParam(required = false) String tranType) {
        List<FexProductVO> list = fexProductService.listByProLevelAndTranType(null, tranType);
        return list;
    }

    /**
     * 根据交易类型获取产品信息
     * @param tranType
     * @return
     */
    @RequestMapping(params = "getProductByTranType")
    @ResponseBody
    public AjaxJson getProductByTranType(String tranType){
        AjaxJson j=new AjaxJson();
        j.setSuccess(true);
        j.setObj(fexProductService.listByProLevelAndTranType(ForeignConstant.PRO_LEVEL_1, tranType));
        return j;
    }

    /**
     * 添加外汇产品信息
     * @param fexProduct
     * @return
     */
    @LogMessage(content = "添加外汇产品表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexProductVO fexProduct){
        fexProductService.dataCheck(fexProduct);
        fexProductService.saveFexProduct(fexProduct, this.getCurrentUser());
        return success();
    }

    /**
     * 修改外汇产品表
     * @param fexProduct
     * @return
     */
    @LogMessage(content = "修改外汇产品表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexProductVO fexProduct){
        fexProductService.dataCheck(fexProduct);
        fexProductService.checkGroupChangeValid(fexProduct);
        fexProductService.updateFexProduct(fexProduct, this.getCurrentUser());
        return  success();
    }

    /**
     * 删除外汇产品表
     * @param id
     * @return
     */
    @LogMessage(content = "删除外汇产品表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FexProductVO fexProduct = fexProductService.getByIdUnion(id);
        //产品组校验下面是否有产品，有产品则不允许删除
        if(ForeignConstant.PRO_LEVEL_0.equals(fexProduct.getProLevel())){
            List<FexProduct> list = fexProductService.listByProId(id);
            if(!EmptyUtils.isEmptyList(list)){
                throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_PRODUCTGROUP_DEL);
            }
        }
        //产品数据关联校验
        fexProductService.checkAssociated(id);

        fexProductService.delFexProduct(id, this.getCurrentUser());
        return success();
    }

    /**
     * 产品选择
     * @return
     */
    @GetMapping("/selectFexProduct")
    public String selectFexProduct(FexProductVO fexProduct, Model model) {
        model.addAttribute("params", fexProduct);
        return "foreign/selectFexProduct";
    }

    /**
     * 票据详细信息查看
     * @return
     */
    @GetMapping("/selectFexProductView")
    public String selectFexProductView(FexProductVO fexProduct, Model model) {
        List<FexProductVO> list = fexProductService.selectFexProductView(fexProduct);
        model.addAttribute("list", list);
        return "foreign/selectFexProductView";
    }

    /**
     *获取有效的外汇产品
     *
     * @return
     */
    @GetMapping("/listFexProduct/{id}")
    @ResponseBody
    public FexProductVO listTranGroup(@PathVariable String id) {
        return fexProductService.getByIdUnion(id);
    }

    /**
     * 根据外汇交易类型获取产品信息
     * @param fexTranType 外汇交易类型 0-即期、1-远期
     * @return
     */
    @RequestMapping("getProductByType")
    @ResponseBody
    public List<FexProductVO> getProductByType(String fexTranType){
        return fexProductService.listByProLevelAndFexTranType(ForeignConstant.PRO_LEVEL_1, fexTranType);
    }

    /**
     * 资金收付交易初始化产品
     *
     * @return
     */
    @RequestMapping("/getInitProductDate")
    @ResponseBody
    public List<FexProductVO> getInitProductDate(){
         return fexProductService.getInitProductDate();
    }


    /**
     * 获取产品下拉框列表
     * @param tranType
     * @param fexTranType
     * @return
     */
    @ResponseBody
    @PostMapping("/json")
    public List<FexProductVO> json(String tranType,String fexTranType) {
        List<FexProductVO> list = fexProductService.listByProAndFexTranType(ForeignConstant.PRO_LEVEL_1, tranType,fexTranType);
        return list;
    }
}
