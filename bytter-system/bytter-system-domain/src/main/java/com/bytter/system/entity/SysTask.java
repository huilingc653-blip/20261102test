package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: SYS_TASK
 * @date 2017-06-19 18:44:40
 */
@Entity
@Table(name = "SYS_TASK", schema = "")
@SuppressWarnings("serial")
public class SysTask extends IdEntity implements java.io.Serializable {
    /**
     * 任务名称
     */
    private String taskname;
    /**
     * 状态
     */
    private Integer active;
    /**
     * 是否删除
     */
    private Integer deletedsw;
    /**
     * 任务类型
     */
    private Integer type;
    /**
     * 任务命令
     */
    private String command;
    /**
     * 任务参数
     */
    private String arguments;
    /**
     * 执行方式
     */
    private Integer period;
    /**
     * 秒参数
     */
    private String second;
    /**
     * 分钟参数
     */
    private String minute;
    /**
     * 小时参数
     */
    private String hour;
    /**
     * 日期参数
     */
    private String days;
    /**
     * 月份参数
     */
    private String month;
    /**
     * 星期参数
     */
    private String week;
    /**
     * 开始日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdtm;
    /**
     * 结束日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddtm;
    /**
     * 执行任务的角色
     */
    private String taskowner;
    /**
     * 提醒邮件
     */
    private String emailReminder;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  任务名称
     */
    @Column(name = "TASKNAME", nullable = true, length = 100)
    public String getTaskname() {
        return this.taskname;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  任务名称
     */
    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  状态
     */
    @Column(name = "ACTIVE", nullable = true, length = 2)
    public Integer getActive() {
        return this.active;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  状态
     */
    public void setActive(Integer active) {
        this.active = active;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  是否删除
     */
    @Column(name = "DELETEDSW", nullable = true, length = 2)
    public Integer getDeletedsw() {
        return this.deletedsw;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  是否删除
     */
    public void setDeletedsw(Integer deletedsw) {
        this.deletedsw = deletedsw;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  任务类型
     */
    @Column(name = "TYPE", nullable = true, length = 5)
    public Integer getType() {
        return this.type;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  任务类型
     */
    public void setType(Integer type) {
        this.type = type;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  任务命令
     */
    @Column(name = "COMMAND", nullable = true, length = 120)
    public String getCommand() {
        return this.command;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  任务命令
     */
    public void setCommand(String command) {
        this.command = command;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  任务参数
     */
    @Column(name = "ARGUMENTS", nullable = true, length = 1024)
    public String getArguments() {
        return this.arguments;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  任务参数
     */
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  执行方式
     */
    @Column(name = "PERIOD", nullable = true, length = 2)
    public Integer getPeriod() {
        return this.period;
    }
    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  执行方式
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  秒参数
     */
    @Column(name = "SECOND", nullable = true, length = 50)
    public String getSecond() {
        return this.second;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  秒参数
     */
    public void setSecond(String second) {
        this.second = second;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  分钟参数
     */
    @Column(name = "MINUTE", nullable = true, length = 50)
    public String getMinute() {
        return this.minute;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  分钟参数
     */
    public void setMinute(String minute) {
        this.minute = minute;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  小时参数
     */
    @Column(name = "HOUR", nullable = true, length = 50)
    public String getHour() {
        return this.hour;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  小时参数
     */
    public void setHour(String hour) {
        this.hour = hour;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  日期参数
     */
    @Column(name = "DAYS", nullable = true, length = 50)
    public String getDays() {
        return this.days;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  日期参数
     */
    public void setDays(String days) {
        this.days = days;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  月份参数
     */
    @Column(name = "MONTH", nullable = true, length = 50)
    public String getMonth() {
        return this.month;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  月份参数
     */
    public void setMonth(String month) {
        this.month = month;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  星期参数
     */
    @Column(name = "WEEK", nullable = true, length = 50)
    public String getWeek() {
        return this.week;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  星期参数
     */
    public void setWeek(String week) {
        this.week = week;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  开始日期时间
     */
    @Column(name = "STARTDTM", nullable = true)
    public Date getStartdtm() {
        return this.startdtm;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  开始日期时间
     */
    public void setStartdtm(Date startdtm) {
        this.startdtm = startdtm;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  结束日期时间
     */
    @Column(name = "ENDDTM", nullable = true)
    public Date getEnddtm() {
        return this.enddtm;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  结束日期时间
     */
    public void setEnddtm(Date enddtm) {
        this.enddtm = enddtm;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  执行任务的角色
     */
    @Column(name = "TASKOWNER", nullable = true, length = 32)
    public String getTaskowner() {
        return this.taskowner;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  执行任务的角色
     */
    public void setTaskowner(String taskowner) {
        this.taskowner = taskowner;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  提醒邮件
     */
    @Column(name = "EMAIL_REMINDER", nullable = true, length = 50)
    public String getEmailReminder() {
        return this.emailReminder;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  提醒邮件
     */
    public void setEmailReminder(String emailReminder) {
        this.emailReminder = emailReminder;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人
     */
    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
