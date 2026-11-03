package com.bytter.biz.inneraccount.vo.v1;


import com.bytter.biz.inneraccount.vo.BisCorpPoolOverdraftVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("单位归集透支金额数据")
@Data
public class BisCorpPoolOverdraftPageVO {

    private BisCorpPoolOverdraftVO bisCorpPoolOverdraft;
}
