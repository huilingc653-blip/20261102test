package com.bytter.system.api;
import com.bytter.core.dict.DictInDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.dto.DictGroupDTO;
import java.util.List;
import java.util.Map;
/**
 * 字典管理接口类
 *
 * @author zhoul
 * @date 2017/5/15
 */
public interface IDictService {
    /**
     * 根据字典组code获取字段组信息
     *
     * @param groupCode 字典组code
     * @return
     */
    DictGroupDTO get(String groupCode);
    /**
     * 根据字典组code获取所属字典集合
     *
     * @param groupCode 字典组code
     * @return
     */
    List<DictDTO> list(String groupCode);
    /**
     * 根据字典组code获取所属字典Map(code和名称为Key)
     *
     * @param groupCode 字典组code
     * @return
     */
    Map<String, DictDTO> listMap(String groupCode);
    /**
     * 根据字典组code批量获取所属字典Map(code为Key)。
     *
     * @param groupCode 字典组Code。
     * @return 字典数据。
     */
    Map<String,Map<String,DictDTO>> listDictMap(String... groupCode);

    Map<String, Map<String, DictInDTO>> listDictMap(List<String> groupCode);
    /**
     * 根据字典组code和字典code获取字典名称
     *
     * @param groupCode 字典组code
     * @param code      字典code
     * @return 字典名称
     */
    String get(String groupCode, Object code);
    /**
     * 根据字典组code和字典名称获取字典code
     *
     * @param groupCode 字典组code
     * @param name      字典名称
     * @return 字典code
     */
    String getCodeByName(String groupCode, Object name);
    /**
     * 根据sql脚本查询
     *
     * @param sql sql脚本
     * @return
     */
    List<Map<String, Object>> getExtendTableData(String sql);
    /**
     * 根据字典组code获取字典名称集合
     *
     * @param typeGroupCode 字典组code
     * @return 字典名称集合
     */
    List<String> listDictNameByDictGroup(String typeGroupCode);
    /**
     * 根据字典组code和字典代码获取字典name
     *
     * @param groupCode 字典组code
     * @param name      字典代码
     * @return 字典名称
     */
    String getByName(String groupCode, String code);

    /**
     * 根据字典组code和 TYPE_CODE
     * @param typeCodes
     * @param groupCode
     * @return
     */
    Map<String, String> getNameByTypeCodesAndTypeGroupCode(List<String> typeCodes, String groupCode);
}
