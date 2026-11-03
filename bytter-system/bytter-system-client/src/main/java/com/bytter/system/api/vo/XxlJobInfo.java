package com.bytter.system.api.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
/**
 * XXL定时任务对象。
 *
 * @author zhaohc
 */
@Data
@Accessors(chain = true)
public class XxlJobInfo implements Serializable{
    /**
     * 任务id
     */
    private int id;
    /**
     * 执行器主键ID
     */
    private int jobGroup;
    /**
     * job描述
     */
    private String jobDesc;
    /**
     * 负责人
     */
    private String author;
    /**
     * 调度类型
     */
    private String scheduleType;
    /**
     * 调度配置，值含义取决于调度类型
     */
    private String scheduleConf;
    /**
     * 执行器，任务Handler名称
     */
    private String executorHandler;
    /**
     * 调度过期策略
     */
    private String misfireStrategy;
    /**
     * 执行器路由策略
     */
    private String executorRouteStrategy = "ROUND";
    /**
     * 执行器，任务参数
     */
    private String executorParam;
    /**
     * 阻塞处理策略
     */
    private String executorBlockStrategy;
    

   	private int executorTimeout;     		// 任务执行超时时间，单位秒
   	
   	private int executorFailRetryCount;		// 失败重试次数
   	
   	private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
   	private String glueSource;		// GLUE源代码
   	private String glueRemark;		// GLUE备注
   	private Date glueUpdatetime;	// GLUE更新时间

   	private String childJobId;		// 子任务ID，多个逗号分隔

   	private int triggerStatus;		// 调度状态：0-停止，1-运行
   	private long triggerLastTime;	// 上次调度时间
   	private long triggerNextTime;	// 下次调度时间

}

