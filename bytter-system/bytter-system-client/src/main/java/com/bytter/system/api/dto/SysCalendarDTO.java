package com.bytter.system.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日历维护DTO
 *
 * @author fangcf
 * @date 2025-10-23
 */
@Data
public class SysCalendarDTO implements Serializable {
    /**
     * calendarNum
     */
    private Integer calendarNum;
    /**
     * 工作日标志：1：工作日，0：非工作日
     */
    private Integer workDaySign;
    /**
     * 日历日期
     */
    private Date createDate;
    /**
     * 地点
     */
    private String countryId;
    /**
     * 币别
     */
    private String curId;
}
