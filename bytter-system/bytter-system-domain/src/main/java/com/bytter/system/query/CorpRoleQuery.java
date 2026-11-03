package com.bytter.system.query;
import lombok.Data;
import java.util.List;
@Data
public class CorpRoleQuery {
    private String corpId;
    private List<String> roleIds;
    private String type;
    private String resourceContent;
}
