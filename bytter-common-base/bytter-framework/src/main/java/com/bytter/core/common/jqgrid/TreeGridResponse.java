package com.bytter.core.common.jqgrid;
import com.bytter.core.common.page.IPageAdapter;
import com.bytter.core.common.page.IPagePara;
import com.bytter.core.common.page.IPageResult;
import java.io.Serializable;
import java.util.List;
/**
 * Created by zhoul on 2017/8/10.
 */
public class TreeGridResponse<T extends Serializable> extends JqGridResponse implements IPageAdapter, Serializable {
    public TreeGridResponse() {
    }
    public TreeGridResponse(List<T> rows) {
        super(rows);
    }
    @Override
    public void convert(IPagePara pagePara, IPageResult pageResult) {
        setRows(pageResult.getList());
    }
}
