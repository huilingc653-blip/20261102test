package com.bytter.core.common.page;
import java.io.Serializable;
/**
 * 提供BaseDao层获取页面接口
 *
 * @author zhoul
 * @since 2017/9/18.
 */
public interface IPagePara extends Serializable {
    /**
     * 显示第几页
     *
     * @return 显示第几页
     */
    Integer getPageNumber();
    /**
     * 每页的大小
     *
     * @return 每页的大小
     */
    Integer getPageSize();
    /**
     * <p>
     * 设置每页的大小
     * </p>
     * <b>新增：</b>chenlw 2019-01-07
     *
     * @param pageSize
     *            页面大小
     */
    void setPageSize(Integer pageSize);
    String getCurrentUserId();
}
