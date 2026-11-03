package com.bytter.biz.finacial.interest.vo;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>利息处理结果<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月5日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class InterestResult implements Serializable {
    private AccInterestInfo accInterestInfo;
    public AccInterestInfo getAccInterestInfo() {
        return accInterestInfo;
    }
    public void setAccInterestInfo(AccInterestInfo accInterestInfo) {
        this.accInterestInfo=accInterestInfo;
    }
}
