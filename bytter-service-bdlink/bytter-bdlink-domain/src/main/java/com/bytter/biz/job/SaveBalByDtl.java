package com.bytter.biz.job;

import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.service.BisAccBalService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SaveBalByDtl implements ITaskService {
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private BisAccBalService bisAccBalService;
    @DubboReference(check = false)
    private IUserService userService;

    @XxlJob("SaveBalByDtl")
    @Override
    public void run(String arguments) {
        //获取参数
        String bankAcc = XxlJobHelper.getJobParam();
        String curId = "4028802e5c346968015c346c18850008";
        XxlJobHelper.log("参数："+bankAcc);
        if(EmptyUtils.isNotEmpty(bankAcc)) {
            String[] bankAccStr = bankAcc.split(",");
            for(int i=0;i<bankAccStr.length;i++ ){
                BtBankAccDTO bankAccDTO = btBankAccService.getBankAccByBankAccAndCurId(bankAccStr[i],curId);
                if(!EmptyUtils.isEmpty(bankAccDTO)){
                    Map<String, Object> map = new HashMap<>();
                    map.put("accCurId",bankAccDTO.getCurId());
                    map.put("bankAcc",bankAccDTO.getBankAcc());
                    map.put("accCorpId",bankAccDTO.getCorpId());
                    map.put("bifCode",bankAccDTO.getBifCode());
                    BisAccBalDTO bisAccBalVO = new BisAccBalDTO();
                    bisAccBalVO.setBeginDate(DateUtils.parse("2024-01-01","yyyy-MM-dd"));
                    bisAccBalVO.setEndDate(DateUtils.getDateYMD());
                    bisAccBalVO.setGenerateBalance(1);
                    UserDTO userDTO = userService.getByLoginName("admin");
                    ICurrentUser user = new ShiroUser(userDTO.getId(),userDTO.getLoginName(),userDTO.getUsername());
                    bisAccBalService.saveAccBalByDtlTransaction(map,bisAccBalVO,user);
                }
            }

        }
    }
}
