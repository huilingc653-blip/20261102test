package com.bytter.system.api.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * Created by zhoul on 2017/5/15.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictDTO implements Serializable {
    private String id;
    private String name;
    private String code;
    private String dictGroupId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDictGroupId() {
        return dictGroupId;
    }
    public void setDictGroupId(String dictGroupId) {
        this.dictGroupId = dictGroupId;
    }

    public static final DictDTO EMPTY = new DictDTO();
}
