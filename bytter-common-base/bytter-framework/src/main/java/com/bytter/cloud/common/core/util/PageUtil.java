package com.bytter.cloud.common.core.util;

import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.PageResult;

import java.util.List;

/**
 * 手动分页工具
 *
 * @author loveCamille
 * @date 2025-08-20 18:06:41
 */
public class PageUtil {

    private PageUtil(){}

    /**
     * 手动分页
     * @author loveCamille
     * @param list 数据
     * @param page 当前页
     * @param pageSize 每页大小
     * @return {@link IPageResult<T>} 分页结果
    */
    public static <T> IPageResult<T> page(List<T> list, Integer page, Integer pageSize) {
        if (list == null || list.isEmpty()) {
            return new PageResult<>();
        }

        int total = list.size();
        int pages = (int) Math.ceil((double) total / pageSize);

        // 修正当前页，避免越界
        if (page < 1) {
            page = 1;
        }
        if (page > pages) {
            page = pages;
        }

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<T> pageList = list.subList(fromIndex, toIndex);

        return PageResult.of(page, list.size(), pageList);
    }

    public static <T> IPageResult<T> pageByStart(List<T> list, int index, int pageSize) {
        if (list == null || list.isEmpty()) {
            return new PageResult<>();
        }

        return page(list, (index / pageSize) + 1, pageSize);
    }

}
