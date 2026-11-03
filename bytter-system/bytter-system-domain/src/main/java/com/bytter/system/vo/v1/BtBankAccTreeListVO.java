package com.bytter.system.vo.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BtBankAccTreeListVO implements java.io.Serializable{

    public String netId;
    public String corpId;
    public String curId;
    public String poolSign;
    public List<AccRelationTreeVO> treeList;
}
