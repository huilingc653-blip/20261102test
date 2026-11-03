package com.bytter.basic.service.impl;
import com.bytter.basic.entity.FcInstrateAdjust;
import com.bytter.basic.service.FcInstrateAdjustService;
import com.bytter.basic.vo.FcInstrateAdjustVO;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.bean.AccInstrateBean;
import com.bytter.system.api.bean.InstrateAdjustBean;
import com.bytter.system.api.dto.FcInstrateAdjustDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
@Service("fcInstrateAdjustServiceI")
@DubboService
@Transactional
public class FcInstrateAdjustServiceImpl implements IFcInstrateAdjustService {
    @Autowired
    private FcInstrateAdjustService fcInstrateAdjustService;
    /**
     * 保存调整记录
     *
     * @param fcInstrateAdjustDTO
     */
    @Override
    public void saveFcInstrateAdjust(FcInstrateAdjustDTO fcInstrateAdjustDTO) {
        FcInstrateAdjust adjust = new FcInstrateAdjust();
        BeanUtils.copyProperties(fcInstrateAdjustDTO, adjust);
        fcInstrateAdjustService.saveOrUpdate(adjust);
    }
    @Override
    public List<FcInstrateAdjustDTO> getDTOByInstrateId(String instrateId) {
        List<FcInstrateAdjustVO> list = fcInstrateAdjustService.getVOByInstrateId(instrateId);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<FcInstrateAdjustDTO> dto = BeanUtils.copyListProperties(list, FcInstrateAdjustDTO.class);
        return dto;
    }
    @Override
    public Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc) {
        return fcInstrateAdjustService.queryInterestAdjustsByAcc(blnctrAcc);
    }
    @Override
    public List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds, String account) {
        return fcInstrateAdjustService.queryInstRateAdjustInfos(netCode, accTypeCode, endInstDate, corpIds, account);
    }
}
