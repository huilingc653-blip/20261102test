package com.bytter.poi.excel.imports.sax.parse;
import com.bytter.poi.excel.entity.sax.SaxReadCellEntity;
import java.util.List;
/**
 * Created by zhouj on 2017/6/2.
 */
public interface ISaxRowRead {
    <T> List<T> getList();
    void parse(int var1, List<SaxReadCellEntity> var2);
}
