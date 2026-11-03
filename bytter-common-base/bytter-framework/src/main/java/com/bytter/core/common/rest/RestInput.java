package com.bytter.core.common.rest;
import com.bytter.core.base.persistence.BasePersistence;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IParameter;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.page.ParamImpl;
/**
 *
 * Created by zhoul on 2017/7/25.
 */
public class RestInput extends BasePersistence implements IParameterInput, IPagePara {
    @Override
    public Integer getPageNumber() {
        Integer pageNumber = (Integer) get("pageNumber");
        if (pageNumber == null) {
            return 1;
        }
        return pageNumber;
    }
    @Override
    public Integer getPageSize() {
        Integer pageSize = (Integer) get("pageSize");
        if (pageSize == null) {
            return 15;
        }
        return pageSize;
    }
    /**
     * <p>
     * 设置每页的大小
     * </p>
     * <b>新增：</b>chenlw 2019-01-07
     *
     * @param pageSize
     *            页面大小
     */
    @Override
    public void setPageSize(Integer pageSize) {
        put("pageSize", pageSize);
    }
    @Override
    public IParameter getParameter() {
        return new ParamImpl(this);
    }
}
