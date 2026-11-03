package com.bytter.system.api.vo;
import java.io.Serializable;
import java.util.List;
/**
 * Created by zhangxb on 2017/6/14.
 */
public class FcInitAccListVO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3079916656586003776L;
	/**状态*/
    private Integer status;
    private List<FcInitAccVO> fcInitAccs;
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public List<FcInitAccVO> getFcInitAccs() {
        return fcInitAccs;
    }
    public void setFcInitAccs(List<FcInitAccVO> fcInitAccs) {
        this.fcInitAccs = fcInitAccs;
    }
}
