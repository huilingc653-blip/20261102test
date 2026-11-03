package com.bytter.biz.netbank.service.impl;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bytter.biz.netbank.service.BisFailReasonService;
import com.bytter.biz.netbank.vo.BisFailReasonVO;
import com.bytter.biz.payment.api.dto.BisFailReasonDTO;
import com.bytter.biz.payment.api.service.IBisFailReasonService;
import com.bytter.core.constant.CommonConstant;
/**
 * @author huangx
 * @Description 交易失败原因接口实现类
 * @date 2019.5.27
 */
@Service("bisFailReasonServiceImpl")
@Transactional
@DubboService
public class BisFailReasonServiceImpl implements IBisFailReasonService {
    @Autowired
    private BisFailReasonService bisFailReasonService;
    @Override
    public BisFailReasonDTO getFailReasonByReturnMsg(String bifCode, String returnMsg) {
        List<BisFailReasonVO> bisFailReasonList = this.bisFailReasonService.listBisFailReasonByBifCode(bifCode);
        BisFailReasonDTO bisFailReasonDTO = new BisFailReasonDTO();
        for(BisFailReasonVO bisFailReason:bisFailReasonList){
            String[] keyWords = bisFailReason.getKeyWords().split(CommonConstant.SEMI_SPLIT);
            for(String keyWord : keyWords) {
                if(returnMsg.indexOf(keyWord) != -1){
                    BeanUtils.copyProperties(bisFailReason, bisFailReasonDTO);
                    break;
                }
            }
        }
        return bisFailReasonDTO;
    }
}
