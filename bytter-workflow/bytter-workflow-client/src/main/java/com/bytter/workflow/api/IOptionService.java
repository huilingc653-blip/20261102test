package com.bytter.workflow.api;
import com.bytter.workflow.dto.OptionDTO;
/**
 * @author liyh
 * @date 2021/08/27
 */
public interface IOptionService {
    /**
    * <br>
    * <b>功能：</b> 查询移动端的模版消息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/8/27 0027 <br>
    */
    OptionDTO getMessageTemplateByCategory(String categoryId);
}
