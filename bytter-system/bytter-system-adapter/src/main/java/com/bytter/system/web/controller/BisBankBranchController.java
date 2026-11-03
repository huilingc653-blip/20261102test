package com.bytter.system.web.controller;
import com.bytter.basic.entity.BisBankBranch;
import com.bytter.basic.service.BisBankBranchService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 办理机构表
 * @date 2022-12-12 19:06:25
 */
@Controller
@RequestMapping("/basic/business/bisBankBranch")
public class BisBankBranchController extends BaseController {
    @Autowired
    private BisBankBranchService bisBankBranchService;
    /**
     * 获取有效的地区信息
     *
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public List<BisBankBranch> listAll() {
        List<BisBankBranch> list = bisBankBranchService.listValid();
        return list;
    }
}
