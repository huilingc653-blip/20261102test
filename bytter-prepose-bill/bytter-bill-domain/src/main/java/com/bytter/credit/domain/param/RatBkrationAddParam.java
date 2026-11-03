package com.bytter.credit.domain.param;
import lombok.Data;
import java.util.Date;
/**
 * 授信追加信息查询
 */
@Data
public class RatBkrationAddParam {
    /** 授信编号 */
    private String ratCode;
    /**
     * 授信合同编号
     */
    private String contractNo;
    /**
     * 授信开始日期(rang)
     */
    private Date gBeginDate;
    private Date lBeginDate;
    /**
     * 授信到期日期(rang)
     */
    private Date gEndDate;
    private Date lEndDate;
}
