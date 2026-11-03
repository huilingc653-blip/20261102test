package com.bytter.biz.inneraccount.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
/**
 * @Title: Entity
 * @Description: 银行账号关系策略维护VO
 * @author onlineGenerator
 * @date 2017-06-20 15:20:33
 * @version 1.0
 *
 */
@Setter
@Getter
@ApiModel("银行账号关系策略维护VO")
public class BisAccGatherPolicyTreeVO implements Serializable {

	/**科目ID*/
	@ApiModelProperty("科目ID")
	private String id;
	/**科目父ID*/
	@ApiModelProperty("科目父ID")
	private String fatherId;
	/**调拨策略代码*/
	@ApiModelProperty("调拨策略代码")
	private String gatherPolicyCode;
	/**调拨策略名称*/
	@ApiModelProperty("调拨策略名称")
	private String gatherPolicyName;
	/**账号*/
	@ApiModelProperty("账号")
	private String bankAcc;
	/**账户名称*/
	@ApiModelProperty("账户名称")
	private String accName;
	/**开户行名称*/
	@ApiModelProperty("开户行名称")
	private String bankName;

	/**调拨策略ID*/
	@ApiModelProperty("调拨策略ID")
	private String gatherPolicyId;
	/**父账号ID*/
	@ApiModelProperty("父账号ID")
	private String prentAccId;
	/**科目ID*/
	@ApiModelProperty("科目ID")
	private String treeId;
	/**科目父ID*/
	@ApiModelProperty("科目父ID")
	private String treePrentId;
	/**币别*/
	@ApiModelProperty("币别")
	private String curId;
	private String curName;

}
