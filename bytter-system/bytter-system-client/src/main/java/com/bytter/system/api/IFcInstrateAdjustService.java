package com.bytter.system.api;
import com.bytter.system.api.bean.AccInstrateBean;
import com.bytter.system.api.bean.InstrateAdjustBean;
import com.bytter.system.api.dto.FcInstrateAdjustDTO;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
public interface IFcInstrateAdjustService {
    /**
     * 保存调整记录
     *
     * @param fcInstrateAdjustDTO
     */
    void saveFcInstrateAdjust(FcInstrateAdjustDTO fcInstrateAdjustDTO);
    /**
     * 通过科目利率表Id 查询对象列表
     *
     * @param instrateId
     * @return
     */
    List<FcInstrateAdjustDTO> getDTOByInstrateId(String instrateId);
    Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc);
    List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds, String account);
}
