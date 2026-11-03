package com.bytter.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BtBankAccTreeVO implements java.io.Serializable{

    public String netId;
    public String corpId;
    public String curId;
    public String poolSign;
    public List<TreeVO> treeList;
}
