package com.bytter.biz.finacial.interest.service.proccessor;
import java.util.Date;
import java.util.List;
import com.bytter.biz.finacial.interest.bean.AccInstrateBean;
import com.bytter.biz.finacial.interest.bean.AccInstrateZoneBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcPredrawDtl;
import com.bytter.core.base.entity.ICurrentUser;

/**
 * <br>
 * <b>类功能：</b>利息计息适配器<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月11日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public interface IInterestProvider {
    /**
     * <b>功能介绍：</b>查询计息账户的积数余额<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    List<FcAccuBalancef> queryAccuBalanceInfos(String netCode, String accTypeCode, Date endInstDate);
    /**
     * <b>功能介绍：</b>查询指定计息账户的积数余额<br>
     * <b>日期：</b>2019年5月21日<br>
     * <b>作者：</b>liyh<br>
     *
     * @version 1.0
     */
    public List<FcAccuBalancef> queryAccAccuBalance(String account, Date endInstDate);
    /**
     * <b>功能介绍：</b>查询计息账户的利率调整记录<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate,
                                                   Integer[] corpIds, String account);
    /**
     * <b>功能介绍：</b>查询计息账户的余额区间调整记录<br>
     * <b>日期：</b>2017年7月6日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    List<AccInstrateZoneBean> queryInstZoneAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds);
    /**
     * <b>功能介绍：</b>计算利息<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    AccInterestInfo calcInterest(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>暂存利息<br>
     * <b>日期：</b>2017年7月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void saveTempInterest(AccInterestInfo accInterestInfo, String netId, ICurrentUser userBean, String generateMode);
    /**
     * <b>功能介绍：</b>冲预提<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void rushPredrawn(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>根据利息清单生成预提凭证<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void processPredrawnVoucher(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>根据利息清单生成结息凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void processInterestVoucher(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>利息预提数据转移<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void processInterestDtls(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>构造展示计息凭证<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void createInterestVoucher(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>构造展示预提凭证<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void createPredrawVoucher(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>更新暂存的预提<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    void savePredrawRushDtl(AccInterestInfo accInterestInfo);
    /**
     * <b>功能介绍：</b>查询待冲销预提信息<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<FcPredrawDtl> queryUnRushPredrawDtls(String netCode, String accTypeCode, String curCode, Date endDate);
    /**
     * <b>功能介绍：</b>设置记账人<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void setAccbookTallyCode(String userCode);
    /**
    * <br>
    * <b>功能：</b> 更新积数的计息日期(不能是已计息的数据) <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    void updateAccuBalancesInterestSign(String businessId, List<String> ids, Date interestTime);
}
