package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexTranInfo;
import com.bytter.biz.foreign.service.FexTranInfoService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 交易编码表
 * @date 2019-07-15 15:20:33
 */
@Controller
@RequestMapping("/foreign/fextraninfo")
public class FexTranInfoController extends BaseController {
    @Autowired
    private FexTranInfoService fexTranInfoService;

    /**
     * 获取交易编码信息
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/list")
    @ResponseBody
    public List<FexTranInfo> listAll() {
        return fexTranInfoService.listOrderByTranCode();
    }
}
