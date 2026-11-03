package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrdDataSourceCategoryService;
import com.bytter.biz.creditRating.vo.CrdDataSourceCategoryVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/credit/dataSourceCategory")
public class CrdDataSourceCategoryController extends BaseController {

    @Autowired
    private CrdDataSourceCategoryService dataSourceCategoryService;

    @GetMapping(value = "/dataTable")
    @ResponseBody
    public List<CrdDataSourceCategoryVo> dataTable(){
        return dataSourceCategoryService.dataTable();
    }
}
