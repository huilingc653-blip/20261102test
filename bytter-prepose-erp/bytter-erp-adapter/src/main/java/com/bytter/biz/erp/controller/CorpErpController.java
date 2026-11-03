package com.bytter.biz.erp.controller;


import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/erp/corp")
public class CorpErpController {

    @DubboReference(check = false)
    private ICorpService corpService;

    @PostMapping("/save")
    public void save(@RequestBody List<CorpDTO> corpList) {
        corpService.save(corpList);
    }

    @PostMapping("/update")
    public void update(@RequestBody List<CorpDTO> corpList) {
        corpService.update(corpList);
    }

    @PostMapping("/delete")
    public void delete(@RequestBody List<CorpDTO> corpList) {
        corpService.delete(corpList);
    }

    @PostMapping("/getCorpByCode")
    public CorpDTO getCorpByCode(@RequestBody CorpDTO corpDTO){
        return corpService.getCorpByCode(corpDTO.getCode());
    }

}
