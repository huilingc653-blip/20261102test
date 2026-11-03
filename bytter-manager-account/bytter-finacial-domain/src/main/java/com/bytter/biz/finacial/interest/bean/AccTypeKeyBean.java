package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
/**
 * <br>
 * <b>类功能：</b>账户信息key<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月6日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
public class AccTypeKeyBean implements Serializable {
	private String netId;
    private String curId;
    private String accType;
    public AccTypeKeyBean(String netId, String curId, String accType) {
        this.netId = netId;
        this.curId = curId;
        this.accType = accType;
    }
    
    public String getnetId() {
        return netId;
    }
    public void setnetId(String netId) {
        this.netId = netId;
    }
    public String getcurId() {
        return curId;
    }
    public void setcurId(String curId) {
        this.curId = curId;
    }
    public String getaccType() {
        return accType;
    }
    public void setaccType(String accType) {
        this.accType = accType;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accType == null) ? 0 : accType.hashCode());
        result = prime * result + ((curId == null) ? 0 : curId.hashCode());
        result = prime * result + ((netId == null) ? 0 : netId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AccTypeKeyBean other = (AccTypeKeyBean) obj;
        if (accType == null) {
            if (other.accType != null)
                return false;
        } else if (!accType.equals(other.accType))
            return false;
        if (curId == null) {
            if (other.curId != null)
                return false;
        } else if (!curId.equals(other.curId))
            return false;
        if (netId == null) {
            if (other.netId != null)
                return false;
        } else if (!netId.equals(other.netId))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "AccTypeKeyBean [netId=" + netId + ", curId=" + curId + ", accType=" + accType + "]";
    }
}
