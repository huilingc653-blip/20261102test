package com.bytter.system.web.controller;
import com.bytter.basic.service.BtSwiftInfoService;
import com.bytter.basic.vo.BtSwiftInfoVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 境外银行行号信息维护
 *
 * @author qinl
 * @date 2019-07-25
 */
@Controller
@RequestMapping("/basic/business/btswiftinfo")
public class BtSwiftInfoController extends BaseController {
    /**
     * 境外银行行号信息
     */
    @Autowired
    private BtSwiftInfoService btSwiftInfoService;
    /**
     * <p>
     * （页面）境外银行行号信息列表页
     * </P>
     *
     * @return listBtSwiftInfo
     */
    @GetMapping("/list")
    public String viewList() {
        return "basic/business/listBtSwiftInfo";
    }
    /**
     * <p>
     * （页面）境外银行行号信息查询页
     * </P>
     *
     * @return viewBtSwiftInfo
     */
    @GetMapping("/view/{id}")
    public String viewEntity(Model model, @PathVariable String id) {
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("btSwiftInfoVO", new BtSwiftInfoVO());
        }
        model.addAttribute("btSwiftInfoVO", btSwiftInfoService.getVOById(id));
        return "basic/business/viewBtSwiftInfo";
    }
    /**
     * <p>
     * （页面）境外银行行号信息编辑页
     * </P>
     *
     * @return addBtSwiftInfo
     */
    @GetMapping("/add")
    public String viewAdd(Model model, String id) {
        //新增页面
        if (StringUtils.isEmpty(id)) {
            model.addAttribute("btSwiftInfoVO", new BtSwiftInfoVO());
            return "basic/business/addBtSwiftInfo";
        }
        //更新页面
        model.addAttribute("btSwiftInfoVO", btSwiftInfoService.getVOById(id));
        return "basic/business/addBtSwiftInfo";
    }
    /**
     * <p>
     * （数据）境外银行信息列表数据源
     * </p>
     *
     * @param dataTablesInput [封装数据]
     * @return DataTablesOutput
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return btSwiftInfoService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * <p>
     * （操作）新增或保存
     * </p>
     *
     * @return AjaxJson
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BtSwiftInfoVO btSwiftInfoVO) {
        //id是否存在
        if (StringUtils.isEmpty(btSwiftInfoVO.getId())) {
            //执行保存
            btSwiftInfoService.save(btSwiftInfoVO, getUserLoginName());
        } else {
            //执行更新
            btSwiftInfoService.update(btSwiftInfoVO, getUserLoginName());
        }
        return success();
    }
    /**
     * <p>
     * （操作）根据ID删除
     * </p>
     *
     * @return AjaxJson
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable String id) {
        btSwiftInfoService.delete(id);
        return success();
    }
    /**
     * <p>
     * （组件）境外银行信息选择页面
     * </p>
     *
     * @return selectBtSwiftInfo
     */
    @GetMapping("/select")
    public String select() {
        return "basic/business/selectBtSwiftInfo";
    }
    /**
     * <p>
     * （组件）导入excel数据
     * </p>
     *
     * @param list excel数据
     * @return AjaxJson
     */
    @PostMapping("/import")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<BtSwiftInfoVO> list) {
        this.btSwiftInfoService.saveImport(list, this.getCurrentUser());
        return success();
    }
    /**
     * <p>
     * （组件）境外银行信息选择页面
     * </p>
     *
     * @return selectBtSwiftInfo
     */
    @GetMapping("/selectOverseasBankInfo")
    public String selectOverseasBankInfo() {
        return "basic/business/selectOverseasBankInfo";
    }
    /**
     * <p>
     * （数据）境外银行信息列表数据源(选择组件，无需权限控制)
     * </p>
     *
     * @param dataTablesInput [封装数据]
     * @return DataTablesOutput
     */
    @PostMapping("/dataTableSelect")
    @ResponseBody
    public DataTablesOutput dataTableSelect(@RequestBody DataTablesInput dataTablesInput) {
        return btSwiftInfoService.getDataTablesUnion(dataTablesInput);
    }


    /**
     * <p>
     * （组件）境外银行信息选择页面
     * </p>
     *
     * @return selectBtSwiftInfo
     */
    @GetMapping("/selectOuterBankInfo")
    public String selectOuterBankInfo() {
        return "basic/business/selectOuterBankInfo";
    }
}
