package com.bytter.cloud.auth.persistence;
import java.util.Date;
import lombok.*;
/**
 * 登陆日志信息表
 *
 * @author mfs 2022-08-10
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogLogin {
	
    private String id;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 最后修改人
     */
    private String updateBy;
    /**
     * 最后修改时间
     */
    private Date updateTime;
    /**
     * 编号
     */
    private String seqNo;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * ip地址
     */
    private String ipAddress;
    /**
     * 设备信息
     */
    private String device;
    /**
     * 登陆状态 0失败 1成功
     */
    private Integer status;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 登陆地点
     */
    private String address;
}
