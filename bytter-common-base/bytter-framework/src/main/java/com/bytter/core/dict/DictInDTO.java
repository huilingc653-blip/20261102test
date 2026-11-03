package com.bytter.core.dict;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DictInDTO implements Serializable {
    private String id;
    private String name;
    private String code;
    private String dictGroupId;

    public static final DictInDTO EMPTY = new DictInDTO();
}
