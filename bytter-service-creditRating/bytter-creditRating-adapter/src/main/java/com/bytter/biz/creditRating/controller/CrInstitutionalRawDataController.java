package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrInstitutionalRawDataService;
import com.bytter.biz.creditRating.vo.CrInstitutionalRawDataVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/credit/institutionalRawData")
public class CrInstitutionalRawDataController extends BaseController {

    @Autowired
    CrInstitutionalRawDataService crInstitutionalRawDataService;

    /**
     *
     * @param types 机构类别, 通过英文逗号(,)进行拼接；如果 为 空 则返回全部记录
     * @return
     */
    @GetMapping("/queryByTypes")
    @ResponseBody
    public List<CrInstitutionalRawDataVo> queryByTypes(String types){
        List<CrInstitutionalRawDataVo> result =  crInstitutionalRawDataService.queryByTypes(types);
        return result;
    }
}
