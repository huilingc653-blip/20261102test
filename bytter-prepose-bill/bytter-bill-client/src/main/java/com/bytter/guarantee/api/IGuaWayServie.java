package com.bytter.guarantee.api;
import com.bytter.guarantee.dto.GuaWayDTO;
import java.util.List;
import java.util.Map;
/**
 * 担保方式接口
 *
 * @author luxy
 */
public interface IGuaWayServie {
    /**
     * 根据担保方式code获取担保方式名称
     *
     * @param codes 担保方式code
     * @return 担保方式名称
     */
    String getGuaWayNameByCodes(String codes);
    /**
     * 查询所有担保方式
     *
     * @return 担保方式DTO集合
     */
    List<GuaWayDTO> getAllGuaWay();
    /**
     * 根据担保方式id获取担保方式名称
     *
     * @param ids 担保方式id
     * @return 担保方式名称
     */
    String getGuaWayNameByIds(String ids);
    /**
     * todo 开放为暴露接口 根据担保code查询担保方式
     *
     * @param code
     * @param id
     * @return
     */
    List<GuaWayDTO> findAllByCode(String code, String id);
    /**
     * 根据担保方式id集合获取担保方式名称
     *
     * @param ids 担保方式id
     * @return key=id,value=object 对象集合
     */
    Map<String, GuaWayDTO> getGuaWayNameByIds(List<String> ids);
}
