package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexLinkmanInfo;
import com.bytter.biz.foreign.service.FexLinkmanInfoService;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款联系人信息表
 * @date 2019-07-15 15:20:33
 */
@Controller
@RequestMapping("/foreign/fexlinkmaninfo")
public class FexLinkmanInfoController extends BaseController {
    @Autowired
    private FexLinkmanInfoService fexLinkmanInfoService;

    /**
     * 根据单位获取汇款联系人信息
     *
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/listByCorpId")
    @ResponseBody
    public List<FexLinkmanInfo> listByCorpId(String corpId) {
        List<FexLinkmanInfo> list = null;
        if(StringUtils.isBlank(corpId)){
            list = new ArrayList<>();
        }else {
            list = fexLinkmanInfoService.listByCorpId(corpId);
        }
        return list;
    }
}
