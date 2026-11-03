package com.bytter.workflow.domain.entity;


import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.workflow.domain.util.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Created by zhoul on 2017/7/19.
 * ----添加支持批量撤回字段   lifh  2018-11-22
 * ----添加是否是移动审批字段 chenlw 2020-03-03
 * ----添加已处理页面跳转Url geyq 2022-01-17
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("act_extend_category")
public class FlowCategory extends TreeEntity implements Serializable  {
    private String parentId;
    private String name;
    private String type;
    private String cateKey;
    private String url;
    private Integer sort;
    private String mobileUrl;
    private String notificationId;
    /**
     * 必须挂审批、0不是（默认），1是
     */
    private boolean mustApprove;
    /**
     * 业务：决定了业务流程列表页是否有撤回功能
     */
    private boolean batchRevokeAble;
    /**
     * 菜单ID
     */
    private String menuId;
    private String menuName;
    /**
     * 是否是移动审批：值为0时表示不是；值为1或空置表示是（默认）
     */
    private boolean mobileAveAble;
    /**
     * 资源值
     */
    private String resourceContent;
    /**
     * 流程分类所属业务系统服务ID 消息通知使用
     */
    private String serviceId;
    
    /**
     * 是否是移动审批：值为0时表示不是；值为1或空置表示是（默认）
     */
    private transient Integer mobileApprovalFlag;

    private String tenantId;
	
//  @Transient
//  private  List<FlowCategory> children;
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String updateBy;
    @TableField(fill = FieldFill.INSERT)
    protected Date createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateDate;
    
    
    
}
