package com.bytter.biz.erp.controller;

import com.bytter.core.base.BaseController;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhoul on 2017/12/5.
 */
@Controller
@RequestMapping("/erp/uration")
public class RatUrationServiceController extends BaseController {

    @DubboReference(check = false)
    private IRatUrationService iRatUrationService;


    /**
     * 授信冻结占用(业务数据提交待审批)
     * @param ratdto
     */
    @PostMapping ("/use")
    @ResponseBody
    public void urationUse(@RequestBody RatUrationDTO ratdto) {
            iRatUrationService.addUseRation(ratdto);

    }


    /**
     *  授信打回(审批打回)
     * @param ratdto
     */
    @PostMapping ("/release")
    @ResponseBody
    public void urtaionRelease(@RequestBody RatUrationDTO ratdto){
        iRatUrationService.backUseRation(ratdto);
    }







}
