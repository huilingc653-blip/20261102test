package com.bytter.core.common.page;
import lombok.Data;
/**
 * 前端查询传递的分页参数
 *
 */
@Data
public class PageParam  {
    protected int pageNo;
    protected int pageSize;
    /**
     * 兼容老系统字段 等价于pageNo前端修改后统一去掉
     */
    protected int start;
    protected int draw;
    public PageParam() {
        this.start = 1;
        this.pageNo = 1;
        this.pageSize = 10;
    }
    public PageParam(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.start = pageNo;
        this.pageSize = pageSize;
    }
    public static PageParam of(int pageNo, int pageSize) {
        return new PageParam(pageNo, pageSize);
    }
}
