package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexRateInfo;
import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.service.FexRateInfoService;
import com.bytter.biz.foreign.service.FexRateTypeService;
import com.bytter.biz.foreign.vo.FexRateTypeVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/foreign/fexratetype")
public class FexRateTypeController extends BaseController {
    @Autowired
    private FexRateTypeService fexRateTypeService;
    @Autowired
    private FexRateInfoService fexRateInfoService;
    /**
     * 列表页
     *
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXRATETYPE_APPLY)
    @GetMapping("/list")
    public String list() {
        return "foreign/listFexRateType";
    }
    /**
     * 新增页面
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXRATETYPE_APPLY)
    @GetMapping("/add")
    public String add() {
        return "foreign/addFexRateType";
    }
    /**
     * 外币汇款申请表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXRATETYPE_APPLY)
    @GetMapping("/update")
    public String update(FexRateTypeVO fexRateTypeVO, Model model) {
        FexRateType fexRateType = fexRateTypeService.get(fexRateTypeVO.getId());
        model.addAttribute("fexRateType",fexRateType);
        return "foreign/updateFexRateType";
    }

    /**
     * 列表数据接口
     *
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexRateTypeService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexRateTypeVO fexRateTypeVO) {
        fexRateTypeService.saveCheck(fexRateTypeVO,new FexRateTypeVO());
        fexRateTypeService.save(fexRateTypeVO, this.getCurrentUser());
        return success();
    }

    /**
     * 修改
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexRateTypeVO fexRateTypeVO,String currentTypeName,String currentBifCode,String currentRateInterfaceType) {
        FexRateTypeVO vo2 = new FexRateTypeVO();
        vo2.setRateInterfaceType(currentRateInterfaceType);
        vo2.setBifCode(currentBifCode);
        vo2.setTypeName(currentTypeName);
        fexRateTypeService.saveCheck(fexRateTypeVO,vo2);
        fexRateTypeService.update(fexRateTypeVO, this.getCurrentUser());
        return success();
    }

    /**
     * 删除
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        //校验牌价类型是否已使用
        List<FexRateInfo> list = fexRateInfoService.listByRateTypeId(id);
        if(EmptyUtils.isNotEmpty(list)){
            throw new ParameterException(ForeignMessageEnum.FEX_RATETYPE_MESSAGE_RATETYPE_DEL);
        }
        fexRateTypeService.deleteData(id);
        return success();
    }

    /**
     * 详情页
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexRateTypeVO query = new FexRateTypeVO();
        query.setId(id);
        FexRateTypeVO fexRateType = fexRateTypeService.getByVO(query);
        model.addAttribute("fexRateType",fexRateType);
        return "foreign/viewFexRateType";
    }

    /**
     * 获取有效的外汇牌价类型
     *
     * @return
     */
    @RequestMapping("/listFexRateType/{rateInterfaceType}")
    @ResponseBody
    public List<FexRateTypeVO> listFexRateType(@PathVariable("rateInterfaceType") String rateInterfaceType) {
        List<FexRateTypeVO> list = fexRateTypeService.listByRateInterfaceType(rateInterfaceType);
        return list;
    }

    /**
     * 获取所有有效的外汇牌价类型
     *
     * @return
     */
    @GetMapping("/listFexRateTypeAll")
    @ResponseBody
    public List<FexRateTypeVO> listFexRateTypeAll() {
        List<FexRateTypeVO> list = fexRateTypeService.listFexRateTypeAll();
        return list;
    }

    /**
     * 校验名称是否重复
     *
     * @param typeName 名称
     * @return
     */
    @PostMapping("/checkTypeName")
    @ResponseBody
    public AjaxJson checkTypeName(String typeName) {
        FexRateTypeVO query = new FexRateTypeVO();
        query.setTypeName(typeName);
        FexRateTypeVO byTypeName = fexRateTypeService.getByVO(query);
        if (byTypeName != null) {
            throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_NAME_UNIQUE, typeName);
        }
        return success();
    }
}
