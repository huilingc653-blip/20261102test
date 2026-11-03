package com.bytter.system.api.dto;
import lombok.Data;
import java.io.Serializable;
/**
 * Created by zuoy on 2017/8/15.
 */
@Data
public class RegionDTO implements Serializable {
    private String id;
    private String name;
    private Integer level;
    private String parentId;
    private Integer bankProv;
    private Integer bankCity;
    private String provName;
}
