

/**
 * 判断是否为空 true：空 false:不为空
 * @param data
 */
function isNull(data){
	return (data == "" || data == undefined || data == null) ? true : false;
}
/**
 * 导入还款计划(其他)
 */
function addImport(o,surplusPayMoney) {
	var repayAmount=o.repayMoney+o.repayInterest
	var html='<tr>';
	html+='<td><input type="text" id="securityIssueCode_'+rowNum+'" name="detailDtl['+rowNum+'].securityIssueCode" value="'+$("#securityIssueCode").val()+'" readonly="readonly" class="layui-input" /></td>';
	html+='<td><input name="detailDtl['+rowNum+'].repayDate" value="'+o.repayDate+'" data-biz-name="repayDate_'+rowNum+'" id="repayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
	html+='<td><input name="detailDtl['+rowNum+'].planRepayDate" value="'+o.planRepayDate+'" data-biz-name="planRepayDate_'+rowNum+'" id="planRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';

	html+='<td><input divName="repayMoney" value="'+o.repayMoney+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayMoney_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repayMoney_'+rowNum+'" data-min="0" data-max="'+maxrepayMoney+'" data-name="detailDtl['+rowNum+'].repayMoney" /></td> ';
	html+='<td><input divName="repayInterest" value="'+o.repayInterest+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayInterest_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repayInterest_'+rowNum+'" data-min="0"  data-name="detailDtl['+rowNum+'].repayInterest" /></td> ';
	html+='<td><input disabled divName="repayAmount" value="'+repayAmount+'" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayAmount_'+rowNum+'"  lay-verify="required" data-biz-name="repayAmount_'+rowNum+'" data-min="0"  data-name="detailDtl['+rowNum+'].repayAmount" /></td> ';
	html+='<td><input disabled divName="remainMoney" value="'+surplusPayMoney+'" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="remainMoney_'+rowNum+'" type="text" data-decimal="2" id="remainMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].remainMoney" /></td>';

	html+='<td class="textRight"><input type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isRepay" value="1" /></td>';
	html+='<td><input name="detailDtl['+rowNum+'].rmk" value="'+o.rmk+'" id="rmk"  class="layui-input" type="text"/></td>';
	html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
	html+='</tr>';
	$("#planDetailTB tbody").append(html);
	addFun();
	sumMoney();
};
    /**
     * 新增还款计划(其他)
     */
	function add(){

        var html='<tr>';
        html+='<td><input type="text" id="securityIssueCode_'+rowNum+'" name="detailDtl['+rowNum+'].securityIssueCode" value="'+$("#securityIssueCode").val()+'" readonly="readonly" class="layui-input" /></td>';
        html+='<td><input name="detailDtl['+rowNum+'].repayDate" data-biz-name="repayDate_'+rowNum+'" id="repayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';
		html+='<td><input name="detailDtl['+rowNum+'].planRepayDate" data-biz-name="planRepayDate_'+rowNum+'" id="planRepayDate_'+rowNum+'" type="text" class="layui-input bt-biz-dateType"  data-type="date" lay-verify="required" /></td>';

		html+='<td><input divName="repayMoney" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayMoney_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repayMoney_'+rowNum+'" data-min="0" data-max="'+maxrepayMoney+'" data-name="detailDtl['+rowNum+'].repayMoney" /></td> ';
		html+='<td><input divName="repayInterest" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayInterest_'+rowNum+'" data-callback="bt.calMoney" lay-verify="required" data-biz-name="repayInterest_'+rowNum+'" data-min="0"  data-name="detailDtl['+rowNum+'].repayInterest" /></td> ';
		html+='<td><input disabled divName="repayAmount" class="layui-input bt-input-inline  bt-biz-moneyType"  type="text" data-decimal="2" id="repayAmount_'+rowNum+'"  lay-verify="required" data-biz-name="repayAmount_'+rowNum+'" data-min="0"  data-name="detailDtl['+rowNum+'].repayAmount" /></td> ';
		html+='<td><input disabled divName="remainMoney" class="layui-input bt-input-inline  bt-biz-moneyType" data-biz-name="remainMoney_'+rowNum+'" type="text" data-decimal="2" id="remainMoney_'+rowNum+'" data-min="0" data-name="detailDtl['+rowNum+'].remainMoney" /></td>';

        html+='<td class="textRight"><input type="checkbox" lay-skin="primary" name="detailDtl['+rowNum+'].isRepay" value="1" /></td>';
        html+='<td><input name="detailDtl['+rowNum+'].rmk" id="rmk"  class="layui-input" type="text"/></td>';
        html+='<td><button data-type="del" type="button" class="layui-btn layui-btn-mini"><i class="layui-icon">&#xe640;</i>'+bt.lang("common.button.delete")+'</button></td>';
        html+='</tr>';
        $("#planDetailTB tbody").append(html);
        addFun();
		sumMoney();
    };


/**
 * 计算剩余金额，占用利率
 * lastsecurityIssueCodeIdId 上一次
 * securityIssueCode_id    本次
 */
function calculation(lastsecurityIssueCodeId, securityIssueCode_id){
	var id = securityIssueCode_id.split("_")[1];
	//缴付金额
	var repayMoney = moneyType.get("repayMoney_"+id).val();
	if(surplusPayMoney <= 0){
        moneyType.get("repayMoney_"+id).val(surplusPayMoney);
        moneyType.get("remainMoney_"+id).val(0);
	    return;
    }
	//剩余本金
	if(Number(surplusrepayMoney) <= Number(repayMoney)){
		moneyType.get("repayMoney_"+id).val(surplusPayMoney);
		repayMoney = Number(surplusPayMoney);
    	moneyType.get("remainMoney_"+id).val(0);
    	surplusPayMoney = 0;
	}else{
		surplusPayMoney = (surplusPayMoney-Number(repayMoney)).toFixed(2);
		moneyType.get("remainMoney_"+id).val(surplusPayMoney);
	}
	//moneyType.get("repaymentIst_"+id).val(repaymentIst);
}


/**
 * 根据本次缴付金额算出剩余缴付金额，本次占比（其他：循环每一行，之后调用calculation方法计算） */
function MoneyCalculation(){
	var trs = $("#planDetailTB").children("tbody").children("tr");
	var lastsecurityIssueCodeId="-1";//上一笔还款本金id
	// 遍历行数据
	trs.each(function(index, tr) {

		var securityIssueCode_id="";//融资编码
		var tds = $(tr).find("td");
		tds.each(function(i, td) {
			var $input = $(td).find("input");
			if ($input.length) {
				if(!isNull($input.attr("id"))&&$input.attr("id").indexOf("securityIssueCode")>=0){
					securityIssueCode_id = $input.attr("id");
				}
			}
		}
		)
		var isPay=$(tr).find("td[name^='isPay']").find("input").val();
		if(isPay=="1"){
			calculation(lastsecurityIssueCodeId, securityIssueCode_id);
			lastsecurityIssueCodeId = securityIssueCode_id;
		}else{
			lastsecurityIssueCodeId = securityIssueCode_id;
		}
	})
	sumMoney();
}


/**
 * 各金额的合计
 */
function sumMoney(){
	var issueMoney=$("#issueMoney").val();
	//计算本次缴付金额
	var repayMoneySum=0;
	var repayInterestSum=0;
	var repayAmountSum=0;
	$("input[divName^='repayMoney']").each(function(i){
		repayMoneySum = bt.money.add(repayMoneySum, $(this).val());
	});
	$("input[divName^='repayInterest']").each(function(i){
		repayInterestSum = bt.money.add(repayInterestSum, $(this).val());
	});
	$("input[divName^='repayAmount']").each(function(i){
		repayAmountSum = bt.money.add(repayAmountSum, $(this).val());
	});
	moneyType.get("repayMoneySum").val(repayMoneySum);
	moneyType.get("repayInterestSum").val(repayInterestSum);
	moneyType.get("repayAmountSum").val(repayAmountSum);
	maxrepayMoney=moneyFun.sub(parseFloat(issueMoney),parseFloat(repayMoneySum));//最大缴付金额
}




