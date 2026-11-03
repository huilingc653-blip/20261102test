package com.bytter.system.velocity.tags;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.dto.DictDTO;
import org.apache.velocity.tools.config.DefaultKey;
import java.util.List;
@DefaultKey("dictTag")
public class DictTag {
    private final IDictService dictService;
    public DictTag() {
        this.dictService = ApplicationContextUtil.getContext().getBean("dictServiceI", IDictService.class);
    }
    public List<DictDTO> list(String dictGroup) {
        return dictService.list(dictGroup);
    }
    public String get(String groupCode, Object value) {
        return dictService.get(groupCode, value);
    }
    public String getValue(String groupCode, Object name) {
        return dictService.getCodeByName(groupCode, name);
    }
}
