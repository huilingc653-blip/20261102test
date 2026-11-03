package com.bytter.system.api.dto;
import lombok.Data;
import java.util.Date;
@Data
/**
 * @Title: Entity
 * @Description: SYS_CA_INFO
 * @author onlineGenerator
 * @date 2017-10-26 16:56:53
 * @version 1.0
 *
 */
public class SysCaInfoDTO implements java.io.Serializable {
    private String id;
    /**
     * 产生编码日期
     */
    private Date encryDate;
    /**
     * 加密随机数
     */
    private String random;
    /**
     * 编码源
     */
    private String encrySource;
    /**
     * CA编码
     */
    private String caCoding;
}
